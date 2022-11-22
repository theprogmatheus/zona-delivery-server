package com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers;

import com.github.theprogmatheus.zonadelivery.server.ifood.IFoodAPI;
import com.github.theprogmatheus.zonadelivery.server.ifood.events.IFoodEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodEvent;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDetails;
import com.github.theprogmatheus.zonadelivery.server.service.OrderService;

public class IFoodOrderConfirmedEventHandler implements IFoodEventHandler {

	private OrderService orderService;

	public IFoodOrderConfirmedEventHandler(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public boolean handle(IFoodEvent event) throws Exception {

		IFoodOrderDetails orderDetails = IFoodAPI.getOrderDetails(event.getOrderId());
		if (orderDetails != null)
			this.orderService.createNewOrderByIfoodOrder(orderDetails);

		/*
		 * A aplicação deveria adicionar o pedido ao ser confirmado, mas por exigência
		 * do ifood a aplicação só adiciona depois de despachado
		 */

		return true;
	}

	@Override
	public String getEventName() {
		return "CONFIRMED";
	}

}
