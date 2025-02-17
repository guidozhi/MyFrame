<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
<title>银行转账数据列表</title>
<%@include file="/k/kui-base-form.jsp"%>
<%
	Date date = new Date();
	String year = new SimpleDateFormat("yyyy").format(date);
	String month = new SimpleDateFormat("MM").format(date);
%>
<script type="text/javascript" src="pub/bpm/js/util.js"></script>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<script type="text/javascript">

var tbar="";
$(function () {
         tbar=[
               { text: '导入', id: 'save', icon: 'save',click:save},
             { text: '关闭', id: 'close', icon:'cancel', click:function(){api.data.window.Qm.refreshGrid();api.close();}}];
 	$("#form1").initForm({
         showToolbar: true,
         toolbarPosition: "bottom",
         toolbar: tbar,
         success: function (response) {
     		if(response.success){
     			top.$.notice("保存成功！",3);
     			api.data.window.Qm.refreshGrid();
     			api.close();
     		}
     		else{
     			$.ligerDialog.error("操作失败！<br/>" + response.msg);
     		}
     	}
 	});
 	
 });
function directChange(){ 
	var obj=$("#form1").validate().form();
 if(obj){
	 $("#form1").submit();
 }else{
	 return;
}}   
$(function(){
	
	importData();
});
function save(){
	var month = 2;
	if(month==null||month==""||month=="null"){
 		 return;
	}
	//alert(1)
	$("#imp").click();
	//importData();
}
	
         
        
function importData() {
    //创建上传实例
    khFileUploader = new KHFileUploader({
        fileSize: "10mb",//文件大小限制
         buttonId: "imp",//上传按钮ID 
        container: "filecontainer3",//上传控件容器ID
        title: "数据导入",//文件选择框提示
        saveDB : false,
        extName: "xls,xlsx",//文件扩展名限制
        fileNum: 1,//限制上传文件数目
        callback : function(files){
       saveData(files);
        }
    });
}


// 上传完成，开始保存汇编数据
	function saveData(files){

	var year = $("#salaryYear-txt").val();
	var month = $("#salaryTmonth-txt").val();
	if(month==null||month==""||month=="null"){
		var manager = $.ligerDialog.waitting('请选择导入工资的月份...');
		
        setTimeout(function (){manager.close();},1500);
	}else{
    $("body").mask("正在保存...");
    $.post("disciplinePlanAction/saveImport.do",{"year":$.ligerui.toJSON(year),"month":$.ligerui.toJSON(month),files:$.ligerui.toJSON(files)},function (data) {
            $("body").unmask();
            /* alert(data.repData!=""&&data.repData!='undefined'); */
            if (data.success) {
            	
                if(data.repData!=""&&data.repData!='undefined')/* {
                    $.ligerDialog.alert("保存成功！有交易日期重复数据：<br/>" + data.repData);
                } else */ {
                	api.data.window.Qm.refreshGrid();
                	top.$.notice("保存成功！");
					api.close();
                }
            } else {
                $.ligerDialog.error("保存失败！请确认&nbsp;<span style='color:red;'>"+data.result+"</span>&nbsp;在系统中的信息或在表格中的数据是否正确！<br/><span style='color:red;'>确认后，请删除刚导入的数据并重新导入！</span>");
                //api.close();
            }
       },"json");}
}
</script>
<style type="">
/* #filecontainer3{color: red;
left: 235px;
width: 30px;
height: 2px;} */
</style>
</head>
<body>

	 <form  id="form1" action="finance/importSalaryAction/saveim.do" >
	
		<table style="margin-top:5%;margin-right: 5%; ">
			<tr style="display: none">
     	 	  <td class="l-t-td-left" >月份/工资</td>
     		  <td colspan="3" >
    			    <input   id="salartTime" name="salartTime" type="text" ltype="date" ligerui="{initValue:'',format:'yyyy-MM'}" />
   		 	  </td>
   			</tr>
   			<tr>
   			<td class="l-t-td-left" >年</td>
   			<td>
   			<u:combo  name="salaryYear" code="TJY2_IMPGZBDN"/>
   			</td>
   			<td>月</td>
   			<td>
   			<u:combo name="salaryTmonth" code="TJY2_IMPGZBDY" />
   			</td>
   			</tr>
		</table>
		
	</form>
	<input type="hidden" id="imp"/>
	 <div id="filecontainer3"></div>
	
</body>

</html>
