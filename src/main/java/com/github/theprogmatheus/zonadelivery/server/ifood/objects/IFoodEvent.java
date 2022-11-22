package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class IFoodEvent {

	private String id;
	private String code;
	private String fullCode;
	private String orderId;
	private String merchantId;
	private String createdAt;
	private Map<String, ?> metadata;
	
}

/*
{
    "id": "d427af11-4dcc-4b48-b118-ffbf8846347d",
    "code": "CFM",
    "fullCode": "CONFIRMED",
    "orderId": "ab247097-59a6-4c82-a3e2-23797c6cbc00",
    "merchantId": "955ba810-8aaf-4dc5-a218-841198bf41d4",
    "createdAt": "2022-08-13T15:01:42.104Z",
    "metadata": {
        "ORIGIN": "ORDER_API",
        "ownerName": "theprogmatheus",
        "CLIENT_ID": "theprogmatheus:theprogmatheus-teste-c",
        "appName": "theprogmatheus-teste-c"
    }
}
*/