/*
 * FileName 	: ITSMonitorEndpointImpl.java
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

import com.services.ITSMonitorEndpoint;
import com.services.UserForm;
import com.services.UserList;
import com.services.base.BaseBean;
import com.services.base.DatabaseTool;
import com.services.base.ErrorUtil;
import com.services.base.baseForm;

@WebService(targetNamespace="http://webservice.teleframe.com", serviceName="ITSMonitorEndpointService", 
		portName="ITSMonitorEndpoinInstance", name ="ITSMonitorEndpoint", 
		wsdlLocation = "wsdl/ITSMonitorEndpoint.wsdl")  
		@SOAPBinding(style = Style.RPC)
		public class ITSMonitorEndpointImpl extends BaseBean implements ITSMonitorEndpoint  {

	@Resource
	WebServiceContext wsctx;

	String sql = "";
	PreparedStatement pstm = null;
	Connection con = null;
	ResultSet rs = null;
	private static String DataTableName = "kk_20130508_165150";
	private static String MsgTableName = "t_a_kk_msg";

	public ITSMonitorEndpointImpl(){
		this.setAuthor("karl");
		this.setAuthorID("001");
		this.setData("2013/10/2 10:00");
		this.setVersion("0.1");
	}

	/*
	 * return  ref ErrorUtil
	 * -1 : user ssid null
	 * 1 : the data is null
	 * 0 : OK
	 * 2 : the status = 0;
	 * 5 : SQLException
	 * 9 : connection is null
	 * */
	public String getWarning(
			@WebParam(name = "ssid")String ssid,
			@WebParam(name = "reqLines")int reqLines, 
			@WebParam(name = "dir")int dir )
	{
		return this.getWarningInfo(ssid, 0, reqLines, dir);
	}

	public int Received(
			@WebParam(name = "ssid")String ssid, 
			@WebParam(name = "msgid")long msgid) 
	{
		JSONObject jsonmsg = new JSONObject();
		UserForm user = UserList.getUser(ssid);
		if(UserList.checkUser(ssid)==false || user==null){
			return -1;
		}

		int lines=0;
		sql = "update "+MsgTableName+" set sended=1 where userid=? and msgid=? ";
		con = DatabaseTool.getConnection();
		if(con==null){
			jsonmsg.put("error", ErrorUtil.CONNECT_NULL);
			jsonmsg.put("msg", "connection is null");
			return -1;
		}
		try {
			pstm = con.prepareStatement(sql);
			pstm.setLong(1, user.getId());
			pstm.setLong(2, msgid);
			lines = pstm.executeUpdate();
			rs.close();
			pstm.close();
			con.close();

		} catch (SQLException e) {
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			System.out.println("SQLException="+e.getMessage());
		}
		return lines;
	}


	public String getWarningInfo(
			@WebParam(name = "ssid")String ssid, 
			@WebParam(name = "fromId")long fromId,
			@WebParam(name = "reqLines")int reqLines, 
			@WebParam(name = "dir")int dir)
	{
		JSONObject jsonmsg = new JSONObject();
		JSONArray jsonEmployeeArray = new JSONArray();

		MessageContext context = wsctx.getMessageContext();
		HttpServletRequest request = (HttpServletRequest)context.get(MessageContext.SERVLET_REQUEST); 
		HttpSession session = request.getSession();
		String path = request.getContextPath();
		String baseServer = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
		String basePath = baseServer+path;
		String itsImageRoot = baseForm.getItsImageRoot();
		String smallImageEnd = baseForm.getSmallImageEnd();
		if(itsImageRoot==null || itsImageRoot.isEmpty())
			itsImageRoot = path;

		UserForm user = UserList.getUser(ssid);
		if(UserList.checkUser(ssid)==false ){
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
			if(rs!=null){
				String msgids = "";
				while(rs!=null && rs.next()){
					msgids += rs.getLong("msgid")+",";
				}
				rs.close();
				if(!msgids.isEmpty()){
					if(msgids.endsWith(",")){
						msgids = msgids.substring(0, msgids.length()-1);
					}

					sql = "select * from "+DataTableName+" where id in ("+msgids+")";
					if(dir>=0){
						sql += " and dir="+dir;
					}
					System.out.println("DataTableName @ "+sql);
					//	jsonmsg.put("sql", sql+msgid);
					pstm = con.prepareStatement(sql);

					rs = pstm.executeQuery();
					if(rs==null){
						jsonmsg.element("error", ErrorUtil.DATA_NULL);
						jsonmsg.element("msg", "kk_20130508_165150 is null where id in "+msgids);
						jsonmsg.put("data", "");
					}else{
						jsonmsg.element("error", ErrorUtil.OK);
						jsonmsg.element("msg", "OK");
						while(rs!=null && rs.next()){
							JSONObject elem = new JSONObject();
							elem.put("id", rs.getLong("id"));
							elem.put("LPNumber", rs.getString("LPNumber"));
							elem.put("dir", rs.getString("dir"));
							elem.put("absTime", rs.getString("absTime"));
							elem.put("location", rs.getString("location"));
							elem.put("shortImageA", baseServer+itsImageRoot+ rs.getString("shortImageA")+smallImageEnd);
							elem.put("ImageUrl", baseServer+itsImageRoot+ rs.getString("shortImageA"));
							jsonEmployeeArray.add(elem);
						}
						if(jsonEmployeeArray.size()>0)
							jsonmsg.put("data", jsonEmployeeArray.toArray());
						else
							jsonmsg.put("data", "");
					}
				}else{
					jsonmsg.put("error", ErrorUtil.DATA_NULL);
					jsonmsg.put("msg", MsgTableName+" is null @ "+MsgTableName+"  user="+user.getUsercode());
				}
			}
			if(rs!=null)
				rs.close();
			pstm.close();

		} catch (SQLException e) {
			jsonmsg.put("error", ErrorUtil.SQLEXCEPTION);
			jsonmsg.put("msg", "SQLException="+e.getMessage());
			e.printStackTrace();
		}
		return jsonmsg.toString();
	}

	public String getWarningFromId(
			@WebParam(name = "ssid")String ssid, 
			@WebParam(name = "fromId")long fromId,
			@WebParam(name = "reqLines")int reqLines, 
			@WebParam(name = "dir")int dir ) 
	{
		return this.getWarningInfo(ssid, fromId, reqLines, dir);
	}



	/*
	 * msgids: json format: [{"id",101},{"id",102},{"id",103}]
	 * */
	@Override
	public int Receiveds(
			@WebParam(name = "ssid")String ssid, 
			@WebParam(name = "msgids")String msgids) 
	{
		int ret = -1;
		JSONObject jsonmsg = new JSONObject();
		JSONArray jsonEmployeeArray = JSONArray.fromObject(msgids);
		for(int i=0;i<jsonEmployeeArray.size();i++){  
			JSONObject jsonObject = JSONObject.fromObject(jsonEmployeeArray.get(i));  
			long id = jsonObject.getLong("id");  
			ret = this.Received(ssid, id);
		}
		return 0;
	}

	@Override
	public String Statistics(
			@WebParam(name = "ssid")String ssid, 
			@WebParam(name = "startTime")long startTime, 
			@WebParam(name = "endTime")long endTime,
			@WebParam(name = "reqLines")int reqLines, 
			@WebParam(name = "dir")int dir, 
			@WebParam(name = "type")String type) 
	{
		return null;
	}


}
