package com.webapp.models;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Notification;
import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.restfb.json.JsonObject;
import com.utils.UUIDGenerator;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.daos.ApnsDeviceDao;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

public class ApnsDeviceModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(ApnsDeviceModel.class);

	public static final String pushNotificationPasswd = "";

	private static final String APP_NAME = "appName";;

	private static final String APP_VERSION = "appVersion";

	private static final String DEVICE_TOKEN = "deviceToken";

	private static final String DEVICE_MODEL = "devicemodel";

	private static final String DEVICE_VERSION = "deviceversion";

	public static final String DEVICE_TYPE = "devicetype";
	public static final String DEVICE_TYPE_ANDROID = "android";
	public static final String DEVICE_TYPE_IOS = "iPhone";
	public static final String DEVICE_TYPE_WIN7 = "win7";

	private String apnsDeviceId;

	private String userId;

	private String appName;

	private String appVersion;

	private String deviceUid;

	private String deviceToken;

	private String deviceName;

	private String deviceModel;

	private String deviceVersion;

	private String deviceType;

	private String apiSessionKey;

	private boolean pushBadge;

	private boolean pushAlert;

	private boolean pushSound;

	private boolean development;

	private boolean deleted;

	private String timezone;

	private String lastNotificationsViewedTimeId;

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getApnsDeviceId() {
		return apnsDeviceId;
	}

	public void setApnsDeviceId(String apnsDeviceId) {
		this.apnsDeviceId = apnsDeviceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getDeviceUid() {
		return deviceUid;
	}

	public void setDeviceUid(String deviceUid) {
		this.deviceUid = deviceUid;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public boolean isPushBadge() {
		return pushBadge;
	}

	public void setPushBadge(boolean pushBadge) {
		this.pushBadge = pushBadge;
	}

	public boolean isPushAlert() {
		return pushAlert;
	}

	public void setPushAlert(boolean pushAlert) {
		this.pushAlert = pushAlert;
	}

	public boolean isPushSound() {
		return pushSound;
	}

	public void setPushSound(boolean pushSound) {
		this.pushSound = pushSound;
	}

	public boolean isDevelopment() {
		return development;
	}

	public void setDevelopment(boolean development) {
		this.development = development;
	}

	public String getLastNotificationsViewedTimeId() {
		return lastNotificationsViewedTimeId;
	}

	public void setLastNotificationsViewedTimeId(String lastNotificationsViewedTimeId) {
		this.lastNotificationsViewedTimeId = lastNotificationsViewedTimeId;
	}

	public String getApiSessionKey() {
		return apiSessionKey;
	}

	public void setApiSessionKey(String apiSessionKey) {
		this.apiSessionKey = apiSessionKey;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public int insertApnsDeviceDetails() {

		int insertStatus = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		ApnsDeviceDao apnsDeviceDao = session.getMapper(ApnsDeviceDao.class);

		try {

			this.setApnsDeviceId(UUIDGenerator.generateUUID());
			this.setCreatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			apnsDeviceDao.deleteApnsDeviceDetails(this.getDeviceToken(), DateUtils.nowAsGmtMillisec());
			apnsDeviceDao.deleteApnsDeviceDetailsByUserId(this.getUserId(), DateUtils.nowAsGmtMillisec());

			insertStatus = apnsDeviceDao.insertApnsDeviceDetails(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertApnsDevice : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return insertStatus;
	}

	public static boolean checkRecordViewedNotificationTime(String userId) {

		boolean checkStatus = false;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		ApnsDeviceDao apnsDeviceDao = session.getMapper(ApnsDeviceDao.class);

		try {
			checkStatus = apnsDeviceDao.checkRecordViewedNotificationTime(userId);
			logger.debug("checkStatus : " + checkStatus);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during RecordViewedNotificationTime : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return checkStatus;
	}

	public static boolean deleteNotificationTime(String userId) {

		boolean deleteStatus = false;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		ApnsDeviceDao apnsDeviceDao = session.getMapper(ApnsDeviceDao.class);

		try {
			int status = apnsDeviceDao.deleteNotificationTime(userId);

			if (status > 0) {
				deleteStatus = true;
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteNotificationTime : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return deleteStatus;
	}

	public static ApnsDeviceModel getDeviseByUserId(String userId) {
		ApnsDeviceModel apnsDeviceModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		ApnsDeviceDao apnsDeviceDao = session.getMapper(ApnsDeviceDao.class);

		try {
			apnsDeviceModel = apnsDeviceDao.getDeviseByUserId(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDeviseByUserId : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return apnsDeviceModel;
	}

	public static ApnsDeviceModel getDeviseByDeviceTokenAndDeviceUid(String deviceToken, String deviceUid) {
		ApnsDeviceModel apnsDeviceModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		ApnsDeviceDao apnsDeviceDao = session.getMapper(ApnsDeviceDao.class);

		try {
			apnsDeviceModel = apnsDeviceDao.getDeviseByDeviceTokenAndDeviceUid(deviceToken, deviceUid);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDeviseByUserId : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return apnsDeviceModel;
	}

	public static long isdevicsTokenExist(String devicsToken) {

		long apnDeviceId = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		ApnsDeviceDao apnsDeviceDao = session.getMapper(ApnsDeviceDao.class);

		try {
			apnDeviceId = apnsDeviceDao.isdevicsTokenExist(devicsToken);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isdevicsTokenExist : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return apnDeviceId;
	}

	public boolean recordViewedNotificationTime() {

		boolean insertStatus = false;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		ApnsDeviceDao apnsDeviceDao = session.getMapper(ApnsDeviceDao.class);

		try {
			int status = apnsDeviceDao.recordViewedNotificationTime(this);

			if (status > 0) {
				insertStatus = true;
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during recordViewedNotificationTime : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return insertStatus;
	}

	public void sendNotification(String apnsMessageId, String notificationType, String message, String sampleKey, String certificatePath) {

		UserModel user = UserModel.getUserAccountDetailsById(this.getUserId());

		if (user != null && user.isNotificationStatus()) {

			logger.debug("Android ===========> : ");

			/**
			 * old method does not work for fcm use sendAndroidFcmNotification
			 **/
			// sendAndroidNotification(message, sampleKey, notificationType, apnsMessageId);

			sendAndroidFcmNotification(message, sampleKey, notificationType, apnsMessageId);

			logger.debug("iphone ============> : ");
			sendIphoneNotification(message, sampleKey, notificationType, apnsMessageId, certificatePath);
		}

	}

	public void sendPushNotification(String apnsMessageId, String notificationType, String message, String sampleKey, String certificatePath, String deviceType) {

		if ("android".equalsIgnoreCase(deviceType)) {
			logger.debug("\n\n\n \t Android ====================> : \n\n\n");
			sendAndroidFcmNotification(message, sampleKey, notificationType, apnsMessageId);
		} else {
			logger.debug("\n\n\n \t iphone =====================> : \n\n\n");
			sendIphoneNotification(message, sampleKey, notificationType, apnsMessageId, certificatePath);
		}
	}

	public void sendAndroidNotification(String messageStr, String sampleKey, String notificationType, String apnsMessageId) {

		try {
			String key = WebappPropertyUtils.getWebAppProperty("notification_key");

			Sender sender = new Sender(key);

			ArrayList<String> devicesList = new ArrayList<String>();
			devicesList.add(this.deviceToken);

			int badgeCount = 0;

			badgeCount = getBadgeCount(this.userId);

			// use this line to send message with payload data
			Message message = new Message.Builder().addData("message", messageStr).addData("badgeCount", "" + badgeCount).build();
			MulticastResult result = sender.send(message, devicesList, 1);

			logger.debug("message  : ===>>> " + message.toString());

			// sender.send(message, this.deviceToken, 1);

			logger.debug("result :========>" + result.toString());
			if (result.getResults() != null) {
				int canonicalRegId = result.getCanonicalIds();
				if (canonicalRegId != 0) {
					logger.debug("canonicalRegId :========>  " + canonicalRegId);
				}
			} else {
				int error = result.getFailure();
				logger.debug("error :========> " + error);
			}
		} catch (Exception e) {
			e.printStackTrace();

			logger.debug("error :" + e);
		}

	}

	private static final List<String> DONT_OVERRIDE_NOTIFICATION_TYPE = Arrays.asList(ProjectConstants.NOTIFICATION_TYPE_MB_NEW_JOB, ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.NOR);

	public void sendAndroidFcmNotification(String messageStr, String sampleKey, String notificationType, String apnsMessageId) {

		try {

			// Only one type of notification handled at app side.
			if ((!StringUtils.validString(sampleKey)) || (!DONT_OVERRIDE_NOTIFICATION_TYPE.contains(sampleKey.trim()))) {
				sampleKey = ProjectConstants.NOTIFICATION_TYPE_GENERAL;
			}

			String url = "https://fcm.googleapis.com/fcm/send";

			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			String key = WebappPropertyUtils.getWebAppProperty("notification_key");

			ArrayList<String> devicesList = new ArrayList<String>();
			devicesList.add(this.deviceToken);

			int badgeCount = 1;

			// badgeCount = getBadgeCount(this.userId);

			// add header
			httpPost.setHeader("Authorization", "key=" + key);
			httpPost.setHeader("Content-Type", "application/json");

			JsonObject body = new JsonObject();
			body.put("to", this.deviceToken);
			body.put("priority", "high");
			// body.put("dry_run", true);

			// JsonObject notification = new JsonObject();
			// notification.put("body", messageStr);
			// notification.put("title", "title string here");
			// notification.put("icon", "myicon");

			JsonObject data = new JsonObject();
			data.put("badgeCount", badgeCount);
			data.put("body", messageStr);
			data.put("notificationType", sampleKey);

			// data.put("key2", "key2");

			// body.put("notification", notification);
			body.put("data", data);

			StringEntity entity = new StringEntity(body.toString(), "UTF8");

			httpPost.setEntity(entity);

			HttpResponse response = client.execute(httpPost);

			logger.info("\n\n\nSending 'POST' request to URL : " + url);
			logger.info("\n\nPost parameters : " + httpPost.getEntity());
			logger.info("\n\nResponse Code : " + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			logger.info(result.toString());

		} catch (Exception e) {
			e.printStackTrace();

			logger.info("\n\nerror :" + e);
		}

	}

	public void sendIphoneNotification(String message, String sampleKey, String notificationType, String apnsMessageId, String certificatePath) {

		// int badgeCount = getBadgeCount(this.userId);

		int badgeCount = 1;

		try {
			send(this.deviceToken, certificatePath, pushNotificationPasswd, Boolean.parseBoolean(WebappPropertyUtils.getWebAppProperty("iosNotificationEnvironmentProduction")), apnsMessageId, badgeCount, message, sampleKey);
		} catch (JSONException e) {

			logger.debug("Error.. " + e);
			e.printStackTrace();
		}
	}

	public static int getBadgeCount(String userId) {

		int badgeCount = 0;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		ApnsDeviceDao apnsDeviceDao = session.getMapper(ApnsDeviceDao.class);

		try {
			badgeCount = apnsDeviceDao.getBadgeCount(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isdevicsTokenExist : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		return badgeCount;

	}

	public static void send(String deviceToken, Object keystore, String password, boolean production, String apnsMessageId, int badgeCount, String message, String sampleKey) throws JSONException {

		/* Build a blank payload to customize */
		PushNotificationPayload payload = PushNotificationPayload.complex();

		/* Customize the payload */
		payload.addAlert(message);
		// payload.addCustomDictionary("mt", sampleKey);
		payload.addCustomDictionary("type", sampleKey);
		payload.addBadge(badgeCount);
		payload.addSound("default");
		// etc.

		logger.info(" \n\n deviceToken = " + deviceToken);

		/* Push your custom payload */
		List<PushedNotification> notifications = new ArrayList<PushedNotification>();
		try {
			notifications = Push.payload(payload, keystore, password, production, deviceToken);
		} catch (CommunicationException e) {
			e.printStackTrace();
		} catch (KeystoreException e) {
			e.printStackTrace();
		}

		for (PushedNotification pushedNotification : notifications) {
			logger.info("\n\n ios getDevice " + pushedNotification.getDevice() + " response =" + pushedNotification.getResponse() + " payload =" + pushedNotification.getPayload() + " identifier + " + pushedNotification.getIdentifier() + " isTransmissionCompleted ="
						+ pushedNotification.isTransmissionCompleted());
			logger.info("\n\n ios Status " + pushedNotification.isSuccessful());

		}

	}

	public static List<ApnsDeviceModel> getDeviceListByUserIds(List<String> userIdList) {

		List<ApnsDeviceModel> apnsDeviceModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("userIdList", userIdList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ApnsDeviceDao apnsDeviceDao = session.getMapper(ApnsDeviceDao.class);

		try {

			apnsDeviceModelList = apnsDeviceDao.getDeviceListByUserIds(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getDeviceListByUserIds : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return apnsDeviceModelList;
	}

	public static List<String> valiadtevaliadteApnsDeviceModel(ApnsDeviceModel apnsDeviceModel) {

		List<String> errorMessages = new ArrayList<String>();

		Validator validator = new Validator();

		validator.addValidationMapping(APP_NAME, APP_NAME, new RequiredValidationRule());
		validator.addValidationMapping(APP_VERSION, APP_VERSION, new RequiredValidationRule());
		validator.addValidationMapping(DEVICE_TOKEN, DEVICE_TOKEN, new RequiredValidationRule());
		validator.addValidationMapping(DEVICE_MODEL, DEVICE_MODEL, new RequiredValidationRule());
		validator.addValidationMapping(DEVICE_VERSION, DEVICE_VERSION, new RequiredValidationRule());

		return errorMessages;

	}
 
	public void sendFCMNotification(String apnsMessageId, String notificationType, String titleKey, String notificationMessage) {
		
		UserModel user = UserModel.getUserAccountDetailsById(this.getUserId());

		if (user != null && user.isNotificationStatus()) {

			logger.debug("Android ===========> : ");
			
			sendFCMNotificationForAndroidAndIphone(notificationMessage, notificationType, titleKey, apnsMessageId);
		}
		
	}
	
	public void sendPushFCMNotification(String apnsMessageId, String notificationType, String message, String titleKey, String deviceType) {

		sendFCMNotificationForAndroidAndIphone(message, notificationType, titleKey, apnsMessageId);
		
	}

	private void sendFCMNotificationForAndroidAndIphone(String notificationMessage, String notificationType, String titleKey, String apnsMessageId) {
		
		try {
			// Path to the service account key file
			FileInputStream serviceAccount = new FileInputStream(WebappPropertyUtils.FIRE_BASE_CERTIFICATE_PATH);
			logger.debug(" \n \n  \t  fire base certificate path ==============>"+WebappPropertyUtils.FIRE_BASE_CERTIFICATE_PATH);
			// Initialize the Firebase Admin SDK
		            FirebaseOptions options = new FirebaseOptions.Builder()
		                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		                    .build();
		            
		            if (FirebaseApp.getApps().isEmpty()) {
			                FirebaseApp.initializeApp(options);
			     }
		            
		            Notification notification = Notification.builder()
		       	                    .setTitle(titleKey)
		       	                    .setBody(notificationMessage)
		       	                    .build();
		         // Create a message to send
		            com.google.firebase.messaging.Message message = com.google.firebase.messaging.Message.builder()
		                    .setToken(this.deviceToken)
		                    .setNotification(notification)
		                   // .putData("test1", "1")
		                   // .putData("test2", "2")
		                    .build();
		            
		            // Send a message to the device corresponding to the provided registration token
		            String res = FirebaseMessaging.getInstance().send(message);

		            // Response contains the message ID string
		            // System.out.println("Successfully sent message: " + res);
			
		} catch (Exception e) {
			e.printStackTrace();

			logger.info("\n\nerror :" + e);
		}
		
		
	}
}