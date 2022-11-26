package com.github.theprogmatheus.zonadelivery.server.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantOrderDTO;
import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantOrderInputDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.service.OrderService;

@RestController
@RequestMapping("/restaurant/{restaurantId}/order")
public class RestaurantOrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/create")
	public Object place(@PathVariable UUID restaurantId, @RequestBody RestaurantOrderInputDTO order) {
		try {

			Object result = this.orderService.placeOrder(restaurantId, order.getChannel(), null, order.getSimpleId(),
					order.getDeliveryDateTime(), order.getOrderType(), order.getCustomerId(), order.getAddressId(),
					order.getItems(), order.getTotal(), order.getPayment());

			if (result instanceof RestaurantOrderEntity)
				return new RestaurantOrderDTO((RestaurantOrderEntity) result);

			return ResponseEntity.ok(result);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@PostMapping("/{orderId}/confirm")
	public Object confirm(@PathVariable UUID restaurantId, @PathVariable UUID orderId) {
		try {

			Object result = this.orderService.acceptOrder(orderId);

			if (result instanceof RestaurantOrderEntity)
				return new RestaurantOrderDTO((RestaurantOrderEntity) result);

			return ResponseEntity.ok(result);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@PostMapping("/{orderId}/cancel")
	public Object cancel(@PathVariable UUID restaurantId, @PathVariable UUID orderId) {
		try {

			Object result = this.orderService.cancelOrder(orderId);

			if (result instanceof RestaurantOrderEntity)
				return new RestaurantOrderDTO((RestaurantOrderEntity) result);

			return ResponseEntity.ok(result);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@PostMapping("/{orderId}/dispatch")
	public Object dispatch(@PathVariable UUID restaurantId, @PathVariable UUID orderId) {
		try {

			Object result = this.orderService.dispatchOrder(orderId);

			if (result instanceof RestaurantOrderEntity)
				return new RestaurantOrderDTO((RestaurantOrderEntity) result);

			return ResponseEntity.ok(result);

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@PostMapping("/{orderId}/ready")
	public Object ready(@PathVariable UUID restaurantId, @PathVariable UUID orderId) {
		try {

			Object result = this.orderService.readyToPickupOrder(orderId);

			if (result instanceof RestaurantOrderEntity)
				return new RestaurantOrderDTO((RestaurantOrderEntity) result);

			return ResponseEntity.ok(result);

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

}
