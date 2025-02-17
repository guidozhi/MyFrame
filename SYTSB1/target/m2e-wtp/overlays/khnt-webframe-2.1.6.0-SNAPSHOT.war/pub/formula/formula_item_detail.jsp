<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head pageStatus="${param.pageStatus}">
    <title>可选择项目管理详细页面</title>
    <%@include file="/k/kui-base-form.jsp" %>
    <script type="text/javascript">
        $(function () {
            $("#form1").initForm({
                success:function(response){
                	if (response.success) {
                        top.$.notice('保存成功！',4);
                        api.data.window.submitAction();
                        api.close();
                    } else {
                    	top.$.dialog.tips('保存失败！错误信息：<br />' + response.msg,5,"k/kui/images/icons/dialog/32X32/hits.png",null,0)
        			}
                }
            });
        });
    </script>
</head>
<body>

<form id="form1" action="pub/formula/item/save.do" getAction="pub/formula/item/detail.do?id=${param.id}" style="padding-top:1em;">
    <input type="hidden" id="id" name="id">
    <input type="hidden" id="pub_formula_type" name="types" value="${param.typeCode}">
    <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
        <tr>
            <td class="l-t-td-left"> 项目名称：</td>
            <td class="l-t-td-right">
            	<input name="name" type="text" ltype='text' validate="{required:true,maxlength:32}" />
            </td>
        </tr>
        <tr>
            <td class="l-t-td-left"> 变量名：</td>
            <td class="l-t-td-right">
            	<input name="variable" type="text" ltype='text' validate="{required:true,maxlength:32}" />
            </td>
        </tr>
        <tr>
            <td class="l-t-td-left"> 测试值：</td>
            <td class="l-t-td-right">
                <input name="testValues" type="text" ltype='text' validate="{required:true,maxlength:32}" />
            </td>
        </tr>
 
    </table>
</form>

</body>
</html>
