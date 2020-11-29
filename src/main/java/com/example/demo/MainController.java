/*
 * MainController
 * ------------------------------------------------------------------
 * The controller controls the data flow into model object and
 * updates the view whenever data changes. It keeps view and 
 * model separate. All page redirections are done here.
 * ------------------------------------------------------------------
 */

package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class MainController {
	
	@Autowired
	UserServiceImpl userServiceImpl;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	User user;
	
	long throughputId;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	// new andrij
	@PostMapping("/login")
	public String loginPost() {
		return "index";
	}
	
	
	@GetMapping("/")
	public String home(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String Email = auth.getName();
		user = userRepository.findByEmail(Email); // new!! andrij
		model.addAttribute("user", user); // new!! andrij
		Calendar c = Calendar.getInstance();
		String currentYear = new SimpleDateFormat("yyyy").format(new Date());
		int yearPlusOne = Integer.parseInt(currentYear);
		String reminder = "";
		yearPlusOne = yearPlusOne + 1;
		//sets Month
		c.set(Calendar.MONTH, 00);
		
		c.set(Calendar.DATE, 01);
		
		c.set(Calendar.YEAR, yearPlusOne);
		
		Date nextNewYear = c.getTime();
		System.out.println(yearPlusOne);
		System.out.println(nextNewYear);
		if(user.getSignedDate().after(nextNewYear)) {
			user.setSigned(false);
			userRepository.save(user);
			reminder = "Remember to sign your yearly waiver!";
			
		}
		model.addAttribute("reminder", reminder);
//		model.addAttribute("myGarage", user.getMyGarage());
//		model.addAttribute("photo", user.getUserImagePath());
		return"index";
	}
	
	@GetMapping("/addCar")
	public String addCarPre(Model model) {
		return "addCar";
	}
	
	// modified nov 27, 2020 andrij
	@PostMapping("/addCar")
	public void addCarPost(
			@RequestParam(value="carMake",required =false) String carMake, 
			@RequestParam(value="carModel",required =false) String carModel, 
			@RequestParam(value="carYear",required =false) String carYear,
			@RequestParam(value="carClass",required =false) String carClass,
			@RequestParam(value="licensePlateNum",required =false) String licensePlateNum,
			@RequestParam(value="carPhoto", required =false) MultipartFile multipartFile,
			Model model) throws IOException {
		
		// Security authenticator
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		// Convert year from String to int
		int year = Integer.parseInt(carYear);
		
		String carPhoto = null, fileName, uploadDir, newCar;
		uploadDir = "./src/main/resources/static/pictures/" + user.getId() + "/cars/" + licensePlateNum;
		Path uploadPath = Paths.get(uploadDir);
		if(!Files.exists(uploadPath)) 
		{
			Files.createDirectories(uploadPath);
		}
		
		// photo logic:
		if(multipartFile.isEmpty())
		{
			// if no photo uploaded
			
			// if directory doesn't exist, create it!
//			if(!Files.exists(uploadPath)) 
//			{
//				Files.createDirectories(uploadPath);
//			}
			System.out.println("No photo uploaded.");
		}
		else
		{
			// if photo uploaded
			fileName = licensePlateNum+"ProfilePic.jpg";
			carPhoto = fileName;
			try(InputStream inputStream = multipartFile.getInputStream())
			{
				Path filePath = uploadPath.resolve(fileName);
				System.out.println(filePath.toString());
				Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			catch(IOException e) 
			{
				throw new IOException("Could not save uploaded file: " + fileName);
			}
		}
		
		
		
		
		
		
//		String carPhoto = null;
//		
//		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//		fileName = licensePlateNum+"ProfilePic.jpg";
//		carPhoto = fileName;
//
//		String uploadDir = "./src/main/resources/static/pictures/" + user.getId() + "/cars/" + licensePlateNum;
//		Path uploadPath = Paths.get(uploadDir);
//		if(!Files.exists(uploadPath)) {
//			Files.createDirectories(uploadPath);
//		}
//		
//		try(InputStream inputStream = multipartFile.getInputStream()){;
//		Path filePath = uploadPath.resolve(fileName);
//		System.out.println(filePath.toString());
//		Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
//		}catch(IOException e) {
//			throw new IOException("Could not save uploaded file: " + fileName);
//		}
		
		newCar = year+" "+carMake+" "+carModel+" "+"("+carClass+") ["+licensePlateNum+" "+carPhoto+ "] was added to your garage!";

		
		
		
		
		
		String Email = auth.getName();
		user = userRepository.findByEmail(Email);
		user.addCar(carMake, carModel, year, carClass, licensePlateNum,carPhoto);
		System.out.println(newCar);
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
		
		
		return "redirect:/myGarage"; // change to mygarage
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
	public String waiverSubmit(@RequestParam(value="waiverVerify",required =false) String checkboxValue,Model model) {
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
		
		return"redirect:/";
	}

	@PostMapping("/saveUserPic")
	public String saveUser(@ModelAttribute(name = "user")User user, @RequestParam("driverImage") MultipartFile multipartFile) throws IOException{
//		String fileName = "profilepic"+StringUtils.cleanPath(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")));
		String fileName = "profilepic.jpg";

//		fileName = user.getFirstName()+"profilepic";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String Email = auth.getName();
		User savedUser = userRepository.findByEmail(Email);
//		String filename = p.getId()+"."+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
		savedUser.setPhoto(fileName);
		userRepository.save(savedUser);
//		"/pictures/" + id + "/profile/" + photo;
		String uploadDir = "./src/main/resources/static/pictures/" + savedUser.getId() + "/profile/";
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
	public String deleteUser(@RequestParam(value="id",required =false)Long id, Model model ) {
		
	    User delUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
	    userRepository.delete(delUser);
	    
	    return"redirect:/AdminHomepage";
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
	
	long userCarsId;
	@PreAuthorize("hasAnyRole('TECH','ADMIN')")
	@GetMapping("/techShowUserCars/{id}")
	public String techShowUserCars(@PathVariable("id") long id, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String Email = auth.getName();

		user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userCarsId = id;
		System.out.println(userCarsId);
		model.addAttribute("user", user);
		model.addAttribute("myGarage", user.getMyGarage());
		return "techShowUserCars";
	}
	String throughputLicPlateNo;
	
	@PreAuthorize("hasAnyRole('TECH','ADMIN')")
	@GetMapping("/techInspection/{carLiscPlateNo}")
	public String techInspection(@PathVariable("carLiscPlateNo") String liscPlateNo) {
		throughputLicPlateNo = "";
		throughputLicPlateNo = liscPlateNo;
		return "techInspection";
	}
	String inspectNote= "";
	@PostMapping("/techInspection")
	public String postTechInspection(@RequestParam(value="playInWheelBearings", required = false)String playInWheelBearings,
				@RequestParam(value="wheelsAreTight", required = false)String wheelsAreTight, @RequestParam(value="hubcapsRemoved", required = false)String hubcapsRemoved,
				@RequestParam(value="tiresInGoodCondition", required = false)String tiresInGoodCondition,@RequestParam(value="tireTreadDepth", required = false)String tireTreadDepth,
				@RequestParam(value="brakePadsGoodCondition", required = false)String brakePadsGoodCondition, @RequestParam(value="looseBodyPanels", required = false)String looseBodyPanels,
				@RequestParam(value="numbersOnAndVisible", required = false)String numbersOnAndVisible, @RequestParam(value="looseItemsRemoved", required = false)String looseItemsRemoved,
				@RequestParam(value="pedalsSecure", required = false)String pedalsSecure, @RequestParam(value="firmBreakPedal", required = false)String firmBreakPedal,
				@RequestParam(value="noPlayInSteering", required = false)String noPlayInSteering, @RequestParam(value="noPlayInSelector", required = false)String noPlayInSelector,
				@RequestParam(value="seatInOrder", required = false)String seatInOrder, @RequestParam(value="seatBelt", required = false)String seatBelt, 
				@RequestParam(value="gearMounted", required = false)String gearMounted, @RequestParam(value="batterySecure", required = false)String batterySecure,
				@RequestParam(value="airIntakeSecure", required = false)String airIntakeSecure, @RequestParam(value="throttleCableSecure", required = false)String throttleCableSecure,
				@RequestParam(value="fluidCapsSecure", required = false)String fluidCapsSecure, @RequestParam(value="noMajorLeaks", required = false)String noMajorLeaks,
				@RequestParam(value="trunkEmpty", required = false)String trunkEmpty, @RequestParam(value="functionalExaust", required = false)String functionalExaust,
				@RequestParam(value="inspectorsNotes", required = false) String inspectorsNotes) throws FileNotFoundException {
		User carUser = userRepository.findById(userCarsId).orElseThrow(() -> new IllegalArgumentException("INVALID HERE"));
		String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String currentDriver = carUser.getFirstName() + " " + carUser.getLastName();
		String carMake = carUser.getMyGarage().get(carUser.findCarByLicenseTest(throughputLicPlateNo)).getMake();
		String carModel = carUser.getMyGarage().get(carUser.findCarByLicenseTest(throughputLicPlateNo)).getModel();
		long driverId = carUser.getId();
		int carYear = carUser.getMyGarage().get(carUser.findCarByLicenseTest(throughputLicPlateNo)).getYear();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String Email = auth.getName();
		User techUser = userRepository.findByEmail(Email);
		String currentTech = techUser.getFirstName() + " " + techUser.getLastName();
		String unChecked = "Inspection Report for " + currentDriver +"'s "+ carYear +" "+ carMake +" "+carModel +" on "+ currentDate +" by " + currentTech + ".\r\n" + "\r\n" +"Unchecked Items: " + "\r\n" ;
	
		inspectNote = inspectorsNotes;
		if(playInWheelBearings != null) {
			 
		}
		else {
			unChecked = unChecked.concat("There was Some Play In the wheel bearings," + "\r\n");
		}
		
		if(wheelsAreTight != null) {
			
		}
		else {
			unChecked = unChecked.concat("Wheels Are Not Tight," + "\r\n");
		}
		
		if(hubcapsRemoved != null) {
			
		}
		else {
			unChecked = unChecked.concat("Hubcaps were not removed or firmly secured by lug nuts,"+ "\r\n");
		}
		
		if(tiresInGoodCondition != null) {
			
		}
		else {
			unChecked = unChecked.concat("Tries were not in good condition,"+ "\r\n");
		}
		
		if(tireTreadDepth != null) {
			
		}
		else {
			unChecked = unChecked.concat("Tire tread Depth was not appropriate,"+ "\r\n");
		}
		
		if(brakePadsGoodCondition != null) {
			
		}
		else {
			unChecked = unChecked.concat("Brake pads were not in good condition,"+ "\r\n");
		}
		
		if(looseBodyPanels != null) {
			
		}
		else {
			unChecked = unChecked.concat("Some body panels were loose,"+ "\r\n");
		}
		
		if(numbersOnAndVisible != null) {
			
		}
		else {
			unChecked = unChecked.concat("Numbers were not on the vehicle or visible," + "\r\n");
		}
		
		if(looseItemsRemoved != null) {
			
		}
		else {
			unChecked = unChecked.concat("There were loose items in the car," + "\r\n");
		}
		
		if(pedalsSecure != null) {
			
		}
		else {
			unChecked = unChecked.concat("Brake, Throttle, or Clutch was not secure," + "\r\n");
		}
		
		if(firmBreakPedal != null) {
			
		}
		else {
			unChecked = unChecked.concat("Brake pedal was not firm," + "\r\n");
		}
		
		if(noPlayInSteering != null) {
			
		}
		else {
			unChecked = unChecked.concat("There was some excessive play in steering," + "\r\n");
		}
		
		if(noPlayInSelector != null) {
			
		}
		else {
			unChecked = unChecked.concat("There was some excessive play in the gear selector," + "\r\n");
		}
		
		if(seatInOrder != null) {
			
		}
		else {
			unChecked = unChecked.concat("Seat was not in functional order," + "\r\n");
		}
		
		if(seatBelt != null) {
			
		}
		else {
			unChecked = unChecked.concat("Seat belt was not proper for year of car and class," + "\r\n");
		}
		
		if(gearMounted != null) {
			
		}
		else {
			unChecked = unChecked.concat("Camreas, Phones, and other gear was not mounted to the car," + "\r\n");
		}
		
		if( batterySecure != null) {
			
		}
		else {
			unChecked = unChecked.concat("Battery and connections were not secure," + "\r\n");
		}
		
		if( airIntakeSecure != null) {
			
		}
		else {
			unChecked = unChecked.concat("Aire intake or Airbox was not secure," + "\r\n");
		}
		
		if( throttleCableSecure != null) {
			
		}
		else {
			unChecked = unChecked.concat("Throttle cable was not secure," + "\r\n");
		}
		
		if( fluidCapsSecure != null) {
			
		}
		else {
			unChecked = unChecked.concat("All fluid caps were not secure," + "\r\n");
		}
		
		if( noMajorLeaks != null) {
			
		}
		else {
			unChecked = unChecked.concat("There was a major leak apparent somewhere on the vehicle," + "\r\n");
		}
		
		if( trunkEmpty != null) {
			
		}
		else {
			unChecked = unChecked.concat("Trunk was not empty" + "\r\n");
		}
		
		if( functionalExaust != null) {
			
		}
		else {
			unChecked = unChecked.concat("Exaust system was not functional or was excessively loud or does not exit behind driver," + "\r\n");
		}
		unChecked = unChecked.concat("\r\n" + "\r\n");
		if(inspectorsNotes != null ) {
			unChecked = unChecked.concat("Inspectors Notes: " + "\r\n");
			unChecked = unChecked.concat(inspectNote);
			System.out.println(inspectNote);
		}
		else {
			
		}
		
		
		//.getInspections().add(unChecked);
		System.out.println(throughputLicPlateNo);
		
	/*	System.out.println(carUser.getMyGarage().get(carUser.findCarByLicenseTest(throughputLicPlateNo)).getModel());
		carUser.getMyGarage().get(carUser.findCarByLicenseTest(throughputLicPlateNo)).makeArrayListNotNull();
		System.out.println(carUser.getMyGarage().get(carUser.findCarByLicenseTest(throughputLicPlateNo)).getInspect());
		carUser.getMyGarage().get(carUser.findCarByLicenseTest(throughputLicPlateNo)).getInspect().add(unChecked);
		System.out.println(carUser.getMyGarage().get(carUser.findCarByLicenseTest(throughputLicPlateNo)).getInspect()); */
		System.out.println(unChecked +" "+ throughputLicPlateNo);
		
		String upDir = "./src/main/resources/static/inspections/"+ driverId+"/"+throughputLicPlateNo;
		File theDir = new File(upDir);
		if(!theDir.exists()) {
			theDir.mkdirs();
		}
		String inspectFilename = "./src/main/resources/static/inspections/"+ driverId+"/"+throughputLicPlateNo+"/"+currentDate+throughputLicPlateNo+".txt";


		PrintWriter printw = new PrintWriter(inspectFilename);
		
		printw.println(unChecked);
		printw.close();
		
		return"redirect:/TechHomepage";
	}
	String passthroughCarLic;
	@GetMapping("/showInspections/{lisPlateNo}")
	public String showInspecitions(@PathVariable("lisPlateNo") String liscPlateNo, Model model) {
		passthroughCarLic = "";
		//String currentCarLic = user.getMyGarage().get(user.findCarByLicense(liscPlateNo)).getLicensePlateNum();
		passthroughCarLic = liscPlateNo;
		System.out.println(liscPlateNo);
		
		return "showInspections";
	}
	String content = "";
	@PostMapping("/showInspections")
	public String postShowInspections(@RequestParam(value="techDate", required = false) String techDate, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String Email = auth.getName();
		User driver = userRepository.findByEmail(Email);
		long driverId = driver.getId();
		content = "";

		
		
		String inspectionFileName ="./src/main/resources/static/inspections/" + driverId+"/"+passthroughCarLic+"/"+techDate+passthroughCarLic+".txt";
		
		Path filePath = Paths.get(inspectionFileName);
		System.out.println(inspectionFileName);
		try
		{
			content = Files.readString(filePath);
			System.out.println(content);
			
			
		}
		catch(IOException e)
		{
			
			System.out.println("WE ARE BROKEN");
			
		}
		
		
		return "redirect:/inspectionFinal";
	}
	
	
	@GetMapping("/inspectionFinal")
	public String inspecitonFinal(Model model) {
		
		
		model.addAttribute("inspectString", content);
		
		return"inspectionFinal";
	}
	
	@PreAuthorize("hasAnyRole('TECH','ADMIN')")
	@GetMapping("/changeRole/{id}")
	public String changeRole(@PathVariable("id") long id, Model model) {
		String roleName = "";
		user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		model.addAttribute("user", user);
		throughputId = id;
		model.addAttribute("roleName", roleName);
		roleName.toUpperCase();
		Role role = roleRepository.findById((id)).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		return "changeRole";
	}
	@PostMapping("/changeRole")
	public String postChangeRole(@RequestParam(value="roleName",required =false)String roleName, Model model) {
		User user = userRepository.findById(throughputId).orElseThrow(() -> new IllegalArgumentException("Invalid at findbyid"));
		model.addAttribute("uName", user.getFirstName());
		Role role = roleRepository.findById((throughputId)).orElseThrow(() -> new IllegalArgumentException("Invalid at findbyid"));
		System.out.println(throughputId);
		role.setName("ROLE_" + roleName.toUpperCase());
		roleRepository.save(role);
		
		return "redirect:/AdminHomepage";
	}
}
