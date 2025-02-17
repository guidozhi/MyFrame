<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://khnt.com/tags/qm" prefix="q" %>
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
	String phone = (String)session.getAttribute("Phone");
	String name = (String)session.getAttribute("Name");
	String accessToken = (String)session.getAttribute("AccessToken");
	%>
<%-- <%@include file="/k/jqm/mobile-base.jsp" %> --%>
<script type="text/javascript">
var qmUserConfig = {
		sp_fields:[
				     {name: "emp_name", compare: "like", value : ""},
				     {name: "emp_sex", compare: "like", value : ""},
				     {name: "work_department_name", compare: "like", value : ""}
		] 
};
/* function hideTitle(){
	window.KHANJS.titleShow("null");
} */
var isClick;
$(function(){
	$.ajax({
		url : "${pageContext.request.contextPath}/employeeBaseAction/empByPhone.do?phone="+'<%=phone%>',
		type : "post",
		datatype : "json",
		contentType: "application/json; charset=utf-8",
      success : function (data) {
      	if(data.success){
      		var employeeBase = data.employeeBase;
      		var workDepartment = employeeBase.workDepartment;
      		/* var workDepartment = "100041"; */
      		if(workDepartment == "100041" || workDepartment == "100029"){
      			isClick = true;
      		}else{
      			isClick = false;
      		}
      	}else{
      		dialogShow(data.msg, 300, 100);
      	}
      }
	});
}) 
Qm.listClick=function(e,sThis){
	if(isClick == true){
		var selectedId = sThis.find('input[name="id"]').val() ;
		location.href="${pageContext.request.contextPath}/app/weixin/rs/rs_detail_1.jsp?id="+selectedId;
	}else{
		dAlert("您无权查看职工详细信息！");
	}
}
</script>
<style>
.wrapper{ margin-top:28%;}

</style>

</head>
<body>
<div  id="a-index" class="a-index">
	<div class="header">
		<div class="bg"></div>
		<div class="logo"></div>
		<div class="user">
			<h1>四川省特检院</h1>
			<h2>职工档案-职工列表</h2>
		</div>
	</div>
</div>
	
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
<q:km pageid="TJY2_RL_EMPLOYEE_MD">
<q:param name="emp_status" value="(4,3)" compare="in"/>
</q:km>
</body>
</html>



