package com.github.theprogmatheus.zonadelivery.server.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantCustomerAddressDTO;
import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantCustomerDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerEntity;
import com.github.theprogmatheus.zonadelivery.server.service.RestaurantCustomerService;
import com.github.theprogmatheus.zonadelivery.server.util.StringUtils;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/restaurant/{restaurantId}/customer")
public class RestaurantCustomerController {

	@Autowired
	private RestaurantCustomerService customerService;

	@GetMapping("/list")
	public Object list(@PathVariable UUID restaurantId) {
		try {

			Object result = this.customerService.listCustomers(restaurantId);

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.ok(((List<RestaurantCustomerEntity>) result).stream()
					.map(customer -> new RestaurantCustomerDTO(customer)).collect(Collectors.toList()));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@GetMapping("/{customerId}")
	public Object customer(@PathVariable UUID restaurantId, @PathVariable String customerId,
			@PathParam(value = "by") String by) {
		try {

			RestaurantCustomerEntity customer = null;

			if (by != null && !by.isBlank()) {

				switch (by.toUpperCase()) {
				
				case "WHATSAPP":
					customer = this.customerService.getCustomeByWhatsappCustomerId(customerId);
					break;
					
				case "IFOOD":
					customer = this.customerService.getCustomeByIfoodCustomerId(customerId);
					break;

				default:
					customer = this.customerService.getCustomerById(StringUtils.getUUIDFromString(customerId));
					break;
				}

			} else
				customer = this.customerService.getCustomerById(StringUtils.getUUIDFromString(customerId));

			if (customer != null)
				return ResponseEntity.ok(new RestaurantCustomerDTO(customer));
			else
				return ResponseEntity.ok("Customer not found");
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/register")
	public Object register(@PathVariable UUID restaurantId, @RequestBody Map<String, String> body) {
		try {

			Object result = this.customerService.createNewCustomer(restaurantId, body.get("name"), body.get("ifoodId"),
					body.get("whatsappId"), body.get("phone"));

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new RestaurantCustomerDTO((RestaurantCustomerEntity) result));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/{customerId}/change_name")
	public Object changeName(@PathVariable UUID restaurantId, @PathVariable UUID customerId,
			@RequestBody Map<String, String> body) {
		try {
			Object result = this.customerService.changeCustomerName(customerId, body.get("name"));

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new RestaurantCustomerDTO((RestaurantCustomerEntity) result));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/{customerId}/change_ifood_id")
	public Object changeIFoodId(@PathVariable UUID restaurantId, @PathVariable UUID customerId,
			@RequestBody Map<String, String> body) {
		try {
			Object result = this.customerService.changeCustomerIFoodId(customerId, body.get("ifoodId"));

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new RestaurantCustomerDTO((RestaurantCustomerEntity) result));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/{customerId}/change_whatsapp_id")
	public Object changeWhatsappId(@PathVariable UUID restaurantId, @PathVariable UUID customerId,
			@RequestBody Map<String, String> body) {
		try {
			Object result = this.customerService.changeCustomerWhatsAppId(customerId, body.get("whatsappId"));

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new RestaurantCustomerDTO((RestaurantCustomerEntity) result));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/{customerId}/change_phone")
	public Object changePhone(@PathVariable UUID restaurantId, @PathVariable UUID customerId,
			@RequestBody Map<String, String> body) {
		try {
			Object result = this.customerService.changeCustomerPhone(customerId, body.get("phone"));

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new RestaurantCustomerDTO((RestaurantCustomerEntity) result));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@GetMapping("/{customerId}/addresses")
	public Object addresses(@PathVariable UUID restaurantId, @PathVariable UUID customerId) {
		try {
			Object result = this.customerService.listCustomerAddresses(customerId);

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.ok(((List<RestaurantCustomerAddressEntity>) result).stream()
					.map(address -> new RestaurantCustomerAddressDTO(address)).collect(Collectors.toList()));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/{customerId}/address/register")
	public Object addressRegister(@PathVariable UUID restaurantId, @PathVariable UUID customerId,
			@RequestBody Map<String, String> body) {
		try {

			String streetName = body.get("streetName");
			String streetNumber = body.get("streetNumber");
			String neighborhood = body.get("neighborhood");
			String complement = body.get("complement");
			String postalCode = body.get("postalCode");
			String city = body.get("city");
			String state = body.get("state");
			String country = body.get("country");
			String reference = body.get("reference");
			double latitude = StringUtils.getDoubleFromString(body.get("latitude"));
			double longitude = StringUtils.getDoubleFromString(body.get("longitude"));

			Object result = this.customerService.createCustomerAddress(customerId, streetName, streetNumber,
					neighborhood, complement, postalCode, city, state, country, reference, latitude, longitude);

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new RestaurantCustomerAddressDTO((RestaurantCustomerAddressEntity) result));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@DeleteMapping("/{customerId}/address/delete")
	public Object addressDelete(@PathVariable UUID restaurantId, @RequestBody Map<String, String> body) {
		try {
			Object result = this.customerService
					.deleteCustomerAddress(StringUtils.getUUIDFromString(body.get("address")));
			return ResponseEntity.ok(result);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@DeleteMapping("/{customerId}/delete")
	public Object delete(@PathVariable UUID restaurantId, @RequestBody Map<String, String> body) {
		try {
			Object result = this.customerService.deleteCustomer(StringUtils.getUUIDFromString(body.get("customer")));
			return ResponseEntity.ok(result);
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}
}
