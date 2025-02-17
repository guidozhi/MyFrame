﻿<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>为权限配置资源</title>
<%@include file="/k/kui-base-form.jsp"%>
<script type="text/javascript">
	var ztree;
	var setting = {
		data: {
			key: {
				name: "text",
				url:""
			}
		},
		<c:if test="${param.checked==true}">
		check: {
			enable: true,
			chkboxType: { "Y" : "ps", "N" : "s" }
		},
		</c:if>
		callback: {
			onNodeCreated:function(event, treeId, treeNode){
				var treeObj = $.fn.zTree.getZTreeObj(treeId);
				if(treeNode.image!="null" && treeNode.image!="" && treeNode.image!=undefined){
					treeNode.icon = treeNode.image;
					treeObj.updateNode(treeNode)
				}
			}
		}
	};
	$(function() {
		$.getJSON("rbac/permission/ininAuthorizedResource.do?perId=${param.perId}",function(data){
			var strData = JSON.stringify(data);
			strData = strData.replace(/isexpand/g, "open");
			var zNodes = eval(strData)
			ztree = $.fn.zTree.init($("#tree1"), setting, zNodes);
		});
	});

	function getSelectedResource(){
		var nodes = ztree.getSelectedNodes();
		return nodes;
	}
	//设置资源权限
	function authorizedResource() {
		var notes = ztree.getCheckedNodes();
		var ids = "";
		for ( var i = 0; i < notes.length; i++) {
			ids += "|" + notes[i].id;
		}
		if (ids.length > 1)
			ids = ids.substring(1);
		$("body").mask("授权执行中，请稍候...");
		$.post("rbac/permission/authorizedResource.do", {
			perId : '${param.perId}',
			resourceIds : ids
		}, function(data) {
			$("body").unmask();
			if (data.success) {
				top.$.notice('授权成功',3);
				api.close();
			} else {
				$.ligerDialog.success('设置失败');
			}
		});
	}
</script>
</head>
<body>
	<ul id="tree1" class="ztree"></ul>
</body>
</html>
