package com.lsts.humanresources.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.khnt.base.Factory;
import com.khnt.core.crud.dao.impl.EntityDaoImpl;
import com.khnt.rbac.impl.bean.User;
import com.khnt.utils.StringUtil;
import com.lsts.constant.Constant;
import com.lsts.humanresources.bean.WxLeave;
/**
 * <p>
 * 
 * </p>
 * 
 * @ClassName Tjy2BgLeaveDao
 * @JDK 1.5
 * @author 
 * @date  
 */
@Repository("WxLeaveDao")
public class WxLeaveDao extends EntityDaoImpl<WxLeave> {
	//已请假种类及天数
	public List<?> queryLeave(HttpServletRequest request,String peopleId,String startDate)throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date start=sdf.parse(startDate);
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(start);
		int year = calendar.get(Calendar.YEAR);
		String dateCycles = Factory.getSysPara().getProperty("TJY2_RL_ANNUAL_LEAVE_CYCLE_"+year);
		if(StringUtil.isEmpty(dateCycles)) {
			if(year==2018) {
				dateCycles = Constant.TJY2_RL_ANNUAL_LEAVE_CYCLE_2018;
			}else if(year==2019) {
				dateCycles = Constant.TJY2_RL_ANNUAL_LEAVE_CYCLE_2019;
			}else if(year==2020) {
				dateCycles = Constant.TJY2_RL_ANNUAL_LEAVE_CYCLE_2020;
			}
		}
		String[] dateCycle = dateCycles.split(",");
		String lastDate = dateCycle[0];//去年指定的日期
		String date = dateCycle[1];//今年指定的日期
		String nextDate = dateCycle[2];//明年指定的日期
		java.util.Date givenDate=sdf.parse(date);  
	    String sql=null;
		if(start.getTime() < givenDate.getTime()){
			sql = "select sum(t.leave_count2),t.leave_type from TJY2_RL_LEAVE t where t.people_id=? "+ 
					"and  t.apply_status in ('SPTG','YXJ') "+
					"and  to_char(t.start_date,'yyyy-MM-dd') >= '"+lastDate+"' "+
			        "and  to_char(t.start_date,'yyyy-MM-dd') <'"+date+"' "+
					"group by t.leave_type";
		}else if(start.getTime() >= givenDate.getTime()){
			sql = "select sum(t.leave_count2),t.leave_type from TJY2_RL_LEAVE t where t.people_id=? "+ 
					"and  t.apply_status in ('SPTG','YXJ') "+
					"and  to_char(t.start_date,'yyyy-MM-dd') >= '"+date+"' "+
			        "and  to_char(t.start_date,'yyyy-MM-dd') <'"+nextDate+"' "+
					"group by t.leave_type";
		}
		List<?> list = createSQLQuery(sql,peopleId).list();
		return list;
    }
	//查询角色所拥有的权限
	public List<?> getUserPower(String peopleId){
		String sql = "select t.name from SYS_ROLE t ,sys_user_role r,sys_user u,employee e "+
					"where  r.role_id=t.id and u.id=r.user_id and e.id=u.employee_id and u.employee_id = ?";
		List<?> list = this.createSQLQuery(sql,peopleId).list();
		return list;
	}
	//根据员工ID查询用户信息
	public User getUser(String peopleId){
		String sql = "select * from SYS_USER where EMPLOYEE_ID =?";
		List<?> list = this.createSQLQuery(sql,peopleId).list();
		User user = new User();
		if(list.size()>0&&list!=null){
			Object obj =list.get(0);
			Object[] objs=(Object[]) obj;
			user.setId(objs[0].toString());
			user.setAccount(objs[1].toString());
			user.setPassword(objs[2].toString());
			user.setName(objs[5].toString());
		}
		return user;
	}
	//根据电话查询account
	public List<?> getAccount(String phone){
		List<?> list = new ArrayList<>();
		String sql = "select t.account from SYS_USER t,EMPLOYEE e where t.employee_id = e.id and e.mobile_tel = ? and t.status = '1' ";
		try {
			list = this.createSQLQuery(sql,phone).list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
  	 * 查询流程id
  	 * */
	@SuppressWarnings("rawtypes")
	public List getactivityId(String id){
		String sql = "select t.activity_id from TJY2_RL_LEAVE b, v_pub_worktask t where b.id=t.SERVICE_ID and t.WORK_TYPE like '%TJY2_RL_LEAVE%' and t.STATUS='0' and b.id=?";
		List list = this.createSQLQuery(sql,id).list();
		return list;
	}
	//通过请休假ID查询流程主键ID
	public  String queryMainId(HttpServletRequest request, String id) throws Exception {
		String mainId = null;
		String sql = "select t.ID from v_pub_worktask t where t.SERVICE_ID = ? and t.STATUS='0'";
		List<?> list = this.createSQLQuery(sql,id).list();
		if(list!=null && !list.isEmpty()){
			mainId = list.get(0).toString();
		}
		return mainId;
	}
	//通过请休假ID查询flowId
	public  String queryFlowId(String id) throws Exception {
		String flowId = null;
		String sql = "select t.FLOW_NUM_ID from v_pub_worktask t where t.SERVICE_ID = ? and t.STATUS='0'";
		List<?> list = this.createSQLQuery(sql,id).list();
		if(list!=null && !list.isEmpty()){
			flowId = list.get(0).toString();
		}
		return flowId;
	}
	//通过业务ID查询流程环节
	public  String getRemark(String business_id,String hander_id) throws Exception {
		String remark = null;
		String sql = "select t.REMARK from V_PUB_WORKTASK t "+
					"where t.SERVICE_ID=?  and t.STATUS='0' "+//and t.WORK_TYPE='TJY2_RL_LEAVE'
					"and t.HANDLER_ID='"+hander_id+"'";
				/*"select T.REMARK from  PUB_WORKTASK T "+ 
				"WHERE exists (select 1 from  FLOW_PROCESS P, FLOW_ACTIVITY A where "+
				"T.SERVICE_ID = A.ID "+
				"AND A.PROCESS = P.ID "+
				"and p.BUSINESS_ID=? "+
				") and T.WORK_TYPE='TJY2_RL_LEAVE' and T.STATUS='0' and T.HANDLER_ID='"+hander_id+"'";*/
		System.out.println(sql);
		List<?> list = this.createSQLQuery(sql,business_id).list();
		if(list.size()<=0){
			sql = "select t.REMARK from v_pub_worktask_add_position t "+
					"where t.SERVICE_ID=?  and t.STATUS='0' "+//and t.WORK_TYPE='TJY2_RL_LEAVE'
					"and t.HANDLER_ID='"+hander_id+"'";
			list = this.createSQLQuery(sql,business_id).list();
		}
		if(list!=null && !list.isEmpty()){
			remark = list.get(0).toString();
		}
		return remark;
	}
	//通过业务ID查询流程环节
		public  String getRemarkGwwc(String business_id,String hander_id) throws Exception {
			String remark = null;
			String sql = "select t.REMARK from V_PUB_WORKTASK t "+
						"where t.SERVICE_ID=?  and t.STATUS='0' "+ // and t.WORK_TYPE='TJY2_RL_GWWC'
						"and t.HANDLER_ID='"+hander_id+"'";
					/*"select T.REMARK from  PUB_WORKTASK T "+ 
					"WHERE exists (select 1 from  FLOW_PROCESS P, FLOW_ACTIVITY A where "+
					"T.SERVICE_ID = A.ID "+
					"AND A.PROCESS = P.ID "+
					"and p.BUSINESS_ID=? "+
					") and T.WORK_TYPE='TJY2_RL_LEAVE' and T.STATUS='0' and T.HANDLER_ID='"+hander_id+"'";*/
			System.out.println(sql);
			List<?> list = this.createSQLQuery(sql,business_id).list();
			if(list!=null && !list.isEmpty()){
				remark = list.get(0).toString();
			}
			return remark;
		}
}