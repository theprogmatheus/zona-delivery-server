package com.github.theprogmatheus.zonadelivery.server.entity;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.EventType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private RestaurantOrderEntity order;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EventType type;

	@Column(nullable = false)
	private Date createdAt;

	@Type(type = "json")
	@Column(columnDefinition = "json")
	private Map<String, Object> metadata;

}
