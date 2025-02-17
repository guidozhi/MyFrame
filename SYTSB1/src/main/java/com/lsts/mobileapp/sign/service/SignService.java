package com.lsts.mobileapp.sign.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.base.Factory;
import com.khnt.bpm.core.bean.Activity;
import com.khnt.bpm.ext.service.FlowExtManager;
import com.khnt.bpm.ext.support.FlowExtWorktaskParam;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.rbac.impl.bean.Employee;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rtbox.config.bean.RtDir;
import com.khnt.rtbox.config.bean.RtPersonDir;
import com.khnt.rtbox.config.dao.RtDirDao;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.advicenote.service.MessageService;
import com.lsts.common.service.InspectionCommonService;
import com.lsts.constant.Constant;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.dao.DeviceDao;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.dao.InspectionInfoDao;
import com.lsts.inspection.dao.ReportItemValueDao;
import com.lsts.inspection.service.InspectionService;
import com.lsts.inspection.service.ReportItemValueService;
import com.lsts.inspection.service.ReportPageCheckInfoService;
import com.lsts.inspection.service.SysAutoIssueLogService;
import com.lsts.log.service.SysLogService;
import com.lsts.mobileapp.sign.dao.SignDao;
import com.lsts.report.bean.Report;
import com.lsts.report.dao.ReportDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
public class SignService extends EntityManageImpl<InspectionInfo, SignDao>{

	@Autowired
	SignDao signDao;
	@Autowired
	private RtDirDao rtDirDao;
	@Autowired
	private InspectionInfoDao infoDao;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private ReportDao reportDao;
	@Autowired
	ReportItemValueDao reportItemValueDao;
	@Autowired
	ReportItemValueService reportItemValueService;
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
	InspectionService inspectionService;
	@Autowired
	MessageService messageService;
	
	
	public List<Object[]> getSignMission() throws Exception
    {	
		
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		@SuppressWarnings("unchecked")
		List<Object[]> list = signDao.createSQLQuery("select v.definition_id FLOWID,v.FLOWNAME, "
						+" v.ACTIVITY_ID,v.NAME,v.FUNCTION,v.NUM "
						+" from V_WORK_INFO_PLATFORM v "
						+" where v.HANDLER_ID='"+ user.getUserId()+"' and state='300' and v.function like '%sign%'").list();
		
		return list;
    }


	@SuppressWarnings("unchecked")
	public String loadReportDir(String id,String code) throws Exception {
		if (StringUtil.isNotEmpty(id)) {
			List<RtPersonDir> rpd = this.rtDirDao.createQuery("from RtPersonDir where fkBusinessId=? and rtCode=?", id, code).list();
			if (rpd != null && !rpd.isEmpty()) {
				return rpd.get(0).getRtDirJson();
			}
		}
		List<RtDir> rd = this.rtDirDao.createQuery("from RtDir where rtCode=?", code).list();
		if (rd != null && !rd.isEmpty()) {
			return rd.get(0).getRtDirJson();
		}
		throw new Exception("没有找到目录,code:" + code);

	}


