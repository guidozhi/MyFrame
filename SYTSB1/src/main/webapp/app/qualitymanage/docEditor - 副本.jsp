<%@page import="com.lsts.report.bean.SysOrg"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="util.TS_Util" %>
<%@ page import="com.khnt.base.Factory" %>
<%@ page import="java.sql.Connection" %>
<%@taglib uri="http://bpm.khnt.com" prefix="bpm"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/k/kui-base-form.jsp"%>
<%
	String type=request.getParameter("type");
	System.out.println(type);
%>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
<script src="app/pub/office/js/editor.js" type="text/javascript"></script>
<script src="app/pub/seal/js/seal.js" type="text/javascript"></script>
<script type="text/javascript">
	var basepath = "${pageContext.request.contextPath}/";
	var toolBar;//工具条
	var beanData = api.data.id;//父窗口的数据
	var identifier = api.data.identifier;//父窗口的数据
	var com_name = api.data.com_name;//父窗口的数据
	var item1 = api.data.item1;//父窗口的数据
	var item2 = api.data.item2;//父窗口的数据
	var item3 = api.data.item3;//父窗口的数据
	var content = api.data.content;//父窗口的数据
	var inspector = api.data.inspector;//父窗口的数据
	var inspector_date = api.data.inspector_date;//父窗口的数据
	var type = api.data.type;//父窗口的数据
	/* 意见通知书中的额外数据 */
	var inspector_people = api.data.inspector_people;//父窗口的数据
	var inspector_people_date = api.data.inspector_people_date;//父窗口的数据

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
		//initSeal("editor_container");//初始化印章参数。用户提示安装控件
		//远程加载文档
		loadBumfDoc();
		initDocView();
	}
	
	function initDocView(){
		if("${param.pageStatus}"=="detail"){
			TANGER_OCX_OBJ.SetReadOnly(true);
			TANGER_OCX_OBJ.Statusbar = false;
			TANGER_OCX_OBJ.Toolbars = false;
		}
	}
	
	function initToolBar(){
    	var closeBtn;
    	var sealBtn;
    	var checkBtn;
    	var printBtn;
		 closeBtn={
				id: "close",
				text: "关闭",
				icon:"close",
				click: function(){
				    api.close();
				    api.data.window.Qm.refreshGrid();
					return true;
				}
		 };
		 sealBtn={
					id: "seal",
					text: "加盖印章",
					icon:"seal",
					click: function(){
						var wordObj = document.getElementById("TANGER_OCX_OBJ");
						insertWordSeal("监检机构检验专用章", wordObj.ActiveDocument, "", false, null);
						/* gz(pdfPath,report_sn,signPage,sealPstX,sealPstY,certPwd,certPath,sealXmlPath);*/
						/* gz("D:\Servers\SCSEI_WEB\webapps\ROOT\app\qualitymanage\02opinionNote.doc","02opinionNote","1","1","1","1234","D:/biceng/certs/JYZY.pfx","D:/biceng/seals/JYZY.xml"); */
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
					if('liaison'=='<%=type%>'){
						top.$.ajax({
				             url: "QualityLiaisonAction/doPrint.do?id=" + beanData,
				             type: "GET",
				             dataType:'json',
				             async: false,
				             success:function(resp) {
									if (resp.success) {
									} else {
										$.ligerDialog.error(resp.msg);
									}
								}
				         });
					}else if('note'=='<%=type%>'){
						top.$.ajax({
				             url: "QualityNoteAction/doPrint.do?id=" + beanData,
				             type: "GET",
				             dataType:'json',
				             async: false,
				             success:function(resp) {
									if (resp.success) {
									} else {
										$.ligerDialog.error(resp.msg);
									}
								}
				         });
					}
					doPrint();
				}
		 };
		var itemArr=new Array();
		if("${param.op_type}"=="print"){
			itemArr.push(printBtn);
		}else if("${param.op_type}"=="check"){
			itemArr.push(checkBtn);
		}else if("${param.op_type}"=="seal"){
			if(type=="特种设备监督检验意见通知书"){
				itemArr.push(sealBtn);
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
		if(beanData != null){
			if(type=="特种设备监督检验联络单"){
				TANGER_OCX_OBJ.OpenFromURL("${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/app/qualitymanage/01liaison.doc");
				setBookMarkValue1("identifier",api.data.identifier);
				setBookMarkValue1("com_name",api.data.com_name);
				setBookMarkValue1("item1",api.data.item1);
				setBookMarkValue1("item2",api.data.item2);
				setBookMarkValue1("item3",api.data.item3);
				setBookMarkValue1("content",api.data.content);
				setBookMarkValue1("inspector",api.data.inspector);
				setBookMarkValue1("inspector_date",api.data.inspector_date);
			}else if(type=="特种设备监督检验意见通知书"){
				TANGER_OCX_OBJ.OpenFromURL("${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/app/qualitymanage/02opinionNote.doc");
				setBookMarkValue1("identifier",api.data.identifier);
				setBookMarkValue1("com_name",api.data.com_name);
				setBookMarkValue1("item1",api.data.item1);
				setBookMarkValue1("item2",api.data.item2);
				setBookMarkValue1("item3",api.data.item3);
				setBookMarkValue1("content",api.data.content);
				setBookMarkValue1("inspector",api.data.inspector);
				setBookMarkValue1("inspector_date",api.data.inspector_date);
				setBookMarkValue1("inspector_people",api.data.inspector);
				setBookMarkValue1("inspector_people_date",api.data.inspector_date);
			}else{
				TANGER_OCX_OBJ.OpenFromURL("${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/app/qualitymanage/templete.doc");
			}
		}else{
			TANGER_OCX_OBJ.OpenFromURL("${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/app/qualitymanage/templete.doc");
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
			content: 'url:app/qualitymanage/inspection_note_check.jsp?id='+id+'&type='+"<%=type%>",
			data : {"window" : window}
		});
	}
	function gz(pdfPath,report_sn,signPage,sealPstX,sealPstY,certPwd,certPath,sealXmlPath){
		//初始化、指定处理函数、发送请求的函数
		if (window.XMLHttpRequest) //Mozila
		{
			http_request = new XMLHttpRequest();
		} else if (window.ActiveXobject) //IE
		{
		   try
		   {
				 http_request = new ActiveXObject("Msxml2.XMLHTTP");
		   }
		   catch (e)
		   {
			   try{
					 http_request = new ActiveXObject("Microsoft.XMLHTTP");
				   }
				catch (e) { }
		   }
		}
		 
		if (!http_request)  // 异常，创建对象实例失败
		{
		   alert("不能创建XMLHttpRequest实例！！");
		   return false;
		}
		alert(http_request);
		// 指定当服务器返回信息时客户端的处理方式
		http_request.onreadystatechange = processRequest;
		var url ="${pageContext.request.scheme}://${pageContext.request.serverName}:8081/gxBatchSealing/services/sign.jsp"; 
		//var url ="http://localhost:8081/gxBatchSealing/services/sign.jsp"; 
		//var url ="http://jx.scsei.org.cn:8081/gxBatchSealing/services/sign.jsp"; 
		var sendContent = "<root><certPwd>"+certPwd+"</certPwd>"+
							"<certPath>"+certPath+"</certPath>"+
							"<sealXmlPath>"+sealXmlPath+"</sealXmlPath>"+
							"<pdfPath>"+pdfPath+"</pdfPath>"+
							"<signPage>"+signPage+"</signPage>"+
							"<sealPstX>"+sealPstX+"</sealPstX>"+
							"<sealPstY>"+sealPstY+"</sealPstY>"+
							"</root>";
		http_request.open("POST",url,false);
		http_request.send(sendContent);
		alert(sendContent);
	}
	function processRequest()
    {
		//alert("判断对象状态-----------"+http_request.readyState);
        if (http_request.readyState == 4) // 判断对象状态
        {
           if (http_request.status == 200)  // 请求结果已经成功返回
           {
        	   /* alert(1);
        	   $.post("inspectionInfo/basic/expPdfFlag.do",{"date":day,"id":api.data.id},function(res){
        		   if(res.success){
        			   alert("盖章并上传成功！");  
        		   }
        	   }) */
        	   
             //alert(http_request.responseText);
			  //document.getElementById("getTxt").value=http_request.responseText;
           }
           else  //页面不正常
           {
              alert("你请求的页面不正常");
           }
        }
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