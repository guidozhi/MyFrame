package com.lsts.inspection.service;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.core.crud.manager.impl.EntityManageImpl;
import com.khnt.core.crud.web.support.JsonWrapper;
import com.khnt.rbac.impl.bean.Employee;
import com.khnt.rbac.impl.bean.User;
import com.khnt.rtbox.config.bean.ReportMark;
import com.khnt.rtbox.config.bean.RtExportData;
import com.khnt.rtbox.config.dao.ReportMarkDao;
import com.khnt.rtbox.config.dao.RtPageDao;
import com.khnt.rtbox.config.service.RtPageManager;
import com.khnt.rtbox.template.constant.RtExportDataType;
import com.khnt.rtbox.template.constant.RtField;
import com.khnt.rtbox.template.constant.RtPageType;
import com.khnt.rtbox.template.handle.export.RtSaveAsst;
import com.khnt.security.CurrentSessionUser;
import com.khnt.security.util.SecurityUtil;
import com.khnt.utils.DateUtil;
import com.khnt.utils.StringUtil;
import com.lsts.common.dao.CodeTablesDao;
import com.lsts.common.service.InspectionCommonService;
import com.lsts.constant.Constant;
import com.lsts.device.bean.Accessory;
import com.lsts.device.bean.BoilerPara;
import com.lsts.device.bean.CablewayPara;
import com.lsts.device.bean.CranePara;
import com.lsts.device.bean.DeviceDocument;
import com.lsts.device.bean.ElevatorPara;
import com.lsts.device.bean.EnginePara;
import com.lsts.device.bean.PressurevesselsPara;
import com.lsts.device.bean.RidesPara;
import com.lsts.device.dao.AccessoryDao;
import com.lsts.device.dao.BoilerParaDao;
import com.lsts.device.dao.CablewayParaDao;
import com.lsts.device.dao.CraneParaDao;
import com.lsts.device.dao.DeviceDao;
import com.lsts.device.dao.ElevatorParaDao;
import com.lsts.device.dao.EngineParaDao;
import com.lsts.device.dao.PressurevesselsParaDao;
import com.lsts.device.dao.RidesParaDao;
import com.lsts.inspection.bean.InspectionInfo;
import com.lsts.inspection.bean.InspectionInfoPay;
import com.lsts.inspection.bean.ReportBHGRecord;
import com.lsts.inspection.bean.ReportItemValue;
import com.lsts.inspection.bean.ReportPicValue;
import com.lsts.inspection.dao.InspectionInfoDao;
import com.lsts.inspection.dao.ReportBHGRecordDao;
import com.lsts.inspection.dao.ReportItemValueDao;
import com.lsts.report.bean.Report;
import com.lsts.report.dao.ReportDao;
import com.lsts.report.service.ReportService;

import util.TS_Util;

/**
 * 报告检验项目业务逻辑对象
 * 
 * @ClassName ReportItemValueService
 * @JDK 1.7
 * @author GaoYa
 * @date 2014-02-26 上午09:09:00
 */
@Service("reportItemValueService")
public class ReportItemValueService extends EntityManageImpl<ReportItemValue, ReportItemValueDao> {
	@Autowired
	private ReportService reportService;
	@Autowired
	private ReportItemValueDao reportItemValueDao;
	@Autowired
	private ReportBHGRecordDao reportBHGRecordDao;
	@Autowired
	private RtPageManager rtPageManager;
	@Autowired
	private RtPageDao rtPageDao;
	@Autowired
	private InspectionInfoDao infoDao;
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private ReportDao reportDao;
	@Autowired
	private InspectionService inspectionService;
	@Autowired
	private InspectionInfoPayService inspectionInfoPayService;
	@Autowired
	private BoilerParaDao boilerParaDao;
	@Autowired
	private PressurevesselsParaDao pressurevesselsParaDao;
	@Autowired
	private CraneParaDao craneParaDao;
	@Autowired
	private ElevatorParaDao elevatorParaDao;
	@Autowired
	private EngineParaDao engineParaDao;
	@Autowired
	private RidesParaDao ridesParaDao;
	@Autowired
	private AccessoryDao accDao;
	@Autowired
	private CablewayParaDao calDao;
	@Autowired
	private CodeTablesDao codeTablesDao;
	@Autowired
	private InspectionCommonService inspectionCommonService;
	
	@Autowired
	private ReportMarkDao reportMarkDao;
	

	// 获取报告检验项目
	public List<ReportItemValue> queryByInspectionInfoId(String inspectionInfoId, String report_id) {
		return reportItemValueDao.queryByInspectionInfoId(inspectionInfoId, report_id);
	}

	// 获取报告检验项目
	public List<ReportItemValue> queryByitemValue(String inspectionInfoId) {
		return reportItemValueDao.queryByitemValue(inspectionInfoId);
	}

	// 根据业务ID、报告类型ID、参数名称获取报告参数值
	public List<ReportItemValue> getItemByItemName(String info_id, String report_type, String item_name) {
		return reportItemValueDao.getItemByItemName(info_id, report_type, item_name);
	}

	// 根据业务ID、报告类型ID、参数名称集合获取报告参数值
	public List<ReportItemValue> getItemByItemNames(String info_id, String report_type, String item_names) {
		return reportItemValueDao.getItemByItemNames(info_id, report_type, item_names);
	}

