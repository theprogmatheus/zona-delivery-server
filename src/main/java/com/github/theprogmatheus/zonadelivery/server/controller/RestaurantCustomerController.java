package com.github.theprogmatheus.zonadelivery.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.service.RestaurantCustomerService;

@RestController
@RequestMapping("/restaurant/customer")
public class RestaurantCustomerController {

	@Autowired
	private RestaurantCustomerService customerService;

	public Object list() {
		return null;
	}

	public Object register() {
		return null;
	}

	public Object changeName() {
		return null;
	}

	public Object changeIFoodId() {
		return null;
	}

	public Object changeWhatsappId() {
		return null;
	}

	public Object changePhone() {
		return null;
	}

	public Object addresses() {
		return null;
	}

	public Object addressRegister() {
		return null;
	}

	public Object addressDelete() {
		return null;
	}

	public Object delete() {
		return null;

	}
}
