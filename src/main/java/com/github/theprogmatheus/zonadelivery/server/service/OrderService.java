package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderItem;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDetails;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderItem;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderItemOption;
import com.github.theprogmatheus.zonadelivery.server.repository.OrderRepository;
import com.github.theprogmatheus.zonadelivery.server.util.StringUtils;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private RestaurantService restaurantService;

	public RestaurantOrderEntity createNewOrderByIfoodOrder(IFoodOrderDetails ifoodOrder) {

		if (ifoodOrder != null) {

			RestaurantEntity restaurant = this.restaurantService
					.getRestaurantByIFoodMerchantId(ifoodOrder.getMerchant().getId());

			if (restaurant != null) {

				RestaurantCustomerEntity customer = this.customerService
						.getCustomeByIfoodCustomerId(ifoodOrder.getCustomer().getId());

				// caso o banco de dados não tenha este cliente ainda, vamos cadastra-lo...
				if (customer == null)
					customer = this.customerService.createNewCustomerByIFoodOrderCustomer(ifoodOrder.getCustomer());

				// precisamos resgatar/registrar o endereço fornecido pelo ifood
				RestaurantCustomerAddressEntity address = this.customerService
						.addNewCustomerAddressByIFoodOrderDeliveryAddress(customer,
								ifoodOrder.getDelivery().getDeliveryAddress());

				// vamos listar os itens do pedido
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

				return createNewOrder(restaurant, "IFOOD", ifoodOrder.getDisplayId(), customer, address, items);
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
