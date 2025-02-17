		<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %> 
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<%
CurrentSessionUser user = SecurityUtil.getSecurityUser();
User uu = (User)user.getSysUser();
com.khnt.rbac.impl.bean.Employee e = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
e.getId();
String userId=e.getId();
String uId = SecurityUtil.getSecurityUser().getId();
%>
<head pageStatus="${param.pageStatus}">
    <title></title>
    <%@include file="/k/kui-base-form.jsp" %>
	<link type="text/css" rel="stylesheet" href="app/qualitymanage/css/form_detail.css" />
	<script type="text/javascript" src="pub/bpm/js/util.js"></script>
    <script type="text/javascript">
    var tbar="";
	var ab=pageStatus="${param.pageStatus}";
    var isChecked="${param.isChecked}";
    var serviceId = "${requestScope.serviceId}";//提交数据的id
	var activityId = "${requestScope.activityId}";//流程id
	var processId = "${requestScope.processId}";//退回id

	var status1="${requestScope.status}";
	var areaFlag;//改变状态
 	<bpm:ifPer function="TJY2_ZL_SGCJJYBG_BMFZR" activityId = "${requestScope.activityId}">areaFlag="1";</bpm:ifPer>//部门负责人
 	<bpm:ifPer function="TJY2_ZL_SGCJJYBG_JYRJFZR" activityId = "${requestScope.activityId}">areaFlag="2";</bpm:ifPer>//检验软件负责人
 	<bpm:ifPer function="TJY2_ZL_SGCJJYBG_YWFWBJBR" activityId = "${requestScope.activityId}">areaFlag="3";</bpm:ifPer>//业务服务部经办人
 	<bpm:ifPer function="TJY2_ZL_SGCJJYBG_ZLFZR" activityId = "${requestScope.activityId}">areaFlag="4";</bpm:ifPer>//质量监督管理部负责人（这条是后加的(+﹏+)~狂晕）

 	var njts;
	var njts2;
	var njts3;
	var njts4;
	var njts5;
	var njts6;
	var njts7;
	var equipmentName;
	var equipmentName2;
	var equipmentName3;
	var equipmentName4;
	var equipmentName5;
	var equipmentName6;
	var equipmentName7;
 	
 	$(function () {
    	if(isChecked!="" && typeof(isChecked)!="undefined"){
    		if(areaFlag=="3"){
	        	$("#fwbjhbg").transform("modify",function(){});
	        	$("#fwbjhbg2").transform("modify",function(){});
	        	$("#fwbjhbg3").transform("modify",function(){});
	        	$("#fwbjhbg4").transform("modify",function(){});
	        	$("#fwbjhbg5").transform("modify",function(){});
	        	$("#fwbjhbg6").transform("modify",function(){});
	        	$("#fwbjhbg7").transform("modify",function(){});
        	}
        	$("#sg").transform("detail",function(){});
   	    	$("#sg").setValues("quality/sgcjjybg/detail.do?id=${requestScope.serviceId}",function(res){
   	    		njts=res.data.njts;
   	    		njts2=res.data.njts2;
   	    		njts3=res.data.njts3;
   	    		njts4=res.data.njts4;
   	    		njts5=res.data.njts5;
   	    		njts6=res.data.njts6;
   	    		njts7=res.data.njts7;
   	    		equipmentName=res.data.equipmentName;
   	    		equipmentName2=res.data.equipmentName2;
   	    		equipmentName3=res.data.equipmentName3;
   	    		equipmentName4=res.data.equipmentName4;
   	    		equipmentName5=res.data.equipmentName5;
   	    		equipmentName6=res.data.equipmentName6;
   	    		equipmentName7=res.data.equipmentName7;
   	    		
   	    		var a=res.data.departmentManDate;
   	    		if(a != null){
	            	var departmentManDate=a.substring(a.indexOf("/")+1,a.lastIndexOf(" "));
	            	$("#departmentManDate").html(departmentManDate);
	            	$("#departmentManDate").val(departmentManDate);
   	    		}
            	var d=res.data.zlshtime;
            	if(d != null){
	            	var zlshtime=d.substring(d.indexOf("/")+1,d.lastIndexOf(" "));
	            	$("#zlshtime").html(zlshtime);
	            	$("#zlshtime").val(zlshtime);
            	}
            	var b=res.data.jyrjfzrDate;
            	if(b != null){
	            	var jyrjfzrDate=b.substring(b.indexOf("/")+1,b.lastIndexOf(" "));
	            	$("#jyrjfzrDate").html(jyrjfzrDate);
	            	$("#jyrjfzrDate").val(jyrjfzrDate);
            	}
            	var c=res.data.ywfwbjbrDate;
            	if(c != null){
	            	var ywfwbjbrDate=c.substring(c.indexOf("/")+1,c.lastIndexOf(" "));
	            	$("#ywfwbjbrDate").html(ywfwbjbrDate);
	            	$("#ywfwbjbrDate").val(ywfwbjbrDate);
            	}
   	    	});
   	    	$("#sg1").setValues("quality/sgcjjybg/detail.do?id=${requestScope.serviceId}");
   	    	
   	    	tbar=[{ text: '通过', id: 'submit1', icon: 'accept', click: submitSh},
   	    		{ text: '不通过', id: 'submit2', icon: 'forbid', click: nosubmitSh},
                   { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
        } else {
            tbar=[{ text: '保存', id: 'up', icon: 'save', click: directChange},
                { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
        }
    	if(areaFlag=="4"){
    		tbar=[{ text: '审核', id: 'up', icon: 'save', click: zlbsh},
                  { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
	    	}
    	 if ("${param.pageStatus}"=="detail")
    	        tbar=[{ text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
    	$("#form1").initForm({
            showToolbar: true,
            toolbarPosition: "bottom",
            toolbar: tbar,
            getSuccess : function(res) { //
            	var a=res.data.departmentManDate;
   	    		if(a != null){
	            	var departmentManDate=a.substring(a.indexOf("/")+1,a.lastIndexOf(" "));
	            	$("#departmentManDate").html(departmentManDate);
	            	$("#departmentManDate").val(departmentManDate);
   	    		}
            	var d=res.data.zlshtime;
            	if(d != null){
	            	var zlshtime=d.substring(d.indexOf("/")+1,d.lastIndexOf(" "));
	            	$("#zlshtime").html(zlshtime);
	            	$("#zlshtime").val(zlshtime);
            	}
            	var b=res.data.jyrjfzrDate;
            	if(b != null){
	            	var jyrjfzrDate=b.substring(b.indexOf("/")+1,b.lastIndexOf(" "));
	            	$("#jyrjfzrDate").html(jyrjfzrDate);
	            	$("#jyrjfzrDate").val(jyrjfzrDate);
            	}
            	var c=res.data.ywfwbjbrDate;
            	if(c != null){
	            	var ywfwbjbrDate=c.substring(c.indexOf("/")+1,c.lastIndexOf(" "));
	            	$("#ywfwbjbrDate").html(ywfwbjbrDate);
	            	$("#ywfwbjbrDate").val(ywfwbjbrDate);
            	}
            }
    	});
    });
 	function zlbsh(){
 		top.$.dialog({
			width: 600,
			height: 350,
			lock: true,
			parent: api,
			data: {
				window: window
			},
			title: "审核",
			content: 'url:app/qualitymanage/sgcjjybg_zlbyj.jsp?pageStatus=add&serviceId='+serviceId+
					'&activityId='+activityId+"&areaFlag="+areaFlag+'&processId='+processId+'&st='+"W"
		});
 	}
    function submitSh(){
    	var serviceId = "${requestScope.serviceId}";//提交数据的id
    	var activityId = "${requestScope.activityId}";//流程id
    	/*alert(serviceId+"=="+activityId);*/
    	var bgbh =$("#bgbh").val();
    	var bgbh2 =$("#bgbh2").val();
    	var bgbh3 =$("#bgbh3").val();
    	var bgbh4 =$("#bgbh4").val();
    	var bgbh5 =$("#bgbh5").val();
    	var bgbh6 =$("#bgbh6").val();
    	var bgbh7 =$("#bgbh7").val();
    	var zlshyj =$("#zlshyj").val();
        $.ligerDialog.confirm('是否要通过？', function (yes){
        if(!yes){return false;}
        if(areaFlag=="3"){
        	if($("#equipmentName").html()!=null&&$("#equipmentName").html()!=''){
        		if(bgbh==null||bgbh==''){
        			$.ligerDialog.warn("请选择报告/证书编号！");
        			return false;
        		}
        	}else if($("#equipmentName2").html()!=null&&$("#equipmentName2").html()!=''){
        		if(bgbh2==null||bgbh2==''){
        			$.ligerDialog.warn("请选择报告/证书编号！");
        			return false;
        		}
        	}else if($("#equipmentName3").html()!=null&&$("#equipmentName3").html()!=''){
        		if(bgbh3==null||bgbh3==''){
        			$.ligerDialog.warn("请选择报告/证书编号！");
        			return false;
        		}
        	}else if($("#equipmentName4").html()!=null&&$("#equipmentName4").html()!=''){
        		if(bgbh4==null||bgbh4==''){
        			$.ligerDialog.warn("请选择报告/证书编号！");
        			return false;
        		}
        	}else if($("#equipmentName5").html()!=null&&$("#equipmentName5").html()!=''){
        		if(bgbh5==null||bgbh5==''){
        			$.ligerDialog.warn("请选择报告/证书编号！");
        			return false;
        		}
        	}else if($("#equipmentName6").html()!=null&&$("#equipmentName6").html()!=''){
        		if(bgbh6==null||bgbh6==''){
        			$.ligerDialog.warn("请选择报告/证书编号！");
        			return false;
        		}
        	}else if($("#equipmentName7").html()!=null&&$("#equipmentName7").html()!=''){
        		if(bgbh7==null||bgbh7==''){
        			$.ligerDialog.warn("请选择报告/证书编号！");
        			return false;
        		}
        	}
        }
         $("body").mask("提交中...");
         getServiceFlowConfig("TJY2_ZL_SGCJJYBG_WT","",function(result,data){
                if(result){
                     top.$.ajax({
                         url: "quality/sgcjjybg/sgcjjybgsh.do?id="+serviceId+
                        		 "&typeCode=TJY2_ZL_SGCJJYBG_WT&status="+"&activityId="+activityId+"&areaFlag="+areaFlag+
                        		 "&bgbh="+bgbh+"&bgbh2="+bgbh2+"&bgbh3="+bgbh3+"&bgbh4="+bgbh4+"&bgbh5="+bgbh5+"&bgbh6="+bgbh6+"&bgbh7="+bgbh7,
                         type: "GET",
                         dataType:'json',
                         data:"&zlshyj="+zlshyj,
                         async: false,
                         success:function (data) {
                             if (data) {
                                $("body").unmask();
                                top.$.notice("审核成功！！！",4);
//                                 api.data.window.Qm.refreshGrid();//刷新
//                                 api.close();
                                api.data.window.Qm.refreshGrid();
			                     api.close();
                             }
                         }
                     });
                }else{
                 $.ligerDialog.error("出错了！请重试！");
                 $("body").unmask();
                }
             });
        });
    }
    function nosubmitSh(){
    	var zlshyj =$("#zlshyj").val();
        $.ligerDialog.confirm('是否要不通过？', function (yes){
        if(!yes){return false;}
         $("body").mask("提交中...");
         getServiceFlowConfig("TJY2_ZL_SGCJJYBG_WT","",function(result,data){
                if(result){
                     top.$.ajax({
                         url: "quality/sgcjjybg/sgcjjybgth.do?id="+serviceId+
                        		 "&typeCode=TJY2_ZL_SGCJJYBG_WT&status="+"&activityId="+activityId+"&areaFlag="+areaFlag+"&processId="+processId,
                         type: "GET",
                         dataType:'json',
                         data:"&zlshyj="+zlshyj,
                         async: false,
                         success:function (data) {
                             if (data) {
                                $("body").unmask();
                                top.$.notice("审核成功！！！");
                                api.data.window.Qm.refreshGrid();
			                     api.close();
                             }
                         }
                     });
                }else{
                 $.ligerDialog.error("出错了！请重试！");
                 $("body").unmask();
                }
             });
        });
    	
    }
    function directChange(saveone){ 
       	var obj=$("#form1").validate().form();
    	 if(obj){
    		 var njts=$("#njts").val();
    		 var njts2=$("#njts2").val();
    		 var njts3=$("#njts3").val();
    		 var njts4=$("#njts4").val();
    		 var njts5=$("#njts5").val();
    		 var njts6=$("#njts6").val();
    		 var njts7=$("#njts7").val();
//     		 if(njts==""||njts2==""||njts3==""||njts4==""||njts5==""||njts6==""||njts7==""){
//     			 $.ligerDialog.error('提示：请填写拟检台数，如有疑问请联系业务发展部。');
//     			 return;
//     		 }
    		 
    		//判断检验任务单是否必填------开始
    		 var tj=true;
//     		 var type=$("#equipmentType").val();
    		 var rwdSn1=$("#rwdSn1").val();
//     		 var type2=$("#equipmentType2").val();
    		 var rwdSn2=$("#rwdSn2").val();
//     		 var type3=$("#equipmentType3").val();
    		 var rwdSn3=$("#rwdSn3").val();
//     		 var type4=$("#equipmentType4").val();
    		 var rwdSn4=$("#rwdSn4").val();
//     		 var type5=$("#equipmentType5").val();
    		 var rwdSn5=$("#rwdSn5").val();
//     		 var type6=$("#equipmentType6").val();
    		 var rwdSn6=$("#rwdSn6").val();
//     		 var type7=$("#equipmentType7").val();
    		 var rwdSn7=$("#rwdSn7").val();
//     		 var zbzl=["BJ","YLRQ","YLGD","AQFJJAQBHZZ","YLGDYJ","GL","CL"];//承压必填
//     		 if(rwdSn1=="" && $.inArray(type, zbzl)>-1){
			
				if(rwdSn1==""  &&njts!=""){
	    			 $.ligerDialog.error('提示：请选择检验任务单，如有疑问请联系业务发展部。');
	    			 return;
	    		 }
    		 if(rwdSn2=="" &&njts2!=""){
    			 $.ligerDialog.error('提示：请选择检验任务单，如有疑问请联系业务发展部。');
    			 return;
    		 }
    		 if(rwdSn3=="" &&njts3!=""){
    			 $.ligerDialog.error('提示：请选择检验任务单，如有疑问请联系业务发展部。');
    			 return;
    		 }
    		 if(rwdSn4==""&&njts4!=""){
    			 $.ligerDialog.error('提示：请选择检验任务单，如有疑问请联系业务发展部。');
    			 return;
    		 }
    		 if(rwdSn5=="" &&njts5!=""){
    			 $.ligerDialog.error('提示：请选择检验任务单，如有疑问请联系业务发展部。');
    			 return;
    		 }
    		 if(rwdSn6=="" &&njts6!=""){
    			 $.ligerDialog.error('提示：请选择检验任务单，如有疑问请联系业务发展部。');
    			 return;
    		 }
    		 if(rwdSn7=="" &&njts7!=""){
    			 $.ligerDialog.error('提示：请选择检验任务单，如有疑问请联系业务发展部。');
    			 return;
    		 }
    		 //判断检验任务单是否必填------结束
    		 
    		 
    		 var formData = $("#form1").getValues();
             $("body").mask("正在保存......");
            $.ajax({
                url: "quality/sgcjjybg/save.do?saveone="+saveone,
                type: "POST",
                datatype: "json",
                contentType: "application/json; charset=utf-8",
                data: $.ligerui.toJSON(formData),
                success: function (data, stats) {
                    $("body").unmask();
                    if (data["success"]) {
                    	top.$.notice(data.msg,3);	
                        //top.$.dialog.notice({content:'保存成功！'});
                        if("${param.pageStatus}"=="modify"){
                        	 api.data.window.Qm.refreshGrid();
		                     api.close();
                        }else{
                    	 	 api.data.window.api.data.window.Qm.refreshGrid();
		                     api.data.window.api.close();
		                     api.close();
                        }
                    }else{
                        $.ligerDialog.error('提示：' + data.msg);
                        if("${param.pageStatus}"=="modify"){
                       	 api.data.window.Qm.refreshGrid();
                       }else{
                   	 	 api.data.window.api.data.window.Qm.refreshGrid();
                       }
                    }
                },
                error: function (data,stats) {
                    $("body").unmask();
                    $.ligerDialog.error('提示：' + data.msg);
                }
            });
    	 }else{
    		 return;
    	}
    	 }
        function choosePerson(){
        	//var api=api.data;
        	top.$.dialog({
                width: 800,
                height: 450,
                lock: true,
                parent: api.data,
                title: "选择人员",
                content: 'url:app/common/person_choose.jsp',
                cancel: true,
                ok: function(){
                    var p = this.iframe.contentWindow.getSelectedPerson();
                    if(!p){
                        top.$.notice("请选择人员！", 3, "k/kui/images/icons/dialog/32X32/hits.png");
                        return false;
                    }
                    $("#applyNanId").val(p.id);
                    $("#applyName").val(p.name);
                    $("#department").val(p.org_name);
                    $("#departmentId").val(p.org_id);
                }
            });
        }
      //设置报告编号
   	 function setFwbjhbg(){
          if(areaFlag=="1" || areaFlag=="2" || areaFlag=="3"){
	        	top.$.dialog({
					parent: api,
					width : 660, 
					height : 200, 
					lock : false, 
					title:"选择报告/证书编号",
					content: 'url:app/qualitymanage/sgcjjybg_xuanze_w.jsp?abc='+"1"+"&fid="+serviceId,
					data : {"parentWindow" : window}
				});
          }else{
       	   $.ligerDialog.error("亲，你不可以选择哦！！！");
          }
        }
   	 function callBackReport(id, test_coding){
		 var id = "${requestScope.serviceId}";
		 top.$.ajax({
	            url: "quality/sgcjjybg/setbgbh.do?id="+id+"&test_coding="+test_coding+"&njts="+njts+"&row="+"1",
	            type: "POST",
	            dataType:'json',
	            async: false,
	            success:function (data) {
	            	if (data.success) {
                   $("#fwbjhbg").html(data.bgbh);
                   $("#bgbh").val(data.bgbh);
                 }else{
                	 
                 }
	            }
	        });
		}	
	 function setFwbjhbg2(){
         if(areaFlag=="1" || areaFlag=="2" || areaFlag=="3"){
  	        	top.$.dialog({
  					parent: api,
  					width : 660, 
  					height : 500, 
  					lock : false, 
  					title:"选择报告/证书编号",
  					content: 'url:app/qualitymanage/sgcjjybg_xuanze_w.jsp?abc='+"2"+"&fid="+serviceId,
  					data : {"parentWindow" : window}
  				});
         }else{
      	   $.ligerDialog.error("亲，你不可以选择哦！！！");
         }
       }
  	 function callBackReport2(id, test_coding){
  		 var id = "${requestScope.serviceId}";
		 top.$.ajax({
	            url: "quality/sgcjjybg/setbgbh.do?id="+id+"&test_coding="+test_coding+"&njts="+njts2+"&row="+"2",
	            type: "POST",
	            dataType:'json',
	            async: false,
	            success:function (data) {
	            	if (data.success) {
                   $("#fwbjhbg2").html(data.bgbh);
                   $("#bgbh2").val(data.bgbh);
                 }else{
                	 
                 }
	            }
	        });
 	}	
  	function setFwbjhbg3(){
        if(areaFlag=="1" || areaFlag=="2" || areaFlag=="3"){
	        	top.$.dialog({
					parent: api,
					width : 660, 
					height : 500, 
					lock : false, 
					title:"选择报告/证书编号",
					content: 'url:app/qualitymanage/sgcjjybg_xuanze_w.jsp?abc='+"3"+"&fid="+serviceId,
					data : {"parentWindow" : window}
				});
        }else{
     	   $.ligerDialog.error("亲，你不可以选择哦！！！");
        }
      }
 	 function callBackReport3(id, test_coding){
  		 var id = "${requestScope.serviceId}";
		 top.$.ajax({
	            url: "quality/sgcjjybg/setbgbh.do?id="+id+"&test_coding="+test_coding+"&njts="+njts3+"&row="+"3",
	            type: "POST",
	            dataType:'json',
	            async: false,
	            success:function (data) {
	            	if (data.success) {
                   $("#fwbjhbg3").html(data.bgbh);
                   $("#bgbh3").val(data.bgbh);
                 }else{
                	 
                 }
	            }
	        });
 	}	
 	function setFwbjhbg4(){
        if(areaFlag=="1" || areaFlag=="2" || areaFlag=="3"){
	        	top.$.dialog({
					parent: api,
					width : 660, 
					height : 500, 
					lock : false, 
					title:"选择报告/证书编号",
					content: 'url:app/qualitymanage/sgcjjybg_xuanze_w.jsp?abc='+"4"+"&fid="+serviceId,
					data : {"parentWindow" : window}
				});
        }else{
     	   $.ligerDialog.error("亲，你不可以选择哦！！！");
        }
      }
 	 function callBackReport4(id, test_coding){
  		 var id = "${requestScope.serviceId}";
		 top.$.ajax({
	            url: "quality/sgcjjybg/setbgbh.do?id="+id+"&test_coding="+test_coding+"&njts="+njts4+"&row="+"4",
	            type: "POST",
	            dataType:'json',
	            async: false,
	            success:function (data) {
	            	if (data.success) {
                   $("#fwbjhbg4").html(data.bgbh);
                   $("#bgbh4").val(data.bgbh);
                 }else{
                	 
                 }
	            }
	        });
 	}	
 	function setFwbjhbg5(){
        if(areaFlag=="1" || areaFlag=="2" || areaFlag=="3"){
	        	top.$.dialog({
					parent: api,
					width : 660, 
					height : 500, 
					lock : false, 
					title:"选择报告/证书编号",
					content: 'url:app/qualitymanage/sgcjjybg_xuanze_w.jsp?abc='+"5"+"&fid="+serviceId,
					data : {"parentWindow" : window}
				});
        }else{
     	   $.ligerDialog.error("亲，你不可以选择哦！！！");
        }
      }
 	 function callBackReport5(id, test_coding){
  		 var id = "${requestScope.serviceId}";
		 top.$.ajax({
	            url: "quality/sgcjjybg/setbgbh.do?id="+id+"&test_coding="+test_coding+"&njts="+njts5+"&row="+"5",
	            type: "POST",
	            dataType:'json',
	            async: false,
	            success:function (data) {
	            	if (data.success) {
                   $("#fwbjhbg5").html(data.bgbh);
                   $("#bgbh5").val(data.bgbh);
                 }else{
                	 
                 }
	            }
	        });
 	}	
 	function setFwbjhbg6(){
        if(areaFlag=="1" || areaFlag=="2" || areaFlag=="3"){
	        	top.$.dialog({
					parent: api,
					width : 660, 
					height : 500, 
					lock : false, 
					title:"选择报告/证书编号",
					content: 'url:app/qualitymanage/sgcjjybg_xuanze_w.jsp?abc='+"6"+"&fid="+serviceId,
					data : {"parentWindow" : window}
				});
        }else{
     	   $.ligerDialog.error("亲，你不可以选择哦！！！");
        }
      }
 	 function callBackReport6(id, test_coding){
  		 var id = "${requestScope.serviceId}";
		 top.$.ajax({
	            url: "quality/sgcjjybg/setbgbh.do?id="+id+"&test_coding="+test_coding+"&njts="+njts6+"&row="+"6",
	            type: "POST",
	            dataType:'json',
	            async: false,
	            success:function (data) {
	            	if (data.success) {
                   $("#fwbjhbg6").html(data.bgbh);
                   $("#bgbh6").val(data.bgbh);
                 }else{
                	 
                 }
	            }
	        });
 	}	
 	function setFwbjhbg7(){
        if(areaFlag=="1" || areaFlag=="2" || areaFlag=="3"){
	        	top.$.dialog({
					parent: api,
					width : 660, 
					height : 500, 
					lock : false, 
					title:"选择报告/证书编号",
					content: 'url:app/qualitymanage/sgcjjybg_xuanze_w.jsp?abc='+"7"+"&fid="+serviceId,
					data : {"parentWindow" : window}
				});
        }else{
     	   $.ligerDialog.error("亲，你不可以选择哦！！！");
        }
      }
 	 function callBackReport7(id, test_coding){
  		 var id = "${requestScope.serviceId}";
		 top.$.ajax({
	            url: "quality/sgcjjybg/setbgbh.do?id="+id+"&test_coding="+test_coding+"&njts="+njts7+"&row="+"7",
	            type: "POST",
	            dataType:'json',
	            async: false,
	            success:function (data) {
	            	if (data.success) {
                   $("#fwbjhbg7").html(data.bgbh);
                   $("#bgbh7").val(data.bgbh);
                 }else{
                	 
                 }
	            }
	      });
 	}	
 	 var a;
  	function valChk(row){
  		a=row;
			top.$.dialog({
				parent: api,
				width : 800, 
				height : 550, 
				lock : true, 
				title:"选择检验任务单",
				content:'url:app/inspection/serviceDepartment_jyrwd.jsp',
				data : {"parentWindow" : window}
			});
		}
  	function jyrwdBack(id,sn){
  		$("#rwdId"+a).val(id);
  		$("#rwdSn"+a).val(sn);
  	}
    </script>
</head>
<style>
.l-t-td-right1 {
    padding: 0;
    margin: 0;
}

.l-t-td-right1 .l-text {
    background-image: none;
}

.l-t-td-right1 .l-text-wrapper {
    width: 100%;
    height: 90px;
}

.l-textarea .l-text-wrapper {
    width: 100%;
    height: 100%;
}

.l-textarea-onerow {
    height: 30px;
}

.l-textarea-onerow div {
    padding: 0;
}

.l-t-td-right1 .l-text {
    border: none;
}

.l-t-td-right1 .l-text.change, .l-t-td-right1 .l-radio-group-wrapper.change
    {
    background: url("../images/x-input.png") repeat-x;
}

.l-t-td-right1 .l-text input {
    height: 90px;
    padding-top: 0;
    line-height: 24px;
}

.l-t-td-right1 .l-radio-group-wrapper {
    height: 90px;
    padding-left: 5px;
}

.l-t-td-right1 textarea {
    height: 100%;
}

.l-textarea-onerow textarea {
    height: 12px;
    padding: 6px 5px;
    width: 98%
}

.l-t-td-right1 label {
    height: 90px;
    line-height: 24px;
    display: inline-block;
}

.l-t-td-right1 div.input, .l-td-right div.input {
    border: none;
    padding-left: 5px;
}

.l-t-td-right1 .input-warp div {
    height: 100%;
    line-height: 28px;
}
</style>
<body >
<form id="form1" action="quality/sgcjjybg/save.do" getAction="quality/sgcjjybg/detail.do?id=${param.id}">
<!-- 设置报告编号 -->
	<input type="hidden" id="bgbh" name="bgbh"/>
	<input type="hidden" id="bgbh2" name="bgbh2"/>
	<input type="hidden" id="bgbh3" name="bgbh3"/>
	<input type="hidden" id="bgbh4" name="bgbh4"/>
	<input type="hidden" id="bgbh5" name="bgbh5"/>
	<input type="hidden" id="bgbh6" name="bgbh6"/>
	<input type="hidden" id="bgbh7" name="bgbh7"/>

    <input type="hidden" id="id" name="id"/>
    <input type="hidden" id="status" name="status"/>
    <input type="hidden" id="statusa" name="statusa" value="1"/>
    <input type="hidden" id="registrant" name="registrant"/>
    <input type="hidden" id="registrantId" name="registrantId"/>
    <input type="hidden" id="registrantDate" name="registrantDate"/>
	<input type="hidden" id="departmentId" name="departmentId"  value="<%=user.getDepartment().getId() %>"/>
    <input type="hidden" id="applyManId" name="applyManId" value="<%=userId%>"/>
    <h1 id="sg2" align="center" style="padding:5mm 0 2mm 0;font-family:微软雅黑;font-size:6mm;">手工出具检验报告/证书审批表 </h1></br>
    <table id="sg1" class="check">
		<tr>
            <td width="650px">&nbsp;</td>
            <td width="50px" align="center">编号：</td>
            <td style="width: 200px;" class="l-t-td-right"><input ltype='text' readOnly="true" name="identifier" type="text"/></td>
			<td width="10px">&nbsp;</td>
        	<td width="10px" align="center"></td>
        	<td width="20" class="l-t-td-right"></td>
		</tr>
	</table>
    <table id="sg" border="1" cellpadding="3" class="l-detail-table" width="720px">
    	<tr>
    	 	<th style="border:0px;width:15px"></th>
	        <th style="border:0px;width:45px"></th>
	        <th style="border:0px;width:45px"></th>
	        <th style="border:0px;width:30px"></th>
	        <th style="border:0px;width:45px"></th>
<!-- 	        <th style="border:0px;width:60px"></th> -->
            <th style="border:0px;width:45px"></th>
            <th style="border:0px;width:15px"></th>
<!--             <th style="border:0px;width:10px"></th> -->
            <th style="border:0px;width:105px"></th>
            <th style="border:0px;width:45px"></th>
        </tr>
        <tr>
            <td class="l-t-td-left" colspan="2">用户名称</td>
         	<td class="l-t-td-right" colspan="7"><input validate="{required:true}" ltype="text"  name="userName" id="userName" type="text"/></td>
         	
        </tr>
         <tr>
            <td class="l-t-td-left" colspan="2">报检/委托时间</td>
            <%SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
				String nowTime=""; 
				nowTime = dateFormat.format(new Date());%>
        	<td colspan="7" class="l-t-td-right"><input name="bjwtsj" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" value="<%=nowTime%>" /></td>
        </tr>
        <tr>
        	<td align="center">序号</td>
			<td align="center" colspan="2">设备名称</td>
			<td align="center">拟检台数</td>
<!-- 			<td align="center" colspan="2">合同/协议编号</td> -->
			<td align="center" colspan="2">检验任务单</td>
			<td align="center" colspan="2">服务部计划的报告/证书编号</td>
			<td align="center">备注</td>
        </tr>
        <tr>
        	<td class="l-t-td-right"><input name="num" id="num" type="text" ltype="text" validate="{maxlength:2}"/></td>
        	<td class="l-t-td-right" colspan="2"><input name="equipmentName" id="equipmentName" type="text" ltype="text"/></td>
        	<td class="l-t-td-right"><input name="njts" id="njts" type="text" ltype="text"/></td>
<%--         	<td class="l-t-td-right"> <u:combo name="equipmentType" code="TJY2_SBZL" validate="required:false"  attribute="isMultiSelect:false" /> </td> --%>
<!--         	<td class="l-t-td-right" colspan="3"><input name="contractNumber" id="contractNumber" type="text" ltype="text"/></td> -->
        	<td class="l-t-td-right" colspan="2">
        	<input name="rwdId1" id="rwdId1" type="hidden" />
        	<input name="rwdSn1" id="rwdSn1" type="text" readonly="readonly" onclick="valChk(1)" ltype="text"/></td>
        	<td class="l-t-td-right" id="fwbjhbg" colspan="2"><input readonly="readonly" name="fwbjhbg" id="fwbjhbg" 
        		type="text" ltype="text" onclick="setFwbjhbg();" ligerui="{iconItems:[{icon:'add',click:setFwbjhbg}]}"/></td>
        	<td class="l-t-td-right"><input name="remarks" id="remarks" type="text" ltype="text"/></td>
        </tr>
        <tr>
        	<td class="l-t-td-right"><input name="num2" id="num2" type="text" ltype="text" validate="{maxlength:2}"/></td>
        	<td class="l-t-td-right" colspan="2"><input name="equipmentName2" id="equipmentName2" type="text" ltype="text"/></td>
        	<td class="l-t-td-right"><input name="njts2" id="njts2" type="text" ltype="text"/></td>
<%--         	<td class="l-t-td-right"> <u:combo name="equipmentType2" code="TJY2_SBZL" validate="required:false"  attribute="isMultiSelect:false" /> </td> --%>
<!--         	<td class="l-t-td-right" colspan="3"><input name="contractNumber2" id="contractNumber2" type="text" ltype="text"/></td> -->
        	<td class="l-t-td-right" colspan="2">
        	<input name="rwdId2" id="rwdId2" type="hidden" />
        	<input name="rwdSn2" id="rwdSn2" readonly="readonly" onclick="valChk(2)"  type="text" ltype="text"/></td>
        	<td class="l-t-td-right" id="fwbjhbg2" colspan="2"><input readonly="readonly" name="fwbjhbg2" id="fwbjhbg2" 
        		type="text" ltype="text" onclick="setFwbjhbg2();" ligerui="{iconItems:[{icon:'add',click:setFwbjhbg2}]}"/></td>
        	<td class="l-t-td-right"><input name="remarks2" id="remarks2" type="text" ltype="text"/></td>
        </tr>
        <tr>
        	<td class="l-t-td-right"><input name="num3" id="num3" type="text" ltype="text" validate="{maxlength:2}"/></td>
        	<td class="l-t-td-right" colspan="2"><input name="equipmentName3" id="equipmentName3" type="text" ltype="text"/></td>
        	<td class="l-t-td-right"><input name="njts3" id="njts3" type="text" ltype="text"/></td>
<%--         	<td class="l-t-td-right"> <u:combo name="equipmentType3" code="TJY2_SBZL" validate="required:false"  attribute="isMultiSelect:false" /> </td> --%>
<!--         	<td class="l-t-td-right" colspan="3"><input name="contractNumber3" id="contractNumber3" type="text" ltype="text"/></td> -->
        	<td class="l-t-td-right" colspan="2">
        	<input name="rwdId3" id="rwdId3" type="hidden" />
        	<input name="rwdSn3" id="rwdSn3" readonly="readonly" onclick="valChk(3)"  type="text" ltype="text"/></td>
        	<td class="l-t-td-right" id="fwbjhbg3" colspan="2"><input readonly="readonly" name="fwbjhbg3" id="fwbjhbg3" 
        		type="text" ltype="text" onclick="setFwbjhbg3();" ligerui="{iconItems:[{icon:'add',click:setFwbjhbg3}]}"/></td>
        	<td class="l-t-td-right"><input name="remarks3" id="remarks3" type="text" ltype="text"/></td>
        </tr>
        <tr>
        	<td class="l-t-td-right"><input name="num4" id="num4" type="text" ltype="text" validate="{maxlength:2}"/></td>
        	<td class="l-t-td-right" colspan="2"><input name="equipmentName4" id="equipmentName4" type="text" ltype="text"/></td>
        	<td class="l-t-td-right"><input name="njts4" id="njts4" type="text" ltype="text"/></td>
<%--         	<td class="l-t-td-right"> <u:combo name="equipmentType4" code="TJY2_SBZL" validate="required:false"  attribute="isMultiSelect:false" /> </td> --%>
        	<td class="l-t-td-right" colspan="2">
        	<input name="rwdId4" id="rwdId4" type="hidden" />
        	<input name="rwdSn4" id="rwdSn4" readonly="readonly" onclick="valChk(4)"  type="text" ltype="text"/></td>
        	
<!--         	<td class="l-t-td-right" colspan="3"><input name="contractNumber4" id="contractNumber4" type="text" ltype="text"/></td> -->
        	<td class="l-t-td-right" id="fwbjhbg4" colspan="2"><input readonly="readonly" name="fwbjhbg4" id="fwbjhbg4" 
        		type="text" ltype="text" onclick="setFwbjhbg4();" ligerui="{iconItems:[{icon:'add',click:setFwbjhbg4}]}"/></td>
        	<td class="l-t-td-right"><input name="remarks4" id="remarks4" type="text" ltype="text"/></td>
        	
        </tr>
        <tr>
        	<td class="l-t-td-right"><input name="num5" id="num5" type="text" ltype="text" validate="{maxlength:2}"/></td>
        	<td class="l-t-td-right" colspan="2"><input name="equipmentName5" id="equipmentName5" type="text" ltype="text"/></td>
        	<td class="l-t-td-right"><input name="njts5" id="njts5" type="text" ltype="text"/></td>
<%--         	<td class="l-t-td-right"> <u:combo name="equipmentType5" code="TJY2_SBZL" validate="required:false"  attribute="isMultiSelect:false" /> </td> --%>
        	<td class="l-t-td-right" colspan="2">
        	<input name="rwdId5" id="rwdId5" type="hidden" />
        	<input name="rwdSn5" id="rwdSn5" readonly="readonly" onclick="valChk(5)"  type="text" ltype="text"/></td>
<!--         	<td class="l-t-td-right" colspan="3"><input name="contractNumber5" id="contractNumber5" type="text" ltype="text"/></td> -->
        	<td class="l-t-td-right" id="fwbjhbg5" colspan="2"><input readonly="readonly" name="fwbjhbg5" id="fwbjhbg5" 
        		type="text" ltype="text" onclick="setFwbjhbg5();" ligerui="{iconItems:[{icon:'add',click:setFwbjhbg5}]}"/></td>
        	<td class="l-t-td-right"><input name="remarks5" id="remarks5" type="text" ltype="text"/></td>
        	
        </tr>
        <tr>
        	<td class="l-t-td-right"><input name="num6" id="num6" type="text" ltype="text" validate="{maxlength:2}"/></td>
        	<td class="l-t-td-right" colspan="2"><input name="equipmentName6" id="equipmentName6" type="text" ltype="text"/></td>
        	<td class="l-t-td-right"><input name="njts6" id="njts6" type="text" ltype="text"/></td>
<%--         	<td class="l-t-td-right"> <u:combo name="equipmentType6" code="TJY2_SBZL" validate="required:false"  attribute="isMultiSelect:false" /> </td> --%>
        	<td class="l-t-td-right" colspan="2">
        	<input name="rwdId6" id="rwdId6" type="hidden" />
        	<input name="rwdSn6" id="rwdSn6" readonly="readonly"  onclick="valChk(6)" type="text" ltype="text"/></td>
<!--         	<td class="l-t-td-right" colspan="3"><input name="contractNumber6" id="contractNumber6" type="text" ltype="text"/></td> -->
        	<td class="l-t-td-right" id="fwbjhbg6" colspan="2"><input readonly="readonly" name="fwbjhbg6" id="fwbjhbg6" 
        		type="text" ltype="text" onclick="setFwbjhbg6();" ligerui="{iconItems:[{icon:'add',click:setFwbjhbg6}]}"/></td>
        	<td class="l-t-td-right"><input name="remarks6" id="remarks6" type="text" ltype="text"/></td>
        	
        </tr>
        <tr>
        	<td class="l-t-td-right"><input name="num7" id="num7" type="text" ltype="text" validate="{maxlength:2}"/></td>
        	<td class="l-t-td-right" colspan="2"><input name="equipmentName7" id="equipmentName7" type="text" ltype="text"/></td>
        	<td class="l-t-td-right"><input name="njts7" id="njts7" type="text" ltype="text"/></td>
<%--         	<td class="l-t-td-right"> <u:combo name="equipmentType7" code="TJY2_SBZL" validate="required:false"  attribute="isMultiSelect:false" /> </td> --%>
        	<td class="l-t-td-right" colspan="2">
        	<input name="rwdId7" id="rwdId7" type="hidden" />
        	<input name="rwdSn7" id="rwdSn7" readonly="readonly"  onclick="valChk(7)"  type="text" ltype="text"/></td>
<!--         	<td class="l-t-td-right" colspan="3"><input name="contractNumber7" id="contractNumber7" type="text" ltype="text"/></td> -->
        	<td class="l-t-td-right" id="fwbjhbg7" colspan="2"><input readonly="readonly" name="fwbjhbg7" id="fwbjhbg7" 
        		type="text" ltype="text" onclick="setFwbjhbg7();" ligerui="{iconItems:[{icon:'add',click:setFwbjhbg7}]}"/></td>
        	<td class="l-t-td-right"><input name="remarks7" id="remarks7" type="text" ltype="text"/></td>
        	
        </tr>
        <tr>
        	<td class="l-t-td-left" colspan="2">申请部门</td>
        	<td class="l-t-td-right" colspan="7">
        		<input validate="{required:true}" ltype="text" name="department" id="department" type="text" 
        		ligerui="{disabled:true}" value="<%=user.getDepartment().getOrgName() %>"/>
        	</td>
        </tr>
        <tr>
            <td class="l-t-td-left" colspan="2">申请人</td>
            <td class="l-t-td-right" colspan="3"><input name="applyName" id="applyName" type="text" ltype="text"  value="<sec:authentication property="principal.name"/>" ligerui="{disabled:true}" /></td>
            <td class="l-t-td-left" colspan="2">申请日期</td>
        	<td class="l-t-td-right" colspan="2"><input id="applyTime" name="applyTime" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" value="<%=nowTime%>" validate="{required:true}"/></td>
        </tr>
          <tr>
            <td class="l-t-td-left" colspan="2"><br></br>部门负责人<br></br><br/></td>
            <td class="l-t-td-right1" colspan="3">
<!--             <textarea readonly="readonly" name="departmentMan" id="departmentMan" rows="3" ltype="text" ></textarea> -->
            <input readonly="readonly" name="departmentMan" type="text" ltype="text" />
            </td>
            <td class="l-t-td-left" colspan="2">日期</td>
        	<td class="l-t-td-right1" colspan="2"><input readonly="readonly" id="departmentManDate" name="departmentManDate" type="text" ltype='text'/></td>
        </tr>
         <tr>	
            <td class="l-t-td-left" rowspan="2" colspan="2">质量监督管理部意见</td>
            <td class="l-t-td-right1" colspan="7"><textarea readonly="readonly" name="zlshyj" id="zlshyj" rows="7" cols="25" class="l-textarea" ></textarea></td>
        </tr>
         <tr>
           	<td class="l-t-td-left" colspan="1"><br></br>签字<br></br></td>
        	<td class="l-t-td-right" colspan="2"><input readonly="readonly" id="zlshman" name="zlshman" type="text" ltype="text" /></td>
     		<td class="l-t-td-left" colspan="2">日期</td>
    		<td class="l-t-td-right" colspan="2"><input readonly="readonly" id="zlshtime" name="zlshtime" type="text" ltype='text'/></td>
        </tr>
         <tr>
            <td class="l-t-td-left" rowspan="2" colspan="2"><br></br><br/>检验软件责任人确认<br></br><br/></td>
            <td class="l-t-td-left" colspan="1"><br></br>签字<br></br></td>
            <td class="l-t-td-right1" colspan="2"><input readonly="readonly" name="jyrjfzr" type="text" ltype="text" /></td>
         	<td class="l-t-td-left" colspan="2">日期</td>
        	<td class="l-t-td-right1" colspan="2"><input readonly="readonly" id="jyrjfzrDate" name="jyrjfzrDate" type="text" ltype='text'/></td>
<!--         ltype='date' ligerui="{disabled:true,format:'yyyy-MM-dd'}" -->
        </tr> 
         <tr>
            <td class="l-t-td-right" colspan="7">
            	<h6 align="center" style="padding:0 0 0 0;font-family:微软雅黑;font-size:4mm;">注：在信息中心发布的手工报告目录内的项目此项不签。 </h6>
            </td>
        </tr>
         <tr>
            <td class="l-t-td-left" colspan="2"><br></br>业务服务部经办人签字<br></br><br/></td>
            <td class="l-t-td-right1" colspan="3"><input readonly="readonly" name="ywfwbjbr" type="text" ltype="text" /></td>
         	<td class="l-t-td-left" colspan="2">日期</td>
        	<td class="l-t-td-right1" colspan="2"><input readonly="readonly" id="ywfwbjbrDate" name="ywfwbjbrDate" type="text" ltype='text'/></td>
        </tr> 
    </table>
    <div>
   		 注 1、设备种类：锅炉、压力容器、压力管道、电梯、起重机械等；<br />
		  &nbsp;&nbsp;&nbsp;&nbsp;2、设备品种：第一类压力容器、超高压容器、球形储罐、医用氧舱、汽车罐车、长输管道、桥式起重机等；<br />
		  &nbsp;&nbsp;&nbsp;&nbsp;3、检验类别：包括制造、安装、修理、改造、进口、定期等检验。<br />
    </div>
</form>
</body>
</html>
