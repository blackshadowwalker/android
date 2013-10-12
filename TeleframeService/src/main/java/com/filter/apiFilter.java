package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class apiFilter implements Filter {

	private FilterConfig filterConfig = null;

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		String browserDet = ((HttpServletRequest) request).getHeader("User-Agent").toLowerCase();
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		String uri = req.getRequestURI();
		String pathInfo = req.getPathInfo();
		
		chain.doFilter(request, response);
		/*
		String reqPath = uri.substring(req.getContextPath().length());
		String api = "/api";
		System.out.println("api="+api);
		if(uri.startsWith(api))
			chain.doFilter(request, response);
		else if(reqPath.toLowerCase().startsWith(api.toLowerCase())){
			System.out.println("RequestDispatcher to api = "+api);
			req.getRequestDispatcher("api").forward(req, resp);
		}
		else
			chain.doFilter(request, response);
		System.out.println("uri="+uri +"  \nreq.getPathInfo="+pathInfo);
		*/
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		filterConfig = arg0;
	}
	
}
