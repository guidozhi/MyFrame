﻿<%@page import="com.khnt.rbac.bean.Position"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head pageKeys="sysMain">
	<%@include file="/k/kui-base.jsp"%>
	<title>Loading...</title>

	<script type="text/javascript">

	
		//2017年12月15日 16:55:45 必须最前声明
		<%
		String _orgName = "";
		CurrentSessionUser user = SecurityUtil.getSecurityUser();
		String orgName = user.getUnit().getOrgName();
		if(!user.getUnit().getId().equals(user.getDepartment().getId()))
			orgName += ("-" + user.getDepartment().getOrgName());
		%>
		var _current_user_account = "<%=user.getUsername()%>";
		var userName = "<%=user.getName()%>";
		if(kFrameConfig.user.nickName){
			userName = kFrameConfig.user.nickName;
		}
		var loginUserName={org:"<%=orgName%>",name:userName};
		if(kui["SYS_POSITION_SET"]){
			<%
				Position position = user.getPosition();
				String posName = "";
				if(position!=null){
					posName = position.getPosName();
				}
				_orgName = "".equals(posName)?orgName:orgName+"-【"+posName+"】";
			%>
			loginUserName = {org:"<%=_orgName%>",name:userName};
		}
		var systemUserName="${pageContext.request.scheme}_${pageContext.request.serverName}_${pageContext.request.serverPort}${pageContext.request.contextPath}_"+(loginUserName["name"]);
	</script>

	<%--<link rel="stylesheet" href="app/bigdata/css/frame-main.css"/><!--大数据效果，主体css库-->--%>

	<script type="text/javascript">
	loadCoreLibrary("main");//载入框架核心
	</script>

	<%--<script type="text/javascript" src="app/bigdata/js/frame-main.js"></script><!--大数据效果，主体js库-->--%>

	<link rel="stylesheet" href="k/kui/skins/extflat/css/menu2.css"/><!--子菜单无图标效果-->

	<script type="text/javascript">
	function sysMainPageComplate() {
		//menuRelocation("m001","org_manage_division");
		// mBigData();//大数据效果 载入大数据显示效果 2016年10月25日 09:52:21 lybide
		/*var oneExe1=setTimeout(function(){//菜单一次性展开，此功能有问题。不推荐使用
			return;
			$("#mMenu2").find("> div > div .m-menu2-1-li-hasChildren-one").each(function(){
				var _this=$(this);
				_this.attr("isexpand","0");
			}).click();
		},11);*/
	}
	//每个二级菜单生成后，回调函数。对此菜单元素进行控制
	function mMenu2Create1Complate(thisMenuId,menuLength) {
		return;
		if (menuLength==1) {
			return;
		}
		var oneExe1=setTimeout(function(){//菜单一次性展开
			var menuObj=$("#mPanel_"+thisMenuId);//所有菜单
			var menuObj=$("#mPanel_"+thisMenuId+" [menuid=comp_bpm]");//某个菜单
			menuObj.find(".m-menu2-1-li-hasChildren-one").each(function(){
				var _this=$(this);
				_this.attr("isexpand","0");
			}).click();
		},10);
	}
	</script>

	</head>
	<body>
		<div id="sysMain">
			<div id="mTop" position="top" class="m-top">
				<div id="mTopDiv" class="m-top-div">
					<div id="systemTitle" class="m-top-logo">
						<div id="systemTitleText"></div>
					</div>
					<div id="mTopLeft" class="m-top-left"></div>
					<div id="mTopRight" class="m-top-right"></div>
				</div>
			</div>
			<div id="mSystemItem" class="m-system-item">
				<ul>
				</ul>
			</div>
			<div id="mUserInfo" class="m-user-info">
				<div id="mUserSetMenu" class="user-set-menu"></div>
				<div id="mUserInfoName" class="m-user-info-name">
					<div>loading</div>
				</div>
			</div>
			<div id="mFoldLeft" class="m-fold-left" title="收缩菜单工作区"><a><span>收缩/打开左菜单工作区</span></a></div>
			<div id="mFoldTop" class="m-fold-top" title="收缩顶部工作区"><a><span>收缩/打开主工作区</span></a></div>
			<div id="mFoldRight" class="m-fold-right" title="收缩右工作区"><a><span>收缩/打开右工作区</span></a></div>
			<div id="mFoldButtom" class="m-fold-buttom" title="收缩底工作区"><a><span>收缩/打开底工作区</span></a></div>
			<div id="mMenu1More" class="m-menu1-more"></div>
			<div id="mMenu1" class="m-menu1">
				<div class="m-menu1-div" id="mMenu1Div">
					<ul>
					</ul>
				</div>
			</div>
			<div id="mMenu2" position="left" class="m-menu2">
				<div class="m-menu2-1-div" id="mMenu2Div">

                </div>
			</div>
			<div id="framecenter" position="center" class="m-center"></div>
			<div id="mFoot" position="bottom" class="m-foot"></div>
		</div>

         <!--dvDockStart part-->
                <div id="dvDock" class="go" style="display:none">

                       <!--mI part-->
                       <div hidefocus="true" tabindex="0" title="快捷菜单" id="dvDockStartOuter" class="mI" >
                               <div id="dvDockStart" class="ps lc">
                                   <img src="k/kui/public/shortcut-menu/windows.png" alt="" style="height:30px"/>
                                   <span style="display:none" id="spnDockNew" class="qg"></span>
                               </div>
                       </div>
                       <!--mI part end-->
                       <!--oN part-->
                       <div id="dvDockListt" class="oN hg">
                       		<a hidefocus="hidefocus" class="nui-ico ld js-component-link " href="javascript:void(0)" id="_mail_link_95_488"></a>
                            <div class="menu_tr">
                            	<a hidefocus="hidefocus" class="kX nui-ico-option close" href="javascript:void(0)" id="_mail_link_3_118" title="设置"><b class=" nui-ico-option "></b></a>
                                <a hidefocus="hidefocus" class="kX nui-ico-option open" href="javascript:void(0)" id="_mail_link_4_120"><b class=" nui-ico-dockMax "></b></a>
                            </div>
                            <!--le part-->
                       		<div id="dvDockAppLsit" style="height: 287px;" class="le">
                              <div class="relative">
                                <div id="dvDockItems" style="height: 287px;" class="kl nui-fClear">
                                </div>
                                </div>

                            </div>
                            <!--le part end-->


                            <span class="bottom_boder"></span>
                            <span class="bottom_left"></span>


                       </div>
                       <!--oN part end-->
                       <!--dvFullList part-->
                       <div id="dvFullListt" class="oN kW">
                      		<a hidefocus="hidefocus" class="nui-ico ld js-component-link " href="javascript:void(0)" id="_mail_link_95_488"></a>
                            <div class="menu_tr">
                            	<a hidefocus="hidefocus" class="kX nui-ico-option closees" href="javascript:void(0)" id="_mail_link_3_118" title="设置"><b class=" nui-ico-option "></b></a>
                            	<a hidefocus="hidefocus" class="kX nui-ico-option edits" href="javascript:void(0)" id="_mail_link_3_118" title="编辑"><b class=" nui-ico-edit "></b></a>
                                <a hidefocus="hidefocus" class="kX nui-ico-option openes" href="javascript:void(0)" id="_mail_link_4_120"><b class=" nui-ico-dockMax "></b></a>
                            </div>
                             <!--le part-->
                       		<div id="dvDockAppLsit"  class="le">
                                <div id="dvDockItems" style="height: 250px;" class="kl nui-fClear">
                                </div>
                            </div>
                            <!--le part end-->
                            <span class="bottom_boder"></span>

                       </div>
                       <!--dvFullList part end-->
                </div>
       <!--dvDockStart part end-->
       <!--
       <div class="hxbtn" style="display:block; position:absolute; bottom:35px; right:65px; z-index:1000; cursor:pointer;">横向展开</div>
       <div class="zxbtn" style="display:block; position:absolute; bottom:35px; right:155px; z-index:1000; cursor:pointer;">纵向展开</div>
       <div class="zkbtn" style="display:block; position:absolute; bottom:35px; right:255px; z-index:1000; cursor:pointer;">始终最大化</div>
       -->
</body>
</html>

