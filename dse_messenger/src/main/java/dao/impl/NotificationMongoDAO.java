package dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import dao.INotificationDAO;
import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.Notification;
import dse_domain.domain.Patient;

public class NotificationMongoDAO implements INotificationDAO {
	static final Logger logger = Logger.getLogger(NotificationMongoDAO.class);

	@Autowired
	MongoOperations mongo;
	
	public NotificationMongoDAO(MongoOperations mongo) {
		this.mongo = mongo;
	}

	@Override
	public void insertNotification(Notification notification) {
		mongo.insert(notification);

		logger.info("Notification stored in database: " + notification);
	}

	@Override
	public Patient findPatient(String patientID) {
		return mongo.findById(patientID, Patient.class);
	}

	@Override
	public Doctor findDoctor(String doctorID) {
		return mongo.findById(doctorID, Doctor.class);
	}

	@Override
	public Hospital findHospital(String hospitalID) {
		return mongo.findById(hospitalID, Hospital.class);
	}

	@Override
	public List<Notification> findAllNotifications() {
		return mongo.findAll(Notification.class);
	}

}
