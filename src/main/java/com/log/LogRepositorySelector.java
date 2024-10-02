package com.log;

import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.DefaultRepositorySelector;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.RootLogger;

public class LogRepositorySelector implements RepositorySelector {

	private static boolean initialized = false;

	private static Logger guard = null;

	private static Hierarchy repository;

	private static LoggerRepository defaultRepository;

	private static void init() {

		if (!initialized) {

			synchronized (LogRepositorySelector.class) {

				if (!initialized) {
					defaultRepository = LogManager.getLoggerRepository();
					RepositorySelector theSelector = new LogRepositorySelector();
					LogManager.setRepositorySelector(theSelector, guard);
					initialized = true;
				}
			}
		}
	}

	public static void destroy() {

		if (initialized) {

			synchronized (LogRepositorySelector.class) {

				if (initialized) {

					if (repository != null) {
						repository.shutdown();
						repository = null;
					}

					DefaultRepositorySelector theSelector = new DefaultRepositorySelector(defaultRepository);

					LogManager.setRepositorySelector(theSelector, guard);

					initialized = false;
				}
			}
		}
	}

	public static void init(URL loggerCfgFile) {

		init();

		Hierarchy hierarchy = getHierarchy();

		try {
			PropertyConfigurator conf = new PropertyConfigurator();
			conf.doConfigure(loggerCfgFile, hierarchy);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void init(String loggerCfgFile) {

		init();

		Hierarchy hierarchy = getHierarchy();

		try {
			PropertyConfigurator conf = new PropertyConfigurator();
			conf.doConfigure(loggerCfgFile, hierarchy);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void init(Properties props) {

		init();

		Hierarchy hierarchy = getHierarchy();

		PropertyConfigurator conf = new PropertyConfigurator();

		conf.doConfigure(props, hierarchy);
	}

	public synchronized static Hierarchy getHierarchy() {

		if (repository == null) {
			repository = new Hierarchy(new RootLogger(Level.INFO));
		}

		return repository;
	}

	public LoggerRepository getLoggerRepository() {
		return getHierarchy();
	}

}