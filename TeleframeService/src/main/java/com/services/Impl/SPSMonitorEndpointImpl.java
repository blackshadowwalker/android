package com.services.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.services.SPSMonitorEndpoint;
import com.services.UserForm;
import com.services.UserList;
import com.services.base.DatabaseTool;
import com.services.base.ErrorUtil;
import com.services.base.baseForm;

@WebService(targetNamespace="http://webservice.teleframe.com", serviceName="SPSMonitorEndpointService", 
		portName="SPSMonitorEndpointInstance", name ="SPSMonitorEndpoint" )  
		@SOAPBinding(style = Style.RPC)
		public class SPSMonitorEndpointImpl implements SPSMonitorEndpoint {

	@Resource
	WebServiceContext wsctx;

	String sql = "";
	PreparedStatement pstm = null;
	Connection con = null;
	ResultSet rs = null;
	private static String DataTableName = "af_event";
	private static String MsgTableName = "t_a_xf_msg";

	public int Received(@WebParam(name = "ssid")String ssid, @WebParam(name = "msgid")long msgid) {
		JSONObject jsonmsg = new JSONObject();
		UserForm user = UserList.getUser(ssid);
		if(UserList.checkUser(ssid)==false || user==null){
			return -1;
		}
		int lines=0;
		sql = "update "+MsgTableName+" set sended=1 where userid=? and msgid=? ";
		try {
			pstm = con.prepareStatement(sql);
			pstm.setLong(1, user.getId());
			pstm.setLong(2, msgid);
			lines = pstm.executeUpdate();
			rs.close();
			pstm.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQLException="+e.getMessage());
		}
		return lines;
	}

	@Override
	public String getWarning(@WebParam(name = "ssid")String ssid,
			@WebParam(name = "reqLines")int reqLines, 
			@WebParam(name = "level")int level ) {
		return this.getWarningInfo(ssid, 0, reqLines, level);
	}

	@Override
	public String getWarningFromId(@WebParam(name = "ssid")String ssid, 
			@WebParam(name = "fromId")long fromId,
			@WebParam(name = "reqLines")int reqLines, 
			@WebParam(name = "level")int level) 
	{
		return this.getWarningInfo(ssid, fromId, reqLines, level);
	}

	@Override
	public String getWarningInfo(@WebParam(name = "ssid")String ssid, 
			@WebParam(name = "fromId")long fromId,
			@WebParam(name = "reqLines")int reqLines, 
			@WebParam(name = "level")int level) 
	{
		JSONObject jsonmsg = new JSONObject();
		JSONArray jsonEmployeeArray = new JSONArray();

		MessageContext context = wsctx.getMessageContext();
		HttpServletRequest request = (HttpServletRequest)context.get(MessageContext.SERVLET_REQUEST); 
		HttpSession session = request.getSession();
		String path = request.getContextPath();
		String smallImageEnd = baseForm.getSmallImageEnd();
		String baseServer = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
		String basePath = baseServer+path;
		String itsImageRoot = baseForm.getItsImageRoot();
		if(itsImageRoot==null || itsImageRoot.isEmpty())
			itsImageRoot = path;
		
		UserForm user = UserList.getUser(ssid);
		if(UserList.checkUser(ssid, request)==false ){
			jsonmsg.put("users", UserList.getString());
			jsonmsg.put("error", ErrorUtil.SSID_NULL);
			jsonmsg.put("msg", "Please Login @checkUser.");
			return jsonmsg.toString();
		}
		if(user==null){
			jsonmsg.put("error", ErrorUtil.SSID_NULL);
			jsonmsg.put("msg", "Please Login @user==null.");
			return jsonmsg.toString();
		}

		con = DatabaseTool.getConnection();
		if(con==null){
			jsonmsg.put("error", ErrorUtil.CONNECT_NULL);
			jsonmsg.put("msg", "connection is null");
			return jsonmsg.toString();
		}
		try {
			sql = "select msgid from "+MsgTableName+" where userid=? and sended=0 order by msgid desc ";
			if(reqLines>0)
				sql += " limit "+reqLines;
			System.out.println("getWarning @ "+sql);

			pstm = con.prepareStatement(sql);
			pstm.setLong(1, user.getId());
			rs = pstm.executeQuery();

			String msgids = "";
			if(rs!=null){
				while(rs!=null && rs.next()){
					msgids += rs.getLong("msgid")+",";
				}
				rs.close();
				if(!msgids.isEmpty()){
					if(msgids.endsWith(",")){
						msgids = msgids.substring(0, msgids.length()-1);
					}
					msgids = msgids.substring(0, msgids.length());
					sql = "select * from "+DataTableName+" where id in ("+msgids+")";
					System.out.println("DataTableName @ "+sql);

					//		jsonmsg.put("sql", sql+msgid);
					if(level>=0)
						sql += " and nSeriousLevel="+level;
					pstm = con.prepareStatement(sql);

					rs = pstm.executeQuery();
					if(rs==null){
						jsonmsg.element("error", ErrorUtil.DATA_NULL);
						jsonmsg.element("msg", DataTableName+" is null where id in "+msgids);
						jsonmsg.put("data", "");
					}else{
						jsonmsg.element("error", ErrorUtil.OK);
						jsonmsg.element("msg", "OK");
						while(rs!=null && rs.next()){
							JSONObject elem = new JSONObject();
							elem.put("id", rs.getLong("id"));
							elem.put("eventName", rs.getString("eventName"));
							elem.put("level", rs.getString("nSeriousLevel"));
							elem.put("personInCharge", rs.getString("personInCharge"));
							elem.put("absTime", rs.getString("AlarmStartTime"));
							elem.put("location", rs.getString("location"));
							elem.put("desp", rs.getString("nEventDesc"));
							elem.put("shortImage", baseServer+itsImageRoot+ rs.getString("AlarmStartPic")+smallImageEnd);
							elem.put("ImageUrl", baseServer+itsImageRoot+ rs.getString("AlarmStartPic"));
							jsonEmployeeArray.add(elem);
						}
						rs.close();
						if(jsonEmployeeArray.size()>0)
							jsonmsg.put("data", jsonEmployeeArray.toArray());
						else
							jsonmsg.put("data", "");
					}
				}else{
					jsonmsg.put("error", ErrorUtil.DATA_NULL);
					jsonmsg.put("sql", sql+msgids);
					jsonmsg.put("msg", MsgTableName+" is null @ user 'admin'-->INSERT "+MsgTableName+"(userid, msgid,sended) values(?,0,1)");
				}	

			}
			pstm.close();

		} catch (SQLException e) {
			jsonmsg.put("error", ErrorUtil.SQLEXCEPTION);
			jsonmsg.put("msg", "SQLException="+e.getMessage());
			e.printStackTrace();
		}
		return jsonmsg.toString();
	}


	@Override
	public int Receiveds(
			@WebParam(name = "ssid")String ssid, 
			@WebParam(name = "msgids")String msgids) 
	{
		List<Long> ids = new ArrayList<Long>();
		JSONObject jsonmsg = new JSONObject();
		JSONArray jsonEmployeeArray = JSONArray.fromObject(msgids);
		for(int i=0;i<jsonEmployeeArray.size();i++){  
			JSONObject jsonObject = JSONObject.fromObject(jsonEmployeeArray.get(i));  
			long id = jsonObject.getLong("id");
			this.Received(ssid, id);////返回通知服务器接收成功
		}
		return 0;
	}

	@Override
	public String Statistics(
			@WebParam(name = "ssid")String ssid, 
			@WebParam(name = "startTime")long startTime, 
			@WebParam(name = "endTime")long endTime,
			@WebParam(name = "reqLines")int reqLines, 
			@WebParam(name = "level")int level, 
			@WebParam(name = "type")String type) 
	{
		return null;
	}

}
