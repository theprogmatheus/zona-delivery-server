package com.github.theprogmatheus.zonadelivery.server.whatsapp;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhatsappEvent {

	private String eventName;
	private List<?> eventArgs;

}
