package com.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.servlet.FileServlet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BaseFilter implements Filter {

	private FilterConfig filterConfig = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	
	public void updateCheck(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		HttpServletResponse resp = (HttpServletResponse)response;
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html");
		
		String app_name = request.getParameter("app_name");
		System.out.println("app_name="+app_name);
		if( app_name==null )
			return ;
		
		PrintWriter out = response.getWriter();
		String thisPath = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
		
		JSONObject appInfo = FileServlet.getAppVersion(app_name, basePath, thisPath);
		
		out.println(appInfo.toString());
	}
	

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		String outBaseString = new Date()+":BaseFileter.doFilter";
		
		HttpServletRequest req = (HttpServletRequest)request;
		System.out.println(outBaseString+".getQueryString =>> "+req.getQueryString());
		
		String update = request.getParameter("update");
		
		if(update!=null && update.equals("true")){
			System.out.println(outBaseString+" : update request @ "+req.getQueryString());
			String logdb = request.getParameter("logdb");
			if(logdb!=null && logdb.endsWith("false"))
				;//do nothing
			else
				BaseDB.LogDB((HttpServletRequest)request, (HttpServletResponse)response);
			updateCheck((HttpServletRequest)request, (HttpServletResponse)response);
		}
		else{
			chain.doFilter(request, response);
		}
	}

	public void destroy() {

	}


}
