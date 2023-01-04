package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantMenuDTO {

	private UUID id;
	private UUID restaurant;
	private String name;
	private Set<RestaurantMenuCategoryDTO> categories;
	private Set<RestaurantMenuItemDTO> items;
	private Set<RestaurantMenuAditionalDTO> aditionals;
	private Set<RestaurantMenuOptionalDTO> optionals;

	public RestaurantMenuDTO(RestaurantMenuEntity restaurantMenuEntity) {

		if (restaurantMenuEntity.getId() != null)
			this.id = restaurantMenuEntity.getId();

		if (restaurantMenuEntity.getRestaurant() != null)
			this.restaurant = restaurantMenuEntity.getRestaurant().getId();

		if (restaurantMenuEntity.getName() != null)
			this.name = restaurantMenuEntity.getName();

		if (restaurantMenuEntity.getCategories() != null)
			this.categories = restaurantMenuEntity.getCategories().stream().map(category -> {
				category.setMenu(null);
				return new RestaurantMenuCategoryDTO(category);
			}).collect(Collectors.toSet());

		if (restaurantMenuEntity.getItems() != null)
			this.items = restaurantMenuEntity.getItems().stream().map(item -> {
				item.setMenu(null);
				return new RestaurantMenuItemDTO(item);
			}).collect(Collectors.toSet());

		if (restaurantMenuEntity.getAditionals() != null)
			this.aditionals = restaurantMenuEntity.getAditionals().stream().map(aditional -> {
				aditional.setMenu(null);
				return new RestaurantMenuAditionalDTO(aditional);
			}).collect(Collectors.toSet());

		if (restaurantMenuEntity.getOptionals() != null)
			this.optionals = restaurantMenuEntity.getOptionals().stream().map(optional -> {
				optional.setMenu(null);
				return new RestaurantMenuOptionalDTO(optional);
			}).collect(Collectors.toSet());

	}

}
