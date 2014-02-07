package com.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.listener.UserListenerTimer;
import com.services.base.DatabaseTool;
import com.services.base.ErrorUtil;

public class UserList {
	
	private static Logger logger = Logger.getLogger(UserList.class);
	private static Map<String, UserForm> usermap = new HashMap<String, UserForm>();
	private static Map<String, UserForm> oldUsermap = new HashMap<String, UserForm>();
	
	private static Connection con=null;
	private static String sql = "";
	private static PreparedStatement pstm = null;
	
	public UserList(){}
	
	public static boolean checkUser(String ssid){
		UserForm user = usermap.get(ssid);
		if(user!=null){
			user.setLastAccessTime(System.currentTimeMillis());
			return true;
		}
		return false;
	}
	public static boolean checkUser(String ssid, HttpServletRequest request){
		UserForm user = usermap.get(ssid);
		if(user!=null){
			user.setLastAccessTime(System.currentTimeMillis());
			if(request!=null){
				if(!request.getRemoteAddr().equals(user.getLastAccessIp())){
					con = DatabaseTool.getConnection();
					if(con!=null){
						sql = "insert into  t_a_accessLog(usercode, accessIp) values(?, ?)";
						try {
							pstm = con.prepareStatement(sql);
							pstm.setString(1, user.getUsercode());
							pstm.setString(2, request.getRemoteAddr());
							pstm.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					user.setLastAccessIp(request.getRemoteAddr());
				}
			}
			return true;
		}
		return false;
	}
	
	public static void clear(){
		usermap.clear();
		logger.info("usermap.clear()");
	}
	
	public static String getString() {
		
		List<Map.Entry<String,UserForm>> mappingList = null; 
		mappingList = new ArrayList<Map.Entry<String,UserForm>>(usermap.entrySet()); 
		
		System.out.println("mappingList size = "+mappingList.size());
		//通过比较器实现比较排序 
		Collections.sort(mappingList, new Comparator<Map.Entry<String,UserForm>>(){ 
			@Override
			public int compare(Entry<String, UserForm> o1,Entry<String, UserForm> o2) {
				return (int) (o2.getValue().getLastAccessTime() - o1.getValue().getLastAccessTime());
			} 
		}); 
		
		JSONObject jsonmsg = new JSONObject();
		JSONArray jsonEmployeeArray = new JSONArray();
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		jsonmsg.put("error", 0);
		jsonmsg.put("msg", "OK");
		
		for(Map.Entry<String,UserForm> mapping:mappingList){ 
			JSONObject json = new JSONObject();
			UserForm user = mapping.getValue();
			json.put("Ssid", user.getSSID());
			json.put("UserCode", user.getUsercode());
			json.put("UserName", user.getUsername());
			json.put("LastAccessTime", dFormat.format(new Date(user.getLoginTime())));
			json.put("LastAccessIp", user.getLastAccessIp());
			json.put("LoginTime", dFormat.format(new Date(user.getLoginTime())));
			json.put("LoginIp", user.getIP());
			json.put("LastLoginTime", dFormat.format(new Date(user.getLastLoginTime())));
			json.put("LastLoginIp", user.getLastIP());
			jsonEmployeeArray.add(json);
        }
        jsonmsg.put("data", jsonEmployeeArray.toString());
        System.out.println(jsonmsg.toString());
        logger.debug(jsonmsg.toString());
		return jsonmsg.toString();
	}

	public static UserForm getUser(String ssid){
		return usermap.get(ssid);
	}
	
	/*
	 * return key by usercode
	 */
	public static String getUserByUsercode(String usercode){
		Set<String> keys = usermap.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();) {
            String key = (String) it.next();
            UserForm user = usermap.get(key);
            if(user.getUsercode().endsWith(usercode)){
            	return key;
            }
        }
        return null;
	}
	
	public static void addUser(String key, UserForm user){
		String k = getUserByUsercode(user.getUsercode());
		if(k!=null){
			user.setLastLoginTime(usermap.get(k).getLoginTime());
			user.setLastIP(usermap.get(k).getIP());
			usermap.remove(k);
		}
		usermap.put(key, user);
		System.out.println("Added User " + user.getUsercode()+"  SSID="+key);
	
	}
	public static void removeUser(String ssid){
		usermap.remove(ssid);
	}
	
	public static long getUserId(String ssid){
		UserForm user = usermap.get(ssid);
		if(user!=null)
			return user.getId();
		else
			return 0;
	}

	public static Map<String, UserForm> getUsermap() {
		return usermap;
	}

	public static void setUsermap(Map<String, UserForm> user_map) {
		usermap = user_map;
	}
	
	public static void UpdateAllUser(long timeout){
		long curtime = System.currentTimeMillis();
		Set<String> key = usermap.keySet();
        for (Iterator it = key.iterator(); it.hasNext();) {
            String s = (String) it.next();
            UserForm user = usermap.get(s);
            if(user!=null){
            	if(user.getLastAccessTime()==0){
            		user.setLastAccessTime(curtime);
            		continue;
            	}
            	long span = curtime - user.getLastAccessTime();
            //	System.out.println("timeout="+timeout+"  curtime="+curtime+" user.getLastAccessTime="+user.getLastAccessTime()+"  span="+span);
				if(span>= timeout*1000){
					String msg = "remove user @ "+user.getUsercode()+" "+user.getUsername();
					logger.debug(msg);
					System.out.println(msg);
					usermap.remove(s);
				}
			}
        }
	}

	public static String listOnlineUser(){
		JSONObject jsonmsg = new JSONObject();
		JSONArray jsonEmployeeArray = new JSONArray();
		Set<String> key = usermap.keySet();
		if(usermap.size()>0){
			jsonmsg.element("error", ErrorUtil.OK);
			jsonmsg.element("msg", "OK");
	        for (Iterator it = key.iterator(); it.hasNext();) {
	            String s = (String) it.next();
	            UserForm user = usermap.get(s);
	            if(user!=null){
	            	JSONObject json = new JSONObject();
	            	json.put("usercode", user.getUsercode());
	            	json.put("username", user.getUsername());
	            	jsonEmployeeArray.add(json);
				}
	        }
	        jsonmsg.element("data", jsonEmployeeArray.toString());
		}else{
			jsonmsg.element("error", ErrorUtil.DATA_NULL);
			jsonmsg.element("msg", "Online user is NULL");
			
		}
		logger.debug("online user @ \n"+jsonmsg.toString());
        return jsonmsg.toString();
	}
	
	
}
