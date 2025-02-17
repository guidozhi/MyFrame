package com.fwxm.scientific.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.pub.expimp.types.BeanUtil;
import com.khnt.pub.fileupload.bean.Attachment;
import com.khnt.pub.fileupload.service.AttachmentManager;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rbac.impl.dao.UserDao;
import com.khnt.utils.BeanUtils;
import com.khnt.utils.StringUtil;
import com.lsts.advicenote.service.MessageService;
import com.lsts.log.bean.SysLog;
import com.lsts.log.service.SysLogService;
import com.lsts.qualitymanage.bean.QualityManagerFiles;
import com.lsts.qualitymanage.dao.QualityManagerFilesDao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fwxm.scientific.bean.InstructionInfo;
import com.fwxm.scientific.bean.InstructionProject;
import com.fwxm.scientific.bean.InstructionRw;
import com.fwxm.scientific.bean.Tjy2ScientificRemark;
import com.fwxm.scientific.dao.InstructionInfoDao;
import com.fwxm.scientific.dao.InstructionProjectDao;
import com.fwxm.scientific.dao.InstructionRwDao;

/*******************************************************************************
 * <p>
 * 
 * </p>
 * 
 * @ClassName InstructionRwManager
 * @JDK 1.5
 * @author CODER_V3.0
 * @date 2018-01-11 15:37:16
 */
@Service("instructionRwManager")
public class InstructionRwManager extends EntityManageImpl<InstructionRw, InstructionRwDao> {
	@Autowired
	InstructionRwDao instructionRwDao;
	@Autowired
	InstructionInfoDao instructionInfoDao;
	@Autowired
	InstructionProjectDao instructionProjectDao;
	@Autowired
	QualityManagerFilesDao qualityManagerFilesDao;
    @Autowired
	private AttachmentManager attachmentManager;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SysLogService logService;
	@Autowired
	private Tjy2ScientificRemarkManager tjy2ScientificRemarkManager;
    @Autowired
	private MessageService messageService;
	//定时任务，即将超期数据发送短信
	public void getOverdue(){
		String hql = "from InstructionRw c where c.status =5";
		List<InstructionRw> list = instructionRwDao.createQuery(hql).list();
		for (InstructionRw instructionRw : list) {

			Date endTime=instructionRw.getProjectEndDate();
			Date startTime=instructionRw.getProjectStartDate();
			long newDate = new Date().getTime();
			long endDate = endTime.getTime();
			long startDate=startTime.getTime();
			int day1 = (int) ((endDate - newDate)/(1000 * 60 * 60 * 24));
			int day2 = (int) ((endDate - startDate)/(1000 * 60 * 60 * 24));
		   if(day1<7 && day2>7){
			   User user1= userDao.get(instructionRw.getProjectHeadId());
			   //发送短信
			   String content="您好，编号为"+instructionRw.getRwNumber()+"的作业指导书还有"+day1+"天超期，请尽快填写！";
			   messageService.sendMoMsg(null, instructionRw.getId(), content,user1.getEmployee().getMobileTel());
		   }
		    
			
			
		}
	}
	
	
	
