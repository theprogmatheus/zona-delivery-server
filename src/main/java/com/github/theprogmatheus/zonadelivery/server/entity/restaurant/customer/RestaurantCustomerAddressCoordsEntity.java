package com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "restaurant_customer_address_coords")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantCustomerAddressCoordsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@OneToOne
	@JoinColumn(name = "address_id")
	private RestaurantCustomerAddressEntity address;

	private double longitude;

	private double latitude;
}
