/*
 * UserRepository
 * ------------------------------------------------------------------
 * Allows public access to search the database
 * of users by their id, which is their email
 * address, and return a corresponding User 
 * object or null if not found.
 * ------------------------------------------------------------------
 */

package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);
}

