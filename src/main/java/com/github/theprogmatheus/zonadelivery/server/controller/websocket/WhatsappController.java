package com.github.theprogmatheus.zonadelivery.server.controller.websocket;

import java.util.Arrays;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.github.theprogmatheus.zonadelivery.server.config.websocket.WebSocketUser;
import com.github.theprogmatheus.zonadelivery.server.whatsapp.WhatsappEvent;
import com.github.theprogmatheus.zonadelivery.server.whatsapp.WhatsappExecutor;

@Controller
public class WhatsappController {

	@MessageMapping("/whatsapp/event")
	@SendToUser(value = "/whatsapp/execute", broadcast = false)
	public WhatsappExecutor onWhatsappEvent(@Payload WhatsappEvent event, WebSocketUser user) {

		System.out.println("Received whatsapp event " + event.getEventName() + ", " + event.getEventArgs());
		/*
		 * Nesse endpoint será recebeido TODOS os eventos dos gestores dos clientes.
		 * 
		 * Aqui será tratado cada evento individualmente por gestor. 
		 * 
		 * Será enviado o evento para um 'handler' onde retornará um 'WhatsappExecutor',
		 * com o evento, comando e argumentos
		 * 
		 * Esse 'WhatsappExecutor' será interpretado no lado do cliente, e será
		 * executado de acordo.  clear
		 * 
		 * 
		 * verificar depois se tem NLP para Java 
		 */ 

		return new WhatsappExecutor(event, "reply", 
				Arrays.asList("Não consegui entender o que você disse, poderia ser mais específico?"));
	}

}
