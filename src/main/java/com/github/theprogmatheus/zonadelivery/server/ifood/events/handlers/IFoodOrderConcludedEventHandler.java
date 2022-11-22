package com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers;

import com.github.theprogmatheus.zonadelivery.server.ifood.events.IFoodEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodEvent;

public class IFoodOrderConcludedEventHandler implements IFoodEventHandler {

	@Override
	public boolean handle(IFoodEvent event) throws Exception {
		/*
		 * OrderModel orderModel =
		 * this.orderRepository.findByOrderId(event.getOrderId()); if (orderModel !=
		 * null) { orderModel.setOrderStatus(getEventName()); return
		 * this.orderRepository.saveAndFlush(orderModel) != null; }
		 */
		return true;
	}

	@Override
	public String getEventName() {
		return "CONCLUDED";
	}

}
