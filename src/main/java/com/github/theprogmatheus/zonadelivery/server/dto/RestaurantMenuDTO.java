package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.RestaurantMenuEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantMenuDTO {

	private UUID id;
	private RestaurantDTO restaurant;
	private String name;
	private Set<RestaurantMenuCategoryDTO> categories;

	public RestaurantMenuDTO(RestaurantMenuEntity restaurantMenuEntity) {

		if (restaurantMenuEntity.getId() != null)
			this.id = restaurantMenuEntity.getId();

		if (restaurantMenuEntity.getRestaurant() != null)
			this.restaurant = new RestaurantDTO(restaurantMenuEntity.getRestaurant());

		if (restaurantMenuEntity.getName() != null)
			this.name = restaurantMenuEntity.getName();

		if (restaurantMenuEntity.getCategories() != null)
			this.categories = restaurantMenuEntity.getCategories().stream().map(category -> {
				category.setMenu(null);
				return new RestaurantMenuCategoryDTO(category);
			}).collect(Collectors.toSet());

	}

}
