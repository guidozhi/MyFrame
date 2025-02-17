<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<head pageStatus="${param.status}">
<%@ include file="/k/kui-base-form.jsp"%>
<script type="text/javascript" src="app/fwxm/contract/contract_custom_list.js"></script>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript">
var pageStatus = "${param.status}";		
<%
String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

%>

var  flag = ${param.flag}

$(function() {
	$("#formObj").initForm({
		 toolbar:[
                   {text:"保存", icon:"save", click:function(){
                	   var contract_type = $("#contract_type").ligerGetRadioGroupManager().getValue();
                	   var fk_contract_id = $("#fk_contract_id").val();
                	   if(contract_type=="2"&&fk_contract_id==""){
                		   $.ligerDialog.warn('分包合同需要选择总合同!')
                		   return;
                	   }
                           $("#formObj").submit();
                       }
                   },
	            	{text:"取消", icon:"cancel", click:function(){
			        	api.close();
		    }}
               ],
        
        toolbarPosition :"bottom",
		getSuccess : function(resp) {
		},
		success : function(resp) {//处理成功
			if (resp.success) {
				top.$.notice("保存成功！");
				api.data.window.submitAction();
				api.close();
			} else {

					$.ligerDialog.error('保存失败!')
			}
		}
	});
	/*
	if ("${param.status}" == "add") {
		$.get("rbac/user/newUserCode.do", function(response) {
			if (response.success) {
				$("#userCode").val(response.data.value);
			} else {
				$.ligerDialog.error("获取用户编号发生错误，请稍后再试，或者请联系系统维护人员!");
			}
		});
	}
	*/
});

//选择物品借阅人
function selUser() {
	var inspect_op_id = "inspect_op_id";
	var inspect_op = "inspect_op";
	selectUnitOrUser(1, 0, inspect_op_id, inspect_op, function(callbackData) {
		var userId = callbackData["code"];
		$.ajax({
			url : "rbac/user/detail.do?id=" + userId,
			type : "POST",
			async : false,
			success : function(callbackData) {
				if(callbackData.data.employee){
					var org = callbackData.data.employee.org;
					$("#inspect_depart_id").val(org.id);
					$("#inspect_depart").val(org.orgName);
				}
			}
		});
	});
}

//选择谈判记录人
function selUserR() {
	var negotiate_recorder_id = "negotiate_recorder_id";
	var negotiate_recorder = "negotiate_recorder";
	selectUnitOrUser(1, 0, negotiate_recorder_id, negotiate_recorder, function(callbackData) {
		/* var userId = callbackData["code"];
		$.ajax({
			url : "rbac/user/detail.do?id=" + userId,
			type : "POST",
			async : false,
			success : function(callbackData) {
				if(callbackData.data.employee){
					var org = callbackData.data.employee.org;
					$("#inspect_depart_id").val(org.id);
					$("#inspect_depart").val(org.orgName);
				}
			}
		}); */
	});
}

function callBack(com_id,com_name){
	//选择单位回调方法
	$("#fk_customer_id").val(com_id);
	$("#custom_com_name").val(com_name);
}

function selectorg(type){
	com_type=type;
	var url = 'url:app/fwxm/contract/custom_open_list.jsp';
	top.$.dialog({
		parent: api,
		width : 800, 
		height : 550, 
		lock : true, 
		title:"选择客户信息",
		content: url,
		data : {"parentWindow" : window}
	});
}

function callBackContact(id,contract_no){
	//选择总合同回调方法
	$("#fk_contract_id").val(id);
	$("#fk_contract_no").val(contract_no);
}
function selectContract(){
	var id = $("#id").val();
	var url = 'url:app/fwxm/contract/contract_open_list.jsp?id='+id;
	top.$.dialog({
		parent: api,
		width : 800, 
		height : 550, 
		lock : true, 
		title:"选择合同信息",
		content: url,
		data : {"parentWindow" : window}
	});
}

function changeType(val,text){
	if(val=="2"){
		$("#sumContract").show();
	}else{
		$("#fk_contract_id").val("");
		$("#fk_contract_no").val("");
		$("#sumContract").hide();
		
	}
}

