package com.github.theprogmatheus.zonadelivery.server.util;

public class StringUtils {

	public static String generateRandomString(String chars, int size) {
		StringBuilder stringBuilder = new StringBuilder();

		if (chars != null && size > 0) {
			for (int count = 0; count < size; count++)
				stringBuilder.append(chars.charAt(Utils.RANDOM.nextInt(chars.length())));
		}
		return stringBuilder.toString();
	}
}
