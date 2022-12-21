package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuOptionalEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantMenuOptionalDTO {

	private UUID id;
	private Set<RestaurantMenuItemDTO> items;
	private String name;
	private String description;
	private int min, max;
	private Set<RestaurantMenuAditionalDTO> options;

	public RestaurantMenuOptionalDTO(RestaurantMenuOptionalEntity restaurantMenuOptionalEntity) {
		if (restaurantMenuOptionalEntity != null) {
			this.id = restaurantMenuOptionalEntity.getId();

			if (restaurantMenuOptionalEntity.getItems() != null)
				this.items = restaurantMenuOptionalEntity.getItems().stream()
						.map(item -> new RestaurantMenuItemDTO(item)).collect(Collectors.toSet());

			this.name = restaurantMenuOptionalEntity.getName();
			this.description = restaurantMenuOptionalEntity.getDescription();
			this.min = restaurantMenuOptionalEntity.getMin();
			this.max = restaurantMenuOptionalEntity.getMax();
			
			if (restaurantMenuOptionalEntity.getOptions() != null)
				this.options = restaurantMenuOptionalEntity.getOptions().stream()
						.map(option -> new RestaurantMenuAditionalDTO(option)).collect(Collectors.toSet());
		}
	}

}
