<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head pageStatus="${param.status}">
	</head>
	<%@ include file="/k/kui-base-form.jsp"%>
	<script type="text/javascript"
		src="/app/inspection/inspection_zzjd_info.js"></script>
	<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
	<style type="">
.l-icon-downloadExcel {
	background: url('k/kui/images/icons/16/excel-export.png') no-repeat center;
}
</style>
	<script type="text/javascript">
	var pageStatus="${param.pageStatus}";
	// 设备类别
	var deviceType=<u:dict sql="select t.value id,t.name text from PUB_CODE_TABLE_VALUES t,pub_code_table t1 where t.code_table_id=t1.id and   t1.code = 'device_classify' "/>
	$(function () {
		createDeviceInfoGrid();
	    $("#formObj").initForm({    //参数设置示例
	       toolbar:[
	       		{ text:'下载模板', id:'download2003',icon:'excel-export', click:download2003},
	       		{ text:'复制', id:'copy',icon:'copy', click:copy},
	            { text:'保存', id:'save',icon:'save', click:saveInfo},
	            { text:'关闭', id:'close',icon:'cancel', click:close}
	        ],
	        getSuccess:function(resp){
	        	if(resp.success){
					deviceGrid.loadData({
						Rows : resp.inspectionZZJDInfo
					});
					$("#formObj").setValues(resp.data);
					$("#contract_task_sn").val(resp.contract_task_sn);
					$("#contract_task_ids").val(resp.contract_task_id);
					$("input[id='check_type-txt']").ligerComboBox().selectValue(parseInt(resp.data.check_type));
					$.post("inspection/zzjd/getbglxName.do", {bglxId:resp.data.report_type}, function(resp) {
						if(resp.success){
							$("#report_type_name").val(resp.data);
						}
						
					});

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
				var report_type = $('#report_type_name').val();
				var type = "";
				if(report_type == ""){
					$.ligerDialog.alert("亲，请先选择您要导入的报告类型哦！");
					return;
				}else{
					if(report_type.indexOf("车用气瓶安装监督检验证书")!=-1){
						type = "2";
					}else if(report_type.indexOf("制造监督检验证书（压力容器）")!=-1){
						type = "1";
					}else if(report_type.indexOf("批量制造压力容器监督检验证书")!=-1){
						type = "3";
					}else if(report_type.indexOf("压力容器改造与重大修理监督检验证书")!=-1){
						type = "4";
					}else if(report_type.indexOf("爆破片装置安全性能监督检验证书")!=-1){
						type = "5";
					}else if(report_type.indexOf("特种设备制造监督检验证书（气瓶）")!=-1){
						type = "6";
					}else if(report_type.indexOf("制造监督检验证书（压力容器受压元件、受压部件）")!=-1){
						type = "7";
					}else if(report_type.indexOf("安全阀安全性能监督检验证书")!=-1){
						type = "8";
					}else if(report_type.indexOf("压力管道元件监检证书（埋弧焊钢管）")!=-1){
						type = "9";
					}else if(report_type.indexOf("压力管道元件监检证书（聚乙烯管）")!=-1){
						type = "10";
					}else if(report_type.indexOf("压力管道元件监检证书（低温管件）")!=-1){
						type = "11";
					}else if(report_type.indexOf("金属常压罐体制造委托监督检验证书")!=-1){
						type = "12";
					}else if(report_type.indexOf("压力管道安装安全质量监督检验证书")!=-1){
						type = "13";
					}else if(report_type.indexOf("特种设备制造监检证书（锅炉）")!=-1){
						type = "14";
					}else if(report_type.indexOf("特种设备制造监检证书（锅炉部件、组件）")!=-1){
						type = "15";
					}else if(report_type.indexOf("特种设备制造监督检验证书（储气井）")!=-1){
						type = "16";
					}else if(report_type.indexOf("锅炉设计文件鉴定报告")!=-1){
						type = "17";
					}else if(report_type.indexOf("安全阀校验报告")!=-1){
						type = "18";
					}else if(report_type.indexOf("特种设备制造监督检验证书（出口锅炉及锅炉部件、组件）")!=-1){
						type = "19";
					}else if(report_type.indexOf("特种设备制造监督检验证书（出口压力容器）")!=-1){
						type = "20";
					}else if(report_type.indexOf("封头委托制造监督检验证书")!=-1){
						type = "21";
					}else{
						alert("亲，系统暂不支持该类报告模板的导入功能哦！");
						return;
					}
				}
				if(type != ""){
					$.post("inspection/zzjd/importData.do", {fileId:file[0].id,report_type:type}, function(resp) {
						if (resp.success) {
							deviceGrid.addRows(resp.infoList);	// 添加行集合
							/*deviceGrid.loadData({			// 加载行数据
								Rows : resp.data
							});*/
							$("#file_path").val(resp.file_path);
						}else{
							$.ligerDialog.error(resp.data);
						}
					});
				}else{
					alert("亲，系统暂不支持该类报告模板的导入功能哦！");
					return;
				}			
			}
		};

		//以下上传实例是指定一个页面容器，自动生成按钮和上传文件显示
		uploadConfig.fileContainer = "filecontainer1";
		var uploader1 = new KHKuiFileuploader(uploadConfig);
	});
	
	// 下载excel2003模板，通过文件id下载
	function download2003(){
		var report_type = $('#report_type_name').val();
		var url = "";
		if(report_type == ""){
			$.ligerDialog.alert("亲，请先选择报告类型哦！");
			return;
		}else{
			if(report_type.indexOf("车用气瓶安装监督检验证书")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1407121277918181888.xls&fileName="+encodeURI(encodeURI("车用气瓶安装监督检验证书模板（2003）.xls"));
			}else if(report_type.indexOf("制造监督检验证书（压力容器）")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1407121277958299688.xls&fileName="+encodeURI(encodeURI("压力容器制造监督检验通用模板（2003）.xls"));
			}else if(report_type.indexOf("批量制造压力容器监督检验证书")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1429856462598888888.xls&fileName="+encodeURI(encodeURI("批量制造压力容器监督检验通用模板（2003）.xls"));
			}else if(report_type.indexOf("压力容器改造与重大修理监督检验证书")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1431046678271181818.xls&fileName="+encodeURI(encodeURI("压力容器改造与重大修理监督检验证书通用模板（2003）.xls"));
			}else if(report_type.indexOf("爆破片装置安全性能监督检验证书")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1431323135639988888.xls&fileName="+encodeURI(encodeURI("爆破片装置安全性能监督检验证书通用模板（2003）.xls"));
			}else if(report_type.indexOf("特种设备制造监督检验证书（气瓶）")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803523484848.xls&fileName="+encodeURI(encodeURI("特种设备制造监督检验证书（气瓶）通用模板（2003）.xls"));
			}else if(report_type.indexOf("制造监督检验证书（压力容器受压元件、受压部件）")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803549494949.xls&fileName="+encodeURI(encodeURI("特种设备制造监督检验证书（压力容器受压元件、受压部件）通用模板（2003）.xls"));
			}else if(report_type.indexOf("安全阀安全性能监督检验证书")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803523898989.xls&fileName="+encodeURI(encodeURI("安全阀安全性能监督检验证书通用模板（2003）.xls"));
			}else if(report_type.indexOf("压力管道元件监检证书（埋弧焊钢管）")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803523191919.xls&fileName="+encodeURI(encodeURI("压力管道元件监检证书（埋弧焊钢管）通用模板（2003）.xls"));
			}else if(report_type.indexOf("压力管道元件监检证书（聚乙烯管）")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803523292929.xls&fileName="+encodeURI(encodeURI("压力管道元件监检证书（聚乙烯管）通用模板（2003）.xls"));
			}else if(report_type.indexOf("压力管道元件监检证书（低温管件）")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803523393939.xls&fileName="+encodeURI(encodeURI("压力管道元件监检证书（低温管件）通用模板（2003）.xls"));
			}else if(report_type.indexOf("金属常压罐体制造委托监督检验证书")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803523595959.xls&fileName="+encodeURI(encodeURI("金属常压罐体制造委托监督检验证书通用模板（2003）.xls"));
			}else if(report_type.indexOf("压力管道安装安全质量监督检验证书")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803523696969.xls&fileName="+encodeURI(encodeURI("压力管道安装安全质量监督检验证书通用模板（2003）.xls"));
			}else if(report_type.indexOf("特种设备制造监检证书（锅炉）")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803523797979.xls&fileName="+encodeURI(encodeURI("特种设备制造监检证书（锅炉）通用模板（2003）.xls"));
			}else if(report_type.indexOf("特种设备制造监检证书（锅炉部件、组件）")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803523999999.xls&fileName="+encodeURI(encodeURI("特种设备制造监检证书（锅炉部件、组件）通用模板（2003）.xls"));
			}else if(report_type.indexOf("特种设备制造监督检验证书（储气井）")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803524191919.xls&fileName="+encodeURI(encodeURI("特种设备制造监督检验证书（储气井）通用模板（2003）.xls"));
			}else if(report_type.indexOf("锅炉设计文件鉴定报告")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803524595959.xls&fileName="+encodeURI(encodeURI("锅炉设计文件鉴定报告通用模板（2003）.xls"));
			}else if(report_type.indexOf("安全阀校验报告")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803524493949.xls&fileName="+encodeURI(encodeURI("安全阀校验报告通用模板（2003）.xls"));
			}else if(report_type.indexOf("特种设备制造监督检验证书（出口锅炉及锅炉部件、组件）")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803524494949.xls&fileName="+encodeURI(encodeURI("特种设备制造监督检验证书（出口锅炉及锅炉部件、组件）通用模板（2003）.xls"));
			}else if(report_type.indexOf("特种设备制造监督检验证书（出口压力容器）")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803525595959.xls&fileName="+encodeURI(encodeURI("特种设备制造监督检验证书（出口压力容器）通用模板（2003）.xls"));
			}else if(report_type.indexOf("封头委托制造监督检验证书")!=-1){
				url = "/fileupload/downloadByFilePath.do?path=1436161803526695959.xls&fileName="+encodeURI(encodeURI("封头委托制造监督检验证书通用模板（2003）.xls"));
			}else{
				alert("亲，系统暂无该类报告模板哦！");
				return;
			}
		}
		if(url != ""){
			window.location.href = url;
		}else{
			alert("亲，系统暂无该类报告模板哦！");
			return;
		}
	}
		
	function saveInfo(){
		//验证表单数据
		if ($("#formObj").validate().form()) {
			var report_type= $('#report_type_name').val();
			var type = "";
			if(report_type == ""){
				$.ligerDialog.alert("亲，请先选择报告类型哦！");
				return;
			}else{
				if(report_type.indexOf("车用气瓶安装监督检验证书")!=-1){
					type = "2";
				}else if(report_type.indexOf("制造监督检验证书（压力容器）")!=-1){
					type = "1";
				}else if(report_type.indexOf("批量制造压力容器监督检验证书")!=-1){
					type = "3";
				}else if(report_type.indexOf("压力容器改造与重大修理监督检验证书")!=-1){
					type = "4";
				}else if(report_type.indexOf("爆破片装置安全性能监督检验证书")!=-1){
					type = "5";
				}else if(report_type.indexOf("特种设备制造监督检验证书（气瓶）")!=-1){
					type = "6";
				}else if(report_type.indexOf("制造监督检验证书（压力容器受压元件、受压部件）")!=-1){
					type = "7";
				}else if(report_type.indexOf("安全阀安全性能监督检验证书")!=-1){
					type = "8";
				}else if(report_type.indexOf("压力管道元件监检证书（埋弧焊钢管）")!=-1){
					type = "9";
				}else if(report_type.indexOf("压力管道元件监检证书（聚乙烯管）")!=-1){
					type = "10";
				}else if(report_type.indexOf("压力管道元件监检证书（低温管件）")!=-1){
					type = "11";
				}else if(report_type.indexOf("金属常压罐体制造委托监督检验证书")!=-1){
					type = "12";
				}else if(report_type.indexOf("压力管道安装安全质量监督检验证书")!=-1){
					type = "13";
				}else if(report_type.indexOf("特种设备制造监检证书（锅炉）")!=-1){
					type = "14";
				}else if(report_type.indexOf("特种设备制造监检证书（锅炉部件、组件）")!=-1){
					type = "15";
				}else if(report_type.indexOf("特种设备制造监督检验证书（储气井）")!=-1){
					type = "16";
				}else if(report_type.indexOf("锅炉设计文件安全鉴定及节能审查报告")!=-1){
					type = "17";
				}else if(report_type.indexOf("安全阀校验报告")!=-1){
					type = "18";
				}else if(report_type.indexOf("特种设备制造监督检验证书（出口锅炉及锅炉部件、组件）")!=-1){
					type = "19";
				}else if(report_type.indexOf("特种设备制造监督检验证书（出口压力容器）")!=-1){
					type = "20";
				}else if(report_type.indexOf("封头委托制造监督检验证书")!=-1){
					type = "21";
				}
			}
			
			// 验证grid
			if(!validateGrid(deviceGrid)){
				return false;
			}
			var grid=deviceGrid.getData();
			if(grid.length==0){
				$.ligerDialog.alert("检验报告证书不能为空");
				return;
			}

			var rwdId=$("#contract_task_ids").val();
			var rwdSn=$("#contract_task_sn").val();
			if(rwdId=="" || rwdSn==""){
				$("#save").removeAttr("disabled");
				$.ligerDialog.alert("请选择检验任务单！");
				return;
			}
			
			if(confirm("确定保存？")){
				$("#save").attr("disabled","disabled");
				$("#jdjy_type").val(type);
				url="inspection/zzjd/saveBasic.do?status="+pageStatus+"&contract_task_id="+rwdId+"&contract_task_sn="+rwdSn;
				var formData = $("#formObj").getValues();
				var data = {};
				data = formData;
				data["inspectionZZJDInfo"] = deviceGrid.getData();
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
	
	function copy(){
		var report_type = $("#report_type").val();
		if(report_type == ""){
			$.ligerDialog.alert("亲，请先选择报告类型再复制报告哦！");
			return;
		}
		top.$.dialog({
			parent: api,
			width : 800, 
			height : 550, 
			lock : true, 
			title:"复制报告内容",
			content: 'url:app/inspection/copy_zzjd_list.jsp?report_type='+report_type,
			data : {"window" : window}
		});
	}
	
	function callBack(ids){
		$.post("inspection/zzjdinfo/getInfos.do?ids="+ids, function(resp) {
			if (resp.success) {
				deviceGrid.addRows(resp.list);
				/*deviceGrid.loadData({
					Rows : resp.list
				});*/
			}else{
				$.ligerDialog.error(resp.msg);
			}
		});
	}	
	
	// 选择报告类型
	function selectReport(){	
		top.$.dialog({
			parent: api,
			width : 600, 
			height : 500, 
			lock : true, 
			title:"选择报告类型",
			content: 'url:app/inspection/choose_report_list.jsp',
			data : {"parentWindow" : window}
		});
	}
	
	function callBackReport(id, report_name){
		$('#report_type').val(id);					// 报告类型ID
		$('#report_type_name').val(report_name);	// 报告名称
	}		
	    
	function close(){
		api.close();
	}	
	function valChk(){
		var  check_type= $('#check_type').val();	// 获取检验类别
//			var com_name=$("#com_name").val();
//			com_name= encodeURI(encodeURI(com_name))
		
		top.$.dialog({
			parent: api,
			width : 800, 
			height : 550, 
			lock : true, 
			title:"选择检验任务单",
// 			content: 'url:app/enter/enter_open_list.jsp',
			content:'url:app/inspection/serviceDepartment_jyrwd.jsp?check_type='+check_type,
			data : {"parentWindow" : window}
		});
	}
	function jyrwdBack(id,sn){
		$("#contract_task_ids").val(id);
		$("#contract_task_sn").val(sn);
	}
</script>
	<body>
		<form name="formObj" id="formObj" method="post"
			action="inspection/zzjd/saveBasic.do"
			getAction="inspection/zzjd/getDetail.do?id=${param.id}">
			<input id="id" name="id"  type="hidden"  />
			<input id="sn" name="sn"  type="hidden"  />
			<input id="file_path" name="file_path"  type="hidden"  />
			<input id="file_name" name="file_name"  type="hidden"  />
			<input id="jdjy_type" name="jdjy_type"  type="hidden"  />
			<fieldset class="l-fieldset">
				<legend class="l-legend">
					<div>
						基本信息
					</div>
				</legend>
				<table border="1" cellpadding="3" cellspacing="0" width=""
					class="l-detail-table">
					<tr>
						<td class="l-t-td-left">检验类别：</td>
						<td class="l-t-td-right"><u:combo name="check_type" code="BASE_CHECK_TYPE" validate="required:true"  attribute="disabled:false"   /></td>
						<td class="l-t-td-left">
							报告类型：
						</td>
						<td class="l-t-td-right">
							<input id="report_type" name="report_type"  type="hidden"  />
							<input type="text" id="report_type_name" name="report_type_name" ltype='text' validate="{required:true}"
								ligerui="{value:'',iconItems:[{icon:'add',click:function(){selectReport()}}]}" onclick="selectReport()"/>
						</td>
						<td class="l-t-td-left">检验任务单：</td>
						<td class="l-t-td-right" >
							<input type="hidden"  name="contract_task_ids" id="contract_task_ids"/>
							<input type="text" id="contract_task_sn" name="contract_task_sn" readonly="readonly" ltype="text" validate="{required:false}" onclick="valChk()"  onchange="setValues(this.value,'contract_task_id')" />
						</td>
						
					</tr>
				</table>
			</fieldset>
			<fieldset class="l-fieldset">
				<legend class="l-legend">
					<div>
						检验报告证书列表
					</div>
				</legend>
				<div id="deviceInfos"></div>
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