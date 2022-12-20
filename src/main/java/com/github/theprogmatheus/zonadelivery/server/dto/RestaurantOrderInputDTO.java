package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderItem;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderPayment;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderTotal;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class RestaurantOrderInputDTO {

	private UUID restaurantId, customerId, addressId;
	private String channel, simpleId, orderType;
	private Date deliveryDateTime;
	private List<RestaurantOrderItem> items;
	private RestaurantOrderTotal total;
	private RestaurantOrderPayment payment;
	private String note;

}
