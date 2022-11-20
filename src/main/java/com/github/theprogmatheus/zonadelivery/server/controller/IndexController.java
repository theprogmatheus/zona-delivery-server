package com.github.theprogmatheus.zonadelivery.server.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantDTO;
import com.github.theprogmatheus.zonadelivery.server.dto.UserDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantRepository;

@RestController
@RequestMapping("/")
public class IndexController {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@GetMapping("/")
	public Object index() {
		return new UserDTO((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}

	@GetMapping("/restaurants")
	public Object restaurants() {
		return restaurantRepository.findAll().stream().map(restaurant -> new RestaurantDTO(restaurant))
				.collect(Collectors.toList());
	}

}
