﻿<%@page import="com.khnt.certificate.util.CertificateUtil"%>
<%@page import="java.security.cert.X509Certificate"%>
<%@page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%
	boolean isCertError = false;
	Object userName = "";
	if (request.isSecure()){
		X509Certificate cert = CertificateUtil.extractClientCertificate(request);
		try {
			userName = CertificateUtil.extractPrincipal(cert,"CN=(.*?),");
		} catch (Exception e) {
			isCertError = true;
		}
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageKeys="login">
<%@include file="/k/kui-base.jsp"%>
<title>Loading...</title>
<meta HTTP-EQUIV="Page-Exit" CONTENT="blendTrans(Duration=0.5)">
<script type="text/javascript" src="app/js/jQuery.md5.js"></script>
<script type="text/javascript">
	loadCoreLibrary("login");
	var LOGIN_CERT = {
		uname : "<%=userName%>",//用户账号 admin
		isCertErr : <%=isCertError%>//是否证书读取
	};
	
	$(function() {
		if("${param.userName}"!=""){
			$("#TxtUserName").val("${param.userName}");
		}
		if("${param.password}"!=""){
			$("#TxtPassword").val("${param.password}");
		}
		 if($("#TxtUserName").val()!=""&&$("#TxtPassword").val()!=""){
			$("#lFormSubmit1").click();
		} 
		$("#userButtonPanel").css("width","380px")
		$(".k-login-inputbtn").css("margin-right",'1px')
		 
		/* $('#lFormSubmit1').click(function(){
			
		}) */
	})
	function loginByKey (){
		var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		if (keyCode==13) {
			login1();
		}
	}
	function loginByKeyUserName(){
		var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		if (keyCode==13) {
			var pass = $("#TxtPassword").val();
			if(pass==""){
				$('#TxtPassword').focus();
			} else {
				login1();
			}
		}
	}
	 function login1(type){
			 var name = $("#TxtUserName").val();
				var pass = $("#TxtPassword").val();
				
				if(name==""){
					alert("请输入用户名！")
					return;
				}
				if(pass==""){
					alert("请输入密码！")
					return;
				}
				var html1=$("#userCodePanel .k-login-inputbtn").html();
				var html2=$("#userButtonPanel").html();
				$("#userButtonPanel").html('<div class="k-login-submit-msg" id="k-login-submit-msg"><div>正在登录中……</div></div>');
				//if(name=="admin"){
					/* $.post("j_spring_security_check", {
						j_password : pass,
						j_username : name
					}, function(data) {
						if (data["success"]) {
							//alert(window.location.host)
							//window.location="http://"+window.location.host+"/k/main.jsp";
							positionLogin(data,type);
						} else {
							alert("用户名或密码错误！");
							$("#userCodePanel .k-login-inputbtn").html(html1);
							$("#userButtonPanel").html(html2);
						}
					}) */
					if(type==2){
						$.post("j_spring_security_check", {
							j_password : pass,
							j_username : name
						}, function(data) {
							if (data["success"]) {
								//alert(window.location.host)
								//window.location="http://"+window.location.host+"/k/main.jsp";
								positionLogin(data,type);
							} else {
								alert("用户名或密码错误！");
								$("#userCodePanel .k-login-inputbtn").html(html1);
								$("#userButtonPanel").html(html2);
							}
						}) 
					}else{
						window.location.href = "http://192.168.1.50:8081/user/login.do?userName="+name+"&password="+pass;
					}
					
					
					
					
					
				/*}else{
				    var q = $.md5(pass);
				    $.post("employee/basic/checkPass.do", {
						password : q,
						name : name
					}, function(data) {
						if (data["success"]) {
							var userName = data.userName;
							$.post("j_spring_security_check", {
								j_password : $.md5(pass),
								j_username : userName
							}, function(data) {
								if (data["success"]) {
									positionLogin(data);
								} else {
									alert("用户名或密码错误！");
									$("#userCodePanel .k-login-inputbtn").html(html1);
									$("#userButtonPanel").html(html2);
								}
							})
						} else {
							alert("用户名或密码错误！");
							$("#userCodePanel .k-login-inputbtn").html(html1);
							$("#userButtonPanel").html(html2); 
						}
					})
				}*/
		 }

	function positionLogin(respData,type){
		$.getJSON("rbac/user/getUserPosition.do",{userId:respData.data.userId},function(pos){
			
			if(pos.success&&pos.data&&pos.data.length>0){
				if(pos.data.length>1){
					top.$.dialog({
						width: 300,
						height: 130,
						title: "选择岗位",
						reSize: false,
						max: false,
						min: false,
						lock: true,
						content: "url:pub/rbac/position_select.jsp",
						data : {"data" : pos.data},
						cancel:true,
						close:function(){
							location = kFrameConfig["base"]+"security/authentication/logout.do";
							return false;
						},
						ok:function(){
							var data = this.iframe.contentWindow.getPostionId();
							if(data==""||data==undefined||data==null){
								$.dialog.alert("请选择要登录的岗位!");
								return false;
							}else{
								$.getJSON("rbac/position/positionLogin.do",{positionId:data},function(res){
									if(res.success){
										
										location = kFrameConfig["base"]+kFrameConfig["loginOkUrl"];
									
										return false;
									}else{
										$.dialog.alert("登录失败!");
										location = kFrameConfig["base"]+"security/authentication/logout.do";
										
										return false;
									}
								})
							}
							return false;
						},okVal:'确定'
					});
				}else{
					
					$.getJSON("rbac/position/positionLogin.do",{positionId:pos.data[0].id},function(res){
						if(res.success){
							location =kFrameConfig["base"]+kFrameConfig["loginOkUrl"];
							return false;
						}else{
							$.dialog.alert("登录失败!");
							location = kFrameConfig["base"]+"security/authentication/logout.do";
							return false;
						}
					})
				}
			}
			else{
				//没有岗位直接登陆哇
				location = kFrameConfig["base"]+kFrameConfig["loginOkUrl"];
			}
		})
	}
	
</script>
</head>
<body>
	<div class="k-login-layer">
		<div class="k-login-layer-panel">
			<div class="k-login-top">
				<div class="k-login-top-logo">
					<div class="k-login-top-logo-btnbox"></div>
				</div>
			</div>
			<div class="k-login-middle">
				<div class="k-login-layout">
					<div class="k-login-layout-content">
						<div class="k-login-custom_img">
							<div class="k-login-custom_mask"></div>
						</div> 
						<div class="k-login-btnbox">
							<ul></ul>
						</div>
						<div class="k-login-box" id="k-login-box">
							<form name="loginForm" id="loginForm" action="j_spring_security_check" method="post"
								onsubmit="return sysLoginDoSubmit(this);">
								<div class="k-login-box-title" id="sysNamePanel">
									<div>
										<span></span>
									</div>
								</div>
								<div class="k-login-inputbox" id="userNamePanel">
									<div class="k-login-inputname">
										<div>
											<span>用户名</span>
										</div>
									</div>
									<div class="k-login-inputleft">
										<input class="TxtUserNameCssClass" id="TxtUserName" type="text" value="" onkeyup="loginByKeyUserName()" maxLength="20"
											name="j_username">
									</div>
									<div class="k-login-inputright">
										<div>
											<span>*请输入用户名</span>
										</div>
									</div>
								</div>
								<div class="k-login-inputbox" id="userPassPanel">
									<div class="k-login-inputname">
										<div>
											<span>密　码</span>
										</div>
									</div>
									<div class="k-login-inputleft">
										<input class="TxtPasswordCssClass" id="TxtPassword" type="password" value="" onkeyup="loginByKey()" name="j_password">
									</div>
									<div class="k-login-inputright">
										<div>
											<span>*请输入密码</span>
										</div>
									</div>
								</div>
								<div class="k-login-inputbox" id="userCodePanel">
									<div class="k-login-inputname">
										<div>
											<span>验证码</span>
										</div>
									</div>
									<div class="k-login-inputyz">
										<input name="j_validate_code" type="text" id="validateCode" />
									</div>
									<div class="k-login-inputyzimg" id="k-login-inputyzimg"></div>
								</div>
								<div class="k-login-inputbox-only" id="userButtonPanel" style="width: 400px;">
									<div class="k-login-inputbtn bsubmit-plane" id="bsubmit-plane">
										<input type="button" value="登录" class="button" id="lFormSubmit2" onclick="login1(2)"/>
									</div>
									<div class="k-login-inputbtn bsubmit-plane" id="bsubmit-plane">
										<input type="button" value="登录检验" class="button" id="lFormSubmit1" onclick="login1()"/>
									</div>
									
									<div class="k-login-inputbtn breset-plane" id="breset-plane">
										<input type="reset" value="清 空" class="reset" id="lFormReset"/>
									</div>
								</div>
							</form>
						</div>
						
						
						<div class="k-login-style"></div>
					</div>
				</div>
			</div>
			<div class="k-login-foot">
				<div class="k-login-foot-copy" id="loginCopy">
					<div>
						<span></span>
					</div>
				</div>
				<div class="k-login-foot-tech" id="loginTech">
					<div>
						<span></span>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>