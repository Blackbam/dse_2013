package dao;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;

import dao.impl.UserInterfaceMongoDAO;
import de.flapdoodle.embedmongo.MongoDBRuntime;
import de.flapdoodle.embedmongo.MongodExecutable;
import de.flapdoodle.embedmongo.MongodProcess;
import de.flapdoodle.embedmongo.config.MongodConfig;
import de.flapdoodle.embedmongo.distribution.Version;
import de.flapdoodle.embedmongo.runtime.Network;
import dse_domain.util.DataCreator;

public class UiDaoTests {

	private static MongodExecutable mongodExe;
	private static MongodProcess mongod;
	private static Mongo mongo;
	private static UserInterfaceMongoDAO uiDao;
	private static MongoTemplate template;

	@Before
	public void setUpTestData() throws IOException {
		MongoDBRuntime runtime = MongoDBRuntime.getDefaultInstance();
	    mongodExe = runtime.prepare(new MongodConfig(Version.V2_2_0_RC0, 27017, Network.localhostIsIPv6()));
	    mongod = mongodExe.start();
	    mongo = new Mongo("localhost", 27017);

		template = new MongoTemplate(mongo, "dse");
		uiDao = new UserInterfaceMongoDAO(template);

		DataCreator.insertTestData(template);
	}

	@After
	public void cleanUpDB() {
		if (mongod != null) {
			mongodExe.cleanup();
		}
	}

	@Test
	public void testPatientRetrieval() {
		assertEquals("Check number of patients", 6, uiDao.findAllPatients().size());
	}

	@Test
	public void testDoctorRetrieval() {
		assertEquals("Check number of doctors", 4, uiDao.findAllDoctors().size());
	}

	@Test
	public void testHospitalRetrieval() {
		assertEquals("Check number of hospitals", 4, uiDao.findAllHospitals().size());
	}
}
