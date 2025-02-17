<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.status}">
<title>角色管理-编辑、查看</title>
<%@ include file="/k/kui-base-form.jsp"%>
<script type="text/javascript">
	$("#formObj").initForm({
		success : function(res) {
			if (res.success) {
				top.$.notice("保存成功！");
				try {
					api.data.window.submitAction()
				} catch (e) {
				}
			}
		}
	});
</script>
</head>
<body>
	<form name="formObj" id="formObj" method="post" action="rbac/role/save.do"
		getAction="rbac/role/detail.do?id=${param.id}">
		<input name="id" type="hidden" />
		<div class="l-detail-div">
			<table border="0" cellpadding="3" cellspacing="0" width="" height="" align="" class="l-detail-table">
				<tr>
					<td class="l-t-td-left">角色名称：</td>
					<td class="l-t-td-right"><input name="name" type="text" ltype='text' validate="{required:true,maxlength:128}"
							style="width: 100%;" /></td>
				</tr>
				<tr>
					<td class="l-t-td-left">描述：</td>
					<td class="l-t-td-right"><textarea name="remark" cols="60" rows="4" class="l-textarea" validate="{maxlength:1024}"></textarea></td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>
