<%@page import="com.khnt.utils.StringUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	String realPath1 = request.getSession().getServletContext().getRealPath("upload");
	String[] id = realPath1.split("\\\\");
	String realPath="";
	for(int i=0;i<id.length;i++){
		if(StringUtil.isNotEmpty(realPath)){
		realPath = realPath +"/"+id[i];
		}else{
			realPath = id[0];
		}
	}
%>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
<%@include file="/k/kui-base-form.jsp"%>
<style type="text/css">
    .l-icon-exportDoc{background:url('k/kui/images/16/icons/word2.png') no-repeat center;}
    .l-icon-printPreview{background:url('k/kui/images/16/icons/search2.png') no-repeat center;}
    .l-icon-fullScreen{background:url('k/kui/images/16/icons/div-drag.gif') no-repeat center;}
</style>
<script src="app/pub/office/ntkoofficecontrol.js" type="text/javascript"></script>
<script src="app/pub/office/editor.js" type="text/javascript"></script>
<script type="text/javascript">
	var basepath = "${pageContext.request.contextPath}/";
	var dates = api.data.date.split(",");
	var report_sns = api.data.report_sns.split(",");
	var toolBar;//工具条
	$(function(){
		$(".layout").ligerLayout({
			bottomHeight:30,
			space : 0
		});
		
		//加载文档编辑器
		//createNtkoEditor("editor","<sec:authentication property="principal.name" htmlEscape="false" />");	  
	});
	
	function initPage(){
		initToolBar();
		createNtkoEditor("editor_container");
		TANGER_OCX_OBJ.SetReadOnly(true);
		TANGER_OCX_OBJ.Menubar = false;
		TANGER_OCX_OBJ.Statusbar = false;
		TANGER_OCX_OBJ.Toolbars = false;
		 //加载pdf插件
		addPdfPlugin();
		var printcopies = api.data.printcopies;
		var url = "D:/TEMP/"+"${param.report_sn}"+".pdf";

		for (var i = 0; i < report_sns.length; i++) {
			var d_url = $("base").attr("href") + "upload/"+dates[i]+"/"+report_sns[i]+"/"+report_sns[i]+".pdf" ;
			
			TANGER_OCX_OBJ.OpenFromURL(d_url);
			TANGER_OCX_OBJ.SetReadOnly(false,"");
			for (var j = 0; j < printcopies-0; j++) {
				TANGER_OCX_OBJ.printout(false);
			}
			if(api.data.type=="1"){	
				savePrint();	
				savePrintLog(printcopies);
			}else if(api.data.type=="3"){		
				savePrintLog(printcopies);
			}else{
				
			} 
			if(i ==( report_sns.length-1)){
				top.$.dialog.notice({content:'提示：报告已经全部发送打印！'});
				//$.ligerDialog.alert('提示：报告已经全部发送打印！');
				api.close();
			}
		}
		
		/* alert(d_url);
		TANGER_OCX_OBJ.OpenFromURL(d_url);
		TANGER_OCX_OBJ.SetReadOnly(false,"");
		if ("${param.print}" == "true") {
			for (var i = 0; i < printcopies-0; i++) {
				TANGER_OCX_OBJ.printout(false);
			}
			if(api.data.type=="1"){	
				savePrint();	
				savePrintLog(printcopies);
			}else if(api.data.type=="3"){		
				savePrintLog(printcopies);
			}else{
				api.close();
			}
		} */
		TANGER_OCX_OBJ.SetReadOnly(true,"");
	}
	
	 function savePrint(){
		var id = "${param.id}";
		url = "department/basic/flow_print.do?infoId=" + id + "&acc_id="
				+ api.data.acc_id + "&flow_num=" + api.data.flow_num;
		$.ajax({
			url : url,
			type : "POST",
			async:false,
			dataType : "json",
			success : function(resp) {
				//api.close();
			},
			error : function(resp) {
				$.ligerDialog.error('提示：' + resp.msg);
			}
		});
	}
	
	function savePrintLog(printcopies){
		var id = "${param.id}";
		url = "report/query/savePrintLog.do?id=" + id +"&print_type="+api.data.print_type+"&print_count="+printcopies+"&print_remark="+encodeURI(encodeURI(api.data.print_remark));
		$.ajax({
			url : url,
			type : "POST",
			async:false,
			dataType : "json",
			success : function(resp) {
				api.close();
				//api.data.window.api.close();
			},
			error : function(resp) {
				$.ligerDialog.error('提示：' + resp.msg);
			}
		});
	}

	function initToolBar() {
		var closeBtn;
		var printBtn;
		var printPreviewBtn;
		var fullScreenBtn;
		printBtn = {
			id : "print",
			text : "打印",
			icon : "print",
			click : function() {
				TANGER_OCX_OBJ.printout(false);
				
				/* if(api.data.type=="1"){	
					savePrintLog(api.data.printcopies);
					savePrint();
				}else if(api.data.type=="3"){		
					savePrintLog(api.data.printcopies);
				}else{
					api.close();
				} */
			}
		};
		printPreviewBtn = {
			id : "printPreview",
			text : "打印预览",
			icon : "preview",
			click : function() {
				printPreview();
				return true;
			}
		};
		fullScreenBtn = {
			id : "fullScreen",
			text : "全屏",
			icon : "provide",
			click : function() {
				fullScreen();
				return true;
			}
		};
		closeBtn = {
			id : "closeScreen",
			text : "关闭",
			icon : "provide",
			click : function() {
				api.close();
				return true;
			}
		};
		var itemArr = new Array();
		/* itemArr.push(printBtn);
		//itemArr.push(subBtn);
		itemArr.push(fullScreenBtn);
		itemArr.push(printPreviewBtn); 
		itemArr.push(closeBtn);*/
		toolBar = $("#toolbar").ligerButton({
			items : itemArr
		});
	}

	function saveCurrentDocument() {
	}
</script>
</head>
<body onload="initPage();">
<div class="layout">
	<div position="center" id="editor_container" style="width:100%;height:100%"></div>
	<div position="bottom">
		<div class="div1" id="toolbar" style="padding:1px;text-align:right;"></div>
	</div>
</div>
<iframe id="export_doc_iframe" style="display:none;">
</iframe>
</body>
</html>