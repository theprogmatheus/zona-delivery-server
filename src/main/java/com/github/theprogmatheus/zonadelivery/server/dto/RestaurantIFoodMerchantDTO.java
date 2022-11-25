package com.github.theprogmatheus.zonadelivery.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantIFoodMerchantEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RestaurantIFoodMerchantDTO {

	private String merchantName;
	private String merchantId;

	public RestaurantIFoodMerchantDTO(RestaurantIFoodMerchantEntity restaurantIFoodMerchantEntity) {
		if (restaurantIFoodMerchantEntity != null) {

			if (restaurantIFoodMerchantEntity.getName() != null)
				this.merchantName = restaurantIFoodMerchantEntity.getName();

			if (restaurantIFoodMerchantEntity.getMerchantId() != null)
				this.merchantId = restaurantIFoodMerchantEntity.getMerchantId();

		}

	}

}
