<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检验报告</title> 
<%

String infoId = request.getParameter("info_id");
if(infoId==null){
	infoId = request.getParameter("fk_inspection_info_id");
}
String inspectOp  = "select u.id,u.name from employee u, tzsb_inspection_info info where info.check_op_id like '%'||u.id||'%'  and info.id='" + infoId + "'";
//String auditOp = "select u.id,u.name from sys_user u,sys_user_role ur,sys_role r where r.id = ur.role_id and ur.user_id = u.id and r.name like '%电梯报告审核%'";

StringBuffer sql = new StringBuffer();
sql.append("	with classify as	");
sql.append("	 (select decode(cla.big_classify_code,	");
sql.append("	                '1000',	");
sql.append("	                '锅炉报告审核',	");
sql.append("	                '2000',	");
sql.append("	                '容器报告审核',	");
sql.append("	                '3000',	");
sql.append("	                '电梯报告审核',	");
sql.append("	                '4000',	");
sql.append("	                '起重报告审核',	");
sql.append("	                '5000',	");
sql.append("	                '厂车报告审核',	");
sql.append("	                '6000',	");
sql.append("	                '游乐报告审核',	");
sql.append("	                '7000',	");
sql.append("	                '管道元件报告审核',	");
sql.append("	                '8000',	");
sql.append("	                '管道报告审核',	");
sql.append("	                '7310',	");
sql.append("	                '安全阀报告审核') text	");
sql.append("	    from TZSB_INSPECTION_INFO info,	");
sql.append("	         BASE_DEVICE_DOCUMENT doc,	");
sql.append("	         BASE_DEVICE_CLASSIFY cla	");
sql.append("	   where info.fk_tsjc_device_document_id = doc.id	");
sql.append("	     and doc.device_sort_code = cla.device_sort_code	");
sql.append("	     and info.id = '").append(infoId).append("')	");
sql.append("		");
sql.append("	select t1.id code, t1.name text	");
sql.append("	  from sys_user t1	");
sql.append("	 where t1.id in (select s.user_id	");
sql.append("	                   from sys_user_role s, Sys_Role e	");
sql.append("	                  where e.id = s.role_id	");
sql.append("	                    and e.name = (select text from classify ))	");

String auditOp =  sql.toString();
System.out.println(sql.toString());
%>

<% 

String code = request.getParameter("code");
String pageName = request.getParameter("pageName");
String pagePath = "/rtbox/app/templates/"+code+"/"+pageName;

%>
<%@include file="/rtbox/public/base.jsp"%>
 <link href="rtbox/app/templates/default/tpl.css" rel="stylesheet" />
 <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<script type="text/javascript" src="rtbox/app/templates/default/tpl02.js"></script>
<!-- <script type="text/javascript" src="rtbox/app/templates/default/jquery.js"></script> -->
<script type="text/javascript" src="rtbox/app/templates/default/SelectTool.js"></script>
<script type="text/javascript" src="rtbox/app/templates/default/Label.js"></script>
<script type="text/javascript" src="rtbox/public/js/browser.js"></script>
<script type="text/javascript" src="rtbox/app/templates/default/enter.js"></script>
<script type="text/javascript" src="rtbox/app/templates/default/tpl_photo2.js"></script>
<script type="text/javascript" src="rtbox/public/js/main-k.js"></script>

<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>

