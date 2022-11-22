package com.github.theprogmatheus.zonadelivery.server.ifood.events;

import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodEvent;

public interface IFoodEventHandler {

	public abstract boolean handle(IFoodEvent event) throws Exception;

	public abstract String getEventName();
}
