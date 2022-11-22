package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class IFoodOrderItemOption {

	private int index;
	private String id;
	private String name;
	private String unit;
	private int quantity;
	private double unitPrice;
	private double addition;
	private double price;
	
}
/*

"options": [
    {
        "index": 0,
        "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "name": "Opcional Teste",
        "unit": "UN",
        "quantity": 1,
        "unitPrice": 1.00,
        "addition": 0.00,
        "price": 1.00
    }
],
*/