<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head pageStatus="${param.status}">
<!-- 每个页面引入，页面编码、引入js，页面标题 -->
<%@ include file="/k/kui-base-form.jsp"%>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<script type="text/javascript">
	//jQuery页面载入事件

	var beanData;
	$(function() {
		//发票附件执行上传
    	var receiptUploadConfig = {
    			fileSize : "10mb",//文件大小限制
    			businessId : "",//业务ID
    			buttonId : "receiptfilesBtn",//上传按钮ID
    			container : "receiptfilesDIV",//上传控件容器ID
    			title : "请选会议室照片",//文件选择框提示
    			extName : "jpg,gif,jpeg,png,bmp",//文件扩展名限制
    			saveDB : true,//是否往数据库写信息
    			workItem : "one",//页面多点上传文件标识符号
    			attType : "",//文件存储类型；1:数据库，0:磁盘，默认为磁盘
    			fileNum : 1,//限制上传文件数目
    			callback : function(files){//上传成功后回调函数,实现页面文件显示，交与业务自行后续处理
    				//显示图片
    				addAttachFile(files);
    			}
    		};
		var receiptUploader= new KHFileUploader(receiptUploadConfig);
		//配置资源选择器
		$("#formObj").initForm({
			success : function(responseText) {//处理成功
				if (responseText.success) {
					top.$.notice("保存成功！");
					if(api.data.window.Qm){
						api.data.window.Qm.refreshGrid();
					}else{
						api.data.window.getData();
					}
					api.close();
				} else {
					$.ligerDialog.error(responseText.msg)
				}
			},
			getSuccess : function(response) {
				if (response.success)
					beanData = response.data;
				else {
					$.ligerDialog.alert("获取数据错误!");
					return;
				}
				//显示采购单附件
				var files=response.files;
				showAttachFile(files);
			}
		});
	});
	function addAttachFile(files){
		if("${param.status}"=="detail"){
			$("#receiptfilesBtn").hide();
		}
		if(files){
			$.each(files,function(i){
				var data=files[i];
				createFileView(data.id,data.name,$("head").attr("pageStatus")=="detail"?false:true,"receiptfiles",true,function(fid){
					deleteFileUp();
				})
				getUploadFile();
			})
		}
	}
	 //显示附件文件
    function showAttachFile(files){
    	if("${param.status}"=="detail"){
			$("#receiptfilesBtn").hide();
		}
		if(files!=null&&files!=""){
			$.each(files,function(i){
				var data=files[i];
				createFileView(data.id,data.fileName,$("head").attr("pageStatus")=="detail"?false:true,"receiptfiles",true,function(fid){
					deleteFileUp();
				})
			})
		}
    }
  //将上传的所有文件id写入隐藏框中
	function getUploadFile(){
		var attachId="";
		var i=0;
		$("#receiptfiles li").each(function(){
			attachId+=(i==0?"":",")+this.id;
			i=i+1;
		});
		$("#roompic_id").val(attachId);
	}
	function deleteFileUp(){
		$("#receiptfilesBtn").show();
		$("#procufilesBtn").show();
		getUploadFile();
	}
	
	function choosePerson(){
	    top.$.dialog({
	        width: 800,
	        height: 450,
	        lock: true,
	        parent: api,
	        title: "选择人员",
	        content: 'url:app/common/person_choose.jsp',
	        cancel: true,
	        ok: function(){
	            var p = this.iframe.contentWindow.getSelectedPerson();
	            if(!p){
	                top.$.notice("请选择人员！", 3, "k/kui/images/icons/dialog/32X32/hits.png");
	                return false;
	            }
	            $("#manager_id").val(p.id);
	            $("#manager_name").val(p.name);
	            $("#manager_phone").val(p.mobile_tel);
	        }
	    });
	}

</script>

