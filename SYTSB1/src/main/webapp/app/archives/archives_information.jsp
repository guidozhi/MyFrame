<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript" src="pub/rbac/js/draggable.js"></script>
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
				return "k/kui/images/16/icons/home.png";
			else if (data["level"] == 1)
				return "k/kui/images/16/icons/org-2.png";
        	else
            	return "k/kui/images/16/icons/user.png";
        },
		data : [],
		attribute : [ "url" ],
		onSelect : function(node) {
			ztreeClick(node.data.id);
		}
	});

	 $.getJSON("uploadsAction/a/getFileTree.do?fileId=${param.fileId}",function(dataList){
		// orgTreeManager.append(dataList);
	  orgTreeManager.append(unitId,[{
			id : "0",
			text : "${param.bh}",
			path :"",
			level : "0",
			children : dataList
		}]); 
		orgTreeManager.selectNode("0");
	});  
});	


function ztreeClick(treeId){
	var path="";
	var parent="";
	 $.getJSON("uploadsAction/a/detail.do?id="+treeId,function(res){
		 <%request.getParameter("data");%>
		 path=res.data.uploadPath;
		 parent=res.data.parentId;
		 path=path.replace("\\", ",");
		 if(parent=="10000"){
			 $("#rightFrame").attr("src","app/archives/archives_word.jsp?path="+path);
		 }else if(parent=="10002"){
			 $("#rightFrame").attr("src","app/archives/preview.jsp?id="+res.data.uploadId);
		 }
		//rightFrame.loadGridData(path);
	 });

}
</script>
 
</head>
<body>
<div id="layout1">
    <div position="left" title="档案" style="overflow:auto;">
			<ul id="tree1"></ul>
	</div>
	<div id="div1" position="center" align="center" >
		<iframe id="rightFrame" name="rightFrame" src=""  frameborder="0" 
			width="100%" height="100%" scrolling="no"></iframe>
	</div>
	
       </div>
</body>
</html>