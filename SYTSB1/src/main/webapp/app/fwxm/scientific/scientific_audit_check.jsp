<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.status}">
<title>科研项目申请书审核</title>
<%@ include file="/k/kui-base-form.jsp"%>
<%
	String apply_type = request.getParameter("apply_type");
%>
<SCRIPT type=text/javascript src="app/common/ueditor/ueditor.config.js"></SCRIPT>  
<SCRIPT type=text/javascript src="app/common/ueditor/ueditor.all.min.js"></SCRIPT>
<script type="text/javascript" src="app/common/js/idCard.js"></script>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript">
	$(function() {
		var typeData=[{id:'YNJD',text:'机电类'},{id:'YNCY',text:'承压类'},{id:'YNZH',text:'综合类'}];
		 $("#codeType").ligerComboBox({data:typeData,isMultiSelect: false,
	          onSelected: function (typeTag)
	          {
	              $.ajax({
	  				url : "tjy2ScientificResearchAction/getProjectNo.do?typeTag=" + typeTag,
	  				type : "post",
	  				async : false,
	  				success : function(data) {
	  					if (data.success) {
	  						$('#code').val(data.projectNo);
	  					} else {
	  						$('#code').val("");
	  						top.$.notice("设置失败！");
	  					}
	  				}
	  			});
	          }
	      });
		$("#tr1").hide();
		$("#tr2").hide();
	    $("#formObj").initForm({
			success: function (response) {//处理成功
	    		if (response.success) {
	            	top.$.dialog.notice({
	             		content: "保存成功！"
	            	});
	            	api.data.window.Qm.refreshGrid();
	            	api.close();
	    		} else {
	           		$.ligerDialog.error('保存失败！<br/>' + response.msg);
	      		}
			}, getSuccess: function (response){
				if (response.attachs != null && response.attachs != undefined)
					showAttachFile(response.attachs);
			}, toolbar: [
	      		{
	      			text: "保存", icon: "save", click: function(){
	      				//表单验证
				    	if ($("#formObj").validate().form()) {			    		
				    			$("#formObj").submit();
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
	function auditJg(flag){
		if(flag!="1"){
			$("#tr1").show();
			$("#tr2").hide();
		}else{
			$("#tr2").show();
			$("#tr1").hide();
		}
	}
	$(document).ready(function(){
		$.get(
			"tjy2ScientificResearchAction/lookUp.do?id=${param.id}",
			function(data){
				var list = data.success;
				$.each(list,function (index,element) {
					$(".l-detail-table").append("<tr>"+
					"<td class=\"l-t-td-left tdstyle\">${param.project_name}</td>"+
					"<td class=\"l-t-td-right tdstyle\">"+element.project_audit+"</td>"+
					"<td class=\"l-t-td-left tdstyle\">"+element.project_grade_total+"</td>"+
					"<td class=\"l-t-td-right tdstyle\">"+element.project_audit_date+"</td>"+
					"</tr>");
				});
			}
		);
	});
</script>
<style>
	.tdstyle{
		font-size: 0.875rem;
		border: 0.0625rem solid #a2c8fb;
		text-align: center;
		border-radius: 1%;
	}
	
</style>
</head>
<body>
	<form name="formObj" id="formObj" method="post" action="tjy2ScientificResearchAction/updateConfirm.do?id=${param.id}">
		<input type="hidden" name="id" id="id"/>
		<fieldset class="l-fieldset">
			<legend class="l-legend">
				<div>科研项目申请书审核</div>
			</legend>
			<table cellpadding="3" cellspacing="0" class="l-detail-table" style="border-collapse: collapse;">
				<tr>
					<td class="l-t-td-left">审核结果：</td>
					<td class="l-t-td-right"><input type="radio" name="result"
				id="result" ltype="radioGroup" 
				validate="{required:true}"
				ligerui="{
					initValue:'1',
					readonly:true,
					onChange:auditJg,
					data: <u:dict code="BASE_SCIENTIFIC_JG"/>
					}" />
					
					<%-- <u:combo name="apply_audit_result" code="BASE_EQ_APPLY_RESULT" validate="required:true"/> --%>
					</td>									
				</tr>
				<tr id="tr1">
					<td class="l-t-td-left">退回步骤：</td>
					<td class="l-t-td-right"><input type="radio" name="back"
				id="back" ltype="radioGroup" 
				validate="{required:true}"
				ligerui="{
					initValue:'2',
					readonly:true,
					data: <u:dict code="BASE_SCIENTIFIC_TH"/>
					}" />
					
					<%-- <u:combo name="apply_audit_result" code="BASE_EQ_APPLY_RESULT" validate="required:true"/> --%>
					</td>									
				</tr>
				<tr id="tr2">
				<td class="l-t-td-left">编号类型：</td>
				<td class="l-t-td-right">
				<input id="codeType" type="text" ltype="select" validate="{required:false}" ligerui="{initValue:'',data:[{id:'YNJD',text:'机电类'},{id:'YNCY',text:'承压类'},{id:'YNZH',text:'综合类'}]}"/>
				</td>
					<td class="l-t-td-left">编号：</td>
				<td class="l-t-td-right">
				<input id="code" name="code" type="text" ltype="text" validate="{required:true}" readonly="readonly"   />
				</td>
			</tr>
				<tr>
					<td class="l-t-td-left">审核意见：</td>
					<td class="l-t-td-right" colspan="3">
						<textarea id="remark" rows="2" cols="25" name="remark" class="l-textarea" validate="{maxlength:256}" ></textarea>
					</td>	
				</tr>
				<tr>
					<td class="l-t-td-left tdstyle">项目名称</td>
					<td class="l-t-td-right tdstyle">评审人</td>
					<td class="l-t-td-left tdstyle">评分</td>
					<td class="l-t-td-right tdstyle">评分日期</td>
				</tr>
			</table>
		</fieldset>
	</form>
</div>
</body>
</html>
