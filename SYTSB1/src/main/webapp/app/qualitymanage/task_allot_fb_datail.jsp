<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    CurrentSessionUser user=(CurrentSessionUser)request.getSession().getAttribute("currentSessionUser");

%>
<%SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	String nowTime=""; 
	nowTime = dateFormat.format(new Date());%>

<head pageStatus="${param.pageStatus}">
    <title></title>
    <!-- 每个页面引入，页面编码、引入js，页面标题 -->
    <%@include file="/k/kui-base-form.jsp" %>
    <script type="text/javascript" src="pub/bpm/js/util.js"></script>
    <script type="text/javascript">
    $(function() {
		$("#form1").initForm({
			        success: function (response) {
						if(response.success){
							top.$.notice("保存成功！",3);
							api.data.window.Qm.refreshGrid();
							api.close();
						}else{
							$.ligerDialog.error("操作失败！<br/>" + response.msg);
						}
			        },
			afterParse:function(){
				if("${param.pageStatus}"=="detail" && "${param.status}"!="WXF"){
	    			$("#fankui").show();
	    		} else {
	    			//$("#fankui").hide();
	    			$("#fankui").css('display','block'); 
	    		}
			}
		});
    })
    		
    </script>
</head>

<style type="text/css">
    .l-t-td-right1 .l-text{
		background-color: rgb(255, 255, 255);
    	box-shadow: none;
		border: none;
		background-image: none;
		width:35%
	}
