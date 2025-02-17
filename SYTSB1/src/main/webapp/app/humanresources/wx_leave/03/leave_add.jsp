
<%@page import="com.lsts.humanresources.dao.EmployeeBaseDao"%>
<%@page import="com.lsts.humanresources.bean.EmployeeBase"%>
<%@page import="com.khnt.rbac.impl.bean.Employee"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %> 
<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<!DOCTYPE html>
<html>
<head>
<title>请休假申请</title>
<% 
	/* 请休假id */
	String leave_id=request.getParameter("leave_id");
    if(leave_id==null){
    	leave_id=(String)session.getAttribute("leave_id");
    }
	/* 员工id */
	String id=request.getParameter("id");
    if(id==null){
    	id=(String)session.getAttribute("id");
    }
    /* 用户id */
	String user_id=request.getParameter("userId");

    /* add页面步骤 */
    String step=request.getParameter("step");
    /* 是否公务外出 */
    String allow=request.getParameter("allow");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no"/>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/css/public.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/css/header.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/css/content.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/css/selectFilter.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/css/jquery.notice.css">

<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/k/km/lib/jquery.min.js"></script> --%>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath }/app/car/weixin/js/jquery-1.7.min.js"></script> --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/laydate/laydate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/js/selectFilter.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/js/time.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/js/jquery.notice.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/pub/bpm/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/k/km/lib/app-end.js"></script>
<script src="${pageContext.request.contextPath}/app/k/km/lib/kh-mobile.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/k/jqm/a-main.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/weixin/js/timejs/date.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/weixin/js/timejs/iscroll.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/weixin/js/timejs/mobiscroll.2.13.2.js"></script>
<script type="text/javascript">
var flowId='';
var param='';
  $(function(){
	    var leave_id="<%=leave_id %>";
	    var id = "<%=id%>";
	    <%
			if("0".equals(allow)){
		%>
			getcodetabl("TJY2_BG_LEAVE_TYPE","leaveType");
		<%
			}else if("1".equals(allow)){
		%>
	  	getcodetabl("TJY2_RL_LEAVE_TYPE","leaveType");
	  	<%
			}
		%>
	  	getcodetabl("TJY2_BG_LEAVE_STATUS","applyStatus");
		function getcodetabl(code,id){   
		   $.ajax({
			url : "${pageContext.request.contextPath}/WxLeaveAction/getcodetabl.do?tablname="+code,
			type : "POST",
			async : false,
			success : function(json) {		
				var	s="";
				if(json.success){
				for(var i in json.data) {	
		
				s+="<option value='"+json.data[i].value+"'>"+json.data[i].name+"</option>";				    				  				
				}
				$("#"+id).html(s);	
				
				}
			} 
		 });
		}
		if(id!="null"){
			$("#ztsq").hide();
			
			$.ajax({
				url : "${pageContext.request.contextPath}/WxLeaveAction/loadUser.do?id="+id,
				type : "POST",
				async : false,
				success : function(resp) {		
					$("#peopleName").val(resp.peopleName);
					$("#depName").val(resp.workDepartmentName);
					$("#peopleNameAnddepName").val(resp.peopleName+"（"+resp.workDepartmentName+"）");
				} 
			 });
			
		}else{
			/* alert("对不起！你的微信号未与特检平台同步！"); */
		}
		if(leave_id!="null"){
			$("#ztsq").show();
			$("#next1").hide();
			document.getElementById("btnGroup").className = "Button2"; 
			$("#formObj").transform("detail");
		    $("#formObj").setValues("${pageContext.request.contextPath}/WxLeaveAction/detail.do?id="+leave_id,function(res){
		    	$("#peopleNameAnddepName").html(res.data.peopleName+"（"+res.data.depName+"）"); 
		    	var b=res.data.startDate;
				 	if(b==null){
				 	  $("#startDate").html();
				 	}else{
				 	 $("#startDate").html(b.substring(b.indexOf("/")+1,b.lastIndexOf(" ")));
				 	}
				 var c=res.data.endDate;
				 	if(b==null){
				 	  $("#endDate").html();
				 	}else{
				 	 $("#endDate").html(c.substring(c.indexOf("/")+1,c.lastIndexOf(" ")));
				 	}
				 	//状态
				 	var applyStatus=$("#applyStatus").text();
			    	if(applyStatus=="未提交" || applyStatus=="已撤销" || applyStatus=="审批不通过" || applyStatus=="审批通过" ||applyStatus=="已销假"){
						$("#next2").hide();
						document.getElementById("btnGroup").className = "Button1"; 
			    		//$("#Button2").hide();
					}else{
						$.ajax({
							url : "${pageContext.request.contextPath}/WxLeaveAction/getCheckName.do?id="+res.data.id+"&orgId="+res.data.depId,
							type : "post",
							datatype : "json",
							contentType: "application/json; charset=utf-8",
					        success : function (data) {
					        	if(data.name!=""&&data.name!="undefined"&&data.name!=null){
					        		$("#applyStatus").html(applyStatus+"("+data.name+")");
					        	}
					        }
						});
						$("#next2").show();
						document.getElementById("btnGroup").className = "Button2"; 
						//$("#Button1").hide();
						//$("#Button2").show();
					}
			    	
			    // 申请及审批信息
			    if(res.data.peopleName!=null){
				    $("#peopleSign").html(res.data.peopleName); 
				}else{
					$("#people").hide();
				}
				if(res.data.peopleSignDate!=null){
				    $("#peopleSignDate").html(res.data.peopleSignDate.substring(0,res.data.peopleSignDate.lastIndexOf(":"))); 
				}
				if(res.data.leaveReason!=null){
				    $("#peopleLeaveReason").html(res.data.leaveReason); 
				}
			    if(res.data.ksfzryj!=null){
			    	$("#ksfzryj").html(res.data.ksfzryj); 
			    }else{
			    	$("#ksfzr").attr("class","ad_mind");
			    	$("#one").hide();
			    }
			    if(res.data.ksfzryjSing!=null){
			    	$("#ksfzryjSing").html(res.data.ksfzryjSing); 
			    }else{
					$("#ksfzr").hide();
				}
			    if(res.data.ksfzryjDate!=null){
			    	$("#ksfzryjDate").html(res.data.ksfzryjDate.substring(0,res.data.ksfzryjDate.lastIndexOf(":"))); 
			    }
			    if(res.data.rsyj!=null){
			    	$("#rsyj").html(res.data.rsyj); 
			    }else{
			    	$("#rs").attr("class","ad_mind")
			    	$("#two").hide();
			    }
			    if(res.data.rsyjSign!=null){
			    	$("#rsyjSign").html(res.data.rsyjSign); 
			    }else{
					$("#rs").hide();
				}
			    if(res.data.rsyjDate!=null){
			    	$("#rsyjDate").html(res.data.rsyjDate.substring(0,res.data.rsyjDate.lastIndexOf(":"))); 
			    }
			    if(res.data.fgyldyj!=null){
			    	$("#fgyldyj").html(res.data.fgyldyj); 
			    }else{
			    	$("#fgyld").attr("class","ad_mind")
			    	$("#three").hide();
			    }
			    if(res.data.fgyldyjSign!=null){
			    	$("#fgyldyjSign").html(res.data.fgyldyjSign); 
			    }else{
					$("#fgyld").hide();
				}
			    if(res.data.fgyldyjDate!=null){
			    	$("#fgyldyjDate").html(res.data.fgyldyjDate.substring(0,res.data.fgyldyjDate.lastIndexOf(":"))); 
			    }
			    if(res.data.yzyj!=null){
			    	$("#yzyj").html(res.data.yzyj); 
			    }else{
			    	$("#yz").attr("class","ad_mind")
			    	$("#four").hide();
			    }
			    if(res.data.yzyjSign!=null){
			    	$("#yzyjSign").html(res.data.yzyjSign); 
			    }else{
					$("#yz").hide();
				}
			    if(res.data.yzyjDate!=null){
			    	$("#yzyjDate").html(res.data.yzyjDate.substring(0,res.data.yzyjDate.lastIndexOf(":"))); 
			    }
		    });
		}else{
			$("#next2").hide();
			document.getElementById("btnGroup").className = "Button2"; 
			//$("#Button1").show();
			//$("#Button2").hide();
		}
		
		//提交表单
		$("#next1").click(function() {
			removeSave();
			saveInfo();
		});
		//撤销申请
		$("#next2").click(function() {
			$.ajax({
				url : "${pageContext.request.contextPath}/WxLeaveAction/revokeLeave.do?id="+'<%=leave_id%>',
				type : "post",
				datatype : "json",
				contentType: "application/json; charset=utf-8",
		        success : function (data) {
		        	if(data.success){
		        		 $("#next1").hide();
		        		 $("#next2").hide();
		        		 document.getElementById("btnGroup").className = "Button1"; 
		        		 jQnotice('撤销成功！');
		        		 location.href="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/leave_index.jsp";
		        		//dAlert("撤销成功！"); 
		        		 //alert("撤销成功！");
		        		 //setTimeout("alert('撤销成功')", 150 );
		        		 
		        	}else{
		        		//dAlert("出错了，撤销失败！");
		        		//alert("撤销失败！");
		        		//setTimeout("alert('撤销失败')", 150 );
		        		jQnotice('撤销失败！');
		        		//dialogShow(data.msg, 300, 100);
		        	}
		        }
			});  
		
		});
		//返回
		$("#next3").click(function() {
			location.href="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/leave_index.jsp";
		});
		
            /* var curr = new Date().getFullYear();
            var opt={};
			opt.date = {preset : 'date'};
			opt.datetime = {preset : 'datetime'};
			opt.time = {preset : 'time'};

          opt.default = {
				theme: 'android-holo light', //皮肤样式
		        display: 'modal', //显示方式 
		        mode: 'scroller', //日期选择模式
				dateFormat: 'yyyy-mm-dd',
				lang: 'zh',
				showNow: true,
				nowText: "今天",
				stepMinute: 5,
		        startYear: curr - 0, //开始年份
		        endYear: curr + 20 //结束年份
			}; 
            $('.settings').bind('change', function() {
                var demo = 'datetime';
                if (!demo.match(/select/i)) {
                    $('.demo-test-' + demo).val('');
                }
                $('.demo-test-' + demo).scroller('destroy').scroller($.extend(opt['date'], opt['default']));
                $('.demo').hide();
                $('.demo-' + demo).show();
            });
            $('#demo').trigger('change'); */
            $('#leaveType').change(function(){
            	var type=$("#leaveType").val();
                if(type=='SHIJ'||type=='TQJ'){
                	$.ajax({
        	        	url: "BgLeaveAction/queryYearDays.do?peopleId="+"<%=id %>",
        	            type: "POST",
        	            datatype: "json",
        	            contentType: "application/json; charset=utf-8",
        	            success: function (resp) {
        	            	if(resp.success){
        	            		var yearDays=resp.yearDays;
        	            		if(yearDays>0){
        	            			//dAlert("你还有"+yearDays+"天年假可休"+"，不能休“事假”或“探亲假”！");
        	            			//alert("亲，您还有"+yearDays+"天年假可休"+"，不能休“事假”或“探亲假”！");
        	            			//setTimeout("alert('亲，您还有"+yearDays+"天年假可休"+"，不能休“事假”或“探亲假”！')", 150 );
        	            			jQnotice("您还有"+yearDays+"天年假"+"，不能休“事假”或“探亲假”！");
        	            			<%
	        	            			if("0".equals(allow)){
	        	            		%>
	        	            			getcodetabl("TJY2_BG_LEAVE_TYPE","leaveType");
	        	            		<%
	        	            			}else if("1".equals(allow)){
	        	            		%>
	        	            	  		getcodetabl("TJY2_RL_LEAVE_TYPE","leaveType");
	        	            	  	<%
	        	            			}
	        	            		%>
        	            		}
        	            	}else{
        	            		//dAlert("获取数据出错！");
        	            		//alert("获取数据失败！");
        	            		//setTimeout("alert('获取数据失败！')", 150 );
        	            		jQnotice("获取数据失败！");
        	            	}
        	            },
        	            error: function (data) {
        	            	//dAlert("出错了！请重试！");
        	            	//alert("请重试！");
        	            	//setTimeout("alert('请重试！')", 150 );
        	            	jQnotice("请重试！");
        	            }
        	        });
                }
 
            })
	
  })
  
  
  function saveInfo(){
	  if($("#leaveType").val()==""||$("#startDate").val()==""||$("#endDate").val()==""||$("#peopleName").val()==""||$("#depName").val()==""||$("#leaveCount1").val()==""||$("#total").val()==""||$("#leaveReason").val()==""){
		    //dialogShow("必填项不能为空！", 300, 100);
		    //alert("亲，带*号的是必填项，不能为空！");
		    //setTimeout("alert('亲，带*号的是必填项，不能为空！')", 150 );
		    jQnotice("亲，带*号的是必填项，不能为空！");
			addSave();
			return false;
		}
		var leaveType=$("#leaveType").val(); 
		var startDate=$("#startDate").val();      
		var endDate=$("#endDate").val();
		if(!compareDate(startDate,endDate) ){
			var days = "亲，开始时间不能大于结束时间！";
			 //dAlert(days); 
			 //alert(days);
			 //setTimeout("alert('"+days+"')", 150 );
			 jQnotice(days);
			 addSave();
			 return false;
		}
		var leaveCount1=$("#leaveCount1").val();
		var leaveReason=encodeURI(encodeURI($("#leaveReason").val()));
		var total=encodeURI(encodeURI($("#total").val()));
		param="id="+"<%=id%>"+"&leaveType="+leaveType+"&startDate="+startDate+"&endDate="+endDate+"&leaveCount1="+leaveCount1
		         +"&leaveReason="+leaveReason+"&userId="+"<%=user_id%>"+"&total="+total;
		$.ajax({
			url : "${pageContext.request.contextPath}/WxLeaveAction/mobileSubmit.do?"+param,
			type : "post",
			datatype : "json",
			contentType: "application/json; charset=utf-8",
	        success : function (data) {
	        	if(data.success){        	
	        		 id=data.data;
	        		 $("#next1").hide();	
	        		 document.getElementById("btnGroup").className = "Button1"; 
	        		 jQnotice('提交成功！');
	        		 location.href="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/leave_index.jsp";
	        		 //dAlert("提交成功！");
	        		 //alert("提交成功！");
	        		 //setTimeout("alert('提交成功')", 150 );
	     		     addSave(); 
	        	}else{
	        		//dAlert("出错了，提交失败！");
	        		//alert("提交失败！");
	        		//setTimeout("alert('提交失败')", 150 );
	        		jQnotice('提交失败！');
	     		    addSave();
	        		dialogShow(data.msg, 300, 100);
	        	}
	        }
		});  
  }
  function setleaveCount1(){
	  var startDate=$("#startDate").val();
	  var endDate=$("#endDate").val();
	  var leaveType=$("#leaveType").val();
	  if(startDate!=null&&endDate!=null&&startDate!=""&&endDate!=""&&leaveType!=null&&leaveType!=""){
		  if(startDate!=null&&endDate!=null&&startDate!=""&&endDate!=""){
			  if(compareDate(startDate,endDate) ){
				  //dAlert(compareDate(startDate,endDate));   
				  //jQnotice(compareDate(startDate,endDate));
			  	countLeave(startDate,endDate,"<%=id%>",leaveType);
			  } else {
				  var days = "亲，开始时间不能大于结束时间！";
				  //dAlert(days); 
				  //alert(days);
				  //setTimeout("alert('"+days+"')", 150 );
				  jQnotice(days);
			  }
		  } else {
			  return false;
		  }
	  }
  }
  
  function checkdate(){
	  var startDate=$("#startDate").val();
	  var endDate=$("#endDate").val();
	  var leaveType=$("#leaveType").val();
	  if(startDate!=null&&endDate!=null&&leaveType!=null&&startDate!=""&&endDate!=""&&leaveType!=""){
		  if(compareDate(startDate,endDate) ){
		  	countLeave(startDate,endDate,"<%=id%>",leaveType);
		  } else {
			  var days = "亲，开始时间不能大于结束时间！";
			  //dAlert(days);
			  //alert(days);
			  //setTimeout("alert('"+days+"')", 150 );
			  jQnotice(days);
		  }
	  } else {
		  return ;
	  }
  }
	
  function compareDate(startDate,endDate){
	  startDate = new Date(startDate.replace(/-/g, "/"));
	  endDate = new Date(endDate.replace(/-/g, "/"));
	  var flag = false ;
	  var time = endDate.getTime() - startDate.getTime();
	  if(time>=0){
		  flag = true ;
	  }
	  return flag ;
  }
  function countLeave(startDate,endDate,peopleId,leaveType){
		
		var leaveType = $("#leaveType").val();
		$.ajax({
			url : "${pageContext.request.contextPath}/BgLeaveAction/countDays.do?peopleId="+peopleId+"&startDate="+$("#startDate").val()+"&endDate="+$("#endDate").val()+"&leaveType="+leaveType,
			type : "POST",
			datatype : "json",
			contentType: "application/json; charset=utf-8",
			success : function (data) {
	        	if(data.success){
	        		$("#leaveCount1").val(data.days);
	        		queryLeave(peopleId,$("#startDate").val());
	        	}else{
	        		//alert(data.notice);
	        		//dAlert(data.notice);
	        		//setTimeout("alert('"+data.notice+"')", 150 );
	        		jQnotice(data.notice);
	        	}
	        } 
		 });
	}
	//获取已请假种类及天数
	function queryLeave(peopleId,startDate){
		if(peopleId!=""&&peopleId!="null"){
			$.ajax({
	        	url: "${pageContext.request.contextPath}/WxLeaveAction/queryLeave.do?peopleId="+peopleId+"&startDate="+startDate,
	            type: "POST",
	            datatype: "json",
	            contentType: "application/json; charset=utf-8",
	            success: function (resp) {
	            	if(resp.success){
	            		$("#total").val(resp.leaveInfo);
	            	}else{
	            		//dAlert(resp.leaveInfo);
	            		//setTimeout("alert('"+resp.leaveInfo+"')", 150 );
	            		jQnotice(resp.leaveInfo);
	            	}
	            },
	            error: function (data) {
	            	//dAlert("出错了！请重试！");
	            	//alert("请重试！");
	            	//setTimeout("alert('请重试！')", 150 );
	            	jQnotice("请重试！");
	            }
	        }); 
		}else{
			//dAlert("人员信息不正确！");
			//alert("亲，人员信息不正确！");
			//setTimeout("alert('亲，人员信息不正确！')", 150 );
			jQnotice("亲，人员信息不正确！");
		}
	}	
	
	function removeSave(){
		$("#next1").hide();
		document.getElementById("btnGroup").className = "Button1"; 
	}
	
	function addSave(){
		$("#next1").show();
		document.getElementById("btnGroup").className = "Button2"; 
	}
