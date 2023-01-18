package com.github.theprogmatheus.zonadelivery.server.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantEmployeeUserDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEmployeeUserEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.UserRole;
import com.github.theprogmatheus.zonadelivery.server.service.RestaurantEmployeeService;

@RestController
@RequestMapping("/restaurant/{restaurantId}/employee")
public class RestaurantEmployeeController {

	@Autowired
	private RestaurantEmployeeService restaurantEmployeeService;

	// /login
	// /register

	@PostMapping("/login")
	public Object login(@PathVariable UUID restaurantId, @RequestBody RestaurantEmployeeUserEntity user) {
		return this.restaurantEmployeeService.login(restaurantId, user.getUsername(), user.getPassword());
	}

	@Secured(UserRole.USER_ROLE_NAME)
	@PostMapping("/register")
	public Object register(@PathVariable UUID restaurantId, @RequestBody RestaurantEmployeeUserEntity user) {
		Object result = this.restaurantEmployeeService.register(restaurantId, user.getUsername(), user.getDisplayName(),
				user.getPassword());
		if (result instanceof RestaurantEmployeeUserEntity)
			return new RestaurantEmployeeUserDTO((RestaurantEmployeeUserEntity) result);
		return result;
	}

}
