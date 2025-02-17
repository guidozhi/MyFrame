package com.lsts.humanresources.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.bpm.communal.BpmOrg;
import com.khnt.bpm.communal.BpmOrgImpl;
import com.khnt.bpm.communal.BpmUser;
import com.khnt.bpm.communal.BpmUserImpl;
import com.khnt.bpm.ext.service.FlowDefinitionManager;
import com.khnt.bpm.ext.service.FlowExtManager;
import com.khnt.bpm.ext.support.FlowExtParam;
import com.khnt.bpm.ext.support.FlowExtWorktaskParam;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.core.exception.KhntException;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rbac.impl.dao.UserDao;
import com.khnt.utils.StringUtil;
import com.lsts.advicenote.service.MessageService;
import com.lsts.constant.Constant;
import com.lsts.humanresources.bean.BgLeave;
import com.lsts.humanresources.bean.EmployeeBase;
import com.lsts.humanresources.bean.WxLeave;
import com.lsts.humanresources.dao.EmployeeBaseDao;
import com.lsts.humanresources.dao.WxLeaveDao;
import com.lsts.log.service.SysLogService;
import net.sf.json.JSONObject;

@Service("WxLeaveManager")
public class WxLeaveManager extends EntityManageImpl<WxLeave, WxLeaveDao> {
	@Autowired
	EmployeeBaseDao employeeBaseDao;
	@Autowired
	WxLeaveDao wxLeaveDao;
	@Autowired
	FlowExtManager flowExtManager;
	@Autowired
    private BgLeaveManager  bgLeaveManager;
	@Autowired
    private EmployeeBaseManager  employeeBaseManager;
	@Autowired
	private SysLogService logService;
	@Autowired
	FlowDefinitionManager flowDefManager;
	@Autowired
	private UserDao userDao;
	@Autowired
    private MessageService  messageService;
	/** 状态常量 */
	public final static String LEAVE_FLOW_WTJ = "WJY"; // 未提交
	public final static String LEAVE_FLOW_YTJ = "YTJ"; // 已提交
	public final static String LEAVE_FLOW_SHZ = "SPZ"; // 审批中
	public final static String LEAVE_FLOW_SHTG = "SPTG"; // 审批通过
	public final static String LEAVE_FLOW_SHBTG = "SPBTG"; // 审批不通过

	/**
	 * 启动流程
	 * 
	 * @param map
	 * @throws Exception
	 */

	public void doStartPress(Map<String, Object> map) throws Exception {
		flowExtManager.startFlowProcess(map);
	}

	/**
	 * 审核
	 */

	public void doProcess(Map<String, Object> map) throws Exception {
		flowExtManager.submitActivity(map);
	}

	/**
	 * 流程结束
	 */
	public void stop(Map<String, Object> map) throws Exception {
		flowExtManager.finishProcess(map);
	}

