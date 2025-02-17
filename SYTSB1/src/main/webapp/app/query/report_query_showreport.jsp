<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.lsts.inspection.bean.ReportItemValue"%>
<%@ page import="com.lsts.report.bean.ReportItem"%>
<%@page import="com.khnt.pub.fileupload.bean.Attachment"%>
<%@page import="com.khnt.pub.fileupload.service.AttachmentManager"%>
<%@page import="com.lsts.inspection.dao.ReportPerDao"%>
<%@page import="com.lsts.common.dao.AttachmentsDao"%>
<%@page import="java.io.IOException"%>
<%@page import="com.lsts.device.bean.DeviceDocument"%>
<%@ taglib uri="/WEB-INF/Ming.tld" prefix="ming" %>
<%@ page import="com.ming.webreport.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.khnt.utils.StringUtil" %>
<%@ page import="com.khnt.utils.DateUtil"%>
<%@ page import="com.lsts.ImageTool"%>
<%@ page import="com.lsts.inspection.bean.*" %>
<%@ page import="com.lsts.report.bean.*" %>
<%@ page import="oracle.sql.*" %>
<%@ page import="util.*"%>
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

<%


	Calendar cal = Calendar.getInstance();
	String printout = request.getParameter("printout");
	String isLast = request.getParameter("isLast");	// 状态（提交后关闭窗口）
	//判断是否是提交数据后，如是则关闭窗口
	if (StringUtil.isNotEmpty(isLast))
	{
%>
	<script>
		alert("打印完成！");
		//parent.left.printAll();
	</script>
<%
	//return;
	}
