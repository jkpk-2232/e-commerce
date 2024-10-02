package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;

public class CreateUserAccountsThread extends Thread {

	private static Logger logger = Logger.getLogger(CreateUserAccountsThread.class);

	private final static int INSERT_BATCH_COUNT = 100;

	public CreateUserAccountsThread() {
		this.start();
	}

	@Override
	public void run() {

		try {
			List<String> roleIdList = new ArrayList<>();

			roleIdList.add(UserRoles.DRIVER_ROLE_ID);
			roleIdList.add(UserRoles.VENDOR_ROLE_ID);

			long urlId = 34;

			List<UserModel> userModelList = UserModel.getUserWithAccountDetailsListByRoleIds(roleIdList, urlId);

			logger.info("\n\n\n userModelList Size: " + userModelList.size() + "\n\n\n");

			if (userModelList != null) {

				List<UserAccountModel> userAccountModelList = new ArrayList<UserAccountModel>();

				List<UrlAccessesModel> urlAccessesModelList = new ArrayList<UrlAccessesModel>();

				long currentTimeInMillies = DateUtils.nowAsGmtMillisec();
				String createdBy = "1";

				logger.info("\n\n\n ===========> currentTimeInMillies: " + currentTimeInMillies + "\n\n\n");
				logger.info("\n\n\n ===========> createdBy: " + createdBy + "\n\n\n");

				for (UserModel userModel : userModelList) {

					if ((userModel.getUserAccountId() == null) || ((userModel.getUserAccountId() != null) && ("".equals(userModel.getUserAccountId().trim())))) {

						UserAccountModel userAccountModel = new UserAccountModel();

						userAccountModel.setUserAccountId(UUIDGenerator.generateUUID());
						userAccountModel.setUserId(userModel.getUserId());
						userAccountModel.setCurrentBalance(0);
						userAccountModel.setHoldBalance(0);
						userAccountModel.setApprovedBalance(0);
						userAccountModel.setTotalBalance(0);
						userAccountModel.setActive(true);
						userAccountModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
						userAccountModel.setCreatedAt(currentTimeInMillies);
						userAccountModel.setCreatedBy(createdBy);
						userAccountModel.setUpdatedAt(currentTimeInMillies);
						userAccountModel.setUpdatedBy(createdBy);

						userAccountModelList.add(userAccountModel);

					}

					if (UserRoles.VENDOR_ROLE_ID.equals(userModel.getRoleId())) {

						if (urlId != userModel.getUrlId()) {

							UrlAccessesModel urlAccessesModel = new UrlAccessesModel();

							urlAccessesModel.setUrlId(34);
							urlAccessesModel.setUserId(userModel.getUserId());
							urlAccessesModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
							urlAccessesModel.setCreatedAt(currentTimeInMillies);
							urlAccessesModel.setCreatedBy(createdBy);
							urlAccessesModel.setUpdatedAt(currentTimeInMillies);
							urlAccessesModel.setUpdatedBy(createdBy);

							urlAccessesModelList.add(urlAccessesModel);
						}
					}
				}

				logger.info("\n\n\n userAccountModelList Size: " + userAccountModelList.size() + "\n\n\n");
				logger.info("\n\n\n urlAccessesModelList Size: " + urlAccessesModelList.size() + "\n\n\n");

				if (userAccountModelList.size() > 0 && userAccountModelList.size() <= INSERT_BATCH_COUNT) {

					logger.info("\n\n\n =========================> Batch insert 1 \n\n\n");

					UserAccountModel.insertAccountBalanceDetailsBatch(userAccountModelList);

				} else {

					logger.info("\n\n\n =========================> Batch insert 2 \n\n\n");

					List<UserAccountModel> subUserAccountModelList = new ArrayList<UserAccountModel>();

					for (UserAccountModel userAccountModel : userAccountModelList) {

						subUserAccountModelList.add(userAccountModel);

						if (subUserAccountModelList.size() == INSERT_BATCH_COUNT) {

							UserAccountModel.insertAccountBalanceDetailsBatch(subUserAccountModelList);

							subUserAccountModelList.clear();

							logger.info("\n\n\n =========================> Sleep time start 5 Sec. 1 \n\n\n");

							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}
					}

					if (subUserAccountModelList.size() > 0) {

						UserAccountModel.insertAccountBalanceDetailsBatch(subUserAccountModelList);
					}

				}

				if (urlAccessesModelList.size() > 0 && urlAccessesModelList.size() <= INSERT_BATCH_COUNT) {

					logger.info("\n\n\n =========================> Batch insert 3 \n\n\n");

					UrlAccessesModel.addUserUrlAccessesBatch(urlAccessesModelList);

				} else {

					logger.info("\n\n\n =========================> Batch insert 4 \n\n\n");

					List<UrlAccessesModel> subUrlAccessesModelList = new ArrayList<UrlAccessesModel>();

					for (UrlAccessesModel urlAccessesModel : urlAccessesModelList) {

						subUrlAccessesModelList.add(urlAccessesModel);

						if (subUrlAccessesModelList.size() == INSERT_BATCH_COUNT) {

							UrlAccessesModel.addUserUrlAccessesBatch(subUrlAccessesModelList);

							subUrlAccessesModelList.clear();

							logger.info("\n\n\n =========================> Sleep time start 5 Sec. 2 \n\n\n");

							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}
					}

					if (subUrlAccessesModelList.size() > 0) {

						UrlAccessesModel.addUserUrlAccessesBatch(subUrlAccessesModelList);
					}
				}

			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}