package controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;

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

import domain.OpSlot;
import domain.Patient;
import domain.Reservation;

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

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String patient(Model model, @RequestParam("id") String id) {
		
		// Get the patient with the given id
		Patient patient = mongoTemplate.findById(id, Patient.class);
		model.addAttribute("patient", patient);

		// TODO error handling if patient is not found

		// Get all operation slots for patient
		List<Reservation> reservations = mongoTemplate.find(new Query(where("patient").is(patient)), Reservation.class);
		List<OpSlot> opSlots = mongoTemplate.find(new Query(where("reservation").in(reservations)), OpSlot.class);
		model.addAttribute("opSlots", opSlots);

		return "doctor";
	}

	@RequestMapping(value = "/reserve/", method = RequestMethod.POST)
	public String doctorReserve(@RequestParam("patientID") String patientID) {

		logger.info("trying to reserve patient: " + patientID + ". sending message to allocator");

		return "doctor";
	}

	@RequestMapping(value = "/remove_reservation/", method = RequestMethod.GET)
	public String doctorRemoveReservation(@RequestParam("patientID") int patientID) {

		// TODO ME

		return "doctor";
	}

}
