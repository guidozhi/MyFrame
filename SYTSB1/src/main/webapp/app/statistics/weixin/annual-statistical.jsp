<%@page import="java.util.Map"%>
<%@page import="util.ReportUtil"%>
<%@page import="java.util.HashMap"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>你的2016特检院大数据</title>
<!-- <embed src="app/statistics/weixin/music/music1.ac3" hidden="true" autostart="true" loop="true"> </embed> -->
<%@include file="/k/kui-base-list.jsp"%>
<meta http-equiv="cache-control" content="no-cache" />
<meta name="format-detection" content="telephone=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="description" content="luofanting.com.cn">  
<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0"> 
<link rel="stylesheet" href="app/statistics/weixin/css/style.css">
<link rel="stylesheet" href="app/statistics/weixin/css/swiper.min.css">
<link rel="stylesheet" href="app/statistics/weixin/css/animate.css">
<link rel="stylesheet" href="app/statistics/weixin/css/animate.min.css">
<script src="app/statistics/js/echarts.js"></script>
<script src="app/statistics/weixin/js/china.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.2"></script>
<%
String name = request.getAttribute("name").toString();
HashMap<String, Object> map = ReportUtil.getNewsN(name,"20","1");
String data = "";
String sumC = "";
int sumCI = 0;
if(map.get("data")!=null){
	data = map.get("data").toString();
	sumC = map.get("sumC").toString();
	if("-1".equals(sumC)){
		sumC="0";
		sumCI = 0;
	}else{
		sumCI = Integer.parseInt(sumC);
	}
	
}
%>
<style>
*{
	margin:0;
	padding:0;
}
html,body{
	height:100%;
}
body{
	font-family:"microsoft yahei";
}
.swiper-container {
  /*  width: 320px;
    height: 480px;*/
	width: 100%;
    height: 100%;
	background:#000;

  
}  

.swiper-slide{
	width:100%;
	height:100%;
	background:url(upload/bg.jpg) no-repeat left top;
	background-size:100% 100%;

}
img{
	/*display:block;*/
}
.swiper-pagination-bullet {
width: 8px;
height: 8px;
background: #000;
opacity: .4;
}
.swiper-pagination-bullet-active {
	    background: #e77917;
opacity: 1;
}
@-webkit-keyframes start {
	0%,30% {opacity: 0;-webkit-transform: translate(10px,0);}
	40% {opacity: .5;-webkit-transform: translate(5px,0);}
	80% {opacity: 1;-webkit-transform: translate(0,0);}
	90% {opacity: .5;-webkit-transform: translate(-4px,0);}
	100% {opacity: 0;-webkit-transform: translate(-8px,0);}
}
@-moz-keyframes start {
	0%,30% {opacity: 0;-moz-transform: translate(10px,0);}
	40% {opacity: .5;-moz-transform: translate(5px,0);}
	80% {opacity: 1;-moz-transform: translate(0,0);}
	90% {opacity: .5;-moz-transform: translate(-4px,0);}
	100% {opacity: 0;-moz-transform: translate(-8px,0);}
}
@keyframes start {
	0%,30% {opacity: 0;transform: translate(10px,0);}
	40% {opacity: .5;transform: translate(5px,0);}
	80% {opacity: 1;transform: translate(0,0);}
	90% {opacity: .5;transform: translate(-4px,0);}
	100% {opacity: 0;transform: translate(-8px,0);}
}
.ani{
/*	position:absolute;*/
	}
.txt{
	position:absolute;
}
.array{
	position:absolute;z-index:999;-webkit-animation: start 1.5s infinite ease-in-out;
}
</style>

</head>

<body >
<audio autoplay="autoplay" controls="controls"loop="loop" preload="auto" style="display:none;"
            src="app/statistics/weixin/music/2.mp3">
      你的浏览器不支持audio标签
