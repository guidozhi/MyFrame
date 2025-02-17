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
<title>职工信息</title>
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<script type="text/javascript" src="${pageContext.request.contextPath}/app/k/km/lib/app-end.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/k/km/lib/kh-mobile.css" />
<script src="${pageContext.request.contextPath}/app/k/km/lib/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/app/k/km/lib/kh-mobile.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/pub/bpm/js/util.js"></script>
	
<meta charset="utf-8">
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<link rel="stylesheet" href="app/k/km/lib/kh-mobile.css"/>
<script src="app/k/jqm/jquery2.js"></script>
<script src="app/k/km/lib/kh-mobile.js"></script>
<script src="app/k/km/lib/kh-mobile-list.js"></script>

<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no"/>
<link rel="stylesheet" href="app/k/jqm/skins/default.css"/>
<script src="app/k/jqm/a-main.js" type="text/javascript"></script>
<script src="app/k/km/lib/kh-mobile-list.js"></script>
<script type="text/javascript">
<% String id=request.getParameter("id");
    if(id==null){
    	id=(String)session.getAttribute("id");
    }
%>
	$(function() {
		if('<%=id%>'!="null"){
			$.ajax({
				url : "${pageContext.request.contextPath}/employeeBaseAction/getEmpOnWXById.do?id="+'<%=id%>',
				type : "post",
				datatype : "json",
				contentType: "application/json; charset=utf-8",
		      success : function (data) {
		      	if(data.success){
					   $("#form1").transform("detail");
				       $("#form1").setValues(data.employeeBase);
				       var g1=data.employeeBase.joinWorkDate;
	 				 	if(g1!=null){
	 				 	 $("#joinWorkDate").html(g1.substring(0,7));
	 				 	}
	 				 	var g2=data.employeeBase.initialStopDate;
	  				 	if(g2!=null){
	  				 	 $("#initialStopDate").html(g2.substring(0,7));
	  				 	}
				       var g3=data.employeeBase.intoWorkDate;
	  				 	if(g3!=null){
	  				 	 $("#intoWorkDate").html(g3.substring(0,7));
	  				 	}
		      	}else{
		      		//$("body").unmask();
		      		dialogShow(data.msg, 300, 100);
		      	}
		      }
			});
			<%-- $("#form1").transform("detail");
		    $("#form1").setValues("${pageContext.request.contextPath}/employeeBaseAction/detail.do?id="+'<%=id%>'); --%>  
		}else{
			dAlert("无数据！");
		}
		<%-- $.ajax({
			url : "${pageContext.request.contextPath}/employeeBaseAction/empByPhone.do?phone="+'<%=phone%>',
			type : "post",
			datatype : "json",
			contentType: "application/json; charset=utf-8",
	      success : function (data) {
	      	if(data.success){
					$("#form1").transform("detail");
					if(data.employeeBase.isCheck=="1"){
						$("#next1").hide();
					}
			       $("#form1").setValues(data.employeeBase);
			       var g1=data.employeeBase.joinWorkDate;
 				 	if(g1!=null){
 				 	 $("#joinWorkDate").html(g1.substring(0,7));
 				 	}
 				 	var g2=data.employeeBase.initialStopDate;
  				 	if(g2!=null){
  				 	 $("#initialStopDate").html(g2.substring(0,7));
  				 	}
			       var g3=data.employeeBase.intoWorkDate;
  				 	if(g3!=null){
  				 	 $("#intoWorkDate").html(g3.substring(0,7));
  				 	}
	      	}else{
	      		//$("body").unmask();
	      		dialogShow(data.msg, 300, 100);
	      	}
	      }
		}); --%>
		//性别
		getcodetabl("BASE_SEX","empSex");
		//员工来源
		getcodetabl("TJY2_RL_MANSOURCE","manSource");
		//岗位
		getcodetabl("TJY2_RL_POSITION","position");
		//等级
		getcodetabl("TJY2_RL_LEVEL","grade");
		//员工身份
		getcodetabl("pub_user_type", "empPosition");
		//经费形式
		getcodetabl("TJY2_RL_FUNDINGSHAPE", "fundingShape");
		
		 $("#form1").initForm({
			 getSuccess: function (res){
					if(res.data==null||res.success=="false"){
						alert("对不起！暂时没有你的个人档案");
					}
					
			}
    	});	
	});

	function getcodetabl(code,id) {
		$.ajax({
			url : "${pageContext.request.contextPath}/employeeBaseAction/getcodetabl.do?tablname=" + code,
			type : "POST",
			async : false,
			success : function(json) {
				var s = "";
				if (json.success) {
					for ( var i in json.data) {
						s += "<option value='"+json.data[i].value+"'>"
								+ json.data[i].name + "</option>";
					}
					$("#" + id).html(s);

				}
			}
		});
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
			<h2>职工档案-职工信息</h2>
		</div>
	</div>
</div>
<div id="wrapper" class="wrapper">
	<div class="container">
		<div class="page-header" align="center">
			<h1></h1>
		</div>
		<div class="page-panel" id="m-page-panel">
			<form id="form1" name="form1" method="post" action="" onsubmit="return false;" getAction="" pageStatus="detail">
				<table border="0" cellpadding="0" cellspacing="0" width="100%"
					height="" align="">
					<tr class="tr1">
						<td width="80" class="tdtext1">姓名：</td>
						<td><input type="text" class="form-control" id="name"
							name="empName" 
							maxLength="10"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">性别：</td>
						<td><select  class="form-control" id="empSex"
							name="empSex"  maxLength="18"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">身份证号：</td>
						<td><input type="text" class="form-control" id="empIdCard"
							name="empIdCard" 
							maxLength="11"></td>

					</tr>
					<tr class="tr1">
						<td class="tdtext1">籍贯：</td>
						<td><input type="text" class="form-control"
							id="empNativePlace" name="empNativePlace" ext_isNull="1" maxLength="25" ext_isNull="1"></td>

					</tr>
					<tr class="tr1">
						<td class="tdtext1">政治面貌：</td>
						<td><input type="text" class="form-control" id="empPolitical"
							name="empPolitical"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">联系电话：</td>
						<td><input type="text" class="form-control" id="mobilePhone"
							name="mobilePhone"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">职称：</td>
						<td><input type="text" class="form-control" id="empTitle"
							name="empTitle"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">参加工作时间：</td>
						<td><input type="text" class="form-control" id="joinWorkDate"
							name="joinWorkDate"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">初始学历：</td>
						<td><input type="text" class="form-control" id="initialEducation"
							name="initialEducation"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">初始学位：</td>
						<td><input type="text" class="form-control" id="initial_degree"
							name="initial_degree"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">初始专业：</td>
						<td><input type="text" class="form-control" id="initialMajor"
							name="initialMajor"></td>
					</tr> 
					<tr class="tr1">
						<td class="tdtext1">初始毕业院校：</td>
						<td><input type="text" class="form-control" id="initialSchool"
							name="initialSchool"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">在职学历：</td>
						<td><input type="text" class="form-control" id="mbaEducation"
							name="mbaEducation"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">在职学位：</td>
						<td><input type="text" class="form-control" id="mba_degree"
							name="mba_degree"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">在职专业：</td>
						<td><input type="text" class="form-control" id="mbaMajor"
							name="mbaMajor"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">在职毕业院校：</td>
						<td><input type="text" class="form-control" id="mbaSchool"
							name="mbaSchool"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">毕业时间：</td>
						<td><input type="text" class="form-control" id="initialStopDate"
							name="initialStopDate"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">职务：</td>
						<td><input type="text" class="form-control" id="workTitle"
							name="workTitle"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">人员来源：</td>
						<td><select  class="form-control" id="manSource"
							name="manSource" ></td>
							
					</tr>
					<tr class="tr1">
						<td class="tdtext1">经费形式：</td>
						<td><select type="text" class="form-control" id="fundingShape"
							name="fundingShape"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">所属部门：</td>
						<td><input type="text" class="form-control" id="workDepartmentName"
							name="workDepartmentName"></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">岗位：</td>
						<td><select  class="form-control" id="position"
							name="position" ></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">等级：</td>
						<td><select  class="form-control" id="grade"
							name="grade" ></td>
							
					</tr>
					<tr class="tr1">
						<td class="tdtext1">员工身份：</td>
						<td><select class="form-control" id="empPosition"
							name="empPosition" ></td>
					</tr>
					<tr class="tr1">
						<td class="tdtext1">入院时间：</td>
						<td><input type="text" class="form-control" id="intoWorkDate"
							name="intoWorkDate"></td>
					</tr>
				</table>
			</form>
		
		</div>
	</div>
</div>
</body>
</html>