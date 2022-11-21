package com.github.theprogmatheus.zonadelivery.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@PostMapping("/register")
	public void register(String username, String email, String password) {
		
	}

}
