<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Carousel Template &middot; Bootstrap</title>
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
        	Download ITS&SPS.apk </a></p>
        <p><a href="/download-center/apk/weixin50android.apk" id=learnmore class="btn btn-primary btn-large" style="width:250px;">
        	weixin50android.apk </a></p>
      </div>

      <!-- Example row of columns -->
      <div class="row">
        <div class="span5">
          <h2>ITS & SPS</h2>
          <p>Intelligent Transport System  and Security & Protection System.</p>
          
          <p>
          	<img src="images/qr/download_its_sps.png" class="img-rounded" style="width:200px;height:200px;">
          	<a href="/download-center/apk/its_sps.apk" class="btn btn-info btn-large" >Download</a>
          </p>
          <p><a class="btn" href="#">View details &raquo;</a></p>
        </div>
        <div class="span3">
          <h2>Heading</h2>
          <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
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
