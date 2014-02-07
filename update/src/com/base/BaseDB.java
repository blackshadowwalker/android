package com.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseDB {

	static Logger logger = Logger.getAnonymousLogger();
	public static String sqlitePath = "";
	public static final String DB_NAME ="update.db";

	public static String init(String srcdbPath, String basePath) throws Exception{
		boolean bGeted = false;
		String dbPath= srcdbPath;
		if(dbPath==null || dbPath.isEmpty()){
			dbPath = basePath+"/WEB-INF"+"/"+DB_NAME;
			File f = new File (dbPath);
			if(f.exists()){
				bGeted = true;
				logger.info("sqlitePath geted @ = "+dbPath);
			}else{
				logger.info("dbPath==null sqlite db is not exist @ "+dbPath);
			}
		}
		if(!bGeted && srcdbPath!=null && !srcdbPath.isEmpty()){
			dbPath = basePath.substring(0,basePath.lastIndexOf("/"))+ "/"+ srcdbPath +"/"+DB_NAME ;
			File f1 = new File (dbPath);
			if(f1.exists()){
				bGeted = true;
				logger.info("sqlitePath geted @ = "+dbPath);
			}else{
				logger.info("sqlite db is not exist @ "+dbPath);
			}
		}
		if(!bGeted){
			logger.info("sqlite db is not exist @ "+dbPath);
			throw new FileNotFoundException("sqlite db is not exist @ "+dbPath);
		}
		sqlitePath = dbPath;
		return "";
	}

	public static Connection getCon(){
		Connection conn=null;
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}  
		try {
			conn = DriverManager.getConnection("jdbc:sqlite://"+sqlitePath);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;  
	}


	public static void LogDB(HttpServletRequest req, HttpServletResponse resp ){

		String ip = req.getRemoteAddr();
		String phone = req.getParameter("phone");
		String latitude = req.getParameter("latitude");
		String longitude = req.getParameter("longitude");
		String version = req.getParameter("version");

		SimpleDateFormat curTime= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS"); 
		String nowTime = curTime.format(new Date());//文件名称    

		Connection con = BaseDB.getCon();
		PreparedStatement pstm  = null;
		if(con!=null){
			String sql = "insert into update_log(ip, phone, latitude, longitude, time, version) values(?,?,?,?,?,?)";
			try {
				pstm = con.prepareStatement(sql);
				pstm.setString(1, ip);
				pstm.setString(2, phone);
				pstm.setString(3, latitude);
				pstm.setString(4, longitude);
				pstm.setString(5, nowTime);
				pstm.setString(6, version);
				int lines = pstm.executeUpdate();
				pstm.close();
				System.out.println("lines="+lines);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
