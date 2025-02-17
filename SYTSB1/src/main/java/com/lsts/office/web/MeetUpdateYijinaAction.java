 package com.lsts.office.web;
import com.khnt.bpm.ext.support.FlowExtWorktaskParam;
import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.StringUtil;
import com.lsts.advicenote.service.MessageService;
import com.lsts.common.service.MessageXinxiService;
import com.lsts.constant.Constant;
import com.lsts.log.service.SysLogService;
import com.lsts.office.bean.MeetUpdateYijina;
import com.lsts.office.bean.MeetingReq;
import com.lsts.office.dao.MeetUpdateYijinaDao;
import com.lsts.office.dao.MeetingReqDao;
import com.lsts.office.service.MeetUpdateYijinaManager;
import com.lsts.office.service.MeetingReqManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
@Controller
@RequestMapping("updates/yijina")
public class MeetUpdateYijinaAction extends SpringSupportAction<MeetUpdateYijina, MeetUpdateYijinaManager> {

    @Autowired
    private MeetUpdateYijinaManager meetUpdateYijinaManager;
    @Autowired
    private MeetUpdateYijinaDao meetUpdateYijinaDao;
    @Autowired
    private MeetingReqManager meetingReqManager;
    @Autowired
    private MessageXinxiService messageXinxiService;
    @Autowired
    private MeetingReqDao meetingReqDao;
    @Autowired
    private MessageService messageservice;
    @Autowired
   	private MeetingReqAction meetingReqAction;
    @Autowired
	private SysLogService logService;
    
    /**
   	 * 添加审核意见
   	 * @param response
        * @throws Exception 
   	 */
    @RequestMapping(value = "savesh")
   	@ResponseBody
   	@SuppressWarnings("unused")
    public HashMap<String, Object> savesh(HttpServletRequest request,MeetUpdateYijina meetUpdateYijina) throws Exception {
    	HashMap<String, Object> map = new HashMap<String, Object>();
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		meetUpdateYijina.setAuditMan(user.getName());
		meetUpdateYijina.setAuditTime(new Date());
		meetUpdateYijina.setAuditStep(user.getName()+meetUpdateYijina.getAuditResult());
		meetUpdateYijinaManager.save(meetUpdateYijina);
    	map.put("success", true);
		return map;

   	}
    
    
    /**审批流程并改变状态
   	 * @param id
   	 * @param flowId
   	 * @param typeCode
   	 * @param status
   	 * @return
   	 * @throws Exception
   	 */
   	@RequestMapping(value = "zltj")
   	@ResponseBody
   	public HashMap<String, Object> zltj(String areaFlag,String id,
   			String typeCode, String status,String activityId,HttpServletRequest request) throws Exception {
   		Map<String, Object> map = new HashMap<String, Object>();
   		CurrentSessionUser user = SecurityUtil.getSecurityUser();
   		map.put(FlowExtWorktaskParam.SERVICE_ID, id);
   		map.put(FlowExtWorktaskParam.ACTIVITY_ID, activityId);
   		map.put(FlowExtWorktaskParam.SERVICE_TITLE, "会议申请单"+user.getName());
   		map.put(FlowExtWorktaskParam.SERVICE_TYPE, typeCode);
   		map.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK,true);
   		
