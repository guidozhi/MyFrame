<%@page import="com.lsts.device.bean.DeviceDocument"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.lsts.inspection.bean.ReportItemValue"%>
<%@ page import="com.lsts.report.bean.ReportItem"%>
<%@page import="com.khnt.pub.fileupload.bean.Attachment"%>
<%@page import="com.khnt.pub.fileupload.service.AttachmentManager"%>
<%@page import="com.lsts.inspection.dao.ReportPerDao"%>
<%@page import="com.lsts.common.dao.AttachmentsDao"%>
<%@page import="java.io.IOException"%>
<%@page import="util.TS_Util"%>
<%@ taglib uri="/WEB-INF/Ming.tld" prefix="ming" %>
<%@ page import="com.ming.webreport.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.khnt.utils.StringUtil" %>
<%@ page import="com.khnt.utils.DateUtil"%>
<%@ page import="com.lsts.ImageTool"%>
<%@ page import="com.lsts.inspection.bean.*" %>
<%@ page import="com.lsts.report.bean.*" %>
<%@ page import="oracle.sql.*" %>
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

<%


	Calendar cal = Calendar.getInstance();
	String check = request.getParameter("check");
	String isLast = request.getParameter("isLast");	// 状态（提交后关闭窗口）
	//判断是否是提交数据后，如是则关闭窗口
	if (StringUtil.isNotEmpty(isLast))
	{
%>
	<script>
		//alert("打印完成！");
		parent.left.printAll();
	</script>
<%
	//return;
	}
