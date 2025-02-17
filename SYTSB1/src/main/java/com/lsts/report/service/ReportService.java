package com.lsts.report.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.core.exception.KhntException;
import com.khnt.rbac.impl.bean.Org;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rtbox.config.bean.RtDir;
import com.khnt.rtbox.config.bean.RtPage;
import com.khnt.rtbox.config.dao.RtPageDao;
import com.khnt.rtbox.config.service.RtPageManager;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.constant.Constant;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.dao.InspectionInfoDao;
import com.lsts.log.service.SysLogService;
import com.lsts.report.bean.Report;
import com.lsts.report.bean.ReportItem;
import com.lsts.report.dao.ReportDao;
import com.lsts.report.dao.ReportItemDao;
import com.lsts.report.dao.ReportSnDelCodeDao;

import util.TS_Util;

/**
 * 报告信息管理 servier
 * 
 * @author 肖慈边 2014-1-23
 */
@Service("reportService")
public class ReportService extends EntityManageImpl<Report, ReportDao> {
	@Autowired
	private ReportDao reportDao;
	@Autowired
	private InspectionInfoDao inspectionInfoDao;
	@Autowired
	private ReportSnDelCodeDao reportSnDelCodeDao;
	@Autowired
	private SysLogService logService;
	@Autowired
	private ReportItemDao reportItemDao;
	@Autowired
	RtPageDao rtPageDao;
	@Autowired
	private RtPageManager rtPageManager;

	@SuppressWarnings("unchecked")
	public String getReportType(String unit_id, String check_type, String isJson)
			throws Exception {

		StringBuffer json = new StringBuffer("[");
		StringBuffer sql = new StringBuffer();
		sql
				.append("select t1.id,t1.report_name from base_unit_flow t,base_reports t1 where t.fk_report_id = t1.id and t.fk_unit_id = "
						+ unit_id + " and t.check_type =" + check_type);

		List list = reportDao.createSQLQuery(sql.toString()).list();

		if (list != null && list.size() > 0) {

			Object[] obj = list.toArray();
			for (int i = 0; i < obj.length; i++) {
				Object[] oo = (Object[]) obj[i];

				json.append("{id:\'").append(oo[0].toString()).append(
						"\',text:\'").append(oo[1].toString()).append("\'}");

				// json.append("{name:\"").append(oo[1].toString())
				// .append("\",value:").append(oo[2].toString())
				// .append(",color:\"")
				// .append(COLORARRAY[i % COLORARRAY.length])
				// .append("\"}");
				if (i != obj.length - 1) {
					json.append(",");
				}
			}

		}

		json.append("]");

		return json.toString();

	}

