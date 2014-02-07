package com.listener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import com.base.BaseDB;


public class ContextListener  extends HttpServlet 
implements ServletContextListener{

	static Logger logger = Logger.getAnonymousLogger();

	public void contextDestroyed(ServletContextEvent arg0) {

	}

	public void contextInitialized(ServletContextEvent event) {

		ServletContext sc = event.getServletContext();
		String basePath = sc.getRealPath("/");
		basePath = basePath.replaceAll("\\\\", "/");
		if(basePath.endsWith("/"))
			basePath = basePath.substring(0,basePath.length()-1);

		initDB(sc, basePath);

		logger.info("ContextInitialized!");

	}


	public void initDB(ServletContext sc, String basePath ){
		// set dataSource
		String dbPath = sc.getInitParameter("dbUri");
		logger.info("dbPath = "+dbPath);

		try {
			BaseDB.init(dbPath, basePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
