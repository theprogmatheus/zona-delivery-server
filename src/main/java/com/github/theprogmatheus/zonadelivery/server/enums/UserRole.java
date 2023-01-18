package com.github.theprogmatheus.zonadelivery.server.enums;

public enum UserRole {

	ADMIN, USER, EMPLOYEE;

	private RoleAuthority[] authorities;

	private UserRole(RoleAuthority... authorities) {
		this.authorities = authorities;
	}

	public RoleAuthority[] getAuthorities() {
		return authorities;
	}

	public String toRoleName() {
		return "ROLE_" + this.name();
	}

	public static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";
	public static final String USER_ROLE_NAME = "ROLE_USER";
	public static final String EMPLOYEE_ROLE_NAME = "ROLE_EMPLOYEE";

}
