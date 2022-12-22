package com.github.theprogmatheus.zonadelivery.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.service.UserService;

@Component
public class MyAuthenticationManager implements AuthenticationManager {

	@Autowired
	private UserService userService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		this.userService.createDefaultUserIfNotExists();

		if (authentication == null)
			throw new BadCredentialsException("BCE1000");

		if (authentication.getPrincipal() == null)
			throw new BadCredentialsException("BCE1001");

		if (authentication.getCredentials() == null)
			throw new BadCredentialsException("BCE1002");

		String email = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();

		UserEntity user = this.userService.getUserByEmail(email);

		if (user == null)
			throw new BadCredentialsException("BCE1003");

		// check password
		if (!(userService.checkPassword(password, user.getPassword())))
			throw new BadCredentialsException("BCE1004");

		return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	}

}