package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.RestaurantEntity;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class RestaurantDTO {

	private UUID id;
	private String nameId;
	private String displayName;
	private UserDTO owner;

	public RestaurantDTO(RestaurantEntity restaurantEntity) {
		this.id = restaurantEntity.getId();
		this.nameId = restaurantEntity.getNameId();
		this.displayName = restaurantEntity.getDisplayName();

		if (restaurantEntity.getOwner() != null) {

			restaurantEntity.getOwner().setRestaurants(null);

			this.owner = new UserDTO(restaurantEntity.getOwner());

		}

	}

}
