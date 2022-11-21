package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.RestaurantMenuCategoryEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantMenuCategoryDTO {

	private UUID id;
	private RestaurantMenuDTO menu;
	private String name;
	private Set<RestaurantMenuItemDTO> items;

	public RestaurantMenuCategoryDTO(RestaurantMenuCategoryEntity restaurantMenuCategoryEntity) {
		if (restaurantMenuCategoryEntity != null) {
			if (restaurantMenuCategoryEntity.getId() != null)
				this.id = restaurantMenuCategoryEntity.getId();

			if (restaurantMenuCategoryEntity.getMenu() != null)
				this.menu = new RestaurantMenuDTO(restaurantMenuCategoryEntity.getMenu());

			if (restaurantMenuCategoryEntity.getName() != null)
				this.name = restaurantMenuCategoryEntity.getName();

			if (restaurantMenuCategoryEntity.getItems() != null)
				this.items = restaurantMenuCategoryEntity.getItems().stream().map(item -> {
					item.setCategory(null);
					return new RestaurantMenuItemDTO(item);
				}).collect(Collectors.toSet());
		}
	}

}
