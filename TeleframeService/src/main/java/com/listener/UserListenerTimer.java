package com.listener;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.services.UserForm;
import com.services.UserList;

public class UserListenerTimer extends TimerTask {
	
	static Logger logger = Logger.getLogger(UserListenerTimer.class);
	private static long timeout=0;
	
	public UserListenerTimer(){
	}
	public UserListenerTimer(long timeout){
		this.timeout = timeout;
	}
	
	@Override
	public void run() {
		Date date = new Date();
		String message = "UserListenerTimer @ "+date.toLocaleString();
		logger.debug(message);
	//	System.out.println(message);
		UserList.UpdateAllUser(timeout);
	}

}
