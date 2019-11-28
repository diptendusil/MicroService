package com.cognizant.truyum.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.truyum.TruyumConstants;
import com.cognizant.truyum.model.User;
import com.cognizant.truyum.service.UserService;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class AuthenticationController {
	
	@Autowired
	private UserService userService;
	
	
	public static Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
	
	@GetMapping("/authenticate")
	public Map<String, String> authenticate(@RequestHeader("Authorization") String authHeader) {
		LOGGER.debug("Start");
		String user = getUser(authHeader);
		TruyumConstants.LOGGER.debug(user);
		String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString();
		
		Map<String, String> auth = new HashMap<>();
		auth.put("token", generateJwt(user, role));
		auth.put("role", role);
		User u = userService.getUserByUsername(user);
		auth.put("firstName", u.getFirstName());
		auth.put("lastName", u.getLastName());
		LOGGER.debug("End");
		return auth;
	}
	
	private String getUser(String authHeader) {
		byte[] auth = Base64.getDecoder().decode(authHeader.split(" ")[1]);
		String authStr = new String(auth);
		return authStr.split(":")[0];
	}
	
	private String generateJwt(String user, String role) {
		JwtBuilder builder = Jwts.builder();
		builder.setSubject(user + ":" + role);
		
		builder.setIssuedAt(new Date());
		
		builder.setExpiration(new Date((new Date()).getTime() + 1200000));
		
		builder.signWith(SignatureAlgorithm.HS256, "secretkey");
		
		String token = builder.compact();
		
		return token;
	}
	
	
	@GetMapping("/logout")
	public String logout() throws ServletException, IOException {
		return "Log out successful";
	}
}