	/**
	 * 已请假种类及天数
	 */
	public String queryLeave(HttpServletRequest request, String peopleId, String startDate) throws Exception {
		String leaveInfo = "";
		String totalDay = "";
		try {
			totalDay = employeeBaseDao.get(peopleId).getTotalDays();
			if (totalDay != null && totalDay != "") {
				BigDecimal totalDays = new BigDecimal(Integer.parseInt(totalDay));
				// 初始化
				BigDecimal leave_total = new BigDecimal(0);
				List<?> list = wxLeaveDao.queryLeave(request, peopleId, startDate);
				if (list.size() > 0 && list != null) {
					for (Object obj : list) {
						Object[] objs = (Object[]) obj;
						if (((String) objs[1]).equals("GWWC")) {
							leave_total = leave_total.add((BigDecimal) objs[0]);
							leaveInfo += "公务外出：" + objs[0] + "天，";
						} else if (((String) objs[1]).equals("NJ")) {
							leave_total = leave_total.add((BigDecimal) objs[0]);
							leaveInfo += "年假：" + objs[0] + "天， ";
							totalDays = totalDays.subtract((BigDecimal) objs[0]);
						} else if (((String) objs[1]).equals("SHIJ")) {
							leave_total = leave_total.add((BigDecimal) objs[0]);
							leaveInfo += "事假：" + objs[0] + "天，";
						} else if (((String) objs[1]).equals("HJ")) {
							leave_total = leave_total.add((BigDecimal) objs[0]);
							leaveInfo += "婚假：" + objs[0] + "天，";
						} else if (((String) objs[1]).equals("CJ")) {
							leave_total = leave_total.add((BigDecimal) objs[0]);
							leaveInfo += "产假：" + objs[0] + "天， ";
						} else if (((String) objs[1]).equals("TQJ")) {
							leave_total = leave_total.add((BigDecimal) objs[0]);
							leaveInfo += "探亲假：" + objs[0] + "天，";
						} else if (((String) objs[1]).equals("BJ")) {
							leave_total = leave_total.add((BigDecimal) objs[0]);
							leaveInfo += "病假：" + objs[0] + "天，";
						} else if (((String) objs[1]).equals("SANGJ")) {
							leave_total = leave_total.add((BigDecimal) objs[0]);
							leaveInfo += "丧假：" + objs[0] + "天，";
						} else if (((String) objs[1]).equals("PCJ")) {
							leave_total = leave_total.add((BigDecimal) objs[0]);
							leaveInfo += "陪产假：" + objs[0] + "天，";
						} else if (((String) objs[1]).equals("OTHER")) {
							leave_total = leave_total.add((BigDecimal) objs[0]);
							leaveInfo += "其他：" + objs[0] + "天， ";
						}
					}
					leaveInfo += "合计：" + leave_total + "天;剩余年假天数:" + totalDays + "天。";
					System.out.println(leaveInfo);
				} else {
					leaveInfo += "今年暂无请假记录！";
				}
			} else {
				leaveInfo = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			leaveInfo = "1";
		}
		return leaveInfo;
	}

	/**
	 * 查询角色所拥有的权限
	 */
	public List<?> getUserPower(String peopleId) {
		List<?> list = wxLeaveDao.getUserPower(peopleId);
		return list;
	}

	/**
	 * 根据员工ID查询用户信息
	 */
	public User getUser(String peopleId) {
		User user = wxLeaveDao.getUser(peopleId);
		return user;
	}

	/**
	 * 根据手机号查询account
	 */
	public String getAccount(String phone) {
		String account = null;
		List<?> list = wxLeaveDao.getAccount(phone);
		log.debug(this.getClass().getName() + " method getAccount 获取到account个数："+list.size());
  		if(list != null && list.size()>0) {
  			account = list.get(0).toString();
  		}
  		return account;
	}

	/**
	 * 保存微信申请并返回申请ID
	 */
	public String saveLeaveMessageWX(WxLeave wxLeave, HttpServletRequest request, String userId) throws Exception {
		EmployeeBase employeeBase = employeeBaseDao.get(userId);
		wxLeave.setPeopleId(employeeBase.getId());
		wxLeave.setPeopleName(employeeBase.getEmpName());
		wxLeave.setDepId(employeeBase.getWorkDepartment());
		wxLeave.setDepName(employeeBase.getWorkDepartmentName());
		wxLeave.setPeopleSign(employeeBase.getEmpName());
		wxLeave.setPeopleSignDate(new Date());
		wxLeave.setApplyStatus("WTJ");
		// 创建人创、建时间
		wxLeave.setCreateName(employeeBase.getEmpName());
		wxLeave.setCreateDate(new Date());
		this.wxLeaveDao.save(wxLeave);
		String id = wxLeave.getId();
		return id;
	}

	/**
	 * 通过请休假ID查询流程主键ID
	 * 
	 * @param request
	 * @param id
	 * @throws Exception
	 */
	public String queryMainId(HttpServletRequest request, String id) throws Exception {
		String mainId = wxLeaveDao.queryMainId(request, id);
		return mainId;
	}

	/**
	 * 通过请休假ID查询流程activityId
	 * 
	 * @param id
	 * @throws Exception
	 */
	public String getactivityId(String id) throws Exception {
		List mainIdlist = wxLeaveDao.getactivityId(id);
		String mainId = mainIdlist.get(0).toString();
		return mainId;
	}

	/**
	 * 通过请休假ID查询流程queryFlowId
	 * 
	 * @param id
	 * @throws Exception
	 */
	public String queryFlowId(String id) throws Exception {
		String flowId = wxLeaveDao.queryFlowId(id);
		return flowId;
	}

	/**
	 * 通过业务ID查询流程环节
	 * 
	 * @param id
	 * @throws Exception
	 */
	public String getRemark(String business_id, String hander_id) throws Exception {
		String remark = wxLeaveDao.getRemark(business_id, hander_id);
		return remark;
	}

	public String getRemarkGwwc(String business_id, String hander_id) throws Exception {
		String remark = wxLeaveDao.getRemarkGwwc(business_id, hander_id);
		return remark;
	}

	/**提交流程并更新请休假状态
	 * @param id
	 * @param flowId
	 * @param typeCode
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> subFolw(ServletRequest request, String id, String people_id, String leave_count1,
			String flowId, String userId,String serviceType) throws Exception {
		
		/*CurrentSessionUser curUser = SecurityUtil.getSecurityUser();*/
		// 获取申请人用户信息
		User apply_user = null;
		if (StringUtil.isNotEmpty(userId)) {
			apply_user = userDao.get(userId);
		}
		String phoneTemp = "";
		String checkName = "";
		String leaveType = "";
		List<?> list = bgLeaveManager.getUserPower(people_id);// 获取申请人的权限
		
		if (StringUtil.isEmpty(id)) {
			return JsonWrapper.failureWrapper("参数错误！");
		} else {
			if (StringUtil.isNotEmpty(flowId)) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 流程传参
				// 获取申请人角色信息
				Map<String, String> role = userDao.findUserRoles(userId);
				BpmOrg bpmOrg = new BpmOrgImpl(apply_user.getOrg());
				BpmUser user = new BpmUserImpl(apply_user.getId(), apply_user.getName(), bpmOrg, bpmOrg, role);
				if(map.containsKey(FlowExtWorktaskParam.BPM_USER)) {
					user = (BpmUser)map.get(FlowExtParam.BPM_USER);
				}
				map.put(FlowExtParam.BPM_USER, user);
				
				//获取流程名称
				String flow_name = flowDefManager.get(flowId).getFlowname();		
//				String flow_type =  flowDefManager.get(flowId).getFlowtype();
				
				map.put(FlowExtWorktaskParam.SERVICE_ID, id);
				//map.put(FlowExtWorktaskParam.ACTIVITY_ID, activityId);
				map.put(FlowExtWorktaskParam.SERVICE_TITLE, flow_name);
				map.put(FlowExtWorktaskParam.FLOW_DEFINITION_ID, flowId);
				map.put(FlowExtWorktaskParam.SERVICE_TYPE, serviceType);
				map.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, true);
				
				// 选择分支
				JSONObject dataBus = new JSONObject();
				if (!list.contains("部门负责人") && !list.contains("院领导") && !list.contains("院长")) {
					dataBus.put("org", "1");
				} else if (list.contains("部门负责人") || list.contains("院领导")) {
					dataBus.put("org", "2");
				}
				map.put(FlowExtWorktaskParam.DATA_BUS, dataBus);
				// 启动流程
				try {
					bgLeaveManager.doStartPress(map);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("启动请休假流程失败：业务ID：" + id);
					throw new KhntException("启动流程失败：业务ID：" + id);
				}
				// 更新请休假状态
				WxLeave wxLeave = wxLeaveDao.get(id);
				wxLeave.setApplyStatus(BgLeaveManager.LEAVE_FLOW_YTJ);
				wxLeaveDao.save(wxLeave);

				if(wxLeave.getLeaveType().equals("GWWC")){ leaveType="公务外出"; }else
				  if(wxLeave.getLeaveType().equals("NJ")){ leaveType="年假"; }else
				  if(wxLeave.getLeaveType().equals("SHIJ")){ leaveType="事假"; }else
				  if(wxLeave.getLeaveType().equals("HJ")){ leaveType="婚假"; }else
				  if(wxLeave.getLeaveType().equals("CJ")){ leaveType="产假"; }else
				  if(wxLeave.getLeaveType().equals("TQJ")){ leaveType="探亲假"; }else
				  if(wxLeave.getLeaveType().equals("BJ")){ leaveType="病假"; }else
				  if(wxLeave.getLeaveType().equals("SANGJ")){ leaveType="丧假"; }else
				  if(wxLeave.getLeaveType().equals("PCJ")){ leaveType="陪产假"; }else
				  if(wxLeave.getLeaveType().equals("OTHER")){ leaveType="其他"; }
				  phoneTemp=bgLeaveManager.getAuditor(wxLeave.getId(),"0");
				  //发送消息给审核人
				  messageService.sendWxMsg((HttpServletRequest) request, wxLeave.getId(),Constant.PEOPLE_CORPID, Constant.PEOPLE_PWD,
						  "<a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb0f376eb09e64dd3&redirect_uri=http://kh.scsei.org.cn/WxLeaveAction/weixinUserInfo.do?businessId="
								  +wxLeave.getId()+
								  "&response_type=code&scope=snsapi_base&state=STATE'>待办事务：有新的（"+leaveType+
								  "）请休假申请待审核,"+wxLeave.getPeopleName()+"（"+wxLeave.getDepName()+"）"+new
								  SimpleDateFormat("yyyy-MM-dd").format(wxLeave.getStartDate())+"至"+new
								  SimpleDateFormat("yyyy-MM-dd").format(wxLeave.getEndDate())+"（"+wxLeave.
								  getLeaveCount1()+"天），请及时处理！</a>", phoneTemp);
				  messageService.sendMoMsg((HttpServletRequest) request, wxLeave.getId(),
						  "待办事务：有新的（"+leaveType+"）请休假申请待审核,"+wxLeave.getPeopleName()+"（"+wxLeave.
						  getDepName()+"）"+new
						  SimpleDateFormat("yyyy-MM-dd").format(wxLeave.getStartDate())+"至"+new
						  SimpleDateFormat("yyyy-MM-dd").format(wxLeave.getEndDate())+"（"+wxLeave.
						  getLeaveCount1()+"天），请及时处理！", phoneTemp);
				  System.out.println("this auditor's phone is :"+phoneTemp); 
				  checkName = bgLeaveManager.getCheckName(wxLeave.getId(), wxLeave.getDepId()); 
				  checkName = StringUtil.isNotEmpty(checkName)?checkName:"";
				  //发送消息给请假人
				  messageService.sendWxMsg((HttpServletRequest) request, wxLeave.getId(),
						  Constant.PEOPLE_CORPID, Constant.PEOPLE_PWD, "办理进度：您的（"+leaveType+"）请休假（"+new
						  SimpleDateFormat("yyyy-MM-dd").format(wxLeave.getStartDate())+"至"+ new
						  SimpleDateFormat("yyyy-MM-dd").format(wxLeave.getEndDate())+"）已提交至"+checkName
						  +"审核环节！", employeeBaseManager.get(wxLeave.getPeopleId()).getEmpPhone());
				  messageService.sendMoMsg((HttpServletRequest) request, wxLeave.getId(),
						  "办理进度：您的（"+leaveType+"）请休假（"+new
						  SimpleDateFormat("yyyy-MM-dd").format(wxLeave.getStartDate())+"至"+ new
						  SimpleDateFormat("yyyy-MM-dd").format(wxLeave.getEndDate())+"）已提交至"+checkName
						  +"审核环节！", employeeBaseManager.get(wxLeave.getPeopleId()).getEmpPhone());
				 

				// 业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
				logService.setLogs(wxLeave.getId(), "提交休假请假申请", "提交休假请假申请至" + checkName + "，操作结果：已提交",
						apply_user != null ? apply_user.getId() : "未获取到操作用户编号", apply_user != null ? apply_user.getName() : "未获取到操作用户姓名",
						new Date(), request != null ? request.getRemoteAddr() : "");
			} else {
				return JsonWrapper.failureWrapper("流程ID为空！");
			}
			return JsonWrapper.successWrapper(id);
		}
	}
	
	/**提交公务外出流程并更新请休假状态
	 * @param id
	 * @param flowId
	 * @param typeCode
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> subFolwGwwc(ServletRequest request, String id, String people_id, String leave_count1,
			String flowId, String userId,String serviceType) throws Exception {
		// CurrentSessionUser user = SecurityUtil.getSecurityUser();
		// 获取申请人用户信息
		User apply_user = null;
		if (StringUtil.isNotEmpty(userId)) {
			apply_user = userDao.get(userId);
		}
		String phoneTemp = "";
		String checkName = "";
		String sign = "0";
		String leaveType = "";
		List<?> list = bgLeaveManager.getUserPower(people_id);// 获取申请人的权限
		String workTitle = employeeBaseManager.getWorkTitle(people_id);// 获取申请人职务

		if (StringUtil.isEmpty(id)) {
			return JsonWrapper.failureWrapper("参数错误！");
		} else {
			if (StringUtil.isNotEmpty(flowId)) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 流程传参
				
				// 获取申请人角色信息
				Map<String, String> role = userDao.findUserRoles(userId);
				
				BpmOrg bpmOrg = new BpmOrgImpl(apply_user.getOrg());
				BpmUser user = new BpmUserImpl(apply_user.getId(), apply_user.getName(), bpmOrg, bpmOrg, role);
				if(map.containsKey(FlowExtWorktaskParam.BPM_USER)) {
					user = (BpmUser)map.get(FlowExtParam.BPM_USER);
				}
				map.put(FlowExtParam.BPM_USER, user);

				// 获取流程名称
				String flow_name = flowDefManager.get(flowId).getFlowname();
//				String flow_type = flowDefManager.get(flowId).getFlowtype();

				map.put(FlowExtWorktaskParam.SERVICE_ID, id);
				// map.put(FlowExtWorktaskParam.ACTIVITY_ID, activityId);
				map.put(FlowExtWorktaskParam.SERVICE_TITLE, flow_name);
				map.put(FlowExtWorktaskParam.FLOW_DEFINITION_ID, flowId);
				map.put(FlowExtWorktaskParam.SERVICE_TYPE, serviceType);
				map.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, true);

				// 选择分支
				JSONObject dataBus = new JSONObject();
				BgLeave bgLeave = bgLeaveManager.get(id);
				// 若是院党政领导则直接有院长审批，否则按照职称选择流程分支
				if (bgLeave.getDepId().equals("100029")) {
					dataBus.put("org", "3");
				} else {
					if (workTitle.indexOf("副主任") != -1 || workTitle.indexOf("副部长") != -1
							|| workTitle.indexOf("助理") != -1) {
						if (workTitle.indexOf("主持") == -1 && workTitle.indexOf("主持工作") == -1) {
							dataBus.put("org", "1");
						} else if (workTitle.indexOf("主持") != -1 || workTitle.indexOf("主持工作") != -1) {
							sign = "1";
							dataBus.put("org", "2");
						}
					} else if (workTitle.indexOf("主任") != -1 || workTitle.indexOf("部长") != -1) {
						if (workTitle.indexOf("副主任") == -1 && workTitle.indexOf("副部长") == -1) {
							sign = "1";
							dataBus.put("org", "2");
						}
					} else if (workTitle.indexOf("院长") != -1 || workTitle.indexOf("副院长") != -1
							|| workTitle.indexOf("副总工") != -1) {
						dataBus.put("org", "3");
					}
				}
				map.put(FlowExtWorktaskParam.DATA_BUS, dataBus);
				
				// 启动流程
				try {
					bgLeaveManager.doStartPress(map);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("启动请休假流程失败：业务ID：" + id);
					throw new KhntException("启动流程失败：业务ID：" + id);
				}
				
				// 更新请休假状态
				bgLeave.setApplyStatus(BgLeaveManager.LEAVE_FLOW_YTJ);
				bgLeaveManager.save(bgLeave);
				
				if(bgLeave.getLeaveType().equals("GWWC")){ 
					leaveType="公务外出"; 
				}else if(bgLeave.getLeaveType().equals("NJ")){
					leaveType="年假"; 
				}else if(bgLeave.getLeaveType().equals("SHIJ")){
					leaveType="事假"; 
				}else if(bgLeave.getLeaveType().equals("HJ")){
					leaveType="婚假"; 
				}else if(bgLeave.getLeaveType().equals("CJ")){ 
					leaveType="产假"; 
				}else if(bgLeave.getLeaveType().equals("TQJ")){
					leaveType="探亲假"; 
				}else if(bgLeave.getLeaveType().equals("BJ")){
					leaveType="病假"; 
				}else if(bgLeave.getLeaveType().equals("SANGJ")){
					leaveType="丧假"; 
				}else if(bgLeave.getLeaveType().equals("PCJ")){ 
					leaveType="陪产假"; 
				}else if(bgLeave.getLeaveType().equals("OTHER")){
					leaveType="其他"; 
				}
				phoneTemp=bgLeaveManager.getAuditor(bgLeave.getId(),sign);
				//发送消息给审核人
				messageService.sendWxMsg((HttpServletRequest) request, bgLeave.getId(), Constant.PEOPLE_CORPID, Constant.PEOPLE_PWD,
					"<a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb0f376eb09e64dd3&redirect_uri=http://kh.scsei.org.cn/WxLeaveAction/weixinUserInfo.do?businessId="
					+bgLeave.getId()+
					"&response_type=code&scope=snsapi_base&state=STATE'>待办事务：有新的（"+leaveType+
					"）请休假申请待审核,"+bgLeave.getPeopleName()+"（"+bgLeave.getDepName()+"）"+new
					SimpleDateFormat("yyyy-MM-dd").format(bgLeave.getStartDate())+"至"+new
					SimpleDateFormat("yyyy-MM-dd").format(bgLeave.getEndDate())+"（"+bgLeave.
					getLeaveCount1()+"天），请及时处理！</a>", phoneTemp);
				messageService.sendMoMsg((HttpServletRequest) request, bgLeave.getId(),
					"待办事务：有新的（"+leaveType+"）请休假申请待审核,"+bgLeave.getPeopleName()+"（"+bgLeave.
					getDepName()+"）"+new
					SimpleDateFormat("yyyy-MM-dd").format(bgLeave.getStartDate())+"至"+new
					SimpleDateFormat("yyyy-MM-dd").format(bgLeave.getEndDate())+"（"+bgLeave.
					getLeaveCount1()+"天），请及时处理！", phoneTemp);
				System.out.println("this auditor's phone is :"+phoneTemp); 
				checkName =bgLeaveManager.getCheckName(bgLeave.getId(), bgLeave.getDepId()); 
				checkName =StringUtil.isNotEmpty(checkName)?checkName:"";
				//发送消息给请假人
				messageService.sendWxMsg((HttpServletRequest) request, bgLeave.getId(),
					Constant.PEOPLE_CORPID, Constant.PEOPLE_PWD, "办理进度：您的（"+leaveType+"）请休假（"+new
					SimpleDateFormat("yyyy-MM-dd").format(bgLeave.getStartDate())+"至"+ new
					SimpleDateFormat("yyyy-MM-dd").format(bgLeave.getEndDate())+"）已提交至"+checkName
					+"审核环节！", employeeBaseManager.get(bgLeave.getPeopleId()).getEmpPhone());
				messageService.sendMoMsg((HttpServletRequest) request, bgLeave.getId(),
					"办理进度：您的（"+leaveType+"）请休假（"+new
					SimpleDateFormat("yyyy-MM-dd").format(bgLeave.getStartDate())+"至"+ new
					SimpleDateFormat("yyyy-MM-dd").format(bgLeave.getEndDate())+"）已提交至"+checkName
					+"审核环节！", employeeBaseManager.get(bgLeave.getPeopleId()).getEmpPhone());

				// 业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
				logService.setLogs(bgLeave.getId(), "提交休假请假申请", "提交休假请假申请至" + checkName + "，操作结果：已提交",
						apply_user != null ? apply_user.getId() : "未获取到操作用户编号",
								apply_user != null ? apply_user.getName() : "未获取到操作用户姓名", new Date(),
						request != null ? request.getRemoteAddr() : "");
			} else {
				return JsonWrapper.failureWrapper("流程ID为空！");
			}
			return JsonWrapper.successWrapper(id);
		}
	}
}
