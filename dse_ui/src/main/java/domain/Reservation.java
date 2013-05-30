package domain;

public class Reservation {
	
	private Doctor doctor;
	private Patient patient;
	private OpSlot opSlot;
	
	public Reservation(Doctor doctor, Patient patient, OpSlot opSlot) {
		this.doctor = doctor;
		this.opSlot = opSlot;
		this.patient = patient;
	}
	
	public Doctor getDoctor() {
		return doctor;
	}

	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public OpSlot getOpSlot() {
		return opSlot;
	}
	
}
