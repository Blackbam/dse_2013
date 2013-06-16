package Controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import controller.HomeController;
import controller.SlotController;
import dse_domain.domain.Hospital;
import dse_domain.domain.OpSlot;

/**
 * Tests for the controller classes.
 * 
 * @author Taylor
 */
public class ControllerTests {

	/**
	 * Name of test hospital 1
	 */
	private String hospitalName1 = "Hospital1";

	/**
	 * Name of test hospital 2
	 */
	private String hospitalName2 = "Hospital2";

	/**
	 * List of test slots
	 */
	private List<OpSlot> slots = new ArrayList<OpSlot>();

	/**
	 * Test date 1
	 */
	private DateTime pastDate1 = new DateTime().minusYears(100);

	/**
	 * Test date 2
	 */
	private DateTime pastDate2 = new DateTime().minusYears(50);

	/**
	 * Test date 3
	 */
	private DateTime futureDate1 = new DateTime().plusYears(50);

	/**
	 * Test date 4
	 */
	private DateTime futureDate2 = new DateTime().plusYears(100);

	/**
	 * Create test data
	 */
	@Before
	public void createTestSlots() {
		
		// Hospitals
		Hospital hospital1 = new Hospital(hospitalName1, null);
		Hospital hospital2 = new Hospital(hospitalName2, null);

		// OpSlots
		OpSlot slot1 = new OpSlot(hospital1, 60, OpSlot.Type.AUGENHEILKUNDE, pastDate1.toDate());
		OpSlot slot2 = new OpSlot(hospital2, 60, OpSlot.Type.AUGENHEILKUNDE, pastDate2.toDate());
		OpSlot slot3 = new OpSlot(hospital1, 60, OpSlot.Type.AUGENHEILKUNDE, futureDate1.toDate());
		OpSlot slot4 = new OpSlot(hospital2, 60, OpSlot.Type.AUGENHEILKUNDE, futureDate2.toDate());

		slots.add(slot2);
		slots.add(slot1);
		slots.add(slot4);
		slots.add(slot3);
	}

	@Test
	public void testOpSlotEmptyFilter() {
		assertEquals("Check size of original list of OP slots", 4, slots.size());
		List<OpSlot> filteredList = SlotController.filterOpSlotList(slots, null, null, null, "unset", null, null, null,
				null);
		assertEquals("Filter function should not remove any slots", slots.size(), filteredList.size());
	}

	@Test
	public void testOpSlotHospitalFilter() {
		assertEquals("Check size of original list of OP slots", 4, slots.size());
		List<OpSlot> filteredList = SlotController.filterOpSlotList(slots, null, null, null, "unset", hospitalName2,
				null, null, null);
		assertEquals("Filter function should remove 2 slots", 2, filteredList.size());
	}

	@Test
	public void testRemovePastOpSlots() {
		assertEquals("Check size of original list of OP slots", 4, slots.size());
		List<OpSlot> filteredList = SlotController.removePastSlots(slots);
		assertEquals("Filter function should remove 2 slots", 2, filteredList.size());
	}

	@Test
	public void testOpSlotSorting() {
		
		// Assert that the list is not already in order
		assertFalse(pastDate1.toDate() == slots.get(0).getDate());
		assertFalse(pastDate2.toDate() == slots.get(1).getDate());
		assertFalse(futureDate1.toDate() == slots.get(2).getDate());
		assertFalse(futureDate2.toDate() == slots.get(3).getDate());

		// Sort slot list
		Collections.sort(slots, new HomeController().new OpSlotComparator());
		
		// Assert that the list is now sorted
		assertEquals(pastDate1.toDate(), slots.get(0).getDate());
		assertEquals(pastDate2.toDate(), slots.get(1).getDate());
		assertEquals(futureDate1.toDate(), slots.get(2).getDate());
		assertEquals(futureDate2.toDate(), slots.get(3).getDate());
	}

}
