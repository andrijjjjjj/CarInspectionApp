package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.eats.model.User;
import com.example.demo.Driver;



@Service
@Transactional
public class driverService {
	
	private final DriverRepository driverRepository;
	

	public driverService(DriverRepository driverRepository) {
		
		this.driverRepository = driverRepository;
	}
	
	public void saveDriver(Driver driver) {
		
		 driverRepository.save(driver);
		}
	
	public Driver findByUsernameAndPassword(String username, String password) {
		return driverRepository.findByUsernameAndPassword(username, password);
	}
	

}
