<%@page import="com.ctc.wstx.util.DataUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.khnt.rbac.impl.bean.Org"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.utils.DateUtil"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	CurrentSessionUser sessionUser = SecurityUtil.getSecurityUser();
	String userName=sessionUser.getName();
	String id=sessionUser.getId();
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	java.util.Date currentTime = new java.util.Date();//得到当前系统时间 
	String str_date1 = formatter.format(currentTime); //将日期时间格式化 
%>
<head pageStatus="${param.status}">
<%@ include file="/k/kui-base-form.jsp"%>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<link type="text/css" rel="stylesheet" href="app/finance/css/form_detail.css" />
<script type="text/javascript" src="app/fwxm/scientific/instruction/selectUser/selectUnitOrUser.js"></script>


<script type="text/javascript">
var pageStatus = "${param.status}"
var type = "${param.type}";
var tbar="";
var czuserids="";
var czusernames="";
var selector;
$(function() {
	if(pageStatus=="detail"){
		tbar=[{text : '关闭',icon : 'cancel',click : function() {api.close();}}];
	}else{
		if(type!='0' && type !='4' && type !='5'){
			tbar=[{text : '通过',icon : 'accept',click : sqjrTg},{text : '不通过',icon : 'forbid',click : sqjrNo}, {text : '取消',icon : 'cancel',click : function() {api.close();}}];
		}else if(type=='5'){
			tbar=[
			      	{text : '分配人员',icon : 'role',click : fpry},
					{text : '关闭',icon : 'cancel',click : function() {api.close();
					api.data.window.Qm.refreshGrid();}}
					];
		}else{
			tbar=[{text : '保存',icon : 'save',click : save},{text : '取消',icon : 'cancel',click : function() {api.close();}}];
		}
	}
	$("#formObj").initForm({
		toolbar : tbar,
		showToolbar: true,
		success : function(response) {//处理成功
			if (response.success) {
				top.$.notice("保存成功！");					
				api.close();
				api.data.window.Qm.refreshGrid();
			} else {
				$.ligerDialog.error('保存失败！<br/>' + response.msg);
			}
		},
		getSuccess: function (response) {
			if(response.success){
				czuserids=response.data.cz_user_ids;
				czusernames=response.data.cz_user_names;
			}
			else{
				$.ligerDialog.error("获取信息失败！<br/>" + response.msg);
			}
		}
		
	});
	
	//设置状态
	if(${param.type!="0"}){
		$("#jdlb").ligerComboBox({ disabled: true});
		$("#jdzl").ligerComboBox({ disabled: true});
	}
})

function fpry(){
	 selector = $.ligerDialog.open({ 
		 title: '选择', 
		 width: 500, 
		 height: 300,
		 parent:api, 
		 url: 'app/discipline/choose_user_list.jsp?czuserids='+czuserids,
		 data: {"window" : window},
		 buttons: [
		    { text: '确定', onclick: f_importOK },
			{ text: '取消', onclick: f_importCancel }
		 ]
 });
}

function f_importOK(item, selector){
	var f_rows = selector.frame.f_select();
    if (f_rows.length==0){
    	$.ligerDialog.error("请选择人员信息！");
        return;
    }
    var ids="";
    var names="";
    var nameList = new Array();
    for(var i =0;i<f_rows.length;i++){
    	ids=ids+f_rows[i].id+",";
    	names=names+f_rows[i].name+",";
    	nameList.push(f_rows[i].name);
    }
    ids=ids.substring(0,ids.length-1);
    names=names.substring(0,names.length-1);
    if(czusernames!=null && czusernames!=""){
    	var czusername=czusernames.split(",");
		for(var i=0;i<czusername.length;i++){
			if($.inArray(czusername[i],nameList)<0){
    			$.ligerDialog.error("必须包含工作人员："+czusername[i]);
    			return false;
    		}
		}
    }
    var fzrxf=$("#fzrxf").val();
    fzrxf=encodeURI(fzrxf);
    $("body").mask("正在处理请稍后...");
    $.ajax({
        url: "/com/zdsx/sqjr/sqjrSzCzr.do",
        data: {'id':"${param.id}","ids":ids,"names":names,"fzrxf":fzrxf},
        type: "post",
        async: true,
        success: function (data) {
       	 if(data.success){
       		 czuserids=ids;
     		 czusernames=names;
       		 $("#cz_user_ids").val(ids);
       		 $("#cz_user_names").val(names);
			 $("body").unmask("");
			 top.$.notice("设置成功！");					
			 selector.close();
       	 }
        }
    });
    
}
function f_importCancel(){selector.close();}

