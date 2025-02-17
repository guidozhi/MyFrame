<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html;charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
    <title></title>
<%@ include file="/k/kui-base-form.jsp"%>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<!-- <script type="text/javascript" src="app/archives/ss/abc.js"></script> -->
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>

<script type="text/javascript">
var lb ="${param.device_type}";   //类别
var idd ="${param.id}";   
var fileid;
var filepath;
var filename;
$(function() {
    $("#form1").initForm({
		success: function (response) {//处理成功
    		if (response.success) {
            	top.$.dialog.notice({
             		content: "保存成功！"
            	});
            	api.close();
         		api.data.window.refreshGrid();
    		} else {
           		$.ligerDialog.error('保存失败！<br/>' + response.msg);
      		}
		}, getSuccess: function (response){
			if (response.attachs != null && response.attachs != undefined){
				
				showAttachFile(response.attachs);}
		}, toolbar: [{text: "保存2", icon: "save", click: function(){
      				//表单验证
//       				var b=$("#fileName").val();
//       				var d=b.substring(b.indexOf("/")+1,b.lastIndexOf("-"));
// 			    	if (lb==d) {
						var uploadFiles=$("#uploadFiles").val();
			    		var formData = $("#form1").getValues();
			            $("body").mask("正在保存......");
			           $.ajax({
			               url: "tx/file/save1.do?uploadFiles="+uploadFiles,
			               type: "POST",
			               datatype: "json",
			               contentType: "application/json; charset=utf-8",
			               data: $.ligerui.toJSON(formData),
			               success: function (data, stats) {
			                   $("body").unmask();//alert(99);
			                   if (data["success"]) {
			                   	top.$.notice(data.msg,3);	
			                       //top.$.dialog.notice({content:'保存成功！'});
			                       api.data.window.Qm.refreshGrid();//刷新
			                       api.close();
			                   }else{
			                       $.ligerDialog.error('提示：' + data.msg);
			                       api.data.window.Qm.refreshGrid();//刷新
			                   }
			               },
			               error: function (data,stats) {
			                   $("body").unmask();
			                   $.ligerDialog.error('提示：' + data.msg);
			               }
			           });
					    //表单提交
					    //$("#form1").submit();
// 			    	}else{
// 			    		 $.ligerDialog.error('提示：' +"类别选择错误，请检查好后上传！");
// 			    	}
      			}
      		},
			{text: "关闭", icon: "cancel", click: function(){api.close();}}
		],toolbarPosition: "bottom"
		
	});		
	
	var receiptUploadConfig = {
			fileSize : "100mb",	//文件大小限制
			businessId : "",	//业务ID
			buttonId : "procufilesBtn",		//上传按钮ID
			container : "procufilesDIV",	//上传控件容器ID
			title : "选择新文件",	//文件选择框提示
			extName : "doc,docx,dot,dotx,pdf",	//文件扩展名限制
			workItem : "",	//页面多点上传文件标识符号
			fileNum : 1,	//限制上传文件数目
			callback : function(files){	//上传成功后回调函数,实现页面文件显示，交与业务自行后续处理
				addAttachFile(files);
			}
	};
	var receiptUploader= new KHFileUploader(receiptUploadConfig);
	$('#department').autocomplete("employee/basic/searchOrg.do", {
        max: 12,    //列表里的条目数
        minChars: 1,    //自动完成激活之前填入的最小字符
        width: 200,     //提示的宽度，溢出隐藏
        scrollHeight: 300,   //提示的高度，溢出显示滚动条
        scroll: false,   //当结果集大于默认高度时是否使用卷轴显示
        matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
        autoFill: false,    //自动填充
        formatItem: function(row, i, max) {
        //alert(row);
            return row.orgName;
        },
        formatMatch: function(row, i, max) {
            return row.orgName;
        },
        formatResult: function(row) {
            return row.orgName;
        }
    }).result(function(event, row, formatted) {
        $("#departmentId").val(row.orgId);

    });
});
        function directChange(){ 
        	var obj=$("#form1").validate().form();
    	 if(obj){
    		 
    		 //$("#form1").submit();
    	 }else{
    		 return;
    	}} 
        
   	// 将上传的所有文件id写入隐藏框中
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
	function setinput(a){
		//var a=file.name;
		var b=a.substring(a.indexOf("/")+1,a.lastIndexOf("."));
		var c=b.substring(b.indexOf("/")+1,b.lastIndexOf("（")==-1?b.lastIndexOf("("):b.lastIndexOf("（"));
		var d=b.substring(b.indexOf("/")+1,b.lastIndexOf("）")==-1?b.lastIndexOf(")"):b.lastIndexOf("）"));
		top.$.ajax({
            url: "tx/file/getTime.do?implementDate="+d,
            type: "GET",
            dataType:'json',
            async: false,
            success:function (data) {
                if (data) {
//     				if(b==""){
//     					 $.ligerDialog.error("上传文件错误，检查后上传！");
//     				}
                	$("#fileName").val(b);
    				$("#fileId").val(c);
    				var f=data.msg.substring(data.msg.indexOf("/")+1,data.msg.lastIndexOf(" "));
    				$("#implementDate").val(f);
                }
            },
            error:function () {
                $.ligerDialog.alert("出错了!！请重试！");
            }
        });
	}
	//添加附件
	function addAttachFile(files){
//		alert(files);
		if("${param.pageStatus}"=="detail"){
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
				setinput(file.name);
			});
			getUploadFile();
		}
	}
	// 显示附件文件
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
					$("#"+attContainerId).append("<li id='"+file.id+"'>"+
											"<div><a href='fileupload/downloadByFilePath.do?path="+file.filePath+"&fileName="+file.fileName+"'>"+file.fileName+"</a></div>"+
											"</li>");
				});
			}
			//修改
			else if("${param.pageStatus}"=="modify"){
				$.each(files,function(i){
					var file=files[i];
					$("#"+attContainerId).append("<li id='"+file.id+"'>"+
							"<div><a href='fileupload/downloadByFilePath.do?path="+file.filePath+"&fileName="+file.fileName+"'>"+file.fileName+"</a></div>"+
							"<div class='l-icon-close progress' onclick='deleteUploadFile(\""+file.id+"\",\""+file.filePath+"\",this,getUploadFile)'>&nbsp;</div>"+
							"</li>");
					setinput(file.name);
				});
				getUploadFile();
			}
		}
    }
    function chooseOrg(){
        top.$.dialog({
            width: 800,
            height: 450,
            lock: true,
            parent: api,
            title: "选择部门",
            content: 'url:app/common/org_choose.jsp',
            cancel: true,
            ok: function(){
                var p = this.iframe.contentWindow.getSelectedPerson();
                if(!p){
                    top.$.notice("请选择人员！", 3, "k/kui/images/icons/dialog/32X32/hits.png");
                    return false;
                }
                $("#departmentId").val(p.id);
                $("#department").val(p.name);
            }
        });
    }
    </script>
