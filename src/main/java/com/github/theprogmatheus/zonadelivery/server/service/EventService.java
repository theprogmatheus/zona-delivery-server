package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.events.Event;
import com.github.theprogmatheus.zonadelivery.server.events.EventManager;
import com.github.theprogmatheus.zonadelivery.server.events.EventType;

@Service
public class EventService {

	public Event createNewEvent(RestaurantOrderEntity order, EventType type, Object data) {
		return EventManager.emit(order.getRestaurant().getId(), type, data);
	}

	public Object acknowledgment(UUID restaurantId, Collection<UUID> eventIds) {
		if (restaurantId == null)
			return "restaurantId is null";

		if (eventIds == null)
			return "events is null";

		EventManager.consumeIf(event -> eventIds.contains(event.getId()) && event.getRestaurant().equals(restaurantId));

		return polling(restaurantId);
	}

	public Object polling(UUID restaurantId) {
		if (restaurantId == null)
			return "restaurantId is null";
		return EventManager.polling(restaurantId);
	}

}
