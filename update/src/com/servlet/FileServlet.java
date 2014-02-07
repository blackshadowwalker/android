package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.base.BaseDB;
import com.base.ErrorUtil;
import com.base.Util;

public class FileServlet extends HttpServlet{

	private static String tag = " TAG:"+ FileServlet.class.getPackage()+"."+FileServlet.class.getName() +" ";

	JSONObject jsonmsg = null;
	JSONArray jsonEmployeeArray = null;

	public static void main(String[] args) {
		String source = "first {test} is here,two {test2} is here!";
		String find = "\\{.*\\}";
		Pattern pattern = Pattern.compile(find);
		Matcher matcher = pattern.matcher(source);
		while(matcher.find()) {
			System.out.println(matcher.group());
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();

		System.out.println("-----------------");
		Enumeration reqmap = req.getSession().getAttributeNames();
		while(reqmap.hasMoreElements()){
			String name = (String) reqmap.nextElement();
			Object obj= (Object) req.getSession().getAttribute(name);
			if(obj instanceof String)
				System.out.println(name+"="+(String)obj);
		}
		System.out.println("-----------------");

		String pathInfo = req.getPathInfo();
		System.out.println("FileServlet Servlet request with pathinfo = "+pathInfo);
		if(pathInfo.equals("/save/apk")){
			JSONObject json = saveFileAPK(req, resp);
			out.println(json.toString());
			out.close();
		}

	}

	/*
	 * 保存上传后的文件到 app_update
	 * 所需的参数是在session中获得，用户在submit的时候，在updateServlet中会将所有的参数存入session.
	 * 
	 * */
	public JSONObject saveFileAPK(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException{

		JSONObject jsonret = new JSONObject();
		jsonret.put("error", 0);
		jsonret.put("msg", "OK");

		System.out.println(new Date()+tag+": save File");

		SimpleDateFormat curTime= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS"); 
		String nowTime = curTime.format(new Date());//

		jsonmsg = JSONObject.fromObject( req.getSession().getAttribute("filejson"));
		System.out.println(new Date()+": "+jsonmsg.toString());
		if(jsonmsg==null){
			jsonret.put("error", ErrorUtil.JSON_NULL);
			jsonret.put("msg", "jsonmsg is null");
		}else{
			int  error  = 0; 
			try{
				error = jsonmsg.getInt("error");
			}catch(Exception e){
				e.printStackTrace();
				jsonret.put("error", ErrorUtil.JSON_EXCEPTION_GETINT);
				jsonret.put("msg", "JSON_EXCEPTION_GETINT");
				return jsonret;
			}
			if(error!=0){
				jsonret.put("error", 1002);
				jsonret.put("msg", "jsonmsg error="+error);
			}else{
				jsonEmployeeArray = jsonmsg.getJSONArray("data");
				System.out.println(jsonEmployeeArray.size());
				for(int i=0; i<jsonEmployeeArray.size(); i++){
					JSONObject json = jsonEmployeeArray.getJSONObject(i);
					PreparedStatement pstm  = null;
					String url = null;
					String sql = null;
					String app_name = (String) req.getSession().getAttribute("app_name");
					String name = (String) req.getSession().getAttribute("name");
					String description = (String) req.getSession().getAttribute("description");
					if(app_name==null ||  app_name.isEmpty())
						continue;
					if(description.length()>Util.DESCRIPTION_LENGTH)
						description = description.substring(0,Util.DESCRIPTION_LENGTH);//如果更新描述内容过长，则截断
					
					String versionName = (String) req.getSession().getAttribute("versionName");
					String versionCode = (String) req.getSession().getAttribute("versionCode");
					if(versionName ==null){
						versionName = "";
					}
					if(versionCode ==null){
						versionCode = "";
					}

					System.out.println("app_name="+app_name);
					System.out.println("name="+name);
					System.out.println("description="+description);
					System.out.println("versionName="+versionName);
					System.out.println("versionCode="+versionCode);

					Connection con = BaseDB.getCon();
					System.out.println("geted Connection");
					if(con!=null ){
						if(json.getInt("error")==0){
							url = json.getString("savePath")+"/"+json.getString("savename");
						}
						if(url!=null){
							sql = "select * from app_update where app_name=? and versionName=? and versionCode=? ";
							try{
								pstm = con.prepareStatement(sql);
								if(pstm!=null){
									pstm.setString(1, app_name);
									pstm.setString(2, versionName);
									pstm.setString(3, versionCode);
									ResultSet rs = pstm.executeQuery();
									if(rs!=null && rs.next()){
										System.out.println("versionName:"+rs.getString("versionName"));
										System.out.println("description:"+rs.getString("description"));
										sql = "update app_update set name=?, versionName=?, versionCode=?, url=?, description=?, time=?  where id=? ";
										pstm =  con.prepareStatement(sql);
										pstm.setString(1, name);
										pstm.setString(2, versionName);
										pstm.setString(3, versionCode);
										pstm.setString(4, url);
										pstm.setString(5, description);
										pstm.setString(6, nowTime);
										pstm.setLong(7, rs.getLong("id"));
										rs.close();
									}else{
										sql = "insert into app_update(app_name, name, versionCode, versionName, url, description, time) values(?,?,?,?,?,?,?) ";
										pstm =  con.prepareStatement(sql);
										pstm.setString(1, app_name);
										pstm.setString(2, name);
										pstm.setString(3, versionCode);
										pstm.setString(4, versionName);
										pstm.setString(5, url);
										pstm.setString(6, description);
										pstm.setString(7, nowTime);
									}
									int lines = pstm.executeUpdate();
									pstm.close();
									System.out.println(new Date()+tag+" Lines:"+lines+" @ "+sql);
									if(lines>0){
										jsonret.put("error", ErrorUtil.OK);
										jsonret.put("msg", "OK");
									}else{
										jsonret.put("error", ErrorUtil.FAILED);
										jsonret.put("msg", "FAILED");
										jsonret.put("data", sql);
									}
								}
								con.close();
							} catch (SQLException e) {
								jsonret.put("error", ErrorUtil.FAILED);
								jsonret.put("msg", "FAILED");
								jsonret.put("data", e.getMessage());
								e.printStackTrace();
							}
						}
						try {
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return jsonret;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		this.doPost(req, resp);
	}


	public static JSONObject getAppVersion(String app_name, String basePath, String thisPath){
		JSONObject jsonmsg = new JSONObject();

		Connection con = BaseDB.getCon();
		PreparedStatement pstm  = null;
		String ret = "";
		if(con!=null){
			String sql = "select * from app_update where app_name=? order by id desc limit 1 ";
			try {
				pstm = con.prepareStatement(sql);
				if(pstm!=null){
					pstm.setString(1, app_name);
					ResultSet rs = pstm.executeQuery();
					if(rs!=null && rs.next()){
						
						jsonmsg.put("app_name", app_name);
						jsonmsg.put("name", rs.getString("name"));
						jsonmsg.put("versionCode", rs.getLong("versionCode"));
						jsonmsg.put("versionName", rs.getString("versionName"));
						
						String url = rs.getString("url");
						if(url.startsWith("http://"))
							jsonmsg.put("url", url);//其他服务器路径
						else if(url.startsWith("/"))
							jsonmsg.put("url", basePath + url);//本服务器根路径
						else
							jsonmsg.put("url", basePath +"/"+thisPath+ url);//本项目下的路径
						
						jsonmsg.put("description", rs.getString("description"));
						jsonmsg.put("time", rs.getString("time"));

						rs.close();
					}
					pstm.close();
				}
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return jsonmsg;
	}


}
