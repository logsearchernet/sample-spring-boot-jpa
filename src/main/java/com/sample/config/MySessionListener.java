package com.sample.config;

import java.util.Date;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MySessionListener implements HttpSessionListener {
	
	private static Log logger = LogFactory.getLog(MySessionListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		logger.info("-- sessionCreated --, "+ new Date());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		logger.info("-- sessionDestroyed --, "+ new Date());
	}

}
