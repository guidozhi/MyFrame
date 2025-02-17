<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html;charset=UTF-8"%>

<%SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	String nowTime=""; 
	nowTime = dateFormat.format(new Date());%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
<title></title>
<%@include file="/k/kui-base-form.jsp"%>
<script type="text/javascript" src="pub/bpm/js/util.js"></script>
<script type="text/javascript">
	$(function () {
		 $("#form1").initForm({    //参数设置示例
		       toolbar:[
		            { text:'保存', id:'save',icon:'save', click:saveAdd },
		            { text:'关闭', id:'close',icon:'cancel', click:close }
		        ],
		         showToolbar: true,
	             toolbarPosition: "bottom",
	    		 success: function (response) {
					if(response.success){
						top.$.notice("保存成功！",3);
						api.close();
					}else{
						$.ligerDialog.error("操作失败！<br/>" + response.msg);
					}
		 		}	
		 });
	});
		 
	function saveAdd(){
		var radio= $("input[name='status']:checked").val();
		if(radio == null || radio =="" ){
			 $.ligerDialog.warn('请选择完成进度!');
			 return;
		}
		 if(radio=='WWC'){
			 var unfinishedTask = $("#unfinishedTask").val();
			 if(unfinishedTask=='' || unfinishedTask==undefined){
				 $.ligerDialog.warn('请填写未完成原因!');
			 }else{
				 $("#form1").submit();
			 }
		 }else{
			 var feedbackRemark = $("#feedbackRemark").val();
			 if(feedbackRemark=='' || feedbackRemark==undefined){
				 $.ligerDialog.warn('请填写完成情况!');
			 }else{
				 $("#form1").submit();
				 api.data.window.Qm.refreshGrid();
			 } 
		 }
	 }
		
	function delayShow(){
		 var radio= $("input[name='status']:checked").val();
		 if(radio=='WWC'){
		     $("#feedbackRemark").val("");
			 $("#feedbackRemark1").hide();
			 $("#unfinishedTask1").show();
			 $("#unfinishedTask").attr("readonly",false);
		 }else if(radio=='YWC'){
		     $("#unfinishedTask").val("");
			 $("#unfinishedTask1").hide();
			 $("#feedbackRemark1").show();
			 $("#feedbackRemark").attr("readonly",false);
		 }else if(radio=='JXZ'){
		     $("#unfinishedTask").val("");
			 $("#feedbackRemark1").show();
			 $("#unfinishedTask1").hide();
			 $("#feedbackRemark").attr("readonly",false);
		 }else{
			 $("#feedbackRemark1").show();
			 $("#unfinishedTask1").show();
		 }
		 
	 }
		function close(){
			api.close();
		}
	
</script>

</head>
<body>

    <form id="form1" action="task/Feedback/saveWei.do" method="get" getAction="task/Feedback/fdetail.do?id=${param.id}" >
        <input type="hidden" name="id">
        <input type="hidden" value="${param.id}" id="ids" name="ids">
        <input type="hidden" name="weightyTask.id" value="${param.id}"/></br>
        <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
	       <tr> 
		        <td class="l-t-td-left">完成进度</td>
		        <td class="l-t-td-right">
		        <u:combo   name="status" ltype="radioGroup" code="TJY2_BG_RWFK" attribute="onChange:function(){delayShow();}"/>
		        </td>
		   </tr>
	       <tr> 
	        <td class="l-t-td-left">
		        <table id="feedbackRemark1" cellpadding="3" cellspacing="0" width="800px" class="l-detail-table1">
		        <td class="l-t-td-left">完成情况</td>
		        
		        <td colspan="3" class="l-t-td-right"> 
		       		 <textarea  style="height: 100px" name="feedbackRemark" id="feedbackRemark" type="text" ltype='text' readonly="readonly" validate="{required:false,maxlength:2000}"></textarea>
		        </td>
		        </table>
	        </td>
       </tr>
        <tr> 
	        <td class="l-t-td-left">
		  		 <table id="unfinishedTask1" cellpadding="3" cellspacing="0" width="800px" class="l-detail-table1">
			        <td class="l-t-td-left"> 未完成原因</td>     
		        <td colspan="3" class="l-t-td-right"> 
		      		  <textarea  style="height: 100px" name="unfinishedTask" id="unfinishedTask" type="text" ltype='text' readonly="readonly" validate="{required:false,maxlength:2000}"></textarea>
		        </td>
		        </table>
	        </td>
       </tr>
      </table>
    </form>
    <script type="text/javascript">
	        $("#form1").initForm({
				// opType:"auto",//数据保存模式，发现actionParam的关联信息为空时会自动调用opTypeFun，默认为auto
				// opTypeFun:save,//自动保存调用方法，opType为atuo时才会用到,默认为save
				// action:"",//保存数据或其它操作的action
	            actionParam:{"weightyTask.id":$("#form1>#ids")}, //保存时会在当前表单上附加此数据，如：{fkId:$("#form1>#id")}会把from1下的name为id的值带上去
				 delAction:'task/Feedback/getDelete.do',//删除数据的action
				 // delActionParam:{"id":$},//默认为选择行的id
				 // getAction:"",//取数据的action
				 // getActionParam:{},//取数据时附带的参数，一般只在需要动态取特定值时用到
	            onSelectRow:function (rowdata, rowindex) {
	                $("#form1").setValues(rowdata);
	            },
				// toolbar:[
				//  { text:'保存', click:function(){$("#formJc").submit();}, icon:'save'},
				//  { text:'删除', click:function(){$("#formJc").submit();}, icon:'save'}
				//  ],
	            columns:[
	                //此部分配置同grid
	                { display:'主键', name:'id', width:50, hide:true},
	                { display:'完成进度', name:'status', width:200},
	                { display:'完成情况', name:'feedbackRemark', width:200},
	                { display:'未完成原因', name:'unfinishedTask', width:200}
	            ]
	        });
    	</script>
    
</body>
</html>