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
<script type="text/javascript" src="app/common/lodop/LodopFuncs.js"></script>
  
  <script test="text/javascript">
    $(function () {//jQuery页面载入事件

		//打印
		$("#btn1").css({"height":"20px","width":"50px","padding-buttom":"10px"});
        //导出
		$("#btn2").css({"height":"20px","width":"50px","line-height":"18px"});
		$("#btn1").ligerButton({
            icon:"print",
            click: function (){
            	printInfo();
            },
            text:"打印"
        });
        $("#btn2").ligerButton({
            icon:"excel-export",
            click: function (){
                out();
            },
            text:"导出"
        });
		initGrid();
    });
    function initGrid(){
       var column=null;
    	   column =[
		 			{ display: '编号', name: 'bh',align: 'center', width: 78,totalSummary:{ render: function (e){return "" }}},
		 			{ display: '费用类型', name: 'fylx',align: 'center', width: 180,totalSummary:{ render: function (e){return "" }}},
		 			{ display: '报销人', name: 'bxr',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}},
		 			{ display: '报销日期', name: 'bxrq',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}},
		 			{ display: '单位', name: 'dw',align: 'center', width: 180,totalSummary:{ render: function (e){return "" }}},
		 			{ display: '部门', name: 'bm',align: 'center', width: 180,totalSummary:{ render: function (e){return "" }}},
		 			{ display: '金额', name: 'je',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}},
		 			{ display: '处理人', name: 'clr',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}},
		 			{ display: '处理时间', name: 'clsj',align: 'center', width: 80,totalSummary:{ render: function (e){return "" }}}
		 			
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
        $.getJSON(encodeURI("feeStatisticsAction/deptFeeDetail.do?startDate=${param.startDate}&endDate=${param.endDate}&dept=${param.dept}&clss=${param.clss}"),function(res){
        	
        	var gridDataArr=new Array();
        	for(var i=0; i<res.rows.length;i++){
        		console.log(res.rows[i].BSDATE);
        		var rowData=new Object();
        		rowData.bh=res.rows[i].IDENTIFIER;
        		rowData.fylx=res.rows[i].CLSS;
        		rowData.bxr=res.rows[i].PEOPLECONCERNDE;
        		rowData.bxrq=res.rows[i].BSDATE;
        		rowData.dw=res.rows[i].UNIT;
        		rowData.bm=res.rows[i].DEPARTMENT;
        		rowData.je=res.rows[i].MONEY;
        		rowData.clr=res.rows[i].HANDLE_NAME;
        		rowData.clsj=res.rows[i].HANDLE_TIME;
        		gridDataArr.push(rowData);
        	}

		    if(gridDataArr!=null){
			   grid.loadData({Rows:gridDataArr});
		    }
		    $("body").unmask();
        	
        });
        
        
    }
    

	
	 function out(){
	        $("body").mask("正在导出数据,请等待..."); 
	        $("#form1").submit();
	        $("body").unmask();
	    };
	// 打印
	function printInfo(){
		CreateOneFormPage();
		LODOP.PREVIEW();	
		
	}

	function CreateOneFormPage(){	
			LODOP=getLodop();
			// 设置打印样式  
			var strBodyStyle="<style> table{border:2;text-align:center;margin-left:60px;} table,td { border: 0 solid #000000;border-collapse:collapse;font-size:12px } "+
			"</style>";
			// 设置打印方式
			LODOP.SET_PRINT_PAGESIZE(2, 0, 0,"B5");	// 1 纵向打印 2 横向打印
			var printReportContent = "";
			// 循环上一命令（页码）
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
			printReportContent += "<table width='1000' bordercolor='black' style='border-collapse:collapse;'  border='1' id='table'>";
			printReportContent += "<caption><center><font size='3'><b>财务报账费用统计明细</b></font></center></caption>";
			printReportContent += "<tr align='center' height='20px'><td width='30'>编号</td>"+
			"<td width='30'>费用类型</td><td width='30'>报销人</td><td width='30'>报销日期</td><td >单位</td><td width='60'>部门</td>"
			+"<td width='60'>金额</td><td width='30'>处理人</td><td width='30'>处理时间</td>"
			+"</tr>";
			
			var data = {"startDate":'${param.startDate }',"endDate":'${param.endDate }',
					 "dept":'${param.dept}','clss':'${param.clss}'};

			 var rows = {};
			 $.ajax({
					type: "POST",
					url: "feeStatisticsAction/deptFeeDetail.do", 
					data:data,
					async : false,
					success : function(res) {
						rows=res.rows;
					}

	        });
			 for (var i=0;i<rows.length;i++){
				 printReportContent +="<tr align='center' height='20px'><td>"+rows[i].IDENTIFIER+"</td><td>"+rows[i].CLSS+"</td><td>"+rows[i].PEOPLECONCERNDE+"</td>"+
				 "<td>"+rows[i].BSDATE+"</td><td>"+rows[i].UNIT+"</td><td>"+rows[i].DEPARTMENT+"</td>"
				 +"<td>"+rows[i].MONEY+"</td><td>"+rows[i].HANDLE_NAME+"</td><td>"+rows[i].HANDLE_TIME+"</td></tr>";
			}
			 printReportContent +="</table>";
			// 获取打印内容
			var strFormHtml=strBodyStyle+"<body>"+printReportContent+"</body>";
			LODOP.NewPage();	// 强制分页
			// 打印表格（上边距、左边距、宽、高、打印内容）
			LODOP.ADD_PRINT_TABLE(20,0,"100%","100%",strFormHtml);	
	}
  </script>
</head>
<body onload="query()">


	<div style="height: 0px">
		<form name="form1" id="form1" action="feeStatisticsAction/exportDeptFeeMX.do"  target="_blank" method="post">
			<input name="dept" type="hidden" value="${param.dept}"/>
			<input name="clss" type="hidden" value="${param.clss}"/>
			<input name="startDate" type="hidden" value="${param.startDate }"/>
			<input name="endDate" type="hidden" value="${param.endDate }"/>
		</form>
	</div>

	
	
	<div class="item-tm">
    <div class="l-page-title2 has-icon has-note" style="height: 80px">
        <div class="l-page-title2-div"></div>
        <div class="l-page-title2-text"><h1>财务报账费用统计</h1></div>
        <div class="l-page-title2-note" style="height:20px;">按各经济类型，各部门报销费用统计（单位：<span>元</span>）</div>
        <div class="l-page-title2-icon">
        	<img src="k/kui/images/icons/32/statistics.png" border="0"/>
        </div>
        <div class="l-page-title-content" style="top:25px;left:300px;height:80px;"> 
            <table border="0" cellpadding="0" cellspacing="0" width="">
                <tr>
                  
                    
                    
                    
                    <td colspan="1" align="right">
                        <div id="btn1"></div>
                        <div id="btn2"></div>
                        <div id="btn3"></div>
                        <div id="btn4"></div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
	
	
	<div style="height: 0px">
		<form name="form1" id="form1" action="feeStatisticsAction/exportDeptDetail.do"  target="_blank" method="post">
			<input name="dept" type="hidden" value="${param.dept}"/>
			<input name="clss" type="hidden" value="${param.clss}"/>
			<input name="startDate" type="hidden" value="${param.startDate }"/>
			<input name="endDate" type="hidden" value="${param.endDate }"/>
		</form>
	</div>
	
	
<div id="container" position="center" style="display:none;width:99%;height:89%;">
    <div id="grid"></div>   
</div>
    	







<div id="checkGrid" style="overflow: auto;"></div>
</body>
</html>