%>
<%
    
	DeviceDocument deviceDocument = (DeviceDocument)request.getSession().getAttribute("deviceDocument");
	List<ReportItemRecord> reportItemRecordList = (List<ReportItemRecord>)request.getSession().getAttribute("reportItemRecordList");
	//List<ReportItem> reportItemList = (List<ReportItem>)request.getSession().getAttribute("reportItemList");
    InspectionInfo inspectionInfo = (InspectionInfo)request.getSession().getAttribute("inspectionInfo");
    Report report = (Report)request.getSession().getAttribute("report");
    String report_code = report.getReport_code().trim();
    String is_print_double = report.getIs_print_double();	// 是否双面打印 （0：否 1：是）
	String printer_name = (String)request.getSession().getAttribute("printer_name");	// 打印机名称  
	String print_copies = request.getParameter("printcopies");	// 打印份数
	String print_pages = request.getParameter("printpages");	// 打印页码
	
    //String report_item = inspectionInfo.getReport_item();//tsjy_inspection_info.getStringValue("report_item",0);
     String ysjl_item = inspectionInfo.getYsjl_item();
    String device_sort_code = deviceDocument.getDevice_sort_code();
    String big_class = StringUtil.isNotEmpty(device_sort_code)?device_sort_code.substring(0, 1):"";	// 设备大类
    if(StringUtil.isEmpty(big_class)){
    	big_class = StringUtil.isNotEmpty(deviceDocument.getDevice_sort())?deviceDocument.getDevice_sort().substring(0, 1):"";	// 设备大类
    }
    String com_name = "";
    String device_area_name = "";
    String device_street_name = "";

	
    // 构造DataRecord、MRDataSet对象
    MRDataSet mrds = new MRDataSet();
    DataRecord rec = new DataRecord();
    
	 // 检验项目表信息处理
    for (int i = 0; i < reportItemRecordList.size(); i++)
    {
    	ReportItemRecord reportItemRecord = reportItemRecordList.get(i);
    	
    	String item_value = "";
    	if("ZLSCQK".equals(reportItemRecord.getItem_name()) || "LAST_CHECK_PROBLEMS".equals(reportItemRecord.getItem_name())
    			|| reportItemRecord.getItem_name().contains("REMARKS")){
    		item_value = TS_Util.nullToString(reportItemRecord.getItem_value());
    	}else{
    		item_value = TS_Util.nullToString(reportItemRecord.getItem_value()).replaceAll("\r|\n","");
    	}
    	if(reportItemRecord.getItem_name().equals("DRAFT_DATE")){
    		if(StringUtil.isNotEmpty(item_value)){
    			if(item_value.indexOf("/")!=-1){
    				cal.setTime(DateUtil.convertStringToDate("yyyy/MM/dd", item_value));
    			}else{
    				cal.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", item_value));
    			}
        		String newdate = DateUtil.getDateTime("yyyy年MM月dd日",cal.getTime()); 
        		rec.setValue("DRAFT_DATE",newdate);
    		}
    	}else if("COM_NAME".equals(reportItemRecord.getItem_name())){
			com_name = TS_Util.nullToString(item_value);
		}else if("DEVICE_AREA_NAME".equals(reportItemRecord.getItem_name())){
			device_area_name = TS_Util.nullToString(item_value);
		}else if("DEVICE_STREET_NAME".equals(reportItemRecord.getItem_name())){
			device_street_name = TS_Util.nullToString(item_value);
		}else if(reportItemRecord.getItem_name().equals("INSPECTION_DATE_END")){
    		if(StringUtil.isNotEmpty(reportItemRecord.getItem_value())){
    			if(reportItemRecord.getItem_value().indexOf("/")!=-1){
    				cal.setTime(DateUtil.convertStringToDate("yyyy/MM/dd", reportItemRecord.getItem_value()));
    			}else{
    				cal.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", reportItemRecord.getItem_value()));
    			}
        		String newdate = DateUtil.getDateTime("yyyy年MM月dd日",cal.getTime()); 
        		rec.setValue("INSPECTION_DATE_END",newdate);
    		}
    	}else{
    		rec.setValue(reportItemRecord.getItem_name(), item_value); 
    	}
    }

  	//处理图片信息
  	Map<String,Object> picMap 
  		= (Map<String,Object>)request.getSession().getAttribute("PICTURE");
  	for (String key : picMap.keySet()) {
  		String keyid = key.substring(0,key.length()-1);
  		rec.setValue(keyid, "");
  		if(key.endsWith("P")){
  			rec.setValue(key,  (byte[])picMap.get(key) );
  		}else{
  			rec.setValue(key, 
  					TS_Util.nullToString(picMap.get(key)).getBytes("GB2312") );
  		}
  	} 
	 
 	// 人员电子签名处理
 	byte[] check_op_img = (byte[])request.getAttribute("check_op_img");
 	byte[] examine_op_img = (byte[])request.getAttribute("examine_op_img");
 	byte[] issue_op_img = (byte[])request.getAttribute("issue_op_img");
 	byte[] enter_op_img = (byte[])request.getAttribute("enter_op_img");
 	// 校核人员手写签名图片处理
 	byte[] exam_op_img = (byte[])request.getAttribute("exam_op_img");
 	
 	// 设置人员姓名不可见，电子签名可见
 	rec.setValue("INSPECTION_OP_STR", "");
 	rec.setValue("INSPECTION_AUDIT_STR","");
 	rec.setValue("INSPECTION_CONFIRM_STR","");
 	rec.setValue("INSPECTION_ENTER_STR","");
 	
 	// 检验员电子签名
 	rec.setValue("INSPECTION_OP_PICTURE", check_op_img!=null?check_op_img:"");			
  	// 审核电子签名
 	rec.setValue("INSPECTION_AUDIT_PICTURE", examine_op_img!=null?examine_op_img:"");
 	// 签发(批准)电子签名
 	rec.setValue("INSPECTION_CONFIRM_PICTURE", issue_op_img!=null?issue_op_img:"");	                                                                              	                                                             
 	// 编制电子签名
 	rec.setValue("INSPECTION_ENTER_PICTURE", enter_op_img!=null?enter_op_img:"");	
 	// 校核人员手写签字图片
 	rec.setValue("EXAMINE_NAME_1", exam_op_img!=null?exam_op_img:"");	
 	
 	//将下次检验日期按照yyyy年MM月dd日方式显示
 	 String newdate = "";
 	if(inspectionInfo.getLast_check_time()!=null){
 		String last_check_time = DateUtil.getDate(inspectionInfo.getLast_check_time());
 		if(StringUtil.isNotEmpty(last_check_time)){
 			newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getLast_check_time()); 
 			rec.setValue("LAST_INSPECTION_DATE",newdate);
 		}
 	}
 	//将批准日期按照yyyy年MM月dd日方式显示
 	if(inspectionInfo.getIssue_Date() !=null){
 		String confirm_date = DateUtil.getDate(inspectionInfo.getIssue_Date());
 		if(StringUtil.isNotEmpty(confirm_date)){
 			newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getIssue_Date()); 
 			rec.setValue("CONFIRM_DATE",newdate);
 		}
 	}
 	//将审批日期按照yyyy年MM月dd日方式显示
 	if(inspectionInfo.getExamine_Date() != null){
 		String examine_date = DateUtil.getDate(inspectionInfo.getExamine_Date());
 		if(StringUtil.isNotEmpty(examine_date)){
 			newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getExamine_Date()); 
 			rec.setValue("AUDIT_DATE",newdate);	
 		}
 	}
 	//将编制日期按照yyyy年MM月dd日方式显示
 		if(inspectionInfo.getEnter_time() != null){
 			String enter_date = DateUtil.getDate(inspectionInfo.getEnter_time());
 			if(StringUtil.isNotEmpty(enter_date)){			
 				newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getEnter_time()); 
 				rec.setValue("DRAFT_DATE",newdate);	
 			}
 		}
 	
 	//将检验日期按照yyyy年MM月dd日方式显示
 	if(inspectionInfo.getAdvance_time() != null){
 		String advance_time = DateUtil.getDate(inspectionInfo.getAdvance_time());
 		if(StringUtil.isNotEmpty(advance_time)){
 			newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getAdvance_time()); 
 			rec.setValue("INSPECTION_DATE",newdate);
 			rec.setValue("INSPECTION_DATE＿1", newdate);
 			rec.setValue("INSPECTION_DATE＿2", newdate);
 			rec.setValue("INSPECTION_DATE_TOP",newdate );	
 		}
 	}
 	
 	//将校核日期按照yyyy年MM月dd日方式显示
 		if(inspectionInfo.getConfirm_Date() != null){
 			String confirm_date = DateUtil.getDate(inspectionInfo.getConfirm_Date());
 			if(StringUtil.isNotEmpty(confirm_date)){			
 				newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getConfirm_Date()); 
 				rec.setValue("EXAMINE_DATE",newdate);	
 			}
 		}
 	
 	// 报告书使用单位
 	if(inspectionInfo.getReport_com_name() != null){
 		rec.setValue("COM_NAME", 
 				TS_Util.nullToString(inspectionInfo.getReport_com_name()).getBytes("GB2312") );
 		com_name = TS_Util.nullToString(inspectionInfo.getReport_com_name());	
 	}
 	
 	if(StringUtil.isNotEmpty(com_name)){
 		rec.setValue("COM_NAME", com_name);
 	}	
 	
 	if(StringUtil.isEmpty(device_area_name)){
 		device_area_name = deviceDocument.getDevice_area_name();
 	}
 	if(StringUtil.isEmpty(device_street_name)){
 		device_street_name = deviceDocument.getDevice_street_name();
 	}
 	rec.setValue("DEVICE_AREA_NAME", device_area_name);
 	rec.setValue("DEVICE_STREET_NAME", device_street_name);
 	
	//rec.setValue("P_JGHZH", "TS7110306-2019");
	rec.setValue("TotalP", StringUtil.isNotEmpty(ysjl_item)?ysjl_item.split(",").length:0);
	mrds.addRow(rec);
	
    // 创建报表引擎对象，指定报表根目录，传递结果集，绑定报表
    MREngine engine = new MREngine(pageContext, StringUtil.isNotEmpty(report.getRootpath())?report.getRootpath():"");    
    engine.setUnicodeOption(1);
    // 用MRDataSet对象为报表提供数据集：
    engine.addMRDataSet(StringUtil.isNotEmpty(report.getMrdataset())?report.getMrdataset():"", mrds);
    engine.addReport(StringUtil.isNotEmpty(report.getYsjl_file())?report.getYsjl_file():"");	// 原始记录报表文件
    engine.bind();
