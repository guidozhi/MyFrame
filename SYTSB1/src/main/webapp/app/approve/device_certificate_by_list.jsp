<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	String sql = "select * from ( "
			//设备类别
			+" select ct.value id,'' pid, SUBSTR(ct.value,0,1) code ,ct.name text from PUB_CODE_TABLE c ,PUB_CODE_TABLE_VALUES ct where c.id = ct.code_table_id  and  ct.code_table_id='402883a04b35cf38014b38c2da07245a' and ct.code_table_values_id is null " 
			//电梯下的部门
			+" union all " 
			+" select id,'3000' pid,id code, ORG_NAME text from SYS_ORG  where id in ('100020','100021','100022','100023','100063') "
			//压力管道下的部门
			/* +" union all " 
			+" select id, '8000' pid, id code, ORG_NAME text from SYS_ORG where id in ('100033','100034','100035','100036','100037','100065','100066') " */
			//氧舱年度检验报告下的部门
			/* +" union all " 
			+" select id, '402883a04fd1199c014ff3d726b33318' pid, id code, ORG_NAME text from SYS_ORG where id in ('100033','100034','100035','100036','100037','100065','100066') "
			//固定式压力容器年度检查报告下的部门
			+" union all " 
			+" select id, '402883a059a738c30159aa1d593e99d9' pid, id code, ORG_NAME text from SYS_ORG where id in ('100033','100034','100035','100036','100037','100065','100066') " */
			//氧舱年度检验报告下的部门、固定式压力容器年度检查报告下的部门
			+" union all " 
			+" select o.id, r.id pid, o.id code, o.ORG_NAME text from SYS_ORG o, BASE_REPORTS r where o.id in ('100033','100034','100035','100036','100037','100065','100066') and r.report_name in ('氧舱年度检查报告', '固定式压力容器年度检查报告') and r.is_issue = '1' "
			//其他类
			+" union all "
			+" select id,'0000' pid,id,b.report_name2 from base_reports b where b.is_issue='1' "
			+" ) tt order by case when tt.id='0000' then 1 else 0 end, tt.id asc ";
%>
<%@include file="/k/kui-base-list.jsp"%>
<title>设备信息</title>
<script type="text/javascript">
var ztree;
var zNodes;
var setting = {
		data: {
			key: {
				name: "text",
				url:""
			}
		},callback: {
			onClick: ztreeClick,
			onNodeCreated:function(event, treeId, treeNode){
				var treeObj = $.fn.zTree.getZTreeObj(treeId);
				if(treeNode.image!="null" && treeNode.image!="" && treeNode.image!=undefined){
					treeNode.icon = treeNode.image;
					treeObj.updateNode(treeNode)
				}
				if(treeNode.id=='root'){
					treeObj.selectNode(treeNode);
					$("#rightFrame").attr("src","pub/chart/chart/chart_list.jsp?typeId="+treeNode.id);
				}
			}
		}
};
$(function() {
	$("#layout1").ligerLayout({
		leftWidth : 250,
    	space : 5,
		allowLeftCollapse : false,
		allowRightCollapse : false
	});


		/* var treeData=<u:dict code="device_type" />;
		var treeData=<u:dict sql="select t.id,t.code_table_values_id pid,SUBSTR(t.value,0,1) code,t.name text from PUB_CODE_TABLE_VALUES t,pub_code_table t1 where t.code_table_id=t1.id and   t1.code = 'device_classify' and t.code_table_values_id is null order by case when t.value='0000' then 1 else 0 end, t.value asc" />;
		var treeData=<u:dict sql="select * from (select ct.value id,'' pid, SUBSTR(ct.value,0,1) code ,ct.name text from PUB_CODE_TABLE c ,PUB_CODE_TABLE_VALUES ct where c.id = ct.code_table_id  and  ct.code_table_id='402883a04b35cf38014b38c2da07245a' and ct.code_table_values_id is null  union all select distinct t.device_type||t1.org_id id , t.device_type pid , t1.org_id code ,t1.org_name text from BASE_UNIT_FLOW t,FLOW_SERVICE_CONFIG t1 where t.fk_flow_id = t1.id and t.device_type='3000' union all select id,'0000' pid,id,b.report_name from base_reports b where b.is_issue='1') tt order by case when tt.id='0000' then 1 else 0 end, tt.id asc" />; */
		var treeData=<u:dict sql="<%=sql%>" />;
		ztree = $.fn.zTree.init($("#tree1"), setting, treeData);
	
	
});
function ztreeClick(event,treeId,treeNode){
	var win = $("#rightFrame").get(0).contentWindow.window;
	if(win.loadGridData) win.loadGridData(treeNode.id,treeNode,treeNode.getParentNode());
	$("#typepic").attr('src',treeNode.icon)
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
<body class="p5">
	<div id="layout1">
		<div position="left" title="设备类别" class="overflow-auto">
			<ul id="tree1" class="ztree"></ul>
		</div>
		<div position="center" align="center" title="">
			<iframe id="rightFrame" frameborder="0" name="userFrame" width="100%" height="100%" scrolling="no" src="app/approve/certificate_by_rule.jsp" /></iframe>
		</div>
	</div>
</body>
</html>
