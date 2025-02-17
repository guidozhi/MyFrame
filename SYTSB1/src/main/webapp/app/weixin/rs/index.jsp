<%@page import="javax.mail.Session"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no"/>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<link rel="stylesheet" href="app/k/jqm/skins/default.css"/>
<script type="text/javascript" charset="utf-8" src="app/k/jqm/jquery2.js"></script>
<script type="text/javascript" charset="utf-8" src="app/k/jqm/jquery.mobile.js"></script>
	<script type="text/javascript">
	function tiao(href){
		location.href=href;
		}	
	</script>
	<script type="text/javascript">
	<%
	String phone = (String)session.getAttribute("Phone");
	String name = (String)session.getAttribute("Name");
	String accessToken = (String)session.getAttribute("AccessToken");
	%>
	//查询是否为在编人员
	$(function() {
		$.ajax({
			url : "${pageContext.request.contextPath}/employeeBaseAction/empByPhone.do?phone="+'<%=phone%>',
			type : "post",
			datatype : "json",
			contentType: "application/json; charset=utf-8",
	      success : function (data) {
	      	if(data.success){
					if(data.employeeBase.empPosition=="1"){
						$("#gjj").hide();
						$("#gzb").hide();
					}
	      	}else{
	      		//$("body").unmask();
	      		dialogShow(data.msg, 300, 100);
	      	}
	      }
		});
	})
	</script>
	
</head>
<body>
<div data-role="page" id="a-index" class="a-index">
	
			
			
			<div id="a_menu_item_an_ywbl" class="bar bar02">
				<div id = "jbxx" class="item" onclick="tiao('${pageContext.request.contextPath}/app/weixin/rs/rs_detail.jsp');">
					<div id="aMenu_cszh" class="a" href="javascript:void(0);"
							menuid="cszh" url="">
							<div class="icon clr_22" style___="background-color:#e13668;">
								<img src="k/kui/images/icons/64/m1-default.jpg" alt="">
							</div>
							<div class="bartxt">基本信息</div>
						</div>	
				</div>
				<div id = "gjj" class="item" onclick="tiao('${pageContext.request.contextPath}/app/weixin/gjj/gjj_detail.jsp');">
					<div id="aMenu_cszh" class="a" href="javascript:void(0);"
							menuid="cszh" url="">
							<div class="icon clr_22" style___="background-color:#e13668;">
								<img src="app/k/kui/images/icons/64/gjj.jpg" alt="">
							</div>
							<div class="bartxt">公积金</div>
						</div>	
				</div>
				<div id = "gzb" class="item" onclick="tiao('${pageContext.request.contextPath}/app/weixin/hys/humanresources/ywfwbgzqr_detail.jsp');">
					<div id="aMenu_cszh" class="a" href="javascript:void(0);"
							menuid="cszh" url="">
							<div class="icon clr_22" style___="background-color:#e13668;">
								<img src="app/k/kui/images/icons/64/gz.jpg" alt="">
							</div>
							<div class="bartxt">工资表</div>
						</div>	
	</div>
	<h2>&nbsp;&nbsp;&nbsp;&nbsp;请查看“员工信息”“公积金”“工资”三个板块。如无问题请点击页面下方的“确定”，如有问题请勿确定，并以部门为单位及时反映到人力资源部陈俐宏处，待修改完毕并再次查看后重新“确定”。
</br>（温馨提示：1、在编人员只有员工信息，无公积金和工资模块；2、请获得双学位的同事务必确定学历学位是否正确）
</h2>
	<!--<div data-role="footer">
		<h1>在此处写入页脚文本</h1>
	</div>-->
</div></div>
</body>
</html>