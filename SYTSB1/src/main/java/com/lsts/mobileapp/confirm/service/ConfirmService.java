package com.lsts.mobileapp.confirm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.bpm.core.bean.Activity;
import com.khnt.bpm.core.engine.FlowEngine;
import com.khnt.bpm.ext.service.FlowExtManager;
import com.khnt.bpm.ext.support.FlowExtWorktaskParam;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.rbac.impl.bean.Employee;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rbac.impl.dao.EmployeeDao;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.StringUtil;
import com.lsts.common.service.InspectionCommonService;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.dao.DeviceDao;
import com.lsts.flow.service.BaseFlowConfigService;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.dao.InspectionInfoDao;
import com.lsts.inspection.service.InspectionService;
import com.lsts.inspection.service.ReportItemRecordService;
import com.lsts.inspection.service.ReportItemValueService;
import com.lsts.log.service.SysLogService;
import com.lsts.mobileapp.confirm.dao.ConfirmDao;
import com.lsts.mobileapp.input.bean.TzsbRecordLog;
import com.lsts.mobileapp.input.dao.TzsbRecordLogDao;
import com.lsts.report.bean.Report;
import com.lsts.report.dao.ReportDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.TS_Util;
@Service
public class ConfirmService extends EntityManageImpl<InspectionInfo, ConfirmDao>{

	@Autowired
	ConfirmDao confirmDao;
	@Autowired
	InspectionInfoDao infoDao;
	@Autowired
	DeviceDao deviceDao;
	@Autowired
	ReportDao reportDao;
	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	SysLogService logService;
	@Autowired
	FlowExtManager flowExtManager;
	@Autowired
	BaseFlowConfigService baseFlowConfigService;
	@Autowired
	InspectionCommonService inspectionCommonService;
	@Autowired
	ReportItemValueService reportItemValueService;
	@Autowired
	ReportItemRecordService reportItemRecordService;
	@Autowired
	TzsbRecordLogDao recordLogDao;
	@Autowired
	private InspectionService inspectionService;

