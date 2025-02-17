﻿<%@page import="com.khnt.utils.DateUtil"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="modify">
    <title></title>
    <%@include file="/k/kui-base-form.jsp" %>
    <script test="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
    <script type="text/javascript">
        $(function () {
			$("#form1").initForm({    //参数设置示例
				toolbar:[
						{id:'userUnlock',icon:'userUnlock',text:'启用委托',click:function(){
							$("#form1").submit()
						    }
						},
						{id:'userLock',icon:'userLock',text:'结束委托',click:function(){
								$.ligerDialog.confirm('结束委托，将踢出委托人登录，确认结束？', function (yes) { 
									if(yes){
										$.getJSON("entrust/info/close.do",{id:$("#id").val()},function(res){
											if(res.success){
												$.post("security/web/killSessionUser.do",{userId:res.userId,sessionId:res.sessionId},function(resp){
													if(resp.success){
														top.$.notice("委托结束！",3);
														api.close();
													}
												},"json")
											}
										});
									}
								});
							}
						},
				        {id:'close',icon:'cancel',text:'关闭',click:function(){api.close()}}
				],
				success: function (response) {
					if(response.success){
						top.$.notice("委托成功！",3);
						api.close();
					}
					else{
						$.ligerDialog.error("操作失败！<br/>" + response.msg);
					}
				},getSuccess:function(res){
					if(res.data){
						$.ligerui.get("userUnlock").set("disabled", true);
					}else{
						$.ligerui.get("userLock").set("disabled", true);
					}
				}
			});
			if(kui["ENTRUST_POSITION"]){
				var userId = '<sec:authentication property="principal.id" ></sec:authentication>'
				$.getJSON("rbac/user/getUserPositionAll.do",{userId:userId},function(res){
					if(res.success){
						var data =[]; 
						for(var i in res.data){
							data.push({id:res.data[i].id,text:res.data[i].posName})
						}
						if(data.length>0){
							$("#positionId").ligerGetComboBoxManager().setData(data);
						}
					}
				})
				$("#position").show();
			}
			
		});
        function setPosition(){
		}
    </script>
</head>
<body>
<form id="form1" action="entrust/info/save.do" getAction="entrust/info/getLastentrust.do">
    <input type="hidden" id="id" name="id">
    <input type="hidden" name="entrustStatus">
    <input type="hidden" name="entrustStartDate">
    <input type="hidden" name="entrustEndDate">
    <input type="hidden" name="entrustBy">
    <input type="hidden" name="entrustCreatedDate">
    <table cellpadding="3" class="l-detail-table">
        <tr>
            <td class="l-t-td-left">委托人：</td>
            <td class="l-t-td-right"  colspan="3">
            	<input id="entrustPerson" name="entrustPerson" type="text" ltype="text" validate="{required:true,maxlength:512}" 
            	onclick="selectUnitOrUser(1,0,'entrustPersonIds', 'entrustPerson',function(){setPosition()})"
            	ligerui="{iconItems:[{img:'k/kui/images/icons/16/icon-down.png',click:function(val,e,srcObj){selectUnitOrUser(1, 0,'entrustPersonIds', 'entrustPerson',function(){setPosition()})}}]}"/>
            	<input id="entrustPersonIds" name="entrustPersonIds" type="hidden" validate="{required:true,maxlength:512}" />
            </td>
        </tr>
        <tr id="positipositionn" style="display:none">
        	<td class="l-t-td-left">委托岗位：</td>
            <td class="l-t-td-right" colspan="3">
            	<input type="text" ltype="select" id="positionId" name="positionId" ligerui="{}"/>
            </td>
        </tr>
        <tr>
            <td class="l-t-td-left">委托事宜：</td>
            <td class="l-t-td-right" colspan="3">
            	<textarea rows="5" cols="20" name="remark" ltype="textarea" validate="{required:true,maxlength:512}"></textarea>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
