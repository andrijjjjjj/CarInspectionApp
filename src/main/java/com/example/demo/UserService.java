/*
 * UserService
 * ------------------------------------------------------------------
 * A service for Spring Security that allows
 * user data to be transferred into the database
 * and saved, along with user creation, and the
 * ability to return users based on their id.
 * ------------------------------------------------------------------
 */
package com.example.demo;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
	User getUserbyId(long id);
}
