package models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;


public class Hospital {
	
	private String name;
	
	@Id
	private String id;

	@GeoSpatialIndexed
	private double[] location;
	
	public Hospital(String name,double[] location) {
		this.name = name;
		this.location = location;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
