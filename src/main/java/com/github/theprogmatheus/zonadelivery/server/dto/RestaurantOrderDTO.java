package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderItem;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderPayment;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderTotal;
import com.github.theprogmatheus.zonadelivery.server.enums.OrderStatus;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDetails;

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
	private Date createdAt;
	private Date deliveryDateTime;
	private String orderType;
	private OrderStatus status;
	private RestaurantCustomerDTO customer;
	private RestaurantCustomerAddressDTO address;
	private List<RestaurantOrderItem> items;
	private RestaurantOrderTotal total;
	private RestaurantOrderPayment payment;
	private IFoodOrderDetails ifoodOrder;
	private String note;

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

			if (restaurantOrderEntity.getCreatedAt() != null)
				this.createdAt = restaurantOrderEntity.getCreatedAt();

			if (restaurantOrderEntity.getDeliveryDateTime() != null)
				this.deliveryDateTime = restaurantOrderEntity.getDeliveryDateTime();

			if (restaurantOrderEntity.getOrderType() != null)
				this.orderType = restaurantOrderEntity.getOrderType();

			if (restaurantOrderEntity.getStatus() != null)
				this.status = restaurantOrderEntity.getStatus();

			if (restaurantOrderEntity.getCustomer() != null)
				this.customer = new RestaurantCustomerDTO(restaurantOrderEntity.getCustomer());

			if (restaurantOrderEntity.getAddress() != null)
				this.address = new RestaurantCustomerAddressDTO(restaurantOrderEntity.getAddress());

			if (restaurantOrderEntity.getItems() != null)
				this.items = restaurantOrderEntity.getItems();

			if (restaurantOrderEntity.getTotal() != null)
				this.total = restaurantOrderEntity.getTotal();

			if (restaurantOrderEntity.getPayment() != null)
				this.payment = restaurantOrderEntity.getPayment();

			if (restaurantOrderEntity.getIfoodOrder() != null)
				this.ifoodOrder = restaurantOrderEntity.getIfoodOrder();
			
			this.note = restaurantOrderEntity.getNote();
		}

	}

}
