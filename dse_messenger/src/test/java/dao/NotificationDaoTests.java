package dao;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import dao.impl.NotificationMongoDAO;
import dse_domain.domain.Notification;
import dse_domain.util.AbstractDBTest;

/**
 * Unit tests for the Notification DAO.
 * 
 * @author Taylor
 */
public class NotificationDaoTests extends AbstractDBTest {

	private static NotificationMongoDAO notificationDao;

	@Before
	public void getDao() {
		notificationDao = new NotificationMongoDAO(template);
	}

	@Test
	public void testNotificationPersistence() {

		// Get original count of notifications in the DB
		int notificationCount = notificationDao.findAllNotifications().size();

		// Insert a new notification
		Notification notification = new Notification(notificationDao.findAllPatients().get(0), "test", "test");
		notificationDao.insertNotification(notification);

		// Get new count of notifications in the DB
		int newNotificationCount = notificationDao.findAllNotifications().size();

		assertEquals("Check number of notifications", (notificationCount + 1), newNotificationCount);
	}

	@Test
	public void testPatientRetrieval() {
		assertEquals("Check number of patients", 6, notificationDao.findAllPatients().size());
	}

}
