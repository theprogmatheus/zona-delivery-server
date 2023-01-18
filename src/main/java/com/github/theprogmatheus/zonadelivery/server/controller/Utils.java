package com.github.theprogmatheus.zonadelivery.server.controller;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.github.theprogmatheus.zonadelivery.server.enums.UserRole;

@Secured(UserRole.USER_ROLE_NAME)
@RestController
@RequestMapping("/utils")
public class Utils {

	public static final String GEO_API_URL = "https://api.opencagedata.com/geocode/v1/json?key=68b877e5d30744288d928bd10d04d80a&query=";
	public static final String GMAPS_DISTANCE_API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins={origins}&destinations={destinations}&units=metric&key=AIzaSyClz93HtBfipKebP6o4KFqPgzsoaQi0R7w";
	public static final RestTemplate rest = new RestTemplate();

	@GetMapping("/geodata")
	public Object getGeoData(@PathParam("query") String query) {
		Object body = "";
		try {
			body = rest.exchange(GEO_API_URL.concat(query != null ? query : ""), HttpMethod.GET, null, Object.class)
					.getBody();
		} catch (Exception exception) {
			System.out.println(
					"Ocorreu um erro ao tentar resgatar as informações de um endereço via (api.opencagedata.com): "
							+ exception.getMessage());
		}
		return body;
	}

	/// utils/gmaps/distance/?origins={0}&destinations={1}

	@GetMapping("/gmaps/distance")
	public Object getGmapsDistance(@PathParam("origins") String origins,
			@PathParam("destinations") String destinations) {
		Object body = "";
		try {
			String url = GMAPS_DISTANCE_API_URL.replace("{origins}", origins).replace("{destinations}", destinations);
			body = rest.exchange(url, HttpMethod.GET, null, Object.class).getBody();
		} catch (Exception exception) {
			System.out.println(
					"Ocorreu um erro ao tentar recuperar a distancia entre endereços via (maps.googleapis.com/maps/api/distancematrix): "
							+ exception.getMessage());
		}
		return body;
	}
}
