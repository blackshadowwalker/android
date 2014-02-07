<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <title>Upload Files</title>
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
     <%@ include file="../../nav.jsp" %>
     
     <div class="container">
		<div align=right>
			<div class="input-append text-right">
			  <input class="span2" id="appendedInput" type="text">
			  <span class="btn">Search</span>
			</div>
		</div>

      <!-- Example row of columns -->
      <div class="row">
        <div class="span6">
          <h2>What you want?</h2>
          <p>
			<li >Upload Files</li>
			<li >List Files</li>
		  </p>
		  <p><a class="btn btn-info" href="modules/help/index.jsp">Help &raquo;</a></p>
       </div>
        
        
      </div>

      <hr>
    </div> <!-- /container -->
     
     
     
  </body>
</html>
