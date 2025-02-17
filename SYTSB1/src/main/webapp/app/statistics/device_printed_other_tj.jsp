<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="java.util.Calendar"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <%@include file="/k/kui-base-list.jsp" %>
    <%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
    <script test="text/javascript">
    $(function () {
    	/* $("#btn1").css({"height":"20px","line-height":"20px"})
        $("#btn2").css({"height":"20px","line-height":"20px"}) */
      
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
            },text:"导出"
        });
        
        $("#form1").ligerForm();     
        init();       
    });
    
    
    function init(){
    	var columns;
		var startDate = $("#startDate").val();
	    var endDate = $("#endDate").val();
	    var equipmentVariety = $("#equipmentVariety").val();
	    var equipmentVariety_name = $("#equipmentVariety_name").val();
	    if(equipmentVariety!=null&&equipmentVariety!=""){
	    	if(equipmentVariety.substring(0,1)=='4'){
	    		title=
	    		columns=[
					{ display: '机电（起重机械）种类打印', columns:[
						{ display: '起重机械', name: 'device_name',align: 'center', width: 150},
                     	{ display: '机电六部', columns:[
							 { display: '定期检验', name: 'jd6_dj_count',align: 'center', width: 100},
			           		 { display: '监督检验', name: 'jd6_jj_count',align: 'center', width: 100},
		                     { display: '委托检验', name: 'jd6_wj_count',align: 'center', width: 100}
                     	]},
		           		 { display: '合计', name: 'total',align: 'center', width: 100}
                     ]}
	            ];
	    	}else if(equipmentVariety.substring(0,1)=='5'){
	    		columns=[
					{ display: '机电（场（厂）内专用机动车辆）种类打印', columns:[
						{ display: '场（厂）内专用机动车辆', name: 'device_name',align: 'center', width: 150},
                     	{ display: '机电六部', columns:[
			           		 { display: '监督检验', name: 'jd6_jj_count',align: 'center', width: 100},
		                     { display: '委托检验', name: 'jd6_wj_count',align: 'center', width: 100}
                     	]},
		           		 { display: '合计', name: 'total',align: 'center', width: 100}
                     ]}
	            ];
	    	}else if(equipmentVariety.substring(0,1)=='6'){
	    		columns=[
					{ display: '机电（大型游乐设施）种类打印', columns:[
						{ display: '大型游乐设施', name: 'device_name',align: 'center', width: 150},
                     	{ display: '机电六部', columns:[
							 { display: '定期检验', name: 'jd6_dj_count',align: 'center', width: 100},
			           		 { display: '监督检验', name: 'jd6_jj_count',align: 'center', width: 100}
                     	]},
		           		 { display: '合计', name: 'total',align: 'center', width: 100}
                     ]}
	            ];
	    	}else if(equipmentVariety.substring(0,1)=='9'){
	    		columns=[
					{ display: '机电（客运索道）种类打印', columns:[
						{ display: '客运索道', name: 'device_name',align: 'center', width: 150},
                     	{ display: '机电六部', columns:[
							 { display: '定期检验', name: 'jd6_dj_count',align: 'center', width: 100}
                     	]},
		           		 { display: '合计', name: 'total',align: 'center', width: 100}
                     ]}
	            ];
	    	}else{
	    		$.ligerDialog.alert("暂无此类设备统计信息！");
	    		return
	    	}
	    }else{
	    	columns=[
				{ display: '机电（起重机械）种类打印', columns:[
					{ display: '起重机械', name: 'device_name',align: 'center', width: 150},
                 	{ display: '机电六部', columns:[
						 { display: '定期检验', name: 'jd6_dj_count',align: 'center', width: 100},
		           		 { display: '监督检验', name: 'jd6_jj_count',align: 'center', width: 100},
	                     { display: '委托检验', name: 'jd6_wj_count',align: 'center', width: 100}
                 	]},
	           		 { display: '合计', name: 'total',align: 'center', width: 100}
                 ]}
            ];
	    }
	    $.post("sta/analyse/devicePrintedCount_JD6.do",{"equipmentVariety":equipmentVariety,"equipmentVariety_name":equipmentVariety_name,"startDate":startDate,"endDate":endDate}, function(resp){
	    	inputGrid = $("#countGrid").ligerGrid({
	            columns: columns, 
	            data:{Rows:eval(JSON.stringify(resp.data))},//json格式的字符串转为对象
	            height:'100%',
	            usePager:false,
	            width:'100%'
	       	 },"json");
        });
    }	    
    
    function out(){
    	$("body").mask("正在导出数据,请等待...");
    	$("#form1").attr("action","sta/analyse/exportPrintedCount_JD6.do");
    	$("#form1").submit();
    	$("body").unmask();
    };
    function valueChange(val,text){
    	if(text!=""&&text!="undefined"){
    		$("#equipmentVariety_name").val(text);
    	}
	}
    </script>
</head>
<%
	String firstDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
	String curDate  = DateUtil.getDateTime("yyyy-MM-dd", new Date());
%>
<body>
<form name="form1" id="form1" method="post" action="" getAction="" target="_blank">
<div class="item-tm" >
	<div class="l-page-title2 has-icon has-note" style="height: 80px">
		<div class="l-page-title2-div"></div>
		<div class="l-page-title2-text"><h1>机电六部已打印的定检、监检、委检报告统计表</h1></div>
		<div class="l-page-title2-note">以各检验类别、设备类别为统计对象，已打印检验报告数量单位：台。</div>
		<div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"/></div>
		<div class="l-page-title-content" style="top:15px;height:80px;">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="80" style="text-align:center;">设备类别：</td>
					<td width="110">
							<input id="equipmentVariety_name" name="equipmentVariety_name" type="hidden" value="起重机械"/>
							<u:combo name="equipmentVariety" code="device_type" attribute="initValue:'4000',onSelected:valueChange"/>
					</td>
					<td width="80" style="text-align:center;">统计时间：</td>
					<td width="110">
							<input id="startDate" name="startDate" type="text" ltype="date" value="<%=firstDate %>"/>
					</td>
					<td width="" align="center">到</td>
					<td  width="110">
						<input id="endDate" name="endDate" type="text" ltype="date" value="<%=curDate %>"/>
					</td>
					<td colspan="6" width="" style="text-align: right;padding-left: 5px;">
						<div id="btn1"></div>
						<div id="btn2"></div>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
</form>
<div position="center">
	<div id="countGrid"></div>   
</div>
</body>
</html>