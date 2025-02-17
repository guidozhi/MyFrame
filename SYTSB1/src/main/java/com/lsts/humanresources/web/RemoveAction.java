package com.lsts.humanresources.web;

import com.khnt.core.crud.web.SpringSupportAction;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.lsts.humanresources.Tjy2Ywfwbgzqrb.dao.Tjy2YwfwbgzqrbDao;
import com.lsts.humanresources.Tjy2Ywfwbgzqrb.service.Tjy2YwfwbgzqrbManager;
import com.lsts.humanresources.bean.EmployeeBase;
import com.lsts.humanresources.bean.Remove;
import com.lsts.humanresources.bean.WorkExperience;
import com.lsts.humanresources.dao.Tjy2GjjDao;
import com.lsts.humanresources.service.EmployeeBaseManager;
import com.lsts.humanresources.service.RemoveManager;

import com.lsts.humanresources.service.Tjy2GjjManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;









import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("removeAction")
public class RemoveAction extends SpringSupportAction<Remove, RemoveManager> {

    @Autowired
    private RemoveManager  removeManager;
    @Autowired
    private EmployeeBaseManager employeeBaseManager;
    //移除员工信息
    @Autowired
    private Tjy2YwfwbgzqrbDao tjy2YwfwbgzqrbDao;

    @Autowired
    private Tjy2GjjDao tjy2GjjDao;

    @Autowired
	private Tjy2YwfwbgzqrbManager gzManager;
    @Autowired
	private Tjy2GjjManager gjjManager;
    
  	@RequestMapping(value = "addRemove")
  	@ResponseBody
  	public HashMap<String, Object> addRemove(Remove entity,String status,HttpServletRequest request)
  			throws Exception {
  		HashMap<String, Object> wrapper = new HashMap<String, Object>();
  		CurrentSessionUser user = SecurityUtil.getSecurityUser();
  		try{
  			EmployeeBase employeeBase=new EmployeeBase();
  			employeeBase=employeeBaseManager.get(entity.getFkEmployeeBaseId());
  			if(status.equals("0")){
  				employeeBase.setEmpStatus("5");
  				entity.setManner("1");
  				//解聘
				String id = entity.getId();
  				tjy2YwfwbgzqrbDao.deleteByEmployeeId(id);
  				tjy2GjjDao.deleteByEmployeeId(id);
  			}else if(status.equals("1")){
  				// 离退休
  				employeeBase.setEmpStatus("6");
  				entity.setManner("2");
  			} else if("3".equals(status)) {
  				// 离职
                // 标记员工基本信息empStatus-员工状态为离职
				employeeBase.setEmpStatus("8");
				// 标记Remove表中的manner-移除方式为离职
				entity.setManner("3");

				// 标识离职人员的工资信息及公积金信息
				this.setGzAndGjjIdent(employeeBase.getId());
			}
  			employeeBaseManager.save(employeeBase);
  			entity.setOperateMan(user.getName());
  			removeManager.save(entity);
  			wrapper.put("success", true);
  		}catch(Exception e){
  			log.error("保存移除信息：" + e.getMessage());
  			wrapper.put("success", false);
  			wrapper.put("message", "保存移除信息出错！");
  			e.printStackTrace();	
  		}
  		return wrapper;
  	}

	/**
	 * 标识离职人员的工资信息及公积金信息
	 * @param empId 离职人员的empId
	 */
	private void setGzAndGjjIdent(String empId) {
		this.gzManager.setDimissionIdent(empId);
		this.gjjManager.setDimissionIdent(empId);
	}
}