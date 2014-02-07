package com.services.base;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTool {
	
	static JdbcConfig config = null;
	
	public DatabaseTool()
	{
		config = baseForm.getJdbcConfig();
	}
	
	public static Connection getConnection(){
		Connection  connection=null;   
		config = baseForm.getJdbcConfig();
        try{
            Class.forName(config.getDriverClassName());
            connection = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
		return connection;
	}
}
