package com.lsts.inspection.bean;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.khnt.annotation.Comment;
import com.khnt.core.crud.bean.BaseEntity;

import util.TS_Util;


/*******************************************************************************
 * 
 * 报检业务数据
 * 
 * @author 肖慈边 2014-2-13
 * 
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true, value = { "inspection" })
@Table(name = "tzsb_inspection_info")
public class InspectionInfo implements BaseEntity{
	
	
	private static final long serialVersionUID = 1L;
	private String	id	;	//	ID
	private String	sn	;	//	业务流水号
//	private String	fk_inspection_id	;	//	报检表ID
	
	@Comment(comment="设备ID")
	private String	fk_tsjc_device_document_id	;	//	设备ID
	
	@Comment(comment="报告类型ID")
	private String	report_type	;	//	报告类型
	private String	fk_flow_index_id;	//	引用流程ID
	private String	flow_note_num	;	//	流程环节编号
	private String	flow_note_id	;	//	流程环节id
	private Double	advance_fees	;	//	预收金额
	
	@Comment(comment="检验日期")
	private Date	advance_time	;	//	预检日期
	private String	data_status	;	//	数据状态（0:表示移动端数据上传初始状态，未启动流程正式使用 1：使用 99：作废）
	private String	ysjl_data_status;	//	原始记录数据状态（0:正常使用 99：作废）
	private String	check_op	;	//	检验联系人
	private String	check_tel	;	//	联系人电话
	@Comment(comment="项目负责人ID")
	private String	item_op_id	;	//	项目负责人ID
	@Comment(comment="项目负责人名字")
	private String	item_op_name	;	//	项目负责人名字
	@Comment(comment="参检人员")
	private String	check_op_id	;	//	参检人员ID
	private String	check_op_name	;	//	参检人员名字	
	private String	check_unit_id	;	//	检验部门ID
	@Comment(comment="录入编制人员")
	private String	enter_op_id	;	//	录入人员ID
	private String	enter_op_name;	//	录入人员姓名
	
	@Comment(comment="录入时间")
	private Date	enter_time	;	//	录入时间
	private Date	enter_time2	;	//	录入日期（实际编制日期，不显示于报告，仅用于数据统计）
	private String	report_item	;	//	报告项目
	private String	ysjl_item	;	//	原始记录项目（页码）
	private String	fee_status	;	//	收费状态(0,默认,1,待收费,2,已收费 3 已入账)数据字典：PAYMENT_STATUS
	
	@Comment(comment="检验结论")
	private String	inspection_conclusion	;	//	检验结论
	
	@Comment(comment="下次检验日期")
	private Date	last_check_time	;	//	下次检验日期
	private String	advance_remark	;	//	预收金额备注
	private String	advance_type	;	//	预收金额类型 0 正常收费 1 协议收费 2 免收费
	//private Date	last_upd_date	;	//	同步到监察系统时间
	
	@Comment(comment="报告书编号")
	private String	report_sn	;	//	报告书编号
	
	@Comment(comment="使用单位名称")
	private String	report_com_name	;	//	报告书上使用单位名称
	
	@Comment(comment="使用单位名地址")
	private String	report_com_address	;	//	报告书上使用单位名地址
	private Double	receivable	;	//	实收金额
	private Integer	discount	;	//	折扣率
	private String	appoint_op_id	;	//	指定操作人ID
	private String	is_report_input	;	//	报告是否已经录入
	private String	apprresult	;	//	评价结果
	private String	is_report_confirm	;	// 原始记录是否已经校核，默认为0（0：未校核 1：校核通过 2：校核未通过）
	private String  report_confirm_remark;	// 报告校核备注
	private Integer	warning_deadline	;	// 预警时限
	private String	is_plan	;	//	是否分配
	private String	invoice_no	;	//	发票号
	private Date	invoice_date	;	//	缴费日期
	private String	enter_unit_id	;	//	录入人员所属部门ID
	private String	is_borrow	;	//	是否预借报告书
	private String	borrow_sn	;	//	预借报告书编号
	private Date    borrow_date;	// 外借日期
	private String	fee_type	;	//	收费方式 ["1","现金收费"],["2","支票"],["4","加急支票"],["3","扣除预付款"],["5","免收费"]
	private String	is_recheck	;	//	是否是复检
	private Date	print_time	;	//	报告第一次打印时间
	private String  is_print_tags;	//  是否已打印标签（0：未打印 1：已打印）
	private String	is_xsqsy	;	//	是否有限速器实验
	private String	is_zzsy	;	//	是否有载重实验
	private String	is_return_print	;	//	退回打印  1 退回打印  
	private String	red	;	//	超期已检
	private String	is_user_defined	;	//	自定义报告 1 是
	private String	file_name	;	//	自定义报告文件名
	@Comment(comment="限速器台数")
	private String	xsqts	;		//	限速器台数
	private String  report_sn_xsq;	//  限速器报告编号	
	private String	fk_hall_no	;	//	大厅报检订单号
	private String  report_confirm_id;		// 预存校核人ID
	private String  report_confirm_name;	// 预存校核人姓名
	private String  confirm_id;		// 实际校核人ID
	private String  confirm_name;	// 实际校核人姓名
	private Date    confirm_Date;	// 实际校核时间
	private String  examine_id;		// 审批人ID
	private String  examine_name;	// 审批人姓名
	private Date    examine_Date;	// 审批时间
	private Date	examine_Date2	;	//	审核日期（实际审核日期，不显示于报告，仅用于数据统计）
	
	@Comment(comment="签发人ID")
	private String  issue_id;	// 签发人ID
	
	@Comment(comment="签发人姓名")
	private String  issue_name;	// 签发人姓名
	
	@Comment(comment="签发时间")
	private Date    issue_Date;	// 签发时间
	private Date    issue_Date2;	// 签发日期（实际签发日期，不显示于报告，仅用于数据统计）
	private String  is_issue;	// 是否已签发（默认否，0：否 1：是）	
	private String  is_flow;	// 是否启动流程
	private String  flow_note_name;	//步骤名称
	private String  is_copy;  		//是否是复制报告
	private String  is_back;		// 是否退回报检
	private String  is_mobile;		// 是否移动检验，默认为0（0：否 1：是）
	private Date    create_time;	// 创建时间
	private Date    last_mdy_time;	// 最后修改时间
	private String  check_status;	// 检验状态，默认为0（0：正常检验 1：中止检验）
	private String  archives_status;// 档案恢复（1为恢复）
	private String  export_pdf;		// 报告pdf导出时间标志
	private String  is_self_sn;		// 是否自编号（0：否 1：是）
										// 如果是自编号，系统不自动生成报告编号，用户可修改；
										// 如果不是自编号，系统自动生成报告编号，用户不可修改；
	private String  pact_id;			// 合同ID
	private String  pact_sn;			// 合同编号
	private String  contract_task_id;	// 检验任务单ID
	private String  contract_task_sn;	// 检验任务单编号
	private String device_qr_code;	// 设备二维码编号
	private String is_cur_error;	// 当前正在纠错中（0：否 1：是）
	private String send_status;		// 数据当前是否已传输（默认否，0：否 1：是）	
	
	private String to_swf;			// 转swf标志 0正在转 1已经转成功
	private String is_auto_issue;	// 是否由系统自动随机分配签发（0：否 1：是）
	
	@Comment(comment="设备注册代码")
	private String device_registration_code; // 设备注册代码
	
	@Comment(comment="单位内部编号")
	private String internal_num; 	// 单位内部编号
	
	@Comment(comment="使用登记证号")
	private String registration_num; 	// 使用登记证号
	
	@Comment(comment="设备型号")
	private String device_model;	// 设备型号
	
	@Comment(comment="出厂编号")
	private String factory_code; 	// 出厂编号
	
	private String make_units_name; // 制造单位名称
	private String make_date; 		// 制造日期
	private String construction_units_name; // 安装/施工单位名称
	private String maintain_unit_name; 		// 维保单位名称
	private String security_op; 			// 安全管理人员
	private String security_tel; 			// 安全管理联系电话
	
	@Comment(comment="设备使用地点")
	private String device_use_place; 		// 设备使用地点
	
	private String  is_receive;			// 前台是否已签收报告（0：未签收 1：已签收）
	private String  receive_user_id;	// 签收人ID
	private String  receive_user_name;	// 签收人姓名
	private Date    receive_date;		// 签收时间
	
	private String  check_category_code;// 检验类型代码（1：制造监检 2：安改维监检 3：定检  4：委托检验......）
	private String  check_category_name;// 检验类型名称
	private String  check_type_code;	// 检验类别代码（起重机监检：1：新装、2：移装、3：改造、4：重大修理）
	private String  check_type_name;	// 检验类别名称（电梯监检：安装、改造、修理）
	private String  is_validation;     // 验证结果 0 验证通过 1验证不通过 2等待上传维保数据 3成都市局验证中  
	private String  is_send_draw_msg;	// 是否已发送报告领取通知（是否已发送报告领取通知（是否已发送报告领取通知（0：否 1：是）
	
	private String scan_upload_by;
	private String scan_upload_by_name;
	private Date scan_upload_time;
	private String  is_print_ysjl;	//  是否已打印原始记录（0：未打印 1：已打印）
	private Date report_end_date;//报告归档日期
	
	private String fk_report_draw_id;//报告领取信息
	private String is_draw;//是否已领
	
	/**
	 * 2019-01-03 添加
	 * 
	 */
	private String is_input;//是否已经录入标记 1 已录入
	private String flow_note_end; //业务是否已经结束  0 未结束  1 已结束 (归档就算结束了)
	private String is_error_correction;//是否正在纠错流程中 0 否 1 是
	/**
	 * 新报告工具添加字段
	 */
	private String save_page_item;
	private String doc_export;// 1 代表已经导出doc

	//原始记录
	private String random_code;//原始记录随机码（用于校验原始记录是否已提交）
	//
	private String recordPageCode;//原始记录页码
	private String recordEnterId;
	private String recordEnterOp;
	private Date recordEnterTime;

	private String recordFlow;//原始记录所在流程（0 录入 1 校核 2 审核 9退回录入 10转移录入）
	private String recordHandleId;
	private String recordHandleOp;
	private String recordConvertStatus;	//原始记录转换报告状态（0：未转换 1：已转换）

	private String recordConfirmId;//原始记录校核人员
	private String recordConfirmOp;
	private Date recordConfirmTime;

	private String receiveStatus;//接收状态 0 未接收 1 已接收

	private String pdfExportPs;// nodeJS 打印标志用于html转pdf
	private String pdfExportAtt;// 报告pdf附件id
	private String twoDimenSional;//二维码附件ID
	private Integer report_page_num;//报告页数
	private Integer record_page_num;//报告页数
	
	private String pdfExportRecord;//  原始记录 html转pdf
	private String pdfExportRecordAtt;// 原始记录 pdf附件id
	
	private String recordVersion;//原始记录版本信息，json格式 (存了大版本和页面的小版本)
	private String reportVersion;//报告版本信息，json格式(存了大版本和页面的小版本)
	
	private String activity_id;//下一步流程的id(Activity.id);
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Inspection inspection;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_inspection_id")
	public Inspection getInspection() {
		return inspection;
	}

	public void setInspection(Inspection inspection) {
		this.inspection = inspection;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	
public String getArchives_status() {
		return archives_status;
	}
	public void setArchives_status(String archives_status) {
		this.archives_status = archives_status;
	}
//	public String getFk_inspection_id() {
//		return fk_inspection_id;
//	}
//	public void setFk_inspection_id(String fk_inspection_id) {
//		this.fk_inspection_id = fk_inspection_id;
//	}
	public String getFk_tsjc_device_document_id() {
		return fk_tsjc_device_document_id;
	}
	public void setFk_tsjc_device_document_id(String fk_tsjc_device_document_id) {
		this.fk_tsjc_device_document_id = fk_tsjc_device_document_id;
	}
	public String getItem_op_name() {
		return item_op_name;
	}
	public void setItem_op_name(String item_op_name) {
		this.item_op_name = item_op_name;
	}
	public String getReport_type() {
		return report_type;
	}
	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}
	public String getFk_flow_index_id() {
		return fk_flow_index_id;
	}
	public void setFk_flow_index_id(String fk_flow_index_id) {
		this.fk_flow_index_id = fk_flow_index_id;
	}
	public String getFlow_note_num() {
		return flow_note_num;
	}
	public void setFlow_note_num(String flow_note_num) {
		this.flow_note_num = flow_note_num;
	}
	public Double getAdvance_fees() {
		return advance_fees;
	}
	public void setAdvance_fees(Double advance_fees) {
		this.advance_fees = advance_fees;
	}
	public Date getAdvance_time() {
		return advance_time;
	}
	public void setAdvance_time(Date advance_time) {
		this.advance_time = advance_time;
	}
	public String getData_status() {
		return data_status;
	}
	public void setData_status(String data_status) {
		this.data_status = data_status;
	}
	public String getCheck_op() {
		return check_op;
	}
	public void setCheck_op(String check_op) {
		this.check_op = check_op;
	}
	public String getCheck_tel() {
		return check_tel;
	}
	public void setCheck_tel(String check_tel) {
		this.check_tel = check_tel;
	}
	public String getIs_flow() {
		return is_flow;
	}
	public void setIs_flow(String is_flow) {
		this.is_flow = is_flow;
	}
	public String getItem_op_id() {
		return item_op_id;
	}
	public void setItem_op_id(String item_op_id) {
		this.item_op_id = item_op_id;
	}
	public String getCheck_op_id() {
		return check_op_id;
	}
	public void setCheck_op_id(String check_op_id) {
		this.check_op_id = check_op_id;
	}
	public String getCheck_unit_id() {
		return check_unit_id;
	}
	public void setCheck_unit_id(String check_unit_id) {
		this.check_unit_id = check_unit_id;
	}
	public String getEnter_op_id() {
		return enter_op_id;
	}
	public void setEnter_op_id(String enter_op_id) {
		this.enter_op_id = enter_op_id;
	}
	public Date getEnter_time() {
		return enter_time;
	}
	public void setEnter_time(Date enter_time) {
		this.enter_time = enter_time;
	}
	public String getReport_item() {
		return report_item;
	}
	public void setReport_item(String report_item) {
		this.report_item = report_item;
	}
	public String getFee_status() {
		return fee_status;
	}
	public void setFee_status(String fee_status) {
		this.fee_status = fee_status;
	}
	public String getInspection_conclusion() {
		return inspection_conclusion;
	}
	public void setInspection_conclusion(String inspection_conclusion) {
		this.inspection_conclusion = inspection_conclusion;
	}
	public Date getLast_check_time() {
		return last_check_time;
	}
	public void setLast_check_time(Date last_check_time) {
		this.last_check_time = last_check_time;
	}
	public String getAdvance_remark() {
		return advance_remark;
	}
	public void setAdvance_remark(String advance_remark) {
		this.advance_remark = advance_remark;
	}
	public String getAdvance_type() {
		return advance_type;
	}
	public void setAdvance_type(String advance_type) {
		this.advance_type = advance_type;
	}
