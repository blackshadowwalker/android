package com.listener;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.xml.bind.Marshaller.Listener;

import com.services.base.JdbcConfig;
import com.services.base.baseForm;

public class JdbcListener  extends HttpServlet implements ServletContextListener{


	private static String  JDBC_PROPERTIES= "WEB-INF/jdbc.properties";
	private static Map<String, String> KeyMap = new HashMap<String, String>(); 
	
	public void initJdbcConfig(ServletContext sc){
		
		Properties prop = new Properties();
		InputStream in;
		JdbcConfig config=new JdbcConfig();
		try {
			in = new BufferedInputStream (new FileInputStream(sc.getRealPath("/")+JDBC_PROPERTIES));
			prop.load(in);
			config.setJdbcname(prop.getProperty(JdbcConfig.JDBC_NAME));
			config.setDriverClassName(prop.getProperty(JdbcConfig.JDBC_DRIVERCLASSNAME));
			config.setUrl(prop.getProperty(JdbcConfig.JDBC_URL));
			config.setUsername(prop.getProperty(JdbcConfig.JDBC_USERNAME));
			config.setPassword(prop.getProperty(JdbcConfig.JDBC_PASSWORD));
			baseForm.setJdbcConfig(config);
			System.out.println("set jdbc config @"+config.getUrl());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void contextInitialized(ServletContextEvent event) {
		
		ServletContext sc = event.getServletContext();
		String jdbcconfig = sc.getInitParameter("jdbcConfig");
		if(jdbcconfig!=null && !jdbcconfig.isEmpty())
			JDBC_PROPERTIES = jdbcconfig;
		
		initJdbcConfig(sc);
		
		Properties prop = new Properties();
		InputStream in;
		try {
			in = new BufferedInputStream (new FileInputStream(sc.getRealPath("/")+JDBC_PROPERTIES));
			prop.load(in);
			Set keyValue = prop.keySet();
			System.out.println("=============== Jdbc Config ============================");
			for (Iterator it = keyValue.iterator(); it.hasNext();)
			{
				String key = (String) it.next();
				String value = prop.getProperty(key);
				KeyMap.put(key, value);
				System.out.println(key+"="+value);
			}
			
			System.out.println("========================================================");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	
	public static void main(String[] args) throws IOException {
		
		Properties prop = new Properties();
		InputStream in = new BufferedInputStream (new FileInputStream(JDBC_PROPERTIES));
		prop.load(in);
		Set keyValue = prop.keySet();
		for (Iterator it = keyValue.iterator(); it.hasNext();)
		{
			String key = (String) it.next();
			String value = prop.getProperty(key);
			KeyMap.put(key, value);
			System.out.println(key+"="+value);
		}

	}
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
