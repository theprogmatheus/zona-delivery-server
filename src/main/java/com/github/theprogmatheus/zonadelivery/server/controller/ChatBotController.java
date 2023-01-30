package com.github.theprogmatheus.zonadelivery.server.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.enums.UserRole;

@Secured(UserRole.USER_ROLE_NAME)
@RestController
@RequestMapping("/chatbot")
public class ChatBotController {

	/*
	 * Antes de começar preciso ter em mente que.. o chatbot precisa manter um
	 * contexto, isso é ele poderá usar informações de mensagens anteriores para
	 * continuar uma conversa fluida.
	 * 
	 * 
	 */
	public Object proccess(String input) {
		return null;
	}

}
