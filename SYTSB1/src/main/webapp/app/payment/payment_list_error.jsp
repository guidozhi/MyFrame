<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>纠错报告差价</title>
<%@include file="/k/kui-base-list.jsp"%>
<style type="">
.l-icon-exportExcel {
	background: url('k/kui/images/icons/16/excel-export.png') no-repeat center;
}
</style>
<script type="text/javascript">
	var check_deps = <u:dict sql="select id code, ORG_NAME text from SYS_ORG where ORG_CODE like 'cy%' order by orders "></u:dict>;
	var qmUserConfig = {
		sp_defaults:{columnWidth:0.33,labelAlign:'right',labelSeparator:'',labelWidth:100},	// 默认值，可自定义
		sp_fields:[
			{name:"report_sn", id:"report_sn", compare:"like"},
			{name:"old_money", id:"old_money", compare:"=", value:""},
			{name:"new_report_sn", id:"new_report_sn", compare:"like"},
			{name:"new_money", id:"new_money", compare:"=", value:""},
			{name : "money", id:"money", compare : "="}
		],
		tbar : [
		
		],
		listeners : {
			rowClick : function(rowData, rowIndex) {
				//setConditionValues(rowData);
			},
			rowDblClick : function(rowData, rowIndex) {
				//detail();
			},
			selectionChange : function(rowData, rowIndex) {
				//selectionChange()
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
			}
		}
	};

	// 行选择改变事件
	function selectionChange() {
		var count = Qm.getSelectedCount();//行选择个数
	}

	
	function refreshGrid() {
		Qm.refreshGrid();
	}
</script>
</head>
<body>
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
	<qm:qm pageid="report_error_money" script="false" >
	</qm:qm>
</body>
</html>