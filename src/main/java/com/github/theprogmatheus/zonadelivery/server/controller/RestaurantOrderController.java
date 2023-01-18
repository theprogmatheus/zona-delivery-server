package com.github.theprogmatheus.zonadelivery.server.controller;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantOrderDTO;
import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantOrderInputDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.UserRole;
import com.github.theprogmatheus.zonadelivery.server.ifood.IFoodAPI;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodCancellationReason;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodCancellationReasonRequest;
import com.github.theprogmatheus.zonadelivery.server.service.OrderService;

@Secured(value = { UserRole.USER_ROLE_NAME, UserRole.EMPLOYEE_ROLE_NAME })
@RestController
@RequestMapping("/restaurant/{restaurantId}/order")
public class RestaurantOrderController {

	@Autowired
	private OrderService orderService; // /restaurant/{restaurantId}/order/list

	@GetMapping("/list")
	public Object list(@PathVariable UUID restaurantId, @PathParam("search") String search) {
		try {
			List<RestaurantOrderDTO> result = this.orderService.listLastOrders(restaurantId).stream()
					.map(order -> new RestaurantOrderDTO(order)).collect(Collectors.toList());

			if (search != null && !search.isBlank())
				result = result.stream().filter(order -> this.orderService.searchMatch(order, search))
						.collect(Collectors.toList());

			return ResponseEntity.ok(result);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@Secured(UserRole.USER_ROLE_NAME)
	@GetMapping("/list/all")
	public Object listAll(@PathVariable UUID restaurantId, @PathParam("search") String search) {
		try {
			List<RestaurantOrderDTO> result = this.orderService.listOrders(restaurantId).stream()
					.map(order -> new RestaurantOrderDTO(order)).collect(Collectors.toList());

			if (search != null && !search.isBlank())
				result = result.stream().filter(order -> this.orderService.searchMatch(order, search))
						.collect(Collectors.toList());

			return ResponseEntity.ok(result);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@GetMapping("/{orderId}")
	public Object order(@PathVariable UUID restaurantId, @PathVariable UUID orderId) {
		try {
			Object result = this.orderService.getOrder(orderId);

			if (result instanceof RestaurantOrderEntity)
				return new RestaurantOrderDTO((RestaurantOrderEntity) result);

			return ResponseEntity.ok(result);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@Secured(UserRole.USER_ROLE_NAME)
	@PostMapping("/create")
	public Object place(@PathVariable UUID restaurantId, @RequestBody RestaurantOrderInputDTO order) {
		try {
// verificar ao fazer um pedido que não é para entrega, não precisar de endereço
			Object result = this.orderService.placeOrder(restaurantId, order.getChannel(), null, order.getSimpleId(),
					order.getDeliveryDateTime(), order.getOrderType(), order.getCustomerId(), order.getAddressId(),
					order.getItems(), order.getTotal(), order.getPayment(), order.getNote());

			if (result instanceof RestaurantOrderEntity)
				return new RestaurantOrderDTO((RestaurantOrderEntity) result);

			return ResponseEntity.ok(result);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@Secured(UserRole.USER_ROLE_NAME)
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

	@Secured(UserRole.USER_ROLE_NAME)
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
			Object result = null;

			Object order = this.orderService.getOrder(orderId);

			if (order instanceof RestaurantOrderEntity) {
				RestaurantOrderEntity theOrder = ((RestaurantOrderEntity) order);
				if (theOrder.getIfoodOrder() != null && (theOrder.getIfoodOrder().getOrderType().equals("TAKEOUT"))) {
					// manda ready
					result = this.orderService.readyToPickupOrder(orderId);
				} else {
					// manda o dispatch
					result = this.orderService.dispatchOrder(orderId);
				}
				// TAKEOUT
			}

			if (result != null && (result instanceof RestaurantOrderEntity))
				return new RestaurantOrderDTO((RestaurantOrderEntity) result);

			return ResponseEntity.ok(result);

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@Secured(UserRole.USER_ROLE_NAME)
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

	@Secured(UserRole.USER_ROLE_NAME)
	@GetMapping("/{orderId}/ifood/cancellationReasons")
	public Object cancellationReasons(@PathVariable UUID restaurantId, @PathVariable UUID orderId) {
		try {
			Object result = this.orderService.getOrder(orderId);
			if (result instanceof RestaurantOrderEntity) {
				RestaurantOrderEntity order = ((RestaurantOrderEntity) result);
				if (order.getIfoodOrder() != null) {
					List<IFoodCancellationReason> reasons = IFoodAPI
							.getCancellationReasons(order.getIfoodOrder().getId());
					if (reasons != null)
						return ResponseEntity.ok(reasons);
				}
			}
			return ResponseEntity.ok(Arrays.asList());
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@Secured(UserRole.USER_ROLE_NAME)
	@PostMapping("/{orderId}/ifood/requestCancellation")
	public Object requestCancellation(@PathVariable UUID restaurantId, @PathVariable UUID orderId,
			@RequestBody IFoodCancellationReasonRequest request) {
		Object result = this.orderService.getOrder(orderId);
		if (result instanceof RestaurantOrderEntity) {
			RestaurantOrderEntity order = ((RestaurantOrderEntity) result);
			if (order.getIfoodOrder() != null)
				IFoodAPI.requestCancellation(order.getIfoodOrder().getId(), request);
		}
		return ResponseEntity.ok(request);
	}

	@Secured(UserRole.USER_ROLE_NAME)
	@PostMapping("/{orderId}/ifood/acceptCancellation")
	public Object acceptCancellation(@PathVariable UUID restaurantId, @PathVariable UUID orderId) {
		try {
			Object result = this.orderService.getOrder(orderId);
			if (result instanceof RestaurantOrderEntity) {
				RestaurantOrderEntity order = ((RestaurantOrderEntity) result);
				if (order.getIfoodOrder() != null)
					IFoodAPI.acceptCancellation(order.getIfoodOrder().getId());

			}
			return ResponseEntity.ok();
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

	@Secured(UserRole.USER_ROLE_NAME)
	@PostMapping("/{orderId}/ifood/denyCancellation")
	public Object denyCancellation(@PathVariable UUID restaurantId, @PathVariable UUID orderId) {
		try {
			Object result = this.orderService.getOrder(orderId);
			if (result instanceof RestaurantOrderEntity) {
				RestaurantOrderEntity order = ((RestaurantOrderEntity) result);
				if (order.getIfoodOrder() != null)
					IFoodAPI.denyCancellation(order.getIfoodOrder().getId());

			}
			return ResponseEntity.ok();
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
		}
	}

}
