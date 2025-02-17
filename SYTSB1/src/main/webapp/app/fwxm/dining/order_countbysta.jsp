<%@page import="com.khnt.utils.DateUtil"%>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <%@include file="/k/kui-base-list.jsp" %>
    <script type="text/javascript" src="app/fwxm/dining/js-chart/highcharts.js"></script>
    <script test="text/javascript">
    var data = null;
	
    $(function () {
    	var toDayDate="<%=DateUtil.getDate(new Date())%>";
    	$("#toolbar1").ligerToolBar({
			items: [
				"-",
				{icon: "date", text: "本周", click: function () {
					dateNs("bz", "startDate", "endDate", toDayDate);
				}},
				{icon: "date", text: "本月", click: function () {
					dateNs("by", "startDate", "endDate", toDayDate);
				}},
				{icon: "date", text: "本季度", click: function () {
					dateNs("bjd", "startDate", "endDate", toDayDate);
				}},
				{icon: "date", id: "wefwef", text: "本年", disabled: false, click: function () {
					dateNs("bn", "startDate", "endDate", toDayDate);
				}},
				"-",
				{icon: "date", text: "上周", click: function () {
					dateNs("sz", "startDate", "endDate", toDayDate);
				}},
				{icon: "date", text: "上月", click: function () {
					dateNs("sy", "startDate", "endDate", toDayDate);
				}},
				{icon: "date", text: "上季度", click: function () {
					dateNs("sjd", "startDate", "endDate", toDayDate);
				}},
				{icon: "date", text: "上年", click: function () {
					dateNs("sn", "startDate", "endDate", toDayDate);
				}},
				
				"-"
			]
		});
      
        $("#btn1").ligerButton({
        	icon:"count",
            click: function (){
            	var startDate = $("#startDate").val();
        	    var endDate = $("#endDate").val();
        	    
                $.post("dining/foodOrder/orderCount.do",{"startDate":startDate,"endDate":endDate},function(resp){
            		data = resp.data;
            		chart(data);
            	}); 
            },text:"统计"
        });
        
        $("#btn2").ligerButton({
        	icon:"excel-export",
            click: function (){
            	out();
            },text:"导出excel"
        });
        
        $("#form1").ligerForm();
        
        var startDate = $("#startDate").val();
	    var endDate = $("#endDate").val();
        $.post("dining/foodOrder/orderCount.do",{"startDate":startDate,"endDate":endDate},function(resp){
    		data = resp.data;
    		chart(data);
    	}); 
        
    });
    function chart(data){
    	var al = [];
    	var cate = [];
    	for(var i=0;i<data.length;i++){
    		al.push(data[i][0]);
    		if(data[i][4]==0){
    			cate.push('早餐');
    		}else if(data[i][4]==1){
    			cate.push('午餐');
    		}else{
    			cate.push('晚餐');
    		}
    	}
    	 var chart = {
    		      type: 'bar'
    		   };
    		   var title = {
    		      text: '订单数量统计情况'   
    		   };
    		   var subtitle = {
    		      text: ''  
    		   };
    		   var xAxis = {
    		      categories: cate,
    		      title: {
    		         text: null
    		      }
    		   };
    		   var yAxis = {
    		      min: 0,
    		      title: {
    		         text: 'Population (单)',
    		         align: 'high'
    		      },
    		      labels: {
    		         overflow: 'justify'
    		      }
    		   };
    		   var tooltip = {
    		      valueSuffix: ' 单'
    		   };
    		   var plotOptions = {
    		      bar: {
    		         dataLabels: {
    		            enabled: true
    		         }
    		      }
    		   };
    		   var legend = {
    		      layout: 'vertical',
    		      align: 'right',
    		      verticalAlign: 'top',
    		      x: -40,
    		      y: 100,
    		      floating: true,
    		      borderWidth: 1,
    		      backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
    		      shadow: true
    		   };
    		   var credits = {
    		      enabled: false
    		   };
    		   
    		   var series= [{
    				            
    					        name: '各餐点订单总数',
    					        data: al
    				        }
    		   ];     
    		      
    		   var json = {};   
    		   json.chart = chart; 
    		   json.title = title;   
    		   json.subtitle = subtitle; 
    		   json.tooltip = tooltip;
    		   json.xAxis = xAxis;
    		   json.yAxis = yAxis;  
    		   json.series = series;
    		   json.plotOptions = plotOptions;
    		   json.legend = legend;
    		   json.credits = credits;
    		   $('#container').highcharts(json);
    }
   
   
    </script>
	<style type="text/css">
	.top {
	    color: black;
	    height: 125px;
	}
	.counter{
	    font-weight:bold;
	    font-size: 14px;
	    text-decoration:none;
		padding: 0 5px;
	}
	</style>
	</script>
</head>
<body>
<div class="item-tm">
	<div id="toolbar1"></div>
</div>
<form name="form1" id="form1" action="" getAction="" target="_blank">
<div class="item-tm" >
	<div class="l-page-title2 has-icon has-note" style="height: 80px">
		<div class="l-page-title2-div"></div>
		<div class="l-page-title2-text"><h1>按时间段统计</h1></div>
		<div class="l-page-title2-note">以各餐点订单总数量为统计对象。</div>
		<div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"></div>
		<div class="l-page-title-content" style="top:15px;height:80px;">
			<table border="0" cellpadding="0" cellspacing="0">
				 <tr>
					<td width="80" style="text-align:center">统计时间：从</td>
					<td width="" width="100"><input id="startDate" name="startDate" type="text" ltype="date" value="2017-1-1"  /></td>
					<td width="" align="center">至</td>
					<td width="" width="100"><input id="endDate" name="endDate" type="text" ltype="date" value="<%=DateUtil.getDate(new Date()) %>"  /></td>
					<td width="" style="text-align: right;float: left; padding-left: 5px;"><div id="btn1" >  </div></td>
				</tr>
			</table>
		</div>
	</div>
</div>
</form>
<!-- <div id="main" style="width:100%;height:80%;"></div>  -->
<div id="container" style="width: 550px; height: 400px; margin: 0 auto"></div>
</body>
</html>