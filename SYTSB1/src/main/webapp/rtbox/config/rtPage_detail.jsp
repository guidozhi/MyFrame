<%@ page import="com.khnt.rtbox.template.constant.*" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
<title>详情页面</title>
<%@include file="/k/kui-base-form.jsp"%>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<script type="text/javascript">
var pageStatus = "${param.pageStatus}";
	$(function() {
		$("#formObj").initForm({ //参数设置示例
			toolbar: [{
				id: "save",
     	        text: '保存',
     	        icon: 'save',
     	        click: function() {
     	            save(0);
     	        }
     	    },{
     	    	id: "submit",
     	        text: '保存并生成',
     	        icon: 'submit',
     	        click: function() {
     	            save(1);
     	        },
     	        disabled: true
     	    },{
     	    	id: "cancel",
     	        text: '关闭',
     	        icon: 'cancel',
     	        click: function() {
     	        	api.close();
     	        }
     	    }],
			success : function(response) {
				if (response.success) {
					top.$.notice("保存成功！", 3);
					api.data.window.Qm.refreshGrid();
					api.close();
				} else {
					$.ligerDialog.error("操作失败！请稍后再试或联系管理员！");
				}
			}
			,getSuccess: function(response){
				var isEdit = false;
				if((response.data.version == "1" && response.data.status=="1") || response.data.status == "0") {
					isEdit = true;
					try {
						enableSubmit();
					} catch (e) {
						// TODO: handle exception
					}
					
				}
				alert($("#id").val())
				if($("#id").val()!=""){
			            getBusinessAttachments($("#id").val(),function(file){
			            	alert(file.length)
			                if(file.length>0){
			                	$("#attachments").val(file[0].id);
			                	
			                    createFileView(file[0].id,file[0].name,isEdit,"upfilelist1",true,function(fid){});
			                }
			            });
			     }
				 if(!isEdit) {
					 $("#pickfiles1").hide();
				 }
			}
		});
		if(pageStatus=="add") enableSubmit();
		
		var uploadConfig = {
				fileSize: "100mb",//文件大小限制
				businessId: "",//业务ID
				buttonId: "pickfiles1",//上传按钮ID
				container: "filecontainer1",//上传控件容器ID
				title: "图片",//文件选择框提示
				extName: "jpg,gif,bpm,png,doc,docx",//文件扩展名限制
				saveDB: true,//是否往数据库写信息
				attType: "",//文件存储类型；1:数据库，0:磁盘，默认为磁盘
				workItem: "",//页面多点上传文件标识符号
				fileNum: 1,//只上传一个文件
				callback: function (file) {//回调函数
					$("#attachments").val(file[0].id);
					createFileView(file[0].id,file[0].name,true,"upfilelist1",true,function(fid){});
 					//$.ligerDialog.alert("上传成功的文件名：" + file[0].name + "；文件路径：" + file[0].path);
				}
			};

			//以下两个上传实例是指定一个页面容器，自动生成按钮和上传文件显示
			uploadConfig.fileContainer = "filecontainer1";
			var uploader1 = new KHFileUploader(uploadConfig);
			
	});
	
	 function initUpload(){
    	var _my_uploader = new KHFileUploader({
            buttonId : "pickfiles1",
            container : "upload-ctr",
            fileSize: "100mb",
            fileNum: 1,
            extName : "docx",
            callback : function(files){
                $("#attachments").val(files[0].id);
                createFileView(files[0].id,files[0].name,true,"attList",false,function(fid){
                    $("#pickfiles1").show();
                	initUpload();
                });
                _my_uploader.destroy();
                $("#pickfiles1").hide();
            }
        });
    }

	// 重写保存
	function save(isCreateTpl){
		$("#isCreateTpl").val(isCreateTpl);
		$("#formObj").submit();
	}
	
	// 启用按钮
	function enableSubmit(){
		$.ligerui.get("submit").set("disabled", false);
	}
