package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.events.Event;
import com.github.theprogmatheus.zonadelivery.server.events.EventType;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class EventDTO {

	private UUID id;
	private UUID restaurant;
	private EventType type;
	private Date createdAt;
	private Object data;

	public EventDTO(Event event) {
		if (event != null) {
			this.id = event.getId();
			this.restaurant = event.getRestaurant();
			this.type = event.getType();
			this.createdAt = event.getCreatedAt();
			if (event.getData() != null) {
				
				if (event.getData() instanceof RestaurantOrderEntity)
					this.data = new RestaurantOrderDTO((RestaurantOrderEntity) event.getData());

			}
		}
	}
}
