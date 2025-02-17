<%@page import="java.util.Map"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.utils.StringUtil"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>检验和质量管理软件功能开发（修改）任务书列表</title>
		<%@include file="/k/kui-base-list.jsp"%>
		<script type="text/javascript">
	var bar =[	
		{ text:'详情', id:'detail',icon:'detail', click: detail},"-", 
		{ text:'新增', id:'add',icon:'add', click: add},"-", 	       
		{ text:'修改', id:'modify',icon:'modify', click: modify},"-",
		{ text:'签发', id:'issues',icon:'modify', click: issues},"-", 
		{ text:'退回', id:'backs',icon:'back', click: backs},"-", 
		{ text:'流转过程', id:'turnHistory',icon:'follow', click: turnHistory},"-",   
		{ text:'删除', id:'del',icon:'delete', click: del}
 	]
 	var qmUserConfig = {
		sp_defaults:{columnWidth:0.25,labelAlign:'right',labelSeparator:'',labelWidth:100},	// 默认值，可自定义	
		sp_fields:[
			{name:"sn", id:"sn", compare:"like"},
			{name:"task_name", id:"task_name", compare:"like"}
	    ],
		tbar:bar,
	   	listeners: {
	    	selectionChange : function(rowData,rowIndex){
	       		var count=Qm.getSelectedCount();//行选择个数
	         	Qm.setTbar({modify:count==1,turnHistory:count==1,issues:count>0,backs:count>0,detail:count==1,del:count>0});
	     	},
			rowDblClick : function(rowData, rowIndex) {
				Qm.getQmgrid().selectRange("id", [rowData.id]);
				detail();
			}/*,
	    	afterQmReady:function(){
	      		Qm.searchData();
	   		}*/
		}
	};
	
	// 详情
	function detail(id){
		if($.type(id)!="string"){
			id = Qm.getValueByName("id").toString();
		}		
		top.$.dialog({
			width : 1000, 
			height : 600, 
			lock : true,
			title : "任务书详情",
			content : 'url:app/func_task/func_task_detail.jsp?status=detail&id='+ id,
			data : {
				"window" : window
			}
		});
	}
	
	function add(){	
		top.$.dialog({
			width : 800, 
			height : 600, 
			lock : true, 
			title : "新增任务书", 
			data : {"window" : window},
			content : 'url:app/func_task/func_task_detail.jsp?status=add'
		});
	}

	function modify(){
		top.$.dialog({
			width : 1000, 
			height : 600, 
			lock : true, 
			title:"修改任务书",
			content: 'url:app/func_task/func_task_detail.jsp?status=modify&id='+Qm.getValueByName("id"),
			data : {"window" : window}
		});
	}
	
	function del(){
		var statusArr = Qm.getValuesByName("data_status");	// 数据状态
		for(var i=0;i<statusArr.length;i++){
			if(statusArr[i] != '任务下达'){
				$.ligerDialog.error("亲，您所选的数据中，包含“"+statusArr[i]+"”的数据，不能删除哦，请重新选择！");
				return;
			}
		}
		$.del("亲，确定删除所选任务书吗？删除后系统无法恢复数据哦！",
	    	"functionTaskInfo/del.do",
	    		{"ids":Qm.getValuesByName("id").toString()},function(result){alert('删除成功！')});
	}
	
	function commits(){
		var statusArr = Qm.getValuesByName("data_status");	// 数据状态
		for(var i=0;i<statusArr.length;i++){
			if(statusArr[i] != '未提交'){
				$.ligerDialog.error("亲，您所选的数据中，包含已提交的数据，不能重复操作哦，请重新选择！");
				return;
			}
		}
		if(confirm("亲，确认提交所选任务书吗？提交后无法撤回数据哦！")){
				$.getJSON("functionTaskInfo/commits.do?ids="+Qm.getValuesByName("id").toString(), function(resp){
					if(resp.success){
						top.$.dialog.notice({
				             content: "提交成功！"
						});
						refreshGrid();
					}else{
						$.ligerDialog.error("签收失败，未找到系统相关业务流程，请联系系统管理员！");
					}
			})
		}
	}
	
	function issues(){
		var statusArr = Qm.getValuesByName("data_status");	// 数据状态
		for(var i=0;i<statusArr.length;i++){
			if(statusArr[i] != '任务下达'){
				$.ligerDialog.error("亲，您所选的数据中，包含已签发的数据，不能重复操作哦，请重新选择！");
				return;
			}
		}
		if(confirm("亲，确认签发所选任务书吗？签发后无法撤回数据哦！")){
				$.getJSON("functionTaskInfo/issues.do?ids="+Qm.getValuesByName("id").toString(), function(resp){
					if(resp.success){
						top.$.dialog.notice({
				             content: "签收成功！"
						});
						refreshGrid();
					}else{
						$.ligerDialog.error("签收失败，未找到系统相关业务流程，请联系系统管理员！");
					}
			})
		}
	}
	
	// 退回
	function backs(){
		top.$.dialog({
			width : 600, 
			height : 200, 
			lock : true, 
			title:"任务签发时退回",
			parent:api,
			content: 'url:app/func_task/func_task_back.jsp?ids='+Qm.getValuesByName("id"),
			data : {"window" : window}
		});
	}
	
	// 流转过程
	function turnHistory(){	
		top.$.dialog({
	   		width : 400, 
	   		height : 700, 
	   		lock : true, 
	   		title: "流程卡",
	   		content: 'url:functionTaskInfo/getFlowStep.do?id='+Qm.getValueByName("id").toString(),
	   		data : {"window" : window}
	   	});
	}
	
	function refreshGrid() {
		Qm.refreshGrid();
	}
</script>
	</head>
	<body>
		<qm:qm pageid="func_task_list" singleSelect="false"><!-- script="false"  -->
			<qm:param name="status" value="0" compare="="/>
		</qm:qm>
		<script type="text/javascript">
			Qm.config.columnsInfo.advance_org_id.binddata=<u:dict sql="select t.id code, t.ORG_NAME text from SYS_ORG t where t.parent_id='100000' and t.property='dep' and t.status='used' order by orders "></u:dict>;
		</script>
	</body>
</html>