	/**
	 * 生成报告证书编号（生成规则：前缀[CO]-设备类别T[电梯：T]检验类别C[定期检验：D/安改维监督检验：A/制造监督检验:J/进口设备检验:K/出口设备检验:C/委托检验:W]年份R[2014]序号XXX[00000]）
	 * 其中5位序号，从50001开始（除电梯从00001开始以外）
	 * 
	 * @param device_type --
	 *            设备类别
	 * @param check_type --
	 *            检验类型
	 * @param report_id --
	 *            报告ID
	 * @param org_id --
	 *            部门ID
	 * @return
	 * @author GaoYa
	 * @date 2014-05-27 下午01:10:00
	 */
	public synchronized String generateReportCode(String device_type,
			String check_type, String report_id, String org_id) throws Exception {
		// 获取当前登录用户及其部门信息
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		//User uu = (User)user.getSysUser();	// 登录用户基本信息
		if(StringUtil.isEmpty(org_id)){
			Org org = TS_Util.getCurOrg(user);		// 登录用户部门信息
			org_id = org.getId();
		}
		
		String report_code = "";
		if (StringUtil.isNotEmpty(device_type)
				&& StringUtil.isNotEmpty(check_type)
				&& StringUtil.isNotEmpty(report_id)) {
			Report report = reportDao.get(report_id);
			if (report != null) {
				String pre_device_type = device_type.substring(0, 1); // 获取设备类别前缀（大类）
				if("2610".equals(device_type)){
					// 2610：常压罐车
					// 常压罐车的编号规格（W2015-CYGCJⅹⅹⅹⅹⅹ），与机电类承压类编号规则不相同
					// W——委托检验；2015——证书出具时的年份；CYGC——常压罐车；J——制造监督检验；ⅹⅹⅹⅹⅹ——年度内报告顺序号。
					
					// 1、委托检验
					if(report.getReport_name().contains("委托")){
						report_code += Constant.REPORT_SN_C_W;
					}
					// 2、出证年份
					String curYear = TS_Util.getCurYear();
					if (StringUtil.isNotEmpty(curYear)) {
						report_code += curYear;
					} else {
						throw new Exception("生成报告证书编号出错：获取当前年份失败！");
					}
					
					// 3、分割线
					report_code += "-";
					// 4、设备类别（常压罐车）
					HashMap<String, Object> map = getDeviceType(
							report_code, device_type);
					if (Boolean.valueOf(String.valueOf(map
							.get("success")))) {
						report_code = String.valueOf(map
								.get("data"));
					} else {
						throw new Exception(String.valueOf(map
								.get("msg")));
					}
					//report_code += "CYGC";
					// 5、检验类别（制造监督检验）
					HashMap<String, Object> map1 = getCheckType(
							report_code, check_type.trim());
					if (Boolean.valueOf(String.valueOf(map1
							.get("success")))) {
						report_code = String.valueOf(map1
								.get("data"));
					} else {
						throw new Exception(String.valueOf(map1
								.get("msg")));
					}
					
					synchronized (this){
						// 查询已作废的报告书编号（最小值）
						String minReportSn = reportSnDelCodeDao.queryDelReport_sn(report_code, org_id);
						if(StringUtil.isNotEmpty(minReportSn) && !"null".equals(minReportSn)){
							// 重用已作废的报告书编号，删除原业务信息的作废记录
							reportSnDelCodeDao.delReport_sn(minReportSn);
							List<InspectionInfo> list = inspectionInfoDao.queryInspectionInfo(minReportSn);
							if (list.isEmpty()) {
								return minReportSn;
							}
						}
					}
					
					// 序号（ 获取5位序号）
					HashMap<String, Object> map2 = getNumber5(report_code, device_type, org_id);
					if (Boolean.valueOf(String.valueOf(map2
							.get("success")))) {
						report_code = String.valueOf(map2
								.get("data"));
					} else {
						throw new Exception(String.valueOf(map2
								.get("msg")));
					}
					return report_code;
				}else if("2130".equals(device_type) && report.getReport_name().contains("储气井定期检验报告")){
					// 2130：第三类压力容器
					// 储气井定期检验报告编号规则（RDJ-2016-012），与机电类其他承压类编号规则不相同
					// R——表示压力容器；D——表示定期检验；J——表示储气井；2016——表示报告出具时的年份；012——表示年度内报告流水号。
					
					// 1、设备类别
					HashMap<String, Object> map = getDeviceType(
							report_code, device_type);
					if (Boolean.valueOf(String.valueOf(map
							.get("success")))) {
						report_code = String.valueOf(map
								.get("data"));
					} else {
						throw new Exception(String.valueOf(map
								.get("msg")));
					}
					
					// 2、检验类别（定期检验）
					HashMap<String, Object> map1 = getCheckType(
							report_code, check_type.trim());
					if (Boolean.valueOf(String.valueOf(map1
							.get("success")))) {
						report_code = String.valueOf(map1
								.get("data"));
					} else {
						throw new Exception(String.valueOf(map1
								.get("msg")));
					}
					
					// 3、储气井
					report_code += Constant.REPORT_SN_T_J;
					
					// 4、分割线
					report_code += "-";
					
					// 5、出证年份
					String curYear = TS_Util.getCurYear();
					if (StringUtil.isNotEmpty(curYear)) {
						report_code += curYear;
					} else {
						throw new Exception("生成报告证书编号出错：获取当前年份失败！");
					}
					// 6、分割线
					report_code += "-";
					
					// 7、查询已作废的报告书编号（最小值）
					synchronized (this){
						String minReportSn = reportSnDelCodeDao.queryDelReport_sn(report_code, org_id);
						if(StringUtil.isNotEmpty(minReportSn) && !"null".equals(minReportSn)){
							// 重用已作废的报告书编号，删除原作废记录
							reportSnDelCodeDao.delReport_sn(minReportSn);
							List<InspectionInfo> list = inspectionInfoDao.queryInspectionInfo(minReportSn);
							if (list.isEmpty()) {
								return minReportSn;
							}
						}
					}
					
					// 8、序号（ 获取3位序号）
					HashMap<String, Object> map2 = getNumber3(report_code, device_type);
					if (Boolean.valueOf(String.valueOf(map2
							.get("success")))) {
						report_code = String.valueOf(map2
								.get("data"));
					} else {
						throw new Exception(String.valueOf(map2
								.get("msg")));
					}
					return report_code;
				}else if("2130".equals(device_type) && report.getReport_name().contains("储气井固井质量检测与评价报告")){
					// 2130：第三类压力容器
					// 储气井定期检验报告编号规则（RGJ-2016-012），与机电类和其他承压类编号规则不相同
					// RGJ——前綴；2016——表示报告出具时的年份；012——表示年度内报告流水号。
					
					// 1、前綴
					report_code += "RGJ";
					
					// 2、分割线
					report_code += "-";
					
					// 3、出证年份
					String curYear = TS_Util.getCurYear();
					if (StringUtil.isNotEmpty(curYear)) {
						report_code += curYear;
					} else {
						throw new Exception("生成报告证书编号出错：获取当前年份失败！");
					}
					// 4、分割线
					report_code += "-";
					
					// 5、查询已作废的报告书编号（最小值）
					synchronized (this){
						String minReportSn = reportSnDelCodeDao.queryDelReport_sn(report_code, org_id);
						if(StringUtil.isNotEmpty(minReportSn) && !"null".equals(minReportSn)){
							// 重用已作废的报告书编号，删除原作废记录
							reportSnDelCodeDao.delReport_sn(minReportSn);
							List<InspectionInfo> list = inspectionInfoDao.queryInspectionInfo(minReportSn);
							if (list.isEmpty()) {
								return minReportSn;
							}
						}
					}
					
					// 6、序号（ 获取3位序号）
					HashMap<String, Object> map2 = getNumber3(report_code, device_type);
					if (Boolean.valueOf(String.valueOf(map2
							.get("success")))) {
						report_code = String.valueOf(map2
								.get("data"));
					} else {
						throw new Exception(String.valueOf(map2
								.get("msg")));
					}
					return report_code;
				}else if(report.getReport_name().contains("锅炉设计文件")){
					// 1：锅炉  B：部件
					if("1".equals(pre_device_type) || "B".equals(pre_device_type)){
						// 锅炉设计文件安全鉴定及节能审查报告
						// 编号规则（TSWJGLSC123－两位数年号+三位数顺序号），例如TSWJGLSC123－15431
						// TSWJGLSC123——前缀；15——表示两位数年号；431——表示流水号。					
						// 1、前缀
						String report_sn_pre = "TSWJGLSC123";
						// 2、分割线
						report_code = report_sn_pre + "-";
						// 3、两位数年号
						String curYear = TS_Util.getCurYear2();
						if (StringUtil.isNotEmpty(curYear)) {
							report_code += curYear;
						} else {
							throw new Exception("生成报告证书编号出错：获取当前年份失败！");
						}
						
						// 4、查询已作废的报告书编号（最小值）
						synchronized (this){
							String minReportSn = reportSnDelCodeDao.queryDelReport_sn(report_code, org_id);
							if(StringUtil.isNotEmpty(minReportSn) && !"null".equals(minReportSn)){
								// 重用已作废的报告书编号，删除原作废记录
								reportSnDelCodeDao.delReport_sn(minReportSn);
								List<InspectionInfo> list = inspectionInfoDao.queryInspectionInfo(minReportSn);
								if (list.isEmpty()) {
									return minReportSn;
								}
							}
						}
						
						// 5、序号（ 获取3位序号）
						HashMap<String, Object> map2 = getNumber31(report_code, device_type);
						if (Boolean.valueOf(String.valueOf(map2
								.get("success")))) {
							report_code = String.valueOf(map2
									.get("data"));
						} else {
							throw new Exception(String.valueOf(map2
									.get("msg")));
						}
						return report_code;
					}
				} else if (report.getReport_name().contains("委托检验检测报告") && "100067".equals(org_id)) {
					// 100067：无损检测事业部
					// 委托检验检测报告编号规则（CO-WW+四位数年号+五位数流水号），例如CO-WW201650001
					// CO-WW——前缀；2016——表示四位数年号；50001——表示流水号。
					
					// 1、前缀
					String report_sn_pre = "CO-WW";
					report_code += report_sn_pre;
					
					// 2、四位数年号
					String curYear = String.valueOf(TS_Util.getCurrentYear());
					if (StringUtil.isNotEmpty(curYear)) {
						report_code += curYear;
					} else {
						throw new Exception("生成报告证书编号出错：获取当前年份失败！");
					}

					// 3、流水号（ 获取5位流水号）
					HashMap<String, Object> map2 = getNumber5(report_code, device_type, org_id);
					if (Boolean.valueOf(String.valueOf(map2.get("success")))) {
						report_code = String.valueOf(map2.get("data"));
					} else {
						throw new Exception(String.valueOf(map2.get("msg")));
					}
					return report_code;
				} else if (report.getReport_name().contains("无损检测报告")) {
					// 无损检测报告编号规则（CO-WW+四位数年号+五位数流水号），例如CO-WW201650001
					// CO-WW——前缀；2016——表示四位数年号；50001——表示流水号。
					// 2016-09-18 update by GaoYa
					
					// 1、前缀
					String report_sn_pre = "CO-WW";
					report_code += report_sn_pre;
					
					// 2、四位数年号
					String curYear = String.valueOf(TS_Util.getCurrentYear());
					if (StringUtil.isNotEmpty(curYear)) {
						report_code += curYear;
					} else {
						throw new Exception("生成报告证书编号出错：获取当前年份失败！");
					}
					
					// 3、查询已作废的报告书编号（最小值）
					synchronized (this){
						String minReportSn = reportSnDelCodeDao.queryDelReport_sn(report_code, org_id);
						if(StringUtil.isNotEmpty(minReportSn) && !"null".equals(minReportSn)){
							// 重用已作废的报告书编号，删除原作废记录
							reportSnDelCodeDao.delReport_sn(minReportSn);
							List<InspectionInfo> list = inspectionInfoDao.queryInspectionInfo(minReportSn);
							if (list.isEmpty()) {
								return minReportSn;
							}
						}
					}

					// 4、流水号（ 获取5位流水号）
					HashMap<String, Object> map2 = getNumber5(report_code, device_type, org_id);
					if (Boolean.valueOf(String.valueOf(map2.get("success")))) {
						report_code = String.valueOf(map2.get("data"));
					} else {
						throw new Exception(String.valueOf(map2.get("msg")));
					}
					return report_code;
				} else if (report.getReport_name().contains("电梯安全评估报告")) {
					// 无损检测报告编号规则（CO-WW+四位数年号+五位数流水号），例如CO-WW201650001
					// CO-WW——前缀；2016——表示四位数年号；50001——表示流水号。
					// 2016-09-18 update by GaoYa
					
					// 1、前缀
					String report_sn_pre = "CO-TP";
					report_code += report_sn_pre;
					
					// 2、四位数年号
					String curYear = String.valueOf(TS_Util.getCurrentYear());
					if (StringUtil.isNotEmpty(curYear)) {
						report_code += curYear;
					} else {
						throw new Exception("生成报告证书编号出错：获取当前年份失败！");
					}
					
					// 3、查询已作废的报告书编号（最小值）
					synchronized (this){
						String minReportSn = reportSnDelCodeDao.queryDelReport_sn(report_code, org_id);
						if(StringUtil.isNotEmpty(minReportSn) && !"null".equals(minReportSn)){
							// 重用已作废的报告书编号，删除原作废记录
							reportSnDelCodeDao.delReport_sn(minReportSn);
							List<InspectionInfo> list = inspectionInfoDao.queryInspectionInfo(minReportSn);
							if (list.isEmpty()) {
								return minReportSn;
							}
						}
					}

					// 4、流水号（ 获取5位流水号）
					HashMap<String, Object> map2 = getNumber5(report_code, device_type, org_id);
					if (Boolean.valueOf(String.valueOf(map2.get("success")))) {
						report_code = String.valueOf(map2.get("data"));
					} else {
						throw new Exception(String.valueOf(map2.get("msg")));
					}
					return report_code;
				}else{
					String report_sn = report.getReport_sn(); // 报告书编号格式
					if (StringUtil.isNotEmpty(report_sn)) {
						String[] codeRules = report_sn.split(",");
						if (codeRules.length > 3) {
							for (int i = 0; i < codeRules.length; i++) {
								if (Constant.REPORT_SN_PRE.equals(codeRules[i])) { // 1、前缀（CO）
									report_code = codeRules[i]; 
									continue;
								}
								if (Constant.REPORT_SN_T.equals(codeRules[i])) { // 2、设备类别
									HashMap<String, Object> map = getDeviceType(
											report_code, device_type);
									if (Boolean.valueOf(String.valueOf(map
											.get("success")))) {
										report_code = String.valueOf(map
												.get("data"));
										continue;
									} else {
										throw new Exception(String.valueOf(map
												.get("msg")));
									}
								}
								if (Constant.REPORT_SN_C.equals(codeRules[i])) { // 3、检验类别
									HashMap<String, Object> map = getCheckType(
											report_code, check_type.trim());
									if (Boolean.valueOf(String.valueOf(map
											.get("success")))) {
										report_code = String.valueOf(map
												.get("data"));
										continue;
									} else {
										throw new Exception(String.valueOf(map
												.get("msg")));
									}
								}
								
								if (Constant.REPORT_SN_R.equals(codeRules[i])) { // 4、年份
									String curYear = TS_Util.getCurYear();
									if (StringUtil.isNotEmpty(curYear)) {
										report_code += curYear;
										continue;
									} else {
										throw new Exception("生成报告证书编号出错：获取当前年份失败！");
									}
								}
								
								
								// 5、查询已作废的报告书编号（最小值）
								synchronized (this){
									String minReportSn = "";
									if("100069".equals(org_id)){
										minReportSn = reportSnDelCodeDao.queryDelReport_sn(report_code+"9", org_id);				
									}else if("100090".equals(org_id)){
										minReportSn = reportSnDelCodeDao.queryDelReport_sn(report_code+"8", org_id);				
									}else if("100091".equals(org_id)){
										minReportSn = reportSnDelCodeDao.queryDelReport_sn(report_code+"8", org_id);				
									}else{
										minReportSn = reportSnDelCodeDao.queryDelReport_sn(report_code, org_id);				
									}
									if(StringUtil.isNotEmpty(minReportSn) && !"null".equals(minReportSn)){
										// 重用已作废的报告书编号，删除原作废记录
										reportSnDelCodeDao.delReport_sn(minReportSn);
										List<InspectionInfo> list = inspectionInfoDao.queryInspectionInfo(minReportSn);
										if (list.isEmpty()) {
											return minReportSn;
										}
									}
								}
								
								if (Constant.REPORT_SN_XXX.equals(codeRules[i])) { // 6、序号
									// 获取5位序号
									HashMap<String, Object> map = getNumber5(report_code, device_type, org_id);
									if (Boolean.valueOf(String.valueOf(map
											.get("success")))) {
										report_code = String.valueOf(map
												.get("data"));
										continue;
									} else {
										throw new Exception(String.valueOf(map
												.get("msg")));
									}
								}
							}
						}else if (codeRules.length > 1) {
							for (int i = 0; i < codeRules.length; i++) {
								if (Constant.REPORT_SN_Y.equals(codeRules[i])) { // 1、年份
									String curYear = TS_Util.getCurYear();
									if (StringUtil.isNotEmpty(curYear)) {
										report_code += curYear;
										continue;
									} else {
										throw new Exception("生成报告证书编号出错：获取当前年份失败！");
									}
								}
								if (Constant.REPORT_SN_XXX.equals(codeRules[i])) { // 2、序号
									// 获取3位序号
									HashMap<String, Object> map = getNumber3(report_code, device_type);
									if (Boolean.valueOf(String.valueOf(map
											.get("success")))) {
										report_code = String.valueOf(map
												.get("data"));
										continue;
									} else {
										throw new Exception(String.valueOf(map
												.get("msg")));
									}
								}
								// 3、查询已作废的报告书编号（最小值）
								synchronized (this){
									String minReportSn = reportSnDelCodeDao.queryDelReport_sn(report_code, org_id);
									if(StringUtil.isNotEmpty(minReportSn) && !"null".equals(minReportSn)){
										// 重用已作废的报告书编号，删除原作废记录
										reportSnDelCodeDao.delReport_sn(minReportSn);
										List<InspectionInfo> list = inspectionInfoDao.queryInspectionInfo(minReportSn);
										if (list.isEmpty()) {
											return minReportSn;
										}
									}
								}
							}
						} else {
							throw new Exception("生成报告证书编号出错：报告书编号格式配置错误["
									+ report.getReport_name()
									+ "]，格式规则（CO,T,C,R,XXX）");
						}
					} else {
						throw new Exception(
								"生成报告证书编号出错：未配置该报告书编号格式["
										+ (StringUtil.isNotEmpty(report
												.getReport_name()) ? report
												.getReport_name() : report_id)
										+ "]");
					}
				}
			} else {
				throw new Exception("生成报告证书编号出错：获取报告书编号格式失败[" + report_id + "]");
			}
			if (StringUtil.isEmpty(report_code)) {
				throw new Exception("生成报告证书编号出错：报告书编号格式配置错误["
						+ (StringUtil.isNotEmpty(report
								.getReport_name()) ? report
								.getReport_name() : report_id)
						+ "]，格式规则（CO,T,C,R,XXX）");
			}
		}
		System.out.println("生成报告证书编号成功：" + report_code);
		return report_code;
	}

