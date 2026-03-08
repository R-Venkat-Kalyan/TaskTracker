package com.tracker.tasktracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	@GetMapping("/")
	public String home() {
		return "/index";
	}
	
	@GetMapping("/sign-in")
	public String signIn() {
		return "/login-form";
	}

	@PostMapping("/user")
	public String dashBoard(){
		return "/employee-pages/dashboard";
	}







}
