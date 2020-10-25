package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarInspectionAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarInspectionAppApplication.class, args);
//		Driver a = new Driver("firstName", "lastName", "birthday", "phone", "gender", "email", "username", "password");
		Driver a = new Driver("Johnny", "Appleseed", "7/4/2001", "773-202-1234", "Male", "jappleseed@gmail.com", "japple", "1234abcd");
		a.myGarage.addCar("Ford", "Mustang", 2003, "D");
		a.myGarage.addCar("Nissan", "GT-R", 2020, "A");
		a.myGarage.addCar("Honda", "Civic Type R", 2019, "B");
		System.out.println(a.toString());
		System.out.println("\n\nCars:");
		a.myGarage.listCarsInGarage();
		
		a.myGarage.removeCar(2);
		System.out.println("\n\nCars:");
		a.myGarage.listCarsInGarage();
	}

}
