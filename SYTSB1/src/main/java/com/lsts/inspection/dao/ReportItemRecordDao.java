package com.lsts.inspection.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.khnt.core.crud.dao.impl.EntityDaoImpl;
import com.khnt.rbac.impl.manager.PositionManager;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.IdFormat;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.bean.ElevatorPara;
import com.lsts.device.bean.PressurevesselsPara;
import com.lsts.device.dao.DeviceDao;
import com.lsts.device.dao.ElevatorParaDao;
import com.lsts.device.dao.PressurevesselsParaDao;
import com.lsts.employee.dao.EmployeeCertDao;
import com.lsts.employee.dao.EmployeesDao;
import com.lsts.hall.bean.ReportHallInfo;
import com.lsts.hall.dao.ReportHallInfoDao;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.bean.ReportItemRecord;
import com.lsts.org.bean.EnterInfo;
import com.lsts.org.dao.EnterDao;

import util.TS_Util;



/**
 * 移动端原始记录检验项目参数表数据处理对象
 * @ClassName ReportItemRecordDao
 * @JDK 1.7
 * @author GaoYa
 * @date 2015-11-13 上午10:38:00
 */
@Repository("reportItemRecordDao")
public class ReportItemRecordDao extends EntityDaoImpl<ReportItemRecord> {

	@Autowired
	private EnterDao enterDao;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private InspectionInfoDao infoDao;
	@Autowired
	private ElevatorParaDao elevatorParaDao;
	@Autowired
	private PressurevesselsParaDao pressurevesselsParaDao;
	@Autowired
	private ReportHallInfoDao reportHallInfoDao;
	@Autowired
	private EmployeeCertDao employeeCertDao;
	@Autowired
	private EmployeesDao employeesDao;
	@Autowired
	private PositionManager positionManager;
	