   		if (StringUtil.isEmpty(id)) {
   			return JsonWrapper.failureWrapper("参数错误！");
   		} else {
   			// 审批流程
   			String opinion=request.getParameter("opinion");
   			//URLEncoder.encode(opinion,"utf-8"); 
   			if (StringUtil.isNotEmpty(activityId)) {
				/*if(areaFlag.equals("2")){
					meetUpdateYijinaManager.doProcess(map);
					//保存业务表数据
					MeetingReq meetingReq=meetingReqManager.get(id);
					meetingReq.setStatus(MeetUpdateYijinaManager.MEET_FLOW_SHZ);
					meetingReq.setSzrYj(opinion);
					meetingReqManager.save(meetingReq);
					
				}*/
				/*else if(areaFlag.equals("3")){
					meetUpdateYijinaManager.doProcess(map);
					//保存业务表数据
					MeetingReq meetingReq=meetingReqManager.get(id);
					meetingReq.setStatus(MeetUpdateYijinaManager.MEET_FLOW_SHZ);
					meetingReq.setOfficeYj(opinion);
					meetingReqManager.save(meetingReq);
					
				}*/
				if(areaFlag.equals("2")){
					meetUpdateYijinaManager.doProcess(map);
					//保存业务表数据
					MeetingReq meetingReq=meetingReqManager.get(id);
					meetingReq.setStatus(MeetUpdateYijinaManager.MEET_FLOW_SHTG);
					meetingReq.setFgyzYj(opinion);
					meetingReqManager.save(meetingReq);
					String employee=meetingReqDao.getTel(meetingReq.getDjPeopleId()).toString();
					String mobileTel = Pattern.compile("\\b([\\w\\W])\\b").matcher(employee.toString()
			    			.substring(1,employee.toString().length()-1)).replaceAll("'$1'"); 
					 String msg = "您好！您的会议室申请("+meetingReq.getName()+")已通过！";
					 messageXinxiService.setSaveMessageXinxi(id, meetingReq.getSqDepartment(), mobileTel, new Date(), msg, "会议室申请", "短信，微信", "实时发送");
					 messageservice.sendMoMsg(request, id, msg, mobileTel);
					 //微信发送
						String msg1 ="您好！您的会议室申请("+meetingReq.getName()+")已通过！";
						messageXinxiService.setSaveMessageXinxi(id, meetingReq.getSqDepartment(), mobileTel, new Date(), msg1, "会议室申请", "短信，微信", "实时发送");
						messageservice.sendWxMsg(request, id, Constant.OFFICE_CORPID, Constant.OFFICE_PWD, msg1, mobileTel);
					logService.setLogs(id, "会议室申请审核", "会议室申请审核，审核结果：审核通过。", user.getId(), user
							.getName(), new Date(), request.getRemoteAddr()); 
					
	  	    			
				}else{
					MeetingReq meetingReq=meetingReqManager.get(id);
					meetingReq.setStatus(MeetUpdateYijinaManager.MEET_FLOW_SHBTG);
					meetingReqManager.save(meetingReq);
					String employee=meetingReqDao.getTel(meetingReq.getDjPeopleId()).toString();
					String mobileTel = Pattern.compile("\\b([\\w\\W])\\b").matcher(employee.toString()
			    			.substring(1,employee.toString().length()-1)).replaceAll("'$1'"); 
					 String msg = "您好！您的会议室申请("+meetingReq.getName()+")失败！";
					 messageXinxiService.setSaveMessageXinxi(id, meetingReq.getSqDepartment(), mobileTel, new Date(), msg, "会议室申请", "短信，微信", "实时发送");
					 //messageXinxiService.setSendMessage(mobileTel, msg);
					 messageservice.sendMoMsg(request, id, msg, mobileTel);
					 //微信发送
						String msg1 = "您好！您的会议室申请("+meetingReq.getName()+")失败！";
						messageXinxiService.setSaveMessageXinxi(id, meetingReq.getSqDepartment(), mobileTel, new Date(), msg1, "会议室申请", "短信，微信", "实时发送");
						 messageservice.sendWxMsg(request, id, Constant.OFFICE_CORPID, Constant.OFFICE_PWD, msg1, mobileTel);
				 logService.setLogs(id, "会议室申请审核", "会议室申请审核，审核结果：审核不通过。", user.getId(), user
							.getName(), new Date(), request.getRemoteAddr()); 
					
				}
			} else {
				return JsonWrapper.failureWrapper("流程ID为空！");
			}

   			return JsonWrapper.successWrapper(id);
   		}

   	}

   	/**退回审批流程并改变状态
	 * @param id
	 * @param flowId
	 * @param typeCode
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "xgth")
	@ResponseBody
	public HashMap<String, Object> xgth(MeetUpdateYijina meetUpdateYijina,String areaFlag,String id,
			String typeCode, String status,String activityId,String processId,HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put(FlowExtWorktaskParam.SERVICE_ID, id);
		map.put(FlowExtWorktaskParam.ACTIVITY_ID, activityId);
		map.put(FlowExtWorktaskParam.SERVICE_TITLE, "修改流程");
		map.put(FlowExtWorktaskParam.SERVICE_TYPE, typeCode);
		map.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, true);
		map.put(FlowExtWorktaskParam.PROCESS_ID, processId);
		MeetingReq meetingReq=meetingReqManager.get(id);
		meetingReq.setStatus(MeetUpdateYijinaManager.MEET_FLOW_SHBTG);
		
		if (StringUtil.isEmpty(id)) {
			return JsonWrapper.failureWrapper("参数错误！");
		} else {
			// 退回流程
			request.setCharacterEncoding("UTF-8");
   			String opinion=request.getParameter("opinion");
			if (StringUtil.isNotEmpty(activityId)) {
				/*if(areaFlag.equals("2")){
					meetingReq.setSzrYj(opinion);
					meetingReqManager.save(meetingReq);
					meetUpdateYijinaManager.stop(map);
				}else*/ 
				if(areaFlag.equals("2")){
					meetingReq.setOfficeYj(opinion);
					meetingReqManager.save(meetingReq);
				   meetUpdateYijinaManager.doreturn(map);
				}else{
					meetingReq.setFgyzYj(opinion);
					meetingReqManager.save(meetingReq);
				   meetUpdateYijinaManager.doreturn(map);
				}
				String employee=meetingReqDao.getTel(meetingReq.getDjPeopleId()).toString();
				String mobileTel = Pattern.compile("\\b([\\w\\W])\\b").matcher(employee.toString()
		    			.substring(1,employee.toString().length()-1)).replaceAll("'$1'"); 
				 String msg = "您好！您的会议室申请("+meetingReq.getName()+")失败！";
				 messageXinxiService.setSaveMessageXinxi(id, meetingReq.getSqDepartment(), mobileTel, new Date(), msg, "会议室申请", "短信，微信", "实时发送");
				 messageservice.sendMoMsg(request, id, msg, mobileTel);
				 
				 //微信发送
					String msg1 = "您好！您的会议室申请("+meetingReq.getName()+")失败！";
					messageXinxiService.setSaveMessageXinxi(id, meetingReq.getSqDepartment(), mobileTel, new Date(), msg1, "会议室申请", "短信，微信", "实时发送");
					 messageservice.sendWxMsg(request, id, Constant.OFFICE_CORPID, Constant.OFFICE_PWD, msg1, mobileTel);
				
				
			} else {
				return JsonWrapper.failureWrapper("流程ID为空！");
			}
			return JsonWrapper.successWrapper(id);
		}
    
	}
	
	/**撤销审批流程并改变状态
	 * @param id
	 * @param flowId
	 * @param typeCode
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "ret")
	@ResponseBody
	public HashMap<String, Object> ret(HttpServletRequest request,String id,String cxyy,String typeCode) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		cxyy = java.net.URLDecoder.decode(cxyy,"UTF-8");
		meetUpdateYijinaManager.saveRet(request,id, cxyy, getCurrentUser(),typeCode);
		logService.setLogs(id, "撤销会议室申请", "撤销会议室申请，操作结果：撤销成功。", user.getId(), user
				.getName(), new Date(), request.getRemoteAddr()); 
		map.put("success", true);
		return map; 
	}
	/**
	 * 微信
	 * 撤销审批流程并改变状态
	 * @param id
	 * @param flowId
	 * @param typeCode
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "ret1")
	@ResponseBody
	public HashMap<String, Object> ret1(HttpServletRequest request,String id,String typeCode,String eId) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String code=null;
		/*String userId=meetingReqAction.load(request, code);*/
		Boolean bool=null;
		try {
			bool=meetUpdateYijinaManager.saveRetWX(request,id,eId,typeCode);
			map.put("success", true);
			/*if(bool==true){
				map.put("success", true);
			}else{
				map.put("success", false);
			}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map; 
	}
}