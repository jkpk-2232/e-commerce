package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.AnnouncementDao;

public class AnnouncementModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AnnouncementModel.class);

	private String announcementId;
	private String announcementMessage;
	private boolean isDeleted;

	public int addAnnouncementtMessage() {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AnnouncementDao announcementDao = session.getMapper(AnnouncementDao.class);

		try {
			this.setAnnouncementId(UUIDGenerator.generateUUID());
			this.setCreatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			status = announcementDao.addAnnouncementtMessage(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addAnnouncementtMessage : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<AnnouncementModel> getAnnouncementListForSearch(long startDatelong, long endDatelong, int start, int length, String order, String globalSearchString) {

		List<AnnouncementModel> announcementModel = new ArrayList<AnnouncementModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AnnouncementDao announcementDao = session.getMapper(AnnouncementDao.class);

		try {
			announcementModel = announcementDao.getAnnouncementListForSearch(startDatelong, endDatelong, start, length, order, globalSearchString);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAnnouncementListForSearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return announcementModel;
	}

	public static int getTotalAnnouncementCountBySearch(long startDatelong, long endDatelong, String globalSearch) {

		int announcementCount = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AnnouncementDao announcementDao = session.getMapper(AnnouncementDao.class);

		try {
			announcementCount = announcementDao.getTotalAnnouncementCountBySearch(startDatelong, endDatelong, globalSearch);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalUserCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return announcementCount;
	}

	public static int getAnnouncementCount(long startDatelong, long endDatelong) {

		int announcementCount = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AnnouncementDao announcementDao = session.getMapper(AnnouncementDao.class);

		try {
			announcementCount = announcementDao.getAnnouncementCount(startDatelong, endDatelong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAnnouncementCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return announcementCount;
	}

	public String getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(String announcementId) {
		this.announcementId = announcementId;
	}

	public String getAnnouncementMessage() {
		return announcementMessage;
	}

	public void setAnnouncementMessage(String announcementMessage) {
		this.announcementMessage = announcementMessage;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
