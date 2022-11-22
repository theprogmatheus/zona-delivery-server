package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class IFoodOrderCustomerPhone {
	
	private String number;
	private String localizer;
	private String localizerExpiration;	
	
}
/*

"phone": {
    "number": "11999999999",
    "localizer": "50736444",
    "localizerExpiration": "2022-08-13T18:01:05.170Z"
},
*/