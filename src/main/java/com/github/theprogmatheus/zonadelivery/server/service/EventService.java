package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.EventEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.EventType;
import com.github.theprogmatheus.zonadelivery.server.repository.EventRepository;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;

	public Object createNewEvent(RestaurantEntity restaurant, EventType type, Map<String, Object> metadata) {

		if (restaurant == null)
			return "restaurant is null";

		if (type == null)
			return "type is null";

		return this.eventRepository.saveAndFlush(new EventEntity(null, restaurant, type, new Date(), metadata));
	}

	public Object acknowledgment(UserEntity userEntity, Collection<UUID> eventIds) {
		
		if (userEntity == null)
			return "User is null";
		
		if (eventIds == null)
			return "events is null";

		for (UUID eventId : eventIds) {
			
			EventEntity event = this.eventRepository.findById(eventId).orElse(null);
			
			if (event != null && userEntity.getId().equals(event.getRestaurant().getOwner().getId()))
				this.eventRepository.delete(event);
			
		}

		this.eventRepository.flush();

		return polling(userEntity);
	}

	public Object polling(UserEntity userEntity) {

		if (userEntity == null)
			return "user is null";

		return this.eventRepository.findAll().stream()
				.filter(event -> userEntity.getId().equals(event.getRestaurant().getOwner().getId()))
				.collect(Collectors.toList());
	}

	public EventRepository getEventRepository() {
		return eventRepository;
	}

}
