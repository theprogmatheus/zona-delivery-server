package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderItem;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderPayment;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderPaymentMethod;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderTotal;
import com.github.theprogmatheus.zonadelivery.server.enums.OrderStatus;
import com.github.theprogmatheus.zonadelivery.server.ifood.IFoodAPI;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDetails;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderItem;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderItemOption;
import com.github.theprogmatheus.zonadelivery.server.repository.OrderRepository;
import com.github.theprogmatheus.zonadelivery.server.util.StringUtils;

@Service
public class OrderService {

	@Autowired
	private EventService eventService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestaurantCustomerService customerService;

	@Autowired
	private RestaurantService restaurantService;

	public List<RestaurantOrderEntity> listOrders(UUID restaurantId) {
		return this.orderRepository.findAll().stream()
				.filter(order -> order.getRestaurant().getId().equals(restaurantId)).collect(Collectors.toList());

	}

	public Object getOrder(UUID orderId) {
		if (orderId == null)
			return "The orderId is not valid";
		RestaurantOrderEntity order = getOrderById(orderId);
		if (order == null)
			return "Order not found";

		return order;
	}

	public RestaurantOrderEntity createNewOrderByIfoodOrder(IFoodOrderDetails ifoodOrder) {

		if (ifoodOrder != null) {

			RestaurantEntity restaurant = this.restaurantService
					.getRestaurantByIFoodMerchantId(ifoodOrder.getMerchant().getId());

			if (restaurant != null) {

				RestaurantCustomerEntity customer = this.customerService
						.getCustomeByIfoodCustomerId(ifoodOrder.getCustomer().getId());

				// caso o banco de dados não tenha este cliente ainda, vamos cadastra-lo...
				if (customer == null) {
					Object result = this.customerService.createNewCustomerByIFoodOrderCustomer(restaurant.getId(),
							ifoodOrder.getCustomer());

					if (result instanceof RestaurantCustomerEntity)
						customer = (RestaurantCustomerEntity) result;
				}

				if (customer == null)
					return null;

				// precisamos resgatar/registrar o endereço fornecido pelo ifood
				RestaurantCustomerAddressEntity address = null;
				Object result = this.customerService.addNewCustomerAddressByIFoodOrderDeliveryAddress(customer.getId(),
						ifoodOrder.getDelivery().getDeliveryAddress());

				if (result instanceof RestaurantCustomerAddressEntity)
					address = (RestaurantCustomerAddressEntity) result;

				if (address == null)
					return null;

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

				RestaurantOrderTotal total = new RestaurantOrderTotal(ifoodOrder.getTotal().getSubTotal(),
						ifoodOrder.getTotal().getDeliveryFee(), ifoodOrder.getTotal().getBenefits(),
						ifoodOrder.getTotal().getOrderAmount(), ifoodOrder.getTotal().getAdditionalFees());

				RestaurantOrderPayment payment = new RestaurantOrderPayment(ifoodOrder.getPayments().getPrepaid(),
						ifoodOrder.getPayments().getPending(),
						ifoodOrder.getPayments().getMethods().stream().map(paymentPethod -> {

							return new RestaurantOrderPaymentMethod(paymentPethod.getValue(),
									paymentPethod.getCurrency(), paymentPethod.getMethod(), paymentPethod.getType(),
									paymentPethod.isPrepaid(), paymentPethod.getCash(), paymentPethod.getCard());
						}).collect(Collectors.toList()));

				result = placeOrder(restaurant.getId(), "IFOOD", ifoodOrder.getId(), ifoodOrder.getDisplayId(), null,
						ifoodOrder.getOrderType(), customer.getId(), address.getId(), items, total, payment);

				if (result instanceof RestaurantOrderEntity)
					return (RestaurantOrderEntity) result;
			}
		}
		return null;
	}

