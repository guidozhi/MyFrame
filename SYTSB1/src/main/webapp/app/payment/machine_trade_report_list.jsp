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
<script type="text/javascript">
	var check_deps = <u:dict sql="select id code, ORG_NAME text from SYS_ORG where ORG_CODE like 'jd%' or ORG_CODE like 'cy%' order by orders "></u:dict>;
	
	var qmUserConfig = {
		sp_defaults:{columnWidth:0.33,labelAlign:'right',labelSeparator:'',labelWidth:100},	// 默认值，可自定义
		sp_fields:[
			{name:"report_sn", id:"report_sn", compare:"like"},			
			{name:"report_com_name", id:"report_com_name", compare:"like"},
			{name:"maintain_unit_name", id:"maintain_unit_name", compare:"like"},
			{name:"construction_units_name", id:"construction_units_name", compare:"like"},
			{name:"check_unit_id", id:"check_unit_id", compare:"="},
			
			{group:[
				{name:"advance_time", id:"advance_time", compare:">=", value:""},
				{label:"到", name:"advance_time", id:"advance_time1", compare:"<=", value:"", labelAlign:"center", labelWidth:20}
			]},
			{name : "fee_status", compare : "="},
			{group:[
				{name:"invoice_date", id:"invoice_date", compare:">=", value:""},
				{label:"到", name:"invoice_date", id:"invoice_date1", compare:"<=", value:"", labelAlign:"center", labelWidth:20}
			]},
			{name : "invoice_no", id : "invoice_no", compare : "like"}
		],
		tbar : [
			<sec:authorize access="hasRole('charge')">	
				{text : '收费', id : 'pay', click : doPayment, icon : 'add' }, '-', 
			</sec:authorize>
			{ text:'流转过程', id:'turnHistory',icon:'follow', click: turnHistory},'-', 
 			{text : '清空', id : 'empty', icon : 'modify', click : empty}     
		],
		listeners : {
			rowClick : function(rowData, rowIndex) {
				setConditionValues(rowData);
			},
			rowDblClick : function(rowData, rowIndex) {

			},
			selectionChange : function(rowData, rowIndex) {
				selectionChange()
			},
			rowAttrRender : function(rowData, rowid) {
				var fontColor="black";
				if (rowData.advance_type == "3"){
					if(rowData.fee_status == "初始" || rowData.fee_status == "待收费"){
						rowData.fee_status = "支付宝待开票";
					}else if(rowData.fee_status == "已收费"){
						rowData.fee_status = "支付宝";
					}
					fontColor="blue";
				}
				if (rowData.advance_type == "4"){
					if(rowData.fee_status == "初始" || rowData.fee_status == "待收费"){
						rowData.fee_status = "微信待开票";
					}else if(rowData.fee_status == "已收费"){
						rowData.fee_status = "微信";
					}
					fontColor="green";
				}
		   		return "style='color:"+fontColor+";'";
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
				turnHistory : false
			});
		} else if (count == 1) {
			Qm.setTbar({
				pay : true,
				turnHistory : true
			});
		} else {
			Qm.setTbar({
				pay : true,
				turnHistory : false 
			});
		}
	}
	
	function setConditionValues(rowData){
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
		
	function empty(){
		$("#qm-search-p input").each(function(){
			$(this).val("");
		})
		//$("input[name='fee_status-txt']").ligerComboBox().selectValue('');	// 收费状态
	}
	
	// 收费
	function doPayment() {
		var ids = Qm.getValuesByName("inspection_info_id").toString();	// 报检业务ID
		var com_ids = Qm.getValuesByName("fk_unit_id");	// 报检单位ID
		var comArr = Qm.getValuesByName("fk_company_info_use_id");	// 设备使用单位ID
		
		var first_com = comArr[0];
		var diff_com = false;
		
		for(var i=1;i<comArr.length;i++){
			if(comArr[i] != first_com){
				diff_com = true;
			}
		}

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
						});
		}
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
	
	function parseIs_borrow(thisValue){
		var str = "";
		if("2" == thisValue){
	    	str = "是";
		}else{
			str = "否";
		}
		return str;
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
	
	<div class="item-tm" id="titleElement">
		<div class="l-page-title">
		<div class="l-page-title-note">提示：列表数据项均表示已在一体机上在线支付，其中 
			<font color="blue">“蓝色”</font>代表支付宝业务；
			<font color="green">“绿色”</font>代表微信业务。
		</div>
		</div>
	</div>
	
	<qm:qm pageid="payment_list" script="false">
		<qm:param name="advance_type" value="3" compare="=" logic="or" />
		<qm:param name="advance_type" value="4" compare="=" logic="or" />
	</qm:qm>
	
	<script type="text/javascript">
		// 根据 sql或码表名称获得Json格式数据
		<%--var aa=<u:dict code="community_type"></u:dict>;--%>
		Qm.config.columnsInfo.area_id.binddata=<u:dict sql='select id,parent_id pid,regional_code code, regional_name text from V_AREA_CODE'></u:dict>;
		Qm.config.columnsInfo.check_unit_id.binddata=<u:dict sql="select id code, ORG_NAME text from SYS_ORG where ORG_CODE like 'jd%' or ORG_CODE like 'cy%' order by orders "></u:dict>;
	</script>
</body>
</html>