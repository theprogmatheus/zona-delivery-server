package com.github.theprogmatheus.zonadelivery.server.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "restaurant_menu_item_aditionals")
@Getter
@Setter
public class RestaurantMenuAditionalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private RestaurantMenuItemEntity item;

	@Column(nullable = false, columnDefinition = "VARCHAR(128)")
	private String name;

	@Column(nullable = false)
	private double price;

}
