package com.example.demo;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Driver {
	@Id
	private String username; // ID in database
	
	private String firstName, lastName, gender, email, password, birthday, phoneNumber;
	
	public Garage myGarage;
	
	protected Driver() {}

	public Driver(String firstName, String lastName, String birthday, String phoneNumber, String gender, 
			String email, String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.email = email;
		this.username = username;
		this.password = password;
		myGarage = new Garage();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String toString() {
		return firstName+" "+lastName+"\n"+birthday+"\n"+phoneNumber+"\n"+gender+"\n"+email+"\n"+username+"\n"+password;
	}
}
