<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>设备采购申请</title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript" src="pub/bpm/js/util.js"></script>

<script type="text/javascript">
var qmUserConfig = {
	sp_defaults:{columnWidth:0.3,labelAlign:'right',labelSeparator:'',labelWidth:120},	// 默认值，可自定义
    sp_fields:[
		{name:"project_name", compare:"like"},
		{name:"project_num", compare:"like"},
		{name:"project_no", compare:"like"}
	],
	tbar:[
    	{ text:'详情', id:'detail',icon:'detail', click: detail},
        "-",
        { text:'审核', id:'sign',icon:'edit',click:sign}
     ],
     listeners: {
     	selectionChange : function(rowData,rowIndex){
        	var count = Qm.getSelectedCount(); // 行选择个数
			Qm.setTbar({
				detail: count==1,
				sign:count==1
			});
        	console.log("<sec:authentication property='principal.id' htmlEscape='false' />");
        	if(count==1){
        		if(rowData.audit_id!="<sec:authentication property='principal.id' htmlEscape='false' />"){
        		Qm.setTbar({
					sign:false
				});
        	}
        	}
			

        	
       },rowAttrRender : function(rowData, rowid) {
	       var fontColor="black";
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
			content: 'url:app/fwxm/scientific/instruction/instruction_sq_detail.jsp?status=detail&id=' + id,
			data:{window:window}
		});
	}
        
     //新增
     function add(){
	 	top.$.dialog({
			width: 1000,
			height: 730,
			lock:true,
			title:"采购申请",
			data: {window:window},
			content: 'url:app/fwxm/scientific/instruction/instruction_detail.jsp?status=add'
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
			content: 'url:app/fwxm/scientific/instruction/instruction_detail.jsp?status=modify&type=modify&id='+Qm.getValueByName("id")
		});
     }
     //删除
     function del(){
     	$.del("确定要删除？删除后无法恢复！","equipmentBuyAction/delete.do",{"ids":Qm.getValuesByName("id").toString()});
     } 
     //提交
     function submit(){
 		var id = Qm.getValueByName("id");
 		if(!id){
             $.ligerDialog.alert("请先选择要提交审核的数据！");
             return;
         }
         $.ligerDialog.confirm('是否提交审核？', function (yes){
         if(!yes){return false; }
         $("body").mask("提交中...");
          //获取业务流程
          getServiceFlowConfig("TJY2_EQUIPMENT_BUY","",function(result,data){
         	 if(result){
                  top.$.ajax({
                      url: "equipmentBuyAction/subFolw.do?id="+id+"&flowId="+data.id+"&typeCode=TJY2_EQUIPMENT_BUY&status="+status,
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
             }else{
             	$.ligerDialog.error('出错了！请重试！'); 
              $("body").unmask();
             }
          });
         });
 	}
     function sign(){
    	 top.$.dialog({
 			width: 1000,
 			height: 730,
 			lock:true,
 			title:"审核",
 			data: {window:window},
 			content: 'url:app/fwxm/scientific/instruction/instruction_sq_detail.jsp?status=modify&type=modify&id='+Qm.getValueByName("id")+"&tj=1"
 		});

//      	top.$.dialog({
// 			width: 1000,
// 			height: 730,
// 			lock:true,
// 			title:"审核",
// 			data: {window:window},
// 			content: 'url:app/fwxm/scientific/instruction/choose_opinion_sq.jsp?status=modify&type=audit&id='+Qm.getValueByName("id")
// 		});
     }

</script>

	<div class="item-tm" id="titleElement">
        <div class="l-page-title">
            <div class="l-page-title-note">提示：列表数据项
                <font color="black">“黑色”</font>代表未处理，
                <font color="#EEE685">“黄色”</font>代表退回修改，
            </div>
        </div>
    </div>
</head>
<body>
	<qm:qm pageid="TJY2_INSTRUCTION_SQ" singleSelect="true">
	<qm:param name="status" value="1" compare="="  />
	</qm:qm>
</body>
</html>