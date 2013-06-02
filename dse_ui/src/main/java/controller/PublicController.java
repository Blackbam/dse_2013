package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dse_domain.domain.OpSlot;

/**
 * Handles requests for the public access page.
 * 
 * @author Taylor
 */
@Controller
@RequestMapping(value = "/public")
public class PublicController {

	@Autowired(required = false)
	MongoDbFactory mongoDbFactory;

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

	@Autowired(required = false)
	@Qualifier(value = "serviceProperties")
	Properties serviceProperties;

	static final Logger logger = Logger.getLogger(PublicController.class);

	/**
	 * TODO
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String publicInfo(Model model) {
		List<OpSlot> opSlots = mongoTemplate.findAll(OpSlot.class);
		model.addAttribute("opSlots", opSlots);
		return "public";
	}

	/**
	 * TODO
	 * 
	 * @param model
	 * @param date
	 * @param from
	 * @param to
	 * @param status
	 * @param hospital
	 * @param doctor
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String publicInfoFiltered(Model model, @RequestParam("date") String date, @RequestParam("from") String from,
			@RequestParam("to") String to, @RequestParam("status") String status,
			@RequestParam("hospital") String hospital, @RequestParam("doctor") String doctor,
			@RequestParam("type") String type) {

		List<OpSlot> allSlots = mongoTemplate.findAll(OpSlot.class);

		// Filter list according to received criteria
		List<OpSlot> toRemove = new ArrayList<OpSlot>();
		for (OpSlot slot : allSlots) {

			// date
			String dateString = null;
			if (slot.getDate() != null) {
				DateTime dt = new DateTime(slot.getDate());
				dateString = dt.toLocalDate().toString("dd.MM.yyyy");
			}
			if (!date.isEmpty() && !date.equals(dateString)) {
				toRemove.add(slot);
				continue;
			}

			// from
			if (!from.isEmpty()) {
				DateTime slotEndTime = new DateTime(slot.getDate()).plusMinutes(slot.getLength());
				String[] split = from.split(":");
				Integer fromHour = Integer.valueOf(split[0]);
				Integer fromMinute = Integer.valueOf(split[1]);

				if (slotEndTime.getHourOfDay() < fromHour
						|| (slotEndTime.getHourOfDay() == fromHour && slotEndTime.getMinuteOfHour() < fromMinute)) {
					toRemove.add(slot);
					continue;
				}
			}

			// TODO to
			if (!to.isEmpty()) {
				DateTime slotStartTime = new DateTime(slot.getDate());
				String[] split = to.split(":");
				Integer fromHour = Integer.valueOf(split[0]);
				Integer fromMinute = Integer.valueOf(split[1]);

				if (slotStartTime.getHourOfDay() > fromHour
						|| (slotStartTime.getHourOfDay() == fromHour && slotStartTime.getMinuteOfHour() > fromMinute)) {
					toRemove.add(slot);
					continue;
				}
			}

			// status
			String reserved = "available";
			if (slot.getReservation() != null) {
				reserved = "reserved";
			}
			if (!status.equalsIgnoreCase("unset") && !status.equalsIgnoreCase(reserved)) {
				toRemove.add(slot);
				continue;
			}

			// Hospital
			if (!hospital.isEmpty() && !slot.getHospital().getName().toUpperCase().contains(hospital.toUpperCase())) {
				toRemove.add(slot);
				continue;
			}

			// Doctor
			String fullname = "";
			if (slot.getReservation() != null && slot.getReservation().getDoctor() != null) {
				String firstName = slot.getReservation().getDoctor().getFirstName().toUpperCase();
				String lastName = slot.getReservation().getDoctor().getLastName().toUpperCase();
				fullname = firstName + " " + lastName;
			}
			if (!doctor.isEmpty() && !fullname.contains(doctor.toUpperCase())) {
				toRemove.add(slot);
				continue;
			}

			// Type
			if (!type.isEmpty() && !slot.getTypeString().toUpperCase().contains(type.toUpperCase())) {
				toRemove.add(slot);
				continue;
			}
		}

		// Remove all slots that did not match criteria
		allSlots.removeAll(toRemove);

		// Set model
		model.addAttribute("opSlots", allSlots);
		return "public";
	}
}