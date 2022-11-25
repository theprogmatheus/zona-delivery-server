package com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers;

import com.github.theprogmatheus.zonadelivery.server.ifood.IFoodAPI;
import com.github.theprogmatheus.zonadelivery.server.ifood.events.IFoodEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodEvent;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDetails;
import com.github.theprogmatheus.zonadelivery.server.service.OrderService;

public class IFoodOrderCancelledEventHandler implements IFoodEventHandler {

	private OrderService orderService;

	public IFoodOrderCancelledEventHandler(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public boolean handle(IFoodEvent event) throws Exception {

		IFoodOrderDetails orderDetails = IFoodAPI.getOrderDetails(event.getOrderId());
		if (orderDetails != null) {

			// Emitir um evento de cancelamento para minha aplicação
		}

		return true;
	}

	@Override
	public String getEventName() {
		return "CANCELLED";
	}

}
