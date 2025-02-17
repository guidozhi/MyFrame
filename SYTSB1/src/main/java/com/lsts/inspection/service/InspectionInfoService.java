package com.lsts.inspection.service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.khnt.base.Factory;
import com.khnt.bpm.communal.BpmOrg;
import com.khnt.bpm.communal.BpmOrgImpl;
import com.khnt.bpm.communal.BpmUser;
import com.khnt.bpm.communal.BpmUserImpl;
import com.khnt.bpm.core.bean.Activity;
import com.khnt.bpm.ext.service.FlowDefinitionManager;
import com.khnt.bpm.ext.service.FlowWorktaskManager;
import com.khnt.bpm.ext.support.FlowExtParam;
import com.khnt.bpm.ext.support.FlowExtWorktaskParam;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.core.exception.KhntException;
import com.khnt.pub.fileupload.service.AttachmentManager;
import com.khnt.rbac.impl.bean.Employee;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rbac.impl.dao.UserDao;
import com.khnt.rtbox.config.bean.RtPage;
import com.khnt.rtbox.config.bean.RtPersonDir;
import com.khnt.rtbox.config.dao.RtPageDao;
import com.khnt.rtbox.config.dao.RtPersonDirDao;
import com.khnt.rtbox.config.service.TemplateManager;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.DateUtil;
import com.khnt.utils.LogUtil;
import com.khnt.utils.StringUtil;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import com.lsts.advicenote.service.MessageService;
import com.lsts.constant.Constant;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.dao.DeviceDao;
import com.lsts.employee.dao.EmployeesDao;
import com.lsts.flow.service.BaseFlowConfigService;
import com.lsts.inspection.bean.Inspection;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.bean.InspectionVersion;
import com.lsts.inspection.dao.InspectionDao;
import com.lsts.inspection.dao.InspectionInfoDao;
import com.lsts.inspection.dao.InspectionVersionDao;
import com.lsts.inspection.dao.ReportItemRecordDao;
import com.lsts.inspection.dao.ReportItemValueDao;
import com.lsts.log.service.SysDzyzLogService;
import com.lsts.log.service.SysLogService;
import com.lsts.org.bean.EnterInfo;
import com.lsts.org.service.EnterService;
import com.lsts.report.bean.Report;
import com.lsts.report.dao.ReportDao;
import com.lsts.webservice.cxf.server.report.ReportDTO;
import com.scts.machine.bean.MachineLog;
import com.scts.machine.dao.MachineLogDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.PDF2SWFUtil;
import util.TS_Util;

/**
 * 业务报检 servier
 * 
 * @author 肖慈边 2014-2-17
 */
