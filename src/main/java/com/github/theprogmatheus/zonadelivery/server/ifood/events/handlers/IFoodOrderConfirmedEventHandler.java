package com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers;

import com.github.theprogmatheus.zonadelivery.server.ifood.events.IFoodEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodEvent;

public class IFoodOrderConfirmedEventHandler implements IFoodEventHandler {

	@Override
	public boolean handle(IFoodEvent event) throws Exception {
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
