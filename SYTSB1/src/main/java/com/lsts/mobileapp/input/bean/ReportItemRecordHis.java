package com.lsts.mobileapp.input.bean;

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
 * 移动端原始记录检验项目参数表
 * ReportItemRecord entity. 
 * @author GaoYa
 * @date 2015-11-12 18:50:00
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "TZSB_REPORT_ITEM_RECORD_HIS")
public class ReportItemRecordHis implements BaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	private String id ;
	private String fkReportId;	// 报告类型ID
	private String itemName;		// 项目指标名称
	private String itemValue;		// 项目指标值
	private String itemType;		// 项目指标类型
	private String fkInspectionInfoId;	// 检验业务信息ID
	private String lastMdyUid;	// 最后修改用户ID
	private String lastMdyUname;  // 最后修改用户姓名
	private Date lastMdyTime;   // 最后修改时间 
	private String dataStatus;		// 数据状态，版本号
	private String pageNo;//页码
	private String image;//图片信息
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="fk_report_id")
	public String getFkReportId() {
		return fkReportId;
	}
	public void setFkReportId(String fkReportId) {
		this.fkReportId = fkReportId;
	}
	@Column(name="item_name")
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@Column(name="item_value")
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	
	@Column(name="item_type")
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	@Column(name="fk_inspection_info_id")
	public String getFkInspectionInfoId() {
		return fkInspectionInfoId;
	}
	public void setFkInspectionInfoId(String fkInspectionInfoId) {
		this.fkInspectionInfoId = fkInspectionInfoId;
	}
	@Column(name="last_mdy_uid")
	public String getLastMdyUid() {
		return lastMdyUid;
	}
	public void setLastMdyUid(String lastMdyUid) {
		this.lastMdyUid = lastMdyUid;
	}
	@Column(name="last_mdy_uname")
	public String getLastMdyUname() {
		return lastMdyUname;
	}
	public void setLastMdyUname(String lastMdyUname) {
		this.lastMdyUname = lastMdyUname;
	}
	@Column(name="last_mdy_time")
	public Date getLastMdyTime() {
		return lastMdyTime;
	}
	public void setLastMdyTime(Date lastMdyTime) {
		this.lastMdyTime = lastMdyTime;
	}
	@Column(name="data_status")
	public String getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	
	@Column(name="page_no")
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
}
