package com.example.demo;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.demo.User;
import com.example.demo.UserRegistrationDto;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
	User getUserbyId(long id);
}