</script>
</head>
<body>
	<form id="formObj" action="com/rt/page/save.do" getAction="com/rt/page/detail.do?id=${param.id}">
		<input type="hidden" id="id" name="id" />
		<input type="hidden" id="templateId" name="template.id" value="${param.tplId}"/>
		<input name="pagePath" id="pagePath" type="hidden" />
		<input name="status" id="status" type="hidden" value="<%=RtSign.STATUS_DISABLE%>"/>
		<input name="createdById" id="createdById" type="hidden" />
	   	<input name="createdBy" id="createdBy" type="hidden" />
	   	<input name="createdDate" id="createdDate" type="hidden" />
	   	<input name="lastUpdById" id="lastUpdById" type="hidden" />
	   	<input name="lastUpdBy" id="lastUpdBy" type="hidden" />
	   	<input name="lastUpdDate" id="lastUpdDate" type="hidden" />
	   	<input name="version" id="version" type="hidden" />
		<input type="hidden" id="attachments"  name="attachment" value="" />
		<input type="hidden" id="isCreateTpl"  name="isCreateTpl" value="" />
		<br/>
		<table cellpadding="3" class="l-detail-table">
			<tr>
				<td class="l-t-td-left">报表类型：</td>
				<td class="l-t-td-right">
					<input name="modelType" id="modelType" type="radio" ltype="radioGroup" validate="{required:true}" ligerui="{initValue:'0',data:[{id:'0',text:'报告'},{id:'1',text:'原始记录'}]}" />						
				</td>
			</tr>
			<tr>
				<td class="l-t-td-left">报表代码：</td>
				<td class="l-t-td-right"><input name="rtCode" id="rtCode" type="text" ltype="text" validate="{required:true,maxlength:50}" value="${param.rtCode}" ligerui="{readonly:true,disabled:true}" /></td>
				<td class="l-t-td-left">报表名称：</td>
				<td class="l-t-td-right"><input name="rtName" id="rtName" type="text" ltype="text" validate="{required:true,maxlength:200}" value="${param.rtName}" ligerui="{readonly:true,disabled:true}" /></td>
			</tr>
			<tr>
				<td class="l-t-td-left">设备类型：</td>
				<td class="l-t-td-right"><input name="deviceType" id="deviceType" type="text" ltype="select" validate="{required:true}" ligerUi="{data:<u:dict code='device_type'/>}" /></td>
				<td class="l-t-td-left">报表类型：</td>
				<td class="l-t-td-right"><input name="reportType" id="reportType" type="text" ltype="select" validate="{required:true}" ligerUi="{data:<u:dict code='tzsb_report_type'/>}"/></td>
			</tr>
			
			<tr>
				<td class="l-t-td-left">是否分页：</td>
				<td class="l-t-td-right"><u:combo name="isPage" code="sys_sf" attribute="initValue:'1'" validate="required:true"></u:combo></td>
				<td class="l-t-td-left">分页代码：</td>
				<td class="l-t-td-right"><input name="rtPageCode" id="rtPageCode" type="text" ltype="text" validate="{required:false,maxlength:50}" placeholder="区别全报表，其中一个分页编号，功能待定" /></td>

			</tr>
			<tr>
				<td class="l-t-td-left">关联表代码：</td>
				<td class="l-t-td-right"><input name="relTableCode" id="relTableCode" type="text" ltype="text" validate="{required:false,maxlength:100}" placeholder="关联了哪些表，以逗号分隔，仅作存储" />
				</td>
			</tr>
			<tr>

				<td class="l-t-td-left">关联字段：</td>
				<td class="l-t-td-right" colspan="3"><input name="relColCode" id="relColCode" type="text" ltype="text" validate="{required:true,maxlength:100}"
					value="FK_REPORT_ID,FK_INSPECTION_INFO_ID" placeholder="关联ID为哪些，系统建表使用，以逗号分隔，默认:FK_REPORT_ID,FK_INSPECTION_INFO_ID" /></td>
			</tr>
			<tr>
				<td class="l-t-td-left">引入CSS：</td>
				<td class="l-t-td-right" colspan="3"><input name="linkCss" id="linkCss" type="text" ltype="text" validate="{required:false,maxlength:500}" placeholder="全路径以逗号隔开" /></td>
			</tr>
			<tr>
				<td class="l-t-td-left">引入JS：</td>
				<td class="l-t-td-right" colspan="3"><input name="linkJs" id="linkJs" type="text" ltype="text" validate="{required:false,maxlength:500}" placeholder="全路径以逗号隔开" /></td>
			</tr>
			<tr>
				<td class="l-t-td-left">保存路径：</td>
				<td class="l-t-td-right"><input name="savePath" id="savePath" type="text" ltype="text" validate="{required:false,maxlength:100}" placeholder="包括类名方法名,saveAction" /></td>

				<td class="l-t-td-left">详情路径：</td>
				<td class="l-t-td-right"><input name="detailPath" id="detailPath" type="text" ltype="text" validate="{required:false,maxlength:100}" placeholder="包括类名方法名,getAction" /></td>
			</tr>
		<!-- 	<tr>
				<td class="l-t-td-left">页面存放路径：</td>
				<td class="l-t-td-right" colspan="3"><input name="pagePath" id="pagePath" type="text" ltype="text" validate="{required:false,maxlength:100}" /></td>
			</tr> -->
			<tr>
				<td class="l-t-td-left">自定义起始序号：</td>
				<td class="l-t-td-right"><input name="firstNum" id="firstNum" type="text" ltype="text" validate="{number:true,min:0}" placeholder="应用于生成特殊附页" /></td>
				
			</tr>
			<tr>
				<td class="l-t-td-left">备注：</td>
				<td class="l-t-td-right" colspan="3"><input name="remark" id="remark" type="text" ltype="text" validate="{required:false,maxlength:500}" /></td>
			</tr>
			<tr style="height:50px;">
				<td class="l-t-td-left">模板文件：</td>
				<td class="l-t-td-right">
			    	<p id="filecontainer1"><a class="l-button" id="pickfiles1"><span class="l-button-main"><span class="l-button-text">选择文件</span></span></a></p>
			    	<div class="l-upload-ok-list">
						<ul id="upfilelist1"></ul>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
