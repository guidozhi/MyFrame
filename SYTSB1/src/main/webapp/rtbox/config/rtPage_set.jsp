<%@page import="com.khnt.rtbox.tools.Utils"%>
<%@page import="com.khnt.rtbox.template.constant.RtPath"%>
<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" %>
<%
	String outDocDirPath=RtPath.outputDocPath + Utils.monthDir() +"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
<title>详情页面</title>
<%@include file="/k/kui-base-form.jsp" %>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<!-- <script type="text/javascript" src="rtbox/app/templates/rtbox.js"></script> -->
<script type="text/javascript">
	var base="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/";
	$(function() {
		$("#formObj").initForm({ //参数设置示例
			success : function(response) {
				if (response.success) {
					top.$.notice("设置成功！", 3);
					refreshTpl(response.data);
// 					api.data.window.Qm.refreshGrid();
// 					api.close();
				} else {
					$.ligerDialog.error("操作失败！请稍后再试或联系管理员！");
				}
			},
			getSuccess: function(response){
				 if($("#id").val()!=""){
			            getBusinessAttachments($("#id").val(),function(file){
			                if(file.length>0){
			                	$("#attachments").val(file[0].id);
// 			                    $("#uploadfilebtn").hide();
			                    createFileView(file[0].id,file[0].name,true,"upfilelist1",true,function(fid){});
			                }
			            });
			            
			            refreshTpl(response.data);
			        }
			}
		});
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
// 					$.ligerDialog.alert("上传成功的文件名：" + file[0].name + "；文件路径：" + file[0].path);
				}
			};

			//以下两个上传实例是指定一个页面容器，自动生成按钮和上传文件显示
			uploadConfig.fileContainer = "filecontainer1";
			var uploader1 = new KHFileUploader(uploadConfig);

	});
	
	 function initUpload(){
    	var _my_uploader = new KHFileUploader({
            buttonId : "uploadfilebtn",
            container : "upload-ctr",
            fileSize: "100mb",
            fileNum: 1,
            extName : "docx",
            callback : function(files){
                $("#attachments").val(files[0].id);
                createFileView(files[0].id,files[0].name,true,"attList",false,function(fid){
                    $("#uploadfilebtn").show();
                	initUpload();
                });
                _my_uploader.destroy();
                $("#uploadfilebtn").hide();
            }
        });
    }
	
	function refreshTpl(data){
		var templeteDocFilePath=data.templeteDocFilePath;
		
// 		templeteDocFilePath=templeteDocFilePath.replace("D:/rtbox/","");
// 		$("#down").html("<a href='"+base+templeteDocFilePath+"' target='_blank'>模板下载</a>");
		
		var name=$("#rtName").val()+"(模板).docx";
		$("#down").html("<a href='rtbox/public/down.jsp?name="+$("#rtCode").val()+"(temple).docx"+"&path="+templeteDocFilePath+"' target='_blank'>模板下载</a>");
		var rtCode=$("#rtCode").val();
		var modelType=$("#modelType").val();
		if(modelType=="1"){
			
			$("#preview").html('<a href="<%=RtPath.tplRecordPageDir%>/index.jsp?code='+rtCode+'" target="_blank">预览</a>');
			
		}else{
			<%-- $("#preview").html('<a href="<%=RtPath.tplPageDir%>'+ rtCode +'/index.jsp?code='+rtCode+'" target="_blank">预览</a>'); --%>
			$("#preview").html('<a href="<%=RtPath.tplPageDir%>/index.jsp?code='+rtCode+'" target="_blank">预览</a>');
		}
		
		
	}
	
	function outReport(){
		var rtCode=$("#rtCode").val();
		var param=[{name:"fk_report_id",value:'test'}];
		outRt("test",rtCode,param);
		
		var outPutDocDirPath = "<%=outDocDirPath%>";
		outPutDocDirPath = base + outPutDocDirPath.replace("D:/rtbox/", "")+ "test" + ".docx";
		$("#revert").html('<a href="' + outPutDocDirPath+ '" target="_blank">导出DOCX预览</a>');
	}
	 
</script>
</head>
<body>
	<form id="formObj" action="com/rt/page/setTemplate.do" getAction="com/rt/page/detail.do?id=${param.id}">
		<input type="hidden" id="id" name="id" value="${param.id}"/> 
		<input type="hidden" id="modelType" name="modelType" /> 
		<input type="hidden" id="attachments"  name="attachment" value="" />
		<table cellpadding="3" class="l-detail-table">
			<tr>
				<td class="l-t-td-left">报表代码：</td>
				<td class="l-t-td-right"><input name="rtCode" id="rtCode" type="text" ltype="text" validate="{required:true,maxlength:50}" ligerui="{readonly:true,disabled:true}" /></td>
			</tr>
			<tr>
				<td class="l-t-td-left">报表名称：</td>
				<td class="l-t-td-right"><input name="rtName" id="rtName" type="text" ltype="text" validate="{required:true,maxlength:200}" ligerui="{readonly:true,disabled:true}"  /></td>
			</tr>

			<tr style="height:50px;">
				<td class="l-t-td-left">提交模板：</td>
				<td class="l-t-td-right">
			    	<p id="filecontainer1"><a class="l-button" id="pickfiles1"><span class="l-button-main"><span class="l-button-text">选择文件</span></span></a></p>
			    	<div class="l-upload-ok-list">
						<ul id="upfilelist1"></ul>
					</div>
				</td>
			</tr>
			<tr>
				<td class="l-t-td-left">下载模板：</td>
				<td class="l-t-td-right"><div id="down">暂无</div></td>
			</tr>
			<tr>
				<td class="l-t-td-left">网页预览：</td>
				<td class="l-t-td-right"><div id="preview">暂无</div></td>
			</tr>
			<tr>
				<td class="l-t-td-left"><input type="button" value="导出DOCX预览" onclick="outReport();"/>：</td>
				<td class="l-t-td-right">
					
					<div id="revert">暂无</div>
				
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
