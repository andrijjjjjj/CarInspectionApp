package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
//import java.util.Calendar;
import java.util.Date;
//import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

//import com.example.demo.User;
//import com.example.demo.UserRepository;
//import com.example.demo.UserServiceImpl;



@Controller
public class MainController {
	
	@Autowired
	UserServiceImpl userServiceImpl;

	@Autowired
	UserRepository userRepository;
	
	User user;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	
	@GetMapping("/")
	public String home(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String Email = auth.getName();
//		User user = userRepository.findByEmail(Email);
		user = userRepository.findByEmail(Email);
		model.addAttribute("firstName", user.getFirstName());
		return"index";
	}
	
	//public Car(String make, String model, int year, String vehicleClass)
	@GetMapping("/carregistration")
	public String carRegistrationForm(Model model) {
		return "carregistration";
	}
	
	@PostMapping("/carregistration")
	public void carRegistrationFormSubmit(
			@RequestParam(value="carMake",required =false) String carMake, 
			@RequestParam(value="carModel",required =false) String carModel, 
			@RequestParam(value="carYear",required =false) String carYear,
			@RequestParam(value="carClass",required =false) String carClass,
			Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int year = Integer.parseInt(carYear);
		
		String Email = auth.getName();

		user = userRepository.findByEmail(Email);
		System.out.println(carMake+"\n"+carModel+"\n"+year+"\n"+carClass);
		user.addCar(carMake, carModel, year, carClass);
		user.listCarsInGarage();
//		userRepository.save(user);
	}
	
	//public Car(String make, String model, int year, String vehicleClass)
		@GetMapping("/myGarage")
		public String myGarage(Model model) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String Email = auth.getName();

			user = userRepository.findByEmail(Email);
			model.addAttribute("garageList", user.getGarageList());
			return "myGarage";
		}

	
	@GetMapping("/uploadimage")
	public String uploadImageForm(Model model) {
		return "uploadimage";
	}
	
	// Waiver Method START 
	@GetMapping("/waiver")
	public String waiverForm(Model model) {
		
		return "waiver";
	}
	
	@PostMapping("/waiver")
	public void waiverSubmit(@RequestParam(value="waiverVerify",required =false) String checkboxValue,Model model) {
		boolean waiverVerify = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("waiverVerify", waiverVerify);
		if (checkboxValue != null) {
			waiverVerify = true;
		}
		else {
			waiverVerify = false;
		}
		Date currentDate = new Date();

		String Email = auth.getName();

		User user = userRepository.findByEmail(Email);
		user.setSigned(waiverVerify);
		user.setSignedDate(currentDate);
		userRepository.save(user);
		
		
	}

	
	//WORKS NOW ADDS PICTURE TO LOGGED IN USER.
	@PostMapping("/saveUserPic")
	public String saveUser(@ModelAttribute(name = "user")User user, @RequestParam("driverImage") MultipartFile multipartFile) throws IOException{
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String Email = auth.getName();
		User savedUser = userRepository.findByEmail(Email);
		savedUser.setPhoto(fileName);
		userRepository.save(user);
		String uploadDir = "./user-logos/" + savedUser.getId();
		Path uploadPath = Paths.get(uploadDir);
		
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try(InputStream inputStream = multipartFile.getInputStream()){;
		Path filePath = uploadPath.resolve(fileName);
		System.out.println(filePath.toString());
		Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException e) {
			throw new IOException("Could not save uploaded file: " + fileName);
		}
		
		System.out.println("this thing is working");
		return "redirect:/uploadimage?success";
	} 
	
}
