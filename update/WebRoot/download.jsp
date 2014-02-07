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
	  <div style="width:870px;">
		<div id="myCarousel" class="carousel slide">
		  <ol class="carousel-indicators">
		    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
		    <li data-target="#myCarousel" data-slide-to="1"></li>
		    <li data-target="#myCarousel" data-slide-to="2"></li>
		  </ol>
		  <!-- Carousel items -->
		  <div class="carousel-inner">
		    <div class="active item">
				<img src="images/apps/its_sps.png" alt="">
                <div class="carousel-caption">
                   <p>
                   	<a href="/download-center/apk/its_sps.apk" id=learnmore class="btn btn-success btn-large" style="width:800px;">
        			核工业安防保卫系统 </a>
        			</p>
                </div>
			</div>
		    <div class="item">
				<img src="images/apps/tfMobileAuth.png" alt="">
                <div class="carousel-caption">
                	<p>
                		<a href="/download-center/apk/tfMobileAuth.apk" id=learnmore 
                			class="btn btn btn-warning btn-large" style="width:800px;">
        				金融认证系统
        				</a>
        			</p>
                </div>
			</div>
		    <div class="item">
		    	<img src="images/apps/vlpr.png" alt="">
                <div class="carousel-caption">
                  <p>
                  	<a href="/download-center/apk/TeleframeVLPR.apk" id=learnmore 
                  		class="btn btn-primary btn-large" style="width:800px;">
                  		手机车牌识别系统
                  	</a>
        	 	  </p>
                </div>
			</div>
		  </div>
		  <!-- Carousel nav -->
		  <a class="carousel-control left" href="#myCarousel" data-slide="prev">&lsaquo;</a>
		  <a class="carousel-control right" href="#myCarousel" data-slide="next">&rsaquo;</a>
		</div>
	  </div>
	 </div>
 
 
      <!-- Example row of columns -->
      <div class="row" id="download-apks">
      	<div id="download-Loading">
		      	<center>
		      		<image src="images/loading/loading_007.gif"><h2>Loading......</h2></image>
		      	</center>
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
    		 
			$("textarea[name='description']").val("");
			var app_names = ["its_sps", "tfMobileAuth", "TelLPR"];
			for(var i=0; i<3; i++)
			{
				var url = "?update=true&app_name="+app_names[i]+"&logdb=false";
				$.get(url, function(result){
					var form = $("#"+result.app_name+"Form");
					if(result.name!=undefined)
					{
						form.children("input[name='name']").val(result.name);
						form.children("input[name='versionName']").val(result.versionName);
						form.children("input[name='versionCode']").val(result.versionCode);
						form.children("textarea[name='description']").val(result.description);
						
						var html="";
						html += "	<div class=span4>";
					    html += "      <h2>"+result.name+"</h2>";
					    html += "      ";
					    html += "      <p id=its_spsDownload>";
					    html += "      	<img src=\"qrservice?text="+result.url+" \" ";
					    html += "      		class=\"img-rounded\" style=\"width:240px;height:240px;\" >";
					    html += "      	<br>";
					    html += "      	<a name=Download href=\" "+result.url+" \" class=\"btn btn-info btn-large\" >Download</a>";
					    html += "       </p>";
					    html += "      <p><a class=btn href=#>View details &raquo;</a></p>";
					    html += "    </div>";
		        		$("#download-apks").append(html);
		        		$("#download-Loading").css("display","none");
					}
				}, "json");
			}	
    	});
    </script>
  </body>
</html>
