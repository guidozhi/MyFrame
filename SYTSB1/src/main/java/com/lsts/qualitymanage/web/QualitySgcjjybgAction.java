package com.lsts.qualitymanage.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.khnt.bpm.communal.BpmOrgImpl;
import com.khnt.bpm.communal.BpmUser;
import com.khnt.bpm.communal.BpmUserImpl;
import com.khnt.bpm.communal.BpmUserOrgManager;
import com.khnt.bpm.core.bean.Activity;
import com.khnt.bpm.core.bean.Participator;
import com.khnt.bpm.core.dao.ActivityDao;
import com.khnt.bpm.core.service.ActivityManager;
import com.khnt.bpm.core.support.FinishFlowInf;
import com.khnt.bpm.ext.support.FlowExtWorktaskParam;
import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.core.exception.KhntException;
import com.khnt.pub.worktask.bean.Worktask;
import com.khnt.pub.worktask.service.WorktaskManager;
import com.khnt.rbac.impl.bean.Org;
import com.khnt.rbac.impl.bean.Role;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rbac.impl.dao.EmployeeDao;
import com.khnt.rbac.impl.dao.RoleDao;
import com.khnt.rbac.impl.dao.UserDao;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.support.CurrentBpmSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.StringUtil;
import com.khnt.weixin.enums.EnumMethod;
import com.khnt.weixin.interceptor.OAuthRequired;
import com.khnt.weixin.util.HttpRequestUtil;
import com.khnt.weixin.util.Result;
import com.khnt.weixin.util.WxUtil;
import com.lsts.advicenote.service.MessageService;
import com.lsts.common.service.CodeTablesService;
import com.lsts.constant.Constant;
import com.lsts.humanresources.service.WxLeaveManager;
import com.lsts.log.service.SysLogService;
import com.lsts.qualitymanage.bean.QualitySgcjjybg;
import com.lsts.qualitymanage.bean.QualitySug;
import com.lsts.qualitymanage.bean.SgcjjybgNum;
import com.lsts.qualitymanage.dao.QualitySgcjjybgDao;
import com.lsts.qualitymanage.service.QualitySgcjjybgManager;
import com.lsts.qualitymanage.service.QualitySugManager;
import com.lsts.qualitymanage.service.SgcjjybgNumManager;
import com.lsts.qualitymanage.service.TaskbookManager;
import com.scts.task.bean.ContractTaskInfo;
import com.scts.task.dao.ContractTaskInfoDao;
import com.scts.weixin.app.service.WeixinAppInfoManager;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("quality/sgcjjybg")
public class QualitySgcjjybgAction extends SpringSupportAction<QualitySgcjjybg, QualitySgcjjybgManager> {

    @Autowired
    private QualitySgcjjybgManager  qualitySgcjjybgManager;
    @Autowired
    private TaskbookManager taskbookManager;
    @Autowired
   	private CodeTablesService codeTablesService;
    @Autowired
    private SgcjjybgNumManager  sgcjjybgNumManager;
    @Autowired
   	private QualitySugManager qualitySugManager;
    @Autowired
   	private QualitySgcjjybgDao qualitySgcjjybgDao;
    @Autowired
	private SysLogService logService;
    @Autowired
    private WorktaskManager worktaskManager;
    @Autowired
    private ActivityManager activityManager;
    @Autowired
	private WxLeaveManager wxLeaveManager;
    @Autowired
    private BpmUserOrgManager bpmUserOrgManager;
    @Autowired 
    private ActivityDao activityDao;
    @Autowired 
    private RoleDao roleDao;
    @Autowired 
    private UserDao userDao;
    @Autowired
	private MessageService messageService;
    @Autowired
    ContractTaskInfoDao contractTaskInfoDao;
    @Autowired
    private WeixinAppInfoManager  weixinAppInfoManager;
    /**
   	 * 添加
   	 * @param response
        * @throws Exception 
   	 */
       @Override
   	public HashMap<String, Object> save(HttpServletRequest request,@RequestBody QualitySgcjjybg qualitySgcjjybg) throws Exception {
   		CurrentSessionUser user = SecurityUtil.getSecurityUser();
   		
   		HashMap<String, Object> map = new HashMap<String, Object>();
   		if(qualitySgcjjybg.getId()==null || qualitySgcjjybg.getId().equals("")){
   			qualitySgcjjybgManager.saveSgcjjybg(request,qualitySgcjjybg,user);
   			map.put("success", true);
   			map.put("msg", "数据保存成功！");
   		}else{
   			//CwBorrowMoney cwMoney1=cwBorrowMoneyManager.get(cwMoney.getId());
   			if(qualitySgcjjybg.getStatus().equals(QualitySgcjjybgManager.ZL_SGCJ_PASS)){
   				map.put("msg", "此条数据已通过不可修改！");
   				map.put("success", false);
   	  		}else if(qualitySgcjjybg.getStatus().equals(QualitySgcjjybgManager.ZL_SGCJ_YTJ)){
   				map.put("msg", "此条数据已提交不可修改！");
   				map.put("success", false);
   	 		}else if(qualitySgcjjybg.getStatus().equals(QualitySgcjjybgManager.ZL_SGCJ_SHZ)){
   				map.put("msg", "此条数据审核中不可修改！");
   				map.put("success", false);
   	 		}else{
   	  			qualitySgcjjybgManager.saveSgcjjybg(request,qualitySgcjjybg,user);
   				map.put("success", true);
   				map.put("msg", "数据保存成功！");
   			}
   		}
//   		String saveone=request.getParameter("saveone");
//   		if(saveone!=null || !saveone.equals("")){
//   			if(saveone.equals("2")){
//				String id =	qualitySgcjjybg.getId();
//				this.subFolws(request,id,"","TJY2_ZL_SGCJJYBG","","");
//			}
//   		}
   		return map;
   	}
       @RequestMapping(value="save2")
       @ResponseBody
       public HashMap<String, Object> save2(HttpServletRequest request,QualitySgcjjybg qualitySgcjjybg) throws Exception {
      		CurrentSessionUser user = SecurityUtil.getSecurityUser();
      		
      		HashMap<String, Object> map = new HashMap<String, Object>();
      		if(qualitySgcjjybg.getId()==null || qualitySgcjjybg.getId().equals("")){
      			qualitySgcjjybgManager.saveSgcjjybg(request,qualitySgcjjybg,user);
      			map.put("success", true);
      			map.put("msg", "数据保存成功！");
      		}else{
      			//CwBorrowMoney cwMoney1=cwBorrowMoneyManager.get(cwMoney.getId());
      			if(qualitySgcjjybg.getStatus().equals(QualitySgcjjybgManager.ZL_SGCJ_PASS)){
      				map.put("msg", "此条数据已通过不可修改！");
      				map.put("success", false);
      	  		}else if(qualitySgcjjybg.getStatus().equals(QualitySgcjjybgManager.ZL_SGCJ_YTJ)){
      				map.put("msg", "此条数据已提交不可修改！");
      				map.put("success", false);
      	 		}else if(qualitySgcjjybg.getStatus().equals(QualitySgcjjybgManager.ZL_SGCJ_SHZ)){
      				map.put("msg", "此条数据审核中不可修改！");
      				map.put("success", false);
      	 		}else{
      	  			qualitySgcjjybgManager.saveSgcjjybg(request,qualitySgcjjybg,user);
      				map.put("success", true);
      				map.put("msg", "数据保存成功！");
      			}
      		}
      		return map;
      	}
       
       /**
   	 * 删除
   	 * */
   	@Override
   	public HashMap<String, Object> delete(String ids) throws Exception {
   		HashMap<String, Object> map = new HashMap<String, Object>();
   		QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(ids);
 		if(qualitySgcjjybg.getStatus().equals(QualitySgcjjybgManager.ZL_SGCJ_PASS)){
			map.put("msg", "此条数据已通过不可删除！");
			map.put("success", false);
 		}else if(qualitySgcjjybg.getStatus().equals(QualitySgcjjybgManager.ZL_SGCJ_YTJ)){
			map.put("msg", "此条数据已提交不可删除！");
			map.put("success", false);
 		}else if(qualitySgcjjybg.getStatus().equals(QualitySgcjjybgManager.ZL_SGCJ_SHZ)){
			map.put("msg", "此条数据审核中不可删除！");
			map.put("success", false);
 		}else{
 			qualitySgcjjybgManager.delete(ids);
			map.put("msg", "数据删除成功！");
			map.put("success", true);
   		}
   		return map;
   		//return JsonWrapper.successWrapper();
   	}
   	/**
   	 * 获取流程id
   	 * */
	@RequestMapping(value = "getLcid")
   	@ResponseBody
   	public HashMap<String, Object> getLcid(String id) throws Exception{
   		HashMap<String, Object> map = new HashMap<String, Object>();
   		String a=qualitySgcjjybgDao.getLcid(id).toString();
   		String ids = Pattern.compile("\\b([\\w\\W])\\b").matcher(a.toString()
    			.substring(1,a.toString().length()-1)).replaceAll("'$1'"); 
   		map.put("ids", ids);
   		map.put("success", true);
		return map;
	}
	/**
	 * 
	 * @param id 业务id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getWorktask")
	@ResponseBody
	public HashMap<String, Object> getWorktask(HttpServletRequest request,String id) throws Exception{
		HashMap<String, Object> map = new HashMap<String, Object>();
		String a = qualitySgcjjybgDao.getLcid(id).toString();
		String ids = Pattern.compile("\\b([\\w\\W])\\b").matcher(a.toString()
				.substring(1,a.toString().length()-1)).replaceAll("'$1'"); 
		//流程
		Worktask worktask = (Worktask) this.worktaskManager.get(ids);
		if (worktask == null) {
			throw new KhntException("找不到id为“" + id + "”的任务");
		} else {
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			if ((!"person".equals(worktask.getHandlerType()) || user.getId().equals(worktask.getHandlerId()))
					&& (!"role".equals(worktask.getHandlerType()) || user.hasRole(worktask.getHandlerId()))) {
				String url = worktask.getUrl();
				if (StringUtil.isEmpty(url)) {
					this.log.error("工作任务【标题=" + worktask.getTitle() + ";ID=" + worktask.getId() + "】未配置业务处理URL！");
					throw new KhntException("没有找到任务处理页面！");
				} else {
					Activity activity = activityManager.get(worktask.getServiceId());
					map.put("worktask", worktask);
					map.put("activity", activity);
				}
			} else {
				this.log.error("用户【" + user.getName() + "】非法操作工作任务【标题=" + worktask.getTitle() + ";ID="
						+ worktask.getId() + "】");
				throw new KhntException("对不起，您没有处理该任务的权力！");
			}
		}
		map.put("success", true);
		return map;
	}
	
	/**
   	 * 获取流程process_id
   	 * */
	@RequestMapping(value = "getprocess_id")
   	@ResponseBody
   	public HashMap<String, Object> getprocess_id(String id) throws Exception{
   		HashMap<String, Object> map = new HashMap<String, Object>();
   		String a=qualitySgcjjybgDao.getprocess_id(id).toString();
   		String process_id = Pattern.compile("\\b([\\w\\W])\\b").matcher(a.toString()
    			.substring(1,a.toString().length()-1)).replaceAll("'$1'"); 
   		map.put("process_id", process_id);
   		map.put("success", true);
		return map;
	}
	
