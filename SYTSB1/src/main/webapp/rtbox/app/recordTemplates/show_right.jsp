<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检验原始记录</title> <% String infoId = request.getParameter("info_id");
String inspect_op402880ee5e123818015e1240e9d60003 = "select u.id,u.name from sys_user u, tzsb_inspection_info info where info.check_op_id like '%'||u.id||'%'  and info.id='"+infoId+"'"; 
String audit_op402880ee5e174aa8015e1766c7ed0000 = "select u.id,u.name from sys_user u,sys_user_role ur,sys_role r where r.id = ur.role_id and ur.user_id = u.id and r.name like '%容器审核%'";  String inspect_op402880485ea7db57015ea869c77c000a = "select u.id,u.name from sys_user u, tzsb_inspection_info info where info.check_op_id like '%'||u.id||'%'  and info.id='"+infoId+"'";   String inspect_op402880ee5e4a71ac015e4aeab46e0003 = "select u.id,u.name from sys_user u, tzsb_inspection_info info where info.check_op_id like '%'||u.id||'%'  and info.id='"+infoId+"'";   String audit_op402880ee5e4a71ac015e4b4815730007 = "select u.id,u.name from sys_user u,sys_user_role ur,sys_role r where r.id = ur.role_id and ur.user_id = u.id and r.name like '%电梯报告审核%'";  String audit_op402880ee5e174aa8015e17d0b3ad0028 = "select u.id,u.name from sys_user u,sys_user_role ur,sys_role r where r.id = ur.role_id and ur.user_id = u.id and r.name like '%容器审核%'";

String code = request.getParameter("code");
String pageName = request.getParameter("pageName");
String pagePath = "/rtbox/app/recordTemplates/"+code+"/"+pageName;

%>
<%@include file="/rtbox/public/base.jsp"%>
 <link href="rtbox/app/templates/default/tpl.css" rel="stylesheet" />
 <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
  <script type="text/javascript" src="rtbox/public/js/page-data.js"></script>
<script type="text/javascript">
	var markOptions = <u:dict code="problem_nature_type"></u:dict>;
	var fk_report_id = "${param.fk_report_id}";
	var input = "${param.input}";
	var pageCode = "${param.pageCode}"
	var code_ext = null;
	if(pageCode.split("__").length>1){
		code_ext = pageCode.split("__")[1];
	}
	var info_id="${param.info_id}"; //ZQ EDIT 0827 
	var pageStatus = "${param.pageStatus}";
	var check_op ="${param.check_op}";
	var relColumn='fk_report_id=${param.fk_report_id}&fk_inspection_info_id=${param.fk_inspection_info_id}';
	var modelType = "record";
	var rtboxId = "${param.rtPageId}";
	var rtboxCode = "${param.code}";

	$(function() {
		/* if(paramss.mobile!=1&&paramss.mobile!=2){
			rtboxId = parent.rtboxId;
		} */
		//页面布局
	//先查询模板内容再渲染
		 getPageData(rtboxId,pageCode,function(data){
				//console.log(res.data)
			$("#layout1").html(data);
			$("form").ligerForm();
			initForm();
	
			
			//稍后可修改
			$(".l-text").css("display", 'inline-block');
			
			$(".l-text-wrapper").css("width", '95%');
			$(".l-text-date").css("width", '95%');//日期框
			$(".l-text").css("width", '95%');
			$(".l-text").css("border-color", '#FFFFFF');
			//$(".l-text").css("background-color", '#CAE1FF');
			$("input").css("width", '95%');
			//$("input").css("background-color", '#CAE1FF');
			if("${param.print}"=='1'){
				printPreviewForm();
			}
			$(".checkboxDiv").ligerCheckBoxList();
			//设置复选框默认值
			setligerCheckBoxListInitValue();
			$("#fk_report_id").val("${param.fk_report_id}");
			
			
		}) 
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

</script>
 <script type="text/javascript" src="rtbox/app/recordTemplates/js/tpl02.js"></script>
 
<script type="text/javascript" src="rtbox/public/js/browser.js"></script>
<!-- <script type="text/javascript" src="rtbox/app/templates/default/jquery.js"></script> -->
<script type="text/javascript" src="rtbox/app/templates/default/SelectTool.js"></script>
<script type="text/javascript" src="rtbox/app/templates/default/Label.js"></script>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<script type="text/javascript" src="rtbox/app/templates/default/tpl_photo2.js"></script>
<script type="text/javascript" src="rtbox/app/templates/default/enter.js"></script>

<style type="text/css">
		body, table, tr, th, td, input { margin: 0; padding: 0; line-height: 16px;}
		.dtitle { text-align: center; font-weight: bold; font-size:24px; font-family: 'simsun'; }
		.wrapper {  width: 90%; font-family: 'simsun'; }
		.wrapper table { position: relative; font-size: 14px; font-family: 'SimSun';table-layout: fixed; vertical-align: top; border-collapse: collapse!important; width: 90%; }
		#layout2 table tr{min-height:6mm;height:auto; }
		.wrapper table.two { border: 1px solid #000; cellpadding:1; cellspacing:1; }
		.wrapper table.two td { border: 1px solid #000; }
		.wrapper table.l-checkboxlist-table { border: 0px solid #fff; cellpadding:1; cellspacing:1; }
		.wrapper table.l-checkboxlist-table td { border: 0px solid #fff; }
		
		.wrapper table.two td.noborder1 {border-right:0px solid #fff!important;}
		.wrapper table.two td.noborder2 {border-left:0px solid #fff!important;}
		.wrapper table p { margin: 0;  width: auto;}
		.wrapper table input { border: 1px solid #fff;text-align: center;padding: 0px;   }
		.wrapper table input.iptw2 {width:72%;}
		.wrapper table input:focus { outline: 0 none; background: rgba(0,0,0,.02); border: 1px solid #ccc; }
		.wrapper table textarea { width: 96%;border: 1px solid #fff; }
		.wrapper table textarea:focus { outline: 0 none; background: rgba(0,0,0,.02); border: 1px solid #ccc; }
		.wrapper table.l-checkboxlist-table input { width:20px}
		.wrapper table p.bianma {text-align:right;}
		.l-text{height:100%;}
		.l-text-field{position:static!important;width:100%}
		.l-text-wrapper{width:95%}
</style>
</head>

<body>
	<form id="formObj" action="report/item/record/saveMap.do" getAction="report/item/record/getInspData.do">
	 <input type="hidden" id="code_ext" name="code_ext"  value="${param.code_ext}"/> 
	 <div id="layout1"  class="a4-main " style="width: 100%;height: 100%" >
				</div>
		<%-- <jsp:include page="<%=pagePath %>"></jsp:include> --%>
	</form>
</body>
</html>