</audio>
<div class="layout">

	<div class="swiper-container" id="swiper-container">
		
		
		 <div class="swiper-wrapper">
		 
			<!-------------slide1----------------->
			<section class="swiper-slide bg01">
			
				<div class="container "   >
			
				<div class="in_logo"> <img src="app/statistics/weixin/images/lo_t.png"  class=" ani"  swiper-animate-effect="slideInLeft" swiper-animate-duration="0.5s" swiper-animate-delay="0s" > </div>
				<div class="in_2016"> <img src="app/statistics/weixin/images/2016ds.png" class="ani" swiper-animate-effect="zoomIn" swiper-animate-duration="0.5s" swiper-animate-delay="0.2s" > </div>	
				<div class="in_ren"> <img src="app/statistics/weixin/images/ren.png" class="ani" swiper-animate-effect="fadeInUp" swiper-animate-duration="0.5s" swiper-animate-delay="0.5s" > </div>
				<img src="app/statistics/weixin/images/check_production.png" style="width:4.5rem; bottom:.8rem; right:.25rem;" class="array" > 
				</div>
			
			</section>
			<!-------------slide2----------------->
			<section class="swiper-slide bg02">
	
			<div class="container" >
			
				<div class="box01">
				<div class="inzs_list ani" swiper-animate-effect="bounceInUp"  style="margin-left: 30px;"
                 swiper-animate-duration="0.2s" swiper-animate-delay="0.1s" class="biao_body" >
                 <span style="font-size: 20px;color: #36648B;">
                 ${data.empTitle!='无'?data.empTitle:''}
                 <%=name %></span> <br/><span  class="dsj"  style="font-size: 14px;">您好！</span></div>
                 <br/>
					<div class="max_title">
						<img src="app/statistics/weixin/images/a_t1.png"  class="ani" swiper-animate-effect="flipInX" swiper-animate-duration="0.5s" swiper-animate-delay="0s"  >
					</div>
					<div class="num01 ani" swiper-animate-effect="lightSpeedIn" swiper-animate-duration="0.5s" swiper-animate-delay="0.2s" >
						<c:if test="${data.enterYear!=null&&data.enterYear!='0'}">
							<span class="dsj"> ${data.enterYear==null?'0':data.enterYear}</span > 年
						</c:if>
						<c:if test="${data.enterMonth!=null&&data.enterMonth!='0'}">
							<span class="dsj"> ${data.enterMonth==null?'0':data.enterMonth}</span> 月
						</c:if>
						<c:if test="${data.enterDay!=null&&data.enterDay!='0'}">
							 <span class="dsj"> ${data.enterDay==null?'0':data.enterDay}</span> 天 
						 </c:if>
							
					</div>
				</div>
				
				<div class="box02">
					 <div class="max_title2 ani" swiper-animate-effect="flipInX" swiper-animate-duration="0.5s" swiper-animate-delay="0.5s" >
					</div>
					 <span class="zhenshu dsj ani" swiper-animate-effect="bounceInDown" swiper-animate-duration="0.5s" swiper-animate-delay="0.5s" >${data.cartListC==0?1:data.cartListC+1}</span>
				
				</div>
				<c:if test="${data.cartListC>0}">
				<div class="inzs_list ani" swiper-animate-effect="bounceInUp" swiper-animate-duration="1s" swiper-animate-delay="0.8s" >
					   
					   <table class="biaot" align="center" cellpadding="0" cellspacing="0" >
						<tbody>
						  <tr height="30">
							<th align="left"  width="20%" >证书项目</th>
							<th align="left"  width="20%" >证书性质</th>
							<th align="left"  width="30%" >批准日期</th>
							<th align="left"  width="30%" >有效日期</th>
				
						  </tr>
						</tbody>
					  </table>
					  
					  
					  <table id="showTable"  align="center" cellpadding="0" cellspacing="0" class="biao_body">
						<tbody id="certtb">
						  </tbody>
					  </table>
					  
					  
					  
					  
					   </div>
				</c:if>
			<%--  <c:if test="${data.education!=null}">
			 	<div class="inzs_list ani" swiper-animate-effect="bounceInUp" 
			 	 swiper-animate-duration="1.2s" swiper-animate-delay="1s"  class="biao_body" style="margin-left: 50px;">
			 	 <span style="font-size: 16px;color: #36648B;">学历：</span> <span  class="dsj" style="font-size: 14px;">${data.education}</span></div></c:if>
               --%> <c:if test="${empIdCard!=null}">
               <div class="inzs_list ani" swiper-animate-effect="bounceInUp"  style="margin-left: 50px;"
                 swiper-animate-duration="1.2s" swiper-animate-delay="1s" class="biao_body" >
                 <span style="font-size: 16px;color: #36648B;">身份证：</span> <span  class="dsj"  style="font-size: 14px;">${empIdCard}</span></div></c:if>
		   <img src="app/statistics/weixin/images/check_production.png" style="width:4.5rem; bottom:.8rem; right:.25rem;" class="array" > 
		   </div>
		   
		   </section>
		   
		   <c:if test="${rolej==true&&data.report!=null}">
			<!-------------slide3----------------->
			<section class="swiper-slide bg03">
			
			
				<div class="container "  >
	 	
		<div class="box03">
			<div class="max_title">
				<img src="app/statistics/weixin/images/b_t1.png"  class="ani"  swiper-animate-effect="flipInY" swiper-animate-duration="1s" swiper-animate-delay="0s">
			</div>

		</div>
		
	    <div class="work_list">
			
			<div class="w_bt w_t1 ani" swiper-animate-effect="fadeInRight" swiper-animate-duration="1s" swiper-animate-delay="0.5s">
				
				
				<div> <span class="work_tit">检验特种设备</span> <span class="dsj red">${data.dCount}</span>台/套</div>
				
				<div class="bar">
					<div  class="remainingSpaceUi">
					<span class="remainingSpaceUi_span" style="width: ${data.dbl}%; background-color:#ff7700; background-position: initial initial; background-repeat: initial initial;"></span> 
					</div>
					
					
					<span class="bar_tt">占年度全院的${data.dbl}% </span>
				</div>
			</div>
			
			
			
			
			<div class="w_bt w_t2 ani" swiper-animate-effect="fadeInRight" swiper-animate-duration="1s" swiper-animate-delay="0.8s">
			
				<div> <span class="work_tit">审核报告书</span> <span class="dsj red">${data.report. sh_count}</span>份</div>
				
				<div class="bar">
					<div  class="remainingSpaceUi">
					<span class="remainingSpaceUi_span" style="width: ${fn:substring((data.report. sh_count)/(data.reportAll. sh_count)*100, 0, 5)}%; background-color:#ff7700; background-position: initial initial; background-repeat: initial initial;"></span> 
					</div>
					
					
					<span class="bar_tt">占年度全院的${fn:substring((data.report. sh_count)/(data.reportAll. sh_count)*100, 0, 5)}% </span>
				</div>
			
			
			</div>
			<div class="w_bt w_t3 ani" swiper-animate-effect="fadeInRight" swiper-animate-duration="1s" swiper-animate-delay="1s">
				
				
				<div> <span class="work_tit">签发报告</span> <span class="dsj red">${data.report. qf_count}</span>份</div>
				
				<div class="bar">
					<div  class="remainingSpaceUi">
					<span class="remainingSpaceUi_span" style="width: ${fn:substring((data.report. qf_count)/(data.reportAll. qf_count)*100, 0, 5)}%; background-color:#ff7700; background-position: initial initial; background-repeat: initial initial;"></span> 
					</div>
					
					
					<span class="bar_tt">占年度全院的${fn:substring((data.report. qf_count)/(data.reportAll. qf_count)*100, 0, 5)}%</span>
				</div>
			</div>
			
			<!-- <div class="w_bt"></div> -->
		
		    <div class="work_bg ani time_o_5" swiper-animate-effect="fadeInLeft" swiper-animate-duration="1s" swiper-animate-delay="0.2s"></div>
		
		</div>

		

	 <img src="app/statistics/weixin/images/check_production.png" style="width:4.5rem; bottom:.8rem; right:.25rem;" class="array" > 
	 
	 </div>
			
			
			
			</section>
		   
		   </c:if>
		   <c:if test="${rolef==true&&data.report!=null}">
		   
			<!-------------slide4----------------->
			<section class="swiper-slide bg04">
			
				
				<div class="container" >
	 	
		<div class="box03">
			<div class="max_title">
				<img src="app/statistics/weixin/images/b_t1.png"  class=" ani " swiper-animate-effect="flipInY" swiper-animate-duration="0.5s" swiper-animate-delay="0s" >
			</div>

		</div>
		
	    <div class="work_list">
			
			<div class="w_bt w_t1 ani" swiper-animate-effect="fadeInRight" swiper-animate-duration="1s" swiper-animate-delay="0.5s">
				
				
				<div> <span class="work_tit">打印报告</span> <span class="dsj red">${data.report. dy_count}</span>份</div>
				
				<div class="bar">
					<div  class="remainingSpaceUi">
					<span class="remainingSpaceUi_span" style="width: ${fn:substring((data.report. dy_count)/(data.reportAll. dy_count)*100, 0, 5)}%; background-color:#ff7700; background-position: initial initial; background-repeat: initial initial;"></span> 
					</div>
					
					
					<span class="bar_tt">占全院${fn:substring((data.report. dy_count)/(data.reportAll. dy_count)*100, 0, 5)}% </span>
				</div>
			</div>
			
			
			
			
			<div class="w_bt w_t2 ani" swiper-animate-effect="fadeInRight" swiper-animate-duration="1s" swiper-animate-delay="0.8s">
			
				<div> <span class="work_tit">打印合格证</span> <span class="dsj red">${data.report. dyhgz_count}</span>份</div>
				
				<div class="bar">
					<div  class="remainingSpaceUi">
					<span class="remainingSpaceUi_span" style="width: ${fn:substring((data.report. dyhgz_count)/(data.reportAll. dyhgz_count)*100, 0, 5)}%; background-color:#ff7700; background-position: initial initial; background-repeat: initial initial;"></span> 
					</div>
					
					
					<span class="bar_tt">占全院${fn:substring((data.report. dyhgz_count)/(data.reportAll. dyhgz_count)*100, 0, 5)}% </span>
				</div>
			
			
			</div>
			<div class="w_bt w_t3 ani" swiper-animate-effect="fadeInRight" swiper-animate-duration="1s" swiper-animate-delay="1s">
				
				
				<div> <span class="work_tit">领取</span> <span class="dsj red">${data.report. lq_count}</span>份</div>
				
				<div class="bar">
					<div  class="remainingSpaceUi">
					<span class="remainingSpaceUi_span" style="width: ${fn:substring((data.report. lq_count)/(data.reportAll. lq_count)*100, 0, 5)}%; background-color:#ff7700; background-position: initial initial; background-repeat: initial initial;"></span> 
					</div>
					
					
					<span class="bar_tt">占全院${fn:substring((data.report. lq_count)/(data.reportAll. lq_count)*100, 0, 5)}%</span>
				</div>
			</div>
			
			<div class="w_bt w_t4 ani" swiper-animate-effect="fadeInRight" swiper-animate-duration="1s" swiper-animate-delay="1.2s">
			
				<div> <span class="work_tit">归档报告</span> <span class="dsj red">${data.report. gd_count}</span>份</div>
				
				<div class="bar">
					<div  class="remainingSpaceUi">
					<span class="remainingSpaceUi_span" style="width: ${fn:substring((data.report. gd_count)/(data.reportAll. gd_count)*100, 0, 5)}%; background-color:#ff7700; background-position: initial initial; background-repeat: initial initial;"></span> 
					</div>
					
					
					<span class="bar_tt">占全院${fn:substring((data.report. gd_count)/(data.reportAll. gd_count)*100, 0, 5)}%</span>
				</div>			
			
			
			</div>
		
		    <div class="work_bg ani time_o_5"  swiper-animate-effect="fadeInLeft" swiper-animate-duration="1s" swiper-animate-delay="0.2s"></div>
		
		</div>

		

	 <img src="app/statistics/weixin/images/check_production.png" style="width:4.5rem; bottom:.8rem; right:.25rem;" class="array" > 
	 
	 </div>
				
				
			
			
			</section>	   
			</c:if>

	<c:if test="${roley==true||rolebf==true}">		
		<!-------------slide5--地图--------------->
		<section class="swiper-slide bg06">	
		<div class="container" >
		<div class="box04">
			<div class="max_title3">
				<img src="app/statistics/weixin/images/ld.png"  class=" ani" swiper-animate-effect="rotateIn" swiper-animate-duration="0.5s" swiper-animate-delay="0s">
			</div>
		
		</div>
		<c:if test="${data.pxSum!=null}">
			<div id="main" class="gzzj_list ani" swiper-animate-effect="fadeInUp" swiper-animate-duration="1s" swiper-animate-delay="0.4s" style="width: 90%;height: 70%;"></div>
    	</c:if>	
    	<c:if test="${data.pxSum==null}">
    		<div style="margin-left: 30px;">系统没有相关数据！</div>
    	</c:if>
			</div>
			</section>	
		  </c:if> 
			<!-------------slide5----------------->
	<section class="swiper-slide bg05">
			
				
				<div class="container" >
	 	
		<div class="box03">
			<div class="max_title">
				<img src="app/statistics/weixin/images/c_t1.png"  class=" ani " swiper-animate-effect="flipInY" swiper-animate-duration="0.5s" swiper-animate-delay="0s">
			</div>

		</div>
		
	    <div class="px_bd ani" swiper-animate-effect="bounceIn" swiper-animate-duration="0.5s" swiper-animate-delay="0.2s">
		
			<div class="px_nub"> ${data.pxSum==null?'0':data.pxSum} </div>
		
		</div>
		
		<div class="px_ad ani" swiper-animate-effect="fadeInUp" swiper-animate-duration="0.5s" swiper-animate-delay="0.5s">
		
		
			<div><span class="px_time">${data.pxts==null?'0':data.pxts} <i style="font-size:.5rem;">天</i></span> <span style=" margin-right:.25rem"> &nbsp;</span> <span class="px_tt">${data.pxdd }</span>   </div>
		
			
		
		</div>
		

		

	 
	 <img src="app/statistics/weixin/images/check_production.png" style="width:4.5rem; bottom:.8rem; right:.25rem;" class="array" > 
	 </div>		
			
			
			
			</section>
		  
			<!-------------slide6----------------->
			<section class="swiper-slide bg06">
			
			<div class="container " >
	 	
		<div class="box03">
			<div class="max_title">
				<img src="app/statistics/weixin/images/d_t1.png"  class=" ani " swiper-animate-effect="flipInY" swiper-animate-duration="0.5s" swiper-animate-delay="0s" >
			</div>

		</div>
		
		<div class="gzzj_bt ani " swiper-animate-effect="fadeInUp" swiper-animate-duration="0.5s" swiper-animate-delay="0.2s" >
			<div> 特检云内有你<span><%=sumC %></span>条工作足迹，这里是最近<span><%=sumCI>10?10:sumCI %></span>条</div>
			<div> （可在检验软件云检索内输入姓名查看详细）</div>
			
		</div>
		
		<div class="gzzj_list ani" swiper-animate-effect="fadeInUp" swiper-animate-duration="1s" swiper-animate-delay="0.4s">
		
		     	<ul id='newsList'>
                </ul>
			
		
		
		
		</div>
		

		

	 <img src="app/statistics/weixin/images/check_production.png" style="width:4.5rem; bottom:.8rem; right:.25rem;" class="array" > 
	 
	 </div>
				
			
			
			</section>
		   
			<!-------------slide7----------------->
			<section class="swiper-slide bg07">
			
			
				<div class="container" >
	 	

		
		<div class="gzzj_bt msg_bt ani " swiper-animate-effect="fadeInUp" swiper-animate-duration="0.5s" swiper-animate-delay="0s">
			<c:if test="${roley==true}">
				<div> 2016年我院检验平台有<span>1275</span>项优化升级，信息化已成为特检工作重要抓手。
			</c:if>
			<c:if test="${rolebf==true&&roley==false}">
				<div> 2016年我院检验平台有<span>1275</span>项优化升级， 其中你部门提出了<span>${data.advanceSum==null?'0':data.advanceSum}</span>项，占比<span>${data.advanceBl==null?'0':data.advanceBl}%</span>。
			</c:if>
			<c:if test="${rolebf==false&&roley==false}">
				<div> 2016年我院检验平台有<span>1275</span>项优化升级， 其中有你提出的<span>${data.advanceSum==null?'0':data.advanceSum}</span>项，占比<span>${data.advanceBl==null?'0':data.advanceBl}%</span>。
			</c:if>
			
			
		</div>			
		</div>
		
		<div class="gzzj_list msg_list ani" swiper-animate-effect="fadeInUp" swiper-animate-duration="0.5s" swiper-animate-delay="0.2s">
		
		     	<ul id='advance'>
                	<c:forEach items="${listAdvance}" var="advance" end="${data.advanceSum<10?data.advanceSum:10}">
                		<li>
                        <div class="w_break">· ${advance.pro_desc}</div> <span>${fn:substring(advance.advance_date, 0, 10)}</span>
                   		 </li>
                	</c:forEach>
                </ul>
			
		
		
		
		</div>
		
		
		<div class="app_down ani" swiper-animate-effect="zoomIn" swiper-animate-duration="0.5s" swiper-animate-delay="0.5s">
		
			<div class="down_tt">
			
				<div><a href="http://app.qq.com/#id=search&key=%25E5%259B%259B%25E5%25B7%259D%25E7%2589%25B9%25E6%25A3%2580%25E4%25BA%2591" class="but01">安卓版下载</a></div>
			<!-- 	<div><a href="#" class="but02">苹果版下载</a></div> -->
				<div>
				<div class="bar kjdx">
				<span class="bar_tt">你的特检云空间已使用</span>
					<div  class="remainingSpaceUi">
					<span class="remainingSpaceUi_span" style="width: ${data.bl }%; background-color:#ff7700; background-position: initial initial; background-repeat: initial initial;"></span>                    
					</div>
					<span> <span id='use'></span>/${data.maxSize }G</span>
					
					
				</div>
				
				</div>

			</div>
			
			
		
		
		</div>

		
