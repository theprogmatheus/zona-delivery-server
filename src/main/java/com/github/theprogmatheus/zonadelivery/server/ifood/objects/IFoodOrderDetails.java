package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class IFoodOrderDetails {

	private String id;
	private IFoodOrderDelivery delivery;
	private String orderType;
	private String orderTiming;
	private String displayId;
	private String createdAt;
	private String preparationStartDateTime;
	private boolean test;
	private IFoodOrderMerchant merchant;
	private IFoodOrderCustomer customer;
	private List<IFoodOrderItem> items;
	private String salesChannel;
	private IFoodOrderTotal total;
	private IFoodOrderPayment payments;
	private Map<String, ?> additionalInfo;
	private List<Map<?, ?>> benefits;
	private List<Map<?, ?>> additionalFees;
	private String extraInfo;
	private Map<?, ?> picking;
	private Map<?, ?> takeout;
	private Map<?, ?> indoor;
	private Map<?, ?> scheduled;

}
/*
 * { "id": "ab247097-59a6-4c82-a3e2-23797c6cbc00", "delivery":
 * IFoodOrderDelivery, "orderType": "DELIVERY", "orderTiming": "IMMEDIATE",
 * "displayId": "1792", "createdAt": "2022-08-13T15:01:05.170Z",
 * "preparationStartDateTime": "2022-08-13T15:01:05.170Z", "isTest": true,
 * "merchant": IFoodOrderMerchant, "customer": IFoodOrderCustomer, "items":
 * [...IFoodOrderItem], "salesChannel": "POS", "total": IFoodOrderTotal,
 * "payments": IFoodOrderPayment, "additionalInfo": { "metadata": {
 * "additionalProp1": "string" } } }
 */