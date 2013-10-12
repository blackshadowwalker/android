package com.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Endpoint;

import com.services.Impl.ITSMonitorEndpointImpl;



public class ReleaseWebService extends HttpServlet implements ServletContextListener  {

	public void init() throws ServletException {
	}

	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@SuppressWarnings("restriction")
	public void contextInitialized(ServletContextEvent event) {
		  ServletContext sc = event.getServletContext();
		  String scheme = sc.getServletContextName();
		  String thisPath = sc.getContextPath();
	//	  String hs = ServletContext.getServerInfo();
		  System.out.println("Endpoint.publish @ "+thisPath +"  sc="+sc.getServerInfo());
	//	 Endpoint.publish("http://10.168.1.110:7070/ITSWebSoap/services.ITSWebSoapService", (new ITSMonitorEndpointImpl())); 
		  
	//	  Endpoint.publish("http://10.168.1.110:8080/java6ws/services.Java6WebService", new Java6WebService()); 
		//  System.out.println("Endpoint.publish : http://10.168.1.110:8080/java6ws/services.Java6WebService");
		
	}

}
