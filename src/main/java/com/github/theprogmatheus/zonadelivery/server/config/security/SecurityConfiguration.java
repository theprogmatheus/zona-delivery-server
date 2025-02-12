package com.github.theprogmatheus.zonadelivery.server.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.theprogmatheus.zonadelivery.server.enums.UserType;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantEmployeeUserRepository;
import com.github.theprogmatheus.zonadelivery.server.repository.UserRepository;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

	public static final long EXPIRATION_TIME = 604_800_000; // 7 dias
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String TOKEN_TYPE = "bearer";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/auth/login";
	public static final String EMPLOYEE_SIGN_UP_URL = "/restaurant/**/employee/login";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RestaurantEmployeeUserRepository employeeUserRepository;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// dizemos que queremos usar as configurações dos CORS
		http.cors()

				.and()

				// libera todo o acesso ao /auth/login
				.authorizeRequests().antMatchers(HttpMethod.POST, SIGN_UP_URL, EMPLOYEE_SIGN_UP_URL).permitAll()
				// websocket
				// .antMatchers("/ws/**").permitAll()

				.antMatchers("/dashboard/**").hasAuthority("*")

				// qualquer outro acesso tem que estar autenticado
				.anyRequest().authenticated()

				.and()

				// adiciona os filtros que nós criamos
				.addFilter(new JWTAuthenticationFilter(this.authenticationManager))
				.addFilter(new JWTAuthorizationFilter(this.authenticationManager, this.userRepository,
						this.employeeUserRepository))

				// nós não precisamos criar sessões
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				// desabilitamos o csrf
				.and().csrf().disable();

		return http.build();
	}

	public static String createJWTToken(UserType userType, String subject, long lifetime) {
		return JWT.create().withClaim("userType", userType.name()).withSubject(subject).withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + lifetime))
				.sign(Algorithm.HMAC512(System.getenv("SPRING_JWT_SECRET").getBytes()));
	}

	public static DecodedJWT readJWTToken(String token) {
		return JWT.require(Algorithm.HMAC512(System.getenv("SPRING_JWT_SECRET").getBytes())).build().verify(token);
	}

}