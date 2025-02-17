package com.lsts.report.dao;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.khnt.core.crud.dao.impl.EntityDaoImpl;
import com.lsts.report.bean.ReportPrint;

/**
 * 检验资料报送打印签收主表数据处理对象
 * @ClassName ReportPrintDao
 * @JDK 1.7
 * @author GaoYa
 * @date 2015-09-06 13:23:00
 */
@Repository("reportPrintDao")
public class ReportPrintDao extends EntityDaoImpl<ReportPrint> {

	/**
	 * 根据数据状态（已提交待签收）查询明细列表
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2017-03-07 下午14:50:00
	 */
	@SuppressWarnings("unchecked")
	public List<ReportPrint> getReportPrints() {
		List<ReportPrint> list = new ArrayList<ReportPrint>();
		String hql = "from ReportPrint i where i.data_status = '1'";
		list = this.createQuery(hql).list();
		return list;
	}
	/**
	 * 根据id查询报告报送打印信息
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReportPrint> queryReportPrints(String id) {
		List<ReportPrint> list = new ArrayList<ReportPrint>();
		String hql = "from ReportPrint i where i.id in (:id)";
		list = this.createQuery(hql).setParameter("id", id).list();
		return list;
	}
}
