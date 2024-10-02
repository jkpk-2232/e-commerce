package com.log;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class LogFileAppender extends FileAppender {

	protected long timeUnit = 60 * 1000; // 1 minute

	protected String origFileName = null;

	protected long timeWhenFileOpened = 0;

	protected long logRollTime = 1 * 60; //1 hour

	protected long interval = logRollTime * timeUnit;

	public LogFileAppender() {
		super();
	}

	private String getLogFileName(String file) {

		this.origFileName = file;

		this.timeWhenFileOpened = System.currentTimeMillis();

		SimpleDateFormat formatter = new SimpleDateFormat("MMM-dd-yyyy-HH");

		String dateStamp = formatter.format(new Date());

		String origFileNm = null;

		if (origFileName.toLowerCase().endsWith(".log")) {
			origFileNm = origFileName.substring(0, origFileName.length() - 4);
		} else {
			origFileNm = origFileName;
		}

		return origFileNm + "." + dateStamp + ".log";
	}

	public void setFile(String file) {
		super.setFile(getLogFileName(file));
	}

	public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) throws IOException {

		super.setFile(fileName, append, bufferedIO, bufferSize);

		if (append) {
			File f = new File(fileName);
			((CountingQuietWriter) qw).setCount(f.length());
		}
	}

	public void rollOver() {

		this.closeFile();

		String fn = getLogFileName(origFileName);

		super.setFile(fn);

		try {
			setFile(fn, false, false, 0);
		} catch (Exception e) {
			LogLog.error("setFile(" + fn + ", false) call failed.", e);
		}
	}

	public void setLogRollTime(long logRollTime) {
		this.logRollTime = logRollTime;
		this.interval = this.logRollTime * this.timeUnit;
	}

	public long getLogRollTime() {
		return logRollTime;
	}

	public void setTimeUnit(long timeUnit) {
		this.timeUnit = timeUnit;
		this.interval = this.logRollTime * this.timeUnit;
	}

	public long getTimeUnit() {
		return timeUnit;
	}

	protected void setQWForFiles(Writer writer) {
		this.qw = new CountingQuietWriter(writer, errorHandler);
	}

	protected void subAppend(LoggingEvent event) {

		super.subAppend(event);

		long toTime = System.currentTimeMillis();

		if ((fileName != null) && ((toTime - timeWhenFileOpened) > this.interval)) {
			this.rollOver();
		}
	}

}