<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.lsts.inspection.bean.ReportItemValue"%>
<%@ page import="com.lsts.report.bean.ReportItem"%>
<%@page import="java.io.IOException"%>
<%@page import="util.TS_Util"%>
<%@ taglib uri="/WEB-INF/Ming.tld" prefix="ming" %>
<%@ page import="com.ming.webreport.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.khnt.utils.StringUtil" %>
<%@ page import="com.khnt.utils.DateUtil"%>
<%@ page import="com.lsts.inspection.bean.*" %>
<%@ page import="com.lsts.report.bean.*" %>
<%@ page import="oracle.sql.*" %>
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
<%
	Calendar cal = Calendar.getInstance();
	List<ReportItemValue> reportItemValueList = (List<ReportItemValue>)request.getSession().getAttribute("reportItemValueList");
    InspectionInfo inspectionInfo = (InspectionInfo)request.getSession().getAttribute("inspectionInfo");
    Report report = (Report)request.getSession().getAttribute("report");
    
    String report_item = "";
 	// 处理报告分页单独审核信息
	List<String> pageCheckList = (ArrayList<String>) request
			.getAttribute("pageIndexList");
	for (int i=0;i<pageCheckList.size();i++) {
		if(report_item.length()>0){
			report_item += ",";
		}
		report_item += pageCheckList.get(i);
	}
    
	String advance_time = "";
	String enter_time = "";
	String check_time = "";
	if(inspectionInfo!=null){
		advance_time = inspectionInfo.getAdvance_time()!=null?DateUtil.getDateTime("yyyy-MM-dd",inspectionInfo.getAdvance_time()):"";
		enter_time = inspectionInfo.getEnter_time()!=null?DateUtil.getDateTime("yyyy-MM-dd",inspectionInfo.getEnter_time()):"";
		check_time = inspectionInfo.getExamine_Date()!=null?DateUtil.getDateTime("yyyy-MM-dd",inspectionInfo.getExamine_Date()):"";
	}
    
    String com_name = "";

    // 构造DataRecord、MRDataSet对象
    MRDataSet mrds = new MRDataSet();
    DataRecord rec = new DataRecord();
    for (int i = 0; i < reportItemValueList.size(); i++)
    {
    	ReportItemValue reportItemValue = reportItemValueList.get(i);
    	String item_value = TS_Util.nullToString(reportItemValue.getItem_value()).replaceAll("\r|\n","");
    	if(reportItemValue.getItem_name().equals("DRAFT_DATE")){
    		if(StringUtil.isNotEmpty(reportItemValue.getItem_value())){
    			if(reportItemValue.getItem_value().indexOf("/")!=-1){
    				cal.setTime(DateUtil.convertStringToDate("yyyy/MM/dd", reportItemValue.getItem_value()));
    			}else{
    				cal.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", reportItemValue.getItem_value()));
    			}
        		String newdate = DateUtil.getDateTime("yyyy年MM月dd日",cal.getTime()); 
        		rec.setValue("DRAFT_DATE",newdate);
    		}
    	}else if("COM_NAME".equals(reportItemValue.getItem_name())){
			com_name = TS_Util.nullToString(item_value);
		}else{
    		rec.setValue(reportItemValue.getItem_name(), item_value); 
    	}
    }
    rec.setValue("INSPECTION_AUDIT_STR", 
    		inspectionInfo.getExamine_name());
	rec.setValue("TotalP", StringUtil.isNotEmpty(inspectionInfo.getReport_item())?inspectionInfo.getReport_item().split(",").length:0);
	
	//将下次检验日期按照yyyy-MM-dd 方式显示
	 String newdate = "";
	if(inspectionInfo.getLast_check_time()!=null){
		String last_check_time = DateUtil.getDate(inspectionInfo.getLast_check_time());
		if(StringUtil.isNotEmpty(last_check_time)){
			newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getLast_check_time()); 
		rec.setValue("LAST_INSPECTION_DATE",newdate);
		}
		
	}
	//将签发日期按照yyyy年MM月dd日方式显示
	if(inspectionInfo.getIssue_Date() !=null){
		String confirm_date = DateUtil.getDate(inspectionInfo.getIssue_Date());
		if(StringUtil.isNotEmpty(confirm_date)){
			newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getIssue_Date()); 
			rec.setValue("CONFIRM_DATE",newdate);
		}
	}
	//将审核日期按照yyyy年MM月dd日方式显示
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
		String advance_time1 = DateUtil.getDate(inspectionInfo.getAdvance_time());
		if(StringUtil.isNotEmpty(advance_time1)){
			newdate = DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getAdvance_time()); 
			rec.setValue("INSPECTION_DATE",newdate);
			rec.setValue("INSPECTION_DATE＿1", newdate);
			rec.setValue("INSPECTION_DATE＿2", newdate);
			rec.setValue("INSPECTION_DATE_1", newdate);
			rec.setValue("INSPECTION_DATE_2", newdate);
			rec.setValue("INSPECTION_DATE_TOP",newdate );	
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
	
	// 显示电子签名
	Map<String, Object> imgMap = (Map<String, Object>) request.getAttribute("IMAGES");
	
	byte[] check_op_img = (byte[])imgMap.get("check_op_img");
	byte[] examine_op_img = (byte[])imgMap.get("examine_op_img");
	byte[] issue_op_img = (byte[])imgMap.get("issue_op_img");
	byte[] enter_op_img = (byte[])imgMap.get("enter_op_img");
	
	//设置打印签名不可见，电子签名可见
	rec.setValue("INSPECTION_OP_STR", "");
	rec.setValue("INSPECTION_AUDIT_STR","");
	rec.setValue("INSPECTION_CONFIRM_STR","");
	rec.setValue("INSPECTION_ENTER_STR","");
	
	//检验员电子签名
	rec.setValue("INSPECTION_OP_PICTURE", check_op_img!=null?check_op_img:"");			
 	//审核电子签名
	rec.setValue("INSPECTION_AUDIT_PICTURE", examine_op_img!=null?examine_op_img:"");	
	//签发(批准)电子签名
	rec.setValue("INSPECTION_CONFIRM_PICTURE", issue_op_img!=null?issue_op_img:"");	                                                                             	                                                             
	//编制电子签名
	rec.setValue("INSPECTION_ENTER_PICTURE", enter_op_img!=null?enter_op_img:"");
	
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

	//rec.setValue("P_JGHZH","TS7110306-2019");
	
	mrds.addRow(rec);

    // 创建报表引擎对象，指定报表根目录，传递结果集，绑定报表
    MREngine engine = new MREngine(pageContext, StringUtil.isNotEmpty(report.getRootpath())?report.getRootpath():"");    
    engine.setUnicodeOption(1);
    // 用MRDataSet对象为报表提供数据集：
    engine.addMRDataSet(StringUtil.isNotEmpty(report.getMrdataset())?report.getMrdataset():"", mrds);
    engine.addReport(StringUtil.isNotEmpty(report.getReport_file())?report.getReport_file():"");//报表文件
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
		//setReports();
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
	}
	
	function parseValueReports()
	{
		//把每一个是Memo的对象中间包含“”的并且不是总页数的控件对象都设置为可以提交的状态,提交KEY = memo中的字段名
		report = MRViewer.Report;
		pagecount = MRViewer.PageCount;
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
	
	function save(){
		var type = '${param.type}';
		var isOp = parent.left._opid${param.opid}.innerHTML;
		if(type == '04'){
			if(isOp!=""){
				alert("亲，这份报告已审核，不需要再审核了哦！");
				return;
			}
		}else{
			if(isOp!=""){
				alert("亲，这份报告已签发，不需要再签发了哦！");
				return;
			}
		}
		
		var data = new Object();
		data.acc_id = '${param.activity_id}';
		data.flow_num = '${param.flow_note_id}';
		data.type = type;
		data.device_type = '${param.device_type}';
		data.ids = '${param.id}';
		data.opid = '${param.opid}';
		data.advance_time = '<%=advance_time%>';
		data.check_time = '<%=check_time%>';
		data.enter_time = '<%=enter_time%>';
		data.op_type = '单份'; 	// 1：操作方式
		data.isBatch = '0';		// 0:单份操作
		parent.save(data);
	}
	
	function showBB(){
		$("#sssss").show();
	}
	
	$(window).load(function() {
		$("#MRViewer").append('<param name="wmode" value="transparent" />');
	});
</script>
</head>
<body style="margin: 0" onload="setReports()">
<form name="formObj" method="post" action="">
</form>
<div class="scroll-tm" style="overflow: hidden" id="sssss"> 
            <ming:MRViewer id="MRViewer" shownow="true"  width="100%"   height="100%" simple="false" invisiblebuttons="Export,Close,PrintPopup,ExportPopup,Find,FindNext"  postbackurl="" canedit="false" wrapparams="true"  />
</div>
</body>
</html>