package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class IFoodOrderPaymentMethod {

	private double value;
	private String currency;
	private String method;
	private String type;
	private Map<String, ? > card;
	private Map<String, ? > cash;
	private Map<String, ? > wallet;
	private boolean prepaid;
	
}
/*

"methods": [
    {
        "value": 2.00,
        "currency": "BRL",
        "method": "EXTERNAL",
        "type": "ONLINE",
        "card": {
            "brand": "EXTERNAL"
        },
        "prepaid": true
    }
]
*/


/*

"methods": [
{
    "value": 16.00,
    "currency": "BRL",
    "method": "CASH",
    "type": "OFFLINE",
    "cash": {
        "changeFor": 200.00
    },
    "prepaid": false
}
]
*/