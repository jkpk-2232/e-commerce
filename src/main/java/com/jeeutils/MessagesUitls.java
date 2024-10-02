package com.jeeutils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class MessagesUitls {

	private static Map<String, ResourceBundle> resourceBundles;

	private static ResourceBundle loadBundle(String language) {

		if (resourceBundles == null) {
			resourceBundles = new HashMap<String, ResourceBundle>();
		}

		ResourceBundle currentBundle = resourceBundles.get(language);

		if (currentBundle == null) {
			currentBundle = ResourceBundle.getBundle("i18n.Messages", new Locale(language));
			resourceBundles.put(language, currentBundle);
		}

		return currentBundle;
	}

	public static String messageForKey(String key, String language) {

		ResourceBundle currentBundle = loadBundle(language);

		return currentBundle.getString(key);
	}
}