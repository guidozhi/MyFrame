var treeManager = null;
var menu;
var menu2;// 只有删除
var actionNode;
var saveFlag = false;
$(function() {
	menu = $.ligerMenu({
		top : 100,
		left : 100,
		width : 120,
		items : [ {
			text : '复制',
			click : copy,
			icon : 'add'
		},{
			text : '预览',
			click : previewSingle,
			icon : 'preview'
		},{
			text : '编辑源码',
			click : editCode,
			icon : 'modify'
		},{
			text : '检查重复命名',
			click : checkSameName,
			icon : 'preview'
		},{
			text : '查看base命名',
			click : checkAllBaseName,
			icon : 'preview'
		}
		

		// , {
		// text : '修改',
		// click : edit
		// }, {
		// line : true
		// },
		// {
		// text : '删除',
		// click : del
		// }
		]
	});

	menu2 = $.ligerMenu({
		top : 100,
		left : 100,
		width : 120,
		items : [ {
			text : '删除',
			click : del
		},{
			text : '预览',
			click : previewSingle,
			icon : 'preview'
		}
 ]
	});

	// 页面布局
	$("#layout1").ligerLayout({
		leftWidth : 190,
		// topHeight: 30,
		space : 3,
		allowTopResize : false
	});

	createTree();
});

function createTree() {
	
	//var url = "com/rt/dir/getDir.do?code=" + rt_code + "&id=" + info_id;
	var url = "";
	
	if(info_id!=undefined&&info_id!=""&info_id!=null&&info_id!="null"){
		url = "inspectRecordDirAction/getRecordDir.do?code=" + rt_code + "&id=" + info_id;
	}else{
		url = "com/rt/page/getDir.do?id=" + rtboxId+"&code=" + rt_code + "&infoId=" + info_id;
	}
	
	treeManager = $("#tree1").ligerTree({
		checkbox : false,
		selectCancel : false,
		idFieldName : "code",
		textFieldName : "name",
		nodeWidth : 200,
		onSuccess:onSuccess,
		url : url,
		/*onSelect : function(node) {
			actionNode = node;
			var code = node.data.code;
			if (code == "root") {
				return false;
			}
			openRt(node.data.pageName);
		},*/
		onClick : function(node) {
			actionNode = node;
			var code = node.data.code;
			if (code == "root") {
				return false;
			}
			openRt(node.data.pageName);
		},
		onCheck : function(node, checked) {
		},
		onContextmenu : function(node, e) {
			actionNode = node;
			var testCode = node.data.code;
			var pageName = node.data.pageName;
			//if (testCode.indexOf("_") > 0) {
			if(pageName.indexOf("code_ext")>0){
				menu2.show({
					top : e.pageY,
					left : e.pageX
				});
			} else {
				menu.show({
					top : e.pageY,
					left : e.pageX
				});
			}

			return false;
		}
	});
	openRt("index1.html");
}

function onSuccess(){
	$.post("com/rt/dir/getErrorPage.do?id="+info_id,function(res){
		if(res.success){
			if(res.errorPage!=""){
				var nodes = treeManager.data[0].children;
				var l = $(".l-children").children("li").length;
				var lis = $(".l-children").children("li");
				for (var i = 0; i < l; i++) {
					var treedataindex = $(lis[i]).attr("treedataindex");
					if(i!=0){
						var text = $($($(lis[i]).children("div")).children("span")).text();
						$($($(lis[i]).children("div")).children("span")).text(i+" "+text);
					}
					
					if((","+res.errorPage+",").indexOf(","+nodes[i].code+",")>=0){
						
						$($($(lis[i]).children("div")).children("span")).css("color","red");
					}
				}
			}else{
				var nodes = treeManager.data[0].children;
				var l = $(".l-children").children("li").length;
				var lis = $(".l-children").children("li");
				for (var i = 0; i < l; i++) {
					var treedataindex = $(lis[i]).attr("treedataindex");
					if(i!=0){
						var text = $($($(lis[i]).children("div")).children("span")).text();
						$($($(lis[i]).children("div")).children("span")).text(i+" "+text);
					}
				}
			}
		}
	})
	
}

