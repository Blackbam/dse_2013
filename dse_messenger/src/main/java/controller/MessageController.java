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
import dse_domain.DTO.ReservationDTO;
import dse_domain.DTO.ReservationFailNotificationDTO;
import dse_domain.DTO.ReservationSuccessNotificationDTO;
import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.Notification;
import dse_domain.domain.Patient;
import dse_domain.domain.User;

@Controller
public class MessageController {
	static final Logger logger = Logger.getLogger(MessageController.class);

	@Autowired
	INotificationDAO notificationDAO;

	@Autowired(required = false)
	RabbitTemplate rabbitTemplate;

	public void handleNotificationRequest(Object message) {

		logger.info("Received Message: " + message);

		try {

			if (message instanceof ReservationSuccessNotificationDTO) {
				logger.info("Message is instance of ReservationSuccessNotificationDTO, processing..");
				ReservationSuccessNotificationDTO dto = (ReservationSuccessNotificationDTO) message;

				ReservationDTO reservationRequestDTO = dto.getReservationRequest();
				OpSlotDTO opSlotDTO = dto.getAssignedOpSlot();

				Patient patient = notificationDAO.findPatient(reservationRequestDTO.getPatientID());
				Doctor doctor = notificationDAO.findDoctor(reservationRequestDTO.getDoctorID());
				Hospital hospital = notificationDAO.findHospital(opSlotDTO.getHospitalID());

				String title = "Reservierung erfolgreich";
				String content = "Reservierung für die Operation des Patienten " + patient.getFirstName() + " "
						+ patient.getLastName() + " im Krankenhaus " + hospital.getName() + " am "
						+ opSlotDTO.getDate() + " von " + opSlotDTO.getStartTime() + " bis " + opSlotDTO.getEndTime()
						+ ", beantragt von " + doctor.getTitle() + " " + doctor.getFirstName() + " "
						+ doctor.getLastName() + ", konnte erfolgreich durchgeführt werden!";

				Notification patientNotification = new Notification(patient, title, content);
				Notification doctorNotification = new Notification(doctor, title, content);
				Notification hospitalNotification = new Notification(hospital, title, content);

				notificationDAO.insertNotification(doctorNotification);
				notificationDAO.insertNotification(patientNotification);
				notificationDAO.insertNotification(hospitalNotification);

			} else if (message instanceof ReservationFailNotificationDTO) {
				logger.info("Message is instance of ReservationFailNotificationDTO, processing..");
				ReservationFailNotificationDTO dto = (ReservationFailNotificationDTO) message;
				ReservationDTO reservationRequest = dto.getReservationRequest();

				Patient patient = notificationDAO.findPatient(reservationRequest.getPatientID());
				Doctor doctor = notificationDAO.findDoctor(reservationRequest.getDoctorID());

				String title = "Reservierung fehlgeschlagen";
				String content = "Reservierung für die Operation des Patienten " + patient.getFirstName() + " "
						+ patient.getLastName() + ", beantragt von " + doctor.getTitle() + " " + doctor.getFirstName()
						+ " " + doctor.getLastName() + ", konnte nicht durchgeführt werden! Grund: "
						+ dto.getFailureReason();

				Notification patientNotification = new Notification(patient, title, content);
				Notification doctorNotification = new Notification(patient, title, content);

				notificationDAO.insertNotification(doctorNotification);
				notificationDAO.insertNotification(patientNotification);

			} else if (message instanceof NotificationDTO) {
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
			e.printStackTrace();
		}

	}
	

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {

		List<String> services = new ArrayList<String>();

		if (rabbitTemplate != null) {
			services.add("RabbitMQ: " + rabbitTemplate.getConnectionFactory().toString());
		}
		
		List<Notification> nots = notificationDAO.findAllNotifications();

		model.addAttribute("services", services);
		
		return "home";
	}

}
