package com.lsts.report.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.security.CurrentSessionUser;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.constant.Constant;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.service.DeviceService;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.service.InspectionInfoService;
import com.lsts.report.bean.Report;

import com.lsts.report.service.ReportErrorRecordInfoService;
import com.lsts.report.service.ReportService;

/**
 * 报告管理 web controller
 * 
 * @author 肖慈边 2014-1-23
 */

@Controller
@RequestMapping("report/basic")
public class ReportAction extends SpringSupportAction<Report, ReportService> {

	@Autowired
	private ReportService reportService;
	@Autowired
	private InspectionInfoService infoService;
	@Autowired
	private DeviceService deviceService;
//	@Autowired
//	private ReportErrorRecordInfo reportErrorRecordInfo;

	@Override
	public HashMap<String, Object> save(HttpServletRequest request,
			Report entity) throws Exception {
		// TODO Auto-generated method stub

		System.out.print(111111);
		return super.save(request, entity);
	}

	/**
	 * 查询报告类型
	 * 
	 * @param response
	 */
	@RequestMapping(value = "getReportType")
	@ResponseBody
	public void getReportType(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String unit_id = request.getParameter("unitId");
			String check_type = request.getParameter("checkType");
			String isJson = StringUtil.transformNull(request
					.getParameter("isJson"));
			String chart = reportService.getReportType(unit_id, check_type,
					isJson);
			response.setContentType("text/xml;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(JSONArray.fromString(chart));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 测试生成报告书编号
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "generateReportCode")
	@ResponseBody
	public HashMap<String, Object> generateReportCode(HttpServletRequest request) {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try {
			List<InspectionInfo> list = infoService.queryAll();
			List<String> report_codeList = new ArrayList<String>();
			for (InspectionInfo inspectionInfo : list) {
				if (inspectionInfo.getInspection() != null) {
					DeviceDocument device = deviceService.get(inspectionInfo
							.getFk_tsjc_device_document_id());
					String report_sn = reportService.generateReportCode(
							device.getDevice_sort_code(), inspectionInfo
									.getInspection().getCheck_type(), inspectionInfo.getReport_type(), inspectionInfo.getCheck_unit_id());
					if (StringUtil.isNotEmpty(report_sn)) {
						report_codeList.add(report_sn);
					}
				}
			}
			wrapper.put("success", true);
			wrapper.put("message", "生成报告书编号：" + report_codeList);
		} catch (Exception e) {
			log.error(e.getMessage());
			wrapper.put("success", false);
			wrapper.put("message", "生成报告书编号失败！");
			e.printStackTrace();
		}
		return wrapper;
	}
	//报告启用
	@RequestMapping(value = "enable")
	@ResponseBody
	public HashMap<String, Object> enable(HttpServletRequest request,String ids) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			reportService.enable(request,ids);
			map.put("success", true);
				
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "删除失败！");
		}
		return map;
	}
	//报告停用
	@RequestMapping(value = "disable")
	@ResponseBody
		public HashMap<String, Object> disable(HttpServletRequest request,String ids) throws Exception {
			HashMap<String, Object> map = new HashMap<String, Object>();
			try {
				reportService.disable(request,ids);
				map.put("success", true);
			} catch (Exception e) {
				map.put("success", false);
				map.put("msg", "删除失败！");
			}
			return map;
		}
	
	@RequestMapping(value = "createDefaultTemplete")
	@ResponseBody
	public HashMap<String, Object> createDefaultTemplete(HttpServletRequest request) throws Exception {
		try {
			reportService.createDefaultTemplete(request);
			return JsonWrapper.successWrapper();
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg(e.getMessage());
		}
	}
}
