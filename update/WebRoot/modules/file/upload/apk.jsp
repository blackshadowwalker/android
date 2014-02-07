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
    	.img-rounded{
    		margin-left:-10px;
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
	       		<div class="span3">
			       	<h3>核工业安防保卫系统</h3>
			        <p style="display:none">Nuclear security defense system</p>
			       	<p>
			       		
			       	</p>
					<form name="its_spsForm" id="its_spsForm"
						target="hidden_frame" 
						action="<%=path %>/upload.do?dir=/download-center/apk&saveName=its_sps.apk&callback=uploadapk&backup=true" 
						method="post" ENCTYPE="multipart/form-data" >
						<input name="myfileapk" style="width:75px;display:none;" onclick="return clearRetInfo();" class="btn btn-info" type="file" onchange="apkchanged(this);">
						<script>
							function apkchanged(obj){
								//alert(obj.value);
							}
							//document.its_spsForm.apkpath.innerHTML=this.value
						</script>
						<input type="button"  class="btn btn-info" value="上传APK" onclick="document.its_spsForm.myfileapk.click()"> 
						<span name="apkpath" ></span>
						&nbsp;&nbsp;
						<span><a href="?update=true&app_name=its_sps">更新检测地址</a></span>
						<br>
						
						<span style="display:none;" >
							<input name="myfilexml" style="width:75px;display:none;" onclick="return clearRetInfo();" class="btn btn-info" type="file" value="jk">
							<input type="button"  class="btn btn-info" value="上传XML" onclick="document.its_spsForm.myfilexml.click()"> 
								<span name="xmlpath" ></span><br>
						</span>
						<div style="height:5px;">&nbsp;</div>
		          		<input type="hidden" name="name" placeholder="name" value="信帧安防保卫" />
			          	<input type="hidden" name="app_name" placeholder="app_name" value="its_sps"/>
			          	<input type="text" name="versionName" id="versionName" placeholder="版本(e.g: 1.2.2)" value="" onblur="versionChanged(this);"/>
			          	<span id="versionNameInfo"  for="versionName"></span>
			          	<input type="hidden" name="versionCode"  for="versionName" placeholder="versionCode(e.g: 122)" value="" /><br/>
			          	<textarea  name="description" placeholder="描述" rows="10" ></textarea>
			          	
	          			<iframe name="hidden_frame" style="display:none;"></iframe><br/>
						<input type="button" class="btn btn-primary " value="Submit" onclick="chekFileds(this);" >
						<br>
						<br>
						<p name="ret"></p>
					</form>  
				</div>
				
				<div class="span3">
			          	<h3>金融认证系统</h3>
			       	<p>
			       	</p>
			       	
					<form name="tfMobileAuthForm" id="tfMobileAuthForm"
						target="hidden_frame" 
						action="<%=path %>/upload.do?dir=/download-center/apk&saveName=tfMobileAuth.apk&callback=uploadapk&backup=true" 
						method="post" ENCTYPE="multipart/form-data" >
						<input name="myfileapk" style="width:75px;display:none;" onclick="return clearRetInfo();" class="btn btn-info" type="file">
						<input type="button"  class="btn btn-info" value="上传APK" onclick="document.tfMobileAuthForm.myfileapk.click()"> 
						<span name="apkpath" ></span>
						&nbsp;&nbsp;
						<span><a href="?update=true&app_name=tfMobileAuth">更新检测地址</a></span>
						<br>
						
						<div style="height:5px;">&nbsp;</div>
		          		<input type="hidden" name="name" placeholder="name" value="信帧金融认证" />
			          	<input type="hidden" name="app_name" placeholder="app_name" value="tfMobileAuth"/>
			          	<input type="text" name="versionName" id="versionName" placeholder="版本(e.g: 1.2.2)" value="" onblur="versionChanged(this);"/>
			          	<span id="versionNameInfo"  for="versionName"></span>
			          	<input type="hidden" name="versionCode"  for="versionName" placeholder="versionCode(e.g: 122)" value="" /><br/>
			          	<textarea  name="description" placeholder="描述" rows="10" ></textarea>
			          	
		          		<iframe name="hidden_frame" style="display:none;"></iframe><br/>
						<input type="button" class="btn btn-primary " value="Submit" onclick="chekFileds(this);">
						<br>
						<br>
						<p name="ret"></p>
					</form>  
					
		        </div>
		        <div class="span3">
			          	<h3>手机车牌识别</h3>
			       	<p>
			       		
			       	</p>
					<form  name="TelLPRForm" id="TelLPRForm"
						target="hidden_frame" 
						action="<%=path %>/upload.do?dir=/download-center/apk&saveName=TeleframeVLPR.apk&callback=uploadapk&backup=true" 
						method="post" ENCTYPE="multipart/form-data" >
						<input name="myfileapk" style="width:75px;display:none;" onclick="return clearRetInfo();" class="btn btn-info" type="file">
						<input type="button"  class="btn btn-info" value="上传APK" onclick="document.TelLPRForm.myfileapk.click()"> 
						<span name="apkpath" ></span>
						&nbsp;&nbsp;
						<span><a href="?update=true&app_name=TelLPR">更新检测地址</a></span>
						<br>
		          		
						<div style="height:5px;">&nbsp;</div>
		          		<input type="hidden" name="name" placeholder="name" value="信帧车牌识别" />
			          	<input type="hidden" name="app_name" placeholder="app_name" value="TelLPR"/>
			          	<input type="text" name="versionName" id="versionName" placeholder="版本(e.g: 1.2.2)" value="" onblur="versionChanged(this);"/>
			          	<span id="versionNameInfo"  for="versionName"></span>
			          	<input type="hidden" name="versionCode"  for="versionName" placeholder="versionCode(e.g: 122)" value="" /><br/>
			          	<textarea  name="description" placeholder="描述" rows="10" ></textarea>
		          		
		          		<iframe name="hidden_frame" style="display:none;"></iframe><br/>
						<input type="button" class="btn btn-primary " value="Submit" onclick="chekFileds(this);" >
						
						<br>
						<p name="ret"></p>
					</form>  
					
		        </div>
				
			 </div>
	      </div><!-- end of hero-unit -->
	      
	      <div class="row" id="download-apks">
	      	<div id="download-Loading">
		      	<center>
		      		<image src="images/loading/loading_007.gif"><h2>Loading......</h2></image>
		      	</center>
	      	</div>
	<!-- 
	        
		       <div class="span3">
		          <h2>手机车牌识别</h2>
		          <p></p>
		          <p>
		          	<img src="qrservice?text=<%=baseServer %>/download-center/apk/TefLPR.apk" 
		          		class="img-rounded" style="width:240px;height:240px;">
		          	<br/>
		          	<a name="Download" href="/download-center/apk/TefLPR.apk" class="btn btn-info btn-large" >Download</a>
		          </p>
		          <p><a class="btn" href="#">View details &raquo;</a></p>
		        </div>
      -->  
      
	      </div>
	 </div> <!-- /container -->
	
	
	<script src="jquery/jquery.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
	<script src="jquery/jquery-ui.js"></script>
	
	
    <script>
    	$(document).ready(function(){
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
		var mForm=null;
    	function chekFileds(obj){
    		var form = $(obj).parent();
			mForm = form;
    		var item = form.children("input[name='name']");
    		if($.trim(item.val())==""){
    			alert("请填写APP名称!");
    			item.focus();
    			return false;
    		}
    		var item = form.children("input[name='versionName']");
    		if($.trim(item.val())==""){
    			alert("请填写版本!");
    			item.focus();
    			return false;
    		}
    		
    		var item = form.children("input[name='versionCode']");
    		if($.trim(item.val())==""){
    			alert("请填写版本!");
    			item.focus();
    			return false;
    		}
    		var item = form.children("textarea[name='description']");
    		if($.trim(item.val())==""){
    			alert("请填写描述!");
    			item.focus();
    			return false;
    		}
    		form.submit();
    	}
    	
    	function getSubVersion(val){
    		t = val.match(/([0-9]{1,2})(\.)([0-9]{1})(\.)([0-9]{1})/);
    		if(t==null){
    			return null;
    		}
    		return t[0];
    		//var str = t[0].replace(/\./g, "");
    		//return str;
    	}
    	
    	function versionChanged(obj){
    	
    		if($.trim(obj.value)=="")
    			return;
    		//方法 1
    		var val = obj.value;
    		var form = $(obj).parent();
    		var patrn = /^([0-9]{1,2})(\.)([0-9]{1})(\.)([0-9]{1})$/;
    		if (!patrn.exec(val)) {
    			var eg = getSubVersion ( val);
    			if(eg==null)
    				eg = "1.2.6";
    			form.children("#versionNameInfo").html("<font color=red>e.g:"+eg+"</font>");
    			obj.focus();
    			return;
			}
			var str = val.replace(/\./g, "");
		//	form.children("input[for='"+obj.name+"']").val(str); // 或者 	$("#versionCode").val(str); 
			form.children("input[name='versionCode']").val(str);
			form.children("#versionNameInfo").html("");
			
			return ;
    		
    		//方法 2
    		var val = $("#versionName").val();
    		t = val.match(/([0-9]{1,2})(\.)([0-9]{1})(\.)([0-9]{1})/);
    		if(t==null){
    			$("#versionNameInfo").html("<font color=red>e.g: 1.2.6</font>");
    			$("#versionName").focus();
    			return;
    		}
    		var str = t[0].replace(/\./g, "");
    		$("#versionCode").val(str);
    		$("#versionNameInfo").html("");
    		
    	}
    	
    	function clearRetInfo(){
    		$("p[name='ret']").html("");
    	}
    	function uploadapk(result){
    		if(result.error == 0){
    			$.get("FileServlet/save/apk", function(result){
    					if(result.error==0)
    						alert("上传成功!");
    					else {
							if(mForm!=null)
								mForm.children("p[name='ret']").html(result.msg+":"+result.data);
							else
								$("p[name='ret']").html(result.msg+":"+result.data);
    						alert("上传失败!");
    					}
				}, "json");
    		}else{
				if(mForm!=null)
					mForm.children("p[name='ret']").html(result.msg+",fileSize="+result.data.length);
				else
					$("p[name='ret']").html(result.msg+",fileSize="+result.data.length);
    			alert("上传失败");
    		}
		}
    </script>

  </body>
</html>