</script>
</head>
<body >
	<!-- <div class="settings" style="display:none;">
  		<select name="demo" id="demo" style="border: solid 0px #000;appearance:none;-moz-appearance:none;-webkit-appearance:none;">
       		<option value="date">日期</option>
  		</select>
	</div> -->
	<form id="formObj" name="formObj" method="post" action="" onsubmit="return false;" getAction="" pageStatus="">
	<input id="depName" name="depName" type="hidden">
	<input id="peopleName" name="peopleName" type="hidden">
	<section id="web" class="holiday">
		<header id="header"> <img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/tb.png"> </header>
		<section id="content">
			<section class="department">
				<section class="departmentCenter">
					<section class="top"><strong><b>*</b>姓&#12288;&#12288;名：</strong>
						<input type="text" class="task" name="peopleNameAnddepName"  id="peopleNameAnddepName" ext_name="姓名" ext_isNull="1" readonly="readonly">
						<!-- <input type="text" name="name" placeholder="" class="task"> -->
					</section>
					<section class="top"><strong><b>*</b>种&#12288;&#12288;类：</strong>
						<select id="leaveType" name="leaveType" ext_name="假期种类" ext_isNull="1" maxLength="18" ></select>
						<!-- <select>
							<option>休假</option>
						</select> -->
						<span class="arrow"><img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/dropDown.png"></span></section>
					<section class="top"><strong><b>*</b>开始时间：</strong>
						<input type="text" class="task" id="startDate" name="startDate"  placeholder="时间格式为2018-01-01" ext_name="假期开始时间" ext_isNull="1" maxLength="18" >
						<!-- <input type="text" class="task" placeholder="请选择日期" id="test1"> -->
					</section>
					<section class="top"><strong><b>*</b>结束时间：</strong>
						<input type="text" class="task" id="endDate" name="endDate" placeholder="时间格式为2018-01-01" ext_name="假期结束时间" ext_isNull="1" maxLength="18">
						<!-- <input type="text" class="task" placeholder="请选择日期" id="test2"> -->
					</section>
					<section class="top"><strong><b>*</b>天&#12288;&#12288;数：</strong>
						<input type="text" class="task" id="leaveCount1" name="leaveCount1" ext_name="假期天数" ext_isNull="1" maxLength="11" readonly="readonly" onclick="setleaveCount1();">
						<!-- <input type="text" name="dates" placeholder="" class="task"> -->
					</section>
					<section class="top"><strong><b>*</b>请假记录：</strong>
						<textarea type="text" class="task" id="total" name="total" ext_name="已请假种类及天数" readonly="readonly" ext_isNull="1" style="height: 60px;"></textarea>
						<!-- <textarea class="task">bbbb</textarea> -->
					</section>
					<section class="top"><strong><b>*</b>理&#12288;&#12288;由：</strong>
						<textarea class="task" id="leaveReason"  name="leaveReason"   ext_name="请假理由" ext_isNull="1" style="height: 60px;"></textarea>
						<!-- <textarea class="task">bbbb</textarea> -->
					</section>
					<!-- 
					<section class="bottom" id="ztsq"><strong><b>*</b>申请状态：</strong>
						<select class="task" id="applyStatus" name="applyStatus" ext_name="请假状态" ext_isNull="1"></select>
					</section>
					 -->
					<%
						if("detail".equals(step)){
							%>
							<section class="add_bottom">
								<ul>
									<li id="people">
										<div>
										<div class="add_fir">
											<img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/user.png" />
											<span class="add_distance" id="peopleSign"></span>
											<span class="add_distance">发起申请</span>
										</div>
										<div class="add_sen" id="peopleSignDate"></div>
<!-- 										<div class="add_third"><span class="add_third2" id="peopleLeaveReason"></span></div> -->
										</div>
									</li>	
									<li id="ksfzr">
										<div>
										<div class="add_fir">
											<img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/user.png" />
											<span class="add_distance" id="ksfzryjSing"></span>
											<span class="add_distance add_agree">已同意</span>
										</div>
										<div class="add_sen" id="ksfzryjDate"></div>
										<div class="add_third" id="one">
											<span class="add_third2"  id="ksfzryj"></span>
										</div>
										</div>
									</li>
									<li id="rs">
										<div>
										<div class="add_fir">
											<img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/user.png" />
											<span class="add_distance" id="rsyjSign"></span>
											<span class="add_distance add_agree">已同意</span>
										</div>
										<div class="add_sen" id="rsyjDate"></div>
										<div class="add_third" id="two">
											<span class="add_third2"  id="rsyj"></span>
										</div>
										</div>
									</li>
									<li id="fgyld">
										<div>
										<div class="add_fir">
											<img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/user.png" />
											<span class="add_distance" id="fgyldyjSign"></span>
											<span class="add_distance add_agree">已同意</span>
										</div>
										<div class="add_sen" id="fgyldyjDate"></div>
										<div class="add_third" id="three">
											<span class="add_third2"  id="fgyldyj"></span>
										</div>
										</div>
									</li>
									<li id="yz">
										<div>
										<div class="add_fir">
											<img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/user.png" />
											<span class="add_distance" id="yzyjSign"></span>
											<span class="add_distance add_agree">已同意</span>
										</div>
										<div class="add_sen" id="yzyjDate"></div>
										<div class="add_third" id="four">
											<span class="add_third2"  id="yzyj"></span>
										</div>
										</div>
									</li>
								</ul>
							</section>
							<%						
						}
					%>
				</section>
			</section>
		</section>
		<!-- <section class="Button1"> <a href="itemList.html">返回</a> </section> -->
		<!-- <section class="Button2"> <a href="javascript:">提交</a> <a href="itemList.html">返回</a> </section> -->
		<section class="Button3" id="btnGroup" >
			<a id="next1" href="javascript:void(0);">提交</a>
			<a id="next2" href="javascript:void(0);">撤销</a>
			<a id="next3" href="javascript:void(0);">返回</a>
		</section>
	</section>
	</form>
</body>
</html>

		