<img src="app/statistics/weixin/images/check_production.png" style="width:4.5rem; bottom:.8rem; right:.25rem;" class="array" > 
	 
	 
	 </div>
			
			
			
			</section>
		   
			<!-------------slide1----------------->
			<section class="swiper-slide bg08">
			
			
				<div class="container" >
	 	
		<div class="end_img">
			<div class="max_title">
				<img src="app/statistics/weixin/images/end_lo.png"  class=" ani " swiper-animate-effect="lightSpeedIn" swiper-animate-duration="1s" swiper-animate-delay="0s">
			</div>

		</div>
		
		<div class="end_img xcy delay_2"> <img src="app/statistics/weixin/images/end_tt.png"  class=" ani " swiper-animate-effect="fadeInLeft" swiper-animate-duration="0.5s" swiper-animate-delay="0.5s"> </div>
		
		<div class="copyright ani" swiper-animate-effect="fadeInUp" swiper-animate-duration="0.5s" swiper-animate-delay="0.8s">
		<div style="font-weight: bold;font-size: 18px; " align="center">
		<!-- <a href="http://pt.scsei.org.cn/weiXinAnnualSta/weiXinStaQuery.do">查看你的大数据</a> -->
		<a href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb0f376eb09e64dd3&redirect_uri=http://kh.scsei.org.cn/weiXinAnnualSta/weiXinStaQuery.do&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect" style="color:#e77919;"><B>查看你的大数据</B></a>
		
		</div>
		<br/>
		<span style="font-weight: bold;font-size: 16px; ">技术支持：信息宣传中心</span> </div>
		
		
		
		
		
		

		

	 
	 
	 </div>
			
			
			
			</section>
		   
		   
		   
		   
		   
		   
		   
	
	
			</div>	 
		 
		
		
		
	
		<div class="swiper-pagination"></div>  
		
		
		
	</div>

