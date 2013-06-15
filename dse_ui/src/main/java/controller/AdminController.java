package controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dao.IUserInterfaceDAO;
import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.Patient;

/**
 * Handles requests for the administrator page.
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	IUserInterfaceDAO uiDAO;

	/**
	 * Default Admin Controller.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String publicInfo(Model model) {
		addStandardOutputs(model);
		return "admin";
	}

	/**
	 * Create a new hospital.
	 * 
	 * @param model
	 * @param name
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@RequestMapping(value = "/hospital/create/", method = RequestMethod.POST)
	public String createHospital(Model model, @RequestParam("username") String username,
			@RequestParam("name") String name, @RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude) {

		double[] geocoords = { latitude, longitude };

		Hospital hospital = new Hospital(name, geocoords);
		hospital.setUsername(username);

		uiDAO.save(hospital);

		hospital = uiDAO.findHospital(hospital.getId()); // TODO wat ??

		if (hospital != null) {
			model.addAttribute("new_hospital", hospital);
			model.addAttribute("add_hospital", true);
		} else {
			model.addAttribute("add_hospital", false);
		}

		addStandardOutputs(model);

		return "admin";
	}

	/**
	 * Delete a given hospital.
	 * 
	 * @param model
	 * @param idToDelete
	 * @return
	 */
	@RequestMapping(value = "/hospital/delete/", method = RequestMethod.GET)
	public String deleteHospital(Model model, @RequestParam("id") String idToDelete) {

		Hospital to_delete = uiDAO.findHospital(idToDelete);

		boolean removed = uiDAO.delete(to_delete);

		if (removed) {
			model.addAttribute("delete_hospital", true);
		} else {
			model.addAttribute("delete_hospital", false);
		}

		addStandardOutputs(model);

		return "admin";
	}

	/**
	 * Create a new Doctor entity.
	 * 
	 * @param model
	 *            the model associated with the view for this controller
	 * @param username
	 *            the doctor's username
	 * @param password
	 *            the doctor's password
	 * @param title
	 *            the doctor's title
	 * @param firstName
	 *            the doctor's first name
	 * @param lastName
	 *            the doctor's last name
	 * @return
	 */
	@RequestMapping(value = "/doctor/create/", method = RequestMethod.POST)
	public String createDoctor(Model model, @RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("title") String title,
			@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {

		// Persist doctor
		Doctor doctor = new Doctor(title, firstName, lastName);
		doctor.setUsername(username);
		doctor.setPassword(password);

		uiDAO.save(doctor);

		doctor = uiDAO.findDoctor(doctor.getId());

		if (doctor != null) {
			model.addAttribute("new_doctor", doctor);
			model.addAttribute("add_doctor", true);
		} else {
			model.addAttribute("add_doctor", false); // dead code TODO
		}

		addStandardOutputs(model);

		return "admin";
	}

	/**
	 * Delete a given doctor.
	 * 
	 * @param model
	 * @param idToDelete
	 * @return
	 */
	@RequestMapping(value = "/doctor/delete/", method = RequestMethod.GET)
	public String deleteDoctor(Model model, @RequestParam("id") String idToDelete) {

		Doctor toDelete = uiDAO.findDoctor(idToDelete);

		boolean removed = uiDAO.delete(toDelete);
		model.addAttribute("delete_doctor", removed);

		addStandardOutputs(model);

		return "admin";
	}

	/**
	 * Create a new Patient entity.
	 * 
	 * @param model
	 *            the model associated with the view for this controller
	 * @param username
	 *            the patient's username
	 * @param password
	 *            the patient's password
	 * @param firstName
	 *            the patient's first name
	 * @param lastName
	 *            the patient's last name
	 * @param latitude
	 *            the latitude of the patient's location
	 * @param longitude
	 *            the longitude of the patient's location
	 * @return
	 */
	@RequestMapping(value = "/patient/create/", method = RequestMethod.POST)
	public String createPatient(Model model, @RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude) {

		// Persist patient
		double[] geoCoords = { latitude, longitude };
		Patient patient = new Patient(firstName, lastName, geoCoords);
		patient.setUsername(username);
		patient.setPassword(password);

		uiDAO.save(patient);

		patient = uiDAO.findPatient(patient.getId());

		if (patient != null) {
			model.addAttribute("new_patient", patient);
			model.addAttribute("add_patient", true);
		} else {
			model.addAttribute("add_patient", false); // dead code TODO
		}

		addStandardOutputs(model);

		return "admin";
	}

	/**
	 * Delete a given patient.
	 * 
	 * @param model
	 * @param idToDelete
	 * @return
	 */
	@RequestMapping(value = "/patient/delete/", method = RequestMethod.GET)
	public String deletePatient(Model model, @RequestParam("id") String idToDelete) {

		Patient toDelete = uiDAO.findPatient(idToDelete);

		boolean removed = uiDAO.delete(toDelete);

		model.addAttribute("delete_patient", removed);

		addStandardOutputs(model);

		return "admin";
	}

	/**
	 * Retrieve all standard entities and add them to a given model.
	 * 
	 * @param model
	 */
	private void addStandardOutputs(Model model) {
		List<Hospital> hospitals = uiDAO.findAllHospitals();
		List<Doctor> doctors = uiDAO.findAllDoctors();
		List<Patient> patients = uiDAO.findAllPatients();

		model.addAttribute("hospitals", hospitals);
		model.addAttribute("doctors", doctors);
		model.addAttribute("patients", patients);
	}

}