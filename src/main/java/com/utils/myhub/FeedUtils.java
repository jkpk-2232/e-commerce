package com.utils.myhub;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.utils.myhub.notifications.MyHubNotificationUtils;
import com.webapp.ProjectConstants;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.VendorFeedLikeModel;
import com.webapp.models.VendorFeedModel;
import com.webapp.models.VendorFeedViewModel;
import com.webapp.models.VendorSubscriberModel;

public class FeedUtils {

	private static Logger logger = Logger.getLogger(FeedUtils.class);

	public static String insertFeed(String vendorId, String feedName, String feedMessage, String hiddenFeedBanerImage, String loggedInUserId, String mediaType, String isSponSored, String vendorStoreId, String vendorProductId) {

		VendorFeedModel vendorFeedModel = new VendorFeedModel();
		vendorFeedModel.setVendorId(vendorId);
		vendorFeedModel.setFeedName(feedName);
		vendorFeedModel.setFeedMessage(feedMessage);
		vendorFeedModel.setFeedBaner(hiddenFeedBanerImage);
		vendorFeedModel.setMediaType(mediaType);
		vendorFeedModel.setIsSponsored(Boolean.parseBoolean(isSponSored));
		vendorFeedModel.setVendorStoreId(vendorStoreId);
		vendorFeedModel.setVendorProductId(vendorProductId);

		return insertFeed(vendorFeedModel, loggedInUserId, vendorId);
	}

	public static String insertFeed(VendorFeedModel vendorFeedModel, String loggedInUserId, String vendorId) {

		vendorFeedModel.setVendorId(vendorId);

		String vendorFeedId = vendorFeedModel.insertVendorFeed(loggedInUserId);

		FeedUtils.sendVendorFeedNotificationToSubscribers(vendorFeedId);

		return vendorFeedId;
	}

	public static void deleteFeeds(String vendorFeedId) {

		VendorFeedModel vfm = new VendorFeedModel();
		vfm.setVendorFeedId(vendorFeedId);
		vfm.deleteVendorFeedByVendorFeedId();

		ApnsMessageModel apnsMessageModel = new ApnsMessageModel();
		apnsMessageModel.setExtraInfoId(vendorFeedId);
		apnsMessageModel.deleteVendorFeedsByVendorFeedId();
	}

	public static void sendVendorFeedNotificationToSubscribers(String vendorFeedId) {

		VendorFeedModel vfm = VendorFeedModel.getVendorFeedDetailsByFeedId(vendorFeedId);
		if (vfm == null) {
			return;
		}

		try {

			Runnable r1 = () -> {

				int pushMsgBatchCount = Integer.parseInt(WebappPropertyUtils.getWebAppProperty("push_msg_batch"));
				String certificatePath = WebappPropertyUtils.getWebAppProperty("certificatePath");

				int start = 0;
				int length = pushMsgBatchCount;
				List<String> toUserList = new ArrayList<>();
				List<VendorSubscriberModel> vsm = new ArrayList<>();

				String feedMessage = MyHubUtils.getTrimmedTo(vfm.getFeedMessage(), 140);

				while (true) {

					vsm = VendorSubscriberModel.getVendorSubscribersByVendorId(vfm.getVendorId(), start, length);

					if (vsm.isEmpty()) {
						vfm.setFeedNotificationStatus(ProjectConstants.VENDOR_FEED_STATUS.SUCCESS);
						vfm.updateFeedNotificationStatus();
						break;
					}

					vsm.parallelStream().forEach(vendorSubcriberModel -> toUserList.add(vendorSubcriberModel.getUserId()));

					if (toUserList.size() > 0) {
						MyHubNotificationUtils.sendVendorFeedNotifications(toUserList, feedMessage, certificatePath, true, vfm.getVendorFeedId());
					}

					start += length;
				}
			};

			Thread t1 = new Thread(r1);
			t1.setName("Vendor-Feed-Notification-Thread-vendorFeedId-" + vendorFeedId);
			t1.start();

		} catch (Exception e) {
			logger.error("\nException sendVendorFeedNotificationToSubscribers :: " + e);
			vfm.setFeedNotificationStatus(ProjectConstants.VENDOR_FEED_STATUS.FAILED);
			vfm.updateFeedNotificationStatus();
		}
	}

	public static void markFeedAsViewed(String vendorFeedId, String loggedInuserId, VendorFeedModel vfm) {

		VendorFeedViewModel vfvm = new VendorFeedViewModel();
		vfvm.setVendorFeedId(vendorFeedId);
		vfvm.setUserId(loggedInuserId);
		vfvm.setVendorId(vfm.getVendorId());
		vfvm.insertVendorFeedView(loggedInuserId);

		int vendorFeedViewCount = VendorFeedViewModel.getVendorFeedViewsCount(vendorFeedId);
		vfm.setFeedViewsCount(vendorFeedViewCount);
		vfm.updateFeedViewsCount(loggedInuserId);
	}

	public static void likeFeed(String vendorFeedId, String loggedInuserId, VendorFeedModel vfm) {

		VendorFeedLikeModel vflm = new VendorFeedLikeModel();
		vflm.setVendorId(vfm.getVendorId());
		vflm.setUserId(loggedInuserId);
		vflm.setVendorFeedId(vendorFeedId);
		vflm.insertVendorFeedLike(loggedInuserId);

		updateLikesCountInVendorFeeds(vendorFeedId, loggedInuserId, vfm);
	}

	public static void disLikeFeed(String vendorFeedId, String loggedInuserId, VendorFeedModel vfm) {

		VendorFeedLikeModel vflm = new VendorFeedLikeModel();
		vflm.setVendorFeedId(vendorFeedId);
		vflm.setUserId(loggedInuserId);
		vflm.deleteVendorFeedLikeByUserId();

		updateLikesCountInVendorFeeds(vendorFeedId, loggedInuserId, vfm);
	}

	private static void updateLikesCountInVendorFeeds(String vendorFeedId, String loggedInuserId, VendorFeedModel vfm) {
		int vendorFeedLikeCount = VendorFeedLikeModel.getVendorFeedLikesCount(vendorFeedId);
		vfm.setFeedLikesCount(vendorFeedLikeCount);
		vfm.updateFeedLikesCount(loggedInuserId);
	}
}
