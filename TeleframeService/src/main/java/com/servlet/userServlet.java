package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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

import org.apache.log4j.Logger;

import com.services.base.ErrorUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UserServlet extends HttpServlet  {

	static Logger logger =  Logger.getLogger(UserServlet.class);
	static ServletConfig config = null;
	private static String userTableName = "t_a_user"; 
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config = config;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	public String list(ServletContext sc) throws Exception{
		JSONObject jsonmsg = new JSONObject();
		JSONArray jsonEmployeeArray = new JSONArray();
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
			DatabaseMetaData dbmd = conn.getMetaData();  
			logger.info("Sqlite.db = ["+dbmd.getURL()+"]");
			System.out.println( "Sqlite.db = ["+dbmd.getURL()+"]");
			
			PreparedStatement pstm =null;
		//	pstm.execute();
			pstm = conn.prepareStatement("select * from "+userTableName);
			ResultSet rs = pstm.executeQuery();
			
			while(rs!=null && rs.next()){
				JSONObject json = new JSONObject();
				json.put("id", rs.getLong("id"));
				json.put("usercode", rs.getString("usercode"));
				json.put("username", rs.getString("username"));
				json.put("password", rs.getString("password")); // rs.getString("password")
				json.put("group", rs.getString("group"));
				json.put("photo", rs.getString("photo"));
				json.put("status",rs.getString("status"));
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
	public String delete(ServletContext sc, HttpServletRequest req){
		JSONObject jsonmsg = new JSONObject();
		String pathInfo = req.getPathInfo();
		String  usercode = pathInfo.substring("/delete".length()+1);
		String sql="";
		PreparedStatement pstm=null;
		if(usercode!=null && !usercode.isEmpty()){
			int index = usercode.indexOf("/");
			if(index>0)
				usercode = usercode.substring(0, index);
			DataSource sqlite = (DataSource)sc.getAttribute("sqlite");
			try{
				Connection conn = sqlite.getConnection();
				if(conn==null){
					logger.error("Connection conn==null @ user.list");
					jsonmsg.element("error", ErrorUtil.CONNECT_NULL);
					jsonmsg.element("msg", "Connection conn==null @ user.list");
					return jsonmsg.toString();
				}
				sql = "delete from "+userTableName+" where usercode=?";
				System.out.println(sql+usercode);
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, usercode);
				int lines = pstm.executeUpdate();
				if(lines==1){
					jsonmsg.element("error", ErrorUtil.SUCCESS);
					jsonmsg.element("msg", "Delete user["+usercode+"] success.");
				}
				else{
					jsonmsg.element("error", ErrorUtil.FAILED);
					jsonmsg.element("msg", "Delete user["+usercode+"] failed.");
				}
				pstm.close();
				conn.close();
			}catch (SQLException e) {
				jsonmsg.element("error", ErrorUtil.SQLEXCEPTION);
				jsonmsg.element("msg", "SQLEXCEPTION  : "+ sql);
				e.printStackTrace();
			}
		}
		return jsonmsg.toString();
	}
	
	public String add(ServletContext sc, HttpServletRequest req){
		JSONObject jsonmsg = new JSONObject();
		String pathInfo = req.getPathInfo();
		String  usercode = pathInfo.substring("/add".length()+1);
		String sql="";
		
		PreparedStatement pstm = null;
		Connection conn = null;
		ResultSet rs = null;
		
		if(usercode!=null && !usercode.isEmpty()){
			int index = usercode.indexOf("/");
			if(index>0)
				usercode = usercode.substring(0, index);
			DataSource sqlite = (DataSource)sc.getAttribute("sqlite");
			try{
				conn = sqlite.getConnection();
				if(conn==null){
					logger.error("Connection conn==null @ user.list");
					jsonmsg.element("error", ErrorUtil.CONNECT_NULL);
					jsonmsg.element("msg", "Connection conn==null @ user.list");
					return jsonmsg.toString();
				}
				long random = (long)(Math.random()*1000000);
				
				sql = "select * from "+userTableName+" where usercode=?";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, usercode);
				rs = pstm.executeQuery();
				if(rs!=null && rs.next()){
					jsonmsg.element("error", ErrorUtil.FAILED);
					jsonmsg.element("msg", "Add user failed. The user["+usercode+"] has been exist.");
					rs.close();
				}
				else{
					String password = String.format("%d", random);
					sql = "insert into "+userTableName+"(usercode, username, password) values(?,?,?);";
					pstm = conn.prepareStatement(sql);
					pstm.setString(1, usercode);
					pstm.setString(2, usercode);
					pstm.setString(3, password);
					
					int lines = pstm.executeUpdate();
					if(lines==1){
						jsonmsg.element("error", ErrorUtil.OK);
						jsonmsg.element("msg", "Add user success. @ "+usercode);
					}
					else{
						jsonmsg.element("error", ErrorUtil.FAILED);
						jsonmsg.element("msg", "Add user failed.");
					}
				}
				pstm.close();
				conn.close();
				
			}catch (SQLException e) {
				jsonmsg.element("error", ErrorUtil.SQLEXCEPTION);
				jsonmsg.element("msg", "SQLEXCEPTION  : "+ sql);
				e.printStackTrace();
			}finally
	        {
				try {
					pstm.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
		}
		return jsonmsg.toString();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ServletContext sc = config.getServletContext();
		String pathInfo = req.getPathInfo();
		System.out.println("User Servlet request with pathinfo = "+pathInfo);
		
		if(pathInfo==null || pathInfo.isEmpty() || pathInfo.equals("/")){
			System.out.println("pathInfo==null");
		//	resp.sendRedirect(sc.getContextPath()+"/modules/user/index.jsp");
			req.getRequestDispatcher("/modules/user/index.jsp").forward(req, resp);
		//	super.doGet(req, resp);
			return;
		}
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text; charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		if(pathInfo.equals("/list")){
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/html; charset=utf-8");
		//	resp.setHeader("Content-Disposition", "attachment;filename=aaa.doc");
			String listuser = "";
			try {
				listuser  = this.list(sc);
			} catch (Exception e) {
				e.printStackTrace();
			}
			req.setAttribute("ret", listuser);
			out.write(listuser);
			out.close();
			//req.getRequestDispatcher("/modules/user/list.jsp").forward(req, resp);
		}else if(pathInfo.startsWith("/add")){
			String ret = this.add(sc, req);
			out.write(ret);
			out.close();
			System.out.println("add user ret="+ret);
			req.setAttribute("ret", ret);
		//	req.getRequestDispatcher("/modules/user/add.jsp").forward(req, resp);
		}else if(pathInfo.startsWith("/delete")){
			String ret = this.delete(sc, req);
			out.write(ret);
			out.close();
			System.out.println("delete user ret="+ret);
			req.setAttribute("ret", ret);
		//	req.getRequestDispatcher("/modules/user/delete.jsp").forward(req, resp);
		}else{
		//	resp.sendRedirect("modules/user/index.jsp");
			//req.getRequestDispatcher("/modules/user/index.jsp").forward(req, resp);
		}
	}

}
