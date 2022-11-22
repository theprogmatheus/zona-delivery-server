package com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "restaurant_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@Column(columnDefinition = "VARCHAR(8)", nullable = false)
	private String simpleId;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private RestaurantEntity restaurant;

	@Column(columnDefinition = "VARCHAR(128)", nullable = false)
	private String channel;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private RestaurantCustomerEntity customer;

	@ManyToOne
	@JoinColumn(name = "address_id")
	private RestaurantCustomerAddressEntity address;

	@ElementCollection
	@CollectionTable(name = "restaurant_order_items", joinColumns = @JoinColumn(name = "order_id"))
	private Set<RestaurantOrderItemEntity> items;

}
