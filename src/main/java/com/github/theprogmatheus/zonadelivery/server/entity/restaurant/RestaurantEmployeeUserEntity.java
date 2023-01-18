package com.github.theprogmatheus.zonadelivery.server.entity.restaurant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.theprogmatheus.zonadelivery.server.enums.RoleAuthority;
import com.github.theprogmatheus.zonadelivery.server.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "restaurant_employees")
public class RestaurantEmployeeUserEntity implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private RestaurantEntity restaurant;

	@Column(nullable = false, columnDefinition = "VARCHAR(128)")
	private String username;

	@Column(columnDefinition = "VARCHAR(128)")
	private String displayName;

	@Column(nullable = false, columnDefinition = "VARCHAR(256)")
	private String password;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		UserRole role = UserRole.EMPLOYEE;

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
		for (RoleAuthority roleAuthority : role.getAuthorities())
			authorities.add(new SimpleGrantedAuthority(roleAuthority.name()));

		return authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
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
