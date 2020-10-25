package com.example.demo;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Garage {
	
	protected Garage() {}

	private ArrayList<Car> myGarage = new ArrayList<Car>();
	
	
	public void addCar(String make, String model, int year, String vehicleClass) {
		 Car newCar = new Car(make, model, year, vehicleClass);
		 myGarage.add(newCar);
	}
	
	public void removeCar(int orderInGarage) {
		myGarage.remove(orderInGarage-1);
	}
	
	public void listCarsInGarage() {
		for(int i = 0; i < myGarage.size(); i++) {
			System.out.println("Car #"+(i+1)+" - "+myGarage.get(i).getYear()+" "+myGarage.get(i).getMake()+" "+myGarage.get(i).getModel()+" (Class "+myGarage.get(i).getVehicleClass()+")");
		}
	}

	
}
