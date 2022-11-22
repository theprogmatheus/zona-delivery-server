package com.github.theprogmatheus.zonadelivery.server.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.dto.UserDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public Object register(@RequestBody Map<String, String> body) {

		String username = body.get("username");
		String password = body.get("password");
		String email = body.get("email");

		Object result = this.userService.createNewUserAccount(username, password, email);

		if (result instanceof UserEntity)
			return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO((UserEntity) result));

		return ResponseEntity.ok(result);
	}

}
