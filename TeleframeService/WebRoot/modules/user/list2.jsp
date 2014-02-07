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

	<style>
		td {
		    border: 1px solid #C1DAD7;   
		    background: #fff;
		    font-size:15px;
		    padding: 6px 6px 6px 12px;
		    color: #4f6b72;
		}
		.ipclass{
			cursor: hand;
		}
		  body {
	        padding: 10px; /* 60px to make the container go all the way to the bottom of the topbar */
			background-color: #fafafa;
			font-size:15px;
			align:center;
	      }
		 .form-div{
	      	max-width: 510px;
	        margin: 0 auto 20px;
	        background-color: #fff;
	        border: 1px solid #e5e5e5;
	        vertical-align:middle;
	        -webkit-border-radius: 5px;
	           -moz-border-radius: 5px;
	                border-radius: 5px;
	        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
	           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
	                box-shadow: 0 1px 2px rgba(0,0,0,.05);
	      }
	      .topbar span{
	   	  	color:#000;
	   	  	font-weight:bold;
		  }
		  .topbar:hover{
	   	  	cursor:move;		  
		  }
		
		.result{  
		  	position:fixed;
			margin: auto auto 10px auto ;
		}
	</style>
  </head>
  
  <body>

  <center>
    <div id="userlist"> Querying </div>
    <br>
   <div id="resultframe" class="form-div result" style="">
      	<div class=topbar >
			<span>&nbsp;</span>
		</div>
      	<div>
		<iframe name="location" style="width:500px;height:40px; border=0px;" 
			src="http:\/\/int.dpool.sina.com.cn\/iplookup\/iplookup.php?format=text&ip=114.249.229.211" ></iframe>
		</div>
	</div>	
    <div id="userlog"></div>

	<br/>
	<br/>
	<br/>
	<br/>
	
    <script src="jquery/jquery.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
		
	<script src="jquery/jquery-ui.js"></script>
		
    <script type="text/javascript">
    	
    	var html = "";
    	$(document).ready(function(){
    		 $( "#resultframe" ).draggable();
    		 $.get("user/list", "" , function(ret){
    		 	html += "<h2>User List</h2>";
    			if(ret.error==0){
    				var obj = ret.data;
    				
    				html += "<table border=1>";
			    	html += "<tr>";
			    	html += "<td style=\"display:none;\">id</td> ";
			    	html += "<td>User Name</td>";
			    	html += "<td>User Code</td>";
			    	html += "<td>Password</td>";
			    	html += "<td>Operate</td>";  
			    	html += "<td>Status</td>";
			    	html += " </tr>";
	    	
    				$(obj).each(function(index){
   						var user = obj[index];
   						html += "<tr>";
   						html += "<td style=\"display:none;\" >"+user.id+"</td>";  
   						html += "<td >"+user.username+"</td>";  
   						html += "<td >"+user.usercode+"</td>";  
   						html += "<td >"+user.password+"</td>";  
   						html += "<td><a href=\"javascript:showlog(\'"+user.usercode+"\');\" >Log</a></td>";  
   						if(user.status==1)
	   						html += "<td >OK</td>";
	   					else  
	   						html += "<td >Didabled</td>";
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
		   				html +="<td class=ipclass onclick=\"getIp(this);\" title='Show Location ?'>"+userlog.accessIp+"</td>";
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
			//	alert(obj.parentNode.innerHTML);
    		$.get("user/delete/"+usercode, "" , function(data){
    		//	if(data.error==0)
    		}, "json" );
    	}
    	
    	function getIp(obj){
    		var ipurl = "http:\/\/int.dpool.sina.com.cn\/iplookup\/iplookup.php?format=text&ip="+obj.innerHTML;
    		window.open(ipurl, "location");
    	}
    	
    </script>
    
    </center>
    
    
   <br>
   <br>
  </body>
</html>
