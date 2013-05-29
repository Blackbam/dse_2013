package domain;

public class Reservation {
	
	private Doctor doctor;
	private Patient patient;
	private OpSlot op_slot;
	
	public Reservation(Doctor doctor, Patient patient, OpSlot op_slot) {
		this.doctor = doctor;
		this.op_slot = op_slot;
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

	public OpSlot getOp_slot() {
		return op_slot;
	}
	
}
