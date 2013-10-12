package com.listener;

import javax.jms.Session;
import javax.servlet.http.HttpSession;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class SessionContext {
	private static SessionContext instance;
	private static HashMap mymap;

	private SessionContext() {
		mymap = new HashMap();
	}

	public static SessionContext getInstance() {
		if (instance == null) {
			instance = new SessionContext();
		}
		return instance;
	}

	public static synchronized void AddSession(HttpSession session) {
		if (session != null) {
			mymap.put(session.getId(), session);
		}
	}

	public static synchronized void DelSession(HttpSession session) {
		if (session != null) {
			mymap.remove(session.getId());
		}
	}
	public static void pringlnSessions(){
		Collection sss = mymap.values();
		Iterator it = sss.iterator();
		while(it.hasNext()){
			HttpSession s = (HttpSession)it.next();
			System.out.println(s.getId());
		}
	}
	public static synchronized HttpSession getSession(String session_id) {
		//pringlnSessions();
		if (session_id == null) return null;
		return (HttpSession) mymap.get(session_id);
	}

}