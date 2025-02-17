<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>消息管理</title>
<%@include file="/k/kui-base-list.jsp"%>
<script test="text/javascript">
	var qmUserConfig = {
		sp_defaults : {
			labelWidth : 70
		},
		sp_fields : [
		 	{name:"types",compare:"like"},
			{name:"sender_name",compare:"like"},
			{group:[{
				label:"发送时间",
				name:"send_time",compare:">="
			},{
				label:"到",labelWidth:20,labelAlign:"center",
				name:"send_time",compare:">="
			}]}
		],
		<sec:authorize ifNotGranted="super_administrate">
			<tbar:toolBar type="tbar" code="pub_message">
			</tbar:toolBar>,
		</sec:authorize>
		<sec:authorize access="hasRole('super_administrate')">
			tbar:[
             { text:'发送消息', id:'add',icon:'add', click: add},
             "-",
             { text:'接收短信', id:'msg',icon:'talk', click: smsReceive}
        	],
		</sec:authorize>
		listeners : {
			selectionChange : function(rowData, rowIndex) {
				var count = Qm.getSelectedCount();
				Qm.setTbar({
					detail : count == 1,
					edit : count == 1,
					del : count >= 1 
				});
			},
			detail: {
				height: "auto", 
				onShowDetail: showMsgSender
			}
		}
	};
	
	function showMsgSender(row,detailPanel,callback){
  	 	var grid = document.createElement('div'); 
  	 	$(grid).addClass(".ttt");
        $(detailPanel).append(grid);
        $(grid).css('margin',0).ligerGrid({
            columns: [
				{ display: '消息类型', name: 'type',render: changeType},
				{ display: '接收人 ', name: 'personName'},
				{ display: '手机号码/邮箱', name: 'account'},
				{ display: '发送时间', name: 'sendTime',type:'date',width:120},
				{ display: '状态', name: 'status',render: changeStatus},
				{ display: '发送情况 ', name: 'result',frozen: true },
			], 
			isScroll: false, 
			width: '100%', 
			dateFormat:'yyyy-MM-dd hh:mm',  
			data: f_getOrdersData(row.id) , 
			showTitle: false, 
			columnWidth: 100,
			onAfterShowData: callback,
			frozen: false,
			rownumbers: true,
			usePager: false,
			rowAttrRender: function(rowdata,rowid){
				if(rowdata.status=="1"){
					return "style='color:red'";
				}
			}
        });  
	} 
	function changeType(rowdata, index, value){
	 	if(value=="sms"){
			return "短信";
		}else if(value=="system"){
			return "系统内消息";
		} else if(value=="email"){
			return "邮件";
		}else{
			return value;
		}
	}
	function changeStatus(rowdata, index, value){
		if(value=="0"){
			return "未发送";
		}else if(value=="1"){
			return "<a style='color:red;text-decoration:underline' href='javascript:showMsg(\"" + rowdata.result + "\")' title='点击查看'>发送失败</a>"
		}else if(value=="2"){
			return "发送成功";
		}else if(value=="3"){
			return "对方已读";
		}
	}
	
	
	function f_getOrdersData(msgId){
		var data = { Rows: []};   
		var myData={Rows:[]};
		$.ajax({
    		type:"get",
    		dataType:"json",
    		async: false,
    		data:{"msgId":msgId},
    		url:"pub/message/getMsgSendsById.do",
    		success:function(datas){
    			for(var i=0;i<datas.length;i++){
					var x = {
						"type": datas[i].type,
						"personName": datas[i].personName,
					 	"account":datas[i].account,
					 	"sendTime":datas[i].sendTime,
					 	"status":datas[i].status,
					 	"result":datas[i].result
					};
					myData.Rows.push(x); 
    			}
    		}	
    	});
		for(var i=0;i<myData.Rows.length;i++){
			var x = {
				"type": myData.Rows[i].type,
				"personName": myData.Rows[i].personName,
				"account": myData.Rows[i].account,
				"sendTime": myData.Rows[i].sendTime,
				"status": myData.Rows[i].status,
				"result": myData.Rows[i].result
			};
			data.Rows.push(x);
		}
    	return data; 
    }
	
	function showMsg(msg){
		top.$.dialog({
			width : 500,
			height : 200,
			cancel : true,
			title : "查看发送结果",
			content : "<p style='text-align:left;padding:5px;width:100%;'>" + msg + "</p>"
		});
	}

	function add() {
		top.$.dialog({
			width : 800,
			height : 550,
			lock : true,
			title : "发送消息",
			data : {Qm:Qm},
			content : 'url:pub/message/message_detail.jsp?pageStatus=add'
		});
	}
	
	function smsReceive(){
		top.$.dialog({
			lock : true,
			title : "接收短信",
			data : {Qm:Qm},
			content : 'url:pub/message/sms_list.jsp'
		}).max();
	}
</script>
</head>
<body>
	<qm:qm pageid="pub_message_one" singleSelect="true"></qm:qm>
	<script test="text/javascript">
		Qm.config.sortInfo= [{field:'create_time',direction:'desc'}];
	</script>
	<div id="myDiv" style="display: none;"></div>
</body>
</html>