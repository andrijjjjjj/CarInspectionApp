/*
 * User
 * ------------------------------------------------------------------
 * The User class is a blueprint of what makes up am user
 * on the CCSCC car inspection application. Personal 
 * information, documentation, photographs, cars, login
 * information, roles, and other information vital for
 * an user are declared here.
 * ------------------------------------------------------------------ 
 */
package com.example.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.data.annotation.Transient;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "user" , uniqueConstraints = @UniqueConstraint( columnNames="email"))
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name ="first_name")
	private String firstName;
	
	@Column(name ="last_name")
	private String lastName;
	
	private String email;
	
	private String password;
	
	private String birthday;
	
	private String phoneNumber;
	
	private boolean isSigned;
	
	private Date signedDate;
	
	private ArrayList<Car> myGarage = new ArrayList<Car>();
	
	@Column(nullable = true, length = 64)
	private String photo;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
		name = "users_roles",
		joinColumns = @JoinColumn(
				name = "user_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(
				name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;
	public User() {}
	
	public User(String firstName, String lastName, String password, String email, String birthday, String phoneNumber, boolean isSigned,Collection<Role> roles) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.roles = roles;
		this.birthday = birthday;
		this.phoneNumber = phoneNumber;
		this.isSigned = isSigned;
	}

	public void addCar(String make, String model, int year, String vehicleClass, String licensePlateNum) {
		 Car newCar = new Car(make, model, year, vehicleClass, licensePlateNum);
		 myGarage.add(newCar);
	}
	
	public void removeCar(int orderInGarage) {
		myGarage.remove(orderInGarage-1);
//		myGarage.remove(orderInGarage);
	}
	
//	public String listCarsInGarage() {
//		String cars ="";
//		for(int i = 0; i < myGarage.size(); i++) {
////			System.out.println("Car #"+(i+1)+" - "+myGarage.get(i).getYear()+" "+myGarage.get(i).getMake()+" "+myGarage.get(i).getModel()+" (Class "+myGarage.get(i).getVehicleClass()+")");
//			cars += myGarage.get(i).getYear()+" "+myGarage.get(i).getMake()+" "+myGarage.get(i).getModel()+" ("+myGarage.get(i).getVehicleClass()+")"+"\n";
//		}
//		return cars;
//	}
	
	public ArrayList<Car> getMyGarage() {
		return myGarage;
	}
	
	// use only for deleteCar!
	public int findCar(Car x) {
		int index = 0;
		for(int i = 0; i < myGarage.size(); i++)
		{
			if(myGarage.get(i).getLicensePlateNum() == x.getLicensePlateNum())
			{
				index = i;
			}
		}
		return index+1;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	public void setSigned(boolean isSigned) {
		this.isSigned = isSigned;
	}
	
	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	public String getPhoto() {
		return photo;
	}
	
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public Date getSignedDate() {
		return signedDate;
	}
	
	public void setSignedDate(Date signedDate) {
		this.signedDate = signedDate;
	}
	
	@Transient
	public String getUserImagePath(){
		if (photo == null || id == null) return null;
		
		return "/user-logos/" + id + "/" +photo;
	}
}
