package com.github.theprogmatheus.zonadelivery.server.dto;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;

import lombok.Data;

@Data
public class UserDTO {

	private long id;
	private UUID userId;
	private String username;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;

	public UserDTO(UserEntity userEntity) {
		this.id = userEntity.getId();
		this.userId = userEntity.getUserId();
		this.username = userEntity.getUsername();
		this.email = userEntity.getEmail();
		this.authorities = userEntity.getAuthorities();

	}

}