   	@RequestMapping(value = "setbgbh")
   	@ResponseBody
   	public HashMap<String, Object> setbgbh(String id,String test_coding,String njts,String row) throws Exception{
   		HashMap<String, Object> map = new HashMap<String, Object>();
   		CurrentSessionUser user = SecurityUtil.getSecurityUser();
   		int arr = Integer.parseInt(njts);
   		
   		String a=qualitySgcjjybgManager.setbgbh(id, test_coding, njts).toString();
   		String bgbh1 = Pattern.compile("\\b([\\w\\W])\\b").matcher(a.toString()
    			.substring(1,a.toString().length()-1)).replaceAll("'$1'"); 
   		String bgbh=bgbh1.replaceAll("'", "");
   		map.put("bgbh",bgbh);//返回报告编号
   		
   		String b = qualitySgcjjybgManager.getbgbh(id, test_coding, njts).toString();
   		//开始编号
   		String ksbh = Pattern.compile("\\b([\\w\\W])\\b").matcher(b.toString()
    			.substring(1,b.toString().length()-1)).replaceAll("'$1'");
		int docNumArray = Integer.parseInt(ksbh.replaceAll("'-'", ""));

   		for (int i = 0; i < arr; i++) {
   			SgcjjybgNum sgcjjybgNum=new SgcjjybgNum();
   			
   			sgcjjybgNum.setFwbjhbgNum(test_coding+String.valueOf(docNumArray+i).substring(0, 4)+"-"+String.valueOf(docNumArray+i).substring(4, 8));
   			sgcjjybgNum.setRegistrant(user.getName());
   			sgcjjybgNum.setRegistrantId(user.getId());
   			sgcjjybgNum.setRegistrantDate(new Date());
   			sgcjjybgNum.setStatus("1");
   			sgcjjybgNum.setIdentifierType("1");
   			sgcjjybgNum.setFksgId(id);
   			if(row.equals("1")){//第几行
   				sgcjjybgNum.setRowId(row);
   			}else if(row.equals("2")){
   				sgcjjybgNum.setRowId(row);
   			}else if(row.equals("3")){
   				sgcjjybgNum.setRowId(row);
   			}else if(row.equals("4")){
   				sgcjjybgNum.setRowId(row);
   			}else if(row.equals("5")){
   				sgcjjybgNum.setRowId(row);
   			}else if(row.equals("6")){
   				sgcjjybgNum.setRowId(row);
   			}else if(row.equals("7")){
   				sgcjjybgNum.setRowId(row);
   			}
   			sgcjjybgNum.setTestCoding(test_coding);
   			sgcjjybgNumManager.save(sgcjjybgNum);
		}
   		
		map.put("success", true);
   		return map;
   	}
   	@RequestMapping(value = "setbgbh2")
   	@ResponseBody
   	public HashMap<String, Object> setbgbh2(String id,String test_coding,String njts) throws Exception{
   		HashMap<String, Object> map = new HashMap<String, Object>();
   		CurrentSessionUser user = SecurityUtil.getSecurityUser();
   		int arr = Integer.parseInt(njts);
   		String b = qualitySgcjjybgManager.getbgbh(id, test_coding, njts).toString();
   		//开始编号
   		String ksbh = Pattern.compile("\\b([\\w\\W])\\b").matcher(b.toString()
    			.substring(1,b.toString().length()-1)).replaceAll("'$1'");
		int docNumArray = Integer.parseInt(ksbh.replaceAll("'-'", ""));

   		for (int i = 1; i <= arr; i++) {
   			SgcjjybgNum sgcjjybgNum=new SgcjjybgNum();
   			
   			sgcjjybgNum.setFwbjhbgNum(test_coding+String.valueOf(docNumArray+i).substring(0, 4)+"-"+String.valueOf(docNumArray+i).substring(4, 8));
   			sgcjjybgNum.setRegistrant(user.getName());
   			sgcjjybgNum.setRegistrantId(user.getId());
   			sgcjjybgNum.setRegistrantDate(new Date());
   			sgcjjybgNum.setStatus("1");
   			sgcjjybgNum.setIdentifierType("1");
   			sgcjjybgNum.setFksgId(id);
   			sgcjjybgNum.setRowId("2");
   			sgcjjybgNum.setTestCoding(test_coding);
   			sgcjjybgNumManager.save(sgcjjybgNum);
		}
   		
   		String a=qualitySgcjjybgManager.setbgbh(id, test_coding, njts).toString();
   		String bgbh1 = Pattern.compile("\\b([\\w\\W])\\b").matcher(a.toString()
    			.substring(1,a.toString().length()-1)).replaceAll("'$1'"); 
   		String bgbh2=bgbh1.replaceAll("'", "");
   		map.put("bgbh",bgbh2);//返回报告编号
		map.put("success", true);
   		return map;
   	}
   	@RequestMapping(value = "setbgbh3")
   	@ResponseBody
   	public void setbgbh3(String id,String fwbjhbg3) throws Exception{
		QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
		qualitySgcjjybg.setFwbjhbg3(fwbjhbg3);
		qualitySgcjjybgManager.save(qualitySgcjjybg);
   	}
   	@RequestMapping(value = "setbgbh4")
   	@ResponseBody
   	public void setbgbh4(String id,String fwbjhbg4) throws Exception{
		QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
		qualitySgcjjybg.setFwbjhbg4(fwbjhbg4);
		qualitySgcjjybgManager.save(qualitySgcjjybg);
   	}
   	@RequestMapping(value = "setbgbh5")
   	@ResponseBody
   	public void setbgbh5(String id,String fwbjhbg5) throws Exception{
		QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
		qualitySgcjjybg.setFwbjhbg5(fwbjhbg5);
		qualitySgcjjybgManager.save(qualitySgcjjybg);
   	}
   	@RequestMapping(value = "setbgbh6")
   	@ResponseBody
   	public void setbgbh6(String id,String fwbjhbg6) throws Exception{
		QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
		qualitySgcjjybg.setFwbjhbg6(fwbjhbg6);
		qualitySgcjjybgManager.save(qualitySgcjjybg);
   	}
   	@RequestMapping(value = "setbgbh7")
   	@ResponseBody
   	public void setbgbh7(String id,String fwbjhbg7) throws Exception{
		QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
		qualitySgcjjybg.setFwbjhbg7(fwbjhbg7);
		qualitySgcjjybgManager.save(qualitySgcjjybg);
   	}
   	/**
	 * 手工出具检验报告/证书审批表提交审核
	 * 
	 * */
    @RequestMapping(value = "subFolws")
   	@ResponseBody
   	public HashMap<String, Object> subFolws(ServletRequest request,String id, String flowId,
   			String typeCode, String status,String activityId) throws Exception {
    	CurrentSessionUser user = SecurityUtil.getSecurityUser();
   		Map<String, Object> map = new HashMap<String, Object>();
   		//流程传参
   		map.put(FlowExtWorktaskParam.SERVICE_ID, id);
   		map.put(FlowExtWorktaskParam.ACTIVITY_ID, activityId);
   		map.put(FlowExtWorktaskParam.SERVICE_TITLE, "手工出具检验报告申请");
   		map.put(FlowExtWorktaskParam.FLOW_DEFINITION_ID, flowId);
   		map.put(FlowExtWorktaskParam.SERVICE_TYPE, typeCode);
   		map.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, true);
   		
   		
   		if (StringUtil.isEmpty(id)) {
   			return JsonWrapper.failureWrapper("参数错误！");
   		} else {
   			// 启动流程
   			if (StringUtil.isNotEmpty(flowId)) {
   			//改变状态
   				QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
   				try {
   					Map<String, Object> map2 = qualitySgcjjybgManager.doStartPress(map);
					//发送消息
	   				List<Activity> list = (List<Activity>)map2.get("result_activity_list");
	   				List<String> usernames = new ArrayList<>();
					for (Activity activity : list) {
						List<BpmUser> users = getBpmUserPaticipator(activity);
						activityId = activity.getActivityId();
						for (BpmUser bpmUser : users) {
							String tel = qualitySgcjjybgManager.getUserTel(bpmUser);
							System.out.println("发送微信给>>>>>>:"+bpmUser.getName()+" \n this auditor's phone is :"+tel);
							//发送消息给审核人
							sendToAudit(qualitySgcjjybg,(HttpServletRequest)request,bpmUser,tel);
							usernames.add(bpmUser.getName());
						}
					}
//					//当前流程
//					Activity activity = (Activity) this.activityDao.get(activityId);
//					
//					//发送消息给申请人
//					sendToApply(qualitySgcjjybg,(HttpServletRequest)request,activity,list,user,"manualReportRemind");
//   					//发送微信消息 end 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
   				
   				qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_YTJ);
   				qualitySgcjjybgManager.save(qualitySgcjjybg);
   				//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
  		  		logService.setLogs(id, 
  		  				"提交手工出具检验报告/证书审批表", 
  		  				"提交手工出具检验报告/证书审批表至部门负责人审核。操作结果：已提交", 
  		  				user != null ? user.getId() : "未获取到操作用户编号",
  						user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
  						request != null ? request.getRemoteAddr() : "");
   			} else {
   				return JsonWrapper.failureWrapper("流程ID为空！");
   			}

   			return JsonWrapper.successWrapper(id);
   		}
   	}
    
    /**手工出具检验报告/证书审批表审批流程并改变状态
   	 * @param id
   	 * @param flowId
   	 * @param typeCode
   	 * @param status
   	 * @return
   	 * @throws Exception
   	 */
   	@RequestMapping(value = "sgcjjybgsh")
   	@ResponseBody
   	public HashMap<String, Object> sgcjjybgsh(ServletRequest request,String areaFlag,String id,
   			String typeCode, String status,String activityId,
   			String bgbh,String bgbh2,String bgbh3,String bgbh4,String bgbh5,String bgbh6,String bgbh7
   			,String zlshyj) {
   		
   		Map<String, Object> map = new HashMap<String, Object>();
   		map.put(FlowExtWorktaskParam.SERVICE_ID, id);
   		map.put(FlowExtWorktaskParam.ACTIVITY_ID, activityId);
   		map.put(FlowExtWorktaskParam.SERVICE_TITLE, "手工出具检验报告申请");
   		map.put(FlowExtWorktaskParam.SERVICE_TYPE, typeCode);
   		map.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, true);
   		CurrentSessionUser user = SecurityUtil.getSecurityUser();
   		try{
   		//流程记录表
   		QualitySug qualitySug=new QualitySug();
   		if (StringUtil.isEmpty(id)) {
   			return JsonWrapper.failureWrapper("参数错误！");
   		} else {
   			// 审批流程
   			if (StringUtil.isNotEmpty(activityId)) {
   				if(areaFlag.equals("1")){//部门负责人
   					Map<String, Object> map2 = qualitySgcjjybgManager.doProcess(map);
   					QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
   					
   					//发送消息
	   				List<Activity> list = (List<Activity>)map2.get("result_activity_list");
					for (Activity activity : list) {
						List<BpmUser> users = getBpmUserPaticipator(activity);
						for (BpmUser bpmUser : users) {
							String tel = qualitySgcjjybgManager.getUserTel(bpmUser);
							System.out.println("发送微信给>>>>>>:"+bpmUser.getName()+" \n this auditor's phone is :"+tel);
							//发送消息给审核人
							sendToAudit(qualitySgcjjybg,(HttpServletRequest)request,bpmUser,tel);
						}
					}
					//当前流程
					Activity activity = (Activity) this.activityDao.get(activityId);
					//发送消息给申请人
					sendToApply(qualitySgcjjybg,(HttpServletRequest)request,activity,list,user,"manualReportRemind");
   					//发送微信消息 end 
   					
   	   				qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_SHZ);
   	   				qualitySgcjjybg.setDepartmentMan(user.getName());	
	   				qualitySgcjjybg.setDepartmentManDate(new Date());
	   				qualitySgcjjybgManager.save(qualitySgcjjybg);
	   				
	   				//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
	  		  		logService.setLogs(id, 
	  		  				"手工出具检验报告/证书审批表审核", 
	  		  				"部门负责人审核。审核结果：通过", 
	  		  				user != null ? user.getId() : "未获取到操作用户编号",
	  						user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
	  						request != null ? request.getRemoteAddr() : "");
	  		  		
   	   				//审批记录
   	   				qualitySug.setBusinessId(id);
   	   				qualitySug.setBusinessName("手工出具检验报告申请");
   	   				qualitySug.setSug("审核通过");
   	   				qualitySug.setAss("");//签字id
	   				qualitySug.setSeal("");//盖章id
	   				qualitySug.setReturnName("");//退回环节名称
   	   				qualitySug.setSpUserId(user.getId());
   	   				qualitySug.setSpUserName(user.getName());
   	   				qualitySug.setSpTime(new Date());
   	   				qualitySug.setSpResult("通过");
   	   			    qualitySug.setSpLevel(areaFlag);
   	   			    qualitySug.setStatus(QualitySgcjjybgManager.ZL_SGCJ_SHZ);
   	   			    qualitySugManager.save(qualitySug);
   				}else if(areaFlag.equals("4")){//质量监督管理部负责人
   					QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
   					JSONObject dataBus = new JSONObject();
   					if(qualitySgcjjybg.getDepartmentId().equals("100028")){
   						dataBus.put("org", "2");//经办人
   					}else{
   						dataBus.put("org", "1");//软件负责人
   					}
   					map.put(FlowExtWorktaskParam.DATA_BUS, dataBus);
   					Map<String, Object> map2 = qualitySgcjjybgManager.doProcess(map);
   				    //发送消息
	   				List<Activity> list = (List<Activity>)map2.get("result_activity_list");
					for (Activity activity : list) {
						List<BpmUser> users = getBpmUserPaticipator(activity);
						for (BpmUser bpmUser : users) {
							String tel = qualitySgcjjybgManager.getUserTel(bpmUser);
							System.out.println("发送微信给>>>>>>:"+bpmUser.getName()+" \n this auditor's phone is :"+tel);
							//发送消息给审核人
							sendToAudit(qualitySgcjjybg,(HttpServletRequest)request,bpmUser,tel);
						}
					}
					//当前流程
					Activity activity = (Activity) this.activityDao.get(activityId);
					//发送消息给申请人
					sendToApply(qualitySgcjjybg,(HttpServletRequest)request,activity,list,user,"manualReportRemind");
   					//发送微信消息 end 
   	   				qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_SHZ);
   	   				qualitySgcjjybg.setZlshman(user.getName());	
   	   				qualitySgcjjybg.setZlshtime(new Date());
   	   				qualitySgcjjybg.setZlshyj(zlshyj);
   	   				qualitySgcjjybgManager.save(qualitySgcjjybg);
   	   				
   	   				//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
	  		  		logService.setLogs(id, 
	  		  				"手工出具检验报告/证书审批表审核", 
	  		  				"质量监督管理部负责人审核。审核结果：通过", 
	  		  				user != null ? user.getId() : "未获取到操作用户编号",
	  						user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
	  						request != null ? request.getRemoteAddr() : "");
   	   				//审批记录
   	   				qualitySug.setBusinessId(id);
   	   				qualitySug.setBusinessName("手工出具检验报告申请");
   	   				qualitySug.setSug("审核通过");
   	   				qualitySug.setAss("");//签字id
   	   				qualitySug.setSeal("");//盖章id
   	   				qualitySug.setReturnName("");//退回环节名称
   	   				qualitySug.setSpUserId(user.getId());
   	   				qualitySug.setSpUserName(user.getName());
   	   				qualitySug.setSpTime(new Date());
   	   				qualitySug.setSpResult("通过");
   	   			    qualitySug.setSpLevel(areaFlag);
   	   			    qualitySug.setStatus(QualitySgcjjybgManager.ZL_SGCJ_SHZ);
   	   			    qualitySugManager.save(qualitySug);
   				}else if(areaFlag.equals("2")){//检验软件负责人
   					Map<String, Object> map2 = qualitySgcjjybgManager.doProcess(map);
   					QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
   					//发送消息
	   				List<Activity> list = (List<Activity>)map2.get("result_activity_list");
					for (Activity activity : list) {
						List<BpmUser> users = getBpmUserPaticipator(activity);
						for (BpmUser bpmUser : users) {
							String tel = qualitySgcjjybgManager.getUserTel(bpmUser);
							System.out.println("发送微信给>>>>>>:"+bpmUser.getName()+" \n this auditor's phone is :"+tel);
							//发送消息给审核人
							sendToAudit(qualitySgcjjybg,(HttpServletRequest)request,bpmUser,tel);
						}
					}
					//当前流程
					Activity activity = (Activity) this.activityDao.get(activityId);
					//发送消息给申请人
					sendToApply(qualitySgcjjybg,(HttpServletRequest)request,activity,list,user,"manualReportRemind");
   					//发送微信消息 end 
   	   				qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_SHZ);
		   			qualitySgcjjybg.setJyrjfzr(user.getName());
		   			qualitySgcjjybg.setJyrjfzrDate(new Date());
   	   				qualitySgcjjybgManager.save(qualitySgcjjybg);
   	   				
   	   				//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
	  		  		logService.setLogs(id, 
	  		  				"手工出具检验报告/证书审批表审核", 
	  		  				"检验软件负责人审核。审核结果：通过", 
	  		  				user != null ? user.getId() : "未获取到操作用户编号",
	  						user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
	  						request != null ? request.getRemoteAddr() : "");
   	   				//审批记录
   	   				qualitySug.setBusinessId(id);
   	   				qualitySug.setBusinessName("手工出具检验报告申请");
   	   				qualitySug.setSug("审核通过");
   	   				qualitySug.setAss("");//签字id
	   				qualitySug.setSeal("");//盖章id
	   				qualitySug.setReturnName("");//退回环节名称
   	   				qualitySug.setSpUserId(user.getId());
   	   				qualitySug.setSpUserName(user.getName());
   	   				qualitySug.setSpTime(new Date());
   	   				qualitySug.setSpResult("通过");
   	   			    qualitySug.setSpLevel(areaFlag);
   	   			    qualitySug.setStatus(QualitySgcjjybgManager.ZL_SGCJ_SHZ);
   	   			    qualitySugManager.save(qualitySug);
   				}else if(areaFlag.equals("3")){//服务部门经办人
   					Map<String, Object> map2 = qualitySgcjjybgManager.doProcess(map);
   					QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
   					//发送消息
	   				List<Activity> list = (List<Activity>)map2.get("result_activity_list");
					for (Activity activity : list) {
						List<BpmUser> users = getBpmUserPaticipator(activity);
						for (BpmUser bpmUser : users) {
							String tel = qualitySgcjjybgManager.getUserTel(bpmUser);
							System.out.println("发送微信给>>>>>>:"+bpmUser.getName()+" \n this auditor's phone is :"+tel);
							//发送消息给审核人
							sendToAudit(qualitySgcjjybg,(HttpServletRequest)request,bpmUser,tel);
						}
					}
					//当前流程
					Activity activity = (Activity) this.activityDao.get(activityId);
					//发送消息给申请人
					sendToApply(qualitySgcjjybg,(HttpServletRequest)request,activity,list,user,"manualReportRemind");
   					//发送微信消息 end 
   	   				qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_PASS);
   	   				qualitySgcjjybg.setYwfwbjbr(user.getName());
   	   				qualitySgcjjybg.setYwfwbjbrDate(new Date());
   	   				
   	   				qualitySgcjjybg.setFwbjhbg(bgbh);
	   	   			qualitySgcjjybg.setFwbjhbg2(bgbh2);
		   	   		qualitySgcjjybg.setFwbjhbg3(bgbh3);
			   	   	qualitySgcjjybg.setFwbjhbg4(bgbh4);
				   	qualitySgcjjybg.setFwbjhbg5(bgbh5);
				   	qualitySgcjjybg.setFwbjhbg6(bgbh6);
				   	qualitySgcjjybg.setFwbjhbg7(bgbh7);
   	
   	   				qualitySgcjjybgManager.save(qualitySgcjjybg);
   	   				
   	   				//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
	  		  		logService.setLogs(id, 
	  		  				"手工出具检验报告/证书审批表审核", 
	  		  				"服务部门经办人审核。审核结果：通过", 
	  		  				user != null ? user.getId() : "未获取到操作用户编号",
	  						user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
	  						request != null ? request.getRemoteAddr() : "");
   	   				//审批记录
   	   				qualitySug.setBusinessId(id);
   	   				qualitySug.setBusinessName("手工出具检验报告申请");
   	   				qualitySug.setSug("审核通过");
   	   				qualitySug.setAss("");//签字id
	   				qualitySug.setSeal("");//盖章id
	   				qualitySug.setReturnName("");//退回环节名称
   	   				qualitySug.setSpUserId(user.getId());
   	   				qualitySug.setSpUserName(user.getName());
   	   				qualitySug.setSpTime(new Date());
   	   				qualitySug.setSpResult("通过");
   	   			    qualitySug.setSpLevel(areaFlag);
   	   			    qualitySug.setStatus(QualitySgcjjybgManager.ZL_SGCJ_PASS);
   	   			    qualitySugManager.save(qualitySug);
   	   				
   	   				String a=qualitySgcjjybg.getFwbjhbg();//服务部计划的报告/证书编号
   	   				
   	   				//生成任务书
   	   				if(qualitySgcjjybg.getFwbjhbg()==null || qualitySgcjjybg.getFwbjhbg().equals("") &&
   	   					qualitySgcjjybg.getEquipmentName()==null || qualitySgcjjybg.getEquipmentName().equals("")){
   	   				}else{
   	   					String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety()));
   	   					String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory()));
   	   					String p=taskbookManager.getbh();//编号
	   	   				if(qualitySgcjjybg.getStatusa().equals("0")){
	   	   					taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg(), 
	   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName(), c+","+d,
	   	   							qualitySgcjjybg.getNjts(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
	   	   							qualitySgcjjybg.getDepartmentId(),qualitySgcjjybg.getContractNumber(),
	   	   							qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
	   	   							qualitySgcjjybg.getRwdId1(),qualitySgcjjybg.getRwdSn1());
	   	   				}else{
	   	   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg(), 
   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName(), "",
   	   							qualitySgcjjybg.getNjts(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
   	   							qualitySgcjjybg.getDepartmentId(),qualitySgcjjybg.getContractNumber(),
   	   							qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
   	   							qualitySgcjjybg.getRwdId1(),qualitySgcjjybg.getRwdSn1());
	   	   				}
   	   				}
   	   				//生成任务书2
   	   				if(qualitySgcjjybg.getFwbjhbg2()==null || qualitySgcjjybg.getFwbjhbg2().equals("") &&
   	   					qualitySgcjjybg.getEquipmentName2()==null || qualitySgcjjybg.getEquipmentName2().equals("")){
   	   				}else{
   	   					String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety2()));
   	   					String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory2()));
   	   					String p=taskbookManager.getbh();//编号
   	   					if(qualitySgcjjybg.getStatusa().equals("0")){
   	   						taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg2(), 
   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName2(), c+","+d,
   	   							qualitySgcjjybg.getNjts2(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
   	   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber2(),
   	   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
   	   	   				qualitySgcjjybg.getRwdId2(),qualitySgcjjybg.getRwdSn2());
	   	   				}else{
	   	   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg2(), 
   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName2(), "",
   	   							qualitySgcjjybg.getNjts2(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
   	   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber2(),
   	   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
   	   	   				qualitySgcjjybg.getRwdId2(),qualitySgcjjybg.getRwdSn2());
	   	   				}
   	   				}
   	   				//生成任务书3
   	   				if(qualitySgcjjybg.getFwbjhbg3()==null || qualitySgcjjybg.getFwbjhbg3().equals("") &&
   	   					qualitySgcjjybg.getEquipmentName3()==null || qualitySgcjjybg.getEquipmentName3().equals("")){
   	   				}else{
   	   					String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety3()));
   	   					String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory3()));
   	   					String p=taskbookManager.getbh();//编号
	   	   				if(qualitySgcjjybg.getStatusa().equals("0")){
	   	   					taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg3(), 
	   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName3(), c+","+d,
	   	   							qualitySgcjjybg.getNjts3(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
	   	   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber3(),
	   	   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
	   	   	   				qualitySgcjjybg.getRwdId3(),qualitySgcjjybg.getRwdSn3());
	   	   				}else{
	   	   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg3(), 
   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName3(), "",
   	   							qualitySgcjjybg.getNjts3(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
   	   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber3(),
   	   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
   	   	   				qualitySgcjjybg.getRwdId3(),qualitySgcjjybg.getRwdSn3());	
	   	   				}
   	   				}
   	   				//生成任务书4
   	   				if(qualitySgcjjybg.getFwbjhbg4()==null || qualitySgcjjybg.getFwbjhbg4().equals("") &&
   	   					qualitySgcjjybg.getEquipmentName4()==null || qualitySgcjjybg.getEquipmentName4().equals("")){
   	   				}else{
   	   					String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety4()));
   	   					String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory4()));
   	   					String p=taskbookManager.getbh();//编号
	   	   				if(qualitySgcjjybg.getStatusa().equals("0")){
	   	   					taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg4(), 
	   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName4(), c+","+d,
	   	   							qualitySgcjjybg.getNjts4(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
	   	   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber4(),
	   	   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
	   	   	   				qualitySgcjjybg.getRwdId4(),qualitySgcjjybg.getRwdSn4());
	   	   				}else{
	   	   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg4(), 
   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName4(), "",
   	   							qualitySgcjjybg.getNjts4(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
   	   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber4(),
   	   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
   	   	   				qualitySgcjjybg.getRwdId4(),qualitySgcjjybg.getRwdSn4());
		   				}
   	   				}
   	   				//生成任务书5
   	   				if(qualitySgcjjybg.getFwbjhbg5()==null || qualitySgcjjybg.getFwbjhbg5().equals("") &&
   	   					qualitySgcjjybg.getEquipmentName5()==null || qualitySgcjjybg.getEquipmentName5().equals("")){
   	   				}else{
   	   					String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety5()));
   	   					String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory5()));
   	   					String p=taskbookManager.getbh();//编号
	   	   				if(qualitySgcjjybg.getStatusa().equals("0")){
	   	   					taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg5(), 
	   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName5(), c+","+d,
	   	   							qualitySgcjjybg.getNjts5(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
	   	   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber5(),
	   	   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
	   	   	   				qualitySgcjjybg.getRwdId5(),qualitySgcjjybg.getRwdSn5());
	   	   				}else{
	   	   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg5(), 
   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName5(), "",
   	   							qualitySgcjjybg.getNjts5(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
   	   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber5(),
   	   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
   	   	   				qualitySgcjjybg.getRwdId5(),qualitySgcjjybg.getRwdSn5());
		   				}
   	   				}
   	   				//生成任务书6
   	   				if(qualitySgcjjybg.getFwbjhbg6()==null || qualitySgcjjybg.getFwbjhbg6().equals("") &&
   	   					qualitySgcjjybg.getEquipmentName6()==null || qualitySgcjjybg.getEquipmentName6().equals("")){
   	   				}else{
   	   					String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety6()));
   	   					String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory6()));
   	   					String p=taskbookManager.getbh();//编号
	   	   				if(qualitySgcjjybg.getStatusa().equals("0")){
	   	   					taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg6(), 
	   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName6(), c+","+d,
	   	   							qualitySgcjjybg.getNjts6(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
	   	   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber6(),
	   	   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
	   	   	   				qualitySgcjjybg.getRwdId6(),qualitySgcjjybg.getRwdSn6());
	   	   				}else{
	   	   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg6(), 
   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName6(), "",
   	   							qualitySgcjjybg.getNjts6(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
   	   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber6(),
   	   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
   	   	   				qualitySgcjjybg.getRwdId6(),qualitySgcjjybg.getRwdSn6());
		   				}
   	   				}
   	   				//生成任务书7
   	   				if(qualitySgcjjybg.getFwbjhbg7()==null || qualitySgcjjybg.getFwbjhbg7().equals("") &&
   	   					qualitySgcjjybg.getEquipmentName7()==null || qualitySgcjjybg.getEquipmentName7().equals("")){
   	   				}else{
   	   					String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety7()));
   	   					String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory7()));
   	   					String p=taskbookManager.getbh();//编号
	   	   				if(qualitySgcjjybg.getStatusa().equals("0")){
	   	   					taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg7(), 
	   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName7(), c+","+d,
	   	   							qualitySgcjjybg.getNjts7(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
	   	   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber7(),
	   	   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
	   	   	   				qualitySgcjjybg.getRwdId7(),qualitySgcjjybg.getRwdSn7());
	   	   				}else{
	   	   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg7(), 
   	   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName7(), "",
   	   							qualitySgcjjybg.getNjts7(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
   	   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber7(),
   	   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
   	   	   				qualitySgcjjybg.getRwdId7(),qualitySgcjjybg.getRwdSn7());	
		   				}
   	   				}
   				}else{
   					QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
   	   				qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_NO_PASS);
   	   				qualitySgcjjybgManager.save(qualitySgcjjybg);
   				}
   			} else {
   				return JsonWrapper.failureWrapper("流程ID为空！");
   			}
   		
   				
	   		
   			}
	   		}catch(Exception e){
	   			
	   		e.printStackTrace();
	   		
	   		}
   		return JsonWrapper.successWrapper(id);

   	}
