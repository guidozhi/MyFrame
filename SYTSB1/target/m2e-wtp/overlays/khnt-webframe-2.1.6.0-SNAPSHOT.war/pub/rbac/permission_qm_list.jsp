﻿<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>权限管理</title>
		<%@include file="/k/kui-base-list.jsp"%>
		<script type="text/javascript">
          var qmUserConfig = {
          sp_fields:[
              {label:"权限代码",name:"p_code",compare:"like"},
              {label:"权限名称",name:"p_name",compare:"like"},
              {label:"权限名称",name:"remark",compare:"like"}
           ],
            tbar:[
                { text:'增加 ', id:'add',icon:'add', click: add},
                "-",
                { text:'修改 ', id:'modify',icon:'modify', click:modify},
                "-",
                { text:'删除 ', id:'del',icon:'delete', click:del}
            ],
            listeners: {
                selectionChange :function(rowData,rowIndex){selectionChange();}
            }
        };
       
       
        //行选择改变事件
        function selectionChange(){
			var count=Qm.getSelectedCount();//行选择个数
          	Qm.setTbar({modify:count==1,del:count>0});
        }
        
		//新增系统权限
		function add(){
			top.$.dialog({
				width:600,
				height:300,
				lock:true,
				title:"新增系统权限",
				content: 'url:pub/rbac/permission_detail.jsp?status=add',
				data:{window:window}
			});
		}
		
		//刷新列表
		function submitAction() {
			Qm.refreshGrid();
		}
        
        //修改系统权限
		function modify(){
			top.$.dialog({
				width:600,
				height:300,
				lock:true,
				title:"修改系统权限",
				data:{window:window},
				content: 'url:pub/rbac/permission_detail.jsp?status=modify&id='+Qm.getValueByName("id")
			});
		} 
    
        //删除任务节点功能
        function del(){
            $.del("删除指定的权限,用户对应的权限可能要失效，你是否要删除指定的权限?","rbac/permission/delete.do",{"ids":Qm.getValuesByName("id").toString()});
        }
    </script>
	</head>
	<body>
		<qm:qm pageid="sys_01" script="false" singleSelect="false"></qm:qm>
	</body>
</html>
