package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class IFoodOrderTotal {
	
	private double subTotal;
	private double deliveryFee;
	private double benefits;
	private double orderAmount;
	private double additionalFees;	
	
}
/*
"total": {
"subTotal": 2.00,
"deliveryFee": 0.00,
"benefits": 0,
"orderAmount": 2.00,
"additionalFees": 0
},
*/