package dse_domain.DTO;

import java.io.Serializable;

/**
 * Serializable operation slot model.
 */
public class OpSlotDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String hospitalID;

	private String date;

	private String startTime;

	private String endTime;

	public OpSlotDTO(String hospitalID, String date, String startTime, String endTime) {
		super();
		this.hospitalID = hospitalID;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getHospitalID() {
		return hospitalID;
	}

	public void setHospitalID(String hospitalID) {
		this.hospitalID = hospitalID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "OpSlotDTO [hospitalID=" + hospitalID + ", date=" + date + ", startTime=" + startTime + ", endTime="
				+ endTime + "]";
	}
}
