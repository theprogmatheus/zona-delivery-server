package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.whatsapp.ChatSession;
import com.github.theprogmatheus.zonadelivery.server.whatsapp.WhatsappEvent;
import com.github.theprogmatheus.zonadelivery.server.whatsapp.WhatsappExecutor;

/*
 * É importante que eu separe as sessõs dos restaurantes
 */

@Service
public class WhatsappService {

	private static Map<String, ChatSession> chatSessions = new HashMap<>();

	public WhatsappExecutor handleEvent(WhatsappEvent event) {
		switch (event.getEventName()) {

		case "message":
			return handleMessage(new JSONObject(event.getEventArgs().get(0)));

		default:
			return new WhatsappExecutor(event, null, null);
		}
	}

	public WhatsappExecutor handleMessage(JSONObject message) {

		JSONObject id = message.getJSONObject("id");
		String remote = id.getString("remote");
		String rawMessage = message.getString("body");

		ChatSession session = chatSessions.get(remote);
		if (session == null)
			chatSessions.put(remote, (session = new ChatSession(remote)));
		
		
		

		System.out.println("The remote is " + remote);
		System.out.println("The rawmessage is " + rawMessage);
		System.out.println(message);

		return null;
	}

}
