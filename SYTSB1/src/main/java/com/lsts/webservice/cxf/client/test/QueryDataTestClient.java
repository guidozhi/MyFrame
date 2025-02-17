package com.lsts.webservice.cxf.client.test;

import java.util.Iterator;
import java.util.List;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.lsts.webservice.cxf.server.IQueryData;


/**
 * 调用scts服务接口测试类
 * 
 * @ClassName QueryDataTestClient
 * @JDK 1.7
 * @author GaoYa
 * @date 2014-09-16 上午11:20:00
 */
public class QueryDataTestClient {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		//创建WebService客户端代理工厂  
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();  
		//注册WebService接口  
		factory.setServiceClass(IQueryData.class);  
		
		//设置WebService地址  
		factory.setAddress("http://localhost:8080/ws/QueryData");  
		//factory.setAddress("http://kh.scsei.org.cn/ws/QueryData");  
		IQueryData queryService = (IQueryData)factory.create();  
		System.out.println("invoke webservice...");  		
		try {
			/*
			// 1、测试根据设备类别获取报告书信息 
			// 数据结构说明（report_com_name：单位名称，report_sn：报告书编号，check_type：检验类型，advance_time：检验日期，flow_note_name：检验状态，advance_fees：预收金额）
			System.out.println("测试根据设备类别获取报告书信息，正在查询...");  
			String res = queryService.queryReports("3");	//3：电梯 
			System.out.println("返回结果（报告书信息）：" + res);  
			// 解析返回结果字符串
            Document document = DocumentHelper.parseText(res);
            Element root = document.getRootElement();
            Element set = (Element) root.selectSingleNode("/root");
            Iterator<Element> iterator = set.elementIterator("set");
            List list = set.elements();
            System.out.println("返回结果数量（报告书信息）====" + list.size());
            Element element = null;
            while (iterator.hasNext()) {
                element = iterator.next();
                Iterator iter = element.attributeIterator();
                while (iter.hasNext()) {
                    Attribute attribute = (Attribute) iter.next();
                    System.out.println(attribute.getName() + ":" + element.attributeValue(attribute.getName()));                  
                } 
            }
            System.out.println("测试根据设备类别获取报告书信息，查询结束。");  */
            
            
			/*
			 * // 2、测试根据查询条件（使用单位（sydw）、设备注册代码（zcdm1）、报检日期（bjrq）、检验日期（jyrq））获取设备信息 //
			 * 数据结构说明（device_name：设备类型，company_name：使用单位，internal_num：自编号，
			 * device_registration_code：设备注册代码，check_type：检验类型，advance_time：检验日期，
			 * inspection_conclusion：检验结果，flow_note_name：检验状态，advance_fees：预收金额，report_sn：
			 * 报告编号） System.out.println(
			 * "测试根据查询条件（报告编号（report_sn）、使用单位（sydw）、设备注册代码（zcdm1）、报检日期（bjrq）、检验日期（jyrq））获取设备信息，正在查询..."
			 * ); String res1 = queryService.queryDevices("sydw", "成都玮盛物业服务有限公司");
			 * System.out.println("返回结果（设备信息）：" + res1); // 解析返回结果字符串 Document document1 =
			 * DocumentHelper.parseText(res1); Element root1 = document1.getRootElement();
			 * Element set1 = (Element) root1.selectSingleNode("/root"); Iterator<Element>
			 * iterator1 = set1.elementIterator("set"); List list1 = set1.elements();
			 * System.out.println("返回结果数量（设备信息）====" + list1.size()); Element element1 =
			 * null; while (iterator1.hasNext()) { element1 = iterator1.next(); Iterator
			 * iter = element1.attributeIterator(); while (iter.hasNext()) { Attribute
			 * attribute = (Attribute) iter.next(); System.out.println(attribute.getName() +
			 * ":" + element1.attributeValue(attribute.getName())); } }
			 * System.out.println("测试根据查询条件（使用单位、设备注册代码、报检日期、检验日期）获取设备信息，查询结束。");
			 */
			// 2.1、测试根据查询条件（设备注册代码（zcdm1）、使用单位（sydw）、报检日期（bjrq）、检验日期（jyrq）、报告编号（report_sn）、二维码编号（ewmbh））获取设备信息 
            // 数据结构说明（device_name：设备类型，company_name：使用单位，internal_num：自编号，device_registration_code：设备注册代码，check_type：检验类型，advance_time：检验日期，inspection_conclusion：检验结果，flow_note_name：检验状态，advance_fees：预收金额，report_sn：报告编号）
            System.out.println("测试根据查询条件（设备注册代码（zcdm1）、使用单位（sydw）、报检日期（bjrq）、检验日期（jyrq）、报告编号（report_sn）、二维码编号（ewmbh）和报检年份（bjyear））获取设备信息，正在查询...");  
            String res1 = queryService.queryDevicesNew("sydw", "宏达", "");
			System.out.println("返回结果（设备信息）：" + res1);  
			// 解析返回结果字符串
            Document document1 = DocumentHelper.parseText(res1);
            Element root1 = document1.getRootElement();
            Element set1 = (Element) root1.selectSingleNode("/root");
            Iterator<Element> iterator1 = set1.elementIterator("set");
            List list1 = set1.elements();
            System.out.println("返回结果数量（设备信息）====" + list1.size());
            Element element1 = null;
            while (iterator1.hasNext()) {
                element1 = iterator1.next();
                Iterator iter = element1.attributeIterator();
                while (iter.hasNext()) {
                    Attribute attribute = (Attribute) iter.next();
                    System.out.println(attribute.getName() + ":" + element1.attributeValue(attribute.getName()));                  
                } 
            }
            System.out.println("测试根据查询条件（设备注册代码（zcdm1）、使用单位（sydw）、报检日期（bjrq）、检验日期（jyrq）、报告编号（report_sn）、二维码编号（ewmbh）和报检年份（bjyear））获取设备信息，查询结束。"); 
            
			/*
			// 3、测试根据查询条件（使用单位（sydw）、报检日期（bjrq）、检验日期（jyrq））查询批量报检的报告书信息 
            // 数据结构说明（company_name：使用单位，check_type：检验类型，advance_time：检验日期，inspection_conclusion：检验结果，flow_note_name：检验状态，advance_fees：预收金额）
            System.out.println("测试根据查询条件（使用单位（sydw）、报检日期（bjrq）、检验日期（jyrq））查询批量报检的报告书信息，正在查询...");  
            String res1 = queryService.queryZZJDReports("sydw", "成都成航工业安全系统责任有限公司");
			System.out.println("返回结果（报告书信息）：" + res1);  
			// 解析返回结果字符串
            Document document1 = DocumentHelper.parseText(res1);
            Element root1 = document1.getRootElement();
            Element set1 = (Element) root1.selectSingleNode("/root");
            Iterator<Element> iterator1 = set1.elementIterator("set");
            List list1 = set1.elements();
            System.out.println("返回结果数量（报告书信息）====" + list1.size());
            Element element1 = null;
            while (iterator1.hasNext()) {
                element1 = iterator1.next();
                Iterator iter = element1.attributeIterator();
                while (iter.hasNext()) {
                    Attribute attribute = (Attribute) iter.next();
                    System.out.println(attribute.getName() + ":" + element1.attributeValue(attribute.getName()));                  
                } 
            }
            System.out.println("测试根据查询条件（使用单位、报检日期、检验日期）查询批量报检的报告书信息，查询结束。");
			
			// 3、测试根据查询条件（报告书编号：report_sn）查询报告电子印章信息 		
            System.out.println("测试根据查询条件（报告书编号：report_sn）查询报告电子印章信息 ，正在查询...");  
            String res1 = queryService.queryDZYZPrintInfos("report_sn", "CO-TD201624490");
			System.out.println("返回结果（报告电子印章信息）：" + res1);  
			// 解析返回结果字符串
            Document document1 = DocumentHelper.parseText(res1);
            Element root1 = document1.getRootElement();
            Element set1 = (Element) root1.selectSingleNode("/root");
            Iterator<Element> iterator1 = set1.elementIterator("set");
            List list1 = set1.elements();
            System.out.println("返回结果数量（报告电子印章信息）====" + list1.size());
            Element element1 = null;
            while (iterator1.hasNext()) {
                element1 = iterator1.next();
                Iterator iter = element1.attributeIterator();
                while (iter.hasNext()) {
                    Attribute attribute = (Attribute) iter.next();
                    System.out.println(attribute.getName() + ":" + element1.attributeValue(attribute.getName()));                  
                } 
            }
            System.out.println("测试根据查询条件（报告书编号：report_sn）查询报告电子印章信息，查询结束。");
            
            
			// 4、测试根据查询条件（设备ID：device_id）查询罐车检验报告信息 		
            System.out.println("测试根据查询条件（设备device_id）查询罐车检验报告信息，正在查询中，请稍后...");  
            String res1 = queryService.queryGCInfos("device_id", "402883a052166c5b01521a057f486a76");
			System.out.println("返回结果（罐车检验报告信息）：" + res1);  
			// 解析返回结果字符串
            Document document1 = DocumentHelper.parseText(res1);
            Element root1 = document1.getRootElement();
            Element set1 = (Element) root1.selectSingleNode("/root");
            Iterator<Element> iterator1 = set1.elementIterator("set");
            List list1 = set1.elements();
            System.out.println("返回结果数量（罐车检验报告信息）====" + list1.size());
            Element element1 = null;
            while (iterator1.hasNext()) {
                element1 = iterator1.next();
                Iterator iter = element1.attributeIterator();
                while (iter.hasNext()) {
                    Attribute attribute = (Attribute) iter.next();
                    System.out.println(attribute.getName() + ":" + element1.attributeValue(attribute.getName()));                  
                } 
            }
            System.out.println("测试根据查询条件（设备ID：device_id）查询罐车检验报告信息，查询结束。");*/ 
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
