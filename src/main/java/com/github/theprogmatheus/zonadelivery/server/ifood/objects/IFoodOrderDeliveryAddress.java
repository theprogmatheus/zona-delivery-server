package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class IFoodOrderDeliveryAddress {
	
	private String streetName;
	private String streetNumber;
	private String formattedAddress;
	private String neighborhood;
	private String complement;
	private String postalCode;
	private String city;
	private String state;
	private String country;
	private String reference;
	private IFoodOrderDeliveryAddressCoords coordinates;

}
/*

"deliveryAddress": {
    "streetName": "Ramal Bujari",
    "streetNumber": "100",
    "formattedAddress": "Ramal Bujari, 100",
    "neighborhood": "Centro",
    "postalCode": "69923000",
    "city": "Bujari",
    "state": "AC",
    "country": "BR",
    "reference": "Perto da padaria",
    "coordinates": IFoodOrderDeliveryAddressCoords
}
*/