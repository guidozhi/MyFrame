<%@page import="org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument.Include"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.khnt.utils.StringUtil"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@ taglib uri="/WEB-INF/Ming.tld" prefix="ming" %>
<%@ page import="com.ming.webreport.*"%>
<%@ page import="com.lsts.inspection.bean.ReportItemValue"%>
<%@ page import="java.util.*"%>
<%@ page import="util.TS_Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="add">
<title> 报告书录入 </title>
<%
	
	MRDataSet mrds = new MRDataSet();
	DataRecord rec = new DataRecord();
	
	String com_name = "";
	String device_use_place = "";
	String advance_time = "";
	String enter_time = "";
	String check_time = "";
	String check_user_name = "";
	
	//处理检验业务表基本信息
	Map<String,Object> infoMap 
		= (Map<String,Object>)request.getAttribute("INSPECTIONINFO");
	
	String ins_date = TS_Util.nullToString(infoMap.get("advance_time"));
	
	for (String key : infoMap.keySet()) {
		//System.out.println("================="+info_value);
		
		String info_value = TS_Util.nullToString(infoMap.get(key)).replaceAll("\r|\n","");
		if("REPORT_COM_NAME".equals(key)){
			com_name = TS_Util.nullToString(info_value);
			if(StringUtil.isNotEmpty(com_name)){
				rec.setValue("COM_NAME", com_name);
			}	
		} else if (("REPORT_COM_ADDRESS").equals(key)) {//报告书上使用单位名称

			rec.setValue("DEVICE_USE_PLACE", TS_Util.nullToString(
					info_value));
			device_use_place = TS_Util.nullToString(
					info_value);
		} else if(("CHECK_OP_NAME").equals(key)){///检验员
			
			rec.setValue("INSPECTION_OP_STR", 
					TS_Util.nullToString(info_value).getBytes("GB2312"));
		} else if (("CONFIRM_DATE").equals(key)) {
			// 校核日期不做任何处理
		} else{
			rec.setValue(key, 
					TS_Util.nullToString(info_value) );
		}
		
		if(StringUtil.isEmpty(check_time)){
			if("EXAMINE_DATE".equals(key)){
				check_time = TS_Util.nullToString(info_value);
			}
		}
		if("EXAMINE_NAME".equals(key)){
			check_user_name = TS_Util.nullToString(info_value);
		}
		if("ADVANCE_TIME".equals(key)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			advance_time = sdf.format(sdf.parse(TS_Util.nullToString(info_value)));
		}
		if("ENTER_TIME".equals(key)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(info_value!=null && !"".equals(info_value)){
				enter_time = sdf.format(sdf.parse(TS_Util.nullToString(info_value)));
				rec.setValue("DRAFT_DATE", enter_time);
			}
		}
		
	}
	//处理设备主表信息
	Map<String,Object> docMap 
		= (Map<String,Object>)request.getAttribute("DEVICEDOCUMENT");
	for (String key : docMap.keySet()) {
		//判断设备类型 剪切前两位用于报告封面设备类型
		//if(key.equals("DEVICE_SORT_CODE")){
		//	rec.setValue(key, 
			//		TS_Util.nullToString(device_value.toString().substring(0,2)) );
			
		//}else{
			String device_value = TS_Util.nullToString(docMap.get(key)).replaceAll("\r|\n","");
			if(key.equals("DEVICE_USE_PLACE")){
				device_use_place = TS_Util.nullToString(device_value);
				rec.setValue(key
						,TS_Util.nullToString(device_value));
			}else if(key.equals("SECURITY_OP")){
				System.out.println("安全管理人员====================:"+TS_Util.nullToString(device_value));
				rec.setValue(key
						,TS_Util.nullToString(device_value));
			}else{
				rec.setValue(key, 
						TS_Util.nullToString(device_value) );
			}
			
		//}
	}
	//处理锅炉参数表信息
	Map<String,Object> boilerMap 
		= (Map<String,Object>)request.getAttribute("BOILERPARA");
	for (String key : boilerMap.keySet()) {
		//System.out.println("================="+device_value);
		rec.setValue(key, 
				TS_Util.nullToString(boilerMap.get(key)).getBytes("GB2312") );
	}

	//处理压力容器参数表信息
	Map<String,Object> vesselMap 
		= (Map<String,Object>)request.getAttribute("VESSELPARA");
	for (String key : vesselMap.keySet()) {
		//System.out.println("================="+device_value);
		rec.setValue(key, 
				TS_Util.nullToString(vesselMap.get(key)).getBytes("GB2312") );
	}
	
	//处理电梯参数表信息
	Map<String,Object> elevatorMap 
		= (Map<String,Object>)request.getAttribute("ELEVATORPARA");
	for (String key : elevatorMap.keySet()) {
		//System.out.println("================="+device_value);
		rec.setValue(key, 
				TS_Util.nullToString(elevatorMap.get(key)).getBytes("GB2312") );
	}
	
	
	//处理起重机参数表信息
	Map<String,Object> craneMap 
		= (Map<String,Object>)request.getAttribute("CRANEPARA");
	for (String key : craneMap.keySet()) {
		//System.out.println("================="+device_value);
		rec.setValue(key, 
				TS_Util.nullToString(craneMap.get(key)).getBytes("GB2312") );
	}
		
	//处理场内机动车参数表信息
	Map<String,Object> engineMap 
		= (Map<String,Object>)request.getAttribute("ENGINEPARA");
	for (String key : engineMap.keySet()) {
		//System.out.println("================="+device_value);
		rec.setValue(key, 
				TS_Util.nullToString(engineMap.get(key)).getBytes("GB2312") );
	}

	//处理游乐设施参数表信息
	Map<String,Object> rideMap 
		= (Map<String,Object>)request.getAttribute("RIDEPARA");
	for (String key : rideMap.keySet()) {
		//System.out.println("================="+device_value);
		rec.setValue(key, 
				TS_Util.nullToString(rideMap.get(key)).getBytes("GB2312") );
	}
	
	List<ReportItemValue> reportItemValueList 
		= (List)request.getAttribute("REPORTITEMVALUE");
	for(ReportItemValue riv : reportItemValueList) {
		//System.out.println("getItem_name:"+riv.getItem_name()
		//		+"----getItem_value:"+riv.getItem_value());
		String item_value = "";
    	if("ZLSCQK".equals(riv.getItem_name()) || "LAST_CHECK_PROBLEMS".equals(riv.getItem_name())
    			|| riv.getItem_name().contains("REMARKS")){
    		item_value = TS_Util.nullToString(riv.getItem_value());
    	}else{
    		item_value = TS_Util.nullToString(riv.getItem_value()).replaceAll("\r|\n","");
    	}
		if("COM_NAME".equals(riv.getItem_name())){
			com_name = TS_Util.nullToString(item_value);
		} else if ("DEVICE_USE_PLACE".equals(riv.getItem_name())) {
			device_use_place = TS_Util.nullToString(item_value);
			rec.setValue(TS_Util.nullToString(riv.getItem_name())
					,TS_Util.nullToString(item_value));
		} else if("INSPECTION_OP_STR".equals(riv.getItem_name())){
			rec.setValue(TS_Util.nullToString(riv.getItem_name())
					,TS_Util.nullToString(item_value));
		}else if("SECURITY_OP".equals(riv.getItem_name())){
			System.out.println("安全管理人员====================:"+TS_Util.nullToString(item_value));
			rec.setValue(TS_Util.nullToString(riv.getItem_name())
					,TS_Util.nullToString(item_value));
		}else if(riv.getItem_name().equals("LAST_INSPECTION_DATE")){
    		if(StringUtil.isNotEmpty(item_value)){
    			Calendar cal = Calendar.getInstance();
    			if(item_value.indexOf("/")!=-1){
    				cal.setTime(DateUtil.convertStringToDate("yyyy/MM/dd", item_value));
    			}else{
    				cal.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", item_value));
    			}
        		String newdate = DateUtil.getDateTime("yyyy-MM-dd",cal.getTime()); 
        		rec.setValue("LAST_INSPECTION_DATE",newdate);
    		}
    	}else{
			rec.setValue(TS_Util.nullToString(riv.getItem_name())
					,TS_Util.nullToString(item_value) );
		}
		
		if(StringUtil.isEmpty(advance_time)){
			if("INSPECTION_DATE".equals(riv.getItem_name())){
				advance_time = TS_Util.nullToString(item_value);
			}
		}
		if(StringUtil.isEmpty(check_time)){
			if("AUDIT_DATE".equals(riv.getItem_name())){
				check_time = TS_Util.nullToString(item_value);
			}
		}
		if(StringUtil.isEmpty(enter_time)){
			if("DRAFT_DATE".equals(riv.getItem_name())){
				enter_time = TS_Util.nullToString(item_value);
				rec.setValue("DRAFT_DATE", enter_time);
			}
		}
		if(StringUtil.isNotEmpty(check_time)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			check_time = DateUtil.getDateTime("yyyy-MM-dd", sdf.parse(check_time)); 
			rec.setValue("AUDIT_DATE",check_time);	
		}
		if(StringUtil.isNotEmpty(enter_time)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			enter_time = DateUtil.getDateTime("yyyy-MM-dd", sdf.parse(enter_time)); 
			rec.setValue("DRAFT_DATE", enter_time);
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			enter_time = DateUtil.getDateTime("yyyy-MM-dd", new Date()); 
			rec.setValue("DRAFT_DATE", enter_time);
		}
		if(StringUtil.isNotEmpty(check_user_name)){
			rec.setValue("INSPECTION_AUDIT_STR",check_user_name);	
		}
		
		
	}
	if (StringUtil.isNotEmpty(device_use_place)) {
		rec.setValue("DEVICE_USE_PLACE", device_use_place);
	}
	rec.setValue("INSPECTION_DATE",advance_time );
	rec.setValue("INSPECTION_DATE＿1", advance_time);
	rec.setValue("INSPECTION_DATE＿2", advance_time);
	rec.setValue("INSPECTION_DATE_1", advance_time);
	rec.setValue("INSPECTION_DATE_2", advance_time);
	rec.setValue("INSPECTION_DATE_TOP",advance_time );	
	
	// 编制人
	//CurrentSessionUser user = SecurityUtil.getSecurityUser();
	//System.out.println("=================编制人"+user.getName());
	//rec.setValue("INSPECTION_ENTER_STR", user.getName());
	
	// 显示电子签名
	Map<String, Object> imgMap = (Map<String, Object>) request
					.getAttribute("IMAGES");
			
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

	Map<String,Object> paraMap 
		= (Map<String,Object>)request.getAttribute("REPORTPARA");
	
	//额外的一些报表设置参数
	rec.setValue("TotalP", paraMap.get("TotalP"));
	//rec.setValue("JGHZH", "TS7110306-2019");
	mrds.addRow(rec);
	MREngine engine = new MREngine(pageContext);
	
	engine.setRootPath((String)paraMap.get("report_root_path"));
	engine.addReport((String)paraMap.get("report_file"));
	engine.addMRDataSet((String)paraMap.get("MRDataSet"), mrds);
	engine.bind();
	
