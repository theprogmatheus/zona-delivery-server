package com.github.theprogmatheus.zonadelivery.server.ifood.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.theprogmatheus.zonadelivery.server.ifood.IFoodAPI;
import com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers.IFoodOrderCancelledEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers.IFoodOrderConcludedEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers.IFoodOrderConfirmedEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers.IFoodOrderDispatchedEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.events.handlers.IFoodOrderPlacedEventHandler;
import com.github.theprogmatheus.zonadelivery.server.ifood.objects.IFoodEvent;
import com.github.theprogmatheus.zonadelivery.server.service.OrderService;

@Component
public class IFoodEventsManager {

	private static Map<String, List<IFoodEventHandler>> registeredEventHandlers;

	private List<String> acknowEvents = new ArrayList<>();

	@Autowired
	private OrderService orderService;

	private void registerAllEventHandlers() {

		registeredEventHandlers = new HashMap<>();

		// register events
		registerEventHandler(new IFoodOrderCancelledEventHandler(this.orderService));
		registerEventHandler(new IFoodOrderConcludedEventHandler(this.orderService));
		registerEventHandler(new IFoodOrderConfirmedEventHandler(this.orderService));
		registerEventHandler(new IFoodOrderDispatchedEventHandler(this.orderService));
		registerEventHandler(new IFoodOrderPlacedEventHandler(this.orderService));
	}

	/*
	 * O IFood recomenda que o polling dos eventos acontece no mínimo a cada 30
	 * segundos, mas o rate-limit da aplicação é de 6000 req/min, o que me permite
	 * usar um tempo menor de requisições... eu decidi verificar novos eventos a
	 * cada 3 segundos, o que dá um total de 20 req/min
	 * 
	 * Podemos conferir os rate-limit através de
	 * https://developer.ifood.com.br/pt-BR/docs/rate-limit
	 */
	@Scheduled(fixedRate = 30_000)
	public void checkIFoodEvents() {

		if (registeredEventHandlers == null)
			registerAllEventHandlers();

		// checar e executar os eventos do iFood
		List<IFoodEvent> events = IFoodAPI.eventsPolling();

		if (events != null && !events.isEmpty()) {

			List<IFoodEvent> eventsToAcknowledgment = new ArrayList<>();

			for (IFoodEvent event : events) {

				List<IFoodEventHandler> eventHandlers = registeredEventHandlers.get(event.getFullCode());
				if ((eventHandlers != null && !eventHandlers.isEmpty())
						&& (!this.acknowEvents.contains(event.getId()))) {

					this.acknowEvents.add(event.getId());

					for (IFoodEventHandler eventHandler : eventHandlers) {
						try {

							if (eventHandler.handle(event))
								eventsToAcknowledgment.add(event);

						} catch (Exception exception) {
							System.err.println("Ocorreu um erro ao tentar executar o evento ("
									+ eventHandler.getClass().getName() + "): " + exception.getMessage());

							exception.printStackTrace();
						}
					}

				} else
					eventsToAcknowledgment.add(event);
			}

			IFoodAPI.eventsAcknowledgment(eventsToAcknowledgment);
		}

	}

	/*
	 * a lista this.acknowEvents foi criada com a intenção de prevenir eventos
	 * repetidos, por isso, todo o evento é adicionado a esta lista, e verificada
	 * depois para não acionar mais de uma vez. Porém, essa lista vai ficando cheia
	 * com o tempo, e precisamos limpar ela, para não sobrecarregar a memória.
	 * 
	 * A cada 30 minutos a lista this.acknowEvents é limpa
	 */
	@Scheduled(fixedRate = 1_800_000)
	public void clearAcknowEvents() {
		this.acknowEvents.clear();
	}

	public static void registerEventHandler(IFoodEventHandler eventHandler) {
		List<IFoodEventHandler> handlers = registeredEventHandlers.get(eventHandler.getEventName());
		if (handlers == null)
			handlers = new ArrayList<>();

		if (!handlers.contains(eventHandler)) {

			handlers.add(eventHandler);

			registeredEventHandlers.put(eventHandler.getEventName(), handlers);
		}
	}
}
