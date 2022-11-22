package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class IFoodOrderDelivery {
	
	private String mode;
	private String deliveredBy;
	private String deliveryDateTime;
	private IFoodOrderDeliveryAddress deliveryAddress;
	

}
/*
"delivery": {
"mode": "DEFAULT",
"deliveredBy": "IFOOD",
"deliveryDateTime": "2022-08-13T15:31:05.167Z",
"deliveryAddress": IFoodOrderDeliveryAddress
}
*/