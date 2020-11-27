/*
 * MainController
 * ------------------------------------------------------------------
 * The controller controls the data flow into model object and
 * updates the view whenever data changes. It keeps view and 
 * model separate. All page redirections are done here.
 * ------------------------------------------------------------------
 */

package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
		user = userRepository.findByEmail(Email);
//		model.addAttribute("firstName", user.getFirstName());
		
//		model.addAttribute("myGarage", user.getMyGarage());
		model.addAttribute("photo", user.getUserImagePath());
		return"index";
	}
	
	@GetMapping("/addCar")
	public String addCarPre(Model model) {
		return "addCar";
	}
	
	@PostMapping("/addCar")
	public void addCarPost(
			@RequestParam(value="carMake",required =false) String carMake, 
			@RequestParam(value="carModel",required =false) String carModel, 
			@RequestParam(value="carYear",required =false) String carYear,
			@RequestParam(value="carClass",required =false) String carClass,
			@RequestParam(value="licensePlateNum",required =false) String licensePlateNum,
			@RequestParam(value="carPhoto", required =false) MultipartFile multipartFile,
			Model model) throws IOException {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int year = Integer.parseInt(carYear);
		
		String carPhoto;
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		carPhoto = fileName;
		String uploadDir = "./src/main/resources/static/user-logos/" + licensePlateNum;
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
		String newCar = year+" "+carMake+" "+carModel+" "+"("+carClass+") ["+licensePlateNum+" "+carPhoto+ "] was added to your garage!";
//		model.addAttribute("newCar", newCar);
		
		String Email = auth.getName();

		user = userRepository.findByEmail(Email);
	
//		System.out.println(carMake+"\n"+carModel+"\n"+year+"\n"+carClass);
		user.addCar(carMake, carModel, year, carClass, licensePlateNum,carPhoto);
		
		System.out.println(newCar);
//		System.out.println(user.listCarsInGarage());
		userRepository.save(user);
	}
	
	
	@GetMapping("/myGarage")
	public String myGarage(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String Email = auth.getName();

		user = userRepository.findByEmail(Email);
//		model.addAttribute("garageList", user.getGarageList());
		model.addAttribute("user", user);
		model.addAttribute("myGarage", user.getMyGarage());
		return "myGarage";
	}

	@GetMapping("/deleteCar")
	public String deleteCarPre(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String Email = auth.getName();

		user = userRepository.findByEmail(Email);
//		model.addAttribute("garageList", user.getGarageList());
		model.addAttribute("user", user);
		model.addAttribute("myGarage", user.getMyGarage());
//		user.getMyGarage().get(0).getLicensePlateNum();
//		user.findCar(x)
		return "deleteCar";
	}
	
	@PostMapping("/deleteCar")
	public String deleteCarPost(@RequestParam(value="index",required =false) String index, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String Email = auth.getName();
		user = userRepository.findByEmail(Email);
		
		int carIndex = Integer.parseInt(index);
		
		if(carIndex <= user.getMyGarage().size() && carIndex > 0)
		{
			System.out.println(	user.getMyGarage().get(carIndex-1).getYear()+" "+
								user.getMyGarage().get(carIndex-1).getMake()+" "+
								user.getMyGarage().get(carIndex-1).getModel()+" ("+
								user.getMyGarage().get(carIndex-1).getVehicleClass()+") ["+
								user.getMyGarage().get(carIndex-1).getLicensePlateNum()+"] was removed from your garage!");
			user.removeCar(carIndex);
			userRepository.save(user);
		}
		else {
			System.out.println("Illegal vehicle number (index) provided by user!\nNo car was removed from garage!\n");
		}
		
		
		return "deleteCar"; // change to mygarage
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

	@PostMapping("/saveUserPic")
	public String saveUser(@ModelAttribute(name = "user")User user, @RequestParam("driverImage") MultipartFile multipartFile) throws IOException{
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String Email = auth.getName();
		User savedUser = userRepository.findByEmail(Email);
		savedUser.setPhoto(fileName);
		userRepository.save(savedUser);
		String uploadDir = "./src/main/resources/static/user-logos/" + savedUser.getId();
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
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/adminPage")
	public String adminPage(Model model) {
		List<User> listUsers = new ArrayList<>();
		userRepository.findAll().forEach(user -> listUsers.add(user));
		
		model.addAttribute("user", userRepository.findAll());
		return "adminPage";
	}
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/deleteUser")
	public String showDeleteUser(Model model) {
		List<User> listUsers = new ArrayList<>();
		userRepository.findAll().forEach(user -> listUsers.add(user));
		model.addAttribute("user", userRepository.findAll());
		return "deleteUser";
	}
	
	@PostMapping("/deleteUser")
	public void deleteUser(@RequestParam(value="id",required =false)Long id, Model model ) {
		
	    User delUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
	    userRepository.delete(delUser);
	} 
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/AdminHomepage")
	public String AdminHomepage() {
		return "AdminHomepage";
	}
	@PreAuthorize("hasAnyRole('TECH','ADMIN')")
	@GetMapping("/TechHomepage")
	public String TechHomepage() {
		return "TechHomepage";
	}
	@PreAuthorize("hasAnyRole('TECH','ADMIN')")
	@GetMapping("/techShowUser")
	public String techShowUser(Model model){
		List<User> listUsers = new ArrayList<>();
		userRepository.findAll().forEach(user -> listUsers.add(user));
		
		model.addAttribute("user", userRepository.findAll());
		return "techShowUser";
	}
}
