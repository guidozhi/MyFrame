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
            },text:"导出"
        });
        
        $("#form1").ligerForm();    
        $("input[name='_device_type-txt']").ligerComboBox().selectValue('3000');  
        init();       
    });
    
    
    function init(){
		var startDate = $("#startDate").val();
	    var endDate = $("#endDate").val();
	    var device_type = $("#device_type").val();
	    if(device_type==""){
	    	$.ligerDialog.alert("请先选择设备类别！");
	    	return;
	    }
	    var selText = $("input[id='device_type-txt']").ligerComboBox().findTextByValue(device_type);
	    $.post("sta/analyse/deviceCheckedCount.do",{"startDate":startDate,"endDate":endDate,"device_type":device_type}, function(resp){
	    	inputGrid = $("#countGrid").ligerGrid({
	            columns: [
					{ display: selText, columns:[
						{ display: '检验部门', name: 'check_unit',align: 'center', width: 150,totalSummary:
                     		{
		                         render: function (e) {  
		                        	return "<div>合计</div>"; 
		                        	} 
		                     }},
                     	{ display: '检验类别', columns:[
							 { display: '定期检验', name: 'cur_dj_count',align: 'center', width: 100,totalSummary:
								{
		                        	type: 'sum',
		                         	render: function (e) {  
		                        		return "<div>" + e.sum + "</div>"; 
		                        	} 
		                     	}
		                     },
			           		 { display: '监督检验', name: 'cur_jj_count',align: 'center', width: 100,totalSummary:
		                     	{
		                         	type: 'sum',
		                         	render: function (e) {  
		                        		return "<div>" + e.sum + "</div>"; 
		                        	} 
		                     	}
		                     },
			           		 { display: '委托检验', name: 'cur_wj_count',align: 'center', width: 100,totalSummary:
		                     	{
		                         	type: 'sum',
		                         	render: function (e) {  
		                        		return "<div>" + e.sum + "</div>"; 
		                        	} 
		                     	}
		                     }
                     	]},
		           		 { display: '合计', name: 'cur_jy_total',align: 'center', width: 100,totalSummary:
	                     	{
	                         	type: 'sum',
	                         	render: function (e) {  
	                        		return "<div>" + e.sum + "</div>"; 
	                        	} 
	                     	}
	                     },
		           		 { display: '去年同期合计', name: 'last_jy_total',align: 'center', width: 120,totalSummary:
	                     	{
	                         	type: 'sum',
	                         	render: function (e) {  
	                        		return "<div>" + e.sum + "</div>"; 
	                        	} 
	                     	}
	                     },
		           		 { display: '与去年同期比较', name: 'compare_count',align: 'center', width: 120,totalSummary:
	                     	{
	                         	type: 'sum',
	                         	render: function (e) {  
	                        		return "<div>" + e.sum + "</div>"; 
	                        	} 
	                     	}
	                     }
                     ]}
	            ], 
	            data:{Rows:eval(JSON.stringify(resp.data))},//json格式的字符串转为对象
	            height:'100%',
	            usePager:false,
	            width:'100%'
	       	 },"json");
        });
    }	    
    
    function out()
    {
    	var device_type = $("#device_type").val();
	    if(device_type==""){
	    	$.ligerDialog.alert("请先选择设备类别！");
	    	return;
	    }
	    var selText = $("input[id='device_type-txt']").ligerComboBox().findTextByValue(device_type);
	    $("#device_name").val(selText);
    	$("body").mask("正在导出数据,请等待...");
    	$("#form1").attr("action","sta/analyse/exportCheckedCount.do");
    	$("#form1").submit();
    	$("body").unmask();
    };
    </script>
</head>
<%
	String firstDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
	String curDate  = DateUtil.getDateTime("yyyy-MM-dd", new Date());
%>
<body>
<form name="form1" id="form1" action="" getAction="" target="_blank">
<input type="hidden" name="device_name" id="device_name"/>
<div class="item-tm" >
	<div class="l-page-title2 has-icon has-note" style="height: 80px">
		<div class="l-page-title2-div"></div>
		<div class="l-page-title2-text"><h1>各检验部门检验业务统计表</h1></div>
		<div class="l-page-title2-note">以各检验部门、检验类别、设备类别为统计对象，检验设备数量单位：台。</div>
		<div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"/></div>
		<div class="l-page-title-content" style="top:15px;height:80px;">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="60" style="text-align:center">设备类别：</td>
					<td width="100">
						<u:combo name="device_type" code="device_classify" validate="required:true"   /> 
					</td>
					<td width="80" style="text-align:center;">统计时间：</td>
					<td width="110">
							<input id="startDate" name="startDate" type="text" ltype="date" value="<%=firstDate %>"/>
					</td>
					<td width="" align="center">到</td>
					<td  width="110">
						<input id="endDate" name="endDate" type="text" ltype="date" value="<%=curDate %>"/>
					</td>
					<td width="" style="text-align: right;float: left;padding-left: 5px;padding-top: 5px">
						<div id="btn1"></div><div id="btn2"></div>
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