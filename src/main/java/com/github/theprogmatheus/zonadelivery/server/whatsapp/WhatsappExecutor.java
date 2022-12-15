package com.github.theprogmatheus.zonadelivery.server.whatsapp;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhatsappExecutor {

	private WhatsappEvent event;
	private String command;
	private List<Object> args;

}
