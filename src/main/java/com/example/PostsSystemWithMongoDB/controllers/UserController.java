package com.example.PostsSystemWithMongoDB.controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.PostsSystemWithMongoDB.dto.UserDTO;
import com.example.PostsSystemWithMongoDB.services.UserService;
import com.example.PostsSystemWithMongoDB.services.exceptions.RepeatedEmailException;

@Controller
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
	
	@GetMapping("/sign-up")
	public String signUpPage() {
		return "signup";
	}
	
	@PostMapping("/sign-up")
	public String createAccount(@Valid UserDTO userDto, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "signup";
		}
		try {
			userService.insertUser(userDto.dtoToUser(userDto));
			return "redirect:/login";
		} catch (RepeatedEmailException error) {
			model.addAttribute("exception", error.getLocalizedMessage());
			return "signup";
		}
	}
}
