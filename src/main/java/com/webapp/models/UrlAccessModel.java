package com.webapp.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.jeeutils.exceptions.DatabaseOperationException;
import com.utils.UUIDGenerator;
import com.utils.dbsession.DbSession;
import com.webapp.daos.UrlAccessDao;

public class UrlAccessModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UrlAccessModel.class);

	private String urlAccessId;
	private long roleId;
	private long urlId;

	public String getUrlAccessId() {
		return urlAccessId;
	}

	public void setUrlAccessId(String urlAccessId) {
		this.urlAccessId = urlAccessId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public long getUrlId() {
		return urlId;
	}

	public void setUrlId(long urlId) {
		this.urlId = urlId;
	}

	public static boolean addAccess(long urlId, String roleId, String actorId) {

		boolean result = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UrlAccessDao urlAccessDao = session.getMapper(UrlAccessDao.class);

		try {
			UrlAccessModel model = new UrlAccessModel();
			long currentTime = DateUtils.nowAsGmtMillisec();
			model.setCreatedAt(currentTime);
			model.setUpdatedAt(currentTime);
			model.setCreatedBy(actorId);
			model.setRoleId(Long.parseLong(roleId));
			model.setRecordStatus("A");
			model.setUpdatedBy(actorId);
			model.setUrlAccessId(UUIDGenerator.generateUUID());
			model.setUrlId(urlId);
			result = urlAccessDao.addAccess(model);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addAccess : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return result;
	}

	public static boolean hasAccess(String url, String roleId) {

		boolean hasAccess = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UrlAccessDao urlAccessDao = session.getMapper(UrlAccessDao.class);

		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("url", url);
			map.put("roleId", roleId);
			hasAccess = urlAccessDao.hasAccess(map);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during hasAccess : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return hasAccess;
	}

	public static boolean userHasAccessToUrl(String roleId, String url) {

		boolean userHasAccessToUrl = false;

		String enableAccessControl = "";

		if (enableAccessControl == null || !"yes".equalsIgnoreCase(enableAccessControl)) {
			userHasAccessToUrl = true;
			return userHasAccessToUrl;
		}

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UrlAccessDao urlAccessDao = session.getMapper(UrlAccessDao.class);

		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("url", url);
			map.put("roleId", roleId);
			userHasAccessToUrl = urlAccessDao.hasAccess(map);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during userHasAccessToUrl : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userHasAccessToUrl;
	}

	public static boolean userHasAccessToUrl(HttpServletRequest request, HttpServletResponse response, String url) {

		boolean userHasAccessToUrl = false;

		DbSession dbSession = DbSession.getSession(request, response, false);

		if (dbSession != null) {
			userHasAccessToUrl = userHasAccessToUrl(dbSession.getUserId(), url);
		}

		return userHasAccessToUrl;
	}

	public static boolean allGroupHasAccess(List<UrlModel> urls, String roleId) {

		if (urls == null) {
			return false;
		}

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		boolean hasAccess = true;

		try {

			UrlAccessDao urlAccessDao = session.getMapper(UrlAccessDao.class);

			for (UrlModel urlModel : urls) {

				Map<String, String> map = new HashMap<String, String>();
				map.put("url", urlModel.getUrl());
				map.put("roleId", roleId);

				if (!urlAccessDao.hasAccess(map)) {
					hasAccess = false;
					break;
				}
			}

		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured during allGroupHasAccess", e);
			throw new DatabaseOperationException(e);
		} finally {
			session.close();
		}

		return hasAccess;
	}
}