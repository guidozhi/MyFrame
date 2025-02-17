package com.lsts.inspection.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.khnt.base.Factory;
import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.rbac.bean.Org;
import com.khnt.rbac.impl.bean.Employee;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rbac.impl.manager.OrgManagerImpl;
import com.khnt.rbac.web.extend.IUserLoginExtendAction;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.BeanUtils;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.advicenote.service.MessageService;
import com.lsts.approve.bean.CertificateBy;
import com.lsts.approve.bean.CertificateRule;
import com.lsts.approve.service.CertificateByService;
import com.lsts.approve.service.CertificateRuleService;
import com.lsts.archives.service.ArchivesBoxManager;
import com.lsts.common.dao.CodeTablesDao;
import com.lsts.common.service.InspectionCommonService;
import com.lsts.constant.Constant;
import com.lsts.device.bean.DeviceDTO;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.service.DeviceService;
import com.lsts.employee.service.EmployeesService;
import com.lsts.inspection.bean.Drawlog;
import com.lsts.inspection.bean.Inspection;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.bean.InspectionZZJDInfo;
import com.lsts.inspection.bean.ReportDrawSign;
import com.lsts.inspection.bean.ReportItemValue;
import com.lsts.inspection.bean.ReportPageCheckInfo;
import com.lsts.inspection.bean.SysAutoIssueLog;
import com.lsts.inspection.dao.DrawlogDao;
import com.lsts.inspection.service.InspectionInfoService;
import com.lsts.inspection.service.InspectionService;
import com.lsts.inspection.service.InspectionZZJDInfoService;
import com.lsts.inspection.service.ReportDrawSignService;
import com.lsts.inspection.service.ReportItemValueService;
import com.lsts.inspection.service.ReportPageCheckInfoService;
import com.lsts.inspection.service.SysAutoIssueLogService;
import com.lsts.log.service.SysLogService;
import com.lsts.org.bean.EnterInfo;
import com.lsts.org.service.EnterService;
import com.lsts.report.bean.Report;
import com.lsts.report.bean.ReportDraw;
import com.lsts.report.bean.ReportItem;
import com.lsts.report.service.ReportDrawService;
import com.lsts.report.service.ReportItemService;
import com.lsts.report.service.ReportService;
import com.lsts.report.web.ReportDrawAction;
import com.ming.webreport.MREngine;
import com.scts.payment.bean.InspectionInfoDTO;
import com.scts.payment.service.InspectionPayInfoService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.TS_Util;

/**
 * 科室报检 web controller
 * 
 * @author 肖慈边 2014-2-13
 */

