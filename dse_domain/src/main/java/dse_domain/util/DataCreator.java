package dse_domain.util;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.MongoTemplate;

import dse_domain.domain.Doctor;
import dse_domain.domain.Hospital;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;

/**
 * Helper class to create test database data for unit tests.
 * 
 * @author Taylor
 */
public class DataCreator {

	public static final double[] WIEN = new double[] { 48.208889, 16.3725 };
	public static final double[] GRAZ = new double[] { 47.066667, 15.433333 };
	public static final double[] INNSBRUCK = new double[] { 47.266667, 11.383333 };
	public static final double[] SALZBURG = new double[] { 47.8, 13.033333 };
	public static final double[] LINZ = new double[] { 48.303056, 14.290556 };
	public static final double[] KLOSTERNEUBURG = new double[] { 48.304167, 16.316667 };
	public static final double[] TULLN = new double[] { 48.333333, 16.05 };

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

		Doctor doctor1 = new Doctor();
		doctor1.setTitle("Dr.");
		doctor1.setFirstName("Michael");
		doctor1.setLastName("Aufmesser");
		doctor1.setUsername("Michael");
		doctor1.setPassword("Aufmesser");

		Doctor doctor2 = new Doctor();
		doctor2.setTitle("Dr.");
		doctor2.setFirstName("David");
		doctor2.setLastName("Gott");
		doctor2.setUsername("David");
		doctor2.setPassword("Gott");

		Doctor doctor3 = new Doctor();
		doctor3.setTitle("Dr.");
		doctor3.setFirstName("Johannes");
		doctor3.setLastName("Gunst-Fehler");
		doctor3.setUsername("Johannes");
		doctor3.setPassword("Gunst-Fehler");

		Doctor doctor4 = new Doctor();
		doctor4.setTitle("Dr.");
		doctor4.setFirstName("Stefan");
		doctor4.setLastName("Augenblick");
		doctor4.setUsername("Stefan");
		doctor4.setPassword("Augenblick");

		template.save(doctor1);
		template.save(doctor2);
		template.save(doctor3);
		template.save(doctor4);

		Hospital hospital1 = new Hospital("AKH Wien", WIEN);
		hospital1.setUsername(hospital1.getName());
		hospital1.setPassword(hospital1.getName());

		Hospital hospital2 = new Hospital("LKH Klosterneuburg", KLOSTERNEUBURG);
		hospital2.setUsername(hospital2.getName());
		hospital2.setPassword(hospital2.getName());

		Hospital hospital3 = new Hospital("Barmherzige Brüder Wien", WIEN);
		hospital3.setUsername(hospital3.getName());
		hospital3.setPassword(hospital3.getName());

		Hospital hospital4 = new Hospital("LKH Tulln", TULLN);
		hospital4.setUsername(hospital4.getName());
		hospital4.setPassword(hospital4.getName());

		template.save(hospital1);
		template.save(hospital2);
		template.save(hospital3);
		template.save(hospital4);

		OpSlot opSlot1 = new OpSlot(hospital1, 60, OpSlot.Type.AUGENHEILKUNDE, DateTime.now().plusDays(10).toDate());
		OpSlot opSlot2 = new OpSlot(hospital2, 120, OpSlot.Type.HNO, DateTime.now().plusDays(11).toDate());
		OpSlot opSlot3 = new OpSlot(hospital3, 180, OpSlot.Type.KARDIOLOGIE, DateTime.now().plusDays(12).toDate());
		OpSlot opSlot4 = new OpSlot(hospital4, 240, OpSlot.Type.NEUROCHIRURGIE, DateTime.now().plusDays(13).toDate());
		OpSlot opSlot5 = new OpSlot(hospital4, 240, OpSlot.Type.ORTHOPÄDIE, DateTime.now().plusDays(14).toDate());
		OpSlot opSlot6 = new OpSlot(hospital2, 240, OpSlot.Type.ORTHOPÄDIE, DateTime.now().plusDays(14).toDate());

		template.save(opSlot1);
		template.save(opSlot2);
		template.save(opSlot3);
		template.save(opSlot4);
		template.save(opSlot5);
		template.save(opSlot6);
	}
}