</style>
<body>
<div class="navtab">
<div  title="任务书信息" lselected="true">
	<form id="form1" action="taskAllot/allot/save.do" getAction="taskAllot/allot/detail.do?id=${param.id}">
		<input type="hidden" id="id" name="id"/>
		<input type="hidden" id="dutyId" name="dutyId" />
		<input type="hidden" id="dutyDepId" name="dutyDepId" />
		<h1 class="l-label" align="center" style="padding:5mm 0 2mm 0;font-family:微软雅黑;font-size:6mm;">任&nbsp;务&nbsp;书 </h1><div style="height:2px">&nbsp;</div>
		
		<table border="0" cellpadding="3" cellspacing="0" width=""
				class="l-detail-table" style="width:48.8%">
            	 <tr>
                    <td class="l-t-td-left">编号：</td>
                    <td class="l-t-td-right1"><input type="text" ltype="text" name="taskSn" readonly="readonly"  /></td>
                 </tr>
            </table>
		<table border="1" cellpadding="3" cellspacing="0" width=""
				class="l-detail-table" id="zlrw">
					<tr>
						<td class="l-t-td-left">负责人</td>
						<td class="l-t-td-right"><input type="text" ltype="text" name="dutyName" id="dutyName" validate="{required:true}" /></td>
						<td class="l-t-td-left">责任部门</td>
						<td class="l-t-td-right"><input type="text" ltype="text" name="dutyDep" id="dutyDep" /></td>
					</tr>
					<tr>
						<td class="l-t-td-left">登记人</td>
						<td class="l-t-td-right"><input type="text" ltype="text" name="registerName" id="registerName" /></td>
						<td class="l-t-td-left">登记时间</td>
						<td class="l-t-td-right"><input name="registerDate" id="registerDate" type="text" ltype="date" 
        					ligerui="{initValue:'',format:'yyyy-MM-dd'}"/></td>
					</tr>
					<tr>
						<td class="l-t-td-left">任务名称</td>
						<td class="l-t-td-right"><input type="text" ltype="text" name="itemName" id="itemName" validate="{required:true}" /></td>
						<td class="l-t-td-left">期望完成时间</td>
						<td class="l-t-td-right"><input name="itemDate" type="text" ltype="date" validate="{required:false}" 
        					ligerui="{initValue:'',format:'yyyy-MM-dd'}" id="itemDate"  
        					 readonly="readonly" value="<%=nowTime%>" /></td>
					</tr>
					<tr>
						<td class="l-t-td-left">任务内容</td>
						<td class="l-t-td-right" colspan="3"><textarea name="itemContent" id="itemContent" rows="8" cols="25" class="l-textarea" validate="{maxlength:2000}" validate="{required:true}"></textarea></td>
					</tr>
					<tr>
						<td class="l-t-td-left">任务要求</td>
						<td class="l-t-td-right" colspan="3"><textarea name="itemRequire" id="itemRequire" rows="8" cols="25" class="l-textarea" validate="{maxlength:2000}" validate="{required:true}"></textarea></td>
					</tr>
					<tr>
						<td class="l-t-td-left">备注</td>
						<td class="l-t-td-right" colspan="3"><textarea name="remark" id="remark" rows="8" cols="25" class="l-textarea" validate="{maxlength:2000}"></textarea></td>
					</tr>
					</table>
				</form>
				</div>
	
	<div title="反馈" id="fankui">
	<form id="formfk" name="formfk" method="post" action="task/Fk/saveFK.do"   getAction="task/Fk/getDetail.do?id=${param.id}">
  		<input type="hidden" name="id" />
        <input type="hidden" value="${param.id}" id="ids" name="ids" />
        <input type="hidden" name="taskAllot.id" value="${param.id}"/></br>
	<table cellpadding="3" cellspacing="0" class="l-detail-table"  >
		<tr> 
	        <td class="l-t-td-left">完成进度</td>
	        <td class="l-t-td-right"> 
	        <u:combo name="status" code="TJY2_BG_RWFK" />
	        </td>
	        <td class="l-t-td-left">延期至</td>
       		<td class="l-t-td-right"><input id="delay" name="delay" type="text" ltype="date" validate="{required:false}" 
        		ligerui="{initValue:'',format:'yyyy-MM-dd'}"/> </td>
       </tr>
       <tr> 
        <td class="l-t-td-left">完成情况</td>
        <td colspan="3" class="l-t-td-right1"> 
       		 <textarea name="feedback" type="text" ltype='text' validate="{required:true,maxlength:2000}"></textarea>
        </td>
       </tr>
      	<tr>
      		<td class="l-t-td-left">未完成理由</td>
      		<td class="l-t-td-right1" colspan="4"> 
        		<textarea name="unfinished" id="unfinished" rows="5" cols="25" class="l-textarea" validate="{maxlength:2000}"></textarea>
      	    </td>
      		
      	</tr>
	    
	   
	</table> 
      
      <script type="text/javascript">
	        $("#formfk").initFormList({
				// opType:"auto",//数据保存模式，发现actionParam的关联信息为空时会自动调用opTypeFun，默认为auto
				// opTypeFun:save,//自动保存调用方法，opType为atuo时才会用到,默认为save
				// action:"",//保存数据或其它操作的action
	            actionParam:{"taskAllot.id" : $("#form1>[name='id']")}, //保存时会在当前表单上附加此数据，如：{fkId:$("#form1>#id")}会把from1下的name为id的值带上去
	            delAction:'task/Fk/delete.do',//删除数据的action
				 // delActionParam:{"id":$},//默认为选择行的id
				 // getAction:"",//取数据的action
				 // getActionParam:{},//取数据时附带的参数，一般只在需要动态取特定值时用到
	            onSelectRow:function (rowdata, rowindex) {
	                $("#formfk").setValues(rowdata);
	            },
				// toolbar:[
				//  { text:'保存', click:function(){$("#formJc").submit();}, icon:'save'},
				//  { text:'删除', click:function(){$("#formJc").submit();}, icon:'save'}
				//  ],
	            columns:[
	                 {display:'主键', name:'id', width:'1%', hide:true},
	                 {display:'完成进度', name:'status',width:'15%'},
	                 {display:'完成情况',name:'feedback',width:'15%'},
	                 {display:'未完成理由',name:'unfinished',width:'15%'},
	                 {display:'延期至',name:'delay',width:'15%'}
	                 
	            	]
	        });
    	</script>
	</form>
	</div>
	
	</div>
</body>
</html>
