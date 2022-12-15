package com.github.theprogmatheus.zonadelivery.server.whatsapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WhatsApp {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	public static void init() {
	}

	public static Object handleEvent(WhatsappEvent event) {

		return null;

	}

}
