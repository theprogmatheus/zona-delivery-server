package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.EventEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.EventType;

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
	private Map<String, Object> metadata;

	public EventDTO(EventEntity eventEntity) {
		if (eventEntity != null) {
			if (eventEntity.getId() != null)
				this.id = eventEntity.getId();

			if (eventEntity.getRestaurant() != null)
				this.restaurant = eventEntity.getRestaurant().getId();

			if (eventEntity.getType() != null)
				this.type = eventEntity.getType();

			if (eventEntity.getCreatedAt() != null)
				this.createdAt = eventEntity.getCreatedAt();

			if (eventEntity.getMetadata() != null)
				this.metadata = eventEntity.getMetadata();
		}
	}

}
