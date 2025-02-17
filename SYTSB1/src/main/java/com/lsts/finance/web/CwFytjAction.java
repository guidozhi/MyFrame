package com.lsts.finance.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.finance.bean.CwFytjDTO;
import com.lsts.finance.service.CwFytjManager;
import com.lsts.statistics.bean.DeviceCountDTO;


/**
 *财务统计Action
 *
 * @author dxf
 *
 * @date 2015年10月16日
 */
@Controller
@RequestMapping("cw/fytj")
public class CwFytjAction {
	private Log log = LogFactory.getLog(getClass());
    @Autowired
    private CwFytjManager  cwFytjManager;
    
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**获取统计数据
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getCwData")
	@ResponseBody
	public HashMap<String, Object> getCwData(HttpServletRequest request) throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String org_id = request.getParameter("org_id");
		String userName = request.getParameter("userName");
		try {
			if (StringUtil.isEmpty(startDate)) {
				startDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
			}
			if (StringUtil.isEmpty(endDate)) {
				endDate = DateUtil.getDateTime("yyyy-MM-dd", new Date());
			}
			wrapper.put("success", true);
			wrapper.put("data", cwFytjManager.getCwData(startDate, endDate, org_id,userName));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg("获取财务报账数据失败，请重试！");
		}
		return wrapper;
	}
	/**获取统计数据(部门)
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getCwBmData")
	@ResponseBody
	public HashMap<String, Object> getCwBmData(HttpServletRequest request) throws Exception {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String org_id = request.getParameter("org_id");
		String unit = request.getParameter("unit");
		try {
			if (StringUtil.isEmpty(startDate)) {
				startDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
			}
			if (StringUtil.isEmpty(endDate)) {
				endDate = DateUtil.getDateTime("yyyy-MM-dd", new Date());
			}
			wrapper.put("success", true);
			wrapper.put("data", cwFytjManager.getCwBmData(startDate, endDate, org_id,unit));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg("获取财务报账数据失败，请重试！");
		}
		return wrapper;
	}
	/**
	 * 各部门人员业务统计表导出
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="exportCount")
	public String exportCountByUser(HttpServletRequest request){	
		try {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String org_id = request.getParameter("org_id1");
			String org_name = request.getParameter("org_id");
			String userName = request.getParameter("userName");
		
			if (StringUtil.isEmpty(startDate)) {
				startDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
			}
			if (StringUtil.isEmpty(endDate)) {
				endDate = DateUtil.getDateTime("yyyy-MM-dd", new Date());
			}
			List<CwFytjDTO> list = cwFytjManager.getCwData(startDate, endDate, org_id,userName);	
			request.getSession().setAttribute("startDate", startDate);
			request.getSession().setAttribute("endDate", endDate);
			request.getSession().setAttribute("org_name", org_name);
			request.getSession().setAttribute("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("各部门人员业务统计表导出失败！");
		}
		return "app/finance/cw_fytj_export";
	}
	/**
	 * 各部门业务统计表导出（部门）
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="exportBmCount")
	public String exportCountByBm(HttpServletRequest request){	
		try {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String org_id = request.getParameter("org_id1");
			String org_name = request.getParameter("org_id");
			String unit = request.getParameter("unit");
			if (StringUtil.isEmpty(startDate)) {
				startDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
			}
			if (StringUtil.isEmpty(endDate)) {
				endDate = DateUtil.getDateTime("yyyy-MM-dd", new Date());
			}
			List<CwFytjDTO> list = cwFytjManager.getCwBmData(startDate, endDate, org_id,unit);	
			request.getSession().setAttribute("startDate", startDate);
			request.getSession().setAttribute("endDate", endDate);
			request.getSession().setAttribute("org_name", org_name);
			request.getSession().setAttribute("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("各部门人员业务统计表导出失败！");
		}
		return "app/finance/cw_bmfytj_export";
	}
	
}