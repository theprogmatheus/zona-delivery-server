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

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEmployeeUserEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.UserType;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantEmployeeUserRepository;
import com.github.theprogmatheus.zonadelivery.server.repository.UserRepository;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final UserRepository userRepository;
	private final RestaurantEmployeeUserRepository employeeUserRepository;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository,
			RestaurantEmployeeUserRepository employeeUserRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
		this.employeeUserRepository = employeeUserRepository;
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

				DecodedJWT token = SecurityConfiguration.readJWTToken(accessToken);

				Claim userTypeClaim = token.getClaim("userType");

				if (userTypeClaim != null && userTypeClaim.asString() != null) {
					UserType userType = UserType.valueOf(userTypeClaim.asString());
					UUID userId = UUID.fromString(token.getSubject());

					switch (userType) {

					case USER:

						UserEntity user = this.userRepository.findById(userId).orElse(null);
						if (user != null)
							return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

						break;

					case EMPLOYEE:

						RestaurantEmployeeUserEntity employee = this.employeeUserRepository.findById(userId)
								.orElse(null);
						if (employee != null)
							return new UsernamePasswordAuthenticationToken(employee, null, employee.getAuthorities());

						break;

					default:
						break;
					}
				}
			}
			return null;
		} catch (TokenExpiredException exception) {
			// o token expirou :/

		} catch (SignatureVerificationException exception) {
			// token inválido
		} catch (Exception exception) {
			System.out.println("Erro ao tentar authenticar " + exception.getMessage());
			exception.printStackTrace();
		}

		return null;
	}
}