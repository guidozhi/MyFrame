<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="java.util.Calendar"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <%@include file="/k/kui-base-list.jsp" %>
    <%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
    <script src="app/statistics/js/echarts.js"></script>
    <script test="text/javascript">
    var countData;
    var model='0';//"0"默认状态为图表模式
    var grid;
    $(function () {
        $("#btn1").ligerButton({
        	icon:"count",
            click: function (){
            	query();
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
        query();
    });
    
    
    function init(){
		var startDate = $("#startDate").val();
	    var endDate = $("#endDate").val();

	       var column=null;
	    	   column =[
			 			{ display: '部门', name: 'bm',align: 'center', width: 178,totalSummary:{ render: function (e){return "合计" }}},
			 			//{ display: '合计', name: 'hj',align: 'center', width: 180,totalSummary:{ render: function (e){return "" }}},
			 			{ display:'非常满意',columns:[
			 			                          {display:'个数',name:'fcmygs',width:100,align:'center'},
			 			                          {display:'百分比',name:'fcmybfb',width:100,align:'center'}]},
			 			{display:'满意',columns:[
			 			                       {display:'个数',name:'mygs',width:100,align:'center'},
			 			                      {display:'百分比',name:'mybfb',width:100,align:'center'}]},
			 			{display:'一般',columns:[
			 					 			                       {display:'个数',name:'ybgs',width:100,align:'center'},
			 					 			                      {display:'百分比',name:'ybbfb',width:100,align:'center'}]},
			 			{ display: '回访数量', name: 'hfsl',align: 'center', width: 78,totalSummary:{ render: function (e){return "" }}}
			 					 					 			
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
//       	$("body").mask("正在统计数据,请等待...");

      	var startTime=$("#startTime").val();
      	var endTime=$("#endTime").val();
      	
        $.getJSON(encodeURI("disciplinePlanAction/selectTj.do?startTime="+startTime+"&endTime="+endTime),function(res){

    		console.log(res);
        	var gridDataArr=new Array();
        	for(var i=0; i<res.data.length;i++){
        		console.log(res.data[i]);
        		var hj=parseInt(res.data[i].FCMY)+parseInt(res.data[i].MY)+parseInt(res.data[i].YB);
        		var rowData=new Object();
        		rowData.bm=res.data[i].SJ;
        		rowData.hj=hj;
        		rowData.fcmygs=res.data[i].FCMY;
        		rowData.fcmybfb=decimal(parseInt(res.data[i].FCMY)/hj*100,2);
        		rowData.mygs=res.data[i].MY;
        		rowData.mybfb=decimal(parseInt(res.data[i].MY)/hj*100,2);
        		rowData.ybgs=res.data[i].YB;
        		rowData.ybbfb=decimal(parseInt(res.data[i].YB)/hj*100,2);
        		rowData.hfsl=res.data[i].HFSL;
        		gridDataArr.push(rowData);
        	}

		    if(gridDataArr!=null){
			   grid.loadData({Rows:gridDataArr});
		    }
		   $("body").unmask();
        	
        });
        
        
    }
    //四舍五入num数字，V保留个数
    function decimal(num,v){
    	var vv = Math.pow(10,v);
    	return Math.round(num*vv)/vv;
    	}
    function out()
    {
    	$("body").mask("正在导出数据,请等待...");
    	$("#form1").attr("action","disciplinePlanAction/exportCheckedCount.do");
    	$("#form1").submit();
    	$("body").unmask();

//         var id = Qm.getValueByName("id");
//         var url="disciplinePlanAction/exportCheckedCount.do?id=";
//         console.log(url);
//         download(url,"post",id);
    };
	 function download(url, method, id){
         jQuery('<form action="'+url+'" method="'+(method||'post')+'">' +  // action请求路径及推送方法
             '<input type="text" name="id" value="'+id+'"/>' + // id
             '</form>')
             .appendTo('body').submit().remove();
     }
	 
	 
    </script>
</head>
<%
String startDate = new SimpleDateFormat("yyyy").format(new Date())+"-01-01";
String endDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
%>
<body style="overflow: auto;" >
<form name="form1" id="form1" action="" getAction="" target="_blank">
<input type="hidden" name="device_name" id="device_name"/>
<div class="item-tm" >
	<div class="l-page-title2 has-icon has-note" style="height: 80px">
		<div class="l-page-title2-div"></div>
		<div class="l-page-title2-text"><h1>工作服务统计</h1></div>
		<div class="l-page-title2-note"></div>
		<div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"/></div>
		<div class="l-page-title-content" style="top:15px;height:80px;">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					
					<td width="80" style="text-align:center;">统计时间：</td>
					<td width="110">
							<input id="startTime" name="startTime" type="text" ltype="date" value="<%=startDate %>"/>
					</td>
					<td width="" align="center">至</td>
					<td  width="110">
						<input id="endTime" name="endTime" type="text" ltype="date" value="<%=endDate %>"/>
					</td>
					<td width="" style="text-align: right;float: left;padding-left: 5px;">
						<div id="btn1"></div><div id="btn2"></div><div id="btn3"></div>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
</form>

<div id="checkGrid" style="overflow: auto;"></div>
 
</body>
</html>