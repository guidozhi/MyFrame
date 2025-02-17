package com.lsts.webservice.cxf.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khnt.base.Factory;
import com.khnt.utils.StringUtil;
import com.lsts.common.service.CodeTablesService;
import com.lsts.log.service.SysLogService;


/**
 * 报告书、设备信息查询业务逻辑对象
 * @ClassName QueryDataService
 * @JDK 1.7
 * @author GaoYa
 * @date 2014-09-16 上午10:28:00
 */
@Service("queryDataService")
public class QueryDataService{
	private Logger logger = Logger.getLogger(this.getClass());
	
	public static final String CHECK_TYPE = "BASE_CHECK_TYPE";	// 设备类型
	private static String res = ""; // 返回结果
	private static Connection conn = null;  // 数据库连接
    private static PreparedStatement pstmt = null; // 数据库操作对象
    private static ResultSet rs = null; // 结果集
    
    @Autowired
    private CodeTablesService codeTablesService;
    @Autowired
	private SysLogService logService;
    /**
	 * 查询设备和报告书信息
	 * @param query_column -- 查询字段
	 * @param value -- 字段值
	 * @param bjyear -- 检验年份
	 * @param reportStatus -- 报告状态
	 * @return 
	 * @author xuqianhui
	 * @date 2019年2月20日
	 */
    @SuppressWarnings("unchecked")
    public String queryDevicesThirdVer(String query_column, String value, String bjyear, String reportStatus){
    	Date date_start = new Date();
    	String sql = "select s.* from (select device.id,substr(device.device_sort_code,0,1) device_type,device.device_name,info.report_com_name company_name,info.internal_num,device.device_registration_code,"
    		+ " ins.check_type,info.advance_time,info.inspection_conclusion,info.flow_note_name,info.advance_fees,info.report_sn,info.is_validation,info.device_qr_code"
			+ " from tzsb_inspection_info info, tzsb_inspection ins,"
			+ " base_device_document device where info.fk_tsjc_device_document_id = device.id(+) and info.fk_inspection_id = ins.id(+)"
			+ " and info.data_status < '99'"
			+ " and device.device_status < '99'"; 
    		if (StringUtil.isNotEmpty(query_column) && StringUtil.isNotEmpty(value)) {
    			if ("zcdm1".equals(query_column)) {
    				sql += " and device.device_registration_code like '%"+value+"%' ";
    			}else if("sydw".equals(query_column)){
    				sql += " and info.report_com_name like '%"+value+"%' ";
    			}else if("bjrq".equals(query_column)){
    				sql += " and ins.inspection_time = to_date('"+value+"','yyyy-MM-dd') ";
    			}else if("jyrq".equals(query_column)){
    				sql += " and info.advance_time = to_date('"+value+"','yyyy-MM-dd') ";
    			}else if ("ewmbh".equals(query_column)) {
    				sql += " and info.device_qr_code = '"+value+"' ";
    			}else if ("wbdw".equals(query_column)) {
    				sql = sql + " and info.maintain_unit_name like '%" + value + "%' ";
    			}else if ("sbmc".equals(query_column)) {
    				sql = sql + " and device.device_name like '%" + value + "%' ";
    			}
			}
    		if(StringUtil.isNotEmpty(bjyear)) {
    			String[] bjyearArr = bjyear.split(",");
    			String bjyearTemp = "";
    			for(String year : bjyearArr) {
    				if(StringUtil.isEmpty(bjyearTemp)) {
    					bjyearTemp = "\'"+year+"\'";
    				}else {
    					bjyearTemp = bjyearTemp+","+"\'"+year+"\'";
    				}
    			}
    			sql = sql + " and to_char(info.advance_time,'yyyy') in (" + bjyearTemp + ") ";
    		}
    		if(StringUtil.isNotEmpty(reportStatus)) {
    			String[] reportStatusArr = reportStatus.split(",");
    			String reportStatusTemp = "";
    			for(String status : reportStatusArr) {
    				if("cjz".equals(status)) {
    					if(StringUtil.isEmpty(reportStatusTemp)) {
        					reportStatusTemp = "'报告录入','报告送审','报告审批','报告审核','报告签发','打印报告'";
        				}else {
        					reportStatusTemp = reportStatusTemp+",'报告录入','报告送审','报告审批','报告审核','报告签发','打印报告'";
        				}
        			}else if("klq".equals(status)) {
        				if(StringUtil.isEmpty(reportStatusTemp)) {
        					reportStatusTemp = "'报告领取'";
        				}else {
        					reportStatusTemp = reportStatusTemp+",'报告领取'";
        				}
        			}else if("ylq".equals(status)) {
        				if(StringUtil.isEmpty(reportStatusTemp)) {
        					reportStatusTemp = "'报告归档'";
        				}else {
        					reportStatusTemp = reportStatusTemp+",'报告归档'";
        				}
        			}
    				
    			}
    			sql = sql + " and info.flow_note_name in (" + reportStatusTemp + ") ";
    		}
			sql += " order by info.advance_time desc) s where rownum <= 500 ";
			if(StringUtil.isNotEmpty(query_column) && StringUtil.isNotEmpty(value) && "report_sn".equals(query_column)) {
				sql = "select device.id,substr(device.device_sort_code,0,1) device_type,device.device_name,info.report_com_name company_name,info.internal_num,device.device_registration_code, info.CHECK_CATEGORY_CODE check_type,info.advance_time,info.inspection_conclusion,info.flow_note_name,info.advance_fees,info.report_sn,info.is_validation,info.device_qr_code from tzsb_inspection_info info, base_device_document device where info.fk_tsjc_device_document_id = device.id(+) and info.data_status < '99' and device.device_status < '99' and info.report_sn = '" + value + "' ";
			}
			System.out.println("查询语句：" + sql);
		try {
			conn = getConn();
                // 返回XML格式数据
                Element root = DocumentHelper.createElement("root");
                Element set = null;
                pstmt = conn.prepareStatement(sql);
                System.out.println("【获取设备信息SQL：】" + sql);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                int count = 0;
                while (rs.next()) {
                	count++;
                    set = root.addElement("set");
                    for (int i = 1; i <= columnCount; i++) {
                    	String column_name = rsmd.getColumnName(i);
                    	String column_value = rs.getString(column_name);
                    	if ("CHECK_TYPE".equals(column_name)) {
							column_value = codeTablesService.getValueByCode(CHECK_TYPE, column_value);
						}
                    	if("IS_VALIDATION".equals(column_name)){
                    		if(column_value!=null){
                    			if(column_value.equals("1")){
                        			column_name="FLOW_NOTE_NAME";
                        			column_value="成都市局验证";
                        		}
                    		}
                    		
                    	}
                        set.addAttribute(column_name, column_value);
                    }
                }
                res = root.asXML();  
                // 记录日志
                logService.setLogs(null, "接口-报告查询，"+query_column+"（"+value+"），bjyear（"+bjyear+"），reportStatus（"+reportStatus+"）。sql:"+sql, "查询成功！共"+count+"条结果。耗时："+(new Date().getTime()-date_start.getTime()), "customer", "customer",
        				new Date(),null);
        } catch (Exception e) {
        	res = "获取数据失败：" + e.getMessage();
        	try {
        		// 记录日志
				logService.setLogs(null, "接口-报告查询，"+query_column+"（"+value+"），bjyear（"+bjyear+"），reportStatus（"+reportStatus+"）。sql:"+sql, "查询失败。耗时："+(new Date().getTime()-date_start.getTime())+"，"+e.getMessage(), "customer", "customer",
						new Date(),null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
            e.printStackTrace();
        }
        closeConn();
        logger.setLevel(Level.INFO); 
        logger.info("测试调用webservice接口查询"+query_column+"（"+value+"）类数据");
        return res;
    }
    /**
	 * 查询设备和报告书信息
	 * @param query_column -- 查询字段
	 * @param value -- 字段值
	 * @param bjyear -- 报检年份
	 * @return 
	 * @author xuqianhui
	 * @date 2019年1月18日
	 */
    @SuppressWarnings("unchecked")
    public String queryDevicesNew(String query_column, String value, String bjyear){
    	Date date_start = new Date();
    	String sql = "select s.* from (select device.id,substr(device.device_sort_code,0,1) device_type,device.device_name,info.report_com_name company_name,info.internal_num,device.device_registration_code,"
    		+ " ins.check_type,info.advance_time,info.inspection_conclusion,info.flow_note_name,info.advance_fees,info.report_sn,info.is_validation,info.device_qr_code"
			+ " from tzsb_inspection_info info, tzsb_inspection ins,"
			+ " base_device_document device where info.fk_tsjc_device_document_id = device.id(+) and info.fk_inspection_id = ins.id(+)"
			+ " and info.data_status < '99'"
			+ " and device.device_status < '99'"; 
    		if (StringUtil.isNotEmpty(query_column) && StringUtil.isNotEmpty(value)) {
    			if ("zcdm1".equals(query_column)) {
    				sql += " and device.device_registration_code like '%"+value+"%' ";
    			}else if("sydw".equals(query_column)){
    				sql += " and info.report_com_name like '%"+value+"%' ";
    			}else if("bjrq".equals(query_column)){
    				sql += " and ins.inspection_time = to_date('"+value+"','yyyy-MM-dd') ";
    			}else if("jyrq".equals(query_column)){
    				sql += " and info.advance_time = to_date('"+value+"','yyyy-MM-dd') ";
    			}else if ("ewmbh".equals(query_column)) {
    				sql += " and info.device_qr_code = '"+value+"' ";
    			}else if ("wbdw".equals(query_column)) {
    				sql = sql + " and info.maintain_unit_name like '%" + value + "%' ";
    			}else if ("sbmc".equals(query_column)) {
    				sql = sql + " and device.device_name like '%" + value + "%' ";
    			}
			}
    		if(StringUtil.isNotEmpty(bjyear)) {
    			String[] bjyearArr = bjyear.split(",");
    			String bjyearTemp = "";
    			for(String year : bjyearArr) {
    				if(StringUtil.isEmpty(bjyearTemp)) {
    					bjyearTemp = "\'"+year+"\'";
    				}else {
    					bjyearTemp = bjyearTemp+","+"\'"+year+"\'";
    				}
    			}
    			sql = sql + " and to_char(ins.inspection_time,'yyyy') in (" + bjyearTemp + ") ";
    		}
			sql += " order by info.advance_time desc) s where rownum <= 500 ";
			if(StringUtil.isNotEmpty(query_column) && StringUtil.isNotEmpty(value) && "report_sn".equals(query_column)) {
				sql = "select device.id,substr(device.device_sort_code,0,1) device_type,device.device_name,info.report_com_name company_name,info.internal_num,device.device_registration_code, info.CHECK_CATEGORY_CODE check_type,info.advance_time,info.inspection_conclusion,info.flow_note_name,info.advance_fees,info.report_sn,info.is_validation,info.device_qr_code from tzsb_inspection_info info, base_device_document device where info.fk_tsjc_device_document_id = device.id(+) and info.data_status < '99' and device.device_status < '99' and info.report_sn = '" + value + "' ";
			}
			System.out.println("查询语句：" + sql);
		try {
			conn = getConn();
                // 返回XML格式数据
                Element root = DocumentHelper.createElement("root");
                Element set = null;
                pstmt = conn.prepareStatement(sql);
                System.out.println("【获取设备信息SQL：】" + sql);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                int count = 0;
                while (rs.next()) {
                	count++;
                    set = root.addElement("set");
                    for (int i = 1; i <= columnCount; i++) {
                    	String column_name = rsmd.getColumnName(i);
                    	String column_value = rs.getString(column_name);
                    	if ("CHECK_TYPE".equals(column_name)) {
							column_value = codeTablesService.getValueByCode(CHECK_TYPE, column_value);
						}
                    	if("IS_VALIDATION".equals(column_name)){
                    		if(column_value!=null){
                    			if(column_value.equals("1")){
                        			column_name="FLOW_NOTE_NAME";
                        			column_value="成都市局验证";
                        		}
                    		}
                    		
                    	}
                        set.addAttribute(column_name, column_value);
                    }
                }
                res = root.asXML();  
                // 记录日志
                logService.setLogs(null, "接口-报告查询，"+query_column+"（"+value+"），bjyear（"+bjyear+"）。sql:"+sql, "查询成功！共"+count+"条结果。耗时："+(new Date().getTime()-date_start.getTime()), "customer", "customer",
        				new Date(),null);
        } catch (Exception e) {
        	res = "获取数据失败：" + e.getMessage();
        	try {
        		// 记录日志
				logService.setLogs(null, "接口-报告查询，"+query_column+"（"+value+"），bjyear（"+bjyear+"）。sql:"+sql, "查询失败。耗时："+(new Date().getTime()-date_start.getTime())+"，"+e.getMessage(), "customer", "customer",
						new Date(),null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
            e.printStackTrace();
        }
        closeConn();
        logger.setLevel(Level.INFO); 
        logger.info("测试调用webservice接口查询"+query_column+"（"+value+"）类数据");
        return res;
    }
    /**
	 * 查询设备和报告书信息
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @return 
	 * @author GaoYa
	 * @date 2014-09-16 上午10:02:00
	 */
    @SuppressWarnings("unchecked")
    public String queryDevices(String query_column, String value){
    	String sql = "select device.id,substr(device.device_sort_code,0,1) device_type,device.device_name,info.report_com_name company_name,info.internal_num,device.device_registration_code,"
    		+ " ins.check_type,info.advance_time,info.inspection_conclusion,info.flow_note_name,info.advance_fees,info.report_sn,info.is_validation,info.device_qr_code"
			+ " from tzsb_inspection_info info, tzsb_inspection ins,"
			+ " base_device_document device where rownum <= 500 "; 
    		if (StringUtil.isNotEmpty(query_column) && StringUtil.isNotEmpty(value)) {
    			if ("zcdm1".equals(query_column)) {
    				sql += " and device.device_registration_code like '%"+value+"%' ";
    			}else if("sydw".equals(query_column)){
    				sql += " and info.report_com_name like '%"+value+"%' ";
    			}else if("bjrq".equals(query_column)){
    				sql += " and ins.inspection_time = to_date('"+value+"','yyyy-MM-dd') ";
    			}else if("jyrq".equals(query_column)){
    				sql += " and info.advance_time = to_date('"+value+"','yyyy-MM-dd') ";
    			}else if("report_sn".equals(query_column)){
    				sql += " and info.report_sn like '%"+value+"%' ";
    			}else if ("ewmbh".equals(query_column)) {
    				sql += " and info.device_qr_code = '"+value+"' ";
    			}
			}
			sql += " and info.fk_tsjc_device_document_id = device.id(+)"
				+ " and info.fk_inspection_id = ins.id(+) "
				+ " and info.data_status <> '99'"
				+ " and device.device_status <> '99'"
				+ " order by info.advance_time desc ";
			System.out.println("查询语句：" + sql);
		try {
			conn = getConn();
                // 返回XML格式数据
                Element root = DocumentHelper.createElement("root");
                Element set = null;
                pstmt = conn.prepareStatement(sql);
                System.out.println("【获取设备信息SQL：】" + sql);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                while (rs.next()) {
                    set = root.addElement("set");
                    for (int i = 1; i <= columnCount; i++) {
                    	String column_name = rsmd.getColumnName(i);
                    	String column_value = rs.getString(column_name);
                    	if ("CHECK_TYPE".equals(column_name)) {
							column_value = codeTablesService.getValueByCode(CHECK_TYPE, column_value);
						}
                    	if("IS_VALIDATION".equals(column_name)){
                    		if(column_value!=null){
                    			if(column_value.equals("1")){
                        			column_name="FLOW_NOTE_NAME";
                        			column_value="成都市局验证";
                        		}
                    		}
                    		
                    	}
                        set.addAttribute(column_name, column_value);
                    }
                }
                res = root.asXML();          
        } catch (Exception e) {
        	res = "获取数据失败：" + e.getMessage();
            e.printStackTrace();
        }
        closeConn();
        logger.setLevel(Level.INFO); 
        logger.info("测试调用webservice接口查询"+query_column+"（"+value+"）类数据");
        return res;
    }
    
    /**
	 * 查询批量报检的报告书信息
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @return 
	 * @author GaoYa
	 * @date 2015-09-09 下午05:10:00
	 */
    @SuppressWarnings("unchecked")
    public String queryZZJDReports(String query_column, String value){
    	String sql = "select info.report_com_name company_name,"
    		+ "i.check_type,info.advance_time,info.inspection_conclusion,info.flow_note_name,info.advance_fees,ins.device_name,info.report_sn "
			+ " from tzsb_inspection_info info, tzsb_inspection_zzjd i,tzsb_inspection_zzjd_info ins"
			+ " where rownum <= 500 "; 
    		if (StringUtil.isNotEmpty(query_column) && StringUtil.isNotEmpty(value)) {
    			if("sydw".equals(query_column)){
    				sql += " and info.report_com_name like '%"+value+"%' ";
    			}else if("jyrq".equals(query_column)){
    				sql += " and info.advance_time = to_date('"+value+"','yyyy-MM-dd') ";
    			}else if("report_sn".equals(query_column)){
    				sql += " and info.report_sn like '%"+value+"%' ";
    			}
			}
			sql += " and info.id = ins.fk_inspection_info_id"
				+ " and i.id = ins.fk_inspection_zzjd_id"
				+ " and info.data_status <> '99'"
				+ " and i.data_status <> '99'"
				+ " and ins.data_status <> '99'"
				+ " order by info.advance_time desc ";
			System.out.println("查询语句：" + sql);
		try {
			conn = getConn();
                // 返回XML格式数据
                Element root = DocumentHelper.createElement("root");
                Element set = null;
                pstmt = conn.prepareStatement(sql);
                System.out.println("【获取报告书信息SQL：】" + sql);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                while (rs.next()) {
                    set = root.addElement("set");
                    for (int i = 1; i <= columnCount; i++) {
                    	String column_name = rsmd.getColumnName(i);
                    	String column_value = rs.getString(column_name);
                    	if ("CHECK_TYPE".equals(column_name)) {
							column_value = codeTablesService.getValueByCode(CHECK_TYPE, column_value);
						}
                        set.addAttribute(column_name, column_value);
                    }
                }
                res = root.asXML();          
        } catch (Exception e) {
        	res = "获取数据失败：" + e.getMessage();
            e.printStackTrace();
        }
        closeConn();
        //logger.setLevel(Level.INFO); 
        //logger.info("测试调用webservice接口查询"+query_column+"（"+value+"）类数据");
        return res;
    }
    
    /**
	 * 查询报告电子印章信息
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @return 
	 * @author GaoYa
	 * @date 2016-09-27 下午04:52:00
	 */
    @SuppressWarnings("unchecked")
    public String queryDZYZPrintInfos(String query_column, String value){
    	String sql = "select info.report_sn, log.op_action, log.op_user_name, log.op_time "
			+ " from tzsb_inspection_info info, sys_dzyz_log log "
			+ " where rownum <= 10 "; 
    		if (StringUtil.isNotEmpty(query_column) && StringUtil.isNotEmpty(value)) {
    			if("report_sn".equals(query_column)){
    				sql += " and info.report_sn like '%"+value+"%' ";
    			}
    			if("id".equals(query_column)){
    				sql += " and info.id = '"+value+"' ";
    			}
			}
			sql += " and info.id = log.business_id "
				+ " and info.data_status <> '99' "
				+ " and log.op_action like '%报告盖章%' ";
			System.out.println("【获取报告电子印章信息SQL：】" + sql);
		try {
			conn = getConn();
                // 返回XML格式数据
                Element root = DocumentHelper.createElement("root");
                Element set = null;
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                while (rs.next()) {
                    set = root.addElement("set");
                    for (int i = 1; i <= columnCount; i++) {
                    	String column_name = rsmd.getColumnName(i);
                    	String column_value = rs.getString(column_name);
                        set.addAttribute(column_name, column_value);
                    }
                }
                res = root.asXML();          
        } catch (Exception e) {
        	res = "获取数据失败：" + e.getMessage();
            e.printStackTrace();
        }
        closeConn();
        logger.setLevel(Level.INFO); 
        logger.info("调用webservice接口查询报告电子印章信息"+query_column+"（"+value+"）");
        return res;
    }
    
    /**
	 * 查询罐车（包括常压罐车、汽车罐车、铁路罐车）检验报告信息（查询使用单位、车牌、报告编号、咨询电话18190875982）
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @return 
	 * @author GaoYa
	 * @date @date 2016-10-27 上午10:56:00
	 */
    @SuppressWarnings("unchecked")
    public String queryGCInfos(String query_column, String value){
    	String sql = "select info.report_com_name, p.ladle_car_number, info.report_sn, info.inspection_conclusion report_result "
			+ " from tzsb_inspection_info info, BASE_DEVICE_DOCUMENT d, base_device_pressurevessels p"
			+ " where d.id(+) = info.fk_tsjc_device_document_id and d.device_sort_code in ('2610','2210','2220')and d.id=p.fk_tsjc_device_document_id(+)"; 
    		if (StringUtil.isNotEmpty(query_column) && StringUtil.isNotEmpty(value)) {
    			if("info_id".equals(query_column)){
    				sql += " and info.id = '"+value+"' ";
    			}
    			if("device_id".equals(query_column)){
    				sql += " and d.id = '"+value+"' ";
    			}
			}
			sql += " and info.data_status <> '99' ";
			System.out.println("【获取罐车检验报告信息SQL：】" + sql);
		try {
			conn = getConn();
                // 返回XML格式数据
                Element root = DocumentHelper.createElement("root");
                Element set = null;
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                while (rs.next()) {
                    set = root.addElement("set");
                    for (int i = 1; i <= columnCount; i++) {
                    	String column_name = rsmd.getColumnName(i);
                    	String column_value = rs.getString(column_name);
                        set.addAttribute(column_name, column_value);
                    }
                }
                res = root.asXML();          
        } catch (Exception e) {
        	res = "获取数据失败：" + e.getMessage();
            e.printStackTrace();
        }
        closeConn();
        logger.setLevel(Level.INFO); 
        logger.info("调用webservice接口查询罐车检验报告信息"+query_column+"（"+value+"）");
        return res;
    }
    
    /**
	 * 查询报告书信息
	 * @param device_type -- 设备类别（设备大类）
	 * @return 
	 * @author GaoYa
	 * @date 2014-09-16 上午10:02:00
	 */
    @SuppressWarnings("unchecked")
    public String queryReports(String device_type){
    	String sql = "select info.id,info.report_com_name,info.report_sn,ins.check_type,info.advance_time,info.flow_note_name,info.advance_fees,substr(device.device_sort_code,0,1) device_type,info.device_qr_code,info.internal_num "
			+ " from tzsb_inspection_info info, tzsb_inspection ins,"
			+ " base_device_document device where rownum <= 50 "; 
			if (StringUtil.isNotEmpty(device_type)) {
				sql += " and substr(device.device_sort,0,1) = '" + device_type + "'";
			}
			sql += " and info.fk_tsjc_device_document_id = device.id(+)"
			+ " and info.fk_inspection_id = ins.id(+) "
			+ " and info.data_status <> '99'"
			+ " and device.device_status <> '99'"
			+ " order by info.advance_time desc ";
			System.out.println("查询语句：" + sql);
		try {
			conn = getConn();
                // 返回XML格式数据
                Element root = DocumentHelper.createElement("root");
                Element set = null;
                pstmt = conn.prepareStatement(sql);
                System.out.println("【获取报告书信息SQL：】" + sql);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                while (rs.next()) {
                    set = root.addElement("set");
                    for (int i = 1; i <= columnCount; i++) {
                    	String column_name = rsmd.getColumnName(i);
                    	String column_value = rs.getString(column_name);
                    	if ("CHECK_TYPE".equals(column_name)) {
							column_value = codeTablesService.getValueByCode(CHECK_TYPE, column_value);
						}
                        set.addAttribute(column_name, column_value);
                    }
                }
                res = root.asXML();          
        } catch (Exception e) {
        	res = "获取数据失败：" + e.getMessage();
            e.printStackTrace();
        }
        closeConn();
        logger.setLevel(Level.INFO); 
        logger.info("测试调用webservice接口查询"+device_type+"类数据");
        return res;
    	
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
}
