﻿<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus = "${param.pageStatus}">
<title>部门管理详情</title>
<%@ include file="/k/kui-base-form.jsp"%>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<link type="text/css" rel="stylesheet" href="app/finance/css/form_detail.css" />
<script type="text/javascript">
	var pageStatus = "${param.pageStatus}";
	var tbar="";
	$(function() {
		tbar=[{text: "保存", icon: "save", click: function(){
	    	if ($("#form1").validate().form()) {
	    		$("#form1").submit();
	    	}else{
	    		$.ligerDialog.error('提示：' + '请填写完整后保存！');
	    	}}},
			{text: "关闭", icon: "cancel", click: function(){api.close();}}];
		$("#form1").initForm({
			success: function (response) {//处理成功
	    		if (response.success) {
	            	top.$.dialog.notice({
	             		content: "保存成功！"
	            	});
	         		api.data.window.refreshGrid();
	            	api.close();
	    		} else {
	           		$.ligerDialog.error(response.data);
	      		}
			}, getSuccess: function (response){
				var orgidLeaderid = response.data;
	        	if(orgidLeaderid!=null&&orgidLeaderid!=""){
        			$("#form1").setValues(orgidLeaderid);
            		var readers = [];
                    var names = [];
                    var ids = [];
                    if(orgidLeaderid.orgName){
                         names = orgidLeaderid.orgName.split(",");
                         ids = orgidLeaderid.fkSysOrgId.split(",");
                         for(var i=0;i<names.length;i++){
                            readers.push({
                                types : "reader",
                                name: names[i],
                                id: ids[i]
                            });
                        } 
                        addReader(readers,false);
                    }
	        	}
			},
			showToolbar: true,
	        toolbarPosition: "bottom",
	        toolbar: tbar
    	});
	})	
	//院领导选择
	function choosePerson(){
	            top.$.dialog({
	                width: 800,
	                height: 450,
	                lock: true,
	                parent: api,
	                title: "选择院领导",
	                content: 'url:app/humanresources/department/head_scjy_choose.jsp',
	                cancel: true,
	                ok: function(){
	                    var p = this.iframe.contentWindow.getSelectedPerson();
	                    if(!p){
	                        top.$.notice("请选择院领导！", 3, "k/kui/images/icons/dialog/32X32/hits.png");
	                        return false;
	                    }
	                    $("#fkRlEmplpyeeId").val(p.id);
	                    $("#empName").val(p.name);
	                }
	            });
	        } 
	function selectReaders(){
        
        var readers = $("#form1").data("readers"); 
        selectUnitOrUser("0",1,"","",function(datas){
            if(!datas.code)return;
            var codeArr = datas.code.split(",");
            var nameArr = datas.name.split(",");
            var readers = [];
            var existReaders = $("#form1").data("readers")||[];
            for(var i in codeArr){
                var exist = false;
                for(var j in existReaders){
                    if(existReaders[j].stakeholderId == codeArr[i] && existReaders[j].valid == true)
                        exist=true;
                }
                if(exist) continue;
                readers.push({
                    types : "reader",
                    name: nameArr[i],
                    id: codeArr[i]
                });
            }
            addReader(readers,true);
        });
    }
	var CCnames='';//负责人姓名
    var CCids='';//负责人id
	function addReader(newReaders,isNew){
		var names = '';
		var ids = '';
		var repids='';
		var repNames='';
		//alert(newReaders);
		for(var i in newReaders){
			if(CCids.indexOf(newReaders[i].id)==-1){
				names = names+newReaders[i].name+","; 
                 ids = ids+newReaders[i].id+",";
                 var mtext='<span class="label label-read" id="' + (isNew?newReaders[i].id:newReaders[i].id) + '"><span class="text">' + newReaders[i].name;
                 if(pageStatus!="detail"){
                    mtext = mtext+'</span><span class="del btn btn-lm" onclick="deleteReader(\'' + (isNew?newReaders[i].id:newReaders[i].id) + '\',' + isNew + ')"><img src="k/kui/images/icons/16/delete1.png">&nbsp;</span></span>';
                 }
                 if(pageStatus=="detail"){
                    mtext = mtext+',';
                  }
                 $("#reader_td").prepend(mtext);
             } else if (newReaders[i].name){
                 repNames = repNames+newReaders[i].name+",";
             }
        }
        if(repNames != ""){
                 $.ligerDialog.warn("部门："+"<span style='color:red;'>"+repNames.substring(0,repNames.length-1)+"</span>"+" 已存在！");
        }
        CCnames = CCnames+names;
        CCids = CCids+ids;
        $("#orgName").val(CCnames.substring(0, CCnames.length-1));
        $("#fkSysOrgId").val(CCids);
	}
	function deleteReader(id,isNew){
		$("#"+id).remove();
		var ids = CCids.split(",");
		var names = CCnames.split(",");
		for(var i in ids){
			if(id==ids[i]){
                ids[i]="";
                names[i]="";
            }
		}
		var cid="";
		var cname="";
		for(var i in ids){
			if(ids[i]!=""){
                cid = cid+ids[i]+',';
                cname = cname+names[i]+',';
            }
		}
		CCnames = cname;
		CCids = cid;
		$("#orgName").val(CCnames);
		$("#fkSysOrgId").val(CCids);
	}
</script>
</head>
<body>
    <form id="form1" action="orgidLeaderidAction/saveRelate.do?status=${param.pageStatus}" getAction="orgidLeaderidAction/detail.do?id=${param.id}">
     <input type="hidden" name="id" id="id"/>
     <input type="hidden" name="fkRlEmplpyeeId" id="fkRlEmplpyeeId"/>
     <input type="hidden" name="fkSysOrgId" id="fkSysOrgId"/>
     <input type="hidden" id="createDate" name="createDate"/>
     <input type="hidden" id="createId" name="createId"/>
     <input type="hidden" id="createBy" name="createBy"/>
       <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
	  <tr> 
        <td class="l-t-td-left"> 院领导</td>
        <td class="l-t-td-right"> 
        <input name="empName" id="empName" type="text" ltype='text' validate="{required:true}" readonly="readonly" onclick="choosePerson();" ligerui="{iconItems:[{icon:'user',click:choosePerson}]}"/>
        </td>
      </tr>
      <tr>
         <td class="l-t-td-left">所管部门</td>
        	<td class="l-t-td-right" id="reader_td">
        	<input type="hidden" name="orgName" id="orgName"/>
         <c:if test="${param.pageStatus=='modify' or param.pageStatus=='add'}"><span class="l-button label" title="添加部门">
   		<span  class="l-icon l-icon-add"  onclick="selectReaders();">&nbsp;</span>
    	 </span></c:if>
     	</td>
   	</tr>
      </table>
    </form> 
</body>
</html>
