package controller;

import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dse_domain.domain.OpSlot;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends SlotController {

	@Autowired(required = false)
	MongoDbFactory mongoDbFactory;

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

	static final Logger logger = Logger.getLogger(HomeController.class);

	/**
	 * Retrieve and display all upcoming operation slots.
	 * 
	 * @param model
	 *            the model associated with the view for this controller
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String publicInfo(Model model) {
		List<OpSlot> opSlots = mongoTemplate.findAll(OpSlot.class);

		logger.debug("Public OP Slots found: " + opSlots.size());

		// Sort the list of OpSlots according to their dates
		Collections.sort(opSlots, new OpSlotComparator());

		model.addAttribute("opSlots", opSlots);
		model.addAttribute("slotCount", opSlots.size());
		return "home";
	}

	/**
	 * Retrieve and display all upcoming operation slots that match the given criteria.
	 * 
	 * @param model
	 *            the model associated with the view for this controller
	 * @param date
	 *            a date to filter by
	 * @param from
	 *            a starting time to filter by
	 * @param to
	 *            an end time to filter by
	 * @param status
	 *            a status to filter by
	 * @param hospital
	 *            a hospital name to filter by
	 * @param doctor
	 *            a doctor name to filter by
	 * @param type
	 *            an operation type to filter by
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String publicInfoFiltered(Model model, @RequestParam("date") String date, @RequestParam("from") String from,
			@RequestParam("to") String to, @RequestParam("status") String status,
			@RequestParam("hospital") String hospital, @RequestParam("doctor") String doctor,
			@RequestParam("type") String type) {

		// Retrieve all slots
		List<OpSlot> allSlots = mongoTemplate.findAll(OpSlot.class);

		// Remove past slots before filtering
		allSlots = removePastSlots(allSlots);

		// Perform filtering
		allSlots = filterOpSlotList(allSlots, date, from, to, status, hospital, doctor, type);

		// Sort the list of OpSlots according to their dates
		Collections.sort(allSlots, new OpSlotComparator());

		// Set models
		model.addAttribute("opSlots", allSlots);
		model.addAttribute("slotCount", allSlots.size());
		return "home";
	}

}
