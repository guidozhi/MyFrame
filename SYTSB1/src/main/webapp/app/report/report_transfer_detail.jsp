<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head pageStatus="${param.status}">
	</head>
	<%@ include file="/k/kui-base-form.jsp"%>
	<script type="text/javascript"
		src="app/report/report_transfer_info.js"></script>
	<%
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String org_id = user.getDepartment().getId();
		String org_name = user.getDepartment().getOrgName();
	%>
	<script type="text/javascript">
	var pageStatus="${param.status}";
	$(function () {
		createReportTransferRecordGrid();
	    $("#formObj").initForm({    //参数设置示例
	       toolbar:[
	       		{ text:'选择复制', id:'copy',icon:'copy', click:copyInfo},
	            //{ text:'保存', id:'save',icon:'save', click:saveInfo},
	            { text:'提交', id:'commit',icon:'submit', click:commitInfo},
	            { text:'关闭', id:'close',icon:'cancel', click:close}
	        ],
	        getSuccess:function(resp){
	        	if(resp.success){
					reportTransferGrid.loadData({
						Rows : resp.reportTransferRecord
					});
					$("#formObj").setValues(resp.data);
				}
	        },
	        success: function (response) {//处理成功
	    		if (response.success) {
		      		top.$.dialog.notice({
			             content: "保存成功！"
					});
	            	api.data.window.refreshGrid();
	            	api.close();
	    		} else {
	           		$.ligerDialog.error('保存失败！<br/>' + response.msg);
	      		}
			}
		});
		$("#org_id").val("<%=org_id%>");
		$("#org_name").val("<%=org_name%>");
	});	
		
	function saveInfo(){
		//验证表单数据
		if ($("#formObj").validate().form()) {
			
			// 验证grid
			if(!validateGrid(reportTransferGrid)){
				return false;
			}
			     
			if(confirm("亲，确定保存吗？")){
				$("#save").attr("disabled","disabled");
				url="report/transfer/saveBasic.do?status="+pageStatus;
				var formData = $("#formObj").getValues();
				var data = {};
				data = formData;
				data["reportTransferRecord"] = reportTransferGrid.getData();
				$("body").mask("正在保存数据，请稍后！");
				$.ajax({
					url: url,
					type: "POST",
				 	datatype: "json",
				 	contentType: "application/json; charset=utf-8",
				 	data: $.ligerui.toJSON(data),
				  	success: function (resp) {
				   		$("body").unmask();
				      	if (resp["success"]) {
				       		if(api.data.window.Qm){
				                api.data.window.Qm.refreshGrid();
				   			}
				         	top.$.dialog.notice({content:'保存成功！'});
				     		api.close();
				     	}else{
				      		$.ligerDialog.error('提示：' + resp.msg);
				    	}
				  	},
					error: function (resp) {
				   		$("body").unmask();
						$.ligerDialog.error('提示：' + resp.msg);
						$("#save").removeAttr("disabled");
					}
				});
			}        
		}
	}
	
	function commitInfo(){
		//验证表单数据
		if ($("#formObj").validate().form()) {
			
			// 验证grid
			if(!validateGrid(reportTransferGrid)){
				return false;
			}
			     
			if(confirm("亲，确定提交吗？提交后不能修改哦！")){
				$("#commit").attr("disabled","disabled");
				url="report/transfer/commitBasic.do?status="+pageStatus;
				var formData = $("#formObj").getValues();
				var data = {};
				data = formData;
				data["reportTransferRecord"] = reportTransferGrid.getData();
				$("body").mask("正在提交数据，请稍后！");
				$.ajax({
					url: url,
					type: "POST",
				 	datatype: "json",
				 	contentType: "application/json; charset=utf-8",
				 	data: $.ligerui.toJSON(data),
				  	success: function (resp) {
				   		$("body").unmask();
				      	if (resp["success"]) {
				       		if(api.data.window.Qm){
				                api.data.window.Qm.refreshGrid();
				   			}
				         	top.$.dialog.notice({content:'提交成功！'});
				     		api.close();
				     	}else{
				      		$.ligerDialog.error('提示：' + resp.msg);
				    	}
				  	},
					error: function (resp) {
				   		$("body").unmask();
						$.ligerDialog.error('提示：' + resp.msg);
						$("#commit").removeAttr("disabled");
					}
				});
			}        
		}
	}
	    
	function copyInfo(){
		top.$.dialog({
			parent: api,
			width : 800, 
			height : 550, 
			lock : true, 
			title:"选择复制",
			content: 'url:app/report/copy_report_print_list2.jsp',
			data : {"window" : window}
		});
	}
	
	function callBack(ids){
		$.post("report/transfer/queryReportInfos.do?ids="+ids, function(resp) {
			if (resp.success) {
				reportTransferGrid.addRows(resp.list);
				/*reportTransferGrid.loadData({
					Rows : resp.list
				});*/
			}else{
				$.ligerDialog.error(resp.msg);
			}
		});
	}	
	    
	function close(){
		api.close();
	}	
</script>
	<body>
		<form name="formObj" id="formObj" method="post"
			action="report/transfer/saveBasic.do"
			getAction="report/transfer/getDetail.do?id=${param.id}">
			<input id="id" name="id"  type="hidden"  />
			<input id="sn" name="sn"  type="hidden"  />
			<!-- 
			<fieldset class="l-fieldset">
				<legend class="l-legend">
					<div>
						基本信息
					</div>
				</legend>
				<table border="1" cellpadding="3" cellspacing="0" width=""
					class="l-detail-table">
					<tr>
						<td class="l-t-td-left">部门：</td>
						<td class="l-t-td-right">
							<input id="org_id" name="org_id"  type="hidden" value="" />
							<input id="org_name" name="org_name"  type="text" ltype="text" value="" />
						</td>	
						<td class="l-t-td-left"></td>
						<td class="l-t-td-right"></td>						
					</tr>
				</table>
			</fieldset>
			 -->
			<fieldset class="l-fieldset">
				<legend class="l-legend">
					<div>
						前后台报告交接明细表（注：单位、报告编号、份数必填）
					</div>
				</legend>
				<div id="reportTransferRecords"></div>
			</fieldset>		
		</form>
	</body>
</html>