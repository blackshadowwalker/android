<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>更新检测系统</title>
		<style type="text/css">
		li{
			height:30px;
			float:left;
			margin-left:20px;
		}
		</style>
	</head>
	<body>

		<center>
			<h1>
				更新检测系统
			</h1>
			<div style="width: 200px;">
				<ul>
					<li>
						<a href="log">日志查看</a>
					</li>
					<li>
						<a href="apk">文件上传</a>
					</li>
				</ul>
			</div>
		</center>
		<%--@ include  file="modules/file/upload/update_log.jsp" --%>


	</body>
</html>