package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantCustomerDTO {

	private UUID id;
	private RestaurantDTO restaurant;
	private String name;
	private String ifoodCustomerId;
	private String whatsappCustomerId;
	private String phone;
	private Set<RestaurantCustomerAddressDTO> addresses;

	public RestaurantCustomerDTO(RestaurantCustomerEntity restaurantCustomerEntity) {

		if (restaurantCustomerEntity != null) {

			if (restaurantCustomerEntity.getId() != null)
				this.id = restaurantCustomerEntity.getId();
			
			if (restaurantCustomerEntity.getRestaurant() != null)
				this.restaurant = new RestaurantDTO(restaurantCustomerEntity.getRestaurant());
			
			if (restaurantCustomerEntity.getName() != null)
				this.name = restaurantCustomerEntity.getName();
			
			if (restaurantCustomerEntity.getIfoodCustomerId() != null)
				this.ifoodCustomerId = restaurantCustomerEntity.getIfoodCustomerId();
			
			if (restaurantCustomerEntity.getWhatsappCustomerId() != null)
				this.whatsappCustomerId = restaurantCustomerEntity.getWhatsappCustomerId();
			
			if (restaurantCustomerEntity.getPhone() != null)
				this.phone = restaurantCustomerEntity.getPhone();

			if (restaurantCustomerEntity.getAddresses() != null)
				this.addresses = restaurantCustomerEntity.getAddresses().stream().map(address -> {

					address.setCustomer(null);
					return new RestaurantCustomerAddressDTO(address);
				}).collect(Collectors.toSet());
		}

	}

}