	/**
	 * 更新报告检验项目参数值
	 * 
	 * @param id          -- ID
	 * @param info_id     -- 报告业务ID
	 * @param report_type -- 报告模板类型ID
	 * @param item_name   -- 检验项目name
	 * @param item_value  -- 报检验项目value
	 * @return
	 * @author GaoYa
	 * @date 2014-02-26 上午09:29:00
	 */
	public void updateItemValue(String item_id, String info_id, String report_type, String item_name,
			String item_value) {
		try {
			reportItemValueDao.createSQLQuery("update tzsb_report_item_value set fk_report_id='" + report_type
					+ "', fk_inspection_info_id='" + info_id + "', item_name='" + item_name + "',item_value='"
					+ item_value + "',item_type='String' where id='" + item_id + "'").executeUpdate();
		} catch (Exception e) {
			log.debug(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 更新报告检验项目参数值
	 * 
	 * @param info_id     -- 报告业务ID
	 * @param report_type -- 报告模板类型ID
	 * @param item_name   -- 检验项目name
	 * @param item_value  -- 报检验项目value
	 * @return
	 * @author GaoYa
	 * @date 2014-02-26 上午09:29:00
	 */
	public void updateItemValue(String info_id, String report_type, String item_name, String item_value) {
		try {
			reportItemValueDao.createSQLQuery("update  TZSB_REPORT_ITEM_VALUE set item_value='" + item_value
					+ "'where fk_report_id='" + report_type + "' and item_name='" + item_name
					+ "' and fk_inspection_info_id='" + info_id + "'").executeUpdate();
		} catch (Exception e) {
			log.debug(e.toString());
			e.printStackTrace();
		}
	}

	public Map<String, Object> getPic(String id) throws Exception {
		Map<String, Object> picMap = new HashMap<String, Object>();

		// 根据item_value内容获取相应图片 begin
		StringBuffer picSql = new StringBuffer();
		// picSql.append("from ReportPicValue p,ReportItemValue t ");
		// picSql.append("where p.id = t.item_value and p.business_id =
		// t.fk_inspection_info_id and p.business_id=? and upper(t.item_name)
		// like 'PICTURETEXT%'");
		picSql.append("from ReportPicValue p,ReportItemValue t ");
		picSql.append(" where p.id = t.item_value ");
		picSql.append(" and t.fk_inspection_info_id=? ");
		picSql.append(" and upper(t.item_name) like 'PICTURETEXT%'");
		List list = reportItemValueDao.createQuery(picSql.toString(), new String[] { id }).list();

		for (int i = 0; i < list.size(); i++) {
			Object o[] = (Object[]) list.get(i);
			ReportPicValue picv = (ReportPicValue) o[0];
			ReportItemValue ival = (ReportItemValue) o[1];
			ByteArrayOutputStream sout = new ByteArrayOutputStream();
			sout.write(picv.getPic_blob());
			// picMap.put(picv.getItem_name()+"P", sout.toByteArray());
			picMap.put(ival.getItem_name() + "P", picv.getPic_blob());
			picMap.put(ival.getItem_name() + "C", picv.getPic_clob());
			System.out.println("-------------------------:" + sout.toByteArray());
		}

		return picMap;
	}

	/**
	 * 获取报告检验项目
	 * 
	 * @param info_id    -- 检验业务信息ID
	 * @param report_id  -- 检验报告模板信息ID
	 * @param item_names -- 检验项目keys
	 * 
	 * @return
	 * @author GaoYa
	 * @date 2017-10-30 14:44:00
	 */
	public Map<String, Object> getGCContent(String info_id, String report_id, String item_names) throws Exception {
		return reportItemValueDao.getGCContent(info_id, report_id, item_names);
	}

	public boolean checkIfHg(String ids) {
		String[] idss = ids.split(",");
		for (int i = 0; i < idss.length; i++) {
			List<ReportItemValue> list = reportItemValueDao.queryByitemValue(idss[i]);
			for (int j = 0; j < list.size(); j++) {
				ReportItemValue value = list.get(j);
				// System.out.println("--------------------------"+value.getItem_value());
				if ("不合格".equals(value.getItem_value())) {
					List<ReportBHGRecord> list1 = reportBHGRecordDao.queryResourceByInfoId(idss[i]);
					if (list1 == null || list1.size() == 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 打开报告查询（新报表） author pingZhou
	 * 
	 * @param id
	 * @param input
	 * @param page
	 * @param code_ext
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> detailMap(String id, String input, String page, String code_ext) throws Exception {
		String sql = "from ReportItemValue where fk_inspection_info_id =?";
		if (StringUtil.isNotEmpty(code_ext) && !"null".equals(code_ext)) {
			sql = "from ReportItemValue where fk_inspection_info_id =? and item_name like '%" + code_ext + "%'";
		}
		List<ReportItemValue> list = this.reportItemValueDao.listQuery(sql, id);
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		Map<String, Object> dataMap = this.rtPageManager.loadFuncData(id, input, page);
		Map<String, Object> colorMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_COLOR);
		Map<String, Object> boldMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_BOLD);
		Map<String, Object> italicMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_ITALIC);
		Map<String, Object> sizeMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_SIZE);
		Map<String, Object> familyMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_FAMILY);
		Map<String, Object> imageMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_IMAGE);
		Map<String, Object> markMap = (Map<String, Object>) dataMap.get(RtExportDataType.EXPORT_DATA_MARK);

		/*
		 * -------------------------------------以下内容根据模板命名，所有模板统一命名 pingZhou
		 * --------------------------------------------
		 */
		InspectionInfo info = infoDao.get(id);
		if (info.getReport_sn() != null) {
			// 报告编号
			Map<String, String> mapi = new HashMap<String, String>();
			mapi.put("name", "base__report_sn");
			mapi.put("value", info.getReport_sn());
			returnList.add(mapi);
		}
		if (info.getReport_com_name() != null) {
			// 单位名称
			Map<String, String> mapi = new HashMap<String, String>();
			mapi.put("name", "base__com_name");
			mapi.put("value", info.getReport_com_name());
			returnList.add(mapi);
		}
		if (info.getReport_com_address() != null) {
			// 单位名称
			Map<String, String> mapi = new HashMap<String, String>();
			mapi.put("name", "base__com_address");
			mapi.put("value", info.getReport_com_address());
			returnList.add(mapi);
		}
		if (info.getCheck_op_id() != null) {
			// 检验人员
			Map<String, String> mapi = new HashMap<String, String>();
			mapi.put("name", "base__inspect_op");
			mapi.put("value", info.getCheck_op_id());
			returnList.add(mapi);
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (info.getAdvance_time() != null) {
			// 检验日期
			Map<String, String> mapi = new HashMap<String, String>();
			mapi.put("name", "base__inspect_date");
			mapi.put("value", df.format(info.getAdvance_time()));
			returnList.add(mapi);
		}
		if (info.getEnter_op_name() != null) {
			// 录入人员前台没有选择，直接取名字
			Map<String, String> mapi = new HashMap<String, String>();
			mapi.put("name", "base__enter_op");
			mapi.put("value", info.getEnter_op_name());
			returnList.add(mapi);
		}
		if (info.getEnter_time() != null) {
			// 录入时间
			Map<String, String> mapi = new HashMap<String, String>();
			mapi.put("name", "base__enter_date");
			mapi.put("value", df.format(info.getEnter_time()));
			returnList.add(mapi);
		}
		if (info.getExamine_id() != null) {
			// 审核人员
			Map<String, String> mapi = new HashMap<String, String>();
			mapi.put("name", "base__audit_op");
			mapi.put("value", info.getExamine_id());
			returnList.add(mapi);
		}
		if (info.getExamine_name() != null) {
			// 审核人员
			Map<String, String> mapi = new HashMap<String, String>();
			mapi.put("name", "base__audit_op_name");
			mapi.put("value", info.getExamine_name());
			returnList.add(mapi);
		}
		if (info.getExamine_Date() != null) {
			// 审核时间
			Map<String, String> mapi = new HashMap<String, String>();
			mapi.put("name", "base__audit_date");
			mapi.put("value", df.format(info.getExamine_Date()));
			returnList.add(mapi);
		}

		if (info.getIssue_name() != null) {
			// 签发人员前台没有选择，直接取名字
			Map<String, String> mapi = new HashMap<String, String>();
			mapi.put("name", "base__sign_op");
			mapi.put("value", info.getIssue_name());
			returnList.add(mapi);
		}
		
		if (info.getIssue_Date() != null) {
			// 签发时间
			Map<String, String> mapi = new HashMap<String, String>();
			mapi.put("name", "base__sign_date");
			mapi.put("value", df.format(info.getIssue_Date()));
			returnList.add(mapi);
		}
		
		/*
		 * -------------------------------------以上内容根据模板命名，所有模板统一命名---------------------
		 * -----------------------
		 */

		Set<String> set = new HashSet<>();
		
		for (ReportItemValue item : list) {
			// if (StringUtil.isNotEmpty(item.getItemValue())) {
			Map<String, String> map = new HashMap<String, String>();
			String name = item.getItem_name();
			if (!name.contains(RtPageType.TABLE) 
					&& !name.contains("record__") 
					&& !name.contains("conclusion__")
					&& !name.contains("fk")
					&& !name.contains("FK")) 
			{
				name = "base__" + name;
			}
			map.put("name", name);
			map.put("value", item.getItem_value());
			if (colorMap != null && colorMap.containsKey(item.getItem_name())
					&& colorMap.get(item.getItem_name()) != null) {
				map.put("color", colorMap.get(item.getItem_name()).toString());
			}
			if (boldMap != null && boldMap.containsKey(item.getItem_name())
					&& boldMap.get(item.getItem_value()) != null) {
				map.put("bold", boldMap.get(item.getItem_value()).toString());
			}
			if (italicMap != null && italicMap.containsKey(item.getItem_name())
					&& italicMap.get(item.getItem_name()) != null) {
				map.put("italic", italicMap.get(item.getItem_name()).toString());
			}
			if (sizeMap != null && sizeMap.containsKey(item.getItem_name())
					&& sizeMap.get(item.getItem_name()) != null) {
				map.put("size", sizeMap.get(item.getItem_name()).toString());
			}
			if (familyMap != null && familyMap.containsKey(item.getItem_name())
					&& familyMap.get(item.getItem_name()) != null) {
				map.put("family", familyMap.get(item.getItem_name()).toString());
			}
			if (imageMap != null && imageMap.containsKey(item.getItem_name())
					&& imageMap.get(item.getItem_name()) != null) {
				map.put("image", imageMap.get(item.getItem_name()).toString());
			}
			if (markMap != null && markMap.containsKey(item.getItem_name())
					&& markMap.get(item.getItem_name()) != null) {
				map.put("markContent", markMap.get(item.getItem_name()).toString());
				set.add(item.getItem_name());
			}
			/* System.out.println("------------"+imageMap+"--------------"); */
			returnList.add(map);
			// }
		}
		
		if(markMap!=null) {
			for (String key : markMap.keySet()) {
				//20190329 pingZhou
				//处理有标注，但是没得值的情况
				if(!set.contains(key)) {
					
					Map<String, String> map = new HashMap<String, String>();
					String name = key;
					if (!name.contains(RtPageType.TABLE) 
							&& !name.contains("record__") 
							&& !name.contains("conclusion__")
							&& !name.contains("fk")
							&& !name.contains("FK")) 
					{
						name = "base__" + name;
					}
					map.put("name", name);
					map.put("value", "");
					map.put("markContent", markMap.get(key).toString());
					returnList.add(map);
				}
				
			}
		}
		
		

		boolean flag = false;
		if (page != null && StringUtil.isNotEmpty(page)) {
			// 新报表工具才需要
			flag = infoDao.checkHasSave(info, page);
		}

		// 在录入状态，并且没有保存过数据则查基础数据
		if (input != null && !flag) {

			// 处理设备基础表信息
			Map<String, Object> baseMap = new HashMap<String, Object>();

			baseMap = getReportDeviceInfo(info.getFk_tsjc_device_document_id(), baseMap);

			Map<String, String> docMap = (Map<String, String>) baseMap.get("DEVICE_DOCUMENT_MAP");// 设备基础档案
			Map<String, String> boilerMap = (Map<String, String>) baseMap.get("DEVICE_BOILER_MAP");// 锅炉参数
			Map<String, String> elevatorMap = (Map<String, String>) baseMap.get("DEVICE_ELEVATOR_MAP");// 电梯参数
			Map<String, String> vesselMap = (Map<String, String>) baseMap.get("DEVICE_PRESSUREVESSEL_MAP");// 压力容器参数
			Map<String, String> craneMap = (Map<String, String>) baseMap.get("DEVICE_CRANE_MAP");// 起重参数
			Map<String, String> engineMap = (Map<String, String>) baseMap.get("DEVICE_ENGINE_MAP");// 厂车参数
			Map<String, String> ylssMap = (Map<String, String>) baseMap.get("DEVICE_YLSS_MAP");// 游乐设施参数
			Map<String, String> accMap = (Map<String, String>) baseMap.get("DEVICE_ACCESSORY_MAP");// 安全阀参数
			for (String key : docMap.keySet()) {
				if (docMap.get(key) != null) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", "base__" + key.toLowerCase());
					map.put("value", docMap.get(key));
					returnList.add(map);
				}
			}
			for (String key : boilerMap.keySet()) {
				if (boilerMap.get(key) != null) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", "base__" + key.toLowerCase());
					map.put("value", boilerMap.get(key));
					returnList.add(map);
				}
			}
			for (String key : elevatorMap.keySet()) {
				if (elevatorMap.get(key) != null) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", "base__" + key.toLowerCase());
					map.put("value", elevatorMap.get(key));
					returnList.add(map);
				}
			}
			for (String key : vesselMap.keySet()) {
				if (vesselMap.get(key) != null) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", "base__" + key.toLowerCase());
					map.put("value", vesselMap.get(key));
					returnList.add(map);
				}
			}
			for (String key : craneMap.keySet()) {
				if (craneMap.get(key) != null) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", "base__" + key.toLowerCase());
					map.put("value", craneMap.get(key));
					returnList.add(map);
				}
			}
			for (String key : engineMap.keySet()) {
				if (engineMap.get(key) != null) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", "base__" + key.toLowerCase());
					map.put("value", engineMap.get(key));
					returnList.add(map);
				}
			}
			for (String key : ylssMap.keySet()) {
				if (ylssMap.get(key) != null) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", "base__" + key.toLowerCase());
					map.put("value", ylssMap.get(key));
					returnList.add(map);
				}
			}
			for (String key : accMap.keySet()) {
				if (accMap.get(key) != null) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", "base__" + key.toLowerCase());
					map.put("value", accMap.get(key));
					returnList.add(map);
				}
			}
		}
		return returnList;
	}

	public HashMap<String, Object> getRecordModelDir(String infoId, String index) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		InspectionInfo info = infoDao.get(infoId);
		Report report = reportDao.get(info.getReport_type());
		/*String dirJson = inspectionInfoService.getDir(null,infoId,report.getRtboxCode());//rtDirManager.getDir(infoId, report.getRtboxCode());

		JSONArray dirs = JSONArray.fromString(dirJson);
		JSONObject root = dirs.getJSONObject(0);
		JSONArray childen = root.getJSONArray("children");
		int sum = childen.length();
		int now = 0;
		for (int i = 0; i < childen.length(); i++) {
			if (childen.getJSONObject(i).getString("pageName").equals(index)) {
				now = i + 1;
				break;
			}
		}
		map.put("sumPage", sum);
		map.put("nowPage", now);*/
		map.put("signPicture", getInfoSign(info));
		return map;
	}

	public Map<String, String> getInfoSign(InspectionInfo info) {

		Map<String, String> signMap = new HashMap<String, String>();

		// 处理单选的人员签名
		List<ReportItemValue> list = reportItemValueDao.queryOpByInspectionInfoId(info.getId());
		for (int i = 0; i < list.size(); i++) {
			String opId = list.get(i).getItem_value();
			if (opId != null && StringUtil.isNotEmpty(opId)) {
				String[] opIds = opId.split(",");
				if (opIds.length == 1) {
					opIds = opId.split(";");
					String signIds = null;
					Set<String> has = new HashSet<String>();
					for (int j = 0; j < opIds.length; j++) {
						String signId = reportItemValueDao.getOpSign(opIds[j]);

						if (StringUtil.isNotEmpty(signId)) {
							if (!has.contains(signId)) {
								if (signIds == null) {
									signIds = signId;
								} else {
									signIds = signIds + "," + signId;
								}
								has.add(signId);
							}
						}
					}
					System.out.println("base__" + list.get(i).getItem_name() + "-------------" + signIds);
					signMap.put("base__" + list.get(i).getItem_name() + "_signp", signIds);
				}
			}
		}

		if (info.getEnter_op_id() != null) {

			signMap.put("base__enter_op_signp", reportItemValueDao.getOpSign(info.getEnter_op_id()));

		}

		if (info.getEnter_op_id() != null) {

			signMap.put("base__confirm_op_signp", reportItemValueDao.getOpSign(info.getEnter_op_id()));

		}

		if (info.getExamine_id() != null) {

			signMap.put("base__audit_op_signp", reportItemValueDao.getOpSign(info.getExamine_id()));

		}

		if (info.getCheck_op_id() != null) {
			String[] checkOps = info.getCheck_op_id().split(",");
			String signIds = null;
			Set<String> has = new HashSet<String>();
			for (int i = 0; i < checkOps.length; i++) {
				String signId = reportItemValueDao.getOpSign(checkOps[i]);
				if (!"".equals(signId)) {
					if (!has.contains(signId)) {
						if (signIds == null) {
							signIds = signId;
						} else {
							signIds = signIds + "," + signId;
						}
						has.add(signId);
					}
				}

				signMap.put("base__inspect_op_signp", signIds);

			}

		}

		if (info.getIssue_id() != null) {
			signMap.put("base__sign_op_signp", reportItemValueDao.getOpSign(info.getIssue_id()));

		}

		return signMap;

	}

	/**
	 * 加载报告关联的特种设备信息
	 * 
	 * @return Map
	 * @author Liaozw
	 * @throws Exception
	 */
	public Map<String, Object> getReportDeviceInfo(String fk_device_id, Map<String, Object> map) throws Exception {

		Map<String, String> docMap = new HashMap<String, String>();// 设备档案
		Map<String, String> boilerMap = new HashMap<String, String>();// 锅炉参数
		Map<String, String> elevatorMap = new HashMap<String, String>();// 电梯参数
		Map<String, String> vesselMap = new HashMap<String, String>();// 容器参数
		Map<String, String> craneMap = new HashMap<String, String>();// 起重机械参数
		Map<String, String> engineMap = new HashMap<String, String>();// 厂车参数
		Map<String, String> ylssMap = new HashMap<String, String>();// 游乐设施参数
		Map<String, String> accMap = new HashMap<String, String>();// 安全阀参数
		// Map<String,String> pipelineMap = new HashMap<String,String>();//管道参数
		// Map<String,String> repMap = new HashMap<String,String>();//报告文件信息
		// Map<String,Object> supMap = new HashMap<String,Object>();//产品监检

		DeviceDocument baseDeviceDocument = deviceDao.get(fk_device_id);
		for (Map.Entry<String, Object> entry : inspectionService.beanToMap(baseDeviceDocument).entrySet()) {
			if (entry.getValue() instanceof String) {
				docMap.put(entry.getKey(), (String) entry.getValue());
			}
		}

		for (BoilerPara boiler : baseDeviceDocument.boiler) {
			for (Map.Entry<String, Object> entry : inspectionService.beanToMap(boiler).entrySet()) {
				if (entry.getValue() instanceof String) {
					boilerMap.put(entry.getKey(), (String) entry.getValue());
				}
			}
		}
		for (ElevatorPara elevator : baseDeviceDocument.elevatorParas) {
			for (Map.Entry<String, Object> entry : inspectionService.beanToMap(elevator).entrySet()) {
				if (entry.getValue() instanceof String) {
					elevatorMap.put(entry.getKey(), (String) entry.getValue());
				}
			}
		}
		for (PressurevesselsPara vessel : baseDeviceDocument.pressurevessels) {
			for (Map.Entry<String, Object> entry : inspectionService.beanToMap(vessel).entrySet()) {
				if (entry.getValue() instanceof String) {
					vesselMap.put(entry.getKey(), (String) entry.getValue());
				}
			}
		}
		for (CranePara cranes : baseDeviceDocument.crane) {
			for (Map.Entry<String, Object> entry : inspectionService.beanToMap(cranes).entrySet()) {
				if (entry.getValue() instanceof String) {
					craneMap.put(entry.getKey(), (String) entry.getValue());
				}
			}
		}
		for (EnginePara engine : baseDeviceDocument.engine) {
			for (Map.Entry<String, Object> entry : inspectionService.beanToMap(engine).entrySet()) {
				if (entry.getValue() instanceof String) {
					engineMap.put(entry.getKey(), (String) entry.getValue());
				}
			}
		}
		for (RidesPara ylss : baseDeviceDocument.ridesPara) {
			for (Map.Entry<String, Object> entry : inspectionService.beanToMap(ylss).entrySet()) {
				if (entry.getValue() instanceof String) {
					ylssMap.put(entry.getKey(), (String) entry.getValue());
				}
			}
		}
		for (Accessory acc : baseDeviceDocument.accessory) {
			for (Map.Entry<String, Object> entry : inspectionService.beanToMap(acc).entrySet()) {
				if (entry.getValue() instanceof String) {
					accMap.put(entry.getKey(), (String) entry.getValue());
				}
			}
		}
		/*
		 * for(PressurePipeline pipe : baseDeviceDocument.pressurepipelines) {
		 * pipelineMap = pipe.bean_to_Map(); }
		 */

		map.put("DEVICE_DOCUMENT_MAP", docMap);// 设备基础档案
		map.put("DEVICE_BOILER_MAP", boilerMap);// 锅炉参数
		map.put("DEVICE_ELEVATOR_MAP", elevatorMap);// 电梯参数
		map.put("DEVICE_PRESSUREVESSEL_MAP", vesselMap);// 压力容器参数
		map.put("DEVICE_CRANE_MAP", craneMap);// 起重参数
		map.put("DEVICE_ENGINE_MAP", engineMap);// 厂车参数
		map.put("DEVICE_YLSS_MAP", ylssMap);// 游乐设施参数
		map.put("DEVICE_ACCESSORY_MAP", accMap);// 安全阀参数
		// map.put("DEVICE_PIPELINE_MAP", pipelineMap);//压力管道参数
		return map;
	}

	/**
	 * 保存报告信息（新报表）
	 * 
	 * @param map
	 * @param page
	 * @param pageName
	 * @throws Exception
	 */
	public void saveMap(Map<String, RtExportData> map, String page, String pageName) throws Exception {

		System.out.println("--------------------保存开始！---------------------");

		String fk_report_id = map.get("fk_report_id").getValue();
		String fk_inspection_info_id = map.get("fk_inspection_info_id").getValue();

		String code_ext = map.get("fkCodeExt") == null ? "" : map.get("fkCodeExt").getValue();
		if (StringUtil.isEmpty(code_ext)) {
			code_ext = "";
		}
		// 根据业务信息ID获取业务信息bean
		InspectionInfo info = infoDao.get(fk_inspection_info_id);
		String fK_device_id = info.getFk_tsjc_device_document_id();

		if (StringUtil.isNotEmpty(fk_report_id)) {

			CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
			User user = (User) curUser.getSysUser();
			Employee emp = (com.khnt.rbac.impl.bean.Employee) user.getEmployee();
			// Org org = emp.getOrg();

			// 根据设备ID获取设备基础信息bean
			DeviceDocument devicedoc = deviceDao.get(fK_device_id);
			// 获取检验类型
			String check_type = info.getInspection().getCheck_type();
			info.setCheck_category_code(check_type);
			info.setCheck_category_name(codeTablesDao.getValueByCode("BASE_CHECK_TYPE", check_type));
			// 获取设备类型
			String device_type = devicedoc.getDevice_sort_code();
			// 为每个参数表设置一个字段名的集合
			Set<String> _boilerMap = new HashSet<String>();
			Set<String> _elevatorMap = new HashSet<String>();
			Set<String> _vesselMap = new HashSet<String>();
			Set<String> _craneMap = new HashSet<String>();
			Set<String> _engineMap = new HashSet<String>();
			Set<String> _rideMap = new HashSet<String>();
			Set<String> _accMap = new HashSet<String>();
			Set<String> _docMap = new HashSet<String>();
			Set<String> _calMap = new HashSet<String>();

			// 将bean字段放入相应集合
			_boilerMap = inspectionService.beanFieldSet(BoilerPara.class);
			_elevatorMap = inspectionService.beanFieldSet(ElevatorPara.class);
			_vesselMap = inspectionService.beanFieldSet(PressurevesselsPara.class);
			_craneMap = inspectionService.beanFieldSet(CranePara.class);
			_engineMap = inspectionService.beanFieldSet(EnginePara.class);
			_accMap = inspectionService.beanFieldSet(Accessory.class);
			_rideMap = inspectionService.beanFieldSet(RidesPara.class);
			// _docMap = beanFieldSet(DeviceDocument.class);
			_docMap = DeviceDocument.bean_to_set();
			_calMap = inspectionService.beanFieldSet(CablewayPara.class);
			// 定义设备类型标志位
			boolean b_boiler = false;
			boolean b_vessel = false;
			boolean b_elevator = false;
			boolean b_crane = false;
			boolean b_engine = false;
			boolean b_acc = false;
			boolean b_ride = false;
			boolean b_doc = false;
			boolean b_cal = false;
			// 定义设备单数bean
			BoilerPara p_boiler = null;
			PressurevesselsPara p_vessel = null;
			ElevatorPara p_elevator = null;
			CranePara p_crane = null;
			EnginePara p_engine = null;
			RidesPara p_ride = null;
			Accessory p_acc = null;
			CablewayPara p_cal = null;
			// 根据设备类型获得相应的参数表bean
			if (devicedoc.getDevice_sort_code().startsWith("1")) {
				Collection<BoilerPara> c = devicedoc.getBoiler();
				for (BoilerPara it : c) {
					p_boiler = boilerParaDao.get(it.getId());
				}
				b_boiler = true;
			}
			// 根据设备类型获得相应的参数表bean
			if (devicedoc.getDevice_sort_code().startsWith("2")) {
				Collection<PressurevesselsPara> c = devicedoc.getPressurevessels();
				for (PressurevesselsPara it : c) {
					p_vessel = pressurevesselsParaDao.get(it.getId());
				}
				b_vessel = true;
			}
			// 根据设备类型获得相应的参数表bean
			if (devicedoc.getDevice_sort_code().startsWith("3")) {
				Collection<ElevatorPara> c = devicedoc.getElevatorParas();
				for (ElevatorPara it : c) {
					p_elevator = elevatorParaDao.get(it.getId());
				}
				b_elevator = true;
			}
			// 根据设备类型获得相应的参数表bean
			if (devicedoc.getDevice_sort_code().startsWith("4")) {
				Collection<CranePara> c = devicedoc.getCrane();
				for (CranePara it : c) {
					p_crane = craneParaDao.get(it.getId());
				}
				b_crane = true;
			}
			// 根据设备类型获得相应的参数表bean
			if (devicedoc.getDevice_sort_code().startsWith("5")) {
				Collection<EnginePara> c = devicedoc.getEngine();
				for (EnginePara it : c) {
					p_engine = engineParaDao.get(it.getId());
				}
				b_engine = true;
			}
			// 根据设备类型获得相应的参数表bean
			if (devicedoc.getDevice_sort_code().startsWith("6")) {
				Collection<RidesPara> c = devicedoc.getRidesPara();
				for (RidesPara it : c) {
					p_ride = ridesParaDao.get(it.getId());
				}
				b_ride = true;
			}
			// 根据设备类型获得相应的参数表bean
			if (devicedoc.getDevice_sort_code().startsWith("9")) {
				Collection<CablewayPara> c = devicedoc.getCableway();
				for (CablewayPara it : c) {
					p_cal = calDao.get(it.getId());
				}
				b_cal = true;
			}
			// 根据设备类型获得相应的参数表bean
			if (devicedoc.getDevice_sort_code().startsWith("Q")||devicedoc.getDevice_sort_code().startsWith("F")) {
				Collection<Accessory> c = devicedoc.getAccessory();
				for (Accessory it : c) {
					p_acc = accDao.get(it.getId());
				}
				b_acc = true;
			}
			// 根据设备类型获得相应的参数表bean
			if (devicedoc.getDevice_sort_code().startsWith("731")) {
				Collection<Accessory> c = devicedoc.getAccessory();
				if (c.size() == 0) {
					p_acc = new Accessory();
					p_acc.setDeviceDocument(devicedoc);
					accDao.save(p_acc);
				} else {
					for (Accessory it : c) {
						p_acc = accDao.get(it.getId());
					}
				}
				b_acc = true;
			}
			/*
			 * --------------------------------------以下循环保存报告当页数据---------------------------
			 * --------------------------
			 */

			RtSaveAsst asst = new RtSaveAsst();
			// System.out.println("----------------------"+page+"-------------------"+map.keySet().toString()+"-------------------------");
			// 取出所有需要保存内容
			String KeyNames = map.keySet().toString();
			String names = "";
			if (StringUtil.isEmpty(code_ext) || !code_ext.contains("_")) {
				names = KeyNames.substring(1, KeyNames.length() - 1).replace(",", "','").replace(" ", "");
			} else {
				names = KeyNames.substring(1, KeyNames.length() - 1).replace(",", RtField.separator + code_ext + "','")
						.replace(" ", "");
			}

			this.reportItemValueDao
					.createSQLQuery("delete from TZSB_REPORT_ITEM_VALUE where fk_report_id =? and item_name in ('"
							+ names + "')" + " and fk_inspection_info_id=?", fk_report_id, fk_inspection_info_id)
					.executeUpdate();
			this.rtPageManager.delFuncDataBacth(fk_inspection_info_id, names, page);

			String check_date_y = null;
			String check_date_m = null;
			String check_date_d = null;
			String last_check_y = null;
			String last_check_m = null;
			String last_check_d = null;

			String device_model = ""; // 设备型式
			int dtcs = 0; // 电梯层数
			double sdxc = 0; // 索道斜长（带小数）
			int ppc_zcsl = 0; // 碰碰车座舱数量
			int xhc_czrs = 0; // 小火车乘座人数
			int sc_clzs = 0; // 赛车车辆总数
			double qz_q = 0; // 起重量
			double qz_lj = 0; // 起重力矩
			double qzkd = 0; // 起重机械跨度

			String enter_time = ""; // 录入时间
			String inspection_date = ""; // 检验日期
			String year = "";
			String month = "";

			String inspection_conclusion = ""; // 检验结论
			String device_qr_code = ""; // 设备二维码编号
			String check_unit_id = info.getCheck_unit_id(); // 检验部门
			// 开始处理报表内容数据
			for (String itemName : map.keySet()) {
				// if (itemName.contains(RtPageType.TABLE)) {
				RtExportData rtExportData = map.get(itemName);
				// 保存字段所在页码
				rtExportData.setPageNo(page);
				String name = RtField.getName(itemName, code_ext);
				rtExportData.setName(name);
				/*
				 * 注释的原因是在前面进行了一次批量删除，这里不用操作了 int n = this.reportItemValueDao
				 * .createSQLQuery("delete from TZSB_REPORT_ITEM_VALUE where fk_report_id =? and item_name=? and fk_inspection_info_id=?"
				 * , fk_report_id, name,fk_inspection_info_id) .executeUpdate();
				 * 
				 * if(n>0){ //如果影响行数大于0说明以前保存过，此判断用于优化保存方法 pingZhou 20171201
				 * this.rtPageManager.delFuncData(fk_inspection_info_id, itemName); }
				 */
				String markContent = rtExportData.getMarkContent();
				boolean flagm = !StringUtil.isEmpty(markContent) && !"null".equals(markContent) && markContent != null;
				if ((rtExportData.getValue() != null && StringUtil.isNotEmpty(rtExportData.getValue())
						&& !"null".equals(rtExportData.getValue())) || flagm) {
					ReportItemValue item = new ReportItemValue();
					item.setItem_name(rtExportData.getName());
					item.setItem_value(rtExportData.getValue());
					item.setPage_no(page);
					item.setFk_report_id(fk_report_id);
					item.setFk_inspection_info_id(fk_inspection_info_id);
					
					String fname = rtExportData.getName().toUpperCase();
					String value = rtExportData.getValue();
					// 将报检单位写入检验业务信息表
					if (fname.equals("COM_NAME")) {
						info.setReport_com_name(value);
					}
					// 将报检单位地址写入业务信息表和设备主表
					if (fname.equals("COM_ADDRESS")) {
						info.setReport_com_address(value);
						devicedoc.setUse_site_address(value);
					}
					// 将检验联系人、联系人电话写入业务信息表
					if (fname.equals("CHECK_OP")) {
						info.setCheck_op(value);
					}
					// 返写设备安全管理员
					if (fname.equals("SECURITY_OP")) {
						if (StringUtil.isNotEmpty(value)) {
							devicedoc.setSecurity_op(value);
							info.setSecurity_op(value);
						}
					}
					// 返写设备安全管理员联系电话
					if (fname.equals("SECURITY_TEL")) {
						if (StringUtil.isNotEmpty(value)) {
							devicedoc.setSecurity_tel(value);
							info.setSecurity_tel(value);
						}
					}
					// 2018-03-13应张展彬要求增加
					// 返写设备注册代码到info业务表，以供报告查询
					if (fname.equals("DEVICE_REGISTRATION_CODE")) {
						if (StringUtil.isNotEmpty(value)) {
							info.setDevice_registration_code(value);
						}
					}
					// 返写设备代码
					if (fname.equals("DEVICE_CODE")) {
						if (StringUtil.isNotEmpty(value)) {
							devicedoc.setDevice_code(value);
						}
					}
					// 返写联系电话
					if (fname.equals("CHECK_TEL")) {
						info.setCheck_tel(value);
					}
					// 将维保单位返写报告业务信息表
					if (fname.equals("MAINTAIN_UNIT_NAME")) {
						info.setMaintain_unit_name(value);
					}
					// 将制造单位返写报告业务信息表
					if (fname.equals("MAKE_UNITS_NAME")) {
						info.setMake_units_name(value);
					}
					// 将安装/施工单位返写报告业务信息表
					if (fname.equals("CONSTRUCTION_UNITS_NAME")) {
						info.setConstruction_units_name(value);
					}

					// 将维保单位联系人、电话写入设备信息表
					if (fname.equals("MAINTENANCE_MAN")) {
						devicedoc.setMaintenance_man(value);
					}
					if (fname.equals("MAINTENANCE_TEL")) {
						devicedoc.setMaintenance_tel(value);
					}

					// 返写施工类别
					if (fname.equals("SGLB")) { // SGLB：施工类别（监检）
						if (device_type.startsWith("4")) { // 4：起重机
							info.setCheck_type_code(value);
							if ("1".equals(value)) {
								info.setCheck_type_name("新装");
							} else if ("2".equals(value)) {
								info.setCheck_type_name("移装");
							} else if ("3".equals(value)) {
								info.setCheck_type_name("改造");
							} else if ("4".equals(value)) {
								info.setCheck_type_name("重大修理");
							}
						} else if (device_type.startsWith("3")) {// 3：电梯（安装、改造、修理）
							info.setCheck_type_code(value);
							info.setCheck_type_name(value);
						}
					}
					// 返写检验类别2
					if (fname.equals("CHECK_TYPE")) {// CHECK_TYPE：检验类别（定期、首次）
						if (device_type.startsWith("4")) { // 4：起重机
							info.setCheck_type_code(value);
							info.setCheck_type_name(value);
						}
					}

					// 检验结论写入到主表中和业务信息表
					if (fname.equals("INSPECTION_CONCLUSION")) {
						devicedoc.setInspect_conclusion(value);
						info.setInspection_conclusion(value);

						// conlustion
						// = value;
						// 如果下次检验日期为“/则往设备信息主表和检验记录表里保存空
						// if(conlustion.equals("不合格")){
						// info
						// .setLast_check_time("");
						// }
						//
						inspection_conclusion = value;// 获取检验结论
					}
					// 将电梯二维码编号写入业务信息表，签发后写入设备信息表
					if (fname.equals("DEVICE_QR_CODE")) {
						if (value != null) {
							info.setDevice_qr_code(value);
						} else {
							info.setDevice_qr_code("");
						}
					}
					// 将电梯使用登记证号写入设备信息表
					if (fname.equals("REGISTRATION_NUM")) {
						if (value != null) {
							devicedoc.setRegistration_num(value);
							info.setRegistration_num(value);
						} else {
							devicedoc.setRegistration_num("");
							info.setRegistration_num("");
						}
					}
					// 常压罐车检验结论特殊处理
					if ("2610".equals(device_type)) {
						if (fname.equals("JYJL_SELECT1")) {
							if (StringUtil.isNotEmpty(value) && "√".equals(value)) {
								devicedoc.setInspect_conclusion("允许使用");
								info.setInspection_conclusion("允许使用");
							}
						}
						if (fname.equals("JYJL_SELECT3")) {
							if (StringUtil.isNotEmpty(value) && "√".equals(value)) {
								devicedoc.setInspect_conclusion("停止使用");
								info.setInspection_conclusion("停止使用");
							}
						}
					}

					// 汽车罐车、压力容器检验结论（非合格、不合格）特殊处理
					if (devicedoc.getDevice_sort().startsWith("2") || devicedoc.getDevice_sort().startsWith("1")) {
						if (fname.equals("JYJL_SELECT1")) {
							if (StringUtil.isNotEmpty(value) && "√".equals(value)) {
								devicedoc.setInspect_conclusion("符合要求");
								info.setInspection_conclusion("符合要求");
							}
						}
						if (fname.equals("JYJL_SELECT2")) {
							if (StringUtil.isNotEmpty(value) && "√".equals(value)) {
								devicedoc.setInspect_conclusion("基本符合要求");
								info.setInspection_conclusion("基本符合要求");
							}
						}
						if (fname.equals("JYJL_SELECT3")) {
							if (StringUtil.isNotEmpty(value) && "√".equals(value)) {
								devicedoc.setInspect_conclusion("不符合要求");
								info.setInspection_conclusion("不符合要求");
							}
						}
					}

					// 超高压压力容器检验结论（非合格、不合格）特殊处理
					if ("2110".equals(device_type)) {
						if (fname.equals("JYJL_SELECT1")) {
							if (StringUtil.isNotEmpty(value) && "√".equals(value)) {
								devicedoc.setInspect_conclusion("继续使用");
								info.setInspection_conclusion("继续使用");
							}
						}
						if (fname.equals("JYJL_SELECT3")) {
							if (StringUtil.isNotEmpty(value) && "√".equals(value)) {
								devicedoc.setInspect_conclusion("判废");
								info.setInspection_conclusion("判废");
							}
						}
					}
					// 下次检验时间写入业务信息表
					// （主表在领导确认报告合格之后某一个环节写入）
					if (fname.equals("LAST_INSPECTION_DATE")) {
						String last_check_time = value;
						// 如果下次检验日期为“/则往设备信息主表和检验记录表里保存空
						if (last_check_time.equals("/") || last_check_time.equals("")
								|| last_check_time.equals("待受检单位申请后确定") || inspection_conclusion.equals("不合格")) {
							last_check_time = "";
						}
						if (!"".equals(last_check_time)) {
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

							Date date = format.parse((last_check_time.replace("/", "-")).toString());
							info.setLast_check_time(date);

						}
					}

					// 起重机下次检验日期特殊处理（因业务需要，起重机下次检验日期报告中只精确到年月）
					if (fname.equals("LAST_INSPECTION_DATE_Y")) {
						if (StringUtil.isNotEmpty(value)) {
							year = value;
						}
					}
					if (fname.equals("LAST_INSPECTION_DATE_M")) {
						if (StringUtil.isNotEmpty(value)) {
							month = value;
						}
					}
					if (device_type.startsWith("4")) {
						// 起重取参数

						// 起重量
						if (fname.equals("P40002004")) {
							try {
								qz_q = new Double(value);
							} catch (Exception e) {
								qz_q = 0;
							}

						}
						// 起重力矩
						if (fname.equals("P40002005")) {
							try {
								qz_lj = new Double(value);
							} catch (Exception e) {
								qz_lj = 0;
							}

						}

						// 起重跨度
						/*
						 * if (PostBackValue[i].toUpperCase().equals("P_KD")) { try { qzkd = new
						 * Double(engine.getParameter(PostBackValue[i])); } catch (Exception e) { qzkd =
						 * 0; } }
						 */
					}

					if (StringUtil.isNotEmpty(year) && StringUtil.isNotEmpty(month)) {
						if ("/".equals(year.trim()) && "/".equals(month.trim())) {
							info.setLast_check_time(null);
							devicedoc.setInspect_next_date(null);
						} else {
							String last_day = TS_Util.getLastDayOfMonth(Integer.parseInt(year.trim()),
									Integer.parseInt(month.trim()));
							String str = year.trim() + "-" + month.trim()
									+ (StringUtil.isNotEmpty(last_day) ? "-" + last_day : "-28");
							info.setLast_check_time(DateUtil.convertStringToDate("yyyy-MM-dd", str));
							devicedoc.setInspect_next_date(DateUtil.convertStringToDate("yyyy-MM-dd", str));
						}
					}

					// 客运索道的下次检验日期
					if (fname.equals("KY_LAST_INSPECTION_DATE")) {
						String last_check_time = value;
						// 如果下次检验日期为“/则往设备信息主表和检验记录表里保存空
						if (last_check_time.equals("/") || last_check_time.equals("")
								|| last_check_time.equals("待受检单位申请后确定")) {
							last_check_time = "";
						}
						if (!"".equals(last_check_time)) {
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							String[] date_arr = last_check_time.split("-");
							if (date_arr.length == 2) {
								last_check_time += "-01"; // 客运索道报告，下次检验日期只显示年月，但返现检验业务信息时，需要精确到年月日（默认为月初1号）
							}
							Date date = format.parse((last_check_time.replace("/", "-")).toString());
							info.setLast_check_time(date);

						}
					}

					// 编制日期写入业务信息表
					// （主表在领导确认报告合格之后某一个环节写入）
					if (fname.equals("DRAFT_DATE")) {
						enter_time = value;
						// 如果编制日期为“/则往设备信息主表和检验记录表里保存空
						if (enter_time.equals("/") || enter_time.equals("") || enter_time.equals("待受检单位申请后确定")) {
							enter_time = "";
						}
						if (!"".equals(enter_time)) {
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

							Date date = format.parse((enter_time.replace("/", "-")).toString());
							info.setEnter_time(date);

						}
					}

					// 如果检验时间不为空，则回写如业务信息表
					if (fname.equals("INSPECTION_DATE")) {
						if (!value.equals("")) {

							String check_date = value;
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							inspection_date = check_date;
							info.setAdvance_time(format.parse(check_date));

						}
					}

					// 反写设备参数信息
					try {
						if (b_boiler && _boilerMap.contains(fname)) {
							String backValue = value.replaceAll("'", "’");
							;
							// 判断是否为/ 如果是就变为空
							if (backValue.equals("/")) {
								backValue = "";
							}
							inspectionService.SetParaValue(p_boiler, fname.toLowerCase(), backValue);
						}

						if (b_vessel && _vesselMap.contains(fname)) {
							String backValue = value.replaceAll("'", "’");
							;
							// 判断是否为/ 如果是就变为空
							if (backValue.equals("/")) {
								backValue = "";
							}
							inspectionService.SetParaValue(p_vessel, fname.toLowerCase(), backValue);
						}

						if (b_elevator && _elevatorMap.contains(fname)) {
							String backValue = value.replaceAll("'", "’");

							// 判断是否为/ 如果是就变为空
							if (backValue.equals("/")) {
								backValue = "";
							}

							inspectionService.SetParaValue(p_elevator, fname.toLowerCase(), backValue);
						}

						if (b_engine && _engineMap.contains(fname)) {
							String backValue = value.replaceAll("'", "’");
							;
							// 判断是否为/ 如果是就变为空
							if (backValue.equals("/")) {
								backValue = "";
							}
							inspectionService.SetParaValue(p_engine, fname.toLowerCase(), backValue);
						}

						if (b_crane && _craneMap.contains(fname)) {
							String backValue = value.replaceAll("'", "’");
							;
							// 判断是否为/ 如果是就变为空
							if (backValue.equals("/")) {
								backValue = "";
							}
							inspectionService.SetParaValue(p_crane, fname.toLowerCase(), backValue);
						}

						if (b_ride && _rideMap.contains(fname)) {
							String backValue = value.replaceAll("'", "’");
							;

							// 判断是否为/ 如果是就变为空
							if (backValue.equals("/")) {
								backValue = "";
							}
							inspectionService.SetParaValue(p_ride, fname.toLowerCase(), backValue);

						}
						if (b_acc && _accMap.contains(fname)) {
							String backValue = value.replaceAll("'", "’");
							;

							// 判断是否为/ 如果是就变为空
							if (backValue.equals("/")) {
								backValue = "";
							}
							inspectionService.SetParaValue(p_acc, fname.toLowerCase(), backValue);
						}

						if (_docMap.contains(fname)) {
							String backValue = value.replaceAll("'", "’");
							;

							// 判断是否为/ 如果是就变为空
							// if(backValue.equals("/")){
							// backValue = "";
							// }

							inspectionService.SetParaValue(devicedoc, fname.toLowerCase(), backValue);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					// begin
					if (rtExportData.getName().toUpperCase().equals("INSPECT_DATE_Y")) {
						if (!rtExportData.getValue().equals("")) {
							check_date_y = rtExportData.getValue();
						}
					}
					if (rtExportData.getName().toUpperCase().equals("INSPECT_DATE_M")) {
						if (!rtExportData.getValue().equals("")) {
							check_date_m = rtExportData.getValue();
						}
					}
					if (rtExportData.getName().toUpperCase().equals("INSPECT_DATE_D")) {
						if (!rtExportData.getValue().equals("")) {
							check_date_d = rtExportData.getValue();
						}
					}
					if (rtExportData.getName().toUpperCase().equals("INSPECT_NEXT_DATE_Y")) {
						if (!rtExportData.getValue().equals("")) {
							last_check_y = rtExportData.getValue();
						}
					}
					if (rtExportData.getName().toUpperCase().equals("INSPECT_NEXT_DATE_M")) {
						if (!rtExportData.getValue().equals("")) {
							last_check_m = rtExportData.getValue();
						}
					}
					if (rtExportData.getName().toUpperCase().equals("INSPECT_NEXT_DATE_D")) {
						if (!rtExportData.getValue().equals("")) {
							last_check_d = rtExportData.getValue();
						}
					}

					this.save(item);
				}

				// 判断是否着色
				asst.setFkBusinessId(fk_inspection_info_id);
				asst.setRtPageDao(this.rtPageDao);
				// asst.setrtReportItemValueDao(rtReportItemValueDao);
				// asst.setRtCode(rtCode);
				asst.execute(rtExportData, null, null, null);

				/*
				 * if(rtExportData.getName()!=null&&rtExportData.getValue()!=null&&StringUtil.
				 * isNotEmpty(rtExportData.getValue())){
				 * if(("inspection_op"+page).equals(rtExportData.getName().toLowerCase())){
				 * //单独检验员
				 * 
				 * saveItemOp(page,pageName, "inspector",rtExportData.getValue(),info);
				 * 
				 * }else if(("audit_op"+page).equals(rtExportData.getName().toLowerCase())){
				 * //单独审核人
				 * 
				 * saveItemOp(page,pageName, "auditor",rtExportData.getValue(),info);
				 * 
				 * }else if(("eval_op"+page).equals(rtExportData.getName().toLowerCase())){
				 * //单独评片人
				 * 
				 * saveItemOp(page,pageName, "evalOp",rtExportData.getValue(),info);
				 * 
				 * } }
				 */

			}

			if (b_boiler) {
				if (p_boiler != null) {
					boilerParaDao.save(p_boiler);
				}
			}
			if (b_vessel) {
				if (p_vessel != null) {
					pressurevesselsParaDao.save(p_vessel);
				}
			}

			if (b_elevator) {
				if (p_elevator != null) {
					elevatorParaDao.save(p_elevator);
				}
			}
			if (b_crane) {
				if (p_crane != null) {
					craneParaDao.save(p_crane);
				}
			}
			if (b_engine) {
				if (p_engine != null) {
					engineParaDao.save(p_engine);
				}
			}
			if (b_ride) {
				if (p_ride != null) {
					ridesParaDao.save(p_ride);
				}
			}
			if (info.getSave_page_item() == null || StringUtil.isEmpty(info.getSave_page_item())) {

				info.setSave_page_item(page);

			} else {

				boolean flag = infoDao.checkHasSave(info, page);
				if (!flag) {
					info.setSave_page_item(info.getSave_page_item() + "," + page);
				}

			}

			if (info.getIs_report_input() == null || !"2".equals(info.getIs_report_input())) {
				// 修改检验业务信息
				// 报告编制人信息
				info.setEnter_op_id(user.getEmployee().getId());
				info.setEnter_op_name(user.getEmployee().getName());
				info.setEnter_time(new Date());

			}

			info.setIs_report_input("2"); // 报告是否录入 2是已经录入
			info.setIs_copy("0"); // 不是复制报告
			// 单独处理机电类下次检验日期
			if (last_check_y != null && last_check_m != null) {

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date = format.parse(last_check_y + "-" + last_check_m + "-01");
					info.setLast_check_time(date);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 单独处理机电类检验日期
			if (check_date_y != null && check_date_m != null && check_date_d != null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date = format.parse(check_date_y + "-" + check_date_m + "-" + check_date_d);
					info.setAdvance_time(date);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 生成报告书编号
			String report_sn = "";
			if (StringUtil.isEmpty(info.getReport_sn())) {
				String report_type = info.getReport_type();
				// 不存在报告编号，就生成报告书编号
				synchronized (this) {
					Map<String, Object> reportSnMap = inspectionCommonService.createReportSn("saveReport",
							fk_inspection_info_id, report_type, check_type, device_type);
					report_sn = String.valueOf(reportSnMap.get("report_sn"));
					info.setReport_sn(report_sn);
				}
			} else {
				report_sn = info.getReport_sn();
			}
			// 写入ReportItemValue里面
			// 判断REPORT_SN是否存在，存在就更新，不存在就新增
			String sql = " from ReportItemValue where fk_inspection_info_id=?"
					+ " and fk_report_id=? and upper(item_name) = 'REPORT_SN' ";
			List<ReportItemValue> lists = reportItemValueDao
					.createQuery(sql, new String[] { info.getId(), info.getReport_type() }).list();
			if (lists.isEmpty()) {
				ReportItemValue item = new ReportItemValue();
				item.setItem_name("report_sn");
				item.setItem_value(report_sn);
				item.setFk_report_id(fk_report_id);
				item.setFk_inspection_info_id(fk_inspection_info_id);
				this.save(item);
			} else {
				String updateSql = "update ReportItemValue set item_value=? where fk_inspection_info_id=? and fk_report_id=? and upper(item_name) = 'REPORT_SN' ";
				reportItemValueDao.createQuery(updateSql, report_sn, info.getId(), info.getReport_type())
						.executeUpdate();
			}
			// 计算金额
			this.countMoney(info, devicedoc, inspection_conclusion, device_model, dtcs, sdxc, ppc_zcsl, xhc_czrs,
					sc_clzs, qz_q, qz_lj);

			info.setLast_mdy_time(new Date());
			info.setEnter_time2(new Date()); // 报告实际编制日期
			// 保存回写的业务信息和设备基础信息
			infoDao.save(info);
			deviceDao.save(devicedoc);

			System.out.println("--------------------保存成功！---------------------");

		} else {
			throw new Exception("找不到关联的报表ID...");
		}
		// 未选择目录树时初始化RtPersonDir一个版本
		inspectionCommonService.initRtPersonDir(info);
	}

	/**
	 * 计算报告金额
	 * 
	 * @param insInfo
	 * @param devicedoc
	 * @param inspection_conclusion
	 * @param device_model
	 * @param dtcs
	 * @param sdxc
	 * @param ppc_zcsl
	 * @param xhc_czrs
	 * @param sc_clzs
	 * @param qz_q
	 * @param qz_lj
	 * @throws java.text.ParseException
	 */
	public void countMoney(InspectionInfo insInfo, DeviceDocument devicedoc, String inspection_conclusion,
			String device_model, int dtcs, double sdxc, int ppc_zcsl, int xhc_czrs, int sc_clzs, double qz_q,
			double qz_lj) throws java.text.ParseException {
		String check_unit_id = insInfo.getCheck_unit_id(); // 检验部门
		String check_type = insInfo.getInspection().getCheck_type(); // 获取检验类型
		// 计算电梯收费金额
		int dtfy = 0; // 打折后，四舍五入
		double dt_money = 0; // 不打折，也不四舍五入

		Report report = reportService.get(insInfo.getReport_type());
		String report_name = report.getReport_name();
		if (report_name.contains("病床")) {
			if (StringUtil.isNotEmpty(check_unit_id)) {
				if (!Constant.CHECK_UNIT_100090.equals(check_unit_id)
						&& !Constant.CHECK_UNIT_100091.equals(check_unit_id)) {
					if (check_type.equals("3")) {// 定期检验
						// 判断是否有限速器
						if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
							// 判断电梯层数
							if (dtcs <= 5) {
								dtfy = (int) Math.round((500));
							} else {
								dtfy = (int) Math.round(500 + (dtcs - 5) * 50);
							}
						} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
							if (dtcs <= 5) {
								dtfy = (int) Math.round(500 * 1.3 + 200);
							} else {
								dtfy = (int) Math.round((500 + (dtcs - 5) * 50) * 1.3 + 200);
							}

						} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
							if (dtcs <= 5) {
								dtfy = (int) Math.round(500 * 1.3 + 400);
							} else {
								dtfy = (int) Math.round((500 + (dtcs - 5) * 50) * 1.3 + 400);
							}
						}
					} else if (check_type.equals("2")) {// 监督检验
						// 判断电梯层数
						if (dtcs <= 5) {
							dtfy = (int) Math.round(500 * 1.5);
						} else {
							dtfy = (int) Math.round((500 + (dtcs - 5) * 50) * 1.5);
						}
					}
				} else {
					// 2017-12-14应明子涵要求，新疆特检突击队报告费用由检验员手动输入，不再自动计算价格biu~biu~
					// 2018-03-29应明子涵要求，九寨特检突击队报告费用由系统自动计算价格biu~biu~
					// 九寨沟的计算公式均无1.3和1.5的系数，故此处去掉
					if (Constant.CHECK_UNIT_100091.equals(check_unit_id)) {
						if (check_type.equals("3")) {// 定期检验
							// 判断是否有限速器
							if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
								// 判断电梯层数
								if (dtcs <= 5) {
									dtfy = (int) Math.round((500));
								} else {
									dtfy = (int) Math.round(500 + (dtcs - 5) * 50);
								}
							} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
								if (dtcs <= 5) {
									dtfy = (int) Math.round(500 + 200);
								} else {
									dtfy = (int) Math.round((500 + (dtcs - 5) * 50) + 200);
								}

							} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
								if (dtcs <= 5) {
									dtfy = (int) Math.round(500 + 400);
								} else {
									dtfy = (int) Math.round((500 + (dtcs - 5) * 50) + 400);
								}
							}
						} else if (check_type.equals("2")) {// 监督检验
							// 判断电梯层数
							if (dtcs <= 5) {
								dtfy = (int) Math.round(500);
							} else {
								dtfy = (int) Math.round((500 + (dtcs - 5) * 50));
							}
						}
					}
				}
			}
		} else {
			if (StringUtil.isNotEmpty(check_unit_id)) {
				if (!Constant.CHECK_UNIT_100090.equals(check_unit_id)
						&& !Constant.CHECK_UNIT_100091.equals(check_unit_id)) {
					// 从2015-08-01开始，检验的电梯，不再打折；2015-08-01以前检验的电梯，均打9折
					Date check_date = insInfo.getAdvance_time();
					Date no_cheap_date = DateUtil.convertStringToDate("yyyy-MM-dd", "2015-08-01");

					if (check_date.before(no_cheap_date)) {
						if (check_type.equals("3")) {// 定期检验
							// 区分设备类型
							if (devicedoc.getDevice_sort().equals("3100") || devicedoc.getDevice_sort().equals("3200")
									|| devicedoc.getDevice_sort_code().equals("3410")
									|| devicedoc.getDevice_sort_code().equals("3420")) {
								// 判断是否有限速器
								if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
									// 判断电梯层数
									if (dtcs <= 5) {
										dtfy = (int) Math.round((550 * 0.9));
									} else {
										dtfy = (int) Math.round(((550 + (dtcs - 5) * 55) * 0.9));
									}
								} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
									if (dtcs <= 5) {
										dtfy = (int) Math.round(((550 * 1.3 + 200) * 0.9));
									} else {
										dtfy = (int) Math.round((((550 + (dtcs - 5) * 55) * 1.3 + 200) * 0.9));
									}

								} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
									if (dtcs <= 5) {
										dtfy = (int) Math.round(((550 * 1.3 + 400) * 0.9));
									} else {
										dtfy = (int) Math.round((((550 + (dtcs - 5) * 55) * 1.3 + 400) * 0.9));
									}
								}
							}
							// else
							// if(devicedoc.getDevice_sort_code().equals("3170")){//病床电梯
							// //判断是否有限速器
							// if(insInfo.getXsqts().equals("0")){//没有勾选限速器
							// //判断电梯层数
							// if(dtcs<=5){
							// dtfy=(int)Math.round((500*0.9));
							// }else{
							// dtfy=(int)Math.round(((500+(dtcs-5)*50)*0.9));
							// }
							// }else if(insInfo.getXsqts().equals("1")){//勾选一个限速器
							// if(dtcs<=5){
							// dtfy=(int)Math.round(((500*1.3+200)*0.9));
							// }else{
							// dtfy=(int)Math.round((((500+(dtcs-5)*50)*1.3+200)*0.9));
							// }
							//
							// }else if(insInfo.getXsqts().equals("2")){//勾选两个限速器
							// if(dtcs<=5){
							// dtfy=(int)Math.round(((500*1.3+400)*0.9));
							// }else{
							// dtfy=(int)Math.round((((500+(dtcs-5)*50)*1.3+400)*0.9));
							// }
							// }
							//
							// }
							else if (devicedoc.getDevice_sort().equals("3300")) {
								dtfy = (int) (400 * 0.9);
							} else if (devicedoc.getDevice_sort_code().equals("3430")) {
								if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
									dtfy = (int) (400 * 0.9);
								} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
									dtfy = (int) ((400 + 200) * 0.9);
								} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
									dtfy = (int) ((400 + 200 + 200) * 0.9);
								}
							}
						} else if (check_type.equals("2")) {// 监督检验
							// 区分设备类型
							if (devicedoc.getDevice_sort_code().equals("3410")
									|| devicedoc.getDevice_sort_code().equals("3420")
									|| devicedoc.getDevice_sort().equals("3200")
									|| devicedoc.getDevice_sort().equals("3100")) {

								// 判断电梯层数
								if (dtcs <= 5) {
									dtfy = (int) Math.round((550 * 1.5 * 0.9));
								} else {
									dtfy = (int) Math.round(((550 + (dtcs - 5) * 55) * 1.5 * 0.9));
								}

							}
							// else
							// if(devicedoc.getDevice_sort_code().equals("3170")){
							// //判断电梯层数
							// if(dtcs<=5){
							// dtfy=(int) Math.round((500*1.5*0.9));
							// }else{
							// dtfy=(int)Math.round(((500+(dtcs-5)*50)*1.5*0.9));
							// }
							//
							// }
							else if (devicedoc.getDevice_sort().equals("3300")) {
								dtfy = (int) (600 * 0.9);
							} else if (devicedoc.getDevice_sort_code().equals("3430")) {
								if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
									dtfy = (int) (600 * 0.9);
								} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
									dtfy = (int) ((600 + 200) * 0.9);
								} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
									dtfy = (int) ((600 + 200 + 200) * 0.9);
								}
							}
						}
					} else {
						if (check_type.equals("3")) {// 定期检验
							// 区分设备类型
							if (devicedoc.getDevice_sort().equals("3100") || devicedoc.getDevice_sort().equals("3200")
									|| devicedoc.getDevice_sort_code().equals("3410")
									|| devicedoc.getDevice_sort_code().equals("3420")) {
								// 判断是否有限速器
								if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
									// 判断电梯层数
									if (dtcs <= 5) {
										dt_money = 550;
									} else {
										dt_money = (550 + (dtcs - 5) * 55);
									}
								} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
									if (dtcs <= 5) {
										dt_money = 550 * 1.3 + 200;
									} else {
										dt_money = (550 + (dtcs - 5) * 55) * 1.3 + 200;
									}

								} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
									if (dtcs <= 5) {
										dt_money = 550 * 1.3 + 400;
									} else {
										dt_money = (550 + (dtcs - 5) * 55) * 1.3 + 400;
									}
								}
							}
							// else
							// if(devicedoc.getDevice_sort_code().equals("3170")){//病床电梯
							// //判断是否有限速器
							// if(insInfo.getXsqts().equals("0")){//没有勾选限速器
							// //判断电梯层数
							// if(dtcs<=5){
							// dt_money=(int)Math.round((500));
							// }else{
							// dt_money=(int)Math.round(((500+(dtcs-5)*50)));
							// }
							// }else if(insInfo.getXsqts().equals("1")){//勾选一个限速器
							// if(dtcs<=5){
							// dt_money=(int)Math.round(((500*1.3+200)));
							// }else{
							// dt_money=(int)Math.round((((500+(dtcs-5)*50)*1.3+200)));
							// }
							//
							// }else if(insInfo.getXsqts().equals("2")){//勾选两个限速器
							// if(dtcs<=5){
							// dt_money=(int)Math.round(((500*1.3+400)));
							// }else{
							// dt_money=(int)Math.round((((500+(dtcs-5)*50)*1.3+400)));
							// }
							// }
							//
							// }
							else if (devicedoc.getDevice_sort().equals("3300")) {
								dt_money = 400;
							} else if (devicedoc.getDevice_sort_code().equals("3430")) {
								if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
									dt_money = 400;
								} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
									dt_money = 400 + 200;
								} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
									dt_money = 400 + 200 + 200;
								}
							}
						} else if (check_type.equals("2")) {// 监督检验
							// 区分设备类型
							if (devicedoc.getDevice_sort_code().equals("3410")
									|| devicedoc.getDevice_sort_code().equals("3420")
									|| devicedoc.getDevice_sort().equals("3200")
									|| devicedoc.getDevice_sort().equals("3100")) {

								if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
									// 判断电梯层数

									if (dtcs <= 5) {
										dt_money = 550 * 1.5;
									} else {
										dt_money = (550 + (dtcs - 5) * 55) * 1.5;
									}

								} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
									if (dtcs <= 5) {
										dt_money = 550 * 1.5 + 200;
									} else {
										dt_money = (550 + (dtcs - 5) * 55) * 1.5 + 200;
									}
								} else if (insInfo.getXsqts().equals("2")) {// 勾选一个限速器
									if (dtcs <= 5) {
										dt_money = 550 * 1.5 + 400;
									} else {
										dt_money = (550 + (dtcs - 5) * 55) * 1.5 + 400;
									}
								}
							}
							// else
							// if(devicedoc.getDevice_sort_code().equals("3170")){
							// //判断电梯层数
							// if(dtcs<=5){
							// dt_money=(int) Math.round((500*1.5));
							// }else{
							// dt_money=(int)Math.round(((500+(dtcs-5)*50)*1.5));
							// }
							//
							// }
							else if (devicedoc.getDevice_sort().equals("3300")) {
								dt_money = 600;
							} else if (devicedoc.getDevice_sort_code().equals("3430")) {
								if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
									dt_money = 600;
								} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
									dt_money = 600 + 200;
								} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
									dt_money = 600 + 200 + 200;
								}
							}
						}
					}
				} else {
					// 2017-12-14应明子涵要求，新疆特检突击队报告费用由检验员手动输入，不再自动计算价格biu~biu~
					// 2018-03-29应明子涵要求，九寨特检突击队报告费用由系统自动计算价格biu~biu~
					// 九寨沟的计算公式均无1.3和1.5的系数，故此处去掉
					if (Constant.CHECK_UNIT_100091.equals(check_unit_id)) {
						if (check_type.equals("3")) {// 定期检验
							// 区分设备类型
							if (devicedoc.getDevice_sort().equals("3100") || devicedoc.getDevice_sort().equals("3200")
									|| devicedoc.getDevice_sort_code().equals("3410")
									|| devicedoc.getDevice_sort_code().equals("3420")) {
								// 判断是否有限速器
								if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
									// 判断电梯层数
									if (dtcs <= 5) {
										dt_money = 550;
									} else {
										dt_money = (550 + (dtcs - 5) * 55);
									}
								} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
									if (dtcs <= 5) {
										dt_money = 550 + 200;
									} else {
										dt_money = (550 + (dtcs - 5) * 55) + 200;
									}

								} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
									if (dtcs <= 5) {
										dt_money = 550 + 400;
									} else {
										dt_money = (550 + (dtcs - 5) * 55) + 400;
									}
								}
							} else if (devicedoc.getDevice_sort().equals("3300")) {
								dt_money = 400;
							} else if (devicedoc.getDevice_sort_code().equals("3430")) {
								if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
									dt_money = 400;
								} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
									dt_money = 400 + 200;
								} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
									dt_money = 400 + 200 + 200;
								}
							}
						} else if (check_type.equals("2")) {// 监督检验
							// 区分设备类型
							if (devicedoc.getDevice_sort_code().equals("3410")
									|| devicedoc.getDevice_sort_code().equals("3420")
									|| devicedoc.getDevice_sort().equals("3200")
									|| devicedoc.getDevice_sort().equals("3100")) {
								if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
									// 判断电梯层数
									if (dtcs <= 5) {
										dt_money = 550;
									} else {
										dt_money = (550 + (dtcs - 5) * 55);
									}

								} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
									if (dtcs <= 5) {
										dt_money = 550 + 200;
									} else {
										dt_money = (550 + (dtcs - 5) * 55) + 200;
									}
								} else if (insInfo.getXsqts().equals("2")) {// 勾选一个限速器
									if (dtcs <= 5) {
										dt_money = 550 + 400;
									} else {
										dt_money = (550 + (dtcs - 5) * 55) + 400;
									}
								}
							} else if (devicedoc.getDevice_sort().equals("3300")) {
								dt_money = 600;
							} else if (devicedoc.getDevice_sort_code().equals("3430")) {
								if (insInfo.getXsqts().equals("0")) {// 没有勾选限速器
									dt_money = 600;
								} else if (insInfo.getXsqts().equals("1")) {// 勾选一个限速器
									dt_money = 600 + 200;
								} else if (insInfo.getXsqts().equals("2")) {// 勾选两个限速器
									dt_money = 600 + 200 + 200;
								}
							}
						}

					}
				}
			}
		}

		if (devicedoc.getDevice_sort().startsWith("3")) {
			if (StringUtil.isNotEmpty(check_unit_id)) {
				if (!Constant.CHECK_UNIT_100090.equals(check_unit_id)
						&& !Constant.CHECK_UNIT_100091.equals(check_unit_id)) {
					if (dtfy == 0) {
						if (inspection_conclusion.equals("复检合格")) {
							insInfo.setAdvance_fees((double) Math.round(dt_money * 0.3));
						} else {
							insInfo.setAdvance_fees(dt_money);
						}

					} else {
						if (inspection_conclusion.equals("复检合格")) {
							insInfo.setAdvance_fees((double) Math.round(dtfy * 0.3));
						} else {
							insInfo.setAdvance_fees((double) dtfy);
						}
					}
				} else {
					// 2017-12-14应明子涵要求，新疆特检突击队报告费用由检验员手动输入，不再自动计算价格biu~biu~
					// 2018-04-28应明子涵要求，九寨沟特检突击队报告费用由系统自动计算价格biu~biu~
					if (Constant.CHECK_UNIT_100091.equals(check_unit_id)) {
						insInfo.setAdvance_fees(dt_money);
					}
				}
			}
		} else if (devicedoc.getDevice_sort().startsWith("6")) {
			// 游乐设施费用计算
			if ("6900".equals(devicedoc.getDevice_sort())) { // 6900：小火车类
				if (check_type.equals("3")) {
					// 定期检验
					if (xhc_czrs <= 10) {
						// 10人以内（含10人）
						dt_money = 140;
					} else if (11 <= xhc_czrs && xhc_czrs <= 30) {
						// 11人到30人（含30人）
						dt_money = 160;
					} else {
						dt_money = 180;
					}
				} else if (check_type.equals("2")) {
					// 监督检验
					if (xhc_czrs <= 10) {
						// 10人以内（含10人）
						dt_money = 168;
					} else if (11 <= xhc_czrs && xhc_czrs <= 30) {
						// 11人到30人（含30人）
						dt_money = 192;
					} else {
						dt_money = 216;
					}
				}
			} else if ("6A00".equals(devicedoc.getDevice_sort())) { // 6A00：碰碰车类
				if (StringUtil.isEmpty(device_model)) {
					device_model = devicedoc.getDevice_sort_code();
				}
				List<InspectionInfoPay> list = inspectionInfoPayService.getByDeviceSortCodes(device_model);
				for (InspectionInfoPay inspectionInfoPay : list) {
					double pay_value = inspectionInfoPay.getPay_value();
					String check_typep = inspectionInfoPay.getCheck_type();
					if (check_type.equals(check_typep)) {
						dt_money = ppc_zcsl * pay_value;
					}
				}
			} else if ("6800".equals(devicedoc.getDevice_sort())) { // 6800：赛车类
				if (StringUtil.isEmpty(device_model)) {
					device_model = devicedoc.getDevice_sort_code();
				}
				List<InspectionInfoPay> list = inspectionInfoPayService.getByDeviceSortCodes(device_model);
				for (InspectionInfoPay inspectionInfoPay : list) {
					double pay_value = inspectionInfoPay.getPay_value();
					String check_typep = inspectionInfoPay.getCheck_type();
					if (check_type.equals(check_typep)) {
						dt_money = sc_clzs * pay_value;
					}
				}
			} else { // 6A00：游乐设施除了（小火车和碰碰车的其他类型）
				if (StringUtil.isEmpty(device_model)) {
					device_model = devicedoc.getDevice_sort_code();
				}

				List<InspectionInfoPay> list = inspectionInfoPayService.getByDeviceSortCodes(device_model);
				for (InspectionInfoPay inspectionInfoPay : list) {
					double pay_value = inspectionInfoPay.getPay_value();
					String check_typep = inspectionInfoPay.getCheck_type();
					if (check_type.equals(check_typep)) {
						dt_money = pay_value;
					}

				}

				if (list.size() == 0) {
					if (StringUtil.isEmpty(device_model)) {
						device_model = devicedoc.getDevice_sort();
					}
					List<InspectionInfoPay> list1 = inspectionInfoPayService.getByDeviceSortCodes(device_model);
					for (InspectionInfoPay inspectionInfoPay : list1) {
						double pay_value = inspectionInfoPay.getPay_value();
						String check_typep = inspectionInfoPay.getCheck_type();
						if (check_type.equals(check_typep)) {
							dt_money = pay_value;
						}

					}
				}

			}
			if (insInfo.getAdvance_fees() == null) {
				if (dt_money != 0) {
					insInfo.setAdvance_fees((double) dt_money);
				}
			} else {
				if (insInfo.getAdvance_fees() == 0) {
					if (dt_money != 0) {
						insInfo.setAdvance_fees((double) dt_money);
					}
				}
			}
		} else if (devicedoc.getDevice_sort().startsWith("9")) {

			if (StringUtil.isEmpty(device_model)) {
				device_model = devicedoc.getDevice_sort_code();
			}
			// 客运索道费用计算
			// 9100：客运架空索道 9300：客运拖牵索道
			if ("9100".equals(devicedoc.getDevice_sort())) {
				List<InspectionInfoPay> list = inspectionInfoPayService.getByDeviceSortCodes(device_model);
				for (InspectionInfoPay inspectionInfoPay : list) {
					int pay_key = StringUtil.isNotEmpty(inspectionInfoPay.getPay_key())
							? Integer.parseInt(inspectionInfoPay.getPay_key())
							: 0;
					double pay_value = inspectionInfoPay.getPay_value();
					// 在基数的基础上，斜长每增加100米，费用增加费用基数的3%
					String check_typep = inspectionInfoPay.getCheck_type();
					if (check_type.equals(check_typep)) {
						double jsxc = sdxc - pay_key;
						if (jsxc < 100) {
							dt_money = pay_value;
						} else {
							int t = (int) (jsxc / 100);
							dt_money = pay_value + (t * pay_value * 0.03);
						}
					}
				}
			} else if ("9300".equals(devicedoc.getDevice_sort())) {
				if (StringUtil.isEmpty(device_model)) {
					device_model = devicedoc.getDevice_sort_code();
				}
				if (StringUtil.isEmpty(device_model)) {
					device_model = devicedoc.getDevice_sort();
				}

				List<InspectionInfoPay> list = inspectionInfoPayService.getByDeviceSortCodes(device_model);
				for (InspectionInfoPay inspectionInfoPay : list) {
					double pay_value = inspectionInfoPay.getPay_value();
					String check_typep = inspectionInfoPay.getCheck_type();
					if (check_type.equals(check_typep)) {
						dt_money = pay_value;
					}

				}
			}
			if (insInfo.getAdvance_fees() == null) {
				if (dt_money != 0) {
					insInfo.setAdvance_fees((double) dt_money);
				}
			} else {
				if (insInfo.getAdvance_fees() == 0) {
					if (dt_money != 0) {
						insInfo.setAdvance_fees((double) dt_money);
					}
				}
			}
		} else if (devicedoc.getDevice_sort().startsWith("4")) {
			if (StringUtil.isNotEmpty(check_unit_id)) {
				if (!Constant.CHECK_UNIT_100090.equals(check_unit_id)) {
					if ("2".equals(check_type) || "3".equals(check_type)) {
						double money = 0;
						// 起重机费用计算
						Collection<CranePara> crane = devicedoc.getCrane();
						int cws = 0;
						for (CranePara cranePara : crane) {
							if (cranePara.getP_cws() != null && StringUtil.isNotEmpty(cranePara.getP_cws())) {
								cws = Integer.parseInt(cranePara.getP_cws());
							}
						}
						// 2016-01-27修改：桥门式起重机暂时取消自动计算金额功能，以用户手动输入为准（因起重量多数值（例如：30/15）的特殊性）
						/*
						 * if("4170".equals(devicedoc.getDevice_sort_code()) ){// 电动单梁起重机 money = 300;
						 * double jsxc = qzkd-13.5; if(jsxc>=5){ int t = (int) (jsxc / 5); money =money
						 * + t * 50; } }else if((devicedoc.getDevice_sort().startsWith("41")
						 * ||devicedoc.getDevice_sort().startsWith("42"))
						 * &&!"4170".equals(devicedoc.getDevice_sort_code()) ){//双梁桥、 门式起重机
						 * if(qz_q<=20){//起重量Q<=20 money = 400; }else{//起重量Q<=20 double jsxc = qz_q -
						 * 20; money =500; //每增加10t加收50元 if(jsxc>=10){ int t = (int) (jsxc / 10); money
						 * =500 + t * 50; } } double jsxc = qzkd-13.5; if(jsxc>=5){ int t = (int) (jsxc
						 * / 5); money =money + t * 50; } }else
						 */if (devicedoc.getDevice_sort().startsWith("43")) {// 塔式起重机
							// 检验类别（check_type，2：监检 3：定检）
							if ("2".equals(check_type)) {
								// qz_lj = qz_lj / 10000;
								// 监检的起重力矩单位是t(t.m)，所以这里不需要除以10000
							} else if ("3".equals(check_type)) {
								// 定检的起重力矩单位是N.m，所以这里需要除以10000
								qz_lj = qz_lj / 10000;
							}

							if (qz_lj <= 25) {// 起重力矩<=25
								money = 500;
							} else if (qz_lj > 25 && qz_lj <= 40) {// 25<起重力矩<=40
								money = 550;
							} else if (qz_lj > 40 && qz_lj <= 60) {// 40<起重力矩<=60
								dt_money = 600;
							} else if (qz_lj > 60 && qz_lj <= 80) {// 60<起重力矩<=80
								money = 650;
							} else {// 起重力矩>80
								money = 700;
							}

						} else if (devicedoc.getDevice_sort().startsWith("44")
								&& !"4450".equals(devicedoc.getDevice_sort_code())) {// 流动式起重机（排除铁路起重机）
							if (qz_q <= 16) {// 起重量Q<=16
								money = 400;
							} else {// 起重量Q<=16
								double jsxc = qz_q - 16;
								money = 500;
								// 每增加10t加收50元
								if (jsxc >= 10) {
									int t = (int) (jsxc / 10);
									money = 500 + t * 50;
								}
							}
						} else if (devicedoc.getDevice_sort().startsWith("47")) {// 门坐式起重机
							if (qz_q <= 10) {// 起重量Q<=10
								money = 500;
							} else {// 起重量Q<=16
								double jsxc = qz_q - 10;
								money = 600;
								// 每增加10t加收50元
								if (jsxc >= 10) {
									int t = (int) (jsxc / 10);
									money = 600 + t * 50;
								}
							}

						} else if ("4D00".equals(devicedoc.getDevice_sort())) {// 机械式停车设备
							money = 60 * cws;
						} else {
							// 起重机械的其他类型的起重机
							if (StringUtil.isEmpty(device_model)) {
								device_model = devicedoc.getDevice_sort_code();
							}
							List<InspectionInfoPay> list = inspectionInfoPayService.getByDeviceSortCodes(device_model);
							for (InspectionInfoPay inspectionInfoPay : list) {
								double pay_value = inspectionInfoPay.getPay_value();
								String check_typep = inspectionInfoPay.getCheck_type();
								if (check_type.equals(check_typep)) {
									money = pay_value;
								}

							}

							if (list.size() == 0) {
								if (StringUtil.isEmpty(device_model)) {
									device_model = devicedoc.getDevice_sort();
								}

								List<InspectionInfoPay> list1 = inspectionInfoPayService
										.getByDeviceSortCodes(device_model);
								for (InspectionInfoPay inspectionInfoPay : list1) {
									double pay_value = inspectionInfoPay.getPay_value();
									String check_typep = inspectionInfoPay.getCheck_type();
									if (check_type.equals(check_typep)) {
										money = pay_value;
									}

								}
							}
						}

						if ("2".equals(check_type)) {
							dt_money = money * 2;
						} else if ("3".equals(check_type)) {
							dt_money = money;
						}
					}
					if (dt_money != 0) {
						// 2016-01-27修改：桥门式起重机暂时取消自动计算金额功能，以用户手动输入为准（因起重量多数值（例如：30/15）的特殊性）
						if (!(devicedoc.getDevice_sort().startsWith("41")
								&& devicedoc.getDevice_sort().startsWith("42"))) {
							if (insInfo.getAdvance_fees() == null) {
								if (dt_money != 0) {
									insInfo.setAdvance_fees((double) dt_money);
								}
							} else {
								if (insInfo.getAdvance_fees() == 0) {
									if (dt_money != 0) {
										insInfo.setAdvance_fees((double) dt_money);
									}
								}
							}
						}
					}
				} else {
					// 新疆特检突击队起重机报告费用由检验员手动输入，不再自动计算价格
				}
			}

		} else if (devicedoc.getDevice_sort().startsWith("5")) {
			double money = 0;
			// 厂内机动车费用计算
			if ("2".equals(check_type) || "3".equals(check_type)) {
				if (StringUtil.isEmpty(device_model)) {
					device_model = devicedoc.getDevice_sort_code();
				}
				List<InspectionInfoPay> list = inspectionInfoPayService.getByDeviceSortCodes(device_model);
				for (InspectionInfoPay inspectionInfoPay : list) {
					double pay_value = inspectionInfoPay.getPay_value();
					String check_typep = inspectionInfoPay.getCheck_type();
					if (check_type.equals(check_typep)) {
						money = pay_value;
					}

				}

				if (list.size() == 0) {
					if (StringUtil.isEmpty(device_model)) {
						device_model = devicedoc.getDevice_sort();
					}

					List<InspectionInfoPay> list1 = inspectionInfoPayService.getByDeviceSortCodes(device_model);
					for (InspectionInfoPay inspectionInfoPay : list1) {
						double pay_value = inspectionInfoPay.getPay_value();
						String check_typep = inspectionInfoPay.getCheck_type();
						if (check_type.equals(check_typep)) {
							money = pay_value;
						}

					}
				}

				if ("3".equals(check_type)) {
					dt_money = money;
				} else if ("2".equals(check_type)) {
					dt_money = money * 2;
				}
			}
			if (insInfo.getAdvance_fees() == null) {
				if (dt_money != 0) {
					insInfo.setAdvance_fees((double) dt_money);
				}
			} else {
				if (insInfo.getAdvance_fees() == 0) {
					if (dt_money != 0) {
						insInfo.setAdvance_fees((double) dt_money);
					}
				}
			}
		}

		// 西藏报告收费标准，直梯（有机房、无机房）定检1000元整，扶梯定检800元整，杂物梯定检400元整
		// 新疆报告收费标准，直梯（有机房、无机房）定检1000元整，扶梯定检800元整，杂物梯定检400元整
		if (StringUtil.isNotEmpty(check_unit_id)) {
			// 100069：赴藏特检突击队
			if (Constant.CHECK_UNIT_100069.equals(check_unit_id)) {
				double xz_money = 1000.00;
				if (devicedoc.getDevice_sort().equals("3100") || devicedoc.getDevice_sort().equals("3200")) {
					xz_money = 1000.00;
				} else if (devicedoc.getDevice_sort().equals("3400")) {
					if (devicedoc.getDevice_sort_code().equals("3430")) {
						xz_money = 400.00;
					}
				} else if (devicedoc.getDevice_sort().equals("3300")) {
					xz_money = 800.00;
				}
				insInfo.setAdvance_fees(xz_money);
			}
			// 100090：新疆特检突击队
			// 2017-12-14应明子涵要求，新疆特检突击队报告费用由检验员手动输入，不再自动计算价格biu~biu~
			/*
			 * if (Constant.CHECK_UNIT_100090.equals(check_unit_id)) { double xj_money =
			 * 1000.00; if (devicedoc.getDevice_sort().equals("3100") ||
			 * devicedoc.getDevice_sort().equals("3200")) { xj_money = 1000.00; } else if
			 * (devicedoc.getDevice_sort().equals("3400")) { if
			 * (devicedoc.getDevice_sort_code().equals("3430")) { xj_money = 400.00; } }
			 * else if (devicedoc.getDevice_sort().equals("3300")) { xj_money = 800.00; }
			 * insInfo.setAdvance_fees(xj_money); }
			 */
		}
	}

	/**
	 * 保存应收金额（新报表录入前保存）
	 * 
	 * @param request
	 * @return
	 */
	public HashMap<String, Object> saveYsje(HttpServletRequest request) {
		String info_id = request.getParameter("info_id");
		String ysje = request.getParameter("ysje");
		if (ysje != null && !"".equals(ysje.toString().trim())) {
			Double doc = Double.valueOf(ysje.toString());
			reportItemValueDao
					.createSQLQuery(
							"update tzsb_inspection_info set advance_fees='" + doc + "'  where id='" + info_id + "'")
					.executeUpdate();
		}
		return JsonWrapper.successWrapper("保存成功！");
	}
	
	
	/**
	 * 保存科鸿报告标注
	 * @param
	 *   reportMark 标注对象
	 * 	String fk_report_id //报告书id
	 *  String fk_inspection_info_id//业务ID
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void saveLabel(ReportMark reportMark, String fk_report_id, String fk_inspection_info_id) throws Exception {
		//记录基础字段信息,因为再录入环节可能检验员没有填写这个字段，所以保存的时候没有这个字段名，那么我们必须先把这个字段保存导itemvalue表
		List<ReportItemValue> reportItemValues = this.reportItemValueDao.createQuery("from ReportItemValue "
				+ "where item_name=? and fk_inspection_info_id=?", reportMark.getItem(),fk_inspection_info_id).list();
		if(reportItemValues.size() == 0) {
			ReportItemValue reportItemValue = new ReportItemValue();
			reportItemValue.setItem_name(reportMark.getItem());
			reportItemValue.setItem_value(reportMark.getItemValue());
			reportItemValue.setFk_report_id(fk_report_id);
			reportItemValue.setFk_inspection_info_id(fk_inspection_info_id);
			this.reportItemValueDao.save(reportItemValue);
		}
		com.khnt.rbac.bean.User user = SecurityUtil.getSecurityUser().getSysUser();
		//根据报告书ID、页码、和填写项目名称去查找标注。不用ID是因为可能一些特殊操作，可能造成同一页、同一个项目标注重复。比如两台机器或者两个窗口同时标注
		String sql = " from  ReportMark  where fkBusinessId = ? and  item = ? and page = ? and status = '0' and isDel = '0' " ;
		List<ReportMark> markList = reportMarkDao.createQuery( sql, fk_inspection_info_id, reportMark.getItem(), reportMark.getPage() ).list();
		//if(StringUtil.isEmpty(reportMark.getId())){
		//如果没有找到标注，则新增
		if(markList.size()==0) {
			reportMark.setIsDel("0");
			reportMark.setMarkBy(user.getName());
			reportMark.setMarkById(user.getId());
			reportMark.setMarkTime(new Date());
			reportMark.setFkBusinessId(fk_inspection_info_id);
			reportMarkDao.save(reportMark);
		}else{
			/*String hql = "update ReportMark set itemValue=?,markType=?,markContent=?,editById=?,"
					+ "editBy=?,editTime=?,status=? where id=?";
			reportMarkDao.createQuery(hql, reportMark.getItemValue(),
					reportMark.getMarkType(),reportMark.getMarkContent(),user.getId(),
					user.getName(),new Date(),reportMark.getStatus(),reportMark.getId()).executeUpdate();*/

			//找到标注，则进行更新第一条操作（随机一条，没排序，当然可以按照时间进行排序，但是我就不）
			String hql = "update ReportMark set itemValue=?,markType=?,markContent=?,editById=?,"
					+ "editBy=?,editTime=?,status=? where id=?";
			reportMarkDao.createQuery(hql, reportMark.getItemValue(),
					reportMark.getMarkType(),reportMark.getMarkContent(),user.getId(),
					user.getName(),new Date(),reportMark.getStatus(),markList.get(0).getId()).executeUpdate();
			//这段是为了处理可能存在的重复数据，如果一旦出现重复，除了上面更新第一条外，其他的都 say goodbye
			if(markList.size()>1) {
				for(ReportMark mark : markList) {
					mark.setIsDel("1");
					mark.setDelBy(user.getName());
					mark.setDelById(user.getId());
					mark.setDelTime(new Date());
					reportMarkDao.save(mark);
				}
			}
		}
	}

	/**
	 * 删除科鸿报告标注
	 * @param
	 * 	id id //标注id
	 * @return void
	 */
	public void deleteLabel(String id) throws Exception{
		com.khnt.rbac.bean.User user = SecurityUtil.getSecurityUser().getSysUser();
		reportMarkDao.createQuery("update ReportMark set isDel='1',delById=?,delBy=?,delTime=? where id=?",
				user.getId(),user.getName(),new Date(), id).executeUpdate();
	}


	
	public int checkLabel(String businessId) throws Exception{
		String sql = "select count(*) from rt_marks where fk_business_id = ? and status='0' and is_del = '0' ";
		Object obj = this.rtPageDao.createSQLQuery(sql, businessId).uniqueResult();
		int num = Integer.parseInt(obj.toString());
		return num;
	}
	

	public List<Object[]> checkLabel2(String info_ids) throws Exception{
		String sql = "select fk_business_id,count(*) from rt_marks where fk_business_id in ('"+info_ids.replaceAll(",", "','")+"') and status='0' and is_del = '0' ";
		@SuppressWarnings("unchecked")
		List<Object[]> list = this.rtPageDao.createSQLQuery(sql).list();
		return list;
	}

}
