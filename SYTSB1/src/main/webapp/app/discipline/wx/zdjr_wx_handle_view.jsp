<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>主动介入</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no"/>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/css/public.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/css/header.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/app/qualitymanage/manualReport/css/content.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/css/selectFilter.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/css/jquery.notice.css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/app/weixin/js/timejs/common.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/weixin/js/timejs/mobiscroll.2.13.2.css"  />

<script type="text/javascript" src="${pageContext.request.contextPath}/app/k/km/lib/app-end.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/laydate/laydate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/js/selectFilter.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/js/jquery.notice.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/pub/bpm/js/util.js"></script>
<script src="${pageContext.request.contextPath}/app/k/km/lib/kh-mobile.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/app/weixin/js/timejs/date.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/weixin/js/timejs/iscroll.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/weixin/js/timejs/mobiscroll.2.13.2.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/app/k/jqm/a-main.js" ></script>

  <%
        String id = request.getParameter("id");
        CurrentSessionUser user = SecurityUtil.getSecurityUser();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sf.format(new Date());
    %>
<script type="text/javascript">
  $(function(){
      //时间初始化格式----------开始
            var curr = new Date().getFullYear();
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
            $('#demo').trigger('change'); 
            //时间初始化格式----------结束
            //初始化数据
            $.get("${pageContext.request.contextPath }/disciplineZdjrAction/wx/detail.do?id=${param.id}",function(res) {
            	if(res.success){
    				var detailData = res.data;
    				 $("#formObj").setValues(detailData);
    				var jdsj= detailData.jdsj_start.substring(0,10).replace(/\-/g,'-');
    				var jdsj_end= detailData.jdsj_end.substring(0,10).replace(/\-/g,'-');
    				 $("#jdsj_start").val(jdsj);
    				 $("#jdsj_end").val(jdsj_end);
    				 $("#jdfs").attr({"disabled":"disabled"});
    				 $("#jdlb").attr({"disabled":"disabled"});
    				 $("#jdgzsy").attr({"disabled":"disabled"});
    			}
            });
            

            $.ajax({
                url: "${pageContext.request.contextPath }/disciplineZdjrAction/wx/getFlowStep.do?id=${param.id}",
                type: "POST",
                async: false,
                success: function (data) {
                    if ( data["success"] ) {
                        var flowList = data["data"]["flowStep"];
                        for (var i = 0; i < flowList.length; i++) {
                            var flow = flowList[i];
                            if(i==0){
                            	$("#splc").append('<li id="people" class="ad_mind"><div><div class="add_fir"><img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/user.png" />'
                            			+'<span class="add_distance" id="peopleSign">'+ flow["op_user_name"] +'</span><span class="add_distance">发起申请</span></div><div class="add_sen" id="peopleSignDate">'+ flow["op_time"].substring(0, 16).replace(/\-/g, '.') +'</div></div></li>');
                            	
                            }else{
                            	$("#splc").append('<li id="ksfzr"><div ><div class="add_fir"><img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/user.png" />'+
                            			'<span class="add_distance" id="ksfzryjSing">'+flow["op_user_name"]+'</span><span class="add_distance add_agree">审批</span></div>'
                            			+'<div class="add_sen" id="ksfzryjDate">'+flow["op_time"].substring(0, 16).replace(/\-/g, '.')+'</div>'+
                            			'<div class="add_third" id="one"><span class="add_third2"  id="ksfzryj">'+flow["op_remark"]+'</span></div></div></li>');
                            }
                        }
                    }
                }
            });
            
          	//判断是否该当前人审核
            var pageStatus = '${param.pageStatus}';
            if(pageStatus=='check'){
                $.ajax({
                    url: "${pageContext.request.contextPath }/disciplineZdjrAction/wx/chekcCanProcess.do?serviceId=${param.id}" ,
                    type: "POST",
                    async: false,
                    success: function (data) {
                        if ( !data["success"] ) {
                            back();
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        back();
                    }
                });
            }
	
  });
  //返回
  function back() {
      var url = "${pageContext.request.contextPath }/app/discipline/wx/zdjr_wx_handle_list.jsp?skip=${param.skip}";
      location.href = url;
  }
  function shtg(){
	  if(${param.state eq '2'}){
	  _this = this;
	  var strhtml = '<div class="maskbou"></div>'
			+'<div class="add_name">'
			+'<label class="pu_colse">x</label>'
			+'<div class="title"><span>是否需要提交到纪检分管院领导审核？</span></div><div class="cont">'
			+ '<div class="int_mo" style="margin-left: 5px;margin-right: 5px;">' +
            '            <font size="3px"><b>说明：</b></font><br>选择“是”，提交到纪检分管院领导审核<br>\n' +
            '            选择“否”，审核流程结束。</div></div>'
			+'<div class="infobtn"><button class="btn_one " onclick="tj(false)" data-name="否">否</button><button class="btn_two mleft12" onclick="tj(true)" data-name="是">是</button></div></div>'
			$("body").append(strhtml);
	  _this.close1=function(num){
			$(".maskbou").remove();
          $('.add_name').remove();
		}
			
		$(".pu_colse").click(function(){
				_this.close1();
		});
	 
		
	  }else{
		  tj(false)
	  }
  }
	function tj(fgy){
		var jjgzyj=encodeURI($("#jjgzyj").val());
		var yld=encodeURI($("#yld").val());
		var bmyj=encodeURI($("#bmyj").val());
			    var url="${pageContext.request.contextPath }/disciplineZdjrAction/wx/zdjrTg.do?id=${param.id}&activity_id=${param.activity_id}&fgy="+fgy+"&jjgzyj="+jjgzyj+"&yld="+yld+"&bmyj="+bmyj;
			    if((${param.state=="2"} && !fgy)|| ${param.state=="3"}){//纪检监察审核并且不提交到分管院领导   或分管院领导审核
			   	 url="${pageContext.request.contextPath }/disciplineZdjrAction/wx/flowEnds.do?id=${param.id}&processId=${param.process_id}&type=1&jjgzyj="+jjgzyj+"&yld="+yld+"&bmyj="+bmyj;
			    }
			   
			    $.ajax({
			        url: url,
			        type: "post",
			        async: true,
			        success: function (data) {
			       	 if(data.success){
	                        jQnotice('操作成功!', 3000);
	                        back();
			       	 }
			        }
			    });
			
	}
	//不通过
	function shbtg(){//异常(强制)结束
		var jjgzyj=encodeURI($("#jjgzyj").val());
		var yld=encodeURI($("#yld").val());
		var bmyj=encodeURI($("#bmyj").val());
		$.ajax({
	        url:"${pageContext.request.contextPath }/disciplineZdjrAction/wx/flowEnds.do?id=${param.id}&processId=${param.process_id}&type=2&jjgzyj="+jjgzyj+"&yld="+yld+"&bmyj="+bmyj,
	        type: "post",
	        async: true,
	        success: function (data) {
	       	 if(data.success){
                    jQnotice('操作成功!', 3000);
                    back();
	       	 }
	        }
	    });
	}
</script>
</head>

<body >
<div class="settings" style="display:none;">
	<select name="demo" id="demo">
    	<option value="date">日期</option>
	</select>
</div>
<form id="formObj" name="formObj" method="post" action="" onsubmit="return false;" getAction="" pageStatus="">
			<input  type="hidden" name="id" value="${param.id }"/>
	<section id="web" class="holiday">
		<header id="header"> <img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/tb.png"> </header>
		<section id="content">
			<section class="department">
			<section class="top">
                <strong>编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</strong>
                <input type="text" onfocus="this.blur()" name="sn" id="sn"
                       placeholder="自动生成" readonly="readonly" style="border:0px;">
            	</section>
				<section class="top">
                <strong><b>*</b>所在部门：</strong>
                <input type="hidden" id="szbm_id" name="szbm_id" value="<%=user.getDepartment().getId()%>"/>
                <input type="text" name="szbm" id="szbm" value="<%=user.getDepartment().getOrgName()%>" class="task" readonly="readonly" style="border:0px;">
            	</section>
				<section class="top">
                <strong><b>*</b>监督类别：</strong>
                <select name="jdlb" id="jdlb"  class="task"  style="border: solid 0px #000;appearance:none;-moz-appearance:none;-webkit-appearance:none;">
						<option value="zdjc">重大决策</option>
						<option value="zygbrm">重要干部任免</option>
						<option value="zdxmap">重大项目安排</option>
						<option value="dedzjsy">大额度资金使用</option>
					</select>
        		</section>
					 <section class="top">
                <strong><b>*</b>监督方式：</strong>
                 <select name="jdfs" id="jdfs"  style="border: solid 0px #000;appearance:none;-moz-appearance:none;-webkit-appearance:none;"
                        >
						<option value="sqyf">事情预防</option>
						<option value="szjr">事中介入</option>
						<option value="shjr">事后介入</option>
						<option value="qccy">全程参与</option>
						<option value="qt">其它</option>
					</select>
            </section>
            <section id="top" class="top">
                <strong style="width:42%;"><b>*</b>监督工作事由事项：</strong>
               <textarea class="task" rows="2" readonly="readonly" style="width: 100%;height: 50px; border: 1px;" name="jdgzsy" id="jdgzsy"></textarea>
            </section>
			<section class="top">
			<strong style="width:35%;"><b>*</b>监督开始时间：</strong>
			<input type="text" id="jdsj_start" name="jdsj_start"  readonly="readonly" style="height:50px;border:none;outline:medium;" placeholder="时间格式为2018-01-01"  maxLength="18" >
			
			</section>
			<section class="top">
			<strong style="width:35%;"><b>*</b>监督结束时间：</strong>
			<input type="text" id="jdsj_end" name="jdsj_end"  readonly="readonly" style="height:50px;border:none;outline:medium;" placeholder="时间格式为2018-01-01"  maxLength="18" >
			
			</section>
            <section id="top" class="top">
                <strong>部门意见：</strong>
                <input type="text" name="bmyj" id="bmyj" <c:if test="${param.state ne '1' }"> readonly='readonly' </c:if>  class="task" style="border:0px; " class="task">
            </section>
			<section class="top">
                <strong style="width:38%;">纪检负责人意见：</strong>
                <input type="text" name="jjgzyj" id="jjgzyj"  style="border:0px;width: 60%;" <c:if test="${param.state ne '2' }"> readonly='readonly' </c:if>   class="task">
            </section>
                
            <section class="top" id="top">
                <strong style="width:38%;">纪检院领导意见：</strong><input type="text" name="yld" id="yld" <c:if test="${param.state ne '3' }"> readonly='readonly' </c:if> style="border:0px; width: 60%;" class="task">
            </section>
            </section>
	            <section class="department" id="splc_countent">
	            <section class="departmentCenter" style="padding-bottom: 2rem;margin-top: -1rem;">
	                <section class="add_bottom">
	                    <ul id="splc">
	                    </ul>
	                </section>
	            </section>
	        	</section>
			</section>
			</section>
		</section>
		
	</section>
	 
	<div id="load" align="center"><img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/loading3.gif" width="39" height="39" align="center" style="margin-top: 50%;"/></div>
	</form>
	<c:if test="${param.pageStatus ne 'detail'}">
    <section id="apply" class="Button3">
        <a href="javascript:" onclick="shtg()">同意</a>
        <a href="javascript:" onclick="shbtg()">不同意</a>
        <a href="javascript:" onclick="back()">返回</a>
    </section>
	</c:if>
	
	<c:if test="${param.pageStatus eq 'detail'}">
    <section id="apply" class="Button1">
        <a href="javascript:" onclick="back()">返回</a>
    </section>
	</c:if>
</body>
</html>

		