	// fazer um novo pedido
	public Object placeOrder(UUID restaurantId, String channel, String ifoodId, String simpleId, Date deliveryDateTime,
			String orderType, UUID customerId, UUID addressId, List<RestaurantOrderItem> items,
			RestaurantOrderTotal total, RestaurantOrderPayment payment) {

		if (restaurantId == null)
			return "The restaurantId is not valid";

		if (channel == null || channel.isEmpty())
			return "The channelId is not valid";

		if (simpleId == null || simpleId.isEmpty())
			simpleId = generateOrderSimpleId();

		if (deliveryDateTime == null)
			deliveryDateTime = new Date(System.currentTimeMillis() + 2_700_000L); // 45 minutos

		if (orderType == null || orderType.isEmpty())
			return "The orderType is not valid";

		if (customerId == null)
			return "The customerId is not valid";

		if (addressId == null)
			return "The addressId is not valid";

		if (items == null || items.isEmpty())
			return "The items is not valid";

		if (total == null)
			return "The total is not valid";

		if (payment == null)
			return "The payment is not valid";

		RestaurantEntity restaurant = this.restaurantService.getRestaurantById(restaurantId);
		if (restaurant == null)
			return "Restaurant is not valid";

		RestaurantCustomerEntity customer = this.customerService.getCustomerById(customerId);
		if (customer == null)
			return "Customer is not valid";

		RestaurantCustomerAddressEntity address = this.customerService.getAddressById(addressId);
		if (address == null)
			return "Address is not valid";

		IFoodOrderDetails iFoodOrderDetails = null;
		if (ifoodId != null)
			iFoodOrderDetails = IFoodAPI.getOrderDetails(ifoodId);

		RestaurantOrderEntity restaurantOrderEntity = this.orderRepository.saveAndFlush(
				new RestaurantOrderEntity(null, restaurant, new Date(), simpleId, deliveryDateTime, orderType, channel,
						customer, address, items, total, payment, OrderStatus.PLACED, iFoodOrderDetails));

		if (restaurantOrderEntity != null)
			this.eventService.createNewEvent(restaurantOrderEntity, OrderStatus.PLACED, null);

		return restaurantOrderEntity;
	}

	public Object acceptOrder(UUID orderId) {

		if (orderId == null)
			return "The orderId is not valid";
		RestaurantOrderEntity order = getOrderById(orderId);
		if (order == null)
			return "Order not found";

		if (order.getChannel().equals("IFOOD") && order.getIfoodOrder() != null)
			IFoodAPI.confirmOrder(order.getIfoodOrder().getId());

		return changeOrderStatus(orderId, OrderStatus.CONFIRMED);
	}

	public Object cancelOrder(UUID orderId) {
		return changeOrderStatus(orderId, OrderStatus.CANCELLED);
	}

	public Object dispatchOrder(UUID orderId) {

		if (orderId == null)
			return "The orderId is not valid";
		RestaurantOrderEntity order = getOrderById(orderId);
		if (order == null)
			return "Order not found";

		if (order.getChannel().equals("IFOOD") && order.getIfoodOrder() != null)
			IFoodAPI.dispatchOrder(order.getIfoodOrder().getId());

		return changeOrderStatus(orderId, OrderStatus.DISPATCHED);
	}

	public Object readyToPickupOrder(UUID orderId) {
		return changeOrderStatus(orderId, OrderStatus.READY_TO_PICKUP);
	}

	public Object finishOrder(UUID orderId) {
		return changeOrderStatus(orderId, OrderStatus.CONCLUDED);
	}

	public Object changeOrderStatus(UUID orderId, OrderStatus status) {
		return changeOrderStatus(orderId, status, null);
	}

	public Object changeOrderStatus(UUID orderId, OrderStatus status, Map<String, Object> metadata) {

		if (orderId == null)
			return "The orderId is not valid";
		if (status == null)
			return "The status is not valid";

		RestaurantOrderEntity order = getOrderById(orderId);
		if (order == null)
			return "Order not found";

		// emit event
		Object result = this.eventService.createNewEvent(order, status, metadata);
		if (result instanceof String)
			return result;

		order.setStatus(status);

		return this.orderRepository.saveAndFlush(order);
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

	public RestaurantOrderEntity getOrderById(UUID orderId) {
		if (orderId == null)
			return null;
		return this.orderRepository.findById(orderId).orElse(null);
	}

	public RestaurantOrderEntity getOrderByIFoodId(String ifoodId) {
		return this.orderRepository.findAll().stream()
				.filter(order -> order.getIfoodOrder() != null && order.getIfoodOrder().getId().equals(ifoodId))
				.findFirst().orElse(null);
	}
}
