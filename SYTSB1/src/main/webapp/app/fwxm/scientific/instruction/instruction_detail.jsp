﻿<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    CurrentSessionUser user=(CurrentSessionUser)request.getSession().getAttribute("currentSessionUser");
%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!--获取当前登录人  -->
<%
	CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
	User uu = (User)curUser.getSysUser();
	com.khnt.rbac.impl.bean.Employee e = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
	String userId=e.getId();
	String userid = SecurityUtil.getSecurityUser().getId();
	String users=curUser.getName();
	%>
<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<head pageStatus="${param.status}">
<title>设备采购申请</title>
<%@ include file="/k/kui-base-form.jsp"%>
<%
	String status = request.getParameter("status");
%>
<script type="text/javascript" src="pub/bpm/js/util.js"></script>
<script type="text/javascript" src="app/fwxm/scientific/instruction/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript" src="/app/common/js/render.js"></script>
<link rel="Stylesheet" href="app/finance/css/jquery.autocomplete.css" />
<link type="text/css" rel="stylesheet" href="app/finance/css/form_detail.css" /> 
<script type="text/javascript" src="app/humanresources/js/doc_order.js"></script>
<script type="text/javascript">
	//jQuery页面载入事件
var pageStatus="<%=status%>";
var columns=[];
	$(function(){
		if(pageStatus!="detail"){
		defineColumns();
		initGrid();
		}else{
			 var jrxx_html = '<div class="l-grid-body l-grid-body2 l-scroll"><div class="l-grid-body-inner"><table id="table4" class="l-detail-table b-dyn-table decimal-table"><tr class="l-t-td-title" style="height:30px;font-weight:normal">'
                 +'<td class="l-t-td-title" style="width:15%">项目名称</td>'
                 +'<td class="l-t-td-title" style="width:5%">制订/修订</td>'
                 +'<td class="l-t-td-title" style="width:25%">制修订理由</td>'
                 +'<td class="l-t-td-title" style="width:25%">要求</td>'
                 +'<td class="l-t-td-title" style="width:10%">完成时限</td>'
                 +'<td class="l-t-td-title" style="width:10%">提出部门</td>'
                 +'<td class="l-t-td-title" style="width:10%">提出人</td>'; 
			 $("#device").html(jrxx_html);
		}
		$("#formObj").initForm({ //参数设置示例
          	toolbar : [ {
   	        	text : '保存',
   	        	id : 'save',
   		        icon : 'save',
   		        click : save
   	          },{
     	        	text : '提交',
       	        	id : 'submit',
       		        icon : 'save',
       		        click : submit
       	          }, {
   	           	text : '关闭',
   		        id : 'close',
   		        icon : 'cancel',
   		        click : close
   	          } ],
			getSuccess : function(res) {
				if(res.success){
					/* deviceGrid.loadData({
						Rows : res.inspectionDatas
					}); */
					
					 if(pageStatus!="detail"){
						queryEquipmentList();
					}else{
						queryEquipmentList1();
					} 
					/* $("#formObj").setValues({id:res.id});
					$("#formObj").setValues(res.data);
					 */
				}
			}
		});
		$("body").append('<object style="height:1px;" id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0><embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed></object>');
	
	 
	});

	function submit(){
		var id="";
		if($("#device").ligerGetGridManager().recordNumber!=0){//获取ligerui行数
			if(${param.tj!=0}){//审批、审核
			 top.$.dialog({
					width : 400,
					height : 200,
					lock: true,
					parent: null,
					data: {window: window},
					title: "审核",
					content: 'url:app/fwxm/scientific/instruction/choose_opinion.jsp',
					cancel: true,
					ok : function() {
						  var data = this.iframe.contentWindow.getSelectResult();
						  if(${param.tj==2}&& data.opinion=="0"){
							  
							 doSumbit(id,null,null,data.opinion,data.remark);
						  }else{
					            	   selectUnitOrUser1(1,0, null, null, function(datas){
						           			if(datas.code!=null&&datas.code.length>0&&datas.code!="undefined"){
						           				doSumbit(id,datas.code,datas.name,data.opinion,data.remark);
						           			}else{
						           				top.$.dialog.alert("请选择一个用户!");
						           			}
					            	   });
						  }
						}
					});
			}else{//直接提交
		            	   selectUnitOrUser1(1,0, null, null, function(data){
			           			if(data.code!=null&&data.code.length>0&&data.code!="undefined"){
			           				doSumbit(id,data.code,data.name,null,null);
			           			}else{
			           				top.$.dialog.alert("请选择一个用户!");
			           			}
		            	   });
			
			}
		
		
		}else{
			top.$.dialog.alert("没有数据不能提交!");
		}
	}
	

    function doSumbit(id,tjUserId,tjUserName,opinion,remark){
    	var formData = $("#formObj").getValues();
        formData["tjUserId"]=tjUserId;
        formData["tjUserName"]=tjUserName;
        formData["type"]="2";
        formData["auditOpinion"]=remark;
        if(${param.tj==1}){//审核环节
            formData["opinion"]=opinion;
        }
        if(${param.tj==2}){//审批环节
            formData["opinion"]=opinion;
        }
		formData.instructionInfo=deviceGrid.getData();
       var instruction=$.ligerui.toJSON(formData);
    $("body").mask("提交中...");
       $.ajax({
           url: "com/tjy2/instruction/saveBasic.do",
           type: "POST",
           data:{"instruction":instruction},
           success : function(data, stats) {
				$("body").unmask();
				if (data["success"]) {
					if("${param.tj!=1}"){
						top.$.dialog.notice({
							content : '提交成功'
						});
						api.data.window.Qm.refreshGrid();
						api.close();
					}
				} else {
					$.ligerDialog.error('提示：' + data.message);
				}
			},
           error : function(data) {
               $("body").unmask();
               $.ligerDialog.error('保存数据失败！');
           }
           });
    }
    
	//定义列表
	function defineColumns(){
		if(pageStatus!="detail"){
			var nowDate = new Date().format("yyyy-MM-dd");
			$("#applyDate").val(nowDate);
			columns.push({
				display: "<a class='l-a iconfont l-icon-add' href='javascript:deviceGrid.addEditRow();'><span>增加</span></a>",
				isSort: false, 
				width: '30',
				render : function(item, index) {
    				return "<a class='l-a l-icon-del' href='javascript:delDevice(deviceGrid," + index + ")'><span>删除</span></a>";
    			}
			});
		}
		var type=<u:dict code="instruction_type"/>
		var projectName=<u:dict sql="select t.id,t.project_name pid, t.id code,t.project_name text  from TJY2_INSTRUCTION_PROJECT t where t.status='3'"/>
		columns.push({display: 'id', name: 'id', hide:true},
				{display: 'id', name: 'tjy2InstructionId', hide:true},
				{display: 'projectName', name: 'projectName', hide:true},
				{display: '项目名称',width: '12%',name: 'projectNameId',type:'text',required:true,
					editor : {type : 'select', data: projectName ,ext:{emptyOption:false}},
					 render: function (item) {
						 	file_id=renderId(item["projectNameId"],projectName);
			                return render(item["projectNameId"],projectName);
				}},
				{display: '制订/修订',width: '8%',name:'type',type:'text',required:true,
				editor: { type: 'select', data: type ,ext:{emptyOption:false}},
	            render: function (item) {
	                return render(item["type"],type);
	            }},
				{display: '制修订理由',width: '25%',name: 'reason',type:'text',required:true,editor : {type : 'textarea',maxlength:'1000'}},
				{display: '要求',width: '25%',name: 'requirements',type:'text' ,required:true,editor : {type : 'textarea',maxlength:'1000'}},
				{display: '完成时限',width: '8%',name: 'endDate',type: 'date', format: 'yyyy-MM-dd',maxlength:'1000',required:true,editor : { type: 'date'} },
				{display: '提出部门',width: '10%',name: 'forwardBm',type:'text',required:true,editor : {type : 'text',maxlength:'200'}},
				{display: '提出人',width: '10%',name: 'forwardMan',type:'text',required:true,editor : {type : 'text',maxlength:'200'}}
		);
	}
	//detail页面加载设备列表信息
    function createJrxxGrid(data) {
         var jrxx_html = '<div class="l-grid-body l-grid-body2 l-scroll"><div class="l-grid-body-inner"><table id="table4" class="l-detail-table b-dyn-table decimal-table"><tr class="l-t-td-title" style="height:30px;font-weight:normal">'
                          +'<td class="l-t-td-title" style="width:15%">项目名称</td>'
                          +'<td class="l-t-td-title" style="width:5%">制订/修订</td>'
                          +'<td class="l-t-td-title" style="width:25%">制修订理由</td>'
                          +'<td class="l-t-td-title" style="width:25%">要求</td>'
                          +'<td class="l-t-td-title" style="width:10%">完成时限</td>'
                          +'<td class="l-t-td-title" style="width:10%">提出部门</td>'
                          +'<td class="l-t-td-title" style="width:10%">提出人</td>';  
        if(data && data.length > 0){
        	 for(var i=0;i<data.length;i++){ 
        		 var projectName="";
            	 var type=""; 
            	 var reason="";
            	 var requirements="";
            	 var endDate="";
            	 var forwardBm="";
            	 var forwardMan="";
        		  if(data[i].projectName!=null){
        			  projectName=data[i].projectName;
        		  }
        		  if(data[i].type!=null){
        			  type=data[i].type;
        		  }
        		  if(data[i].reason!=null){
        			  reason=data[i].reason;
        		  }
        		  if(data[i].requirements!=null){
        			  requirements=data[i].requirements;
        		  }
        		  if(data[i].endDate!=null){
        			  endDate=data[i].endDate;
        		  }
        		  if(data[i].forwardBm!=null){
        			  forwardBm=data[i].forwardBm;
        		  }
        		  if(data[i].forwardMan!=null){
        			  forwardMan=data[i].forwardMan;
        		  }
        		  
        		  
                jrxx_html += '<tr class="l-t-td-right center" style="height:30px">'
                               +'<td class="l-grid-row-cell" style="text-align:center;height:30px">'+projectName
                               +'</td><td class="l-grid-row-cell" style="text-align:center">' +type
                               +'</td><td class="l-grid-row-cell" style="text-align:center">' +reason
                               +'</td><td class="l-grid-row-cell" style="text-align:center">' +requirements
                               +'</td><td class="l-grid-row-cell" style="text-align:center">'+endDate
                               +'</td><td class="l-grid-row-cell" style="text-align:center">'+forwardBm
                               +'</td><td class="l-grid-row-cell" style="text-align:center">'+forwardMan
                               
            }
        }else{
        	jrxx_html += '<tr><td colspan="5">无数据</td></tr>';
        }
        jrxx_html += '</table></div></div>';
        $("#device").html(jrxx_html);
    }
	var deviceGrid;

	 function initGrid(){
		 deviceGrid = $("#device").ligerGrid({
	    	columns: columns,
	    	enabledEdit: pageStatus != "detail",
	    	rownumbers : true,
		    width:"99.6%",
		    height:"400px",
		    frozenRownumbers: false,
		    usePager: false,
	        data: {Rows: []},
	    	onAfterEdit:chenckDevice 
    	});
	 } 
	 var file_id="";
	 function chenckDevice(e){
		 var grid=deviceGrid;
		 var data={projectName:file_id};
			grid.updateRow(grid.getSelectedRow(),data);
	 }
	 function renderId(value,data){
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
		   // alert(value);
		    return value;
		}
	 function initGridFalse(){
	        deviceGrid = $("#device").ligerGrid({
		    	columns: columns,
		    	enabledEdit: false,
		    	rownumbers : true,
			    width:"99.6%",
			    frozenRownumbers: false,
			    usePager: false,
		        data: {Rows: []} 
	    	});
	 } 
	 
	 function delDevice(row,index){
		 row.deleteSelectedRow();
	 }
	function save(){
		 if ($("#formObj").validate().form()) {
			 $("body").mask("正在保存数据，请稍后！");
			 var formData = $("#formObj").getValues();
			 formData.instructionInfo=deviceGrid.getData();
	        var instruction=$.ligerui.toJSON(formData);
	        $.ajax({
                url: "com/tjy2/instruction/saveBasic.do",
                type: "POST",
                data:{"instruction":instruction},
                success : function(data, stats) {
					$("body").unmask();
					if (data["success"]) {
						top.$.dialog.notice({
							content : '保存成功'
						});
						api.data.window.Qm.refreshGrid();
						api.close();
					} else {
						$.ligerDialog.error('提示：' + data.message);
					}
				},
                error : function(data) {
                    $("body").unmask();
                    $.ligerDialog.error('保存数据失败！');
                }
            });
		 }
	}
	function close(){	
		 api.close();
	}		
	//详情页面查询设备列表
	function queryEquipmentList1(){
		$.ajax({
        	url: "com/tjy2/instruction/getDetail.do?id=${param.id}",
            type: "POST",
            success: function (resp) {
                if (resp.success) {
                	var equipmentApplyLists = resp.data.instructionInfo;
                	//将设备显示在申请列表里面
                	createJrxxGrid(equipmentApplyLists);
                }else{
                	$.ligerDialog.error('提示：' + data.msg);
                }
            },
            error: function (data0,stats) {
                $.ligerDialog.error('提示：' + data.msg);
            }
        });
	}
	//其他页面查询设备列表
	function queryEquipmentList(){
		$.ajax({
        	url: "com/tjy2/instruction/getDetail.do?id=${param.id}",
            type: "POST",
            success: function (resp) {
                if (resp.success) {
                	var instructionInfo = resp.data.instructionInfo;
                	//将设备显示在申请列表里面
                	 for(var e in instructionInfo){
                		var bb = {id : instructionInfo[e].id,
                				projectNameId:instructionInfo[e].projectNameId,
                				projectName: instructionInfo[e].projectName,
                				type : instructionInfo[e].type,
                				reason: instructionInfo[e].reason,
                				requirements : instructionInfo[e].requirements,
                				endDate: instructionInfo[e].endDate,
                				forwardBm: instructionInfo[e].forwardBm,
                				forwardMan: instructionInfo[e].forwardMan};
						deviceGrid.addRow(bb);
                	}  
                }else{
                	$.ligerDialog.error('提示：' + data.msg);
                }
            },
            error: function (data0,stats) {
                $.ligerDialog.error('提示：' + data.msg);
            }
        });
	}
	//申请人选择
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
	            $("#applyId").val(p.id);
	            $("#applyName").val(p.name);
	            $("#applyUnitId").val(p.org_id);
	            $("#applyUnitName").val(p.org_name);
	        }
	    });
    }
	//合同签订人选择
	function choosePerson1(){
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
	            $("#signedMan").val(p.name);
	        }
	    });
    }
  
      </script>
      
