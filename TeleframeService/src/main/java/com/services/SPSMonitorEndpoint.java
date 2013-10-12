package com.services;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService(targetNamespace="http://webservice.teleframe.com", serviceName="SPSMonitorEndpointService" )  
@SOAPBinding(style = Style.RPC)
public interface SPSMonitorEndpoint extends TFMonitor {
	
	/*
	 * type:  LIST/CHART
	 * LIST: get the detail LIST of report
	 * CHART: Chart analysis
	 * 
	 * Return: Json  
	 */
	public String Statistics(String ssid, long startTime, long endTime, int reqLines, int level, String type );
}
