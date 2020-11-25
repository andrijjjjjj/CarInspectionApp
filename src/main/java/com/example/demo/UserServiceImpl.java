package com.example.demo;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Car;
import com.example.demo.Role;
import com.example.demo.User;
import com.example.demo.UserRepository;
import com.example.demo.UserRegistrationDto;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public UserServiceImpl() {
		super();
	}

	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public User save(UserRegistrationDto registrationDto) {
		User user = new User(registrationDto.getFirstName(),
				registrationDto.getLastName(), passwordEncoder.encode(registrationDto.getPassword()),registrationDto.getEmail(),
				registrationDto.getBirthday(),registrationDto.getPhoneNumber(),registrationDto.getIsSigned(),Arrays.asList(new Role("ROLE_USER")));
		
		return userRepository.save(user);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
	
		User user =userRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid Username or Password");
		}
		
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorties(user.getRoles()));
	}
	private Collection<? extends GrantedAuthority> mapRolesToAuthorties(Collection<Role> roles){
		
		
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public User getUserbyId(long id) {
		User user = null;
		Optional<User> optional = userRepository.findById(id);
		if(optional.isPresent()) {
			user = optional.get();
		}
		else {
			throw new RuntimeException("User not found");
		}
		return user;
	}
	


	  public void retrieve(long id) {
		 userRepository.findById(id); 
	  }
}
