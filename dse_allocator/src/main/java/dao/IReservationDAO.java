package dao;

import java.util.Date;
import java.util.List;

import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;

public interface IReservationDAO {

	/**
	 * updates opSlot - insert if opSlot isn't in database yet
	 * 
	 * @param opSlot
	 */
	public void updateOpSlot(OpSlot opSlot);

	public Patient findPatient(String patientID);

	public Doctor findDoctor(String doctorID);

	/**
	 * finds free op_slot which are closest to patient (to maxDistance away) and which are in the
	 * specified date range, and have the minimal length
	 * 
	 * @param maxDistance
	 * @param patient
	 * @param startDate
	 * @param endDate
	 * @param minTime
	 * @param hospital
	 * @return OpSlot if successful, null otherwise
	 */
	public OpSlot findFreeOPSlotInNearHospital(int maxDistance, Patient patient, Date startDate, Date endDate,
			int minTime);

	/**
	 * TODO change GeoResults return find hospitals in the area specified with maxDistance and
	 * patient location
	 * 
	 * @param maxDistance
	 *            in kilometers
	 * @param patient
	 * @return list with GeoResults sorted by distance
	 */
	// public GeoResults<Hospital> findNearHospitals(int maxDistance, Patient patient);

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param minTime
	 * @param hospital
	 * @return
	 */
	public List<OpSlot> findFreeOPSlotsInHospitalSortedList(Date startDate, Date endDate, int minTime, Hospital hospital);

}
