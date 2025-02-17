<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %> 
<%@page import="java.text.SimpleDateFormat"%>
<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!--获取当前登录人  -->
<%
	CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
	User uu = (User)curUser.getSysUser();
	com.khnt.rbac.impl.bean.Employee e = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
	String userId=e.getId();
	String uId = SecurityUtil.getSecurityUser().getId();
%>
<head pageStatus="${param.pageStatus}">
	<title></title>
    <!-- 每个页面引入，页面编码、引入js，页面标题 -->
    <%@include file="/k/kui-base-form.jsp"%> 
	<link type="text/css" rel="stylesheet" href="app/finance/css/form_detail.css" />
	<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
	<script type="text/javascript" src="pub/bpm/js/util.js"></script>
	<script type="text/javascript" src="app/common/lodop/LodopFuncs.js"></script>
    <script type="text/javascript">
  	//jQuery页面载入事件
	var isChecked = "${param.isChecked}";
	var serviceId = "${requestScope.serviceId}";//提交数据的id
	var activityId = "${requestScope.activityId}";//流程id
	var processId = "${requestScope.processId}";//过程
	var areaFlag ='';//改变状态
	<bpm:ifPer function="TJY2_LEAVE_C_BMFZRQZ" activityId = "${requestScope.activityId}">areaFlag="2";</bpm:ifPer>//部门负责人签字
	<bpm:ifPer function="TJY2_LEAVE_C_RSYJ" activityId = "${requestScope.activityId}">areaFlag="3";</bpm:ifPer>//人事意见
	/* <bpm:ifPer function="TJY2_LEAVE_C_KSFZRYJ" activityId = "${requestScope.activityId}">areaFlag="4";</bpm:ifPer>//科室负责人意见 */
	<bpm:ifPer function="TJY2_LEAVE_C_FGLDYJ" activityId = "${requestScope.activityId}">areaFlag="4";</bpm:ifPer>//分管领导意见
	<bpm:ifPer function="TJY2_LEAVE_C_YZYJ" activityId = "${requestScope.activityId}">areaFlag="5";</bpm:ifPer>//院长意见
    //公务外出
    <bpm:ifPer function="GWWC_BZSH" activityId = "${requestScope.activityId}">areaFlag="2";</bpm:ifPer>//部长审核
	<bpm:ifPer function="GWWC_FHLDSH" activityId = "${requestScope.activityId}">areaFlag="4";</bpm:ifPer>//分管领导审核
	<bpm:ifPer function="GWWC_YZSH" activityId = "${requestScope.activityId}">areaFlag="5";</bpm:ifPer>//院长审核
	//定义变量
    var pageStatus="${param.pageStatus}";
    var modifyType="${param.modifyType}";
    var tbar="";
    var startDate;
    var endDate;
    var uploadId="";
    var leave="";
    $(function(){
    	var nowDate = new Date().format("yyyy-MM-dd");
    	$("#peopleSignDate").val(nowDate);
    	$("#peopleSign").val("<%=uu.getName()%>");
    	if(isChecked!="" && typeof(isChecked)!="undefined"){
    		$("#onefileBtn").hide();
    		queryFiles("${requestScope.serviceId}");
    		//$("#table1").transform("detail");
     		$("#table1").setValues("BgLeaveAction/detail.do?id=${requestScope.serviceId}");
     		$("#table2").setValues("BgLeaveAction/detail.do?id=${requestScope.serviceId}");
     		$("#table3").setValues("BgLeaveAction/detail.do?id=${requestScope.serviceId}");
     		$("#table3").attr("disabled",true);
     		$("#table8").attr("disabled",true);
     		$("#table8").setValues("BgLeaveAction/detail.do?id=${requestScope.serviceId}");
    		if(areaFlag=="2"){
    			//仅部门负责人签字可编辑
    			$("#ksfzryjSing").val("<%=uu.getName()%>");
    			$("#ksfzryjDate").val(nowDate);
     			$("#table5").attr("disabled",true);
     			$("#table6").attr("disabled",true);
     			$("#table7").attr("disabled",true);
     		}else if(areaFlag=="3"){
     			$("#table4").setValues("BgLeaveAction/detail.do?id=${requestScope.serviceId}");
     			//仅人事意见可编辑
     			$("#rsyjSign").val("<%=uu.getName()%>");
    			$("#rsyjDate").val(nowDate);
     			$("#table4").attr("disabled",true);
     			$("#table6").attr("disabled",true);
     			$("#table7").attr("disabled",true);
     		}else if(areaFlag=="4"){
     			$("#table4").setValues("BgLeaveAction/detail.do?id=${requestScope.serviceId}");
     			$("#table5").setValues("BgLeaveAction/detail.do?id=${requestScope.serviceId}");
     			//仅分管领导意见可编辑
     			$("#fgyldyjSign").val("<%=uu.getName()%>");
    			$("#fgyldyjDate").val(nowDate);
     			$("#table4").attr("disabled",true);
     			$("#table5").attr("disabled",true);
     			$("#table7").attr("disabled",true);
     		}else if(areaFlag=="5"){
     			$("#table4").setValues("BgLeaveAction/detail.do?id=${requestScope.serviceId}");
     			$("#table5").setValues("BgLeaveAction/detail.do?id=${requestScope.serviceId}");
     			$("#table6").setValues("BgLeaveAction/detail.do?id=${requestScope.serviceId}");
     			//仅院长意见可编辑
     			$("#yzyjSign").val("<%=uu.getName()%>");
    			$("#yzyjDate").val(nowDate);
     			$("#table4").attr("disabled",true);
     			$("#table5").attr("disabled",true);
     			$("#table6").attr("disabled",true);
     		}else{
     			$("#table4").attr("disabled",true);
     			$("#table5").attr("disabled",true);
     			$("#table6").attr("disabled",true);
     			$("#table7").attr("disabled",true);
     		}
    		tbar=[{ text: '审核不通过', id: 'shbtg', icon: 'del', click: shbtg},
                  { text: '审核通过', id: 'shtg', icon: 'submit', click: shtg},
                  { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
    	}else if(pageStatus=="detail"){
    		$("#form1").transform("detail");
    		queryFiles("${param.id}");
    		tbar=[
				  { text: '打印', id: 'print', icon: 'print', click: print},
    		      { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
    		//----------------------------
    		$("#onefileBtn").hide();
    	}else if(pageStatus=="modify"){
    		$("#form1").setValues("BgLeaveAction/detail.do?id=${param.id}");
    		queryFiles("${param.id}");
    		if(modifyType=="back"){
	   			$("#table1").attr("disabled",false);
	   			$("#table2").attr("disabled",false);
	   			$("#table3").attr("disabled",true);
	   			$("#table8").attr("disabled",false);
    		}else{
    			$("#table8").attr("disabled",true);
    		}
    		$("#table4").attr("disabled",true);
    		$("#table5").attr("disabled",true);
    		$("#table6").attr("disabled",true);
    		$("#table7").attr("disabled",true);
            tbar=[{ text: '保存', id: 'up', icon: 'save', click: save},
                { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
    	}else{
    		<%-- queryLeave("<%=userId %>"); --%>
    		$("#table4").attr("disabled",true);
    		$("#table5").attr("disabled",true);
    		$("#table6").attr("disabled",true);
    		$("#table7").attr("disabled",true);
    		$("#table8").attr("disabled",true);
            tbar=[{ text: '保存', id: 'up', icon: 'save', click: save},
                { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
    	}
        $("#form1").initForm({
            showToolbar: true,
            toolbarPosition: "bottom",
            toolbar: tbar,
            afterParse: function(fm){
		    	leave = $("#leaveType").val();
            }
    	});
        $("body").append('<object style="height:1px;" id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0><embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed></object>');
       	//----------------------------------------------------------
      	//上传图片
   		var oneUploadConfig = {
   				fileSize : "10mb",//文件大小限制
   				businessId : "",//业务ID
   				buttonId : "onefileBtn",//上传按钮ID
   				container : "onefileDIV",//上传控件容器ID
   				attType : "",//文件存储类型；1:数据库，0:磁盘，默认为磁盘
   				title : "图片选择",//文件选择框提示
   				extName : "jpg,jpeg,gif,bmp,png",//文件扩展名限制
   				fileNum : 1,//限制上传文件选择数目 弹出选择框 可以选择文件数量限制
   				workItem : "one",//页面多点上传文件标识符号
   				callback : function(files){//上传成功后回调函数,实现页面文件显示，交与业务自行后续处理
   					/* $("#empPhoto").val(files[0].path); */
   					$("#image").attr("src","upload/"+files[0].path)
   					$("#image").show();
   					uploadId=files[(files.length-1)].id;
   					//将文件ID写入隐藏框中
   					$("#uploadFiles").val(files[(files.length-1)].id)
   					$("#procufiles").attr("onclick","view()");
   				}
   			};
   		var oneUploader= new KHFileUploader(oneUploadConfig); 
    });
    //保存
    function save(){
    	var uploadFiles=$("#uploadFiles").val();
		url = "BgLeaveAction/save.do?pageStatus="+pageStatus+"&uploadFiles="+uploadFiles+"&modifyType="+modifyType; 
		//验证表单数据
		if($('#form1').validate().form()){
			if($('#leaveCount1').val()=="开始时间不能大于结束时间！"){
				$.ligerDialog.error("开始时间或结束时间有误！请重新选择！");
				return false;
			}else{
				var formData = $("#form1").getValues();
		        var data = {};
		        data = formData;
		        $("body").mask("正在保存数据，请稍后！");
		        $.ajax({
		        	url: url,
		            type: "POST",
		            datatype: "json",
		            contentType: "application/json; charset=utf-8",
		           	data:$.ligerui.toJSON(data),
		            success: function (data) {
		            	api.data.window.Qm.refreshGrid();
		                api.close();
		                top.$.dialog.notice("保存成功");
		            },
		            error: function (data) {
		            	$.ligerDialog.error("保存失败");
	                	$("#save").removeAttr("disabled");
		            }
		        });
			}
		}
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
	            $("#peopleId").val(p.id);
	            $("#peopleName").val(p.name);
	            $("#depId").val(p.org_id);
	            $("#depName").val(p.org_name);
	        }
	    });
    }
	//审核通过
	function shtg(){
     	var id=$("#id").val();
     	var formData=$("#form1").getValues();
     	var leaveType=$("#leaveType").val();
     	var bgLeave=$.ligerui.toJSON(formData);
     	if(leaveType=='GWWC'){
     		$.ligerDialog.confirm('是否审核通过？', function (yes){
    	         if(!yes){return false;}
    	    	 $("body").mask("正在处理，请稍后！");
    	    	 getServiceFlowConfig("TJY2_RL_GWWC","",function(result,data){
    	    		 if(result){
    	                 top.$.ajax({
    	                     url: "BgLeaveAction/subPassGwwc.do?id="+serviceId+
    	                    		 "&typeCode=TJY2_RL_GWWC&status="+"&activityId="+activityId+"&areaFlag="+areaFlag+"&processId="+processId,
    	                     type: "POST",
    	                     dataType:'json',
    	                     data:{"bgLeave":bgLeave},
    	                     async: false,
    	                     success:function (data) {
    	                         if (data) {
    	                           	api.data.window.Qm.refreshGrid();
    				                api.close();
    	                         }
    	                     }
    	                 });
    	            }else{
    	              $.ligerDialog.alert("出错了！请重试！");
    	              $("body").unmask();
    	             }
    	         });
         	});
     	}else{
     		$.ligerDialog.confirm('是否审核通过？', function (yes){
    	        if(!yes){return false;}
    	        $("body").mask("提交中...");
    	        getServiceFlowConfig("TJY2_RL_LEAVE_CANCEL","",function(result,data){
    	        	if(result){
    	            	top.$.ajax({
    	                	url: "BgLeaveAction/subPassCancel.do?id="+serviceId+
    	                    	"&typeCode=TJY2_RL_LEAVE_CANCEL&status="+"&activityId="+activityId+"&areaFlag="+areaFlag,
    	                    type: "POST",
    	                    dataType:'json',
    	          	 		data: {"bgLeave":bgLeave},
    	                    async: false,
    	                    success:function (data) {
    	                    	if (data) {
    	                       	api.data.window.Qm.refreshGrid();
    	                    	api.close();
    	                        }
    	                    }
    	                });
    	             }else{
    	                  $.ligerDialog.alert("出错了！请重试！");
    	                  $("body").unmask();
    	                 }
    	         });
             });
     	}
	}
	//审核不通过
	function shbtg(){
		 var id=$("#id").val();
       	 var formData=$("#form1").getValues();
       	 var leaveType=$("#leaveType").val();
       	 var bgLeave=$.ligerui.toJSON(formData);
       	 if(leaveType=='公务外出'){
       		$.ligerDialog.confirm('是否要不通过？', function (yes){
      	         if(!yes){return false;}
      	    	 $("body").mask("正在处理，请稍后！");
      	    	 getServiceFlowConfig("TJY2_RL_GWWC","",function(result,data){
      	    		 if(result){
      	                 top.$.ajax({
      	                     url: "BgLeaveAction/shbtgGwwc.do?id="+serviceId+
      	                    		 "&typeCode=TJY2_RL_GWWC&status="+"&activityId="+activityId+"&areaFlag="+areaFlag+"&processId="+processId,
      	                     type: "POST",
      	                     dataType:'json',
      	                     data:{"bgLeave":bgLeave},
      	                     async: false,
      	                     success:function (data) {
      	                         if (data) {
      	                           	api.data.window.Qm.refreshGrid();
      				                api.close();
      	                         }
      	                     }
      	                 });
      	            }else{
      	              $.ligerDialog.alert("出错了！请重试！");
      	              $("body").unmask();
      	             }
      	         });
           	});
       	 }else{
       		$.ligerDialog.confirm('是否要不通过？', function (yes){
   	         if(!yes){return false;}
   	    	 $("body").mask("正在处理，请稍后！");
   	    	 getServiceFlowConfig("TJY2_RL_LEAVE_CANCEL","",function(result,data){
   	    		 if(result){
   	                 top.$.ajax({
   	                     url: "BgLeaveAction/shbtgCancel.do?id="+serviceId+
   	                    		 "&typeCode=TJY2_RL_LEAVE_CANCEL&status="+"&activityId="+activityId+"&areaFlag="+areaFlag+"&processId="+processId,
   	                     type: "POST",
   	                     dataType:'json',
   	                     data:{"bgLeave":bgLeave},
   	                     async: false,
   	                     success:function (data) {
   	                         if (data) {
   	                           	api.data.window.Qm.refreshGrid();
   				                api.close();
   	                         }
   	                     }
   	                 });
   	            }else{
   	              $.ligerDialog.alert("出错了！请重试！");
   	              $("body").unmask();
   	             }
   	         });
        	});
       	 }
   }
	//获取开始时间
	function getStartDate(){
		if(leave==""||leave==undefined){
			$("#startDate").val("");
			top.$.notice("请先选择假期种类！");
			return false;
		}
		startDate = $("#startDate").val();
		if(endDate!=""&&endDate!=undefined&&startDate!=""&&startDate!=undefined&&$("#leaveType").val()!=""&&$("#leaveType").val()!=undefined){
			var count=countLeave("<%=userId %>",startDate,endDate,$("#leaveType").val());
			$("#leaveCount1").val(count);
			$("#leaveCount2").val(count);
			queryLeave("<%=userId %>",startDate);
		}
	}
	//获取结束时间
	function getEndDate(){
		if(leave==""||leave==undefined){
			$("#endDate").val("");
			top.$.notice("请先选择假期种类！");
			return false;
		}
		if(startDate==""||startDate==undefined){
			$("#endDate").val("");
			top.$.notice("请先选择开始日期！");
			return false;
		}
		endDate = $("#endDate").val();
		if(endDate!=""&&endDate!=undefined&&startDate!=""&&startDate!=undefined&&$("#leaveType").val()!=""&&$("#leaveType").val()!=undefined){
			var count=countLeave("<%=userId %>",startDate,endDate,$("#leaveType").val());
			$("#leaveCount1").val(count);
			$("#leaveCount2").val(count);
			queryLeave("<%=userId %>",startDate);
		}
	}
	/* //获取销假时间
	function getXjrqDate(){
		endDate = $("#xjrqDate").val();
		if(endDate!=""&&startDate!=""){
			if(endDate<startDate){
				$.ligerDialog.error("销假时间不能小于开始时间！");
				return false;
			}else{
				$("#leaveCount2").ligerGetTextBoxManager().setEnabled();
				$("#leaveCount2").val(countLeave(startDate,endDate)-1);
			}
		}
	} */
	//计算假期天数
	function countLeave(peopleId,startDate,endDate,leaveType){
		startDate1 = new Date(startDate.replace(/-/g, "/"));
		endDate1 = new Date(endDate.replace(/-/g, "/"));
		$.ajax({
        	url: "BgLeaveAction/countDays.do?peopleId="+peopleId+"&startDate="+startDate+"&endDate="+endDate+"&leaveType="+leaveType,
            type: "POST",
            datatype: "json",
            contentType: "application/json; charset=utf-8",
            success: function (resp) {
            	if(resp.success){
	            	$("#leaveCount1").val(resp.days);
	            	$("#leaveCount2").val(resp.days);
            	}else{
            		$.ligerDialog.alert(resp.notice);
            		$("#startDate").val("");
            	}
            },
            error: function (data) {
            	$.ligerDialog.alert("出错了！请重试！");
            }
        });
		/* if(leave=="年假"||leave=="事假"){
		}else{
			var time = endDate1.getTime() - startDate1.getTime();
			if(time>=0){
				var days = parseInt(time / (1000 * 60 * 60 * 24))+1;
				return days;
			}else{
				$.ligerDialog.alert("开始时间不能大于结束时间！");
			}
		} */
	}
	//获取已请假种类及天数
	function queryLeave(peopleId,startDate){
		if(peopleId!=""&&peopleId!="null"){
			$.ajax({
	        	url: "BgLeaveAction/queryLeave.do?peopleId="+peopleId+"&startDate="+startDate,
	            type: "POST",
	            datatype: "json",
	            contentType: "application/json; charset=utf-8",
	            success: function (resp) {
	            	if(resp.success){
	            		if(pageStatus=="detail"){
			            	$("#total").html(resp.leaveInfo);
		            	}else{
			            	$("#total").val(resp.leaveInfo);
		            	}
	            	}else{
	            		$.ligerDialog.alert(resp.leaveInfo);
	            	}
	            },
	            error: function (data) {
	            	$.ligerDialog.alert("出错了！请重试！");
	            }
	        }); 
		}else{
			$.ligerDialog.alert("人员信息不正确！");
		}
	}
	//----------------------------------------------------------------------
	// 将上传的所有文件id写入隐藏框中
	function getUploadFile(){
		var attachId="";
		var i=0;
		$("#procufiles li").each(function(){
			attachId+=(i==0?"":",")+this.id;
			i=i+1;
		});
		if(attachId!=""){
			attachId=attachId.substring(0,attachId.length);
		}
		$("#uploadFiles").val(attachId);
	}
    function  view(){
	   top.$.dialog({
	         width: 1200,
	         height: 800,
	         lock: true,
	         parent: api,
	         data: {
	       	 window: window
	         },
	         title: "图片",
	         content: 'url:app/humanresources/preview.jsp?id='+uploadId
          });
	}
    //加载附件
    function  queryFiles(id){
    	if(id!=""&&id!="null"){
			$.ajax({
	        	url: "BgLeaveAction/queryFiles.do?id="+id,
	            type: "POST",
	            datatype: "json",
	            contentType: "application/json; charset=utf-8",
	            success: function (resp) {
	            	var list = resp.list;
	            	if(list!=null&&list!=""){
	            		for(var e in list){
	            			var picture="upload/"+list[e].filePath;
	            			var pictureId=list[e].id;
			            	$("#image").attr("src",picture); 
				            $("#image").show();
				            uploadId=pictureId;
				            $("#procufiles").attr("onclick","view()");
				            /* 
				            $("#image").attr("src","upload/"+files[0].path)
		   					$("#image").show();
		   					//将文件ID写入隐藏框中
		   					$("#uploadFiles").val(files[(files.length-1)].id)
		   					$("#procufiles").attr("onclick","view(files[(files.length-1)].id)"); */
	            		}
		            } 
	            },
	            error: function (data) {
	            	$.ligerDialog.alert("出错了！请重试！");
	            }
	        }); 
		}else{
			$.ligerDialog.alert("人员信息不正确！");
		}
	}
    
    //获取假期种类
	function valueChange(val,text){
		leave = text;
    	if((leave=="事假"||leave=="探亲假")&&modifyType!="back"){
    		$.ajax({
	        	url: "BgLeaveAction/queryYearDays.do?peopleId="+"<%=userId %>",
	            type: "POST",
	            datatype: "json",
	            contentType: "application/json; charset=utf-8",
	            success: function (resp) {
	            	if(resp.success){
	            		var yearDays=resp.yearDays;
	            		if(yearDays>0){
	            			$.ligerDialog.alert("你还有"+yearDays+"天年假可休"+"，不能休“事假”或“探亲假”！");
	            			$("input[name='_leaveType-txt']").ligerComboBox().selectValue("");
	            		}
	            	}else{
	            		$.ligerDialog.alert("获取数据出错！");
	            	}
	            },
	            error: function (data) {
	            	$.ligerDialog.alert("出错了！请重试！");
	            }
	        }); 
    	}
		
		if(endDate!=""&&endDate!=undefined&&startDate!=""&&startDate!=undefined&&$("#leaveType").val()!=""&&$("#leaveType").val()!=undefined){
			var count=countLeave("<%=userId %>",startDate,endDate,$("#leaveType").val());
			$("#leaveCount1").val(count);
			$("#leaveCount2").val(count);
		}
	}
    //假期天数与实际天数
    function setLeaveCount1(){
    	$("#leaveCount2").val($("#leaveCount1").val());
	}
	
    //----------------------------------------------------------------------------
    //打印
    function print(){
        var LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
        var strBodyStyle = "<style>" + $("#pstyle").html() + "</style>";
        LODOP.PRINT_INIT("打印休假请假单");
        LODOP.ADD_PRINT_HTM('12px', '10px', "750px", "100%", strBodyStyle+ $("#form1").html());
        LODOP.SET_PRINT_PAGESIZE (1, 0, 0,"A4");
        //LODOP.PREVIEW();
        LODOP.PRINT();
     }
    </script>
<style type="text/css" media="print" id="pstyle">
* {font-family:"宋体";font-size:15px;letter-spacing:normal;}
h1{font-family:宋体;font-size:6mm; text-align:center;margin:10px 0 0 0;}
table{ margin:-2 auto;width: 650px;}
table td{ height:40px}
.l-detail-table td, .l-detail-table {
    border-collapse: collapse;
    border: 1px solid black;
}
.l-detail-table {
    padding:0px;
    border:0px solid #CFE3F8;
    border-top:0px;
    border-left:0px;
    word-break:break-all;
    table-layout:fixed;
}
.check {
    width:720px;
}
.l-t-td-left{ text-align:center;}
.l-t-td-right{ padding-left:5px;}
#table2，#table3,#tabl4,#table5,#table6,#table7,#tabl8,#table9{
 padding:5px;
    border:0px solid #CFE3F8;
    border-top:0px;
    border-left:0px;
    word-break:break-all;
    table-layout:fixed;
}
#table1_td1{ height:40px;width:100px}
#table1_td2{ height:40px;width:160px}
#table1_td3{ height:40px;width:100px}
#table1_td4{ height:40px;width:160px}
#table2_td1{ height:40px;width:106px}
#table2_td2{ height:40px;width:463px}
#table3_td1,#table4_td1,#table5_td1,#table6_td1,#table7_td1,#table8_td1{ height:40px;width:118px}
#table9_td1{ height:40px;width:103px}
#table9_td2{ height:40px;width:463px}
</style>

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
</head>
<body>
<div class="navtab">
<div title="申请表" id="formObj1">
<form id="form1" action="BgLeaveAction/save.do" getAction="BgLeaveAction/detail.do?id=${param.id}">
<h1 style="padding:5mm 0 2mm 0;font-family:微软雅黑;font-size:6mm;text-align:center;">请&nbsp;假&nbsp;撤&nbsp;销&nbsp;审&nbsp;批&nbsp;表</h1></br>
<input type="hidden" id="id" name="id"/>
<input type="hidden" id="peopleId" name="peopleId" value="<%=userId%>"/>
<input type="hidden" id="depId" name="depId" value="<%=curUser.getDepartment().getId() %>"/>
<table id="table1" cellpadding="3" cellspacing="0" class="l-detail-table">
    <tr>
		<td class="l-t-td-left" id="table1_td1" >姓名</td>
		<td class="l-t-td-right" id="table1_td2" >
			<input id="peopleName" name="peopleName" type="text" ltype='text' required="true" readonly="readonly" value="<%=e.getName()%>" validate="{maxlength:100}"  ligerui="{iconItems:[{icon:'user'}]}"/>
		</td>
		<td class="l-t-td-left" id="table1_td3" >所在部门</td>
		<td class="l-t-td-right" id="table1_td4" >
			<input id="depName" name="depName" type="text" ltype="text" readonly="readonly" value="<%=curUser.getDepartment().getOrgName() %>" validate="{maxlength:100}" ligerui="{iconItems:[{icon:'org'}]}"/>
		</td>
	</tr>
	<tr>
		<td class="l-t-td-left">假期种类</td>
		<td class="l-t-td-right" >
		<%-- <u:combo name="leaveType" code="TJY2_BG_LEAVE_TYPE" validate="required:true"  attribute="onSelected:valueChange"/> --%>
			<c:if test="${param.allow=='0' }">
				<u:combo name="leaveType" code="TJY2_BG_LEAVE_TYPE" validate="required:true"  attribute="onSelected:valueChange,initValue:'NJ'"/>
			</c:if>
			<c:if test="${param.allow=='1'}">
				<u:combo name="leaveType" code="TJY2_RL_LEAVE_TYPE" validate="required:true"  attribute="onSelected:valueChange,initValue:'GWWC'"/>
			</c:if>
			<c:if test="${param.isChecked=='undefined' }">
				<u:combo name="leaveType" code="TJY2_RL_LEAVE_TYPE" validate="required:true"  attribute="onSelected:valueChange,initValue:'GWWC'"/>
			</c:if>
		</td>
		<td class="l-t-td-left">假期天数</td>
		<td class="l-t-td-right" >
		<input id="leaveCount1" name="leaveCount1" type="text" ltype='text' ligerUi="{disabled:true}" required="true"/>
		</td>
	</tr>
	<tr>
		<td class="l-t-td-left">开始时间</td>
		<td class="l-t-td-right" >
		<input name="startDate" id="startDate" type="text" ltype='date' required="true" onchange="getStartDate()" ligerui="{format:'yyyy-MM-dd'}" /></td>
		<td class="l-t-td-left">结束时间</td>
		<td class="l-t-td-right" >
		<input name="endDate" id="endDate" type="text" ltype='date' required="true" onchange="getEndDate()" ligerui="{format:'yyyy-MM-dd'}" /></td>
	</tr>
	</table>
    <table id="table2" cellpadding="3" cellspacing="0" class="l-detail-table" >
    <tr>
		<td class="l-t-td-left" width="110px" id="table2_td1">已请假种类<br />及天数</td>
		<td class="l-t-td-right" colspan="4" id="table2_td2">
		<input id="total" name="total" type="text" ltype='text' required="true" ligerUi="{disabled:true}"/></td>
	</tr>
	</table>
  	<table id="table3" cellpadding="3" cellspacing="0" class="l-detail-table">
     <tr>
        <td class="l-t-td-left" width="10px" rowspan="2" id="table3_td1">请假事由</td>
	    <td colspan="4" id="table3_td2">
		<textarea id="leaveReason" name="leaveReason" required="true" rows="4" cols="50" class="l-textarea" validate="{maxlength:400}"></textarea>
	    </td>
     </tr>
     <tr>
        <td class="l-t-td-left" align="right">请假人签字</td>
	    <td class="l-t-td-right">
		<input id="peopleSign" name="peopleSign" type="text" ltype='text' required="true" validate="{maxlength:100}"/></td>
        <td class="l-t-td-left">日期</td>
	    <td class="l-t-td-right">
		<input name="peopleSignDate" id="peopleSignDate" type="text" ltype='date' required="true" ligerui="{format:'yyyy-MM-dd'}" /></td>
     </tr>
  </table>
  <table id="table4" cellpadding="3" cellspacing="0" class="l-detail-table">
     <tr>
        <td class="l-t-td-left" width="10px" rowspan="2" id="table4_td1">部&nbsp;&nbsp;&nbsp;&nbsp;门<br />负&nbsp;责&nbsp;人<br />意&nbsp;&nbsp;&nbsp;&nbsp;见</td>
	    <td colspan="4">
		<textarea id="ksfzryj" name="ksfzryj" rows="4" cols="50" class="l-textarea" validate="{maxlength:400}"></textarea>
	    </td>
     </tr>
     <tr>
     	<td class="l-t-td-left" align="right">部门负责人签字</td>
	    <td class="l-t-td-right">
		<input id="ksfzryjSing" name="ksfzryjSing" type="text" ltype='text' validate="{maxlength:100}"/></td>
        <td class="l-t-td-left">日期</td>
	    <td class="l-t-td-right">
		<input name="ksfzryjDate" id="ksfzryjDate" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" /></td>
     </tr>
  </table>
  <table id="table5" cellpadding="3" cellspacing="0" class="l-detail-table">
     <tr>
        <td class="l-t-td-left" width="10px" rowspan="2" id="table5_td1">人&nbsp;&nbsp;&nbsp;&nbsp;事<br />意&nbsp;&nbsp;&nbsp;&nbsp;见</td>
	    <td colspan="4">
		<textarea id="rsyj" name="rsyj" rows="4" cols="50" class="l-textarea" validate="{maxlength:400}"></textarea>
	    </td>
     </tr>
     <tr>
     	<td class="l-t-td-left" align="right">人事签字</td>
	    <td class="l-t-td-right">
		<input id="rsyjSign" name="rsyjSign" type="text" ltype='text' validate="{maxlength:100}"/></td>
        <td class="l-t-td-left">日期</td>
	    <td class="l-t-td-right">
		<input name="rsyjDate" id="rsyjDate" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" /></td>
     </tr>
  </table>
  <table id="table6" cellpadding="3" cellspacing="0" class="l-detail-table">
     <tr>
        <td class="l-t-td-left" width="10px" rowspan="2" id="table5_td1">分管领导<br />意&nbsp;&nbsp;&nbsp;&nbsp;见</td>
	    <td colspan="4">
		<textarea id="fgyldyj" name="fgyldyj" rows="4" cols="50" class="l-textarea" validate="{maxlength:400}"></textarea>
	    </td>
     </tr>
     <tr>
     	<td class="l-t-td-left" align="right">分管院领导签字</td>
	    <td class="l-t-td-right">
		<input id="fgyldyjSign" name="fgyldyjSign" type="text" ltype='text' validate="{maxlength:100}"/></td>
        <td class="l-t-td-left">日期</td>
	    <td class="l-t-td-right">
		<input name="fgyldyjDate" id="fgyldyjDate" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" /></td>
     </tr>
  </table>
  <table id="table7" cellpadding="3" cellspacing="0" class="l-detail-table">
     <tr>
        <td class="l-t-td-left" width="10px" rowspan="2" id="table7_td1">院长意见</td>
	    <td colspan="4">
		<textarea id="yzyj" name="yzyj" rows="4" cols="50" class="l-textarea" validate="{maxlength:400}"></textarea>
	    </td>
     </tr>
     <tr>
     	<td class="l-t-td-left" align="right">院长签字</td>
	    <td class="l-t-td-right">
		<input id="yzyjSign" name="yzyjSign" type="text" ltype='text' validate="{maxlength:100}"/></td>
        <td class="l-t-td-left">日期</td>
	    <td class="l-t-td-right">
		<input name="yzyjDate" id="yzyjDate" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" /></td>
     </tr>
  </table>
  <table id="table8" cellpadding="3" cellspacing="0" class="l-detail-table">
     <tr>
        <td class="l-t-td-left" width="10px" rowspan="2" id="table8_td1">销假记录</td>
		<td class="l-t-td-left" align="right">销假日期</td>
		<td class="l-t-td-right" >
		<input name="xjrqDate" id="xjrqDate" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" /></td>
		<td class="l-t-td-left">实际请（休）<br />假天数</td>
		<td class="l-t-td-right">
		<input id="leaveCount2" name="leaveCount2" type="text" ltype='text' required="true" /></td>
     </tr>
     <tr>
		<td class="l-t-td-left" align="right">本人签字</td>
		<td class="l-t-td-right" >
		<input id="xjPeopleSign" name="xjPeopleSign" type="text" ltype='text' validate="{maxlength:100}"/></td>
		<td class="l-t-td-left">人力资源管理<br />部经办人签字</td>
		<td class="l-t-td-right">
		<input id="xjHrSign" name="xjHrSign" type="text" ltype='text' validate="{maxlength:100}"/></td>
	</tr>
  </table>
  <table id="table9" cellpadding="5" cellspacing="0" class="l-detail-table">
        <tr>
        	<td id="table9_td1" class="l-t-td-left" width="110px" rowspan="4">备注</td>
	    	<td id="table9_td2" colspan="4" rowspan="4" class="l-t-td-right"><h3>  1、干部职工请假一天及以内的由部门负责人签字同意，一天以上的须经分管领导签字同意；</br>2、各部门负责人请假一天及以内的由分管领导签字同意；一天以上的须经院长批准；<br />3、两天以上病假须出具医院相关证明；<br />4、此表由人力资源管理部统一存档备查。</h3></td>
        </tr>
  </table>
  </form>
</div>
<div title="附件上传" id="formObj2">
	<table cellpadding="3" cellspacing="0" class="l-detail-table">
		<tr>
			<td class="l-t-td-left">医院证明：</td>
			<td class="l-t-td-right" >
				<input id="uploadFiles" name="uploadFiles" type="hidden"/>
				<span id="onefileDIV">
				<a class="l-button" id="onefileBtn">
				<span class="l-button-main"><span class="l-button-text">选择文件</span></span>
				</a></span>
			</td>	
		</tr>
		<tr>
			<td class="l-t-td-left"></td>
	        <td class="l-t-td-right">
			    <a id="procufiles">
			    <img id="image" src="" style="width:400px;height: 300px ;padding-left: 150px"></img>
			    </a>
	        </td>	
		</tr>
	</table>
</div>
</div>
</body>
</html>
