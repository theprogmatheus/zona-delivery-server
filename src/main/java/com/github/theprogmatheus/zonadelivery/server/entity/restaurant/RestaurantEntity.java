package com.github.theprogmatheus.zonadelivery.server.entity.restaurant;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;

import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "restaurants")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@Column(nullable = false, unique = true, columnDefinition = "VARCHAR(128)")
	private String nameId;

	@Column(nullable = false, columnDefinition = "VARCHAR(128)")
	private String displayName;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private UserEntity owner;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "restaurant")
	private Set<RestaurantMenuEntity> menus;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "restaurant")
	private Set<RestaurantIFoodMerchantEntity> iFoodMerchants;

}
