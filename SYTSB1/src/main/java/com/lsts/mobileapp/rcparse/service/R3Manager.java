package com.lsts.mobileapp.rcparse.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.datameta.Variable;

import com.khnt.annotation.Comment;
import com.khnt.base.Factory;
import com.khnt.core.crud.bean.BaseEntity;
import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.core.exception.KhntException;
import com.khnt.rtbox.template.constant.RtPageType;
import com.khnt.rtbox.template.constant.RtPath;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.constant.ReportConstant;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.dao.DeviceDao;
import com.lsts.flow.service.BaseFlowConfigService;
import com.lsts.inspection.bean.Inspection;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.bean.ReportItemRecord;
import com.lsts.inspection.bean.ReportItemValue;
import com.lsts.inspection.dao.InspectionInfoDao;
import com.lsts.inspection.dao.ReportItemRecordDao;
import com.lsts.inspection.dao.ReportItemValueDao;
import com.lsts.log.service.SysLogService;
import com.lsts.mobileapp.input.bean.TzsbRecordLog;
import com.lsts.mobileapp.input.dao.TzsbRecordLogDao;
import com.lsts.mobileapp.rcparse.bean.R3;
import com.lsts.mobileapp.rcparse.bean.R4;
import com.lsts.mobileapp.rcparse.dao.R3Dao;
import com.lsts.report.bean.Report;
import com.lsts.report.bean.ReportItem;
import com.lsts.report.dao.ReportDao;
import com.lsts.report.service.ReportItemService;

import jxl.write.DateFormat;
import net.sourceforge.jeval.Evaluator;
import util.LikeHashMap;
import util.ReportUtil;
import util.TS_Util;

/** 
 * @author 作者 廖增伟
 * @JDK 1.8
 * @version 创建时间：2018年6月10日 22:16:15  
 * 类说明
 */
@Service("r3Manager")
public class R3Manager extends EntityManageImpl<R3, R3Dao> {
	
	@Autowired
	private R3Dao r3Dao;
	
	@Autowired
	private InspectionInfoDao infoDao ;
	
	@Autowired
	private ReportItemRecordDao itemRecordDao;
	
	@Autowired
	private SysLogService logService;
	
	@Autowired
	private ReportDao ReportDao;
	
	@Autowired
	DeviceDao DeviceDao;
	
	
	@Autowired
	private TzsbRecordLogDao recordLogDao;
	
	@Autowired
	private ReportItemService ReportItemManager;
	
	@Autowired
	private BaseFlowConfigService tzsbWorkFlowService;
	
	@Autowired
	private ReportItemValueDao reportItemValueDao;
	
