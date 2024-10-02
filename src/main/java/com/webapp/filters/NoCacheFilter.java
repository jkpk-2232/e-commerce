package com.webapp.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.apache.log4j.Logger;

public class NoCacheFilter implements ContainerResponseFilter {

	private Logger logger = Logger.getLogger(NoCacheFilter.class);

	@Override
	public void filter(ContainerRequestContext arg0, ContainerResponseContext responseContext) throws IOException {

		logger.info("Settin no cache headers");

		responseContext.getHeaders().add("Pragma", "No-cache");
		responseContext.getHeaders().add("Cache-Control", "no-cache,no-store,max-age=0");
		responseContext.getHeaders().add("Expires", 1);
	}

}