﻿<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户角色授权</title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript">
	var qmUserConfig = {
		sp_defaults:{
			columnWidth: "48%"
		},
		sp_fields:[
			{name:"name",compare:"like",labelWidth:40},
	    	{name:"account",compare:"like",labelWidth:60}
	    ],
		listeners : {
			onAfterShowData : initGridSelectRange,
			onCheckRow : function(checked,rowdata, rowindex, rowDomElement){
				parent.addOrRemoveUser(checked,rowdata);
			},
			onCheckAllRow:function(checked,row){
				var data =Qm.getQmgrid().getData();
				for(var i in data){
					parent.addOrRemoveUser(checked, data[i]);
				}
			}
		}
	};
	//表格渲染时，将被选择的角色勾选
	function initGridSelectRange(){
		var idRange = parent.getRoleUserArr();
		Qm.getQmgrid().selectRange("id", idRange);
	}
	//重新加载数据，根据层级编码
	function loadGridData(levelCode){
		Qm.config.defaultSearchCondition[0].value=levelCode;
		Qm.searchData();
	}
</script>
</head>
	<qm:qm pageid="user_1" script="false" singleSelect="false">
		<qm:param name="level_code" compare="llike" value="${param.levelCode}" />
	</qm:qm>
</html>
