package com.webapp.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.UrlAccessesDao;

public class UrlAccessesModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UrlAccessesModel.class);

	private String urlAccessId;
	private String userId;
	private long urlId;

	private String url;

	public static void addCustomUrlAccesses(String userId, String[] URLS) {

		UrlAccessesModel urlAccessesModel = new UrlAccessesModel();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UrlAccessesDao urlAccessesDao = session.getMapper(UrlAccessesDao.class);

		try {

			urlAccessesDao.deleteUrlAccesses(userId);

			urlAccessesModel.setUserId(userId);
			urlAccessesModel.setRecordStatus("A");
			urlAccessesModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			urlAccessesModel.setUpdatedBy(userId);
			urlAccessesModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
			urlAccessesModel.setCreatedBy(userId);

			for (int i = 0; i < URLS.length; i++) {
				urlAccessesModel.setUrlId(Integer.parseInt(URLS[i]));
				urlAccessesDao.addUserUrlAccesses(urlAccessesModel);
			}

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addCustomUrlAccesses : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public int addUserUrlAccesses(String userId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UrlAccessesDao urlAccessesDao = session.getMapper(UrlAccessesDao.class);

		try {

			long count = 10;

			for (int i = 0; i < 3; i++) {

				count++;
				this.setUserId(userId);
				this.setUrlId(count);
				this.setRecordStatus("A");
				this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				this.setCreatedAt(DateUtils.nowAsGmtMillisec());

				status = urlAccessesDao.addUserUrlAccesses(this);
			}

			count = 18;
			this.setUserId(userId);
			this.setUrlId(count);
			this.setRecordStatus("A");
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setCreatedAt(DateUtils.nowAsGmtMillisec());

			status = urlAccessesDao.addUserUrlAccesses(this);

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getToursByPassengerId : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static int addUserUrlAccessesBatch(List<UrlAccessesModel> urlAccessesModelList) throws IOException {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UrlAccessesDao urlAccessesDao = session.getMapper(UrlAccessesDao.class);

		try {
			status = urlAccessesDao.addUserUrlAccessesBatch(urlAccessesModelList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addUserUrlAccessesBatch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<UrlAccessesModel> getUserUrlAccesses(String userId) {

		List<UrlAccessesModel> urlAccessesList = new ArrayList<UrlAccessesModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UrlAccessesDao urlAccessesDao = session.getMapper(UrlAccessesDao.class);

		try {
			urlAccessesList = urlAccessesDao.getUserUrlAccesses(userId);
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserUrlAccesses : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return urlAccessesList;
	}

	public static boolean hasUserUrlAccess(String url, String userId) {

		boolean hasAccess = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UrlAccessesDao urlAccessesDao = session.getMapper(UrlAccessesDao.class);

		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("url", url);
			map.put("userId", userId);
			hasAccess = urlAccessesDao.hasUserurlAccess(map);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during hasUserurlAccess : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return hasAccess;
	}

	public String getUrlAccessId() {
		return urlAccessId;
	}

	public void setUrlAccessId(String urlAccessId) {
		this.urlAccessId = urlAccessId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getUrlId() {
		return urlId;
	}

	public void setUrlId(long urlId) {
		this.urlId = urlId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}