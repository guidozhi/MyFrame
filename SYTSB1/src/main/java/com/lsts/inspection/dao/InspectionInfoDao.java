package com.lsts.inspection.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.khnt.core.crud.dao.impl.EntityDaoImpl;
import com.khnt.utils.StringUtil;
import com.lsts.IdFormat;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.report.bean.ReportPrint;
import com.lsts.report.bean.ReportPrintRecord;
import com.lsts.webservice.cxf.server.report.ReportDTO;

/**
 * 业务洗洗脑管理 dao
 * 
 * @author 肖慈边 2014-2-17
 */

@Repository("inspectionInfoDao")
public class InspectionInfoDao extends EntityDaoImpl<InspectionInfo> {

	@SuppressWarnings("unchecked")
	public InspectionInfo getInspectionInfo(String id) {
		String hql = "from InspectionInfo i where i.id=?";
		List list = this.createQuery(hql, id).list();
		if (!list.isEmpty()) {
			return (InspectionInfo) list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<InspectionInfo> getInspectionInfos(String inspection_id) {
		String hql = "from InspectionInfo i where i.inspection.id=?";
		List list = this.createQuery(hql, inspection_id).list();
		if (!list.isEmpty()) {
			return list;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<InspectionInfo> getInfos(
			String info_ids) {
		List<InspectionInfo> list = new ArrayList<InspectionInfo>();
		info_ids = IdFormat.formatIdStr(info_ids);
		String hql = "from InspectionInfo i where i.id in ("+info_ids+") and i.data_status != '99'";
		list = this.createQuery(hql).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<InspectionInfo> getInfoByRandomcode(
			String random_code) {
		List<InspectionInfo> list = new ArrayList<InspectionInfo>();
		String hql = "from InspectionInfo i where i.random_code = ? and i.data_status != '99' order by i.create_time desc";
		list = this.createQuery(hql, random_code).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<InspectionInfo> getInfosByReportSn(String report_sn) {
		String hql = "from InspectionInfo t where t.report_sn=? and t.data_status!='99'";
		List list = this.createQuery(hql, report_sn).list();
		if (!list.isEmpty()) {
			return list;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public InspectionInfo getInfos(String report_sn, String com_name) {
		String hql = "from InspectionInfo t where t.report_sn=? and t.report_com_name=? and t.data_status!='99' ";
		List list = this.createQuery(hql, report_sn, com_name).list();
		if (!list.isEmpty()) {
			return (InspectionInfo)list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public InspectionInfo getInfoByReportSn(String report_sn) {
		String hql = "from InspectionInfo t where t.report_sn=? and t.data_status!='99' ";
		List list = this.createQuery(hql, report_sn).list();
		if (!list.isEmpty()) {
			return (InspectionInfo)list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public InspectionInfo getInfoByRandomCode(String random_code) {
		String hql = "from InspectionInfo t where t.random_code=? and t.data_status!='99' ";
		List list = this.createQuery(hql, random_code).list();
		if (!list.isEmpty()) {
			return (InspectionInfo)list.get(0);
		}
		return null;
	}
	
	// 获取设备最新报告
	@SuppressWarnings("unchecked")
	public List<InspectionInfo> getNewInfoByDeviceId(String device_id, String inspection_id) {
		List<InspectionInfo> list = new ArrayList<InspectionInfo>();
		String hql = "from InspectionInfo i where i.fk_tsjc_device_document_id=? and i.data_status!='99'";
		if(StringUtil.isNotEmpty(inspection_id)){
			hql += " and i.inspection.id != ?";
			list = this.createQuery(hql, device_id, inspection_id).list();
		}else{
			list = this.createQuery(hql, device_id).list();
		}
		
		if (!list.isEmpty()) {
			return list;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInfo(String inspectionInfoIds) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		inspectionInfoIds=IdFormat.formatIdStr(inspectionInfoIds);
		String sql = "select distinct info.id,info.report_type,reports.report_name,com.com_name, "
				+ " info.report_sn,nvl(reports.printcopies,1) as printcopies,info.check_unit_id, "
				+ " reports.is_double_side_printer as double_sider"
				+ " from tzsb_inspection_info info, base_reports reports, base_company_info com, "
				+ " base_device_document  device  where info.id in ("+inspectionInfoIds+") "
				+ " and info.report_type = reports.id "
				+ " and info.fk_tsjc_device_document_id = device.id "
				+ " and device.fk_company_info_use_id = com.id "
				//+ " and info.id = item.fk_inspection_info_id"
				//+ " and info.report_type = item.fk_report_id"
				//+ " and item.item_name = 'REPORT_SN'"
				+ " order by info.id asc ";
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", obj[0]);
				map.put("report_id", obj[1]);
				map.put("report_name", obj[2]);
				map.put("com_name", obj[3]);
				map.put("item_value", obj[4]);
				map.put("printcopies", obj[5]);
				map.put("org_id", obj[6]);
				map.put("double_sider",obj[7]);
				mapList.add(map);
			}
		}
		return mapList;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryPageInfo(String inspectionInfoIds,
			String userId) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		inspectionInfoIds = IdFormat.formatIdStr(inspectionInfoIds);
		String sql = "select　distinct  info.id,info.report_type,reports.report_name,nvl(reports.printcopies,1) as printcopies, "
				+ " device.id as device_id,device.device_name, device.device_registration_code, info.report_sn, "
				+ " to_char(info.advance_time,'yyyy-MM-dd'), to_char(info.examine_date,'yyyy-MM-dd'), to_char(info.enter_time,'yyyy-MM-dd'), "
				+ " info.flow_note_id, v.ACTIVITY_ID "
				+ " from tzsb_inspection_info info, base_reports reports, V_PUB_WORKTASK v, "
				+ " base_device_document  device where info.id in ( "
				+ inspectionInfoIds
				+ " ) and info.report_type = reports.id"
				+ " and info.fk_tsjc_device_document_id = device.id"
				//+ " and device.fk_company_info_use_id = com.id"
				//+ " and info.id = item.fk_inspection_info_id"
				//+ " and info.report_type = item.fk_report_id"
				+ " and info.id = v.SERVICE_ID and v.STATUS = '0'";
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
				//map.put("com_name", obj[3]);
				map.put("printcopies", obj[3]);
				map.put("device_id", obj[4]);
				map.put("device_name", obj[5]);
				map.put("device_registration_code", obj[6]);
				map.put("item_value", obj[7]);
				map.put("advance_time", obj[8]);
				map.put("check_time", obj[9]);
				map.put("enter_time", obj[10]);
				map.put("flow_note_id", obj[11]);
				map.put("activity_id", obj[12]);
				map.put("printpages", "");
				mapList.add(map);
			}
		}
		return mapList;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInfo(String inspectionInfoIds,
			String userId, String print_count) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		inspectionInfoIds = IdFormat.formatIdStr(inspectionInfoIds);
		String sql = "select　distinct  info.id,info.report_type,reports.report_name,nvl(reports.printcopies,1) as printcopies, "
				+ " device.id as device_id,device.device_name, device.device_registration_code, info.report_sn, "
				+ " to_char(info.advance_time,'yyyy-MM-dd'), to_char(info.examine_date,'yyyy-MM-dd'), to_char(info.enter_time,'yyyy-MM-dd'), "
				+ " info.pdf_export_att pdf_att, info.pdf_export_record_att pdf_record_att ";
		if (StringUtil.isNotEmpty(userId)) {
			sql += " , info.flow_note_id, v.ACTIVITY_ID from tzsb_inspection_info info, base_reports reports, V_PUB_WORKTASK v, "
				+ " base_device_document  device where info.id in ( "
				+ inspectionInfoIds + " ) and info.report_type = reports.id"
				+ " and info.fk_tsjc_device_document_id = device.id"
				+ " and info.id = v.SERVICE_ID and v.STATUS = '0' and v.HANDLER_ID = '" + userId + "' ";
		}else{
			sql += " , info.flow_note_id from tzsb_inspection_info info, base_reports reports, base_device_document  device where info.id in ( "
				+ inspectionInfoIds + " ) and info.report_type = reports.id"
				+ " and info.fk_tsjc_device_document_id = device.id";
		}
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
				if(StringUtil.isNotEmpty(print_count)){
					if(!"0".equals(print_count) && !"null".equals(print_count) && !"undefined".equals(print_count)){
						map.put("printcopies", print_count);
					}else{
						map.put("printcopies", obj[3]);
					}
				}else{
					map.put("printcopies", obj[3]);
				}
				map.put("device_id", obj[4]);
				map.put("device_name", obj[5]);
				map.put("device_registration_code", obj[6]);
				map.put("item_value", obj[7]);
				map.put("advance_time", obj[8]);
				map.put("check_time", obj[9]);
				map.put("enter_time", obj[10]);
				map.put("pdf_att", obj[11]);
				map.put("pdf_record_att", obj[12]);
				map.put("flow_note_id", obj[13]);
				if (StringUtil.isNotEmpty(userId)) {
					map.put("activity_id", obj[14]);
				}else{
					map.put("activity_id", "");
				}
				map.put("printpages", "");
				mapList.add(map);
			}
		}
		return mapList;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryInfo(String inspectionInfoIds, String print_count) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		inspectionInfoIds = IdFormat.formatIdStr(inspectionInfoIds);
		String sql = "select distinct info.id,info.report_type,reports.report_name,nvl(reports.printcopies,1) as printcopies, "
				+ " device.id as device_id,device.device_name, device.device_registration_code, info.report_sn, info.pdf_export_att pdf_att, info.pdf_export_record_att pdf_record_att "
				+ " from tzsb_inspection_info info, base_reports reports, "
				+ " base_device_document  device where info.id in ( "
				+ inspectionInfoIds
				+ " ) and info.report_type = reports.id"
				+ " and info.fk_tsjc_device_document_id = device.id";
				//+ " and device.fk_company_info_use_id = com.id"
				//+ " and info.id = item.fk_inspection_info_id"
				//+ " and info.report_type = item.fk_report_id";
				//+ " and item.item_name = 'REPORT_SN'";
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
				//map.put("com_name", obj[3]);			
				if(StringUtil.isNotEmpty(print_count)){
					if(!"0".equals(print_count) && !"null".equals(print_count) && !"undefined".equals(print_count)){
						map.put("printcopies", print_count);
					}else{
						map.put("printcopies", obj[3]);
					}
				}else{
					map.put("printcopies", obj[3]);
				}				
				map.put("device_id", obj[4]);
				map.put("device_name", obj[5]);
				map.put("device_registration_code", obj[6]);
				map.put("item_value", obj[7]);
				map.put("pdf_att", obj[9]);
				map.put("pdf_record_att", obj[10]);
				map.put("printpages", "");
				mapList.add(map);
			}
		}
		return mapList;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryZZJDInfo(String inspectionInfoIds,
			String userId) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		inspectionInfoIds = IdFormat.formatIdStr(inspectionInfoIds);
		String sql = "select distinct info.id,info.report_type,r.report_name, "
				+ " nvl(r.printcopies,1) as printcopies, info.report_sn, "
				+ " to_char(info.advance_time,'yyyy-MM-dd'), to_char(info.examine_date,'yyyy-MM-dd'), "
				+ " to_char(info.enter_time,'yyyy-MM-dd'), info.flow_note_id, v.ACTIVITY_ID "
				+ " from tzsb_inspection_info info, base_reports r, V_PUB_WORKTASK v "
				+ " where info.id in ( "
				+ inspectionInfoIds
				+ " ) and info.report_type = r.id and info.id = v.SERVICE_ID and v.STATUS = '0'";
		if (StringUtil.isNotEmpty(userId)) {
			sql += " and v.HANDLER_ID = '" + userId + "' ";
		}
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
				map.put("report_sn", obj[4]);
				map.put("advance_time", obj[5]);
				map.put("check_time", obj[6]);
				map.put("enter_time", obj[7]);
				map.put("flow_note_id", obj[8]);
				map.put("activity_id", obj[9]);
				map.put("printpages", "");
				mapList.add(map);
			}
		}
		return mapList;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryZZJDInfo(String inspectionInfoIds) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		inspectionInfoIds = IdFormat.formatIdStr(inspectionInfoIds);
		String sql = "select distinct info.id,info.report_type,r.report_name,"
				+ " nvl(r.printcopies,1) as printcopies, info.report_sn "
				+ " from tzsb_inspection_info info, base_reports r "
				+ " where info.id in ( "
				+ inspectionInfoIds
				+ " ) and info.report_type = r.id"
				+ " order by info.report_sn asc ";
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
				map.put("report_sn", obj[4]);
				map.put("printpages", "");
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 查询报告书信息
	 * 
	 * @param device_type --
	 *            设备类别（设备大类）
	 * @return
	 * @author GaoYa
	 * @date 2014-09-16 上午10:20:00
	 */
	@SuppressWarnings("unchecked")
	public List<ReportDTO> queryReports(String device_type) {
		List<ReportDTO> reportList = new ArrayList<ReportDTO>();
		String sql = "select　info.report_com_name,info.report_sn,ins.check_type, "
				+ " info.advance_time,info.flow_note_name "
				+ " from tzsb_inspection_info info, tzsb_inspection ins,"
				+ " base_device_document device where substr(device.device_sort,0,1) = '"
				+ device_type
				+ "'"
				+ " and info.fk_tsjc_device_document_id = device.id(+)"
				+ " and info.fk_inspection_id = ins.id(+) "
				+ " order by info.advance_time desc ";
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				ReportDTO reportDTO = new ReportDTO();
				reportDTO.setCom_name(String.valueOf(obj[0]));
				reportDTO.setReport_sn(String.valueOf(obj[1]));
				reportDTO.setCheck_type(String.valueOf(obj[2]));
				reportDTO.setCheck_date(String.valueOf(obj[3]));
				reportDTO.setCheck_status(String.valueOf(obj[4]));
				reportList.add(reportDTO);
			}
		}
		return reportList;
	}
	
	/**
	 * 查询设备检验日期
	 * 
	 * @param device_id --
	 *            设备ID
	 * @return
	 * @author GaoYa
	 * @date 2015-04-22 上午11:06:00
	 */
	@SuppressWarnings("unchecked")
	public String queryInspection_date(String device_id) {
		String sql = "select to_char(info.advance_time,'yyyy-MM-dd')  "
				+ " from tzsb_inspection_info info"
				+ " where info.fk_tsjc_device_document_id = ? and info.data_status != '99'"
				+ " order by info.advance_time desc ";
		List list = this.createSQLQuery(sql, device_id).list();
		if (!list.isEmpty()) {
			return String.valueOf(list.get(0));
		}
		return "";
	}
	
	/**
	 * 获取报告流程ID
	 * 
	 * @param device_big_class --
	 *            设备类别代码
	 * @param check_type --
	 *            检验类别
	 * @param org_id --
	 *            部门ID
	 * @param report_id --
	 *            报告类型ID
	 * @return
	 * @author GaoYa
	 * @date 2017-07-20 下午15:56:00
	 */
	public String queryFlowId(String device_big_class, String check_type, String org_id, String report_id) {
		String sql = "select t.flow_id from FLOW_SERVICE_CONFIG t,BASE_UNIT_FLOW t1 "
				+ "where t.id=t1.FK_FLOW_ID and t1.DEVICE_TYPE like'" + device_big_class
				+ "%' and t1.CHECK_TYPE =? and t.org_id=? and t1.FK_REPORT_ID=?";
		List list = this.createSQLQuery(sql, device_big_class, check_type, org_id, report_id).list();
		if (!list.isEmpty()) {
			return String.valueOf(list.get(0));
		}
		return "";
	}
	
	/**
	 * 查询报告的检验日期
	 * 
	 * @param ids --
	 *            报告业务ID集合
	 * @return
	 * @author GaoYa
	 * @date 2017-05-20 下午16:14:00
	 */
	public String queryInspectionDate(String ids) {
		String inspection_date = "";
		ids = IdFormat.formatIdStr(ids);
		String sql = "select to_char(info.advance_time,'yyyyMMdd')  "
				+ " from tzsb_inspection_info info"
				+ " where info.id in ("+ids+") ";
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				if(StringUtil.isNotEmpty(inspection_date)){
					inspection_date += ",";
				}
				inspection_date += String.valueOf(list.get(i));
			}
		}
		return inspection_date;
	}

