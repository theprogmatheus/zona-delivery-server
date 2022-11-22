package com.github.theprogmatheus.zonadelivery.server.entity;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@Column(unique = true, nullable = false, columnDefinition = "VARCHAR(128)")
	private String username;

	@Column(unique = true, columnDefinition = "VARCHAR(128)")
	private String email;

	@Column(nullable = false, columnDefinition = "VARCHAR(256)")
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_authorities", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "authority_id") })
	private List<UserAuthorityEntity> authorities;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
	private Set<RestaurantEntity> restaurants;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
				.collect(Collectors.toList());
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
