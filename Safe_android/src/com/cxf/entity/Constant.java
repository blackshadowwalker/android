package com.cxf.entity;

public class Constant {
	// 命名空间
	public static final String NAMESPACE = "http://webservice.teleframe.com";
	// 主机和端口
	public static String HOST = "http://10.168.1.252:8888/TeleframeService";
	public static final String DEFAULT_HOST = "http://teleframe.xicp.net/TeleframeService";
	// 车辆告警URL
	public static String CAR_GUARD_URL = HOST
			+ "/service/ITSMonitorEndpointService?wsdl";
	// 保卫安防告警
	public static String SECURITY_GUARD_URL = HOST
			+ "/service/SPSMonitorEndpointService?wsdl";
	// 获得车辆告警方法
	public static final String CAR_GUARD_METHOD_GETWARNING = "getWarning";
	// 返回车辆告警接收完成方法
	public static final String CAR_GUARD_METHOD_RECEIVED = "Received";
	// 返回车辆告警接收完成方法
	public static final String CAR_GUARD_METHOD_RECEIVEDS = "Receiveds";
	// 获得保卫安防告警方法
	public static final String SECURITY_GUARD_METHOD_GETWARNING = "getWarning";
	// 返回已接收保卫安防告警方法
	public static final String SECURITY_GUARD_METHOD_RECEIVED = "Received";
	// 返回已接收保卫安防告警方法
	public static final String SECURITY_GUARD_METHOD_RECEIVEDS = "Receiveds";
	// 检查用户信息URL
	public static String USER_URL = HOST + "/service/UserEndpointService?wsdl";
	// 检查用户信息方法
	public static final String USER_METHOD_CHECKUSER = "checkUser";
	// 修改用户密码
	public static final String USER_METHOD_MODIFYPASSWORD = "modifyPassword";
	// 修改用户密码
		public static final String USER_METHOD_LOGOFF = "logoff";

	// 车辆统计表
	public static String CAR_GUARD_CHART_URL = HOST + "/chart/clchart";
	// 保卫安防统计表
	public static String SECURITY_GUARD_CHART_URL = HOST + "/chart/xfchart";

	public static void init(String host) {

		Constant.HOST = host;
		CAR_GUARD_URL = HOST + "/service/ITSMonitorEndpointService?wsdl";
		SECURITY_GUARD_URL = HOST + "/service/SPSMonitorEndpointService?wsdl";
		USER_URL = HOST + "/service/UserEndpointService?wsdl";
		// 车辆统计表
		CAR_GUARD_CHART_URL = HOST + "/chart/clchart";
		// 保卫安防统计表
		SECURITY_GUARD_CHART_URL = HOST + "/chart/xfchart";
	}

}
