package com.github.theprogmatheus.zonadelivery.server.ifood.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDeliveryAddressCoords;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodOrderDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

public class IFoodOrderDeliveryUtils {

	public static List<IFoodOrderDetails> sortByAddressCoords(IFoodOrderDeliveryAddressCoords main,
			List<IFoodOrderDetails> orders) {

		// clona a lista para LinkedList
		final LinkedList<IFoodOrderDetails> newOrdersList = new LinkedList<>(orders);

		// vamos guardar as duas entregas mais longe uma da outra
		IFoodOrderDetails startPoint;
		IFoodOrderDetails endPoint;

		// Primeiro, vamos pegar a entrega mais longe da loja, e definir em endPoint
		Collections.sort(newOrdersList, new IFoodOrderDeliveryAddressCoordsComparator(main, true));
		endPoint = newOrdersList.getFirst();

		// Agora, vamos pegar a entrega mais longe do endPoint, e definir em startPoint
		Collections.sort(newOrdersList, new IFoodOrderDeliveryAddressCoordsComparator(
				endPoint.getDelivery().getDeliveryAddress().getCoordinates(), true));
		startPoint = newOrdersList.getFirst();

		// Por fim, vamos organizar a rota em ordem do incio da rota até o final.
		return track(startPoint, newOrdersList);
	}

	private static List<IFoodOrderDetails> track(IFoodOrderDetails mainOrder, List<IFoodOrderDetails> orders) {

		final List<IFoodOrderDetails> ordersClone = new ArrayList<>(orders);
		final List<IFoodOrderDetails> newOrdersList = new ArrayList<>();

		while (!ordersClone.isEmpty()) {
			newOrdersList.add(mainOrder = nearestCoord(mainOrder, ordersClone));
			ordersClone.remove(mainOrder);
		}
		return newOrdersList;
	}

	private static IFoodOrderDetails nearestCoord(IFoodOrderDetails mainOrder, List<IFoodOrderDetails> orders) {

		final LinkedList<IFoodOrderDetails> newOrdersList = new LinkedList<>(orders);

		Collections.sort(newOrdersList, new IFoodOrderDeliveryAddressCoordsComparator(
				mainOrder.getDelivery().getDeliveryAddress().getCoordinates(), false));

		return newOrdersList.getFirst();
	}

	@Data
	@AllArgsConstructor
	private static class IFoodOrderDeliveryAddressCoordsComparator implements Comparator<IFoodOrderDetails> {

		private IFoodOrderDeliveryAddressCoords main;
		private boolean reverse;

		@Override
		public int compare(IFoodOrderDetails orderA, IFoodOrderDetails orderB) {

			IFoodOrderDeliveryAddressCoords coordA = orderA.getDelivery().getDeliveryAddress().getCoordinates();
			IFoodOrderDeliveryAddressCoords coordB = orderB.getDelivery().getDeliveryAddress().getCoordinates();

			double distanceCoordA = this.distance(this.main.getLongitude(), this.main.getLatitude(),
					coordA.getLongitude(), coordA.getLatitude());

			double distanceCoordB = this.distance(this.main.getLongitude(), this.main.getLatitude(),
					coordB.getLongitude(), coordB.getLatitude());

			if (distanceCoordA > distanceCoordB)
				return this.reverse ? -1 : 1;
			if (distanceCoordA < distanceCoordB)
				return this.reverse ? 1 : -1;

			return 0;
		}

		private double distance(double x1, double y1, double x2, double y2) {
			return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
		}
	}

}
