package com.webapp.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.actions.BusinessAction;
import com.webapp.daos.UrlDao;

public class UrlModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UrlModel.class);

	private String urlId;
	private long urlGroupId;
	private String urlTitle;
	private String url;
	private long parentUrlId;
	private String urlIcon;
	private boolean isSubMenuOption;

	private String urlGroupName;
	private long menuPriority;

	public static List<UrlModel> getPriorityMenus(String userId, boolean convert) throws SQLException {

		List<UrlModel> urls = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UrlDao urlDao = session.getMapper(UrlDao.class);

		try {
			urls = urlDao.getPriorityMenus(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPriorityMenus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		if (convert) {

			for (UrlModel urlModel : urls) {

				if (String.valueOf(urlModel.getUrlGroupId()).equals("1")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelDashboard"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("2")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelManualBookings"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("4")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelAdminUser"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("5")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelPassengers"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("6")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelDrivers"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("7")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelCars"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("10")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelAnnouncments"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("11")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelRefund"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("15")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelBookings"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("16")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelReports"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("22")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelBookings"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("23")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelDrivers"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("24")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelCars"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("25")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelRideLater"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("26")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelCriticalRideLater"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("27")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelDriverAccounts"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("28")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelVendorAccounts"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("24")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelHoldEncashRequests"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("25")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelApprovedEncashRequests"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("26")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelTransferredEncashRequests"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("27")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelRejectedEncashRequests"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("33")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelDriverAccounts"));
				} else if (String.valueOf(urlModel.getUrlGroupId()).equals("34")) {
					urlModel.setUrlGroupName(BusinessAction.messageForKeyAdmin("labelMyAccount"));
				} 
			}
		}

		return urls;
	}

	public static List<UrlModel> getPriorityMenusBO(String userId, String language) throws SQLException {

		List<UrlModel> urls = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UrlDao urlDao = session.getMapper(UrlDao.class);

		try {
			urls = urlDao.getPriorityMenus(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPriorityMenus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		for (int i = 0; i < urls.size(); i++) {
			if (i == 0) {
				urls.get(i).setUrlGroupName(BusinessAction.messageForKeyAdmin("labelBookACar", language));
			} else if (i == 1) {
				urls.get(i).setUrlGroupName(BusinessAction.messageForKeyAdmin("labelMyBookings", language));
			} else if (i == 2) {
				urls.get(i).setUrlGroupName(BusinessAction.messageForKeyAdmin("labelBusinessOperator", language));
			}
		}

		return urls;
	}

	public static String getFirstPriorityURL(String userId) {

		String url;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UrlDao urlDao = session.getMapper(UrlDao.class);

		try {
			url = urlDao.getFirstPriorityURL(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFirstPriorityURL : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return url;
	}

	public String getUrlIcon() {
		return urlIcon;
	}

	public void setUrlIcon(String urlIcon) {
		this.urlIcon = urlIcon;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public long getUrlGroupId() {
		return urlGroupId;
	}

	public void setUrlGroupId(long urlGroupId) {
		this.urlGroupId = urlGroupId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlTitle() {
		return urlTitle;
	}

	public void setUrlTitle(String urlTitle) {
		this.urlTitle = urlTitle;
	}

	public long getParentUrlId() {
		return parentUrlId;
	}

	public void setParentUrlId(long parentUrlId) {
		this.parentUrlId = parentUrlId;
	}

	public boolean isSubMenuOption() {
		return isSubMenuOption;
	}

	public void setSubMenuOption(boolean isSubMenuOption) {
		this.isSubMenuOption = isSubMenuOption;
	}

	public String getUrlGroupName() {
		return urlGroupName;
	}

	public void setUrlGroupName(String urlGroupName) {
		this.urlGroupName = urlGroupName;
	}

	public long getMenuPriority() {
		return menuPriority;
	}

	public void setMenuPriority(long menuPriority) {
		this.menuPriority = menuPriority;
	}
}