</div>




<script type="text/javascript" src="app/statistics/weixin/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="app/statistics/weixin/js/script.js"></script>
<script src="app/statistics/weixin/js/swiper.min.js"></script>
<script src="app/statistics/weixin/js/swiper.animate1.0.2.min.js"></script>

<script>  

	  
  var mySwiper = new Swiper ('.swiper-container', {
   direction : 'horizontal',
   pagination: '.swiper-pagination',
   
   mousewheelControl : true,
   onInit: function(swiper){
   swiperAnimateCache(swiper);
   swiperAnimate(swiper);
	  },
   onSlideChangeEnd: function(swiper){
	swiperAnimate(swiper);
    }	  
})  
  var useSize = "${data.useSize}";
	var size = useSize-0;
	if(size==0){
		useSize = "0G";
	}else if(size<0.01){
		useSize = "0.01G";
		
	}else{
		useSize = size.toFixed(2)+"G";
	}
	
	$("#use").html(useSize);
  //证书信息
  var cartList = <%=request.getAttribute("cartList")%>;
  if(cartList!=null){
	  for (var i = 0; i < cartList.length; i++) {
		  	$("#certtb").append('<tr>'
		  			+'<td  width="20%">'+cartList[i].cert_type+'</td>'
		  			+'<td  width="20%">'+cartList[i].cert_category+'</td>'
		  			+'<td align="left"  width="30%" >'+cartList[i].cert_begin_date+'</td>'
					+'<td align="left"  width="30%" >'+cartList[i].cert_end_date+'</td>' +'</tr>')
		  }
		  
  }
 
