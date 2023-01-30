package com.github.theprogmatheus.zonadelivery.server.service;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantOrderDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderAddress;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderCustomer;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderItem;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderPayment;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderPaymentMethod;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.order.RestaurantOrderEntity.RestaurantOrderTotal;
import com.github.theprogmatheus.zonadelivery.server.enums.OrderStatus;
import com.github.theprogmatheus.zonadelivery.server.events.EventType;
import com.github.theprogmatheus.zonadelivery.server.ifood.IFoodAPI;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDeliveryAddress;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDetails;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderItem;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderItemOption;
import com.github.theprogmatheus.zonadelivery.server.repository.OrderRepository;
import com.github.theprogmatheus.zonadelivery.server.util.StringUtils;

@Service
public class OrderService {

	private static final long LAST_ORDER_TIME = 43_200_000;

	@Autowired
	private EventService eventService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestaurantService restaurantService;

	public List<RestaurantOrderEntity> listLastOrders(UUID restaurantId) {
		return this.orderRepository.findAll().stream()
				.filter(order -> order.getRestaurant().getId().equals(restaurantId)
						&& System.currentTimeMillis() < (order.getCreatedAt().getTime() + LAST_ORDER_TIME))
				.collect(Collectors.toList());

	}

	public List<RestaurantOrderEntity> listOrders(UUID restaurantId) {
		return this.orderRepository.findAll().stream()
				.filter(order -> order.getRestaurant().getId().equals(restaurantId)).collect(Collectors.toList());

	}

