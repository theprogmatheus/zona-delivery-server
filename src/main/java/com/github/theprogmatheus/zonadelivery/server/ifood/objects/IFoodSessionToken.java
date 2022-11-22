package com.github.theprogmatheus.zonadelivery.server.ifood.objects;

import org.springframework.util.StringUtils;

import lombok.Data;

@Data
public class IFoodSessionToken {

	private String accessToken;
	private String type;
	private long expiresIn;

	public String toHeaderString() {
		return StringUtils.capitalize(this.type).concat(" ").concat(this.accessToken);
	}

}
