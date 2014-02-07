<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>卡口报表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script language="javascript" type="text/javascript" src="<%=basePath%>jquery/jquery.js"></script>
	<script language="javascript" type="text/javascript" src="<%=basePath%>modules/base/scripts/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=basePath%>modules/base/Highcharts-3.0.1/js/highcharts.js"></script>
	<script language="javascript" type="text/javascript" src="<%=basePath%>modules/base/Highcharts-3.0.1/js/modules/exporting.js"></script>
	<style>
		body{
			
		}
		table{border-collapse:collapse;border-spacing:0;border-left:1px solid #888;border-top:1px solid #888;background:#efefef;}
		th,td{border-right:1px solid #888;border-bottom:1px solid #888;padding:5px 2px;border: 1px solid #C1DAD7; }
		th{font-weight:bold;background-color:rgb(170, 233, 150);}
	</style>
	
		<script type="text/javascript">
			$(function () {
			    //getData("chart","${currentTime}");
			    
			     window.setTimeout('lazy_getData()',500);
			});
			
			function lazy_getData(){
				setTime($("#time").val());
			}
			
			function setTime(t){
				getData("chart",t);
			}
			
			function goback(){
				$("#chart").show();
				$("#report").hide();
				$("#pic").hide();
				var table=document.getElementById("table");
				for(var n=table.rows.length;n>1;n--){
					table.deleteRow(n-1);
				}
			}
			
			function goback2(){
				$("#chart").hide();
				$("#report").show();
				$("#pic").hide();
				$("#img").attr("src","");
			}
			
			function jump(){
				location.href=$("#img").attr("src");
			}
			
			function getdetail(id){
				
				$.ajax({
					type:"Post",
					url:"chart/clgetdata",
					data:{"type":"pic","id":id},
					success:function(retext){
						$("#img").attr("src",retext.split(",")[4]);
						$("#detail").html("车牌："+retext.split(",")[0]+"<br/>时间："+retext.split(",")[1]+"<br/>地点："+retext.split(",")[2]+"<br/>方向："+retext.split(",")[3]);
						$("#chart").hide();
						$("#pic").show();
						$("#report").hide();
					}
				})
			}
			
			function getreport(){
				var time=$("#time").val();
				$("#chart").hide();
				$("#pic").hide();
				$("#report").show();
				$("#loading").show();
				$.ajax({
					type:"Post",
					url:"chart/clgetdata",
					data:{"type":"table","time":time},
					success:function(retext){
						//alert(retext)
						var tr=retext.split("#");
						var table=document.getElementById("table");
						for(i=1;i<tr.length;i++){
							var nextRow = table.insertRow(i);
							var td=tr[i-1].split(",");
							for(j=0;j<6;j++){
			          			var newCell = nextRow.insertCell(j);
			          			if(j<5)
			          				newCell.innerHTML=td[j];
			          			else 
			          				newCell.innerHTML="<a href='javascript:getdetail("+td[0]+");'>查看</a>";
		          			}
						}
						$("#loading").hide();
					}
				})
			}
			
			function getData(type,time){
				$.ajax({
					type:"Post",
					url:"chart/clgetdata",
					data:{"type":type,"time":time},
					success:function(retext){
						if(retext=="" || retext==null){
							alert("无数据！！！");
							return;
						}
						
						var array = new Array();
						var re=retext.split("#");

						for(var i=0;i<re.length-1;i++){
							array[i]=[re[i].split(",")[0],parseInt(re[i].split(",")[1])];
						}
						$('#container').highcharts({
					        chart: {
					            plotBackgroundColor: null,
					            plotBorderWidth: null,
					            plotShadow: false
					        },
					        title: {
					            text: '<span style="font-family:黑体;">卡口检测</span>'
					        },
					        tooltip: {
					    	    formatter: function() {
						            return '<b>'+ this.point.name +'</b>: '+ Highcharts.numberFormat(this.percentage, 1) +'% ('+
						                         Highcharts.numberFormat(this.y, 0, ',') +' 个)';
						         }
					        },
					        plotOptions: {
					            pie: {
					                allowPointSelect: true,
					                cursor: 'pointer',
					                dataLabels: {
					                    enabled: true,
					                    color: '#000000',
					                    connectorColor: '#000000',
					                   	formatter: function() {
				                            return '<b>'+ this.point.name +'</b>: '+ Highcharts.numberFormat(this.percentage, 1) +'%' ;
				                        }
					                }
					            }
					        },
					        series: [{
					            type: 'pie',
					            name: '百分比：',
					            data: array
					        }],credits: {
						    	enabled: true,
						    	href: "http://www.teleframe.cn/",
							    text: "信帧电子"
							}
				    	});
					}
				})
			}
		</script>
  </head>
  <body>
  <center>
	  <div id="chart">
	   	 时间：<input type="text" value="${currentTime}" name="" id="time" style=width:120px;height:22px; class="Wdate"  onClick="WdatePicker({isShowToday:false,dateFmt:'yyyy-MM-dd',onpicked:function(dp){setTime(this.value)}})" readonly>
	   	 <div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
	   	 <div><a href="javascript:getreport();">查看报表</a></div>
	  </div>
  	  <div id="report" style="display:none;">
  	  	  <a href="javascript:goback();"  style="font-size:80%;">返回</a>
	  	  <table id="table" border=1px cellspacing=0 style="font-size:80%;width:392px;">
	  			<tr>
	  				<th align="center">编号</th>
	  				<th align="center">车牌</th>
	  				<th align="center">时间</th>
	  				<th align="center">起点</th>
	  				<th align="center">方向</th>
	  				<th align="center">操作</th>
	  			</tr>
	  	  </table>
	  	  <img id="loading" alt="" src="modules/chart/loading.gif?" style="display:none ">
  	  </div>
  	  <div id="pic" style="display:none">
  	  	<a href="javascript:goback2();">返回</a><br/>
  	  	<div id="detail" style="width:400px;text-align:left "></div>
  	  	<a href="javascript:jump();"><img id="img" alt="" src="" width=400 height=300 /></a>
  	  	
  	  	
  	  </div>
  </center>
  </body>
</html>
