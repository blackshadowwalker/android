<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="net.sf.json.JSONArray" %>
<%@ page import="net.sf.json.JSONObject" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1";/>
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
		a:HOVER {
		 	color: #00FF00; /*鼠标经过的颜色变化*/
		}
		a{
		 	text-decoration:underline;
		 	cursor:hand; 
		}
		
	</style>

  </head>
  
  <body>
    <br>
  <center>
    <div id="userlist"> Querying </div>
    
    <br>
    <div style="display:block;">
		<iframe name="location" style="width:500px;height:40px; border=0px;" 
			src="http:\/\/int.dpool.sina.com.cn\/iplookup\/iplookup.php?format=text&ip=114.249.229.211" ></iframe>
	</div>	
    <div id="userlog"></div>
    
    <script src="jquery/jquery.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
		
		
    <script type="text/javascript">
    
    	var html = "";
    	$(document).ready(function(){
    		// $( "#login" ).draggable();
    		 $.get("user/list", "" , function(ret){
    		 	html += "<h2>User List</h2>";
    			if(ret.error==0){
    				var obj = ret.data;
    				
    				html += "<table border=1 name=listuser>";
			    	html += "<tr><td>id</td> ";
			    	html += "<td>User Name</td>";
			    	html += "<td>User Code</td>";
			    	html += "<td>Password</td>";
			    	html += "<td>status</td>";
			    	html += "<td>Operate</td>";  
			    	html += " </tr>";
	    	
    				$(obj).each(function(index){
   						var user = obj[index];
   						html += "<tr id="+user.id+">";
   						html += "<td >"+user.id+"</td>";  
   						html += "<td >"+user.username+"</td>";  
   						html += "<td >"+user.usercode+"</td>";  
   						html += "<td >"+user.password+"</td>";  
   						html += "<td >"+user.status+"</td>";  
   						html += "<td><a href=\"javascript:showlog(\'"+user.usercode+"\');\" >Log</a> <span>&nbsp;&nbsp;</span>";  
   						html += "<a onclick=\"javascript:deleteUser(\'"+user.usercode+"\', this);\" >Delete</a></td>";  
   						html += "</tr>";
   						
   					});
   					html += "</table>";
    			}
    			else
    				html = "Error("+ret.error+"): " + ret.msg;
	    		$("#userlist").html(html);
    		}, "json" );
    		 
    	});
    	
    	function showlog(usercode){
    		
    		var url = "userlog?usercode="+usercode;
    		var html= "";
    		$.get(url, "", function(json){
    			if(json.error==0){
    				html ="<table>";
    				html +="<tr>";
	   				html +="<th>AccessIp</th>";
	   				html +="<th>AccessTime</th>";
	   				html +="</tr>";
	   				var data = json.data;
		   			$(data).each(function(i){
		  				var userlog = data[i];
		   				html +="<tr>";
		   				html +="<td>"+userlog.accessIp+"</td>";
		   				
		   				html +="<td>"+userlog.accessTime+"</td>";
		   				html +="</tr>";
		   			});
		   			html +="</table>";
    			}else{
    				html="Error:"+json.msg;
    			}
    			$("#userlog").html(html);
    		}, "json" );
    	}
    
    	function deleteUser(usercode, obj){
    		var i=obj.parentNode.parentNode.rowIndex;
    		$.get("user/delete/"+usercode, "" , function(ret){
    			if(ret.error==0 || ret.error==200)
    				obj.parentNode.parentNode.parentNode.deleteRow(i);
    		}, "json" );
    	}
    </script>
    
    </center>
    
    
   
  </body>
</html>
