package com.lsts.hall.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import util.TS_Util;

import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.StringUtil;
import com.lsts.hall.bean.ReportHall;
import com.lsts.hall.service.ReportHallParaService;
import com.lsts.hall.service.ReportHallService;
import com.lsts.inspection.bean.InspectionHall;
import com.lsts.log.service.SysLogService;

/**
 * @author Mr.Dawn 
 * @date 2014-12-22
 * @version 1.1
 * @summary
 */

@Controller
@RequestMapping("reportHallAction")
public class ReportHallAction extends
		SpringSupportAction<ReportHall, ReportHallService> {

	@Autowired
	private ReportHallService reportHallService;
	@Autowired
	private ReportHallParaService reportHallParaService;
	@Autowired
	private SysLogService logService;

	/**
	 * 保存报检大厅信息
	 */
	@RequestMapping(value = "saveBasic")
	@ResponseBody
	public HashMap<String, Object> saveBasic(@RequestBody ReportHall hall,
			HttpServletRequest request) throws Exception {
		try {
			ReportHall baseinfo = reportHallService.saveBasic(request, hall);

			return JsonWrapper.successWrapper(baseinfo);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg("保存失败！");
		}
	}


	/** 获取大厅报检信息 **/
	@RequestMapping(value = "getDetail")
	@ResponseBody
	public HashMap<String, Object> getDetail(HttpServletRequest request)
			throws Exception {
		String id = request.getParameter("id");
		String hall_id = request.getParameter("hall_id");
		ReportHall reportHall = reportHallService.getDetail(id, hall_id);
		return JsonWrapper.successWrapper(reportHall);

	}

	/** 提交数据到科室 **/
	@RequestMapping(value = "subDep")
	@ResponseBody
	public HashMap<String, Object> subDep(HttpServletRequest request)
			throws Exception {

		String ids = request.getParameter("ids");

		HashMap<String, Object> map = new HashMap<String, Object>();

		reportHallService.subDep(request, ids);

		map.put("success", true);

		return JsonWrapper.successWrapper(map);

	}
	
	/**
	 * 获取大厅报检回执单打印内容
	 * 
	 * @param request
	 * @param id
	 *            -- 大厅报检ID
	 * @return
	 */
	/*@RequestMapping(value = "getPrintContent")
	@ResponseBody
	public HashMap<String, Object> getPrintContent(HttpServletRequest request,
			String id) {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try {
			if (StringUtil.isNotEmpty(id)) {
				ReportHall reportHall = reportHallService.get(id);
				String inspection_type = codeTablesService.getValueByCode(
						ReportConstant.CT_BASE_CHECK_TYPE,
						reportHall.getInspection_type());
				reportHall.setInspection_type(inspection_type);
				// 获取大厅报检回执单打印内容
				String printContent = ReportUtil
						.getInspectionHallPrintContent(reportHall);
				wrapper.put("success", true);
				wrapper.put("printContent", printContent);
			}
		} catch (Exception e) {
			log.error("获取大厅报检回执单打印内容出错：" + e.getMessage());
			wrapper.put("success", false);
			wrapper.put("message", "获取大厅报检回执单打印内容出错！");
			e.printStackTrace();
		}
		return wrapper;
	}*/

	/**
	 * 获取大厅报检申请受理书
	 * 
	 * @param request
	 * @param id
	 *            -- 大厅报检ID
	 * @return
	 */
	/*@RequestMapping(value = "getPrintAcceptContent")
	@ResponseBody
	public HashMap<String, Object> getPrintAcceptContent(
			HttpServletRequest request, String id) {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		try {
			if (StringUtil.isNotEmpty(id)) {
				ReportHall reportHall = reportHallService.get(id);
				String inspection_type = codeTablesService.getValueByCode(
						ReportConstant.CT_BASE_CHECK_TYPE,
						reportHall.getInspection_type());
				reportHall.setInspection_type(inspection_type);
				// 获取大厅报检申请受理书内容
				String printContent = ReportUtil
						.getInspectionHallPrintAcceptContent(reportHall);
				wrapper.put("success", true);
				wrapper.put("printContent", printContent);
			}
		} catch (Exception e) {
			log.error("获取大厅报检申请受理书内容出错：" + e.getMessage());
			wrapper.put("success", false);
			wrapper.put("message", "获取大厅报检申请受理书内容出错！");
			e.printStackTrace();
		}
		return wrapper;
	}*/

	/** 撤回报检大厅信息 **/
	@RequestMapping(value = "recall")
	@ResponseBody
	public HashMap<String, Object> recallHall(String ids) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();

		int dcount = reportHallService.recall(ids);
		map.put("success", true);
		if (ids.indexOf(",") == -1) {
			if (dcount > 0) {
				map.put("success", false);
				map.put("message", "你选择的记录已生成检验任务，不能撤回！");
			}
		}
		return map;

	}
	
	/** 作废报检大厅信息 **/
	@RequestMapping(value = "del")
	@ResponseBody
	public HashMap<String, Object> del(HttpServletRequest request, String ids) throws Exception {
		reportHallService.del(request, ids);
		return JsonWrapper.successWrapper(ids);
	}

	/** 保存科室流转信息 **/
	@RequestMapping(value = "saveTransfer")
	@ResponseBody
	public HashMap<String, Object> saveTransfer(HttpServletRequest request)
			throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("device_id", request.getParameter("device_id"));
		map.put("unit_code", request.getParameter("unit_code"));

		try {
			reportHallService.saveTransfer(map);
			return JsonWrapper.successWrapper(map);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg("保存失败！");
		}

	}
	
	/** 保存分配信息 **/
	@RequestMapping(value = "savePlan")
	@ResponseBody
	public HashMap<String, Object> savePlan(HttpServletRequest request)
			throws Exception {
		try {
			CurrentSessionUser user = super.getCurrentUser();

			Map<String, Object> map = new HashMap<String, Object>();
			
			String hall_id = request.getParameter("hall_id");
			ReportHall reportHall = reportHallService.get(hall_id);
			
			String op_ids = request.getParameter("op_ids");
			String check_ids = request.getParameter("check_ids");
			String op_name = request.getParameter("op_name");
			String check_name = request.getParameter("check_name");
			String hall_para_id = request.getParameter("hall_para_id");
			check_ids = check_ids + ',' + op_ids;
			check_ids = TS_Util.distArrays(check_ids.split(","));
			check_name = check_name + ',' + op_name;
			check_name = TS_Util.distArrays(check_name.split(","));

			map.put("hall_para_id", hall_para_id);
			map.put("op_ids", op_ids);
			map.put("inc_time", request.getParameter("inc_time"));
			map.put("check_ids", check_ids);
			map.put("op_name", op_name);
			map.put("check_name", check_name);

			reportHallService.savePlan(map,request);
			
			if (hall_para_id.indexOf(",") != -1) {
				String temp[] = hall_para_id.split(",");
				for (int i = 0; i < temp.length; i++) {
					logService.setLogs(temp[i], "【任务分配】",
						"参检人员："+check_name, user.getId(), user.getName(),
							new Date(), request.getRemoteAddr());
				}
			}
			return JsonWrapper.successWrapper(map);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg("保存失败！");
		}

	}
	
	
	/** 保存分配信息 **/
	@RequestMapping(value = "savePlanMobile")
	@ResponseBody
	public HashMap<String, Object> savePlanMobile(HttpServletRequest request)
			throws Exception {
		try {

			Map<String, Object> map = new HashMap<String, Object>();

			String op_ids = request.getParameter("op_ids");
			String check_ids = request.getParameter("check_ids");
			String op_name = request.getParameter("op_name");
			String check_name = request.getParameter("check_name");
			check_ids = check_ids + ',' + op_ids;
			check_ids = TS_Util.distArrays(check_ids.split(","));
			check_name = check_name + ',' + op_name;
			check_name = TS_Util.distArrays(check_name.split(","));

			map.put("hall_para_id", request.getParameter("hall_para_id"));
			map.put("op_ids", op_ids);
			map.put("inc_time", request.getParameter("inc_time"));
			map.put("check_ids", check_ids);
			map.put("op_name", op_name);
			map.put("check_name", check_name);

			reportHallService.savePlanMobile(map,request);
			return JsonWrapper.successWrapper(map);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg("保存失败！");
		}

	}
	
	// 查询流程步骤信息
	@RequestMapping(value = "getFlowStep")
	@ResponseBody
	public ModelAndView getFlowStep(HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map = reportHallService
				.getFlowStep(request.getParameter("id"));
		ModelAndView mav = new ModelAndView("app/insing/flow_card", map);
		return mav;
	}

	
	/** 任务分配退回报检大厅 **/
	@RequestMapping(value = "backHall")
	@ResponseBody
	public Map<String, Object> backHall(HttpServletRequest request)
			throws Exception {
		CurrentSessionUser user = super.getCurrentUser();
		try {
			String hall_id = request.getParameter("hall_id");
			String back_reason = request.getParameter("back_reason");
			ReportHall reportHall = reportHallService.get(hall_id);
			reportHall.setFlow_status("3");
			reportHall.setBack_reason(back_reason);
			reportHallService.save(reportHall);
			logService.setLogs(hall_id, "【任务分配】退回报检大厅",
					back_reason, user.getId(), user.getName(),
					new Date(), request.getRemoteAddr());
			//return reportHallService.backHall(hall_id);
			return JsonWrapper.successWrapper("退回成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg("保存失败！");
		}
	}
	//-------------新版移动端代码-----------------------------------
	/**
     * 保存手机APP端的大厅报检信息
     *
     * @param hall
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "saveBasicApp")
    @ResponseBody
    public HashMap<String, Object> saveBasicApp(@RequestBody ReportHall hall) throws Exception {
    	CurrentSessionUser user = SecurityUtil.getSecurityUser();
        if (StringUtil.isEmpty(hall.getId())) {
        	
            hall.setReg_date(new Date());
            hall.setFlow_status("1");
            hall.setReg_op(user.getName());
            //表示数据是从手机APP存入的
            hall.setData_status("33");

            if (StringUtil.isEmpty(hall.getHall_no())) {
                // 获取表单号
                String num = TS_Util.getHallNumber();
                hall.setHall_no(num);

            }
            reportHallService.save(hall);
            return JsonWrapper.successWrapper(hall);

        } else {
        	ReportHall InspectionHall = reportHallService.get(hall.getId());

            if(InspectionHall.getReg_op()==null) {
            	InspectionHall.setReg_op(user.getName());
            }
            InspectionHall.setCom_name(hall.getCom_name());
            InspectionHall.setDep_address(hall.getDep_address());
            InspectionHall.setInspection_type(hall.getInspection_type());
            InspectionHall.setArea_code(hall.getArea_code());
            InspectionHall.setContant_person(hall.getContant_person());
            InspectionHall.setContant_phone(hall.getContant_phone());
            InspectionHall.setRemark(hall.getRemark());
            reportHallService.save(InspectionHall);
            return JsonWrapper.successWrapper(InspectionHall);
        }


    }
    

    /**
     * APP获取大厅报检详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getHallDetail")
    @ResponseBody
    public HashMap<String, Object> getHallDetail(String id) throws Exception {
        if (StringUtil.isEmpty(id)) {

            return JsonWrapper.failureWrapperMsg("报检业务ID为空!");
        }
        ReportHall reportHall = reportHallService.get(id);

        return JsonWrapper.successWrapper(reportHall);

    }
    
    /**
     * 移动端大厅报检提交验证设备有没流转信息
     */
    @RequestMapping(value = "checkDateStatus")
    @ResponseBody
    public HashMap<String,Object> checkDateStatus(String ids){
        HashMap<String,Object> map = new HashMap<String, Object>();
        try {
            map = reportHallService.checkDateStatus(ids);
        } catch (Exception e) {
            map.put("success",false);
        }
        return map;
    }


    
    
	
	/** 移动端保存分配信息 **/
	@RequestMapping(value = "savePlanM")
	@ResponseBody
	public HashMap<String, Object> savePlanM(HttpServletRequest request,String hallParamId)
			throws Exception {
		try {

			reportHallService.savePlanM(hallParamId,request);
//			departInspectRecordService.saveCheck(check_name,check_ids,request.getParameter("hall_para_id"));
			return JsonWrapper.successWrapper();
		} catch (Exception e) {
			e.printStackTrace();
			return JsonWrapper.failureWrapperMsg("保存失败！");
		}

	}
	//-------------新版移动端代码-----------------------------------
}