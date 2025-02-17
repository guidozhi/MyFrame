<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %> 
<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<!DOCTYPE html>
<html>
<head>	
<meta charset="utf-8">
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<script src="app/k/jqm/jquery2.js"></script>
<script src="app/k/km/lib/kh-mobile.js"></script>

<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no"/>
<link rel="stylesheet" href="app/k/jqm/skins/default.css"/>
<script src="app/k/jqm/a-main.js" type="text/javascript"></script>
	<script type="text/javascript">

		$(function(){
			$.ajax({
				url : "${pageContext.request.contextPath}/employeeBaseAction/weixinHRCount.do",
				type : "post",
				datatype : "json",
				contentType: "application/json; charset=utf-8",
			    success : function (data) {
			    if(data.success){
			    	var list = data.list;
					$("#form1").transform("detail");
					$("#form1").setValues(list[0]);     
			      	}else{
			      		//$("body").unmask();
			      		dialogShow(data.msg, 300, 100);
			      	}
			      }
			})
		});
	</script>
</head>
<body>
<div data-role="page" id="a-index" class="a-index">
	<div data-role="header">
		<div class="bg"></div>
		<div class="logo"></div>
		<div class="user">
			<h1>四川省特检院</h1>
			<h2>人力资源-全院概况</h2>
		</div>
	</div>
	<div data-role="content">
		<div class="content">
			<div class="wrap" style="padding-top: 26%;">
			<form id="form1" name="form1" method="post" action="" onsubmit="return false;" getAction="${pageContext.request.contextPath}/employeeBaseAction/weixinHRCount.do" pageStatus="detail">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" height="" align="center" class="table">
					<tr>
						<td class="a"><div class="d1">基础信息</div></td>
						<td class="b"><div class="d2">总人数<span class="n" id="total1">
									<input id="total"  name="total" style="width: 30px;"></span>人，    
							女性职工<span class="n" id="woman1">
									<input class="form-control" id="woman"  name="woman" style="width: 30px;"></span>人，   
							男性职工<span class="n" id="man1">
									<input class="form-control" id="man"  name="man" style="width: 30px;"></span>人，    
							在编人员<span class="n" id="regular1">
									<input class="form-control" id="regular"  name="regular" style="width: 30px;"></span>人。</div>
						</td>
					</tr>
					<tr>
						<td class="a"><div class="d1">教育程度</div></td>
						<td class="b"><div class="d2">博士<span class="n" id="phd1">
								<input class="form-control" id="phd" name="phd" style="width: 30px;"></span>人，
							研究生<span class="n" id="postgraduate1">
								<input class="form-control" id="postgraduate" name="postgraduate" style="width: 30px;"></span>人，
							本科<span class="n" id="undergraduate1">
								<input class="form-control" id="undergraduate" name="undergraduate" style="width: 30px;"></span>人。</div>
						
						</td>
					</tr>
					<tr>
						<td class="a"><div class="d1">政治面貌</div></td>
						<td class="b c"><div class="d2">党员干部<span class="n" id="member_CPC1">
								<input class="form-control" id="member_CPC" name="member_CPC" style="width: 30px;"></span>人，
							共青团员<span class="n" id="member_CYL1">
								<input class="form-control" id="member_CYL" name="member_CYL" style="width: 30px;"></span>人。</div>
							</td>
					</tr>
					<tr>
						<td class="a"><div class="d1">持证情况</div></td>
						<td class="b c"><div class="d2">高级工程师<span class="n" id="senior_engineer1">
										<input class="form-control" id="senior_engineer" name="senior_engineer" style="width: 30px;"></span>人
							（教授级高工<span class="n" id="p_senior_engineer1">
										<input class="form-control" id="p_senior_engineer" name="p_senior_engineer" style="width: 30px;"></span>人），
							工程师<span class="n" id="enginer1">
										<input class="form-control" id="enginer" name="enginer" style="width: 30px;"></span>人。
							具有各类检验项目持证<span class="n" id="examiner1">
										<input class="form-control" id="examiner" name="examiner" style="width: 30px;"></span>人/项，
							其中承压类检验师<span class="n" id="pressure_examiner1">
										<input class="form-control" id="pressure_examiner" name="pressure_examiner" style="width: 30px;"></span>人/项，
							机电类检验师<span class="n" id="ele_examiner1">
										<input class="form-control" id="ele_examiner" name="ele_examiner" style="width: 30px;"></span>人/项。
							无损检测Ⅲ级（高级）证<span class="n" id="non_destructive1">
										<input class="form-control" id="non_destructive" name="non_destructive" style="width: 30px;"></span>人/项，
							Ⅱ级（中级）证<span class="n" id="second_class1">
										<input class="form-control" id="second_class" name="second_class" style="width: 30px;"></span>人/项，
							检验项目覆盖率达100%。</div>
						</td>
					</tr>
				</table>
				</form>
			</div>
		</div>
	</div>
	<!--<div data-role="footer">
		<h1>在此处写入页脚文本</h1>
	</div>-->
</div>


</body>
</html>