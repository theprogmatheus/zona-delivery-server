package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantIfoodMerchantEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity.RestaurantCustomerAddressCoords;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderItem;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderCustomer;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDeliveryAddress;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDetails;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderItem;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderItemOption;
import com.github.theprogmatheus.zonadelivery.server.repository.OrderRepository;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantCustomerAddressRepository;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantCustomerRepository;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantIfoodMerchantRepository;
import com.github.theprogmatheus.zonadelivery.server.util.StringUtils;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestaurantIfoodMerchantRepository restaurantIfoodMerchantRepository;

	@Autowired
	private RestaurantCustomerRepository restaurantCustomerRepository;

	@Autowired
	private RestaurantCustomerAddressRepository restaurantCustomerAddressRepository;

	public RestaurantOrderEntity createNewOrderByIfoodOrder(IFoodOrderDetails ifoodOrder) {

		if (ifoodOrder != null) {

			RestaurantIfoodMerchantEntity restaurantIfoodMerchant = this.restaurantIfoodMerchantRepository
					.findByMerchantId(ifoodOrder.getMerchant().getId());

			if (restaurantIfoodMerchant != null) {

				IFoodOrderCustomer ifoodCustomer = ifoodOrder.getCustomer();

				RestaurantCustomerEntity customer = this.restaurantCustomerRepository
						.findByIfoodCustomerId(ifoodOrder.getCustomer().getId());

				if (customer == null)
					customer = this.restaurantCustomerRepository.saveAndFlush(new RestaurantCustomerEntity(null,
							ifoodCustomer.getName(), ifoodCustomer.getId(), null, null, new HashSet<>()));

				IFoodOrderDeliveryAddress ifoodAddress = ifoodOrder.getDelivery().getDeliveryAddress();

				RestaurantCustomerAddressEntity address = new RestaurantCustomerAddressEntity(null, customer,
						ifoodAddress.getStreetName(), ifoodAddress.getStreetNumber(), ifoodAddress.getNeighborhood(),
						ifoodAddress.getComplement(), ifoodAddress.getPostalCode(), ifoodAddress.getCity(),
						ifoodAddress.getState(), ifoodAddress.getCountry(), ifoodAddress.getReference(),
						new RestaurantCustomerAddressCoords(ifoodAddress.getCoordinates().getLongitude(),
								ifoodAddress.getCoordinates().getLatitude()));

				RestaurantCustomerAddressEntity selectedAddress = customer.getAddresses().stream()
						.filter(target -> target.equals(address)).findFirst().orElse(null);

				if (selectedAddress == null) {
					selectedAddress = this.restaurantCustomerAddressRepository.saveAndFlush(address);

					customer.getAddresses().add(selectedAddress);
					customer = this.restaurantCustomerRepository.saveAndFlush(customer);
				}

				List<RestaurantOrderItem> items = new ArrayList<>();

				for (IFoodOrderItem ifoodItem : ifoodOrder.getItems()) {
					List<RestaurantOrderItem> aditionals = new ArrayList<>();

					if (ifoodItem.getOptions() != null && !ifoodItem.getOptions().isEmpty()) {
						for (IFoodOrderItemOption ifoodItemOption : ifoodItem.getOptions()) {
							aditionals.add(new RestaurantOrderItem(ifoodItemOption.getName(),
									ifoodItemOption.getPrice(), ifoodItemOption.getQuantity(), null));
						}
					}

					items.add(new RestaurantOrderItem(ifoodItem.getName(), ifoodItem.getPrice(),
							ifoodItem.getQuantity(), aditionals));
				}

				return createNewOrder(restaurantIfoodMerchant.getRestaurant(), "IFOOD", ifoodOrder.getDisplayId(),
						customer, selectedAddress, items);
			}
		}
		return null;
	}

	public RestaurantOrderEntity createNewOrder(RestaurantEntity restaurant, String channel, String simpleId,
			RestaurantCustomerEntity customer, RestaurantCustomerAddressEntity address,
			List<RestaurantOrderItem> items) {
		return this.orderRepository.saveAndFlush(new RestaurantOrderEntity(null, new Date(),
				((simpleId != null && !simpleId.isEmpty()) ? simpleId : generateOrderSimpleId()), restaurant, channel,
				customer, address, items));
	}

	public String generateOrderSimpleId() {
		return StringUtils.generateRandomString("1234567890", 5);
	}

	public String generateSimpleId() {
		return null;
	}

	public OrderRepository getRepository() {
		return orderRepository;
	}

}
