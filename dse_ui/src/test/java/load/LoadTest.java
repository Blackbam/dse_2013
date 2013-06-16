package load;

import static com.jayway.restassured.RestAssured.expect;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.jayway.restassured.RestAssured;

public class LoadTest {

	private final static String BASE_URL = "http://dse_ui.cloudfoundry.com";

	static final Logger logger = Logger.getLogger(LoadTest.class);

	@Test
	public void testLightLoad() {

		int runs = 10;

		long timeBefore = System.nanoTime();
		for (int i = 0; i < runs; i++) {
			RestAssured.baseURI = BASE_URL;
			RestAssured.port = 80;
			expect().statusCode(200).when().get("/notification/?id=51bdf32fc01182b4d6e87630");
		}
		long timeAfter = System.nanoTime();
		long duration = timeAfter - timeBefore;

		logger.info(runs + " runs: " + duration + " nanoseconds in total, averaged " + (duration / runs) + " nanoseconds per run.");
	}

	@Test
	public void testModerateLoad() {

		int runs = 50;

		long timeBefore = System.nanoTime();
		for (int i = 0; i < runs; i++) {
			RestAssured.baseURI = BASE_URL;
			RestAssured.port = 80;
			expect().statusCode(200).when().get("/notification/?id=51bdf32fc01182b4d6e87630");
		}
		long timeAfter = System.nanoTime();
		long duration = timeAfter - timeBefore;

		logger.info(runs + " runs: " + duration + " nanoseconds in total, averaged " + (duration / runs) + " nanoseconds per run.");
	}

	@Test
	public void testHighLoad() {

		int runs = 100;

		long timeBefore = System.nanoTime();
		for (int i = 0; i < runs; i++) {
			RestAssured.baseURI = BASE_URL;
			RestAssured.port = 80;
			expect().statusCode(200).when().get("/notification/?id=51bdf32fc01182b4d6e87630");
		}
		long timeAfter = System.nanoTime();
		long duration = timeAfter - timeBefore;

		logger.info(runs + " runs: " + duration + " nanoseconds in total, averaged " + (duration / runs) + " nanoseconds per run.");
	}

}
