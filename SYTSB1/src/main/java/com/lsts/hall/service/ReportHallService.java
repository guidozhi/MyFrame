package com.lsts.hall.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.core.exception.KhntException;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.hall.bean.ReportHall;
import com.lsts.hall.bean.ReportHallPara;
import com.lsts.hall.dao.ReportHallDao;
import com.lsts.log.bean.SysLog;
import com.lsts.log.service.SysLogService;

import util.TS_Util;

/**
 * @author Mr.Dawn
 * @date 2014-12-22
 * @summary 大厅报检业务逻辑处理类
 */
@Service("reportHallService")
public class ReportHallService extends
		EntityManageImpl<ReportHall, ReportHallDao> {
	@Autowired
	private ReportHallDao reportHallDao;
	@Autowired
	private SysLogService logService;
	
	/**
	 * 
	 * @param hall
	 * @param user
	 * @param map
	 *            前台提供的参数
	 * @return
	 * @throws Exception
	 */
	public ReportHall saveBasic(HttpServletRequest request, ReportHall hall) throws Exception {

		CurrentSessionUser user = SecurityUtil.getSecurityUser();

		for (ReportHallPara hallPara : hall.getHallPara()) {

			hallPara.setReportHall(hall);
			hallPara.setIs_rec("1");// 是否接收
			hallPara.setIs_plan("1");// 是否分配 默认否
			hallPara.setTransfer_op(user.getUserName());
			hallPara.setTransfer_date(new Date());
		}
		hall.setReg_date(new Date());
		hall.setFlow_status("1");
		hall.setReg_op(user.getUserName());
		hall.setData_status("1");

		if (StringUtil.isEmpty(hall.getHall_no())) {
			// 获取表单号
			String num = TS_Util.getHallNumber();
			hall.setHall_no(num);

		}
		reportHallDao.save(hall);
		
		logService.setLogs(hall.getId(), "电梯业务受理",
				"添加报检信息", user.getId(), user.getName(),
				new Date(), request.getRemoteAddr());
		return hall;
	}

	/**
	 * 删除报检大厅信息
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void del(HttpServletRequest request, String ids) throws Exception {
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		
		String id[] = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			reportHallDao
					.createSQLQuery(
							"update TZSB_INSPECTION_HALL set data_status='2' where id =?",
							id[i]).executeUpdate();
			// 记录日志
			logService.setLogs(id[i], "作废电梯业务", "移动检验电梯业务作废", curUser.getId(), curUser.getName(), new Date(), request.getRemoteAddr());
		}
	}

	/**
	 * 撤回报检大厅信息
	 * 
	 * @param ids
	 * @return 失败条数
	 * @throws Exception
	 */
	public int recall(String ids) throws Exception {
		int dcount = 0;
		String id[] = ids.split(",");

		for (int i = 0; i < id.length; i++) {
			String sql = "select count(1) from tzsb_inspection where fk_hall_para_id=(  select id from tzsb_inspection_hall_para where fk_hall_id=? )";
			Object robj = reportHallDao.createSQLQuery(sql, id[i])
					.uniqueResult();
			int count = Integer.parseInt(robj.toString());
			if (count > 0) {
				dcount += 1;
			} else
				reportHallDao
						.createSQLQuery(
								"update  TZSB_INSPECTION_HALL  set flow_status='1' where id =?",
								id[i]).executeUpdate();

		}
		return dcount;

	}

	/**
	 * 提交数据到科室
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void subDep(HttpServletRequest request, String ids) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String id[] = ids.split(",");
		for (int i = 0; i < id.length; i++) {

			reportHallDao.createSQLQuery(
							"update  TZSB_INSPECTION_HALL  set flow_status='2' where id =?",
							id[i]).executeUpdate();
			
			logService.setLogs(id[i], "电梯业务提交",
					"提交报检信息到检验部门", user.getId(), user.getName(),
					new Date(), request.getRemoteAddr());

		}

	}
	public void subDep(String ids) throws Exception {
		// TODO Auto-generated method stub
		//判断是否多个ID
		if(ids.indexOf(",")!=-1){
			
			String id[] =ids.split(",");
			
			for(int i=0;i<id.length;i++){
				
				reportHallDao.createSQLQuery("update  TZSB_INSPECTION_HALL  set flow_status='2' where id =?",id[i] ).executeUpdate();
				
			}
			
		}else{
		
			reportHallDao.createSQLQuery("update  TZSB_INSPECTION_HALL  set flow_status='2' where id = ?", ids).executeUpdate();
			
		}
		
	}
	/**
	 * 获取大厅报检信息
	 * @param id
	 * @param hall_id
	 * @return
	 * @throws Exception
	 */
	public ReportHall getDetail(String id, String hall_id) throws Exception {

		ReportHall hall = reportHallDao.get(hall_id);

		String ids = "";
		// 判断是否多个id
		StringBuffer json = new StringBuffer();
		if (id.indexOf(",") != -1) {
			String temp[] = id.split(",");

			for (int i = 0; i < temp.length; i++) {

				json.append("'").append(temp[i]).append("'");
				if (i != temp.length - 1) {
					json.append(",");
				}

			}
			ids = json.toString();
		} else {
			ids = json.append("'").append(id).append("'").toString();
		}

		String sql = "select t.* from TZSB_INSPECTION_HALL_PARA t where  t.id in("
				+ ids + ")";

		SQLQuery query = reportHallDao.getquery(sql);

		query.addEntity(ReportHallPara.class);

		List list = query.list();

		hall.setParaList(list);

		return hall;

	}

	/**
	 * 保存科室流转信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void saveTransfer(Map<String, Object> map) throws KhntException {

		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String id = map.get("device_id").toString();
		String unit_code = map.get("unit_code").toString();
		String ids = "";
		// 判断是否多个id
		StringBuffer json = new StringBuffer();
		if (id.indexOf(",") != -1) {
			String temp[] = id.split(",");
			for (int i = 0; i < temp.length; i++) {

				json.append("'").append(temp[i]).append("'");
				if (i != temp.length - 1) {
					json.append(",");
				}

			}
			ids = json.toString();
		} else {
			ids = json.append("'").append(id).append("'").toString();
		}

		reportHallDao.createSQLQuery(
				"update  TZSB_INSPECTION_HALL_PARA  set UNIT_CODE='"
						+ unit_code + "',TRANSFER_OP='" + user.getUserName()
						+ "',TRANSFER_DATE=sysdate where id in(" + ids + ")")
				.executeUpdate();

	}
	
	/**
	 * 保存分配信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void savePlan(Map<String, Object> map, HttpServletRequest request)
			throws KhntException {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String hall_id = request.getParameter("hall_id");
		if (!hall_id.contains(",")) {
			String sql = " update tzsb_inspection_hall t set t.inspection_type='"
					+ request.getParameter("inspection_type")
					+ "',t.area_code='"
					+ request.getParameter("area_code")
					+ "',t.dep_address='"
					+ request.getParameter("dep_address")
					+ "',t.contant_person='"
					+ request.getParameter("contant_person")
					+ "',t.contant_phone='"
					+ request.getParameter("contant_phone")
					+ "',t.com_op='"
					+ request.getParameter("com_op")
					+ "',t.remark='"
					+ request.getParameter("remark")
					+ "' where t.id= '"
					+ hall_id + "'";
			updateHall(sql);
		}

		String id = map.get("hall_para_id").toString();
		String op_ids = map.get("op_ids").toString();
		String check_ids = map.get("check_ids").toString();
		String op_name = map.get("op_name").toString();
		String check_name = map.get("check_name").toString();
		String inc_time = map.get("inc_time").toString();
		String cur_time = DateUtil.getCurrentDateTime();
		String ids = "";
		// 判断是否多个id
		StringBuffer json = new StringBuffer();
		if (id.indexOf(",") != -1) {
			String temp[] = id.split(",");

			for (int i = 0; i < temp.length; i++) {

				json.append("'").append(temp[i]).append("'");
				if (i != temp.length - 1) {
					json.append(",");
				}

			}
			ids = json.toString();
		} else {
			ids = json.append("'").append(id).append("'").toString();
		}

		reportHallDao.createSQLQuery(
				"update  TZSB_INSPECTION_HALL_PARA  set INC_TIME=to_date('"
						+ inc_time + "','yyyy-MM-dd'), INC_OP_ID='" + op_ids
						+ "',CHECK_OP_IDS='" + check_ids + "',OP_NAME='"
						+ op_name + "',CHECK_NAME='" + check_name
						+ "',IS_PLAN='2',ASSIGN_USER_ID='" + user.getId()
						+ "',ASSIGN_USER_NAME='" + user.getName()
						+ "',ASSIGN_TIME=to_date('" + cur_time
						+ "','yyyy-MM-dd HH24:mi:ss') where id in(" + ids + ")")
				.executeUpdate();

	}
	
	
	/**
	 * 保存分配信息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void savePlanMobile(Map<String, Object> map,
			HttpServletRequest request) throws KhntException {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String hall_id = request.getParameter("hall_id");
		/*
		 * if (!hall_id.contains(",")) { String sql =
		 * " update tzsb_inspection_hall t set t.inspection_type='" +
		 * request.getParameter("inspection_type") + "',t.area_code='" +
		 * request.getParameter("area_code") + "',t.dep_address='" +
		 * request.getParameter("dep_address") + "',t.contant_person='" +
		 * request.getParameter("contant_person") + "',t.contant_phone='" +
		 * request.getParameter("contant_phone") + "',t.com_op='" +
		 * request.getParameter("com_op") + "',t.remark='" +
		 * request.getParameter("remark") + "' where t.id= '" + hall_id + "'";
		 * updateHall(sql); }
		 */

		String id = map.get("hall_para_id").toString();
		String op_ids = map.get("op_ids").toString();
		String check_ids = map.get("check_ids").toString();
		String op_name = map.get("op_name").toString();
		String check_name = map.get("check_name").toString();
		String inc_time = map.get("inc_time").toString();
		String cur_time = DateUtil.getCurrentDateTime();
		String ids = "";
		// 判断是否多个id
		StringBuffer json = new StringBuffer();
		if (id.indexOf(",") != -1) {
			String temp[] = id.split(",");

			for (int i = 0; i < temp.length; i++) {

				json.append("'").append(temp[i]).append("'");
				if (i != temp.length - 1) {
					json.append(",");
				}

			}
			ids = json.toString();
		} else {
			ids = json.append("'").append(id).append("'").toString();
		}

		reportHallDao.createSQLQuery(
				"update  TZSB_INSPECTION_HALL_PARA  set INC_TIME=to_date('"
						+ inc_time + "','yyyy-MM-dd'), INC_OP_ID='" + op_ids
						+ "',CHECK_OP_IDS='" + check_ids + "',OP_NAME='"
						+ op_name + "',CHECK_NAME='" + check_name
						+ "',IS_PLAN='2',ASSIGN_USER_ID='" + user.getId()
						+ "',ASSIGN_USER_NAME='" + user.getName()
						+ "',ASSIGN_TIME=to_date('" + cur_time
						+ "','yyyy-MM-dd HH24:mi:ss') where id in(" + ids + ")")
				.executeUpdate();

	}
	
	
	/**
	 * 任务分配回退报检大厅
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> backHall(String ids) throws KhntException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String id[] = ids.split(",");
			for (int i = 0; i < id.length; i++) {
				reportHallDao
						.createSQLQuery(
								"update  TZSB_INSPECTION_HALL  set flow_status='3' where id =?",
								id[i]).executeUpdate();

			}
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
		return map;
	}
	
	public HashMap<String, Object>  getFlowStep(String ins_info_id) throws Exception
    {	
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		
		List<SysLog> list = reportHallDao.createQuery("  from SysLog where business_id ='"+ins_info_id+"' order by op_time,id asc").list();
		
		list.size();
	
		map.put("flowStep", list);
		map.put("size", list.size());
		map.put("sn", reportHallDao.get(ins_info_id).getHall_no());
		map.put("success", true);
		
		return map;
    }
	
	public void updateHall(String sql) throws KhntException {
		reportHallDao.createSQLQuery(sql).executeUpdate();
	}
	
	//-------------------新版移动端代码---------------------------------
	/**
	 * 移动端大厅报检提交验证设备有没流转信息
	 */
	public HashMap<String,Object> checkDateStatus(String ids){
		String id[] = ids.split(",");
		HashMap<String,Object> map = new HashMap<String, Object>();
		String sql = "SELECT COUNT(1) FROM TZSB_INSPECTION_HALL T,TZSB_INSPECTION_HALL_PARA T1 WHERE T1.FK_HALL_ID = T.ID AND T.ID = ?";
		for (int i = 0; i < id.length; i++) {
			Integer a = Integer.valueOf(reportHallDao.createSQLQuery(sql,id[i]).uniqueResult().toString());
			if(a >= 1){
			}else{
				map.put("success",false);
				return map;
			}
		}
		map.put("success",true);
		return map;
	}

	
	

	public void savePlanM(String hallParamId, HttpServletRequest request) {
		CurrentSessionUser user=SecurityUtil.getSecurityUser();
		if(hallParamId==null) {
			hallParamId = request.getParameter("hallParamId");
		}
		String opId = request.getParameter("opId");
		String opName = request.getParameter("opName");
		String checkId = request.getParameter("checkId");
		String checkName = request.getParameter("checkName");
		String hallParamIds = hallParamId.replace(",", "','");
		reportHallDao.createSQLQuery(
				"update  TZSB_INSPECTION_HALL_PARA  set INC_OP_ID='" + opId
						+ "',OP_NAME='"
						+ opName
						+ "',check_op_ids='"
						+ checkId
						+ "',check_name='"
						+ checkName+ "',IS_PLAN='2' where id in ('" + hallParamIds + "')")
				.executeUpdate();
		/*@SuppressWarnings("unchecked")

		String[] hallParamIdss = hallParamId.split(",");
		for (int i = 0; i < hallParamIdss.length; i++) {
			String hall_para_id = hallParamIdss[i];
			
			
			String context="";
			List datalist = reportHallDao.getData(hall_para_id);
			if(datalist.size()>0){
				Object[] data = (Object[]) datalist.get(0);
				context="您有一条来自"+user.getName()+"分配的"+data[1]+"的"+data[2]+"台"+data[4]+"的检验任务。";
			}
			List tellist = reportHallDao.getTel(opId);
			JSONArray ptr =new JSONArray();
			if(tellist.size()>0){
				Object[] tel = (Object[]) tellist.get(0);
				JSONObject jObj = new JSONObject();
				if(tel[1]!=null){
					jObj.put("account",tel[1]);
			     	jObj.put("personName",tel[0]);
			     	ptr.put(jObj);
			     	if("true".equals(Factory.getSysPara().getProperty("SYS_SEND_MASSAGE"))){
			     		sysMessageManager.sendMessage(user.getId(), user.getName(), context, "sms", ptr);
					}
				}
			}
		}*/
		
    	
	}
	//-------------------新版移动端代码---------------------------------
	
}
