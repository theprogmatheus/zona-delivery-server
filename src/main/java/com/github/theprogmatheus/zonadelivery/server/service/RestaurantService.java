package com.github.theprogmatheus.zonadelivery.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantRepository;
import com.github.theprogmatheus.zonadelivery.server.util.Utils;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository repository;

	public RestaurantEntity createRestaurant(UserEntity owner, String name) {

		if (owner != null && (name != null && !name.isEmpty()))
			return this.repository
					.saveAndFlush(new RestaurantEntity(null, Utils.SLUGIFY.slugify(name), name, owner, null));

		return null;
	}

	public RestaurantRepository getRepository() {
		return repository;
	}

}
