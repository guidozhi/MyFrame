<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html  xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<script type="text/javascript">
var ids="";
	var qmUserConfig = {
			sp_defaults:{columnWidth:0.3,labelAlign:'right',labelSeparator:'',labelWidth:120},	
			sp_fields:[ {name:"status",compare: "="},
						{name:'invoice_type',compare:"like"},
						{name:'money_type',compare:'like'},
						{name:'lead_name',compare:'like'},
						{name:'invoice_name',compare:'like'},
						{group:[
							  {name:'invoice_code',compare:'>='},
							  {label:'到',name:'invoice_code', compare:'<=',labelAlign:"center",labelWidth:20}
							]},
						{group:[
							{name:'lead_date',compare:'>='},
							{label:'到',name:'lead_date', compare:'<=',labelAlign:"center",labelWidth:20}
						]},
						{group:[
							{name:'invoice_date',compare:'>='},
							{label:'到',name:'invoice_date', compare:'<=',labelAlign:"center",labelWidth:20}
						]},
						{name:'invoice_unit',compare:'like'}
					],
			
					tbar:[{text: '详情', id: 'detail', icon: 'detail', click:detail}, 
					      '-',
					      { text:'修改', id:'modify',icon:'modify', click:modify},
					      '-',
					      {text: '领用', id: 'lead',	 icon: 'consuming', click:lead}, 
					      '-',
					      {text: '作废', id: 'cancel', icon:'delete', click:cancel}
					      , '-',
					      {text: '发票金额统计', id: 'money', icon:'count', click:money}
					      ,'-',
					      {text: '历史记录',id:'leadLog',icon:'view', click:leadLog}
					      
		
					],
					listeners:{
						rowClick:function(rowData,rowIndex){
						},
						rowDblClick:function(rowDate,rowIndex){
							Qm.getQmgrid().selectRange("id", [rowDate.id]);
							detail();
						},
						
						selectionChange: function(rowData, rowIndex) {
							var count = Qm.getSelectedCount();
							Qm.setTbar({
	        					detail: count==1,
	        					modify: count==1,
	        					lead: count>0,
	        					cancel: count>0,
	        					money: count>0,
	        					leadLog: count==1
	        				});

							if(count>0){
								//判断按钮可用情况
								var up_status = Qm.getValuesByName("status").toString();
								var strs = new Array(); //定义一数组
	        	 					strs = up_status.split(","); //字符分割
	        	 				var isUse=false;
	        	 				var isLead=false;
								for(var i=0;i<count;i++){
									if(strs[i]=="未使用"){
										
									}if (strs[i]=="已使用"){
										isUse=true;
									}if( strs[i]=="已领用"){
										isLead=true;
									}if( strs[i]=="已作废"){
										Qm.setTbar({detail: true,lead : false,cancel:false});
									}
									
								}
								if(isUse||isLead){
									Qm.setTbar({detail: true,lead : false,cancel:true});
								}
							}
							
				         }
					}
			};
	
	function detail(){
		var id = Qm.getValueByName("id").toString();
		top.$.dialog({
			width : 700,
			height : 250,
			lock : true,
			title : "业务详情",
			content : 'url:app/finance/cwInvoice_lead_datail1.jsp?pageStatus=detail&id='+ id,
			data : {
				"window" : window
			}
		});
	}
	//修改
    function modify(){
    	var id = Qm.getValueByName("id").toString();
		top.$.dialog({
			width: 800,
			height: 300,
			lock:true,
			title:"修改",
			data: {window:window},
			content: 'url:app/finance/cwInvoice_lead_datail2.jsp?pageStatus=modify&id='+id
		});
    }
	function money(){
		var moneys=Qm.getValuesByName("invoice_money").toString();
		var money=new Array();
		money=moneys.split(",");
		var newmoney=0.0;
		for(var i=0;i<money.length;i++){
			newmoney+=money[i]*1
		}
		 $.ligerDialog.alert('选中发票金额为：'+newmoney)
	}
	function leadLog(){
	 	var id = Qm.getValueByName("id").toString();
		top.$.dialog({
			width : 700,
			height : 500,
			lock : true,
			title : "历史记录",
			content : 'url:app/finance/cwInvoice_lead_record.jsp?pageStatus=detail&id='+ id,
			data : {
				"window" : window
			}
		});
	}
	/* function leadLog(){
	 	var id = Qm.getValueByName("pk_lead_id").toString();
		top.$.dialog({
			width : 700,
			height : 300,
			lock : true,
			title : "领用记录",
			content : 'url:app/finance/cwInvoice_lead_datail.jsp?pageStatus=detail&id='+ id,
			data : {
				"window" : window
			}
		});
	} */
	
	
	
	function lead(){
		var invoice_codes = Qm.getValuesByName("invoice_code").toString();
		ids = Qm.getValuesByName("id").toString();
		
		top.$.dialog({
			width : 700, 
			height : 250, 
			lock : true, 
			title : "发票领取", 
			data : {"window" : window,
				"ids":ids,
				"invoice_codes":invoice_codes,
				"count":Qm.getSelectedCount()},
			content : 'url:app/finance/cwInvoice_lead_datail.jsp?&pageStatus=add'
			
		});
		
	}
	
	function cancel(){
		 $.del("确定作废当前选中的数据吗?",
		    		"cwInvoiceLead/lead/cancel.do",
		    		{"ids":Qm.getValuesByName("id").toString()});
	}
	 $(function(){
	        importData();
	    });
	function importData() {
	  //创建上传实例
	  khFileUploader = new KHFileUploader({
	      fileSize: "10mb",//文件大小限制
	      buttonId: "importData",//上传按钮ID
	      container: "filecontainer3",//上传控件容器ID
	      title: "银行转账数据",//文件选择框提示
	      saveDB : false,
	      extName: "xls,xlsx",//文件扩展名限制
	      fileNum: 1,//限制上传文件数目
	      callback : function(files){
	                  //文件无误，执行保存
	                  saveData(files);
	      }
	  });
	}

	  //上传完成，开始保存汇编数据
	  function saveData(files){
		  $("body").mask("正在上传...");
		  $.post("cwInvoice/reg/saveTask.do",{files:$.ligerui.toJSON(files)},function (data) {
		          $("body").unmask();
		          if (data.success) {
	                    $.ligerDialog.success("成功导入&nbsp;"+"<span style='color:green;'>"+data.total+"</span>"+"&nbsp;条发票!");
		              Qm.refreshGrid();
		          } else {
		              $.ligerDialog.error("保存失败！<br/>");
		          }
		     },"json");
	  }
	 
</script>
</head>
<body>
	<p id="filecontainer3" style="margin:5px">
		<a class="l-button-warp l-button" id="importData">
			<span class="l-button-main l-button-hasicon">
				<span class="l-button-text">导入发票信息</span>
				<span class="l-button-icon iconfont l-icon-excel-import"></span>
			</span>
		</a>
	</p>
	<qm:qm pageid="TJY2_CW_FPGL" script="false" singleSelect="false"></qm:qm>
</body>
</html>