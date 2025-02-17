<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
<title>四川省特种设备检验研究院 简历在线填报系统</title>

</head>
<body>

	<form id="formObj" action="" getAction="employeeBaseAction/detailBasic.do?id=${param.id}">
		<input type="hidden" id="empId" name="id" />
		<input type="hidden" id="fkEmployee" name="fkEmployee" />
		<input type="hidden" id="empPhoto" name="empPhoto" />
		<input type="hidden" id="isCheck" name="isCheck" />
		<input type="hidden" id="empStatus" name="empStatus" />
		<input type="hidden" id="createDate" name="createDate" />
		<input type="hidden" id="seniorityDate" name="seniorityDate" />
		<input type="hidden" id="leaveDays" name="leaveDays" />
		<input type="hidden" id="extraDays" name="extraDays" />
		<input type="hidden" id="totalDays" name="totalDays" />
		<input type="hidden" id="workTitleWarnExcept" name="workTitleWarnExcept" />
		<input type="hidden" id="retiredWarnExcept" name="retiredWarnExcept" />
		<input type="hidden" id="sort" name="sort" />
		<h1 class="l-label" style="padding:5mm 0 2mm 0;font-family:微软雅黑;font-size:5mm;">四 川 省 特 种 设 备 检 验 研 究 院 简 历 在 线 填 报 系 统 </h1>
		<h1 class="l-label" style="padding:5mm 0 2mm 0;font-family:微软雅黑;font-size:6mm;">个&nbsp;人&nbsp;简&nbsp;历&nbsp;</h1><div style="height:2px">&nbsp;</div>
	   <div class="fieldset-caption">
            <span>基本信息</span>
        </div>
	 	<table border="1" cellpadding="3" cellspacing="0" width=""
				class="l-detail-table" id="table1">
					 <tr>
						<td class="l-t-td-left">姓名</td>
						<td class="l-t-td-right"><input type="text" ltype="text" name="empName" validate="{required:true,maxlength:32}" /></td>
						<td class="l-t-td-left">性别</td>
						<td class="l-t-td-right"><u:combo  name="empSex" code="BASE_SEX" validate="required:true"></u:combo></td>
						<td class="l-t-td-right" rowspan="10" align="center" width="190px">
						<div><img id="image" src="" style="display: none;width:180px;height: 220px"></img></div>
						<c:if test='${param.pageStatus!="detail" }'>
						<span id="onefileDIV">
						         <a class="l-button" id="onefileBtn"><span
									class="l-button-main"><span class="l-button-text">选择图片</span>
								</span>
								</a>
						</span>
						</c:if></td>
					</tr> 
					<tr>
						<td class="l-t-td-left">民族</td>
						<td class="l-t-td-right"><input type="text" ltype="text" name="empNation" validate="{maxlength:50}"  /></td>
						<td class="l-t-td-left">籍贯</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="empNativePlace" validate="{maxlength:200}"/></td>
					</tr>
					<tr>
						<td class="l-t-td-left">身份证号</td>
						<td class="l-t-td-right"><input type="text" ltype="text" id="empIdCard" name="empIdCard" validate="{required:true,idno:true}" onblur="ageAndBirthday()"/></td>
						<td class="l-t-td-left">政治面貌</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="empPolitical" validate="{maxlength:32}"/></td>
					</tr>
					<tr>
						<td class="l-t-td-left">身高(cm)</td>
						<td class="l-t-td-right"><input type="text" ltype="spinner" name="empStature" ligerui="{type:'int',isNegative:false}" /></td>
						<td class="l-t-td-left">体重(kg)</td>
						<td class="l-t-td-right"><input type="text"  ltype="spinner"  name="empWeight" ligerui="{type:'int',isNegative:false}" /></td>
					</tr>
					<tr>
						<td class="l-t-td-left">出生日期</td>
						<td class="l-t-td-right"><input type="text" ltype="text" id="birthDate1" name="birthDate1" readonly="readonly" /></td>
						<td class="l-t-td-left">年龄</td>
						<td class="l-t-td-right"><input type="text"  ltype="text"  id="age" name="age" readonly="readonly" /></td>
					</tr>
					<tr>
						<td class="l-t-td-left">职称</td>
						<td class="l-t-td-right"><u:combo  name="empTitle" code="RLZY_RYZC" validate="required:false"></u:combo></td>
						<td class="l-t-td-left">职称证书编号</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="empTitleNum" id="empTitleNum" validate="{maxlength:32}"/></td>
					</tr>
					<tr>
						<td class="l-t-td-left">职称(其他)</td>
						<td class="l-t-td-right" colspan="3"><input type="text" ltype="text" name="empTitleQt" validate="{maxlength:32}" /></td>
						
					</tr>
					<tr>
						<td class="l-t-td-left">检验资格</td>
						<td class="l-t-td-right"><input type="text" ltype="text" name="empInspection"  validate="{maxlength:200}"/></td>
						<td class="l-t-td-left">持证情况</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="empPermit" validate="{maxlength:200}"/></td>
					</tr>
					<tr>
						<td class="l-t-td-left">联系电话</td>
						<td class="l-t-td-right"><input type="text" ltype="text" name="empPhone" id="empPhone"  validate="{maxlength:11}"/></td>
						<td class="l-t-td-left">期望工资(月)</td>
						<td class="l-t-td-right"><input type="text" ltype="spinner"  name="expectSalary" ligerui="{type:'int',isNegative:false}" /></td>
					</tr>
					<tr>
						<td class="l-t-td-left">参加工作时间</td>
						<td class="l-t-td-right"><input type="text" ltype="date" name="joinWorkDate"  /></td>
						<td class="l-t-td-left">入党时间</td>
						<td class="l-t-td-right"><input type="text" ltype="date"  name="joinPartyDate"  /></td>
					</tr>
					<tr>
						<td class="l-t-td-left">应聘岗位</td>
						<td class="l-t-td-right"colspan="4"><input type="text" ltype="text" name="freelanceJobs"  validate="{maxlength:300}"/></td>
					</tr>
					</table>
				<div class="fieldset-caption">
                    <span>教育背景</span>
                </div>
					<table border="1" cellpadding="3" cellspacing="0" width=""
						class="l-detail-table" id="table2">
					<tr>
						<td class="l-t-td-left">开始日期</td>
						<td class="l-t-td-right"><input type="text" ltype="date"  name="initialStartDate" /></td>
						<td class="l-t-td-left">结束日期</td>
						<td class="l-t-td-right"><input type="text" ltype="date"  name="initialStopDate" /></td>
					</tr>
					<tr>
						<td class="l-t-td-left">初始学历</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="initialEducation" validate="{maxlength:200}"/></td>
						<td class="l-t-td-left">初始专业</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="initialMajor" validate="{maxlength:200}" /></td>
					</tr>
				<tr>
				        <td class="l-t-td-left">初始学位</td>
						<td class="l-t-td-right" ><input type="text" ltype="text"  name="initial_degree" validate="{maxlength:300}"/></td>
						<td class="l-t-td-left">初始毕业院校</td>
						<td class="l-t-td-right" ><input type="text" ltype="text"  name="initialSchool" validate="{maxlength:200}"/></td>
						
					</tr>
					<tr>
						<td class="l-t-td-left">开始日期</td>
						<td class="l-t-td-right"><input type="text" ltype="date"  name="mbaStartDate" /></td>
						<td class="l-t-td-left">结束日期</td>
						<td class="l-t-td-right"><input type="text" ltype="date"  name="mbaStopDate"/></td>
					</tr>
					<tr>
						<td class="l-t-td-left">在职学历</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="mbaEducation" validate="{maxlength:200}"/></td>
						<td class="l-t-td-left">在职专业</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="mbaMajor" validate="{maxlength:200}"/></td>
					</tr>
				<tr>
				       <td class="l-t-td-left">在职学位</td>
						<td class="l-t-td-right" ><input type="text" ltype="text"  name="mba_degree" validate="{maxlength:300}"/></td>
						<td class="l-t-td-left">在院毕业院校</td>
						<td class="l-t-td-right" ><input type="text" ltype="text"  name="mbaSchool" validate="{maxlength:200}"/></td>
						
					</tr>
		
					
					</table> 
			<div class="fieldset-caption">
                <span>实习及工作经历</span>
            </div>
					<div id="work" style="width: 100%">
					
					</div>
				<div class="fieldset-caption">
                    <span>获奖情况</span>
                </div>
					<table border="1" cellpadding="3" cellspacing="0" width=""
						class="l-detail-table" id="table3">
						<tr>
						<td ><textarea name="empAwards" rows="4" cols="25" class="l-textarea" validate="{maxlength:4000}"></textarea></td>
					</tr>
					
					
					</table>
				<div class="fieldset-caption">
                    <span>个人评价</span>
                </div>
					<table border="1" cellpadding="3" cellspacing="0" width=""
						class="l-detail-table" id="table4">
						<tr>
						<td ><textarea name="empEvaluation" rows="4" cols="25" class="l-textarea" validate="{maxlength:4000}"></textarea></td>
					</tr>
					
					
					</table>
				<div class="fieldset-caption">
                    <span>兴趣爱好</span>
                </div>
					<table border="1" cellpadding="3" cellspacing="0" width=""
						class="l-detail-table" id="table5">
						<tr >
						<td ><textarea  name="empHobby" id="remark" rows="4" cols="25" class="l-textarea" validate="{maxlength:4000}" ></textarea></td>
					     </tr>
					
					
					</table>
				<div class="fieldset-caption">
                    <span>其他能力</span>
                </div>
					<table border="1" cellpadding="3" cellspacing="0" width=""
						class="l-detail-table" id="table6">
						<tr>
						<td ><textarea name="otherSkills" id="remark" rows="4" cols="25" class="l-textarea" validate="{maxlength:4000}"></textarea></td>
					</tr>
					
					
					</table>
			<c:if test='${param.permission!="apply" }'>
				<div class="fieldset-caption" id="div_work">
                    <span>工作信息</span>
                </div>
					<table border="1" cellpadding="3" cellspacing="0" width="" id="table7"
						class="l-detail-table">
					
			        <tr>
						<td class="l-t-td-left">职务</td>
						<td class="l-t-td-right"><input type="text" ltype="text" name="workTitle"  /></td>
						<td class="l-t-td-left">职务到期时间</td>
						<td class="l-t-td-right"><input type="text" ltype="date"  name="workTitleDate" /></td>
					</tr>
					 <tr>
						<td class="l-t-td-left">人员来源</td>
						<td class="l-t-td-right"><u:combo  name="manSource" code="TJY2_RL_MANSOURCE"></u:combo></td>
						<td class="l-t-td-left">经费形式</td>
						<td class="l-t-td-right"><u:combo  name="fundingShape" code="TJY2_RL_FUNDINGSHAPE"></u:combo></td>
					</tr>
					 <tr>
						<td class="l-t-td-left">岗位</td>
						<td class="l-t-td-right"><u:combo  name="position" code="TJY2_RL_POSITION" ></u:combo></td>
						<td class="l-t-td-left">等级</td>
						<td class="l-t-td-right"><u:combo  name="grade" code="TJY2_RL_LEVEL" ></u:combo></td>
					</tr>
					 <tr>
						<td class="l-t-td-left">所属部门</td>
						<td class="l-t-td-right"><input type="hidden"   name="workDepartment"  id="workDepartment"/>
						<input  validate="{required:true,maxlength:100}" ltype="text"  name="workDepartmentName" id="workDepartmentName" type="text" onclick="chooseOrg()" ligerui="{iconItems:[{icon:'org',click:chooseOrg}]}"/></td>
					    <td class="l-t-td-left">员工身份</td>
						<td class="l-t-td-right"><u:combo  name="empPosition" code="pub_user_type" validate="required:true"></u:combo></td>
					</tr>
		           <tr>
						<td class="l-t-td-left">是否已婚</td>
						<td class="l-t-td-right"><u:combo  name="empMating" code="ba_sf" ></u:combo></td>
						<td class="l-t-td-left">邮箱</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="email" /></td>
					</tr>
					 <tr>
						<td class="l-t-td-left">手机</td>
						<td class="l-t-td-right"><input type="text" ltype="text" name="mobilePhone" validate="{maxlength:11}" /></td>
						<td class="l-t-td-left">住址电话</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="addressPhone" validate="{maxlength:20}"/></td>
					</tr>
					<tr>
						<td class="l-t-td-left">办公电话</td>
						<td class="l-t-td-right"><input type="text" ltype="text" name="officePhone"  validate="{maxlength:20}"/></td>
						<td class="l-t-td-left">微信账号</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="wechat" validate="{maxlength:32}"/></td>
					</tr>
					<tr>
					    <td class="l-t-td-left">企业账号</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="enterprise" validate="{maxlength:32}"/></td>
					    <td class="l-t-td-left">现居地址</td>
						<td class="l-t-td-right"><input type="text" ltype="text"  name="currentAddress" validate="{maxlength:200}"/></td>
					</tr>
					<tr>
						<td class="l-t-td-left">入院日期</td>
						<td class="l-t-td-right"colspan="3">
						<input name="intoWorkDate" id="intoWorkDate" type="text" ltype='date' ligerui="{initValue:'',format:'yyyy-MM-dd'}"/></td>
					</tr>
					</table>
					<!-- 	<div class="fieldset-caption">
                    <span>打印机设置</span>
                        </div>
					<table border="1" cellpadding="3" cellspacing="0" width=""
						class="l-detail-table">
						<tr>
					<td class="l-t-td-left">报告打印机名称：</td>
					<td class="l-t-td-right"><input name="printer_name" id="printer_name" type="text" ltype="text" validate="{maxlength:100}"/></td>
					<td class="l-t-td-left">标签打印机名称：</td>
					<td class="l-t-td-right"><input name="printer_name_tags" id="printer_name_tags"  type="text" ltype="text" validate="{maxlength:100}"/></td>
				        </tr>
					</table>
					<div class="fieldset-caption">
                    <span>电子签名</span>
                    </div>
					<table cellpadding="3" cellspacing="0" class="l-detail-table">
				    <tr>
					<td class="l-t-td-left">电子签名：</td>
					<td class="l-t-td-right">
						<input name="uploadFiles" type="hidden" id="uploadFiles" validate="{maxlength:1000}" />
						<p id="procufilesDIV2">
							<a class="l-button" id="procufilesBtn2">
								<span class="l-button-main"><span class="l-button-text">选择文件</span></span>
							</a>
						</p>
				    	<div class="l-upload-ok-list">
							<ul id="procufiles4"></ul>
						</div>
					</td>
				</tr>
			</table> -->
					</c:if>
		</form>
</body>
</html>