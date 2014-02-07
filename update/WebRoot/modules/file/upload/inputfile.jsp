<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<body>
<p>
	action="upload.do?dir=/upload"<br>
</p>
<form action="<%=path %>/upload.do?dir=/upload" method="post" ENCTYPE="multipart/form-data" >

			<input name="myfile" type="file"><br>
			<input name="myfile" type="file"><br>
			<input type="submit"><br>
</form>
<hr size=3>
<p>
	action="upload.do?dir=/download-center/apk&saveName=test.apk&backup=true"<br>
</p>
<form action="<%=path %>/upload.do?dir=/download-center/apk&saveName=test.apk&backup=true" method="post" ENCTYPE="multipart/form-data" >

			<input name="myfile" type="file"><br>
			<input name="myfile" type="file"><br>
			<input type="submit"><br>
</form>
<hr size=3>

<p>
	action="upload.do?callback=callback"<br>
</p>
<form action="<%=path %>/upload.do?callback=callback" method="post" target="hidden_frame" ENCTYPE="multipart/form-data" >
			<input name="myfile" type="file"><br>
			<input name="myfile" type="file"><br>
			<input name="myfile" type="file"><br>
			<input type="submit"><br>
			<p id="ret">
			</p>
</form>
<iframe name="hidden_frame" width=900 height=400></iframe>
<script>
	function callback(ret){
		var result = document.getElementById("ret");
		result.innerHTML = ret.data[0].msg;
	}
</script>

</body>