<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
<title></title>
<%@include file="/k/kui-base-form.jsp"%> 
<script type="text/javascript" src="pub/bpm/js/util.js"></script>
 <script type="text/javascript" src="app/finance/js/jquery.autocomplete.js"></script>
 <script type="text/javascript" src="app/common/lodop/LodopFuncs.js"></script>
<link rel="Stylesheet" href="app/finance/css/jquery.autocomplete.css" />
  <link type="text/css" rel="stylesheet" href="app/finance/css/form_detail.css" />
   <script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
  <script type="text/javascript">
  $(function() {
  	$("#certForm").initForm({
			success: function (response) {//处理成功
	    		if (response.success) {
	            	top.$.dialog.notice({
	             		content: "保存成功！"
	            	});
	          		//保存基本信息（主表）后，id未自动赋值，故此处手动赋值
	          		//$("#basic_id").attr("value",response.data.id);
	            	api.data.window.Qm.refreshGrid();
	            	api.close();
	    		} else {
	           		$.ligerDialog.error('保存失败！<br/>' + response.msg);
	      		}
			}, getSuccess: function (response){
				if("detail" == "${param.pageStatus}"){
					document.getElementById("employeeName").innerHTML=response.employeeBase.name;
				}else{
					$("#employeeName").val(response.employeeBase.name);
					$("#employeeId").val(response.employeeBase.id);
				}
				
				if (response.attachs != null && response.attachs != undefined){
					showAttachFile(response.attachs);
				}
			}
  	});		
  	// 附件上传
		var receiptUploadConfig = {
  			fileSize : "100mb",	//文件大小限制
  			businessId : "",	//业务ID
  			buttonId : "procufilesBtn",		//上传按钮ID
  			container : "procufilesDIV",	//上传控件容器ID
  			title : "图片",	//文件选择框提示
  			extName : "jpg,gif,png,bmp,mp4,AVI,wma,rmvb,rm,flash,mid,3GP,doc,pdf,txt,xls,rtf,ppt",	//文件扩展名限制
  			workItem : "",	//页面多点上传文件标识符号
  			fileNum : 100,	//限制上传文件数目
  			callback : function(file){	//上传成功后回调函数,实现页面文件显示，交与业务自行后续处理
  				addAttachFile(file);
  			}
  	};
		var receiptUploader= new KHFileUploader(receiptUploadConfig);
  	
  	
  });
//添加附件
	function addAttachFile(files){
		if("${param.pageStatus}"=="detail"){
			$("#procufilesBtn").hide();
		}
		if(files){
			var attContainerId="procufiles";
			$.each(files,function(i){
				var file=files[i];
				$("#"+attContainerId).append("<li id='"+file.id+"'>"+
						"<div><a href='fileupload/downloadByFilePath.do?path="+file.path+"&fileName="+file.name+"'>"+file.name+"</a></div>"+
						"<div class='l-icon-close progress' onclick='deleteUploadFile(\""+file.id+"\",\"\",this,getUploadFile)'>&nbsp;</div>"+
						"</li>");
			});
			getUploadFile();
		}
	}

	// 显示附件
  function showAttachFile(files){
  	if("${param.pageStatus}"=="detail"){
			$("#procufilesBtn").hide();
		}
		if(files){
			//详情
			var attContainerId="procufiles";
			if("${param.pageStatus}"=="detail"){
				$.each(files,function(i){
					var file=files[i];
					 //显示附件
				/* 	$("#"+attContainerId).append("<li id='"+file.id+"'>"+
											"<div><a href='fileupload/downloadByFilePath.do?path="+file.filePath+"&fileName="+file.fileName+"'>"+file.fileName+"</a></div>"+
											"</li>"); */
					$("#"+attContainerId).append("<li id='"+file.id+"'>"+
							"<div><a href='/fileupload/download.do?id="+file.id+"&fileName="+file.fileName+"'>"+file.fileName+"</a></div>"+
							"</li>");
				});
			}
			//修改
			else if("${param.pageStatus}"=="modify"){
				$.each(files,function(i){
					var file=files[i];
					$("#"+attContainerId).append("<li id='"+file.id+"'>"+
							/* "<div><a href='fileupload/downloadByFilePath.do?path="+file.filePath+"&fileName="+file.fileName+"'>"+file.fileName+"</a></div>"+ */
							"<div><a href='/fileupload/download.do?id="+file.id+"&fileName="+file.fileName+"'>"+file.fileName+"</a></div>"+
							"<div class='l-icon-close progress' onclick='deleteUploadFile(\""+file.id+"\",\"\",this,getUploadFile)'>&nbsp;</div>"+
							"</li>");
				});
				getUploadFile();
			}
		}
  }
