package com.github.theprogmatheus.zonadelivery.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantIFoodMerchantEntity;

@Repository
public interface RestaurantIfoodMerchantRepository extends JpaRepository<RestaurantIFoodMerchantEntity, UUID> {

	public RestaurantIFoodMerchantEntity findByMerchantId(String merchantId);
}
