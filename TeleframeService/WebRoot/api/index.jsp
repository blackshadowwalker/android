<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
 <body>
    <h2>This is Service API Page. </h2>
<pre>
Write-BY: Karl
Data	: 2013.10.3
</pre>
<hr/>
    <pre>

WebService URL : http://10.168.1.110:8080/TeleframeService/service/TelframeService

注：
1.常见函数返回值
	public static final long SSID_NULL = -1;//user ssid null
	public static final long OK = 0;// There's no error
	public static final long CONNECT_NULL = 9 ;// connect is null
	public static final long DATA_NULL = 1; // the data is null
	public static final long SQLEXCEPTION = 5; // sql exception

Service API

1.UserEndpointService
  WSDL: http://10.168.1.110:8080/TeleframeService/service/UserEndpointService?wsdl
  DESP: 用户管理类，包含用户的登录和注销
  1)登录
  CODE:
	UserEndpointService service = new UserEndpointService();
	UserEndpoint userEndpoint = service.getUserEndpoinInstance();
	String checkUser = userEndpoint.checkUser("karl", "karl");
	System.out.println(checkUser);
	JSONObject jsonuser = JSONObject.fromObject(checkUser);
	long error = jsonuser.getLong("error"); // 返回0表示成功
	String msg = jsonuser.getString("msg"); // 与error对应的错误解释
	String ssid = jsonuser.getString("data");//分配给用户的SSID, 此处返回的SSID在以后的调用中都会用到
	
  2)注销
	userEndpoint.logoff(ssid);
  

2.ITSMonitorEndpointService
  WSDL: http://10.168.1.110:8080/TeleframeService/service/ITSMonitorEndpointService?wsdl
  DESP: 车辆告警监控服务

  CODE:
	ITSMonitorEndpointService ITSservice = new ITSMonitorEndpointService();
	ITSMonitorEndpoint		its = ITSservice.getITSMonitorEndpoinInstance();

	//
	String warning = its.getWarning(this.SSID, 2, -1);//获得车辆告警信息 SSID 是UserEndpoint.checkUser的返回值ssid
	System.out.println(warning);
	JSONObject jsonWarning = JSONObject.fromObject(warning);

	Object obj = jsonWarning.get("data");
	if(obj instanceof net.sf.json.JSONArray ){
		JSONArray jsonArray = (JSONArray) jsonWarning.getJSONArray("data");
		for(int i=0;i&ltjsonArray.size();i++){  
			JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(i));  
			long id = jsonObject.getLong("id");  
			its.received(this.SSID, id);//返回通知服务器接收成功
			System.out.println("recevied data @ id="+id);
		}
	}
	
	告警返回数据JSON:
	{"error":0,"msg":"OK","data":[{"id":167,"LPNumber":"京AB0003","dir":"1","absTime":"2013-10-03 17:12:52.0","location":"后门","shortImageA":"http://10.168.1.110:8080/TeleframeService/its_upload/pic/its.jpg"},{"id":168,"LPNumber":"京AB0004","dir":"1","absTime":"2013-10-03 18:12:52.0","location":"大门口A","shortImageA":"http://10.168.1.110:8080/TeleframeService/its_upload/pic/its.jpg"},{"id":169,"LPNumber":"京AB0005","dir":"1","absTime":"2013-10-03 19:12:52.0","location":"大门口F","shortImageA":"http://10.168.1.110:8080/TeleframeService/its_upload/pic/its.jpg"}]}



	
3.SPSMonitorEndpointService
  WSDL: http://10.168.1.110:8080/TeleframeService/service/SPSMonitorEndpointService?wsdl
  DESP: 安防告警监控服务

  CODE:
	UserEndpointService UserService = new UserEndpointService();
	UserEndpoint user = UserService.getUserEndpoinInstance();

	String warning = sps.getWarning(this.SSID, 2, -1);//获得安防告警信息 SSID 是UserEndpoint.checkUser的返回值ssid
	System.out.println(warning);
	JSONObject jsonWarning = JSONObject.fromObject(warning);
	
	Object obj = jsonWarning.get("data");
	if(obj instanceof net.sf.json.JSONArray ){
		JSONArray jsonArray = (JSONArray) jsonWarning.getJSONArray("data");
		for(int i=0;i&ltjsonArray.size();i++){  
			JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(i));  
			long id = jsonObject.getLong("id");  
			sps.received(this.SSID, id);////返回通知服务器接收成功
			System.out.println("recevied data @ id="+id);
		}
	};

	告警返回数据JSON:

	{"error":0,"msg":"OK","data":[{"id":7340,"eventName":"不明身份人员闯入","level":"1","personInCharge":"王XX","absTime":"2013-10-03 14:12:52.0","location":"资料中心B","desp":"资料室有不明身份人员闯入，资料室负责人:王XX, 电话010-11112222-123","shortImage":"http://10.168.1.110:8080/TeleframeService/its_upload/pic/its.jpg"},{"id":7341,"eventName":"不明身份人员闯入","level":"1","personInCharge":"王XX","absTime":"2013-10-03 14:12:52.0","location":"资料中心B","desp":"资料室有不明身份人员闯入，资料室负责人:王XX, 电话010-11112222-123","shortImage":"http://10.168.1.110:8080/TeleframeService/its_upload/pic/its.jpg"}]}


	
4. 动态设定WSDL路径
	
	CODE:
	static String BASE_WSLD_URL = "http://10.168.1.250:8888/TeleframeService/service/";
	static String UserEndpointName = "UserEndpointService";
	static String targetNamespace = "http://webservice.teleframe.com";
	
	URL url = new URL(BASE_WSLD_URL+UserEndpointName+"?wsdl");
	QName qname=  new QName(targetNamespace, UserEndpointName);
	UserEndpointService UserService = new UserEndpointService(url,qname);
	UserEndpoint user = UserService.getUserEndpoinInstance();


<hr/>
  </pre>
  </body>
</html>
