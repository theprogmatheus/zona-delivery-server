package com.github.theprogmatheus.zonadelivery.server.config.security;

import static com.github.theprogmatheus.zonadelivery.server.config.security.SecurityConfiguration.EXPIRATION_TIME;
import static com.github.theprogmatheus.zonadelivery.server.config.security.SecurityConfiguration.SIGN_UP_URL;
import static com.github.theprogmatheus.zonadelivery.server.config.security.SecurityConfiguration.TOKEN_TYPE;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.theprogmatheus.zonadelivery.server.dto.AuthenticationResponseDTO;
import com.github.theprogmatheus.zonadelivery.server.dto.UserDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.UserType;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;

		setFilterProcessesUrl(SIGN_UP_URL);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UserEntity user = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);

			return this.authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), null));
		} catch (IOException e) {
			throw new RuntimeException("Falha ao autenticar usuario: ", e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String token = SecurityConfiguration.createJWTToken(UserType.USER,
				((UserEntity) authResult.getPrincipal()).getId().toString(), EXPIRATION_TIME);

		AuthenticationResponseDTO authenticationResponse = new AuthenticationResponseDTO(TOKEN_TYPE, token,
				EXPIRATION_TIME / 1_000, new UserDTO((UserEntity) authResult.getPrincipal()));

		response.addHeader("Content-Type", "application/json");
		response.getWriter().write(new ObjectMapper().writeValueAsString(authenticationResponse));
		response.getWriter().flush();

	}
}