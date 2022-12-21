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

@Entity(name = "restaurant_menu_item_optionals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantMenuOptionalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "menu_id")
	private RestaurantMenuEntity menu;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "restaurant_menu_item_optionals_items", joinColumns = {
			@JoinColumn(name = "optional_id") }, inverseJoinColumns = { @JoinColumn(name = "item_id") })
	private Set<RestaurantMenuItemEntity> items;

	@Column(nullable = false, columnDefinition = "VARCHAR(128)")
	private String name;

	@Column(columnDefinition = "VARCHAR(512)")
	private String description;

	@Column
	private int min;

	@Column
	private int max;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "restaurant_menu_item_optionals_options", joinColumns = {
			@JoinColumn(name = "optional_id") }, inverseJoinColumns = { @JoinColumn(name = "aditional_id") })
	private Set<RestaurantMenuAditionalEntity> options;

}
