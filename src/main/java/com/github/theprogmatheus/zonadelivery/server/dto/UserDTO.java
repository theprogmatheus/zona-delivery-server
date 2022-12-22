package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class UserDTO {

	private UUID id;
	private String username;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;
	private Set<RestaurantDTO> restaurants;

	public UserDTO(UserEntity userEntity) {
		this.id = userEntity.getId();
		this.username = userEntity.getUsername();
		this.email = userEntity.getEmail();
		this.authorities = userEntity.getAuthorities();

		if (userEntity.getRestaurants() != null) {
			this.restaurants = userEntity.getRestaurants().stream().map(restaurant -> {
				
				
				RestaurantDTO restaurantDTO = new RestaurantDTO();
				restaurantDTO.setId(restaurant.getId());
				restaurantDTO.setNameId(restaurant.getNameId());
				restaurantDTO.setDisplayName(restaurant.getDisplayName());

				return restaurantDTO;
			}).collect(Collectors.toSet());
		}
	}
}
