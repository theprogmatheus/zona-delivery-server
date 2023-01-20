package com.github.theprogmatheus.zonadelivery.server.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.theprogmatheus.zonadelivery.server.enums.UserRole;

@Secured(value = { UserRole.USER_ROLE_NAME, UserRole.EMPLOYEE_ROLE_NAME })
@RestController
@RequestMapping("/restaurant/{restaurantId}/employee/delivery")
public class RestaurantEmployeeDeliveryController {

	// aqui vai ter quase todos os endepoints dos entregadores..

}
