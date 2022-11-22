package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class IFoodOrderHandshakeCode {

	private String orderId;
	private String handshakeCode;
}
