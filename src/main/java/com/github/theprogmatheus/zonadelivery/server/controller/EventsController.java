package com.github.theprogmatheus.zonadelivery.server.controller;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.dto.EventDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.EventEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.service.EventService;

@SuppressWarnings({ "unchecked" })
@RestController
@RequestMapping("/event")
public class EventsController {

	@Autowired
	private EventService service;

	@GetMapping("/polling")
	public Object polling() {
		try {

			Object result = this.service
					.polling((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

			if (result instanceof Collection)
				result = ((Collection<EventEntity>) result).stream().map(event -> new EventDTO(event))
						.collect(Collectors.toList());

			return ResponseEntity.ok(result);

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}

	}

	@PostMapping("/acknowledgment")
	public Object acknowledgment(@RequestBody Collection<EventDTO> events) {
		try {

			Object result = this.service.acknowledgment(
					(UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
					events.stream().map(event -> event.getId()).collect(Collectors.toList()));

			if (result instanceof Collection)
				result = ((Collection<EventEntity>) result).stream().map(event -> new EventDTO(event))
						.collect(Collectors.toList());

			return ResponseEntity.ok(result);

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}

	}

}
