package com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer;

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

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "restaurant_customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantCustomerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private RestaurantEntity restaurant;

	@Column(columnDefinition = "VARCHAR(128)", nullable = false)
	private String name;

	@Column(columnDefinition = "VARCHAR(128)", unique = true)
	private String ifoodCustomerId;

	@Column(columnDefinition = "VARCHAR(128)", unique = true)
	private String whatsappCustomerId;

	@Column(columnDefinition = "VARCHAR(128)")
	private String phone;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "customer")
	private Set<RestaurantCustomerAddressEntity> addresses;

}
