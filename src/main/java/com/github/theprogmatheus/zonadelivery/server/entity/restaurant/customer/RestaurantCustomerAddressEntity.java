package com.github.theprogmatheus.zonadelivery.server.entity.restaurant.customer;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "restaurant_customer_addresses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantCustomerAddressEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
	@Type(type = "uuid-char")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private RestaurantCustomerEntity customer;

	@Column(columnDefinition = "VARCHAR(128)", nullable = false)
	private String streetName;

	@Column(columnDefinition = "VARCHAR(128)", nullable = false)
	private String streetNumber;

	@Column(columnDefinition = "VARCHAR(128)", nullable = false)
	private String neighborhood;

	@Column(columnDefinition = "VARCHAR(128)")
	private String complement;

	@Column(columnDefinition = "VARCHAR(128)")
	private String postalCode;

	@Column(columnDefinition = "VARCHAR(128)", nullable = false)
	private String city;

	@Column(columnDefinition = "VARCHAR(128)", nullable = false)
	private String state;

	@Column(columnDefinition = "VARCHAR(128)", nullable = false)
	private String country;

	@Column(columnDefinition = "VARCHAR(128)")
	private String reference;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "longitude", column = @Column(name = "coordinates_longitude")),
			@AttributeOverride(name = "latitude", column = @Column(name = "coordinates_latitude")) })
	private RestaurantCustomerAddressCoords coordinates;

	@Override
	public int hashCode() {
		return Objects.hash(city, complement, coordinates, country, neighborhood, postalCode, reference, state,
				streetName, streetNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestaurantCustomerAddressEntity other = (RestaurantCustomerAddressEntity) obj;
		return Objects.equals(city, other.city) && Objects.equals(complement, other.complement)
				&& Objects.equals(coordinates, other.coordinates) && Objects.equals(country, other.country)
				&& Objects.equals(neighborhood, other.neighborhood) && Objects.equals(postalCode, other.postalCode)
				&& Objects.equals(reference, other.reference) && Objects.equals(state, other.state)
				&& Objects.equals(streetName, other.streetName) && Objects.equals(streetNumber, other.streetNumber);
	}

	@Embeddable
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RestaurantCustomerAddressCoords {

		private double longitude;
		private double latitude;

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			RestaurantCustomerAddressCoords other = (RestaurantCustomerAddressCoords) obj;
			return Double.doubleToLongBits(latitude) == Double.doubleToLongBits(other.latitude)
					&& Double.doubleToLongBits(longitude) == Double.doubleToLongBits(other.longitude);
		}

		@Override
		public int hashCode() {
			return Objects.hash(latitude, longitude);
		}

	}

}