function itemclick(item) {
	notice("TBL00005");
	alert(actionNodeID + " | " + item.text);
	// notice("test3");
	// alert("保存");
}

function edit() {
	if (!info_id) {
		comm.info("没有发现业务主键");
		return;
	}

	var d = dialog({
		title : '报告名称',
		content : '<input id="newName" autofocus value="'
				+ actionNode.data.name + '"/>',
		ok : function() {
			var newName = document.getElementById("newName");
			if (!newName.value) {
				newName.focus();
				return false;
			}

			treeManager.update(actionNode.target, {
				name : newName.value
			});
		},
		okValue : '确定'
	});
	d.showModal();
}

function copy(item) {
	if (!info_id) {
		comm.info("没有发现业务主键");
		return;
	}

	var d = dialog({
		title : '系统提示',
		content : '确认复制吗?',
		okValue : '确定',
		ok : function() {
			var nodes = [];
			var actionNodeData = actionNode.data;
			var length = 1;
			if (actionNodeData.children) {
				length = actionNodeData.children.length + 1;
			}
			var code = actionNodeData.code + "_" + length;
			var pageName = actionNodeData.pageName + "?code_ext=" + code;
			var name = actionNodeData.name + length;
			nodes.push({
				name : name,
				code : code,
				pageName : pageName
			});
			treeManager.append(actionNode.target, nodes);
			treeManager.selectNode(code);
			var data = treeManager.getData();
			comm.Loading("loading", "正在生成页面请稍候...");
			$.post("com/rt/dir/setDir.do", {
				rtCode : rt_code,
				rtDirJson : JSON.stringify(data),
				id : info_id,
				oldCode : actionNodeData.code,
				newCode : code,
				setType : 'copyPage'
			}, function(res) {
				if (res.success) {
					comm.Content("操作成功");
					comm.LoadEnd(2);
				} else {
					comm.LoadEnd();
					alert("操作失败");
					location.reload();
				}
			}, "json");

		},
		cancelValue : '取消',
		cancel : function() {
		}
	});
	d.show();
}

function del(item) {
	if (!info_id) {
		comm.info("没有发现业务主键");
		return;
	}
	var actionNodeData = actionNode.data;
	var parentNode = treeManager.getParent(actionNode);
	// treeManager.remove(actionNode.target);
	treeManager.remove(actionNode);
	// treeManager.remove(actionNode.data);
	treeManager.selectNode(parentNode);
	var data = treeManager.getData();
	comm.Loading("loading", "正在删除分页请稍候...");
	$.post("com/rt/dir/setDir.do", {
		rtCode : rt_code,
		rtDirJson : JSON.stringify(data),
		id : info_id,
		oldCode : actionNodeData.code,
		setType : 'delPage'
	}, function(res) {
		if (res.success) {
			comm.Content("操作成功");
			comm.LoadEnd(2);
		} else {
			comm.LoadEnd();
			alert("操作失败");
			location.reload();
		}
	}, "json");
}

function save() {
	var data = $("#formObj").getValues();
	$.ajax({
		url : "com/rt/page/saveMap.do",
		type : "POST",
		datatype : "json",
		contentType : "application/json; charset=utf-8",
		data : $.ligerui.toJSON(transFormDataToJSON(data)),
		success : function(data, stats) {
			if (data.success) {
				alert("成功");
			} else {
				alert("失败");
			}
		},
		error : function(data) {
			alert("失败");
		}
	});
}

function setDir(callback) {
	comm.Loading("loading", "正在生成页面请稍候...");
	$.post("com/rt/dir/setDir.do", {
		code : "${param.code}",
		id : info_id
	}, function(res) {
		if (res.success) {
			eval(callback);
			comm.Content("操作成功");
			comm.LoadEnd(2);
		} else {
			comm.Content("操作失败功");
			comm.LoadEnd(2);
		}
	}, "json");
}

