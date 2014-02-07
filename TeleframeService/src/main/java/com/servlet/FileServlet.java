package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.services.base.ErrorUtil;

public class FileServlet extends HttpServlet  {

	static Logger logger =  Logger.getLogger(UserServlet.class);
	static ServletConfig config = null;
	private static String fileTableName = "t_u_file";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config = config;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doPost(req, resp);
	}
	
	public String getFilePath(HttpServletRequest req, HttpServletResponse resp){
		ServletContext sc = config.getServletContext();
		JSONObject jsonmsg = new JSONObject();
		JSONArray jsonEmployeeArray = new JSONArray();
		
		String idstr = req.getParameter("id");
		if(idstr==null ||idstr.isEmpty()){
			logger.error("Please set file id.");
			jsonmsg.element("error", ErrorUtil.FAILED);
			jsonmsg.element("msg", "Please set file id.");
			return jsonmsg.toString();
		}
		long id = Long.parseLong(idstr);
		
		DataSource sqlite = (DataSource)sc.getAttribute("sqlite");
		if(sqlite==null){
			logger.error("DataSource sqlite==null @ user.list");
			jsonmsg.element("error", ErrorUtil.DATASOURCE_NULL);
			jsonmsg.element("msg", "DataSource sqlite==null @ user.list.");
			return jsonmsg.toString();
		}
		try {
			Connection conn = sqlite.getConnection();
			if(conn==null){
				logger.error("Connection conn==null @ user.list");
				jsonmsg.element("error", ErrorUtil.CONNECT_NULL);
				jsonmsg.element("msg", "Connection conn==null @ user.list");
				return jsonmsg.toString();
			}
			PreparedStatement pstm =null;
		//	pstm.execute();
			pstm = conn.prepareStatement("select * from "+fileTableName + " where id=? ");
			pstm.setLong(1, id);
			ResultSet rs = pstm.executeQuery();
			
			jsonmsg.element("error", ErrorUtil.DATA_NULL);
			jsonmsg.element("msg", "Sorry, there's no user!");
			
			if(rs!=null && rs.next()){
				JSONObject json = new JSONObject();
				json.put("id", rs.getLong("id"));
				json.put("filename", rs.getString("filename"));
				json.put("filetype", rs.getString("filetype"));
				json.put("path", rs.getString("path"));
				json.put("pathflag", rs.getString("pathflag"));
				json.put("status",rs.getString("status"));
				json.put("ctTime",rs.getString("ctTime"));
				jsonEmployeeArray.add(json);
			}
			rs.close();
			pstm.close();
			conn.close();
			
			if(jsonEmployeeArray.size()>0){
				jsonmsg.element("error", ErrorUtil.OK);
				jsonmsg.element("msg", "There's no error.");
				jsonmsg.element("data", jsonEmployeeArray.toString());
			}else{
				jsonmsg.element("error", ErrorUtil.DATA_NULL);
				jsonmsg.element("msg", "Sorry, there's no user!");
			}
			
		} catch (SQLException e) {
			jsonmsg.element("error", ErrorUtil.SQLEXCEPTION);
			jsonmsg.element("msg", "SQLEXCEPTION");
			e.printStackTrace();
		}
		return jsonmsg.toString();
	}

	public String list(HttpServletRequest req, HttpServletResponse resp){
		ServletContext sc = config.getServletContext();
		JSONObject jsonmsg = new JSONObject();
		JSONArray jsonEmployeeArray = new JSONArray();
		DataSource sqlite = (DataSource)sc.getAttribute("sqlite");
		if(sqlite==null){
			logger.error("DataSource sqlite==null @ user.list");
			jsonmsg.element("error", ErrorUtil.DATASOURCE_NULL);
			jsonmsg.element("msg", "DataSource==null @ user.list.");
			return jsonmsg.toString();
		}
		try {
			Connection conn = sqlite.getConnection();
			if(conn==null){
				logger.error("Connection conn==null @ user.list");
				jsonmsg.element("error", ErrorUtil.CONNECT_NULL);
				jsonmsg.element("msg", "Connection conn==null @ user.list");
				return jsonmsg.toString();
			}
			PreparedStatement pstm =null;
			pstm = conn.prepareStatement("select * from "+fileTableName);
			ResultSet rs = pstm.executeQuery();
			while(rs!=null && rs.next()){
				JSONObject json = new JSONObject();
				json.put("id", rs.getLong("id"));
				json.put("filename", rs.getString("filename"));
				json.put("filetype", rs.getString("filetype"));
				json.put("path", rs.getString("path"));
				json.put("pathflag", rs.getString("pathflag"));
				json.put("status",rs.getString("status"));
				json.put("ctTime",rs.getString("ctTime"));
				jsonEmployeeArray.add(json);
			}
			rs.close();
			pstm.close();
			conn.close();
			
			if(jsonEmployeeArray.size()>0){
				jsonmsg.element("error", ErrorUtil.OK);
				jsonmsg.element("msg", "There's no error.");
				jsonmsg.element("data", jsonEmployeeArray.toString());
			}else{
				jsonmsg.element("error", ErrorUtil.DATA_NULL);
				jsonmsg.element("msg", "Sorry, there's no user!");
			}
		} catch (SQLException e) {
			jsonmsg.element("error", ErrorUtil.SQLEXCEPTION);
			jsonmsg.element("msg", "SQLEXCEPTION");
			e.printStackTrace();
		}
		return jsonmsg.toString();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException 
	{
		ServletContext sc = config.getServletContext();
		String pathInfo = req.getPathInfo();
		System.out.println("File Servlet request with pathinfo = "+pathInfo);
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text; charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		if(pathInfo==null || pathInfo.isEmpty() || pathInfo.equals("/")){
			
			String idstr = req.getParameter("id");
			System.out.println("idstr="+idstr);
			if(idstr!=null && !idstr.isEmpty()){
				out.print(this.getFilePath(req, resp));
				out.close();
			}else{
				System.out.println("pathInfo==null, forward to -> /file/index");
				req.getRequestDispatcher("/file/index").forward(req, resp);
			}
			return;
		}
		if(pathInfo.equals("/index")){
			req.getRequestDispatcher("/modules/file/index.jsp").forward(req, resp);
		}else if(pathInfo.equals("/list")){
			out.print(this.list(req, resp));
			out.close();
		}
	} 

}












