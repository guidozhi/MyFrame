<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<%@include file="/k/kui-base.jsp" %>
	<title></title>
	<script type="text/javascript">
		loadCoreLibrary("withoutForm");
	</script>
	<style type="text/css" media="screen" id="style1">
		html, body { overflow:hidden; width:100%; height:100%; }
		.s-b-msg-page-main { width:100%; height:100%; }
		.s-b-msg-title { height:30px; }
	</style>
	<script type="text/javascript">
	document.write('<link rel="stylesheet" type="text/css" href="'+kui["kuiBase"]+'kui/skins/'+(L_MAIN_SKIN_NAME || kui["frameStyle"])+'/shell/main-frame.css" id="mainCSS"/>');
	var IS_SHELL_PAGE_WINDOW=true;
	</script>
	<script type="text/javascript">
		$(function () {//jQuery页面载入事件
			//IS_SHELL_PAGE_WINDOW="isShellPageWindow";
			//$("#s-b-msg-title1").html(kui["name"]);
			//$.dialog.loading();
			//$("#isShellPageWindow").css({"width":$(window).width(),"height":$(window).height()-$("#s-b-msg-title").height()});
			var oneExe1=setTimeout(function(){
				var url="${param.url}" || "demo/demo_detail7.jsp";
				//var url="demo/detail1.jsp";
				/*if (url.indexOf("?")>0) {
					url=url+"&isShellWindow=1";
				} else {
					url=url+"?isShellWindow=1";
				}*/
				//$("#isShellPageWindow").attr("src",url);
				$.dialog({
					title: kui["name"],
					content: "url:" + url,
					close: function(){
					    window.close();
					},
					//width:"100%",height:"100%",
					lock:false,
					resize:false,
					esc:false,
					drag:false,
					max:false,
					min:false,
					skin:"win-flat close4 title-is-show"
				}).max();
				//$(".s-b-page-win .ui_border").addClass("border-1");
			},10);

			/*$("#isShellPageWindow").bind("load",function(){//页面载入完成监听 2014/11/19 15:32 lybide
				$.dialog.loadingClose();
			});*/
			/*$("#s-b-msg-title-close").click(function () {
				//2014/11/14 11:16 lybide 关闭消息提示
				//is_window_externall_msgUserClose=1;
				//var ret=window.external.WriteIni("user", "msgBoxUserClose", "1", "confing.user.ini");
				//var ret=window.external.StopTrayFlicker();
				window.close();
			});*/
		});
	</script>
</head>
<body>
<%--<div id="s-b-msg-page-main" class="s-b-msg-page-main isshell" style="">
	<div id="s-b-msg-page-wrap" class="s-b-msg-page-wrap">
		<div id="s-b-msg-title" class="s-b-msg-title">
			<div class="title" id="s-b-msg-title1"></div>
			<div class="close" id="s-b-msg-title-close"><a href="#khsoft"><span>x</span></a></div>
		</div>
		<div id="s-b-msg-main" class="s-b-msg-main">
			<iframe id="isShellPageWindow" name="isShellPageWindow" src="" width="100%" height="100%" scrolling="no"
					frameborder="0"	border="0" style=""></iframe>
		</div>
	</div>
</div>--%>

</body>
</html>
