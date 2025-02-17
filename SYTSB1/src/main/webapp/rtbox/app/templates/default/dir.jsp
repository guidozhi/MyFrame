<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head id="main_head">
<%@include file="/k/kui-base-list.jsp"%>
<title></title>
<script type="text/javascript">
	var ztree;
	var siteID = "";//站点id
	var info_id = "${param.info_id}";
	var code = "${param.code}";
	var pageCode = "${param.pageCode}";
	$(function() {

		ztree = $("#tree1").ligerTree({
			checkbox : true,
			selectCancel : false,//第二次点击节点不取消选择
			idFieldName : "code",
			textFieldName : "name",
			/* url : 'com/rt/dir/viewDir.do?code=' + code, */
			url : "com/rt/page/getDir.do?id=&code=" + code + "&infoId=" + info_id,
			btnClickToToggleOnly : false,
			nodeWidth : 400,
			slide : false,
			onAfterAppend : function(parentNode, newdata) {
				// 				ztree.selectNode(newdata);//初始化
				if (pageCode) {
					var pageCodes = pageCode.split(",");
					for (var i = 0; i < pageCodes.length; i++) {
						ztree.selectNode(pageCodes[i]);
					}
				}

			}

		});

	});

	function getSelectResult() {
		var flag = false;
		var nodes = ztree.getChecked();
		if (nodes=="" || nodes=="undefined"  || nodes==null) {
			top.$.dialog.alert("请选择任何节点！");
			return null;
		}
		var result = [];
		if(nodes[0]["data"]["code"]=="root"){
			result.push(nodes[0].data);
		}else{
			var data = [];
			$.each(nodes, function(i, el) {
				data.push(this.data);
			});
			result = [ {
				code : "root",
				name : "目录",
				children : data
			} ];
			
		}

		var r = confirm("确认更换目录吗？");
		if (!r) {
			return false;
		}

		$("body").mask("正在保存....");
		$.ajax({
			type : "POST",
			dataType : "json",
			async : false,
			// 			        contentType: "application/json;charset=utf-8",
			url : "com/rt/dir/setDir.do",
			data : {
				rtCode : code,
				rtDirJson : JSON.stringify(result),
				id : info_id,
				setType : 'reset'
			},
			success : function(resp, stats) {
				if (resp.success) {
					top.$.notice("操作成功！", 4);
					flag = true;
				} else {
					$.ligerDialog.error("保存失败！<br/>" + resp.msg);
				}
				$("body").unmask();
			},
			error : function(data) {
				$.ligerDialog.error('保存失败！<br/>' + data.msg);
				$("body").unmask();
			}
		});

		return flag;
	}
</script>
</head>
<body style="overflow: auto">
	<ul id="tree1"></ul>

</body>
</html>
