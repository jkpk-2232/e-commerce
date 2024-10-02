package com.utils.myhub.notifications;

import com.utils.myhub.MyHubUtils;
import com.webapp.models.ApnsDeviceModel;

public class SendDeviceSingleFCMNotification extends Thread {
	
	String userId;

	String path;

	String message;

	String deviceToken;

	String deviceType;

	public SendDeviceSingleFCMNotification(String deviceType, String deviceToken, String message, String userId, String path) {

		this.deviceType = deviceType;
		this.deviceToken = deviceToken;
		this.userId = userId;
		this.message = message;
		this.path = path;

		this.start();
	}

	@Override
	public void run() {

		ApnsDeviceModel apnsDevice = new ApnsDeviceModel();

		apnsDevice.setUserId(this.userId);
		apnsDevice.setDeviceToken(this.deviceToken);

		String newMessage = MyHubUtils.getTrimmedTo(this.message, 140);

		apnsDevice.sendPushFCMNotification("1", "Push", newMessage, "Announcement",  this.deviceType);
	}

}
