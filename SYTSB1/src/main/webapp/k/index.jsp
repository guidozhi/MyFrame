﻿<%@page import="com.khnt.certificate.util.CertificateUtil"%>
<%@page import="java.security.cert.X509Certificate"%>
<%@page contentType="text/html;charset=UTF-8"
	trimDirectiveWhitespaces="true"%>
<%
	boolean isCertError = false;
	Object userName = "";
	if (request.isSecure()) {
		X509Certificate cert = CertificateUtil.extractClientCertificate(request);
		try {
			userName = CertificateUtil.extractPrincipal(cert, "CN=(.*?),");
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

		kFrameConfig["j_login_strategy_qrcode"]=false;
		loadCoreLibrary("login");
		var LOGIN_CERT = {
			uname : "<%=userName%>",//用户账号 admin
		isCertErr : <%=isCertError%>
	//是否证书读取
	};
</script>
<script type="text/javascript" src="k/kui/frame/login-frame.js"></script>
<script type="text/javascript">
	$(function() {

		kFrameConfig.loginLogo["src"] = "app/public/13/logo.png";
		//系统名称
		$("#loginCopy span").html(kFrameConfig["copyCom"]);//版权所有
		$("#loginTech span").html(kFrameConfig["techCom"]);//技术支持
		$("#TxtUserName,#TxtPassword,#validateCode").val("");

		// $.Move($('#login-box2'));

		//开启验证码
		// kFrameConfig["loginYZM"]=true;
		if (kFrameConfig["loginYZM"]) {
			var yzm = $('<img src="security/web/validateCodeImg.do?isNew=true" />');
			yzm.click(function() {
				var $this = $(this);
				$this.attr("src", "security/web/validateCodeImg.do?isNew=true"
						+ "&r=" + Math.random());
			})
			$("#userCodePanel").show();
			$("#k-login-inputyzimg").html(yzm);
			$("#userButtonPanel").hide();//登录，取消等，显示
		} else {
			$("#userCodePanel").hide().html("");//验证码
			$("#useryzm-plane").hide();
			$("#login-box,#login-box2").addClass("no-yzm")
		}

		//安全证书登录
		//kFrameConfig["loginCert"].isCert=true;
		if (kFrameConfig["loginCert"].isCert) {
			var cretUName = typeof LOGIN_CERT == "undefined" ? ""
					: LOGIN_CERT["uname"];
			cretUName = cretUName || "没有读出用户名";//2015/1/5 10:58 lybide
			var dm1 = [ "早上好，", "上午好，", "中午好，", "下午好，", "晚上好，", "" ];//2015/2/11 15:15 lybide
			var ca1, cc1 = new Date().getHours();
			if (cc1 >= 19 && cc1 <= 24) {
				ca1 = 4;
			} else if (cc1 >= 5 && cc1 <= 8) {
				ca1 = 0;
			} else if (cc1 >= 9 && cc1 <= 11) {
				ca1 = 1;
			} else if (cc1 >= 12 && cc1 <= 13) {
				ca1 = 2;
			} else if (cc1 >= 14 && cc1 <= 18) {
				ca1 = 3;
			} else {
				ca1 = 5;
			}
			;
			ca1 = 5;
			var isCertStr1 = '<div class="l-userName-zs"><div>' + (dm1[ca1])
					+ "" + cretUName + "" + '</div></div>';
			//isCertStr1+='<input id="TxtUserName" type="hidden" value="admin" name="j_username"title="请输入您的用户名"/>';
			$("#TxtUserName").remove();
			$("#username-plane").append(isCertStr1);
			$("#TxtUserName").attr("readonly", true);
			$(".k-login-inputname").hide();
			$("body").addClass("is-cert");
		}

		//打印标题
		$("#").append(
				'<div class="k-login-top-logo-text" id="sysNameText"><div><a><span>'
						+ kFrameConfig["name"] + '</span></a></div></div>');
		if (kFrameConfig["logoMode"] == "img") {
			$("#k-login-top-logo").css(
					"background",
					"url('" + kFrameConfig.loginLogo["src"]
							+ "') no-repeat center center");
			if (kFrameConfig.loginLogo["style"]) {
				$("#k-login-top-logo").css(this.loginLogo["style"]);
			}
		} else {
			$("#k-login-top-logo").hide();
			$("#sysNameText").show();
		}

		// for (var i = 0, l=imageJson.length; i < l; i++) {
		// 	var item=imageJson[i];
		// 	var str1='<div class="i-main-datu" style="background-image:url('+item["img"]+');"></div>';
		// 	if (BROWSER_INFO.ie && BROWSER_INFO.ieversion<=8) {
		// 		str1='<div class="i-main-datu"><img src="'+item["img"]+'" alt=""/></div>';
		// 	}
		// 	$("#loop1").append(str1);
		// };
		if (BROWSER_INFO.ie && BROWSER_INFO.ieversion <= 8) {
			$("#loginForm .input")
					.each(
							function(i) {
								var _this = $(this);
								var id = _this.attr("id");
								var placeholder = _this.attr("placeholder");
								var divObj = $('<div class="l-u-msg s-' + id + '-msg" id="' + id + '-msg">'
										+ placeholder + '</div>');
								divObj.click(function() {
									_this.focus();
									divObj.hide();
								});
								_this.after(divObj);
							}).focus(function() {
						var _this = $(this);
						var id = _this.attr("id");
						var placeholder = _this.attr("placeholder");
						var divObj = $("#" + id + "-msg");
						if (_this.val() == "" || _this.val() == placeholder) {
							divObj.hide();
						}

					}).blur(function() {
						var _this = $(this);
						var id = _this.attr("id");
						var placeholder = _this.attr("placeholder");
						var divObj = $("#" + id + "-msg");
						if (_this.val() == "") {
							divObj.show();
						}
					});
			$("#bsubmit-plane input").click(function() {
				$("#loginForm .l-u-msg").show();
			});
		}

		if ("${param.userName}" != "") {
			$("#TxtUserName").val("${param.userName}");
		}
		if ("${param.password}" != "") {
			$("#TxtPassword").val("${param.password}");
		}
		if ($("#TxtUserName").val() != "" && $("#TxtPassword").val() != "") {
			$("#lFormSubmit2").click();
		}

	});

	function loginByKey() {
		var keyCode = event.keyCode ? event.keyCode : event.which ? event.which
				: event.charCode;
		if (keyCode == 13) {
			login();
		}
	}
	function loginByKeyUserName() {
		var keyCode = event.keyCode ? event.keyCode : event.which ? event.which
				: event.charCode;
		if (keyCode == 13) {
			var pass = $("#TxtPassword").val();
			if (pass == "") {
				$('#TxtPassword').focus();
			} else {
				login();
			}
		}
	}
	function login(type) {
		var name = $("#TxtUserName").val();
		var pass = $("#TxtPassword").val();
		if (name == "") {
			alert("请输入用户名！")
			return;
		}
		if (pass == "") {
			alert("请输入密码！")
			return;
		}
		var html1 = $("#userCodePanel .k-login-inputbtn").html();
		var html2 = $("#userButtonPanel").html();
		$("#userButtonPanel").html('<div class="k-login-submit-msg" id="k-login-submit-msg"><div>正在登录中……</div></div>');
			type = 2;
			if(type==2){
				var q = $.md5(pass);
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
						$("#userButtonPanel"+type).html(html2);
					}
				}) 
			}else{
				//window.location = "http://192.168.1.50:8081/user/login.do?"+encodeURI("userName="+name+"&password="+pass);
				//刷新登录页面
				var href = "http://localhost:8180/k/index.jsp?"+encodeURI("userName="+name+"&password="+pass);
				
				window.location = href;
			} 
	}

	function positionLogin(respData, type) {
		$.getJSON(
				"rbac/user/getUserPosition.do",
				{
					userId : respData.data.userId
				},
						function(pos) {
							if (pos.success && pos.data && pos.data.length > 0) {
								if (pos.data.length > 1) {
									top.$.dialog({
												width : 300,
												height : 130,
												title : "选择岗位",
												reSize : false,
												max : false,
												min : false,
												lock : true,
												content : "url:pub/rbac/position_select.jsp",
												data : {
													"data" : pos.data
												},
												cancel : true,
												close : function() {
													location = kFrameConfig["base"]
															+ "security/authentication/logout.do";
													return false;
												},
												ok : function() {
													var data = this.iframe.contentWindow
															.getPostionId();
													if (data == ""
															|| data == undefined
															|| data == null) {
														$.dialog
																.alert("请选择要登录的岗位!");
														return false;
													} else {
														$.getJSON(
																"rbac/position/positionLogin.do",
																{
																	positionId : data
																},
																function(
																		res) {
																	if (res.success) {

																		location = kFrameConfig["base"]
																				+ kFrameConfig["loginOkUrl"];

																		return false;
																	} else {
																		$.dialog.alert("登录失败!");
																		location = kFrameConfig["base"]
																				+ "security/authentication/logout.do";

																		return false;
																	}
																})
													}
													return false;
												},
												okVal : '确定'
											});
								} else {

									$.getJSON(
											"rbac/position/positionLogin.do",
											{
												positionId : pos.data[0].id
											},
											function(res) {
												if (res.success) {
													location = kFrameConfig["base"]
															+ kFrameConfig["loginOkUrl"];
													return false;
												} else {
													$.dialog
															.alert("登录失败!");
													location = kFrameConfig["base"]
															+ "security/authentication/logout.do";
													return false;
												}
											})
								    }
							} else {
								//没有岗位直接登陆哇
								location = kFrameConfig["base"]+ kFrameConfig["loginOkUrl"];
							}
						})
	}
