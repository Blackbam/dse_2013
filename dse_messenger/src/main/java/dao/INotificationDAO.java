package dao;

import java.util.List;

import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.Notification;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;

/**
 * Interface defining data access methods for notifications.
 */
public interface INotificationDAO {

	public Patient findPatient(String patientID);

	public Doctor findDoctor(String doctorID);

	public Hospital findHospital(String hospitalID);

	public OpSlot findOpSlot(String opSlotID);

	/**
	 * Inserts notification into the database.
	 * 
	 * @param notification
	 */
	public void insertNotification(Notification notification);

	/**
	 * Retrieve all notifications.
	 * 
	 * @return
	 */
	public List<Notification> findAllNotifications();

	public List<Patient> findAllPatients();

}
