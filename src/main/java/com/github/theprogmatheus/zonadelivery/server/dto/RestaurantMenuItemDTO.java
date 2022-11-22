package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuItemEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantMenuItemDTO {

	private UUID id;
	private RestaurantMenuCategoryDTO category;
	private String name;
	private double price;
	private Set<RestaurantMenuAditionalDTO> aditionals;

	public RestaurantMenuItemDTO(RestaurantMenuItemEntity restaurantMenuItemEntity) {
		if (restaurantMenuItemEntity != null) {

			if (restaurantMenuItemEntity.getId() != null)
				this.id = restaurantMenuItemEntity.getId();

			if (restaurantMenuItemEntity.getCategory() != null)
				this.category = new RestaurantMenuCategoryDTO(restaurantMenuItemEntity.getCategory());

			if (restaurantMenuItemEntity.getName() != null)
				this.name = restaurantMenuItemEntity.getName();

			this.price = restaurantMenuItemEntity.getPrice();

			if (restaurantMenuItemEntity.getAditionals() != null)
				this.aditionals = restaurantMenuItemEntity.getAditionals().stream().map(aditional -> {
					aditional.setItem(null);
					return new RestaurantMenuAditionalDTO(aditional);
				}).collect(Collectors.toSet());

		}

	}

}
