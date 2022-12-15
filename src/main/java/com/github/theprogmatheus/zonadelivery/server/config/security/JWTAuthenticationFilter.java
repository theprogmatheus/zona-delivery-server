package com.github.theprogmatheus.zonadelivery.server.config.security;

import static com.github.theprogmatheus.zonadelivery.server.config.security.SecurityConfiguration.EXPIRATION_TIME;
import static com.github.theprogmatheus.zonadelivery.server.config.security.SecurityConfiguration.SIGN_UP_URL;
import static com.github.theprogmatheus.zonadelivery.server.config.security.SecurityConfiguration.TOKEN_TYPE;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.theprogmatheus.zonadelivery.server.dto.UserDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;

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
			
			return this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null));
		} catch (IOException e) {
			throw new RuntimeException("Falha ao autenticar usuario: ", e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		

		Date expireAt = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
		String token = JWT.create().withSubject(((UserEntity) authResult.getPrincipal()).getId().toString())
				.withExpiresAt(expireAt).sign(Algorithm.HMAC512(System.getenv("SPRING_JWT_SECRET").getBytes()));

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("type", TOKEN_TYPE);
		responseBody.put("accessToken", token);
		responseBody.put("expireIn", EXPIRATION_TIME / 1_000);
		responseBody.put("user", new UserDTO((UserEntity) authResult.getPrincipal()));

		response.addHeader("Content-Type", "application/json");
		response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
		response.getWriter().flush();

	}
}