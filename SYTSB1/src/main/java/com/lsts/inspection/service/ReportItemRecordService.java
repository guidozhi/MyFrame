package com.lsts.inspection.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.khnt.base.Factory;
import com.khnt.bpm.core.bean.Activity;
import com.khnt.bpm.core.service.ActivityManager;
import com.khnt.bpm.core.service.ProcessManager;
import com.khnt.bpm.ext.service.FlowDefinitionManager;
import com.khnt.bpm.ext.service.FlowWorktaskManager;
import com.khnt.bpm.ext.support.FlowExtWorktaskParam;
import com.khnt.core.crud.bean.BaseEntity;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.core.crud.web.support.QueryCondition;
import com.khnt.core.exception.KhntException;
import com.khnt.pub.fileupload.bean.Attachment;
import com.khnt.pub.fileupload.dao.AttachmentDao;
import com.khnt.pub.fileupload.service.AttachmentManager;
import com.khnt.rbac.impl.bean.Employee;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rbac.impl.dao.UserDao;
import com.khnt.rbac.impl.manager.UserManagerImpl;
import com.khnt.rtbox.config.bean.RtExportData;
import com.khnt.rtbox.config.dao.RtPageDao;
import com.khnt.rtbox.config.service.RtPageManager;
import com.khnt.rtbox.template.constant.RtExportDataType;
import com.khnt.rtbox.template.constant.RtField;
import com.khnt.rtbox.template.constant.RtPageType;
import com.khnt.rtbox.template.constant.RtPath;
import com.khnt.rtbox.template.handle.export.RtSaveAsst;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.DateUtil;
import com.khnt.utils.FileUtil;
import com.khnt.utils.StringUtil;
import com.lsts.IdFormat;
import com.lsts.common.dao.CodeTablesDao;
import com.lsts.common.service.InspectionCommonService;
import com.lsts.constant.Constant;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.bean.DeviceWarningDeal;
import com.lsts.device.bean.DeviceWarningStatus;
import com.lsts.device.bean.ElevatorPara;
import com.lsts.device.bean.PressurevesselsPara;
import com.lsts.device.dao.DeviceDao;
import com.lsts.device.dao.ElevatorParaDao;
import com.lsts.device.dao.PressurevesselsParaDao;
import com.lsts.device.service.DeviceService;
import com.lsts.device.service.DeviceWarningService;
import com.lsts.device.service.DeviceWarningStatusService;
import com.lsts.employee.dao.EmployeesDao;
import com.lsts.employee.service.EmployeesService;
import com.lsts.inspection.bean.Inspection;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.bean.ReportItemRecord;
import com.lsts.inspection.bean.ReportItemValue;
import com.lsts.inspection.bean.ReportPicValue;
import com.lsts.inspection.bean.ReportRecordParse;
import com.lsts.inspection.dao.InspectionDao;
import com.lsts.inspection.dao.InspectionInfoDao;
import com.lsts.inspection.dao.ReportBHGRecordDao;
import com.lsts.inspection.dao.ReportItemRecordDao;
import com.lsts.inspection.dao.ReportItemValueDao;
import com.lsts.inspection.dao.ReportRecordParseDao;
import com.lsts.log.service.SysLogService;
import com.lsts.mobileapp.input.bean.InspectRecordDir;
import com.lsts.mobileapp.input.bean.TzsbRecordLog;
import com.lsts.mobileapp.input.dao.InspectRecordDirDao;
import com.lsts.mobileapp.input.dao.TzsbRecordLogDao;
import com.lsts.mobileapp.input.service.InspectRecordDirService;
import com.lsts.mobileapp.input.service.TzsbRecordLogService;
import com.lsts.org.bean.EnterInfo;
import com.lsts.org.dao.EnterDao;
import com.lsts.org.service.EnterService;
import com.lsts.report.bean.Report;
import com.lsts.report.bean.ReportErrorRecordInfo;
import com.lsts.report.bean.ReportItem;
import com.lsts.report.bean.SetReportItem;
import com.lsts.report.dao.ReportDao;
import com.lsts.report.dao.ReportErrorRecordInfoDao;
import com.lsts.report.service.ReportItemService;
import com.lsts.report.service.ReportService;
import com.ming.webreport.MREngine;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.TS_Util;
/**
 * 移动端原始记录检验项目参数表业务逻辑对象
 * @ClassName ReportItemRecordService
 * @JDK 1.7
 * @author GaoYa
 * @date 2015-11-13 上午11:09:00
 */
@Service("reportItemRecordService")
public class ReportItemRecordService extends EntityManageImpl<ReportItemRecord, ReportItemRecordDao> {
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static Connection conn = null;  // 数据库连接
    private static PreparedStatement pstmt = null; // 数据库操作对象
    private static ResultSet rs = null; // 结果集
    
	@Autowired
	private ReportItemRecordDao reportItemRecordDao;
	@Autowired
	private EmployeesDao employeesDao;
	@Autowired
	private ReportDao reportDao;
	@Autowired
	private InspectionInfoDao infoDao;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private InspectionDao inspectionDao;
	@Autowired
	private ReportItemValueDao reportItemValueDao;
	@Autowired
	private EnterService enterService;
	@Autowired
	private SysLogService logService;
	@Autowired
	private InspectionService inspectionService;
	@Autowired
	private InspectionInfoService inspectionInfoService;
	@Autowired
	private ReportService reportService;
	@Autowired
	FlowDefinitionManager flowDefManager;
	@Autowired
	private ProcessManager processManager;
	@Autowired
	private ActivityManager activityManager;
	@Autowired
	FlowWorktaskManager flowExtManager;
	@Autowired
	private EmployeesService employeesService;
	@Autowired
	private ReportBHGRecordDao reportBHGRecordDao;
	@Autowired
	private ReportBHGRecordService recordBHGRecordService;
	@Autowired
	private DeviceWarningStatusService deviceWarningStatusService;
	@Autowired
	private DeviceWarningService devicewarningService;
	@Autowired
	private UserManagerImpl userManager;
	@Autowired
	private ReportItemService reportItemService;
	@Autowired
	private ReportErrorRecordInfoDao reportErrorRecordInfoDao;
	@Autowired
	private ReportRecordParseDao reportRecordParseDao;
	@Autowired
	private AttachmentManager attachmentManager;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private ElevatorParaDao elevatorParaDao;
	@Autowired
	private PressurevesselsParaDao pressurevesselsParaDao;
	@Autowired
	private CodeTablesDao codeTablesDao;
	@Autowired
	private InspectionCommonService inspectionCommonService;
	@Autowired
	UserDao userDao;
	@Autowired
	EnterDao enterDao;
	
	private DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	RtPageManager rtPageManager;
	@Autowired
	RtPageDao rtPageDao;
	@Autowired
	InspectRecordDirService recordDirService;
	@Autowired
	InspectRecordDirDao recordDirDao;
	@Autowired
	AttachmentDao attachmentDao;
	@Autowired
	TzsbRecordLogService recordLogService;
	@Autowired
	ReportDao reportsDao;
	@Autowired
	TzsbRecordLogDao recordLogDao;
	@Autowired
	ReportItemValueService reportItemValueService;
	@Autowired
	ReportRecordParseService parseManager;
	@Autowired
	ReportService reportsManager;
	

	// 获取报告检验项目参数
	public List<ReportItemRecord> queryByInspectionInfoId(String info_id, String report_id){
		return reportItemRecordDao.queryByInspectionInfoId(info_id, report_id);
	}
	
	// 获取报告检验项目参数
	public List<ReportItemRecord> queryByInfoId(String info_id){
		return reportItemRecordDao.queryByInfoId(info_id);
	}
	
	// 根据业务ID获取已作废的原始记录
	public List<ReportItemRecord> queryDelByInfoId(String infoId){
		return reportItemRecordDao.queryDelByInfoId(infoId);
	}
	
	// 根据业务ID、报告类型ID、参数名称获取报告参数值
	public List<ReportItemRecord> getItemByItemName(String info_id, String report_type, String item_name){
		return reportItemRecordDao.getItemByItemName(info_id, report_type, item_name);
	}
		
	// 更新报告检验项目参数值
	public void updateItemRecord(String item_id, String info_id, String report_id, String item_name, String item_value){
		try {
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			ReportItemRecord reportItemRecord = reportItemRecordDao.get(item_id);
			reportItemRecord.setFk_report_id(report_id);
			reportItemRecord.setFk_inspection_info_id(info_id);
			reportItemRecord.setItem_name(item_name);
			reportItemRecord.setItem_value(item_value);
			reportItemRecord.setItem_type("string");
			reportItemRecord.setLast_mdy_uid(user.getId());
			reportItemRecord.setLast_mdy_uname(user.getName());
			reportItemRecord.setLast_mdy_time(DateUtil.getCurrentDateTime());
			reportItemRecord.setData_status("1");		// 数据状态，默认0（0：新建 1：修改 99：删除）
			reportItemRecordDao.save(reportItemRecord);	
			//reportItemRecordDao.createSQLQuery("update tzsb_report_item_record set fk_report_id='"+report_id+"', fk_inspection_info_id='"+info_id+"', item_name='"+item_name+"',item_value='"+item_value+"',item_type='String' where id='"+item_id+"'").executeUpdate();
		} catch (Exception e) {
			log.debug(e.toString());
			e.printStackTrace();
		}
	}
	
	// 删除原始记录检验项目参数记录
	public void delRecordItems(String info_id) {
		try {
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			reportItemRecordDao
					.createSQLQuery(
							"update tzsb_report_item_record set DATA_STATUS='99',last_mdy_uid=?,last_mdy_uname=?,last_mdy_time=? where fk_inspection_info_id =? and upper(item_name) <> 'REPORT_SN'",
							user.getId(), user.getName(),
							DateUtil.getCurrentDateTime(), info_id)
					.executeUpdate();
		} catch (Exception e) {
			log.debug(e.toString());
			e.printStackTrace();
		}
	}
	
	// 删除原始记录检验项目参数记录
	public void delRecordItem(String info_id, String report_id, String item_name) {
		try {
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			reportItemRecordDao
					.createSQLQuery(
							"update tzsb_report_item_record set DATA_STATUS='99',last_mdy_uid=?,last_mdy_uname=?,last_mdy_time=? where fk_inspection_info_id =? and fk_report_id=? and item_name=?",
							user.getId(), user.getName(),
							DateUtil.getCurrentDateTime(), info_id, report_id, item_name)
					.executeUpdate();
		} catch (Exception e) {
			log.debug(e.toString());
			e.printStackTrace();
		}
	}
	
	public List<Map<String, Object>> queryRecordInfos(String infoIds) {
		return reportItemRecordDao.queryRecordInfos(infoIds);
	}
	
	public List<Map<String, Object>> queryGcRecordInfos(String infoIds) {
		return reportItemRecordDao.queryGcRecordInfos(infoIds);
	}
	
	/**
	 * 移动端原始记录数据上传
	 * @param json
	 * @param map 
	 * @param request 
	 * @return 
	 * @throws KhntException
	 * @throws ParseException 
	 */
	public synchronized HashMap<String, Object> saveMobileInsp(JSONObject json, HashMap<String, Object> map,
			HttpServletRequest request) throws KhntException, ParseException{
		//CurrentSessionUser user = SecurityUtil.getSecurityUser();
		
		// 获取原始记录项目总数，验证实际上传数量是否一致，不一致时提示失败请重试
		int item_num = 0;
		String item_count = json.getString("item_count");
		if (StringUtil.isNotEmpty(item_count)) {
			item_num = Integer.parseInt(item_count);
		}else{
			String msg = "亲，app已发布新版本，优化了提交性能，请先更新再使用提交功能！";
			map.put("success", false);
			map.put("msg", msg);
			return map;
		}
		
		String device_registration_code = "";
		String internal_num = "";
		Object listdatas = json.get("reportitemvalue");
		if (listdatas != null && !"".equals(listdatas.toString()) && !"null".equals(listdatas.toString())) {
			JSONArray listdatas1 = JSONArray.fromObject(listdatas.toString());
			if (listdatas1.length() != item_num) {
				// 设备注册代码
				device_registration_code = json.getString("device_registration_code");
				
				if(StringUtil.isEmpty(device_registration_code)){
					// 单位内编号
					internal_num = json.getString("internal_num");
				}
				String msg = "亲，当前网络不稳定，设备（";
				if(StringUtil.isNotEmpty(device_registration_code)){
					msg += device_registration_code+"）原始记录提交失败，请重试！";
				}else{
					msg += internal_num+"）原始记录提交失败，请重试！";
				}
				map.put("success", false);
				map.put("msg", msg);
				return map;
			}
		}
		
		//获取总表（检验信息）
		Object sumtable = json.get("sumtable");
		String infoId = null;	
		Inspection inspection = null;
		InspectionInfo info = null;
		String check_type = "";		// 检验类别（3：定检 2：监检）
		String report_code = "";	// 报告类别编号
		String report_id = "";		// 报告ID
		String submitid = "";		// 移动端数据传参ID
		String com_name = "";		// 使用单位名称
		String check_op_id = "";	// 参检人员ID
		String check_op_name = "";	// 参检人员
		String inspection_date = "";// 检验日期
		String device_type = "";	// 设备类型
		String device_sort_code = "";	// 设备类别
		String check_status = "";	// 检验状态，默认为0（0：正常检验 1：中止检验）中止检验的原始记录，不能生成报告
		String report_item = "";	// 报告页码
		String ysjl_item = "";		// 原始记录页码
		String xsqts = "";			// 限速器个数
		String report_name = "";	// 报告名称
		String ysjl_name = "";		// 原始记录名称
		String report_version = "";	// 原始记录版本号（为空代表老版本、"1"代表2号修改单新版本、"2"代表西藏的版本、"3"代表新疆的版本、"4"代表九寨沟的版本）
		String random_code = "";	// 原始记录随机码（用于校验原始记录是否已提交）
		int dtcs = 0; 				// 电梯层数
		String device_qr_code = "";		// 二维码编号
		String device_use_place = "";	// 设备所在地（...路...号...栋）
		String device_place_road = "";	// 设备所在地（...路/街）
		String device_place_no = "";	// 设备所在地（...号）
		String device_place_num = "";	// 设备所在地（...栋）
		String registration_num = "";	// 使用登记证号
		String inspection_conclusion = "";	//检验结论
		boolean canSave = false;	// 本次是否可提交保存数据
		
		Map<String, String> old_Map = new HashMap<String, String>();	// 原始记录修改前的内容，用作修改内容对比
		Map<String, String> new_Map = new HashMap<String, String>();	// 原始记录修改后的新内容，用作修改内容对比
		DeviceDocument deviceDocument = null;
		EnterInfo companyInfo = null;
		JSONObject sumTable = null;
		Date inspectiondate = null;
		Date end_date = null;
		Date cur_date = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.getCurrentDateTime());
		
