package com.github.theprogmatheus.zonadelivery.server.entity;

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

import lombok.Getter;
import lombok.Setter;

@Entity(name = "restaurant_menu_categories")
@Getter
@Setter
public class RestaurantMenuCategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "menu_id")
	private RestaurantMenuEntity menu;

	@Column(nullable = false, columnDefinition = "VARCHAR(128)")
	private String name;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
	private Set<RestaurantMenuItemEntity> items;

}
