﻿<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.status}">
<%@include file="/k/kui-base-form.jsp"%>
<script src='pub/calendar/lib/moment.min.js'></script>
<script src='pub/calendar/js/holiday-edit.js'></script>
<script type="text/javascript">
	$(function() {
		if(api.data){
			var startMoment = moment(api.data.event.start);
			var endMoment = moment(api.data.event.end).subtract(1,"d");
			$("#start_date").val(startMoment.format("YYYY-MM-DD"));
			$("#end_date").val(endMoment.format("YYYY-MM-DD"));
			
			if(endMoment.diff(startMoment,"days") == 0){
				$("#end_date_label").hide();
				$("#end_date_cell").hide();
			}
		}
		
		$("#formObj").initForm({
			toolbar: [{
				text: "保存",
				icon: "save",
				click: doHolidaySave
			},{
				text: "取消",
				icon: "close",
				click: function(){api.close();}
			}],
			afterParse: function(response){
				if("${param.status}"=="add" && ${param.legal=='1'} && api.data.event.title){
					$("#title-txt").ligerGetComboBoxManager().selectValue(api.data.event.title);
				}
			}
		});
	});

	function doHolidaySave(){
		if(!$("#formObj").validate().form()){
			return;
		}
		var formData = $("#formObj").getValues();
		formData.name = formData.title;
		
		$("body").mask("正在保存，请稍后...");
		var start = $("#start_date").val(),end = moment($("#end_date").val()).add(1,"d").format("YYYY-MM-DD");
		saveHoliday(formData,start,end,function(response){
           	if(response.success){
           		top.$.notice("保存成功！");
           		if(api.data.callback) api.data.callback(response.data);
           		api.close();
           	}else{
           		$.ligerDialog.error("保存失败！" + (response.msg||""));
           	}
           	$("body").unmask();
		});
	}
</script>
</head>
<body>
	<form name="formObj" id="formObj" method="post" 
		getAction="pub/calendar/holiday/detail_as_event.do?id=${param.id}">
		<input name="id" type="hidden" />
		<input name="weekend" type="hidden" value="0" />
		<input name="legal" type="hidden" value="${param.legal}" />
		<table class="l-detail-table">
			<tr>
				<td class="l-t-td-left" style="width:100px;"></td>
				<td class="l-t-td-right"></td>
				<td class="l-t-td-left" style="width:20px;text-align:left;"></td>
				<td class="l-t-td-right"></td>
			</tr>
			<tr>
				<td class="l-t-td-left">节假日：</td>
				<td class="l-t-td-right" colspan="3"><c:choose>
					<c:when test="${param.legal=='1'}"><u:combo name="title" code="pub_holiday_legal" attribute="emptyOption:false" validate="required:true" /></c:when>
					<c:when test="${param.legal!='1'}"><input name="title" ltype="text" validate="{required:true,maxlength:256}" /></c:when>
				</c:choose></td>
			</tr>
			<tr>
				<td class="l-t-td-left">日期：</td>
				<td class="l-t-td-right"><input name="start_date" id="start_date" ltype="date" validate="{required:true}" /></td>
				<td class="l-t-td-left" id="end_date_label">到</td>
				<td class="l-t-td-right" id="end_date_cell"><input name="end_date" id="end_date" ltype="date" validate="{required:true}" /></td>
			</tr>
			<tr>
				<td class="l-t-td-left">是否休息日：</td>
				<td class="l-t-td-right" colspan="3"><u:combo name="dayOff" code="sys_sf" attribute="initValue:'1'" ltype="radioGroup" /></td>
			</tr>
			<tr>
				<td class="l-t-td-left">备注：</td>
				<td class="l-t-td-right" colspan="3"><textarea name="remark" style="height:6em"
						class="l-textarea" validate="{maxlength:256}"></textarea></td>
			</tr>
		</table>
	</form>
</body>
</html>
