package com.teleframe.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "UserEndpoint", targetNamespace = "http://webservice.teleframe.com")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface UserEndpoint {

	/**
	 * 
	 * @param username
	 * @param password
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(partName = "return")
	public String checkUser(
			@WebParam(name = "username", partName = "username") String username,
			@WebParam(name = "password", partName = "password") String password);

	/**
	 * 
	 * @param ssid
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(partName = "return")
	public String listUser(
			@WebParam(name = "ssid", partName = "ssid") String ssid);

	/**
	 * 
	 * @param ssid
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(partName = "return")
	public String logoff(@WebParam(name = "ssid", partName = "ssid") String ssid);

}
