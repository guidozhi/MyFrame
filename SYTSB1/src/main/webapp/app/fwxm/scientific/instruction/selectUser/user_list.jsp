﻿<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>机构人员列表</title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript">
	var qmUserConfig = {
		sp_fields: [ {
			label: "姓名",
			name: "name",
			compare: "like",
			labelAlign: "right",
			labelWidth: 40,
			width: 100
		} ,{
			name: "emp_position",
			compare: "=",
			width: 100
		} 
		],
		listeners : {
			onAfterShowData : function() {
				initGridSelectRange();
			},
			<c:if test="${param.checkbox=='1'}">
			onCheckRow : function(checked, rowdata, rowindex, rowDomElement){
				parent.addOrRemoveUser(checked,rowdata);
			},
			rowAttrRender:function(rowData,rowIndex){
				if(rowData.Status=="否"){
					  return 'style="color:#e6640d;"';
				}
			},
			onCheckAllRow:function(checked,row){
				var data = Qm.getQmgrid().getData();
				for(var i in data){
					parent.addOrRemoveUser(checked, data[i]);
				}
			}
			
			</c:if>
			<c:if test="${param.checkbox=='0'}">
			onSelectRow: function(rowdata, rowindex, rowDomElement){
				parent.addOrRemoveUser(true,rowdata);
			},
			onUnSelectRow: function(rowdata, rowindex, rowDomElement){
				parent.addOrRemoveUser(false,rowdata);
			}</c:if>
		}
	};
	
	function loadGridData(orgId) {
		Qm.config.defaultSearchCondition[0].value = orgId;
		Qm.searchData();
	}
	
	//表格渲染时，将被选择的角色勾选
	function initGridSelectRange(){
		var idRange = parent.getSelectUserArr();
		Qm.getQmgrid().selectRange("id", idRange);
	}
</script>
</head>
<body>
	<qm:qm pageid="user_sel" singleSelect="${param.checkbox=='0'}" pagesize="100">
		<qm:param name="org_id" value="${param.orgId}" compare="=" />
	</qm:qm>
</body>
</html>