function sqjrTg(){
	 if ($("#formObj").validate().form()) {
		 $("body").mask("正在处理请稍后...");
         var formDatas = $("#formObj").getValues();
         formDatas.activity_id='${param.activity_id}';
         
         formDatas.process_id='${param.process_id}';
         var url="/com/zdsx/sqjr/sqjrTg.do"
         formDatas.type=1;
         if(${param.type=="3"}){//纪检监察审核
        	 url="/com/zdsx/sqjr/sqjrFlowEnd.do";
         }
         var formData=$.ligerui.toJSON(formDatas);
         $.ajax({
             url: url,
             data: {"entity":formData},
             type: "post",
             async: true,
             success: function (data) {
            	 if(data.success){
					 $("body").unmask("");
     				top.$.notice("保存成功！");					
    				api.close();
    				api.data.window.Qm.refreshGrid();
            		 
            	 }
             }
         });
// 			 url: "com/zdsx/sqjr/sqjrTg.do?activity_id=${param.activity_id}",
	 }
}
function sqjrNo(){
    var formDatas = $("#formObj").getValues();
    formDatas.activity_id='${param.activity_id}';
    formDatas.process_id='${param.process_id}';
    formDatas.type=2;
    if(${param.type==2}){
    	formDatas.bmfgy_time=<%=str_date1 %>
    }
    if(${param.type==1}){
    	formDatas.bmfzr_time=<%=str_date1 %>
    }
    if(${param.type==3}){
    	formDatas.jjfgy_time=<%=str_date1 %>
    }
    
    var formData=$.ligerui.toJSON(formDatas);
	 $("body").mask("正在处理请稍后...");
	$.ajax({
        url: "/com/zdsx/sqjr/sqjrFlowEnd.do",
        data: {"entity":formData},
        type: "post",
        async: true,
        success: function (data) {
       	 if(data.success){
			 $("body").unmask("");
				top.$.notice("保存成功！");					
				api.close();
				api.data.window.Qm.refreshGrid();
       		 
       	 }
        }
    });
}
function save(){
	var jdsj_start=$("#jdsj_start").val();
	var jdsj_end=$("#jdsj_end").val();
	if ($("#formObj").validate().form()) {
		var start = new Date(jdsj_start.replace("-", "/").replace("-", "/"));
		var end = new Date(jdsj_end.replace("-", "/").replace("-", "/"));
		if(end<start){  
			$.ligerDialog.error("监督开始时间不能大于结束时间！");
			return false;  
		}
		$("#formObj").submit();
	}
 }
function callUnit(id,text){
	$("#szbm_id").val(id);
	$("#szbm").val(text);
	$("#sqr_id").val("");
	$("#sqr").val(null);
}

function selectUnit(){
	top.$.dialog({
		width : 290,
		height : 420,
		lock : true,
		title : "选择所在部门",
		content : 'url:app/maintenance/choose_unit_list.jsp',
		data : {
			"window" : window
		}
	});
}

function selectUser(){
	var org_id = $("#szbm_id").val();
	var org_name = $("#szbm").val();
	if(org_id == "" || org_name == "" ){
		$.ligerDialog.alert("请先选择所在部门！");
		return;
	}
	top.$.dialog({
		width : 200,
		height : 420,
		lock : true,
		title : "选择申请人",
		content : 'url:app/maintenance/choose_user_list.jsp?org_id='+org_id,
		data : {
			"window" : window
		}
	});
}
function callUser(id,text){
	$("#sqr_id").val(id);
	$("#sqr").val(text);
}
</script>
</head>
<body>
	
