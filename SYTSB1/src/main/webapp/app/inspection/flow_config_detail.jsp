<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.status}">
<title>流程配置</title>
<%@ include file="/k/kui-base-form.jsp"%>
<script type="text/javascript">
	$(function() {
	    $("#formObj").initForm({
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
			}, getSuccess: function (response){
			
			}, toolbar: [
	      		{
	      			text: "保存", icon: "save", click: function(){
	      				//表单验证
				    	if ($("#formObj").validate().form()) {
				    		//setForm();
				    		if(confirm("确定保存？")){
						    	//表单提交
						    	$("#formObj").submit();
							}
				    	}
	      			}
	      		},
				{
					text: "关闭", icon: "cancel", click: function(){
						api.close();
					}
				}
			], toolbarPosition: "bottom"
		});		
	});

	function closewindow(){
		api.close();
	}
	
	function setForm(){
		var selReportValue = $("#fk_report_id").ligerComboBox().getValue();
		var selFlowValue = $("#fk_flow_id").ligerComboBox().getValue();
		var selReportText = $("#fk_report_id").ligerComboBox().findTextByValue(selReportValue);
		var selFlowText = $("#fk_flow_id").ligerComboBox().findTextByValue(selFlowValue);
		$("#report_name").val(selReportText);
		$("#flow_name").val(selFlowText);
	}
	
	// 选择报告类型
	function selectReport(){	
		top.$.dialog({
			parent: api,
			width : 666, 
			height : 500, 
			lock : true, 
			title:"选择报告类型",
			content: 'url:app/inspection/choose_reports_list.jsp',
			data : {"parentWindow" : window}
		});
	}
	
	function callBackReport(id, report_name){
		$('#fk_report_id').val(id);			// 报告类型ID
		$('#report_name').val(report_name);	// 报告名称
	}		
	
	// 选择检验流程
	function selectFlow(){	
		top.$.dialog({
			parent: api,
			width : 600, 
			height : 500, 
			lock : true, 
			title:"选择检验流程",
			content: 'url:app/inspection/choose_flows_list.jsp',
			data : {"parentWindow" : window}
		});
	}
	
	function callBackFlow(id, flow_name){
		$('#fk_flow_id').val(id);			// 流程ID
		$('#flow_name').val(flow_name);		// 流程名称
	}		
</script>
</head>
<body>
	<form name="formObj" id="formObj" method="post" action="flow/config/save.do?status=${param.status}"
		getAction="flow/config/detail.do?id=${param.id}">
		<input type="hidden" name="id" id="id" value="${param.id}"/>
		<fieldset class="l-fieldset">
			<legend class="l-legend">
				<div>流程配置</div>
			</legend>
			<table cellpadding="3" cellspacing="0" class="l-detail-table">
				<tr>
					<td class="l-t-td-left">设备类别：</td>
					<td class="l-t-td-right"><u:combo name="device_type" code="device_classify" validate="required:true" tree="false" attribute="treeLeafOnly:false"/></td>		
					<td class="l-t-td-left">检验类别：</td>
					<td class="l-t-td-right"><u:combo name="check_type" code="BASE_CHECK_TYPE" validate="required:true"/></td>	
				</tr>
				<tr>
					<td class="l-t-td-left">报告类型：</td>
					<td class="l-t-td-right">
						<input id="fk_report_id" name="fk_report_id"  type="hidden"  />
						<input type="text" id="report_name" name="report_name" ltype='text' validate="{required:true}"
								ligerui="{value:'',iconItems:[{icon:'add',click:function(){selectReport()}}]}" onclick="selectReport()"/>
					</td>
					<td class="l-t-td-left">检验流程：</td>
					<td class="l-t-td-right">
						<input id="fk_flow_id" name="fk_flow_id"  type="hidden"  />
						<input type="text" id="flow_name" name="flow_name" ltype='text' validate="{required:true}"
								ligerui="{value:'',iconItems:[{icon:'add',click:function(){selectFlow()}}]}" onclick="selectFlow()"/>
					</td>	
				</tr>
				<tr>
					<td class="l-t-td-left">备注：</td>
					<td class="l-t-td-right" colspan="3">
						<textarea name="remark" id="remark" rows="2" cols="25" class="l-textarea" validate="{maxlength:200}"></textarea>
					</td>		
				</tr>
			</table>
		</fieldset>
	</form>
</div>
</body>
</html>
