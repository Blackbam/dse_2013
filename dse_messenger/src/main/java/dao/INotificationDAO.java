package dao;

import java.util.List;

import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.Notification;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;

public interface INotificationDAO {

	//simple find methods to retrieve entities
	public Patient findPatient(String patientID);
	public Doctor findDoctor(String doctorID);
	public Hospital findHospital(String hospitalID);
	public OpSlot findOpSlot(String opSlotID);

	/**
	 * inserts notification into database
	 * @param notification
	 */
	public void insertNotification(Notification notification);
	
	/**
	 * retrieve all notifications
	 * @return
	 */
	public List<Notification> findAllNotifications();
	
}
