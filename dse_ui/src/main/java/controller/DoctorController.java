package controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dse_domain.DTO.ReservationDTO;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;
import dse_domain.domain.Doctor;
import dse_domain.domain.Reservation;
import dse_domain.domain.Hospital;

@Controller
@RequestMapping(value = "/doctor")
public class DoctorController {
	static final Logger logger = Logger.getLogger(DoctorController.class);

	@Autowired(required = false)
	MongoDbFactory mongoDbFactory;

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

	@Autowired
	AmqpTemplate amqpTemplate;

	// Requires DOCTOR id
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String patient(Model model, @RequestParam("id") String id) {
		
		
		Doctor doctor = mongoTemplate.findById(id, Doctor.class);
		
		/* For Debugging: Test Reservations */
		
		/*
		Patient p = mongoTemplate.findOne(new Query(),Patient.class);
		OpSlot op_slot = mongoTemplate.findOne(new Query(),OpSlot.class);
		
		
		
		if(doctor!=null && p!=null && op_slot!=null && op_slot.getReservation()==null) {
			Reservation res = new Reservation(doctor,p);
			mongoTemplate.save(res);
			op_slot.setReservation(res);
			mongoTemplate.save(op_slot);
			logger.debug("Test reservation created");
		} else {
			logger.debug("Test reservation error");
		}
		
		*/
		// End Debug
		
		
		// finds all the reservation
		List<Reservation> reservations = mongoTemplate.find(new Query(where("doctor").is(doctor)), Reservation.class);
		
		List<OpSlot> opSlots = mongoTemplate.find(new Query(where("reservation").in(reservations)), OpSlot.class);
		
		model.addAttribute("op_slots_this_doctor", opSlots);
		
		addStandardOutputs(model);

		return "doctor";
	}

	@RequestMapping(value = "/reserve/", method = RequestMethod.POST)
	public String doctorReserve(Model model, @RequestParam("date_start") String date_start, @RequestParam("date_end") String date_end,
			@RequestParam("patient_id") String patient_id, @RequestParam("type") OpSlot.Type type,
			@RequestParam("min_time") int min_time,@RequestParam("max_distance") int max_distance) {

		
		

		ReservationDTO res = new ReservationDTO(patient_id,date_start, date_end,type,min_time,max_distance);
		
		logger.info("trying to reserve patient: " + patient_id + ". sending message to allocator: " +res);
		
		amqpTemplate.convertAndSend("allocator", res);
		
		
		addStandardOutputs(model);
		
		return "doctor";
	}

	// i think we need op_slot id here
	@RequestMapping(value = "/remove_reservation/", method = RequestMethod.GET)
	public String doctorRemoveReservation(Model model, @RequestParam("opslot_id") int opslot_id) {
		
		//
		
		// TODO ME
		addStandardOutputs(model);
		
		return "doctor";
	}
	
	private void addStandardOutputs(Model model) {
		List<OpSlot> opSlots = mongoTemplate.findAll(OpSlot.class);
		model.addAttribute("opSlots", opSlots);
		
		List<Patient> patients = mongoTemplate.findAll(Patient.class);
		model.addAttribute("patients", patients);
	}

}
