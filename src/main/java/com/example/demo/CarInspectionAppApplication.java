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
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CarInspectionAppApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CarInspectionAppApplication.class, args);
	}
	
	@Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	      return builder.sources(CarInspectionAppApplication.class);
	  }
}