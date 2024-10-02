package com.utils.myhub;

import java.util.ArrayList;
import java.util.List;

import com.webapp.models.UrlAccessesModel;

public class UrlAccessUtils {

	public static void processAdminUrlAccesses(String userId, List<String> accessList) {
		String[] urlIds = accessList.toArray(new String[accessList.size()]);
		UrlAccessesModel.addCustomUrlAccesses(userId, urlIds);
	}

	public static List<String> getUserAccessList(String userId) {

		List<UrlAccessesModel> urlAccesses = UrlAccessesModel.getUserUrlAccesses(userId);

		List<String> accessList = new ArrayList<>();

		for (int i = 0; i < urlAccesses.size(); i++) {
			accessList.add(urlAccesses.get(i).getUrlId() + "");
		}

		return accessList;
	}
}
