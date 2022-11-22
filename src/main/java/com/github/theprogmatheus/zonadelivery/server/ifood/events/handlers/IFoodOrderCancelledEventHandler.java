package com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers;

import com.github.theprogmatheus.zonadelivery.server.ifood.events.IFoodEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodEvent;

public class IFoodOrderCancelledEventHandler implements IFoodEventHandler {

	@Override
	public boolean handle(IFoodEvent event) throws Exception {
		
		// verifica se o pedido ainda não saiu, se não saiu, então arquive-o com tag
		// cancelled

		// caso o pedido já tenha saído, e esteja em rota... arquive-o com tag cancelled

		// caso o pedido já tenha sido arquivado, atualize a tag e o orderDetails

		/*
		 * 
		 * OrderModel orderModel =
		 * this.orderRepository.findByOrderId(event.getOrderId()); if (orderModel !=
		 * null) { orderModel.setOrderStatus(getEventName()); return
		 * this.orderRepository.saveAndFlush(orderModel) != null; }
		 */
		return true;
	}

	@Override
	public String getEventName() {
		return "CANCELLED";
	}

}
