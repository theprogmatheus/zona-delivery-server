package com.github.theprogmatheus.zonadelivery.server.controller;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/utils")
public class Utils {

	public static final String GEO_API_URL = "https://api.opencagedata.com/geocode/v1/json?key=68b877e5d30744288d928bd10d04d80a&query=";
	public static final RestTemplate rest = new RestTemplate();

	@GetMapping("/geodata")
	public Object getGeoData(@PathParam("query") String query) {
		Object body = "";
		try {
			body = rest.exchange(GEO_API_URL.concat(query != null ? query : ""), HttpMethod.GET, null, Object.class)
					.getBody();
		} catch (Exception exception) {
		}
		return body;
	}

}
