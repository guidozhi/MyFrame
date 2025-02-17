package com.khnt.rtbox.template.constant;

import com.khnt.utils.StringUtil;

/**
 * @author ZQ
 * @version 2016年3月16日 下午10:50:29 类说明
 */
public class RtField {

	public static int length = 500;// 默认字段长度
	public static String format = "yyyy-MM-dd";// 默认格式化
	public static String separator = "__";// 分隔符
	//检验项目及其内容
	//检验结果
	public static String record = "[{id:'符合',text:'符合'},{id:'不符合',text:'不符合'},"
			+ "{id:'资料确认符合',text:'资料确认符合'},{id:'无此项',text:'无此项'}]";
	//检验结论
	public static String conclusion = "[{id:'合格',text:'合格'},{id:'不合格',text:'不合格'},"
			+ "{id:'复检合格',text:'复检合格'},{id:'复检不合格',text:'复检不合格'},{id:'无此项',text:'无此项'}]";
	
	/*public static String jlrecord = "[{id:'√',text:'√'},{id:'×',text:'×'},"
			+ "{id:'∕',text:'∕'},{id:'0',text:'0'}]";
	//检验结论
	public static String jlconclusion = "[{id:'√',text:'√'},{id:'×',text:'×'},"
			+ "{id:'∕',text:'∕'},{id:'0',text:'0'}]";*/
	public static String jlrecord = "[{id:'符合',text:'符合'},{id:'不符合',text:'不符合'},"
			+ "{id:'资料确认符合',text:'资料确认符合'},{id:'无此项',text:'无此项'},{id:'-',text:'-'}]";
	//检验结论
	public static String jlconclusion = "[{id:'合格',text:'合格'},{id:'不合格',text:'不合格'},"
			+ "{id:'复检合格',text:'复检合格'},{id:'复检不合格',text:'复检不合格'},{id:'-',text:'-'}]";
	
	//电梯原始记录
	//校验结果
	public static String dtjlRecordCode ="dt_jl_record";
	//检验结论
	public static String dtjlConclusionCode = "dt_jl_conclusion";
	//电梯报告
	//校验结果
	public static String dtbgRecordCode ="dt_bg_record";
	//检验结论
	public static String dtbgConclusionCode = "dt_bg_conclusion";
	
	//起重机原始记录
	//校验结果
	public static String qzjlRecordCode ="qz_jl_record";
	//检验结论
	public static String qzjlConclusionCode = "qz_jl_conclusion";
	//起重机报告
	//校验结果
	public static String qzbgRecordCode ="qz_bg_record";
	//检验结论
	public static String qzbgConclusionCode = "qz_bg_conclusion";
	
	//厂车原始记录
	//校验结果
	public static String ccjlRecordCode ="cc_jl_record";
	//检验结论
	public static String ccjlConclusionCode = "cc_jl_conclusion";
	//厂车报告
	//校验结果
	public static String ccbgRecordCode ="cc_bg_record";
	//检验结论
	public static String ccbgConclusionCode = "cc_bg_conclusion";
	
;

	/**
	 * 生成ID名称
	 * 
	 * @param item_name
	 * @param code
	 * @return
	 */
	public static String getName(String item_name, String code) {
		if (StringUtil.isEmpty(code) || !code.contains("_")) {
			return item_name;
		}
		return item_name + separator + code;
	}
}
