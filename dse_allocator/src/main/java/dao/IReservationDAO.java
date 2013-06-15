package dao;

import java.util.Date;
import java.util.List;

import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.OpSlot;
import dse_domain.domain.OpSlot.Type;
import dse_domain.domain.Patient;

/**
 * Interface defining data access operations relating to performing reservations.
 */
public interface IReservationDAO {

	/**
	 * Updates a given operation slot or inserts it if it does not yet exist.
	 * 
	 * @param opSlot
	 */
	public void updateOpSlot(OpSlot opSlot);

	public Patient findPatient(String patientID);

	public Doctor findDoctor(String doctorID);

	/**
	 * Finds free operation slots that are closest to a patient (up to maxDistance away) and that are in the specified
	 * date range, and have the minimal length, and have the correct op type.
	 * 
	 * @param maxDistance
	 * @param patient
	 * @param startDate
	 * @param endDate
	 * @param minTime
	 * @param hospital
	 * @param type
	 * @return OpSlot if successful, null otherwise
	 */
	public OpSlot findFreeOPSlotInNearHospital(int maxDistance, Patient patient, Date startDate, Date endDate,
			int minTime, Type type);

	public List<OpSlot> findFreeOPSlotsInHospitalSortedList(Date startDate, Date endDate, int minTime,
			Hospital hospital, Type type);

	public void save(Doctor doctor);

	public void save(Patient patient);
}
