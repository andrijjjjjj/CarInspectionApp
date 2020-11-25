/*
 * Main SpringBoot Application
 * ------------------------------------------------------------------
 * This file runs our Car Inspection Application.
 * Run as... 'Spring Boot App'
 * ------------------------------------------------------------------
 */
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarInspectionAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarInspectionAppApplication.class, args);
	}
	
}