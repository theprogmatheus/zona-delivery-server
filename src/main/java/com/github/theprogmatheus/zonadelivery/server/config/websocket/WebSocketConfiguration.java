package com.github.theprogmatheus.zonadelivery.server.config.websocket;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").setHandshakeHandler(new UserHandshakeHandler()).setAllowedOrigins("*").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableSimpleBroker("/queue", "/topic", "/user/");
		registry.setUserDestinationPrefix("/user");
	}

	private class UserHandshakeHandler extends DefaultHandshakeHandler {

		@Override
		protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
				Map<String, Object> attributes) {

			if (request.getPrincipal() != null
					&& (request.getPrincipal() instanceof UsernamePasswordAuthenticationToken)) {

				UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) request
						.getPrincipal();
				if (authenticationToken.getPrincipal() != null
						&& (authenticationToken.getPrincipal() instanceof UserEntity)) {

					UserEntity userEntity = (UserEntity) authenticationToken.getPrincipal();
					return new WebSocketUser(userEntity.getId().toString(), userEntity);
				}
			}
			return null;
		}

	}

}