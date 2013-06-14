package dse_domain.domain;

/**
 * Model class for reservations.
 */
public class Reservation {

	private Doctor doctor;
	
	private Patient patient;

	public Reservation(Doctor doctor, Patient patient) {
		this.doctor = doctor;
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

	@Override
	public String toString() {
		return "Reservation [doctor=" + doctor + ", patient=" + patient + "]";
	}

}
