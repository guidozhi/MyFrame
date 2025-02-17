<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://khnt.com/tags/qm" prefix="q" %>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no"/>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<link rel="stylesheet" href="app/k/km/lib/kh-mobile.css"/>
<script src="app/k/jqm/jquery2.js"></script>
<script src="app/k/km/lib/kh-mobile.js"></script>
<script src="app/k/km/lib/kh-mobile-list.js"></script>

<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<script type="text/javascript" src="${pageContext.request.contextPath}/app/k/km/lib/app-end.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/pub/bpm/js/util.js"></script>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no"/>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<link rel="stylesheet" href="app/k/jqm/skins/default.css"/>
<script src="app/k/jqm/a-main.js" type="text/javascript"></script>
<% String id=request.getParameter("id");
    if(id==null){
    	id=(String)session.getAttribute("id");
    }
%>
<%SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	String nowTime=""; 
	nowTime = dateFormat.format(new Date());
	String nowTime1=""; 
	nowTime1 = dateFormat.format(new Date().getTime()+24*60*60*1000);
	String nowTime2=""; 
	nowTime2 = dateFormat.format(new Date().getTime()+24*60*60*1000*2);
	%>	
<script type="text/javascript">
//Qm.config.pageid="qm_mb_01";

Qm.listClick=function(e,sThis){
	var selectedId = sThis.find('input[name="id"]').val();
	var status = sThis.find('input[name="status"]').val();
	var dj_peoppleid = sThis.find('input[name="dj_peoppleid"]').val();
	location.href="${pageContext.request.contextPath}/app/weixin/hys/meeting_detail.jsp?id="+selectedId+"&status="+status+"&dj_peoppleid="+dj_peoppleid+"&eId="+'<%=id%>';
};

</script>

<script type="text/javascript">  
	var Sys = {};  
	    var ua =navigator.userAgent.toLowerCase();  
	    var s;  
	    (s = ua.match(/msie([\d.]+)/)) ? Sys.ie = s[1] :  
	    (s =ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :  
	    (s =ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :  
	    (s =ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :  
	    (s =ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] :0;  
	   
	if (Sys.opera || Sys.safari){  
		window.setInterval("reinitIframe()", 200);  
	}  
	function reinitIframe() //针对opera safari  
	{  
		var iframe = document.getElementById("PandL");  
		try{  
			var bHeight =iframe.contentWindow.document.body.scrollHeight;  
			var dHeight =iframe.contentWindow.document.documentElement.scrollHeight;  
			var height = Math.max(bHeight, dHeight);  
			iframe.height =  height;  
		}catch (ex){}  
	}  
	   
	function iframeAutoFit(iframeObj)  
	{   
	setTimeout(function(){  
		if(!iframeObj)   
		return;  
		iframeObj.height=(iframeObj.Document?iframeObj.Document.body.scrollHeight:iframeObj.contentDocument.body.offsetHeight)+10;//这里+10是有目的的，比如ie下会少那么一些像素  
		},200)  
	}  
</script>

<style>
.wrapper{ margin-top:28%;}
</style>
</head>
<body>
<div  id="a-index" class="a-index">
	<div class="header">
		<div class="bg"></div>
		<div class="logo"></div>
		<div class="user">
			<h1>四川省特检院</h1>
			<h2>办公助手-会议申请列表</h2>
		</div>
	</div>
	</div>
<div id="wrapper" class="wrapper">
	<section id="reg1" class="">
		<div class="container">

			<div class="page-panel" id="m-page-panel">
				
			<fieldset class="l-fieldset">
			<legend class="l-legend" style = "font-size:12px;">
					<div>今日会议（<%=nowTime%>）</div>
					<div id = "today">
					<%-- <div class="km-list">
						<ul id="__qm_list" class="qm-list">
	
						</ul>
					</div> --%>
					<iframe id="iframe1" frameborder="0" scrolling="no" src="app/weixin/hys/meeting_today_list.jsp" onLoad="javascript:iframeAutoFit(this);"></iframe>
					</div>
			</legend>
			</fieldset>
			<fieldset class="l-fieldset">
			<legend class="l-legend" style = "font-size:12px;">
					<div style="color:blue">明日会议（<%=nowTime1%>）</div>
					<div id = "tomorrow">
					<%-- <div class="km-list">
						<ul id="__qm_list1" class="qm-list">
	
						</ul>
					</div> --%>
					<iframe id="iframe2" frameborder="0" scrolling="no" src="app/weixin/hys/meeting_tomorrow_list.jsp" onLoad="javascript:iframeAutoFit(this);"></iframe>
					</div>
			</legend>
			</fieldset>
			<fieldset class="l-fieldset">
			<legend class="l-legend" style = "font-size:12px;">
					<div style="color:orange">后天会议（<%=nowTime2%>）</div>
					<div id = "aftertomorrow">
					<%-- <div class="km-list">
						<ul id="__qm_list2" class="qm-list">
	
						</ul>
					</div> --%>	
					<iframe id="iframe3" frameborder="0" scrolling="no" src="app/weixin/hys/meeting_aftertomorrow_list.jsp" onLoad="javascript:iframeAutoFit(this);"></iframe>
					</div>
			</legend>
			</fieldset>
			</div>
		</div>
	</section>
</div>
<%-- <q:km pageid="TJY2_HYS_LIST"></q:km> --%>
</body>
</html>
<script type="text/javascript" src="app/k/km/lib/app-end.js"></script>
