<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head pageStatus="${param.status}">
	</head>
	<%@ include file="/k/kui-base-form.jsp"%>
	<script type="text/javascript"
		src="app/payment/payment_import_info.js"></script>
	<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
	<style type="">
	.l-icon-downloadExcel {
		background: url('k/kui/images/icons/16/excel-export.png') no-repeat center;
	}
	</style>
	<script type="text/javascript">
	var pageStatus="${param.pageStatus}";
	var orgList = <u:dict sql="select id code, ORG_NAME text from SYS_ORG where (ORG_CODE like 'jd%' or ORG_CODE like 'cy%') and ORG_CODE != '100049' and ORG_CODE != '100068' order by orders"/>
	
	$(function () {
		createPaymentInfoGrid();
	    $("#formObj").initForm({    //参数设置示例
	       toolbar:[
	       		{ text:'下载模板', id:'download2003',icon:'excel-export', click:download2003},
	            { text:'保存', id:'save',icon:'save', click:saveInfo},
	            { text:'关闭', id:'close',icon:'cancel', click:close}
	        ],
	        getSuccess:function(resp){
	        	if(resp.success){
					paymentGrid.loadData({
						Rows : resp.inspectionZZJDInfo
					});
					$("#formObj").setValues(resp.data);
				}
	        },
	        success: function (response) {//处理成功
	    		if (response.success) {
		      		top.$.dialog.notice({
			             content: "保存成功！"
					});
	            	api.data.window.refreshGrid();
	            	api.close();
	    		} else {
	           		$.ligerDialog.error('保存失败！<br/>' + response.msg);
	      		}
			}
		});
		
		var uploadConfig = {
			fileSize: "100mb",	//文件大小限制
			businessId: "",		//业务ID
			title: "Excel",		//文件选择框提示
			extName: "xls,xlsx",	//文件扩展名限制
			saveDB: false,	//是否往数据库写信息
			attType: "",	//文件存储类型；1:数据库，0:磁盘，默认为磁盘
			workItem: "",	//页面多点上传文件标识符号
			fileNum: 1,		//只上传一个文件
			callback: function (file) {	//回调函数				
				$.post("payment/payInfo/importData.do", {filename:file[0].id}, function(resp) {
					if (resp.success) {
						paymentGrid.addRows(resp.infoList);	// 添加行集合
						//$("#file_path").val(resp.file_path);
					}else{
						$.ligerDialog.error(resp.data);
					}
				});			
			}
		};

		//以下上传实例是指定一个页面容器，自动生成按钮和上传文件显示
		uploadConfig.fileContainer = "filecontainer1";
		var uploader1 = new KHKuiFileuploader(uploadConfig);
	});
	
	// 下载excel2003模板，通过文件id下载
	function download2003(){
		var url = "${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/fileupload/downloadByFilePath.do?path=1407121277958899888.xls&fileName="+encodeURI(encodeURI("通用开票批量导入模板.xls"));
		window.location.href = url;
	}
		
	function saveInfo(){
		//验证表单数据
		if ($("#formObj").validate().form()) {	
			// 验证grid
			if(!validateGrid(paymentGrid)){
				return false;
			}
			     
			if(confirm("确定保存？")){
				$("#save").attr("disabled","disabled");
				url="payment/payInfo/batchSave.do?status="+pageStatus;
				var formData = $("#formObj").getValues();
				var data = {};
				data = formData;
				data["inspectionPayInfo"] = paymentGrid.getData();
				$("body").mask("正在保存数据，请稍后！");
				$.ajax({
					url: url,
					type: "POST",
				 	datatype: "json",
				 	contentType: "application/json; charset=utf-8",
				 	data: $.ligerui.toJSON(data),
				  	success: function (resp) {
				   		$("body").unmask();
				      	if (resp["success"]) {
				       		if(api.data.window.Qm){
				                api.data.window.Qm.refreshGrid();
				   			}
				         	top.$.dialog.notice({content:'保存成功！'});
				     		api.close();
				     	}else{
				      		$.ligerDialog.error('提示：' + resp.msg);
				      		$("#save").removeAttr("disabled");
				    	}
				  	},
					error: function (resp) {
				   		$("body").unmask();
						$.ligerDialog.error('提示：' + resp.msg);
						$("#save").removeAttr("disabled");
					}
				});
			}        
		}
	}
	
	function close(){
		api.close();
	}	
</script>
	<body>
		<form name="formObj" id="formObj" method="post"
			action="payment/payInfo/batchSave.do" getAction="payment/payInfo/initBatch.do">
			<input id="id" name="id"  type="hidden"  />
			<!-- <input id="file_path" name="file_path"  type="hidden"  /> -->
			<fieldset class="l-fieldset">
				<legend class="l-legend">
					<div>
						通用开票信息列表
					</div>
				</legend>
				<div id="paymentInfos"></div>
			</fieldset>
			<fieldset class="l-fieldset" id="filecontainer1">
				<legend class="l-legend">
					<div>
						Excel导入
					</div>
				</legend>
			</fieldset>
		</form>
	</body>
</html>