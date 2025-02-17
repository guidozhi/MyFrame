
<%@page import="java.util.Date"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head pageStatus="${param.status}">
		<title></title>
		<!-- 每个页面引入，页面编码、引入js，页面标题 -->
		<%@include file="/k/kui-base-form.jsp"%>
		<%
	CurrentSessionUser user = SecurityUtil.getSecurityUser();
%>
	<script type="text/javascript" src="app/inspection/device_info.js"></script>
		<script type="text/javascript">	
		var pageStatus = "${param.status}";
		var checkType=null;
		var reportType =null;

		// 检验任务单
// 		var contractTaskList = <u:dict sql="select t.id as id,t.sn as text from CONTRACT_TASK_INFO t where t.data_status !='99' and t.com_name = '${param.org_id}'"/>
		var contractTaskList ;
		
		var fzr = <u:dict sql="select t.id code,t.name text from employee t where t.org_id='${param.org_id}' "/>
		
		// 设备类别
		var deviceType = <u:dict code="device_classify"/>

		var flowId=null;
		$(function() {
			$('#check_type-txt').change(function(){
				if($(this).val()==''){
					alert();
				}else{
					checkType=$("input[name='check_type']").val();
				};					
			});
					
			if(pageStatus=='modify'){
				reportType=<u:dict sql="select distinct(t.fk_report_id),t.report_name from base_unit_flow t where t.check_type =${param.checkT} and t.device_type like '${param.deviceT}%'"/>;
			}else{
				reportType="";
			}

// 			$.post("contractTaskInfo/getContractTasks.do", function(resp) {
// 				if (resp.data) {
// 					var initData = '[]';
// 					if(resp.data != '[]'){
// 						initData = eval(resp.data);
// 					}else{
// 						initData = [ {  id:'无' ,text:'无'}];						
// 					}
// 					$("#contract_task_ids").ligerGetComboBoxManager().setData(initData);
// 				}
// 			});	

			createdeviceGrid();
	
			$('#advance_time').val('<%=DateUtil.getDateTime("yyyy-MM-dd", new Date()) %>');
			
			$("#form1").initForm({ //参数设置示例
				toolbar : [ {
					text : '保存',
					id : 'save',
					icon : 'save',
					click : save
				}, {
					text : '关闭',
					id : 'close',
					icon : 'cancel',
					click : close
				} ],
				getSuccess : function(res) {
					if(res.success){
						var inspectionDatas=res.inspectionDatas;
						var arr = new Array(res.inspectionDatas.length);
						for (var i=0;i<inspectionDatas.length;i++)
						{
						var d=inspectionDatas[i];
						d.device_code_t=inspectionDatas[i].device_sort;
						d.contract_task_id=res.inspection.inspectionInfo[0].contract_task_sn;
						arr[i]=d;
						}
						deviceGrid.loadData({
							Rows : arr//res.inspectionDatas
						});
						$("#form1").setValues({check_op_ids:res.op_name_ids,check_op_name:res.op_name_names,id:res.inspection.id,fk_unit_id:res.inspection.fk_unit_id,com_name:res.inspection.com_name,com_address:res.inspection.com_address,check_type:res.inspection.check_type,accept_no:res.inspection.accept_no});
						$("#contract_task_sn").val(res.inspection.inspectionInfo[0].contract_task_sn);
						$("#contract_task_ids").val(res.inspection.inspectionInfo[0].contract_task_id);
						
					}
					// 修改报检时，重新设置报告类型选项
					$.post("department/basic/getPage.do?checkType="+res.inspection.check_type+"&device_code="+res.inspectionDatas[0].device_sort, function(resp) {
						if (resp.data) {
							$("#report_types").ligerGetComboBoxManager().setData(eval(resp.data));
						}
					});
				}
			});
		})

		function close(url){	
			api.close();
		}
		
		function save(url){
				// 机电部门ID
				var org_ids = "100020,100021,100022,100023,100024,100063";
					var org_id = '${param.org_id}';
					$("#save").attr("disabled","disabled");
					
					var  check_op_name= $('#check_op_name').val();				
					if(check_op_name==''){
						$("#save").removeAttr("disabled");
						$.ligerDialog.alert("参检人员未选择！");
						return;
					}else{
						var check_users = check_op_name.split(",");						
						//var  check_type= $('#check_type').val();//获取检验类型						
						var  check_type= $('#check_type').val();	// 获取设备类别
						var  report_types= $('#report_types').val();	// 获取报告类型

						//if(check_type!='7'){
							if(org_ids.indexOf(org_id) != -1){
								if(check_users.length!=2){
									if(report_types.indexOf("电梯安全评估")==-1){
										$("#save").removeAttr("disabled");
										$.ligerDialog.alert("参检人员，请选择2位！");
										return;
									}
								}
							}else{
								// 承压部门验证必选合同
								/*
								var  pact_sn= $('#pact_sn').val();
								if(pact_sn==''){
									$("#save").removeAttr("disabled");
									$.ligerDialog.alert("请选择合同号！如未找到您要的合同号，请联系业务发展部028-86607816！");
									return;
								}*/
							}
						//}
					}
				
					var contract_task_sn=$("#contract_task_sn").val();
					var sblb="";
					$.each(deviceGrid.getData(),function(i,v){
						sblb=sblb+v.device_code_t.substring(0,1)+',';
					});
					sblb=sblb.substring(0,sblb.length-1);
					var sblbs=sblb.split(",");
					//机电类
					var dt=$.inArray("3",sblbs);//电梯
					var qzjx=$.inArray("4",sblbs);//起重机械
					var cnjdc=$.inArray("5",sblbs);//场内机动车
					var dxylss=$.inArray("6",sblbs);//大型游乐设施
					var kysd=$.inArray("9",sblbs);//客运索道
					
					
					var sfyz=true;//是否验证（承压需要验证）
					if(dt>-1 || qzjx>-1 || cnjdc>-1 || dxylss>-1 || kysd>-1){
						sfyz=false;
					}
					
					if(contract_task_sn=="" && sfyz){
						$("#save").removeAttr("disabled");
						$.ligerDialog.alert("请选择检验任务单！");
						return;
					}
						
					
					//url = "department/basic/flow_saveBasic.do?flowId=${param.flowId}&check_type="+check_type+"&acc_no="+acc_no;
					var rwdId=$("#contract_task_ids").val();
					var rwdSn=$("#contract_task_sn").val();
					url="department/basic/saveBasic2.do?check_type="+checkType+"&check_op_name="+encodeURIComponent(check_op_name)+"&status="+pageStatus+"&rwdId="+rwdId+"&rwdSn="+rwdSn;
					
				//验证表单数据
				if($('#form1').validate().form())
				{
					
					var formData = $("#form1").getValues();
			        var data = {};
			        data = formData;
			        //验证grid
			        if(!validateGrid(deviceGrid))
					{
						return false;
					}
			        
			        data["inspectionInfo"] = deviceGrid.getData();
			       
			     
			        $("body").mask("正在保存数据，请稍后！");
			        $.ajax({
			            url: url,
			            type: "POST",
			            datatype: "json",
			            contentType: "application/json; charset=utf-8",
			           	data: $.ligerui.toJSON(data),
			            success: function (data, stats) {
			            	$("body").unmask();
			                if (data["success"]) {
			                	if(api.data.window.Qm)
			                	{
			                		api.data.window.Qm.refreshGrid();
			                	}
			                    top.$.dialog.notice({content:'保存成功'});
			                    api.close();
			                }else
			                {
			                	$.ligerDialog.error('提示：' + data.msg);
			                	$("#save").removeAttr("disabled");
			                }
			            },
			            error: function (data,stats) {
			            	
			            	$("body").unmask();
			                $.ligerDialog.error('提示：' + data.msg);
			                $("#save").removeAttr("disabled");
			            }
			        });
				}
			}
			
			
			function getOrg(value){
				
	
				
			$.getJSON('department/basic/getOrg.do?code='+value,function(data){
				
			
					
					if(data.success==true){
						$('#fk_unit_id').val(data.id);
						$('#com_name').val(data.com_name);
						$('#com_address').val(data.com_address);
						
					}else{
						top.$.notice("此设备数据有误，请到数据管理完善！");
					}				
				});
				
				
				
				
			}
			
			function changeData(){
				
				var org_id = $('#fk_unit_id').val();
				
				if(org_id==''|| org_id==null || org_id == undefined){
					top.$.notice("请选择单位信息！");
						return;
				}
				
				top.$.dialog({
					width : 800, 
					height : 700, 
					lock : true, 
					title:"修改",
					content: 'url:app/enter/enter_detail.jsp?status=modify&id='+org_id+'&flag=1',
					data : {"changeWindow" : window}
				});
			}
			
			function change(id,name,address){
				
				$('#fk_unit_id').val(id);
				$('#com_name').val(name);
				$('#com_address').val(address);
				
		
		}
			
			function submit(){
				url = "department/basic/flow_saveBasic.do?flowId=${param.flowId}&check_type="+check_type+"&acc_no="+acc_no;
				//验证表单数据
				if($('#form1').validate().form())
				{
					var formData = $("#form1").getValues();
			        var data = {};
			        data = formData;
			        //验证grid
			        if(!validateGrid(deviceGrid))
					{
						return false;
					}

			        data["inspectionInfo"] = deviceGrid.getData();
			       
			     
			        $("body").mask("正在保存数据，请稍后！");
			        $.ajax({
			            url: url,
			            type: "POST",
			            datatype: "json",
			            contentType: "application/json; charset=utf-8",
			           	data: $.ligerui.toJSON(data),
			            success: function (data, stats) {
			            	$("body").unmask();
			                if (data["success"]) {
			                	if(api.data.window.Qm)
			                	{
			                		api.data.window.Qm.refreshGrid();
			                	}
			                    top.$.dialog.notice({content:'保存成功'});
			                    api.close();
			                }else
			                {
			                	$.ligerDialog.error('提示：' + data.msg);
			                }
			            },
			            error: function (data,stats) {
			            	$("body").unmask();
			                $.ligerDialog.error('提示：' + data.msg);
			            }
			        });
				}
			}
			function selectorg(){
				var check_type = $("input[id='check_type-txt']").ligerComboBox().getValue();
				if("" == check_type){
					$.ligerDialog.alert("请先选择“检验类别”！");
					return;
				}
				var tmp= deviceGrid.getData();
				tmp = JSON.stringify(tmp)
			
				if(tmp!="[]"){
					
					top.$.notice("请先删除设备信息！");
					return;
				}
			
				top.$.dialog({
					parent: api,
					width : 800, 
					height : 550, 
					lock : true, 
					title:"选择企业信息",
					content: 'url:app/enter/enter_open_list.jsp',
					data : {"parentWindow" : window}
				});
			}
			
			function callBack(id,name,address){
				
					$('#fk_unit_id').val(id);
					$('#com_name').val(name);
					$('#com_address').val(address);
					
			
			}

			
			function setValues(valuex,name){
// 				alert(valuex+name);
				if(valuex==""){
					return;
				}
					var selected = deviceGrid.rows;
					
		            if (!selected) { alert('请选择行'); return; }
		            
		            var check_op;
		            var check_tel;
		            var check_op_id;
		            var check_op_name;
		            var advance_time;
		            var report_type;
		            var contract_task_id;
		            
					
		            for(var i in selected){
		            	if(name=='check_op'){
		            		if(valuex==''|| valuex==null || valuex == undefined){
		            			check_op = selected[i].check_op;
		            		}else{
		            			check_op = valuex;
		            		}
		            	}
						//if(name=='check_op_name'){
		            		//if(valuex==''|| valuex==null || valuex == undefined){
		            		//	check_op_name = selected[i].check_op_name;
		            		//}else{
		            			//check_op_name = valuex;
		            		//}
		            //	}
						
						if(name=='check_op_id'){
							
	            		if(valuex==''|| valuex==null || valuex == undefined){
	            			check_op_id = selected[i].check_op_id;
	            		}else{
	            			//check_op_id = valuex;
	            			
	            			var text= $("input[name='check_op_ids']").ligerGetComboBoxManager().getValue();
	            			
	            			
	            			
	            			$('#check_op_name').val(valuex);
	            			
	            			check_op_name = valuex;
	            			check_op_id = text;
	            			
	            			
	            			
	            		}
	            	}
		            	
		            	if(name=='check_tel'){
		            		if(valuex==''|| valuex==null || valuex ==undefined){
		            			check_tel = selected[i].check_tel;
		            		}else{
		            			var text= $("input[name='check_tels']").ligerGetComboBoxManager().getValue();
		            			check_tel = text;
		            		}
		            	}
		            	if(name=='advance_time'){
		            		if(valuex==''|| valuex==null || valuex ==undefined){
		            			advance_time = selected[i].advance_time;
		            		}else{
		            			//var date = new Date(valuex.replace(/-/g, "/"));
								//var tt = new Date(date).format('yyyy-MM-dd');
		            			advance_time = valuex;
		            		}
		            	}
		            	if(name=='report_type'){
		            		if(valuex==''|| valuex==null || valuex ==undefined){
		            			report_type = selected[i].report_type;
		            		}else{
		            			var text= $("input[name='report_types']").ligerGetComboBoxManager().getValue();
		            			report_type = text;
		            		}
		            	}
		            	if(name=='contract_task_id'){
		            		if(valuex=='' || valuex=='null' || valuex==null || valuex ==undefined){
		            			contract_task_id = selected[i].contract_task_id;
		            		}else{
// 		            			var text= $("input[name='contract_task_ids']").ligerGetComboBoxManager().getValue();
								
		            			contract_task_id = valuex;
// 		            			$('#contract_task_sn').val(value);
		            		}
		            	}
		            	deviceGrid.updateRow(selected[i],{
		            	check_op_id: check_op_id,
		            	check_op_name:check_op_name,
		            	check_op: check_op,
		            	check_tel: check_tel,
		            	advance_time: advance_time,
		            	report_type: report_type,
		            	contract_task_id: contract_task_id
		            });
		         }
			}
			
			function valChk(){
// 				var  check_type= $('#check_type').val();	// 获取设备类别
// 				var com_name=$("#com_name").val();
// 				com_name= encodeURI(encodeURI(com_name))
				top.$.dialog({
					parent: api,
					width : 800, 
					height : 550, 
					lock : true, 
					title:"选择检验任务单",
					content:'url:app/inspection/serviceDepartment_jyrwd.jsp',
					data : {"parentWindow" : window}
				});
			}
			function jyrwdBack(id,sn){
				$("#contract_task_ids").val(id);
				$("#contract_task_sn").val(sn);
				setValues(sn,'contract_task_id')
			}
	</script>
	</head>
	<body>
		
	<form id="form1"  getAction="department/basic/getDetail.do?id=${param.id}">
	
	<fieldset class="l-fieldset">
					<legend class="l-legend">
						<div>
							单位信息
						</div>
					</legend>
          <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
          <input id="id" name="id"  type="hidden"  />
          <input id="accept_no" name="accept_no" type="hidden" />
            <tr>
           <!--  <td class="l-t-td-left">大厅报检单位名称：</td>
						<td class="l-t-td-right" colspan="3">
            <input type="text" name="hall_com_nam" value="${param.com_name}" readonly="readonly"/>
            </td> -->
            <td class="l-t-td-left">检验类别：</td>
						<td class="l-t-td-right" colspan="3">
            <u:combo name="check_type" code="BASE_CHECK_TYPE" validate="required:true"  attribute="disabled:false"   />
            </td> 
            </tr>
             <tr>
          
            <td class="l-t-td-left">设备注册代码：</td>
						<td class="l-t-td-right" colspan="1">
            <input type="text" id="base_code" name="base_code"  validate="required:false"  onchange="getOrg(this.value)" />
            </td>
            <TD class="l-t-td-right" colspan="2"><b>如遇不知道使用单位的设备，在此输入设备注册代码</b></TD> 
            </tr>
	<tr >				

	


	
						<td class="l-t-td-left">受检单位：</td>
						<td class="l-t-td-right" colspan="2">
						<input type="hidden" name="fk_unit_id" id="fk_unit_id" />
						<input type="text" id="com_name" name="com_name"  readonly="readonly"  ltype="text"  validate="{required:true}" onclick="selectorg()"
										ligerui="{readonly:true,value:'',iconItems:[{icon:'add',click:function(){selectorg()}}]}"/>
						<td  class="l-t-td-right"><input  type="button"  ltype="text" value="修改数据"  onclick="changeData()" /></td>
					</tr>
					<tr class="d_tr">
						<td class="l-t-td-left">单位地址：</td>
						<td class="l-t-td-right" colspan="3"><input type="text"  ltype="text" name="com_address" id="com_address"  readonly></td>
					</tr>
		</table>
