package com.swinkels.emperio.objects.security;

public abstract class Security {
	private static String key;

	public static String getKey() {
		return key;
	}

	public static void setKey(String pKey) {
		key = pKey;
	}
}
