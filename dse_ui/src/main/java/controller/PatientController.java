package controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;
import dse_domain.domain.Reservation;

/**
 * Handles requests for the patient view
 * 
 * @author Taylor
 */
@Controller
@RequestMapping(value = "/patient")
public class PatientController {

	@Autowired(required = false)
	MongoDbFactory mongoDbFactory;

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

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

		// Get the patient with the given id
		Patient patient = mongoTemplate.findById(id, Patient.class);
		model.addAttribute("patient", patient);
		model.addAttribute("patientID", patient.getId());

		// TODO error handling if patient is not found

		// Get all operation slots for patient
		List<Reservation> reservations = mongoTemplate.find(new Query(where("patient").is(patient)), Reservation.class);
		List<OpSlot> opSlots = mongoTemplate.find(new Query(where("reservation").in(reservations)), OpSlot.class);
		model.addAttribute("opSlots", opSlots);

		return "patient";
	}

}