	/**
	 * 签发通过进入下一步
	 * @param request
	 * @param businessArray
	 * @param formData
	 * @return
	 * @throws Exception
	 */
	public List<InspectionInfo> pass(HttpServletRequest request, JSONArray businessArray, JSONObject formData) throws Exception {
		List<InspectionInfo> infos = new ArrayList<InspectionInfo>();
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		Employee emp = ((User)user.getSysUser()).getEmployee();
		//签发日期验证
		Date op_date = null;
		try {
			op_date = DateUtil.convertStringToDate("yyyy-MM-dd", formData.getString("op_time"));
			Date cur_date = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.getCurrentDateTime()) ;
			if(op_date.after(cur_date)){
				throw new Exception("日期逻辑错误，签发日期不能晚于当前日期，请重新选择！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception("签发日期必填！");
		}
		
		Map<String, String> send_sn_map = new HashMap<String, String>();	// 报告领取通知内容之报告编号集合
		Map<String, String> send_num_map = new HashMap<String, String>();	// 报告领取通知内容之车牌号集合
		//批量验证加提交流程
		for(int i = 0,len = businessArray.length(); i < len; i++) 
		{
			JSONObject obj = businessArray.getJSONObject(i);
			InspectionInfo info = infoDao.get(obj.getString("id"));
			infos.add(info);
			// 验证签发日期是否早于审核日期
			Date axamineDate = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.format(info.getExamine_Date(), "yyyy-MM-dd"));
			if (op_date.before(axamineDate)) {
				throw new Exception(info.getReport_sn() + "签发日期不能早于审核日期，请检查！");
			}
			
			//设备大类获取
			DeviceDocument ddoc = deviceDao.get(info.getFk_tsjc_device_document_id());
			String deviceBigClass = inspectionService.getBigClass(info,ddoc);
			if(StringUtil.isEmpty(deviceBigClass)) 
			{
				throw new Exception("未获取到设备大类！");
			}
			
			
			obj.put("remark", "结论：通过\n" + formData.getString("remark"));
			obj.put("flag", "0");// 如果是0，就不用指定下一步操作人
			// 返写业务信息（返写审批人信息等）
			info.setIssue_id(emp.getId());
			info.setIssue_name(emp.getName());
			info.setIssue_Date(op_date);
			info.setIssue_Date2(new Date());	// 签发时间（实际签发日期，不显示于报告，仅用于数据统计）
			info.setIs_issue("1"); 		// 是否已签发（默认否，0：否 1：是）
			info.setSend_status("0");	// 数据当前是否已传输（默认否，0：否 1：是）	
			// 返写设备信息
			inspectionService.dealDeviceInfo(info,ddoc);	// 签发通过时，返写设备信息
			// 将报告流程提交至下一步环节
			flowProcess(request,info,formData);
			
			//报告
			Report report = reportDao.get(info.getReport_type());
			// 2018-03-19根据张展彬要求，改即时单份推送为一批多份推送提醒,此处进行发送统计
			if (StringUtil.isNotEmpty(report.getIssue_msg_type())) {
				if (!"0".equals(report.getIssue_msg_type())) {
					String item_names = Factory.getSysPara().getProperty("GC_REPORT_ITEM_NAMES");
					if (StringUtil.isEmpty(item_names)) {
						item_names = Constant.GC_REPORT_ITEM_NAMES;
					}
					// 获取发送内容
					Map<String, Object> content_map = reportItemValueService.getGCContent(info.getId(),report.getId(), item_names);
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
				}
			}
		}
		
		// 常压罐车和汽车罐车定检报告，当报告书签发后，给报告安全管理人员的联系电话推短信提醒
		// 发送报告领取提醒start...
		String is_draw = Factory.getSysPara().getProperty("GC_REPORT_DRAW");
		if(StringUtil.isEmpty(is_draw)){
			is_draw = "0";
		}
		// 是否发送消息提醒（1：微信 2：短信 3：微信和短信 0：不发送 ）
		if(!"0".equals(is_draw)){
			for (Iterator<String> iterator = send_sn_map.keySet().iterator(); iterator.hasNext();) {
				String destNumber = iterator.next();
				String content = Constant.getGcNoticeContent2(send_num_map.get(destNumber), send_sn_map.get(destNumber));
				if ("1".equals(is_draw)) {
					// 发送微信
					messageService.sendWxMsg(null, infos.get(0).getId(), Constant.INSPECTION_CORPID,
							Constant.INSPECTION_PWD, content, destNumber.trim());
				} else if ("2".equals(is_draw)) {
					// 发送短信
					messageService.sendMoMsg(null, infos.get(0).getId(), content, destNumber.trim());
				} else if ("3".equals(is_draw)) {
					// 发送微信和短信
					messageService.sendWxMsg(null, infos.get(0).getId(), Constant.INSPECTION_CORPID,
							Constant.INSPECTION_PWD, content, destNumber.trim());
					messageService.sendMoMsg(null, infos.get(0).getId(), content, destNumber.trim());
				}
			}
		}
		// 发送报告领取提醒end...
		return infos;
	}
	/**
	  *  签发送打印
	 * @param request
	 * @param info
	 * @param formObject
	 * @throws Exception
	 */
	public synchronized void flowProcess(HttpServletRequest request,InspectionInfo info,JSONObject formObject ) throws Exception{
		
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		//提交流程参数
		paramMap.put(FlowExtWorktaskParam.ACTIVITY_ID, info.getActivity_id());
		paramMap.put(FlowExtWorktaskParam.SERVICE_ID,info.getId());
		//提交流程
		Map<String, Object> flowMap = flowExtManager.submitActivity(paramMap);
		//获取下一步流程步骤
		@SuppressWarnings("unchecked")
		List<Activity> list = (List<Activity>) flowMap.get(FlowExtWorktaskParam.RESULT_ACTIVITY_LIST);
		
		String currentActivity = info.getFlow_note_name();
		String nextActivityId = list.get(0).getActivityId();
		String nextActivity = list.get(0).getName();
		//写入日志
		String op_conclusion = "从【"+currentActivity+"】环节进入【"+nextActivity+"】环节。";
		op_conclusion += "结论：通过\n"+formObject.getString("remark");
		try {
			logService.setLogs(info.getId(), currentActivity, op_conclusion, user.getId(), user.getName(), new Date(), request.getRemoteAddr());
		} catch (Exception e) {
			e.printStackTrace();
		}
		info.setActivity_id(list.get(0).getId());
		info.setFlow_note_id(nextActivityId);
		info.setFlow_note_name(nextActivity);
		info.setIs_back("0");//提交后状态变成0
	}


	/**
	 * 退回
	 * @param request
	 * @param businesses
	 * @param formData
	 * @return
	 * @throws Exception
	 */
	public List<InspectionInfo> failed(HttpServletRequest request, JSONArray businesses, JSONObject formData) throws Exception {
		List<InspectionInfo> infos = new ArrayList<InspectionInfo>();
		for (int i = 0, len = businesses.length();i<len; i++) {
			JSONObject obj = businesses.getJSONObject(i);
			String id = obj.getString("id");
			
			InspectionInfo info = infoDao.get(id);
			infos.add(info);
			this.backProcess(request,info,formData);
		}
		return infos;
	}
	
	public synchronized void backProcess(HttpServletRequest request,InspectionInfo info,JSONObject formObject) throws Exception{
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			String backStep = formObject.getString("backStep");
			if(StringUtil.isEmpty(backStep))
			{
				backStep = "input";
			}
			JSONObject dataBus = new JSONObject();
			dataBus.put("flag", backStep);
			paramMap.put(FlowExtWorktaskParam.DATA_BUS, dataBus);
			
			paramMap.put(FlowExtWorktaskParam.ACTIVITY_ID, info.getActivity_id());
			//paramMap.put(FlowExtWorktaskParam.SERVICE_ID,info.getId());
			
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
			String op_conclusion = "从【"+currentActivity+"】环节进入【"+nextActivity+"】环节。\n结论：不通过\n"+formObject.getString("remark");
			User user = (User)SecurityUtil.getSecurityUser().getSysUser();
			
			logService.setLogs(info.getId(), currentActivity+"回退", op_conclusion, user.getId(), user.getName(), new Date(), request.getRemoteAddr());
	}
	
	
}
