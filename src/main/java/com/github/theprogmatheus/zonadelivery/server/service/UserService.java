package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.repository.UserRepository;

@Service
public class UserService {

	public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Autowired
	private UserRepository repository;

	public UserEntity createDefaultUserIfNotExists() {

		if (this.repository.count() <= 0)
			return this.repository.saveAndFlush(new UserEntity(null, "admin", "admin@mail.com",
					PASSWORD_ENCODER.encode("admin"), Arrays.asList(), null));

		return null;
	}

	public UserEntity getUserByUsername(String username) {
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
