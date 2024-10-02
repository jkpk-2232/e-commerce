package com.log;

import java.io.File;

import org.apache.log4j.helpers.LogLog;

public class Log4jPropertyChangeListener extends Thread {

	public static final long DEFAULT_DELAY = 60000L;

	protected String filename;

	protected long delay;

	File file;

	long lastModif;

	boolean fileExistWarnedAlready;

	boolean interrupted;

	boolean isBusy;

	protected Log4jPropertyChangeListener() {
		super();
		delay = 60000L;
		lastModif = 0L;
		fileExistWarnedAlready = false;
		interrupted = false;
		isBusy = false;
		setDaemon(true);
		init();
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public void setFileName(String fileName) {
		this.filename = fileName;
		file = new File(fileName);
	}

	protected synchronized void init() {

		boolean fileExists;

		if (file == null)
			return;

		try {
			fileExists = file.exists();
		} catch (SecurityException e) {
			LogLog.warn("Was not allowed to read check file existance, file:[" + filename + "].");
			interrupted = true;
			return;
		}

		if (fileExists) {

			long l = file.lastModified();

			if (l > lastModif) {

				System.out.println("log4j.properties File has been modified.");

				fileExistWarnedAlready = false;

				boolean ret = onChange();

				if (ret)
					lastModif = l;
			}
		} else if (!fileExistWarnedAlready) {
			LogLog.debug("[" + filename + "] does not exist.");
			fileExistWarnedAlready = true;
		}

	}

	public void stopListener() {
		interrupted = true;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void run() {

		while (!interrupted) {

			try {
				Thread.currentThread();
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}

			if (!interrupted) {
				init();
			}
		}

		LogLog.debug("Log Listener stops.");
	}

	public boolean onChange() {

		isBusy = true;

		LogLog.debug("Sync log4j configuration with file:" + filename);

		try {
			LogRepositorySelector.init(filename);
		} catch (Exception ex) {
			LogLog.debug("Log4j setting is incorrect, will check it again later. Error message:" + ex.getMessage());
			return false;
		}

		isBusy = false;

		return true;
	}

}