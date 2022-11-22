package com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "restaurant_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class RestaurantOrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;
	
	@Column(nullable = false)
	private Date createdAt;

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

	@Type(type = "json")
	@Column(columnDefinition = "json")
	private List<RestaurantOrderItem> items;

	@JsonInclude(Include.NON_NULL)
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RestaurantOrderItem {

		private String productName;
		private double productPrice;
		private int amount;
		private List<RestaurantOrderItem> aditionals;

	}

}
