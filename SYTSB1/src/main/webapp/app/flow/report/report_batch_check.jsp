<%@page import="util.TS_Util"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.Date"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.khnt.utils.DateUtil"%>
<%@page import="com.khnt.utils.StringUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%
	String type = request.getParameter("type");	// 操作类别（04：报告审核 05：报告签发）
	String op_name = "04".equals(type)?"审核":"签发";
	String device_type = request.getParameter("device_type");	// 设备类别（用来判断是否默认审核日期为编制日期加3天、签发日期为审核日期加1天）
	String device_sort_code = request.getParameter("device_sort_code");	// 设备类别
	String check_flow = request.getParameter("check_flow");	// 报告审批流程（0：二级审核 1：一级审核）
	String enter_time = request.getParameter("enter_time");	// 编制日期
	String check_time = request.getParameter("check_time");	// 审核日期
	String org_id = request.getParameter("org_id");	// 检验部门ID
	
	CurrentSessionUser user = SecurityUtil.getSecurityUser();
	String cur_user_name = user.getSysUser().getName();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

		<title></title>
		<!-- 每个页面引入，页面编码、引入js，页面标题 -->
		<%@include file="/k/kui-base-form.jsp"%>
		
		<head>
		<script type="text/javascript">
		
		
			$(function() {
			
				$("#form1").initForm({ //参数设置示例
					toolbar : [ {
						text : '<%=op_name%>',
						//id : 'save',
						icon : 'save',
						click : save
					}
				
					, 
					{
						text : '关闭',
						//id : 'close',
						icon : 'cancel',
						click : close
					} ],
					getSuccess : function(res) {
						
					
						
						
					}
				});
				<%
					if("04".equals(type)){	// 报告审核，默认审核日期
						String check_date = "";
						if("3".equals(device_type) || "4".equals(device_type)){
							Calendar calendar = Calendar.getInstance();
							if(StringUtil.isNotEmpty(enter_time)){
								calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd",enter_time));	// 编制日期
							}else{
								calendar.setTime(new Date());	// 当前日期
							}
							calendar.add(Calendar.DATE, 3);	//（2014-10-08要求，审核日期=编制日期+3天）
							//calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
							check_date = DateUtil.getDate(calendar.getTime());
						}else{
							check_date = DateUtil.getDateTime("yyyy-MM-dd", new Date());	// 非电梯、起重机类机电设备审核日期默认当前日期
						}
						
						%>
						$('#op_time').val('<%=check_date%>');
						<%
					}else if("05".equals(type)){
						String confirm_date = "";
						if("3".equals(device_type) || "4".equals(device_type)){
							Calendar calendar = Calendar.getInstance();
							if(StringUtil.isNotEmpty(check_time)){
								calendar.setTime(DateUtil.convertStringToDate("yyyy-MM-dd",check_time));	// 审核日期
							}else{
								calendar.setTime(new Date());	// 审核日期
							}
							
							calendar.add(Calendar.DATE, 1);	//（2014-10-08要求，签发日期=审核日期+1天）	
							//calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
							confirm_date = DateUtil.getDate(calendar.getTime());
						}else{
							confirm_date = DateUtil.getDateTime("yyyy-MM-dd", new Date());	// 非电梯、起重机类机电设备签发日期默认当前日期
						}
						%>
						$('#op_time').val('<%=confirm_date%>');
						<%
					}
				%>
				//$('#op_time').val(currentTime());（2014-10-08要求，审核日期=编制日期+3天，签发日期=审核日期+1天）
				//document.getElementById("curDate").innerHTML=currentTime();
			})
		
		function close(url)
			{	
				 api.close();
				 api.data.window.showBB();
			}
			function save(url)
			{
				
					var  Type= ${param.type};
				
					var is_pass =$('#revise_conclusion').ligerGetRadioGroupManager().getValue();
					var op_time = $('#op_time').val();
					op_time = op_time.replace(/-/g,"/");
					var op_date = new Date(op_time);
					
					
					
					if(is_pass=="1"){
						if(${param.type}=="04" && ${param.check_flow}=="0"){							
							if("3"=="<%=device_type %>"){
								var enter_date = new Date('<%=enter_time%>'.replace(/-/g,"/"));
								if(op_date<enter_date){
									alert("亲，审核日期不能早于编制日期哦，请重新选择审核日期！");
									return;
								}	
							}
						}
						
						if(${param.type}=="05"){
							var check_date = new Date('<%=check_time%>'.replace(/-/g,"/"));
							if(op_date<check_date){
								alert("亲，签发日期不能早于审核日期哦，请重新选择签发日期！");
								return;
							}	
						}	
					}
					if(is_pass=="2"){
						if($('#revise_remark').val()==null||$('#revise_remark').val()==undefined||$('#revise_remark').val()==""){
							 top.$.dialog.notice({content:'请填写退回原因！'});
							return;
						}
						
					}
					
				//判断是否是报告签发 报告签发回退分2步 一步回退上一步，一步回退到报告起草
					if(Type=="05"){
						var backType=$('#backStep').ligerGetRadioGroupManager().getValue();
						url = "report/batch/flow_batchCheck.do?type=${param.type}&device_sort_code=${param.device_sort_code}&check_flow=${param.check_flow}&isBatch=${param.isBatch}&flow_num=${param.flow_num}&acc_id=${param.acc_id}&infoId=${param.ids}&backType="+backType;
					}else{
						url = "report/batch/flow_batchCheck.do?type=${param.type}&device_sort_code=${param.device_sort_code}&check_flow=${param.check_flow}&isBatch=${param.isBatch}&flow_num=${param.flow_num}&acc_id=${param.acc_id}&infoId=${param.ids}&org_id=${param.org_id}";
					}
				//验证表单数据
				if($('#form1').validate().form())
				{
					var formData = $("#form1").getValues();
			        var data = {};
			        data = formData;
			 
			     
			        $("body").mask("系统正在玩命提交数据中，请耐心等待！");
			        $.ajax({
			            url: url,
			            type: "POST",
			            datatype: "json",
			           
			           	data: {dataStr : $.ligerui.toJSON(data)},
			            success: function (resp) {
			            	$("body").unmask();
			                if (resp["success"]) {
			                	if(resp["isBatch"] == "1"){
			                		if("1"=="<%=check_flow %>" || is_pass=="2" || <%=org_id %>=="100069" || <%=org_id %>=="100090" || <%=org_id %>=="100091"){
			                			if(api.data.window.Qm)
					                	{
					                		api.data.window.Qm.refreshGrid();
					                	}
					                    top.$.dialog.notice({content:'提交成功'});
					                    api.data.window.api.data.window.Qm.refreshGrid();
					                    api.data.window.api.close();
					                    api.close();
				                	}else{
											     		api.data.window.api.data.window.Qm.refreshGrid();
									                    api.data.window.api.close();
									                    api.close();	
														top.$.dialog({
											    			width : 800,
											    			height :600,
											    			lock : true,
											    			title : "签发分配结果",
											    			content : 'url:app/inspection/report_auto_issue_list.jsp?info_ids=${param.ids}',
											    			data : {
											    				"window" : window
											    			}
											    		});
				                	}              	
			                	}else{
			                		top.$.dialog.notice({content:'保存成功'});
				                    api.data.window.left._opid${param.opid}.innerHTML="<img src='k/kui/images/icons/16/check.png' border='0' >";
				                    api.close();
			                	}
			                }else
			                {
			                	$.ligerDialog.error('提示：' + resp.msg);
			                }
			            },
			            error: function (resp) {
			            	$("body").unmask();
			                $.ligerDialog.error('提示：' + resp.msg);
			            }
			        });
				}
			}
	
			
			
			function setValue(valuex,name){
				
				if(valuex==""){
					return;
				}
				if(name=='next_op_name'){
				$('#next_op_name').val(valuex)
				}
		}
			
		
			function currentTime(){
				
				var d = new Date(),
				
				 str = '';
				
				 str += d.getFullYear()+'-';
				 if(d.getMonth()<10){
				 	str  += "0";
				 }
				
				 str  += d.getMonth() + 1+'-';
				 if(d.getDate()<10){
				 	str  += "0";
				 }
				 str  += d.getDate()+' ';
				
				 //str += d.getHours()+':';
				
				 //str  += d.getMinutes()+':';
				
				//str+= d.getSeconds();
				
				
				
				return str;
				
				}
				
			//setInterval(function(){$('#op_time').val(currentTime)},1000);
			
			
			function changeFlag(flag){
				
			
			
				var type = ${param.type};
				
				$("#back").hide();
				if(flag=="1"){
					$("#sub_op").show();
				}else if(type=="05"&&flag=="2") {
					$("#back").show();
				}else{
					$("#sub_op").hide();
				}
			}
	</script>
	</head>
	<body>
		
	<form id="form1"  >
	
		
					
					<table border="1" cellpadding="0" cellspacing="0" width="" class="l-detail-table">
					
					
					
					
						<tr>
							<td class="l-t-td-left">结论：</td>
							<td class="l-t-td-right" >
							
							<input type="radio" name="revise_conclusion"  id="revise_conclusion" ltype="radioGroup"
											validate="{required:false}"
											ligerui="{onChange:changeFlag,value:'1',data: [ { text:'通过', id:'1' }, { text:'不通过', id:'2' } ] }"/></td>
							
							
							
							
					</tr>
					<tr>
							<td class="l-t-td-left">检验日期：</td>
							<td class="l-t-td-right" ><c:out value="${param.advance_time}"></c:out> </td>
					</tr>
					<%
						if("3".equals(device_type)){
							%>
					<tr>
							<td class="l-t-td-left">编制日期：</td>
							<td class="l-t-td-right" ><c:out value="${param.enter_time}"></c:out> </td>
					</tr>		
							<%
						}
					%>
					<c:if test='${param.type=="05"}'>
					<tr>
							<td class="l-t-td-left">审核日期：</td>
							<td class="l-t-td-right" ><c:out value="${param.check_time}"></c:out> </td>
					</tr>	
					</c:if>
					<!-- 
					<tr>
							<td class="l-t-td-left">当前日期：</td>
							<td class="l-t-td-right" ><div id="curDate"></div></td>
					</tr>
					 -->
					<tr>
						
						<c:choose>
							<c:when test='${param.type=="04"}'>
								<td class="l-t-td-left">审核日期：</td>
								<input type="hidden" name="title_and_data_conclusion" value="报告审核"/>
							</c:when>
							<c:when test='${param.type=="05"}'>
								<td class="l-t-td-left">签发日期：</td>
								<input type="hidden" name="title_and_data_conclusion" value="报告签发"/>
							</c:when>
							
						</c:choose>
							<td class="l-t-td-right" >
									<input name="op_time"
					type="text" ltype="date" validate="{required:false}"
					ligerui="{initValue:'',format:'yyyy-MM-dd'}" id="op_time" />
							</td>
						</tr>
					
			
					<tr>
						<td class="l-t-td-left">备注：</td>
						<td class="l-t-td-right"  colspan="2"><textarea name="revise_remark" id="revise_remark" rows="3" cols="" ext_type="string" ext_maxLength="200" ext_name="备注" isNull="Y" class="area_text" onfocus="this.innerHTML='';">请在此处填写报告退回原因！</textarea></td>
						
					</tr>
					<c:if test="${param.type=='04' && param.check_flow eq '0' && param.org_id != '100069' && param.org_id != '100090' && param.org_id != '100091'}">
						<tr id="sub_op" >
							<td class="l-t-td-left" >下一步操作人：</td>
							<td class="l-t-td-right" colspan="2">&nbsp;&nbsp;审核通过时，由系统自动分配，保存审核后可进入“签发分配记录”查看分配结果。&nbsp;&nbsp;</td>
						</tr>
					</c:if>
					<c:if test="${param.type=='04' && param.check_flow eq '0' && (param.org_id eq '100069' || param.org_id eq '100090' || param.org_id eq '100091')}">
						<tr id="sub_op" >
							<td class="l-t-td-left" >下一步操作人：</td>
							<td class="l-t-td-right"  colspan="2">
							<c:choose>
									<c:when test="${param.org_id eq '100069' || param.org_id eq '100090' || param.org_id eq '100091'}">
										
										<input  name ="next_op_name" id="next_op_name" type="hidden"/>
								<input type="text"  name="next_sub_op" id="next_sub_op"  ltype="select"  validate="{required:false}" onchange="setValue(this.value,'next_op_name')"
									ligerui="{tree:{checkbox:false,data: <u:dict sql="select t.id, t.pid, t.code, t.text from (select o.id as id,o.id as code, o.ORG_NAME as text, o.PARENT_ID as pid from sys_org o union select t1.id as id, t1.id as code, t1.NAME as text, t1.ORG_ID as pid from employee e,sys_user t1 where e.id=t1.employee_id and t1.status='1' and t1.id in (select s.user_id from sys_user_role s ,Sys_Role r where r.id=s.role_id and r.name='报告签发') union select t2.id as id, t2.id as code, t2.NAME as text, p2.ORG_ID as pid from employee e2, sys_user t2, SYS_EMPLOYEE_POSITION p1, SYS_POSITION p2 where e2.id = t2.employee_id and p1.position_id = p2.id and p1.employee_id = e2.id and (p2.org_id = '100091') and t2.status='1' and t2.id in (select s.user_id from sys_user_role s, Sys_Role r where r.id = s.role_id and r.name = '报告签发')) t start with t.id in ('100091') connect by t.pid = prior t.id"/>}}"/>
										
									
									</c:when>
									<c:otherwise>
									
										<input  name ="next_op_name" id="next_op_name" type="hidden"/>
								<input type="text"  name="next_sub_op" id="next_sub_op"  ltype="select"  validate="{required:false}" onchange="setValue(this.value,'next_op_name')"
									ligerui="{tree:{checkbox:false,data: <u:dict sql="select t.id, t.pid, t.code, t.text from (select o.id as id,o.id as code, o.ORG_NAME as text, o.PARENT_ID as pid from sys_org o union select t1.id as id, t1.id as code, t1.NAME as text, t1.ORG_ID as pid from employee e,sys_user t1 where e.id=t1.employee_id and t1.status='1' and t1.id in (select s.user_id from sys_user_role s ,Sys_Role r where r.id=s.role_id and r.name='报告签发') union select t2.id as id, t2.id as code, t2.NAME as text, p2.ORG_ID as pid from employee e2, sys_user t2, SYS_EMPLOYEE_POSITION p1, SYS_POSITION p2 where e2.id = t2.employee_id and p1.position_id = p2.id and p1.employee_id = e2.id and (p2.org_id = '100091') and t2.status='1' and t2.id in (select s.user_id from sys_user_role s, Sys_Role r where r.id = s.role_id and r.name = '报告签发')) t start with t.id in ('100020','100021','100022','100023','100024','100026','100029','100030','100034','100035','100033','100036','100037','100066','100063','100065','100067','100090','100091') connect by t.pid = prior t.id"/>}}"/>
										
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:if>
						<c:if test='${param.type=="05"}'>
						
						<tr id="back" >
							<td class="l-t-td-left">回退步骤：</td>
							<td class="l-t-td-right" >
							
							<input type="radio" name="backStep"  id="backStep" ltype="radioGroup"
											validate="{required:false}"
											ligerui="{value:'1',data: [ { text:'退回上一步', id:'1' }, { text:'退回报告录入', id:'2' } ] }"/>
							
								
						
							</td>
							
							
					</tr>
						</c:if>
					
					</table>
					
				
		</form>
	
		
		
	</body>
</html>