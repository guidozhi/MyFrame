<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@ page contentType="text/html;charset=UTF-8" %>
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
var flowId="";
var flowName="";
  $(function(){
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
            
            
            $.getJSON("${pageContext.request.contextPath }/bpm/serviceConfig/getFlowServiceConfig.do", {
				serviceCode : "ZDJC_ZDJR_FLOW",
				orgId : ""
			}, function(resp) {
				if (resp.success) {
					if (resp.data.length > 0){
						flowId = resp.data[0].flowId;
						flowName = resp.data[0].flowName;
					}
				}
			});
	
  });
  function submitForm(){
	  if($("#jdgzsy").val()=="" || $("#jdsj").val()==""){
		  alert("亲，带*号的是必填项，不能为空！");
			return false;
	  }

		var jdsj_start=$("#jdsj_start").val();
		var jdsj_end=$("#jdsj_end").val();
		var start = new Date(jdsj_start.replace("-", "/").replace("-", "/"));
		var end = new Date(jdsj_end.replace("-", "/").replace("-", "/"));
		if(end<start){  
			alert("监督开始时间不能大于结束时间！");
			return false;  
		}
		
		
	  var formDatas = $("#formObj").getValues();
	  $.ajax({
          url: "${pageContext.request.contextPath }/disciplineZdjrAction/wx/saveAndSubmit1.do?flowId="+flowId,
          data: JSON.stringify(formDatas),
          dataType: 'json',
          contentType: 'application/json; charset=utf-8',
          type: "post",
          async: true,
          success: function (data) {
              $('#load').hide();
              if ( data.success ) {
                  jQnotice('操作成功!', 3000);
              } else {
                  alert(data.msg);
              }
          },error: function (XMLHttpRequest, textStatus, errorThrown) {
              $('#load').hide();
              alert("提交失败，请联系管理员处理");
          }
      });
	  
  }
  function setleaveCount1(){}
  //返回
  function back() {
      var url = "${pageContext.request.contextPath }/app/discipline/wx/zdjr_wx_handle_list.jsp?skip=${param.skip}";
      location.href = url;
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
<!-- 	<input id="depName" name="depName" type="hidden"> -->
<!-- 	<input id="peopleName" name="peopleName" type="hidden"> -->
	<section id="web" class="holiday">
		<header id="header"> <img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/tb.png"> </header>
		<section id="content">
			<section class="department">
				<section class="top">
                <strong>编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</strong>
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
                 <select name="jdfs" id="jdfs" style="border: solid 0px #000;appearance:none;-moz-appearance:none;-webkit-appearance:none;"
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
               <textarea class="task" rows="2" style="width: 100%;height: 50px; border: 1px" name="jdgzsy" id="jdgzsy"></textarea>
<!-- 			<input type="text" name="jdgzsy" id="jdgzsy" class="task" style="border:0px; width:60%" class="task"> -->
            </section>
			<section class="top"><strong style="width:35%;"> <b>*</b>监督开始时间：</strong>
						<input type="text" id="jdsj_start" name="jdsj_start" class="form-control demo-test-date demo-test-datetime demo-test-time demo-test-credit" style="height:60px;border:none;outline:medium;" placeholder="时间格式为2019-01-01" ext_name="" ext_isNull="1" maxLength="18" >
			</section>
			<section class="top"><strong style="width:35%;"><b>*</b>监督结束时间：</strong>
						<input type="text" id="jdsj_end" name="jdsj_end" class="form-control demo-test-date demo-test-datetime demo-test-time demo-test-credit" style="height:60px;border:none;outline:medium;" placeholder="时间格式为2019-01-01" ext_name="" ext_isNull="1" maxLength="18" >
			</section>
            <section id="top" class="top">
                <strong>部门意见：</strong>
                <input type="text" name="bmyj" id="bmyj" class="task" readonly="readonly" style="border:0px; " class="task">
            </section>
			<section class="top">
                <strong style="width:38%;">纪检负责人意见：</strong>
                <input type="text" name="jjgzyj" id="jjgzyj"  style="border:0px;width: 60%;"  readonly="readonly" class="task">
            </section>
                
            <section class="top" id="top">
                <strong style="width:38%;">纪检院领导意见：</strong><input type="text" name="yld" id="yld" readonly="readonly"  style="border:0px; width: 60%;" class="task">
            </section>
            </section>
					
					
				</section>
			</section>
		</section>
		
	</section>
	<div id="load" align="center"><img src="${pageContext.request.contextPath}/app/humanresources/wx_leave/03/images/loading3.gif" width="39" height="39" align="center" style="margin-top: 50%;"/></div>
	</form>
	<section id="apply" class="Button2">
    <a href="javascript:" onclick="submitForm()">提交</a>
    <a href="javascript:" onclick="back()">返回</a>
</section>
</body>
</html>

		