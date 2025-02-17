<%@page import="com.khnt.base.Factory"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<head pageStatus="${param.status}">
<%@ include file="/k/kui-base-form.jsp"%>
<%
	String type = request.getParameter("type");
	String op_name = "04".equals(type)?"审核":"签发";

%>
<script type="text/javascript" src="rtbox/app/templates/rtbox.js"></script>
<script type="text/javascript">
var onekey = false ;	//是否允许一键操作
var autoSub = false ;	//是否自动提交
var infon = 0;
var pageStatus="${param.status}"; 
var bigClassify="${param.bigClassify}";
var little_type = "${param.device_sort}";
var checkOk = {};
	var navtab;
	$(function () {
		
			bar = [ { text : '报告<%=op_name%>', icon : 'save', id:'save', click : save } ,
			        { text : '关闭', icon : 'cancel', click : close }  ];
		
		var id = "${param.ids}";
		
		var ids = id.split(",");
		var report_sn = api.data.report_sn;
		var fk_report_ids = api.data.fk_report_ids;
		var codes = api.data.codes;
		for (var i = 0; i < ids.length; i++) {
			var _param = "";
			_param += "&fk_report_id=" + fk_report_ids[i]+"&code="+codes[i]+"&id="+ids[i];
			
			$("#mainTab").append('<div title="<span id=\'title'+i+'\'>'+report_sn[i]+'</span>"  lselected ="true" tabid="Tab'+i+'">'
					+'<iframe src="'+'rtbox/app/templates/' + codes[i]
					+ '/index.jsp?code='+codes[i]+'&pageStatus=detail&status=modify&singleAudit=1' + _param+'" style="border: 0px;"></iframe>'
					+'</div>')
			
		} 
		
		navtab = $(".navtab").ligerTab({
            //  onAfterSelectTabItem：这个方法是选中tab后进行的操作
            onAfterSelectTabItem : function (tabid)     {
            	//获取选项卡序号
            	infon = tabid.substring(3,tabid.length);
            	//如果报告已经审核过则按钮不可用
            	if(checkOk[infon]!=undefined&&checkOk[infon]==true){
            		disableBtn();
            	}
            	
            },  onBeforeSelectTabItem : function (tabid)     {
            	return;
            	
            	//alert("11---"+tabid.substring(3,tabid.length))
            	
            }}); 
		
		//navtab.removeTabItem("Tab1")
		//var manage = $("#mainTab").ligerTab();
		//manage.selectTabItem("Tab1");
	  $("#formObj").initForm({    //参数设置示例
	       toolbar:bar,
	        getSuccess:function(res){
	        	
	        }
	        
	    });
	})

	
	//添加问题
	function addError(){
		alert("此功能待完善，考虑用以前的版本还是用悬浮框！")
		//right.openError();
	}
	//关闭
	function close(url)
	{	
		api.close();
	}
	//保存
	function save(url)
	{
		
		share.data("isBatch",true);
		//打开提交页面
		doSub();
	}
	

	//禁用一键操作按钮
	function disableallBtn(){
		$("#save").attr("disabled",true);
		$("#all").attr("disabled",true);
		
	}
	function enableallBtn(){
		$("#save").attr("disabled",false);
		$("#all").attr("disabled",false);
		
	}
	
	//禁用操作按钮
	function disableBtn(){
		$("#save").attr("disabled",true);
	}
	//启用操作按钮
	function ableBtn(){
		$("#save").attr("disabled",false);
	}
	function showBB(){
		enableallBtn();
	}
	
	//报告校核页面
	function doSub(){
		disableBtn();
		var data = new Object();
		
		var ids = "${param.ids}".split(",");
		//var id=ids[infon];
		//var infoId = ids[infon];
		//var activityId = api.data.activityId[infon];
		
		
		data.acc_id = api.data.activityId.toString();
		data.flow_num = '${param.flow_num}';
		data.type = '${param.type}';
		data.device_type = '${param.device_type}';
		//alert(data.device_type+"--------"+data.type)
		data.device_sort_code = '${param.device_sort_code}';
		data.check_flow = '${param.check_flow}';
		data.org_id = '${param.org_id}';
		data.ids = "${param.ids}";
		data.opid = '';
		$.post("report/query/getTime.do",{id:ids[0]},function(res){
			//取时间
			var enter_time = res["data"].enter_time==null?'':res["data"].enter_time;
			var advance_time = res["data"].advance_time==null?'':res["data"].advance_time;
			var confirm_date = res["data"].confirm_date==null?'':res["data"].confirm_date;
			var examine_date = res["data"].examine_date==null?'':res["data"].examine_date;
			
			data.advance_time = advance_time;
			data.check_time = examine_date;
			data.enter_time = enter_time;
			data.op_type = '批量'; 	// 操作方式
			data.isBatch = '1'; 	// 1：批量操作
			saveOnly(data);
		})
	}

	function saveOnly(data){
		$('#sssss').hide();
		if(data.ids=="null" || data.acc_id=="null"){
			alert('请先点击您想要单独<%=op_name%>的报告！ ');
			showBB();
			return;
		}
		if(checkOk[infon]){
			alert('您选择的报告已经<%=op_name%>！ ');
			return;
		}

		top.$.dialog({
			width : 600, 
			height : 500, 
			lock : true, 
			title:data.op_type+'<%=op_name%>',
			parent:api,
			content: 'url:app/flow/report/page/report_batch_check.jsp?acc_id='+data.acc_id+'&flow_num='+data.flow_num+
					'&type=${param.type}&device_type=${param.device_type}&device_sort_code=${param.device_sort_code}&check_flow=${param.check_flow}&ids='+
					data.ids+'&isBatch='+data.isBatch+'&opid='+data.opid+'&advance_time='+data.advance_time
					+'&check_time='+data.check_time+'&enter_time='+data.enter_time+'&org_id='+data.org_id,
			data : {"window" : window}
		});
		
	}
	
	
	
	function auditOk(){
		var img = "<img alt=' ' src='k/kui/images/icons/16/accept.png' border='0'>";
		$("#title"+infon).prepend(img);
		checkOk[infon]= true;
	}
	
</script>
</head>
<body>
	<form id="formObj" action="" getAction="">
		<div class="navtab" id="mainTab">
			<%-- <c:forEach items="${param.ids.split(',')}" var="ids">
				<div title="${ids}"  lselected ="true" id="${ids}">
						</div>
			</c:forEach> --%>
		</div>
	</form>
</body>
	
</html>
