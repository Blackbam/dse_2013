package dse_domain.domain;

import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

/**
 * Model class for hospitals.
 */
public class Hospital extends User {

	private String name;

	@GeoSpatialIndexed
	private double[] location;

	public Hospital(String name, double[] location) {
		this.name = name;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double[] getLocation() {
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}

}
