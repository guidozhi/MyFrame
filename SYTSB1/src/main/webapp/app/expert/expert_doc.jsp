<%@page import="com.khnt.utils.StringUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<%
String realPath1 = request.getSession().getServletContext().getRealPath("upload");

//System.out.print(">>>>>>>"+realPath1+"<<<<<<");
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
<!--  -->
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />

<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
<%@include file="/k/kui-base-form.jsp"%>
<style type="text/css">
    .l-icon-exportDoc{background:url('k/kui/images/16/icons/word2.png') no-repeat center;}
    .l-icon-printPreview{background:url('k/kui/images/16/icons/search2.png') no-repeat center;}
    .l-icon-fullScreen{background:url('k/kui/skins/icons/div-drag.gif') no-repeat center;}
</style>
<script src="pub/office/ntkoofficecontrol.js" type="text/javascript"></script>
<script src="pub/office/editor.js" type="text/javascript"></script>
<script type="text/javascript">
	var basepath = "${pageContext.request.contextPath}/";
	var toolBar;//工具条
	$(function(){
		$(".layout").ligerLayout({
			bottomHeight:30,
			space : 0
		});
	});
	
	//文档标签赋值
	function setBookMarkValue1(bookMarkName,inputValue){
		var bkmkObj = TANGER_OCX_OBJ.ActiveDocument.BookMarks(bookMarkName);
		if(!bkmkObj){
			return;
		}
		var saverange = bkmkObj.Range;
		saverange.Text = inputValue;
		TANGER_OCX_OBJ.ActiveDocument.Bookmarks.Add(bookMarkName,saverange);
	}
	
	function initPage(){
		initToolBar();
		createNtkoEditor("editor_container");
		TANGER_OCX_OBJ.SetReadOnly(true);
		TANGER_OCX_OBJ.Menubar = false;
	    TANGER_OCX_OBJ.Statusbar = false;
		TANGER_OCX_OBJ.Toolbars = false;
		
			 TANGER_OCX_OBJ.OpenFromURL($("base").attr("href") + "app/expert/temple/meeting_expert.doc");
		
		setValues();
		
		
	}
	
  	function initToolBar(){
    	var saveBtn;
    	var closeBtn;
    	var printBtn;
    	var printPreviewBtn;
    	var setLayoutBtn;
    	var fullScreenBtn;
    	var subBtn;
    	var backBtn;
    
		closeBtn={
				id: "close",
				text: "关闭",
				icon:"close",
				click: function(){
				    api.data.window.Qm.refreshGrid();
				    api.close();
					return true;
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
		 printPreviewBtn={
				id: "printPreview",
				text: "打印预览",
				icon:"preview",
				click: function(){
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
		 fullScreenBtn={
				id: "fullScreen",
				text: "全屏",
				icon:"provide",
				click: function(){
					fullScreen();
					return true;
				}
		 };
		
	
		var itemArr=new Array();
		
			itemArr.push(printBtn);
			itemArr.push(printPreviewBtn);
		   	itemArr.push(fullScreenBtn);
		   	itemArr.push(closeBtn);
		   	//itemArr.push(printBtn);
		   	//itemArr.push(backBtn);
		   	//itemArr.push(subBtn);
		   	//itemArr.push(printPreviewBtn);
		
	    toolBar=$("#toolbar").ligerButton({
			items:itemArr
		});
    }
	function confirmCurrentDocument(){
		
	}
 
  	
  	
  	
  	function setValues(){
  		
  		//报告录入之前插入基础数据
  			$.post("expertRecordAction/detail.do?id=${param.id}",function(res){
				if(res.success){
					TANGER_OCX_OBJ.SetReadOnly(false,"");
					var doc = TANGER_OCX_OBJ.ActiveDocument;
					var bookMarks = TANGER_OCX_OBJ.ActiveDocument.BookMarks;
					var l = bookMarks.count;
					
					for (var i=1;i<=l;i++) {
						//书签用__号把序号隔开
							var bookMarkName = bookMarks(i).Name;
							var sign = bookMarkName;
							//报告书编号
							
							if("expert_result"==sign){
								setBookMarkValue1(bookMarkName,api.data.emp_name);
							}else if(sign=="meeting_date"){
								if(res.data[sign]!=null&&res.data[sign]!=undefined&&res.data[sign]!=""){

									setBookMarkValue1(bookMarkName,res.data[sign].substring(0,10));
									
								}
							}else{
								if(res.data[sign]!=null&&res.data[sign]!=undefined&&res.data[sign]!=""){

									setBookMarkValue1(bookMarkName,res.data[sign]);
									
								}
							}
					}
				}
  			})
  		
  		
  		
  		
  		
  	}
  	
  	
  	//插入签名
  	function FindKeyword(keyword,sign_id)
	{
		var l = keyword.length;
		var doc = TANGER_OCX_OBJ.ActiveDocument;
		var sel = doc.Application.Selection;
		sel.HomeKey(6);
		
		var result = true;
		var i = 1;
		var pids = [];
		var eId = sign_id.split(",");
		for (var k = 0; k < eId.length; k++) {
			$.ajax({
				url : "inspectionInfoAction/getSignPictureById.do",
				type : "POST",
				async:false,
				datatype : "json",
				data : {
					id:eId[k]
				},
				success : function(res) {
					if(res.pictureID!=null){
						pids[pids.length]=res.pictureID;
					}
				}
			})
	
	}
		
		do
		{
			
			
			
			
			result = sel.Find.Execute (keyword);
			if (result)
			{
				sel.SetRange(sel.End-l,sel.End);
				doc.Bookmarks.Add("bk_"+keyword +"_"+ i, doc.Range(sel.End-l,sel.End));
				for (var j= 0; j < pids.length; j++) {
					try {
						 TANGER_OCX_OBJ.AddPicFromURL($("base").attr("href")+"fileupload/downloadByObjId.do?id="+pids[j], 
									false, 0, 0,1,100, 1);
					} catch (e) {
						// TODO: handle exception
					}
					
				}
				 
				//TANGER_OCX_OBJ.SetBookmarkValue("bk_"+keyword+"_"+ i,"第" + i + "个");
				i = i + 1;
			}				
		}
		while (result);
	}
	//插入文字
	function FindKeywordTime(keyword,times)
	{
		var l = keyword.length;
		var doc = TANGER_OCX_OBJ.ActiveDocument;
		var sel = doc.Application.Selection;
		sel.HomeKey(6);
		
		var result = true;
		var i = 1;
		do
		{
			result = sel.Find.Execute (keyword);
			if (result)
			{
				sel.SetRange(sel.End-l,sel.End);
				doc.Bookmarks.Add("bk_"+keyword +"_"+ i, doc.Range(sel.End-l,sel.End));
				TANGER_OCX_OBJ.SetBookmarkValue("bk_"+keyword+"_"+ i,times);
				i = i + 1;
			}				
		}
		while (result);
	}
	
	
	function setValueOld(){
		$.post("inspectionInfoAction/getSignPicture.do",{id:api.data.id},function(res){
			if(res.success){
				var doc = TANGER_OCX_OBJ.ActiveDocument;
				var bookMarks = TANGER_OCX_OBJ.ActiveDocument.BookMarks;
				var l = bookMarks.count;
				for (var i=1;i<=l;i++) {
					//书签用#号把序号隔开
						var bookMarkName = bookMarks(i).Name;
						var sign = bookMarkName.split("__")[0];
						if(sign=="enterTime"||sign=="enter_time"){
							//检验日期
							if(res["enter_time"]!=null&&res["enter_time"]!=undefined&&res["enter_time"]!=""){
								setBookMarkValue1(bookMarkName,res["enter_time"]);
							}
							
							
						}else if(sign=="inspectDate"||sign=="inspect_date"){
							//检验日期
							if(res["inspect_date"]!=null&&res["inspect_date"]!=undefined&&res["inspect_date"]!=""){
								setBookMarkValue1(bookMarkName,res["inspect_date"]);
							}
							
							
						}else if(sign=="confirmTime"||sign=="confirm_time"){
							//审核时间
							if(res["confirm_time"]!=null&&res["confirm_time"]!=undefined&&res["confirm_time"]!=""){
								setBookMarkValue1(bookMarkName,res["confirm_time"]);
							}
							
							
						}else if(sign=="auditTime"||sign=="audit_time"){
							//审核时间
							if(res["audit_time"]!=null&&res["audit_time"]!=undefined&&res["audit_time"]!=""){
								setBookMarkValue1(bookMarkName,res["audit_time"]);
							}
							
							
						}else if(sign=="signTime"||sign=="sign_time"){
							//签发时间
							if(res["sign_time"]!=null&&res["sign_time"]!=undefined&&res["sign_time"]!=""){
								setBookMarkValue1(bookMarkName,res["sign_time"]);
							}
							
							
						}else if(sign!="check_op"){
							//参检人员
							if(res[sign]!=null&&res[sign]!=undefined&&res[sign]!=""){
								
								TANGER_OCX_OBJ.ActiveDocument.Application.Selection.GoTo(-1, 0, 0, bookMarkName);
								var urls = res[sign].split("\\");
								var url = "";
								for (var j = 0; j < urls.length; j++) {
									url = url+"/"+urls[j];
								}
									 TANGER_OCX_OBJ.AddPicFromURL($("base").attr("href")+"upload"+url, 
												false, 0, 0,1,100, 1) 
								
							}
						}else{
							var flag = false;
							//处理参检人员问题
							if(res[sign]!=null&&res[sign]!=undefined&&res[sign]!=""){
								var checkList= res[sign]; 
								for (var n=0;n<checkList.length;n++) {
									//处理参检人员和项目负责人可能重复的问题
									
									if(checkList[n].check_op!==""&&checkList[n].check_op!=res["item_op"]){
										TANGER_OCX_OBJ.ActiveDocument.Application.Selection.GoTo(-1, 0, 0, bookMarkName);
										var urls = checkList[n].check_op.split("\\");
										var url = "";
										for (var j = 0; j < urls.length; j++) {
											url = url+"/"+urls[j];
										}
											 TANGER_OCX_OBJ.AddPicFromURL($("base").attr("href")+"upload"+url, 
														false, 0, 0,1,100, 1) 
										
										 
										 
									}else if(checkList[n].check_op!=""&&checkList[n].check_op==res["item_op"]){
										flag = true;
									}
								}
								//在参检人员处插入
								if(res["item_op"]!=null&&res[sign]!=undefined&&res["item_op"]!=""&&flag){
									TANGER_OCX_OBJ.ActiveDocument.Application.Selection.GoTo(-1, 0, 0, bookMarkName);
									var urls = res["item_op"].split("\\");
									var url = "";
									for (var j = 0; j < urls.length; j++) {
										url = url+"/"+urls[j];
									}
										 TANGER_OCX_OBJ.AddPicFromURL($("base").attr("href")+"upload"+url, 
													false, 0, 0,1,100, 1) 
								
								}
							}
							
							
						}
						
					
				
				}
			}
		})
	}
	
</script>
</head>
<body onload="initPage();">
<div class="layout">
	<div position="center" id="editor_container" style="width:100%;height:100%"></div>
	<div position="bottom" style="height: 50px;">
		<div class="div1" id="toolbar" style="padding:1px;text-align:right;"></div>
	</div>
</div>
<iframe id="export_doc_iframe" style="display:none;">
</iframe>
</body>
</html>