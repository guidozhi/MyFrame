﻿<%@page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageKeys="login">
<%@include file="/k/kui-base.jsp"%>
<title>Loading...</title>
<meta HTTP-EQUIV="Page-Exit" CONTENT="blendTrans(Duration=0.5)">
<script type="text/javascript">
	loadCoreLibrary("login");
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
										<input class="TxtUserNameCssClass" id="TxtUserName" type="text" value="" name="j_username" maxlength="255">
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
										<input class="TxtPasswordCssClass" id="TxtPassword" type="password" value="" name="j_password" maxlength="255">
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
										<input name="j_validate_code" type="text" id="validateCode" maxlength="9"/>
									</div>
									<div class="k-login-inputyzimg" id="k-login-inputyzimg"></div>
								</div>
								<div class="k-login-inputbox-only" id="userButtonPanel">
									<div class="k-login-inputbtn bsubmit-plane" id="bsubmit-plane">
										<input type="submit" value="登 录" class="submit" id="lFormSubmit"/>
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
