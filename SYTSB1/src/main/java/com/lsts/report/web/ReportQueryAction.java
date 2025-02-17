package com.lsts.report.web;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.rbac.bean.Org;
import com.khnt.rbac.impl.bean.Employee;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rbac.impl.manager.OrgManagerImpl;
import com.khnt.rtbox.print.service.PrintPdfTaskManager;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.DateUtil;
import com.khnt.utils.FileUtil;
import com.khnt.utils.StringUtil;
import com.lsts.ImageTool;
import com.lsts.common.service.CodeTablesService;
import com.lsts.constant.Constant;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.service.DeviceService;
import com.lsts.employee.service.EmployeePrinterService;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.bean.InspectionZZJDInfo;
import com.lsts.inspection.bean.ReportItemValue;
import com.lsts.inspection.service.InspectionInfoService;
import com.lsts.inspection.service.InspectionService;
import com.lsts.inspection.service.InspectionZZJDInfoService;
import com.lsts.inspection.service.ReportItemValueService;
import com.lsts.inspection.service.ReportPageCheckInfoService;
import com.lsts.log.service.SysLogService;
import com.lsts.report.bean.Report;
import com.lsts.report.bean.ReportDraw;
import com.lsts.report.bean.ReportItem;
import com.lsts.report.bean.ReportPrintLog;
import com.lsts.report.service.ReportDrawService;
import com.lsts.report.service.ReportItemService;
import com.lsts.report.service.ReportPrintLogService;
import com.lsts.report.service.ReportService;
import com.swetake.util.Qrcode;

/**
 * 检验报告业务控制器
 * 
 * @ClassName InspectionReportAction
 * @JDK 1.7
 * @author GaoYa
 * @date 2014-02-25 上午10:35:00
 */