<%--   //建议
  var listAdvance =  <%=request.getAttribute("listAdvance")%>;;
	if(listAdvance!=null){
		var l = 20;
		if(listAdvance.length<20){
			l = listAdvance.length;
		}
		for (var i = 0; i < l; i++) {
			var title = listAdvance[i].pro_desc;
			$("#advance").append('<li>'
				           		 	+'<div class="w_break">·'+title+'</div> <span>'+listAdvance[i].advance_date+'</span>'
						      		+ ' </li>')
		}
	}
 --%>
	  //内网新闻信息
	  var newsData = <%=data %>;
		if(newsData!=null&&newsData!=""){
			var l = 10;
			if(newsData.length<10){
				l = newsData.length;
			}
			for (var i = 0; i < l; i++) {
				var title = newsData[i].title;
				$("#newsList").append('<li>'
					           		 	+'<div class="w_break">·'+title+'</div> <span>'+newsData[i].date+'</span>'
							      		+ ' </li>')
			}
		}
  </script>
  <c:if test="${roley==true||rolebf==true}">
  <c:if test="${data.pxSum!=null}">
  <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
        var geoCoordMap = {
        	    '上海': [121.4648,31.2891],
        	    '东莞': [113.8953,22.901],
        	    '东营': [118.7073,37.5513],
        	    '中山': [113.4229,22.478],
        	    '临汾': [111.4783,36.1615],
        	    '临沂': [118.3118,35.2936],
        	    '丹东': [124.541,40.4242],
        	    '丽水': [119.5642,28.1854],
        	    '乌鲁木齐': [87.9236,43.5883],
        	    '佛山': [112.8955,23.1097],
        	    '保定': [115.0488,39.0948],
        	    '兰州': [103.5901,36.3043],
        	    '包头': [110.3467,41.4899],
        	    '北京': [116.4551,40.2539],
        	    '北海': [109.314,21.6211],
        	    '南京': [118.8062,31.9208],
        	    '南宁': [108.479,23.1152],
        	    '南昌': [116.0046,28.6633],
        	    '南通': [121.1023,32.1625],
        	    '厦门': [118.1689,24.6478],
        	    '台州': [121.1353,28.6688],
        	    '合肥': [117.29,32.0581],
        	    '呼和浩特': [111.4124,40.4901],
        	    '咸阳': [108.4131,34.8706],
        	    '哈尔滨': [127.9688,45.368],
        	    '唐山': [118.4766,39.6826],
        	    '嘉兴': [120.9155,30.6354],
        	    '大同': [113.7854,39.8035],
        	    '大连': [122.2229,39.4409],
        	    '天津': [117.4219,39.4189],
        	    '太原': [112.3352,37.9413],
        	    '威海': [121.9482,37.1393],
        	    '宁波': [121.5967,29.6466],
        	    '宝鸡': [107.1826,34.3433],
        	    '宿迁': [118.5535,33.7775],
        	    '常州': [119.4543,31.5582],
        	    '广州': [113.5107,23.2196],
        	    '廊坊': [116.521,39.0509],
        	    '延安': [109.1052,36.4252],
        	    '张家口': [115.1477,40.8527],
        	    '徐州': [117.5208,34.3268],
        	    '德州': [116.6858,37.2107],
        	    '惠州': [114.6204,23.1647],
        	    '成都': [103.9526,30.7617],
        	    '扬州': [119.4653,32.8162],
        	    '承德': [117.5757,41.4075],
        	    '拉萨': [91.1865,30.1465],
        	    '无锡': [120.3442,31.5527],
        	    '日照': [119.2786,35.5023],
        	    '昆明': [102.9199,25.4663],
        	    '杭州': [119.5313,29.8773],
        	    '枣庄': [117.323,34.8926],
        	    '柳州': [109.3799,24.9774],
        	    '株洲': [113.5327,27.0319],
        	    '武汉': [114.3896,30.6628],
        	    '汕头': [117.1692,23.3405],
        	    '江门': [112.6318,22.1484],
        	    '沈阳': [123.1238,42.1216],
        	    '沧州': [116.8286,38.2104],
        	    '河源': [114.917,23.9722],
        	    '泉州': [118.3228,25.1147],
        	    '泰安': [117.0264,36.0516],
        	    '泰州': [120.0586,32.5525],
        	    '济南': [117.1582,36.8701],
        	    '济宁': [116.8286,35.3375],
        	    '海口': [110.3893,19.8516],
        	    '淄博': [118.0371,36.6064],
        	    '淮安': [118.927,33.4039],
        	    '深圳': [114.5435,22.5439],
        	    '清远': [112.9175,24.3292],
        	    '温州': [120.498,27.8119],
        	    '渭南': [109.7864,35.0299],
        	    '湖州': [119.8608,30.7782],
        	    '湘潭': [112.5439,27.7075],
        	    '滨州': [117.8174,37.4963],
        	    '潍坊': [119.0918,36.524],
        	    '烟台': [120.7397,37.5128],
        	    '玉溪': [101.9312,23.8898],
        	    '珠海': [113.7305,22.1155],
        	    '盐城': [120.2234,33.5577],
        	    '盘锦': [121.9482,41.0449],
        	    '石家庄': [114.4995,38.1006],
        	    '福州': [119.4543,25.9222],
        	    '秦皇岛': [119.2126,40.0232],
        	    '绍兴': [120.564,29.7565],
        	    '聊城': [115.9167,36.4032],
        	    '肇庆': [112.1265,23.5822],
        	    '舟山': [122.2559,30.2234],
        	    '苏州': [120.6519,31.3989],
        	    '莱芜': [117.6526,36.2714],
        	    '菏泽': [115.6201,35.2057],
        	    '营口': [122.4316,40.4297],
        	    '葫芦岛': [120.1575,40.578],
        	    '衡水': [115.8838,37.7161],
        	    '衢州': [118.6853,28.8666],
        	    '西宁': [101.4038,36.8207],
        	    '西安': [109.1162,34.2004],
        	    '贵阳': [106.6992,26.7682],
        	    '连云港': [119.1248,34.552],
        	    '邢台': [114.8071,37.2821],
        	    '邯郸': [114.4775,36.535],
        	    '郑州': [113.4668,34.6234],
        	    '鄂尔多斯': [108.9734,39.2487],
        	    '重庆': [107.7539,30.1904],
        	    '金华': [120.0037,29.1028],
        	    '铜川': [109.0393,35.1947],
        	    '银川': [106.3586,38.1775],
        	    '镇江': [119.4763,31.9702],
        	    '长春': [125.8154,44.2584],
        	    '长沙': [113.0823,28.2568],
        	    '长治': [112.8625,36.4746],
        	    '阳泉': [113.4778,38.0951],
        	    '青岛': [120.4651,36.3373],
        	    '韶关': [113.7964,24.7028]
        	};
        var BJData = [];
			var listpxj = <%=request.getAttribute("listpxj")%>
			if(listpxj!=null){
				for (var i = 0; i < listpxj.length; i++) {
					//alert(listpxj[i].pxdd+"----"+geoCoordMap[listpxj[i].pxdd])
					if(geoCoordMap[listpxj[i].pxdd]!=null&&geoCoordMap[listpxj[i].pxdd]!=undefined){
						///alert("1--"+listpxj[i].pxdd+'----'+geoCoordMap[listpxj[i].pxdd])
						var data = [];
						data[0]={};
						data[1]={};
						data[0]["name"]='成都';
					data[1]["name"]=listpxj[i].pxdd;
					data[1]["value"]=listpxj[i].pxsj;
					BJData[BJData.length]= data;	
					}else if(listpxj[i].jwd_x!=null&&listpxj[i].jwd_x!=undefined){
						
						geoCoordMap[listpxj[i].pxdd]=[];
						geoCoordMap[listpxj[i].pxdd][0]=listpxj[i].jwd_x;
						geoCoordMap[listpxj[i].pxdd][1]=listpxj[i].jwd_y;
						//alert("2--"+listpxj[i].pxdd+'----'+geoCoordMap[listpxj[i].pxdd])
						var data = [];
						data[0]={};
						data[1]={};
						data[0]["name"]='成都';
						data[1]["name"]=listpxj[i].pxdd;
						data[1]["value"]=listpxj[i].pxsj;
						BJData[BJData.length]= data;	
					}
				}
			}
      /*  var BJData = [
        	    [{'name':'成都'}, {'name':'上海','value':95}],
        	    [{'name':'成都'}, {'name':'广州','value':90}]
        	];  */
        	var planePath = 'path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z';

        	var convertData = function (data) {
        	    var res = [];
        	    for (var i = 0; i < data.length; i++) {
        	        var dataItem = data[i];
        	        var fromCoord = geoCoordMap[dataItem[0].name];
        	        var toCoord = geoCoordMap[dataItem[1].name];
        	        if (fromCoord && toCoord) {
        	            res.push({
        	                fromName: dataItem[0].name,
        	                toName: dataItem[1].name,
        	                coords: [fromCoord, toCoord]
        	            });
        	        }
        	    }
        	    return res;
        	};

        	var color = ['#ff4500'];
        	var series = [];
        	var item = ['成都', BJData];
        	
        	    series.push({
        	        name: item[0] + ' Top10',
        	        type: 'lines',
        	        zlevel: 1,
        	        effect: {
        	            show: true,
        	            period: 6,
        	            trailLength: 0.7,
        	            color: '#fff',
        	            symbolSize: 3
        	        },
        	        lineStyle: {
        	            normal: {
        	                color: color[0],
        	                width: 0,
        	                curveness: 0.2
        	            }
        	        },
        	        data: convertData(item[1])
        	    },
        	    {
        	        name: item[0],
        	        type: 'lines',
        	        zlevel: 2,
        	        symbol: ['none', 'arrow'],
        	        symbolSize: 10,
        	        effect: {
        	            show: true,
        	            period: 6,
        	            trailLength: 0,
        	            symbol: planePath,
        	            symbolSize: 15
        	        },
        	        lineStyle: {
        	            normal: {
        	                color: color[0],
        	                width: 1,
        	                opacity: 0.6,
        	                curveness: 0.2
        	            }
        	        },
        	        data: convertData(item[1])
        	    },
        	    {
        	        name: item[0] + ' Top10',
        	        type: 'effectScatter',
        	        coordinateSystem: 'geo',
        	        zlevel: 2,
        	        rippleEffect: {
        	            brushType: 'stroke'
        	        },
        	        label: {
        	            normal: {
        	                show: true,
        	                position: 'right',
        	                formatter: '{b}'
        	            }
        	        },
        	        symbolSize: function (val) {
        	            return val[2] / 8;
        	        },
        	        itemStyle: {
        	            normal: {
        	                color: color[0]
        	            }
        	        },
        	        data: item[1].map(function (dataItem) {
        	            return {
        	                name: dataItem[1].name,
        	                value: geoCoordMap[dataItem[1].name].concat([dataItem[1].value])
        	            };
        	        }), mapLocation:{
        	               x:'10',
        	               y:'10',
        	               width:'100%',
        	               height:'100%'
        	             }
        	    });

        	option = {
        	    backgroundColor: '#F5FFFA',
        	    title : {
        	        text: '',
        	        subtext: '',
        	        left: 'center',
        	        textStyle : {
        	            color: '#fff'
        	        }
        	    },
        	    tooltip : {
        	        trigger: 'item'
        	    },
        	legend: {
        	        orient: 'vertical',
        	        top: 'bottom',
        	        left: 'right',
        	        data:['成都'],
        	        textStyle: {
        	            color: '#fff'
        	        },
        	        selectedMode: 'single'
        	    }, 
        	    geo: {
        	        map: 'china',
        	        label: {
        	            emphasis: {
        	                show: false
        	            }
        	        },
        	        roam: false,
        	        itemStyle: {
        	            normal: {
        	                areaColor: '#8fd1e8',
        	                borderColor: '#fff'
        	            },
        	            emphasis: {
        	                areaColor: '#fbdd08'
        	            }
        	        },mapLocation: { x: 'left', y: 'top', height: '100%', width: '100%' }
        	        
        	    },
        	    series: series,
        	    mapLocation: { x: 'left', y: 'top', height: '100%', width: '100%' }
        	};
        	// 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
            </script>
     </c:if>
  </c:if>

</body>

</html>

