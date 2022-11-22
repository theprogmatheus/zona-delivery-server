package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderItem;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantOrderDTO {

	private UUID id;
	private String simpleId;
	private RestaurantDTO restaurant;
	private String channel;
	private RestaurantCustomerDTO customer;
	private RestaurantCustomerAddressDTO address;
	private List<RestaurantOrderItem> items;

	public RestaurantOrderDTO(RestaurantOrderEntity restaurantOrderEntity) {
		if (restaurantOrderEntity != null) {

			if (restaurantOrderEntity.getId() != null)
				this.id = restaurantOrderEntity.getId();

			if (restaurantOrderEntity.getSimpleId() != null)
				this.simpleId = restaurantOrderEntity.getSimpleId();

			if (restaurantOrderEntity.getRestaurant() != null)
				this.restaurant = new RestaurantDTO(restaurantOrderEntity.getRestaurant());

			if (restaurantOrderEntity.getChannel() != null)
				this.channel = restaurantOrderEntity.getChannel();

			if (restaurantOrderEntity.getCustomer() != null)
				this.customer = new RestaurantCustomerDTO(restaurantOrderEntity.getCustomer());

			if (restaurantOrderEntity.getAddress() != null)
				this.address = new RestaurantCustomerAddressDTO(restaurantOrderEntity.getAddress());

			if (restaurantOrderEntity.getItems() != null)
				this.items = restaurantOrderEntity.getItems();

		}

	}

}
