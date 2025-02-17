<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>部门管理列表</title>
		<%@include file="/k/kui-base-list.jsp"%>
		<script type="text/javascript">
        var qmUserConfig = {
        	sp_defaults:{columnWidth:0.3,labelAlign:'right',labelSeparator:'',labelWidth:120},	// 默认值，可自定义
            sp_fields:[
				{name:"emp_name", compare:"like"},
				{name:"org_name", compare:"like"}
            ],
            tbar:[
            	{ text:'详情', id:'detail',icon:'detail', click: detail}
            	,"-",
                { text:'增加', id:'add',icon:'add', click: add},
                "-",
                { text:'修改', id:'modify',icon:'modify', click:modify},
                "-",
                { text:'删除', id:'del',icon:'delete', click:del}
            ],
            listeners: {
                selectionChange : function(rowData,rowIndex){
                	selectionChange();
                },
        		rowDblClick :function(rowData,rowIndex){
        			detail(rowData.id);
        		},
        		rowAttrRender : function(rowData, rowid) {
    	            
    	        }
            }
        };
        
        // 行选择改变事件
		function selectionChange() {
	   		var count = Qm.getSelectedCount(); //行选择个数
			Qm.setTbar({
				detail: count==1,
				modify: count==1,
				del: count>0
			});
		}
        
		function detail(){
			var id = Qm.getValueByName("id").toString();
			top.$.dialog({
				width:600,
				height:300,
				lock:true,
				title:"详情",
				content: 'url:app/humanresources/department/department_manager_detail.jsp?pageStatus=detail&id='+id,
				data:{window:window}
			});
		}
        
        //新增
        function add(){
			top.$.dialog({
				width:600,
				height:300,
				lock:true,
				title:"新增",
				data: {window:window},
				content: 'url:app/humanresources/department/department_manager_detail.jsp?pageStatus=add'
			});
        }
        
        //修改
        function modify(){
        	var id = Qm.getValueByName("id").toString();
			top.$.dialog({
				width:600,
				height:300,
				lock:true,
				title:"修改",
				data: {window:window},
				content: 'url:app/humanresources/department/department_manager_detail.jsp?pageStatus=modify&id='+id
			});
        } 
        
        //删除
        function del(){
            $.del("确定要删除？删除后无法恢复！","orgidLeaderidAction/delete.do",{"ids":Qm.getValuesByName("id").toString()});
        }
        // 刷新Grid
        function refreshGrid() {
            Qm.refreshGrid();
        }
    </script>
	</head>
	<body>
		<qm:qm  pageid="TJY2_RL_DEPARTMENT" singleSelect="false"></qm:qm>
	</body>
</html>
