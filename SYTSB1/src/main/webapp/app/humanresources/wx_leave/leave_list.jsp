<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://khnt.com/tags/qm" prefix="q" %>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<link rel="stylesheet" href="app/k/km/lib/kh-mobile.css"/>
<script src="app/k/jqm/jquery2.js"></script>
<script src="app/k/km/lib/kh-mobile.js"></script>
<script src="app/k/km/lib/kh-mobile-list.js"></script>

<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/k/km/lib/app-end.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/pub/bpm/js/util.js"></script>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no"/>
<link rel="stylesheet" href="app/k/jqm/skins/default.css"/>
<script src="app/k/jqm/a-main.js" type="text/javascript"></script>
<script src="app/k/km/lib/kh-mobile-list.js"></script>
	<%
	CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
	User uu = (User)curUser.getSysUser();
	com.khnt.rbac.impl.bean.Employee e = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
	String userId=e.getId();//员工ID
	String uId = SecurityUtil.getSecurityUser().getId();//用户ID
	String depId = e.getOrg().getOrgCode();
%>
<% String id=request.getParameter("id");
    if(id==null){
    	id=(String)session.getAttribute("id");
    }
%>
<script type="text/javascript">
//Qm.config.pageid="qm_mb_01";


Qm.listClick=function(e,sThis){
	//alert(sThis.find('input[name="id"]').val());
	var selectedId = sThis.find('input[name="id"]').val() ;
	location.href="${pageContext.request.contextPath}/app/humanresources/wx_leave/leave_add.jsp?leave_id="+selectedId+"&step=detail&allow=1";
	
};
$(function(){
	//返回
	$("#next1").click(function() {
		location.href="${pageContext.request.contextPath}/app/humanresources/wx_leave/leave_index.jsp";
	});
});
</script>
<style>
.wrapper{ margin-top:28%;}

</style>

</head>
<body>
<%StringBuffer sql = new StringBuffer();
		String sql1="select b.* from TJY2_RL_LEAVE b where people_id = '" + userId+"'";
		String departmentid=SecurityUtil.getSecurityUser().getDepartment().getId();
	%>
	<!-- 部门负责人 -->
    <sec:authorize access="hasRole('TJY2_BMFZR')">
    	<%sql1="select b.* from TJY2_RL_LEAVE b where people_id = '" + userId +"' union "+
    			"select b.* from TJY2_RL_LEAVE b where b.dep_id = '" + departmentid +"' and (APPLY_STATUS = 'SPTG' or APPLY_STATUS = 'YXJ')";%>
		<%System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@部门负责人@@@@@@@@@@@@@@@@@@@@@@@@！");
		System.out.println(sql1.toString());%>
	</sec:authorize>
	<!-- 人事部门负责人 -->
	<sec:authorize access="hasRole('TJY2_RL_RSSH')">
		<%sql1="select b.* from TJY2_RL_LEAVE b where people_id = '" + userId +"' union "+
    			"select b.* from TJY2_RL_LEAVE b where (APPLY_STATUS = 'SPTG' or APPLY_STATUS = 'YXJ')";%>
		<%System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@人事部门负责人@@@@@@@@@@@@@@@@@@@@@@@@！");
		System.out.println(sql1.toString());%>
	</sec:authorize>
	<!-- 分管领导 -->
	<sec:authorize access="hasRole('TJY2_RL_FGLDSH')">
	<%sql1="select b.* from TJY2_RL_LEAVE b where people_id = '" + userId +"' union "+
    			"select b.* from TJY2_RL_LEAVE b where (APPLY_STATUS = 'SPTG' or APPLY_STATUS = 'YXJ')"+
				"and instr((select d.fk_sys_org_id from TJY2_RL_ORGID_LEADERID d "+
    					"where d.fk_rl_emplpyee_id='"+userId+"'), b.dep_id) > 0";
    	if(e.getName().equals("孙宇艺")){
    		sql1="select b.* from TJY2_RL_LEAVE b where people_id = '" + userId +"' union "+
    				"select b.* from TJY2_RL_LEAVE b where (APPLY_STATUS = 'SPTG' or APPLY_STATUS = 'YXJ') and b.dep_id='100025' union "+
        			"select b.* from TJY2_RL_LEAVE b where (APPLY_STATUS = 'SPTG' or APPLY_STATUS = 'YXJ')"+
    				"and instr((select d.fk_sys_org_id from TJY2_RL_ORGID_LEADERID d "+
        					"where d.fk_rl_emplpyee_id='"+userId+"'), b.dep_id) > 0";
    	}
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@分管领导@@@@@@@@@@@@@@@@@@@@@@@@！");
		System.out.println(sql1.toString());
		%>
	</sec:authorize>
	<!-- 院长 -->
	<sec:authorize access="hasRole('TJY2_LEADER')">
		<%sql1="select b.* from TJY2_RL_LEAVE b where people_id = '" + userId +"' union "+
    			"select b.* from TJY2_RL_LEAVE b where (APPLY_STATUS = 'SPTG' or APPLY_STATUS = 'YXJ')";%>
		<%System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@院长 @@@@@@@@@@@@@@@@@@@@@@@@！");
		System.out.println(sql1.toString());%>
	</sec:authorize>
	<!-- 销假权限 -->
	<sec:authorize access="hasRole('TJY2_RL_XIAOJIA')">
		<%sql1="select b.* from TJY2_RL_LEAVE b where people_id = '" + userId+"'";%>
		<%System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@销假权限 @@@@@@@@@@@@@@@@@@@@@@@@！");
		System.out.println(sql1.toString());%>
	</sec:authorize>
	<!-- 没有流程处理权限 -->
    <sec:authorize access="!hasRole('TJY2_RL_CLANKJ')">
    	<%sql1="select b.* from TJY2_RL_LEAVE b where people_id = '" + userId+"'";%>
		<%System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@没有流程处理权限@@@@@@@@@@@@@@@@@@@@@@@@！");%>
	</sec:authorize>
<div  id="a-index" class="a-index">
	<div class="header">
		<div class="bg"></div>
		<div class="logo"></div>
		<div class="user">
			<h1>四川省特检院</h1>
			<h2>请休假-申请列表</h2>
		</div>
	</div>
</div>
	
	<div >
		<div id="wrapper" class="wrapper">

			<div class="container">
				<div class="page-panel" id="m-page-panel">
					<div class="km-list">
						<ul id="__qm_list" class="qm-list">
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="bt1">
		<div class="text-center row">
			<div class=""><a id="next1" class="button button-block button-rounded button-primary">返回</a></div>
		</div>
	</div>
<q:km pageid="TJY2_RL_LEAVE_M" sql="<%=sql1.toString() %>">
</q:km>
</html>
