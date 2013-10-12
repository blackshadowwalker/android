package com.services;
import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.Endpoint;


@WebService
@SOAPBinding(style = Style.RPC)
public interface ITSMonitorEndpoint extends TFMonitor  {
	/*
	 * type:  LIST/CHART
	 * LIST: get the detail LIST of report
	 * CHART: Chart analysis
	 * 
	 * Return: Json  
	 */
	public String Statistics(String ssid, long startTime, long endTime, int reqLines, int dir, String type );
	
	
}
