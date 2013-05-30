package controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import domain.OpSlot;

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

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {

		// TODO only find this doctors op slots + find needed data through database relations
		List<OpSlot> opSlots = mongoTemplate.findAll(OpSlot.class);

		model.addAttribute("op_slots", opSlots);

		logger.debug("Doctor view finished");

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
