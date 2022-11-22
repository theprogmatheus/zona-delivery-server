package com.github.theprogmatheus.zonadelivery.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, UUID> {

	public RestaurantEntity findByNameId(String nameId);

}