	 public HashMap<String, Object> saveBasicTx(InstructionRw instruction,HttpServletRequest request,String userId,String userName) throws Exception{
		 HashMap<String, Object> map=new HashMap<String, Object>();
		 
		 String bean=request.getParameter("instruction").toString();
		 JSONObject json= JSON.parseObject(bean);
		 String opinion="",remark="",uploadFiles="";//back=""，uploadFiles1="",
		 
		 if(json.containsKey("opinion")){opinion=json.getString("opinion");}
		 if(json.containsKey("remark")){remark=json.getString("remark");}
//		 if(json.containsKey("uploadFiles1")){uploadFiles1 = json.getString("uploadFiles1");}
		 if(json.containsKey("uploadFiles")){uploadFiles = json.getString("uploadFiles");}
//		 if(json.containsKey("back")){back = json.getString("back");}
		 

			if("2".equals(json.getString("tjType"))){//提交
			Tjy2ScientificRemark entity=new Tjy2ScientificRemark();
			instruction.setIsReturn("0");
			if(StringUtil.isEmpty(opinion)&&instruction.getStatus().equals("4")){
				instruction.setStatus("5");
				logService.setLogs(instruction.getId(), "作业指导书填写", "提交到标准审查", userId, userName,
						new Date(), request.getRemoteAddr());
			}else if(opinion.equals("0")){
				entity.setProcess("审核通过");
//				instruction.setAudit_opinion(remark);
			 if(instruction.getStatus().equals("5")){
					instruction.setStatus("6");//审查完成
//					instruction.setReview_opinion(remark);
					instruction.setReview_date(new Date());
					logService.setLogs(instruction.getId(), "标准审查到审核人员", remark, userId, userName,
							new Date(), request.getRemoteAddr());
				}else if(instruction.getStatus().equals("6")){
				instruction.setStatus("7");//提交到评审会
				instruction.setAudit_date(new Date());
				//发短息提示评审会

				   User user1= userDao.get("402883a04a055c63014a09c8e8080b00");//彭朝华
				   User user2= userDao.get("402883a0515e5d7601516101b68243a2");//林敏
				   //发送短信
				   String content="您好，编号为"+instruction.getRwNumber()+"的作业指导书审核通过，请组织人员开评审会！";
				   messageService.sendMoMsg(null, instruction.getId(), content,user1.getEmployee().getMobileTel());
				   messageService.sendMoMsg(null, instruction.getId(), content,user2.getEmployee().getMobileTel());
				   
				   
				
				logService.setLogs(instruction.getId(), "审核人员到评审会审核", remark, userId, userName,
						new Date(), request.getRemoteAddr());
			}else if(instruction.getStatus().equals("7")){
				instruction.setStatus("8");//提交到批准
//				instruction.setAudit_opinion(remark);
				instruction.setPsh_date(new Date());
				instruction.setPsh_opinion(remark);
				logService.setLogs(instruction.getId(), "审核人员到批准人员", remark, userId, userName,
						new Date(), request.getRemoteAddr());
			}else if(instruction.getStatus().equals("8")){
				if(instruction.getProjectAcceptanceDate()!=null){
				instruction.setStatus("9");//批准完成
//				instruction.setSign_opinion(remark);
				instruction.setSign_date(new Date());
				logService.setLogs(instruction.getId(), "", remark, userId, userName,
						new Date(), request.getRemoteAddr());
				//将文件信息修改反写会质量体系文件
				this.setModifyQualityFile(instruction);
				}else{
					map.put("success", false);
					map.put("msg", "对不起，验收时间未填写，不能批准!");
					return map;
				}
			}
			}else if(opinion.equals("1")){
				entity.setProcess("审核不通过");
				instruction.setIsReturn("1");
				instruction.setStatus("4");//退回填写
				logService.setLogs(instruction.getId(), "审核不通过", "审核不通过", userId, userName,new Date(), request.getRemoteAddr());
			}
			if(StringUtil.isNotEmpty(entity.getProcess())){
			    entity.setCreate_date(new Date());
				entity.setCreate_man(userName);
				entity.setRemark(remark);
				entity.setProject_name(instruction.getProjectName());
				entity.setFk_scientific_id(instruction.getId());
				tjy2ScientificRemarkManager.save(entity);
			}
//				if(StringUtil.isNotEmpty(uploadFiles1)){
//		 			String[] files = uploadFiles1.replaceAll("/^,/", "").split(",");
//		 			for(String file : files){
//		 				if (StringUtil.isNotEmpty(file)) {
//		 					attachmentManager.setAttachmentBusinessId(file, entity.getId());	// 2、保存附件
//		 				}
//		 			}
//		 		}
			}
				
		 
		 instructionRwDao.save(instruction);
		 if(StringUtil.isNotEmpty(uploadFiles)){
	 			String[] files = uploadFiles.replaceAll("/^,/", "").split(",");
	 			for(String file : files){
	 				if (StringUtil.isNotEmpty(file)) {
	 					attachmentManager.setAttachmentBusinessId(file, instruction.getId());	// 2、保存附件
	 				}
	 			}
	 		}

			map.put("success", true);
			return map;
	 }
	 public void saveBasic(InstructionRw entity,HttpServletRequest request,String userId,String userName) throws Exception{
		 String uploadFiles = request.getParameter("uploadFiles");
		 String instruction=request.getParameter("instruction").toString();
		 JSONObject json= JSON.parseObject(instruction);
		 String opinion="";
		 String remark="";
		 String tjlx="";
			if(json.containsKey("opinion")){
				opinion=json.getString("opinion");
			}
		if(json.containsKey("remark")){
			remark=json.getString("remark");
		}
		if(json.containsKey("tjlx")){
			tjlx=json.getString("tjlx");
		}
		 if(entity.getRwNumber()==null||entity.getRwNumber().equals("")){
			 String sql="";
			 String year="";
			 String sn="";
					year= new SimpleDateFormat("yyyy").format(new Date());
//					 User user= userDao.get(entity.getProjectHeadId());
//				 if(user.getOrg().getId().equals("100020")||user.getOrg().getId().equals("100021")||user.getOrg().getId().equals("100022")||user.getOrg().getId().equals("100023")||user.getOrg().getId().equals("100024")||user.getOrg().getId().equals("100063")){
//					 sn="ZDSJ-"+year;
//				 }else{
//					 sn="ZDSC-"+year;
//				 }
				if("1".equals(json.getString("number"))){
					sn="ZDSJ-"+year;
				}else{
					sn="ZDSC-"+year;
				}
				 sql= "select substr(t.RW_NUMBER,-2,2)"
							+ " from TJY2_INSTRUCTION_RW t "
							+ "where t.RW_NUMBER like '"+sn+"%'  order by substr(t.RW_NUMBER,-2,2) desc";
				 List<Object> list = instructionRwDao.createSQLQuery(sql).list();
					if(list.size()>0){
						String num = list.get(0).toString();
						String num2 = new Integer(num)+1+"";
						int l = num2.length();
						for (int i = 0; i < 2-l; i++) {
							num2 = "0"+num2;
						}
						sn = sn+"-"+num2;
					}else{
						sn = sn+"-"+"01";
					}
					entity.setRwNumber(sn);
		 }
		 

			if("2".equals(json.getString("tjType"))){//提交
				if(opinion.equals("0")|| "".equals(opinion)){//同意
					if(entity.getStatus().equals("0")){
						entity.setStatus("1");//提交科技委
						logService.setLogs(entity.getId(), "任务书到科技委", "从任务书提交到科技委", userId, userName,
									new Date(), request.getRemoteAddr());
					}else if(entity.getStatus().equals("1")){
						
						if(tjlx.equals("0")){//一般制订
							entity.setStatus("4");//提交主任审核
							entity.setSign_date(new Date());
							logService.setLogs(entity.getId(), "科技委审核", remark, userId, userName,
									new Date(), request.getRemoteAddr());
						}else{
							entity.setStatus("2");//提交副主任
							entity.setKj_date(new Date());
							logService.setLogs(entity.getId(), "科技委到常务副主任", remark, userId, userName,
									new Date(), request.getRemoteAddr());
							
						}
					}else if(entity.getStatus().equals("2")){
						entity.setStatus("3");//提交主任签字
						entity.setFzr_date(new Date());
						logService.setLogs(entity.getId(), "常务副主任到主任签字", remark, userId, userName,
								new Date(), request.getRemoteAddr());
					}else if(entity.getStatus().equals("3")){
						entity.setStatus("4");//提交主任审核
						entity.setSign_date(new Date());
						
						logService.setLogs(entity.getId(), "主任签字审核", remark, userId, userName,
								new Date(), request.getRemoteAddr());
					}
				}else{//不同意
					entity.setIsReturn("1");
					entity.setStatus("0");//退回上一步
					logService.setLogs(entity.getId(), "退回到任务书填写", remark, userId, userName,
							new Date(), request.getRemoteAddr());
				}
			}
		 
		 
		 
		 
		 instructionRwDao.save(entity);
		 if(StringUtil.isNotEmpty(uploadFiles)){
	 			String[] files = uploadFiles.replaceAll("/^,/", "").split(",");
	 			for(String file : files){
	 				if (StringUtil.isNotEmpty(file)) {
	 					attachmentManager.setAttachmentBusinessId(file, entity.getId());	// 2、保存附件
	 				}
	 			}
	 		}
		 
	 }

