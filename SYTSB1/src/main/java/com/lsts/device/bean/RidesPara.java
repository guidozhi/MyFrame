package com.lsts.device.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.khnt.core.crud.bean.BaseEntity;

/*******************************************************************************
 * 
 * 游乐设施參數
 * 
 * @author 肖慈边 2014-1-21
 * 
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true, value = { "deviceDocument" })
@Table(name = "BASE_DEVICE_YLSS_PARA")
public class RidesPara implements BaseEntity {
	
	private static final long serialVersionUID = 1L;
	private String	id	;	//	id
//	private String	fk_tsjc_device_document_id	;	//	设备基本信息ID
	private String	p60001001	;	//	驱动形式
	private String	p60002001	;	//	额定载荷[kg]
	private String	p60002002	;	//	额定速度[m/s]
	private String	p60002003	;	//	倾夹角或坡度[度]
	private String	p60002004	;	//	副速度[m/s]
	private String	p60002005	;	//	驱动主功率[kw]
	private String	p60002006	;	//	电压[v]
	private String	p60002007	;	//	副功率[kw]
	private String	p60002008	;	//	座舱高度[m]
	private String	p60002009	;	//	回转直径[mm]
	private String	p60002010	;	//	轨矩[m]
	private String	p60002011	;	//	轨矩长度[mm]
	private String	p60002012	;	//	水滑梯高度[m]
	private String	p60002013	;	//	游乐池水深[m]
	private String	p60002014	;	//	游乐设施线速度[m/s]
	private String	p60002015	;	//	游乐设施高度[m]
	private String	p60002016	;	//	额定乘客人数[人],碰碰车、赛车座位数
	private String	p_edssxsd	;	//	额定设施线速度[m/s]
	private String	p_clzs	;	//	车辆总数[辆]ma
	private String	p_ccmj	;	//	车场面积[平方米]
	
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public DeviceDocument deviceDocument;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_tsjc_device_document_id")
	public DeviceDocument getDeviceDocument() {
		return deviceDocument;
	}

	public void setDeviceDocument(DeviceDocument deviceDocument) {
		this.deviceDocument = deviceDocument;
	}
//	public String getFk_tsjc_device_document_id() {
//		return fk_tsjc_device_document_id;
//	}
//	public void setFk_tsjc_device_document_id(String fk_tsjc_device_document_id) {
//		this.fk_tsjc_device_document_id = fk_tsjc_device_document_id;
//	}
	public String getP60001001() {
		return p60001001;
	}
	public void setP60001001(String p60001001) {
		this.p60001001 = p60001001;
	}
	public String getP60002001() {
		return p60002001;
	}
	public void setP60002001(String p60002001) {
		this.p60002001 = p60002001;
	}
	public String getP60002002() {
		return p60002002;
	}
	public void setP60002002(String p60002002) {
		this.p60002002 = p60002002;
	}
	public String getP60002003() {
		return p60002003;
	}
	public void setP60002003(String p60002003) {
		this.p60002003 = p60002003;
	}
	public String getP60002004() {
		return p60002004;
	}
	public void setP60002004(String p60002004) {
		this.p60002004 = p60002004;
	}
	public String getP60002005() {
		return p60002005;
	}
	public void setP60002005(String p60002005) {
		this.p60002005 = p60002005;
	}
	public String getP60002006() {
		return p60002006;
	}
	public void setP60002006(String p60002006) {
		this.p60002006 = p60002006;
	}
	public String getP60002007() {
		return p60002007;
	}
	public void setP60002007(String p60002007) {
		this.p60002007 = p60002007;
	}
	public String getP60002008() {
		return p60002008;
	}
	public void setP60002008(String p60002008) {
		this.p60002008 = p60002008;
	}
	public String getP60002009() {
		return p60002009;
	}
	public void setP60002009(String p60002009) {
		this.p60002009 = p60002009;
	}
	public String getP60002010() {
		return p60002010;
	}
	public void setP60002010(String p60002010) {
		this.p60002010 = p60002010;
	}
	public String getP60002011() {
		return p60002011;
	}
	public void setP60002011(String p60002011) {
		this.p60002011 = p60002011;
	}
	public String getP60002012() {
		return p60002012;
	}
	public void setP60002012(String p60002012) {
		this.p60002012 = p60002012;
	}
	public String getP60002013() {
		return p60002013;
	}
	public void setP60002013(String p60002013) {
		this.p60002013 = p60002013;
	}
	public String getP60002014() {
		return p60002014;
	}
	public void setP60002014(String p60002014) {
		this.p60002014 = p60002014;
	}
	public String getP60002015() {
		return p60002015;
	}
	public void setP60002015(String p60002015) {
		this.p60002015 = p60002015;
	}
	public String getP60002016() {
		return p60002016;
	}
	public void setP60002016(String p60002016) {
		this.p60002016 = p60002016;
	}
	public String getP_edssxsd() {
		return p_edssxsd;
	}
	public void setP_edssxsd(String p_edssxsd) {
		this.p_edssxsd = p_edssxsd;
	}
	public String getP_clzs() {
		return p_clzs;
	}
	public void setP_clzs(String p_clzs) {
		this.p_clzs = p_clzs;
	}
	public String getP_ccmj() {
		return p_ccmj;
	}
	public void setP_ccmj(String p_ccmj) {
		this.p_ccmj = p_ccmj;
	}


}
