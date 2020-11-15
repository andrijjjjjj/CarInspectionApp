package com.example.demo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

//import com.eats.model.User;
import com.example.demo.Driver;

public interface DriverRepository extends CrudRepository<Driver, String> {
//	@Override
//	ArrayList<Driver>findAll();
	public Driver findByUsernameAndPassword(String username, String password);
}
