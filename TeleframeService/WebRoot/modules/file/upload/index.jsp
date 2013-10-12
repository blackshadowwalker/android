<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>File Uplaod Service</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
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
	<h2>  File Uplaod Service</h2>
	<h3>Usage:</h3>
	RFC 1867 'form based file upload in HTML'.<br />
	 
	Content type of the POST request must be set to multipart/form-data <br />
	upload action url : upload.do? <br /> 
	
	<br />
	<strong>Query parameter: </strong><br />
	
	dir = destination directory of the file relative to uploadroot. 
		If dir startwith '/'(e.g: upload?dir=/upload), the file directory relative to tomcat webapps. 
		Default is 'tempdir' sub webapp directory. <br />
	<br />
	uploadroot = destination root directory, default is webapp directory. <br />
	<br />
	fileTypes = the files types to upload, format: mp4,avi,txt etc. <br />
	<br />
	fileNum = the order of cur file to upload at onece submit. <br />
	
	<br />
	Returns: Formated to json<br /> 
	<strong>e.g:</strong><br />
	{"error":0,"msg":"Uploaded 1 files","data":[{"error":0,"filename":"android.131005190538.sql","uploadroot":"E:/WebServer/Tomcat 6.0/webapps","savePath":"/upload","savename":"20131006141123882_0_0.sql"}]}
	<br>
	<br />
	error=0 means there's no error.
	<br />
	
	<br />
	<br />
	Demo:<br />
	<a href="modules/file/upload/inputfile.jsp" >input type=File</a> <br />


  </body>
</html>
