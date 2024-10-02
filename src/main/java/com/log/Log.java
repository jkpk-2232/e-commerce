package com.log;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;

@SuppressWarnings("serial")
public class Log implements Serializable {

	private static boolean initialized = false;

	private static int traceLevel = 3;

	static Log4jPropertyChangeListener chgListener = null;

	private Logger log4jLogger = null;

	public Log(Class<?> c) {
		this(c.getName());
	}

	public Log(String name) {
		if (!Log.isInitialized()) {
			init();
		}
		this.log4jLogger = Logger.getLogger(name);
	}

	private void init() {

		ClassLoader cl = Log.class.getClassLoader();

		URL aUrl = cl.getResource("log4j.properties");

		int refresh = 60;

		if (aUrl == null || aUrl.getPath().endsWith("!/log4j.properties")) {

			String logHome = System.getProperty("log.dir");

			if (logHome != null) {

				try {
					File f = new File(logHome + "/log4j.properties");

					if (f.exists()) {
						aUrl = f.getCanonicalFile().toURI().toURL();
						refresh = 60;
					}

				} catch (Exception e) {
					System.out.println("Error: Cannot open log4j.properties:" + e.getMessage());
				}
			}
		}

		if (aUrl != null && aUrl.getPath().endsWith("!/log4j.properties") == false) {

			Log.init(aUrl, refresh);

		} else {

			Properties props = new Properties();
			props.setProperty("log4j.rootCategory", "debug, stdout");
			props.setProperty("log4j.appender.stdout", ConsoleAppender.class.getName());
			props.setProperty("log4j.appender.stdout.layout", RollingFileAppender.class.getName());
			String defaultPattern = "LOG> %p %d{HH:mm:ss} %c> %m%n";
			props.setProperty("log4j.appender.stdout.layout.ConversionPattern", defaultPattern);
			LogRepositorySelector.init(props);
			Log.initialized = true;
		}

	}

	public static void init(URL fileNm, int refresh) {

		Properties props = new Properties();

		destroy();

		if (refresh > 0) {

			if (chgListener == null) {
				chgListener = new Log4jPropertyChangeListener();
				chgListener.setFileName(fileNm.getFile());
				chgListener.setDelay(refresh * 1000);
				chgListener.init();
				chgListener.start();
			} else {
				chgListener.setFileName(fileNm.getFile());
				chgListener.setDelay(refresh * 1000);
				chgListener.init();
			}

		} else {

			LogRepositorySelector.init(fileNm);
		}

		try {
			props.load(fileNm.openStream());
		} catch (IOException e) {
			System.out.println("Error: Cannot open log4j.properties:" + e.getMessage());
		}

		String dl = props.getProperty("log4j.category.debugDepth", "3");

		traceLevel = Integer.parseInt(dl);

		Log.initialized = true;

	}

	public static boolean isInitialized() {
		return Log.initialized;
	}

	public boolean isLogEnabled() {
		return log4jLogger.isInfoEnabled();
	}

	public static void destroy() {

		if (chgListener != null) {

			chgListener.stopListener();

			while (chgListener.isBusy()) {
			}
			;

			chgListener = null;
		}
	}

	public static int getTraceLevel() {
		return traceLevel;
	}

	public static void setDebugDepth(int level) {
		traceLevel = level;
	}

	public boolean isTraceLevel(int level) {
		return log4jLogger.isDebugEnabled() && traceLevel >= level;
	}

	public void log(CharSequence msg) {
		log4jLogger.info(msg);
	}

	public void log(Throwable e) {
		log4jLogger.error("Exception : ", e);
	}

	public void log(CharSequence msg, Throwable e) {
		log4jLogger.error(msg, e);
	}

	public void trace(int level, CharSequence msg) {

		if (isTraceLevel(level)) {
			log4jLogger.debug(msg);
		}
	}

	public void trace(int level, Object o) {

		if (isTraceLevel(level)) {
			log4jLogger.debug(o);
		}
	}

	public void trace(int level, CharSequence msg, Throwable e) {

		if (isTraceLevel(level)) {
			log4jLogger.error(msg + " Exception: ", e);
		}
	}

}