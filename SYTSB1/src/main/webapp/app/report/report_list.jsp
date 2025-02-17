<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>报告配置</title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript">
	var qmUserConfig = {
		sp_defaults:{columnWidth:0.33,labelAlign:'right',labelSeparator:'',labelWidth:100},	// 默认值，可自定义
		sp_fields:[
			{name:"report_name",compare:"like",value:""},
			{name:"report_file",compare:"like",value:""},
			{name:"enter_type",compare:"=",value:""},
			{name:"is_upload",compare:"=",value:""},
			{name:"flag",compare:"=",value:""}
		],
	  	tbar:[
			{ text:'查看', id:'detail',icon:'detail', click: detail},
			"-",
			{ text:'新增', id:'add',icon:'add', click: add},
			"-",
			{ text:'修改', id:'modify',icon:'modify', click: modify},"-",
			{ text:'预览', id:'preview',icon:'detail', click: preview}
		],
	    listeners: {
	    	selectionChange : function(rowData,rowIndex){
	       		var count=Qm.getSelectedCount();//行选择个数
	        	Qm.setTbar({modify:count==1,detail:count==1,preview:count==1,del:count>0});
	      	}
		}
	};
	
	function add(){
		top.$.dialog({
			width : 800, 
			height : 500, 
			lock : true, 
			title:"添加报告信息",
			content: 'url:app/report/report_detail.jsp?status=add',
			data : {"window" : window}
		});
		
	}
	
	function modify(){
		top.$.dialog({
			width : 1000, 
			height : 600, 
			lock : true, 
			title:"修改报告信息",
			content: 'url:app/report/report_detail.jsp?status=modify&id='+Qm.getValueByName("id"),
			data : {"window" : window}
		});
	}
	
	function detail(){		
		top.$.dialog({
			width : 1000, 
			height : 600, 
			lock : true, 
			title:"报告信息",
			content: 'url:app/report/report_detail.jsp?status=detail&id='+Qm.getValueByName("id"),
			data : {"window" : window}
		});
	}
	function preview() {
		top.$.dialog({
			width : 800,
			height : 500,
			lock : true,
			title : "预览模板",
			content : 'url:app/report/report_preview.jsp?draw_template='+Qm.getValueByName("report_file"),
			data : {
				"window" : window
			}
		}).max();

	}
	
	function loadGridData(nodeId,unitId,url){
		Qm.searchData();
	}
	
	function submitAction(o) {
		Qm.refreshGrid();
	}
</script>
</head>
<body>
	<qm:qm pageid="report_info" script="false" singleSelect="false">
	</qm:qm>
</body>
</html>
