<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd"); 
	String nowTime=""; 
	nowTime = dateFormat.format(new Date());%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript">
var orgTreeManager = null;
var unitId ="<sec:authentication property="principal.unit.id" />";
var unitName = "<sec:authentication property="principal.unit.orgName" />";
$(function() {
	//页面布局
	$("#layout1").ligerLayout({
		leftWidth : 279,
		topHeight: 30,
		space: 5,
		allowTopResize : false,
		allowLeftCollapse : false,
		allowRightCollapse : false
	});
	//组织机构树
	orgTreeManager = $("#tree1").ligerTree({
		checkbox : false,
        iconFieldName : "level",
        iconParse : function(data){
        	if(data["level"]==0)
				return "k/kui/images/icons/16/home.png";
			else if (data["level"] == 1)
				return "k/kui/images/icons/16/org-2.png";
        	else
            	return "k/kui/images/icons/16/user.png";
        },
		data : [],
		attribute : [ "url" ],
		onSelect : function(node) {
			ztreeClick(node.data.id);
		}
	});
	 $.getJSON("rbac/org/getSubordinate.do?orgid=" + unitId,function(dataList){
		orgTreeManager.append(unitId,[{
			id : unitId,
			text : unitName,
			level : "0",
			children : dataList
		}]);
		orgTreeManager.selectNode(unitId);
	}); 
});

function ztreeClick(treeId){
	rightFrame.loadGridData(treeId);
}
</script>
 <style type="text/css">
    .l-tree .l-tree-icon-none img {
        height: 16px;
        margin: 3px;
        width: 16px;
    }

</style>
</head>
<body>
<div id="layout1">
    <div position="left" title="组织机构" style="overflow:auto;">
			<ul id="tree1"></ul>
	</div>
	<c:if test='${param.status=="active"}'>
	<div position="center" align="center" title="在职人员信息">
		<iframe id="rightFrame" name="rightFrame" src="app/humanresources/employee_active.jsp" frameborder="0" 
			width="100%" height="100%" scrolling="no"></iframe>
	</div>
	</c:if>
	<c:if test='${param.status=="dismissal"}'>
	<div position="center" align="center" title="解聘人员信息">
		<iframe id="rightFrame" name="rightFrame" src="app/humanresources/employee_dismissal.jsp" frameborder="0" 
			width="100%" height="100%" scrolling="no"></iframe>
	</div>
	</c:if>
	<c:if test='${param.status=="retired"}'>
	<div position="center" align="center" title="退休人员信息">
		<iframe id="rightFrame" name="rightFrame" src="app/humanresources/employee_retired.jsp" frameborder="0" 
			width="100%" height="100%" scrolling="no"></iframe>
	</div>
	</c:if>
	<c:if test='${param.status=="dimission"}'>
		<div position="center" align="center" title="离职人员信息">
			<iframe id="rightFrame" name="rightFrame" src="app/humanresources/employee_dimission.jsp" frameborder="0"
					width="100%" height="100%" scrolling="no"></iframe>
		</div>
	</c:if>
	<c:if test='${param.status=="trial"}'>
	<div position="center" align="center" title="试用人员信息">
		<iframe id="rightFrame" name="rightFrame" src="app/humanresources/employee_trial.jsp" frameborder="0" 
			width="100%" height="100%" scrolling="no"></iframe>
	</div>
	</c:if>
	<c:if test='${param.status=="stopRemind"}'>
	<div position="center" align="center" title="聘用到期提醒设置">
		<iframe id="rightFrame" name="rightFrame" src="app/humanresources/employee_stop_remind.jsp" frameborder="0" 
			width="100%" height="100%" scrolling="no"></iframe>
	</div>
	</c:if>
	<c:if test='${param.status=="birthdayRemind"}'>
	<div position="center" align="center" title="生日提醒设置">
		<iframe id="rightFrame" name="rightFrame" src="app/humanresources/employee_birthday_remind.jsp" frameborder="0" 
			width="100%" height="100%" scrolling="no"></iframe>
	</div>
	</c:if>
	<c:if test='${param.status=="retireRemind"}'>
	<div position="center" align="center" title="退休提醒设置">
		<iframe id="rightFrame" name="rightFrame" src="app/humanresources/employee_retire_remind.jsp" frameborder="0" 
			width="100%" height="100%" scrolling="no"></iframe>
	</div>
	</c:if>
	<c:if test='${param.status=="titleChange"}'>
	<div position="center" align="center" title="职务任免">
		<iframe id="rightFrame" name="rightFrame" src="app/humanresources/employee_worktitle_list.jsp" frameborder="0" 
			width="100%" height="100%" scrolling="no"></iframe>
	</div>
	</c:if>
	<c:if test='${param.status=="addressBook"}'>
	<div position="center" align="center" title="通讯录">
		<iframe id="rightFrame" name="rightFrame" src="app/humanresources/employee_addressBook.jsp" frameborder="0" 
			width="100%" height="100%" scrolling="no"></iframe>
	</div>
	</c:if>
       </div>
</body>
</html>