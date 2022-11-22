package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantDTO {

	private UUID id;
	private String nameId;
	private String displayName;
	private UserDTO owner;
	private Set<RestaurantMenuDTO> menus;

	public RestaurantDTO(RestaurantEntity restaurantEntity) {
		if (restaurantEntity != null) {
			if (restaurantEntity.getId() != null)
				this.id = restaurantEntity.getId();

			if (restaurantEntity.getNameId() != null)
				this.nameId = restaurantEntity.getNameId();

			if (restaurantEntity.getDisplayName() != null)
				this.displayName = restaurantEntity.getDisplayName();

			if (restaurantEntity.getOwner() != null) {
				restaurantEntity.getOwner().setRestaurants(null);
				this.owner = new UserDTO(restaurantEntity.getOwner());
			}

			if (restaurantEntity.getMenus() != null)
				this.menus = restaurantEntity.getMenus().stream().map(menu -> {
					menu.setRestaurant(null);
					return new RestaurantMenuDTO(menu);
				}).collect(Collectors.toSet());

		}
	}
}
