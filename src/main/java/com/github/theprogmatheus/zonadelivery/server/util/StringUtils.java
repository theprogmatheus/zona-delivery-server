package com.github.theprogmatheus.zonadelivery.server.util;

import java.util.UUID;

public class StringUtils {

	public static String generateRandomString(String chars, int size) {
		StringBuilder stringBuilder = new StringBuilder();

		if (chars != null && size > 0) {
			for (int count = 0; count < size; count++)
				stringBuilder.append(chars.charAt(Utils.RANDOM.nextInt(chars.length())));
		}
		return stringBuilder.toString();
	}

	public static UUID getUUIDFromString(String uuidString) {
		try {
			return UUID.fromString(uuidString);
		} catch (Exception exception) {
			return null;
		}
	}

	public static double getDoubleFromString(String doubleString) {
		try {
			return Double.parseDouble(doubleString);
		} catch (Exception exception) {
			return -1;
		}
	}
}
