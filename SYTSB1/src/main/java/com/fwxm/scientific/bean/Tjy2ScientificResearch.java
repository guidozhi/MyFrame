package com.fwxm.scientific.bean;

import java.beans.Transient;
import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.khnt.core.crud.bean.BaseEntity;

@Entity
@Table(name = "TJY2_SCIENTIFIC_RESEARCH")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tjy2ScientificResearch implements BaseEntity{

    private String id;//id

    private String projectName;//项目名称

    private String projectNo;//项目编号
    private String projectType;//项目类别

    private String professionalType;//专业类别

    private String projectDepartment;//承担部门
    private String projectDepartmentId;//承担部门

    private java.util.Date startDate;//开始日期

    private java.util.Date endDate;//结束日期

    private String projectHead;//项目负责人
    private String projectHeadId;//项目负责人id

    private java.util.Date fillDate;//填表日期

    private java.util.Date createDate;//新建时间

    private String createMan;//新建人

    private java.util.Date lastModifyDate;//最后修改日期

    private String lastModifyMan;//最后修改人

    private String status;//状态 0 正常 99删除

   

    private String fileName;//申请文件名称

    

    private String taskFileName;//任务文件名

    private String projectMoney;//项目资金
    private String projectResultsType;//项目成果形式
    private String projectResults;//项目成果
    private String projectFlag;//项目模块
    private String remark;//备注
   /* private byte  P100001[];//第一页内容
    private byte  P200001[];//第二页内容
    private byte  P300001[];//第三页内容
    private byte  P400001[];//第四页内容
    private byte  P500001[];//第五页内容
    private byte  P600001[];//第六页内容
    private byte  P700001[];//第七页内容
*/    private String  P800001;//第八页内容
    private String  P800002;
    private String  P800003;
    private String  P800004;
    private String  P800005;
    private String  P800006;
    private String  P800007;
    private String  P800008;
    private String  P800009;
    private String  P8000010;
    private String  P900001;//第九页内容
    private String  P900002;
    private String  P900003;
    private String  P900004;
    private String  P900005;
    private String  P900006;
    private String  P900007;
    private String  P900008;
    private String  P900009;
    private String  P9000010;
    private String  P9000011;
    private String  P9000012;
    private String  P9000013;
    private String  P9000014;
    private String  P9000015;
    private String  P9000016;
    private String  P9000017;
    private String  P9000018;
    private String  P1000001;//第十页内容
    private String  P1000002;
    private String  P1000003;
    private String  P1000004;
    private String  P1000005;
    private String  P1000006;
    private String  P1000007;
    private String  P1000008;
    private String  P1000009;
    private String  P10000010;
    private String  P10000011;
    private String  P10000012;
    private String  P10000013;
    private String  P10000014;
    private String  P10000015;
    private String  P10000016;
    private String  P10000017;
    private String  P10000018;
    private String  P10000019;
    private String  P10000020;
    private String  P1100001;//第十一页内容
    private String  P1100002;
    private String  P1100003;
    private String  P1100004;
    private String  P1100005;
    private String  P1100006;
    private String  P1100007;
    private String  P1100008;
    private String  P1100009;
    private String  P11000010;
    private String  P11000011;
    private String  P11000012;
    private String  P11000013;
    private String  P11000014;
    private String  P11000015;
    private String  P11000016;
    private String  P11000017;
    private String  P11000018;
    private String  P11000019;
    private String  P11000020;
    private String  P11000021;
    private String  P11000022;
    private String  P11000023;
    private String  P11000024;
    private String  P11000025;
    private String  P11000026;
    private String  P11000027;
    private String  P11000028;
    private String  P11000029;
    private String  P11000030;
    private String  P11000031;
    private String  P11000032;
    private String  P11000033;
    private String  P11000034;
    private String  P11000035;
    private String  P11000036;
    private String  P11000037;
    private String  P11000038;
    private String  P11000039;
    private String  P11000040;
    private String  P11000041;
    private String  P11000042;
    private String  P11000043;
    private String  P11000044;
    private String  P11000045;
    private String  P11000046;
    private String  P11000047;
    private String  P11000048;
    private String  P11000049;
    private String  P11000050;
    private String  P11000051;
    private String  P11000052;
    private String  P11000053;
    private String  P11000054;
    private String  P11000055;
    private String  P11000056;
    private String  P11000057;
    private String  P11000058;
    private String  P11000059;
    private String  P11000060;
    private String  P11000061;
    private String  P11000062;
    private String  P11000063;
    private String  P11000064;
    private String  P11000065;
    
    private String auditName;//审核人
    private String auditId;//审核人id
    private String auditStatus;//审核人状态
    
    
    

    @Id
   	@GeneratedValue(generator = "system-uuid")
   	@GenericGenerator(name = "system-uuid", strategy = "uuid")
   	public String getId() {
   		return id;
   	}

   	public void setId(String id) {
   		this.id = id;
   	}
    public void setProjectName(String value){
        this.projectName = value;
    }
    public void setProjectType(String value){
        this.projectType = value;
    }
    public void setProfessionalType(String value){
        this.professionalType = value;
    }
    public void setProjectDepartment(String value){
        this.projectDepartment = value;
    }
    public void setStartDate(java.util.Date value){
        this.startDate = value;
    }
    public void setEndDate(java.util.Date value){
        this.endDate = value;
    }
    public void setProjectHead(String value){
        this.projectHead = value;
    }
    public void setFillDate(java.util.Date value){
        this.fillDate = value;
    }
    public void setCreateDate(java.util.Date value){
        this.createDate = value;
    }
    public void setCreateMan(String value){
        this.createMan = value;
    }
    public void setLastModifyDate(java.util.Date value){
        this.lastModifyDate = value;
    }
    public void setLastModifyMan(String value){
        this.lastModifyMan = value;
    }
    public void setStatus(String value){
        this.status = value;
    }

    public void setFileName(String value){
        this.fileName = value;
    }

    public void setTaskFileName(String value){
        this.taskFileName = value;
    }
    public void setProjectMoney(String value){
        this.projectMoney = value;
    }

    @Column(name ="PROJECT_NAME",unique=false,nullable=true,insertable=true,updatable=true,length=100)
    public String getProjectName(){
        return this.projectName;
    }
    @Column(name ="PROJECT_TYPE",unique=false,nullable=true,insertable=true,updatable=true,length=32)
    public String getProjectType(){
        return this.projectType;
    }
    @Column(name ="PROFESSIONAL_TYPE",unique=false,nullable=true,insertable=true,updatable=true,length=32)
    public String getProfessionalType(){
        return this.professionalType;
    }
    @Column(name ="PROJECT_DEPARTMENT",unique=false,nullable=true,insertable=true,updatable=true,length=32)
    public String getProjectDepartment(){
        return this.projectDepartment;
    }
    @Column(name ="START_DATE",unique=false,nullable=true,insertable=true,updatable=true,length=7)
    public java.util.Date getStartDate(){
        return this.startDate;
    }
    @Column(name ="END_DATE",unique=false,nullable=true,insertable=true,updatable=true,length=7)
    public java.util.Date getEndDate(){
        return this.endDate;
    }
    @Column(name ="PROJECT_HEAD",unique=false,nullable=true,insertable=true,updatable=true,length=32)
    public String getProjectHead(){
        return this.projectHead;
    }
    @Column(name ="FILL_DATE",unique=false,nullable=true,insertable=true,updatable=true,length=7)
    public java.util.Date getFillDate(){
        return this.fillDate;
    }
    @Column(name ="CREATE_DATE",unique=false,nullable=true,insertable=true,updatable=true,length=7)
    public java.util.Date getCreateDate(){
        return this.createDate;
    }
    @Column(name ="CREATE_MAN",unique=false,nullable=true,insertable=true,updatable=true,length=32)
    public String getCreateMan(){
        return this.createMan;
    }
    @Column(name ="LAST_MODIFY_DATE",unique=false,nullable=true,insertable=true,updatable=true,length=7)
    public java.util.Date getLastModifyDate(){
        return this.lastModifyDate;
    }
    @Column(name ="LAST_MODIFY_MAN",unique=false,nullable=true,insertable=true,updatable=true,length=32)
    public String getLastModifyMan(){
        return this.lastModifyMan;
    }
    @Column(name ="STATUS",unique=false,nullable=true,insertable=true,updatable=true,length=32)
    public String getStatus(){
        return this.status;
    }
    @Column(name ="FILE_NAME",unique=false,nullable=true,insertable=true,updatable=true,length=100)
    public String getFileName(){
        return this.fileName;
    }
    @Column(name ="TASK_FILE_NAME",unique=false,nullable=true,insertable=true,updatable=true,length=100)
    public String getTaskFileName(){
        return this.taskFileName;
    }
    @Column(name ="PROJECT_MONEY",unique=false,nullable=true,insertable=true,updatable=true,length=32)
    public String getProjectMoney(){
        return this.projectMoney;
    }
    @Column(name ="PROJECT_DEPARTMENT_ID")
	public String getProjectDepartmentId() {
		return projectDepartmentId;
	}

	public void setProjectDepartmentId(String projectDepartmentId) {
		this.projectDepartmentId = projectDepartmentId;
	}
	@Column(name ="PROJECT_HEAD_ID")
	public String getProjectHeadId() {
		return projectHeadId;
	}

	public void setProjectHeadId(String projectHeadId) {
		this.projectHeadId = projectHeadId;
	}
	@Column(name ="PROJECT_RESULTS_TYPE")
	public String getProjectResultsType() {
		return projectResultsType;
	}

	public void setProjectResultsType(String projectResultsType) {
		this.projectResultsType = projectResultsType;
	}
	@Column(name ="PROJECT_RESULTS")
	public String getProjectResults() {
		return projectResults;
	}

	public void setProjectResults(String projectResults) {
		this.projectResults = projectResults;
	}
	@Column(name ="REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name ="P800001")
	public String getP800001() {
		return P800001;
	}

	public void setP800001(String p800001) {
		P800001 = p800001;
	}
	
	@Column(name ="P800002")
	public String getP800002() {
		return P800002;
	}

	public void setP800002(String p800002) {
		P800002 = p800002;
	}
	@Column(name ="P800003")
	public String getP800003() {
		return P800003;
	}

	public void setP800003(String p800003) {
		P800003 = p800003;
	}
	@Column(name ="P800004")
	public String getP800004() {
		return P800004;
	}

	public void setP800004(String p800004) {
		P800004 = p800004;
	}
	@Column(name ="P800005")
	public String getP800005() {
		return P800005;
	}

	public void setP800005(String p800005) {
		P800005 = p800005;
	}
	@Column(name ="P800006")
	public String getP800006() {
		return P800006;
	}

	public void setP800006(String p800006) {
		P800006 = p800006;
	}
	@Column(name ="P800007")
	public String getP800007() {
		return P800007;
	}

	public void setP800007(String p800007) {
		P800007 = p800007;
	}
	@Column(name ="P800008")
	public String getP800008() {
		return P800008;
	}

	public void setP800008(String p800008) {
		P800008 = p800008;
	}
	@Column(name ="P800009")
	public String getP800009() {
		return P800009;
	}

	public void setP800009(String p800009) {
		P800009 = p800009;
	}
	@Column(name ="P8000010")
	public String getP8000010() {
		return P8000010;
	}

	public void setP8000010(String p8000010) {
		P8000010 = p8000010;
	}
	@Column(name ="P900001")
	public String getP900001() {
		return P900001;
	}

	public void setP900001(String p900001) {
		P900001 = p900001;
	}
	@Column(name ="P900002")
	public String getP900002() {
		return P900002;
	}

	public void setP900002(String p900002) {
		P900002 = p900002;
	}
	@Column(name ="P900003")
	public String getP900003() {
		return P900003;
	}

	public void setP900003(String p900003) {
		P900003 = p900003;
	}
	@Column(name ="P900004")
	public String getP900004() {
		return P900004;
	}

	public void setP900004(String p900004) {
		P900004 = p900004;
	}
	@Column(name ="P900005")
	public String getP900005() {
		return P900005;
	}

	public void setP900005(String p900005) {
		P900005 = p900005;
	}
	@Column(name ="P900006")
	public String getP900006() {
		return P900006;
	}

	public void setP900006(String p900006) {
		P900006 = p900006;
	}
	@Column(name ="P900007")
	public String getP900007() {
		return P900007;
	}

	public void setP900007(String p900007) {
		P900007 = p900007;
	}
	@Column(name ="P900008")
	public String getP900008() {
		return P900008;
	}

	public void setP900008(String p900008) {
		P900008 = p900008;
	}
	@Column(name ="P900009")
	public String getP900009() {
		return P900009;
	}

	public void setP900009(String p900009) {
		P900009 = p900009;
	}
	@Column(name ="P9000010")
	public String getP9000010() {
		return P9000010;
	}

	public void setP9000010(String p9000010) {
		P9000010 = p9000010;
	}
	@Column(name ="P9000011")
	public String getP9000011() {
		return P9000011;
	}

	public void setP9000011(String p9000011) {
		P9000011 = p9000011;
	}
	@Column(name ="P9000012")
	public String getP9000012() {
		return P9000012;
	}

	public void setP9000012(String p9000012) {
		P9000012 = p9000012;
	}
	@Column(name ="P9000013")
	public String getP9000013() {
		return P9000013;
	}

	public void setP9000013(String p9000013) {
		P9000013 = p9000013;
	}
	@Column(name ="P9000014")
	public String getP9000014() {
		return P9000014;
	}

	public void setP9000014(String p9000014) {
		P9000014 = p9000014;
	}
	@Column(name ="P9000015")
	public String getP9000015() {
		return P9000015;
	}

	public void setP9000015(String p9000015) {
		P9000015 = p9000015;
	}
	@Column(name ="P9000016")
	public String getP9000016() {
		return P9000016;
	}

	public void setP9000016(String p9000016) {
		P9000016 = p9000016;
	}
	@Column(name ="P9000017")
	public String getP9000017() {
		return P9000017;
	}

	public void setP9000017(String p9000017) {
		P9000017 = p9000017;
	}
	@Column(name ="P9000018")
	public String getP9000018() {
		return P9000018;
	}

	public void setP9000018(String p9000018) {
		P9000018 = p9000018;
	}
	
	@Column(name ="P1000001")
	public String getP1000001() {
		return P1000001;
	}

	public void setP1000001(String p1000001) {
		P1000001 = p1000001;
	}
	@Column(name ="P1000002")
	public String getP1000002() {
		return P1000002;
	}

	public void setP1000002(String p1000002) {
		P1000002 = p1000002;
	}
	@Column(name ="P1000003")
	public String getP1000003() {
		return P1000003;
	}

	public void setP1000003(String p1000003) {
		P1000003 = p1000003;
	}
	@Column(name ="P1000004")
	public String getP1000004() {
		return P1000004;
	}

	public void setP1000004(String p1000004) {
		P1000004 = p1000004;
	}
	@Column(name ="P1000005")
	public String getP1000005() {
		return P1000005;
	}

	public void setP1000005(String p1000005) {
		P1000005 = p1000005;
	}
	@Column(name ="P1000006")
	public String getP1000006() {
		return P1000006;
	}

	public void setP1000006(String p1000006) {
		P1000006 = p1000006;
	}
	@Column(name ="P1000007")
	public String getP1000007() {
		return P1000007;
	}

	public void setP1000007(String p1000007) {
		P1000007 = p1000007;
	}
	@Column(name ="P1000008")
	public String getP1000008() {
		return P1000008;
	}

	public void setP1000008(String p1000008) {
		P1000008 = p1000008;
	}
	@Column(name ="P1000009")
	public String getP1000009() {
		return P1000009;
	}

	public void setP1000009(String p1000009) {
		P1000009 = p1000009;
	}
	@Column(name ="P10000010")
	public String getP10000010() {
		return P10000010;
	}

	public void setP10000010(String p10000010) {
		P10000010 = p10000010;
	}
	@Column(name ="P10000011")
	public String getP10000011() {
		return P10000011;
	}

	public void setP10000011(String p10000011) {
		P10000011 = p10000011;
	}
	@Column(name ="P10000012")
	public String getP10000012() {
		return P10000012;
	}

	public void setP10000012(String p10000012) {
		P10000012 = p10000012;
	}
	@Column(name ="P10000013")
	public String getP10000013() {
		return P10000013;
	}

	public void setP10000013(String p10000013) {
		P10000013 = p10000013;
	}
	@Column(name ="P10000014")
	public String getP10000014() {
		return P10000014;
	}

	public void setP10000014(String p10000014) {
		P10000014 = p10000014;
	}
	@Column(name ="P10000015")
	public String getP10000015() {
		return P10000015;
	}

	public void setP10000015(String p10000015) {
		P10000015 = p10000015;
	}
	@Column(name ="P10000016")
	public String getP10000016() {
		return P10000016;
	}

	public void setP10000016(String p10000016) {
		P10000016 = p10000016;
	}
	@Column(name ="P10000017")
	public String getP10000017() {
		return P10000017;
	}

	public void setP10000017(String p10000017) {
		P10000017 = p10000017;
	}
	@Column(name ="P10000018")
	public String getP10000018() {
		return P10000018;
	}

	public void setP10000018(String p10000018) {
		P10000018 = p10000018;
	}
	@Column(name ="P10000019")
	public String getP10000019() {
		return P10000019;
	}

	public void setP10000019(String p10000019) {
		P10000019 = p10000019;
	}
	@Column(name ="P10000020")
	public String getP10000020() {
		return P10000020;
	}

	public void setP10000020(String p10000020) {
		P10000020 = p10000020;
	}
	@Column(name ="P1100001")
	public String getP1100001() {
		return P1100001;
	}

	public void setP1100001(String p1100001) {
		P1100001 = p1100001;
	}
	@Column(name ="P1100002")
	public String getP1100002() {
		return P1100002;
	}

	public void setP1100002(String p1100002) {
		P1100002 = p1100002;
	}
	@Column(name ="P1100003")
	public String getP1100003() {
		return P1100003;
	}

	public void setP1100003(String p1100003) {
		P1100003 = p1100003;
	}
	@Column(name ="P1100004")
	public String getP1100004() {
		return P1100004;
	}

	public void setP1100004(String p1100004) {
		P1100004 = p1100004;
	}
	@Column(name ="P1100005")
	public String getP1100005() {
		return P1100005;
	}

	public void setP1100005(String p1100005) {
		P1100005 = p1100005;
	}
	@Column(name ="P1100006")
	public String getP1100006() {
		return P1100006;
	}

	public void setP1100006(String p1100006) {
		P1100006 = p1100006;
	}
	@Column(name ="P1100007")
	public String getP1100007() {
		return P1100007;
	}

	public void setP1100007(String p1100007) {
		P1100007 = p1100007;
	}
	@Column(name ="P1100008")
	public String getP1100008() {
		return P1100008;
	}

	public void setP1100008(String p1100008) {
		P1100008 = p1100008;
	}
	@Column(name ="P1100009")
	public String getP1100009() {
		return P1100009;
	}

	public void setP1100009(String p1100009) {
		P1100009 = p1100009;
	}
	@Column(name ="P11000010")
	public String getP11000010() {
		return P11000010;
	}

	public void setP11000010(String p11000010) {
		P11000010 = p11000010;
	}
	@Column(name ="P11000011")
	public String getP11000011() {
		return P11000011;
	}

	public void setP11000011(String p11000011) {
		P11000011 = p11000011;
	}
	@Column(name ="P11000012")
	public String getP11000012() {
		return P11000012;
	}

	public void setP11000012(String p11000012) {
		P11000012 = p11000012;
	}
	@Column(name ="P11000013")
	public String getP11000013() {
		return P11000013;
	}

	public void setP11000013(String p11000013) {
		P11000013 = p11000013;
	}
	@Column(name ="P11000014")
	public String getP11000014() {
		return P11000014;
	}

	public void setP11000014(String p11000014) {
		P11000014 = p11000014;
	}
	@Column(name ="P11000015")
	public String getP11000015() {
		return P11000015;
	}

	public void setP11000015(String p11000015) {
		P11000015 = p11000015;
	}
	@Column(name ="P11000016")
	public String getP11000016() {
		return P11000016;
	}

	public void setP11000016(String p11000016) {
		P11000016 = p11000016;
	}
	@Column(name ="P11000017")
	public String getP11000017() {
		return P11000017;
	}

	public void setP11000017(String p11000017) {
		P11000017 = p11000017;
	}
	@Column(name ="P11000018")
	public String getP11000018() {
		return P11000018;
	}

	public void setP11000018(String p11000018) {
		P11000018 = p11000018;
	}
	@Column(name ="P11000019")
	public String getP11000019() {
		return P11000019;
	}

	public void setP11000019(String p11000019) {
		P11000019 = p11000019;
	}
	@Column(name ="P11000020")
	public String getP11000020() {
		return P11000020;
	}

	public void setP11000020(String p11000020) {
		P11000020 = p11000020;
	}
	@Column(name ="P11000021")
	public String getP11000021() {
		return P11000021;
	}

	public void setP11000021(String p11000021) {
		P11000021 = p11000021;
	}
	@Column(name ="P11000022")
	public String getP11000022() {
		return P11000022;
	}

	public void setP11000022(String p11000022) {
		P11000022 = p11000022;
	}
	@Column(name ="P11000023")
	public String getP11000023() {
		return P11000023;
	}

	public void setP11000023(String p11000023) {
		P11000023 = p11000023;
	}
	@Column(name ="P11000024")
	public String getP11000024() {
		return P11000024;
	}

	public void setP11000024(String p11000024) {
		P11000024 = p11000024;
	}
	@Column(name ="P11000025")
	public String getP11000025() {
		return P11000025;
	}

	public void setP11000025(String p11000025) {
		P11000025 = p11000025;
	}
	@Column(name ="P11000026")
	public String getP11000026() {
		return P11000026;
	}

	public void setP11000026(String p11000026) {
		P11000026 = p11000026;
	}
	@Column(name ="P11000027")
	public String getP11000027() {
		return P11000027;
	}

	public void setP11000027(String p11000027) {
		P11000027 = p11000027;
	}
	@Column(name ="P11000028")
	public String getP11000028() {
		return P11000028;
	}

	public void setP11000028(String p11000028) {
		P11000028 = p11000028;
	}
	@Column(name ="P11000029")
	public String getP11000029() {
		return P11000029;
	}

	public void setP11000029(String p11000029) {
		P11000029 = p11000029;
	}
	@Column(name ="P11000030")
	public String getP11000030() {
		return P11000030;
	}

	public void setP11000030(String p11000030) {
		P11000030 = p11000030;
	}
	@Column(name ="P11000031")
	public String getP11000031() {
		return P11000031;
	}

	public void setP11000031(String p11000031) {
		P11000031 = p11000031;
	}
	@Column(name ="P11000032")
	public String getP11000032() {
		return P11000032;
	}

	public void setP11000032(String p11000032) {
		P11000032 = p11000032;
	}
	@Column(name ="P11000033")
	public String getP11000033() {
		return P11000033;
	}

	public void setP11000033(String p11000033) {
		P11000033 = p11000033;
	}
	@Column(name ="P11000034")
	public String getP11000034() {
		return P11000034;
	}

	public void setP11000034(String p11000034) {
		P11000034 = p11000034;
	}
	@Column(name ="P11000035")
	public String getP11000035() {
		return P11000035;
	}

	public void setP11000035(String p11000035) {
		P11000035 = p11000035;
	}
	@Column(name ="P11000036")
	public String getP11000036() {
		return P11000036;
	}

	public void setP11000036(String p11000036) {
		P11000036 = p11000036;
	}
	@Column(name ="P11000037")
	public String getP11000037() {
		return P11000037;
	}

	public void setP11000037(String p11000037) {
		P11000037 = p11000037;
	}
	@Column(name ="P11000038")
	public String getP11000038() {
		return P11000038;
	}

	public void setP11000038(String p11000038) {
		P11000038 = p11000038;
	}
	@Column(name ="P11000039")
	public String getP11000039() {
		return P11000039;
	}

	public void setP11000039(String p11000039) {
		P11000039 = p11000039;
	}
	@Column(name ="P11000040")
	public String getP11000040() {
		return P11000040;
	}

	public void setP11000040(String p11000040) {
		P11000040 = p11000040;
	}
	@Column(name ="P11000041")
	public String getP11000041() {
		return P11000041;
	}

	public void setP11000041(String p11000041) {
		P11000041 = p11000041;
	}
	@Column(name ="P11000042")
	public String getP11000042() {
		return P11000042;
	}

	public void setP11000042(String p11000042) {
		P11000042 = p11000042;
	}
	@Column(name ="P11000043")
	public String getP11000043() {
		return P11000043;
	}

	public void setP11000043(String p11000043) {
		P11000043 = p11000043;
	}
	@Column(name ="P11000044")
	public String getP11000044() {
		return P11000044;
	}

	public void setP11000044(String p11000044) {
		P11000044 = p11000044;
	}
	@Column(name ="P11000045")
	public String getP11000045() {
		return P11000045;
	}

	public void setP11000045(String p11000045) {
		P11000045 = p11000045;
	}
	@Column(name ="P11000046")
	public String getP11000046() {
		return P11000046;
	}

	public void setP11000046(String p11000046) {
		P11000046 = p11000046;
	}
	@Column(name ="P11000047")
	public String getP11000047() {
		return P11000047;
	}

	public void setP11000047(String p11000047) {
		P11000047 = p11000047;
	}
	@Column(name ="P11000048")
	public String getP11000048() {
		return P11000048;
	}

	public void setP11000048(String p11000048) {
		P11000048 = p11000048;
	}
	@Column(name ="P11000049")
	public String getP11000049() {
		return P11000049;
	}

	public void setP11000049(String p11000049) {
		P11000049 = p11000049;
	}
	@Column(name ="P11000050")
	public String getP11000050() {
		return P11000050;
	}

	public void setP11000050(String p11000050) {
		P11000050 = p11000050;
	}
	@Column(name ="P11000051")
	public String getP11000051() {
		return P11000051;
	}

	public void setP11000051(String p11000051) {
		P11000051 = p11000051;
	}
	@Column(name ="P11000052")
	public String getP11000052() {
		return P11000052;
	}

	public void setP11000052(String p11000052) {
		P11000052 = p11000052;
	}
	@Column(name ="P11000053")
	public String getP11000053() {
		return P11000053;
	}

	public void setP11000053(String p11000053) {
		P11000053 = p11000053;
	}
	@Column(name ="P11000054")
	public String getP11000054() {
		return P11000054;
	}

	public void setP11000054(String p11000054) {
		P11000054 = p11000054;
	}
	@Column(name ="P11000055")
	public String getP11000055() {
		return P11000055;
	}

	public void setP11000055(String p11000055) {
		P11000055 = p11000055;
	}
	@Column(name ="P11000056")
	public String getP11000056() {
		return P11000056;
	}

	public void setP11000056(String p11000056) {
		P11000056 = p11000056;
	}
	@Column(name ="P11000057")
	public String getP11000057() {
		return P11000057;
	}

	public void setP11000057(String p11000057) {
		P11000057 = p11000057;
	}
	@Column(name ="P11000058")
	public String getP11000058() {
		return P11000058;
	}

	public void setP11000058(String p11000058) {
		P11000058 = p11000058;
	}
	@Column(name ="P11000059")
	public String getP11000059() {
		return P11000059;
	}

	public void setP11000059(String p11000059) {
		P11000059 = p11000059;
	}
	@Column(name ="P11000060")
	public String getP11000060() {
		return P11000060;
	}

	public void setP11000060(String p11000060) {
		P11000060 = p11000060;
	}
	@Column(name ="P11000061")
	public String getP11000061() {
		return P11000061;
	}

	public void setP11000061(String p11000061) {
		P11000061 = p11000061;
	}
	@Column(name ="P11000062")
	public String getP11000062() {
		return P11000062;
	}

	public void setP11000062(String p11000062) {
		P11000062 = p11000062;
	}
	@Column(name ="P11000063")
	public String getP11000063() {
		return P11000063;
	}

	public void setP11000063(String p11000063) {
		P11000063 = p11000063;
	}
	@Column(name ="P11000064")
	public String getP11000064() {
		return P11000064;
	}

	public void setP11000064(String p11000064) {
		P11000064 = p11000064;
	}
	@Column(name ="P11000065")
	public String getP11000065() {
		return P11000065;
	}

	public void setP11000065(String p11000065) {
		P11000065 = p11000065;
	}
	@Column(name ="PROJECT_NO")
	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
	@Column(name ="AUDIT_NAME")
	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}
	@Column(name ="AUDIT_ID")
	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	@Column(name ="AUDIT_STATUS")
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	@Column(name ="PROJECT_FLAG")
	public String getProjectFlag() {
		return projectFlag;
	}

	public void setProjectFlag(String projectFlag) {
		this.projectFlag = projectFlag;
	}
    
	
   /* @Transient
	public byte[] getFileBlob() {
		return fileBlob;
	}

	public void setFileBlob(byte[] fileBlob) {
		this.fileBlob = fileBlob;
	}
	 @Transient
	public byte[] getTaskFile() {
		return taskFile;
	}

	public void setTaskFile(byte[] taskFile) {
		this.taskFile = taskFile;
	}
*/

} 
