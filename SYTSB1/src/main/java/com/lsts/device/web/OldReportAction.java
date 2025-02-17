package com.lsts.device.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.pub.fileupload.bean.Attachment;
import com.khnt.pub.fileupload.service.AttachmentManager;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.lsts.device.bean.OldReportApply;
import com.lsts.device.service.OldReportService;
import com.lsts.log.service.SysLogService;


@Controller
@RequestMapping("oldReportAction")
public class OldReportAction extends SpringSupportAction<OldReportApply, OldReportService>{
	
	@Autowired
	private OldReportService oldReportService;
	@Autowired
	private AttachmentManager attachmentManager;
	@Autowired
	private SysLogService logService;

	@ResponseBody
	@RequestMapping("savebasic")
	public HashMap<String, Object> savebasic(HttpServletRequest request, OldReportApply entity) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String uploadFiles = request.getParameter("uploadFiles");
		try {
			
			oldReportService.saveEquipment(entity, uploadFiles);
			//记录日志
		    String status = request.getParameter("status");
		    if(status== "add" ){
			logService.setLogs(entity.getId(), "新增旧版本报告模板使用申请", "新增旧版本报告模板使用申请",
					user.getId(), user.getName(), new Date(), request
							.getRemoteAddr());
			}else{
				logService.setLogs(entity.getId(), "修改旧版本报告模板使用申请", "修改旧版本报告模板使用申请",
						user.getId(), user.getName(), new Date(), request
								.getRemoteAddr());
		    }
			map.put("success", true);
			map.put("data", entity);
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "保存失败！请重试！");
			//return JsonWrapper.failureWrapperMsg("保存失败！请重试！");
		}
		return map;
	}
	
	@RequestMapping(value = "dle")
	@ResponseBody
	public HashMap<String, Object> detail(HttpServletRequest request, String ids) throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try {
			OldReportApply oldReportApply = oldReportService.get(ids);
			
			if(oldReportApply.getUploadFiles()!=null&&com.khnt.utils.StringUtil.isNotEmpty(oldReportApply.getUploadFiles())){
				List<Attachment> list1 =  new ArrayList<Attachment>();
				String []ids1 = oldReportApply.getUploadFiles().split(",");
				for (int i = 0; i < ids1.length; i++) {
					Attachment attachment = attachmentManager.get(ids1[i]);
					list1.add(attachment);
				}
				wrapper.put("attachs", list1);
			}
			if(oldReportApply.getUploadotherFiles()!=null&&com.khnt.utils.StringUtil.isNotEmpty(oldReportApply.getUploadotherFiles())){
				List<Attachment> list2 =  new ArrayList<Attachment>();
				String []ids2 = oldReportApply.getUploadotherFiles().split(",");
				for (int i = 0; i < ids2.length; i++) {
					Attachment attachment2 = attachmentManager.get(ids2[i]);
					list2.add(attachment2);
				}
				wrapper.put("attachs2", list2);
			}
			if(oldReportApply.getUploadanotherFiles()!=null&&com.khnt.utils.StringUtil.isNotEmpty(oldReportApply.getUploadanotherFiles())){
				List<Attachment> list3 =  new ArrayList<Attachment>();
				String []ids3 = oldReportApply.getUploadanotherFiles().split(",");
				for (int i = 0; i < ids3.length; i++) {
					Attachment attachment3 = attachmentManager.get(ids3[i]);
					list3.add(attachment3);
				}
				wrapper.put("attachs3", list3);
			}
			
			wrapper.put("success", true);
			wrapper.put("data", oldReportApply);

		} catch (Exception e) {
			e.printStackTrace();
			wrapper.put("success", false);
			wrapper.put("msg", "查询失败！");
		}
		
		return wrapper;
	
	}
	
	@ResponseBody
	@RequestMapping("del")
	public HashMap<String, Object> del(HttpServletRequest request,String ids) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			oldReportService.del(request, ids);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "删除失败！");
		}
		return map;
	}
	
	@RequestMapping(value = "subs")
   	@ResponseBody
   	public HashMap<String, Object> subs(HttpServletRequest request,String ids, String step) {
   		HashMap<String, Object> map = new HashMap<String, Object>();
   		try {
   			oldReportService.subs(request, ids, step);
   			map.put("success", true);
   		} catch (Exception e) {
   			e.printStackTrace();
   			map.put("success", false);
   			map.put("msg", "提交失败！");
   		}
   		return map;
   	}
}