		try {
			// 1、保存检验业务信息
			// 获取人员信息
			String userId = json.getString("userId");		// 用户ID（sys_user）
			String userName = json.getString("userName");	// 用户姓名（sys_user）
			User user = userManager.get(userId);
			// 获取employee信息（报告书内所有业务人员全部使用的是employee，故此处要获取employee表数据）
			Employee employee = employeesDao.queryEmployeeByUser(userId, userName);
			
			// 获取报检单位信息（设备使用单位ID）
			String fk_unit_id = json.getString("fk_company_info_use_id");
			
			// 获取检验部门ID（录入部门ID）
			String check_unit_id = json.getString("depId");
			if (StringUtil.isEmpty(check_unit_id)) {
				check_unit_id = employee.getOrg().getId();
			}
			
			if(sumtable!=null&&!"".equals(sumtable)){
				sumTable = JSONObject.fromObject(sumtable.toString());
				
				// 获取使用单位名称
				com_name = sumTable.getString("use_company");	
				// 判断是初次上传还是修改上传
				if (sumTable.get("submitid")!=null&&!"".equals(sumTable.get("submitid"))&&!"null".equals(sumTable.get("submitid").toString())) {
					//存在submitid，说明已经上传过，上传过进行修改操作
					submitid = sumTable.getString("submitid");
					info = infoDao.get(submitid);
					inspection = info.getInspection();
					
					// 获取原始记录修改前的内容
					old_Map = this.queryRecordByInfoId(submitid);
				}
				
				// 获取原始记录版本号（为空代表老版本，"1"代表2号修改单新版本、"2"代表西藏的版本、"3"代表新疆的版本、"4"代表九寨的版本）
				report_version = sumTable.getString("report_version");
				// 原始记录随机码（用于校验原始记录是否已提交）
				random_code = sumTable.getString("random_code");
				if(StringUtil.isEmpty(random_code)){
					String msg = "亲，当前网络不稳定，设备（"+ device_registration_code+"）原始记录提交失败，请重试！";
					map.put("success", false);
					map.put("msg", msg);
					return map;
				}else{
					if(info == null){
						List<InspectionInfo> infos = infoDao.getInfoByRandomcode(random_code);
						if(!infos.isEmpty()){
							info = infos.get(0);
							inspection = info.getInspection();
							
							// 获取原始记录修改前的内容
							old_Map = this.queryRecordByInfoId(info.getId());
						}
					}
				}
				
				if(info == null){
					// 新建报检信息表
					inspection = new Inspection();
					
					// 新建检验业务信息表
					info = new InspectionInfo();
					// 业务流水号，用户进去系统提交业务时，再生成业务流水号
					//info.setSn(this.getInspectionInfoSn(1));	
					info.setCreate_time(new Date());
				}
				
				if (StringUtil.isEmpty(info.getFlow_note_name()) || "报告录入".equals(info.getFlow_note_name())) {
					canSave = true;
				}else{
					map.put("success", false);
					map.put("msg", "亲，报告当前正在审批中，不能修改哦！如需修改，请先确保报告已退回至报告录入阶段！已封存的报告，请提交纠错申请后再重新录入原始记录！");
					return map;
				}
				
				if(canSave){
					// 获取设备ID
					String device_id = sumTable.getString("device_id");
					if(StringUtil.isNotEmpty(submitid)){
						device_id = info.getFk_tsjc_device_document_id();
					}
					if(StringUtil.isNotEmpty(device_id)){
						info.setFk_tsjc_device_document_id(device_id);	// 设备ID
						deviceDocument =  deviceDao.get(device_id);
					}else{
						if(StringUtil.isEmpty(submitid)){
							deviceDocument = new DeviceDocument();
						}
					}
					
					// 获取报告类别编号
					report_code = sumTable.getString("report_type");
					// 获取原始记录名称
					ysjl_name = sumTable.getString("reportname");	
					// 根据报告类别编号、原始记录版本号获取报告ID
					report_id = reportDao.queryReportId(report_code, report_version);
					if(StringUtil.isEmpty(report_id)){
						map.put("success", false);
						map.put("msg", "亲，系统暂未找到该原始记录对应的报告模板，请联系系统管理员！");
						return map;
					}else{
						// 获取报告基本信息
						Report report = reportService.get(report_id);
						if(report==null){
							map.put("success", false);
							map.put("msg", "亲，系统暂未找到该原始记录对应的报告模板，请联系系统管理员！");
							return map;
						}else{
							if(StringUtil.isNotEmpty(report.getYsjl_pages())){
								ysjl_item = report.getYsjl_pages();
							}
							if(StringUtil.isEmpty(report_name) && StringUtil.isNotEmpty(report.getReport_name())){
								report_name = report.getReport_name();
							}
						}
						List<ReportItem> reportItemList = reportItemService.queryByReportId(report_id);
						// 遍历报告模板中的所有页面
						for (ReportItem reportItem : reportItemList) {
							if(report_item.length()>0){
								report_item += ",";
							}
							report_item += reportItem.getPage_index();
						}
						
						// report_version为空代表原始记录模板为2号修改单以前的老版本
						if (StringUtil.isEmpty(report_version)) {
							if (StringUtil.isNotEmpty(xsqts)) {
								if ("1".equals(xsqts)) {
									// 一个限速器，去掉最后1页
									report_item = report_item.substring(0, report_item.length() - 2);
									ysjl_item = ysjl_item.substring(0, ysjl_item.length() - 2);
								} else if ("0".equals(xsqts)) {
									// 无限速器，去掉最后2页
									// 有机房01/02、无机房03/04、液压05/06、杂物09/10
									// 病床有机房18/19、病床无机房20/21
									// 定检/监检有限速器
									if (StringUtil.isEmpty(report_version)) {
										if ("01".equals(report_code) || "02".equals(report_code)
												|| "03".equals(report_code) || "04".equals(report_code)
												|| "05".equals(report_code) || "06".equals(report_code)
												|| "09".equals(report_code) || "10".equals(report_code)
												|| "18".equals(report_code) || "19".equals(report_code)
												|| "20".equals(report_code) || "21".equals(report_code)) {
											report_item = report_item.substring(0, report_item.length() - 4);
											ysjl_item = ysjl_item.substring(0, ysjl_item.length() - 4);
										}
									}
								}
							} else {
								xsqts = "0";
								// 无限速器，去掉最后2页
								// 有机房01/02、无机房03/04、液压05/06、杂物09/10
								// 病床有机房18/19、病床无机房20/21
								// 定检/监检有限速器
								if (StringUtil.isEmpty(report_version)) {
									if ("01".equals(report_code) || "02".equals(report_code) || "03".equals(report_code)
											|| "04".equals(report_code) || "05".equals(report_code)
											|| "06".equals(report_code) || "09".equals(report_code)
											|| "10".equals(report_code) || "18".equals(report_code)
											|| "19".equals(report_code) || "20".equals(report_code)
											|| "21".equals(report_code)) {
										report_item = report_item.substring(0, report_item.length() - 4);
										ysjl_item = ysjl_item.substring(0, ysjl_item.length() - 4);
									}
								}
							}
						}else{
							if("4".equals(report_version)){
								if (StringUtil.isNotEmpty(xsqts)) {
									if("0".equals(xsqts)){
											if ("01".equals(report_code)
													|| "02".equals(report_code)
													|| "03".equals(report_code)
													|| "04".equals(report_code)
													|| "05".equals(report_code)
													|| "06".equals(report_code)
													|| "09".equals(report_code)
													|| "10".equals(report_code)
													|| "18".equals(report_code)
													|| "19".equals(report_code)
													|| "20".equals(report_code)
													|| "21".equals(report_code)) {
												report_item = report_item.substring(0,
														report_item.length() - 2);
												ysjl_item = ysjl_item.substring(0,
														ysjl_item.length() - 3);
											}
									}
								}else{
									xsqts = "0";
										if ("01".equals(report_code)
												|| "02".equals(report_code)
												|| "03".equals(report_code)
												|| "04".equals(report_code)
												|| "05".equals(report_code)
												|| "06".equals(report_code)
												|| "09".equals(report_code)
												|| "10".equals(report_code)
												|| "18".equals(report_code)
												|| "19".equals(report_code)
												|| "20".equals(report_code)
												|| "21".equals(report_code)) {
											report_item = report_item.substring(0,
													report_item.length() - 2);
											ysjl_item = ysjl_item.substring(0,
													ysjl_item.length() - 3);
										}
									}
							
							}
						}
					}
					info.setReport_type(report_id);			// 报告类型ID
					
					// 获取二维码编号
					device_qr_code = sumTable.getString("device_qr_code");	
					// 获取使用登记证号
					registration_num = sumTable.getString("registration_num");	
					// 原始记录版本号:"2"代表西藏的版本，因西藏设备不存在金属二维码和使用登记账号，故不做任何验证
					// 原始记录版本号:"3"代表新疆的版本，因新疆设备不存在金属二维码和使用登记账号，故不做任何验证
					// 原始记录版本号:"4"代表九寨的版本，因九寨设备不存在金属二维码和使用登记账号，故不做任何验证
					if(!"2".equals(report_version) && !"3".equals(report_version) && !"4".equals(report_version)){
						if(StringUtil.isEmpty(device_qr_code)){
							map.put("success", false);
							map.put("msg", "亲，二维码编号是必填项目，请补充完整再提交！");
							return map;
						}else{
							// 验证二维码编号是否重复
							/*String deviceid = deviceService.getDeviceIdByQRCode(device_qr_code);
							if(StringUtil.isNotEmpty(deviceid)){
								if(StringUtil.isNotEmpty(device_id)){
									if(!deviceid.equals(device_id)){
										map.put("success", false);
										map.put("msg", "亲，二维码编号重复了，请纠正再提交！");
										return map;
									}
								}else{
									map.put("success", false);
									map.put("msg", "亲，二维码编号重复了，请纠正再提交！");
									return map;
								}
							}*/	
							List<DeviceDocument> list = deviceService.getDeviceInfosByQRCode(device_id, device_qr_code);
							for(DeviceDocument device : list){
								if(device != null){
									if(StringUtil.isNotEmpty(device_id)){
										if(StringUtil.isNotEmpty(device.getId())){
											if(!device.getId().equals(device_id)){
												map.put("success", false);
												map.put("msg", "亲，二维码编号（"+device_qr_code+"）与设备（"+device.getDevice_registration_code()+"）重复了，请纠正再提交！");
												return map;
											}
										}
									}else{
										if(StringUtil.isNotEmpty(device.getId())){
											map.put("success", false);
											map.put("msg", "亲，二维码编号（"+device_qr_code+"）与设备（"+device.getDevice_registration_code()+"）重复了，请纠正再提交！");
											return map;
										}
									}
								}
							}
							deviceDocument.setDevice_qr_code(device_qr_code);
							info.setDevice_qr_code(device_qr_code);
						}
						
						// 验证使用登记证号是否重复
						if(StringUtil.isEmpty(registration_num)){
							map.put("success", false);
							map.put("msg", "亲，使用登记证号是必填项目，请补充完整再提交！");
							return map;
						}else{
							List<DeviceDocument> list = deviceService.getDeviceInfosByRegistrationNum(device_id, registration_num);
							for(DeviceDocument device : list){
								if(device != null){
									if(StringUtil.isNotEmpty(device_id)){
										if(StringUtil.isNotEmpty(device.getId())){
											if(!device.getId().equals(device_id)){
												map.put("success", false);
												map.put("msg", "亲，使用登记证号与设备（"+device.getDevice_registration_code()+"）重复了，请纠正再提交！");
												return map;
											}
										}
									}else{
										if(StringUtil.isNotEmpty(device.getId())){
											map.put("success", false);
											map.put("msg", "亲，使用登记证号与设备（"+device.getDevice_registration_code()+"）重复了，请纠正再提交！");
											return map;
										}
									}
								}
							}
							deviceDocument.setRegistration_num(registration_num);
							info.setRegistration_num(registration_num);
						}
					}
					// 获取检验类别
					check_type = sumTable.getString("check_type");	// 检验类别（3：定检 2：监检）
					// 验证设备2个月内是否已录入报告
					if (deviceDocument != null) {
						if(StringUtil.isNotEmpty(deviceDocument.getDevice_registration_code())){
							// 定检报告，验证同一个设备2个月内是否已录入报告，有纠错程序除外（质量部要求）
							// 检验类别（3：定检 2：监检）
							// 2017-08-22 质量部谢方提出修改，增加不同的报告模板出具时，则不受此限制
							// 2017-08-22 张展彬提出修改
							// 配合2017年联合国世界旅游组织第22届全体大会保障性检验，暂时放开电梯定检报告出具周期限制
							// 2017-09-11 张展彬提出修改，重启开启电梯定检报告出具周期限制
							// 2017-09-12 张展彬提出修改，暂时放开电梯定检报告出具周期限制
							// 2017-09-12 增加电梯定检报告出具周期（2个月）限制开关可控功能
							String vali = Factory.getSysPara().getProperty("DT_VALIDATE_REPEAT_2M");
							boolean validate_repeat = true;
							if(StringUtil.isNotEmpty(vali)){
								if("true".equals(vali.trim()) || "false".equals(vali.trim())){
									validate_repeat = Boolean.parseBoolean(vali.trim());
								}
							}
							if(validate_repeat){
								if ("3".equals(check_type)) {
									String inspection_id = info.getInspection() != null ? info.getInspection().getId() : "";
									List<InspectionInfo> info_list = infoDao
											.getNewInfoByDeviceId(info.getFk_tsjc_device_document_id(), inspection_id);
									if (info_list != null) {
										if (info_list.size() > 0) {
											for (InspectionInfo old_info : info_list) {
												if (StringUtil.isNotEmpty(old_info.getInspection_conclusion())) {
													if (!"不合格".equals(old_info.getInspection_conclusion().trim())) {
														// 质量部谢方2017-08-22提出修改，增加不同的报告模板出具时，则不受此限制
														if(old_info.getReport_type().equals(info.getReport_type())){
															HashMap<String, Object> returnMap = validateRepeat(old_info,
																	deviceDocument.getDevice_registration_code());
															if (!Boolean.valueOf(String.valueOf(returnMap.get("success")))) {
																return returnMap;
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
						
						if(StringUtil.isNotEmpty(deviceDocument.getDevice_sort())){
							device_type = deviceDocument.getDevice_sort();
						}
						if(StringUtil.isNotEmpty(deviceDocument.getDevice_sort_code())){
							device_sort_code = deviceDocument.getDevice_sort_code();
						}
						
						companyInfo = deviceDocument.getEnterInfo();
						if (companyInfo == null) {
							companyInfo = enterService.get(fk_unit_id);
						}
						if (companyInfo != null) {
							info.setCheck_op(companyInfo.getCom_contact_man()); // 检验联系人
							info.setCheck_tel(companyInfo.getCom_tel()); // 检验联系电话
							info.setReport_com_address(companyInfo.getCom_address()); // 使用单位地址
						}
					}
					
					// 限速器个数
					xsqts = sumTable.getString("xsqts");
					if(StringUtil.isEmpty(xsqts) || "null".equals(xsqts)){
						xsqts = "0";
					}
					
					// 获取参检人员id
					check_op_id = sumTable.getString("check_op_id");
					// 获取参检人员姓名
					check_op_name = sumTable.getString("check_op_name");
					// 获取检验状态
					check_status = sumTable.getString("check_status");
					
					/*
					 * 上传数据不进行是否存在报告模板的验证，由用户在系统手动启动流程时验证
					if (StringUtil.isEmpty(report_id)) {
						map.put("success", false);
						map.put("msg", "抱歉，系统暂未找到与该原始记录相匹配的报告模板，请联系系统管理员！");
					}
					*/
					
					inspection.setCheck_type(check_type);	// 检验类别（3：定检 2：监检）
					inspection.setFk_unit_id(fk_unit_id);	// 报检单位ID
					inspection.setCom_name(com_name);		// 报检单位名称
					inspection.setEnter_op(employee.getName());	// 录入人员
					inspection.setInspection_time(Timestamp.valueOf(timeformat.format(new Date())));
					inspection.setData_status("0");			// 0：新建 1:使用 2：删除
					
					// 获取检验日期，并计算下次检验日期
					inspection_date = sumTable.getString("inspection_date");
					if(StringUtil.isEmpty(inspection_date)){
						inspection_date = DateUtil.getDateTime("yyyy-MM-dd", new Date());
					}else{
						if (inspection_date.contains("/")) {
							Date date = dateformat.parse(
								(inspection_date.replace("/", "-")).toString());
							info.setAdvance_time(date);
						}else if(inspection_date.contains(".")){
							Date date = dateformat.parse(
									(inspection_date.replace(".", "-")).toString());
							info.setAdvance_time(date);
						}else{
							info.setAdvance_time(DateUtil.convertStringToDate(Constant.defaultDatePattern, inspection_date));
						}
						
						// 自动计算下次检验日期，下次检验日期=（检验日期 + 1年）- 1天
						Date inspectionDate = info.getAdvance_time();
						if (inspectionDate != null) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(inspectionDate);
							calendar.add(Calendar.YEAR, 1);	
							if(StringUtil.isNotEmpty(device_type)){
								if (!device_type.startsWith("9")) {
									//	客运索道（device_sort_code为9开头的4位数字）报告只显示到年月，不需要精确到年月日，故此处不用减去一天
									calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
								}
							}else{
								// 暂无设备的情况，默认下次检验日期=（检验日期 + 1年）- 1天
								calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
							}
							info.setLast_check_time(calendar.getTime());
						}
					}
					
					info.setReport_com_name(com_name);		// 使用单位名称
					info.setReport_type(report_id);			// 报告类型ID
					info.setReport_item(report_item);		// 报告页码（多个页码，以逗号分隔）
					info.setYsjl_item(ysjl_item);			// 原始记录页码（多个页码，以逗号分隔）
					info.setRandom_code(random_code); 		// 原始记录随机码（用于校验原始记录是否已提交）
					info.setXsqts(xsqts);					// 限速器个数
					info.setEnter_op_id(employee.getId());		// 录入人ID
					info.setEnter_op_name(employee.getName());	// 录入人姓名
					info.setIs_report_confirm("0");	// 原始记录是否已经校核，默认为0（0：未校核 1：校核通过 2：校核未通过）	
					info.setCheck_op_id(check_op_id);		// 参检人员id			
					info.setCheck_op_name(check_op_name);	// 参检人员姓名		
					info.setCheck_unit_id(check_unit_id);	// 检验部门ID
					info.setEnter_unit_id(check_unit_id);	// 录入部门ID
					info.setIs_back("1");		// 默认的状态为1，提交后变成0，退回也变成1
					info.setIs_copy("0");		// 是否是复制报告（0：非复制报告 1：复制报告）
					info.setIs_mobile("1");		// 是否移动检验（0：否 1：是）
					//info.setIs_flow("1");		// 判断是否已经启动流程（1 未进入流程 2 进入流程）
					info.setIs_flow(null);		
					info.setCheck_category_code(check_type);	// 检验类型代码（1：制造监检 2：安改维监检 3：定检  4：委托检验......）	
					info.setCheck_category_name(codeTablesDao.getValueByCode("BASE_CHECK_TYPE", check_type));	// 检验类型名称
					
					// 自动计算录入/编制日期（编制日期=检验日期+1天）
					if(device_type.startsWith("3") || device_type.startsWith("4") || device_type.startsWith("6") || device_type.startsWith("9")){
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(info.getAdvance_time());
						calendar.add(Calendar.DATE, 1);	
						// 检验日期为当天的，编制日期不再+1，默认为当天
						Date draft_date = null;
						if(calendar.getTime().after(cur_date)){
							draft_date = cur_date;
						}else{
							draft_date = calendar.getTime();
						}
						info.setEnter_time(draft_date);
					}else{
						info.setEnter_time(new Date());		// 录入日期
					}
					
					// 未启动流程前，业务数据状态都为初始状态不进入正常使用流程
					if(StringUtil.isEmpty(submitid)){
						info.setData_status("0");		// 报告数据状态（0：初始（未生成报告） 1：正常 99：删除）
						info.setFee_status("0");		// 收费状态（0：初始）未启动流程前，收费状态都为初始状态不进入待收费流程
						info.setYsjl_data_status("0");	// 原始记录数据状态（0：未生成报告 1：已生成报告 2：原始记录已修改未生成报告）
					}else{
						//info.setData_status("2");		
						info.setYsjl_data_status("2");
					}								
					info.setCheck_status(check_status);	// 检验状态，默认为0（0：正常检验 1：中止检验）
														// 中止检验的原始记录，不能生成报告
					inspectionDao.save(inspection);
					
					info.setInspection(inspection);
					info.setLast_mdy_time(new Date());
					info.setIs_print_ysjl("0");//原始记录打印状态（0：未打印 1：已打印）
					infoDao.save(info);
					
					infoId = info.getId();
					logService.setLogs(infoId, "上传原始记录", "移动端原始记录数据上传", userId, userName, new Date(), request.getRemoteAddr());
				}
			}
			
			// 2、保存检验项目参数表信息
			// itemvalue 数据项信息保存
			if(canSave){
				//Object listdatas = json.get("reportitemvalue");
				if(listdatas != null && !"".equals(listdatas.toString()) && !"null".equals(listdatas.toString())){
					// 再次上传数据时，删除初次上传的原始记录
					if(StringUtil.isNotEmpty(submitid)){
						this.delRecordItems(submitid);	// 删除原始记录
					}

					JSONArray listdatas1 = JSONArray.fromObject(listdatas.toString());
					for (int i = 0; i < listdatas1.length(); i++) {
						Object reportItemObject = listdatas1.get(i);
						JSONObject itemValue = JSONObject.fromObject(reportItemObject.toString());
						String item_key = itemValue.getString("key");
						String item_value = itemValue.getString("value");
						if(StringUtil.isNotEmpty(item_key)){
							// 获取电梯层数		
							if("P30002005".equals(item_key)){
								try {
									dtcs = Integer.parseInt(item_value.trim());
								} catch (Exception e) {
									dtcs = 0;
								}
							}
							// 获取设备类型
							if("DEVICE_SORT".equals(item_key)){
								device_type = item_value.trim();
							}
							// 获取设备类别
							if("DEVICE_SORT_CODE".equals(item_key)){
								device_sort_code = item_value.trim();
							}
							// 获取检验结论
							if("INSPECTION_CONCLUSION".equals(item_key)){
								inspection_conclusion = item_value.trim();
							}
							
							// 生成检验结论报告下次检验日期和限速器下次检验日期
							if("INSPECTION_DATE".equals(item_key)){
								inspectiondate = DateUtil.convertStringToDate(Constant.defaultDatePattern, item_value.trim());
								if(inspectiondate.after(cur_date)){
									map.put("success", false);
									map.put("msg", "亲，现场检验日期不能晚于今天，请修改后再提交！");
									return map;
								}
								
								Date last_check_date = info.getLast_check_time();
								if(last_check_date == null){
									if (inspectiondate != null) {
										Calendar calendar = Calendar.getInstance();
										calendar.setTime(inspectiondate);
										calendar.add(Calendar.YEAR, 1);	
										if(StringUtil.isNotEmpty(device_type)){
											if (!device_type.startsWith("9")) {
												//	客运索道（device_sort_code为9开头的4位数字）报告只显示到年月，不需要精确到年月日，故此处不用减去一天
												calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
											}
										}else{
											// 暂无设备的情况，默认下次检验日期=（检验日期 + 1年）- 1天
											calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
										}
										last_check_date = calendar.getTime();
										info.setLast_check_time(last_check_date);
									}
								}
								// 自动生成检验结论报告下次检验日期
								reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
										report_id, "LAST_INSPECTION_DATE", DateUtil.format(last_check_date, Constant.defaultDatePattern), infoId, userId, userName);
								
								if(StringUtil.isNotEmpty(xsqts)){
									if(!"0".equals(xsqts)){
										if(last_check_date != null){
											Calendar calendar = Calendar.getInstance();
											calendar.setTime(last_check_date);
											// 限速器的下次检验日期，为结论页下次检验日期的基础上再翻1年
											calendar.add(Calendar.YEAR, 1);	
											String xsq_last_check_date = DateUtil.getDate(calendar
													.getTime());
											// 自动生成限速器下次检验日期
											reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
													report_id, "XSQ_LAST_INSPECTION_DATE", xsq_last_check_date, infoId, userId, userName);
										}
									}
								}
							}
							// 完成检验日期
							if("INSPECTION_DATE_END".equals(item_key)){
								end_date = DateUtil.convertStringToDate(Constant.defaultDatePattern, item_value.trim());
								if(end_date.after(cur_date)){
									map.put("success", false);
									map.put("msg", "亲，检验完成日期不能晚于现场检验日期，请修改后再提交！");
									return map;
								}
							}
							
							if(inspectiondate != null && end_date!=null){
								if(inspectiondate.after(end_date)){
									map.put("success", false);
									map.put("msg", "亲，检验完成日期不能早于今天，请修改后再提交！");
									return map;
								}
							}
							
							// 返写检验类别1
							if ("SGLB".equals(item_key)) {	// SGLB：施工类别（监检）
								if(device_type.startsWith("4")){	// 4：起重机
									info.setCheck_type_code(item_value.trim());	// 检验类别代码（起重机监检：1：新装、2：移装、3：改造、4：重大修理）
									if("1".equals(item_value.trim())){
										info.setCheck_type_name("新装");
									}else if("2".equals(item_value.trim())){
										info.setCheck_type_name("移装");
									}else if("3".equals(item_value.trim())){
										info.setCheck_type_name("改造");
									}else if("4".equals(item_value.trim())){
										info.setCheck_type_name("重大修理");
									}
								}else if(device_type.startsWith("3")){// 3：电梯（安装、改造、修理）
									info.setCheck_type_code(item_value.trim());	
									info.setCheck_type_name(item_value.trim());	// 检验类别名称（电梯监检：安装、改造、修理）
								}
							}

							// 返写检验类别2
							if ("CHECK_TYPE".equals(item_key)) {// CHECK_TYPE：检验类别（定期、首次）
								if(device_type.startsWith("4")){	// 4：起重机
									info.setCheck_type_code(item_value.trim());
									info.setCheck_type_name(item_value.trim());
								}
							}
							
							// 返写设备注册代码到info业务表，以供报告查询
							if ("DEVICE_REGISTRATION_CODE".equals(item_key)) {
								info.setDevice_registration_code(item_value.trim());
							}
							
							// 九寨沟设备所在地之路（街）
							if ("DEVICE_PLACE_ROAD".equals(item_key)) {
								device_place_road = item_value.trim();
							}
							// 九寨沟设备所在地之号
							if ("DEVICE_PLACE_NO".equals(item_key)) {
								device_place_no = item_value.trim();
							}
							// 九寨沟设备所在地之栋
							if ("DEVICE_PLACE_NUM".equals(item_key)) {
								device_place_num = item_value.trim();
							}
							
							// 保存原始记录检验项目记录表（新增）
							reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
									report_id, item_key, item_value, infoId, userId, userName);
							/* 上传原始记录时，不直接生成报告检验项目参数，改由用户手动启动流程时，从原始记录表中获取数据再生成
							// 初次上传数据，才生成报告检验项目参数表
							// 再次上传数据时，不再生成报告检验项目参数表
							if (StringUtil.isEmpty(submitid)) {
								// 保存报告检验项目参数表（新增）
								reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
										report_id, itemValue.getString("key"),
										itemValue.getString("value"),infoId);
							}
							*/
							// 设置设备信息
							deviceDocument = updateDeviceInfo(deviceDocument, item_key, item_value);
						}
					}
					
					if (sumTable.get("submitid")==null || "".equals(sumTable.get("submitid")) || "null".equals(sumTable.get("submitid").toString())) {
						// 不存在submitid，说明是初次上传，初次上传执行新增操作
						// 往原始记录检验项目记录表里，增加报告书编号，默认为空，生成报告后赋值
						reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
								report_id, "REPORT_SN", "", infoId, userId, userName);
					}
					
					
					// 因九寨沟的设备所在地拆分了路（街）、号、栋，此处拼接处理后返写设备所在地
					// report_version：4为九寨沟设备
					if("4".equals(report_version)){
						device_use_place = device_place_road.trim() + device_place_no.trim() + device_place_num.trim();
						deviceDocument.setDevice_use_place(device_use_place);
					}
					// 返写报告业务信息和设备信息（监检生成设备注册代码）
					updateInfos(deviceDocument, report_id, infoId, userId, userName);
					
					if(StringUtil.isEmpty(device_type)){
						map.put("success", false);
						map.put("msg", "亲，您提交的原始记录无设备类别无法计算费用哦，请完善后再提交！");
						return map;
					}
					if(StringUtil.isEmpty(device_sort_code)){
						map.put("success", false);
						map.put("msg", "亲，您提交的原始记录无设备名称无法计算费用哦，请完善后再提交！");
						return map;
					}
					
					// 计算电梯收费金额（2016-03-09杜院要求上传数据的同时计算费用，方便检验员查询费用）
					// 2017-12-14应明子涵要求，新疆特检突击队报告费用由检验员手动输入，不再自动计算价格biu~biu~	
					// 2018-03-29应明子涵要求，九寨特检突击队报告费用由系统自动计算价格biu~biu~
					// 九寨沟的计算公式均无1.3和1.5的系数，故此处去掉
					if(StringUtil.isNotEmpty(check_unit_id)){
						if (!Constant.CHECK_UNIT_100090.equals(check_unit_id) && !Constant.CHECK_UNIT_100091.equals(check_unit_id)) {
							double dt_money = 0;	// 不打折，也不四舍五入
							if (report_name.contains("病床")) {
								if (check_type.equals("3")) {// 定期检验
									// 判断是否有限速器
									if (xsqts.equals("0")) {// 没有勾选限速器
										// 判断电梯层数
										if (dtcs <= 5) {
											dt_money = (int) Math.round((500));
										} else {
											dt_money = (int) Math.round(500 + (dtcs - 5) * 50);
										}
									} else if (xsqts.equals("1")) {// 勾选一个限速器
										if (dtcs <= 5) {
											dt_money = (int) Math.round(500 * 1.3 + 200);
										} else {
											dt_money = (int) Math.round((500 + (dtcs - 5) * 50) * 1.3 + 200);
										}
									} else if (xsqts.equals("2")) {// 勾选两个限速器
										if (dtcs <= 5) {
											dt_money = (int) Math.round(500 * 1.3 + 400);
										} else {
											dt_money = (int) Math.round((500 + (dtcs - 5) * 50) * 1.3 + 400);
										}
									}
								} else if (check_type.equals("2")) {// 监督检验
									// 判断电梯层数
									if (dtcs <= 5) {
										dt_money = (int) Math.round(500 * 1.5);
									} else {
										dt_money = (int) Math.round((500 + (dtcs - 5) * 50) * 1.5);
									}
								}
							}else{
								if (StringUtil.isNotEmpty(device_type)
										&& StringUtil.isNotEmpty(device_sort_code)) {
									// 检验类别（3：定检 2：监检）
									if ("3".equals(check_type)) {
										// 区分设备类型
										if (device_type.equals("3100")
												|| device_type.equals("3200")
												|| device_sort_code.equals(
														"3410")
												|| device_sort_code.equals(
														"3420")) {
											// 判断是否有限速器
											if ("0".equals(xsqts)) {// 没有勾选限速器
												// 判断电梯层数
												if (dtcs <= 5) {
													dt_money = 550;
												} else {
													dt_money = (550 + (dtcs - 5) * 55);
												}
											} else if ("1".equals(xsqts)) {// 勾选一个限速器
												if (dtcs <= 5) {
													dt_money = 550 * 1.3 + 200;
												} else {
													dt_money = (550 + (dtcs - 5) * 55) * 1.3 + 200;
												}

											} else if ("2".equals(xsqts)) {// 勾选两个限速器
												if (dtcs <= 5) {
													dt_money = 550 * 1.3 + 400;
												} else {
													dt_money = (550 + (dtcs - 5) * 55) * 1.3 + 400;
												}
											}
										}else if (device_type.equals("3300")
												|| device_sort_code.equals(
														"3430")) {
											dt_money = 400;// 自动扶梯和人行道、杂物电梯 定额收费
										}
									} else if (check_type.equals("2")) {// 监督检验
										// 区分设备类型
										if (device_sort_code.equals("3410")
												|| device_sort_code.equals(
														"3420")
												|| device_type.equals("3200")
												|| device_type.equals("3100")) {

											// 判断电梯层数
											if (dtcs <= 5) {
												dt_money = 550 * 1.5;
											} else {
												dt_money = (550 + (dtcs - 5) * 55) * 1.5;
											}

										}else if (device_type.equals("3300")
												|| device_sort_code.equals(
														"3430")) {
											dt_money = 600;// 自动扶梯和人行道、杂物电梯 定额收费
										}
									}
								}
							}
							
							// 复检收费30%
							if (device_type.startsWith("3")) {
								if (inspection_conclusion.equals("复检合格")) {
									dt_money = (double) Math
											.round(dt_money * 0.3);
								}
							}
							info.setAdvance_fees(dt_money);
						}else{
							// 2017-12-14因明子涵要求，新疆特检突击队报告费用由检验员手动输入，不再自动计算价格biu~biu~
							// 2018-03-29应明子涵要求，九寨特检突击队报告费用由系统自动计算价格biu~biu~
							// 九寨沟的计算公式均无1.3和1.5的系数，故此处去掉
							double dt_money = 0;	// 不打折，也不四舍五入
							if (report_name.contains("病床")) {
								if (check_type.equals("3")) {// 定期检验
									// 判断是否有限速器
									if (xsqts.equals("0")) {// 没有勾选限速器
										// 判断电梯层数
										if (dtcs <= 5) {
											dt_money = (int) Math.round((500));
										} else {
											dt_money = (int) Math.round(500 + (dtcs - 5) * 50);
										}
									} else if (xsqts.equals("1")) {// 勾选一个限速器
										if (dtcs <= 5) {
											dt_money = (int) Math.round(500 + 200);
										} else {
											dt_money = (int) Math.round((500 + (dtcs - 5) * 50) + 200);
										}
									} else if (xsqts.equals("2")) {// 勾选两个限速器
										if (dtcs <= 5) {
											dt_money = (int) Math.round(500 + 400);
										} else {
											dt_money = (int) Math.round((500 + (dtcs - 5) * 50) + 400);
										}
									}
								} else if (check_type.equals("2")) {// 监督检验
									// 判断电梯层数
									if (dtcs <= 5) {
										dt_money = (int) Math.round(500);
									} else {
										dt_money = (int) Math.round((500 + (dtcs - 5) * 50));
									}
								}
							}else{
								if (StringUtil.isNotEmpty(device_type)
										&& StringUtil.isNotEmpty(device_sort_code)) {
									// 检验类别（3：定检 2：监检）
									if ("3".equals(check_type)) {
										// 区分设备类型
										if (device_type.equals("3100")
												|| device_type.equals("3200")
												|| device_sort_code.equals(
														"3410")
												|| device_sort_code.equals(
														"3420")) {
											// 判断是否有限速器
											if ("0".equals(xsqts)) {// 没有勾选限速器
												// 判断电梯层数
												if (dtcs <= 5) {
													dt_money = 550;
												} else {
													dt_money = (550 + (dtcs - 5) * 55);
												}
											} else if ("1".equals(xsqts)) {// 勾选一个限速器
												if (dtcs <= 5) {
													dt_money = 550  + 200;
												} else {
													dt_money = (550 + (dtcs - 5) * 55) + 200;
												}

											} else if ("2".equals(xsqts)) {// 勾选两个限速器
												if (dtcs <= 5) {
													dt_money = 550 + 400;
												} else {
													dt_money = (550 + (dtcs - 5) * 55) + 400;
												}
											}
										}else if (device_type.equals("3300")
												|| device_sort_code.equals(
														"3430")) {
											dt_money = 400;// 自动扶梯和人行道、杂物电梯 定额收费
										}
									} else if (check_type.equals("2")) {// 监督检验
										// 区分设备类型
										if (device_sort_code.equals("3410")
												|| device_sort_code.equals(
														"3420")
												|| device_type.equals("3200")
												|| device_type.equals("3100")) {

											// 判断电梯层数
											if (dtcs <= 5) {
												dt_money = 550;
											} else {
												dt_money = (550 + (dtcs - 5) * 55);
											}

										}else if (device_type.equals("3300")
												|| device_sort_code.equals(
														"3430")) {
											dt_money = 600;// 自动扶梯和人行道、杂物电梯 定额收费
										}
									}
								}
							}
							
							// 复检收费30%
							if (device_type.startsWith("3")) {
								if (inspection_conclusion.equals("复检合格")) {
									dt_money = (double) Math
											.round(dt_money * 0.3);
								}
							}
							info.setAdvance_fees(dt_money);
						}
					}
					
					// 西藏报告收费标准，直梯（有机房、无机房）定检1000元整，扶梯定检800元整，杂物梯定检400元整
					// 新疆报告收费标准，直梯（有机房、无机房）定检1000元整，扶梯定检800元整，杂物梯定检400元整
					if (StringUtil.isNotEmpty(check_unit_id)) {
						// 100069：赴藏特检突击队
						if (Constant.CHECK_UNIT_100069.equals(check_unit_id)) {
							double xz_money = 1000.00;
							if (device_type.equals("3100") || device_type.equals("3200")) {
								xz_money = 1000.00;
							} else if (device_type.equals("3400")) {
								if (device_type.equals("3430")) {
									xz_money = 400.00;
								}
							} else if (device_type.equals("3300")) {
								xz_money = 800.00;
							}
							info.setAdvance_fees(xz_money);
						}
						// 100090：新疆特检突击队
						// 2017-12-14因明子涵要求，新疆特检突击队报告费用由检验员手动输入，不再自动计算价格biu~biu~
						/*if (Constant.CHECK_UNIT_100090.equals(check_unit_id)) {
							double xz_money = 1000.00;
							if (device_type.equals("3100") || device_type.equals("3200")) {
								xz_money = 1000.00;
							} else if (device_type.equals("3400")) {
								if (device_type.equals("3430")) {
									xz_money = 400.00;
								}
							} else if (device_type.equals("3300")) {
								xz_money = 800.00;
							}
							info.setAdvance_fees(xz_money);
						}*/
					}
					
					if(StringUtil.isEmpty(info.getFk_tsjc_device_document_id())){
						info.setFk_tsjc_device_document_id(deviceDocument.getId());
					}
					info.setInspection_conclusion(inspection_conclusion);	// 检验结论
					infoDao.save(info);
					
					// 中止检验时，返写设备的处理情况
					if("1".equals(check_status)){	// 检验状态，默认为0（0：正常检验 1：中止检验）
						updateDeviceDeal(deviceDocument.getId(), user, "109");
					}else{
						updateDeviceDeal(deviceDocument.getId(), user, "100");
					}
				}else{
					map.put("success", false);
					map.put("msg", "亲，系统未找到您上传数据中的检验项目表信息，请核实该部分数据是否已正确保存！");
					return map;
				}
				
				// 3、不合格问题来源数据处理
				Object bhglistdatas = json.get("reportbhgitemvalue");
				if(bhglistdatas != null && !"".equals(bhglistdatas.toString()) && !"null".equals(bhglistdatas.toString())){
					// 再次上传数据时，删除初次上传的不合格问题来源数据
					if(StringUtil.isNotEmpty(submitid)){
						recordBHGRecordService.delBhgRecord(submitid);	// 删除不合格问题来源数据
					}

					JSONArray listdatas1 = JSONArray.fromObject(bhglistdatas.toString());
					for (int i = 0; i < listdatas1.length(); i++) {
						Object reportItemObject = listdatas1.get(i);
						JSONObject itemValue = JSONObject.fromObject(reportItemObject.toString());
						String item_key = itemValue.getString("key");
						String item_value = itemValue.getString("value");
						
						// 保存不合格问题来源记录表（新增）
						reportBHGRecordDao.insertReportBHGRecord(
								StringUtil.getUUID(), item_key, item_value, infoId,
								userId, userName);
					}
				}
				
				// 4、图片上传处理
				// 4.1、再次提交数据上传图片前，先查找之前是否上传过图片，存在图片就先删除再上传
				if(StringUtil.isNotEmpty(submitid)){
					List<Attachment> attachments = attachmentManager.getBusAttachment(submitid);
					for(Attachment attachment: attachments){
						employeesService.deleteAttach(attachment);
					}
				}
				
				// 4.2、上传图片
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> files = multipartRequest.getFileMap();
				if (files.size() > 0) {
					//Iterator<String> fileNames = multipartRequest.getFileNames();
					//String fileName = (String) fileNames.next();
					for (String pic_name : files.keySet()) {  
						String pic_type = pic_name.substring(0,pic_name.lastIndexOf("_"));
					    Attachment attachment = new Attachment();
					    CommonsMultipartFile file = (CommonsMultipartFile) files.get(pic_name);
						attachment.setFileSize(file.getSize());
						attachment.setFileType(file.getFileItem().getContentType());
						attachment.setFileName(file.getOriginalFilename());
						attachment.setBusinessId(infoId);
						//attachment.setBusUniqueName("");
						//attachment.setId(StringUtil.getUUID());
						if (user != null) {
							attachment.setUploader(user.getId());
							attachment.setUploaderName(user.getName());
						}
						//attachment.setWorkItem("");
						// saveType 存储类别
						// saveDB 是否往数据库写入附件信息，此项只在存储类型为文件系统时有效
						map = employeesService.saveAttachment(file.getInputStream(), attachment, "", true, pic_type);
						if(Boolean.parseBoolean(map.get("success")+"")==false){
							map.put("id", infoId);	// 返回业务检验信息ID
							return map;
						}
						/*String str = "{\"success\":\"true\",\"data\":{\"id\":\"" + attachment.getId() + "\",\"name\":\""
								+ attachment.getFileName() + "\",\"path\":\"" + attachment.getFilePath() + "\",\"workItem\":\""
								+ attachment.getWorkItem() + "\"}}";
						response.getWriter().write(str);*/
					  
					}  
				}
				
				// 判断是初次上传还是修改上传，修改时，记录修改日志
				if (sumTable.get("submitid")!=null&&!"".equals(sumTable.get("submitid"))&&!"null".equals(sumTable.get("submitid").toString())) {
					// 5.1、获取原始记录修改后的新内容
					new_Map = this.queryRecordByInfoId(infoId);
					// 5.2、将原始记录修改前后的内容进行对比并记录区别
					Map<String, String> infoMap = new HashMap<String, String>();	
			    	infoMap.put("table_code", "tzsb_report_item_record");	// 数据库表名代码
					infoMap.put("table_name", "原始记录检验项目参数表");	// 数据库表名标题
					infoMap.put("op_action", "修改原始记录内容");			// 操作动作
					infoMap.put("op_remark", "电脑端修改原始记录内容");		// 操作描述
					infoMap.put("business_id", infoId);							// 业务ID
					// 5.3、修改前后对比并返回有差异的数据集合 
					Map<String, String> diff_Map = inspectionCommonService.compareMap(infoMap, old_Map, new_Map, request);
					
					// 6、删除修改前后无差异的已作废数据
					// 6.1、获取已作废的原始记录数据
					List<ReportItemRecord> delRecordList = this.queryDelByInfoId(infoId);
					for(ReportItemRecord record : delRecordList){
						if(!diff_Map.containsKey(record.getItem_name())){
							// 6.2、删除无差异的已作废数据（物理删除）
							this.deleteBusiness(record.getId());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "数据上传失败！错误代码："+e.toString());
			return map;
		}
		
		if(infoId!=null){
			map.put("success", true);
			map.put("id", infoId);	// 返回业务检验信息ID
		}else{
			map.put("success", false);
			map.put("msg", "亲，检验业务信息保存失败，数据上传失败！");
		}
		return map;
	}
	
	private HashMap<String, Object> validateRepeat(InspectionInfo old_info, String device_registration_code){
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		Date enter_time = old_info.getEnter_time();
		if(enter_time == null){
			enter_time = old_info.getAdvance_time();
		}
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(enter_time); // 录入/检验日期
		c2.setTime(new Date()); // 当前日期
		// 设置时间为0时   
		c1.set(java.util.Calendar.HOUR_OF_DAY, 0);   
		c1.set(java.util.Calendar.MINUTE, 0);   
		c1.set(java.util.Calendar.SECOND, 0);   
		c2.set(java.util.Calendar.HOUR_OF_DAY, 0);   
		c2.set(java.util.Calendar.MINUTE, 0);   
		c2.set(java.util.Calendar.SECOND, 0);   
		
		// 计算两个日期相差的天数   
        int days = ((int) (c2.getTime().getTime() / 1000) - (int) (c1   
               .getTime().getTime() / 1000)) / 3600 / 24; 
		
		// 计算月份差（只能计算同一年的日期，翻年的日期不适应该方法）
		// int result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
		if (days <= 60) {
			List<ReportErrorRecordInfo> record_infos = reportErrorRecordInfoDao
					.queryInfoByInfoIds(old_info.getId());
			if (record_infos.isEmpty()) {
				returnMap.put("success", false);
				if(StringUtil.isNotEmpty(old_info.getFlow_note_name())){
					if ("打印报告".equals(old_info.getFlow_note_name())
							|| "报告领取".equals(old_info.getFlow_note_name())
							|| "报告归档".equals(old_info.getFlow_note_name())) {
						returnMap.put("msg",
								"设备" + (device_registration_code != null
										? device_registration_code : "")
										+ "在短期内已出过报告（已签发），如若需要重新出报告，请先提交纠错申请！");
					} else {
						returnMap.put("msg",
								"设备" + (device_registration_code != null
										? device_registration_code : "")
										+ "在短期内已出过报告（未签发），不能重复，请检查！");
					}
				} else {
					returnMap.put("msg", old_info.getEnter_op_name() + "同志已报检该设备（"
								+ (device_registration_code != null ? device_registration_code : "") + "），不能重复，请核实！");
				}
			}else{
				returnMap.put("success", true);
			}
		}else{
			returnMap.put("success", true);
		}
		return returnMap;
	}
	
	private DeviceDocument updateDeviceInfo(DeviceDocument deviceDocument,
			String item_key, String item_value) throws Exception{
		if (StringUtil.isNotEmpty(item_value)) {
			if ("DEVICE_NAME".equals(item_key)) {
				deviceDocument.setDevice_name(item_value);
			}
			if ("DEVICE_SORT".equals(item_key)) {
				deviceDocument.setDevice_sort(item_value);
			}
			if ("DEVICE_SORT_CODE".equals(item_key)) {
				deviceDocument.setDevice_sort_code(item_value);
			}
			if ("DEVICE_QR_CODE".equals(item_key)) {
				deviceDocument.setDevice_qr_code(item_value);
			}
			if ("DEVICE_REGISTRATION_CODE".equals(item_key)) {
				deviceDocument.setDevice_registration_code(item_value);
			}
			if ("DEVICE_USE_PLACE".equals(item_key)) {
				deviceDocument.setDevice_use_place(item_value);
			}
			if ("DEVICE_AREA_CODE".equals(item_key)) {
				deviceDocument.setDevice_area_code(item_value);
			}
			if ("DEVICE_AREA_NAME".equals(item_key)) {
				deviceDocument.setDevice_area_name(item_value);
			}
			if ("DEVICE_STREET_CODE".equals(item_key)) {
				deviceDocument.setDevice_street_code(item_value);
			}
			if ("DEVICE_STREET_NAME".equals(item_key)) {
				deviceDocument.setDevice_street_name(item_value);
			}
			if ("MAKE_DATE".equals(item_key)) {
				deviceDocument.setMake_date(item_value);
			}
			if ("SECURITY_OP".equals(item_key)) {
				deviceDocument.setSecurity_op(item_value);
			}
			if ("SECURITY_TEL".equals(item_key)) {
				deviceDocument.setSecurity_tel(item_value);
			}
			if ("MAKE_UNITS_NAME".equals(item_key)) {
				deviceDocument.setMake_units_name(item_value);
			}
			if ("CONSTRUCTION_UNITS_NAME".equals(item_key)) {
				deviceDocument.setConstruction_units_name(item_value);
			}
			if ("MAINTAIN_UNIT_NAME".equals(item_key)) {
				deviceDocument.setMaintain_unit_name(item_value);
			}
			if ("DEVICE_MODEL".equals(item_key)) {
				deviceDocument.setDevice_model(item_value);
			}
			if ("FACTORY_CODE".equals(item_key)) {
				deviceDocument.setFactory_code(item_value);
			}
			//if(StringUtil.isEmpty(deviceDocument.getId())){
				if ("INTERNAL_NUM".equals(item_key)) {
					deviceDocument.setInternal_num(item_value);
				}
			//}
			if ("INSPECTION_DATE".equals(item_key)) {
				deviceDocument.setInspect_date(dateformat.parse(item_value));
			}
			if ("REGISTRATION_NUM".equals(item_key)) {
				deviceDocument.setRegistration_num(item_value);
			}
		}
		return deviceDocument;
	}
	
	public void updateInfos(DeviceDocument deviceDocument, String report_id,
			String infoId, String userId, String userName) throws Exception {
		if (StringUtil.isEmpty(deviceDocument.getDevice_registration_code())) {
			// 2017-09-07根据9.6省院市局协调会纪要和成质监特[2017]48号文件要求，停止检验软件内所有设备注册代码的新生成
			/*if (!deviceDocument.getDevice_sort_code().substring(0, 2).equals("26")) {
				if (StringUtil.isNotEmpty(deviceDocument.getDevice_sort_code())
						&& StringUtil.isNotEmpty(deviceDocument.getDevice_area_code())) {
					// 自动生成注册代码
					String registration_code = deviceDao.getRegistrationCode(deviceDocument.getDevice_sort_code(),
							deviceDocument.getDevice_area_code());
					deviceDocument.setDevice_registration_code(registration_code);
				}
			}*/
			deviceDocument.setDevice_registration_code("/");
		}
		// 往原始记录表中新增设备注册代码
		// 新增前先删除原数据
		this.delRecordItem(infoId, report_id, "DEVICE_REGISTRATION_CODE");
		reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), report_id, "DEVICE_REGISTRATION_CODE",
				deviceDocument.getDevice_registration_code(), infoId, userId, userName);

		deviceDocument.setDevice_status("2"); 	// 设置设备状态为可使用
		deviceDocument.setIs_cur_check("2"); 	// 设置当前报检中
		deviceDocument.setSend_status("0"); 	// 记录数据被修改

		if (StringUtil.isEmpty(deviceDocument.getId())) {
			deviceDocument.setRegistration_date(new Date()); // 注册登记日期
			// 创建日期和创建人员
			deviceDocument.setCreated_date(new Date());
			deviceDocument.setCreated_by(userName);
		}
		deviceDocument.setLast_upd_date(new Date()); 	// 最后修改日期
		deviceDocument.setLast_upd_by(userName); 		// 最后修改人

		deviceDao.save(deviceDocument);
		
		Collection<PressurevesselsPara> paras = deviceDocument.getPressurevessels();	
		if(!paras.isEmpty()){
			for (PressurevesselsPara pp : paras) {
				pp.setDeviceDocument(deviceDocument);
				pressurevesselsParaDao.save(pp);
			}
		}
		
		InspectionInfo info = inspectionInfoService.get(infoId);
		info.setMaintain_unit_name(deviceDocument.getMaintain_unit_name());
		info.setMake_units_name(deviceDocument.getMake_units_name());
		info.setConstruction_units_name(deviceDocument.getConstruction_units_name());
		info.setMake_date(deviceDocument.getMake_date());
		info.setSecurity_op(deviceDocument.getSecurity_op());
		info.setSecurity_tel(deviceDocument.getSecurity_tel());
		info.setInternal_num(deviceDocument.getInternal_num());
		info.setDevice_model(deviceDocument.getDevice_model());
		info.setFactory_code(deviceDocument.getFactory_code());
		info.setDevice_use_place(deviceDocument.getDevice_use_place());
		info.setDevice_qr_code(deviceDocument.getDevice_qr_code());
		info.setDevice_registration_code(deviceDocument.getDevice_registration_code());
		inspectionInfoService.save(info);
	}
	
	
	public void updateDeviceDeal(String device_id, User user, String type) throws Exception {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		// 获取设备检验日期
		String inspection_date = inspectionInfoService
				.queryInspection_date(device_id);
		DeviceWarningDeal record = new DeviceWarningDeal();
		record.setFk_base_device_document_id(device_id);
		if("109".equals(type)){
			record.setDeal_remark("中止检验");
		}
		record.setDeal_status(type);	// 处理状态（100：已报检，109：现场不具备检验条件）
		
		record.setDeal_man_id(user.getId());
		record.setDeal_man(user.getName());
		record.setDeal_unit_id("100000");
		record.setDeal_unit("四川省特种设备检验研究院");
		record.setDeal_time(new Date());
		record.setSend_status("0"); 	// 数据传输状态（0：未传输 1：已传输）

		QueryCondition qc = new QueryCondition(DeviceWarningStatus.class);
		qc.addCondition("fk_base_device_document_id", "=", device_id);
		qc.addCondition("active_flag", "=", '1');
		List<DeviceWarningStatus> dwslist = deviceWarningStatusService
				.query(qc);
		if (dwslist.size() > 0) {
			DeviceWarningStatus dws = dwslist.get(0);
			dws.setDeal_status(record.getDeal_status());
			dws.setDeal_time(new Date());
			dws.setRemark(record.getDeal_remark());
			// dws.setFk_base_device_document_id(idArrays[i]);
			dws.setActive_flag('1');
			dws.setSend_status("0");
			// 获取检验日期
			if ((record.getDeal_status().equals("101") || record
					.getDeal_status().equals("104"))
					&& StringUtil.isNotEmpty(inspection_date)) {
				dws.setInspection_date(fmt.parse(inspection_date));
			}
			deviceWarningStatusService.update(dws);
		} else {
			DeviceWarningStatus dws = new DeviceWarningStatus();
			dws.setDeal_status(record.getDeal_status());
			dws.setDeal_time(new Date());
			dws.setRemark(record.getDeal_remark());
			dws.setFk_base_device_document_id(device_id);
			dws.setActive_flag('1');
			if ((record.getDeal_status().equals("101") || record
					.getDeal_status().equals("104"))
					&& StringUtil.isNotEmpty(inspection_date)) {
				dws.setInspection_date(fmt.parse(inspection_date));
			}
			deviceWarningStatusService.save(dws);
		}
		devicewarningService.save(record);
	}
	
	/**
	 * 移动端原始记录数据上传（常压罐车）
	 * @param json
	 * @param map 
	 * @param request 
	 * @return 
	 * @throws KhntException
	 * @throws ParseException 
	 */
	public synchronized HashMap<String, Object> saveGcMobile(JSONObject json, HashMap<String, Object> map,
			HttpServletRequest request) throws KhntException, ParseException{
		//CurrentSessionUser user = SecurityUtil.getSecurityUser();		
		// 获取原始记录项目总数，验证实际上传数量是否一致，不一致时提示失败请重试
		int item_num = 0;
		String item_count = json.getString("item_count");
		if (StringUtil.isNotEmpty(item_count)) {
			item_num = Integer.parseInt(item_count);
		}else{
			String msg = "亲，app已发布新版本，优化了提交性能，请先更新再使用提交功能！";
			map.put("success", false);
			map.put("msg", msg);
			return map;
		}
		
		String internal_num = "";
		Object listdatas = json.get("reportitemvalue");
		if (listdatas != null && !"".equals(listdatas.toString()) && !"null".equals(listdatas.toString())) {
			JSONArray listdatas1 = JSONArray.fromObject(listdatas.toString());
			if (listdatas1.length() != item_num) {
				internal_num = json.getString("internal_num");
				String msg = "亲，当前网络不稳定，设备（"+ internal_num+"）原始记录提交失败，请重试！";
				map.put("success", false);
				map.put("msg", msg);
				return map;
			}
		}
		
		//获取总表（检验信息）
		Object sumtable = json.get("sumtable");
		String infoId = null;	
		Inspection inspection = null;
		InspectionInfo info = null;
		
		String check_type = "";		// 检验类别（3：定检 2：监检）
		String report_code = "";	// 报告类别编号
		String report_id = "";		// 报告ID
		String submitid = "";		// 移动端数据传参ID
		String com_name = "";		// 使用单位名称
		String check_op_id = "";	// 参检人员ID
		String check_op_name = "";	// 参检人员
		String inspection_date = "";// 检验日期
		String device_type = "2600";		// 设备类型（常压容器）
		String device_sort_code = "2610";	// 设备类别（常压罐车）
		String report_item = "";	// 报告页码（原始）
		String report_page = "";	// 报告页码（实际）
		String ysjl_item = "";		// 原始记录页码（原始）
		String ysjl_page = "";		// 原始记录页码（实际）
		String report_name = "";	// 报告名称
		String random_code = "";	// 原始记录随机码（用于校验原始记录是否已提交）
		String inspection_conclusion = "";	//检验结论
		boolean canSave = false;	// 本次是否可提交保存数据
		
		Map<String, String> old_Map = new HashMap<String, String>();	// 原始记录修改前的内容，用作修改内容对比
		Map<String, String> new_Map = new HashMap<String, String>();	// 原始记录修改后的新内容，用作修改内容对比
		DeviceDocument deviceDocument = null;
		PressurevesselsPara pressurevessels = null;
		Collection<PressurevesselsPara> paras = null;
		EnterInfo companyInfo = null;
		JSONObject sumTable = null;
		Date inspectiondate = null;
		Date end_date = null;
		Date cur_date = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.getCurrentDateTime());
		
		try {
			// 1、保存检验业务信息
			// 获取人员信息
			String userId = json.getString("userId");		// 用户ID（sys_user）
			String userName = json.getString("userName");	// 用户姓名（sys_user）
			User user = userManager.get(userId);
			// 获取employee信息（报告书内所有业务人员全部使用的是employee，故此处要获取employee表数据）
			Employee employee = employeesDao.queryEmployeeByUser(userId, userName);
			
			// 获取报检单位信息（设备使用单位ID）
			String fk_unit_id = json.getString("fk_company_info_use_id");
			
			// 获取检验部门ID（录入部门ID）
			String check_unit_id = json.getString("depId");
			if (StringUtil.isEmpty(check_unit_id)) {
				check_unit_id = employee.getOrg().getId();
			}
			
			if(sumtable!=null&&!"".equals(sumtable)){
				sumTable = JSONObject.fromObject(sumtable.toString());
				
				// 获取使用单位名称
				com_name = sumTable.getString("use_company");	
				// 判断是初次上传还是修改上传
				if (sumTable.get("submitid")!=null&&!"".equals(sumTable.get("submitid"))&&!"null".equals(sumTable.get("submitid").toString())) {
					//存在submitid，说明已经上传过，上传过进行修改操作
					submitid = sumTable.getString("submitid");
					info = infoDao.get(submitid);
					inspection = info.getInspection();
					
					// 获取原始记录修改前的内容
					old_Map = this.queryRecordByInfoId(submitid);
				}

				// 原始记录随机码（用于校验原始记录是否已提交）
				random_code = sumTable.getString("random_code");
				if(StringUtil.isEmpty(random_code)){
					String msg = "亲，当前网络不稳定，设备（"+ internal_num+"）原始记录提交失败，请重试！";
					map.put("success", false);
					map.put("msg", msg);
					return map;
				}else{
					if(info == null){
						List<InspectionInfo> infos = infoDao.getInfoByRandomcode(random_code);
						if(!infos.isEmpty()){
							info = infos.get(0);
							inspection = info.getInspection();
							
							// 获取原始记录修改前的内容
							old_Map = this.queryRecordByInfoId(info.getId());
						}
					}
				}
				
				if(info == null){
					// 新建报检信息表
					inspection = new Inspection();
					
					// 新建检验业务信息表
					info = new InspectionInfo();
					// 业务流水号，用户进去系统提交业务时，再生成业务流水号
					//info.setSn(this.getInspectionInfoSn(1));	
					info.setCreate_time(new Date());
				}
				
				if (StringUtil.isEmpty(info.getFlow_note_name()) || "报告录入".equals(info.getFlow_note_name())) {
					canSave = true;
				}else{
					map.put("success", false);
					map.put("msg", "亲，报告当前正在审批中，不能修改哦！如需修改，请先确保报告已退回至报告录入阶段！已封存的报告，请提交纠错申请后再重新录入原始记录！");
					return map;
				}
				
				if(canSave){
					paras = new ArrayList<PressurevesselsPara>();
					// 获取设备ID
					String device_id = sumTable.getString("device_id");
					if(StringUtil.isNotEmpty(submitid)){
						device_id = info.getFk_tsjc_device_document_id();
					}
					if(StringUtil.isNotEmpty(device_id)){
						info.setFk_tsjc_device_document_id(device_id);	// 设备ID
						deviceDocument =  deviceDao.get(device_id);
						if(StringUtil.isEmpty(deviceDocument.getDevice_sort())){
							deviceDocument.setDevice_sort(device_type);
						}
						if(StringUtil.isEmpty(deviceDocument.getDevice_sort_code())){
							deviceDocument.setDevice_sort_code(device_sort_code);
						}
						if(StringUtil.isEmpty(deviceDocument.getDevice_name())){
							deviceDocument.setDevice_name("常压罐车");
						}
						paras = deviceDocument.getPressurevessels();	
						if(!paras.isEmpty()){
							for (PressurevesselsPara pp : paras) {
								pressurevessels = pp;
							}
						}else{
							pressurevessels = new PressurevesselsPara();
							pressurevessels.setId(null);
						}
					}else{
						if(StringUtil.isEmpty(submitid)){
							deviceDocument = new DeviceDocument();
							deviceDocument.setDevice_sort(device_type);
							deviceDocument.setDevice_sort_code(device_sort_code);
							deviceDocument.setDevice_name("常压罐车");
							pressurevessels = new PressurevesselsPara();
							pressurevessels.setId(null);
						}
					}
					
					
					// 获取检验类别
					check_type = sumTable.getString("check_type");	// 检验类别（3：定检 2：监检）
					if (deviceDocument != null) {				
						companyInfo = deviceDocument.getEnterInfo();
						if (companyInfo == null) {
							companyInfo = enterService.get(fk_unit_id);
						}
						if (companyInfo != null) {
							info.setCheck_op(companyInfo.getCom_contact_man()); // 检验联系人
							info.setCheck_tel(companyInfo.getCom_tel()); // 检验联系电话
							info.setReport_com_address(companyInfo.getCom_address()); // 使用单位地址
						}
					}

					// 获取报告类别编号
					report_code = sumTable.getString("report_type");
					// 根据报告类别编号、原始记录版本号获取报告ID
					report_id = reportDao.queryGcReportId(report_code);
					if(StringUtil.isEmpty(report_id)){
						map.put("success", false);
						map.put("msg", "亲，系统暂未找到该原始记录对应的报告模板，请联系系统管理员！");
						return map;
					}else{
						// 获取报告基本信息
						Report report = reportService.get(report_id);
						if(report==null){
							map.put("success", false);
							map.put("msg", "亲，系统暂未找到该原始记录对应的报告模板，请联系系统管理员！");
							return map;
						}else{
							if(StringUtil.isNotEmpty(report.getYsjl_pages())){
								ysjl_item = report.getYsjl_pages();
							}
							if(StringUtil.isEmpty(report_name) && StringUtil.isNotEmpty(report.getReport_name())){
								report_name = report.getReport_name();
							}
						}
						
						
						// 设置报告页码
						List<ReportItem> reportItemList = reportItemService.queryByReportId(report_id);
						// 获取报告模板中的必选页面
						for (ReportItem reportItem : reportItemList) {
							if("1".equals(reportItem.getIs_must())){
								if(report_item.length()>0){
									report_item += ",";
								}
								report_item += reportItem.getPage_index();
							}
							
						}

						String jl_item = sumTable.get("report_item").toString();
						report_page = report_item;
						ysjl_page = ysjl_item;
						
						if(jl_item.indexOf("7")!=-1){
							report_page = report_item+",8";
							ysjl_page = ysjl_item.substring(0, ysjl_item.length() - 2);
						}else{
							if(jl_item.indexOf("8")!=-1){
								report_page = report_item + ",9";
								ysjl_page = ysjl_item.substring(0, ysjl_item.length() - 4) + ysjl_item.substring(ysjl_item.length() - 2);
							}else{
								ysjl_page = ysjl_item.substring(0, ysjl_item.length() - 4);
							}
						}
						if(jl_item.indexOf("8")!=-1){
							if(jl_item.indexOf("7")!=-1){
								report_page = report_item + ",8,9";
								ysjl_page = ysjl_item;
							}else{
								report_page = report_item + ",9";
								ysjl_page = ysjl_item.substring(0, ysjl_item.length() - 4) + ysjl_item.substring(ysjl_item.length() - 2);
							}
						}else{
							if(jl_item.indexOf("7")!=-1){
								report_page = report_item + ",8";
								ysjl_page = ysjl_item.substring(0, ysjl_item.length() - 2);
							}else{
								ysjl_page = ysjl_item.substring(0, ysjl_item.length() - 4);
							}
						}
					}
					
					// 获取参检人员id
					check_op_id = sumTable.getString("check_op_id");
					// 获取参检人员姓名
					check_op_name = sumTable.getString("check_op_name");
					
					inspection.setCheck_type(check_type);	// 检验类别（3：定检 2：监检）
					inspection.setFk_unit_id(fk_unit_id);	// 报检单位ID
					inspection.setCom_name(com_name);		// 报检单位名称
					inspection.setEnter_op(employee.getName());	// 录入人员
					inspection.setInspection_time(Timestamp.valueOf(timeformat.format(new Date())));
					inspection.setData_status("0");			// 0：新建 1:使用 2：删除
					
					// 获取检验日期，并计算下次检验日期
					inspection_date = sumTable.getString("inspection_date");
					if(StringUtil.isEmpty(inspection_date)){
						inspection_date = DateUtil.getDateTime("yyyy-MM-dd", new Date());
					}else{
						if (inspection_date.contains("/")) {
							Date date = dateformat.parse(
								(inspection_date.replace("/", "-")).toString());
							info.setAdvance_time(date);
						}else if(inspection_date.contains(".")){
							Date date = dateformat.parse(
									(inspection_date.replace(".", "-")).toString());
							info.setAdvance_time(date);
						}else{
							info.setAdvance_time(DateUtil.convertStringToDate(Constant.defaultDatePattern, inspection_date));
						}
						
						// 自动计算下次检验日期，下次检验日期=（检验日期 + 1年）- 1天
						Date inspectionDate = info.getAdvance_time();
						if (inspectionDate != null) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(inspectionDate);
							calendar.add(Calendar.YEAR, 1);	
							if(StringUtil.isNotEmpty(device_type)){
								if (!device_type.startsWith("9")) {
									//	客运索道（device_sort_code为9开头的4位数字）报告只显示到年月，不需要精确到年月日，故此处不用减去一天
									calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
								}
							}else{
								// 暂无设备的情况，默认下次检验日期=（检验日期 + 1年）- 1天
								calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
							}
							info.setLast_check_time(calendar.getTime());
						}
					}
					
					info.setReport_com_name(com_name);		// 使用单位名称
					info.setReport_type(report_id);			// 报告类型ID
					info.setReport_item(report_page);		// 报告页码（多个页码，以逗号分隔）
					info.setYsjl_item(ysjl_page);			// 原始记录页码（多个页码，以逗号分隔）
					info.setRandom_code(random_code); 		// 原始记录随机码（用于校验原始记录是否已提交）
					info.setEnter_op_id(employee.getId());		// 录入人ID
					info.setEnter_op_name(employee.getName());	// 录入人姓名
					info.setItem_op_id(employee.getId());		// 项目负责人ID（罐车的项目负责人默认为原始记录编制人）
					info.setItem_op_name(employee.getName());	// 项目负责人姓名
					info.setIs_report_confirm("0");	// 原始记录是否已经校核，默认为0（0：未校核 1：校核通过 2：校核未通过）	
					info.setCheck_op_id(check_op_id);		// 参检人员id			
					info.setCheck_op_name(check_op_name);	// 参检人员姓名		
					info.setCheck_unit_id(check_unit_id);	// 检验部门ID
					info.setEnter_unit_id(check_unit_id);	// 录入部门ID
					info.setIs_back("1");		// 默认的状态为1，提交后变成0，退回也变成1
					info.setIs_copy("0");		// 是否是复制报告（0：非复制报告 1：复制报告）
					info.setIs_mobile("1");		// 是否移动检验（0：否 1：是）
					//info.setIs_flow("1");		// 判断是否已经启动流程（1 未进入流程 2 进入流程）
					info.setIs_flow(null);		
					info.setCheck_category_code(check_type);	// 检验类型代码（1：制造监检 2：安改维监检 3：定检  4：委托检验......）	
					info.setCheck_category_name(codeTablesDao.getValueByCode("BASE_CHECK_TYPE", check_type));	// 检验类型名称
					
					// 自动计算录入/编制日期（编制日期=检验日期+1天）
					if(device_type.startsWith("3") || device_type.startsWith("4") || device_type.startsWith("6") || device_type.startsWith("9")){
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(info.getAdvance_time());
						calendar.add(Calendar.DATE, 1);	
						// 检验日期为当天的，编制日期不再+1，默认为当天
						Date draft_date = null;
						if(calendar.getTime().after(cur_date)){
							draft_date = cur_date;
						}else{
							draft_date = calendar.getTime();
						}
						info.setEnter_time(draft_date);
					}else{
						info.setEnter_time(new Date());		// 录入日期
					}
					
					// 未启动流程前，业务数据状态都为初始状态不进入正常使用流程
					if(StringUtil.isEmpty(submitid)){
						info.setData_status("0");		// 报告数据状态（0：初始（未生成报告） 1：正常 99：删除）
						info.setFee_status("0");		// 收费状态（0：初始）未启动流程前，收费状态都为初始状态不进入待收费流程
						info.setYsjl_data_status("0");	// 原始记录数据状态（0：未生成报告 1：已生成报告 2：原始记录已修改未生成报告）
					}else{
						//info.setData_status("2");		
						info.setYsjl_data_status("2");
					}								
					inspectionDao.save(inspection);
					
					info.setInspection(inspection);
					info.setLast_mdy_time(new Date());
					info.setIs_print_ysjl("0");//原始记录打印状态（0：未打印 1：已打印）
					infoDao.save(info);
					
					infoId = info.getId();
					logService.setLogs(infoId, "上传原始记录", "移动端原始记录数据上传", userId, userName, new Date(), request.getRemoteAddr());
				}
			}
			
			// 2、保存检验项目参数表信息
			// itemvalue 数据项信息保存
			if(canSave){
				//Object listdatas = json.get("reportitemvalue");
				if(listdatas != null && !"".equals(listdatas.toString()) && !"null".equals(listdatas.toString())){
					// 再次上传数据时，删除初次上传的原始记录
					if(StringUtil.isNotEmpty(submitid)){
						this.delRecordItems(submitid);	// 删除原始记录
					}

					JSONArray listdatas1 = JSONArray.fromObject(listdatas.toString());
					for (int i = 0; i < listdatas1.length(); i++) {
						Object reportItemObject = listdatas1.get(i);
						JSONObject itemValue = JSONObject.fromObject(reportItemObject.toString());
						String item_key = itemValue.getString("key");
						String item_value = itemValue.getString("value");
						if(StringUtil.isNotEmpty(item_key)){
							// 获取车牌号
							if("LADLE_CAR_NUMBER".equals(item_key)){
								info.setInternal_num(item_value.trim());
								deviceDocument.setInternal_num(item_value.trim());
								pressurevessels.setLadle_car_number(item_value.trim());
							}
							// 获取发动机号
							if("LADLE_CAR_DOMAIN_NUM".equals(item_key)){
								pressurevessels.setLadle_car_domain_num(item_value.trim());
							}
							// 获取车辆型号
							if("LADLE_CAR_STRUCTURE".equals(item_key)){
								pressurevessels.setLadle_car_structure(item_value.trim());
							}
							// 获取车架号
							if("P20003002".equals(item_key)){
								pressurevessels.setP20003002(item_value.trim());
							}
							// 获取安全管理员
							if("SECURITY_OP".equals(item_key)){
								info.setSecurity_op(item_value.trim());
								deviceDocument.setSecurity_op(item_value.trim());
							}
							// 获取安全管理员联系电话
							if("SECURITY_TEL".equals(item_key)){
								info.setSecurity_tel(item_value.trim());
								deviceDocument.setSecurity_tel(item_value.trim());
							}
							
							// 获取检验结论
							if("INSPECTION_CONCLUSION".equals(item_key)){
								inspection_conclusion = item_value.trim();
							}
							
							// 生成检验结论报告下次检验日期和限速器下次检验日期
							if("INSPECTION_DATE".equals(item_key)){
								inspectiondate = DateUtil.convertStringToDate(Constant.defaultDatePattern, item_value.trim());
								if(inspectiondate.after(cur_date)){
									map.put("success", false);
									map.put("msg", "亲，现场检验日期不能晚于今天，请修改后再提交！");
									return map;
								}
								
								Date last_check_date = info.getLast_check_time();
								if(last_check_date == null){
									if (inspectiondate != null) {
										Calendar calendar = Calendar.getInstance();
										calendar.setTime(inspectiondate);
										calendar.add(Calendar.YEAR, 1);	
										if(StringUtil.isNotEmpty(device_type)){
											if (!device_type.startsWith("9")) {
												//	客运索道（device_sort_code为9开头的4位数字）报告只显示到年月，不需要精确到年月日，故此处不用减去一天
												calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
											}
										}else{
											// 暂无设备的情况，默认下次检验日期=（检验日期 + 1年）- 1天
											calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
										}
										last_check_date = calendar.getTime();
										info.setLast_check_time(last_check_date);
									}
								}
								// 自动生成检验结论报告下次检验日期
								reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
										report_id, "LAST_INSPECTION_DATE", DateUtil.format(last_check_date, Constant.defaultDatePattern), infoId, userId, userName);
							}
							// 完成检验日期
							if("INSPECTION_DATE_END".equals(item_key)){
								end_date = DateUtil.convertStringToDate(Constant.defaultDatePattern, item_value.trim());
								if(end_date.after(cur_date)){
									map.put("success", false);
									map.put("msg", "亲，检验完成日期不能晚于现场检验日期，请修改后再提交！");
									return map;
								}
							}
							
							if(inspectiondate != null && end_date!=null){
								if(inspectiondate.after(end_date)){
									map.put("success", false);
									map.put("msg", "亲，检验完成日期不能早于今天，请修改后再提交！");
									return map;
								}
							}
							
							// 返写设备注册代码到info业务表，以供报告查询
							if ("DEVICE_REGISTRATION_CODE".equals(item_key)) {
								info.setDevice_registration_code(item_value.trim());
							}
							
							// 保存原始记录检验项目记录表（新增）
							reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
									report_id, item_key, item_value, infoId, userId, userName);
							// 设置设备信息
							deviceDocument = updateDeviceInfo(deviceDocument, item_key, item_value);
						}
					}
					
					if (sumTable.get("submitid")==null || "".equals(sumTable.get("submitid")) || "null".equals(sumTable.get("submitid").toString())) {
						// 不存在submitid，说明是初次上传，初次上传执行新增操作
						// 往原始记录检验项目记录表里，增加报告书编号，默认为空，生成报告后赋值
						reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
								report_id, "REPORT_SN", "", infoId, userId, userName);
					}
					
					deviceDocument.setDevice_sort(device_type);
					deviceDocument.setDevice_sort_code(device_sort_code);
					
					paras.add(pressurevessels);
					deviceDocument.setPressurevessels(paras);
					
					// 返写报告业务信息和设备信息（监检生成设备注册代码）
					updateInfos(deviceDocument, report_id, infoId, userId, userName);
					
					if(StringUtil.isEmpty(info.getFk_tsjc_device_document_id())){
						info.setFk_tsjc_device_document_id(deviceDocument.getId());
					}
					info.setInspection_conclusion(inspection_conclusion);	// 检验结论
					infoDao.save(info);
					
					// 返写设备的处理情况
					updateDeviceDeal(deviceDocument.getId(), user, "100");
					
				}else{
					map.put("success", false);
					map.put("msg", "亲，系统未找到您上传数据中的检验项目表信息，请核实该部分数据是否已正确保存！");
					return map;
				}
				
				// 3、不合格问题来源数据处理
				Object bhglistdatas = json.get("reportbhgitemvalue");
				if(bhglistdatas != null && !"".equals(bhglistdatas.toString()) && !"null".equals(bhglistdatas.toString())){
					// 再次上传数据时，删除初次上传的不合格问题来源数据
					if(StringUtil.isNotEmpty(submitid)){
						recordBHGRecordService.delBhgRecord(submitid);	// 删除不合格问题来源数据
					}

					JSONArray listdatas1 = JSONArray.fromObject(bhglistdatas.toString());
					for (int i = 0; i < listdatas1.length(); i++) {
						Object reportItemObject = listdatas1.get(i);
						JSONObject itemValue = JSONObject.fromObject(reportItemObject.toString());
						String item_key = itemValue.getString("key");
						String item_value = itemValue.getString("value");
						
						// 保存不合格问题来源记录表（新增）
						reportBHGRecordDao.insertReportBHGRecord(
								StringUtil.getUUID(), item_key, item_value, infoId,
								userId, userName);
					}
				}
				
				// 4、图片上传处理
				// 4.1、再次提交数据上传图片前，先查找之前是否上传过图片，存在图片就先删除再上传
				if(StringUtil.isNotEmpty(submitid)){
					List<Attachment> attachments = attachmentManager.getBusAttachment(submitid);
					for(Attachment attachment: attachments){
						employeesService.deleteAttach(attachment);
					}
				}
				
				// 4.2、上传图片
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> files = multipartRequest.getFileMap();
				if (files.size() > 0) {
					//Iterator<String> fileNames = multipartRequest.getFileNames();
					//String fileName = (String) fileNames.next();
					for (String pic_name : files.keySet()) {  
						String pic_type = pic_name.substring(0,pic_name.lastIndexOf("_"));
					    Attachment attachment = new Attachment();
					    CommonsMultipartFile file = (CommonsMultipartFile) files.get(pic_name);
						attachment.setFileSize(file.getSize());
						attachment.setFileType(file.getFileItem().getContentType());
						attachment.setFileName(file.getOriginalFilename());
						attachment.setBusinessId(infoId);
						//attachment.setBusUniqueName("");
						//attachment.setId(StringUtil.getUUID());
						if (user != null) {
							attachment.setUploader(user.getId());
							attachment.setUploaderName(user.getName());
						}
						//attachment.setWorkItem("");
						// saveType 存储类别
						// saveDB 是否往数据库写入附件信息，此项只在存储类型为文件系统时有效
						
						if ("GC_8_CHDBWT".equals(pic_type) || "GC_17_QXWZSYT".equals(pic_type)
								|| "GC_18_QXWZSYT".equals(pic_type) || "GC_19_QXWZSYT".equals(pic_type)){
							map = employeesService.saveAttachment2(file, attachment, "", true, pic_type, report_id, userId, userName);
						}else{
							map = employeesService.saveAttachment(file.getInputStream(), attachment, "", true, pic_type);
						}
						
						if(Boolean.parseBoolean(map.get("success")+"")==false){
							map.put("id", infoId);	// 返回业务检验信息ID
							return map;
						}
						/*String str = "{\"success\":\"true\",\"data\":{\"id\":\"" + attachment.getId() + "\",\"name\":\""
								+ attachment.getFileName() + "\",\"path\":\"" + attachment.getFilePath() + "\",\"workItem\":\""
								+ attachment.getWorkItem() + "\"}}";
						response.getWriter().write(str);*/
					  
					}  
				}
				
				// 判断是初次上传还是修改上传，修改时，记录修改日志
				if (sumTable.get("submitid")!=null&&!"".equals(sumTable.get("submitid"))&&!"null".equals(sumTable.get("submitid").toString())) {
					// 5.1、获取原始记录修改后的新内容
					new_Map = this.queryRecordByInfoId(infoId);
					// 5.2、将原始记录修改前后的内容进行对比并记录区别
					Map<String, String> infoMap = new HashMap<String, String>();	
			    	infoMap.put("table_code", "tzsb_report_item_record");	// 数据库表名代码
					infoMap.put("table_name", "原始记录检验项目参数表");	// 数据库表名标题
					infoMap.put("op_action", "修改原始记录内容");			// 操作动作
					infoMap.put("op_remark", "电脑端修改原始记录内容");		// 操作描述
					infoMap.put("business_id", infoId);							// 业务ID
					// 5.3、修改前后对比并返回有差异的数据集合 
					Map<String, String> diff_Map = inspectionCommonService.compareMap(infoMap, old_Map, new_Map, request);
					
					// 6、删除修改前后无差异的已作废数据
					// 6.1、获取已作废的原始记录数据
					List<ReportItemRecord> delRecordList = this.queryDelByInfoId(infoId);
					for(ReportItemRecord record : delRecordList){
						if(!diff_Map.containsKey(record.getItem_name())){
							// 6.2、删除无差异的已作废数据（物理删除）
							this.deleteBusiness(record.getId());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "数据上传失败！错误代码："+e.toString());
			return map;
		}
		
		if(infoId!=null){
			map.put("success", true);
			map.put("id", infoId);	// 返回业务检验信息ID
		}else{
			map.put("success", false);
			map.put("msg", "亲，检验业务信息保存失败，数据上传失败！");
		}
		return map;
	}
	
	
	
	/**
	 * 移动端原始记录数据上传
	 * @param json
	 * @param map 
	 * @param request 
	 * @return 
	 * @throws KhntException
	 * @throws ParseException 
	 */
	public synchronized HashMap<String, Object> saveMobileTank(JSONObject json, HashMap<String, Object> map,
			HttpServletRequest request) throws KhntException, ParseException{
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		//获取总表（检验信息）
		Object sumtable = json.get("sumtable");
		String infoId = null;	
		Inspection inspection = null;
		InspectionInfo info = null;
		String check_type = "";
		String report_code = "";
		String report_id = "";
		String submitid = "";
		String com_name = "";
		String check_op_id = "";
		String check_op_name = "";
		String inspection_date = "";
		String device_type = "";
		DeviceDocument deviceDocument = null;
		EnterInfo companyInfo = null;
		JSONObject sumTable = null;
		try {
			// 1、保存检验业务信息
			// 获取人员信息
			String userId = json.getString("userId");		// 用户ID（sys_user）
			String userName = json.getString("userName");	// 用户姓名（sys_user）
			// 获取employee信息（报告书内所有业务人员全部使用的是employee，故此处要获取employee表数据）
			Employee employee = employeesDao.queryEmployeeByUser(userId, userName);
			
			// 获取报检单位信息（设备使用单位ID）
			String fk_unit_id = json.getString("fk_company_info_use_id");
			
			// 获取检验部门ID（录入部门ID）
			String check_unit_id = json.getString("depId");
			if (StringUtil.isEmpty(check_unit_id)) {
				check_unit_id = employee.getOrg().getId();
			}
			
			if(sumtable!=null&&!"".equals(sumtable)){
				sumTable = JSONObject.fromObject(sumtable.toString());
				// 获取检验类别
				//check_type = sumTable.getString("check_type");	// 检验类别（3：定检 2：监检）
				// 获取报告类别编号
				report_code = sumTable.getString("report_type");
				// 根据报告类别编号获取报告ID
//				report_id = reportDao.queryReportId(report_code);
				// 获取参检人员id
				check_op_id = sumTable.getString("check_op_id");
				// 获取参检人员姓名
				check_op_name = sumTable.getString("check_op_name");
				
				/*
				 * 上传数据不进行是否存在报告模板的验证，由用户在系统手动启动流程时验证
				if (StringUtil.isEmpty(report_id)) {
					map.put("success", false);
					map.put("msg", "抱歉，系统暂未找到与该原始记录相匹配的报告模板，请联系系统管理员！");
				}
				*/
				com_name = sumTable.getString("use_company");	// 使用单位名称
				if (sumTable.get("submitid")!=null&&!"".equals(sumTable.get("submitid"))&&!"null".equals(sumTable.get("submitid").toString())) {
					//存在submitid，说明已经上传过，上传过进行修改操作
					submitid = sumTable.getString("submitid");
					info = infoDao.get(submitid);
					report_id=info.getReport_type();
					inspection = info.getInspection();
				}

				if(info == null){
					// 新建报检信息表
					inspection = new Inspection();
					
					// 新建检验业务信息表
					info = new InspectionInfo();
					// 业务流水号，用户进去系统提交业务时，再生成业务流水号
					//info.setSn(this.getInspectionInfoSn(1));	
				}
				
				//inspection.setCheck_type(check_type);	// 检验类别（3：定检 2：监检）
				inspection.setFk_unit_id(fk_unit_id);	// 报检单位ID
				inspection.setCom_name(com_name);		// 报检单位名称
				inspection.setEnter_op(employee.getName());	// 录入人员
				inspection.setInspection_time(Timestamp.valueOf(timeformat.format(new Date())));
				inspection.setData_status("0");			// 0：新建 1:使用 2：删除
				
				String device_id = sumTable.getString("device_id");
				info.setFk_tsjc_device_document_id(device_id);	// 设备ID
				if(StringUtil.isNotEmpty(device_id)){
					deviceDocument =  deviceDao.get(device_id);
					if(deviceDocument != null){
						device_type = deviceDocument.getDevice_sort_code();
						if(StringUtil.isEmpty(device_type)){
							device_type = deviceDocument.getDevice_sort();
						}
						if(StringUtil.isEmpty(device_type)){
							device_type = "3000";	// 3000：代表电梯
						}
						companyInfo = deviceDocument.getEnterInfo();
						if (companyInfo == null) {
							companyInfo = enterService.get(fk_unit_id);
						}
						if (companyInfo!=null) {
							info.setCheck_op(companyInfo.getCom_contact_man());	// 检验联系人
							info.setCheck_tel(companyInfo.getCom_tel());		// 检验联系电话
							info.setReport_com_address(companyInfo.getCom_address());	// 使用单位地址
						}
					}
				}
				info.setReport_com_name(com_name);		// 使用单位名称
//				info.setReport_type(report_id);			// 报告类型ID
//				info.setCheck_op_id(check_op_id);		// 参检人员id
//				info.setCheck_op_name(check_op_name);	// 参检人员姓名
				
				// 获取检验日期，并计算下次检验日期
				inspection_date = sumTable.getString("inspection_date");
				if(StringUtil.isEmpty(inspection_date)){
					inspection_date = info.getAdvance_time().toString();//如果检验日期未取册取info的检验时间
				}else{
					if (inspection_date.contains("/")) {
						Date date = dateformat.parse(
							(inspection_date.replace("/", "-")).toString());
						info.setAdvance_time(date);
					}else if(inspection_date.contains(".")){
						Date date = dateformat.parse(
								(inspection_date.replace(".", "-")).toString());
						info.setAdvance_time(date);
					}else{
						info.setAdvance_time(DateUtil.convertStringToDate("yyyy-MM-dd", inspection_date));
					}
					
					// 自动计算下次检验日期，下次检验日期=（检验日期 + 1年）- 1天
					Date inspectionDate = info.getAdvance_time();
					if (inspectionDate != null) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(inspectionDate);
						calendar.add(Calendar.YEAR, 1);	
						if (!device_type.startsWith("9")) {
							//	客运索道（device_sort_code为9开头的4位数字）报告只显示到年月，不需要精确到年月日，故此处不用减去一天
							calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
						}
						info.setLast_check_time(calendar.getTime());
					}
				}
				//设置报告项目
				String item_report="";
				//默认前面几页
				item_report="1,2,3,4,5,6,7";
				
				String  report_item = sumTable.get("report_item").toString();
				
				if(report_item.indexOf("6")!=-1){
					item_report+=",8";
					
				}else if(report_item.indexOf("7")!=-1){
					item_report+=",9";
					
				}else if(report_item.indexOf("8")!=-1){
					item_report+=",10";
					
				}else if(report_item.indexOf("9")!=-1){
					item_report+=",11";
					
				}else if(report_item.indexOf("10")!=-1){
					item_report+=",12";
					
				}else if(report_item.indexOf("11")!=-1){
					item_report+=",17";
					
				}else if(report_item.indexOf("12")!=-1){
					item_report+=",18";
					
				}else if(report_item.indexOf("13")!=-1){
					item_report+=",19";
					
				}
				
				
				
				
				info.setReport_item(item_report);
//				info.setCheck_unit_id(check_unit_id);	// 检验部门ID
				info.setEnter_unit_id(check_unit_id);	// 录入部门ID
				info.setFee_status("0");	// 收费状态（0：初始）未启动流程前，收费状态都为初始状态不进入待收费流程
				info.setIs_back("1");		// 默认的状态为1，提交后变成0，退回也变成1
				info.setIs_copy("0");		// 是否是复制报告（0：非复制报告 1：复制报告）
				info.setIs_mobile("1");		// 是否移动检验（0：否 1：是）
				//info.setIs_flow("1");		// 判断是否已经启动流程（1 未进入流程 2 进入流程）
				info.setIs_flow(null);		
				info.setEnter_op_id(employee.getId());		// 录入人ID
				info.setEnter_op_name(employee.getName());	// 录入人姓名
				
				// 自动计算录入/编制日期（编制日期=检验日期+1天）
				if(device_type.startsWith("3") || device_type.startsWith("4") || device_type.startsWith("6") || device_type.startsWith("9")){
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(info.getAdvance_time());
					// 起重机编制日期（机电五部2015-08-24要求，编制日期=检验日期+1天）
					// 游乐设施编制日期（机电五部2015-09-25龚高科要求，编制日期=检验日期+1天）
					// 客运索道编制日期（机电五部2015-10-15龚高科要求，编制日期=检验日期+1天）
					calendar.add(Calendar.DATE, 1);	
					info.setEnter_time(calendar.getTime());
					//insInfo.setEnter_time(DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.getDate(calendar.getTime())));
				}else{
					info.setEnter_time(new Date());		// 录入日期
				}
				//info.setData_status("0");			// 数据状态（0：初始 1：正常使用 99：删除）未启动流程前，业务数据状态都为初始状态不进入正常使用流程
				
				inspectionDao.save(inspection);
				
				info.setInspection(inspection);
				infoDao.save(info);
				
				infoId = info.getId();
				logService.setLogs(infoId, "上传原始记录", "移动端原始记录数据上传", userId, userName, new Date(), request.getRemoteAddr());
			}
			
			// 2、保存检验项目参数表信息
			// itemvalue 数据项信息保存
			Object listdatas = json.get("reportitemvalue");
			if(listdatas != null && !"".equals(listdatas.toString()) && !"null".equals(listdatas.toString())){
				// 再次上传数据时，删除初次上传的原始记录
				if(StringUtil.isNotEmpty(submitid)){
					this.delRecordItems(submitid);	// 删除原始记录
				}

				JSONArray listdatas1 = JSONArray.fromObject(listdatas.toString());
				for (int i = 0; i < listdatas1.length(); i++) {
					Object reportItemObject = listdatas1.get(i);
					JSONObject itemValue = JSONObject.fromObject(reportItemObject.toString());
					// 保存原始记录检验项目记录表（新增）
					reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
							report_id, itemValue.getString("key"), 
							itemValue.getString("value"), infoId, userId, userName);
					/* 上传原始记录时，不直接生成报告检验项目参数，改由用户手动启动流程时，从原始记录表中获取数据再生成
					// 初次上传数据，才生成报告检验项目参数表
					// 再次上传数据时，不再生成报告检验项目参数表
					if (StringUtil.isEmpty(submitid)) {
						// 保存报告检验项目参数表（新增）
						reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
								report_id, itemValue.getString("key"),
								itemValue.getString("value"),infoId);
					}
					*/
				}
			}else{
				map.put("success", false);
				map.put("msg", "亲，系统未找到您上传数据中的检验项目表信息，请核实该部分数据是否已正确保存！");
				return map;
			}
			
			// 3、图片上传处理
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> files = multipartRequest.getFileMap();
			if (files.size() > 0) {
				//Iterator<String> fileNames = multipartRequest.getFileNames();
				//String fileName = (String) fileNames.next();
				for (String pic_name : files.keySet()) {  
				    Attachment attachment = new Attachment();
				    CommonsMultipartFile file = (CommonsMultipartFile) files.get(pic_name);
					attachment.setFileSize(file.getSize());
					attachment.setFileType(file.getFileItem().getContentType());
					attachment.setFileName(file.getOriginalFilename());
					attachment.setBusinessId(infoId);
					//attachment.setBusUniqueName("");
					//attachment.setId(StringUtil.getUUID());
					if (user != null) {
						attachment.setUploader(user.getId());
						attachment.setUploaderName(user.getName());
					}
					//attachment.setWorkItem("");
					// saveType 存储类别
					// saveDB 是否往数据库写入附件信息，此项只在存储类型为文件系统时有效
					map = employeesService.saveAttached(file.getInputStream(), attachment, "", true);
					if(Boolean.parseBoolean(map.get("success")+"")==false){
						return map;
					}
					/*String str = "{\"success\":\"true\",\"data\":{\"id\":\"" + attachment.getId() + "\",\"name\":\""
							+ attachment.getFileName() + "\",\"path\":\"" + attachment.getFilePath() + "\",\"workItem\":\""
							+ attachment.getWorkItem() + "\"}}";
					response.getWriter().write(str);*/
				  
				}  
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "数据上传失败！错误代码："+e.toString());
			return map;
		}
		
		if(infoId!=null){
			map.put("success", true);
			map.put("id", infoId);	// 返回业务检验信息ID
		}else{
			map.put("success", false);
			map.put("msg", "亲，检验业务信息保存失败，数据上传失败！");
		}
		return map;
	}
	
	/**
	 * 移动端审核、签发报告
	 * @param dataMap -- 报告审核、签发参数
	 * @param returnMap -- 返回信息
	 * @param request 
	 * @return 
	 * @throws KhntException
	 * @throws ParseException 
	 */
	public synchronized HashMap<String, Object> mo_batchCheck(JSONObject dataMap, HashMap<String, Object> returnMap,
			HttpServletRequest request) throws KhntException, ParseException{	
		try {
			// 流程提交
			Map<String, Object> map = new HashMap<String, Object>();
			String type = dataMap.getString("type"); // 操作类型（04：报告审核 05：报告签发）
			// 流程编号ID
			String acc_id = dataMap.getString("acc_id");
			// 业务ID
			String ins_info_id = dataMap.getString("infoId");
			// 步骤号
			String flow_num = dataMap.getString("flow_num");

			String revise_conclusion = dataMap.getString("revise_conclusion"); // 结论

			String op_time = dataMap.getString("op_time"); // 操作时间

			// if (ins_info_id.indexOf(",") != -1) {
			String ids[] = ins_info_id.split(",");
			String acc_ids[] = acc_id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if (type.equals("04") && "1".equals(revise_conclusion)) {
					String next_sub_op = dataMap.getString("next_sub_op");
					String next_op_name = dataMap.getString("next_op_name");
					map.put("next_sub_op", next_sub_op);
					map.put("next_op_name", next_op_name);
				}
				map.put("ins_info_id", ids[i]);
				map.put("acc_id", acc_ids[i]);
				map.put("revise_conclusion", revise_conclusion);
				map.put("op_time", op_time);
				map.put("type", type);
				map.put("flow_num", flow_num);
				// revise_conclusion（结论：1：通过 2：不通过）
				if ("1".equals(revise_conclusion) && type.equals("04")) {
					// 如果审核状态是通过，则进入下一个环节
					map.put("revise_remark", "结论：通过\n");
					map.put("flag", "参数判断是否获取下一步指定人");
					
					inspectionService.flow_saveCheck(map);
					// 提交到下一个环节
					inspectionService.subFlowProcess(map, request);
				} else if ("1".equals(revise_conclusion) && type.equals("05")) {
					map.put("revise_remark", "结论：通过\n");
					map.put("flag", "0");// 如果是0，就不用指定下一步操作人
					inspectionService.flow_saveCheck(map);
					// 返写设备信息
					inspectionService.dealDeviceInfo(map);	// 签发通过时，返写设备信息
					// 提交到下一个环节
					inspectionService.subFlowProcess(map, request);
				} else if ("2".equals(revise_conclusion) && type.equals("04")) {
					String revise_remark = dataMap.getString("revise_remark"); // 不通过原因
					// 如果审核状态是不通过，则返回上一环节
					map.put("revise_remark", "结论：不通过\n" + revise_remark);
					// 回退到上一步
					inspectionService.returApprove(map, request);
				} else if ("2".equals(revise_conclusion) && type.equals("05")) {
					String revise_remark = dataMap.getString("revise_remark"); // 不通过原因
					// 报告签发时，回退可以回退至上一步（报告审核环节），也可以回退至报告录入环节
					map.put("revise_remark", "结论：不通过\n" + revise_remark);
					map.put("backStep", "1");	// 1：退回上一步 2：退回报告录入
					inspectionService.returApprove(map, request);
				}
			}
			returnMap.put("success", true);
			returnMap.put("msg", "操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("success", false);
			returnMap.put("msg", "操作失败！错误代码："+e.toString()+e.getCause().getMessage());
			return returnMap;
		}
		return returnMap;
	}
	

	/**
	 * 移动端获取所有机电、承压检验部门人员信息
	 * @param json
	 * @param map 
	 * @param request 
	 * @return 
	 * @throws KhntException
	 * @throws ParseException 
	 */
	public List<Map<String,Object>> queryEmployeeList() throws ParseException{
		return reportItemRecordDao.queryEmployeeList();
	}
	
	/**
	 * 根据部门ID获取检验部门人员信息
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2016-02-25 下午14:52:00
	 */
	public List<Map<String,Object>> queryEmployeesByOrgId(String org_id) {
		return reportItemRecordDao.queryEmployeesByOrgId(org_id);
	}
	
	/**
	 * 移动端获取电梯检验部门信息
	 * 
	 * @return
	 * @author GaoYa
	 * @date @date 2016-02-26 上午10:59:00
	 */
	public List<Map<String,Object>> queryDtOrgs() {
		return reportItemRecordDao.queryDtOrgs();
	}
	
	/**
	 * 删除移动端上传的原始记录数据
	 * @param json
	 * @param map 
	 * @param request 
	 * @return 
	 * @throws KhntException
	 * @throws ParseException 
	 */
	public void del(HttpServletRequest request, String ids) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();	
		String info_ids[] = ids.split(",");
		for (int i = 0; i < info_ids.length; i++) {
			// 1、删除与原始记录对应的检验业务信息
			infoDao
			.createSQLQuery(
					"update TZSB_INSPECTION_INFO set ysjl_data_status='99',data_status='99' where id = ?",
					info_ids[i]).executeUpdate();
			// 2、删除原始记录
			this.delRecordItems(info_ids[i]);
			
			// 3、写入日志
			logService.setLogs(info_ids[i], "删除原始记录", "原始记录作废", user.getId(), user
					.getName(), new Date(), request.getRemoteAddr());
		}
	}
	
