<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/modules/chat/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=request.getRequestURI()%>">
		<title>My JSP 'chat.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<script type="text/javascript" async="" src="js/ga.js"></script>
		

		<link rel="stylesheet" type="text/css" href="css/style.css"
			media="screen">

		<link rel="stylesheet" type="text/css" href="css/im.css"
			charset="utf-8" media="screen">

		<style>
		input{
			height:24px;
		}
</style>
	</head>

	<body>
		<script type="text/javascript" async="" src="js/im.0.02.1107.js"></script>
		<div id="site_title" class="site_title"></div>

		<div class="app_btn_wrapper" id=setup
			style="position: absolute; left: 220px; top: 10px;">
			<div class="app_btn"
				style="background-image: url(images/setting.gif);"></div>
			<div class="app_text" >
				设置
			</div>
		</div>

	<div class="app_btn_wrapper" style="position: absolute; left: 290px; top: 10px;"><div class="app_btn" style="background-image: url(images/share.gif);"></div><div class="app_text">邀请他人</div></div>
<div class="app_btn_wrapper" style="position: absolute; left: 360px; top: 10px;"><div class="app_btn" style="background-image: url(images/new-topic.gif);"></div><div class="app_text">建讨论组</div></div>
<div class="app_btn_wrapper" style="position: absolute; left: 430px; top: 10px;"><div class="app_btn" style="background-image: url(images/search.gif);"></div><div class="app_text">查找用户</div></div>
<div class="app_btn_wrapper" style="position: absolute; left: 500px; top: 10px;"><div class="app_btn" style="background-image: url(images/search.gif);"></div><div class="app_text">热门小组</div></div>

		<div class="app_btn_wrapper"
			style="position: absolute; left: 570px; top: 10px;">
			<div class="app_btn" style="background-image: url(images/help.gif);"></div>
			<div class="app_text">
				呼叫客服
			</div>
		</div>



		<div id=setdialog
			style="position: absolute; left: 396px; top: 94px; width: 550px; height: 420px; z-index: 14; font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: normal; font-family: 宋体;">
			<div
				style="position: absolute; left: 0px; top: 0px; width: 550px; height: 420px; opacity: 1; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 5px; border-bottom-left-radius: 5px; -webkit-box-shadow: rgb(102, 153, 204) 2px 2px 8px; box-shadow: rgb(102, 153, 204) 2px 2px 8px;">
				<img src="images/window-box-bg-0-0.gif"
					style="position: absolute; left: 0px; top: 0px; width: 6px; height: 26px;">
				<img src="images/window-box-bg-0-1.gif"
					style="position: absolute; left: 6px; top: 0px; width: 538px; height: 26px;">
				<img src="images/window-box-bg-0-2.gif"
					style="position: absolute; left: 544px; top: 0px; width: 6px; height: 26px;">
				<img src="images/window-box-bg-1-0.gif"
					style="position: absolute; left: 0px; top: 26px; width: 6px; height: 388px;">
				<img src="images/window-box-bg-1-1.gif"
					style="position: absolute; left: 6px; top: 26px; width: 538px; height: 388px;">
				<img src="images/window-box-bg-1-2.gif"
					style="position: absolute; left: 544px; top: 26px; width: 6px; height: 388px;">
				<img src="images/window-box-bg-2-0.gif"
					style="position: absolute; left: 0px; top: 414px; width: 6px; height: 6px;">
				<img src="images/window-box-bg-2-1.gif"
					style="position: absolute; left: 6px; top: 414px; width: 538px; height: 6px;">
				<img src="images/window-box-bg-2-2.gif"
					style="position: absolute; left: 544px; top: 414px; width: 6px; height: 6px;">
			</div>
			<div
				style="position: absolute; left: 0px; top: 0px; width: 550px; height: 24px;">
				<div
					style="position: absolute; background-color: rgb(255, 255, 255); left: 0px; top: 0px; width: 550px; height: 24px; opacity: 0.01; background-position: initial initial; background-repeat: initial initial;"></div>
				<img src="images/sphere.gif"
					style="position: absolute; display: none; left: 0px; top: 0px; width: 24px; height: 24px;">
				<div title="关闭"
					style="text-align: center; float: right; position: relative; cursor: pointer; top: 0px; right: 6px; color: rgb(238, 238, 238); font-style: normal; font-variant: normal; font-weight: normal; font-size: 12px; line-height: 16px; font-family: biondi, 'Arial Black', 'Bodoni MT Black'; height: 16px; width: 40px; opacity: 1;">
					<span style="display: block;" id=closesetup >X</span>
				</div>
				<span
					style="position: absolute; display: inline-block; left: 24px; top: 0px; height: 24px; line-height: 24px;">设置</span>
			</div>
			<div
				style="position: absolute; outline: none; left: 4px; top: 28px; width: 542px; height: 388px;">
				<div
					style="border: none; position: absolute; overflow-x: hidden; overflow-y: auto; left: 0px; top: 0px; width: 100px; height: 388px;">
					<span title="基本资料"
						style="display: inline-block; white-space: nowrap; cursor: pointer; position: relative; padding: 2px; width: 96px; height: 22px; line-height: 26px; background-color: rgb(255, 255, 255); font-weight: bold; background-position: initial initial; background-repeat: initial initial;">
						<span
						style="display: inline-block; position: absolute; left: 26px; top: 0px; height: 22px; line-height: 22px; vertical-align: top;">基本资料</span>
					</span>
					<span title="验证设置"
						style="display: inline-block; white-space: nowrap; cursor: pointer; position: relative; padding: 2px; width: 96px; height: 22px; line-height: 26px;">
						<span
						style="display: inline-block; position: absolute; left: 26px; top: 0px; height: 22px; line-height: 22px; vertical-align: top;">验证设置</span>
					</span>
					<span title="修改密码"
						style="display: inline-block; white-space: nowrap; cursor: pointer; position: relative; padding: 2px; width: 96px; height: 22px; line-height: 26px;">
						<span
						style="display: inline-block; position: absolute; left: 26px; top: 0px; height: 22px; line-height: 22px; vertical-align: top;">修改密码</span>
					</span>
				</div>
				<div
					style="border: none; background-color: rgb(255, 255, 255); position: absolute; left: 100px; top: 0px; width: 442px; height: 388px; background-position: initial initial; background-repeat: initial initial;">
					<div
						style="border: none; background-color: rgb(255, 255, 255); display: block; position: absolute; z-index: 1; left: 6px; top: 6px; width: 430px; height: 376px; background-position: initial initial; background-repeat: initial initial;">
						<br>
						<div style="position: relative; float: left; width: 65%;">
							<div
								style="position: relative; margin-left: 10px; float: left; line-height: 32px;">
								<table style="font: 12px 宋体">
									<tbody>
										<tr>
											<td>
												昵称:
											</td>
											<td>
												<input type="text" id="user_nick"
													style="border: 1px solid #999; background: #fff url(images/input-bg.gif); padding: 4px; width: 170px; height: 24px;"
													value="用户1078497">
											</td>
										</tr>
										<tr>
											<td>
												签名:
											</td>
											<td>
												<textarea id="user_bulletin"
													style="border: 1px solid #999; background: #fff url(images/input-bg.gif); padding: 4px; width: 170px; height: 48px; resize: none; -moz-resize: none; overflow-x: none; overflow-y: auto;"
													rows="2" maxlength="50">sign</textarea>
											</td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td>
												<label for="us_male">
													性别:
												</label>
											</td>
											<td>
												<label for="us_male">
													<input id="us_male" type="radio">
													男
												</label>
												<label for="us_female">
													<input id="us_female" type="radio">
													女
												</label>
											</td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td>
												<label for="us_province">
													家乡:
												</label>
											</td>
											<td>
												<select id="us_province">
													<option>
														北京
													</option>
													<option>
														上海
													</option>
													<option>
														天津
													</option>
													<option>
														重庆
													</option>
													<option>
														黑龙江
													</option>
													<option>
														吉林
													</option>
													<option>
														辽宁
													</option>
													<option>
														山东
													</option>
													<option>
														山西
													</option>
													<option>
														陕西
													</option>
													<option>
														河北
													</option>
													<option>
														河南
													</option>
													<option>
														湖北
													</option>
													<option>
														湖南
													</option>
													<option>
														海南
													</option>
													<option>
														江苏
													</option>
													<option>
														江西
													</option>
													<option>
														广东
													</option>
													<option>
														广西
													</option>
													<option>
														云南
													</option>
													<option>
														贵州
													</option>
													<option>
														四川
													</option>
													<option>
														内蒙古
													</option>
													<option>
														宁夏
													</option>
													<option>
														甘肃
													</option>
													<option>
														青海
													</option>
													<option>
														西藏
													</option>
													<option>
														新疆
													</option>
													<option>
														安徽
													</option>
													<option>
														浙江
													</option>
													<option>
														福建
													</option>
													<option>
														台湾
													</option>
													<option>
														香港
													</option>
													<option>
														澳门
													</option>
												</select>
											</td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td>
												<label for="us_year">
													生日:
												</label>
											</td>
											<td>
												<label for="us_year">
													<select id="us_year" title="选择年">
														<option value="1900">
															1900
														</option>
														<option value="1901">
															1901
														</option>
														<option value="1902">
															1902
														</option>
														<option value="1903">
															1903
														</option>
														<option value="1904">
															1904
														</option>
														<option value="1905">
															1905
														</option>
														<option value="1906">
															1906
														</option>
														<option value="1907">
															1907
														</option>
														<option value="1908">
															1908
														</option>
														<option value="1909">
															1909
														</option>
														<option value="1910">
															1910
														</option>
														<option value="1911">
															1911
														</option>
														<option value="1912">
															1912
														</option>
														<option value="1913">
															1913
														</option>
														<option value="1914">
															1914
														</option>
														<option value="1915">
															1915
														</option>
														<option value="1916">
															1916
														</option>
														<option value="1917">
															1917
														</option>
														<option value="1918">
															1918
														</option>
														<option value="1919">
															1919
														</option>
														<option value="1920">
															1920
														</option>
														<option value="1921">
															1921
														</option>
														<option value="1922">
															1922
														</option>
														<option value="1923">
															1923
														</option>
														<option value="1924">
															1924
														</option>
														<option value="1925">
															1925
														</option>
														<option value="1926">
															1926
														</option>
														<option value="1927">
															1927
														</option>
														<option value="1928">
															1928
														</option>
														<option value="1929">
															1929
														</option>
														<option value="1930">
															1930
														</option>
														<option value="1931">
															1931
														</option>
														<option value="1932">
															1932
														</option>
														<option value="1933">
															1933
														</option>
														<option value="1934">
															1934
														</option>
														<option value="1935">
															1935
														</option>
														<option value="1936">
															1936
														</option>
														<option value="1937">
															1937
														</option>
														<option value="1938">
															1938
														</option>
														<option value="1939">
															1939
														</option>
														<option value="1940">
															1940
														</option>
														<option value="1941">
															1941
														</option>
														<option value="1942">
															1942
														</option>
														<option value="1943">
															1943
														</option>
														<option value="1944">
															1944
														</option>
														<option value="1945">
															1945
														</option>
														<option value="1946">
															1946
														</option>
														<option value="1947">
															1947
														</option>
														<option value="1948">
															1948
														</option>
														<option value="1949">
															1949
														</option>
														<option value="1950">
															1950
														</option>
														<option value="1951">
															1951
														</option>
														<option value="1952">
															1952
														</option>
														<option value="1953">
															1953
														</option>
														<option value="1954">
															1954
														</option>
														<option value="1955">
															1955
														</option>
														<option value="1956">
															1956
														</option>
														<option value="1957">
															1957
														</option>
														<option value="1958">
															1958
														</option>
														<option value="1959">
															1959
														</option>
														<option value="1960">
															1960
														</option>
														<option value="1961">
															1961
														</option>
														<option value="1962">
															1962
														</option>
														<option value="1963">
															1963
														</option>
														<option value="1964">
															1964
														</option>
														<option value="1965">
															1965
														</option>
														<option value="1966">
															1966
														</option>
														<option value="1967">
															1967
														</option>
														<option value="1968">
															1968
														</option>
														<option value="1969">
															1969
														</option>
														<option value="1970">
															1970
														</option>
														<option value="1971">
															1971
														</option>
														<option value="1972">
															1972
														</option>
														<option value="1973">
															1973
														</option>
														<option value="1974">
															1974
														</option>
														<option value="1975">
															1975
														</option>
														<option value="1976">
															1976
														</option>
														<option value="1977">
															1977
														</option>
														<option value="1978">
															1978
														</option>
														<option value="1979">
															1979
														</option>
														<option value="1980">
															1980
														</option>
														<option value="1981">
															1981
														</option>
														<option value="1982">
															1982
														</option>
														<option value="1983">
															1983
														</option>
														<option value="1984">
															1984
														</option>
														<option value="1985">
															1985
														</option>
														<option value="1986">
															1986
														</option>
														<option value="1987">
															1987
														</option>
														<option value="1988">
															1988
														</option>
														<option value="1989">
															1989
														</option>
														<option value="1990">
															1990
														</option>
														<option value="1991">
															1991
														</option>
														<option value="1992">
															1992
														</option>
														<option value="1993">
															1993
														</option>
														<option value="1994">
															1994
														</option>
														<option value="1995">
															1995
														</option>
														<option value="1996">
															1996
														</option>
														<option value="1997">
															1997
														</option>
														<option value="1998">
															1998
														</option>
														<option value="1999">
															1999
														</option>
														<option value="2000">
															2000
														</option>
														<option value="2001">
															2001
														</option>
														<option value="2002">
															2002
														</option>
														<option value="2003">
															2003
														</option>
														<option value="2004">
															2004
														</option>
														<option value="2005">
															2005
														</option>
														<option value="2006">
															2006
														</option>
														<option value="2007">
															2007
														</option>
														<option value="2008">
															2008
														</option>
														<option value="2009">
															2009
														</option>
														<option value="2010">
															2010
														</option>
													</select>
													年&nbsp;
												</label>
												<label for="us_month">
													<select id="us_month">
														<option value="1">
															1
														</option>
														<option value="2">
															2
														</option>
														<option value="3">
															3
														</option>
														<option value="4">
															4
														</option>
														<option value="5">
															5
														</option>
														<option value="6">
															6
														</option>
														<option value="7">
															7
														</option>
														<option value="8">
															8
														</option>
														<option value="9">
															9
														</option>
														<option value="10">
															10
														</option>
														<option value="11">
															11
														</option>
														<option value="12">
															12
														</option>
													</select>
													月&nbsp;
												</label>
												<label for="us_day">
													<select id="us_day">
														<option value="1">
															1
														</option>
														<option value="2">
															2
														</option>
														<option value="3">
															3
														</option>
														<option value="4">
															4
														</option>
														<option value="5">
															5
														</option>
														<option value="6">
															6
														</option>
														<option value="7">
															7
														</option>
														<option value="8">
															8
														</option>
														<option value="9">
															9
														</option>
														<option value="10">
															10
														</option>
														<option value="11">
															11
														</option>
														<option value="12">
															12
														</option>
														<option value="13">
															13
														</option>
														<option value="14">
															14
														</option>
														<option value="15">
															15
														</option>
														<option value="16">
															16
														</option>
														<option value="17">
															17
														</option>
														<option value="18">
															18
														</option>
														<option value="19">
															19
														</option>
														<option value="20">
															20
														</option>
														<option value="21">
															21
														</option>
														<option value="22">
															22
														</option>
														<option value="23">
															23
														</option>
														<option value="24">
															24
														</option>
														<option value="25">
															25
														</option>
														<option value="26">
															26
														</option>
														<option value="27">
															27
														</option>
														<option value="28">
															28
														</option>
														<option value="29">
															29
														</option>
														<option value="30">
															30
														</option>
														<option value="31">
															31
														</option>
													</select>
													日&nbsp;
												</label>
											</td>
										</tr>
									</tbody>
								</table>
								<span id="base_set_res" style="color: red"></span>
							</div>
						</div>
						<div style="position: relative; float: left; width: 30%;">
							<div style="position: relative; float: left; right: 10px;">
								<img id="user_logo" style="width: 120px; height: 120px;"
									src="/images/contacts2.jpg">
								<div id="change_avatar" align="center"
									style="position: relative; top: 10px; left: 20px; width: 80px; height: 24px; cursor: pointer;">
									<button class="sign_btn">
										上传头像
									</button>
									<div
										style="padding: 0px; margin: 0px; position: absolute; left: 0px; top: 0px; width: 80px; height: 24px; opacity: 0.01;"
										title="点击更新头像">
										<iframe
											style="border: none; padding: 0px; margin: 0px; position: absolute; left: 0px; top: 0px; width: 80px; height: 24px; opacity: 0.01;"
											title="点击更新头像" src="">
											<html>
												<head></head>
												<body
													style="padding: 0px; margin: 0px; border: none; overflow: hidden;">
													<form id="upload_form" method="post"
														action="/im/group_set_logo/-.txt"
														enctype="multipart/form-data"
														style="padding: 0px; margin: 0px; border: none; overflow: hidden;">
														<input type="file" id="dcontent" hidefocus="true"
															title="点击更新头像"
															style="padding: 0px; margin: 0px; position: absolute; left: -5px; top: 0px; verflow: hidden; cursor: pointer; width: 85px; height: 24px; line-height: 24px; opacity: 0.01;"
															name="dcontent">
														<input type="hidden" name="MAX_FILE_SIZE" value="600">
														<input type="hidden" name="dnid" value="000000000010ad87">
														<input type="hidden" name="dgid" value="1078497">
														<input type="hidden" name="dtid" value="0">
													</form>
												</body>
											</html>
										</iframe>
									</div>
								</div>
							</div>
						</div>
						<div
							style="float: right; position: relative; top: 100px; right: 40px;">
							<button id="base_set_confirm" class="sign_btn">
								确认
							</button>
							&nbsp;
							<button id="base_set_cancel" class="sign_btn">
								取消
							</button>
						</div>
					</div>
					<div
						style="border: none; background-color: rgb(255, 255, 255); display: block; position: absolute; z-index: 0; visibility: hidden; left: 6px; top: 6px; width: 430px; height: 376px; background-position: initial initial; background-repeat: initial initial;"></div>
					<div
						style="border: none; background-color: rgb(255, 255, 255); display: block; position: absolute; z-index: 0; visibility: hidden; left: 6px; top: 6px; width: 430px; height: 376px; background-position: initial initial; background-repeat: initial initial;"></div>
				</div>
			</div>
		</div>
	<script src="<%=path %>/jquery/jquery.min.js"></script>
	<script src="<%=path %>/bootstrap/js/bootstrap.min.js"></script>
		
	<script src="<%=path %>/jquery/jquery-ui.js"></script>
    <script>
    	$(document).ready(function(){
    		 $( "#setdialog" ).draggable();
    	});
    	$("#setup").click(function(){
    		var show =  $( "#setdialog" ).css("display") ;
    		if(show!="none"){
    			$( "#setdialog" ).hide();
    		}else
    			$( "#setdialog" ).show();
    	});
    </script>
	</body>
</html>
