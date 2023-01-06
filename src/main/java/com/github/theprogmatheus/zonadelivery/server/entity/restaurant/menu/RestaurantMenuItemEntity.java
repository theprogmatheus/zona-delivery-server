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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "restaurant_menu_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantMenuItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "menu_id")
	private RestaurantMenuEntity menu;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "items")
	private Set<RestaurantMenuCategoryEntity> categories;

	@Column(nullable = false, columnDefinition = "VARCHAR(128)")
	private String name;

	@Column(columnDefinition = "VARCHAR(512)")
	private String description;

	@Column(columnDefinition = "BLOB")
	private String image;

	@Column(nullable = false)
	private double price;

	@Column
	private double oldPrice;

	@Column
	private boolean paused;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "restaurant_menu_items_optionals", joinColumns = {
			@JoinColumn(name = "item_id") }, inverseJoinColumns = { @JoinColumn(name = "optional_id") })
	private Set<RestaurantMenuOptionalEntity> optionals;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "restaurant_menu_items_aditionals", joinColumns = {
			@JoinColumn(name = "item_id") }, inverseJoinColumns = { @JoinColumn(name = "aditional_id") })
	private Set<RestaurantMenuAditionalEntity> aditionals;
}
