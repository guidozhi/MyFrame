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
	String op_type=request.getParameter("op_type");
%>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
<script src="app/pub/office/js/editor.js" type="text/javascript"></script>
<script type="text/javascript" src="pub/bpm/js/util.js"></script>
<script type="text/javascript" src="pub/worktask/js/worktask.js"></script>
<script type="text/javascript">
	var basepath = "${pageContext.request.contextPath}/";
	var toolBar;//工具条
	var beanData = api.data.id;//父窗口的数据
	var use_department = api.data.use_department;//父窗口的数据
	var car_num = api.data.car_num;//父窗口的数据
	var car_logo = api.data.car_logo;//父窗口的数据
	var engine_no = api.data.engine_no;//父窗口的数据
	var car_code = api.data.car_code;//父窗口的数据
	var senrepair_name = api.data.senrepair_name;//父窗口的数据
	var senrepair_date = api.data.senrepair_date;//父窗口的数据
	var repair_item = api.data.repair_item;//父窗口的数据
	var use_department_manager_opinion = api.data.use_department_manager_opinion;//父窗口的数据
	var fleet_manager_opinion = api.data.fleet_manager_opinion;//父窗口的数据
	var department_manager_opinion = api.data.department_manager_opinion;//父窗口的数据
	var type = api.data.type;//父窗口的数据

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
		if("${param.pageStatus}"=="detail"){
			TANGER_OCX_OBJ.SetReadOnly(true);
			TANGER_OCX_OBJ.Statusbar = false;
			TANGER_OCX_OBJ.Toolbars = false;
		}
	}
	
	function initToolBar(){
    	var closeBtn;
    	var printBtn;
    	var checkBtn;
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
		if("${param.op_type}"=="deal"){
			itemArr.push(checkBtn);
		}
		itemArr.push(printBtn);
		itemArr.push(closeBtn);
		toolBar=$("#toolbar").ligerButton({
			items:itemArr
		});
    }
	
	//加载正文
 	function loadBumfDoc(){
		TANGER_OCX_OBJ.ToolBars=true;
		if(beanData != null && type !=null){
			TANGER_OCX_OBJ.OpenFromURL("${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/app/oa/car/carrepairNote.doc");
			setBookMarkValue1("type",api.data.type);
			setBookMarkValue1("use_department",api.data.use_department);
			setBookMarkValue1("car_num",api.data.car_num);
			setBookMarkValue1("car_logo",api.data.car_logo);
			setBookMarkValue1("engine_no",api.data.engine_no);
			setBookMarkValue1("car_code",api.data.car_code);
			setBookMarkValue1("senrepair_name",api.data.senrepair_name);
			setBookMarkValue1("senrepair_date",api.data.senrepair_date);
			setBookMarkValue1("repair_item",api.data.repair_item);
			setBookMarkValue1("use_department_manager_opinion",api.data.use_department_manager_opinion);
			setBookMarkValue1("fleet_manager_opinion",api.data.fleet_manager_opinion);
			setBookMarkValue1("department_manager_opinion",api.data.department_manager_opinion);
		}else{
			TANGER_OCX_OBJ.OpenFromURL("${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/app/oa/car/templete.doc");
		}
	}
	
	function showBB(){
		$("#sssss").show();
	}
	function check(beanData) {
		$('#sssss').hide();
    	var list;
    	var id;
    	var title;
    	var business_id = beanData;
    	$.ajax({
        	url: "equipMaintainAction/queryMainId.do?typeCode=carrepair_flow&id="+business_id,
            type: "POST",
            success: function (resp) {
            	if(resp.success){
            		list = resp.list;
            		id=list[0].id;
        	        title=list[0].title;
            		var config={
                	        width :500,
                	        height : 200,
                	        id:id,
                	        title:title
                	    }
            		// 调用流程的方法
               	    openWorktask(config);
            	}else{
            		$.ligerDialog.error('没有流程数据！');
            	}
            },
            error: function (data,stats) {
                $.ligerDialog.error('提示：' + data.msg);
            }
        });
     }
	/* function check(id){
		$('#sssss').hide();
		top.$.dialog({
			width : 500, 
			height : 200, 
			lock : true, 
			title:"维修申请单审核",
			parent:api,
			content: 'url:app/oa/car/carrepair_note_check.jsp?id='+id,
			data : {"window" : window}
		});
	} */

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