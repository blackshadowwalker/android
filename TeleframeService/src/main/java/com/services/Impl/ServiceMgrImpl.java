package com.services.Impl;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.services.ServiceMgr;

@WebService(targetNamespace="http://webservice.teleframe.com", serviceName="TelframeService", 
		portName="TelframeEndpoinInstance", name ="TelframeEndpoint" )  
@SOAPBinding(style = Style.RPC)
public class ServiceMgrImpl implements ServiceMgr {

	public String listService() {
		return null;
	}

}
