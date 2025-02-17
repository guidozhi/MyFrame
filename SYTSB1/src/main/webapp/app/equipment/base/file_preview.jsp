<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.utils.StringUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />

<base
	href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
<%@include file="/k/kui-base-form.jsp"%>
<style type="text/css">
.l-icon-exportDoc {
	background: url('k/kui/images/16/icons/word2.png') no-repeat center;
}

.l-icon-printPreview {
	background: url('k/kui/images/16/icons/search2.png') no-repeat center;
}

.l-icon-fullScreen {
	background: url('k/kui/skins/icons/div-drag.gif') no-repeat center;
}
</style>
<script src="pub/office/ntkoofficecontrol.js" type="text/javascript"></script>
<script src="pub/office/editor.js" type="text/javascript"></script>
<script src="app/tzsb/inspection/flow/insing/hall/common/common.js" type="text/javascript"></script>
<script type="text/javascript">
	var basepath = "${pageContext.request.contextPath}/";
	var toolBar;//工具条
	var device_sort="";
	var device_sort_code="";
	$(function() {
		$(".layout").ligerLayout({
			bottomHeight : 30,
			space : 0
		});
	});

	//文档标签赋值
	function setBookMarkValue1(bookMarkName, inputValue) {
		var bkmkObj = TANGER_OCX_OBJ.ActiveDocument.BookMarks(bookMarkName);
		if (!bkmkObj) {
			return;
		}
		var saverange = bkmkObj.Range;
		saverange.Text = inputValue;
		TANGER_OCX_OBJ.ActiveDocument.Bookmarks.Add(bookMarkName, saverange);
	}

	function initPage() {
		initToolBar();
		createNtkoEditor("editor_container");
		   //加载pdf插件
		addPdfPlugin();
		   
		TANGER_OCX_OBJ.SetReadOnly(true);
		TANGER_OCX_OBJ.Menubar = false;
		TANGER_OCX_OBJ.Statusbar = false;
		TANGER_OCX_OBJ.Toolbars = false;
		
		if(api.data.id!=null){
			TANGER_OCX_OBJ.OpenFromURL($("base").attr("href")
					+ "fileupload/downloadCompress.do?id="+api.data.id+"&proportion=0"
							);
		}
	}

	function initToolBar() {
		var saveBtn;
		var closeBtn;
		var printBtn;
		var printPreviewBtn;
		var setLayoutBtn;
		var fullScreenBtn;
		var backBtn;
		var downloadBtn;
		downloadBtn = {
				id : "download",
				text : "下载",
				icon : "picture-save",
				click : function() {
					window.open($("base").attr("href")
							+ "fileupload/downloadCompress.do?id="+api.data.id+"&proportion=0");
					return true;
				}
			};
		
		closeBtn = {
			id : "close",
			text : "关闭",
			icon : "close",
			click : function() {
				api.close();
				return true;
			}
		};

		printBtn = {
			id : "print",
			text : "打印",
			icon : "print",
			click : function() {
				doPrint();
				//savePrint();
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
		 saveBtn = {
					id : "save",
					text : "保存",
					icon:"save",
					click : function() {
						saveCurrentDocument();
						return false;
					}
		}; 
		var itemArr = new Array();
		 if("${param.status}"!="detail"){
		} 
		itemArr.push(printBtn);
		itemArr.push(printPreviewBtn);
		itemArr.push(downloadBtn);
		itemArr.push(fullScreenBtn);
		itemArr.push(closeBtn);
		toolBar = $("#toolbar").ligerButton({
			items : itemArr
		});
	}

</script>
</head>
<body onload="initPage();">
	<div class="layout">
		<div position="center" id="editor_container"
			style="width: 100%; height: 100%"></div>
		<div position="bottom" style="height: 50px;">
			<div class="div1" id="toolbar"
				style="padding: 1px; text-align: right;"></div>
		</div>
	</div>
	<iframe id="export_doc_iframe" style="display: none;"> </iframe>
</body>
</html>