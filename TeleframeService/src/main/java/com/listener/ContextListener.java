package com.listener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.services.base.baseForm;

public class ContextListener  extends HttpServlet 
	implements ServletContextListener{
	
	static Logger logger = Logger.getLogger(ContextListener.class);
	private static String  JDBC_PROPERTIES= "WEB-INF/classes/jdbc.properties";
	static String database="";

	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
	
	public void contextInitialized(ServletContextEvent event) {
		 
	    ServletContext sc = event.getServletContext();

	    // set dataSource
		ApplicationContext ctx = new ClassPathXmlApplicationContext("../applicationContext.xml");  
		DataSource basedb = (DataSource)ctx.getBean("dataSource");  
		DataSource sqlite = (DataSource)ctx.getBean("sqlite");  
		sc.setAttribute("basedb", basedb);
		sc.setAttribute("sqlite", basedb);
		
		String itsImageRoot = sc.getInitParameter("itsImageRoot");
		String smallImageEnd = sc.getInitParameter("smallImageEnd");
		if(smallImageEnd==null || smallImageEnd.isEmpty() )
			smallImageEnd = "";
		if(itsImageRoot==null || itsImageRoot.isEmpty())
			itsImageRoot = sc.getContextPath();
		sc.setAttribute("itsImageRoot", itsImageRoot);
		baseForm.setItsImageRoot(itsImageRoot);
		baseForm.setSmallImageEnd(smallImageEnd);
		System.out.println("itsImageRoot="+baseForm.getItsImageRoot());
		System.out.println("smallImageEnd="+baseForm.getSmallImageEnd());
		
		logger.info("mysql jdbc="+basedb.toString());
		logger.info("sqlite jdbc="+sqlite.toString());
		
		logger.info("ContextInitialized!");
		System.out.println("ContextInitialized");
		
	}
	
	/*
	public void initSqlite(ServletContext sc) throws FileNotFoundException{
		
		String thisRealPath = sc.getRealPath("/");
		String thisPath = sc.getContextPath();
		
		String sqliteDir = thisRealPath+"/WEB-INF";//thisRealPath.substring(0, thisRealPath.lastIndexOf(thisPath));
		String sqlitePath = sqliteDir+"/sqlite.db";
		System.out.println("init sqlite @ "+sqlitePath);
		File file  = new File(sqlitePath);
		if(file.exists()){
			return ;
		}
        
		 try {  
	            long start = System.currentTimeMillis();  
	            // ����SQLite��JDBC  
	            Class.forName("org.sqlite.JDBC");  
	            // ����һ����ݿ���test.db�����ӣ������ھ��ڵ�ǰĿ¼�´���֮  
	            Connection conn = DriverManager.getConnection("jdbc:sqlite://"+sqlitePath);  
	            long end = System.currentTimeMillis();  
	            System.out.println("������ݿ��ļ������Ӻķ�ʱ�䣺" + (end - start));  
	              
	            conn.close();  
	              
	            start = System.currentTimeMillis();  
	            conn = DriverManager.getConnection("jdbc:sqlite://"+sqlitePath);   
	            end = System.currentTimeMillis();  
	            System.out.println("��ݿ����Ӻķ�ʱ�䣺" + (end - start));  
	              
	            start = System.currentTimeMillis();  
	            Statement stat = conn.createStatement();  
	            // ����һ���?����  
	            stat.executeUpdate("create table tbl1(name varchar(20), salary int);");           
	            end = System.currentTimeMillis();  
	            System.out.println("������ķ�ʱ�䣺" + (end - start));  
	              
	            // �������  
	            start = System.currentTimeMillis();  
	            stat.executeUpdate("insert into tbl1 values('ZhangSan',8000);");  
	            stat.executeUpdate("insert into tbl1 values('LiSi',7800);");  
	            stat.executeUpdate("insert into tbl1 values('WangWu',5800);");  
	            stat.executeUpdate("insert into tbl1 values('ZhaoLiu',9100);");  
	            end = System.currentTimeMillis();  
	            System.out.println("����������ݺķ�ʱ�䣺" + (end - start));  
	              
	            start = System.currentTimeMillis();  
	            ResultSet rs = stat.executeQuery("select * from tbl1;"); // ��ѯ���  
	            while (rs.next()) { // ����ѯ������ݴ�ӡ����  
	                System.out.print("name = " + rs.getString("name") + " "); // ������һ  
	                System.out.println("salary = " + rs.getString("salary")); // �����Զ�  
	            }  
	            rs.close();  
	            end = System.currentTimeMillis();  
	            System.out.println("��ѯ��ݺķ�ʱ�䣺" + (end - start));              
	              
	            conn.close(); // ������ݿ������  
	  
	  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	}
	*/

}
