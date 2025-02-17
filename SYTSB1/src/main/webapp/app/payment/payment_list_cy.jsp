<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>承压开票</title>
<%@include file="/k/kui-base-list.jsp"%>
<style type="">
.l-icon-exportExcel {
	background: url('k/kui/images/icons/16/excel-export.png') no-repeat center;
}
</style>
<script type="text/javascript">
	var check_deps = <u:dict sql="select id code, ORG_NAME text from SYS_ORG where ORG_CODE like 'cy%' order by orders "></u:dict>;
	var qmUserConfig = {
		sp_defaults:{columnWidth:0.25,labelAlign:'right',labelSeparator:'',labelWidth:100},	// 默认值，可自定义
		sp_fields:[
			{name:"report_com_name", id:"report_com_name", compare:"like"},
			{name:"company_name", id:"company_name", compare:"like"},
			{name:"check_dep_id", id:"check_dep_id", compare:"=", treeLeafOnly: false},
			{group:[
				{name:"pay_date", id:"pay_date", compare:">=", value:""},
				{label:"到", name:"pay_date", id:"pay_date1", compare:"<=", value:"", labelAlign:"center", labelWidth:20}
			]},
			{group:[
				{name:"invoice_no", id:"invoice_no", compare:">=", value:""},
				{label:"到", name:"invoice_no", id:"invoice_no1", compare:"<=", value:"", labelAlign:"center", labelWidth:20}
			]},
			{name : "invoice_no", id : "invoice_no2", compare : "="},
			{name:'invoice_type',compare:"=",isMultiSelect:true},
			{name : "pay_received", compare : "="}
		],
		tbar : [
		/*
		{
			text : '详情',
			id : 'detail',
			click : detail,
			icon : 'detail'
		}*/
		<sec:authorize access="hasRole('charge')">	
			//, '-', 
			{
				text : '开票',
				id : 'doPayment',
				click : doPayment,
				icon : 'add'
			}, '-', {
				text : '继续开票',
				id : 'batchPayment',
				click : batchPayment,
				icon : 'excel-import'
			}, '-', {
				text : '批量开票',
				id : 'importPay',
				click : importPay,
				icon : 'excel-import'
			}, '-', {
				text : '修改开票',
				id : 'modifyPayment',
				click : modifyPayment,
				icon : 'modify'
			}, '-', {
				text : '取消开票',
				id : 'cancelPayment',
				click : cancelPayment,
				icon : 'del'
			}, '-', {
				text : '打印缴费单',
				id : 'printPayInfo',
				click : printPayInfo,
				icon : 'print'
			}, '-', 
		</sec:authorize>
		{
			text : '金额合计',
			id : 'pay_received',
			click : doTotal1,
			icon : 'help'
		}, '-', {
			text : '交账明细导出',
			id : 'exportPayInfo',
			click : exportPayInfo,
			icon : 'excel-export'
		}//, '-', 
        //{ text:'开票日志', id:'turnHistory',icon:'follow', click: turnHistory}
		, '-', {
			text : '清空', 
			id : 'empty', 
			icon : 'modify', 
			click : empty
		} ],
		listeners : {
			rowClick : function(rowData, rowIndex) {
				setConditionValues(rowData);
			},
			rowDblClick : function(rowData, rowIndex) {
				//detail();
			},
			selectionChange : function(rowData, rowIndex) {
				selectionChange()
			},
			rowAttrRender : function(rowData, rowid) {
				/*
	            var fontColor="black";
	            // 2：已开票
	            if (rowData.status == "2"){
	            	fontColor="black";
	            }
	            // 99：取消开票
	            if (rowData.status == "99"){
	            	fontColor="red";
	            }
	            return "style='color:"+fontColor+"'";
	            */
			},
	        pageSizeOptions:[10,20,30,50,100,200]
		}
	};

	// 行选择改变事件
	function selectionChange() {
		var count = Qm.getSelectedCount();//行选择个数
		if (count == 0) {
			Qm.setTbar({
				doPayment : true,
				batchPayment:false,
				detail : false,
				pay_received : false,
				printPayInfo : false,
				modifyPayment : false,
				cancelPayment : false,
				turnHistory : false,
				exportPayInfo : true
			});
		} else if (count == 1) {
			Qm.setTbar({
				doPayment : true,
				batchPayment:true,
				detail : true,
				pay_received : true,
				printPayInfo : true,
				modifyPayment : true,
				cancelPayment : true,
				turnHistory : true,
				exportPayInfo : true

			});
		} else {
			Qm.setTbar({
				doPayment : true,
				batchPayment:false,
				detail : false,
				pay_received : true,
				printPayInfo : false,
				modifyPayment : false,
				cancelPayment : true,
				turnHistory : false,
				exportPayInfo : true
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
		$("input[name='check_dep_id-txt']").ligerComboBox().selectValue(render(rowData["check_dep_id"],check_deps));	// 检验部门
	}
		
	function empty(){
		$("#qm-search-p input").each(function(){
			$(this).val("");
		})
	}
	
	// 查看详情
	function detail() {
		var id = Qm.getValueByName("id").toString();
		var url = 'url:app/payment/payment_detail_cy.jsp?status=detail&id='+ id;
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
	
	
	// 开票
	function doPayment() {
		top.$.dialog({
			width : 900,
			height : 600,
			lock : true,
			title : "开票",
			data : {
				"window" : window
			},
			content : 'url:app/payment/payment_detail_cy.jsp?status=add'
		});
	}
	// 开票
	function batchPayment() {
		top.$.dialog({
			width : 900,
			height : 600,
			lock : true,
			title : "同批次开票",
			data : {
				"window" : window
			},
			content : 'url:app/payment/payment_detail_cy.jsp?status=modify&isBatch=1&id='+Qm.getValueByName("id").toString()
		});
	}
	
	// 批量开票
	function importPay(){	
		top.$.dialog({
			width : 1000, 
			height : 600, 
			lock : true, 
			title : "批量开票", 
			data : {"window" : window},
			content : 'url:app/payment/payment_import_detail.jsp?status=add'
		});
	}
	
	// 修改开票
	function modifyPayment() {
		top.$.dialog({
			width : 900,
			height : 600,
			lock : true,
			title : "修改开票",
			data : {
				"window" : window
			},
			content : 'url:app/payment/payment_detail_cy2.jsp?status=modify&id='+Qm.getValueByName("id").toString()
		});
	}
	
	// 取消开票
	function cancelPayment() {
		$.ligerDialog.confirm("您确定要取消开票？", function(yes) {
			if (yes) {
				$.ajax({
					url : "payment/payInfo/cancelPayCY.do?ids=" + Qm.getValuesByName("id").toString(),
					type : "post",
					async : false,
					success : function(resp) {
						if (resp.success) {
							top.$.notice("取消成功！");
							Qm.refreshGrid();
						} else {
							top.$.notice("取消失败！" + resp.msg);
						}
					}
				});
			}
		});
	}

	// 打印缴费单
	function printPayInfo(){
		top.$.dialog({
			width : 750, 
			height : 500, 
			lock : true, 
			title:"打印缴费结算单",
			content: 'url:payment/payInfo/doBatchPrintCY.do?id='+Qm.getValueByName("id").toString()+'&type=2&type_1=invoice',
			data : {"window" : window}
		})
	}
	
	// 金额合计
	function doTotal1() {
		var ids = Qm.getValuesByName("pay_received").toString();
		doTotal(ids,"开票金额");
	}
	
	// 导出交账明细
	function exportPayInfo(){
		var org_id = $("input[name='check_dep_id-txt']").ligerGetComboBoxManager().getValue();
		$("#org_id").val(org_id);
		$("#pay_start_date").val($("#pay_date").val());
		$("#pay_end_date").val($("#pay_date1").val());
		$("body").mask("正在导出数据,请等待...");
    	$("#form1").attr("action","payment/payInfo/exportCY.do");
    	$("#form1").submit();
    	$("body").unmask();
	}
	
	// 开票日志
	function turnHistory(){	
		top.$.dialog({
	   		width : 400, 
	   		height : 500, 
	   		lock : true, 
	   		title: "开票日志",
	   		content: 'url:payment/payInfo/getFlowStep.do?id='+Qm.getValueByName("id"),
	   		data : {"window" : window}
	   	});
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
			$.ligerDialog.alert(title+'合计：' + total.toFixed(2) + "元。");
		}
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
		<input type="hidden" name="pay_start_date" id="pay_start_date" value=""/>
		<input type="hidden" name="pay_end_date" id="pay_end_date" value=""/>
	</form>
	<!-- 
	<div class="item-tm" id="titleElement">
	    <div class="l-page-title">
			<div class="l-page-title-note">提示：列表数据项
				<font color="black">“黑色”</font>代表已开票，
				<font color="red">“红色”</font>代表取消开票。
			</div>
		</div>
	</div>
	 -->
	<qm:qm pageid="payment_list_cy" script="false" >
	</qm:qm>
	<script type="text/javascript">
	// 根据 sql或码表名称获得Json格式数据
	<%--var aa=<u:dict code="community_type"></u:dict>;--%>
	Qm.config.columnsInfo.check_dep_id.binddata=<u:dict sql="select id,parent_id pid,id code, ORG_NAME text from SYS_ORG where (ORG_CODE like 'jd%' or ORG_CODE like 'cy%') and ORG_CODE!='cy4_1' order by orders "></u:dict>;
	Qm.config.columnsInfo.payment_status.binddata=<u:dict code="PAY_STATUS_CY"></u:dict>;
	</script>
</body>
</html>