package com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu;

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

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "restaurant_menus")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantMenuEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private RestaurantEntity restaurant;

	@Column(nullable = false, columnDefinition = "VARCHAR(128)")
	private String name;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "menu", orphanRemoval = true)
	private Set<RestaurantMenuCategoryEntity> categories;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "menu", orphanRemoval = true)
	private Set<RestaurantMenuItemEntity> items;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "menu", orphanRemoval = true)
	private Set<RestaurantMenuAditionalEntity> aditionals;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "menu", orphanRemoval = true)
	private Set<RestaurantMenuOptionalEntity> optionals;
}
