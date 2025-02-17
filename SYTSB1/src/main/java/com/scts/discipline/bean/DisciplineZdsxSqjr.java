package com.scts.discipline.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.khnt.core.crud.bean.BaseEntity;

/**
 * 重大事项-申请介入
 * @author Administrator
 *
 */
@Entity
@Table(name="TJY2_DISCIPLINE_ZDSX_SQJR")
public class DisciplineZdsxSqjr implements BaseEntity {
	private static final long serialVersionUID = 1L;
	private String id;
	private String sn;//业务流水号
	private String sqr;//申请人
	private String sqr_id;//申请人id
	private String szbm;//所在部门
	private String szbm_id;//所在部门id
	private String jdlb;//监督类别
	private String jdlb_qt;//监督类别---其它
	private String jdzl;//监督种类
	private String jdzl_qt;//监督种类---其它
	private String jdgzsy;//监督工作事由
	private Date jdsj_start;//监督时间
	private Date jdsj_end;//监督时间
	private String bmfzryj;//部门负责人意见	
	private Date bmfzr_time;//部门负责人填写已经日期
	private String bmfgyyj;//部门分管院领导意见
	private Date bmfgy_time;//部门分管院领导填写时间
	private String jjfgyyj;//纪检分管院领导意见
	private Date jjfgy_time;//纪检分管院领导意见--时间
	private String bljg;//办理结果
	private Date bljg_time;//办理结果日期
	private String state;//状态0`未提交`1`部门负责人审核`2`部门分管院领导审核`3`纪检分管院领导审核`4`审核通过`5`审核不通过`6`完结`
	private Date create_time;//创建时间
	private String create_user_id;
	private String create_user_name;
	private String create_org_id;
	private String create_org_name;
	private String cz_user_ids;//纪检检查分配可操作人员id
	private String cz_user_names;//纪检检查分配可操作人员name
	private String fzrxf;//负责人工作安排意见
	
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="CZ_USER_IDS")
	public String getCz_user_ids() {
		return cz_user_ids;
	}
	public void setCz_user_ids(String cz_user_ids) {
		this.cz_user_ids = cz_user_ids;
	}
	@Column(name="CZ_USER_NAMES")
	public String getCz_user_names() {
		return cz_user_names;
	}
	public void setCz_user_names(String cz_user_names) {
		this.cz_user_names = cz_user_names;
	}
	@Column(name="SN")
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	@Column(name="SQR")
	public String getSqr() {
		return sqr;
	}
	public void setSqr(String sqr) {
		this.sqr = sqr;
	}
	@Column(name="SQR_ID")
	public String getSqr_id() {
		return sqr_id;
	}
	public void setSqr_id(String sqr_id) {
		this.sqr_id = sqr_id;
	}
	@Column(name="SZBM")
	public String getSzbm() {
		return szbm;
	}
	public void setSzbm(String szbm) {
		this.szbm = szbm;
	}
	@Column(name="SZBM_ID")
	public String getSzbm_id() {
		return szbm_id;
	}
	public void setSzbm_id(String szbm_id) {
		this.szbm_id = szbm_id;
	}
	@Column(name="JDLB")
	public String getJdlb() {
		return jdlb;
	}
	public void setJdlb(String jdlb) {
		this.jdlb = jdlb;
	}
	@Column(name="JDLB_QT")
	public String getJdlb_qt() {
		return jdlb_qt;
	}
	public void setJdlb_qt(String jdlb_qt) {
		this.jdlb_qt = jdlb_qt;
	}
	@Column(name="JDZL")
	public String getJdzl() {
		return jdzl;
	}
	public void setJdzl(String jdzl) {
		this.jdzl = jdzl;
	}
	@Column(name="JDZL_QT")
	public String getJdzl_qt() {
		return jdzl_qt;
	}
	public void setJdzl_qt(String jdzl_qt) {
		this.jdzl_qt = jdzl_qt;
	}
	@Column(name="JDGZSY")
	public String getJdgzsy() {
		return jdgzsy;
	}
	public void setJdgzsy(String jdgzsy) {
		this.jdgzsy = jdgzsy;
	}
	@Column(name="JDSJ_START")
	public Date getJdsj_start() {
		return jdsj_start;
	}
	public void setJdsj_start(Date jdsj_start) {
		this.jdsj_start = jdsj_start;
	}
	@Column(name="JDSJ_END")
	public Date getJdsj_end() {
		return jdsj_end;
	}
	public void setJdsj_end(Date jdsj_end) {
		this.jdsj_end = jdsj_end;
	}
	@Column(name="BMFZRYJ")
	public String getBmfzryj() {
		return bmfzryj;
	}
	public void setBmfzryj(String bmfzryj) {
		this.bmfzryj = bmfzryj;
	}
	@Column(name="BMFZR_TIME")
	public Date getBmfzr_time() {
		return bmfzr_time;
	}
	public void setBmfzr_time(Date bmfzr_time) {
		this.bmfzr_time = bmfzr_time;
	}
	@Column(name="BMFGYYJ")
	public String getBmfgyyj() {
		return bmfgyyj;
	}
	public void setBmfgyyj(String bmfgyyj) {
		this.bmfgyyj = bmfgyyj;
	}
	@Column(name="BMFGY_TIME")
	public Date getBmfgy_time() {
		return bmfgy_time;
	}
	public void setBmfgy_time(Date bmfgy_time) {
		this.bmfgy_time = bmfgy_time;
	}
	@Column(name="JJFGYYJ")
	public String getJjfgyyj() {
		return jjfgyyj;
	}
	public void setJjfgyyj(String jjfgyyj) {
		this.jjfgyyj = jjfgyyj;
	}
	@Column(name="JJFGY_TIME")
	public Date getJjfgy_time() {
		return jjfgy_time;
	}
	public void setJjfgy_time(Date jjfgy_time) {
		this.jjfgy_time = jjfgy_time;
	}
	@Column(name="BLJG")
	public String getBljg() {
		return bljg;
	}
	public void setBljg(String bljg) {
		this.bljg = bljg;
	}
	@Column(name="CREATE_TIME")
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	@Column(name="CREATE_USER_ID")
	public String getCreate_user_id() {
		return create_user_id;
	}
	public void setCreate_user_id(String create_user_id) {
		this.create_user_id = create_user_id;
	}
	@Column(name="CREATE_USER_NAME")
	public String getCreate_user_name() {
		return create_user_name;
	}
	public void setCreate_user_name(String create_user_name) {
		this.create_user_name = create_user_name;
	}
	@Column(name="CREATE_ORG_ID")
	public String getCreate_org_id() {
		return create_org_id;
	}
	public void setCreate_org_id(String create_org_id) {
		this.create_org_id = create_org_id;
	}
	@Column(name="CREATE_ORG_NAME")
	public String getCreate_org_name() {
		return create_org_name;
	}
	public void setCreate_org_name(String create_org_name) {
		this.create_org_name = create_org_name;
	}
	@Column(name="STATE")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String getFzrxf() {
		return fzrxf;
	}
	public void setFzrxf(String fzrxf) {
		this.fzrxf = fzrxf;
	}
	public Date getBljg_time() {
		return bljg_time;
	}
	public void setBljg_time(Date bljg_time) {
		this.bljg_time = bljg_time;
	}
	
}
