package controller;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import dao.IReservationDAO;
import dse_domain.DTO.ReservationDTO;
import dse_domain.domain.Doctor;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;
import dse_domain.domain.Reservation;

@Controller
public class ReservationController {
	static final Logger logger = Logger.getLogger(ReservationController.class);

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

				} else {
					logger.info("Reservation not possible.. no free OpSlots found");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
