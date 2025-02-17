<%@page import="com.khnt.rbac.impl.bean.Employee"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<%@page import="com.khnt.utils.StringUtil"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head id="main_head">
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<%
	CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
	User uu = (User)curUser.getSysUser();
	Employee e = (Employee)uu.getEmployee();
	String emp_id = e.getId();
	String user_id = uu.getId();
	String uId = SecurityUtil.getSecurityUser().getId();
	String org_code = curUser.getDepartment().getOrgCode();	// 部门编号
	String org_id = curUser.getDepartment().getId();
	String type = request.getParameter("type");
%>
<script type="text/javascript">
	var qmUserConfig = {
		sp_defaults:{columnWidth:0.3,labelAlign:'right',labelSeparator:'',labelWidth:100},	// 默认值，可自定义
		sp_fields : [ 
			<%
				if(org_code.startsWith("jd") || org_code.startsWith("cy") || org_code.startsWith("xxzx")){
					if("04".equals(type)){	// 04：检验员已纠正，部门负责人待确认
						%>
						{name:"error_user_name", compare:"like"},	
						<%
					}
				}
			%>
			{name:"report_sn", compare:"like"}	
		],
		tbar : [ {
			text : '详情',
			id : 'detail',
			click : detail,
			icon : 'detail'
		}
		, '-', {
			text : '查看纠错报告',
			id : 'showReport',
			click : showReport,
			icon : 'detail'
		}
		, '-', {
			text : '不符合纠正',
			id : 'error_user_deal',
			click : error_user_deal,
			icon : 'modify'
		}/*
		, '-', {
			text : '确认',
			id : 'dep_head',
			click : dep_head,
			icon : 'modify'
		}*/
		, '-', { 
			text:'流转过程', 
			id:'turnHistory',
			icon:'follow', 
			click: turnHistory
		}
		],
		listeners : {
			rowClick : function(rowData, rowIndex) {
			},
			rowDblClick : function(rowData, rowIndex) {
				detail();
			},
			selectionChange : function(rowData, rowIndex) {
				selectionChange()
			},
			rowAttrRender : function(rowData, rowid) {
	            var fontColor="black";
	            // 01：质量部已记录，待审核
	            if (rowData.status == '01'){
	            	fontColor="black";
	            }
	            // 02：质量部审核未通过
	            if (rowData.status == '02'){
	            	fontColor="purple";
	            }
	            // 03：质量部审核通过，检验员待处理
	            if (rowData.status == '03'){
	            	fontColor="red";
	            }
	            // 04：检验员已纠正，部门负责人待确认
	            if (rowData.status == '04'){
	            	fontColor="blue";
	            }
	             
	           // 2016-09-02修改，去掉部门负责人确认流程
	           // 2017-08-01修改，增加部门负责人确认流程
	           // 05：部门负责人已确认，质量部待确认
	            if (rowData.status == '05'){
	            	fontColor="orange";
	            } 
	            // 06：质量部已确认整改完成
	            if (rowData.status == '06'){
	            	fontColor="green";
	            }
	            return "style='color:"+fontColor+"'";
			}
		}
	};

	//行选择改变事件
	function selectionChange() {
		var count = Qm.getSelectedCount();//行选择个数
		var status = Qm.getValueByName("status").toString();
		if (count == 0) {
			Qm.setTbar({
				detail : false,
				turnHistory : false,
				//dep_head : false,
				showReport : false,
				error_user_deal : false
			});
		} else if (count == 1) {
			if(status == "03"){
				Qm.setTbar({
					detail : true,
					turnHistory : true,
					//dep_head : false,
					showReport : true,
					error_user_deal:true
				});	
			}else if(status == "04"){
				Qm.setTbar({
					detail : true,
					turnHistory : true,
					dep_head : true,
					//error_user_deal:false
					showReport : true
				});	
			}else{
				Qm.setTbar({
					detail : true,
					turnHistory : true,
					//dep_head : true,
					error_user_deal:false,
					showReport : true
				});	
			}
		} else {
			Qm.setTbar({
				detail : false,
				turnHistory : false,
				//dep_head : false,
				showReport : false,
				error_user_deal : false
			});
		}
	}

	//查看
	function detail() {
		var error_dep_id = Qm.getValueByName("error_dep_id").toString();
		top.$.dialog({
			width : 1000,
			height : 700,
			lock : true,
			title : "详情",
			data : {
				"window" : window
			},//把当前页面窗口传入下一个窗口，以便调用。
			content : 'url:app/report/report_error_detail.jsp?status=detail&org_id=<%=org_id%>&id='+ Qm.getValueByName("id")
		});
	}
	
	// 不符合纠正
	function error_user_deal(){
		// 不符合报告ID
		var id = Qm.getValueByName("id").toString();
		var error_dep_id = Qm.getValueByName("error_dep_id").toString();
		var data_status = Qm.getValueByName("data_status").toString();
        if(data_status.indexOf("审核通过") == -1){
        	$.ligerDialog.warn("该不符合报告已纠正，不能重复操作，请核查！如需重新纠正，请填写“检验报告不符合纠正流转表”！");
        	return;
        }
        // 不符合项目（0：报告 1：其他）其中报告需要填写不符合纠正流转表，其他不需要
        var error_category = Qm.getValueByName("error_category").toString();
        if("0" == error_category){
        	top.$.dialog({
    			width : 1000,
    			height : 600,
    			lock : true,
    			title : "不符合纠正",
    			data : {
    				"window" : window
    			},
    			content : 'url:app/report/report_error_record_detail2.jsp?status=modify&type=&confirm_flag=1&org_id='+error_dep_id+'&report_error_id=' + id
    		});
        }else{
        	top.$.dialog({
    			width: 1000,
    			height: 700,
    			lock:true,
    			title:"不符合纠正",
    			data: {window:window},
    			content: 'url:app/report/report_error_detail.jsp?status=modify&type=03&error_category='+error_category+'&org_id='+error_dep_id+'&id='+id
    		});
        }
	}
	
	// 流转过程
	function turnHistory(){	
		top.$.dialog({
	   		width : 400, 
	   		height : 700, 
	   		lock : true, 
	   		title: "流程卡",
	   		content: 'url:report/error/getFlowStep.do?id='+Qm.getValueByName("id"),
	   		data : {"window" : window}
	   	});
	}
	
	// 查看报告
	function showReport(){
		var id = Qm.getValueByName("id").toString();
		top.$.dialog({
			width: 1000,
			height: 620,
			lock:true,
			title:"查看报告",
			data: {window:window},
			content: 'url:app/report/report_error_info_list.jsp?id='+id
		});
	}


	function refreshGrid() {
		Qm.refreshGrid();
	}
