package com.utils.myhub;

import java.util.List;

import org.apache.log4j.Logger;

import com.webapp.models.VendorFeedCommentModel;

public class VendorFeedCommentsUtils {
	
	private static Logger logger = Logger.getLogger(VendorFeedCommentsUtils.class);

	public static List<VendorFeedCommentModel> getVendorFeedCommentsByPostId(String postId, int start, int length) {
		
		List<VendorFeedCommentModel> vendorFeedCommentList = VendorFeedCommentModel.getVendorFeedCommentsByPostId(postId, start, length);
		for (VendorFeedCommentModel vendorFeedCommentModel : vendorFeedCommentList) {
			
			List<VendorFeedCommentModel> subCommentList = vendorFeedCommentModel.getVendorFeedCommentsByParentComment(vendorFeedCommentModel.getCommentId());
			vendorFeedCommentModel.setSubCommentsList(subCommentList);
		}
		return vendorFeedCommentList;
	}
	
	

}
