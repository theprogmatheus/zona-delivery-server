package com.github.theprogmatheus.zonadelivery.server.events;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

	private UUID id;
	private UUID restaurant;
	private EventType type;
	private Date createdAt;
	private Object data;
}
