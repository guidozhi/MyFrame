package com.lsts.inspection.web;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khnt.core.crud.bean.BaseEntity;
import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.pub.fileupload.bean.Attachment;
import com.khnt.pub.fileupload.service.AttachmentManager;
import com.khnt.rbac.impl.bean.Employee;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rbac.impl.manager.EmployeeManager;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.StringUtil;
import com.lsts.advicenote.service.MessageService;
import com.lsts.common.service.InspectionCommonService;
import com.lsts.constant.Constant;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.service.DeviceService;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.bean.InspectionInfoDTO2;
import com.lsts.inspection.bean.ReportItemValue;
import com.lsts.inspection.dao.InspectionDao;
import com.lsts.inspection.service.InspectionInfoService;
import com.lsts.inspection.service.InspectionService;
import com.lsts.inspection.service.ReportItemRecordService;
import com.lsts.inspection.service.ReportItemValueService;
import com.lsts.log.bean.SysLog;
import com.lsts.log.service.SysLogService;
import com.lsts.report.bean.Report;
import com.lsts.report.service.ReportService;

/**
 * 业务报检 web controller
 * 
 * @author 肖慈边 2014-2-17
 */

@Controller
@RequestMapping("inspectionInfo/basic")
public class InspectionInfoAction extends
		SpringSupportAction<InspectionInfo, InspectionInfoService> {

	@Autowired
	private InspectionInfoService infoService;
	@Autowired
	private AttachmentManager attachmentManager;
	@Autowired
	private EmployeeManager employeeManager;
	@Autowired
	private MessageService messageService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private SysLogService logService;
	@Autowired
	private ReportItemValueService reportItemValueService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private InspectionCommonService inspectionCommonService;
	@Autowired
	private InspectionService inspectionService;
	@Autowired
	private SysLogService sysLogService;
	@Autowired
	private InspectionDao inspectionDao;
	@Autowired
	private ReportItemRecordService reportItemRecordService;

	// 删除部门报检信息
	@RequestMapping(value = "del")
	@ResponseBody
	public HashMap<String, Object> del(HttpServletRequest request, String ids) throws Exception {
		infoService.del(request, ids);
		return JsonWrapper.successWrapper(ids);
	}

	/**
	 * 上传附件
	 * 
	 * @param request
	 * @param employee
	 * @throws Exception
	 */
	@RequestMapping(value = "uploadAttachments")
	@ResponseBody
	public HashMap<String, Object> uploadAttachments(HttpServletRequest request) throws Exception {
		String uploadFiles = request.getParameter("uploadFiles");
		String info_id = request.getParameter("info_id");
		try {
			infoService.saveAttachments(info_id, uploadFiles); // 保存附件
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg("保存人员信息失败，请重试！");
		}
		return JsonWrapper.successWrapper(info_id);
	}

	/**
	 * 详情
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getAttachments")
	@ResponseBody
	public HashMap<String, Object> getAttachments(HttpServletRequest request, String info_id) throws Exception {
		List<Attachment> list = attachmentManager.getBusAttachment(info_id);
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		wrapper.put("success", true);
		wrapper.put("attachs", list);
		return wrapper;
	}

	/**
	 * 罐车任务分配
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "addPlan")
	@ResponseBody
	public Map<String, Object> addPlan(HttpServletRequest request, InspectionInfo info) throws Exception {
		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		try {
			paraMap = infoService.savePlan(request, info);
		} catch (Exception e) {
			e.printStackTrace();
			paraMap.put("success", false);
			paraMap.put("message", "请检查流程配置！");

		}
		return paraMap;

	}

	/**
	 * 修改任务分配
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "changePlan")
	@ResponseBody
	public Map<String, Object> changePlan(HttpServletRequest request, InspectionInfo info) throws Exception {
		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		try {
			paraMap = infoService.changePlan(request, info);
		} catch (Exception e) {
			e.printStackTrace();
			paraMap.put("success", false);
			paraMap.put("message", "修改错误！");

		}
		return paraMap;

	}

	// 报告录入时退回
	@RequestMapping(value = "backPlan")
	@ResponseBody
	public HashMap<String, Object> backPlan(HttpServletRequest request) throws Exception {
		String ids = request.getParameter("infoId");
		HashMap map = new HashMap();

		try {
			map = infoService.backPlan(ids);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "回退失败！");

		}

		return map;
	}

	/**
	 * 确定任务分配
	 * 
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "subFlow")
	@ResponseBody
	public Map<String, Object> subFlow(HttpServletRequest request) throws Exception {
		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		try {
			String info = request.getParameter("infoId");
			paraMap = infoService.subFlow(request, info);
		} catch (Exception e) {
			e.printStackTrace();
			paraMap.put("success", false);
			paraMap.put("message", "请检查流程配置！");

		}
		return paraMap;

	}

	// 上传pdf
	@RequestMapping("importPdf")
	@ResponseBody
	public HashMap importPdf(String filePath, String fileName, HttpServletRequest request, String type)
			throws Exception {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		// String basePath =
		// request.getSession().getServletContext().getRealPath("\\");
		// System.out.println(basePath);"app/oa/staff/template/template.xls"
		try {
			infoService.importPdf(paramMap, filePath, request, fileName);
			paramMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			paramMap.put("success", false);
		}

		return paramMap;
	}

	@RequestMapping("expPdfFlag")
	@ResponseBody
	public HashMap<String, Object> expPdfFlag(String id, String date, HttpServletRequest request) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		// String basePath =
		// request.getSession().getServletContext().getRealPath("\\");
		// System.out.println(basePath);"app/oa/staff/template/template.xls"
		try {
			infoService.expPdfFlag(id, date);
			paramMap.put("report_type", infoService.get(id).getReport_type());
			paramMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			paramMap.put("success", false);
		}

		return paramMap;
	}

	// 修改编制人
	@RequestMapping(value = "addEditor")
	@ResponseBody
	public HashMap<String, Object> addEditor(HttpServletRequest request) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();

		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User user = (User) curUser.getSysUser();
		Employee emp = (Employee) user.getEmployee();
		String emp_id = emp.getId();
		String emp_name = emp.getName();

		try {
			String info_id = request.getParameter("info_id");
			String op_id = request.getParameter("enter_op_id");
			String op_name = request.getParameter("enter_op_name");

			if (StringUtil.isNotEmpty(op_id) && StringUtil.isNotEmpty(op_name)) {
				if (StringUtil.isNotEmpty(info_id)) {
					InspectionInfo info = infoService.get(info_id);
					if (info != null) {
						String enter_op_id = info.getEnter_op_id();
						String[] enter_ops = enter_op_id.split(",");
						if (enter_ops.length == 2) {
							return JsonWrapper.failureWrapperMsg("亲，您所选的报告已有2位编制人，不能再添加了哦！");
						} else {
							String enter_op_ids = emp_id + "," + op_id;
							String enter_op_names = emp_name + "," + op_name;
							info.setEnter_op_id(enter_op_ids);
							info.setEnter_op_name(enter_op_names);
							infoService.save(info);

							// 获取被提醒对象用户信息
							Employee employee = employeeManager.get(op_id);
							if (employee != null) {
								String mobile = employee.getMobileTel();
								if (StringUtil.isNotEmpty(mobile)) {
									if (mobile.length() == 11) {
										// 获取报告名称
										String report_name = "";
										if (StringUtil.isNotEmpty(info.getReport_type())) {
											Report report = reportService.get(info.getReport_type());
											report_name = report.getReport_name();
										}

										// 获取发送内容
										String content = Constant.getReportNoticeContent(emp_name, employee.getName(),
												report_name, info.getReport_sn(), info.getReport_com_name());
										// 发送微信
										messageService.sendWxMsg(request, employee.getId(), Constant.INSPECTION_CORPID,
												Constant.INSPECTION_PWD, content, mobile);
										// 发送短信
										messageService.sendMoMsg(request, employee.getId(), content, mobile);
									} else {
										return JsonWrapper.failureWrapperMsg("发送失败：手机号码不正确，请核实！");
									}
								} else {
									return JsonWrapper.failureWrapperMsg("发送失败：系统暂未设置手机号码！请联系系统管理员！");
								}
							}
							paramMap.put("success", true);
						}
					}
				}
			} else {
				return JsonWrapper.failureWrapperMsg("请选择您要添加的编制人！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			paramMap.put("success", false);
		}
		return paramMap;
	}

	// 撤销盖章
	@RequestMapping(value = "backs")
	@ResponseBody
	public HashMap<String, Object> backs(HttpServletRequest request) throws Exception {
		return infoService.backs(request);
	}

	// 报告校核
	@RequestMapping(value = "reportCheck")
	@ResponseBody
	public HashMap<String, Object> reportCheck(@RequestBody InspectionInfoDTO2 entity, HttpServletRequest request)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User user = (User) curUser.getSysUser();
		Employee emp = (Employee) user.getEmployee();
		String emp_id = emp.getId();
		String emp_name = emp.getName();
		if (!request.getParameter("ids").equals("") && request.getParameter("ids") != null) {
			String[] ids = request.getParameter("ids").split(",");
			for (String id : ids) {
				InspectionInfo inspectionInfo = infoService.get(id);
				if (request.getParameter("check").equals("pass")) {
					inspectionInfo.setIs_report_confirm("1"); // 校核通过
				} else if (request.getParameter("check").equals("nopass")) {
					inspectionInfo.setIs_report_confirm("2"); // 校核未通过
					inspectionInfo.setReport_confirm_remark(entity.getReport_confirm_remark());
				}
				inspectionInfo.setConfirm_Date(new Date());
				inspectionInfo.setConfirm_id(emp_id);
				inspectionInfo.setConfirm_name(emp_name);
				infoService.save(inspectionInfo);

				logService.setLogs(id, "原始记录校核", "原始记录校核", user.getId(), user.getName(), new Date(),
						request.getRemoteAddr());
			}
			map.put("success", true);
			map.put("msg", "校核成功！");
		} else {
			map.put("success", false);
			map.put("msg", "请选择要校核的报告！");
		}
		return map;
	}

	/**
   	 * 批量同步设备信息
   	 * 同步内容：使用单位代码、单位内部编号、金属二维码、设备型号、出厂编号、
   	 * 安全管理员、安全管理联系电话、设备使用地点、制造日期、维保单位联系人、维保单位联系人电话
   	 * @param request
   	 * 
   	 * @return
   	 * @author GaoYa
   	 * @date 2017-05-18 上午10:45:00
   	 */
	@RequestMapping(value = "updateDevices")
	@ResponseBody
	public HashMap<String, Object> updateDevices(HttpServletRequest request) throws Exception {	
       	HashMap<String, Object>  map = new HashMap<String, Object>();	
       	try {
       		// 报告业务ID集合
       		String infoIds = request.getParameter("infoIds");	
   			String ids[] = infoIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				// 1、报告业务信息
				InspectionInfo inspectionInfo = infoService.get(ids[i]);	
				// 2、获取设备库原设备信息
				DeviceDocument old_device = deviceService.get(inspectionInfo.getFk_tsjc_device_document_id());
				// 3、获取报告里的设备信息
				List<ReportItemValue> list = reportItemValueService.getItemByItemNames(ids[i],
						inspectionInfo.getReport_type(), Constant.DT_UPDATE_COLUMNS);
				if (!list.isEmpty()) {
					DeviceDocument new_device = new DeviceDocument();					
					
					// 4、设置设备修改日志基本信息
					Map<String, String> infoMap = new HashMap<String, String>();	
			       	infoMap.put("table_code", "base_device_document");
			   		infoMap.put("table_name", "设备基础信息表");
			   		infoMap.put("op_action", "同步设备信息");	   		
			   		infoMap.put("op_remark", "报告书覆盖设备信息");
			   		
					// 5、封装需要更新的设备信息
					Set<String> _docMap = new HashSet<String>();
					_docMap = DeviceDocument.update_bean_to_set();
					for (ReportItemValue reportItemValue : list) {
						if(StringUtil.isNotEmpty(reportItemValue.getItem_value())){
							if (_docMap.contains(reportItemValue.getItem_name().toUpperCase())) {
								// 只同步更新报告内容和设备库有不一致的数据
								if("COM_CODE".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getCom_code()!=null){
										if(old_device.getCom_code().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}else if("DEVICE_QR_CODE".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getDevice_qr_code()!=null){
										if(old_device.getDevice_qr_code().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}else if("INTERNAL_NUM".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getInternal_num()!=null){
										if(old_device.getInternal_num().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}else if("DEVICE_MODEL".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getDevice_model()!=null){
										if(old_device.getDevice_model().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}else if("FACTORY_CODE".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getFactory_code()!=null){
										if(old_device.getFactory_code().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}else if("SECURITY_OP".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getSecurity_op()!=null){
										if(old_device.getSecurity_op().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}else if("SECURITY_TEL".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getSecurity_tel()!=null){
										if(old_device.getSecurity_tel().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}else if("DEVICE_USE_PLACE".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getDevice_use_place()!=null){
										if(old_device.getDevice_use_place().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}else if("MAKE_DATE".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getMake_date()!=null){
										if(old_device.getMake_date().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}/*else if("MAINTAIN_UNIT_NAME".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getMaintain_unit_name()!=null){
										if(old_device.getMaintain_unit_name().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}else if("MAKE_UNITS_NAME".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getMake_units_name()!=null){
										if(old_device.getMake_units_name().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}else if("CONSTRUCTION_UNITS_NAME".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getConstruction_units_name()!=null){
										if(old_device.getConstruction_units_name().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}*/else if("CONSTRUCTION_LICENCE_NO".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getConstruction_licence_no()!=null){
										if(old_device.getConstruction_licence_no().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}else if("REGISTRATION_NUM".equals(reportItemValue.getItem_name().toUpperCase())){
									if(old_device.getRegistration_num()!=null){
										if(old_device.getRegistration_num().equals(reportItemValue.getItem_value().trim())){
											continue;
										}
									}
								}
								setEntityParaValue(new_device, reportItemValue.getItem_name().toLowerCase(),
										reportItemValue.getItem_value().trim());
							}
						}
					}
					// 6、保存设备并返回修改前的老数据集合
					List<Map<String, String>> oldList = deviceService.updateDevices(request, new_device, inspectionInfo.getFk_tsjc_device_document_id());
					for (int j = 0; j < oldList.size(); j++) {
		   				Map<String, String> old_Map = oldList.get(j);
		   				String device_id = old_Map.get("id").toString();
		   				// 7、获取设备修改后的新数据集合
		   				Map<String, String> new_Map = deviceService.queryDeviceById(device_id);
		   				infoMap.put("business_id", device_id);
		   				// 8、对比新老数据并记录修改日志
		   				inspectionCommonService.compareMapOfBean(infoMap, old_Map, new_Map, request);
		   			}
				}
			}
   			map.put("success", true);
   		} catch (Exception e) {
   			e.printStackTrace();
   			map.put("success", false);
   			map.put("msg", "请求超时，请稍后再试！");
   		}
    	return map;
	}
	
	// 流程提交（报告打印未自动提交流程至报告领取时，进行手动提交流程）
	@RequestMapping(value = "printSubFlow")
	@ResponseBody
	public Map<String, Object> printSubFlow(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 业务ID
		String ins_info_id = request.getParameter("infoId");
		// 流程编号ID
		String acc_id = request.getParameter("acc_id");
		// 步骤号
		String flow_num = request.getParameter("flow_num");
		String ids[] = ins_info_id.split(",");
		String acc_ids[] = acc_id.split(",");
		for (int i = 0; i < ids.length; i++) {
			// 业务信息
			InspectionInfo inspectionInfo = infoService.get(ids[i]);
			Date print_time = null;
			if (StringUtil.isNotEmpty(inspectionInfo.getFlow_note_name())
					&& "打印报告".equals(inspectionInfo.getFlow_note_name())) {
				List<SysLog> list = sysLogService.getReportLogs(ids[i], "打印");
				if (list == null) {
					return JsonWrapper.failureWrapperMsg(inspectionInfo.getReport_sn() + "报告未打印，暂不能提交流程至报告领取，请重新选择！");
				} else {
					if (inspectionInfo.getPrint_time() == null) {
						for (SysLog sysLog : list) {
							print_time = sysLog.getOp_time();
						}
					}
				}
			}

			if (inspectionInfo.getPrint_time() == null) {
				inspectionInfo.setPrint_time(print_time);
				infoService.save(inspectionInfo);
			}

			map.put("ins_info_id", ids[i]);
			map.put("acc_id", acc_ids[i]);
			map.put("flow_num", flow_num);
			map.put("flag", "0"); // 不指定下一步操作人			
			
			// 流程提交，提交到下一个流程环节，报告领取
			inspectionService.subFlowProcess(map, request);
		}
		return JsonWrapper.successWrapper(ins_info_id);
	}
	
	// 启动流程
	@RequestMapping(value = "startFlows")
	@ResponseBody
	public Map<String, Object> startFlows(HttpServletRequest request) throws Exception {
		// 报告业务ID
		String ins_info_id = request.getParameter("infoId");
		// 设备类别代码
		String big_class = request.getParameter("big_class");
		// 检验类别代码
		String check_type_code = request.getParameter("check_type_code");
		
		String ids[] = ins_info_id.split(",");	
		String big_classs[] = big_class.split(",");	
		String check_type_codes[] = check_type_code.split(",");	
		
		for (int i = 0; i < ids.length; i++) {
			// 报告业务信息
			InspectionInfo inspectionInfo = infoService.get(ids[i]);
			if (StringUtil.isEmpty(inspectionInfo.getFlow_note_name())) {
				if(StringUtil.isEmpty(inspectionInfo.getIs_flow())){
					String flow_id = infoService.queryFlowId(big_classs[i], check_type_codes[i], inspectionInfo.getCheck_unit_id(), inspectionInfo.getReport_type());
					if(StringUtil.isNotEmpty(flow_id)){
						HashMap<String, String> info_map = new HashMap<String, String>();
						info_map.put("infoId", ids[i]);
						info_map.put("flowId", flow_id);
						info_map.put("status", "1");

						String process_id = inspectionDao.getProcess(ids[i]);
						if(StringUtil.isEmpty(process_id)){
							inspectionService.StarFlowProcess(info_map, request);
						}
					}
				}
			}else{
				return JsonWrapper.failureWrapperMsg(inspectionInfo.getReport_sn()+"流程已启动，不能重复启动！");
			}
		}
		return JsonWrapper.successWrapper(ins_info_id);
	}

	@RequestMapping("getExportDirs")
	@ResponseBody
	public HashMap<String, Object> getExportDirs(HttpServletRequest request, String ids) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		try {
			paramMap.put("export_dirs", infoService.queryInspectionDate(ids));
			paramMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			paramMap.put("success", false);
			paramMap.put("msg", "打印报告时，获取报告目录失败，请稍后再试！");
		}
		return paramMap;
	}
	
	
	// 报告校核
	@RequestMapping(value = "pdf2Swf")
	@ResponseBody
	public HashMap<String, Object> pdf2Swf(String pdfPath, String swfPath, String swfName, HttpServletRequest request)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		/*
		 * pdfPath="TEST/123.pdf"; swfPath = "TEST"; swfName = "123";
		 */
		try {
			infoService.pdf2Swf(pdfPath, swfPath, swfName);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			map.put("mag", "pdf报告转swf失败！");
			e.printStackTrace();
		}

		return map;
	}

	// 报告校核
	@RequestMapping(value = "setPdf2Swf")
	@ResponseBody
	public HashMap<String, Object> setPdf2Swf(String pdfFold, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();

		try {
			String flag = request.getParameter("flag");
			infoService.setPdf2Swf(pdfFold,flag);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			map.put("mag", "批处理pdf报告转swf失败！");
			e.printStackTrace();

		}

		return map;
	}
	
	 // 修改报告收费金额
    @RequestMapping(value = "modifyMoneys")
    @ResponseBody
    public HashMap<String, Object> modifyMoneys(HttpServletRequest request) throws Exception {	
        return infoService.modifyMoneys(request);
    }
	
	private void setEntityParaValue(BaseEntity obj ,String key , String value) 
			throws Exception {
		Method method=null;
		if(key.equals("first_install_date")||key.equals("remake_date")||key.equals("repair_date")||key.equals("install_finish_date")){
			method =obj.getClass().getMethod("set"+changFisrtUpper(key),
					Date.class);
			SimpleDateFormat far  = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			if(method!= null) {
				if (StringUtil.isNotEmpty(value)) {
					date = far.parse(value);
				}
				method.invoke(obj, date) ;
			}
		}else{
			method = obj.getClass().getMethod("set"+changFisrtUpper(key),
				String.class);
			if(method!= null) {
				method.invoke(obj, value) ;
			}
		}
	}
	
	private static String changFisrtUpper(String str){
		return str.substring(0, 1).toUpperCase()
				+ str.substring(1, str.length());
	}

	@RequestMapping(value = "/downloadPdf", method = RequestMethod.GET)
	public void pdfStreamHandeler(HttpServletRequest request, HttpServletResponse response, String fileName) {
		if(StringUtil.isEmpty(fileName)){
			fileName = "LINUX.pdf";
		}
		String filePath = "D:/TEMP/" + fileName;
		// System.out.println(fileName);
		File file = new File(filePath);
		byte[] data = null;
		try {
			FileInputStream input = new FileInputStream(file);
			data = new byte[input.available()];
			input.read(data);
			response.getOutputStream().write(data);
			input.close();
		} catch (Exception e) {
			System.out.print("pdf文件处理异常：" + e.getMessage());
		}

	}
	
	/**
	 * 根据业务id查询模板内容，没有传入版本号时默认最新
	 * author pingZhou
	 * @param request
	 * @param infoId 模板id
	 * @param modelType 模板类型 report||record
	 * @return 模板内容
	 * @throws Exception
	 */
	@RequestMapping("getPageContent")
	@ResponseBody
	public HashMap<String, Object> getPageContentByVersion(HttpServletRequest request,String infoId,String pageCode,String modelType) throws Exception {
		try {
			
			String content  = infoService.getPageContent(request,infoId,pageCode,modelType);
			return JsonWrapper.successWrapper(content);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg(e.getMessage());
		}
	}
	
	/**
	 * 更新模板
	 * author pingZhou
	 * @param request
	 * @param infoId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("setReportModelVersion")
	@ResponseBody
	public HashMap<String, Object> setReportModelVersion(HttpServletRequest request,String infoId,String type) throws Exception {
		try {
			
			infoService.setReportModelVersion(infoId,type);
			return JsonWrapper.successWrapper();
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg(e.getMessage());
		}
	}
	


	@RequestMapping("getDir")
	@ResponseBody
	public String getDir(HttpServletRequest request, String id,String infoId,String code) {

		try {
			String json = this.infoService.getDir(id,infoId,code);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return "[]";
		}

	}
	

	@RequestMapping("test")
	@ResponseBody
	public Map<String, Object> test(HttpServletRequest request,String c) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		
		
		int len = Integer.parseInt(c);
		 // 创建一个固定大小的线程池
        ExecutorService service = Executors.newFixedThreadPool(8);
        for (int i = 0; i < len; i++) {
            System.out.println("创建线程" + i);
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
						reportItemRecordService.queryEmployeeList();
						//reportItemRecordService.getTasks("402884c4477c9bac01477ff32fc6005b");
						//tjyptResourcePathService.queryResourceNeedReceive("1");

					} catch (ParseException e) {
						
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
        System.out.println("all thread complete");

		
		return map;
	}

}
