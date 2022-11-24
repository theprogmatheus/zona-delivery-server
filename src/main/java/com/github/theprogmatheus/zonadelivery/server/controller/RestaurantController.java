package com.github.theprogmatheus.zonadelivery.server.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.service.RestaurantService;
import com.github.theprogmatheus.zonadelivery.server.service.UserService;
import com.github.theprogmatheus.zonadelivery.server.util.StringUtils;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public Object createNewRestaurant(@RequestBody Map<String, String> body) {

		UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userEntity == null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be logged in to do that");

		Object result = this.restaurantService.createNewRestaurant(userEntity, body.get("name"));
		if (result instanceof RestaurantEntity)
			return ResponseEntity.status(HttpStatus.CREATED).body(new RestaurantDTO((RestaurantEntity) result));

		return ResponseEntity.ok(result);
	}

	@PostMapping("/{restaurantId}/change_name_id")
	public Object changeNameId(@PathVariable UUID restaurantId, @RequestBody Map<String, String> body) {
		try {

			Object result = this.restaurantService
					.changeRestaurantNameId(this.restaurantService.getRestaurantById(restaurantId), body.get("nameId"));

			if (result instanceof RestaurantEntity)
				return ResponseEntity.status(HttpStatus.CREATED).body(new RestaurantDTO((RestaurantEntity) result));

			return ResponseEntity.ok(result);

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@PostMapping("/{restaurantId}/change_display_name")
	public Object changeDisplayName(@PathVariable UUID restaurantId, @RequestBody Map<String, String> body) {
		try {

			Object result = this.restaurantService.changeRestaurantDisplayName(
					this.restaurantService.getRestaurantById(restaurantId), body.get("displayName"));

			if (result instanceof RestaurantEntity)
				return ResponseEntity.status(HttpStatus.CREATED).body(new RestaurantDTO((RestaurantEntity) result));

			return ResponseEntity.ok(result);

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/{restaurantId}/change_owner")
	public Object changeOwner(@PathVariable UUID restaurantId, @RequestBody Map<String, String> body) {
		try {

			Object result = this.restaurantService.changeRestaurantOwner(
					this.restaurantService.getRestaurantById(restaurantId),
					this.userService.getUserById(StringUtils.getUUIDFromString(body.get("ownerId"))));

			if (result instanceof RestaurantEntity)
				return ResponseEntity.status(HttpStatus.CREATED).body(new RestaurantDTO((RestaurantEntity) result));

			return ResponseEntity.ok(result);

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}
}
