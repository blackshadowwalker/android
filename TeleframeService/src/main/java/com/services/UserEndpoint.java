package com.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.services.base.DatabaseTool;

@WebService
@SOAPBinding(style = Style.RPC)
public interface  UserEndpoint  {
	
	public String	checkUser(String username, String password);
	public String 	listUser(String ssid);
	public String	logoff(String ssid);

}
