package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.RestaurantMenuAditionalEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantMenuAditionalDTO {

	private UUID id;
	private RestaurantMenuItemDTO item;
	private String name;
	private double price;

	public RestaurantMenuAditionalDTO(RestaurantMenuAditionalEntity restaurantMenuAditionalEntity) {
		if (restaurantMenuAditionalEntity != null) {

			if (restaurantMenuAditionalEntity.getId() != null)
				this.id = restaurantMenuAditionalEntity.getId();

			if (restaurantMenuAditionalEntity.getItem() != null)
				this.item = new RestaurantMenuItemDTO(restaurantMenuAditionalEntity.getItem());

			if (restaurantMenuAditionalEntity.getName() != null)
				this.name = restaurantMenuAditionalEntity.getName();

			this.price = restaurantMenuAditionalEntity.getPrice();
		}
	}
}
