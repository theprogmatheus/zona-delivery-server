package com.github.theprogmatheus.zonadelivery.server.ifood;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodCancellationReason;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodCancellationReasonRequest;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodEvent;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodHandshakeConfirmResponse;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDeliveryAddressCoords;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDetails;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderHandshakeCode;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodSessionToken;

public class IFoodAPI {

	public static final String API_URL = "https://merchant-api.ifood.com.br";

	private static IFoodSessionToken sessionToken;

	public static IFoodOrderDetails getOrderDetails(String orderId) {

		RestTemplate restTemplate = new RestTemplate();

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(new LinkedMultiValueMap<>(),
				createHeaders());

		ResponseEntity<IFoodOrderDetails> response = restTemplate.exchange(
				IFoodEndPoints.ORDER_DETAILS.createUrl(orderId), IFoodEndPoints.ORDER_DETAILS.getMethod(), httpEntity,
				IFoodOrderDetails.class);

		return response.getBody();

	}

	public static IFoodOrderDeliveryAddressCoords getMerchantAddressCoords(String merchantId) {

		RestTemplate restTemplate = new RestTemplate();

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(new LinkedMultiValueMap<>(),
				createHeaders());

		ResponseEntity<Object> response = restTemplate.exchange(IFoodEndPoints.MERCHANT_DETAILS.createUrl(merchantId),
				IFoodEndPoints.MERCHANT_DETAILS.getMethod(), httpEntity, Object.class);

		Map<?, ?> addressMap = (Map<?, ?>) ((Map<?, ?>) response.getBody()).get("address");

		return new IFoodOrderDeliveryAddressCoords((double) addressMap.get("latitude"),
				(double) addressMap.get("longitude"));
	}

