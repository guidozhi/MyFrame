package com.scts.payment.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.khnt.base.Factory;
import com.khnt.core.crud.dao.impl.EntityDaoImpl;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.scts.payment.bean.InspectionInfoDTO;
import com.scts.payment.bean.InspectionPayInfo;
import com.scts.payment.bean.InspectionPayInfoDTO;
import com.scts.payment.bean.ReportBorrowDTO;

import util.TS_Util;

/**
 * 报检收费数据处理对象
 * 
 * @ClassName InspectionPayInfoDao
 * @JDK 1.7
 * @author GaoYa
 * @date 2014-05-04 下午04:23:00
 */
@Repository("inspectionPayInfoDao")
public class InspectionPayInfoDao extends EntityDaoImpl<InspectionPayInfo> {
	private Logger logger = Logger.getLogger(this.getClass());

	private static Connection conn = null;  // 数据库连接
    private static PreparedStatement pstmt = null; // 数据库操作对象
    private static ResultSet rs = null; // 结果集
    
	/**
	 * 根据业务ID查询业务收费信息
	 * 
	 * @param fk_inspection_info_id
	 * @return
	 * @author GaoYa
	 * @date 2014-05-06 上午09:29:00
	 */
	@SuppressWarnings("unchecked")
	public InspectionPayInfo queryByInspectionInfoID(
			String fk_inspection_info_id) {
		List<InspectionPayInfo> list = new ArrayList<InspectionPayInfo>();
		InspectionPayInfo inspectionPayInfo = null;
		try {
			if (StringUtil.isNotEmpty(fk_inspection_info_id)) {
				String hql = "from InspectionPayInfo p where p.fk_inspection_info_id like :fk_inspection_info_id and p.payment_status!='99'";
				list = this.createQuery(hql)
						.setParameter("fk_inspection_info_id",
								"%" + fk_inspection_info_id + "%").list();
				if(!list.isEmpty()){
    				return (InspectionPayInfo)list.get(0);
    			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inspectionPayInfo;
	}
	
	/**
	 * 根据业务ID查询业务收费信息
	 * 
	 * @param fk_inspection_info_id
	 * 			-- 检验报告id
	 * @return
	 * @author GaoYa
	 * @date 2016-01-20 下午15:41:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionPayInfo> queryList(
			String fk_inspection_info_id) {
		List<InspectionPayInfo> list = null;
		try {
			if (StringUtil.isNotEmpty(fk_inspection_info_id)) {
				// payment_status:2 已收费
				String hql = "from InspectionPayInfo p where p.fk_inspection_info_id like :fk_inspection_info_id and p.payment_status='2'";
				list = this.createQuery(hql)
						.setParameter("fk_inspection_info_id",
								"%" + fk_inspection_info_id + "%").list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据发票号查询业务收费信息
	 * 
	 * @param invoice_no
	 * 			-- 发票号
	 * @return
	 * @author GaoYa
	 * @date 2016-09-02 下午15:58:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionPayInfo> queryPayList(
			String invoice_no) {
		List<InspectionPayInfo> list = new ArrayList<InspectionPayInfo>();
		try {
			if (StringUtil.isNotEmpty(invoice_no)) {
				// payment_status:2 已收费
				String hql = "from InspectionPayInfo p where p.invoice_no=? and p.payment_status='2'";
				list = this.createQuery(hql, invoice_no).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 删除
	public void delete(String id) {
		// 1、先删除检验收费业务与银行转账记录关联信息表
		//String sql = "delete from TJY2_CW_BANK2PAY where FK_INSPECTION_PAY_ID = ?";
		//this.createSQLQuery(sql, id).executeUpdate();
		
		// 2、删除检验收费业务信息
		//String hql = "delete from InspectionPayInfo where id = ?";
		//this.createQuery(hql, id).executeUpdate();
		InspectionPayInfo inspectionPayInfo = this.get(id);
		if (inspectionPayInfo!=null) {
			String sql = "update TZSB_INSPECTION_PAY_INFO set payment_status = '99' where id = ?";
			this.createSQLQuery(sql,id).executeUpdate();
			//String hql = "delete from InspectionPayInfo where id = ?";
			//this.createQuery(hql, id).executeUpdate();
		}
	}

	/**
	 * 获取编号后四位序号
	 * 
	 * @param pay_no_pre --
	 *            编号前缀
	 * @return
	 * @author GaoYa
	 * @date 2014-03-10 下午03:32:00
	 */
	@SuppressWarnings("unchecked")
	public String queryNextPay_no(String pay_no_pre) {
		String nextNum = "";
		String sql = "select nvl(max(Substr(t.PAY_NO, length('" + pay_no_pre
				+ "')+1)), '0000') count_num"
				+ " from TZSB_INSPECTION_PAY_INFO t" + " where t.PAY_NO like '"
				+ pay_no_pre + "%'";
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			String curNum = list.get(0) + "";
			if (StringUtil.isNotEmpty(curNum)) {
				nextNum = getCountNum(Integer.parseInt(curNum) + 1);
			}
		}
		return nextNum;
	}
	
	/**
	 * 根据收费业务id获取收费时使用的发票号
	 * 
	 * @param id --
	 *            收费业务id
	 * @return
	 * @author GaoYa
	 * @date 2015-12-22 下午16:02:00
	 */
	@SuppressWarnings("unchecked")
	public String queryInvoice_no(String id) {
		String invoice_no = "";
		String sql = "select t.invoice_no from TZSB_INSPECTION_PAY_INFO t where t.id = ?";
		List list = this.createSQLQuery(sql, id).list();
		if (!list.isEmpty()) {
			invoice_no = list.get(0) + "";
		}
		return invoice_no;
	}
	
	/**
	 * 根据收费业务id获取收费总金额
	 * 
	 * @param id --
	 *            收费业务id
	 * @return
	 * @author GaoYa
	 * @date 2015-12-22 下午16:02:00
	 */
	@SuppressWarnings("unchecked")
	public double queryMoney(String id) {
		double money = 0;
		String sql = "select t.PAY_RECEIVED from TZSB_INSPECTION_PAY_INFO t where t.id = ?";
		List list = this.createSQLQuery(sql, id).list();
		if (!list.isEmpty()) {
			money = list.get(0)!=null?Double.parseDouble(list.get(0) + ""):0;
		}
		return money;
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> queryOldMoneyAndInvoice_no(String id) {
		String sql = "select t.PAY_RECEIVED,t.invoice_no from TZSB_INSPECTION_PAY_INFO t where t.id = ?";
		return this.createSQLQuery(sql, id).list();
	}

	// 生成4位序号
	private String getCountNum(int count_num) {
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

	/**
	 * 获取待收费业务信息
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2014-06-20 下午02:45:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionInfoDTO> getUnPayInfo() {
		List<InspectionInfoDTO> list = new ArrayList<InspectionInfoDTO>();
		try {
			conn = getConn();
			String sql = "select t1.report_com_name COMPANY_NAME,o.org_name as unitName,t1.advance_fees,t1.fee_status,t1.report_sn,t1.sn "
					+ " from TZSB_INSPECTION_INFO t1,SYS_ORG o "
					+ " where t1.CHECK_UNIT_ID = o.id(+)"
					+ " and t1.PRINT_TIME is not null "
					+ "order by t1.PRINT_TIME desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()){
						InspectionInfoDTO inspectionInfoDTO = new InspectionInfoDTO();
						inspectionInfoDTO.setCom_name(rs.getString(1));
						inspectionInfoDTO.setCheck_department(rs.getString(2));
						inspectionInfoDTO.setAdvance_fees(rs.getDouble(3));
						inspectionInfoDTO.setFee_status(rs.getString(4));
						inspectionInfoDTO.setReport_sn(rs.getString(5));
						inspectionInfoDTO.setSn(rs.getString(6));
						list.add(inspectionInfoDTO);
			}
			closeConn();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
		}
		return list;
	}

	/**
	 * 交账明细、收入明细导出
	 * 
	 * @param check_year
	 *            检验年份
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2014-06-10 上午11:24:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionPayInfoDTO> export(String check_unit_id,
			String invoice_start_date, String invoice_end_date) {
		List<InspectionPayInfoDTO> list = new ArrayList<InspectionPayInfoDTO>();
		try {
			String sql = "select to_char(p.pay_date, 'yyyy-MM-dd'),p.invoice_no,Rtrim(Ltrim(p.company_name)),p.pay_type,o.another_name as check_department,"
					+ " substr(b.device_sort_code, 0, 1) as big_class,sum(p.pay_received) as pay_received,sum(p.cash_pay) as cash_pay,sum(p.remark) as transfer_pay,sum(p.pos) as pos"
					+ " from TZSB_INSPECTION_PAY_INFO p,tzsb_inspection_info t,base_device_document b,SYS_ORG o"
					+ " where (case instr(p.fk_inspection_info_id, ',') when 0 then p.fk_inspection_info_id else substr(p.fk_inspection_info_id,0,instr(p.fk_inspection_info_id, ',') - 1)"
					+ "  end) = t.ID and t.CHECK_UNIT_ID = o.id";
					if (StringUtil.isNotEmpty(check_unit_id.trim())) {
						sql += " and t.CHECK_UNIT_ID = '" + check_unit_id.trim() + "'";
					}
					if (StringUtil.isNotEmpty(invoice_start_date)) {
						sql += " and p.pay_date >= to_date('" + invoice_start_date
								+ "','yyyy-MM-dd')";
					}
					if (StringUtil.isNotEmpty(invoice_end_date)) {
						sql += " and p.pay_date <= to_date('" + invoice_end_date
								+ "','yyyy-MM-dd')";
					}
					
			sql += " and t.fk_tsjc_device_document_id = b.id and t.fee_status = '2' and t.data_status!='99' and p.payment_status = '2' and p.pay_type!='3'";
			sql += " group by p.pay_date,p.invoice_no,Rtrim(Ltrim(p.company_name)),p.pay_type, o.another_name , substr(b.device_sort_code, 0, 1) order by invoice_no";
	
			/*
			String sql = "with pay_invoice_info as";
			sql += " (select invoices.invoice_no iNo, info.* from (select substr(temp1.min_no, 0, 3) || to_char(substr(temp1.min_no, 4) + (rownum - 1)) invoice_no";
			sql += " from (select min(substr(pi.invoice_no, 0, 9)) min_no, max(substr(pi.invoice_no, 0, 9)) max_no from TZSB_INSPECTION_PAY_INFO pi";
			sql += " where pi.payment_status = '2' ";
			if (StringUtil.isNotEmpty(invoice_start_date)) {
				sql += " and pi.pay_date >= to_date('"+invoice_start_date+"', 'yyyy-MM-dd')";
			}
			if (StringUtil.isNotEmpty(invoice_end_date)) {
				sql += " and pi.pay_date <= to_date('"+invoice_end_date+"', 'yyyy-MM-dd')";
			}
			sql += " ) temp1 connect by temp1.min_no + (rownum - 1) <= temp1.max_no) invoices, TZSB_INSPECTION_PAY_INFO info";
			sql += " where invoices.invoice_no = info.invoiceNo(+)) select * from (select to_char(p.pay_date, 'yyyy-MM-dd'),";
			sql += " p.iNo invoice_no,p.company_name, p.pay_type,sum(p.pay_received) as pay_received,sum(p.cash_pay) as cash_pay,sum(p.remark) as transfer_pay,";
			sql += " p.pos,o.another_name as check_department,substr(b.device_sort_code, 0, 1) as big_class ";
			sql += " from pay_invoice_info p,tzsb_inspection_info t,base_device_document b,SYS_ORG o";
			sql += " where (case instr(p.fk_inspection_info_id, ',') when 0 then p.fk_inspection_info_id else substr(p.fk_inspection_info_id,0,instr(p.fk_inspection_info_id, ',') - 1) end) = t.ID(+)";
			sql += " and t.CHECK_UNIT_ID = o.id(+) ";
			if (StringUtil.isNotEmpty(check_unit_id.trim())) {
				sql += " and t.CHECK_UNIT_ID = '" + check_unit_id.trim() + "'";
			}
			sql += " and t.fk_tsjc_device_document_id = b.id(+) and t.fee_status(+) = '2' group by to_char(p.pay_date, 'yyyy-MM-dd'),";
			sql += " p.iNo,p.company_name,p.pay_type,p.pos,o.another_name,substr(b.device_sort_code, 0, 1)) order by invoice_no";
			*/
			List rslist = this.createSQLQuery(sql).list();
			if (!rslist.isEmpty()) {
				for (int i = 0; i < rslist.size(); i++) {
					Object[] objArr = rslist.toArray();
					Object[] obj = (Object[]) objArr[i];
					InspectionPayInfoDTO inspectionPayInfoDTO = new InspectionPayInfoDTO();
					inspectionPayInfoDTO.setPay_date(String.valueOf(obj[0]));
					inspectionPayInfoDTO.setInvoice_no(obj[1]!=null?obj[1]+"":"");
					inspectionPayInfoDTO
							.setPay_com_name(String.valueOf(obj[2]));
					//inspectionPayInfoDTO.setCom_name(String.valueOf(obj[3]));
					inspectionPayInfoDTO.setPay_type(String.valueOf(obj[3]));
					inspectionPayInfoDTO
							.setPay_received(String.valueOf(obj[6]));
					inspectionPayInfoDTO.setCash_pay(String.valueOf(obj[7]));
					inspectionPayInfoDTO.setRemark(String.valueOf(obj[8]));
					inspectionPayInfoDTO.setPos(String.valueOf(obj[9]));
					String department = String.valueOf(obj[4]);
					String big_class = String.valueOf(obj[5]);
					if (StringUtil.isNotEmpty(big_class)) {
						if ("4".equals(big_class)) {
							department += "（起重）";
						} else if ("6".equals(big_class)) {
							department += "（游乐）";
						} else if ("9".equals(big_class)) {
							department += "（索道）";
						}
					}
					inspectionPayInfoDTO.setCheck_department(department);
					/*
					inspectionPayInfoDTO.setInspection_date(DateUtil
							.convertStringToDate("yyyy-MM-dd", String
									.valueOf(obj[11])));
					// 2015-08-01以前检验的设备均打9折，从2015-08-01开始检验的设备均不再打折
					Date inspection_date = inspectionPayInfoDTO.getInspection_date();
					Date unsale_Date = DateUtil.convertStringToDate("yyyy-MM-dd", "2015-08-01");
					if (inspection_date.before(unsale_Date)) {
						inspectionPayInfoDTO.setIsDiscount("1");
					}else{
						inspectionPayInfoDTO.setIsDiscount("0");
					}
					*/
					list.add(inspectionPayInfoDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 交账明细导出（承压）
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2015-12-13 上午10:24:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionPayInfoDTO> exportCY(String check_unit_id,
			String pay_start_date, String pay_end_date) {
		List<InspectionPayInfoDTO> list = new ArrayList<InspectionPayInfoDTO>();
		try {
			String sql = "select to_char(p.pay_date, 'yyyy-MM-dd'),p.invoice_no,Rtrim(Ltrim(p.company_name)),p.pay_type,o.another_name as check_department,"
					+ " sum(p.pay_received) as pay_received,sum(p.cash_pay) as cash_pay,sum(p.remark) as transfer_pay,sum(p.pos) as pos,sum(p.hand_in) as hand_in,sum(p.draft) as draft"
					+ " from TZSB_INSPECTION_PAY_INFO p,SYS_ORG o"
					+ " where p.device_type='CY'";
					if (StringUtil.isNotEmpty(check_unit_id.trim())) {
						sql += " and p.check_dep_id = '" + check_unit_id.trim() + "'";
					}
					sql += " and p.check_dep_id = o.id ";
					if (StringUtil.isNotEmpty(pay_start_date)) {
						sql += " and p.pay_date >= to_date('" + pay_start_date
								+ "','yyyy-MM-dd')";
					}
					if (StringUtil.isNotEmpty(pay_end_date)) {
						sql += " and p.pay_date <= to_date('" + pay_end_date
								+ "','yyyy-MM-dd')";
					}
					
			sql += " and p.payment_status = '2' and p.pay_type!='3'";
			sql += " group by p.pay_date,p.invoice_no,Rtrim(Ltrim(p.company_name)),p.pay_type, o.another_name order by invoice_no";
			List rslist = this.createSQLQuery(sql).list();
			if (!rslist.isEmpty()) {
				for (int i = 0; i < rslist.size(); i++) {
					Object[] objArr = rslist.toArray();
					Object[] obj = (Object[]) objArr[i];
					InspectionPayInfoDTO inspectionPayInfoDTO = new InspectionPayInfoDTO();
					inspectionPayInfoDTO.setPay_date(String.valueOf(obj[0]));
					inspectionPayInfoDTO.setInvoice_no(obj[1]!=null?obj[1]+"":"");
					inspectionPayInfoDTO
							.setPay_com_name(String.valueOf(obj[2]));
					//inspectionPayInfoDTO.setCom_name(String.valueOf(obj[3]));
					inspectionPayInfoDTO.setPay_type(String.valueOf(obj[3]));
					inspectionPayInfoDTO
							.setPay_received(String.valueOf(obj[5]));
					inspectionPayInfoDTO.setCash_pay(String.valueOf(obj[6]));
					inspectionPayInfoDTO.setRemark(String.valueOf(obj[7]));
					inspectionPayInfoDTO.setPos(String.valueOf(obj[8]));
					inspectionPayInfoDTO.setHand_in(String.valueOf(obj[9]));
					inspectionPayInfoDTO.setDraft(String.valueOf(obj[10]));
					String department = String.valueOf(obj[4]);				
					inspectionPayInfoDTO.setCheck_department(department);
					list.add(inspectionPayInfoDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 交账明细导出（所有，包含机电和承压）
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2015-12-22 上午11:22:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionPayInfoDTO> exportAll(String check_unit_id, String pay_start_date, String pay_end_date,
			String invoice_no_start, String invoice_no_end, String invoice_category, String payment_category,
			String op_user_name) {
		List<InspectionPayInfoDTO> list = new ArrayList<InspectionPayInfoDTO>();
		try {
			String sql = "select t1.pay_date,t1.invoice_no,Rtrim(Ltrim(t1.company_name)),t1.pay_type,t1.check_department,";
			sql += " sum(t1.pay_received),sum(t1.cash_pay),sum(t1.transfer_pay),sum(t1.pos),t1.big_class,t1.invoice_type,sum(t1.hand_in) ";
			sql += "from (";
			sql += "select to_char(p.pay_date, 'yyyy-MM-dd') as pay_date,p.invoice_no,Rtrim(Ltrim(p.company_name)) as company_name,p.pay_type,o.another_name as check_department,"
					+ " p.pay_received,p.cash_pay,p.remark as transfer_pay,p.pos,'' as big_class,s.invoice_type,p.hand_in "
					+ " from TZSB_INSPECTION_PAY_INFO p,SYS_ORG o,TJY2_CW_INVOICE_F s"
					+ " where p.device_type='CY' and p.payment_status = '2' ";
			if (StringUtil.isNotEmpty(check_unit_id.trim())) {
				sql += " and p.check_dep_id = '" + check_unit_id.trim() + "'";
			}
			sql += " and p.check_dep_id = o.id(+) ";
			if (StringUtil.isNotEmpty(pay_start_date)) {
				sql += " and p.pay_date >= to_date('" + pay_start_date + "','yyyy-MM-dd')";
			}
			if (StringUtil.isNotEmpty(pay_end_date)) {
				sql += " and p.pay_date <= to_date('" + pay_end_date + "','yyyy-MM-dd')";
			}
			if (StringUtil.isNotEmpty(invoice_no_start)) {
				sql += " and p.invoice_no >= '" + invoice_no_start + "'";
			}
			if (StringUtil.isNotEmpty(invoice_no_end)) {
				sql += " and p.invoice_no <= '" + invoice_no_end + "'";
			}
			if(StringUtil.isNotEmpty(payment_category)){
				sql += " and p.pay_type='"+payment_category+"'";
			}
			if(StringUtil.isNotEmpty(invoice_category)){
				sql += " and s.invoice_type='"+invoice_category+"'";
			}
			sql += " and p.pay_type!='3' and s.INVOICE_CODE(+)=p.invoice_no";

			sql += " union ";

			sql += " select to_char(p.pay_date, 'yyyy-MM-dd') as pay_date,p.invoice_no,Rtrim(Ltrim(p.company_name)) as company_name,p.pay_type,o.another_name as check_department,"
					+ " p.pay_received,p.cash_pay,p.remark as transfer_pay,p.pos,substr(b.device_sort_code, 0, 1) as big_class,s.invoice_type,p.hand_in "
					+ " from TZSB_INSPECTION_PAY_INFO p,tzsb_inspection_info t,base_device_document b,SYS_ORG o,TJY2_CW_INVOICE_F s"
					+ " where (case instr(p.fk_inspection_info_id, ',') when 0 then p.fk_inspection_info_id else substr(p.fk_inspection_info_id,0,instr(p.fk_inspection_info_id, ',') - 1)"
					+ "  end) = t.ID and t.CHECK_UNIT_ID = o.id";
			if (StringUtil.isNotEmpty(check_unit_id.trim())) {
				sql += " and t.CHECK_UNIT_ID = '" + check_unit_id.trim() + "'";
			}
			if (StringUtil.isNotEmpty(pay_start_date)) {
				sql += " and p.pay_date >= to_date('" + pay_start_date + "','yyyy-MM-dd')";
			}
			if (StringUtil.isNotEmpty(pay_end_date)) {
				sql += " and p.pay_date <= to_date('" + pay_end_date + "','yyyy-MM-dd')";
			}
			if (StringUtil.isNotEmpty(invoice_no_start)) {
				sql += " and p.invoice_no >= '" + invoice_no_start + "'";
			}
			if (StringUtil.isNotEmpty(invoice_no_end)) {
				sql += " and p.invoice_no <= '" + invoice_no_end + "'";
			}
			if(StringUtil.isNotEmpty(payment_category)){
				sql += " and p.pay_type='"+payment_category+"'";
			}
			if(StringUtil.isNotEmpty(invoice_category)){
				sql += " and s.invoice_type='"+invoice_category+"'";
			}
			sql += " and t.fk_tsjc_device_document_id = b.id and t.fee_status = '2' and t.data_status!='99' ";
			sql += " and p.payment_status = '2' and p.pay_type!='3' and s.INVOICE_CODE(+)=p.invoice_no) t1";
			sql += " group by t1.pay_date,t1.invoice_no,Rtrim(Ltrim(t1.company_name)),t1.pay_type, t1.check_department,big_class,t1.invoice_type order by t1.invoice_no";
			List rslist = this.createSQLQuery(sql).list();
			if (!rslist.isEmpty()) {
				for (int i = 0; i < rslist.size(); i++) {
					Object[] objArr = rslist.toArray();
					Object[] obj = (Object[]) objArr[i];
					InspectionPayInfoDTO inspectionPayInfoDTO = new InspectionPayInfoDTO();
					inspectionPayInfoDTO.setPay_date(String.valueOf(obj[0]));
					inspectionPayInfoDTO.setInvoice_no(obj[1] != null ? obj[1] + "" : "");
					inspectionPayInfoDTO.setPay_com_name(String.valueOf(obj[2]));
					// inspectionPayInfoDTO.setCom_name(String.valueOf(obj[3]));
					inspectionPayInfoDTO.setPay_type(String.valueOf(obj[3]));
					inspectionPayInfoDTO.setPay_received(String.valueOf(obj[5]));
					inspectionPayInfoDTO.setCash_pay(String.valueOf(obj[6]));
					inspectionPayInfoDTO.setRemark(String.valueOf(obj[7]));
					inspectionPayInfoDTO.setPos(String.valueOf(obj[8]));
					String department = String.valueOf(obj[4]);
					String big_class = String.valueOf(obj[9]);
					if (StringUtil.isNotEmpty(big_class)) {
						if ("4".equals(big_class)) {
							department += "（起重）";
						} else if ("6".equals(big_class)) {
							department += "（游乐）";
						} else if ("9".equals(big_class)) {
							department += "（索道）";
						}
					}
					inspectionPayInfoDTO.setCheck_department(department);
					inspectionPayInfoDTO.setInvoice_type(String.valueOf(obj[10]));
					inspectionPayInfoDTO.setHand_in(String.valueOf(obj[11]));
					list.add(inspectionPayInfoDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 交账明细、收入明细导出
	 * 
	 * @param check_year
	 *            检验年份
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2014-06-10 上午11:24:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionPayInfoDTO> exportZZJD(String check_dep_name,
			String invoice_start_date, String invoice_end_date) {
		List<InspectionPayInfoDTO> list = new ArrayList<InspectionPayInfoDTO>();
		try {
			String sql = "select to_char(p.pay_date,'yyyy-MM-dd') as pay_date,nvl(p.invoice_no,' ') as invoice_no,Rtrim(Ltrim(p.company_name)),"
					+ "t.report_com_name as COM_NAME,p.pay_type,p.pay_received as pay_received,p.cash_pay as cash_pay,p.remark as transfer_pay,"
					+ "p.pos,o.org_name as check_department,substr(b.device_sort_code,0,1) as big_class, t.advance_time"
					+ " from TZSB_INSPECTION_PAY_INFO p,tzsb_inspection_info t,base_device_document b,SYS_ORG o "
					+ " where (case  instr(p.fk_inspection_info_id,',') when 0 then p.fk_inspection_info_id "
					+ " else substr(p.fk_inspection_info_id,0,instr(p.fk_inspection_info_id,',')-1) end)=t.ID ";
			if (StringUtil.isNotEmpty(check_dep_name)) {
				sql += " and o.org_name like '%" + check_dep_name + "%'";
			}
			if (StringUtil.isNotEmpty(invoice_start_date)) {
				sql += " and p.pay_date >= to_date('" + invoice_start_date
						+ "','yyyy-MM-dd')";
			}
			if (StringUtil.isNotEmpty(invoice_end_date)) {
				sql += " and p.pay_date <= to_date('" + invoice_end_date
						+ "','yyyy-MM-dd')";
			}/*
				 * else { sql += " and p.pay_date <= trunc(sysdate)"; }
				 */
			sql += " and t.fk_tsjc_device_document_id=b.id";
			sql += " and t.CHECK_UNIT_ID = o.id and t.fee_status='2' and t.data_status!='99' and p.payment_status='2' and p.pay_type!='3'";
			sql += " and t.fk_inspection_id is null and t.fk_tsjc_device_document_id is null order by p.invoice_no";
			List rslist = this.createSQLQuery(sql).list();
			if (!rslist.isEmpty()) {
				for (int i = 0; i < rslist.size(); i++) {
					Object[] objArr = rslist.toArray();
					Object[] obj = (Object[]) objArr[i];
					InspectionPayInfoDTO inspectionPayInfoDTO = new InspectionPayInfoDTO();
					inspectionPayInfoDTO.setPay_date(String.valueOf(obj[0]));
					inspectionPayInfoDTO.setInvoice_no(String.valueOf(obj[1]));
					inspectionPayInfoDTO
							.setPay_com_name(String.valueOf(obj[2]));
					inspectionPayInfoDTO.setCom_name(String.valueOf(obj[3]));
					inspectionPayInfoDTO.setPay_type(String.valueOf(obj[4]));
					inspectionPayInfoDTO
							.setPay_received(String.valueOf(obj[5]));
					inspectionPayInfoDTO.setCash_pay(String.valueOf(obj[6]));
					inspectionPayInfoDTO.setRemark(String.valueOf(obj[7]));
					inspectionPayInfoDTO.setPos(String.valueOf(obj[8]));
					String department = String.valueOf(obj[9]);
					String big_class = String.valueOf(obj[10]);
					if (StringUtil.isNotEmpty(big_class)) {
						if ("4".equals(big_class)) {
							department += "（起重机械）";
						} else if ("6".equals(big_class)) {
							department += "（游乐设施）";
						} else if ("9".equals(big_class)) {
							department += "（客运索道）";
						}
					}
					inspectionPayInfoDTO.setCheck_department(department);
					/*
					inspectionPayInfoDTO.setInspection_date(DateUtil
							.convertStringToDate("yyyy-MM-dd", String
									.valueOf(obj[11])));
					// 2015-08-01以前检验的设备均打9折，从2015-08-01开始检验的设备均不再打折
					Date inspection_date = inspectionPayInfoDTO.getInspection_date();
					Date unsale_Date = DateUtil.convertStringToDate("yyyy-MM-dd", "2015-08-01");
					if (inspection_date.before(unsale_Date)) {
						inspectionPayInfoDTO.setIsDiscount("1");
					}else{
						inspectionPayInfoDTO.setIsDiscount("0");
					}
					*/
					list.add(inspectionPayInfoDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 借报告
	 * 
	 * @param check_unit_id
	 *            检验部门id
	 * @param sign_leader_id
	 *            签字主任id
	 * @param borrow_start_date
	 *            外借开始日期
	 * @param borrow_end_date
	 *            外借结束日期
	 * @param borrow_type
	 *            外借类型（0：外借报告，1：外借发票，2：外借报告和发票）
	 * @return
	 * @author GaoYa
	 * @date 2014-06-10 上午11:24:00
	 */
	@SuppressWarnings("unchecked")
	public List<ReportBorrowDTO> exportBorrow(String check_unit_id, String sign_leader_id,
			String borrow_start_date, String borrow_end_date, String borrow_type) {
		List<ReportBorrowDTO> list = new ArrayList<ReportBorrowDTO>();
		// 获取部门签字主任信息集合
		Map<String, Object> org_mans = this.getOrg_mans(check_unit_id);
		try {
			String sql = "select borrow_date,invoice_no,COM_NAME,check_department,maintain_unit_name,borrow_name,contack_number,sign_leader_name,sum(unpay_amount),sum(counts),CHECK_UNIT_ID,repay_date"
					+ " from (select to_char(b.borrow_date, 'yyyy-MM-dd') as borrow_date, to_char(b.repay_date, 'yyyy-MM-dd') as repay_date, nvl(b.invoice_no, ' ') as invoice_no,t.report_com_name as COM_NAME, "
					+ " b.unpay_amount,regexp_count(b.fk_inspection_info_id, ',') + 1 counts,o.org_name as check_department,b1.maintain_unit_name,b.borrow_name,b.contack_number,b.sign_leader_name,t.CHECK_UNIT_ID"
					+ " from TZSB_REPORT_BORROW b,tzsb_inspection_info t,base_device_document b1,SYS_ORG o "
					+ " where (case instr(b.fk_inspection_info_id, ',') "
					+ " when 0 then b.fk_inspection_info_id else substr(b.fk_inspection_info_id,0,instr(b.fk_inspection_info_id, ',') - 1) end) = t.ID";
			if (StringUtil.isNotEmpty(check_unit_id)) {
				sql += " and t.CHECK_UNIT_ID = '" + check_unit_id.trim() + "' ";
			}
			sql += " and t.CHECK_UNIT_ID = o.id ";
			if (StringUtil.isNotEmpty(sign_leader_id)) {
				sql += " and b.SIGN_LEADER_ID = '" + sign_leader_id.trim() + "' ";
			}
			if (StringUtil.isNotEmpty(borrow_type)) {
				sql += " and b.borrow_type = '" + borrow_type + "'";
				sql += " and t.fee_status = '3'";
			}
			if (StringUtil.isNotEmpty(borrow_start_date)) {
				sql += " and b.borrow_date >= to_date('" + borrow_start_date + "','yyyy-MM-dd')";
			}
			if (StringUtil.isNotEmpty(borrow_end_date)) {
				sql += " and b.borrow_date <= to_date('" + borrow_end_date + "','yyyy-MM-dd')";
			}

			sql += " and b.borrow_status!='3' and t.data_status!='99' and b1.device_status !='99' and t.FK_TSJC_DEVICE_DOCUMENT_ID = b1.id) temp"
					+ " group by borrow_date,invoice_no,COM_NAME,check_department,maintain_unit_name,borrow_name,contack_number,sign_leader_name,CHECK_UNIT_ID,repay_date order by borrow_date ";
			List rslist = this.createSQLQuery(sql).list();
			if (!rslist.isEmpty()) {
				for (int i = 0; i < rslist.size(); i++) {
					Object[] objArr = rslist.toArray();
					Object[] obj = (Object[]) objArr[i];
					ReportBorrowDTO reportBorrowDTO = new ReportBorrowDTO();
					reportBorrowDTO.setBorrow_date(obj[0] != null ? String.valueOf(obj[0]) : "");
					reportBorrowDTO.setInvoice_no(obj[1] != null ? String.valueOf(obj[1]) : "");
					reportBorrowDTO.setCom_name(obj[2] != null ? String.valueOf(obj[2]) : "");
					reportBorrowDTO.setCheck_department(obj[3] != null ? String.valueOf(obj[3]) : "");
					reportBorrowDTO.setMaintain_unit_name(obj[4] != null ? String.valueOf(obj[4]) : "");
					reportBorrowDTO.setBorrow_name(obj[5] != null ? String.valueOf(obj[5]) : "");
					reportBorrowDTO.setContack_number(obj[6] != null ? String.valueOf(obj[6]) : "");
					String sign_leader_name = obj[7]!=null?String.valueOf(obj[7]):"";
					if(StringUtil.isNotEmpty(sign_leader_name)){
						reportBorrowDTO.setIssue_name(sign_leader_name);
					}else{
						String org_id = obj[10] != null ? String.valueOf(obj[10]) : "".trim();
						if (StringUtil.isNotEmpty(org_id)) {
							if (org_mans.containsKey(org_id)) {
								reportBorrowDTO.setIssue_name(String.valueOf(org_mans.get(org_id)));
							}
						} else {
							if (StringUtil.isNotEmpty(check_unit_id.trim())) {
								if (org_mans.containsKey(check_unit_id)) {
									reportBorrowDTO.setIssue_name(String.valueOf(org_mans.get(check_unit_id)));
								}
							}
						}
					}
					reportBorrowDTO.setUnpay_amount(obj[8] != null ? String.valueOf(obj[8]) : "");
					reportBorrowDTO.setReport_count(obj[9] != null ? String.valueOf(obj[9]) : "");
					reportBorrowDTO.setRepay_date(obj[11] != null ? String.valueOf(obj[11]) : "");
					list.add(reportBorrowDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 借发票
	 * 
	 * @param check_unit_id
	 *            检验部门id
	 * @param borrow_start_date
	 *            外借开始日期
	 * @param borrow_end_date
	 *            外借结束日期
	 * @param borrow_type
	 *            外借类型（0：外借报告，1：外借发票，2：外借报告和发票）
	 * @return
	 * @author GaoYa
	 * @date 2014-06-10 上午11:24:00
	 */
	@SuppressWarnings("unchecked")
	public List<ReportBorrowDTO> exportBorrow2(String check_unit_id, String sign_leader_id,
			String borrow_start_date, String borrow_end_date, String borrow_type) {
		List<ReportBorrowDTO> list = new ArrayList<ReportBorrowDTO>();
		// 获取部门签字主任信息集合
		Map<String, Object> org_mans = this.getOrg_mans(check_unit_id);
		try {
			String sql = "select borrow_date,invoice_no,report_com_name,check_department,borrow_name,contack_number,sign_leader_name,sum(unpay_amount),CHECK_UNIT_ID"
					+ " from (select to_char(b.borrow_date, 'yyyy-MM-dd') as borrow_date,nvl(b.invoice_no, ' ') as invoice_no,t.report_com_name,"
					+ " b.unpay_amount,o.org_name as check_department,b.borrow_name,b.contack_number,b.sign_leader_name,t.CHECK_UNIT_ID "
					+ " from TZSB_REPORT_BORROW b, tzsb_inspection_info t,base_device_document b1,SYS_ORG o"
					+ " where (case instr(b.fk_inspection_info_id, ',') when 0 then b.fk_inspection_info_id " 
					+ " else substr(b.fk_inspection_info_id,0,instr(b.fk_inspection_info_id, ',') - 1) end) = t.ID" ;		
			if (StringUtil.isNotEmpty(check_unit_id.trim())) {
				sql += " and t.CHECK_UNIT_ID = '" + check_unit_id.trim() + "'  ";
			}
			sql += " and t.CHECK_UNIT_ID = o.id ";
			if (StringUtil.isNotEmpty(sign_leader_id.trim())) {
				sql += " and b.SIGN_LEADER_ID = '" + sign_leader_id.trim() + "' ";
			}
			if (StringUtil.isNotEmpty(borrow_type)) {
				sql += " and b.borrow_type = '" + borrow_type + "'";
				sql += " and t.fee_status = '4'";
			}
			if (StringUtil.isNotEmpty(borrow_start_date)) {
				sql += " and b.borrow_date >= to_date('" + borrow_start_date
						+ "','yyyy-MM-dd')";
			}
			if (StringUtil.isNotEmpty(borrow_end_date)) {
				sql += " and b.borrow_date <= to_date('" + borrow_end_date
						+ "','yyyy-MM-dd')";
			}/*
				 * else { sql += " and b.borrow_date <= trunc(sysdate)"; }
				 */
			
			sql += " and b.borrow_status!='3' and t.data_status!='99' and b1.device_status !='99' and t.FK_TSJC_DEVICE_DOCUMENT_ID = b1.id) temp";
			sql += " group by borrow_date, invoice_no,report_com_name, check_department, borrow_name,contack_number,sign_leader_name,CHECK_UNIT_ID order by borrow_date";
			List rslist = this.createSQLQuery(sql).list();
			if (!rslist.isEmpty()) {
				for (int i = 0; i < rslist.size(); i++) {
					Object[] objArr = rslist.toArray();
					Object[] obj = (Object[]) objArr[i];
					ReportBorrowDTO reportBorrowDTO = new ReportBorrowDTO();
					reportBorrowDTO.setBorrow_date(obj[0]!=null?String.valueOf(obj[0]):"");
					reportBorrowDTO.setInvoice_no(obj[1]!=null?String.valueOf(obj[1]):"");
					reportBorrowDTO.setBorrow_com_name(obj[2]!=null?String.valueOf(obj[2]):"");
					reportBorrowDTO.setCheck_department(obj[3]!=null?String.valueOf(obj[3]):"");
					reportBorrowDTO.setBorrow_name(obj[4]!=null?String.valueOf(obj[4]):"");
					reportBorrowDTO.setContack_number(obj[5]!=null?String.valueOf(obj[5]):"");
					String sign_leader_name = obj[6]!=null?String.valueOf(obj[6]):"";
					if(StringUtil.isNotEmpty(sign_leader_name)){
						reportBorrowDTO.setIssue_name(sign_leader_name);
					}else{
						String org_id = obj[8] != null ? String.valueOf(obj[8]) : "".trim();
						if (StringUtil.isNotEmpty(org_id)) {
							if (org_mans.containsKey(org_id)) {
								reportBorrowDTO.setIssue_name(String.valueOf(org_mans.get(org_id)));
							}
						} else {
							if (StringUtil.isNotEmpty(check_unit_id.trim())) {
								if (org_mans.containsKey(check_unit_id)) {
									reportBorrowDTO.setIssue_name(String.valueOf(org_mans.get(check_unit_id)));
								}
							}
						}
					}
					
					reportBorrowDTO.setUnpay_amount(obj[7]!=null?String.valueOf(obj[7]):"");
					list.add(reportBorrowDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 借报告和发票
	 * 
	 * @param check_unit_id
	 *            检验部门id
	 * @param borrow_start_date
	 *            外借开始日期
	 * @param borrow_end_date
	 *            外借结束日期
	 * @param borrow_type
	 *            外借类型（0：外借报告，1：外借发票，2：外借报告和发票）
	 * @return
	 * @author GaoYa
	 * @date 2014-06-10 上午11:24:00
	 */
	@SuppressWarnings("unchecked")
	public List<ReportBorrowDTO> exportBorrow3(String check_unit_id, String sign_leader_id,
			String borrow_start_date, String borrow_end_date, String borrow_type) {
		List<ReportBorrowDTO> list = new ArrayList<ReportBorrowDTO>();
		// 获取部门签字主任信息集合
		Map<String, Object> org_mans = this.getOrg_mans(check_unit_id);
		try {
			String sql = "select borrow_date,invoice_no,report_com_name,check_department,borrow_name,"
					+ " contack_number,sign_leader_name,sum(unpay_amount),CHECK_UNIT_ID,maintain_unit_name,sum(counts)"
					+ " from (select to_char(b.borrow_date, 'yyyy-MM-dd') as borrow_date,nvl(b.invoice_no, ' ') as invoice_no,t.report_com_name,"
					+ " b.unpay_amount,o.org_name as check_department,b.borrow_name,b.contack_number,b.sign_leader_name,t.CHECK_UNIT_ID,"
					+ " b1.maintain_unit_name,regexp_count(b.fk_inspection_info_id, ',') + 1 counts "
					+ " from TZSB_REPORT_BORROW b, tzsb_inspection_info t,base_device_document b1,SYS_ORG o"
					+ " where (case instr(b.fk_inspection_info_id, ',') when 0 then b.fk_inspection_info_id " 
					+ " else substr(b.fk_inspection_info_id,0,instr(b.fk_inspection_info_id, ',') - 1) end) = t.ID" ;		
			if (StringUtil.isNotEmpty(check_unit_id.trim())) {
				sql += " and t.CHECK_UNIT_ID = '" + check_unit_id.trim() + "'  ";
			}
			sql += " and t.CHECK_UNIT_ID = o.id ";
			if (StringUtil.isNotEmpty(sign_leader_id.trim())) {
				sql += " and b.SIGN_LEADER_ID = '" + sign_leader_id.trim() + "' ";
			}
			if (StringUtil.isNotEmpty(borrow_type)) {
				sql += " and b.borrow_type = '" + borrow_type + "'";
				sql += " and t.fee_status = '5'";	// 业务信息收费状态，5：外借报告和发票
			}
			if (StringUtil.isNotEmpty(borrow_start_date)) {
				sql += " and b.borrow_date >= to_date('" + borrow_start_date
						+ "','yyyy-MM-dd')";
			}
			if (StringUtil.isNotEmpty(borrow_end_date)) {
				sql += " and b.borrow_date <= to_date('" + borrow_end_date
						+ "','yyyy-MM-dd')";
			}/*
				 * else { sql += " and b.borrow_date <= trunc(sysdate)"; }
				 */
			
			sql += " and b.borrow_status!='3' and t.data_status!='99' and t.FK_TSJC_DEVICE_DOCUMENT_ID = b1.id) temp";
			sql += " group by borrow_date, invoice_no,report_com_name, check_department, borrow_name,contack_number,sign_leader_name,CHECK_UNIT_ID,maintain_unit_name order by borrow_date";
			List rslist = this.createSQLQuery(sql).list();
			if (!rslist.isEmpty()) {
				for (int i = 0; i < rslist.size(); i++) {
					Object[] objArr = rslist.toArray();
					Object[] obj = (Object[]) objArr[i];
					ReportBorrowDTO reportBorrowDTO = new ReportBorrowDTO();
					reportBorrowDTO.setBorrow_date(String.valueOf(obj[0]));
					reportBorrowDTO.setInvoice_no(obj[1]!=null?String.valueOf(obj[1]):"");
					reportBorrowDTO.setCom_name(String.valueOf(obj[2]));
					reportBorrowDTO.setCheck_department(String.valueOf(obj[3]));
					reportBorrowDTO.setBorrow_name(obj[4]!=null?String.valueOf(obj[4]):"");
					reportBorrowDTO.setContack_number(obj[5]!=null?String.valueOf(obj[5]):"");
					String sign_leader_name = obj[6]!=null?String.valueOf(obj[6]):"";
					if(StringUtil.isNotEmpty(sign_leader_name)){
						reportBorrowDTO.setIssue_name(sign_leader_name);
					}else{
						String org_id = obj[8] != null ? String.valueOf(obj[8]) : "".trim();
						if (StringUtil.isNotEmpty(org_id)) {
							if (org_mans.containsKey(org_id)) {
								reportBorrowDTO.setIssue_name(String.valueOf(org_mans.get(org_id)));
							}
						} else {
							if (StringUtil.isNotEmpty(check_unit_id.trim())) {
								if (org_mans.containsKey(check_unit_id)) {
									reportBorrowDTO.setIssue_name(String.valueOf(org_mans.get(check_unit_id)));
								}
							}
						}
					}
					reportBorrowDTO.setUnpay_amount(String.valueOf(obj[7]));
					reportBorrowDTO.setMaintain_unit_name(obj[9]!=null?String.valueOf(obj[9]):"");
					reportBorrowDTO.setReport_count(obj[10] != null ? String.valueOf(obj[10]) : "");
					list.add(reportBorrowDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}



	/**
	 * 借报告、借发票导出
	 * 
	 * @param check_dep_name
	 *            检验部门
	 * @param borrow_start_date
	 *            外借开始日期
	 * @param borrow_end_date
	 *            外借结束日期
	 * @param borrow_type
	 *            外借类型（0：外借报告，1：外借发票，2：外借报告和发票）
	 * @return
	 * @author GaoYa
	 * @date 2014-06-10 上午11:24:00
	 */
	@SuppressWarnings("unchecked")
	public List<ReportBorrowDTO> exportZZJDBorrow(String check_dep_name,
			String borrow_start_date, String borrow_end_date, String borrow_type) {
		List<ReportBorrowDTO> list = new ArrayList<ReportBorrowDTO>();
		try {
			String sql = "select to_char(b.borrow_date,'yyyy-MM-dd') as borrow_date,nvl(b.invoice_no,' ') as invoice_no,t.report_com_name as COM_NAME,b.unpay_amount,b.com_name as borrow_com_name,b.fk_inspection_info_id "
					+ " from TZSB_REPORT_BORROW b,tzsb_inspection_info t,SYS_ORG o"
					+ " where (case instr(b.fk_inspection_info_id,',') when 0 then b.fk_inspection_info_id "
					+ " else substr(b.fk_inspection_info_id,0,instr(b.fk_inspection_info_id,',')-1) end)=t.ID ";
			if (StringUtil.isNotEmpty(check_dep_name)) {
				sql += " and o.org_name like '%" + check_dep_name + "%'";
			}
			if (StringUtil.isNotEmpty(borrow_start_date)) {
				sql += " and b.borrow_date >= to_date('" + borrow_start_date
						+ "','yyyy-MM-dd')";
			}
			if (StringUtil.isNotEmpty(borrow_end_date)) {
				sql += " and b.borrow_date <= to_date('" + borrow_end_date
						+ "','yyyy-MM-dd')";
			}/*
				 * else { sql += " and b.borrow_date <= trunc(sysdate)"; }
				 */
			if (StringUtil.isNotEmpty(borrow_type)) {
				sql += " and b.borrow_type = '" + borrow_type + "'";
			}
			sql += " and t.CHECK_UNIT_ID = o.id and t.data_status!='99' and t.fk_inspection_id is null and t.fk_tsjc_device_document_id is null order by b.invoice_no";
			List rslist = this.createSQLQuery(sql).list();
			if (!rslist.isEmpty()) {
				for (int i = 0; i < rslist.size(); i++) {
					Object[] objArr = rslist.toArray();
					Object[] obj = (Object[]) objArr[i];
					ReportBorrowDTO reportBorrowDTO = new ReportBorrowDTO();
					reportBorrowDTO.setBorrow_date(String.valueOf(obj[0]));
					reportBorrowDTO.setInvoice_no(String.valueOf(obj[1]));
					reportBorrowDTO.setCom_name(String.valueOf(obj[2]));
					reportBorrowDTO.setBorrow_com_name(String.valueOf(obj[4]));
					reportBorrowDTO.setUnpay_amount(String.valueOf(obj[3]));
					String fk_inspection_info_id = String.valueOf(obj[5]);
					String[] arr = fk_inspection_info_id.split(",");
					int report_count = 0;
					for (int j = 0; j < arr.length; j++) {
						if (StringUtil.isNotEmpty(arr[j])) {
							report_count++;
						}
					}
					reportBorrowDTO.setReport_count(String
							.valueOf(report_count));
					list.add(reportBorrowDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据部门ID获取对应的部门负责人姓名
	 * @param org_id -- 部门ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrg_mans(String org_id){
		List list = null;
		Map<String, Object> org_mans = new HashMap<String, Object>();
		String sql = "select u.org_id,u.name from SYS_ROLE t,sys_user_role t2,sys_user u,sys_org o ";
		sql += " where t.id = t2.role_id and t2.user_id=u.id and u.org_id=o.id and t.name like '%责任部门负责人%'";
		if (StringUtil.isNotEmpty(org_id)) {
			sql += " and u.org_id = ?";
			list = this.createSQLQuery(sql, org_id).list(); 
		}else{
			list = this.createSQLQuery(sql).list(); 
		}
		
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objArr = list.toArray();
				Object[] obj = (Object[]) objArr[i];
				org_mans.put(String.valueOf(obj[0]), obj[1]);
			}
		}
		return org_mans;
	}

	/**
	 * 部门报告流转统计
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2014-08-13 下午03:58:00
	 */
	@SuppressWarnings("unchecked")
	public List<InspectionInfoDTO> getReportCount(String dep_id) {
		List<InspectionInfoDTO> list = new ArrayList<InspectionInfoDTO>();
		try {
			conn = getConn();
			String sql = "SELECT days.da,nvl(advance.c,0),nvl(issu.v,0) FROM (SELECT t.advance_time,COUNT(*) AS c FROM TZSB_INSPECTION_INFO t"
					+ " WHERE (sysdate - t.advance_time <= 3 AND sysdate - t.advance_time > 0) "
					+ " AND t.advance_time IS NOT NULL and t.check_unit_id='"
					+ dep_id
					+ "' and t.data_status<>'99' GROUP BY t.advance_time ) advance,"
					+ " (SELECT t1.issue_date,COUNT(*) AS v FROM TZSB_INSPECTION_INFO t1"
					+ " WHERE (sysdate - t1.issue_date <= 3 AND sysdate - t1.issue_date > 0)"
					+ " AND t1.issue_date IS NOT NULL and t1.check_unit_id='"
					+ dep_id
					+ "' and t1.data_status<>'99' GROUP BY t1.issue_date) issu,"
					+ " (SELECT (trunc(sysdate)-rownum+1) da FROM dual CONNECT BY rownum -3 <= 0 ) days"
					+ " WHERE days.da = advance.advance_time(+) AND days.da = issu.issue_date(+) order by days.da desc";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()){
						InspectionInfoDTO inspectionInfoDTO = new InspectionInfoDTO();
						inspectionInfoDTO.setCur_date(DateUtil
								.convertStringToDate("yyyy-MM-dd", String
										.valueOf(rs.getDate(1))));
						inspectionInfoDTO.setDraft_count(Integer
								.parseInt(rs.getString(2)));
						inspectionInfoDTO.setIssue_count(Integer
								.parseInt(rs.getString(3)));
						list.add(inspectionInfoDTO);
			}
			closeConn();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
		}
		return list;
	}
	
	/**
	 * 新增收费业务与银行转账记录关联信息数据
	 * @param id -- ID
	 * @param fk_cw_bank_id -- 银行转账记录ID
	 * @param fk_inspection_pay_id -- 收费业务ID
	 * @param cur_used_money -- 本次收费金额
	 * @param pay_remark -- 备注
	 * 
	 * @return 
	 * @author GaoYa
	 * @date 2015-11-16 14:29:00
	 */
	public Object insertCwBank2Pay(final String id,
			final String fk_cw_bank_id, final String fk_inspection_pay_id, 
			final BigDecimal cur_used_money, final String pay_remark)
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
						String sql = "insert into TJY2_CW_BANK2PAY "
								+ "(id,fk_cw_bank_id,fk_inspection_pay_id,cur_used_money,pay_remark) "
								+ " values(?,?,?,?,?)"; 
						PreparedStatement statement 
							= conn.prepareStatement(sql);
						statement.setString(1, id);				// id
						statement.setString(2, fk_cw_bank_id);	// 银行转账记录ID
						statement.setString(3, fk_inspection_pay_id);	// 收费业务id
						statement.setBigDecimal(4, TS_Util.ratioTransform(cur_used_money.doubleValue()));	// 本次收费金额
						statement.setString(5, pay_remark);	// 备注
						statement.execute();
						statement.close();
					}
				});
				return null;
			}
		});
	}
	
	/**
	 * 删除收费业务与银行转账记录关联信息数据
	 * @param fk_cw_bank_id -- 银行转账记录ID
	 * @param fk_inspection_pay_id -- 收费业务ID
	 * 
	 * @return 
	 * @author GaoYa
	 * @date 2015-11-16 14:49:00
	 */
	public void delCwBank2Pay(String fk_cw_bank_id, String fk_inspection_pay_id){
		String sql = "delete from TJY2_CW_BANK2PAY where "
				+" FK_CW_BANK_ID = ? "
				+" and FK_INSPECTION_PAY_ID = ? " ;
		this.createSQLQuery(sql, fk_cw_bank_id, fk_inspection_pay_id).executeUpdate();
	}
	
	// 获取数据库连接
    public Connection getConn() {
        try {
            conn = Factory.getDB().getConnetion();
        } catch (Exception e) {
        	logger.error("获取数据库连接失败：" + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    // 关闭连接
    public void closeConn() {
        try {
            /*if (null != rs)
                rs.close();
            if (null != pstmt)
                pstmt.close();
            if (null != conn)
                conn.close();*/
        	Factory.getDB().freeConnetion(conn);	// 释放连接
        } catch (Exception e) {
        	logger.error("关闭数据库连接错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

	public   InspectionPayInfo getByInspectionInfoId(String id) {
		String hql = "from InspectionPayInfo where fk_inspection_info_id like ? ";
		InspectionPayInfo pay = (InspectionPayInfo) this.createQuery(hql, "%"+id+"%").uniqueResult();
		return pay;
		
	}
	
	/**
	 * 根据查询条件查询收费统计
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getPaymentList(HttpServletRequest request){
		String company_name = request.getParameter("company_name");
        String check_dep_id = request.getParameter("check_dep_id");
        String receive_man_name = request.getParameter("receive_man_name");
        String start_date = request.getParameter("start_date");
        String end_date = request.getParameter("end_date");
        String start_invoice_no = request.getParameter("start_invoice_no");
        String end_invoice_no = request.getParameter("end_invoice_no");
        String  invoice_no = request.getParameter("invoice_no");
        String  invoice_type = request.getParameter("invoice_type");
        String  pay_type = request.getParameter("pay_type");
        String  pay_received = request.getParameter("pay_received");
        
		StringBuffer sqlBuffer=new StringBuffer("");
		sqlBuffer.append("select t.ALIPAY_MOBILE as \"alipay_mobile\",t.BIG_CLASS as \"big_class\",t.CASH_PAY as \"cash_pay\",t.CHECK_DEP_ID as \"check_dep_id\",t.CHECK_DEP_NAME as \"check_dep_name\","
				+ "t.COMPANY_NAME as \"company_name\",t.HAND_IN as \"hand_in\",t.ID as \"id\",t.INVOICE_NO as \"invoice_no\",tb.name as \"invoice_type\",to_char(t.pay_date, 'yyyy-MM-dd')  as \"pay_date\",to_char(t.pay_date1, 'yyyy-MM-dd') as \"pay_date1\","
				+ "t.PAY_NO as \"pay_no\",t.PAY_RECEIVED as \"pay_received\",ta.name as \"pay_type\",t.POS as \"pos\",t.RECEIVE_MAN_NAME as \"receive_man_name\",t.REMARK as \"remark\","
				+ "t.WECHAT_MOBILE as \"wechat_mobile\" from PAYMENT_LIST_NEW  t left join (select cv1.name,cv1.value from PUB_CODE_TABLE_VALUES cv1 left join PUB_CODE_TABLE c1 on cv1.code_table_id=c1.id where c1.code='PAY_TYPE') ta on t.pay_type= ta.value "
				+ "left join (select cv2.name,cv2.value from PUB_CODE_TABLE_VALUES cv2 left join PUB_CODE_TABLE c2 on cv2.code_table_id=c2.id where c2.code='TJY2_CW_FP_TYPE') tb on t.invoice_type=tb.value where 1=1 ");
		Map<String,Object> paraMap=new HashMap<String,Object>();
		Map<String,Object[]> parasMap=new HashMap<String,Object[]>();
		if(StringUtil.isNotEmpty(company_name) ){
			sqlBuffer.append(" and t.company_name=:company_name");
			paraMap.put("company_name", company_name);
		}
		if(StringUtil.isNotEmpty(check_dep_id) ){
			sqlBuffer.append(" and t.check_dep_id=:check_dep_id");
			paraMap.put("check_dep_id", check_dep_id);
		}
		if(StringUtil.isNotEmpty(receive_man_name) ){
			String[] names=receive_man_name.split(",");
			sqlBuffer.append(" and t.receive_man_name in (:receive_man_name)");
			parasMap.put("receive_man_name",names);
		}
		if(StringUtil.isNotEmpty(start_date) ){
			sqlBuffer.append(" and ((t.pay_date>=to_date(:start_date, 'yyyy-MM-dd') and t.pay_date1 is null ) or t.pay_date1>=to_date(:start_date, 'yyyy-MM-dd'))");
			paraMap.put("start_date", start_date);
		}
		if(StringUtil.isNotEmpty(end_date) ){
			sqlBuffer.append(" and ((t.pay_date<=to_date(:end_date, 'yyyy-MM-dd') and t.pay_date1 is null ) or t.pay_date1<=to_date(:end_date, 'yyyy-MM-dd'))");;
			paraMap.put("end_date", end_date);
		}
		if(StringUtil.isNotEmpty(start_invoice_no) ){
			sqlBuffer.append(" and t.invoice_no>=:start_invoice_no");
			paraMap.put("start_invoice_no", start_invoice_no);
		}
		if(StringUtil.isNotEmpty(end_invoice_no) ){
			sqlBuffer.append(" and t.invoice_no<=:end_invoice_no");
			paraMap.put("end_invoice_no", end_invoice_no);
		}
		if(StringUtil.isNotEmpty(invoice_no) ){
			sqlBuffer.append(" and t.invoice_no=:invoice_no");
			paraMap.put("invoice_no", invoice_no);
		}
		if(StringUtil.isNotEmpty(invoice_type) ){
			String[] invoice_types=invoice_type.split(",");
			sqlBuffer.append(" and t.invoice_type in (:invoice_type)");
			parasMap.put("invoice_type",invoice_types);
		}
		if(StringUtil.isNotEmpty(pay_type) ){
			String[] pay_types=pay_type.split(",");
			sqlBuffer.append(" and t.pay_type in (:pay_type)");
			parasMap.put("pay_type",pay_types);
		}
		if(StringUtil.isNotEmpty(pay_received) ){
			sqlBuffer.append(" and t.pay_received=:pay_received");
			paraMap.put("pay_received", pay_received);
		}
		Query query =this.createSQLQuery(sqlBuffer.toString());
		for (Map.Entry<String, Object> entry : paraMap.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, Object[]> entry : parasMap.entrySet()) {
			query.setParameterList(entry.getKey(), entry.getValue());
		}
		List<Map<String, String>> list=query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	/**
	 * 根据发票号计算此发票实收总额（用于修改收费）
	 * @param invoice_no
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public double sumMoneyByInvoiceNo(String invoice_no) {
		double money = 0;
		String sql = "select sum(t.pay_received) " + 
				"  from TZSB_INSPECTION_PAY_INFO t " + 
				" where t.invoice_no = ? " + 
				"   and t.payment_status <> '99' ";
		List list = this.createSQLQuery(sql, invoice_no).list();
		if (!list.isEmpty()) {
			money = list.get(0)!=null?Double.parseDouble(list.get(0) + ""):0;
		}
		return money;
	}
}
