package dao;

import java.util.List;

import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.Notification;
import dse_domain.domain.Patient;

public interface INotificationDAO {

	
	public Patient findPatient(String patientID);
	public Doctor findDoctor(String doctorID);
	public Hospital findHospital(String hospitalID);

	public void insertNotification(Notification notification);
	
	public List<Notification> findAllNotifications();
	
}
