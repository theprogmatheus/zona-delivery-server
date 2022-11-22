package com.github.theprogmatheus.zonadelivery.server.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.service.RestaurantService;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;

	
	@PostMapping("/register")
	public Object createNewRestaurant(@RequestBody Map<String, String> body) {

		UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userEntity == null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be logged in to do that");

		String name = body.get("name");

		Object result = this.restaurantService.createNewRestaurant(userEntity, name);
		if (result instanceof RestaurantEntity)
			return ResponseEntity.status(HttpStatus.CREATED).body(new RestaurantDTO((RestaurantEntity) result));

		return ResponseEntity.ok(result);
	}
}