%>
<%@ include file="/k/kui-base-form.jsp"%>
<%@ include file="report.js.jsp"%>

<script language="javascript">
	var com_name = "<%=com_name%>";
	var device_use_place = "<%=device_use_place%>";
<!--
	$(function() {
		var height = $(window).height()-$('.toolbar-tm').height();
		$("#scroll-tm").css({height:height});
		setReports();
		
		$('#audit').click(function(){
			doAudit();
		});
		
		$('#save').click(function(){
			doSave();
			
			api.close();
			api.data.window.api.data.window.submitAction();
		});
		
		$('#cancel').click(function(){
			api.close();
		});
	})
	
	//报告审核页面
	function doAudit(){
	
		var type = ${param.type};
		
		var titel="";
		
		if(type=="04"){
			titel="报告审核";
			
		}else if(type=="05"){
			titel="报告签发";
			
		}

		
		$('#sssss').hide()
	
		top.$.dialog({
			width : 600, 
			height : 500, 
			lock : true, 
			title:titel,
			parent:api,
			content: 'url:app/flow/report/item_take_revise.jsp?'
					+'acc_id=${param.acc_id}'
					+'&flow_num=${param.flow_num}'
					+'&type=${param.type}'
					+'&device_type=${param.device_type}'
					+'&device_sort_code=${param.device_sort_code}'
					+'&check_flow=${param.check_flow}'
					+'&id=${param.ins_info_id}'
					+'&org_id=${param.org_id}'
					+'&advance_time=<%=advance_time%>'
					+'&check_time=<%=check_time%>'
					+'&enter_time=<%=enter_time%>',
			data : {"window" : window}
		});
		
		
	}
	
	function showBB(){
		$("#sssss").show();
	}
	
	//保存开始禁用按钮
	function disableButton(){
		//$.ligerDialog.alert(111);
	}
	//保存结束按钮可用
	function enableButton(){
		//$.ligerDialog.alert(222);
	}
	$(window).load(function() {
		$("#MRViewer").append('<param name="wmode" value="transparent" />');
	});
