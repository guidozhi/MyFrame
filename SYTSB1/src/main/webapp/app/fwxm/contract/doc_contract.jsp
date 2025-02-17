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
		TANGER_OCX_OBJ.SetReadOnly(true);
		TANGER_OCX_OBJ.Menubar = false;
		TANGER_OCX_OBJ.Statusbar = false;
		TANGER_OCX_OBJ.Toolbars = false;
		if("${param.contract_type}"=="2"){
			//分包合同
			TANGER_OCX_OBJ.OpenFromURL($("base").attr("href")
					+ "app/fwxm/contract/temple/FBHT.doc");
		}else {
			//普通合同
			TANGER_OCX_OBJ.OpenFromURL($("base").attr("href")
					+ "app/fwxm/contract/temple/PTHT.doc"
							);
		}
			
			if("${param.type}"=="set"){
				var doc = TANGER_OCX_OBJ.ActiveDocument;
				setValues(doc,"${param.contract_type}");
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
		/*  if("${param.status}"!="detail"){
		 itemArr.push(saveBtn);
		}  */
		itemArr.push(printBtn);
		itemArr.push(fullScreenBtn);
		itemArr.push(printPreviewBtn);
		itemArr.push(closeBtn);
		toolBar = $("#toolbar").ligerButton({
			items : itemArr
		});
	}

	function saveCurrentDocument() {
		var case_id = api.data.case_id;
		var device_id = api.data.device_id;
		var report_sn = api.data.report_sn;
		var response = TANGER_OCX_OBJ
		.SaveToURL(
				"${pageContext.request.contextPath}/contractAction/saveContractWord.do?net_id=${param.id}&id="+api.data.hc_id,
				"docFile", "", api.data.hc_id+".doc");
		var data = $.parseJSON(response);
		if (data.success) {
				top.$.notice("文件保存成功！");
			api.close();
	
		} else {
			alert("文件保存失败！");
		}
	}

	//插入书签值
	function setValues(doc,type) {
		$.post("contractInfoAction/detail.do",{id:"${param.id}"},function(res){
			if(res.success){
				var data = res.data;
				 if (data.contract_no !=null &&data.contract_no  != "null"||data.contract_no != undefined) {
					 TANGER_OCX_OBJ.SetBookmarkValue("contract_no",data.contract_no)
					} 
				 if(type=="2"){
					 if (data.fk_contract_no !=null &&data.fk_contract_no  != "null"||data.fk_contract_no != undefined) {
						 TANGER_OCX_OBJ.SetBookmarkValue("fk_contract_no",data.fk_contract_no)
						} 
					 if (data.negotiate_parter !=null &&data.negotiate_parter  != "null"||data.negotiate_parter != undefined) {
						 TANGER_OCX_OBJ.SetBookmarkValue("negotiate_parter",data.negotiate_parter)
						} 
					
				 }
				
				 if (data.sign_time !=null &&data.sign_time  != "null"||data.sign_time != undefined) {
					 TANGER_OCX_OBJ.SetBookmarkValue("sign_time",
							 data.sign_time.substring(0,4)+"年"+data.sign_time.substring(5,7)+"月"+data.sign_time.substring(8,10)+"日")
					} 
				 if (data.negotiate_place !=null &&data.negotiate_place  != "null"||data.negotiate_place != undefined) {
					 TANGER_OCX_OBJ.SetBookmarkValue("negotiate_place",data.negotiate_place)
					} 
				 
				 if (data.negotiate_recorder !=null &&data.negotiate_recorder  != "null"||data.negotiate_recorder != undefined) {
					 TANGER_OCX_OBJ.SetBookmarkValue("negotiate_recorder",data.negotiate_recorder)
					} 
				
				 //合同编号
				/* if (doc.BookMarks.Exists("contract_no")) {
					var jgdm = data.contract_no;
					if (jgdm == null||jgdm == "null"||jgdm == undefined) {
						setBookMarkValue1("contract_no", "");
					} else {
						
						setBookMarkValue1("contract_no", jgdm);
					}
				}
				//总合同编号
				if (doc.BookMarks.Exists("fk_contract_no")) {
					var jgdm = data.fk_contract_no;
					if (jgdm == null||jgdm == "null"||jgdm == undefined) {
						setBookMarkValue1("fk_contract_no", "");
					} else {
						setBookMarkValue1("fk_contract_no", jgdm);
					}
				}
				
				//时间
				if (doc.BookMarks.Exists("sign_time")) {
					var jgdm = data.sign_time;
					if (jgdm == null||jgdm == "null"||jgdm == undefined) {
						setBookMarkValue1("sign_time", "");
					} else {
						setBookMarkValue1("sign_time", jgdm.substring(0,10));
					}
				}
				//谈判地点
				if (doc.BookMarks.Exists("negotiate_place")) {
					var jgdm = data.negotiate_place;
					if (jgdm == null||jgdm == "null"||jgdm == undefined) {
						setBookMarkValue1("negotiate_place", "");
					} else {
						setBookMarkValue1("negotiate_place", jgdm);
					}
				}
				//谈判记录人
				if (doc.BookMarks.Exists("negotiate_recorder")) {
					var jgdm = data.negotiate_recorder;
					if (jgdm == null||jgdm == "null"||jgdm == undefined) {
						setBookMarkValue1("negotiate_recorder", "");
					} else {
						setBookMarkValue1("negotiate_recorder", jgdm);
					}
				}
				//谈判参加人员
				if (doc.BookMarks.Exists("negotiate_parter")) {
					var jgdm = data.negotiate_parter;
					if (jgdm == null||jgdm == "null"||jgdm == undefined) {
						setBookMarkValue1("negotiate_parter", "");
					} else {
						setBookMarkValue1("negotiate_parter", jgdm);
					}
				} */
			}
		})
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