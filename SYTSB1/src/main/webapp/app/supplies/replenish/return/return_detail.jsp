<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus = "${param.pageStatus}">
<title>固定资产信息</title>
  <%@ include file="/k/kui-base-form.jsp"%>
<!-- 生成条形码JS导入 -->
<script type="text/javascript" src="app/common/lodop/LodopFuncs.js"></script>
<script type="text/javascript">
	var pageStatus = "${param.pageStatus}";
	
	$(function() {
		$("#form1").initForm({
			success: function (response) {//处理成功
	    		if (response.success) {
	            	top.$.dialog.notice({
	             		content: "保存成功！"
	            	});
	         		api.data.window.refreshGrid();
	            	api.close();
	    		} else {
	           		$.ligerDialog.error('保存失败！<br/>' + response.msg);
	      		}
			}, getSuccess: function (response){
				if (response.attachs != null && response.attachs != undefined)
					showAttachFile(response.attachs);
			},
			showToolbar: true,
            toolbarPosition: "bottom",
            toolbar: [
                   		{text: "保存", icon: "save", click: function(){
		  				//表单验证
				    	if ($("#form1").validate().form()) {
				    		$("#form1").submit();
				    	}else{$.ligerDialog.error('提示：' + '请将信息填写完整后保存！');}
		  				}},
		  				{text:"导出验收表",icon:"export" },
		  				{text:"导出入库单",icon:"export" },
	  				{text: "关闭", icon: "cancel", click: function(){api.close();}}]
    	});
		
		init();
		
	});

	var grid;
    function init(){
        var column=null;
     	   column =[
					{display: "<a class='l-a iconfont l-icon-add' href='javascript:grid.addEditRow();'><span>增加</span></a>",
						isSort: false, 
						width: '30',
						render : function(item, index) {
							return "<a class='l-a l-icon-del' href='javascript:delDevice(grid," + index + ")'><span>删除</span></a>";
						}
					},
 		 			{ display: '产品名称', name: 'INVOICE_NO',align: 'center', width: 78,editor: { type: 'text' },totalSummary:{ render: function (e){return "合计：" }}},
 		 			{ display: '供应商', name: 'CLSS',align: 'center', width: 180,editor: { type: 'text' },totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '类型', name: 'lx',align: 'center', width: 80,editor: { type: 'text' },totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '规格及型号', name: 'COMPANY_NAME',align: 'center', width: 80,editor: { type: 'text' },totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '单位', name: 'DEPT',align: 'center', width: 80,editor: { type: 'text' },totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '数量', name: 'PAY_NO',align: 'center', width: 80,editor: { type: 'int' },totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '金额', name: 'je',align: 'center', width: 80,editor: { type: 'float' },totalSummary:{ render: function (e){return e.sum }}},
 		 			{ display: '税额', name: 'se',align: 'center', width: 80,editor: { type: 'float' },totalSummary:{ render: function (e){return  e.sum }}},
 		 			{ display: '总金额', name: 'zje',align: 'center', width: 80,editor: { type: 'float' },totalSummary:{ render: function (e){return e.sum }}},
 		 			{ display: '使用部门', name: 'PAY_TYPE',align: 'center', width: 100,totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '入库时间', name: 'time',align: 'center', width: 80,type:'date',format: 'yyyy-MM-dd',editor: { type: 'date'},totalSummary:{ render: function (e){return "" }}},
 		 			{ display: '备注', name: 'bz',align: 'center', width: 180,editor: { type: 'text' },totalSummary:{ render: function (e){return "" }}}
 		 			
 				]
     	   
     
 		
    		grid = $("#checkGrid").ligerGrid({
             columns:column, 
             enabledEdit: true,
             data:{Rows:[]},
             rownumbers:true,
             frozenRownumbers: false,
             usePager: false,
             height:'90%'
 	  	});
     	  
    }

	 function delDevice(row,index){
		 row.deleteSelectedRow();
	 }
    function query(){
      	$("body").mask("正在统计数据,请等待...");

    	var beanData = api.data.beans;//父窗口的数据
    	console.log("eeee");
    	console.log(beanData);
//         $.getJSON(encodeURI("feeStatisticsAction/deptDetail.do?startDate=${param.startDate}&endDate=${param.endDate}&dept=${param.dept}"),function(res){
        	var gridDataArr=beanData;//new Array();
//         	for(var i=0; i<beanData.length;i++){
//         		var rowData=new Object();
//         		rowData.INVOICE_NO="电池";
//         		rowData.CLSS="京东";
//         		rowData.lx="办公用品";
//         		rowData.COMPANY_NAME="DJI";
//         		rowData.DEPT="只";
//         		rowData.PAY_NO="5";
//         		rowData.je="300";
//         		rowData.se="1";
//         		rowData.time="2018-09-26";
//         		rowData.zje="1505";
//         		rowData.PAY_TYPE="信息中心";
//         		rowData.CASH_PAY="";
        		

//         		gridDataArr.push(rowData);
//         	}

		    if(gridDataArr!=null){
			   grid.loadData({Rows:gridDataArr});
		    }
		    $("body").unmask();
        	
//         });
        
        
    }
</script>
</head>
<body  onload="query()">
    <form id="form1" action="com/tjy2/supplier/save.do" getAction="com/tjy2/supplier/detail.do?id=${param.id}">
     <input type="hidden" name="id"/>
     <input type="hidden" name="createUserId"/>
     <input type="hidden" name="createUserName"/>
     <input type="hidden" name="createDate"/>
     <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
       <tr>
       		<td class="l-t-td-left"></td>
       		<td class="l-t-td-right"></td>
       		<td class="l-t-td-left"></td>
       		<td class="l-t-td-right"></td>
       		<td class="l-t-td-left"></td>
       		<td class="l-t-td-right"></td>
       </tr>
       <tr>
       		<td class="l-t-td-left">收货单位：</td>
       		<td class="l-t-td-right" colspan="5"><input name="gysmc" value="四川省特种设备检验研究院" id="gysmc" type="text" ltype='text' validate="{maxlength:200}" /></td>
       		
       </tr>
       <tr>
       		<td class="l-t-td-left">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
       		<td class="l-t-td-right" colspan="5"><input name="gysdz" id="gysdz" value="成都市成华区东风路北二巷4号" type="text" ltype='text' validate="{maxlength:200}" /></td>
       		
       </tr>
       <tr>
       		<td class="l-t-td-left">联系人姓名：</td>
       		<td class="l-t-td-right"><input value="夏塽"/></td>
       		<td class="l-t-td-left">联系人部门：</td>
       		<td class="l-t-td-right"><input value="信息中心"/></td>
       		<td class="l-t-td-left">电话：</td>
       		<td class="l-t-td-right"><input value="18628140010"/></td>
       </tr>
       
      </table>
      
	
<div id="checkGrid" style="overflow: auto;"></div>
    </form> 


</body>
</html>