	public boolean searchMatch(RestaurantOrderDTO order, String search) {
		if (search == null || search.isBlank())
			return true;

		Object object = order;
		if (search.contains(":")) {
			try {
				String[] searchSplit = search.split(":");
				String fieldName = searchSplit[0];
				search = search.substring((search.indexOf(":") + 1), search.length());
				Field field = order.getClass().getDeclaredField(fieldName);
				if (field != null) {
					field.setAccessible(true);
					object = field.get(order);
				}
			} catch (Exception exception) {
				System.out.println("Erro ao tentar search: " + exception.getMessage());
				exception.printStackTrace();
			}
		}

		try {
			return new ObjectMapper().writeValueAsString(object).toLowerCase().contains(search.toLowerCase());
		} catch (Exception exception) {
			System.out.println("Erro ao tentar search: " + exception.getMessage());
			return false;
		}
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

				RestaurantOrderCustomer customer = new RestaurantOrderCustomer(ifoodOrder.getCustomer().getName(),
						ifoodOrder.getCustomer().getPhone().getNumber() + ";"
								+ ifoodOrder.getCustomer().getPhone().getLocalizer(),
						ifoodOrder.getCustomer().getId(), null);

				// build address
				IFoodOrderDeliveryAddress ifoodAddress = ifoodOrder.getDelivery().getDeliveryAddress();
				StringBuilder formattedAddress = new StringBuilder();

				if (ifoodAddress.getStreetName() != null)
					formattedAddress.append(ifoodAddress.getStreetName());

				if (ifoodAddress.getStreetNumber() != null)
					formattedAddress.append(", " + ifoodAddress.getStreetNumber());

				if (ifoodAddress.getNeighborhood() != null)
					formattedAddress.append(", " + ifoodAddress.getNeighborhood());

				if (ifoodAddress.getComplement() != null)
					formattedAddress.append(", " + ifoodAddress.getComplement());

				if (ifoodAddress.getPostalCode() != null)
					formattedAddress.append(", " + ifoodAddress.getPostalCode());

				if (ifoodAddress.getCity() != null)
					formattedAddress.append(", " + ifoodAddress.getCity());

				if (ifoodAddress.getState() != null)
					formattedAddress.append(", " + ifoodAddress.getState());

				if (ifoodAddress.getCountry() != null)
					formattedAddress.append(", " + ifoodAddress.getCountry());

				if (ifoodAddress.getReference() != null)
					formattedAddress.append(", " + ifoodAddress.getReference());

				RestaurantOrderAddress address = new RestaurantOrderAddress(formattedAddress.toString(),
						ifoodAddress.getCoordinates().getLatitude(), ifoodAddress.getCoordinates().getLongitude());

				// vamos listar os itens do pedido
				List<RestaurantOrderItem> items = new ArrayList<>();

				// NOTES
				String orderNote = null;

				for (IFoodOrderItem ifoodItem : ifoodOrder.getItems()) {
					List<RestaurantOrderItem> aditionals = new ArrayList<>();

					if (ifoodItem.getOptions() != null && !ifoodItem.getOptions().isEmpty()) {
						for (IFoodOrderItemOption ifoodItemOption : ifoodItem.getOptions()) {
							aditionals.add(new RestaurantOrderItem(ifoodItemOption.getName(),
									ifoodItemOption.getUnitPrice(), ifoodItemOption.getQuantity(), null, null));
						}
					}
					items.add(new RestaurantOrderItem(ifoodItem.getName(), ifoodItem.getUnitPrice(),
							ifoodItem.getQuantity(), aditionals, ifoodItem.getObservations()));
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

				Date deliveryDateTime = null;

				if (ifoodOrder.getDelivery() != null && ifoodOrder.getDelivery().getDeliveryDateTime() != null
						&& !ifoodOrder.getDelivery().getDeliveryDateTime().isBlank()) {
					deliveryDateTime = Date.from(Instant.parse(ifoodOrder.getDelivery().getDeliveryDateTime()));
				}

				Object result = placeOrder(restaurant.getId(), "IFOOD", ifoodOrder.getId(), ifoodOrder.getDisplayId(),
						deliveryDateTime, ifoodOrder.getOrderType(), customer, address, items, total, payment,
						orderNote);

				if (result instanceof RestaurantOrderEntity)
					return (RestaurantOrderEntity) result;
			}
		}
		return null;
	}

	// fazer um novo pedido
	public Object placeOrder(UUID restaurantId, String channel, String ifoodId, String simpleId, Date deliveryDateTime,
			String orderType, RestaurantOrderCustomer customer, RestaurantOrderAddress address,
			List<RestaurantOrderItem> items, RestaurantOrderTotal total, RestaurantOrderPayment payment,
			String observations) {

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

		if (customer == null || customer.getName() == null)
			return "The customerId is not valid";

		if (orderType.equals("DELIVERY") && address == null)
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

		IFoodOrderDetails iFoodOrderDetails = null;
		if (ifoodId != null)
			iFoodOrderDetails = IFoodAPI.getOrderDetails(ifoodId);

		RestaurantOrderEntity restaurantOrderEntity = this.orderRepository.saveAndFlush(
				new RestaurantOrderEntity(null, restaurant, new Date(), simpleId, deliveryDateTime, orderType, channel,
						customer, address, items, total, payment, OrderStatus.PLACED, iFoodOrderDetails, observations));

		if (restaurantOrderEntity != null)
			this.eventService.createNewEvent(restaurantOrderEntity, EventType.ORDER_UPDATE, restaurantOrderEntity);

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
		if (orderId == null)
			return "The orderId is not valid";
		RestaurantOrderEntity order = getOrderById(orderId);
		if (order == null)
			return "Order not found";

		if (order.getChannel().equals("IFOOD") && order.getIfoodOrder() != null)
			IFoodAPI.readyToPickupOrder(order.getIfoodOrder().getId());

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

		order.setStatus(status);
		order = this.orderRepository.saveAndFlush(order);

		if (order != null) {
			// emit event
			Object result = this.eventService.createNewEvent(order, EventType.ORDER_UPDATE, order);
			if (result instanceof String)
				return result;
		}

		return order;
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

	public void finishOldOrders() {
		this.orderRepository.findAll().stream().forEach(order -> {
			// 18_000_000 = 5hours

			if ((!(order.getStatus().equals(OrderStatus.CONCLUDED) || order.getStatus().equals(OrderStatus.CANCELLED)))
					&& (System.currentTimeMillis() >= (order.getCreatedAt().getTime() + 18_000_000))) {
				changeOrderStatus(order.getId(), OrderStatus.CONCLUDED);
			}
		});

	}
}
