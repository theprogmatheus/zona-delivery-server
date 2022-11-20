package com.github.theprogmatheus.zonadelivery.server.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	
	@Type(type = "uuid-char")
	@Column(unique = true, nullable = false, columnDefinition = "VARCHAR(36)")
	private UUID userId;

	@Column(unique = true, nullable = false, columnDefinition = "VARCHAR(128)")
	private String username;

	@Column(unique = true, columnDefinition = "VARCHAR(128)")
	private String email;

	@Column(nullable = false, columnDefinition = "VARCHAR(256)")
	private String password;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
