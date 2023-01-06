package com.github.theprogmatheus.zonadelivery.server.controller;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.dto.EventDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.events.Event;
import com.github.theprogmatheus.zonadelivery.server.service.EventService;

@RestController
@RequestMapping("/event/{restaurantId}")
public class EventsController {

	@Autowired
	private EventService service;

	@GetMapping("/polling")
	public Object polling(@PathVariable UUID restaurantId) {
		try {

			UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (user != null && user.getRestaurants() != null
					&& user.getRestaurants().stream().anyMatch(restaurant -> restaurant.getId().equals(restaurantId))) {
				Object result = this.service.polling(restaurantId);
				if (result != null && result instanceof Collection)
					return ResponseEntity.ok(((Collection<Event>) result).stream().map(event -> new EventDTO(event))
							.collect(Collectors.toList()));
			}

			return ResponseEntity.ok();
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@PostMapping("/acknowledgment")
	public Object acknowledgment(@PathVariable UUID restaurantId, @RequestBody Collection<Event> events) {
		try {
			UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (user != null && user.getRestaurants() != null
					&& user.getRestaurants().stream().anyMatch(restaurant -> restaurant.getId().equals(restaurantId))) {

				Object result = this.service.acknowledgment(restaurantId,
						events.stream().map(event -> event.getId()).collect(Collectors.toList()));

				if (result instanceof Collection)
					return result;
			}

			return ResponseEntity.ok();
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}

	}

}
