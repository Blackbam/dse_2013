package controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dao.IUserInterfaceDAO;
import dse_domain.DTO.ReservationCancelNotificationDTO;
import dse_domain.DTO.ReservationDTO;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;
import dse_domain.domain.Doctor;

@Controller
@RequestMapping(value = "/doctor")
public class DoctorController {
	static final Logger logger = Logger.getLogger(DoctorController.class);

	@Autowired
	AmqpTemplate amqpTemplate;

	@Autowired
	IUserInterfaceDAO uiDAO;

	// Requires DOCTOR id
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String patient(Model model, @RequestParam("id") String id) {

		Doctor doctor = uiDAO.findDoctor(id);

		/* For Debugging: Test Reservations */

		/*
		 * Patient p = mongoTemplate.findOne(new Query(),Patient.class); OpSlot op_slot =
		 * mongoTemplate.findOne(new Query(),OpSlot.class);
		 * 
		 * 
		 * 
		 * if(doctor!=null && p!=null && op_slot!=null && op_slot.getReservation()==null) {
		 * Reservation res = new Reservation(doctor,p); mongoTemplate.save(res);
		 * op_slot.setReservation(res); mongoTemplate.save(op_slot);
		 * logger.debug("Test reservation created"); } else {
		 * logger.debug("Test reservation error"); }
		 */
		// End Debug

		List<OpSlot> opSlots = uiDAO.findAllReservedOpSlotsWithDoctor(doctor);

		for (OpSlot curr : opSlots) {
			logger.debug("found op slot " + curr.getDateString() + " with reservation: "
					+ curr.getReservation().getDoctor() + "/" + curr.getReservation().getPatient());
		}

		model.addAttribute("doctorID", id);
		model.addAttribute("op_slots_this_doctor", opSlots);
		model.addAttribute("sent_reservation", false);

		addStandardOutputs(model);

		return "doctor";
	}

	/**
	 * sends a reservation request to an allocator, which gets processed asynchronously
	 * 
	 * @param model
	 * @param dateStart
	 * @param dateEnd
	 * @param patientID
	 * @param doctorID
	 * @param type
	 * @param minTime
	 * @param maxDistance
	 * @return
	 */
	@RequestMapping(value = "/reserve/", method = RequestMethod.POST)
	public String doctorReserve(Model model, @RequestParam("date_start") String dateStart,
			@RequestParam("date_end") String dateEnd, @RequestParam("patient_id") String patientID,
			@RequestParam("doctor_id") String doctorID, @RequestParam("type") OpSlot.Type type,
			@RequestParam("min_time") int minTime, @RequestParam("max_distance") int maxDistance) {

		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy");
		DateTime startDate = DateTime.parse(dateStart, fmt);
		DateTime endDate = DateTime.parse(dateEnd, fmt);

		ReservationDTO res = new ReservationDTO(patientID, doctorID, startDate.toDate(), endDate.toDate(), type,
				minTime, maxDistance);

		logger.info("trying to reserve patientID: " + patientID + " (as doctorID: " + doctorID
				+ "). sending message to allocator: " + res);

		amqpTemplate.convertAndSend("allocator", res);

		model.addAttribute("sent_reservation", true);
		model.addAttribute("sent_dto", res);
		addStandardOutputs(model);

		return "doctor";
	}

	@RequestMapping(value = "/remove_reservation/", method = RequestMethod.DELETE)
	public String doctorRemoveReservation(Model model, @RequestParam("opslot_id") String opslot_id) {

		// TODO delete reservation (DAO)
		//uiDAO.removeReservationFromOpSlot(opslot_id); // not tested yet TODO

		ReservationCancelNotificationDTO cancel = new ReservationCancelNotificationDTO(opslot_id);
		amqpTemplate.convertAndSend(cancel);

		addStandardOutputs(model);

		return "doctor";
	}

	private void addStandardOutputs(Model model) {

		List<OpSlot> opSlots = uiDAO.findAllOpSlots();
		model.addAttribute("opSlots", opSlots);

		List<Patient> patients = uiDAO.findAllPatients();
		model.addAttribute("patients", patients);
	}

}
