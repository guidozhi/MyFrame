<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%
	CurrentSessionUser user = SecurityUtil.getSecurityUser();
	String userId = user.getId();
	String userName = user.getName();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.status}">
<%@ include file="/k/kui-base-form.jsp"%>
<title>选择审核意见</title>
<script type="text/javascript" src="/app/fwxm/scientific/instruction/selectUser/selectUnitOrUser.js"></script>
 <script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<script type="text/javascript">

function getSelectResult() {
	var result = null;
	var opinion=$("#opinion").ligerComboBox().getValue();
	var remark=$("#remark").val();
// 	var backs=$('#bacfk').ligerComboBox().getValue();
// 	var uploadFiles=$("#uploadFiles").val();
	if(opinion=="1"){
		if($("#remark").val()==""||$("#remark").val()=="null"){
			//$.ligerDialog.error("请填写不通过原因！！！");
			result = {'opinion':opinion,'remark':remark};
		}else{
			result = {'opinion':opinion,'remark':remark};
		}
	}else{
		result = {'opinion':opinion,'remark':remark};
	}
	return result;
}

$(function() {
	//alert(11);
	 $("#formObj").initForm({
			toolbar: null,
		
	}); 
	// 附件上传
		var receiptUploadConfig = {
 			fileSize : "10mb",	//文件大小限制
 			businessId : "",	//业务ID
 			buttonId : "procufilesBtn",		//上传按钮ID
 			container : "procufilesDIV",	//上传控件容器ID
 			title : "图片、Word、Excel",	//文件选择框提示
 			extName : "jpg,gif,png,bmp,doc,docx,xls,xlsx,rar,zip,pptx,pps",	//文件扩展名限制
 			workItem : "",	//页面多点上传文件标识符号
 			fileNum : 1,	//限制上传文件数目
 			callback : function(file){	//上传成功后回调函数,实现页面文件显示，交与业务自行后续处理
 				addAttachFile(file);
 			}
 	};
		var receiptUploader= new KHFileUploader(receiptUploadConfig);
});
function save(){
	$.ligerDialog.confirm("确认提交？", function(yes){
		if(yes){
			 $("body").mask("提交中...");
			var opinion=$("#opinion").ligerGetRadioGroupManager().getValue();
			var remark=$("#remark").val();
			var backs=$('#back').ligerComboBox().getValue();
			var uploadFiles=$("#uploadFiles").val();
			var ids="${param.id}";
			if(opinion==""){
				$.ligerDialog.warn("请选择结论！！！");
				return;
			}
			$.ajax({
	        	url: "com/tjy2/instructionRw/subAudit.do?backs="+backs,
	            type: "POST",
	            data:{id:ids,opinion:opinion,remark:remark,uploadFiles:uploadFiles},
	            success: function (resp) {
	            	$("body").unmask();
	                if (resp.success) {
	                	
	               	 	top.$.notice('提交成功！！！');
	               	    api.data.window.Qm.refreshGrid();
						api.close();
	                }else{
	                	$.ligerDialog.error('提示：' + resp.msg);
	                }
	            },
	            error: function (data0,stats) {
	                $.ligerDialog.error('提示：' + data.msg);
	            }
	        });
		}
	});
}
function selectUser() {
    selectUnitOrUser(1, 1, "auditId", "auditName");
}
function changeFlag(flag) {
// 	$("#back").hide();
// 	if (flag == "0") {
// 		$("#sub_op").show();
// 	} else {
// 		$("#back").show();
// 		$("#sub_op").hide();
// 	}
}
//添加附件
	function addAttachFile(files){
		if("${param.status}"=="detail"){
			$("#procufilesBtn").hide();
		}
		if(files){
			var attContainerId="procufiles";
			$.each(files,function(i){
				var file=files[i];
				$("#"+attContainerId).append("<li id='"+file.id+"'>"+
						"<div><a href='fileupload/downloadByFilePath.do?path="+file.path+"&fileName="+file.name+"'>"+file.name+"</a></div>"+
						"<div class='l-icon-close progress' onclick='deleteUploadFile(\""+file.id+"\",\""+file.path+"\",this,getUploadFile)'>&nbsp;</div>"+
						"</li>");
			});
			getUploadFile();
		}
	}
	 
 	// 将上传的电子签名id写入隐藏框中
	function getUploadFile(){
		var attachId="";
		var i=0;
		$("#procufiles li").each(function(){
			attachId+=(i==0?"":",")+this.id;
			i=i+1;
		});
		if(attachId!=""){
			attachId=attachId.substring(0,attachId.length);
		}
		$("#uploadFiles").val(attachId);
	}
</script>
</head>
<body>
	<form name="formObj" id="formObj">
	<table  border="1" cellpadding="3" cellspacing="0"
			class="l-detail-table">
		<tr>
			<td class="l-t-td-left">结论：</td>
			<td class="l-t-td-right">
			<input id="opinion" name="opinion" type="radio" ltype='radioGroup'
			 ligerui="{initValue:'0',onChange:changeFlag,data:[{id:'0',text:'通过'},{id:'1',text:'不通过'}]}"/>
			</td>
		</tr>
				<tr>
					<td class="l-t-td-left">审核意见：</td>
					<td class="l-t-td-right" >
						<textarea id="remark" rows="2" cols="25" name="remark" class="l-textarea" validate="{maxlength:256}" ></textarea>
					</td>	
				</tr>
<!-- 			<tr id="back" > -->
<!-- 					<td class="l-t-td-left">退回步骤：</td> -->
<!-- 					<td class="l-t-td-right"><input type="radio" name="bacfk" -->
<!-- 				id="bacfk" ltype="radioGroup"  -->
<!-- 				validate="{required:true}" -->
<%-- 				ligerui="{ --%>
<!-- 					initValue:'2', -->
<!-- 					readonly:true, -->
<!-- 					data: <u:dict code="BASE_SCIENTIFIC_TH"/> -->
<%-- 					}" /> --%>
<!-- 					</td>									 -->
<!-- 				</tr> -->
<!-- 				  <tr> -->
<!--             <td class="l-t-td-left">上传文件：</td> -->
<!--             <td class="l-t-td-right" > -->
<!--             <input name="uploadFiles" type="hidden" id="uploadFiles" validate="{maxlength:1000}" /> -->
<!-- 						<p id="procufilesDIV"> -->
<!-- 							<a class="l-button" id="procufilesBtn"> -->
<!-- 								<span class="l-button-main"><span class="l-button-text">选择文件</span></span> -->
<!-- 							</a> -->
<!-- 						</p> -->
<!-- 				    	<div class="l-upload-ok-list"> -->
<!-- 							<ul id="procufiles"></ul> -->
<!-- 						</div> -->
<!--             </td> -->
<!--         </tr> -->
	</table>
	</form>
</body>
</html>