	/**
	 * 将原始记录数据生成报告，并启动流程
	 * @param ids -- 报检业务id
	 * @param checktypes -- 检验类别 
	 * @param request 
	 * @return 
	 * @throws Exception
	 */
	public Map<String, Object> startFlow(HttpServletRequest request) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String ids = request.getParameter("ids");
		String checktypes = request.getParameter("checktypes");
		String flow_code = "";
		String ywid = "";
		String device_type = "";
		Map<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		boolean validate = true;
		String msg = "";
		checktypes = IdFormat.formatIdStr(checktypes);

		String[] temp = ids.split(","); // 报检业务id
		for (int i = 0; i < temp.length; i++) {
			String sql = "select t.id tid,d.device_sort_code,d.device_name,t.is_flow,t.is_report_input,d.id did,d.inspect_next_date,t.advance_time,t.is_back,d.device_sort,d.device_registration_code,d.fk_company_info_use_id,t.report_com_name,t.report_type from TZSB_INSPECTION_INFO t, base_device_document d where d.id=t.fk_tsjc_device_document_id and t.fk_inspection_id='"
					+ temp[i] + "'";
			List list = inspectionDao.createSQLQuery(sql).list();
			Object[] objArr = list.toArray();
			for (int j = 0; j < list.size(); j++) {
				Object[] obj2 = (Object[]) objArr[j];
				// 启动流程前，先验证设备信息是否完整
				// 如果设备信息不完整，就提示用户先完善设备信息；反之，启动流程
				if (obj2[1] == null || obj2[1] == "" || obj2[9] == null || obj2[9] == "") { // 设备类别（三级类别）
					validate = false;
					if(StringUtil.isNotEmpty(msg)){
						msg += "\n";
					}
					msg += "亲，请完善数据管理" + obj2[10] + "的设备类别！";
				} else {
					if (obj2[1] != null && obj2[1] != "") {
						device_type = String.valueOf(obj2[1]);
					}
				}
				if (obj2[2] == null || obj2[2] == "") { // 设备名称
					validate = false;
					if(StringUtil.isNotEmpty(msg)){
						msg += "\n";
					}
					msg += "亲，请完善数据管理" + obj2[10] + "的设备名称！";
				}
				if (obj2[7] == null || obj2[7] == "") { // 检验日期
					validate = false;
					if(StringUtil.isNotEmpty(msg)){
						msg += "\n";
					}
					msg += "亲，请完善数据管理" + obj2[10] + "的检验日期！";
				}

				if (obj2[11] == null || obj2[11] == "") { // 设备使用单位id
					validate = false;
					if(StringUtil.isNotEmpty(msg)){
						msg += "\n";
					}
					msg += "亲，请完善数据管理" + obj2[10] + "的使用单位！";
				} else {
					EnterInfo enterInfo = enterService.get(String.valueOf(obj2[11]));
					if (!enterInfo.getCom_name().equals(obj2[12])) {
						validate = false;
						if(StringUtil.isNotEmpty(msg)){
							msg += "\n";
						}
						msg += "亲，设备" + obj2[10] + "的使用单位与数据管理的不一致哦！";
					}
				}
			}
		}
		