// 将上传的附件id写入隐藏框中
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
                 $("#employeeId").val(p.id);
                 $("#employeeName").val(p.name);
             }
         });
     }
  </script>
</head>
<body>

<form id="certForm" name="certForm" method="post" action="employee/cert/saveCert.do?status=${param.status}" getAction="employee/cert/detailCert.do?id=${param.id}&empId=${param.empId}">
  	<input type="hidden" name="id" id="cert_id"/>
  	<input type="hidden" name="employee.id" id="employeeId" />
	<table cellpadding="3" cellspacing="0" class="l-detail-table"  >
		<tr>
		   <td class="l-t-td-left">姓名：</td>
	       <td class="l-t-td-right"><input name="employee.name" id="employeeName" type="text" ltype='text' validate="{required:true,maxlength:36}" onclick="choosePerson();" ligerui="{iconItems:[{icon:'user',click:choosePerson}]}"/></td>
	       <td class="l-t-td-left">证书编号</td>
	       <td class="l-t-td-right"><input name="cert_no" id="cert_no" type="text" ltype='text' validate="{required:true,maxlength:36}" /></td>
	      
	     
	    </tr>
	    <tr>
	       <td class="l-t-td-left">证书项目</td>
	       <td class="l-t-td-right" ><input name="cert_type" id="cert_type" type="text" ltype='text' validate="{required:true,maxlength:36}"></td>	
	       <td class="l-t-td-left">证书性质：</td>
	       <td class="l-t-td-right" ><input name="cert_category" id="cert_category" type="text" ltype='text' validate="{required:true,maxlength:36}"></td>	
	     
	    </tr>
	  
	    <tr>
	     <td class="l-t-td-left">发证机构</td>
	       <td class="l-t-td-right" ><input name="cert_issue_org" id="cert_issue_org" type="text" ltype='text' validate="{maxlength:36}" /></td>
	    	<td class="l-t-td-left">注册日期</td>
	       	<td class="l-t-td-right"><input name="first_get_date" id="first_get_date" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}"  /></td>
	     
	    </tr>
	    <tr>
	      	<td class="l-t-td-left">批准日期</td>
	       	<td class="l-t-td-right"><input name="cert_begin_date" id="cert_begin_date" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" validate="{required:true}" /></td>
	       	<td class="l-t-td-left">有效日期</td>
	       	<td class="l-t-td-right"><input name="cert_end_date" id="cert_end_date" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" validate="{required:true}" /></td>
	    </tr>
	  <%--   <tr>
	        <td class="l-t-td-left">审核权限：</td>
	       <td class="l-t-td-right" colspan="3"><u:combo name="cert_status" code="ba_sf"></u:combo></td>
	    </tr> --%>
		<tr height="80px">
            <td class="l-t-td-left">附件</td>
            <td class="l-t-td-right" colspan="3">
                <input type="hidden" name="uploadFiles" id="uploadFiles">
                <c:if test='${param.pageStatus!="detail" }'>
                    <p id="procufilesDIV">
                        <a class="l-button" id="procufilesBtn">
                            <span class="l-button-main"><span class="l-button-text">选择文件</span></span>
                        </a>
                    </p>
                </c:if>
                <div class="l-upload-ok-list">
                    <ul id="procufiles"></ul>
                </div>
            </td>
        </tr>
				
	</table> 
	</form>
</body>
</html>