package com.lsts.humanresources.service;

import com.khnt.base.Factory;
import com.khnt.bpm.ext.service.FlowExtManager;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.pub.fileupload.bean.Attachment;
import com.khnt.pub.fileupload.service.AttachmentManager;
import com.khnt.security.CurrentSessionUser;
import com.khnt.utils.StringUtil;
import com.lsts.advicenote.service.MessageService;
import com.lsts.constant.Constant;
import com.lsts.humanresources.bean.BgLeave;
import com.lsts.humanresources.dao.BgLeaveDao;
import com.lsts.humanresources.dao.EmployeeBaseDao;
import com.lsts.log.bean.SysLog;
import com.lsts.log.dao.SysLogDao;
import com.lsts.qualitymanage.bean.QualityUpdateFile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Service("BgLeaveManager")
public class BgLeaveManager extends EntityManageImpl<BgLeave, BgLeaveDao> {
    private Logger logger = LoggerFactory.getLogger(BgLeaveManager.class);
    @Autowired
    BgLeaveDao bgLeaveDao;
    @Autowired
    FlowExtManager flowExtManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    EmployeeBaseDao employeeBaseDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SysLogDao sysLogDao;
    /** 状态常量 */
    public final static String LEAVE_FLOW_WTJ = "WJY"; //未提交
    public final static String LEAVE_FLOW_YTJ = "YTJ"; //已提交
    public final static String LEAVE_FLOW_SHZ = "SPZ"; //审批中
    public final static String LEAVE_FLOW_SHTG = "SPTG"; //审批通过
    public final static String LEAVE_FLOW_SHBTG = "SPBTG"; //审批不通过
    public final static String LEAVE_FLOW_CXZ = "CXZ"; //撤销中
    public final static String LEAVE_FLOW_YCX = "YCX"; //已撤销

    /**
     * 启动流程
     *
     * @param map
     * @throws Exception
     */

    public void doStartPress(Map<String, Object> map) throws Exception {
        flowExtManager.startFlowProcess(map);
    }

    /**
     * 审核
     */

    public void doProcess(Map<String, Object> map) throws Exception {
        flowExtManager.submitActivity(map);
    }

    /**
     * 流程结束
     */
    public void stop(Map<String, Object> map) throws Exception {
        flowExtManager.finishProcess(map);
    }

    /**
     * 保存休假请假申请以及附件
     */
    public void saveEntyandFiles(BgLeave bgLeave, String uploadFiles) {
        //保存申请
        bgLeaveDao.save(bgLeave);
        //保存附件
        if (StringUtil.isNotEmpty(uploadFiles)) {
            String[] files = uploadFiles.replaceAll("/^,/", "").split(",");
            for (String file : files) {
                if (StringUtil.isNotEmpty(file)) {
                    attachmentManager.setAttachmentBusinessId(file, bgLeave.getId());
                }
            }
        }
    }

