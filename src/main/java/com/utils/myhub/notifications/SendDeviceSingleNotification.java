package com.utils.myhub.notifications;

import com.utils.myhub.MyHubUtils;
import com.webapp.models.ApnsDeviceModel;

public class SendDeviceSingleNotification extends Thread {

	String userId;

	String path;

	String message;

	String deviceToken;

	String deviceType;

	public SendDeviceSingleNotification(String deviceType, String deviceToken, String message, String userId, String path) {

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

		apnsDevice.sendPushNotification("1", "Push", newMessage, "key", this.path, this.deviceType);
	}
}