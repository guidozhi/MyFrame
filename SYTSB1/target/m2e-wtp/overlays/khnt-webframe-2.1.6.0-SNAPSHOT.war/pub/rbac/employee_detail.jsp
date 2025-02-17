<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
<%@ include file="/k/kui-base-form.jsp"%>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<script type="text/javascript" src="pub/photograph/js/photograph.js"></script>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript">
    
	var pageStatus = "${param.pageStatus}";
	var p;
	//载入layout。初始化页面
	function initPage() {
		$("#layout1").ligerLayout({
			leftWidth : 220,
			sapce : 5,
			allowLeftCollapse : false,
			allowRightCollapse : false
		});
	}
	$(function() {
		
		$("#formObj").initForm({
			//取得图片
			getSuccess : function(res) {
				if (res.data && res.data.idNo) {
					p.setIdn(res.data.idNo);
				} else {
					if (res.data && res.data.picture) {
						p.setPictureByPath(res.data.picture);
					}
				}
			}
		});
		//初始化页面
		initPage();
		//照片设置
		p = new photogragh("photocontainer", function(val) {
			$("#picture").val(val);
		});
		$('input[name="idNo"]').change(function() {
			getidn($(this).val())
		})
	});
	function getidn(idn) {
		p.setIdn(idn);//根据身份证号取图片路径
		var idCard = idn;
		if (idCard != "") {
			var result = $.kh.validte(idCard);
			if (result == false) {
				$.ligerDialog.error("身份证号码位数不对!");
				$("#idNo").val("");
				$("#birthDate").val("");
				$("#gender-txt").val("");
			} else if (!result.msg == "") {
				$.ligerDialog.error(result.msg);
				$("#idNo").val("");
				$("#birthDate").val("");
				$("#gender-txt").val("");
			} else {
				var birthday = result.idInfo.birthday;
				var xb = result.idInfo.gender;
				//设置出生日期
				$("#birthDate").val(birthday);
				//设置性别
				$("#gender-txt").ligerRadioGroup().setValue(xb);
			}
		} else {
			$("#birthDate").val("");
			$("#gender-txt").val("");
		}
	}
	function showOrg(val, e, srcObj) {
		selectUnitOrUser(0, 0, null, null, function(data){
			$("#orgName").val(data.name);
			$("#orgId").val(data.code);
		},"1");
	}
	function showPositions(val, e, srcObj) {
		selectUnitOrUser(4, 1, null, null, function(data){
			$("#positionNames").val(data.name);
			$("#positionIds").val(data.code);
		});
	}
