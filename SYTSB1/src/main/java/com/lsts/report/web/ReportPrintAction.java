package com.lsts.report.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.utils.StringUtil;
import com.lsts.report.bean.ReportPrint;
import com.lsts.report.bean.ReportPrintRecordDTO;
import com.lsts.report.service.ReportPrintService;
import com.scts.maintenance.bean.MaintenanceInfoDTO;

/**
 * 检验资料报送打印签收业务控制器
 * 
 * @ClassName ReportPrintAction
 * @JDK 1.7
 * @author GaoYa
 * @date 2015-09-06 下午01:20:00
 */
@Controller
@RequestMapping("report/print")
public class ReportPrintAction extends
		SpringSupportAction<ReportPrint, ReportPrintService> {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private ReportPrintService reportPrintService;


	// 保存报送打印信息
	@RequestMapping(value = "saveBasic")
	@ResponseBody
	public HashMap<String, Object> saveBasic(HttpServletRequest request, @RequestBody ReportPrint reportPrint)
			throws Exception {
		try {
			return reportPrintService.saveBasic(reportPrint, request);
		} catch (Exception e) {
			log.debug(e.toString());
			e.printStackTrace();
		}
		return JsonWrapper.successWrapper(reportPrint);
	}
	
	// 保存并提交报送打印信息
	@RequestMapping(value = "commitBasic")
	@ResponseBody
	public HashMap<String, Object> commitBasic(HttpServletRequest request, @RequestBody ReportPrint reportPrint)
			throws Exception {
		try {
			return reportPrintService.commitBasic(reportPrint, request);
		} catch (Exception e) {
			log.debug(e.toString());
			e.printStackTrace();
		}
		return JsonWrapper.successWrapper(reportPrint);
	}

	// 获取报送打印信息
	@RequestMapping(value = "getDetail")
	@ResponseBody
	public HashMap<String, Object> getDetail(HttpServletRequest request)
			throws Exception {
		String id = request.getParameter("id");
		try {
			return reportPrintService.getDetail(id);
		} catch (Exception e) {
			log.debug(e.toString());
			return JsonWrapper.failureWrapperMsg("读取报送打印信息失败！");
		}
	}
	
	// 获取报送打印信息（用于打印）
	@RequestMapping(value = "getPrintDetail")
	@ResponseBody
	public HashMap<String, Object> getPrintDetail(HttpServletRequest request)
			throws Exception {
		String id = request.getParameter("id");
		try {
			return reportPrintService.getPrintDetail(id);
		} catch (Exception e) {
			log.debug(e.toString());
			return JsonWrapper.failureWrapperMsg("读取报送打印信息失败！");
		}
	}

	// 删除报送打印信息
	@RequestMapping(value = "del")
	@ResponseBody
	public HashMap<String, Object> del(HttpServletRequest request, String ids)
			throws Exception {
		String[] idArr = ids.split(",");
		for (int i = 0; i < idArr.length; i++) {
			reportPrintService.del(request, idArr[i]);
		}
		return JsonWrapper.successWrapper(ids);
	}

	// 报送打印提交
	@RequestMapping(value = "commit")
	@ResponseBody
	public HashMap<String, Object> commit(HttpServletRequest request, String ids)
			throws Exception {
		HashMap<String, Object> wrapper = JsonWrapper.successWrapper();
		boolean isSuccess = reportPrintService.commit(ids, request);
		wrapper.put("success", isSuccess);
		return wrapper;

	}
	
	// 签收报送打印信息
	@RequestMapping(value = "receive")
	@ResponseBody
	public HashMap<String, Object> receive(HttpServletRequest request,
			String id) throws Exception {
		HashMap<String, Object> wrapper = JsonWrapper.successWrapper();
		boolean isSuccess = reportPrintService.receives(id, request);
		wrapper.put("success", isSuccess);
		return wrapper;
	}
	
	// 批量签收报送打印
	@RequestMapping(value = "receives")
	@ResponseBody
	public HashMap<String, Object> receives(HttpServletRequest request, String ids)
			throws Exception {
		HashMap<String, Object> wrapper = JsonWrapper.successWrapper();
		boolean isSuccess = reportPrintService.receives(ids, request);
		wrapper.put("success", isSuccess);
		return wrapper;
	}
	
	// 获取报告信息
	@RequestMapping(value = "queryReportInfos")
	@ResponseBody
	public HashMap<String, Object> getReportInfos(HttpServletRequest request)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String ids = request.getParameter("ids");
		try {
			map.put("list", reportPrintService.queryReportInfos(ids));
			map.put("exitList", reportPrintService.queryReportInfos(ids));
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
			return JsonWrapper.failureWrapperMsg("读取报告信息失败！");
		}
		return map;
	}
	
	// 验证报告是否报送且已签收状态
	@RequestMapping(value = "validateInfos")
	@ResponseBody
	public HashMap<String, Object> validateInfos(HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String ids = request.getParameter("ids");
		try {
			map = reportPrintService.validateInfos(ids);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
			return JsonWrapper.failureWrapperMsg("读取验证信息失败！");
		}
		return map;
	}
	
	/**
	 * 根据id查询报告报送打印信息
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "queryReportPrints")
	@ResponseBody
	public HashMap<String, Object> queryReportPrints(HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		String reportType = request.getParameter("reportType");
		System.out.println("报送ID："+id+",报表类型："+reportType);
		try {
			map = reportPrintService.queryReportPrints(id, reportType);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
			return JsonWrapper.failureWrapperMsg("获取报送打印信息失败！");
		}
		return map;
	}
}
