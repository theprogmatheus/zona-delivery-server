package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantRepository;
import com.github.theprogmatheus.zonadelivery.server.util.Utils;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository repository;

	public Object createNewRestaurant(UserEntity owner, String name) {

		if (owner == null)
			return "The 'owner' field can't be null.";

		if (name == null || name.isEmpty())
			return "The 'name' are required field in your request body, do not leave it null";

		String nameId = Utils.SLUGIFY.slugify(name);

		while (getRestaurantByNameId(nameId) != null)
			nameId = createNameIdByRestaurantName(name);

		return this.repository.saveAndFlush(new RestaurantEntity(null, nameId, name, owner, null, null));
	}

	public String createNameIdByRestaurantName(String name) {
		return Utils.SLUGIFY.slugify(name).concat("-").concat(UUID.randomUUID().toString().split("-")[0]);
	}

	public RestaurantEntity getRestaurantById(UUID id) {
		return this.repository.findById(id).orElse(null);
	}

	public RestaurantEntity getRestaurantByNameId(String nameId) {
		return this.repository.findByNameId(nameId);
	}

	public RestaurantRepository getRepository() {
		return repository;
	}

}
