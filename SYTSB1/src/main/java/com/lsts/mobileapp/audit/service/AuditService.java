package com.lsts.mobileapp.audit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.bpm.core.bean.Activity;
import com.khnt.bpm.ext.service.FlowExtManager;
import com.khnt.bpm.ext.support.FlowExtWorktaskParam;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.rbac.impl.bean.Employee;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rtbox.config.bean.RtPage;
import com.khnt.rtbox.config.bean.RtPersonDir;
import com.khnt.rtbox.config.dao.RtPageDao;
import com.khnt.rtbox.config.dao.RtPersonDirDao;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.common.service.InspectionCommonService;
import com.lsts.constant.Constant;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.dao.DeviceDao;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.bean.ReportPageCheckInfo;
import com.lsts.inspection.bean.SysAutoIssueLog;
import com.lsts.inspection.dao.InspectionInfoDao;
import com.lsts.inspection.dao.ReportItemValueDao;
import com.lsts.inspection.dao.ReportPerDao;
import com.lsts.inspection.service.InspectionService;
import com.lsts.inspection.service.InspectionZZJDInfoService;
import com.lsts.inspection.service.ReportPageCheckInfoService;
import com.lsts.inspection.service.SysAutoIssueLogService;
import com.lsts.log.service.SysLogService;
import com.lsts.mobileapp.audit.dao.AuditDao;
import com.lsts.report.bean.Report;
import com.lsts.report.dao.ReportDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class AuditService extends EntityManageImpl<InspectionInfo, AuditDao>{

	@Autowired
	AuditDao auditDao;
	@Autowired
	InspectionInfoDao infoDao;
	@Autowired
	private RtPageDao rtPageDao;
	@Autowired
	private RtPersonDirDao rtPersonDirDao;
	@Autowired
	private ReportDao reportsDao;
	@Autowired
	private ReportPerDao reportPerDao;
	@Autowired
	DeviceDao deviceDao;
	@Autowired
	ReportItemValueDao reportItemValueDao;
	
	@Autowired
	InspectionCommonService inspectionCommonService;
	@Autowired
	ReportPageCheckInfoService reportPageCheckInfoService;
	@Autowired
	SysAutoIssueLogService sysAutoIssueLogService;
	@Autowired
	FlowExtManager flowExtManager;
	@Autowired
	SysLogService logService;
	@Autowired
	InspectionZZJDInfoService inspectionZZJDInfoService;
	
	@Autowired
	InspectionService inspectionService;
	
	
	public List<Object[]> getAuditMission() throws Exception
    {	
		
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		@SuppressWarnings("unchecked")
		List<Object[]> list = auditDao.createSQLQuery("select v.definition_id FLOWID,v.FLOWNAME, "
						+" v.ACTIVITY_ID,v.NAME,v.FUNCTION,v.NUM "
						+" from V_WORK_INFO_PLATFORM v "
						+" where v.HANDLER_ID='"+ user.getUserId()+"' and state='300' and v.function like '%audit%'").list();
		
		return list;
    }


	@SuppressWarnings("unchecked")
	public String loadReportDir(String id,String code) throws Exception {
		
		if (StringUtil.isNotEmpty(id)) {
			List<RtPersonDir> rpd = this.rtPersonDirDao.createQuery("from RtPersonDir where fkBusinessId=? and rtCode=?", id, code).list();
			if (rpd != null && !rpd.isEmpty()) {
				return rpd.get(0).getRtDirJson();
			}
		}
		List<RtPage> rtPage = this.rtPageDao.createQuery("from RtPage where rtCode=?", code).list();
		if (rtPage != null && !rtPage.isEmpty()) {
			return rtPage.get(0).getRtDirJson();
		}
		throw new Exception("没有找到目录,code:" + code);

	}
	
	public String getRtcode(String id) {
			InspectionInfo info = infoDao.get(id);
			Report reports = reportsDao.get(info.getReport_type());
			String code = reports.getRtboxCode();
			return code;
	}


	public List<InspectionInfo> pass(HttpServletRequest request, JSONArray businessArray, JSONObject formObject) throws Exception {
		List<InspectionInfo> infos = new ArrayList<InspectionInfo>();
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		Employee emp = ((User)user.getSysUser()).getEmployee();
		//审核日期验证
		Date op_date = null;
		try {
			op_date = DateUtil.convertStringToDate("yyyy-MM-dd", formObject.getString("op_time"));
			Date cur_date = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.getCurrentDateTime()) ;
			if(op_date.after(cur_date)){
				throw new Exception("日期逻辑错误，审核日期不能晚于当前日期，请重新选择！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception("审核日期必填！");
		}
			
		//批量验证加提交流程
		for(int i = 0,len = businessArray.length(); i < len; i++) 
		{
			JSONObject obj = businessArray.getJSONObject(i);
			String id = obj.getString("id");
			
			InspectionInfo info = infoDao.get(id);
			infos.add(info);
			//设备大类获取
			DeviceDocument ddoc = deviceDao.get(info.getFk_tsjc_device_document_id());
			String deviceBigClass = inspectionService.getBigClass(info,ddoc);
			if(StringUtil.isEmpty(deviceBigClass)) 
			{
				throw new Exception("为获取到设备大类！");
			}
			// 验证审核日期是否早于编制日期
			inspectionService.validateEnterTime(info,deviceBigClass,op_date);
			
			//
			// 报告模板配置信息
			String nextOp = "";		// 签发人ID（employee表主键）
			String nextOpName = "";	// 签发人姓名
			String rule = "";			// 实际分配规则（1、相同单位优先分配；2、量少优先分配；）
			boolean is_auto_issue = false;
			// 获取报告自动分配签发人start......
			// 2017-07-03修改，包括压力容器，所有设备均由系统进行自动分配签发人
			// 2017-10-13修改，增加西藏报告，不进行自动分配，由审核人手动选择签发人
			// 2017-12-12修改，增加新疆报告，不进行自动分配，由审核人手动选择签发人
			// 2018-03-29修改，增加九寨报告，不进行自动分配，由审核人手动选择签发人
			if("100069".equals(info.getCheck_unit_id()) 
					|| "100090".equals(info.getCheck_unit_id()) 
					|| "100091".equals(info.getCheck_unit_id()))
			{
				nextOp = formObject.getString("next_op_id");
				nextOpName = formObject.getString("next_op_name");
				if(StringUtil.isNotEmpty(nextOpName))
				{
					if(nextOpName.equals(info.getEnter_op_name()) || nextOpName.equals(user.getName()))
					{
						throw new Exception("您选择的签发人员（"+nextOpName+"）是已参与检验的人员，报告编号：" + info.getReport_sn() + "，请重新选择！");
					}
				}else{
					throw new Exception("请选择签发人再提交！");
				}
			}else{
				// 除压力容器之外其他设备均由系统进行自动随机分配签发人
				Map<String, Object> infoMaps = inspectionCommonService.autoIssue(info, deviceBigClass);
				if(Boolean.valueOf(String.valueOf(infoMaps.get("success"))))
				{
					nextOp = String.valueOf(infoMaps.get("next_op_id"));
					nextOpName = String.valueOf(infoMaps.get("next_op_name"));
					rule = infoMaps.get("rule")!=null?String.valueOf(infoMaps.get("rule")):"";
				}else{
					throw new Exception(String.valueOf(infoMaps.get("msg")));
				}
				is_auto_issue = true;
			}
			//必须remove以前的数据，不然获取的可能是数组
			formObject.remove("nextOp");
			formObject.put("nextOp", nextOp);
			formObject.remove("nextOpName");
			formObject.put("nextOpName", nextOpName);
			formObject.put("rule", rule);
			
			
			// 获取报告页单独审核信息，如果存在报告页待审核或者审核不通过的情况，本次审核不能保存并不能提交至下一环节
			// 报告页单独审核信息
			List<ReportPageCheckInfo> list = reportPageCheckInfoService.queryPageInfos(info.getId(),info.getReport_type());
			String returnMsg = "";
			for (ReportPageCheckInfo reportPageCheckInfo : list) {
				if (StringUtil.isNotEmpty(reportPageCheckInfo.getAudit_user_id())) {
					if (!"1".equals(reportPageCheckInfo.getData_status())) {
						String page_name = reportPageCheckInfo.getPage_name();
						String data_status = "0".equals(reportPageCheckInfo.getData_status()) ? "待审核" : "审核不通过";
						if (returnMsg.length() > 0) {
							returnMsg += ";\r\n";
						}
						returnMsg += "报告书编号：" + info.getReport_sn() + "，报告页【" + page_name + "】" + data_status;
					}
				}
			}
			if (StringUtil.isNotEmpty(returnMsg)) {
				returnMsg += "。\r\n请等待所有报告页审核通过后，再操作！";
				throw new Exception(returnMsg);
			}
			//验证单独审核页通过
			if(StringUtil.isEmpty(nextOp)){
				throw new Exception("当前无在岗签字人员，详询质量部028-86607892！");
			} else {
				if (is_auto_issue) {
					// 记录系统自动分配报告签发日志
					SysAutoIssueLog issueLog = new SysAutoIssueLog();
					issueLog.setBusiness_id(info.getId());
					issueLog.setReport_sn(info.getReport_sn());
					issueLog.setReport_com_name(info.getReport_com_name().trim());
					issueLog.setCheck_unit_id(info.getCheck_unit_id());
					issueLog.setDevice_type(deviceBigClass);
					issueLog.setOp_user_id(user.getId());
					issueLog.setOp_user_name(user.getName());
					issueLog.setOp_action(Constant.SYS_OP_ACTION_AUTO_ISSUE);
					issueLog.setOp_remark(Constant.SYS_OP_ACTION_AUTO_ISSUE + "至【" + nextOpName + "】");
					issueLog.setOp_time(DateUtil.convertStringToDate(Constant.ymdhmsDatePattern, DateUtil.getCurrentDateTime()));
					issueLog.setTo_user_id(nextOp);
					issueLog.setTo_user_name(nextOpName);
					issueLog.setIssue_type(rule);
					sysAutoIssueLogService.save(issueLog);
					// 标记报告为系统自动随机分配签发报告
					info.setIs_auto_issue("1");
				}
			}
			//更新审核人、审核时间
			info.setExamine_id(emp.getId());
			info.setExamine_name(emp.getName());
			info.setExamine_Date(op_date);
			
			reportItemValueDao.createSQLQuery("update tzsb_report_item_value set item_value='"+info.getExamine_name()+"' where fk_inspection_info_id='"+info.getId()+"' and item_name like 'INSPECTION_AUDIT_STR%' and (item_value ='' or item_value is null)").executeUpdate();
			reportPerDao.createSQLQuery("update  base_reports_per_audit set item_value='"+info.getExamine_name()+"',item_value_id='"+info.getExamine_id()+"' where  fk_inspection_info_id='"+id+"' and item_name like 'INSPECTION_AUDIT_STR%' and (item_value ='' or item_value is null)").executeUpdate();
			// 提交到下一步步骤
			flowProcess(request,info,formObject);
		}
		return infos;
	}
	public synchronized void flowProcess(HttpServletRequest request,InspectionInfo info,JSONObject formObject ) throws Exception{
		
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		//提交流程参数
		paramMap.put(FlowExtWorktaskParam.ACTIVITY_ID, info.getActivity_id());
		paramMap.put(FlowExtWorktaskParam.SERVICE_ID,info.getId());
		//下一步操作人员
		JSONObject databus = new JSONObject();
		JSONArray  persons = new JSONArray();
		if(StringUtil.isNotEmpty(formObject.getString("nextOp"))){
			JSONObject person = new JSONObject();
			person.put("id", formObject.getString("nextOp"));
			person.put("name", formObject.getString("nextOpName"));
			persons.put(person);
		}
		databus.put("paticipator", persons);
		paramMap.put(FlowExtWorktaskParam.DATA_BUS, databus);
		//提交流程
		Map<String, Object> flowMap = flowExtManager.submitActivity(paramMap);
		//获取下一步流程步骤
		@SuppressWarnings("unchecked")
		List<Activity> list = (List<Activity>) flowMap.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);
		
		String currentActivity = info.getFlow_note_name();
		String nextActivityId = list.get(0).getActivityId();
		String nextActivity = list.get(0).getName();
		//写入日志
		String op_conclusion = "从"+currentActivity+"环节进入"+nextActivity+"环节。";
		op_conclusion += StringUtil.isEmpty(formObject.getString("remark"))?"\"结论：通过\\n无\"":"结论：通过\\n"+StringUtil.isEmpty(formObject.getString("remark"));
		
		try {
			logService.setLogs(info.getId(), currentActivity, op_conclusion, user.getId(), user.getName(), new Date(), request.getRemoteAddr());
			if ("报告录入".equals(currentActivity)) {
				String to_user_name = "提交至" + ((JSONObject)persons.get(0)).getString("name");
				logService.setLogs(info.getId(), "提交报告审核", to_user_name, user.getId(), user.getName(), new Date(), request.getRemoteAddr());
			}
			if("报告审核".equals(currentActivity)){
				String to_user_name = "提交至" + ((JSONObject)persons.get(0)).getString("name");
				logService.setLogs(info.getId(), currentActivity+"提交", to_user_name, user.getId(), user.getName(), new Date(), request.getRemoteAddr());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		info.setActivity_id(list.get(0).getId());
		info.setFlow_note_id(nextActivityId);
		info.setFlow_note_name(nextActivity);
		info.setIs_back("0");//提交后状态变成0
		
	}


	public List<InspectionInfo> auditBack(HttpServletRequest request,JSONArray businessArray, JSONObject formObject) throws Exception {
		List<InspectionInfo> infos = new ArrayList<InspectionInfo>();
		for (int i = 0, len = businessArray.length();i<len; i++) {
			JSONObject obj = businessArray.getJSONObject(i);
			String id = obj.getString("id");
			
			InspectionInfo info = infoDao.get(id);
			infos.add(info);
			// 回退到上一步
			backProcess(request,info,formObject);
		}
		return infos;
	}
	private synchronized void backProcess(HttpServletRequest request,InspectionInfo info, JSONObject formObject) throws Exception {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		
		String backStep = formObject.getString("backStep");
		
		JSONObject databus = new JSONObject();
		if(StringUtil.isEmpty(backStep))
		{
			//默认退回录入
			databus.put("flag", "input");
		}else{
			databus.put("flag", backStep);
		}
		paramMap.put(FlowExtWorktaskParam.DATA_BUS, databus);
		
		paramMap.put(FlowExtWorktaskParam.ACTIVITY_ID, info.getActivity_id());
		
		Map<String, Object> flowMap = flowExtManager.returnedActivity(paramMap);
		
		//获取退回步骤Id
		@SuppressWarnings("unchecked")
		List<Activity> list = (List<Activity>) flowMap.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);
		
		String currentActivity = info.getFlow_note_name();
		String nextActivityId = list.get(0).getActivityId();
		String nextActivity = list.get(0).getName();
		
		info.setActivity_id(list.get(0).getId());
		info.setFlow_note_id(nextActivityId);
		info.setFlow_note_name(nextActivity);
		// 1：退回报检环节
		info.setIs_back("1");	
		if(nextActivity.indexOf("校核")!=-1) {
			info.setRecordFlow("1"); //重新设置为9
		}else if(nextActivity.indexOf("录入")!=-1) {
			info.setRecordFlow("9"); //退回待接收
			info.setReceiveStatus("0");
			info.setRecordHandleId(info.getEnter_op_id());
			info.setRecordHandleOp(info.getEnter_op_name());
			//后期发送消息
		}

		//写入日志
		String op_conclusion = "从【"+currentActivity+"】环节进入【"+nextActivity+"】环节。\n结论：不通过，"+formObject.getString("remark");
		User user = (User)SecurityUtil.getSecurityUser().getSysUser();
		
		logService.setLogs(info.getId(), currentActivity+"回退", op_conclusion, user.getId(), user.getName(), new Date(), request.getRemoteAddr());
	}
}
