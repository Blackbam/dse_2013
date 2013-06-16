package dao;

import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import dao.impl.UserInterfaceMongoDAO;
import de.flapdoodle.embedmongo.MongoDBRuntime;
import de.flapdoodle.embedmongo.MongodExecutable;
import de.flapdoodle.embedmongo.MongodProcess;
import de.flapdoodle.embedmongo.config.MongodConfig;
import de.flapdoodle.embedmongo.distribution.Version;
import dse_domain.domain.Patient;

public class UiDaoTests {

	private static MongodExecutable mongodExe;
	private static MongodProcess mongod;
	private static DB db;
	private static Mongo mongo;
	private static UserInterfaceMongoDAO uiDao;

	@BeforeClass
	public static void initResources() throws IOException {
		MongoDBRuntime runtime = MongoDBRuntime.getDefaultInstance();
		mongodExe = runtime.prepare(new MongodConfig(Version.V2_1_2, 27017, false));
		mongod = mongodExe.start();
		mongo = new Mongo("127.0.0.1", 27017);
		db = mongo.getDB("dse");

		if (mongod != null) {
			mongodExe.cleanup();
		}

		MongoTemplate template = new MongoTemplate(mongo, "dse");
		uiDao = new UserInterfaceMongoDAO(template);
	}

	@Before
	public void setUpTestData() {

		DBCollection colPatient = db.getCollection("patients");
		colPatient.drop();
		colPatient = db.createCollection("patients", new BasicDBObject());

		Patient patient1 = new Patient();
		patient1.setFirstName("Franz");
		patient1.setLastName("Meier");
		// patient1.setLocation(new double[] { 48.207744, 16.377068 });
		// patient1.setUsername("Franz");
		// patient1.setPassword("Meier");

		colPatient.save(new BasicDBObject("testPatient1", patient1));
	}

	@Test
	public void test() {
		System.out.println("Found: " + uiDao.findAllPatients().size());
	}
}