	/**
	 * 获取报告书编号之设备类别
	 * 
	 * @param report_code --
	 *            报告证书编号
	 * @param device_type --
	 *            设备类别
	 *            检验类别
	 * @return
	 * @author GaoYa
	 * @date 2014-05-27 下午01:25:00
	 */
	private HashMap<String, Object> getDeviceType(String report_code,
			String device_type) {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		String pre_device_type = device_type.substring(0, 1); // 获取设备类别前缀（大类）
		if ("7310".equals(device_type)) {	// 7310：安全阀
			pre_device_type = "Q";
		}
		if ("2610".equals(device_type)) {	// 2610：常压罐车
			report_code += Constant.REPORT_SN_T_CYGC;
			wrapper.put("success", true);
			wrapper.put("data", report_code);
			return wrapper;
		}
		if (StringUtil.isNotEmpty(pre_device_type)) {
			if (report_code.length()>0) {
				report_code += "-";
			}
			if (Constant.DEVICE_TYPE_3.equals(pre_device_type)) { // 3、电梯
				report_code += Constant.REPORT_SN_T_T;
			}else if (Constant.DEVICE_TYPE_1.equals(pre_device_type)) { // 1、锅炉
				report_code += Constant.REPORT_SN_T_G;
			}else if (Constant.DEVICE_TYPE_2.equals(pre_device_type)) { // 2、压力容器
				if (device_type.startsWith(Constant.DEVICE_TYPE_2_3)) {	// 2.3、气瓶
					report_code += Constant.REPORT_SN_T_P;
				}else{
					report_code += Constant.REPORT_SN_T_R;
				}
			}else if (Constant.DEVICE_TYPE_4.equals(pre_device_type)) { // 4、起重机械
				report_code += Constant.REPORT_SN_T_Q;
			}else if (Constant.DEVICE_TYPE_5.equals(pre_device_type)) { // 5、场（厂）内机动车辆
				report_code += Constant.REPORT_SN_T_N;
			}else if (Constant.DEVICE_TYPE_6.equals(pre_device_type)) { // 6、大型游乐设施
				report_code += Constant.REPORT_SN_T_Y;
			}else if (Constant.DEVICE_TYPE_7.equals(pre_device_type)) { // 7、压力管道元件（编号规则与压力管道一致）
				report_code += Constant.REPORT_SN_T_D;
			}else if (Constant.DEVICE_TYPE_8.equals(pre_device_type)) { // 8、压力管道
				report_code += Constant.REPORT_SN_T_D;
			}else if (Constant.DEVICE_TYPE_9.equals(pre_device_type)) { // 9、客运索道
				report_code += Constant.REPORT_SN_T_S;
			}else if (Constant.DEVICE_TYPE_B.equals(pre_device_type)) { // B、部件
				report_code += Constant.REPORT_SN_T_B;
			}else if (Constant.DEVICE_TYPE_F.equals(pre_device_type)) { // F、安全附件及安全保护装置
				report_code += Constant.REPORT_SN_T_H;
			}else if (Constant.DEVICE_TYPE_Q.equals(pre_device_type)) { // Q、安全阀
				report_code += Constant.REPORT_SN_T_F;
			}else if (Constant.DEVICE_TYPE_0.equals(pre_device_type)) { // 0、其他
				report_code += Constant.REPORT_SN_T_QT;
			}
			wrapper.put("success", true);
			wrapper.put("data", report_code);
		} else {
			wrapper.put("success", false);
			wrapper.put("msg", "生成报告证书编号出错：获取设备类别前缀失败[" + device_type + "]");
		}
		return wrapper;
	}
	
