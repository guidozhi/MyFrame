package com.lsts.device.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.khnt.annotation.Comment;
import com.khnt.core.crud.bean.BaseEntity;
import com.lsts.org.bean.EnterInfo;

/*******************************************************************************
 * 
 * 特种设备
 * 
 * @author 肖慈边 2014-1-08
 * 
 */

@Entity
@Table(name = "base_device_document")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDocument implements BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Comment(comment="使用登记证号")
	private String registration_num; // 使用登记证号
	
	@Comment(comment="注册登记机构")
	private String registration_agencies; // 注册登记机构
	
	@Comment(comment="注册登记日期")
	private Date registration_date; // 注册登记日期
	
	@Comment(comment="设备注册代码")
	private String device_registration_code; // 设备注册代码
	
	@Comment(comment="更新日期")
	private Date update_date; // 更新日期
	
	@Comment(comment="单位内部编号")
	private String internal_num; // 单位内部编号
	
	@Comment(comment="注册登记人员")
	private String registration_op; // 注册登记人员
	
	@Comment(comment="使用单位ID")
	private String fk_company_info_use_id; // 使用单位ID

	@Comment(comment="安全管理部门")
	private String security_management; // 安全管理部门
	
	@Comment(comment="安全管理部门")
	private String security_op; // 安全管理人员
	
	@Comment(comment="安全管理联系电话")
	private String security_tel; // 安全管理联系电话
	
	@Comment(comment="设备类别")
	private String device_sort; // 设备类别
	
	@Comment(comment="设备品种")
	private String device_sort_code; // 设备品种代码
	
	@Comment(comment="设备品种名称")
	private String device_sort_name; // 设备品种
	
	@Comment(comment="设备名称")
	private String device_name; // 设备名称
	
	@Comment(comment="设备型号")
	private String device_model;	// 设备型号
	
	@Comment(comment="制造单位ID")
	private String fk_company_info_make_id; // 制造单位ID
	
	@Comment(comment="制造日期")
	private String make_date; // 制造日期
	
	@Comment(comment="出厂编号")
	private String factory_code; // 出厂编号
	
	@Comment(comment="安装单位ID")
	private String fk_company_info_install_id; // 安装单位ID
	
	@Comment(comment="安装竣工日期")
	private String install_finish_date; // 安装竣工日期
	
	@Comment(comment="投用日期")
	private Date use_date; // 投用日期
	
	@Comment(comment="设备状态")
	private String device_status; // 设备状态(0：告知 1：使用 2：停用 3：报废) 
									// 4：拆除 5：停用 6：报废 7：废弃信息 80：重点监控设备（导入数据）
	
	@Comment(comment="制造单位名称")
	private String make_units_name; // 制造单位名称
	
	@Comment(comment="安装单位名称")
	private String construction_units_name; // 安装单位名称（现场施工单位名称）
	
	@Comment(comment="安装（施工）单位负责人")
	private String construction_user_name; 	// 安装（施工）单位负责人
	
	@Comment(comment="安装单位许可证号")
	private String construction_licence_no; // 安装单位许可证号
	
	@Comment(comment="安装（施工）日期")
	private String construction_date; 		// 安装（施工）日期
	private String created_by; // 创建人
	private Date created_date; // 创建时间
	private String last_upd_by; // 最后更新人
	private Date last_upd_date; // 最后更新时间
	
	@Comment(comment="设备所在地编号")
	private String device_area_code; // 设备所在地编号
	
	@Comment(comment="设备所在地名称")
	private String device_area_name; // 设备所在地名称
	
	@Comment(comment="设备所在街道编号")
	private String device_street_code; // 设备所在街道编号
	
	@Comment(comment="设备所在街道名称")
	private String device_street_name; // 设备所在街道名称
	
	@Comment(comment="检验结论")
	private String inspect_conclusion; // 检验结论
	
	@Comment(comment="下次检验日期")
	private Date inspect_next_date; // 下次检验日期
	
	@Comment(comment="检验日期")
	private Date inspect_date; // 检验日期
	
	@Comment(comment="设备使用地点")
	private String device_use_place; // 设备使用地点
	
	@Comment(comment="设备代码")
	private String device_code; // 设备代码
	
	@Comment(comment="使用单位分厂/分站/楼盘")
	private String use_site; // 使用单位分厂/分站/楼盘
	
	private String note; // 备注
	
	@Comment(comment="检验类别")
	private String check_type; // 检验类别
	
	@Comment(comment="使用单位分厂/分站/楼盘地址")
	private String use_site_address; // 使用单位分厂/分站/楼盘地址
	
	@Comment(comment="制造许可证号")
	private String make_licence_no; // 制造许可证号
	
	@Comment(comment="使用单位")
	private String company_name; // 使用单位
	
	@Comment(comment="使用单位联系人")
	private String com_user_name; // 使用单位联系人
	
	private String fk_maintain_unit_id ;//维保单位ID
	
	@Comment(comment="维保单位名称")
	private String maintain_unit_name; // 维保单位名称
	
	@Comment(comment="维保单位联系人")
	private String maintenance_man; // 维保单位联系人
	
	@Comment(comment="维保单位联系人电话")
	private String maintenance_tel; // 维保单位联系人电话
	
	@Comment(comment="改造单位ID")
	private String fk_reform_unit_id ;	//改造单位ID
	
	@Comment(comment="改造单位名称")
	private String reform_unit_name; 	// 改造单位名称
	
	@Comment(comment="改造日期")
	private String reform_date; 		// 改造日期
	
	@Comment(comment="设备使用场所 ")
	private String use_place;//设备使用场所 （为了给市局传递数据加的字段）
	
	private String id;
	@Comment(comment="使用单位代码")
	private String com_code; //使用单位代码
	
	private String send_status; //同步状态
	private String is_cur_check;	// 当前是否报检（1：未报检 2：当前报检中）
	
	@Comment(comment="设备二维码编号")
	private String device_qr_code;	// 设备二维码编号
	
	// 移动检验业务任务接收信息
	private String receive_user_id; // 接收人ID
	private String receive_user_name; // 接收人姓名
	private Date receive_time; // 接收时间
	
	
	public String getCom_code() {
		return com_code;
	}

	public void setCom_code(String com_code) {
		this.com_code = com_code;
	}

	private EnterInfo enterInfo;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return this.id;
	}

	public void setId(String arg0) {
		this.id = arg0;
	}

	public String getRegistration_num() {
		return registration_num;
	}

	public void setRegistration_num(String registration_num) {
		this.registration_num = registration_num;
	}

	public String getRegistration_agencies() {
		return registration_agencies;
	}

	public void setRegistration_agencies(String registration_agencies) {
		this.registration_agencies = registration_agencies;
	}

	public Date getRegistration_date() {
		return registration_date;
	}

	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}

	public String getDevice_registration_code() {
		return device_registration_code;
	}

	public void setDevice_registration_code(String device_registration_code) {
		this.device_registration_code = device_registration_code;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getInternal_num() {
		return internal_num;
	}

	public void setInternal_num(String internal_num) {
		this.internal_num = internal_num;
	}

	public String getRegistration_op() {
		return registration_op;
	}

	public void setRegistration_op(String registration_op) {
		this.registration_op = registration_op;
	}

	public String getFk_company_info_use_id() {
		return fk_company_info_use_id;
	}

	public void setFk_company_info_use_id(String fk_company_info_use_id) {
		this.fk_company_info_use_id = fk_company_info_use_id;
	}

	public String getSecurity_management() {
		return security_management;
	}

	public void setSecurity_management(String security_management) {
		this.security_management = security_management;
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

	public String getDevice_sort() {
		return device_sort;
	}

	public void setDevice_sort(String device_sort) {
		this.device_sort = device_sort;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public String getFk_company_info_make_id() {
		return fk_company_info_make_id;
	}

	public void setFk_company_info_make_id(String fk_company_info_make_id) {
		this.fk_company_info_make_id = fk_company_info_make_id;
	}

	public String getMake_date() {
		return make_date;
	}

	public void setMake_date(String make_date) {
		this.make_date = make_date;
	}

	public String getFactory_code() {
		return factory_code;
	}

	public void setFactory_code(String factory_code) {
		this.factory_code = factory_code;
	}

	public String getFk_company_info_install_id() {
		return fk_company_info_install_id;
	}

	public void setFk_company_info_install_id(String fk_company_info_install_id) {
		this.fk_company_info_install_id = fk_company_info_install_id;
	}

	public String getInstall_finish_date() {
		return install_finish_date;
	}

	public void setInstall_finish_date(String install_finish_date) {
		this.install_finish_date = install_finish_date;
	}

	public Date getUse_date() {
		return use_date;
	}

	public void setUse_date(Date use_date) {
		this.use_date = use_date;
	}

	public String getDevice_status() {
		return device_status;
	}

	public void setDevice_status(String device_status) {
		this.device_status = device_status;
	}

	public String getDevice_sort_code() {
		return device_sort_code;
	}

	public void setDevice_sort_code(String device_sort_code) {
		this.device_sort_code = device_sort_code;
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

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public String getLast_upd_by() {
		return last_upd_by;
	}

	public void setLast_upd_by(String last_upd_by) {
		this.last_upd_by = last_upd_by;
	}

	public Date getLast_upd_date() {
		return last_upd_date;
	}

	public void setLast_upd_date(Date last_upd_date) {
		this.last_upd_date = last_upd_date;
	}

	public String getDevice_area_code() {
		return device_area_code;
	}

	public void setDevice_area_code(String device_area_code) {
		this.device_area_code = device_area_code;
	}

	public String getInspect_conclusion() {
		return inspect_conclusion;
	}

	public void setInspect_conclusion(String inspect_conclusion) {
		this.inspect_conclusion = inspect_conclusion;
	}

	public Date getInspect_next_date() {
		return inspect_next_date;
	}

	public void setInspect_next_date(Date inspect_next_date) {
		this.inspect_next_date = inspect_next_date;
	}

	public Date getInspect_date() {
		return inspect_date;
	}

	public void setInspect_date(Date inspect_date) {
		this.inspect_date = inspect_date;
	}

	public String getDevice_use_place() {
		return device_use_place;
	}

	public void setDevice_use_place(String device_use_place) {
		this.device_use_place = device_use_place;
	}

	public String getDevice_code() {
		return device_code;
	}

	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	public String getDevice_street_code() {
		return device_street_code;
	}

	public void setDevice_street_code(String device_street_code) {
		this.device_street_code = device_street_code;
	}

	public String getUse_site() {
		return use_site;
	}

	public void setUse_site(String use_site) {
		this.use_site = use_site;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCheck_type() {
		return check_type;
	}

	public void setCheck_type(String check_type) {
		this.check_type = check_type;
	}

	public String getUse_site_address() {
		return use_site_address;
	}

	public void setUse_site_address(String use_site_address) {
		this.use_site_address = use_site_address;
	}

	public String getMake_licence_no() {
		return make_licence_no;
	}

	public void setMake_licence_no(String make_licence_no) {
		this.make_licence_no = make_licence_no;
	}

	// 关联电梯参数对象
	 public Collection<ElevatorPara> elevatorParas;
//	public ElevatorPara elevatorParas;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "deviceDocument", orphanRemoval = true)
	public Collection<ElevatorPara> getElevatorParas() {
		return elevatorParas;
	}

	public void setElevatorParas(Collection<ElevatorPara> elevatorParas) {
		this.elevatorParas = elevatorParas;
	}
	
	// 关联锅炉参数对象
		
	public Collection<BoilerPara> boiler;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "deviceDocument", orphanRemoval = true)
	public Collection<BoilerPara> getBoiler() {
		return boiler;
	}

	public void setBoiler(Collection<BoilerPara> boiler) {
		this.boiler = boiler;
	}
	
	// 关联起重参数对象
	
	public Collection<CranePara> crane;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "deviceDocument", orphanRemoval = true)
	public Collection<CranePara> getCrane() {
		return crane;
	}

	public void setCrane(Collection<CranePara> crane) {
		this.crane = crane;
	}
	
	// 关联压力容器参数对象
	
	public Collection<PressurevesselsPara> pressurevessels;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "deviceDocument", orphanRemoval = true)
	public Collection<PressurevesselsPara> getPressurevessels() {
		return pressurevessels;
	}

	public void setPressurevessels(Collection<PressurevesselsPara> pressurevessels) {
		this.pressurevessels = pressurevessels;
	}
	
	// 关联厂内机动车参数对象
	
	public Collection<EnginePara> engine;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "deviceDocument", orphanRemoval = true)
	public Collection<EnginePara> getEngine() {
		return engine;
	}

	public void setEngine(Collection<EnginePara> engine) {
		this.engine = engine;
	}
	
	// 关联游乐设施参数对象
	
	public Collection<RidesPara> ridesPara;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "deviceDocument", orphanRemoval = true)
	public Collection<RidesPara> getRidesPara() {
		return ridesPara;
	}

	public void setRidesPara(Collection<RidesPara> ridesPara) {
		this.ridesPara = ridesPara;
	}
	
	// 关联安全阀参数对象
	
	public Collection<Accessory> accessory;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "deviceDocument", orphanRemoval = true)
	public Collection<Accessory> getAccessory() {
		return accessory;
	}

	public void setAccessory(Collection<Accessory> accessory) {
		this.accessory = accessory;
	}
	
	// 关联客运索道参数对象
	
	public Collection<CablewayPara> cableway;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "deviceDocument", orphanRemoval = true)
	public Collection<CablewayPara> getCableway() {
		return cableway;
	}

	private void setCableway(Collection<CablewayPara> cableway) {
		this.cableway = cableway;
	}
	
	public void addCableway(CablewayPara cableway){
		if (cableway!=null) {
			if(this.getCableway()!=null){
				this.getCableway().add(cableway);
			}else{
				List<CablewayPara> list = new ArrayList<CablewayPara>();
				list.add(cableway);
				this.cableway = list;
			}
			
	        cableway.setDeviceDocument(this);
		}
   }

	
	/***************************************************************************
	 * bean说明
	 * 
	 * @return
	 */
	public String toString() {
		return "设备名称：" + this.device_name + "(id=" + this.getId() + ")";

	}


	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getFk_maintain_unit_id() {
		return fk_maintain_unit_id;
	}

	public void setFk_maintain_unit_id(String fk_maintain_unit_id) {
		this.fk_maintain_unit_id = fk_maintain_unit_id;
	}

	public String getMaintain_unit_name() {
		return maintain_unit_name;
	}

	public void setMaintain_unit_name(String maintain_unit_name) {
		this.maintain_unit_name = maintain_unit_name;
	}

	public String getMaintenance_man() {
		return maintenance_man;
	}

	public void setMaintenance_man(String maintenance_man) {
		this.maintenance_man = maintenance_man;
	}

	public String getMaintenance_tel() {
		return maintenance_tel;
	}

	public void setMaintenance_tel(String maintenance_tel) {
		this.maintenance_tel = maintenance_tel;
	}

	@Transient
	public EnterInfo getEnterInfo() {
		return enterInfo;
	}

	public void setEnterInfo(EnterInfo enterInfo) {
		this.enterInfo = enterInfo;
	}

	public String getConstruction_licence_no() {
		return construction_licence_no;
	}

	public void setConstruction_licence_no(
			String construction_licence_no) {
		this.construction_licence_no = construction_licence_no;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getSend_status() {
		return send_status;
	}

	public void setSend_status(String send_status) {
		this.send_status = send_status;
	}

	public String getDevice_area_name() {
		return device_area_name;
	}

	public void setDevice_area_name(String device_area_name) {
		this.device_area_name = device_area_name;
	}

	public String getIs_cur_check() {
		return is_cur_check;
	}

	public void setIs_cur_check(String is_cur_check) {
		this.is_cur_check = is_cur_check;
	}

	public String getConstruction_user_name() {
		return construction_user_name;
	}

	public void setConstruction_user_name(String construction_user_name) {
		this.construction_user_name = construction_user_name;
	}

	public String getCom_user_name() {
		return com_user_name;
	}

	public void setCom_user_name(String com_user_name) {
		this.com_user_name = com_user_name;
	}

	public String getDevice_street_name() {
		return device_street_name;
	}

	public void setDevice_street_name(String device_street_name) {
		this.device_street_name = device_street_name;
	}

	public String getDevice_sort_name() {
		return device_sort_name;
	}

	public void setDevice_sort_name(String device_sort_name) {
		this.device_sort_name = device_sort_name;
	}

	@Transient
	public String getReceive_user_id() {
		return receive_user_id;
	}

	public void setReceive_user_id(String receive_user_id) {
		this.receive_user_id = receive_user_id;
	}

	@Transient
	public String getReceive_user_name() {
		return receive_user_name;
	}

	public void setReceive_user_name(String receive_user_name) {
		this.receive_user_name = receive_user_name;
	}

	@Transient
	public Date getReceive_time() {
		return receive_time;
	}

	public void setReceive_time(Date receive_time) {
		this.receive_time = receive_time;
	}

	public String getDevice_qr_code() {
		return device_qr_code;
	}

	public void setDevice_qr_code(String device_qr_code) {
		this.device_qr_code = device_qr_code;
	}
	
	public String getFk_reform_unit_id() {
		return fk_reform_unit_id;
	}

	public void setFk_reform_unit_id(String fk_reform_unit_id) {
		this.fk_reform_unit_id = fk_reform_unit_id;
	}

	public String getReform_unit_name() {
		return reform_unit_name;
	}

	public void setReform_unit_name(String reform_unit_name) {
		this.reform_unit_name = reform_unit_name;
	}

	public String getReform_date() {
		return reform_date;
	}

	public void setReform_date(String reform_date) {
		this.reform_date = reform_date;
	}

	public String getConstruction_date() {
		return construction_date;
	}

	public void setConstruction_date(String construction_date) {
		this.construction_date = construction_date;
	}

	public String getUse_place() {
		return use_place;
	}

	public void setUse_place(String use_place) {
		this.use_place = use_place;
	}

	public static Set<String> bean_to_set(){
	    Set<String> set = new HashSet<String>();
		//set.add("ID");
		set.add("REGISTRATION_NUM");
		//set.add("REGISTRATION_AGENCIES");
		set.add("REGISTRATION_DATE");
		//set.add("DEVICE_REGISTRATION_CODE");
		//set.add("UPDATE_DATE");
		//set.add("INTERNAL_NUM");
		set.add("REGISTRATION_OP");
		//set.add("FK_COMPANY_INFO_USE_ID");
		set.add("SECURITY_MANAGEMENT");
		set.add("SECURITY_OP");
		set.add("SECURITY_TEL");
		//set.add("DEVICE_SORT_CODE");
		//set.add("DEVICE_SORT");
		set.add("DEVICE_CODE");
		set.add("DEVICE_NAME");
		//set.add("FK_COMPANY_INFO_MAKE_ID");
		//set.add("MAKE_UNITS_NAME");
		set.add("MAKE_LICENCE_NO");
		set.add("MAKE_DATE");
		set.add("FACTORY_CODE");
		//set.add("FK_COMPANY_INFO_INSTALL_ID");
		//set.add("CONSTRUCTION_UNITS_NAME");
		set.add("INSTALL_FINISH_DATE");
		set.add("USE_DATE");
		//set.add("DEVICE_STATUS");
		set.add("USE_SITE_ADDRESS");
		//set.add("NOTE");
		//set.add("DEVICE_AREA_CODE");
		set.add("CHECK_TYPE");
		//set.add("INSPECT_DATE");
		set.add("INSPECT_CONCLUSION");
		//set.add("INSPECT_NEXT_DATE");
		set.add("DEVICE_USE_PLACE");
		//set.add("DEVICE_STREET_CODE");
		set.add("USE_SITE");
		//set.add("CREATED_BY");
		//set.add("CREATED_DATE");
		//set.add("LAST_UPD_BY");
		//set.add("LAST_UPD_DATE");
		//set.add("COMPANY_NAME");
		//set.add("FK_MAINTAIN_UNIT_ID");
		//set.add("MAINTAIN_UNIT_NAME");
		set.add("MAINTENANCE_MAN");
		set.add("MAINTENANCE_TEL");
		set.add("CONSTRUCTION_LICENCE_NO");
		set.add("COM_CODE");
		set.add("DEVICE_MODEL");
		//set.add("SEND_STATUS");
		//set.add("OLD_REGISTRATION_CODE");
		set.add("DEVICE_AREA_NAME");
		//set.add("IS_CUR_CHECK");
		set.add("COM_USER_NAME");
		set.add("CONSTRUCTION_USER_NAME");
		//set.add("DEVICE_SORT_OLD");
		//set.add("DEVICE_SORT_CODE_OLD");
		//set.add("DEVICE_NAME_OLD");
		//set.add("UPDATE_STATUS");
		set.add("DEVICE_STREET_NAME");
		set.add("DEVICE_SORT_NAME");
		//set.add("DEVICE_QR_CODE");
		return set;
	}

	public static Set<String> update_bean_to_set(){
	    Set<String> set = new HashSet<String>();
	    set.add("COM_CODE");
	    set.add("DEVICE_QR_CODE");
	    set.add("INTERNAL_NUM");
	    set.add("DEVICE_MODEL");
	    set.add("FACTORY_CODE");	    
		set.add("SECURITY_OP");
		set.add("SECURITY_TEL");
		set.add("DEVICE_USE_PLACE");
		set.add("MAKE_DATE");
		/*set.add("MAKE_UNITS_NAME");
		set.add("MAINTAIN_UNIT_NAME");
		set.add("CONSTRUCTION_UNITS_NAME");*/
		set.add("CONSTRUCTION_LICENCE_NO");
		set.add("REGISTRATION_NUM");
		return set;
	}

	public Set<String> update_columns(){
		Set<String> set = new HashSet<String>();
		set.add("DEVICE_PATTERN");
		set.add("DEVICE_SPEC");
		set.add("DEVICE_MODEL");
		set.add("MAINTENANCE_TEL");
		set.add("MAINTENANCE_MAN");
		set.add("MAINTAIN_UNIT_NAME");
		set.add("USE_SITE");
		set.add("DEVICE_STREET_CODE");
		set.add("DEVICE_USE_PLACE");
		//报告保存的时候不回写检验日期，下次检验日期，检验结论
		//set.add("INSPECT_NEXT_DATE");
		//set.add("INSPECT_CONCLUSION");
		//set.add("INSPECT_DATE");
		set.add("DEVICE_AREA_CODE");
		set.add("USE_SITE_ADDRESS");
		set.add("USE_DATE");
		set.add("INSTALL_FINISH_DATE");
		set.add("CONSTRUCTION_UNITS_NAME");
		set.add("FACTORY_CODE");
		set.add("MAKE_DATE");
		set.add("MAKE_LICENCE_NO");
		set.add("MAKE_UNITS_NAME");
		set.add("DEVICE_NAME");
		set.add("DEVICE_CODE");
		set.add("DEVICE_SORT");
		set.add("DEVICE_SORT_CODE");
		set.add("SECURITY_TEL");
		set.add("SECURITY_OP");
		set.add("SECURITY_MANAGEMENT");
		set.add("REGISTRATION_OP");
		set.add("INTERNAL_NUM");
		set.add("DEVICE_REGISTRATION_CODE");
		set.add("REGISTRATION_DATE");
		set.add("REGISTRATION_AGENCIES");
		set.add("REGISTRATION_NUM");
		set.add("DEVICE_TYPE");
		set.add("DEVICE_TYPE_NAME");
		set.add("TRANSFORM_DATE");
		set.add("AFTER_TRANSFORM_CODE");
		set.add("MAKE_STANDARD");
		set.add("TRANSFORM_UNIT_NAME");
		return set;
	}
}
