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
                        $.ligerDialog.error('保存失败：' + responseText.msg)
                    }
                }
            });
        });
    </script>
</head>
<body>
<form name="formObj" id="formObj" method="post"
      action="rbac/area/saveArea.do"
      getAction="rbac/area/detail.do?id=${param.id}">
    <input type="hidden" name="sysAreaByPid.id" value="${param.pid}">
    <input name="id" type="hidden"/>
    <input name="levels" type="hidden"/>
    <input name="fullName" type="hidden"/>
    <table cellpadding="3" cellspacing="0" class="l-detail-table">
        <tr>
            <td class="l-t-td-left">编 码：</td>
            <td class="l-t-td-right"><input name="code" type="text"
                                            ltype='text' validate="{required:true,maxlength:30}" value="${param.pcode}"/></td>
        </tr>
        <tr>
            <td class="l-t-td-left">名 称：</td>
            <td class="l-t-td-right"><input name="name" type="text"
                                            ltype='text' validate="{required:true,maxlength:50}"/></td>
        </tr>
        <tr>
            <td class="l-t-td-left">简称：</td>
            <td class="l-t-td-right"><input name="simpleName" type="text"
                                            ltype='text' validate="{required:true,maxlength:50}"/></td>
        </tr>
        <tr>
            <td class="l-t-td-left">经纬度：</td>
            <td class="l-t-td-right"><input name="geoCode" type="text"
                                            ltype='text' validate="{maxlength:50}"/></td>
        </tr>
        <tr>
            <td class="l-t-td-left">排 序：</td>
            <td class="l-t-td-right"><input name="orders" type="text"
                                            ltype='number' validate="{maxlength:10}"/></td>
        </tr>
        <tr>
            <td class="l-t-td-left">备 注：</td>
            <td class="l-t-td-right"><input name="remarks" type="text"
                                            ltype='text'validate="{maxlength:50}"/></td>
        </tr>
    </table>
</form>
</body>
</html>
