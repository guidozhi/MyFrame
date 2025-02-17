<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>不符合报告明细列表</title>
		<%@include file="/k/kui-base-list.jsp"%>
		<script type="text/javascript">
		var w = window.screen.availWidth;
		var h = window.screen.availHeight;
	var bar =[];
 	bar =[ 
		{ text:'查看纠错报告', id:'showReport',icon:'detail', click: showReport}
		//,{ text:'查看新报告', id:'showNewReport',icon:'detail', click: showNewReport}
 	]
 	
 	var qmUserConfig = {
		sp_fields:[
	        {name:"report_sn", id:"report_sn", compare:"like"}
	    ],
		tbar:bar,
	   	listeners: {
	    	selectionChange : function(rowData,rowIndex){
	       		var count=Qm.getSelectedCount();//行选择个数
	         	Qm.setTbar({showReport:count==1});//,showNewReport:count==1
	     	},
	    	afterQmReady:function(){
	      		Qm.searchData();
	   		}
		}
	};
	
	// 查看纠错报告
	function showReport(){	
		var id = Qm.getValueByName("info_id").toString();
		var report_id = Qm.getValueByName("report_id").toString();	// 报告类型
		if(id !="" && id !=null && report_id !="" && report_id !=null ){
			var url = "report/query/showReport.do?id="+id+"&report_id="+report_id;
			top.$.dialog({
				width : w, 
				height : h, 
				lock : true, 
				title:"纠错报告",
				content: 'url:'+url,
				data : {"window" : window}
			}).max();
		}else{
			$.ligerDialog.alert("亲，所选报告系统未能识别，请核实！");
			return;
		}
	}
	
	// 查看新报告
	function showNewReport(){	
		var id = Qm.getValueByName("new_info_id").toString();
		var report_id = Qm.getValueByName("new_report_id").toString();	// 报告类型
		if(id !="" && id !=null && report_id !="" && report_id !=null ){
			var url = "report/query/showReport.do?id="+id+"&report_id="+report_id;
			top.$.dialog({
				width : w, 
				height : h, 
				lock : true, 
				title:"新报告",
				content: 'url:'+url,
				data : {"window" : window}
			}).max();
		}else{
			$.ligerDialog.alert("亲，所选报告系统未能识别，请核实！");
			return;
		}
	}
	
	function refreshGrid() {
		Qm.refreshGrid();
	}
</script>
	</head>
	<body>
		<%
			String id = request.getParameter("id");
		%>
		<qm:qm pageid="REPORT_ERROR2" script="false" singleSelect="false">
			<qm:param name="fk_report_error_id" compare="=" value="<%=id %>"/>	
		</qm:qm>
	</body>
</html>
