<%@page import="com.khnt.rtbox.template.constant.RtPath"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>制造监督检验报告信息查询</title>
		<%@include file="/k/kui-base-list.jsp"%>
		<script type="text/javascript" src="rtbox/app/templates/rtbox.js"></script>
		<script type="text/javascript">
		var w = window.screen.availWidth;
		var h = window.screen.availHeight;
        var qmUserConfig = {
        	checkRowId:"inspection_info_id",
        	sp_defaults:{columnWidth:0.33,labelAlign:'right',labelSeparator:'',labelWidth:100},	// 默认值，可自定义
            sp_fields:[
				{name:"report_sn", compare:"like"},
				{name:"report_name", compare:"like"},
				{name:"made_unit_name", compare:"like"},
            	{name:"check_unit_id", compare:"="},
            	{name:"check_op_name", compare:"like"},
            	{name:"lrry", compare:"like"},
            	{group:[
					{name:"advance_time", id:"advance_time1", compare:">="},
					{label:"到", name:"advance_time", id:"advance_time2", compare:"<=", labelAlign:"center", labelWidth:20}
				]},
            	{group:[
					{name:"print_time", id:"print_time1", compare:">="},
					{label:"到", name:"print_time", id:"print_time2", compare:"<=", labelAlign:"center", labelWidth:20}
				]},
				{name:"fee_status", compare:"="}
            ],
            tbar:[
				{ text:'查看报告', id:'showReport',icon:'detail', click: showReport},
 				<sec:authorize access="hasRole('report_print')">
 				'-', 
				{ text:'打印报告', id:'print',icon:'print', click: doPrintReport},
 				</sec:authorize>
 				"-",
 				{ text:'下载附件', id:'download',icon:'excel-export', click: downloadAttachments},
 				"-",
 				{ text:'导出excel', id:'exportReportToExcel',icon:'excel-export', click: exportReportToExcel},
 				'-', 
 				{ text:'流转过程', id:'turnHistory',icon:'follow', click: turnHistory},
 				"-",
 				{text : '应收金额合计', id : 'payTotal', icon : 'help', click : payTotal}            
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
	            Qm.setTbar({turnHistory: true, showReport: true, print: true,payTotal:true,download:true, exportReportToExcel:true});            	
	 		}else if(count > 1){
	       		Qm.setTbar({turnHistory: false, showReport: false, print: true,payTotal:true,download:false, exportReportToExcel:true});
	    	}else{
	    		Qm.setTbar({turnHistory: false, showReport: false, print: false,payTotal:false,download:false, exportReportToExcel:false});
	    	}
		}
		
		// 流转过程
		function turnHistory(){	
			top.$.dialog({
	   			width : 400, 
	   			height : 700, 
	   			lock : true, 
	   			title: "流程卡",
	   			content: 'url:inspection/zzjd/getFlowStep.do?ins_info_id='+Qm.getValueByName("inspection_info_id").toString(),
	   			data : {"window" : window}
	   		});
		}
		
		// 查看报告
		function showReport(){
			var id = Qm.getValueByName("inspection_info_id").toString();
			var ispdf =  Qm.getValuesByName("export_pdf").toString();
			var report_id = Qm.getValueByName("report_type").toString();	// 报告类型
			var reportType = Qm.getValueByName("reportType").toString();	// 新老报表识别代码
			
			var to_swf = Qm.getValueByName("to_swf").toString();
			var inspect_date = Qm.getValueByName("advance_time").toString();
			var date = inspect_date.substring(0,4)+inspect_date.substring(5,7)+inspect_date.substring(8,10);
			var report_sn = Qm.getValueByName("report_sn").toString();
			if("2" == reportType){
				var pdf_export_ps =  Qm.getValuesByName("pdf_export_ps").toString();	// 新报表导出pdf标记
				var pdf_export_att =  Qm.getValuesByName("pdf_export_att").toString();	// 新报表导出pdfid
				if(pdf_export_ps == "1" || (ispdf!=null&&ispdf!=""&&ispdf!="undefined")){
					if(ispdf!=null&&ispdf!=""&&ispdf!="undefined"){
						showFile(id,to_swf,date,ispdf,report_sn);
					}else{
						top.$.dialog({
							width : 800, 
							height : 400, 
							lock : true, 
							title:"查看报告",
							content: 'url:app/flow/reportPdfPrint/report_doc.jsp?status=detail&id='+id,
							data : {"window" : window,"pdf_export_att":pdf_export_att}
						}).max();
					}
				}else{
					//科鸿rtbox报表
					var code = Qm.getValueByName("rtbox_code");
					var param=[
						{name:"id",value:id},
						{name:"fk_report_id",value: report_id},
						{name:"status",value:"detail"},
						{name:"pageStatus",value:"detail"},
						{name:"isBatchBusiness",value:"1"}];
					opRt(code,param,"<%=RtPath.tplPageDir%>");
				}
			}else{
				if(ispdf!=null&&ispdf!=""&&ispdf!="undefined"){
					showFile(id,to_swf,date,ispdf,report_sn);
				}else{
					var w=window.screen.availWidth;
					var h=(window.screen.availHeight);
					var url = "report/query/showReport.do?id="+id+"&report_id="+report_id;
					top.$.dialog({
						width : w, 
						height : h, 
						lock : true, 
						title:"查看报告",
						content: 'url:'+url,
						data : {"window" : window}
					}).max();
				}
			}
			
		}

		// 打印报告
		function doPrintReport(){	
			var ids = Qm.getValuesByName("inspection_info_id").toString();	
			var flow_note_name = Qm.getValuesByName("flow_note_name").toString();	// 当前步骤
			if(flow_note_name.indexOf("录入")!=-1 || flow_note_name.indexOf("审")!=-1 || flow_note_name.indexOf("签发")!=-1){
				$.ligerDialog.error("所选报告中包含有未签发的报告，不能打印，请重新选择！");
				return;
			}else{
				var ispdf =  Qm.getValuesByName("export_pdf").toString();
				var pdf = "";
				if(ispdf!=null){
					var pdflist = ispdf.split(",");
					for(var i=0;i<pdflist.length;i++){
						pdf += pdflist[i];
					}
				}
				if(ispdf==null || ispdf=="" || pdf==""){
					top.$.dialog({
						width : w, 
						height : h, 
						lock : true, 
						title : "打印报告",
						content: 'url:app/query/report_zzjd_print_index.jsp?id='+ids+'&printType=1',
						data : {"window" : window}
					}).max();
				}else{
					printPdf();
				}
			}
		}
        
        // 下载附件
		function downloadAttachments() {
			var is_upload = Qm.getValueByName("is_upload").toString();
			if(is_upload != "1"){
				$.ligerDialog.alert('亲，系统暂不支持该类报告的附件下载功能哦！', "提示");
				return;
			}
			top.$.dialog({
				width : 600, 
				height : 400, 
				lock : true, 
				title : "下载附件", 
				data : {"window" : window},
				cancel : true,
				content : 'url:app/flow/report_attachment_upload.jsp?status=detail&info_id=' + Qm.getValueByName("inspection_info_id").toString()
			});
		}
        
		// 预收金额合计
		function payTotal(){
			var amount = Qm.getValuesByName("advance_fees").toString();
			doTotal(amount,"预收金额");
		}
		
		function doTotal(amount,title){
			var str = new Array();
			str = amount.split(",");
			var total = 0;
			if (str != null && str.length > 0) {
				for ( var i = 0; i < str.length; i++) {
					if(str[i]==''||str[i]==null){
							str[i]=0;
					}
					total = total + parseFloat(str[i]);
				}
				$.ligerDialog.alert(title+'合计：' + total + "元。");
			}
		}
		
		// 将报告导出excel
		function exportReportToExcel(){
			var do_export = true;		
			var info_ids = Qm.getValuesByName("inspection_info_id").toString();	
			var export_excels = Qm.getValuesByName("export_excel").toString();
			if(export_excels.indexOf("0") != -1){
				$.ligerDialog.error("所选报告不支持导出excel！");
				do_export = false;
				return;
			}
			
			if(do_export){
				$("#info_ids").val(info_ids);
				$("body").mask("正在导出数据,请等待...");
		    	$("#form1").attr("action","inspection/zzjdinfo/exportReport.do");
		    	$("#form1").submit();
		    	$("body").unmask();
			}
		}
        
		function printPdf(ids,report_sns,export_pdfs,acc_id,flow_num,printcopies) {
			if(ids == "" || ids == null || ids == "undefined"){
				var ids = Qm.getValuesByName("inspection_info_id");
				var report_sns = Qm.getValuesByName("report_sn");
				var export_pdfs = Qm.getValuesByName("export_pdf");
				var acc_id = Qm.getValuesByName("activity_id");
				var flow_num = Qm.getValuesByName("flow_num_id");
				var printcopies = Qm.getValuesByName("printcopies");
				
				var l = ids.length;
				openN(0, ids, report_sns, export_pdfs, l, acc_id, flow_num,
						printcopies, 1,"0","打印报告");
			}else{
				var l = ids.length;
				openN(0, ids, report_sns, export_pdfs, l, acc_id, flow_num,
						printcopies, 1,"0","打印报告");
			}
		}
		
		function openN(i, ids, report_sns, export_pdfs, l, acc_id, flow_num, 
				printcopies, type,print_type,print_remark) {	
			top.$.dialog({
				width : 800,
				height : 400,
				lock : true,
				title : "打印报告",
				content : 'url:app/flow/reportPdfPrint/report_doc.jsp?status=detail&print=true&id='+ ids[i],
				data : {
					"window" : window,
					"report_sn" : report_sns[i],
					"date" : export_pdfs[i],
					"acc_id" : acc_id[i],
					"flow_num" : flow_num[i],
					"printcopies" : printcopies[i],
					"type" : 1,
					"print_type":print_type,
					"print_remark":print_remark
				},
				close : function() {
					i++;
					if (i < l) {
						openN(i, ids, report_sns, export_pdfs, l, acc_id, flow_num, printcopies, type,print_type,print_remark);
					} else {		
						//addReportTransferInfos(ids);
						Qm.refreshGrid();
					}
				}
			}).max();
		}
		
        // 刷新Grid
        function refreshGrid() {
            Qm.refreshGrid();
        }
     	// 查看报告文档
        function showFile(id,to_swf,date,ispdf,report_sn){
        	if(to_swf==null||to_swf==""||to_swf=="undefined"){
    			$("body").mask(" 第一次查看该报告，正在准备文档，请稍后......");
    			$.post("inspectionInfo/basic/pdf2Swf.do",{"pdfPath":date+"/"+report_sn+"/"+report_sn+".pdf","swfPath":date+"/"+report_sn,"swfName":report_sn},function(res){
    				if(res.success){
            			$("body").unmask();
            			top.$.dialog({
            				width : 800, 
            				height : 400, 
            				lock : true, 
            				title:"查看报告",
            				content: 'url:app/query/showFile.jsp?status=detail&path='+date+"/"+report_sn+"/"+report_sn+".swf"+'&id='+id,
            				data : {"window" : window,"report_sn":report_sn,"date":ispdf},
            				close:function(res){
            					Qm.refreshGrid();
            					}
    					}).max(); 
    				}
    			}) 
    		}else{
    			 top.$.dialog({
    				width : 800, 
    				height : 400, 
    				lock : true, 
    				title:"查看报告",
    				content: 'url:app/query/showFile.jsp?status=detail&path='+date+"/"+report_sn+"/"+report_sn+".swf"+'&id='+id,
    				data : {"window" : window,"report_sn":report_sn,"date":ispdf}
    			}).max(); 
    		}
        }
    </script>
	</head>
	<body>	
		<form name="form1" id="form1" action="" getAction="" target="_blank">
			<input type="hidden" name="info_ids" id="info_ids" value=""/>
		</form>
		<qm:qm pageid="report_zzjd_query" singleSelect="false">
		</qm:qm>
		<script type="text/javascript">
			Qm.config.columnsInfo.fee_status.binddata=<u:dict code="PAYMENT_STATUS"></u:dict>;
			//Qm.config.columnsInfo.report_type.binddata=<u:dict sql="select t.id,t.report_name from base_reports t where t.enter_type='1' "></u:dict>;
			Qm.config.columnsInfo.check_unit_id.binddata=<u:dict sql="select id code, ORG_NAME text from SYS_ORG where ORG_CODE like 'cy%' and ORG_CODE != 'cy4_1' and ORG_CODE != 'cy8' order by orders "></u:dict>;
		</script>
	</body>
</html>
