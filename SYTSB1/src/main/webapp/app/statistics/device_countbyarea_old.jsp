<%@page import="com.khnt.utils.DateUtil"%>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <%@include file="/k/kui-base-list.jsp" %>
    
    <script test="text/javascript">
    $(function () {
    	var toDayDate="<%=DateUtil.getDate(new Date())%>";
    	/*$("#toolbar1").ligerToolBar({
			items: [
				"-",
				{icon: "date-bz", text: "本周", click: function () {
					dateNs("bz", "startDate", "endDate", toDayDate);
				}},
				{icon: "date-by", text: "本月", click: function () {
					dateNs("by", "startDate", "endDate", toDayDate);
				}},
				{icon: "date-bjd", text: "本季度", click: function () {
					dateNs("bjd", "startDate", "endDate", toDayDate);
				}},
				{icon: "date-bn", id: "wefwef", text: "本年", disabled: false, click: function () {
					dateNs("bn", "startDate", "endDate", toDayDate);
				}},
				"-",
				{icon: "date-sz", text: "上周", click: function () {
					dateNs("sz", "startDate", "endDate", toDayDate);
				}},
				{icon: "date-sy", text: "上月", click: function () {
					dateNs("sy", "startDate", "endDate", toDayDate);
				}},
				{icon: "date-sjd", text: "上季度", click: function () {
					dateNs("sjd", "startDate", "endDate", toDayDate);
				}},
				{icon: "date-sn", text: "上年", click: function () {
					dateNs("sn", "startDate", "endDate", toDayDate);
				}},
				
				"-"
			]
		});*/
    	
    	$("#btn1").css({"height":"20px","line-height":"20px"})
        $("#btn2").css({"height":"20px","line-height":"20px"})
      
        $("#btn1").ligerButton({
        	icon:"count",
            click: function (){
            	init();
            },text:"统计"
        });
        
        $("#btn2").ligerButton({
        	icon:"excel-export",
            click: function (){
            	out();
            },text:"导出excel"
        });
        
        $("#form1").ligerForm();
        
        init();       
    });
    function init(){
    	$("body").mask("正在加载数据...");
    	var startDate = $("#startDate").val();
	    var endDate = $("#endDate").val();
	    $.post("sta/analyse/deviceCountByArea.do",{"startDate":startDate,"endDate":endDate},function(data){
	    	inputGrid = $("#countGrid").ligerGrid({
	            columns: [
					 { display: '行政区划', name: 'areaName',align: 'center', width: 130,totalSummary:
                     {
                         render: function (e) {  
                        	return "<div'>合计</div>"; 
                        	} 
                     }},
					 { display: '锅炉预警', name: '1',align: 'center', width: 120,render: function (rowdata, rowindex, value) {
                         //var a= "<a href='javascript:detail("+rowdata.areaCode+",\"device_classify1000\""+")' title='查看'>"+value+" </a> ";
                         var a = value;
                         return a;
                	 },totalSummary:
                     {
                         type: 'sum',
                         render: function (e) {  
                        	return "<div'>" + e.sum + "</div>"; 
                        	} 
                     }},
	           		 { display: '压力容器预警', name: '2',align: 'center', width: 120,render: function (rowdata, rowindex, value) {
                         //var a= "<a href='javascript:detail("+rowdata.areaCode+",\"device_classify2000\""+")' title='查看'>"+value+" </a> ";
                         var a = value;
                         return a;
                	 },totalSummary:
                     {
                         type: 'sum',
                         render: function (e) {  
                        	return "<div'>" + e.sum + "</div>"; 
                        	} 
                     }},
                     { display: '电梯', columns:[
		           		 { display: '预警', name: '3',align: 'center', width: 120,render: function (rowdata, rowindex, value) {
	                         //var a= "<a href='javascript:detail("+rowdata.areaCode+",\"device_classify3000\""+")' title='查看'>"+value+" </a> ";
		                         var a = value;
		                         return a;
		                	 },totalSummary:
		                     {
		                         type: 'sum',
		                         render: function (e) {  
		                        	return "<div'>" + e.sum + "</div>"; 
		                        	} 
		                     }
	                     },
	                     { display: '未报检', name: '32',align: 'center', width: 120,render: function (rowdata, rowindex, value) {
	                         //var a= "<a href='javascript:detail("+rowdata.areaCode+",\"device_classify3000\""+")' title='查看'>"+value+" </a> ";
		                         var a = "<font color='red'>"+value+"</font>";
		                         return a;
		                	 },totalSummary:
		                     {
		                         type: 'sum',
		                         render: function (e) {  
		                        	return "<div'>" + e.sum + "</div>"; 
		                        	} 
		                     }
	                     }
                     ]},
                     /*
	           		 { display: '起重机械预警', name: '4',align: 'center', width: 120,render: function (rowdata, rowindex, value) {
                         var a= "<a href='javascript:detail("+rowdata.areaCode+",\"device_classify4000\""+")' title='查看'>"+value+" </a> ";
                         return a;
                	 },totalSummary:
                     {
                         type: 'sum',
                         render: function (e) {  
                        	return "<div'>" + e.sum + "</div>"; 
                        	} 
                     }},
	           		 { display: '厂内机动车辆预警', name: '5',align: 'center', width: 120,render: function (rowdata, rowindex, value) {
                         var a= "<a href='javascript:detail("+rowdata.areaCode+",\"device_classify5000\""+")' title='查看'>"+value+" </a> ";
                         return a;
                	 },totalSummary:
                     {
                         type: 'sum',
                         render: function (e) {  
                        	return "<div'>" + e.sum + "</div>"; 
                        	} 
                     }},*/
	           		 { display: '大型游乐设施预警', name: '6',align: 'center', width: 120,render: function (rowdata, rowindex, value) {
                         //var a= "<a href='javascript:detail("+rowdata.areaCode+",\"device_classify6000\""+")' title='查看'>"+value+" </a> ";
                         var a = value;
                         return a;
                	 },totalSummary:
                     {
                         type: 'sum',
                         render: function (e) {  
                        	return "<div'>" + e.sum + "</div>"; 
                        	} 
                     }},
                     /*
	           		 { display: '压力管道预警', name: '7',align: 'center', width: 120,render: function (rowdata, rowindex, value) {
                         var a= "<a href='javascript:detail("+rowdata.areaCode+",\"device_classify8000\""+")' title='查看'>"+value+" </a> ";
                         return a;
                	 },totalSummary:
                     {
                         type: 'sum',
                         render: function (e) {  
                        	return "<div'>" + e.sum + "</div>"; 
                        	} 
                     }},*/
	           		 { display: '客运索道预警', name: '9',align: 'center', width: 120,render: function (rowdata, rowindex, value) {
                         //var a= "<a href='javascript:detail("+rowdata.areaCode+",\"device_classify9000\""+")' title='查看'>"+value+" </a> ";
                         var a = value;
                         return a;
                	 },totalSummary:
                     {
                         type: 'sum',
                         render: function (e) {  
                        	return "<div'>" + e.sum + "</div>"; 
                        	} 
                     }}/*,
	           		 { display: '合计', name: 'total',align: 'center', width: 120,type:'int',totalSummary:
                     {
                         type: 'sum',
                         render: function (e) {  
                        	return "<div'>" + e.sum + "</div>"; 
                        	} 
                     }}*/
	            ], 
	            onAfterShowData:function(currentData){
	            	$("body").unmask();
	            	$(".l-grid-totalsummary-cell").css({"border":"1px solid #C9D6E9"});
	            	var trs=$("#countGrid .l-grid-body-table tr");
	            	for(i=0;i<trs.length;i++){
	            		$($(trs[i]).find("td")[0]).css({"background":"#e0ecff"});
	            	}
	            },
	            data:{Rows:eval(JSON.stringify(data.data))},//json格式的字符串转为对象
	            height:'100%',
	            usePager:false,
	            width:'100%'
	       	 },"json");
        });
    }	    
    function detail(areaCode,main_sort_code){
		top.$.dialog({
			width : 1000,
			height : 600,
			lock : true,
			title : "特种设备信息",
			content : 'url:app/statistics/count_devicearea_list.jsp?areaCode='+areaCode+'&sortCode='+main_sort_code,
			data : {
				"window" : window
			}
		});
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
</head>
<body>
<div class="item-tm">
	<div id="toolbar1"></div>
</div>
<form name="form1" id="form1" action="" getAction="" target="_blank">
<div class="item-tm" >
	<div class="l-page-title2 has-icon has-note" style="height: 80px">
		<div class="l-page-title2-div"></div>
		<div class="l-page-title2-text"><h1>按行政区划及设备类别统计</h1></div>
		<div class="l-page-title2-note">以超期未检的设备为统计对象。</div>
		<div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"></div>
		<!-- 
		<div class="l-page-title-content" style="top:15px;height:80px;">
		
		<table border="0" cellpadding="0" cellspacing="0">
			 <tr>
							<td width="80" style="text-align:center">统计时间：从</td>
							<td width="" width="100"><input id="startDate" name="startDate" type="text" ltype="date" value="2014-01-01"  /></td>
							<td width="" align="center">至</td>
							<td width="" width="100"><input id="endDate" name="endDate" type="text" ltype="date" value="<%=DateUtil.getDate(new Date()) %>"  /></td>
							</tr>
							<tr>
							<td width=""></td>
							<td width="" style="text-align: right;float: left;padding-top: 5px"><div id="btn1" >  </div></td>
						</tr>
					</table>
		
		</div>
		 -->
	</div>
</div>
</form>
<div position="center"  >
        <div id="countGrid"  ></div>   
 </div>
</body>
</html>