package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class IFoodOrderCustomer {

	private String id;
	private String name;
	private IFoodOrderCustomerPhone phone;
	private String documentNumber;
	private int ordersCountOnMerchant;
	private String segmentation;

}
/*
 * "customer": { "id": "d1d159ad-895b-4237-bf92-ad172f3bd9b9", "name":
 * "Customer Name", "phone": IFoodOrderCustomerPhone, "ordersCountOnMerchant": 0
 * },
 */