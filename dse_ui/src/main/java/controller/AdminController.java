package controller;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import domain.Doctor;
import domain.Hospital;
import domain.OpSlot;
import domain.Patient;
import domain.Reservation;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired(required = false)
	MongoDbFactory mongoDbFactory;

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

	@Autowired(required = false)
	@Qualifier(value = "serviceProperties")
	Properties serviceProperties;
	
	/**
	 * Default Admin Controller.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String publicInfo(Model model) {
		
		addStandardOutputs(model);

		return "admin";
	}
	
	/**
	 * If parameters are valid, a new hospital will be created.
	 * 
	 * @param model
	 * @param name
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@RequestMapping(value = "/hospital/create/", method = RequestMethod.POST)
	public String createHospital(Model model, @RequestParam("name") String name, @RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {
		
		double[] geocoords = {latitude,longitude};
		
		Hospital hospital = new Hospital(name,geocoords);

		mongoTemplate.save(hospital);
		
		hospital = mongoTemplate.findById(hospital.getId(),Hospital.class);
		
		if(hospital!=null) {
			model.addAttribute("new_hospital", hospital);
			model.addAttribute("add_hospital",true);
		} else {
			model.addAttribute("add_hospital",false);
		}
		
		
		addStandardOutputs(model);

		return "admin";
	}
	
	/**
	 * Delete some hospital
	 */
	@RequestMapping(value = "/hospital/delete/", method = RequestMethod.GET)
	public String deleteHospital(Model model,@RequestParam("id") String id_to_delete) {
		
		
		Hospital to_delete = mongoTemplate.findById(id_to_delete, Hospital.class);
		
		// check if hospital may be deleted
		Criteria criteria = Criteria.where("hospital").is(to_delete);
		
		Query query = new Query(criteria);
		
		List<OpSlot> opslots = mongoTemplate.find(query, OpSlot.class);
		
		if(opslots.isEmpty()) {
			mongoTemplate.remove(to_delete);
			model.addAttribute("delete_hospital",true);
		} else {
			model.addAttribute("delete_hospital",false);
		}
		
		addStandardOutputs(model);

		return "admin";
	}
	
/**
 * New doctor, if parameters are valid.
 * @param model
 * @param title
 * @param firstName
 * @param lastName
 * @return
 */
	@RequestMapping(value = "/doctor/create/", method = RequestMethod.POST)
	public String createDoctor(Model model, @RequestParam("title") String title, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
		
		Doctor doctor = new Doctor(title,firstName,lastName);

		mongoTemplate.save(doctor);
		
		doctor = mongoTemplate.findById(doctor.getId(),Doctor.class);
		
		if(doctor!=null) {
			model.addAttribute("new_doctor", doctor);
			model.addAttribute("add_doctor",true);
		} else {
			model.addAttribute("add_doctor",false);
		}
		
		addStandardOutputs(model);

		return "admin";
	}
	
	/**
	 * Delete some doctor
	 */
	@RequestMapping(value = "/doctor/delete/", method = RequestMethod.GET)
	public String deleteDoctor(Model model,@RequestParam("id") String id_to_delete) {
		
		
		Doctor to_delete = mongoTemplate.findById(id_to_delete, Doctor.class);
		
		// check if this doctor has any open reservations, if not, the doctor can be deleted
		// @Maydo: Delete all notifications for this doctor
		Criteria criteria = Criteria.where("doctor").is(to_delete);
		Query query = new Query(criteria);
		
		List<Reservation> reservations = mongoTemplate.find(query, Reservation.class);
		
		if(reservations.isEmpty()) {
			mongoTemplate.remove(to_delete);
			model.addAttribute("delete_doctor",true);
		} else {
			model.addAttribute("delete_doctor",false);
		}
		
		addStandardOutputs(model);

		return "admin";
	}
	
	
		/**
		 * New doctor, if parameters are valid.
		 * @param model
		 * @param title
		 * @param firstName
		 * @param lastName
		 * @return
		 */
		@RequestMapping(value = "/patient/create/", method = RequestMethod.POST)
		public String createPatient(Model model, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {
			
			double[] geocoords = {latitude,longitude};
			
			Patient patient = new Patient(firstName,lastName,geocoords);

			mongoTemplate.save(patient);
			
			patient = mongoTemplate.findById(patient.getId(),Patient.class);
			
			if(patient!=null) {
				model.addAttribute("new_patient", patient);
				model.addAttribute("add_patient",true);
			} else {
				model.addAttribute("add_patient",false);
			}
			
			addStandardOutputs(model);

			return "admin";
		}
		
		/**
		 * Delete some patient
		 */
		@RequestMapping(value = "/patient/delete/", method = RequestMethod.GET)
		public String deletePatient(Model model,@RequestParam("id") String id_to_delete) {
			
			
			Patient to_delete = mongoTemplate.findById(id_to_delete, Patient.class);
			
			// check if this patient has any open reservations, if not, the patient can be deleted
			// @Maydo: Delete all notifications for this patient
			Criteria criteria = Criteria.where("patient").is(to_delete);
			Query query = new Query(criteria);
			
			List<Reservation> reservations = mongoTemplate.find(query, Reservation.class);
			
			if(reservations.isEmpty()) {
				mongoTemplate.remove(to_delete);
				model.addAttribute("delete_patient",true);
			} else {
				model.addAttribute("delete_patient",false);
			}
			
			addStandardOutputs(model);

			return "admin";
		}
	
	
	private void addStandardOutputs(Model model) {
		List<Hospital> hospitals = mongoTemplate.findAll(Hospital.class);
		List<Doctor> doctors = mongoTemplate.findAll(Doctor.class);
		List<Patient> patients = mongoTemplate.findAll(Patient.class);
		
		model.addAttribute("hospitals",hospitals);
		model.addAttribute("doctors",doctors);
		model.addAttribute("patients",patients);
	}

}