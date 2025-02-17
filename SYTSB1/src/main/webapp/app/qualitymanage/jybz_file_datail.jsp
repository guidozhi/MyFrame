<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %> 
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
    <title></title>
    <%@include file="/k/kui-base-form.jsp" %>
	<script type="text/javascript" src="pub/bpm/js/util.js"></script>
    <script type="text/javascript">
    var tbar="";
    var code_zzzt = <u:dict code='device_classify'/>;
 	$(function () {
         tbar=[{ text: '保存', id: 'up', icon: 'save', click: directChange},
             { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
         if ("${param.pageStatus}"=="detail")
 	        tbar=[{ text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
         $("#form1").initForm({
            showToolbar: true,
            toolbarPosition: "bottom",
            toolbar: tbar
    	});
    });
        function directChange(){ 
        	var obj=$("#form1").validate().form();
            var d1=$("#effectiveStarttime").val();
            var d2=$("#effectiveStoptime").val();
        	if(obj){
	        	 top.$.ajax({
	 	            url: "xybz/file1/yxq.do?d1="+d1+"&d2="+d2,
	 	            type: "GET",
	 	            dataType:'json',
	 	            date: $("#form1").validate(),
	 	            success:function (data) {
	 	                if(data.success){
 	 	                	$("#form1").submit();
	 	                    Qm.refreshGrid();//刷新
	 	                }else{
	 	                	$.ligerDialog.error(data.msg);  
	 	                }
	 	             },
	 	             error:function () {
	 	            	 $.ligerDialog.error("保存失败！");
	 	             }
	 			});
    	 }else{
    		 return;
    	}} 
        function choosefile(){
            top.$.dialog({
            	width: 570,
                height: 450,
                lock: true,
                parent: api,
                title: "选择文件",
                content: 'url:app/qualitymanage/file_list2.jsp',
                cancel: true,
                ok: function(){
                    var p = this.iframe.contentWindow.getSelectedPerson();
                    if(!p){
                 		top.$.dialog.tips('请选择文件！',4,'k/kui/skins/icons/32X32/hits.png',null,0);
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
	<form id="form1" action="xybz/file1/save1.do" getAction="xybz/file1/detail.do?id=${param.id}">
		<input type="hidden" id="id" name="id"/>
		<input type="hidden" id="status" name="status"/>
		<input type="hidden" id="qualityXybzFileId" name="qualityXybzFileId"/>
		<%SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
					String nowTime=""; 
					nowTime = dateFormat.format(new Date());%>
		<fieldset class="l-fieldset">
					<legend class="l-legend">
						<div>
							检验标准文件
						</div>
					</legend>
		<table border="1" cellpadding="3" cellspacing="0" width=""class="l-detail-table" id="">
			<tr>
				<td class="l-t-td-left">文件名称</td>
       				<td class="l-t-td-right" colspan="3"><input  validate="{required:true}" ltype="text"  name="fileName" id="fileName" 
	         		type="text"/></td>
	         </tr>
<!-- 	       ligerui="{readonly:true,value:'',iconItems:[{icon:'search',click:choosefile}]}" onclick="choosefile();" title="点击选择文件"   - -->
<!--  ligerui="{readonly:true,value:'',iconItems:[{icon:'search',click:choosefile}]}" onclick="choosefile();" title="点击选择文件"           ligerUi="{readonly:true}"-->
	         <tr>
	         	<td class="l-t-td-left">文件编号</td>
	         	<td class="l-t-td-right" colspan="3"><input id="fileId" name="fileId" type="text" ltype="text" validate="{required:true}" /></td>
			</tr>
			
			<tr>
			    <td class="l-t-td-left">执行日期</td>
 	        	<td class="l-t-td-right"> 
 	        		<input id="effectiveStarttime" name="effectiveStarttime" type="text" ltype='date' ligerui="{readonly:true,format:'yyyy-MM-dd'}" value="<%=nowTime%>"  validate="{required:true}"/> 
 	        	</td> 
 	        	<td class="l-t-td-left">(到)</td> 
                <td class="l-t-td-right">
                    <input id="effectiveStoptime" name="effectiveStoptime" type="text" ltype='date' ligerui="{readonly:true,format:'yyyy-MM-dd'}"  validate="{required:true}" value="<%=nowTime%>"/>
                </td>
				
	        </tr>
	        <tr>
                <td class="l-t-td-left">特种设备类型</td>
                <td class="l-t-td-right"><u:combo name="tzEquipmentType" code="device_classify" validate="required:true" tree="true" attribute="treeLeafOnly:false"/></td>
                <td class="l-t-td-left">登记人</td>
                <td class="l-t-td-right">
                    <input id="registrant" name="registrant" type="text" ltype='text' validate="{required:true}" 
                    ligerUi="{disabled:true}" value="<sec:authentication property="principal.name"/>"/>
                </td>
            </tr>
	        <tr>
	            <td class="l-t-td-left">检验类型</td>
                <td class="l-t-td-right" colspan="3"><u:combo name="checkoutType" code="BASE_CHECK_TYPE" validate="required:true"  attribute="isMultiSelect:true" />
                </td>
	        </tr>
		</table><br/></fieldset>
	</form>
</body>
</html>