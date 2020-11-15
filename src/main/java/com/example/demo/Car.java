package com.example.demo;

import javax.persistence.Embeddable;
import java.io.Serializable;

public class Car implements Serializable{
	
	private String make, model, vehicleClass;
	int year;
	
	public Car(String make, String model, int year, String vehicleClass) {
		this.make = make;
		this.model = model; 
		this.year = year;
		this.vehicleClass = vehicleClass;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public String getVehicleClass() {
		return vehicleClass;
	}

	public int getYear() {
		return year;
	}
	
	// no setters (cars shouldn't change information once created)
}