		if (!validate) {
			returnMap.put("success", false);
			returnMap.put("msg", msg);
			return returnMap;
		} else {
			for (int i = 0; i < temp.length; i++) {
				String sql = "select t.id tid,d.device_sort_code,d.device_name,t.is_flow,t.is_report_input,d.id did,d.inspect_next_date,t.advance_time,t.is_back,d.device_sort,d.device_registration_code,d.fk_company_info_use_id,t.report_com_name,t.report_type from TZSB_INSPECTION_INFO t, base_device_document d where d.id=t.fk_tsjc_device_document_id and t.fk_inspection_id='"
						+ temp[i] + "'";
				List list = inspectionDao.createSQLQuery(sql).list();
				Object[] objArr = list.toArray();
				for (int j = 0; j < list.size(); j++) {
					Object[] obj2 = (Object[]) objArr[j];
					// 根据设备类别、检验类别获取相关业务流程
					String hql = "select t.flow_id,t1.check_type from FLOW_SERVICE_CONFIG t,BASE_UNIT_FLOW t1 where t.id=t1.FK_FLOW_ID and t1.DEVICE_TYPE like '"
							+ device_type.substring(0, 1) + "%' and t1.CHECK_TYPE in (" + checktypes
							+ ") and t.org_id='" + user.getDepartment().getId() + "' and t1.FK_REPORT_ID='" + obj2[13]
							+ "'";
					List list2 = inspectionDao.createSQLQuery(hql).list();
					if (list2.size() > 0) {
						Object[] objArr2 = list2.toArray();
						Object[] flow_obj = (Object[]) objArr2[0];
						flow_code = flow_obj[0].toString();
						ywid = obj2[0].toString(); // 检验业务id
						map.put("infoId", ywid);
						map.put("flowId", flow_code); // 流程id
						map.put("status", "1");
						map.put("device_type", device_type); // 设备类别
						map.put("check_type", flow_obj[1].toString()); // 检验类别

						// 将原始记录数据生成报告检验项目表参数
						InspectionInfo info = infoDao.get(ywid);
						// if (obj2[3] == null) {
						// 报告当前流程在“报告录入”或为空时，才可将原始记录数据生成报告检验项目表参数
						if (StringUtil.isEmpty(info.getFlow_note_name()) || "报告录入".equals(info.getFlow_note_name())) {
							// 原始记录生成报告项目表参数前，先删除报告项目参数表（tzsb_report_item_value）中之前保存的数据
							reportItemValueDao.delReportItemValue(ywid);
							// 启动流程前，先获取原始记录，并将原始记录生成检验报告项目表参数
							List<ReportItemRecord> recordList = reportItemRecordDao.queryByInfoId(ywid);
							for (ReportItemRecord reportItemRecord : recordList) {
								ReportItemValue reportItemValue = new ReportItemValue();
								BeanUtils.copyProperties(reportItemRecord, reportItemValue);
								// 原始记录生成报告前，对检验项目中的检验结果进行解析
								// if
								// (StringUtil.isNotEmpty(reportItemRecord.getItem_value()))
								// {
								// 解析检验结果（检验项目代码中A代表检验结果，B代表检验结论）
								// 因检验结论代码中也存在A字符串，结论里的A代表的是监督检验，故此处进行非B判断
								if(StringUtil.isNotEmpty(reportItemRecord.getItem_name())){
									if (!reportItemRecord.getItem_name().contains("B")) {
										// 解析原始记录
										List<ReportRecordParse> parselist = reportRecordParseDao
												.getInfos(info.getReport_type(), reportItemRecord.getItem_name());
										if (parselist.size() > 0) {
											for (ReportRecordParse recordParse : parselist) {
												/*if(recordParse.getItem_name().contains("T_ZD_D_A4.6_1_2")){
													System.out.println(reportItemRecord.getItem_name()+"=="+reportItemRecord.getItem_value());
												}*/
												// 是否单独解析（0：否 1：是）
												if ("1".equals(recordParse.getIs_check())) {
													// 单独解析时，先获取解析运算符
													// 若有解析运算符，则根据运算符进行相应操作
													// 反之，根据解析判断依据来判断
													String check_type = recordParse.getCheck_type(); // 解析判断运算符
													if (StringUtil.isNotEmpty(check_type)) {
														// “+”此处代表拼接运算符
														if ("+".equals(check_type)) {
															reportItemValue
																	.setItem_value((recordParse.getItem_value() != null
																			? recordParse.getItem_value() : "")
																			+ (reportItemRecord.getItem_value() != null
																					? reportItemRecord.getItem_value()
																					: ""));
														}else if("%".equals(check_type)){
															// “%”此处代表判断检验项目值是否包含此单位
															if(StringUtil.isNotEmpty(reportItemRecord.getItem_value())){
																if(reportItemRecord.getItem_value().contains("%")){
																	reportItemValue.setItem_value(reportItemRecord.getItem_value());
																}else{
																	reportItemValue.setItem_value(recordParse.getItem_value());
																}
															}else{
																reportItemValue.setItem_value(recordParse.getItem_value());
															}
														}else if("1=1".equals(check_type)){
															// “%”此处代表判断检验项目值是否包含此单位
															if(StringUtil.isNotEmpty(reportItemRecord.getItem_value())){
																if("无此项".equals(reportItemRecord.getItem_value().trim()) || "√".equals(reportItemRecord.getItem_value().trim())){
																	String check_key = recordParse.getCheck_key(); // 解析判断依据（检验结论key）
																	// 根据解析判断依据，获取原始记录检验结论，依此推断生成如何生成报告的检验结果
																	List<ReportItemRecord> rList = reportItemRecordDao
																			.getInfoByItemName(ywid,
																					info.getReport_type(), check_key);
																	String i_value = "";
																	for (ReportItemRecord rRecord : rList) {
																		String item_value = rRecord.getItem_value();
																		if (StringUtil.isNotEmpty(item_value)) {
																			// 根据结论，推断检验结果
																			if ("合格".equals(item_value)
																					|| "复检合格".equals(item_value)) {
																				i_value = "符合";
																			} else if ("不合格".equals(item_value)
																					|| "复检不合格".equals(item_value)) {
																				i_value = "不符合";
																			} else if ("-".equals(item_value)
																					|| "无此项".equals(item_value)
																					|| "/".equals(item_value)) {
																				i_value = "无此项";
																			} else {
																				// 虽然不做任何处理，但还是感觉自己萌萌哒~
																			}
																		}
																	}
																	reportItemValue.setItem_value(i_value);
																}
															}else{
																reportItemValue.setItem_value(recordParse.getItem_value());
															}
														}
													} else {
														// 无解析运算符时，获取解析判断依据
														String check_key = recordParse.getCheck_key(); // 解析判断依据（检验结论key）
														boolean resetValue = true;
														if (StringUtil.isNotEmpty(check_key)) {
															if (StringUtil.isNotEmpty(reportItemRecord.getItem_value())) {
																if ("无此项".equals(reportItemRecord.getItem_value().trim())
																		|| "不符合".equals(
																				reportItemRecord.getItem_value().trim())
																		|| reportItemRecord.getItem_value().trim().contains("记录")) {
																	resetValue = false;
																	reportItemValue.setItem_value(
																			reportItemRecord.getItem_value());
																}
															}
															if (resetValue) {
																String i_value = "";
																if(StringUtil.isNotEmpty(reportItemRecord.getItem_value())){
																	if (!"√".equals(reportItemRecord.getItem_value()) ) {
																		if (!"资料确认符合".equals(reportItemRecord.getItem_value())
																				&& !"无此项".equals(
																						reportItemRecord.getItem_value())) {
																			// 根据解析判断依据，获取原始记录检验结论，依此推断生成如何生成报告的检验结果
																			List<ReportItemRecord> rList = reportItemRecordDao
																					.getInfoByItemName(ywid,
																							info.getReport_type(), check_key);

																			for (ReportItemRecord rRecord : rList) {
																				String item_value = rRecord.getItem_value();
																				if (StringUtil.isNotEmpty(item_value)) {
																					// 根据结论，推断检验结果
																					if ("合格".equals(item_value)
																							|| "复检合格".equals(item_value)) {
																						i_value = "符合";
																					} else if ("不合格".equals(item_value)
																							|| "复检不合格".equals(item_value)) {
																						i_value = "不符合";
																					} else if ("-".equals(item_value)
																							|| "无此项".equals(item_value)
																							|| "/".equals(item_value)) {
																						i_value = "无此项";
																					} else {
																						// 虽然不做任何处理，但还是感觉自己萌萌哒~
																					}
																				}
																			}
																		} else {
																			i_value = reportItemRecord.getItem_value();
																		}
																	}else{
																		// 根据解析判断依据，获取原始记录检验结论，依此推断生成如何生成报告的检验结果
																		List<ReportItemRecord> rList = reportItemRecordDao
																				.getInfoByItemName(ywid,
																						info.getReport_type(), check_key);
																		for (ReportItemRecord rRecord : rList) {
																			String item_value = rRecord.getItem_value();
																			if (StringUtil.isNotEmpty(item_value)) {
																				// 根据结论，推断检验结果
																				if ("合格".equals(item_value)
																						|| "复检合格".equals(item_value)) {
																					if("无此项".equals(recordParse.getItem_value())){
																						i_value = "无此项";
																					}else{
																						if("资料确认符合".equals(recordParse.getItem_value())){
																							i_value = "资料确认符合";
																						}else{
																							i_value = "符合";
																						}
																					}
																				} else if ("不合格".equals(item_value)
																						|| "复检不合格".equals(item_value)) {
																					i_value = "不符合";
																				} else if ("-".equals(item_value)
																						|| "无此项".equals(item_value)
																						|| "/".equals(item_value)) {
																					i_value = "无此项";
																				} else {
																					// 虽然不做任何处理，但还是感觉自己萌萌哒~
																				}
																			}
																		}
																		// 直接获取解析表中的默认值
																		/*reportItemValue
																				.setItem_value(recordParse.getItem_value());*/
																	}
																	
																	if (StringUtil.isNotEmpty(i_value)) {
																		reportItemValue.setItem_value(i_value);
																	} else {
																		// 根据结论仍找不到检验结果时，直接获取解析表中的默认值
																		reportItemValue
																				.setItem_value(recordParse.getItem_value());
																	}
																}
															}
														} else {
															// 无解析判断依据时，直接获取解析表中的默认值
															reportItemValue.setItem_value(recordParse.getItem_value());
														}
													}
												} else {
													// 非单独解析时，直接获取解析表中的默认值
													reportItemValue.setItem_value(recordParse.getItem_value());
												}
											}
										}
									}
								}
								// }

								if (StringUtil.isNotEmpty(info.getReport_sn())) {
									List<ReportItemRecord> rList = reportItemRecordDao.getInfoByItemName(info.getId(),
											info.getReport_type(), "REPORT_SN");
									if (rList.isEmpty()) {
										// 保存原始记录检验项目记录表（新增）
										reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(),
												info.getReport_type(), "REPORT_SN", info.getReport_sn(), info.getId(),
												user.getId(), user.getName());
									} else {
										ReportItemRecord record = rList.get(0);
										record.setItem_value(info.getReport_sn());
										record.setLast_mdy_uid(user.getId());
										record.setLast_mdy_uname(user.getName());
										record.setLast_mdy_time(DateUtil.getCurrentDateTime());
										reportItemRecordDao.save(record);
									}
								}

								reportItemValue.setId(null);
								reportItemValueDao.save(reportItemValue);
								// 数据状态，默认0（0：未生成报告 // 1：已生成报告 //
								// 99：已删除）
								reportItemRecord.setData_status("1");
								reportItemRecordDao.save(reportItemRecord);
							}
						} else {
							// 报告当前正在流转中时，不可将原始记录数据生成报告检验项目表参数
							returnMap.put("success", false);
							returnMap.put("msg",
									"亲，该设备（" + obj2[10] + "）的检验报告（编号：" + info.getReport_sn() + "）当前正在审批中，不能执行该操作哦！");
							return returnMap;
						}

						if (StringUtil.isEmpty(info.getReport_type())) {
							returnMap.put("success", false);
							returnMap.put("msg", "抱歉，系统暂未找到与该原始记录相匹配的报告模板，请联系系统管理员！");
							return returnMap;
						}
						// 先判断检验业务是否已经启动了流程
						// 业务流程表有数据就说明已经启动了流程，不再重新启动流程，没数据就启动流程

						if (obj2[3] == null) {
							String process_id = inspectionDao.getProcess(ywid);
							if (StringUtil.isEmpty(process_id)) {
								this.startFlowProcess(map, request);
							}
						} else {
							map2.put("ins_info_id", ywid);
							com.khnt.bpm.core.bean.Process process = processManager.getServiceProcess(ywid);
							List<Activity> activities = activityManager.getCurrentServiceActivity(process.getBusinessId());
							if (activities.size() > 0) {
								map2.put("acc_id", activities.get(0).getId());
								map2.put("flow_num", activities.get(0).getActivityId());
							}
							map2.put("flag", "0");
							this.subFlowProcess(map2, request);
						}

						// 设置业务流水号
						if (StringUtil.isEmpty(info.getSn())) {
							// 获取业务流水号
							info.setSn(inspectionInfoService.getInspectionInfoSn(1));
						}

						// 未启动流程前，业务数据状态都为初始状态不进入正常使用流程
						// 启动流程后，检验业务数据状态更新为“使用”状态，表示该业务正式使用
						info.setFee_status("1"); // 收费状态（1：待收费）启动流程后，收费状态更新为待收费状态，进入待收费流程
						info.setData_status("1");
						info.setYsjl_data_status("1"); // 原始记录数据状态（0：未生成报告
														// 1：已生成报告 2：原始记录已修改）
						info.setIs_report_input("2"); // 2：报告已录入
						info.setIs_back("0"); // 默认的状态为1，提交后变成0，退回也变成1
						info.setEnter_time2(new Date());	// 录入时间（实际编制日期，不显示于报告，仅用于数据统计）
						infoDao.save(info);

						// 写入日志
						logService.setLogs(ywid, "生成报告", "将原始记录生成报告", user.getId(), user.getName(), new Date(),
								request.getRemoteAddr());

						// start 添加设备预警处理记录 2014-11-05 11:55:00 update by GaoYa
						inspectionService.saveRecord(user, obj2);
						// end 添加设备预警处理记录 2014-11-05 11:55:00 update by GaoYa
						returnMap.put("success", true);
						returnMap.put("msg", "亲，恭喜您！报告已生成！请到“待处理业务”中完成下一步操作！");
					} else {
						returnMap.put("success", false);
						returnMap.put("msg", "流程启动失败，系统暂未找到该类设备（" + obj2[2].toString() + "）的相关业务流程，请联系系统管理员！");
					}
				}
			}
		}
		return returnMap;
	}
	
	/**
	 * 将原始记录数据生成报告，并启动流程
	 * @param ids -- 报检业务id
	 * @param checktypes -- 检验类别 
	 * @param request 
	 * @return 
	 * @throws Exception
	 */
	public Map<String, Object> createReport(String ids, HttpServletRequest request) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			String [] temp = ids.split(",");	// 报检业务id			
			for(int i = 0;i<temp.length;i++){
				InspectionInfo  info = infoDao.get(temp[i]);
				if (StringUtil.isEmpty(info.getFlow_note_name()) || "报告录入".equals(info.getFlow_note_name())) {
					//原始记录生成报告项目表参数前，先删除报告项目参数表（tzsb_report_item_value）中之前保存的数据
					reportItemValueDao.delReportItemValue(temp[i]);
					//启动流程前，先获取原始记录，并将原始记录生成检验报告项目表参数
					List<ReportItemRecord> recordList = reportItemRecordDao.queryByInfoId(temp[i]);
					for (ReportItemRecord reportItemRecord : recordList) {
						ReportItemValue reportItemValue = new ReportItemValue();
						BeanUtils.copyProperties(reportItemRecord, reportItemValue);
						reportItemValue.setId(null);
						reportItemValueDao.save(reportItemValue);
					}
					//写入日志
					logService.setLogs(temp[i], "生成报告", "将原始记录生成报告", user.getId(), user.getName(), new Date(), request.getRemoteAddr());
					returnMap.put("success", true);
					returnMap.put("msg", "亲，恭喜您！报告已生成成功！请到“待处理业务”中完成下一步操作！");
				}else{
					returnMap.put("success", false);
					returnMap.put("msg", "亲，该检验报告（"+info.getReport_sn()+"）当前正在流转中，不能执行该操作哦！");
					return returnMap;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("success", false);
			returnMap.put("msg", "流程启动失败，错误代码："+e.toString()+e.getCause().getMessage()+"，请联系系统管理员！");
		}
		return returnMap;
	}
	
	/**
	 * 启动流程
	 * @param map -- 流程参数

	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized void startFlowProcess(HashMap<String,String> map, HttpServletRequest request) {
		try{
			//CurrentSessionUser user = SecurityUtil.getSecurityUser();
			
			Map<String, Object> param = new HashMap<String, Object>();
			
			//业务ID
			String infoId = map.get("infoId");
			//流程id
			String flowId = map.get("flowId");
			//设备类别
			String device_type = map.get("device_type");
			//流程id
			//String check_type = map.get("check_type");
			
			InspectionInfo  info = infoDao.get(infoId);
			//判断是否已经启动流程（1 未进入流程 2 进入流程）
			if(!"2".equals(info.getIs_flow()) || StringUtil.isEmpty(info.getIs_flow())){
				//获取流程名称
				String flow_name = flowDefManager.get(flowId).getFlowname();
				String flow_type =  flowDefManager.get(flowId).getFlowtype();
				//流程业务ID
				param.put(FlowExtWorktaskParam.SERVICE_ID, infoId);
				//业务标题
				param.put(FlowExtWorktaskParam.SERVICE_TITLE,flow_name);
				//流程ID
				param.put(FlowExtWorktaskParam.FLOW_DEFINITION_ID, flowId);
				//第一个环节任务处理方式
				param.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, true);
				param.put(FlowExtWorktaskParam.SERVICE_TYPE, flow_type);
				
				//启动流程
				Map<String, Object> flowMap = flowExtManager.startFlowProcess(param);
				//获取下一步流程步骤
				List<Activity> list = (List) flowMap.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);
				//流程ID插入业务表
				info.setFk_flow_index_id(flowId);
				//更新业务表状态 1 未进入流程 2 进入流程
				info.setIs_flow("2");
				//流程状态 01 业务报检 02 报告录入 03 报告审核 04 报告签发  05 打印报告 06领取报告 07报告归档
				info.setFlow_note_id(list.get(0).getActivityId());
				info.setFlow_note_name(list.get(0).getName());
				
				// 设置业务流水号
				if(StringUtil.isEmpty(info.getSn())){
					//获取业务流水号
		    		info.setSn(inspectionInfoService.getInspectionInfoSn(1));
				}
				
				// 生成报告书编号
				/*if(StringUtil.isEmpty(info.getReport_sn())){
					String report_sn = reportService.generateReportCode(device_type, 
							check_type, info.getReport_type(), info.getCheck_unit_id());
					String report_sn = inspectionService.synReportSN("0", info.getId(), device_type, check_type, info.getReport_type());
					
					//报告输编号插入业务主表
					info.setReport_sn(report_sn);
					//将报告书编号更新到报告检验项目参数表
					reportItemValueDao.createSQLQuery("update  TZSB_REPORT_ITEM_VALUE set item_value='"+report_sn+"'where fk_report_id='"+info.getReport_type()+"' and item_name='REPORT_SN' and fk_inspection_info_id='"+infoId+"'").executeUpdate();
					reportItemValueDao.createSQLQuery("update  tzsb_report_item_record set item_value='"+report_sn+"'where fk_report_id='"+info.getReport_type()+"' and item_name='REPORT_SN' and fk_inspection_info_id='"+infoId+"'").executeUpdate();
				}else{
					//将报告书编号更新到报告检验项目参数表
					reportItemValueDao.createSQLQuery("update  TZSB_REPORT_ITEM_VALUE set item_value='"+info.getReport_sn()+"'where fk_report_id='"+info.getReport_type()+"' and item_name='REPORT_SN' and fk_inspection_info_id='"+infoId+"'").executeUpdate();
					reportItemValueDao.createSQLQuery("update  tzsb_report_item_record set item_value='"+info.getReport_sn()+"'where fk_report_id='"+info.getReport_type()+"' and item_name='REPORT_SN' and fk_inspection_info_id='"+infoId+"'").executeUpdate();
				}*/
				
				// 启动流程后，自动计算下次检验日期，下次检验日期=（检验日期 + 1年）- 1天
				Date inspection_date = info.getAdvance_time();
				if (inspection_date != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(inspection_date);
					calendar.add(Calendar.YEAR, 1);	
					if (!device_type.startsWith("9")) {
						//	客运索道（device_sort_code为9开头的4位数字）报告只显示到年月，不需要精确到年月日，故此处不用减去一天
						calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
					}
					info.setLast_check_time(calendar.getTime());
				}

				// 启动流程后，自动计算录入/编制日期
				if(device_type.startsWith("3") || device_type.startsWith("4") || device_type.startsWith("6") || device_type.startsWith("9")){
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(inspection_date);
					// 起重机编制日期（机电五部2015-08-24要求，编制日期=检验日期+1天）
					// 游乐设施编制日期（机电五部2015-09-25龚高科要求，编制日期=检验日期+1天）
					// 客运索道编制日期（机电五部2015-10-15龚高科要求，编制日期=检验日期+1天）
					calendar.add(Calendar.DATE, 1);	
					
					// 检验日期为当天的，编制日期不再+1，默认为当天
					Date cur_date = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.getCurrentDateTime());
					Date draft_date = null;
					if(calendar.getTime().after(cur_date)){
						draft_date = cur_date;
					}else{
						draft_date = calendar.getTime();
					}
					info.setEnter_time(draft_date);					
				}				
				info.setIs_report_input("2");	// 2：报告已录入
				info.setFee_status("1");		// 收费状态（1：待收费）启动流程后，收费状态更新为待收费状态，进入待收费流程
				info.setIs_back("0");			// 默认的状态为1，提交后变成0，退回也变成1
				
				// 移动检验数据状态（0：未生成报告 1：已生成报告 2：原始记录已修改）
				// 未启动流程前，业务数据状态都为初始状态不进入正常使用流程
				// 启动流程后，检验业务数据状态更新为“使用”状态，表示该业务正式使用
				info.setData_status("1");		
				infoDao.save(info);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 将原始记录数据生成报告，并启动流程（罐车）
	 * @param ids -- 报检业务id
	 * @param checktypes -- 检验类别 
	 * @param request 
	 * @return 
	 * @throws Exception
	 */
	public Map<String, Object> startFlowTanker(HttpServletRequest request) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String ids = request.getParameter("ids");
		String checktypes = request.getParameter("checktypes");
		String flow_code = "";
		String ywid = "";
		String device_type = "";
		Map<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		boolean validate = true;
		String msg = "";
		checktypes = IdFormat.formatIdStr(checktypes);

		String[] temp = ids.split(","); // 报检业务id
		for (int i = 0; i < temp.length; i++) {
			String sql = "select t.id tid,d.device_sort_code,d.device_name,t.is_flow,t.is_report_input,d.id did,d.inspect_next_date,t.advance_time,t.is_back,d.device_sort,d.device_registration_code,d.fk_company_info_use_id,t.report_com_name,t.report_type from TZSB_INSPECTION_INFO t, base_device_document d where d.id=t.fk_tsjc_device_document_id and t.fk_inspection_id='"
					+ temp[i] + "'";
			List list = inspectionDao.createSQLQuery(sql).list();
			Object[] objArr = list.toArray();
			for (int j = 0; j < list.size(); j++) {
				Object[] obj2 = (Object[]) objArr[j];
				// 启动流程前，先验证设备信息是否完整
				// 如果设备信息不完整，就提示用户先完善设备信息；反之，启动流程
				if (obj2[1] == null || obj2[1] == "" || obj2[9] == null || obj2[9] == "") { // 设备类别（三级类别）
					validate = false;
					if(StringUtil.isNotEmpty(msg)){
						msg += "\n";
					}
					msg += "亲，请完善数据管理" + obj2[10] + "的设备类别！";
				} else {
					if (obj2[1] != null && obj2[1] != "") {
						device_type = String.valueOf(obj2[1]);
					}
				}
				if (obj2[2] == null || obj2[2] == "") { // 设备名称
					validate = false;
					if(StringUtil.isNotEmpty(msg)){
						msg += "\n";
					}
					msg += "亲，请完善数据管理" + obj2[10] + "的设备名称！";
				}
				if (obj2[7] == null || obj2[7] == "") { // 检验日期
					validate = false;
					if(StringUtil.isNotEmpty(msg)){
						msg += "\n";
					}
					msg += "亲，请完善数据管理" + obj2[10] + "的检验日期！";
				}

				if (obj2[11] == null || obj2[11] == "") { // 设备使用单位id
					validate = false;
					if(StringUtil.isNotEmpty(msg)){
						msg += "\n";
					}
					msg += "亲，请完善数据管理" + obj2[10] + "的使用单位！";
				} else {
					EnterInfo enterInfo = enterService.get(String.valueOf(obj2[11]));
					if (!enterInfo.getCom_name().equals(obj2[12])) {
						validate = false;
						if(StringUtil.isNotEmpty(msg)){
							msg += "\n";
						}
						msg += "亲，设备" + obj2[10] + "的使用单位与数据管理的不一致哦！";
					}
				}
			}
		}
		
		if (!validate) {
			returnMap.put("success", false);
			returnMap.put("msg", msg);
			return returnMap;
		} else {
			for (int i = 0; i < temp.length; i++) {
				String sql = "select t.id tid,d.device_sort_code,d.device_name,t.is_flow,t.is_report_input,d.id did,d.inspect_next_date,t.advance_time,t.is_back,d.device_sort,d.device_registration_code,d.fk_company_info_use_id,t.report_com_name,t.report_type from TZSB_INSPECTION_INFO t, base_device_document d where d.id=t.fk_tsjc_device_document_id and t.fk_inspection_id='"
						+ temp[i] + "'";
				List list = inspectionDao.createSQLQuery(sql).list();
				Object[] objArr = list.toArray();
				for (int j = 0; j < list.size(); j++) {
					Object[] obj2 = (Object[]) objArr[j];
					// 根据设备类别、检验类别获取相关业务流程
					String hql = "select t.flow_id,t1.check_type from FLOW_SERVICE_CONFIG t,BASE_UNIT_FLOW t1 where t.id=t1.FK_FLOW_ID and t1.DEVICE_TYPE like '"
							+ device_type.substring(0, 1) + "%' and t1.CHECK_TYPE in (" + checktypes
							+ ") and t.org_id='" + user.getDepartment().getId() + "' and t1.FK_REPORT_ID='" + obj2[13]
							+ "'";
					List list2 = inspectionDao.createSQLQuery(hql).list();
					if (list2.size() > 0) {
						Object[] objArr2 = list2.toArray();
						Object[] flow_obj = (Object[]) objArr2[0];
						flow_code = flow_obj[0].toString();
						ywid = obj2[0].toString(); // 检验业务id
						map.put("infoId", ywid);
						map.put("flowId", flow_code); // 流程id
						map.put("status", "1");
						map.put("device_type", device_type); // 设备类别
						map.put("check_type", flow_obj[1].toString()); // 检验类别

						// 将原始记录数据生成报告检验项目表参数
						InspectionInfo info = infoDao.get(ywid);
						// if (obj2[3] == null) {
						// 报告当前流程在“报告录入”或为空时，才可将原始记录数据生成报告检验项目表参数
						if (StringUtil.isEmpty(info.getFlow_note_name()) || "报告录入".equals(info.getFlow_note_name())) {
							// 原始记录生成报告项目表参数前，先删除报告项目参数表（tzsb_report_item_value）中之前保存的数据
							reportItemValueDao.delReportItemValue(ywid);
							// 启动流程前，先获取原始记录，并将原始记录生成检验报告项目表参数
							List<ReportItemRecord> recordList = reportItemRecordDao.queryByInfoId(ywid);
							for (ReportItemRecord reportItemRecord : recordList) {
								ReportItemValue reportItemValue = new ReportItemValue();
								BeanUtils.copyProperties(reportItemRecord, reportItemValue);

								if (StringUtil.isNotEmpty(info.getReport_sn())) {
									List<ReportItemRecord> rList = reportItemRecordDao.getInfoByItemName(info.getId(),
											info.getReport_type(), "REPORT_SN");
									if (rList.isEmpty()) {
										// 保存原始记录检验项目记录表（新增）
										reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(),
												info.getReport_type(), "REPORT_SN", info.getReport_sn(), info.getId(),
												user.getId(), user.getName());
									} else {
										ReportItemRecord record = rList.get(0);
										record.setItem_value(info.getReport_sn());
										record.setLast_mdy_uid(user.getId());
										record.setLast_mdy_uname(user.getName());
										record.setLast_mdy_time(DateUtil.getCurrentDateTime());
										reportItemRecordDao.save(record);
									}
								}

								reportItemValue.setId(null);
								reportItemValueDao.save(reportItemValue);
								// 数据状态，默认0（0：未生成报告 // 1：已生成报告 //
								// 99：已删除）
								reportItemRecord.setData_status("1");
								reportItemRecordDao.save(reportItemRecord);
							}
						} else {
							// 报告当前正在流转中时，不可将原始记录数据生成报告检验项目表参数
							returnMap.put("success", false);
							returnMap.put("msg",
									"亲，该设备（" + obj2[10] + "）的检验报告（编号：" + info.getReport_sn() + "）当前正在审批中，不能执行该操作哦！");
							return returnMap;
						}

						if (StringUtil.isEmpty(info.getReport_type())) {
							returnMap.put("success", false);
							returnMap.put("msg", "抱歉，系统暂未找到与该原始记录相匹配的报告模板，请联系系统管理员！");
							return returnMap;
						}
						// 先判断检验业务是否已经启动了流程
						// 业务流程表有数据就说明已经启动了流程，不再重新启动流程，没数据就启动流程

						if (obj2[3] == null) {
							String process_id = inspectionDao.getProcess(ywid);
							if (StringUtil.isEmpty(process_id)) {
								this.startFlowProcess(map, request);
							}
						} else {
							map2.put("ins_info_id", ywid);
							com.khnt.bpm.core.bean.Process process = processManager.getServiceProcess(ywid);
							List<Activity> activities = activityManager.getCurrentServiceActivity(process.getBusinessId());
							if (activities.size() > 0) {
								map2.put("acc_id", activities.get(0).getId());
								map2.put("flow_num", activities.get(0).getActivityId());
							}
							map2.put("flag", "0");
							this.subFlowProcess(map2, request);
						}

						// 设置业务流水号
						if (StringUtil.isEmpty(info.getSn())) {
							// 获取业务流水号
							info.setSn(inspectionInfoService.getInspectionInfoSn(1));
						}

						// 未启动流程前，业务数据状态都为初始状态不进入正常使用流程
						// 启动流程后，检验业务数据状态更新为“使用”状态，表示该业务正式使用
						info.setFee_status("1"); // 收费状态（1：待收费）启动流程后，收费状态更新为待收费状态，进入待收费流程
						info.setData_status("1");
						info.setYsjl_data_status("1"); // 原始记录数据状态（0：未生成报告
														// 1：已生成报告 2：原始记录已修改）
						info.setIs_report_input("2"); // 2：报告已录入
						info.setIs_back("0"); // 默认的状态为1，提交后变成0，退回也变成1
						info.setEnter_time2(new Date());	// 录入时间（实际编制日期，不显示于报告，仅用于数据统计）
						infoDao.save(info);

						// 写入日志
						logService.setLogs(ywid, "生成报告", "将原始记录生成报告", user.getId(), user.getName(), new Date(),
								request.getRemoteAddr());

						// start 添加设备预警处理记录 2014-11-05 11:55:00 update by GaoYa
						inspectionService.saveRecord(user, obj2);
						// end 添加设备预警处理记录 2014-11-05 11:55:00 update by GaoYa
						returnMap.put("success", true);
						returnMap.put("msg", "亲，恭喜您！报告已生成！请到“待处理业务”中完成下一步操作！");
					} else {
						returnMap.put("success", false);
						returnMap.put("msg", "流程启动失败，系统暂未找到该类设备（" + obj2[2].toString() + "）的相关业务流程，请联系系统管理员！");
					}
				}
			}
		}
		return returnMap;
	}
	
	/**
	 * 将原始记录数据生成报告，并启动流程
	 * @param ids -- 报检业务id
	 * @param checktypes -- 检验类别 
	 * @param request 
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> startFlowTanker(String ids, String checktypes, HttpServletRequest request) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		
		String flow_code = "";
		String ywid = "";
		String device_type = "";
		Map<String, Object> returnMap = new HashMap<String, Object>();
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		boolean validate = true;
		String  msg = "";
		checktypes = IdFormat.formatIdStr(checktypes);
		
		String [] temp = ids.split(",");	// 报检业务id			
		for(int i = 0;i<temp.length;i++){
			String sql = "select t.id tid,d.device_sort_code,d.device_name,t.is_flow,t.is_report_input,d.id did,d.inspect_next_date,t.advance_time,t.is_back,d.device_sort,d.device_registration_code,d.fk_company_info_use_id,t.report_com_name,t.report_type from TZSB_INSPECTION_INFO t, base_device_document d where d.id=t.fk_tsjc_device_document_id and t.fk_inspection_id='"+temp[i]+"'";
			List list = inspectionDao.createSQLQuery(sql).list();
			Object[] objArr = list.toArray();
			for (int j = 0; j < list.size(); j++) {
				Object[] obj2 = (Object[]) objArr[j];
				// 启动流程前，先验证设备信息是否完整
				// 如果设备信息不完整，就提示用户先完善设备信息；反之，启动流程
				if (obj2[1] == null || obj2[1] == "" || obj2[9] == null || obj2[9] == "") {		// 设备类别（三级类别）
					validate = false;
					msg += "亲，该设备（"+obj2[10]+"）的设备类别暂未完善，请到“数据管理”中补充完整！";
				}else{
					if(obj2[1] != null && obj2[1] != ""){
						device_type = String.valueOf(obj2[1]);
					}
				}
				if(obj2[2] == null || obj2[2] == ""){	// 设备名称
					validate = false;
					msg += "亲，该设备（"+obj2[10]+"）的设备名称暂未完善，请到“数据管理”中补充完整！";
				}
				if(obj2[7] == null || obj2[7] == ""){	// 检验日期
					validate = false;
					msg += "亲，该设备（"+obj2[10]+"）的检验日期暂未完善，请修改您的原始记录！";
				}else{
					int curYear = TS_Util.getCurrentYear();	// 获取系统当前年份
					if (String.valueOf(obj2[7]).contains("/")) {
						Date date = dateformat.parse(
							(String.valueOf(obj2[7]).replace("/", "-")).toString());
						int year  = Integer.parseInt(DateUtil.getDateTime("yyyy", date)); 
						if (year != curYear) {
							validate = false;
							msg += "亲，该设备（"+obj2[10]+"）的检验日期为"+year+"年，当前年份是"+curYear+"年哦，请纠正！";
						}
					}else if(String.valueOf(obj2[7]).contains(".")){
						Date date = dateformat.parse(
								(String.valueOf(obj2[7]).replace(".", "-")).toString());
						int year  = Integer.parseInt(DateUtil.getDateTime("yyyy", date)); 
						if (year != curYear) {
							validate = false;
							msg += "亲，该设备（"+obj2[10]+"）的检验日期为"+year+"年，当前年份是"+curYear+"年哦，请纠正！";
						}
					}else{
						Date date = DateUtil.convertStringToDate("yyyy-MM-dd", String.valueOf(obj2[7]));
						int year  = Integer.parseInt(DateUtil.getDateTime("yyyy", date)); 
						if (year != curYear) {
							validate = false;
							msg += "亲，该设备（"+obj2[10]+"）的检验日期为"+year+"年，当前年份是"+curYear+"年哦，请纠正！";
						}
					}
				}
				
				if(obj2[11] == null || obj2[11] == ""){	// 设备使用单位id
					validate = false;
					msg += "亲，该设备（"+obj2[10]+"）的使用单位暂未完善，请到“数据管理”中补充完整！";
				}else{
					EnterInfo enterInfo = enterService.get(String.valueOf(obj2[11]));
					if (!enterInfo.getCom_name().equals(obj2[12])) {
						validate = false;
						msg += "亲，该设备（"+obj2[10]+"）原始记录中的使用单位与“数据管理”中不一致哦，请纠正！";
					}
				}
				
				if (!validate) {
					returnMap.put("success", false);
					returnMap.put("msg", msg);
					return returnMap;
				}
				
				if(obj2[8]!=null){					
					if(!obj2[8].toString().equals("2")){	
						String hql = "select t.flow_id from FLOW_SERVICE_CONFIG t where t.service_code='CYGC'";
						List list2 = inspectionDao.createSQLQuery(hql).list(); 
						if(list2.size()>0){
							Object[] objArr2 = list2.toArray();
							flow_code = objArr2[0].toString();
							ywid = obj2[0].toString();	// 检验业务id
							map.put("infoId", ywid);
							map.put("flowId", flow_code);	// 流程id
							map.put("type", "2");//1 表示电脑启动 2表示 原始记录启动							if(obj2[3]==null){
								// 将原始记录数据生成报告检验项目表参数
								InspectionInfo  info = infoDao.get(ywid);
								// 报告当前流程在“报告录入”或为空时，才可将原始记录数据生成报告检验项目表参数
								if (StringUtil.isEmpty(info.getFlow_note_name()) || "报告录入".equals(info.getFlow_note_name())) {
									//原始记录生成报告项目表参数前，先删除报告项目参数表（tzsb_report_item_value）中之前保存的数据
									reportItemValueDao.delReportItemValue(ywid);
									//启动流程前，先获取原始记录，并将原始记录生成检验报告项目表参数
									List<ReportItemRecord> recordList = reportItemRecordDao.queryByInfoId(ywid);
									for (ReportItemRecord reportItemRecord : recordList) {
										ReportItemValue reportItemValue = new ReportItemValue();
										BeanUtils.copyProperties(reportItemRecord, reportItemValue);
										reportItemValue.setId(null);
										reportItemValueDao.save(reportItemValue);
									}
								}else{
									// 报告当前正在流转中时，不可将原始记录数据生成报告检验项目表参数
									returnMap.put("success", false);
									returnMap.put("msg", "亲，该设备（"+obj2[10]+"）的检验报告（编号："+info.getReport_sn()+"）当前正在流转中，不能执行该操作哦！");
									return returnMap;
								}
								
								if(StringUtil.isEmpty(info.getReport_type())){
									returnMap.put("success", false);
									returnMap.put("msg", "抱歉，系统暂未找到与该原始记录相匹配的报告模板，请联系系统管理员！");
							
									return returnMap;
								}
								
								// 先判断检验业务是否已经启动了流程
								// 业务流程表有数据就说明已经启动了流程，不再重新启动流程，没数据就启动流程
								String process_id = inspectionDao.getProcess(ywid);
								if(StringUtil.isEmpty(process_id)){
									inspectionInfoService.StarFlowProcess(map, request);
								}
							}
							// start 添加设备预警处理记录 2014-11-05 11:55:00 update by GaoYa
							inspectionService.saveRecord(user, obj2);
							// end 添加设备预警处理记录 2014-11-05 11:55:00 update by GaoYa
							returnMap.put("success", true);
							returnMap.put("msg", "亲，恭喜您！报告已生成，流程启动成功！请到“待处理业务”中完成下一步操作！");
						}else{
							returnMap.put("success", false);
							returnMap.put("msg", "流程启动失败，系统暂未找到该类设备（"+obj2[2].toString()+"）的相关业务流程，请联系系统管理员！");
						}
					}else{
						continue;
					}
			}
		}
		return returnMap;
	}
	
	
	/**
	 * 提交流程
	 * @param map -- 流程参数
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized void subFlowProcess(Map<String, Object> map, HttpServletRequest request) throws Exception{
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		
		String infoId = map.get("ins_info_id").toString();
		String acc_id = map.get("acc_id").toString();
		String flow_num = map.get("flow_num").toString();
		String flag = map.get("flag").toString();
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		JSONObject dataBus = new JSONObject();
		
		if((!"0".equals(flag)&&!"1".equals(flag))){
			String next_sub_op = map.get("next_sub_op").toString();
			String next_op_name = map.get("next_op_name").toString();
			
			// 从数据总线中获取下一步操作人
			JSONArray  pts = new JSONArray();
			JSONObject pt = new JSONObject();
			pt.put("id", next_sub_op);
			pt.put("name", next_op_name);
			pts.put(pt);
			dataBus.put("paticipator", pts);
	
			paramMap.put(FlowExtWorktaskParam.DATA_BUS, dataBus);
		}
		
		InspectionInfo info = infoDao.get(infoId);
		paramMap.put(FlowExtWorktaskParam.ACTIVITY_ID, acc_id);
		paramMap.put(FlowExtWorktaskParam.SERVICE_ID, infoId);
		
		if(info.getFlow_note_id().equals(flow_num)){
			Map<String, Object> flowMap = flowExtManager.submitActivity(paramMap);
			// 获取下一步流程步骤
			List<Activity> list = (List) flowMap.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);			
			// 写入日志
			String step_name = info.getFlow_note_name();	
			String op_conclusion = "从"+step_name+"环节进入"+list.get(0).getName()+"环节。";				 	
			String revise_remark = map.get("revise_remark")==null?"":map.get("revise_remark").toString();
				
			if(!"".equals(revise_remark)){
				op_conclusion=op_conclusion +map.get("revise_remark").toString();					
			}
			info.setFlow_note_id(list.get(0).getActivityId());
			info.setFlow_note_name(list.get(0).getName());			
			info.setIs_back("0");	// 默认的状态为1，提交后变成0，退回也变成1
			info.setIs_report_input("2");	// 2：报告已录入
			info.setData_status("1");		// 启动流程后，检验业务数据状态更新为“使用”状态，表示该业务正式使用
			infoDao.save(info);
			try {
				logService.setLogs(infoId, step_name, op_conclusion, user.getId(), user.getName(), new Date(), request.getRemoteAddr());
				if ("报告录入".equals(step_name)) {
					String to_user_name = "提交至"+map.get("next_op_name").toString();
					logService.setLogs(infoId, "提交报告审核", to_user_name, user.getId(), user.getName(), new Date(), request.getRemoteAddr());
				}
				if("报告审核".equals(step_name)){
					String to_user_name = "提交至"+map.get("next_op_name").toString();
					logService.setLogs(infoId, step_name+"提交", to_user_name, user.getId(), user.getName(), new Date(), request.getRemoteAddr());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
	
	/**
	 * 移动端获取报告审核人员信息
	 * @param org_id -- 部门id
	 * 
	 * @return 
	 */
	public List<Map<String,Object>> getShUsersList(String org_id) {
		return reportItemRecordDao.getShUsersList(org_id);
	}
	
	/**
	 * 移动端获取报告签发人员信息
	 * @param org_id -- 部门id
	 * 
	 * @return 
	 */
	public List<Map<String,Object>> getQfUsersList(String org_id) {
		return reportItemRecordDao.getQfUsersList(org_id);
	}
	
	/**
	 * 移动端获取待处理业务信息
	 * @param user_id -- 用户id
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTasks(String user_id) {
		List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
		try {
			if (StringUtil.isNotEmpty(user_id)) {
				// 1、获取待审核、待签发的业务信息
				String sql = "SELECT  T.ACTIVITY_NAME,subStr(t.TITLE,0, instr(t.TITLE,'-')-1) as flowName,count(t.id) as task_count,substr(d.device_sort_code, 0, 1) as big_class"
						+ "  FROM V_PUB_WORKTASK T, TZSB_INSPECTION_INFO b,TZSB_INSPECTION T1,flow_activity a,base_device_document d where "
						+ " t.SERVICE_ID = b.id and B.FK_INSPECTION_ID=T1.ID and b.flow_note_id = a.activity_id and a.id = t.ACTIVITY_ID and b.fk_tsjc_device_document_id=d.id "
						+ " and T.STATUS='0' and b.data_status<>'99' and t.HANDLER_ID='" + user_id + "'"
						+ " group by t.ACTIVITY_NAME, subStr(t.TITLE,0, instr(t.TITLE,'-')-1), substr(d.device_sort_code, 0, 1)";
				List list = reportItemRecordDao.createSQLQuery(sql).list();
				if (!list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						Object[] objArr = list.toArray();
						Object[] obj = (Object[]) objArr[i];
						if ("报告审核".equals(obj[0]) || "报告签发".equals(obj[0])) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("task_name", obj[0]);
							map.put("flow_name", obj[1]);
							map.put("task_count", obj[2]);
							map.put("device_type", obj[3]);
							returnlist.add(map);
						}
					}
				}

				// 2、获取待校核的业务信息
				// 2.1、获取employee信息（报告书内所有业务人员全部使用的是employee，故此处要获取employee表数据）
				//Employee employee = employeesDao.queryEmployeeByUser(user_id);
				//if (employee != null) {
					String sql2 = "SELECT substr(d.device_sort_code, 0, 1) as big_class, count(b.id) as task_count "
							+ " FROM TZSB_INSPECTION_INFO b, base_device_document d ,sys_user u "
							+ " where b.fk_tsjc_device_document_id=d.id(+) and b.data_status<>'99' and b.is_mobile='1' and b.enter_op_id !=  u.employee_id "
							+ " and instr(b.check_op_id,u.employee_id)>0 and (b.is_report_confirm<>'1' or b.is_report_confirm is null)"
							+" and  u.id = '"+user_id+"'"
							+ " group by substr(d.device_sort_code, 0, 1)" ;

					List list2 = reportItemRecordDao.createSQLQuery(sql2).list();
					if (!list2.isEmpty()) {
						for (int i = 0; i < list2.size(); i++) {
							Object[] objArr = list2.toArray();
							Object[] obj = (Object[]) objArr[i];
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("task_name", "原始记录校核");
							map.put("flow_name", "");
							map.put("task_count", obj[1]);
							map.put("device_type", obj[0]);
							returnlist.add(map);
						}
					}
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnlist;
	}
	
	/**
	 * 移动端获取待校核原始记录
	 * @param user_id -- 用户id
	 * @param user_name -- 用户姓名
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2017-03-01 下午16:05:00
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRecords(String user_id, String user_name) {
		List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
		try {
			if (StringUtil.isNotEmpty(user_id)) {
				// 获取employee信息（报告书内所有业务人员全部使用的是employee，故此处要获取employee表数据）
				Employee employee = employeesDao.queryEmployeeByUser(user_id, user_name);
				if(employee != null){
					String sql = "SELECT d.device_registration_code,b.report_sn,b.report_com_name,to_char(b.advance_time,'yyyy-mm-dd'),"
							+ " b.check_op_name,b.enter_op_name,b.is_report_confirm,b.confirm_name,to_char(b.confirm_date,'yyyy-mm-dd'),b.id " + " FROM TZSB_INSPECTION_INFO b, base_device_document d "
							+ " where b.fk_tsjc_device_document_id=d.id(+) and b.data_status<>'99' and b.is_mobile='1' and b.enter_op_id != '"
							+ employee.getId() + "'" + " and instr(b.check_op_id,'" + employee.getId()
							+ "')>0 and b.is_report_confirm<>'1'" + " ";

					List list = reportItemRecordDao.createSQLQuery(sql).list();
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							Object[] objArr = list.toArray();
							Object[] obj = (Object[]) objArr[i];

							Map<String, Object> map = new HashMap<String, Object>();
							map.put("device_registration_code", obj[0]);
							map.put("report_sn", obj[1]);
							map.put("report_com_name", obj[2]);
							map.put("advance_time", obj[3]);
							map.put("check_op_name", obj[4]);
							map.put("enter_op_name", obj[5]);
							map.put("is_report_confirm", obj[6]);
							map.put("confirm_name", obj[7]);
							map.put("confirm_date", obj[8]);
							map.put("id", obj[9]);
							returnlist.add(map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnlist;
	}
	
	/**
	 * 移动端获取人员角色信息
	 * @param user_id -- 用户id
	 * 
	 * @return 
	 */
	public String getUserRoles(String user_id) {
		return reportItemRecordDao.getUserRoles(user_id);
	}
	
	/**
	 *  移动端根据设备ID获取设备信息
	 * @param device_id -- 设备id
	 * 
	 * @return 
	 */
	public List<Map<String,Object>> getDevices(String device_id) {
		return reportItemRecordDao.getDevices(device_id);
	}
	
	/**
	 *  移动端根据设备车牌号获取设备信息（罐车）
	 * @param device_id -- 设备id
	 * 
	 * @return 
	 */
	public Map<String,Object> getDeviceByInternal_num(String internal_num) {
		return reportItemRecordDao.getDeviceByInternal_num(internal_num);
	}
	
	/**
	 *  移动端根据业务ID获取设备信息
	 * @param device_id -- 设备id
	 * 
	 * @return 
	 */
	public List<Map<String,Object>> getDevicesByInfoId(String info_id) {
		return reportItemRecordDao.getDevicesByInfoId(info_id);
	}
	
	/**
	 *  移动端根据单位ID和单位名称获取设备信息
	 * @param com_id -- 单位id
	 * @param com_name -- 单位名称
	 * 
	 * @return 
	 */
	public List<Map<String,Object>> getDeviceList(String com_id, String com_name) {
		return reportItemRecordDao.getDeviceList(com_id, com_name);
	}

	/**
	 *  根据报告业务ID和报告模板ID获取原始记录目录信息（电梯）
	 * @param map -- 包含参数报告业务ID、报告模板ID
	 * 
	 * @return 
	 */
	public HashMap<String, Object>  openCatalog(Map<String, Object> map) throws Exception{	
		HashMap<String, Object> listMap = new HashMap<String, Object>();
		// 获取业务信息表
		InspectionInfo info = infoDao.get(map.get("id").toString());
		// 获取报告模板信息
		Report report = reportDao.get(map.get("report_id").toString());
		String report_name = report.getReport_name();
		
		String[] record_item = report.getYsjl_pages().split(",");
		String[] ysjl_item = info.getYsjl_item().split(",");
		
		List<SetReportItem>  setList = new ArrayList<SetReportItem>();
		// 配置页面显示
		for(int i = 0;i<record_item.length;i++){
			SetReportItem  item = new SetReportItem();
			item.setPage_index(record_item[i]);
			item.setIs_must("yes");
			if(report_name.contains("2号修改单") || report_name.contains("新疆")){
				if("1".equals(record_item[i])){
					item.setIndex_text("封面");
				}else if("2".equals(record_item[i])){
					item.setIndex_text("填写规则");
				}else if("3".equals(record_item[i])){
					item.setIndex_text("检验结论");
				}else if("4".equals(record_item[i])){
					item.setIndex_text("信息表");
				}else if(Integer.parseInt(record_item[i])>4){
					item.setIndex_text("检验项目表（"+(Integer.parseInt(record_item[i])-4)+"）");
				}
			} else {
				if ("1".equals(record_item[i])) {
					item.setIndex_text("封面");
				} else if ("2".equals(record_item[i])) {
					item.setIndex_text("填写规则");
				} else if ("3".equals(record_item[i])) {
					item.setIndex_text("检验结论");
				} else if (Integer.parseInt(record_item[i]) > 3) {
					item.setIndex_text("检验项目表（" + (Integer.parseInt(record_item[i]) - 3) + "）");
				}
			}
			
			// 带限速器的模板，判断页面是否是限速器页
			// 如果带限速器，设置限速器为选填项目
			if("1".equals(report.getIs_xsq())){
				if (report_name.contains("九寨")){
					if(i==(record_item.length-1)){
						item.setIs_must("no");
						item.setIndex_text("限速器");
					}
				}else{
					if(i==(record_item.length-2)){
						item.setIs_must("no");
						item.setIndex_text("轿厢限速器");
					}else if(i==(record_item.length-1)){
						item.setIs_must("no");
						item.setIndex_text("对重限速器");
					}
				}
			}
			
			// 判断页面是否已选择
			String disSelect="";
			if(ysjl_item!=null){
				for(int j=0;j<ysjl_item.length;j++){
					if(record_item[i].equals(ysjl_item[j])){
						disSelect="checked";
						break;
					}
				}
			}
			item.setIs_disCheck(disSelect);			
			setList.add(item);
		}
		
		listMap.put("recordItems", setList);
		listMap.put("status", "add");
		//listMap.put("deviceSort", doc.getDevice_sort_code().substring(0, 1));
		listMap.put("success", true);
		return listMap;
    }
	
	/**
	 *  根据报告业务ID和报告模板ID获取原始记录目录信息（罐车）
	 * @param map -- 包含参数报告业务ID、报告模板ID
	 * 
	 * @return 
	 */
	public HashMap<String, Object> openGcCatalog(Map<String, Object> map) throws Exception {
		HashMap<String, Object> listMap = new HashMap<String, Object>();
		// 获取业务信息表
		InspectionInfo info = infoDao.get(map.get("id").toString());
		// 获取报告模板信息
		Report report = reportDao.get(map.get("report_id").toString());
		//String report_name = report.getReport_name();

		String[] record_item = report.getYsjl_pages().split(",");
		String[] ysjl_item = info.getYsjl_item().split(",");

		List<SetReportItem> setList = new ArrayList<SetReportItem>();
		// 配置页面显示
		for (int i = 0; i < record_item.length; i++) {
			SetReportItem item = new SetReportItem();
			item.setPage_index(record_item[i]);
			item.setIs_must("yes");

			if ("1".equals(record_item[i])) {
				item.setIndex_text("封面");
			} else if ("2".equals(record_item[i])) {
				item.setIndex_text("目录");
			} else if ("3".equals(record_item[i])) {
				item.setIndex_text("基本情况记录");
			} else if ("4".equals(record_item[i])) {
				item.setIndex_text("资料审查记录");
			} else if (Integer.parseInt(record_item[i]) > 4 && Integer.parseInt(record_item[i]) < 7) {
				item.setIndex_text("附页（" + (Integer.parseInt(record_item[i]) - 4) + "）");
			} else if ("7".equals(record_item[i])) {
				item.setIndex_text("壁厚测定记录");
				item.setIs_must("no");
			} else if ("8".equals(record_item[i])) {
				item.setIndex_text("盛水试漏试验记录");
				item.setIs_must("no");
			} 

			// 判断页面是否已选择
			String disSelect = "";
			if (ysjl_item != null) {
				for (int j = 0; j < ysjl_item.length; j++) {
					if (record_item[i].equals(ysjl_item[j])) {
						disSelect = "checked";
						break;
					}
				}
			}
			item.setIs_disCheck(disSelect);
			setList.add(item);
		}

		listMap.put("recordItems", setList);
		listMap.put("status", "add");
		// listMap.put("deviceSort", doc.getDevice_sort_code().substring(0, 1));
		listMap.put("success", true);
		return listMap;
	}
	
	/**
     *保存原始记录配置信息（电梯）
     *
     * @return
     * @throws Exception
     */
	public void saveItem(Map<String, Object> map) {
		try {
			String id = map.get("id").toString();
			String report_id = map.get("report_id").toString();
			List<ReportItem> reportItemList = reportItemService.queryByReportId(report_id);
			String report_item = "";
			// 遍历报告模板中的所有页面
			for (ReportItem reportItem : reportItemList) {
				if(report_item.length()>0){
					report_item += ",";
				}
				report_item += reportItem.getPage_index();
			}
			
			String record_item = map.get("record_item").toString();
			String xsqnum = map.get("xsqnum").toString();
			
			Report report = reportService.get(report_id);
			String report_code = report.getReport_code();
			String report_name = report.getReport_name();
			
			if("1".equals(xsqnum)){
				// 一个限速器，去掉最后1页
				if(!report_name.contains("九寨沟")){
					report_item = report_item.substring(0,report_item.length()-2);
				}
			}else if("0".equals(xsqnum)){				
				// 无限速器，去掉最后2页
				// 有机房01/02、无机房03/04、液压05/06、杂物09/10(因九寨沟杂物电梯由2号修改单改编过来无限速器故去掉杂物的判断)
				// 病床有机房18/19、病床无机房20/21
				// 定检/监检有限速器
				if(report_name.contains("九寨")){
					if ("01".equals(report_code)
							|| "02".equals(report_code)
							|| "03".equals(report_code)
							|| "04".equals(report_code)
							|| "05".equals(report_code)
							|| "06".equals(report_code)
							|| "18".equals(report_code)
							|| "19".equals(report_code)
							|| "20".equals(report_code)
							|| "21".equals(report_code)) {
						report_item = report_item.substring(0,
								report_item.length() - 2);	
					}	
				}else{
					if(!report_name.contains("2号修改单") && !report_name.contains("新疆")){
						if ("01".equals(report_code)
								|| "02".equals(report_code)
								|| "03".equals(report_code)
								|| "04".equals(report_code)
								|| "05".equals(report_code)
								|| "06".equals(report_code)
								|| "09".equals(report_code)
								|| "10".equals(report_code)
								|| "18".equals(report_code)
								|| "19".equals(report_code)
								|| "20".equals(report_code)
								|| "21".equals(report_code)) {
							report_item = report_item.substring(0,
									report_item.length() - 4);	
						}	
					}
				}
			}
			// 保存原始记录和报告目录页码
			reportItemRecordDao.createSQLQuery("update tzsb_inspection_info set report_item='"+report_item+"',ysjl_item='"+record_item+"',xsqts='"+xsqnum+"' where id='"+id+"'").executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	
	/**
     *保存原始记录配置信息（罐车）
     *
     * @return
     * @throws Exception
     */
	public void saveGcItem(Map<String, Object> map) {
		try {
			String id = map.get("id").toString();
			String report_id = map.get("report_id").toString();
			List<ReportItem> reportItemList = reportItemService.queryByReportId(report_id);
			
			String report_item = "";
			// 遍历报告模板中的所有页面
			for (ReportItem reportItem : reportItemList) {
				if("1".equals(reportItem.getIs_must())){
					if(report_item.length()>0){
						report_item += ",";
					}
					report_item += reportItem.getPage_index();
				}
			}
			
			String record_item = map.get("record_item").toString();
			String report_pages = report_item;
			if(record_item.indexOf("7")==-1){
				if(record_item.indexOf("8")!=-1){
					report_pages = report_item + ",9";
				}
			}else{
				if(record_item.indexOf("8")==-1){
					report_pages = report_item + ",8";
				}else{
					report_pages = report_item + ",8,9";
				}
			}
			
			if(record_item.indexOf("8")==-1){
				if(record_item.indexOf("7")!=-1){
					report_pages = report_item + ",8";
				}
			}else{
				if(record_item.indexOf("7")==-1){
					report_pages = report_item + ",9";
				}else{
					report_pages = report_item + ",8,9";
				}
			}
			// 保存原始记录和报告目录页码
			reportItemRecordDao.createSQLQuery("update tzsb_inspection_info set report_item='"+report_pages+"',ysjl_item='"+record_item+"' where id='"+id+"'").executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	
	/**
	 * 修改原始记录时，保存原始记录内容（电梯）
	 * 
	 * 
	 * @param PostBackValue
	 * @param report_id
	 * @param id
	 * @throws Exception 
	 * @author GaoYa
	 * @date 2017-04-20 17:17:00
	 */
	public void saveRecord(String[] PostBackValue, MREngine engine, HttpServletRequest request,
			HttpServletResponse response) throws KhntException {
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User user = (User) curUser.getSysUser();
		// Employee emp = (com.khnt.rbac.impl.bean.Employee) user.getEmployee();
		// Org org = TS_Util.getCurOrg(curUser);
		try {
			String ins_info_id = request.getParameter("id");
			String device_id = request.getParameter("device_id");
			String report_type = request.getParameter("report_id");
			
			// 根据业务信息ID获取业务信息bean
			InspectionInfo insInfo = infoDao.get(ins_info_id);
			// 获取检验类型
			String check_type = insInfo.getInspection().getCheck_type();
			insInfo.setCheck_category_code(check_type);	// 检验类型代码（1：制造监检 2：安改维监检 3：定检  4：委托检验......）	
			insInfo.setCheck_category_name(codeTablesDao.getValueByCode("BASE_CHECK_TYPE", check_type));	// 检验类型名称
			
			// 根据设备ID获取设备基础信息bean
			DeviceDocument devicedoc = deviceDao.get(device_id);
			
			// 根据报告ID获取报告基础信息bean
			Report report = reportDao.get(report_type);
			String report_name = report.getReport_name();
			
			// 为每个参数表设置一个字段名的集合
			Set<String> _docMap = new HashSet<String>();
			Set<String> _elevatorMap = new HashSet<String>();
			// 将bean字段放入相应集合
			//_docMap = beanFieldSet(DeviceDocument.class);
			_docMap = DeviceDocument.bean_to_set();
			_elevatorMap = beanFieldSet(ElevatorPara.class);
			
			// 定义设备类型标志位
			boolean b_elevator = false;
			// 定义设备单数bean
			ElevatorPara p_elevator = null;
			// 根据设备类型获得相应的参数表bean
			if (devicedoc.getDevice_sort_code().startsWith("3")) {
				Collection<ElevatorPara> c = devicedoc.getElevatorParas();
				for (ElevatorPara it : c) {
					p_elevator = elevatorParaDao.get(it.getId());
				}
				b_elevator = true;
			}

			int dtcs = 0;	 					// 电梯层数
			String inspection_date = ""; 		// 检验日期
			String last_inspection_date = ""; 	// 下次检验日期
			String inspection_conclusion = ""; 	// 检验结论
			String device_sort = "";			// 设备类型（二类）
			String device_sort_code = "";		// 设备类别（三类）
			String xsqts = insInfo.getXsqts();	// 限速器台数
			String device_use_place = "";	// 设备所在地（...路...号...栋）
			String device_place_road = "";	// 设备所在地（...路/街）
			String device_place_no = "";	// 设备所在地（...号）
			String device_place_num = "";	// 设备所在地（...栋）
			
			Date cur_date = DateUtil.convertStringToDate(Constant.defaultDatePattern, DateUtil.getCurrentDateTime());

			// 先删除tzsb_report_item_record表中之前保存的数据
			// 报告书编号不删除
			if (StringUtil.isNotEmpty(ins_info_id)) {
				this.delRecordItems(ins_info_id); // 删除原始记录
			}
			// 然后处理原始记录检验项目数据
			for (int i = 0; i < PostBackValue.length; i++) {
				// 如果为空则不处理
				if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
					// 将报检单位写入检验业务信息表
					if (PostBackValue[i].toUpperCase().equals("COM_NAME")) {
						insInfo.setReport_com_name(engine.getParameter(PostBackValue[i]));
					}
					// 将报检单位地址写入业务信息表和设备主表
					if (PostBackValue[i].toUpperCase().equals("COM_ADDRESS")) {
						insInfo.setReport_com_address(engine.getParameter(PostBackValue[i]));
						devicedoc.setUse_site_address(engine.getParameter(PostBackValue[i]));
					}
					// 将检验联系人、联系人电话写入业务信息表
					if (PostBackValue[i].toUpperCase().equals("CHECK_OP")) {
						insInfo.setCheck_op(engine.getParameter(PostBackValue[i]));
					}
					if (PostBackValue[i].toUpperCase().equals("SECURITY_OP")) {
						if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
							devicedoc.setSecurity_op(engine.getParameter(PostBackValue[i]));
						}
					}

					if (PostBackValue[i].toUpperCase().equals("DEVICE_CODE")) {
						if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
							devicedoc.setDevice_code(engine.getParameter(PostBackValue[i]));
						}
					}
					if (PostBackValue[i].toUpperCase().equals("CHECK_TEL")) {
						insInfo.setCheck_tel(engine.getParameter(PostBackValue[i]));
					}
					
					// 将维保单位、安装/施工单位、制造单位返写报告业务信息表
					if (PostBackValue[i].toUpperCase().equals("MAINTAIN_UNIT_NAME")) {
						insInfo.setMaintain_unit_name(engine.getParameter(PostBackValue[i]));
					}
					if (PostBackValue[i].toUpperCase().equals("MAKE_UNITS_NAME")) {
						insInfo.setMake_units_name(engine.getParameter(PostBackValue[i]));
					}
					if (PostBackValue[i].toUpperCase().equals("CONSTRUCTION_UNITS_NAME")) {
						insInfo.setConstruction_units_name(engine.getParameter(PostBackValue[i]));
					}
					
					// 将维保单位联系人、电话写入设备信息表
					if (PostBackValue[i].equals("MAINTENANCE_MAN")) {
						devicedoc.setMaintenance_man(engine.getParameter(PostBackValue[i]));
					}
					if (PostBackValue[i].toUpperCase().equals("MAINTENANCE_TEL")) {
						devicedoc.setMaintenance_tel(engine.getParameter(PostBackValue[i]));
					}

					// 检验结论写入到主表中和业务信息表
					if (PostBackValue[i].toUpperCase().equals("INSPECTION_CONCLUSION")) {
						devicedoc.setInspect_conclusion(engine.getParameter(PostBackValue[i]));
						insInfo.setInspection_conclusion(engine.getParameter(PostBackValue[i]));
						inspection_conclusion = engine.getParameter(PostBackValue[i]);// 获取检验结论
					}

					// 将电梯二维码编号写入业务信息表，报告签发后写入设备信息表
					if (PostBackValue[i].equals("DEVICE_QR_CODE")) {
						if (engine.getParameter(PostBackValue[i]) != null) {
							insInfo.setDevice_qr_code(engine.getParameter(PostBackValue[i]).trim());
						} else {
							insInfo.setDevice_qr_code("");
						}
					}
					
					// 将电梯使用登记证号写入业务信息表和设备信息表
					if (PostBackValue[i].equals("REGISTRATION_NUM")) {
						if (engine.getParameter(PostBackValue[i]) != null) {
							devicedoc.setRegistration_num(engine.getParameter(PostBackValue[i]).trim());
							insInfo.setRegistration_num(engine.getParameter(PostBackValue[i]).trim());
						} else {
							devicedoc.setRegistration_num("");
							insInfo.setRegistration_num("");
						}
					}

					// 如果安装日期不为空，则回写到设备信息表
					if (PostBackValue[i].toUpperCase().equals("INSTALL_FINISH_DATE")) {
						if (!engine.getParameter(PostBackValue[i]).equals("")) {
							String install_date = engine.getParameter(PostBackValue[i]);
							devicedoc.setInstall_finish_date(install_date);
						}
					}
					
					// 获取电梯层数
					if (PostBackValue[i].toUpperCase().equals("P30002005")) {
						try {
							dtcs = Integer.parseInt((String) engine.getParameter(PostBackValue[i].trim()));
						} catch (Exception e) {
							dtcs = 0;
						}
					}
					
					// 获取设备类型
					if(PostBackValue[i].toUpperCase().equals("DEVICE_SORT")){
						device_sort = engine.getParameter(PostBackValue[i]);
						devicedoc.setDevice_sort(device_sort);
					}
					
					// 获取设备类别
					if(PostBackValue[i].toUpperCase().equals("DEVICE_SORT_CODE")){
						device_sort_code = engine.getParameter(PostBackValue[i]);
						devicedoc.setDevice_sort_code(device_sort_code);
					}
					
					// 获取设备注册代码
					if(PostBackValue[i].toUpperCase().equals("DEVICE_REGISTRATION_CODE")){
						devicedoc.setDevice_registration_code(engine.getParameter(PostBackValue[i]));
					}
					
					// 九寨沟设备所在地之路（街）
					if ("DEVICE_PLACE_ROAD".equals(PostBackValue[i].toUpperCase())) {
						device_place_road = engine.getParameter(PostBackValue[i]);
					}
					// 九寨沟设备所在地之号
					if ("DEVICE_PLACE_NO".equals(PostBackValue[i].toUpperCase())) {
						device_place_no = engine.getParameter(PostBackValue[i]);
					}
					// 九寨沟设备所在地之栋
					if ("DEVICE_PLACE_NUM".equals(PostBackValue[i].toUpperCase())) {
						device_place_num = engine.getParameter(PostBackValue[i]);
					}

					// 如果检验时间不为空，则返写报告业务信息表
					// 并生成结论报告下次检验日期和限速器下次检验日期
					if (PostBackValue[i].toUpperCase().equals("INSPECTION_DATE")) {
						if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
							String check_date = engine.getParameter(PostBackValue[i]);
							if (check_date.contains("年")) {
								inspection_date = DateUtil
										.format(DateUtil.convertStringToDate("yyyy年MM月dd日", check_date), "yyyy-MM-dd");
							} else if (check_date.contains("/")) {
								inspection_date = DateUtil.format(
										dateformat.parse((check_date.replace("/", "-")).toString()),
										Constant.defaultDatePattern);
							} else if (check_date.contains(".")) {
								inspection_date = DateUtil.format(
										dateformat.parse((check_date.replace(".", "-")).toString()),
										Constant.defaultDatePattern);
							} else {
								inspection_date = check_date;
							}
							
							Date inspectionDate = DateUtil.convertStringToDate(Constant.defaultDatePattern, inspection_date);
							if(inspectionDate.after(cur_date)){
								// 检验日期不能晚于当天，如果晚于当天默认为当天
								inspectionDate = cur_date;
								inspection_date = DateUtil.format(cur_date, Constant.defaultDatePattern);
							}
							insInfo.setAdvance_time(inspectionDate);
							// 保存检验日期
							reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
									report_type, "INSPECTION_DATE", inspection_date, ins_info_id, user.getId(), user.getName());
							
							// 自动计算下次检验日期，下次检验日期=（检验日期 + 1年）- 1天
							if (inspectionDate != null) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(inspectionDate);
								calendar.add(Calendar.YEAR, 1);	
								calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
								last_inspection_date = DateUtil.format(calendar.getTime(), Constant.defaultDatePattern);
								insInfo.setLast_check_time(calendar.getTime());
								
								
							}
							// 自动生成检验结论报告下次检验日期
							reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
									report_type, "LAST_INSPECTION_DATE", last_inspection_date, ins_info_id, user.getId(), user.getName());
							
							// 自动生成限速器下次检验日期
							if(StringUtil.isNotEmpty(xsqts)){
								if(!"0".equals(xsqts)){
									if(StringUtil.isNotEmpty(last_inspection_date)){
										Calendar calendar = Calendar.getInstance();
										calendar.setTime(insInfo.getLast_check_time());
										// 限速器的下次检验日期，为结论页下次检验日期的基础上再翻1年
										calendar.add(Calendar.YEAR, 1);	
										String xsq_last_check_date = DateUtil.getDate(calendar
												.getTime());
										// 自动生成限速器下次检验日期
										reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
												report_type, "XSQ_LAST_INSPECTION_DATE", xsq_last_check_date, ins_info_id, user.getId(), user.getName());
									}
								}
							}
							continue;
						}
					}
					

					// 完成检验日期
					if(PostBackValue[i].toUpperCase().equals("INSPECTION_DATE_END")){
						if(StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))){
							String end_date = engine.getParameter(PostBackValue[i]);
							if (end_date.contains("年")) {
								end_date = DateUtil
										.format(DateUtil.convertStringToDate("yyyy年MM月dd日", end_date), "yyyy-MM-dd");
							} else if (end_date.contains("/")) {
								end_date = DateUtil.format(
										dateformat.parse((end_date.replace("/", "-")).toString()),
										Constant.defaultDatePattern);
							} else if (end_date.contains(".")) {
								end_date = DateUtil.format(
										dateformat.parse((end_date.replace(".", "-")).toString()),
										Constant.defaultDatePattern);
							}
							Date inspection_date_end = DateUtil.convertStringToDate(Constant.defaultDatePattern, end_date);
							if(inspection_date_end.after(cur_date)){
								inspection_date_end = cur_date;
								end_date = DateUtil.format(cur_date, Constant.defaultDatePattern);
							}
							// 保存完成检验日期
							reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
									report_type, "INSPECTION_DATE_END", end_date, ins_info_id, user.getId(), user.getName());
							continue;
						}
					}
					
					// 返写检验类别1
					if ("SGLB".equals(PostBackValue[i].toUpperCase())) {	// SGLB：施工类别（监检）
						if(device_sort.startsWith("4")){	// 4：起重机
							insInfo.setCheck_type_code(engine.getParameter(PostBackValue[i]));	// 检验类别代码（起重机监检：1：新装、2：移装、3：改造、4：重大修理）
							if("1".equals(engine.getParameter(PostBackValue[i]))){
								insInfo.setCheck_type_name("新装");
							}else if("2".equals(engine.getParameter(PostBackValue[i]))){
								insInfo.setCheck_type_name("移装");
							}else if("3".equals(engine.getParameter(PostBackValue[i]))){
								insInfo.setCheck_type_name("改造");
							}else if("4".equals(engine.getParameter(PostBackValue[i]))){
								insInfo.setCheck_type_name("重大修理");
							}
						}else if(device_sort.startsWith("3")){// 3：电梯（安装、改造、修理）
							insInfo.setCheck_type_code(engine.getParameter(PostBackValue[i]));	
							insInfo.setCheck_type_name(engine.getParameter(PostBackValue[i]));	// 检验类别名称（电梯监检：安装、改造、修理）
						}
					}

					// 返写检验类别2
					if ("CHECK_TYPE".equals(PostBackValue[i].toUpperCase())) {// CHECK_TYPE：检验类别（定期、首次）
						if(device_sort.startsWith("4")){	// 4：起重机
							insInfo.setCheck_type_code(engine.getParameter(PostBackValue[i]));
							insInfo.setCheck_type_name(engine.getParameter(PostBackValue[i]));
						}
					}

					if (PostBackValue[i].toUpperCase().equals("REPORT_SN")) {
						insInfo.setReport_sn(engine.getParameter(PostBackValue[i]));
						// 由于report_sn在原始记录上传的时候就已经生成在tsjy_report_item_record
						// 故此处不再进行insert操作，直接进入下一次循环
						continue;
					}

					if (PostBackValue[i].toUpperCase().indexOf("selectRow") != -1) {
						continue;
					}

					try {
						if (b_elevator && _elevatorMap.contains(PostBackValue[i].toUpperCase())) {
							String backValue = engine.getParameter(PostBackValue[i]).replaceAll("'", "’");
							// 判断是否为/ 如果是就便问空
							if (backValue.equals("/")) {
								backValue = "";
							}
							SetParaValue(p_elevator, PostBackValue[i].toLowerCase(), backValue);
						}
						if (_docMap.contains(PostBackValue[i].toUpperCase())) {
							String backValue = engine.getParameter(PostBackValue[i]).replaceAll("'", "’");
							SetParaValue(devicedoc, PostBackValue[i].toLowerCase(), backValue);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), report_type,
							PostBackValue[i].trim(), engine.getParameter(PostBackValue[i]), ins_info_id, user.getId(),
							user.getName());
					// 更新设备信息
					devicedoc = updateDeviceInfo(devicedoc, PostBackValue[i].trim(), engine.getParameter(PostBackValue[i]));
				}
			}
			
			// 因九寨沟的设备所在地拆分了路（街）、号、栋，此处拼接处理后返写设备所在地
			// report_version：4为九寨沟设备
			if(report_name.contains("九寨")){
				device_use_place = device_place_road.trim() + device_place_no.trim() + device_place_num.trim();
				devicedoc.setDevice_use_place(device_use_place);
			}
			// 返写设备信息，并生成设备注册代码
			updateInfos(devicedoc, report_type, ins_info_id, user.getId(), user.getName());

			
			// 报告是否录入 2 已经录入
			insInfo.setIs_report_input("2");
			insInfo.setIs_copy("0");	// 0 不是复制报告
			// 原始记录修改后，校核状态更新为待校核
			insInfo.setIs_report_confirm("0");	// 校核状态，默认为0（0：未校核 1：校核通过 2：校核未通过）
			insInfo.setYsjl_data_status("2");	// 原始记录数据状态（0：未生成报告 1：已生成报告 2：原始记录已修改未生成报告）
			insInfo.setIs_flow(null);

			// 编制/录入日期
			if (StringUtil.isNotEmpty(inspection_date)) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", inspection_date));
				calendar.add(Calendar.DATE, 1);
				Date draft_date = null;
				if (calendar.getTime().after(cur_date)) {
					draft_date = cur_date;
				} else {
					draft_date = calendar.getTime();
				}
				insInfo.setEnter_time(draft_date);
			}else{
				insInfo.setEnter_time(cur_date);
			}

			if (b_elevator) {
				if (p_elevator != null) {
					elevatorParaDao.save(p_elevator);
				}
			}

			// 计算电梯收费金额
			double dt_money = 0; // 不打折，也不四舍五入
			if (report_name.contains("病床")) {
				if (check_type.equals("3")) {// 定期检验
					// 判断是否有限速器
					if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
						// 判断电梯层数
						if (dtcs <= 5) {
							dt_money = (int) Math.round((500));
						} else {
							dt_money = (int) Math.round(500 + (dtcs - 5) * 50);
						}
					} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
						if (dtcs <= 5) {
							dt_money = (int) Math.round(500 * 1.3 + 200);
						} else {
							dt_money = (int) Math.round((500 + (dtcs - 5) * 50) * 1.3 + 200);
						}
					} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
						if (dtcs <= 5) {
							dt_money = (int) Math.round(500 * 1.3 + 400);
						} else {
							dt_money = (int) Math.round((500 + (dtcs - 5) * 50) * 1.3 + 400);
						}
					}
				} else if (check_type.equals("2")) {// 监督检验
					// 判断电梯层数
					if (dtcs <= 5) {
						dt_money = (int) Math.round(500 * 1.5);
					} else {
						dt_money = (int) Math.round((500 + (dtcs - 5) * 50) * 1.5);
					}
				}
			} else {
				if (check_type.equals("3")) {// 定期检验
					// 区分设备类型
					if (devicedoc.getDevice_sort().equals("3100") || devicedoc.getDevice_sort().equals("3200")
							|| devicedoc.getDevice_sort_code().equals("3410")
							|| devicedoc.getDevice_sort_code().equals("3420")) {
						// 判断是否有限速器
						if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
							// 判断电梯层数
							if (dtcs <= 5) {
								dt_money = 550;
							} else {
								dt_money = (550 + (dtcs - 5) * 55);
							}
						} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
							if (dtcs <= 5) {
								dt_money = 550 * 1.3 + 200;
							} else {
								dt_money = (550 + (dtcs - 5) * 55) * 1.3 + 200;
							}

						} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
							if (dtcs <= 5) {
								dt_money = 550 * 1.3 + 400;
							} else {
								dt_money = (550 + (dtcs - 5) * 55) * 1.3 + 400;
							}
						}
					} else if (devicedoc.getDevice_sort().equals("3300")) {
						dt_money = 400;
					} else if (devicedoc.getDevice_sort_code().equals("3430")) {
						if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
							dt_money = 400;
						} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
							dt_money = 400 + 200;
						} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
							dt_money = 400 + 200 + 200;
						}
					}
				} else if (check_type.equals("2")) {// 监督检验
					// 区分设备类型
					if (devicedoc.getDevice_sort_code().equals("3410")
							|| devicedoc.getDevice_sort_code().equals("3420")
							|| devicedoc.getDevice_sort().equals("3200")
							|| devicedoc.getDevice_sort().equals("3100")) {

						if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
							// 判断电梯层数

							if (dtcs <= 5) {
								dt_money = 550 * 1.5;
							} else {
								dt_money = (550 + (dtcs - 5) * 55) * 1.5;
							}

						} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
							if (dtcs <= 5) {
								dt_money = 550 * 1.5 + 200;
							} else {
								dt_money = (550 + (dtcs - 5) * 55) * 1.5 + 200;
							}
						} else if (insInfo.getXsqts().equals("2")) {// 勾选一个限速器
							if (dtcs <= 5) {
								dt_money = 550 * 1.5 + 400;
							} else {
								dt_money = (550 + (dtcs - 5) * 55) * 1.5 + 400;
							}
						}
					} else if (devicedoc.getDevice_sort().equals("3300")) {
						dt_money = 600;
					} else if (devicedoc.getDevice_sort_code().equals("3430")) {
						if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
							dt_money = 600;
						} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
							dt_money = 600 + 200;
						} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
							dt_money = 600 + 200 + 200;
						}
					}
				}
			
			}

			// 复检收费30%
			if (device_sort.startsWith("3")) {
				if (inspection_conclusion.equals("复检合格")) {
					dt_money = (double) Math
							.round(dt_money * 0.3);
				}
			}
			insInfo.setAdvance_fees(dt_money);

			// 返写报告业务信息
			insInfo.setLast_mdy_time(new Date());	// 最后修改时间
			infoDao.save(insInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改原始记录时，保存原始记录内容（罐车）
	 * 
	 * 
	 * @param PostBackValue
	 * @param report_id
	 * @param id
	 * @throws Exception 
	 * @author GaoYa
	 * @date 2018-04-09 14:39:00
	 */
	public void saveGcRecord(String[] PostBackValue, MREngine engine, HttpServletRequest request,
			HttpServletResponse response) throws KhntException {
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User user = (User) curUser.getSysUser();
		// Employee emp = (com.khnt.rbac.impl.bean.Employee) user.getEmployee();
		// Org org = TS_Util.getCurOrg(curUser);
		try {
			String ins_info_id = request.getParameter("id");
			String device_id = request.getParameter("device_id");
			String report_type = request.getParameter("report_id");
			
			// 根据业务信息ID获取业务信息bean
			InspectionInfo insInfo = infoDao.get(ins_info_id);
			// 获取检验类型
			String check_type = insInfo.getInspection().getCheck_type();
			insInfo.setCheck_category_code(check_type);	// 检验类型代码（1：制造监检 2：安改维监检 3：定检  4：委托检验......）	
			insInfo.setCheck_category_name(codeTablesDao.getValueByCode("BASE_CHECK_TYPE", check_type));	// 检验类型名称
			
			// 根据设备ID获取设备基础信息bean
			DeviceDocument devicedoc = deviceDao.get(device_id);
			
			// 为每个参数表设置一个字段名的集合
			Set<String> _docMap = new HashSet<String>();
			Set<String> _pressurevesselsMap = new HashSet<String>();
			// 将bean字段放入相应集合
			//_docMap = beanFieldSet(DeviceDocument.class);
			_docMap = DeviceDocument.bean_to_set();
			_pressurevesselsMap = beanFieldSet(PressurevesselsPara.class);
			
			// 定义设备类型标志位
			boolean b_pressurevessels = false;
			// 定义设备单数bean
			PressurevesselsPara p_pressurevessels = null;
			// 根据设备类型获得相应的参数表bean
			if (devicedoc.getDevice_sort_code().startsWith("26")) {
				Collection<PressurevesselsPara> paras = devicedoc.getPressurevessels();
				if(!paras.isEmpty()){
					for (PressurevesselsPara it : paras) {
						p_pressurevessels = pressurevesselsParaDao.get(it.getId());
					}
					b_pressurevessels = true;
				}else{
					p_pressurevessels = new PressurevesselsPara();
					p_pressurevessels.setId(null);
					p_pressurevessels.setDeviceDocument(devicedoc);
				}
			}

			String inspection_date = ""; 		// 检验日期
			String last_inspection_date = ""; 	// 下次检验日期

			Date cur_date = DateUtil.convertStringToDate(Constant.defaultDatePattern, DateUtil.getCurrentDateTime());

			// 先删除tzsb_report_item_record表中之前保存的数据
			// 报告书编号不删除
			if (StringUtil.isNotEmpty(ins_info_id)) {
				this.delRecordItems(ins_info_id); // 删除原始记录
			}
			// 然后处理原始记录检验项目数据
			for (int i = 0; i < PostBackValue.length; i++) {
				// 如果为空则不处理
				if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
					// 将报检单位写入检验业务信息表
					if (PostBackValue[i].toUpperCase().equals("COM_NAME")) {
						insInfo.setReport_com_name(engine.getParameter(PostBackValue[i]));
					}
					// 将报检单位地址写入业务信息表和设备主表
					if (PostBackValue[i].toUpperCase().equals("COM_ADDRESS")) {
						insInfo.setReport_com_address(engine.getParameter(PostBackValue[i]));
						devicedoc.setUse_site_address(engine.getParameter(PostBackValue[i]));
					}
					// 将检验联系人、联系人电话写入业务信息表
					if (PostBackValue[i].toUpperCase().equals("CHECK_OP")) {
						insInfo.setCheck_op(engine.getParameter(PostBackValue[i]));
					}
					
					// 将安全管理员写入业务信息表
					if (PostBackValue[i].toUpperCase().equals("SECURITY_OP")) {
						if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
							devicedoc.setSecurity_op(engine.getParameter(PostBackValue[i]));
						}
					}
					
					// 将安全管理员联系电话写入业务信息表
					if (PostBackValue[i].toUpperCase().equals("SECURITY_TEL")) {
						if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
							devicedoc.setSecurity_tel(engine.getParameter(PostBackValue[i]));
						}
					}
					
					// 获取车牌号
					if("LADLE_CAR_NUMBER".equals(PostBackValue[i].toUpperCase())){
						if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
							insInfo.setInternal_num(engine.getParameter(PostBackValue[i]));
							devicedoc.setInternal_num(engine.getParameter(PostBackValue[i]));
							p_pressurevessels.setLadle_car_number(engine.getParameter(PostBackValue[i]));
						}
						
					}
					// 获取发动机号
					if("LADLE_CAR_DOMAIN_NUM".equals(PostBackValue[i].toUpperCase())){
						if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
							p_pressurevessels.setLadle_car_domain_num(engine.getParameter(PostBackValue[i]));
						}
					}
					// 获取车辆型号
					if("LADLE_CAR_STRUCTURE".equals(PostBackValue[i].toUpperCase())){
						if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
							p_pressurevessels.setLadle_car_structure(engine.getParameter(PostBackValue[i]));
						}
					}
					// 获取车架号
					if("P20003002".equals(PostBackValue[i].toUpperCase())){
						if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
							p_pressurevessels.setP20003002(engine.getParameter(PostBackValue[i]));
						}
					}
					
					if (PostBackValue[i].toUpperCase().equals("DEVICE_CODE")) {
						if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
							devicedoc.setDevice_code(engine.getParameter(PostBackValue[i]));
						}
					}
					if (PostBackValue[i].toUpperCase().equals("CHECK_TEL")) {
						insInfo.setCheck_tel(engine.getParameter(PostBackValue[i]));
					}
					
					// 将维保单位、安装/施工单位、制造单位返写报告业务信息表
					if (PostBackValue[i].toUpperCase().equals("MAINTAIN_UNIT_NAME")) {
						insInfo.setMaintain_unit_name(engine.getParameter(PostBackValue[i]));
					}
					if (PostBackValue[i].toUpperCase().equals("MAKE_UNITS_NAME")) {
						insInfo.setMake_units_name(engine.getParameter(PostBackValue[i]));
					}
					if (PostBackValue[i].toUpperCase().equals("CONSTRUCTION_UNITS_NAME")) {
						insInfo.setConstruction_units_name(engine.getParameter(PostBackValue[i]));
					}
					
					// 将维保单位联系人、电话写入设备信息表
					if (PostBackValue[i].equals("MAINTENANCE_MAN")) {
						devicedoc.setMaintenance_man(engine.getParameter(PostBackValue[i]));
					}
					if (PostBackValue[i].toUpperCase().equals("MAINTENANCE_TEL")) {
						devicedoc.setMaintenance_tel(engine.getParameter(PostBackValue[i]));
					}

					// 检验结论写入到主表中和业务信息表
					if (PostBackValue[i].toUpperCase().equals("INSPECTION_CONCLUSION")) {
						devicedoc.setInspect_conclusion(engine.getParameter(PostBackValue[i]));
						insInfo.setInspection_conclusion(engine.getParameter(PostBackValue[i]));
					}
					
					// 将电梯使用登记证号写入业务信息表和设备信息表
					if (PostBackValue[i].equals("REGISTRATION_NUM")) {
						if (engine.getParameter(PostBackValue[i]) != null) {
							devicedoc.setRegistration_num(engine.getParameter(PostBackValue[i]).trim());
							insInfo.setRegistration_num(engine.getParameter(PostBackValue[i]).trim());
						} else {
							devicedoc.setRegistration_num("");
							insInfo.setRegistration_num("");
						}
					}

					// 如果安装日期不为空，则回写到设备信息表
					if (PostBackValue[i].toUpperCase().equals("INSTALL_FINISH_DATE")) {
						if (!engine.getParameter(PostBackValue[i]).equals("")) {
							String install_date = engine.getParameter(PostBackValue[i]);
							devicedoc.setInstall_finish_date(install_date);
						}
					}
					
					// 获取设备注册代码
					if(PostBackValue[i].toUpperCase().equals("DEVICE_REGISTRATION_CODE")){
						devicedoc.setDevice_registration_code(engine.getParameter(PostBackValue[i]));
					}

					// 如果检验时间不为空，则返写报告业务信息表
					// 并生成结论报告下次检验日期和限速器下次检验日期
					if (PostBackValue[i].toUpperCase().equals("INSPECTION_DATE")) {
						if (StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))) {
							String check_date = engine.getParameter(PostBackValue[i]);
							if (check_date.contains("年")) {
								inspection_date = DateUtil
										.format(DateUtil.convertStringToDate("yyyy年MM月dd日", check_date), "yyyy-MM-dd");
							} else if (check_date.contains("/")) {
								inspection_date = DateUtil.format(
										dateformat.parse((check_date.replace("/", "-")).toString()),
										Constant.defaultDatePattern);
							} else if (check_date.contains(".")) {
								inspection_date = DateUtil.format(
										dateformat.parse((check_date.replace(".", "-")).toString()),
										Constant.defaultDatePattern);
							} else {
								inspection_date = check_date;
							}
							
							Date inspectionDate = DateUtil.convertStringToDate(Constant.defaultDatePattern, inspection_date);
							if(inspectionDate.after(cur_date)){
								// 检验日期不能晚于当天，如果晚于当天默认为当天
								inspectionDate = cur_date;
								inspection_date = DateUtil.format(cur_date, Constant.defaultDatePattern);
							}
							insInfo.setAdvance_time(inspectionDate);
							// 保存检验日期
							reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
									report_type, "INSPECTION_DATE", inspection_date, ins_info_id, user.getId(), user.getName());
							
							// 自动计算下次检验日期，下次检验日期=（检验日期 + 1年）- 1天
							if (inspectionDate != null) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(inspectionDate);
								calendar.add(Calendar.YEAR, 1);	
								calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
								last_inspection_date = DateUtil.format(calendar.getTime(), Constant.defaultDatePattern);
								insInfo.setLast_check_time(calendar.getTime());
								
								
							}
							// 自动生成检验结论报告下次检验日期
							reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
									report_type, "LAST_INSPECTION_DATE", last_inspection_date, ins_info_id, user.getId(), user.getName());

							continue;
						}
					}
					

					// 完成检验日期
					if(PostBackValue[i].toUpperCase().equals("INSPECTION_DATE_END")){
						if(StringUtil.isNotEmpty(engine.getParameter(PostBackValue[i]))){
							String end_date = engine.getParameter(PostBackValue[i]);
							if (end_date.contains("年")) {
								end_date = DateUtil
										.format(DateUtil.convertStringToDate("yyyy年MM月dd日", end_date), "yyyy-MM-dd");
							} else if (end_date.contains("/")) {
								end_date = DateUtil.format(
										dateformat.parse((end_date.replace("/", "-")).toString()),
										Constant.defaultDatePattern);
							} else if (end_date.contains(".")) {
								end_date = DateUtil.format(
										dateformat.parse((end_date.replace(".", "-")).toString()),
										Constant.defaultDatePattern);
							}
							Date inspection_date_end = DateUtil.convertStringToDate(Constant.defaultDatePattern, end_date);
							if(inspection_date_end.after(cur_date)){
								inspection_date_end = cur_date;
								end_date = DateUtil.format(cur_date, Constant.defaultDatePattern);
							}
							// 保存完成检验日期
							reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
									report_type, "INSPECTION_DATE_END", end_date, ins_info_id, user.getId(), user.getName());
							continue;
						}
					}
					
					// 分项报告部位示意图处理
					if (PostBackValue[i].toUpperCase().contains("PICTURETEXT")
							&& !PostBackValue[i].toUpperCase().endsWith("P")) {
						// 保存完成检验日期
						reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), report_type,
								PostBackValue[i].toUpperCase(), engine.getParameter(PostBackValue[i]), ins_info_id,
								user.getId(), user.getName());
						continue;
					}

					// 返写检验类别2
					if ("GC_4_JYLB".equals(PostBackValue[i].toUpperCase())) {// CHECK_TYPE：检验类别（首次、年度、全面）
						insInfo.setCheck_type_code(engine.getParameter(PostBackValue[i]));
						insInfo.setCheck_type_name(engine.getParameter(PostBackValue[i]));
					}

					if (PostBackValue[i].toUpperCase().equals("REPORT_SN")) {
						insInfo.setReport_sn(engine.getParameter(PostBackValue[i]));
						// 由于report_sn在原始记录上传的时候就已经生成在tsjy_report_item_record
						// 故此处不再进行insert操作，直接进入下一次循环
						continue;
					}

					if (PostBackValue[i].toUpperCase().indexOf("selectRow") != -1) {
						continue;
					}

					try {
						if (b_pressurevessels && _pressurevesselsMap.contains(PostBackValue[i].toUpperCase())) {
							String backValue = engine.getParameter(PostBackValue[i]).replaceAll("'", "’");
							// 判断是否为/ 如果是就便问空
							if (backValue.equals("/")) {
								backValue = "";
							}
							SetParaValue(p_pressurevessels, PostBackValue[i].toLowerCase(), backValue);
						}
						if (_docMap.contains(PostBackValue[i].toUpperCase())) {
							String backValue = engine.getParameter(PostBackValue[i]).replaceAll("'", "’");
							SetParaValue(devicedoc, PostBackValue[i].toLowerCase(), backValue);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), report_type,
							PostBackValue[i].trim(), engine.getParameter(PostBackValue[i]), ins_info_id, user.getId(),
							user.getName());
					// 更新设备信息
					devicedoc = updateDeviceInfo(devicedoc, PostBackValue[i].trim(), engine.getParameter(PostBackValue[i]));
				}
			}

			// 返写设备信息，并生成设备注册代码
			updateInfos(devicedoc, report_type, ins_info_id, user.getId(), user.getName());

			
			// 报告是否录入 2 已经录入
			insInfo.setIs_report_input("2");
			insInfo.setIs_copy("0");	// 0 不是复制报告
			// 原始记录修改后，校核状态更新为待校核
			insInfo.setIs_report_confirm("0");	// 校核状态，默认为0（0：未校核 1：校核通过 2：校核未通过）
			insInfo.setYsjl_data_status("2");	// 原始记录数据状态（0：未生成报告 1：已生成报告 2：原始记录已修改未生成报告）
			insInfo.setIs_flow(null);

			// 编制/录入日期
			if (StringUtil.isNotEmpty(inspection_date)) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", inspection_date));
				calendar.add(Calendar.DATE, 1);
				Date draft_date = null;
				if (calendar.getTime().after(cur_date)) {
					draft_date = cur_date;
				} else {
					draft_date = calendar.getTime();
				}
				insInfo.setEnter_time(draft_date);
			}else{
				insInfo.setEnter_time(cur_date);
			}

			if (b_pressurevessels) {
				if (p_pressurevessels != null) {
					pressurevesselsParaDao.save(p_pressurevessels);
				}
			}

			// 返写报告业务信息
			insInfo.setLast_mdy_time(new Date());	// 最后修改时间
			infoDao.save(insInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新原始记录检验项目参数值
	 * 
	 * @param info_id
	 *            -- 报告业务ID
	 * @param report_type
	 *            -- 报告模板类型ID
	 * @param item_name
	 *            -- 检验项目name
	 * @param item_value
	 *            -- 报检验项目value
	 * @return
	 * @author GaoYa
	 * @date 2017-04-25 上午09:51:00
	 */
	public void updateItemValue(String info_id, String report_type, String item_name, String item_value) {
		try {
			reportItemRecordDao.createSQLQuery("update  tzsb_report_item_record set item_value='" + item_value
					+ "'where fk_report_id='" + report_type + "' and item_name='" + item_name
					+ "' and fk_inspection_info_id='" + info_id + "'").executeUpdate();
		} catch (Exception e) {
			log.debug(e.toString());
			e.printStackTrace();
		}
	}
	
	private Set<String> beanFieldSet(Class clazz)throws Exception {
		Set<String> set = new HashSet<String>();
		Field[] field =  clazz.getDeclaredFields();
		for(Field fd : field) {
			if(fd.getName().equals("serialVersionUID"))
				continue ;
			set.add(fd.getName().toUpperCase());
		}
		return set ;
	}
	
	private void SetParaValue(BaseEntity obj ,String key , String value) 
			throws Exception {
		Method method = null;
		if (key.equals("first_install_date") || key.equals("remake_date") || key.equals("repair_date")
				|| key.equals("install_finish_date")|| key.equals("use_date")) {
			method = obj.getClass().getMethod("set" + changFisrtUpper(key), Date.class);
			SimpleDateFormat far = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			if (method != null) {
				if (StringUtil.isNotEmpty(value)) {
					date = far.parse(value);
				}
				method.invoke(obj, date);
			}
		} else {
			method = obj.getClass().getMethod("set" + changFisrtUpper(key), String.class);

			if (method != null) {
				method.invoke(obj, value);
			}
		}
	}
	
	private static String changFisrtUpper(String str){
		return str.substring(0, 1).toUpperCase()
				+ str.substring(1, str.length());
	}

	/**
	 * 查询原始记录内容
	 * 
	 * @param info_id
	 *            -- 报告业务ID
	 * @return
	 * @author GaoYa
	 * @date 2017-04-29 下午17:17:00
	 */
	public Map<String, String> queryRecordByInfoId(String info_id) {
		Map<String, String> newMap = new HashMap<String, String>();
		String sql = "select item_name,item_value from tzsb_report_item_record  where fk_inspection_info_id = '" + info_id + "' and data_status<>'99' and item_name<>'REPORT_SN'";
		try {
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String key = "";
			String value = "";
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					String column_name = rsmd.getColumnName(i);
					String column_value = rs.getString(column_name);
					if("ITEM_NAME".equals(column_name)){
						key = column_value;
					}if("ITEM_VALUE".equals(column_name)){
						value = column_value;
					}
				}
				newMap.put(key, value);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		closeConn();
		return newMap;
	}
	
	/**
	 * 移动端校验原始记录是否已经提交
	 * @param json
	 * @param map 
	 * @param request 
	 * @return 
	 * @throws KhntException
	 * @throws ParseException 
	 */
	public HashMap<String, Object> check_Ysjls(HashMap<String, Object> map,
			HttpServletRequest request) throws KhntException, ParseException{
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		String check_codes = request.getParameter("check_codes");
		JSONArray listdatas = JSONArray.fromObject(check_codes);
		
		for (int i = 0; i < listdatas.length(); i++) {
			Object reportItemObject = listdatas.get(i);
			JSONObject itemValue = JSONObject.fromObject(reportItemObject.toString());
			String sid = itemValue.getString("sid");
			String random_code = itemValue.getString("random_code");

			InspectionInfo info = inspectionInfoService.getInfoByRandomCode(random_code);
			if(info != null){
				HashMap<String, Object> object_map = new HashMap<String, Object>();
				object_map.put("sid", sid);
				object_map.put("info_id", info.getId());
				mapList.add(object_map);
			}
		}
		
		map.put("success", true);
		map.put("data", mapList);	// 返回业务检验信息ID
		return map;
	}
	
	
	/**
	 * 获取原始记录图片信息
	 * @param id -- 业务id

	 * @return 
	 * @throws Exception 
	 * @author GaoYa
	 * @since
	 */
	public Map<String, Object> getPicFromRecord(String id) throws Exception {
		Map<String, Object> picMap = new HashMap<String, Object>();

		// 根据item_value内容获取相应图片 begin
		StringBuffer picSql = new StringBuffer();
		// picSql.append("from ReportPicValue p,ReportItemValue t ");
		// picSql.append("where p.id = t.item_value and p.business_id =
		// t.fk_inspection_info_id and p.business_id=? and upper(t.item_name)
		// like 'PICTURETEXT%'");
		picSql.append("from ReportPicValue p,ReportItemRecord t ");
		picSql.append(" where p.id = t.item_value and t.data_status <> '99'");
		picSql.append(" and t.fk_inspection_info_id=? ");
		picSql.append(" and upper(t.item_name) like 'PICTURETEXT%'");
		List list = reportItemValueDao.createQuery(picSql.toString(), new String[] { id }).list();

		for (int i = 0; i < list.size(); i++) {
			Object o[] = (Object[]) list.get(i);
			ReportPicValue picv = (ReportPicValue) o[0];
			ReportItemRecord ival = (ReportItemRecord) o[1];
			ByteArrayOutputStream sout = new ByteArrayOutputStream();
			sout.write(picv.getPic_blob());
			// picMap.put(picv.getItem_name()+"P", sout.toByteArray());
			picMap.put(ival.getItem_name() + "P", picv.getPic_blob());
			picMap.put(ival.getItem_name() + "C", picv.getPic_clob());
			System.out.println("-------------------------:" + sout.toByteArray());
		}

		return picMap;
	}

	// 获取数据库连接
	public Connection getConn() {
		try {
			conn = Factory.getDB().getConnetion();
		} catch (Exception e) {
			logger.error("获取数据库连接失败：" + e.getMessage());
			e.printStackTrace();
		}
		return conn;
	}

	// 关闭连接
	public void closeConn() {
		try {
			/*
			 * if (null != rs) rs.close(); if (null != pstmt) pstmt.close(); if
			 * (null != conn) conn.close();
			 */
			Factory.getDB().freeConnetion(conn); // 释放连接
		} catch (Exception e) {
			logger.error("关闭数据库连接错误：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 获取用户信息
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		return userDao.get(id);
	}
	/**
	 * 待处理任务数量
	 * author pingZhou
	 * @param request
	 * @return
	 */
	public List<HashMap<String, Object>> allDealCount(HttpServletRequest request) {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
		String userId = ((User)user.getSysUser()).getEmployee()==null?user.getId():((User)user.getSysUser()).getEmployee().getId();
		HashMap<String, Object> map11 = new HashMap<String, Object>(); 
		//科室报检
		int moKsrw = reportItemRecordDao.getUnitReceiveCount(userId);
		map11.put("code", "mo_ksrw");
		map11.put("value", moKsrw);
		list.add(map11);
		//转移录入
		HashMap<String, Object> map12 = new HashMap<String, Object>(); 
		int moYsjllr = reportItemRecordDao.getOtherReceiveCount(userId);
		map12.put("code", "mo_ysjllr");
		map12.put("value", moYsjllr);
		list.add(map12);
		
		//退回录入
		HashMap<String, Object> map13 = new HashMap<String, Object>(); 
		int backRecord = reportItemRecordDao.getBackReceiveCount(userId);
		map13.put("code", "back_record");
		map13.put("value", backRecord);
		list.add(map13);
		
		//任务接收 = 科室报检+转移录入+退回录入
		HashMap<String, Object> map1 = new HashMap<String, Object>(); 
		//int needReceive = reportItemRecordDao.getNeedReceiveCount(user.getId());
		map1.put("code", "need_receive");
		map1.put("value", moKsrw+moYsjllr+backRecord);
		list.add(map1);
		
		//原始记录校核
		HashMap<String, Object> map2 = new HashMap<String, Object>(); 
		int recordConfirm = reportItemRecordDao.getRecordConfirmCount(userId);
		map2.put("code", "record_confirm");
		map2.put("value", recordConfirm);
		list.add(map2);
		//报告审核
		HashMap<String, Object> map3 = new HashMap<String, Object>(); 
		int reportAudit = reportItemRecordDao.getReportAuditCount(user.getId());
		map3.put("code", "report_audit");
		map3.put("value", reportAudit);
		list.add(map3);
				
		//报告签发
		HashMap<String, Object> map4 = new HashMap<String, Object>(); 
		int reportSign = reportItemRecordDao.getReportSignCount(user.getId());
		map4.put("code", "report_sign");
		map4.put("value", reportSign);
		list.add(map4);
		
		//大厅任务数量
		HashMap<String, Object> map5 = new HashMap<String, Object>(); 
		int hallMession = reportItemRecordDao.getHallMessionCount(user.getSysUser().getOrg().getId());
		map5.put("code", "mo_dtrw");
		map5.put("value", hallMession);
		list.add(map5);
						
		return list;
	}

	//以下是移动端代码
	
	/**
	 * 保存的页码
	 * author pingZhou
	 * @param map
	 * @param pages
	 * @param infoId 业务id
	 * @throws Exception
	 */
	public InspectionInfo saveMap(HttpServletRequest request,net.sf.json.JSONObject object,InspectionInfo info) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		Employee emp = ((User)user.getSysUser()).getEmployee();
		Map<String, String> imgIdMap = new HashMap<String, String>();
		//页码信息
		String pages = object.getString("page");
		if(StringUtil.isEmpty(pages)) {
			throw new Exception("为获取提交的页码！");
		}
		//业务id
		String infoId = object.getString("infoId");
		//原始记录业务信息，新增info信息时需要取record的信息
		JSONObject record = object.getJSONObject("record");
		if(StringUtil.isEmpty(infoId)||"null".equals(infoId)) {
			infoId = record.getString("fkInspectInfoId");
		}
		//业务info
		if(info == null) {
			if(StringUtil.isNotEmpty(infoId) && !"null".equals(infoId)) {
				//查询业务数据
				info = infoDao.get(infoId);
			}else {
				//infoId 为空则新建一条业务数据
				info = new InspectionInfo();
			}
		}
		
		//原始记录模板
		JSONObject recordM = object.getJSONObject("recordM");
		//判断模板是否存在
		String fk_report_id = StringUtil.isEmpty(info.getReport_type())?recordM.getString("id"):info.getReport_type();
		if(StringUtil.isEmpty(fk_report_id)) {
			throw new Exception("找不到关联的报表ID...");
		}
		//原始记录目录信息
		JSONArray recordDir = object.getJSONArray("recordDir");
		if(recordDir==null) {
			throw new KhntException("没有提交目录信息！");
		}
		//原始记录录入数据信息
		JSONArray list = object.getJSONArray("list");
		//将录入信息转成map
		Map<String, RtExportData> map = RtSaveAsst.transToMap(list, RtPageType.TABLE);
		
		//保存附件图片
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> files = multipartRequest.getFileMap();
			if (files.size() > 0) {
				
				for (String pic_name : files.keySet()) {  
				    Attachment attachment = new Attachment();
				    CommonsMultipartFile file = (CommonsMultipartFile) files.get(pic_name);
					attachment.setFileSize(file.getSize());
					attachment.setFileType("image/jpeg");
					attachment.setFileName(pic_name);
					System.out.println("--key----"+pic_name+"--filename----"+file.getOriginalFilename());
					saveAttachment(file.getInputStream(),attachment,"desk",false);
					int l = pic_name.lastIndexOf(".");
					imgIdMap.put(pic_name.substring(0, l), attachment.getId());
				}  
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//throw new KhntException("图片信息保存出错！");
		}
		//删除数据
		if(StringUtil.isNotEmpty(info.getId())) {
			//取出所有需要保存内容
			List<String> nameList = new ArrayList<String>();
			
			for (String name : map.keySet()) {
				if(name.lastIndexOf("__")!=-1) {
					name = name.substring(0,name.lastIndexOf("__"));
				}
				nameList.add(name);
			}
			String names = String.join("','", nameList.toArray(new String[nameList.size()]));
	
			String pagess = pages.replace(",", "','");
			
			String hql = "update ReportItemRecord set data_status='99' where  item_name in ('"+names+"') and fk_inspection_info_id='" + info.getId() + "' and page_no in ('"+pagess+"')";
			
			this.reportItemRecordDao.createQuery(hql).executeUpdate();
			
			this.rtPageManager.delFuncDataBacth(info.getId(),names,pages);
		}else {
			//直接缓存设备检验
			String baseDeviceDocumentId = record.getString("fkDeviceId");
			if(StringUtil.isEmpty(baseDeviceDocumentId)||"null".equals(baseDeviceDocumentId)) {
				throw new Exception("未提交原始记录关联设备！");
			}
			DeviceDocument ddoc = deviceDao.get(baseDeviceDocumentId);
			EnterInfo company =  ddoc.getEnterInfo();
			if (company == null) {
				company = enterService.get(ddoc.getFk_company_info_use_id());
			}
			
			Inspection inspection = new Inspection();
			inspection.setCheck_type(record.getString("checkType"));	// 检验类别（3：定检 2：监检）
			inspection.setFk_unit_id(record.getString("fkComId"));	// 报检单位ID
			inspection.setCom_name(record.getString("comName"));		// 报检单位名称
			inspection.setEnter_op_id(emp.getId());
			inspection.setEnter_op(emp.getName());	// 录入人员
			inspection.setInspection_time(Timestamp.valueOf(timeformat.format(new Date())));
			inspection.setData_status("0");			// 0：新建 1:使用 2：删除
			inspectionDao.save(inspection);
			
			info.setInspection(inspection);
			//设备id
			info.setFk_tsjc_device_document_id(ddoc.getId());
			//验证设备使用登记证号是否重复
			if(StringUtil.isNotEmpty(ddoc.getRegistration_num()))
			{
				//validateRegistrationNumDuplicate(ddoc);
				info.setRegistration_num(ddoc.getRegistration_num());
			}
			//验证设备二维码重复
			if(StringUtil.isNotEmpty(ddoc.getDevice_qr_code()))
			{
				//validateDeviceQrCodeDuplicate(ddoc);
				info.setDevice_qr_code(ddoc.getDevice_qr_code());
			}
			//设备注册代码
			info.setDevice_registration_code(record.getString("deviceRegistrationCode"));
			// 检验类别（3：定检 2：监检）
			info.setCheck_category_code(record.getString("checkType"));
			info.setCheck_category_name(record.getString("checkTypeName"));
			//report.id
			info.setReport_type(record.getString("fkModelId"));
			
			//参检人员
			info.setCheck_op_id(record.getString("checkOpId"));
			info.setCheck_op_name(record.getString("checkOp"));
			//项目负责人
			info.setItem_op_id(record.getString("itemOpId"));
			info.setItem_op_name(record.getString("itemOp"));
			// 检验部门ID
			info.setCheck_unit_id(user.getSysUser().getOrg().getId());	
			// 录入部门ID
			info.setEnter_unit_id(user.getSysUser().getOrg().getId());	
			//提交校核时已经赋值为校核人员
			if(StringUtil.isEmpty(info.getRecordHandleId())) {
				info.setRecordHandleId(record.getString("handleId"));
			}
			if(StringUtil.isEmpty(info.getRecordHandleOp())) {
				info.setRecordHandleOp(record.getString("handleOp"));
			}
			//报检单位名称
			info.setReport_com_name(record.getString("comName"));
			if(company!=null) {
				info.setCheck_op(company.getCom_contact_man()); // 检验联系人
				info.setCheck_tel(company.getCom_tel()); // 检验联系电话
				info.setReport_com_address(company.getCom_address()); // 使用单位地址
			}
			// 原始记录是否已经校核，默认为0（0：未校核 1：校核通过 2：校核未通过）	
			info.setIs_report_confirm("0");	
			info.setIs_back("1");		// 默认的状态为1，提交后变成0，退回也变成1
			info.setIs_copy("0");		// 是否是复制报告（0：非复制报告 1：复制报告）
			info.setIs_mobile("1");		// 是否移动检验（0：否 1：是）
			info.setIs_flow("0");//是否启动流程		
			// 未启动流程前，业务数据状态都为初始状态不进入正常使用流程
			info.setData_status("0");		// 报告数据状态（0：初始（未生成报告） 1：正常 99：删除）
			info.setFee_status("0");		// 收费状态（0：初始）未启动流程前，收费状态都为初始状态不进入待收费流程
			info.setYsjl_data_status("0");	// 原始记录数据状态（0：未生成报告 1：已生成报告 2：原始记录已修改未生成报告）
			info.setLast_mdy_time(new Date());
			info.setIs_print_ysjl("0");//原始记录打印状态（0：未打印 1：已打印）
			//创建日期
			info.setCreate_time(new Date());
			//检验日期inspectDate
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(record.getString("inspectDate"));
			}catch(ParseException e) {
				e.printStackTrace();
				date = new Date();
			}
			info.setAdvance_time(date);
			// 自动计算
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.YEAR, 1);	
			//下次检验日期，下次检验日期=（检验日期 + 1年）- 1天
			if(StringUtil.isNotEmpty(ddoc.getDevice_sort())){
				if (!ddoc.getDevice_sort().startsWith("9")) {
					//	客运索道（device_sort_code为9开头的4位数字）报告只显示到年月，不需要精确到年月日，故此处不用减去一天
					calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
				}
			}else{
				// 暂无设备的情况，默认下次检验日期=（检验日期 + 1年）- 1天
				calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
			}
			info.setLast_check_time(calendar.getTime());
			//编辑日期
			// 自动计算录入/编制日期（编制日期=检验日期+1天）
			Date cur_date = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.getCurrentDateTime());
			if(ddoc.getDevice_sort().startsWith("3") || ddoc.getDevice_sort().startsWith("4") || ddoc.getDevice_sort().startsWith("6") || ddoc.getDevice_sort().startsWith("9")){
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(info.getAdvance_time());
				calendar2.add(Calendar.DATE, 1);	
				// 检验日期为当天的，编制日期不再+1，默认为当天
				Date draft_date = null;
				if(calendar2.getTime().after(cur_date)){
					draft_date = cur_date;
				}else{
					draft_date = calendar.getTime();
				}
				info.setEnter_time(draft_date);
			}else{
				info.setEnter_time(new Date());		// 录入日期
			}
			//验证60天内是否重复生成报告
			validateInfoDuplicate(ddoc,info);  
			//生成业务流水号
    		info.setSn(reportService.getSn(1));
			infoDao.save(info);
		}

		//保存录入信息
		RtSaveAsst asst = new RtSaveAsst();
		
		for (String key : map.keySet()) {
			RtExportData rtExportData = map.get(key);
			String itemName = rtExportData.getName();
			
			String markContent = rtExportData.getMarkContent();
			boolean flagm = !StringUtil.isEmpty(markContent)&&!"null".equals(markContent)&&markContent!=null ;
			if ((rtExportData.getValue() != null && StringUtil.isNotEmpty(rtExportData.getValue()))
					||flagm) {
				ReportItemRecord item = new ReportItemRecord();
				
				String value = rtExportData.getValue();
				if(imgIdMap.containsKey("base__"+itemName)||imgIdMap.containsKey(itemName)) {
					if(imgIdMap.containsKey("base__"+itemName)) {
						value = imgIdMap.get("base__"+itemName);
					}else if(imgIdMap.containsKey(itemName)) {
						value = imgIdMap.get(itemName);
					}
				}
				
				item.setItem_name(rtExportData.getName());
				item.setItem_value(value);
				if(rtExportData.getImage()!=null&&!"null".equals(rtExportData.getImage())&&StringUtil.isNotEmpty(rtExportData.getImage())) {
					item.setImage(rtExportData.getImage());
				}
				
				item.setFk_report_id(fk_report_id);
				item.setFk_inspection_info_id(infoId);
				item.setLast_mdy_uid(user.getId());
				item.setLast_mdy_uname(user.getName());//MdyUname();
				item.setLast_mdy_time(timeformat.format(new Date()));//MdyTime();
				item.setData_status("0");//Status("0");
				item.setPage_no(rtExportData.getPageNo());
				
			
				this.save(item);
			}
			
			asst.setFkBusinessId(infoId);
			asst.setRtPageDao(this.rtPageDao);
			asst.execute(rtExportData, null, null,null);
		}
		
		info.setEnter_op_id(emp.getId());
		info.setEnter_op_name(emp.getName());
		//实际编辑日期
		info.setEnter_time2(new Date());
		info.setIs_input("1");
		
		if(info.getRecordPageCode()==null||StringUtil.isEmpty(info.getRecordPageCode())){
			
			info.setRecordPageCode(pages);
			
			info.setRecordEnterId(emp.getId());
			info.setRecordEnterOp(emp.getName());
			info.setRecordEnterTime(new Date());

		}else{
			String pageN = infoDao.createHasSavePage(info.getRecordPageCode(), pages);
			
			info.setRecordPageCode(pageN);
			if(!info.getRecordEnterId().contains(emp.getId())) {
				//多人录入
				info.setRecordEnterId(info.getRecordEnterId()+","+emp.getId());
				info.setRecordEnterOp(info.getRecordEnterOp()+","+emp.getName());
			}
		}
		
		infoDao.save(info);
		
		//处理目录信息
		recordDirService.changeDir(info.getId(), recordDir);
		//记录提交日志
		recordLogService.setLogs(info.getId(), user.getName(), "保存原始记录到服务器",
				user.getId(), user.getName(), new Date(), TS_Util.getIpAddress(request), "保存",null,null);
		
		// 原始记录转换成报告
		parseManager.dealRecordToReport(request, info.getId());

		/*// 处理报告书编号
		if(StringUtil.isEmpty(info.getReport_sn())) {
			reportItemValueService.saveReportSn(info);
		}*/
							
		// 更新原始记录转换报告状态
		parseManager.updateRecordStatus(info);	
		return info;
	}
	
	
	
	
	
	/**
	 * 保存附件
	 * 
	 * @param inputStream
	 *            文件流
	 * @param attachment
	 *            附件信息BEAN
	 * @param saveType
	 *            存储类别，数据库/文件系统，参考AttachmentManager.SAVE_TYPE_DISK,
	 *            AttachmentManager.SAVE_TYPE_DB
	 * @param saveDB
	 *            是否往数据库写入附件信息，此项只在存储类型为文件系统时有效
	 * @throws Exception
	 */
	public synchronized HashMap<String, Object> saveAttachment(InputStream inputStream, Attachment attachment, String saveType, boolean saveDB)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String attType;
				attType = "0";
			
			attachment.setSaveType(attType);// 设置存储类别
			attachment.setUploadTime(Calendar.getInstance().getTime());// 上传时间设置
			boolean isExist = StringUtil.isNotEmpty(attachment.getId());
			String realPath = null;// 文件路径
			String existPath = null;
			String dates = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String newName = dates+"_"+attachment.getFileName();
			// 将文件存储到磁盘
			if ("desk".equals(saveType)) {
				
				if ("relative".equals(Factory.getSysPara().getProperty("attachmentPathType", "relative"))) // 相对路径
					realPath = Factory.getWebRoot() + "upload/record";
				/*else
					realPath = attachmentPath;// 绝对路径
*/
				
				attachment.setFilePath("/record/"+newName);
				if (isExist) {
					Attachment currentAttach = this.attachmentDao.get(attachment.getId());
					existPath = currentAttach.getFilePath();
					BeanUtils.copyProperties(attachment, currentAttach);
				}
				
					// 将附件信息持久化
					this.attachmentDao.save(attachment);
					
					File file = new File(realPath);
					if(!file.exists()) {
						file.mkdirs();
					}
							
				byte[] data = new byte[inputStream.available()];
				inputStream.read(data);
				FileUtil.writeFile(realPath + File.separator + newName, data);// 写入文件系统

				// 删除之前的文件
				if (StringUtil.isNotEmpty(existPath))
					FileUtil.delAllFile(realPath + File.separator + existPath);
			}
			// 将文件存储到DB
			else if (saveDB) {
				if (isExist)// 如果已经存在，先删除
					this.deleteAttach(attachment);
				this.attachmentDao.saveAttachToDB(attachment, inputStream);// 写入数据库
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "图片上传失败！错误代码："+e.toString());
			return map;
		}
		
		map.put("success", true);
		return map;
	}
	
	/**
	 * 根据ID、文件相对路径删除文件。支持多文件删除(ids,path使用分号;分隔)
	 */
	public void deleteAttach(String ids, String path) throws Exception {
		// 按照指定ID删除文件
		if (StringUtil.isNotEmpty(ids)) {
			for (String id : ids.split(";")) {
				Attachment attachment = attachmentDao.get(id);
				this.deleteAttach(attachment);
			}
		}

		// 删除指定路径的文件
		if (StringUtil.isNotEmpty(path)) {
			String realPath="";// 文件路径
			if ("relative".equals(Factory.getSysPara().getProperty("attachmentPathType", "relative"))) // 相对路径
				realPath = Factory.getWebRoot() + "upload/record";

			for (String pstr : path.split(";")) {
				realPath += File.separator + pstr;
				FileUtil.delAllFile(realPath);
			}
		}
	}


	/**
	 * 删除附件对象，如果有文件也同时删除
	 * 
	 * @param attachment
	 * @param realPath
	 * @throws Exception
	 */
	public void deleteAttach(Attachment attachment) throws Exception {
		if (attachment == null)
			return;
		if (attachment.getSaveType().equals("desk")) {// 存储于磁盘，需要删除磁盘文件
			String realPath = null;// 文件路径
			if ("relative".equals(Factory.getSysPara().getProperty("attachmentPathType", "relative"))) // 相对路径
				realPath = Factory.getWebRoot() + "upload/record";

			realPath += File.separator + attachment.getFilePath();
			FileUtil.delAllFile(realPath);
		}

		this.attachmentDao.removeById(attachment.getId());// 从数据删除附件记录
	}
	
	
	/**
	 * 移动端获取部门人员信息
	 * @param json
	 * @param map 
	 * @param request 
	 * @return 
	 * @throws KhntException
	 * @throws ParseException 
	 */
	public List<Map<String,Object>> getCheckUsersList(String orgId) throws ParseException{
		return reportItemRecordDao.queryUsersByOrgId(orgId);
	}



	public InspectionInfo saveInspToOther(HttpServletRequest request, JSONObject object) throws Exception {
		if(object==null){
			throw new KhntException("请传入参数！");
		}

		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		
		String nextOpId = object.getString("nextOpId");
		String nextOp = object.getString("nextOp");
		
		if(!object.has("nextOpId")||object.get("nextOpId")==null){
			throw new KhntException( "请选择下一个录入人员！");
		}
		//保存数据信息
		InspectionInfo info = saveMap(request, object,null);
		
		info.setRecordHandleId(nextOpId);
		info.setRecordHandleOp(nextOp);
		info.setRecordFlow("10");
		info.setReceiveStatus("0");
		infoDao.save(info);
		
		//记录提交日志
		recordLogService.setLogs(info.getId(), user.getName(), "转移原始记录"+nextOp+"录入",
				user.getId(), user.getName(), new Date(), TS_Util.getIpAddress(request), "转移录入",nextOpId,nextOp);
		
		return info;
		
	}


	/**
	 * 查询检验原始记录信息
	 * author pingZhou
	 * @param map
	 * @param request
	 * @param infoId
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getInspData(HashMap<String, Object> map, HttpServletRequest request, String infoId) throws Exception {
		
		if(map==null) {
			map = new HashMap<String, Object>();
		}
		
		//CurrentSessionUser user = SecurityUtil.getSecurityUser();
		//baseInfo参数用于是否只查询原始记录信息，不要录入数据
		String baseInfo  = request.getParameter("baseInfo");
		//保存数据信息
		InspectionInfo info = infoDao.get(infoId);
		if(info==null) {
			throw new KhntException("没有取到数据，请检查传入的参数！");
			
		}
		//业务记录信息
		map.put("record", info);
	
		
		//目录信息
		List<InspectRecordDir> dirList =  recordDirDao.getDirByBid(infoId);
		map.put("dirList", dirList);
		
		Report reports = reportsDao.get(info.getReport_type());
		map.put("recordCode", reports.getRecordModelCode());
		
		if(baseInfo==null||"false".equals(baseInfo)) {
			//不是查询基础信息，则查询所有信息
			//录入数据信息
			List<Map<String, String>> dataList = detailMap(request,infoId,null);
			
			map.put("dataList", dataList);
			//模板信息
			if(info.getReport_type()!=null) {
				
				if(reports!=null&&reports.getRecordModelCode()!=null) {
					HashMap<String, Object> modelMap = new  HashMap<String, Object>();
					recordDirService.getRecordModel(modelMap, reports.getRecordModelCode());
					map.put("recordModel", modelMap);
				}
				
			}
			
			//设备信息
			DeviceDocument device = deviceDao.get(info.getFk_tsjc_device_document_id());
			if(device.getEnterInfo()==null) {
				EnterInfo company = enterDao.get(device.getFk_company_info_use_id());
				device.setEnterInfo(company);
			}
			map.put("device", device);
			/*//记录日志
			recordLogService.setLogs(info.getId(), user.getName(), "查询原始记录数据信息",
							user.getId(), user.getName(), new Date(), TS_Util.getIpAddress(request), "查询",null,null);*/
			
		}
		return map;
	}
	
	
	/**
	 * 查询原始记录录入数据信息
	 * author pingZhou
	 * @param request 
	 * @param id
	 * @param input 是否录入环节
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> detailMap(HttpServletRequest request, String id, String input) throws Exception {
		String sql = "from ReportItemRecord where fk_inspection_info_id =? and data_status <> '99'";
		
		String pageCode = request.getParameter("pageCode");
		if(pageCode!=null&&StringUtil.isNotEmpty(pageCode)) {
			sql = sql + " and page_no = '"+pageCode+"'";
		}
		
		List<ReportItemRecord> list = this.reportItemRecordDao.listQuery(sql,id);
		
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		Map<String, Object> dataMap = this.rtPageManager.loadFuncData(id,input,pageCode);
		Map<String, Object> colorMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_COLOR);
		Map<String, Object> boldMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_BOLD);
		Map<String, Object> italicMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_ITALIC);
		Map<String, Object> sizeMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_SIZE);
		Map<String, Object> familyMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_FAMILY);
		Map<String, Object> imageMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_IMAGE);
		Map<String, Object> markMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_MARK);


		InspectionInfo info = infoDao.get(id);
		Map<String, String> infoMap= info.bean_to_Map();
		for (String key : infoMap.keySet()) {
			String value = infoMap.get(key);
			if(value!=null&&StringUtil.isNotEmpty(infoMap.get(key))) {
				Map<String, String> mapi = new HashMap<String, String>();
				
				mapi.put("fkRecordId", id);
				mapi.put("itemValue", value);
				mapi.put("itemName", "base__"+key.toLowerCase());
				returnList.add(mapi);
			}
			
		}

		for (ReportItemRecord item : list) {
			// if (StringUtil.isNotEmpty(item.getItemValue())) {
			Map<String, String> map = new HashMap<String, String>();
			String name = item.getItem_name();
			if (!(name.contains(RtPageType.TABLE) || name.contains("FK") || name.contains("fk")
					|| name.contains("picture")||name.startsWith(RtPath.getPropetyValue("inspect_record", "record")+"__")
					||name.startsWith(RtPath.getPropetyValue("inspect_conclusion", "conclusion")+"__"))) {
				name = "base__" + name;
			}
			map.put("fkReportId", item.getFk_report_id());
			map.put("pageNo", item.getPage_no());
			map.put("fkRecordId", id);
			map.put("itemValue", item.getItem_value());
			map.put("itemName", name);
			map.put("itemValue", item.getItem_value());
			
			String key = item.getItem_name();
			
			if (colorMap != null && colorMap.containsKey(key)&&colorMap.get(key)!=null) {
				map.put("color", colorMap.get(key).toString());
			}
			if (boldMap != null && boldMap.containsKey(key)&&boldMap.get(key)!=null) {
				map.put("bold", boldMap.get(item.getItem_value()).toString());
			}
			if (italicMap != null && italicMap.containsKey(key)&&italicMap.get(key)!=null) {
				map.put("italic", italicMap.get(key).toString());
			}
			if (sizeMap != null && sizeMap.containsKey(key)&&sizeMap.get(key)!=null) {
				map.put("size", sizeMap.get(key).toString());
			}
			if (familyMap != null && familyMap.containsKey(key)&&familyMap.get(key)!=null) {
				map.put("family", familyMap.get(key).toString());
			}
			if (imageMap != null && imageMap.containsKey(key)&&imageMap.get(key)!=null) {
				map.put("image", imageMap.get(key).toString());
			}
			if (markMap != null && markMap.containsKey(key)&&markMap.get(key)!=null) {
				map.put("markContent", markMap.get(key).toString());
			}
			/* System.out.println("------------"+imageMap+"--------------"); */
			returnList.add(map);
			// }
		}


		
		return returnList;
	}
	
	
	/**
	 * 按照分页查询原始记录录入数据信息
	 * author pingZhou
	 * @param request 
	 * @param id
	 * @param input 是否录入环节
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> detailMapBypageCodes(HttpServletRequest request, String id, String input,String pageCodes) throws Exception {
		String sql = "from ReportItemRecord where fkInspectionInfoId =? and dataStatus <> '99'";
		String pageCodess = pageCodes.replace(",", "','");
		if(pageCodes!=null&&StringUtil.isNotEmpty(pageCodes)) {
			sql = sql + " and pageNo in ('"+pageCodess+"')";
		}
		
		/*if(code_ext!=null&&code_ext!="null"&&StringUtil.isNotEmpty(code_ext)){
			sql = "from ReportItemRecord where fkInspectionInfoId =? and itemName like '%"+code_ext+"%'";
		}*/
		List<ReportItemRecord> list = this.reportItemRecordDao.listQuery(sql,
				id);
		
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		Map<String, Object> dataMap = this.rtPageManager.loadFuncDataByPages(id,input,pageCodes);
		Map<String, Object> colorMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_COLOR);
		Map<String, Object> boldMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_BOLD);
		Map<String, Object> italicMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_ITALIC);
		Map<String, Object> sizeMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_SIZE);
		Map<String, Object> familyMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_FAMILY);
		Map<String, Object> imageMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_IMAGE);
		Map<String, Object> markMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_MARK);

		if(pageCodes==null||StringUtil.isEmpty(pageCodes)) {
			InspectionInfo info = infoDao.get(id);
			Map<String, String> infoMap= info.bean_to_Map();
			for (String key : infoMap.keySet()) {
				String value = infoMap.get(key);
				if(value!=null&&StringUtil.isNotEmpty(infoMap.get(key))) {
					Map<String, String> mapi = new HashMap<String, String>();
					
					mapi.put("fkRecordId", id);
					mapi.put("itemValue", value);
					mapi.put("itemName", "base__"+key.toLowerCase());
					returnList.add(mapi);
				}
				
			}
		}

		for (ReportItemRecord item : list) {
			// if (StringUtil.isNotEmpty(item.getItemValue())) {
			Map<String, String> map = new HashMap<String, String>();
			String name = item.getItem_name();
			if (!(name.contains(RtPageType.TABLE) || name.contains("FK") || name.contains("fk")
					|| name.contains("picture")||name.startsWith(RtPath.getPropetyValue("inspect_record", "record")+"__")
					||name.startsWith(RtPath.getPropetyValue("inspect_conclusion", "conclusion")+"__"))) {
				name = "base__" + name;
			}
			map.put("fkReportId", item.getFk_report_id());
			map.put("pageNo", item.getPage_no());
			map.put("fkRecordId", id);
			map.put("itemValue", item.getItem_value());
			map.put("itemName", name);
			map.put("itemValue", item.getItem_value());
			
			String key = item.getItem_name()+item.getPage_no();
			
			if (colorMap != null && colorMap.containsKey(key)&&colorMap.get(key)!=null) {
				map.put("color", colorMap.get(key).toString());
			}
			if (boldMap != null && boldMap.containsKey(key)&&boldMap.get(key)!=null) {
				map.put("bold", boldMap.get(item.getItem_value()).toString());
			}
			if (italicMap != null && italicMap.containsKey(key)&&italicMap.get(key)!=null) {
				map.put("italic", italicMap.get(key).toString());
			}
			if (sizeMap != null && sizeMap.containsKey(key)&&sizeMap.get(key)!=null) {
				map.put("size", sizeMap.get(key).toString());
			}
			if (familyMap != null && familyMap.containsKey(key)&&familyMap.get(key)!=null) {
				map.put("family", familyMap.get(key).toString());
			}
			if (imageMap != null && imageMap.containsKey(key)&&imageMap.get(key)!=null) {
				map.put("image", imageMap.get(key).toString());
			}
			if (markMap != null && markMap.containsKey(key)&&markMap.get(key)!=null) {
				map.put("markContent", markMap.get(key).toString());
			}
			/* System.out.println("------------"+imageMap+"--------------"); */
			returnList.add(map);
			// }
		}


		
		return returnList;
	}

	/**
	 * 原始记录录入提交校核
	 * author pingZhou
	 * @param request
	 * @param infoId
	 * @return 
	 * @throws Exception 
	 */
	public InspectionInfo subConfirm(HttpServletRequest request,JSONObject object) throws Exception {
		
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		
		if(object==null){
			throw new KhntException("请传入参数！");
		}
		if(!object.has("nextOpId")||object.get("nextOpId")==null){
			throw new KhntException("请选择下一个录入人员！");
		}
		String infoId = object.getString("infoId");
		
		InspectionInfo info = null;
		if(StringUtil.isNotEmpty(infoId) && !"null".equals(infoId)) {
			info  = infoDao.get(infoId);
			//0录入 9退回录入 10转移录入
			if(info.getRecordFlow()!=null&&!("0".equals(info.getRecordFlow())||"9".equals(info.getRecordFlow())||"10".equals(info.getRecordFlow()))) {
				throw new KhntException("此检验流程已经提交走，不能重复操作！");
			}
		}
		
		if(info == null) {
			info = new InspectionInfo();
		}
		String nextOpId = object.getString("nextOpId");
		String nextOp = object.getString("nextOp");
		//修改持有人
		info.setRecordHandleId(nextOpId);
		info.setRecordHandleOp(nextOp);
		//校核
		info.setRecordFlow("1");
		//保存录入数据信息
		saveMap(request,object,info);
		//记录日志
		recordLogService.setLogs(info.getId(), user.getName(), "原始记录提交校核",
							user.getId(), user.getName(), new Date(), TS_Util.getIpAddress(request), "提交校核",nextOpId,nextOp);
		
		return info;
	}
	
	

	/**
	 * 原始记录录入提交校核（不包括数据保存）
	 * author pingZhou
	 * @param request
	 * @param infoId
	 * @throws Exception 
	 */
	public HashMap<String, Object> subToConfirmWithoutData(HttpServletRequest request,HashMap<String, Object> map) throws Exception {
		
		
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String data = request.getParameter("data");
		
		JSONObject object = JSONObject.fromString(data);
		if(object==null){
			map.put("success", false);
			map.put("msg", "请传入参数！");
			return map;
		}

		String infoId = object.getString("infoId");
		InspectionInfo info  = infoDao.get(infoId);
		//0录入 9退回录入 10转移录入
		if(info.getRecordFlow()!=null&&!("0".equals(info.getRecordFlow())||"9".equals(info.getRecordFlow())||"10".equals(info.getRecordFlow()))) {
			throw new KhntException("此检验流程已经提交走，不能重复操作！");
		}
		
		if(!object.has("nextOpId")||object.get("nextOpId")==null){
			map.put("success", false);
			map.put("msg", "请选择下一个录入人员！");
			return map;
		}
		
		
		String nextOpId = object.getString("nextOpId");
		String nextOp = object.getString("nextOp");
		
		
		
		info.setRecordFlow("1");//校核
		//修改持有人
		info.setRecordHandleId(nextOpId);
		info.setRecordHandleOp(nextOp);
		
		infoDao.save(info);
		//记录日志
		recordLogService.setLogs(info.getId(), user.getName(), "原始记录提交校核",
							user.getId(), user.getName(), new Date(), TS_Util.getIpAddress(request), "提交校核",nextOpId,nextOp);
		
		return map;
	}


	/**
	 * 接收待录入原始记录
	 * author pingZhou
	 * @param map
	 * @param request
	 * @param infoId
	 * @return
	 * @throws Exception
	 */
	
	
	/**
	 * 接收报检任务
	 * author pingZhou
	 * @param map
	 * @param request
	 * @param infoId
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> reviceInspection(HashMap<String, Object> map, HttpServletRequest request, String infoId) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		
		
		InspectionInfo info = infoDao.get(infoId);
		
		//0录入 9退回录入 10转移录入
		if(info.getRecordFlow()!=null&&!("0".equals(info.getRecordFlow())||"9".equals(info.getRecordFlow())||"10".equals(info.getRecordFlow()))) {
			throw new KhntException("此检验流程已经提交走，不能重复操作！");
		}
		
		getInspData(map, request, infoId);
		
		info.setReceiveStatus("1");
		info.setRecordHandleId(((User)user.getSysUser()).getEmployee().getId());
		info.setRecordHandleOp(((User)user.getSysUser()).getEmployee().getName());
		

		//
	/*	String reportsn = info.getReport_sn();
		if (StringUtil.isEmpty(reportsn)) {
			if ("402880e45af554e9015af56488a600gl".equals(info.getFk_tsjc_device_document_id())) {
				reportsn = reportItemValueService.autoGeneratGLReportSn(infoId);
			} else {
				reportsn = reportItemValueService.autoGeneratReportSn(infoId);
			}
		}
		info.setReportSn(reportsn);
		*/
	/*	if(StringUtil.isEmpty(info.getTwoDimenSional())) {
			//生成二维码
			reportsManager.showTwoDimNew(infoId, request);
		}
		*/
		
		
		infoDao.save(info);
		
		return map;
	}



	public void getCopyData(HashMap<String, Object> map, HttpServletRequest request, String infoId) throws Exception {
		if(map==null) {
			map = new HashMap<String, Object>();
		}
		String id = request.getParameter("id");
		CurrentSessionUser user = SecurityUtil.getSecurityUser();

		//保存数据信息
		InspectionInfo info = infoDao.get(infoId);
		if(info==null) {
			throw new KhntException("没有取到数据，请检查传入的参数！");
			
		}
		//业务记录信息
		map.put("record", info);
	
		
		//目录信息
		List<InspectRecordDir> dirList =  recordDirDao.getDirByBid(infoId);
		map.put("dirList", dirList);
		
		Report reports = reportsDao.get(info.getReport_type());
		map.put("recordCode", reports.getRecordModelCode());

		//不是查询基础信息，则查询所有信息
		//录入数据信息
		List<Map<String, String>> dataList = detailMap(request,infoId,null);
			
		map.put("dataList", dataList);
		
		if(id!=null&&StringUtil.isNotEmpty(id)) {
			
			TzsbRecordLog log  = new TzsbRecordLog();
			log.setBusiness_id(id);
			log.setOp_action("原始记录复制");
			log.setOp_ip(TS_Util.getIpAddress(request));
			log.setOp_remark("原始记录复制");
			log.setOp_status("复制");
			log.setOp_time(new Date());
			log.setOp_user_id(user.getId());
			log.setOp_user_name(user.getName());
			log.setTarget_id(infoId);
			
			recordLogDao.save(log);
			
		}
	}
	
	/**
	 * 退回原始记录录入
	 * author pingZhou
	 * @param request
	 * @param ids
	 * @param remark
	 * @param opDate
	 */
	public void backToInput(HttpServletRequest request, String ids, String remark,String opDate) {

		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		if(remark==null||StringUtil.isEmpty(remark)) {
			remark = request.getParameter("remark");
		}
		if(remark==null||StringUtil.isEmpty(remark)) {
			remark = "原始记录退回";
		}
		
		if(opDate==null||StringUtil.isEmpty(opDate)) {
			opDate = request.getParameter("opDate");
		}
		Date now = new Date();
		if(opDate==null||StringUtil.isEmpty(opDate)) {
			try {
				now =new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(opDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String[] idss = ids.split(",");
		for (int i = 0; i < idss.length; i++) {
			//保存数据信息
			InspectionInfo info = infoDao.get(idss[i]);
			info.setRecordHandleId(info.getRecordEnterId());
			info.setRecordHandleOp(info.getRecordEnterOp());
			info.setRecordFlow("9");//退回用专门的标志
			info.setReceiveStatus("0");//设置为未接收
			
			infoDao.save(info);
			
			
				TzsbRecordLog log  = new TzsbRecordLog();
				log.setBusiness_id(idss[i]);
				log.setOp_action("原始记录退回");
				log.setOp_ip(TS_Util.getIpAddress(request));
				log.setOp_remark(remark);
				log.setOp_status("退回");
				log.setOp_time(now);
				log.setOp_user_id(user.getId());
				log.setOp_user_name(user.getName());
				
				recordLogDao.save(log);
		}
		
	}


	
	
	
	public List<Map<String, Object>> queryCodeTable(String code, String codes) {
		if(codes==null) {
			return reportItemRecordDao.queryCodeTable(code);
		}else{
			codes = codes.replace(",", "','");
			return reportItemRecordDao.queryCodeTableByIds(codes);
		}
	}
	
	
	public String queryCodesByIds(String codeupdateTime) {
		String codes = "";
		List<String> list = reportItemRecordDao.queryCodeTableUpdate(codeupdateTime);
		if(list.size()>0) {
			
			for (int i = 0; i < list.size(); i++) {
				codes = codes +"," +list.get(i);
			}
			codes = codes.substring(1);
			
			
		}
		return codes;
	}



	public void saveMapPc(HttpServletRequest request,List<Map<String, Object>> list, String page) throws Exception {
		
		Map<String, String> imgIdMap = new HashMap<String, String>();
		Map<String, RtExportData> map = RtSaveAsst.transToMap(list, RtPageType.TABLE);
		
		String fk_report_id = map.get("fk_report_id").getValue();
		String fk_inspection_info_id = map.get("fk_inspection_info_id").getValue();

		String code_ext = map.get("fkCodeExt") == null ? "" : map.get("fkCodeExt").getValue();
		if (StringUtil.isEmpty(code_ext)) {
			code_ext = "";
		}else{
			page = page+"__"+code_ext;
		}
		
		InspectionInfo info = infoDao.get(fk_inspection_info_id);

		if(fk_report_id==null||StringUtil.isEmpty(fk_report_id)) {
			
			fk_report_id = info.getReport_type();
			
		}
		
		if (StringUtil.isNotEmpty(fk_inspection_info_id)) {
			CurrentSessionUser user = SecurityUtil.getSecurityUser();

			RtSaveAsst asst = new RtSaveAsst();

			//取出所有需要保存内容
			String KeyNames = map.keySet().toString();
			String names = "";
			if (StringUtil.isEmpty(code_ext) || !code_ext.contains("_")) {
				names = KeyNames.substring(1,KeyNames.length()-1).replace(",", "','").replace(" ", "");
			}else{
				names = KeyNames.substring(1,KeyNames.length()-1).replace(",", RtField.separator + code_ext+"','").replace(" ", "");
			}

			this.reportItemRecordDao
					.createSQLQuery("update TZSB_REPORT_ITEM_Record set data_status='99' where fk_report_id =? and item_name in ('"+names+"')"
									+ " and fk_inspection_info_id=? and data_status<>'99' and page_no =?",
							fk_report_id, fk_inspection_info_id,page)
					.executeUpdate();
			this.rtPageManager.delFuncDataBacth(fk_inspection_info_id, names,page);



			for (String itemName : map.keySet()) {
				// if (itemName.contains(RtPageType.TABLE)) {
				RtExportData rtExportData = map.get(itemName);
				if("com_name".equals(itemName)) {
					System.err.println(map.get(itemName).getValue());
				}
				rtExportData.setPageNo(page);
				String name = RtField.getName(itemName, code_ext);
				rtExportData.setName(name);
				
				String markContent = rtExportData.getMarkContent();
				boolean flagm = !StringUtil.isEmpty(markContent)&&!"null".equals(markContent)&&markContent!=null ;
				if ((rtExportData.getValue() != null && StringUtil.isNotEmpty(rtExportData.getValue()))
						||flagm) {
					ReportItemRecord item = new ReportItemRecord();
					item.setItem_name(rtExportData.getName());
					item.setItem_value(rtExportData.getValue());
					item.setFk_report_id(fk_report_id);
					item.setLast_mdy_time(timeformat.format(new Date()));
					item.setLast_mdy_uid(user.getId());
					item.setLast_mdy_uname(user.getName());
					item.setData_status("0");
					item.setPage_no(page);
					item.setFk_inspection_info_id(fk_inspection_info_id);
					// if (item.getItemValue() != null &&
					// item.getItemValue().trim().length() > 0) {

					
					this.save(item);
				}




				// 判断是否着色
				asst.setFkBusinessId(fk_inspection_info_id);
				asst.setRtPageDao(this.rtPageDao);
				// asst.setrtReportItemValueDao(rtReportItemValueDao);
				// asst.setRtCode(rtCode);
				asst.execute(rtExportData, null, null,null);


				// }

				// }
			}


			if(info.getRecordPageCode()==null||StringUtil.isEmpty(info.getRecordPageCode())){

				info.setRecordPageCode(page);

			}else{

				boolean flag = infoDao.checkHasSave(info, page);
				if(!flag){
					info.setSave_page_item(info.getRecordPageCode()+","+page);
				}

			}
			
			if(info.getIs_input()==null||!"1".equals(info.getIs_input())){
				//修改检验业务信息
				info.setEnter_op_id(((User)user.getSysUser()).getEmployee().getId());
				info.setEnter_op_name(((User)user.getSysUser()).getEmployee().getName());
				
				info.setEnter_time(new Date());
				info.setIs_input("1");

			}
			if(info.getRecordEnterId()==null) {
				info.setRecordEnterId(((User)user.getSysUser()).getEmployee().getId());
				info.setRecordEnterOp(((User)user.getSysUser()).getEmployee().getName());
				info.setRecordEnterTime(new Date());
			}
		
			// 原始记录转换成报告
			parseManager.dealRecordToReport(request, info.getId());

			/*// 处理报告书编号
			if(StringUtil.isEmpty(info.getReport_sn())) {
				reportItemValueService.saveReportSn(info);
			}*/
			// 更新原始记录转换报告状态
			parseManager.updateRecordStatus(info);
			
		}
	}


	/**
	 * 分页复制
	 * author pingZhou
	 * @param map
	 * @param request
	 * @param infoId
	 * @throws Exception
	 */
	public void getCopyDataByPage(HashMap<String, Object> map, HttpServletRequest request, String infoId) throws Exception {
		if(map==null) {
			map = new HashMap<String, Object>();
		}
		String id = request.getParameter("id");
		CurrentSessionUser user = SecurityUtil.getSecurityUser();

		String pageCodes = request.getParameter("pageCodes");
		if(pageCodes==null||StringUtil.isEmpty(pageCodes)) {
			throw new KhntException("请检查传入需复制的分页信息参数！");
			
		}
		//保存数据信息
		InspectionInfo info = infoDao.get(infoId);
		if(info==null) {
			throw new KhntException("没有取到数据，请检查传入的参数！");
			
		}
		//业务记录信息
		map.put("record", info);
	
		
		//目录信息
		List<InspectRecordDir> dirList =  recordDirDao.getDirByBid(infoId);
		map.put("dirList", dirList);
		
		Report reports = reportsDao.get(info.getReport_type());
		map.put("recordCode", reports.getRecordModelCode());

		//不是查询基础信息，则查询所有信息
		//录入数据信息
		List<Map<String, String>> dataList = detailMapBypageCodes(request,infoId,null,pageCodes);
			
		map.put("dataList", dataList);
		
		if(id!=null&&StringUtil.isNotEmpty(id)) {
			
			TzsbRecordLog log  = new TzsbRecordLog();
			log.setBusiness_id(id);
			log.setOp_action("原始记录复制");
			log.setOp_ip(TS_Util.getIpAddress(request));
			log.setOp_remark("原始记录复制");
			log.setOp_status("复制");
			log.setOp_time(new Date());
			log.setOp_user_id(user.getId());
			log.setOp_user_name(user.getName());
			log.setTarget_id(infoId);
			
			recordLogDao.save(log);
			
		}
	}
	private void validateRegistrationNumDuplicate(DeviceDocument ddoc) throws Exception{
		// 验证使用登记证号是否重复
		if(StringUtil.isEmpty(ddoc.getRegistration_num())){
			throw new Exception("亲，使用登记证号是必填项目，请补充完整再提交！");
		}
		List<DeviceDocument> list = deviceService.getDeviceInfosByRegistrationNum(ddoc.getId(), ddoc.getRegistration_num());
		if(list.size()>1) {
			String[] drcs = new String[list.size()];
			for(int i =0;i<list.size();i++){
				drcs[i]=list.get(i).getDevice_registration_code();
			}
			String drc = String.join(",", drcs);
			throw new Exception("亲，使用登记证号与设备（"+drc+"）重复了，请纠正再提交！");
		}
	}
	private void validateDeviceQrCodeDuplicate(DeviceDocument ddoc) throws Exception{
		if(StringUtil.isEmpty(ddoc.getDevice_qr_code())){
			throw new Exception("亲，该设备没有二维码编号或未填写！");
		}
		// 验证使用登记证号是否重复
		List<DeviceDocument> list = deviceService.getDeviceInfosByQRCode(ddoc.getId(), ddoc.getDevice_qr_code());
		if(list.size()>1) {
			String[] drcs = new String[list.size()];
			for(int i =0;i<list.size();i++){
				drcs[i]=list.get(i).getDevice_qr_code();
			}
			String drc = String.join(",", drcs);
			throw new Exception("亲，设备："+drc+" 的二维码编号重复了，请纠正再提交！");
		}
	}
	private void validateInfoDuplicate(DeviceDocument ddoc,InspectionInfo info) throws Exception{
		if(StringUtil.isNotEmpty(ddoc.getDevice_registration_code())){
			// 定检报告，验证同一个设备2个月内是否已录入报告，有纠错程序除外（质量部要求）
			// 检验类别（3：定检 2：监检）
			// 2017-08-22 质量部谢方提出修改，增加不同的报告模板出具时，则不受此限制
			// 2017-08-22 张展彬提出修改
			// 配合2017年联合国世界旅游组织第22届全体大会保障性检验，暂时放开电梯定检报告出具周期限制
			// 2017-09-11 张展彬提出修改，重启开启电梯定检报告出具周期限制
			// 2017-09-12 张展彬提出修改，暂时放开电梯定检报告出具周期限制
			// 2017-09-12 增加电梯定检报告出具周期（2个月）限制开关可控功能
			String vali = Factory.getSysPara().getProperty("DT_VALIDATE_REPEAT_2M");
			boolean validate_repeat = true;
			if(StringUtil.isNotEmpty(vali)){
				if("true".equals(vali.trim()) || "false".equals(vali.trim())){
					validate_repeat = Boolean.parseBoolean(vali.trim());
				}
			}
			if(validate_repeat){
				if ("3".equals(info.getCheck_category_code())) {
					String inspectionId = info.getInspection() != null ? info.getInspection().getId() : "";
					List<InspectionInfo> infoList = infoDao.getNewInfoByDeviceId(ddoc.getId(), inspectionId);
					if (infoList != null && infoList.size() > 0) {
						for (InspectionInfo oInfo : infoList) {
							if (StringUtil.isNotEmpty(oInfo.getInspection_conclusion())
									&&!"不合格".equals(oInfo.getInspection_conclusion().trim())) {
								// 质量部谢方2017-08-22提出修改，增加不同的报告模板出具时，则不受此限制
								if(oInfo.getReport_type().equals(info.getReport_type())){
									HashMap<String, Object> returnMap = validateRepeat(oInfo,ddoc.getDevice_registration_code());
									if (!Boolean.valueOf(String.valueOf(returnMap.get("success")))) {
										throw new Exception(returnMap.get("msg").toString());
									}
								}
							}
						}
					}
				}
			}
		}
	}
	/*
	 * private double caculateFee(DeviceDocument ddoc,InspectionInfo info) throws
	 * Exception{ if(StringUtil.isEmpty(ddoc.getDevice_sort())){ throw new
	 * Exception("亲，您提交的原始记录无设备类别无法计算费用哦，请完善后再提交！"); }
	 * if(StringUtil.isEmpty(ddoc.getDevice_sort_code())){ throw new
	 * Exception("亲，您提交的原始记录无设备名称无法计算费用哦，请完善后再提交！"); } //
	 * 计算电梯收费金额（2016-03-09杜院要求上传数据的同时计算费用，方便检验员查询费用） //
	 * 2017-12-14应明子涵要求，新疆特检突击队报告费用由检验员手动输入，不再自动计算价格biu~biu~ //
	 * 2018-03-29应明子涵要求，九寨特检突击队报告费用由系统自动计算价格biu~biu~ // 九寨沟的计算公式均无1.3和1.5的系数，故此处去掉
	 * if(StringUtil.isNotEmpty(check_unit_id)){ if
	 * (!Constant.CHECK_UNIT_100090.equals(check_unit_id) &&
	 * !Constant.CHECK_UNIT_100091.equals(check_unit_id)) { double dt_money = 0; //
	 * 不打折，也不四舍五入 if (report_name.contains("病床")) { if (check_type.equals("3")) {//
	 * 定期检验 // 判断是否有限速器 if (xsqts.equals("0")) {// 没有勾选限速器 // 判断电梯层数 if (dtcs <= 5)
	 * { dt_money = (int) Math.round((500)); } else { dt_money = (int)
	 * Math.round(500 + (dtcs - 5) * 50); } } else if (xsqts.equals("1")) {//
	 * 勾选一个限速器 if (dtcs <= 5) { dt_money = (int) Math.round(500 * 1.3 + 200); } else
	 * { dt_money = (int) Math.round((500 + (dtcs - 5) * 50) * 1.3 + 200); } } else
	 * if (xsqts.equals("2")) {// 勾选两个限速器 if (dtcs <= 5) { dt_money = (int)
	 * Math.round(500 * 1.3 + 400); } else { dt_money = (int) Math.round((500 +
	 * (dtcs - 5) * 50) * 1.3 + 400); } } } else if (check_type.equals("2")) {//
	 * 监督检验 // 判断电梯层数 if (dtcs <= 5) { dt_money = (int) Math.round(500 * 1.5); }
	 * else { dt_money = (int) Math.round((500 + (dtcs - 5) * 50) * 1.5); } } }else{
	 * if (StringUtil.isNotEmpty(device_type) &&
	 * StringUtil.isNotEmpty(device_sort_code)) { // 检验类别（3：定检 2：监检） if
	 * ("3".equals(check_type)) { // 区分设备类型 if (device_type.equals("3100") ||
	 * device_type.equals("3200") || device_sort_code.equals( "3410") ||
	 * device_sort_code.equals( "3420")) { // 判断是否有限速器 if ("0".equals(xsqts)) {//
	 * 没有勾选限速器 // 判断电梯层数 if (dtcs <= 5) { dt_money = 550; } else { dt_money = (550 +
	 * (dtcs - 5) * 55); } } else if ("1".equals(xsqts)) {// 勾选一个限速器 if (dtcs <= 5)
	 * { dt_money = 550 * 1.3 + 200; } else { dt_money = (550 + (dtcs - 5) * 55) *
	 * 1.3 + 200; }
	 * 
	 * } else if ("2".equals(xsqts)) {// 勾选两个限速器 if (dtcs <= 5) { dt_money = 550 *
	 * 1.3 + 400; } else { dt_money = (550 + (dtcs - 5) * 55) * 1.3 + 400; } } }else
	 * if (device_type.equals("3300") || device_sort_code.equals( "3430")) {
	 * dt_money = 400;// 自动扶梯和人行道、杂物电梯 定额收费 } } else if (check_type.equals("2")) {//
	 * 监督检验 // 区分设备类型 if (device_sort_code.equals("3410") ||
	 * device_sort_code.equals( "3420") || device_type.equals("3200") ||
	 * device_type.equals("3100")) {
	 * 
	 * // 判断电梯层数 if (dtcs <= 5) { dt_money = 550 * 1.5; } else { dt_money = (550 +
	 * (dtcs - 5) * 55) * 1.5; }
	 * 
	 * }else if (device_type.equals("3300") || device_sort_code.equals( "3430")) {
	 * dt_money = 600;// 自动扶梯和人行道、杂物电梯 定额收费 } } } }
	 * 
	 * // 复检收费30% if (device_type.startsWith("3")) { if
	 * (inspection_conclusion.equals("复检合格")) { dt_money = (double) Math
	 * .round(dt_money * 0.3); } } info.setAdvance_fees(dt_money); }else{ //
	 * 2017-12-14因明子涵要求，新疆特检突击队报告费用由检验员手动输入，不再自动计算价格biu~biu~ //
	 * 2018-03-29应明子涵要求，九寨特检突击队报告费用由系统自动计算价格biu~biu~ // 九寨沟的计算公式均无1.3和1.5的系数，故此处去掉
	 * double dt_money = 0; // 不打折，也不四舍五入 if (report_name.contains("病床")) { if
	 * (check_type.equals("3")) {// 定期检验 // 判断是否有限速器 if (xsqts.equals("0")) {//
	 * 没有勾选限速器 // 判断电梯层数 if (dtcs <= 5) { dt_money = (int) Math.round((500)); } else
	 * { dt_money = (int) Math.round(500 + (dtcs - 5) * 50); } } else if
	 * (xsqts.equals("1")) {// 勾选一个限速器 if (dtcs <= 5) { dt_money = (int)
	 * Math.round(500 + 200); } else { dt_money = (int) Math.round((500 + (dtcs - 5)
	 * * 50) + 200); } } else if (xsqts.equals("2")) {// 勾选两个限速器 if (dtcs <= 5) {
	 * dt_money = (int) Math.round(500 + 400); } else { dt_money = (int)
	 * Math.round((500 + (dtcs - 5) * 50) + 400); } } } else if
	 * (check_type.equals("2")) {// 监督检验 // 判断电梯层数 if (dtcs <= 5) { dt_money = (int)
	 * Math.round(500); } else { dt_money = (int) Math.round((500 + (dtcs - 5) *
	 * 50)); } } }else{ if (StringUtil.isNotEmpty(device_type) &&
	 * StringUtil.isNotEmpty(device_sort_code)) { // 检验类别（3：定检 2：监检） if
	 * ("3".equals(check_type)) { // 区分设备类型 if (device_type.equals("3100") ||
	 * device_type.equals("3200") || device_sort_code.equals( "3410") ||
	 * device_sort_code.equals( "3420")) { // 判断是否有限速器 if ("0".equals(xsqts)) {//
	 * 没有勾选限速器 // 判断电梯层数 if (dtcs <= 5) { dt_money = 550; } else { dt_money = (550 +
	 * (dtcs - 5) * 55); } } else if ("1".equals(xsqts)) {// 勾选一个限速器 if (dtcs <= 5)
	 * { dt_money = 550 + 200; } else { dt_money = (550 + (dtcs - 5) * 55) + 200; }
	 * 
	 * } else if ("2".equals(xsqts)) {// 勾选两个限速器 if (dtcs <= 5) { dt_money = 550 +
	 * 400; } else { dt_money = (550 + (dtcs - 5) * 55) + 400; } } }else if
	 * (device_type.equals("3300") || device_sort_code.equals( "3430")) { dt_money =
	 * 400;// 自动扶梯和人行道、杂物电梯 定额收费 } } else if (check_type.equals("2")) {// 监督检验 //
	 * 区分设备类型 if (device_sort_code.equals("3410") || device_sort_code.equals(
	 * "3420") || device_type.equals("3200") || device_type.equals("3100")) {
	 * 
	 * // 判断电梯层数 if (dtcs <= 5) { dt_money = 550; } else { dt_money = (550 + (dtcs -
	 * 5) * 55); }
	 * 
	 * }else if (device_type.equals("3300") || device_sort_code.equals( "3430")) {
	 * dt_money = 600;// 自动扶梯和人行道、杂物电梯 定额收费 } } } }
	 * 
	 * // 复检收费30% if (device_type.startsWith("3")) { if
	 * (inspection_conclusion.equals("复检合格")) { dt_money = (double) Math
	 * .round(dt_money * 0.3); } } info.setAdvance_fees(dt_money); } }
	 * 
	 * // 西藏报告收费标准，直梯（有机房、无机房）定检1000元整，扶梯定检800元整，杂物梯定检400元整 //
	 * 新疆报告收费标准，直梯（有机房、无机房）定检1000元整，扶梯定检800元整，杂物梯定检400元整 if
	 * (StringUtil.isNotEmpty(check_unit_id)) { // 100069：赴藏特检突击队 if
	 * (Constant.CHECK_UNIT_100069.equals(check_unit_id)) { double xz_money =
	 * 1000.00; if (device_type.equals("3100") || device_type.equals("3200")) {
	 * xz_money = 1000.00; } else if (device_type.equals("3400")) { if
	 * (device_type.equals("3430")) { xz_money = 400.00; } } else if
	 * (device_type.equals("3300")) { xz_money = 800.00; }
	 * info.setAdvance_fees(xz_money); } }
	 * //info.setInspection_conclusion(inspection_conclusion); // 检验结论 }
	 */
}
