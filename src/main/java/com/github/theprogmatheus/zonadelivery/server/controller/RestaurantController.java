package com.github.theprogmatheus.zonadelivery.server.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.UserRole;
import com.github.theprogmatheus.zonadelivery.server.service.RestaurantService;
import com.github.theprogmatheus.zonadelivery.server.service.UserService;
import com.github.theprogmatheus.zonadelivery.server.util.StringUtils;

@Secured(UserRole.USER_ROLE_NAME)
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private UserService userService;

	@SuppressWarnings("unchecked")
	@GetMapping
	public Object listRestaurants() {

		UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userEntity == null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be logged in to do that");

		Object result = this.restaurantService.listRestaurantsByOwner(userEntity);

		if (result instanceof List)
			return ResponseEntity.ok(((List<RestaurantEntity>) result).stream()
					.map(restaurant -> new RestaurantDTO(restaurant)).collect(Collectors.toList()));

		return ResponseEntity.ok(result);
	}

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

	@GetMapping("/{restaurantId}/ifood/merchants")
	public Object merchants(@PathVariable UUID restaurantId) {
		try {

			Object result = this.restaurantService
					.getRestaurantIFoodMerchants(this.restaurantService.getRestaurantById(restaurantId));

			if (result instanceof Set)
				return ResponseEntity.status(HttpStatus.OK).body(result);

			return ResponseEntity.ok(result);

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@GetMapping("/{restaurantId}/ifood/merchants/{merchantId}/status")
	public Object merchantStatus(@PathVariable UUID restaurantId, @PathVariable String merchantId) {
		try {

			Object result = this.restaurantService.getRestaurantIFoodMerchantStatus(restaurantId, merchantId);

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/{restaurantId}/ifood/add_merchant")
	public Object addMerchant(@PathVariable UUID restaurantId, @RequestBody Map<String, String> body) {
		try {

			Object result = this.restaurantService.addRestaurantIFoodMerchant(
					this.restaurantService.getRestaurantById(restaurantId), body.get("merchantName"),
					body.get("merchantId"));

			if (result instanceof RestaurantEntity)
				return ResponseEntity.status(HttpStatus.CREATED).body(new RestaurantDTO((RestaurantEntity) result));

			return ResponseEntity.ok(result);

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@DeleteMapping("/{restaurantId}/ifood/delete_merchant")
	public Object deleteMerchant(@PathVariable UUID restaurantId, @RequestBody Map<String, String> body) {
		try {

			Object result = this.restaurantService.deleteRestaurantIFoodMerchant(
					this.restaurantService.getRestaurantById(restaurantId), body.get("merchantId"));

			if (result instanceof RestaurantEntity)
				return ResponseEntity.status(HttpStatus.CREATED).body(new RestaurantDTO((RestaurantEntity) result));

			return ResponseEntity.ok(result);

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

}
