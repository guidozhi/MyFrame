<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="com.khnt.utils.StringUtil"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head pageStatus="${param.status}">
	<%@ include file="/k/kui-base-form.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/app/relation/relation_info.js"></script>

	<script type="text/javascript">
	var pageStatus="${param.status}";
	$(function () {
		createInfoGrid2();
	    $("#formObj").initForm({    //参数设置示例
	       toolbar:[
	    	 	{ text:'保存', id:'save',icon:'save', click:saveInfo},
	    		{ text:'关闭', id:'close',icon:'cancel', click:close}
	        ],
	        getSuccess:function(resp){
	        	if(resp.success){
	        		relationInfoGrid.loadData({
						Rows : resp.reportRecordParse
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
	});	
		
	function saveInfo(){
		//验证表单数据
		if ($("#formObj").validate().form()) {
			
			// 验证grid
			if(!validateGrid(relationInfoGrid)){
				return false;
			}
			     
			if(confirm("亲，确定保存吗？")){
				$("#save").attr("disabled","disabled");
				url="r3Action/saveBasic2.do?status=" + pageStatus;
				var formData = $("#formObj").getValues();
				var data = {};
				data = formData;
				data["reportRecordParse"] = relationInfoGrid.getData();

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
				      		$("#save").removeAttr("disabled");
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
	
	function setValues(valuex,name){
		if(valuex==""){
			return;
		}
		var selected = relationInfoGrid.rows;
		if (!selected) { alert('请选择行'); return; }
		var formule;
		var formuleType;
		var defaultValue;

		for(var i in selected){
			if(name=='formule'){
		    	if(valuex==''|| valuex==null || valuex ==undefined){
		    		formule = selected[i].formule;
		      	}else{
		        	formule = valuex;
		      	}
		  	}
			if(name=='formuleType'){
		    	if(valuex==''|| valuex==null || valuex ==undefined){
		    		formuleType = selected[i].formuleType;
		      	}else{
		        	var text= $("input[name='formuleTypes']").ligerGetComboBoxManager().getValue();
		        	formuleType = text;
		      	}
		  	}
	       	
	       	if(name=='defaultValue'){
		    	if(valuex==''|| valuex==null || valuex == undefined){
		    		defaultValue = selected[i].defaultValue;
	         	}else{
	         		defaultValue = valuex;
	            }
		  	}	    
		  	
	       	relationInfoGrid.updateRow(selected[i],{
				formule: formule,
				formuleType: formuleType,
				defaultValue: defaultValue
		    });
		}
	}
	
	function close(){
		api.close();
	}	
</script>
</head>
<body>
	<form name="formObj" id="formObj" method="post"
		action="r3Action/saveBasic2.do"
		getAction="r3Action/getRelationsByIds.do?ids=${param.ids}">
		<input id="id" name="id" type="hidden" />
		<fieldset class="l-fieldset">
			<legend class="l-legend">
				<div>对应关系记录表</div>
			</legend>
			<div id="infos"></div>
		</fieldset>
		<fieldset class="l-fieldset">
			<legend class="l-legend">
				<div>便捷填写</div>
			</legend>
			<table border="1" cellpadding="3" cellspacing="0" width=""
				class="l-detail-table">
				<tr>
					<td class="l-t-td-left">转换计算公式：</td>
					<td class="l-t-td-right">
						<input type="text" id="formules" name="formules" ltype="text" validate="{required:false}" 
							onchange="setValues(this.value,'formule')" />
					</td>
					<td class="l-t-td-left">转换类型：</td>
					<td class="l-t-td-right"><input type="text" id="formuleTypes"
						name="formuleTypes" ltype="select" validate="{required:false}"
						ligerui="{value:'',readonly:true,data: <u:dict code="RELATION_TYPE"/>,suffixWidth:'140'}"
						onchange="setValues(this.value,'formuleType')" /></td>
				</tr>
				<tr>
					<td class="l-t-td-left">转换默认值：</td>
					<td class="l-t-td-right">
						<input type="text" id="defaultValues" name="defaultValues" ltype="text" validate="{required:false}" 
							onchange="setValues(this.value,'defaultValue')" />
					</td>
					<td class="l-t-td-left">&nbsp;&nbsp;</td>
					<td class="l-t-td-right">&nbsp;&nbsp;</td>
				</tr>
			</table>
		</fieldset>
	</form>
</body>
</html>