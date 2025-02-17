﻿<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="detail">
<title>选择机构</title>
<%@include file="/k/kui-base-form.jsp"%>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript">
	var orgTreeManager = null;
	var unitId = ${param.type=='00'}?'':"<sec:authentication property="principal.unit.id" htmlEscape="false"/>";
	var unitName = ${param.type=='00'}?'':"<sec:authentication property="principal.unit.orgName" htmlEscape="false"/>";
	var orgCode = ${param.type=='00'}?'':"<sec:authentication property="principal.unit.orgCode" />";
	var sort = 	${param.type=='00'}?'':"<sec:authentication property="principal.unit.sort" />";
	var utellphone = ${param.type=='00'}?'':"<sec:authentication property="principal.unit.tellphone" />";
	var tellphone = utellphone=="null"?"":utellphone;
	var property = ${param.type=='00'}?'':"<sec:authentication property="principal.unit.property" />";
	if(${param.type=='000'}){
		unitId = "<sec:authentication property="principal.department.id" />";
		unitName = "<sec:authentication property="principal.department.orgName" htmlEscape="false"/>";
		orgCode = "<sec:authentication property="principal.department.orgCode" />";
		sort = 	"<sec:authentication property="principal.department.sort" />";
		utellphone = "<sec:authentication property="principal.department.tellphone" />";
		tellphone = utellphone=="null"?"":utellphone;
		property = "<sec:authentication property="principal.department.property" />";
		<sec:authorize access="hasRole('unit_administrate')">
			unitId = "<sec:authentication property="principal.unit.id" htmlEscape="false"/>";
			unitName = "<sec:authentication property="principal.unit.orgName" htmlEscape="false"/>";
			orgCode = "<sec:authentication property="principal.unit.orgCode" />";
			sort = 	"<sec:authentication property="principal.unit.sort" />";
			utellphone = "<sec:authentication property="principal.unit.tellphone" />";
			tellphone = utellphone=="null"?"":utellphone;
			property = "<sec:authentication property="principal.unit.property" />";
		</sec:authorize>
	}
	<sec:authorize ifAnyGranted="sys_administrate,super_administrate">
		unitId = '';
	</sec:authorize>
	$(function() {
		//组织机构树
		orgTreeManager = $("#tree1").ligerTree({
			checkbox: ${param.checkbox=='1'},
			selectCancel : false,
            iconFieldName: "level",
            textFieldName: "orgName",
            iconParse: function(data){
            	if(data["level"]==0)
					return "k/kui/images/icons/16/home.png";
				else if (data["property"] == 'unit')
					return "k/kui/images/icons/16/org.png";
				else if (data["property"] == 'dep')
                	return "k/kui/images/icons/16/group.png";
				else
                	return "k/kui/images/icons/16/folders_explorer.png";
            },
			data : [],
            onBeforeExpand: appendChildren
		});
		if(unitId==""){
			$.getJSON("rbac/org/getTopOrg.do",function(res){
				if(res.success){
					setOrgTree(res.data.id,res.data.orgName,res.data.orgCode,res.data.sort,res.data.tellphone,res.data.property)
				}else{
					$.ligerDialog.warn("获取机构信息出错！");
				}
			})
		}else{
			setOrgTree(unitId,unitName,orgCode,sort,tellphone,property);
		}
	})
	function setOrgTree(unitId,unitName,orgCode,sort,tellphone,property){
	    $("body").mask("正在加载数据，请稍候....");
		$.getJSON("rbac/org/getSubordinate.do?orgid=" + unitId,function(dataList){
			orgTreeManager.append(unitId,[{
				id: unitId,
				text: unitName,
				orgName: unitName,
				orgCode: orgCode,
				sort: sort=="null"?"":sort,
				tellphone: tellphone=="null"?"":sort,
				property: property,
				describle: "",
				level: "0",
				children: dataList,
				expandChild: true
    		}]);
			$("body").unmask();
    		//orgTreeManager.selectNode(unitId);
		});
	}
	function appendChildren(node){
		if (!node.data.expandChild){
			orgTreeManager.loadData(node.data, "rbac/org/getSubordinate.do?orgid=" + node.data.id);
			node.data.expandChild = true;
		}
	}
	
	function getSelectResult(){
		var isCheckBox = ${param.checkbox=='1'};
		var nodes = (isCheckBox?orgTreeManager.getChecked():orgTreeManager.getSelected());
		if(!nodes){
			top.$.dialog.alert("您没有选择任何节点！");
			return null;
		}
		var result = {code:"",name:""};
		if(isCheckBox){
			$.each(nodes,function(i,el){
				result.code += (i==0?"":",") + this.data.id;
				result.name += (i==0?"":",") + this.data.text;
			});
		}else{
			result.code = nodes.data.id;
			result.name = nodes.data.text;
		}
		return result;
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
<body style="overflow:auto">
	<ul id="tree1"></ul>
</body>
</html>
