package dao;

import java.util.List;

import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;

public interface IUserInterfaceDAO {

	public Doctor findDoctor(String doctorID);

	public Hospital findHospital(String hospitalID);

	public Patient findPatient(String patientID);

	/**
	 * finds all op slots with a reservation with the specified doctor
	 * 
	 * @param doctor
	 * @return
	 */
	public List<OpSlot> findAllReservedOpSlotsWithDoctor(Doctor doctor);

	public List<OpSlot> findAllOpSlots();

	public List<Patient> findAllPatients();

	public List<Doctor> findAllDoctors();

	public List<Hospital> findAllHospitals();

	public void save(Hospital hospital);

	public void save(Doctor doctor);

	public void save(Patient patient);

	/**
	 * 
	 * @param hospital
	 * @return true if hospital was removed / false if hospital had op_slots with Reservations -
	 *         hospital not removed
	 */
	public boolean delete(Hospital hospital);

	/**
	 * removes doctor and his notifications if he has no open Reservations
	 * 
	 * @param doctor
	 * @return true if doctor (and notifications) was removed / false if doctor has open
	 *         Reservations - doctor not removed
	 */
	public boolean delete(Doctor doctor);

	/**
	 * removes patient and his notifications if he has no open op_slot Reservations
	 * 
	 * @param patient
	 * @return true if patient (and notifications) was removed / false if patient has open
	 *         Reservations - patient not removed
	 */
	public boolean delete(Patient patient);

}
