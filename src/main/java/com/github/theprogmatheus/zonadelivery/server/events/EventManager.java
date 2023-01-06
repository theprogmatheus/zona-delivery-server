package com.github.theprogmatheus.zonadelivery.server.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EventManager {

	private static final List<Event> events = new ArrayList<>();
	public static final long EVENT_LIFE_TIME = 1_800_000; // 30 minutes

	public static Event emit(UUID restaurantId, EventType type, Object data) {
		if (restaurantId != null && type != null) {
			Event event = new Event(UUID.randomUUID(), restaurantId, type, new Date(), data);

			events.add(event);

			return event;
		}
		return null;
	}

	public static boolean consume(Event event) {
		return (event != null && events.removeIf(targetEvent -> event.getId().equals(targetEvent.getId())));
	}

	public static boolean consumeIf(Predicate<? super Event> filter) {
		return events.removeIf(filter);
	}

	public static boolean cleanUp() {
		return events
				.removeIf(event -> System.currentTimeMillis() >= (event.getCreatedAt().getTime() + EVENT_LIFE_TIME));
	}

	public static List<Event> polling(UUID restaurantId) {
		return events.stream().filter(event -> event.getRestaurant().equals(restaurantId)).collect(Collectors.toList());
	}
}