	public List<Map<String, Object>> queryMessage(String infoIds) {

		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		infoIds = IdFormat.formatIdStr(infoIds);
		String sql = "select　distinct  info.id,info.report_sn "
				+ " from tzsb_inspection_info info" + "  where info.id in ( "
				+ infoIds + " )";

		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", obj[0]);
				map.put("report_sn", obj[1]);
				map.put("printcopies", "1");
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 获取检验机构核准证号
	 * 
	 * @param advance_time --
	 *            检验日期
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryJGHZH(String advance_time) {
		String jghzh = "";
		String sql = "select to_date('2010-07-30','yyyy-mm-dd') - to_date('"
				+ advance_time + "','yyyy-mm-dd') as flag from dual";
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			Object flag = list.get(0);
			if (StringUtil.isNotEmpty(flag + "")) {
				if (Integer.parseInt(flag + "") <= 0) {
					jghzh = "TS7110306-2019";
				}
			}
		}
		return jghzh;
	}

	/**
	 * 取消收费时，更新报检业务信息收费状态为“待收费”等
	 * 
	 * @param ids --
	 *            报检业务ID
	 * @return
	 */
	public void updateInspectionInfo(String ids) {
		ids = IdFormat.formatIdStr(ids);
		String sql = "update InspectionInfo set fee_status = '1',receivable= null,receivable_remark='',fee_type='',advance_type='',invoice_no='',invoice_date='' where id in ("
				+ ids + ")";
		this.createQuery(sql).executeUpdate();
	}

