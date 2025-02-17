package com.lsts.gis.device.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.utils.StringUtil;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.gis.device.dao.GisDeviceDao;
import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;

@Service
public class GisDeviceService extends EntityManageImpl<DeviceDocument, GisDeviceDao> {
	@Autowired
	private GisDeviceDao gisDeviceDao;

	/**
	 * 省院大数据地图 根据设备注册代码查询设备
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List queryDevices(String code,String device_qr_code) throws Exception {
		String sql = "select t.* ,t1.id rid,t1.flow_note_name,t1.ENTER_TIME2,t1.advance_fees,t1.receivable,t1.report_sn,"
					+ "ep.name enter_op_name,ep.mobile_tel enter_op_tel,att.file_path,att.file_name "
					+ "from base_device_document t,TZSB_INSPECTION_INFO t1,employee ep,employee_ext ex,pub_attachment att "
					+ "where t1.FK_TSJC_DEVICE_DOCUMENT_ID = t.id "
					+ "and t1.enter_op_id = ep.id "
					+ "and t1.enter_op_id = ex.fk_emp_id(+) "
					+ "and ex.id = att.business_id(+)  "
					+ "and t.device_status <> '99' "; 
		if(!StringUtil.isEmpty(code)){
			sql+=" and t.device_registration_code = '"+code+"'";
		}
		if(!StringUtil.isEmpty(device_qr_code)){
			sql+=" and t.device_qr_code = '"+device_qr_code+"'";
		}
		sql += " order by t.id,t1.ENTER_TIME2 desc nulls last ";
		List list = gisDeviceDao.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	/**
	 * 公司名称查询
	 * @param company_name
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List queryByCompany(String company_name) throws Exception {
		String sql = "select * from ("+
						"select row_number() over(partition by rid order by op_time desc ) row_num ,v3.* from("+
						"select l.op_action,l.op_user_name,l.op_time,l.op_remark ,v2.* from ("+
						"select row_number() over(partition by id order by ENTER_TIME2 desc nulls last) r_num ,v1.* "+
						"from("+
						  "select i.id rid,i.flow_note_name,(case when i.ENTER_TIME2 is null then i.enter_time else i.ENTER_TIME2 end) ENTER_TIME2,i.advance_fees,i.receivable,i.report_sn,d.*  "+
						  "from base_device_document d,TZSB_INSPECTION_INFO i "+
						  "where i.FK_TSJC_DEVICE_DOCUMENT_ID = d.id "+
						  "and d.device_status <> '99' "+
						  "and i.flow_note_name <> '报告作废' "+
						  "and d.company_name like '%"+company_name+"%' "+
						") v1 ) v2,sys_log l where v2.rid =l.business_id and l.op_action <> '报告作废' and r_num = 1) v3) where row_num = 1"; 
		
		List list = gisDeviceDao.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	/**
	 * 省院大数据地图 刷新最新正在检验的数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public HashMap<String, Object> queryTodayList() throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String sql = "select t.*, t1.ENTER_TIME2,t1.advance_fees,t1.receivable from base_device_document t,TZSB_INSPECTION_INFO t1 "
				+ "where t1.FK_TSJC_DEVICE_DOCUMENT_ID = t.id "
				+ "and t.device_status <> '99' and t1.ENTER_TIME2 >= "
				+ "to_date(to_char(sysdate,'yyyymmdd')||' 00:00:00','yyyymmdd hh24:mi:ss') "
				+ " order by t1.ENTER_TIME2 desc";
		List list = gisDeviceDao.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		map.put("data", list);
		return map;
	}

	/**
	 * 统计总数（new）
	 * @param areaCode
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> initCount() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select count(*) as c from base_device_document t,TZSB_INSPECTION_INFO t1 "
				+ "where t1.FK_TSJC_DEVICE_DOCUMENT_ID = t.id and t.device_status <> '99' ";
		String annualSql = sql
				+ "and t1.ENTER_TIME2 >= to_date(to_char(sysdate,'yyyy')||'0101 00:00:00','yyyymmdd hh24:mi:ss') ";
		String yesterdaySql = sql
				+ "and t1.ENTER_TIME2 >= to_date(to_char(sysdate-1,'yyyymmdd')||' 00:00:00','yyyymmdd hh24:mi:ss') "
				+ "and t1.ENTER_TIME2 < to_date(to_char(sysdate,'yyyymmdd')||' 00:00:00','yyyymmdd hh24:mi:ss') ";
		String todaySql = sql
				+ "and t1.ENTER_TIME2 >= to_date(to_char(sysdate,'yyyymmdd')||' 00:00:00','yyyymmdd hh24:mi:ss') ";
		
		List<Object> annualC = gisDeviceDao.createSQLQuery(annualSql).list();
		List<Object> yesterdayC = gisDeviceDao.createSQLQuery(yesterdaySql).list();
		List<Object> todayC = gisDeviceDao.createSQLQuery(todaySql).list();
		map.put("annual", annualC);
		map.put("yesterday", yesterdayC);
		map.put("today", todayC);

		String sql2 = "select t.*, t1.ENTER_TIME2 from base_device_document t,TZSB_INSPECTION_INFO t1 "
				+ "where t1.FK_TSJC_DEVICE_DOCUMENT_ID = t.id "
				+ "and t1.ENTER_TIME2 >= to_date(to_char(sysdate,'yyyymmdd')||' 00:00:00','yyyymmdd hh24:mi:ss') "
				+ "and t.device_status <> '99' order by t1.ENTER_TIME2 desc";
		List list = gisDeviceDao.createSQLQuery(sql2).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		map.put("todayDevices", list);
		return map;
	}
	/**
	 * 微信总数
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> initWxCount() throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		/*String wxSql = "select count(*) from QUERYMESSAGEINFO q ,base_device_document d "
				+ "where q.querycontent = d.device_registration_code "
				+ "and d.device_status <> '99' ";*/
		String wxSql = "select count(*) from QUERYMESSAGEINFO q ";
		List<Object[]> wxc = gisDeviceDao.createSQLQuery(wxSql).list();
		map.put("wxc", wxc);
		return map;
	}
	

	/**
	 * 缓存各部门设备
	 * @return
	 * @throws Exception
	 */
	
	  @SuppressWarnings("unchecked") 
	  public HashMap<String, Object> rtCache() throws Exception { 
		  HashMap<String, Object> map = new HashMap<String, Object>();
		  List<Object[]> list = new ArrayList<Object[]>();
		  String sql = "select v.*,rownum from ("+
            "select att.file_path,att.file_name,ep.name enter_op_name,ep.mobile_tel enter_op_tel,v2.* "+
            "from ( "+
            "select row_number() over(partition by company_name, device_use_place "+
            "order by ENTER_TIME2 desc nulls last) r_num ,v1.*  "+
            "from ( "+
		            "select i.id rid,i.flow_note_name,i.ENTER_TIME2,i.enter_op_id,i.check_op_name,i.advance_fees,i.receivable,i.report_sn,t.* "+
		            "from base_device_document t, TZSB_INSPECTION_INFO i  "+
		            "where t.id = i.fk_tsjc_device_document_id and t.device_status <> '99' and i.data_status <> '99' "+
		            "and i.check_unit_id = ? and t.device_area_code like '%5101%' "+
            ") v1 "+
            ") v2 left join employee ep on substr(v2.enter_op_id,0,32) = ep.id  "+ 
				  "left join employee_ext ex on substr(v2.enter_op_id,0,32) = ex.fk_emp_id "+
				  "left join pub_attachment att on ex.id = att.business_id "
				  + "where r_num=1 and v2.ENTER_TIME2 is not null order by v2.ENTER_TIME2 desc"+
           ") v where rownum<=100";
				 
		  //机电一部
	  	  list= gisDeviceDao.createSQLQuery(sql,"100020")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	  	  map.put("d1", list); 
	  	  //机电二部
		  list = gisDeviceDao.createSQLQuery(sql,"100021")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d2", list); 
		  //机电三部
		  list = gisDeviceDao.createSQLQuery(sql,"100022")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d3", list); 
		  //机电四部
		  list = gisDeviceDao.createSQLQuery(sql,"100023")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d4", list); 
		  //机电五部
		  list = gisDeviceDao.createSQLQuery(sql,"100063")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d5", list); 
		  
		  //机电六部
		  list = gisDeviceDao.createSQLQuery(sql,"100024")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d6", list);
		  String sql2 = "select v.*,rownum from ("+
		            "select att.file_path,att.file_name,ep.name enter_op_name,ep.mobile_tel enter_op_tel,v2.* "+
		            "from ( "+
		            "select row_number() over(partition by company_name, device_use_place "+
		            "order by ENTER_TIME2 desc nulls last) r_num ,v1.*  "+
		            "from ( "+
				            "select i.id rid,i.flow_note_name,i.ENTER_TIME2,i.enter_op_id,i.check_op_name,t.* "+
				            "from base_device_document t, TZSB_INSPECTION_INFO i  "+
				            "where t.id = i.fk_tsjc_device_document_id and t.device_status <> '99' and i.data_status <> '99' "+
				            "and i.check_unit_id = ? "+
		            ") v1 "+
		            ") v2 left join employee ep on substr(v2.enter_op_id,0,32) = ep.id  "+ 
						  "left join employee_ext ex on substr(v2.enter_op_id,0,32) = ex.fk_emp_id "+
						  "left join pub_attachment att on ex.id = att.business_id "
						  + "where r_num=1 and v2.ENTER_TIME2 is not null order by v2.ENTER_TIME2 desc"+
		           ") v where rownum<=100";
		  //锅炉检验事业部
		  list = gisDeviceDao.createSQLQuery(sql2,"100033")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d7", list); 
		  //容器检验事业部
	  	  list= gisDeviceDao.createSQLQuery(sql2,"100034")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	  	  map.put("d8", list); 
	  	  //产品监督检验事业部
		  list = gisDeviceDao.createSQLQuery(sql2,"100035")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d9", list); 
		  //罐车检验事业部
		  list = gisDeviceDao.createSQLQuery(sql2,"100036")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d10", list); 
		  //管道检验事业部
		  list = gisDeviceDao.createSQLQuery(sql2,"100037")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d11", list); 
		  
		  //石化装置检验事业部
		  list = gisDeviceDao.createSQLQuery(sql2,"100065")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d12", list); 
		  //储气井检验事业部
		  list = gisDeviceDao.createSQLQuery(sql2,"100066")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d13", list); 
		  //无损检验事业部
		  list = gisDeviceDao.createSQLQuery(sql2,"100067")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d14", list); 
		  //援藏特检突击队
		  list = gisDeviceDao.createSQLQuery(sql2,"100069")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d15", list); 
		  //援疆特检突击队
		  list = gisDeviceDao.createSQLQuery(sql2,"100090")
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("d16", list); 
		  map.put("isNew", false);
		  return map; 
	  }
	  
	  	/**
		 * 缓存各部门设备
		 * @return
		 * @throws Exception
		 */
		  @SuppressWarnings("unchecked") 
		  public HashMap<String, Object> rtCache2() throws Exception { 
			  HashMap<String, Object> map = new HashMap<String, Object>();
			  List<Object[]> list = new ArrayList<Object[]>();
			  String sql = "select v.*,rownum from ("+
	            "select att.file_path,att.file_name,ep.name enter_op_name,ep.mobile_tel enter_op_tel,v2.* "+
	            "from ( "+
	            "select row_number() over(partition by company_name, device_use_place "+
	            "order by ENTER_TIME2 desc nulls last) r_num ,v1.*  "+
	            "from ( "+
			            "select i.id rid,i.flow_note_name,i.ENTER_TIME2,i.enter_op_id,i.check_op_name,i.advance_fees,i.receivable,i.report_sn,t.* "+
			            "from base_device_document t, TZSB_INSPECTION_INFO i  "+
			            "where t.id = i.fk_tsjc_device_document_id and t.device_status <> '99' and i.data_status <> '99' "+
			            "and i.check_unit_id = ? and t.device_area_code like '%5101%' "+
	            ") v1 "+
	            ") v2 left join employee ep on substr(v2.enter_op_id,0,32) = ep.id  "+ 
					  "left join employee_ext ex on substr(v2.enter_op_id,0,32) = ex.fk_emp_id "+
					  "left join pub_attachment att on ex.id = att.business_id "
					  + "where r_num=1 and v2.ENTER_TIME2 is not null order by v2.ENTER_TIME2 desc"+
	           ") v where rownum<=100";
					 
			  //机电一部
		  	  list= gisDeviceDao.createSQLQuery(sql,"100020")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  	  map.put("100020", list); 
		  	  //机电二部
			  list = gisDeviceDao.createSQLQuery(sql,"100021")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100021", list); 
			  //机电三部
			  list = gisDeviceDao.createSQLQuery(sql,"100022")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100022", list); 
			  //机电四部
			  list = gisDeviceDao.createSQLQuery(sql,"100023")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100023", list); 
			  //机电五部
			  list = gisDeviceDao.createSQLQuery(sql,"100063")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100063", list); 
			  
			  //机电六部
			  list = gisDeviceDao.createSQLQuery(sql,"100024")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100024", list);
			  String sql2 = "select v.*,rownum from ("+
			            "select att.file_path,att.file_name,ep.name enter_op_name,ep.mobile_tel enter_op_tel,v2.* "+
			            "from ( "+
			            "select row_number() over(partition by company_name, device_use_place "+
			            "order by ENTER_TIME2 desc nulls last) r_num ,v1.*  "+
			            "from ( "+
					            "select i.id rid,i.flow_note_name,i.ENTER_TIME2,i.enter_op_id,i.check_op_name,t.* "+
					            "from base_device_document t, TZSB_INSPECTION_INFO i  "+
					            "where t.id = i.fk_tsjc_device_document_id and t.device_status <> '99' and i.data_status <> '99' "+
					            "and i.check_unit_id = ? "+
			            ") v1 "+
			            ") v2 left join employee ep on substr(v2.enter_op_id,0,32) = ep.id  "+ 
							  "left join employee_ext ex on substr(v2.enter_op_id,0,32) = ex.fk_emp_id "+
							  "left join pub_attachment att on ex.id = att.business_id "
							  + "where r_num=1 and v2.ENTER_TIME2 is not null order by v2.ENTER_TIME2 desc"+
			           ") v where rownum<=100";
			  //锅炉检验事业部
			  list = gisDeviceDao.createSQLQuery(sql2,"100033")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100033", list); 
			  //容器检验事业部
		  	  list= gisDeviceDao.createSQLQuery(sql2,"100034")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  	  map.put("100034", list); 
		  	  //产品监督检验事业部
			  list = gisDeviceDao.createSQLQuery(sql2,"100035")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100035", list); 
			  //罐车检验事业部
			  list = gisDeviceDao.createSQLQuery(sql2,"100036")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100036", list); 
			  //管道检验事业部
			  list = gisDeviceDao.createSQLQuery(sql2,"100037")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100037", list); 
			  
			  //石化装置检验事业部
			  list = gisDeviceDao.createSQLQuery(sql2,"100065")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100065", list); 
			  //储气井检验事业部
			  list = gisDeviceDao.createSQLQuery(sql2,"100066")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100066", list); 
			  //无损检验事业部
			  list = gisDeviceDao.createSQLQuery(sql2,"100067")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100067", list); 
			  //援藏特检突击队
			  list = gisDeviceDao.createSQLQuery(sql2,"100069")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100069", list); 
			  //援疆特检突击队
			  list = gisDeviceDao.createSQLQuery(sql2,"100090")
					  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			  map.put("100090", list); 
			  return map; 
		  }
	  @SuppressWarnings("unchecked") 
	  public HashMap<String, Object> rtQuery(int minute) throws Exception { 
		  HashMap<String, Object> map = new HashMap<String, Object>();
		  String sql = " select att.file_path,att.file_name,v.* from ("
				  			+ "select i.id rid,i.flow_note_name,i.ENTER_TIME2,i.enter_op_id,i.check_op_name,i.advance_fees,i.receivable,i.report_sn,"
				  			+ "ep.name enter_op_name,ep.mobile_tel enter_op_tel,t.* "
				  			+ "from base_device_document t,TZSB_INSPECTION_INFO i,employee ep "
				  			+ "where t.id=i.fk_tsjc_device_document_id and substr(i.enter_op_id,0,32) = ep.id "
				  			+ "and t.device_status <> '99' "//and t.device_sort like '3%'
				  			+ "and i.ENTER_TIME2 > sysdate-1/24/60*"+minute+") v "
				  		+ "left join employee_ext ex on substr(v.enter_op_id,0,32) = ex.fk_emp_id "
				  		+ "left join pub_attachment att on ex.id = att.business_id "; 
		  String sql2 = "select t.company_name,t.device_name,count(t.id) as numb "
				  		+ "from base_device_document t,TZSB_INSPECTION_INFO i "
				  		+ "where t.id = i.fk_tsjc_device_document_id "
				  		+ "and t.device_status <> '99' "
				  		+ "and i.ENTER_TIME2 > sysdate - 1 / 24 / 60 * "+minute 
				  		+ "group by t.company_name,t.device_name "; 
		  List<Object[]> list = gisDeviceDao.createSQLQuery(sql)
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list(); 
		  
		  List<Object[]> list2 = gisDeviceDao.createSQLQuery(sql2)
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list(); 
		  
		  map.put("isNew", true);
		  map.put("data", list);
		  map.put("data2", list2);
		  return map; 
	  }
	/**
	   * 缓存各部门设备(微信查询)
	   * @return
	   * @throws Exception
	   */
	  
	  @SuppressWarnings("unchecked") 
	  public HashMap<String, Object> wxCache() throws Exception { 
		  HashMap<String, Object> map = new HashMap<String, Object>();
		  List<Object[]> list = new ArrayList<Object[]>();
		  String sql = 
				  "select * from (select row_number() over(partition by q_id order by ENTER_TIME2 desc) r_num,v1.* "+
                  "from((select t1.id q_id,t1.username,t1.querytime, t1.querycontent,i.id rid,i.ENTER_TIME2,"
                  + "ep.name enter_op_name,ep.mobile_tel enter_op_tel, t.* "
                  + "from base_device_document t,QUERYMESSAGEINFO t1 ,TZSB_INSPECTION_INFO i,employee ep "
                  + "where t1.querycontent = t.device_registration_code "
                  + "and t.id=i.fk_tsjc_device_document_id "
                  + "and i.enter_op_id = ep.id "
                  + "and t1.querytime > to_date(to_char(sysdate,'yyyymmdd')||' 00:00:00','yyyymmdd hh24:mi:ss') "
                  + "and t.device_status <> '99' "
                  + "and i.data_status <> '99' "
                  + "and t.device_use_place is not null) "
                  + "union "
                  + "(select t1.id q_id,t1.username,t1.querytime, t1.querycontent,i.id rid,i.ENTER_TIME2,"
                  + "ep.name enter_op_name,ep.mobile_tel enter_op_tel, t.* "
                  + "from base_device_document t,QUERYMESSAGEINFO t1 ,TZSB_INSPECTION_INFO i,employee ep "
                  + "where i.report_com_name like '%'||t1.querycontent||'%' "
                  + "and t.id=i.fk_tsjc_device_document_id "
                  + "and i.enter_op_id = ep.id "
                  + "and t1.querytime > to_date(to_char(sysdate,'yyyymmdd')||' 00:00:00','yyyymmdd hh24:mi:ss') "
                  + "and t.device_status <> '99' "
                  + "and i.data_status <> '99' "
                  + "and t.device_use_place is not null)) v1 "+
                  ")v2 where r_num = 1";
		  list = gisDeviceDao.createSQLQuery(sql)
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("data", list);
		  return map; 
	  }
	  /**
	 * 省院大数据地图 通过微信查询了显示在地图上
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	
	  @SuppressWarnings("unchecked") 
	  public HashMap<String,Object> xwQuery() throws Exception { 
		  HashMap<String,Object> map = new HashMap<String,Object>();
		  String sql = "select * from (select row_number() over(partition by q_id order by ENTER_TIME2 desc) r_num,v1.* "+
			          "from((select t1.id q_id,t1.username,t1.querytime,t1.querycontent,i.id rid,i.ENTER_TIME2,i.check_op_name,"
			          + "ep.name enter_op_name,ep.mobile_tel enter_op_tel, t.* "
			          + "from base_device_document t,QUERYMESSAGEINFO t1 ,TZSB_INSPECTION_INFO i,employee ep "
			          + "where t1.querycontent = t.device_registration_code "
			          + "and t.id=i.fk_tsjc_device_document_id "
			          + "and substr(i.enter_op_id,0,32) = ep.id "
			          + "and t1.querytime > (sysdate-1/24/60*3)  "
			          + "and t.device_status <> '99' "
			          + "and i.data_status <> '99' "
			          + "and t.device_use_place is not null) "
			          + "union "
			          + "(select t1.id q_id,t1.username,t1.querytime,t1.querycontent,i.id rid,i.ENTER_TIME2,i.check_op_name,"
			          + "ep.name enter_op_name,ep.mobile_tel enter_op_tel, t.* "
			          + "from base_device_document t,QUERYMESSAGEINFO t1 ,TZSB_INSPECTION_INFO i,employee ep "
			          + "where i.report_com_name like '%'||t1.querycontent||'%' "
			          + "and t.id=i.fk_tsjc_device_document_id "
			          + "and substr(i.enter_op_id,0,32) = ep.id "
			          + "and t1.querytime > (sysdate-1/24/60*3)"
			          + "and t.device_status <> '99' "
			          + "and i.data_status <> '99' "
			          + "and t.device_use_place is not null)) v1 "+
			          ")v2 where r_num = 1";
		  List<Object[]> list = gisDeviceDao.createSQLQuery(sql)
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		  map.put("data", list);
		  System.out.println("微信查询到"+list.size()+"条数据");
		  return map; 
	  }

	//批量展示信息
	  public List<Object[]> queryLastFlow(String ids) throws Exception {
		  ids = ids.replaceAll(",", "','");
		  String flow = "select d.device_registration_code,d.device_qr_code,l.op_action,l.op_user_name,l.op_time,l.op_remark "
		  		+ "from sys_log l,TZSB_INSPECTION_INFO i,base_device_document d "
		  		+ "where l.business_id=i.id and i.fk_tsjc_device_document_id = d.id and i.id in ('"+ids+"') "
		  		+ "and l.op_time = (select max(op_time) from Sys_Log where business_id = i.id)";
		  @SuppressWarnings("unchecked")
		  List<Object[]> list = gisDeviceDao.createSQLQuery(flow).list();
		  return list;
	  }
	public List<Object> queryFlow(String id) throws Exception {
		String flow = "select t2.* from TZSB_INSPECTION_INFO t,FLOW_PROCESS t1, FLOW_ACTIVITY t2 "
				+ "WHERE t.ID = t1.BUSINESS_ID AND t1.ID = t2.PROCESS AND t.id = ?";
		@SuppressWarnings("unchecked")
		List<Object> process = gisDeviceDao.createSQLQuery(flow, id)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return process;
	}

	public void writePointToDocument(String id, String longitude, String latitude) throws Exception{
		String sql = "update base_device_document set longitude=?, latitude=? where id = ?";
		gisDeviceDao.createSQLQuery(sql,longitude,latitude, id).executeUpdate();
	}
	public List queryByType(String type, String content) {
		String sql = "select t.* ,t1.id rid,t1.flow_note_name,t1.ENTER_TIME2,t1.advance_fees,t1.receivable,t1.report_sn,"
				+ "ep.name enter_op_name,ep.mobile_tel enter_op_tel,att.file_path,att.file_name "
				+ "from base_device_document t,TZSB_INSPECTION_INFO t1,employee ep,employee_ext ex,pub_attachment att "
				+ "where t1.FK_TSJC_DEVICE_DOCUMENT_ID = t.id "
				+ "and t1.enter_op_id = ep.id "
				+ "and t1.enter_op_id = ex.fk_emp_id(+) "
				+ "and ex.id = att.business_id(+)  "
				+ "and t.device_status <> '99' "; 
		if("1".equals(type)){
			sql+=" and t.device_registration_code = '"+content+"' order by t.id,t1.ENTER_TIME2 desc nulls last ";
		}else if("2".equals(type)){
			sql+=" and t.device_qr_code = '"+content+"' order by t.id,t1.ENTER_TIME2 desc nulls last ";
		}else if("3".equals(type)){
			sql = "select * from ("+
					"select row_number() over(partition by rid order by op_time desc ) row_num ,v3.* from("+
					"select l.op_action,l.op_user_name,l.op_time,l.op_remark ,v2.* from ("+
					"select row_number() over(partition by id order by ENTER_TIME2 desc nulls last) r_num ,v1.* "+
					"from("+
					  "select i.id rid,i.flow_note_name,(case when i.ENTER_TIME2 is null then i.enter_time else i.ENTER_TIME2 end) ENTER_TIME2,i.advance_fees,i.receivable,i.report_sn,d.*  "+
					  "from base_device_document d,TZSB_INSPECTION_INFO i "+
					  "where i.FK_TSJC_DEVICE_DOCUMENT_ID = d.id "+
					  "and d.device_status <> '99' "+
					  "and i.flow_note_name <> '报告作废' "+
					  "and d.company_name like '%"+content+"%' "+
					") v1 ) v2,sys_log l where v2.rid =l.business_id and l.op_action <> '报告作废' and r_num = 1) v3) where row_num = 1"; 
	
		}
		return gisDeviceDao.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	public List queryLastRt() {
		String sql = " select att.file_path,att.file_name,v.* from ("
	  			+ "select i.id rid,i.flow_note_name,i.ENTER_TIME2,i.enter_op_id,i.check_op_name,i.advance_fees,i.receivable,i.report_sn,"
	  			+ "ep.name enter_op_name,ep.mobile_tel enter_op_tel,t.*,row_number() over(ORDER BY i.ENTER_TIME2 desc nulls last) rown \n"
	  			+ "from base_device_document t,TZSB_INSPECTION_INFO i,employee ep "
	  			+ "where t.id=i.fk_tsjc_device_document_id and substr(i.enter_op_id,0,32) = ep.id "
	  			+ "and t.device_status <> '99' ) v "
	  		+ "left join employee_ext ex on substr(v.enter_op_id,0,32) = ex.fk_emp_id "
	  		+ "left join pub_attachment att on ex.id = att.business_id where rown<=100"; 
		return gisDeviceDao.createSQLQuery(sql)
				  .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list(); 
	}

}
