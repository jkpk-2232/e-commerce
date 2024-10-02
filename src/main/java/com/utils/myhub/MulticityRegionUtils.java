package com.utils.myhub;

import java.util.ArrayList;
import java.util.List;

import com.jeeutils.DateUtils;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.models.MulticityUserRegionModel;

public class MulticityRegionUtils {

	public static void addUserRegions(List<String> regionList, String loggedInUserId, String userId, String roleId) {

		long currentTime = DateUtils.nowAsGmtMillisec();

		if (regionList.size() > 0) {

			List<MulticityUserRegionModel> multicityDriverRegionModelList = new ArrayList<MulticityUserRegionModel>();

			for (String multicityRegionId : regionList) {

				MulticityUserRegionModel multicityUserRegionModel = new MulticityUserRegionModel();

				multicityUserRegionModel.setMulticityUserRegionId(UUIDGenerator.generateUUID());
				multicityUserRegionModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
				multicityUserRegionModel.setMulticityCityRegionId(multicityRegionId);
				multicityUserRegionModel.setUserId(userId);
				multicityUserRegionModel.setRoleId(roleId);
				multicityUserRegionModel.setCreatedAt(currentTime);
				multicityUserRegionModel.setUpdatedAt(currentTime);
				multicityUserRegionModel.setCreatedBy(loggedInUserId);
				multicityUserRegionModel.setUpdatedBy(loggedInUserId);

				multicityDriverRegionModelList.add(multicityUserRegionModel);
			}

			if (multicityDriverRegionModelList.size() > 0) {

				MulticityUserRegionModel multicityUserRegionModel = new MulticityUserRegionModel();

				multicityUserRegionModel.setUserId(userId);
				multicityUserRegionModel.setUpdatedAt(currentTime);
				multicityUserRegionModel.setUpdatedBy(loggedInUserId);

				multicityUserRegionModel.deleteUserRegions();

				MulticityUserRegionModel.addMulticityUserRegion(multicityDriverRegionModelList);
			}
		}
	}

	public static List<String> getUserAccessRegionList(String userId) {

		List<String> regionList = new ArrayList<String>();

		List<MulticityUserRegionModel> multicityUserRegionModelList = MulticityUserRegionModel.getMulticityUserRegionByUserId(userId);

		if (multicityUserRegionModelList.size() > 0) {

			for (MulticityUserRegionModel multicityUserRegionModel : multicityUserRegionModelList) {

				regionList.add(multicityUserRegionModel.getMulticityCityRegionId());
			}
		}

		return regionList;
	}
}
