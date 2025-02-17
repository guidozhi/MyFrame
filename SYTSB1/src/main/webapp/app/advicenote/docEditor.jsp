<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="util.TS_Util" %>
<%@ page import="com.khnt.base.Factory" %>
<%@ page import="java.sql.Connection" %>
<%@taglib uri="http://bpm.khnt.com" prefix="bpm"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<% 
Connection conn = Factory.getDB().getConnetion(); 
String num = TS_Util.getAdviceNoteSn(1, conn);
Factory.getDB().freeConnetion(conn);//释放连接
%>
<head>
<%@include file="/k/kui-base-form.jsp"%>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

<script src="app/pub/office/js/editor.js" type="text/javascript"></script>
<script src="app/pub/seal/js/seal.js" type="text/javascript"></script>
<script type="text/javascript">
	var basepath = "${pageContext.request.contextPath}/";
	var toolBar;//工具条
	var beanData = api.data.bean;//父窗口的数据
	var bumfId=beanData == null?"": beanData.id;

	$(function(){
		var height = $(window).height()-$('.toolbar-tm').height();
		$("#scroll-tm").css({height:height});
	
		$(".layout").ligerLayout({
			bottomHeight:30,
			space : 0
		});
	});
	
	function initPage(){
		initToolBar();
		createNtkoEditor("editor_container");
		//远程加载文档
		loadBumfDoc();
		initDocView();
	}
	
	
	function initDocView(){
		if("${param.status}"=="detail"){
			initView("view");
		}
	}
	
	function initToolBar(){
	    	var saveBtn;
	    	var closeBtn;
	    	var checkBtn;
	    	var printBtn;
	 		saveBtn = {
	 					id : "save",
	 					text : "保存",
	 					icon:"save",
	 					click : function() {
	 						saveBumfDraft();
	 						return false;
	 					}
	 				};
			 closeBtn={
					id: "close",
					text: "关闭",
					icon:"close",
					click: function(){
					    api.close();
						return true;
					}
			 };
			 checkBtn = {
	 					id : "check",
	 					text : "审核",
	 					icon:"check",
	 					click : function() {
	 						check(beanData);
	 						return false;
	 					}
	 				};
	 		printBtn={
					id: "print",
					text: "打印",
					icon:"print",
					click: function(){						
						doPrint();
					}
			 };
			var itemArr=new Array();
			 //有提交
			if("${param.status}"!="detail"){
				if("${param.status}"=="check"){
					itemArr.push(checkBtn);
				}else if("${param.status}"=="print"){
					itemArr.push(printBtn);
				}else {
					itemArr.push(saveBtn);
				}
			}
			itemArr.push(closeBtn);
			toolBar=$("#toolbar").ligerButton({
				items:itemArr
			});
	    }
	
	//加载正文
	function loadBumfDoc(){
		TANGER_OCX_OBJ.ToolBars=true;
		//$.ligerDialog.alert(${param.type}==1);
		if (${empty param.docId}) {
			if(${param.type}==1){
				TANGER_OCX_OBJ.OpenFromURL("/app/advicenote/025特种设备安装安全监督验工作联络单.doc");
				setBookMarkValue("<%=num%>","ls_tzsb_azjjgzlld");
			}else if(${param.type}==2){
				TANGER_OCX_OBJ.OpenFromURL("/app/advicenote/026特种设备安装安全监督检验工作意见通知书.doc");
				setBookMarkValue("<%=num%>","ls_tzsb_azjjgzyjtzs");
			}else if(${param.type}==3){
				TANGER_OCX_OBJ.OpenFromURL("/app/advicenote/027压力管道安装监督检验意见通知书.doc");
				setBookMarkValue("<%=num%>","ls_ylgd_azjjyjtzs");
			}else if(${param.type}==4){
				TANGER_OCX_OBJ.OpenFromURL("/app/advicenote/030特种设备定期检验通知书.doc");
				setBookMarkValue("<%=num%>","ls_tzsb_dqjytzs");
			}else if(${param.type}==5){
				TANGER_OCX_OBJ.OpenFromURL("/app/advicenote/040特种设备(定期)检验意见通知书(1).doc");
				setBookMarkValue("<%=num%>","ls_tzsb_dqjyyjtzs1");
			}else if(${param.type}==6){
				TANGER_OCX_OBJ.OpenFromURL("/app/advicenote/041特种设备(定期)检验意见通知书(2).doc");
				setBookMarkValue("<%=num%>","ls_tzsb_dqjyyjtzs2");
			}else if(${param.type}==7){
				TANGER_OCX_OBJ.OpenFromURL("/app/advicenote/047安全阀校验意见通知书2010.doc");
				setBookMarkValue("<%=num%>","ls_aqf_jyyjtzs2010");
			}else{
				TANGER_OCX_OBJ.OpenFromURL("/app/advicenote/templete.doc");
			}
		}else {
			TANGER_OCX_OBJ.OpenFromURL("/fileupload/downloadByObjId.do?id=${param.docId}");
		}
	}

	//保存正文isClose:是否关闭窗口并提示保存成功
	function saveBumfDraft(isClose) {
		//正文已经存在时，先删除正文表，然后在上传
		var title=encodeURI(beanData.title);
		var response = TANGER_OCX_OBJ.SaveToURL("${pageContext.request.contextPath}/fileupload/fileUp.do?businessId="+bumfId+"&fileId=${param.docId}","docFile", "", title+".doc");
		var data = $.parseJSON(response);
		if(data.success){
			//将附件id返回到文档基本信息页面
			if ("${param.id}" == ""){
				api.data.pwindow.editorCallback(data.data.id);
			}
			top.$.notice("文件保存成功！");
			api.close();
		}
		else{
			$.ligerDialog.alert("文件保存失败！");
		}
	}
	
	function showBB(){
		$("#sssss").show();
	}
	
	function check(id){
		$('#sssss').hide();
		top.$.dialog({
			width : 500, 
			height : 200, 
			lock : true, 
			title:"通知书审核",
			parent:api,
			content: 'url:app/advicenote/advicenote_check.jsp?id='+id,
			data : {"window" : window}
		});
	}
</script>
</head>
<body onload="initPage();">
<div class="scroll-tm" style="overflow: hidden" id="sssss"> 
<div class="layout">
    <div id="seal_container"></div>
	<div position="center" id="editor_container" style="width:100%;height:100%"></div>
	<div position="bottom" style="height: 50px;">
		<div class="div1" id="toolbar" style="padding:1px;text-align:right;"></div>
	</div>
</div>
<iframe id="export_doc_iframe" style="display:none;">
</iframe>
</div>
</body>
</html>