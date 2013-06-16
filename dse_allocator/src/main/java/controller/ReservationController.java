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
import dse_domain.domain.OpSlot.Type;
import dse_domain.domain.Patient;
import dse_domain.domain.Reservation;

/**
 * Controller component that handles incoming reservation requests.
 */
@Controller
public class ReservationController {

	static final Logger logger = Logger.getLogger(ReservationController.class);

	private final String messengerQueue = "messenger";

	@Autowired
	AmqpTemplate amqpTemplate;

	@Autowired
	IReservationDAO reservationDAO;

	/**
	 * Checks reservation request and make reservation, if possible.
	 * 
	 * @param message
	 */
	public void handleReservationRequest(Object message) {

		logger.info("Received Message: " + message);

		try {

			if (message instanceof ReservationDTO) {

				logger.info("Message is instance of ReservationDTO, processing...");

				ReservationDTO receivedDTO = (ReservationDTO) message;

				String patientID = receivedDTO.getPatientID();
				String doctorID = receivedDTO.getDoctorID();
				int maxDistance = receivedDTO.getMaxDistance();
				Date startDate = receivedDTO.getDateStart();
				Date endDate = receivedDTO.getDateEnd();
				int minTime = receivedDTO.getMinTime();
				Type type = receivedDTO.getType();

				Patient patient = reservationDAO.findPatient(patientID);
				Doctor doctor = reservationDAO.findDoctor(doctorID);

				// main op_slot query - result is an available op_slot nearest the patient
				OpSlot foundOpSlot = reservationDAO.findFreeOPSlotInNearHospital(maxDistance, patient, startDate,
						endDate, minTime, type);

				if (foundOpSlot != null) {
					logger.info("Reservation finished processing.. found OpSlot: " + foundOpSlot.getDateString()
							+ " / " + foundOpSlot.getStartTimeString() + "-" + foundOpSlot.getEndTimeString()
							+ " in hospital: " + foundOpSlot.getHospital().getName());

					// update the found op_slot in the database and send notification request
					Reservation reservation = new Reservation(doctor, patient);
					foundOpSlot.setReservation(reservation);

					reservationDAO.updateOpSlot(foundOpSlot);

					OpSlotDTO opSlotDTO = new OpSlotDTO(foundOpSlot.getHospital().getId(), foundOpSlot.getDateString(),
							foundOpSlot.getStartTimeString(), foundOpSlot.getEndTimeString());

					sendSuccessNotification(receivedDTO, opSlotDTO);
				} else {
					logger.info("Reservation not possible...no free OpSlots were found");
					String failureReason = "Keine freien Op-Slots wurden gefunden.";
					sendFailNotification(receivedDTO, failureReason);
				}

			} else {
				logger.info("Received unexpected message type.");
			}
		} catch (Exception e) {
			// catch exceptions to avoid loops in case something unexpected happened
			logger.error("Exception in reservation controller: " + e.getMessage());
		}

	}

	/**
	 * Sends failure notification request to messenger, with specified failure reason (the notification is then built in
	 * messenger with the information sent).
	 * 
	 * @param receivedDTO
	 * @param failureReason
	 */
	private void sendFailNotification(ReservationDTO receivedDTO, String failureReason) {

		ReservationFailNotificationDTO failNotification = new ReservationFailNotificationDTO(receivedDTO, failureReason);
		amqpTemplate.convertAndSend(messengerQueue, failNotification);

		logger.info("Sending to messenger: " + failNotification);
	}

	/**
	 * Sends success notification request to messenger (the notification is then built in messenger with the information
	 * sent).
	 * 
	 * @param receivedDTO
	 * @param opSlotDTO
	 */
	private void sendSuccessNotification(ReservationDTO receivedDTO, OpSlotDTO opSlotDTO) {
		ReservationSuccessNotificationDTO succsNotification = new ReservationSuccessNotificationDTO(receivedDTO,
				opSlotDTO);
		amqpTemplate.convertAndSend(messengerQueue, succsNotification);

		logger.info("Sending to messenger: " + succsNotification);
	}

}
