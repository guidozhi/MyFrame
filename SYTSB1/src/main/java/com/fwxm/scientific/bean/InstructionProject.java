package com.fwxm.scientific.bean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.khnt.core.crud.bean.BaseEntity;

import java.util.Date;

/*******************************************************************************
 * 
 * <p>
 *  
 * </p>
 * 
 * @ClassName InstructionProject
 * @JDK 1.7
 * @author CODER_V3.0
 * @date 2018-01-24 10:48:10
 */
@Entity
@Table(name = "TJY2_INSTRUCTION_PROJECT")
public class InstructionProject implements BaseEntity {
	private static final long serialVersionUID = 1L;
	
	private String id;//
	private String projectName;//文件名称
	private String projectNum;//文件编号
	private String content;//修改内容
	private String reason;//修改理由
	private Date createDate;//编制时间
	private String createId;//编制人id
	private String createMan;//编制人
	private Date auditDate;//审核时间
	private String auditId;//审核人id
	private String auditMan;//审核人
	private Date signDate;//批准时间
	private String signId;//批准人id
	private String signMan;//批准人
	private String status;//状态
	private String projectNo;//编号
	private String auditOpinion;
	private String signOpinion;
	private String quality_file_id;
	private String isReturn;//是否退回；0：否；1：是
	private String type;//制定，修订
	
	
	@Column(name="TYPE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name="IS_RETURN")
	public String getIsReturn() {
		return isReturn;
	}
	public void setIsReturn(String isReturn) {
		this.isReturn = isReturn;
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

	@Column(name="PROJECT_NAME")
	public String getProjectName(){
		return projectName;
	}
		
	public void setProjectName(String projectName){
		this.projectName=projectName;
	}

	@Column(name="PROJECT_NUM")
	public String getProjectNum(){
		return projectNum;
	}
		
	public void setProjectNum(String projectNum){
		this.projectNum=projectNum;
	}

	@Column(name="CONTENT")
	public String getContent(){
		return content;
	}
		
	public void setContent(String content){
		this.content=content;
	}

	@Column(name="REASON")
	public String getReason(){
		return reason;
	}
		
	public void setReason(String reason){
		this.reason=reason;
	}

	@Column(name="CREATE_DATE")
	public Date getCreateDate(){
		return createDate;
	}
		
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}

	@Column(name="CREATE_ID")
	public String getCreateId(){
		return createId;
	}
		
	public void setCreateId(String createId){
		this.createId=createId;
	}

	@Column(name="CREATE_MAN")
	public String getCreateMan(){
		return createMan;
	}
		
	public void setCreateMan(String createMan){
		this.createMan=createMan;
	}

	@Column(name="AUDIT_DATE")
	public Date getAuditDate(){
		return auditDate;
	}
		
	public void setAuditDate(Date auditDate){
		this.auditDate=auditDate;
	}

	@Column(name="AUDIT_ID")
	public String getAuditId(){
		return auditId;
	}
		
	public void setAuditId(String auditId){
		this.auditId=auditId;
	}

	@Column(name="AUDIT_MAN")
	public String getAuditMan(){
		return auditMan;
	}
		
	public void setAuditMan(String auditMan){
		this.auditMan=auditMan;
	}

	@Column(name="SIGN_DATE")
	public Date getSignDate(){
		return signDate;
	}
		
	public void setSignDate(Date signDate){
		this.signDate=signDate;
	}

	@Column(name="SIGN_ID")
	public String getSignId(){
		return signId;
	}
		
	public void setSignId(String signId){
		this.signId=signId;
	}

	@Column(name="SIGN_MAN")
	public String getSignMan(){
		return signMan;
	}
		
	public void setSignMan(String signMan){
		this.signMan=signMan;
	}

	@Column(name="STATUS")
	public String getStatus(){
		return status;
	}
		
	public void setStatus(String status){
		this.status=status;
	}

	@Column(name="PROJECT_NO")
	public String getProjectNo(){
		return projectNo;
	}
		
	public void setProjectNo(String projectNo){
		this.projectNo=projectNo;
	}

	
	
	@Column(name="AUDIT_OPINION")
	public String getAuditOpinion() {
		return auditOpinion;
	}
	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}
	@Column(name="SIGN_OPINION")
	public String getSignOpinion() {
		return signOpinion;
	}
	public void setSignOpinion(String signOpinion) {
		this.signOpinion = signOpinion;
	}
	
	
	public String getQuality_file_id() {
		return quality_file_id;
	}
	public void setQuality_file_id(String quality_file_id) {
		this.quality_file_id = quality_file_id;
	}
	/***************************************************************************
	 * bean说明
	 * 
	 * @return
	 */
	public String toString() {
		return "TJY2_INSTRUCTION_PROJECT:ID="+id;

	}
}