	/**
	 * 打印标签（合格证）时，标记打印状态为“已打印”
	 * 
	 * @param id --
	 *            报检业务ID
	 * @return
	 */
	public void updateInfo(String id) {
		String sql = "update InspectionInfo set is_print_tags = '1' where id = '"
				+ id + "'";
		this.createQuery(sql).executeUpdate();
	}

	/**
	 * 外借报告时，修改报检业务信息“是否预借报告书”
	 * 
	 * @param id --
	 *            报检业务ID
	 * @return
	 */
	public void updateInspectionInfoIs_borrow(String id, String borrow_date) {
		String sql = "update InspectionInfo set is_borrow = '2',borrow_date=to_date('"
				+ borrow_date + "','yyyy-MM-dd') where id='" + id + "'";
		this.createQuery(sql).executeUpdate();
	}

	/**
	 * 根据报告书编号查询报告是否存在
	 * 
	 * @param report_sn --
	 *            报告书编号
	 * @return
	 * @author GaoYa
	 * @date 2014-07-30 下午15:26:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionInfo> queryByReport_sn(String report_sn) {
		List<InspectionInfo> list = new ArrayList<InspectionInfo>();
		try {
			if (StringUtil.isNotEmpty(report_sn)) {
				String hql = "from InspectionInfo t where t.report_sn = ?";
				list = this.createQuery(hql, report_sn).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据报告书编号查询报告
	 * 
	 * @param report_sn --
	 *            报告书编号
	 * @return
	 * @author GaoYa
	 * @date 2014-09-28 下午15:49:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionInfo> queryInspectionInfo(String report_sn) {
		List<InspectionInfo> list = new ArrayList<InspectionInfo>();
		try {
			if (StringUtil.isNotEmpty(report_sn)) {
				String hql = "from InspectionInfo t where t.report_sn=? and t.data_status!='99'";	
				list = this.createQuery(hql, report_sn).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据设备ID查询报告
	 * 
	 * @param device_id --
	 *            设备ID
	 * @return
	 * @author GaoYa
	 * @date 2015-12-28 下午14:30:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionInfo> queryInfos(String device_id) {
		List<InspectionInfo> list = new ArrayList<InspectionInfo>();
		try {
			if (StringUtil.isNotEmpty(device_id)) {
				String hql = "from InspectionInfo t where t.fk_tsjc_device_document_id=? and  t.data_status!='99'";	
				list = this.createQuery(hql, device_id).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据设备ID查询报告
	 * 
	 * @param device_id --
	 *            设备ID
	 * @return
	 * @author GaoYa
	 * @date 2017-05-10 上午10:51:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionInfo> queryInfosByDeviceId(String device_id, String info_id) {
		List<InspectionInfo> list = new ArrayList<InspectionInfo>();
		try {
			String hql = "from InspectionInfo t where t.data_status!='99'";	
			if (StringUtil.isNotEmpty(device_id)) {
				hql += " and t.fk_tsjc_device_document_id=?";	
			}
			if (StringUtil.isNotEmpty(info_id)) {
				hql += " and t.id!=?";	
			}
			list = this.createQuery(hql, device_id, info_id).list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取所有机电检验部门人员信息
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2015-11-12 下午17:49:00
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryEmployeeList() {
		List<Map<String,Object>> returnlist = new ArrayList<Map<String,Object>>();
		String sql = "select t.id,t.name,t.org_id,o.org_name,u.id user_id from employee t,sys_user u,sys_org o ";
		sql += " where t.ORG_ID=o.id and t.id = u.employee_id";
		sql += " and o.org_code like '%jd%'";
		
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				Map<String,Object> map = new HashMap<String,Object>();
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
	 * 获取报告书编号后五位序号（非西藏、非新疆、非九寨）
	 * 
	 * @param sn_pre --
	 *            报告书编号前缀
	 * @param device_type --
	 *            设备类别
	 * @return
	 * @author GaoYa
	 * @date 2014-03-10 下午03:32:00
	 */
	@SuppressWarnings("unchecked")
	public synchronized String queryNextReportSn5(String sn_pre, String device_type) {
		String nextNum = "";
		String pre_device_type = device_type.substring(0, 1); // 获取设备类别前缀（大类）
		String start_num = "00000";
		if (!"3".equals(pre_device_type)) {
			start_num = "50000";
		}
		String sql = "select nvl(max(Substr(t.report_sn, length('" + sn_pre
				+ "')+1)), '"+start_num+"')" + " from InspectionInfo t"
				+ " where t.report_sn like '" + sn_pre + "%' "
				+ " and t.check_unit_id != '100069' and t.check_unit_id != '100090' and t.check_unit_id != '100091' and t.data_status != '99' ";
		if("3".equals(pre_device_type)) {
			sql += " and t.report_sn not like '"+sn_pre+"8%' and t.report_sn not like '"+sn_pre+"9%' "; // 用以解决非九寨沟部门却生成了九寨沟报告编号的情况（20181127）以及避免出现西藏新疆报告编号类似情况
		}
		List list = this.createQuery(sql).list();
		if (!list.isEmpty()) {
			String curNum = list.get(0) + "";
			if (StringUtil.isNotEmpty(curNum)) {
				nextNum = getCountNum5(Integer.parseInt(curNum) + 1);
			}
		}
		return nextNum;
	}
	
