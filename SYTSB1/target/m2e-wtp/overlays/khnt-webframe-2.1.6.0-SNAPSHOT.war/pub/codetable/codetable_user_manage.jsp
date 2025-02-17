﻿<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head id="main_head">
<title>数据字典管理</title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript">
	var gridManager = null;
	$(function() {
		//按钮工具栏
		$("#toptoolbar").ligerToolBar({
			items : [{
				text : '增加值',
				id : 'addValue',
				click : addValue,
				icon : 'info-add'
			},"-", {
				text : '修改值',
				id : 'modifyValue',
				click : modifyValue,
				icon : "info-edit"
			},"-", {
				text : '删除值',
				id : 'delValue',
				click : delValue,
				icon : "info-del"
			} ]
		});

		//页面布局
		$("#layout").ligerLayout({
			topHeight : 30,
			space: 1,
			allowTopResize : false
		});
		
		gridManager = $("#dataGridDiv").ligerGrid({
			columns : [
			   	{
					display : '名称', name : 'name', width : 240, align : 'left'
				},
				{
					display : '值', name : 'code', width : 80, align : 'left'
				}, 
				 {
					display : '说明', name : 'remark', width : 200, align : 'left'
				},{
					display : '排序', name : 'sort', width : 80
				}
			],
			usePager : false,
			allowHideColumn:false,
			height: '100%',
			tree : {
				columnName : 'name'
			}, 
			autoCheckChildren : false
		});
		reloadDataGrid();
	});
		
	//增加值
	function addValue() {
		var actionNodeID2 = "";
		var row = gridManager.getSelectedRow();
		if (row) actionNodeID2 = row.id;
		top.$.dialog({
			width : 450,
			height : 220,
			lock : true,
			parent : api,
			title : "新增表值",
			content : 'url:pub/codetable/codetablevalue_detail.jsp?status=add&id=${param.id}&code_table_value_id=' + actionNodeID2, 
			data : {
				"window" : window
			}
		});
	}

	//修改值
	function modifyValue() {
		var row = gridManager.getSelectedRow();
		if (row) {
			var actionNodeID2 = row.id;
			top.$.dialog({
				width : 450,
				height : 220,
				lock : true,
				parent : api,
				title : "修改表值",
				content : 'url:pub/codetable/codetablevalue_detail.jsp?status=modify&id='
						+ actionNodeID2, data : {
					"window" : window
				}
			});
		}
		else{
			$.ligerDialog.error('请选择需要修改的值!')
		}
	}
	
	//删除值
	function delValue() {
		var row = gridManager.getSelected();
		if (row) {
			if (gridManager.hasChildren(row)) {
				$.ligerDialog.alert('该节点下含有子节点，请先删除子节点!')
				return;
			}
			$.ligerDialog.confirm("你确定要删除【" + row.name + "】吗？\n删除后不能恢复！", function(yes) {
				if (yes) {
					$.post("pub/codetablevalue/deleteValue.do", {"ids" : row.id, codeTabledId : "${param.id}"}, function(resp) {
						if (resp.success) {
							gridManager.remove(row);//执行删除
						} else {
							$.ligerDialog.error('删除失败');
							return false;
						}
					},"json");
				}
			});
		}
		else {
			$.ligerDialog.alert('请选择您要删除的项目!')
		}
	} 
	
	//刷新码表值数据
	function reloadDataGrid() {
		$.getJSON("pub/codetablevalue/getCodetableValues.do?id=${param.id}",function(res){
			gridManager.loadData({Rows:res.Rows})
		})
	}
</script>
</head>
<body class="p5">
	<div id="layout">
		<div position="top" id="toptoolbar"></div>
		<div position="center" id="dataGridDiv"></div>
	</div>
</body>
</html>
