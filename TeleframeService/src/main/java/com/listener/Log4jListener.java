package com.listener;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4jListener extends HttpServlet  {

	static Logger logger = Logger.getLogger(Log4jListener.class);
	public static final String log4jdirkey = "log4j";

	public void init(ServletConfig config)
	throws ServletException
	{
		
		ServletContext sc = config.getServletContext();
		String prefix = sc.getRealPath("/");
		String file = config.getInitParameter("log4j");
		String filePath = prefix + file;
		System.out.println("Log4j filePath = "+filePath);
		
		Properties props = new Properties();
		
		String log4jdir = sc.getRealPath("/");
		log4jdir = log4jdir.replaceAll("\\\\", "/");
		String contextPath = sc.getContextPath();
		int index = log4jdir.indexOf(contextPath);
		if(index>0)
			log4jdir = log4jdir.substring(0, index+1);
		
		try
		{
			FileInputStream istream = new FileInputStream(filePath);
            props.load(istream);
            istream.close();
            toPrint(props.getProperty("log4j.appender.file.File"));
            String AlogFile = log4jdir + props.getProperty("log4j.appender.A1.File"); 
            String logFile = log4jdir + props.getProperty("log4j.appender.FILE.File"); 
            props.setProperty("log4j.appender.A1.File",AlogFile);
            props.setProperty("log4j.appender.FILE.File",logFile);
            System.out.println("log4j.appender.A1.File="+props.getProperty("log4j.appender.A1.File"));
            System.out.println("log4j.appender.FILE.File="+props.getProperty("log4j.appender.FILE.File"));

          //  PropertyConfigurator.configure(props);// 
            PropertyConfigurator.configure(filePath);// 

			logger.info("End of Servlet init.");
			System.out.println("Log4j was inited.");

		} catch (Exception e) {
			e.printStackTrace();
		}
		super.init(config);
	}
	
	public static void toPrint(String content) {
        System.out.println(content);
    }


}
