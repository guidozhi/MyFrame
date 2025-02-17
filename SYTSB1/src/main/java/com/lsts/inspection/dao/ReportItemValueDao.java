package com.lsts.inspection.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.khnt.core.crud.dao.impl.EntityDaoImpl;
import com.khnt.utils.StringUtil;
import com.lsts.IdFormat;
import com.lsts.inspection.bean.ReportItemValue;



/**
 * 报告检验项目数据处理对象
 * @ClassName ReportItemValueDao
 * @JDK 1.7
 * @author GaoYa
 * @date 2014-02-26 上午09:27:00
 */
@Repository("reportItemValueDao")
public class ReportItemValueDao extends EntityDaoImpl<ReportItemValue> {

	/**
	 * 获取报告检验项目
	 * @param inspectionInfoId -- 报检信息ID
	 * @param report_id -- 报告ID
	 * @return 
	 * @author GaoYa
	 * @date 2014-02-26 上午09:29:00
	 */
	@SuppressWarnings("unchecked")
	public List<ReportItemValue> queryByInspectionInfoId(String inspectionInfoId, String report_id){
		List<ReportItemValue> list = new ArrayList<ReportItemValue>();
    	try {
    		if (StringUtil.isNotEmpty(inspectionInfoId) && StringUtil.isNotEmpty(report_id)) {
    			String hql = "from ReportItemValue r where r.fk_inspection_info_id=? and r.fk_report_id=?";
    			list = this.createQuery(hql, inspectionInfoId, report_id).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ReportItemValue> queryByitemValue(String inspectionInfoId){
		List<ReportItemValue> list = new ArrayList<ReportItemValue>();
    	try {
    		if (StringUtil.isNotEmpty(inspectionInfoId)) {
    			String hql = "from ReportItemValue r where r.fk_inspection_info_id=? ";
    			list = this.createQuery(hql, inspectionInfoId).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<ReportItemValue> getItemByItemName(String info_id, String report_type, String item_name){
		List<ReportItemValue> list = new ArrayList<ReportItemValue>();
		try {
			String hql = "from ReportItemValue where fk_inspection_info_id=? "
				 + " and fk_report_id=? and item_name=?";
		list = this.createQuery(hql, info_id, report_type, item_name).list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<ReportItemValue> getItemByItemNames(String info_id, String report_type, String item_names){
		List<ReportItemValue> list = new ArrayList<ReportItemValue>();
		try {
			if(StringUtil.isNotEmpty(item_names)){
				item_names = IdFormat.formatIdStr(item_names);
				String hql = "from ReportItemValue where fk_inspection_info_id=? "
					 + " and fk_report_id=? and item_name in ("+item_names+")";
				list = this.createQuery(hql, info_id, report_type).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	public Object insertReportItemValue(final String gwid,final String report_type,
			final String item_name, final String item_value,
			final String ins_info_id)
	{
		return this.getHibernateTemplate()
				.execute(new HibernateCallback<Object>() {
			//@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				session.doWork(new Work() {
					//@Override
					public void execute(Connection conn) 
											throws SQLException {
						String sql = "insert into TZSB_REPORT_ITEM_VALUE"
								+ " (id,fk_report_id,item_name,item_value,item_type,fk_inspection_info_id) "
								+ " values(?,?,?,?,?,?)"; 
						PreparedStatement statement 
							= conn.prepareStatement(sql);
						statement.setString(1, gwid);
						statement.setString(2, report_type);
						statement.setString(3, item_name);
						statement.setString(4, item_value);
						statement.setString(5, "string");
						statement.setString(6, ins_info_id);
						statement.execute();
						statement.close();
					}
				});
				return null;
			}
		});
	}
	
	/**
	 * 删除报告检验项目表参数
	 * @param info_id -- 检验业务信息ID
	 * 
	 * @return 
	 * @author GaoYa
	 * @date 2015-11-14 19:29:00
	 */
	public void delReportItemValue(String info_id){
		//删除tzsb_report_item_value表中之前保存的数据
		//报告书编号不删除
		String sql = "delete from tzsb_report_item_value where "
				+" fk_inspection_info_id = ? "
				+" and upper(item_name) <> 'REPORT_SN' " ;
		this.createSQLQuery(sql, info_id).executeUpdate();
	}
	
	/**
	 * 获取报告检验项目
	 * @param info_id -- 检验业务信息ID
	 * @param report_id -- 检验报告模板信息ID
	 * @param item_names -- 检验项目keys
	 * 
	 * @return 
	 * @author GaoYa
	 * @date 2017-10-30 14:42:00
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getGCContent(String info_id, String report_id, String item_names) throws Exception {
		Map<String, Object> contentMap = new HashMap<String, Object>();
		
		List<ReportItemValue> list = new ArrayList<ReportItemValue>();
		try {
			if(StringUtil.isNotEmpty(info_id) && StringUtil.isNotEmpty(report_id)){
				item_names = IdFormat.formatIdStr(item_names);
				String hql = "from ReportItemValue where fk_inspection_info_id=? "
					 + " and fk_report_id=? and item_name in ("+item_names+")";
				list = this.createQuery(hql, info_id, report_id).list();
				
				for (int i = 0; i < list.size(); i++) {
					ReportItemValue ival = (ReportItemValue) list.get(i);
					contentMap.put(ival.getItem_name(), ival.getItem_value());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return contentMap;
	}
	// 根据长度、数字生成前面补0的数字 liaozw
	private static String getCountNum(int count_num,int length){
		String str = String.valueOf(count_num);
		String nextNum = str ;
		for(int i = str.length() ; i<length ; i++ ){
			str ="0"+ str;
		}
		return  str;
	}
	// 生成3位序号
	private String getCountNum(int count_num){
        String nextNum = "";
       if((0 < count_num) && (count_num < 10))
    	   nextNum = "00" + count_num;
       if((9 < count_num) && (count_num < 100))
    	   nextNum = "0" + count_num;
       else if(count_num > 99)
    	   nextNum = String.valueOf(count_num);
       return  nextNum;
   }
	public String getOpSign(String userId){
		//适应省院业务，更改SYS_USER为PUB_ATTACHMENT
		String sql = "select t.id from PUB_ATTACHMENT t,employee e where e.id = t.business_id and e.id = ?";
		;
		List<Object> list = this.createSQLQuery(sql,userId).list();
		if(list.size()>0&&list.get(0)!=null) {
			return list.get(0).toString();
		}else {
			return "";
		}
	}
	/**
	 * 获取报告检验项目
	 * @param infoId -- 报检信息ID
	 * @param report_id -- 报告ID
	 * @param item_name -- 报告检验项目key
	 * 
	 * @return 
	 * @author GaoYa
	 * @date 2018-09-26 下午14:55:00
	 */
	@SuppressWarnings("unchecked")
	public List<ReportItemValue> queryItems(String infoId, String report_id, String item_name){
		List<ReportItemValue> list = new ArrayList<ReportItemValue>();
    	try {
    		if (StringUtil.isNotEmpty(infoId) && StringUtil.isNotEmpty(report_id) && StringUtil.isNotEmpty(item_name)) {
    			String hql = "from ReportItemValue r where r.fk_inspection_info_id=? and r.fk_report_id=? and r.item_name=?";
    			list = this.createQuery(hql, infoId, report_id, item_name).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	/**
	 * 获取报告书编号后三位序号
	 * @param sn_pre -- 报告书编号前缀
	 * @return 
	 * @author GaoYa
	 * @date 2014-03-10 下午03:32:00
	 */
	@SuppressWarnings("unchecked")
	public String queryNextReportSn(String sn_pre,String codeRules){
		String mod = "";
		for(int i = 0 ; i < codeRules.length() ; i++){
			mod+="0";
		}
		String nextNum = "";
		String sql = "select nvl(max(Substr(t.item_value, length('" + sn_pre + "')+1)), '"+mod+"') count_num" +
        " from TZSB_REPORT_ITEM_VALUE t" +
        " where t.item_name='REPORT_SN' and t.item_value like '" + sn_pre + "%'";
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			String curNum = list.get(0)+"";
			if (StringUtil.isNotEmpty(curNum)) {
				nextNum = getCountNum(Integer.parseInt(curNum)+1,codeRules.length());
			}
		}
		return nextNum;
	}
	public List<ReportItemValue> queryOpByInspectionInfoId(String inspectionInfoId){
		List<ReportItemValue> list = new ArrayList<ReportItemValue>();
    	try {
    		if (StringUtil.isNotEmpty(inspectionInfoId) ) {
    			String hql = "from ReportItemValue r where r.fk_inspection_info_id=? and r.item_name  like '%%_op%'";
    			list = this.createQuery(hql, inspectionInfoId).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
}
