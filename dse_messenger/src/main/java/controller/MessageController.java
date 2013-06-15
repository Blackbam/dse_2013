package controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dao.INotificationDAO;
import dse_domain.DTO.NotificationDTO;
import dse_domain.DTO.OpSlotDTO;
import dse_domain.DTO.ReservationCancelNotificationDTO;
import dse_domain.DTO.ReservationDTO;
import dse_domain.DTO.ReservationFailNotificationDTO;
import dse_domain.DTO.ReservationSuccessNotificationDTO;
import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.Notification;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;
import dse_domain.domain.User;

/**
 * Controller component that handles incoming notification requests and a simple debug list - view to see notifications
 * in the database
 */
@Controller
public class MessageController {

	static final Logger logger = Logger.getLogger(MessageController.class);

	@Autowired
	private INotificationDAO notificationDAO;

	@Autowired(required = false)
	private RabbitTemplate rabbitTemplate;

	/**
	 * Listener method that takes notification requests from the messenger queue and processes them. Depending on the
	 * instance of the incoming DTOs, it takes the information and builds notifications out of it, which get inserted
	 * into the database.
	 * 
	 * @param message
	 */
	public void handleNotificationRequest(Object message) {

		logger.info("Received Message: " + message);

		try {

			if (message instanceof ReservationSuccessNotificationDTO) {
				// success notification from allocator for patient, doctor, hospital
				logger.info("Message is instance of ReservationSuccessNotificationDTO, processing..");
				ReservationSuccessNotificationDTO dto = (ReservationSuccessNotificationDTO) message;

				ReservationDTO reservationRequestDTO = dto.getReservationRequest();
				OpSlotDTO opSlotDTO = dto.getAssignedOpSlot();

				Patient patient = notificationDAO.findPatient(reservationRequestDTO.getPatientID());
				Doctor doctor = notificationDAO.findDoctor(reservationRequestDTO.getDoctorID());
				Hospital hospital = notificationDAO.findHospital(opSlotDTO.getHospitalID());

				processSuccessNotification(reservationRequestDTO, opSlotDTO, patient, doctor, hospital);

			} else if (message instanceof ReservationFailNotificationDTO) {
				// failure notification from allocator for patient, doctor
				logger.info("Message is instance of ReservationFailNotificationDTO, processing..");
				ReservationFailNotificationDTO dto = (ReservationFailNotificationDTO) message;
				ReservationDTO reservationRequestDTO = dto.getReservationRequest();

				Patient patient = notificationDAO.findPatient(reservationRequestDTO.getPatientID());
				Doctor doctor = notificationDAO.findDoctor(reservationRequestDTO.getDoctorID());

				processFailNotification(dto, reservationRequestDTO, patient, doctor);

			} else if (message instanceof ReservationCancelNotificationDTO) {
				// cancellation notification from UI for patient, doctor
				logger.info("Message is instance of ReservationCancellationDTO, processing..");

				ReservationCancelNotificationDTO cancelDTO = (ReservationCancelNotificationDTO) message;

				OpSlot slot = notificationDAO.findOpSlot(cancelDTO.getOpSlotID());

				Patient patient = slot.getReservation().getPatient();
				Doctor doctor = slot.getReservation().getDoctor();

				if (slot.getReservation() != null) {
					processCancellationNotification(slot, patient, doctor);
				}

				logger.info("Cancellation not possible since the opSlot has no reservation");

			} else if (message instanceof NotificationDTO) {
				// generic notification for generic notification requests (currently not used)
				logger.info("Message is instance of NotificationDTO, processing..");

				NotificationDTO dto = (NotificationDTO) message;

				User user = dto.getUser();
				String title = dto.getTitle();
				String content = dto.getContent();

				Notification notification = new Notification(user, title, content);
				notificationDAO.insertNotification(notification);

			} else {
				logger.info("Message of unexpected type");
			}
			logger.info("..processing of message done.");
		} catch (Exception e) {
			// catch exceptions to avoid loops in case something unexpected happened
			logger.error("Exception in message controller: " + e.getMessage());
		}
	}

