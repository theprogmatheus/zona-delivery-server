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

@Entity(name = "restaurant_menu_categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "restaurant_menu_categories_items", joinColumns = {
			@JoinColumn(name = "category_id") }, inverseJoinColumns = { @JoinColumn(name = "item_id") })
	private Set<RestaurantMenuItemEntity> items;
	
	@Column
	private boolean paused;

}
