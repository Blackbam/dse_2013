package dao;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

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
import dse_domain.util.DataCreator;

public class UiDaoTests {

	private static MongodExecutable mongodExe;
	private static MongodProcess mongod;
	private static Mongo mongo;
	private static UserInterfaceMongoDAO uiDao;
	private static MongoTemplate template;

	@BeforeClass
	public static void initResources() throws IOException {
		MongoDBRuntime runtime = MongoDBRuntime.getDefaultInstance();
		mongodExe = runtime.prepare(new MongodConfig(Version.V2_1_2, 27017, false));
		mongod = mongodExe.start();
		mongo = new Mongo("127.0.0.1", 27017);

		if (mongod != null) {
			mongodExe.cleanup();
		}

		template = new MongoTemplate(mongo, "dse");
		uiDao = new UserInterfaceMongoDAO(template);
	}

	@Before
	public void setUpTestData() {
		DataCreator.insertTestData(template);
	}

	@Test
	public void testPatientRetrieval() {
		assertEquals("Check number of patients", 6, uiDao.findAllPatients().size());
	}
}
