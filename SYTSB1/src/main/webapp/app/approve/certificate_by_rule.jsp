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
<script type="text/javascript">
	var status="${param.pageStatus}";
	var device_id = '1';
	var device='锅炉';
	var dept_id = '1';
	var dept = '全院';
	var report_id ='';
	var report='';
 	function loadGridData(treeNodeId,treeNode,parentNode) {
    	if(treeNode){
    		//console.log(treeNode);
    		//console.log(parentNode);
    		if(!parentNode){
	    		device_id = treeNode.id;
	    		device = treeNode.text;
	    		dept_id = '1';
	    		dept = '全院';
	    		report_id ='';
	    		report='';
	    		if(device_id=='3'||device_id=='0'){
	    			$.ligerDialog.alert("请选择下一级节点！");
	    			return;
	    		}
    		}else{
    			if(treeNode.text=="氧舱年度检查报告"||treeNode.text=="固定式压力容器年度检查报告"){
	    			$.ligerDialog.alert("请选择下一级节点！");
	    			return;
	    		}
    			if(parentNode.text == "氧舱年度检查报告" || parentNode.text == "固定式压力容器年度检查报告"){
    				var grandparentNode = parentNode.getParentNode();
    				//设备类别
    				device_id = grandparentNode.id;
        			device= grandparentNode.text;
        			//报告类型
        			report_id = parentNode.id;
    				report = parentNode.text;
    				//部门
    				dept_id = treeNode.id;
    				dept= treeNode.text;
    			}else{
    				device_id = parentNode.id;
        			device=parentNode.text;
        			if(treeNode.id.length==32){
        				report_id = treeNode.id;
        				report = treeNode.text;
        				dept_id = '1';
        				dept='全院';
        			}else{
        				dept_id = treeNode.id;
        				dept=treeNode.text;
        				report_id ='';
        				report ='';
        				
        			}
    			}
    		}
    	}
    	
    	$.post('certificateRuleAction/details.do',{
			device : device_id,
			dept : dept_id,
			report : report_id
		},function(res){
			if(res.success){
				if(res.data){
					deviceGrid.loadData({
						Rows : res.data["list"]
					});
					$("#form1").setValues(res.data);
					$("#hree").html(res.count1);
					$("#vacation").html(res.count2);
					$("#state").html(res.count3);
				}else{
					$('#id').val('');
					$('#device_classify_id').val(device_id);
					$('#device_classify_name').val(device);
					$('#dept_id').val(dept_id);
					$('#dept').val(dept);
					$('#report_id').val(report_id);
					$('#report_name').val(report);
					$("input[name='certificate_rule']").ligerGetRadioGroupManager().setValue('1');
					$("input[name='is_same_unit']").ligerGetRadioGroupManager().setValue('1');
					$("input[name='is_allow_self']").ligerGetRadioGroupManager().setValue('0');
					deviceGrid.loadData({
						Rows : []
					});
					$("#hree").html(0);
					$("#vacation").html(0);
					$("#state").html(0);
				}
				 
			}else{
				$('#id').val('');
				$('#device_classify_id').val(device_id);
				$('#device_classify_name').val(device);
				$('#dept_id').val(dept_id);
				$('#dept').val(dept);
				$('#report_id').val(report_id);
				$('#report_name').val(report);
				$("input[name='certificate_rule']").ligerGetRadioGroupManager().setValue('1');
				$("input[name='is_same_unit']").ligerGetRadioGroupManager().setValue('1');
				$("input[name='is_allow_self']").ligerGetRadioGroupManager().setValue('0');
				deviceGrid.loadData({
					Rows : []
				});
				$("#hree").html(0);
				$("#vacation").html(0);
				$("#state").html(0);
			}
		});
	}
 	//初始化选择框
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
			}]
		});
		$.post('certificateRuleAction/details.do',{
			device : device_id,
			dept : dept_id,
			report : report_id
		},function(res){
			if(res.success){
				if(res.data){
					deviceGrid.loadData({
						Rows : res.data["list"]
					});
					$("#form1").setValues(res.data);
					$("#hree").html(res.count1);
					$("#vacation").html(res.count2);
					$("#state").html(res.count3);
				}else{
					$('#id').val('');
					$('#device_classify_id').val(device_id);
					$('#device_classify_name').val(device);
					$('#dept_id').val(dept_id);
					$('#dept').val(dept);
					$('#report_id').val(report_id);
					$('#report_name').val(report);
					$("input[name='certificate_rule']").ligerGetRadioGroupManager().setValue('1');
					$("input[name='is_same_unit']").ligerGetRadioGroupManager().setValue('1');
					$("input[name='is_allow_self']").ligerGetRadioGroupManager().setValue('0');
					deviceGrid.loadData({
						Rows : []
					});
					$("#hree").html(0);
					$("#vacation").html(0);
					$("#state").html(0);
				}
			}else{
				$('#id').val('');
				$('#device_classify_id').val(device_id);
				$('#device_classify_name').val(device);
				$('#dept_id').val(dept_id);
				$('#dept').val(dept);
				$('#report_id').val(report_id);
				$('#report_name').val(report);
				$("input[name='certificate_rule']").ligerGetRadioGroupManager().setValue('1');
				$("input[name='is_same_unit']").ligerGetRadioGroupManager().setValue('1');
				$("input[name='is_allow_self']").ligerGetRadioGroupManager().setValue('0');
				deviceGrid.loadData({
					Rows : []
				});
				$("#hree").html(0);
				$("#vacation").html(0);
				$("#state").html(0);
			}
			
		});
	});
		function defineColumns(){
			if(status!="detail"){
				columns.push({ display: "<a class='l-a iconfont l-icon-add' href='javascript:void(0);' onclick='javascript:addDevice()' title='增加'><span>增加</span></a>", 
								isSort: false, 
								minWidth:30,
								width: '5%',
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
					{ display:'ID', name:'id', width:'1%',hide:true},
		            { display:'人员id', name:'user_id',width:'1%', hide:true},
		            { display:'账号', name:'user_account', width:'1%',hide:true},
		            { display:'姓名', name:'user_name', minWidth:150,width:'15%', type: 'text',
		            	required:true,editor: { type:'text'}
		            },
		            { display:'人员所在部门id', name:'deptId', width:'1%',hide:true},
		            { display:'人员所在部门', name:'dept', width:'1%',hide:true},
		            { display:'设备类别id', name:'deviceTypeId',width:'1%', hide:true},
		            { display:'设备类别', name:'deviceType', width:'1%',hide:true},
		            { display:'是否是主签人员', name:'is_substitute_person',minWidth:150, width:'15%',type: 'text',required:true,
		            	editor: { type: 'select', data: [{id:'0',text:'否'},{id:'1',text:'是'}] ,ext:{emptyOption:true}},
			            render: function (item) 
			            		{
			                		return render(item["is_substitute_person"],[{id:'0',text:'否'},{id:'1',text:'是'}]);
			               		}
		            },
		            { display:'是否签本部门部长', name:'is_ministerofaduit_person',minWidth:150, width:'15%',type: 'text',required:false,
		            	editor: { type: 'select', data: [{id:'0',text:'否'},{id:'1',text:'是'}] ,ext:{emptyOption:true}},
			            render: function (item) 
			            		{
			                		return render(item["is_ministerofaduit_person"],[{id:'0',text:'否'},{id:'1',text:'是'}]);
			               		}
		            },
		            { display:'可以审部门id', name:'mayCertDeptId',width:'1%', hide:true},
		            { display:'可以审部门', name:'mayCertDept',width:'1%', hide:true},
		            { display:'份额', name:'percentage',width:'1%', hide:true,required:false,type:'float',
		            	editor: { type: 'float',ext:{emptyOption:true}}},
		            { display:'报告id', name:'report_id', width:'1%',hide:true},
		            { display:'报告名称', name:'report_name', width:'1%',hide:true},
		            { display:'使用状态', name:'status',minWidth:150, width:'15%',type: 'text',required:true,
		            	editor: { type: 'select', data: [{id:'0',text:'停用'},{id:'1',text:'启用'}] ,ext:{emptyOption:true}},
			            render: function (item) {
			                return render(item["status"],[{id:'0',text:'停用'},{id:'1',text:'启用'}]);
			                }
		            	},
	            	{ display:'人员状态', name:'is_vacation',minWidth:150, width:'15%',type: 'text',required:true,
		            	editor: { type: 'select', data: [{id:'0',text:'在岗'},{id:'1',text:'请假'}] ,ext:{emptyOption:true}},
			            render: function (item) {
			                return render(item["is_vacation"],[{id:'0',text:'在岗'},{id:'1',text:'请假'}]);
			                }},
		            { display:'规则', name:'fk_rule',width:'1%', hide:true}
			);
		}
		/*  function f_onSelected(e) { 
	            if (!e.data || !e.data.length) return;
	            var selected = e.data[0]; 
	            grid.updateRow(deviceGrid.lastEditRow, {
                    user_id: selected.id,
		    		user_account:selected.account,
		    		user_name: selected.name,
		    		deptId:selected.org_id,
		    		dept: selected.org_name
	            });

	        } */
		
		function initGrid() {
	        deviceGrid = $("#device").ligerGrid({
		    	columns: columns,
		    	enabledEdit: true,
		        clickToEdit: true,
		        rownumbers: true,  
		        height:"150",
		        width:"100%",
		        frozenRownumbers: false,
		        usePager: false,
		       /*  onAfterEdit:changeBox, */
		        onBeforeEdit:f_onBeforeEdit, 
		        rowAttrRender : function(rowData, rowid){
            		var fontColor="#8B008B";
            		if (rowData.is_vacation == '0'&&rowData.status=='1'){
            			fontColor="green";
            		}else if(rowData.is_vacation == '1' ) {
            			fontColor="red";
            		}else if(rowData.status=='0'){
            			fontColor="blue";
            		}
            		return "style='color:"+fontColor+"'";
            	},
		        data: {Rows: []},
		        width: '100%'
		   	  });
		}  
		
       function changeBox(e){
    	
       }
       var selector;
       function f_onBeforeEdit(e)
       { 
    	   var column_name = e.column.name;
    	   if (column_name == "user_name")
           {
    		   selector = $.ligerDialog.open({ 
    				 title: '选择', 
    				 width: 600, 
    				 height: 400,
    				 parent:api, 
    				 url: 'app/approve/user_selector.jsp',
    				 data: {"window" : window},
    				 buttons: [
    				    { text: '确定', onclick: f_importOK },
    					{ text: '取消', onclick: f_importCancel }
    				 ]
    		   });
           }
       }
       
       //选择人员后回调函数
       var f_rows='';
       function f_importOK(item, selector){
			f_rows = selector.frame.f_select();
			var selected = deviceGrid.getSelected();
		    if (!f_rows){
		    	top.$.notice("请选择行！");
		        return;
		    }
		    var rows = deviceGrid.getData();
		    for(var j =0;j<rows.length;j++){
		    	if(f_rows[0].id==rows[j]["user_id"]){
		    		top.$.notice("重复添加人员！");
			        return;
		    	}
		    }
		    selector.close();
		    deviceGrid.updateRow(selected,{ 
   			 	user_id: f_rows[0].id,
	    		 user_account: f_rows[0].account,
	    		 user_name: f_rows[0].name,
	    		 deptId: f_rows[0].org_id,
	    		 dept: f_rows[0].org_name
   		 	});
		}
		
		function f_importCancel(item, selector){
			selector.close();
		}		
			
		function save(){
			//验证表单数据
			if($('#form1').validate().form()){
				var formData = $("#form1").getValues();
		        if(!validateGrid(deviceGrid)){
					return false;
				}
		        var bys = deviceGrid.getData();
		        //验证是否指定人员
		        if(formData.certificate_rule == '3'){
		        	var num = 0;
		        	var len = bys.length;
		        	if(len<1 || len>2){
		        		$.ligerDialog.alert('提示：选择“指定分配人”规则时，只能添加1-2名签字人员，并且必须设置唯一位为主签人员!');
		        		return;
		        	}
		        	for(var i=0;i<len;i++){
		        		if(bys[i].is_substitute_person=='1'){
		        			num++;
		        		}
		        	}
		        	if(num!=1){
		        		$.ligerDialog.alert('提示：选择“指定分配人”规则时，只能添加1-2名签字人员，并且必须设置唯一位为主签人员！');
		        		return;
		        	}
		        }
		        formData["list"] = bys;
		        var  jsonString = $.ligerui.toJSON(formData);
		        $("body").mask("正在保存数据，请稍后！");
		        $.ajax({
		            url: "certificateRuleAction/saves.do",
		            type: "POST",
		            datatype: "json",
		            contentType: "application/json; charset=utf-8",
		           	data: $.ligerui.toJSON(formData),
		            success: function (data, stats) {
		            	$("body").unmask();
		                if (data["success"]) {
		                    top.$.dialog.notice({content:'保存成功'});
		                    deviceGrid.loadData({
								Rows : data.data["list"]
							});
							$("#form1").setValues(data.data);
							var items = data.data['list'];
							var c1=0,c2=0,c3=0;
							$.each(items,function(i,item){
								if(item.status=='0'){
									c3++;
								}else if(item.is_vacation=='0'){
									c1++;
								}else if(item.is_vacation=='1'){
									c2++;
								}
							});
							$("#hree").html(c1);
							$("#vacation").html(c2);
							$("#state").html(c3);
		                }else{
		                	$.ligerDialog.error('提示：' + data["msg"]);
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
			var tt = {
					user_name:'',
					deviceTypeId:device_id,
					deviceType:device,
					mayCertDeptId:dept_id,
					mayCertDept:dept,
					report_id:report_id,
					report_name:report,
					percentage:1,
					is_substitute_person:'0',
					is_ministerofaduit_person:'0',
					is_vacation:'0',
					status:'1'
				};
			deviceGrid.addRow(tt);
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

		function render(value,data,fname){
			if(fname=='checkOpId'){
			if(value!=undefined&&value.indexOf(',')!=-1){
					var values = value.split(",");
					var text=[];
					for(var ele in values){
						 for (var i in data) {
						  if (data[i]["id"] == values[ele]){
							  text.push(data[i]['text']);
							 }
							}
						}
					return text;
				}
			}
			for (var i in data) {
				
				if (data[i]["id"] == value)
				{
					
					return data[i]['text'];
				}
				if(data[i].children)
				{
					for(var j in data[i].children)
					{
						if(data[i].children[j]["id"] ==value)
							return data[i].children[j]['text'];
						if(data[i].children[j].children)
						{
							for(var k in data[i].children[j].children)
								if(data[i].children[j].children[k]["id"]==value)
								{
									return data[i].children[j].children[k]["text"];
								}
						}
					}
				}
			}
			return value;
		}

   
    </script>
</head>

<body>
<div class="item-tm" id="titleElement">
		<div class="l-page-title">
			<div class="l-page-title-note">提示：状态
				<font color="green">“绿色”</font>代表当前可签字人员，
                <font color="red">“红色”</font>代表当前请假人员，
                <font color="blue">“蓝色”</font>代表当前已停用人员。
			</div>
		</div>
	</div>
	<form id="form1" method="post" action="certificateRuleAction/saves.do" getAction="certificateRuleAction/details.do?id=${param.id}">
		<fieldset class="l-fieldset">
			<legend class="l-legend">
				<div>检验报告授权签字人<span>（当前可签字总人数：<span id="hree">0</span>人，请假：<span id="vacation">0</span>人，停用：<span id="state">0</span>人。）</span></div>
			</legend>
			<div style="height:150px;" id="device"></div>
		</fieldset>		 
		<fieldset class="l-fieldset">
			<legend class="l-legend">
				<div>
					检验报告签字分配规则
				</div>
			</legend>
			<table cellpadding="3" cellspacing="0" class="l-detail-table">
				<input type="hidden" id="id" name="id" value=""/>
				<input type="hidden" id="device_classify_id" name="device_classify_id" value=""/>
				<input type="hidden" id="device_classify_name" name="device_classify_name" value=""/>
				<input type="hidden" id="dept_id" name="dept_id" value=""/>
				<input type="hidden" id="dept" name="dept" value=""/>
				<input type="hidden" id="report_id" name="report_id" value=""/>
				<input type="hidden" id="report_name" name="report_name" value=""/>
				<tr>
					<td class="l-t-td-left">1、是否允许签发人参与其他流程：</td>
					<td class="l-t-td-right">&nbsp;&nbsp;</td>
				</tr>
				<tr>
					<td class="l-t-td-left" >&nbsp;&nbsp;</td>
					<td class="l-t-td-right" >
						<input name="is_same_person" type="radio" ltype="radioGroup"  ligerui="{initValue:'0',data:[{id:'1',text:'是'},{id:'0',text:'否'}]}"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left" >2、单人分配（无任何特殊情况下的单人分配）</td>
					<td class="l-t-td-right">&nbsp;&nbsp;</td>
				</tr>
				<tr>
					<td class="l-t-td-left">3、是否可签本部门报告：</td>
					<td class="l-t-td-right">&nbsp;&nbsp;</td>
				</tr>
				<tr>
					<td class="l-t-td-left" >&nbsp;&nbsp;</td>
					<td class="l-t-td-right" >
						<input name="is_allow_self" type="radio" ltype="radioGroup"  ligerui="{initValue:'0',data:[{id:'1',text:'是'},{id:'0',text:'否'}]}"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left">4、请假与候补分配</td>
					<td class="l-t-td-right">&nbsp;&nbsp;</td>
				</tr>
				<tr>
					<td class="l-t-td-left" >5、是否匹配相同单位：</td>
					<td class="l-t-td-right" >&nbsp;&nbsp;</td>
				</tr>
				<tr>
					<td class="l-t-td-left" >&nbsp;&nbsp;</td>
					<td class="l-t-td-right" >
						<u:combo name="is_same_unit" ltype="radioGroup"  attribute="initValue:'1',data:[{id:'1',text:'是'},{id:'0',text:'否'}]"/>
					</td>
				</tr>
				<tr>
					<td class="l-t-td-left" >6、优先分配规则：</td>
					<td class="l-t-td-right" >&nbsp;&nbsp;</td>
				</tr>
				<tr>
					<td class="l-t-td-left" >&nbsp;&nbsp;</td>
					<td class="l-t-td-right" >
						<u:combo name="certificate_rule" ltype="radioGroup" attribute="initValue:'1',data:[{id:'1',text:'量少优先分配'},{id:'2',text:'随机分配 '},{id:'3',text:'指定分配人'}]"/>
					</td>
				</tr>
			</table>
			
	</fieldset>
	</form>
</body>
</html>