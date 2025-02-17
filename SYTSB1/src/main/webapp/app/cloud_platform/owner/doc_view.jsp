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
<%
	String realPath1 = request.getSession().getServletContext().getRealPath("upload");
	CurrentSessionUser user = SecurityUtil.getSecurityUser();
	String now_date = new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
	System.out.print(">>>>>>>" + realPath1 + "<<<<<<");
	String[] id = realPath1.split("\\\\");
	String realPath = "";
	for (int i = 0; i < id.length; i++) {
		if (StringUtil.isNotEmpty(realPath)) {
			realPath = realPath + "/" + id[i];
		} else {
			realPath = id[0];
		}
	}
%>
<!--  -->
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
	background: url('k/kui/images/16/icons/div-drag.gif') no-repeat center;
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
			/* alert($("base").attr("href")
					+ "fileupload2/downloadCompress.do?id="+api.data.id+"&proportion=0"
					) */
			TANGER_OCX_OBJ.OpenFromURL($("base").attr("href")
					+ "fileupload2/downloadCompress.do?id="+api.data.id+"&proportion=0"
							);
		}
		setInterval('refreshQuery()',1000); 
	}

	function refreshQuery(){
			try{
				TANGER_OCX_OBJ.ActiveDocument.SetViewPercent(130);
		}catch(e){
		}
		try{
			TANGER_OCX_OBJ.ActiveDocument.ActiveWindow.ActivePane.View.Zoom.Percentage = 130
			}catch(e){
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
		/**
		setLayoutBtn={
			id: "printSet",
			text: "页面设置",
			icon:"setting",
			click: function(){
				setLayout();
				return true;
			}
		};**/
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

		//itemArr.push(setLayoutBtn);
		 if("${param.status}"!="detail"){
		// itemArr.push(saveBtn);
		} 
		itemArr.push(printBtn);
		itemArr.push(fullScreenBtn);
		itemArr.push(printPreviewBtn);
		itemArr.push(closeBtn);
		toolBar = $("#toolbar").ligerButton({
			items : itemArr
		});
	}

</script>


<!-- <script language="JScript" for="TANGER_OCX" event="AfterOpenFromURL(doc)"> 
 				doc.ActiveWindow.ActivePane.View.Zoom.Percentage = 10
</script> -->
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