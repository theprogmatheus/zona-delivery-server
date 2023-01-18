package com.github.theprogmatheus.zonadelivery.server.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantMenuAditionalDTO;
import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantMenuCategoryDTO;
import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantMenuDTO;
import com.github.theprogmatheus.zonadelivery.server.dto.RestaurantMenuItemDTO;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuAditionalEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuCategoryEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuEntity;
import com.github.theprogmatheus.zonadelivery.server.entity.restaurant.menu.RestaurantMenuItemEntity;
import com.github.theprogmatheus.zonadelivery.server.enums.UserRole;
import com.github.theprogmatheus.zonadelivery.server.service.RestaurantMenuService;
import com.github.theprogmatheus.zonadelivery.server.util.StringUtils;

@SuppressWarnings("unchecked")
@Secured(UserRole.USER_ROLE_NAME)
@RestController
@RequestMapping("/restaurant/{restaurantId}/menu")
public class RestaurantMenuController {

	@Autowired
	private RestaurantMenuService menuService;

	@GetMapping("/list")
	public Object menus(@PathVariable UUID restaurantId) {
		try {
			Object result = this.menuService.listMenus(restaurantId);

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.ok(((List<RestaurantMenuEntity>) result).stream()
					.map(menu -> new RestaurantMenuDTO(menu)).collect(Collectors.toList()));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@GetMapping("/{menuId}")
	public Object find(@PathVariable UUID restaurantId, @PathVariable UUID menuId) {
		try {
			RestaurantMenuEntity menu = this.menuService.getMenuById(menuId);

			if (menu == null)
				return ResponseEntity.ok("Menu not found");

			return ResponseEntity.ok(new RestaurantMenuDTO(menu));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@GetMapping("/{menuId}/categories")
	public Object categories(@PathVariable UUID restaurantId, @PathVariable UUID menuId) {
		try {
			Object result = this.menuService.listCategories(menuId);

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.ok(((List<RestaurantMenuCategoryEntity>) result).stream()
					.map(category -> new RestaurantMenuCategoryDTO(category)).collect(Collectors.toList()));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@GetMapping("/{menuId}/items")
	public Object items(@PathVariable UUID restaurantId, @PathVariable UUID menuId) {
		try {
			Object result = this.menuService.listItems(menuId);

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.ok(((List<RestaurantMenuItemEntity>) result).stream()
					.map(item -> new RestaurantMenuItemDTO(item)).collect(Collectors.toList()));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@GetMapping("/{menuId}/aditionals")
	public Object aditionals(@PathVariable UUID restaurantId, @PathVariable UUID menuId) {
		try {
			Object result = this.menuService.listAditionals(menuId);

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.ok(((List<RestaurantMenuAditionalEntity>) result).stream()
					.map(aditional -> new RestaurantMenuAditionalDTO(aditional)).collect(Collectors.toList()));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/register")
	public Object menuRegister(@PathVariable UUID restaurantId, @RequestBody RestaurantMenuDTO menu) {
		try {

			Object result = this.menuService.createMenu(restaurantId, menu.getName());
			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.status(HttpStatus.CREATED).body(new RestaurantMenuDTO((RestaurantMenuEntity) result));

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/{menuId}/register_category")
	public Object menuRegisterCategory(@PathVariable UUID restaurantId, @PathVariable UUID menuId,
			@RequestBody Map<String, String> body) {
		try {

			Object result = this.menuService.createCategory(menuId, body.get("name"));

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new RestaurantMenuCategoryDTO((RestaurantMenuCategoryEntity) result));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/{menuId}/register_item")
	public Object menuRegisterItem(@PathVariable UUID restaurantId, @PathVariable UUID menuId,
			@RequestBody Map<String, String> body) {
		try {

			Object result = this.menuService.createItem(menuId, body.get("name"), body.get("description"),
					body.get("image"), StringUtils.getDoubleFromString(body.get("price")));

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new RestaurantMenuItemDTO((RestaurantMenuItemEntity) result));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/{menuId}/register_item_aditional")
	public Object menuRegisterItemAditional(@PathVariable UUID restaurantId, @PathVariable UUID menuId,
			@RequestBody RestaurantMenuAditionalDTO aditional) {
		try {

			Object result = this.menuService.createAditional(menuId, aditional.getName(), aditional.getDescription(),
					aditional.getPrice(), aditional.getMinAmount(), aditional.getMaxAmount());

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new RestaurantMenuAditionalDTO((RestaurantMenuAditionalEntity) result));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/{menuId}/add_item_to_category")
	public Object menuAddItemToCategory(@PathVariable UUID restaurantId, @PathVariable UUID menuId,
			@RequestBody Map<String, String> body) {
		try {

			Object result = this.menuService.addItemToCategory(menuId,
					StringUtils.getUUIDFromString(body.get("category")),
					StringUtils.getUUIDFromString(body.get("item")));

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.ok(new RestaurantMenuCategoryDTO((RestaurantMenuCategoryEntity) result));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@PostMapping("/{menuId}/add_aditional_to_item")
	public Object menuAddAditionalToItem(@PathVariable UUID restaurantId, @PathVariable UUID menuId,
			@RequestBody Map<String, String> body) {
		try {

			Object result = this.menuService.addAditionalToItem(menuId, StringUtils.getUUIDFromString(body.get("item")),
					StringUtils.getUUIDFromString(body.get("aditional")));

			if (result instanceof String)
				return ResponseEntity.ok(result);

			return ResponseEntity.ok(new RestaurantMenuItemDTO((RestaurantMenuItemEntity) result));
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	// add_aditional_to_item
	// add_item_to_category

	//// DELETE DELETE DELETE DELETE DELETE DELETE !!!!!

	@DeleteMapping("/{menuId}/delete_menu")
	public Object menuDelete(@PathVariable UUID restaurantId, @PathVariable UUID menuId,
			@RequestBody Map<String, String> body) {
		try {
			menuId = StringUtils.getUUIDFromString(body.get("menu"));
			this.menuService.deleteMenu(menuId);
			return ResponseEntity.ok(this.menuService.getMenuById(menuId) == null);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@DeleteMapping("/{menuId}/delete_category")
	public Object menuDeleteCategory(@PathVariable UUID restaurantId, @PathVariable UUID menuId,
			@RequestBody Map<String, String> body) {
		try {
			UUID categoryId = StringUtils.getUUIDFromString(body.get("category"));
			this.menuService.deleteCategory(categoryId);
			return ResponseEntity.ok(this.menuService.getMenuCategoryById(categoryId) == null);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@DeleteMapping("/{menuId}/delete_item")
	public Object menuDeleteItem(@PathVariable UUID restaurantId, @PathVariable UUID menuId,
			@RequestBody Map<String, String> body) {
		try {
			UUID itemId = StringUtils.getUUIDFromString(body.get("item"));
			this.menuService.deleteItem(itemId);
			return ResponseEntity.ok(this.menuService.getMenuItemById(itemId) == null);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

	@DeleteMapping("/{menuId}/delete_aditional")
	public Object menuDeleteItemAditional(@PathVariable UUID restaurantId, @PathVariable UUID menuId,
			@RequestBody Map<String, String> body) {
		try {
			this.menuService.deleteAditional(StringUtils.getUUIDFromString(body.get("aditional")));
			return ResponseEntity.ok();
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}
}
