package com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "restaurant_menu_item_aditionals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantMenuAditionalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "menu_id")
	private RestaurantMenuEntity menu;

	@ManyToMany(mappedBy = "aditionals")
	private Set<RestaurantMenuItemEntity> items;

	@Column(nullable = false, columnDefinition = "VARCHAR(128)")
	private String name;

	@Column(nullable = false)
	private double price;
	
	@Column(nullable = false)
	private int minAmount; // se o minAmount não for definido, então minAmount é 1
	
	@Column(nullable = false)
	private int maxAmount; // se o maxAmount não for definido, então maxAmount é infinito

}
