<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="net.sf.json.JSONObject" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String ret = (String)request.getAttribute("ret");
System.out.println(ret);

JSONObject jsonRet = JSONObject.fromObject(ret);
long error = jsonRet.getLong("error");
String msg = jsonRet.getString("msg");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'add.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
	<br>
	<script>
		var error = "<%=error %>";
		var msg = "<%=msg %>";
		alert(msg);
	</script>
	<center>
		<a href="user/list" >list user</a>
	</center>
  </body>
</html>
