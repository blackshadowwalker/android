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
  	<base href="<%=basePath%>">
    <meta charset="utf-8">
    <title>List Files</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1";/>
    <meta name="description" content="">
    <meta name="author" content="">
	
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
	<!-- bootstrap -->
	<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
	<link href="bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">

    <link href="css/home.css" rel="stylesheet" >
    <style>
		th,td {
		    border: 1px solid #C1DAD7;   
		    background: #fff;
		    font-size:15px;
		    padding: 6px 6px 6px 12px;
		    color: #4f6b72;
		}
	</style>

  </head>

  <body>

    <!-- NAVBAR
    ================================================== -->
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
        <div class="span8">
          <h2>File List</h2>
          <div id=filelist></div>
          <br/>
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
    	function listfile(){
    		var url = "file/list";
    		var html= "";
    		$.get(url, "", function(json){
    			if(json.error==0){
    				html ="<table border=0 >";
    				html +="<tr>";
	   				html +="<th>id</th>";
	   				html +="<th style='width:200px;'>FileName</th>";
	   				html +="<th style='width:50px;'>FileType</th>";
	   				html +="<th style='width:160px;'>ctTime</th>";
	   				html +="<th>Download</th>";
	   				html +="</tr>";
	   				var data = json.data;
		   			$(data).each(function(i){
		  				var file = data[i];
		   				html +="<tr>";
		   				html +="<td>"+file.id+"</td>";
		   				html +="<td>"+file.filename+"</td>";
		   				html +="<td>"+file.filetype+"</td>";
		   				html +="<td>"+file.ctTime+"</td>";
		   				html +="<td><a href='"+file.path+"'>Download</a></td>";
		   				html +="</tr>";
		   			});
		   			html +="</table>";
    			}else{
    				html="Error:"+json.msg;
    			}
    			$("#filelist").html(html);
    		}, "json" );
    	}
    	$(document).ready(function(){
    		 $( "#userpanel" ).draggable();
    		 listfile();
    	});
    </script>
  </body>
</html>
