package com.github.theprogmatheus.zonadelivery.server.entity.restaurant;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "restaurant_ifood_merchant_entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantIFoodMerchantEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", unique = true, nullable = false)
	@Type(type = "uuid-char")
	private UUID id;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private RestaurantEntity restaurant;

	@Column(columnDefinition = "VARCHAR(128)", nullable = false)
	private String name;

	@Column(columnDefinition = "VARCHAR(128)", nullable = false)
	private String merchantId;

}
