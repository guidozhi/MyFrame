package com.lsts.humanresources.Tjy2Ywfwbgzqrb.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.security.CurrentSessionUser;
import com.lsts.humanresources.Tjy2Ywfwbgzqrb.bean.Tjy2Ywfwbgzqrb;
import com.lsts.humanresources.Tjy2Ywfwbgzqrb.dao.Tjy2YwfwbgzqrbDao;
import com.lsts.log.service.SysLogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("Tjy2Ywfwbgzqrb")
public class Tjy2YwfwbgzqrbManager extends EntityManageImpl<Tjy2Ywfwbgzqrb, Tjy2YwfwbgzqrbDao> {
    private Logger logger = LoggerFactory.getLogger(Tjy2YwfwbgzqrbManager.class);
    @Autowired
    Tjy2YwfwbgzqrbDao tjy2YwfwbgzqrbDao;

    @Autowired
    private SysLogService logService;

    public void saveyw(HttpServletRequest request, CurrentSessionUser user, Tjy2Ywfwbgzqrb tjy2Ywfwbgzqrb) {
        // TODO Auto-generated method stub

        double XLGZ = XLGZ(tjy2Ywfwbgzqrb.getEducation());//学历
        tjy2Ywfwbgzqrb.setEducationMoney(XLGZ + "");


        double GWZW = GWZW(tjy2Ywfwbgzqrb.getJobs());//岗位职务
        tjy2Ywfwbgzqrb.setPostSalaryZw(GWZW + "");


        double GWCC = GWCC(tjy2Ywfwbgzqrb.getProfessional());//岗位职称
        System.out.println(tjy2Ywfwbgzqrb.getSalactName());
        tjy2Ywfwbgzqrb.setPostSalaryZc(GWCC + "");
        if (tjy2Ywfwbgzqrb.getSalactName().equals("zsry")) {//正职
            double xj = xj(XLGZ, GWZW, GWCC);
            tjy2Ywfwbgzqrb.setTotal(xj + "");
        } else if (tjy2Ywfwbgzqrb.getSalactName().equals("syry")) {//试用
            tjy2Ywfwbgzqrb.setTotal(syxj(XLGZ) + "");
        } else if (tjy2Ywfwbgzqrb.getSalactName().equals("sxqry")) {//实习
            tjy2Ywfwbgzqrb.setTotal(1500 + "");
        }
        tjy2Ywfwbgzqrb.setCreatTime(new Date());
        if (tjy2Ywfwbgzqrb.getId() == null || tjy2Ywfwbgzqrb.getId().equals("")) {
            tjy2Ywfwbgzqrb.setYesNo("未确认");
        }

        tjy2YwfwbgzqrbDao.save(tjy2Ywfwbgzqrb);
        // 写入系统日志
        try {
            logService.setLogs(tjy2Ywfwbgzqrb.getId(), "工资修改", tjy2Ywfwbgzqrb.getName() + "工资的最新修改人", user.getId(),
                    user.getName(),
                    new Date(), request.getRemoteAddr());
        } catch (Exception e) {
            System.out.println("@@@@@@@@@@@@@@@@@工资修改操作失败！！@@@@@@@@@@@@@@@@@@@@@@@");
            e.printStackTrace();
        }
    }


    public double xj(double XLGZ, double GWZW, double GWCC) {
        double xj = 0;

        if (GWZW > GWCC) {
            xj = XLGZ + GWZW;
        } else {
            xj = XLGZ + GWCC;
        }
        return xj;
    }

    public double syxj(double XLGZ) {
        double xj = XLGZ + 800;


        return xj;
    }

    /**
     * 学历工资
     */

    public double XLGZ(String a) {

        double f = 0;
        if (a != null) {
            if (a.equals("cz")) {
                f = 170;
            }
            if (a.equals("gz") || a.equals("zz")) {
                f = 188;
            }
            if (a.equals("dxzk")) {
                f = 251;
            }
            if (a.equals("dxbk")) {
                f = 299;
            }
            if (a.equals("sxwbg") || a.equals("yjs") || a.equals("whqssxwyjs")) {
                f = 355;
            }
            if (a.equals("ss")) {
                f = 419;
            }
            if (a.equals("bs")) {
                f = 535;
            }
        }
        return f;
    }

    /**
     * 岗位职务
     */
    public double GWZW(String a) {


        double f = 0;
        if (a != null) {
            if (a.equals("bmzz")) {
                f = 1160;
            }
            if (a.equals("bmfz")) {
                f = 1020;
            }
            if (a.equals("bmzl")) {
                f = 920;
            }
            if (a.equals("bsy")) {
                f = 850;
            }
            if (a.equals("gqg")) {
                f = 830;
            }
        }
        return f;
    }

    /**
     * 岗位职称
     */
    public double GWCC(String a) {

        double f = 0;
        if (a != null) {
            if (a.equals("zg")) {
                f = 2055;
            }
            if (a.equals("fg")) {
                f = 1460;
            }
            if (a.equals("gcs")) {
                f = 1090;
            }
            if (a.equals("zlgcs")) {
                f = 920;
            }
            if (a.equals("jsy")) {
                f = 850;
            }
        }

        return f;
    }


    //String转float
    public double flo(String a) {
        double sourceF = Double.valueOf(a);
        return sourceF;
    }

    public String GetIDs(String ids) {
        Tjy2Ywfwbgzqrb list = tjy2YwfwbgzqrbDao.GetIDs(ids).get(0);
        String id = list.getId();
        return id;
    }

    //删除工资信息
    public void delete(String id) {

        Tjy2Ywfwbgzqrb gz = tjy2YwfwbgzqrbDao.get(id);
        gz.setData_status("99");
        tjy2YwfwbgzqrbDao.save(gz);
    }

    /**
     * 设置离职标记
     *
     * @param nameId employee_base.id
     */
    public void setDimissionIdent(String nameId) {
        Tjy2Ywfwbgzqrb gz = tjy2YwfwbgzqrbDao.getGzByEmp(nameId);
        if (gz != null) {
            gz.setData_status("98");
            tjy2YwfwbgzqrbDao.save(gz);
        } else {
            this.logger.error("该员工没有工资信息,TJY2_RL_EMPLOYEE_BASE.id = {}", nameId);
        }
    }

    /**
     * 设置基本信息关键字修改标记
     *
     * @param nameId employee_base.id
     */
    public void setModifyIdent(String nameId) {
        Tjy2Ywfwbgzqrb gz = tjy2YwfwbgzqrbDao.getGzByEmp(nameId);
        if (gz != null) {
            gz.setYesNo("0");
            gz.setData_status("97");
            tjy2YwfwbgzqrbDao.save(gz);
        } else {
            this.logger.error("该员工没有工资信息,TJY2_RL_EMPLOYEE_BASE.id = {}", nameId);
        }
    }
}
