package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

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

	/**
	 * Filter out operation slots that have already occurred.
	 * 
	 * @param opSlots
	 *            a list of operation slots
	 * @return
	 */
	public static List<OpSlot> removePastSlots(List<OpSlot> opSlots) {
		List<OpSlot> toRemove = new ArrayList<OpSlot>();
		for (OpSlot slot : opSlots) {
			DateTime dt = new DateTime(slot.getDate());
			if (dt.isBeforeNow()) {
				toRemove.add(slot);
			}
		}
		opSlots.removeAll(toRemove);
		return opSlots;
	}

	/**
	 * Filter a list of OpSlots according to given criteria.
	 * 
	 * @param slots
	 *            the list of slots to filter
	 * @param date
	 *            a date string to filter by
	 * @param from
	 *            the starting time to filter by
	 * @param to
	 *            the end time to filter by
	 * @param status
	 *            a status string to filter by
	 * @param hospital
	 *            a hospital name to filter by
	 * @param doctor
	 *            a doctor's name to filter by
	 * @param type
	 *            an operation type to filter by
	 * @return
	 */
	public static List<OpSlot> filterOpSlotList(List<OpSlot> slots, String date, String from, String to, String status,
			String hospital, String doctor, String type) {

		// Filter list according to received criteria
		List<OpSlot> toRemove = new ArrayList<OpSlot>();
		for (OpSlot slot : slots) {

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

			// to
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

		// Remove all slots that do not match criteria
		slots.removeAll(toRemove);

		return slots;
	}

	/**
	 * Create a new patient.
	 * 
	 * @param firstname
	 *            the patient's first name
	 * @param lastname
	 *            the patient's last name
	 * @param lat
	 *            the patient's latitude geo position.
	 * @param lon
	 *            the patient's longitude geo position.
	 * @return
	 */
	@RequestMapping(value = "/patient/create/{firstname}/{lastname}/{lat}/{lon}", method = RequestMethod.GET)
	public String patientCreate(@PathVariable("firstname") String firstname, @PathVariable("lastname") String lastname,
			@PathVariable("lat") double lat, @PathVariable("lon") double lon) {
		double[] coordinates = new double[] { lat, lon };
		Patient patient = new Patient(firstname, lastname, coordinates);
		mongoTemplate.save(patient);
		return "redirect:/";
	}

	/**
	 * Custom comparator used to sort lists of OpSlots
	 * 
	 * @author Taylor
	 */
	class OpSlotComparator implements Comparator<OpSlot> {

		@Override
		public int compare(OpSlot slot1, OpSlot slot2) {

			DateTime slot1Dt = new DateTime(slot1.getDate());
			DateTime slot2Dt = new DateTime(slot2.getDate());

			if (slot1Dt.isBefore(slot2Dt)) {
				return -1;
			} else if (slot2Dt.isBefore(slot1Dt)) {
				return 1;
			} else {
				return 0;
			}
		}
	}

}
