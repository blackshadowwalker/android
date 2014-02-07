<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String thisPath = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+thisPath+"/";
%>


<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	 <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1";/>
    <title>车牌识别用户检测更新日志</title>
    
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.4"></script>
	<script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js"></script>
	
	<style>
		body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;}
		
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
		 .hiddenclass{
		 	display:none;
		 }
		
	</style>

  </head>
  
  <body>

<table style="width:100%; height:100%;">
<tr >
<td  style="width:450px;" valign=top>
	<center>
		<a href="javascript:FreshData();" >刷新</a>
		<span id="status"></span>
	</center>
	<hr>
	<div style="width:450px;height:100%;border:0px #f00 solid; font-size:9px;float:left;overflow:scroll">
   	<center>
   		<P></P>
   		<table id="updateLogList" border =1>
   			<tr>
   				<th class=hiddenclass>ID</th>
   				<th>IP</th>
   				<th>PHONE</th>
   				<th class=hiddenclass>latitude</th>
   				<th class=hiddenclass>longitude</th>
   				<th >Time</th>
   				<th class=hiddenclass>ShowMap</th>
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
</td>	

<td style="width:100%;">	
	<div id="allmap" >map</div>
</td>
</tr>
</table>
	
	<script src="<%=thisPath%>/jquery/jquery.min.js"></script>
	<script src="<%=thisPath%>/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=thisPath%>/jquery/jquery-ui.js"></script>
	
    <script type="text/javascript">
    
    	//地图初始化
		var bm = new BMap.Map("allmap");
		var pre_point = new BMap.Point(116.318912, 40.03708); //116.318912  40.037083
		bm.setCurrentCity("北京");          // 设置地图显示的城市 此项是必须设置的
		bm.centerAndZoom(pre_point, 17);	
		bm.enableScrollWheelZoom(); 
		
		$(document).ready(function(){
    		$( "#resultframe" ).draggable();
			$.get("<%=thisPath%>/update/log", "", updateLog, "text" );
    	});
		
		function FreshData(){
			$.get("<%=thisPath%>/update/log", "", updateLog, "text" );
		}
		
		function updateLog(response,status,xhr){
			var ret = eval("("+response+")");
			var obj = ret.data;
   			$("#updateLogList tr:gt(0)").remove();
   			$(obj).each(function(index){
			
   				var log = obj[index];
				if(log.phone!=undefined && log.phone!="")
				{
					var newRow = "<tr>"
					newRow += "<td class=hiddenclass>"+log.id+"</td>";
					newRow += "<td class=ipclass onclick=\"getIp(this);\" title='Show Location ?'>"+log.ip+"</td>";
					newRow += "<td class=ipclass onclick=\"showMap('"+log.latitude+"', '"+log.longitude+"',\'"+log.phone+"\');\" title='showMap'>"+log.phone+"</td>";
					newRow += "<td class=hiddenclass>"+log.latitude+"</td>";
					newRow += "<td class=hiddenclass>"+log.longitude+"</td>";
					newRow += "<td class=ipclass  title=\"   "+log.version+"\">"+log.time+"</td>";
				//	newRow += "<td><a href=\"javascript:showMap(\'"+log.latitude+"\', \'"+log.longitude+"\');\">ShowMap</a></td>";
					newRow += "</tr>";
					$("#updateLogList tr:last").after(newRow);
				}
   			});
			$("#updateLogList tr:gt(0)").hover(
			    function () { $(this).addClass("hover") },
			    function () { $(this).removeClass("hover") });
    	};
		
		var mLabel = "这里哦";
		//坐标转换完之后的回调函数
		translateCallback = function (point){
		    var marker = new BMap.Marker(point);
		    bm.addOverlay(marker);
		    var label = new BMap.Label(mLabel,{offset:new BMap.Size(20,-10)});
		    marker.setLabel(label); //添加百度label
		    bm.setCenter(point);
		}

    	function showMap(y, x, phone){
			if(x==0 && y==0)
			{
				$("#status").html("<h3><font color=red>经纬度为空!</font></h3>");
				return ;
			}
			$("#status").html("经度:"+x + "  纬度:"+y);
			mLabel = phone;
    		var xx = x;
			var yy = y;
			var gpsPoint = new BMap.Point(xx,yy);
			BMap.Convertor.translate(gpsPoint,0,translateCallback);     //真实经纬度转成百度坐标
    	}

	
    </script>

  </body>
</html>