	/**
	 * 获取报告书编号之检验类别
	 * 
	 * @param report_code --
	 *            报告证书编号
	 * @param check_type --
	 *            检验类别
	 * @return
	 * @author GaoYa
	 * @date 2014-05-27 下午01:48:00
	 */
	private HashMap<String, Object> getCheckType(String report_code, String check_type) {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(check_type.trim())) {
			if (Constant.CHECK_TYPE_2.equals(check_type.trim())) {
				report_code += Constant.REPORT_SN_C_A; // 安改维监督检验
			} else if (Constant.CHECK_TYPE_3.equals(check_type.trim())) {
				report_code += Constant.REPORT_SN_C_D; // 定期检验
			} else if (Constant.CHECK_TYPE_1.equals(check_type.trim())) {
				report_code += Constant.REPORT_SN_C_J; // 制造监督检验
			} else if (Constant.CHECK_TYPE_7.equals(check_type.trim())) {
				report_code += Constant.REPORT_SN_C_K; // 进口设备检验
			} else if (Constant.CHECK_TYPE_8.equals(check_type.trim())) {
				report_code += Constant.REPORT_SN_C_C; // 出口设备检验
			} else if (Constant.CHECK_TYPE_4.equals(check_type.trim())) {
				report_code += Constant.REPORT_SN_C_W; // 委托检验
			}
			
			wrapper.put("success", true);
			wrapper.put("data", report_code);
		} else {
			wrapper.put("success", false);
			wrapper.put("msg", "生成报告证书编号出错：获取检验类别失败[" + check_type + "]");
		}
		return wrapper;
	}

	/**
	 * 获取报告书序号（5位）
	 * 
	 * @param report_code --
	 *            报告证书编号
	 * @param device_type --
	 *            设备类别  
	 * @param org_id --
	 *            部门ID  
	 * @return
	 * @author GaoYa
	 * @date 2016-10-08 上午09:18:00
	 */
	private synchronized HashMap<String, Object> getNumber5(String report_code, String device_type, String org_id) {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		String report_sn_suf = "";
		if ("100069".equals(org_id)) {
			report_sn_suf = inspectionInfoDao.queryXzNextReportSn5(report_code, device_type);	// 西藏
		} else if ("100090".equals(org_id)) {
			report_sn_suf = inspectionInfoDao.queryXjNextReportSn5(report_code, device_type);	// 新疆
		} else if ("100091".equals(org_id)) {
			report_sn_suf = inspectionInfoDao.queryJzNextReportSn5(report_code, device_type);	// 九寨
		} else {
			report_sn_suf = inspectionInfoDao.queryNextReportSn5(report_code, device_type);		// 其他
		}

		if (StringUtil.isNotEmpty(report_sn_suf)) {
			/*
			 * List<InspectionInfo> list =
			 * inspectionInfoDao.queryByReport_sn(report_code+report_sn_suf); if
			 * (!list.isEmpty()) { report_sn_suf =
			 * String.valueOf(Integer.parseInt(report_sn_suf)+1); }
			 */
			report_code += report_sn_suf;
			wrapper.put("success", true);
			wrapper.put("data", report_code);

		} else {
			wrapper.put("success", false);
			wrapper.put("msg", "生成报告证书编号出错：生成报告书序号失败[" + report_code + "]");
		}
		return wrapper;
	}
	
	/**
	 * 获取报告书序号（4位）
	 * 
	 * @param report_code --
	 *            报告证书编号
	 * @param device_type --
	 *            设备类别  
	 * @return
	 * @author GaoYa
	 * @date 2014-05-27 下午02:18:00
	 */
	private synchronized HashMap<String, Object> getNumber4(String report_code, String device_type) {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		String report_sn_suf = inspectionInfoDao
				.queryNextReportSn4(report_code, device_type);
		if (StringUtil.isNotEmpty(report_sn_suf)) {
			/*
			List<InspectionInfo> list = inspectionInfoDao.queryByReport_sn(report_code+report_sn_suf);
			if (!list.isEmpty()) {
				report_sn_suf = String.valueOf(Integer.parseInt(report_sn_suf)+1);
			}
			*/
			report_code += report_sn_suf;
			wrapper.put("success", true);
			wrapper.put("data", report_code);
			
		} else {
			wrapper.put("success", false);
			wrapper.put("msg", "生成报告证书编号出错：生成报告书序号失败[" + report_code + "]");
		}
		return wrapper;
	}
	
	/**
	 * 获取报告书序号（3位）
	 * 
	 * @param report_code --
	 *            报告证书编号
	 * @param device_type --
	 *            设备类别  
	 * @return
	 * @author GaoYa
	 * @date 2014-05-27 下午02:18:00
	 */
	private synchronized HashMap<String, Object> getNumber3(String report_code, String device_type) {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		String report_sn_suf = inspectionInfoDao
				.queryNextReportSn3(report_code, device_type);
		if (StringUtil.isNotEmpty(report_sn_suf)) {
			/*
			List<InspectionInfo> list = inspectionInfoDao.queryByReport_sn(report_code+report_sn_suf);
			if (!list.isEmpty()) {
				report_sn_suf = String.valueOf(Integer.parseInt(report_sn_suf)+1);
			}
			*/
			report_code += report_sn_suf;
			wrapper.put("success", true);
			wrapper.put("data", report_code);
			
		} else {
			wrapper.put("success", false);
			wrapper.put("msg", "生成报告证书编号出错：生成报告书序号失败[" + report_code + "]");
		}
		return wrapper;
	}
	
	/**
	 * 获取报告书序号（3位）
	 * 
	 * @param report_code --
	 *            报告证书编号
	 * @param device_type --
	 *            设备类别  
	 * @return
	 * @author GaoYa
	 * @date 2014-05-27 下午02:18:00
	 */
	private synchronized HashMap<String, Object> getNumber31(String report_code, String device_type) {
		HashMap<String, Object> wrapper = new HashMap<String, Object>();
		String report_sn_suf = inspectionInfoDao
				.queryNextReportSn31(report_code, device_type);
		if (StringUtil.isNotEmpty(report_sn_suf)) {
			/*
			List<InspectionInfo> list = inspectionInfoDao.queryByReport_sn(report_code+report_sn_suf);
			if (!list.isEmpty()) {
				report_sn_suf = String.valueOf(Integer.parseInt(report_sn_suf)+1);
			}
			*/
			report_code += report_sn_suf;
			wrapper.put("success", true);
			wrapper.put("data", report_code);
			
		} else {
			wrapper.put("success", false);
			wrapper.put("msg", "生成报告证书编号出错：生成报告书序号失败[" + report_code + "]");
		}
		return wrapper;
	}
	//启用报告
	public void enable(HttpServletRequest request,String ids) throws Exception {
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		String[] idss = ids.split(",");
		for (String id : idss) {
			Report report =this.get(id);
			if(report != null){
				//记录
				if ("1".equals(report.getReport_version())) {
					report.setFlag("1");
					report.setRemark(Constant.REPORT_apply_REMARK);	// 备注
					report.setMdy_user_id(curUser.getId()); 	// 最后修改人ID
					report.setMdy_user_name(curUser.getName()); // 最后修改人姓名
					report.setMdy_date(DateUtil.convertStringToDate(Constant.ymdhmsDatePattern,
							DateUtil.getCurrentDateTime())); 	// 最后修改时间
					this.save(report);
					
					//记录日志
					logService.setLogs(id, Constant.REPORT_apply_REMARK, Constant.REPORT_apply_REMARK,
							curUser.getId(), curUser.getName(), new Date(), request
									.getRemoteAddr());
				}
			}
		}
	}
	//停用报告
	public void disable(HttpServletRequest request,String ids) throws Exception {
		CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
		String[] idss = ids.split(",");
		try {
			for (String id : idss) {
				Report report =this.get(id);
				if(report != null){
					//记录
					if ("1".equals(report.getReport_version())) {
						report.setFlag("0");
						report.setRemark(Constant.REPORT_stope_REMARK);	// 备注
						report.setMdy_user_id(curUser.getId()); 	// 最后修改人ID
						report.setMdy_user_name(curUser.getName()); // 最后修改人姓名
						report.setMdy_date(DateUtil.convertStringToDate(Constant.ymdhmsDatePattern,
								DateUtil.getCurrentDateTime())); 	// 最后修改时间
						this.save(report);
						
						//记录日志
						logService.setLogs(id, Constant.REPORT_stope_REMARK, Constant.REPORT_stope_REMARK,
								curUser.getId(), curUser.getName(), new Date(), request
										.getRemoteAddr());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 获取业务流水号
		public synchronized  String getSn(int cont)
				throws KhntException {
			String number = "";
			StringBuffer sql = new StringBuffer();
			sql.append("select nvl2(max(sn), lpad(to_number(max(sn)) +");
			sql.append(cont);
			sql.append(" , 6, 0), lpad(to_number(0) + ");
			sql.append(cont).append(" , 6, 0)) sn from tzsb_inspection_info ") ;
			List list = reportDao.createSQLQuery(sql.toString()).list();
			number = list.get(0).toString();
			System.out.println("================number"+number);
			//number = obj[0].toString();
			return number;

		}
		/**
		 * 生成默认目录
		 * @param request
		 * @throws Exception
		 */
		public void createDefaultTemplete(HttpServletRequest request) throws Exception {
			String fkReportsId = request.getParameter("fk_reports_id");//报告id
			String pageIndex = "";
			Report report = reportDao.get(fkReportsId);
			List<ReportItem> reportItemList = reportItemDao.queryMustPageByReportId(fkReportsId);
			
			String rtDirJson = rtPageDao.getByTemplateAndVersion(report.getRtboxId(), null).getRtDirJson();
			if(reportItemList!=null&&reportItemList.size()>0) {
				for(ReportItem reportItem : reportItemList) {
					if(StringUtil.isNotEmpty(pageIndex)) {
						pageIndex = pageIndex+","+reportItem.getPage_index();
					}else {
						pageIndex = reportItem.getPage_index();
					}
				}
				JSONArray array = new JSONArray(rtDirJson);
				JSONArray temp = new JSONArray();
				rtPageManager.filterDelNode(array, temp,pageIndex);
				pageIndex = temp.toString();
			}
			report.setRtDefaultDirJson(pageIndex);
			reportDao.save(report);
		}
}
