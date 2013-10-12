<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String target = (String)request.getParameter("target");
if(target==null)
	target="";
%>

<!DOCTYPE html>
<html lang=en>
  <head>
    <title>Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1";/>
	
	<!-- bootstrap -->
	<link href="<%=path %>/bootstrap/css/bootstrap.min.css" rel="stylesheet" rel="stylesheet">
	<link href="<%=path %>/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
	<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="<%=path %>/js/html5shiv.js"></script>
    <![endif]-->
    
    <script src="<%=path %>/jquery/jquery.min.js"></script>
	   
	<style>
		.errorMessage{
			width:200px;
			color:red;
		}
		.labelclass{
			text-align: right;
			font-size:18px;
			width:110px;
		}
		
      loginbody {
        padding: 10px; /* 60px to make the container go all the way to the bottom of the topbar */
		background-color: #fafafa;
		font-size:15px;
		align:center;
      }
      .form-div{
      	max-width: 350px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        vertical-align:middle;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
	  .form-signin {
	 	 margin: 15px 29px 20px 29px;
      }
      .form-signin .form-signin-heading,
      .form-signin .checkbox {
        margin-bottom: 10px;
      }
      .topbar{
      	margin:0 0 0 0 ;
       	padding: 5px 0px 5px;
      	border:1px #f0f0f0 solid;
      	text-align:center;
      	font-size:22px;
      	background-color: #f0f0f0;
      	-webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
                
      }
   
	  
	 
    </style>
  </head>
  <body>
	<div class="container loginbody">
	 <div class="form-div">
	 	<div class=topbar >
			Login
		</div>
	 	
      <form name="loginform" class="form-signin" action="<%=path %>/login!smallLogin.action" target="_self" method="post">
        <br/>
        <input type="hidden" name="target" value="_parent" > 
        <div  id=i_usernameDiv  class="">
	        <div class="input-prepend" >
		        <span class="add-on">
		            <i class="icon-user"></i>
		        </span>
		        <input type="text" class="span3" placeholder="user name" name="user.username"  value="${user.username}"  label="username" />
		        <span id=i_usernameError >
					<s:property value="errors['user.username'][0]" />
				</span>
			</div>
		</div>
        
       <div id=i_passwordDiv >
	        <div class="control-group input-prepend">
		        <span class="add-on">
		            <i class="icon-lock"></i>
		        </span>
		        <input type="password" class="span3 error" placeholder="Password" name="user.password" value="${user.password}" label="password" />
				<span id=i_passwordError class=errorMessage>
					<s:property value="errors['user.password'][0]" />
				</span>
			</div>
		</div>
		
        <label class="checkbox " >
          <input type="checkbox" value="remember-me"> Remember me
        </label>
        
        <button class="btn btn-middle btn-primary" type="submit">Sign in</button>
        <span>&nbsp;&nbsp;</span>
		<input class="btn btn-middle btn-info"  type="reset" onclick="reset()"  value="Reset" />
        
      </form>
      </div>
	</div> <!-- /container -->
	
	<script>
		$(document).ready(function(){
			var target = "<%=target%>";
			$("[name='user.username']").focus();
			if($.trim($("#i_usernameError").text())!=""){
				$("#i_usernameDiv").attr("class","control-group error");
			}else if($.trim($("#i_passwordError").text())!=""){
				$("#i_passwordDiv").attr("class","control-group error");
			}else{
				if(target!=""){	
					if(confirm())
						window.parent.location.reload();
				}
			}
		});
		function reset(){
			$("[name='user.username']").val("");
			$("[name='user.password']").val("");
		}
	</script>

	<script src="<%=path %>/bootstrap/js/bootstrap.min.js"></script>
		
  </body>
</html>