	/**
	 * 获取报告书编号后五位序号（西藏）
	 * 
	 * @param sn_pre --
	 *            报告书编号前缀
	 * @param device_type --
	 *            设备类别
	 * @return
	 * @author GaoYa
	 * @date 2014-03-10 下午03:32:00
	 */
	@SuppressWarnings("unchecked")
	public synchronized String queryXzNextReportSn5(String sn_pre, String device_type) {
		String nextNum = "";
		//String pre_device_type = device_type.substring(0, 1); // 获取设备类别前缀（大类）
		String start_num = "90000";
		String sql = "select nvl(max(Substr(t.report_sn, length('" + sn_pre
				+ "')+1)), '"+start_num+"')" + " from InspectionInfo t"
				+ " where t.report_sn like '" + sn_pre + "%' and t.check_unit_id = '100069' and t.data_status != '99' ";
		
		List list = this.createQuery(sql).list();
		if (!list.isEmpty()) {
			String curNum = list.get(0) + "";
			if (StringUtil.isNotEmpty(curNum)) {
				nextNum = getCountNum5(Integer.parseInt(curNum) + 1);
			}
		}
		return nextNum;
	}
	
	/**
	 * 获取报告书编号后五位序号（新疆）
	 * 
	 * @param sn_pre --
	 *            报告书编号前缀
	 * @param device_type --
	 *            设备类别
	 * @return
	 * @author GaoYa
	 * @date 2017-12-12 上午09:55:00
	 */
	@SuppressWarnings("unchecked")
	public synchronized String queryXjNextReportSn5(String sn_pre, String device_type) {
		String nextNum = "";
		//String pre_device_type = device_type.substring(0, 1); // 获取设备类别前缀（大类）
		String start_num = "80000";
		String sql = "select nvl(max(Substr(t.report_sn, length('" + sn_pre
				+ "')+1)), '"+start_num+"')" + " from InspectionInfo t"
				+ " where t.report_sn like '" + sn_pre + "%' and t.check_unit_id = '100090' and t.data_status != '99' ";
		
		List list = this.createQuery(sql).list();
		if (!list.isEmpty()) {
			String curNum = list.get(0) + "";
			if (StringUtil.isNotEmpty(curNum)) {
				nextNum = getCountNum5(Integer.parseInt(curNum) + 1);
			}
		}
		return nextNum;
	}
	
