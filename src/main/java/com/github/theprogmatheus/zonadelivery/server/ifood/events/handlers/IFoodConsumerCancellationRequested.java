package com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.events.EventManager;
import com.github.theprogmatheus.zonadelivery.server.events.EventType;
import com.github.theprogmatheus.zonadelivery.server.ifood.events.IFoodEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodEvent;
import com.github.theprogmatheus.zonadelivery.server.service.OrderService;

public class IFoodConsumerCancellationRequested implements IFoodEventHandler {

	private OrderService orderService;

	public IFoodConsumerCancellationRequested(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public boolean handle(IFoodEvent event) throws Exception {

		RestaurantOrderEntity order = this.orderService.getOrderByIFoodId(event.getOrderId());
		if (order != null)
			EventManager.emit(order.getRestaurant().getId(), EventType.IFOOD_CONSUMER_CANCELLATION_REQUESTED, order);

		return true;
	}

	@Override
	public String getEventName() {
		return "CONSUMER_CANCELLATION_REQUESTED";
	}

}
