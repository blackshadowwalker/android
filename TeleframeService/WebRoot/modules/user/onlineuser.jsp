<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1";/>
    <base href="<%=basePath%>">
    
    <title>onlineuser.jsp</title>
    
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
		    font-size:13px;
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
		.ipclass{
			cursor: hand;
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
  <div style="width:100%; border:0px #f00 solid; font-size:9px;float:left;">
   <center>
   		<P>Online Users</P>
   		<table id="onLineUserList" border =1>
   			<tr>
   				<th>Num<br></th>
   				<th>SSID</th>
   				<th>UserCode</th>
   				<th>UserName</th>
   				<th>LastAccessTime</th>
   				<th>LastAccessIP</th>
   				<th>LoginTime</th>
   				<th>LoginIP</th>
   				<th>LastLoginTime</th>
   				<th>LastLoginIP</th>
   				<th>ShowLog</th>
   			</tr>
   		</table>
   		
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
   	
	<div id="userlog" style=""></div>
   		
   </center>
   		<div class="booter" align=right style="display:none;">
  			 <a style="cursor:hand;" title="Clear All Users" dir="rtl"  onclick="clearUser()">C</a>
  		</div>
	</div>
	
	<br/>
	<br/>
	<br/>
	<br/>
  		
	<script src="jquery/jquery.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
	<script src="jquery/jquery-ui.js"></script>
		
    <script type="text/javascript">
    
    	var timer = null;
    	$(document).ready(function(){
    		$( "#resultframe" ).draggable();
    	
			var ipurl = "http:\/\/int.dpool.sina.com.cn\/iplookup\/iplookup.php?format=text&ip=114.249.229.211";
		/*
			alert(ipurl);
			$.get("http:\/\/10.168.1.250:8888/TeleframeService/onlineuser", "", function(retdata){
				alert(retdata);
			}, "json");
			var postdata = { format: "text", ip: "114.249.229.211" };
 			$.ajax({
			  type: 'POST',
			  url: ipurl,
			  data: postdata,
			  success: success,
			  dataType: dataType
			});
 		*/
			$.get("onlineuser", "", updateOnlineUser, "text" );
    		timer = setInterval("getOnlineUser()",50000);   	
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
   						"<td>"+user.UserName+"</td> <td>"+user.LastAccessTime+"</td>"+"<td>"+user.LastAccessIp+"</td>"+
   						"<td>"+user.LoginTime+"</td><td>"+user.LoginIp+"</td><td>"+user.LastLoginTime+"</td><td>"+user.LastLoginIp+"</td>"+
   						"<td><a href=\"javascript:showlog(\'"+user.UserCode+"\');\">ShowLog</a></td></tr>";
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
    	
    	function showlog(usercode){
    		var url = "userlog?usercode="+usercode;
    		var html= "";
    		$.get(url, "", function(json){
    			if(json.error==0){
    				html ="<table>";
    				html +="<tr>";
	   				html +="<th >AccessIp</th>";
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
    	
    	function getIp(obj){
    		var ipurl = "http:\/\/int.dpool.sina.com.cn\/iplookup\/iplookup.php?format=text&ip="+obj.innerHTML;
    		alert(location.src);
    		location.src = ipurl;
    	}
    	
    </script>

  </body>
</html>
