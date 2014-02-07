package com.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.services.base.DatabaseTool;
import com.services.base.ErrorUtil;

public class UserLog extends HttpServlet{

	private static Connection con=null;
	private static String sql = "";
	private static PreparedStatement pstm = null;
	private static ResultSet rs=null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		String path = req.getContextPath();
		String baseServer = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort();
		String basePath = baseServer + path+"/";
		
		String usercode = req.getParameter("usercode");
		if(usercode==null || usercode.isEmpty()){
			String url = basePath+"/userlog?usercode=karl";
			out.print("e.g:  <a href='"+basePath+"'>"+basePath+"</a>");
		}

		JSONObject jsonmsg = new JSONObject();
		JSONArray jsonEmployeeArray = new JSONArray();
		jsonmsg.put("error", 0);
		jsonmsg.put("msg", "OK");

		con = DatabaseTool.getConnection();
		if(con!=null){
			sql = "select * from t_a_accessLog where usercode=? order by ctTime desc";
			try {
				pstm = con.prepareStatement(sql);
				pstm.setString(1, usercode);
				rs = pstm.executeQuery();
				if(rs==null){
					jsonmsg.put("error", ErrorUtil.DATA_NULL);
					jsonmsg.put("msg", "DATA_NULL");
				}else
					while(rs!=null && rs.next()){
						JSONObject json = new JSONObject();
						json.put("accessIp", rs.getString("accessIp"));
						json.put("accessTime", rs.getString("ctTime"));
						jsonEmployeeArray.add(json);
					}
				jsonmsg.put("data", jsonEmployeeArray);
				out.print(jsonmsg);

			} catch (SQLException e) {
				jsonmsg.put("error",ErrorUtil.SQLEXCEPTION);
				jsonmsg.put("msg", "SQLEXCEPTION="+e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		super.doPost(req, resp);
	}

}