	/**
	 * 获取原始记录检验项目参数
	 * 
	 * @param inspectionInfoId
	 *            -- 检验业务信息ID
	 * @param report_id
	 *            -- 报告ID
	 * @return
	 * @author GaoYa
	 * @date 2014-02-26 上午09:29:00
	 */
	@SuppressWarnings("unchecked")
	public List<ReportItemRecord> queryByInspectionInfoId(String inspectionInfoId, String report_id) {
		List<ReportItemRecord> list = new ArrayList<ReportItemRecord>();
		try {
			if (StringUtil.isNotEmpty(inspectionInfoId) && StringUtil.isNotEmpty(report_id)) {
				String hql = "from ReportItemRecord r where r.fk_inspection_info_id=? and r.fk_report_id=? and data_status!='99'";
				list = this.createQuery(hql, inspectionInfoId, report_id).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<ReportItemRecord> queryByInfoId(String inspectionInfoId) {
		List<ReportItemRecord> list = new ArrayList<ReportItemRecord>();
		try {
			if (StringUtil.isNotEmpty(inspectionInfoId)) {
				String hql = "from ReportItemRecord r where r.fk_inspection_info_id=? and r.data_status!='99'";
				list = this.createQuery(hql, inspectionInfoId).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据业务ID获取已作废的原始记录
	 * 
	 * @param infoId
	 *            -- 业务ID
	 * @return
	 * @author GaoYa
	 * @date 2017-04-29 下午18:20:00
	 */
	@SuppressWarnings("unchecked")
	public List<ReportItemRecord> queryDelByInfoId(String infoId) {
		List<ReportItemRecord> list = new ArrayList<ReportItemRecord>();
		try {
			if (StringUtil.isNotEmpty(infoId)) {
				String hql = "from ReportItemRecord r where r.fk_inspection_info_id=? and r.data_status='99'";
				list = this.createQuery(hql, infoId).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<ReportItemRecord> getItemByItemName(String info_id, String report_type, String item_name) {
		List<ReportItemRecord> list = new ArrayList<ReportItemRecord>();
		try {
			String hql = "from ReportItemRecord where fk_inspection_info_id=? " + " and fk_report_id=? and item_name=?";
			list = this.createQuery(hql, info_id, report_type, item_name).list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<ReportItemRecord> getInfoByItemName(String info_id, String report_type, String item_name) {
		List<ReportItemRecord> list = new ArrayList<ReportItemRecord>();
		try {
			String hql = "from ReportItemRecord where fk_inspection_info_id=? "
					+ " and fk_report_id=? and item_name=? and data_status!='99' order by last_mdy_time desc";
			list = this.createQuery(hql, info_id, report_type, item_name).list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public String getItemValueByItemName(String info_id, String report_type, String item_name) {
		try {
			String sql = "select t.item_value from tzsb_report_item_record t where t.fk_inspection_info_id=? "
					+ " and t.fk_report_id=? and t.item_name=? and t.data_status!='99'";
			List list = this.createSQLQuery(sql, info_id, report_type, item_name).list();
			if (list.size() > 0) {
				return list.get(0) != null ? String.valueOf(list.get(0)) : "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public String getItemIDByItemName(String info_id, String report_type, String item_name) {
		try {
			String sql = "select t.id from tzsb_report_item_record t where t.fk_inspection_info_id=? "
					+ " and t.fk_report_id=? and t.item_name=? and t.data_status!='99'";
			List list = this.createSQLQuery(sql, info_id, report_type, item_name).list();
			if (list.size() > 0) {
				return list.get(0) != null ? String.valueOf(list.get(0)) : "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取所有机电、承压检验部门人员信息
	 * 
	 * @return
	 * @author GaoYa
	 * @throws ParseException
	 * @date 2015-11-12 下午17:49:00
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEmployeeList() throws ParseException {
		//List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
		
		
		
		String sql = 
				"select A.id,\n" +
						"                   A.name,\n" + 
						"                   A.user_id,\n" + 
						"                   case\n" + 
						"                     when A.org_ids is null then\n" + 
						"                      a.org_id\n" + 
						"                     else\n" + 
						"                      A.org_ids\n" + 
						"                   end org_id,\n" + 
						"                   case\n" + 
						"                     when A.org_ids is null then\n" + 
						"                      a.org_name\n" + 
						"                     else\n" + 
						"                      A.org_names\n" + 
						"                   end org_name\n" + 
						"              from (select a.id,\n" + 
						"                           a.name,\n" + 
						"                           a.user_id,\n" + 
						"                           a.org_id,\n" + 
						"                           a.org_name,\n" + 
						"                           (listagg(ep.org_id, ',') WITHIN GROUP(order by ep.id)) as org_ids,\n" + 
						"                           (listagg((select o.org_name\n" + 
						"                                      from sys_org o\n" + 
						"                                     where o.id = ep.org_id),\n" + 
						"                                    ',') WITHIN GROUP(order by ep.id)) as org_names\n" + 
						"\n" + 
						"                      from (select t.id,\n" + 
						"                                   t.name,\n" + 
						"                                   t.org_id,\n" + 
						"                                   o.org_name,\n" + 
						"                                   u.id user_id,\n" + 
						"                                   (select count(1)\n" + 
						"                                      from employee_cert c\n" + 
						"                                     where c.employee_id = t.id\n" + 
						"                                       and c.cert_end_date > sysdate\n" + 
						"                                       and c.cert_type in ('TS',\n" + 
						"                                                           'DT',\n" + 
						"                                                           'DT-1',\n" + 
						"                                                           'RS',\n" + 
						"                                                           'RQ-1',\n" + 
						"                                                           'RQ-2',\n" + 
						"                                                           'GD-1',\n" + 
						"                                                           'GD-2',\n" + 
						"                                                           'GD')\n" + 
						"                                                           ) cc\n" + 
						"                              from employee t, sys_user u, sys_org o\n" + 
						"                             where t.ORG_ID = o.id\n" + 
						"                               and t.id = u.employee_id\n" + 
						"                               and u.status = '1') a,\n" + 
						"                          (select pp.employee_id,p.* from sys_employee_position pp,\n" + 
						"                           sys_position p where pp.position_id(+) = p.id  ）ep\n" + 
						"                     where a.id = ep.employee_id(+)\n" + 
						"                       and a.cc > 0\n" + 
						"                     group by a.id, a.name, a.org_id, a.org_name, a.user_id) A";


		
		
		List<Map<String, Object>> list = this.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		
		List<Map<String, Object>> ret = new ArrayList<>();
		if(list.size()>0) {
			for (Map<String,Object> map : list) {
				Map<String, Object> rr = new HashMap<>();
				for (String key : map.keySet()) {
					rr.put(key.toLowerCase(), map.get(key));
				}
				ret.add(rr);
			}
		}
		
		
		return ret;
		
		
		
		/*String sql = "select t.id,t.name,t.org_id,o.org_name,u.id user_id from employee t,sys_user u,sys_org o ";
		sql += " where t.ORG_ID=o.id and t.id = u.employee_id and u.status='1' ";
		// sql += " and (o.org_code like '%jd%' or o.org_code like '%cy%')";

		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				Map<String, Object> map = new HashMap<String, Object>();

				// 获取相关检验资格证书
				boolean has_cert = false;
				List<EmployeeCert> certList = employeeCertDao.queryEmployeeCertByBasicId(obj[0] + "");
				for (EmployeeCert cert : certList) {
					// 电梯相关建议资格证书
					if ("TS".equals(cert.getCert_type()) || "DT".equals(cert.getCert_type())
							|| "DT-1".equals(cert.getCert_type())) {
						Date cur_date = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.getCurrentDateTime());
						if (cert.getCert_end_date().after(cur_date)) {
							has_cert = true;
						}
					}
					// 罐车相关建议资格证书
					if ("RS".equals(cert.getCert_type()) || "RQ-1".equals(cert.getCert_type())
							|| "RQ-2".equals(cert.getCert_type()) || "GD-1".equals(cert.getCert_type())
							|| "GD-2".equals(cert.getCert_type()) || "GD".equals(cert.getCert_type())) {
						Date cur_date = DateUtil.convertStringToDate("yyyy-MM-dd", DateUtil.getCurrentDateTime());
						if (cert.getCert_end_date().after(cur_date)) {
							has_cert = true;
						}
					}
				}

				// 获取人员兼职部门岗位信息
				List<String> positionList = employeesDao.getPositionIDs(String.valueOf(obj[0]));
				String position_org_ids = "";
				String position_org_names = "";
				for (int j = 0; j < positionList.size(); j++) {
					Position position = positionManager.get(positionList.get(j));
					if (position_org_ids.length() > 0) {
						position_org_ids += ",";
					}
					position_org_ids += position.getOrg().getId();
					if (position_org_names.length() > 0) {
						position_org_names += ",";
					}
					position_org_names += position.getOrg().getOrgName();
				}
				if (has_cert) {
					map.put("id", obj[0]);
					map.put("name", obj[1]);

					if (StringUtil.isNotEmpty(position_org_ids)) {
						map.put("org_id", position_org_ids);
					} else {
						map.put("org_id", obj[2]);
					}
					if (StringUtil.isNotEmpty(position_org_names)) {
						map.put("org_name", position_org_names);
					} else {
						map.put("org_name", obj[3]);
					}

					map.put("user_id", obj[4]);
					returnlist.add(map);
				}
			}
		}
		return returnlist;*/
	}

	/**
	 * 根据部门ID获取检验部门人员信息
	 * 
	 * @return
	 * @author GaoYa
	 * @date @date 2016-02-25 下午14:52:00
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEmployeesByOrgId(String org_id) {
		List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
		String sql = "select t.id,t.name,t.org_id,o.org_name,u.id user_id from employee t,sys_user u,sys_org o ";
		sql += " where t.ORG_ID=o.id and t.id = u.employee_id";
		sql += " and (o.org_code like '%jd%' or o.org_code like '%cy%')";
		sql += " and o.id = ?";
		List list = this.createSQLQuery(sql, org_id).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", obj[0]);
				map.put("name", obj[1]);
				map.put("org_id", obj[2]);
				map.put("org_name", obj[3]);
				map.put("user_id", obj[4]);
				returnlist.add(map);
			}
		}
		return returnlist;
	}

	/**
	 * 移动端获取电梯检验部门信息
	 * 
	 * @return
	 * @author GaoYa
	 * @date @date 2016-02-26 上午10:59:00
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryDtOrgs() {
		List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
		String sql = "select o.id,o.org_name from sys_org o ";
		sql += " where o.org_code like '%jd%' and o.org_code != 'jd6'";
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", obj[0]);
				map.put("text", obj[1]);
				returnlist.add(map);
			}
		}
		return returnlist;
	}


	public Object insertReportItemRecord(final String id, final String report_id, final String item_name,
			final String item_value, final String info_id, final String user_id, final String user_name) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
			// @Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				session.doWork(new Work() {
					// @Override
					public void execute(Connection conn) throws SQLException {
						String sql = "insert into tzsb_report_item_record (id,fk_report_id,item_name,item_value,item_type,"
								+ "fk_inspection_info_id,last_mdy_uid,last_mdy_uname,last_mdy_time,data_status) " 
								+ " values(?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement statement = conn.prepareStatement(sql);
						statement.setString(1, id); // id
						statement.setString(2, report_id); // 报告类型ID
						statement.setString(3, item_name); // 项目数据参数
						statement.setString(4, item_value); // 项目数据值
						statement.setString(5, "string"); // 数据类型
						statement.setString(6, info_id); // 检验业务信息ID
						statement.setString(7, user_id); // 最后修改人ID
						statement.setString(8, user_name); // 最后修改人姓名
						statement.setString(9, DateUtil.getCurrentDateTime()); // 最后修改时间
						statement.setString(10, "0"); // 数据状态，默认0（0：未生成报告
														// 1：已生成报告 99：删除）
						statement.execute();
						statement.close();
					}
				});
				return null;
			}
		});
	}
	
	// 修改原始记录值
	public void updateReportItemRecord(String id, String item_name, String item_value, String user_id,
			String user_name) {
		try {
			if (StringUtil.isNotEmpty(id) && StringUtil.isNotEmpty(item_name) && StringUtil.isNotEmpty(item_value)) {
				this.createSQLQuery("update tzsb_report_item_record set item_value='" + item_value + "',last_mdy_uid='"
						+ user_id + "',last_mdy_uname='" + user_name + "',last_mdy_time='"
						+ DateUtil.getCurrentDateTime() + "' where id='" + id + "' and item_name='" + item_name + "'")
						.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 移动端获取报告审核人员信息
	 * 
	 * @param org_id
	 *            -- 部门id
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2015-11-25 上午11:22:00
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getShUsersList(String org_id) {
		List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
		String sql = "select t.id, t.pid, t.code, t.text from (select o.id as id,o.id as code,o.ORG_NAME as text,o.PARENT_ID as pid from sys_org o";
		sql += " union select t1.id as id, t1.id as code, t1.NAME as text, t1.ORG_ID as pid from employee e, sys_user t1 where e.id = t1.employee_id";
		sql += " and t1.id in (select s.user_id from sys_user_role s, Sys_Role r where r.id = s.role_id and r.name = '报告审核')) t ";
		if (StringUtil.isNotEmpty(org_id)) {
			sql += " start with t.pid = '" + org_id + "'";
		} else {
			sql += " start with t.id in ('100020','100022','100023','100024','100026','100034','100035','100036','100037','100033')";
		}
		sql += " connect by t.pid = prior t.id";

		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", obj[0]);
				map.put("pid", obj[1]);
				map.put("user_id", obj[2]);
				map.put("user_name", obj[3]);
				returnlist.add(map);
			}
		}
		return returnlist;
	}

	/**
	 * 移动端获取报告签发人员信息
	 * 
	 * @param org_id
	 *            -- 部门id
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2015-11-25 上午11:28:00
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getQfUsersList(String org_id) {
		List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
		String sql = "select t.id, t.pid, t.code, t.text from (select o.id as id,o.id as code,o.ORG_NAME as text,o.PARENT_ID as pid from sys_org o";
		sql += " union select t1.id as id, t1.id as code, t1.NAME as text, t1.ORG_ID as pid from employee e, sys_user t1 where e.id = t1.employee_id";
		sql += " and t1.id in (select s.user_id from sys_user_role s, Sys_Role r where r.id = s.role_id and r.name = '报告签发')) t ";
		if (StringUtil.isNotEmpty(org_id)) {
			sql += " start with t.pid = '" + org_id + "'";
		} else {
			sql += " start with t.id in ('100020','100022','100023','100024','100026','100034','100035','100036','100037','100033')";
		}
		sql += " connect by t.pid = prior t.id";

		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", obj[0]);
				map.put("pid", obj[1]);
				map.put("user_id", obj[2]);
				map.put("user_name", obj[3]);
				returnlist.add(map);
			}
		}
		return returnlist;
	}

	/**
	 * 移动端获取人员角色信息
	 * 
	 * @param user_id
	 *            -- 人员id
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2015-11-25 下午15:19:00
	 */
	@SuppressWarnings("unchecked")
	public String getUserRoles(String user_id) {
		String returnStr = "";
		String sql = "select distinct r.*,ur.user_id from sys_role r, sys_user_role ur where r.id = ur.role_id";
		if (StringUtil.isNotEmpty(user_id)) {
			sql += " and ur.user_id = '" + user_id + "'";
		}
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				if (returnStr.length() > 0) {
					returnStr += ",";
				}
				returnStr += obj[1];
			}
		}
		return returnStr;
	}


	/**
	 * 移动端根据设备ID获取设备信息
	 * 
	 * @param device_id
	 *            -- 设备id
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2015-11-25 上午11:22:00
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDevices(String device_id) {
		List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
		try {
			String[] devices = device_id.split(","); // 设备id
			for (int i = 0; i < devices.length; i++) {
				String hql = "from DeviceDocument d where d.id=?";
				DeviceDocument device = (DeviceDocument) this.createQuery(hql, devices[i]).uniqueResult();
				if (device != null) {
					EnterInfo enterInfo = device.getEnterInfo();
					if (enterInfo == null) {
						if (StringUtil.isNotEmpty(device.getFk_company_info_use_id())) {
							enterInfo = enterDao.get(device.getFk_company_info_use_id());
							device.setEnterInfo(enterInfo);
						}
					}
					Collection<ElevatorPara> elevatorParas = device.getElevatorParas();
					if (elevatorParas == null) {
						elevatorParas = elevatorParaDao.queryParas(device_id);
						device.setElevatorParas(elevatorParas);
					}
					Map<String, Object> map = TS_Util.convertBeanToMap2(device);
					returnlist.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnlist;
	}

	/**
	 * 移动端根据设备车牌号获取设备信息（罐车）
	 * 
	 * @param internal_num
	 *            -- 车牌号（单位内编号）
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2018-04-03 上午10:37:00
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getDeviceByInternal_num(String internal_num) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean hasResult = true;
		String device_id = "";
		try {
			String sql = "select d.id from base_device_document d where d.internal_num=? "
					+ "and d.device_status!='99'  and SUBSTR(d.device_sort,0,1)='2' order by d.inspect_next_date desc";
			List<String> list = this.createSQLQuery(sql, internal_num.trim()).list();
			if (list.isEmpty()) {
				hasResult = false;
			} else {
				// 结果集为多条记录时，返回下次检验日期最新的一个
				device_id = list.get(0) != null ? String.valueOf(list.get(0)) : "";
				if (StringUtil.isEmpty(device_id)) {
					hasResult = false;
				}
			}

			if (hasResult) {
				map.put("success", true);
				map.put("device_id", device_id);
				map.put("msg", "查询到设备（ID：" + device_id + "）");
			} else {
				map.put("success", false);
				map.put("device_id", "");
				map.put("device_status", "0");
				map.put("msg", "亲，系统未查询到" + internal_num + "该车牌号的相关设备信息！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 移动端根据业务ID获取设备信息
	 * 
	 * @param info_id
	 * 
	 * @return
	 * @author X
	 * @date 2015-12-22 上午15:22:00
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDevicesByInfoId(String info_id) {
		List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
		try {
			String[] infoids = info_id.split(","); // 业务id
			for (int i = 0; i < infoids.length; i++) {
				InspectionInfo info = infoDao.get(infoids[i]);

				info.setIs_plan("2");// 修改已领取状态

				String deviceId = info.getFk_tsjc_device_document_id();// 设备ID
				DeviceDocument device = deviceDao.get(deviceId);

				if (device != null) {
					EnterInfo enterInfo = device.getEnterInfo();
					if (enterInfo == null) {
						if (StringUtil.isNotEmpty(device.getFk_company_info_use_id())) {
							enterInfo = enterDao.get(device.getFk_company_info_use_id());
							device.setEnterInfo(enterInfo);
						}
					}
					Collection<PressurevesselsPara> pressurevesselsPara = device.getPressurevessels();
					if (pressurevesselsPara == null) {
						pressurevesselsPara = pressurevesselsParaDao.queryParas(deviceId);
						device.setPressurevessels(pressurevesselsPara);
					}
					Map<String, Object> map = TS_Util.convertBeanToMap2(device);
					returnlist.add(map);
				}

				infoDao.save(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnlist;
	}

	/**
	 * 移动端根据单位ID和单位名称获取设备信息
	 * 
	 * @param com_id
	 *            -- 单位id
	 * @param com_name
	 *            -- 单位名称
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2015-12-22 下午14:05:00
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDeviceList(String com_id, String com_name) {
		List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
		try {
			if (StringUtil.isNotEmpty(com_id)) {
				EnterInfo enterInfo = enterDao.get(com_id);
				List<DeviceDocument> list = deviceDao.queryDtDevicesByComId(com_id);
				List<DeviceDocument> deviceList = new ArrayList<DeviceDocument>();
				for (DeviceDocument deviceDocument : list) {
					List<ReportHallInfo> reportHallList = reportHallInfoDao.getInfos(deviceDocument.getId());
					if (!reportHallList.isEmpty()) {
						ReportHallInfo reportHallInfo = reportHallList.get(0);
						deviceDocument.setReceive_user_id(reportHallInfo.getReceive_user_id());
						deviceDocument.setReceive_user_name(reportHallInfo.getReceive_user_name());
						deviceDocument.setReceive_time(reportHallInfo.getReceive_time());
					}
					deviceList.add(deviceDocument);
				}
				enterInfo.setDeviceList(deviceList);
				Map<String, Object> map = TS_Util.beanToMap(enterInfo);
				returnlist.add(map);
			} else {
				if (StringUtil.isNotEmpty(com_name)) {
					EnterInfo eInfo = null;
					List<DeviceDocument> deviceList = new ArrayList<DeviceDocument>();
					List<EnterInfo> enterInfos = enterDao.queryInfos(com_name);
					for (EnterInfo enterInfo : enterInfos) {
						if (eInfo == null) {
							eInfo = enterInfo;
						}
						List<DeviceDocument> list = deviceDao.queryDtDevicesByComId(enterInfo.getId());
						for (DeviceDocument deviceDocument : list) {
							List<ReportHallInfo> reportHallList = reportHallInfoDao.getInfos(deviceDocument.getId());
							if (!reportHallList.isEmpty()) {
								ReportHallInfo reportHallInfo = reportHallList.get(0);
								deviceDocument.setReceive_user_id(reportHallInfo.getReceive_user_id());
								deviceDocument.setReceive_user_name(reportHallInfo.getReceive_user_name());
								deviceDocument.setReceive_time(reportHallInfo.getReceive_time());
							}
							// deviceDocument.setEnterInfo(enterInfo);
							deviceList.add(deviceDocument);
						}
					}
					eInfo.setDeviceList(deviceList);
					Map<String, Object> map = TS_Util.convertBeanToMap2(eInfo);
					returnlist.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnlist;
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryRecordInfos(String infoIds) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		infoIds = IdFormat.formatIdStr(infoIds);
		String sql = "select distinct info.id,info.report_type,reports.report_name,nvl(reports.print_ysjl_copies,1) as printcopies, "
				+ " device.id as device_id,device.device_name, device.device_registration_code, reports.ysjl_name, info.report_sn"
				+ " from tzsb_inspection_info info, base_reports reports, base_device_document device "
				+ " where info.id in ( " + infoIds
				+ " ) and info.report_type = reports.id" + " and info.fk_tsjc_device_document_id = device.id";
		sql += " order by info.report_sn asc ";
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", obj[0]);
				map.put("report_id", obj[1]);
				map.put("report_name", obj[2]);
				map.put("printcopies", obj[3]);
				map.put("device_id", obj[4]);
				map.put("device_name", obj[5]);
				map.put("device_registration_code", obj[6]);
				map.put("ysjl_name", obj[7]);
				map.put("report_sn", obj[8]);
				map.put("printpages", "");
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryGcRecordInfos(String infoIds) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		infoIds = IdFormat.formatIdStr(infoIds);
		String sql = "select distinct info.id,info.report_type,reports.report_name,nvl(reports.print_ysjl_copies,1) as printcopies, "
				+ " device.id as device_id,device.device_name, device.internal_num, reports.ysjl_name, info.report_sn"
				+ " from tzsb_inspection_info info, base_reports reports, "
				+ " base_device_document  device where info.id in ( " + infoIds
				+ " ) and info.report_type = reports.id" + " and info.fk_tsjc_device_document_id = device.id";
		sql += " order by info.report_sn asc ";
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", obj[0]);
				map.put("report_id", obj[1]);
				map.put("report_name", obj[2]);
				map.put("printcopies", obj[3]);
				map.put("device_id", obj[4]);
				map.put("device_name", obj[5]);
				map.put("internal_num", obj[6]);
				map.put("ysjl_name", obj[7]);
				map.put("report_sn", obj[8]);
				map.put("printpages", "");
				mapList.add(map);
			}
		}
		return mapList;
	}

	//大厅任务数量
	public int getHallMessionCount(String userOrgId){
		String sql = "with done as (select i.fk_hall_para_id ,count(i1.id) nums from  TZSB_INSPECTION i , TZSB_INSPECTION_INFO i1 "+
						"where i.id = i1.fk_inspection_id and i.fk_hall_para_id is not null "+
						"group by i.fk_hall_para_id)  "+
						"select count(1) "+
						 " from tzsb_inspection_hall t, tzsb_inspection_hall_para t1 ,done t2 "+
						" where t.id = t1.fk_hall_id and t1.id = t2.fk_hall_para_id(+) "+
						 "  and t.flow_status = '2' "+
						 "  and t.data_status = '1' "+
						 "  and t1.is_rec='1' "+
						 "  and t1.unit_code='"+userOrgId+"' "+
						 "  and t1.is_plan='1'";
		List<Object> list = this.createSQLQuery(sql).list();
		if(list.size()>0&&list.get(0)!=null) {
			return Integer.parseInt(list.get(0).toString());
		}
		return 0;
	}
	

	//签发
	public int getReportSignCount(String userId) {
		String sql = "select count(*) from V_WORK_INFO_LIST_ALL t where t.reportType = '2' and t.HANDLER_ID = '"+userId+"' and t.state = '300' and t.flow_note_name = '报告签发'" ;
		Object obj = this.createSQLQuery(sql).uniqueResult();
		if(obj != null) {
			return Integer.parseInt(obj.toString());
		}
		return 0;
	}
	

	//报告审核
	public int getReportAuditCount(String userId) {
		String sql = "select count(*) from V_WORK_INFO_LIST_ALL t where t.reportType = '2' and t.HANDLER_ID = '"+userId+"' and t.state = '300' and t.flow_note_name = '报告审核'";
		Object obj = this.createSQLQuery(sql).uniqueResult();
		if(obj != null) {
			return Integer.parseInt(obj.toString());
		}
		return 0;
	}

	//原始记录待审核
		public int getRecordConfirmCount(String employeeId) {
			String needReceiveSql = "select count(1)\n" +
									"  from TZSB_INSPECTION_INFO t\n" + 
									" where t.record_handle_id = ? \n" + 
									" and t.record_flow = '1'";
			Object obj = this.createSQLQuery(needReceiveSql,employeeId).uniqueResult();
			if(null != obj) {
				return Integer.parseInt(obj.toString());
			}
			
			return 0;
		}
		//退回待接收
		public int getBackReceiveCount(String employeeId) {
			String needReceiveSql = "select count(1)\n" +
									"  from TZSB_INSPECTION_INFO t\n" + 
									" where t.record_handle_id = ? \n" + 
									"   and t.record_flow = '9'\n" + 
									"   and (t.receive_status is null or t.receive_status = '0')";

			Object obj = this.createSQLQuery(needReceiveSql,employeeId).uniqueResult();
			if(null != obj) {
				return Integer.parseInt(obj.toString());
			}
			return 0;
		}
		//科室报检待接收
		public int getUnitReceiveCount(String userId) {
			String needReceiveSql = "select count(1) "+
									  "from tzsb_inspection a, tzsb_inspection_info b, base_device_document c "+
									 "where a.id = b.fk_inspection_id "+
									   "and b.fk_tsjc_device_document_id = c.id "+
									   "and b.data_status <> '99' "+
									   "and b.receive_status = '0'  "+
									   "and b.record_flow is null "+
									   "and (b.item_op_id='"+userId+"' or b.check_op_id like '%"+userId+"%')";
			List<Object> list = this.createSQLQuery(needReceiveSql).list();
			if(list.size()>0) {
				return Integer.parseInt(list.get(0).toString());
			}
			return 0;
		}
		//转移录入
		public int getOtherReceiveCount(String employeeId) {
			String sql = "select count(1)\n" +
						"  from TZSB_INSPECTION_INFO t\n" + 
						" where t.record_handle_id = ? \n" + 
						"   and t.record_flow = '10'\n" + 
						"   and (t.receive_status is null or t.receive_status = '0')";

			Object obj = this.createSQLQuery(sql,employeeId).uniqueResult();
			if(null != obj) {
				return Integer.parseInt(obj.toString());
			}
			
			return 0;
		}
		
		//以下是移动端的代码
		
		/**
		 * 根据部门ID获取检验部门人员信息
		 * 
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> queryUsersByOrgId(String org_id) {
			List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
			String sql = "select u.id,u.name,o.id org_id,o.org_name,e.id employee_id,e.name employee_name from SYS_USER u, sys_org o, employee e "
							+ "where u.org_id = o.id and u.employee_id = e.id and u.status='1' "
							+ "and o.status='used' and o.property='dep'";
			if(org_id!=null&&StringUtil.isNotEmpty(org_id)) {
				sql += " and o.id = '"+org_id+"'";
			}
			
			List<Object[]> list = this.createSQLQuery(sql).list();
			Map<String, Object> map = null;
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Object[] objArr = list.get(i);
					map = new HashMap<String, Object>();
					map.put("id", objArr[0]);
					map.put("name", objArr[1]);
					map.put("org_id", objArr[2]);
					map.put("org_name", objArr[3]);
					map.put("employee_id", objArr[4]);
					map.put("employee_name", objArr[5]);
					returnlist.add(map);
				}
			}
			return returnlist;
		}
		
		//根据检验业务info表ID和baseReport表ID取得原始记录最新数据的list
		public List<ReportItemRecord> getItemRecordListByInfoId(String fk_info_id,String fk_report_id){
			String hql = "from ReportItemRecord where fk_inspection_info_id = ? and fk_report_id = ?  and  data_status = 0 " ;
			return this.createQuery(hql, fk_info_id,fk_report_id).list();
		}
		//所有待接收，弃用
		public int getNeedReceiveCount(String userId) {
			String needReceiveSql = "select  count(1)" + 
					"  from TZSB_INSPECTION_INFO t," + 
					"       base_device_document d," + 
					"       base_reports         r," + 
					"       rt_page              p" + 
					" where  r.record_model_id = p.id" + 
					"   and t.fk_report_id = r.id" + 
					"   and t.fk_tsjc_device_document_id = d.id" + 
					"   and (t.record_handle_id = '"+userId+"'" + 
					" or (t.record_handle_id is null and t.check_op_id like '%"+userId+"%'))"+
					"   and t.record_flow in ('0','9') and  (t.receive_status is null or t.receive_status='0')";
			List<Object> list = this.createSQLQuery(needReceiveSql).list();
			if(list.size()>0) {
				return Integer.parseInt(list.get(0).toString());
			}
			
			return 0;
		}
		
		public List<ReportItemRecord> getRecordByItemName(String fk_info_id, String fk_report_id, String item_name) {
			String hql = "from ReportItemRecord where fk_inspection_info_id = ? and fk_report_id = ? and item_name = ? and  data_status = 0 ";
			return this.createQuery(hql, fk_info_id, fk_report_id, item_name).list();
		}
		

		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> queryCodeTable(String code) {
			List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
			String sql = "select v.id,t.code,t.name codeName,v.value,v.name,v.code_table_values_id from PUB_CODE_TABLE t ,Pub_Code_Table_Values v where t.id = v.code_table_id";
			if(code!=null&&StringUtil.isNotEmpty(code)) {
				sql += " and t.code='"+code+"'";
			}
			
			sql +=  "  order by t.code,v.sort";
			
			List list = this.createSQLQuery(sql).list();
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Object[] objArr = list.toArray();
					Object[] obj = (Object[]) objArr[i];
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", obj[0]);
					map.put("code", obj[1]);
					map.put("codeName", obj[2]);
					map.put("value", obj[3]);
					map.put("name", obj[4]);
					map.put("pid", obj[5]);
					returnlist.add(map);
				}
			}
			return returnlist;
		}

		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> queryCodeTableByIds(String codes) {
			
			List<Map<String, Object>> returnlist = new ArrayList<Map<String, Object>>();
			String sql = "select v.id,t.code,t.name codeName,v.value,v.name,v.code_table_values_id from PUB_CODE_TABLE t ,Pub_Code_Table_Values v where t.id = v.code_table_id";
			sql += " and t.code in ('"+codes+"')";
			
			sql +=  "  order by t.code,v.sort";
			
			List list = this.createSQLQuery(sql).list();
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Object[] objArr = list.toArray();
					Object[] obj = (Object[]) objArr[i];
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", obj[0]);
					map.put("code", obj[1]);
					map.put("codeName", obj[2]);
					map.put("value", obj[3]);
					map.put("name", obj[4]);
					map.put("pid", obj[5]);
					returnlist.add(map);
				}
			}
			return returnlist;
		}
		
		public List<String> queryCodeTableUpdate(String codeupdateTime) {
			String sql = "select c.code  from PUB_CODE_TABLE_UPDATE t,pub_code_table c "
					+ " where c.id =t.code_table_id and  to_char(t.update_date,'yyyy-MM-dd HH24:mm:ss') >='"+codeupdateTime+"'";
			List<String> list = this.createSQLQuery(sql).list();
			return list;
		}
}
