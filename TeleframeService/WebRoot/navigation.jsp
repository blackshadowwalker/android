<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String baseServer = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
String basePath = baseServer + path+"/";
%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Navigation</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1";/>
    <meta name="description" content="">
    <meta name="author" content="">
	
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
	<!-- bootstrap -->
	<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
	<link href="bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">

    <link href="css/home.css" rel="stylesheet" >

  </head>

  <body>

    <!-- NAVBAR
    ================================================== -->
     <%@ include file="nav.jsp" %>
     

	<div class="container">
		<div align=right>
			<div class="input-append text-right">
			  <input class="span2" id="appendedInput" type="text">
			  <span class="btn">Search</span>
			</div>
		</div>

      <!-- Example row of columns -->
      <div class="row">
        <div class="span5">
          <h2>Download</h2>
          <p><a href="download">Download Center</a></p>
          <p><a href="modules/file/uploadfiles.jsp">Upload Files</a></p>
          <p><a href="modules/file/listfiles.jsp">List Files</a></p>
          <p><a href="upload.do">Upload File Help</a></p>
          
          <p><a class="btn" href="#">View details &raquo;</a></p>
        </div>
        
        <div class="span3">
          <h2>User</h2>
          <p><a href="modules/user/list2.jsp">List Users</a></p>
          <p><a href="online.jsp">Online Users</a></p>
          <p><a class="btn" href="#">View details &raquo;</a></p>
       </div>
        
        <div class="span3">
          <h2>Heading</h2>
          <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>
          <p><a class="btn" href="#">View details &raquo;</a></p>
        </div>
      </div>

      <hr>
    </div> <!-- /container -->
	
	
	
	
    <!-- Le javascript =========== -->
    <!-- Placed at the end of the document so the pages load faster -->
		<script src="jquery/jquery.min.js"></script>
		<script src="bootstrap/js/bootstrap.min.js"></script>
		
	<script src="jquery/jquery-ui.js"></script>
    <script>
    	$(document).ready(function(){
    		 $( "#login" ).draggable();
    		 $( "#userpanel" ).draggable();
    		 
    	});
    </script>
  </body>
</html>
