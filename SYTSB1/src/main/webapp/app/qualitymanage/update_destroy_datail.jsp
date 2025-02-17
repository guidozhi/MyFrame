<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<head pageStatus="${param.pageStatus}">
    <title></title>
    <%@include file="/k/kui-base-form.jsp" %>
	<script type="text/javascript" src="pub/bpm/js/util.js"></script>
    <script type="text/javascript">
    var tbar="";
    var tbar="";
    var isChecked="${param.isChecked}";
    var serviceId = "${requestScope.serviceId}";//提交数据的id
	var activityId = "${requestScope.activityId}";//流程id
	var processId = "${requestScope.processId}";//退回id

	var areaFlag;//改变状态
     <bpm:ifPer function="TJY2_ZL_XH_FGYLD" activityId = "${requestScope.activityId}">areaFlag="1";</bpm:ifPer>//分管领导
  	 <bpm:ifPer function="TJY2_ZL_XH_DWFZR" activityId = "${requestScope.activityId}">areaFlag="2";</bpm:ifPer>//单位负责人
 	$(function () {
 		if(isChecked!="" && typeof(isChecked)!="undefined"){
        	$("#xh").transform("detail",function(){});
   	    	$("#xh").setValues("update/destroy/detail.do?id=${requestScope.serviceId}");
        	 tbar=[{ text: '审核不通过', id: 'del', icon: 'forbid', click: nosubmitSh},
                   { text: '审核通过', id: 'submit', icon: 'accept', click: submitSh},
                   { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
        } else {
            tbar=[{ text: '保存', id: 'up', icon: 'save', click: directChange},
                { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
            
        }
        if ("${param.pageStatus}"=="detail")
        tbar=[{ text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
    	$("#form1").initForm({
            showToolbar: true,
            toolbarPosition: "bottom",
            toolbar: tbar
    	});
    });
 	function submitSh(){
    	var serviceId = "${requestScope.serviceId}";//提交数据的id
    	var activityId = "${requestScope.activityId}";//流程id
        $.ligerDialog.confirm('是否通过审核？', function (yes){
        if(!yes){return false;}
         $("body").mask("提交中...");
         getServiceFlowConfig("TJY2_ZL_XH","",function(result,data){
                if(result){
                     top.$.ajax({
                         url: "update/destroy/xhsh.do?id="+serviceId+
                        		 "&typeCode=TJY2_ZL_XH&status="+"&activityId="+activityId+
                        		 "&areaFlag="+areaFlag,
                         type: "GET",
                         dataType:'json',
                         async: false,
                         success:function (data) {
                             if (data) {
                                $("body").unmask();
                          	  top.$.notice('审核成功！！！',3);	
                                api.data.window.Qm.refreshGrid();//刷新
                                api.close();
                             }
                         },
                         error:function () {
                        	 $.ligerDialog.error('出错了！请重试！!',3);	
                              $("body").unmask();
                          }
                     });
                }else{
                	$.ligerDialog.error('出错了！请重试！!',3);	
                 	$("body").unmask();
                }
             });
        });
    	
    }
 	 function nosubmitSh(){
     	$.ligerDialog.confirm('是否要不通过审核？', function (yes){
            if(!yes){return false;}
     	 $("body").mask("正在处理，请稍后！");
     	 
     	 getServiceFlowConfig("TJY2_ZL_XH","",function(result,data){
              if(result){
                   top.$.ajax({
                       url: "update/destroy/noxhsh.do?id="+serviceId+
                      		 "&typeCode=TJY2_ZL_XH&status="+"&activityId="+activityId+
                      		 "&areaFlag="+areaFlag+"&processId="+processId,
                       type: "GET",
                       dataType:'json',
                       async: false,
                       success:function (data) {
                           if (data) {
                              $("body").unmask();
                        	  top.$.notice('操作成功！！！',3);	
                              api.data.window.Qm.refreshGrid();//刷新
                              api.close();
                           }
                       },
                       error:function () {
                    	   $.ligerDialog.error('出错了！请重试！!',3);	
                           $("body").unmask();
                       }
                   });
              }else{
            	  $.ligerDialog.error('出错了！请重试！!',3);	
               $("body").unmask();
              }
           });
      });
     }
        function directChange(){ 
        	var obj=$("#form1").validate().form();
    	 if(obj){
    		 $("#form1").submit();
    	 }else{
    		 return;
    	}} 
        function choosefile(){
            top.$.dialog({
            	width: 900,
                height: 450,
                lock: true,
                parent: api,
                title: "选择文件",
                content: 'url:app/qualitymanage/file_list.jsp',
                cancel: true,
                ok: function(){
                    var p = this.iframe.contentWindow.getSelectedPerson();
                    if(!p){
                        top.$.notice("请选择文件！", 3, "k/kui/images/icons/dialog/face-sad.png");
                        return false;
                    }
                    $("#fileId").val(p.fileid);
                    $("#fileName").val(p.name);
                    $("#qualityXybzFileId").val(p.id);
                }
            });
        }
    </script>
</head>
<body>
	<form id="form1" action="update/destroy/save.do" getAction="update/destroy/detail.do?id=${param.id}">
		<input type="hidden" id="id" name="id"/>
		<input type="hidden" id="status" name="status"/>
		<input type="hidden" id="qualityXybzFileId" name="qualityXybzFileId"/>
		
		<fieldset class="l-fieldset">
			<legend class="l-legend">
				<div>销毁</div>
			</legend>
			<table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table" id="xh">
				<tr>
					<td class="l-t-td-left">文件名称</td>
	       				<td class="l-t-td-right" ><input  validate="{required:true}" ltype="text"  name="fileName" id="fileName" 
		         		type="text" ligerui="{readonly:true,value:'',iconItems:[{icon:'add',click:choosefile}]}" onclick="choosefile();" title="点击选择文件"/></td>
		         	
		         	<td class="l-t-td-left">文件编号</td>
		         	<td class="l-t-td-right"><input ligerui="{readonly:true,value:'',iconItems:[{icon:'add',click:choosefile}]}" onclick="choosefile();" ligerUi="{readonly:true}" title="点击选择文件" id="fileId" name="fileId" type="text" ltype="text" validate="{required:true}" /></td>
				</tr>
				<tr>
					<td class="l-t-td-left">销毁原因</td>
					<td class="l-t-td-right" colspan="3"><textarea name="destroyReason" id="destroyReason" rows="5" cols="25" class="l-textarea" validate="{maxlength:2000}" validate="{required:true}"></textarea></td>
				</tr>
			</table>
		</fieldset>
		<input type="hidden" id="fgyld" name="fgyld"/>
		<input type="hidden" id="fgyldTime" name="fgyldTime"/>
		<input type="hidden" id="fgyldyj" name="fgyldyj"/>
		<input type="hidden" id="dwfzr" name="dwfzr"/>
		<input type="hidden" id="dwfzrTime" name="dwfzrTime"/>
		<input type="hidden" id="dwfzryj" name="dwfzryj"/>
		
	</form>
</body>
</html>