</script>
</head>
<body>
	<form name="formObj" id="formObj" method="post"
		action="rbac/employee/save.do"
		getAction="rbac/employee/detail.do?id=${param.id}">
			<table cellpadding="3" cellspacing="0" class="l-detail-table">
				<input type="hidden" name="id" />
				<input type="hidden" name="polstatus" value="0" />
				<input type="hidden" name="posOrgIds" />
				<input type="hidden" name="posOrgNames" />
				<tr>
					<td class="l-t-td-left">员工号：</td>
			 		<td class="l-t-td-right">
			 			<input name="code" type="text" ltype='text' validate="{required:true,maxlength:20}"  />
					</td>
					<td class="l-t-td-left">姓名：</td>
			 		<td class="l-t-td-right">
			 			<input name="name" type="text" ltype='text' validate="{required:true,maxlength:20}"  />
					</td>
                    <td class="l-t-td-left" rowspan="6">照片：</td>
	                <td class="l-t-td-right" rowspan="6" style="text-align: center" id="photocontainer">
	                <input type="hidden" name="picture" id="picture"   />
	                </td>
				</tr>
				<tr>
					<td class="l-t-td-left">所属部门：</td>
			 		<td class="l-t-td-right">
			 			<input name="org.orgName" id="orgName" type="text" ltype='text' readonly validate="{required:true,maxlength:128}"  
						ligerui="{iconItems:[{icon:'orgAdd',click:function(val,e,srcObj){showOrg(val,e,srcObj)}}]}"/>
						<input name="org.id" id="orgId" type="hidden"/>
					</td>
					<td class="l-t-td-left">岗位：</td>
					<td class="l-t-td-right">
						<input name="positionNames" id="positionNames" type="text" ltype='text' readonly validate="{required:true,maxlength:512}"  
						ligerui="{iconItems:[{icon:'role',click:function(val,e,srcObj){showPositions(val,e,srcObj)}}]}"/>
						<input name="positionIds" id="positionIds" type="hidden"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">别名(昵称)：</td>
			 		<td class="l-t-td-right">
			 			<input name="otherName" type="text" ltype='text' validate="{maxlength:20}"  />
					</td>
					<td class="l-t-td-left">身份证号码：</td>
					<td class="l-t-td-right">
						<input name="idNo" type="text" ltype='text' validate="{required:true,idno:true,maxlength:18}" />
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">性别：</td>
					<td class="l-t-td-right">
					<u:combo ltype="radioGroup" name="gender" code="pub_xb" validate="required:true"  />
					</td>
					<td class="l-t-td-left">出生年月：</td>
					<td class="l-t-td-right">
					<input name="birthDate" id="birthDate" type="text" ltype='date' validate="{required:true}" ligerui="{format:'yyyy-MM-dd'}" /></td>
				</tr>
				<tr>
					<td class="l-t-td-left">籍贯：</td>
					<td class="l-t-td-right">
						<input name="nativePlace" type="text" ltype='text' validate="{maxlength:128}" />
					</td>
					<td class="l-t-td-left">户口所在地：</td>
					<td class="l-t-td-right">
						<input name="birthPlace" type="text" ltype='text' validate="{maxlength:128}" />
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">家庭住址：</td>
					<td class="l-t-td-right" >
						<input name="homePlace" type="text" ltype='text' validate="{maxlength:128}" />
					</td>
					<td class="l-t-td-left">通讯地址：</td>
					<td class="l-t-td-right">
						<input name="currentPlace" type="text" ltype='text' validate="{maxlength:128}" />
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">民族：</td>
					<td class="l-t-td-right">
						<u:combo name="nation" code="pub_mz"></u:combo>
					</td>
					<td class="l-t-td-left">政治面貌：</td>
					<td class="l-t-td-right">
						<u:combo name="empGov" code="pub_zzmm"></u:combo>
					</td>
					<td class="l-t-td-left"></td>
					<td class="l-t-td-right">
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">婚姻状况：</td>
					<td class="l-t-td-right">
						<u:combo name="maritalStatus" code="pub_hy"></u:combo>
					</td>
					<td class="l-t-td-left">学历：</td>
					<td class="l-t-td-right">
						<u:combo name="education" code="pub_xl"></u:combo>
					</td>
				    <td class="l-t-td-left">学位：</td>
					<td class="l-t-td-right">
						<u:combo name="degree" code="pub_xw"></u:combo>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">毕业学校：</td>
					<td class="l-t-td-right">
						<input name="schoolName" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
					<td class="l-t-td-left">毕业时间：</td>
					<td class="l-t-td-right">
						<input name="graduateTime"  type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" /></td>
				    <td class="l-t-td-left">专业：</td>
					<td class="l-t-td-right">
						<input name="major" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">英语等级：</td>
					<td class="l-t-td-right">
						<input name="englishLevel"  type="text" ltype='text' validate="{maxlength:32}" /></td>
				    <td class="l-t-td-left">电脑等级：</td>
					<td class="l-t-td-right">
						<input name="pcLevel" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
					<td class="l-t-td-left">邮编：</td>
					<td class="l-t-td-right">
						<input name="postCode" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">电子邮件：</td>
					<td class="l-t-td-right">
						<input name="email"  type="text" ltype='text' validate="{email:true,maxlength:32}" /></td>
				    <td class="l-t-td-left">qq：</td>
					<td class="l-t-td-right">
						<input name="qq" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
					<td class="l-t-td-left">微信：</td>
					<td class="l-t-td-right">
						<input name="weixin" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">微博：</td>
					<td class="l-t-td-right">
						<input name="weibo"  type="text" ltype='text' validate="{maxlength:32}" /></td>
				    <td class="l-t-td-left">电话：</td>
					<td class="l-t-td-right">
						<input name="tel" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
					<td class="l-t-td-left">移动电话：</td>
					<td class="l-t-td-right">
						<input name="mobileTel" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">家庭电话：</td>
					<td class="l-t-td-right">
						<input name="homeTel"  type="text" ltype='text' validate="{maxlength:32}" /></td>
				    <td class="l-t-td-left">办公电话：</td>
					<td class="l-t-td-right">
						<input name="officeTel" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
					<td class="l-t-td-left">短号：</td>
					<td class="l-t-td-right">
						<input name="vtel" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">体重：</td>
					<td class="l-t-td-right">
						<input name="weight"  type="text" ltype='text' validate="{maxlength:32}" /></td>
				    <td class="l-t-td-left">身高：</td>
					<td class="l-t-td-right">
						<input name="height" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
					<td class="l-t-td-left">健康状况：</td>
					<td class="l-t-td-right">
						<input name="health" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">宗教信仰：</td>
					<td class="l-t-td-right">
						<input name="faith"  type="text" ltype='text' validate="{maxlength:32}" /></td>
				    <td class="l-t-td-left">血型：</td>
					<td class="l-t-td-right">
						<input name="bloodtype" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
					<td class="l-t-td-left">员工类型：</td>
					<td class="l-t-td-right">
						<input name="empType" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">员工状态：</td>
					<td class="l-t-td-right">
						<input name="empStatus"  type="text" ltype='text' validate="{maxlength:32}" /></td>
				    <td class="l-t-td-left">银行类型：</td>
					<td class="l-t-td-right">
						<input name="bankType" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
					<td class="l-t-td-left">银行账号：</td>
					<td class="l-t-td-right">
						<input name="bankAccount" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">紧急联系人：</td>
					<td class="l-t-td-right">
						<input name="emcPerson"  type="text" ltype='text' validate="{maxlength:32}" /></td>
				    <td class="l-t-td-left">紧急联系人电话：</td>
					<td class="l-t-td-right">
						<input name="emcTel" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
					<td class="l-t-td-left">紧急联系人与本人关系：</td>
					<td class="l-t-td-right">
						<input name="emcRelate" type="text" ltype="text" validate="{maxlength:32}"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">进入本单位日期：</td>
					<td class="l-t-td-right">
						<input name="comeDate"  type="text" ltype='date' /></td>
				    <td class="l-t-td-left">转正日期：</td>
					<td class="l-t-td-right">
						<input name="empDate"  type="text" ltype='date' />
					</td>
					<td class="l-t-td-left">离职，辞退日期：</td>
					<td class="l-t-td-right">
						<input name="outDate"  type="text" ltype='date' />
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">退休日期：</td>
					<td class="l-t-td-right">
						<input name="retireDate"  type="text" ltype='date' /></td>
				    <td class="l-t-td-left">有无驾照：</td>
					<td class="l-t-td-right">
						<u:combo name="isDriver" code="pub_yw"></u:combo>
					</td>
					<td class="l-t-td-left">预计转正日期：</td>
					<td class="l-t-td-right">
						<input name="expEmpDate"  type="text" ltype='date' />
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">是否购买社保：</td>
					<td class="l-t-td-right">
						<u:combo name="socialInsurance" code="sys_sf"></u:combo>
				    <td class="l-t-td-left">是否购买住房公积金：</td>
					<td class="l-t-td-right">
						<u:combo name="roomFund" code="sys_sf"></u:combo>
					</td>
					<td class="l-t-td-left">是否考勤：</td>
					<td class="l-t-td-right">
						<u:combo name="attence" code="sys_sf"></u:combo>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">备注：</td>
					<td class="l-t-td-right" colspan="5">
						<textarea rows="5" cols="20" name="remark" validate="{maxlength:512}"></textarea>
					</td>
				</tr>
			</table>
	</form>
</body>
</html>


