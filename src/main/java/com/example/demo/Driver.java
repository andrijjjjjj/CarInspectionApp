package com.example.demo;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Driver {
	
	@Id
	private String username;
//	private String userID; // ID in database
	
	private String firstName, lastName, email, password, birthday, phoneNumber;
	private boolean isSigned;
	public Garage myGarage;
	
	protected Driver() {}

	public Driver(String firstName, String lastName, String birthday, String phoneNumber, 
			String email, String username, String password, boolean isSigned) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.username = username;
		this.password = password;
		this.isSigned = isSigned;
//		userID = firstName.substring(0,1) + lastName + birthday; // auto generate unique primary key: first letter of firstname + last name + birthday
//		userID = username;
		myGarage = new Garage();
	}

//	public String getUserID() {
//		return userID;
//	}

//	public void setUsername(String username) {
//		this.username = username;
//	}

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
	
	public boolean getIsSigned() {
		return isSigned;
	}

	public void setIsSigne(String phoneNumber) {
		this.isSigned = isSigned;
	}
	public String toString() {
//		return firstName+" "+lastName+"\n"+birthday+"\n"+phoneNumber+"\n"+gender+"\n"+email+"\n"+username+"\n"+password;
		return "Name:\t\t"+firstName+" "+lastName+"\nDOB:\t\t"+birthday+"\nPhone:\t\t"+phoneNumber+"\nGender:\t\t"+"\nEmail:\t\t"+email+"\nUsername:\t"+username+"\nPassword:\t"+password;
	}
}
