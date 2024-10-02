package com.utils.myhub.notifications;

import java.util.ArrayList;
import java.util.List;

import com.jeeutils.DateUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.AwsSesEmailUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;

public class MyHubNotificationUtils {

	public static void sendEmailToUser(String emailId, String subject, String message) {
		sendEmailToUser(emailId, subject, message, null);
	}

	public static void sendEmailToUser(String emailId, String subject, String message, String cc) {
		AwsSesEmailUtils.sendTextMail(emailId, subject, message, cc);
	}

	public static void sendPushNotificationToUser(String userId, String notificationMessage) {
		sendPushNotificationToUser(userId, null, notificationMessage);
	}

	public static void sendPushNotificationToUser(ApnsDeviceModel apnsDevice, String notificationMessage, String key) {

		if (apnsDevice != null) {
			apnsDevice.sendNotification("1", "Push", notificationMessage, key, WebappPropertyUtils.getWebAppProperty("certificatePath"));
		}
	}

	public static void sendPushNotificationToUser(String userId, String key, String notificationMessage) {

		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(userId);

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(notificationMessage);
		apnsMessage.setMessageType("push");
		apnsMessage.setToUserId(userId);
		apnsMessage.insertPushMessage();

		if (apnsDevice != null) {
			apnsDevice.sendNotification("1", "Push", notificationMessage, key, WebappPropertyUtils.getWebAppProperty("certificatePath"));
		}
	}

	public static void insertSocketNotification(String userId, String message) {

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(message);
		apnsMessage.setMessageType("socket");
		apnsMessage.setToUserId(userId);
		apnsMessage.insertPushMessage();
	}

	public static void sendNotificationForAnnouncements(String announcementMessage, String userRole, List<String> passengerList, List<String> driverList) {

		List<String> toUserList = null;

		int pushMsgBatchCount = Integer.parseInt(WebappPropertyUtils.getWebAppProperty("push_msg_batch"));
		
		if (UserRoleUtils.isPassengerRole(userRole)) {
			
			toUserList = passengerList;
			
			new SendDeviceFCMNotifications(toUserList, announcementMessage, pushMsgBatchCount, WebappPropertyUtils.getWebAppProperty("certificatePath"), true);
		} else {
			toUserList = driverList;

			new SendDeviceNotifications(toUserList, announcementMessage, pushMsgBatchCount, WebappPropertyUtils.getWebAppProperty("certificatePath"), true);
		}

		/*
		if (toUserList.size() > 0) {

			int pushMsgBatchCount = Integer.parseInt(WebappPropertyUtils.getWebAppProperty("push_msg_batch"));

			new SendDeviceNotifications(toUserList, announcementMessage, pushMsgBatchCount, WebappPropertyUtils.getWebAppProperty("certificatePath"), true);
		}
		*/
	}

	public static void sendVendorFeedNotifications(List<String> toUserList, String feedMessage, String certificatePath, boolean isSavePushMsgInDB, String vendorFeedId) {

		List<ApnsDeviceModel> apnsDeviceModelList = ApnsDeviceModel.getDeviceListByUserIds(toUserList);
		List<ApnsMessageModel> apnsMessageList = new ArrayList<ApnsMessageModel>();

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		long createdAt = DateUtils.nowAsGmtMillisec();

		for (ApnsDeviceModel apnsDeviceModel : apnsDeviceModelList) {

			apnsDeviceModel.sendPushNotification("1", "Push", feedMessage, ProjectConstants.NOTIFICATION_TYPE_VENDOR_FEED, certificatePath, apnsDeviceModel.getDeviceType());

			apnsMessage = new ApnsMessageModel();
			apnsMessage.setApnsMessageId(UUIDGenerator.generateUUID());
			apnsMessage.setCreatedAt(createdAt);
			apnsMessage.setUpdatedAt(createdAt);
			apnsMessage.setToUserId(apnsDeviceModel.getUserId());
			apnsMessage.setMessage(feedMessage);
			apnsMessage.setMessageType("push");
			apnsMessage.setExtraInfoId(vendorFeedId);
			apnsMessage.setExtraInfoType(ProjectConstants.NOTIFICATION_TYPE_VENDOR_FEED);

			apnsMessageList.add(apnsMessage);
		}

		if (!apnsMessageList.isEmpty()) {

			if (isSavePushMsgInDB) {
				ApnsMessageModel.insertMultiplePushMessage(apnsMessageList);
			}

			apnsMessageList.clear();
		}
	}

	public static void sendFCMPushNotificationToUser(String userId, String titleKey, String notificationMessage) {
		
		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(userId);

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(notificationMessage);
		apnsMessage.setMessageType("push");
		apnsMessage.setToUserId(userId);
		apnsMessage.insertPushMessage();

		if (apnsDevice != null) {
			apnsDevice.sendFCMNotification("1", "Push", titleKey, notificationMessage);
		}
		
	}
}