</fieldset>
<fieldset class="l-fieldset" >
					<legend class="l-legend">
						<div>
							设备信息（承压类设备，合同号必选。如未找到合同号，请联系业务发展部028-86607816。） 
						</div>
					</legend>
					<div style="height:300px;" id="device"></div>
</fieldset>				
				
		
		<fieldset class="l-fieldset">
		<legend class="l-legend">
						<div>
							便捷填写
						</div>
					</legend>
				
				 <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
					<tr>
						<td class="l-t-td-left">报告类型：</td>
						<td class="l-t-td-right" >
							<input type="text" id="report_types" name="report_types" ltype="select" validate="{required:false}"  onchange="setValues(this.value,'report_type')" />
						</td>
						<td class="l-t-td-left">参检人员：</td>
						<td class="l-t-td-right" >
							<c:choose>
								<c:when test="${param.org_id eq '100024' || param.org_id eq '100063' || param.org_id eq '100023' || param.org_id eq '100020' || param.org_id eq '100021' || param.org_id eq '100022' || param.org_id eq '100069' || param.org_id eq '100090' || param.org_id eq '100091'}">
									<input type="text" name="check_op_ids" id="check_op_ids" ltype="select" onchange="setValues(this.value,'check_op_id')" validate="{required:false}" ligerui="{
							readonly:true,
							tree:{checkbox:true,data: <u:dict sql="select t.id, t.pid, t.code, t.text from (select o.id as id,o.id as code, o.org_code  as tcode, o.ORG_NAME as text,o.PARENT_ID as pid from sys_org o union select e.id as id, e.id as code, e.code as tcode, e.NAME as text, e.ORG_ID as pid from employee e union select e2.id as id, e2.id as code,e2.code as tcode, e2.NAME as text, p2.ORG_ID as pid from employee e2, SYS_EMPLOYEE_POSITION p1, SYS_POSITION p2 where p1.position_id = p2.id and p1.employee_id = e2.id and p2.org_id in ('100020', '100021','100022', '100023', '100063','100024', '100069', '100090', '100091')) t start with t.id in ('100020', '100021','100022', '100023', '100063','100024', '100069', '100090', '100091') connect by t.pid = prior t.id ORDER BY T.TCODE"/>}
							}"/>
								</c:when>
								<c:otherwise>
									<input type="text" name="check_op_ids" id="check_op_ids" ltype="select" onchange="setValues(this.value,'check_op_id')" validate="{required:false}" ligerui="{
							readonly:true,
							tree:{checkbox:true,data: <u:dict sql="select t.id, t.pid, t.code, t.text from (select o.id as id,o.id as code, o.org_code  as tcode, o.ORG_NAME as text,o.PARENT_ID as pid from sys_org o union select e.id as id, e.id as code, e.code as tcode, e.NAME as text, e.ORG_ID as pid from employee e where e.ORG_ID != '100049') t where t.id!='100049' start with t.id in ('100029','100045','100034', '100035','100033', '100065', '100036','100037', '100066','100067','100030') connect by t.pid = prior t.id ORDER BY T.TCODE"/>}
							}"/>
								</c:otherwise>
							</c:choose>
						
							<input type="hidden"  name="check_op_name" id="check_op_name"/>
						</td>
						
					</tr>
					<tr>
						<td class="l-t-td-left">检验日期：</td>
						<td class="l-t-td-right" >
							<input name="advance_time" type="text" ltype="date" validate="{required:false}"
								ligerui="{initValue:'',format:'yyyy-MM-dd'}" id="advance_time"  onchange="setValues(this.value,'advance_time')"/>
						</td>
						<td class="l-t-td-left">检验任务单：</td>
						<td class="l-t-td-right" >
							<input type="hidden"  name="contract_task_ids" id="contract_task_ids"/>
							<input type="text" id="contract_task_sn" name="contract_task_sn" ltype="text" readonly="readonly" validate="{required:false}" onclick="valChk()"   />
						</td>
					</tr>
					<tr>
						<td class="l-t-td-left">检验联系人：</td>
						<td class="l-t-td-right" >
						<input type="text" ltype="text" name="check_ops" value="" size="20"  onchange="setValues(this.value,'check_op')"></td>
						<td class="l-t-td-left">联系电话：</td>
						<td class="l-t-td-right" >
						<input type="text" ltype="text" name="check_tels" value="" size="15"  onchange="setValues(this.value,'check_tel')"></td>
					</tr>
					
				</table>
				</fieldset>
		
				
		</form>
	</body>
</html>
