package com.webapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.UrlSubCategoryDao;

public class UrlSubCategoryModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UrlSubCategoryModel.class);

	private String urlSubCategoryId;
	private String urlId;
	private String urlTitle;
	private String url;
	private String urlIcon;
	private int menuPriority;

	public static List<Map<String, List<UrlSubCategoryModel>>> getSubPriorityMenus(String userId) {

		List<Map<String, List<UrlSubCategoryModel>>> outputMap = new ArrayList<>();

		List<UrlSubCategoryModel> urls = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UrlSubCategoryDao urlSubCategoryDao = session.getMapper(UrlSubCategoryDao.class);

		try {
			urls = urlSubCategoryDao.getSubPriorityMenus(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getSubPriorityMenus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		Map<String, List<UrlSubCategoryModel>> innerMap = new HashMap<>();

		for (UrlSubCategoryModel urlSubCategoryModel : urls) {

			if (innerMap.containsKey(urlSubCategoryModel.getUrlId())) {
				List<UrlSubCategoryModel> innerList = innerMap.get(urlSubCategoryModel.getUrlId());
				innerList.add(urlSubCategoryModel);
			} else {
				List<UrlSubCategoryModel> innerList = new ArrayList<>();
				innerList.add(urlSubCategoryModel);
				innerMap.put(urlSubCategoryModel.getUrlId(), innerList);
				outputMap.add(innerMap);
			}
		}

		logger.info("\n\n\n\n\toutputMap\t" + outputMap.toString());

		return outputMap;
	}

	public String getUrlSubCategoryId() {
		return urlSubCategoryId;
	}

	public void setUrlSubCategoryId(String urlSubCategoryId) {
		this.urlSubCategoryId = urlSubCategoryId;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public String getUrlTitle() {
		return urlTitle;
	}

	public void setUrlTitle(String urlTitle) {
		this.urlTitle = urlTitle;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlIcon() {
		return urlIcon;
	}

	public void setUrlIcon(String urlIcon) {
		this.urlIcon = urlIcon;
	}

	public int getMenuPriority() {
		return menuPriority;
	}

	public void setMenuPriority(int menuPriority) {
		this.menuPriority = menuPriority;
	}
}