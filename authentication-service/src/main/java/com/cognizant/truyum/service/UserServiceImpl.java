package com.cognizant.truyum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.truyum.model.User;
import com.cognizant.truyum.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	/*
	@Autowired
	private UserDao userDao;
	
	@Override
	public void signUp(User user) throws UserAlreadyExistsException {
		userDao.signUp(user);
	}

	@Override
	public String checkUsername(String username) {
		return userDao.checkUsername(username);
	}
	*/
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
