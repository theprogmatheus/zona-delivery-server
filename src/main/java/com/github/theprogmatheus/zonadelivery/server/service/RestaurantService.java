package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.UserEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantIFoodMerchantEntity;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantIfoodMerchantRepository;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantRepository;
import com.github.theprogmatheus.zonadelivery.server.util.Utils;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private RestaurantIfoodMerchantRepository restaurantIFoodMerchantRepository;

	public RestaurantEntity getRestaurantByIFoodMerchantId(String iFoodMerchantId) {

		RestaurantIFoodMerchantEntity restaurantIFoodMerchantEntity = this.restaurantIFoodMerchantRepository
				.findByMerchantId(iFoodMerchantId);

		if (restaurantIFoodMerchantEntity != null)
			return restaurantIFoodMerchantEntity.getRestaurant();

		return null;
	}

	public Object createNewRestaurant(UserEntity owner, String name) {

		if (owner == null)
			return "The 'owner' field can't be null.";

		if (name == null || name.isEmpty())
			return "The 'name' are required field in your request body, do not leave it null";

		String nameId = Utils.SLUGIFY.slugify(name);

		while (getRestaurantByNameId(nameId) != null)
			nameId = createNameIdByRestaurantName(name);

		return this.restaurantRepository.saveAndFlush(new RestaurantEntity(null, nameId, name, owner, null, null));
	}

	public Object changeRestaurantNameId(RestaurantEntity restaurant, String newNameId) {

		if (restaurant == null)
			return "You need to enter a valid restaurant";

		if (newNameId == null || newNameId.isEmpty())
			return "You need to enter a valid nameId";

		newNameId = Utils.SLUGIFY.slugify(newNameId);

		if (getRestaurantByNameId(newNameId) != null)
			return "This nameId: '" + newNameId + "', is already used, choose another nameId";

		restaurant.setNameId(newNameId);

		return this.restaurantRepository.saveAndFlush(restaurant);
	}

	public Object changeRestaurantDisplayName(RestaurantEntity restaurant, String newDisplayName) {

		if (restaurant == null)
			return "You need to enter a valid restaurant";

		if (newDisplayName == null || newDisplayName.isEmpty())
			return "You need to enter a valid displayName";

		restaurant.setDisplayName(newDisplayName);

		return this.restaurantRepository.saveAndFlush(restaurant);
	}

	public Object changeRestaurantOwner(RestaurantEntity restaurant, UserEntity newOwner) {

		if (restaurant == null)
			return "You need to enter a valid restaurant";

		if (newOwner == null)
			return "You need to enter a valid ownerId";

		restaurant.setOwner(newOwner);

		return this.restaurantRepository.saveAndFlush(restaurant);
	}

	public String createNameIdByRestaurantName(String name) {
		return Utils.SLUGIFY.slugify(name).concat("-").concat(UUID.randomUUID().toString().split("-")[0]);
	}

	public RestaurantEntity getRestaurantById(UUID id) {
		return this.restaurantRepository.findById(id).orElse(null);
	}

	public RestaurantEntity getRestaurantByNameId(String nameId) {
		return this.restaurantRepository.findByNameId(nameId);
	}

	public RestaurantRepository getRestaurantRepository() {
		return restaurantRepository;
	}

	public RestaurantIfoodMerchantRepository getRestaurantIfoodMerchantRepository() {
		return restaurantIFoodMerchantRepository;
	}

}