</script>
<link rel="stylesheet" type="text/css" href="app/public/13/k-login.css" media="all" />
<style type="text/css">
.k-login-middle {
	background: url('app/public/13/images/login_tsbg.jpg') no-repeat top center;
}

.k-login-layout-content {
	background: none;
}

#userCodePanel, #userButtonPanel {
	display: block;
}

.k-login-box {
	display: block;
	background: none;
	box-shadow: none;
	top: 18px;
}

.leftbox {
	left: -20px;
}

.rightbox {
	right: 110px;
}

.k-login-box-title {
	display: block;
	font-size: 26px;
}

#userCodePanel {
	display: none;
}

.tit01 {
	width: 430px;
	text-align: right;
}

.tit02 {
	color: #3790e0;
}

.lginfo {
	font-size: 18px;
	padding-left: 40px;
	line-height: 50px;
	margin-bottom: 10px;
}

.k-login-inputname {
	display: none;
}

.k-login-inputbox-only {
	margin-left: 30px;
}

.ftc1 {
	color: #333;
}

.k-login-inputbox-only {
	width: 290px;
	margin-left: 30px;
}

.k-login-inputbtn {
	width: 130px;
	padding-left: 0;
	margin-right: 27px;
}

.breset-plane {
	margin-right: 0;
}