	/**
	 * 原始记录转化报告书后台执行主方法
	 * 
	 * @author 廖增伟
	 * @param fk_info_id 检验info表ID
	 * @return void
	 * @desc
	 */
	public void dealRecordToReport(HttpServletRequest request, InspectionInfo info) {
		try {
			// 获取设备信息对象
			DeviceDocument devicedoc = DeviceDao.get(info.getFk_tsjc_device_document_id());
			// 初始化设备返写参数集合
			Map<String, String> paramMap = new HashMap<String, String>();
			// 获取设备需要返写的参数集合
			Set<String> deviceMap = new HashSet<String>();
			deviceMap = devicedoc.update_columns();	
			
			// 删掉以前的报告数据信息
			r3Dao.deleteReportItemValue(info.getId());
			
			// 原始记录转换报告
			paramMap = beginParse(info, deviceMap, paramMap);
			
			// 处理系统参数
			parseBySysParams(info);
			
			// 处理报告书页码信息
			String report_pages = paramMap.get("REPORT_PAGES");
			updateReportPage(info, report_pages);
			
			// 处理下次检验日期
			String inspect_date = paramMap.get("INSPECT_DATE");
			String inspect_next_date = paramMap.get("INSPECT_NEXT_DATE");
			updateReportDate(info, inspect_date,inspect_next_date);
			
			// 反写设备信息
			paramMap.remove("REPORT_PAGES");
			paramMap.remove("INSPECT_DATE");
			updateDevice(devicedoc, paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 原始记录转化报告书后台执行主方法
	 * 
	 * @author 廖增伟
	 * @param InspectionInfo 检验info对象
	 * @return void
	 * @desc
	 *      > 判断有新增页的项目，并放到一个容器中，循环处理的时候需要，根据容器中这些有新增页的进行判断，来对报告的页码同步进行处理
	 *      > 循环转换关系表，一次循环里面的内容进行转换处理
	 *      > 根据转换规则，每条记录分别进行对应的转换
	 */
	public Map<String, String> beginParse(InspectionInfo info, Set<String> deviceMap, Map<String, String> paramMap) {
		//获取报告书ID
		String fkReportId = info.getReport_type();
		
		
		//取得需要转化的报告书对应的而所有转化对象list
		List<R3> pList = r3Dao.getParseListByReportId(fkReportId);
		
		//取得最后原始记录最新信息,是一个LikeHashMap，自定义的map容器，可以支持模糊检索，检索后返回一个list
		LikeHashMap<String,ReportItemRecord> recordMap = getItemRecordMapByInfoId(info.getId(), fkReportId);
		// 报告书页码
		String report_pages = "";
		
		// 循环每个转化规则对象
		// 反转规则报告项目
		String reportItems = "";
		for(R3 parse : pList) {
			//处理itemName的值（主要是需要去掉base__）
			String recordItemName = getSaveItemName(parse.getRecordItemName());
			//取得的值取得原始记录里面对应的所有保存记录List（有复制页的时候list数量就大于1了）
			List<ReportItemRecord> recordList = recordMap.get(recordItemName,false);
			//一次对recodeList进行转换处理，每个ir就是一条原始记录保存的内容
			for(ReportItemRecord ir : recordList) {
				if(ir==null) {
					continue;
				}
				// 转换数据
				if(parse.getFormuleType().equals("1")) {
					// 计算
					parseByCal(parse,info,ir);
				} else if (parse.getFormuleType().equals("2")) {
					// 判断
					parseByJudgement(parse,info,ir);
				} else if (parse.getFormuleType().equals("3")) {
					// 预定义规则
					parseByPredefine(parse,info,ir);
				} else if (parse.getFormuleType().equals("4")) {
					// 反转规则（即根据报告的项目名称查找原始记录对应的项目名称，再计算其结果生成报告内容，其他规则反之）
					if(!reportItems.contains(parse.getReportItemName())) {
						parseByReport(parse,info,ir);
						if(StringUtil.isNotEmpty(reportItems)) {
							reportItems += ",";
						}
						reportItems += parse.getReportItemName();
					}
				}
				
				// 获取转换后的报告书页码
				String record_page = ir.getPage_no();	// 原始记录页码
				String report_page = parse.getReportPageNo();	// 报告页码
				if(StringUtil.isNotEmpty(record_page) && StringUtil.isNotEmpty(report_page)){
					String pageNo = r3Dao.parsePageNo(record_page,report_page);
					if(!report_pages.contains(pageNo)) {
						if(StringUtil.isNotEmpty(report_pages)) {
							report_pages += ",";
						}
						report_pages += pageNo;
					}
				}
				
				// 组装返写设备参数集合
				// 将原始记录中的检验日期写入返写设备参数集合
				if (deviceMap.contains(ir.getItem_name().toUpperCase()) || "INSPECT_DATE".equals(ir.getItem_name().toUpperCase())
						|| "INSPECT_NEXT_DATE".equals(ir.getItem_name().toUpperCase())) {
					paramMap.put(ir.getItem_name().toUpperCase(), ir.getItem_value());
				}
				
			}
		}
		
		// 将报告页码写入返写信息参数集合
		paramMap.put("REPORT_PAGES", report_pages);
		
		return paramMap;
	}
	
	/**
	 * 根据计算进行转化
	 * 
	 * @author 廖增伟
	 * @param R3 转换规则对象 , InspectionInfo 报告书对象
	 * @return void
	 * @throws KhntException
	 * @desc 公式编写规则
	 * 		1、计算公式采用正常的数学运算编写方式。编制公式的时候，用value代替当前原始记录的值
	 */
	public void parseByCal(R3 r3, InspectionInfo info, ReportItemRecord ir ) throws KhntException {
		//处理itemName的值（主要是需要去掉base__）
		String reportItemName = getSaveItemName(r3.getReportItemName());
		
		Map<String,String> infoMap = new HashMap<String,String>();
		//组装需要调用保存方法的内容，这部分都是固定的，只有最终转换出来的值需要在下面的计算中确定
		infoMap.put("FK_INFO_ID", info.getId());
		infoMap.put("FK_REPORT_ID", info.getReport_type());
		infoMap.put("ITEM_NAME", reportItemName);
		infoMap.put("RECORD_PAGE_NO", ir.getPage_no());
		infoMap.put("PARSE_REPORT_PAGE_NO", r3.getReportPageNo());

		//先取得判断公式-需要把value转换成#{value},这个是因为使用jeval这个jar的要求
		String formule = r3Dao.getFormuleFromParse(r3,info).replaceAll("value", "#{value}");
		Evaluator eval = new Evaluator();
		eval.setVariables(Collections.singletonMap("value", ir.getItem_value()));
		try {
			String result =eval.evaluate(formule);
			//往infoMap插入转换后的值
			infoMap.put("ITEM_VALUE", result);
			//保存转换后的结果到报告表
			saveReportItemValue(infoMap,info);
		} catch (Exception e) {
			//如果没有配置转换公式，方法会抛出异常，所以这里捕获异常后，进行是否有配置默认值
			//如果也没有配置默认值，则继续抛出异常，否则直接用默认值作为转换结果。
			if(StringUtil.isEmpty(r3.getDefaultValue())) {
				throw new KhntException("没有配置转换公式并且没有配置默认值。报告书编号："+info.getReport_sn()+";项目名："+r3.getReportItemName());
			} else {
				//使用默认值作为转换结果
				//保存转换后结果到报告表
				infoMap.put("ITEM_VALUE", r3.getDefaultValue());
				saveReportItemValue(infoMap,info);
			}
		}
		
	}
	
	/**
	 * 根据判断进行转化
	 * 
	 * @author 廖增伟
	 * @param R3 转换规则对象 , InspectionInfo 报告书对象
	 * @return void
	 * @throws
	 * @desc 公式编写规则
	 *       1、如果是原始记录的值直接转化成报告的值，就直接录入=，这个也是最常见的情况，也是默认配置的时候默认值。
	 *       2、判断公式，采用IK Expression进行解析 一般使用 ? : 三元运算符，完全就是java的写法，可嵌套。编制公式的时候，用value代替当前原始记录的值
	 */
	public void parseByJudgement(R3 parse ,InspectionInfo info , ReportItemRecord ir ) throws KhntException  {
		//处理itemName的值（主要是需要去掉base__）
		String reportItemName = getSaveItemName(parse.getReportItemName());
		
		Map<String,String> infoMap = new HashMap<String,String>();
		//组装需要调用保存方法的内容，这部分都是固定的，只有最终转换出来的值需要在下面的计算中确定
		infoMap.put("FK_INFO_ID", info.getId());
		infoMap.put("FK_REPORT_ID", info.getReport_type());
		//infoMap.put("ITEM_NAME", parse.getReportItemName());
		infoMap.put("ITEM_NAME", reportItemName);
		infoMap.put("RECORD_PAGE_NO", ir.getPage_no());
		infoMap.put("PARSE_REPORT_PAGE_NO", parse.getReportPageNo());
		try {
			//先取得判断公式(为空会抛出异常，下面判断不需要判断为空)
			String formule = r3Dao.getFormuleFromParse(parse,info);
			if(formule.equals("=")) {//如果公式是=，则直接用原始记录的值进行保存
				//往infoMap插入转换后的值
				infoMap.put("ITEM_VALUE", ir.getItem_value());
			} else {
				if(formule.contains(RtPageType.TABLE)) {
					String itemValues = judgementItem(formule, info);
					//往infoMap插入转换后的值
					if(itemValues.contains(ReportConstant.ITEM_F)) {
						infoMap.put("ITEM_VALUE", ReportConstant.ITEM_F);
					}else {
						infoMap.put("ITEM_VALUE", ReportConstant.ITEM_S);
					}
				}else {
					List<Variable> variables = new ArrayList<Variable>();
					if(ir.getItem_value().contains(".")) {
						variables.add(Variable.createVariable("value", Double.valueOf(ir.getItem_value())));
					}else {
						variables.add(Variable.createVariable("value", Integer.valueOf(ir.getItem_value())));
					}
					Object result = ExpressionEvaluator.evaluate(formule, variables);
					System.out.println("Result = " + result);
					//往infoMap插入转换后的值
					infoMap.put("ITEM_VALUE", (String)result);
				}
			}
			//保存转换后的结果到报告表
			saveReportItemValue(infoMap,info);
			
		} catch(Exception e) {
			//如果没有配置转换公式，方法会抛出异常，所以这里捕获异常后，进行是否有配置默认值
			//如果也没有配置默认值，则继续抛出异常，否则直接用默认值作为转换结果。
			if(StringUtil.isEmpty(parse.getDefaultValue())) {
				throw new KhntException("没有配置转换公式并且没有配置默认值。报告书编号："+info.getReport_sn()+";项目名："+parse.getReportItemName());
			} else {
				//使用默认值作为转换结果
				//保存转换后结果到报告表
				infoMap.put("ITEM_VALUE", parse.getDefaultValue());
				saveReportItemValue(infoMap,info);
			}
		}
	}
	
	/**
	 * 根据判断进行转化检验项目
	 * 
	 * 
	 * @param formule -- 转换公式
	 * @param info -- 报告业务对象
	 * @return 转换结果字符串
	 * @throws
	 * @desc 公式编写规则
	 *       1、直接赋值模式：将原始记录的值，直接转化成报告的值，公式则直接为“=”即可，这是最常见的一对一直接赋值模式。
	 *       2、判断公式，采用IK Expression进行解析。一般使用 ? : 三元运算符，完全就是java的写法，可嵌套。编制公式的时候，用value代替当前原始记录的值
	 * @author GaoYa
	 * @since 2018-08-09 14:27:00
	 */
	private String judgementItem(String formule, InspectionInfo info) {
		String[] itemNames = formule.split(",");
		String itemValues = "";
		for (int i = 0; i < itemNames.length; i++) {
			String itemValue = "";
			List<R3> plist = r3Dao.getParseListByItemName(info.getReport_type(), itemNames[i]);
			if (!plist.isEmpty()) {
				R3 rParse = plist.get(0);
				List<ReportItemRecord> recordList = itemRecordDao.getRecordByItemName(info.getId(),
						info.getReport_type(), itemNames[i]);
				ReportItemRecord itemRecord = null;
				if (!recordList.isEmpty()) {
					itemRecord = recordList.get(0);
				}
				if (itemRecord == null) {
					continue;
				} else {
					if (StringUtil.isEmpty(itemRecord.getItem_value())) {
						continue;
					}
				}

				String item_formule = r3Dao.getFormuleFromParse(rParse, info);
				if (item_formule.equals("=")) {
					itemValue = itemRecord != null ? itemRecord.getItem_value() : rParse.getDefaultValue();
				} else {
					List<Variable> variables = new ArrayList<Variable>();
					if(itemRecord.getItem_value().contains(".")) {
						variables.add(Variable.createVariable("value", Double.valueOf(itemRecord.getItem_value())));
					}else {
						variables.add(Variable.createVariable("value", Integer.valueOf(itemRecord.getItem_value())));
					}
					Object result = ExpressionEvaluator.evaluate(item_formule, variables);
					System.out.println("Result = " + result);
					itemValue = (String) result;
				}
				if (StringUtil.isNotEmpty(itemValues)) {
					itemValues += ",";
				}
				itemValues += itemValue;
			}
		}
		return itemValues;
	}
	
	
	/**
	 * 根据预定义规则进行转化
	 * 
	 * @author GaoYa
	 * @param R3 转换规则对象
	 * @param InspectionInfo 报告书对象
	 * @param ReportItemRecord 原始记录对象
	 * @return void
	 * @throws KhntException
	 * @desc 针对原始记录中复选框的情况，获取打勾的项目文本默认值作为报告项目内容
	 */
	public void parseByPredefine(R3 parse, InspectionInfo info, ReportItemRecord ir ) throws KhntException {
		//处理itemName的值（主要是需要去掉base__）
		String reportItemName = getSaveItemName(parse.getReportItemName());
		
		Map<String,String> infoMap = new HashMap<String,String>();
		//组装需要调用保存方法的内容，这部分都是固定的，只有最终转换出来的值需要在下面的计算中确定
		infoMap.put("FK_INFO_ID", info.getId());
		infoMap.put("FK_REPORT_ID", info.getReport_type());
		infoMap.put("ITEM_NAME", reportItemName);
		infoMap.put("RECORD_PAGE_NO", ir.getPage_no());
		infoMap.put("PARSE_REPORT_PAGE_NO", parse.getReportPageNo());

		try {
			if(StringUtil.isNotEmpty(ir.getItem_value())) {
				String report_value = "";
				Map<String, String> charMap = null;
				String item_pre = RtPath.getPropetyValue("inspect_conclusion", "conclusion");
				String item_s = "";
				String item_f = "";
				if(ir.getItem_name().contains(item_pre)) {
					charMap = ReportConstant.char_to_JlMap();
					item_s = ReportConstant.ITEM_JL_S;
					item_f = ReportConstant.ITEM_JL_F;
				}else {
					charMap = ReportConstant.char_to_JgMap();
					item_s = ReportConstant.ITEM_S;
					item_f = ReportConstant.ITEM_F;
				}
				for (Map.Entry<String, String> entry : charMap.entrySet()) {
					if (ir.getItem_value().contains(item_s)
							&& !ir.getItem_value().contains(item_f)) {
						report_value = item_s;
						break;
					} else if (ir.getItem_value().contains(item_f)) {
						report_value = item_f;
						break;
					} else {
						if (ir.getItem_value().contains(entry.getKey())) {
							report_value = entry.getValue();
							break;
						}
					}
				}
				if(StringUtil.isNotEmpty(report_value)) {
					// 往infoMap插入转换后的值
					infoMap.put("ITEM_VALUE", report_value);
				}else {
					// 使用默认值作为转换结果
					infoMap.put("ITEM_VALUE", parse.getDefaultValue());
				}
				
				// 保存转换后的结果到报告表
				saveReportItemValue(infoMap,info);
			}else {
				// 使用默认值作为转换结果
				// 往infoMap插入转换后的值
				infoMap.put("ITEM_VALUE", parse.getDefaultValue());
				// 保存转换后的结果到报告表
				saveReportItemValue(infoMap,info);
			}
			
		} catch (Exception e) {
			//如果没有配置转换公式，方法会抛出异常，所以这里捕获异常后，进行是否有配置默认值
			//如果也没有配置默认值，则继续抛出异常，否则直接用默认值作为转换结果。
			if(StringUtil.isEmpty(parse.getDefaultValue())) {
				throw new KhntException("没有配置转换公式并且没有配置默认值。报告书编号："+info.getReport_sn()+";项目名："+parse.getReportItemName());
			} else {
				//使用默认值作为转换结果
				//保存转换后结果到报告表
				infoMap.put("ITEM_VALUE", parse.getDefaultValue());
				saveReportItemValue(infoMap,info);
			}
		}
	}
	
	/**
	 * 根据报告项目进行反转判断
	 * 
	 * @param parse -- 转换规则对象
	 * @param info -- 报告书对象
	 * @param ir -- 原始记录对象
	 * 
	 * @return void
	 * @throws KhntException
	 * @author GaoYa
	 * @since 2018-08-10 11:29:00
	 */
	public void parseByReport(R3 parse, InspectionInfo info, ReportItemRecord ir) throws KhntException {
		// 处理itemName的值（主要是需要去掉base__）
		String reportItemName = getSaveItemName(parse.getReportItemName());

		Map<String, String> infoMap = new HashMap<String, String>();
		// 组装需要调用保存方法的内容，这部分都是固定的，只有最终转换出来的值需要在下面的计算中确定
		infoMap.put("FK_INFO_ID", info.getId());
		infoMap.put("FK_REPORT_ID", info.getReport_type());
		// infoMap.put("ITEM_NAME", parse.getReportItemName());
		infoMap.put("ITEM_NAME", reportItemName);
		infoMap.put("RECORD_PAGE_NO", ir.getPage_no());
		infoMap.put("PARSE_REPORT_PAGE_NO", parse.getReportPageNo());
		try {
			// 先取得判断公式(为空会抛出异常，下面判断不需要判断为空)
			List<R3> recordParseList = r3Dao.getParsesByReportItem(info.getReport_type(),
					parse.getReportItemName());
			String itemValues = "";
			if (!recordParseList.isEmpty()) {
				for (R3 reParse : recordParseList) {
					String formule = r3Dao.getFormuleFromParse(reParse, info);
					if (formule.equals("=")) {
						if (StringUtil.isNotEmpty(itemValues)) {
							itemValues += ",";
						}
						itemValues += ir.getItem_value();
					} else {
						List<Variable> variables = new ArrayList<Variable>();
						if (ir.getItem_value().contains(".")) {
							variables.add(Variable.createVariable("value", Double.valueOf(ir.getItem_value())));
						} else {
							variables.add(Variable.createVariable("value", Integer.valueOf(ir.getItem_value())));
						}
						Object result = ExpressionEvaluator.evaluate(formule, variables);
						if (StringUtil.isNotEmpty(itemValues)) {
							itemValues += ",";
						}
						itemValues += result;
					}
				}
			}

			// 往infoMap插入转换后的值
			if (itemValues.contains(ReportConstant.ITEM_F)) {
				infoMap.put("ITEM_VALUE", ReportConstant.ITEM_F);
			} else {
				infoMap.put("ITEM_VALUE", ReportConstant.ITEM_S);
			}

			// 保存转换后的结果到报告表
			saveReportItemValue(infoMap,info);

		} catch (Exception e) {
			// 如果没有配置转换公式，方法会抛出异常，所以这里捕获异常后，进行是否有配置默认值
			// 如果也没有配置默认值，则继续抛出异常，否则直接用默认值作为转换结果。
			if (StringUtil.isEmpty(parse.getDefaultValue())) {
				throw new KhntException(
						"没有配置转换公式并且没有配置默认值。报告书编号：" + info.getReport_sn() + ";项目名：" + parse.getReportItemName());
			} else {
				// 使用默认值作为转换结果
				// 保存转换后结果到报告表
				infoMap.put("ITEM_VALUE", parse.getDefaultValue());
				saveReportItemValue(infoMap,info);
			}
		}
	}
	
	/**
	 * 处理报告内容之系统参数
	 * 
	 * @param 
	 * @return void
	 * @throws KhntException
	 * @author GaoYa
	 * @since 2018-09-26 14:48:00
	 */
	public void parseBySysParams(InspectionInfo info) throws KhntException {
		//处理报告系统参数
		String[] report_sys_params = ReportConstant.REPORT_SYS_PARAMS;
		for (int i = 0; i < report_sys_params.length; i++) {
			List<ReportItemValue> reportLists = reportItemValueDao.queryItems(info.getId(), info.getReport_type(), report_sys_params[i]);
			if(!reportLists.isEmpty()) {
				r3Dao.deleteReportItems(info.getId(), info.getReport_type(), report_sys_params[i]);
			}
			
			String item_name = report_sys_params[i]
					.substring(report_sys_params[i].indexOf("_") + 1);

			String item_value = "";
			try {
				item_value = ReportUtil.dealContent(Factory.getSysPara().getProperty(report_sys_params[i])) + "";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			Map<String, String> infoMap = new HashMap<String, String>();
			// 组装需要调用保存方法的内容，这部分都是固定的，只有最终转换出来的值需要在下面的计算中确定
			infoMap.put("FK_INFO_ID", info.getId());
			infoMap.put("FK_REPORT_ID", info.getReport_type());
			infoMap.put("ITEM_NAME", item_name.toLowerCase());
			infoMap.put("ITEM_VALUE", item_value);
			saveReportItemValue(infoMap,info);
		}
	}
	
	/**
	 * 对项目的名称进行处理（针对关联基础数据的字段，需要去掉base__）
	 * 
	 * @author 廖增伟
	 * @param  String ITME_NAME
	 * @return String
	 * @throws
	 */
	public String getSaveItemName(String name) {
		if (!(name.contains(RtPageType.TABLE) || name.contains("FK") || name.contains("fk") || name.contains("picture")
				|| name.startsWith(RtPath.getPropetyValue("inspect_record", "record") + "__")
				|| name.startsWith(RtPath.getPropetyValue("inspect_conclusion", "conclusion") + "__"))) {
			return name.replace("base__", "").replace("BASE__", "");
		} else {
			return name;
		}
	}
		
	//根据检验业务info表ID和baseReport表ID取得原始记录最新数据的list
	public LikeHashMap<String,ReportItemRecord> getItemRecordMapByInfoId(String fk_info_id,String fk_report_id){
		LikeHashMap<String,ReportItemRecord> map = new LikeHashMap<String,ReportItemRecord>();
		List<ReportItemRecord> list = itemRecordDao.getItemRecordListByInfoId(fk_info_id,fk_report_id);
		for (ReportItemRecord ir : list ) {
			map.put(ir.getItem_name(), ir);
		}
		return map;
	}
	/**
	 * 保存转换之后的报告书内容
	 * 
	 * @author 廖增伟
	 * @param  R3 转换规则对象 , InspectionInfo 报告书对象
	 * @return void
	 * @throws
	 */
	/*public void InsertReportItemValue(String Fk_report_id, String info_id , String item_name , String item_value ,String page_no ) {
		//调用对应的Dao的保存方法
		reportItemValueDao.insertReportItemValue(StringUtil.getUUID(),
				Fk_report_id,item_name,
				item_value,info_id,page_no);
	}*/
	
	/**
	 * 获取请求参数
	 * 
	 * @param request
	 * 
	 * @return 
	 * @throws 
	 * @author GaoYa
	 * @since 2018-08-06 17:19:00
	 */
	public Map<String, String> getReqParams(HttpServletRequest request) {
		// 获取报告业务ID
		String fkInfoId = request.getParameter("fk_info_id");
		// 获取下一步审核操作人
		String nextOp_id = request.getParameter("nextOp");
		String nextOp_name = request.getParameter("nextOpName");
		// 获取原始记录校核信息
		String conclusion = request.getParameter("conclusion");
		String opDate = request.getParameter("opDate");
		String remark = request.getParameter("remark");

		// 组装请求参数
		Map<String, String> reqParamsMap = new HashMap<String, String>();
		reqParamsMap.put("INFO_ID", fkInfoId);
		reqParamsMap.put("NEXT_OP_ID", nextOp_id);
		reqParamsMap.put("NEXT_OP_NAME", nextOp_name);
		reqParamsMap.put("CONCLUSION", conclusion);
		reqParamsMap.put("OP_DATE", opDate);
		reqParamsMap.put("REMARK", remark);
		return reqParamsMap;
	}
	
	/**
	 * 启动流程
	 * 
	 * @param request
	 * 
	 * @return 
	 * @throws 
	 * @author GaoYa
	 * @since 2018-08-07 08:47:00
	 */
	public HashMap<String, Object> startFlow(HttpServletRequest request, InspectionInfo info) {
		// 返回参数集合
		HashMap<String, Object> result = new HashMap<String, Object>();
		// 提交流程参数集合
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		// 查找流程
		String infoId = info.getId();
		String flowId = null;
		// 获取报检信息
		Inspection inspection = info.getInspection();
		String check_type = "";
		if (inspection != null) {
			check_type = inspection.getCheck_type();
		}
		if (StringUtil.isNotEmpty(check_type)) {
			
			flowId = r3Dao.getFlowId(check_type, info.getCheck_unit_id(), info.getReport_type(),true);
		}
		// 没有流程时，返回
		if (flowId == null) {
			result.put("success", false);
			result.put("msg", "没有找到对应的流程，请检查配置！");
			//throw new KhntException("没有找到对应的流程，请检查配置！");
		}else {		
			// 先判断业务是否已经启动了流程
			// 业务流程表有数据就说明已经启动了流程，不再重新启动流程，没数据就启动流程
			String process_id = tzsbWorkFlowService.getProcess(infoId);
			if(StringUtil.isEmpty(process_id)){
				map.put("infoId", infoId);
				map.put("flowId", flowId);
				map.put("status", Factory.getSysPara().getProperty("input"));
				map.put("personArray", ReportUtil.getCheckOps(info));
				// 启动流程
				tzsbWorkFlowService.StartFlowProcess(map, request);
			}
			result.put("success", true);
		}

		// 设置返回参数	
		result.put("infoId", infoId);
		
		/*result.put("orgId", info.getCheckUnitId());
		result.put("reportId", info.getReport_type());
		result.put("deviceId", info.getFk_tsjc_device_document_id());*/

		return result;
	}
	
	/**
	 * 返写设备信息
	 * 
	 * @param device -- 设备信息对象
	 * @param deviceMap -- 需要返写的参数集合
	 * 
	 * @return 
	 * @throws 
	 * @author GaoYa
	 * @since 2018-07-31 11:25:00
	 */
	private void updateDevice(DeviceDocument device, Map<String, String> paramMap) {
		try {
			// 返写设备信息
			for (Map.Entry<String, String> entry : paramMap.entrySet()) { 
				// 返写时，排除非设备参数信息及不返写的参数
				if("REPORT_PAGES".equals(entry.getKey().toLowerCase())) {
					continue;
				}
				if("INSPECT_DATE".equals(entry.getKey().toLowerCase())) {
					continue;
				}
				setDeviceValue(device, entry.getKey().toLowerCase(), entry.getValue());
			}
			// 保存设备信息
			DeviceDao.save(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新原始记录校核状态
	 * 
	 * @param info -- 报告业务信息对象
	 * 
	 * @return 
	 * @throws 
	 * @author GaoYa
	 * @since 2018-08-07 14:40:00
	 */
	public void updateRecordFlow(InspectionInfo info) {
		try {
			// 更新原始记录状态
			info.setRecordFlow("2");
			info.setRecordConfirmId("");
			info.setRecordConfirmOp("");
			infoDao.save(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新原始记录转换报告状态
	 * 
	 * @param info -- 报告业务信息对象
	 * 
	 * @return 
	 * @throws 
	 * @author GaoYa
	 * @since 2018-08-07 14:40:00
	 */
	public void updateRecordStatus(InspectionInfo info) {
		try {
			// 更新原始记录转换报告状态（0：未转换 1：已转换）
			info.setRecordConvertStatus("1");
			infoDao.save(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 记录操作日志
	 * 
	 * @param request -- 请求对象
	 * @param info -- 报告业务信息对象
	 * 
	 * @return 
	 * @throws 
	 * @author GaoYa
	 * @since 2018-08-07 14:40:00
	 */
	public void addRecordLog(HttpServletRequest request, InspectionInfo info) {
		try {
			CurrentSessionUser user = SecurityUtil.getSecurityUser();

			// 记录日志
			TzsbRecordLog log  = new TzsbRecordLog();
			log.setBusiness_id(info.getId());
			log.setOp_action("原始记录提交审核");
			log.setOp_ip(TS_Util.getIpAddress(request));
			log.setOp_remark("原始记录提交审核");
			log.setOp_status("提交审核");
			log.setOp_time(new Date());
			log.setOp_user_id(user.getId());
			log.setOp_user_name(user.getName());
			recordLogDao.save(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setDeviceValue(Object obj ,String key , String value) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Field[] fields =  obj.getClass().getDeclaredFields();
		Field field = null;
		for (Field f : fields) {
			if(f.getName().equals(key)) {
				field = f;
				break;
			}
		}
		if(null == field) {
			return;
		}
		Comment c = field.getAnnotation(Comment.class);
		if(null == c || !"true".equals(c.reverse())) {
			return;
		}
		Method method = obj.getClass().getMethod("set" + upperFirstCase(key), field.getType());
		if(null == method) {
			return;
		}
		String fieldType = field.getGenericType().toString();
		if("class java.util.Date".equals(fieldType)) 
		{
			Date date = null;
			try {
				date = sdf.parse(value);
			} catch (Exception e) {
				System.out.println("日期转换错误！！！");
				return;
			}
			method.invoke(obj, date);
		}
		else if("class java.lang.String".equals(fieldType))
		{
			method.invoke(obj, value);
		}
		else if("class java.lang.Integer".equals(fieldType) || "int".equals(field.getType().toString()))
		{
			method.invoke(obj, Integer.valueOf(value));
		}
		else if("class java.lang.Long".equals(fieldType) || "long".equals(field.getType().toString()))
		{
			method.invoke(obj, Long.valueOf(value));
		}
		else if("class java.lang.Float".equals(fieldType) || "float".equals(field.getType().toString()))
		{
			method.invoke(obj, Float.valueOf(value));
		}
		else if("class java.lang.Double".equals(fieldType) || "double".equals(field.getType().toString()))
		{
			method.invoke(obj, Double.valueOf(value));
		}
		else if("class java.lang.Short".equals(fieldType) || "short".equals(field.getType().toString()))
		{
			method.invoke(obj, Short.valueOf(value));
		}
		else if("class java.lang.Byte".equals(fieldType) || "byte".equals(field.getType().toString()))
		{
			method.invoke(obj, Byte.valueOf(value));
		}
	}
	
	private static String upperFirstCase(String str){
		return str.substring(0, 1).toUpperCase()
				+ str.substring(1, str.length());
	}
	
	/**
	 * 返写报告书页码
	 * 
	 * @param report_pages -- 报告书页码
	 * 
	 * @return 
	 * @throws 
	 * @author GaoYa
	 * @since 2018-07-31 16:14:00
	 */
	private void updateReportPage(InspectionInfo info, String report_pages) {
		try {
			if (StringUtil.isNotEmpty(report_pages)) {
				// 获取报告目录固定必选页
				List<ReportItem>  reportItemList = ReportItemManager.queryMustPageByReportId(info.getReport_type());
				if(!reportItemList.isEmpty()) {
					for (ReportItem ReportItem : reportItemList) {
						// 如果报告页码不包括固定必选页，则加上
						if (!report_pages.contains(ReportItem.getPage_index())) {
							if (StringUtil.isNotEmpty(report_pages)) {
								report_pages += ",";
							}
							report_pages += ReportItem.getPage_index();
						}
					}
				}
				
				// 将报告页码进行排序处理
				String[] pages = report_pages.split(",");
				int temp = -1;
				for (int i = 0; i < pages.length; i++) {
					for (int j = i + 1; j < pages.length; j++) {
						if (Integer.parseInt(pages[i]) > Integer.parseInt(pages[j])) {
							temp = Integer.parseInt(pages[i]);
							pages[i] = pages[j];
							pages[j] = String.valueOf(temp);
						}
					}
				}
				
				String reportItem = "";
				for(int i = 0;i<pages.length;i++){
					if(StringUtil.isNotEmpty(reportItem)) {
						reportItem += ",";
					}
					reportItem += pages[i];      
				}
				
				// 将报告页码返写入报告业务信息
				info.setReport_item(reportItem);
				
				// 保存报告业务信息
				infoDao.save(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 返写报告书下次检验日期
	 * 
	 * @param inspect_date -- 检验日期
	 * @param inspect_next_date 
	 * 
	 * @return 
	 * @throws 
	 * @author GaoYa
	 * @since 2018-08-09 16:50:00
	 */
	private void updateReportDate(InspectionInfo info, String inspect_date, String inspectNextDate) {
		try {
			SimpleDateFormat df  = new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtil.isNotEmpty(inspectNextDate)) {
				//填写了下次检验日期，以填写为准
				if (StringUtil.isNotEmpty(inspect_date)) {
					//报告里面填写的检验日期反写业务表
					info.setAdvance_time( df.parse(inspect_date));
				}
				// 将下次检验日期返写入报告业务信息
				info.setLast_check_time(df.parse(inspect_date));
			}else {
				if (StringUtil.isEmpty(inspect_date)) {
					if(info.getAdvance_time()!=null) {
						inspect_date = DateUtil.format(info.getAdvance_time(), "yyyy-MM-dd");
					}
				}else {
					//报告里面填写的检验日期反写业务表
					info.setAdvance_time( new SimpleDateFormat("yyyy-MM-dd").parse(inspect_date));
				}
				
				if(StringUtil.isNotEmpty(inspect_date)) {
					// 获取报检信息
					Inspection inspection = info.getInspection();
					String check_type = "3";	// 默认定检
					if (inspection != null) {
						check_type = inspection.getCheck_type();
					}
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd", inspect_date));
					
					// 定检时，下次检验日期翻一年；监检时，下次检验日期翻两年
					if("3".equals(check_type)) {
						// 下次检验日期翻一年
						calendar.add(Calendar.YEAR, 1);	
					}else if("2".equals(check_type)) {
						// 下次检验日期翻两年
						calendar.add(Calendar.YEAR, 2);	
					}else {
						// 默认下次检验日期翻一年
						calendar.add(Calendar.YEAR, 1);	
					}
					
					calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	
					// 将下次检验日期返写入报告业务信息
					info.setLast_check_time(calendar.getTime());
					
					
					inspectNextDate =DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
				}
			
				//往报告书内容表插入转换后的内容作为报告书内容
				r3Dao.insertReportItemValue(info.getReport_type(),"inspect_next_date",
						inspectNextDate,info.getId(),"");
				
				// 保存报告业务信息
				infoDao.save(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存报告与原始记录对应关系信息
	 * 
	 * @author GaoYa
	 * @param r4,HttpServletRequest
	 * @return 
	 * @throws 
	 */
	public void saveBasic(R4 r4,HttpServletRequest request) throws Exception {
			CurrentSessionUser user = SecurityUtil.getSecurityUser();
			String report_id = "";			
			String report_name = "";			
			for (R3 r3 : r4.getR3s()) {	
				// 查找并作废已存在的对应关系记录
				List<R3> list = r3Dao.getParseListByItemName(r3.getFkReportId(), r3.getRecordItemName());
				if(!list.isEmpty()) {
					for(R3 parse : list) {
						// 设置已作废
						parse.setData_status("99");
						parse.setLast_upd_by(user.getId()); 	// 最后修改人id
						parse.setLast_upd_name(user.getName()); // 最后修改人姓名
						parse.setLast_upd_date(new Date()); 	// 最后修改时间
						r3Dao.save(parse);
					}
				}
				if(StringUtil.isEmpty(report_id)) {
					report_id = r3.getFkReportId();
					Report report = ReportDao.get(report_id);
					if(report!=null) {
						report_name = report.getReport_name();
					}
				}
				if(StringUtil.isNotEmpty(report_name)) {
					r3.setReportName(report_name);
				}
				r3.setData_status("0");	// 数据状态（0：正常 99：已作废 ）
				r3.setLast_upd_by(user.getId()); 		// 最后修改人id
				r3.setLast_upd_name(user.getName()); 	// 最后修改人姓名
				r3.setLast_upd_date(new Date()); 		// 最后修改时间
				r3Dao.save(r3);
			}
			logService.setLogs(report_id, "添加对应关系", "添加对应关系", user.getId(), user
					.getName(), new Date(), request.getRemoteAddr(),"1");
	}
	
	/**
	 * 根据报告id获取报告与原始记录对应关系信息
	 * 
	 * @author Gaoya
	 * @param report_id -- 报告ID
	 * @return HashMap<String, Object>
	 * @throws 
	 */
	public HashMap<String, Object> getRelations(String report_id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			List<R3> list = r3Dao
					.getParseListByReportId(report_id);
			map.put("reportRecordParse", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
		}
		return map;
	}
	
	/**
	 * 根据报告id获取报告与原始记录对应关系信息
	 * 
	 * @author Gaoya
	 * @param report_id -- 报告ID
	 * @return HashMap<String, Object>
	 * @throws 
	 */
	public List<R3> getRelationInfos(String report_id, String record_item_name) {
		List<R3> list = new ArrayList<R3>();
		try {
			list = r3Dao.getParsesByItemName(report_id, record_item_name);
			if(list.isEmpty()) {
				list = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
		}
		return list;
	}
	
	/**
	 * 根据原始记录项目表取得有新增页的项目（包括原始页和所有新增页的itemName）
	 * 
	 * @author 廖增伟
	 * @param List<String>
	 * @return Set<String> 
	 * @throws 
	 */
	public Set<String> filterItemName(List<String> listIr) {
		//定义缓存当前项目的Set
		Set<String> itemSet = new HashSet<String>();
		//定期最后返回有分页的项目Set
		Set<String> itemRet = new HashSet<String>();
		//开始循环处理list
		for(String item : listIr) {
			//首先根据__进行拆分，取得前面一部分；有分页的一定有__
			String itemA = item.split("__")[0];
			//如果缓存Set包含了这个item，则加入到返回的Set中，否则，加入到缓存Set
			if(itemSet.contains(itemA)) {
				itemRet.add(item);
				//因为第一次发现有新增页的时候，需要把原始页的itemName也放进来
				if(!itemRet.contains(itemA)) {
					itemRet.add(itemA);
				}
			} else {
				itemSet.add(itemA);
			}
		}
		return itemRet;
	}
	
	/**
	 * 删除对应关系信息
	 * 
	 * @param request -- 请求对象
	 * @param ids -- 对应关系记录id
	 * 
	 * @return 
	 * @throws 
	 * @author GaoYa
	 * @since 
	 */
	public void del(HttpServletRequest request, String id) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		// 作废处理
		r3Dao.createSQLQuery("update TZSB_REPORT_RECORD_PARSE set data_status='99' where id = ?", id)
				.executeUpdate();
		// 记录日志
		TzsbRecordLog log = new TzsbRecordLog();
		log.setBusiness_id(id);
		log.setOp_action("作废对应关系");
		log.setOp_ip(TS_Util.getIpAddress(request));
		log.setOp_remark("作废对应关系");
		log.setOp_status("作废");
		log.setOp_time(new Date());
		log.setOp_user_id(user.getId());
		log.setOp_user_name(user.getName());
		recordLogDao.save(log);
	}
	
	// 启用对应关系
	public HashMap<String, Object> enable(HttpServletRequest request) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String ids = request.getParameter("ids"); // 对应关系记录ID
		try {
			String temp[] = ids.split(",");
			for (int i = 0; i < temp.length; i++) {
				R3 reportRecordParse = r3Dao.get(temp[i]);
				// 数据状态（0：启用  99：停用）
				reportRecordParse.setData_status("0");
				reportRecordParse.setLast_upd_by(user.getId()); 	// 最后修改人id
				reportRecordParse.setLast_upd_name(user.getName()); // 最后修改人姓名
				reportRecordParse.setLast_upd_date(new Date()); 	// 最后修改时间
				r3Dao.save(reportRecordParse);
				
				// 记录日志
				TzsbRecordLog log = new TzsbRecordLog();
				log.setBusiness_id(temp[i]);
				log.setOp_action("启用对应关系");
				log.setOp_ip(TS_Util.getIpAddress(request));
				log.setOp_remark("启用对应关系");
				log.setOp_status("启用");
				log.setOp_time(new Date());
				log.setOp_user_id(user.getId());
				log.setOp_user_name(user.getName());
				recordLogDao.save(log);
			}
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "请求超时，请稍后再试！");
		}
		return map;
	}

	// 停用对应关系
	public HashMap<String, Object> disable(HttpServletRequest request) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String ids = request.getParameter("ids"); // 对应关系记录ID
		try {
			String temp[] = ids.split(",");
			for (int i = 0; i < temp.length; i++) {
				R3 reportRecordParse = r3Dao.get(temp[i]);
				// 数据状态（0：启用  99：停用）
				reportRecordParse.setData_status("99");
				reportRecordParse.setLast_upd_by(user.getId()); 	// 最后修改人id
				reportRecordParse.setLast_upd_name(user.getName()); // 最后修改人姓名
				reportRecordParse.setLast_upd_date(new Date()); 	// 最后修改时间
				r3Dao.save(reportRecordParse);
				
				// 记录日志
				TzsbRecordLog log = new TzsbRecordLog();
				log.setBusiness_id(temp[i]);
				log.setOp_action("停用对应关系");
				log.setOp_ip(TS_Util.getIpAddress(request));
				log.setOp_remark("停用对应关系");
				log.setOp_status("停用");
				log.setOp_time(new Date());
				log.setOp_user_id(user.getId());
				log.setOp_user_name(user.getName());
				recordLogDao.save(log);
			}
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "请求超时，请稍后再试！");
		}
		return map;
	}
	
	// 对比对应关系
	public HashMap<String, Object> compare(HttpServletRequest request) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String report_id = request.getParameter("report_id"); // 报告模版ID
		try {
			int error_count = 0;
			List<R3> list = r3Dao.getParsesByReportId(report_id);
			for(R3 parse : list) {
				String record_pref = RtPath.getPropetyValue("inspect_record", "record");
				String conclusion_pref = RtPath.getPropetyValue("inspect_conclusion", "conclusion");
				if (parse.getRecordItemName().contains(record_pref)) {
					String record_suf = parse.getRecordItemName().substring(record_pref.length());
					String report_suf = parse.getReportItemName().substring(record_pref.length());
					if(!record_suf.equals(report_suf)) {
						error_count ++;
						// 数据状态（0：启用 98：潜在错误对应项 99：停用）
						parse.setData_status("98");
						parse.setLast_upd_by(user.getId()); 	// 最后修改人id
						parse.setLast_upd_name(user.getName()); // 最后修改人姓名
						parse.setLast_upd_date(new Date()); 	// 最后修改时间
						r3Dao.save(parse);
					}
				}else if(parse.getRecordItemName().contains(conclusion_pref)) {
					String record_suf = parse.getRecordItemName().substring(conclusion_pref.length());
					String report_suf = parse.getReportItemName().substring(conclusion_pref.length());
					if(!record_suf.equals(report_suf)) {
						error_count ++;
						// 数据状态（0：启用 98：潜在错误对应项 99：停用）
						parse.setData_status("98");
						parse.setLast_upd_by(user.getId()); 	// 最后修改人id
						parse.setLast_upd_name(user.getName()); // 最后修改人姓名
						parse.setLast_upd_date(new Date()); 	// 最后修改时间
						r3Dao.save(parse);
					}
				}
			}
			map.put("success", true);
			map.put("error_count", error_count);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "请求超时，请稍后再试！");
		}
		return map;
	}
	
	// 取消对应关系错误标记
	public HashMap<String, Object> cancelTags(HttpServletRequest request) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String ids = request.getParameter("ids"); // 对应关系记录ID
		try {
			String temp[] = ids.split(",");
			for (int i = 0; i < temp.length; i++) {
				R3 reportRecordParse = r3Dao.get(temp[i]);
				// 数据状态（0：启用 98：潜在错误对应项 99：停用）
				if("98".equals(reportRecordParse.getData_status())) {
					reportRecordParse.setData_status("0");
					reportRecordParse.setLast_upd_by(user.getId()); // 最后修改人id
					reportRecordParse.setLast_upd_name(user.getName()); // 最后修改人姓名
					reportRecordParse.setLast_upd_date(new Date()); // 最后修改时间
					r3Dao.save(reportRecordParse);

					// 记录日志
					TzsbRecordLog log = new TzsbRecordLog();
					log.setBusiness_id(temp[i]);
					log.setOp_action("取消潜在错误对应项");
					log.setOp_ip(TS_Util.getIpAddress(request));
					log.setOp_remark("取消潜在错误对应项");
					log.setOp_status("取消错误标记");
					log.setOp_time(new Date());
					log.setOp_user_id(user.getId());
					log.setOp_user_name(user.getName());
					recordLogDao.save(log);
				}
			}
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "请求超时，请稍后再试！");
		}
		return map;
	}
	
	/**
	 * 根据对应关系记录ids获取对应关系记录
	 * 
	 * 
	 * @param ids -- 对应关系记录Ids
	 * @return HashMap<String, Object>
	 * @throws 
	 * 
	 * @author Gaoya
	 */
	public HashMap<String, Object> getRelationsByIds(String ids) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			List<R3> list = r3Dao
					.getParseListByIds(ids);
			map.put("reportRecordParse", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
		}
		return map;
	}
	
	
	/**
	 * 批量修改报告与原始记录对应关系信息
	 * 
	 * 
	 * @param r4,HttpServletRequest
	 * @return 
	 * @throws 
	 * 
	 * @author GaoYa
	 */
	public void saveBasic2(R4 r4,
			HttpServletRequest request) throws Exception {
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		try {
			for (R3 r3 : r4.getR3s()) {
				R3 parse = r3Dao.get(r3.getId());
				parse.setFormule(r3.getFormule()); 			// 计算公式
				parse.setFormuleType(r3.getFormuleType()); 	// 转换规则
				parse.setDefaultValue(r3.getDefaultValue()); 	// 默认值
				parse.setLast_upd_by(user.getId()); 		// 最后修改人id
				parse.setLast_upd_name(user.getName()); 	// 最后修改人姓名
				parse.setLast_upd_date(new Date()); 		// 最后修改时间
				parse.setData_status(r3.getData_status());
				r3Dao.save(parse);

				logService.setLogs(parse.getId(), "修改对应关系",
						parse.getRecordItemName() + "--" + parse.getReportItemName(), user.getId(), user.getName(),
						new Date(), request.getRemoteAddr(), "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
		}
	}
	
	public void saveReportItemValue(Map<String,String> infoMap,InspectionInfo info) {

		//反检验业务表
		String itemName =  infoMap.get("ITEM_NAME");
		String itemValue =  infoMap.get("ITEM_VALUE");
		
		//检验结论
		if(itemName.toUpperCase().equals("INSPECTION_CONCLUSION")) {
			//devicedoc.setInspect_conclusion(value);
			info.setInspection_conclusion(itemValue);
		}
		
		//单位名称
		else if(itemName.toUpperCase().equals("COM_NAME")) {
			//devicedoc.setInspect_conclusion(value);
			info.setReport_com_name(itemValue);
		}
		//单位地址
		else if(itemName.toUpperCase().equals("COM_ADDRESS")) {
			//devicedoc.setInspect_conclusion(value);
			info.setReport_com_address(itemValue);
		}
		
		infoDao.save(info);
		r3Dao.saveReportItemValue(infoMap);
	}
	
}
