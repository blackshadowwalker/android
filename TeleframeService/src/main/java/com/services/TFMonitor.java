package com.services;

import javax.jws.WebParam;

//
public interface TFMonitor {

	// return json data
	public String getWarning(String ssid, int reqLines, int dir );
	public String getWarningFromId(String ssid, long fromId, int reqLines, int dir );
	public String getWarningInfo(String ssid, long fromId, int reqLines, int dir);
	
	public int Received(String ssid, long msgid);
	public int Receiveds(String ssid, String msgids);
	
	
	
}
