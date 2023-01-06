package com.github.theprogmatheus.zonadelivery.server.schedulers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.theprogmatheus.zonadelivery.server.events.EventManager;

@Component
public class EventsCleanUpScheduler {

	@Scheduled(fixedRate = 300_000) // 5 min
	public void cleanUp() {
		EventManager.cleanUp();
	}

}
