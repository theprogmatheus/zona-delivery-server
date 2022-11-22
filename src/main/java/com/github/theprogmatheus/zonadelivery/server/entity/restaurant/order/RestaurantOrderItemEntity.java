package com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOrderItemEntity {

	private String productName;
	private double productPrice;
	private int amount;

	// @ElementCollection
	// private List<RestaurantOrderItemEntity> aditionals;
}
