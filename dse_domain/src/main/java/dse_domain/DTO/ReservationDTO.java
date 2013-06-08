package dse_domain.DTO;

import java.io.Serializable;

import dse_domain.domain.OpSlot;
import dse_domain.domain.OpSlot.Type;

public class ReservationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String patientID;
	private String dateStart;
	private String dateEnd;
	private OpSlot.Type type;
	private int minTime;
	private int maxDistance;

	public ReservationDTO(String patientID, String dateStart, String dateEnd,
			Type type, int minTime, int maxDistance) {
		this.patientID = patientID;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.type = type;
		this.minTime = minTime;
		this.maxDistance = maxDistance;
	}

	public String getPatientID() {
		return patientID;
	}

	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public OpSlot.Type getType() {
		return type;
	}

	public void setType(OpSlot.Type type) {
		this.type = type;
	}

	public int getMinTime() {
		return minTime;
	}

	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}

	public int getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(int maxDistance) {
		this.maxDistance = maxDistance;
	}

	@Override
	public String toString() {
		return "ReservationDTO [patientID=" + patientID + ", dateStart="
				+ dateStart + ", dateEnd=" + dateEnd + ", type=" + type
				+ ", minTime=" + minTime + ", maxDistance=" + maxDistance + "]";
	}

}
