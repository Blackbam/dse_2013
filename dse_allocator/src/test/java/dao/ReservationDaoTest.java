package dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoResults;

import dao.impl.ReservationMongoDAO;
import dse_domain.domain.Hospital;
import dse_domain.domain.OpSlot;
import dse_domain.domain.Patient;
import dse_domain.util.AbstractDBTest;
import dse_domain.util.DataCreator;

/**
 * Tests for the ReservationDAO.
 * 
 * @author Taylor
 */
public class ReservationDaoTest extends AbstractDBTest {

	private static ReservationMongoDAO reservationDao;

	private static Patient patient = new Patient();

	@Before
	public void getDao() {
		reservationDao = new ReservationMongoDAO(template);
		patient.setFirstName("testname");
		patient.setLastName("lastname");
	}

	@Test
	public void testNearestHospitalVienna() {

		patient.setLocation(DataCreator.WIEN);

		int zeroCount = 0;
		GeoResults<Hospital> geoResults = reservationDao.findNearbyHospitals(1000, patient);
		for (GeoResult<Hospital> geo : geoResults) {
			if (geo.getDistance().getNormalizedValue() == 0) {
				zeroCount++;
			}
		}

		assertEquals("Check number of hospitals that are 0 kilometers away", 2, zeroCount);
	}

	@Test
	public void testNearestHospitalGraz() {

		patient.setLocation(DataCreator.GRAZ);

		int zeroCount = 0;
		GeoResults<Hospital> geoResults = reservationDao.findNearbyHospitals(1000, patient);
		for (GeoResult<Hospital> geo : geoResults) {
			if (geo.getDistance().getNormalizedValue() == 0) {
				zeroCount++;
			}
		}

		assertEquals("Check number of hospitals that are 0 kilometers away", 0, zeroCount);
	}

	@Test
	public void testNearestHospitalNoneInRage() {

		patient.setLocation(DataCreator.INNSBRUCK);

		int foundCount = 0;
		GeoResults<Hospital> geoResults = reservationDao.findNearbyHospitals(10, patient);
		for (GeoResult<Hospital> geo : geoResults) {
			if (geo.getDistance().getNormalizedValue() == 0) {
				foundCount++;
			}
		}

		assertEquals("Check number of hospitals that within 10 kilometers", 0, foundCount);
	}

	@Test
	public void testFreeSlotLookupAtHospital() {

		patient.setLocation(DataCreator.KLOSTERNEUBURG);

		GeoResults<Hospital> geoResults = reservationDao.findNearbyHospitals(1, patient);
		for (GeoResult<Hospital> geo : geoResults) {

			List<OpSlot> hnoSlots = reservationDao.findFreeOPSlotsInHospitalSortedList(DateTime.now().toDate(),
					DateTime.now().plusDays(50).toDate(), 0, geo.getContent(), OpSlot.Type.HNO);

			List<OpSlot> eyeSlots = reservationDao.findFreeOPSlotsInHospitalSortedList(DateTime.now().toDate(),
					DateTime.now().plusDays(50).toDate(), 0, geo.getContent(), OpSlot.Type.AUGENHEILKUNDE);

			assertEquals("Check the number of HNO slots available", 1, hnoSlots.size());
			assertEquals("Check the number of Optic slots available", 0, eyeSlots.size());

		}
	}

	@Test
	public void testFreeSlotLookupNearestPatient() {

		patient.setLocation(DataCreator.TULLN);

		OpSlot bestSlot = reservationDao.findFreeOPSlotInNearHospital(50, patient, DateTime.now().toDate(), DateTime
				.now().plusDays(50).toDate(), 0, OpSlot.Type.ORTHOPÄDIE);

		assertEquals(bestSlot.getHospital().getLocation()[0], DataCreator.TULLN[0], 0.01);
		assertEquals(bestSlot.getHospital().getLocation()[1], DataCreator.TULLN[1], 0.01);

	}
}
