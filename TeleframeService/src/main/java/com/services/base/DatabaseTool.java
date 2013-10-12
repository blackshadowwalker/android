package com.services.base;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTool {
	
	static JdbcConfig config = null;
	static Connection  connection=null;   
	
	public DatabaseTool()
	{
		config = baseForm.getJdbcConfig();
	}
	
	public static Connection getConnection(){
		
		config = baseForm.getJdbcConfig();
		System.out.println("config.getUrl()="+config.getUrl());
        try{
            Class.forName(config.getDriverClassName());
            connection = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
		return connection;
	}
}
