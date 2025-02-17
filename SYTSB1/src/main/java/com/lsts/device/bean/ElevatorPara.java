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

import com.khnt.annotation.Comment;
import com.khnt.core.crud.bean.BaseEntity;

/*******************************************************************************
 * 
 * 電梯參數
 * 
 * @author 肖慈边 2014-1-09
 * 
 */

@Entity
@JsonIgnoreProperties(ignoreUnknown = true, value = { "deviceDocument" })
@Table(name = "base_device_elevator_para")
public class ElevatorPara implements BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	// private String fk_tsjc_device_document_id ; // 设备基本信息ID
	@Comment(comment="控制方式")
	private String p30001001; // 控制方式
	@Comment(comment="拖动方式")
	private String p30001002; // 拖动方式
	@Comment(comment="开门方式")
	private String p30001003; // 开门方式
	@Comment(comment="工作环境")
	private String p30001004; // 工作环境
	@Comment(comment="补偿方式")
	private String p30001005; // 补偿方式
	@Comment(comment="曳引机型式")
	private String p30001006; // 曳引机型式
	@Comment(comment="绳槽型式")
	private String p30001007; // 绳槽型式
	@Comment(comment="安全钳型式")
	private String p30001008; // 安全钳型式
	@Comment(comment="缓冲器型式")
	private String p30001009; // 缓冲器型式
	@Comment(comment="限速器型式")
	private String p30001010; // 限速器型式
	@Comment(comment="轿厢导轨型式")
	private String p30001011; // 轿厢导轨型式
	@Comment(comment="对重导轨型式")
	private String p30001012; // 对重导轨型式
	@Comment(comment="顶升型式")
	private String p30001013; // 顶升型式
	@Comment(comment="油缸形式")
	private String p30001014; // 油缸形式
	@Comment(comment="设计规范")
	private String p30001015; // 设计规范
	@Comment(comment="制造规范")
	private String p30001016; // 制造规范
	@Comment(comment="设备新旧状况")
	private String p30001017; // 设备新旧状况
	@Comment(comment="监检形式")
	private String p30001018; // 监检形式
	@Comment(comment="扶梯制动器型式")
	private String p30001019; // 扶梯制动器型式
	@Comment(comment="扶梯附加制动器型式")
	private String p30001020; // 扶梯附加制动器型式
	@Comment(comment="扶梯站立部件类型")
	private String p30001021; // 扶梯站立部件类型
	@Comment(comment="扶梯扶手栏板型式")
	private String p30001022; // 扶梯扶手栏板型式
	@Comment(comment="扶梯梯级型式")
	private String p30001023; // 扶梯梯级型式
	@Comment(comment="扶梯倾斜角")
	private String p30002001; // 扶梯倾斜角
	@Comment(comment="梯级宽度")
	private String p30002002; // 梯级宽度
	@Comment(comment="额定速度[m/s]")
	private String p30002003; // 额定速度[m/s]
	@Comment(comment="额定载荷[kg]")
	private String p30002004; // 额定载荷[kg]
	@Comment(comment="电梯层站[层] 费用计算使用字段")
	private String p30002005; // 电梯层站[层] 费用计算使用字段
	@Comment(comment="提升高度[m]")
	private String p30002006; // 提升高度[m]
	@Comment(comment="曳引机型号")
	private String p30003001; // 曳引机型号
	@Comment(comment="曳引机出厂编号")
	private String p30003002; // 曳引机出厂编号
	@Comment(comment="电动机型号")
	private String p30003003; // 电动机型号
	@Comment(comment="电动机出厂编号")
	private String p30003004; // 电动机出厂编号
	@Comment(comment="控制屏型号")
	private String p30003005; // 控制屏型号
	@Comment(comment="控制屏出厂编号")
	private String p30003006; // 控制屏出厂编号
	@Comment(comment="轿厢限速器型号")
	private String p30003007; // 轿厢限速器型号
	@Comment(comment="轿厢限速器出厂编号")
	private String p30003008; // 轿厢限速器出厂编号
	@Comment(comment="对重限速器型号")
	private String p30003009; // 对重限速器型号
	@Comment(comment="对重限速器出厂编号")
	private String p30003010; // 对重限速器出厂编号
	@Comment(comment="曳引绳直径")
	private String p_yyszj; // 曳引绳直径
	@Comment(comment="曳引绳数")
	private String p_yyss; // 曳引绳数
	@Comment(comment="曳引比")
	private String p_yyb; // 曳引比
	@Comment(comment="电动机功率")
	private String p_ddjgl; // 电动机功率
	@Comment(comment="转速")
	private String p_zs; // 转速
	@Comment(comment="额定电流")
	private String p_eddl; // 额定电流
	@Comment(comment="对重块数量")
	private String p_dcksl; // 对重块数量
	@Comment(comment="限速器动作速度")
	private String p_xsqdzsd; // 限速器动作速度
	@Comment(comment="电梯门数")
	private String p_dtms; // 电梯门数
	@Comment(comment="电梯站数")
	private String p_dtzs; // 电梯站数
	@Comment(comment="扶梯宽度")
	private String p_ftkd; // 扶梯宽度
	@Comment(comment="轿厢长")
	private String p_jxc; // 轿厢长
	@Comment(comment="轿厢宽")
	private String p_jxk; // 轿厢宽
	@Comment(comment="轿厢高")
	private String p_jxg; // 轿厢高
	@Comment(comment="油缸压力")
	private String p_ygly; // 油缸压力
	@Comment(comment="油缸数量")
	private String p_ygsl; // 油缸数量
	@Comment(comment="油缸型式")
	private String p_ygxs; // 油缸型式
	@Comment(comment="安全钳型号")
	private String p_acqxh; // 安全钳型号
	@Comment(comment="安全钳编号")
	private String p_acqbh; // 安全钳编号
	@Comment(comment="缓冲器型号")
	private String p_hcqxh; // 缓冲器型号
	@Comment(comment="缓冲器编号")
	private String p_hcqbh; // 缓冲器编号
	@Comment(comment="油压泵流量")
	private String p_yybll; // 油压泵流量
	@Comment(comment="液压泵功率")
	private String p_yybgl; // 液压泵功率
	@Comment(comment="上行额定速度")
	private String p_sxydsd; // 上行额定速度
	@Comment(comment="下行额定速度")
	private String p_xxydsd; // 下行额定速度
	@Comment(comment="曳引轮引径")
	private String p_yyryj; // 曳引轮引径
	@Comment(comment="速比")
	private String p_sb; // 速比
	@Comment(comment="开门方向")
	private String p_kmfx; // 开门方向
	@Comment(comment="顶层高度")
	private String p_dcgd; // 顶层高度
	@Comment(comment="底坑深度")
	private String p_dksd; // 底坑深度
	@Comment(comment="实测电气动作速度")
	private String p_dqdzsd; // 实测电气动作速度
	@Comment(comment="实测机械动作速度")
	private String p_jjdzsd; // 实测机械动作速度
	@Comment(comment="使用区段长度")
	private String p_syqdcd; // 使用区段长度
	@Comment(comment="附加制动器")
	private String p_fjzdq; // 附加制动器
	@Comment(comment="公共交通型")
	private String p_gdjtx; // 公共交通型
	@Comment(comment="电压")
	private String p_dy; // 电压
	@Comment(comment="转差率")
	private String p_zcl; // 转差率
	@Comment(comment="运行方法")
	private String p_yxff; // 运行方法
	@Comment(comment="横移速度")
	private String p_hysd; // 横移速度
	@Comment(comment="泊位数量")
	private String p_bwsl; // 泊位数量
	@Comment(comment="电梯驱动方式")
	private String cp_by1; // 电梯驱动方式
	@Comment(comment="扶梯工作环境")
	private String cp_by2; // 扶梯工作环境
	@Comment(comment="整机防爆标志")
	private String cp_by3; // 整机防爆标志
	@Comment(comment="燃爆物质")
	private String cp_by4; // 燃爆物质
	@Comment(comment="整机防爆合格证号")
	private String cp_by5; // 整机防爆合格证号
	@Comment(comment="区域防爆等级")
	private String cp_by6; // 区域防爆等级
	@Comment(comment="CP_BY7")
	private String cp_by7; // CP_BY7
	@Comment(comment="CP_BY8")
	private String cp_by8; // CP_BY8
	@Comment(comment="CP_BY9")
	private String cp_by9; // CP_BY9
	@Comment(comment="CP_BY10")
	private String cp_by10; // CP_BY10
	@Comment(comment="CBZ")
	private String cbz; // CBZ
	@Comment(comment="CCDBZ")
	private String ccdbz; // CCDBZ
	@Comment(comment="CCJRBM")
	private String ccjrbm; // CCJRBM
	@Comment(comment="CCJRMC")
	private String ccjrmc; // CCJRMC
	@Comment(comment="CCJRQ")
	private String ccjrq; // CCJRQ
	@Comment(comment="CGXRQ")
	private String cgxrq; // CGXRQ
	@Comment(comment="名义宽度")
	private String mykd;	// 名义宽度
	@Comment(comment="名义速度")
	private String mysd;	// 名义速度
	@Comment(comment="输送能力")
	private String ssnl;	// 输送能力
	

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return this.id;
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

	// public String getFk_tsjc_device_document_id() {
	// return fk_tsjc_device_document_id;
	// }
	//
	// public void setFk_tsjc_device_document_id(String
	// fk_tsjc_device_document_id) {
	// this.fk_tsjc_device_document_id = fk_tsjc_device_document_id;
	// }

	public String getP30001001() {
		return p30001001;
	}

	public void setP30001001(String p30001001) {
		this.p30001001 = p30001001;
	}

	public String getP30001002() {
		return p30001002;
	}

	public void setP30001002(String p30001002) {
		this.p30001002 = p30001002;
	}

	public String getP30001003() {
		return p30001003;
	}

	public void setP30001003(String p30001003) {
		this.p30001003 = p30001003;
	}

	public String getP30001004() {
		return p30001004;
	}

	public void setP30001004(String p30001004) {
		this.p30001004 = p30001004;
	}

	public String getP30001005() {
		return p30001005;
	}

	public void setP30001005(String p30001005) {
		this.p30001005 = p30001005;
	}

	public String getP30001006() {
		return p30001006;
	}

	public void setP30001006(String p30001006) {
		this.p30001006 = p30001006;
	}

	public String getP30001007() {
		return p30001007;
	}

	public void setP30001007(String p30001007) {
		this.p30001007 = p30001007;
	}

	public String getP30001008() {
		return p30001008;
	}

	public void setP30001008(String p30001008) {
		this.p30001008 = p30001008;
	}

	public String getP30001009() {
		return p30001009;
	}

	public void setP30001009(String p30001009) {
		this.p30001009 = p30001009;
	}

	public String getP30001010() {
		return p30001010;
	}

	public void setP30001010(String p30001010) {
		this.p30001010 = p30001010;
	}

	public String getP30001011() {
		return p30001011;
	}

	public void setP30001011(String p30001011) {
		this.p30001011 = p30001011;
	}

	public String getP30001012() {
		return p30001012;
	}

	public void setP30001012(String p30001012) {
		this.p30001012 = p30001012;
	}

	public String getP30001013() {
		return p30001013;
	}

	public void setP30001013(String p30001013) {
		this.p30001013 = p30001013;
	}

	public String getP30001014() {
		return p30001014;
	}

	public void setP30001014(String p30001014) {
		this.p30001014 = p30001014;
	}

	public String getP30001015() {
		return p30001015;
	}

	public void setP30001015(String p30001015) {
		this.p30001015 = p30001015;
	}

	public String getP30001016() {
		return p30001016;
	}

	public void setP30001016(String p30001016) {
		this.p30001016 = p30001016;
	}

	public String getP30001017() {
		return p30001017;
	}

	public void setP30001017(String p30001017) {
		this.p30001017 = p30001017;
	}

	public String getP30001018() {
		return p30001018;
	}

	public void setP30001018(String p30001018) {
		this.p30001018 = p30001018;
	}

	public String getP30001019() {
		return p30001019;
	}

	public void setP30001019(String p30001019) {
		this.p30001019 = p30001019;
	}

	public String getP30001020() {
		return p30001020;
	}

	public void setP30001020(String p30001020) {
		this.p30001020 = p30001020;
	}

	public String getP30001021() {
		return p30001021;
	}

	public void setP30001021(String p30001021) {
		this.p30001021 = p30001021;
	}

	public String getP30001022() {
		return p30001022;
	}

	public void setP30001022(String p30001022) {
		this.p30001022 = p30001022;
	}

	public String getP30001023() {
		return p30001023;
	}

	public void setP30001023(String p30001023) {
		this.p30001023 = p30001023;
	}

	public String getP30002001() {
		return p30002001;
	}

	public void setP30002001(String p30002001) {
		this.p30002001 = p30002001;
	}

	public String getP30002002() {
		return p30002002;
	}

	public void setP30002002(String p30002002) {
		this.p30002002 = p30002002;
	}

	public String getP30002003() {
		return p30002003;
	}

	public void setP30002003(String p30002003) {
		this.p30002003 = p30002003;
	}

	public String getP30002004() {
		return p30002004;
	}

	public void setP30002004(String p30002004) {
		this.p30002004 = p30002004;
	}

	public String getP30002005() {
		return p30002005;
	}

	public void setP30002005(String p30002005) {
		this.p30002005 = p30002005;
	}

	public String getP30002006() {
		return p30002006;
	}

	public void setP30002006(String p30002006) {
		this.p30002006 = p30002006;
	}

	public String getP30003001() {
		return p30003001;
	}

	public void setP30003001(String p30003001) {
		this.p30003001 = p30003001;
	}

	public String getP30003002() {
		return p30003002;
	}

	public void setP30003002(String p30003002) {
		this.p30003002 = p30003002;
	}

	public String getP30003003() {
		return p30003003;
	}

	public void setP30003003(String p30003003) {
		this.p30003003 = p30003003;
	}

	public String getP30003004() {
		return p30003004;
	}

	public void setP30003004(String p30003004) {
		this.p30003004 = p30003004;
	}

	public String getP30003005() {
		return p30003005;
	}

	public void setP30003005(String p30003005) {
		this.p30003005 = p30003005;
	}

	public String getP30003006() {
		return p30003006;
	}

	public void setP30003006(String p30003006) {
		this.p30003006 = p30003006;
	}

	public String getP30003007() {
		return p30003007;
	}

	public void setP30003007(String p30003007) {
		this.p30003007 = p30003007;
	}

	public String getP30003008() {
		return p30003008;
	}

	public void setP30003008(String p30003008) {
		this.p30003008 = p30003008;
	}

	public String getP_yyszj() {
		return p_yyszj;
	}

	public void setP_yyszj(String p_yyszj) {
		this.p_yyszj = p_yyszj;
	}

	public String getP_yyss() {
		return p_yyss;
	}

	public void setP_yyss(String p_yyss) {
		this.p_yyss = p_yyss;
	}

	public String getP_yyb() {
		return p_yyb;
	}

	public void setP_yyb(String p_yyb) {
		this.p_yyb = p_yyb;
	}

	public String getP_ddjgl() {
		return p_ddjgl;
	}

	public void setP_ddjgl(String p_ddjgl) {
		this.p_ddjgl = p_ddjgl;
	}

	public String getP_zs() {
		return p_zs;
	}

	public void setP_zs(String p_zs) {
		this.p_zs = p_zs;
	}

	public String getP_eddl() {
		return p_eddl;
	}

	public void setP_eddl(String p_eddl) {
		this.p_eddl = p_eddl;
	}

	public String getP_dcksl() {
		return p_dcksl;
	}

	public void setP_dcksl(String p_dcksl) {
		this.p_dcksl = p_dcksl;
	}

	public String getP_xsqdzsd() {
		return p_xsqdzsd;
	}

	public void setP_xsqdzsd(String p_xsqdzsd) {
		this.p_xsqdzsd = p_xsqdzsd;
	}

	public String getP_dtms() {
		return p_dtms;
	}

	public void setP_dtms(String p_dtms) {
		this.p_dtms = p_dtms;
	}

	public String getP_dtzs() {
		return p_dtzs;
	}

	public void setP_dtzs(String p_dtzs) {
		this.p_dtzs = p_dtzs;
	}

	public String getP_ftkd() {
		return p_ftkd;
	}

	public void setP_ftkd(String p_ftkd) {
		this.p_ftkd = p_ftkd;
	}

	public String getP_jxc() {
		return p_jxc;
	}

	public void setP_jxc(String p_jxc) {
		this.p_jxc = p_jxc;
	}

	public String getP_jxk() {
		return p_jxk;
	}

	public void setP_jxk(String p_jxk) {
		this.p_jxk = p_jxk;
	}

	public String getP_jxg() {
		return p_jxg;
	}

	public void setP_jxg(String p_jxg) {
		this.p_jxg = p_jxg;
	}

	public String getP_ygly() {
		return p_ygly;
	}

	public void setP_ygly(String p_ygly) {
		this.p_ygly = p_ygly;
	}

	public String getP_ygsl() {
		return p_ygsl;
	}

	public void setP_ygsl(String p_ygsl) {
		this.p_ygsl = p_ygsl;
	}

	public String getP_ygxs() {
		return p_ygxs;
	}

	public void setP_ygxs(String p_ygxs) {
		this.p_ygxs = p_ygxs;
	}

	public String getP_acqxh() {
		return p_acqxh;
	}

	public void setP_acqxh(String p_acqxh) {
		this.p_acqxh = p_acqxh;
	}

	public String getP_acqbh() {
		return p_acqbh;
	}

	public void setP_acqbh(String p_acqbh) {
		this.p_acqbh = p_acqbh;
	}

	public String getP_hcqxh() {
		return p_hcqxh;
	}

	public void setP_hcqxh(String p_hcqxh) {
		this.p_hcqxh = p_hcqxh;
	}

	public String getP_hcqbh() {
		return p_hcqbh;
	}

	public void setP_hcqbh(String p_hcqbh) {
		this.p_hcqbh = p_hcqbh;
	}

	public String getP_yybll() {
		return p_yybll;
	}

	public void setP_yybll(String p_yybll) {
		this.p_yybll = p_yybll;
	}

	public String getP_yybgl() {
		return p_yybgl;
	}

	public void setP_yybgl(String p_yybgl) {
		this.p_yybgl = p_yybgl;
	}

	public String getP_sxydsd() {
		return p_sxydsd;
	}

	public void setP_sxydsd(String p_sxydsd) {
		this.p_sxydsd = p_sxydsd;
	}

	public String getP_xxydsd() {
		return p_xxydsd;
	}

	public void setP_xxydsd(String p_xxydsd) {
		this.p_xxydsd = p_xxydsd;
	}

	public String getP_yyryj() {
		return p_yyryj;
	}

	public void setP_yyryj(String p_yyryj) {
		this.p_yyryj = p_yyryj;
	}

	public String getP_sb() {
		return p_sb;
	}

	public void setP_sb(String p_sb) {
		this.p_sb = p_sb;
	}

	public String getP_kmfx() {
		return p_kmfx;
	}

	public void setP_kmfx(String p_kmfx) {
		this.p_kmfx = p_kmfx;
	}

	public String getP_dcgd() {
		return p_dcgd;
	}

	public void setP_dcgd(String p_dcgd) {
		this.p_dcgd = p_dcgd;
	}

	public String getP_dksd() {
		return p_dksd;
	}

	public void setP_dksd(String p_dksd) {
		this.p_dksd = p_dksd;
	}

	public String getP_dqdzsd() {
		return p_dqdzsd;
	}

	public void setP_dqdzsd(String p_dqdzsd) {
		this.p_dqdzsd = p_dqdzsd;
	}

	public String getP_jjdzsd() {
		return p_jjdzsd;
	}

	public void setP_jjdzsd(String p_jjdzsd) {
		this.p_jjdzsd = p_jjdzsd;
	}

	public String getP_syqdcd() {
		return p_syqdcd;
	}

	public void setP_syqdcd(String p_syqdcd) {
		this.p_syqdcd = p_syqdcd;
	}

	public String getP_fjzdq() {
		return p_fjzdq;
	}

	public void setP_fjzdq(String p_fjzdq) {
		this.p_fjzdq = p_fjzdq;
	}

	public String getP_gdjtx() {
		return p_gdjtx;
	}

	public void setP_gdjtx(String p_gdjtx) {
		this.p_gdjtx = p_gdjtx;
	}

	public String getP_dy() {
		return p_dy;
	}

	public void setP_dy(String p_dy) {
		this.p_dy = p_dy;
	}

	public String getP_zcl() {
		return p_zcl;
	}

	public void setP_zcl(String p_zcl) {
		this.p_zcl = p_zcl;
	}

	public String getP_yxff() {
		return p_yxff;
	}

	public void setP_yxff(String p_yxff) {
		this.p_yxff = p_yxff;
	}

	public String getP_hysd() {
		return p_hysd;
	}

	public void setP_hysd(String p_hysd) {
		this.p_hysd = p_hysd;
	}

	public String getP_bwsl() {
		return p_bwsl;
	}

	public void setP_bwsl(String p_bwsl) {
		this.p_bwsl = p_bwsl;
	}

	public String getCp_by1() {
		return cp_by1;
	}

	public void setCp_by1(String cp_by1) {
		this.cp_by1 = cp_by1;
	}

	public String getCp_by2() {
		return cp_by2;
	}

	public void setCp_by2(String cp_by2) {
		this.cp_by2 = cp_by2;
	}

	public String getCp_by3() {
		return cp_by3;
	}

	public void setCp_by3(String cp_by3) {
		this.cp_by3 = cp_by3;
	}

	public String getCp_by4() {
		return cp_by4;
	}

	public void setCp_by4(String cp_by4) {
		this.cp_by4 = cp_by4;
	}

	public String getCp_by5() {
		return cp_by5;
	}

	public void setCp_by5(String cp_by5) {
		this.cp_by5 = cp_by5;
	}

	public String getCp_by6() {
		return cp_by6;
	}

	public void setCp_by6(String cp_by6) {
		this.cp_by6 = cp_by6;
	}

	public String getCp_by7() {
		return cp_by7;
	}

	public void setCp_by7(String cp_by7) {
		this.cp_by7 = cp_by7;
	}

	public String getCp_by8() {
		return cp_by8;
	}

	public void setCp_by8(String cp_by8) {
		this.cp_by8 = cp_by8;
	}

	public String getCp_by9() {
		return cp_by9;
	}

	public void setCp_by9(String cp_by9) {
		this.cp_by9 = cp_by9;
	}

	public String getCp_by10() {
		return cp_by10;
	}

	public void setCp_by10(String cp_by10) {
		this.cp_by10 = cp_by10;
	}

	public String getCbz() {
		return cbz;
	}

	public void setCbz(String cbz) {
		this.cbz = cbz;
	}

	public String getCcdbz() {
		return ccdbz;
	}

	public void setCcdbz(String ccdbz) {
		this.ccdbz = ccdbz;
	}

	public String getCcjrbm() {
		return ccjrbm;
	}

	public void setCcjrbm(String ccjrbm) {
		this.ccjrbm = ccjrbm;
	}

	public String getCcjrmc() {
		return ccjrmc;
	}

	public void setCcjrmc(String ccjrmc) {
		this.ccjrmc = ccjrmc;
	}

	public String getCcjrq() {
		return ccjrq;
	}

	public void setCcjrq(String ccjrq) {
		this.ccjrq = ccjrq;
	}

	public String getCgxrq() {
		return cgxrq;
	}

	public void setCgxrq(String cgxrq) {
		this.cgxrq = cgxrq;
	}

	public String getMykd() {
		return mykd;
	}

	public void setMykd(String mykd) {
		this.mykd = mykd;
	}

	public String getMysd() {
		return mysd;
	}

	public void setMysd(String mysd) {
		this.mysd = mysd;
	}

	public String getSsnl() {
		return ssnl;
	}

	public void setSsnl(String ssnl) {
		this.ssnl = ssnl;
	}

	public String getP30003009() {
		return p30003009;
	}

	public void setP30003009(String p30003009) {
		this.p30003009 = p30003009;
	}

	public String getP30003010() {
		return p30003010;
	}

	public void setP30003010(String p30003010) {
		this.p30003010 = p30003010;
	}
	
	
}
