package com.github.theprogmatheus.zonadelivery.server.service;

import static com.github.theprogmatheus.zonadelivery.server.config.security.SecurityConfiguration.EXPIRATION_TIME;
import static com.github.theprogmatheus.zonadelivery.server.config.security.SecurityConfiguration.TOKEN_TYPE;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.config.security.SecurityConfiguration;
import com.github.theprogmatheus.zonadelivery.server.dto.AuthenticationResponseDTO;
import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantEmployeeUserDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEmployeeUserEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.UserType;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantEmployeeUserRepository;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantRepository;

import lombok.Getter;

@Service
@Getter
public class RestaurantEmployeeService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private RestaurantEmployeeUserRepository employeeUserRepository;

	public Object register(UUID restaurantId, String username, String displayName, String password) {

		if (restaurantId == null)
			return "The restaurantId can't be null";

		if (username == null || username.isBlank())
			return "The username can't be null or blank";

		if (password == null || password.isBlank())
			return "The password can't be null or blank";

		RestaurantEntity restaurant = this.restaurantRepository.findById(restaurantId).orElse(null);
		if (restaurant == null)
			return "The restaurant was not found";

		if (restaurant.getEmployees().stream().anyMatch(employee -> employee.getUsername().equalsIgnoreCase(username)))
			return "There is already an employee registered with that username in this restaurant";

		RestaurantEmployeeUserEntity employee = new RestaurantEmployeeUserEntity(null, restaurant,
				username.toLowerCase(), displayName, UserService.PASSWORD_ENCODER.encode(password));

		return this.employeeUserRepository.saveAndFlush(employee);
	}

	public Object login(UUID restaurantId, String username, String password) {

		if (restaurantId == null)
			return "The restaurantId can't be null";

		if (username == null || username.isBlank())
			return "The username can't be null or blank";

		RestaurantEntity restaurant = this.restaurantRepository.findById(restaurantId).orElse(null);
		if (restaurant == null)
			return "The restaurant was not found";

		RestaurantEmployeeUserEntity employee = this.employeeUserRepository.findByUsername(username).orElse(null);

		if (employee == null || password == null || password.isBlank()
				|| !checkPassword(password, employee.getPassword()))
			return "Incorrect credentials, check and try again";

		String token = SecurityConfiguration.createJWTToken(UserType.EMPLOYEE, employee.getId().toString(),
				EXPIRATION_TIME);

		AuthenticationResponseDTO authenticationResponse = new AuthenticationResponseDTO(TOKEN_TYPE, token,
				EXPIRATION_TIME / 1_000, new RestaurantEmployeeUserDTO(employee));
		return authenticationResponse;
	}

	public void update(UUID restaurantId, UUID employeeId, String username, String displayName, String password) {

	}

	public void delete(UUID restaurantId, UUID employeeId) {

	}

	public boolean checkPassword(String rawPassword, String encodedPassword) {
		return UserService.PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
	}

}
