package com.lsts.mobileapp.input.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khnt.core.crud.web.SpringSupportAction;
import com.lsts.common.service.InspectionCommonService;
import com.lsts.device.service.DeviceService;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.bean.ReportItemRecord;
import com.lsts.inspection.service.InspectionInfoService;
import com.lsts.inspection.service.ReportItemRecordService;
import com.lsts.inspection.service.ReportItemValueService;
import com.lsts.mobileapp.input.service.RecordService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("recordAction")
public class RecordAction extends SpringSupportAction<ReportItemRecord, RecordService> {
	@Autowired
	RecordService recordService;
	@Autowired
	InspectionInfoService infoService;
	@Autowired
	DeviceService deviceService;
	@Autowired
	InspectionCommonService inspectionCommonService;
	@Autowired
	ReportItemValueService reportItemValueService;
	@Autowired
	ReportItemRecordService reportItemRecordService;

	@RequestMapping("saveRecord")
	@ResponseBody
	public HashMap<String, Object> saveRecord(HttpServletRequest request) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String data = request.getParameter("data");
			JSONObject object = JSONObject.fromString(data);
			if (object == null) {
				map.put("success", false);
				map.put("msg", "请传入参数！");
			} else {
				InspectionInfo info = recordService.saveRecord(request, object);
				
				map.put("data", info);
				map.put("success", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}

	/**
	 * 提交到校核
	 * 
	 * @param request
	 * @param formObject
	 * @param businessArray
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "flowToConfirm")
	@ResponseBody
	public Map<String, Object> flowToConfirm(HttpServletRequest request, String formObject, String businessArray)
			throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String data = request.getParameter("data");
		
			JSONObject object = JSONObject.fromString(data);
			InspectionInfo info = recordService.saveRecord(request, object);
			
			
			object.put("id",info.getId());
			
			JSONObject formData = new JSONObject();//JSONObject.fromString(formObject); 
			formData.put("confirmOp", object.getString("nextOpId"));
			formData.put("confirmOpName", object.getString("nextOp"));
			JSONArray businesses = new JSONArray();//JSONArray.fromString(businessArray);
			businesses.put(object);
		
			List<InspectionInfo> list = recordService.flowToConfirm(request, businesses, formData);
			
			map.put("data", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("data", e.getMessage());
			map.put("success", false);
		}
		
		return map;
	}

	/**
	 * 转交他人录入
	 * author pingZhou
	 * @param request
	 * @return
	 */
	@RequestMapping("saveInspToOther")
	@ResponseBody
	private HashMap<String, Object> saveInspToOther(HttpServletRequest request) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String data = request.getParameter("data");
			
			JSONObject object = JSONObject.fromString(data);
			
			InspectionInfo info =  this.recordService.saveInspToOther(request,object);
			map.put("success", true);
			map.put("data", info);
		} catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
			map.put("success", true);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	/**
	  *  查询待处理任务条数
	 * author pingZhou
	 * @param request
	 * @return
	 */
	@RequestMapping("allDealCount")
	@ResponseBody
	public HashMap<String, Object> allDealCount(HttpServletRequest request){
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			List<HashMap<String, Object>> countMap =  this.recordService.toBeProcessed(request);
			map.put("data", countMap);
			map.put("success", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.getMessage());
			map.put("success", false);
			map.put("msg", "退回失败！");
			
		}
		return map;
	}
	/**
	 * 查询原始记录数据
	 */
	@RequestMapping("backReceive")
	@ResponseBody
	private HashMap<String, Object> backReceive(HttpServletRequest request,String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String,Object> data = recordService.backReceive(request,id);
			map.put("success", true);
			map.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("data", e.getMessage());
		}
		return map;
	}
}