function openRt(pageName) {
	var pageCode = "1";
	if(actionNode!=null){
		pageCode = actionNode.data.code;
	}
	try {
		//模板设计界面使用改变状态
		if(basePath.indexOf("visual")!=-1){
			changePage();
		}
	} catch (e) {
		// TODO: handle exception
	}
	
	var src = basePath+"show_right.jsp?code="+rt_code +"&pageName="+pageName;
	if (src.indexOf("?") > 0) {
		if(!relColumn||relColumn.length<=0){
			src = src + '&fk_report_id=' + fk_report_id;
		}else{
			src = src + '&' + relColumn;
		}
		
	} else {
		if(!relColumn||relColumn.length<=0){
			src = src + '?fk_report_id=' + fk_report_id;
		}else{
			src = src + '?' + relColumn;
		}
	}
	
	if(input!=""){
		src = src + '&input=' + input;
	}
	src = src +'&' + relColumn;
	if(info_id!=""){
		src = src + '&info_id=' + info_id;
	}
	if(pageStatus!=""){
		src = src + '&pageStatus=' + pageStatus;
	}
	if(check_op!=""){
		src = src + '&check_op='+check_op;
	}
	src = src + '&pageCode='+pageCode;
	if(pageCode.indexOf("__")!=-1){
		src = src + '&code_ext ='+pageCode.split("__")[1];
	}
	if(rtPageId!=""){
		src = src + '&rtPageId='+rtPageId;
	}
	
	if((pageStatus!=""||pageStatus!=undefined)&&$("#rightFrame").attr("src")!=""&&status!="detail"){


		//审核时有标记和标注需要保存
		//切换页面时自动保存
		//try {
		if(input!=undefined&&input!=''&&saveFlag){
			var saveResult = rightFrame.submitForm();
			if(saveResult){
				$("#rightFrame").attr("src", src);
			}else{
				$.ligerDialog.alert("保存失败，请检查填写的数据！", "提示");
			}
		}else{

			$("#rightFrame").attr("src", src);
		}
		//} catch (e) {
			// TODO: handle exception
		//}
		
	}else{

		$("#rightFrame").attr("src", src);
	}
}
// 目录重置
function dir() {
	var data = treeManager.getData();
	data = data[0]["children"];
	var pageCode = "";
	for (var i = 0; i < data.length; i++) {
		if (i != 0) {
			pageCode += ",";
		}
		pageCode += data[i]["code"];
	}

	top.$.dialog({
		id : 'chooseDir',
		width : 530,
		height : 600,
		lock : true,
		ok : function() {
			var flag = this.iframe.contentWindow.getSelectResult();
			if (flag) {
				window.location.reload();
				return true;
			}
			return false;
		},
		cancel : true,
		title : "选择目录",
		content : "url:rtbox/app/templates/default/dir.jsp?code=" + rt_code
				+ "&pageCode=" + pageCode + "&info_id=" + info_id
	});
}


function previewSingle(item) {
	rightFrame.printPreviewForm();
	return;
	var actionNodeData = actionNode.data;
	comm.Loading("loading", "正在生成页面请稍候...");
	//com/rt/dir/previewSingle.do 业务相关独立出来 pingZhou 20171212
	$.post("reportItemValueAction/previewSingle.do", {
		rtCode : rt_code,
		id : info_id,
		code : actionNodeData.code
	}, function(res) {
		if (res.success) {
			//comm.Content("操作成功");
			comm.LoadEnd(2);
			var data = res.data;
			//var outPutDocDirPath = data.outPutDocDirPath;
			//alert(data);
			top.$.dialog({
				width : 1000,
				height : 800,
				lock : true,
				title : "预览",
				content : "url:rtbox/app/templates/default/single_preview.jsp",
				data:{"window":window,"path":data}
			}).max();
		} else {
			comm.LoadEnd();
			alert("操作失败");
//			location.reload();
		}
	}, "json");
 
}

function editCode(item) {
	var actionNodeData = actionNode.data;
	var pageName = actionNodeData.pageName;
	var path = "rtbox/app/recordTemplates/"+rt_code +"/"+pageName;
	window.open("http://192.168.0.110:8081/app/tools/tpl_index.jsp?filepath="+path)
	
 
}

function setH(maxheight){
	
	$("#rightFrame").css("height",maxheight);
}

function checkSameName(){
	rightFrame.checkSameName();
}


function checkAllBaseName(){
	rightFrame.checkAllBaseName();
}