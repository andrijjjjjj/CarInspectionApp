package com.example.demo.web.dto;

public class UserRegistrationDto {
	private String firstName;
	private String lastName;
	private String email;
	private String birthday;
	private String phoneNumber;
	private String password;
	private boolean isSigned;
	
	public UserRegistrationDto(){
		
	}
	public UserRegistrationDto(String firstName, String lastName,  String password, String email, String birthday, String phoneNumber, boolean isSigned) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.birthday = birthday;
		this.phoneNumber = phoneNumber;
		this.isSigned = isSigned;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean getIsSigned() {
		return isSigned;
	}
	public void setIsSigned(boolean isSigned) {
		this.isSigned = isSigned;
	}
	
	
}
