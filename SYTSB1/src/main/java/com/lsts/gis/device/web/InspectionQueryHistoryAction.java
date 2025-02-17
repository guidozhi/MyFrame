package com.lsts.gis.device.web;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khnt.core.crud.web.SpringSupportAction;
import com.lsts.gis.device.bean.InspectionQueryHistory;
import com.lsts.gis.device.service.InspectionQueryHistoryService;
@Controller
@RequestMapping("inspectionQueryHistory")
public class InspectionQueryHistoryAction extends 
SpringSupportAction<InspectionQueryHistory, InspectionQueryHistoryService>{

	@Autowired
	InspectionQueryHistoryService inspectionQueryHistoryService;

	@RequestMapping(value="getHistories")
	@ResponseBody
	public HashMap<String, Object> getHistories(int size) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			List<InspectionQueryHistory> hiss = inspectionQueryHistoryService.getHistories(size);
			map.put("success", true);
			map.put("data", hiss);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	@RequestMapping(value="static/getHistories")
	@ResponseBody
	public HashMap<String, Object> staticGetHistories(int size) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			List<InspectionQueryHistory> hiss = inspectionQueryHistoryService.getHistories(size);
			map.put("success", true);
			map.put("data", hiss);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	//热门搜索
	@RequestMapping(value="hotSearch")
	@ResponseBody
	public HashMap<String, Object> hotSearch(Integer numb) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			List<Object[]> hiss = inspectionQueryHistoryService.hotSearch(numb);
			map.put("success", true);
			map.put("data", hiss);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
}
