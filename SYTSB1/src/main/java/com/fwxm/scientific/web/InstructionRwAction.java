package com.fwxm.scientific.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import util.FileUtil;

import com.alibaba.fastjson.JSON;
import com.fwxm.scientific.bean.InstructionRw;
import com.fwxm.scientific.bean.Tjy2ScientificRemark;
import com.fwxm.scientific.service.InstructionRwManager;
import com.fwxm.scientific.service.Tjy2ScientificRemarkManager;
import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.pub.fileupload.bean.Attachment;
import com.khnt.pub.fileupload.service.AttachmentManager;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rbac.impl.dao.UserDao;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.StringUtil;
import com.lsts.advicenote.service.MessageService;
import com.lsts.log.service.SysLogService;
import com.lsts.constant.Constant;

/**
 * <p>
 * 类说明
 * </p>
 * 
 * @ClassName InstructionRw
 * @JDK 1.7
 * @author CODER_V3.0
 * @date 2018-01-11 15:37:16
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping("com/tjy2/instructionRw")
public class InstructionRwAction extends
		SpringSupportAction<InstructionRw, InstructionRwManager> {

	@Autowired
	private InstructionRwManager instructionRwManager;
	@Autowired
	private UserDao userDao;
    @Autowired
	private MessageService messageService;
	@Autowired
	private AttachmentManager attachmentManager;
	 @Autowired
	    private Tjy2ScientificRemarkManager tjy2ScientificRemarkManager;
	@Autowired
	private SysLogService logService;
	/** 提交项目负责人**/
	@RequestMapping(value = "subFZR")
	@ResponseBody
	public HashMap<String, Object> subFZR(HttpServletRequest request,String id)
			throws Exception {
		HashMap<String, Object> map=new HashMap<String, Object>();
		CurrentSessionUser user=SecurityUtil.getSecurityUser();
		try {
			InstructionRw instructionRw =instructionRwManager.get(id);
			if(instructionRw.getStatus().equals("4")){//提交给项目负责人
				instructionRw.setStatus("5");
				
				logService.setLogs(instructionRw.getId(), "提交项目负责人", "提交项目负责人填写", user.getId(), user.getName(),
						new Date(), request.getRemoteAddr());
				//发送微信
				/**/ User user1= userDao.get(instructionRw.getProjectHeadId());
				 String con="您好,编号为"+instructionRw.getRwNumber()+"的作业指导书任务书已发至你的系统，请注意填写时限!";
		    		messageService.sendWxMsg(null,"任务指导书", Constant.INSPECTION_CORPID, Constant.INSPECTION_PWD, 
		    				con,user1.getEmployee().getMobileTel());
		        //发送短信
		    	/**/	messageService.sendMoMsg(request, instructionRw.getId(), 
		    				"您好,编号为"+instructionRw.getRwNumber()+"的作业指导书任务书已发至你的系统，请注意填写时限!",user1.getEmployee().getMobileTel());
		    	
			}else if(instructionRw.getStatus().equals("1")){//提交给科管部
				instructionRw.setStatus("2");
				logService.setLogs(instructionRw.getId(), "提交给科管部", "提交给科管部", user.getId(), user.getName(),
						new Date(), request.getRemoteAddr());
			}
			instructionRwManager.save(instructionRw);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			// TODO: handle exception
		}
		
		return map;

	}
	// 查询流程步骤信息
	@RequestMapping(value = "getFlowStep")
	@ResponseBody
	public ModelAndView getFlowStep(HttpServletRequest request)
				throws Exception {

			Map<String, Object> map = new HashMap<String, Object>();

			map = instructionRwManager.getFlowStep(request.getParameter("ins_info_id"));

			ModelAndView mav = new ModelAndView("app/fwxm/scientific/instruction/flow_card", map);

			return mav;

		}
	

	@SuppressWarnings("unused")
	@RequestMapping(value = "downloadFile")
	public void downloadFile(HttpServletRequest request,
			HttpServletResponse response, String id){

   		try {
//			List<Attachment> list = attachmentManager.getBusAttachment(id);
   			Attachment attachment=attachmentManager.get(id);
			FileUtil.download(response, attachment.getFileBody(),attachment.getFileName(), attachment.getFileType());
		} catch (Exception e) {
			e.printStackTrace();
		}
   		
	}
	/** 审核**/
	@RequestMapping(value = "subAudit1")
	@ResponseBody
	public HashMap<String, Object> subAudit1(HttpServletRequest request,String id,String opinion,String remark)
			throws Exception {
		HashMap<String, Object> map=new HashMap<String, Object>();
		CurrentSessionUser user=SecurityUtil.getSecurityUser();
		try {
			String ids[]=id.split(",");
			for (int i = 0; i < ids.length; i++) {
			InstructionRw instruction =instructionRwManager.get(ids[i]);
			int status =Integer.parseInt(instruction.getStatus());//获取申请书当前步骤
			if(opinion==null&&instruction.getStatus().equals("0")){
				instruction.setStatus("1");//提交科技委
				logService.setLogs(instruction.getId(), "任务书到科技委", "从任务书提交到科技委", user.getId(), user.getName(),
							new Date(), request.getRemoteAddr());
			}else if(opinion.equals("0")){
			 if(instruction.getStatus().equals("1")){
					instruction.setStatus("2");//科技委审核完成
					instruction.setKj_opinion(remark);
					instruction.setKj_date(new Date());
					logService.setLogs(instruction.getId(), "科技委到常务副主任", "从科技委提交到常务副主任", user.getId(), user.getName(),
							new Date(), request.getRemoteAddr());
					
				}else if(instruction.getStatus().equals("2")){
				instruction.setStatus("3");//提交副主任审核
				instruction.setFzr_opinion(remark);
				instruction.setFzr_date(new Date());
				logService.setLogs(instruction.getId(), "常务副主任到主任签字", "从常务副主任到主任签字", user.getId(), user.getName(),
						new Date(), request.getRemoteAddr());
			}else if(instruction.getStatus().equals("3")){
				instruction.setStatus("4");//提交主任审核
				instruction.setSign_opinion(remark);
				instruction.setSign_date(new Date());
				logService.setLogs(instruction.getId(), "主任签字审核", "主任签字审核", user.getId(), user.getName(),
						new Date(), request.getRemoteAddr());
			}
			 
			}else if(opinion.equals("1")){
				instruction.setStatus(Integer.toString(status-1));//退回上一步
				logService.setLogs(instruction.getId(), "退回上一步", "退回上一步", user.getId(), user.getName(),
						new Date(), request.getRemoteAddr());
				
			}
			instructionRwManager.save(instruction);
			}
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			// TODO: handle exception
		}
		
		return map;

	}
	

	// 批量无附件保存
	@RequestMapping(value = "saveBasicTx")
	@ResponseBody
	public HashMap<String, Object> saveBasicTx(HttpServletRequest request) throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try {
			String instruction=request.getParameter("instruction").toString();
			InstructionRw bean=JSON.parseObject(instruction,InstructionRw.class);
			CurrentSessionUser user=SecurityUtil.getSecurityUser();
			HashMap<String, Object>  map=instructionRwManager.saveBasicTx(bean, request,user.getId(),user.getName());
			return map;
		} catch (Exception e) {
			log.debug(e.toString());
			wrapper.put("success", false);
			wrapper.put("msg", "保存失败！");
			e.printStackTrace();
		}
		return wrapper;
	}
	
	
	
	/** 提交审核**/
	@RequestMapping(value = "subAudit")
	@ResponseBody
	public HashMap<String, Object> subAudit(HttpServletRequest request,String id,String opinion,String backs,String remark)
			throws Exception {
		HashMap<String, Object> map=new HashMap<String, Object>();
		CurrentSessionUser user=SecurityUtil.getSecurityUser();
		try {
			String ids[]=id.split(",");
			for (int i = 0; i < ids.length; i++) {
			InstructionRw instruction =instructionRwManager.get(ids[i]);
			Tjy2ScientificRemark entity=new Tjy2ScientificRemark();
			int status =Integer.parseInt(instruction.getStatus());//获取申请书当前步骤
			if(opinion==null&&instruction.getStatus().equals("5")){
				instruction.setStatus("6");//提交科技委
				logService.setLogs(instruction.getId(), "提交审查", "提交到审查", user.getId(), user.getName(),
						new Date(), request.getRemoteAddr());
			}else if(opinion.equals("0")){
				entity.setProcess("审核通过");
			 if(instruction.getStatus().equals("6")){
					instruction.setStatus("7");//审查完成
					instruction.setReview_opinion(remark);
					instruction.setReview_date(new Date());
					logService.setLogs(instruction.getId(), "提交审核", "从任务书确认到审查", user.getId(), user.getName(),
							new Date(), request.getRemoteAddr());
					//instruction.setReviewId(user.getId());
					//instruction.setReviewMan(user.getName());
				}else if(instruction.getStatus().equals("7")){
				instruction.setStatus("8");//提交审核
				instruction.setAudit_opinion(remark);
				instruction.setAudit_date(new Date());
				logService.setLogs(instruction.getId(), "提交审核", "从审查到审核", user.getId(), user.getName(),
						new Date(), request.getRemoteAddr());
				//instruction.setAuditId(user.getId());
				//instruction.setAuditMan(user.getName());
			}else if(instruction.getStatus().equals("8")){
				if(instruction.getProjectAcceptanceDate()!=null){
				instruction.setStatus("9");//批准完成
				instruction.setSign_opinion(remark);
				instruction.setSign_date(new Date());
				logService.setLogs(instruction.getId(), "提交审核", "从审核到批准", user.getId(), user.getName(),
						new Date(), request.getRemoteAddr());
				//instruction.setSign_id(user.getId());
				//instruction.setSign_man(user.getName());
				//将文件信息修改反写会质量体系文件
				instructionRwManager.setModifyQualityFile(instruction);
				}else{
					map.put("success", false);
					map.put("msg", "对不起，验收时间未填写，不能批准!");
					return map;
				}
			}
			}else if(opinion.equals("1")){
				entity.setProcess("审核不通过");
				if(backs.equals("1")){
					instruction.setStatus("1");//退回录入

					}else{
						instruction.setStatus(Integer.toString(status-1));//退回上一步
					}
				logService.setLogs(instruction.getId(), "审核不通过", "审核不通过", user.getId(), user.getName(),new Date(), request.getRemoteAddr());
			}
			    entity.setCreate_date(new Date());
				entity.setCreate_man(user.getName());
				entity.setRemark(remark);
				entity.setProject_name(instruction.getProjectName());
				entity.setFk_scientific_id(instruction.getId());
				tjy2ScientificRemarkManager.save(entity);
				 String uploadFiles = request.getParameter("uploadFiles");
				 if(StringUtil.isNotEmpty(uploadFiles)){
			 			String[] files = uploadFiles.replaceAll("/^,/", "").split(",");
			 			for(String file : files){
			 				if (StringUtil.isNotEmpty(file)) {
			 					attachmentManager.setAttachmentBusinessId(file, entity.getId());	// 2、保存附件
			 				}
			 			}
			 		}
			instructionRwManager.save(instruction);
			}
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			// TODO: handle exception
		}
		
		return map;

	}
	// 批量无附件保存
	@RequestMapping(value = "saveBasic")
	@ResponseBody
	public HashMap<String, Object> saveBasic(HttpServletRequest request) throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try {

			String instruction=request.getParameter("instruction").toString();
			InstructionRw bean=JSON.parseObject(instruction,InstructionRw.class);

			CurrentSessionUser user=SecurityUtil.getSecurityUser();
			
			instructionRwManager.saveBasic(bean, request,user.getId(),user.getName());
			wrapper.put("success", true);
		} catch (Exception e) {
			log.debug(e.toString());
			wrapper.put("success", false);
			wrapper.put("msg", "保存失败！");
			e.printStackTrace();
		}
		return wrapper;
	}
	/**
   	 * 详情
   	 * 
   	 * @param request
   	 * @param id
   	 * @return
   	 * @throws Exception
   	 */
   	@RequestMapping(value = "detailBasic")
   	@ResponseBody
   	public HashMap<String, Object> detailBasic(HttpServletRequest request, String id)
   			throws Exception {
   		InstructionRw maintenance = instructionRwManager.get(id);
   		List<Attachment> list = attachmentManager.getBusAttachment(id);
   		HashMap<String, Object> wrapper = new HashMap<String, Object>();
   		System.out.println("**************"+maintenance.getId());
   		wrapper.put("success", true);
   		wrapper.put("data", maintenance);
   		wrapper.put("attachs", list);
   		return wrapper;
   	}
   	/** 撤回**/
	@RequestMapping(value = "collback")
	@ResponseBody
	public HashMap<String, Object> collback(HttpServletRequest request,String id)
			throws Exception {
		HashMap<String, Object> map=new HashMap<String, Object>();
		CurrentSessionUser user=SecurityUtil.getSecurityUser();
		try {
			String ids[]=id.split(",");
			for (int i = 0; i < ids.length; i++) {
			InstructionRw instruction =instructionRwManager.get(ids[i]);
			int status =Integer.parseInt(instruction.getStatus());//获取申请书当前步骤
				instruction.setStatus(Integer.toString(status-1));//退回上一步
			
			instructionRwManager.save(instruction);

			logService.setLogs(instruction.getId(), "任务撤回", "任务撤回", user.getId(), user.getName(),
					new Date(), request.getRemoteAddr());
			}
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			// TODO: handle exception
		}
		
		return map;

	}
}
