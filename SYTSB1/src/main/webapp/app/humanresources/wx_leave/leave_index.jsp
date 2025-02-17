<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no"/>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<link rel="stylesheet" href="app/k/jqm/skins/default.css"/>
<script type="text/javascript" charset="utf-8" src="app/k/jqm/jquery2.js"></script>
<script type="text/javascript" charset="utf-8" src="app/k/jqm/jquery.mobile.js"></script>
<script src="app/k/jqm/a-main.js" type="text/javascript"></script>
	<script type="text/javascript">
	<%
	CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
	User uu = (User)curUser.getSysUser();
	com.khnt.rbac.impl.bean.Employee e = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
	String userId=e.getId();//员工ID
	String uId = SecurityUtil.getSecurityUser().getId();//用户ID
%>
	</script>
	<script type="text/javascript">
	function tiao(href){
		var allow = "0";
    	$.ajax({
        	url: 'employeeBaseAction/getWorkTitle1.do?id=<%=userId%>',
            type: "POST",
            datatype: "json",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
            	var workTitle=data.workTitle;
            	if(workTitle!=""&&workTitle!=null&&workTitle!="undefined"){
            		if(workTitle.indexOf("部长")>=0 || workTitle.indexOf("副总工")>=0 || workTitle.indexOf("主持工作")>=0 || workTitle.indexOf("主任")>=0 || workTitle.indexOf("助理")>=0 || workTitle.indexOf("院长")>=0 ){
                    		allow="1";
                    	}
            	}
            	location.href=href+"&allow="+allow;
            }
        });
		}
	</script>
</head>
<body>
<div data-role="page" id="a-index" class="a-index">
	<div data-role="header">
		<div class="bg"></div>
		<div class="logo"></div>
		<div class="user">
			<h1>四川省特检院</h1>
			<h2>请休假-请休假管理</h2>
			
		</div>
	</div>
	<div data-role="content">
		<div id="a-guide-menu" class="a-guide-menu">
			 <!--<div class="a-menu-nav" id="a-menu-nav"><div class="d-big" id="aMenu_big_nav"><a class="a">菜单主页 &gt; </a></div></div>--> 
			<div class="a-menu">
				<div id="a-menu-loading" class="a-menu-loading" style="display: none;">loading</div>
				<div id="app-menu" class="app-menu"></div>
			</div>
			<div id="a_menu_item_an_ywbl" class="bar bar02">
					<div class="item" onclick="tiao('${pageContext.request.contextPath}/app/humanresources/wx_leave/leave_add.jsp?id=<%=userId%>&step=add');">
						<div id="aMenu_wb_new" class="a" href="javascript:void(0);"
							menuid="wb_new" url="">
							<div class="icon clr_1" style="background-color:#EE00EE;">
								<img src="app/k/jqm/skins/default/images/app-ytmz/sq_01.png" alt="">
							</div>
							<div class="bartxt">请休假申请</div>
						</div>
					</div>
					<div class="item" onclick="tiao('${pageContext.request.contextPath}/app/humanresources/wx_leave/leave_list.jsp?id=<%=userId%>');">
						<div id="aMenu_wb_new" class="a" href="javascript:void(0);"
							menuid="wb_new" url="">
							<div class="icon clr_1" style="background-color:#EEB422;">
								<img src="app/k/jqm/skins/default/images/app-ytmz/lb_01.png" alt="">
							</div>
							<div class="bartxt">请休假列表</div>
						</div>
					</div>
					<sec:authorize ifAnyGranted="TJY2_BMFZR,TJY2_RL_RSSH,TJY2_RL_FGLDSH,TJY2_LEADER,TJY2_RL_XIAOJIA,sys_administrate">
					 <div class="item" onclick="tiao('${pageContext.request.contextPath}/app/humanresources/wx_leave/leave_pending.jsp?id=<%=uId%>');">
						<div id="aMenu_wb_new" class="a" href="javascript:void(0);"
							menuid="wb_new" url="">
							<div class="icon clr_1" style="background-color:#2984FF;">
								<img src="app/k/jqm/skins/default/images/app-ytmz/jd_01.png" alt="">
							</div>
							<div class="bartxt">待处理列表</div>
						</div>
					</div>
					</sec:authorize>
				</div>
		</div>
	</div>
	<!--<div data-role="footer">
		<h1>在此处写入页脚文本</h1>
	</div>-->
</div>
</body>
</html>