</head>

<body>
<div class="item-tm" id="titleElement">
        <div class="l-page-title">
            <div class="l-page-title-note">提示：列表数据项
                <font color="green">“绿色”</font>此文件正在启用。
            </div>
        </div>
    </div>
	<form id="form1" action="tx/file/save1.do" getAction="tx/file/detail1.do?id=${param.id}">
		<input type="hidden" id="id" name="id"/>
		<input type="hidden" id="status" name="status"/>
		<input type="hidden" id="departmentId" name="departmentId"/>
		<input type="hidden" id="tjyFile" name="tjyFile" value="${param.device_type}"/>
		<fieldset class="l-fieldset">
			<legend class="l-legend">
				<div>
					基础信息
				</div>
			</legend>
			<table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table" id="">
					
				<tr>
					<td class="l-t-td-left">文件编号</td>
					<td class="l-t-td-right"><input id="fileId" name="fileId" value="" type="text" ltype="text"  validate="{required:true,maxlength:32}"/></td>
<%-- 				<%if("${param.device_type}".equals("CTJS")){%> --%>
<!-- 					<td class="l-t-td-left">文件编号</td> -->
<!-- 					<td class="l-t-td-right"><input name="fileId" value="CTJS-" type="text" ltype="text"  validate="{required:true,maxlength:32}" ligerUi="{disabled:true}"/></td> -->
<%-- 				<%}else if("${param.device_type}".equals("CTJC")){ %> --%>
<!-- 					<td class="l-t-td-left">文件编号</td> -->
<!-- 					<td class="l-t-td-right"><input name="fileId" value="CTJC-" type="text" ltype="text"  validate="{required:true,maxlength:32}" ligerUi="{disabled:true}"/></td> -->
<%-- 				<%}else{ %> --%>
<!-- 					<td class="l-t-td-left">文件编号</td> -->
<!-- 					<td class="l-t-td-right"><input name="fileId" value="CTJB-" type="text" ltype="text"  validate="{required:true,maxlength:32}" ligerUi="{disabled:true}"/></td>		 -->
<%-- 				<%}%> --%>
					<td class="l-t-td-left">实施日期</td>
					<td class="l-t-td-right"><input id="implementDate" name="implementDate" type="text" ltype="date" validate="{required:true}"/></td>
				</tr>
				<tr>
					<td class="l-t-td-left">文件名称</td>
					<td class="l-t-td-right" colspan="3"><input id="fileName" name="fileName" type="text" ltype="text" validate="{required:true,maxlength:100}"/></td>
				</tr>
				
			</table>
			</fieldset>
			<fieldset class="l-fieldset">
			<legend class="l-legend">
				<div>体系文件上传</div>
			</legend>
			<table cellpadding="3" cellspacing="0" class="l-detail-table">
				<tr>
					<td class="l-t-td-left">文件</td>
					<td class="l-t-td-right">
						<input name="uploadFiles" type="hidden" id="uploadFiles" validate="{maxlength:1000}" />
						<p id="procufilesDIV">
							<a class="l-button" id="procufilesBtn">
								<span class="l-button-main"><span class="l-button-text">选择文件</span></span>
							</a>
						</p>
				    	<div class="l-upload-ok-list">
							<ul id="procufiles"></ul>
						</div>
					</td>	
				</tr>
			</table>
		</fieldset>
	</form>
	<script type="text/javascript">
	var manager, g;
		$(function() {
			$("#form1").initFormList({
				root : 'list',
				getAction : "tx/file/detailUploads.do?businessId=${param.id}",
				//getActionParam:{},	//取数据时附带的参数，一般只在需要动态取特定值时用到
				actionParam : {
				}, //保存时会在当前表单上附加此数据，如：{fkId:$("#form1>#id")}会把Form1下的id为id的值带上去,可以是一个对象或选择器
				delAction : 'tx/file/deleteUploads.do', //删除数据的action
				delActionParam : {
					id:'id',
					uploadId:'uploadId'
				}, //默认为选择行的id 
				columns : [ {
					display : 'businessId',
					name : 'businessId',
					width : '1%',
					hide : true
				}, {
					display : 'id',
					name : 'id',
					width : '10%',
					hide : true 
				}, {
					display : '路径',
					name : 'filePath',
					width : '10%',
					hide : true 
				},  {
					display : '上传时间',
					name : 'uploadTime',
					width : '15%'
				},  {
					display : '文件名称',
					name : 'fileName',
					width : '65%'
				},{ display: '文件', isSort: true, width: 100, 
					render: function (rowdata, rowindex, value){
						var j = "";
						j="<a href='fileupload/downloadByFilePath.do?path="+rowdata.filePath+"&fileName="+rowdata.fileName+"'>点击查看</a>";
// 						j="<a href='javascript:ck(" + rowdata.id+rowdata.filePath+rowdata.fileName + ")'>点击查看</a> ";
						return j;
	            	}
				},{ display: '操作', isSort: false, width: 50, render: function (rowdata, rowindex, value)
		            {var h = "";//alert(rowdata.id);
							//h +="<a href='tx/file/wjqy.do?id="+rowdata.id+"'>启用</a> ";href='#' 
							h = "<a onclick=\" wjqy('"+rowdata.id+"' )\">启用</a>";
		            return h;}}],
				toolbar:[
				 { text:'保存', click:function(){ 
					 var uploadFiles=$("#uploadFiles").val();
			    		var formData = $("#form1").getValues();
			            $("body").mask("正在保存......");
			           $.ajax({
			               url: "tx/file/save1.do?uploadFiles="+uploadFiles+"&wjid="+"${param.id}",
			               type: "POST",
			               datatype: "json",
			               contentType: "application/json; charset=utf-8",
			               data: $.ligerui.toJSON(formData),
			               success: function (data, stats) {
			                   $("body").unmask();//alert(99);
			                   if (data.success) {
			                   	top.$.notice(data.msg,3);	
			                       top.$.dialog.notice({content:'保存成功！'});
			                       api.data.window.Qm.refreshGrid();//刷新
			                      //ff();
			                       //api.data.window.reLoad();
			                       window.location.reload();//刷新 
			                       //api.close();
			                   }else{
			                       $.ligerDialog.error('提示：保存失败！');
			                       api.data.window.Qm.refreshGrid();//刷新
			                   }
			               },
			               error: function (data,stats) {
			                   $("body").unmask();
			                   $.ligerDialog.error('提示：保存失败！');
			               }
			           });
				 }, icon:'save'},
				 { text:'关闭', click:function(){api.close();}, icon:'delete'}]
				,rowAttrRender : function(rowData, rowid) {
		            var fontColor="black";
		            if(rowData.status=='1'){
		            	fontColor="green";
		            }
		            return "style='color:"+fontColor+"'";
		        }
			});
		});
	function wjqy(rowId){
		$.ajax({
			url:"tx/file/wjqy.do?id="+rowId,
			type: "POST",
			datatype: "json",
			success: function (data) {
			 	if(data){
			 		top.$.notice(data.msg,3);	
			 		api.data.window.Qm.refreshGrid();//刷新
			 		//api.close();
			 		window.location.reload();//刷新 
			 	}else{
			 		 $.ligerDialog.error('提示：启用失败！' );
			 	}
			}
		});
	}
// 		 $.ajax({
//              url:"tx/file/detailUploads.do?businessId="+idd,
//              type: "POST",
//              datatype: "json",
//              success: function (data, stats) {
//           	   alert("${param.id}");
//              }
//          });
	</script>
</body>
</html>