	/**
	 * 获取报告书编号后五位序号（九寨）
	 * 
	 * @param sn_pre --
	 *            报告书编号前缀
	 * @param device_type --
	 *            设备类别
	 * @return
	 * @author GaoYa
	 * @date 2018-03-29 下午01:02:00
	 */
	@SuppressWarnings("unchecked")
	public synchronized String queryJzNextReportSn5(String sn_pre, String device_type) {
		String nextNum = "";
		//String pre_device_type = device_type.substring(0, 1); // 获取设备类别前缀（大类）
		String start_num = "80000";
		String sql = "select nvl(max(Substr(t.report_sn, length('" + sn_pre
				+ "')+1)), '"+start_num+"')" + " from InspectionInfo t"
				+ " where t.report_sn like '" + sn_pre + "%' and t.check_unit_id = '100091' and t.data_status != '99' ";
		
		List list = this.createQuery(sql).list();
		if (!list.isEmpty()) {
			String curNum = list.get(0) + "";
			if (StringUtil.isNotEmpty(curNum)) {
				nextNum = getCountNum5(Integer.parseInt(curNum) + 1);
			}
		}
		return nextNum;
	}
	
	/**
	 * 获取报告书编号后四位序号
	 * 
	 * @param sn_pre --
	 *            报告书编号前缀
	 * @param device_type --
	 *            设备类别
	 * @return
	 * @author GaoYa
	 * @date 2014-03-10 下午03:32:00
	 */
	@SuppressWarnings("unchecked")
	public synchronized String queryNextReportSn4(String sn_pre, String device_type) {
		String nextNum = "";
		String pre_device_type = device_type.substring(0, 1); // 获取设备类别前缀（大类）
		String start_num = "0000";
		if (!"3".equals(pre_device_type)) {
			start_num = "5000";
		}
		String sql = "select nvl(max(Substr(t.report_sn, length('" + sn_pre
				+ "')+1)), '"+start_num+"') count_num" + " from tzsb_inspection_info t"
				+ " where t.report_sn like '" + sn_pre + "%' and t.data_status != '99' ";
		
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			String curNum = list.get(0) + "";
			if (StringUtil.isNotEmpty(curNum)) {
				nextNum = getCountNum4(Integer.parseInt(curNum) + 1);
			}
		}
		return nextNum;
	}
	
	/**
	 * 获取报告书编号后三位序号
	 * 
	 * @param sn_pre --
	 *            报告书编号前缀
	 * @param device_type --
	 *            设备类别
	 * @return
	 * @author GaoYa
	 * @date 2014-03-10 下午03:32:00
	 */
	@SuppressWarnings("unchecked")
	public synchronized String queryNextReportSn3(String sn_pre, String device_type) {
		String nextNum = "";
		String pre_device_type = device_type.substring(0, 1); // 获取设备类别前缀（大类）
		String start_num = "000";
		if ("1".equals(pre_device_type)) {
			start_num = "600";
		}
		String sql = "select nvl(max(Substr(t.report_sn, length('" + sn_pre
				+ "')+1)), '"+start_num+"')" + " from InspectionInfo t"
				+ " where t.report_sn like '" + sn_pre + "%' and t.data_status != '99' ";
		
		List list = this.createQuery(sql).list();
		if (!list.isEmpty()) {
			String curNum = list.get(0) + "";
			if (StringUtil.isNotEmpty(curNum)) {
				nextNum = getCountNum3(Integer.parseInt(curNum) + 1);
			}
		}
		return nextNum;
	}
	
	/**
	 * 获取报告书编号后三位序号
	 * 
	 * @param sn_pre --
	 *            报告书编号前缀
	 * @param device_type --
	 *            设备类别
	 * @return
	 * @author GaoYa
	 * @date 2016-04-29 上午10:50:00
	 */
	@SuppressWarnings("unchecked")
	public synchronized String queryNextReportSn31(String sn_pre, String device_type) {
		String nextNum = "";
		String pre_device_type = device_type.substring(0, 1); // 获取设备类别前缀（大类）
		String start_num = "000";
		// 2018-01-02锅炉事业部郭霞提出，经质量部卞庭梅确认，锅炉设计文件鉴定报告编号恢复从001开始编号
		/*if ("1".equals(pre_device_type)) {
			start_num = "200";	// 2016-04-18锅炉事业部周黎明要求，经质量部卞姐同意，锅炉设计文件鉴定报告编号从201开始编号
		}*/
		String sql = "select nvl(max(Substr(t.report_sn, length('" + sn_pre
				+ "')+1)), '"+start_num+"')" + " from InspectionInfo t"
				+ " where t.report_sn like '" + sn_pre + "%' and t.data_status != '99' ";
		
		List list = this.createQuery(sql).list();
		if (!list.isEmpty()) {
			String curNum = list.get(0) + "";
			if (StringUtil.isNotEmpty(curNum)) {
				nextNum = getCountNum3(Integer.parseInt(curNum) + 1);
			}
		}
		return nextNum;
	}

	// 生成5位序号
	private synchronized String getCountNum5(int count_num) {
		String nextNum = "";
		if ((0 < count_num) && (count_num < 10))
			nextNum = "0000" + count_num;
		if ((9 < count_num) && (count_num < 100))
			nextNum = "000" + count_num;
		if ((99 < count_num) && (count_num < 1000))
			nextNum = "00" + count_num;
		if ((999 < count_num) && (count_num < 10000))
			nextNum = "0" + count_num;
		else if (count_num > 9999)
			nextNum = String.valueOf(count_num);
		return nextNum;
	}
	
	// 生成4位序号
	private synchronized String getCountNum4(int count_num) {
		String nextNum = "";
		if ((0 < count_num) && (count_num < 10))
			nextNum = "000" + count_num;
		if ((9 < count_num) && (count_num < 100))
			nextNum = "00" + count_num;
		if ((99 < count_num) && (count_num < 1000))
			nextNum = "0" + count_num;
		else if (count_num > 999)
			nextNum = String.valueOf(count_num);
		return nextNum;
	}

	// 生成3位序号
	private synchronized String getCountNum3(int count_num) {
		String nextNum = "";
		if ((0 < count_num) && (count_num < 10))
			nextNum = "00" + count_num;
		if ((9 < count_num) && (count_num < 100))
			nextNum = "0" + count_num;
		else if (count_num > 99)
			nextNum = String.valueOf(count_num);
		return nextNum;
	}
	
	/**
	 * 获取已签发待领取的报告信息(排除已发送过提醒消息的报告信息)
	 * 2018-03-20以前的均已发送过短信提醒，故此处排除
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2018-03-19 下午17:05:00
	 */
	@SuppressWarnings("unchecked")
	public List<String> getGcInfoIds() {
		String sql = " select t.id from tzsb_inspection_info t,base_reports r";
		sql += " where t.data_status <> '99' and t.report_type=r.id and r.issue_msg_type!=0";
		sql += " and t.flow_note_name in ('打印报告','报告领取','报告归档')";
		sql += " and t.ISSUE_DATE2 >= to_date('2018-03-20', 'yyyy-MM-dd')";
		sql += " and t.is_send_draw_msg <> '1'";
		return this.createSQLQuery(sql).list();
	}
	
	/**
	 * 获取业务ID和流程环节名称获取业务流程参数(用于系统自动提交流程使用）
	 * 此方法中的HANDLER_ID均为系统管理员的user_id and 涉及到打印相关角色的role_id
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2018-05-28 下午05:24:00
	 */
	public Map<String, Object> getInfoFlowParams(String info_id, String flow_name) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String sql = "select t.id,v.ACTIVITY_ID,v.FLOW_NUM_ID "
				+ " from tzsb_inspection_info t, V_PUB_WORKTASK v "
				+ " where t.id = v.SERVICE_ID and v.STATUS = '0'and t.flow_note_id = v.flow_num_id and t.data_status <> '99'"
				+ " and (v.HANDLER_ID = '402884c4477c9bac01477fe0d188001b' or v.HANDLER_ID = '4028802b448bbc6401448bdb77f4001a'"
				+ " or v.HANDLER_ID = '402883a057b31d120157b81e39795941' or v.HANDLER_ID = '402880c744be22a10144be51bb9d0013')"
				+ " and t.id= ? and v.ACTIVITY_NAME = ? ";
		List list = this.createSQLQuery(sql, info_id, flow_name).list();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				map.put("info_id", obj[0]);
				map.put("acc_id", obj[1]);
				map.put("flow_num", obj[2]);
				break;
			}
		}
		return map;
	}
	/**
     * 组装保存过的页码
     * author pingZhou
     *
     * @param pages
     * @param page
     * @return
     */
    public String createHasSavePage(String pages, String page) {

        String pageN = pages;
        String pagess = "," + pages + ",";

        String[] pagessN = page.split(",");
        for (int j = 0; j < pagessN.length; j++) {
            if (!pagess.contains("," + pagessN[j] + ",")) {
                pageN = pageN + "," + pagessN[j];
            }
        }

        return pageN;
    }
    /**
     * 判断页面是否保存过
     * author pingZhou
     *
     * @param pages
     * @param page
     * @return
     */
    public boolean checkHasSave(InspectionInfo info, String page) {
    	String sql = "select distinct page_no from TZSB_REPORT_ITEM_VALUE  where fk_inspection_info_id = ? ";
    	@SuppressWarnings("unchecked")
		List<Object> list = this.createSQLQuery(sql, info.getId()).list();
    	if(null != list && list.contains(page)) {
    		return true;
    	}
        return false;
    }
    /**
     * 根据报告id和报告类型获取报告信息
     * 
     * @param id
     * @param reportType 2:新报表
     * @return
     */
    @SuppressWarnings("unchecked")
	public InspectionInfo getInfo(String id, String reportType) {
    	String hql = "select info from InspectionInfo info, Report r where info.report_type = r.id  and info.id = ? and r.reportType = ? ";
    	InspectionInfo info = new InspectionInfo();
    	List<InspectionInfo> list = this.createQuery(hql, id, reportType).list();
    	info = list.get(0);
    	return info;
    }

	public Object getInfoObj(String infoId) {
		String hql = "select info.report_com_name,info.report_sn,info.flow_note_name,info.flow_note_id from InspectionInfo info where info.id = ? ";
    	List<Object> list = this.createQuery(hql,infoId).list();
    	if(list.size()>0) {
    		return list.get(0);
    	}
		return null;
	}
	
	public Object getInfoObj2(String infoId) {
		String hql = "select info.report_com_name,info.report_sn from InspectionInfo info where info.id = ? ";
    	List<Object> list = this.createQuery(hql,infoId).list();
    	if(list.size()>0) {
    		return list.get(0);
    	}
		return null;
	}

	public void saveSubFlag( String infoId,String activityId, String name, String isBack) {
		
		String hql = "update InspectionInfo set flow_note_id = ?,flow_note_name=? ,is_back = ? where id = ? ";
		this.createQuery(hql,activityId,name,isBack,infoId).executeUpdate();
		// TODO Auto-generated method stub
		
	}

	public Object queryInfoForPay(String infoId) {
		String hql = "select info.report_sn,info.advance_fees,info.check_unit_id,o.orgName,info.report_com_name from InspectionInfo info,Org o where info.check_unit_id = o.id and info.id = ? ";
    	List<Object> list = this.createQuery(hql,infoId).list();
    	if(list.size()>0) {
    		return list.get(0);
    	}
		return null;
	}

	public void saveSubEnd(String info_id, String flowId, String flowNum, String flowName) {
		String hql = "update InspectionInfo set flow_note_id = ?,flow_note_num=? ,flow_note_name=? ,report_end_date = ? where id = ? ";
		this.createQuery(hql,flowId,flowNum,flowName,new Date(),info_id).executeUpdate();
		
	}
}