</head>
<body>
	<form name="formObj" id="formObj" method="post"
		action="oa/meetingRoom/info/save.do"
		getAction="oa/meetingRoom/info/detail.do?id=${param.id}">
		<input name="id" type="hidden" id="id"/>
		<input type="hidden" name="unit_id" id="unit_id" value="<sec:authentication property="principal.unit.id" />"/>
		<input name="unit_name" id="unit_name" type="hidden" value="<sec:authentication property="principal.unit.orgName" />"/>
		<input type="hidden" name="creator_unit_id" id="creator_unit_id" value="<sec:authentication property="principal.unit.id"/>"/>
		<input name="creator_unit_name" id="creator_unit_name" type="hidden" value="<sec:authentication property="principal.unit.orgName"/>"/>
		<input type="hidden" name="creator_id" id="creator_id" value="<sec:authentication property="principal.id"/>"/>
		<input name="creator_name" type="hidden" value="<sec:authentication property="principal.name"/>"/>
		<input name="create_time" id="create_time" type="hidden" value="<%=DateUtil.getDate(new Date())%>"/>

		<table border="1" cellpadding="3" cellspacing="0"
			class="l-detail-table">
			<tr>
				<td class="l-t-td-left">会议室名称：</td>
				<td class="l-t-td-right"><input name="code" type="text"
					ltype='text' validate="{required:true,maxlength:25}"/>
				</td>
				<td class="l-t-td-left">可容纳人数：</td>
				<td class="l-t-td-right">
				<input type="text" name="accommodate_no" validate="{required:true,maxlength:10}"  ltype="spinner" ligerui="{type:'int',suffix:'人',isNegative:false}" />
				</td>
			</tr>
			<tr>
				<td class="l-t-td-left">管理人：</td>
				<td class="l-t-td-right"><input type="hidden" name="manager_id" id="manager_id" value="<sec:authentication property="principal.id"/>"/>
				<input id="manager_name" name="manager_name" type="text" onclick="selectUnitOrUser(1,0,'manager_id','manager_name','manager_phone')"
					ltype='text' validate="{required:true,maxlength:50}" readonly="readonly" ligerui="{iconItems:[{icon:'user',click:choosePerson}]}" validate="{required:true}"  value="<sec:authentication property="principal.name"/>"/></td>
				<td class="l-t-td-left">联系电话：</td>
					<td class="l-t-td-right"><input name="manager_phone" id="manager_phone" type="text"
					ltype='text' validate="{required:true,maxlength:11}"/>
				</td>
			</tr>
			<tr>
				<td class="l-t-td-left">所在地点：</td>
				<td class="l-t-td-right" colspan="3">
				<input name="place" type="text" ltype='text' validate="{maxlength:100,required:true}" />
				</td>
			</tr>
			<tr>
			    <td class="l-t-td-left">基本设施：</td>
	            <td class="l-t-td-right" colspan="3">
	            <input type="checkbox" name="facilityOption" ltype="checkboxGroup" validate="{required:true}"
	                   ligerui="{url:'app/office/meeting/facility.json'}"/>
	            </td>
			</tr>
			<tr>
			    <td class="l-t-td-left">其它设施：</td>
	            <td class="l-t-td-right" colspan="3">
			     <textArea name="facility" type="text"	ltype='text' validate="{maxlength:100}" rows="5"></textArea>
			    </td>
			</tr>
			<tr height="80px">
				<td class="l-t-td-left">会议室照片：</td>
				<td class="l-t-td-right" colspan="3">
					<input name="roompic_id" id="roompic_id" type="hidden"/>
					<c:if test='${param.pageStatus!="detail" }'>
						<p id="receiptfilesDIV">
							<a class="l-button" id="receiptfilesBtn">
								<span class="l-button-main"><span class="l-button-text">选择文件</span></span>
							</a>
						</p>
					</c:if>
					<div class="l-upload-ok-list">
						<ul id="receiptfiles"></ul>
					</div>
				</td>
			</tr>
		</table>
	        
	</form>
</body>
</html>

