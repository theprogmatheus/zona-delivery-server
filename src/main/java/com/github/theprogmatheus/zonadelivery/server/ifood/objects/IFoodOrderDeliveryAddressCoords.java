package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class IFoodOrderDeliveryAddressCoords {

	private double latitude;
	private double longitude;
	
}
/*

"coordinates": {
    "latitude": -9.822159,
    "longitude": -67.948475
}
*/