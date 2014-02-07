<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String baseServer = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
String basePath = baseServer + path+"/";
%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Download Center</title>
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

      <!-- Main hero unit for a primary marketing message or call to action -->
      <div class="hero-unit">
        <h1>Hello, Android !</h1>
        <p>This is Teleframe Android Download Center. </p>
        <p><a href="/download-center/apk/its_sps.apk" id=learnmore class="btn btn-primary btn-large" style="width:250px;">
        	核工业安防保卫系统 </a></p>
        <p><a href="/download-center/apk/tfMobileAuth.apk" id=learnmore class="btn btn-primary btn-large" style="width:250px;">
        	金融认证系统 </a></p>
        <p><a href="/download-center/apk/TeleframeVLPR.apk" id=learnmore class="btn btn-primary btn-large" style="width:250px;">
        	手机车牌识别 </a></p>
      </div>

      <!-- Example row of columns -->
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
         <p></p>
          <p>
          	<img src="qrservice?text=<%=baseServer %>/download-center/apk/tfMobileAuth.apk" 
          		class="img-rounded" style="width:240px;height:240px;">
          	<br/>
          	<a href="/download-center/apk/tfMobileAuth.apk" class="btn btn-info btn-large" >Download</a>
          </p>
          <p><a class="btn" href="#">View details &raquo;</a></p>
       </div>
        <div class="span3">
          <h2>手机车牌识别</h2>
          <p></p>
          <p>
          	<img src="qrservice?text=<%=baseServer %>/download-center/apk/TeleframeVLPR.apk" 
          		class="img-rounded" style="width:240px;height:240px;">
          	<br/>
          	<a href="/download-center/apk/TeleframeVLPR.apk" class="btn btn-info btn-large" >Download</a>
          </p>
          <p><a class="btn" href="#">View details &raquo;</a></p>
        </div>
      </div>

      <hr>
    </div> <!-- /container -->
	
	
	<div class=container >
	<a href="#login" data-toggle="modal" role="button" class="btn" >登录</a>
	<!-- Modal -->
			<div id="login" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			  <div class="modal-header">
				<button type="button" class="btn pull-right" data-dismiss="modal" aria-hidden="true">
					<span color=black> &nbsp;&nbsp;x&nbsp;&nbsp;</span>
				</button>
				<h3 id="myModalLabel">&nbsp;</h3>
			  </div>
			  <div class="modal-body" align=center>
				<iframe src="modules/login/smallLogin.jsp" frameborder=0 scrolling="no"  
					style="width:500px;height:300px;"></iframe>
			  </div>
			</div>
	</div>
	
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
