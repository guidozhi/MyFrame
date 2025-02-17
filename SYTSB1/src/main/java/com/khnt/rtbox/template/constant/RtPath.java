package com.khnt.rtbox.template.constant;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.khnt.base.Factory;
import com.khnt.rtbox.config.bean.RtPersonDir;

/**
 * @author ZQ
 * @version 2016年3月15日 上午11:42:07 类说明
 */
public class RtPath {
	public static WebApplicationContext aplicationContext = ContextLoader.getCurrentWebApplicationContext();
	public static Properties prop =  new  Properties(); 
	  static  { 
		  try  { 
			InputStream in = new BufferedInputStream(new FileInputStream(new File(Factory.getWebRoot()+"WEB-INF/config/rtbox.properties")));

				prop.load(in);    
	        }  catch  (IOException e) {    
	            e.printStackTrace();    
	        }    
	    }
	public static String saveDB = getPropetyValue("saveDB","0");// 模版文件保存方式：1数据库、0磁盘
	//模板磁盘文件根目录
	public static String projectPath = getPropetyValue("PROJECT_PATH",Factory.getWebRoot());
	
	public static String basePath = getPropetyValue("basePath","D:/rtbox/");
	
	public static String imagePath = "".equals(getPropetyValue("imagePath",""))?Factory.getWebRoot()+ "/upload"
			:getPropetyValue("imagePath","");

	public static String templeteDocPath = getPropetyValue("templeteDocPath","templete/word/");// 模板存放地
	public static String templeteXmlPath = getPropetyValue("templeteXmlPath","templete/xml/");// 模板存放地
	public static String outputDocPath = getPropetyValue("outputDocPath","output/word/");// word输出地
	public static String outputHtmlPath = getPropetyValue("outputHtmlPath","output/html/");// html输出地

	public static String outputXmlPath = getPropetyValue("outputXmlPath","output/xml/");// XML输出地

	public static String outputExportPath = getPropetyValue("outputExportPath","output/export/");// 用户级word输出地

	public static String tplPageDir = getPropetyValue("tplPageDir","rtbox/app/templates/");
	public static String tplRecordPageDir = getPropetyValue("tplRecordPageDir","rtbox/app/recordTemplates/");
	public static String tplXmlPath = getPropetyValue("tplXmlPath","rtbox/app/xml/");// XML模板，代码.xml，完整版
	
	public static Integer MINVERSION = 0;// 初始化版本号

	// 规则：指定TPLXMLPATH+报表代码目录+报表代码+后缀名，获取完整版XML的超链接模板
	public static String getTplRelsPathFull(String rtCode) {
		String tplRelsPath = getTplXmlPath(rtCode) + rtCode + ".xml.rels";
		return tplRelsPath;
	}

	// 规则：指定TPLXMLPATH+报表代码目录+报表代码+后缀名，获取完整版XML模板
	public static String getTplXmlPathFull(String rtCode) {
		String tplXmlPath = getTplXmlPath(rtCode) + rtCode + ".xml";
		return tplXmlPath;
	}

	// 规则：指定TPLXMLPATH+报表代码目录+报表代码+后缀名，获取完整版XML模板
	public static String getTplXmlPath(String rtCode) {
		String tplXmlPath = RtPath.tplXmlPath + rtCode + "/";
		return tplXmlPath;
	}

	// 规则：指定OUTPUTXMLPATH+报表代码目录+业务ID目录+版本号+后缀名
	public static String getXmlPath(RtPersonDir rpd) {
		return RtPath.outputXmlPath + rpd.getRtCode() + "/" + rpd.getFkBusinessId() + "_" + rpd.getVersion() + ".xml";
	}

	// 规则：指定outputExportPath+报表代码目录+业务ID目录+版本号+后缀名
	public static String getExportPath(RtPersonDir rpd) {
		return RtPath.outputExportPath + rpd.getRtCode() + "/" + rpd.getFkBusinessId() + "_" + rpd.getVersion() + ".docx";
	}

	// 规则：指定outputExportPath+报表代码目录+业务ID目录+版本号+后缀名
	public static String getPreviewSinglePath(RtPersonDir rpd) {
		return RtPath.outputExportPath + rpd.getRtCode() + "/preview/" + rpd.getFkBusinessId() + "_" + rpd.getVersion() + ".docx";
	}
	
	public static String getPreviewSingleDocPath(RtPersonDir rpd,String code) {
		return RtPath.outputExportPath + rpd.getRtCode() +"/" + rpd.getFkBusinessId() + "_" + rpd.getVersion() +"_"+code+ ".docx";
	}
	
	/**
	 * 取得rtbox里面的常量配置值
	 * author pingZhou
	 * @param name 常量名字
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String getPropetyValue(String name,String defaultValue){
		String value = "";
		
		try {
			value = prop.getProperty(name).trim();  
		} catch (Exception e) {
			if(defaultValue!=null){
				value = defaultValue;
			}
			
		}
		if("".equals(value)&&defaultValue!=null){
			value = defaultValue;
		}
		
		return value;
	}
}