//-->
</script>
</head>
<body>
<div class="scroll-tm" style="overflow: hidden" id="sssss"> 
	<ming:MRViewer id="MRViewer" shownow="true"  width="100%"   height="100%"
	 simple="false" 
	 invisiblebuttons="Print,Export,Close,PrintPopup,ExportPopup,Find,FindNext" 
	 postbackurl=""  canedit="false"   wrapparams="true"  />
	 <script>
	

	 </script>
</div>
<div class="toolbar-tm">
	<div class="toolbar-tm-bottom" style="text-align:center;">
	
		<a id="audit" class="l-button-warp l-button" ligeruiid="Button1012">
			<span class="l-button-main l-button-hasicon">
			<c:choose>
				<c:when test='${param.type=="04"}'>
					<span class="l-button-text">审核结论</span>
				</c:when>
				<c:when test='${param.type=="05"}'>
					<span class="l-button-text">签发结论</span>
				</c:when>
				<c:when test='${param.type=="06"}'>
					<span class="l-button-text">审查结论</span>
				</c:when>
			</c:choose>
				<span class="l-button-icon l-icon-search"></span>
			</span>
		</a>
		
		<a id="cancel" class="l-button-warp l-button" ligeruiid="Button1012">
			<span class="l-button-main l-button-hasicon">
				<span class="l-button-text">关闭</span>
				<span class="l-button-icon l-icon-cancel"></span>
			</span>
		</a>
	</div>
</div>
</body>
</html>