	 //将文件信息修改反写会质量体系文件
	public void setModifyQualityFile(InstructionRw instruction) throws Exception {
		String tructionInfoId = instruction.getTjy2InstructionInfoId();
		InstructionInfo info = instructionInfoDao.get(tructionInfoId);
		String pId=info.getProjectNameId();
		InstructionProject pro = instructionProjectDao.get(pId);
		String quality_file_id=pro.getQuality_file_id();
		if(StringUtil.isNotEmpty(quality_file_id)){
			QualityManagerFiles qf = qualityManagerFilesDao.get(quality_file_id);
			//将现有体系文件设置为旧体系文件
			List<Attachment> list = attachmentManager.getBusAttachment(quality_file_id, "new");
			for (int j = 0; j < list.size(); j++) {
				Attachment att = list.get(j);
				att.setWorkItem("old");
				attachmentManager.save(att);
			}
			List<Attachment> q_list=attachmentManager.getBusAttachment(instruction.getId());
			for (int j = 0; j < q_list.size(); j++) {
				Attachment att = q_list.get(j);
				Attachment file=new Attachment();
				BeanUtils.copyProperties(file, att);
				file.setId(null);
				file.setBusinessId(quality_file_id);
				file.setWorkItem("new");
				attachmentManager.save(file);
			}
			qualityManagerFilesDao.save(qf);
		}
	}
	public HashMap<String, Object>  getFlowStep(String ins_info_id) throws Exception
    {	
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		List<SysLog> list = instructionRwDao.createQuery("  from SysLog where business_id ='"+ins_info_id+"' order by op_time,id asc").list();
		list.size();
		map.put("flowStep", list);
		map.put("size", list.size());
		map.put("sn", instructionRwDao.get(ins_info_id).getRwNumber());
		map.put("success", true);
		
		return map;
    }

}
