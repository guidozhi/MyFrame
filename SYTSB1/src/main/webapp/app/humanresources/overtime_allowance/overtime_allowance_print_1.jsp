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
	background: url('k/kui/skins/icons/word.gif') no-repeat center;
}

.l-icon-printPreview {
	background: url('k/kui/skins/icons/search2.gif') no-repeat center;
}

.l-icon-fullScreen {
	background: url('k/kui/skins/icons/div-drag.gif') no-repeat center;
}
</style>
<script src="pub/office/ntkoofficecontrol.js" type="text/javascript"></script>
<script src="pub/office/editor.js" type="text/javascript"></script>
<script src="app/tzsb/inspection/flow/insing/hall/common/common.js"
	type="text/javascript"></script>
<script type="text/javascript">
	var basepath = "${pageContext.request.contextPath}/";
	var toolBar;//工具条
	var device_sort = "";
	var device_sort_code = "";
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
		var roleFlag = "${param.role_flag}";
		
		TANGER_OCX_OBJ
		.OpenFromURL($("base").attr("href")
				+ "app/humanresources/overtime_allowance/allowance.docx");
		
		
		/* if (roleFlag == "narmal_person") {
			// 普通员工
			TANGER_OCX_OBJ
					.OpenFromURL($("base").attr("href")
							+ "app/humanresources/request_for_overtime/temple/normalperson.docx");
		}
		if (roleFlag == "depart_manager") {
			// 部门负责人
			TANGER_OCX_OBJ
					.OpenFromURL($("base").attr("href")
							+ "app/humanresources/request_for_overtime/temple/departmanager.docx");
		}
		if (roleFlag == "leader_manager") {
			// 院领导
			TANGER_OCX_OBJ
					.OpenFromURL($("base").attr("href")
							+ "app/humanresources/request_for_overtime/temple/leadermanager.docx");
		} */
		var doc = TANGER_OCX_OBJ.ActiveDocument;
		setValues(doc);
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
			icon : "save",
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
						"${pageContext.request.contextPath}/contractAction/saveContractWord.do?net_id=${param.id}&id="
								+ api.data.hc_id, "docFile", "", api.data.hc_id
								+ ".doc");
		var data = $.parseJSON(response);
		if (data.success) {
			top.$.notice("文件保存成功！");
			api.close();

		} else {
			alert("文件保存失败！");
		}
	}

	//插入书签值
	function setValues(doc) {
		
		var doc = TANGER_OCX_OBJ.ActiveDocument;
		
		$
				.post(
						"employeeallowanceAction/detail.do",
						{
							id : "${param.id}"
						},
						function(res) {
							if (res.success) {
								var data = res.data;
								if (data.other_applicants != null
										&& data.other_applicants != "null"
										&& data.other_applicants != undefined) {
									TANGER_OCX_OBJ.SetBookmarkValue(
											"apply_op", data.other_applicants)
								}
								if (data.department != null
										&& data.department != "null"
											&&data.department != undefined) {
									TANGER_OCX_OBJ.SetBookmarkValue(
											"department", data.department)
								}
								if (data.overtime_place != null
										&& data.overtime_place != "null"
											&&data.overtime_place != undefined) {
									TANGER_OCX_OBJ.SetBookmarkValue(
											"overtime_place",
											data.overtime_place)
								}
								if (data.overtime_type != null
										&& data.overtime_type != "null"
											&&data.overtime_type != undefined) {
									TANGER_OCX_OBJ
											.SetBookmarkValue("overtime_type",
													data.overtime_type)
								}
								if (data.overtime_reason != null
										&& data.overtime_reason != "null"
											&& data.overtime_reason != undefined) {
									TANGER_OCX_OBJ.SetBookmarkValue(
											"overtime_reason",
											data.overtime_reason)
								}

								
									TANGER_OCX_OBJ.SetBookmarkValue(
											"overtime",
											api.data.startDate+" 到 "+api.data.endDate)
								
								if (data.overtime_day != null
										&& data.overtime_day != "null"
										&&data.overtime_day != undefined) {
									TANGER_OCX_OBJ.SetBookmarkValue(
											"overtime_day", data.overtime_day)
								}
								if (data.overtime_type != null
										&& data.overtime_type != "null"
										&&data.overtime_type != undefined) {
									if(data.overtime_type=="双休日"){
										TANGER_OCX_OBJ.SetBookmarkValue("overtime_type_2","√")
										TANGER_OCX_OBJ.SetBookmarkValue("overtime_type_1","  ")
										TANGER_OCX_OBJ.SetBookmarkValue("overtime_type_3","  ")
									}else if(data.overtime_type=="法定节假日"){
										//alert("法定节假日")
											TANGER_OCX_OBJ.SetBookmarkValue("overtime_type_2","  ")
											TANGER_OCX_OBJ.SetBookmarkValue("overtime_type_1","  ")
											TANGER_OCX_OBJ.SetBookmarkValue("overtime_type_3","√")
										
									}else if(data.overtime_type=="周一至周五"){
										//alert("周一至周五")
										TANGER_OCX_OBJ.SetBookmarkValue("overtime_type_2","  ")
										TANGER_OCX_OBJ.SetBookmarkValue("overtime_type_1","√")
										TANGER_OCX_OBJ.SetBookmarkValue("overtime_type_3","  ")
									
									}else{
										TANGER_OCX_OBJ.SetBookmarkValue("overtime_type_2","  ")
										TANGER_OCX_OBJ.SetBookmarkValue("overtime_type_1","  ")
										TANGER_OCX_OBJ.SetBookmarkValue("overtime_type_3","  ")
									
									}
									
								}
								if(doc.BookMarks.Exists("overtime_opinion_2")&&doc.BookMarks.Exists("overtime_opinion_1")){
									TANGER_OCX_OBJ.SetBookmarkValue("overtime_opinion_2","√")
									TANGER_OCX_OBJ.SetBookmarkValue("overtime_opinion_1","  ")
								}
								if(doc.BookMarks.Exists("subsidy_money")){
									if(data.sumMoney!=null){
										TANGER_OCX_OBJ.SetBookmarkValue("subsidy_money",data.sumMoney+"元")
									}
								}
								
							/* 	$.post("allowancefoAction/getDetailByOp.do",{"id":"${param.id}","opId":"${param.opId}"},function(res){
									if(res.success&&res.data!=null){
										overtime_opinion  = res.data.overtime_type;
										if(overtime_opinion!=undefined&&overtime_opinion!=null){
											if(overtime_opinion=="补助"){
												TANGER_OCX_OBJ.SetBookmarkValue("overtime_opinion_2","√")
												TANGER_OCX_OBJ.SetBookmarkValue("overtime_opinion_1","  ")
											}else if(overtime_opinion=="补休"){
												TANGER_OCX_OBJ.SetBookmarkValue("overtime_opinion_2","√")
												TANGER_OCX_OBJ.SetBookmarkValue("overtime_opinion_1","  ")
												
											}else{
												TANGER_OCX_OBJ.SetBookmarkValue("overtime_opinion_2","  ")
												TANGER_OCX_OBJ.SetBookmarkValue("overtime_opinion_1","  ")
											}
										}
										if(doc.BookMarks.Exists("take_time")){
											if(res.data.take_date_start!=null&&res.data.take_date_end!=null){
												TANGER_OCX_OBJ.SetBookmarkValue("take_time",res.data.take_date_start+" 到 "+res.data.take_date_end)
											}else if(res.data.take_date_start!=null&&res.data.take_date_end==null){
												TANGER_OCX_OBJ.SetBookmarkValue("take_time",res.data.take_date_start)
											}else if(res.data.take_date_start==null&&res.data.take_date_end!=null){
												TANGER_OCX_OBJ.SetBookmarkValue("take_time",res.data.take_date_end)
											}
										}
										
										if(doc.BookMarks.Exists("subsidy_money")){
											if(res.data.subsidy_money!=null){
												TANGER_OCX_OBJ.SetBookmarkValue("subsidy_money",res.data.subsidy_money)
											}
										}
										
									}
									
								}) */
								if(doc.BookMarks.Exists("enter_time")){
									if(res.data.enter_time!=null){
										TANGER_OCX_OBJ.SetBookmarkValue("enter_time",
												res.data.enter_time.substring(0,4)+"年"+res.data.enter_time.substring(5,7)+"月"+res.data.enter_time.substring(8,10))
									}
								}
								$.ajax({
											url : "employee/basic/getEmpSignId.do",
											type : "POST",
											async:false,
											datatype : "json",
											data : {
												id:"${param.opId}",type:"user"
											},
											success : function(res) {
												if(res.pictureID!=null&&res.pictureID!=""){
												
												TANGER_OCX_OBJ.ActiveDocument.Application.Selection.GoTo(-1, 0, 0, "other_applicants_id");
												
											
												try {
													TANGER_OCX_OBJ.AddPicFromURL($("base").attr("href")+"fileupload2/downloadByObjId2.do?id="+res.pictureID,
															false,1,0,1,100); 
												} catch (e) {
												}
												}
											}
										})


									if (data.minister_audit_remark != null
											&& data.minister_audit_remark != "null"
												&& data.minister_audit_remark != undefined) {
										TANGER_OCX_OBJ.SetBookmarkValue(
												"minister_audit_remark",
												data.minister_audit_remark)
									}
									
									if(data.minister_audit != null
											&& data.minister_audit != "null"
												&& data.minister_audit != undefined){
										if(res.data.minister_audit_id!=null){
											$.ajax({
												url : "employee/basic/getEmpSignId.do",
												type : "POST",
												async:false,
												datatype : "json",
												data : {
													id:res.data.minister_audit_id,type:"user"
												},
												success : function(res) {
													if(res.pictureID!=null&&res.pictureID!=""){
													
													TANGER_OCX_OBJ.ActiveDocument.Application.Selection.GoTo(-1, 0, 0, "minister_audit");
													
												
													try {
														TANGER_OCX_OBJ.AddPicFromURL($("base").attr("href")+"fileupload2/downloadByObjId2.do?id="+res.pictureID,
																false,1,0,1,100); 
													} catch (e) {
													}
													}
												}
											})
										}
										
									}
									
									if (data.dept_op_date != null
											&& data.dept_op_date != "null"
												&&data.dept_op_date != undefined) {
										TANGER_OCX_OBJ.SetBookmarkValue("dept_op_date",
												 data.dept_op_date.substring(0,4)+"年"+data.dept_op_date.substring(5,7)+"月"+data.dept_op_date.substring(8,10)+"日")
									}
								
							
								//	普通员工，院领导，部门负责人涉及人事审核
								/* if (data.role_flag == "narmal_person"
										|| data.role_flag == "leader_manager"
										|| data.role_flag == "depart_manager") { */
									if (data.personnel_audit_remark != null
											&& data.personnel_audit_remark != "null"
												&&data.personnel_audit_remark != undefined) {
										TANGER_OCX_OBJ.SetBookmarkValue(
												"personnel_audit_remark",
												data.personnel_audit_remark)
									}
									
									if(data.personnel_audit != null
											&& data.personnel_audit != "null"
												&& data.personnel_audit != undefined){
										if(res.data.personnel_audit_id!=null){
											$.ajax({
												url : "employee/basic/getEmpSignId.do",
												type : "POST",
												async:false,
												datatype : "json",
												data : {
													id:res.data.personnel_audit_id,type:"user"
												},
												success : function(res) {
													if(res.pictureID!=null&&res.pictureID!=""){
													TANGER_OCX_OBJ.ActiveDocument.Application.Selection.GoTo(-1, 0, 0, "personnel_audit");
													try {
														TANGER_OCX_OBJ.AddPicFromURL($("base").attr("href")+"fileupload2/downloadByObjId2.do?id="+res.pictureID,
																false,1,0,1,100); 
													} catch (e) {
													}
													}
												}
											})
										}
										
									}
									
									if (data.personnel_audit_time != null
											&& data.personnel_audit_time != "null"
												&& data.personnel_audit_time != undefined) {
										TANGER_OCX_OBJ.SetBookmarkValue("personnel_audit_time",
												 data.personnel_audit_time.substring(0,4)+"年"+data.personnel_audit_time.substring(5,7)+"月"+data.personnel_audit_time.substring(8,10)+"日")
									}
								//}
								// 普通员工和部长涉及院领导审核
								/* if (data.role_flag == "narmal_person"
										|| data.role_flag == "depart_manager") { */
									if (data.leader_audit_remark != null
											&& data.leader_audit_remark != "null"
											&& data.leader_audit_remark != undefined) {
										TANGER_OCX_OBJ.SetBookmarkValue(
												"leader_audit_remark",
												data.leader_audit_remark)
									}
									
									if(data.leader_audit != null
											&& data.leader_audit != "null"
												&& data.leader_audit != undefined){
										if(res.data.leader_audit_op!=null){
											$.ajax({
												url : "employee/basic/getEmpSignId.do",
												type : "POST",
												async:false,
												datatype : "json",
												data : {
													id:res.data.leader_audit_op,type:"user"
													
												},
												success : function(res) {
													if(res.pictureID!=null&&res.pictureID!=""){
													
													TANGER_OCX_OBJ.ActiveDocument.Application.Selection.GoTo(-1, 0, 0, "leader_audit");
													
													try {
														TANGER_OCX_OBJ.AddPicFromURL($("base").attr("href")+"fileupload2/downloadByObjId2.do?id="+res.pictureID,
																false,1,0,1,100); 
													} catch (e) {
													}
													}
												}
											})
										}
										
									//}
									
									if (data.leader_date != null
											&& data.leader_date != "null"
											&& data.leader_date != undefined) {
										TANGER_OCX_OBJ.SetBookmarkValue("leader_date",
												 data.leader_date.substring(0,4)+"年"+data.leader_date.substring(5,7)+"月"+data.leader_date.substring(8,10)+"日")
									}
								}
								// 部门负责人和分管领导涉及院长审核
							/* 	if (data.role_flag == "depart_manager"
										&& data.role_flag == "leader_manager") { */
									if (data.dean_audit_remark != null
											&& data.dean_audit_remark != "null"
											&& data.dean_audit_remark != undefined) {
										TANGER_OCX_OBJ.SetBookmarkValue(
												"dean_audit_remark",
												data.dean_audit_remark)
									}
									
									
									if(data.dean_audit != null
											&& data.dean_audit != "null"
												&& data.dean_audit != undefined){
										if(res.data.dean_id!=null){
											$.ajax({
												url : "employee/basic/getEmpSignId.do",
												type : "POST",
												async:false,
												datatype : "json",
												data : {
													id:res.data.dean_id,type:"user"
												},
												success : function(res) {
													if(res.pictureID!=null&&res.pictureID!=""){
													TANGER_OCX_OBJ.ActiveDocument.Application.Selection.GoTo(-1, 0, 0, "dean_audit");
													try {
														TANGER_OCX_OBJ.AddPicFromURL($("base").attr("href")+"fileupload2/downloadByObjId2.do?id="+res.pictureID,
																false,1,0,1,100); 
													} catch (e) {
													}
													}
												}
											})
										}
										
									}
									
									if (data.dean_audit_time != null
											&& data.dean_audit_time != "null"
											&& data.dean_audit_time != undefined) {
										TANGER_OCX_OBJ.SetBookmarkValue("dean_audit_time",
												 data.dean_audit_time.substring(0,4)+"年"+data.dean_audit_time.substring(5,7)+"月"+data.dean_audit_time.substring(8,10)+"日")
									}
								//}
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