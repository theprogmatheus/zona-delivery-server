package com.github.theprogmatheus.zonadelivery.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.github.theprogmatheus.zonadelivery.server.repository.UserRepository;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

	public static final long EXPIRATION_TIME = 43_200_000; // 12 horas
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String TOKEN_TYPE = "bearer";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/auth/login";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// dizemos que queremos usar as configurações dos CORS
		http.cors()

				.and()

				// libera todo o acesso ao /auth/login
				.authorizeRequests().antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()

				// websocket
				//.antMatchers("/ws/**").permitAll()

				.antMatchers("/dashboard/**").hasAuthority("*")

				// qualquer outro acesso tem que estar autenticado
				.anyRequest().authenticated()

				.and()

				// adiciona os filtros que nós criamos
				.addFilter(new JWTAuthenticationFilter(this.authenticationManager))
				.addFilter(new JWTAuthorizationFilter(this.authenticationManager, this.userRepository))

				// nós não precisamos criar sessões
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				// desabilitamos o csrf
				.and().csrf().disable();

		return http.build();
	}

}