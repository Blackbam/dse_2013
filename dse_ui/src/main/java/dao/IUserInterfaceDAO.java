package dao;

import java.util.List;

import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.Notification;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;
import dse_domain.domain.Person;

/**
 * Interface defining data-access methods.
 */
public interface IUserInterfaceDAO {

	public Doctor findDoctor(String doctorID);

	public Hospital findHospital(String hospitalID);

	public Patient findPatient(String patientID);

	/**
	 * Find all OP slots with a reservation with the specified doctor.
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
	 * Removes hospital and open op_slots without reservations.
	 * 
	 * @param hospital
	 * @return true if hospital was removed / false if hospital had op_slots with reservations - hospital not removed
	 */
	public boolean delete(Hospital hospital);

	/**
	 * Removes doctor and his notifications if he has no open reservations.
	 * 
	 * @param doctor
	 * @return true if doctor (and notifications) was removed / false if doctor has open reservations - doctor not
	 *         removed
	 */
	public boolean delete(Doctor doctor);

	/**
	 * Removes patient and his notifications if he has no open op_slot reservations.
	 * 
	 * @param patient
	 * @return true if patient (and notifications) was removed / false if patient has open reservations - patient not
	 *         removed
	 */
	public boolean delete(Patient patient);

	public List<Notification> findNotificationsForPerson(Person person);

	public List<Notification> findAllNotifications();

	public void save(Notification notification);

	public void delete(Notification notification);

	public void removeReservationFromOpSlot(String opSlotID);

}
