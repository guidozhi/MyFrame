package com.lsts.relation.bean;

import java.util.Collection;

import javax.persistence.Transient;

import com.lsts.inspection.bean.ReportRecordParse;


/**
 * 报告与原始记录对应关系数据传输对象
 * RelationInfoDTO. 
 * @author GaoYa
 * @date 2018-06-20 下午16:55:00
 */

public class RelationInfoDTO{

	private String id;			// ID
	private String fk_report_id;// 报告模版ID
	private String report_name;	// 报告模版名称
	
	public Collection<ReportRecordParse> reportRecordParse;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFk_report_id() {
		return fk_report_id;
	}

	public void setFk_report_id(String fk_report_id) {
		this.fk_report_id = fk_report_id;
	}

	public String getReport_name() {
		return report_name;
	}

	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	@Transient
	public Collection<ReportRecordParse> getReportRecordParse() {
		return reportRecordParse;
	}

	public void setReportRecordParse(Collection<ReportRecordParse> reportRecordParse) {
		this.reportRecordParse = reportRecordParse;
	}	
}