%>
<%
    // 报告检验项目表信息
	List<ReportItemValue> reportItemValueList = (List<ReportItemValue>)request.getSession().getAttribute("reportItemValueList");
	// 报告页信息
	List<ReportItem> reportItemList = (List<ReportItem>)request.getSession().getAttribute("reportItemList");
	// 业务信息
    InspectionInfo inspectionInfo = (InspectionInfo)request.getSession().getAttribute("inspectionInfo");
 	// 业务信息
    DeviceDocument deviceDocument = (DeviceDocument)request.getSession().getAttribute("deviceDocument");
	// 报告信息
    Report report = (Report)request.getSession().getAttribute("report");
    String report_code = StringUtil.isNotEmpty(report.getReport_code())?report.getReport_code():"";
    String report_item = inspectionInfo.getReport_item();
    String xsqts = StringUtil.isNotEmpty(inspectionInfo.getXsqts())?inspectionInfo.getXsqts():"";
    String com_name = "";
	String construction_units_name = "";
    String device_area_name = "";
    String device_street_name = "";
    String device_registration_code = "";
    String inspect_date = "";
    String cqjgjpj_date = "";
    String report_sn = inspectionInfo.getReport_sn();
    String last_inspecion_date_y = "";
    String last_inspecion_date_m = "";
    
    // 构造DataRecord、MRDataSet对象
    MRDataSet mrds = new MRDataSet();
    DataRecord rec = new DataRecord();
    
    Map<String, Object> zzjdInfoMap = (Map<String, Object>) request.getSession().getAttribute("INSPECTIONZZJDINFO");
	for (String key : zzjdInfoMap.keySet()) {
		if (("MADE_UNIT_NAME").equals(key)) { // 制造单位
	if (!"".equals(zzjdInfoMap.get(key))
			&& zzjdInfoMap.get(key) != null) {
		rec.setValue(key, TS_Util.nullToString(zzjdInfoMap.get(
				key).toString()));
	}else{
		com_name = inspectionInfo.getReport_com_name(); 
		    	rec.setValue("MADE_UNIT_NAME", inspectionInfo.getReport_com_name());
	}
		} else if (("INSTALL_UNIT_NAME").equals(key)) { // 安装单位
	if (!"".equals(zzjdInfoMap.get(key))
			&& zzjdInfoMap.get(key) != null) {
		rec.setValue(key, TS_Util.nullToString(zzjdInfoMap.get(
				key).toString()));
	}else{
		com_name = inspectionInfo.getReport_com_name(); 
		    	rec.setValue("INSTALL_UNIT_NAME", inspectionInfo.getReport_com_name());
	}
		} else if (("CONSTRUCTION_UNIT_NAME").equals(key)) { // 施工单位名称
	if (!"".equals(zzjdInfoMap.get(key))
			&& zzjdInfoMap.get(key) != null) {
		rec.setValue(key, TS_Util.nullToString(zzjdInfoMap.get(
				key).toString()));
	}else{
		com_name = inspectionInfo.getReport_com_name(); 
		    	rec.setValue("CONSTRUCTION_UNIT_NAME", inspectionInfo.getReport_com_name());
	}
		} else if (("COM_NAME").equals(key)) { // 使用单位
	if (!"".equals(zzjdInfoMap.get(key))){
		rec.setValue("COM_NAME", TS_Util
				.nullToString(zzjdInfoMap.get(key)));
		com_name = TS_Util
				.nullToString(zzjdInfoMap.get(key));
	}else{
		com_name = inspectionInfo.getReport_com_name(); 
		    	rec.setValue("COM_NAME", inspectionInfo.getReport_com_name());
	}
	
		} else if (key.contains("GLSJWJ_JDQD_")) { // 锅炉设计文件鉴定清单
	if("GLSJWJ_JDQD_ZTBH".equals(key) || "GLSJWJ_JDQD_BTTBH".equals(key) ||
			"GLSJWJ_JDQD_DLGSBH".equals(key) || "GLSJWJ_JDQD_GTBH".equals(key) ||
			"GLSJWJ_JDQD_GRQBH".equals(key) || "GLSJWJ_JDQD_JWQBH".equals(key) ||
			"GLSJWJ_JDQD_SMQBH".equals(key) || "GLSJWJ_JDQD_ZRQBH".equals(key) ||
			"GLSJWJ_JDQD_RYLXTTBH".equals(key) || "GLSJWJ_JDQD_QDJSHZBBH".equals(key) ||
			"GLSJWJ_JDQD_ZJSGDBH".equals(key) || "GLSJWJ_JDQD_ZZQGDBH".equals(key) ||
			"GLSJWJ_JDQD_ZRZQLDGDBH".equals(key) || "GLSJWJ_JDQD_ZRZQRDGDBH".equals(key)){
		String jdqd = TS_Util.nullToString(zzjdInfoMap.get(key));
		if (StringUtil.isNotEmpty(jdqd)) {
			String[] jdqdList = jdqd.split(",");
			if(jdqdList.length>0){
				for(int i=0;i<jdqdList.length;i++){
					String gzzlmc_key = key+"_"+(i+1);
					rec.setValue(gzzlmc_key, TS_Util.nullToString(jdqdList[i]));
				}
			}						
		}
	}
		} else {
	rec.setValue(key, TS_Util
			.nullToString(zzjdInfoMap.get(key)));
		}
	}
	
	// 序号
    if (report.getReport_name().contains("锅炉设计文件")) {
		rec.setValue("DEVICE_NO", report_sn.split("-")[1]); // 序号（由报告书编号中的序号部分生成）
	}
    
    // 检验项目表信息处理
    for (int i = 0; i < reportItemValueList.size(); i++)
    {
    	ReportItemValue reportItemValue = reportItemValueList.get(i);
    	String item_value = "";
    	if("ZLSCQK".equals(reportItemValue.getItem_name()) || "LAST_CHECK_PROBLEMS".equals(reportItemValue.getItem_name())
    			|| reportItemValue.getItem_name().contains("REMARKS") || reportItemValue.getItem_name().contains("INSPECTION_CONCLUSION")
    			|| reportItemValue.getItem_name().contains("DEVICE_COUNT") || reportItemValue.getItem_name().contains("JLBG_WTJCLYJ")
    			|| reportItemValue.getItem_name().contains("JLBG_FXWT")){
    		item_value = TS_Util.nullToString(reportItemValue.getItem_value());
    	}else{
    		item_value = TS_Util.nullToString(reportItemValue.getItem_value()).replaceAll("\r|\n","");
    	}
    	
    	if(reportItemValue.getItem_name().equals("DRAFT_DATE")){
    		if(StringUtil.isNotEmpty(item_value)){
    			if(item_value.indexOf("/")!=-1){
    				cal.setTime(DateUtil.convertStringToDate("yyyy/MM/dd", item_value));
    			}else{
    				cal.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", item_value));
    			}
        		String newdate = DateUtil.getDateTime("yyyy年MM月dd日",cal.getTime()); 
        		rec.setValue("DRAFT_DATE",newdate.getBytes("GB2312"));
    		}
    	}else if("COM_NAME".equals(reportItemValue.getItem_name())){
			com_name = TS_Util.nullToString(item_value);
		} else if ("CONSTRUCTION_UNITS_NAME".equals(reportItemValue.getItem_name())) {
			construction_units_name = TS_Util.nullToString(item_value);
		} else if("DEVICE_AREA_NAME".equals(reportItemValue.getItem_name())){
			device_area_name = TS_Util.nullToString(item_value);
		}else if("DEVICE_STREET_NAME".equals(reportItemValue.getItem_name())){
			device_street_name = TS_Util.nullToString(item_value);
		}else if("DEVICE_REGISTRATION_CODE".equals(reportItemValue.getItem_name())){
			device_registration_code = TS_Util.nullToString(item_value);
		}else if("INSPECT_DATE".equals(reportItemValue.getItem_name())){
			inspect_date = TS_Util.nullToString(item_value);
			rec.setValue("INSPECT_DATE",inspect_date.getBytes("GB2312"));
		}else if(reportItemValue.getItem_name().equals("CQJ_GJ_PJ_DATE")){
			cqjgjpj_date = TS_Util.nullToString(item_value);
    		if(StringUtil.isNotEmpty(cqjgjpj_date)){
    			if(cqjgjpj_date.indexOf("/")!=-1){
    				cal.setTime(DateUtil.convertStringToDate("yyyy/MM/dd", cqjgjpj_date));
    			}else{
    				cal.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", cqjgjpj_date));
    			}
        		String newdate = DateUtil.getDateTime("yyyy年MM月dd日",cal.getTime()); 
        		rec.setValue("CQJ_GJ_PJ_DATE",newdate.getBytes("GB2312"));
    		}
    	}else if(reportItemValue.getItem_name().startsWith("INSPECTION_DATE") || "INSPECTION_END_DATE".equals(reportItemValue.getItem_name())
   			 || "INSPECT_NEXT_DATE".equals(reportItemValue.getItem_name()) || reportItemValue.getItem_name().contains("INSPECTION_DATE") || reportItemValue.getItem_name().contains("INSPECT_DATE")){
			if(!"LAST_INSPECTION_DATE_Y".equals(reportItemValue.getItem_name()) && !"LAST_INSPECTION_DATE_M".equals(reportItemValue.getItem_name())
					&& !"LAST_INSPECT_DATE_Y".equals(reportItemValue.getItem_name()) && !"LAST_INSPECT_DATE_M".equals(reportItemValue.getItem_name())
					 && !"LAST_INSPECTION_DATE_AUTO".equals(reportItemValue.getItem_name())
					 && !"DEVICE_INSPECTION_DATE_MONTH".equals(reportItemValue.getItem_name())
	    				&& !"DEVICE_INSPECTION_DATE_YEAR".equals(reportItemValue.getItem_name())
	    				&& !"KY_LAST_INSPECTION_DATE".equals(reportItemValue.getItem_name())
	    				&& !"DEVICE_INSPECTION_DATE".equals(reportItemValue.getItem_name())
				&& !"NEXT_INSPECT_DATE".equals(reportItemValue.getItem_name())
				&& !"LAST_INSPECT_DATE".equals(reportItemValue.getItem_name())
				&& !"INSPECTION_DATE_END1".equals(reportItemValue.getItem_name())
				&& !"INSPECTION_DATE_M".equals(reportItemValue.getItem_name())
				&& !"INSPECTION_DATE_Y".equals(reportItemValue.getItem_name())
				&& !"INSPECTION_DATE_D".equals(reportItemValue.getItem_name())
				&& !"L_INSPECTION_DATE_M".equals(reportItemValue.getItem_name())
				&& !"L_INSPECTION_DATE_Y".equals(reportItemValue.getItem_name())
				&& !"L_INSPECTION_DATE_D".equals(reportItemValue.getItem_name())){
				if (StringUtil.isNotEmpty(item_value)) {
					if(item_value.indexOf("~") != -1){
						rec.setValue(reportItemValue.getItem_name(), item_value.getBytes("GB2312"));
					}else{
						if(item_value.indexOf("~") != -1){
							rec.setValue(reportItemValue.getItem_name(), item_value.getBytes("GB2312"));
						}else{
							if (item_value.indexOf(".") != -1) {
								item_value = item_value.replaceAll("\\.", "-");
							} else if (item_value.indexOf("－") != -1) {
								item_value = item_value.replaceAll("－", "-");
							} else if (item_value.indexOf("/") != -1) {
								if (!"/".equals(item_value)) {
									cal.setTime(DateUtil.convertStringToDate("yyyy/MM/dd", item_value));
								} else {
									rec.setValue(reportItemValue.getItem_name(), item_value.getBytes("GB2312"));
								}
							} else {
								cal.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", item_value));
							}
							String newdate = DateUtil.getDateTime("yyyy年MM月dd日", cal.getTime());
							rec.setValue(reportItemValue.getItem_name(), newdate.getBytes("GB2312"));
						}
					}
				}
			}else{
				if("LAST_INSPECTION_DATE_Y".equals(reportItemValue.getItem_name())){
					last_inspecion_date_y = item_value;
				}
				if("LAST_INSPECTION_DATE_M".equals(reportItemValue.getItem_name())){
					last_inspecion_date_m = item_value;
				}
    			rec.setValue(reportItemValue.getItem_name(),item_value.getBytes("GB2312"));
    		}
    	}else{
    		rec.setValue(reportItemValue.getItem_name(), item_value.getBytes("GB2312")); 
    	}
    }

	// 人员电子签名处理
	byte[] check_op_img = (byte[])request.getAttribute("check_op_img");
	byte[] examine_op_img = (byte[])request.getAttribute("examine_op_img");
	byte[] issue_op_img = (byte[])request.getAttribute("issue_op_img");
	byte[] enter_op_img = (byte[])request.getAttribute("enter_op_img");
	
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
	
	//将下次检验日期按照yyyy年MM月dd日方式显示
	 String newdate = "";
	if(inspectionInfo.getLast_check_time()!=null){
		String last_check_time = DateUtil.getDate(inspectionInfo.getLast_check_time());
		if(StringUtil.isNotEmpty(last_check_time)){
			newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getLast_check_time()); 
			rec.setValue("LAST_INSPECTION_DATE",newdate.getBytes("GB2312"));
		}
	}
	//将下次检验日期按照yyyy年MM月方式显示
	 String newdate_y = "";
	 String newdate_m = "";
	if(inspectionInfo.getLast_check_time()!=null){
		String last_check_time = DateUtil.getDate(inspectionInfo.getLast_check_time());
		if(StringUtil.isNotEmpty(last_check_time)){
			if(StringUtil.isEmpty(last_inspecion_date_y)){
				newdate_y = DateUtil.getDateTime("yyyy", inspectionInfo.getLast_check_time()); 
				rec.setValue("LAST_INSPECTION_DATE_Y",newdate_y.getBytes("GB2312"));
			}
			if(StringUtil.isEmpty(last_inspecion_date_m)){
				newdate_m = DateUtil.getDateTime("MM", inspectionInfo.getLast_check_time());
				rec.setValue("LAST_INSPECTION_DATE_M",newdate_m.getBytes("GB2312"));
			}			
		}
	}
	//将批准日期按照yyyy年MM月dd日方式显示
	if(inspectionInfo.getIssue_Date() !=null){
		String confirm_date = DateUtil.getDate(inspectionInfo.getIssue_Date());
		if(StringUtil.isNotEmpty(confirm_date)){
			newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getIssue_Date()); 
			rec.setValue("CONFIRM_DATE",newdate.getBytes("GB2312"));
		}
	}
	//将审批日期按照yyyy年MM月dd日方式显示
	if(inspectionInfo.getExamine_Date() != null){
		String examine_date = DateUtil.getDate(inspectionInfo.getExamine_Date());
		if(StringUtil.isNotEmpty(examine_date)){
			newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getExamine_Date()); 
			rec.setValue("AUDIT_DATE",newdate.getBytes("GB2312"));	
		}
	}
	//将编制日期按照yyyy年MM月dd日方式显示
		if(inspectionInfo.getEnter_time() != null){
			String enter_date = DateUtil.getDate(inspectionInfo.getEnter_time());
			if(StringUtil.isNotEmpty(enter_date)){			
				newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getEnter_time()); 
				rec.setValue("DRAFT_DATE",newdate.getBytes("GB2312"));	
			}
		}
	
	//将检验日期按照yyyy年MM月dd日方式显示
	if(inspectionInfo.getAdvance_time() != null){
		String advance_time = DateUtil.getDate(inspectionInfo.getAdvance_time());
		if(StringUtil.isNotEmpty(advance_time)){
			newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getAdvance_time()); 
			if (StringUtil.isEmpty(inspect_date)) {
				rec.setValue("INSPECT_DATE", newdate.getBytes("GB2312"));
			} else {
				if(inspect_date.contains("-") && !"-".equals(inspect_date)){
					inspect_date = DateUtil.getDateTime("yyyy年MM月dd日",
							DateUtil.convertStringToDate("yyyy-MM-dd", inspect_date));
				}
				rec.setValue("INSPECT_DATE", inspect_date.getBytes("GB2312"));
			}
			rec.setValue("INSPECTION_DATE",newdate.getBytes("GB2312"));
			rec.setValue("INSPECTION_DATE＿1", newdate.getBytes("GB2312"));
			rec.setValue("INSPECTION_DATE＿2", newdate.getBytes("GB2312"));
			rec.setValue("INSPECTION_DATE_1", newdate.getBytes("GB2312"));
			rec.setValue("INSPECTION_DATE_2", newdate.getBytes("GB2312"));
			rec.setValue("INSPECTION_DATE_TOP",newdate.getBytes("GB2312"));	
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
	
	if (StringUtil.isEmpty(device_area_name)) {
		device_area_name = StringUtil.isNotEmpty(deviceDocument.getDevice_area_name())
				&& !"null".equals(deviceDocument.getDevice_area_name()) ? deviceDocument.getDevice_area_name()
						: "";
	}
	if (StringUtil.isEmpty(device_street_name)) {
		device_street_name = StringUtil.isNotEmpty(deviceDocument.getDevice_street_name())
				&& !"null".equals(deviceDocument.getDevice_street_name())
						? deviceDocument.getDevice_street_name() : "";
	}
	if(StringUtil.isEmpty(device_registration_code)){
		device_registration_code = deviceDocument.getDevice_registration_code();
	}
	rec.setValue("DEVICE_AREA_NAME", device_area_name.getBytes("GB2312"));
	rec.setValue("DEVICE_STREET_NAME", device_street_name.getBytes("GB2312"));
	if(!report.getReport_name().equals("锅炉设计文件鉴定报告")){
		if(StringUtil.isNotEmpty(device_registration_code)){
		rec.setValue("DEVICE_REGISTRATION_CODE", device_registration_code.getBytes("GB2312"));
		}
	}
	
	
	//处理用户上传的图片信息
	Map<String,Object> picMap 
		= (Map<String,Object>)request.getAttribute("PICTURE");
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
	
	// 处理单独检验、审核人员电子签名
	Map<String, Object> page_check_imgMap = (Map<String, Object>) request
			.getAttribute("PAGE_CHECK_IMAGES");
	for (String key : page_check_imgMap.keySet()) {
		if(key.startsWith("INSPECT_MAN_PICTURE")){
			String page_index = key.substring("INSPECT_MAN_PICTURE".length());
			String item_name = "INSPECT_MAN_PTR" + page_index;
			rec.setValue(item_name, "");	// 设置检验人员姓名不显示
			//rec.setValue("INSPECT_DATE"+page_index, DateUtil.getDateTime("yyyy-MM-dd", new Date()));	// 默认检验日期为系统当前日期
		}
		if(key.startsWith("AUDIT_MAN_PICTURE")){
			String item_name = "AUDIT_MAN_PTR" + key.substring("AUDIT_MAN_PICTURE".length());
			rec.setValue(item_name, "");	// 设置审核人员姓名不显示
		}
		if(key.startsWith("EVAL_PIC_MAN_PICTURE")){
			String page_index = key.substring("EVAL_PIC_MAN_PICTURE".length());
			String item_name = "EVAL_PIC_MAN_PTR" + page_index;
			rec.setValue(item_name, "");	// 设置评片人员姓名不显示
			//rec.setValue("EVAL_PIC_DATE"+page_index, DateUtil.getDateTime("yyyy-MM-dd", new Date()));	// 默认评片日期为系统当前日期
		}
		rec.setValue(key, page_check_imgMap.get(key));	// 设置显示人员电子签名
	}
	
	// 报告书编号
	rec.setValue("REPORT_SN", report_sn);
	
	// 检验机构核准证号
	//rec.setValue("JGHZH","TS7110306-2019");	
	rec.setValue("TotalP", StringUtil.isNotEmpty(report_item)?report_item.split(",").length:0);
	mrds.addRow(rec);

	// 创建报表引擎对象，指定报表根目录，传递结果集，绑定报表
    MREngine engine = new MREngine(pageContext);    
    //engine.setUnicodeOption(1);
    // 用MRDataSet对象为报表提供数据集：
    engine.setRootPath(StringUtil.isNotEmpty(report.getRootpath())?report.getRootpath():"");
    engine.addReport(StringUtil.isNotEmpty(report.getReport_file())?report.getReport_file():"");//报表文件
    engine.addMRDataSet(StringUtil.isNotEmpty(report.getMrdataset())?report.getMrdataset():"", mrds);
    engine.bind();
    
    
%>
<html>
<head>
<title> 报表信息 </title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />

<%@include file="/k/kui-base-list.jsp"%>
<script language="javascript">
	var com_name = "<%=com_name%>";
	var construction_units_name = "<%=construction_units_name%>";
	
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
		var ss = "<%=report_item%>".split(",");
		
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

		
		
		//alert(ss_length+"-----"+(ss_length%2)+"-----"+(ss_length%2==1))
		//如果为基数页的话，自动在页后添加空白页
		if(ss_length%2==1)
		{
			//alert();
			//report.AddPage();
		}
		
		if(!status)
		{
			alert("报告模板上没有对应的索引！");
			api.close();	// window.close();
		}
	
		parseValueReports();
		MRViewer.Preview();
		<%
			if("yes".equals(printout)){				
		%>
				doPrintreport();
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
					}else if(obj.Prop("memo").indexOf("[BGDS.\"CONSTRUCTION_UNITS_NAME\"]")!=-1){
						if(construction_units_name.indexOf("江苏")!=-1 && construction_units_name.indexOf("祥电梯有限公司")!=-1){
							obj.Prop("Memo") = "江苏燊祥电梯有限公司";
						}else{
							obj.Prop("Memo") = construction_units_name;
						}
					}
				}
			} catch(e){}
		}
	}
	
	function showBB(){
		$("#sssss").show();
	}
	
	$(window).load(function() {
		$("#MRViewer").append('<param name="wmode" value="transparent" />');
	});
</script>
</head>
<body>
<div class="scroll-tm" style="overflow: hidden" id="sssss"> 
	<ming:MRViewer id="MRViewer" shownow="true"  width="100%"   height="100%"
	 simple="false" 
	 invisiblebuttons="Print,Export,Close,PrintPopup,ExportPopup,Find,FindNext" 
	 postbackurl=""  canedit="false"   wrapparams="true"  />
</div>
</body>
</html>