<style type="text/css" media="print" id="pstyle">
* {font-family:"宋体";font-size:12px;letter-spacing:normal;}
* {
    font-family:"宋体";
    font-size:12px;
    letter-spacing:normal;
    
}
h1{font-family:宋体;font-size:6mm; text-align:center;margin:10px 0 0 0;}
table{ margin:-2 auto;width: 650px;}
table td{ height:40px;}
.l-detail-table td, .l-detail-table {
    border-collapse: collapse;
    border: 1px solid black;
}
#table1,#table2,#table3{
 padding:5px;
    border:0px solid #CFE3F8;
    border-top:0px;
    border-left:0px;
    word-break:break-all;
    table-layout:fixed;
}
#table4{
 padding:5px;
    border:0px solid #CFE3F8;
    border-top:0px;
    border-left:0px;
    word-break:break-all;
    table-layout:fixed;
    text-align:center;
    margin-left:48px;
}
.l-t-td-left{ text-align:center;}
.fieldset-caption{margin:15px 0px 2px 0px;}
.l-t-td-title{}
</style>
</head>
<body>
	<div title="计划表表" id="form1">
	<form name="formObj" id="formObj" method="post" 
		getAction="com/tjy2/instruction/getDetail.do?id=${param.id}">
		<h1 style="padding:5mm 0 2mm 0;font-family:微软雅黑;font-size:6mm;text-align:center;">作&nbsp;业&nbsp;指&nbsp;导&nbsp;书&nbsp;制&nbsp;修&nbsp;订&nbsp;需&nbsp;求&nbsp;计&nbsp;划&nbsp;表</h1></br>
		<input type="hidden" id="id" name="id"/>
		<input type="hidden" name="status"/>
		<input type="hidden" name="createDate"/>
		<input type="hidden" name="createMan"/>
		<input type="hidden" name="auditDate"/>
		<input type="hidden" name="auditMan"/>
		<input type="hidden" name="signDate"/>
		<input type="hidden" name="signMan"/>
		<input type="hidden" name="createId"/>
		<input type="hidden" name="auditId"/>
		<input type="hidden" name="signId"/>
		<input type="hidden" name="auditOpinion"/>
		
			<!-- <table id="table1" cellpadding="3" cellspacing="0" class="l-detail-table">
			     <tr>
					<td class="l-t-td-left"></td>
					<td class="l-t-td-right" >
					</td>
					<td class="l-t-td-left">编号</td>
					<td class="l-t-td-right">
					<input type="text" ltype='text'
					 validate="{required:true}" name="instructionNumber" id="instructionNumber" />
					</td>
				</tr>
				</table> -->
			<div id="device" style="width:100%"></div>
			<%if(status.equals("detail")){ %>
			<table id="table2" cellpadding="3" cellspacing="0" class="l-detail-table">
			     <tr>
			     <td class="l-t-td-left">编制</td>
					<td class="l-t-td-right">
					<input type="hidden" name="createId"/>
					<input type="text" ltype='text'
					 validate="{required:true}" name="createMan" id="createMan" />
					</td>
				<td class="l-t-td-left">编制日期</td>
					<td class="l-t-td-right">
					<input type="text" ltype='date'
					 validate="{required:true}" name="createDate" id="createDate" ligerui="{initValue:'',format:'yyyy-MM-dd'}"/>
					</td>
			     </tr>
			      <tr>
			     <td class="l-t-td-left">审核</td>
					<td class="l-t-td-right">
					<input type="hidden" name="auditId"/>
					<input type="text" ltype='text'
					 validate="{required:true}" name="auditMan" id="auditMan" />
					</td>
				<td class="l-t-td-left">审核日期</td>
					<td class="l-t-td-right">
					<input type="text" ltype='date'
					 validate="{required:true}" name="auditDate" id="auditDate" ligerui="{initValue:'',format:'yyyy-MM-dd'}"/>
					</td>
			     </tr>
			      <tr>
			     <td class="l-t-td-left">批准</td>
					<td class="l-t-td-right">
					<input type="hidden" name="signId"/>
					<input type="text" ltype='text'
					 validate="{required:true}" name="signMan" id="signMan" />
					</td>
				<td class="l-t-td-left">批准日期</td>
					<td class="l-t-td-right">
					<input type="text" ltype='date'
					 validate="{required:true}" name="signDate" id="signDate" ligerui="{initValue:'',format:'yyyy-MM-dd'}"/>
					</td>
			     </tr>
				</table>
				<%} %>
	</form>
	</div>
</body>
</html>
