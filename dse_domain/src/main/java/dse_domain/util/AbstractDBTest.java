package dse_domain.util;

import org.junit.After;
import org.junit.Before;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;

import de.flapdoodle.embedmongo.MongoDBRuntime;
import de.flapdoodle.embedmongo.MongodExecutable;
import de.flapdoodle.embedmongo.MongodProcess;
import de.flapdoodle.embedmongo.config.MongodConfig;
import de.flapdoodle.embedmongo.distribution.Version;
import de.flapdoodle.embedmongo.runtime.Network;

/**
 * Abstract class providing generalized functionality required for testing the database and data access objects.
 * 
 * @author Taylor
 */
public abstract class AbstractDBTest {

	private static MongodExecutable mongodExe;

	private static MongodProcess mongod;

	protected static MongoTemplate template;

	@Before
	public void setUpTestData() throws Exception {
		MongoDBRuntime runtime = MongoDBRuntime.getDefaultInstance();
		mongodExe = runtime.prepare(new MongodConfig(Version.V2_2_0_RC0, 27017, Network.localhostIsIPv6()));
		mongod = mongodExe.start();
		Mongo mongo = new Mongo("localhost", 27017);

		template = new MongoTemplate(mongo, "dse");

		DataCreator.insertTestData(template);
	}

	@After
	public void cleanUpDB() {
		if (mongod != null) {
			mongod.stop();
			mongodExe.cleanup();
		}
	}

}
