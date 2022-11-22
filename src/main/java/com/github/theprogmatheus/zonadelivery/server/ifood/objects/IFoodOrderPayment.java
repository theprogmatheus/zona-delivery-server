package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class IFoodOrderPayment {
	
	private double prepaid;
	private double pending;
	private List<IFoodOrderPaymentMethod> methods;
	
}
/*
"payments": {
    "prepaid": 2.00,
    "pending": 0,
    "methods": [...IFoodOrderPaymentMethod]
},

*/