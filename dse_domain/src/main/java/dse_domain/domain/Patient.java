package dse_domain.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

/**
 * Model class for patients.
 */
public class Patient extends Person implements Serializable {

	private static final long serialVersionUID = -2704716436489063116L;

	@GeoSpatialIndexed
	private double[] location;

	public Patient() {}

	public Patient(String firstName, String lastName, double[] location) {
		super(firstName, lastName);
		this.setLocation(location);
	}

	public double[] getLocation() {
		return location;
	}

	public String getLocationString() {
		return "(" + location[0] + ", " + location[1] + ")";
	}

	public void setLocation(double[] location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Patient [id = " + getId() + ", firstName = " + getFirstName() + ", lastName = " + getLastName()
				+ ", location = " + getLocationString() + "]";
	}
}
