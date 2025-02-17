﻿<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.status}">
    <title>区划地址 </title>
    <%@ include file="/k/kui-base-form.jsp" %>
    <script type="text/javascript">
        $(function () {
            $("#formObj").initForm({
                success: function (responseText) {//处理成功
                    if (responseText.success) {
                        api.data.window.Qm.refreshGrid();
                        api.data.window.parent.reloadNode();
                        api.close();
                    } else {
                        $.ligerDialog.error('保存失败：' + responseText.data)
                    }
                }
            });
        });
    </script>
</head>
<body>
<form name="formObj" id="formObj" method="post"
      action="rbac/area/saveMore.do">
    <input type="hidden" name="pid" value="${param.pid}">
    <table cellpadding="3" cellspacing="0" class="l-detail-table">
        <tr>
            <td class="l-t-td-left" width="50">数 量：</td>
            <td class="l-t-td-right"><input id="num" name="num" type="text"
                                            ltype='text' validate="{required:true,maxlength:3}" value="10"/></td>
        </tr>
        <tr>
            <td class="l-t-td-left">后 缀：</td>
            <td class="l-t-td-right"><input id="suf" name="suf" type="text"
                                            ltype='text' validate="{required:true,maxlength:20}"/></td>
        </tr>
    </table>
</form>
</body>
</html>
