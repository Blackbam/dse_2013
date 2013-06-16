package Notification;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.matcher.RestAssuredMatchers.matchesXsd;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasXPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import dao.INotificationDAO;
import dao.impl.NotificationMongoDAO;
import dse_domain.domain.Patient;

public class NotificationTests {

	private static final String BASE_URL = "http://dse_ui.cloudfoundry.com/notification/?id=";

	@Autowired
	private INotificationDAO notificationDAO;

	@Test
	public void testNotificationResponse() {

		List<Patient> allPatients = notificationDAO.findAllPatients();

		for (Patient patient : allPatients) {
			Response res = get(BASE_URL + patient.getId());
			assertEquals(200, res.getStatusCode());
			// String json = res.asString();
			// JsonPath jp = new JsonPath(json);
			// assertEquals("test@hascode.com", jp.get("email"));
			// assertEquals("Tim", jp.get("firstName"));
			// assertEquals("Testerman", jp.get("lastName"));
			// assertEquals("1", jp.get("id"));
		}

	}

}
