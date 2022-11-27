package com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.OrderStatus;
import com.github.theprogmatheus.zonadelivery.server.ifood.events.IFoodEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodEvent;
import com.github.theprogmatheus.zonadelivery.server.service.OrderService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IFoodOrderConcludedEventHandler implements IFoodEventHandler {

	private OrderService orderService;

	@Override
	public boolean handle(IFoodEvent event) throws Exception {

		RestaurantOrderEntity order = this.orderService.getOrderByIFoodId(event.getOrderId());
		if (order != null)
			this.orderService.changeOrderStatus(order.getId(), OrderStatus.CONCLUDED);
		
		
		
		return true;
	}

	@Override
	public String getEventName() {
		return "CONCLUDED";
	}

}
