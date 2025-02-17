<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<%@ taglib uri="http://khnt.com/tags/chart" prefix="chart" %>
<%
  String startDate = new SimpleDateFormat("yyyy").format(new Date())+"-01-01";
  String endDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title></title>
  <%@include file="/k/kui-base-list.jsp" %>
  <%@ include file="/k/kui-base-form.jsp"%>
  <%@ include file="/k/kui-base-chart.jsp"%>
  
  <script test="text/javascript">
    $(function () {//jQuery页面载入事件
    	init();
    });
    function init(){

        var column=null;
     	   column =[
 		 			{ display: '票号', name: 'INVOICE_NO',align: 'center', width: 78,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '发票类型', name: 'CLSS',align: 'center', width: 180,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '开票名称', name: 'COMPANY_NAME',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '检验部门', name: 'DEPT',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '合同号/编号', name: 'PAY_NO',align: 'center', width: 180,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '总金额', name: 'PAY_RECEIVED',align: 'center', width: 180,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '收费方式', name: 'PAY_TYPE',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '现金', name: 'CASH_PAY',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '转账', name: 'REMARK',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: 'POS', name: 'POS',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '上缴财政', name: 'HAND_IN',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '开票人', name: 'RECEIVE_MAN_NAME',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '开票日期', name: 'PAY_DATE',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}}
 		 			
 				]
     	   
     
 		
    		grid = $("#checkGrid").ligerGrid({
             columns:column, 
             enabledEdit: true,
             data:{Rows:[]},
             rownumbers:true,
             frozenRownumbers: false,
             usePager: false,
             height:'90%'
 	  	});
    }
    function query(){
      	$("body").mask("正在统计数据,请等待...");
        $.getJSON(encodeURI("feeStatisticsAction/deptDetail.do?startDate=${param.startDate}&endDate=${param.endDate}&dept=${param.dept}"),function(res){
        	var gridDataArr=new Array();
        	for(var i=0; i<res.rows.length;i++){
        		var rowData=new Object();
        		rowData.INVOICE_NO=res.rows[i].INVOICE_NO;
        		rowData.CLSS=res.rows[i].CLSS;
        		rowData.COMPANY_NAME=res.rows[i].COMPANY_NAME;
        		rowData.DEPT=res.rows[i].DEPT;
        		rowData.PAY_NO=res.rows[i].PAY_NO;
        		rowData.PAY_RECEIVED=res.rows[i].PAY_RECEIVED;
        		rowData.PAY_TYPE=res.rows[i].PAY_TYPE;
        		rowData.CASH_PAY=res.rows[i].CASH_PAY;
        		rowData.REMARK=res.rows[i].REMARK;
        		rowData.POS=res.rows[i].POS;
        		rowData.HAND_IN=res.rows[i].HAND_IN;
        		rowData.RECEIVE_MAN_NAME=res.rows[i].RECEIVE_MAN_NAME;
        		rowData.PAY_DATE=res.rows[i].PAY_DATE;
        		gridDataArr.push(rowData);
        	}

		    if(gridDataArr!=null){
			   grid.loadData({Rows:gridDataArr});
		    }
		    $("body").unmask();
        	
        });
        
        
    }
    

  </script>
</head>
<body onload="query()">
	<div class="item-tm">
    <div class="l-page-title2 has-icon has-note" style="height: 80px">
        <div class="l-page-title2-div"></div>
        <div class="l-page-title2-text"><h1>财务收入统计</h1></div>
        <div class="l-page-title2-note" style="height:20px;">按各开票类型，各部门收费统计（单位：<span>元</span>）</div>
        <div class="l-page-title2-icon">
        	<img src="k/kui/images/icons/32/statistics.png" border="0"/>
        </div>
        
    </div>
</div>
	
<div id="checkGrid" style="overflow: auto;"></div>
</body>
</html>