<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<%@include file="/k/kui-base-form.jsp"%>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

<script src="app/pub/office/js/editor.js" type="text/javascript"></script>
<script type="text/javascript" src="app/payment/payment_list.js"></script>
<script type="text/javascript">
	var basepath = "${pageContext.request.contextPath}/";
	var toolBar;//工具条
	var beanData = api.data.bean;//父窗口的数据
	var op_user_name = api.data.op_user_name;//父窗口的数据

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
		var printBtn;
		var closeBtn;
		printBtn={
			id: "print",
			text: "打印",
			icon:"print",
			click: function(){						
				doPrint();
				//if("2" == "${param.isPrint}"){
				//	api.data.window.api.close();
				//	api.data.window.api.data.window.submitAction();
				//}
				api.close();
				return true;
			}
		};
		closeBtn={
			id: "close",
			text: "关闭",
			icon:"close",
			click: function(){
				//if("2" == "${param.isPrint}"){
				//	api.data.window.api.close();
				//	api.data.window.api.data.window.submitAction();
				//}
				api.close();
				return true;
			}
		};
		var itemArr=new Array();
		itemArr.push(printBtn);
		itemArr.push(closeBtn);
		toolBar=$("#toolbar").ligerButton({
			items:itemArr
		});
	}
	
	//加载正文
	function loadBumfDoc(){
		TANGER_OCX_OBJ.ToolBars=true;
		
		if(beanData != null){
			TANGER_OCX_OBJ.OpenFromURL("/app/flow/01zzjd_get.doc");	
			setBookMarkValue1("pulldown_op", beanData.pulldown_op);	// 领取人
			var datas = beanData["inspectionInfoDTOList"];
			var num = 0;
			for(var i in datas){
				num ++;
				if(num == 1){
					setBookMarkValue1("report_com_name", datas[i]["report_com_name"]);		// 制造单位（报告书使用单位）
				}
			}
			setBookMarkValue1("count", num);	// 报告数量
			setBookMarkValue1("report_sn", beanData.report_sn);	// 报告书编号
			setBookMarkValue1("linkmode", beanData.linkmode);	// 领取人联系电话
			setBookMarkValue1("pulldown_time", beanData.pulldown_time.substring(0,10));	// 领取日期
			setBookMarkValue1("op_user_name", op_user_name);	// 工作人员
		}else{
			TANGER_OCX_OBJ.OpenFromURL("/app/flow/templete.doc");
		}	
		//saved属性用来判断文档是否被修改过,文档打开的时候设置成ture,当文档被修改,自动被设置为false,该属性由office提供.
		//TANGER_OCX_OBJ.activeDocument.saved=true;
	}

	function showBB(){
		$("#sssss").show();
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