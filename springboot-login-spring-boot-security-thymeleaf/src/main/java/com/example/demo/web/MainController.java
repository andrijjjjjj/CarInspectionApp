package com.example.demo.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	
	@GetMapping("/")
	public String home() {
		return"index";
	}
	
	// Waiver Method START ( not sure how either of these methods work just changed them to load the page)
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
		System.out.println(waiverVerify);
		
	}

}
