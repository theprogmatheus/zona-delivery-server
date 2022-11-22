package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class IFoodHandshakeConfirmResponse {

	private String handshakeStatus;
	private String message;
}
