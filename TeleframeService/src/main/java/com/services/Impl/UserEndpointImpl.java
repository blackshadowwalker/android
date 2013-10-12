/*
 * FileName 	: UserEndpointImpl.java
 * Author 		: Karl
 * Author ID	: 001
 * Data 		: 2013/10/2 10:00
 * Updated Data	: 2013/10/2 12:00
 */

package com.services.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.omg.IOP.ServiceContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.services.UserEndpoint;
import com.services.UserForm;
import com.services.UserList;
import com.services.base.BaseException;
import com.services.base.DatabaseTool;
import com.services.base.ErrorUtil;

@WebService(targetNamespace="http://webservice.teleframe.com", serviceName="UserEndpointService", 
			portName="UserEndpoinInstance", name ="UserEndpoint" )  
@SOAPBinding(style = Style.RPC)
//@HandlerChain(file="handler-chain.xml")    // ָ��SOAP handler
public class UserEndpointImpl extends BaseException implements  UserEndpoint {
	
	@Resource
    WebServiceContext wsctx;
	
	String sql = "";
	PreparedStatement pstm = null;
	Connection con = null;
	ResultSet rs = null;
	
	/*
	 * return 
	 * -1 : user ssid null
	 * 1 : the data is null
	 * 0 : OK
	 * 2 : the status = 0;
	 * 5 : SQLException
	 * 9 : connection is null
	 * */
	public String  checkUser(@WebParam(name = "username")String username, 
			@WebParam(name = "password")String password) {
		
		JSONArray jsonEmployeeArray = new JSONArray(); 
		
		MessageContext context = wsctx.getMessageContext();
		HttpServletRequest request = (HttpServletRequest)context.get(MessageContext.SERVLET_REQUEST); 
		HttpSession session = request.getSession();
		String ip = request.getRemoteAddr();
		
		String ret = "";
		
		JSONObject jsonmsg = new JSONObject();
		con = DatabaseTool.getConnection();
		if(con==null){
			jsonmsg.put("error", ErrorUtil.CONNECT_NULL);
			jsonmsg.put("msg", "connect is null");
			jsonmsg.put("data", "");
			return jsonmsg.toString();
		}
		sql = "select * from t_a_user where usercode=? and password=? ";
		System.out.println("checkUser @ "+sql);
		
		try {
			pstm = con.prepareStatement(sql);
			pstm.setString(1, username);
			pstm.setString(2, password);
			rs = pstm.executeQuery();
			if(rs!=null && rs.next()){
				long status = rs.getLong("status");
				if(status==1){
					jsonmsg.element("error", ErrorUtil.OK);
					jsonmsg.element("msg", "OK");
					jsonmsg.element("data", session.getId());
					
					UserForm user = new UserForm();
					user.setId(rs.getLong("id"));
					user.setUsercode(rs.getString("usercode"));
					user.setUsername(rs.getString("username"));
					user.setPassword(rs.getString("password"));
					user.setStatus(rs.getLong("status"));
					user.setSSID(session.getId());
					user.setLastAccessTime(System.currentTimeMillis());
					user.setLoginTime(System.currentTimeMillis());
					user.setIP(ip);
					UserList.addUser(session.getId(), user);
					jsonmsg.element("user", user.getUsercode()+"/"+user.getUsername());
				}
				else{
					jsonmsg.element("error", 2);
					jsonmsg.element("msg", "user has been disabled.");
					jsonmsg.element("data", "");
				}
			}else{
				jsonmsg.element("error", 1);
				jsonmsg.element("msg", "The username or password you entered is incorrect.");
				jsonmsg.element("data", "");
			}
			rs.close();
			pstm.close();
			
		} catch (SQLException e) {
			jsonmsg.element("error", 5);
			jsonmsg.element("msg", "SQLException="+e.getMessage());
			jsonmsg.element("data", "");
			e.printStackTrace();
		}
		return jsonmsg.toString();
	}

	/*
	 * return 
	 * -1 : user ssid null
	 * 1 : the data is null
	 * 0 : OK
	 * 2 : the status = 0;
	 * 5 : SQLException
	 * 9 : connection is null
	 * */
	public String listUser(@WebParam(name = "ssid")String ssid)  {
		
		JSONArray jsonEmployeeArray = new JSONArray(); 
		JSONObject jsonmsg = new JSONObject();
		
		if(UserList.checkUser(ssid)==false){
			jsonmsg.put("error", "-1");
			jsonmsg.put("msg", "Please Login.");
			return jsonmsg.toString();
		}
		
		/*
		MessageContext context = wsctx.getMessageContext();
		HttpServletRequest request = (HttpServletRequest)context.get(MessageContext.SERVLET_REQUEST); 
		HttpSession session = request.getSession();
		System.out.println("listUser session="+session.getId());
		*/
		
		con = DatabaseTool.getConnection();
		if(con==null){
			jsonmsg.put("error", "9");
			jsonmsg.put("msg", "connect is null");
		//	jsonmsg.put(jsonEmployeeArray);
			return jsonmsg.toString();
		}
		sql = "select * from t_a_user ";
		System.out.println("listUser @ "+sql);
		
		/*
		JSONObject jsonSession = new JSONObject();  
		jsonSession.put("session", session.getId());
		jsonEmployeeArray.add(jsonSession);
	*/
		try {
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			
			

			if(rs!=null ){
			//	long cols = rs.getMetaData().getColumnCount();
			//	rs.last();
			//	long rows = rs.getRow();
			//	rs.first();
				jsonmsg.put("error", "0");
				jsonmsg.put("msg", "OK");
				while(rs!=null && rs.next() ){
					JSONObject jsonEmployee = new JSONObject();  
					jsonEmployee.put("id", rs.getLong("id"));
					jsonEmployee.put("usercode", rs.getString("username"));
					jsonEmployee.put("username", rs.getString("username"));
					jsonEmployee.put("password", "******");//rs.getString("password")
					jsonEmployee.put("status", rs.getLong("status"));
					jsonEmployeeArray.add(jsonEmployee);  
				}
				jsonmsg.put("data", jsonEmployeeArray.toArray());
			}
			else{
				jsonmsg.put("error", "1");
				jsonmsg.put("msg", "OK");
				jsonmsg.put("data", "");
			}
			rs.close();
			pstm.close();
			
		} catch (SQLException e) {
			jsonmsg.put("error", "5");
			jsonmsg.put("msg", "SQLException="+e.getMessage());
			e.printStackTrace();
		}
		return jsonmsg.toString();
	}

	public String logoff(@WebParam(name = "ssid")String ssid) {
		UserList.removeUser(ssid);
		return "";
	}

}
