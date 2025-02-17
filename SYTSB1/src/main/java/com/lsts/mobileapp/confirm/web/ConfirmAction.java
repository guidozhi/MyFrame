package com.lsts.mobileapp.confirm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khnt.core.crud.web.SpringSupportAction;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.mobileapp.confirm.service.ConfirmService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Controller
@RequestMapping("confirmAction")
public class ConfirmAction extends SpringSupportAction<InspectionInfo, ConfirmService>{

	@Autowired
	ConfirmService confirmService;
	
	// 保存报告审核
	@RequestMapping(value = "confirm")
	@ResponseBody
	public Map<String, Object> confirm(HttpServletRequest request,String formObject,String businessArray) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject formData = JSONObject.fromString(formObject);
			JSONArray businesses = JSONArray.fromString(businessArray);
				
			List<InspectionInfo> list = confirmService.confirm(request,businesses,formData);
			
			map.put("data",list);
			map.put("success",true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg",e.getMessage());
			map.put("success",false);
		}
		return map;
	}
	@RequestMapping(value = "failed")
	@ResponseBody
	public Map<String, Object> failed(HttpServletRequest request,String formObject,String businessArray) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject formData = JSONObject.fromString(formObject);
			JSONArray businesses = JSONArray.fromString(businessArray);
			
			List<InspectionInfo> list = confirmService.failed(request, businesses,formData);
			
			map.put("data",list);
			map.put("success",true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg",e.getMessage());
			map.put("success",true);
		}
		return map;
	}
}
