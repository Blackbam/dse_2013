package dao.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import dao.IUserInterfaceDAO;
import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.Notification;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;

public class UserInterfaceMongoDAO implements IUserInterfaceDAO {
	static final Logger logger = Logger.getLogger(UserInterfaceMongoDAO.class);
	MongoOperations mongo;

	public UserInterfaceMongoDAO(MongoOperations mongo) {
		this.mongo = mongo;
	}

	@Override
	public Doctor findDoctor(String doctorID) {
		return mongo.findById(doctorID, Doctor.class);
	}

	@Override
	public List<OpSlot> findAllReservedOpSlotsWithDoctor(Doctor doctor) {
		Criteria query = where("reservation").exists(true).and("reservation.doctor").is(doctor);
		return mongo.find(new Query(query), OpSlot.class);
	}

	@Override
	public List<OpSlot> findAllOpSlots() {
		return mongo.findAll(OpSlot.class);
	}

	@Override
	public List<Patient> findAllPatients() {
		return mongo.findAll(Patient.class);
	}
	
	@Override
	public List<Doctor> findAllDoctors() {
		return mongo.findAll(Doctor.class);
	}

	@Override
	public List<Hospital> findAllHospitals() {
		return mongo.findAll(Hospital.class);
	}

	@Override
	public void save(Hospital hospital) {
		mongo.save(hospital);
	}

	@Override
	public void save(Doctor doctor) {
		mongo.save(doctor);
	}

	@Override
	public void save(Patient patient) {
		mongo.save(patient);
	}

	@Override
	public Hospital findHospital(String hospitalID) {
		return mongo.findById(hospitalID, Hospital.class);
	}

	@Override
	public Patient findPatient(String patientID) {
		return mongo.findById(patientID, Patient.class);
	}

	@Override
	public boolean delete(Hospital hospital) {

		// find op_slots in hospital where Reservation exists
		Query query = new Query(where("hospital").is(hospital).and("reservation").exists(true));
		List<OpSlot> opSlots = mongo.find(query, OpSlot.class);

		if (opSlots.isEmpty()) {
			mongo.remove(hospital);
			mongo.remove(new Query(where("hospital").is(hospital)), OpSlot.class);
			logger.debug("No Reservations in hospital, remove was successful");
			return true;
		} else {
			logger.debug("There is atleast one Reservation in hospital, remove was unsuccessful");
			return false;
		}
	}

	@Override
	public boolean delete(Doctor doctor) {

		// check if this doctor has any open reservations, if not, the doctor can be deleted
		Query query = new Query(where("reservation").exists(true).and("reservation.doctor").is(doctor));
		List<OpSlot> opSlots = mongo.find(query, OpSlot.class);

		if (opSlots.isEmpty()) {
			// delete doctor as well as his notifications
			Query notificationDelete = new Query(where("user.id").is(doctor.getId()));
			mongo.remove(notificationDelete, Notification.class);
			mongo.remove(doctor);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean delete(Patient patient) {
		// check if this patient has any open reservations, if not, the patient can be deleted
		Query query = new Query(where("reservation").exists(true).and("reservation.patient").is(patient));
		List<OpSlot> opSlots = mongo.find(query, OpSlot.class);

		if (opSlots.isEmpty()) {
			// delete doctor as well as his notifications
			Query notificationDelete = new Query(where("user.id").is(patient.getId()));
			mongo.remove(notificationDelete, Notification.class);
			mongo.remove(patient);
			return true;
		} else {
			return false;
		}
	}



}
