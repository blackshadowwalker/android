package com.services;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.services.base.baseForm;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JSONArray jsonEmployeeArray = new JSONArray(); 
		
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("satus", 1);
		jsonEmployeeArray.add(jsonObject2);
        jsonObject.put("username", "huangwuyi");
        jsonObject.put("sex", "ÄÐ");
        jsonObject.put("QQ", "413425430");
        jsonObject.put("Min.score", new Integer(99));
        jsonObject.put("nickname", "ÃÎÖÐÐÄ¾³");
        
        
        jsonEmployeeArray.add(jsonObject);
        
        System.out.println("jsonObject£º" + jsonEmployeeArray.toString());

	}

}
