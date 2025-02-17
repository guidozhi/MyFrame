<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.rtbox.template.constant.RtPath"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>报告录入</title>
<%@include file="/k/kui-base-list.jsp"%>
	<%String userId=SecurityUtil.getSecurityUser().getId();%>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript" src="rtbox/app/templates/rtbox.js"></script>
<script type="text/javascript" src="pub/bpm/js/util.js"></script>
<script type="text/javascript">

	var personId = "<sec:authentication property='principal.sysUser.employee.id' htmlEscape='false' />";
	
	var	unitcode="";
		var type = "";
		
		var flow_num ="${param.flow_num}";
		
		var buff = "${param.function}";
		


		var bar =[];
		
		var top_name="";
		var type_name="";
	//报告录入

	if(buff.indexOf("_bglr")!=-1){
		
		
			bar = [
				{text : '报告录入',id : 'input',icon : 'modify',click : input}, "-",
				{text : '查看质量资料', id:'showQuality',icon:'detail', click: showQuality},'-', 
				{text : '生成编号',id : 'addNum',icon : 'modify',click : addNum},"-",
				{text : '流转过程', id:'flow_note',icon:'follow', click: getFlow},"-",
				{text : '报告复制',id : 'copy',icon : 'copy',click : copy},"-",
				{text : '提交审核',id : 'copy',icon : 'search',click : subCheck},"-",
				{text : '提交记录',id : 'subHistory',icon : 'detail',click : subHistory},"-",
				{text : '报告作废',id : 'del',icon : 'delete',click : del},"-",
				{text : '返回', id:'return',icon:'return', click: back}];
	
		
	}			
	
	if(buff.indexOf("_rwfp")!=-1){
		unitcode = 	<sec:authentication property='principal.department.id' htmlEscape='false' />
		bar =[{ text:'任务分配', id:'rwfp',icon:'search', click: rwfp},"-",
		      { text:'流转过程', id:'flow_note',icon:'follow', click: getFlow},"-",
		      { text:'退回', id:'backINfo',icon:'back', click: backINfo}
		      ];
		//condition=[{name : "check_unit_id", compare : "=", value : unitcode}]
		type="02";
		top_name="任务分配";
		
	}
	if(buff.indexOf("_xmfzrsh")!=-1){
		
		bar =[{ text:'项目负责人校验', id:'check',icon:'search', click: check},"-",{ text:'流转过程', id:'flow_note',icon:'follow', click: getFlow},"-",{ text:'返回', id:'back',icon:'back', click: back}];
		type="03";
		top_name="项目负责人校验";
		 
	}
	if(buff.indexOf("_bgsh")!=-1){
		bar =[
			{ text:'批量审核', id:'batchCheck',icon:'search', click: batchCheck},"-",
			{ text:'单份审核', id:'check',icon:'search', click: check},"-",
			{ text:'批量退回', id:'subBack',icon:'back', click: subBack},"-",
			{ text:'下载附件', id:'download',icon:'excel-export', click: downloadAttachments},"-",
			{ text:'流转过程', id:'flow_note',icon:'follow', click: getFlow},"-",
			{ text:'返回', id:'back',icon:'back', click: back}
		];
		type="04";
		top_name="报告审核";
		type_name="审核";
	}
 	
	if(buff.indexOf("_bgqf")!=-1){
		bar =[
			{ text:'批量签发', id:'batchCheck',icon:'search', click: batchCheck},"-",
			{ text:'单份签发', id:'check',icon:'search', click: check},"-",
			{ text:'批量退回', id:'qfBack',icon:'back', click: qfBack},"-",
			{ text:'下载附件', id:'download',icon:'excel-export', click: downloadAttachments},"-",
			{ text:'流转过程', id:'flow_note',icon:'follow', click: getFlow},"-",
			"-",{ text:'返回', id:'back',icon:'back', click: back}
		];
		type="05";
		top_name="报告签发";
		type_name="签发";
	}
	

	if(buff.indexOf("_dybg")!=-1){
		bar =[{ text:'查看报告', id:'showReport',icon:'detail', click: showReport},"-",
          	{ text:'打印报告', id:'print',icon:'print', click: doPrintReport},"-",
          	//{text :'报告作废',id : 'del',icon : 'delete',click : del},
          	{ text:'下载附件', id:'download',icon:'excel-export', click: downloadAttachments},"-",
          	{ text:'流转过程', id:'flow_note',icon:'follow', click: getFlow}];
          //	{ text:'退回修改', id:'returnIput',icon:'return', click: backInput},"-",{ text:'返回', id:'back',icon:'back', click: back}];
		
	}

	if(buff.indexOf("_bglq")!=-1){
		bar =[ { text:'查看报告', id:'showReport',icon:'detail', click: showReport},"-",
		       { text:'报告领取', id:'getinput',icon:'modify', click: getReport},"-",
		       //{ text:'打印标签', id:'printTags',icon:'print', click: doPrintTags},"-",
		       { text:'二维码标签', id:'printTagsE',icon:'print', click: printTagsE},
		       //{text :'报告作废',id : 'del',icon : 'delete',click : del},
		       //{ text:'下载附件', id:'download',icon:'excel-export', click: downloadAttachments},"-",
		       { text:'流转过程', id:'flow_note',icon:'follow', click: getFlow},"-",
	           { text:'领取记录', id:'history',icon:'detail', click: history},"-",{ text:'返回', id:'back',icon:'back', click: back}];
		
	}

	if(buff.indexOf("_bggd")!=-1){
		bar =[{ text:'查看报告', id:'showReport',icon:'detail', click: showReport},"-",
		      { text:'报告归档', id:'reportEnd',icon:'info-save', click: reportEnd},"-",
		      //{ text:'下载附件', id:'download',icon:'excel-export', click: downloadAttachments},"-",
		      { text:'流转过程', id:'flow_note',icon:'follow', click: getFlow},"-",
		      { text:'返回', id:'back',icon:'back', click: back}];
		
	}

	
	
	var qmUserConfig = {
			sp_defaults:{columnWidth:0.3,labelAlign:'right',labelSeparator:'',labelWidth:120},	
		sp_fields : [
		{
			name : "device_registration_code",
			compare : "like",
			value : ""
		},
		{name:"report_sn", id:"report_sn", compare:"like"},
		{
			name : "com_name",
			compare : "like",
			value : ""
		},
		{
			name : "make_units_name",
			compare : "like",
			value : ""
		},
		{
			name : "maintain_unit_name",
			compare : "like",
			value : ""
		},
		{
			name : "enter_op_name",
			compare : "like",
			value : ""
		}

		],
		tbar :bar,
		listeners : {
			selectionChange : function(rowData, rowIndex) {
				var count = Qm.getSelectedCount();//行选择个数
				if(buff.indexOf("dt_receive")!=-1){
					
					selectionChange();
				}else{
					
					Qm.setTbar({subBack:count>0,qfBack:count>0,printInfo:count>0,addNum:count>0,toPrint:count==1,del:count>0,getinput : count>0,flow_note : count==1,returnIput: count==1,print: count>0,printTags:count>0,showReport : count==1,history : count==1,input : count == 1,detail : count == 1,device : count == 1,rwfp : count >= 1,backINfo:count==1,batchCheck:count>1, check : count == 1,copy : count > 0,reportEnd:count>0,upload:count==1,download:count==1,printTagsE:count>0});
				}
				
			},
		rowAttrRender : function(rowData, rowid) {
			var fontColor="black";
     	
              //到期10天之内 显示蓝色
      		 if(rowData.item_op_id ==personId) {
      			fontColor="blue";
      		}
      		
  		return "style='color:"+fontColor+"'";
	}
	//,

			//afterQmReady : function() {

			//	Qm.setCondition([ {
			//		name : "handler_id",
			//		compare : "=",
			//		value : userId
			//	},{
			//		name : "flow_note_num",
			//		compare : "=",
			//		value : acc_id
			//	}
				
				
				//]);

			//	Qm.searchData();
			//}
		}
	};
	
	// 行选择改变事件
	function selectionChange() {
   		var count = Qm.getSelectedCount(); // 行选择个数
   		alert(count);
     	if(count == 1){
            Qm.setTbar({history: true, flow_note: true, showReport: true, getinput: true, back:true,rwfp:true});            	
 		}else if(count > 1){
       		Qm.setTbar({history: true, flow_note: false, showReport: false, getinput: true, back:true,rwfp:true});
    	}else{
    		Qm.setTbar({history: true, flow_note: false, showReport: false, getinput: false, back:true,rwfp:false});
    	}
	}
	// 查看质量相关资料
	function showQuality() {
		var report_type = Qm.getValueByName("report_type").toString();	
		if(report_type==null||report_type==""){
			alert("没有关联报告！");
			return;
		}
		top.$.dialog({
			width : 800, 
			height : 500, 
			lock : true, 
			title : "查看图片", 
			data : {"window" : window},
			cancel : true,
			content : 'url:app/qualitymanage/qualityFiles_list2.jsp?reportType=' + report_type
		});
	}
	function detail() {
	
		top.$.dialog({
			width : 800,
			height : 500,
			lock : true,
			title : "业务详情",
			content : 'url:app/flow/info_detail.jsp?status=detail&id='+ Qm.getValueByName("id"),
			data : {
				"window" : window
			}
		});
	
	}
	
	
	function back(){
	
		window.location="/department/basic/getFlowTanker.do";
		
	}
	
	function subReceive(){
		
		var is_print = Qm.getValuesByName("print_time");
		
	
		
		var temp;
		
		if(is_print==""||is_print==null){
			
			$.ligerDialog.alert("所选数据报告未打印，请重新选择！");
			return;
		}
		
		if(is_print.indexOf(",")!=-1){
			
			temp = is_print.split(",");
			for(var i = 0 ; i < temp.length ; i++){
				if(temp[i]==""||temp[i]==null){
					
					$.ligerDialog.alert("所选数据报告未打印，请重新选择！");
					return;
				}
			}
			
		}else{
		
			if(is_print==""||is_print==null){
				
				$.ligerDialog.alert("所选数据报告未打印，请重新选择！");
				return;
			}
		}
		
		$.getJSON('department/basic/subReceive.do?infoId='+Qm.getValueByName("id")+'&flow_num='+flow_num+'&acc_id='+Qm.getValueByName("activity_id"),function(data){
			
			if(data.success){
				top.$.notice("提交报告领取成功！");
				submitAction();
			}
		
		});
		
		
	}
	
	
	
	
	function subCheck(){
		
		var item_op_ids = Qm.getValuesByName("item_op_id").toString();
		var item_op_idArr = item_op_ids.split(",");
		for(var i=0;i< item_op_idArr.length;i++){
			if(item_op_idArr[i]!=personId){
				$.ligerDialog.alert("项目负责人不是自己的报告不能提交，请重新选择！");
				return;
			}
		}
		var is_input = Qm.getValuesByName("is_report_input").toString();
		var examine_names = Qm.getValuesByName("examine_name").toString();
		if(is_input == "" || is_input == null){
			$.ligerDialog.alert("所选数据报告未录入，请重新选择！");
			return;
		}
		if(is_input.indexOf(",")!= -1){
			var temp = is_input.split(",");
			var names = examine_names.split(",");
			for(var i = 0 ; i < temp.length ; i++){
				if(temp[i] != "2"){
					if(temp[i] == "1" && names[i]==""){
						$.ligerDialog.alert("所选数据报告未录入，请重新选择！");
						return;
					}
				}
			}
		}else{
			if(is_input != "2"){
				if(is_input == "1" && examine_names==""){
					$.ligerDialog.alert("所选数据报告未录入，请重新选择！");
					return;
				}
			}
		}
		
		var check_flows = Qm.getValuesByName("check_flow");
		var check_flow1 = check_flows[0];
		for (var i = 1; i < check_flows.length; i++) {
			if(check_flows[i]!=check_flow1){
				$.ligerDialog.error('提示：请选择相同审批级别的报告！');
				return;
			}
		}
		
		top.$.dialog({
			width : 600, 
			height : 400, 
			lock : true, 
			title:"报告审核提交",
			parent:api,
			content: 'url:app/flow/audit_submit.jsp?infoId='+Qm.getValuesByName("id")+'&flow_num='+flow_num+'&check_flow='+check_flow1+'&acc_id='+Qm.getValuesByName("activity_id"),
			data : {"window" : window}
		});
		
		
	}
	
	// 发微信和短信
	function sendMsgs(ids) {
		$.ajax({
			url : "department/basic/sendMsgs.do?ids=" + ids,
			type : "post",
			async : false,
			success : function(resp) {
				if (resp.success) {
					//top.$.notice("保存成功！");
					Qm.refreshGrid();
				} else {
					$.ligerDialog.alert(resp.msg);
					Qm.refreshGrid();
				}
			}
		});
	}
	
	function subBack(){
		top.$.dialog({
			width : 600, 
			height : 200, 
			lock : true, 
			title:"报告审核时退回",
			parent:api,
			content: 'url:app/flow/batch_back.jsp?type='+type+'&infoId='+Qm.getValuesByName("id")+'&flow_num='+flow_num+'&acc_id='+Qm.getValuesByName("activity_id"),
			data : {"window" : window}
		});
		
		
	}
	
	function qfBack(){
		top.$.dialog({
			width : 600, 
			height : 200, 
			lock : true, 
			title:"报告签发时退回",
			parent:api,
			content: 'url:app/flow/qf_back.jsp?type='+type+'&infoId='+Qm.getValuesByName("id")+'&flow_num='+flow_num+'&acc_id='+Qm.getValuesByName("activity_id"),
			data : {"window" : window}
		});
		
		
	}
	
	function subHistory(){
		
		top.$.dialog({
			width : 800,
			height : 600,
			lock : true,
			title : "提交记录",
			content : 'url:app/flow/subHistory_list.jsp',
			data : {
				"window" : window
			}
		});
		
		
		
	}
	
	function printInfo(){
		
	var is_input = Qm.getValuesByName("is_report_input").toString();
		
	
		
		var temp;
		
		if(is_input==""||is_input==null){
			
			// $.ligerDialog.alert("所选数据报告未录入，请重新选择！");
			// return;
		}
		
		if(is_input.indexOf(",")!=-1){
			
			temp = is_input.split(",");
			for(var i = 0 ; i < temp.length ; i++){
				if(temp[i]!="2"){
					
					// $.ligerDialog.alert("所选数据报告未录入，请重新选择！");
					// return;
				}
			}
			
		}else{
		
			if(is_input!="2"){
				
				// $.ligerDialog.alert("所选数据报告未录入，请重新选择！");
				// return;
			}
		}
	
		var report_types = Qm.getValuesByName("report_type").toString();
		
		var report_type = "";
		
	
		if(report_types.indexOf(",")!=-1){
			
			report_type = report_types.split(",");
			for(var i = 0 ; i < report_type.length ; i++){
				for(var j = 1 ; j < report_type.length ; j++){
					if(report_type[i]!=report_type[j]){
						$.ligerDialog.alert("只能选择同一个模板的报告");
						return;
					}
				}
			}
			report_types = report_type[0];
		} 
		
	
		
		$.getJSON('report/query/queryModle.do?report_type='+report_types,function(data){
			if(data.success){
				var ids = Qm.getValuesByName("id").toString();
				var w=window.screen.availWidth;
				var h=(window.screen.availHeight);			
				top.$.dialog({
					width : w, 
					height :h, 
					lock : true, 
					title:"打印信息表",
					content: 'url:app/query/report_print_index_message.jsp?report_type='+report_types+'&ids='+ids,
					data : {"window" : window}
				}).max();
			}else{
				$.ligerDialog.alert("没有找到信息表模板！");
			}
		});
		
		
		
		
		
		
	}
	
	// 打印检验申请单
	function printApply(){
		var selectedIds = Qm.getValueByName("id").toString();	// 业务信息ID
		var device_id = Qm.getValueByName("fk_tsjc_device_document_id").toString();	// 设备ID
		$.getJSON("department/basic/getDeviceDetail.do?status=detail&id="+selectedIds+"&device_id="+device_id, function(resp){
			if(resp.success){
					if(resp.data["id"] != null){
						top.$.dialog({
							width : $(top).width(),
							height :  $(top).height()-40,
							lock : true,
							title : "打印检验申请单",
							parent: api,
							content : 'url:app/inspection/docEditor.jsp?status=modify',	
							data: {pwindow: window, bean: resp.data}
						}).max();
					}else{
						$.ligerDialog.alert("设备信息异常，请先检查设备信息是否完善！");
					}
			}
		})
	}
	
	function addNum(){
		
		var item_op_id = Qm.getValuesByName("item_op_id").toString();
			
			if(item_op_id!=personId){
				$.ligerDialog.alert("项目负责人不是自己的报告不能生成编号，请重新选择！");
				return;
				
			}
		var report_sn_list = Qm.getValuesByName("report_sn").toString();
		var report_snArr = report_sn_list.split(",");
		for(var i=0;i<report_snArr.length;i++){
			if(report_snArr[i]!=""){
				$.ligerDialog.error("所选报告已生成了报告书编号，请重新选择！");
				return;
			}
		}
		$.ligerDialog.confirm('确定生成报告书编号?', function (yes) { 
			if(yes){
				$.getJSON('department/basic/flow_addNum.do?infoId='+Qm.getValuesByName("id"),function(data){
					if(data){
						top.$.notice("编号生成成功！");
						submitAction();
					}else{
						$.ligerDialog.alert(data.msg,"提示");
					}
				});
			}
		});
	}
	
	function reportEnd(){
		
		
		$.ligerDialog.confirm('确定归档该数据?', function (yes) { 
	
			if(yes){
				
				$.getJSON('department/basic/flow_reportEnd.do',{infoId:Qm.getValuesByName("id"),flow_num:flow_num ,process_id:Qm.getValuesByName("process_id")},function(data){
					
					if(data){
						top.$.notice("归档成功！");
						submitAction();
					}
				
				});
				
				
			}
		
		
	});
	
		
	}
	
	function toPrint(){
		
	var is_input = Qm.getValuesByName("is_report_input").toString();
	
	
	
	var temp;
	
	if(is_input==""||is_input==null){
		
		// $.ligerDialog.alert("所选数据报告未录入，请重新选择！");
		// return;
	}
	
	if(is_input.indexOf(",")!=-1){
		
		temp = is_input.split(",");
		for(var i = 0 ; i < temp.length ; i++){
			if(temp[i]!="2"){
				
				// $.ligerDialog.alert("所选数据报告未录入，请重新选择！");
				// return;
			}
		}
		
	}else{
	
		if(is_input!="2"){
			
			// $.ligerDialog.alert("所选数据报告未录入，请重新选择！");
			// return;
		}
	}
		
	
	
		$.ligerDialog.confirm('确定提交该数据?', function (yes) { 
	
			if(yes){
				
				$.getJSON('department/basic/flow_toPrint.do',{infoId:Qm.getValueByName("id"),flow_num: flow_num,acc_id:Qm.getValueByName("activity_id")},function(data){
					
					if(data){
						top.$.notice("提交成功！");
						submitAction();
					}
				
				});
				
				
			}
		
		
	});
	
		
	}
	
	// 单份审核或签发报告
	function check(){
		var reportType = Qm.getValueByName("reportType");
		if(reportType == "2"){
			// 获取报告检验部门
			top.$.dialog({
				width : 800, 
				height : 400,
				lock : true, 
				title:"报告"+(type=="04"?"审核":"签发"),
				content: "url:app/flow/rtcommon/single_audit_detail.jsp"
						+'?status=modify'
						+'&device_type=' + Qm.getValueByName("device_sort_code").toString().substring(0, 1)
						+'&device_sort_code=' + Qm.getValueByName("device_sort_code").toString()
						+'&report_sn=' + Qm.getValueByName("report_sn")
						+'&fk_report_id=' + Qm.getValueByName("report_type")
						+'&org_id=' + Qm.getValueByName("org_id").toString()
						//id 就是inspectionInfo.id 
						+'&id='+ Qm.getValueByName("id")
						// 获取报告审批流程（0：两级审核 1：一级审核）
						+'&check_flow='+ Qm.getValueByName("check_flow").toString()
						+'&type='+type
						+"&code="+Qm.getValueByName("rtbox_code")
						+"&flow_num="+flow_num
						+"&activityId="+Qm.getValueByName("activity_id"),
				data : {"window" : window}
			}).max();
		}else{
			var device_type = Qm.getValueByName("device_sort_code").toString();
			// 获取报告审批流程（0：两级审核 1：一级审核）
			var check_flow = Qm.getValueByName("check_flow").toString();
			   top.$.dialog({
					width : 800, 
					height : 500, 
					lock : true, 
					title: top_name,
					content: 'url:department/basic/reportInfoLoad.do?type='+type+'&device_type='+device_type+'&check_flow='+check_flow+'&flow_num='+flow_num+'&acc_id='+Qm.getValuesByName("activity_id")+'&report_type='+ Qm.getValuesByName("report_type")+'&ins_info_id='+Qm.getValuesByName("id")+'&device_id='+Qm.getValueByName("fk_tsjc_device_document_id"),
					data : {"window" : window}
				}).max();
		}
		
	}
	
	// 批量审核或批量签发报告
	function batchCheck(){
		//判断是不是相同报表的报告
		var reportTypeArr = Qm.getValuesByName("reportType") ;
		for(var r=1;r<reportTypeArr.length;r++){
			if(reportTypeArr[0]!=reportTypeArr[1]){
				$.ligerDialog.alert("请选择相同类型的报告（新报告|老报告）！");
				return;
			}
		}
		//一次审核分数不能超过50
		var ids = Qm.getValuesByName("id");
		if(ids.length>50){
			$.ligerDialog.alert("请勿一次批量操作大于50份报告！");
			return;
		}
		// 获取报告审批流程（0：两级审核 1：一级审核）
		var check_flows = Qm.getValuesByName("check_flow").toString();
		check_flows = check_flows.split(",");
		var first_check_flow = check_flows[0];
		for(var i=1;i<check_flows.length;i++){
			if(check_flows[i] != first_check_flow){
				$.ligerDialog.error("亲，您所选的报告中存在审核流程不一致的报告哦，请重新选择！");
				return;
			}
		}
		// 获取检验部门
		var org_ids = Qm.getValuesByName("org_id").toString();
		org_ids = org_ids.split(",");
		var first_org_id = org_ids[0];
		for(var i=1;i<org_ids.length;i++){
			if(org_ids[i] != first_org_id){
				$.ligerDialog.error("亲，您所选的报告中存在不同检验部门的报告哦，请重新选择！");
				return;
			}
		}
		//设备类别
		var device_type = Qm.getValueByName("device_sort_code").toString().substring(0, 1);
		var device_sort_code = Qm.getValueByName("device_sort_code").toString();
		//浏览器高宽
		var w=window.screen.availWidth;
		var h=(window.screen.availHeight);			
		//地址
		var url = "";
		var params = {};
		if(reportTypeArr[0] == "2"){//新报告
			url = "url:app/flow/rtcommon/batch_index_detail.jsp?status=modify&type="+type;
			params['ids']=ids;
			//审核or签发
			params['type']=type;
			params['device_type']=device_type;
			params['device_sort_code']=device_sort_code;
			// 获取报告审批流程（0：两级审核 1：一级审核）
			params['check_flow']=first_check_flow;
			params['acc_id'] = Qm.getValuesByName("activity_id");
			params['flow_num']=flow_num;
			params['org_id']=first_org_id;
			params['codes']=Qm.getValuesByName("rtbox_code");
			params['report_sns']=Qm.getValuesByName("report_sn");
			params['fk_report_ids']=Qm.getValuesByName("report_type");
			params['is_batch']=1;
			
		}else{//老报告
			url = 'url:app/flow/report/report_batch_index.jsp?ids='+ids
				+'&type='+type
				+'&device_type='+device_type
				+'&check_flow='+first_check_flow
				+'&acc_id='+Qm.getValuesByName("activity_id").toString()
				+'&flow_num='+flow_num
				+'&org_id='+first_org_id;
		}
		//打开窗口
		top.$.dialog({
			width : w, 
			height :h, 
			lock : true, 
			title:"批量"+type_name,
			content: url,
			data : {"window" : window,"params":params}
		}).max();
	}
	
	function backInput(){
		
		
		$.ligerDialog.confirm('确定回退修改?', function (yes) { 
	
			if(yes){
				
				$.getJSON('department/basic/flow_returnInput.do',{infoId:Qm.getValueByName("id"),flow_num: Qm.getValueByName("flow_num"),acc_id:Qm.getValueByName("activity_id")},function(data){
					
					if(data){
						top.$.notice("退回成功！");
						submitAction();
					}
				
				});
				
				
			}
		
		
	});
		
		
	}
	
	function device() {
		
	
		//查询设备ID和设备种类
		$.getJSON('department/basic/getDeviceType.do',{infoId:Qm.getValueByName("id")},function(data){
			
			if(data.success){
				
				top.$.dialog({
					width : 1000,
					height : 600,
					lock : true,
					title : "设备信息",
					content : 'url:app/device/device_detail.jsp?status=detail&id='+data.device_id+'&device_type='+data.device_type,
					data : {
						"window" : window
					}
				});
				
			}
		});
		
	
	}
	function copy() {
	
	var item_op_id = Qm.getValuesByName("item_op_id").toString();
		
		if(item_op_id!=personId){
			$.ligerDialog.alert("项目负责人不是自己的报告不能复制，请重新选择！");
			return;
			
		}
		
		//获取报告类型id
		var report_types = Qm.getValuesByName("report_type").toString();
		
	
		var report_type = "";
		
	
		if(report_types.indexOf(",")!=-1){
			
			report_type = report_types.split(",");
			for(var i = 0 ; i < report_type.length ; i++){
				for(var j = 1 ; j < report_type.length ; j++){
					if(report_type[i]!=report_type[j]){
						$.ligerDialog.alert("只能选择同一个模板的报告");
						return;
					}
				}
			}
			report_types = report_type[0];
		} 
		
		//判断所选ID是否多个
		var ids = Qm.getValuesByName("id").toString();
		
		var par =null;
	
		var cArr=[];
		
		
		
		
		if(ids.indexOf(",")!=-1){
			
			var	temp = ids.split(",");
			
			for(var i in temp){
				
				cArr[cArr.length]="'"+temp[i]+"'";
				
			}
			
			par = cArr.join(",");
		}else{
			par="'"+ids+"'";
		}
		
	
		
		
		top.$.dialog({
	
	
	
			width :800, 
			height : 600, 
			lock : true, 
			title:"报告复制",
			content: 'url:app/flow/report_copy_list.jsp?ids='+ids+'&par='+par+'&report_type='+report_types,
					
			
			data : {"window" : window}
	
		});
		
	}
	
	function saveReport(infoId,reportId,deviceId) {
		
	
		
		 top.$.dialog({
				width : 800, 
				height : 500, 
				lock : true, 
				title:"报告信息",
				content: 'url:department/basic/reportInfoLoad.do?isSub=yes&type=input'
						+'&report_type='+reportId+'&ins_info_id='+infoId+'&device_id='+deviceId,
				data : {"window" : window}
			}).max();
	
	}
	
	
	function input() {
		var item_op_id = Qm.getValuesByName("item_op_id").toString();
		var reportType = Qm.getValueByName("reportType");
		if(item_op_id!=personId){
			$.ligerDialog.alert("项目负责人不是自己的报告不能提交，请重新选择！");
			return;
			
		}
		if(reportType == "2"){
			var reportPath = "<%=RtPath.tplPageDir%>";
            var id = Qm.getValueByName("id");
            var code = Qm.getValueByName("rtbox_code");
            var check_op = Qm.getValueByName("check_op_id");
            var fk_report_id = Qm.getValueByName("report_type");
            var ysje = Qm.getValueByName("ysje");
            var param=[{name:"id",value:id},{name:"fk_inspection_info_id",value:id},
                {name:"fk_report_id",value:fk_report_id},
                {name:"input",value:"1"},{name:"status",value:"add"},{name:"pageStatus",value:"modify"},{name:"check_op",value:check_op}];
            top.$.dialog({
				width : 400,
				height : 150,
				lock : true,
				title : "报告金额",
				parent: api,
				content : 'url:app/flow/report_money_input.jsp',
				data: {
					window: window, 
					code: code, 
					param: param,
					reportPath: reportPath,
					ysje: ysje,
					info_id: id
				}
			});
		}else{
			top.$.dialog({
				width : 800,
				height : 600,
				lock : true,
				title : "报告项目",
				content : 'url:department/basic/flow_reportInput.do?type=input&flow_num='+flow_num+'&id='
						+ Qm.getValueByName("id")+'&org_id='+Qm.getValueByName("org_id")+ '&report_type='
						+ Qm.getValueByName("report_type") +'&activity_id='+Qm.getValuesByName("activity_id")
						+'&device_id='+Qm.getValueByName("fk_tsjc_device_document_id"),

				data : {
					"window" : window
				}
			});
		}
	}
	function rwfp(){
		
		top.$.dialog({
			width : 900, 
			height : 600, 
			lock : true, 
			title: top_name,
			content: 'url:app/inspection/servicePlan_detail.jsp?status=modify&type='+type+'&org_id='+unitcode+'&id='+Qm.getValuesByName("id")+'&ywid='+Qm.getValuesByName("ywid")+'&acc_id='+Qm.getValuesByName("activity_id")+'&flow_num='+flow_num,
			data : {"window" : window}
		});
	}
	//报告领取
	function getReport() {
		var report_com_name = Qm.getValueByName("com_name");	// 报告书使用单位
			var area_name = Qm.getValueByName("regional_name");	// 安装区域
			//var comArr = Qm.getValuesByName("com_name");	// 报检单位
			var fee_statusArr = Qm.getValuesByName("fee_status");
			for(var i=0;i<fee_statusArr.length;i++){
				if("待收费" == fee_statusArr[i]){
					$.ligerDialog.alert("待收费的报告，不能领取！");
					return;
				}
			}
			top.$.dialog({
				width : 700, 
				height : 300, 
				lock : true, 
				title : "报告领取", 
				data : {"window" : window},
				content : 'url:app/flow/report_draw_detail.jsp?status=add&id=' + Qm.getValuesByName("id")+'&acc_id='+Qm.getValuesByName("activity_id")+'&flow_num='+flow_num+'&report_com_name='+encodeURIComponent(report_com_name)+'&area_name='+encodeURIComponent(area_name)
			});
	}
	
	//报告领取记录
	function history() {
		top.$.dialog({
			width : 900, 
			height : 500, 
			lock : true, 
			title : "报告领取记录", 
			data : {"window" : window},
			content : 'url:app/flow/report_draw_history.jsp'
		});
	}
	// 查看报告
	function showReport(){	
		var id = Qm.getValueByName("id").toString();
		var ispdf =  Qm.getValuesByName("export_pdf").toString();
		var report_id = Qm.getValueByName("report_type").toString();	// 报告类型
		var reportType = Qm.getValueByName("reportType").toString();	// 新老报表识别代码
		
		var to_swf = Qm.getValueByName("to_swf").toString();
		var inspect_date = Qm.getValueByName("advance_time").toString();
		var date = inspect_date.substring(0,4)+inspect_date.substring(5,7)+inspect_date.substring(8,10);
		var report_sn = Qm.getValueByName("report_sn").toString();
		if("2" == reportType){
			var pdf_export_ps =  Qm.getValuesByName("pdf_export_ps").toString();	// 新报表导出pdf标记
			var pdf_export_att =  Qm.getValuesByName("pdf_export_att").toString();	// 新报表导出pdfid
			if(pdf_export_ps == "1" || (ispdf!=null&&ispdf!=""&&ispdf!="undefined")){
				if(ispdf!=null&&ispdf!=""&&ispdf!="undefined"){
					showFile(id,to_swf,date,ispdf,report_sn);
				}else{
					top.$.dialog({
						width : 800, 
						height : 400, 
						lock : true, 
						title:"查看报告",
						content: 'url:app/flow/reportPdfPrint/report_doc.jsp?status=detail&id='+id,
						data : {"window" : window,"pdf_export_att":pdf_export_att}
					}).max();
				}
			}else{
				//科鸿rtbox报表
				var code = Qm.getValueByName("rtbox_code");
				var param=[
					{name:"id",value:id},
					{name:"fk_report_id",value: report_id},
					{name:"status",value:"detail"},
					{name:"pageStatus",value:"detail"}];
				opRt(code,param,"<%=RtPath.tplPageDir%>");
			}
		}else{
			if(ispdf!=null&&ispdf!=""&&ispdf!="undefined"){
				showFile(id,to_swf,date,ispdf,report_sn);
			}else{
				var w=window.screen.availWidth;
				var h=(window.screen.availHeight);
				var url = "report/query/showReport.do?id="+id+"&report_id="+report_id;
				top.$.dialog({
					width : w, 
					height : h, 
					lock : true, 
					title:"查看报告",
					content: 'url:'+url,
					data : {"window" : window}
				}).max();
			}
		}
	
	}
	
	// 打印报告
	function doPrintReport(){	
		var ids = Qm.getValuesByName("id").toString();
		//var report_id = Qm.getValueByName("report_type").toString();	// 报告类型
		//var is_user_defined = Qm.getValueByName("is_user_defined").toString();	// 自定义报告
		//if(is_user_defined==""){
			var w=window.screen.availWidth;
			var h=(window.screen.availHeight);			
			top.$.dialog({
				width : w, 
				height :h, 
				lock : true, 
				title:"打印报告",
				content: 'url:app/query/report_print_index.jsp',
				data : {
					"window" : window,
					"id":ids,
					"acc_id":Qm.getValuesByName("activity_id").toString(),
					"flow_num":flow_num
				}
			}).max();
			//url = "/cfdinfo?CONTROLLER=com.khnt.command.BlankSecuredCmd&NEXT_PAGE=TSJY_REPORT_PRINT_INDEX&id="+ids;
			//window.showModalDialog(url,[],"dialogwidth:"+w+";dialogheight:"+h+";help=no;status=no;center=yes;edge=sunken;resizable=yes");
		//} else {
		//	$.ligerDialog.alert("自定义报告请通过“查看报告”按钮下载报告文档进行打印");
		//}
	}
	
	// 打印标签
	function doPrintTags(){	
		var device_id = Qm.getValuesByName("fk_tsjc_device_document_id").toString();
		var conclusions = Qm.getValuesByName("inspection_conclusion").toString();
		if(conclusions.indexOf("不合格")!=-1){
			$.ligerDialog.error("所选报告中包含有检验不合格的报告，不能打印标签，请重新选择！");
			return;
		}
		var ids = Qm.getValuesByName("id").toString();
		var w=window.screen.availWidth;
		var h=(window.screen.availHeight);			
		top.$.dialog({
			width : w, 
			height :h, 
			lock : true, 
			title:"打印标签",
			content: 'url:app/query/report_print_index_tags.jsp?id='+ids+'&device_id='+device_id,
			data : {"window" : window}
		}).max();
	}
	
	// 打印二维码标签
	function printTagsE(){	
		var device_id = Qm.getValuesByName("fk_tsjc_device_document_id").toString();
		var conclusions = Qm.getValuesByName("inspection_conclusion").toString();
		if(conclusions.indexOf("不合格")!=-1){
			$.ligerDialog.error("所选报告中包含有检验不合格的报告，不能打印标签，请重新选择！");
			return;
		}
		var ids = Qm.getValuesByName("id").toString();
		var w=window.screen.availWidth;
		var h=(window.screen.availHeight);			
		top.$.dialog({ 
			width : w, 
			height :h, 
			lock : true, 
			title:"打印标签",
			content: 'url:app/query/report_print_index_tags.jsp?flag=yes&id='+ids+'&device_id='+device_id,
			data : {"window" : window}
		}).max();
	}
	
	//流转过程
	function  getFlow(){
		
		 top.$.dialog({
	   			width : 400, 
	   			height : 700, 
	   			lock : true, 
	   			title: "流程卡",
	   			content: 'url:department/basic/getFlowStep.do?ins_info_id='+Qm.getValuesByName("id"),
	   			data : {"window" : window}
	   		});
	
		
	}
	//任务分配退回
	function backINfo(){
		var selectedId = Qm.getValuesByName("id");
		if (selectedId.length < 1) {
			$.ligerDialog.alert('请先选择要退回的事项！', "提示");
			return;
		}
		
		 
		 $.ligerDialog.confirm('确定回退到科室报检?', function (yes) { 
	
				if(yes){
					
					$.getJSON('department/basic/backINfo.do?infoId='+Qm.getValuesByName("ywid")+'&flow_num='+flow_num+'&acc_id='+Qm.getValuesByName("activity_id"),function(data){
						
						if(data){
							top.$.notice("退回成功！");
							submitAction();
						}
					
					});
				}
		});
	}	
	//任务分配退回
	function reportBackINfo(){
		var selectedId = Qm.getValuesByName("id");
		if (selectedId.length < 1) {
			$.ligerDialog.alert('请先选择要退回的报告！', "提示");
			return;
		}
		//top.$.dialog({
		//	width : 270,
		//	height : 150,
		//	lock : true,
		//	id:"chooseWindow",
		//	title : "退回选择",
		//	max:false,
	    //    min:false,
		//	content : "url:app/flow/reportBackNext.jsp?infoId="+Qm.getValuesByName("ywid")+"&flow_num="+flow_num+"&acc_id="+Qm.getValuesByName("activity_id"),
		//	data:{"pwindow":window}
	    //});
		
		// $.ligerDialog.confirm('确定回退到科室报检?', function (yes) { 
			//	if(yes){
					//$.getJSON('department/basic/backINfo.do',{infoId:Qm.getValueByName("ywid"),flow_num:flow_num,acc_id:Qm.getValueByName("activity_id")},function(data){
					//	if(data){
						//	top.$.notice("退回成功！");
							//submitAction();
						//}
					
					//});
				//}
		//});
		var ywid = Qm.getValuesByName("ywid").toString();
		var activity_id = Qm.getValuesByName("activity_id").toString();
		$.ligerDialog.confirm('确定要退回到业务报检？', function (yes) { 
			if(yes){
				$.getJSON('department/basic/reportBackINfo.do',{nextHandle:'2',infoId:ywid,flow_num:flow_num,acc_id:activity_id},function(data){
					if(data){
						top.$.notice("退回成功！");
						submitAction();
					}
								
				});
			}
		});
	}


	function del(){
var item_op_id = Qm.getValuesByName("item_op_id").toString();
		
		if(item_op_id!=personId){
			$.ligerDialog.alert("项目负责人不是自己的报告不能提交，请重新选择！");
			return;
			
		}
		
		  $.del("确定作废？",
		    		"department/basic/delReport.do",
		    		{"ids":Qm.getValuesByName("id").toString()});
	}
	
	function uploadAttachments(){
		var is_upload = Qm.getValueByName("is_upload").toString();
		if(is_upload != "1"){
			$.ligerDialog.alert('亲，系统暂不支持该类报告的附件上传功能哦！', "提示");
			return;
		}
		top.$.dialog({
			width : 600, 
			height : 400, 
			lock : true, 
			title : "上传附件", 
			data : {"window" : window},
			content : 'url:app/flow/report_attachment_upload.jsp?status=modify&info_id=' + Qm.getValueByName("id").toString()
		});
	}

	// 下载附件
	function downloadAttachments() {
		var is_upload = Qm.getValueByName("is_upload").toString();
		if(is_upload != "1"){
			$.ligerDialog.alert('亲，系统暂不支持该类报告的附件下载功能哦！', "提示");
			return;
		}
		top.$.dialog({
			width : 600, 
			height : 400, 
			lock : true, 
			title : "下载附件", 
			data : {"window" : window},
			cancel : true,
			content : 'url:app/flow/report_attachment_upload.jsp?status=detail&info_id=' + Qm.getValueByName("id").toString()
		});
	}
	function inputAfterSaveYsje() {
		var reportPath = "<%=RtPath.tplPageDir%>";
        var id = Qm.getValueByName("id");
        var code = Qm.getValueByName("rtbox_code");
        var check_op = Qm.getValueByName("check_op_id");
        var fk_report_id = Qm.getValueByName("report_type");
        var ysje = Qm.getValueByName("ysje");
        var param=[{name:"id",value:id},
        	{name:"fk_inspection_info_id",value:id},
            {name:"fk_report_id",value:fk_report_id},
            {name:"input",value:"1"},
            {name:"status",value:"add"},
            {name:"pageStatus",value:"modify"},
            {name:"check_op",value:check_op}];
        opRt(code,param,reportPath);
	}
	
	// 查看报告文档
    function showFile(id,to_swf,date,ispdf,report_sn){
    	if(to_swf==null||to_swf==""||to_swf=="undefined"){
			$("body").mask(" 第一次查看该报告，正在准备文档，请稍后......");
			$.post("inspectionInfo/basic/pdf2Swf.do",{"pdfPath":date+"/"+report_sn+"/"+report_sn+".pdf","swfPath":date+"/"+report_sn,"swfName":report_sn},function(res){
				if(res.success){
        			$("body").unmask();
        			top.$.dialog({
        				width : 800, 
        				height : 400, 
        				lock : true, 
        				title:"查看报告",
        				content: 'url:app/query/showFile.jsp?status=detail&path='+date+"/"+report_sn+"/"+report_sn+".swf"+'&id='+id,
        				data : {"window" : window,"report_sn":report_sn,"date":ispdf},
        				close:function(res){
        					Qm.refreshGrid();
        					}
					}).max(); 
				}
			}) 
		}else{
			 top.$.dialog({
				width : 800, 
				height : 400, 
				lock : true, 
				title:"查看报告",
				content: 'url:app/query/showFile.jsp?status=detail&path='+date+"/"+report_sn+"/"+report_sn+".swf"+'&id='+id,
				data : {"window" : window,"report_sn":report_sn,"date":ispdf}
			}).max(); 
		}
    }
	
	function submitAction() {
		Qm.refreshGrid();
	}
</script>
</head>
<body>
	<div class="item-tm" id="titleElement">
	    <div class="l-page-title">
			<div class="l-page-title-note">提示：列表数据项
				<font color="blue">“蓝色”</font>代表项目负责人是自己报告。
			
				
			</div>
		</div>
	</div>
<qm:qm pageid="report_enter_tanker" script="false" singleSelect="false">
	<qm:param name="flow_note_id" value="${param.flow_num}" compare="=" />
	<qm:param name="fk_flow_index_id" value="${param.flowId}" compare="=" />
	<!--<qm:param name="handler_id" value="<%=userId%>" compare="=" />-->
</qm:qm>
</body>
</html>