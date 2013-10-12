package com.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class TimerListener implements ServletContextListener {

	Logger logger = null;
	private java.util.Timer userListenerTimer = null;

	public TimerListener() {
		logger= Logger.getLogger(TimerListener.class);
	}

	public void contextDestroyed(ServletContextEvent sc) {
		userListenerTimer.cancel();
	}

	public void contextInitialized(ServletContextEvent event) {

		userListenerTimer = new java.util.Timer(true);
		ServletContext sc = event.getServletContext();
		String userTimeOut = sc.getInitParameter("userTimeOut");
		String par  [] = userTimeOut.split("-");
		if(par.length>0){
			long timeout = 0;
			try{
				timeout = Long.parseLong(par[0]);
			}catch (Exception e) {
				e.printStackTrace();
			}
			if(par.length>=2)
			{
				if(par[1].endsWith("hour"))
					timeout = timeout * 60 * 60 ;
				else if(par[1].endsWith("min"))
					timeout= timeout * 60 ;
				else if(par[1].endsWith("sec"))
					timeout= timeout ;
				else 
					timeout= timeout;
			}else
				timeout= timeout;
			long timesleepForTimer = 30*1000;
			userListenerTimer.schedule(new UserListenerTimer(timeout), 2000, timesleepForTimer);
			System.out.println("userListenerTimer started & timeout= "+timeout+" ["+userTimeOut+"]");
			
		}
	}

}
