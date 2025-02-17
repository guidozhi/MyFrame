<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html;charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
<%
DateFormat ds = new SimpleDateFormat("yyyyMMdd");
String date = ds.format(new Date());
%>
    <title></title>
<%@ include file="/k/kui-base-form.jsp"%>
	<!--  <link type="text/css" rel="stylesheet" href="app/finance/css/form_detail.css" /> -->
<script type="text/javascript" src="pub/fileupload1/fileupload/js/fileupload.js"></script>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript" src="app/archives/js/doc_order.js"></script>
<script type="text/javascript">
	var status="${param.pageStatus}";
	var deviceGrid;
	var columns=[];
	$(function(){
		defineColumns();
		initGrid();
		$("#form1").initForm({ //参数设置示例
			toolbar : [ {
				text : '保存',
				id : 'save',
				icon : 'save',
				click : save
			}, 
			{
				text : '关闭',
				id : 'close',
				icon : 'cancel',
				click : function close(){	
						 	api.close();
						}
			}],
			getSuccess : function(res) {
				 if(res.success){
					deviceGrid.loadData({
						Rows : res.data["report"]
					});
					console.log(res.data);
					//$("#form1").setValues({id:res.id});
					$("#form1").setValues(res.data);
				} 
			}
		});
	});
		function defineColumns(){
			if(status!="detail"){
				columns.push({ display: "<a class='l-a iconfont l-icon-add' href='javascript:void(0);' onclick='javascript:addDevice()' title='增加'><span>增加</span></a>", 
								isSort: false, 
								width: '30',
								height:'5%', 
								render: function (rowdata, index, value ) {
											var h = "";
											if (!rowdata._editing) {
												h += "<a class='l-a l-icon-del' href='javascript:delDevice(deviceGrid,"+index+")' title='删除'><span>删除</span></a> ";
											}
											return h;
										}
				});
				
			}
			columns.push(
					{ display:'ID', name:'id', width:'30%', hide:false},
		            { display:'报告名称', name:'report_name', width:'65%'}
			);
		}
		
		
		function initGrid() {
	        deviceGrid = $("#device").ligerGrid({
	    	columns: columns,
	        enabledEdit: status!="detail",
	        clickToEdit: true,
	        rownumbers: true,    
	        width:"99%",
	        frozenRownumbers: false,
	        usePager: false,
	        data: {Rows: []}
	   	  });
		}
		function save(){
			
			//验证表单数据
			if($('#form1').validate().form()){
				
				var formData = $("#form1").getValues();
		        if(!validateGrid(deviceGrid)){
					return false;
				}
		        var reports = deviceGrid.getData();
		        formData["report"] = reports;
		        var  jsonString = $.ligerui.toJSON(formData);
		        $("body").mask("正在保存数据，请稍后！");
		        $.ajax({
		            url: "certificateByAction/saveCertificateBy.do",
		            type: "POST",
		            datatype: "json",
		            contentType: "application/json; charset=utf-8",
		           	data: $.ligerui.toJSON(formData),
		            success: function (data, stats) {
		            	$("body").unmask();
		                if (data["success"]) {
		                	if(api.data.window.Qm){
		                		api.data.window.Qm.refreshGrid();
		                	}
		                    top.$.dialog.notice({content:'保存成功'});
		                    api.close();
		                }else{
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
		
		
		
		function addDevice() {
			 dia1 = $.ligerDialog.open({ 
			 title: '选择', 
			 width: 700, 
			 height: 500,
			 parent:api, 
			 url: 'app/approve/report_list_check.jsp',
			 data: {"window" : window},
			 buttons: [
			    { text: '确定', onclick: f_importOK },
				{ text: '取消', onclick: f_importCancel }
			]
			                                                                                                
			 });
		}
		
		function f_importOK(item, dialog){
			var rows = dialog.frame.f_select();
		    if (!rows) {
		    	top.$.notice("请选择行！");
		        return;
		    } 
		    var devRow = deviceGrid.rows;
		    var isexist=false;
		  
		    for(var i in rows){
		    	for(var j in devRow){
		    		if(rows[i].id == devRow[j].id){
		    			isexist = true;
		    			break;
		    		}else{
		    			isexist = false;
		    		}
		    	}
		    	if(!isexist){
		    		var tt = {
		    					id : rows[i].id, 
		    					report_name: rows[i].report_name
		    				};
					deviceGrid.addRow(tt);
		    	}
		    }
		   		 dialog.close();
		}
		
		function f_importCancel(item, dialog){
			dialog.close();
		}		
			
		
		
		function delDevice(obj, index) {
			var data = obj.getSelectedRow();
			var dataId = data.id;
				$.ligerDialog.confirm("确定要移除吗？", function(yes) {
				if (yes) {
					obj.deleteRow(index);
				}
			});

		}



    function choosePerson(){
        top.$.dialog({
            width: 800,
            height: 450,
            lock: true,
            parent: api,
            title: "选择人员",
            content: 'url:app/common/person_choose.jsp',
            cancel: true,
            ok: function(){
                var p = this.iframe.contentWindow.getSelectedPerson();
                if(!p){
                    top.$.notice("请选择人员！", 3, "k/kui/images/icons/dialog/32X32/hits.png");
                    return false;
                }
                $("#employeeId").val(p.id);
                $("#employeeName").val(p.name);
                $("#dept").val(p.org_name);
                $("#deptId").val(p.org_id);
            }
        });
    }
    function choosePerson2(){
        top.$.dialog({
            width: 800,
            height: 450,
            lock: true,
            parent: api,
            title: "选择人员",
            content: 'url:app/common/person_choose.jsp',
            cancel: true,
            ok: function(){
                var p = this.iframe.contentWindow.getSelectedPerson();
                if(!p){
                    top.$.notice("请选择人员！", 3, "k/kui/images/icons/dialog/32X32/hits.png");
                    return false;
                }
                $("#substitutePersonId").val(p.id);
                $("#substitutePerson").val(p.name);
            }
        });
    }
  /*   function chooseReport(){
        top.$.dialog({
            width: 800,
            height: 450,
            lock: true,
            parent: api,
            title: "选择报告类型",
            content: 'url:app/approve/certificate_list.jsp',
            cancel: true,
            ok: function(){
                var p = this.iframe.contentWindow.getSelectedReport();
                if(!p){
                    top.$.notice("选择一份报告类型！", 3, "k/kui/images/icons/dialog/32X32/hits.png");
                    return false;
                }
                $("#fk_id").val(p.id);
                $("#report_name").val(p.report_name);
            }
        });
    } */
    function setValue(value,obj){
    	$('#'+obj).val(value);
    }
    function setDeviceType(id){
    	//var text = $('#deviceTypeId').ligerGetComboBoxManager().getValue();
    	//var text = $('#deviceTypeId').ligerGetComboBoxManager().findValueByText($('#deviceTypeId').val());
    	var text= $('#deviceTypeId').val();
    	$('#deviceType').val(text);
    }
    </script>
</head>

<body>
	<form id="form1" method="post" action="certificateByAction/saveCertificateBy.do"
		  getAction="certificateByAction/certificateByDetail.do?id=${param.id}">
		<fieldset class="l-fieldset">
			<legend class="l-legend">
				<div>
					授权人
				</div>
			</legend>
			<table cellpadding="3" cellspacing="0" class="l-detail-table">
				<input type="hidden" id="id" name="id"/>
				<input type="hidden" id="employeeId" name="employeeId"/>
				<tr>
					<td class="l-t-td-left">姓名</td>
					<td class="l-t-td-right">
						<input id="employeeName" name="employeeName" type="text" ltype="text" ligerui="{iconItems:[{icon:'user',click:choosePerson}]}"/>
					</td>
					<td class="l-t-td-left">所属部门</td>
					<td class="l-t-td-right">
						<input id="deptId" name="deptId" type="hidden" />
						<input id="dept" name="dept" type="text" ltype="text" />
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">设备类别</td>
					<td class="l-t-td-right">
						<input id="deviceType" name="deviceType" type="hidden" />
						<%-- <u:combo name="deviceTypeId" code="device_type" attribute="onSelected:function(val){
							$('#deviceType').val(val);
						}"/> --%>
						<input id="deviceTypeId" name="deviceTypeId" type="text" ltype="select" onChange="setDeviceType(this.value)"
							ligerui="{
										readonly:true,
										tree:{
											checkbox:false,
											data:<u:dict sql="select t.id,t.code_table_values_id pid,SUBSTR(t.value,0,1) code,t.name text from PUB_CODE_TABLE_VALUES t,pub_code_table t1 where t.code_table_id=t1.id and   t1.code = 'device_classify' and t.code_table_values_id is null order by case when t.value='0000' then 1 else 0 end, t.value asc"/>
										}
									}"/>
						
					</td>
					<td class="l-t-td-left">可签部门</td>
					<td class="l-t-td-right">
						<input id="mayCertDept" name="mayCertDept" type="hidden" />
						<input name="mayCertDeptId" type="text" ltype="select" onChange="setValue(this.value,'mayCertDept')"
							ligerui="{readonly:true,
									tree:{checkbox:false,data:<u:dict sql="select id,parent_id pid,id code, ORG_NAME text from SYS_ORG order by orders"/>}}"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">替代人</td>
					<td class="l-t-td-right">
						<input id="substitutePersonId" name="substitutePersonId" type="hidden"/>
						<input id="substitutePerson" name="substitutePerson" type="text" ltype="text" ligerui="{iconItems:[{icon:'user',click:choosePerson2}]}"/>
					</td>
					<td class="l-t-td-left">份额</td>
					<td class="l-t-td-right">
						<input name="percentage" type="text" ltype="text" value="1"/>
					</td>
				</tr>
				<tr>
					<%-- <td class="l-t-td-left">分配规则</td>
					<td class="l-t-td-right">
						<u:combo name="distributeRule" ltype="radioGroup" code="distribute_rule" validate="{required:true}" attribute="initValue:'1'"/>
					</td> --%>
					<td class="l-t-td-left">是否具体某种报告</td>
					<td class="l-t-td-right">
						<input name="isSpecific" type="radio" ltype="radioGroup" ligerUi="initValue:'0',data:[{id:'1',text:'是'},{id:'0',text:'否'}]"/>
					</td>
					<td class="l-t-td-left">是否启用替代人</td>
					<td class="l-t-td-right">
						<input name="isUseSubstitute" type="radio" ltype="radioGroup" ligerUi="initValue:'0',data:[{id:'1',text:'是'},{id:'0',text:'否'}]"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">状态</td>
					<td class="l-t-td-right">
						<input name="status" type="radio" ltype="radioGroup" ligerUi="initValue:'1',data:[{id:'1',text:'启用'},{id:'0',text:'不启用'}]"/>
					</td>
				</tr>
			</table>
	</fieldset>
	<fieldset class="l-fieldset">
			<legend class="l-legend">
				<div>
					关联报告
				</div>
			</legend>
			<div id="device"></div>
	</fieldset>
	</form>
</body>
</html>