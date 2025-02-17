<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<%@ page import="com.khnt.utils.StringUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head id="main_head">
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<style type="">
.l-icon-exportExcel {
	background: url('k/kui/images/icons/16/excel-export.png') no-repeat center;
}
</style>
<%
	String info_type = request.getParameter("info_type");
%>
<script type="text/javascript">
	var check_deps = <u:dict sql="select id code, ORG_NAME text from SYS_ORG where ORG_CODE like 'jd%' or ORG_CODE like 'cy%' order by orders "></u:dict>;
	
	var qmUserConfig = {
		sp_defaults:{columnWidth:0.25,labelAlign:'right',labelSeparator:'',labelWidth:100},	// 默认值，可自定义
		sp_fields:[
			{name:"report_sn", id:"report_sn", compare:"like"},
			
			{name:"report_com_name", id:"report_com_name", compare:"like"},
			{name:"maintain_unit_name", id:"maintain_unit_name", compare:"like"},
			{name:"construction_units_name", id:"construction_units_name", compare:"like"},
			{name:"check_unit_id", id:"check_unit_id", compare:"="},
			{group:[
				{name:"advance_time", id:"advance_time", compare:">=", value:""},
				{label:"到", name:"advance_time", id:"advance_time1", compare:"<=", value:"", labelAlign:"center", labelWidth:20}
			]}
			<%
				if("3".equals(info_type)){
					%>
					,{name : "fee_status", compare : "="},
					{group:[
						{name:"borrow_date", id:"borrow_date", compare:">=", value:""},
						{label:"到", name:"borrow_date", id:"borrow_date1", compare:"<=", value:"", labelAlign:"center", labelWidth:20}
					]}
					,{name : "invoice_no", id : "invoice_no", compare : "like"}
					<%
				}else{
					if(!"1".equals(info_type)){
						%>
						,{group:[
							{name:"invoice_date", id:"invoice_date", compare:">=", value:""},
							{label:"到", name:"invoice_date", id:"invoice_date1", compare:"<=", value:"", labelAlign:"center", labelWidth:20}
						]}
						,{name : "invoice_no", id : "invoice_no", compare : "like"}
						<%
					}
				}
			%>
				
		],
		tbar : [/*{
			text : '详情',
			id : 'detail',
			click : detail,
			icon : 'detail'
		}*/
		
		<%
			if(StringUtil.isNotEmpty(info_type)){
				if("1".equals(info_type) || "3".equals(info_type)){
					%>
					<sec:authorize access="hasRole('charge')">	
					{
						text : '收费',
						id : 'pay',
						click : doPayment,
						icon : 'add'
					}, '-', 
					</sec:authorize>
					<%
				}
				if("1".equals(info_type)){
					%>
					<sec:authorize access="hasRole('charge')">	
					{
						text : '外借登记',
						id : 'borrow',
						click : doBorrow,
						icon : 'role'
					},{
						text : '修改金额',
						id : 'changeMoney',
						click : changeMoney,
						icon : 'modify'
					}
					</sec:authorize>
					<%
				}
				if("2".equals(info_type)){
					%>
					<sec:authorize access="hasRole('charge')">	
					 {
						text : '取消收费',
						id : 'cancel',
						click : cancelPayment,
						icon : 'del'
					}/*, '-', {
						text : '退款',
						id : 'back',
						click : backPayment,
						icon : 'back'
					}*/
					, '-', {
						text : '修改收费',
						id : 'modifyPayment',
						click : modifyPayment,
						icon : 'modify'
					}, '-', {
						text : '退款记录',
						id : 'backHistory',
						click : backHistory,
						icon : 'detail'
					}, '-', {
						text : '打印缴费单',
						id : 'printPayInfo',
						click : printPayInfo,
						icon : 'print'
					}
					</sec:authorize>
					<%
				}
				if("3".equals(info_type)){	// 外借查询，可修改外借记录（用作发票号录入）
					%>
					<sec:authorize access="hasRole('charge')">	
					{
						text : '修改外借登记',
						id : 'mdyBorrow',
						click : mdyBorrow,
						icon : 'role'
					}, '-', {
						text : '取消外借',
						id : 'delBorrow',
						click : delBorrow,
						icon : 'delete'
					}, '-', {
						text : '打印借条',
						id : 'printBorrow',
						click : printBorrow,
						icon : 'print'
					}, '-', {
						text : '借报告导出',
						id : 'exportPayInfo2',
						click : exportPayInfo2,
						icon : 'excel-export'
					}, '-', {
						text : '借发票导出',
						id : 'exportPayInfo3',
						click : exportPayInfo3,
						icon : 'excel-export'
					}, '-', {
						text : '借报告和发票导出',
						id : 'exportPayInfo4',
						click : exportPayInfo4,
						icon : 'excel-export'
					}</sec:authorize>
					<%
				}
					%> 
					<sec:authorize access="hasRole('charge')">	
					,'-', {
						text : '应收金额合计',
						id : 'pay_receive',
						click : doTotal1,
						icon : 'help'
					}, '-', {
						text : '实收金额合计',
						id : 'pay_received',
						click : doTotal2,
						icon : 'help'
					}
					</sec:authorize>
					<%
				if("2".equals(info_type)){
					%>
					<sec:authorize access="hasRole('charge')">	
					, '-', {
					text : '交账明细导出',
					id : 'exportPayInfo',
					click : exportPayInfo,
					icon : 'excel-export'
					}, '-', {
						text : '收入明细导出',
						id : 'exportPayInfo1',
						click : exportPayInfo1,
						icon : 'excel-export'
					}</sec:authorize>
					<%
				}else if("3".equals(info_type)){
					%>
					<sec:authorize access="hasRole('charge')">	
					, '-', {
						text : '历史记录',
						id : 'borrowHistory',
						click : borrowHistory,
						icon : 'detail'
					}
					</sec:authorize>
					<%
				}
			}else{
				%>
				<sec:authorize access="hasRole('charge')">	
					{
						text : '取消纠错',
						id : 'cancel',
						click : cancelPayment,
						icon : 'del'
					}
					</sec:authorize>
				<%
				
			}
		%>
		
			<sec:authorize access="hasRole('margeCompany')">	
				, "-",
				{
					text : '批量修改',
					id : 'editcom',
					icon : 'modify',
					click : editcom
				}
				, "-",
				{
					text : '合并使用单位',
					id : 'margeCom',
					icon : 'modify',
					click : margeCom
				}
			</sec:authorize>
			<sec:authorize access="hasRole('charge')">	
			,"-",
			{ text:'检验费用计算', id:'dtCal',icon:'modify', click: dtCal}
			,"-",</sec:authorize>
			{ text:'流转过程', id:'turnHistory',icon:'follow', click: turnHistory}
			,'-', 
 			{text : '清空', id : 'empty', icon : 'modify', click : empty}     
		],
		listeners : {
			rowClick : function(rowData, rowIndex) {
				setConditionValues(rowData);
			},
			rowDblClick : function(rowData, rowIndex) {
				detail();
			},
			selectionChange : function(rowData, rowIndex) {
				selectionChange()
			},
			rowAttrRender : function(rowData, rowid) {

			},
	        pageSizeOptions:[10,20,30,50,100,200]
		}
	};
	
	// 行选择改变事件
	function selectionChange() {
		var count = Qm.getSelectedCount();//行选择个数
		if (count == 0) {
			Qm.setTbar({
				pay : false,
				detail : false,
				borrow : false,
				pay_receive : false,
				exportPayInfo : true,
				editcom : false,
				margeCom : false,
				dtCal : true,
				turnHistory : false
				<%
					if("1".equals(info_type)){
						%>
						,changeMoney : false
						<%
					}else if("2".equals(info_type)){
						%>
						,cancel : false,
						modifyPayment : false,
						backHistory : false,
						printPayInfo : false,
						pay_received : false
						<%
					}else if("3".equals(info_type)){
						%>
						,mdyBorrow : false,
						delBorrow : false,
						printBorrow : false,
						borrowHistory : true
						<%
					}
				%>
			});
		} else if (count == 1) {
			Qm.setTbar({
				pay : true,
				detail : true,
				borrow : true,
				pay_receive : true,
				exportPayInfo : true,
				editcom : true,
				margeCom : true,
				dtCal : true,
				turnHistory : true
				<%
					if("1".equals(info_type)){
						%>
						,changeMoney : true
						<%
					}else if("2".equals(info_type)){
						%>
						,cancel : true,
						modifyPayment : true,
						backHistory : true,
						printPayInfo : true,
						pay_received : true
						<%
					}else if("3".equals(info_type)){
						%>
						,mdyBorrow : true,
						delBorrow : true,
						printBorrow : true,
						borrowHistory : true
						<%
					}
				%>
			});
		} else {
			Qm.setTbar({
				pay : true,
				detail : false,
				borrow : true,
				pay_receive : true,
				exportPayInfo : true,
				editcom : true,
				margeCom : true,
				dtCal : true,
				turnHistory : false 
				<%
					if("1".equals(info_type)){
						%>
						,changeMoney : true
						<%
					}else if("2".equals(info_type)){
						%>
						,cancel : true,
						modifyPayment : false,
						backHistory : false,
						printPayInfo : false,
						pay_received : true
						<%
					}else if("3".equals(info_type)){
						%>
						,mdyBorrow : false,
						delBorrow : false,
						printBorrow : false,
						borrowHistory : true
						<%
					}
				%>
			});
		}
	}
	
	function setConditionValues(rowData){
			//$('#device_registration_code1').val(Qm.getValueByName("device_registration_code").toString());	// 设备注册代码
			
			$("#qm-search-p input").each(function(){
				var name = $(this).attr("id");
				if(!$.kh.isNull(name)){
					$(this).val(rowData[name]);
				}
			})
			//$("input[name='device_sort-txt']").ligerComboBox().selectValue(render(rowData["device_sort"],deviceType));	// 设备类别
			//$("input[name='area_id-txt']").ligerComboBox().selectValue(render(rowData["area_id"],areas));	// 设备所在地
			//$("input[name='check_type-txt']").ligerComboBox().selectValue(render(rowData["check_type"],check_types));	// 检验类别
			$("input[name='check_unit_id-txt']").ligerComboBox().selectValue(render(rowData["check_unit_id"],check_deps));	// 检验部门
			
			//$("input[name='report_id-txt']").ligerComboBox().selectValue(render(rowData["report_id"],reports));	// 报告类型
			//$("input[name='fee_status-txt']").ligerComboBox().selectValue(render(rowData["fee_status"],payments));	// 收费状态
		}
		
	function empty(){
		$("#qm-search-p input").each(function(){
			$(this).val("");
		})
		//$("input[name='fee_status-txt']").ligerComboBox().selectValue('');	// 收费状态
	}
	
	// 查看详情
	function detail() {
		var selectedIds = Qm.getValuesByName("inspection_info_id").toString();
		var url = "";
		if("3" == "<%=info_type%>"){	// 外借详情
			url = 'url:app/payment/report_borrow_detail.jsp?status=detail&id='+ selectedIds;
		}else{	// 缴费详情
			url = 'url:app/payment/payment_detail.jsp?status=detail&id='+ selectedIds;
		}
		top.$.dialog({
			width : 1000,
			height : 550,
			lock : true,
			title : "详情",
			data : {
				"window" : window
			},
			content : url	
		});
	}
	
	
	// 收费
	function doPayment() {
		//var company_ids = "";
		var ids = Qm.getValuesByName("inspection_info_id").toString();	// 报检业务ID
		var com_ids = Qm.getValuesByName("fk_unit_id");	// 报检单位ID
		var comArr = Qm.getValuesByName("fk_company_info_use_id");	// 设备使用单位ID
		//var unit_id = Qm.getValuesByName("check_unit_id");	// 单位ID
		
		var first_com = comArr[0];
		var diff_com = false;
		
		for(var i=1;i<comArr.length;i++){
			if(comArr[i] != first_com){
				//$.ligerDialog.error("明明：使用单位不一致，看清楚莫弄错了，皮卡丘，切！");
				//return;
				diff_com = true;
			}
		}
		/*
		company_ids += com_ids;
		var install_com_ids = Qm.getValuesByName("fk_company_info_install_id");	// 安装单位ID
		var make_com_ids = Qm.getValuesByName("fk_company_info_make_id");	// 制造单位ID
		var maintain_com_ids = Qm.getValuesByName("fk_maintain_unit_id");		// 维保单位ID
		for(var i=0;i<install_com_ids.length;i++){
			if(install_com_ids[i].length>0){
				if(company_ids.length>0){
					company_ids += ",";				
				}
				company_ids += install_com_ids[i];
			}
		}
		for(var i=0;i<make_com_ids.length;i++){
			if(make_com_ids[i].length>0){
				if(company_ids.length>0){
					company_ids += ",";				
				}
				company_ids += make_com_ids[i];
			}
			
		}
		for(var i=0;i<maintain_com_ids.length;i++){
			if(maintain_com_ids[i].length>0){
				if(company_ids.length>0){
					company_ids += ",";				
				}
				company_ids += maintain_com_ids[i];
			}
		}*/
		//if(unit_id.indexOf("100036")!=-1){
		//}
		
		
		if(diff_com){
			$.ligerDialog.confirm("亲，使用单位不一致哦，确定收费？", function(yes) {
					if (yes) {
						top.$.dialog({
							width : 1000,
							height : 620,
							lock : true,
							title : "收费",
							data : {
								"window" : window,
								"id":ids,
								"com_ids":com_ids
							},
							content : 'url:app/payment/payment_detail.jsp?status=add'
							//content : 'url:app/payment/payment_detail.jsp?status=add&id='+ids+'&com_ids='+com_ids+'&company_ids='+company_ids
						});
					}
				});
		}else{
			top.$.dialog({
							width : 1000,
							height : 620,
							lock : true,
							title : "收费",
							data : {
								"window" : window,
								"id":ids,
								"com_ids":com_ids
							},
							content : 'url:app/payment/payment_detail.jsp?status=add'
							//content : 'url:app/payment/payment_detail.jsp?status=add&id='+ids+'&com_ids='+com_ids+'&company_ids='+company_ids
						});
		}
	}
	
	// 修改金额
	function changeMoney() {
		var info_ids = Qm.getValuesByName("inspection_info_id").toString();	// 报告业务ID		
		top.$.dialog({
			width : 1000,
			height : 420,
			lock : true,
			title : "修改金额",
			data : {
				"window" : window,
				"info_ids":info_ids
			},
			content : 'url:app/payment/change_money_detail.jsp?status=add'
		});
	}
	
	// 取消收费
	function cancelPayment() {
		var selectedIds = Qm.getValuesByName("inspection_info_id").toString();
		var comArr = Qm.getValuesByName("fk_company_info_use_id");	// 设备使用单位ID
		var first_com = comArr[0];
		for(var i=1;i<comArr.length;i++){
			if(comArr[i] != first_com){
				//$.ligerDialog.error("使用单位不一致，不能同时取消收费，请核实！");
				//return;
			}
		}
		$.ligerDialog.confirm("您确定要取消收费？取消收费后，所选业务信息将退回到“待收费”状态！", function(yes) {
			if (yes) {
				$.ajax({
					url : "payment/payInfo/cancelPayment.do?ids=" + selectedIds+"&type=<%=info_type%>",
					type : "post",
					async : false,
					success : function(resp) {
						if (resp.success) {
							top.$.notice("取消收费成功！");
							Qm.refreshGrid();
						} else {
							top.$.notice("取消收费失败！" + resp.msg);
						}
					}
				});
			}
		});
	}
	
	// 修改收费
	function modifyPayment() {
		top.$.dialog({
			width : 900,
			height : 600,
			lock : true,
			title : "修改收费",
			data : {
				"window" : window
			},
			content : 'url:app/payment/payment_detail2.jsp?status=modify&info_id='+Qm.getValueByName("inspection_info_id").toString()
		});
	}
	
	// 外借登记
	function doBorrow() {
		var selectedIds = Qm.getValuesByName("inspection_info_id").toString();	// 报检业务ID
		//var com_ids = Qm.getValuesByName("fk_unit_id");	// 报检单位ID
		var comArr = Qm.getValuesByName("fk_company_info_use_id");	// 设备使用单位ID
		var first_com = comArr[0];
		var diff_com = false;
		for(var i=1;i<comArr.length;i++){
			if(comArr[i] != first_com){
				//$.ligerDialog.error("设备信息使用单位不一致，不能同时外借，请核实！");
				//return;
				diff_com = true;
			}
		}
		if(diff_com){
			$.ligerDialog.confirm("明明：使用单位不一致，看清楚莫弄错了，皮卡丘，切！", function(yes) {
					if (yes) {
						top.$.dialog({
							width : 1000,
							height : 620,
							lock : true,
							title : "外借登记",
							data : {
								"window" : window
							},
							content : 'url:app/payment/report_borrow_detail.jsp?status=add&id='+ selectedIds+'&com_id='+first_com
						});
					}
				});
		}else{
			top.$.dialog({
				width : 1000,
				height : 620,
				lock : true,
				title : "外借登记",
				data : {
					"window" : window
				},
				content : 'url:app/payment/report_borrow_detail.jsp?status=add&id='+ selectedIds+'&com_id='+first_com
			});
		}
	}
	
	// 修改外借登记
	function mdyBorrow() {
		var selectedIds = Qm.getValuesByName("inspection_info_id").toString();	// 报检业务ID
		//var com_ids = Qm.getValuesByName("fk_unit_id");	// 报检单位ID
		var comArr = Qm.getValuesByName("fk_company_info_use_id");	// 设备使用单位ID
		var first_com = comArr[0];
		for(var i=1;i<comArr.length;i++){
			if(comArr[i] != first_com){
				$.ligerDialog.error("设备信息使用单位不一致，不能同时外借，请核实！");
				return;
			}
		}
		top.$.dialog({
			width : 1000,
			height : 620,
			lock : true,
			title : "修改外借登记",
			data : {
				"window" : window
			},
			content : 'url:app/payment/report_borrow_detail.jsp?status=modify&id='+ selectedIds+'&com_id='+first_com
		});
	}
	
	// 取消外借登记
	function delBorrow() {
		var info_id = Qm.getValueByName("inspection_info_id").toString();	// 报检业务ID
		var com_id = Qm.getValueByName("fk_unit_id");	// 报检单位ID
		$.del("您确定要取消外借吗？取消后如需再次外借或进行收费，请到“待收费”进行处理！", "report/borrow/delBorrow.do", {
			"info_id" : info_id
		});
	}
	
	// 退款
	function backPayment() {
		var selectedIds = Qm.getValuesByName("inspection_info_id").toString();
		var com_id = Qm.getValueByName("fk_unit_id");
		top.$.dialog({
			width : 1000,
			height : 600,
			lock : true,
			title : "退款",
			data : {
				"window" : window
			},
			content : 'url:app/payment/payment_back_detail.jsp?status=add&info_id='+ selectedIds+'&com_id='+com_id
		});
	}
	
	// 退款记录
	function backHistory() {
		var selectedId = Qm.getValueByName("inspection_info_id");
		top.$.dialog({
			width : 1000,
			height : 600,
			lock : true,
			title : "退款记录",
			data : {
				"window" : window
			},
			content : 'url:app/payment/payment_back_list.jsp?info_id='+ selectedId
		});
	}
	
	// 打印缴费单
	function printPayInfo(){
		var selectedIds = Qm.getValuesByName("inspection_info_id").toString();
		var comArr = Qm.getValuesByName("fk_company_info_use_id");	// 设备使用单位ID
		var first_com = comArr[0];
		for(var i=1;i<comArr.length;i++){
			if(comArr[i] != first_com){
				$.ligerDialog.error("设备信息使用单位不一致，不能同时打印缴费单，请核实！");
				return;
			}
		}
		/*
		$.getJSON("payment/payInfo/getDetail.do?status=detail&id="+selectedIds, function(resp){
			if(resp.success){
				if(resp.data["id"] != null){
					top.$.dialog({
						width : $(top).width(),
						height :  $(top).height()-40,
						lock : true,
						title : "打印缴费单",
						parent: api,
						content : 'url:app/payment/docEditor.jsp?status=modify&type=1&isPrint=1',	// type 1：缴费单 2：借条
						data: {pwindow: window, bean: resp.data}
					}).max();
				}else{
					$.ligerDialog.alert("请先收费，再打印缴费单！");
				}
			}
		})*/
		top.$.dialog({
			width : 750, 
			height : 500, 
			lock : true, 
			title:"打印缴费结算单",
			content: 'url:payment/payInfo/doPrint.do?id='+selectedIds+'&type=2',	// 1 收费打印 2 已收费补打
			data : {"window" : window}
		})
	}
	
	// 打印借条
	function printBorrow(){
		var selectedIds = Qm.getValuesByName("inspection_info_id").toString();
		var comArr = Qm.getValuesByName("fk_company_info_use_id");	// 设备使用单位ID
		var first_com = comArr[0];
		for(var i=1;i<comArr.length;i++){
			if(comArr[i] != first_com){
				$.ligerDialog.error("设备信息使用单位不一致，不能同时打印借条，请核实！");
				return;
			}
		}
		$.getJSON("report/borrow/getDetail.do?status=detail&id="+selectedIds, function(resp){
			if(resp.success){
				if(resp.data["id"] != null){
					top.$.dialog({
						width : $(top).width(),
						height :  $(top).height()-40,
						lock : true,
						title : "打印借条",
						parent: api,
						content : 'url:app/payment/docEditor.jsp?status=modify&type=2&isPrint=1',	// type 1：缴费单 2：借条
						data: {pwindow: window, bean: resp.data}
					}).max();
				}else{
					$.ligerDialog.alert("请先进行外借登记，再打印借条！");
				}
			}
		})
	}
	
	function borrowHistory(){
		top.$.dialog({
			width : 1000, 
			height : 600, 
			lock : true, 
			title : "外借历史记录", 
			data : {"window" : window},
			content : 'url:app/payment/report_borrow_list.jsp'
		});
	}
	
	// 应收金额合计
	function doTotal1() {
		var ids = Qm.getValuesByName("advance_fees").toString();
		doTotal(ids,"应收金额");
	}
	
	// 实收金额合计
	function doTotal2() {
		var ids = Qm.getValuesByName("receivable").toString();
		doTotal(ids,"实收金额");
	}
	
	// 导出交账明细
	function exportPayInfo(){
		var org_id = $("input[name='check_unit_id-txt']").ligerGetComboBoxManager().getValue();
		$("#org_id").val(org_id);
		$("#invoice_start_date").val($("#invoice_date").val());
		$("#invoice_end_date").val($("#invoice_date1").val());
		$("body").mask("正在导出数据,请等待...");
    	$("#form1").attr("action","payment/payInfo/export.do");
    	$("#form1").submit();
    	$("body").unmask();
	}
	
	// 导出收入明细
	function exportPayInfo1(){
		var org_id = $("input[name='check_unit_id-txt']").ligerGetComboBoxManager().getValue();
		$("#org_id").val(org_id);
		$("#invoice_start_date").val($("#invoice_date").val());
		$("#invoice_end_date").val($("#invoice_date1").val());
		$("body").mask("正在导出数据,请等待...");
    	$("#form1").attr("action","payment/payInfo/export1.do");
    	$("#form1").submit();
    	$("body").unmask();
	}
	
	// 导出借报告
	function exportPayInfo2(){
		var org_id = $("input[name='check_unit_id-txt']").ligerGetComboBoxManager().getValue();
		$("#org_id").val(org_id);
		$("#borrow_start_date").val($("#borrow_date").val());
		$("#borrow_end_date").val($("#borrow_date1").val());
		$("body").mask("正在导出数据,请等待...");
    	$("#form1").attr("action","payment/payInfo/export2.do");
    	$("#form1").submit();
    	$("body").unmask();
	}
	
	// 导出借发票
	function exportPayInfo3(){
		var org_id = $("input[name='check_unit_id-txt']").ligerGetComboBoxManager().getValue();
		$("#org_id").val(org_id);
		$("#borrow_start_date").val($("#borrow_date").val());
		$("#borrow_end_date").val($("#borrow_date1").val());
		$("body").mask("正在导出数据,请等待...");
    	$("#form1").attr("action","payment/payInfo/export3.do");
    	$("#form1").submit();
    	$("body").unmask();
	}
	
	// 导出借报告和发票
	function exportPayInfo4(){
		var org_id = $("input[name='check_unit_id-txt']").ligerGetComboBoxManager().getValue();
		$("#org_id").val(org_id);
		$("#borrow_start_date").val($("#borrow_date").val());
		$("#borrow_end_date").val($("#borrow_date1").val());
		$("body").mask("正在导出数据,请等待...");
    	$("#form1").attr("action","payment/payInfo/export4.do");
    	$("#form1").submit();
    	$("body").unmask();
	}
	
	function doTotal(ids,title){
		var str = new Array();
		str = ids.split(",");
		var total = 0;
		if (str != null && str.length > 0) {
			for ( var i = 0; i < str.length; i++) {
				if(str[i]==''||str[i]==null){
						str[i]=0;
					}
				total = total + parseFloat(str[i]);
			}
			$.ligerDialog.alert(title+'合计：' + total + "元。");
		}
	}
	
	function parseIs_borrow(thisValue){
		var str = "";
		if("2" == thisValue){
	    	str = "是";
		}else{
			str = "否";
		}
		return str;
	}
	
	function parseIs_report_input(thisValue){
		var str = "";
		if("2" == thisValue){
	    	str = "是";
		}else{
			str = "否";
		}
		return str;
	}
	
	// 批量修改
	function editcom(){
		device_type = Qm.getValueByName("big_class").substring(0, 1);
		if("9" == device_type){
			alert("客运索道暂不支持批量修改！");
			return;
		}else{
			top.$.dialog({
				width : 600,
				height : 300,
				lock : true,
				title : "批量修改",
				content : 'url:app/device/device_change.jsp?status=modify&id='
						+ Qm.getValuesByName("id"),
				data : {
					"window" : window
				}
			});
		}
	}
	
	// 合并使用单位
	function margeCom(){
		device_type = Qm.getValueByName("big_class").substring(0, 1);
		var com_ids = Qm.getValuesByName("fk_company_info_use_id");
		top.$.dialog({
			width : 600,
			height : 200,
			lock : true,
			title : "合并使用单位",
			content : 'url:app/device/device_marge.jsp?status=modify&id='
						+ Qm.getValuesByName("id")+'&com_ids='+com_ids,
			data : {
				"window" : window
			}
		});	
	}

	function dtCal(){
		//var ids = Qm.getValueByName("inspection_info_id").toString();
		top.$.dialog({
			width : $(top).width(),
			height :  $(top).height()-40,
			lock : true,
			title : "检验费用计算",
			data : {
				"window" : window//,
				//"id":ids
			},
			content : 'url:app/payment/pay_calculation.jsp?status=add'
		});
	}
	
	// 流转过程
	function turnHistory(){	
		top.$.dialog({
   			width : 400, 
   			height : 700, 
   			lock : true, 
   			title: "流程卡",
   			content: 'url:department/basic/getFlowStep.do?ins_info_id='+Qm.getValueByName("inspection_info_id"),
   			data : {"window" : window}
   		});
	}
	
	function render(value,data){
		    for (var i in data) {
		    	if (data[i]["text"] == value)
		        {
		    		
		        	return data[i]['id'];
		        }
				if(data[i].children)
				{
					for(var j in data[i].children)
					{
						if(data[i].children[j]["text"] ==value)
							return data[i].children[j]['id'];
						if(data[i].children[j].children)
						{
							for(var k in data[i].children[j].children)
								if(data[i].children[j].children[k]["text"]==value)
								{
									return data[i].children[j].children[k]["id"];
								}
						}
					}
				}
		    }
		    return value;
		}
	
	function refreshGrid() {
		Qm.refreshGrid();
	}
