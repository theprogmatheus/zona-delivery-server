package com.github.theprogmatheus.zonadelivery.server.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

	@GetMapping("/")
	public Object index() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
