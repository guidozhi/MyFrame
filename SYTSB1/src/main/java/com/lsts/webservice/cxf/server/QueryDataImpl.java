package com.lsts.webservice.cxf.server;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 报告书、设备数据查询接口实现类（@WebService这个一定要写上，否则webservice访问不到）
 * 
 * @ClassName QueryDataImpl
 * @JDK 1.7
 * @author GaoYa
 * @date 2014-09-16 上午10:09:00
 */
@WebService(endpointInterface="com.lsts.webservice.cxf.server.IQueryData")
@SOAPBinding(style = Style.RPC)
public class QueryDataImpl implements IQueryData {
	@Autowired
	private QueryDataService queryDataService;

	/**
	 * 查询报告书信息
	 * @param device_type -- 设备类别（设备大类）
	 * @return 
	 * @author GaoYa
	 * @date 2014-09-16 上午09:50:00
	 */
	public String queryReports(String device_type){
		return queryDataService.queryReports(device_type);
	}
	
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
	public String queryDevicesThirdVer(String query_column, String column_value, String bjyear, String reportStatus){
		return queryDataService.queryDevicesThirdVer(query_column, column_value, bjyear, reportStatus);
	}
	
	/**
	 * 查询设备和报告书信息
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @param bjyear -- 报检年份
	 * @return 
	 * @author xuqianhui
	 * @date 2019年1月18日
	 */
	public String queryDevicesNew(String query_column, String column_value, String bjyear){
		return queryDataService.queryDevicesNew(query_column, column_value, bjyear);
	}
	
	/**
	 * 查询设备和报告书信息
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @return 
	 * @author GaoYa
	 * @date 2014-09-16 上午09:55:00
	 */
	public String queryDevices(String query_column, String column_value){
		return queryDataService.queryDevices(query_column, column_value);
	}
	
	/**
	 * 查询批量报检的报告书信息
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @return 
	 * @author GaoYa
	 * @date 2015-09-09 下午05:07:00
	 */
	public String queryZZJDReports(String query_column, String column_value){
		return queryDataService.queryZZJDReports(query_column, column_value);
	}
	
	/**
	 * 查询报告电子印章信息
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @return 
	 * @author GaoYa
	 * @date 2016-09-27 下午04:51:00
	 */
	public String queryDZYZPrintInfos(String query_column, String column_value){
		return queryDataService.queryDZYZPrintInfos(query_column, column_value);
	}
	
	/**
	 * 查询罐车检验报告信息（查询使用单位、车牌、报告编号、咨询电话18190875982）
	 * @param query_column -- 查询字段
	 * @param column_value -- 字段值
	 * @return 
	 * @author GaoYa
	 * @date @date 2016-10-27 上午10:45:00
	 */
	public String queryGCInfos(String query_column, String column_value){
		return queryDataService.queryGCInfos(query_column, column_value);
	}

	public String sayHi(String name){
		return "Hello," + name;
	}
	
	public QueryDataService getQueryDataService() {
		return queryDataService;
	}

	public void setQueryDataService(QueryDataService queryDataService) {
		this.queryDataService = queryDataService;
	}



}
