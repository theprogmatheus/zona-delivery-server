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
	private Set<RestaurantMenuCategoryDTO> categories;
	private String name;
	private String description;
	private String image;
	private double price;
	private double oldPrice;
	private boolean paused;
	private Set<RestaurantMenuOptionalDTO> optionals;
	private Set<RestaurantMenuAditionalDTO> aditionals;

	public RestaurantMenuItemDTO(RestaurantMenuItemEntity restaurantMenuItemEntity) {
		if (restaurantMenuItemEntity != null) {

			if (restaurantMenuItemEntity.getId() != null)
				this.id = restaurantMenuItemEntity.getId();

			if (restaurantMenuItemEntity.getCategories() != null)
				this.categories = restaurantMenuItemEntity.getCategories().stream().map(category -> {
					category.setItems(null);
					return new RestaurantMenuCategoryDTO(category);
				}).collect(Collectors.toSet());

			if (restaurantMenuItemEntity.getName() != null)
				this.name = restaurantMenuItemEntity.getName();

			this.description = restaurantMenuItemEntity.getDescription();
			this.image = restaurantMenuItemEntity.getImage();
			this.price = restaurantMenuItemEntity.getPrice();
			this.oldPrice = restaurantMenuItemEntity.getOldPrice();
			this.paused = restaurantMenuItemEntity.isPaused();

			
			if (restaurantMenuItemEntity.getOptionals() != null)
				this.optionals = restaurantMenuItemEntity.getOptionals().stream().map(optional -> {

					optional.setItems(null);

					return new RestaurantMenuOptionalDTO(optional);
				}).collect(Collectors.toSet());
				

			if (restaurantMenuItemEntity.getAditionals() != null)
				this.aditionals = restaurantMenuItemEntity.getAditionals().stream().map(aditional -> {

					aditional.setItems(null);

					return new RestaurantMenuAditionalDTO(aditional);
				}).collect(Collectors.toSet());

		}

	}

}
