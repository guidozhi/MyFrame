package com.lsts.report.dao;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.khnt.core.crud.dao.impl.EntityDaoImpl;
import com.khnt.core.exception.KhntException;
import com.khnt.utils.StringUtil;
import com.lsts.report.bean.ReportItem;



/**
 * 报告项目管理   dao
 * 
 * @author 肖慈边 2014-1-24
 */

@Repository("reportItemDao")
public class ReportItemDao extends EntityDaoImpl<ReportItem> {

	/**
	 * 获取报告项目
	 * @param report_id -- 报告ID
	 * @return 
	 * @author GaoYa
	 * @date 2014-03-07 上午10:03:00
	 */
	@SuppressWarnings("unchecked")
	public List<ReportItem> queryByReportId(String report_id){
		List<ReportItem> list = new ArrayList<ReportItem>();
    	try {
    		if (StringUtil.isNotEmpty(report_id)) {
    			String hql = "from ReportItem r where r.fk_reports_id=? order by r.page_index asc";
    			list = this.createQuery(hql, report_id).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	/**
	 * 获取报告项目固定必选页
	 * @param report_id -- 报告ID
	 * @return
	 * @author GaoYa
	 * @date 2018-07-31 下午16:40:00
	 */
	@SuppressWarnings("unchecked")
	public List<ReportItem> queryMustPageByReportId(String report_id) throws KhntException{
		List<ReportItem> list = new ArrayList<ReportItem>();
		if (StringUtil.isNotEmpty(report_id)) {
			String sql = "from ReportItem r where r.fk_reports_id=? and r.is_must = '1'";
			list = this.createQuery(sql, report_id).list();
		}
		return list;
	}
}
