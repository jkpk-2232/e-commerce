package com.jeeutils.db;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import com.utils.myhub.WebappPropertyUtils;
import com.webapp.actions.Action;

public class ConnectionBuilderAction extends Action {

	private static Logger logger = Logger.getLogger(ConnectionBuilderAction.class);

	private static SqlSessionFactory sf;

	private ConnectionBuilderAction() {

		if (sf == null) {

			String resource = "mybatisConfig.xml";

			String databaseId = WebappPropertyUtils.getWebAppProperty("database");

			try {
				InputStream inputStream = Resources.getResourceAsStream(resource);
				sf = new SqlSessionFactoryBuilder().build(inputStream, databaseId);
			} catch (IOException e) {
				logger.debug("Failed to create SqlSessionFactory object...");
				e.printStackTrace();
			}
		}
	}

	private SqlSessionFactory getConnection() {
		return sf;
	}

	public static synchronized SqlSession getSqlSession() {

		ConnectionBuilderAction connection = new ConnectionBuilderAction();

		sf = connection.getConnection();

		SqlSession session = sf.openSession();

		return session;
	}

}