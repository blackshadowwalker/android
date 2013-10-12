import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.teleframe.webservice.UserEndpoint;
import com.teleframe.webservice.UserEndpointService;


public class TestUser {

	public static void main(String[] args) {
		
		/*
		Authenticator auth = new Authenticator()
		{
			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication("admin", "admin".toCharArray());
			}
		};
		Authenticator.setDefault(auth);
		*/
		
		UserEndpointService service = new UserEndpointService();
		UserEndpoint userEndpoint = service.getUserEndpoinInstance();
		
		String checkUser = userEndpoint.checkUser("karl", "karl");
		System.out.println(checkUser);
		JSONObject jsonuser = JSONObject.fromObject(checkUser);
		long error = jsonuser.getLong("error");// 返回0表示成功
		String msg = jsonuser.getString("msg");// 与error对应的错误解释
		String ssid = jsonuser.getString("data");//分配给用户的SSID, 此处返回的SSID在以后的调用中都会用到
		System.out.println("error="+error);
		System.out.println("msg="+msg);
		System.out.println("data="+jsonuser.getString("data"));
	
		String userInfo = userEndpoint.listUser(ssid);
		System.out.println(userInfo);
		JSONObject jsonuserlist = JSONObject.fromObject(userInfo);
		
		if(jsonuserlist.get("data").toString().isEmpty())
		{
			System.out.println("data is null");
			return;
		}

		System.out.println("error="+jsonuserlist.getLong("error"));
		System.out.println("msg="+jsonuserlist.getString("msg"));
		System.out.println("data="+jsonuserlist.get("data").getClass());
		
		JSONArray jsonArray = (JSONArray) jsonuserlist.getJSONArray("data");

		String split = "\t";
		if(jsonArray.size()>0){
			JSONObject jsonSession =  JSONObject.fromObject(jsonArray.get(0)); 
			System.out.println(jsonSession);
			System.out.printf("id\t%-15s\t%-15s\t%-15s\tstatus \n", "usercode", "username", "password");  
			for(int i=1;i<jsonArray.size();i++){  
				JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(i));  
				long id = jsonObject.getLong("id");  
				String usercode = jsonObject.getString("usercode");  
				String password = jsonObject.getString("password");  
				String username = jsonObject.getString("usercode");  
				long status = jsonObject.getLong("status");  
				System.out.printf("%d\t%-15s\t%-15s\t%-15s\t-%d \n", id, usercode, username, password, status);  
			}  
		}
		
	}

}
