package domain;

import java.util.Date;

import org.springframework.data.annotation.Id;

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

	public enum Type {
		KARDIO, AUGEN, OTHER
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
