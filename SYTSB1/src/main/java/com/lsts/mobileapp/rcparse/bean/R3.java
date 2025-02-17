package com.lsts.mobileapp.rcparse.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.khnt.core.crud.bean.BaseEntity;

/**
 * 移动端原始记录检验项目解析表
 * ReportRecordParse entity. 
 * @author GaoYa
 * @date 2016-06-27 10:05:00
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "tzsb_report_record_parse")
public class R3 implements BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String id ;	//ID
	private String fkReportId;	//报告书ID
	private String reportName;	//报告名称
	private String recordItemName;	//原始记录项目
	private String reportItemName;	//报告书项目
	private String formule;		//转化公式
	private String formuleType;	//转化类型 1-计算 2 判断 3-预定义规则  4-反转规则 
	private String defaultValue;//转换默认值 如果没有特殊情况，直接用次默认值
	private String recordPageNo;//原始记录所在页码
	private String reportPageNo;//报告所在页码
	private String last_upd_by; // 最后更新人
	private String last_upd_name;	// 最后更新人
	private Date last_upd_date; 	// 最后更新时间
	private String data_status;		// 数据状态（0：正常 98：潜在错误对应项 99：已作废 ）
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="FK_REPORT_ID")
	public String getFkReportId() {
		return fkReportId;
	}
	public void setFkReportId(String fkReportId) {
		this.fkReportId = fkReportId;
	}
	@Column(name="REPORT_NAME")
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	@Column(name="RECORD_ITEM_NAME")
	public String getRecordItemName() {
		return recordItemName;
	}
	public void setRecordItemName(String recordItemName) {
		this.recordItemName = recordItemName;
	}
	@Column(name="REPORT_ITEM_NAME")
	public String getReportItemName() {
		return reportItemName;
	}
	public void setReportItemName(String reportItemName) {
		this.reportItemName = reportItemName;
	}
	@Column(name="FORMULE")
	public String getFormule() {
		return formule;
	}
	public void setFormule(String formule) {
		this.formule = formule;
	}
	@Column(name="FORMULE_TYPE")
	public String getFormuleType() {
		return formuleType;
	}
	public void setFormuleType(String formuleType) {
		this.formuleType = formuleType;
	}
	@Column(name="DEFAULT_VALUE")
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	@Column(name="RECORD_PAGE_NO")
	public String getRecordPageNo() {
		return recordPageNo;
	}
	public void setRecordPageNo(String recordPageNo) {
		this.recordPageNo = recordPageNo;
	}
	@Column(name="REPORT_PAGE_NO")
	public String getReportPageNo() {
		return reportPageNo;
	}
	public void setReportPageNo(String reportPageNo) {
		this.reportPageNo = reportPageNo;
	}
	public String getData_status() {
		return data_status;
	}
	public void setData_status(String data_status) {
		this.data_status = data_status;
	}
	public String getLast_upd_by() {
		return last_upd_by;
	}
	public void setLast_upd_by(String last_upd_by) {
		this.last_upd_by = last_upd_by;
	}
	public String getLast_upd_name() {
		return last_upd_name;
	}
	public void setLast_upd_name(String last_upd_name) {
		this.last_upd_name = last_upd_name;
	}
	public Date getLast_upd_date() {
		return last_upd_date;
	}
	public void setLast_upd_date(Date last_upd_date) {
		this.last_upd_date = last_upd_date;
	}
		
}
