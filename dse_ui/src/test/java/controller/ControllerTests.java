package controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import dse_domain.domain.Hospital;
import dse_domain.domain.OpSlot;

/**
 * Tests for the PublicController
 * 
 * @author Taylor
 */
public class PublicControllerTest {

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
	 * Create test data
	 */
	@Before
	public void createTestSlots() {

		// Hospitals
		Hospital hospital1 = new Hospital(hospitalName1, null);
		Hospital hospital2 = new Hospital(hospitalName2, null);

		// Dates
		DateTime pastDate = new DateTime().minusYears(100);
		DateTime futureDate = new DateTime().plusYears(100);

		// OpSlots
		OpSlot slot1 = new OpSlot(hospital1, 60, OpSlot.Type.AUGEN, pastDate.toDate());
		OpSlot slot2 = new OpSlot(hospital2, 60, OpSlot.Type.AUGEN, pastDate.toDate());
		OpSlot slot3 = new OpSlot(hospital1, 60, OpSlot.Type.AUGEN, futureDate.toDate());
		OpSlot slot4 = new OpSlot(hospital2, 60, OpSlot.Type.AUGEN, futureDate.toDate());

		slots.add(slot1);
		slots.add(slot2);
		slots.add(slot3);
		slots.add(slot4);
	}

	@Test
	public void testOpSlotEmptyFilter() {
		assertEquals("Check size of original list of OP slots", 4, slots.size());
		List<OpSlot> filteredList = PublicController.filterOpSlotList(slots, "", "", "", "unset", "", "", "");
		assertEquals("Filter function should not remove any slots", slots.size(), filteredList.size());
	}

	@Test
	public void testOpSlotHospitalFilter() {
		assertEquals("Check size of original list of OP slots", 4, slots.size());
		List<OpSlot> filteredList = PublicController
				.filterOpSlotList(slots, "", "", "", "unset", hospitalName2, "", "");
		assertEquals("Filter function should remove 2 slots", 2, filteredList.size());
	}

	@Test
	public void testRemovePastOpSlots() {
		assertEquals("Check size of original list of OP slots", 4, slots.size());
		List<OpSlot> filteredList = PublicController.removePastSlots(slots);
		assertEquals("Filter function should remove 2 slots", 2, filteredList.size());
	}

}