</script>
</head>
<body>
	<div class="item-tm" id="titleElement">
	    <div class="l-page-title">
			<div class="l-page-title-note">提示：列表数据项
				<font color="black">“黑色”</font>代表质量部待审核；
				<font color="purple">“紫色”</font>代表质量部审核未通过，流程结束；
				<font color="red">“红色”</font>代表质量部审核通过，检验员待处理；
				<font color="blue">“蓝色”</font>代表检验员已纠正，部门负责人待确认；
				<!-- 2016-09-02孟凡昊提出修改，去掉责任部门负责人确认流程 -->
				<!-- 2017-08-01谢方提出修改，增加责任部门负责人确认流程 -->
				<font color="orange">“桔色”</font>代表部门负责人已确认，质量部待确认； 
				<font color="green">“绿色”</font>代表质量部已确认整改完成。
			</div>
		</div>
	</div>
	<qm:qm pageid="report_error" script="false">
		<%
			if(!org_code.startsWith("ziliang") && !org_code.startsWith("xxzx")){
				
				if(StringUtil.isNotEmpty(type)){
					if("03".equals(type)){
						%>
						<qm:param name="error_user_id" value="<%=user_id%>" compare="like" />
						<%
					}else if("04".equals(type)){
						%>
						<qm:param name="error_dep_id" value="<%=org_id%>" compare="like" />
						<%
					}
				}else{
					%>
					<qm:param name="error_user_id" value="<%=user_id%>" compare="like" />
					<%
				}
			}
		%>
		
	</qm:qm>
	<script type="text/javascript">
	// 根据 sql或码表名称获得Json格式数据
	Qm.config.columnsInfo.error_dep_id.binddata=<u:dict sql="select id code, ORG_NAME text from SYS_ORG where ORG_CODE like 'jd%' or ORG_CODE like 'cy%' order by orders "></u:dict>;
	</script>
</body>
</html>