.k-login-inputbtn input {
	width: 130px;
}
/* .k-login-inputbtn input { background: url(app/public/13/login.png) no-repeat center right; color: #333; } */
.klogin2 input {
	background: url(app/public/13/login-2.png) no-repeat center right;
	color: #333;
}

.klogin2:hover input {
	color: #fff;
}
</style>

</head>
<body>
	<div class="k-login-layer">
		<div class="k-login-layer-panel">
			<div class="k-login-top">
				<div class="k-login-top-logo" id="k-login-top-logo">
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
						<div>
							<!-- 综合管理 -->
							<div class="k-login-box leftbox" id="k-login-box">

								<form name="loginForm1" id="loginForm1"
									action="j_spring_security_check" method="post"
									onsubmit="return sysLoginDoSubmit(this);">

									<div class="k-login-box-title tit01" id="sysNamePanel">
										<div>
											<span>综合管理</span>
										</div>
									</div>
									<div class="lginfo ftc1" style="margin-top: 50px">
										<!-- 非检验临时登录 -->
									</div>
									<div class="k-login-inputbox" id="userNamePanel">
										<div class="k-login-inputname">
											<div>
												<span>用户名</span>
											</div>
										</div>
										<div class="k-login-inputleft">
											<input class="TxtUserNameCssClass" id="TxtUserName"
												type="text" value="" maxLength="20" name="j_username">
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
												<span>密 码</span>
											</div>
										</div>
										<div class="k-login-inputleft">
											<input class="TxtPasswordCssClass" id="TxtPassword"
												type="password" value="" name="j_password">
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
									<div class="k-login-inputbox-only" id="userButtonPanel">
										<div class="k-login-inputbtn bsubmit-plane"
											id="bsubmit-plane2">
											<input type="submit" value="登 录" class="submit"
												id="lFormSubmit2" onclick="login()" />
										</div>
										<div class="k-login-inputbtn breset-plane" id="breset-plane2">
											<input type="reset" value="清 空" class="reset"
												id="lFormReset1" />
										</div>
									</div>

								</form>
							</div>
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
