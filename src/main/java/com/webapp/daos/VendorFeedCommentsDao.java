package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorFeedCommentModel;

public interface VendorFeedCommentsDao {

	void insertVendorFeedComment(VendorFeedCommentModel vendorFeedCommentModel);

	List<VendorFeedCommentModel> getVendorFeedCommentsByPostId(@Param("postId") String postId, @Param("start") int start, @Param("length") int length);

	List<VendorFeedCommentModel> getVendorFeedCommentsByParentComment(@Param("commentId") String commentId);

}
