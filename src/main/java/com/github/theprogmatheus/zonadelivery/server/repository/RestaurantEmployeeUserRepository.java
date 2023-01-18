package com.github.theprogmatheus.zonadelivery.server.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEmployeeUserEntity;

@Repository
public interface RestaurantEmployeeUserRepository extends JpaRepository<RestaurantEmployeeUserEntity, UUID> {

	public Optional<RestaurantEmployeeUserEntity> findByUsername(String username);

}
