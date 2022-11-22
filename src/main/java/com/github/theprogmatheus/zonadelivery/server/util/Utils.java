package com.github.theprogmatheus.zonadelivery.server.util;

import java.util.Locale;

import com.github.slugify.Slugify;

public class Utils {

	public static final Slugify SLUGIFY = Slugify.builder().locale(Locale.GERMANY).build();
	
	public static final String REGEX_EMAIL = "[a-z0-9]+@[a-z]+\\.[a-z]{2,3}";
	public static final String REGEX_USERNAME = "^[A-Za-z][A-Za-z0-9_]{2,29}$";

}
