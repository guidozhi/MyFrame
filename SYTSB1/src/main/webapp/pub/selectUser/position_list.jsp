<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>机构人员列表</title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript">
	var checkbox = "${param.checkbox}";
	var isCheckbox = false;
	if (checkbox == 0)
		isCheckbox = true;
	else if (checkbox == 1)
		isCheckbox = false;
	var qmUserConfig = {
		sp_fields : [ 
			<c:choose>
			<c:when test="${param.position=='1'}">
				{
					label : "岗位",
					name : "position_name",
					compare : "like",
					labelAlign : "right",
					labelWidth : 40,
					width: 100
				}
			</c:when>
			<c:otherwise>
				{
					label : "姓名",
					name : "name",
					compare : "like",
					labelAlign : "right",
					labelWidth : 40,
					width: 100
				}
			</c:otherwise>
			</c:choose>
		],
		listeners : {
			onAfterShowData : function() {
				initGridSelectRange();
			},
			<c:choose>
			<c:when test="${param.position=='1'}">
				<c:if test="${param.checkbox=='1'}">
				onCheckRow : function(checked,rowdata, rowindex, rowDomElement){
					parent.addOrRemovePosition(checked,rowdata);
				},
				onCheckAllRow:function(checked,row){
					parent.removeAllUser();
					if(checked){
						var data = Qm.getQmgrid().getData();
						for(var i in data){
							parent.addOrRemovePosition(checked, data[i]);
						}
					}
				}
				
				</c:if>
				<c:if test="${param.checkbox=='0'}">
				onSelectRow: function(rowdata, rowindex, rowDomElement){
					parent.addOrRemovePosition(true,rowdata);
				}</c:if>
			</c:when>
			<c:otherwise>
				<c:if test="${param.checkbox=='1'}">
				onCheckRow : function(checked,rowdata, rowindex, rowDomElement){
					parent.addOrRemoveUser(checked,rowdata);
				},
				onCheckAllRow:function(checked,row){
					parent.removeAllUser();
					if(checked){
						var data = Qm.getQmgrid().getData();
						for(var i in data){
							parent.addOrRemoveUser(checked, data[i]);
						}
					}
				}
				
				</c:if>
				<c:if test="${param.checkbox=='0'}">
				onSelectRow: function(rowdata, rowindex, rowDomElement){
					parent.addOrRemoveUser(true,rowdata);
				}</c:if>
			</c:otherwise>
			</c:choose>
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
	<c:choose>
		<c:when test="${param.position=='1'}">
			<qm:qm pageid="position_sel" singleSelect="${param.checkbox=='0'}" pagesize="100">
				<qm:param name="org_id" value="${param.orgId}" compare="=" />
			</qm:qm>
		</c:when>
		<c:otherwise>
			<qm:qm pageid="TJY2_RL_EMPLOYEELIST" singleSelect="${param.checkbox=='0'}" pagesize="100">
				<qm:param name="org_id" value="${param.orgId}" compare="=" />
			</qm:qm>
		</c:otherwise>
	</c:choose>
</body>
</html>