<script type="text/javascript">
	var markOptions = <u:dict code="problem_nature_type"></u:dict>;
	var fk_report_id = "${param.fk_report_id}";
	var input = "${param.input}";
	var code_ext = "${param.code_ext}";
	var info_id="${param.info_id}"; //ZQ EDIT 0827 
	var pageStatus = "${param.pageStatus}";
	var check_op ="${param.check_op}";
	var relColumn='fk_report_id=${param.fk_report_id}&fk_inspection_info_id=${param.fk_inspection_info_id}';


	//检验员选项
	var inspectOpData  = <u:dict sql="<%=inspectOp%>"></u:dict>;
	//审核人员选项
	var auditOpData  = <u:dict sql="<%=auditOp%>"></u:dict>;
	
	
	$(function() {
		//页面布局
		/* $("#layout2").ligerLayout({
			rightWidth : 150,
			space : 3,
			allowTopResize : false
		}); */

		$("form").ligerForm();
		
		initOpSelectInput();
		
		initForm();
		//显示二维码
		getTwoDimNewImg(info_id);
		
		
		var width = $("#layout2").width();
		$(".l-layout-center").css("width",width)
		if("${param.print}"=='1'){
			printPreviewForm();
		}
		//加载复选框
		$(".checkboxDiv").ligerCheckBoxList();
		
		//设置复选框默认值
		setligerCheckBoxListInitValue();
	});

	
	function printPreviewForm(){
		
		if("${param.print}"=='1'){
			
			if(_browser == "IE"){
				wb.execwb(7, 1);
			}else{
				window.print();
			}
		}else{
			
				window.open(window.location.href+"&print=1");
			
			
		}
		//$('body').printPreview();
		//
		//$.printPreview.loadPrintPreview();
	}
	

	//显示二维码
	function getTwoDimNewImg(infoId){

		try {
			if(infoId==null||infoId==""){
				return;
			}
			$.post("reportItemValueAction/getWxQrCodeByInfoId.do",{"infoId":infoId},function(res){
				if(res.qrCodeId!=null&&res.qrCodeId!=""){

					$("#qrCodeImg").append('<img src="fileupload/downloadByObjId.do?id='+res.qrCodeId+'"/>')
				}

			})

		} catch (e) {
			// TODO: handle exception
		}

	}

	//A4分割线JS
	function asldkfjwoef() {
		var bc = typeof BROWSER_INFO != "undefined" ? BROWSER_INFO : getBrowserInfo();
		var ie = bc.ie,
			xt = bc.system,
			xtms = bc.systemx,
			dm = bc.docMode,
			bsType=bc.type,
			version=bc.version,
			iever = bc.ieversion;
		//alert([bsType,version])
		if (bsType=="firefox") {
			var str1='<style>.a4-endwise{height:1444px;border:1px solid #ff0000;}</style>';
			//document.write(str1);
		}
		if (bsType=="opera") {
			var str1='<style>.a4-endwise{height:1300px;border:1px solid #ff0000;}</style>';
			//document.write(str1);
		}
		if (ie || bsType=="edge") {
			var str1='<style>.a4-endwise{height:1628px;}</style>';
			//document.write(str1);
			if (ie && parseFloat(iever) <= 7) { //浏览器检测 2012年06月19日 星期二 14:04:54 lybide
				/*var str1 = '提示：您的浏览器版本太低（Internet Explorer ' + iever + '），请立即升级您的浏览器。推荐使用：<a href="http://www.microsoft.com/ie" target="_blank" style="color:#ffff00">（最新IE浏览器）</a>。';
				if ((xt == "win7" || xt == "win8") && iever <= 7) {
					str1 = '提示：请按键 F12，选择“浏览器模式”，必须设置至 Internet Explorer 8 或以上。'
				}
				$("body").append('<div id="bosCk1" style="border:1px solid #000000;background:#ff0000;padding:15px;position:absolute;top:10px;right:10px;z-index:55;color:#ffffff;">' + str1 + '<a href="javascript:void(0);" onclick="$(\'#bosCk1\').hide();" style="color:#ffffff">[关闭此提示]</a></div>');
				if (parseFloat(iever)<=6) {
					try {document.execCommand("BackgroundImageCache", false, true);} catch(e) {} //ie6不缓存背景图片
					DD_belatedPNG.fix("*");
				}*/
			}; //alert([iever,dm])
			if (ie && (parseInt(iever) > 6 && parseInt(iever) < 11) && (7 > parseInt(dm))) {
				/*//alert("亲爱的，请不要使用ie怪义模式嘛");
				//$("body").append('<div id="bosCk2" class="l-ie-ms1">尊敬的用户，您当前的浏览器模式不正常，请按键盘上的 F12 键，简单设置一下即可。请选择：“浏览器模式”与“文档模式”，两种模式必须一致，建议设置至最高模式。<a href="javascript:void(0);" onclick="$(\'#bosCk2\').hide();" style="color:#ffffff">[关闭此提示]</a></div>');
				winOpen({
					id: "sss",
					content: '<img src="'+kui["kuiBase"]+'kui/images/iebug1.gif" border="0"/>',
					width: 750,
					height: 500,
					lock: true,
					title: "友情提示",
					max: false,
					min: false
				});*/
			}
		}
	}
	asldkfjwoef();

	function right(mainStr,lngLen) {
		if(mainStr.length-lngLen>=0 && mainStr.length>=0 && mainStr.length-lngLen<=mainStr.length){ 
			return mainStr.substring(mainStr.length-lngLen,mainStr.length)
		} else {
			return null;
		} 
	} 

	$(function(){
		$(".cont_center").height(1075);
		//打开横屏
		$(".container").removeClass("a4-endwise").addClass("a4-broadwise");
		//生成数据
		//for (var i = 0, l=168; i < l; i++) {
			//$(".a4-main").append('<p>打印测试 成都川大科鸿新技术研究所 '+right("000000"+i,6)+'</p>')
		//}
		//监控页面 2018年09月28日 16:22:48 星期五 lybide
		if (pageStatus != undefined &&pageStatus != ""&& pageStatus == "detail") {
			
		}else{
			var loopExe1=setInterval(function(){
				pageA4Line();
			},2000);
		}
		
		pageA4Line();
	});

	function pageA4Line() {
		var bc = typeof BROWSER_INFO != "undefined" ? BROWSER_INFO : getBrowserInfo();
		var ie = bc.ie,
			xt = bc.system,
			xtms = bc.systemx,
			dm = bc.docMode,
			bsType=bc.type,
			version=bc.version,
			iever = bc.ieversion;
		var w=document.body.offsetWidth;//$(window).width()+$(window).scrollLeft();
		//var h=document.body.offsetHeight;//$(window).height()+$(window).scrollTop();
		var h = $("#formObj").height();
		//console.log(h);
		var a4MainEndwise=$(".a4-main:first").parent().hasClass("a4-endwise");
			if(a4MainEndwise){
				var contentHeight = 1075*(938/660) + 52;
					if (bsType=="firefox") {
						contentHeight=contentHeight+550;
					}
					if (bsType=="opera") {
						contentHeight=contentHeight-10;
					}
					if (ie || bsType=="edge") {
						contentHeight=contentHeight+48;
					}
				//console.log(contentHeight,22)
			}else{
				var contentHeight = 1569*(934/1324)-10;
				if (bsType=="firefox") {
						contentHeight=contentHeight+120;
					}
					if (bsType=="opera") {
						contentHeight=contentHeight-10;
					}
					if (ie || bsType=="edge") {
						contentHeight=contentHeight-30;
					}
			//	console.log(contentHeight,23)
			}
//		if (a4MainBroadwise) {
//			var contentHeight=1075;
//			if (bsType=="firefox") {
//				contentHeight=1000;
//			}
//			if (bsType=="opera") {
//				contentHeight=1000;
//			}
//			if (ie || bsType=="edge") {
//				contentHeight=1047;
//			}
//		} else {
//			var contentHeight=1567;
//			if (bsType=="firefox") {
//				contentHeight=1444;
//			}
//			if (bsType=="opera") {
//				contentHeight=1300;
//			}
//			if (ie || bsType=="edge") {
//				contentHeight=1630;
//			}
//		}
		if (h>contentHeight) {
			if ($("#a4-line-main1").size()>0) {
				
			} else {
				$("#formObj").append('<div id="a4-line-main1" class="a4-line-main" style="top:'+contentHeight+'px;"><div class="a4-line-no">'+1+'</div></div>');
			}
		}
		var hwiw=parseInt(h/contentHeight) + 1;
		//console.log(h,contentHeight,hwiw);//调试信息
		if (hwiw>1) {
			for (var i = 2, l=hwiw; i < l; i++) {
				if ($("#a4-line-main"+i).size()>0) {
				} else {
					if(!a4MainEndwise){
						var cont = contentHeight*(i);
						if (ie || bsType=="edge") {
						var cont = contentHeight*(i)-2*(i-1) ;
						}
						if (bsType=="firefox") {
						var cont = contentHeight*(i)+20*(i-1);
					}
					}else{
						var cont = contentHeight*(i) -10*(i-1);
					if (bsType=="firefox") {
						var cont = contentHeight*(i)-40;
					}
					if (bsType=="opera") {
						contentHeight=contentHeight-10;
					}
					if (ie || bsType=="edge") {
						var cont = contentHeight*(i)+8*(i-1) ;
					}
					}
					$("body").append('<div id="a4-line-main'+i+'" class="a4-line-main" style="top:'+cont+'px;"><div class="a4-line-no">'+(i)+'</div></div>');
				}
				
			}
		}
	}

