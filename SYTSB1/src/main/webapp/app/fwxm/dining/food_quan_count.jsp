<%@page import="com.khnt.utils.DateUtil"%>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <%@include file="/k/kui-base-list.jsp" %>
        <script src="app/statistics/js/echarts.js"></script>
    	<!-- <script src="app/statistics/js/china.js"></script> -->
    <script test="text/javascript">
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
         	     var diningName = $("#diningName").val();
                 $.post("dining/foodOrder/foodQuanCount.do",{"startDate":startDate,"endDate":endDate,"diningName":diningName},function(resp){
                	 echart(resp);
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
        var diningName = $("#diningName").val();
        var startDate = $("#startDate").val();
	    var endDate = $("#endDate").val();
        $.post("dining/foodOrder/foodQuanCount.do",{"startDate":startDate,"endDate":endDate,"diningName":diningName},function(resp){
        	echart(resp);
    	}); 
        
    });
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
		<div class="l-page-title2-note">以某个时间段订单中各菜品的份数为统计对象。</div>
		<div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"></div>
			<div class="l-page-title-content" style="top:15px;height:80px;">
			<table border="0" cellpadding="0" cellspacing="0">
				 <tr>
				 	<td width="80" style="text-align:center">餐名：</td>
					<td width=""><input id="diningName" name="diningName" type="text" ltype="select" ligerUi="{initValue:'1', data:[{ text: '早餐', id: '0' },{ text: '午餐', id: '1' },{ text: '晚餐', id: '2' }]}" validate="{required:true}" /></td>
					<td width="80" style="text-align:center">统计时间：从</td>
					<td width="" width="100"><input id="startDate" name="startDate" type="text" ltype="date" value="2017-1-1"  /></td>
					<td width="" align="center">至</td>
					<td width="" width="100"><input id="endDate" name="endDate" type="text" ltype="date" value="<%=DateUtil.getDate(new Date()) %>"  /></td>
					<td width="" style="text-align: right;float: left; padding-left: 5px;"><div id="btn1" ></td>
				</tr>
			</table>
		</div>
	</div>
</div>
</form>
<div style="width:900px;margin:0 auto;"><div id="main" style="width: 800px;height:400px;"></div></div>
     
</body>
<script type="text/javascript">
function echart(data){
	var diningName = (data.diningName==0?"早餐":data.diningName==1?"午餐":"晚餐");
	var map = data.data;
	
	var total = 0;
	$.each(map,function(i,obj){
		total+=obj.value;
	});
	var chartData0 = [];
	var chartData1 = [];
	var chartData2 = [];
	chartData0[0] = "总份数"; 
	chartData1[0] = total;
	chartData2[0] = 0;
	$.each(map,function(j,obj){
		chartData0[j+1] = obj.key; 
		chartData1[j+1] = obj.value;
		var f = 0;
		for(var k=0;k<j+1;k++){
			f += chartData1[k+1];
		}
		chartData2[j+1] = total-f;
	});
	
	
	
	var myChart = echarts.init(document.getElementById('main'));
	option = {
		    title: {
		        text: "当前时间段"+diningName+'订单菜品组成（单位:份）'
		    },
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        },
		        formatter: function (params) {
		            var tar = params[1];
		            return tar.name + '<br/>' + tar.seriesName + ' : ' + tar.value;
		        }
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis: {
		        type : 'category',
		        splitLine: {show:false},
		        data : chartData0//['总费用','房租','水电费','交通费','伙食费','日用品数']
		    },
		    yAxis: {
		        type : 'value'
		    },
		    series: [
		        {
		            name: '辅助',
		            type: 'bar',
		            stack:  '总量',
		            itemStyle: {
		                normal: {
		                    barBorderColor: 'rgba(0,0,0,0)',
		                    color: 'rgba(0,0,0,0)'
		                },
		                emphasis: {
		                    barBorderColor: 'rgba(0,0,0,0)',
		                    color: 'rgba(0,0,0,0)'
		                }
		            },
		            data: chartData2//[0, 1700, 1400, 1200, 300, 0]
		        },
		        {
		            name: '生活费',
		            type: 'bar',
		            stack: '总量',
		            label: {
		                normal: {
		                    show: true,
		                    position: 'inside'
		                }
		            },
		            data:chartData1//[2900, 1200, 300, 200, 900, 300]
		        }
		    ]
		};

	myChart.setOption(option);
}

</script>
</html>