	/**
	 * 校核提交启动流程、并提交审核、签发
	 */
	public List<InspectionInfo> confirm(HttpServletRequest request, JSONArray businesses, JSONObject formObject) throws Exception {
		
		User user = (User) SecurityUtil.getSecurityUser().getSysUser();
		Employee emp = user.getEmployee();
		
		List<InspectionInfo> infos = new ArrayList<InspectionInfo>();
		
		for (int i = 0,len = businesses.length(); i < len; i++) {
			JSONObject obj = businesses.getJSONObject(i);
			// 获取报告业务信息
			InspectionInfo info = infoDao.get(obj.getString("id"));
			infos.add(info);
			//校核人
			info.setRecordConfirmId(emp.getId());
			info.setRecordConfirmOp(emp.getName());
			
			//校核结论
			info.setIs_report_confirm(formObject.getString("conclusion"));
			info.setReport_confirm_remark(formObject.getString("remark"));
			//这里一定是通过，前面已经排除了不通过
			logService.setLogs(info.getId(), "原始记录校核", "校核通过", user.getId(),user.getName(), new Date(), request.getRemoteAddr());
			//现在校核环节，直接提交，如果是一级审核则提交签发，二级审核则提交审核
			this.flowToAuditOrSign(request,info,formObject,user);
			//不报错则，个人认为其实可以不加
			addRecordLog(request,info,formObject,user);
		}
			
		return infos;
	}

	
	
	
	public void flowToAuditOrSign(HttpServletRequest request,InspectionInfo info,JSONObject formObject,User user) throws Exception {
		//提交流程需要的参数
		Map<String,Object> paramMap = new HashMap<String,Object>();
		String activityId = "";
		if(StringUtil.isNotEmpty(info.getActivity_id())) 
		{
			activityId = info.getActivity_id();
		}
		else
		{
			@SuppressWarnings("unchecked")
			List<Object> oList = confirmDao.createSQLQuery("select a.id from flow_activity a,flow_process p,tzsb_inspection_info i where a.process = p.id and p.business_id = i.id and i.id = ? and a.state = '300'", info.getId()).list();
			activityId = oList.get(0).toString();
		}
		paramMap.put(FlowExtWorktaskParam.ACTIVITY_ID, activityId);
		paramMap.put(FlowExtWorktaskParam.SERVICE_ID,info.getId());
		
		Report report = reportDao.get(info.getReport_type());
		String checkFLow = report.getCheck_flow();
		String act  = "1".equals(checkFLow)?"签发":"审核";
		String nextOp="",nextOpName="";
		// 西藏、新疆、九寨报告，不进行自动分配，由审核人手动选择签发人
		if("100069".equals(info.getCheck_unit_id()) 
				|| "100090".equals(info.getCheck_unit_id()) 
				|| "100091".equals(info.getCheck_unit_id())
				|| "0".equals(checkFLow))
		{
			nextOp = formObject.getString("nextOp");
			
			nextOpName = formObject.getString("nextOpName");
		
			if(nextOpName.equals(info.getEnter_op_name()) || nextOpName.equals(user.getName()))
			{
				throw new Exception(act+"人员不能是参检人员，报告编号：" + info.getReport_sn());
			}
		}else if("1".equals(checkFLow)) {
			//1级,非西藏、新疆、九寨
			// 除压力容器之外其他设备均由系统进行自动随机分配签发人
			DeviceDocument ddoc = deviceDao.get(info.getFk_tsjc_device_document_id());
			String deviceBigClass = inspectionService.getBigClass(info,ddoc);
			if(StringUtil.isEmpty(deviceBigClass)) 
			{
				throw new Exception("为获取到设备大类！");
			}
			Map<String, Object> infoMaps = inspectionCommonService.autoIssue(info, deviceBigClass);
			if(Boolean.valueOf(String.valueOf(infoMaps.get("success"))))
			{
				nextOp = String.valueOf(infoMaps.get("next_op_id"));
				nextOpName = String.valueOf(infoMaps.get("next_op_name"));
			}else{
				throw new Exception("获取签发人员失败："+String.valueOf(infoMaps.get("msg")));
			}
		}
		if(StringUtil.isEmpty(nextOp)) {
			throw new Exception("获取"+act+"人员失败！");
		}
		JSONObject databus = new JSONObject();
		// 获取并组装流程下一步操作人，表单选的人
		JSONArray personArray = new JSONArray();
		JSONObject person = new JSONObject();
		person.put("id", nextOp);
		person.put("name", nextOpName);
		personArray.put(person);
		databus.put(FlowEngine.DATA_BUS_PARTICIPANT_KEY_DEFAULT, personArray);
		paramMap.put(FlowExtWorktaskParam.DATA_BUS, databus);
		// 提交流程
		flowProcess(request,paramMap,info,formObject,user);
		info.setRecordFlow("2");
	}
	public void flowProcess(HttpServletRequest request,Map<String,Object> flowParam,InspectionInfo info,JSONObject formObject,User user) throws Exception{
   			
		Map<String, Object> flowMap = flowExtManager.submitActivity(flowParam);
		//获取下一步流程步骤
		@SuppressWarnings("unchecked")
		List<Activity> list = (List<Activity>) flowMap.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);
		
		//取得环节名称
		String nodeName = info.getFlow_note_name();
		//准备操作说明
		String opRemark = "从【"+nodeName+"】环节进入【"+list.get(0).getName()+"】环节。";
	 	//页面提交的操作说明
		if(StringUtil.isNotEmpty(formObject.getString("remark"))) {
			opRemark += "\n说明："+formObject.getString("remark") ;
		}
		//修改业务表业务流程环节ID
		info.setActivity_id(list.get(0).getId());
		info.setFlow_note_id(list.get(0).getActivityId());
		info.setFlow_note_name(list.get(0).getName());	//修改业务表业务流程环节名称
		
