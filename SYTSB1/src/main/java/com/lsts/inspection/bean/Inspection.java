package com.lsts.inspection.bean;


import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.khnt.core.crud.bean.BaseEntity;


/*******************************************************************************
 * 
 * 科室报检数据
 * 
 * @author 肖慈边 2014-1-26
 * 
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "tzsb_inspection")
public class Inspection implements BaseEntity{
	
	private static final long serialVersionUID = 1L;
	private String	id	;	//	ID
	private String	accept_no	;	//	受理单号
	private String	check_type	;	//	检验类别
	private String	fk_unit_id	;	//	单位ID
	private String	is_merger	;	//	是否组合出具一份报告
	private String	estimated_total_fees	;	//	预计总收费
	private String	remark	;	//	备注
	private Date	inspection_time	;	//	报检日期
	private String	enter_op	;	//	录入人员
	private String	data_status	;	//	数据状态
	private String	accepted_type	;	//	报检类型（1---部门受理，2----报检大厅受理）
	private String	fk_app_id	;	//	告知书ID
	private String	send_status	;	//	
	private String	fk_inc_id	;	//	
	private String	device_num	;	//	报检设备数量
	private String	sub_dep	;	//	提交科室
	private String	com_name	;
	private String	com_address	;
	private String	fk_hall_para_id	;

	private String hall_no;//冗余大厅报检编号
	
	//加入
	private String leader_id;//项目负责人
	private String leader;
	
	private String enter_op_id; //	录入人员
	
	private String inspect_op_id;//参见人员
	private String inspect_op;
	
	private String device_type; //设备类型
	
	private String fk_report_id; //报告类型
	private String report_name;
	
	private String contact;//联系人
	private String contact_phone;//联系电话
	
	private Date inspect_date;//检验日期

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Collection<InspectionInfo> inspectionInfo;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "inspection", orphanRemoval = true)
	public Collection<InspectionInfo> getInspectionInfo() {
		return inspectionInfo;
	}

	public void setInspectionInfo(Collection<InspectionInfo> inspectionInfo) {
		this.inspectionInfo = inspectionInfo;
	}
	
	
	
	public String getAccept_no() {
		return accept_no;
	}
	public void setAccept_no(String accept_no) {
		this.accept_no = accept_no;
	}
	public String getCheck_type() {
		return check_type;
	}
	public void setCheck_type(String check_type) {
		this.check_type = check_type;
	}
	public String getFk_unit_id() {
		return fk_unit_id;
	}
	public void setFk_unit_id(String fk_unit_id) {
		this.fk_unit_id = fk_unit_id;
	}
	public String getIs_merger() {
		return is_merger;
	}
	public void setIs_merger(String is_merger) {
		this.is_merger = is_merger;
	}
	public String getEstimated_total_fees() {
		return estimated_total_fees;
	}
	public void setEstimated_total_fees(String estimated_total_fees) {
		this.estimated_total_fees = estimated_total_fees;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getInspection_time() {
		return inspection_time;
	}
	public void setInspection_time(Date inspection_time) {
		this.inspection_time = inspection_time;
	}
	public String getEnter_op() {
		return enter_op;
	}
	public void setEnter_op(String enter_op) {
		this.enter_op = enter_op;
	}
	public String getData_status() {
		return data_status;
	}
	public void setData_status(String data_status) {
		this.data_status = data_status;
	}
	public String getAccepted_type() {
		return accepted_type;
	}
	public void setAccepted_type(String accepted_type) {
		this.accepted_type = accepted_type;
	}
	public String getFk_app_id() {
		return fk_app_id;
	}
	public void setFk_app_id(String fk_app_id) {
		this.fk_app_id = fk_app_id;
	}
	public String getSend_status() {
		return send_status;
	}
	public String getFk_hall_para_id() {
		return fk_hall_para_id;
	}
	public void setFk_hall_para_id(String fk_hall_para_id) {
		this.fk_hall_para_id = fk_hall_para_id;
	}
	public void setSend_status(String send_status) {
		this.send_status = send_status;
	}
	public String getFk_inc_id() {
		return fk_inc_id;
	}
	public void setFk_inc_id(String fk_inc_id) {
		this.fk_inc_id = fk_inc_id;
	}
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}
	public String getSub_dep() {
		return sub_dep;
	}
	public void setSub_dep(String sub_dep) {
		this.sub_dep = sub_dep;
	}
	public String getCom_name() {
		return com_name;
	}
	public void setCom_name(String com_name) {
		this.com_name = com_name;
	}
	public String getCom_address() {
		return com_address;
	}
	public void setCom_address(String com_address) {
		this.com_address = com_address;
	}

	public String getHall_no() {
		return hall_no;
	}
	public void setHall_no(String hall_no) {
		this.hall_no = hall_no;
	}
	public String getLeader_id() {
		return leader_id;
	}
	public void setLeader_id(String leader_id) {
		this.leader_id = leader_id;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public String getEnter_op_id() {
		return enter_op_id;
	}
	public void setEnter_op_id(String enter_op_id) {
		this.enter_op_id = enter_op_id;
	}
	public String getInspect_op_id() {
		return inspect_op_id;
	}
	public void setInspect_op_id(String inspect_op_id) {
		this.inspect_op_id = inspect_op_id;
	}
	public String getInspect_op() {
		return inspect_op;
	}
	public void setInspect_op(String inspect_op) {
		this.inspect_op = inspect_op;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
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
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getContact_phone() {
		return contact_phone;
	}
	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}
	public Date getInspect_date() {
		return inspect_date;
	}
	public void setInspect_date(Date inspect_date) {
		this.inspect_date = inspect_date;
	}
	
}