//	public Date getLast_upd_date() {
//		return last_upd_date;
//	}
//	public void setLast_upd_date(Date last_upd_date) {
//		this.last_upd_date = last_upd_date;
//	}
	public String getReport_sn() {
		return report_sn;
	}
	public void setReport_sn(String report_sn) {
		this.report_sn = report_sn;
	}
	public String getReport_com_name() {
		return report_com_name;
	}
	public void setReport_com_name(String report_com_name) {
		this.report_com_name = report_com_name;
	}
	public String getReport_com_address() {
		return report_com_address;
	}
	public void setReport_com_address(String report_com_address) {
		this.report_com_address = report_com_address;
	}
	public Double getReceivable() {
		return receivable;
	}
	public void setReceivable(Double receivable) {
		this.receivable = receivable;
	}
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public String getAppoint_op_id() {
		return appoint_op_id;
	}
	public void setAppoint_op_id(String appoint_op_id) {
		this.appoint_op_id = appoint_op_id;
	}
	public String getIs_report_input() {
		return is_report_input;
	}
	public void setIs_report_input(String is_report_input) {
		this.is_report_input = is_report_input;
	}
	public String getApprresult() {
		return apprresult;
	}
	public void setApprresult(String apprresult) {
		this.apprresult = apprresult;
	}
	public String getCheck_op_name() {
		return check_op_name;
	}
	public void setCheck_op_name(String check_op_name) {
		this.check_op_name = check_op_name;
	}
	public String getIs_report_confirm() {
		return is_report_confirm;
	}
	public void setIs_report_confirm(String is_report_confirm) {
		this.is_report_confirm = is_report_confirm;
	}
	public Integer getWarning_deadline() {
		return warning_deadline;
	}
	public void setWarning_deadline(Integer warning_deadline) {
		this.warning_deadline = warning_deadline;
	}
	public String getInvoice_no() {
		return invoice_no;
	}
	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}
	public Date getInvoice_date() {
		return invoice_date;
	}
	public void setInvoice_date(Date invoice_date) {
		this.invoice_date = invoice_date;
	}
	public String getEnter_unit_id() {
		return enter_unit_id;
	}
	public void setEnter_unit_id(String enter_unit_id) {
		this.enter_unit_id = enter_unit_id;
	}
	public String getIs_borrow() {
		return is_borrow;
	}
	public void setIs_borrow(String is_borrow) {
		this.is_borrow = is_borrow;
	}
	public String getBorrow_sn() {
		return borrow_sn;
	}
	public void setBorrow_sn(String borrow_sn) {
		this.borrow_sn = borrow_sn;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public String getIs_recheck() {
		return is_recheck;
	}
	public void setIs_recheck(String is_recheck) {
		this.is_recheck = is_recheck;
	}
	public Date getPrint_time() {
		return print_time;
	}
	public void setPrint_time(Date print_time) {
		this.print_time = print_time;
	}
	public String getIs_xsqsy() {
		return is_xsqsy;
	}
	public void setIs_xsqsy(String is_xsqsy) {
		this.is_xsqsy = is_xsqsy;
	}
	public String getIs_zzsy() {
		return is_zzsy;
	}
	public void setIs_zzsy(String is_zzsy) {
		this.is_zzsy = is_zzsy;
	}
	public String getIs_return_print() {
		return is_return_print;
	}
	public void setIs_return_print(String is_return_print) {
		this.is_return_print = is_return_print;
	}
	public String getRed() {
		return red;
	}
	public void setRed(String red) {
		this.red = red;
	}
	public String getIs_user_defined() {
		return is_user_defined;
	}
	public void setIs_user_defined(String is_user_defined) {
		this.is_user_defined = is_user_defined;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getXsqts() {
		return xsqts;
	}
	public void setXsqts(String xsqts) {
		this.xsqts = xsqts;
	}
	public String getFk_hall_no() {
		return fk_hall_no;
	}
	public void setFk_hall_no(String fk_hall_no) {
		this.fk_hall_no = fk_hall_no;
	}
	public String getConfirm_id() {
		return confirm_id;
	}
	public void setConfirm_id(String confirm_id) {
		this.confirm_id = confirm_id;
	}
	public String getConfirm_name() {
		return confirm_name;
	}
	public void setConfirm_name(String confirm_name) {
		this.confirm_name = confirm_name;
	}
	public Date getConfirm_Date() {
		return confirm_Date;
	}
	public void setConfirm_Date(Date confirm_Date) {
		this.confirm_Date = confirm_Date;
	}
	public String getExamine_id() {
		return examine_id;
	}
	public void setExamine_id(String examine_id) {
		this.examine_id = examine_id;
	}
	public String getExamine_name() {
		return examine_name;
	}
	public void setExamine_name(String examine_name) {
		this.examine_name = examine_name;
	}
	public Date getExamine_Date() {
		return examine_Date;
	}
	public void setExamine_Date(Date examine_Date) {
		this.examine_Date = examine_Date;
	}
	public String getIssue_id() {
		return issue_id;
	}
	public void setIssue_id(String issue_id) {
		this.issue_id = issue_id;
	}
	public String getIssue_name() {
		return issue_name;
	}
	public void setIssue_name(String issue_name) {
		this.issue_name = issue_name;
	}
	public Date getIssue_Date() {
		return issue_Date;
	}
	public void setIssue_Date(Date issue_Date) {
		this.issue_Date = issue_Date;
	}
	public String getEnter_op_name() {
		return enter_op_name;
	}
	public void setEnter_op_name(String enter_op_name) {
		this.enter_op_name = enter_op_name;
	}
	public String getFlow_note_name() {
		return flow_note_name;
	}
	public void setFlow_note_name(String flow_note_name) {
		this.flow_note_name = flow_note_name;
	}
	public String getIs_plan() {
		return is_plan;
	}
	public void setIs_plan(String is_plan) {
		this.is_plan = is_plan;
	}
	public String getIs_copy() {
		return is_copy;
	}
	public void setIs_copy(String is_copy) {
		this.is_copy = is_copy;
	}
	public String getIs_back() {
		return is_back;
	}
	public void setIs_back(String is_back) {
		this.is_back = is_back;
	}
	public String getIs_print_tags() {
		return is_print_tags;
	}
	public void setIs_print_tags(String is_print_tags) {
		this.is_print_tags = is_print_tags;
	}
	public String getIs_mobile() {
		return is_mobile;
	}
	public void setIs_mobile(String is_mobile) {
		this.is_mobile = is_mobile;
	}
	public String getFlow_note_id() {
		return flow_note_id;
	}
	public void setFlow_note_id(String flow_note_id) {
		this.flow_note_id = flow_note_id;
	}
	public Date getBorrow_date() {
		return borrow_date;
	}
	public void setBorrow_date(Date borrow_date) {
		this.borrow_date = borrow_date;
	}
	public String getCheck_status() {
		return check_status;
	}
	public void setCheck_status(String check_status) {
		this.check_status = check_status;
	}
	public String getExport_pdf() {
		return export_pdf;
	}
	public void setExport_pdf(String export_pdf) {
		this.export_pdf = export_pdf;
	}
	public String getYsjl_item() {
		return ysjl_item;
	}
	public void setYsjl_item(String ysjl_item) {
		this.ysjl_item = ysjl_item;
	}
	public String getIs_self_sn() {
		return is_self_sn;
	}
	public void setIs_self_sn(String is_self_sn) {
		this.is_self_sn = is_self_sn;
	}
	public String getPact_id() {
		return pact_id;
	}
	public void setPact_id(String pact_id) {
		this.pact_id = pact_id;
	}
	public String getPact_sn() {
		return pact_sn;
	}
	public void setPact_sn(String pact_sn) {
		this.pact_sn = pact_sn;
	}
	public String getDevice_qr_code() {
		return device_qr_code;
	}
	public void setDevice_qr_code(String device_qr_code) {
		this.device_qr_code = device_qr_code;
	}
	public String getReport_confirm_remark() {
		return report_confirm_remark;
	}
	public void setReport_confirm_remark(String report_confirm_remark) {
		this.report_confirm_remark = report_confirm_remark;
	}
	public String getReport_confirm_id() {
		return report_confirm_id;
	}
	public void setReport_confirm_id(String report_confirm_id) {
		this.report_confirm_id = report_confirm_id;
	}
	public String getReport_confirm_name() {
		return report_confirm_name;
	}
	public void setReport_confirm_name(String report_confirm_name) {
		this.report_confirm_name = report_confirm_name;
	}
	public String getIs_cur_error() {
		return is_cur_error;
	}
	public void setIs_cur_error(String is_cur_error) {
		this.is_cur_error = is_cur_error;
	}
	public String getYsjl_data_status() {
		return ysjl_data_status;
	}
	public void setYsjl_data_status(String ysjl_data_status) {
		this.ysjl_data_status = ysjl_data_status;
	}
	public String getIs_issue() {
		return is_issue;
	}
	public void setIs_issue(String is_issue) {
		this.is_issue = is_issue;
	}
	public String getSend_status() {
		return send_status;
	}
	public void setSend_status(String send_status) {
		this.send_status = send_status;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getLast_mdy_time() {
		return last_mdy_time;
	}
	public void setLast_mdy_time(Date last_mdy_time) {
		this.last_mdy_time = last_mdy_time;
	}
	public String getTo_swf() {
		return to_swf;
	}
	public void setTo_swf(String to_swf) {
		this.to_swf = to_swf;
	}
	public String getIs_auto_issue() {
		return is_auto_issue;
	}
	public void setIs_auto_issue(String is_auto_issue) {
		this.is_auto_issue = is_auto_issue;
	}
	public String getMake_units_name() {
		return make_units_name;
	}
	public void setMake_units_name(String make_units_name) {
		this.make_units_name = make_units_name;
	}
	public String getConstruction_units_name() {
		return construction_units_name;
	}
	public void setConstruction_units_name(String construction_units_name) {
		this.construction_units_name = construction_units_name;
	}
	public String getMaintain_unit_name() {
		return maintain_unit_name;
	}
	public void setMaintain_unit_name(String maintain_unit_name) {
		this.maintain_unit_name = maintain_unit_name;
	}
	public String getInternal_num() {
		return internal_num;
	}
	public void setInternal_num(String internal_num) {
		this.internal_num = internal_num;
	}
	public String getDevice_model() {
		return device_model;
	}
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}
	public String getFactory_code() {
		return factory_code;
	}
	public void setFactory_code(String factory_code) {
		this.factory_code = factory_code;
	}
	public String getMake_date() {
		return make_date;
	}
	public void setMake_date(String make_date) {
		this.make_date = make_date;
	}
	public String getSecurity_op() {
		return security_op;
	}
	public void setSecurity_op(String security_op) {
		this.security_op = security_op;
	}
	public String getSecurity_tel() {
		return security_tel;
	}
	public void setSecurity_tel(String security_tel) {
		this.security_tel = security_tel;
	}
	public String getDevice_use_place() {
		return device_use_place;
	}
	public void setDevice_use_place(String device_use_place) {
		this.device_use_place = device_use_place;
	}
	public String getIs_receive() {
		return is_receive;
	}
	public void setIs_receive(String is_receive) {
		this.is_receive = is_receive;
	}
	public String getReceive_user_id() {
		return receive_user_id;
	}
	public void setReceive_user_id(String receive_user_id) {
		this.receive_user_id = receive_user_id;
	}
	public String getReceive_user_name() {
		return receive_user_name;
	}
	public void setReceive_user_name(String receive_user_name) {
		this.receive_user_name = receive_user_name;
	}
	public Date getReceive_date() {
		return receive_date;
	}
	public void setReceive_date(Date receive_date) {
		this.receive_date = receive_date;
	}
	public String getCheck_category_code() {
		return check_category_code;
	}
	public void setCheck_category_code(String check_category_code) {
		this.check_category_code = check_category_code;
	}
	public String getCheck_category_name() {
		return check_category_name;
	}
	public void setCheck_category_name(String check_category_name) {
		this.check_category_name = check_category_name;
	}
	public String getCheck_type_code() {
		return check_type_code;
	}
	public void setCheck_type_code(String check_type_code) {
		this.check_type_code = check_type_code;
	}
	public String getCheck_type_name() {
		return check_type_name;
	}
	public void setCheck_type_name(String check_type_name) {
		this.check_type_name = check_type_name;
	}
	public String getRandom_code() {
		return random_code;
	}
	public void setRandom_code(String random_code) {
		this.random_code = random_code;
	}
	public String getIs_validation() {
		return is_validation;
	}
	public void setIs_validation(String is_validation) {
		this.is_validation = is_validation;
	}
	public String getRegistration_num() {
		return registration_num;
	}
	public void setRegistration_num(String registration_num) {
		this.registration_num = registration_num;
	}
	public Date getEnter_time2() {
		return enter_time2;
	}
	public void setEnter_time2(Date enter_time2) {
		this.enter_time2 = enter_time2;
	}
	public Date getExamine_Date2() {
		return examine_Date2;
	}
	public void setExamine_Date2(Date examine_Date2) {
		this.examine_Date2 = examine_Date2;
	}
	public Date getIssue_Date2() {
		return issue_Date2;
	}
	public void setIssue_Date2(Date issue_Date2) {
		this.issue_Date2 = issue_Date2;
	}
	public String getDevice_registration_code() {
		return device_registration_code;
	}
	public void setDevice_registration_code(String device_registration_code) {
		this.device_registration_code = device_registration_code;
	}
	public String getScan_upload_by() {
		return scan_upload_by;
	}
	public void setScan_upload_by(String scan_upload_by) {
		this.scan_upload_by = scan_upload_by;
	}
	public String getScan_upload_by_name() {
		return scan_upload_by_name;
	}
	public void setScan_upload_by_name(String scan_upload_by_name) {
		this.scan_upload_by_name = scan_upload_by_name;
	}
	public Date getScan_upload_time() {
		return scan_upload_time;
	}
	public void setScan_upload_time(Date scan_upload_time) {
		this.scan_upload_time = scan_upload_time;
	}
	public String getIs_send_draw_msg() {
		return is_send_draw_msg;
	}
	public void setIs_send_draw_msg(String is_send_draw_msg) {
		this.is_send_draw_msg = is_send_draw_msg;
	}
	public String getReport_sn_xsq() {
		return report_sn_xsq;
	}
	public void setReport_sn_xsq(String report_sn_xsq) {
		this.report_sn_xsq = report_sn_xsq;
	}
	public String getIs_print_ysjl() {
		return is_print_ysjl;
	}
	public void setIs_print_ysjl(String is_print_ysjl) {
		this.is_print_ysjl = is_print_ysjl;
	}
	public Date getReport_end_date() {
		return report_end_date;
	}
	public void setReport_end_date(Date report_end_date) {
		this.report_end_date = report_end_date;
	}
	public String getContract_task_id() {
		return contract_task_id;
	}
	public void setContract_task_id(String contract_task_id) {
		this.contract_task_id = contract_task_id;
	}
	public String getContract_task_sn() {
		return contract_task_sn;
	}
	public void setContract_task_sn(String contract_task_sn) {
		this.contract_task_sn = contract_task_sn;
	}
	public String getFk_report_draw_id() {
		return fk_report_draw_id;
	}
	public void setFk_report_draw_id(String fk_report_draw_id) {
		this.fk_report_draw_id = fk_report_draw_id;
	}
	public String getIs_draw() {
		return is_draw;
	}
	public void setIs_draw(String is_draw) {
		this.is_draw = is_draw;
	}
	
	//新报告工具
	public String getSave_page_item() {
		return save_page_item;
	}

	public void setSave_page_item(String save_page_item) {
		this.save_page_item = save_page_item;
	}

	public String getDoc_export() {
		return doc_export;
	}

	public void setDoc_export(String doc_export) {
		this.doc_export = doc_export;
	}

	@Column(name="RECORD_PAGE_CODE")
	public String getRecordPageCode() {
		return recordPageCode;
	}

	public void setRecordPageCode(String recordPageCode) {
		this.recordPageCode = recordPageCode;
	}
	@Column(name="RECORD_ENTER_ID")
	public String getRecordEnterId() {
		return recordEnterId;
	}

	public void setRecordEnterId(String recordEnterId) {
		this.recordEnterId = recordEnterId;
	}
	@Column(name="RECORD_ENTER_OP")
	public String getRecordEnterOp() {
		return recordEnterOp;
	}

	public void setRecordEnterOp(String recordEnterOp) {
		this.recordEnterOp = recordEnterOp;
	}
	@Column(name="RECORD_ENTER_TIME")
	public Date getRecordEnterTime() {
		return recordEnterTime;
	}

	public void setRecordEnterTime(Date recordEnterTime) {
		this.recordEnterTime = recordEnterTime;
	}
	@Column(name="RECORD_FLOW")
	public String getRecordFlow() {
		return recordFlow;
	}

	public void setRecordFlow(String recordFlow) {
		this.recordFlow = recordFlow;
	}
	@Column(name="RECORD_HANDLE_ID")
	public String getRecordHandleId() {
		return recordHandleId;
	}

	public void setRecordHandleId(String recordHandleId) {
		this.recordHandleId = recordHandleId;
	}
	@Column(name="RECORD_HANDLE_OP")
	public String getRecordHandleOp() {
		return recordHandleOp;
	}

	public void setRecordHandleOp(String recordHandleOp) {
		this.recordHandleOp = recordHandleOp;
	}
	@Column(name="RECORD_CONFIRM_ID")
	public String getRecordConfirmId() {
		return recordConfirmId;
	}

	public void setRecordConfirmId(String recordConfirmId) {
		this.recordConfirmId = recordConfirmId;
	}
	@Column(name="RECORD_CONFIRM_OP")
	public String getRecordConfirmOp() {
		return recordConfirmOp;
	}

	public void setRecordConfirmOp(String recordConfirmOp) {
		this.recordConfirmOp = recordConfirmOp;
	}

	@Column(name="RECORD_CONFIRM_TIME")
	public Date getRecordConfirmTime() {
		return recordConfirmTime;
	}

	public void setRecordConfirmTime(Date recordConfirmTime) {
		this.recordConfirmTime = recordConfirmTime;
	}

	@Column(name="RECEIVE_STATUS")
	public String getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	@Column(name="RECORD_CONVERT_STATUS")
	public String getRecordConvertStatus() {
		return recordConvertStatus;
	}

	public void setRecordConvertStatus(String recordConvertStatus) {
		this.recordConvertStatus = recordConvertStatus;
	}

	@Column(name="PDF_EXPORT_PS")
	public String getPdfExportPs() {
		return pdfExportPs;
	}

	public void setPdfExportPs(String pdfExportPs) {
		this.pdfExportPs = pdfExportPs;
	}

	@Column(name="PDF_EXPORT_ATT")
	public String getPdfExportAtt() {
		return pdfExportAtt;
	}

	public void setPdfExportAtt(String pdfExportAtt) {
		this.pdfExportAtt = pdfExportAtt;
	}

	@Column(name="TWO_DIMENSIONAL")
	public String getTwoDimenSional() {
		return twoDimenSional;
	}

	public void setTwoDimenSional(String twoDimenSional) {
		this.twoDimenSional = twoDimenSional;
	}

	@Column(name="REPORT_PAGE_NUM")
	public Integer getReport_page_num() {
		return report_page_num;
	}

	public void setReport_page_num(Integer report_page_num) {
		this.report_page_num = report_page_num;
	}
	
	@Column(name="RECORD_PAGE_NUM")
	public Integer getRecord_page_num() {
		return record_page_num;
	}

	public void setRecord_page_num(Integer record_page_num) {
		this.record_page_num = record_page_num;
	}
	
	public String getFlow_note_end() {
		return flow_note_end;
	}
	public void setFlow_note_end(String flow_note_end) {
		this.flow_note_end = flow_note_end;
	}
	
	public String getIs_input() {
		return is_input;
	}
	public void setIs_input(String is_input) {
		this.is_input = is_input;
	}
	
	public String getIs_error_correction() {
		return is_error_correction;
	}
	
	public void setIs_error_correction(String is_error_correction) {
		this.is_error_correction = is_error_correction;
	}
	
	@Column(name="PDF_EXPORT_RECORD")
	public String getPdfExportRecord() {
		return pdfExportRecord;
	}

	public void setPdfExportRecord(String pdfExportRecord) {
		this.pdfExportRecord = pdfExportRecord;
	}

	@Column(name="PDF_EXPORT_RECORD_ATT")
	public String getPdfExportRecordAtt() {
		return pdfExportRecordAtt;
	}
	
	public void setPdfExportRecordAtt(String pdfExportRecordAtt) {
		 this.pdfExportRecordAtt = pdfExportRecordAtt;
	}
	
	@Column(name="RECORD_VERSION")
	public String getRecordVersion() {
		return recordVersion;
	}

	public void setRecordVersion(String recordVersion) {
		this.recordVersion = recordVersion;
	}
	@Column(name="REPORT_VERSION")
	public String getReportVersion() {
		return reportVersion;
	}

	public void setReportVersion(String reportVersion) {
		this.reportVersion = reportVersion;
	}
	
	public Map<String,String> bean_to_Map() {
        Map<String,String> map = new HashMap<String,String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        map.put("ID" , id);
        map.put("SN" , sn);
        map.put("CHECK_OP" , check_op);
        map.put("CHECK_TEL" , check_tel);
        map.put("REPORT_SN" , report_sn);
        map.put("COM_NAME" , report_com_name);  //注意，KEY值是COM_NAME,对应报告
        map.put("COM_ADDRESS" , report_com_address); //注意，KEY值是COM_ADDRESS,对应报告
        map.put("CHECK_UNIT_ID", check_unit_id);
        //项目负责人
        map.put("INSPECTION_ITEM_ID", item_op_id);
        map.put("INSPECTION_ITEM_STR", item_op_name);
        //检验人员
//        map.put("INSPECTION_OP_ID", checkOpId);
//        map.put("INSPECTION_OP_STR", checkOpName);
        map.put("INSPECTION_OP_ID", TS_Util.mergeCheckOps(item_op_id, check_op_id));
        map.put("INSPECTION_OP_STR", TS_Util.mergeCheckOps(item_op_name, check_op_name));
        //录入人员(编制人员)
        map.put("INSPECTION_ENTER_ID", enter_op_id);
        map.put("INSPECTION_ENTER_STR", enter_op_name);
        if(enter_time!=null){
        	map.put("ENTER_TIME" , format.format(enter_time));
        }
        //审核人员
        map.put("INSPECTION_AUDIT_ID",examine_id );
        map.put("INSPECTION_AUDIT_STR", examine_name);
        if(examine_Date!=null){
        	map.put("AUDIT_TIME" , format.format(examine_Date));
        }
        //签发人员
        map.put("INSPECTION_SIGN_ID", issue_id);
        map.put("INSPECTION_SIGN_STR", issue_name);
        if(issue_Date!=null){
        	map.put("SIGN_TIME" , format.format(issue_Date));
        	map.put("SIGN_DATE_Y" , format.format(issue_Date).split("-")[0]);
        	map.put("SIGN_DATE_M" , format.format(issue_Date).split("-")[1]);
        	map.put("SIGN_DATE_D" , format.format(issue_Date).split("-")[2]);
        }
        
        if(advance_time!=null){
        	map.put("INSPECT_DATE" , format.format(advance_time));
        	map.put("INSPECT_DATE_Y" , format.format(advance_time).split("-")[0]);
        	map.put("INSPECT_DATE_M" , format.format(advance_time).split("-")[1]);
        	map.put("INSPECT_DATE_D" , format.format(advance_time).split("-")[2]);
        }
        if(last_check_time!=null){
        	map.put("INSPECT_NEXT_DATE" , format.format(last_check_time));
        	map.put("INSPECT_NEXT_DATE_Y" , format.format(last_check_time).split("-")[0]);
        	map.put("INSPECT_NEXT_DATE_M" , format.format(last_check_time).split("-")[1]);
        	map.put("INSPECT_NEXT_DATE_D" , format.format(last_check_time).split("-")[2]);
        }
        
        map.put("INSPECT_CONCLUSION" , inspection_conclusion);

        return map ;
	}
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	
	
}
