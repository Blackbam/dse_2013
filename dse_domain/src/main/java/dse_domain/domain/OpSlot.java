package dse_domain.domain;

import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

/**
 * Model class for operation slots.
 */
public class OpSlot {

	@Id
	private String id;

	private Hospital hospital;

	private Date date;

	private int length;

	private Reservation reservation = null;

	private Type type;

	public OpSlot(Hospital hospital, int length, Type type, Date date) {
		this.hospital = hospital;
		this.length = length;
		this.type = type;
		this.date = date;
	}

	/**
	 * Type of operations that can be performed
	 * 
	 * @author Taylor
	 */
	public enum Type {
		AUGENHEILKUNDE, ORTHOPÄDIE, HNO, NEUROCHIRURGIE, KARDIOLOGIE, OTHER
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public String getDateString() {
		DateTime dt = new DateTime(date);
		return dt.toLocalDate().toString("dd.MM.yyyy");
	}

	public String getStartTimeString() {
		DateTime dt = new DateTime(date);
		return dt.toLocalTime().toString("HH:mm");
	}

	public String getEndTimeString() {
		DateTime dt = new DateTime(date);
		return dt.plusMinutes(length).toLocalTime().toString("HH:mm");
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public String getTypeString() {
		String typeString = type.toString();
		return typeString.substring(0, 1).toUpperCase() + typeString.substring(1).toLowerCase();
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
