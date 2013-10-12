<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
  	<base href="<%=basePath%>">
    <title>信帧 Apk upload </title>
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
    	input[type="text"]
    	{
    		height:30px;
    	}
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
	       		<div class="span4">
			       	<h2>Upload ITS_SPS.apk</h2>
			       	<p>
			       		Here is to update the its_sps android client apk.
			       	</p>
					<form target="hidden_frame" action="<%=path %>/upload.do?dir=/download-center/apk&saveName=its_sps.apk&callback=uploadapk" method="post" ENCTYPE="multipart/form-data" >
						<input name="myfile" style="width:120px;" onclick="return clearRetInfo();" class="btn btn-info" type="file">
		          		<iframe name="hidden_frame" style="display:none;"></iframe>
						<input type="submit" class="btn btn-primary " value="Submit">
						<br>
						<br>
						<p id="ret"></p>
					</form>  
				</div>
				
				<div class="span6">
			          <h2>ITS & SPS</h2>
			          <p>Intelligent Transport System  and Security & Protection System.</p>
			          <p>
			          	<img src="images/qr/download_its_sps.png" class="img-rounded" style="width:200px;height:200px;">
			          	<a href="/download-center/apk/its_sps.apk" class="btn btn-info btn-large" >Download</a>
			          </p>
			          <p><a class="btn" href="#">View details &raquo;</a></p>
		        </div>
				
			 </div>
	      </div><!-- end of hero-unit -->
	      
	      <div class="row">
		      	<div class="span5">
		          <h2>Heading</h2>
		          <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
		          <p><a class="btn" href="#">View details &raquo;</a></p>
		       </div>
	        
		       <div class="span5">
		          <h2>Heading</h2>
		          <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
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
