package dse_domain.util;

import org.springframework.data.mongodb.core.MongoTemplate;

import dse_domain.domain.Patient;

public class DataCreator {

	private static final double[] WIEN = new double[] { 48.208889, 16.3725 };
	private static final double[] GRAZ = new double[] { 47.066667, 15.433333 };
	private static final double[] INNSBRUCK = new double[] { 47.266667, 11.383333 };
	private static final double[] SALZBURG = new double[] { 47.8, 13.033333 };
	private static final double[] LINZ = new double[] { 48.303056, 14.290556 };

	public static void insertTestData(MongoTemplate template) {

		Patient patient1 = new Patient();
		patient1.setFirstName("Franz");
		patient1.setLastName("Meier");
		patient1.setLocation(WIEN);
		patient1.setUsername("Franz");
		patient1.setPassword("Meier");

		Patient patient2 = new Patient();
		patient1.setFirstName("Hermine");
		patient1.setLastName("Müller");
		patient1.setLocation(WIEN);
		patient1.setUsername("Hermine");
		patient1.setPassword("Müller");

		Patient patient3 = new Patient();
		patient1.setFirstName("Markus");
		patient1.setLastName("Moser");
		patient1.setLocation(GRAZ);
		patient1.setUsername("Markus");
		patient1.setPassword("Moser");

		Patient patient4 = new Patient();
		patient1.setFirstName("Beatrix");
		patient1.setLastName("Bauer");
		patient1.setLocation(INNSBRUCK);
		patient1.setUsername("Beatrix");
		patient1.setPassword("Bauer");

		Patient patient5 = new Patient();
		patient1.setFirstName("Ben");
		patient1.setLastName("Bäcker");
		patient1.setLocation(SALZBURG);
		patient1.setUsername("Ben");
		patient1.setPassword("Bäcker");

		Patient patient6 = new Patient();
		patient1.setFirstName("Gloria");
		patient1.setLastName("Fasching");
		patient1.setLocation(LINZ);
		patient1.setUsername("Gloria");
		patient1.setPassword("Fasching");

		template.save(patient1);
		template.save(patient2);
		template.save(patient3);
		template.save(patient4);
		template.save(patient5);
		template.save(patient6);
	}

}
