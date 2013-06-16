package dao;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import dao.impl.UserInterfaceMongoDAO;
import dse_domain.util.AbstractDBTest;

public class UiDaoTests extends AbstractDBTest {

	private static UserInterfaceMongoDAO uiDao;

	@Before
	public void getDao() {
		uiDao = new UserInterfaceMongoDAO(template);
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