@Controller
@RequestMapping("report/query")
public class ReportQueryAction extends
		SpringSupportAction<InspectionInfo, InspectionInfoService> {

	@Autowired
	private InspectionInfoService infoService;
	@Autowired
	private ReportDrawService reportDrawService;
	@Autowired
	private ReportItemValueService reportItemValueService;
	@Autowired
	
	private ReportService reportService;
	@Autowired
	private EmployeePrinterService employeePrinterService;
	@Autowired
	private ReportItemService reportItemService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private ImageTool imageTool;
	@Autowired
	private	SysLogService logService;
	@Autowired
	private	ReportPageCheckInfoService reportPageCheckInfoService;
	@Autowired
	private InspectionZZJDInfoService inspectionZZJDInfoService;
	@Autowired
	private InspectionService inspectionService;
	@Autowired
	private CodeTablesService codeTablesService;
	@Autowired
	private OrgManagerImpl orgManager;
	@Autowired
	private ReportPrintLogService reportPrintLogService;
	@Autowired
	private PrintPdfTaskManager printPdfTaskManager;
	
	/**
	 * 详情
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "detail")
	@ResponseBody
	public HashMap<String, Object> detail(HttpServletRequest request, String id)
			throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		ReportDraw reportDraw = reportDrawService.queryByInspectionInfoId(id); // 获取报告领取记录
		String flag = "";
		if (reportDraw == null) {
			flag = "4";
		} else {
			flag = "3";
		}
		wrapper.put("success", true);
		wrapper.put("data", infoService.get(id));
		wrapper.put("flag", flag);
		wrapper.put("reportDraw", reportDraw != null ? reportDraw.to_Map()
				: null);
		return wrapper;
	}

	/**
	 * 查看报告
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @param report_id --
	 *            检验报告ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "showReport")
	public String showReport(HttpServletRequest request, String id,
			String report_id) throws Exception {
		getPrintData(request, id, report_id);
		// InspectionInfo inspectionInfo = infoService.get(id); // 报检业务
		// Report report = reportService.get(report_id);
		// List<ReportItemValue> reportItemValueList = reportItemValueService
		// .queryByInspectionInfoId(id, report_id); // 报告检验项目
		// request.getSession().setAttribute("inspectionInfo", inspectionInfo);
		// request.getSession().setAttribute("report", report);
		// request.getSession().setAttribute("reportItemValueList",
		// reportItemValueList);
		return "app/query/report_query_showreport";
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
	@RequestMapping(value = "showPrint")
	public String showPrint(HttpServletRequest request, String id)
			throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String userId = user.getSysUser().getId();
		String printType = request.getParameter("printType");	// 打印方式（1：补打 else 流程中打印）
		
		String print_type = request.getParameter("print_type");
		String print_count = request.getParameter("print_count");
		String print_remark = request.getParameter("print_remark");
		if(StringUtil.isNotEmpty(print_remark)){
			print_remark = URLDecoder.decode(print_remark, "UTF-8");  
		}
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		if (StringUtil.isNotEmpty(printType) && "1".equals(printType)) {
			mapList = infoService.queryInfo(id, print_count);
		}else{
			mapList = infoService.queryInfo(id, userId, print_count);
		}
		request.getSession().setAttribute("print_type", print_type);
		request.getSession().setAttribute("print_remark", print_remark);
		request.getSession().setAttribute("mapList", mapList);
		return "app/query/report_print_left";
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "showExport")
	public String showExport(HttpServletRequest request, String id)
			throws Exception {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		List<InspectionInfo> infolist = infoService.getInfoListByIds(id);
		for(InspectionInfo info : infolist) {
			Report report = reportService.get(info.getReport_type());
			if("2".equals(report.getReportType()) && StringUtil.isEmpty(info.getPdfExportAtt())) {
				printPdfTaskManager.saveTask(info.getId(), request);
			}
		}
		mapList = infoService.queryInfo(id, null, null);
		request.getSession().setAttribute("mapList", mapList);
		return "app/flow/export/report_export_left";
	}
	
	/**
	 * 报告导出show
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @return
	 * @throws Exception
	 * @author GaoYa
	 * @date 2016-11-21 下午16:55:00
	 */
	@RequestMapping(value = "showExport2")
	public String showExport2(HttpServletRequest request, String id)
			throws Exception {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		mapList = infoService.queryInfo(id, null, null);
		request.getSession().setAttribute("mapList", mapList);
		return "app/query/report_export_left";
	}
	
	
	/**
	 * 打印报告
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @param report_id --
	 *            检验报告ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "doPrint")
	public String doPrint(HttpServletRequest request, String id,
			String report_id) {
		try {
			getPrintData(request, id, report_id);
		} catch (Exception e) {
			log.error("获取打印数据出错：" + e.getMessage());
			e.printStackTrace();
		}
		return "app/query/report_print";
	}

	/**
	 * 打印导出
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @param report_id --
	 *            检验报告ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "doExport")
	public String doExport(HttpServletRequest request, String id,
			String report_id) {
		String returnUrl = "";
		try {
			Report report = reportService.get(report_id);
			if("2".equals(report.getReportType())) {
				request.setAttribute("inspectionInfo", infoService.get(id));
				request.setAttribute("printout", request.getParameter("printout"));
				returnUrl =  "app/flow/export/report_export_rtbox";
			}else {
				getPrintData(request, id, report_id);
				returnUrl = "app/flow/export/report_export";
			}
		} catch (Exception e) {
			log.error("获取打印数据出错：" + e.getMessage());
			e.printStackTrace();
		}
		return returnUrl;
	}
	
	/**
	 * 打印导出
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @param report_id --
	 *            检验报告ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "doExportNoz")
	public String doExportNoz(HttpServletRequest request, String id,
			String report_id) {
		try {
			getPrintData(request, id, report_id);
		} catch (Exception e) {
			log.error("获取打印数据出错：" + e.getMessage());
			e.printStackTrace();
		}
		return "app/flow/export/report_export2";
	}
	
	
	/**
	 * 报告导出
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @param report_id --
	 *            检验报告ID
	 * @return
	 * @throws Exception
	 * @author GaoYa
	 * @date 2016-11-21 下午16:56:00
	 */
	@RequestMapping(value = "doExport2")
	public String doExport2(HttpServletRequest request, String id,
			String report_id) {
		try {
			getPrintData(request, id, report_id);
		} catch (Exception e) {
			log.error("获取报告数据出错：" + e.getMessage());
			e.printStackTrace();
		}
		return "app/query/report_export";
	}
	
	/**
	 * 打印标签show
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "showPrintTags")
	public String showPrintTags(HttpServletRequest request, String id)
			throws Exception {
		List<Map<String, Object>> mapList = infoService.queryInfo(id, null, null);
		request.getSession().setAttribute("mapList", mapList);
		String flag =request.getParameter("flag"); 
		request.setAttribute("flag",flag );
//		String flag = request.getParameter("flag");
		return "app/query/report_print_left_tags";
	}

	/**
	 * 打印信息表
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "showPrintMessage")
	public String showPrintMessage(HttpServletRequest request, String id,String report_type)
			throws Exception {
		List<Map<String, Object>> mapList = infoService.queryMessage(id);
		
		String modle = getModle(report_type);
		
		request.getSession().setAttribute("modle", modle);
		
		request.getSession().setAttribute("mapList", mapList);
		return "app/query/report_print_left_message";
	}
	

	
	
	/**
	 * 打印标签（合格证）
	 * 
	 * @param request
	 * @param id --
	 *            设备ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "doPrintTags")
	public String doPrintTags(HttpServletRequest request, String id,
			String report_id, String device_id) {
		try {
			getPrintTagsData(request, id, report_id, device_id);	// 1、获取标签打印数据
			infoService.updateInfo(id);	// 2、标记标签打印状态（更新为“已打印”）
		} catch (Exception e) {
			log.error("获取打印数据出错：" + e.getMessage());
			e.printStackTrace();
		}
		String flag =request.getParameter("flag"); 
		request.setAttribute("flag",flag );
		System.out.println(request.getAttribute("flag"));
	
		return "app/query/report_print_tags";
	}
	
	/**
	 * 打印信息表
	 * 
	 * @param request
	 * @param id --
	 *            设备ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "doPrintMessage")
	public String doPrintMessage(HttpServletRequest request, String id) {
		try {
			getPrintMessageData(request, id);
		} catch (Exception e) {
			log.error("获取打印数据出错：" + e.getMessage());
			e.printStackTrace();
		}
		return "app/query/report_print_message";
	}


	/**
	 * 保存报告打印记录
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @param report_id --
	 *            检验报告ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "insertPrintRecord")
	public String insertPrintRecord(HttpServletRequest request, String id,
			String report_id, String isLast, String op_type) {
		CurrentSessionUser curUser = this.getCurrentUser(); // 获取当前用户登录信息
		try {
			// 1、获取报告打印数据，重新加载打印页面时显示
			getPrintData(request, id, report_id);
			/*
			WebApplicationContext webApplicationContext = ContextLoader
					.getCurrentWebApplicationContext();
			SysLogService sysLogService = (SysLogService) webApplicationContext
					.getBean("sysLogService");
			SysLog sysLog = new SysLog();
			sysLog.setOp_user_id(curUser.getSysUser().getId());
			sysLog.setOp_user_name(curUser.getSysUser().getName());
			sysLogService.insertSysLog(sysLog);*/
			
			// 2、保存报告打印记录
			String op_action = "";
			String op_remark = "";
			String print_type = request.getParameter("print_type");		// 报告打印类型（0：报告打印 1：补办打印 2：纠错打印 3：换证打印 4：其他打印 ）
			String print_count = request.getParameter("print_count");	// 报告打印份数
			String print_remark = request.getParameter("print_remark");	// 备注
			if(StringUtil.isEmpty(print_type)){
				op_action = Constant.SYS_OP_ACTION_PRINT;
			}else{
				if("null".equals(print_type)){
					print_type = "0";
					op_action = Constant.SYS_OP_ACTION_PRINT;
				}else{
					op_action = codeTablesService.getValueByCode("REPORT_PRINT_TYPE", print_type);
				}
			}
			
			if(StringUtil.isEmpty(print_remark)){
				op_remark = Constant.SYS_OP_ACTION_PRINT;
			}else{
				if("null".equals(print_remark)){
					op_remark = Constant.SYS_OP_ACTION_PRINT;
				}else{
					op_remark = URLDecoder.decode(print_remark, "UTF-8");  
				}
			}
			
			InspectionInfo inspectionInfo = infoService.get(id); // 报告业务信息
			if(inspectionInfo!=null){
				ReportPrintLog reportPrintLog = new ReportPrintLog();
				reportPrintLog.setId(null);
				reportPrintLog.setInfo_id(id);
				reportPrintLog.setReport_sn(inspectionInfo.getReport_sn());
				
				if(StringUtil.isEmpty(print_count)){
					if(StringUtil.isNotEmpty(inspectionInfo.getReport_type())){
						Report report = reportService.get(inspectionInfo.getReport_type());
						if(report != null){
							print_count = String.valueOf(report.getPrintcopies());
						}
					}else{
						print_count = "1";
					}
				}else{
					if("null".equals(print_count) || "0".equals(print_count)){
						print_count = "1";
					}
				}
				reportPrintLog.setPrint_count(Integer.parseInt(print_count));
				
				reportPrintLog.setCom_name(inspectionInfo.getReport_com_name());
				reportPrintLog.setOrg_id(inspectionInfo.getCheck_unit_id());
				if (StringUtil.isNotEmpty(inspectionInfo.getCheck_unit_id())) {
					Org org = orgManager.get(inspectionInfo
							.getCheck_unit_id());
					reportPrintLog.setOrg_name(StringUtil
							.isNotEmpty(org.getOrgName()) ? org
							.getOrgName() : ""); 	// 检验部门名称
				} else {
					reportPrintLog.setOrg_name("");	// 检验部门名称
				}
				reportPrintLog.setPrint_type(print_type);
				reportPrintLog.setPrint_type_name(op_action);
				reportPrintLog.setRemark(op_remark);
				reportPrintLog.setPrint_user_id(curUser.getId());
				reportPrintLog.setPrint_user_name(curUser.getName());
				reportPrintLog.setPrint_time(new Date());
				reportPrintLogService.save(reportPrintLog);
			}
			
			// 3、记录系统日志
			logService.setLogs(id, op_action, op_remark, curUser.getId(), curUser.getName(), new Date(), request.getRemoteAddr());
		} catch (Exception e) {
			log.error("保存报告打印日志出错：" + e.getMessage());
			e.printStackTrace();
		}

		return "app/query/report_print";
	}
	
	// 报告补打记录打印日志
	@RequestMapping(value = "savePrintLog")
	@ResponseBody
	public synchronized HashMap<String, Object> savePrintLog(HttpServletRequest request, String id) throws Exception {
		CurrentSessionUser curUser = this.getCurrentUser(); // 获取当前用户登录信息
		try {
			// 1、保存报告打印记录
			String op_action = "";
			String op_remark = "";
			String print_type = request.getParameter("print_type");		// 报告打印类型（0：报告打印 1：补办打印 2：纠错打印 3：换证打印 4：其他打印 ）
			String print_count = request.getParameter("print_count");	// 报告打印份数
			String print_remark = request.getParameter("print_remark");	// 备注
			
			if(StringUtil.isEmpty(print_type)){
				op_action = Constant.SYS_OP_ACTION_PRINT;
			}else{
				if("null".equals(print_type)){
					op_action = Constant.SYS_OP_ACTION_PRINT;
				}else{
					op_action = codeTablesService.getValueByCode("REPORT_PRINT_TYPE", print_type);
				}
			}	
			if(StringUtil.isEmpty(print_remark)){
				op_remark = Constant.SYS_OP_ACTION_PRINT;
			}else{
				if("null".equals(print_remark)){
					op_remark = Constant.SYS_OP_ACTION_PRINT;
				}else{
					op_remark = URLDecoder.decode(print_remark, "UTF-8");  
				}
			}
			
			InspectionInfo inspectionInfo = infoService.get(id); // 报告业务信息
			if(inspectionInfo!=null){
				ReportPrintLog reportPrintLog = new ReportPrintLog();
				reportPrintLog.setId(null);
				reportPrintLog.setInfo_id(id);
				reportPrintLog.setReport_sn(inspectionInfo.getReport_sn());
				
				if(StringUtil.isEmpty(print_count)){
					if(StringUtil.isNotEmpty(inspectionInfo.getReport_type())){
						Report report = reportService.get(inspectionInfo.getReport_type());
						if(report != null){
							print_count = String.valueOf(report.getPrintcopies());
						}
					}else{
						print_count = "1";
					}
				}else{
					if("null".equals(print_count) || "0".equals(print_count)){
						print_count = "1";
					}
				}
				reportPrintLog.setPrint_count(Integer.parseInt(print_count));
				
				reportPrintLog.setCom_name(inspectionInfo.getReport_com_name());
				reportPrintLog.setOrg_id(inspectionInfo.getCheck_unit_id());
				if (StringUtil.isNotEmpty(inspectionInfo.getCheck_unit_id())) {
					Org org = orgManager.get(inspectionInfo
							.getCheck_unit_id());
					reportPrintLog.setOrg_name(StringUtil
							.isNotEmpty(org.getOrgName()) ? org
							.getOrgName() : ""); 	// 检验部门名称
				} else {
					reportPrintLog.setOrg_name("");	// 检验部门名称
				}
				reportPrintLog.setPrint_type(print_type);
				reportPrintLog.setPrint_type_name(op_action);
				reportPrintLog.setRemark(op_remark);
				reportPrintLog.setPrint_user_id(curUser.getId());
				reportPrintLog.setPrint_user_name(curUser.getName());
				reportPrintLog.setPrint_time(new Date());
				reportPrintLogService.save(reportPrintLog);
			}
			
			// 2、记录系统日志
			logService.setLogs(id, op_action, op_remark, curUser.getId(), curUser.getName(), new Date(), request.getRemoteAddr());

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("success", true);
			return map;
		} catch (Exception e) {
			return JsonWrapper.failureWrapperMsg("读取信息失败！");
		}
	}

	
	/**
	 * 保存报告导出记录
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @param report_id --
	 *            检验报告ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "insertExportRecord")
	public String insertExportRecord(HttpServletRequest request, String id,
			String report_id, String isLast, String op_type) {
		CurrentSessionUser curUser = this.getCurrentUser(); // 获取当前用户登录信息
		try {
			getPrintData(request, id, report_id);
			logService.setLogs(id, Constant.SYS_OP_ACTION_EXPORT_PDF, Constant.SYS_OP_ACTION_EXPORT_PDF, curUser.getId(), curUser.getName(), new Date(), request.getRemoteAddr());
		} catch (Exception e) {
			log.error("保存报告打印日志出错：" + e.getMessage());
			e.printStackTrace();
		}

		return "app/flow/export/report_export";
	}
	
	/**
	 * 保存报告导出记录
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @param report_id --
	 *            检验报告ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "insertExportRecord2")
	public String insertExportRecord2(HttpServletRequest request, String id,
			String report_id, String isLast, String op_type) {
		CurrentSessionUser curUser = this.getCurrentUser(); // 获取当前用户登录信息
		try {
			getPrintData(request, id, report_id);
			logService.setLogs(id, Constant.SYS_OP_ACTION_EXPORT_PDF, Constant.SYS_OP_ACTION_EXPORT_PDF, curUser.getId(), curUser.getName(), new Date(), request.getRemoteAddr());
		} catch (Exception e) {
			log.error("保存报告导出日志出错：" + e.getMessage());
			e.printStackTrace();
		}

		return "app/query/report_export";
	}
	
	/**
	 * 获取报告打印数据
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @param report_id --
	 *            检验报告ID
	 * @return
	 * @throws Exception
	 */
	private void getPrintMessageData(HttpServletRequest request, String id
			) throws Exception {
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User uu = (User)curUser.getSysUser();
		Employee emp = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
		// 报检业务信息
		InspectionInfo inspectionInfo = infoService.get(id); 
		// 设备信息
		DeviceDocument device = deviceService.get(inspectionInfo.getFk_tsjc_device_document_id());
		List<ReportItemValue> reportItemValueList = reportItemValueService
				.queryByitemValue(id); // 报告检验项目
		String printer_name = employeePrinterService.queryPrinterName(emp.getId(), Constant.PRINTER_TYPE_R);

		request.getSession().setAttribute("printer_name", printer_name); 		// 人员打印机名称
		request.getSession().setAttribute("inspectionInfo", inspectionInfo); 	// 报告业务信息
		request.getSession().setAttribute("device", device); 		// 设备信息
		request.getSession().setAttribute("reportItemValueList",	// 检验项目表信息
				reportItemValueList);
		

	}
	
	/**
	 * 获取信息表模板文件
	
	 * @return
	 * @throws Exception
	 */
	private String getModle( String report_type
			) throws Exception {
		
		Report report = reportService.get(report_type);
	
			return report.getInfo_tmp_name();
	}
	
	
	/**
	 * 获取信息表
	 * 
	 * @param request
	 * @param id --
	 *            报检业务ID
	 * @param report_id --
	 *            检验报告ID
	 * @return
	 * @throws Exception
	 */
	private void getPrintData(HttpServletRequest request, String id,
			String report_id) throws Exception {
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User uu = (User)curUser.getSysUser();
		Employee emp = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
		InspectionInfo inspectionInfo = infoService.get(id); // 报检业务
		if(inspectionInfo!=null){
			InspectionZZJDInfo inspectionZZJDInfo = inspectionZZJDInfoService
					.getByInfoId(id); // 2、获取监督检验明细数据
			if(StringUtil.isEmpty(report_id)){
				report_id = inspectionInfo.getReport_type();
			}else{
				if("null".equals(report_id)){
					report_id = inspectionInfo.getReport_type();
				}
			}
			DeviceDocument deviceDocument = deviceService.get(inspectionInfo.getFk_tsjc_device_document_id());
			Report report = reportService.get(report_id);
			List<ReportItemValue> reportItemValueList = reportItemValueService
					.queryByInspectionInfoId(id, report_id); // 报告检验项目
			List<ReportItem> reportItemList = reportItemService
					.queryByReportId(report_id); // 报告项目

			String printer_name = employeePrinterService.queryPrinterName(emp.getId(), Constant.PRINTER_TYPE_R);
			// 检验人员电子签名
			request.setAttribute("check_op_img",
					imageTool.getEmployeeImg(inspectionInfo.getCheck_op_id()));
			// 审核人员电子签名
			request.setAttribute("examine_op_img",
					imageTool.getEmployeeImg(inspectionInfo.getExamine_id()));
			// 签发（批准）人员电子签名
			request.setAttribute("issue_op_img",
					imageTool.getEmployeeImg(inspectionInfo.getIssue_id()));
			// 编制（录入）人员电子签名
			request.setAttribute("enter_op_img",
					imageTool.getEmployeeImg(inspectionInfo.getEnter_op_id()));
			
			// 报告图片信息
			request.setAttribute("PICTURE",reportItemValueService.getPic(id));
			// 报告分页单独检验审核信息（含电子签名）
			request.setAttribute("PAGE_CHECK_IMAGES",reportPageCheckInfoService.getReportPageInfo(id, report_id));
			
			Map<String,Object> zzjdInfoMap = new HashMap<String,Object>();
			if(inspectionZZJDInfo!=null){
				zzjdInfoMap = inspectionService.beanToMap(inspectionZZJDInfo);
			}

			String print_type = request.getParameter("print_type");
			String print_count = request.getParameter("print_count");
			String print_remark = request.getParameter("print_remark");
			if(StringUtil.isEmpty(print_type)){
				print_type = "打印报告";
			}else{
				if("null".equals(print_type)){
					print_type = "打印报告";
				}
			}
			if(StringUtil.isEmpty(print_remark)){
				print_remark = "打印报告";
			}else{
				if("null".equals(print_remark)){
					print_remark = "打印报告";
				}
			}
			if(StringUtil.isEmpty(print_count)){
				if(StringUtil.isNotEmpty(inspectionInfo.getReport_type())){
					if(report != null){
						print_count = String.valueOf(report.getPrintcopies());
					}
				}else{
					print_count = "1";
				}
			}else{
				if("null".equals(print_count) || "0".equals(print_count)){
					print_count = "1";
				}
			}
			request.getSession().setAttribute("report_id", report_id); // 报告ID
			request.getSession().setAttribute("deviceDocument", deviceDocument); // 设备信息
			request.getSession().setAttribute("inspectionInfo", inspectionInfo); // 报检信息
			request.getSession().setAttribute("INSPECTIONZZJDINFO",
					zzjdInfoMap); // 监督检验明细数据
			request.getSession().setAttribute("report", report); // 报告信息
			request.getSession().setAttribute("reportItemValueList", // 检查项目
					reportItemValueList);
			request.getSession().setAttribute("reportItemList", // 报告项目
					reportItemList);
			request.getSession().setAttribute("printer_name", printer_name); // 人员打印机名称
			request.getSession().setAttribute("print_type", print_type);
			request.getSession().setAttribute("print_count", print_count);
			request.getSession().setAttribute("print_remark", print_remark);
			
			if (inspectionInfo.getAdvance_time() != null) {
				String advance_time = DateUtil.getDateTime("yyyy-MM-dd",
						inspectionInfo.getAdvance_time());
				request.getSession().setAttribute("JGHZH",
						infoService.queryJGHZH(advance_time)); // 获取检验机构核准证号
			}
		}
	}
	
	/**
	 * 获取标签打印数据
	 * 
	 * @param request
	 * @param device_id --
	 *            设备ID
	 * @param report_id --
	 *            报告ID
	 * @param id --
	 *            业务ID
	 * @return
	 * @throws Exception
	 */
	private void getPrintTagsData(HttpServletRequest request, String id,
			String report_id, String device_id) throws Exception {
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User uu = (User)curUser.getSysUser();
		Employee emp = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
		DeviceDocument deviceDocument = deviceService.get(device_id);
		InspectionInfo inspectionInfo = infoService.get(id);
		List<ReportItemValue> reportItemValueList = reportItemValueService
				.queryByInspectionInfoId(id, report_id); // 报告检验项目
		String printer_name = employeePrinterService.queryPrinterName(emp.getId(), Constant.PRINTER_TYPE_T);
		request.getSession().setAttribute("deviceDocument", deviceDocument); // 报检信息
		request.getSession().setAttribute("reportItemValueList",
				reportItemValueList); // 报告项目信息（含电梯参数信息）
		request.getSession().setAttribute("printer_name", printer_name); 	// 人员打印机名称
		request.getSession().setAttribute("inspection_info_id", id);		// 业务信息ID
		request.getSession().setAttribute("inspectionInfo", inspectionInfo);// 业务信息
		request.getSession().setAttribute("report_id", report_id);		// 报告类型ID
	}

	/**
	 * 保存标签打印记录
	 * 
	 * @param request
	 * @param id --
	 *            设备ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "insertPrintTagsRecord")
	public String insertPrintTagsRecord(HttpServletRequest request, String id,
			String report_id, String device_id,String isLast, String op_type) {
		CurrentSessionUser curUser = this.getCurrentUser(); // 获取当前用户登录信息
		try {
			getPrintTagsData(request, id, report_id, device_id);
			logService.setLogs(id, Constant.SYS_OP_ACTION_PRINT_TAGS, Constant.SYS_OP_ACTION_PRINT_TAGS, curUser.getId(), curUser.getName(), new Date(), request.getRemoteAddr());
		} catch (Exception e) {
			log.error("保存标签打印日志出错：" + e.getMessage());
			e.printStackTrace();
		}
		String flag =request.getParameter("flag"); 
		request.setAttribute("flag",flag );
		return "app/query/report_print_tags";
	}
	
	/**
	 * 查询是否有模版
	 * 
	 * @param request
	 * @param report_type --
	 *            报告ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "queryModle")
	@ResponseBody
	public HashMap<String, Object> queryModle(HttpServletRequest request)
			throws Exception {
		String report_id = request.getParameter("report_type");
		HashMap map = new HashMap();
		if(StringUtil.isNotEmpty(report_id)){
			Report report = reportService.get(report_id);
			if(report.getInfo_tmp_name()==null||report.getInfo_tmp_name().equals("")){
				map.put("success", false);
			}else{
				map.put("success", true);
			}
		}else{
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 生成使用标志二维码（会生成临时文件）
	 * 
	 * Zxing
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "showTwoDim")
	public void showTwoDim(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			String filePath = null;
			String id  = request.getParameter("id");
			String device_registration_code  = request.getParameter("device_registration_code");
			if(StringUtil.isEmpty(device_registration_code)) {
				device_registration_code = deviceService.get(id).getDevice_registration_code();
			}
			String device_sort = deviceService.get(id).getDevice_sort();
			filePath = imageTool.showTwoDim(device_registration_code,device_sort,request);
			System.out.println("-----------------"+filePath);
			FileUtil.download(response, filePath, "", "application/octet-stream");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 生成使用标志二维码（直接以流传到页面）
	 * 
	 * Zxing
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "showTwoDimStream")
	public void showTwoDimStream(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			String id  = request.getParameter("id");
			String report_sn  = request.getParameter("report_sn");
			String device_sort = deviceService.get(id).getDevice_sort();
			imageTool.showTwoDimStream(report_sn,device_sort,request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 生成使用标志二维码
	 * 
	 * Qrcode
	 * @param content
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "showTwoDimNew")
	public void encoderQRCoder(String content, HttpServletResponse response,HttpServletRequest request) { 
		try {  
			int width=160;
		    int height=160;
			String id  = request.getParameter("id");
			String device_registration_code  = request.getParameter("device_registration_code");
			if(StringUtil.isEmpty(device_registration_code)) {
				device_registration_code = deviceService.get(id).getDevice_registration_code();
			}
			String device_sort = deviceService.get(id).getDevice_sort();
			if(device_sort!=null&&(device_sort.substring(0,1).equals("6")||device_sort.substring(0,1).equals("9"))){
		    	width = 715; 
		        height = 715; 
		    }
			
			StringBuffer params = new StringBuffer();  
			params.append("http://m.scsei.org.cn/?action=queryreport&searchvalue="+device_registration_code+"");
            Qrcode handler = new Qrcode();  
            handler.setQrcodeErrorCorrect('L');  // 纠错级别（L 7%、M 15%、Q 25%、H 30%）和版本有关    
            handler.setQrcodeEncodeMode('B');  
            handler.setQrcodeVersion(7);	// 设置Qrcode包的版本  
            System.out.println(params);  
            byte[] contentBytes = params.toString().getBytes("UTF-8");  // 字符集 
            BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
            //绘图   
            Graphics2D gs = bufImg.createGraphics();  
            gs.setBackground(Color.WHITE);  
            gs.clearRect(0, 0, width, height);  
            gs.setColor(Color.BLACK);  
              
            //设置下偏移量,如果不加偏移量，有时会导致出错。
            int pixoff = 2;  
            //输出内容：二维码  
            if(contentBytes.length > 0 && contentBytes.length < 120) {  
                boolean[][] codeOut = handler.calQrcode(contentBytes);  
                for(int i = 0; i < codeOut.length; i++) {  
                    for(int j = 0; j < codeOut.length; j++) {  
                        if(codeOut[j][i]) {  
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff,3, 3);  
                        }  
                    }  
                }  
            } else {  
                System.err.println("QRCode content bytes length = " + contentBytes.length + " not in [ 0,120 ]. ");  
            }  
            gs.dispose();  
            bufImg.flush();  
            //生成二维码QRCode图片  
            ImageIO.write(bufImg, "png", response.getOutputStream());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
	/**
	 * LODOP获取使用标志打印数据
	 * 
	 * @param request
	 * @param device_id --
	 *            设备ID
	 * @param report_id --
	 *            报告ID
	 * @param id --
	 *            业务ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getPrintTagsDataNew")
	@ResponseBody
	private HashMap<String, Object> getPrintTagsDataNew(HttpServletRequest request, String id,
			String report_id, String device_id) throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy年MM月dd日" );
		SimpleDateFormat sdf1 =   new SimpleDateFormat( "yyyy年MM月" );
		String device_sort_code=null;//设备种类
		String device_sort=null;//设备品种
		String company_name=null;//使用单位
		String internal_num=null;//单位内编号
		String device_code=null;//设备代码
		String device_qr_code=null;//3D二维码编号
		String registration_agencies=null;//登记机关
		String registration_num=null;//登记证编号
		String inspect_next_date=null;//下次检验日期
		String maintain_unit_name=null;//维保单位
		String security_tel=null;//应急救援电话
		String Year=null;//年份
		String Month=null;//月份
		
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User uu = (User)curUser.getSysUser();
		Employee emp = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
		DeviceDocument deviceDocument = deviceService.get(device_id);
		InspectionInfo inspectionInfo = null;
		String big_class = deviceDocument.getDevice_sort().substring(0, 1);	// 设备大类
		try {
			inspectionInfo = infoService.get(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ReportItemValue> reportItemValueList = reportItemValueService
				.queryByInspectionInfoId(id, report_id); // 报告检验项目
		
		for(ReportItemValue itemValue:reportItemValueList){
			if(itemValue.getItem_name().equals("DEVICE_SORT")){
				device_sort_code=itemValue.getItem_value();
				if(!device_sort_code.trim().matches("^\\d+$")){
					device_sort_code="";
				}
			}
			if(itemValue.getItem_name().equals("DEVICE_NAME")){
				device_sort=itemValue.getItem_value();
			}
			if(itemValue.getItem_name().equals("COM_NAME")){
				company_name=itemValue.getItem_value();
			}
			if(itemValue.getItem_name().equals("INTERNAL_NUM")){
				internal_num=itemValue.getItem_value();
			}
			if(itemValue.getItem_name().equals("DEVICE_QR_CODE")){
				device_qr_code=itemValue.getItem_value();
			}
			if(itemValue.getItem_name().equals("DEVICE_REGISTRATION_CODE")){
				device_code=itemValue.getItem_value();
			}
			if(itemValue.getItem_name().equals("DEVICE_CODE")){
				if(StringUtil.isEmpty(device_code)){
					if(StringUtil.isNotEmpty(itemValue.getItem_value())){
						device_code=itemValue.getItem_value();
					}
				}
			}
			if(big_class.equals("3")){
				if(StringUtil.isNotEmpty(deviceDocument.getDevice_area_code())){
					/*若是成都地区的电梯则取报告里面的登记证编号*/
					if(deviceDocument.getDevice_area_code().substring(0, 4).equals("5101")){
						if(itemValue.getItem_name().equals("REGISTRATION_NUM")){
							registration_num=itemValue.getItem_value();
						}
					}else if(deviceDocument.getDevice_area_code().substring(0, 4).equals("5401")){
						if(itemValue.getItem_name().equals("REGISTRATION_NUM")){
							registration_num=itemValue.getItem_value();
						}
					}else{/*若是其他地区的电梯则取报告里面的设备注册代码*/
						if(itemValue.getItem_name().equals("DEVICE_REGISTRATION_CODE")){
							registration_num=itemValue.getItem_value();
						}
					}
				}else{
					if(itemValue.getItem_name().equals("DEVICE_REGISTRATION_CODE")){
						registration_num=itemValue.getItem_value();
					}
				}
			}else{
				if(itemValue.getItem_name().equals("REGISTRATION_NUM")){
					registration_num=itemValue.getItem_value();
				}
			}
			if(big_class.equals("3")||big_class.equals("6")){
				/*电梯、游乐设施*/
				if(itemValue.getItem_name().equals("LAST_INSPECTION_DATE")){
					inspect_next_date=itemValue.getItem_value();
				}
			}else if(big_class.equals("9")){
				/*客运索道*/
				if(itemValue.getItem_name().equals("KY_LAST_INSPECTION_DATE")){
					inspect_next_date=itemValue.getItem_value();
				}
			} else {
				// 锅炉、压力容器
				if ("1".equals(big_class) || "2".equals(big_class)) {
					device_sort = deviceDocument.getDevice_sort();
					if (itemValue.getItem_name().equals("LAST_INSPECTION_DATE")) {
						inspect_next_date = itemValue.getItem_value();
					}
					if (StringUtil.isEmpty(inspect_next_date)) {
						if (itemValue.getItem_name().equals("LAST_INSPECTION_DATE_AUTO")
								|| itemValue.getItem_name().equals("INSPECT_NEXT_DATE")
								|| itemValue.getItem_name().equals("NEXT_INSPECT_DATE")) {
							if (StringUtil.isNotEmpty(itemValue.getItem_value())) {
								inspect_next_date = itemValue.getItem_value();
							}
						}
					}
					if (StringUtil.isEmpty(inspect_next_date)) {
						if (itemValue.getItem_name().equals("LAST_INSPECTION_DATE_Y")) {
							Year = itemValue.getItem_value();
						}
						if (itemValue.getItem_name().equals("LAST_INSPECTION_DATE_M")) {
							Month = itemValue.getItem_value();
						}
						if (StringUtil.isNotEmpty(Year) && StringUtil.isNotEmpty(Month)) {
							inspect_next_date = Year + "年" + Month + "月";
						}
					}
				} else {
					/* 起重机、厂车 */
					if (itemValue.getItem_name().equals("LAST_INSPECTION_DATE_Y")) {
						Year = itemValue.getItem_value();
					}
					if (itemValue.getItem_name().equals("LAST_INSPECTION_DATE_M")) {
						Month = itemValue.getItem_value();
					}
					if (StringUtil.isNotEmpty(Year) && StringUtil.isNotEmpty(Month)) {
						inspect_next_date = Year + "年" + Month + "月";
					}
				}
			}
			if(itemValue.getItem_name().equals("MAINTAIN_UNIT_NAME")){
				maintain_unit_name=itemValue.getItem_value();
			}
			if(itemValue.getItem_name().equals("SECURITY_TEL")){
				security_tel=itemValue.getItem_value();
			}
		}
		/*判断设备种类是否为空*/
		device_sort_code = StringUtil.isEmpty(device_sort_code) ? deviceDocument.getDevice_sort_code()
				: ((device_sort_code.replace(" ", "").equals("/") || device_sort_code.replace(" ", "").equals("无此项"))
						? deviceDocument.getDevice_sort_code() : device_sort_code);
		
		/*判断设备品种是否为空*/
		device_sort = StringUtil.isEmpty(device_sort) ? deviceDocument.getDevice_name()
				: ((device_sort.replace(" ", "").equals("/") || device_sort.replace(" ", "").equals("无此项"))
						? deviceDocument.getDevice_name() : device_sort);
		
		/*判断使用单位是否为空*/
		if(StringUtil.isEmpty(company_name)){
			if(StringUtil.isEmpty(inspectionInfo.getReport_com_name())){
				company_name=deviceDocument.getEnterInfo().getCom_name();
			}else{
				if(inspectionInfo.getReport_com_name().replace(" ", "").equals("/")||inspectionInfo.getReport_com_name().replace(" ", "").equals("无此项")){
					company_name=deviceDocument.getEnterInfo().getCom_name();
				}else{
					company_name=inspectionInfo.getReport_com_name();
				}
			}
		}else{
			if(company_name.replace(" ", "").equals("/") || company_name.replace(" ", "").equals("无此项")){
				if(StringUtil.isEmpty(inspectionInfo.getReport_com_name())){
					company_name=deviceDocument.getEnterInfo().getCom_name();
				}else{
					if(inspectionInfo.getReport_com_name().replace(" ", "").equals("/")||inspectionInfo.getReport_com_name().replace(" ", "").equals("无此项")){
						company_name=deviceDocument.getEnterInfo().getCom_name();
					}else{
						company_name=inspectionInfo.getReport_com_name();
					}
				}
			}
		}
		
		/*判断单位内编号是否为空*/
		if(StringUtil.isEmpty(internal_num)){
			if(StringUtil.isEmpty(inspectionInfo.getInternal_num())){
				internal_num=deviceDocument.getInternal_num();
			}else{
				if(inspectionInfo.getInternal_num().replace(" ", "").equals("/") || inspectionInfo.getInternal_num().replace(" ", "").equals("无此项")){
					internal_num=deviceDocument.getInternal_num();
				}else{
					internal_num=inspectionInfo.getInternal_num();
				}
			}
		}else{
			if(internal_num.replace(" ", "").equals("/") || internal_num.replace(" ", "").equals("无此项")){
				if(StringUtil.isEmpty(inspectionInfo.getInternal_num())){
					internal_num=deviceDocument.getInternal_num();
				}else{
					if(inspectionInfo.getInternal_num().replace(" ", "").equals("/") || inspectionInfo.getInternal_num().replace(" ", "").equals("无此项")){
						internal_num=deviceDocument.getInternal_num();
					}else{
						internal_num=inspectionInfo.getInternal_num();
					}
				}
			}
		}
		if(StringUtil.isEmpty(internal_num)){
			internal_num="/";
		}
		
		/*判断3D二维码编号是否为空*/
		if(StringUtil.isEmpty(device_qr_code)){
			if(StringUtil.isEmpty(inspectionInfo.getDevice_qr_code())){
				device_qr_code=deviceDocument.getDevice_qr_code();
			}else{
				device_qr_code=inspectionInfo.getDevice_qr_code();
			}
		}
		
		/*判断设备代码是否为空*/
		device_code = StringUtil.isEmpty(device_code)
				? (StringUtil.isEmpty(deviceDocument.getDevice_code()) ? "/" : deviceDocument.getDevice_code())
				: ((device_code.replace(" ", "").equals("/") || device_code.replace(" ", "").equals("-") || device_code.replace(" ", "").equals("无此项"))
						? (StringUtil.isEmpty(deviceDocument.getDevice_code()) ? "/" : deviceDocument.getDevice_code())
						: (device_code.replace(" ", "")));
		
		/*获取登记机关*/
		if(StringUtil.isEmpty(deviceDocument.getRegistration_agencies())){
			if(StringUtil.isEmpty(deviceDocument.getDevice_area_code())){
				registration_agencies="";
			}else{
				if(deviceDocument.getDevice_area_code().substring(0, 4).equals("5101")){
					registration_agencies="成都市质量技术监督局";
				}else if(deviceDocument.getDevice_area_code().substring(0, 4).equals("5401")){
					registration_agencies="拉萨市质量技术监督局";
				}else if(deviceDocument.getDevice_area_code().substring(0, 4).equals("6522")){
					registration_agencies="新疆生产建设兵团第十三师质量技术质监局";
				}else{
					registration_agencies="";
				}
			}
		}else{
			if(deviceDocument.getRegistration_agencies().replace(" ", "").equals("/")
					|| deviceDocument.getRegistration_agencies().replace(" ", "").equals("无此项")){
				if(StringUtil.isEmpty(deviceDocument.getDevice_area_code())){
					registration_agencies="";
				}else{
					if(deviceDocument.getDevice_area_code().substring(0, 4).equals("5101")){
						registration_agencies="成都市质量技术监督局";
					}else if(deviceDocument.getDevice_area_code().substring(0, 4).equals("5401")){
						registration_agencies="拉萨市质量技术监督局";
					}else if(deviceDocument.getDevice_area_code().substring(0, 4).equals("6522")){
						registration_agencies="新疆生产建设兵团第十三师质量技术质监局";
					}else{
						registration_agencies="";
					}
				}
			}else{
				registration_agencies=deviceDocument.getRegistration_agencies();
			}
		}
		
		/*判断登记证编号是否为空*/
		/*registration_num = StringUtil.isEmpty(registration_num) ? deviceDocument.getRegistration_num()
				: ((registration_num.replace(" ", "").equals("/") || registration_num.replace(" ", "").equals("无此项"))
						? deviceDocument.getRegistration_num() : registration_num);*/
		if(StringUtil.isEmpty(registration_num)) {
			registration_num=deviceDocument.getRegistration_num();
			//若是哈密地区的电梯则登记证编号取设备注册代码
			if(StringUtil.isNotEmpty(deviceDocument.getDevice_area_code())) {
				if(deviceDocument.getDevice_area_code().substring(0, 4).equals("6522")) {
					if(big_class.equals("3")) {
						registration_num=deviceDocument.getDevice_registration_code();
					}
				}
			}
		}else {
			if(registration_num.replace(" ", "").equals("/") || registration_num.replace(" ", "").equals("无此项")) {
				registration_num=deviceDocument.getRegistration_num();
				if(StringUtil.isNotEmpty(deviceDocument.getDevice_area_code())) {
					if(deviceDocument.getDevice_area_code().substring(0, 4).equals("6522")) {
						if(big_class.equals("3")) {
							registration_num=deviceDocument.getDevice_registration_code();
						}
					}
				}
			}else {
				registration_num=registration_num.replace(" ", "");
			}
		}
		if(StringUtil.isEmpty(registration_num)){
			registration_num="";
		}else if(registration_num.replace(" ", "").equals("不发证")){
			registration_num="";
		}
		
		/*判断下次检验时期是否为空*/
		if(StringUtil.isEmpty(inspect_next_date)||inspect_next_date.contains("null")){
			Date date_temp=inspectionInfo.getLast_check_time()!=null?inspectionInfo.getLast_check_time():deviceDocument.getInspect_next_date();
			if(date_temp!=null){
				if(!big_class.equals("3")&&!big_class.equals("6")&&!big_class.equals("9")){
					inspect_next_date= sdf1.format(date_temp);
				}else{
					inspect_next_date=sdf.format(date_temp);
				}
			}
		}

		/*判断维保单位是否为空*/
		if(StringUtil.isEmpty(maintain_unit_name)){
			if(StringUtil.isEmpty(inspectionInfo.getMaintain_unit_name())){
				maintain_unit_name=deviceDocument.getMaintain_unit_name();
			}else{
				if(inspectionInfo.getMaintain_unit_name().replace(" ", "").equals("/")||inspectionInfo.getMaintain_unit_name().replace(" ", "").equals("无此项")){
					maintain_unit_name=deviceDocument.getMaintain_unit_name();
				}else{
					maintain_unit_name=inspectionInfo.getMaintain_unit_name();
				}
			}
		}else{
			if(maintain_unit_name.replace(" ", "").equals("/")||maintain_unit_name.replace(" ", "").equals("无此项")){
				if(StringUtil.isEmpty(inspectionInfo.getMaintain_unit_name())){
					maintain_unit_name=deviceDocument.getMaintain_unit_name();
				}else{
					if(inspectionInfo.getMaintain_unit_name().replace(" ", "").equals("/")||inspectionInfo.getMaintain_unit_name().replace(" ", "").equals("无此项")){
						maintain_unit_name=deviceDocument.getMaintain_unit_name();
					}else{
						maintain_unit_name=inspectionInfo.getMaintain_unit_name();
					}
				}
			}
		}
		/*maintain_unit_name = StringUtil.isEmpty(maintain_unit_name)
				? (StringUtil.isEmpty(inspectionInfo.getMaintain_unit_name()) ? deviceDocument.getMaintain_unit_name()
						: inspectionInfo.getMaintain_unit_name())
				: maintain_unit_name;*/
		
		/*判断应急救援电话是否为空*/
		security_tel = StringUtil.isEmpty(security_tel) ? deviceDocument.getMaintenance_tel()
				: ((security_tel.replace(" ", "").equals("/") || security_tel.replace(" ", "").equals("无此项"))
						? deviceDocument.getMaintenance_tel() : security_tel);
		
		String printer_name = employeePrinterService.queryPrinterName(emp.getId(), Constant.PRINTER_TYPE_T);
		wrapper.put("report_sn", inspectionInfo.getReport_sn());          //设备种类
		wrapper.put("device_sort_code", device_sort_code);          //设备种类
		wrapper.put("device_sort", device_sort);					//设备品种
		wrapper.put("big_class", big_class);					    //设备大类
		wrapper.put("company_name", company_name); 	           		//使用单位
		wrapper.put("internal_num", internal_num);		            //单位内编号
		wrapper.put("device_qr_code", device_qr_code);  			//3D二维码编号
		wrapper.put("device_code", device_code);  			 		//设备代码
		wrapper.put("registration_agencies", registration_agencies);//登记机关
		wrapper.put("registration_num", registration_num);          //登记证编号
		wrapper.put("inspect_next_date", inspect_next_date);		//下次检验日期
		wrapper.put("maintain_unit_name", maintain_unit_name); 	    //维保单位
		wrapper.put("security_tel", security_tel);		            //应急救援电话
		wrapper.put("printer_name", printer_name);		            //打印机
		wrapper.put("device_area_code", StringUtil.isNotEmpty(deviceDocument.getDevice_area_code())?deviceDocument.getDevice_area_code().substring(0, 4):"");//设备所在地编号
		wrapper.put("success", true);
		return wrapper;
	}
	/**
	 * 保存标签打印记录
	 * 
	 * @param request
	 * @param id --
	 *            设备ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "savePrintTagsRecord")
	@ResponseBody
	public HashMap<String, Object> savePrintTagsRecord(HttpServletRequest request, String id,
			String report_id, String device_id) {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		CurrentSessionUser curUser = this.getCurrentUser(); // 获取当前用户登录信息
		try {
			InspectionInfo inspectionInfo = infoService.get(id);
			inspectionInfo.setIs_print_tags("1");//已打印
			infoService.save(inspectionInfo);
			logService.setLogs(id, Constant.SYS_OP_ACTION_PRINT_TAGS, Constant.SYS_OP_ACTION_PRINT_TAGS, curUser.getId(), curUser.getName(), new Date(), request.getRemoteAddr());
			wrapper.put("success", true);
		} catch (Exception e) {
			 wrapper.put("success", false);
			log.error("保存标签打印日志出错：" + e.getMessage());
			e.printStackTrace();
		}
		return wrapper;
	}
	
	/**
	 * 获取业务历史操作时间
	 * @param id
	 * @return
	 */
	@RequestMapping("getTime")
	@ResponseBody
	public Map<String, Object> getTime(String id){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		try {
			InspectionInfo inspectionInfo = infoService.get(id);
			paraMap.put("data",inspectionInfo);
			paraMap.put("success",true);
			
		}catch(Exception e) {
			e.printStackTrace();
			paraMap.put("msg",e.getMessage());
			paraMap.put("success",false);
		}
		return paraMap;
	}
	
	/**
	 * 批量报告加载
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "showReportList")
	public ModelAndView showPrint(HttpServletRequest request)
			throws Exception {
		String id = request.getParameter("id");
		String re_print =request.getParameter("re_print");
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String userId = user.getSysUser().getId();
		
		List<Map<String, Object>> mapList = null;
		if (StringUtil.isNotEmpty(re_print) && "1".equals(re_print)) {
			mapList = infoService.queryInfo(id, null);
		}else{
			mapList = infoService.queryInfo(id, userId, null);
		}
		request.getSession().setAttribute("mapList", mapList);
		String view = request.getParameter("view");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mapList", mapList);
	
		map.put("re_print", request.getParameter("re_print"));
		return new ModelAndView(view,map);
	}
	
}
