package com.lsts.humanresources.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.khnt.core.crud.bean.BaseEntity;

@Entity
@Table(name = "TJY2_RL_EMPLOYEE_BASE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeBase implements BaseEntity {

    private String id;//id

    private String empName;//员工姓名

    private String empSex;//员工性别

    private String empNation;//民族

    private String empNativePlace;//籍贯

    private String empIdCard;//身份证号

    private String empPolitical;//政治面貌

    private java.math.BigDecimal empStature;//身高

    private java.math.BigDecimal empWeight;//体重

    private String empTitle;//职称

    private String empTitleNum;//职称证书编号

    private String empInspection;//检验资格

    private String empPermit;//持证情况

    private String empPhone;//联系电话

    private java.math.BigDecimal expectSalary;//期望工资

    private java.math.BigDecimal realSalary;//实际工资

    private String empAwards;//获奖情况

    private String empEvaluation;//个人评价

    private String empHobby;//兴趣爱好

    private String otherSkills;//其他能力

    private String empStatus;//员工状态(0 应聘申请 1审核申请 2 面试 3 试用 4 在职 5 解聘 6 离退休 7 删除 8 离职)

    private String empBirthday;//员工生日

    private java.util.Date fireDate;//试用日期

    private String empPhoto;//员工照片

    private java.util.Date initialStartDate;//教育初始开始日期

    private java.util.Date initialStopDate;//教育初始结束日期

    private String initialEducation;//教育初始学历

    private String initialMajor;//教育初始专业

    private String initialSchool;//教育初始毕业院校

    private java.util.Date mbaStartDate;//教育在职开始日期

    private java.util.Date mbaStopDate;//教育在职结束日期

    private String mbaEducation;//教育在职学历

    private String mbaMajor;//教育在职专业

    private String mbaSchool;//教育在职毕业院校

    private java.util.Date positiveDate;//转正日期

    private String testScorePicture;//考试成绩图片

    private String workTitle;//工作职称

    private String workDepartment;//工作部门id
    private String workDepartmentName;//工作部门名称

    private String contractType;//合同类型
    private String wechat;//w微信

    private java.util.Date contractStopDate;//合同签订时间
    private java.util.Date workTitleDate;//工作职称到期时间
    private String empPosition;//员工身份
    private String empMating;//是否结婚
    private java.util.Date birthDate;//出生年月
    private String email;//邮件
    private String mobilePhone;//手机
    private String addressPhone;//家庭电话
    private String officePhone;//办公电话
    private String currentAddress;//现居地址
    private Set<WorkExperience> workExperience;
    private String fkMessageId;//信息表id
    private String fkEmployee;//员工信息表
    private String fkEmployeeCert;//持证情况表
    private String authority;//权限
    private String enterprise;//企业号
    private String freelanceJobs;//应聘岗位
    private String initial_degree;//初始学位
    private String mba_degree;//在职学位
    private java.util.Date createDate;//创建时间
    private java.util.Date joinWorkDate;//参加工作时间
    private java.util.Date intoWorkDate;//进入本单位时间
    private java.util.Date joinPartyDate;//入党时间
    private String manSource;//人员来源
    private String fundingShape;//经费形式
    private String position;//岗位
    private String grade;//等级
    private String isCheck;//是否确认
    private java.util.Date seniorityDate;//计算工龄时间
    private String leaveDays;//年假天数
    private String extraDays;//额外天数
    private String totalDays;//总共年假天数
    private String workTitleWarnExcept;//职务预警例外标记（0，不例外；1，例外）
    private String retiredWarnExcept;//退休预警例外标记（0，不例外；1，例外）
    private String sort;//排序
    private String empTitleQt;//职称

    private String positionTitleType;//职称类型
    private Date positionTitleStopDate;//职称聘用到期日期

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "fkRlEmplpyeeId")
    public Set<WorkExperience> getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(Set<WorkExperience> workExperience) {
        this.workExperience = workExperience;
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmpName(String value) {
        this.empName = value;
    }

    public void setIsCheck(String value) {
        this.isCheck = value;
    }

    public void setEmpSex(String value) {
        this.empSex = value;
    }

    public void setEmpNation(String value) {
        this.empNation = value;
    }

    public void setEmpNativePlace(String value) {
        this.empNativePlace = value;
    }

    public void setEmpIdCard(String value) {
        this.empIdCard = value;
    }

    public void setEmpPolitical(String value) {
        this.empPolitical = value;
    }

    public void setEmpStature(java.math.BigDecimal value) {
        this.empStature = value;
    }

    public void setEmpWeight(java.math.BigDecimal value) {
        this.empWeight = value;
    }

    public void setEmpTitle(String value) {
        this.empTitle = value;
    }

    public void setEmpTitleNum(String value) {
        this.empTitleNum = value;
    }

    public void setEmpInspection(String value) {
        this.empInspection = value;
    }

    public void setEmpPermit(String value) {
        this.empPermit = value;
    }

    public void setEmpPhone(String value) {
        this.empPhone = value;
    }

    public void setExpectSalary(java.math.BigDecimal value) {
        this.expectSalary = value;
    }

    public void setRealSalary(java.math.BigDecimal value) {
        this.realSalary = value;
    }

    public void setEmpAwards(String value) {
        this.empAwards = value;
    }

    public void setEmpEvaluation(String value) {
        this.empEvaluation = value;
    }

    public void setEmpHobby(String value) {
        this.empHobby = value;
    }

    public void setOtherSkills(String value) {
        this.otherSkills = value;
    }

    public void setEmpStatus(String value) {
        this.empStatus = value;
    }

    public void setEmpBirthday(String value) {
        this.empBirthday = value;
    }

    public void setFireDate(java.util.Date value) {
        this.fireDate = value;
    }

    public void setEmpPhoto(String value) {
        this.empPhoto = value;
    }

    public void setInitialStartDate(java.util.Date value) {
        this.initialStartDate = value;
    }

    public void setInitialStopDate(java.util.Date value) {
        this.initialStopDate = value;
    }

    public void setInitialEducation(String value) {
        this.initialEducation = value;
    }

    public void setInitialMajor(String value) {
        this.initialMajor = value;
    }

    public void setInitialSchool(String value) {
        this.initialSchool = value;
    }

    public void setMbaStartDate(java.util.Date value) {
        this.mbaStartDate = value;
    }

    public void setMbaStopDate(java.util.Date value) {
        this.mbaStopDate = value;
    }

    public void setMbaEducation(String value) {
        this.mbaEducation = value;
    }

    public void setMbaMajor(String value) {
        this.mbaMajor = value;
    }

    public void setMbaSchool(String value) {
        this.mbaSchool = value;
    }

    public void setPositiveDate(java.util.Date value) {
        this.positiveDate = value;
    }

    public void setTestScorePicture(String value) {
        this.testScorePicture = value;
    }

    public void setWorkTitle(String value) {
        this.workTitle = value;
    }

    public void setWorkDepartment(String value) {
        this.workDepartment = value;
    }

    public void setContractType(String value) {
        this.contractType = value;
    }

    public void setContractStopDate(java.util.Date value) {
        this.contractStopDate = value;
    }

    public void setSeniorityDate(java.util.Date value) {
        this.seniorityDate = value;
    }

    public void setLeaveDays(String value) {
        this.leaveDays = value;
    }

    public void setExtraDays(String value) {
        this.extraDays = value;
    }

    public void setTotalDays(String value) {
        this.totalDays = value;
    }

    public void setWorkTitleWarnExcept(String value) {
        this.workTitleWarnExcept = value;
    }

    public void setRetiredWarnExcept(String value) {
        this.retiredWarnExcept = value;
    }

    public void setSort(String value) {
        this.sort = value;
    }

    @Column(name = "SORT", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getSort() {
        return this.sort;
    }

    @Column(name = "ISCHECK", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getIsCheck() {
        return this.isCheck;
    }

    @Column(name = "WORK_TITLE_DATE")
    public java.util.Date getWorkTitleDate() {
        return workTitleDate;
    }

    public void setWorkTitleDate(java.util.Date workTitleDate) {
        this.workTitleDate = workTitleDate;
    }

    @Column(name = "EMP_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getEmpName() {
        return this.empName;
    }

    @Column(name = "EMP_SEX", unique = false, nullable = true, insertable = true, updatable = true, length = 8)
    public String getEmpSex() {
        return this.empSex;
    }

    @Column(name = "EMP_NATION", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
    public String getEmpNation() {
        return this.empNation;
    }

    @Column(name = "EMP_NATIVE_PLACE", unique = false, nullable = true, insertable = true, updatable = true, length =
            200)
    public String getEmpNativePlace() {
        return this.empNativePlace;
    }

    @Column(name = "EMP_ID_CARD", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getEmpIdCard() {
        return this.empIdCard;
    }

    @Column(name = "EMP_POLITICAL", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getEmpPolitical() {
        return this.empPolitical;
    }

    @Column(name = "EMP_STATURE", unique = false, nullable = true, insertable = true, updatable = true, length = 22)
    public java.math.BigDecimal getEmpStature() {
        return this.empStature;
    }

    @Column(name = "EMP_WEIGHT", unique = false, nullable = true, insertable = true, updatable = true, length = 22)
    public java.math.BigDecimal getEmpWeight() {
        return this.empWeight;
    }

    @Column(name = "EMP_TITLE", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getEmpTitle() {
        return this.empTitle;
    }

    @Column(name = "EMP_TITLE_NUM", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getEmpTitleNum() {
        return this.empTitleNum;
    }

    @Column(name = "EMP_INSPECTION", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
    public String getEmpInspection() {
        return this.empInspection;
    }

    @Column(name = "EMP_PERMIT", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
    public String getEmpPermit() {
        return this.empPermit;
    }

    @Column(name = "EMP_PHONE", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getEmpPhone() {
        return this.empPhone;
    }

    @Column(name = "EXPECT_SALARY", unique = false, nullable = true, insertable = true, updatable = true, length = 22)
    public java.math.BigDecimal getExpectSalary() {
        return this.expectSalary;
    }

    @Column(name = "REAL_SALARY", unique = false, nullable = true, insertable = true, updatable = true, length = 22)
    public java.math.BigDecimal getRealSalary() {
        return this.realSalary;
    }

    @Column(name = "EMP_AWARDS", unique = false, nullable = true, insertable = true, updatable = true, length = 4000)
    public String getEmpAwards() {
        return this.empAwards;
    }

    @Column(name = "EMP_EVALUATION", unique = false, nullable = true, insertable = true, updatable = true, length =
            4000)
    public String getEmpEvaluation() {
        return this.empEvaluation;
    }

    @Column(name = "EMP_HOBBY", unique = false, nullable = true, insertable = true, updatable = true, length = 4000)
    public String getEmpHobby() {
        return this.empHobby;
    }

    @Column(name = "OTHER_SKILLS", unique = false, nullable = true, insertable = true, updatable = true, length = 4000)
    public String getOtherSkills() {
        return this.otherSkills;
    }

    @Column(name = "EMP_STATUS", unique = false, nullable = true, insertable = true, updatable = true, length = 2)
    public String getEmpStatus() {
        return this.empStatus;
    }

    @Column(name = "EMP_BIRTHDAY", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getEmpBirthday() {
        return this.empBirthday;
    }

    @Column(name = "FIRE_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
    public java.util.Date getFireDate() {
        return this.fireDate;
    }

    @Column(name = "EMP_PHOTO", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
    public String getEmpPhoto() {
        return this.empPhoto;
    }

    @Column(name = "INITIAL_START_DATE", unique = false, nullable = true, insertable = true, updatable = true,
            length = 7)
    public java.util.Date getInitialStartDate() {
        return this.initialStartDate;
    }

    @Column(name = "INITIAL_STOP_DATE", unique = false, nullable = true, insertable = true, updatable = true, length
            = 7)
    public java.util.Date getInitialStopDate() {
        return this.initialStopDate;
    }

    @Column(name = "INITIAL_EDUCATION", unique = false, nullable = true, insertable = true, updatable = true, length
            = 200)
    public String getInitialEducation() {
        return this.initialEducation;
    }

    @Column(name = "INITIAL_MAJOR", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
    public String getInitialMajor() {
        return this.initialMajor;
    }

    @Column(name = "INITIAL_SCHOOL", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
    public String getInitialSchool() {
        return this.initialSchool;
    }

    @Column(name = "MBA_START_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
    public java.util.Date getMbaStartDate() {
        return this.mbaStartDate;
    }

    @Column(name = "MBA_STOP_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
    public java.util.Date getMbaStopDate() {
        return this.mbaStopDate;
    }

    @Column(name = "MBA_EDUCATION", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
    public String getMbaEducation() {
        return this.mbaEducation;
    }

    @Column(name = "MBA_MAJOR", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
    public String getMbaMajor() {
        return this.mbaMajor;
    }

    @Column(name = "MBA_SCHOOL", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
    public String getMbaSchool() {
        return this.mbaSchool;
    }

    @Column(name = "POSITIVE_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
    public java.util.Date getPositiveDate() {
        return this.positiveDate;
    }

    @Column(name = "TEST_SCORE_PICTURE", unique = false, nullable = true, insertable = true, updatable = true,
            length = 200)
    public String getTestScorePicture() {
        return this.testScorePicture;
    }

    @Column(name = "WORK_TITLE", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getWorkTitle() {
        return this.workTitle;
    }

    @Column(name = "WORK_DEPARTMENT", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getWorkDepartment() {
        return this.workDepartment;
    }

    @Column(name = "CONTRACT_TYPE", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getContractType() {
        return this.contractType;
    }

    @Column(name = "CONTRACT_STOP_DATE", unique = false, nullable = true, insertable = true, updatable = true,
            length = 7)
    public java.util.Date getContractStopDate() {
        return this.contractStopDate;
    }

    @Column(name = "LEAVE_DAYS", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getLeaveDays() {
        return this.leaveDays;
    }

    @Column(name = "EXTRA_DAYS", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getExtraDays() {
        return this.extraDays;
    }

    @Column(name = "TOTAL_DAYS", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    public String getTotalDays() {
        return this.totalDays;
    }

    @Column(name = "WORK_TITLE_WARN_EXCEPT", unique = false, nullable = true, insertable = true, updatable = true,
            length = 32)
    public String getWorkTitleWarnExcept() {
        return this.workTitleWarnExcept;
    }

    @Column(name = "RETIRED_WARN_EXCEPT", unique = false, nullable = true, insertable = true, updatable = true,
            length = 32)
    public String getRetiredWarnExcept() {
        return this.retiredWarnExcept;
    }

    @Column(name = "SENIORITY_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
    public java.util.Date getSeniorityDate() {
        return this.seniorityDate;
    }

    @Column(name = "EMP_POSITION")
    public String getEmpPosition() {
        return empPosition;
    }

    public void setEmpPosition(String empPosition) {
        this.empPosition = empPosition;
    }

    @Column(name = "EMP_MATING")
    public String getEmpMating() {
        return empMating;
    }

    public void setEmpMating(String empMating) {
        this.empMating = empMating;
    }

    @Column(name = "BIRTH_DATE")
    public java.util.Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(java.util.Date birthDate) {
        this.birthDate = birthDate;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "MOBILE_PHONE")
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Column(name = "ADDRESS_PHONE")
    public String getAddressPhone() {
        return addressPhone;
    }

    public void setAddressPhone(String addressPhone) {
        this.addressPhone = addressPhone;
    }

    @Column(name = "OFFICE_PHONE")
    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    @Column(name = "CURRENT_ADDRESS")
    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    @Column(name = "FK_EMPLOYEE_CERT")
    public String getFkEmployeeCert() {
        return fkEmployeeCert;
    }

    public void setFkEmployeeCert(String fkEmployeeCert) {
        this.fkEmployeeCert = fkEmployeeCert;
    }

    @Column(name = "FK_MESSAGE_ID")
    public String getFkMessageId() {
        return fkMessageId;
    }

    public void setFkMessageId(String fkMessageId) {
        this.fkMessageId = fkMessageId;
    }

    @Column(name = "WECHAT")
    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    @Column(name = "WORK_DEPARTMENT_NAME")
    public String getWorkDepartmentName() {
        return workDepartmentName;
    }

    public void setWorkDepartmentName(String workDepartmentName) {
        this.workDepartmentName = workDepartmentName;
    }

    @Column(name = "AUTHORITY")
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Column(name = "ENTERPRISE")
    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    @Column(name = "FREELANCE_JOBS")
    public String getFreelanceJobs() {
        return freelanceJobs;
    }

    public void setFreelanceJobs(String freelanceJobs) {
        this.freelanceJobs = freelanceJobs;
    }

    @Column(name = "INITIAL_DEGREE")
    public String getInitial_degree() {
        return initial_degree;
    }

    public void setInitial_degree(String initial_degree) {
        this.initial_degree = initial_degree;
    }

    @Column(name = "MBA_DEGREE")
    public String getMba_degree() {
        return mba_degree;
    }

    public void setMba_degree(String mba_degree) {
        this.mba_degree = mba_degree;
    }

    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "JOIN_WORK_DATE")
    public java.util.Date getJoinWorkDate() {
        return joinWorkDate;
    }

    public void setJoinWorkDate(java.util.Date joinWorkDate) {
        this.joinWorkDate = joinWorkDate;
    }

    @Column(name = "INTO_WORK_DATE")
    public java.util.Date getIntoWorkDate() {
        return intoWorkDate;
    }

    public void setIntoWorkDate(java.util.Date intoWorkDate) {
        this.intoWorkDate = intoWorkDate;
    }

    @Column(name = "JOIN_PARTY_DATE")
    public java.util.Date getJoinPartyDate() {
        return joinPartyDate;
    }

    public void setJoinPartyDate(java.util.Date joinPartyDate) {
        this.joinPartyDate = joinPartyDate;
    }

    @Column(name = "MAN_SOURCE")
    public String getManSource() {
        return manSource;
    }

    public void setManSource(String manSource) {
        this.manSource = manSource;
    }

    @Column(name = "FUNDING_SHAPE")
    public String getFundingShape() {
        return fundingShape;
    }

    public void setFundingShape(String fundingShape) {
        this.fundingShape = fundingShape;
    }

    @Column(name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Column(name = "GRADE")
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Column(name = "FK_EMPLOYEE")
    public String getFkEmployee() {
        return fkEmployee;
    }

    public void setFkEmployee(String fkEmployee) {
        this.fkEmployee = fkEmployee;
    }

    @Column(name = "EMP_TITLE_QT")
    public String getEmpTitleQt() {
        return empTitleQt;
    }

    public void setEmpTitleQt(String empTitleQt) {
        this.empTitleQt = empTitleQt;
    }


    @Column(name = "POSITION_TITLE_TYPE")
    public String getPositionTitleType() {
        return positionTitleType;
    }

    public void setPositionTitleType(String positionTitleType) {
        this.positionTitleType = positionTitleType;
    }

    @Column(name = "POSITION_TITLE_STOP_DATE")
    public Date getPositionTitleStopDate() {
        return positionTitleStopDate;
    }

    public void setPositionTitleStopDate(Date positionTitleStopDate) {
        this.positionTitleStopDate = positionTitleStopDate;
    }
}
