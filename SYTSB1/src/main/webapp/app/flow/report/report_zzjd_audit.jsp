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
	String advance_time = "";
	String last_inspection_date = "";
	String enter_time = "";
	String check_time = "";
	String check_user_name = "";	// 审核人
	String check_op_name = "";		// 参检人员
	String report_sn = "";		// 报告书编号
	
	// 1、加载业务信息表数据
	Map<String,Object> infoMap = (Map<String,Object>)request.getAttribute("INSPECTIONINFO");	
	for (String key : infoMap.keySet()) {
		String info_value = TS_Util.nullToString(infoMap.get(key))
				.replaceAll("\r|\n", "");
		if ("REPORT_COM_NAME".equals(key)) {
			com_name = TS_Util.nullToString(info_value);
			if (StringUtil.isNotEmpty(com_name)) {
				rec.setValue("MADE_UNIT_NAME", com_name);
			}
		} else if (("CHECK_OP_NAME").equals(key)) { //参检人员	
			check_op_name = TS_Util.nullToString(info_value);
			if (check_op_name.indexOf(",") != -1) {
				check_op_name = check_op_name.replaceAll(",", " ");
			}
			rec.setValue("INSPECTION_OP_STR", check_op_name);
		} else if ("EXAMINE_NAME".equals(key)) {
			check_user_name = TS_Util.nullToString(info_value);
		} else if ("ADVANCE_TIME".equals(key)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			advance_time = sdf.format(sdf.parse(TS_Util
					.nullToString(infoMap.get(key).toString()
							.split(" ")[0])));
		} else if ("LAST_CHECK_TIME".equals(key)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			last_inspection_date = sdf.format(sdf.parse(TS_Util
					.nullToString(infoMap.get(key).toString()
							.split(" ")[0])));
		} else if ("ENTER_TIME".equals(key)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (infoMap.get(key) != null
					&& !"".equals(infoMap.get(key))) {
				enter_time = sdf.format(sdf.parse(TS_Util
						.nullToString(info_value)));
			}
		} else if (("REPORT_SN").equals(key)) { // 报告书编号
			report_sn = TS_Util.nullToString(infoMap.get(key));
			rec.setValue(key, report_sn);
		} else {
			rec.setValue(key, TS_Util.nullToString(info_value));
		}
		if (StringUtil.isEmpty(check_time)) {
			if ("EXAMINE_DATE".equals(key)) {
				check_time = TS_Util.nullToString(info_value);
			}
		}
	}

	if (StringUtil.isNotEmpty(check_time)) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		check_time = DateUtil.getDateTime("yyyy-MM-dd",
				sdf.parse(check_time));
		rec.setValue("AUDIT_DATE", check_time);
	}
	if (StringUtil.isNotEmpty(enter_time)) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		enter_time = DateUtil.getDateTime("yyyy-MM-dd",
				sdf.parse(enter_time));
	} else {
		enter_time = DateUtil.getDateTime("yyyy-MM-dd", new Date());
	}
	if (StringUtil.isNotEmpty(check_user_name)) {
		rec.setValue("INSPECTION_AUDIT_STR", check_user_name);
	}
	if (StringUtil.isNotEmpty(advance_time)) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		advance_time = DateUtil.getDateTime("yyyy-MM-dd",
				sdf.parse(advance_time));
	}
	
	if (StringUtil.isNotEmpty(last_inspection_date)) {
		if(!"/".equals(last_inspection_date)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			last_inspection_date = DateUtil.getDateTime("yyyy-MM-dd",
					sdf.parse(last_inspection_date));
		}
	}

	// 2、加载制造监督检验明细数据
	Map<String, Object> zzjdInfoMap = (Map<String, Object>) request
			.getAttribute("INSPECTIONZZJDINFO");
	for (String key : zzjdInfoMap.keySet()) {
		String zzjdInfo_value = TS_Util.nullToString(
				zzjdInfoMap.get(key)).replaceAll("\r|\n", "");
		if (("DESIGN_DATE").equals(key)) { // 设计日期
			if (!"".equals(zzjdInfoMap.get(key))
					&& zzjdInfoMap.get(key) != null) {
				rec.setValue(key, TS_Util.nullToString(zzjdInfo_value));
			}
		} else if (("MADE_DATE").equals(key)) { // 制造日期
			if (!"".equals(zzjdInfoMap.get(key))
					&& zzjdInfoMap.get(key) != null) {
				rec.setValue(key, TS_Util.nullToString(zzjdInfo_value));
			}
		} else if (("INSPECTION_DATE").equals(key)) { // 检验日期
			if (!"".equals(zzjdInfoMap.get(key))
					&& zzjdInfoMap.get(key) != null) {
				if (StringUtil.isEmpty(advance_time)) {
					advance_time = TS_Util.nullToString(zzjdInfo_value
							.split(" ")[0]);
				}
			}
		}  else if (("LAST_INSPECTION_DATE").equals(key)) { // 下次检验日期
			if (!"".equals(zzjdInfoMap.get(key))
					&& zzjdInfoMap.get(key) != null) {
				if (StringUtil.isEmpty(last_inspection_date)) {
					last_inspection_date = TS_Util.nullToString(zzjdInfo_value
							.split(" ")[0]);
				}
			}
		} else if (key.contains("GLSJWJ_JDQD_")) { // 锅炉设计文件鉴定清单
			if ("GLSJWJ_JDQD_ZTBH".equals(key)
					|| "GLSJWJ_JDQD_BTTBH".equals(key)
					|| "GLSJWJ_JDQD_DLGSBH".equals(key)
					|| "GLSJWJ_JDQD_GTBH".equals(key)
					|| "GLSJWJ_JDQD_GRQBH".equals(key)
					|| "GLSJWJ_JDQD_JWQBH".equals(key)
					|| "GLSJWJ_JDQD_SMQBH".equals(key)
					|| "GLSJWJ_JDQD_ZRQBH".equals(key)
					|| "GLSJWJ_JDQD_RYLXTTBH".equals(key)
					|| "GLSJWJ_JDQD_QDJSHZBBH".equals(key)
					|| "GLSJWJ_JDQD_ZJSGDBH".equals(key) 
					|| "GLSJWJ_JDQD_ZZQGDBH".equals(key) 
					|| "GLSJWJ_JDQD_ZRZQLDGDBH".equals(key) 
					|| "GLSJWJ_JDQD_ZRZQRDGDBH".equals(key)) {
				String jdqd = TS_Util
						.nullToString(zzjdInfoMap.get(key));
				if (StringUtil.isNotEmpty(jdqd)) {
					String[] jdqdList = jdqd.split(",");
					if (jdqdList.length > 0) {
						for (int i = 0; i < jdqdList.length; i++) {
							String gzzlmc_key = key + "_" + (i + 1);
							rec.setValue(gzzlmc_key,
									TS_Util.nullToString(jdqdList[i]));
						}
					}
				}
			}
		} else if (("REPORT_SN").equals(key)) { // 报告书编号
			if(StringUtil.isEmpty(report_sn)){
				report_sn = TS_Util.nullToString(zzjdInfoMap.get(key));
				rec.setValue(key, report_sn);
			}
		} else {
			rec.setValue(key, TS_Util.nullToString(zzjdInfo_value));
		}
	}

	// 3、加载报检检验项目明细表
	List<ReportItemValue> reportItemValueList = (List) request
			.getAttribute("REPORTITEMVALUE");
	for (ReportItemValue riv : reportItemValueList) {
		String item_value = TS_Util.nullToString(riv.getItem_value())
				.replaceAll("\r|\n", "");
		if (("REPORT_SN").equals(riv.getItem_name())) { // 报告书编号
			if (StringUtil.isEmpty(report_sn)) {
				report_sn = TS_Util.nullToString(zzjdInfoMap.get(riv.getItem_name()));
				rec.setValue(TS_Util.nullToString(riv.getItem_name()), report_sn);
			}
		} else {
			if(("INSPECTION_DATE").equals(riv.getItem_name())){
				if(StringUtil.isNotEmpty(advance_time)){
					rec.setValue(TS_Util.nullToString(riv.getItem_name()), advance_time);
				}
			}else if(("AUDIT_DATE").equals(riv.getItem_name())){
				if(StringUtil.isNotEmpty(check_time)){
					rec.setValue(TS_Util.nullToString(riv.getItem_name()), check_time);
				}
			}else{
				rec.setValue(TS_Util.nullToString(riv.getItem_name()), TS_Util.nullToString(riv.getItem_value()));
			}
		}
	}

	// 4、加载报告基本信息表数据
	Map<String, Object> reportMap = (Map<String, Object>) request
			.getAttribute("REPORT");
	for (String key : reportMap.keySet()) {
		if (("REPORT_NAME").equals(key)) { // 报告名称
			String report_name = TS_Util.nullToString(reportMap
					.get(key));
			if (report_name.contains("锅炉设计文件")) {
				rec.setValue("DEVICE_NO", report_sn.split("-")[1]); // 序号（由报告书编号中的序号部分生成）
			}
		}
	}

	rec.setValue("INSPECTION_DATE", advance_time);
	rec.setValue("LAST_INSPECTION_DATE", last_inspection_date);

	// 显示电子签名
	Map<String, Object> imgMap = (Map<String, Object>) request
			.getAttribute("IMAGES");

	byte[] check_op_img = (byte[]) imgMap.get("check_op_img");
	byte[] examine_op_img = (byte[]) imgMap.get("examine_op_img");
	byte[] issue_op_img = (byte[]) imgMap.get("issue_op_img");
	byte[] enter_op_img = (byte[]) imgMap.get("enter_op_img");

	//设置打印签名不可见，电子签名可见
	rec.setValue("INSPECTION_OP_STR", "");
	rec.setValue("INSPECTION_AUDIT_STR", "");
	rec.setValue("INSPECTION_CONFIRM_STR", "");
	rec.setValue("INSPECTION_ENTER_STR", "");

	//检验员电子签名
	rec.setValue("INSPECTION_OP_PICTURE",
			check_op_img != null ? check_op_img : "");
	//审核电子签名
	rec.setValue("INSPECTION_AUDIT_PICTURE",
			examine_op_img != null ? examine_op_img : "");
	//签发(批准)电子签名
	rec.setValue("INSPECTION_CONFIRM_PICTURE",
			issue_op_img != null ? issue_op_img : "");
	//编制电子签名
	rec.setValue("INSPECTION_ENTER_PICTURE",
			enter_op_img != null ? enter_op_img : "");

	Map<String, Object> paraMap = (Map<String, Object>) request
			.getAttribute("REPORTPARA");

	//额外的一些报表设置参数
	rec.setValue("TotalP", paraMap.get("TotalP"));
	//rec.setValue("JGHZH", "TS7110306-2019");
	mrds.addRow(rec);
	MREngine engine = new MREngine(pageContext);

	engine.setRootPath((String) paraMap.get("report_root_path"));
	engine.addReport((String) paraMap.get("report_file"));
	engine.addMRDataSet((String) paraMap.get("MRDataSet"), mrds);
	engine.bind();
%>
<%@ include file="/k/kui-base-form.jsp"%>
<%@ include file="report_zzjd.js.jsp"%>

<script language="javascript">
	var com_name = "<%=com_name%>";
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
			api.data.window.api.data.window.refreshGrid();
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
			content: 'url:app/flow/report/report_zzjd_check.jsp?acc_id=${param.acc_id}&flow_num=${param.flow_num}&check_flow=${param.check_flow}&type=${param.type}&report_type=${param.report_type}&device_type=${param.device_type}&id=${param.ins_info_id}&advance_time=<%=advance_time%>&check_time=<%=check_time%>&enter_time=<%=enter_time%>',
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
</script>
</head>
<body>
<div class="scroll-tm" style="overflow: hidden" id="sssss"> 
	<ming:MRViewer id="MRViewer" shownow="true"  width="100%"   height="100%"
	 simple="false" 
	 invisiblebuttons="Export,Close,PrintPopup,ExportPopup,Find,FindNext" 
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
					<span class="l-button-text">报告审核</span>
				</c:when>
				<c:when test='${param.type=="05"}'>
					<span class="l-button-text">报告签发</span>
				</c:when>
				<c:when test='${param.type=="06"}'>
					<span class="l-button-text">报告审查</span>
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