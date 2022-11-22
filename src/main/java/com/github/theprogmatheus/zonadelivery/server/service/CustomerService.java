package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerAddressEntity.RestaurantCustomerAddressCoords;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer.RestaurantCustomerEntity;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderCustomer;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDeliveryAddress;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantCustomerAddressRepository;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantCustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private RestaurantCustomerRepository customerRepository;

	@Autowired
	private RestaurantCustomerAddressRepository customerAddressRepository;

	public RestaurantCustomerEntity createNewCustomerByIFoodOrderCustomer(IFoodOrderCustomer iFoodCustomer) {
		return this.createNewCustomer(iFoodCustomer.getName(), null, iFoodCustomer.getId(), null);
	}

	public RestaurantCustomerEntity createNewCustomer(String name, String phone, String ifoodId, String whatsappId) {
		return this.customerRepository
				.saveAndFlush(new RestaurantCustomerEntity(null, name, ifoodId, whatsappId, phone, new HashSet<>()));
	}

	public RestaurantCustomerAddressEntity addNewCustomerAddressByIFoodOrderDeliveryAddress(
			RestaurantCustomerEntity customer, IFoodOrderDeliveryAddress iFoodOrderDeliveryAddress) {
		return this.addNewCustomerAddress(customer, iFoodOrderDeliveryAddress.getStreetName(),
				iFoodOrderDeliveryAddress.getStreetNumber(), iFoodOrderDeliveryAddress.getNeighborhood(),
				iFoodOrderDeliveryAddress.getComplement(), iFoodOrderDeliveryAddress.getPostalCode(),
				iFoodOrderDeliveryAddress.getCity(), iFoodOrderDeliveryAddress.getState(),
				iFoodOrderDeliveryAddress.getCountry(), iFoodOrderDeliveryAddress.getReference(),
				iFoodOrderDeliveryAddress.getCoordinates().getLatitude(),
				iFoodOrderDeliveryAddress.getCoordinates().getLongitude());
	}

	public RestaurantCustomerAddressEntity addNewCustomerAddress(RestaurantCustomerEntity customer,
			String streetName, String streetNumber, String neighborhood, String complement, String postalCode,
			String city, String state, String country, String reference, double coordinateLatitude,
			double coordinateLongitude) {

		RestaurantCustomerAddressEntity address = new RestaurantCustomerAddressEntity(null, customer, streetName,
				streetNumber, neighborhood, complement, postalCode, city, state, country, reference,
				new RestaurantCustomerAddressCoords(coordinateLongitude, coordinateLatitude));

		if (!customer.getAddresses().contains(address)) {

			RestaurantCustomerAddressEntity createdAddress = this.customerAddressRepository.saveAndFlush(address);

			customer.getAddresses().add(address);
			this.customerRepository.saveAndFlush(customer);

			return createdAddress;
		} else
			return customer.getAddresses().stream().filter(target -> target.equals(address)).findFirst().orElse(null);
	}

	public RestaurantCustomerEntity getCustomeByIfoodCustomerId(String ifoodCustomerId) {
		return this.customerRepository.findByIfoodCustomerId(ifoodCustomerId);
	}

	public RestaurantCustomerRepository getCustomerRepository() {
		return customerRepository;
	}

	public RestaurantCustomerAddressRepository getCustomerAddressRepository() {
		return customerAddressRepository;
	}

}
