package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class CarInspectionAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarInspectionAppApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner loadData(DriverRepository repo, MainController controller) {
		return (args) -> {
			Driver a = new Driver("Andrij", "P*******", "01/02/3456", "123-456-7890", "Male", "andrij@email.com", "ap123456", "andrijspassword");
			Driver b = new Driver("Braden", "M*******", "02/03/4567", "987-654-3210", "Male", "braden@email.com", "bm234567", "bradenspassword");
			Driver c = new Driver("Lasean", "J*******", "03/04/5678", "123-789-4560", "Male", "lasean@email.com", "lj345678", "laseanspassword");
			
			a.myGarage.addCar("Acura", "TL", 2012, "A");
			b.myGarage.addCar("Nissan", "GTR", 2020, "B"); b.myGarage.addCar("Honda", "Civic Type-R", 2015, "C");
			c.myGarage.addCar("Lexus", "ISF", 2008, "D"); c.myGarage.addCar("Mercedes", "S550", 2018, "E"); c.myGarage.addCar("Porsche", "911 Carrera", 2017, "F");
			
			repo.save(a); repo.save(b); repo.save(c);
			
//			printToConsole(a); printToConsole(b); printToConsole(c);
		};
	}
	
	public void printToConsole(Driver x) {
		System.out.println(x.toString());
		System.out.println(x.getFirstName()+"'s Garage:");
		x.myGarage.listCarsInGarage();
		System.out.println("\n");
	}
}