%>
<html>
<head>
<title> 报表信息 </title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />

<%@include file="/k/kui-base-list.jsp"%>
<script language="javascript">
	var com_name = "<%=com_name%>";
	$(function() {
		var height = $(window).height()-$('.toolbar-tm').height();
		$("#scroll-tm").css({height:height});
		setReports();
	})
	//设置报表属性
	function setReports()
	{
		//MRViewer.ShowToolbar=false;
		report = MRViewer.Report;
		pagecount = MRViewer.PageCount;

		//设置显示页
		var ss = "<%=ysjl_item%>".split(",");	
		var ss_length = ss.length;
		var status = false;
		for(var i=0;i<pagecount;i++){
			for(var j=0;j<ss_length;j++){
				if((i+1)==ss[j])
				{
					status = true;
					page = report.Pages(i);
					page.Prop("Visible") = "True";
					break;
				}
				else
				{
					try{
						page = report.Pages(i);
						page.Prop("Visible") = "False";
					} catch(e){}
				}
			}
		}
		
		if(!status)
		{
			alert("报告模板上没有对应的索引！");
			api.close();	// window.close();
		}

		parseValueReports();
		MRViewer.Preview();
		<%
			if("pass".equals(check)){				
		%>
				/* doPrintreport();
				MRViewer_AfterPrint(1); */
				psss();
		<%
			}else if("nopass".equals(check)){
		%>
				nopass();
		<%
			}
		%>
	}
	
	function parseValueReports()
	{
		//把每一个是Memo的对象中间包含“”的并且不是总页数的控件对象都设置为可以提交的状态,提交KEY = memo中的字段名
		report = MRViewer.Report;
		pagecount = MRViewer.PageCount;
		var ss = '${REPORTPARA.report_item}'.split(",");
		for(var i=0;i<pagecount;i++){
			try{
				page = report.Pages(i);
				objCount = page.ObjectCount;
				for(var j=0;j<objCount;j++){
					obj = page.Objects(j); 
					if(obj.Prop("memo").indexOf("[BGDS.\"COM_NAME\"]")!=-1){
						if(com_name.indexOf("尊宴餐饮娱乐有限公司")!=-1){
							obj.Prop("Memo") = "成都珺龍尊宴餐饮娱乐有限公司";
						}else{
							obj.Prop("Memo") = com_name;
						}
					}
				}
			} catch(e){}
		}
	}
	
	<%-- //打印报告
	function doPrintreport()
	{
		var print_double = "<%=is_print_double %>";
		// 是否双面打印 （0：否 1：是）
		
		if("1"==print_double){
			MRViewer.PrintSetup(0,0,true,"<%=StringUtil.isNotEmpty(print_pages)?print_pages:""%>",0,<%=StringUtil.isNotEmpty(print_copies)?print_copies:1%>,true,"<%=StringUtil.isNotEmpty(printer_name)?printer_name:""%>", 100, print_double);
			MRViewer.Print(false);	// false不提示打印设置框，调用默认的
		}else{
			MRViewer.PrintSetup(0,0,true,"<%=StringUtil.isNotEmpty(print_pages)?print_pages:""%>",0,<%=StringUtil.isNotEmpty(print_copies)?print_copies:1%>,true,"<%=StringUtil.isNotEmpty(printer_name)?printer_name:""%>");
			MRViewer.Print(false);	// false不提示打印设置框，调用默认的
		}		
		//subP();
	} --%>
	
	<%-- function MRViewer_AfterPrint(flag)
	{
		if(flag==undefined || flag =="" ) {
			return;
		}
		//alert("print_afer_begin")
		//alert("print_afer")
		parent.left._opid<%=request.getParameter("opid")%>.innerHTML="<img src='k/kui/images/icons/16/check.png' border='0' >";
		subP();	// 打印完后写数据库记录打印操作
		//alert("MRViewer_AfterPrint")
	} --%>
	
	<%-- function subP()
	{
		parent.left._opid<%=request.getParameter("opid")%>.innerHTML="<img src='k/kui/images/icons/16/check.png' border='0' >";
		formObj.action="report/item/record/print_record.do?id=<%=inspectionInfo.getId() %>&report_id=<%=request.getSession().getAttribute("report_id") %>&isLast=yes&op_type=";
		formObj.submit();
	} --%>
	function pass()
	{
		parent.left._opid<%=request.getParameter("opid")%>.innerHTML="<img src='k/kui/images/icons/16/check.png' border='0' >";
		formObj.action="inspectionInfo/basic/reportCheck.do?ids=<%=inspectionInfo.getId() %>&check=pass";
		formObj.submit();
	}
	function nopass()
	{
		parent.left._opid<%=request.getParameter("opid")%>.innerHTML="<img src='k/kui/images/icons/16/check.png' border='0' >";
		formObj.action="inspectionInfo/basic/reportCheck.do?ids=<%=inspectionInfo.getId() %>&check=nopass";
		formObj.submit();
	}

	function showBB(){
		$("#sssss").show();
	}
	
	$(window).load(function() {
		$("#MRViewer").append('<param name="wmode" value="transparent" />');
	});
</script>
</head>
<body >
<form name="formObj" method="post" action="">
</form>
<div class="scroll-tm" style="overflow: hidden" id="sssss"> 
	<ming:MRViewer id="MRViewer" shownow="true"  width="100%"  height="100%" simple="false" invisiblebuttons="Print,Export,Close,PrintPopup,ExportPopup,Find,FindNext"  postbackurl="" canedit="false" wrapparams="true"  />
</div>
</body>
</html>