<!--  -->
		<form name="formObj" id="formObj"  action="com/zdsx/sqjr/savebasic.do" getAction="com/zdsx/sqjr/detail.do?id=${param.id}">
			<h1 id="sg2" align="center" style="padding: 5mm 0 2mm 0; font-family: 微软雅黑; font-size: 6mm;">四川省特检院重大事项监督工作申请审批表</h1>
			<fieldset class="l-fieldset">
			<input  type="hidden" name="id" value="${param.id }"/>
			<input  type="hidden" name="create_time"/>
			<input  type="hidden" name="create_user_id"/>
			<input  type="hidden" name="create_user_name"/>
			<input  type="hidden" name="create_org_id"/>
			<input  type="hidden" name="create_org_name"/>
			<input  type="hidden" name="state"/>
			<input  type="hidden" name="sn"/>
			<input  type="hidden" name="bmfgy_time"/>
			<input  type="hidden" name="bmfzr_time"/>
			<input  type="hidden" name="jjfgy_time"/>
			<input  type="hidden" name="cz_user_ids"/>
			<table cellpadding="3" cellspacing="0" class="l-detail-table">
					<tr>
					<td class="l-t-td-left">申请人：</td>
				        <td class="l-t-td-right" colspan="2">
				        <input type="hidden" name="sqr_id" id="sqr_id" value="<%=id%>"/>
<%-- 				        <input type="text" id="sqr" name="sqr" ltype='text'  readonly="readonly" <c:if test="${ param.type!=0 }">ligerUi='{disabled:true}' </c:if> <c:if test="${param.type==0 }"> onclick='selectUser()' </c:if> validate='{required:true}'	value="<%=userName %>"	 /></td> --%>
				        <input type="text" id="sqr" name="sqr" ltype='text'  readonly="readonly" <c:if test="${ param.type!=0 }">ligerUi='{disabled:true}' </c:if>  validate='{required:true}'	value="<%=userName %>"	 /></td>
						<td class="l-t-td-left">所在部门：</td>
				 		<td class="l-t-td-right" colspan="2">
					 		<input type="hidden" name="szbm_id" id="szbm_id" value="<%=sessionUser.getDepartment().getId() %>"/>
