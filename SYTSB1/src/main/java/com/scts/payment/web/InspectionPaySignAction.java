package com.scts.payment.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.lsts.report.bean.ReportDraw;
import com.scts.payment.bean.InspectionPaySign;
import com.scts.payment.service.InspectionPaySignService;
@Controller
@RequestMapping("inspectionPaySignAction")
public class InspectionPaySignAction extends SpringSupportAction<InspectionPaySign, InspectionPaySignService>{

	@Autowired
	InspectionPaySignService inspectionPaySignService;
	
	@RequestMapping(value="saveEntity")
	@ResponseBody
	public HashMap<String, Object> saveSign(HttpServletRequest request,@RequestBody InspectionPaySign inspectionPaySign) throws Exception{
		
		HashMap<String, Object> map = new HashMap<String,Object>();
		try {
			inspectionPaySignService.save(request,inspectionPaySign);
			map.put("success", true);
			map.put("data", inspectionPaySign);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		
		return map;
	}
	
	/**
	 * 结算表签名
	 * 
	 * @param request
	 * @param reportDraw
	 * @throws Exception
	 */
	@RequestMapping(value = "mobilePaySign")
	@ResponseBody
	public HashMap<String, Object> mobileReportSign(HttpServletRequest request,@RequestBody Map<String, Object> map) throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try{
			inspectionPaySignService.mobileReportDrawSaveSgnFile(request,map);
			wrapper.put("success", true);
			//wrapper.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			wrapper.put("success", false);
			wrapper.put("msg", "保存报告领取签名信息失败，请重试！");
		}
		return wrapper;
	}
	
	/**
	 * 保存设备id
	 * @param clientid
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "addCid")
	@ResponseBody
	public HashMap<String, Object> addCid(String clientid,String name){
		String successMsg= inspectionPaySignService.addCid(clientid, name);
		if(successMsg.equals("0")){
			return JsonWrapper.failureWrapperMsg("保存失败！");
		}else{
			return JsonWrapper.successWrapper(successMsg);
		}
	}
}