    /**
     * 获取附件
     *
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "detail1")
    @ResponseBody
    public List<Attachment> queryFiles(HttpServletRequest request, String id) throws Exception {
        List<Attachment> list = attachmentManager.getBusAttachment(id);
        return list;
    }

    /**
     * 已请假种类及天数
     */
    public String queryLeave(HttpServletRequest request, String peopleId, String startDate) throws Exception {
        String leaveInfo = "";
        String totalDay = "";
        try {
            totalDay = employeeBaseDao.get(peopleId).getTotalDays();
            if (totalDay != null && totalDay != "") {
                BigDecimal totalDays = new BigDecimal(Integer.parseInt(totalDay));
                //初始化
                BigDecimal leave_total = new BigDecimal(0);
                List<?> list = bgLeaveDao.queryLeave(request, peopleId, startDate);
                if (list.size() > 0 && list != null) {
                    for (Object obj : list) {
                        Object[] objs = (Object[]) obj;
                        if (((String) objs[1]).equals("GWWC")) {
                            leave_total = leave_total.add((BigDecimal) objs[0]);
                            leaveInfo += "公务外出：" + objs[0] + "天，";
                        } else if (((String) objs[1]).equals("NJ")) {
                            leave_total = leave_total.add((BigDecimal) objs[0]);
                            leaveInfo += "年假：" + objs[0] + "天， ";
                            totalDays = totalDays.subtract((BigDecimal) objs[0]);
                        } else if (((String) objs[1]).equals("SHIJ")) {
                            leave_total = leave_total.add((BigDecimal) objs[0]);
                            leaveInfo += "事假：" + objs[0] + "天，";
                        } else if (((String) objs[1]).equals("HJ")) {
                            leave_total = leave_total.add((BigDecimal) objs[0]);
                            leaveInfo += "婚假：" + objs[0] + "天，";
                        } else if (((String) objs[1]).equals("CJ")) {
                            leave_total = leave_total.add((BigDecimal) objs[0]);
                            leaveInfo += "产假：" + objs[0] + "天， ";
                        } else if (((String) objs[1]).equals("TQJ")) {
                            leave_total = leave_total.add((BigDecimal) objs[0]);
                            leaveInfo += "探亲假：" + objs[0] + "天， ";
                        } else if (((String) objs[1]).equals("BJ")) {
                            leave_total = leave_total.add((BigDecimal) objs[0]);
                            leaveInfo += "病假：" + objs[0] + "天， ";
                        } else if (((String) objs[1]).equals("SANGJ")) {
                            leave_total = leave_total.add((BigDecimal) objs[0]);
                            leaveInfo += "丧假：" + objs[0] + "天， ";
                        } else if (((String) objs[1]).equals("PCJ")) {
                            leave_total = leave_total.add((BigDecimal) objs[0]);
                            leaveInfo += "陪产假：" + objs[0] + "天， ";
                        } else if (((String) objs[1]).equals("OTHER")) {
                            leave_total = leave_total.add((BigDecimal) objs[0]);
                            leaveInfo += "其他：" + objs[0] + "天， ";
                        }
                    }
                    leaveInfo += "合计：" + leave_total + "天；剩余年假天数：" + totalDays + "天。";
                    System.out.println(leaveInfo);
                } else {
                    leaveInfo += "今年暂无请假记录！";
                }
            } else {
                leaveInfo = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            leaveInfo = "1";
        }
        return leaveInfo;
    }

    /**
     * 查询角色所拥有的权限
     */
    public List<?> getUserPower(String peopleId) {
        List<?> list = bgLeaveDao.getUserPower(peopleId);
        return list;
    }

    /**
     * 通过请休假ID查询流程主键ID
     *
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    public String queryMainId(HttpServletRequest request, String id) throws Exception {
        String mainId = bgLeaveDao.queryMainId(request, id);
        return mainId;
    }

    /**
     * 通过请休假ID查询PROCESS_ID
     *
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    public String queryProcessId(HttpServletRequest request, String id) throws Exception {
        String processId = bgLeaveDao.queryProcessId(request, id);
        return processId;
    }

    /**
     * 通过请休假ID查询ACTIVITY_ID
     *
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    public String queryActivityId(HttpServletRequest request, String id) throws Exception {
        String activityId = bgLeaveDao.queryActivityId(request, id);
        return activityId;
    }

    /**
     * 通过ID查询图片
     *
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    public void yzImage(HttpServletRequest request, String id) throws Exception {
        bgLeaveDao.yzImage(request, id);

    }

    //计算段时间不含周末及法定节假日的天数
    public int countDays(HttpServletRequest request, Date startDate, Date endDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = sdf.format(startDate);
        String endTime = sdf.format(endDate);
        int days = 0;
        List<?> list = bgLeaveDao.countDays(request, startTime, endTime);
        if (list.size() > 0 && list != null) {
            Object object = list.get(0);
            System.out.println(object);
            days = ((BigDecimal) object).intValue();
        }
        return days;
    }

    //计算段时间不含周末及法定节假日的天数
    public Boolean getLeave(HttpServletRequest request, String peopleId, Date startDate, Date endDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = sdf.format(startDate);
        String endTime = sdf.format(endDate);
        Boolean isexist = false;
        List<?> list = bgLeaveDao.getLeave(request, peopleId, startTime, endTime);
        if (list.size() > 0 && list != null) {
            isexist = true;
        }
        return isexist;
    }

    /**
     * 销假提醒定时发送
     *
     * @return
     * @throws Exception
     */
    public void sendResumeRemind() {
        System.out.println("触发成功！");
        HttpServletRequest request = null;
        String message = "";
        //获取销假提醒对象集合
        List<?> list = bgLeaveDao.sendResumeRemind();
        //遍历获取发送内容并发送信息
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] objs = (Object[]) list.get(i);
                String empId = (String) objs[1];
                String empName = (String) objs[2];
                String depId = (String) objs[3];
                String depName = (String) objs[4];
                message += "【" + depName + "】-" + empName + "，";
            }
            message += "还未销假，请及时通知请（休）假人销假！";
            //微信、短信发送给力资源部陈俐宏
            String mobile = employeeBaseDao.get("402883a046cbb20a0146cbbe550c0034").getEmpPhone();
            messageService.sendWxMsg(request,
                    "销假提醒", Constant.PEOPLE_CORPID, Constant.PEOPLE_PWD, message, mobile);
            messageService.sendMoMsg(request, "销假提醒", message, mobile);
        }
    }

    /**
     * 剩余年假天数
     */
    public String queryYearDays(HttpServletRequest request, String peopleId) throws Exception {
        String yearDay = "";
        try {
            yearDay = employeeBaseDao.get(peopleId).getTotalDays();
            if (yearDay != null && yearDay != "") {
                BigDecimal yearDays2 = new BigDecimal(Integer.parseInt(yearDay));//总年假天数
                String yearDays = bgLeaveDao.queryYearDays(request, peopleId);//已休假天数
                BigDecimal yearDays1 = new BigDecimal(Integer.parseInt(yearDays));
                BigDecimal yearDay1 = yearDays2.subtract(yearDays1);
                yearDay = yearDay1.toString();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return yearDay;
    }

    /**
     * 获取下一步审核人信息
     *
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    public String getAuditor(String id, String sign) throws Exception {
        String telphone = null;
        BgLeave bgLeave = bgLeaveDao.get(id);
        try {
            telphone = bgLeaveDao.getAuditor(bgLeave, sign);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return telphone;
    }

    /**
     * 统计请休假
     *
     * @return
     */
    public List<?> getLeaveInfoCount(String org_id, String userName, String startDate, String lastDate) {
        List<Object> leaveInfoList = new ArrayList<Object>();
        List<?> infoList = employeeBaseDao.getLeaveBaseInfo(org_id, userName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        for (int i = 0; i < infoList.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            Object[] obj = (Object[]) infoList.get(i);
            Calendar now = Calendar.getInstance();
            String org_name = (String) obj[0];//部门名称
            String emp_name = (String) obj[1];//员工姓名
            String peopleId = (String) obj[2];//员工ID
            String into_work_date = "";
            Date into_work_date_raw = (Date) obj[3];//进院时间
            if (into_work_date_raw != null) {
                now.setTime(into_work_date_raw);
                into_work_date =
						String.valueOf(now.get(Calendar.YEAR)) + "." + String.valueOf(now.get(Calendar.MONTH) + 1);
            }
            String work_age = obj[4] == null ? "" : String.valueOf(obj[4]);//工龄(年)
            String total_days = obj[5] == null ? "" : String.valueOf(obj[5]);//应休假天数
            int nj_days_used = 0;//已休年假
            String nj_days_used_date = "";//休假时间
            String nj_days_left = "";//剩余年假
            String other_days_used = "";//其他已休
            String other_days_used_date = "";//休假时间
            String remark = "";//备注

            List<BgLeave> leaveList = bgLeaveDao.count(peopleId, startDate, lastDate);
            for (BgLeave bgLeave : leaveList) {
                if (bgLeave.getLeaveType().equals("NJ")) {
                    nj_days_used = nj_days_used + Integer.valueOf(bgLeave.getLeaveCount2());
                    nj_days_used_date =
							nj_days_used_date + sdf.format(bgLeave.getStartDate()) + "-" + sdf.format(bgLeave.getEndDate()) + "，";
                } else if (bgLeave.getLeaveType().equals("SHIJ")) {
                    other_days_used = other_days_used + bgLeave.getLeaveCount2() + "(事假)，";
                    other_days_used_date =
							other_days_used_date + sdf.format(bgLeave.getStartDate()) + "-" + sdf.format(bgLeave.getEndDate()) + "(事假)，";
                } else if (bgLeave.getLeaveType().equals("HJ")) {
                    other_days_used = other_days_used + bgLeave.getLeaveCount2() + "(婚假)，";
                    other_days_used_date =
							other_days_used_date + sdf.format(bgLeave.getStartDate()) + "-" + sdf.format(bgLeave.getEndDate()) + "(婚假)，";
                } else if (bgLeave.getLeaveType().equals("CJ")) {
                    other_days_used = other_days_used + bgLeave.getLeaveCount2() + "(产假)，";
                    other_days_used_date =
							other_days_used_date + sdf.format(bgLeave.getStartDate()) + "-" + sdf.format(bgLeave.getEndDate()) + "(产假)，";
                } else if (bgLeave.getLeaveType().equals("TQJ")) {
                    other_days_used = other_days_used + bgLeave.getLeaveCount2() + "(探亲假)，";
                    other_days_used_date =
							other_days_used_date + sdf.format(bgLeave.getStartDate()) + "-" + sdf.format(bgLeave.getEndDate()) + "(探亲假)，";
                } else if (bgLeave.getLeaveType().equals("BJ")) {
                    other_days_used = other_days_used + bgLeave.getLeaveCount2() + "(病假)，";
                    other_days_used_date =
							other_days_used_date + sdf.format(bgLeave.getStartDate()) + "-" + sdf.format(bgLeave.getEndDate()) + "(病假)，";
                } else if (bgLeave.getLeaveType().equals("SANGJ")) {
                    other_days_used = other_days_used + bgLeave.getLeaveCount2() + "(丧假)，";
                    other_days_used_date =
							other_days_used_date + sdf.format(bgLeave.getStartDate()) + "-" + sdf.format(bgLeave.getEndDate()) + "(丧假)，";
                } else if (bgLeave.getLeaveType().equals("PCJ")) {
                    other_days_used = other_days_used + bgLeave.getLeaveCount2() + "(陪产假)，";
                    other_days_used_date =
							other_days_used_date + sdf.format(bgLeave.getStartDate()) + "-" + sdf.format(bgLeave.getEndDate()) + "(陪产假)，";
                } else if (bgLeave.getLeaveType().equals("OTHER")) {
                    other_days_used = other_days_used + bgLeave.getLeaveCount2() + "(其他)，";
                    other_days_used_date =
							other_days_used_date + sdf.format(bgLeave.getStartDate()) + "-" + sdf.format(bgLeave.getEndDate()) + "(其他)，";
                }
            }
            nj_days_left = (StringUtil.isEmpty(total_days) ? "" :
					String.valueOf(Integer.valueOf(total_days) - nj_days_used));
            map.put("org_name", org_name);
            map.put("peopleId", peopleId);
            map.put("emp_name", emp_name);
            map.put("into_work_date", into_work_date);
            map.put("work_age", work_age);
            map.put("total_days", total_days);
            map.put("nj_days_used", nj_days_used == 0 ? "" : nj_days_used);
            map.put("nj_days_used_date", StringUtil.isEmpty(nj_days_used_date) ? "" : nj_days_used_date.substring(0,
					nj_days_used_date.length() - 1));
            map.put("nj_days_left", nj_days_left);
            map.put("other_days_used", StringUtil.isEmpty(other_days_used) ? "" : other_days_used.substring(0,
					other_days_used.length() - 1));
            map.put("other_days_used_date", StringUtil.isEmpty(other_days_used_date) ? "" :
					other_days_used_date.substring(0, other_days_used_date.length() - 1));
            map.put("remark", remark);
            leaveInfoList.add(map);
        }
        return leaveInfoList;
    }

    /**
     * 统计公务外出
     *
     * @param org_id
     * @param userName
     * @param startDate
     * @param lastDate
     * @return
     */
    public List<?> getGwwcInfoCount(String org_id, String userName, String startDate, String lastDate) {
        List<Object> leaveInfoList = new ArrayList<Object>();
        List<?> infoList = bgLeaveDao.countGwwc(org_id, userName, startDate, lastDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        for (int i = 0; i < infoList.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            Object[] obj = (Object[]) infoList.get(i);
            Calendar now = Calendar.getInstance();
            String peopleId = (String) obj[0];//员工ID
            String org_name = (String) obj[1];//部门名称
            String emp_name = (String) obj[2];//员工姓名
            String work_title = (String) obj[3];//员工职务
            String emp_phone = (String) obj[4];//联系电话
            String gwwc_start_date = "";//外出开始时间
            String gwwc_end_date = "";//外出结束时间
            String gwwc_place = "";//外出地点
            String gwwc_reason = (String) obj[7];//外出事由
            String remark = "";//备注
            if (StringUtil.isNotEmpty(obj[5].toString())) {
                String temp = obj[5].toString().substring(5, 10);
                temp = temp.replace("-", "月");
                gwwc_start_date = temp + "日";
            }
            if (StringUtil.isNotEmpty(obj[6].toString())) {
                String temp = obj[6].toString().substring(5, 10);
                temp = temp.replace("-", "月");
                gwwc_end_date = temp + "日";
            }
            map.put("peopleId", peopleId);
            map.put("org_name", org_name);
            map.put("emp_name", emp_name);
            map.put("work_title", work_title);
            map.put("emp_phone", emp_phone);
            map.put("gwwc_date", gwwc_start_date + "-" + gwwc_end_date);
            map.put("gwwc_place", gwwc_place);
            map.put("gwwc_reason", gwwc_reason);
            map.put("remark", remark);
            leaveInfoList.add(map);
        }
        return leaveInfoList;
    }

    //根据业务ID，部门ID获取当前审核人
    public String getCheckName(String id, String orgId) throws Exception {
        String name = "";
        BgLeave bgLeave = bgLeaveDao.get(id);
        String remark = bgLeaveDao.getProcessInfo(id);
        System.out.println("获取该申请当前处理流程=================" + remark);
        if (remark.equals("工作流：【休假请假审批流程】，环节：【部门负责人确认签字】")) {
            if (bgLeave.getDepId().equals("100025") || bgLeave.getDepId().equals("100048")) {
                name = "孙宇艺";
            } else if (bgLeave.getDepId().equals("100030")) {
                name = "韩绍义";
            } else if (bgLeave.getDepId().equals("100044")) {
                name = "李山桥";
            } else {
                name = bgLeaveDao.getAuditorName(bgLeave, "0");
            }
        } else if (remark.equals("工作流：【休假请假审批流程】，环节：【人事意见】")) {
            name = bgLeaveDao.getAuditorName(bgLeave, "0");
        } else if (remark.equals("工作流：【休假请假审批流程】，环节：【分管领导意见】")) {
            name = bgLeaveDao.getAuditorName(bgLeave, "1");
        } else if (remark.equals("工作流：【休假请假审批流程】，环节：【院长意见】")) {
            name = bgLeaveDao.getAuditorName(bgLeave, "0");
        } else if (remark.equals("工作流：【公务外出审批流程】，环节：【部长审核】")) {
            if (bgLeave.getDepId().equals("100025") || bgLeave.getDepId().equals("100048")) {
                name = "孙宇艺";
            } else if (bgLeave.getDepId().equals("100030")) {
                name = "韩绍义";
            } else if (bgLeave.getDepId().equals("100044")) {
                name = "李山桥";
            } else {
                name = bgLeaveDao.getAuditorName(bgLeave, "0");
            }
        } else if (remark.equals("工作流：【公务外出审批流程】，环节：【分管领导审核】")) {
            name = bgLeaveDao.getAuditorName(bgLeave, "1");
        } else if (remark.equals("工作流：【公务外出审批流程】，环节：【院长审核】")) {
            name = bgLeaveDao.getAuditorName(bgLeave, "0");
        }
        return name;
    }

    //根据业务ID，获取当前处理人
    public String getCurDealName(String id) throws Exception {
        String name = "";
        BgLeave bgLeave = bgLeaveDao.get(id);
        String remark = bgLeaveDao.getProcessInfo(id);
        System.out.println("获取该申请当前处理流程=================" + remark);
        if (remark.equals("工作流：【休假请假审批流程】，环节：【部门负责人确认签字】")) {
            if (bgLeave.getDepId().equals("100025") || bgLeave.getDepId().equals("100048")) {
                name = "孙宇艺";
            } else if (bgLeave.getDepId().equals("100030")) {
                name = "韩绍义";
            } else if (bgLeave.getDepId().equals("100044")) {
                name = "李山桥";
            } else {
                name = bgLeaveDao.getAuditorName(bgLeave, "0");
            }
        } else if (remark.equals("工作流：【休假请假审批流程】，环节：【人事意见】")) {
            name = bgLeaveDao.getAuditorName(bgLeave, "0");
        } else if (remark.equals("工作流：【休假请假审批流程】，环节：【分管领导意见】")) {
            name = bgLeaveDao.getAuditorName(bgLeave, "1");
        } else if (remark.equals("工作流：【休假请假审批流程】，环节：【院长意见】")) {
            name = bgLeaveDao.getAuditorName(bgLeave, "0");
        } else if (remark.equals("工作流：【公务外出审批流程】，环节：【部长审核】")) {
            if (bgLeave.getDepId().equals("100025") || bgLeave.getDepId().equals("100048")) {
                name = "孙宇艺";
            } else if (bgLeave.getDepId().equals("100030")) {
                name = "韩绍义";
            } else if (bgLeave.getDepId().equals("100044")) {
                name = "李山桥";
            } else {
                name = bgLeaveDao.getAuditorName(bgLeave, "0");
            }
        } else if (remark.equals("工作流：【公务外出审批流程】，环节：【分管领导审核】")) {
            name = bgLeaveDao.getAuditorName(bgLeave, "1");
        } else if (remark.equals("工作流：【公务外出审批流程】，环节：【院长审核】")) {
            name = bgLeaveDao.getAuditorName(bgLeave, "0");
        }
        return name;
    }

    public HashMap<String, Object> getFlowStep(String id) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<SysLog> list = sysLogDao.getBJLogs(id);
        BgLeave bgLeave = bgLeaveDao.get(id);
        String leaveType = "";
        if (bgLeave.getLeaveType().equals("GWWC")) {
            leaveType = "公务外出";
        } else if (bgLeave.getLeaveType().equals("NJ")) {
            leaveType = "年假";
        } else if (bgLeave.getLeaveType().equals("SHIJ")) {
            leaveType = "事假";
        } else if (bgLeave.getLeaveType().equals("HJ")) {
            leaveType = "婚假";
        } else if (bgLeave.getLeaveType().equals("CJ")) {
            leaveType = "产假";
        } else if (bgLeave.getLeaveType().equals("TQJ")) {
            leaveType = "探亲假";
        } else if (bgLeave.getLeaveType().equals("BJ")) {
            leaveType = "病假";
        } else if (bgLeave.getLeaveType().equals("SANGJ")) {
            leaveType = "丧假";
        } else if (bgLeave.getLeaveType().equals("PCJ")) {
            leaveType = "陪产假";
        } else if (bgLeave.getLeaveType().equals("OTHER")) {
            leaveType = "其他";
        }
        map.put("flowStep", list);
        map.put("size", list.size());
        map.put("identifier", bgLeave.getPeopleName() + "（" + bgLeave.getDepName() + "）," + new SimpleDateFormat("yyyy-MM-dd").format(bgLeave.getStartDate()) + "至" + new SimpleDateFormat("yyyy-MM-dd").format(bgLeave.getEndDate()) + "（" + leaveType + " " + bgLeave.getLeaveCount1() + "天）");
        map.put("success", true);

        return map;
    }


    public void sendResumeMsg() {
        this.logger.info("------------系统定时发送销假提醒------------");
        HttpServletRequest request = null;
        try {
            SimpleDateFormat formatData = new SimpleDateFormat("yyyy年MM月dd日");
            // 获取尚未销假的员工信息
            List<?> list = this.getResumeInfo();
            if (list != null && list.size() > 0) {
                for (Object o : list) {
                    Object[] objs = (Object[]) o;
                    //业务id
                    String business_id = (String) objs[0];
                    //当事人id
                    String employee_id = (String) objs[1];
                    //当事人姓名
                    String signed_man = (String) objs[2];
                    //当事人手机号
                    String emp_phone = (String) objs[3];

                    String startDay=formatData.format(objs[4]);
                    String endDay = formatData.format(objs[5]);

                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("signed_man", signed_man);
                    map.put("leave_start_date", startDay);
                    map.put("leave_end_date", endDay);
                    // 发送消息提醒
                    messageService.sendMoMsg(request,business_id,"您的"+startDay+"〜"+endDay+"请假还未销假，请尽快销假。", emp_phone);//发送消息给当事人
                    this.logger.debug("{}的请假还未销假，已向其手机{}发送短信进行提醒。", signed_man, emp_phone);
                }
            } else {
                this.logger.info("没有超期未销假的请假信息");
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(),e);
        }
    }

    private List<?> getResumeInfo() {
        return this.bgLeaveDao.getResumeInfo();
    }

}
