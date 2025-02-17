package com.lsts.humanresources.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository; 
import com.khnt.core.crud.dao.impl.EntityDaoImpl;
import com.lsts.equipment2.bean.EquipmentBuy;
import com.lsts.humanresources.bean.RemindMessage;
/**
 * <p>
 * 
 * </p>
 * 
 * @ClassName RemindMessageDao
 * @JDK 1.5
 * @author 
 * @date  
 */
@Repository("remindMessageDao")
public class RemindMessageDao extends EntityDaoImpl<RemindMessage> {

	/**
  	 * 根据员工ID获取消息提醒信息
  	 */
  	public RemindMessage getRMessage(String fkRlEmplpyeeId){
  		List<RemindMessage> list = new ArrayList<RemindMessage>();
  		RemindMessage remindMessage = new RemindMessage();
  		try {
			String hql = "from RemindMessage  where fkRlEmplpyeeId = ?";
			list = this.createQuery(hql,fkRlEmplpyeeId).list();
			System.out.println(list.size());
			if(list.size()==0){
				remindMessage=null;
			}else if(list.size()==1){
				remindMessage = list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return remindMessage;
    }
  	/**
  	 * 根据系统时间获取将要发送生日提醒的对象集合
  	 */
	public List<?> getBirthdayRemind(int day){
  		List<?> list = new ArrayList<RemindMessage>();
  		try {
  			//生日当月以及生日前半个月和前一天
			String sql="select r.* from tjy2_rl_employee_base e,TJY2_RL_REMIND_MESSAGE r "
					+"where r.fk_rl_emplpyee_id=e.id and to_char(sysdate,'MM') = substr(e.emp_id_card, 11, 2) "
					+"and r.birthday_remind_time='1' and e.emp_status in(3,4)"
					+"union select r.* from tjy2_rl_employee_base e,TJY2_RL_REMIND_MESSAGE r "
					+"where r.fk_rl_emplpyee_id=e.id and to_char(sysdate+15,'MM-dd') =(substr(e.emp_id_card, 11, 2) || '-' || substr(e.emp_id_card, 13, 2)) "
					+"and  r.birthday_remind_time='2' and e.emp_status in(3,4)"
				    +"union select r.* from tjy2_rl_employee_base e,TJY2_RL_REMIND_MESSAGE r "
					+"where r.fk_rl_emplpyee_id=e.id and to_char(sysdate+1,'MM-dd') =(substr(e.emp_id_card, 11, 2) || '-' || substr(e.emp_id_card, 13, 2)) "
				    +"and r.birthday_remind_time='3' and e.emp_status in(3,4)";
			//生日前半个月和前一天
			String sql1="select r.* from tjy2_rl_employee_base e,TJY2_RL_REMIND_MESSAGE r "
					+"where r.fk_rl_emplpyee_id=e.id and to_char(sysdate+15,'MM-dd') =(substr(e.emp_id_card, 11, 2) || '-' || substr(e.emp_id_card, 13, 2)) "
					+"and  r.birthday_remind_time='2' and e.emp_status in(3,4)"
				    +"union select r.* from tjy2_rl_employee_base e,TJY2_RL_REMIND_MESSAGE r "
					+"where r.fk_rl_emplpyee_id=e.id and to_char(sysdate+1,'MM-dd') =(substr(e.emp_id_card, 11, 2) || '-' || substr(e.emp_id_card, 13, 2)) "
				    +"and r.birthday_remind_time='3' and e.emp_status in(3,4)";
			if(day==1){
				list = this.createSQLQuery(sql).list();
			}else{
				list = this.createSQLQuery(sql1).list();
			}
  		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
    }
	/**
  	 * 根据系统时间获取将要发送退休提醒的对象集合
  	 */
	public List<?> getRetireRemind(){
  		List<?> list = new ArrayList<RemindMessage>();
  		try {
			//退休前半年和六个月
			String sql="select r.* from tjy2_rl_employee_base e,TJY2_RL_REMIND_MESSAGE r "
					+"where r.fk_rl_emplpyee_id=e.id "
					+"and trunc((to_char(add_months(sysdate,3),'yyyyMMdd')-to_char(to_date(substr(e.emp_id_card,7,8),'yyyy-MM-dd'),'yyyyMMdd'))/10000)='60' "
					+"and  r.retire_remind_time='1' and e.emp_sex='1' and e.emp_status ='4' "
					+"union "
					+"select r.* from tjy2_rl_employee_base e,TJY2_RL_REMIND_MESSAGE r "
					+"where r.fk_rl_emplpyee_id=e.id "
					+"and trunc((to_char(add_months(sysdate,3),'yyyyMMdd')-to_char(to_date(substr(e.emp_id_card,7,8),'yyyy-MM-dd'),'yyyyMMdd'))/10000)='50' "
					+"and  r.retire_remind_time='1' and e.emp_sex='0' and e.emp_status ='4' "
					+"union "
					+"select r.* from tjy2_rl_employee_base e,TJY2_RL_REMIND_MESSAGE r "
					+"where r.fk_rl_emplpyee_id=e.id "
					+"and trunc((to_char(add_months(sysdate,6),'yyyyMMdd')-to_char(to_date(substr(e.emp_id_card,7,8),'yyyy-MM-dd'),'yyyyMMdd'))/10000)='60' "
					+"and r.retire_remind_time='2' and e.emp_sex='1' and e.emp_status ='4'"
					+"union "
					+"select r.* from tjy2_rl_employee_base e,TJY2_RL_REMIND_MESSAGE r "
					+"where r.fk_rl_emplpyee_id=e.id "
					+"and trunc((to_char(add_months(sysdate,6),'yyyyMMdd')-to_char(to_date(substr(e.emp_id_card,7,8),'yyyy-MM-dd'),'yyyyMMdd'))/10000)='50' "
					+"and r.retire_remind_time='2' and e.emp_sex='0' and e.emp_status ='4'";
			String sql1 = "select r.* from (\n" +
					"select id\n" +
					"  from (\n" +
					"         select T.*, trunc(RETIRED_DATE - sysdate) DAYS, to_char(sysdate, 'yyyy') - substr" +
					"(EMP_ID_CARD, 7, 4) as AGE\n" +
					"           from (\n" +
					"                  select BASE.*,\n" +
					"                         case\n" +
					"                           when POSITION = '1' then\n" +
					"                             case\n" +
					"                               when EMP_SEX = '1' then add_months(to_date(substr(EMP_ID_CARD, 7, " +
					"8), 'yyyy-mm-dd'), 660)\n" +
					"                               else add_months(to_date(substr(EMP_ID_CARD, 7, 8), 'yyyy-mm-dd'), " +
					"600) end\n" +
					"                           else\n" +
					"                             case\n" +
					"                               when EMP_SEX = '1' then add_months(to_date(substr(EMP_ID_CARD, 7, " +
					"8), 'yyyy-mm-dd'), 720)\n" +
					"                               else add_months(to_date(substr(EMP_ID_CARD, 7, 8), 'yyyy-mm-dd'), " +
					"660) end\n" +
					"                           end RETIRED_DATE\n" +
					"                    from TJY2_RL_EMPLOYEE_BASE BASE\n" +
					"                   where (BASE.RETIRED_WARN_EXCEPT = '0' or BASE.WORK_TITLE_WARN_EXCEPT is null)" +
					"\n" +
					"                ) T\n" +
					"          order by DAYS) KHNT\n" +
					" where KHNT.EMP_STATUS = '4'\n" +
					"   and KHNT.DAYS = '90') e,TJY2_RL_REMIND_MESSAGE r where r.FK_RL_EMPLPYEE_ID=e.id and r.RETIRE_REMIND_TIME = '2'";
			list = this.createSQLQuery(sql1).list();
  		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
    }
	/**
  	 * 根据系统时间获取将要发送聘用到期提醒的对象集合
  	 */
	public List<?> getStopRemind(){
  		List<?> list = new ArrayList<RemindMessage>();
  		try {
			//聘用到期前三个月、一个月、半个月
			String sql="select r.* from tjy2_rl_employee_base e,TJY2_RL_REMIND_MESSAGE r,TJY2_RL_CONTRACT t "
					+"where r.fk_rl_emplpyee_id=e.id and e.id=t.fk_employee_id "
					+"and to_char(sysdate+15,'yyyy-MM-dd')=to_char(t.contract_stop_date,'yyyy-MM-dd') "
					+"and  r.retire_remind_time='1' and e.emp_status ='4'"
					+"union "
					+"select r.* from tjy2_rl_employee_base e,TJY2_RL_REMIND_MESSAGE r,TJY2_RL_CONTRACT t "
					+"where r.fk_rl_emplpyee_id=e.id and e.id=t.fk_employee_id "
					+"and to_char(add_months(sysdate,1),'yyyy-MM-dd')=to_char(t.contract_stop_date,'yyyy-MM-dd') "
					+"and  r.retire_remind_time='2' and e.emp_status ='4'"
					+"union "
					+"select r.* from tjy2_rl_employee_base e,TJY2_RL_REMIND_MESSAGE r,TJY2_RL_CONTRACT t "
					+"where r.fk_rl_emplpyee_id=e.id and e.id=t.fk_employee_id "
					+"and to_char(add_months(sysdate,3),'yyyy-MM-dd')=to_char(t.contract_stop_date,'yyyy-MM-dd') "
					+"and r.retire_remind_time='3' and e.emp_status ='4'";
			list = this.createSQLQuery(sql).list();
  		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
    }
}