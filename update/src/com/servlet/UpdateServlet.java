package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.base.BaseDB;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UpdateServlet extends HttpServlet{

	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String pathinfo = req.getPathInfo();
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text; charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		
		if(pathinfo.endsWith("/log")){
			out.print(getLog(req,resp));
			out.close();
		}
		
	}
	
	public String getLog(HttpServletRequest req, HttpServletResponse resp){
		
		JSONObject jsonmsg = new JSONObject();
		JSONArray jsonEmployeeArray = new JSONArray();
		
		Connection con = BaseDB.getCon();
		PreparedStatement pstm  = null;
		if(con!=null){
			String sql = "select * from update_log order by id desc limit 100 ";
			try {
				pstm = con.prepareStatement(sql);
				ResultSet rs = pstm.executeQuery();
				while(rs!=null && rs.next()){
					JSONObject json = new JSONObject();
					json.put("id", rs.getLong("id"));
					json.put("ip", rs.getString("ip"));
					json.put("phone", rs.getString("phone"));
					json.put("latitude", rs.getString("latitude"));
					json.put("longitude", rs.getString("longitude"));
					json.put("time", rs.getString("time"));
					json.put("version", rs.getString("version"));
					jsonEmployeeArray.add(json);
				}
				rs.close();
				pstm.close();
				con.close();
				jsonmsg.element("error", 0);
				jsonmsg.element("msg", "OK");
				jsonmsg.element("data", jsonEmployeeArray);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return jsonmsg.toString();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}

}