function selectPartner(){
	selectUnitOrUser("3", 1, 'negotiate_parter_id', 'negotiate_parter');
}
</script>
</head>
<body>
<form name="formObj" id="formObj" method="post" Action="contractInfoAction/saveBasic.do"
			getAction="contractInfoAction/detail.do?id=${param.id}">
	<fieldset class="l-fieldset">
		<legend class="l-legend">
			<div>合同基本信息</div>
		</legend>
		
	<table cellpadding="3" cellspacing="0" class="l-detail-table">
		<input id="id" name="id" type="hidden" />
		<input id="doc_ids" name="doc_ids" type="hidden"/>
		<input id="data_status" name="data_status" type="hidden" value="0"/>
		<input id="audit_step" name="audit_step" type="hidden" value="0"/>
		<input id="file_date" name="file_date" type="hidden" />
		<input id="pay_sure" name="pay_sure" type="hidden" />
		<input id="sign_op_id" name="sign_op_id" type="hidden" />
		<input id="sign_op" name="sign_op" type="hidden" />
		<input id="file_status" name="file_status" type="hidden" />
		
		
		<tr> 
        	<td class="l-t-td-left"> 合同编号:</td>
        	<td class="l-t-td-right"> 
        		<input id="contract_no" name="contract_no" type="text" ltype='text' validate="{required:true,maxlength:100}"
        	</td>
        	<td class="l-t-td-left"> 签订合同单位:</td>
	        <td class="l-t-td-right"> 
	        	<input type="hidden" id="fk_customer_id" name="fk_customer_id"/>
	        	<input id="custom_com_name" name="custom_com_name" type="text" ltype='text' validate="{required:true,maxlength:255}"
	        	 onclick="selectorg('0')" ligerui="{readonly:true,value:'',iconItems:[{icon:'add',click:function(){selectorg('0')}}]}"
				/>
	        </td>
			
		</tr>
		<tr> 
			<td class="l-t-td-left"> 项目名称:</td>
        	<td class="l-t-td-right" colspan="3"> 
        	<input id="project_name" name="project_name" type="text" ltype='text'/>
        	</td>
        </tr>
       	<tr> 
	        <td class="l-t-td-left"> 合同性质:</td>
        	<td class="l-t-td-right"> 
        		<input id="contract_property" name="contract_property" type="radio" validate="{required:true}"
        		 ltype='radioGroup' ligerui="{
        		 initValue:'1',
        		 data:[{id:'1',text:'公开合同'},{id:'0',text:'非公开合同'}]
        		 }"/>
        	</td>
	        <td class="l-t-td-left"> 合同类型:</td>
	        <td class="l-t-td-right"> 
	        	<input id="contract_type" name="contract_type" type="radio" validate="{required:true}"
        		 ltype='radioGroup' ligerui="{
        		 initValue:'1',
        		 onChange:changeType,
        		 data:[{id:'1',text:'普通合同'},{id:'2',text:'分包合同'}]
        		 }"/>
	        </td>
		</tr>
		<tr style="display: none;" id="sumContract"> 
	        <td class="l-t-td-left"> 总合同:</td>
        	<td class="l-t-td-right" colspan="3"> 
        		<input type="hidden" id="fk_contract_id" name="fk_contract_id"/>
	        	<input id="fk_contract_no" name="fk_contract_no" type="text" ltype='text'
	        	 onclick="selectContract()" ligerui="{readonly:true,value:'',iconItems:[{icon:'add',click:function(){selectContract()}}]}"
				/>
        	</td>
	        
		</tr>
		<tr> 
			<td class="l-t-td-left">签订时间:</td>
        	<td class="l-t-td-right"> 
        		<input id="sign_time" name="sign_time" type="text" ltype='date' validate="{required:true}"
        		 ligerui="{format:'yyyy-MM-dd'}" value="<%=now%>"/>
        	</td>
			<td class="l-t-td-left"> 发展部经办人:</td>
        	<td class="l-t-td-right"> 
        	<input id="dev_op_id" name="dev_op_id" type="hidden" value="<sec:authentication property="principal.id" />"/>
        		<input id="dev_op" name="dev_op" type="text" ltype='text' readonly="readonly" value="<sec:authentication property="principal.name" />"/>
        	</td>
        	
		</tr>
		<tr> 
			<td class="l-t-td-left"> 检验部门经办人:</td>
        	<td class="l-t-td-right"> 
        	<input id="inspect_op_id" name="inspect_op_id" type="hidden" />
        		<input id="inspect_op" name="inspect_op" type="text" ltype='text'
        		 validate="{required:true}" readonly="readonly" ligerui="{iconItems:[
							{icon:'user',click:function(val,e,srcObj){selUser()}}
				]}"/>
        	</td>
        	<td class="l-t-td-left">检验部门：</td>
			<td class="l-t-td-right"><input type="hidden"
				name="inspect_depart_id" id="inspect_depart_id"/> <input
				name="inspect_depart" id="inspect_depart" type="text"
			ltype='text' validate="{required:true,maxlength:50}" /></td>
		</tr>
		<tr> 
			<td class="l-t-td-left">金额:</td>
        	<td class="l-t-td-right"> 
        		<input id="amount" name="amount" type="text" ltype='text'
        		 validate="{required:true,number:true,maxlength:10}" ligerui="{suffix:'万元'}"/>
        	<td class="l-t-td-left">提醒日期:</td>
        	<td class="l-t-td-right"> 
        		<input id="effect_date" name="effect_date" type="text" ltype='date' validate="{required:false}"
        		 ligerui="{format:'yyyy-MM-dd'}" />
        	</td>
		</tr>
		<tr> 
			<td class="l-t-td-left"> 备注:</td>
        	<td class="l-t-td-right" colspan="3"> 
        	<textarea rows="3" cols=""  id="remark" name="remark"  ltype='text'></textarea>
        	</td>
        </tr>
		</table>
	</fieldset>	
	<fieldset class="l-fieldset">
		<legend class="l-legend">
			<div>合同谈判情况</div>
		</legend>
			<table cellpadding="3" cellspacing="0" class="l-detail-table">
				<tr>
		        	<td class="l-t-td-left">谈判日期:</td>
		        	<td class="l-t-td-right"> 
		        		<input id="negotiate_date" name="negotiate_date" type="text" ltype='date' validate="{required:false}"
		        		 ligerui="{format:'yyyy-MM-dd'}" />
		        	</td>
		        	<td class="l-t-td-left">记录人:</td>
			       <td class="l-t-td-right"> 
		        	<input id="negotiate_recorder_id" name="negotiate_recorder_id" type="hidden" />
		        		<input id="negotiate_recorder" name="negotiate_recorder" type="text" ltype='text'
		        		 validate="{required:false}" ligerui="{iconItems:[
									{icon:'user',click:function(val,e,srcObj){selUserR()}}
						]}"/>
		        	</td>
				</tr>
				<tr>
		        	<td class="l-t-td-left">参加人员:</td>
			       <td class="l-t-td-right" colspan="3"> 
		        	<input id="negotiate_parter_id" name="negotiate_parter_id" type="hidden" />
		        		<input id="negotiate_parter" name="negotiate_parter" type="text" ltype='text'
		        		 validate="{required:false,maxlength:640}" ligerui="{iconItems:[
									{icon:'user',click:function(val,e,srcObj){selectPartner()}}
						]}"/>
		        	</td>
				</tr>
				<tr>
		        	<td class="l-t-td-left">谈判地点:</td>
			       <td class="l-t-td-right" colspan="3"> 
		        		<input id="negotiate_place" name="negotiate_place" type="text" ltype='text'
		        		 validate="{required:false,maxlength:100}" />
		        	</td>
				</tr>
				<tr>
		        	<td class="l-t-td-left">谈判内容:</td>
			       <td class="l-t-td-right" colspan="3"> 
		        		<textarea rows="10" cols="" id="negotiate_contain" name="negotiate_contain"></textarea>
		        	</td>
				</tr>
			</table>
		</fieldset>
</form>
</body>
</html>
