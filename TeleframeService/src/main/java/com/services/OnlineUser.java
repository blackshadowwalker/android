package com.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OnlineUser  extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String pathInfo = req.getPathInfo();
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text; charset=UTF-8");
		if(pathInfo==null || pathInfo.isEmpty() || pathInfo.endsWith("/")){
			PrintWriter out = resp.getWriter();
			out.write(UserList.getString());
			out.close();
		}else if(pathInfo.endsWith("/clear")){
			UserList.clear();
			System.out.println("UserList.clear()");
			PrintWriter out = resp.getWriter();
			out.write(UserList.getString());
			out.close();
		}else{
			
		}
		
	}
	
	

}
