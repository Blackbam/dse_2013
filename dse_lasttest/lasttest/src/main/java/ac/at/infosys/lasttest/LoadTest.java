package ac.at.infosys.lasttest;

import static com.jayway.restassured.RestAssured.expect;

import com.jayway.restassured.response.Response;

public class LoadTest {

	private final static String url = "http://dse_ui.cloudfoundry.com/notification/?id=51bdf32fc01182b4d6e87630";

	public static void main(String[] args) {

		long timeBefore = System.nanoTime();
		Response response = expect().statusCode(200).when().get(url);
		long timeAfter = System.nanoTime();
		long duration = timeAfter - timeBefore;

		System.out.println(duration + " nanoseconds.");

	}
}
