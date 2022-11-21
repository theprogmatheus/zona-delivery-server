package com.github.theprogmatheus.zonadelivery.server.util;

import java.util.Locale;

import com.github.slugify.Slugify;

public class Utils {

	public static final Slugify SLUGIFY = Slugify.builder().locale(Locale.GERMANY).build();

}
