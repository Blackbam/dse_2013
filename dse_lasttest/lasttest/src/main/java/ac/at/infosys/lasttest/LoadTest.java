package ac.at.infosys.lasttest;

import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.expect;

/**
 * Load test
 * 
 * @author Taylor
 */
public class LoadTest {

	private final static String BASE_URL = "http://dse_ui.cloudfoundry.com";

	private final static int runs = 50;

	public static void main(String[] args) {

		long timeBefore = System.nanoTime();
		for (int i = 0; i < runs; i++) {

			RestAssured.baseURI = BASE_URL;
			RestAssured.port = 80;
			expect().statusCode(200).when().get("/notification/?id=51bdf32fc01182b4d6e87630");
		}
		long timeAfter = System.nanoTime();
		long duration = timeAfter - timeBefore;

		System.out.println(duration + " nanoseconds in total, averaged " + (duration / runs) + " nanoseconds per run.");

	}
}
