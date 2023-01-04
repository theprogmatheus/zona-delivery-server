package com.github.theprogmatheus.zonadelivery.server.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.theprogmatheus.zonadelivery.server.service.OrderService;

@Component
public class OrderFinishScheduler {

	@Autowired
	private OrderService orderService;
	
	
	@Scheduled(fixedRate = 1_800_000) // 1_800_000 = 30 minutes
	public void finishOldOrders() {
		orderService.finishOldOrders();
	}
}
