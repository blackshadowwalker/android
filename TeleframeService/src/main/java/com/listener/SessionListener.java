package com.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener
{
	public static Map userMap = new HashMap();  
	private   SessionContext myc=SessionContext.getInstance();  
	
	public void sessionCreated(HttpSessionEvent httpSessionEvent)
	{
		HttpSession session = httpSessionEvent.getSession();
		myc.AddSession(session); 
		System.out.println("Add Session: "+session.getId());
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent)
	{
		HttpSession session = httpSessionEvent.getSession();  
		myc.DelSession(session);
		System.out.println("Del Session: "+session.getId());
	}

}
