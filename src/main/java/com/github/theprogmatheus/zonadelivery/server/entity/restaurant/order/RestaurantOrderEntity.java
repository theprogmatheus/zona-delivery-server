package com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import com.github.theprogmatheus.zonadelivery.server.enums.OrderStatus;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDetails;
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

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private RestaurantEntity restaurant;

	@Column(nullable = false)
	private Date createdAt;

	@Column(columnDefinition = "VARCHAR(8)", nullable = false)
	private String simpleId;

	private Date deliveryDateTime;

	@Column(columnDefinition = "VARCHAR(128)", nullable = false)
	private String orderType;

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

	@Embedded
	private RestaurantOrderTotal total;

	@Embedded
	private RestaurantOrderPayment payment;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	@Type(type = "json")
	@Column(columnDefinition = "json")
	private IFoodOrderDetails ifoodOrder;

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

	@JsonInclude(Include.NON_NULL)
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Embeddable
	public static class RestaurantOrderTotal {

		@Column(name = "total_sub_total")
		private double subTotal;

		@Column(name = "total_delivery_fee")
		private double deliveryFee;

		@Column(name = "total_benefits")
		private double benefits;

		@Column(name = "total_order_ammount")
		private double orderAmount;

		@Column(name = "total_aditional_fees")
		private double additionalFees;

	}

	@JsonInclude(Include.NON_NULL)
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Embeddable
	public static class RestaurantOrderPayment {

		@Column(name = "payment_prepaid")
		private double prepaid;

		@Column(name = "payment_pending")
		private double pending;

		@Type(type = "json")
		@Column(name = "payment_methods", columnDefinition = "json")
		private List<RestaurantOrderPaymentMethod> methods;

	}

	@JsonInclude(Include.NON_NULL)
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RestaurantOrderPaymentMethod {

		private double value;
		private String currency;
		private String method;
		private String type;
		private boolean prepaid;
		private Map<String, ?> cash;
		private Map<String, ?> card;

	}
}
