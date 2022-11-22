package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantCustomerAddressDTO {

	private UUID id;
	private RestaurantCustomerDTO customer;
	private String streetName;
	private String streetNumber;
	private String neighborhood;
	private String complement;
	private String postalCode;
	private String city;
	private String state;
	private String country;
	private String reference;
	private RestaurantCustomerAddressCoordsDTO coordinates;

	public RestaurantCustomerAddressDTO(RestaurantCustomerAddressEntity address) {
		if (address != null) {

			if (address.getId() != null)
				this.id = address.getId();

			if (address.getCustomer() != null) {
				this.customer = new RestaurantCustomerDTO(address.getCustomer());
				// this.customer.setAddresses(null);
			}

			if (address.getStreetName() != null)
				this.streetName = address.getStreetName();

			if (address.getStreetNumber() != null)
				this.streetNumber = address.getStreetNumber();

			if (address.getNeighborhood() != null)
				this.neighborhood = address.getNeighborhood();

			if (address.getComplement() != null)
				this.complement = address.getComplement();

			if (address.getPostalCode() != null)
				this.postalCode = address.getPostalCode();

			if (address.getCity() != null)
				this.city = address.getCity();

			if (address.getState() != null)
				this.state = address.getState();

			if (address.getCountry() != null)
				this.country = address.getCountry();

			if (address.getReference() != null)
				this.reference = address.getReference();

			if (address.getCoordinates() != null) {
				address.getCoordinates().setAddress(null);
				this.coordinates = new RestaurantCustomerAddressCoordsDTO(address.getCoordinates());
			}

		}
	}
}
