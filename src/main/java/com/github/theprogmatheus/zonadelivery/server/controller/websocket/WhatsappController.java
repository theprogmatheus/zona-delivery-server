package com.github.theprogmatheus.zonadelivery.server.controller.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.github.theprogmatheus.zonadelivery.server.config.websocket.WebSocketUser;
import com.github.theprogmatheus.zonadelivery.server.service.WhatsappService;
import com.github.theprogmatheus.zonadelivery.server.whatsapp.WhatsappEvent;
import com.github.theprogmatheus.zonadelivery.server.whatsapp.WhatsappExecutor;

@Controller
public class WhatsappController {

	@Autowired
	private WhatsappService service;

	@MessageMapping("/whatsapp/event")
	@SendToUser(value = "/whatsapp/execute", broadcast = false)
	public WhatsappExecutor onWhatsappEvent(@Payload WhatsappEvent event, WebSocketUser user) {
		return this.service.handleEvent(event);
	}

}
