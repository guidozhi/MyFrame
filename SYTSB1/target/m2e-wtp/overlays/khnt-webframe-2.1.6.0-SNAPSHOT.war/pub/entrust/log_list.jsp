<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>系统用户登录日志</title>
		<%@include file="/k/kui-base-list.jsp"%>
		<script type="text/javascript">
		var qmUserConfig = {
            sp_defaults:{columnWidth:0.25,labelAlign:'right',labelSeparator:'',labelWidth:60},// 可以自己定义
            sp_fields:[
				{name:"user_name",compare:"like",value:""},
				{name:"login_ip",compare:"like",value:""},
				//{label:"登录时间:",name:"login_time",compare:">=",value:""},{label:"到:",name:"login_time",compare:"<=",value:"",labelWidth:20}
				{columnWidth:0.5,group:[{name:"login_time",compare:">=",value:"",width:"100"},{label:"至",name:"login_time",compare:"<=",value:"",labelAlign:"center",labelWidth:20,width:"100"}]},
            ],
      		<sec:authorize ifNotGranted="super_administrate">
			<tbar:toolBar type="tbar" code="entrustLogList">
			</tbar:toolBar>,
		</sec:authorize>
		<sec:authorize access="hasRole('super_administrate')">
			tbar:[
              { text:'删除', id:'del',icon:'delete', click:del }
            ],
		</sec:authorize>
            listeners: {
                rowClick :function(rowData,rowIndex){},
                rowDblClick :function(rowData,rowIndex){detail();},
                selectionChange :function(rowData,rowIndex){selectionChange()}
            }
        };
        //行选择改变事件
        function selectionChange(){
			var count = Qm.getSelectedCount();//行选择个数
			Qm.setTbar({del : count > 0});
        }
		
		function detail(){
			
		};
  
       //删除任务节点功能
       function del(){
          $.del(kFrameConfig.DEL_MSG,"entrust/log/delete.do",{"ids":Qm.getValuesByName("id").toString()});
       }    
    </script>
	</head>
	<body>
		<qm:qm pageid="entrust_log" script="false" singleSelect="false">
		</qm:qm>
			<script test="text/javascript">
		Qm.config.sortInfo= [{field:'login_time',direction:'desc'}];
	</script>
	</body>
</html>
