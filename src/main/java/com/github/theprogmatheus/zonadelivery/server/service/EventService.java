package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.EventEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.OrderStatus;
import com.github.theprogmatheus.zonadelivery.server.repository.EventRepository;

@Service
public class EventService {

	public static final long EVENT_LIFE_TIME = 43_200_000; // 12 horas

	@Autowired
	private EventRepository eventRepository;

	public Object createNewEvent(RestaurantOrderEntity order, OrderStatus type, Map<String, Object> metadata) {

		if (order == null)
			return "order is null";

		if (type == null)
			return "type is null";

		return this.eventRepository.saveAndFlush(new EventEntity(null, order, type, new Date(), metadata));
	}

	public Object acknowledgment(UserEntity userEntity, Collection<UUID> eventIds) {

		if (userEntity == null)
			return "User is null";

		if (eventIds == null)
			return "events is null";

		for (UUID eventId : eventIds) {

			EventEntity event = this.eventRepository.findById(eventId).orElse(null);

			if (event != null && userEntity.getId().equals(event.getOrder().getRestaurant().getOwner().getId()))
				this.eventRepository.delete(event);

		}

		this.eventRepository.flush();

		return polling(userEntity);
	}

	public Object polling(UserEntity userEntity) {

		if (userEntity == null)
			return "user is null";

		return this.eventRepository.findAll().stream()
				.filter(event -> userEntity.getId().equals(event.getOrder().getRestaurant().getOwner().getId()))
				.collect(Collectors.toList());
	}

	public EventRepository getEventRepository() {
		return eventRepository;
	}

	/*
	 * A cada 30 minutos o sistema remove os eventos gerados a 12 horas atrás que
	 * não foram devidamente processados.
	 */
	@Scheduled(fixedRate = 1_800_000)
	public void purgeOldEvents() {

		for (EventEntity event : this.eventRepository.findAll().stream()
				.filter(event -> System.currentTimeMillis() >= (event.getCreatedAt().getTime() + EVENT_LIFE_TIME))
				.collect(Collectors.toList()))
			this.eventRepository.delete(event);

		this.eventRepository.flush();
	}

}
