<%@page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="util.TS_Util"%>
<%@page import="com.lsts.webservice.cxf.server.QueryDataService"%>
<%@page import="util.ReportUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.lsts.inspection.dao.InspectionDao"%>
<%@page import="java.util.List"%>
<%@page import="com.lsts.inspection.bean.FlowInfoDTO"%>
<%@page import="com.khnt.utils.StringUtil" %>
<%@page import="com.lsts.employee.bean.EmployeeDTO"%>
<%@page import="com.lsts.employee.dao.EmployeeCertDao"%>
<%@page import="com.lsts.relevant.dao.RelevantPeopleCertDao"%>
<%@page import="com.lsts.relevant.bean.RelevantPeopleDTO"%>
<%@page import="java.util.Date"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="com.scts.payment.dao.InspectionPayInfoDao"%>
<%@page import="com.scts.payment.bean.InspectionInfoDTO"%>
<%@page import="com.scts.maintenance.dao.MaintenanceInfoDao"%>
<%@page import="com.scts.maintenance.bean.MaintenanceInfoDTO"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>内网首页</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="css/main.css" rel="stylesheet">
<link rel="stylesheet" href="css/jquery.fullPage.css">
<script type="text/javascript" src="js/koala.min.1.5.js"></script>

<script src="/app/js/jquery.min.js"></script>
<script type="text/javascript" src="/app/js/desktop.js"></script>
<script src="/app/js/jquery-powerSwitch.js"></script>
<script type="text/javascript" src="/app/js/popup.js"></script>
<script type="text/javascript" src="/app/research/js/encode.js"></script>
<%
    CurrentSessionUser sessionUser=(CurrentSessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String user=sessionUser.getSysUser().getName();
    String userBm= sessionUser.getDepartment().getId();
    Map<String,String> roles=sessionUser.getRoles();
  /*   String noticeData = ReportUtil.getNotice("通知公告");
    String news = ReportUtil.getNotice("内网新闻");
    String newsImg = ReportUtil. getNoticeImg("内网新闻");
    String inspCount = TS_Util.getInspDeviceCount();
    String inspCountN = TS_Util.getInspDeviceCountDay();
    String taskCount = TS_Util.getTaskCount(); */
    %>
<script type="text/javascript">
try{
	parent.mPanelDispay({panel:'left',display:false,close:true});//隐藏父层菜单
}catch (e){}
document.onkeydown=function(event){
    var e = event || window.event || arguments.callee.caller.arguments[0];           
	if(e && e.keyCode==13){ // enter 键
		indexSerachClick();
    }
};
$(function(){//jQuery页面载入事件
	//获取通知公告数据
	<%-- var noticeData = <%=noticeData%>;
	if(noticeData!=null){
		var noticeUl = $('#wordSlide');
		var l = noticeData.length;
		if(l>3){
			l=3;
		}
		for (var i = 0; i < l; i++) {
	      	if(i==0){
	      		noticeUl.append($('<li id="wordList'+(i+1)+'"><span class="point"></span><a href="'+noticeData[i].url+'">'+noticeData[i].title+'<span class="hotgg"></span></a></span> <span class="date">'+noticeData[i].date+'</span></li>'));	
	      	}else{
	      		noticeUl.append($('<li id="wordList'+(i+1)+'"><span class="point"></span><a href="'+noticeData[i].url+'">'+noticeData[i].title+'<span class="hotgg"></span></a></span> <span class="date">'+noticeData[i].date+'</span></li>'));
	      	}
		}
	} --%>
	//获取新闻
<%-- 	var news = <%=news%>;
	if(news!=null){
		$('#news').html("");
		var newContainer = $('#news');
		var l = news.length;
		if(l>10){
     		l=9;
     	}
		for (var i = 0; i < l; i++) {
			newContainer.append($('<li class="hg"><a href="'+news[i].url+'" >'+news[i].title+'</a><span class="date">'+news[i].date+'</span></li>'));
		}
	}
	//当年检验数量
 	var inspCounts = "<%=inspCount%>";
 	var inspCount = inspCounts.split(",")[0];
 	var years = inspCounts.split(",")[1];
 	$("#year").html(years);
 	$("#inspCount").html("");
 	for (var i = 0; i < inspCount.length; i++) {
 		$("#inspCount").append(inspCount.substring(i,i+1))
	}
 	
 	//今天检验数量
 	var inspCountsN = "<%=inspCountN%>";
 	$("#inspCountN").html("");
 	for (var i = 0; i < inspCountsN.length; i++) {
 		$("#inspCountN").append(inspCountsN.substring(i,i+1))
	}
 	
 	//今天任务数量
 	var taskCount = "<%=taskCount%>";
 	$("#taskCount").html("");
 	for (var i = 0; i < taskCount.length; i++) {
 		$("#taskCount").append(taskCount.substring(i,i+1))
	} --%>
});
function openNews(name){
	var name1 = encodeURI(name);
	top.$.dialog({
		width : 1024,
		height :800,
		lock : true,
		title : name,
		content : 'url:app/research/main_news_list.jsp?status=add&name='+name1,
		data : {
			"window" : window
		}
	});
}
function indexSerachClick(){
	if($("#search").val()=="输入职工姓名、维保单位"){
		alert("请输入查询条件！")
		return;
	}
	var name=$("#search").val();
	$("#yuns").html("");
	$("#yuns").append('<div class="waiting"><img src="/k/kui/skins/default/images/common/loading.gif" style="position: absolute;margin-top: 8px;"></img><span style="margin-left: 30px;">正在搜索，请稍后……</span></div>');
	/* window.location.href= $("base").attr("href")+"/enterSearchAction/searchAll.do?name="+EncodeUtf8(name); */
	window.location.href= "/enterSearchAction/searchAll.do?name="+EncodeUtf8(name);
		
}
</script>
</head>
<body>


<div id="dowebok">
<div class="section">
	<!-- page01 -->
	 <div class="slide">
			<div class="m_news">
			 	<div class="container ">
			 		<div class="n_boxes clearfix row">
						<div class="n_lbox">
							<!-- 左列表 -->
							<ul> 
								<li>
									<a href="http://www.scsei.org.cn/innerindex.php?action=category&categoryid=19&page=">
										<i class="cor01"> <img src="images/in_l_ico1.png" alt=""> </i>
										<div class="btname">
											<h4>院文件</h4>
											<!-- <span class="numtt">214,511</span> -->
										</div>
									</a>
								</li>
								<li>
									<a href="javascript:openNews('交流园地')">
										<i class="cor02"> <img src="images/in_l_ico2.png" alt=""> </i>
										<div class="btname">
											<h4>交流园地</h4>
											<!-- <span class="numtt">12,550</span> -->
										</div>
									</a>
								</li>
								<li>
									<a href="/app/maintenance/maintenance_confirm_list.jsp">
										<i class="cor03"> <img src="images/in_l_ico3.png" alt=""> </i>
										<div class="btname">
											<h4>平台运维日志</h4>
											<!-- <span class="numtt">564,123</span> -->
										</div>
									</a>
								</li>
								<li>
									<a href="http://www.scsei.org.cn/innerindex.php?action=topic&topicid=29">
										<i class="cor04"> <img src="images/in_l_ico4.png" alt=""> </i>
										<div class="btname">
											<h4>质量考核通报</h4>
											<!-- <span class="numtt">1,564</span> -->
										</div>
									</a>
								</li>
								<li>
									<a href="http://www.scsei.org.cn/innerindex.php?action=category&categoryid=70&page=">
										<i class="cor05"> <img src="images/in_l_ico5.png" alt=""> </i>
										<div class="btname">
											<h4>体系文件</h4>
											<!-- <span class="numtt">24,022</span> -->
										</div>
									</a>
								</li>
								<li>
									<a href="/resourceSpace/goUserSpace.do?main=true">
										<i class="cor06"> <img src="images/in_l_ico6.png" alt=""> </i>
										<div class="btname">
											<h4>电子资料云平台</h4>
											<!-- <span class="numtt">1,745,233</span> -->
										</div>
									</a>
								</li>
							</ul>
						 	<!-- 左 end -->
						</div>
						<!-- 中间 -->
						<div class="n_mbox">
							<div class="new_cont">
								
								<!-- 通知公告 -->
								<div class="tzgg">

									<div class="new_tit"><span class="tag"></span>通知公告 <span class="btmore"><a href="http://www.scsei.org.cn/innerindex.php?action=category&categoryid=2&page="><div class="typing_loader"></div></a></span> </div>
									<ul id="wordSlide">
										<!-- <li><span class="point"></span><a href="">川特院办13号 关于2019年元旦放假安排的通知<span class="hotgg"></span></a></span> <span class="date">2018-12-29</span></li>
										<li><span class="point"></span><a href="">川特院104号 关于任命王光明为压力容器检验和安全责任工程师的通知</a> <span class="date">2018-12-20</span></li>
										<li><span class="point"></span><a href="">鼎盛公司清算注销中介服务机构比选通告</a> <span class="date">2018-11-01</span></li> -->
									</ul>

								</div>
								<!-- 通知公告 end -->

								<!-- 图片新闻 -->
								<div class="nei_newlist">
									<div class="new_tit"><span class="tag"></span>内网新闻 <span class="btmore"><a href="http://www.scsei.org.cn/innerindex.php?action=category&categoryid=8&page="><div class="typing_loader"></div></a></span> </div>

									<div class="nei_boxes">
										<!-- 轮播 -->

										<div class="classes-wrap clearfix" >	
											<div class="classes-item active" >
												图片新闻
											</div>
											<div class="classes-item">
												列表新闻
											</div>
										 </div>
										<div class="swfimg list-wrap active">
											<div id="fsD1" class="focus">  
													<div id="D1pic1" class="fPic">  
														<div class="fcon" style="display: none;">
															<a target="_blank" href="#"><img src="images/swf01.jpg" style="opacity: 1; "></a>
															<span class="shadow"><a target="_blank" href="#">人力资源部深入一线开展考核工作</a></span>
														</div>
														
														<div class="fcon" style="display: none;">
															<a target="_blank" href="#"><img src="images/swf02.jpg" style="opacity: 1; "></a>
															<span class="shadow"><a target="_blank" href="#">第三党支部开展专题学习活动</a></span>
														</div>
														
														<div class="fcon" style="display: none;">
															<a target="_blank" href="#"><img src="images/swf03.jpg" style="opacity: 1; "></a>
															<span class="shadow"><a target="_blank" href="#">省特检院顺利完成承压类特种设备制造监督检验能力评估迎审工作</a></span>
														</div>
														
														<div class="fcon" style="display: none;">
																<a target="_blank" href="#"><img src="images/swf04.jpg" style="opacity: 1; "></a>
															<span class="shadow"><a target="_blank" href="#">团结援藏</a></span>
														</div>    
													</div>
													<div class="fbg">
													<div class="D1fBt" id="D1fBt">  
														<a href="javascript:void(0)" hidefocus="true" target="_self" class=""><i>1</i></a>  
														<a href="javascript:void(0)" hidefocus="true" target="_self" class=""><i>2</i></a>  
														<a href="javascript:void(0)" hidefocus="true" target="_self" class="current"><i>3</i></a>  
														<a href="javascript:void(0)" hidefocus="true" target="_self" class=""><i>4</i></a>  
													</div>
													</div>
													<span class="prev"></span>   
													<span class="next"></span>    
											</div>
											<script type="text/javascript">
												var news1 = null <%-- <%=newsImg%> --%>;
									         	if(news1!=null){
									         		var l = news1.length;
										         	if(l>4){
										         		l=4;
										         	}
									         		if(l>0){
									         			$("#D1pic1").html("");
									         		}
										        	for (var i = 0; i < l; i++) {
										        		var img = news1[i].image;
										        			//img = img.replace("http:\/\/www.scsei.org.cn\/","http:\/\/192.168.3.5\/");
										            	var ss =  '<div class="fcon" style="display: none;">'+
																'<a target="_blank" href="'+news1[i].url+'"><img src="'+img+'" style="opacity: 1; "></a>'+
																'<span class="shadow"><a target="_blank" href="'+news1[i].url+'">'+news1[i].title+'</a></span>'+
																''+
															'</div>';
															$("#D1pic1").append(ss)
										            }
										             
									         	}
												Qfast.add('widgets', { path: "js/terminator2.2.min.js", type: "js", requires: ['fx'] });  
												Qfast(false, 'widgets', function () {
													K.tabs({
														id: 'fsD1',   //焦点图包裹id  
														conId: "D1pic1",  //** 大图域包裹id  
														tabId:"D1fBt",  
														tabTn:"a",
														conCn: '.fcon', //** 大图域配置class       
														auto: 1,   //自动播放 1或0
														effect: 'fade',   //效果配置
														eType: 'click', //** 鼠标事件
														pageBt:true,//是否有按钮切换页码
														bns: ['.prev', '.next'],//** 前后按钮配置class                          
														interval: 3000  //** 停顿时间  
													}) 
												})  
											</script>
										</div>
										<!-- 轮播 end -->
										<!-- 新闻列表 -->
										<div class="new_boxlist list-wrap">
											<ul id="news">
												<li><i class="hg"></i><a href="" title="省特检院一行赴中国特检院挂职锻炼">省特检院一行赴中国特检院挂职锻炼</a><span class="date">2018-12-04</span></li>
												<li><i class="hg"></i><a href="" title="中办国办出台意见允许科研人员和教师依法依规适度兼职兼薪">中办国办出台意见允许科研人员和教师依法依规适度兼职兼薪</a><span class="date">2018-12-04</span></li>
												<li><i class="hg"></i><a href="" title="第三党支部开展专题学习活动">第三党支部开展专题学习活动</a><span class="date">2018-12-04</span></li>
												<li><i></i><a href="" title="省特检院顺利完成承压类特种设备制造监督检验能力评估迎审工作">省特检院顺利完成承压类特种设备制造监督检验能力评估迎审工作</a><span class="date">2018-12-04</span></li>
												<li><i></i><a href="" title="首批援藏特检突击队半程工作总结">首批援藏特检突击队半程工作总结</a><span class="date">2018-12-04</span></li>
												<li><i></i><a href="" title="业务服务部抓作风强管理">业务服务部抓作风强管理</a><span class="date">2018-12-04</span></li>
												<li><i></i><a href="" title="机电一部积极配合检查西博会电梯安全保障工作">机电一部积极配合检查西博会电梯安全保障工作</a><span class="date">2018-12-04</span></li>
												<li><i></i><a href="" title="首批援藏特检突击队半程工作总结">首批援藏特检突击队半程工作总结</a><span class="date">2018-12-04</span></li>
												<li><i></i><a href="" title="业务服务部抓作风强管理">业务服务部抓作风强管理</a><span class="date">2018-12-04</span></li>
											</ul>
										</div>
										<!-- 新闻列表 end -->
									</div>
									


								</div>
								<!-- 图片新闻 end -->
							</div>
						</div>
						<!-- 中间 end -->
						<!-- 右列表 -->
						<div class="n_rbox">
							
							<div class="yunbox">
								<div class="nr_tit">电子资料云 <span class="datenow" id="today">2019/02/20 星期三</span></div>
								<div class="formboxes" id="yuns">
									<form>
										<input name="" class="ipt" type="text"  value="输入职工姓名、维保单位" id="search" style="color:#9C9A9C;" onfocus="if(this.value=='输入职工姓名、维保单位'){this.value='';this.style.color = '#000'};" onblur="if(this.value==''){this.value='输入职工姓名、维保单位';this.style.color='#9C9A9C'};" />
										<input name="" class="ser_bnt" type="button" value="" id="dosearch" onclick="indexSerachClick()"/>
									</form>
								</div>
							</div>
							<div class="mubiao">
									<div class="nr_tit">常用目录 </div>
									<ul class="mulist">
										<li><a href="http://www.scsei.org.cn/innerindex.php?action=category&categoryid=58&page="><span><i class="i01"></i></span>组织结构</a></li>
										<li><a href="http://y.scsei.org.cn/app/cloud_platform/down/down_file_list.jsp"><span><i class="i02"></i></span>下载中心</a></li>
										<li><a href="javascript:openNews('党建工作')"><span><i class="i03"></i></span>党建工作</a></li>
										<li><a href="javascript:openNews('廉政行风')"><span><i class="i04"></i></span>廉政行风</a></li>
										<li><a href="javascript:openNews('质量管理')"><span><i class="i05"></i></span>质量管理</a></li>
										<li><a href="javascript:openNews('设备管理')"><span><i class="i06"></i></span>设备管理</a></li>
										<li><a href="http://www.scsei.org.cn/innerindex.php?action=category&categoryid=53&page="><span><i class="i07"></i></span>规范标准</a></li>
										<li><a href="http://www.scsei.org.cn/innerindex.php?action=category&categoryid=22&page="><span><i class="i08"></i></span>检验(事故)案例</a></li>
										<li><a href="javascript:openNews('业务培训')"><span><i class="i09"></i></span>业务培训</a></li>
										<li><a href="javascript:openNews('科研工作')"><span><i class="i10"></i></span>科研工作</a></li>
										<li><a href="javascript:openNews('院大事记')"><span><i class="i11"></i></span>院大事记</a></li>
										<li><a href="javascript:openNews('文化建设')"><span><i class="i12"></i></span>文化建设</a></li>
									</ul>
							</div>
						</div>
						<!-- 右列表 end -->
					</div>
			 	</div>
			</div>
	</div>
	<!-- page01 end-->
	<!-- page02 -->
	<div class="slide"> 


		<div class="tj_center">
			<div class="container ">
				<div class="tj-wrap clearfix row">
					<div class="tj-counts">
						<span class="l-tips"></span>
						<div class="clearfix">
							<div class="tj-boxes">
								<a href="#">
									<i class="cor07"> <img src="images/tj01.png" alt=""> </i>
									<div class="btname">
										<h4><span id="year">2016</span>年已完成检验设备</h4>
										<span class="numtt fc1" id="inspCount">350</span>台
									</div>
								</a>
							</div>
							<div  class="tj-boxes">
								<a href="#">
									<i class="cor08"> <img src="images/tj01.png" alt=""> </i>
									<div class="btname">
										<h4>今天完成检验设备</h4>
										<span class="numtt fc2" id="inspCountN">10</span>台
									</div>
								</a> 
							</div>
							<div  class="tj-boxes">
								<a href="#">
									<i class="cor09"> <img src="images/tj01.png" alt=""> </i>
									<div class="btname">
										<h4>今天发布任务书</h4>
										<span class="numtt fc3" id="taskCount">0</span>条
									</div>
								</a>
							</div>
						</div>	
						<div class="tj-info">
							<span id="dataUpdateDateInfo">2019-02-25  数据更新</span>
						</div>
						
					</div>
					<div class="yw-wrap">
					<%if(!userBm.equals("100032") && !"100038".equals(userBm) && !"100039".equals(userBm)){ %>
						<div class="yw-boexs">
							<div class="yw-content">
								<div class="nr_tit">业务处理 </div>
								<ul class="yw-item">
									<li><a href="/app/office/ywhbsgz_fb_list2.jsp" class="bgc01" ><img src="images/yw_a01.png">任务台账</a></li>
									<li><a href="/app/office/office_rv_fb_list2.jsp" class="bgc02" ><img src="images/yw_a02.png">重大任务</a></li>
									<li><a href="/department/basic/getFlowInfo.do" class="bgc03" ><img src="images/yw_a03.png">待处理业务</a></li>
									<li><a href="/inspection/zzjd/getFlowInfo.do" class="bgc04" ><img src="images/yw_a04.png">批量待处理业务</a></li>
								</ul>
							</div>
						</div>
						<div class="yw-boexs">
							<div class="yw-content">
								<div class="nr_tit">财务报销 </div>
								<ul class="yw-item">
										<li><a href="/app/finance/fybxd_list.jsp?validPwd=1" class="bgc05" ><img src="images/yw_b01.png">费用报销申请</a></li>
										<li><a href="/app/finance/clfbxd_list.jsp?validPwd=1" class="bgc06" ><img src="images/yw_b02.png">差旅费用申请</a></li>
										<li><a href="/app/finance/finance_bills.jsp?validPwd=1" class="bgc07" ><img src="images/yw_b03.png">还款单填报</a></li>
										<li><a href="/app/finance/pxfbxd_list.jsp?validPwd=1" class="bgc08" ><img src="images/yw_b04.png">培训费用申请</a></li>
										<li><a href="/app/finance/recipients_bill.jsp?validPwd=1" class="bgc09" ><img src="images/yw_b05.png">领款单填报</a></li>
										<li><a href="/app/finance/messageChecky_detail.jsp" class="bgc10" ><img src="images/yw_b06.png">个人工资查询</a></li>
								</ul>
							</div>
						</div>
						<%} %>
						<div class="yw-boexs">
							<div class="yw-content">
								<div class="nr_tit">设备信息 </div>
								<ul class="yw-item">
									<%if(!userBm.equals("100032") && !"100038".equals(userBm) && !"100039".equals(userBm)){ %>
										<li><a href="/app/device/device_index.jsp?type=1" class="bgc01" ><img src="images/yw_c01.png">设备信息</a></li>
										<%} %> 
									<%  if((roles.get("2c90758150a1f8ad0150a29f15530003")!=null||roles.get("EA94849D4083D0A8E040007F020052CE")!=null)||userBm.equals("100032")||userBm.equals("100038")||userBm.equals("100039")){%>
										<li><a href="/app/device/device_index.jsp?type=2" class="bgc04" ><img src="images/yw_c02.png">设备预警</a></li>
										<%  }%>
										<li><a href="/app/query/report_query_list.jsp" class="bgc11" ><img src="images/yw_c03.png">报告查询</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				
			</div>
		</div>
	</div>
	<!-- page02 end-->


</div>
</div>


<script src="js/jquery-1.8.3.min.js"></script>
<script src="js/jquery.fullPage.js"></script>
<script>
$(function(){
    $('#dowebok').fullpage({
        sectionsColor : [''],
        loopBottom: true
    });

});


$(function(){
	$('.classes-wrap .classes-item').click(function(){
		var i = $(this).index();
		$(this).addClass('active').siblings().removeClass('active');
		$('.list-wrap').eq(i).addClass('active').siblings().removeClass('active');
	})
	// $('#today').html(getDateTime());   jq
})

window.onload=function(){
	document.getElementById("today").innerHTML=getDateTime();  //原生
	document.getElementById("dataUpdateDateInfo").innerHTML="<%=DateUtil.getDate(new Date())%> 数据更新";
}

function getDateTime(){
    var dateObj = new Date(); //表示当前系统时间的Date对象
    var year = dateObj.getFullYear(); //当前系统时间的完整年份值
    var month = dateObj.getMonth()+1; //当前系统时间的月份值
    var date = dateObj.getDate(); //当前系统时间的月份中的日
    var day = dateObj.getDay(); //当前系统时间中的星期值
    var weeks = ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
    var week = weeks[day]; //根据星期值，从数组中获取对应的星期字符串
    var hour = dateObj.getHours(); //当前系统时间的小时值
    var minute = dateObj.getMinutes(); //当前系统时间的分钟值
    var second = dateObj.getSeconds(); //当前系统时间的秒钟值
    var timeValue = "" +((hour >= 12) ? (hour >= 18) ? "晚上" : "下午" : "上午" ); //当前时间属于上午、晚上还是下午
    return dateFilter(year)+"年"+dateFilter(month)+"月"+dateFilter(date)+"日 "+ week;
}

// 年月日不满10位的前面补0
function dateFilter(date){
    if(date < 10){return "0"+date;}
    return date;
}





</script>

    
</body>
</html>