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

import javax.persistence.Column;

import org.springframework.data.annotation.Transient;

public class Car implements Serializable{
	
	/**
	 * New (unused id to solve 'problem from eclipse'
	 */
	private static final long serialVersionUID = 1L;
	private String make, model, vehicleClass, licensePlateNum;
	int year;
	private String carPhoto;
	
	public Car(String make, String model, int year, String vehicleClass, String licensePlateNum, String carPhoto) {
		this.make = make;
		this.model = model; 
		this.year = year;
		this.vehicleClass = vehicleClass;
		this.licensePlateNum = licensePlateNum;
		this.carPhoto = carPhoto;
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
	
	public String getCarPhoto() {
		return carPhoto;
	}

	public void setCarPhoto(String carPhoto) {
		this.carPhoto = carPhoto;
	}

	// differentiates vehicle if cars are identical
	public String getLicensePlateNum() {
		return licensePlateNum;
	}
	@Transient
	public String getCarImagePath(){
		if (carPhoto == null) return null;
		
		return "/user-logos/" + licensePlateNum + "/" + carPhoto;
		//return "/user-logos/" + id + "/" + photo;
	}
}