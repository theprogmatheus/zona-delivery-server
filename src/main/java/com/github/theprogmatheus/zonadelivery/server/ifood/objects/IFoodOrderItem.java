package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class IFoodOrderItem {
	
	private int index;
	private String id;
	private String uniqueId;
	private String name;
	private String unit;
	private int quantity;
	private double unitPrice;
	private double optionsPrice;
	private double totalPrice;
	private List<IFoodOrderItemOption> options;
	private double price;
	private String observations;
	
}
/*

{
    "index": 1,
    "id": "7b956744-38f6-418a-90fa-22676b53e6d1",
    "uniqueId": "4209baf0-ce31-4703-8718-b48c7e7ef478",
    "name": "Produto Teste",
    "unit": "UN",
    "quantity": 1,
    "unitPrice": 1.00,
    "optionsPrice": 1.00,
    "totalPrice": 2.00,
    "options": [...IFoodOrderItemOption],
    "price": 1.00
}
*/