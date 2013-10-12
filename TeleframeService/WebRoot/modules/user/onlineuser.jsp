<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'onlineuser.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style>
		td,th{
		    border: 1px solid #C1DAD7;   
		    background: #fff;
		    font-size:15px;
		    padding: 6px 6px 6px 12px;
		    color: #4f6b72;
		}
		.hover
		{
		  background-color: #f00;
		}
		.odd{ background-color: #bbf;}
		.even{ background-color:#ffc; }
		.booter{
			margin-right: 10px;
		}
		
	</style>

  </head>
  
  <body>
  <div style="width:100%; height:100%; border:0px #f00 solid;">
   <center>
   		<P>Online Users</P>
   		<table id="onLineUserList" border =1>
   			<tr>
   				<th>Num<br></th>
   				<th>SSID</th>
   				<th>UserCode</th>
   				<th>UserName</th>
   				<th>LastAccessTime</th>
   				<th>LoginTime</th>
   				<th>IP</th>
   				<th>LastLoginTime</th>
   				<th>LastLoginIP</th>
   			</tr>
   		</table>
   		
   		
   </center>
   		<div class="booter" align=right>
  			 <a style="cursor:hand;" title="Clear All Users" dir="rtl"  onclick="clearUser()">C</a>
  		</div>
	</div>
	<script src="jquery/jquery.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
		
		
    <script type="text/javascript">
    
    	var timer = null;
    	$(document).ready(function(){
			$.get("onlineuser", "", updateOnlineUser, "text" );
    		timer = setInterval("getOnlineUser()",5000);   	
    	});
    	
    	function clearUser(){
    		$.get("onlineuser/clear", "", updateOnlineUser, "text" );
    	}
    	
    	function updateOnlineUser(response,status,xhr){
			var ret = eval("("+response+")");
			var obj = ret.data;
   			$("#onLineUserList tr:gt(0)").remove();
   			$(obj).each(function(index){
   				var user = obj[index];
   				var newRow = "<tr> <td>"+index+"</td> <td>"+user.Ssid+"</td> <td>"+user.UserCode+"</td>"+
   						"<td>"+user.UserName+"</td> <td>"+user.LastAccessTime+"</td>"+
   						"<td>"+user.LoginTime+"</td><td>"+user.Ip+"</td><td>"+user.LastLoginTime+"</td><td>"+user.LastLoginIp+"</td></tr>";
				$("#onLineUserList tr:last").after(newRow);
   			});
			$("#onLineUserList tr:gt(0)").hover(
			    function () { $(this).addClass("hover") },
			    function () { $(this).removeClass("hover") });
    	};
    	
    	function getOnlineUser(){
    		var url = "onlineuser";
    		$.get(url, "", updateOnlineUser, "text" );
    	}
    	
    </script>

  </body>
</html>
