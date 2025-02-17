package com.lsts.finance.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.finance.bean.CwFytjDTO;
import com.lsts.finance.bean.Pxfbxd;
import com.lsts.finance.service.FeeStatisticsService;
@RequestMapping("feeStatisticsAction")
@Controller
public class FeeStatisticsAction extends SpringSupportAction<Pxfbxd, FeeStatisticsService>{

	@Autowired
	FeeStatisticsService feeStatisticsService;
	
	@RequestMapping(value="statisticsFee")
	@ResponseBody
	public HashMap<String, Object> statisticsFee(String start,String end,String dept,String unit) throws Exception{
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.statisticsFee(start, end, dept,unit);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	/**
	 * 车辆费用统计
	 * author pingZhou
	 * @param start
	 * @param end
	 * @param dept
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="statisticsCarFee")
	@ResponseBody
	public HashMap<String, Object> statisticsCarFee(String start,String end,String carIds) throws Exception{
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.statisticsCarFee(start, end, carIds);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	/**
	 * 按经济类型统计各部门报销费用
	 * @param request
	 * @return
	 */
	@RequestMapping(value="statisticsIncome")
	@ResponseBody
	public HashMap<String, Object> statisticsIncome(HttpServletRequest request,String startDate,String endDate,String dept,String unit) throws Exception{	
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.statisticsIncome(startDate, endDate, dept,unit);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	/**
	 * 按各部门收支对比
	 * @param request
	 * @return
	 */
	@RequestMapping(value="compareFee")
	@ResponseBody
	public HashMap<String, Object> incomeCompareFee(HttpServletRequest request,String startDate,String endDate,String dept,String unit) throws Exception{	
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.incomeCompareFee(startDate, endDate, dept,unit);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	/**
	 * 按经济类型统计各部门报销费用
	 * @param request
	 * @return
	 */
	@RequestMapping(value="exportDeptFee")
	public String exportDeptFee(HttpServletRequest request,String startDate,String endDate,String dept,String unit,String items) throws Exception{	
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.statisticsFee(startDate, endDate, dept,unit);
			request.getSession().setAttribute("items", items);
			request.getSession().setAttribute("start", startDate);
			request.getSession().setAttribute("end", endDate);
			request.getSession().setAttribute("data",map);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("各部门报销费用统计表导出失败！");
		}
		return "app/finance/statistics/export_fee";
	}
	/**
	 * 按经济类型统计各部门报销费用
	 * @param request
	 * @return
	 */
	@RequestMapping(value="exportDeptIncome")
	public String exportDeptIncome(HttpServletRequest request,String startDate,String endDate,String dept,String unit,String items) throws Exception{	
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.statisticsIncome(startDate, endDate, dept,unit);
			request.getSession().setAttribute("items", items);
			request.getSession().setAttribute("start", startDate);
			request.getSession().setAttribute("end", endDate);
			request.getSession().setAttribute("data",map);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("各部门报销费用统计表导出失败！");
		}
		return "app/finance/statistics/export_income";
	}
	/**
	 * 按经济类型统计各部门报销费用
	 * @param request
	 * @return
	 */
	@RequestMapping(value="exportCompare")
	public String exportCompare(HttpServletRequest request,String startDate,String endDate,String dept,String unit) throws Exception{	
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.incomeCompareFee(startDate, endDate, dept,unit);
			request.getSession().setAttribute("start", startDate);
			request.getSession().setAttribute("end", endDate);
			request.getSession().setAttribute("data",map);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("各部门报销费用统计表导出失败！");
		}
		return "app/finance/statistics/export_compare";
	}
	
	
	/**
	 * 存货出入库统计
	 * author pingZhou
	 * @param request
	 * @param startDate
	 * @param endDate
	 * @param dept
	 * @param unit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="inventory")
	@ResponseBody
	public HashMap<String, Object> inventory(HttpServletRequest request,String startDate,String endDate,String dept,
			String unit) throws Exception{	
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.inventory(startDate, endDate, dept,unit);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value="exportInventory")
	public String exportInventory(HttpServletRequest request,String startDate,String endDate,String dept,String unit,String items) throws Exception{	
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.inventory(startDate, endDate, dept,unit);
			request.getSession().setAttribute("items", items);
			request.getSession().setAttribute("start", startDate);
			request.getSession().setAttribute("end", endDate);
			request.getSession().setAttribute("data",map);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("存货统计表导出失败！");
		}
		return "app/finance/statistics/export_inventory";
	}
	
	/**
	 * 车辆费用统计
	 * author pingZhou
	 * @param request
	 * @param start
	 * @param end
	 * @param dept
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="exportCarFee")
	public String exportCarFee(HttpServletRequest request,String startDate,String endDate,String carId,String items) throws Exception{
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.statisticsCarFee(startDate, endDate, carId);
			request.getSession().setAttribute("items", items);
			request.getSession().setAttribute("start", startDate);
			request.getSession().setAttribute("end", endDate);
			request.getSession().setAttribute("data",map);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return "app/finance/statistics/export_car_fee";
	}
	//--------------------------------------
	/**
	 * 导出部门财务收入明细
	 * @param request
	 * @param dept
	 * @param clss
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value="exportDeptDetail")
	public String exportDeptDetail(HttpServletRequest request,String dept,String clss,String startDate,String endDate) throws Exception{	
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.statisticsDeptDetail( dept,clss,startDate,endDate);
			request.getSession().setAttribute("data",map);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("各部门报销费用明细统计表导出失败！");
		}
		return "app/finance/statistics/export_dept_detail";
	}
	
	/**
	 * 部门财务收入明细
	 * @param request
	 * @param dept
	 * @param clss
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="deptDetail")
	@ResponseBody
	public HashMap<String, Object> deptDetail(HttpServletRequest request,String dept,String clss,String startDate,String endDate) throws Exception{	
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.statisticsDeptDetail( dept,clss,startDate,endDate);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	

	/**
	 * 按经济类型统计各部门报销费用明细
	 * @param request
	 * @param startDate
	 * @param endDate
	 * @param dept
	 * @param unit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="exportDeptFeeMX")
	public String exportDeptFeeMX(HttpServletRequest request,String startDate,String endDate,String dept,String clss) throws Exception{	
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.statisticsFeeMX(startDate, endDate, dept, clss);
			request.getSession().setAttribute("start", startDate);
			request.getSession().setAttribute("end", endDate);
			request.getSession().setAttribute("data",map);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("各部门报销费用统计表导出失败！");
		}
		return "app/finance/statistics/export_fee_detail";
	}
	

	@RequestMapping(value="deptFeeDetail")
	@ResponseBody
	public HashMap<String, Object> deptFeeDetail(HttpServletRequest request,String startDate,String endDate,String dept,String clss) throws Exception{	
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = feeStatisticsService.statisticsFeeMX( startDate,endDate,dept,clss);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
}
