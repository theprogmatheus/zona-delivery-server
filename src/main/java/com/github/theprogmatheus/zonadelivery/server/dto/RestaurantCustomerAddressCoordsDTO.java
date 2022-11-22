package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressCoordsEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantCustomerAddressCoordsDTO {

	private UUID id;
	private RestaurantCustomerAddressDTO address;
	private double longitude;
	private double latitude;

	public RestaurantCustomerAddressCoordsDTO(RestaurantCustomerAddressCoordsEntity coordinates) {
		if (coordinates != null) {
			if (coordinates.getId() != null)
				this.id = coordinates.getId();

			if (coordinates.getAddress() != null)
				this.address = new RestaurantCustomerAddressDTO(coordinates.getAddress());

			this.longitude = coordinates.getLongitude();

			this.latitude = coordinates.getLatitude();
		}
	}
}
