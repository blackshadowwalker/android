<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
String baseServer = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
String basePath = baseServer + path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
  	<base href="<%=basePath%>">
    <title> Apk upload </title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1";/>
    <meta name="description" content="">
    <meta name="author" content="">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<!-- bootstrap -->
	<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
	<link href="bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
	<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="//cdnjs.bootcss.com/ajax/libs/html5shiv/3.6.2/html5shiv.js"></script>
    <![endif]-->
    <link href="css/home.css" rel="stylesheet" >
    <style>
    </style>

  </head>
  
  <body>
  
  	<%@ include file="../../../nav.jsp" %>
  	
  	<div class="container">
		<div align=right>
			<div class="input-append text-right">
			  <input class="span2" id="appendedInput" type="text">
			  <span class="btn">Search</span>
			</div>
		</div>
		
		  <div class="hero-unit">
	       	
	       	<div class="row">
	       		<div class="span3">
			       	<h3>核工业安防保卫系统</h3>
			       	<p>
			       		
			       	</p>
					<form target="hidden_frame" 
						action="<%=path %>/upload.do?dir=/download-center/apk&saveName=its_sps.apk&callback=uploadapk&backup=true" 
						method="post" ENCTYPE="multipart/form-data" >
						<input name="myfile" style="width:75px;" onclick="return clearRetInfo();" class="btn btn-info" type="file">
		          			<iframe name="hidden_frame" style="display:none;"></iframe>
						<input type="submit" class="btn btn-primary " value="Submit">
						<br>
						<br>
						<p id="ret"></p>
					</form>  
				</div>
				
				<div class="span3">
			          	<h3>金融认证系统更新</h3>
			       	<p>
			       		
			       	</p>
					<form target="hidden_frame" 
						action="<%=path %>/upload.do?dir=/download-center/apk&saveName=fca.apk&callback=uploadapk&backup=true" 
						method="post" ENCTYPE="multipart/form-data" >
						<input name="myfile" style="width:75px;" onclick="return clearRetInfo();" class="btn btn-info" type="file">
		          		<iframe name="hidden_frame" style="display:none;"></iframe>
						<input type="submit" class="btn btn-primary " value="Submit">
						<br>
						<br>
						<p id="ret"></p>
					</form>  
					
		        </div>
		        <div class="span3">
			          	<h3>手机车牌识别</h3>
			       	<p>
			       		
			       	</p>
					<form target="hidden_frame" 
						action="<%=path %>/upload.do?dir=/download-center/apk&saveName=TefLPR.apk&callback=uploadapk&backup=true" 
						method="post" ENCTYPE="multipart/form-data" >
						<input name="myfile" style="width:75px;" onclick="return clearRetInfo();" class="btn btn-info" type="file">
		          		<iframe name="hidden_frame" style="display:none;"></iframe>
						<input type="submit" class="btn btn-primary " value="Submit">
						<br>
						<br>
						<p id="ret"></p>
					</form>  
					
		        </div>
				
			 </div>
	      </div><!-- end of hero-unit -->
	      
	      <div class="row">
		      <div class="span4">
		          <h2>核工业安防保卫系统</h2>
		          <p></p>
		          
		          <p>
		          	<img src="qrservice?text=<%=baseServer %>/download-center/apk/its_sps.apk" 
		          		class="img-rounded" style="width:240px;height:240px;">
		          	<br/>
		          	<a href="/download-center/apk/its_sps.apk" class="btn btn-info btn-large" >Download</a>
		          </p>
		          <p><a class="btn" href="#">View details &raquo;</a></p>
		        </div>
		        <div class="span4">
		          <h2>金融认证系统</h2>
		         <p> </p>
		          
		          <p>
		          	<img src="qrservice?text=<%=baseServer %>/download-center/apk/fca.apk" 
		          		class="img-rounded" style="width:240px;height:240px;">
		          	<br/>
		          	<a href="/download-center/apk/fca.apk" class="btn btn-info btn-large" >Download</a>
		          </p>
		          <p><a class="btn" href="#">View details &raquo;</a></p>
		       </div>
	        
		       <div class="span3">
		          <h2>手机车牌识别</h2>
		          <p></p>
		          <p>
		          	<img src="qrservice?text=<%=baseServer %>/download-center/apk/TefLPR.apk" 
		          		class="img-rounded" style="width:240px;height:240px;">
		          	<br/>
		          	<a href="/download-center/apk/TefLPR.apk" class="btn btn-info btn-large" >Download</a>
		          </p>
		          <p><a class="btn" href="#">View details &raquo;</a></p>
		        </div>
       
	      </div>
	 </div> <!-- /container -->
	
	
	<script src="jquery/jquery.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
	<script src="jquery/jquery-ui.js"></script>
	
	
    <script>
    	$(document).ready(function(){

    	});
    	function clearRetInfo(){
    		$("#ret").html("");
    	}
    	function uploadapk(result){
			$("#ret").html(result.data[0].msg);
		}
    </script>

  </body>
</html>
