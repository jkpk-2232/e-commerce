package com.utils.myhub.notifications;

import java.util.ArrayList;
import java.util.List;

import com.jeeutils.DateUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.MyHubUtils;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;

public class SendDeviceFCMNotifications extends Thread {
	
	
	String path;

	String message;

	String newMessage;

	List<String> userIdList;

	int pushMsgBatchCount;

	List<ApnsDeviceModel> apnsDeviceModelList;

	int recordNo = 0;

	boolean isSavePushMsgInDB = false;

	public SendDeviceFCMNotifications(List<String> userIdList, String message, int pushMsgBatchCount, String path, boolean isSavePushMsgInDB) {
		this(userIdList, message, pushMsgBatchCount, path, isSavePushMsgInDB, null);
	}

	private SendDeviceFCMNotifications(List<String> userIdList, String message, int pushMsgBatchCount, String path, boolean isSavePushMsgInDB, String vendorFeedId) {

		this.userIdList = userIdList;
		this.message = message;
		this.newMessage = MyHubUtils.getTrimmedTo(this.message, 140);
		this.pushMsgBatchCount = pushMsgBatchCount;
		this.path = path;
		this.isSavePushMsgInDB = isSavePushMsgInDB;

		apnsDeviceModelList = ApnsDeviceModel.getDeviceListByUserIds(this.userIdList);

		if (apnsDeviceModelList.size() > 0) {
			this.start();
		}
	}

	@Override
	public void run() {

		int i = 0;

		List<ApnsMessageModel> apnsMessageList = new ArrayList<ApnsMessageModel>();

		for (; recordNo < this.apnsDeviceModelList.size(); recordNo++) {

			String deviceType = this.apnsDeviceModelList.get(recordNo).getDeviceType();
			String deviceToken = this.apnsDeviceModelList.get(recordNo).getDeviceToken();
			String userId = this.apnsDeviceModelList.get(recordNo).getUserId();

			new SendDeviceSingleFCMNotification(deviceType, deviceToken, this.newMessage, userId, this.path);

			ApnsMessageModel apnsMessage = new ApnsMessageModel();
			apnsMessage.setApnsMessageId(UUIDGenerator.generateUUID());
			apnsMessage.setCreatedAt(DateUtils.nowAsGmtMillisec());
			apnsMessage.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			apnsMessage.setToUserId(userId);
			apnsMessage.setMessage(message);
			apnsMessage.setMessageType("push");

			apnsMessageList.add(apnsMessage);

			i++;

			if (i == this.pushMsgBatchCount) {

				i = 0;

				if (isSavePushMsgInDB) {
					ApnsMessageModel.insertMultiplePushMessage(apnsMessageList);
				}

				apnsMessageList.clear();

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		if (apnsMessageList.size() > 0) {

			if (isSavePushMsgInDB) {
				ApnsMessageModel.insertMultiplePushMessage(apnsMessageList);
			}
		}
	}

}
