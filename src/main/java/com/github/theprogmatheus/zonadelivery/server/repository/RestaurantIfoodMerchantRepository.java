package com.github.theprogmatheus.zonadelivery.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantIfoodMerchantEntity;

@Repository
public interface RestaurantIfoodMerchantRepository extends JpaRepository<RestaurantIfoodMerchantEntity, UUID> {

	public RestaurantIfoodMerchantEntity findByMerchantId(String merchantId);
}