<%-- 					 		<input type="text" id="szbm" name="szbm"  ltype="text"  <c:if test="${ param.type!=0 }">ligerUi='{disabled:true}'</c:if> <c:if test="${ param.type==0 }"> onclick='selectUnit()'</c:if> value="<%=sessionUser.getDepartment().getOrgName() %>" readonly="readonly" /> --%>
					 		<input type="text" id="szbm" name="szbm"  ltype="text"  <c:if test="${ param.type!=0 }">ligerUi='{disabled:true}'</c:if>  value="<%=sessionUser.getDepartment().getOrgName() %>" readonly="readonly" />
					 	</td>
					 </tr>
					 <tr>
						<td class="l-t-td-left">监督类别：</td>
				 		<td class="l-t-td-right" colspan="2">
					 		 <input name="jdlb" id="jdlb" type="text" ltype="select" validate="{required:true}" ligerui="{data: <u:dict code='zdsx_sqjr_jdlb'/>}"  ></input>
				 			 
				 		</td>
				 		<td class="l-t-td-left">监督种类：</td>
				 		<td class="l-t-td-right" colspan="2">
					 		<input name="jdzl" id="jdzl" type="text" ltype="select" validate="{required:true}" ligerui="{data: <u:dict code='zdsx_sqjr_jdzl'/>}"  ></input>
				 		</td>
					</tr>
					 <tr>
					 	<td class="l-t-td-left">监督工作事<br />由、事项</td>
					 	<td class="l-t-td-right" colspan="5"><input  name="jdgzsy" ltype="text" <c:if test="${ param.type!=0 }"> ligerUi='{disabled:true}' </c:if>  validate='{required:true}'/></td>
					 </tr>
					 <tr>
					 	<td class="l-t-td-left">监督开始时间</td>
					 	<td class="l-t-td-right" colspan="2">
					 	<c:if test="${ param.type!=0 }">
						 	<input name="jdsj_start" id="jdsj_start" type="text" ltype="date" validate="{required:true}" ligerui="{initValue:'',format:'yyyy-MM-dd',disabled:true}"  />
					 	</c:if>
					 	<c:if test="${ param.type==0 }">
					 	<input name="jdsj_start" id="jdsj_start" type="text" ltype="date" validate="{required:true}" ligerui="{initValue:'',format:'yyyy-MM-dd'}"  />
					 	</c:if>
					 	</td>
					 	<td class="l-t-td-left">监督结束时间</td>
					 	<td class="l-t-td-right" colspan="2">
					 	<c:if test="${ param.type!=0 }">
						 	<input name="jdsj_end" id="jdsj_end" type="text" ltype="date" validate="{required:true}" ligerui="{initValue:'',format:'yyyy-MM-dd',disabled:true}"  />
					 	</c:if>
					 	<c:if test="${ param.type==0 }">
					 	<input name="jdsj_end" id="jdsj_end" type="text" ltype="date" validate="{required:true}" ligerui="{initValue:'',format:'yyyy-MM-dd'}"  />
					 	</c:if>
					 	</td>
					 </tr>
					 <tr>
						<td class="l-t-td-left" rowspan="3">审批</td>
				 		<td class="l-t-td-left" >部门负责人意见：</td>
					 	<td colspan="4"  height="50px">
					 		<textarea type="text" id="bmfzryj" name="bmfzryj"  rows="3"  ltype='text' <c:if test="${param.type==1 }">  validate='{required:true}'	</c:if>	<c:if test="${param.type!=1 }">ligerUi='{disabled:true}' </c:if>	 ></textarea>
					 	</td>
					 </tr>
					<tr>
						<td class="l-t-td-left">部门分管院领导意见：</td>
					 	<td colspan="4"  height="50px">
					 		<textarea type="text" id="bmfgyyj" name="bmfgyyj"  rows="3"  ltype='text' <c:if test="${param.type==2 }">  validate='{required:true}'	</c:if> <c:if test="${param.type!=2 }">ligerUi='{disabled:true}' </c:if>	 ></textarea>
					 	</td>
					</tr>
					<tr>
						<td class="l-t-td-left">纪检分管院领导意见：</td>
					 	<td colspan="4"  height="50px">
					 		<textarea type="text" id="jjfgyyj"  name="jjfgyyj"  rows="3"  ltype='text' <c:if test="${param.type==3 }">  validate='{required:true}'	</c:if> <c:if test="${param.type!=3}">ligerUi='{disabled:true}' </c:if> 	 ></textarea>
					 	</td>
					</tr>
					<tr>
						<td class="l-t-td-left" rowspan="2">工作安排意见</td>
						<td class="l-t-td-left"> 负责人意见：
<!-- 							<table style="width:100%"> -->
<%-- 								<tr><td colspan="2"><input name="fzrxf" id="fzrxf" type="text" ltype="text" <c:if test="${ param.type!=4 && param.type!=5 }"> ligerUi='{disabled:true}' </c:if>/></td><tr> --%>
<!-- 								<tr><td style="width:13%" >已安排的工作人员：</td><td style="width:70%"><input name="cz_user_names" id="cz_user_names" type="text" ligerUi='{disabled:true}' ltype="text"/></td><tr> -->
<!-- 							</table> -->
						</td>
						<td class="l-t-td-right" colspan="4">
							<input name="fzrxf" id="fzrxf" type="text" ltype="text" <c:if test="${ param.type!=4 && param.type!=5 }"> ligerUi='{disabled:true}' </c:if>/>
						</td>
					</tr>
					<tr>
						<td class="l-t-td-left">已安排的工作人员：</td>
						<td class="l-t-td-right" colspan="4"><input name="cz_user_names" id="cz_user_names" type="text" ligerUi='{disabled:true}' ltype="text"/></td>
					</tr>
					<tr> 
						<td class="l-t-td-left">办理结果：</td>
						<td  colspan="5" >
							<textarea type="text" rows="3"   id="bljg" name="bljg" ltype="text" <c:if test="${ param.type!=4 }">ligerUi='{disabled:true}' </c:if> validate="{required:false}"></textarea>
						</td>
					</tr>
			</table>
	</fieldset>
	
</body>
</html>