</script>
<style type="text/css">
		body, table, tr, th, td, input { margin: 0; padding: 0; }
		.dtitle { text-align: center; font-weight: bold; font-size:24px; font-family: 'simsun'; }
		#layout2 {width: 90%; font-family: 'simsun';background-color:#fff; }
		#layout2 table { position: relative; font-size: 14px; table-layout: fixed; vertical-align: top; border-collapse: collapse!important; width: 90%; }
		#layout2 table tr{min-height:6mm;height:auto; }
		#layout2 table.two { border: 1px solid #000; cellpadding:1; cellspacing:1; }
		#layout2 table.two td { border: 1px solid #000; }
		#layout2 table.l-checkboxlist-table { border: 0px solid #fff; cellpadding:1; cellspacing:1; }
		#layout2 table.l-checkboxlist-table td { border: 0px solid #fff; }
		/* .cont_center{width:1075px ;} */
		#layout2 table.two td.noborder1 {border-right:0px solid #fff!important;}
		#layout2 table.two td.noborder2 {border-left:0px solid #fff!important;}
		#layout2 table p { margin: 0;width: 90%;}
		#layout2 table input {width:90%;padding: 0px;text-align: center;padding: 0px;    }
		 #layout2 table input.iptw2 {width:72%;}
		#layout2 table textarea { width: 90%;border: 1px solid #fff;}
		#layout2 table.l-checkboxlist-table input { width:20px}
		#layout2 table p.bianma {text-align:right;}
		.l-text{height:100%;}
		.l-text-field{position:static!important;width:100%}
		.l-text-wrapper{width:95%}
		#docx4j_tbl_4 tr td:nth-child(1){  text-align: left;}
		#docx4j_tbl_4 tr td:nth-child(1) p{  text-align: left;}
		#docx4j_tbl_4 tr td:nth-child(1) p span{  text-align: left;}
		#docx4j_tbl_4 tr td:nth-child(1) span{  text-align: left;}
		#formObj{position:relative;}
		body {padding:0;margin:0;background:#e6e6e6;font-size:12px;font-family:"微软雅黑","Microsoft yahei","宋体","Lucida Grande","Verdana","Helvetica,Arial","Georgia";}
		.error{
			float:right;
			position: relative;
		}
.container {margin:0px 0;position:relative;}
.a4-endwise{/* width: 1075px; *//*height: 1565px;*/border: 0px #FF0000 solid;/*overflow: hidden;*/padding: 0;word-break:break-all;margin-left:auto;margin-right:auto;}
.a4-broadwise{width: 100%;/*height: 1073px;*/border: 0px #FF0000 solid;overflow: hidden;padding: 0;word-break:break-all;margin-left:auto;margin-right:auto;}
.a4-main {background:#ffffff;box-shadow:0px 0px 10px 0px #8E8E8E;}
.a4-line-main {position:absolute;width:100%;border-bottom:1px solid #0000FF;z-index:111;}
.a4-line-no {position:absolute;right:10px;top:-30px;}
.print{position: fixed;top: 1%;right: 10%;}

@media print {
	body {background:#ffffff;}
	.a4-endwise,.a4-broadwise {border:0px;}
	.a4-line-main {display:none;}
	.a4-main {box-shadow:none!important;border:0px;}
}
	
@media screen and (max-width: 1000px){
  .cont_center{width:80% important!;}
  .a4-line-main{display:none;}
}
		</style>
</head>
<body>
	<div id="layout2" style="width: 99.8%">
		<input type="hidden" id="inputFocus" name="inputFocus" />
		 
		<div position="center" style="overflow-y:auto;width: 100%" align="center">
			<form id="formObj" action="reportItemValueAction/saveMap.do"
						getAction="reportItemValueAction/detailMap.do" >
				<input type="hidden" id="code_ext" name="code_ext"  value="${param.code_ext}"/> 
				<div class="container a4-endwise" style="height: 100%;">
					<div class="a4-main" style="height: 100%;"><jsp:include page="<%=pagePath %>"></jsp:include></div>
				</div>
				
			</form>
		</div>
	</div>
	 <OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="0"></OBJECT>
</body>
</html>
