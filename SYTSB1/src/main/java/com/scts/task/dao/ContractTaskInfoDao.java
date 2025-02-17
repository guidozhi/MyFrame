package com.scts.task.dao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.khnt.core.crud.dao.impl.EntityDaoImpl;
import com.khnt.utils.StringUtil;
import com.scts.task.bean.ContractTaskInfo;

/**
 * 合同检验任务单数据处理对象
 * @ClassName ContractTaskInfoDao
 * @JDK 1.7
 * @author GaoYa
 * @date 2018-04-02 15:16:00
 */
@Repository("contractTaskInfoDao")
public class ContractTaskInfoDao extends EntityDaoImpl<ContractTaskInfo> {

	/**
	 * 获取所有在用检验任务单
	 * @return 
	 * @author GaoYa
	 * @date 2018-04-02 15:20:00
	 */
	@SuppressWarnings("unchecked")
	public List<ContractTaskInfo> getInfos() {
		List<ContractTaskInfo> list = new ArrayList<ContractTaskInfo>();
		String hql = "from ContractTaskInfo i where i.data_status != '99'";
		list = this.createQuery(hql).list();
		return list;
	}
	
	/**
	 * 根据承办部门ID获取部门检验任务单
	 * @param org_id -- 部门ID
	 * @return 
	 * @author GaoYa
	 * @date 2018-04-02 15:17:00
	 */
	@SuppressWarnings("unchecked")
	public List<ContractTaskInfo> queryInfos(
			String org_id) {
		List<ContractTaskInfo> list = new ArrayList<ContractTaskInfo>();
		String hql = "from ContractTaskInfo i where i.org_id=? and i.data_status != '99'";
		list = this.createQuery(hql, org_id).list();
		return list;
	}
	
	/**
	 * 获取任务单编号后三位序号
	 * @param sn_pre -- 任务单编号前缀
	 * @return 
	 * @author GaoYa
	 * @date 2018-04-02 15:17:00
	 */
	public String queryNextSn(String sn_pre){
		String nextNum = "";
		String sql = "select nvl(max(Substr(t.sn, length('" + sn_pre + "')+1)), '000') count_num" +
        " from CONTRACT_TASK_INFO t" +
        " where t.sn like '" + sn_pre + "%'";
		List list = this.createSQLQuery(sql).list();
		if (!list.isEmpty()) {
			String curNum = list.get(0)+"";
			if (StringUtil.isNotEmpty(curNum)) {
				nextNum = getCountNum(Integer.parseInt(curNum)+1);
			}
		}
		return nextNum;
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
	
	public List<Map<String, Object>> getCjsb(String ids){
		String sql="select t.* "
				+ " from TJY2_EQUIPMENT t "
				+ " where "
				+ "  t.id in('"+ids.trim().replaceAll(",", "','")+"')";
				//+ " order by t.created_date desc";
		List<Map<String, Object>> list=this.createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	public List<Map<String, Object>> getbgxx(String id){
		String sql="SELECT t1.id,t1.report_sn,t2.DEVICE_REGISTRATION_CODE,t2.DEVICE_NAME,  t1.LAST_CHECK_TIME, t8.name fee_status, t1.enter_op_name, t1.receivable,t1.device_model "
				+ " FROM CONTRACT_TASK_INFO t, TZSB_INSPECTION_INFO t1 , BASE_DEVICE_DOCUMENT t2 "
				+ " ,(select c1.* from PUB_CODE_TABLE c, PUB_CODE_TABLE_VALUES c1 where c.id = c1.code_table_id  and c.code = 'PAYMENT_STATUS') t8"
				+ " WHERE t1.is_flow ='2' and t.id=t1.contract_task_id AND t2.id = t1.fk_tsjc_device_document_id AND  t1.fee_status = t8.value(+) and t.id=? and  t1.data_status<>'100'";
		List<Map<String, Object>> list=this.createSQLQuery(sql,id).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	public ContractTaskInfo getContractTaskInfoBySn(String sn,String org){
		String hql="from ContractTaskInfo where data_status <> 99 and sn=:sn and (org_id=:org_id or cjry_id like '%"+org+"%')";
		Query query=this.createQuery(hql);
		query.setParameter("sn", sn);
		query.setParameter("org_id", org);
		ContractTaskInfo info=(ContractTaskInfo)query.uniqueResult();
		return info;
	}
}
