package com.test;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
		
		Connection con=null;
		PreparedStatement pstm=null;
		
		String url = "jdbc:mysql://10.168.1.250:3306/vsimonitor";
		String sql = "";
		String plate = "京AB0005";
		String absTime = "2013-10-03 19:12:52";
		String Location = "大门口";
		String pic = "its_upload/pic/its.jpg";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		int lines=0;
		int dir = 0;
		int level = 0;
		
        try {  
        	Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, "root", "teleframe20130301");
            while(true){
            	absTime = dateFormat.format(new Date());
            	long rand = (long)(Math.random()*10000);
            	plate = "京AB"+rand;
            	Location = "大门口"+rand;
            	dir = (int)(Math.random()*10%2);
            	
            	sql = "INSERT INTO kk_20130508_165150(LPNumber, dir, absTime, location, shortImageA) " +
            			"VALUES('"+plate+"',"+dir+",'"+absTime+"','"+Location+"','"+pic+"')";
            	pstm = con.prepareStatement(sql);
            	lines = pstm.executeUpdate();
            	System.out.println("["+lines+"]"+sql);
            	
            	Location = "资料中心"+rand;
            	level = (int)(Math.random()*10%3);
            	sql = "INSERT INTO monitor_event(eventName, nSeriousLevel, AlarmStartTime, location, personInCharge, nEventDesc,  AlarmStartPic) " +
            			"VALUES ('不明身份人员闯入', "+level+", '"+absTime+"', '"+Location+"', '王XX', " +
            			"'资料室有不明身份人员闯入，资料室负责人:王XX, 电话010-11112222-123', 'its_upload/pic/its.jpg')";
            	pstm = con.prepareStatement(sql);
            	lines = pstm.executeUpdate();
            	System.out.println("["+lines+"]"+sql);
            	
            	Thread.sleep(15*1000);
            }
  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 

}
