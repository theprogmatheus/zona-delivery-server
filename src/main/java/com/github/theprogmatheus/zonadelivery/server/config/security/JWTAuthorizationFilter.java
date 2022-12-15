package com.github.theprogmatheus.zonadelivery.server.config.security;

import static com.github.theprogmatheus.zonadelivery.server.config.security.SecurityConfiguration.HEADER_STRING;
import static com.github.theprogmatheus.zonadelivery.server.config.security.SecurityConfiguration.TOKEN_PREFIX;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.repository.UserRepository;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final UserRepository userRepository;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String header = request.getHeader(HEADER_STRING);
		if ((header != null && header.startsWith(TOKEN_PREFIX)) || (request.getParameter("access_token") != null)) {
			UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		chain.doFilter(request, response);
	}

	// Reads the JWT from the Authorization header, and then uses JWT to validate
	// the token
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

		try {
			String accessToken;

			String header = request.getHeader(HEADER_STRING);
			if (header != null && header.startsWith(TOKEN_PREFIX))
				accessToken = header.replace(TOKEN_PREFIX, "");
			else
				accessToken = request.getParameter("access_token");

			if (accessToken != null && !accessToken.isBlank()) {

				String userUuid = JWT.require(Algorithm.HMAC512(System.getenv("SPRING_JWT_SECRET").getBytes())).build()
						.verify(accessToken).getSubject();

				if (userUuid != null) {
					UserEntity user = this.userRepository.findById(UUID.fromString(userUuid)).orElse(null);
					if (user != null)
						return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				}

			}
			return null;
		} catch (TokenExpiredException exception) {

			// o token expirou :/
		}

		return null;
	}
}