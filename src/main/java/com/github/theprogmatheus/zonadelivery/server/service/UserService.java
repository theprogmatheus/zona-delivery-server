package com.github.theprogmatheus.zonadelivery.server.service;

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
	private UserRepository userRepository;

	public UserEntity createDefaultUserIfNotExists() {

		if (this.userRepository.count() <= 0)
			return this.userRepository.saveAndFlush(
					new UserEntity(0, UUID.randomUUID(), "admin", "admin@mail.com", PASSWORD_ENCODER.encode("admin")));

		return null;
	}

	public UserEntity getUserByUsername(String username) {
		return this.userRepository.findByUsername(username);
	}

	public UserEntity getUserByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}

	public UserEntity getUserByUserId(UUID userId) {
		return this.userRepository.findByUserId(userId);
	}

	public boolean checkPassword(String rawPassword, String encodedPassword) {
		return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}
}
