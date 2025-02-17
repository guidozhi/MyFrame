<%@page import="com.khnt.utils.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.status}">
    <title></title>
    <%@include file="/k/kui-base-form.jsp" %>
    <script type="text/javascript">
        $(function () {
			$("#form1").initForm({    //参数设置示例
				toolbar:[{id:'save',text:'保存',icon:'save',click:function(){
					if($.kh.isNull($("input[name='springId']").val())&&$.kh.isNull($("input[name='beanClass']").val())){
						$.ligerDialog.warn("任务对应类或对应SpringBeanId必须存在一个");
						return ;
					}
					//检查表达式
					if($("#form1").validate().form()){
						var cron = $("input[name='cronExpression']").val();
						$.getJSON("pub/scheduleJob/checkCor.do",{cron:cron},function(res){
							if(res.success){
								$("#form1").submit();
							}else{
								$.ligerDialog.warn("时间表达式有问题，请检查！");
								return;
							}
						});
					}
				}},{id:'close',icon:'cancel',text:'关闭',click:function(){api.close()}}],
				success: function (response) {
					if(response.success){
						top.$.notice("保存成功！",3);
						api.data.window.refreshGrid();
						api.close();
					}
					else{
						$.ligerDialog.error("操作失败！<br/>" + response.msg);
					}
				},getSuccess:function(res){
				}
			});
		});
    </script>
</head>
<body>
<form id="form1" action="pub/scheduleJob/save.do" getAction="pub/scheduleJob/detail.do?id=${param.id}">
    <input type="hidden"  name="id">
    <input type="hidden"  name="jobStatus" value="1">
    <input type="hidden"  name="createTime">
    <input type="hidden"  name="updateTime">
    <input type="hidden"  name="runTimes">
    <input type="hidden"  name="lastRunTime">
    <table cellpadding="3" class="l-detail-table">
        <tr>
            <td class="l-t-td-left">任务名称：</td>
            <td class="l-t-td-right">
            	<input type="text" ltype="text" name="jobName" validate="{required:true,maxlength:128}"/>
            </td>
            <td class="l-t-td-left">任务分组：</td>
            <td class="l-t-td-right">
            	<input type="text" ltype="text" name="jobGroup" validate="{required:true,maxlength:128}"/>
            </td>
        </tr>
        <tr>
            <td class="l-t-td-left">任务对应类：</td>
            <td class="l-t-td-right">
            	<input type="text" ltype="text" name="beanClass"  validate="{maxlength:512}" title="包名+类名"/>
            </td>
            <td class="l-t-td-left">对应SpringBeanId：</td>
            <td class="l-t-td-right">
            	<input type="text" ltype="text" name="springId" validate="{maxlength:512}"/>
            </td>
        </tr>
        <tr>
            <td class="l-t-td-left">调用方法：</td>
            <td class="l-t-td-right">
            	<input type="text" ltype="text" name="methodName" validate="{required:true,maxlength:512}"/>
            </td>
            <td class="l-t-td-left">时间表达式：</td>
            <td class="l-t-td-right">
            	<input type="text" ltype="text" name="cronExpression" validate="{required:true,maxlength:512}"/>
            </td>
        </tr>
        <tr>
            <td class="l-t-td-left">任务类型：</td>
            <td class="l-t-td-right" colspan="3">
            	<input type="text" ltype="select" name="isConcurrent" validate="{required:true}"
            	ligerui="{data:[{id:'0',text:'同步任务'},{id:'1',text:'异步任务'}],initValue:'1'}"
            	/>
            </td>
        </tr>
        <tr>
            <td class="l-t-td-left">任务描述：</td>
            <td class="l-t-td-right" colspan="3">
            	<textarea rows="5" cols="100" name="description" ltype="textarea"></textarea>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
