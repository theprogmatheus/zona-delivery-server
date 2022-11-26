package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.EventEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.OrderStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class EventDTO {

	private UUID id;
	private UUID restaurant;
	private UUID order;
	private OrderStatus status;
	private Date createdAt;
	private Map<String, Object> metadata;

	public EventDTO(EventEntity eventEntity) {
		if (eventEntity != null) {
			if (eventEntity.getId() != null)
				this.id = eventEntity.getId();

			if (eventEntity.getOrder() != null) {
				this.order = eventEntity.getOrder().getId();
				this.restaurant = eventEntity.getOrder().getRestaurant().getId();
			}

			if (eventEntity.getStatus() != null)
				this.status = eventEntity.getStatus();

			if (eventEntity.getCreatedAt() != null)
				this.createdAt = eventEntity.getCreatedAt();

			if (eventEntity.getMetadata() != null)
				this.metadata = eventEntity.getMetadata();
		}
	}

}
