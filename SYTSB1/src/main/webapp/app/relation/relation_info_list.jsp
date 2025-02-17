<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.khnt.utils.StringUtil"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head id="main_head">
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript">
	var qmUserConfig = {
		sp_defaults:{columnWidth:0.25,labelAlign:'right',labelSeparator:'',labelWidth:100},	// 默认值，可自定义
		sp_fields : [ 
			{name : "fk_report_id", compare : "=", value : "" },
			{name : "record_item_name", compare:"like", value : ""},
			{name : "report_item_name", compare:"like", value : ""},
			{name : "data_status", compare:"=", value : ""}
		],
		tbar : [/*{
			text : '详情',
			id : 'detail',
			click : detail,
			icon : 'detail'
		}, '-', */{
			text : '新增',
			id : 'add',
			click : add,
			icon : 'add'
		},'-', {
			text : '批量修改',
			id : 'modify',
			click : modify,
			icon : 'modify'
		},'-', {
			text : '启用',
			id : 'enable',
			click : enable,
			icon : 'accept'
		}, '-', {
			text : '停用',
			id : 'disable',
			click : disable,
			icon : 'forbid'
		}, '-', {
			text : '对比',
			id : 'compare',
			click : compare,
			icon : 'compare'
		}, '-', {
			text : '清除错误标记',
			id : 'cancelTags',
			click : cancelTags,
			icon : 'win-cancelVal'
		}],
		listeners : {
			rowClick : function(rowData, rowIndex) {
			},
			rowDblClick : function(rowData, rowIndex) {
				detail();
			},
			selectionChange : function(rowData, rowIndex) {
				selectionChange()
			},
			rowAttrRender : function(rowData, rowid) {
				
				var fontColor="black";
				if (rowData.status == '0'){
		    		fontColor="black";
		    	}
				if (rowData.status == '98'){
	          		fontColor="blue";
	            }
		   		if (rowData.status == '99'){
	          		fontColor="red";
	            }
				return "style='color:"+fontColor+";'";
		 		//return "style='color:"+fontColor+";font-weight: bold;'";
			},
	   		pageSizeOptions:[10,20,30,50,100,200]
		}
	};

	//行选择改变事件
	function selectionChange() {
		var count = Qm.getSelectedCount();//行选择个数
		if (count == 0) {
			Qm.setTbar({
				detail : false,
				enable : false,
				disable : false,
				compare : true,
				cancelTags : false,
				modify : false
			});
		} else if (count == 1) {
			Qm.setTbar({
				detail : true,
				enable : true,
				disable : true,
				compare : true,
				cancelTags : true,
				modify : true
			});
		} else {
			Qm.setTbar({
				detail : false,
				enable : true,
				disable : true,
				compare : true,
				cancelTags : true,
				modify : true
			});
		}
	}

	//查看
	function detail() {
		top.$.dialog({
			width : 1000,
			height : 600,
			lock : true,
			title : "详情",
			data : {
				"window" : window
			},//把当前页面窗口传入下一个窗口，以便调用。
			content : 'url:app/maintenance/maintenance_info_detail.jsp?status=detail&id='+ Qm.getValueByName("id")
		});
	}
	
	//新增
	function add() {
		top.$.dialog({
			width : 600,
			height : 320,
			lock : true,
			title : "新增",
			data : {
				"window" : window
			},
			content : 'url:app/relation/relation_add.jsp'
		});
	}
	
	//批量修改
	function modify() {
		top.$.dialog({
			width : 1000,
			height : 600,
			lock : true,
			title : "批量修改",
			data : {
				"window" : window
			},//把当前页面窗口传入下一个窗口，以便调用。
			content : 'url:app/relation/relation_detail.jsp?status=modify&ids='+ Qm.getValuesByName("id").toString()
		});
	}
	
	// 启用
	function enable() {
		var statusArr = Qm.getValuesByName("data_status").toString();
		if(statusArr.indexOf("启用") != -1){
			$.ligerDialog.alert("所选记录中，包含已启用的数据哦，请重新选择！");
			return;
		}
		$.ligerDialog.confirm("亲，确定启用所选记录吗？", function(yes) {
			if (yes) {
				$.ajax({
					url : "r3Action/enable.do?ids=" + Qm.getValuesByName("id").toString(),
					type : "post",
					async : false,
					success : function(data) {
						if (data.success) {
							top.$.notice("启用成功！");
							Qm.refreshGrid();
						} else {
							top.$.notice("启用失败！" + data.msg);
						}
					}
				});
			}
		});
	}
	
	// 停用
	function disable() {
		var statusArr = Qm.getValuesByName("data_status").toString();
		if(statusArr.indexOf("停用") != -1){
			$.ligerDialog.alert("所选记录中，包含已停用的数据哦，请重新选择！");
			return;
		}
		$.ligerDialog.confirm("亲，确定停用所选记录吗？停用操作请谨慎操作哦！", function(yes) {
			if (yes) {
				$.ajax({
					url : "r3Action/disable.do?ids=" + Qm.getValuesByName("id").toString(),
					type : "post",
					async : false,
					success : function(data) {
						if (data.success) {
							top.$.notice("停用成功！");
							Qm.refreshGrid();
						} else {
							top.$.notice("停用失败！" + data.msg);
						}
					}
				});
			}
		});
	}
	
	// 对比
	function compare(){
		var report_id = $("input[name='p-q-f0-txt']").ligerGetComboBoxManager().getValue();
		if(report_id == ""){
			top.$.notice("亲，请先选择报告模版名称！");			
		}else{
			$("body").mask("正在对比数据，请稍等...");
	    	$.ajax({
				url : "r3Action/compare.do?report_id=" + report_id,
				type : "post",
				async : false,
				success : function(resp) {
					$("body").unmask();
					if (resp.success) {
						//top.$.notice("对比完成！存在潜在错误对应项"+resp.error_count+"项。");
						$.ligerDialog.alert("对比完成！存在潜在错误对应项"+resp.error_count+"项。");
						//Qm.refreshGrid();
						if(resp.error_count>0){
							$("input[name='p-q-f3-txt']").ligerComboBox().selectValue("98")
							$("#listSearchBT1").click();
						}
					} else {
						//top.$.notice("对比失败！" + resp.msg);
						$.ligerDialog.error("对比失败！" + resp.msg);
					}
				}
			});
		}
	}
	
	// 取消错误标记
	function cancelTags() {
		$.ligerDialog.confirm("亲，确定取消所选记录的错误标记吗？", function(yes) {
			if (yes) {
				$.ajax({
					url : "r3Action/cancelTags.do?ids=" + Qm.getValuesByName("id").toString(),
					type : "post",
					async : false,
					success : function(data) {
						if (data.success) {
							top.$.notice("取消成功！");
							Qm.refreshGrid();
						} else {
							top.$.notice("取消失败！" + data.msg);
						}
					}
				});
			}
		});
	}
	
	function refreshGrid() {
		Qm.refreshGrid();
	}
</script>
</head>
<body>
	<div class="item-tm" id="titleElement">
		<div class="l-page-title">
			<div class="l-page-title-note">提示：列表数据项，
				<font color="black">“黑色”</font>代表已启用，
				<font color="blue">“蓝色”</font>代表潜在错误对应项，
				<font color="red">“红色”</font>代表已停用。
			</div>
		</div>
	</div>
	<qm:qm pageid="relation_info_list" script="false">
	</qm:qm>
	<script type="text/javascript">
	Qm.config.columnsInfo.fk_report_id.binddata=<u:dict sql="select id code, report_name text from base_reports t where t.flag='1' and t.REPORT_TYPE='2' "></u:dict>;
	</script>
</body>
</html>
