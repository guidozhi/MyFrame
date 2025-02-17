<%@page import="com.khnt.utils.StringUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="java.util.Map"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用车申请列表查询页</title>
<%@ include file="/k/kui-base-list.jsp"%>
<%
	CurrentSessionUser cur_user = SecurityUtil.getSecurityUser();
	String userId = cur_user.getId();
	
	String orgId = cur_user.getDepartment().getId();
	
	Map<String, String> roles = cur_user.getRoles();
	String role_name = "";	
	for(String roleid : roles.keySet()){
		Object obj = roles.get(roleid);
		if(StringUtil.isNotEmpty(role_name)){
			role_name+= ",";
		}
		role_name+= obj;
	}
%>
<script type="text/javascript" src="app/oa/select/org_select.js"></script>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript">
	var roles = "<%=role_name%>";
	var qmUserConfig = {
		sp_defaults : {
			columnWidth : 0.33,
			labelAlign : 'right',
			labelSeparator : '',
			labelWidth : 60
		},//可以自己定义 layout:column,float,
		sp_fields : [
				{
					name : "apply_dep_id",
					compare : "=",
					xtype : "combo",
					onBeforeOpen : showDepartList
				},
				{
					name : "use_dep_id",
					compare : "=",
					xtype : "combo",
					onBeforeOpen : showDepartList
				},
				{
					name : "plate_number",
					compare : "like"
				},
				{
					group : [
							{
								name : "apply_date",
								compare : ">=",
								value : ""
							},
							{
								label : "到",
								name : "apply_date",
								compare : "<=",value:"",labelAlign:"center",labelWidth:20}
						],columnWidth:0.33
					}, {group:[
							{name:"out_date",compare:">=",
								value : ""
							},
							{
								label : "到",
								name : "out_date",
								compare : "<=",value:"",labelAlign:"center",labelWidth:20}
						],columnWidth:0.33
					}, {group:[
							{name:"back_date",compare:">=",
								value : ""
							}, {
								label : "到",
								name : "back_date",
								compare : "<=",
								value : "",
								labelAlign : "center",
								labelWidth : 20
							} ],
					columnWidth : 0.33
				}, {
					name : "data_status",
					compare : "="
				} ],
		tbar : [ {
			text : '详情',
			id : 'detail',
			click : detail,
			icon : 'detail'
		}, '-', {
			text : '新增',
			id : 'add',
			click : add,
			icon : 'add'
		}, '-', {
			text : '修改',
			id : 'modify',
			click : modify,
			icon : 'modify'
		}, '-', {
			text : '删除',
			id : 'del',
			click : del,
			icon : 'del'
		}, '-', {
			text : '流转过程',
			id : 'turnHistory',
			icon : 'follow',
			click : turnHistory
		} ],
		listeners : {
			rowClick : function(rowData, rowIndex) {
			},
			rowDblClick : function(rowData, rowIndex) {
				detail();
			},
			selectionChange : function(rowData, rowIndex) {
				selectionChange();
			},
			rowAttrRender : function(rowData, rowid) {
				var fontColor="black";
	            // 0：提交申请/用车部门负责人待审核
	            if (rowData.status == '0'){
	            	fontColor="black";
	            }
	            // 1：办公室待审核
	            if (rowData.status == '1'){
	            	fontColor="blue";
	            }
	            // 2：分管院领导待审核
	            if (rowData.status == '2'){
	            	fontColor="purple";
	            }
	            // 3：车队负责人待审核
	            if (rowData.status == '3'){
	            	fontColor="red";
	            } 
	            // 4：派车中
	            if (rowData.status == '4'){
	            	fontColor="maroon";
	            }
	         	// 5：用车中
	            if (rowData.status == '5'){
	            	fontColor="orange";
	            }
	         	// 6：已收车
	            if (rowData.status == '6'){
	            	fontColor="green";
	            }
	         	// 98：已退回
	            if (rowData.status == '98'){
	            	fontColor="grey";
	            }
	         	// 99：已作废
	            if (rowData.status == '99'){
	            	fontColor="pink";
	            }
	            return "style='color:"+fontColor+"'";
			}
		}
	};

	//行选择改变事件
	function selectionChange() {
		var count = Qm.getSelectedCount();//行选择个数
		if (count == 0) {
			Qm.setTbar({
				modify : false,
				del : false,
				detail : false,
				turnHistory : false
			});
		} else if (count == 1) {
			Qm.setTbar({
				modify : true,
				del : true,
				detail : true,
				turnHistory : true

			});
		} else {
			Qm.setTbar({
				modify : false,
				del : true,
				detail : false,
				turnHistory : false
			});
		}
	}

	//显示部门选择列表
	function showDepartList() {
		var unitId = "<sec:authentication property="principal.unit.id" />";
		var unitName = "<sec:authentication property="principal.unit.orgName" htmlEscape="false"/>";
		$(this).data('unitId', unitId);
		$(this).data('unitName', unitName);
		showOrgList.call(this);
	}

	function add() {
		var width = 1050;
		var height = 500;
		top.$.dialog({
			width : width,
			height : height,
			lock : true,
			title : "新增用车申请",
			data : {
				"window" : window
			},
			content : 'url:app/car/car_apply_detail2.jsp?status=add'
		});
	}

	function detail() {
		var selectedId = Qm.getValueByName("id");
		var width = 1050;
		var height = 800;
		var windows = top.$.dialog({
			width : width,
			height : height,
			lock : true,
			title : "详情",
			data : {
				"window" : window
			},
			content : 'url:app/car/car_apply_detail2.jsp?status=detail' + '&id='
					+ selectedId
		});
	}
	
	function modify() {
		var status = Qm.getValueByName("status");
		if(status!='0' && status!='98'){
			$.ligerDialog.alert('该用车申请正在审批流程中不能修改！', "提示");
			return;
		}
		var selectedId = Qm.getValueByName("id");
		var width = 1050;
		var height = 350;
		var windows = top.$.dialog({
			width : width,
			height : height,
			lock : true,
			title : "修改用车申请",
			data : {
				"window" : window
			},
			content : 'url:app/car/car_apply_detail2.jsp?status=modify&id=' + selectedId
		});
	}
	
	

	function del() {
		var toDel = false;
		var selectedId = Qm.getValuesByName("id");
		if(roles.indexOf("车队负责人")!=-1){
			toDel = true;
		}
		if(!toDel){
			if (selectedId.length < 1) {
				$.ligerDialog.alert('请先选择要删除的数据项！', "提示");
				return;
			}
			
			var statusArr = Qm.getValuesByName("status");
			for(var i=0;i<statusArr.length;i++){
				if(statusArr[i]!='0' && statusArr[i]!='98'){
					$.ligerDialog.alert('您所选的用车申请暂不能删除！如需删除，请先联系审核人退回再操作！', "提示");
					return;
				}
			}
			
			var apply_users = Qm.getValuesByName("apply_user_id");
			for(var i=0;i<apply_users.length;i++){
				if(apply_users[i]!="<%=userId %>"){
					$.ligerDialog.alert('您所选的用车申请只能由申请人自己删除！', "提示");
					return;
				}
			}
		}
		
		$.ligerDialog.confirm(kui.DEL_MSG, function(yes) {
			if (yes) {
				$.ajax({
					url : "car/apply/del.do?ids=" + selectedId,
					type : "post",
					async : false,
					success : function(data) {
						if (data.success) {
							top.$.notice("删除成功！");
							Qm.refreshGrid();
						}
					}
				});
			}
		});
	}

	function turnHistory(){
		top.$.dialog({
	   		width : 400, 
	   		height : 700, 
	   		lock : true, 
	   		title: "流转过程",
	   		content: 'url:car/apply/getFlowStep.do?id='+Qm.getValueByName("id").toString(),
	   		data : {"window" : window}
	   	});
	}
	
	function refreshGrid() {
		Qm.refreshGrid();
	}
</script>
</head>

<body>
	<!-- 提示文字 -->
    <div class="item-tm" id="titleElement">
	    <div class="l-page-title">
			<div class="l-page-title-note">提示：
			    <font color="black">“黑色”</font>代表提交申请/用车部门负责人待审核；
				<font color="purple">“紫色”</font>代表分管院领导待审核；
				<font color="red">“红色”</font>代表车队负责人待审核；
				<font color="maroon">“褐红色”</font>代表派车中；
				<font color="blue">“蓝色”</font>代表办公室负责人待审核；
				<font color="orange">“橘色”</font>代表用车中；
				<font color="green">“绿色”</font>代表已收车；
				<font color="grey">“灰色”</font>代表已退回；
				<font color="pink">“粉色”</font>代表已作废。
			</div>
		</div>
	</div>
	<qm:qm pageid="car_apply_list" script="false" singleSelect="false">	
	<%
		if(StringUtil.isNotEmpty(role_name)){
			if(!role_name.contains("系统管理员") && !role_name.contains("车队负责人")){
				%>
				<qm:param name="apply_user_id" value="<%=userId%>" compare="=" />
				<%
			}
		}
	%>
	</qm:qm>
</body>
</html>