/*
 * Car
 * ------------------------------------------------------------------
 * This class is a part of User. Every User has an ArrayList of Cars
 * they can add and remove. Cars are stored in this ArrayList within
 * the User class. Car holds the make, model, year, and class of the 
 * car. The ArrayList that stores Cars in User is called myGarage.
 * ------------------------------------------------------------------
 */

package com.example.demo;

import java.io.Serializable;

public class Car implements Serializable{
	
	/**
	 * New (unused id to solve 'problem from eclipse'
	 */
	private static final long serialVersionUID = 1L;
	private String make, model, vehicleClass, licensePlateNum;
	int year;
	
	public Car(String make, String model, int year, String vehicleClass, String licensePlateNum) {
		this.make = make;
		this.model = model; 
		this.year = year;
		this.vehicleClass = vehicleClass;
		this.licensePlateNum = licensePlateNum;
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
	
	// differentiates vehicle if cars are identical
	public String getLicensePlateNum() {
		return licensePlateNum;
	}
	
}