package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.UUID;

import com.github.theprogmatheus.zonadelivery.server.entity.RestaurantEntity;

import lombok.Data;

@Data
public class RestaurantDTO {

	private long id;
	private UUID restaurantId;
	private String nameId;
	private String displayName;
	private UserDTO owner;

	public RestaurantDTO(RestaurantEntity restaurantEntity) {
		this.id = restaurantEntity.getId();
		this.restaurantId = restaurantEntity.getRestaurantId();
		this.nameId = restaurantEntity.getNameId();
		this.displayName = restaurantEntity.getDisplayName();
		this.owner = new UserDTO(restaurantEntity.getOwner());
	}

}
