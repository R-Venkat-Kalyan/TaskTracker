package com.tracker.tasktracker.controller;

import com.tracker.tasktracker.entity.UserEntity;
import com.tracker.tasktracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/")
	public String home() {
		return "/index";
	}
	
	@GetMapping("/sign-in")
	public String signIn() {
		return "/login-form";
	}


	@PostMapping("/user")
	public String userPanel(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model,
							HttpSession session) {
		List<UserEntity> allUsers = userService.findAllUsers();
		boolean userFound = false;
		for (UserEntity user : allUsers) {
			String employeeID = request.getParameter("empId");
			String password = request.getParameter("password");
			if (user.getEmpId().equals(employeeID) && user.getPassword().equals(password)) {
				session.setAttribute("user", user);
				session.setMaxInactiveInterval(30 * 60);
				userFound = true;
				if(user.getRole().equals("employee"))
					return "redirect:/employee/dashboard";
				else
					return "redirect:/manager/dashboard";
			} else if (user.getEmpId().equals(employeeID) && !user.getPassword().equals(password)) {
				redirectAttributes.addFlashAttribute("successMessage", "Invalid Password..❌❌");
				return "redirect:/sign-in";
			}
		}

		if (!userFound) {
			redirectAttributes.addFlashAttribute("successMessage", "User Not Found..❌\nContact your manager and get credentials created for you !!");
			return "redirect:/sign-in";
		}
		redirectAttributes.addFlashAttribute("successMessage", "Invalid Credentials..❌❌");
		return "redirect:/sign-in";
	}

	@PostMapping("/sign-out")
	public String signOut(HttpSession session){
		session.invalidate();
		return "redirect:/";
	}


}
