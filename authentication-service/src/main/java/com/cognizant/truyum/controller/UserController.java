package com.cognizant.truyum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.truyum.exception.UserAlreadyExistsException;
import com.cognizant.truyum.model.User;
import com.cognizant.truyum.security.AppDetailsService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private AppDetailsService appDetailsService;
	
	@PostMapping
	public void signUp(@RequestBody @Valid User user) throws UserAlreadyExistsException {
		appDetailsService.signUp(user);
	}
	
	@GetMapping("/{username}")
	public String checkUser(@PathVariable String username) {
		String ch = appDetailsService.checkUsername(username);
		System.out.println(ch);
		return ch;
	}
}
