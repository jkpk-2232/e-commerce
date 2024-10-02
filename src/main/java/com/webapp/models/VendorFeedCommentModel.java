package com.webapp.models;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.VendorFeedCommentsDao;

public class VendorFeedCommentModel {
	
	private static Logger logger = Logger.getLogger(VendorFeedModel.class);

	private String commentId;
	private String postId;
	private String commentContent;
	private String userId;
	private String parentComment;
	private Timestamp createdAt;
	
	private String userName;
	
	private List<VendorFeedCommentModel> subCommentsList;

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getParentComment() {
		return parentComment;
	}

	public void setParentComment(String parentComment) {
		this.parentComment = parentComment;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	public  String insertVendorFeedComment(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedCommentsDao vendorFeedCommentsDao = session.getMapper(VendorFeedCommentsDao.class);

		this.commentId = UUIDGenerator.generateUUID();

		try {
			vendorFeedCommentsDao.insertVendorFeedComment(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertVendorFeedComment : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.commentId;
	}

	public static List<VendorFeedCommentModel> getVendorFeedCommentsByPostId(String postId, int start, int length) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedCommentsDao vendorFeedCommentsDao = session.getMapper(VendorFeedCommentsDao.class);
		List<VendorFeedCommentModel> vendorfeedCommentList = null;
		
		try {
			vendorfeedCommentList  = vendorFeedCommentsDao.getVendorFeedCommentsByPostId(postId, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedCommentsByPostId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return vendorfeedCommentList;
	}

	public List<VendorFeedCommentModel> getSubCommentsList() {
		return subCommentsList;
	}

	public void setSubCommentsList(List<VendorFeedCommentModel> subCommentsList) {
		this.subCommentsList = subCommentsList;
	}

	public static List<VendorFeedCommentModel> getVendorFeedCommentsByParentComment(String commentId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedCommentsDao vendorFeedCommentsDao = session.getMapper(VendorFeedCommentsDao.class);
		List<VendorFeedCommentModel> vendorfeedCommentList = null;

		try {
			vendorfeedCommentList = vendorFeedCommentsDao.getVendorFeedCommentsByParentComment(commentId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedCommentsByParentComment : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorfeedCommentList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}



}