	/**
	 * Create and insert notification into the database for cancellation received from the UI component.
	 * 
	 * @param slot
	 * @param patient
	 * @param doctor
	 */
	private void processCancellationNotification(OpSlot slot, Patient patient, Doctor doctor) {
		String title = "Stornierung einer Reservierung";
		String content = "Die Operation (Typ: " + slot.getType() + ") des Patienten " + patient.getFirstName() + " "
				+ patient.getLastName() + " im Krankenhaus " + slot.getHospital().getName() + " am " + slot.getDate()
				+ " von " + slot.getStartTimeString() + " bis " + slot.getEndTimeString() + ", mit dem Arzt "
				+ doctor.getTitle() + " " + doctor.getFirstName() + " " + doctor.getLastName()
				+ "wurde vom Arzt storniert!";

		Notification patientNotification = new Notification(patient, title, content);
		Notification doctorNotification = new Notification(doctor, title, content);
		Notification hospitalNotification = new Notification(slot.getHospital(), title, content);

		notificationDAO.insertNotification(doctorNotification);
		notificationDAO.insertNotification(patientNotification);
		notificationDAO.insertNotification(hospitalNotification);
	}

	/**
	 * Create and insert notification into the database for success notification from the allocator component.
	 * 
	 * @param reservationRequestDTO
	 * @param opSlotDTO
	 * @param patient
	 * @param doctor
	 * @param hospital
	 */
	private void processSuccessNotification(ReservationDTO reservationRequestDTO, OpSlotDTO opSlotDTO, Patient patient,
			Doctor doctor, Hospital hospital) {
		String title = "Reservierung erfolgreich";
		String content = "Reservierung fuer die Operation (Typ: " + reservationRequestDTO.getType()
				+ ") des Patienten " + patient.getFirstName() + " " + patient.getLastName() + " im Krankenhaus "
				+ hospital.getName() + " am " + opSlotDTO.getDate() + " von " + opSlotDTO.getStartTime() + " bis "
				+ opSlotDTO.getEndTime() + ", beantragt von " + doctor.getTitle() + " " + doctor.getFirstName() + " "
				+ doctor.getLastName() + ", konnte erfolgreich durchgefuehrt werden!";

		Notification patientNotification = new Notification(patient, title, content);
		Notification doctorNotification = new Notification(doctor, title, content);
		Notification hospitalNotification = new Notification(hospital, title, content);

		notificationDAO.insertNotification(doctorNotification);
		notificationDAO.insertNotification(patientNotification);
		notificationDAO.insertNotification(hospitalNotification);
	}

	/**
	 * Create and insert notification into the database for failure notification from the allocator component.
	 * 
	 * @param dto
	 * @param reservationRequestDTO
	 * @param patient
	 * @param doctor
	 */
	private void processFailNotification(ReservationFailNotificationDTO dto, ReservationDTO reservationRequestDTO,
			Patient patient, Doctor doctor) {
		String title = "Reservierung fehlgeschlagen";
		String content = "Reservierung fuer die Operation (Typ: " + reservationRequestDTO.getType()
				+ ") des Patienten " + patient.getFirstName() + " " + patient.getLastName() + ", beantragt von "
				+ doctor.getTitle() + " " + doctor.getFirstName() + " " + doctor.getLastName()
				+ ", konnte nicht durchgefuehrt werden! Grund: " + dto.getFailureReason();

		Notification patientNotification = new Notification(patient, title, content);
		Notification doctorNotification = new Notification(doctor, title, content);

		notificationDAO.insertNotification(doctorNotification);
		notificationDAO.insertNotification(patientNotification);
	}

	/**
	 * Selects the home view to render by returning its name. The view contains a simple list with the notifications in
	 * the database (for debugging).
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {

		List<String> services = new ArrayList<String>();

		if (rabbitTemplate != null) {
			services.add("RabbitMQ: " + rabbitTemplate.getConnectionFactory().toString());
		}

		List<Notification> nots = notificationDAO.findAllNotifications();
		model.addAttribute("nots", nots);
		model.addAttribute("services", services);

		return "home";
	}

}
