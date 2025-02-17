package com.lsts.inspection.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.base.Factory;
import com.khnt.bpm.core.bean.Activity;
import com.khnt.bpm.core.service.ActivityManager;
import com.khnt.bpm.core.service.ProcessManager;
import com.khnt.bpm.ext.service.FlowDefinitionManager;
import com.khnt.bpm.ext.service.FlowWorktaskManager;
import com.khnt.bpm.ext.support.FlowExtWorktaskParam;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.rbac.impl.bean.Employee;
import com.khnt.rbac.impl.bean.Org;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rtbox.config.bean.RtExportData;
import com.khnt.rtbox.config.service.RtPageManager;
import com.khnt.rtbox.template.constant.RtExportDataType;
import com.khnt.rtbox.template.constant.RtField;
import com.khnt.rtbox.template.constant.RtPageType;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.ImageTool;
import com.lsts.common.service.CodeTablesService;
import com.lsts.common.service.InspectionCommonService;
import com.lsts.constant.Constant;
import com.lsts.employee.dao.EmployeesDao;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.bean.InspectionZZJD;
import com.lsts.inspection.bean.InspectionZZJDInfo;
import com.lsts.inspection.bean.ReportItemValue;
import com.lsts.inspection.bean.ReportPerAudit;
import com.lsts.inspection.dao.InspectionDao;
import com.lsts.inspection.dao.InspectionInfoDao;
import com.lsts.inspection.dao.InspectionZZJDDao;
import com.lsts.inspection.dao.InspectionZZJDInfoDao;
import com.lsts.inspection.dao.ReportItemValueDao;
import com.lsts.inspection.dao.ReportPerDao;
import com.lsts.log.bean.SysLog;
import com.lsts.log.service.SysLogService;
import com.lsts.report.bean.Report;
import com.lsts.report.bean.ReportItem;
import com.lsts.report.bean.SetReportItem;
import com.lsts.report.dao.ReportItemDao;
import com.lsts.report.service.ReportItemService;
import com.lsts.report.service.ReportService;
import com.ming.webreport.MREngine;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.TS_Util;

/**
 * 制造监督检验报检业务逻辑对象
 * 
 * @ClassName InspectoinZZJDService
 * @JDK 1.6
 * @author GaoYa
 * @date 2015-01-13 上午09:09:00
 */
