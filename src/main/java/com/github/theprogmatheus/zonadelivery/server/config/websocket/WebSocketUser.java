package com.github.theprogmatheus.zonadelivery.server.config.websocket;

import java.nio.file.attribute.UserPrincipal;

import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketUser implements UserPrincipal {

	private String name;
	private UserEntity userEntity;

}