package controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dse_domain.domain.Hospital;
import dse_domain.domain.OpSlot;

/**
 * Handles requests for the hospital view
 * 
 * @author Blackbam
 */
@Controller
@RequestMapping(value = "/hospital")
public class HospitalController extends SlotController {
	static final Logger logger = Logger.getLogger(HospitalController.class);

	@Autowired(required = false)
	MongoDbFactory mongoDbFactory;

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String hospital(Model model, @RequestParam("id") String id) {
		Hospital hospital = mongoTemplate.findOne(new Query(where("id").is(id)), Hospital.class);
		addStandardOutputs(model, hospital);
		return "hospital";
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String filteredHospital(Model model, @RequestParam("id") String id, @RequestParam("date") String date,
			@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("status") String status,
			@RequestParam("patient") String patient, @RequestParam("doctor") String doctor,
			@RequestParam("type") String type) {

		Hospital hospital = mongoTemplate.findOne(new Query(where("id").is(id)), Hospital.class);

		// Retrieve all slots
		List<OpSlot> allSlots = mongoTemplate.findAll(OpSlot.class);

		// Remove past slots before filtering
		allSlots = removePastSlots(allSlots);

		// Perform filtering
		allSlots = filterOpSlotList(allSlots, date, from, to, status, hospital.getName(), doctor, type, patient);

		// Sort the list of OpSlots according to their dates
		Collections.sort(allSlots, new OpSlotComparator());

		// Set models
		model.addAttribute("opSlots", allSlots);
		addStandardOutputs(model, hospital, allSlots);
		return "hospital";
	}

	@RequestMapping(value = "/op_slot/create/", method = RequestMethod.POST)
	public String createHospital(Model model, @RequestParam("hospital_id") String hospital_id,
			@RequestParam("date") String date, @RequestParam("length") int length,
			@RequestParam("type") OpSlot.Type type) {

		Hospital hospital = mongoTemplate.findOne(new Query(where("id").is(hospital_id)), Hospital.class);

		if (hospital != null) {

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date d;
			try {
				d = format.parse(date);

				OpSlot op_slot = new OpSlot(hospital, length, type, d);

				mongoTemplate.save(op_slot);

				op_slot = mongoTemplate.findById(op_slot.getId(), OpSlot.class);

				if (op_slot != null) {
					model.addAttribute("new_opslot", op_slot);
					model.addAttribute("add_opslot", true);

					logger.info("Created op_slot: " + op_slot.getId() + "(" + op_slot.getDateString() + ")");
				} else {
					model.addAttribute("add_opslot", false);
				}

			} catch (ParseException e) {
				model.addAttribute("add_opslot", false);
			}

		} else {
			logger.error("no hospital found");
		}

		addStandardOutputs(model, hospital);

		return "hospital";
	}

	/**
	 * Delete a given OP slot.
	 * 
	 * @param model
	 * @param id_to_delete
	 * @return
	 */
	@RequestMapping(value = "/op_slot/delete/", method = RequestMethod.GET)
	public String deleteHospital(Model model, @RequestParam("id") String id_to_delete) {

		OpSlot to_delete = mongoTemplate.findById(id_to_delete, OpSlot.class);

		if (to_delete != null) {

			Hospital hospital = to_delete.getHospital();

			if (to_delete.getReservation() == null) {
				mongoTemplate.remove(to_delete);
				model.addAttribute("delete_opslot", true);
			} else {
				model.addAttribute("delete_opslot", false);
			}
			mongoTemplate.remove(to_delete);
			model.addAttribute("delete_opslot", true);
			addStandardOutputs(model, hospital);
			return "hospital";
		}

		return "hospital";
	}

	private void addStandardOutputs(Model model, Hospital hospital) {
		Criteria criteria = Criteria.where("hospital").is(hospital);
		Query query = new Query(criteria);
		List<OpSlot> opSlots = mongoTemplate.find(query, OpSlot.class);

		model.addAttribute("hospitalId", hospital.getId());
		model.addAttribute("hospital", hospital);
		model.addAttribute("opSlots", opSlots);
		model.addAttribute("slotCount", opSlots.size());
	}

	private void addStandardOutputs(Model model, Hospital hospital, List<OpSlot> opSlots) {
		model.addAttribute("hospitalId", hospital.getId());
		model.addAttribute("hospital", hospital);
		model.addAttribute("slotCount", opSlots.size());
	}

}
