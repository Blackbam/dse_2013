package controller;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dao.IUserInterfaceDAO;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;

/**
 * Handles requests for the patient view
 * 
 * @author Taylor
 */
@Controller
@RequestMapping(value = "/patient")
public class PatientController extends SlotController {

	@Autowired
	IUserInterfaceDAO uiDAO;

	static final Logger logger = Logger.getLogger(PatientController.class);

	/**
	 * Retrieve basic information for a given patient
	 * 
	 * @param model
	 *            the model associated with the view for this controller
	 * @param id
	 *            the patient ID
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String patient(Model model, @RequestParam("id") String id) {

		Patient patient = uiDAO.findPatient(id);
		List<OpSlot> opSlots = uiDAO.findAllReservedOpSlotsWithPatient(patient);

		for (OpSlot slot : opSlots) {
			logger.debug("Found OP slot " + slot.getDateString() + " with reservation: "
					+ slot.getReservation().getDoctor() + "/" + slot.getReservation().getPatient());
		}

		model.addAttribute("patient", patient);
		model.addAttribute("patientID", id);
		model.addAttribute("opSlots", opSlots);
		addStandardOutputs(model, opSlots);

		return "patient";
	}

	/**
	 * Retrieve model information for a patient with a given id and filter the patiet's OP list according to the given
	 * criteria.
	 * 
	 * @param model
	 * @param id
	 * @param date
	 * @param from
	 * @param to
	 * @param doctor
	 * @param hospital
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String patientFiltered(Model model, @RequestParam("id") String id, @RequestParam("date") String date,
			@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("doctor") String doctor,
			@RequestParam("hospital") String hospital, @RequestParam("type") String type) {

		Patient patient = uiDAO.findPatient(id);
		List<OpSlot> opSlots = uiDAO.findAllReservedOpSlotsWithPatient(patient);

		// Remove past slots before filtering
		opSlots = removePastSlots(opSlots);

		// Perform filtering
		opSlots = filterOpSlotList(opSlots, date, from, to, null, hospital, doctor, type, null);

		// Sort the list of OpSlots according to their dates
		Collections.sort(opSlots, new OpSlotComparator());

		model.addAttribute("patient", patient);
		model.addAttribute("patientID", id);
		model.addAttribute("opSlots", opSlots);
		addStandardOutputs(model, opSlots);

		return "patient";
	}

	private void addStandardOutputs(Model model, List<OpSlot> opSlots) {
		model.addAttribute("opSlots", opSlots);
		model.addAttribute("slotCount", opSlots.size());
	}

}
