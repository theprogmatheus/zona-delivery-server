package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEmployeeUserEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantEmployeeUserDTO {

	private UUID id;
	private UUID restaurant;
	private String username;
	private String displayName;
	private Collection<? extends GrantedAuthority> authorities;

	public RestaurantEmployeeUserDTO(RestaurantEmployeeUserEntity employee) {
		if (employee != null) {
			this.id = employee.getId();
			this.restaurant = employee.getRestaurant().getId();
			this.username = employee.getUsername();
			this.displayName = employee.getDisplayName();
			this.authorities = employee.getAuthorities();
		}
	}
}
