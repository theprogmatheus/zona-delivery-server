package com.github.theprogmatheus.zonadelivery.server.service;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.RestaurantEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuAditionalEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuCategoryEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuItemEntity;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantMenuAditionalRepository;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantMenuCategoryRepository;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantMenuItemRepository;
import com.github.theprogmatheus.zonadelivery.server.repository.RestaurantMenuRepository;

import lombok.Getter;

/*
 * Existe uma falha de segurança que permite que outros clientes consulte os cardápios e faça alterações de outros estabelecimentos... resolva isso!
 */

@Service
@Getter
public class RestaurantMenuService {

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private RestaurantMenuRepository menuRepository;

	@Autowired
	private RestaurantMenuCategoryRepository categoryRepository;

	@Autowired
	private RestaurantMenuItemRepository itemRepository;

	@Autowired
	private RestaurantMenuAditionalRepository aditionalRepository;

	public Object listMenus(UUID restaurantId) {

		if (restaurantId == null)
			return "The restaurantId is not valid";

		return this.menuRepository.findAll().stream().filter(menu -> restaurantId.equals(menu.getRestaurant().getId()))
				.collect(Collectors.toList());
	}

	public Object listCategories(UUID menuId) {

		if (menuId == null)
			return "The menuId is not valid";

		return this.categoryRepository.findAll().stream().filter(category -> category.getMenu().getId().equals(menuId))
				.collect(Collectors.toList());
	}

	public Object listItems(UUID menuId) {

		if (menuId == null)
			return "The menuId is not valid";

		return this.itemRepository.findAll().stream().filter(item -> item.getMenu().getId().equals(menuId))
				.collect(Collectors.toList());
	}

	public Object listAditionals(UUID menuId) {

		if (menuId == null)
			return "The menuId is not valid";

		return this.aditionalRepository.findAll().stream()
				.filter(aditional -> aditional.getMenu().getId().equals(menuId)).collect(Collectors.toList());
	}

	public Object createMenu(UUID restaurantId, String name) {

		if (restaurantId == null)
			return "The restaurantId is not valid";

		if (name == null || name.isEmpty())
			return "The name is not valid";

		RestaurantEntity restaurant = this.restaurantService.getRestaurantById(restaurantId);

		if (restaurant == null)
			return "Restaurant not found";

		return this.menuRepository.saveAndFlush(new RestaurantMenuEntity(null, restaurant, name, null));
	}

	public Object createCategory(UUID menuId, String name) {

		if (menuId == null)
			return "The menuId is not valid";

		if (name == null || name.isEmpty())
			return "The name is not valid";

		RestaurantMenuEntity menu = getMenuById(menuId);
		if (menu == null)
			return "Menu not found";

		return this.categoryRepository.saveAndFlush(new RestaurantMenuCategoryEntity(null, menu, name, null, true));
	}

	public Object createItem(UUID menuId, String itemName, String itemDescription, String itemImage, double itemPrice) {

		if (menuId == null)
			return "The menuId is not valid";

		if (itemName == null || itemName.isEmpty())
			return "The itemName is not valid";

		if (itemDescription == null || itemDescription.isEmpty())
			return "The itemDescription is not valid";

		if (itemPrice < 0)
			return "The itemPrice is not valid";

		RestaurantMenuEntity menu = getMenuById(menuId);
		if (menu == null)
			return "Menu not found";

		return this.itemRepository.saveAndFlush(new RestaurantMenuItemEntity(null, menu, null, itemName,
				itemDescription, itemImage, itemPrice, 0, true, null, null));
	}

	public Object createAditional(UUID menuId, String aditionalName, String aditionalDescription, double aditionalPrice,
			int minAmount, int maxAmount) {

		if (menuId == null)
			return "The menuId is not valid";
		if (aditionalName == null || aditionalName.isEmpty())
			return "The aditionalName is not valid";
		if (aditionalPrice < 0)
			return "The aditionalPrice is not valid";

		RestaurantMenuEntity menu = getMenuById(menuId);
		if (menu == null)
			return "Menu not found";

		if (minAmount < 0)
			minAmount = 1;

		if (maxAmount < 0)
			maxAmount = Integer.MAX_VALUE;

		return this.aditionalRepository.saveAndFlush(new RestaurantMenuAditionalEntity(null, menu, null, null,
				aditionalName, aditionalDescription, aditionalPrice, minAmount, maxAmount, true));

	}

	public Object addItemToCategory(UUID menuId, UUID categoryId, UUID itemId) {

		if (menuId == null)
			return "The menuId is not valid";

		if (categoryId == null)
			return "The categoryId is not valid";

		if (itemId == null)
			return "The itemId is not valid";

		RestaurantMenuCategoryEntity category = getMenuCategoryById(categoryId);
		if (category == null)
			return "Category not found";

		if (!menuId.equals(category.getMenu().getId()))
			return "The category does not belong on this menu";

		RestaurantMenuItemEntity item = getMenuItemById(itemId);
		if (item == null)
			return "Item not found";

		if (!menuId.equals(item.getMenu().getId()))
			return "The item does not belong on this menu";

		category.getItems().add(item);

		return this.categoryRepository.saveAndFlush(category);
	}

	public Object addAditionalToItem(UUID menuId, UUID itemId, UUID aditionalId) {

		if (menuId == null)
			return "The menuId is not valid";

		if (itemId == null)
			return "The itemId is not valid";

		if (aditionalId == null)
			return "The aditionalId is not valid";

		RestaurantMenuItemEntity item = getMenuItemById(itemId);
		if (item == null)
			return "Item not found";

		if (!menuId.equals(item.getMenu().getId()))
			return "The item does not belong on this menu";

		RestaurantMenuAditionalEntity aditional = getMenuAditionalById(aditionalId);
		if (aditional == null)
			return "Aditional not found";

		if (!menuId.equals(aditional.getMenu().getId()))
			return "The aditional does not belong on this menu";

		item.getAditionals().add(aditional);

		return this.itemRepository.saveAndFlush(item);
	}

	public void deleteMenu(UUID menuId) {
		if (menuId == null)
			return;
		this.menuRepository.deleteById(menuId);
		this.menuRepository.flush();
	}

	public void deleteCategory(UUID categoryId) {
		if (categoryId == null)
			return;
		this.categoryRepository.deleteById(categoryId);
		this.categoryRepository.flush();
	}

	public void deleteItem(UUID itemId) {
		if (itemId == null)
			return;
		this.itemRepository.deleteById(itemId);
		this.itemRepository.flush();
	}

	public void deleteAditional(UUID aditionalId) {
		if (aditionalId == null)
			return;
		this.aditionalRepository.deleteById(aditionalId);
		this.aditionalRepository.flush();
	}

	public RestaurantMenuEntity getMenuById(UUID menuId) {
		return this.menuRepository.findById(menuId).orElse(null);
	}

	public RestaurantMenuCategoryEntity getMenuCategoryById(UUID categoryId) {
		return this.categoryRepository.findById(categoryId).orElse(null);
	}

	public RestaurantMenuItemEntity getMenuItemById(UUID itemId) {
		return this.itemRepository.findById(itemId).orElse(null);
	}

	public RestaurantMenuAditionalEntity getMenuAditionalById(UUID aditionalId) {
		return this.aditionalRepository.findById(aditionalId).orElse(null);
	}

}
