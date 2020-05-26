package com.geekhub.secretaryhelperapp.user.service;

import com.geekhub.secretaryhelperapp.user.model.User;
import com.geekhub.secretaryhelperapp.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(final UserRepository userRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public Optional<User> getUserByUsername(String username) {
		try {
			return userRepository.getUserByUsername(username);
		}
		catch (DataAccessException e) {
			logger.error(e.toString());
			return Optional.empty();
		}
	}

	public Optional<User> getUserByEmail(String email) {
		try {
			return userRepository.getUserByEmail(email);
		}
		catch (DataAccessException e) {
			logger.error(e.toString());
			return Optional.empty();
		}
	}

	@Transactional
	public void saveUser(User user) {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setEnabled(true);
			user.setRoles("USER");
			userRepository.save(user);
		}
		catch (DataAccessException e) {
			logger.error(e.toString());
		}
	}

}
