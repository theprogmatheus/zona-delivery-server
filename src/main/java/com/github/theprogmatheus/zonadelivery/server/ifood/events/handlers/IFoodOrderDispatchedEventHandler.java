package com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers;

import com.github.theprogmatheus.zonadelivery.server.ifood.events.IFoodEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodEvent;

public class IFoodOrderDispatchedEventHandler implements IFoodEventHandler {


	/*
	 * O pedido SÓ é adicionado ao banco de dados depois de despachado, para evitar
	 * cancelamentos pelo cliente (requisitos ifood)
	 */
	@Override
	public boolean handle(IFoodEvent event) throws Exception {
		/*
		String orderId = event.getOrderId();

		IFoodOrderDetails orderDetails = IFoodAPI.getOrderDetails(orderId);
		if (orderDetails != null) {

			if (orderDetails.getDelivery().getDeliveredBy() == "IFOOD")
				return true;

			/*
			 * Se esse pedido já foi adicionado, então ignore-o
			 
			if (this.orderRepository.findByOrderId(orderDetails.getId()) != null)
				return true;

			String displayId = orderDetails.getDisplayId();
			String merchantId = event.getMerchantId();

			OrderModel order = new OrderModel();

			order.setOrderId(orderId);
			order.setDisplayId(displayId);
			order.setMerchantId(merchantId);
			order.setOrderStatus(getEventName());

			return this.orderRepository.saveAndFlush(order) != null;
		}
*/
		return true;
	}

	@Override
	public String getEventName() {
		return "DISPATCHED";
	}

}
