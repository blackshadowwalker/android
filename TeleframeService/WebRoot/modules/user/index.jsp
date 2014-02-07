<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
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
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1";/>
    <meta name="description" content="">
    <meta name="author" content="">
	
	<!-- bootstrap -->
	<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
	<link href="bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
	<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="//cdnjs.bootcss.com/ajax/libs/html5shiv/3.6.2/html5shiv.js"></script>
    <![endif]-->
    <link href="css/home.css" rel="stylesheet" >
    
    <style>
    
	input[type="text"]{
		height:30px;
	}
    </style>
    
  </head>
  
  <body>
    <%@ include file="../../nav.jsp" %>
    
    <div class="container">
    	<div align=right>
			<div class="input-append text-right">
			  <input class="span2" id="appendedInput" type="text">
			  <span class="btn">Search</span>
			</div>
		</div>
		
		 <div class="hero-unit">
	        <p>
			    <a href="modules/user/list2.jsp" target=""  class="btn btn-primary btn-large" >list users</span></a>
			    
		    	<span onclick="AutoAddUsers()"  class="btn btn-primary btn-large" >Auto add users</span>
		    </p>
		    <p id="ret">
		    	Return:<br>
		    </p>
	        
	      </div>
	    
	    <div class="row">
	        <div class="span4">
	          <h2>Heading</h2>
	          <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
	          <p><a class="btn" href="#">View details &raquo;</a></p>
	        </div>
	        <div class="span4">
	          <h2>Heading</h2>
	          <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
	          <p><a class="btn" href="#">View details &raquo;</a></p>
	       </div>
	        <div class="span4">
	          <h2>Heading</h2>
	          <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>
	          <p><a class="btn" href="#">View details &raquo;</a></p>
	        </div>
	      </div>
		
	
	 </div><!-- container -->
    
    
    <script src="jquery/jquery.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
	<script src="jquery/jquery-ui.js"></script>
	
    <script>
    	var geted = false;
	    function adduserCallback(response,status,xhr){
    		var html = $("#ret").html();
    		html +="<br>"+response.msg;
	    	$("#ret").html(html);
	    	geted = true;
	    }
	    var num=1;
	    function AutoAddUsers(){
		    var usercode="";
		    geted = true;
		    var timer = setInterval( function(){
		   		if(num>=101) clearInterval(timer);
		    	if(geted==false)
		    		return;
		    	
	    		usercode = "user"+num++;
	    		geted = false;
	    		$.get("user/add/"+usercode, "" , adduserCallback, "json" );
		    }, 10);
	    }
	    
	    
    	$(document).ready(function(){
    		$( "#userpanel" ).draggable();
    	});
    	
    	
    	
    </script>
    
  </body>
</html>
