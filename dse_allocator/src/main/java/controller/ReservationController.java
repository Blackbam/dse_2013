package controller;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import dao.IReservationDAO;
import dse_domain.DTO.OpSlotDTO;
import dse_domain.DTO.ReservationDTO;
import dse_domain.DTO.ReservationFailNotificationDTO;
import dse_domain.DTO.ReservationSuccessNotificationDTO;
import dse_domain.domain.Doctor;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;
import dse_domain.domain.Reservation;

@Controller
public class ReservationController {
	static final Logger logger = Logger.getLogger(ReservationController.class);

	private final String messengerQueue = "messenger";

	@Autowired
	AmqpTemplate amqpTemplate;

	@Autowired
	IReservationDAO reservationDAO;

	public ReservationController() {

	}

	public void handleReservationRequest(Object message) {
		logger.info("Received Message: " + message);

		try {

			if (message instanceof ReservationDTO) {

				logger.info("Message is instance of ReservationDTO, processing..");

				ReservationDTO receivedDTO = (ReservationDTO) message;

				String patientID = receivedDTO.getPatientID();
				String doctorID = receivedDTO.getDoctorID();
				int maxDistance = receivedDTO.getMaxDistance();
				Date startDate = receivedDTO.getDateStart();
				Date endDate = receivedDTO.getDateEnd();
				int minTime = receivedDTO.getMinTime();

				Patient patient = reservationDAO.findPatient(patientID);

				OpSlot foundOpSlot = reservationDAO.findFreeOPSlotInNearHospital(maxDistance, patient, startDate,
						endDate, minTime);

				if (foundOpSlot != null) {
					logger.info("Reservation finished processing.. found OpSlot: " + foundOpSlot.getDateString()
							+ " / " + foundOpSlot.getStartTimeString() + "-" + foundOpSlot.getEndTimeString()
							+ " in hospital: " + foundOpSlot.getHospital().getName());

					Doctor doctor = reservationDAO.findDoctor(doctorID);

					Reservation reservation = new Reservation(doctor, patient);
					foundOpSlot.setReservation(reservation);

					reservationDAO.updateOpSlot(foundOpSlot);

					// sending

//					String notifTitle = "Reservation successful";
//					String notifContent = "Reservierung für die Operation des Patienten " + patient.getFirstName()
//							+ " " + patient.getLastName() + ", durchgeführt " + doctor.getTitle() + " "
//							+ doctor.getFirstName() + " " + doctor.getLastName() + "erfolgreich  "
//							+ foundOpSlot.getDateString() + " / " + foundOpSlot.getStartTimeString() + "-"
//							+ foundOpSlot.getEndTimeString() + " ";

					// NotificationDTO notificationDoctor = new NotificationDTO(doctor,notifTitle,
					// notifContent, true);
					//
					// NotificationDTO notificationPatient = new NotificationDTO(patient,
					// notifTitle, notifContent, true);
					//
					// NotificationDTO notificationHopsital = new
					// NotificationDTO(foundOpSlot.getHospital(), notifTitle, notifContent, true);
					//
					OpSlotDTO opSlotDTO = new OpSlotDTO(foundOpSlot.getHospital().getId(), foundOpSlot.getDateString(),
							foundOpSlot.getStartTimeString(), foundOpSlot.getEndTimeString());
					
					sendSuccessNotification(receivedDTO, opSlotDTO);
					
				} else {
					logger.info("Reservation not possible.. no free OpSlots found");
					
					sendFailNotification(receivedDTO);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void sendFailNotification(ReservationDTO receivedDTO) {
		String failureReason = "No free OpSlots could be found";
		ReservationFailNotificationDTO failNotification = new ReservationFailNotificationDTO(receivedDTO, failureReason);
		amqpTemplate.convertAndSend(messengerQueue, failNotification);
		
		logger.info("Sending to messenger: " +failNotification);
	}

	private void sendSuccessNotification(ReservationDTO receivedDTO, OpSlotDTO opSlotDTO) {
		ReservationSuccessNotificationDTO succsNotification = new ReservationSuccessNotificationDTO(receivedDTO, opSlotDTO);
		amqpTemplate.convertAndSend(messengerQueue, succsNotification);
		
		logger.info("Sending to messenger: " +succsNotification);
	}

	public void sendNotification() {

	}

}
