package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.config.security.CustomPasswordEncoder;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.repository.UserRepository;
import com.github.theprogmatheus.zonadelivery.server.util.Utils;

@Service
public class UserService {

	public static final PasswordEncoder PASSWORD_ENCODER = CustomPasswordEncoder.getInstance();

	@Autowired
	private UserRepository repository;

	public Object createDefaultUserIfNotExists() {
		if (this.repository.count() <= 0)
			return createNewUserAccount("admin", "admin", "admin@mail.com");

		return null;
	}

	public Object createNewUserAccount(String username, String password, String email) {

		if (username != null && password != null) {

			if (!username.matches(Utils.REGEX_USERNAME))
				return "The username is invalid"; // user name invalid

			if (email != null && !email.matches(Utils.REGEX_EMAIL))
				return "The email is invalid"; // email invalid

			if (getUserByUsername(username) != null)
				return "The username already exists"; // user exists

			return this.repository.saveAndFlush(
					new UserEntity(null, username, email, PASSWORD_ENCODER.encode(password), Arrays.asList(), null));
		}

		return "The 'username' and 'password' are required fields in your request body, do not leave them null";
	}

	public UserEntity getUserByUsername(String username) {
		if (username != null)
			username = username.toLowerCase();
		return this.repository.findByUsername(username);
	}

	public UserEntity getUserByEmail(String email) {
		return this.repository.findByEmail(email);
	}

	public UserEntity getUserById(UUID userId) {
		return this.repository.findById(userId).orElse(null);
	}

	public boolean checkPassword(String rawPassword, String encodedPassword) {
		return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
	}

	public UserRepository getRepository() {
		return repository;
	}
}
