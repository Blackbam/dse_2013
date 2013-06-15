package dse_domain.DTO;

import java.io.Serializable;
import java.util.Date;

import dse_domain.domain.OpSlot;
import dse_domain.domain.OpSlot.Type;

/**
 * Serializable reservation model.
 */
public class ReservationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String patientID;

	private String doctorID;

	private Date dateStart;

	private Date dateEnd;

	private OpSlot.Type type;

	private int minTime;

	private int maxDistance;

	public ReservationDTO(String patientID, String doctorID, Date dateStart, Date dateEnd, Type type, int minTime,
			int maxDistance) {
		this.patientID = patientID;
		this.doctorID = doctorID;
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

	public String getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(String doctorID) {
		this.doctorID = doctorID;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
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
		return "ReservationDTO [patientID=" + patientID + ", doctorID=" + doctorID + ", dateStart=" + dateStart
				+ ", dateEnd=" + dateEnd + ", type=" + type + ", minTime=" + minTime + ", maxDistance=" + maxDistance
				+ "]";
	}

}