@Controller
@RequestMapping("department/basic")
@JsonIgnoreProperties(ignoreUnknown = true)
public class InspectionAction extends
		SpringSupportAction<Inspection, InspectionService> implements
		IUserLoginExtendAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	// 流程步骤编号
	public static final String FLOW_INSPECTION = "01";// 任务分配
	public static final String FLOW_INPUT = "02";// 报告录入
	public static final String FLOW_AUDIT = "03";// 报告审批
	public static final String FLOW_WRITE = "04";// 报告签发
	// public static final String FLOW_XMFZRSH = "03";// 项目负责人校验
	// public static final String FLOW_AUDIT = "04";// 报告审批
	// public static final String FLOW_CHECK = "05";// 原始资料审查
	public static final String FLOW_PRINT = "06";// 打印报告
	public static final String FLOW_DRAW = "07";// 领取报告
	public static final String FLOW_FILE = "08";// 报告归档

	@Autowired
	private InspectionService inspectionService;
	@Autowired
	private InspectionInfoService inspectionInfoService;

	@Autowired
	private ReportService reportService;
	@Autowired
	private ReportDrawService reportDrawService;
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private CodeTablesDao codeTablesDao;
	@Autowired
	private OrgManagerImpl orgManager;
	@Autowired
	private	ReportPageCheckInfoService reportPageCheckInfoService;
	@Autowired
	private ReportItemValueService reportItemValueService;
	@Autowired
	private SysLogService logService;
	@Autowired
	private ReportItemService reportItemService;
	@Autowired
	private EmployeesService employeesService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private ArchivesBoxManager archivesBoxManager;
	@Autowired
	private EnterService enterService;
	@Autowired
	private InspectionCommonService inspectionCommonService;
	@Autowired
	private SysAutoIssueLogService sysAutoIssueLogService;
	@Autowired
	private InspectionZZJDInfoService inspectionZZJDInfoService;
	@Autowired
	private CertificateRuleService certificateRuleService;
	@Autowired
	private CertificateByService certificateByService;
	@Autowired
	private ReportDrawAction reportDrawAction;
	@Autowired
	DrawlogDao drawlogDao;

	// 保存科室报检信息
	@RequestMapping(value = "flow_saveBasic")
	@ResponseBody
	public HashMap<String, Object> flow_saveBasic(@RequestBody
	Inspection inspection, HttpServletRequest request) throws Exception {

		if (StringUtil.isNotEmpty(request.getParameter("flowId"))) {
			String acc_no = request.getParameter("acc_no");
			String flowId = request.getParameter("flowId");

			Inspection baseinfo = inspectionService.flow_saveBasic(inspection,
					acc_no);

			HashMap<String, String> map = new HashMap<String, String>();

			for (InspectionInfo info : baseinfo.getInspectionInfo()) {

				String infoId = info.getId();

				map.put("infoId", infoId);
				map.put("flowId", flowId);

				// 获取下一步的步骤
				// String num = getFlowNum(flow_num,"pass");
				map.put("status", FLOW_INPUT);

				inspectionService.StarFlowProcess(map, request);

			}

			return JsonWrapper.successWrapper(baseinfo);
		}

		else {
			Inspection baseinfo = inspectionService.flow_saveBasic(inspection,
					"");
			System.err.print("没有选择流程！");
			return JsonWrapper.successWrapper(baseinfo);
			// return JsonWrapper.failureWrapper("没有选择流程！");
		}

	}

	// 保存科室报检信息
	@RequestMapping(value = "saveBasic2")
	@ResponseBody
	public HashMap<String, Object> saveBasic2(@RequestBody
	Inspection inspection, HttpServletRequest request) throws Exception {
		String chectType = request.getParameter("check_type");
		inspection.setCheck_type(chectType);
		HashMap<String, Object> resultMap = inspectionService.saveBasic(inspection, request);
		if (Boolean.valueOf(String.valueOf(resultMap
				.get("success")))) {
			return JsonWrapper.successWrapper(String.valueOf(resultMap
					.get("msg")));
		} else {
			return JsonWrapper.failureWrapperMsg(String.valueOf(resultMap
					.get("msg")));
		}
	}

	// 保存分配信息
	@RequestMapping(value = "saveServicePlan")
	@ResponseBody
	public HashMap<String, Object> saveServicePlan(@RequestBody
	Inspection inspection, HttpServletRequest request) throws Exception {
		CurrentSessionUser curUser = this.getCurrentUser(); // 获取当前用户登录信息

		// 流程编号ID
		String acc_id = request.getParameter("acc_id");
		// 业务ID
		String ins_info_id = request.getParameter("ywid");
		// 步骤号
		String flow_num = request.getParameter("flow_num");
		String op_name = curUser.getName();
		// op_name = new String(op_name.getBytes("ISO8859-1"), "UTF-8");
		String check_name = request.getParameter("check_name");
		// check_name = new String(check_name.getBytes("ISO8859-1"), "UTF-8");
		String[] fnum = flow_num.split(",");

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (ins_info_id.indexOf(",") != -1) {
			String ids[] = ins_info_id.split(",");
			String acc_ids[] = acc_id.split(",");
			for (int i = 0; i < ids.length; i++) {
				ins_info_id = ids[i];
				acc_id = acc_ids[i];
				map.put("ins_info_id", ins_info_id);
				map.put("acc_id", acc_id);
				map.put("flow_num", fnum[0]);
				map.put("status", FLOW_INPUT);
				map.put("flag", "0");
				inspectionService.subFlowProcess(map, request);
				inspectionService.flow_updateBasic(inspection, ins_info_id,
						curUser.getId(), op_name, check_name);
			}

		} else {
			map.put("ins_info_id", ins_info_id);
			map.put("acc_id", acc_id);
			map.put("flow_num", flow_num);
			map.put("status", FLOW_INPUT);
			map.put("flag", "0");
			// inspectionService.StarFlowProcess(map, request);
			inspectionService.subFlowProcess(map, request);
			inspectionService.flow_updateBasic(inspection, ins_info_id, curUser
					.getId(), op_name, check_name);
		}

		// HashMap<String, Object> wrapper = new HashMap<String, Object>();
		HashMap<String, Object> wrapper = JsonWrapper.successWrapper();
		// wrapper.put("success", true);

		return wrapper;

	}

	// 保存安全阀报检信息
	@RequestMapping(value = "flow_saveAccessory")
	@ResponseBody
	public HashMap<String, Object> flow_saveAccessory(@RequestBody
	Inspection inspection, HttpServletRequest request) throws Exception {

		if (StringUtil.isNotEmpty(request.getParameter("flowId"))) {
			String acc_no = request.getParameter("acc_no");
			String flowId = request.getParameter("flowId");

			Inspection baseinfo = inspectionService.flow_saveAccessory(
					inspection, acc_no);

			HashMap<String, String> map = new HashMap<String, String>();

			for (InspectionInfo info : baseinfo.getInspectionInfo()) {

				String infoId = info.getId();

				map.put("infoId", infoId);
				map.put("flowId", flowId);

				// 获取下一步的步骤
				// String num = getFlowNum(flow_num,"pass");
				map.put("status", FLOW_INPUT);

				inspectionService.StarFlowProcess(map, request);

			}

			return JsonWrapper.successWrapper(baseinfo);
		}

		else {
			System.err.print("没有选择发文流程！");
			return JsonWrapper.failureWrapper("没有选择发文流程！");
		}

	}

	// 保存报告审核、签发、审查信息
	@RequestMapping(value = "flow_saveCheck")
	@ResponseBody
	public Map<String, Object> flow_saveCheck(String dataStr,
			HttpServletRequest request) throws Exception {
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User user = (User) curUser.getSysUser();
		
		JSONObject dataMap = JSONObject.fromString(dataStr);

		// 提交流程所需参数集合
		Map<String, Object> map = new HashMap<String, Object>();

		String type = request.getParameter("type");
		String op_name = "04".equals(type)?"审核日期":"签发日期";
		
		String device_sort_code = request.getParameter("device_sort_code");
		String pre_device_type = "";
		if(StringUtil.isNotEmpty(device_sort_code)){
			if("7310".equals(device_sort_code)){
				pre_device_type = "F";
			}else{
				// 获取设备类别前缀（大类）
				pre_device_type = device_sort_code.substring(0, 1); 
			}
		}

		// 报告审批流程（0：二级审核 1：一级审核）
		String check_flow = request.getParameter("check_flow");

		// 流程编号ID
		String acc_id = request.getParameter("acc_id");
		// 业务ID
		String ins_info_id = request.getParameter("infoId");
		// 步骤号
		String flow_num = request.getParameter("flow_num");

		String revise_conclusion = dataMap.get("revise_conclusion").toString();

		String op_time = dataMap.get("op_time").toString();
		Date cur_op_date = null;
		if (StringUtil.isNotEmpty(op_time)) {
			cur_op_date = DateUtil.convertStringToDate("yyyy-MM-dd", op_time);
			Date cur_date = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.getCurrentDateTime()) ;
			if(cur_op_date.after(cur_date)){
				return JsonWrapper.failureWrapperMsg("日期逻辑错误，"+op_name+"不能晚于当前日期，请重新选择！");
			}
		} else {
			return JsonWrapper.failureWrapperMsg(op_name+"不能为空，请选择！");
		}

		String revise_remark = dataMap.get("revise_remark").toString();
		if("请在此处填写报告退回原因！".equals(revise_remark)){
			revise_remark = "无";
		}

		// 业务信息
		InspectionInfo inspectionInfo = inspectionInfoService.get(ins_info_id);
		// 报告模板配置信息
		Report report = reportService.get(inspectionInfo.getReport_type());
		if(StringUtil.isEmpty(pre_device_type)){
			if(StringUtil.isNotEmpty(inspectionInfo.getFk_tsjc_device_document_id())){
				if ("11111111111111111111111111111111".equals(inspectionInfo.getFk_tsjc_device_document_id())) {
					// 获取监检业务信息
					InspectionZZJDInfo inspectionZZJDInfo = inspectionZZJDInfoService.getByInfoId(ins_info_id);
					if(inspectionZZJDInfo != null){
						if (StringUtil.isNotEmpty(inspectionZZJDInfo.getDevice_type_code())) {
							if ("7310".equals(inspectionZZJDInfo.getDevice_type_code())) {
								pre_device_type = "F";
							} else {
								// 获取设备类别前缀（大类）
								pre_device_type = inspectionZZJDInfo.getDevice_type_code().substring(0, 1);
							}
						}
					}
				} else {
					DeviceDocument deviceDocument = deviceService.get(inspectionInfo.getFk_tsjc_device_document_id());
					if (deviceDocument != null) {
						if(StringUtil.isNotEmpty(deviceDocument.getDevice_sort_code())){
							if("7310".equals(deviceDocument.getDevice_sort_code())){
								pre_device_type = "F";
							}else if("2610".equals(deviceDocument.getDevice_sort_code())){
								pre_device_type = "0";
							}else{
								pre_device_type = deviceDocument.getDevice_sort_code().substring(0, 1);
							}
						}
						if (StringUtil.isEmpty(pre_device_type)) {
							if(StringUtil.isNotEmpty(deviceDocument.getDevice_sort())){
								if("7310".equals(deviceDocument.getDevice_sort())){
									pre_device_type = "F";
								}else if("2600".equals(deviceDocument.getDevice_sort())){
									pre_device_type = "0";
								}else{
									pre_device_type = deviceDocument.getDevice_sort().substring(0, 1);
								}
							}
						}
					}
				}
			}
		}

		// 验证审核或签发日期是否存在逻辑错误
		// 审核日期不能早于编制日期，签发日期不能早于审核日期
		if ("04".equals(type)) {
			// 验证审核日期是否早于编制日期
			Date enter_date = inspectionInfo.getEnter_time(); // 编制日期
			if (enter_date == null) {
				if ("3".equals(pre_device_type) || "4".equals(pre_device_type) || "5".equals(pre_device_type)
						|| "6".equals(pre_device_type) || "9".equals(pre_device_type)) {
					// 编制日期为空时，默认设置为检验日期+1天
					String advance_time = DateUtil.format(inspectionInfo.getAdvance_time(), "yyyy-MM-dd");
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", advance_time));
					// 编制日期（机电五部2015-08-24要求，编制日期=检验日期+1天）
					calendar.add(Calendar.DATE, 1);
					enter_date = calendar.getTime();
				} else {
					enter_date = DateUtil.convertStringToDate("yyyy-MM-dd",
							DateUtil.format(inspectionInfo.getAdvance_time(), "yyyy-MM-dd"));
				}
			} else {
				enter_date = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.format(enter_date, "yyyy-MM-dd"));
			}
			if ("3".equals(pre_device_type)) {
				if (cur_op_date.before(enter_date)) {
					return JsonWrapper.failureWrapperMsg(inspectionInfo.getReport_sn() + "审核日期不能早于编制日期，请检查！");
				}
			}
		} else if ("05".equals(type)) { // 验证签发日期是否早于审核日期
			Date check_date = inspectionInfo.getExamine_Date(); // 审核日期
			check_date = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.format(check_date, "yyyy-MM-dd"));
			if (cur_op_date.before(check_date)) {
				return JsonWrapper.failureWrapperMsg(inspectionInfo.getReport_sn() + "签发日期不能早于审核日期，请检查！");
			}
		}

		if (StringUtil.isEmpty(check_flow)) {
			check_flow = report.getCheck_flow();			
		}

		String next_op_id = "";		// 签发人ID（employee表主键）
		String next_op_name = "";	// 签发人姓名
		String rule = "";			// 实际分配规则（1、相同单位优先分配；2、量少优先分配；）
		boolean is_auto_issue = false;	// 是否是自动分配
		
		if (type.equals("04") && "0".equals(check_flow) && "1".equals(revise_conclusion)) {
			// 获取报告自动分配签发人start......
			// 2017-07-03修改，包括压力容器，所有设备均由系统进行自动分配签发人
			// 2017-10-13修改，增加西藏报告，不进行自动分配，由审核人手动选择签发人
			// 2017-12-12修改，增加新疆报告，不进行自动分配，由审核人手动选择签发人
			// 2018-03-29修改，增加九寨报告，不进行自动分配，由审核人手动选择签发人
			if("100069".equals(inspectionInfo.getCheck_unit_id()) || "100090".equals(inspectionInfo.getCheck_unit_id()) || "100091".equals(inspectionInfo.getCheck_unit_id())){
				next_op_id = dataMap.get("next_sub_op").toString();
				next_op_name = dataMap.get("next_op_name").toString();
				if(StringUtil.isNotEmpty(next_op_name)){
					if(next_op_name.equals(inspectionInfo.getEnter_op_name()) || next_op_name.equals(user.getName())){
						return JsonWrapper.failureWrapperMsg("您选择的签发人员（"+next_op_name+"）已参与报告" + inspectionInfo.getReport_sn()
						+ "的签名，请重新选择！");
					}
				}else{
					return JsonWrapper.failureWrapperMsg("请选择签发人再提交！");
				}
				is_auto_issue = false;
			}else{
				// 除压力容器之外其他设备均由系统进行自动随机分配签发人
				Map<String, Object> infoMaps = inspectionCommonService.autoIssue(inspectionInfo, pre_device_type);
				if(Boolean.valueOf(String.valueOf(infoMaps
						.get("success")))){
					next_op_id = String.valueOf(infoMaps.get("next_op_id"));
					next_op_name = String.valueOf(infoMaps.get("next_op_name"));
					rule = infoMaps.get("rule")!=null?String.valueOf(infoMaps.get("rule")):"";
				}else{
					return JsonWrapper.failureWrapperMsg(String.valueOf(infoMaps.get("msg")));
				}
				is_auto_issue = true;
			}
			// 获取报告自动分配签发人end......
		}

		map.put("ins_info_id", ins_info_id);
		map.put("acc_id", acc_id);
		map.put("revise_conclusion", revise_conclusion);
		map.put("op_time", op_time);

		map.put("type", type);

		map.put("flow_num", flow_num);

		// 如果状态是通过，册进入下一个环节,不通过返回上一环节
		if ("1".equals(revise_conclusion) && type.equals("04")) {
			map.put("revise_remark", "结论：通过\n" + revise_remark);
			if ("1".equals(check_flow)) {
				map.put("flag", "0"); // 如果是0就不用指定下一步操作人
			} else {
				map.put("flag", "参数判断是否获取下一步指定人");
			}

			// 获取报告页单独审核信息，如果存在报告页待审核或者审核不通过的情况，本次审核不能保存并不能提交至下一环节
			// 报告页单独审核信息
			List<ReportPageCheckInfo> list = reportPageCheckInfoService.queryPageInfos(ins_info_id,
					inspectionInfo.getReport_type());
			String returnMsg = "";
			for (ReportPageCheckInfo reportPageCheckInfo : list) {
				if (StringUtil.isNotEmpty(reportPageCheckInfo.getAudit_user_id())) {
					if (!"1".equals(reportPageCheckInfo.getData_status())) {
						String page_name = reportPageCheckInfo.getPage_name();
						String data_status = "0".equals(reportPageCheckInfo.getData_status()) ? "待审核" : "审核不通过";
						if (returnMsg.length() > 0) {
							returnMsg += ";\r\n";
						}
						returnMsg += "报告书编号：" + inspectionInfo.getReport_sn() + "，报告页【" + page_name + "】" + data_status;
					}
				}
			}
			if (StringUtil.isNotEmpty(returnMsg)) {
				returnMsg += "。\r\n请等待所有报告页审核通过后，再操作！";
				return JsonWrapper.failureWrapperMsg(returnMsg);
			}else{
				if ("0".equals(check_flow)) {
					if(StringUtil.isEmpty(next_op_id)){
						return JsonWrapper.failureWrapperMsg("当前无在岗签字人员，详询质量部028-86607892！");
					} else {
						if (is_auto_issue) {
							// 记录系统自动分配报告签发日志
							SysAutoIssueLog issueLog = new SysAutoIssueLog();
							issueLog.setBusiness_id(inspectionInfo.getId());
							issueLog.setReport_sn(inspectionInfo.getReport_sn());
							issueLog.setReport_com_name(inspectionInfo.getReport_com_name().trim());
							issueLog.setCheck_unit_id(inspectionInfo.getCheck_unit_id());
							issueLog.setDevice_type(pre_device_type);
							issueLog.setOp_user_id(user.getId());
							issueLog.setOp_user_name(user.getName());
							issueLog.setOp_action(Constant.SYS_OP_ACTION_AUTO_ISSUE);
							issueLog.setOp_remark(Constant.SYS_OP_ACTION_AUTO_ISSUE + "至【" + next_op_name + "】");
							issueLog.setOp_time(
									DateUtil.convertStringToDate(Constant.ymdhmsDatePattern, DateUtil.getCurrentDateTime()));
							issueLog.setTo_user_id(next_op_id);
							issueLog.setTo_user_name(next_op_name);
							issueLog.setIssue_type(rule);
							sysAutoIssueLogService.save(issueLog);

							// 标记报告为系统自动随机分配签发报告
							inspectionInfo.setIs_auto_issue("1");
							inspectionInfoService.save(inspectionInfo);
						}
					}
					map.put("next_sub_op", next_op_id);
					map.put("next_op_name", next_op_name);
				}
				inspectionService.flow_saveCheck(map);
				// 提交到下一步步骤
				inspectionService.subFlowProcess(map, request);
			}
		} else if ("1".equals(revise_conclusion) && type.equals("05")) {
			map.put("revise_remark", "结论：通过\n" + revise_remark);
			map.put("flag", "0");// 如果是0就不用指定下一步操作人
			// 返写业务信息（返写审批人信息等）
			inspectionService.flow_saveCheck(map);
			// 返写设备信息
			inspectionService.dealDeviceInfo(map); // 签发通过时，返写设备信息
			// 将报告流程提交至下一步环节
			inspectionService.subFlowProcess(map, request);
			// 常压罐车和汽车罐车定检报告，当报告书签发后，给报告安全管理人员的联系电话推短信提醒
			// 签发后是否发送消息提醒（1：微信 2：短信 3：微信和短信 0：不发送 ）
			if(StringUtil.isNotEmpty(report.getIssue_msg_type())){
				if(!"0".equals(report.getIssue_msg_type())){
					String item_names = Factory.getSysPara().getProperty("GC_REPORT_ITEM_NAMES");
					if (StringUtil.isEmpty(item_names)) {
						item_names = Constant.GC_REPORT_ITEM_NAMES;
					}
					// 获取发送内容
					Map<String, Object> content_map = reportItemValueService.getGCContent(ins_info_id, inspectionInfo.getReport_type(), item_names);
					// 获取发送目标号码
					String destNumber = content_map.get("SECURITY_TEL")+"";
					// 获取发送内容
					String content = Constant.getGcNoticeContent(content_map);
					
					if(StringUtil.isNotEmpty(destNumber) && StringUtil.isNotEmpty(content)){
						boolean isMobile = TS_Util.checkMobile(destNumber.trim());
						if(isMobile){
							if("1".equals(report.getIssue_msg_type())){
								// 发送微信
								messageService.sendWxMsg(request, ins_info_id, Constant.INSPECTION_CORPID, Constant.INSPECTION_PWD,
										content, destNumber.trim());
							}else if("2".equals(report.getIssue_msg_type())){
								// 发送短信
								messageService.sendMoMsg(request, ins_info_id, content, destNumber.trim());
							}else if("3".equals(report.getIssue_msg_type())){
								// 发送微信和短信
								messageService.sendWxMsg(request, ins_info_id, Constant.INSPECTION_CORPID, Constant.INSPECTION_PWD,
										content, destNumber.trim());
								messageService.sendMoMsg(request, ins_info_id, content, destNumber.trim());
							}
						}
					}
				}
			}
		} else if ("2".equals(revise_conclusion) && type.equals("04")) {

			map.put("revise_remark", "结论：不通过\n" + revise_remark);

			// 回退到上一步
			inspectionService.returApprove(map, request);
			// 如果是报告签发步骤，回退可以默认回退上一步，也可以回退到报告起草
		} else if ("2".equals(revise_conclusion) && type.equals("05")) {
			String backStep = request.getParameter("backType");

			map.put("revise_remark", "结论：不通过\n" + revise_remark);

			map.put("backStep", backStep);

			inspectionService.returApprove(map, request);

		}
		
		Map<String, Object> return_map = new HashMap<String, Object>();
		return_map.put("success", true);
		return_map.put("info_id", ins_info_id);
		return return_map;
	}
	
	
	
	// 报告分页单独审核
	@RequestMapping(value = "check_reportPage")
	@ResponseBody
	public Map<String, Object> check_reportPage(String dataStr,
			HttpServletRequest request) throws Exception {
		//CurrentSessionUser curUser = this.getCurrentUser(); // 获取当前用户登录信息
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User uu = (User)curUser.getSysUser();
		Employee emp = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
		JSONObject dataMap = JSONObject.fromString(dataStr);
		// 业务ID
		String ins_info_id = request.getParameter("infoId");
		// 业务信息
		InspectionInfo inspectionInfo = inspectionInfoService.get(ins_info_id);
		// 环节类型（04：审核 05：签发）
		String type = request.getParameter("type");
		// 步骤号
		String flow_num = inspectionInfo.getFlow_note_id();
		// 流程编号ID
		String acc_id = inspectionService.getActivity_id(ins_info_id, flow_num);
		// 审核结果（1：审核通过 2：审核不通过）
		String revise_conclusion = dataMap.get("revise_conclusion").toString();	
		// 操作时间（审核/签发日期）
		String op_time = dataMap.get("op_time").toString();	

		String revise_remark = dataMap.get("revise_remark").toString();
		if("请在此处填写报告退回原因！".equals(revise_remark)){
			revise_remark = "无";
		}
		
		// 查询报告页单独审核信息
		List<ReportPageCheckInfo> reportPageInfoList = 
			reportPageCheckInfoService.queryPageInfos(ins_info_id, 
					inspectionInfo.getReport_type(), emp.getId());
		// 1：审核通过，更新报告页单独审核信息表审核状态
		// 2：审核不通过，返回上一环节
		if ("1".equals(revise_conclusion) && type.equals("04")) {
			for (ReportPageCheckInfo reportPageCheckInfo : reportPageInfoList) {
				reportPageCheckInfo.setCheck_user_id(emp.getId());
				reportPageCheckInfo.setCheck_user_name(emp.getName());
				reportPageCheckInfo.setCheck_desc(revise_remark);	// 审核备注
				reportPageCheckInfo.setCheck_date(new Date());
				// 数据状态（数据字典REPORT_PAGE_STATUS，0：待审核 1：审核通过 2：审核不通过 99：删除）
				reportPageCheckInfo.setData_status("1");
				reportPageCheckInfoService.save(reportPageCheckInfo);	// 更新报告页单独审核信息表审核状态
				
				String item_name = "AUDIT_DATE"+reportPageCheckInfo.getPage_index();	// 报告页参数，审核日期						
				List<ReportItemValue> list  = reportItemValueService.getItemByItemName(ins_info_id, inspectionInfo.getReport_type(), item_name);
				// 判断报告参数是否有数据，有就执行更新数据操作，没有就执行插入数据操作
				if(list.size()>0){
					String item_id = list.get(0).getId();
					reportItemValueService.updateItemValue(item_id, ins_info_id, inspectionInfo.getReport_type(), item_name, op_time);
				}else{
					// 新增报告参数值表
					ReportItemValue itemValue = new ReportItemValue();
		        	itemValue.setFk_report_id(inspectionInfo.getReport_type());
		        	itemValue.setFk_inspection_info_id(ins_info_id);
		        	itemValue.setItem_name(item_name);
		        	itemValue.setItem_value(op_time);
		        	itemValue.setItem_type("String");
		        	reportItemValueService.save(itemValue);
				}
        		
        		String page_name = reportPageCheckInfo.getPage_name();
        		if (StringUtil.isEmpty(page_name)) {
        			// 报告页索引信息
        			List<ReportItem> itemlist = reportItemService.queryByReportId(inspectionInfo.getReport_type());
        			for (ReportItem reportItem : itemlist) {
            			if (reportItem.getPage_index().equals(reportPageCheckInfo.getPage_index())) {
            				page_name = reportItem.getItme_name();
    					}
    				}
				}
        		logService.setLogs(ins_info_id, "报告页【"+page_name+"】单独审核", "结论：审核通过（" + revise_remark + "）", curUser.getId(), curUser
						.getName(), new Date(), request.getRemoteAddr());
			}
		}else if ("2".equals(revise_conclusion) && type.equals("04")) {
			for (ReportPageCheckInfo reportPageCheckInfo : reportPageInfoList) {
				reportPageCheckInfo.setCheck_user_id(emp.getId());
				reportPageCheckInfo.setCheck_user_name(emp.getName());
				reportPageCheckInfo.setCheck_desc(revise_remark);	// 审核备注
				reportPageCheckInfo.setCheck_date(new Date());
				// 数据状态（数据字典REPORT_PAGE_STATUS，0：待审核 1：审核通过 2：审核不通过 99：删除）
				reportPageCheckInfo.setData_status("2");
				reportPageCheckInfoService.save(reportPageCheckInfo);	// 更新报告页单独审核信息表审核状态
				
				String page_name = reportPageCheckInfo.getPage_name();
        		if (StringUtil.isEmpty(page_name)) {
        			// 报告页索引信息
        			List<ReportItem> itemlist = reportItemService.queryByReportId(inspectionInfo.getReport_type());
        			for (ReportItem reportItem : itemlist) {
            			if (reportItem.getPage_index().equals(reportPageCheckInfo.getPage_index())) {
            				page_name = reportItem.getItme_name();
    					}
    				}
				}
				
				logService.setLogs(ins_info_id, "报告页【"+page_name+"】单独审核", "结论：审核不通过（" + revise_remark + "）", curUser.getId(), curUser
						.getName(), new Date(), request.getRemoteAddr());
			}
			String flow_name = inspectionInfo.getFlow_note_name();
			// 当前处于“报告审核”环节中，分页单独审核不通过时，业务信息才自动退回至“报告录入”环节
			if ("报告审核".equals(flow_name)) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ins_info_id", ins_info_id);
				map.put("acc_id", acc_id);
				map.put("revise_conclusion", revise_conclusion);
				map.put("op_time", op_time);
				map.put("type", type);
				map.put("flow_num", flow_num);
				map.put("revise_remark", "结论：不通过，" + revise_remark);
				// 回退到上一步（报告录入）
				inspectionService.returnReport(map, request);
			}
		}
		return JsonWrapper.successWrapper(ins_info_id);
	}

	public String getFlowNum(String num, String flag) throws Exception {
		String return_num = "";

		if ("pass".equals(flag)) {
			int tt = (Integer.parseInt(num) + 1);

			if (String.valueOf(tt).length() == 1) {
				return_num = "0" + String.valueOf(tt);
			} else {
				return_num = String.valueOf(tt);
			}

		}
		if ("back".equals(flag)) {

			int tt = (Integer.parseInt(num) - 1);

			if (String.valueOf(tt).length() == 1) {
				return_num = "0" + String.valueOf(tt);
			} else {
				return_num = String.valueOf(tt);
			}

		}

		return return_num;

	}

	// 报告打印
	@RequestMapping(value = "flow_print")
	@ResponseBody
	public synchronized HashMap<String, Object> flow_print(
			HttpServletRequest request) throws Exception {

		try {
			HashMap map = new HashMap();

			String ins_info_id = request.getParameter("infoId");

			String acc_id = request.getParameter("acc_id");

			String flow_num = request.getParameter("flow_num");

			if (ins_info_id.indexOf(",") != -1) {
				String ids[] = ins_info_id.split(",");
				String acc_ids[] = acc_id.split(",");
				for (int i = 0; i < ids.length; i++) {
					map.put("ins_info_id", ids[i]);

					map.put("acc_id", acc_ids[i]);

					map.put("flow_num", flow_num);

					map.put("flag", "0");// 如果是0就不用指定下一步操作人

					// map.put("status", getFlowNum(flow_num, "pass"));

					inspectionService.flow_print(map);

					inspectionService.subFlowProcess(map, request);

					// 回写设备（签发时回写）
					// inspectionService.dealDeviceInfo(map);	
				}

			} else {

				map.put("ins_info_id", ins_info_id);

				map.put("acc_id", acc_id);

				map.put("flow_num", flow_num);

				map.put("flag", "0");// 如果是0就不用指定下一步操作人

				// map.put("status", getFlowNum(flow_num, "pass"));

				inspectionService.flow_print(map);

				inspectionService.subFlowProcess(map, request);

				// 回写设备（签发时回写）
				// inspectionService.dealDeviceInfo(map);   
			}
			map.put("success", true);

			return map;
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取信息失败！");
		}
	}

	// 生成报告书编号
	@RequestMapping(value = "flow_addNum")
	@ResponseBody
	public Map<String, Object> flow_addNum(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String info_id = request.getParameter("infoId");
			String temp[] = info_id.split(",");
			for (int i = 0; i < temp.length; i++) {
				InspectionInfo info = inspectionInfoService.get(temp[i]);
				if (StringUtil.isNotEmpty(info.getReport_sn())) {
					map.put("success", false);
					map.put("msg", "有报告已生成了报告书编号，请重新选择！");
					return map;
				}
			}
			map = inspectionCommonService.createReportSn("createReportSn", info_id, "", "", "");
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			return JsonWrapper.failureWrapperMsg("生成报告书编号失败！");
		}
		return map;
	}

	// 报告归档
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "flow_reportEnd")
	@ResponseBody
	public HashMap<String, Object> flow_reportEnd(HttpServletRequest request)
			throws Exception {
		HashMap map = new HashMap();
		try {
			String info_id = request.getParameter("infoId");
			String process_id = request.getParameter("process_id");
			String flow_num = request.getParameter("flow_num");
			String report_sn= request.getParameter("report_sn");
			String deviceId = request.getParameter("deviceId");
			String flowName = request.getParameter("flowName");
			if (info_id.indexOf(",") != -1) {
				String ids[] = info_id.split(",");
				String p_ids[] = process_id.split(",");
				for (int i = 0; i < ids.length; i++) {
					map.put("infoId", ids[i]);
					map.put("process_id", p_ids[i]);
					map.put("flow_num", flow_num);
					inspectionService.flow_reportEnd(map, request);
				}
			} else {
				map.put("infoId", info_id);
				map.put("process_id", process_id);
				map.put("flow_num", flow_num);
				inspectionService.flow_reportEnd(map, request);
			}
			map.put("success", true);
			archivesBoxManager.setReport_snn(map, request);
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("归档失败！");

		}

		return map;
	}

	// 提交到打印步骤
	@RequestMapping(value = "flow_toPrint")
	@ResponseBody
	public HashMap<String, Object> flow_toPrint(HttpServletRequest request)
			throws Exception {

		try {
			HashMap map = new HashMap();

			String info_id = request.getParameter("infoId");

			String acc_id = request.getParameter("acc_id");

			String flow_num = request.getParameter("flow_num");

			map.put("ins_info_id", info_id);

			map.put("acc_id", acc_id);

			map.put("flow_num", flow_num);
			// flag=1 表示走的是监督检验流程
			map.put("flag", "1");

			map.put("cj", "cj");

			inspectionService.subFlowProcess(map, request);

			map.put("success", true);

			return map;
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取信息失败！");
		}
	}

	// 退回报告录入
	@RequestMapping(value = "flow_returnInput")
	@ResponseBody
	public HashMap<String, Object> flow_returnInput(HttpServletRequest request)
			throws Exception {

		try {
			HashMap map = new HashMap();

			String info_id = request.getParameter("infoId");

			String acc_id = request.getParameter("acc_id");

			String flow_num = request.getParameter("flow_num");

			map.put("ins_info_id", info_id);

			map.put("acc_id", acc_id);

			map.put("flow_num", flow_num);

			try {

				inspectionService.returApprove(map, request);

				map.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();

				map.put("success", false);
			}

			return map;
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取信息失败！");
		}
	}

	// 查询是否有报检信息信息
	@RequestMapping(value = "getPara")
	@ResponseBody
	public HashMap<String, Object> getPara(HttpServletRequest request)
			throws Exception {
		String id = request.getParameter("paraId");
		try {
			return inspectionService.getPara(id);
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取信息失败！");
		}
	}

	// 查询业务详细信息
	@RequestMapping(value = "getInfo")
	@ResponseBody
	public HashMap<String, Object> getInfo(HttpServletRequest request)
			throws Exception {

		String id = request.getParameter("id");

		try {
			return inspectionService.getInfo(id);
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取信息失败！");
		}
	}

	// 查询設備详细信息
	@RequestMapping(value = "getDeviceType")
	@ResponseBody
	public HashMap<String, Object> getDeviceType(HttpServletRequest request)
			throws Exception {

		String id = request.getParameter("infoId");

		try {
			return inspectionService.getDeviceType(id);
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取信息失败！");
		}
	}

	// 判断人员持有证书
	@RequestMapping(value = "checkCer")
	@ResponseBody
	public HashMap<String, Object> checkCer(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String infoId = request.getParameter("infoId");

		String userId = request.getParameter("userId");

		String report_type = request.getParameter("report_type");

		String pageIndex = request.getParameter("pageIndex");

		map.put("infoId", infoId);

		map.put("userId", userId);

		map.put("report_type", report_type);

		map.put("pageIndex", pageIndex);

		try {
			return inspectionService.checkCer(map);
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取信息失败！");
		}
	}

	// 复制报告信息
	@RequestMapping(value = "reportCopy")
	@ResponseBody
	public HashMap<String, Object> reportCopy(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String infoId = request.getParameter("infoId");
		String report_type = request.getParameter("report_type");
		String old_report_type = request.getParameter("old_report_type");
		String id = request.getParameter("id");

		map.put("infoId", infoId);
		map.put("report_type", report_type);
		map.put("old_report_type", old_report_type);
		map.put("id", id);
		inspectionService.reportCopy(map);

		try {
			return JsonWrapper.successWrapper(111);
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取信息失败！");
		}
	}

	// 查询检验参数信息
	@RequestMapping(value = "flow_reportInput")
	@ResponseBody
	public ModelAndView flow_reportInput(HttpServletRequest request)
			throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("id", request.getParameter("id"));
		map.put("report_type", request.getParameter("report_type"));

		Map<String, Object> outMap = new HashMap<String, Object>();

		outMap = inspectionService.flow_reportInput(map);

		// request.setAttribute("reportItems", outMap.get("reportItems"));
		// request.setAttribute("mapManName", outMap.get("mapManName"));
		// request.setAttribute("mapManId", outMap.get("mapManId"));
		// request.setAttribute("report_item", outMap.get("report_item"));

		ModelAndView mav = new ModelAndView("app/flow/set_report_item", outMap);
		return mav;

	}

	// 获取科室报检信息
	@RequestMapping(value = "getDetail")
	@ResponseBody
	public HashMap<String, Object> getDetail(HttpServletRequest request)
			throws Exception {
		String id = request.getParameter("id");
		try {
			return inspectionService.getDetail(id);
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取信息失败！");
		}
	}

	// 获取设备信息
	@RequestMapping(value = "getDeviceDetail")
	@ResponseBody
	public HashMap<String, Object> getDeviceDetail(HttpServletRequest request)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<DeviceDTO> list = new ArrayList<DeviceDTO>();
		String id = request.getParameter("id");
		String device_id = request.getParameter("device_id");
		String idArr[] = id.split(",");
		String device_idArr[] = device_id.split(",");
		try {
			for(int i=0;i<idArr.length;i++) {
				DeviceDTO deviceDTO = new DeviceDTO();
				InspectionInfo inspectionInfo = inspectionInfoService.get(idArr[i]);	// 业务信息
				DeviceDocument deviceDocument = deviceService.get(device_idArr[i]);	// 设备信息
				if (inspectionInfo != null && deviceDocument != null) {
					BeanUtils.copyProperties(deviceDTO, deviceDocument);
					deviceDTO.setCheck_unit_id(inspectionInfo.getCheck_unit_id());
					if (StringUtil.isNotEmpty(inspectionInfo.getCheck_unit_id())) {
						Org org = orgManager.get(inspectionInfo.getCheck_unit_id());
						deviceDTO.setCheck_unit_name(StringUtil.isNotEmpty(org
								.getOrgName()) ? org.getOrgName() : ""); // 检验部门
					} else {
						deviceDTO.setCheck_unit_name("");
					}
					if (StringUtil.isNotEmpty(deviceDocument.getDevice_area_code())) {
						if (StringUtil.isNotEmpty(deviceDocument
								.getDevice_area_name())) {
							deviceDTO.setDevice_area_name(deviceDocument
									.getDevice_area_name());
						} else {
							String device_area_name = codeTablesDao
									.getDeviceAreaName(deviceDocument
											.getDevice_area_code());
							deviceDTO
									.setDevice_area_name(StringUtil
											.isNotEmpty(device_area_name) ? device_area_name
											: ""); // 所在区县
						}
					}
					if (inspectionInfo.getAdvance_time() != null) {
						deviceDTO.setCheck_date(DateUtil.getDateTime("yyyy年MM月dd日", inspectionInfo.getAdvance_time()));	// 检验日期
					}
					if(StringUtil.isNotEmpty(inspectionInfo.getReport_com_name())){
						deviceDTO.setCompany_name(inspectionInfo.getReport_com_name());
					}
					if(StringUtil.isNotEmpty(deviceDocument.getFk_company_info_use_id())){
						EnterInfo enterInfo = enterService.get(deviceDocument.getFk_company_info_use_id());
						if(enterInfo!=null){
							if(StringUtil.isNotEmpty(enterInfo.getCom_address())){
								deviceDTO.setCom_addr(enterInfo.getCom_address());
							}
							if(StringUtil.isEmpty(inspectionInfo.getReport_com_name())){
								deviceDTO.setCompany_name(enterInfo.getCom_name());
							}
						}
					}
					//如果设备品种值为空则根据设备类别代码去查询并赋值deviceDTO
					if(StringUtil.isEmpty(deviceDocument.getDevice_sort_name())) {
						if(StringUtil.isNotEmpty(deviceDocument.getDevice_sort_code())) {
							String device_sort_name = codeTablesDao.getValueByCode("device_classify1", deviceDocument.getDevice_sort_code());
							if(StringUtil.isNotEmpty(device_sort_name)) {
								deviceDTO.setDevice_sort_name(device_sort_name.replace(" ", ""));
							}
						}
					}
				}
				list.add(deviceDTO);
			}
			map.put("data", list);
			map.put("success", true);
			return map;
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取设备信息失败！");
		}
	}

	// 获取分配信息
	@RequestMapping(value = "getAllot")
	@ResponseBody
	public HashMap<String, Object> getAllot(HttpServletRequest request)
			throws Exception {
		String id = request.getParameter("id");
		try {
			return inspectionService.getAllot(id);
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取信息失败！");
		}
	}

	// 删除部门报检信息
	@RequestMapping(value = "del")
	@ResponseBody
	public HashMap<String, Object> del(HttpServletRequest request, String ids)
			throws Exception {

		inspectionService.del(request, ids);

		return JsonWrapper.successWrapper(ids);

	}

	// 删除安全阀报检信息
	@RequestMapping(value = "delAccessory")
	@ResponseBody
	public HashMap<String, Object> delAccessory(String ids) throws Exception {

		inspectionService.delAccessory(ids);

		return JsonWrapper.successWrapper(ids);

	}

	// 保存报告设置信息
	@RequestMapping(value = "saveItem")
	@ResponseBody
	public Map<String, Object> saveItem(String dataStr,
			HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String id = request.getParameter("fk_inspection_info_id");
		String report_item = request.getParameter("report_item");
		String xsqnum = request.getParameter("xsq");
		String fee = request.getParameter("fee");

		map.put("id", id);
		map.put("report_item", report_item);
		map.put("dataMap", dataStr);
		map.put("xsqnum", xsqnum);
		map.put("fee", fee);

		inspectionService.saveItem(map);

		return JsonWrapper.successWrapper(111);

	}

	/**
	 * 加载报告（录入）信息
	 * 
	 * @author Liaozw
	 */
	@RequestMapping(value = "reportInfoLoad")
	public ModelAndView reportInfoLoad(HttpServletRequest request,
			HttpServletResponse response/* ,ModelMap map */) throws Exception {
		String ins_info_id = request.getParameter("ins_info_id");
		String report_type = request.getParameter("report_type");
		String device_id = request.getParameter("device_id");
		String org_id = request.getParameter("org_id");
		// String reportItem=request.getParameter("reportItem");

		String type = request.getParameter("type");
		try {
			Map<String, Object> map = inspectionService
					.getReportInfo(ins_info_id, request);

			for (String key : map.keySet()) {
				request.setAttribute(key, map.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("infoId", ins_info_id);
		map.put("report_type", report_type);
		map.put("device_id", device_id);
		map.put("org_id", org_id);
		// map.put("reportItem", reportItem);

		ModelAndView mav = null;

		if (type.equals("input")) {// 報告錄入頁面
			
			mav = new ModelAndView("app/flow/report/report_input", map);
		} else { // 報告審核頁面
			mav = new ModelAndView("app/flow/report/report_audit"/* ,map */);
		}
		return mav;
	}
	
	/**
	 * 加载报告审核信息
	 * 
	 * @author Liaozw
	 */
	@RequestMapping(value = "reportPageInfoLoad")
	public ModelAndView reportPageInfoLoad(HttpServletRequest request,
			HttpServletResponse response/* ,ModelMap map */) throws Exception {
		String ins_info_id = request.getParameter("ins_info_id");
		String report_type = request.getParameter("report_type");
		String device_id = request.getParameter("device_id");
		try {
			Map<String, Object> map = inspectionService
					.getReportPageInfo(ins_info_id);
			for (String key : map.keySet()) {
				request.setAttribute(key, map.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("infoId", ins_info_id);
		map.put("report_type", report_type);
		map.put("device_id", device_id);

		ModelAndView mav = new ModelAndView("app/flow/report/page/report_audit");
		return mav;
	}

	// 查询流程步骤信息
	@RequestMapping(value = "getFlowStep")
	@ResponseBody
	public ModelAndView getFlowStep(HttpServletRequest request)
			throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		map = inspectionService
				.getFlowStep(request.getParameter("ins_info_id"));

		ModelAndView mav = new ModelAndView("app/flow/flow_card", map);

		return mav;

	}
	// 查询流程步骤信息
	@RequestMapping(value = "mapGetFlowStep")
	@ResponseBody
	public HashMap<String, Object> mapGetFlowStep(HttpServletRequest request,String id)
			throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map = inspectionService.getFlowStep(id);
		
		return map;
		
	}

	// 查询业务流程信息
	@RequestMapping(value = "getFlowInfo")
	@ResponseBody
	public ModelAndView getFlowInfo(HttpServletRequest request)
			throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		map = inspectionService.getFlowInfo();

		ModelAndView mav = new ModelAndView("app/flow/misson_index", map);

		return mav;

	}
	// 查询业务流程信息(baidu map)
	@RequestMapping(value = "mapGetFlowInfo")
	@ResponseBody
	public HashMap<String, Object> mapGetFlowInfo(HttpServletRequest request)
			throws Exception {
		
		HashMap<String, Object> map = inspectionService.getFlowInfo();
		
		return map;
	}

	/**
	 * 报告保存
	 * 
	 * @author Liaozw
	 */
	@RequestMapping(value = "reportInfoInput")
	public void flow_reportInfo_input(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 1、保存报告
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();
		MREngine engine = new MREngine(request, response, servletContext);
		String[] PostBackValue = engine.getParameterNames();
		inspectionService.PostBack(PostBackValue, engine, request, response);
		
		// 2、生成报告书编号
		String ins_info_id = request.getParameter("ins_info_id");
		try {
			// 2.1 根据业务信息ID获取业务信息bean
			InspectionInfo insInfo = inspectionInfoService.get(ins_info_id);
			String check_type = insInfo.getInspection().getCheck_type();
			String report_type = insInfo.getReport_type();
			String device_id = insInfo.getFk_tsjc_device_document_id();
			String report_sn = "";
			if (insInfo.getReport_sn() == null || "".equals(insInfo.getReport_sn())) {
				// 获取设备类型
				String device_type = "";
				if (StringUtil.isNotEmpty(device_id)) {
					// 根据设备ID获取设备基础信息bean
					DeviceDocument devicedoc = deviceService.get(device_id);
					if (devicedoc != null) {
						if (StringUtil.isNotEmpty(devicedoc.getDevice_sort_code())) {
							device_type = devicedoc.getDevice_sort_code();
						}
					}

				}
				// 2.2 不存在报告编号，就让它获得存在
				synchronized (this){
					Map<String, Object> reportSnMap = inspectionCommonService.createReportSn("saveReport", ins_info_id, report_type, check_type, device_type);
					report_sn = String.valueOf(reportSnMap.get("report_sn"));
					insInfo.setReport_sn(report_sn);
					inspectionInfoService.save(insInfo);
				}
			} else {
				// 2.3 存在报告编号，就让它继续存在
				report_sn = insInfo.getReport_sn();
			}
			
			// 2.4 将报告书编号更新到报告检验项目参数表
			if(StringUtil.isNotEmpty(report_sn)){
				reportItemValueService.updateItemValue(ins_info_id, report_type, "REPORT_SN", report_sn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 报告领取保存（填写领取单）
	 * 
	 * @param request
	 * @param reportDraw
	 * @throws Exception
	 */
	@RequestMapping(value = "saveAdd")
	@ResponseBody
	public HashMap<String, Object> saveAdd(HttpServletRequest request,
			String info_id,String acc_id,String flow_num,String draw_sn,
			String base64ImagePath,String evaluate,String linkmode) throws Exception {
		
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();  // 获取当前用户登录信息
		try {
			long st = System.currentTimeMillis();
			//根本报检业务数据表id查询是否已经签名推送
			ExecutorService service = Executors.newFixedThreadPool(10);
			String[] ids = info_id.split(",");
			String[] accis = acc_id.split(",");
	        for (int i = 0; i < ids.length; i++) {
	        	long std = System.currentTimeMillis();
	        	InspectionInfo info = new InspectionInfo();//inspectionInfoService.get(ids[i]);
	        	info.setId(ids[i]);
	        	long etdi = System.currentTimeMillis();
	        	Object infoObj = inspectionInfoService.getInfoObj(ids[i]);
	        	long etdi2 = System.currentTimeMillis();
			     System.out.println("保存領取信息-------------"+ (etdi2-etdi));
	        	Object[] infoObjs = null;
	        	if(infoObj!=null) {
	        		infoObjs = (Object[]) infoObj;
	        	}
				ReportDraw draw = new ReportDraw();
				draw.setJob_unit(infoObjs==null?"":(infoObjs[0]==null?"":infoObjs[0].toString()));
				draw.setLinkmode(linkmode);
				draw.setInspectionInfo(info);
				draw.setPulldown_time(DateUtil.convertStringToDate(
						"yyyy-MM-dd", DateUtil.getDateTime("yyyy-MM-dd",new Date())));
				draw.setReport_sn(infoObjs==null?"":(infoObjs[1]==null?"":infoObjs[1].toString()));
				draw.setDraw_sn(draw_sn);
				draw.setData_status("0");	// 数据状态（0：正常  99：已删除）
				draw.setMdy_user_id(curUser.getId());
				draw.setMdy_user_name(curUser.getName());
				draw.setMdy_date(new Date());
				draw.setSign_file(base64ImagePath);
				draw.setEvaluate(evaluate);
				reportDrawService.save(draw);
				// 流程参数
				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("acc_id", accis[i]);
				map.put("infoId", ids[i]);
				map.put("flag", "0");
				map.put("flow_num", flow_num);
				map.put("userid",curUser.getId());
				map.put("username",curUser.getName());
				map.put("flowName",infoObjs==null?"":(infoObjs[2]==null?"":infoObjs[2].toString()));
				map.put("flowId",infoObjs==null?"":(infoObjs[3]==null?"":infoObjs[3].toString()));
				long etd = System.currentTimeMillis();
			     System.out.println("保存領取信息-------------"+ (etd-std));
	            Runnable run = new Runnable() {
	                @Override
	                public void run() {
	                    try {
	                    	
	                    	inspectionService.subFlowProcessNew(map, request);
	                    	
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                }
	            };
	            // 在未来某个时间执行给定的命令
	            service.execute(run);
	        }
			 // 关闭启动线程
	        service.shutdown();
	        // 等待子线程结束，再继续执行下面的代码
	       service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
	        long et = System.currentTimeMillis();
	        System.out.println("all thread complete"+ (et-st));
			wrapper.put("success", true);
			//wrapper.put("data", info);
			//logger.info("user time = " + (et-st) + "mm,infos:"+info_id);
			logService.getSysLogDao().createSQLQuery("insert into action_log (id,class_name,method_name,use_time,content) values (?,?,?,?,?) ", UUID.randomUUID(),this.getClass().getName(),"saveAdd",(et-st),info_id).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			wrapper.put("success", false);
			wrapper.put("msg", "保存报告领取信息失败，请重试！");
		}
		return wrapper;
	}
	
	

	/**
	 * 报告领取保存（填写领取单）
	 * 
	 * @param request
	 * @param reportDraw
	 * @throws Exception
	 */
	@RequestMapping(value = "saveAddNew")
	@ResponseBody
	public HashMap<String, Object> saveAddNew(HttpServletRequest request,
			String info_id,String draw_sn,
			String base64ImagePath,String evaluate,String linkmode) throws Exception {
		
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();  // 获取当前用户登录信息
		try {
			long st = System.currentTimeMillis();
			//根本报检业务数据表id查询是否已经签名推送
			ExecutorService service = Executors.newFixedThreadPool(10);
			String[] ids = info_id.split(",");
	        for (int i = 0; i < ids.length; i++) {
	        	long std = System.currentTimeMillis();
	        	InspectionInfo info = new InspectionInfo();//inspectionInfoService.get(ids[i]);
	        	info.setId(ids[i]);
	        	long etdi = System.currentTimeMillis();
	        	Object infoObj = inspectionInfoService.getInfoObj2(ids[i]);
	        	long etdi2 = System.currentTimeMillis();
			     System.out.println("保存領取信息-------------"+ (etdi2-etdi));
	        	Object[] infoObjs = null;
	        	if(infoObj!=null) {
	        		infoObjs = (Object[]) infoObj;
	        	}
				ReportDraw draw = new ReportDraw();
				draw.setJob_unit(infoObjs==null?"":(infoObjs[0]==null?"":infoObjs[0].toString()));
				draw.setLinkmode(linkmode);
				draw.setInspectionInfo(info);
				draw.setPulldown_time(DateUtil.convertStringToDate(
						"yyyy-MM-dd", DateUtil.getDateTime("yyyy-MM-dd",new Date())));
				draw.setReport_sn(infoObjs==null?"":(infoObjs[1]==null?"":infoObjs[1].toString()));
				draw.setDraw_sn(draw_sn);
				draw.setData_status("0");	// 数据状态（0：正常  99：已删除）
				draw.setMdy_user_id(curUser.getId());
				draw.setMdy_user_name(curUser.getName());
				draw.setMdy_date(new Date());
				draw.setSign_file(base64ImagePath);
				draw.setEvaluate(evaluate);
				reportDrawService.save(draw);
				// 流程参数
				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("infoId", ids[i]);
				map.put("flag", "0");
				map.put("userid",curUser.getId());
				map.put("username",curUser.getName());
				long etd = System.currentTimeMillis();
			     System.out.println("保存領取信息-------------"+ (etd-std));
	            Runnable run = new Runnable() {
	                @Override
	                public void run() {
	                    try {
	                    	
	                    	inspectionService.subFlowProcessNew2(map, request);
	                    	
						} catch (Exception e) {
							Drawlog log = new Drawlog();
							
							
							log.setBusiness_id(map.get("infoId").toString());
							log.setOp_status("0");
							log.setOp_user_id(curUser.getId());
							log.setOp_user_name(curUser.getName());
							log.setError_msg(e.getMessage());
							log.setOp_time(new Date());
							drawlogDao.save(log);
							e.printStackTrace();
						}
	                }
	            };
	            // 在未来某个时间执行给定的命令
	            service.execute(run);
	        }
			 // 关闭启动线程
	        service.shutdown();
	        // 等待子线程结束，再继续执行下面的代码
	       // service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
	        long et = System.currentTimeMillis();
	        System.out.println("all thread complete"+ (et-st));
			wrapper.put("success", true);
			//wrapper.put("data", info);
			//logger.info("user time = " + (et-st) + "mm,infos:"+info_id);
			logService.getSysLogDao().createSQLQuery("insert into action_log (id,class_name,method_name,use_time,content) values (?,?,?,?,?) ", UUID.randomUUID(),this.getClass().getName(),"saveAdd",(et-st),info_id).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			wrapper.put("success", false);
			wrapper.put("msg", "保存报告领取信息失败，请重试！");
		}
		return wrapper;
	}

	/**
	 * 报告领取保存（填写领取单）
	 * 
	 * @param request
	 * @param reportDraw
	 * @throws Exception
	 */
	@RequestMapping(value = "flow_saveDrow")
	@ResponseBody
	public HashMap<String, Object> flow_saveDrow(HttpServletRequest request,ReportDraw reportDraw) throws Exception {
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();  // 获取当前用户登录信息
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try {
			long st = System.currentTimeMillis();
			String acc_id = request.getParameter("acc_id");
			String[] acc_ids = acc_id.split(",");
			String flow_num = request.getParameter("flow_num");
			String info_id = reportDraw.getInspectionInfo().getId();
			String[] info_ids = info_id.split(",");
			String draw_sn = DateUtil.getDateTime("yyyyMMddHHmmssSSS", new Date());
			for (int i = 0; i < info_ids.length; i++) {
				ReportDraw reportDraw1 = new ReportDraw();
				reportDraw1.setIdcard(reportDraw.getIdcard());
				reportDraw1.setJob_unit(reportDraw.getJob_unit());
				reportDraw1.setLinkmode(reportDraw.getLinkmode());
				reportDraw1.setPulldown_op(reportDraw.getPulldown_op());
				reportDraw1.setInspectionInfo(reportDraw.getInspectionInfo());
				reportDraw1.setRemark(reportDraw.getRemark());
				reportDraw1.setPulldown_time(DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.getDateTime("yyyy-MM-dd",new Date())));
				reportDraw1.getInspectionInfo().setId(info_ids[i]);
				reportDraw1.setReport_sn(reportDraw.getReport_sn());
				reportDraw1.setDraw_sn(draw_sn);
				reportDraw1.setData_status("0");	// 数据状态（0：正常  99：已删除）
				reportDrawService.save(reportDraw1);
				// 流程参数
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("acc_id", acc_ids[i]);
				map.put("ins_info_id", info_ids[i]);
				map.put("flag", "0");
				map.put("flow_num", flow_num);
				inspectionService.subFlowProcess(map, request);
			}
			wrapper.put("success", true);
			wrapper.put("op_user_name", curUser.getName());
			long et = System.currentTimeMillis();
			System.out.println("user time = " + (et-st) + "mm,infos:"+info_id);
			logger.info("user time = " + (et-st) + "mm,infos:"+info_id);
			logService.getSysLogDao().createSQLQuery("insert into action_log (id,class_name,method_name,use_time,content) values (?,?,?,?,?) ", UUID.randomUUID(),this.getClass().getName(),"flow_saveDrow",(et-st),"infos:"+info_id).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			wrapper.put("success", false);
			wrapper.put("msg", "保存报告领取信息失败，请重试！");
		}
		return wrapper;
	}
	/**
	 * 报告领取保存（不填写领取单直接保存）
	 * 
	 * @param request
	 * @param reportDraw
	 * @throws Exception
	 */
	@RequestMapping(value = "flow_saveDrow1")
	@ResponseBody
	public HashMap<String, Object> flow_saveDrow1(HttpServletRequest request,
			String inspection_info_id) throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try {
			String acc_id = request.getParameter("acc_id");
			String[] acc_ids = acc_id.split(",");
			String flow_num = request.getParameter("flow_num");
			String info_id = inspection_info_id;
			String[] info_ids = info_id.split(",");
	
			// String sn = "";
			// String report_type = "";
			for (int i = 0; i < info_ids.length; i++) {
				/*
				 * HashMap<String, Object> returnMap =
				 * inspectionService.flow_saveDrow1(info_ids[i]); if
				 * (sn.length()>0) { sn += ","; } sn +=
				 * returnMap.get("sn").toString();
				 * 
				 * if (report_type.length()>0) { report_type += ","; }
				 * report_type += returnMap.get("report_type").toString();
				 */
				// 流程参数
				HashMap<String, Object> map = new HashMap<String, Object>();
	
				map.put("acc_id", acc_ids[i]);
				map.put("ins_info_id", info_ids[i]);
				map.put("flag", "0");
				// map.put("status", getFlowNum(flow_num, "pass"));
				map.put("flow_num", flow_num);
				inspectionService.subFlowProcess(map, request);
			}
	
			wrapper.put("success", true);
	
			// 获取报告领取打印内容（不填写领取单不打印回执单）
			// wrapper.put("printContent", Constant.getReportDrawPrintContent(
			// reportDraw.getPulldown_op(), reportDraw.getJob_unit(),
			// reportDraw.getLinkmode(), reportDraw.getIdcard(), sn,
			// report_type));
		} catch (Exception e) {
			e.printStackTrace();
			wrapper.put("success", false);
			wrapper.put("msg", "保存报告领取信息失败，请重试！");
		}
		// wrapper.put("data", reportDraw);
		return wrapper;
	}

	/**
	 * 报告领取保存（填写领取单）
	 * 
	 * @param request
	 * @param reportDraw
	 * @throws Exception
	 */
	@RequestMapping(value = "mobileReportDrawSave")
	@ResponseBody
	public HashMap<String, Object> mobileReportDrawSave(HttpServletRequest request,@RequestBody Map<String, Object> map) throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try{
			List<ReportDraw> list = inspectionService.mobileReportDrawSave(request,map);
			wrapper.put("success", true);
			wrapper.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			wrapper.put("success", false);
			wrapper.put("msg", "保存报告领取信息失败，请重试！");
		}
		return wrapper;
	}
	@RequestMapping(value = "mobileReportDrawSave2")
	@ResponseBody
	public HashMap<String, Object> mobileReportDrawSave2(HttpServletRequest request,@RequestBody Map<String, Object> params) throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try{
			ReportDraw rt = inspectionService.mobileReportDrawSave2(request,params);
			wrapper.put("success", true);
			wrapper.put("data", rt);
		} catch (Exception e) {
			e.printStackTrace();
			wrapper.put("success", false);
			wrapper.put("msg", "保存报告领取信息失败，请重试！");
		}
		return wrapper;
	}
	
	
	/**
	 * 打印报告show
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @return
	 * @throws Exception
	 */
	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "flow_showPrint")
	// public String flow_showPrint(HttpServletRequest request, String id)
	// throws Exception {
	// List<Map<String, Object>> mapList = infoService.queryInfo(id);
	// request.getSession().setAttribute("mapList", mapList);
	// return "app/report/report_print_left";
	// }
	//
	// 查询报告是否录入信息
	@RequestMapping(value = "getRportIs")
	@ResponseBody
	public HashMap<String, Object> getRportIs(HttpServletRequest request)
			throws Exception {
		String id = request.getParameter("infoId");
		try {
			return inspectionService.getRportIs(id);
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取信息失败！");
		}
	}

	// 提交到审核步骤
	@RequestMapping(value = "subCheck")
	@ResponseBody
	public HashMap<String, Object> subCheck(HttpServletRequest request)
			throws Exception {
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User user = (User) curUser.getSysUser();
		
		String ins_info_id = request.getParameter("infoId");
		String acc_id = request.getParameter("acc_id");
		String flow_num = request.getParameter("flow_num");
		String check_flow = request.getParameter("check_flow");
		String sub_op_id = request.getParameter("sub_op_id");
		String sub_op_name = request.getParameter("sub_op_name");

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("flow_num", flow_num);
		map.put("flag", "指定下一步审核人员");
		map.put("next_sub_op", sub_op_id);
		map.put("next_op_name", sub_op_name);
		
		try {
			String pre_device_type = "";
			String[] temp = ins_info_id.split(",");
			String[] tp = acc_id.split(",");
			for (int i = 0; i < temp.length; i++) {
				InspectionInfo info = inspectionInfoService.get(temp[i]);
				if (info != null) {
					if(StringUtil.isNotEmpty(sub_op_name)){
						if(sub_op_name.equals(info.getEnter_op_name())){
							return JsonWrapper.failureWrapperMsg("您选择的审核人员（"+sub_op_name+"）已参与报告" + info.getReport_sn()
							+ "的签名，请重新选择！");
						}
					}else{
						if("100069".equals(info.getCheck_unit_id()) || "100090".equals(info.getCheck_unit_id()) || "100091".equals(info.getCheck_unit_id())){
							return JsonWrapper.failureWrapperMsg("请选择审核人员再提交！");
						}
					}
					
					if (StringUtil.isNotEmpty(info.getFk_tsjc_device_document_id())) {
						if ("11111111111111111111111111111111".equals(info.getFk_tsjc_device_document_id())) {
							// 获取监检业务信息
							InspectionZZJDInfo inspectionZZJDInfo = inspectionZZJDInfoService.getByInfoId(temp[i]);
							if(inspectionZZJDInfo != null){
								if (StringUtil.isNotEmpty(inspectionZZJDInfo.getDevice_type_code())) {
									if ("7310".equals(inspectionZZJDInfo.getDevice_type_code())) {
										pre_device_type = "F";
									} else {
										// 获取设备类别前缀（大类）
										pre_device_type = inspectionZZJDInfo.getDevice_type_code().substring(0, 1);
									}
								}
							}
						} else {
							DeviceDocument deviceDocument = deviceService.get(info.getFk_tsjc_device_document_id());
							if (deviceDocument != null) {
								if(StringUtil.isNotEmpty(deviceDocument.getDevice_sort_code())){
									if("7310".equals(deviceDocument.getDevice_sort_code())){
										pre_device_type = "F";
									}else if("2610".equals(deviceDocument.getDevice_sort_code())){
										pre_device_type = "0";
									}else{
										pre_device_type = deviceDocument.getDevice_sort_code().substring(0, 1);
									}
								}
								if (StringUtil.isEmpty(pre_device_type)) {
									if(StringUtil.isNotEmpty(deviceDocument.getDevice_sort())){
										if("7310".equals(deviceDocument.getDevice_sort())){
											pre_device_type = "F";
										}else if("2600".equals(deviceDocument.getDevice_sort())){
											pre_device_type = "0";
										}else{
											pre_device_type = deviceDocument.getDevice_sort().substring(0, 1);
										}
									}
								}
								
								if ("3".equals(pre_device_type)) {	// 3：电梯
									// 1、验证成都市内电梯二维码编号和使用登记账号是否重复（电梯才验证二维码和使用登记账号）
									if (StringUtil.isNotEmpty(info.getReport_type())) {
										Report report = reportService.get(info.getReport_type());
										if (report != null) {
											if ("1".equals(report.getFlag())) {
												if (StringUtil.isEmpty(deviceDocument.getDevice_area_code())) {
													return JsonWrapper.failureWrapperMsg("报告" + info.getReport_sn()
															+ "设备注册代码" + deviceDocument.getDevice_registration_code()
															+ "设备所在区域是必填项，请到“设备注册”中补充完整后再提交审核！");
												} else {
													// 验证设备所在区域是否是成都市内，非成都市内不验证设备二维码编号
													String cd_area_codes = Factory.getSysPara()
															.getProperty("INSPECTION_DT_CD_AREA_CODES");
													if (StringUtil.isEmpty(cd_area_codes)) {
														cd_area_codes = Constant.INSPECTION_DT_CD_AREA_CODES.toString();
													}
													if (cd_area_codes.contains(deviceDocument.getDevice_area_code())) {
														// 100069：援藏特检突击队，因西藏设备不存在金属二维码，故不做任何验证
														// 2017-10-25张展彬要求，电梯安全评估报告不验证二维码编号
														// 2017-12-12明子涵要求，100090：援疆特检突击队，因西藏设备不存在金属二维码，故不做任何验证
														// 2018-03-29明子涵要求，100091：九寨特检突击队，因九寨设备不存在金属二维码，故不做任何验证
														String report_name = report.getReport_name();
														if (!"100069".equals(info.getCheck_unit_id())
																&& !"100090".equals(info.getCheck_unit_id())
																&& !"100091".equals(info.getCheck_unit_id())
																&& !report_name.contains("电梯安全评估")) {
															// 1、验证二维码编号
															String device_qr_code = info.getDevice_qr_code();
															if (StringUtil.isEmpty(device_qr_code)) {
																return JsonWrapper
																		.failureWrapperMsg("报告" + info.getReport_sn()
																				+ "二维码编号是必填项，请补充完整后再提交审核！");
															} else {
																for (int j = 0; j < device_qr_code.trim()
																		.length(); j++) {
																	if (!Character
																			.isDigit(device_qr_code.trim().charAt(j))) {
																		return JsonWrapper.failureWrapperMsg(
																				"亲，报告" + info.getReport_sn() + "二维码编号"
																						+ device_qr_code.trim()
																						+ "错误，必须是6位数字哦，请重新输入再提交审核！");
																	}
																}
																if (device_qr_code.trim().length() != 6) {
																	return JsonWrapper.failureWrapperMsg(
																			"亲，报告" + info.getReport_sn() + "二维码编号"
																					+ device_qr_code.trim()
																					+ "错误，必须是6位数字哦，请重新输入再提交审核！");
																}

																List<DeviceDocument> list = deviceService
																		.getDeviceInfosByQRCode(
																				info.getFk_tsjc_device_document_id(),
																				info.getDevice_qr_code().trim());
																for (DeviceDocument device : list) {
																	if (device != null) {
																		if (StringUtil.isNotEmpty(
																				info.getFk_tsjc_device_document_id())) {
																			if (StringUtil.isNotEmpty(device.getId())) {
																				if (!device.getId().equals(info
																						.getFk_tsjc_device_document_id())) {
																					return JsonWrapper
																							.failureWrapperMsg("亲，报告"
																									+ info.getReport_sn()
																									+ "二维码编号"
																									+ device_qr_code
																											.trim()
																									+ "已与设备（"
																									+ device.getDevice_registration_code()
																									+ "）重复，请纠正再提交审核！");
																				}
																			}
																		}
																	}
																}
															}

															// 2、验证使用登记证号（2017-11-10张展彬同意陈德平提出修改）
															String registration_num = info
																	.getRegistration_num();
															if (StringUtil.isEmpty(registration_num)) {
																if (info.getReport_sn().contains("TD")) {
																	return JsonWrapper.failureWrapperMsg(
																			"报告" + info.getReport_sn()
																					+ "使用登记证号是必填项，请补充完整后再提交审核！");
																}
															} else {
																boolean validate = true;
																if (info.getReport_sn().contains("TA")) {
																	if (registration_num.contains("/")
																			|| !registration_num.contains("\\")
																			|| !registration_num.contains("-")
																			|| !registration_num.contains("——")) {
																		validate = false;
																	}
																}

																if (validate) {
																	List<DeviceDocument> list = deviceService
																			.getDeviceInfosByRegistrationNum(
																					info.getFk_tsjc_device_document_id(),
																					registration_num.trim());
																	for (DeviceDocument device : list) {
																		if (device != null) {
																			if (StringUtil.isNotEmpty(info
																					.getFk_tsjc_device_document_id())) {
																				if (StringUtil
																						.isNotEmpty(device.getId())) {
																					if (!device.getId().equals(info
																							.getFk_tsjc_device_document_id())) {
																						return JsonWrapper
																								.failureWrapperMsg(
																										"亲，报告" + info
																												.getReport_sn()
																												+ "使用登记证号"
																												+ registration_num
																														.trim()
																												+ "已与设备（"
																												+ device.getDevice_registration_code()
																												+ "）重复，请纠正再提交审核！");
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}

									// 2、验证电梯单位内编号
									String internal_num = StringUtil.isNotEmpty(deviceDocument.getInternal_num())
											? deviceDocument.getInternal_num() : ""; // 设备基础表单位内编号
									String report_internal_num = "";
									List<ReportItemValue> list = reportItemValueService.getItemByItemName(temp[i],
											info.getReport_type(), "INTERNAL_NUM");
									for (ReportItemValue reportItemValue : list) {
										if ("INTERNAL_NUM".equals(reportItemValue.getItem_name())) {
											report_internal_num = StringUtil.isNotEmpty(reportItemValue.getItem_value())
													? reportItemValue.getItem_value() : "";
										}
									}
									//若不是九寨沟监检报告则进行单位内编号验证
									if(!reportService.get(info.getReport_type()).equals("杂物电梯监督检验报告(九寨沟)")) {
										if (!internal_num.trim().equals(report_internal_num.trim())) {
											return JsonWrapper.failureWrapperMsg(
													"亲，报告" + info.getReport_sn() + "单位内编号（" + report_internal_num
															+ "）与报检设备的单位内编号（" + internal_num + "）不一致，请纠正再提交审核！");
										}
									}
								}
							}
						}
					}
					
					// 报告审批流程（0：二级审核 1：一级审核）
					// 安全阀校验报告只有一级审核，所以此处交由系统进行自动随机分配签发人
					if ("1".equals(check_flow)) {
						// 自动随机分配start......
						String next_op_id = ""; // 签发人ID（employee表主键）
						String next_op_name = ""; // 签发人姓名
						String rule = ""; // 实际分配规则（1、相同单位优先分配；2、量少优先分配；）
	
						Map<String, Object> infoMaps = inspectionCommonService.autoIssue(info, pre_device_type);
						if (Boolean.valueOf(String.valueOf(infoMaps.get("success")))) {
							next_op_id = String.valueOf(infoMaps.get("next_op_id"));
							next_op_name = String.valueOf(infoMaps.get("next_op_name"));
							rule = infoMaps.get("rule") != null ? String.valueOf(infoMaps.get("rule")) : "";
						} else {
							return JsonWrapper.failureWrapperMsg(String.valueOf(infoMaps.get("msg")));
						}

						if (StringUtil.isEmpty(next_op_id)) {
							return JsonWrapper.failureWrapperMsg("当前无在岗签字人员，详询质量部028-86607892！");
						} else {
							// 记录系统自动随机分配报告签发日志
							SysAutoIssueLog issueLog = new SysAutoIssueLog();
							issueLog.setBusiness_id(info.getId());
							issueLog.setReport_sn(info.getReport_sn());
							issueLog.setReport_com_name(info.getReport_com_name().trim());
							issueLog.setCheck_unit_id(info.getCheck_unit_id());
							issueLog.setDevice_type(pre_device_type);
							issueLog.setOp_user_id(user.getId());
							issueLog.setOp_user_name(user.getName());
							issueLog.setOp_action(Constant.SYS_OP_ACTION_AUTO_ISSUE);
							issueLog.setOp_remark(Constant.SYS_OP_ACTION_AUTO_ISSUE + "至【" + next_op_name + "】");
							issueLog.setOp_time(DateUtil.convertStringToDate(Constant.ymdhmsDatePattern,
									DateUtil.getCurrentDateTime()));
							issueLog.setTo_user_id(next_op_id);
							issueLog.setTo_user_name(next_op_name);
							issueLog.setIssue_type(rule);
							sysAutoIssueLogService.save(issueLog);

							// 标记报告为系统自动随机分配签发报告
							info.setIs_auto_issue("1");
							inspectionInfoService.save(info);
						}
						map.put("next_sub_op", next_op_id);
						map.put("next_op_name", next_op_name);
						// 自动随机分配end......
					}
				}
				
				map.put("ins_info_id", temp[i]);
				map.put("acc_id", tp[i]);
				map.put("acc_id", tp[i]);
				inspectionService.subFlowProcess(map, request);
				
				/*
				// 获取报告页单独审核信息，如果存在报告页单独审核的情况，则发送微信和短信提醒审核人
				// 业务信息
				InspectionInfo inspectionInfo = inspectionInfoService.get(temp[i]);
				// 报告页单独审核信息
				List<ReportPageCheckInfo> list = reportPageCheckInfoService.queryPageInfos(temp[i], inspectionInfo.getReport_type());
				if (!list.isEmpty()) {
					String sendUserId = "";
					for(ReportPageCheckInfo reportPageCheckInfo : list){
						if(StringUtil.isNotEmpty(reportPageCheckInfo.getAudit_user_id())){
							// 同一个业务信息，已发送消息的用户，不再发送消息提醒
							if(!sendUserId.contains(reportPageCheckInfo.getAudit_user_id())){
								// 1、根据单独审核人ID获取审核人员信息
								Employee employee = employeesService.get(reportPageCheckInfo.getAudit_user_id());
								if(employee!=null){
									// 2、获取人员手机号码
									String destNumber = employee.getMobileTel();
									if(StringUtil.isNotEmpty(destNumber)){
										String content =  Constant.getWsReportNoticeContent(inspectionInfo.getReport_sn());
										// 3、发送短信
										messageService.sendMoMsg(request, temp[i], content, destNumber);	
										// 4、发送微信
										messageService.sendWxMsg(request, temp[i], content, destNumber);
										// 5、记录已发送消息用户id
										if(sendUserId.length()>0){
											sendUserId += ","; 
										}
										sendUserId += reportPageCheckInfo.getAudit_user_id(); 
									}
								}
							}
						}
					}
				}
				*/
			}
			map.put("success", true);
			map.put("data", ins_info_id);
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("提交失败！");
		}
		return map;
	}
	
	/**
	 * 发微信
	 * 
	 * @param request
	 * @param id -- 检验业务信息（InspectionInfo）id
	 * @throws Exception
	 * 
	 * @author GaoYa
	 * @date 2015-12-17 17:50:00
	 */
	@RequestMapping(value = "sendMsgs")
	@ResponseBody
	public HashMap<String, Object> sendMsgs(HttpServletRequest request, String ids)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {			
			String[] temp = ids.split(",");
			for (int i = 0; i < temp.length; i++) {					
				// 获取报告页单独审核信息，如果存在报告页单独审核的情况，则发送微信和短信提醒审核人
				// 业务信息
				InspectionInfo inspectionInfo = inspectionInfoService.get(temp[i]);
				// 报告页单独审核信息
				List<ReportPageCheckInfo> list = reportPageCheckInfoService.queryPageInfos(temp[i], inspectionInfo.getReport_type());
				if (!list.isEmpty()) {
					String sendUserId = "";
					for(ReportPageCheckInfo reportPageCheckInfo : list){
						if(StringUtil.isNotEmpty(reportPageCheckInfo.getAudit_user_id())){
							// 同一个业务信息，已发送消息的用户，不再发送消息提醒
							if(!sendUserId.contains(reportPageCheckInfo.getAudit_user_id())){
								// 1、根据单独审核人ID获取审核人员信息
								Employee employee = employeesService.get(reportPageCheckInfo.getAudit_user_id());
								if(employee!=null){
									// 2、获取人员手机号码
									String destNumber = employee.getMobileTel();
									if(StringUtil.isNotEmpty(destNumber)){
										String content =  Constant.getWsReportNoticeContent(inspectionInfo.getReport_sn());
										// 3、发送短信
										//String send_mo_status = messageService.sendMoMsg(request, temp[i], content, destNumber);	
										// 4、发送微信
										String send_wx_status = messageService.sendWxMsg(request, temp[i], "", "", content, destNumber);
										// 5、记录已发送消息用户id
										if("0".equals(send_wx_status.trim())){	// "0".equals(send_mo_status.trim())
											if(sendUserId.length()>0){
												sendUserId += ","; 
											}
											sendUserId += reportPageCheckInfo.getAudit_user_id(); 
										}
									}
								}
							}
						}
					}
					if(StringUtil.isEmpty(sendUserId)){
						return JsonWrapper.failureWrapperMsg("微信和短信消息发送失败！");
					}
				}
			}	
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
			return JsonWrapper.failureWrapperMsg("微信和短信消息发送失败！");
		}
		return map;
	}

	// 报告审核批量退回
	@RequestMapping(value = "backCheck")
	@ResponseBody
	public HashMap<String, Object> backCheck(String dataStr,
			HttpServletRequest request) throws Exception {

		JSONObject dataMap = JSONObject.fromString(dataStr);

		String ins_info_id = request.getParameter("infoId");

		System.out.println("________________" + ins_info_id);

		String acc_id = request.getParameter("acc_id");

		String flow_num = request.getParameter("flow_num");

		String type = request.getParameter("type");

		String revise_remark = dataMap.get("revise_remark").toString();
		if("请在此处填写报告退回原因！".equals(revise_remark)){
			revise_remark = "无";
		}

		HashMap<String, Object> map = new HashMap();

		map.put("flow_num", flow_num);

		map.put("revise_remark", "结论：不通过\n" + revise_remark);
		map.put("op_time", new Date());

		map.put("type", type);

		if (type.equals("05")) {

			String backStep = request.getParameter("backType");

			map.put("backStep", backStep);

		}

		// 判断是否是产品监督流程
		CurrentSessionUser user = SecurityUtil.getSecurityUser();

		String unit_name = user.getDepartment().getOrgCode();

		try {
			// 判断是否多条数据
			if (ins_info_id.indexOf(",") != -1) {
				String[] temp = ins_info_id.split(",");
				String[] tp = acc_id.split(",");
				for (int i = 0; i < temp.length; i++) {
					map.put("ins_info_id", temp[i]);
					map.put("acc_id", tp[i]);

					inspectionService.returApprove(map, request);
				}
				map.put("success", true);
			} else {

				map.put("ins_info_id", ins_info_id);
				map.put("acc_id", acc_id);
				inspectionService.returApprove(map, request);
				map.put("success", true);
			}

		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("提交失败！");

		}
		return map;
	}
	
	// 报告归档时批量退回
	@RequestMapping(value = "end_backCheck")
	@ResponseBody
	public HashMap<String, Object> end_backCheck(String dataStr,
			HttpServletRequest request) throws Exception {
		
		JSONObject dataMap = JSONObject.fromString(dataStr);
		
		String ins_info_id = request.getParameter("infoId");
		String acc_id = request.getParameter("acc_id");
		String flow_num = request.getParameter("flow_num");
		String revise_remark = dataMap.get("revise_remark").toString();
		if("请在此处填写报告退回原因！".equals(revise_remark)){
			revise_remark = "无";
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("flow_num", flow_num);
		map.put("revise_remark", "结论：归档退回\r\n" + revise_remark);
		map.put("op_time", new Date());

		try {
			String[] temp = ins_info_id.split(",");
			String[] tp = acc_id.split(",");
			for (int i = 0; i < temp.length; i++) {
				map.put("ins_info_id", temp[i]);
				map.put("acc_id", tp[i]);
				inspectionService.returApprove(map, request);
			}
			map.put("success", true);
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("归档退回失败！");
		}
		return map;
	}

	// 撤回待审核的报告
	@RequestMapping(value = "backModify")
	@ResponseBody
	public HashMap<String, Object> backModify(HttpServletRequest request)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String ins_info_id = request.getParameter("infoId");
			String acc_id = request.getParameter("acc_id");
			String flow_num = request.getParameter("flow_num");
			String[] ids = ins_info_id.split(",");
			String[] tp = acc_id.split(",");
			for (int i = 0; i < ids.length; i++) {
				map.put("ins_info_id", ids[i]);
				map.put("acc_id", tp[i]);
				map.put("flow_num", flow_num);
				inspectionService.returApprove(map, request);
			}
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "撤回失败！请稍后再试！");
		}
		return map;
	}
	
	// 撤回自动分配待签发报告
	@RequestMapping(value = "qf_backs")
	@ResponseBody
	public HashMap<String, Object> qf_backs(HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String ins_info_id = request.getParameter("infoId");
			String acc_id = request.getParameter("acc_id");
			String device_types = request.getParameter("device_types");
			String flow_num = request.getParameter("flow_num");
			
			String[] ids = ins_info_id.split(",");
			String[] d_types = device_types.split(",");
			String[] tp = acc_id.split(",");
			
			HashMap<String, Object> info_maps = new HashMap<String, Object>();
			
			for (int i = 0; i < ids.length; i++) {
				if(!info_maps.containsKey(ids[i])){
					InspectionInfo inspectionInfo = inspectionInfoService.get(ids[i]);
					Report report = reportService.get(inspectionInfo.getReport_type());
					List<CertificateRule> rule_list = certificateRuleService.queryRuleCode(d_types[i], inspectionInfo.getCheck_unit_id(),
							report.getIs_issue(), report.getReport_name().trim(), inspectionInfo.getReport_type());
					if (rule_list.isEmpty()) {
						map.put("success", false);
						map.put("msg", inspectionInfo.getReport_sn()+"当前签字人员非请假和停用状态，无法撤回！详询质量部028-86607892！");
						return map;
					}else{
						if(rule_list.size()!=1){
							map.put("success", false);
							map.put("msg", inspectionInfo.getReport_sn()+"当前签字人员非请假和停用状态，无法撤回！详询质量部028-86607892！");
							return map;
						}else{
							CertificateRule rule = rule_list.get(0);
							if(rule == null){
								map.put("success", false);
								map.put("msg", inspectionInfo.getReport_sn()+"当前签字人员非请假和停用状态，无法撤回！详询质量部028-86607892！");
								return map;
							}else{
								SysAutoIssueLog issue_log = sysAutoIssueLogService.getIssueLog(ids[i]);
								if(issue_log == null){
									map.put("success", false);
									map.put("msg", inspectionInfo.getReport_sn()+"当前签字人员非请假和停用状态，无法撤回！详询质量部028-86607892！");
									return map;
								}else{
									List<CertificateBy> user_list = certificateByService.queryByRuleId(rule.getId(), issue_log.getTo_user_id());
									for(CertificateBy cert : user_list){
										if("1".equals(cert.getStatus()) && "0".equals(cert.getIs_vacation())){
											map.put("success", false);
											map.put("msg", inspectionInfo.getReport_sn()+"当前签字人员非请假和停用状态，无法撤回！详询质量部028-86607892！");
											return map;
										}
									}
								}
							}
						}
					}
					
					map.put("ins_info_id", ids[i]);
					map.put("acc_id", tp[i]);
					map.put("flow_num", flow_num);
					map.put("backStep", "1");
					inspectionService.returnIssueReports(map, request);
					info_maps.put(ids[i], tp[i]);
				}
				map.put("success", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "报告撤回失败！请稍后再试！");
		}
		return map;
	}

	// 报告作废
	@RequestMapping(value = "delReport")
	@ResponseBody
	public HashMap<String, Object> delReport(HttpServletRequest request)
			throws Exception {

		String ids = request.getParameter("ids");

		inspectionService.delReport(ids, request);

		return JsonWrapper.successWrapper(ids);
	}

	// 产品监检生成任务
	@RequestMapping(value = "subRport")
	@ResponseBody
	public HashMap<String, Object> subRport(HttpServletRequest request)
			throws Exception {

		String flowId = request.getParameter("flowId");

		if (StringUtil.isNotEmpty(flowId)) {

			HashMap<String, String> map = new HashMap<String, String>();

			String report_type = request.getParameter("report_type");

			String ids = request.getParameter("ids");

			map.put("report_type", report_type);

			map.put("flowId", flowId);

			map.put("status", FLOW_INPUT);

			if (ids.indexOf(",") != -1) {

				String[] temp = ids.split(",");

				int count = 0;
				for (int i = 0; i < temp.length; i++) {

					count++;

					map.put("ids", temp[i]);

					map.put("count", String.valueOf(count));

					inspectionService.subRport(map, request);

					// inspectionService.StarFlowProcess(map, request);

				}

			} else {
				map.put("ids", ids);

				map.put("count", "1");

				inspectionService.subRport(map, request);

				// inspectionService.StarFlowProcess(map, request);

			}

			return JsonWrapper.successWrapper(ids);

		} else {
			System.err.print("没有选择流程！");
			return JsonWrapper.failureWrapper("没有选择流程！");
		}
	}

	//
	@RequestMapping(value = "getPage")
	@ResponseBody
	public HashMap<String, Object> getPage(HttpServletRequest request)
			throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			String checkType = request.getParameter("checkType");
			String device_code = request.getParameter("device_code");
			String json = inspectionService.getPage(checkType, device_code);

			map.put("data", json);
			map.put("success", true);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg("访问失败！");
		}
	}
	
	
	@RequestMapping(value = "getContracts")
	@ResponseBody
	public HashMap<String, Object> getContracts(HttpServletRequest request)
			throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			String json = inspectionService.getContracts();
			map.put("data", json);
			map.put("success", true);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg("访问失败！");
		}
	}

	// 提交数据到科室
	@RequestMapping(value = "subDep")
	@ResponseBody
	public HashMap<String, Object> subDep(HttpServletRequest request)
			throws Exception {
		String ids = request.getParameter("ids");
		String checktypes = request.getParameter("checktypes");

		HashMap<String, Object> wrapper = JsonWrapper.successWrapper();
		String msg = inspectionService.subDep(ids, checktypes, request);
		wrapper.put("msg", msg);
		return wrapper;

	}

	// 退回到科室报检
	@RequestMapping(value = "backINfo")
	@ResponseBody
	public HashMap<String, Object> backINfo(HttpServletRequest request)
			throws Exception {
		String ids = request.getParameter("infoId");
		HashMap map = new HashMap();
		// String info_id = request.getParameter("infoId");
		String acc_id = request.getParameter("acc_id");
		String flow_num = request.getParameter("flow_num");
		if (ids.indexOf(",") != -1) {
			String[] temp = ids.split(",");
			String[] temp2 = acc_id.split(",");
			// String [] temp3 = flow_num.split(",");
			for (int i = 0; i < temp.length; i++) {
				ids = temp[i];
				acc_id = temp2[i];
				// flow_num=temp3[i];
				map.put("ins_info_id", ids);
				map.put("acc_id", acc_id);
				map.put("flow_num", flow_num);
				inspectionService.returApprove(map, request);
				map.put("success", true);
			}
		} else {
			map.put("ins_info_id", ids);

			map.put("acc_id", acc_id);

			map.put("flow_num", flow_num);

			inspectionService.returApprove(map, request);

			map.put("success", true);
		}
		// inspectionService.backINfo(ids);
		return map;
	}

	// 报告录入时退回
	@RequestMapping(value = "reportBackINfo")
	@ResponseBody
	public HashMap<String, Object> reportBackINfo(HttpServletRequest request)
			throws Exception {
		String ids = request.getParameter("infoId");
		HashMap map = new HashMap();
		String nextHandle = request.getParameter("nextHandle");
		String acc_id = request.getParameter("acc_id");
		String flow_num = request.getParameter("flow_num");
		if (ids.indexOf(",") != -1) {
			String[] temp = ids.split(",");
			String[] temp2 = acc_id.split(",");
			// String [] temp3 = flow_num.split(",");
			for (int i = 0; i < temp.length; i++) {
				// ids = temp[i];
				// acc_id = temp2[i];
				// flow_num=temp3[i];
				map.put("ins_info_id", temp[i]);
				map.put("acc_id", temp2[i]);
				map.put("flow_num", flow_num);
				map.put("nextHandle", nextHandle);
				inspectionService.returApprove(map, request);
				map.put("success", true);
			}

		} else {
			map.put("ins_info_id", ids);

			map.put("acc_id", acc_id);

			map.put("flow_num", flow_num);

			map.put("nextHandle", nextHandle);

			inspectionService.returApprove(map, request);

			map.put("success", true);
		}
		// inspectionService.backINfo(ids);

		return map;
	}

	// @Override
	public CurrentSessionUser onLoginFail(CurrentSessionUser arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	// 系统登录发送短信方法
	public String sendLoginMessage(CurrentSessionUser arg0)
			throws MalformedURLException, UnsupportedEncodingException {

		URL url = null;
		String CorpID = "scsei";// 账户名
		String Pwd = "e7976a4f6931f1db";// 密码
		String kind = "803";
		String smsId = "09";

		User uu = (User)arg0.getSysUser();
		Employee emp = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
		
		String tel = emp.getMobileTel();

		if (tel == null || tel.equals("")) {

			return null;

		}

		String userName = emp.getName();

		SimpleDateFormat far = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String loginTime = far.format(new Date());

		String content = userName + "于" + loginTime + "登录四川特种设备检验系统. 【四川特检】";
		String send_content = URLEncoder.encode(content
				.replaceAll("<br/>", " "), "GBK");// 发送内容
		System.out.println("`````````````````````````````````" + send_content);

		url = new URL(
				"http://sdk.zyer.cn/SmsService/SmsService.asmx/SendEx?LoginName="
						+ CorpID + "&Password=" + Pwd + "&SmsKind=" + kind
						+ "&SendSim=" + tel + "&ExpSmsId=" + smsId
						+ "&MsgContext=" + send_content);
		BufferedReader in = null;
		String inputLine = "0";

		System.out.println("`````````````````````````````````" + url);
		try {
			// System.out.println("开始发送短信手机号码为 ："+Mobile);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			System.out.println("`````````````````````````````````" + in);
			inputLine = in.readLine();
			String inputLine1 = in.readLine();
			inputLine = in.readLine();
			String inputLine3 = in.readLine();
			String inputLine4 = in.readLine();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			inputLine = "-9";
		}

		// 0，发送成功；-10、用户认证失败；-11、ip或域名错误；-12、余额不足；-14、提交手机号超量；-15、短信内容含屏蔽关键字；-22、发送为空；
		System.out.println("结束发送短信返回值:" + inputLine);
		return inputLine;

	};

	// 获取报检单位
	@RequestMapping(value = "getOrg")
	@ResponseBody
	public HashMap<String, Object> getOrg(HttpServletRequest request)
			throws Exception {
		String code = request.getParameter("code");

		return inspectionService.getOrg(code);
	}

	// 获取报检单位
	@RequestMapping(value = "send_data")
	@ResponseBody
	public void send_data(HttpServletRequest request) throws Exception {

		inspectionService.send_data();
	}

	// 查询检验参数信息
	@RequestMapping(value = "getShowInfo")
	@ResponseBody
	public Map getShowInfo(HttpServletRequest request) throws Exception {

		// Map<String, Object> outMap = new HashMap<String, Object>();
		//
		// outMap = inspectionService.getShowInfo();
		//
		//			
		//
		// ModelAndView mav = new ModelAndView("app/flow/set_report_item",
		// outMap);
		// return mav;

		Map<String, Object> outMap = new HashMap<String, Object>();

		outMap = inspectionService.getShowInfo();

		// request.setAttribute("reportItems", outMap.get("reportItems"));
		// request.setAttribute("mapManName", outMap.get("mapManName"));
		// request.setAttribute("mapManId", outMap.get("mapManId"));
		// request.setAttribute("report_item", outMap.get("report_item"));

		return outMap;

	}
	
	
				// 查询罐车业务流程信息
				@RequestMapping(value = "getFlowTanker")
				@ResponseBody
				public ModelAndView getFlowTanker(HttpServletRequest request)
						throws Exception {

					Map<String, Object> map = new HashMap<String, Object>();

					map = inspectionService.getFlowTanker();

					ModelAndView mav = new ModelAndView("app/tanker/tanker_misson_index", map);

					return mav;

				}
				
				
				/**
				 * 根据设备统计
				 * 
				 */
				@RequestMapping(value = "deviceCountByTanker")
				@ResponseBody
				public HashMap<String, Object> deviceCountByTanker(HttpServletRequest request) {
					HashMap<String, Object> wrapper = new HashMap<String, Object>();
					try {
						
						Date startD = null;
						Date endD = null;
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate"); 
						SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
						if(StringUtil.isEmpty(startDate)){
							/*Calendar cd=Calendar.getInstance();
							int year=cd.get(Calendar.YEAR);
							startDate=year+"-01-01";*/
						}else{
							startD = format.parse(startDate);
						}
						if(StringUtil.isEmpty(endDate)){
							endDate=DateUtil.getCurrentDateTime();
						}else{
							endD = format.parse(endDate);
						}
						wrapper.put("data", inspectionService.deviceCountByTanker(startD,endD));
						wrapper.put("success", true);
					} catch (Exception e) {
						log.error(e.getMessage());
						wrapper.put("success", false);
						wrapper.put("message", e.getMessage());
						e.printStackTrace();
					}
			         return wrapper;
				}
				
				/**
				 * 根据介质统计
				 * 
				 */
				@RequestMapping(value = "deviceCountByMedia")
				@ResponseBody
				public HashMap<String, Object> deviceCountByMedia(HttpServletRequest request) {
					HashMap<String, Object> wrapper = new HashMap<String, Object>();
					try {
						
						Date startD = null;
						Date endD = null;
						String startDate = request.getParameter("startDate");
						String endDate = request.getParameter("endDate");
						SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
						if(StringUtil.isEmpty(startDate)){
							/*Calendar cd=Calendar.getInstance();
							int year=cd.get(Calendar.YEAR);
							startDate=year+"-01-01";*/
						}else{
							startD = format.parse(startDate);
						}
						if(StringUtil.isEmpty(endDate)){
							endDate=DateUtil.getCurrentDateTime();
						}else{
							endD = format.parse(endDate);
						}
						wrapper.put("data", inspectionService.deviceCountByMedia(startD,endD));
						wrapper.put("success", true);
					} catch (Exception e) {
						log.error(e.getMessage());
						wrapper.put("success", false);
						wrapper.put("message", e.getMessage());
						e.printStackTrace();
					}
			         return wrapper;
				}
				
// 查询微信访问数据
@RequestMapping(value = "getWeixinInfo")
@ResponseBody
public Map getWeixinInfo(HttpServletRequest request) throws Exception {

		Map<String, Object> outMap = new HashMap<String, Object>();

		outMap = inspectionService.getWeixinInfo();


		return outMap;

	}
	
	/**
	 * 验证报告是否领取
	 * @param fkInspectionInfoId
	 * @return
	 */
	@RequestMapping("queryReportDraw")
	@ResponseBody
	public HashMap<String, Object> queryReportDraw(String fkInspectionInfoId){
		
		List<ReportDraw> list= inspectionService.queryReportDraw(fkInspectionInfoId);
		return JsonWrapper.successWrapper(list);
	}

	/**
	 * 报告领取签名
	 * 
	 * @param request
	 * @param reportDraw
	 * @throws Exception
	 */
	@RequestMapping(value = "mobileReportSign")
	@ResponseBody
	public HashMap<String, Object> mobileReportSign(HttpServletRequest request,@RequestBody Map<String, Object> map) throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try{
//			List<ReportDraw> list = inspectionService.mobileReportDrawSaveSgnFile(request,map);
			//报告领取签名保存
			ReportDrawSign reportDrawSign=inspectionService.mobileReportDrawSignSaveSgnFile(request, map);
			wrapper.put("success", true);
			wrapper.put("data", reportDrawSign);
		} catch (Exception e) {
			e.printStackTrace();
			wrapper.put("success", false);
			wrapper.put("msg", "保存签名信息失败，请重试！");
		}
		return wrapper;
	}
	
	/**
	 * 验证报告是否领取
	 * @param fkInspectionInfoId
	 * @return
	 */
	@RequestMapping("queryReportDrawCert")
	@ResponseBody
	public HashMap<String, Object> queryReportDrawCert(String fkInspectionInfoId){
		List<ReportDraw> list= inspectionService.queryReportDraw(fkInspectionInfoId.split(",")[0].toString());
		Map<String, Object> map=new HashMap<String, Object>();
		
		if(list.size()>0){
			String signFile=list.get(0).getSign_file();
			if(StringUtil.isEmpty(signFile)){
				return JsonWrapper.successWrapper("该报告未签字确认");
			}else{
				return JsonWrapper.successWrapper();
			}
		}else{
			return JsonWrapper.failureWrapperMsg("该报告未保存领取信息，请先保存再打印！");
		}
	}	
	
	/**
	 * 返回报告领取打印数据
	 * @param request
	 * @param fkInspectionInfoId
	 * @return
	 */
	@RequestMapping("reportDrawCert")
	@ResponseBody
	public HashMap<String, Object> reportDrawCert(HttpServletRequest request,String fkInspectionInfoId){
		String strId="(";
		for(int i=0;i<fkInspectionInfoId.split(",").length;i++){
			String fkInfoId=fkInspectionInfoId.split(",")[i].toString();
			if((fkInspectionInfoId.split(",").length-1)==i){
				strId=strId+"'"+fkInfoId+"')";
			}else{
				strId=strId+"'"+fkInfoId+"',";
			}
		}
		List<Map<String, Object>> list= inspectionService.queryReportDrawAll(strId);
		String ids="";
		for(int i=0;i<list.size();i++){
			String id=list.get(i).get("ID").toString();
			if(i==(list.size()-1)){
				ids=ids+id;
			}else if(i==0){
				ids=id+",";
			}else{
				ids=ids+id+",";
			}
		}
		HashMap<String, Object> wrapper= reportDrawAction.getPrintContent(request, ids); 
		return wrapper;
	}
	
	/**
	 * 获取报告领取签名信息
	 * @param fkInspectionInfoId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryReportDoc")
	@ResponseBody
	public HashMap<String,Object> queryReportDoc(String fkInspectionInfoId) throws Exception{
		List<ReportDraw> list= inspectionService.queryReportDraw(fkInspectionInfoId.split(",")[0].toString());
		String id=list.get(0).getId();
		HashMap<String, Object> map=reportDrawAction.reportDrawDetail1(id);
		return map;
	}
	
	/**
	 * 验证报告是否领取
	 * @param fkInspectionInfoId
	 * @return
	 */
	@RequestMapping("queryReportDrawCert1")
	@ResponseBody
	public HashMap<String, Object> queryReportDrawCert1(String fkInspectionInfoId){
		List<ReportDraw> list= inspectionService.queryReportDraw(fkInspectionInfoId.split(",")[0].toString());
		Map<String, Object> map=new HashMap<String, Object>();
		if(list.size()>0){
			String signFile=list.get(0).getSign_file();
			if(StringUtil.isEmpty(signFile)){
				return JsonWrapper.successWrapper("该报告未签字确认");
			}else{
				return JsonWrapper.successWrapper();
			}
		}else{
			return JsonWrapper.failureWrapperMsg("亲，该报告未保存领取信息，请先保存，耐心等待平板签名！");
		}
	}
	
	/**
	 * 验证报告领取单是否保存
	 * @param fkInspectionInfoId
	 * @return
	 */
	@RequestMapping("isNotSaveReportDrawSign")
	@ResponseBody
	public HashMap<String, Object> isNotSaveReportDrawSign(String fkInspectionInfoId){
		List<ReportDraw> list= inspectionService.queryReportDraw(fkInspectionInfoId.split(",")[0].toString());
		//Map<String, Object> map=new HashMap<String, Object>();
		if(list.size()>0){
			return JsonWrapper.failureWrapperMsg("亲，你已保存报告领取单，不能重新推送！");
		}else{
			return JsonWrapper.successWrapper();
		}
	}
	
	
	/**
	 * 
	 */
	@RequestMapping(value = "add_auditDate")
	@ResponseBody
	public Map<String,Object> addAuditDate(HttpServletRequest request){
		String type = request.getParameter("type");
		String infoId = request.getParameter("id");
		if("04".equals(type)){//审核添加审核时间
			inspectionService.addExamineDate(infoId);
		}else if("05".equals(type)){//签发添加签发时间
			inspectionService.addIssueDate(infoId);
		}
		return JsonWrapper.successWrapper(11);
	}

	@Override
	public void onLoginFail(String arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoginSuccess(CurrentSessionUser arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLogout(CurrentSessionUser arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
