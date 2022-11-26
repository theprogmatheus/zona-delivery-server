package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity.RestaurantCustomerAddressCoords;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerEntity;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderCustomer;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDeliveryAddress;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantCustomerAddressRepository;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantCustomerRepository;

import lombok.Getter;

@Service
@Getter
public class RestaurantCustomerService {

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private RestaurantCustomerRepository customerRepository;

	@Autowired
	private RestaurantCustomerAddressRepository addressRepository;

	public Object listCustomers(UUID restaurantId) {

		if (restaurantId == null)
			return "The restaurantId is not valid";

		return this.customerRepository.findAll().stream()
				.filter(customer -> restaurantId.equals(customer.getRestaurant().getId())).collect(Collectors.toList());
	}

	public Object createNewCustomerByIFoodOrderCustomer(UUID restaurantId, IFoodOrderCustomer iFoodCustomer) {
		return this.createNewCustomer(restaurantId, iFoodCustomer.getName(), iFoodCustomer.getId(), null, null);
	}

	public Object createNewCustomer(UUID restaurantId, String name, String ifoodId, String whatsappId, String phone) {

		if (restaurantId == null)
			return "The restaurantId is not valid";
		if (name == null || name.isEmpty())
			return "The name is not valid";

		RestaurantEntity restaurant = this.restaurantService.getRestaurantById(restaurantId);
		if (restaurant == null)
			return "Restaurant not found";

		return this.customerRepository.saveAndFlush(
				new RestaurantCustomerEntity(null, restaurant, name, ifoodId, whatsappId, phone, null, null));
	}

	public Object changeCustomerName(UUID customerId, String name) {

		if (customerId == null)
			return "The customerId is not valid";

		if (name == null || name.isEmpty())
			return "The name is not valid";

		RestaurantCustomerEntity customer = getCustomerById(customerId);
		if (customer == null)
			return "Customer not found";

		customer.setName(name);

		return this.customerRepository.saveAndFlush(customer);
	}

	public Object changeCustomerIFoodId(UUID customerId, String ifoodId) {

		if (customerId == null)
			return "The customerId is not valid";

		if (ifoodId == null || ifoodId.isEmpty())
			return "The ifoodId is not valid";

		RestaurantCustomerEntity customer = getCustomerById(customerId);
		if (customer == null)
			return "Customer not found";

		customer.setIfoodCustomerId(ifoodId);

		return this.customerRepository.saveAndFlush(customer);
	}

	public Object changeCustomerWhatsAppId(UUID customerId, String whatsappId) {

		if (customerId == null)
			return "The customerId is not valid";

		if (whatsappId == null || whatsappId.isEmpty())
			return "The whatsappId is not valid";

		RestaurantCustomerEntity customer = getCustomerById(customerId);
		if (customer == null)
			return "Customer not found";

		customer.setWhatsappCustomerId(whatsappId);

		return this.customerRepository.saveAndFlush(customer);
	}

	public Object changeCustomerPhone(UUID customerId, String phone) {

		if (customerId == null)
			return "The customerId is not valid";

		if (phone == null || phone.isEmpty())
			return "The phone is not valid";

		RestaurantCustomerEntity customer = getCustomerById(customerId);
		if (customer == null)
			return "Customer not found";

		customer.setPhone(phone);

		return this.customerRepository.saveAndFlush(customer);
	}

	public Object addNewCustomerAddressByIFoodOrderDeliveryAddress(UUID customerId,
			IFoodOrderDeliveryAddress iFoodOrderDeliveryAddress) {

		return this.createCustomerAddress(customerId, iFoodOrderDeliveryAddress.getStreetName(),
				iFoodOrderDeliveryAddress.getStreetNumber(), iFoodOrderDeliveryAddress.getNeighborhood(),
				iFoodOrderDeliveryAddress.getComplement(), iFoodOrderDeliveryAddress.getPostalCode(),
				iFoodOrderDeliveryAddress.getCity(), iFoodOrderDeliveryAddress.getState(),
				iFoodOrderDeliveryAddress.getCountry(), iFoodOrderDeliveryAddress.getReference(),
				iFoodOrderDeliveryAddress.getCoordinates().getLatitude(),
				iFoodOrderDeliveryAddress.getCoordinates().getLongitude());
	}

	public Object createCustomerAddress(UUID customerId, String streetName, String streetNumber, String neighborhood,
			String complement, String postalCode, String city, String state, String country, String reference,
			double latitude, double longitude) {

		if (customerId == null)
			return "The customerId is not valid";
		if (streetName == null || streetName.isEmpty())
			return "The streetName is not valid";
		if (streetNumber == null || streetNumber.isEmpty())
			return "The streetNumber is not valid";
		if (neighborhood == null || neighborhood.isEmpty())
			return "The neighborhood is not valid";
		if (city == null || city.isEmpty())
			return "The city is not valid";
		if (state == null || state.isEmpty())
			return "The state is not valid";
		if (country == null || country.isEmpty())
			return "The country is not valid";

		RestaurantCustomerEntity customer = getCustomerById(customerId);
		if (customer == null)
			return "Customer not found";

		return this.addressRepository.saveAndFlush(new RestaurantCustomerAddressEntity(null, customer, streetName,
				streetNumber, neighborhood, complement, postalCode, city, state, country, reference,
				new RestaurantCustomerAddressCoords(longitude, latitude)));
	}

	public Object deleteCustomerAddress(UUID addressId) {

		if (addressId == null)
			return "The addressId is not valid";
		RestaurantCustomerAddressEntity address = getAddressById(addressId);
		if (address == null)
			return "Address not found";

		this.addressRepository.delete(address);
		this.addressRepository.flush();

		return "success";
	}

	public Object listCustomerAddresses(UUID customerId) {

		if (customerId == null)
			return "The customerId is not valid";

		return this.addressRepository.findAll().stream()
				.filter(address -> address.getCustomer().getId().equals(customerId)).collect(Collectors.toList());
	}

	public Object deleteCustomer(UUID customerId) {
		if (customerId == null)
			return "The customerId is not valid";
		RestaurantCustomerEntity customer = getCustomerById(customerId);
		if (customer == null)
			return "Customer not found";

		this.customerRepository.delete(customer);
		this.customerRepository.flush();

		return "success";
	}

	public RestaurantCustomerEntity getCustomerById(UUID customerId) {
		return this.customerRepository.findById(customerId).orElse(null);
	}

	public RestaurantCustomerAddressEntity getAddressById(UUID addressId) {
		return this.addressRepository.findById(addressId).orElse(null);
	}

	public RestaurantCustomerEntity getCustomeByIfoodCustomerId(String ifoodCustomerId) {
		return this.customerRepository.findByIfoodCustomerId(ifoodCustomerId);
	}
}
