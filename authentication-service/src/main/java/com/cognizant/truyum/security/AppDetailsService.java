package com.cognizant.truyum.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cognizant.truyum.exception.UserAlreadyExistsException;
import com.cognizant.truyum.model.Role;
import com.cognizant.truyum.model.User;
import com.cognizant.truyum.repositories.RoleRepository;
import com.cognizant.truyum.repositories.UserRepository;

@Service
public class AppDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	
	public AppDetailsService() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppDetailsService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	public AppDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		AppUser appUser = null;
		if(user == null)
			throw new UsernameNotFoundException("Username not found");
		else {
			appUser = new AppUser(user);
		}
		
		return appUser;
	}
	
	public void signUp(User newUser) throws UserAlreadyExistsException {
		User user = new User();
		user = userRepository.findByUsername(newUser.getUsername());
		if(user == null) {
			Role role = roleRepository.findById(2).get();
			Set<Role> roleList = new HashSet<>();
			roleList.add(role);
			
			newUser.setRoleList(roleList);
			newUser.setPassword(passwordEncoder().encode(newUser.getPassword()));
			userRepository.save(newUser);
		}
		else {
			System.out.println("User already exists");
			throw new UserAlreadyExistsException();
		}
	}

	private PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}

	public String checkUsername(String username) {
		User user = null;
		user = userRepository.findByUsername(username);
		if(user == null)
			return null;
		else
			return "{\"userNameTaken\":true}";
	}

}
