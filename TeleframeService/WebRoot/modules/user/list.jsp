<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="net.sf.json.JSONObject" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String ret = (String)request.getAttribute("ret");
//System.out.println(ret);

JSONObject jsonRet = JSONObject.fromObject(ret);
long error = jsonRet.getLong("error");
String msg = jsonRet.getString("msg");
JSONArray jsonArray=null;
if(error==0){
	Object obj = jsonRet.get("data");
	if(obj instanceof net.sf.json.JSONArray ){
		jsonArray = (JSONArray) jsonRet.getJSONArray("data");
	}
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>User List Page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<!-- bootstrap -->
	<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
	<link href="bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">

	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style>
		td {
		    border: 1px solid #C1DAD7;   
		    background: #fff;
		    font-size:15px;
		    padding: 6px 6px 6px 12px;
		    color: #4f6b72;
		}
		
	</style>

  </head>
  
  <body>
    <br>
  <center>
    <%
  		out.println("<h2>User List</h2>");
    	if(jsonArray!=null)
    	{
	    	out.println("<table border=1>");
	    	out.println("<tr><td>id</td> ");
	    	out.println("<td>User Name</td>");
	    	out.println("<td>User Code</td>");
	    	out.println("<td>Password</td>");
	    	out.println("<td>status</td>");
	    	out.println("<td>Operate</td>");  
	    	out.println(" </tr>");
	    	for(int i=0;i<jsonArray.size();i++){  
	    		out.println("<tr>");
				JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(i));  
				out.println("<td >"+jsonObject.getLong("id")+"</td>");  
				out.println("<td>"+jsonObject.getString("username")+"</td>");  
				out.println("<td>"+jsonObject.getString("usercode")+"</td>");  
				out.println("<td>"+jsonObject.getString("password")+"</td>");  
				out.println("<td>"+jsonObject.getString("status")+"</td>");  
				out.println("<td><a href='user/list'  onclick=\"deleteUser('"+jsonObject.getString("usercode")+"', this)\" >Delete</a></td>");  
				out.println("</tr>");				
			}
			out.println("</table>");
		}else
		{
			out.println("Error: "+jsonRet.getLong("error")+"["+jsonRet.getString("msg")+"]");
		}
    	
    %>
    
    <script src="jquery/jquery.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
		
		
    <script type="text/javascript">
    
    	function deleteUser(usercode, obj){
			//	alert(obj.parentNode.innerHTML);
    		$.get("user/delete/"+usercode, "" , function(data){
    		//	if(data.error==0)
    		}, "json" );
    	}
    </script>
    
    </center>
    
    
   
  </body>
</html>
