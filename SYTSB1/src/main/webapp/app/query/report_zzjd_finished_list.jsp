<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>制造监督检验已封存报告</title>
		<%@include file="/k/kui-base-list.jsp"%>
		<script type="text/javascript">
		var w = window.screen.availWidth;
		var h = window.screen.availHeight;
        var qmUserConfig = {
        	sp_defaults:{columnWidth:0.33,labelAlign:'right',labelSeparator:'',labelWidth:100},	// 默认值，可自定义
            sp_fields:[
            	{name:"sn", compare:"like"},
            	{name:"report_sn", compare:"like"},
            	{name:"device_type_code", compare:"="},
            	{name:"made_unit_name", compare:"like"},
            	{name:"inspection_unit_name", compare:"like"},
            	{name:"check_op_name", compare:"like"},
            	{name:"lrry", compare:"like"}
            ],
            tbar:[
            	{ text:'查看报告', id:'showReport',icon:'detail', click: showReport},
            	{ text:'流转过程', id:'turnHistory',icon:'follow', click: getFlow},
            	<sec:authorize access="hasRole('delReport')">
            	{ text :'报告作废',id : 'del',icon : 'delete',click : del},
            	</sec:authorize>
            	{ text:'打印报告', id:'print',icon:'print', click: doPrintReport}
            ],
            listeners: {
                selectionChange : function(rowData,rowIndex){
                	selectionChange();
                },
        		rowDblClick :function(rowData,rowIndex){
        		}
            }
        };
        
        // 行选择改变事件
		function selectionChange() {
	   		var count = Qm.getSelectedCount(); // 行选择个数
	     	if(count == 1){
	            Qm.setTbar({del:true,turnHistory: true, showReport: true, print: true});            	
	 		}else if(count > 1){
	       		Qm.setTbar({del:true, turnHistory: false, showReport: false, print: true});
	    	}else{
	    		Qm.setTbar({del:false, turnHistory: false, showReport: false, print: false});
	    	}
		}
		
		// 查看报告
		function showReport(){	
			var id = Qm.getValueByName("inspection_info_id").toString();	// 业务信息ID
			var url = "inspection/zzjd/showReport.do?id="+id;
			top.$.dialog({
				width : w, 
				height : h, 
				lock : true, 
				title:"查看报告",
				content: 'url:'+url,
				data : {"window" : window}
			}).max();
		}
	
		// 打印报告
		function doPrintReport(){	
			var ids = Qm.getValuesByName("inspection_info_id").toString();	
			top.$.dialog({
				width : w, 
				height : h, 
				lock : true, 
				title : "打印报告",
				content: 'url:app/query/report_zzjd_print_index.jsp?id='+ids,
				data : {"window" : window}
			}).max();
		}
	
		//流转过程
		function  getFlow(){
			 top.$.dialog({
		   		width : 400, 
		   		height : 700, 
		   		lock : true, 
		   		title: "流程卡",
		   		content: 'url:inspection/zzjd/getFlowStep.do?ins_info_id='+Qm.getValueByName("inspection_info_id"),
		   		data : {"window" : window}
		   	});
		}
	
		// 报告作废
		function del(){	
			 $.del("确定作废？",
			    		"inspection/zzjd/delReport.do",
			    		{"ids":Qm.getValuesByName("inspection_info_id").toString()});
		}
        
        // 刷新Grid
        function refreshGrid() {
            Qm.refreshGrid();
        }
    </script>
	</head>
	<body>	
		<qm:qm pageid="zzjd_finished_list" singleSelect="false">
		</qm:qm>
	</body>
</html>
