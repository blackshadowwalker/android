package com.test;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONObject;

public class Test {

	static String path ="http://10.168.1.252:8888/update/?update=true&app_name=TelLPR";
	public static void main(String[] args) {
		
		URL url;
		try {
			url = new URL(path);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.connect();  
			// 获取文件大小  
			int length = conn.getContentLength();  
			conn.setConnectTimeout(5000);
			InputStream is = conn.getInputStream(); 
			BufferedInputStream bis = new BufferedInputStream(is); 
			byte buffer[] = new byte[1024];  
			String msg = "";
			String temp;
			int len=-1;
			while((len =bis.read(buffer))!=-1){ 
				temp = new String(buffer,"UTF-8");
				msg += temp;
			//	System.out.println(temp);
			}
			System.out.println(msg.trim());
			JSONObject json = JSONObject.fromObject(msg.trim());
			
			System.out.println("versionCode: "+json.getLong("versionCode"));
			System.out.println("url        : "+json.getString("url"));
			System.out.println("description: "+json.getString("description"));
			
			System.out.println("DONE!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
