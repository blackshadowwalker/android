<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<HTML lang="zh-CN"><HEAD>
<TITLE>提示页面</TITLE>
<STYLE rel="stylesheet">
/*top*/
body{margin:0 auto; background:#fff; font-size:12px; font-family:simsun; font-weight:normal;}
img { border:none;}
ul, ol, li, p, dl, dt, dd{margin:0; padding:0; list-style:none;}
.font_red{color:#f00; font-size:12px; }
.font_gray{color:#333;}
a.font_gray:link, a.font_gray:visited, a.font_gray:active{color:#333; text-decoration:underline; font-size:12px;}
a.font_gray:hover{color:#f00;}
.clear{clear:both;}

/* content */
.full{width:560px; margin:0 auto;}
.full dl.error{width:560px;  background:url(error/images/pic_contbg.gif) 0 0 repeat-y; border-bottom:1px solid #ebebeb; margin:100px 0 10px;}
.full dl.error dt{height:44px; line-height:44px;  background:url(error/images/pic_bg.gif) -20px -18px no-repeat; text-align:left; padding:0 15px; vertical-align:center;}
.full dl.error dt img{padding-top:13px; width:167px; height:20px;  background:url(error/images/pic_bg.gif) -110px -309px no-repeat;}
.full dl.error dd{text-align:center;}
.full dl.error dd span.error500{padding:50px 30px 40px 0; display:inline-block;}
.full dl.error dd span.error500 img{width:198px; height:74px;  background:url(error/images/pic_bg.gif) -20px -375px no-repeat;}
.full dl.error dd span.errortext{padding:10px 0 40px 0; display:inline-block;}
.full dl.error dd span.errortext img{width:329px; height:44px;  background:url(error/images/pic_bg.gif) -239px -375px no-repeat;}
.full dl.error dd span.btn_back{padding:0 0 50px 0; display:inline-block;}
.full dl.error dd span.btn_back img{width:122px; height:42px;  background:url(error/images/pic_bg.gif) -432px -179px no-repeat;}
.error404{padding-top:15px ; }
.error404 img{padding-top:10px; width:220px; height:100px;  background:url(error/images/pic_bg.gif) -170px -128px no-repeat;}

</STYLE>


<META name="GENERATOR" content="MSHTML 9.00.8112.16457"></HEAD>
<BODY>
		<DIV>

			<DIV class="full">
				<DL class="error">
					<DT>
						<IMG title="logo" alt="404 提示页面" src="error/images/pic_dot.gif">
					</DT>
					<DD class=error404 >
						<IMG   title="logo" alt="404 提示页面" src="error/images/pic_dot.gif">
					</DD>
					<DD>
						<SPAN class="errortext"> <IMG title="服务器暂时无法响应您的请求"
								alt="服务器暂时无法响应您的请求" src="error/images/pic_dot.gif"> </SPAN>
					</DD>
					<DD>
						<SPAN class="btn_back"> <A href="/" target="blank"><IMG title="返回" alt="返回" src="error/images/pic_dot.gif"> </A> </SPAN>
					</DD>
				</DL>
			</DIV>
			<DIV
				style="margin: 50px auto auto; width: 600px; text-align: center;">
				<link rel='stylesheet' type='text/css'
					href='error/css/pub_footer_2012.css' />
				<div id="pub_footerall" class="pub_footerall">
					<dl>
						<dt></dt>
						<dd>
							<a href="company/about.html" target="_blank">公司简介</a>|
							<a href="company/recruit.html" target="_blank">招贤纳士</a>|
							<a href="company/marketing.html" target="_blank">广告服务</a>|
							<a href="company/account.html" target="_blank">银行汇款帐号</a>|
							<a href="company/contact.html" target="_blank">联系方式</a>|
							<a href="company/statement.html" target="_blank">版权声明</a>|
							<a href="company/layer.html" target="_blank">法律顾问</a>|
							<a href="mailto:karl.li@teleframe.net">问题报告</a>
						</dd>
						<dd>
							<a
								href="http://wpa.qq.com/msgrd?v=3&amp;uin=1505974441&amp;site=qq&amp;menu=yes"
								target="_blank" class="qq">QQ客服</a>
							<a href="forums/Service" target="_blank" class="online">论坛反馈</a>
							<a href="mailto:karl.li@teleframe.net" class="email">联系邮箱：karl.li@teleframe.net</a>
							<span class="phone">服务热线：400-000-1111</span>
						</dd>
						<dd>
							京&nbsp;ICP&nbsp;证&nbsp;000000&nbsp;号
						</dd>
						<dd>
							信帧电子技术有限公司 版权所有
						</dd>
						<dd>
							Copyright © 2011-2013, CSDN.NET, All Rights Reserved&nbsp;
							<a
								href="http://www.hd315.gov.cn/beian/view.asp?bianhao=010202001032100010"
								target="_blank"> <img
									src="http://csdnimg.cn/pubfooter/images/gongshang_logos.gif"
									alt="GongshangLogo" title="">
							</a>
						</dd>
					</dl>
				</div>
			</DIV>
	</BODY>
</HTML>


