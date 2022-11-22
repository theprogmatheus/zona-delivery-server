package com.github.theprogmatheus.zonadelivery.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerEntity;

@Repository
public interface RestaurantCustomerRepository extends JpaRepository<RestaurantCustomerEntity, UUID> {

	public RestaurantCustomerEntity findByWhatsappCustomerId(String whatsappCustomerId);

	public RestaurantCustomerEntity findByIfoodCustomerId(String ifoodCustomerId);

}
