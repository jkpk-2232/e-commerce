package com.log;

import java.io.Serializable;

public class LogFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	protected static LogFactory singleton = null;

	protected Log createLog(String classNm) {
		return new Log(classNm);
	}

	public static Log create(String classNm) {

		if (LogFactory.singleton == null) {

			synchronized (LogFactory.class) {

				if (LogFactory.singleton == null) {
					LogFactory.singleton = new LogFactory();
				}
			}
		}

		return singleton.createLog(classNm);
	}

	public static Log create(Class<?> cl) {
		return create(cl.getName());
	}

	public static void setFactory(LogFactory fact) {
		LogFactory.singleton = fact;
	}

}