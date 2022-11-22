package com.github.theprogmatheus.zonadelivery.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity.RestaurantCustomerAddressCoords;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantCustomerAddressCoordsDTO {

	private double longitude;
	private double latitude;

	public RestaurantCustomerAddressCoordsDTO(RestaurantCustomerAddressCoords coordinates) {
		if (coordinates != null) {
			this.longitude = coordinates.getLongitude();
			this.latitude = coordinates.getLatitude();
		}
	}
}
