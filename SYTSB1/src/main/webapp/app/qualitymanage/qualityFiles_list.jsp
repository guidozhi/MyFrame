<%@ page import="com.khnt.security.util.SecurityUtil" %>
<%@ page import="com.khnt.security.CurrentSessionUser" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    CurrentSessionUser user = SecurityUtil.getSecurityUser();
    String userid = user.getId();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>列表页面</title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript">
var file_type = null;
var file_type_name = null;
	var qmUserConfig = {
		sp_defaults:{columnWidth:0.3,labelAlign:'right',labelSeparator:'',labelWidth:120},	
		sp_fields: [ 
			{name: "file_num",compare: "like"}, 
			{name: "file_name",compare: "like"},
			{group: [
				{name: "implement_date", compare: ">="},
				{label: "到", name: "implement_date", compare: "<=", labelAlign: "center", labelWidth: 20}
			]}
		],
		tbar: [ {
			text: '详情', id: 'detail', icon: 'detail', click: detail
		}, 
		<sec:authorize access="hasRole('per_quality_file_his')">
			"-", {
				text: '新增', id: 'add', icon: 'add', click: add
			}, "-", {
				text: '修改', id: 'modify', icon: 'modify', click: modify
			},  "-", {
				text: '关联设备类别', id: 'refRep', icon: 'modify', click: refRep
			},  "-", {
				text: '启用', id: 'use', icon: 'ok', click: use
			},"-", {
				text: '作废', id: 'del', icon: 'delete', click: del
			},
		</sec:authorize> 
		"-", {
			text: '提交修改申请', id: 'apply', icon: 'add', click: apply
		}],
		listeners: {
			rowClick: function(rowData, rowIndex) {
			},
			rowDblClick: function(rowData, rowIndex) {
				detail();
			},
			rowAttrRender: function (rowData, rowid) {
                if(rowData.status=='1') // 记录为绿色
                {
                    return "style='color:blue'";
                }
                if(rowData.status=='2') // 记录为红色
                {
                    return "style='color:red'";
                }
            },
			selectionChange: function(rowData, rowIndex) {
				var count = Qm.getSelectedCount();
				Qm.setTbar({
					detail: count==1,
					modify: count==1,
					refRep: count == 1,
					use: count>0,
					apply: count==1,
					del: count>0
				});
			}
		}
	};
	
	function add() {
		var url = 'url:app/qualitymanage/qualityFiles_detail.jsp?pageStatus=add';
		/* if(file_type!=null&&file_type!="zltxfl"){
			url = url +'&file_type='+file_type+"&file_type_name="+file_type_name;
		} */
		top.$.dialog({
			width: 850,
			height: 600,
			lock: true,
			parent: api,
			data: {
				window: window,
				file_type_name : file_type_name,
				file_type : file_type
			},
			title: "新增",
			content: url
		});
	}

	function modify() {
		var id = Qm.getValueByName("id");
		var status = Qm.getValueByName("status");
		if(status!='0'){
			$.ligerDialog.alert("只能修改没有启用的文件信息！");
			return;
		}
		
		top.$.dialog({
			width: 850,
			height: 600,
			lock: true,
			parent: api,
			data: {
				window: window
			},
			title: "修改",
			content: 'url:app/qualitymanage/qualityFiles_modify_detail.jsp?id=' + id + '&pageStatus=modify'
		});
	}
	function refRep() {
		var id = Qm.getValueByName("id");
		top.$.dialog({
			width: 850,
			height: 600,
			lock: true,
			parent: api,
			data: {
				window: window
			},
			title: "修改",
			content: 'url:app/qualitymanage/qualityFiles_modify_detail.jsp?id=' + id + '&pageStatus=modify&report=1'
		});
	}

	function detail() {
		var id = Qm.getValueByName("id");
		top.$.dialog({
			width: 850,
			height: 600,
			lock: true,
			parent: api,
			data: {
				window: window
			},
			title: "详情",
			content: 'url:app/qualitymanage/qualityFiles_modify_detail.jsp?id=' + id + '&pageStatus=detail'
		});
	}

	function del() {
		$.del("确定删除？", "qualityManagerFilesAction/changeStatus.do", {
			"ids": Qm.getValuesByName("id").toString(),"status":"9"
		});
		submitAction();
	}
	
	function apply(){
		var id = Qm.getValueByName("id");
		var status = Qm.getValueByName("status");
		if(status!='1'){
			$.ligerDialog.alert("请选择已经启用的文件信息！");
			return;
		}
		var file_name=Qm.getValueByName("file_name");
		var file_num=Qm.getValueByName("file_num");
		var file_type=Qm.getValueByName("file_type");
		if(file_type=="作业指导书"){
			top.$.dialog({
				width: 1000,
				height: 730,
				lock:true,
				title:"作业指导书修改申请",
				data: {window:window,file_name:file_name,file_num:file_num},
				content: 'url:app/fwxm/scientific/instruction/instruction_sq_detail.jsp?status=add&quality_file_id='+id
			});
		}else{
			top.$.dialog({
				width: 900,
				height: 600,
				lock: true,
				parent: api,
				data: {
					window: window,file_name:file_name,file_num:file_num
				},
				title: "提交修改申请",
				content: 'url:app/qualitymanage/qualityFiles_apply_detail.jsp?file_id=' + id + '&step=apply&pageStatus=add'
			});	
		}	
	}
	
	function use(){
		var status = Qm.getValuesByName("status");
		for (var i = 0; i < status.length; i++) {
			if(status[i]!='0'){
				$.ligerDialog.alert("请选择新增没有启用的文件信息！");
				return;
			}
		}
		
		$.ligerDialog.confirm('确定启用选择的体系文件?', function (yes) { 

			if(yes){
				$.post("qualityManagerFilesAction/changeStatus.do", {
					"ids": Qm.getValuesByName("id").toString(),"status":"1"
				},function(res){
					if(res.success){
						top.$.notice("启用成功！");
						submitAction();
					}
				})
				
			}
		})
				
	}
	
	function submitAction() {
		Qm.refreshGrid();
	}
	
	function loadGridData(nodeId, nodeName, url) {
		file_type = nodeId;
		file_type_name = nodeName;
		//device_id = nodeId;
		if (nodeId != null) {
				Qm.setCondition([ {
					name : "file_type",
					compare : "=",
					value : nodeId
				} ]);
		} else {
			Qm.setCondition([]);
		}
		Qm.searchData();
	}
</script>
</head>
<body>
	<div class="item-tm" id="titleElement">
	    <div class="l-page-title">
			<div class="l-page-title-note">提示：列表数据项
				<font color="blue">“蓝色”</font>代表文件已经启用;
				<font color="red">“红色”</font>代表文件在修订中。
				
			</div>
		</div>
	</div>
	<qm:qm pageid="tzsb_quality_files" script="false" singleSelect="false">
    	 <!--qm:param name="str1" compare="like" value="A"/-->
    </qm:qm>
</body>
</html>