@Service("inspectionZZJDService")
public class InspectionZZJDService extends
		EntityManageImpl<InspectionZZJD, InspectionZZJDDao> {
	@Autowired
	private InspectionZZJDDao inspectionZZJDDao;
	@Autowired
	private InspectionZZJDInfoDao inspectoinZZJDInfoDao;
	@Autowired
	private InspectionInfoDao inspectionInfoDao;
	@Autowired
	private SysLogService logService;
	@Autowired
	private ProcessManager processManager;
	@Autowired
	private ActivityManager activityManager;
	@Autowired
	FlowDefinitionManager flowDefManager;
	@Autowired
	FlowWorktaskManager flowExtManager;
	@Autowired
	private EmployeesDao employeesDao;
	@Autowired
	private ReportService reportService;
	@Autowired
	private CodeTablesService codeTablesService;
	@Autowired
	private ReportItemDao itemDao;
	@Autowired
	private ReportPerDao perDao;
	@Autowired
	private ReportItemService reportItemService;
	@Autowired
	private ReportItemValueDao reportItemValueDao;
	@Autowired
	private InspectionDao inspectionDao;
	@Autowired
	private ImageTool imageTool;
	@Autowired
	private InspectionCommonService inspectionCommonService;
	@Autowired
	private RtPageManager rtPageManager;
	/**
	 * 
	 * 保存报告录入信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public InspectionZZJD saveBasic(InspectionZZJD inspectionZZJD,
			HttpServletRequest request) throws Exception {
		// 获取connection
		Connection conn = Factory.getDB().getConnetion();

		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User user = (User)curUser.getSysUser();
		Employee emp = (com.khnt.rbac.impl.bean.Employee)user.getEmployee();
		Org org = TS_Util.getCurOrg(curUser);
		// 获取业务报检表单号
		try {
			int count = 0;
			for (InspectionZZJDInfo info : inspectionZZJD
					.getInspectionZZJDInfo()) {
				count++;
				if (StringUtil.isEmpty(info.getSn())) {
					// 获取业务流水号
					String sn = TS_Util.getZZJDInfoSn(Integer.valueOf(count),
							conn);
					info.setSn(sn);
				}
				info.setData_status("1"); // 数据状态（1：正常）
				info.setInspection_unit_id(org.getId());
				info.setInspection_unit_name(org.getOrgName());
				info.setDevice_type_name(codeTablesService.getValueByCode(
						"device_classify", info.getDevice_type_code()));
				if (StringUtil.isNotEmpty(info.getDevice_processing())) {
					if (!"√".equals(info.getDevice_processing())) {
						info.setDevice_processing("√");
						info.setDevice_processing2("");
					}
				}
				if (StringUtil.isNotEmpty(info.getDevice_processing2())) {
					if (!"√".equals(info.getDevice_processing2())) {
						info.setDevice_processing("");
						info.setDevice_processing2("√");
					}
				}
				
				InspectionInfo inspectionInfo = null;
				if (StringUtil.isNotEmpty(info.getId())) {
					inspectionInfo = inspectionInfoDao.get(info
							.getFk_inspection_info_id());
				} else {
					inspectionInfo = new InspectionInfo();
				}
				
				// 批量出证的，不存在设备信息，为了实现与机电类统一综合查询，默认赋值
				inspectionInfo.setFk_tsjc_device_document_id(Constant.DEFAULT_DEVICE_ID);
				
				if (StringUtil.isEmpty(inspectionInfo.getSn())) {
					inspectionInfo.setSn(TS_Util.getSn(Integer.valueOf(count),
							conn));
				}

				String check_op_id = "";
				String check_op_name = "";
				if (StringUtil.isNotEmpty(info.getInspection_user_name1())) {
					// 根据用户姓名获取用户ID
					List<Employee> list1 = employeesDao.queryEmployees(info
							.getInspection_user_name1(), org.getId());
					if(list1.size()<1){
						list1 = employeesDao.queryEmployees(info
								.getInspection_user_name1());
					} 
					if(!list1.isEmpty()){
						Employee employee1 = list1.get(0);
						if (employee1 != null) {
							if (StringUtil.isNotEmpty(check_op_id)) {
								check_op_id += ",";
							}
							check_op_id += employee1.getId(); // 参检人员，此处要用employee，因为报告使用的电子签名是与employee关联的
							check_op_name = info.getInspection_user_name1();
						}
					}
				}
				if (StringUtil.isNotEmpty(info.getInspection_user_name2())) {
					List<Employee> list2 = employeesDao.queryEmployees(info
							.getInspection_user_name2(), org.getId());
					if(list2.size()<1){
						list2 = employeesDao.queryEmployees(info
								.getInspection_user_name2());
					} 
					if(!list2.isEmpty()){
						Employee employee2 = list2.get(0);
						if (employee2 != null) {
							if (StringUtil.isNotEmpty(check_op_id)) {
								check_op_id += ",";
							}
							check_op_id += employee2.getId(); // 参检人员ID
							if (StringUtil.isNotEmpty(check_op_name)) {
								check_op_name += ",";
							}
							check_op_name += info.getInspection_user_name2();
						}
					}
				}
				
				inspectionInfo.setCheck_op_id(check_op_id);
				inspectionInfo.setCheck_op_name(check_op_name); // 参检人员姓名
				inspectionInfo.setCheck_unit_id(org.getId());
				inspectionInfo.setEnter_unit_id(org.getId());
				inspectionInfo.setEnter_op_id(emp
						.getId()); // 录入人，此处要用employee，因为报告使用的电子签名是与employee关联的
				inspectionInfo.setEnter_op_name(emp
						.getName());
				inspectionInfo.setEnter_time(new Date());
				inspectionInfo.setReport_type(inspectionZZJD.getReport_type()); // 报告类型
				
				//关联检验任务单

				String contract_task_id=request.getParameter("contract_task_id");
				String contract_task_sn=request.getParameter("contract_task_sn");
				inspectionInfo.setContract_task_id(contract_task_id);
				inspectionInfo.setContract_task_sn(contract_task_sn);
				
				// 根据报告类型ID获取报告页码
				List<ReportItem> itemList = itemDao.createQuery(
						"from ReportItem where fk_reports_id='"
								+ inspectionZZJD.getReport_type()
								+ "' order by page_index ").list();
				String report_item = "";
				if (!itemList.isEmpty()) {
					for (ReportItem reportItem : itemList) {
						if (StringUtil.isNotEmpty(report_item)) {
							report_item += ",";
						}
						report_item += reportItem.getPage_index();
					}
				}
				inspectionInfo.setReport_item(report_item); // 报告页码

				// 未填写或不存在使用单位时，默认将制造单位、施工单位、安装单位作为报告书使用单位
				String report_com_name = inspectionInfo.getReport_com_name();
				if (StringUtil.isNotEmpty(info.getCom_name()) && StringUtil.isEmpty(report_com_name)) {
					report_com_name = info.getCom_name(); // 使用单位
				} 
				if (StringUtil.isNotEmpty(info.getMade_unit_name()) && StringUtil.isEmpty(report_com_name)) {
					report_com_name = info.getMade_unit_name(); // 制造单位
				}
				
				if (StringUtil.isNotEmpty(info.getConstruction_unit_name()) && StringUtil.isEmpty(report_com_name)) {
					report_com_name = info .getConstruction_unit_name(); // 施工单位
				}
				if (StringUtil.isNotEmpty(info.getInstall_unit_name()) && StringUtil.isEmpty(report_com_name)) {
					report_com_name = info.getInstall_unit_name(); // 安装单位
				}
				if(StringUtil.isNotEmpty(report_com_name)){
					inspectionInfo.setReport_com_name(report_com_name);	// 报告书使用单位
				}
				
				// 是否自编号（0：否 1：是）
				String is_self_sn = info.getIs_self_sn();	
				if("1".equals(is_self_sn)){
					// 用户自编号时，才将录入的报告编号写入业务信息
					if(StringUtil.isNotEmpty(info.getReport_sn())){
						inspectionInfo.setReport_sn(info.getReport_sn());
					}
				}
				inspectionInfo.setIs_self_sn(is_self_sn);
				
				// 检验结论
				if(StringUtil.isNotEmpty(info.getInspection_conclusion())){
					inspectionInfo.setInspection_conclusion(info.getInspection_conclusion());
				}
				
				inspectionInfo.setAdvance_fees(info.getAdvance_fees()); 	// 预收金额
				inspectionInfo.setAdvance_time(info.getInspection_date()); 	// 检验日期
				
				// 下次检验日期
				if (StringUtil.isNotEmpty(info.getLast_inspection_date())) {
					if (!"/".equals(info.getLast_inspection_date()) && info.getLast_inspection_date().contains("-")) {
						inspectionInfo.setLast_check_time(DateUtil.convertStringToDate(Constant.defaultDatePattern,
								info.getLast_inspection_date())); 
					}else{
						
						
					}
				}				
				inspectionInfo.setFee_status("1"); // 收费状态（1：待收费）
				inspectionInfo.setIs_report_input("1"); // 报告是否已修改过（1：未修改 2：已修改）
				inspectionInfo.setIs_flow("1"); // 是否已进入流程（1：未进入流程 2：已进入流程）
				inspectionInfo.setIs_back("1"); // 是否退回（默认的状态为1，提交后变成0，退回也变成1）
				inspectionInfo.setIs_mobile("0");	// 是否移动检验（0：否 1：是）
				inspectionInfo.setData_status("1"); // 数据状态（1：正常）
				
				inspectionInfoDao.save(inspectionInfo);

				// 报告类型为“车用气瓶安装监检”、“爆破片监检”、“锅炉设计文件安全鉴定及节能审查报告”时，
				// 保存检验项目表信息（2：车用气瓶安装监检 5：爆破片装置安全性能监督检验证书 17：锅炉设计文件安全鉴定及节能审查报告）
				if ("2".equals(inspectionZZJD.getJdjy_type())) {
					saveQPAZJJ_ItemValues(inspectionZZJD.getReport_type(),
							info, inspectionInfo.getId());
				} else if ("5".equals(inspectionZZJD.getJdjy_type())) {
					saveBPPJJ_ItemValues(inspectionZZJD.getReport_type(), info,
							inspectionInfo.getId());
				} else if ("17".equals(inspectionZZJD.getJdjy_type())) {
					saveBPPJJ_ItemValues(inspectionZZJD.getReport_type(), info,
							inspectionInfo.getId());
				}
				info.setFk_inspection_info_id(inspectionInfo.getId());
				info.setInspectionZZJD(inspectionZZJD);
			}
			inspectionZZJD.setEnter_date(new Date());
			inspectionZZJD.setData_status("1"); // 数据状态（1：正常）
			inspectionZZJD.setEnter_op_id(user.getId()); // 此处不用到电子签名，所以不使用employee
			inspectionZZJD.setEnter_op_name(user.getName());
			if (StringUtil.isEmpty(inspectionZZJD.getSn())) {
				// 获取业务流水号
				String sn = TS_Util.getZZJDSn(Integer.valueOf(count), conn);
				inspectionZZJD.setSn(sn);
			}
			inspectionZZJDDao.save(inspectionZZJD);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
		}
		return inspectionZZJD;
	}

	// 保存车用气瓶安装监检项目表信息
	private void saveQPAZJJ_ItemValues(String report_type,
			InspectionZZJDInfo info, String inspection_info_id) {
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPC401", info.getDevice_file_check(),
				inspection_info_id); // 气瓶文件审核（1）监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPC402", info.getDevice_file_check(),
				inspection_info_id); // 气瓶文件审核（2）监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPD401", info.getFile_check_date(),
				inspection_info_id); // 气瓶文件审核（1）确认时间
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPD402", info.getFile_check_date(),
				inspection_info_id); // 气瓶文件审核（2）确认时间
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPC403", info.getInstall_data_check(),
				inspection_info_id); // 安装资料审核（1）监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPC404", info.getInstall_data_check(),
				inspection_info_id); // 安装资料审核（2）监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPC405", info.getInstall_data_check(),
				inspection_info_id); // 安装资料审核（3）监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPD403", info.getData_check_date(),
				inspection_info_id); // 安装资料审核（1）确认时间
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPD404", info.getData_check_date(),
				inspection_info_id); // 安装资料审核（2）确认时间
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPD405", info.getData_check_date(),
				inspection_info_id); // 安装资料审核（3）确认时间
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPC406", info.getDevice_surface_check(),
				inspection_info_id); // 气瓶外观检查（1）监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPD406", info.getSurface_check_date(),
				inspection_info_id); // 气瓶外观检查（1）确认时间
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPC407", info.getInstall_quality_check(),
				inspection_info_id);// 安装质量检查（1）监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPC408", info.getInstall_quality_check(),
				inspection_info_id);// 安装质量检查（2）监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPC409", info.getInstall_quality_check(),
				inspection_info_id);// 安装质量检查（3）监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPD407", info.getQuality_check_date(),
				inspection_info_id); // 安装质量检查（1）确认时间
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPD408", info.getQuality_check_date(),
				inspection_info_id); // 安装质量检查（2）确认时间
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPD409", info.getQuality_check_date(),
				inspection_info_id); // 安装质量检查（3）确认时间
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPC410", info.getLeak_test_check(),
				inspection_info_id); // 泄漏试验（1）监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPD410", info.getLeak_check_date(),
				inspection_info_id); // 泄漏试验（1）确认时间
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPC411", info.getFinish_data_check(),
				inspection_info_id); // 安装竣工资料审查（1）监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPD411", info.getFinish_check_date(),
				inspection_info_id); // 安装竣工资料审查（1）确认时间
	}

	// 保存车用气瓶安装监检项目表信息
	private void saveBPPJJ_ItemValues(String report_type,
			InspectionZZJDInfo info, String inspection_info_id) {
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA401", "√", inspection_info_id); // 设计文件检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA402", "√", inspection_info_id); // 材料质量证明书检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA403", "√", inspection_info_id); // 材料标记与标记移植检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA404", "/", inspection_info_id); // 焊接记录、热处理报告和记录检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA405", "/", inspection_info_id); // 无损检测报告检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA406", "√", inspection_info_id); // 爆破试验及标定爆破压力检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA407", "√", inspection_info_id); // 产品质量证明书检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA408", "√", inspection_info_id); // 外观、标志检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA409", "B", inspection_info_id); // 爆破试验及标定爆破压力
																	// 类别

		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB401", "设计文件", inspection_info_id); // 设计文件工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB402", "材料质量证明书", inspection_info_id); // 材料质量证明书工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB403", "检查记录", inspection_info_id); // 材料标记与标记移植工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB404", "记录、报告", inspection_info_id); // 焊接记录、热处理报告和记录工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB405", "检测报告", inspection_info_id);// 无损检测报告工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB406", "记录、报告", inspection_info_id);// 爆破试验及标定爆破压力工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB407", "产品质量证明书", inspection_info_id);// 产品质量证明书工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB408", "检查记录", inspection_info_id); // 外观、标志工作见证

		String inspection_user_names = "";
		if (StringUtil.isNotEmpty(info.getInspection_user_name1())) {
			inspection_user_names += info.getInspection_user_name1();
		}
		if (StringUtil.isNotEmpty(info.getInspection_user_name2())) {
			inspection_user_names += info.getInspection_user_name2();
		}

		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR1", inspection_user_names,
				inspection_info_id); // 设计文件监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR2", inspection_user_names,
				inspection_info_id); // 材料质量证明书监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR3", inspection_user_names,
				inspection_info_id); // 材料标记与标记移植监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR4", inspection_user_names,
				inspection_info_id); // 焊接记录、热处理报告和记录监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR5", inspection_user_names,
				inspection_info_id); // 无损检测报告监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR6", inspection_user_names,
				inspection_info_id); // 爆破试验及标定爆破压力监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR7", inspection_user_names,
				inspection_info_id); // 产品质量证明书监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR8", inspection_user_names,
				inspection_info_id); // 外观、标志监检员

		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "CONFIRM_DATE1", info.getConfirm_date1(),
				inspection_info_id); // 确认日期1（检验项目1~3）
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "CONFIRM_DATE2", info.getConfirm_date2(),
				inspection_info_id); // 确认日期2（检验项目4~6）
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "CONFIRM_DATE3", info.getConfirm_date3(),
				inspection_info_id); // 确认日期3（检验项目7~8）
		/*
		 * reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
		 * report_type, "INSPECTION_EVENTS", info.getInspection_events(),
		 * inspection_info_id); // 记事
		 */
	}
	
	// 保存车用气瓶安装监检项目表信息
	private void saveGLSJWJ_ItemValues(String report_type,
			InspectionZZJDInfo info, String inspection_info_id) {
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA401", "合格", inspection_info_id); // 设计文件检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA402", "合格", inspection_info_id); // 材料质量证明书检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA403", "合格", inspection_info_id); // 材料标记与标记移植检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA404", "无此项", inspection_info_id); // 焊接记录、热处理报告和记录检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA405", "无此项", inspection_info_id); // 无损检测报告检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA406", "合格", inspection_info_id); // 爆破试验及标定爆破压力检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA407", "合格", inspection_info_id); // 产品质量证明书检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA408", "合格", inspection_info_id); // 外观、标志检验结果
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPA409", "B", inspection_info_id); // 爆破试验及标定爆破压力
																	// 类别

		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB401", "图纸", inspection_info_id); // 设计文件工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB402", "质证书", inspection_info_id); // 材料质量证明书工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB403", "现场查看", inspection_info_id); // 材料标记与标记移植工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB404", "无此项", inspection_info_id); // 焊接记录、热处理报告和记录工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB405", "无此项", inspection_info_id);// 无损检测报告工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB406", "爆破记录单", inspection_info_id);// 爆破试验及标定爆破压力工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB407", "出厂资料", inspection_info_id);// 产品质量证明书工作见证
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "DTDQPB408", "铭牌", inspection_info_id); // 外观、标志工作见证

		String inspection_user_names = "";
		if (StringUtil.isNotEmpty(info.getInspection_user_name1())) {
			inspection_user_names += info.getInspection_user_name1();
		}
		if (StringUtil.isNotEmpty(info.getInspection_user_name2())) {
			inspection_user_names += info.getInspection_user_name2();
		}

		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR1", inspection_user_names,
				inspection_info_id); // 设计文件监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR2", inspection_user_names,
				inspection_info_id); // 材料质量证明书监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR3", inspection_user_names,
				inspection_info_id); // 材料标记与标记移植监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR4", inspection_user_names,
				inspection_info_id); // 焊接记录、热处理报告和记录监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR5", inspection_user_names,
				inspection_info_id); // 无损检测报告监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR6", inspection_user_names,
				inspection_info_id); // 爆破试验及标定爆破压力监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR7", inspection_user_names,
				inspection_info_id); // 产品质量证明书监检员
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "INSPECTION_OP_STR8", inspection_user_names,
				inspection_info_id); // 外观、标志监检员

		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "CONFIRM_DATE1", info.getConfirm_date1(),
				inspection_info_id); // 确认日期1（检验项目1~3）
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "CONFIRM_DATE2", info.getConfirm_date2(),
				inspection_info_id); // 确认日期2（检验项目4~6）
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				report_type, "CONFIRM_DATE3", info.getConfirm_date3(),
				inspection_info_id); // 确认日期3（检验项目7~8）
		/*
		 * reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
		 * report_type, "INSPECTION_EVENTS", info.getInspection_events(),
		 * inspection_info_id); // 记事
		 */
	}

	/**
	 * 查看报告录入信息
	 * 
	 * @param id --
	 *            制造监督检验主表ID
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getDetail(String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询制造监督检验主表
			InspectionZZJD inspectionZZJD = inspectionZZJDDao.get(id);
			// 查询制造监督检验明细表
			List<InspectionZZJDInfo> list = inspectoinZZJDInfoDao
					.getInspectionZZJDInfos(id);
			String contract_task_sn="";
			String contract_task_id="";
			for (InspectionZZJDInfo bean : list) {
				InspectionInfo info=inspectionInfoDao.get(bean.getFk_inspection_info_id());
				if(info!=null){
					contract_task_sn=info.getContract_task_sn();
					contract_task_id=info.getContract_task_id();
				}
			}
			map.put("data", inspectionZZJD);
			map.put("inspectionZZJDInfo", list);
			map.put("contract_task_sn", contract_task_sn);
			map.put("contract_task_id", contract_task_id);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
		}
		return map;
	}

	// 删除
	public void del(HttpServletRequest request, String id) {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		try {
			// 查询制造监督检验明细表
			List<InspectionZZJDInfo> list = inspectoinZZJDInfoDao
					.getInspectionZZJDInfos(id);
			if (!list.isEmpty()) {
				for (InspectionZZJDInfo inspectionZZJDInfo : list) {
					// 1、先删除业务信息表（tzsb_inspection_info）
					if (StringUtil.isNotEmpty(inspectionZZJDInfo
							.getFk_inspection_info_id())) {
						inspectionInfoDao
								.createSQLQuery(
										"update tzsb_inspection_info set data_status='99' where id = ? ",
										inspectionZZJDInfo
												.getFk_inspection_info_id())
								.executeUpdate();
					}
					// 2、删除制造监督检验明细表（tzsb_inspection_zzjd_info）
					inspectoinZZJDInfoDao
							.createSQLQuery(
									"update tzsb_inspection_zzjd_info set data_status='99' where id = ? ",
									inspectionZZJDInfo.getId()).executeUpdate();
					// 写入日志
					logService.setLogs(inspectionZZJDInfo
							.getFk_inspection_info_id(), "报告作废", "报告录入时，报告删除",
							user.getId(), user.getName(), new Date(), request
									.getRemoteAddr());
				}
			}
			// 2、删除制造监督检验主表（tzsb_inspection_zzjd）
			inspectoinZZJDInfoDao
					.createSQLQuery(
							"update tzsb_inspection_zzjd set data_status='99' where id = ? ",
							id).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	public boolean commit(String ids, HttpServletRequest request) {
		boolean isSuccess = true;
		String flow_code = "";
		String ywid = "";
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();

		try {
			CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
			User user = (User)curUser.getSysUser();
			Employee emp = (com.khnt.rbac.impl.bean.Employee)user.getEmployee();
			Org org = TS_Util.getCurOrg(curUser);
			
			String[] idArr = ids.split(",");
			for (int i = 0; i < idArr.length; i++) {
				InspectionZZJD inspectionZZJD = inspectionZZJDDao.get(idArr[i]);
				String infosql = "select info.id infoid,t.device_type_code,info.is_flow,info.is_report_input,t.device_type_code from tzsb_inspection_zzjd_info t,tzsb_inspection_info info where t.fk_inspection_info_id = info.id and t.fk_inspection_zzjd_id='"
						+ idArr[i]
						+ "' and t.data_status != '99' and info.data_status != '99' order by t.sn asc";
				List zzjd_info_list = inspectoinZZJDInfoDao.createSQLQuery(
						infosql).list();
				Object[] zzjd_info_arr = zzjd_info_list.toArray(); // 监督检验明细记录

				for (int j = 0; j < zzjd_info_list.size(); j++) {
					Object[] zzjd_info_obj = (Object[]) zzjd_info_arr[j];
					String device_type = zzjd_info_obj[1].toString().substring(0, 1);
					// 新设备目录，7310：安全阀，属于（F：安全附件）一类
					if ("7310".equals(zzjd_info_obj[1])) {
						device_type = "F";
					}
					// 获取当前用户所在部门是否存在已配置的相关（所报检设备的设备类型）流程
					String flow_hql = "select t.flow_id from FLOW_SERVICE_CONFIG t,BASE_UNIT_FLOW t1 where t.id=t1.FK_FLOW_ID and t1.DEVICE_TYPE like '"
							+ device_type
							+ "%' and t1.CHECK_TYPE = '"+inspectionZZJD.getCheck_type().trim()+"' and t.org_id='"
							+ org.getId() + "' and t1.FK_REPORT_ID='"+inspectionZZJD.getReport_type()+"'";
					List flow_list = inspectoinZZJDInfoDao.createSQLQuery(
							flow_hql).list();
					if (flow_list.size() > 0) {
						Object[] flow_arr = flow_list.toArray();
						flow_code = flow_arr[0].toString();
						ywid = zzjd_info_obj[0].toString();
						map.put("infoId", ywid); // 业务ID
						map.put("flowId", flow_code); // 工作流ID
						map.put("device_type_code", zzjd_info_obj[4]
										.toString()); // 设备类型编码
						map.put("report_type", inspectionZZJD.getReport_type()); // 报告类型ID
						map.put("check_type", inspectionZZJD.getCheck_type().trim()); // 检验类别
						// 判断该业务信息是否已启动流程（1：未进入流程 2：已进入流程）
						if (zzjd_info_obj[2] == null) {
							// 先判断业务是否已经启动了流程
							// 业务流程表有数据就说明已经启动了流程，不再重新启动流程，没数据就启动流程
							String process_id = inspectionDao.getProcess(ywid);
							if(StringUtil.isEmpty(process_id)){
								this.startFlowProcess(map, request);
							}
						} else {
							if ("".equals(String.valueOf(zzjd_info_obj[2]))
									|| "1".equals(String
											.valueOf(zzjd_info_obj[2]))) {
								this.startFlowProcess(map, request);
							} else {
								// 只有被退回的业务才能再次提交继续流程
								if (zzjd_info_arr[3].equals("1")) {
									map2.put("ins_info_id", ywid);
									com.khnt.bpm.core.bean.Process process = processManager
											.getServiceProcess(ywid);
									List<Activity> activities = activityManager
											.getCurrentServiceActivity(process
													.getBusinessId());
									if (activities.size() > 0) {
										map2.put("acc_id", activities.get(0)
												.getId());
										map2.put("flow_num", activities.get(0)
												.getActivityId());
									}
									map2.put("flag", "0"); // 如果flag是0就不用指定下一步操作人
									this.subFlowProcess(map2, request);
								}
							}
						}
					} else {
						isSuccess = false;
					}
					if (isSuccess) {
						inspectionZZJD.setCommit_date(new Date()); // 提交时间
						inspectionZZJDDao.save(inspectionZZJD);
					}
				}
			}
		} catch (Exception e) {
			isSuccess = false;
			e.printStackTrace();
			log.debug(e.toString());
		}
		return isSuccess;
	}

	/**
	 * 启动流程
	 * 
	 * @param map
	 *            业务信息参数
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void startFlowProcess(HashMap<String, String> map,
			HttpServletRequest request) {
		try {
			String infoId = map.get("infoId"); // 业务ID
			String flowId = map.get("flowId"); // 工作流ID
			InspectionInfo info = inspectionInfoDao.get(infoId);
			Map<String, Object> param = new HashMap<String, Object>();

			// 判断该业务信息是否已启动流程（1：未进入流程 2：已进入流程）
			if (StringUtil.isEmpty(info.getIs_flow())
					|| "1".equals(info.getIs_flow())) {
				// 获取流程名称
				String flow_name = flowDefManager.get(flowId).getFlowname();
				String flow_type = flowDefManager.get(flowId).getFlowtype();
				// 流程业务ID
				param.put(FlowExtWorktaskParam.SERVICE_ID, infoId);
				// 业务标题
				param.put(FlowExtWorktaskParam.SERVICE_TITLE, flow_name);
				// 流程ID
				param.put(FlowExtWorktaskParam.FLOW_DEFINITION_ID, flowId);
				// 第一个环节任务处理方式
				param.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, true);
				param.put(FlowExtWorktaskParam.SERVICE_TYPE, flow_type);
				// 启动流程
				Map<String, Object> flowMap = flowExtManager
						.startFlowProcess(param);
				// 获取下一步流程步骤
				List<Activity> list = (List) flowMap
						.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);
				// 流程ID插入业务表
				info.setFk_flow_index_id(flowId);
				// 更新业务信息表是否启动流程状态（1：未进入流程 2：已进入流程）
				info.setIs_flow("2");
				info.setFlow_note_id(list.get(0).getActivityId());
				info.setFlow_note_name(list.get(0).getName());
				info.setIs_back("0"); // 是否退回（默认的状态为1，提交后变成0，退回也变成1）
				
				synchronized (this){
					// 判断是否生成报告书编号
					// 如果是自编号，系统不自动生成报告编号，用户可修改；
					// 如果不是自编号，系统自动生成报告编号，用户不可修改；
					String is_self_sn = info.getIs_self_sn();	// 是否自编号（0：否 1：是）
					boolean get_sn = true;	// 是否由系统自动生成报告编号
					if(StringUtil.isNotEmpty(is_self_sn)){
						if("1".equalsIgnoreCase(is_self_sn)){
							get_sn = false;
						}
					}
					if(get_sn){
						// 生成报告书编号
						if (StringUtil.isEmpty(info.getReport_sn())) {
							// 1、获取设备类型、报告类型
							String device_type_code = map.get("device_type_code"); // 设备类型编码
							
							String report_type = map.get("report_type"); // 报告类型ID
							Report report = reportService.get(report_type);
							
							String check_type = map.get("check_type").trim(); // 检验类别
							// 存在检验类别，根据检验类别生成报告书编号
							// 不存在检验类别，根据报告书名称判断检验类别来生成报告书编号
							if (StringUtil.isNotEmpty(check_type)) {
								// 2、根据设备类型、检验类型、报告类型生成报告书编号
								Map<String, Object> reportSnMap = inspectionCommonService.createReportSn("saveZZJDRecord", infoId, report_type, check_type, device_type_code);
								String report_sn = String.valueOf(reportSnMap.get("report_sn"));
								info.setReport_sn(report_sn);
							} else {					
								if (report != null) {
									check_type = report.getReport_name().contains("制造")
											|| report.getReport_name().contains(
													"爆破片装置安全性能监督检验") || report.getReport_name().contains(
													"安全阀安全性能监督检验") ? "1" : "2";
									Map<String, Object> reportSnMap = inspectionCommonService.createReportSn("saveZZJDRecord", infoId, report_type, check_type, device_type_code);
									String report_sn = String.valueOf(reportSnMap.get("report_sn"));
									info.setReport_sn(report_sn);
								}
							}
						}
					}
					inspectionInfoDao.save(info);
				}
				// 记录日志
				String op_conclusion = "从【报告录入】环节进入【报告送审】环节。";
				CurrentSessionUser user = SecurityUtil.getSecurityUser();
				logService.setLogs(infoId, "报告录入", op_conclusion, user.getId(),
						user.getName(), new Date(), request.getRemoteAddr());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
		}
	}

	/**
	 * 提交流程
	 * 
	 * @param map
	 *            业务信息参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized void subFlowProcess(Map<String, Object> map,
			HttpServletRequest request) throws Exception {
		String infoId = map.get("ins_info_id").toString();
		String acc_id = map.get("acc_id").toString();
		String flow_num = map.get("flow_num").toString();
		String flag = map.get("flag").toString();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		JSONObject dataBus = new JSONObject();
		if ((!"0".equals(flag) && !"1".equals(flag))) {
			String next_sub_op = map.get("next_sub_op").toString();
			String next_op_name = map.get("next_op_name").toString();
			// 从数据总线中获取指定的下一步操作人
			JSONArray pts = new JSONArray();
			JSONObject pt = new JSONObject();
			pt.put("id", next_sub_op);
			pt.put("name", next_op_name);
			pts.put(pt);
			dataBus.put("paticipator", pts);
			paramMap.put(FlowExtWorktaskParam.DATA_BUS, dataBus);
		}

		InspectionInfo info = inspectionInfoDao.get(infoId);
		paramMap.put(FlowExtWorktaskParam.ACTIVITY_ID, acc_id);
		paramMap.put(FlowExtWorktaskParam.SERVICE_ID, infoId);

		if (info.getFlow_note_id().equals(flow_num)) {
			Map<String, Object> flowMap = flowExtManager
					.submitActivity(paramMap);
			// 获取下一步流程步骤
			List<Activity> list = (List) flowMap
					.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);

			// 记录日志
			String step_name = info.getFlow_note_name();
			String op_conclusion = "从【" + step_name + "】环节进入【"
					+ list.get(0).getName() + "】环节。";
			String revise_remark = map.get("revise_remark") == null ? "" : map
					.get("revise_remark").toString();
			if (!"".equals(revise_remark)) {
				op_conclusion = op_conclusion
						+ map.get("revise_remark").toString();
			}
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			info.setFlow_note_id(list.get(0).getActivityId());
			info.setFlow_note_name(list.get(0).getName());
			info.setIs_back("0"); // 提交后状态变成0
			inspectionInfoDao.save(info);
			try {
				logService.setLogs(infoId, step_name, op_conclusion, user
						.getId(), user.getName(), new Date(), request
						.getRemoteAddr());
				if ("报告送审".equals(step_name) || "报告审核".equals(step_name)) {
					String to_user_name = "提交至"
							+ map.get("next_op_name").toString();
					logService.setLogs(infoId, "【" + step_name + "】环节提交",
							to_user_name, user.getId(), user.getName(),
							new Date(), request.getRemoteAddr());
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.debug(e.toString());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getFlowInfo() throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		List list = inspectionInfoDao
				.createSQLQuery(
						"SELECT  T.ACTIVITY_NAME,subStr(t.TITLE,0, instr(t.TITLE,'-')-1) as flowName,count(t.id) as num, a.activity_id,a.function, b.fk_flow_index_id"
								+ "  FROM V_PUB_WORKTASK T, TZSB_INSPECTION_INFO b,flow_activity a where "
								+ " t.SERVICE_ID = b.id and b.flow_note_id = a.activity_id and a.id = t.ACTIVITY_ID "
								+ " and b.fk_inspection_id is null"
								+ " and T.STATUS='0' and b.data_status<>'99' and t.HANDLER_ID='"
								+ user.getUserId()
								+ "'"
								+ " group by t.ACTIVITY_NAME, a.function,a.activity_id, b.fk_flow_index_id,subStr(t.TITLE,0, instr(t.TITLE,'-')-1)")
				.list();
		list.size();
		map.put("flowData", list);
		map.put("success", true);
		return map;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> flow_reportInput(Map map) throws Exception {
		HashMap<String, Object> listMap = new HashMap<String, Object>();
		// 获取业务信息表
		InspectionInfo info = inspectionInfoDao.get(map.get("id").toString());
		// 获取报告表
		List<ReportItem> itemlist = itemDao.createQuery(
				"from ReportItem where fk_reports_id='"
						+ map.get("report_type").toString()
						+ "' order by page_index asc").list();
		// 获取报告配置项目表
		List<ReportPerAudit> perlist = perDao.createQuery(
				"from ReportPerAudit t where t.fk_inspection_info_id='"
						+ map.get("id").toString() + "'").list();

		// 查询配置页面是否许选择审核、签发人员
		HashMap mapManName = new HashMap();
		HashMap mapManId = new HashMap();
		for (int i = 0; i < perlist.size(); i++) {
			ReportPerAudit item = perlist.get(i);
			mapManName.put(item.getItem_name(), item.getItem_value());
			mapManId.put(item.getItem_name(), item.getItem_value_id());
		}

		// 循环剪切报告配置项目
		String[] report_item = new String[1];
		String tt = info.getReport_item();
		// 判断是否有值
		if (info.getReport_item() != null) {
			// 判断是否有逗号
			if (info.getReport_item().indexOf(",") != -1) {
				report_item = info.getReport_item().split(",");
			} else {
				report_item[0] = tt;
			}
		}

		List<SetReportItem> setList = new ArrayList<SetReportItem>();
		// 配置页面显示
		for (int i = 0; i < itemlist.size(); i++) {
			SetReportItem item = new SetReportItem();
			// 判断页面是否必须选择
			if ("1".endsWith(itemlist.get(i).getIs_must())) {
				item.setIs_must("yes");
				item.setPage_index(itemlist.get(i).getPage_index());
				item.setIndex_text(itemlist.get(i).getItme_name() + "-------"
						+ itemlist.get(i).getPage_index());

			} else {
				item.setIs_must("no");
				String disSelect = "";
				if (report_item != null) {
					for (int j = 0; j < report_item.length; j++) {

						if (itemlist.get(i).getPage_index().equals(
								report_item[j])) {

							disSelect = "checked";
							break;
						}

					}
				}
				item.setIs_disCheck(disSelect);
				item.setPage_index(itemlist.get(i).getPage_index());
				item.setIndex_text(itemlist.get(i).getItme_name() + "-------"
						+ itemlist.get(i).getPage_index());

			}
			// 判断是否有检验员
			if ("2".endsWith(itemlist.get(i).getIs_inspect_man())) {
				item.setIs_inspection("2");
				item.setPage_inspection_op("");
			} else {
				item.setIs_inspection("1");
				item.setPage_inspection_op(mapManName.get("INSPECT_MAN_PTR"
						+ (i + 1)) == null ? "" : mapManName.get(
						"INSPECT_MAN_PTR" + (i + 1)).toString());
				item.setPage_inspection_id(mapManId.get("INSPECT_MAN_PTR"
						+ (i + 1)) == null ? "" : mapManId.get(
						"INSPECT_MAN_PTR" + (i + 1)).toString());
				item.setInspect_seq(Integer.valueOf(itemlist.get(i)
						.getPage_index()));
			}

			// 判断是否有审核人员
			if ("2".endsWith(itemlist.get(i).getIs_inspect_man())) {
				item.setIs_check("2");
				item.setPage_inspection_op("");
			} else {
				item.setIs_check("1");
				item.setAudit_seq(Integer.valueOf(itemlist.get(i)
						.getPage_index()));
				item
						.setPage_check_op(mapManName.get("AUDIT_MAN_PTR"
								+ (i + 1)) == null ? "" : mapManName.get(
								"AUDIT_MAN_PTR" + (i + 1)).toString());
				item
						.setPage_check_id(mapManId.get("AUDIT_MAN_PTR"
								+ (i + 1)) == null ? "" : mapManId.get(
								"AUDIT_MAN_PTR" + (i + 1)).toString());
			}
			setList.add(item);
		}

		listMap.put("reportItems", setList);
		listMap.put("status", "add");
		listMap.put("deviceSort", map.get("device_type_code").toString()
				.substring(0, 1));
		listMap.put("success", true);
		return listMap;
	}

	/**
	 * 
	 * 保存报告配置信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveItem(Map<String, Object> map) {
		try {
			String id = map.get("id").toString();
			String report_item = map.get("report_item").toString();
			String xsqnum = map.get("xsqnum").toString();

			JSONObject dataMap = JSONObject.fromString(map.get("dataMap")
					.toString());
			JSONArray inspect_seq;
			JSONArray audit_seq;
			JSONArray inspect_man;
			JSONArray inspect_man_id;
			JSONArray audit_man;
			JSONArray audit_man_id;
			// 判断是否有数据
			inspect_seq = getArrayByKey(dataMap, "inspect_seq");
			audit_seq = getArrayByKey(dataMap, "audit_seq");
			inspect_man = getArrayByKey(dataMap, "page_inspection_op");
			inspect_man_id = getArrayByKey(dataMap, "page_inspection_id");
			audit_man = getArrayByKey(dataMap, "page_check_op");
			audit_man_id = getArrayByKey(dataMap, "page_check_id");

			String report_type = dataMap.get("report_type").toString();
			// 判断检验人员是否勾选
			if (inspect_seq != null) {
				for (int i = 0; i < inspect_seq.length(); i++) {
					boolean flag = false;
					String item_id = "";
					String item_name = "INSPECT_MAN_PTR" + inspect_seq.get(i);

					List<ReportItemValue> list = reportItemValueDao
							.createQuery(
									"from ReportItemValue where "
											+ "fk_report_id='" + report_type
											+ "' and fk_inspection_info_id="
											+ "'" + id + "' and item_name='"
											+ item_name
											+ "' and item_type='String'")
							.list();
					if (list.size() > 0) {
						flag = true;
						item_id = list.get(0).getId();
					}
					String tt = inspect_man.get(i).toString();
					String tt_id = inspect_man_id.get(i).toString();
					if (tt.indexOf("，") != -1) {
						tt = tt.replace("，", ",");
					}
					if (tt_id.indexOf("，") != -1) {
						tt_id = tt_id.replace("，", ",");
					}
					// 判断报告参数是否有数据，有就更新数据，没有就插入新数据
					if (flag) {
						reportItemValueDao.createSQLQuery(
								"update tzsb_report_item_value set fk_report_id='"
										+ report_type
										+ "', fk_inspection_info_id='" + id
										+ "', item_name='" + item_name
										+ "',item_value='" + tt
										+ "',item_type='String' where id='"
										+ item_id + "'").executeUpdate();

					} else {
						ReportItemValue itemValue = new ReportItemValue();
						itemValue.setFk_report_id(report_type);
						itemValue.setFk_inspection_info_id(id);
						itemValue.setItem_name(item_name);
						itemValue.setItem_value(tt);
						itemValue.setItem_type("String");
						reportItemValueDao.save(itemValue);
					}

					// 先删除报告人员记录表后再保存
					ReportPerAudit per = new ReportPerAudit();
					perDao.createSQLQuery(
							"delete  base_reports_per_audit where fk_report_id='"
									+ report_type
									+ "' and fk_inspection_info_id='" + id
									+ "' and item_name='" + item_name + "'")
							.executeUpdate();
					per.setFk_inspection_info_id(id);
					per.setFk_report_id(report_type);
					per.setItem_value(tt);
					per.setItem_name(item_name);
					per.setItem_value_id(tt_id);
					perDao.save(per);
				}
			}

			// 判断审核人员是否勾选
			if (audit_seq != null) {
				for (int i = 0; i < audit_seq.length(); i++) {
					boolean flag = false;
					String item_id = "";
					String item_name = "AUDIT_MAN_PTR" + audit_seq.get(i);
					List<ReportItemValue> list = reportItemValueDao
							.createQuery(
									"from ReportItemValue where "
											+ "fk_report_id='" + report_type
											+ "' and fk_inspection_info_id="
											+ "'" + id + "' and item_name='"
											+ item_name
											+ "' and item_type='String'")
							.list();
					if (list.size() > 0) {
						flag = true;
						item_id = list.get(0).getId();
					}
					// 判断报告参数是否有数据，有就更新数据，没有就插入新数据
					if (flag) {
						reportItemValueDao.createSQLQuery(
								"update tzsb_report_item_value set fk_report_id='"
										+ report_type
										+ "', fk_inspection_info_id='" + id
										+ "', item_name='" + item_name
										+ "',item_value='"
										+ audit_man.get(i).toString()
										+ "',item_type='String' where id='"
										+ item_id + "'").executeUpdate();
					} else {
						ReportItemValue itemValue = new ReportItemValue();
						itemValue.setFk_report_id(report_type);
						itemValue.setFk_inspection_info_id(id);
						itemValue.setItem_name(item_name);
						itemValue.setItem_value(audit_man.get(i).toString());
						itemValue.setItem_type("String");
						reportItemValueDao.save(itemValue);
					}

					// 先删除报告人员记录表后再保存
					ReportPerAudit per = new ReportPerAudit();
					perDao.createSQLQuery(
							"delete  base_reports_per_audit where fk_report_id='"
									+ report_type
									+ "' and fk_inspection_info_id='" + id
									+ "' and item_name='" + item_name + "'")
							.executeUpdate();
					per.setFk_inspection_info_id(id);
					per.setFk_report_id(report_type);
					per.setItem_value(audit_man.get(i).toString());
					per.setItem_name(item_name);
					per.setItem_value_id(audit_man_id.get(i).toString());
					perDao.save(per);
				}
			}

			// 更新业务信息表的报告页码
			inspectionInfoDao.createSQLQuery(
					"update tzsb_inspection_info set report_item='"
							+ report_item + "',xsqts='" + xsqnum
							+ "' where id='" + id + "'").executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
		}
	}

	public JSONArray getArrayByKey(JSONObject dataMap, String key) {
		JSONArray ret = null;
		try {
			ret = dataMap.getJSONArray(key);
		} catch (Exception e) {
			try {
				String tt = dataMap.get(key).toString();
				if (tt.split(",").length > 1) {
					tt = tt.replace(",", "，");
				}
				ret = JSONArray.fromObject("[" + tt + "]");
			} catch (Exception e2) {
				ret = null;
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getReportInfo(String ins_info_id)
			throws Exception {
		try {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			Map<String, Object> infoMap = new HashMap<String, Object>(); // 业务信息
			Map<String, Object> repMap = new HashMap<String, Object>(); // 报告信息
			Map<String, Object> zzjdInfoMap = new HashMap<String, Object>(); // 监督检验明细数据
			Map<String,Object> imgMap  = new HashMap<String,Object>();

			InspectionInfo inspectionInfo = inspectionInfoDao.get(ins_info_id);
			InspectionZZJDInfo inspectionZZJDInfo = inspectoinZZJDInfoDao
					.getByInfoId(ins_info_id);

			// 是否复制报告
			String iscopy = inspectionInfo.getIs_copy();
			// 下次检验日期加一年并减一天
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(inspectionInfo.getAdvance_time());
			calendar.add(Calendar.YEAR, 1);
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
			inspectionInfo.setLast_check_time(calendar.getTime());
			Report report = reportService.get(inspectionInfo.getReport_type());

			infoMap = beanToMap(inspectionInfo);
			repMap = beanToMap(report);
			zzjdInfoMap = beanToMap(inspectionZZJDInfo);

			// 获取报名检验项目明细表数据
			// 2016-11-28修改，批量上传的报告都要获取，不仅局限于以下三种类型的报告，因报告都可能修改生成ItemValue数据
			List<ReportItemValue> reportItemValueList = new ArrayList<ReportItemValue>();
			/*if (report.getReport_name().contains("车用气瓶安装监督")
					|| report.getReport_name().contains("爆破片装置安全性能监督")
					|| report.getReport_name().contains("锅炉设计文件")) {*/
				reportItemValueList = getReportItemValueInfo(ins_info_id,
						StringUtil.transformNull(inspectionInfo
								.getReport_type()));
			//}
			
			// 电子签名
    		// 检验人员电子签名
    		imgMap.put("check_op_img", imageTool.getEmployeeImg(inspectionInfo.getCheck_op_id()));
    		// 审核人员电子签名
    		imgMap.put("examine_op_img", imageTool.getEmployeeImg(inspectionInfo.getExamine_id()));
    		// 签发（批准）人员电子签名
    		imgMap.put("issue_op_img", imageTool.getEmployeeImg(inspectionInfo.getIssue_id()));
    		// 编制（录入）人员电子签名
    		imgMap.put("enter_op_img", imageTool.getEmployeeImg(inspectionInfo.getEnter_op_id()));   	
    		

			// 开始打包返回数据
			// 以下部分为报告模版设置时固定需要的内容
			Map<String, Object> paraMap = new HashMap<String, Object>();
			if (StringUtil.isNotEmpty(inspectionInfo.getReport_item())) {
				paraMap.put("TotalP", (inspectionInfo.getReport_item()
						.split(",")).length);
				paraMap.put("report_item", inspectionInfo.getReport_item());
				paraMap.put("total_page", (inspectionInfo.getReport_item()
						.split(",")).length);
			} else {
				paraMap.put("TotalP", 1);
				paraMap.put("report_item", "1");
				paraMap.put("total_page", 1);
			}

			paraMap.put("report_root_path", report.getRootpath()); // 报告模板所在目录
			paraMap.put("report_file", report.getReport_file()); // 报告模板文件名
			paraMap.put("MRDataSet", report.getMrdataset()); // 数据集
			returnMap.put("ISCOPY", iscopy); // 是否是复制报告
			returnMap.put("INSPECTIONINFO", infoMap); // 业务信息数据集
			returnMap.put("INSPECTIONZZJDINFO", zzjdInfoMap); // 监督检验明细数据集
			returnMap.put("REPORTITEMVALUE", reportItemValueList); // 检验项目明细数据集
			returnMap.put("REPORT", repMap); // 报告模板信息数据集
			returnMap.put("REPORTPARA", paraMap); // 报告模板参数集
			returnMap.put("IMAGES", imgMap);
			return returnMap;
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> beanToMap(Object obj) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] field = obj.getClass().getDeclaredFields();
		for (Field fd : field) {
			if (fd.getName().equals("serialVersionUID"))
				continue;
			try {
				//System.out.println(changFisrtUpper(fd.getName()));
				//System.out.println("get" + changFisrtUpper(fd.getName()));
				Method method = obj.getClass().getMethod(
						"get" + changFisrtUpper(fd.getName()), null);

				if (method != null) {
					method.getName();
					Object obj1 = method.invoke(obj, null);
					if (fd.getName().equals("advance_time")) {
						map.put(fd.getName().toUpperCase(), DateUtil
								.getDateTime("yyyy-MM-dd", (Date) obj1));
					} else if (fd.getName().equals("last_check_time")) {
						map.put(fd.getName().toUpperCase(), DateUtil
								.getDateTime("yyyy-MM-dd", (Date) obj1));
					} else {
						map.put(fd.getName().toUpperCase(), obj1);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.debug(e.toString());
			}
		}
		return map;
	}

	private static String changFisrtUpper(String str) {
		return str.substring(0, 1).toUpperCase()
				+ str.substring(1, str.length());
	}

	@SuppressWarnings("unchecked")
	public List getReportItemValueInfo(String ins_info_id, String report_type)
			throws Exception {
		try {
			String sql = " from ReportItemValue where fk_inspection_info_id=?"
					+ " and fk_report_id=? ";
			List list = inspectionInfoDao.createQuery(sql,
					new String[] { ins_info_id, report_type }).list();
			return list;
		} catch (Exception e) {
			throw e;
		}
	}

	// 报告作废
	public void delReport(String ids, HttpServletRequest request)
			throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String id[] = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			InspectionInfo info = inspectionInfoDao.get(id[i]);
			String op_conclusion = "从" + info.getFlow_note_name()
					+ "进入【报告作废】环节。";
			inspectionInfoDao
					.createSQLQuery(
							"update  TZSB_INSPECTION_INFO  set data_status='99' where id =?",
							id[i]).executeUpdate(); // 更新业务信息数据状态为已删除（data_status='99'）
			inspectionInfoDao
					.createSQLQuery(
							"update  tzsb_inspection_zzjd_info  set data_status='99' where fk_inspection_info_id =?",
							id[i]).executeUpdate(); // 更新业务明细数据状态为已删除（data_status='99'）
			if (StringUtil.isNotEmpty(info.getReport_sn())) {
				// 根据报告编号获取在用报告业务信息
				List<InspectionInfo> list = inspectionInfoDao.queryByReport_sn(info.getReport_sn());
				boolean can_add = true;
				for(InspectionInfo inspectionInfo : list){
					if(!inspectionInfo.getId().equals(info.getId())){
						can_add = false;
					}
				}
				if(can_add){
					inspectionInfoDao.createSQLQuery(
							"insert into  TZSB_DEL_CODE(ID,REPORT_TYPE,REPORT_SN)  VALUES('"
									+ StringUtil.getUUID() + "','"
									+ info.getReport_type() + "','"
									+ info.getReport_sn() + "')").executeUpdate();
				}
			}

			// 写入日志
			logService.setLogs(id[i], "报告作废", op_conclusion, user.getId(), user
					.getName(), new Date(), request.getRemoteAddr());
		}
	}

	/**
	 * 保存报告
	 * 
	 * @param PostBackValue
	 * @param engine
	 * @param report_type
	 * @param ins_info_id
	 * @throws Exception
	 * @author GaoYa
	 */
	@SuppressWarnings("unchecked")
	public void saveReport(String[] PostBackValue, MREngine engine,
			String report_type, String ins_info_id) {
		try {

			// 根据业务信息ID获取业务信息bean
			InspectionInfo insInfo = inspectionInfoDao.get(ins_info_id);
			// 根据业务信息ID获取制造监督检验明细bean
			InspectionZZJDInfo inspectionZZJDInfo = inspectoinZZJDInfoDao
					.getByInfoId(ins_info_id);
			// 获取报告类型信息
			Report report = reportService.get(report_type);
			
			// 获取检验类别
			String check_type = "";
			InspectionZZJD inspectionZZJD = inspectionZZJDInfo.getInspectionZZJD();
			if(inspectionZZJD!=null){
				if(StringUtil.isNotEmpty(inspectionZZJD.getCheck_type())){
					check_type = inspectionZZJD.getCheck_type();
				}
			}

			// 获取设备类型
			String device_type = inspectionZZJDInfo.getDevice_type_code();
			// 获取本体耗钢量（吨）（用于锅炉设计文件鉴定报告收费金额计算）
			String weight_steels = inspectionZZJDInfo.getWeight_steels();
			// 获取蒸发量（即额定出力（t/h）或（MW），用于锅炉设计文件鉴定报告收费金额计算）
			String rated_output = inspectionZZJDInfo.getRated_output();
			// 获取蒸发量单位（即额定出力（t/h）或（MW），用于锅炉设计文件鉴定报告收费金额计算）
			String rated_output_type = "";
			// 获取额定工作压力（即额定出口压力（MPa），用于锅炉设计文件鉴定报告收费金额计算）
			String device_pressure = inspectionZZJDInfo.getDevice_pressure();

			// 首先删除tzsb_report_item_value表中之前保存的数据
			// 报告书编号不删除
			String delSql = "delete from tzsb_report_item_value where "
					+ " fk_inspection_info_id = ? "
					+ " and upper(item_name) <> 'REPORT_SN' ";
			inspectionInfoDao.createSQLQuery(delSql, ins_info_id)
					.executeUpdate();

			// 开始处理报表内容数据
			for (int i = 0; i < PostBackValue.length; i++) {
				// 如果为空则不处理
				// 2016-11-28修改，如果内容为空也要处理，因用户可能错误上传后需要修改报告时做删除处理（锅炉部邹益平要求）
				if (StringUtil
						.isNotEmpty(engine.getParameter(PostBackValue[i]))
						|| (PostBackValue[i].toUpperCase().equals(
								"DEVICE_PROCESSING") || PostBackValue[i]
								.toUpperCase().equals("DEVICE_PROCESSING2"))) {
					// 将制造单位写入检验业务信息表和制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
									"MADE_UNIT_NAME")) {
						if(StringUtil.isEmpty(insInfo.getReport_com_name())){
							insInfo.setReport_com_name(engine
									.getParameter(PostBackValue[i]));
						}
						if(StringUtil.isEmpty(inspectionZZJDInfo.getCom_name())){
							inspectionZZJDInfo.setCom_name(engine
									.getParameter(PostBackValue[i]));
						}
						inspectionZZJDInfo.setMade_unit_name(engine
								.getParameter(PostBackValue[i]));
					}
					// 将使用单位写入检验业务信息表和制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("COM_NAME")) {
						if(StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))){
							insInfo.setReport_com_name(engine
									.getParameter(PostBackValue[i]));
							inspectionZZJDInfo.setCom_name(engine
									.getParameter(PostBackValue[i]));
						}
					}
					// 将制造单位机构代码写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("MADE_UNIT_CODE")) {
						inspectionZZJDInfo.setMade_unit_code(engine
								.getParameter(PostBackValue[i]));
					}
					// 将制造许可证级别写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"MADE_LICENSE_LEVEL")) {
						inspectionZZJDInfo.setMade_license_level(engine
								.getParameter(PostBackValue[i]));
					}

					// 将制造许可证编号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"MADE_LICENSE_CODE")) {
						inspectionZZJDInfo.setMade_license_code(engine
								.getParameter(PostBackValue[i]));
					}

					// 将安装单位写入检验业务信息表和制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"INSTALL_UNIT_NAME")) {
						insInfo.setReport_com_name(engine
								.getParameter(PostBackValue[i]));
						inspectionZZJDInfo.setInstall_unit_name(engine
								.getParameter(PostBackValue[i]));
					}

					// 将安装许可证编号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"INSTALL_LICENSE_CODE")) {
						inspectionZZJDInfo.setInstall_license_code(engine
								.getParameter(PostBackValue[i]));
					}

					// 将安装日期写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("INSTALL_DATE")) {
						String install_date = engine
								.getParameter(PostBackValue[i]);
						if (StringUtil.isNotEmpty(install_date)) {
							inspectionZZJDInfo.setInstall_date(install_date);
						} else {
							inspectionZZJDInfo.setInstall_date("");
						}

					}

					// 将施工/建设单位写入检验业务信息表和制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"CONSTRUCTION_UNIT_NAME")) {
						insInfo.setReport_com_name(engine
								.getParameter(PostBackValue[i]));
						inspectionZZJDInfo.setConstruction_unit_name(engine
								.getParameter(PostBackValue[i]));
					}
					// 将施工单位组织机构代码写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"CONSTRUCTION_UNIT_CODE")) {
						inspectionZZJDInfo.setConstruction_unit_code(engine
								.getParameter(PostBackValue[i]));
					}
					// 将改造修理许可级别写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"CONSTRUCTION_LICENSE_LEVEL")) {
						inspectionZZJDInfo.setConstruction_license_level(engine
								.getParameter(PostBackValue[i]));
					}

					// 将改造修理许可证编号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"CONSTRUCTION_LICENSE_CODE")) {
						inspectionZZJDInfo.setConstruction_license_code(engine
								.getParameter(PostBackValue[i]));
					}

					// 将施工类别写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"CONSTRUCTION_TYPE")) {
						inspectionZZJDInfo.setConstruction_type(engine
								.getParameter(PostBackValue[i]));
					}

					// 将竣工日期写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"REPAIR_FINISH_DATE")) {
						String repair_finish_date = engine
								.getParameter(PostBackValue[i]);
						if (StringUtil.isNotEmpty(repair_finish_date)) {
							inspectionZZJDInfo
									.setRepair_finish_date(repair_finish_date);
						} else {
							inspectionZZJDInfo.setRepair_finish_date("");
						}
					}

					// 将使用单位组织机构代码写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("COM_CODE")) {
						inspectionZZJDInfo.setCom_code(engine
								.getParameter(PostBackValue[i]));
					}

					// 将使用登记证编号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"COM_REGISTRATION_NUM")) {
						inspectionZZJDInfo.setCom_registration_num(engine
								.getParameter(PostBackValue[i]));
					}

					// 将设备使用地点写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"DEVICE_USE_PLACE")) {
						inspectionZZJDInfo.setDevice_use_place(engine
								.getParameter(PostBackValue[i]));
					}

					// 将设备类型写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"DEVICE_TYPE_CODE")) {
						inspectionZZJDInfo.setDevice_type_code(engine
								.getParameter(PostBackValue[i]));
					}

					// 将设备类型名称写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"DEVICE_TYPE_NAME")) {
						inspectionZZJDInfo.setDevice_type_name(engine
								.getParameter(PostBackValue[i]));
					}

					// 将设备名称写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_NAME")) {
						inspectionZZJDInfo.setDevice_name(engine
								.getParameter(PostBackValue[i]));
					}

					// 将产品编号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_NO")) {
						inspectionZZJDInfo.setDevice_no(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将产品编号2写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_NO_2")) {
						inspectionZZJDInfo.setDevice_no_2(engine
								.getParameter(PostBackValue[i]));
					}

					// 将产品批号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"DEVICE_BATCH_NUM")) {
						inspectionZZJDInfo.setDevice_batch_num(engine
								.getParameter(PostBackValue[i]));
					}

					// 将产品型号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_MODEL")) {
						inspectionZZJDInfo.setDevice_model(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将产品型号2写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_MODEL_2")) {
						inspectionZZJDInfo.setDevice_model_2(engine
								.getParameter(PostBackValue[i]));
					}

					// 将产品数量写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_COUNT")) {
						inspectionZZJDInfo.setDevice_count(engine
								.getParameter(PostBackValue[i]));
					}

					// 将产品标准写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase()
							.equals("DEVICE_STANDARD")) {
						inspectionZZJDInfo.setDevice_standard(engine
								.getParameter(PostBackValue[i]));
					}

					// 将材料牌号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("MATERIAL_NUM")) {
						inspectionZZJDInfo.setMaterial_num(engine
								.getParameter(PostBackValue[i]));
					}

					// 将设备代码写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_CODE")) {
						inspectionZZJDInfo.setDevice_code(engine
								.getParameter(PostBackValue[i]));
					}

					// 将确认日期1写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("CONFIRM_DATE1")) {
						inspectionZZJDInfo.setConfirm_date1(engine
								.getParameter(PostBackValue[i]));
					}

					// 将确认日期2写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("CONFIRM_DATE2")) {
						inspectionZZJDInfo.setConfirm_date2(engine
								.getParameter(PostBackValue[i]));
					}

					// 将确认日期3写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("CONFIRM_DATE3")) {
						inspectionZZJDInfo.setConfirm_date3(engine
								.getParameter(PostBackValue[i]));
					}

					// B300：封头
					if ("B300".equals(device_type)) {
						// 将来料加工（是）写入制造监督检验明细表
						if (PostBackValue[i].toUpperCase().equals(
								"DEVICE_PROCESSING")) {
							String value = engine
									.getParameter(PostBackValue[i].toUpperCase());
							if (StringUtil.isNotEmpty(value) && "√".equals(value)) {
								inspectionZZJDInfo.setDevice_processing(value);
								inspectionZZJDInfo.setDevice_processing2("");
							} else {
								inspectionZZJDInfo.setDevice_processing("");
							}
						}

						// 将来料加工（否）写入制造监督检验明细表
						if (PostBackValue[i].toUpperCase().equals(
								"DEVICE_PROCESSING2")) {
							String value = engine
									.getParameter(PostBackValue[i].toUpperCase());
							if (StringUtil.isNotEmpty(value) && "√".equals(value)) {
								inspectionZZJDInfo.setDevice_processing2(value);
								inspectionZZJDInfo.setDevice_processing("");
							} else {
								inspectionZZJDInfo.setDevice_processing2("");
							}
						}
					}

					// 将监检所抽的产品编号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase()
							.equals("CHECK_DEVICE_NO")) {
						inspectionZZJDInfo.setCheck_device_no(engine
								.getParameter(PostBackValue[i]));
					}

					// 将本证书适用的产品编号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase()
							.equals("TRIAL_DEVICE_NO")) {
						inspectionZZJDInfo.setTrial_device_no(engine
								.getParameter(PostBackValue[i]));
					}

					// 将气瓶文件审查写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"DEVICE_FILE_CHECK")) {
						inspectionZZJDInfo.setDevice_file_check(engine
								.getParameter(PostBackValue[i]));
					}

					// 将文件审查日期写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase()
							.equals("FILE_CHECK_DATE")) {
						inspectionZZJDInfo.setFile_check_date(engine
								.getParameter(PostBackValue[i]));
					}

					// 将设计单位写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"DESIGN_UNIT_NAME")) {
						inspectionZZJDInfo.setDesign_unit_name(engine
								.getParameter(PostBackValue[i]));
					}

					// 将设计单位机构代码写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"DESIGN_UNIT_CODE")) {
						inspectionZZJDInfo.setDesign_unit_code(engine
								.getParameter(PostBackValue[i]));
					}

					// 将设计许可证编号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"DESIGN_LICENSE_CODE")) {
						inspectionZZJDInfo.setDesign_license_code(engine
								.getParameter(PostBackValue[i]));
					}

					// 将产品图号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_PIC_NO")) {
						inspectionZZJDInfo.setDevice_pic_no(engine
								.getParameter(PostBackValue[i]));
					}

					// 将设计日期写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DESIGN_DATE")) {
						String design_date = engine
								.getParameter(PostBackValue[i]);
						if (StringUtil.isNotEmpty(design_date)) {
							inspectionZZJDInfo.setDesign_date(design_date);
						} else {
							inspectionZZJDInfo.setDesign_date("");
						}
					}

					// 将制造日期写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("MADE_DATE")) {
						String made_date = engine
								.getParameter(PostBackValue[i]);
						if (StringUtil.isNotEmpty(made_date)) {
							inspectionZZJDInfo.setMade_date(made_date);
						} else {
							inspectionZZJDInfo.setMade_date("");
						}

					}

					// 将有关安全技术监察规程写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"SAFELY_TECH_STANDARD")) {
						inspectionZZJDInfo.setSafely_tech_standard(engine
								.getParameter(PostBackValue[i]));
					}

					// 将记事写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals(
							"INSPECTION_EVENTS")) {
						inspectionZZJDInfo.setInspection_events(engine
								.getParameter(PostBackValue[i]));
					}

					// 将备注写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("REMARK")) {
						inspectionZZJDInfo.setRemark(engine
								.getParameter(PostBackValue[i]));
					}

					// 将发动机编号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("ENGINE_NO")) {
						inspectionZZJDInfo.setEngine_no(engine
								.getParameter(PostBackValue[i]));
					}

					// 将车辆识别代码写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_CAR_NUM")) {
						inspectionZZJDInfo.setDevice_car_num(engine
								.getParameter(PostBackValue[i]));
					}

					// 将设计压力写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase()
							.equals("DEVICE_PRESSURE")) {
						inspectionZZJDInfo.setDevice_pressure(engine
								.getParameter(PostBackValue[i]));
						if(StringUtil.isEmpty(device_pressure)){
							device_pressure = engine
									.getParameter(PostBackValue[i]).trim();
						}
					}

					// 将设计温度写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("USE_TEMP")) {
						inspectionZZJDInfo.setUse_temp(engine
								.getParameter(PostBackValue[i]));
					}

					// 将设计介质写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_MEDIUM")) {
						inspectionZZJDInfo.setDevice_medium(engine
								.getParameter(PostBackValue[i]));
					}

					// 将设计容积写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_VOLUME")) {
						inspectionZZJDInfo.setDevice_volume(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将设备材料写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_METERIAL")) {
						inspectionZZJDInfo.setDevice_meterial(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将设备长度写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_LENGTH")) {
						inspectionZZJDInfo.setDevice_length(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将工程名称写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("PROJECT_NAME")) {
						inspectionZZJDInfo.setProject_name(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将出厂编号（1）写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("FACTORY_CODE_1")) {
						inspectionZZJDInfo.setFactory_code_1(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将出厂编号（2）写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("FACTORY_CODE_2")) {
						inspectionZZJDInfo.setFactory_code_2(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将改造与重大修理项目写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("REPAIR_PROJECT")) {
						inspectionZZJDInfo.setRepair_project(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对安装单位质量体系运转情况的评价写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("INSTALL_EVALUATE")) {
						inspectionZZJDInfo.setInstall_evaluate(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对安装竣工资料审查日期写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("FINISH_CHECK_DATE")) {
						inspectionZZJDInfo.setFinish_check_date(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对安装竣工资料审查写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("FINISH_DATA_CHECK")) {
						inspectionZZJDInfo.setFinish_data_check(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对泄漏试验日期写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("LEAK_CHECK_DATE")) {
						inspectionZZJDInfo.setLeak_check_date(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对泄漏试验确认写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("LEAK_TEST_CHECK")) {
						inspectionZZJDInfo.setLeak_test_check(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对安装质量检查日期写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("QUALITY_CHECK_DATE")) {
						inspectionZZJDInfo.setQuality_check_date(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对安装质量检查写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("INSTALL_QUALITY_CHECK")) {
						inspectionZZJDInfo.setInstall_quality_check(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对外观检查日期写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("SURFACE_CHECK_DATE")) {
						inspectionZZJDInfo.setSurface_check_date(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对气瓶外观检查写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DEVICE_SURFACE_CHECK")) {
						inspectionZZJDInfo.setDevice_surface_check(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对资料审查日期写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DATA_CHECK_DATE")) {
						inspectionZZJDInfo.setData_check_date(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对气瓶外观检查写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("INSTALL_DATA_CHECK")) {
						inspectionZZJDInfo.setInstall_data_check(engine
								.getParameter(PostBackValue[i]));
					}

					// 检验结论写入到主表中和业务信息表
					/*
					 * if (PostBackValue[i].toUpperCase().equals(
					 * "INSPECTION_CONCLUSION")) {
					 * insInfo.setInspection_conclusion(engine
					 * .getParameter(PostBackValue[i])); }
					 */

					// 编制日期写入业务信息表
					/*
					 * if (PostBackValue[i].toUpperCase().equals("DRAFT_DATE"))
					 * { String enter_time = engine
					 * .getParameter(PostBackValue[i]); //
					 * 如果编制日期为“/则往设备信息主表和检验记录表里保存空 if (enter_time.equals("/") ||
					 * enter_time.equals("") || enter_time.equals("待受检单位申请后确定"))
					 * { enter_time = ""; } if (!"".equals(enter_time)) {
					 * SimpleDateFormat format = new SimpleDateFormat(
					 * "yyyy-MM-dd"); Date date =
					 * format.parse((enter_time.replace("/", "-")).toString());
					 * insInfo.setEnter_time(date); } }
					 */

					// 如果检验时间不为空，则回写如业务信息表
					if (PostBackValue[i].toUpperCase()
							.equals("INSPECTION_DATE")) {
						if (!engine.getParameter(PostBackValue[i]).equals("")) {
							String check_date = engine
									.getParameter(PostBackValue[i]);
							SimpleDateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd");
							insInfo.setAdvance_time(format.parse(check_date));
							inspectionZZJDInfo.setInspection_date(format
									.parse(check_date));
						}
					}
					// 如果下次检验时间不为空，则回写如业务信息表
					if (PostBackValue[i].toUpperCase()
							.equals("LAST_INSPECTION_DATE")) {
						if (!engine.getParameter(PostBackValue[i]).equals("")) {
							String last_check_date = engine
									.getParameter(PostBackValue[i]);
							if(!"/".equals(last_check_date) && last_check_date.contains("-")){
								insInfo.setLast_check_time(DateUtil.convertStringToDate(Constant.defaultDatePattern, last_check_date));
								inspectionZZJDInfo.setLast_inspection_date(last_check_date);
							}else{
								inspectionZZJDInfo.setLast_inspection_date(last_check_date);
							}							
						}
					}
					
					// 锅炉设计文件start
					// 将对制造单位地址写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("MADE_UNIT_ADDR")) {
						inspectionZZJDInfo.setMade_unit_addr(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对鉴定项目编号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("CHECK_PROJECT_CODE")) {
						inspectionZZJDInfo.setCheck_project_code(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对设计属性写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DESIGN_PROPERTY")) {
						inspectionZZJDInfo.setDesign_property(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对审查属性写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("EXAM_PROPERTY")) {
						inspectionZZJDInfo.setExam_property(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对鉴定属性写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("CHECK_PROPERTY")) {
						inspectionZZJDInfo.setCheck_property(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对额定出力写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("RATED_OUTPUT")) {
						inspectionZZJDInfo.setRated_output(engine
								.getParameter(PostBackValue[i]));
						if(StringUtil.isEmpty(rated_output)){
							rated_output = engine
									.getParameter(PostBackValue[i]).trim();
						}
					}
					
					// 将对设计热效率写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DESIGN_HEAT")) {
						inspectionZZJDInfo.setDesign_heat(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对稳定工况范围写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("WORK_RANGE")) {
						inspectionZZJDInfo.setWork_range(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对锅炉给水（回水）温度（℃）写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("USE_TEMP2")) {
						inspectionZZJDInfo.setUse_temp2(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对结构型式写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("STRUCT_TYPE")) {
						inspectionZZJDInfo.setStruct_type(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对设计燃料类型写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("DESIGN_FUEL_TYPE")) {
						inspectionZZJDInfo.setDesign_fuel_type(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对燃烧方式写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("BURN_MODE")) {
						inspectionZZJDInfo.setBurn_mode(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对燃料低位发热量不低于（MJ/Kg） 写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("CALORIFIC_VALUE")) {
						inspectionZZJDInfo.setCalorific_value(engine
								.getParameter(PostBackValue[i]));
					}
					
					// 将对燃烧机型号写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("BURNER_MODEL")) {
						inspectionZZJDInfo.setBurner_model(engine
								.getParameter(PostBackValue[i]));
					}		
					// 获取额定出力单位
					if (PostBackValue[i].toUpperCase().equals("RATED_OUTPUT_TYPE1")) {
						rated_output_type = engine
								.getParameter(PostBackValue[i]);
					}
					// 获取额定出力单位
					if (PostBackValue[i].toUpperCase().equals("RATED_OUTPUT_TYPE")) {
						if(StringUtil.isEmpty(rated_output_type)){
							rated_output_type = engine
									.getParameter(PostBackValue[i]);
						}
					}
					// 锅炉设计文件end
					
					// 安全阀校验报告start
					// 将邮政编码写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("COM_ZIP_CODE")) {
						inspectionZZJDInfo.setCom_zip_code(engine
								.getParameter(PostBackValue[i]));
					}	
					// 将单位地址写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("COM_ADDR")) {
						inspectionZZJDInfo.setCom_addr(engine
								.getParameter(PostBackValue[i]));
					}	
					// 将编码类型写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("CODE_TYPE")) {
						inspectionZZJDInfo.setCode_type(engine
								.getParameter(PostBackValue[i]));
					}
					// 将编码值写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("CODE_VALUE")) {
						inspectionZZJDInfo.setCode_type(engine
								.getParameter(PostBackValue[i]));
					}
					// 将联系人写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("CHECK_OP")) {
						inspectionZZJDInfo.setCheck_op(engine
								.getParameter(PostBackValue[i]));
					}
					// 将联系电话写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("CHECK_TEL")) {
						inspectionZZJDInfo.setCheck_tel(engine
								.getParameter(PostBackValue[i]));
					}
					// 将要求整定压力写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("YQZDYL")) {
						inspectionZZJDInfo.setYqzdyl(engine
								.getParameter(PostBackValue[i]));
					}
					// 将校验方式写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("CHECK_METHOD")) {
						inspectionZZJDInfo.setCheck_method(engine
								.getParameter(PostBackValue[i]));
					}
					// 将校验介质写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("CHECK_MEDIUM")) {
						inspectionZZJDInfo.setCheck_medium(engine
								.getParameter(PostBackValue[i]));
					}
					// 将整定压力写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("ZDYL")) {
						inspectionZZJDInfo.setZdyl(engine
								.getParameter(PostBackValue[i]));
					}
					// 将密封试验压力写入制造监督检验明细表
					if (PostBackValue[i].toUpperCase().equals("MFSYYL")) {
						inspectionZZJDInfo.setMfsyyl(engine
								.getParameter(PostBackValue[i]));
					}					
					// 安全阀校验报告end 
					
					
					if (PostBackValue[i].toUpperCase().equals("REPORT_SN")) {
						insInfo.setReport_sn(engine
								.getParameter(PostBackValue[i]));
						// 由于report_sn在报检时就已经生成，所以直接进入下一次循环
						continue;
					}

					if (PostBackValue[i].toUpperCase().indexOf("selectRow") != -1) {
						continue;
					}
				}
				reportItemValueDao.insertReportItemValue(
						StringUtil.getUUID(), report_type,
						PostBackValue[i],
						engine.getParameter(PostBackValue[i]), ins_info_id);
			}

			// 生成报告书编号
			CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
			User user = (User)curUser.getSysUser();
			Employee emp = (com.khnt.rbac.impl.bean.Employee)user.getEmployee();
			//Org org = TS_Util.getCurOrg(curUser);
			
			synchronized (this){				
				try {
					if (StringUtil.isEmpty(insInfo.getReport_sn())) {
						String report_sn = "";
						// 存在检验类别，根据检验类别生成报告书编号
						// 不存在检验类别，根据报告书名称判断检验类别来生成报告书编号
						if (StringUtil.isNotEmpty(check_type)) {
							// 2、根据设备类型、检验类型、报告类型生成报告书编号
							Map<String, Object> reportSnMap = inspectionCommonService.createReportSn("saveZZJDRecord", ins_info_id, report_type, check_type, device_type);
							report_sn = String.valueOf(reportSnMap.get("report_sn"));
							insInfo.setReport_sn(report_sn);
						} else {
							if (report != null) {
								check_type = report.getReport_name().contains("制造")
										|| report.getReport_name().contains(
												"爆破片装置安全性能监督检验") || report.getReport_name().contains(
												"安全阀安全性能监督检验") ? "1" : "2";
								// 2、根据设备类型、检验类型、报告类型生成报告书编号
								Map<String, Object> reportSnMap = inspectionCommonService.createReportSn("saveZZJDRecord", ins_info_id, report_type, check_type, device_type);
								report_sn = String.valueOf(reportSnMap.get("report_sn"));
								insInfo.setReport_sn(report_sn);
							}
						}
						
						reportItemValueDao
								.createSQLQuery(
										"update TZSB_REPORT_ITEM_VALUE set item_value='"
												+ report_sn
												+ "'where fk_report_id='"
												+ report_type
												+ "' and item_name='REPORT_SN' and fk_inspection_info_id='"
												+ ins_info_id + "'")
								.executeUpdate();
					} else {
						// 报告书编号更新报告数据保存表
						reportItemValueDao
								.createSQLQuery(
										"update  TZSB_REPORT_ITEM_VALUE set item_value='"
												+ insInfo.getReport_sn()
												+ "'where fk_report_id='"
												+ report_type
												+ "' and item_name='REPORT_SN' and fk_inspection_info_id='"
												+ ins_info_id + "'")
								.executeUpdate();
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.debug(e.toString());
				}

				insInfo.setIs_report_input("2");	// 报告是否已修改过（1：未修改 2：已修改）
				insInfo.setIs_copy("0");// 不是复制报告
				insInfo.setEnter_op_id(emp.getId()); // 录入人，此处要用employee，因为报告使用的电子签名是与employee关联的
				insInfo.setEnter_op_name(emp.getName());
				// 关于报告录入日期
				// （制造监督检验，无“录入日期”数据项）
				// 电梯（定检和监检，在报告录入时，用户可自行修改录入日期）
				// 故此处每保存一次报告都更新报告录入日期为当前日期
				insInfo.setEnter_time(new Date());

				// 锅炉设计文件鉴定报告收费金额自动计算start...
				// 2017-11-28锅炉部卞庭梅提出增加
				if (report.getReport_name().contains("锅炉设计文件鉴定")) {
					double advance_fees = 0.0;
					// 报告页码
					String report_item = insInfo.getReport_item();
					// 获取报告目录信息
					List<ReportItem> reportItemList = reportItemService.queryByReportId(report_type); // 报告项目
					boolean only_sc = false;
					for (ReportItem reportItem : reportItemList) {
						if (reportItem.getItme_name().contains("审查")) {
							if (report_item.trim().split(",").length == 1
									&& report_item.trim().equals(reportItem.getPage_index())) {
								only_sc = true;
							}
						}
					}

					// 1、锅炉部件收费金额计算
					if (device_type.equals("B100")) {
						if (StringUtil.isNotEmpty(device_pressure) && StringUtil.isNotEmpty(weight_steels)) {
							if (Float.parseFloat(device_pressure) <= 2.5) {
								double money = (Float.parseFloat(weight_steels) * 10000) * 0.01;
								if (money > 5000) {
									advance_fees = 5000;
								} else {
									advance_fees = money < 300 ? 300 : money;
								}
							} else {
								double money = (Float.parseFloat(weight_steels) * 10000) * 0.007;
								if (money > 5000) {
									advance_fees = 5000;
								} else {
									advance_fees = money < 400 ? 400 : money;
								}
							}
						}
					} else {
						// 2、锅炉本体收费金额计算
						if (StringUtil.isNotEmpty(rated_output)) {
							if ("t/h".equals(rated_output_type)) {
								if (Float.parseFloat(rated_output) >= 1000) {
									advance_fees = 8000;
								} else if (Float.parseFloat(rated_output) >= 100
										&& Float.parseFloat(rated_output) < 1000) {
									advance_fees = 7000;
								} else if (Float.parseFloat(rated_output) < 100) {
									if (StringUtil.isNotEmpty(device_pressure)) {
										if (Float.parseFloat(device_pressure) <= 2.5) {
											double money = (Float.parseFloat(weight_steels) * 10000) * 0.015;
											advance_fees = money < 400 ? 400 : money;
										} else {
											double money = (Float.parseFloat(weight_steels) * 10000) * 0.0075;
											advance_fees = money > 6000 ? 6000 : money;
										}
									}
								}
							} else if ("MW".equals(rated_output_type)) {

							}
							if (only_sc) {
								advance_fees = advance_fees * 0.6;
							}
						}
					}
					insInfo.setAdvance_fees(advance_fees);
				}
				// 锅炉设计文件鉴定报告收费金额自动计算end
				
				// 保存回写的业务信息
				inspectionInfoDao.save(insInfo);
			}
			inspectoinZZJDInfoDao.save(inspectionZZJDInfo);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
		}
	}

	/**
	 * 报告审核时，更新业务信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public void flow_saveCheck(Map<String, Object> map) throws Exception {
		String id = map.get("ins_info_id").toString();
		String type = map.get("type").toString();
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User user = (User)curUser.getSysUser();
		Employee emp = (com.khnt.rbac.impl.bean.Employee)user.getEmployee();
		Org org = TS_Util.getCurOrg(curUser);
		try {
			// 修改业务表审核信息
			InspectionInfo info = inspectionInfoDao.get(id);
			SimpleDateFormat fomat = new SimpleDateFormat("yyyy-MM-dd");

			// 判断流程修改相应数据
			if ("04".equals(type)) { // 04：报告审核
				info.setExamine_id(emp.getId()); // 此处要用employee，因为报告使用的电子签名是与employee关联的
				info.setExamine_name(emp.getName());
				info
						.setExamine_Date(fomat.parse(map.get("op_time")
								.toString()));
			}
			if ("05".equals(type)) { // 05：报告签发
				info.setIssue_id(emp.getId()); // 此处要用employee，因为报告使用的电子签名是与employee关联的
				info.setIssue_name(emp.getName());
				info.setIssue_Date(fomat.parse(map.get("op_time").toString()));
			}
			inspectionInfoDao.save(info);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
		}
	}

	/**
	 * 回退流程
	 * 
	 * @param infoId
	 *            业务id
	 * @param flowId
	 *            流程id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void backFlow(Map<String, Object> map, HttpServletRequest request)
			throws Exception {
		String infoId = map.get("ins_info_id").toString();
		String acc_id = map.get("acc_id").toString();
		String flow_num = map.get("flow_num").toString();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		InspectionInfo info = inspectionInfoDao.get(infoId);
		if (map.get("backStep") != null) {
			String backStep = map.get("backStep").toString();
			JSONObject dataBus = new JSONObject();
			dataBus.put("flag", backStep);
			paramMap.put(FlowExtWorktaskParam.DATA_BUS, dataBus);
		}
		if (map.get("nextHandle") != null) {
			String nextHandle = map.get("nextHandle").toString();
			JSONObject dataBus = new JSONObject();
			dataBus.put("flag", nextHandle);
			paramMap.put(FlowExtWorktaskParam.DATA_BUS, dataBus);
		}

		paramMap.put(FlowExtWorktaskParam.ACTIVITY_ID, acc_id);
		if (info.getFlow_note_id().equals(flow_num)) {
			Map<String, Object> flowMap = flowExtManager
					.returnedActivity(paramMap);
			// 获取退回步骤Id
			List<Activity> list = (List) flowMap
					.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);

			// 写入日志
			String step_name = info.getFlow_note_name();
			String op_conclusion = "从【" + step_name + "】环节退回至【"
					+ list.get(0).getName() + "】环节。";
			String re_name = step_name + "回退";
			String revise_remark = map.get("revise_remark") == null ? "" : map
					.get("revise_remark").toString();
			if (!"".equals(revise_remark)) {
				op_conclusion = op_conclusion
						+ map.get("revise_remark").toString();
			}

			CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
			User user = (User)curUser.getSysUser();
			//Employee emp = (com.khnt.rbac.impl.bean.Employee)user.getEmployee();
			//Org org = TS_Util.getCurOrg(curUser);
			info.setFlow_note_id(list.get(0).getActivityId());
			info.setFlow_note_name(list.get(0).getName());
			info.setIs_report_input("1"); // 报告是否已录入状态（1：未录入 2：已录入）
			info.setIs_back("1"); // 是否是退回报告状态（默认的状态为1，提交后变成0，退回也变成1）
			// 2017-07-10邹益平提出要求，退回后，再次审核或签发时，审批名字应为空白，故此处不再进行记录
			/*if (step_name.contains("审核")) {
				info.setExamine_name(emp.getName());
				info.setExamine_id(emp.getId());
				info.setExamine_Date(new Date());
			} else if (step_name.contains("签发")) {
				info.setIssue_id(emp.getId());
				info.setIssue_name(emp.getName());
				info.setIssue_Date(new Date());
			}*/
			inspectionInfoDao.save(info);
			logService.setLogs(infoId, re_name, op_conclusion, user.getId(),
					user.getName(), new Date(), request.getRemoteAddr());
		}
	}

	// 报告归档
	public void flow_reportEnd(Map<String, Object> map,
			HttpServletRequest request) throws Exception {
		String info_id = map.get("infoId").toString();
		String process_id = map.get("process_id").toString();
		String flow_num = map.get("flow_num").toString();
		InspectionInfo info = inspectionInfoDao.get(info_id);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(FlowExtWorktaskParam.PROCESS_ID, process_id);
		if (info.getFlow_note_id().equals(flow_num)) {
			try {
				flowExtManager.finishProcess(paramMap);
			} catch (Exception e) {
				e.printStackTrace();
				log.debug(e.toString());
			}
			String step_name = info.getFlow_note_name();
			String op_conclusion = "完成【" + step_name + "】环节，流程结束。";
			info.setFlow_note_id("");
			info.setFlow_note_num("99"); // 99表示流程结束
			info.setFlow_note_name("报告归档");
			inspectionInfoDao.save(info);

			// 记录日志
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			logService.setLogs(info_id, step_name, op_conclusion, user.getId(),
					user.getName(), new Date(), request.getRemoteAddr());

		}

	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getFlowStep(String ins_info_id)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<SysLog> list = inspectionInfoDao.createQuery(
				"  from SysLog where business_id ='" + ins_info_id
						+ "' order by op_time,id asc").list();
		list.size();
		map.put("flowStep", list);
		map.put("size", list.size());
		map.put("sn", inspectionInfoDao.get(ins_info_id).getSn());
		map.put("success", true);
		return map;
	}

	// 获取打印报告时，报告列表
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryZZJDInfo(String inspectionInfoIds,
			String userId) {
		return inspectionInfoDao.queryZZJDInfo(inspectionInfoIds, userId);
	}

	// 获取打印报告时，报告列表
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryZZJDInfo(String inspectionInfoIds) {
		return inspectionInfoDao.queryZZJDInfo(inspectionInfoIds);
	}

	// 打印报告时，更新业务信息（更新打印时间）
	public void flow_print(Map<String, Object> map) throws Exception {
		String id = map.get("ins_info_id").toString();
		InspectionInfo info = inspectionInfoDao.get(id);
		info.setPrint_time(new Date());
		inspectionInfoDao.save(info);
	}
	
	public String getbglxNameById(String id){
		String sql="select t.id,t.report_name from base_reports t where t.id=?";
		List<Map<String, Object>> list=inspectionZZJDDao.createSQLQuery(sql, id).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		String name="";
		for (Map<String, Object> map : list) {
			name=String.valueOf(map.get("REPORT_NAME"));
		}
		return name;
	}
	
	/**
	 * 保存报告
	 * 
	 * @param PostBackValue
	 * @param engine
	 * @param report_type
	 * @param ins_info_id
	 * @throws Exception
	 * @author GaoYa
	 */
	@SuppressWarnings("unchecked")
	public void saveReportNew(HttpServletRequest request,Map<String, RtExportData> map) {
		try {
			String page = request.getParameter("page");
			String pageName = request.getParameter("pageName");
			String report_type = map.get("fk_report_id").getValue();
			String ins_info_id = map.get("fk_inspection_info_id").getValue();
			String code_ext = map.get("fkCodeExt") == null ? "" : map.get("fkCodeExt").getValue();
			if (StringUtil.isEmpty(code_ext)) {
				code_ext = "";
			}
			// 根据业务信息ID获取业务信息bean
			InspectionInfo insInfo = inspectionInfoDao.get(ins_info_id);
			// 根据业务信息ID获取制造监督检验明细bean
			InspectionZZJDInfo inspectionZZJDInfo = inspectoinZZJDInfoDao
					.getByInfoId(ins_info_id);
			// 获取报告类型信息
			Report report = reportService.get(report_type);
			
			// 获取检验类别
 			String check_type = "";
			InspectionZZJD inspectionZZJD = inspectionZZJDInfo.getInspectionZZJD();
			if(inspectionZZJD!=null){
				if(StringUtil.isNotEmpty(inspectionZZJD.getCheck_type())){
					check_type = inspectionZZJD.getCheck_type();
				}
			}

			// 获取设备类型
			String device_type = inspectionZZJDInfo.getDevice_type_code();
			// 获取本体耗钢量（吨）（用于锅炉设计文件鉴定报告收费金额计算）
			String weight_steels = inspectionZZJDInfo.getWeight_steels();
			// 获取蒸发量（即额定出力（t/h）或（MW），用于锅炉设计文件鉴定报告收费金额计算）
			String rated_output = inspectionZZJDInfo.getRated_output();
			// 获取蒸发量单位（即额定出力（t/h）或（MW），用于锅炉设计文件鉴定报告收费金额计算）
			String rated_output_type = "";
			// 获取额定工作压力（即额定出口压力（MPa），用于锅炉设计文件鉴定报告收费金额计算）
			String device_pressure = inspectionZZJDInfo.getDevice_pressure();
			
			String KeyNames = map.keySet().toString();
			String names = "";
			if (StringUtil.isEmpty(code_ext) || !code_ext.contains("_")) {
				names = KeyNames.substring(1, KeyNames.length() - 1).replace(",", "','").replace(" ", "");
			} else {
				names = KeyNames.substring(1, KeyNames.length() - 1).replace(",", RtField.separator + code_ext + "','")
						.replace(" ", "");
			}
			
			// 首先删除tzsb_report_item_value表中之前保存的数据
			// 报告书编号不删除
			String delSql = "delete from tzsb_report_item_value where "
					+ " fk_inspection_info_id = ? "
					+ " and upper(item_name) <> 'REPORT_SN' ";
			inspectionInfoDao.createSQLQuery(delSql, ins_info_id)
					.executeUpdate();
			this.rtPageManager.delFuncDataBacth(ins_info_id, names, page);
			// 开始处理报表内容数据
			for (String itemName : map.keySet()) {
				RtExportData rtExportData = map.get(itemName);
				// 保存字段所在页码
				rtExportData.setPageNo(page);
				String name = RtField.getName(itemName, code_ext);
				rtExportData.setName(name);
				String fname = rtExportData.getName().toUpperCase();
				String value = rtExportData.getValue();
				// 如果为空则不处理
				// 2016-11-28修改，如果内容为空也要处理，因用户可能错误上传后需要修改报告时做删除处理（锅炉部邹益平要求）
				if (StringUtil.isNotEmpty(name) || (fname.equals("DEVICE_PROCESSING") || fname.equals("DEVICE_PROCESSING2"))) {
					// 将制造单位写入检验业务信息表和制造监督检验明细表
					if (fname.equals("MAKE_UNITS_NAME")) {
						if(StringUtil.isEmpty(insInfo.getReport_com_name())){
							insInfo.setReport_com_name(value);
						}
						if(StringUtil.isEmpty(inspectionZZJDInfo.getCom_name())){
							inspectionZZJDInfo.setCom_name(value);
						}
						inspectionZZJDInfo.setMade_unit_name(value);
					}
					// 将使用单位写入检验业务信息表和制造监督检验明细表
					if (fname.equals("COM_NAME")) {
						if(StringUtil.isNotEmpty(value)){
							insInfo.setReport_com_name(value);
							inspectionZZJDInfo.setCom_name(value);
						}
					}
					// 将制造单位机构代码写入制造监督检验明细表
					if (fname.equals("MADE_UNIT_CODE")) {
						inspectionZZJDInfo.setMade_unit_code(value);
					}
					// 将制造许可证级别写入制造监督检验明细表
					if (fname.equals("MADE_LICENSE_LEVEL")) {
						inspectionZZJDInfo.setMade_license_level(value);
					}

					// 将制造许可证编号写入制造监督检验明细表
					if (fname.equals("MAKE_LICENSE_CODE") || fname.equals("P_ZZXKZBH") || fname.equals("MAKE_LICENCE_CODE") || fname.equals("MAKE_UNITS_NAME_XKZMWJBH")) {
						inspectionZZJDInfo.setMade_license_code(value);
					}

					// 将安装单位写入检验业务信息表和制造监督检验明细表
					if (fname.equals("INSTALL_UNIT_NAME") || fname.equals("CONSTRUCTION_UNITS_NAME")) {
						insInfo.setReport_com_name(value);
						inspectionZZJDInfo.setInstall_unit_name(value);
					}

					// 将安装许可证编号写入制造监督检验明细表
					if (fname.equals("INSTALL_LICENSE_CODE") || fname.equals("CONSTRUCTION_LICENCE_CODE") 
							|| fname.equals("INSTALL_COM_CERT") || fname.equals("P_AZGZWXXKZBH") || fname.equals("CONSTRUCTION_LICENCE_NO") ) {
						inspectionZZJDInfo.setInstall_license_code(value);
					}

					// 将安装日期写入制造监督检验明细表
					if (fname.equals("INSTALL_DATE") || fname.equals("CONSTRUCTION_DATE") || fname.equals("INSTALL_FINISH_DATE")) {
						String install_date = value;
						if (StringUtil.isNotEmpty(install_date)) {
							inspectionZZJDInfo.setInstall_date(install_date);
						} else {
							inspectionZZJDInfo.setInstall_date("");
						}

					}

					// 将施工/建设单位写入检验业务信息表和制造监督检验明细表
					if (fname.equals("CONSTRUCTION_UNIT_NAME") || fname.equals("BUILD_UNIT")) {
						insInfo.setReport_com_name(value);
						inspectionZZJDInfo.setConstruction_unit_name(value);
					}
					// 将施工单位组织机构代码写入制造监督检验明细表
					if (fname.equals("CONSTRUCTION_UNIT_CODE")) {
						inspectionZZJDInfo.setConstruction_unit_code(value);
					}
					// 将改造修理许可级别写入制造监督检验明细表
					if (fname.equals("CONSTRUCTION_LICENSE_LEVEL")) {
						inspectionZZJDInfo.setConstruction_license_level(value);
					}

					// 将改造修理许可证编号写入制造监督检验明细表
					if (fname.equals("CONSTRUCTION_LICENSE_CODE") || fname.equals("CAD_XKZH")) {
						inspectionZZJDInfo.setConstruction_license_code(value);
					}

					// 将施工类别写入制造监督检验明细表
					if (fname.equals("CONSTRUCTION_TYPE") || fname.equals("CAD_TYPE") || fname.equals("P_SGLB")) {
						inspectionZZJDInfo.setConstruction_type(value);
					}

					// 将竣工日期写入制造监督检验明细表
					if (fname.equals("REPAIR_FINISH_DATE")) {
						String repair_finish_date = value;
						if (StringUtil.isNotEmpty(repair_finish_date)) {
							inspectionZZJDInfo.setRepair_finish_date(repair_finish_date);
						} else {
							inspectionZZJDInfo.setRepair_finish_date("");
						}
					}

					// 将使用单位组织机构代码写入制造监督检验明细表
					if (fname.equals("COM_CODE")) {
						inspectionZZJDInfo.setCom_code(value);
					}

					// 将使用登记证编号写入制造监督检验明细表
					if (fname.equals("COM_REGISTRATION_NUM")) {
						inspectionZZJDInfo.setCom_registration_num(value);
					}

					// 将设备使用地点写入制造监督检验明细表
					if (fname.equals("DEVICE_USE_PLACE")) {
						inspectionZZJDInfo.setDevice_use_place(value);
					}

					// 将设备类型写入制造监督检验明细表
					if (fname.equals("DEVICE_TYPE_CODE")) {
						inspectionZZJDInfo.setDevice_type_code(value);
					}

					// 将设备类型名称写入制造监督检验明细表
					if (fname.equals("DEVICE_TYPE_NAME")) {
						inspectionZZJDInfo.setDevice_type_name(value);
					}

					// 将设备名称写入制造监督检验明细表
					if (fname.equals("DEVICE_NAME")) {
						inspectionZZJDInfo.setDevice_name(value);
					}

					// 将产品编号写入制造监督检验明细表
					if (fname.equals("DEVICE_NO")) {
						inspectionZZJDInfo.setDevice_no(value);
					}
					
					// 将产品编号2写入制造监督检验明细表
					if (fname.equals("DEVICE_NO_2")) {
						inspectionZZJDInfo.setDevice_no_2(value);
					}

					// 将产品批号写入制造监督检验明细表
					if (fname.equals("DEVICE_BATCH_NUM")) {
						inspectionZZJDInfo.setDevice_batch_num(value);
					}

					// 将产品型号写入制造监督检验明细表
					if (fname.equals("DEVICE_MODEL")) {
						inspectionZZJDInfo.setDevice_model(value);
					}
					
					// 将产品型号2写入制造监督检验明细表
					if (fname.equals("DEVICE_MODEL_2")) {
						inspectionZZJDInfo.setDevice_model_2(value);
					}

					// 将产品数量写入制造监督检验明细表
					if (fname.equals("DEVICE_COUNT")) {
						inspectionZZJDInfo.setDevice_count(value);
					}

					// 将产品标准写入制造监督检验明细表
					if (fname.equals("DEVICE_STANDARD")) {
						inspectionZZJDInfo.setDevice_standard(value);
					}

					// 将材料牌号写入制造监督检验明细表
					if (fname.equals("MATERIAL_NUM")) {
						inspectionZZJDInfo.setMaterial_num(value);
					}

					// 将设备代码写入制造监督检验明细表
					if (fname.equals("DEVICE_CODE")) {
						inspectionZZJDInfo.setDevice_code(value);
					}

					// 将确认日期1写入制造监督检验明细表
					if (fname.equals("CONFIRM_DATE1")) {
						inspectionZZJDInfo.setConfirm_date1(value);
					}

					// 将确认日期2写入制造监督检验明细表
					if (fname.equals("CONFIRM_DATE2")) {
						inspectionZZJDInfo.setConfirm_date2(value);
					}

					// 将确认日期3写入制造监督检验明细表
					if (fname.equals("CONFIRM_DATE3")) {
						inspectionZZJDInfo.setConfirm_date3(value);
					}

					// B300：封头
					if ("B300".equals(device_type)) {
						// 将来料加工（是）写入制造监督检验明细表
							if (StringUtil.isNotEmpty(value) && "√".equals(value)) {
								inspectionZZJDInfo.setDevice_processing(value);
								inspectionZZJDInfo.setDevice_processing2("");
							} else {
								inspectionZZJDInfo.setDevice_processing("");
							}
						}

						// 将来料加工（否）写入制造监督检验明细表
						if (fname.equals("DEVICE_PROCESSING2")) {
							if (StringUtil.isNotEmpty(value) && "√".equals(value)) {
								inspectionZZJDInfo.setDevice_processing2(value);
								inspectionZZJDInfo.setDevice_processing("");
							} else {
								inspectionZZJDInfo.setDevice_processing2("");
							}
						}
					}

					// 将监检所抽的产品编号写入制造监督检验明细表
					if (fname.equals("CHECK_DEVICE_NO")) {
						inspectionZZJDInfo.setCheck_device_no(value);
					}

					// 将本证书适用的产品编号写入制造监督检验明细表
					if (fname.equals("TRIAL_DEVICE_NO")) {
						inspectionZZJDInfo.setTrial_device_no(value);
					}

					// 将气瓶文件审查写入制造监督检验明细表
					if (fname.equals("DEVICE_FILE_CHECK")) {
						inspectionZZJDInfo.setDevice_file_check(value);
					}

					// 将文件审查日期写入制造监督检验明细表
					if (fname.equals("FILE_CHECK_DATE")) {
						inspectionZZJDInfo.setFile_check_date(value);
					}

					// 将设计单位写入制造监督检验明细表
					if (fname.equals("DESIGN_UNIT_NAME") || fname.equals("DESIGN_COM_NAME")) {
						inspectionZZJDInfo.setDesign_unit_name(value);
					}

					// 将设计单位机构代码写入制造监督检验明细表
					if (fname.equals("DESIGN_UNIT_CODE") || fname.equals("DESIGN_UNIT_CODE") || fname.equals("DESIGN_COM_CODE")) {
						inspectionZZJDInfo.setDesign_unit_code(value);
					}

					// 将设计许可证编号写入制造监督检验明细表
					if (fname.equals("DESIGN_LICENSE_CODE") || fname.equals("DESIGN_COM_CERT")) {
						inspectionZZJDInfo.setDesign_license_code(value);
					}

					// 将产品图号写入制造监督检验明细表
					if (fname.equals("DEVICE_PIC_NO") || fname.equals("PRODUCT_TH")) {
						inspectionZZJDInfo.setDevice_pic_no(value);
					}

					// 将设计日期写入制造监督检验明细表
					if (fname.equals("DESIGN_DATE") || fname.equals("B_SJRQ") || fname.equals("SJRQ")) {
						String design_date = value;
						if (StringUtil.isNotEmpty(design_date)) {
							inspectionZZJDInfo.setDesign_date(design_date);
						} else {
							inspectionZZJDInfo.setDesign_date("");
						}
					}

					// 将制造日期写入制造监督检验明细表
					if (fname.equals("MAKE_DATE")) {
						String made_date = value;
						if (StringUtil.isNotEmpty(made_date)) {
							inspectionZZJDInfo.setMade_date(made_date);
						} else {
							inspectionZZJDInfo.setMade_date("");
						}

					}

					// 将有关安全技术监察规程写入制造监督检验明细表
					if (fname.equals("SAFELY_TECH_STANDARD")) {
						inspectionZZJDInfo.setSafely_tech_standard(value);
					}

					// 将记事写入制造监督检验明细表
					if (fname.equals("INSPECTION_EVENTS")) {
						inspectionZZJDInfo.setInspection_events(value);
					}

					// 将备注写入制造监督检验明细表
					if (fname.equals("REMARK")) {
						inspectionZZJDInfo.setRemark(value);
					}

					// 将发动机编号写入制造监督检验明细表
					if (fname.equals("ENGINE_NO") || fname.equals("P_FDJXH")) {
						inspectionZZJDInfo.setEngine_no(value);
					}

					// 将车辆识别代码写入制造监督检验明细表
					if (fname.equals("DEVICE_CAR_NUM")) {
						inspectionZZJDInfo.setDevice_car_num(value);
					}

					// 将设计压力写入制造监督检验明细表
					if (fname.equals("DEVICE_PRESSURE")) {
						inspectionZZJDInfo.setDevice_pressure(value);
						if(StringUtil.isEmpty(device_pressure)){
							device_pressure = value.trim();
						}
					}

					// 将设计温度写入制造监督检验明细表
					if (fname.equals("USE_TEMP")) {
						inspectionZZJDInfo.setUse_temp(value);
					}

					// 将设计介质写入制造监督检验明细表
					if (fname.equals("DEVICE_MEDIUM")) {
						inspectionZZJDInfo.setDevice_medium(value);
					}

					// 将设计容积写入制造监督检验明细表
					if (fname.equals("DEVICE_VOLUME")) {
						inspectionZZJDInfo.setDevice_volume(value);
					}
					
					// 将设备材料写入制造监督检验明细表
					if (fname.equals("DEVICE_METERIAL")) {
						inspectionZZJDInfo.setDevice_meterial(value);
					}
					
					// 将设备长度写入制造监督检验明细表
					if (fname.equals("DEVICE_LENGTH")) {
						inspectionZZJDInfo.setDevice_length(value);
					}
					
					// 将工程名称写入制造监督检验明细表
					if (fname.equals("PROJECT_NAME")) {
						inspectionZZJDInfo.setProject_name(value);
					}
					
					// 将出厂编号（1）写入制造监督检验明细表
					if (fname.equals("FACTORY_CODE_1")) {
						inspectionZZJDInfo.setFactory_code_1(value);
					}
					
					// 将出厂编号（2）写入制造监督检验明细表
					if (fname.equals("FACTORY_CODE_2")) {
						inspectionZZJDInfo.setFactory_code_2(value);
					}
					
					// 将改造与重大修理项目写入制造监督检验明细表
					if (fname.equals("REPAIR_PROJECT")) {
						inspectionZZJDInfo.setRepair_project(value);
					}
					
					// 将对安装单位质量体系运转情况的评价写入制造监督检验明细表
					if (fname.equals("INSTALL_EVALUATE")) {
						inspectionZZJDInfo.setInstall_evaluate(value);
					}
					
					// 将对安装竣工资料审查日期写入制造监督检验明细表
					if (fname.equals("FINISH_CHECK_DATE")) {
						inspectionZZJDInfo.setFinish_check_date(value);
					}
					
					// 将对安装竣工资料审查写入制造监督检验明细表
					if (fname.equals("FINISH_DATA_CHECK")) {
						inspectionZZJDInfo.setFinish_data_check(value);
					}
					
					// 将对泄漏试验日期写入制造监督检验明细表
					if (fname.equals("LEAK_CHECK_DATE")) {
						inspectionZZJDInfo.setLeak_check_date(value);
					}
					
					// 将对泄漏试验确认写入制造监督检验明细表
					if (fname.equals("LEAK_TEST_CHECK")) {
						inspectionZZJDInfo.setLeak_test_check(value);
					}
					
					// 将对安装质量检查日期写入制造监督检验明细表
					if (fname.equals("QUALITY_CHECK_DATE")) {
						inspectionZZJDInfo.setQuality_check_date(value);
					}
					
					// 将对安装质量检查写入制造监督检验明细表
					if (fname.equals("INSTALL_QUALITY_CHECK")) {
						inspectionZZJDInfo.setInstall_quality_check(value);
					}
					
					// 将对外观检查日期写入制造监督检验明细表
					if (fname.equals("SURFACE_CHECK_DATE")) {
						inspectionZZJDInfo.setSurface_check_date(value);
					}
					
					// 将对气瓶外观检查写入制造监督检验明细表
					if (fname.equals("DEVICE_SURFACE_CHECK")) {
						inspectionZZJDInfo.setDevice_surface_check(value);
					}
					
					// 将对资料审查日期写入制造监督检验明细表
					if (fname.equals("DATA_CHECK_DATE")) {
						inspectionZZJDInfo.setData_check_date(value);
					}
					
					// 将对气瓶外观检查写入制造监督检验明细表
					if (fname.equals("INSTALL_DATA_CHECK")) {
						inspectionZZJDInfo.setInstall_data_check(value);
					}

					// 检验结论写入到主表中和业务信息表
					/*
					 * if (PostBackValue[i].toUpperCase().equals(
					 * "INSPECTION_CONCLUSION")) {
					 * insInfo.setInspection_conclusion(engine
					 * .getParameter(PostBackValue[i])); }
					 */

					// 编制日期写入业务信息表
					/*
					 * if (PostBackValue[i].toUpperCase().equals("DRAFT_DATE"))
					 * { String enter_time = engine
					 * .getParameter(PostBackValue[i]); //
					 * 如果编制日期为“/则往设备信息主表和检验记录表里保存空 if (enter_time.equals("/") ||
					 * enter_time.equals("") || enter_time.equals("待受检单位申请后确定"))
					 * { enter_time = ""; } if (!"".equals(enter_time)) {
					 * SimpleDateFormat format = new SimpleDateFormat(
					 * "yyyy-MM-dd"); Date date =
					 * format.parse((enter_time.replace("/", "-")).toString());
					 * insInfo.setEnter_time(date); } }
					 */

					// 如果检验时间不为空，则回写如业务信息表
					if (fname.equals("INSPECTION_DATE")) {
						if (!value.equals("")) {
							String check_date = value;
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							insInfo.setAdvance_time(format.parse(check_date));
							inspectionZZJDInfo.setInspection_date(format.parse(check_date));
						}
					}
					// 如果下次检验时间不为空，则回写如业务信息表
					if (fname.equals("LAST_INSPECTION_DATE")) {
						if (!value.equals("")) {
							String last_check_date = value;
							if(!"/".equals(last_check_date) && last_check_date.contains("-")){
								insInfo.setLast_check_time(DateUtil.convertStringToDate(Constant.defaultDatePattern, last_check_date));
								inspectionZZJDInfo.setLast_inspection_date(last_check_date);
							}else{
								inspectionZZJDInfo.setLast_inspection_date(last_check_date);
							}							
						}
					}
					
					// 锅炉设计文件start
					// 将对制造单位地址写入制造监督检验明细表
					if (fname.equals("MADE_UNIT_ADDR")) {
						inspectionZZJDInfo.setMade_unit_addr(value);
					}
					
					// 将对鉴定项目编号写入制造监督检验明细表
					if (fname.equals("CHECK_PROJECT_CODE")) {
						inspectionZZJDInfo.setCheck_project_code(value);
					}
					
					// 将对设计属性写入制造监督检验明细表
					if (fname.equals("DESIGN_PROPERTY")) {
						inspectionZZJDInfo.setDesign_property(value);
					}
					
					// 将对审查属性写入制造监督检验明细表
					if (fname.equals("EXAM_PROPERTY")) {
						inspectionZZJDInfo.setExam_property(value);
					}
					
					// 将对鉴定属性写入制造监督检验明细表
					if (fname.equals("CHECK_PROPERTY")) {
						inspectionZZJDInfo.setCheck_property(value);
					}
					
					// 将对额定出力写入制造监督检验明细表
					if (fname.equals("RATED_OUTPUT")) {
						inspectionZZJDInfo.setRated_output(value);
						if(StringUtil.isEmpty(rated_output)){
							rated_output = value.trim();
						}
					}
					
					// 将对设计热效率写入制造监督检验明细表
					if (fname.equals("DESIGN_HEAT")) {
						inspectionZZJDInfo.setDesign_heat(value);
					}
					
					// 将对稳定工况范围写入制造监督检验明细表
					if (fname.equals("WORK_RANGE")) {
						inspectionZZJDInfo.setWork_range(value);
					}
					
					// 将对锅炉给水（回水）温度（℃）写入制造监督检验明细表
					if (fname.equals("USE_TEMP2")) {
						inspectionZZJDInfo.setUse_temp2(value);
					}
					
					// 将对结构型式写入制造监督检验明细表
					if (fname.equals("STRUCT_TYPE")) {
						inspectionZZJDInfo.setStruct_type(value);
					}
					
					// 将对设计燃料类型写入制造监督检验明细表
					if (fname.equals("DESIGN_FUEL_TYPE")) {
						inspectionZZJDInfo.setDesign_fuel_type(value);
					}
					
					// 将对燃烧方式写入制造监督检验明细表
					if (fname.equals("BURN_MODE")) {
						inspectionZZJDInfo.setBurn_mode(value);
					}
					
					// 将对燃料低位发热量不低于（MJ/Kg） 写入制造监督检验明细表
					if (fname.equals("CALORIFIC_VALUE")) {
						inspectionZZJDInfo.setCalorific_value(value);
					}
					
					// 将对燃烧机型号写入制造监督检验明细表
					if (fname.equals("BURNER_MODEL")) {
						inspectionZZJDInfo.setBurner_model(value);
					}		
					// 获取额定出力单位
					if (fname.equals("RATED_OUTPUT_TYPE1")) {
						rated_output_type = value;
					}
					// 获取额定出力单位
					if (fname.equals("RATED_OUTPUT_TYPE")) {
						if(StringUtil.isEmpty(rated_output_type)){
							rated_output_type = value;
						}
					}
					// 锅炉设计文件end
					
					// 安全阀校验报告start
					// 将邮政编码写入制造监督检验明细表
					if (fname.equals("COM_ZIP_CODE")) {
						inspectionZZJDInfo.setCom_zip_code(value);
					}	
					// 将单位地址写入制造监督检验明细表
					if (fname.equals("COM_ADDR")) {
						inspectionZZJDInfo.setCom_addr(value);
					}	
					// 将编码类型写入制造监督检验明细表
					if (fname.equals("CODE_TYPE")) {
						inspectionZZJDInfo.setCode_type(value);
					}
					// 将编码值写入制造监督检验明细表
					if (fname.equals("CODE_VALUE")) {
						inspectionZZJDInfo.setCode_type(value);
					}
					// 将联系人写入制造监督检验明细表
					if (fname.equals("CHECK_OP")) {
						inspectionZZJDInfo.setCheck_op(value);
					}
					// 将联系电话写入制造监督检验明细表
					if (fname.equals("CHECK_TEL")) {
						inspectionZZJDInfo.setCheck_tel(value);
					}
					// 将要求整定压力写入制造监督检验明细表
					if (fname.equals("YQZDYL")) {
						inspectionZZJDInfo.setYqzdyl(value);
					}
					// 将校验方式写入制造监督检验明细表
					if (fname.equals("CHECK_METHOD")) {
						inspectionZZJDInfo.setCheck_method(value);
					}
					// 将校验介质写入制造监督检验明细表
					if (fname.equals("CHECK_MEDIUM")) {
						inspectionZZJDInfo.setCheck_medium(value);
					}
					// 将整定压力写入制造监督检验明细表
					if (fname.equals("ZDYL")) {
						inspectionZZJDInfo.setZdyl(value);
					}
					// 将密封试验压力写入制造监督检验明细表
					if (fname.equals("MFSYYL")) {
						inspectionZZJDInfo.setMfsyyl(value);
					}					
					// 安全阀校验报告end 
					
					
					if (fname.equals("BASE__REPORT_SN")) {
						insInfo.setReport_sn(value);
						// 由于report_sn在报检时就已经生成，所以直接进入下一次循环
						continue;
					}

					if (fname.indexOf("selectRow") != -1) {
						continue;
					}
					reportItemValueDao.insertReportItemValue(StringUtil.getUUID(), report_type,name,value, ins_info_id);
			}

			// 生成报告书编号
			CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
			User user = (User)curUser.getSysUser();
			Employee emp = (com.khnt.rbac.impl.bean.Employee)user.getEmployee();
			//Org org = TS_Util.getCurOrg(curUser);
			
			synchronized (this){				
				try {
					if (StringUtil.isEmpty(insInfo.getReport_sn())) {
						String report_sn = "";
						// 存在检验类别，根据检验类别生成报告书编号
						// 不存在检验类别，根据报告书名称判断检验类别来生成报告书编号
						if (StringUtil.isNotEmpty(check_type)) {
							// 2、根据设备类型、检验类型、报告类型生成报告书编号
							Map<String, Object> reportSnMap = inspectionCommonService.createReportSn("saveZZJDRecord", ins_info_id, report_type, check_type, device_type);
							report_sn = String.valueOf(reportSnMap.get("report_sn"));
							insInfo.setReport_sn(report_sn);
						} else {
							if (report != null) {
								check_type = report.getReport_name().contains("制造")
										|| report.getReport_name().contains(
												"爆破片装置安全性能监督检验") || report.getReport_name().contains(
												"安全阀安全性能监督检验") ? "1" : "2";
								// 2、根据设备类型、检验类型、报告类型生成报告书编号
								Map<String, Object> reportSnMap = inspectionCommonService.createReportSn("saveZZJDRecord", ins_info_id, report_type, check_type, device_type);
								report_sn = String.valueOf(reportSnMap.get("report_sn"));
								insInfo.setReport_sn(report_sn);
							}
						}
						
						reportItemValueDao
								.createSQLQuery(
										"update TZSB_REPORT_ITEM_VALUE set item_value='"
												+ report_sn
												+ "'where fk_report_id='"
												+ report_type
												+ "' and item_name='REPORT_SN' and fk_inspection_info_id='"
												+ ins_info_id + "'")
								.executeUpdate();
					} else {
						// 报告书编号更新报告数据保存表
						reportItemValueDao
								.createSQLQuery(
										"update  TZSB_REPORT_ITEM_VALUE set item_value='"
												+ insInfo.getReport_sn()
												+ "'where fk_report_id='"
												+ report_type
												+ "' and item_name='REPORT_SN' and fk_inspection_info_id='"
												+ ins_info_id + "'")
								.executeUpdate();
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.debug(e.toString());
				}

				insInfo.setIs_report_input("2");	// 报告是否已修改过（1：未修改 2：已修改）
				insInfo.setIs_copy("0");// 不是复制报告
				insInfo.setEnter_op_id(emp.getId()); // 录入人，此处要用employee，因为报告使用的电子签名是与employee关联的
				insInfo.setEnter_op_name(emp.getName());
				// 关于报告录入日期
				// （制造监督检验，无“录入日期”数据项）
				// 电梯（定检和监检，在报告录入时，用户可自行修改录入日期）
				// 故此处每保存一次报告都更新报告录入日期为当前日期
				insInfo.setEnter_time(new Date());

				// 锅炉设计文件鉴定报告收费金额自动计算start...
				// 2017-11-28锅炉部卞庭梅提出增加
				if (report.getReport_name().contains("锅炉设计文件鉴定")) {
					double advance_fees = 0.0;
					// 报告页码
					String report_item = insInfo.getReport_item();
					// 获取报告目录信息
					List<ReportItem> reportItemList = reportItemService.queryByReportId(report_type); // 报告项目
					boolean only_sc = false;
					for (ReportItem reportItem : reportItemList) {
						if (reportItem.getItme_name().contains("审查")) {
							if (report_item.trim().split(",").length == 1
									&& report_item.trim().equals(reportItem.getPage_index())) {
								only_sc = true;
							}
						}
					}

					// 1、锅炉部件收费金额计算
					if (device_type.equals("B100")) {
						if (StringUtil.isNotEmpty(device_pressure) && StringUtil.isNotEmpty(weight_steels)) {
							if (Float.parseFloat(device_pressure) <= 2.5) {
								double money = (Float.parseFloat(weight_steels) * 10000) * 0.01;
								if (money > 5000) {
									advance_fees = 5000;
								} else {
									advance_fees = money < 300 ? 300 : money;
								}
							} else {
								double money = (Float.parseFloat(weight_steels) * 10000) * 0.007;
								if (money > 5000) {
									advance_fees = 5000;
								} else {
									advance_fees = money < 400 ? 400 : money;
								}
							}
						}
					} else {
						// 2、锅炉本体收费金额计算
						if (StringUtil.isNotEmpty(rated_output)) {
							if ("t/h".equals(rated_output_type)) {
								if (Float.parseFloat(rated_output) >= 1000) {
									advance_fees = 8000;
								} else if (Float.parseFloat(rated_output) >= 100
										&& Float.parseFloat(rated_output) < 1000) {
									advance_fees = 7000;
								} else if (Float.parseFloat(rated_output) < 100) {
									if (StringUtil.isNotEmpty(device_pressure)) {
										if (Float.parseFloat(device_pressure) <= 2.5) {
											double money = (Float.parseFloat(weight_steels) * 10000) * 0.015;
											advance_fees = money < 400 ? 400 : money;
										} else {
											double money = (Float.parseFloat(weight_steels) * 10000) * 0.0075;
											advance_fees = money > 6000 ? 6000 : money;
										}
									}
								}
							} else if ("MW".equals(rated_output_type)) {

							}
							if (only_sc) {
								advance_fees = advance_fees * 0.6;
							}
						}
					}
					insInfo.setAdvance_fees(advance_fees);
				}
				// 锅炉设计文件鉴定报告收费金额自动计算end
				
				// 保存回写的业务信息
				inspectionInfoDao.save(insInfo);
			}
			inspectoinZZJDInfoDao.save(inspectionZZJDInfo);
			// 未选择目录树时初始化RtPersonDir一个版本
			inspectionCommonService.initRtPersonDir(insInfo);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
		}
	}
	/**
	 * 批量业务获取报告数据（新报表）
	 * @param ins_info_id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getReportInfoNew(String id, String input, String page, String code_ext)
			throws Exception {
		try {
			List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
			Map<String, Object> dataMap = this.rtPageManager.loadFuncData(id, input, page);
			Map<String, Object> colorMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_COLOR);
			Map<String, Object> boldMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_BOLD);
			Map<String, Object> italicMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_ITALIC);
			Map<String, Object> sizeMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_SIZE);
			Map<String, Object> familyMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_FAMILY);
			Map<String, Object> imageMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_IMAGE);
			Map<String, Object> markMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_MARK);
			
			InspectionInfo info = inspectionInfoDao.get(id);
			InspectionZZJDInfo inspectionZZJDInfo = inspectoinZZJDInfoDao.getByInfoId(id);
			// 是否复制报告
			String iscopy = info.getIs_copy();
			// 下次检验日期加一年并减一天
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(info.getAdvance_time());
			calendar.add(Calendar.YEAR, 1);
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
			info.setLast_check_time(calendar.getTime());
			/*------------------------------------以下内容为业务信息表数据---------------------------------------------*/
			if (info.getReport_sn() != null) {
				// 报告编号
				Map<String, String> mapi = new HashMap<String, String>();
				mapi.put("name", "base__report_sn");
				mapi.put("value", info.getReport_sn());
				returnList.add(mapi);
			}
			if (info.getReport_com_name() != null) {
				// 单位名称
				Map<String, String> mapi = new HashMap<String, String>();
				mapi.put("name", "base__com_name");
				mapi.put("value", info.getReport_com_name());
				returnList.add(mapi);
			}
			if (info.getCheck_op_id() != null) {
				// 检验人员
				Map<String, String> mapi = new HashMap<String, String>();
				mapi.put("name", "base__inspect_op");
				mapi.put("value", info.getCheck_op_id());
				returnList.add(mapi);
			}
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if (info.getAdvance_time() != null) {
				// 检验日期
				Map<String, String> mapi = new HashMap<String, String>();
				mapi.put("name", "base__inspect_date");
				mapi.put("value", df.format(info.getAdvance_time()));
				returnList.add(mapi);
			}
			if (info.getEnter_op_name() != null) {
				// 录入人员前台没有选择，直接取名字
				Map<String, String> mapi = new HashMap<String, String>();
				mapi.put("name", "base__enter_op");
				mapi.put("value", info.getEnter_op_name());
				returnList.add(mapi);
			}
			if (info.getEnter_time() != null) {
				// 录入时间
				Map<String, String> mapi = new HashMap<String, String>();
				mapi.put("name", "base__enter_date");
				mapi.put("value", df.format(info.getEnter_time()));
				returnList.add(mapi);
			}
			if (info.getExamine_id() != null) {
				// 审核人员
				Map<String, String> mapi = new HashMap<String, String>();
				mapi.put("name", "base__audit_op");
				mapi.put("value", info.getExamine_id());
				returnList.add(mapi);
			}
			if (info.getExamine_Date() != null) {
				// 审核时间
				Map<String, String> mapi = new HashMap<String, String>();
				mapi.put("name", "base__audit_date");
				mapi.put("value", df.format(info.getExamine_Date()));
				returnList.add(mapi);
			}

			if (info.getIssue_name() != null) {
				// 签发人员前台没有选择，直接取名字
				Map<String, String> mapi = new HashMap<String, String>();
				mapi.put("name", "base__sign_op");
				mapi.put("value", info.getIssue_name());
				returnList.add(mapi);
			}
			
			if (info.getIssue_Date() != null) {
				// 签发时间
				Map<String, String> mapi = new HashMap<String, String>();
				mapi.put("name", "base__sign_date");
				mapi.put("value", df.format(info.getIssue_Date()));
				returnList.add(mapi);
			}
			
			/*------------------------------------以上内容为业务信息表数据---------------------------------------------*/
			
			/*------------------------------------以下内容为监督检验明细数据---------------------------------------------*/
			Map<String, Object> zzjdInfoMap = beanToMap(inspectionZZJDInfo);
			for (String key : zzjdInfoMap.keySet()) {
				if(zzjdInfoMap.get(key) != null) {
					switch(key) {
						case "MADE_UNIT_NAME":
							// 制造单位名称
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__make_units_name");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "MADE_LICENSE_CODE":
							// 制造许可证编号
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__p_zzxkzbh");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__make_licence_no");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi1);
								Map<String, String> mapi2 = new HashMap<String, String>();
								mapi2.put("name", "base__make_units_name_xkzmwjbh");
								mapi2.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi2);
								Map<String, String> mapi3 = new HashMap<String, String>();
								mapi3.put("name", "base__make_license_code");
								mapi3.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi3);
								break;
							}
						case "MADE_DATE":
							// 制造日期
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__make_date");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "DEVICE_NO":
							// 产品编号
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__factory_code");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__q_cpbh");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi1);
								break;
							}
						case "DEVICE_CODE":
							// 设备代码
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__device_code");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__device_registration_code");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi1);
								break;
							}
						case "DEVICE_PIC_NO":
							// 产品图号
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__product_th");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "DEVICE_TYPE_CODE":
							// 设备类别代码
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__device_type");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "DESIGN_UNIT_NAME":
							// 设计单位名称
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__design_com_name");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__cbz");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi1);
								break;
							}
						case "DESIGN_UNIT_CODE":
							// 设计单位机构代码
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__design_com_code");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__design_units_code");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi1);
								break;
							}
						case "DESIGN_LICENSE_CODE":
							// 设计许可证编号
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__design_com_cert");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "DESIGN_DATE":
							// 设计日期
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__b_sjrq");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__pr_sjrq");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi1);
								Map<String, String> mapi2 = new HashMap<String, String>();
								mapi2.put("name", "base__sjrq");
								mapi2.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi2);
								break;
							}
						case "INSTALL_UNIT_NAME":
							// 安装单位名称
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__construction_units_name");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "INSTALL_LICENSE_CODE":
							// 安装许可证编号
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__construction_licence_no");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__install_com_cert");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi1);
								Map<String, String> mapi2 = new HashMap<String, String>();
								mapi2.put("name", "base__p_azgzwxxkzbh");
								mapi2.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi2);
								break;
							}
						case "INSTALL_DATE":
							// 安装日期
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__construction_date");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__install_finish_dates");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi1);
								break;
							}
						case "DEVICE_PRESSURE":
							// 公称压力（设计压力）
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__device_pressure");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "DEVICE_MEDIUM":
							// 工作介质（产品适用介质）
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__work_medium");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__device_medium");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi1);
								break;
							}
						case "DEVICE_CAR_NUM":
							// 车辆牌号
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__p_pzhm");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__q_clph");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi1);
								Map<String, String> mapi2 = new HashMap<String, String>();
								mapi2.put("name", "base__device_car_num");
								mapi2.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi2);
								break;
							}
						case "CONSTRUCTION_UNIT_NAME":
							// 施工单位名称
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__build_unit");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "CONSTRUCTION_LICENSE_CODE":
							// 改造修理许可证编号
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__p_azgzwxxkzbh");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__cad_xkzh");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi1);
								break;
							}
						case "CONSTRUCTION_TYPE":
							// 施工类别
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__cad_type");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__p_sglb");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi1);
								break;
							}
						case "REPAIR_FINISH_DATE":
							// 竣工日期
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__install_finish_date");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "COM_REGISTRATION_NUM":
							// 使用登记证编号
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__registration_num");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "DEVICE_MODEL":
							// 设备型式型号
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__device_model");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								Map<String, String> mapi1 = new HashMap<String, String>();
								mapi1.put("name", "base__device_pattern");
								mapi1.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "MATERIAL_NUM":
							// 材料牌号
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__p_gzclph");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "ENGINE_NO":
							// 发动机编号
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__p_fdjxh");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "DEVICE_VOLUME":
							// 容积
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__container_capacity");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "PROJECT_NAME":
							// 工程名称
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__gcxmmc");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "DEVICE_METERIAL":
							// 设备材料（管道材质）
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__p80001005");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "DEVICE_LENGTH":
							// 设备长度（管道长度）
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__gcgm");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "DESIGN_HEAT":
							// 设计热效率
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__b_sjrxl");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "USE_TEMP2":
							// 锅炉给水（回水）温度（℃）
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__p10002011");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "BURN_MODE":
							// 燃烧方式
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__combustion_type");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "YQZDYL":
							// 要求整定压力
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__claim_setting_mpa");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "COM_ADDR":
							// 使用单位地址
							{
								Map<String, String> mapi = new HashMap<String, String>();
								mapi.put("name", "base__com_address");
								mapi.put("value", zzjdInfoMap.get(key).toString());
								returnList.add(mapi);
								break;
							}
						case "InspectionZZJD":
							break;
						case "advance_fees":
							break;
						case "inspection_date":
							break;
							default:
								{
									Map<String, String> mapi = new HashMap<String, String>();
									mapi.put("name", "base__"+key.toLowerCase());
									mapi.put("value", zzjdInfoMap.get(key).toString());
									returnList.add(mapi);
									break;
								}
					}
				}
			}
			/*------------------------------------以上内容为监督检验明细数据---------------------------------------------*/

			// 获取报名检验项目明细表数据
			List<ReportItemValue> reportItemValueList = getReportItemValueInfo(id,StringUtil.transformNull(info.getReport_type()));
			for (ReportItemValue item : reportItemValueList) {
				Map<String, String> map = new HashMap<String, String>();
				String name = item.getItem_name();
				if (!name.contains(RtPageType.TABLE) 
						&& !name.contains("record__") 
						&& !name.contains("conclusion__")
						&& !name.contains("fk")
						&& !name.contains("FK")) 
				{
					name = "base__" + name;
				}
				map.put("name", name);
				map.put("value", item.getItem_value());
				if (colorMap != null && colorMap.containsKey(item.getItem_name())
						&& colorMap.get(item.getItem_name()) != null) {
					map.put("color", colorMap.get(item.getItem_name()).toString());
				}
				if (boldMap != null && boldMap.containsKey(item.getItem_name())
						&& boldMap.get(item.getItem_value()) != null) {
					map.put("bold", boldMap.get(item.getItem_value()).toString());
				}
				if (italicMap != null && italicMap.containsKey(item.getItem_name())
						&& italicMap.get(item.getItem_name()) != null) {
					map.put("italic", italicMap.get(item.getItem_name()).toString());
				}
				if (sizeMap != null && sizeMap.containsKey(item.getItem_name())
						&& sizeMap.get(item.getItem_name()) != null) {
					map.put("size", sizeMap.get(item.getItem_name()).toString());
				}
				if (familyMap != null && familyMap.containsKey(item.getItem_name())
						&& familyMap.get(item.getItem_name()) != null) {
					map.put("family", familyMap.get(item.getItem_name()).toString());
				}
				if (imageMap != null && imageMap.containsKey(item.getItem_name())
						&& imageMap.get(item.getItem_name()) != null) {
					map.put("image", imageMap.get(item.getItem_name()).toString());
				}
				if (markMap != null && markMap.containsKey(item.getItem_name())
						&& markMap.get(item.getItem_name()) != null) {
					map.put("markContent", markMap.get(item.getItem_name()).toString());
				}
				returnList.add(map);
			}
			
			return returnList;
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
			throw e;
		}
	}
}