	public static void dispatchOrder(String orderId) {
		try {

			RestTemplate restTemplate = new RestTemplate();
			IFoodEndPoints endPoint = IFoodEndPoints.ORDER_DISPATCH;
			HttpEntity<String> httpEntity = new HttpEntity<String>(createHeaders());

			restTemplate.exchange(endPoint.createUrl(orderId), endPoint.getMethod(), httpEntity, Object.class);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void readyToPickupOrder(String orderId) {
		try {

			RestTemplate restTemplate = new RestTemplate();
			IFoodEndPoints endPoint = IFoodEndPoints.ORDER_READY_TO_PICKUP;
			HttpEntity<String> httpEntity = new HttpEntity<String>(createHeaders());

			restTemplate.exchange(endPoint.createUrl(orderId), endPoint.getMethod(), httpEntity, Object.class);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void confirmOrder(String orderId) {

		try {

			RestTemplate restTemplate = new RestTemplate();
			IFoodEndPoints endPoint = IFoodEndPoints.ORDER_CONFIRM;
			HttpEntity<String> httpEntity = new HttpEntity<String>(createHeaders());

			restTemplate.exchange(endPoint.createUrl(orderId), endPoint.getMethod(), httpEntity, Object.class);

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public static IFoodHandshakeConfirmResponse confirmCode(IFoodOrderHandshakeCode handshakeCode) {

		try {
			HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
					HttpClientBuilder.create().build());
			RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

			HttpHeaders headers = createHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			IFoodEndPoints endPoint = IFoodEndPoints.HANDSHAKE_CONFIRM;

			HttpEntity<String> httpEntity = new HttpEntity<>(new ObjectMapper().writeValueAsString(handshakeCode),
					headers);

			ResponseEntity<IFoodHandshakeConfirmResponse> response = restTemplate.exchange(endPoint.createUrl(),
					endPoint.getMethod(), httpEntity, IFoodHandshakeConfirmResponse.class);

			if (response != null)
				return response.getBody();

		} catch (HttpClientErrorException.BadRequest badRequestException) {
			return new IFoodHandshakeConfirmResponse("INCORRECT_CODE", "Delivery confirmation code is incorrect");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public static List<IFoodEvent> eventsPolling() {
		try {

			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(new LinkedMultiValueMap<>(),
					createHeaders());

			ResponseEntity<List<IFoodEvent>> response = restTemplate.exchange(IFoodEndPoints.EVENTS_POLLING.createUrl(),
					IFoodEndPoints.EVENTS_POLLING.getMethod(), httpEntity,
					new ParameterizedTypeReference<List<IFoodEvent>>() {
					});

			return response.getBody();
		} catch (Exception exception) {
			System.err.println("Ocorreu um erro ao tentar recuperar os eventos IFood: " + exception.getMessage());
			return new ArrayList<>();
		}
	}

	public static List<IFoodCancellationReason> getCancellationReasons(String orderId) {
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(new LinkedMultiValueMap<>(),
					createHeaders());
			ResponseEntity<List<IFoodCancellationReason>> response = restTemplate.exchange(
					IFoodEndPoints.ORDER_CANCELLATION_REASONS.createUrl(orderId),
					IFoodEndPoints.ORDER_CANCELLATION_REASONS.getMethod(), httpEntity,
					new ParameterizedTypeReference<List<IFoodCancellationReason>>() {
					});
			return response.getBody();
		} catch (Exception exception) {
			System.err.println("Ocorreu um erro ao tentar recuperar as razões de cancelamento do IFood: "
					+ exception.getMessage());
			return new ArrayList<>();
		}
	}

	public static Object getMerchantStatus(String merchantId) {
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(new LinkedMultiValueMap<>(),
					createHeaders());
			ResponseEntity<Object> response = restTemplate.exchange(
					IFoodEndPoints.MERCHANT_STATUS.createUrl(merchantId), IFoodEndPoints.MERCHANT_STATUS.getMethod(),
					httpEntity, new ParameterizedTypeReference<Object>() {
					});
			return response.getBody();
		} catch (Exception exception) {
			System.err.println(
					"Ocorreu um erro ao tentar recuperar o status de merchant do IFood: " + exception.getMessage());
			return new ArrayList<>();
		}
	}

	public static void requestCancellation(String orderId, IFoodCancellationReasonRequest cancelationReason) {
		if (cancelationReason != null) {
			try {

				RestTemplate restTemplate = new RestTemplate();

				HttpHeaders headers = createHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<String> httpEntity = new HttpEntity<String>(
						new ObjectMapper().writeValueAsString(cancelationReason), headers);

				IFoodEndPoints endPoint = IFoodEndPoints.ORDER_REQUEST_CANCELLATION;

				restTemplate.exchange(endPoint.createUrl(orderId), endPoint.getMethod(), httpEntity, Object.class);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void acceptCancellation(String orderId) {
		if (orderId != null && !orderId.isBlank()) {
			try {

				RestTemplate restTemplate = new RestTemplate();

				HttpHeaders headers = createHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(new LinkedMultiValueMap<>(),
						createHeaders());

				IFoodEndPoints endPoint = IFoodEndPoints.ORDER_ACCEPT_CANCELLATION;

				restTemplate.exchange(endPoint.createUrl(orderId), endPoint.getMethod(), httpEntity, Object.class);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void denyCancellation(String orderId) {
		if (orderId != null && !orderId.isBlank()) {
			try {

				RestTemplate restTemplate = new RestTemplate();

				HttpHeaders headers = createHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(new LinkedMultiValueMap<>(),
						createHeaders());

				IFoodEndPoints endPoint = IFoodEndPoints.ORDER_DENY_CANCELLATION;

				restTemplate.exchange(endPoint.createUrl(orderId), endPoint.getMethod(), httpEntity, Object.class);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void eventsAcknowledgment(List<IFoodEvent> events) {
		if (events != null && !events.isEmpty()) {
			try {

				RestTemplate restTemplate = new RestTemplate();

				HttpHeaders headers = createHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<String> httpEntity = new HttpEntity<String>(new ObjectMapper().writeValueAsString(events),
						headers);

				IFoodEndPoints endPoint = IFoodEndPoints.EVENTS_ACKNOW;

				ResponseEntity<Object> response = restTemplate.exchange(endPoint.createUrl(), endPoint.getMethod(),
						httpEntity, Object.class);

				if (response.getStatusCodeValue() != 202)
					throw new Exception("eventsAcknowledgment: response status code is not accepted 202");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", getSessionToken().toHeaderString());
		return headers;
	}

	public static IFoodSessionToken getSessionToken() {
		if (sessionToken == null || (System.currentTimeMillis() >= sessionToken.getExpiresIn()))
			return createNewSessionToken();
		return sessionToken;
	}

	public static IFoodSessionToken createNewSessionToken() {

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grantType", System.getenv("IFOOD_GRANT_TYPE"));
		map.add("clientId", System.getenv("IFOOD_CLIENT_ID"));
		map.add("clientSecret", System.getenv("IFOOD_CLIENT_SECRET"));

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

		ResponseEntity<IFoodSessionToken> response = restTemplate.exchange(
				IFoodEndPoints.AUTHENTICATION_TOKEN.createUrl(), IFoodEndPoints.AUTHENTICATION_TOKEN.getMethod(),
				entity, IFoodSessionToken.class);

		sessionToken = response.getBody();
		sessionToken.setExpiresIn(System.currentTimeMillis() + (sessionToken.getExpiresIn() * 1000));

		return sessionToken;
	}

	public enum IFoodEndPoints {

		AUTHENTICATION_TOKEN("/authentication/v1.0/oauth/token", HttpMethod.POST),
		EVENTS_POLLING("/order/v1.0/events:polling", HttpMethod.GET),
		EVENTS_ACKNOW("/order/v1.0/events/acknowledgment", HttpMethod.POST),
		ORDER_DETAILS("/order/v1.0/orders/{{0}}", HttpMethod.GET),
		ORDER_DISPATCH("/order/v1.0/orders/{{0}}/dispatch", HttpMethod.POST),
		ORDER_CONFIRM("/order/v1.0/orders/{{0}}/confirm", HttpMethod.POST),
		ORDER_START_PREPARATION("/order/v1.0/orders/{{0}}/startPreparation", HttpMethod.POST),
		ORDER_READY_TO_PICKUP("/order/v1.0/orders/{{0}}/readyToPickup", HttpMethod.POST), //
		ORDER_REQUEST_CANCELLATION("/order/v1.0/orders/{{0}}/requestCancellation", HttpMethod.POST),
		ORDER_ACCEPT_CANCELLATION("/order/v1.0/orders/{{0}}/acceptCancellation", HttpMethod.POST),
		ORDER_DENY_CANCELLATION("/order/v1.0/orders/{{0}}/denyCancellation", HttpMethod.POST),
		ORDER_REQUEST_DRIVER("/order/v1.0/orders/{{0}}/requestDriver", HttpMethod.POST),
		ORDER_CANCELLATION_REASONS("/order/v1.0/orders/{{0}}/cancellationReasons", HttpMethod.GET),

		HANDSHAKE_CONFIRM("/marketplace-delivery-handshake/confirm", HttpMethod.POST),
		LIST_MERCHANTS("/merchant/v1.0/merchants", HttpMethod.GET),
		MERCHANT_DETAILS("/merchant/v1.0/merchants/{{0}}", HttpMethod.GET),
		MERCHANT_STATUS("/merchant/v1.0/merchants/{{0}}/status", HttpMethod.GET);

		private String value;
		private HttpMethod method;

		private IFoodEndPoints(String value, HttpMethod method) {
			this.value = value;
			this.method = method;
		}

		public String getValue() {
			return value;
		}

		public HttpMethod getMethod() {
			return method;
		}

		public String createUrl(String... strings) {
			String string = this.value;
			for (int i = 0; i < strings.length; i++) {
				string = string.replace("{{" + i + "}}", strings[i]);
			}

			return (API_URL).concat(string);
		}
	}
}
