<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>设备采购申请</title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript" src="pub/bpm/js/util.js"></script>
<script test="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript">
var qmUserConfig = {
	sp_defaults:{columnWidth:0.3,labelAlign:'right',labelSeparator:'',labelWidth:120},	// 默认值，可自定义
    sp_fields:[
	    {name:"project_name", compare:"like"},
	    {name:"instruction_number", compare:"like"},
	    {name:"type", compare:"="}
	],
	tbar:[
    	{ text:'详情', id:'detail',icon:'detail', click: detail},
//         "-",
//         { text:'增加', id:'add',icon:'add', click: add},
        "-",
        { text:'处理', id:'modify',icon:'modify', click:modify},
        "-",
        { text:'删除', id:'del',icon:'delete', click:del},
//         "-",
//         { text:'提交', id:'submit',icon:'submit',click:submit},
        "-",
        { text:'打印需求计划表', id:'print',icon:'print',click:print}
     ],
     listeners: {
     	selectionChange : function(rowData,rowIndex){
        	var count = Qm.getSelectedCount(); // 行选择个数
			Qm.setTbar({
				detail: count==1,
// 				modify: count==1&&rowData.status=="编制中",
				del: count>0&&rowData.status=="编制中",
// 				submit: count==1&&rowData.status=="编制中",
				print:count==1&&rowData.status=="批准通过"
			});
			
       },rowAttrRender : function(rowData, rowid) {
    	   var fontColor="black";
	       if(rowData.status=='审核中'){
	       		fontColor="blue";
	       }else if(rowData.status=='批准中'){
	       		fontColor="orange";
	       }else if (rowData.status=='批准通过'){
	       		fontColor="green";
	       }
	       if(rowData.is_return=='1'){
	    	   fontColor="#EEE685";
	       }
	       return "style='color:"+fontColor+"'";
		},rowDblClick :function(rowData,rowIndex){
			detail(rowData.id);
		}
     }
};

	function detail(id){
		if($.type(id)!="string"){
			id = Qm.getValueByName("id").toString();
		}
		top.$.dialog({
			width: 1000,
			height: 730,
			lock:true,
			title:"详情",
			content: 'url:app/fwxm/scientific/instruction/instruction_detail.jsp?status=detail&id=' + id,
			data:{window:window}
		});
	}
        
     //新增
     function add(){
	 	top.$.dialog({
			width: 1000,
			height: 730,
			lock:true,
			title:"新增",
			data: {window:window},
			content: 'url:app/fwxm/scientific/instruction/instruction_detail.jsp?status=add&tj=0'
		});
     }
        
     //修改
     function modify(){
		top.$.dialog({
			width: 1000,
			height: 730,
			lock:true,
			title:"修改",
			data: {window:window},
			content: 'url:app/fwxm/scientific/instruction/instruction_detail.jsp?status=modify&type=modify&id='+Qm.getValueByName("id")+"&tj=0"
		});
     }
     //删除
     function del(){
     	$.del("确定要删除？删除后无法恢复！","com/tjy2/instruction/deleteBase.do",{"id":Qm.getValuesByName("id").toString()});
     } 
     //提交
     function submit(){
 		var id = Qm.getValueByName("id");
 		if(!id){
             $.ligerDialog.alert("请先选择要提交审核的数据！");
             return;
         }

 		$.ligerDialog.confirm('是否以当前操作人为目标提交', function (yes){
               if(yes){
            	   doSumbit(id,"<sec:authentication property='principal.id' htmlEscape='false' />","<sec:authentication property='principal.name' htmlEscape='false' />");
               }else{
            	   selectUnitOrUser(1,0, null, null, function(data){
	           			if(data.code!=null&&data.code.length>0&&data.code!="undefined"){
	           				doSumbit(id,data.code,data.name);
	           			}else{
	           				top.$.dialog.alert("请选择一个用户!");
	           			}
            	   });
               }
 		});
 		
 	}
     function doSumbit(id,tjUserId,tjUserName){
      $("body").mask("提交中...");
      top.$.ajax({
          url: "com/tjy2/instruction/subAudit.do?id="+id+"&tjUserId="+tjUserId+"&tjUserName="+tjUserName,
          type: "GET",
          dataType:'json',
          async: false,
          success:function (data) {
              if (data) {
             	$("body").unmask();
            	 	top.$.notice('提交成功！！！');
                 Qm.refreshGrid();
              }
          },
          error:function () {
         	 $("body").unmask();
         	 $.ligerDialog.error('出错了！请重试！!');
          }
      });
     }
     function sign(){
     	top.$.dialog({
			width: 720,
			height: 530,
			lock:true,
			title:"合同签订",
			data: {window:window},
			content: 'url:app/equipment/apply/equipment_buy_detail.jsp?status=modify&type=sign&id='+Qm.getValueByName("id")
		});
     }
     function print(){
  		var status=Qm.getValueByName("status");
  		/* if(status=="5"){
  			$.ligerDialog.alert("审批未通过，请重新选择！！！");
  			return;
  		} */
  		var id=Qm.getValueByName("id");
  		top.$.dialog({
   			width : 1000, 
   			height : 800, 
   			lock : true, 
   			title:"打印作业指导书任务书",
   			content: 'url:app/fwxm/scientific/instruction/instruction_jx_doc.jsp?id='+id,
   			data :{"window" : window}
   		}).max();
  	}
</script>
	<div class="item-tm" id="titleElement">
        <div class="l-page-title">
            <div class="l-page-title-note">提示：列表数据项
                 <font color="black">“黑色”</font>代表未提交，
                <font color="blue">“蓝色”</font>代表审核中，
                <font color="orange">“橙色”</font>代表批准中，
                <font color="green">“绿色”</font>代表批准通过。
                <font color="#EEE685">“黄色”</font>代表退回修改。
            </div>
        </div>
    </div>
</head>
<body>
	<qm:qm pageid="TJY2_INSTRUCTION_LIS" singleSelect="true"></qm:qm>
</body>
</html>