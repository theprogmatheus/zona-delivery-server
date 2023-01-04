package com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.theprogmatheus.zonadelivery.server.ifood.events.IFoodEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodEvent;
import com.github.theprogmatheus.zonadelivery.server.service.EventService;

public class IFoodConsumerCancellationRequested implements IFoodEventHandler {
	
	@Autowired
	private EventService eventService;

	@Override
	public boolean handle(IFoodEvent event) throws Exception {
		
		return true;
	}

	@Override
	public String getEventName() {
		return "CONSUMER_CANCELLATION_REQUESTED";
	}

}
