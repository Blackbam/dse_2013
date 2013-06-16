package controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import dse_domain.domain.OpSlot;

/**
 * Abstract controller providing generalized methods for filtering and sorting operation slot lists.
 * 
 * @author Taylor
 */
public abstract class SlotController {

	static final Logger logger = Logger.getLogger(SlotController.class);

	/**
	 * Filter out operation slots that have already occurred.
	 * 
	 * @param opSlots
	 *            a list of operation slots
	 * @return
	 */
	public static List<OpSlot> removePastSlots(List<OpSlot> opSlots) {
		List<OpSlot> toRemove = new ArrayList<OpSlot>();
		for (OpSlot slot : opSlots) {
			DateTime dt = new DateTime(slot.getDate());
			if (dt.isBeforeNow()) {
				toRemove.add(slot);
			}
		}
		opSlots.removeAll(toRemove);
		return opSlots;
	}

	/**
	 * Filter a list of OpSlots according to given criteria.
	 * 
	 * @param slots
	 *            the list of slots to filter
	 * @param date
	 *            a date string to filter by
	 * @param from
	 *            the starting time to filter by
	 * @param to
	 *            the end time to filter by
	 * @param status
	 *            a status string to filter by
	 * @param hospital
	 *            a hospital name to filter by
	 * @param doctor
	 *            a doctor's name to filter by
	 * @param type
	 *            an operation type to filter by
	 * @return
	 */
	public static List<OpSlot> filterOpSlotList(List<OpSlot> slots, String date, String from, String to, String status,
			String hospital, String doctor, String type, String patient) {

		// Filter list according to received criteria
		List<OpSlot> toRemove = new ArrayList<OpSlot>();
		for (OpSlot slot : slots) {

			// date
			String dateString = null;
			if (slot.getDate() != null) {
				DateTime dt = new DateTime(slot.getDate());
				dateString = dt.toLocalDate().toString("dd.MM.yyyy");
			}
			if (date != null && !date.isEmpty() && !date.equals(dateString)) {
				// logger.debug("Removing slot {" + slot.toString() + "} due to not matching date criteria.");
				toRemove.add(slot);
				continue;
			}

			// from
			if (from != null && !from.isEmpty()) {
				DateTime slotEndTime = new DateTime(slot.getDate()).plusMinutes(slot.getLength());
				String[] split = from.split(":");
				Integer fromHour = Integer.valueOf(split[0]);
				Integer fromMinute = Integer.valueOf(split[1]);

				if (slotEndTime.getHourOfDay() < fromHour
						|| (slotEndTime.getHourOfDay() == fromHour && slotEndTime.getMinuteOfHour() < fromMinute)) {
					// logger.debug("Removing slot {" + slot.toString() + "} due to not matching from time criteria.");
					toRemove.add(slot);
					continue;
				}
			}

			// to
			if (to != null && !to.isEmpty()) {
				DateTime slotStartTime = new DateTime(slot.getDate());
				String[] split = to.split(":");
				Integer fromHour = Integer.valueOf(split[0]);
				Integer fromMinute = Integer.valueOf(split[1]);

				if (slotStartTime.getHourOfDay() > fromHour
						|| (slotStartTime.getHourOfDay() == fromHour && slotStartTime.getMinuteOfHour() > fromMinute)) {
					// logger.debug("Removing slot {" + slot.toString() + "} due to not matching to time criteria.");
					toRemove.add(slot);
					continue;
				}
			}

			// status
			String reserved = "available";
			if (slot.getReservation() != null) {
				reserved = "reserved";
			}
			if (status != null && !status.equalsIgnoreCase("unset") && !status.equalsIgnoreCase(reserved)) {
				// logger.debug("Removing slot {" + slot.toString() + "} due to not matching status criteria.");
				toRemove.add(slot);
				continue;
			}

			// Hospital
			if (hospital != null && !hospital.isEmpty()
					&& !slot.getHospital().getName().toUpperCase().contains(hospital.toUpperCase())) {
				// logger.debug("Removing slot {" + slot.toString() + "} due to not matching hospital criteria.");
				toRemove.add(slot);
				continue;
			}

			// Doctor
			String fullname = "";
			if (slot.getReservation() != null && slot.getReservation().getDoctor() != null) {
				String firstName = slot.getReservation().getDoctor().getFirstName().toUpperCase();
				String lastName = slot.getReservation().getDoctor().getLastName().toUpperCase();
				fullname = firstName + " " + lastName;
			}
			if (doctor != null && !doctor.isEmpty() && !fullname.contains(doctor.toUpperCase())) {
				// logger.debug("Removing slot {" + slot.toString() + "} due to not matching doctor criteria.");
				toRemove.add(slot);
				continue;
			} else if (doctor != null && !doctor.isEmpty() && slot.getReservation() == null) {
				// logger.debug("Removing slot {" + slot.toString() + "} due to not matching doctor criteria.");
				toRemove.add(slot);
				continue;
			}

			// Patient
			fullname = "";
			if (slot.getReservation() != null && slot.getReservation().getPatient() != null) {
				String firstName = slot.getReservation().getPatient().getFirstName().toUpperCase();
				String lastName = slot.getReservation().getPatient().getLastName().toUpperCase();
				fullname = firstName + " " + lastName;
			}
			if (patient != null && !patient.isEmpty() && !fullname.contains(patient.toUpperCase())) {
				// logger.debug("Removing slot {" + slot.toString() + "} due to not matching patient criteria.");
				toRemove.add(slot);
				continue;
			} else if (patient != null && !patient.isEmpty() && slot.getReservation() == null) {
				// logger.debug("Removing slot {" + slot.toString() + "} due to not matching patient criteria.");
				toRemove.add(slot);
				continue;
			}

			// Type
			if (type != null && !type.isEmpty() && !slot.getTypeString().toUpperCase().contains(type.toUpperCase())) {
				// logger.debug("Removing slot {" + slot.toString() + "} due to not matching type criteria.");
				toRemove.add(slot);
				continue;
			}
		}

		// Remove all slots that do not match criteria
		slots.removeAll(toRemove);

		return slots;
	}

	/**
	 * Custom comparator used to sort lists of OpSlots
	 * 
	 * @author Taylor
	 */
	public class OpSlotComparator implements Comparator<OpSlot> {

		@Override
		public int compare(OpSlot slot1, OpSlot slot2) {

			DateTime slot1Dt = new DateTime(slot1.getDate());
			DateTime slot2Dt = new DateTime(slot2.getDate());

			if (slot1Dt.isBefore(slot2Dt)) {
				return -1;
			} else if (slot2Dt.isBefore(slot1Dt)) {
				return 1;
			} else {
				return 0;
			}
		}
	}

}
