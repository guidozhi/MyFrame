<%@page import="com.khnt.rtbox.template.constant.RtPath"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
	<title></title>
	<%@ include file="/k/kui-base-form.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/app/relation/relation_info.js"></script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app/relation/css/common.css" >
	<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

	<%
		String fk_report_id = request.getParameter("report_id");
		String ysjl_rtCode = request.getParameter("ysjl_rtbox_code");
		String report_rtCode = request.getParameter("report_rtbox_code");
		//String report_name = request.getParameter("report_name");
	%>
	<script type="text/javascript">
		var pageStatus = "${param.status}";
		var fk_report_id = "${param.report_id}";
		var ysjl_rtCode = api.data.ysjl_rtCode
		var report_rtCode = api.data.report_rtCode
		var report_name =  api.data.report_name
		var cur_ysjl_page = "1";
		var cur_report_page = "1";
		$(function() {
			//var height = $(window).height();
			//$('#tree_big_tb_td_left').css({height:height});
			//$('#tree_big_tb_td_right').css({height:height});
			
			createInfoGrid();
			//addNewRow("infos");

			$("#formObj").initForm({ //参数设置示例
				toolbar : [],
				getSuccess : function(resp) {
					/*
					if(resp.success){
						relationInfoGrid.loadData({
							Rows : resp.reportRecordParse
						});
					}
					*/
				},
				success : function(response) {//处理成功
					if (response.success) {
						top.$.dialog.notice({
							content : "保存成功！"
						});
						api.close();
						api.data.pwindow.close();
					} else {
						$.ligerDialog.error('保存失败！<br/>' + response.msg);
					}
				}
			});
			
			/* $.post("r3Action/getRelations.do?report_id=${param.report_id}", function(resp) {
				if (resp.success) {
					relationInfoGrid.loadData({
						Rows : resp.reportRecordParse
					});
				}
			}); */

			// 捕获F5事件
			$("body").keydown(function(e){
				var ev = window.event || e;
				var code = ev.keyCode || ev.which;
				if (code==116) {
			 		// 阻止默认的F5事件
					if(ev.preventDefault) {
						ev.preventDefault();
					}else {
						ev.keyCode=0;
						ev.returnValue=false; 
					}
					// 调用刷新函数
					refreshIframe();  
				}
			});
		})
		
		function doSignStart(position,fsEl){
			if(position.indexOf("left")!=-1){
				//f_startItem('left_R_Iframe','原始记录');
				f_startItemSign('left_R_Iframe','原始记录',fsEl);
			}else{
				//f_startItem('right_R_Iframe','报告');
				f_startItemSign('right_R_Iframe','报告',fsEl);
			}
		}
		
		function doSignEnd(position,fsEl){
			if(position.indexOf("left")!=-1){
				//f_endItem('left_R_Iframe','原始记录');
				f_endItemSign('left_R_Iframe','原始记录',fsEl);
			}else{
				//f_endItem('right_R_Iframe','报告');
				f_endItemSign('right_R_Iframe','报告',fsEl);
			}
		}
		
		
		function initSelPages() {
			$.post("com/rt/page/getTempDir.do?code=${param.ysjl_rtbox_code}",
					function(resp) {
						if (resp.success) {
							$("#temp_pages${param.ysjl_rtbox_code}")
									.ligerGetComboBoxManager().setData(
											eval(resp.data));
						}
					});

			$.post("com/rt/page/getTempDir.do?code=${param.report_rtbox_code}",
					function(resp) {
						if (resp.success) {
							$("#temp_pages${param.report_rtbox_code}")
									.ligerGetComboBoxManager().setData(
											eval(resp.data));
						}
					});
		}

		
		function f_addYsjlItem() {
			var focusElementName = window.frames["left_R_Iframe"]
					.getFocusElementName();
			if (focusElementName == undefined
					|| focusElementName == "undefined") {
				$.ligerDialog.alert('亲，请先选择您的原始记录项目！');
			} else {
				addRowOfYsjlKey("infos", focusElementName);
			}
		}

		function f_addYsjlItems() {
			var focusElementName = window.frames["left_R_Iframe"]
					.getFocusElementName();
			if (focusElementName == undefined
					|| focusElementName == "undefined") {
				$.ligerDialog.alert('亲，请先选择您的原始记录项目！');
			} else {
				//updateRowOfYsjlKey("infos", focusElementName);
				updateRowOfFormule("infos", focusElementName);
			}
		}

		function f_addReportItem() {
			var focusElementName = window.frames["right_R_Iframe"]
					.getFocusElementName();
			if (focusElementName == undefined
					|| focusElementName == "undefined") {
				$.ligerDialog.alert('亲，请先选择您的报告项目！');
			} else {
				updateRowOfReportKey("infos", focusElementName);
			}
		}

		function f_addRelations() {
			$("body").mask("正在保存数据，请稍后！");
			var leftElementNames = getIframeElements("left_R_Iframe", "input",
					"beginLeftElementName", "endLeftElementName");
			var rightElementNames = getIframeElements("right_R_Iframe",
					"input", "beginRightElementName", "endRightElementName");
			if (leftElementNames == "") {
				leftElementNames = window.frames["left_R_Iframe"]
						.getFocusElementName();
			}
			if (rightElementNames == "") {
				rightElementNames = window.frames["right_R_Iframe"]
						.getFocusElementName();
			}
			if (leftElementNames != "" && rightElementNames != ""
					&& leftElementNames != "undefined"
					&& rightElementNames != "undefined"
					&& leftElementNames != undefined
					&& rightElementNames != undefined) {
				var e_left_arr = leftElementNames.split(",");
				var e_right_arr = rightElementNames.split(",");
				if (e_left_arr.length != e_right_arr.length) {
					$("body").unmask();
					$.ligerDialog.alert("亲，您所选的报告项目数量与原始记录项目数量不一致哦，请重新选择！");
					removeCssToElements("left_R_Iframe", "input",
							"beginLeftElementName", "endLeftElementName");
					removeCssToElements("right_R_Iframe", "input",
							"beginRightElementName", "endRightElementName");
					removeCssToElements("left_R_Iframe", "div",
							"beginLeftElementName", "endLeftElementName");
					removeCssToElements("right_R_Iframe", "div",
							"beginRightElementName", "endRightElementName");
					clearSigns();
					return;
				}
				var grid_str = $.ligerui.toJSON(relationInfoGrid.getData());
				for (var i = 0; i < e_left_arr.length; i++) {
					if (grid_str.indexOf(e_left_arr[i]) != -1) {
						$("body").unmask();
						/* $.ligerDialog.alert("亲，对应关系列表已存在项目<br>[ "
								+ e_left_arr[i] + " ]<br>不能重复，请重新选择！"); */
						top.$.dialog.notice({
							content : "亲，对应关系列表已存在项目<br>[ "
										+ e_left_arr[i] + " ]<br>不能重复，请重新选择！"
						});
						removeCssToElements("left_R_Iframe", "input",
								"beginLeftElementName", "endLeftElementName");
						removeCssToElements("right_R_Iframe", "input",
								"beginRightElementName", "endRightElementName");
						removeCssToElements("left_R_Iframe", "div",
								"beginLeftElementName", "endLeftElementName");
						removeCssToElements("right_R_Iframe", "div",
								"beginRightElementName", "endRightElementName");
						clearSigns();
						break;
					} else {
						addRelationRow("infos", e_left_arr[i], e_right_arr[i]);
					}
					if (i == e_left_arr.length - 1) {
						$("body").unmask();
						clearSigns();
						top.$.dialog.notice({
							content : "恭喜您！一键添加完成！"
						});
						//$.ligerDialog.success('恭喜您！一键添加完成！');
					}
				}
			} else {
				if (leftElementNames == undefined
						&& rightElementNames == undefined) {
					$.ligerDialog.alert('亲，请先选择您的原始记录和报告项目！');
					$("body").unmask();
				} else if (leftElementNames == undefined
						&& rightElementNames != undefined) {
					$.ligerDialog.alert('亲，请先选择您的原始记录项目！');
					$("body").unmask();
				} else if (rightElementNames == undefined
						&& leftElementNames != undefined) {
					$.ligerDialog.alert('亲，请选择您的报告项目！');
					$("body").unmask();
				}
			}
		}

		function getIframeElements(iframe_name, element_type, beginElementName,
				endElementName) {
			var inputs = window.frames[iframe_name].getElements(element_type);
			var beginElementName = $("#" + beginElementName).val();
			var endElementName = $("#" + endElementName).val();
			var addElement = false;
			var elementNames = "";
			for (var i = 0; i < inputs.length; i++) {
				if (inputs[i].name == beginElementName) {
					addElement = true;
				}
				if (addElement) {
					if (elementNames.indexOf(inputs[i].name) == -1) {
						if (inputs[i].name.indexOf('_val') == inputs[i].name.length - 4) {
							continue;
						}
						if (elementNames != "") {
							elementNames += ",";
						}
						elementNames += inputs[i].name;
					}
				}
				if (inputs[i].name == endElementName) {
					//addElement = false;
					break;
				}
			}
			return elementNames;
		}

		function f_startItem(iframe_name, temp_type) {
			var focusElementName = window.frames[iframe_name]
					.getFocusElementName();
			window.frames[iframe_name].setCssToFocusElement();
			if (focusElementName != undefined
					&& focusElementName != "undefined"
					&& focusElementName != "") {
				if (iframe_name.indexOf("left") != -1) {
					$("#beginLeftElementName").val(focusElementName);
				} else {
					$("#beginRightElementName").val(focusElementName);
				}
			} else {
				$.ligerDialog.alert('亲，请先选择您的' + temp_type + '项目！');
			}
		}

		function f_endItem(iframe_name, temp_type) {
			var focusElementName = window.frames[iframe_name]
					.getFocusElementName();
			if (focusElementName != undefined
					&& focusElementName != "undefined"
					&& focusElementName != "") {
				if (iframe_name.indexOf("left") != -1) {
					$("#endLeftElementName").val(focusElementName);
					setCssToElements(iframe_name, "input",
							"beginLeftElementName", "endLeftElementName");
					setCssToElements(iframe_name, "div",
							"beginRightElementName", "endRightElementName");
				} else {
					$("#endRightElementName").val(focusElementName);
					setCssToElements(iframe_name, "input",
							"beginRightElementName", "endRightElementName");
					setCssToElements(iframe_name, "div",
							"beginRightElementName", "endRightElementName");
				}
			} else {
				$.ligerDialog.alert('亲，请先选择您的' + temp_type + '项目！');
			}
		}

		function f_startItemSign(iframe_name, temp_type, fsEl) {
			//var focusElementName = window.frames[iframe_name].getFocusElementName();
			var focusElementName = fsEl.name;
			window.frames[iframe_name].setCssToElement(fsEl.id);
			if (focusElementName != undefined
					&& focusElementName != "undefined"
					&& focusElementName != "") {
				if (iframe_name.indexOf("left") != -1) {
					$("#beginLeftElementName").val(focusElementName);
				} else {
					$("#beginRightElementName").val(focusElementName);
				}
			} else {
				$.ligerDialog.alert('亲，请先选择您的' + temp_type + '项目！');
			}
		}

		function f_endItemSign(iframe_name, temp_type, fsEl) {
			//var focusElementName = window.frames[iframe_name].getFocusElementName();
			var focusElementName = fsEl.name;
			if (focusElementName != undefined
					&& focusElementName != "undefined"
					&& focusElementName != "") {
				if (iframe_name.indexOf("left") != -1) {
					$("#endLeftElementName").val(focusElementName);
					setCssToElements(iframe_name, "input",
							"beginLeftElementName", "endLeftElementName");
					setCssToElements(iframe_name, "div",
							"beginLeftElementName", "endLeftElementName");
				} else {
					$("#endRightElementName").val(focusElementName);
					setCssToElements(iframe_name, "input",
							"beginRightElementName", "endRightElementName");
					setCssToElements(iframe_name, "div",
							"beginRightElementName", "endRightElementName");
				}
			} else {
				$.ligerDialog.alert('亲，请先选择您的' + temp_type + '项目！');
			}
		}

		function setCssToElements(iframe_name, element_type, beginElementName,
				endElementName) {
			var inputs = window.frames[iframe_name].getElements(element_type);
			var beginElementName = $("#" + beginElementName).val();
			var endElementName = $("#" + endElementName).val();
			var addCss = false;
			var elementNames = "";
			for (var i = 0; i < inputs.length; i++) {
				if (inputs[i].name == beginElementName) {
					addCss = true;
				}
				if (addCss) {
					window.frames[iframe_name].setCssToElement(inputs[i].id);
				}
				if (inputs[i].name == endElementName) {
					break;
				}
			}
		}

		function removeCssToElements(iframe_name, element_type,
				beginElementName, endElementName) {
			var inputs = window.frames[iframe_name].getElements(element_type);
			var beginElementName = $("#" + beginElementName).val();
			var endElementName = $("#" + endElementName).val();
			//var removeCss = false;
			var elementNames = "";
			for (var i = 0; i < inputs.length; i++) {
				/* if (inputs[i].name == beginElementName) {
					removeCss = true;
				}
				if (removeCss) {
					window.frames[iframe_name].removeCssToElement(inputs[i].id);
				}
				if (inputs[i].name == endElementName) {
					break;
				} */
				window.frames[iframe_name].removeCssToElement(inputs[i].id);
			}
		}

		function clearSigns() {
			$("#beginLeftElementName").val("");
			$("#beginRightElementName").val("");
			$("#endLeftElementName").val("");
			$("#endRightElementName").val("");
		}
		
		function clearCss(elementId) {
			$("#"+elementId).val("");
		}

		function toPage(iframeName, rtbox_code) {
			var pageCode = $("input[name='temp_pages" + rtbox_code + "']")
					.ligerGetComboBoxManager().getValue();

			var rtCode = "";
			var position = "";
			if (iframeName.indexOf("left") != -1) {
				cur_ysjl_page = pageCode;
				rtCode = ysjl_rtCode;
				position = "left";
			} else {
				cur_report_page = pageCode;
				rtCode = report_rtCode;
				position = "right";
			}
			var pageName = "index" + pageCode + ".jsp";
			$("#" + iframeName).attr(
					"src",
					"app/relation/rtbox_index.jsp?rtCode=" + rtCode
							+ "&rtPage=" + pageName +"&iframeId="+iframeName+ "&position=" + position+"&pageCode="+pageCode);
			
		}

		function f_clearElementCss(frameName, e_type, s_ele_name, e_ele_name){
			removeCssToElements(frameName, e_type, s_ele_name, e_ele_name);
			clearCss(s_ele_name);
			clearCss(e_ele_name);
		}
		
		function f_clearElementsCss(){
			removeCssToElements("left_R_Iframe", "input",
					"beginLeftElementName", "endLeftElementName");
			removeCssToElements("right_R_Iframe", "input",
					"beginRightElementName", "endRightElementName");
			removeCssToElements("left_R_Iframe", "div",
					"beginLeftElementName", "endLeftElementName");
			removeCssToElements("right_R_Iframe", "div",
					"beginRightElementName", "endRightElementName");
			clearSigns();
		}
		
		function save(type) {
			//验证表单数据
			if ($("#formObj").validate().form()) {

				// 验证grid
				if (!validateGrid(relationInfoGrid)) {
					return false;
				}

				if (relationInfoGrid.getData().length < 1) {
					$.ligerDialog.alert("亲，您还未添加任何对应关系数据哦！");
					return false;
				}

				if (confirm("亲，确定保存吗？")) {
					$("#save").attr("disabled", "disabled");
					url = "r3Action/saveBasic.do";
					var data = {};
					data["r3s"] = relationInfoGrid.getData();
					$("body").mask("正在保存数据，请稍后！");
					$.ajax({
						url : url,
						type : "POST",
						datatype : "json",
						contentType : "application/json; charset=utf-8",
						data : $.ligerui.toJSON(data),
						success : function(resp) {
							$("body").unmask();
							if (resp["success"]) {
								if("close" == type){
									if (api.data.pwindow.api.data.window.Qm) {
										api.data.pwindow.api.data.window.refreshGrid();
										api.data.pwindow.api.close();
									}
								}
								
								top.$.dialog.notice({
									content : '恭喜您，保存成功！'
								});
								if("close" == type){
									api.close();
								}
							} else {
								$.ligerDialog.error('提示：' + resp["msg"]);
							}
						},
						error : function(resp) {
							$("body").unmask();
							$.ligerDialog.error('提示：' + resp.msg);
							$("#save").removeAttr("disabled");
						}
					});
				}
			}
		}

		// 刷新当前框架
		function refreshIframe() {
			//支持IE
			document.getElementById('left_R_Iframe').contentWindow.location
					.reload();
			document.getElementById('right_R_Iframe').contentWindow.location
					.reload();
		}
		
		function f_checkItems(){
			var left_inputs = window.frames["left_R_Iframe"].getElements("input");
			
			var left_items = "";
			for(var i=0;i<left_inputs.length;i++){
				var left_item = left_inputs[i];
				if($(left_item).attr("name") != "undefined" && $(left_item).attr("name") != undefined){
					var left_item_name = $(left_item).attr("name");
					if(left_item_name!="inputFocus" && left_item_name!="thisPage" 
							&& left_item_name!="fk_report_id" && left_item_name!="fk_inspection_info_id" 
							&& left_item_name!="undefined" && left_item_name!=undefined){
						if(left_items != ""){
							left_items += ",";
						}
						left_items += left_item_name;
					}
				}
			}
			
			$.post("${pageContext.request.contextPath}/r3Action/checkItems.do", {
				report_id : fk_report_id,
				itemNames : left_items
			}, function(resp) {
				if (resp.success) {
					if(resp.return_items!=""){
						alert('您有未配置的数据项：' + resp.return_items+"。");
					}else{
						top.$.dialog.notice({
							content : '恭喜您，未检测到有漏项，继续加油哦亲！'
						});
					}
				}else{
					$.ligerDialog.error(resp.msg);
				}
			}, "json");
		}

		function close() {
			api.close();
		}
		

		//本文来自 lujianfeiccie2009 的CSDN 博客 ，全文地址请点击：https://blog.csdn.net/lujianfeiccie2009/article/details/6429794?utm_source=copy 
	</script>	
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="javascript:initSelPages();">
<div style="">
<form name="formObj" id="formObj" getAction="">
	<input id="beginLeftElementName" name="beginLeftElementName"  type="hidden"  />
	<input id="endLeftElementName" name="endLeftElementName"  type="hidden"  />
	<input id="beginRightElementName" name="beginRightElementName"  type="hidden"  />
	<input id="endRightElementName" name="endRightElementName"  type="hidden"  />
	<table border="0" cellpadding="0" cellspacing="0" id="tree_big_tb" height="100%">
		<tr>
			<td id="tree_big_tb_td_left" style="height:20px;">
				<div id="wrap">
				原始记录目录：<input type="text" id="temp_pages${param.ysjl_rtbox_code}" name="temp_pages${param.ysjl_rtbox_code}" ltype="select" validate="{required:false}"  onchange="toPage('left_R_Iframe','${param.ysjl_rtbox_code}')" ligerui="{width:'180'}"/>
				<!-- 
				&nbsp;<input id="getYsjlItemBtn" type="button" class="bt1" value="选取" onclick="javascript:f_addYsjlItem();" />
				&nbsp;<input id="startYsjlItemBtn" type="button" class="bt1" value="开始" onclick="javascript:f_startItem('left_R_Iframe','原始记录');" />
				&nbsp;<input id="endYsjlItemBtn" type="button" class="bt1" value="结束" onclick="javascript:f_endItem('left_R_Iframe','原始记录');" />
				&nbsp;<input id="emptyYsjlItemBtn" type="button" class="bt1" value="清空选择" onclick="javascript:f_clearElementCss('left_R_Iframe', 'input','beginLeftElementName', 'endLeftElementName');" />
				&nbsp;<input id="addRelationBtn" type="button" class="bt1" value="一键添加" onclick="javascript:f_addRelations();" />
				&nbsp;<input id="getYsjlItemsBtn" type="button" class="bt1" value="多对一" onclick="javascript:f_addYsjlItems();" />
				 -->
				</div>
			</td>
			<td id="tree_big_tb_td_right" style="height:20px;">
				<div id="wrap">
				报告目录：<input type="text" id="temp_pages${param.report_rtbox_code}" name="temp_pages${param.report_rtbox_code}" ltype="select" validate="{required:false}"  onchange="toPage('right_R_Iframe','${param.report_rtbox_code}')" ligerui="{width:'180'}"/>			
				<!-- 
				&nbsp;<input id="getReportItemBtn" type="button" class="bt1" value="选取" onclick="javascript:f_addReportItem();" />
				&nbsp;<input id="startYsjlItemBtn" type="button" class="bt1" value="开始" onclick="javascript:f_startItem('right_R_Iframe','报告');" />
				&nbsp;<input id="endYsjlItemBtn" type="button" class="bt1" value="结束" onclick="javascript:f_endItem('right_R_Iframe','报告');" />
				&nbsp;<input id="emptyReportItemBtn" type="button" class="bt1" value="清空选择" onclick="javascript:f_clearElementCss('right_R_Iframe', 'input','beginLeftElementName', 'endLeftElementName');" />
				 -->
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div class="mark">单个添加，请点击目标文本框，然后点击按钮“一键添加”即可；批量添加，请点击目标文本框后，按下Ctrl键标记开始，按下Shift键标记结束，然后点击按钮“一键添加”即可。</div>
			</td>
		</tr>
		<tr>
			<td id="tree_big_tb_td_left">
				<fieldset class="l-fieldset" style="height:350px;">
					<legend class="l-legend">
						<div>原始记录</div>
					</legend>
					<iframe name="left_R_Iframe" id="left_R_Iframe" src="app/relation/rtbox_index.jsp?rtCode=<%=ysjl_rtCode%>&rtPage=index1.jsp&position=left&pageCode=1" width="100%" height="100%" scrolling="auto" frameborder="0" border="0" onload="left_R_Iframe.focus()"></iframe>
				</fieldset>	
				
			</td>
			<td id="tree_big_tb_td_right">
				<fieldset class="l-fieldset" style="height:350px;">
					<legend class="l-legend">
						<div>报告</div>
					</legend>
					<iframe name="right_R_Iframe"  id="right_R_Iframe" src="app/relation/rtbox_index.jsp?rtCode=<%=report_rtCode%>&rtPage=index1.jsp&position=right&pageCode=1" width="100%" height="100%" scrolling="auto" frameborder="0" border="0"></iframe>
				</fieldset>				
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top:1%;">
				<div class="form_btn">
					<a href="javascript:void();" onclick="javascript:f_addRelations();">一键添加</a>
					<a href="javascript:void();" onclick="javascript:f_addYsjlItems();">多对一</a>
					<a href="javascript:void();" onclick="javascript:f_clearElementsCss();">清空选择</a>
					<a href="javascript:void();" onclick="javascript:f_checkItems();">查漏</a>
					<a href="javascript:void();" onclick="javascript:save('');">保存</a>
					<a href="javascript:void();" onclick="javascript:save('close');">保存并关闭</a>
					<a href="javascript:void();" onclick="javascript:close();">关闭</a>
				<div>
			</td>
		</tr>
		<tr>
			<td id="tree_big_tb_td_bottom" colspan="2">
				<fieldset class="l-fieldset">
					<legend class="l-legend">
						<div>
							 对应关系明细表
						</div>
					</legend>
					<div id="infos" style="height:400px;overflow:auto;margin-bottom:20px;"></div>
				</fieldset>	
			</td>
		</tr>
	</table>
</form>
</div>
</body>
</html>