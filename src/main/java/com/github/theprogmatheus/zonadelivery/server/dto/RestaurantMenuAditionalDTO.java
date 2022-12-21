package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuAditionalEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantMenuAditionalDTO {

	private UUID id;
	private Set<RestaurantMenuItemDTO> items;
	private Object optionals;
	private String name;
	private String description;
	private double price;
	private int minAmount;
	private int maxAmount;
	private boolean paused;

	public RestaurantMenuAditionalDTO(RestaurantMenuAditionalEntity restaurantMenuAditionalEntity) {
		if (restaurantMenuAditionalEntity != null) {

			if (restaurantMenuAditionalEntity.getId() != null)
				this.id = restaurantMenuAditionalEntity.getId();

			if (restaurantMenuAditionalEntity.getItems() != null)
				this.items = restaurantMenuAditionalEntity.getItems().stream().map(item -> {
					item.setAditionals(null);
					return new RestaurantMenuItemDTO(item);
				}).collect(Collectors.toSet());

			if (restaurantMenuAditionalEntity.getName() != null)
				this.name = restaurantMenuAditionalEntity.getName();
			
			this.description = restaurantMenuAditionalEntity.getDescription();
			this.price = restaurantMenuAditionalEntity.getPrice();
			this.minAmount = restaurantMenuAditionalEntity.getMinAmount();
			this.maxAmount = restaurantMenuAditionalEntity.getMaxAmount();
			this.paused = restaurantMenuAditionalEntity.isPaused();
		}
	}
}
