package com.listener;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4jContext implements ServletContextListener {

	public static final String log4jdirkey = "log4jdir";

	public void contextDestroyed(ServletContextEvent arg0) {
		System.getProperties().remove(log4jdirkey);
	}

	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		String log4jdir = sc.getRealPath("/");
		log4jdir = log4jdir.replaceAll("\\\\", "/");
		String contextPath = sc.getContextPath();
		int index = log4jdir.indexOf(contextPath);
		if(index>0)
			log4jdir = log4jdir.substring(0, index+1);
		System.setProperty(log4jdirkey, log4jdir+"log4j");
		System.setProperty("webapp.root",sc.getRealPath("/"));
		System.out.println("log4jdir=["+System.getProperty(log4jdirkey)+"]");
		System.out.println("webapp.root=["+System.getProperty("webapp.root")+"]");
	}

}
