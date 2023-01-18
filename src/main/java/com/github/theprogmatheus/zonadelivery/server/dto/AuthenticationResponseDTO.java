package com.github.theprogmatheus.zonadelivery.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDTO {

	private String type;
	private String accessToken;
	private long expireIn;
	private Object user;

}
