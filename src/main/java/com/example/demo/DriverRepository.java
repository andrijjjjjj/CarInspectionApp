package com.example.demo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.Driver;

public interface DriverRepository extends CrudRepository<Driver, String> {
//	@Override
//	ArrayList<Driver>findAll();
}