		logService.setLogs(info.getId(), nodeName, opRemark,user.getId(), user.getName(),new Date(), request.getRemoteAddr(), "1");
	}
	
	
	
	/**
	 * 退回(录入)
	 * @param request
	 * @param businesses
	 * @param formData
	 * @return
	 * @throws Exception
	 */
	public List<InspectionInfo> failed(HttpServletRequest request, JSONArray businesses, JSONObject formObject) throws Exception {
		List<InspectionInfo> infos = new ArrayList<InspectionInfo>();
		User user = (User) SecurityUtil.getSecurityUser().getSysUser();
		Employee emp = user.getEmployee();
		for (int i = 0,len = businesses.length(); i < len; i++) {
			JSONObject obj = businesses.getJSONObject(i);
			// 获取报告业务信息
			InspectionInfo info = infoDao.get(obj.getString("id"));
			infos.add(info);
			//校核人
			info.setRecordConfirmId(emp.getId());
			info.setRecordConfirmOp(emp.getName());
			//校核结论
			info.setIs_report_confirm(formObject.getString("conclusion"));
			info.setReport_confirm_remark(formObject.getString("remark"));
			//这里一定是通过，前面已经排除了不通过
			logService.setLogs(info.getId(), "原始记录校核", "校核不通过", user.getId(),user.getName(), new Date(), request.getRemoteAddr());
			//现在校核环节，直接提交，如果是一级审核则提交签发，二级审核则提交审核
			this.backProcess(request,info,formObject);
		}
		return infos;
	}
	
	private void backProcess(HttpServletRequest request,InspectionInfo info,JSONObject formObject) throws Exception{
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			String activityId = null;
			if(StringUtil.isNotEmpty(info.getActivity_id())) 
			{
				activityId = info.getActivity_id();
			}
			else
			{
				try {
					@SuppressWarnings("unchecked")
					List<Object> oList = confirmDao.createSQLQuery("select a.id from flow_activity a,flow_process p,tzsb_inspection_info i where a.process = p.id and p.business_id = i.id and i.id = ? and a.state = '300'", info.getId()).list();
					activityId = oList.get(0).toString();
				}catch(Exception e) {
					
				}
			}
			if(StringUtil.isEmpty(activityId)) {
				//还是为空,这种情况一般不存在，有说明应该是测试数据，或之前没有启动流程的。
				info.setIs_back("1");	
				info.setRecordFlow("9"); //退回待接收
				info.setReceiveStatus("0");
				info.setRecordHandleId(info.getEnter_op_id());
				info.setRecordHandleOp(info.getEnter_op_name());
				return;
			}
			paramMap.put(FlowExtWorktaskParam.ACTIVITY_ID,activityId);
			
			Map<String, Object> resultMap = flowExtManager.returnedActivity(paramMap);
			//获取退回步骤Id
			@SuppressWarnings("unchecked")
			List<Activity> list = (List<Activity>) resultMap.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);
			
			String currentActivity = info.getFlow_note_name();
			String nextActivityId = list.get(0).getActivityId();
			String nextActivity = list.get(0).getName();
			
			info.setActivity_id(list.get(0).getId());
			info.setFlow_note_id(nextActivityId);
			info.setFlow_note_name(nextActivity);
			// 1：退回录入环节
			info.setIs_back("1");	
			info.setRecordFlow("9"); //退回待接收
			info.setReceiveStatus("0");
			info.setRecordHandleId(info.getEnter_op_id());
			info.setRecordHandleOp(info.getEnter_op_name());
			
	
			//写入日志
			String op_conclusion = "从【"+currentActivity+"】环节进入【"+nextActivity+"】环节。\n结论：不通过," + formObject.getString("remark");
			User user = (User)SecurityUtil.getSecurityUser().getSysUser();
			
			logService.setLogs(info.getId(), currentActivity+"回退", op_conclusion, user.getId(), user.getName(), new Date(), request.getRemoteAddr());
	}




	private void addRecordLog(HttpServletRequest request, InspectionInfo info,JSONObject formObject,User user) {
		String checkFlow = formObject.getString("check_flow");
		String act = ("1".equals(checkFlow)?"签发":"审核");
		try {
			// 记录日志
			TzsbRecordLog log  = new TzsbRecordLog();
			log.setBusiness_id(info.getId());
			log.setOp_action("原始记录提交"+act);
			log.setOp_ip(TS_Util.getIpAddress(request));
			log.setOp_remark("原始记录提交"+act);
			log.setOp_status("提交"+act);
			log.setOp_time(new Date());
			log.setOp_user_id(user.getId());
			log.setOp_user_name(user.getName());
			recordLogDao.save(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
