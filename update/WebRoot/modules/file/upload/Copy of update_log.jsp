<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1";/>
    <base href="<%=basePath%>">
    <title>Update_log.jsp</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.4"></script>
	<style>
		body, html,#allmap {width: 100%;height: 100%;overflow: hidden;hidden;margin:0;}
		
		td,th{
		    border: 1px solid #C1DAD7;   
		    background: #fff;
		    font-size:13px;
		    padding: 6px 6px 6px 12px;
		    color: #4f6b72;
		}
		.hover
		{
		  background-color: #f00;
		}
		.odd{ background-color: #bbf;}
		.even{ background-color:#ffc; }
		.booter{
			margin-right: 10px;
		}
		.ipclass{
			cursor: hand;
		}
		
		.form-div{
	      	max-width: 510px;
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
	      .topbar span{
	   	  	color:#000;
	   	  	font-weight:bold;
		  }
		  .topbar:hover{
	   	  	cursor:move;		  
		  }
		  .result{  
		  	position:fixed;
			margin: auto auto 10px auto ;
		}
		
	</style>

  </head>
  
  <body>
  <div style="width:100%; border:0px #f00 solid; font-size:9px;float:left;">
   <center>
   		<P></P>
   		<table id="updateLogList" border =1>
   			<tr>
   				<th>ID</th>
   				<th>IP</th>
   				<th>PHONE</th>
   				<th>latitude</th>
   				<th>longitude</th>
   				<th>LasttTime</th>
   				<th>ShowMap</th>
   			</tr>
   		</table>
   		
   		<br>
   	
   	<div id="resultframe" class="form-div result" style="">
      	<div class=topbar >
			<span>&nbsp;</span>
		</div>
      	<div>
		<iframe name="location" style="width:500px;height:40px; border=0px;" 
			src="http:\/\/int.dpool.sina.com.cn\/iplookup\/iplookup.php?format=text&ip=114.249.229.211" ></iframe>
		</div>
	</div>	
	
	<hr/>
   </center>
	</div>
	
	<div id="allmap" style="width:100%;height:100px;border:1px solid #f00;">map</div>
	
	<br/>
	<br/>
	<br/>
	<br/>
  		
	<script src="jquery/jquery.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>
	<script src="jquery/jquery-ui.js"></script>
		
    <script type="text/javascript">
    
    $(document).ready(function(){
   		 //地图初始化
   		var map = new BMap.Map("allmap");
		var pre_point = new BMap.Point(116.318912, 40.03708); //116.318912  40.037083
		map.setCurrentCity("北京");          // 设置地图显示的城市 此项是必须设置的
		map.centerAndZoom(pre_point, 11);	
		map.enableScrollWheelZoom();   
		map.enableContinuousZoom();
		
		map.addControl(new BMap.MapTypeControl());
		map.setCurrentCity("北京");
	//	bm.addControl(new BMap.NavigationControl());
	}
		/*
    	var timer = null;
    	$(document).ready(function(){
    		$( "#resultframe" ).draggable();
    	
		//	var ipurl = "http:\/\/int.dpool.sina.com.cn\/iplookup\/iplookup.php?format=text&ip=114.249.229.211";
			$.get("update/log", "", updateLog, "text" );
    	});
    	
    	function updateLog(response,status,xhr){
			var ret = eval("("+response+")");
			var obj = ret.data;
   			$("#updateLogList tr:gt(0)").remove();
   			$(obj).each(function(index){
   				var log = obj[index];
   				var newRow = "<tr>"
   				newRow += "<td>"+log.id+"</td>";
   				newRow += "<td class=ipclass onclick=\"getIp(this);\" title='Show Location ?'>"+log.ip+"</td>";
   				newRow += "<td>"+log.phone+"</td>";
   				newRow += "<td>"+log.latitude+"</td>";
   				newRow += "<td>"+log.longitude+"</td>";
   				newRow += "<td>"+log.time+"</td>";
   				newRow += "<td><a href=\"javascript:showMap(\'"+log.latitude+"\', \'"+log.longitude+"\');\">ShowMap</a></td>";
   				newRow += "</tr>";
				$("#updateLogList tr:last").after(newRow);
   			});
			$("#updateLogList tr:gt(0)").hover(
			    function () { $(this).addClass("hover") },
			    function () { $(this).removeClass("hover") });
    	};
    	
    	//坐标转换完之后的回调函数
		translateCallback = function (point){
		    var marker = new BMap.Marker(point);
		    bm.addOverlay(marker);
		    var label = new BMap.Label("哦",{offset:new BMap.Size(20,-10)});
		    marker.setLabel(label); //添加百度label
		    bm.setCenter(point);
		    alert(point.lng + "," + point.lat);
		}

    	function showMap(y, x){
    		var xx = x;
			var yy = y;
			var gpsPoint = new BMap.Point(xx,yy);
			
			BMap.Convertor.translate(gpsPoint,0,translateCallback);     //真实经纬度转成百度坐标
    	}
    	
    	function getIp(obj){
    		var ipurl = "http:\/\/int.dpool.sina.com.cn\/iplookup\/iplookup.php?format=text&ip="+obj.innerHTML;
    		alert(location.src);
    		location.src = ipurl;
    	}
    	*/
    	
    </script>

  </body>
</html>
