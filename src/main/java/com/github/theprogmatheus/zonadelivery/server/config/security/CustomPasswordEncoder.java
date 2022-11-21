package com.github.theprogmatheus.zonadelivery.server.config.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

	private static final CustomPasswordEncoder instance = new CustomPasswordEncoder();

	public static CustomPasswordEncoder getInstance() {
		return instance;
	}

	private MessageDigest sha256, sha512;

	CustomPasswordEncoder() {
		try {
			this.sha256 = MessageDigest.getInstance("SHA-256");
			this.sha512 = MessageDigest.getInstance("SHA-512");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public String encode(CharSequence rawPassword) {
		try {
			if (rawPassword != null) {

				String generatedKey = this.bytesToHex(this.sha256
						.digest((System.nanoTime() + UUID.randomUUID().toString()).getBytes(StandardCharsets.UTF_8)));

				String encodedPassword = this
						.bytesToHex(this.sha512.digest((generatedKey + rawPassword).getBytes(StandardCharsets.UTF_8)));

				return (generatedKey + "@" + encodedPassword);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (rawPassword != null && encodedPassword != null) {

			if (encodedPassword.contains("@")) {
				String[] split = encodedPassword.split("@");

				if (split.length >= 2) {
					String key = split[0];
					String password = split[1];

					return this.bytesToHex(this.sha512.digest((key + rawPassword).getBytes(StandardCharsets.UTF_8)))
							.equals(password);
				}
			}
		}
		return false;
	}

	private String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

}
