package com.lsts.webservice.cxf.server;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 * 报告书、设备数据查询接口（@WebService这个一定要写上，否则webservice访问不到）
 * 
 * @ClassName IQueryData
 * @JDK 1.7
 * @author GaoYa
 * @date 2014-09-16 上午09:45:00
 */
@WebService
@SOAPBinding(style = Style.RPC)
public interface IQueryData {
	
	/**
	 * 查询报告书信息
	 * @param device_type -- 设备类别（设备大类）
	 * @return 
	 * @author GaoYa
	 * @date 2014-09-16 上午09:50:00
	 */
	public String queryReports(String device_type);	
	
	/**
	 * 查询设备和报告书信息
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @param bjyear -- 检验年份
	 * @param reportStatus -- 报告状态
	 * @return 
	 * @author xuqianhui
	 * @date 2019年2月20日
	 */
	public String queryDevicesThirdVer(String query_column, String column_value, String bjyear, String reportStatus);
	
	/**
	 * 查询设备和报告书信息
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @param bjyear -- 报检年份
	 * @return 
	 * @author xuqianhui
	 * @date 2019年1月18日
	 */
	public String queryDevicesNew(String query_column, String column_value, String bjyear);
	
	/**
	 * 查询设备和报告书信息
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @return 
	 * @author GaoYa
	 * @date 2014-09-16 上午09:55:00
	 */
	public String queryDevices(String query_column, String column_value);
	
	
	/**
	 * 查询批量报检的报告信息
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @return 
	 * @author GaoYa
	 * @date 2015-09-09 下午05:05:00
	 */
	public String queryZZJDReports(String query_column, String column_value);
	
	/**
	 * 查询报告电子印章信息（查询报告基本信息，含盖章信息）
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @return 
	 * @author GaoYa
	 * @date 2016-09-27 下午04:49:00
	 */
	public String queryDZYZPrintInfos(String query_column, String column_value);
	
	/**
	 * 查询罐车检验报告信息（查询使用单位、车牌、报告编号、咨询电话18190875982）
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @return 
	 * @author GaoYa
	 * @date 2016-10-27 上午10:42:00
	 */
	public String queryGCInfos(String query_column, String column_value);
	
	public String sayHi(String name);	
}