//   	public static void main(String[] args){
//   		String v="C777-999,A888-555,C777-955,R999-2,R999-3";
//   		List list=new ArrayList();//-前
//   		List list2=new ArrayList();
//   		List list3=new ArrayList();//-后
//
//   		String arr[] =v.split(",");
//   		for(int i = 0; i < arr.length; i++){
//   			list2.add(arr[i]);
//   	   		//System.out.println(arr[i]);
//   	   		String arr1[] = arr[i].split("-");
//   	   		for (int qwe = 0; qwe < arr1.length; qwe=qwe+2) {
//   	   			list.add(arr1[qwe]);
//   	   			//System.out.println(list.toString());
//   	   	   		System.out.println("arr1[p]="+arr1[qwe]);
//   	   		}
//   	   		String getSignInfo = arr[i].substring(arr[i].indexOf("-") + 1);
//   	   		list3.add(getSignInfo);   		
//   	   	}
//   		System.out.println(list3.toString());
//   		list = (ArrayList<String>) getNewList(list);
//   		//System.out.println(list.toString());
//   		for (int j = 0; j < list.size(); j++) {
//   			//System.out.println(list.get(j));
////			for (int i = 0; i < arr.length; i++) {
////				if(i!=j){
////					//System.out.println(list.get(i).equals(list.get(j)));
////					if(list.get(i).equals(list.get(j))){
////						//System.out.println(list.get(i));
////					}else{
////						//System.out.println("1");
////					}
////				}
////			}
//   		}
//   		//System.out.println(list.get(0));
//   	} 
   	public static List<String> getNewList(List<String> li){
        List<String> list = new ArrayList<String>();
        for(int i=0; i<li.size(); i++){
            String str = li.get(i);  //获取传入集合对象的每一个元素
            if(!list.contains(str)){   //查看新集合中是否有指定的元素，如果没有则加入
                list.add(str);
            }
        }
        return list;  //返回集合
    }

 	/**手工出具检验报告/证书审批表退回审批流程并改变状态
	 * @param id
	 * @param flowId
	 * @param typeCode
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "sgcjjybgth")
	@ResponseBody
	public HashMap<String, Object> sgcjjybgth(ServletRequest request,String areaFlag,String id,
			String typeCode, String status,String activityId,String processId,String zlshyj) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map1 = new HashMap<String, Object>();

		map.put(FlowExtWorktaskParam.SERVICE_ID, id);
		map.put(FlowExtWorktaskParam.ACTIVITY_ID, activityId);
   		map.put(FlowExtWorktaskParam.SERVICE_TITLE, "手工出具检验报告申请");
		map.put(FlowExtWorktaskParam.SERVICE_TYPE, typeCode);
		map.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, true);
		
		map1.put(FlowExtWorktaskParam.PROCESS_ID, processId);
		map1.put(FlowExtWorktaskParam.FINISH_TYPE,FinishFlowInf.FINISH_TYPE_TERMINATE);
		
		//流程记录表
   		QualitySug qualitySug=new QualitySug();
   		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		if (StringUtil.isEmpty(id)) {
			return JsonWrapper.failureWrapper("参数错误！");
		} else {
			// 退回流程
			if (StringUtil.isNotEmpty(activityId)) {
				//当前流程
				Activity activity = (Activity) this.activityDao.get(activityId);
				List<Activity> list = new ArrayList<Activity>();
				list.add(activity);
				//qualitySgcjjybgManager.stop(map1);
				QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
				
				//发送消息给申请人
				sendToApply(qualitySgcjjybg,(HttpServletRequest)request,activity,null,user,"manualReportStop");
				
   				qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_NO_PASS);
   				if(areaFlag.equals("1")){
   					qualitySgcjjybg.setDepartmentMan(user.getName());	
   					qualitySgcjjybg.setDepartmentManDate(new Date());
   					//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
	  		  		logService.setLogs(id, 
	  		  				"手工出具检验报告/证书审批表审核", 
	  		  				"部门负责人审核。审核结果：不通过", 
	  		  				user != null ? user.getId() : "未获取到操作用户编号",
	  						user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
	  						request != null ? request.getRemoteAddr() : "");
   				}else if(areaFlag.equals("4")){
   					qualitySgcjjybg.setZlshman(user.getName());	
   					qualitySgcjjybg.setZlshtime(new Date());
   					qualitySgcjjybg.setZlshyj(zlshyj);
   					//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
	  		  		logService.setLogs(id, 
	  		  				"手工出具检验报告/证书审批表审核", 
	  		  				"质量监督管理部负责人审核。审核结果：不通过", 
	  		  				user != null ? user.getId() : "未获取到操作用户编号",
	  						user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
	  						request != null ? request.getRemoteAddr() : "");
   				}else if(areaFlag.equals("2")){
   					qualitySgcjjybg.setYwfwbjbr(user.getName());
   					qualitySgcjjybg.setYwfwbjbrDate(new Date());
   					//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
	  		  		logService.setLogs(id, 
	  		  				"手工出具检验报告/证书审批表审核", 
	  		  				"检验软件负责人审核。审核结果：不通过", 
	  		  				user != null ? user.getId() : "未获取到操作用户编号",
	  						user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
	  						request != null ? request.getRemoteAddr() : "");
   				}else if(areaFlag.equals("3")){
   					qualitySgcjjybg.setJyrjfzr(user.getName());
   					qualitySgcjjybg.setJyrjfzrDate(new Date());
   					logService.setLogs(id, 
	  		  				"手工出具检验报告/证书审批表审核", 
	  		  				"服务部门经办人审核。审核结果：不通过", 
	  		  				user != null ? user.getId() : "未获取到操作用户编号",
	  						user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
	  						request != null ? request.getRemoteAddr() : "");
   					//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
   				}
   				qualitySgcjjybgManager.save(qualitySgcjjybg);
   				
   				//审批记录
   				qualitySug.setBusinessId(id);
   				qualitySug.setBusinessName("手工出具检验报告申请");
   				qualitySug.setSug("不通过");
   				qualitySug.setAss("");//签字id
   				qualitySug.setSeal("");//盖章id
   				if(areaFlag.equals("1")){
   					qualitySug.setReturnName("部门负责人审核");//退回环节名称
   					qualitySug.setSpLevel(areaFlag);
   				}else if(areaFlag.equals("2")){
   					qualitySug.setReturnName("检验软件负责人审核");//退回环节名称
   					qualitySug.setSpLevel(areaFlag);
   				}else if(areaFlag.equals("3")){
   					qualitySug.setReturnName("检验软件负责人审核");//退回环节名称
   					qualitySug.setSpLevel(areaFlag);
   				}else if(areaFlag.equals("4")){
   					qualitySug.setReturnName("质量监督管理部审核");//退回环节名称
   					qualitySug.setSpLevel(areaFlag);
   				}
   				qualitySug.setSpUserId(user.getId());
   				qualitySug.setSpUserName(user.getName());
   				qualitySug.setSpTime(new Date());
   				qualitySug.setSpResult("未通过");
   			    qualitySug.setStatus(QualitySgcjjybgManager.ZL_SGCJ_NO_PASS);
   			    qualitySugManager.save(qualitySug);
			} else {
				return JsonWrapper.failureWrapper("流程ID为空！");
			}
			return JsonWrapper.successWrapper(id);
		}

	}
	// 查询流程步骤信息
	@RequestMapping(value = "getFlowStep")
	@ResponseBody
	public ModelAndView getFlowStep(HttpServletRequest request)
			throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		map = qualitySgcjjybgManager.getFlowStep(request.getParameter("id"));

		ModelAndView mav = new ModelAndView("app/qualitymanage/flow_card", map);

		return mav;

	}
	/**
	 * 手工出具检验报告申请作废处理
	 * @param request
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="cancel")
	@ResponseBody
	public HashMap<String, Object> cancel (ServletRequest request,String ids) throws Exception{
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String []idArry = ids.split(",");
		for(String id : idArry) {
			QualitySgcjjybg qualitySgcjjybg = qualitySgcjjybgManager.get(id);
			qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_CANCEL);
			qualitySgcjjybgManager.save(qualitySgcjjybg);
			//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
		  		logService.setLogs(id, 
		  				"手工出具检验报告/证书审批表作废", 
		  				"作废操作。操作结果：已作废", 
		  				user != null ? user.getId() : "未获取到操作用户编号",
						user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
						request != null ? request.getRemoteAddr() : "");
		}
		return JsonWrapper.successWrapper(ids);
	}
	/**
   	 * 添加
   	 * @param response
     * @throws Exception 
   	 */
    @RequestMapping(value="weixinSave")
    @ResponseBody
   	public HashMap<String, Object> weixinSave(HttpServletRequest request,@RequestBody QualitySgcjjybg qualitySgcjjybg) throws Exception {
   		CurrentSessionUser user = SecurityUtil.getSecurityUser();
   		HashMap<String, Object> map = new HashMap<String, Object>();
   		try {
   			String id = qualitySgcjjybg.getId();
   			if(StringUtil.isEmpty(id)){
   			if(StringUtil.isNotEmpty(qualitySgcjjybg.getRwdSn1())){
   				ContractTaskInfo info=contractTaskInfoDao.getContractTaskInfoBySn(qualitySgcjjybg.getRwdSn1(),user.getDepartment().getId());
   	   			if(info==null){
   	   			map.put("success", false);
   		   		map.put("msg", "没有找到编号为"+qualitySgcjjybg.getRwdSn1()+"的检验任务单。");
   		   		return map;
   	   			}else{
   	   			qualitySgcjjybg.setRwdId1(info.getId());
   	   			}
   			}
   				qualitySgcjjybgManager.saveSgcjjybg(request,qualitySgcjjybg,user);
   			}else{
   				qualitySgcjjybg = qualitySgcjjybgManager.get(id);
   			}
   			qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_YTJ);
   			qualitySgcjjybgManager.save(qualitySgcjjybg);
   			
   			//流程传参
   			String flowId = request.getParameter("flowId");
   			String typeCode = request.getParameter("typeCode");
   			
   			HashMap<String, Object> flowMap = new HashMap<String, Object>();
   			flowMap.put(FlowExtWorktaskParam.SERVICE_ID, qualitySgcjjybg.getId());
   			flowMap.put(FlowExtWorktaskParam.ACTIVITY_ID, null);
   			flowMap.put(FlowExtWorktaskParam.SERVICE_TITLE, "手工出具检验报告申请");
   			flowMap.put(FlowExtWorktaskParam.FLOW_DEFINITION_ID, flowId);
   			flowMap.put(FlowExtWorktaskParam.SERVICE_TYPE, typeCode);
   			flowMap.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, true);
   			flowMap.put("bpm_user", ((CurrentBpmSessionUser) user).getBpmUser());
   	   		// 启动流程
   			if (StringUtil.isNotEmpty(flowId)) {
   				Map<String, Object> map2 = qualitySgcjjybgManager.doStartPress(flowMap);
   				//发送微信
   				List<Activity> list = (List<Activity>)map2.get("result_activity_list");
				for (Activity activity : list) {
					List<BpmUser> users = getBpmUserPaticipator(activity);
					for (BpmUser bpmUser : users) {
						String tel = qualitySgcjjybgManager.getUserTel(bpmUser);
						System.out.println("发送微信给>>>>>>:"+bpmUser.getName()+" \n this auditor's phone is :"+tel);
						//发送消息给审核人
						sendToAudit(qualitySgcjjybg,(HttpServletRequest)request,bpmUser,tel);
					}
				}
				//发送消息给申请人
				sendToApply(qualitySgcjjybg,(HttpServletRequest)request,null,list,user,"manualReportRemind");
				
				//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
   		  		logService.setLogs(qualitySgcjjybg.getId(), 
   		  				"提交手工出具检验报告/证书审批表", 
   		  				"提交手工出具检验报告/证书审批表至部门负责人审核。操作结果：已提交", 
   		  				user != null ? user.getId() : "未获取到操作用户编号",
   						user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
   						request != null ? request.getRemoteAddr() : "");
   			}
   			map.put("success", true);
	   		map.put("data", qualitySgcjjybg);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
	   		map.put("msg", e.getMessage());
		}
   		return map;
   	}
	@RequestMapping(value="myApply")
	@ResponseBody
	public HashMap<String, Object> myApply (ServletRequest request) throws Exception{
		
		HashMap<String, Object> map = new HashMap<String,Object>();
		try {
			List list = qualitySgcjjybgManager.myApply();
			map.put("success", true);
			map.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	@RequestMapping(value="waitForDeal")
	@ResponseBody
	public HashMap<String, Object> waitForDeal (ServletRequest request) throws Exception{
		
		HashMap<String, Object> map = new HashMap<String,Object>();
		try {
			List list = qualitySgcjjybgManager.waitForDeal();
			map.put("success", true);
			map.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	@RequestMapping(value="dealed")
	@ResponseBody
	public HashMap<String, Object> dealed (ServletRequest request) throws Exception{
		
		HashMap<String, Object> map = new HashMap<String,Object>();
		try {
			List list = qualitySgcjjybgManager.dealed();
			map.put("success", true);
			map.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	/**手工出具检验报告/证书审批表审批流程并改变状态  微信端
	 * @param id
	 * @param flowId
	 * @param typeCode
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "check")
	@ResponseBody
	public HashMap<String, Object> check(ServletRequest request,String areaFlag,String id,
			String typeCode, String status,String activityId,
			String bgbh,String bgbh2,String bgbh3,String bgbh4,String bgbh5,String bgbh6,String bgbh7
			,String zlshyj) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(FlowExtWorktaskParam.SERVICE_ID, id);
			map.put(FlowExtWorktaskParam.ACTIVITY_ID, activityId);
			//当前流程
			Activity currentActivity = (Activity) this.activityDao.get(activityId);
			
			map.put(FlowExtWorktaskParam.SERVICE_TITLE, "手工出具检验报告申请");
			map.put(FlowExtWorktaskParam.SERVICE_TYPE, typeCode);
			map.put(FlowExtWorktaskParam.IS_CURRENT_USER_TASK, true);
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			//流程记录表
			QualitySug qualitySug=new QualitySug();
			if (StringUtil.isEmpty(id)) {
				return JsonWrapper.failureWrapper("参数错误！");
			} else {
				// 审批流程
				if (StringUtil.isNotEmpty(activityId)) {
					if(areaFlag.equals("1")){//部门负责人
						Map<String, Object> map2 = qualitySgcjjybgManager.doProcess(map);
						QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
						//发送微信
		   				List<Activity> list = (List<Activity>)map2.get("result_activity_list");
						for (Activity activity : list) {
							List<BpmUser> users = getBpmUserPaticipator(activity);
							for (BpmUser bpmUser : users) {
								String tel = qualitySgcjjybgManager.getUserTel(bpmUser);
								System.out.println("发送微信给>>>>>>:"+bpmUser.getName()+" \n this auditor's phone is :"+tel);
								sendToAudit(qualitySgcjjybg,(HttpServletRequest)request,bpmUser,tel);
							}
						}
						
						//发送消息给申请人
						sendToApply(qualitySgcjjybg,(HttpServletRequest)request,currentActivity,list,user,"manualReportRemind");
						
						qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_SHZ);
						qualitySgcjjybg.setDepartmentMan(user.getName());	
						qualitySgcjjybg.setDepartmentManDate(new Date());
						qualitySgcjjybgManager.save(qualitySgcjjybg);
						
						//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
				  		logService.setLogs(id, 
				  				"手工出具检验报告/证书审批表审核", 
				  				"部门负责人审核。审核结果：通过", 
				  				user != null ? user.getId() : "未获取到操作用户编号",
								user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
								request != null ? request.getRemoteAddr() : "");
				  		
						//审批记录
						qualitySug.setBusinessId(id);
						qualitySug.setBusinessName("手工出具检验报告申请");
						qualitySug.setSug("审核通过");
						qualitySug.setAss("");//签字id
						qualitySug.setSeal("");//盖章id
						qualitySug.setReturnName("");//退回环节名称
						qualitySug.setSpUserId(user.getId());
						qualitySug.setSpUserName(user.getName());
						qualitySug.setSpTime(new Date());
						qualitySug.setSpResult("通过");
					    qualitySug.setSpLevel(areaFlag);
					    qualitySug.setStatus(QualitySgcjjybgManager.ZL_SGCJ_SHZ);
					    qualitySugManager.save(qualitySug);
					}else if(areaFlag.equals("4")){//质量监督管理部负责人
						QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
						JSONObject dataBus = new JSONObject();
						if(qualitySgcjjybg.getDepartmentId().equals("100028")){
							dataBus.put("org", "2");//经办人
						}else{
							dataBus.put("org", "1");//软件负责人
						}
						map.put(FlowExtWorktaskParam.DATA_BUS, dataBus);
						Map<String, Object> map2 = qualitySgcjjybgManager.doProcess(map);
						
						//发送消息
		   				List<Activity> list = (List<Activity>)map2.get("result_activity_list");
						for (Activity activity : list) {
							List<BpmUser> users = getBpmUserPaticipator(activity);
							for (BpmUser bpmUser : users) {
								String tel = qualitySgcjjybgManager.getUserTel(bpmUser);
								System.out.println("发送微信给>>>>>>:"+bpmUser.getName()+" \n this auditor's phone is :"+tel);
								sendToAudit(qualitySgcjjybg,(HttpServletRequest)request,bpmUser,tel);
							}
						}
						//发送消息给申请人
						sendToApply(qualitySgcjjybg,(HttpServletRequest)request,currentActivity,list,user,"manualReportRemind");
	   					//发送微信消息 end 
						qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_SHZ);
						qualitySgcjjybg.setZlshman(user.getName());	
						qualitySgcjjybg.setZlshtime(new Date());
						qualitySgcjjybg.setZlshyj(zlshyj);
						qualitySgcjjybgManager.save(qualitySgcjjybg);
						
						//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
				  		logService.setLogs(id, 
				  				"手工出具检验报告/证书审批表审核", 
				  				"质量监督管理部负责人审核。审核结果：通过", 
				  				user != null ? user.getId() : "未获取到操作用户编号",
								user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
								request != null ? request.getRemoteAddr() : "");
						//审批记录
						qualitySug.setBusinessId(id);
						qualitySug.setBusinessName("手工出具检验报告申请");
						qualitySug.setSug("审核通过");
						qualitySug.setAss("");//签字id
						qualitySug.setSeal("");//盖章id
						qualitySug.setReturnName("");//退回环节名称
						qualitySug.setSpUserId(user.getId());
						qualitySug.setSpUserName(user.getName());
						qualitySug.setSpTime(new Date());
						qualitySug.setSpResult("通过");
					    qualitySug.setSpLevel(areaFlag);
					    qualitySug.setStatus(QualitySgcjjybgManager.ZL_SGCJ_SHZ);
					    qualitySugManager.save(qualitySug);
					}else if(areaFlag.equals("2")){//检验软件负责人
						Map<String, Object> map2 = qualitySgcjjybgManager.doProcess(map);
						QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
						//发送消息
		   				List<Activity> list = (List<Activity>)map2.get("result_activity_list");
						for (Activity activity : list) {
							List<BpmUser> users = getBpmUserPaticipator(activity);
							for (BpmUser bpmUser : users) {
								String tel = qualitySgcjjybgManager.getUserTel(bpmUser);
								System.out.println("发送微信给>>>>>>:"+bpmUser.getName()+" \n this auditor's phone is :"+tel);
								//发送消息给审核人
								sendToAudit(qualitySgcjjybg,(HttpServletRequest)request,bpmUser,tel);
							}
						}
						//发送消息给申请人
						sendToApply(qualitySgcjjybg,(HttpServletRequest)request,currentActivity,list,user,"manualReportRemind");
	   					//发送微信消息 end 
						qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_SHZ);
			   			qualitySgcjjybg.setJyrjfzr(user.getName());
			   			qualitySgcjjybg.setJyrjfzrDate(new Date());
						qualitySgcjjybgManager.save(qualitySgcjjybg);
						
						//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
				  		logService.setLogs(id, 
				  				"手工出具检验报告/证书审批表审核", 
				  				"检验软件负责人审核。审核结果：通过", 
				  				user != null ? user.getId() : "未获取到操作用户编号",
								user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
								request != null ? request.getRemoteAddr() : "");
						//审批记录
						qualitySug.setBusinessId(id);
						qualitySug.setBusinessName("手工出具检验报告申请");
						qualitySug.setSug("审核通过");
						qualitySug.setAss("");//签字id
						qualitySug.setSeal("");//盖章id
						qualitySug.setReturnName("");//退回环节名称
						qualitySug.setSpUserId(user.getId());
						qualitySug.setSpUserName(user.getName());
						qualitySug.setSpTime(new Date());
						qualitySug.setSpResult("通过");
					    qualitySug.setSpLevel(areaFlag);
					    qualitySug.setStatus(QualitySgcjjybgManager.ZL_SGCJ_SHZ);
					    qualitySugManager.save(qualitySug);
					}else if(areaFlag.equals("3")){//服务部门经办人
						Map<String,Object> map2 = qualitySgcjjybgManager.doProcess(map);
						QualitySgcjjybg qualitySgcjjybg = qualitySgcjjybgManager.get(id);
						
						//发送消息
		   				List<Activity> list = (List<Activity>)map2.get("result_activity_list");
						for (Activity activity : list) {
							List<BpmUser> users = getBpmUserPaticipator(activity);
							for (BpmUser bpmUser : users) {
								String tel = qualitySgcjjybgManager.getUserTel(bpmUser);
								System.out.println("发送微信给>>>>>>:"+bpmUser.getName()+" \n this auditor's phone is :"+tel);
								//发送消息给审核人
								sendToAudit(qualitySgcjjybg,(HttpServletRequest)request,bpmUser,tel);
							}
						}
						//发送消息给申请人
						sendToApply(qualitySgcjjybg,(HttpServletRequest)request,currentActivity,list,user,"manualReportRemind");
	   					//发送微信消息 end 
						qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_PASS);
						qualitySgcjjybg.setYwfwbjbr(user.getName());
						qualitySgcjjybg.setYwfwbjbrDate(new Date());
						
						qualitySgcjjybg.setFwbjhbg(bgbh);
			   			qualitySgcjjybg.setFwbjhbg2(bgbh2);
			   	   		qualitySgcjjybg.setFwbjhbg3(bgbh3);
				   	   	qualitySgcjjybg.setFwbjhbg4(bgbh4);
					   	qualitySgcjjybg.setFwbjhbg5(bgbh5);
					   	qualitySgcjjybg.setFwbjhbg6(bgbh6);
					   	qualitySgcjjybg.setFwbjhbg7(bgbh7);

						qualitySgcjjybgManager.save(qualitySgcjjybg);
						
						//业务编号、操作动作、操作描述、操作用户编号、操作用户姓名、操作时间、操作IP
				  		logService.setLogs(id, 
				  				"手工出具检验报告/证书审批表审核", 
				  				"服务部门经办人审核。审核结果：通过", 
				  				user != null ? user.getId() : "未获取到操作用户编号",
								user != null ? user.getName() : "未获取到操作用户姓名", new Date(),
								request != null ? request.getRemoteAddr() : "");
						//审批记录
						qualitySug.setBusinessId(id);
						qualitySug.setBusinessName("手工出具检验报告申请");
						qualitySug.setSug("审核通过");
						qualitySug.setAss("");//签字id
						qualitySug.setSeal("");//盖章id
						qualitySug.setReturnName("");//退回环节名称
						qualitySug.setSpUserId(user.getId());
						qualitySug.setSpUserName(user.getName());
						qualitySug.setSpTime(new Date());
						qualitySug.setSpResult("通过");
					    qualitySug.setSpLevel(areaFlag);
					    qualitySug.setStatus(QualitySgcjjybgManager.ZL_SGCJ_PASS);
					    qualitySugManager.save(qualitySug);
						
						String a=qualitySgcjjybg.getFwbjhbg();//服务部计划的报告/证书编号
						
						//生成任务书
						if(qualitySgcjjybg.getFwbjhbg()==null || qualitySgcjjybg.getFwbjhbg().equals("") &&
							qualitySgcjjybg.getEquipmentName()==null || qualitySgcjjybg.getEquipmentName().equals("")){
						}else{
							String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety()));
							String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory()));
							String p=taskbookManager.getbh();//编号
			   				if(qualitySgcjjybg.getStatusa().equals("0")){
			   					taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg(), 
			   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName(), c+","+d,
			   							qualitySgcjjybg.getNjts(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
			   							qualitySgcjjybg.getDepartmentId(),qualitySgcjjybg.getContractNumber(),
			   							qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   							qualitySgcjjybg.getRwdId1(),qualitySgcjjybg.getRwdSn1());
			   				}else{
			   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg(), 
									qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName(), "",
									qualitySgcjjybg.getNjts(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
									qualitySgcjjybg.getDepartmentId(),qualitySgcjjybg.getContractNumber(),
									qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
									qualitySgcjjybg.getRwdId1(),qualitySgcjjybg.getRwdSn1());
			   				}
						}
						//生成任务书2
						if(qualitySgcjjybg.getFwbjhbg2()==null || qualitySgcjjybg.getFwbjhbg2().equals("") &&
							qualitySgcjjybg.getEquipmentName2()==null || qualitySgcjjybg.getEquipmentName2().equals("")){
						}else{
							String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety2()));
							String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory2()));
							String p=taskbookManager.getbh();//编号
							if(qualitySgcjjybg.getStatusa().equals("0")){
								taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg2(), 
									qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName2(), c+","+d,
									qualitySgcjjybg.getNjts2(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
									qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber2(),
			   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   						qualitySgcjjybg.getRwdId2(),qualitySgcjjybg.getRwdSn2());
			   				}else{
			   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg2(), 
									qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName2(), "",
									qualitySgcjjybg.getNjts2(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
									qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber2(),
			   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   						qualitySgcjjybg.getRwdId2(),qualitySgcjjybg.getRwdSn2());
			   				}
						}
						//生成任务书3
						if(qualitySgcjjybg.getFwbjhbg3()==null || qualitySgcjjybg.getFwbjhbg3().equals("") &&
							qualitySgcjjybg.getEquipmentName3()==null || qualitySgcjjybg.getEquipmentName3().equals("")){
						}else{
							String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety3()));
							String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory3()));
							String p=taskbookManager.getbh();//编号
			   				if(qualitySgcjjybg.getStatusa().equals("0")){
			   					taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg3(), 
			   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName3(), c+","+d,
			   							qualitySgcjjybg.getNjts3(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
			   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber3(),
			   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   	   					qualitySgcjjybg.getRwdId3(),qualitySgcjjybg.getRwdSn3());
			   				}else{
			   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg3(), 
									qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName3(), "",
									qualitySgcjjybg.getNjts3(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
									qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber3(),
			   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   						qualitySgcjjybg.getRwdId3(),qualitySgcjjybg.getRwdSn3());	
			   				}
						}
						//生成任务书4
						if(qualitySgcjjybg.getFwbjhbg4()==null || qualitySgcjjybg.getFwbjhbg4().equals("") &&
							qualitySgcjjybg.getEquipmentName4()==null || qualitySgcjjybg.getEquipmentName4().equals("")){
						}else{
							String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety4()));
							String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory4()));
							String p=taskbookManager.getbh();//编号
			   				if(qualitySgcjjybg.getStatusa().equals("0")){
			   					taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg4(), 
			   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName4(), c+","+d,
			   							qualitySgcjjybg.getNjts4(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
			   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber4(),
			   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   	   					qualitySgcjjybg.getRwdId4(),qualitySgcjjybg.getRwdSn4());
			   				}else{
			   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg4(), 
									qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName4(), "",
									qualitySgcjjybg.getNjts4(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
									qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber4(),
			   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   						qualitySgcjjybg.getRwdId4(),qualitySgcjjybg.getRwdSn4());
			   				}
						}
						//生成任务书5
						if(qualitySgcjjybg.getFwbjhbg5()==null || qualitySgcjjybg.getFwbjhbg5().equals("") &&
							qualitySgcjjybg.getEquipmentName5()==null || qualitySgcjjybg.getEquipmentName5().equals("")){
						}else{
							String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety5()));
							String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory5()));
							String p=taskbookManager.getbh();//编号
			   				if(qualitySgcjjybg.getStatusa().equals("0")){
			   					taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg5(), 
			   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName5(), c+","+d,
			   							qualitySgcjjybg.getNjts5(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
			   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber5(),
			   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   	   					qualitySgcjjybg.getRwdId5(),qualitySgcjjybg.getRwdSn5());
			   				}else{
			   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg5(), 
									qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName5(), "",
									qualitySgcjjybg.getNjts5(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
									qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber5(),
			   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   						qualitySgcjjybg.getRwdId5(),qualitySgcjjybg.getRwdSn5());
			   				}
						}
						//生成任务书6
						if(qualitySgcjjybg.getFwbjhbg6()==null || qualitySgcjjybg.getFwbjhbg6().equals("") &&
							qualitySgcjjybg.getEquipmentName6()==null || qualitySgcjjybg.getEquipmentName6().equals("")){
						}else{
							String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety6()));
							String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory6()));
							String p=taskbookManager.getbh();//编号
			   				if(qualitySgcjjybg.getStatusa().equals("0")){
			   					taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg6(), 
			   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName6(), c+","+d,
			   							qualitySgcjjybg.getNjts6(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
			   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber6(),
			   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   	   					qualitySgcjjybg.getRwdId6(),qualitySgcjjybg.getRwdSn6());
			   				}else{
			   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg6(), 
									qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName6(), "",
									qualitySgcjjybg.getNjts6(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
									qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber6(),
			   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   						qualitySgcjjybg.getRwdId6(),qualitySgcjjybg.getRwdSn6());
			   				}
						}
						//生成任务书7
						if(qualitySgcjjybg.getFwbjhbg7()==null || qualitySgcjjybg.getFwbjhbg7().equals("") &&
							qualitySgcjjybg.getEquipmentName7()==null || qualitySgcjjybg.getEquipmentName7().equals("")){
						}else{
							String c=codeTablesService.getValueByCode("TJY2_SBPZ",String.valueOf(qualitySgcjjybg.getEquipmentVariety7()));
							String d=codeTablesService.getValueByCode("TJY2_JYLB",String.valueOf(qualitySgcjjybg.getEquipmentCategory7()));
							String p=taskbookManager.getbh();//编号
			   				if(qualitySgcjjybg.getStatusa().equals("0")){
			   					taskbookManager.setTaskbook(p,"法定", qualitySgcjjybg.getFwbjhbg7(), 
			   							qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName7(), c+","+d,
			   							qualitySgcjjybg.getNjts7(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
			   							qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber7(),
			   	   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   	   					qualitySgcjjybg.getRwdId7(),qualitySgcjjybg.getRwdSn7());
			   				}else{
			   					taskbookManager.setTaskbook(p,"委托", qualitySgcjjybg.getFwbjhbg7(), 
									qualitySgcjjybg.getUserName(),qualitySgcjjybg.getEquipmentName7(), "",
									qualitySgcjjybg.getNjts7(),qualitySgcjjybg.getBjwtsj(), qualitySgcjjybg.getDepartment(),
									qualitySgcjjybg.getDepartmentId(), qualitySgcjjybg.getContractNumber7(),
			   						qualitySgcjjybg.getRegistrant(),qualitySgcjjybg.getRegistrantId(),qualitySgcjjybg.getRegistrantDate(),qualitySgcjjybg.getId(),
			   						qualitySgcjjybg.getRwdId7(),qualitySgcjjybg.getRwdSn7());	
			   				}
						}
					}else{
						QualitySgcjjybg qualitySgcjjybg=qualitySgcjjybgManager.get(id);
						qualitySgcjjybg.setStatus(QualitySgcjjybgManager.ZL_SGCJ_NO_PASS);
						qualitySgcjjybgManager.save(qualitySgcjjybg);
					}
				} else {
					return JsonWrapper.failureWrapper("流程ID为空！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapper(e.getMessage());
		}
		return JsonWrapper.successWrapper(id);
	}
	
	/**
	 * 加载个人信息，此处添加了@OAuthRequired注解
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "weixinUserInfo")
	@OAuthRequired
	public String weixinUserInfo(HttpServletRequest request,Model model){
		System.out.println("Load a User!");
        System.out.println("getRequestURL = " + request.getSession().getId());
        System.out.println("getRequestURL = " + request.getRequestURL());
        System.out.println("getRequestURI = " + request.getRequestURI());
        System.out.println("code = " + request.getParameter("code"));
        System.out.println("businessId = " + request.getParameter("businessId"));
        HttpSession session = request.getSession();
        log.debug(this.getClass().getName() + " method weixinUserInfo Load a User start");
        if(session.getAttribute("Userid")==null || ((String)session.getAttribute("Userid")).equals("noUserId")) {
            //AccessToken accessToken = WxUtil.getAccessToken(Constants.CORPID, Constants.SECRET);
            String token = WxUtil.getAccessTokenString();
            log.debug(this.getClass().getName() + " method weixinUserInfo 获取到code："+request.getParameter("code"));
        	log.debug(this.getClass().getName() + " method weixinUserInfo 获取到token："+token);
            if (StringUtil.isNotEmpty(token) && request.getParameter("code")!=null) {
                Result<String> result = WxUtil.oAuth2GetUserByCode(token, request.getParameter("code"), 7);
                String menuUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="+token+"&userid="+result.getObj();
                log.debug(this.getClass().getName() + " method weixinUserInfo menuUrl："+menuUrl);
                JSONObject jo = HttpRequestUtil.httpRequest(menuUrl, EnumMethod.GET.name(), null);
                if(jo!=null){
                	if(jo.has("mobile")) {
						User user;
						try {
							user = weixinAppInfoManager.getUser(jo.getString("mobile"));
							if(user!=null) {
								session.setAttribute("Name", user.getName());
								session.setAttribute("Phone", jo.getString("mobile"));
								session.setAttribute("Account",user.getAccount());
								log.debug(this.getClass().getName() + " method weixinUserInfo 获取到用户信息，用户姓名："+user.getName()+"，用户手机号：" + jo.getString("mobile")+"，用户Account:"+user.getAccount());
							}else {
								session.setAttribute("error_msg", "亲，检验平台中未找到与该手机号匹配的用户信息");
								log.debug("亲，检验平台中未找到与该手机号匹配的用户信息");
								return "app/weixininfo/app_info/weixin_error_page";
							}
						} catch (Exception e) {
							log.debug(e.getMessage());
							return "app/weixininfo/app_info/weixin_error_page";
						}
					}else {
						session.setAttribute("error_msg", "亲，微信接口服务出现异常，未找到手机号");
						log.debug("亲，微信接口服务出现异常，未找到手机号");
						return "app/weixininfo/app_info/weixin_error_page";
					}
                }else {
					session.setAttribute("error_msg", "亲，微信接口服务出现异常，未获取到微信企业用户信息");
					log.debug("亲，微信接口服务出现异常，未获取到微信企业用户信息");
					return "app/weixininfo/app_info/weixin_error_page";
				}
                session.setAttribute("Userid", result.getObj());
                //session.setAttribute("AccessToken", accessToken.getToken());
                
            } else {
            	session.setAttribute("Userid", "noUserId");
            	session.setAttribute("Name", "noUserName");
            	session.setAttribute("Phone", "noUserPhone");
            }
        }
        session.setAttribute("businessId", request.getParameter("businessId"));
        session.setAttribute("pageStatus", request.getParameter("pageStatus"));
		model.addAttribute("Userid", session.getAttribute("Userid"));
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!"+session.getAttribute("Userid"));
		return "app/qualitymanage/manualReport/transfer_page";
	}
	protected List<BpmUser> getBpmUserPaticipator(Activity activity) {
		List<BpmUser> bpmUsers = new ArrayList();
		if (activity.getParticipators() != null && !activity.getParticipators().isEmpty()) {
			Iterator var4 = activity.getParticipators().iterator();

			while (var4.hasNext()) {
				Participator ptor = (Participator) var4.next();
				BpmUser bpmUser = null;
				if (ptor.getParticipantType().equals("person")) {
					bpmUser = this.bpmUserOrgManager.getBpmUser(ptor.getParticipantId());
				} else if (ptor.getParticipantType().equals("role")) {
					bpmUsers.addAll(getRoleBpmUser(ptor.getParticipantId(),ptor.getParticipantRange()));
				} else if (ptor.getParticipantType().equals("start")) {
					Activity startActivity = this.activityDao.getStartActivity(activity.getProcess().getId());
					bpmUsers.addAll(this.getBpmUserPaticipator(startActivity));
				}

				if (bpmUser != null) {
					bpmUsers.add(bpmUser);
				}
			}

			return bpmUsers;
		} else {
			return bpmUsers;
		}
	}
	private List<BpmUser> getRoleBpmUser(String roleId, String range) {
		Role role = (Role) this.roleDao.get(roleId);
		List<BpmUser> list = new ArrayList<BpmUser>();
		if (role.getUsers() != null && !role.getUsers().isEmpty()) {
			Iterator i$ = role.getUsers().iterator();
			User u;
			while(i$.hasNext()){
				u = (User) i$.next();
				if(StringUtil.isEmpty(range)){
					BpmUser bu = getBpmUser(u);
					list.add(bu);
				}else if(u.getOrg().getId().equals(range)){
					BpmUser bu = getBpmUser(u);
					list.add(bu);
				}
			}
		}
		return list;

	}
	private BpmUser getBpmUser(User user) {
		Org unit = getUnit(user.getOrg());
		BpmUser bpmUser = new BpmUserImpl(user.getId(), user.getName(), new BpmOrgImpl(unit),
				new BpmOrgImpl(user.getOrg()), userDao.findUserRoles(user.getId()));
		return bpmUser;
	}
	private Org getUnit(Org org) {
		if (org != null && !"00".equals(org.getId())) {
			return org.getProperty() != null & "unit".equals(org.getProperty()) ? org : this.getUnit(org.getParent());
		} else {
			return org;
		}
	}
	/**
	 * 发送消息给申请人
	 * @param qualitySgcjjybg
	 * @param request
	 * @param user 
	 * @param list 
	 * @throws Exception
	 */
	 
	private void sendToApply(QualitySgcjjybg qualitySgcjjybg,HttpServletRequest request, Activity currentActivity,
			List<Activity> nextActivity, CurrentSessionUser user,String code) throws Exception{
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("checkType","0".equals(qualitySgcjjybg.getStatusa())?"法定检验报告":"委托检验报告");
		params.put("applyTime",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(qualitySgcjjybg.getApplyTime()));
		if(currentActivity!=null){
			params.put("currentActivity", currentActivity.getName());
		}else{
			params.put("currentActivity", "启动");
		}
		if(nextActivity!=null&&nextActivity.size()>0){
			params.put("nextActivity",nextActivity.get(0).getName());
		}
		params.put("auditor",user.getName());
		HashMap<String, Object> params2 = new HashMap<String, Object>();
		params2.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb0f376eb09e64dd3&redirect_uri=http://jx.scsei.org.cn/quality/sgcjjybg/weixinUserInfo.do?businessId="
				+ qualitySgcjjybg.getId() +"%26pageStatus=detail&response_type=code&scope=snsapi_base&state=STATE");
		messageService.sendMassageByConfig(
				request, 
				qualitySgcjjybg.getId(),
				null,
				null,
				code,
				qualitySgcjjybg.getRegistrantId(),
				params,
				params2,
				"2",
				Constant.INSPECTION_CORPID,
				Constant.INSPECTION_PWD);
	}
	/**
	 * 发送消息给审核人
	 * @param qualitySgcjjybg
	 * @param request
	 * @param bpmUser
	 * @param tel
	 * @throws Exception
	 */
	private void sendToAudit(QualitySgcjjybg qualitySgcjjybg,HttpServletRequest request,BpmUser bpmUser,String tel) throws Exception{
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("checkType","0".equals(qualitySgcjjybg.getStatusa())?"法定检验报告":"委托检验报告");
		params.put("applyName",qualitySgcjjybg.getApplyName());
		params.put("department",qualitySgcjjybg.getDepartment());
		params.put("applyTime",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(qualitySgcjjybg.getApplyTime()));
		HashMap<String, Object> params2 = new HashMap<String, Object>();
		params2.put("url","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb0f376eb09e64dd3&redirect_uri=http://jx.scsei.org.cn/quality/sgcjjybg/weixinUserInfo.do?businessId="
				+ qualitySgcjjybg.getId() +"%26pageStatus=check&response_type=code&scope=snsapi_base&state=STATE");
		messageService.sendMassageByConfig(
				(HttpServletRequest) request, 
				qualitySgcjjybg.getId(),
				tel,
				null,
				"manualReport",
				bpmUser.getId(),
				params,
				params2,
				"2",
				Constant.INSPECTION_CORPID,
				Constant.INSPECTION_PWD);
	}
}