</script>
</head>
<body>
	<form name="form1" id="form1" action="" getAction="" target="_blank">
		<input type="hidden" name="org_id" id="org_id" value=""/>
		<input type="hidden" name="invoice_start_date" id="invoice_start_date" value=""/>
		<input type="hidden" name="invoice_end_date" id="invoice_end_date" value=""/>
		<input type="hidden" name="borrow_start_date" id="borrow_start_date" value=""/>
		<input type="hidden" name="borrow_end_date" id="borrow_end_date" value=""/>
	</form>

		<qm:qm pageid="cy_payment_list" script="false">
		<%
			if(StringUtil.isNotEmpty(info_type)){
				%>
			<qm:param name="fee_status" value="<%=info_type%>" compare="=" />
			<!-- 收费状态(0 默认 1 待收费 2 已收费 3 借报告 4 借发票 5 借报告和发票) -->
			<%
					if("1".equals(info_type)){
						%>
			<qm:param name="fee_status" value="0" compare="=" logic="or" />
			<%
					}else if("3".equals(info_type)){	// 外借查询
						%>
			<qm:param name="fee_status" value="3" compare="=" logic="or" />
			<qm:param name="fee_status" value="4" compare="=" logic="or" />
			<qm:param name="fee_status" value="5" compare="=" logic="or" />
			<%
					}
			}else{
				%>
			<qm:param name="fee_status" value="2" compare="=" />
			<qm:param name="advance_type" value="2" compare="=" />
			<!-- 收费类型 0 正常收费 1 协议收费 2 免收费 -->
			<%
			}
		%>
		</qm:qm>
		<script type="text/javascript">
//    根据 sql或码表名称获得Json格式数据
<%--var aa=<u:dict code="community_type"></u:dict>;--%>
Qm.config.columnsInfo.area_id.binddata=<u:dict sql='select id,parent_id pid,regional_code code, regional_name text from V_AREA_CODE'></u:dict>;
Qm.config.columnsInfo.check_unit_id.binddata=<u:dict sql="select id code, ORG_NAME text from SYS_ORG where ORG_CODE like 'jd%' or ORG_CODE like 'cy%' order by orders "></u:dict>;
Qm.config.columnsInfo.fee_status.binddata = [
	{id: '0', text: '外借类型', children: [
		{id: '3', text: '外借报告'},
		{id: '4', text: '外借发票'},
		{id: '5', text: '外借报告和发票'}
	]}
];
</script>
</body>
</html>