@Service("inspectionInfoService")
public class InspectionInfoService extends
		EntityManageImpl<InspectionInfo, InspectionInfoDao> {
	private static final String String = null;
	@Autowired
	private SysLogService sysLogService ;
	@Autowired
	private InspectionInfoDao infoDao;
	@Autowired
	private TemplateManager templateManager;
	@Autowired
	private ReportDao reportDao;
	@Autowired
	InspectionVersionDao inspectionVersionDao;
	@Autowired
	RtPersonDirDao rtPersonDirDao;
	@Autowired
	private InspectionDao inspectionDao;
	@Autowired
	private EmployeesDao employeesDao;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private ReportItemValueDao reportItemValueDao;
	@Autowired
	private ReportItemRecordDao reportItemRecordDao;
	@Autowired
	private ReportItemRecordService reportItemRecordService;
	@Autowired
	private EnterService enterService;
	@Autowired
	private BaseFlowConfigService baseFlowConfigService;
	@Autowired
	private SysLogService logService;
	@Autowired
	private SysDzyzLogService sysDzyzLogService;
	@Autowired
	private AttachmentManager attachmentManager;
	@Autowired
	private BaseFlowConfigService tzsbWorkFlowService;
	@Autowired
	FlowWorktaskManager flowExtManager;
	@Autowired
	FlowDefinitionManager flowDefManager;
	@Autowired
	private UserDao userDao;
	@Autowired
	private MessageService messageService;
	@Autowired
	private MachineLogDao machineLogDao;
	@Autowired
	RtPageDao rtPageDao;
	
	
	private DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

	public InspectionInfo getInspectionInfo(String id) {
		return infoDao.getInspectionInfo(id);
	}
	
	public InspectionInfo getInfoByReportSn(String report_sn) {
		return infoDao.getInfoByReportSn(report_sn);
	}
	
	public InspectionInfo getInfoByRandomCode(String random_code) {
		return infoDao.getInfoByRandomCode(random_code);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInfo(String inspectionInfoIds, String userId, String print_count) {
		return infoDao.queryInfo(inspectionInfoIds, userId, print_count);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInfo(String inspectionInfoId) {
		return infoDao.queryInfo(inspectionInfoId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryPageInfo(String inspectionInfoIds, String userId) {
		return infoDao.queryPageInfo(inspectionInfoIds, userId);
	}
	public void del(String ids) throws Exception {
		// TODO Auto-generated method stub
		// //判断是否多个ID
		// if(ids.indexOf(",")!=-1){
		//			
		// String id[] =ids.split(",");
		//			
		// for(int i=0;i<id.length;i++){
		//				
		// infoDao.createSQLQuery("update TZSB_INSPECTION set data_status='2'
		// where id =?",id[i] ).executeUpdate();
		//				
		// }
		//			
		// }else{
		//		
		infoDao
				.createSQLQuery(
						"update  TZSB_INSPECTION_INFO  set data_status='2' where id = ?",
						ids).executeUpdate();

		// }

	}
	
	/**
	 * 根据报告书编号查询报告
	 * 
	 * @param report_sn --
	 *            报告书编号
	 * @return
	 * @author GaoYa
	 * @date 2014-09-28 下午15:49:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionInfo> queryInspectionInfo(String report_sn) {
		return infoDao.queryInspectionInfo(report_sn);
	}
	/**
	 * 保存业务数据表以及相应附件
	 * 
	 * @param testUploadDemo
	 *            业务实体
	 * @param uploadFiles
	 *            附件对应的id集
	 * @return 返回业务实体
	 * @throws Exception
	 *             抛出异常
	 */
	public String  saveUpload(String inspectionInfoId,
			String uploadFile) throws Exception {
		//testUploadDemoDao.save(testUploadDemo);
		String ids = "" ;
		// 向附件中增加业务id
		if (!StringUtil.isEmpty(uploadFile)) {
			String[] files = uploadFile.replaceAll("/^,/", "").split(",");
			for (String fid : files) {
				if (!StringUtil.isEmpty(fid)){
					attachmentManager.setAttachmentBusinessId(fid,
							inspectionInfoId);
					ids = fid + "," ;
				}
			}
			// attachmentManager.setAttachmentBusinessId(files,testUploadDemo.getId());
		}
		//return null;
		return ids.substring(0, ids.length()-1) ;
	}
	/**
	 * 根据设备ID查询报告
	 * @param device_id --
	 *            设备ID
	 * @return
	 * @author GaoYa
	 * @date 2015-12-28 下午14:32:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionInfo> queryInfos(String device_id) {
		return infoDao.queryInfos(device_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInfo(String inspectionInfoIds, String print_count) {
		return infoDao.queryInfo(inspectionInfoIds, print_count);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryMessage(String infoIds) {
		return infoDao.queryMessage(infoIds);
	}
	public void del(HttpServletRequest request, String ids) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		// TODO Auto-generated method stub
		// //判断是否多个ID
		// if(ids.indexOf(",")!=-1){
		//			
		// String id[] =ids.split(",");
		//			
		// for(int i=0;i<id.length;i++){
		//				
		// infoDao.createSQLQuery("update TZSB_INSPECTION set data_status='2'
		// where id =?",id[i] ).executeUpdate();
		//				
		// }
		//			
		// }else{
		//		
		infoDao
				.createSQLQuery(
						"update  TZSB_INSPECTION_INFO  set data_status='99' where id = ?",
						ids).executeUpdate();
		// 写入日志
		logService.setLogs(ids, "报告作废", "业务报检时删除", user.getId(), user
				.getName(), new Date(), request.getRemoteAddr());

		// }

	}

	// 获取检验机构核准证号
	public String queryJGHZH(String advance_time) {
		return infoDao.queryJGHZH(advance_time);
	}
	//修改报告类型和检验时间
	@SuppressWarnings("unchecked")
	public void changeType(Map map) throws ParseException{
		String id=(String)map.get("id");
		String advance_time=(String)map.get("advance_time");
		String report_type=(String)map.get("report_type");
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		Date advance_date = sdf.parse(advance_time);
		java.sql.Date   formatDate=   new   java.sql.Date(advance_date.getTime());
		infoDao.createSQLQuery(
				"update tzsb_inspection_info set report_type=?,advance_time=? where id=?",
				report_type,formatDate,id).executeUpdate();
	}
	// 取消收费时，更新报检业务信息收费状态为“待收费”等
	public void updateInspectionInfo(String ids) {
		infoDao.updateInspectionInfo(ids);
	}
	
	// 外借报告时，修改报检业务信息“是否预借报告书”
	public void updateInspectionInfoIs_borrow(String id, String borrow_date) {
		infoDao.updateInspectionInfoIs_borrow(id, borrow_date);
	}
	
	// 根据设备类别（设备大类）查询报告书信息
	public List<ReportDTO> queryReports(String device_type) {
		return infoDao.queryReports(device_type);
	}
	
	// 查询设备检验日期
	public String queryInspection_date(String device_id) {
		return infoDao.queryInspection_date(device_id);
	}
	
	// 查询报告的检验日期
	public String queryInspectionDate(String ids) {
		return infoDao.queryInspectionDate(ids);
	}
	
	// 打印标签（合格证）时，标记打印状态为“已打印”
	public void updateInfo(String id) {
		infoDao.updateInfo(id);
	}
	
	// 获取业务流水号
	public synchronized  String getInspectionInfoSn(int cont)
			throws KhntException {
		String number = "";
		StringBuffer sql = new StringBuffer();
		sql.append("select nvl2(max(sn), lpad(to_number(max(sn)) +");
		sql.append(cont);
		sql.append(" , 6, 0), lpad(to_number(0) + ");
		sql.append(cont).append(" , 6, 0)) sn from tzsb_inspection_info ") ;
		List list = infoDao.createSQLQuery(sql.toString()).list();
		number = list.get(0).toString();
		//System.out.println("================number"+number);
		//number = obj[0].toString();
		return number;
	}
	
	// 获取所有机电检验部门人员信息
	public List<Map<String,Object>> queryEmployeeList() {
		return infoDao.queryEmployeeList();
	}
	
	
	/**
	 * 保存附件
	 * @param info_id -- 业务id
	 * @param uploadFiles -- 上传文件 
	 * 
	 * @return 
	 */
	public void saveAttachments(String info_id, String uploadFiles){
		if(StringUtil.isNotEmpty(uploadFiles)){
			String[] files = uploadFiles.replaceAll("/^,/", "").split(",");
			for(String file : files){
				if (StringUtil.isNotEmpty(file)) {
					attachmentManager.setAttachmentBusinessId(file, info_id);	// 保存附件
				}
			}
		}
	}
	
	/**
	 * 保存移动端上传的检验信息
	 * @param json
	 * @param map 
	 * @param request 
	 * @return 
	 * @throws KhntException
	 * @throws ParseException 
	 */
	public synchronized HashMap<String, Object> saveMobileInsp(JSONObject json, HashMap<String, Object> map,
			HttpServletRequest request) throws KhntException, ParseException{
		// 获取总表（检验信息）
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
				check_type = sumTable.getString("check_type");	// 检验类别（3：定检 2：监检）
				// 获取报告类别编号
				report_code = sumTable.getString("report_type");
				// 根据报告类别编号获取报告ID
				report_id = reportDao.queryReportId(report_code);
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
				
				inspection.setCheck_type(check_type);	// 检验类别（3：定检 2：监检）
				inspection.setFk_unit_id(fk_unit_id);	// 报检单位ID
				inspection.setCom_name(com_name);		// 报检单位名称
				inspection.setEnter_op(employee.getName());	// 录入人员
				inspection.setInspection_time(new Date());
				inspection.setData_status("0");			// 0：新建 1:使用 2：删除
				
				String device_id = sumTable.getString("device_id");
				info.setFk_tsjc_device_document_id(device_id);	// 设备ID
				if(StringUtil.isNotEmpty(device_id)){
					deviceDocument =  deviceDao.get(device_id);
					if(deviceDocument != null){
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
				info.setReport_type(report_id);			// 报告类型ID
				info.setCheck_op_id(check_op_id);		// 参检人员id
				info.setCheck_op_name(check_op_name);	// 参检人员姓名
				// 获取检验日期
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
						info.setAdvance_time(DateUtil.convertStringToDate("yyyy-MM-dd", inspection_date));
					}
				}
				info.setCheck_unit_id(check_unit_id);	// 检验部门ID
				info.setEnter_unit_id(check_unit_id);	// 录入部门ID
				info.setFee_status("0");	// 收费状态（0：初始）未启动流程前，收费状态都为初始状态不进入待收费流程
				info.setIs_back("1");		// 默认的状态为1，提交后变成0，退回也变成1
				info.setIs_copy("0");		// 是否是复制报告（0：非复制报告 1：复制报告）
				info.setIs_mobile("1");		// 是否移动检验（0：否 1：是）
				info.setEnter_op_id(employee.getId());		// 录入人ID
				info.setEnter_op_name(employee.getName());	// 录入人姓名
				info.setEnter_time(new Date());		// 录入日期
				info.setData_status("0");			// 数据状态（0：初始 1：正常使用 99：删除）未启动流程前，业务数据状态都为初始状态不进入正常使用流程
				
				inspectionDao.save(inspection);
				
				info.setInspection(inspection);
				infoDao.save(info);
				
				infoId = info.getId();
				logService.setLogs(infoId, "业务报检", "移动端电梯检验报告数据上传", userId, userName, new Date(), request.getRemoteAddr());
			}
			
			// 2、保存检验项目参数表信息
			Object listdatas = json.get("reportitemvalue");
			if(listdatas != null && !"".equals(listdatas.toString()) && !"null".equals(listdatas.toString())){
				// 再次上传数据时，删除初次上传的原始记录
				if(StringUtil.isNotEmpty(submitid)){
					reportItemRecordService.delRecordItems(submitid);	// 删除原始记录
				}

				JSONArray listdatas1 = JSONArray.fromObject(listdatas.toString());
				for (int i = 0; i < listdatas1.length(); i++) {
					Object reportItemObject = listdatas1.get(i);
					JSONObject itemValue = JSONObject.fromObject(reportItemObject.toString());
					// 保存原始记录检验项目记录表（新增）
					reportItemRecordDao.insertReportItemRecord(StringUtil.getUUID(), 
							report_id, itemValue.getString("key"), 
							itemValue.getString("value"), infoId, userId, userName);
					// 初次上传数据，才生成报告检验项目记录表
					// 再次上传数据时，不再生成报告检验项目记录表
					if (StringUtil.isEmpty(submitid)) {
						// 保存报告检验项目记录表（新增）
						reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
								report_id, itemValue.getString("key"),
								itemValue.getString("value"),infoId);
					}
				}
			}else{
				map.put("success", false);
				map.put("msg", "亲，系统未找到您上传数据中的检验项目表信息，请核实该部分数据是否已正确保存！");
				return map;
			}
			
			// 3、图片上传处理
			DefaultMultipartHttpServletRequest multipartRequest = (DefaultMultipartHttpServletRequest)request;
			//所有文件
			Map files = multipartRequest.getFileMap();
			if (files.size() > 0)
			{//获取监察指令文档的信息
				Iterator fileNames = multipartRequest.getFileNames();
				String fileName = (String)fileNames.next();
				MultipartFile file = (MultipartFile)files.get(fileName);
				log.debug((new StringBuilder("上传的文件：")).append(file.getOriginalFilename()).toString());
				//保存文档和记录内容
				//tzsbInspectionFlowManager.saveFeeCost(file.getInputStream(),id);
				System.out.println("file.getName()="+file.getName());
				System.out.println("file.getSize()="+file.getSize());   
				
				// 获取现场检验图片上传路径
				String importpath = Factory.getSysPara().getProperty("UPLOAD_INSPECTION_PIC_PATH");
				if (StringUtil.isEmpty(importpath)) {
					map.put("success", false);
					map.put("msg", "数据上传失败！系统未配置检验现场图片上传路径！");
					return map;
				}
				File importdir = new File(importpath);
				if (!importdir.exists())
					importdir.mkdirs();
				
				InputStream sbs = new ByteArrayInputStream(file.getBytes()); 
				// 保存图片和并存入数据库
				/*
				attachmentManager.setAttachmentBusinessId(file, entity.getId());	

				FileOutputStream fileOutput;coder.decode(path,"UTF-8");
				String filePathAndName = path+fileName+".xls";
				System.out.println("save_path:"+filePathAndName)
				path = java.net.URLDe;
				try {
					fileOutput = new FileOutputStream(filePathAndName);
					fileOutput.write(file.getBytes());
					fileOutput.flush();
					fileOutput.close();
					String filename ="xxxxxxxxxxxxxxxxxxx";
					int nums = shareService.addSharePic(id,filename);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				wrapper.put("success", true);
				*/
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "数据上传失败！错误代码："+e.toString()+e.getCause().getMessage());
			return map;
		}
		
		if(infoId!=null){
			map.put("success", true);
			map.put("id", infoId);	// 返回业务检验信息ID
		}else{
			map.put("success", false);
		}
		return map;
	}
	
	
	//保存快捷生成检验报告信息
		public HashMap<String, Object> savePlan(HttpServletRequest request,InspectionInfo info)
				throws Exception{
			//获取connection
	    	Connection conn = Factory.getDB().getConnetion();
	    
			
			HashMap<String, Object> result=new HashMap<String, Object>();
			
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			
			String org_id=user.getDepartment().getId() ;
			
			String ids = request.getParameter("ids");
			
			String temp[]=ids.split(",");
			int cont =0;
			for(int i=0;i<temp.length;i++){
				cont++;
				
				
				DeviceDocument doc = deviceDao.get(temp[i]);
				
				doc.setIs_cur_check("2");//状态切换成已报检
				
				deviceDao.save(doc);
				
				Inspection ins = new 	Inspection();
				
				ins.setCheck_type("3");
				ins.setData_status("1");
				
				ins.setInspection_time(info.getAdvance_time());
				
				//ins.setInspection_time( new SimpleDateFormat("yyyy-MM-dd").parse(info.getAdvance_time().toString()));
				
				info.setInspection(ins);
				
				
				
				
				info.setReport_com_name(doc.getCompany_name());
				
				
				info.setFk_tsjc_device_document_id(temp[i]);
				
				info.setIs_plan("1");//未领取
				info.setIs_back("1");//未回退
				info.setData_status("1");//状态为
				info.setFee_status("0");//代收费
				info.setCheck_unit_id(org_id);
				info.setEnter_unit_id(org_id);	// 录入部门ID
				
				if(info.getSn()==null){
		    		//获取业务流水号
		    		String sn = TS_Util.getSn(Integer.valueOf(cont),conn) ;
		    		info.setSn(sn);
		    	}
				inspectionDao.save(ins);
				
				infoDao.save(info);
				
			}
			
			result.put("success", true);
		
			return result;
					
		}

		//修改任务分配
			public HashMap<String, Object> changePlan(HttpServletRequest request,InspectionInfo info)
					throws Exception{
				
				
				HashMap<String, Object> result=new HashMap<String, Object>();
				
			
				
				String infoId = request.getParameter("infoId");
				
				String temp[]=infoId.split(",");
				
				for(int i=0;i<temp.length;i++){
					
					InspectionInfo  ins = infoDao.get(temp[i]);
					
					ins.setReport_type(info.getReport_type());
					ins.setAdvance_time(info.getAdvance_time());
					ins.setItem_op_id(info.getItem_op_id());
					ins.setItem_op_name(info.getItem_op_name());
					ins.setCheck_op_id(info.getCheck_op_id());
					ins.setCheck_op_name(info.getCheck_op_name());
					ins.setIs_plan("1");//未领取
					ins.setIs_back("1");//未回退
					infoDao.save(ins);
				
					
					
				}
				
				result.put("success", true);
			
				return result;
						
			}
			
			
			//回退任务分配
	public HashMap<String, Object> backPlan(String ids)
					throws Exception{
				
				
				HashMap<String, Object> result=new HashMap<String, Object>();
				
			
				
			
				
				String temp[]=ids.split(",");
				
				for(int i=0;i<temp.length;i++){
					
					InspectionInfo  ins = infoDao.get(temp[i]);
					
					ins.setIs_back("2");//已回退
					
					
					infoDao.save(ins);
				
				}
				
				result.put("success", true);
			
				return result;
						
			}
	
	
	//确定任务分配
	public HashMap<String, Object> subFlow(HttpServletRequest request,String infoId)
 throws Exception {

		HashMap<String, Object> result = new HashMap<String, Object>();

		HashMap<String, String> map = new HashMap<String, String>();

		String temp[] = infoId.split(",");

		String flowId = baseFlowConfigService.getFlowCY();

		if (flowId == null) {
			result.put("success", false);
			result.put("message", "没有找到对应的流程，请检查配置！");
			throw new KhntException("没有找到对应的流程，请检查配置！");
		} else {

			for (int i = 0; i < temp.length; i++) {

				InspectionInfo info = infoDao.get(temp[i]);
				info.setEnter_op_id(info.getItem_op_id());
				info.setEnter_op_name(info.getItem_op_name());
				info.setIs_plan("2");// 已领取

				infoDao.save(info);

				map.put("infoId", temp[i]);
				map.put("flowId", flowId);
				map.put("type", "1");// 1 表示电脑启动 2表示 原始记录启动

				// 先判断业务是否已经启动了流程
				// 业务流程表有数据就说明已经启动了流程，不再重新启动流程，没数据就启动流程
				String process_id = inspectionDao.getProcess(temp[i]);
				if (StringUtil.isEmpty(process_id)) {
					// 启动流程
					StarFlowProcess(map, request);
				}
			}
		}

		result.put("success", true);
		return result;

	}
	/**
	 * 启动流程
	 * @param infoId 业务id
	 * @param flowId 流程id
	 * @return
	 */
	public void StarFlowProcess(HashMap<String,String> map,HttpServletRequest request) {
		try{
		//业务ID
		String infoId = map.get("infoId");
		
		
		String flowId= map.get("flowId");
		String type = map.get("type");
		
		InspectionInfo  info = infoDao.get(infoId);
		
		Map<String, Object> Param= new HashMap<String, Object>();
		
		//判断是否已经启动流程
		if(!"2".equals(info.getIs_flow())){
		
			//获取流程名称
			String flow_name = flowDefManager.get(flowId).getFlowname();
			
			String flow_type =  flowDefManager.get(flowId).getFlowtype();
			//流程业务ID
			Param.put(FlowExtWorktaskParam.SERVICE_ID, infoId);
			//业务标题
			Param.put(FlowExtWorktaskParam.SERVICE_TITLE,flow_name);
			//流程ID
			Param.put(FlowExtWorktaskParam.FLOW_DEFINITION_ID, flowId);
			//第一个环节任务处理方式
			Param.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, true);
	
			Param.put(FlowExtWorktaskParam.SERVICE_TYPE, flow_type);
			
			
			JSONObject dataBus = new JSONObject();
			//數據總線獲取的指定下一步操作人
			
			
			
//			JSONArray  pts = new JSONArray();
//			JSONObject pt = new JSONObject();
//			pt.put("id",info.getItem_op_id());
//			pt.put("name", info.getItem_op_name());
//			pts.put(pt);
//		
//			dataBus.put("paticipator", pts);
//	
//			Param.put(FlowExtWorktaskParam.DATA_BUS, dataBus);
		
			
			String op_conclusion="";
			
			//启动流程
			Map<String, Object> flowMap=flowExtManager.startFlowProcess(Param);
			//获取下一步流程步骤
			List<Activity> list = (List) flowMap.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);
			//流程ID插入业务表
			info.setFk_flow_index_id(flowId);
			//改变业务表状态 1 未进入流程 2 进入流程
			info.setIs_flow("2");
			
			info.setIs_plan("2");//已经领取
			
			if(type.equals("2")){
				info.setIs_report_input("2");	// 2：报告已录入
				info.setFee_status("1");		// 收费状态（1：待收费）启动流程后，收费状态更新为待收费状态，进入待收费流程

				info.setData_status("1");
				
				op_conclusion="将原始记录生成报告。";
			}else{
				op_conclusion="任务确认，进入报告录入环节";
			}
			
			
			info.setFlow_note_id(list.get(0).getActivityId());//流程状态 01 任务分配02 报告录入 03 报告审核 04 报告签发  05 打印报告 06领取报告 07报告归档
			
			//info.setFlow_note_num(map.get("status").toString());
			
			info.setFlow_note_name(list.get(0).getName());
			info.setIs_report_input("1");
			info.setIs_back("0");
			infoDao.save(info);
			
			
			
			//写入日志
			
			
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			
			String address = request.getRemoteAddr();
			
			//logService.setLogs(infoId, "检验任务生成", op_conclusion, user.getSysUser().getEmployee().getId(), user.getSysUser().getEmployee().getName(), new Date(), request.getRemoteAddr());
			logService.setLogs(infoId, "生成报告", op_conclusion, user.getId(), user.getName(), new Date(), request.getRemoteAddr());
			
		
		}
		
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}

	public void importPdf(HashMap<String, Object> paramMap, String filePath, HttpServletRequest request, java.lang.String report_sn) throws IOException{
		String now = new SimpleDateFormat("yyyyMMdd").format(new Date());
		InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
		String path=request.getSession().getServletContext().getRealPath("upload");
		String realPath = Factory.getWebRoot() + Factory.getSysPara().getProperty("attachmentPath");
		try{
			System.out.println("------------------------------服务器地址------"+realPath);
			String basePath = realPath;
			String folder =  now+"/"+report_sn;
			
			// 获取原始文件的后缀
			boolean inFolder = StringUtil.isNotEmpty(folder);
				
				if (inFolder)
					basePath += File.separator + folder;

				File upload = new File(basePath);
				if (!upload.exists()) {
					upload.mkdirs();
				}
				OutputStream out1 = new BufferedOutputStream(new FileOutputStream(realPath+"/"+now+"/"+report_sn+"/"+report_sn+".pdf"));
				
				byte[] b = new byte[1024];// 相当于我们的缓存
				int j;
				while ((j = inputStream.read(b)) > 0) {
						// 将缓存中的数据写到客户端的内存
						out1.write(b, 0, j);
					}
				inputStream.close();
				out1.flush();
				out1.close();
			
		} catch (IOException e) {
			log.error("尝试在磁盘上创建文件失败！" + e.getMessage());
			LogUtil.logError(log, e);
			throw new KhntException("保存文件失败！");
		}
		
	}

	public void expPdfFlag(String id, String date) throws Exception {
		try{
		InspectionInfo info = infoDao.get(id);
		info.setExport_pdf(date);
		infoDao.save(info);
	
		//写入日志			
		CurrentSessionUser user = SecurityUtil.getSecurityUser();		
		//logService.setLogs(infoId, "检验任务生成", op_conclusion, user.getSysUser().getEmployee().getId(), user.getSysUser().getEmployee().getName(), new Date(), request.getRemoteAddr());
		//logService.setLogs(id, "报告盖章", "报告盖章成功", user.getId(), user.getName(), new Date(), "");
		sysDzyzLogService.setDzyzLogs(id, "报告盖章", "报告盖章成功", user.getId(), user.getName(), new Date(), "");
		}catch (IOException e) {
			log.error("报告盖章失败！" + e.getMessage());
			LogUtil.logError(log, e);
			throw new KhntException("保存文件失败！");
		}
	}
	
	// 撤销盖章
	public HashMap<String, Object> backs(HttpServletRequest request) throws Exception {
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		User user = (User) curUser.getSysUser();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String ids = request.getParameter("ids");
		try {
			String temp[] = ids.split(",");
			for (int i = 0; i < temp.length; i++) {
				// 1、清空报告导出pdf路径
				InspectionInfo info = infoDao.get(temp[i]);
				info.setExport_pdf("");
				infoDao.save(info);

				// 2、记录日志
				logService.setLogs(info.getId(), "撤销盖章", "撤销报告电子盖章", user.getId(), user.getName(), new Date(),
						request.getRemoteAddr());
			}
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "请求超时，请稍后再试！");
		}
		
		return map;
	}

	/**
	 * pdf上传盖章后转swf
	 * author pingZhou
	 * @param pdfpath
	 * @param swfPath
	 * @param swfName
	 * @throws KhntException
	 */
	public void pdf2Swf(String pdfpath,String swfPath,String swfName)throws KhntException {
		
		InspectionInfo info = null;
		String hql = "from InspectionInfo i where i.report_sn = ? and i.data_status<>'99'";
		List<InspectionInfo> list = inspectionDao.createQuery(hql, swfName).list();
		if(list.size()>0){
			info = list.get(0);
		}
		if(info!=null
				&&!"0".equals(info.getTo_swf())
				&&!"1".equals(info.getTo_swf())){
			info.setTo_swf("0");
			String basePath = Factory.getWebRoot()+"upload/";
			//文件原始保存地址
			File sourceFile = new File(basePath+pdfpath);
			
			File pdfFile = new File(basePath+swfPath+"/"+swfName+".swf");
			//临时生成的swf文件，当swf上传到pdf后改文件会被删除
			String temp_swfFile= pdfFile.getPath();
			//临时文件名
			//String exePath = "D:/work/pdf2swf.exe";
			//软件的位置改成相对位置，webaap根目录下面
			String exePath = new File(Factory.getWebRoot()+"pdf2swf.exe").getPath();
			
			//文字pdf转图片pdf
			pdfToImageToPdf(sourceFile.getPath(),basePath+swfPath+"/"+swfName+"_b.pdf");
			
			File sourceFile1 = new File(basePath+swfPath+"/"+swfName+"_b.pdf");
			
			pdf2swf(sourceFile1,temp_swfFile,exePath);
			
			info.setTo_swf("1");
			inspectionDao.save(info);
		}
		
		
	}
	//流程启动组装流程参数
		public Map<String, Object> setFlowParaMap(String service_id 
				, String service_type, String service_title, String flow_id
				, boolean current_user_task ){
			Map<String, Object> Param= new HashMap<String, Object>();
			//流程业务ID
			Param.put(FlowExtWorktaskParam.SERVICE_ID, service_id);
			//流程编码
			Param.put(FlowExtWorktaskParam.SERVICE_TYPE, service_type);
			//流程标题
			Param.put(FlowExtWorktaskParam.SERVICE_TITLE,service_title);
			//流程ID
			Param.put(FlowExtWorktaskParam.FLOW_DEFINITION_ID, flow_id);
			
			Param.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, current_user_task);
			return Param ;
		}
	/**
	 * pdf转swf
	 * author pingZhou
	 * @param pdfFile
	 * @param swfFile
	 * @param exePath
	 */
	public void pdf2swf(File pdfFile,String swfFile,String exePath){
		try { 
			 	
				PDF2SWFUtil.pdf2swf(pdfFile.getPath(), swfFile ,exePath);
				 
			 }catch (IOException e){ 
				
				e.printStackTrace(); 
			}
     	
	}
	
	/**
	 * 批处理所有原来报告pdf文件，转swf
	 * author pingZhou
	 * @param pdfFold
	 * @param flag 
	 * @throws Exception
	 */
	public void setPdf2Swf(String pdfFold, java.lang.String flag) throws Exception {
		String basePath = Factory.getWebRoot()+"upload/";
		//文件原始保存地址
		File sourceFile =  null;
		if(pdfFold!=null){
			sourceFile = new File(basePath+pdfFold);
			
		}else{
			sourceFile = new File(basePath);
		}
		 String dataFileTempDir = sourceFile.getPath();
		 getFiles(dataFileTempDir,flag);
		
	}
	
	/**
	 * 循环处理所有pdf文件，转图片pdf，再转swf
	 * author pingZhou
	 * @param filePath
	 * @throws Exception
	 */
    public void getFiles(String filePath,String flag) throws Exception {

        File root = new File(filePath);
        if (!root.exists()) {
        	log.info(filePath + " not exist!");
        } else {
            File[] files = root.listFiles();
           // Arrays.sort(files, new FileTest.CompratorByLastModified());  
            for (File file : files) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(),flag);
                } else {
                	if(!file.getName().contains("_b")&&file.getName().endsWith(".pdf")){
                		if(flag==null){
	                		//文字pdf转图片pdf
	                		pdfToImageToPdf(filePath+"/"+file.getName(),filePath+"/"+file.getName().substring(0,file.getName().length()-4)+"_b.pdf");
                		}
                		//pdf转swf
	    				PDF2SWFUtil.pdf2swf(filePath+"/"+file.getName().substring(0,file.getName().length()-4)+"_b.pdf", 
	    						filePath+"/"+file.getName().substring(0,file.getName().length()-4)+".swf" ,
	    						new File(Factory.getWebRoot()+"pdf2swf.exe").getPath());
    				
                	}
                    //logger.info("目录:" + filePath + "文件全路径:" + file.getAbsolutePath());
                	System.out.println(file.getName()+"----------------"+file.length()/1024+"KB---------最后修改日期："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date(file.lastModified()))));
                   
                }
            }
        }
    }
    
    /**
	 * 获取报告流程ID
	 * 
	 * @param device_big_class --
	 *            设备类别代码
	 * @param check_type --
	 *            检验类别
	 * @param org_id --
	 *            部门ID
	 * @param report_id --
	 *            报告类型ID
	 * @return
	 * @author GaoYa
	 * @date 2017-07-20 下午15:56:00
	 */
	public String queryFlowId(String device_big_class, String check_type, String org_id, String report_id) {
		return infoDao.queryFlowId(device_big_class, check_type, org_id, report_id);
	}
	
    /**
     * pdf转图片pdf
     * author pingZhou
     * @param oldpath
     * @param newpath
     */
    public  void pdfToImageToPdf(String oldpath,String newpath){
        File file = new File(oldpath);
        Document docOut = new Document();
        docOut.setMargins(0, 0, 0, 0);
        try {
            FileOutputStream os = new FileOutputStream(newpath);
            PdfWriter.getInstance(docOut, os);
            docOut.open();
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for(int i=0; i<pageCount; i++){
                long t1 = System.currentTimeMillis();
                ByteArrayOutputStream bb = new ByteArrayOutputStream();
                BufferedImage image = renderer.renderImage(i, 1.25f);   //第二个参数越大生成图片分辨率越高。
                long t2 = System.currentTimeMillis();
                ImageIO.write(image, "jpg",bb );
                long t3 = System.currentTimeMillis();
                Image jpg = Image.getInstance(bb.toByteArray());
                long t4 = System.currentTimeMillis();
                jpg.scalePercent(80.0f);       //此处百分比与前面的分辨率参数相乘结果为1，则可保证等比输出。
                jpg.setAlignment(Image.ALIGN_CENTER);
                docOut.add(jpg);
                long t5 = System.currentTimeMillis();
                System.out.println((t2 - t1) + "***" + (t3 - t2) + "***" + (t4 - t3) + "***" + (t5 - t4));
            }
            docOut.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
   	 * 修改报告收费金额
   	 * 
   	 * @param request
   	 * 
   	 * @return
   	 * @author GaoYa
   	 * @date 2017-11-30 上午11:08:00
   	 */
 	public HashMap<String, Object> modifyMoneys(HttpServletRequest request) throws Exception {
 		CurrentSessionUser user = SecurityUtil.getSecurityUser();
 		HashMap<String, Object> map = new HashMap<String, Object>();
 		String ids = request.getParameter("info_ids");
 		String money_str = request.getParameter("money");
 		try {
 			String temp[] = ids.split(",");
 			for (int i = 0; i < temp.length; i++) {
 				InspectionInfo inspectionInfo = infoDao.get(temp[i]);
 				double money = 0.00;
 				if(money_str != null){
 					if(!"".equals(money_str.trim())){
 						money = Double.parseDouble(money_str.trim());
 					}
 				}
 				inspectionInfo.setAdvance_fees(money);				
 				inspectionInfo.setLast_mdy_time(new Date());	
 				infoDao.save(inspectionInfo);
 				
 				logService.setLogs(temp[i], "修改金额", "修改报告收费金额为：" + money, user.getId(), user
 						.getName(), new Date(), request.getRemoteAddr());
 			}
 			map.put("success", true);
 		} catch (Exception e) {
 			e.printStackTrace();
 			map.put("success", false);
 			map.put("msg", "请求超时，请稍后再试！");
 		}
 		return map;
 	}


	public List<InspectionInfo> getInfoListByIds(String infos) {
		List<InspectionInfo> list=new ArrayList<InspectionInfo>();
		if(StringUtil.isNotEmpty(infos))
		{
			String [] ids=infos.split(",");
			for(String id:ids)
			{
				InspectionInfo info=this.get(id);
				list.add(info);
			}
		}
		return list;
	}
	/**
	 *  提交审核自动提交流程
	 * @param request -- 请求对象
	 * @param reqParamsMap -- 请求参数集合
	 *
	 * @return
	 * @throws
	 * @author GaoYa
	 * @since 2018-08-07 10:41:00
	 */
	public HashMap<String, Object> subFlow(HttpServletRequest request, Map<String, String> reqParamsMap,
										   InspectionInfo info) {
		// 返回参数集合
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 获取提交流程所需参数
			Map<String, Object> flow_params_map = tzsbWorkFlowService.getInfoFlowParams(info.getId(),
					info.getFlow_note_name(), info.getEnter_op_id());
			// 获取并组装流程下一步操作人
			net.sf.json.JSONArray personArray = new net.sf.json.JSONArray();
			net.sf.json.JSONObject checkObj = new net.sf.json.JSONObject();
			checkObj.put("id", reqParamsMap.get("NEXT_OP_ID"));
			checkObj.put("name", reqParamsMap.get("NEXT_OP_NAME"));
			personArray.put(checkObj);
			flow_params_map.put("personArray", personArray);
			// 提交流程
			tzsbWorkFlowService.subFlowProcess(flow_params_map, request);

			// 设置返回参数
			resultMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("success", false);
			resultMap.put("msg", "提交流程失败：业务ID：" + info.getId());
			throw new KhntException("提交流程失败：业务ID：" + info.getId());
		}
		resultMap.put("infoId", info.getId());
		return resultMap;
	}
	/**
	 * 罐车检验报告领取提醒
	 * 
	 * 常压罐车和汽车罐车定检报告，当报告书签发后，给报告安全管理人员的联系电话推短信提醒
	 * 
	 * @param request
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2018-03-19 下午17:00:00
	 */
	public void gcReportDrawCheck() {
		try {
			
			// 以手机号码为key，通知内容之报告编号集合
			Map<String, String> send_sn_map = new HashMap<String, String>();
			// 以手机号码为key，通知内容之车牌号集合
			Map<String, String> send_num_map = new HashMap<String, String>();
			// 获取已签发待领取的报告信息(排除已提醒过的报告)
			List<String> infoList = infoDao.getGcInfoIds();
			for (String info_id : infoList) {				
				InspectionInfo info = infoDao.get(info_id);
				Report report = reportDao.get(info.getReport_type());
				// 签发后是否发送消息提醒（1：微信 2：短信 3：微信和短信 0：不发送 ）
				if (StringUtil.isNotEmpty(report.getIssue_msg_type())) {
					if (!"0".equals(report.getIssue_msg_type())) {
						String item_names = Factory.getSysPara().getProperty("GC_REPORT_ITEM_NAMES");
						if (StringUtil.isEmpty(item_names)) {
							item_names = Constant.GC_REPORT_ITEM_NAMES;
						}
						// 获取发送内容
						Map<String, Object> content_map = reportItemValueDao.getGCContent(info_id,
								info.getReport_type(), item_names);
						// 获取发送目标号码
						String destNumber = content_map.get("SECURITY_TEL") + "";
						
						// 同一个号码，只发送一条提醒信息
						// 组合报告编号
						if(send_sn_map.containsKey(destNumber)){
							String send_sn = send_sn_map.get(destNumber);
							if(StringUtil.isNotEmpty(send_sn)){
								send_sn += "、" + info.getReport_sn();
							}
							send_sn_map.put(destNumber, send_sn);
						}else{
							send_sn_map.put(destNumber, info.getReport_sn());
						}
						// 组合车牌号
						String car_num = content_map.get("LADLE_CAR_NUMBER")+"";
						if(StringUtil.isEmpty(car_num)){
							car_num = content_map.get("INTERNAL_NUM")+"";
						}else{
							if("null".equals(car_num)){
								car_num = content_map.get("INTERNAL_NUM")+"";
							}
						}
						if(send_num_map.containsKey(destNumber)){
							String send_num = send_num_map.get(destNumber);
							if(StringUtil.isNotEmpty(send_num)){
								send_num += "、" + car_num;
							}
							send_num_map.put(destNumber, send_num);
						}else{
							send_num_map.put(destNumber, car_num);
						}
						
						// 标记报告领取提醒通知状态
						info.setIs_send_draw_msg("1");
						infoDao.save(info);
					}
				}
			}
			
			String is_draw = Factory.getSysPara().getProperty("GC_REPORT_DRAW");
			if(StringUtil.isEmpty(is_draw)){
				is_draw = "0";
			}
			if(!"0".equals(is_draw)){
				for (Iterator iterator = send_sn_map.keySet().iterator(); iterator.hasNext();) {
					String destNumber = (String) iterator.next();
					String content = Constant.getGcNoticeContent2(send_num_map.get(destNumber), send_sn_map.get(destNumber));
					if ("1".equals(is_draw)) {
						// 发送微信
						messageService.sendWxMsg(null, infoList.toString(), Constant.INSPECTION_CORPID,
								Constant.INSPECTION_PWD, content, destNumber.trim());
					} else if ("2".equals(is_draw)) {
						// 发送短信
						messageService.sendMoMsg(null, infoList.toString(), content, destNumber.trim());
					} else if ("3".equals(is_draw)) {
						// 发送微信和短信
						messageService.sendWxMsg(null, infoList.toString(), Constant.INSPECTION_CORPID,
								Constant.INSPECTION_PWD, content, destNumber.trim());
						messageService.sendMoMsg(null, infoList.toString(), content, destNumber.trim());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 智能一体机之打印报告流程自动提交任务
	 * 
	 * 智能一体机只进行报告打印和领取操作，所有涉及流程的工作交由管理系统统一自动提交处理
	 * 
	 * @param request
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2018-05-28 下午02:00:00
	 */
	public void doMachinePrintFlow() {
		try {
			// 1、获取智能一体机上流程待提交的业务
			List<MachineLog> infoList = machineLogDao.getLogsByAction(Constant.MACHINE_ACTION_PRINT_REPORT);
			if (infoList != null) {
				for (MachineLog machineLog : infoList) {
					if (StringUtil.isNotEmpty(machineLog.getBusiness_id())) {
						// 2、获取报告业务流程环节中所需参数
						Map<String, Object> flow_paras_map = infoDao.getInfoFlowParams(machineLog.getBusiness_id(),
								Constant.MACHINE_ACTION_PRINT_REPORT);
						if(!flow_paras_map.isEmpty()) {
							flow_paras_map.put("remote_addr", machineLog.getOp_ip());
							flow_paras_map.put("op_user_id",
									machineLog.getOp_user_id() != null ? machineLog.getOp_user_id() : "");
							flow_paras_map.put("op_user_name",
									machineLog.getOp_user_name() != null ? machineLog.getOp_user_name() : "");
							// 3、提交流程
							this.subFlowForMachine(flow_paras_map);
							// 4、更新智能一体机的业务流程提交状态
							machineLog.setFlow_status("1"); // 1：已提交
							machineLog.setFlow_sub_time(new Date());
							machineLogDao.save(machineLog);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 智能一体机之报告领取流程自动提交任务
	 * 
	 * 智能一体机只进行报告打印和领取操作，所有涉及流程的工作交由管理系统统一自动提交处理
	 * 
	 * @param request
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2018-05-29 上午09:38:00
	 */
	public void doMachineDrawFlow() {
		try {
			// 1、获取智能一体机上流程待提交的业务
			List<MachineLog> infoList = machineLogDao.getLogsByAction(Constant.MACHINE_ACTION_DRAW);
			if (infoList != null) {
				for (MachineLog machineLog : infoList) {
					if (StringUtil.isNotEmpty(machineLog.getBusiness_id())) {
						// 2、提交领取流程之前，先验证一体机上是否存在已打印的记录，存在才提交领取流程
						List<MachineLog> reportList = machineLogDao.getLogsByInfoId(machineLog.getBusiness_id(),
								Constant.MACHINE_ACTION_PRINT_REPORT);
						if (!reportList.isEmpty()) {
							// 3、获取报告业务流程环节中所需参数
							Map<String, Object> flow_paras_map = infoDao.getInfoFlowParams(machineLog.getBusiness_id(),
									Constant.MACHINE_ACTION_DRAW);
							if(!flow_paras_map.isEmpty()) {
								flow_paras_map.put("remote_addr", machineLog.getOp_ip());
								flow_paras_map.put("op_user_id",
										machineLog.getOp_user_id() != null ? machineLog.getOp_user_id() : "");
								flow_paras_map.put("op_user_name",
										machineLog.getOp_user_name() != null ? machineLog.getOp_user_name() : "");
								// 4、提交流程
								this.subFlowForMachine(flow_paras_map);
								// 5、更新智能一体机的业务流程提交状态
								machineLog.setFlow_status("1"); // 1：已提交
								machineLog.setFlow_sub_time(new Date());
								machineLogDao.save(machineLog);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 提交流程（用于智能一体机上的报告业务做自动提交流程之用）
	 * 
	 * @param map -- 业务流程相关参数集合
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized void subFlowForMachine(Map<String, Object> map) throws Exception {
		// 获取业务流程相关参数
		String infoId = map.get("info_id").toString();
		String acc_id = map.get("acc_id").toString();
		String flow_num = map.get("flow_num").toString();
		String op_user_id = map.get("op_user_id").toString();
		String op_user_name = map.get("op_user_name").toString();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(FlowExtWorktaskParam.ACTIVITY_ID, acc_id);
		paramMap.put(FlowExtWorktaskParam.SERVICE_ID, infoId);
		
		// 获取系统管理员账号信息
		String admin_user_id = Factory.getSysPara().getProperty("SYS_ADMIN_USER_ID");
		User admin_user = null;
		if (StringUtil.isNotEmpty(admin_user_id)) {
			admin_user = userDao.get(admin_user_id);
		}
		if(admin_user == null) {
			admin_user = userDao.get(Constant.SYS_ADMIN_USER_ID);
		}else {
			if(!"admin".equals(admin_user.getAccount())) {
				admin_user = userDao.get(Constant.SYS_ADMIN_USER_ID);
			}
		}
		
		// 获取系统角色信息
		Map<String, String> role = userDao.findUserRoles(admin_user.getId());
		
		BpmOrg bpmOrg = new BpmOrgImpl(admin_user.getOrg());
		BpmUser user = new BpmUserImpl(admin_user.getId(), admin_user.getName(), bpmOrg, bpmOrg, role);
		if(paramMap.containsKey(FlowExtWorktaskParam.BPM_USER)) {
			user = (BpmUser)map.get(FlowExtParam.BPM_USER);
		}
		paramMap.put(FlowExtParam.BPM_USER, user);

		InspectionInfo info = infoDao.get(infoId);
		if (info.getFlow_note_id().equals(flow_num)) {
			// 提交流程
			Map<String, Object> flowMap = flowExtManager.submitActivity(paramMap);
			// 获取下一步流程步骤
			List<Activity> list = (List) flowMap.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);
			// 更新报告业务当前流程数据
			info.setFlow_note_id(list.get(0).getActivityId());
			info.setFlow_note_name(list.get(0).getName());
			infoDao.save(info);
			
			try {
				// 记录日志
				String remote_addr = map.get("remote_addr").toString();
				String step_name = info.getFlow_note_name();
				String op_conclusion = "从" + step_name + "环节进入" + list.get(0).getName() + "环节。";
				String revise_remark = map.get("revise_remark") == null ? "" : map.get("revise_remark").toString();
				if (StringUtil.isNotEmpty(revise_remark)) {
					op_conclusion = op_conclusion + map.get("revise_remark").toString();
				}
				op_user_id = StringUtil.isNotEmpty(op_user_id) ? op_user_id : "";
				op_user_name = StringUtil.isNotEmpty(op_user_name) ? op_user_name : "智能一体机";
				logService.setLogs(infoId, step_name, op_conclusion, op_user_id, op_user_name, new Date(), remote_addr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getPageContent(HttpServletRequest request, String infoId, String pageCode, String modelType) throws Exception {
		//report||record
		InspectionInfo info = infoDao.get(infoId);
		Report reports  = reportDao.get(info.getReport_type());
		if("record".equals(modelType)) {
			if(reports.getRecordModelId()==null) {
				throw new KhntException("报告配置没有原始记录模板！");
			}
			String recordVersion = info.getRecordVersion();
			if(recordVersion==null) {
				return templateManager.getPageContentByVersion(request,reports.getRecordModelId(),pageCode,null,null);
			}else{
				JSONObject json = JSONObject.fromString(recordVersion);
				JSONObject pageJson = json.getJSONObject("page");
				int version = 1;
				if(pageJson.has("page"+pageCode)) {
					version = pageJson.getInt("page"+pageCode);
				}
				return templateManager.getPageContentByVersion(request,reports.getRecordModelId(),pageCode,json.getInt("version"),version);
			}
			
			
			
		}else {
			if(reports.getRtboxId()==null) {
				throw new KhntException("报告配置没有原始记录模板！");
			}
			String reportVersion = info.getReportVersion();
			if(reportVersion==null) {
				return templateManager.getPageContentByVersion(request,reports.getRtboxId(),pageCode,null,null);
			}else{
				JSONObject json = JSONObject.fromString(reportVersion);
				JSONObject pageJson = json.getJSONObject("page");
				int version = 1;
				if(pageJson.has("page"+pageCode)) {
					version = pageJson.getInt("page"+pageCode);
				}
				return templateManager.getPageContentByVersion(request,reports.getRtboxId(),pageCode,json.getInt("version"),version);
			}
			
			
		}
		
	}
	
	/**
	 * 设置报告模板版本号
	 * author pingZhou
	 * @param info
	 * @throws Exception
	 */
	public void setReportModelVersion(String infoId,String flag) throws Exception {
		InspectionInfo info = infoDao.get(infoId);
		Report reports  = reportDao.get(info.getReport_type());
		
		int maxV = inspectionVersionDao.getMaxVersion(infoId);
		InspectionVersion version = null;
		if("reset".equals(flag)) {
			version = new InspectionVersion();
		}
		//报告模板
		if(reports.getRtboxId()!=null&&StringUtil.isNotEmpty(reports.getRtboxId())&&!"reset".equals(flag)) {
			String reportVersion = templateManager.getVersionJson(reports.getRtboxId());
			info.setReportVersion(reportVersion);
			
		}else if("reset".equals(flag)) {
			String reportVersion = templateManager.getVersionJson(reports.getRtboxId());
			
			version.setOldReportVersion(info.getReportVersion());
			version.setNewReportVersion(reportVersion);
			
			info.setReportVersion(reportVersion);
		}
		
		//原始记录模板
		if(reports.getRecordModelId()!=null&&StringUtil.isNotEmpty(reports.getRecordModelId())&&!"reset".equals(flag)) {
			String recordVersion = templateManager.getVersionJson(reports.getRecordModelId());
			info.setRecordVersion(recordVersion);
					
		}else if("reset".equals(flag)) {
			String recordVersion = templateManager.getVersionJson(reports.getRecordModelId());
			

			version.setOldRecordVersion(info.getRecordVersion());
			version.setNewRecordVersion(recordVersion);
			
			info.setRecordVersion(recordVersion);
		}
		if("reset".equals(flag)) {
			//记录模板修改日志
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			version.setBusinessId(infoId);
			version.setOpTime(new Date());
			version.setOpUserId(user.getId());
			version.setOpUserName(user.getName());
			version.setVersion(maxV+1);
			
			inspectionVersionDao.save(version);
		}
		infoDao.save(info);

	}
	
	public String getDir(String id,String infoId,String code) {

		if (StringUtil.isNotEmpty(infoId)) {
			List<RtPersonDir> rpd = this.rtPersonDirDao.getDirByBusinessIdAndCode(infoId, code);
			if (rpd != null && !rpd.isEmpty()) {
				return rpd.get(0).getRtDirJson();
			}else {
				Report report = reportDao.get(infoDao.get(infoId).getReport_type());
				if(report != null && StringUtil.isNotEmpty(report.getRtDefaultDirJson())) {
					return report.getRtDefaultDirJson();
				}else {
					if(id==null||StringUtil.isEmpty(id)) {
						
						RtPage rtPage = rtPageDao.getNewByCode(code);
						return rtPage.getRtDirJson();
					}else {
						RtPage rtPage = rtPageDao.get(id);
						return rtPage.getRtDirJson();
					}
				}
			}
		}
		if(id==null||StringUtil.isEmpty(id)) {
			
			RtPage rtPage = rtPageDao.getNewByCode(code);
			return rtPage.getRtDirJson();
		}else {
			RtPage rtPage = rtPageDao.get(id);
			return rtPage.getRtDirJson();
		}
		
	}

	public Object getInfoObj(String infoId) {
		// TODO Auto-generated method stub
		return infoDao.getInfoObj(infoId);
	}
	
	public Object getInfoObj2(String infoId) {
		// TODO Auto-generated method stub
		return infoDao.getInfoObj2(infoId);
	}

	public Object queryInfoForPay(String infoId) {
		// TODO Auto-generated method stub
		return infoDao.queryInfoForPay(infoId);
	}

	/**
	 * 修改收费信息
	 * @param infoId
	 */
	public void updateReceivable(java.lang.String infoId) {
		String hql = "update from InspectionInfo info set info.receivable =info.advance_fees  where info.id = ? and (info.receivable is null or receivable=0 )";
		infoDao.createQuery(hql, infoId).executeUpdate();
	}

	/**
	 * 修改缴费信息
	 * @param infoId
	 * @param pay_type
	 * @param invoice_no
	 * @param advance_type
	 * @param pay_date
	 */
	public void updatePayInfo(java.lang.String infoId, java.lang.String pay_type, java.lang.String invoice_no,
			java.lang.String advance_type, Date pay_date) {

		if(advance_type==null) {
			String hql = "update from InspectionInfo info set info.fee_type=?,info.invoice_no=?,info.fee_status='2',invoice_date=?"
					+ "  where info.id = ? and (info.receivable is null or receivable=0 )";
			
			infoDao.createQuery(hql,pay_type,invoice_no,pay_date, infoId).executeUpdate();
			
			
		}else {
			String hql = "update from InspectionInfo info set info.fee_type=?,info.invoice_no=?,advance_type=?,info.fee_status='2',invoice_date=?"
					+ "  where info.id = ? and (info.receivable is null or receivable=0 )";
			infoDao.createQuery(hql,pay_type,invoice_no,advance_type,pay_date, infoId).executeUpdate();
			
			
		}
		
	}
}
	
