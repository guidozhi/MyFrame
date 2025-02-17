<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.lsts.inspection.bean.InspectionInfo"%>
<%@page import="com.lsts.inspection.dao.InspectionInfoDao"%>
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
ApplicationContext ctx =  WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
InspectionInfoDao infoDao = (InspectionInfoDao)ctx.getBean("inspectionInfoDao");
InspectionInfo info = infoDao.get(infoId);
String unit_id = "";
if(info!=null){
	unit_id = info.getCheck_unit_id();
}

String auditOpSql = null;
if("100069".equals(unit_id) 
		|| "100090".equals(unit_id)  
		|| "100091".equals(unit_id))
{
	auditOpSql = "select t.id, t.pid, t.code, t.text from (select o.id as id, o.id as code, replace(o.org_code, 'j', 'a') as tcode, o.ORG_NAME as text, o.PARENT_ID as pid "
		         +" from sys_org o where o.id = '100091' union select e.id as id,  e.id as code, replace(e.code, 'j', 'a') as tcode, e.NAME as text, t1.ORG_ID as pid "
		         +" from employee e, sys_user t1, EMPLOYEE_PERMISSIONS ep "
		         +" where e.id = t1.employee_id and e.id = ep.fk_employee_id and ep.is_audit_man = '0' and t1.status = '1' "
		         +" and t1.id in (select s.user_id from sys_user_role s, Sys_Role r where r.id = s.role_id and r.name = '报告审核') "
		         +" union select e2.id as id, e2.id as code, replace(e2.code, 'j', 'a') as tcode, e2.NAME as text, p2.ORG_ID as pid "
		         +" from employee   e2, sys_user  t2, SYS_EMPLOYEE_POSITION p1, SYS_POSITION p2, EMPLOYEE_PERMISSIONS  ep2 "
		         +" where e2.id = t2.employee_id and p1.position_id = p2.id and p1.employee_id = e2.id and (p2.org_id = '100091') "
		         +" and e2.id = ep2.fk_employee_id and ep2.is_audit_man = '0' and t2.status = '1' and t2.id in (select s.user_id "
		         +" from sys_user_role s, Sys_Role r  where r.id = s.role_id and r.name = '报告审核')) t "
		 		 +" start with t.id in ('100091') connect by t.pid = prior t.id ORDER BY T.TCODE ";
}else{
	String basesql =  "select t.id, t.pid, t.code, t.text  "+
			 "from (select o.id as id, "+
		      "           o.id as code, "+
		      "           replace(o.org_code, 'j', 'a') as tcode, "+
		      "           o.ORG_NAME as text, "+
		      "           o.PARENT_ID as pid "+
		      "      from sys_org o "+ 
		      "     where o.id != '100049' "+
		     "     union "+
		     "     select e.id as id, "+
		      "           e.id as code, "+
		      "           replace(e.code, 'j', 'a') as tcode, "+
		       "          e.NAME as text, "+
		      "           t1.ORG_ID as pid "+
		      "      from employee e, sys_user t1, EMPLOYEE_PERMISSIONS ep "+
		      "     where e.id = t1.employee_id "+
		      "       and e.id = ep.fk_employee_id "+
		      "       and ep.is_audit_man = '0' "+
		       "      and t1.status = '1' "+
		       "      and t1.id in (select s.user_id "+
		      "                       from sys_user_role s, Sys_Role r "+
		      "                      where r.id = s.role_id "+
		      "                        and r.name = '报告审核') "+
		      "    union "+
		      "    select e2.id as id, "+
		       "          e2.id as code, "+
		       "          replace(e2.code, 'j', 'a') as tcode, "+
		      "           e2.NAME as text, "+
		      "           p2.ORG_ID as pid "+
		     "       from employee              e2, "+
		     "            sys_user              t2, "+
		     "            SYS_EMPLOYEE_POSITION p1, "+
		    "             SYS_POSITION          p2, "+
		     "            EMPLOYEE_PERMISSIONS  ep2 "+
		     "      where e2.id = t2.employee_id "+
		    "         and p1.position_id = p2.id "+
		    "         and p1.employee_id = e2.id "+
		    "         and e2.id = ep2.fk_employee_id "+
		    "         and ep2.is_audit_man = '0' "+
		    "         and t2.status = '1' "+
		    "         and t2.id in (select s.user_id "+
		    "                         from sys_user_role s, Sys_Role r "+
		  "                          where r.id = s.role_id "+
		  "                            and r.name = '报告审核')) t "+
		 "  start with t.id in ('100020', "+
		  "                     '100021', "+
		  "                     '100022', "+
		  "                     '100023', "+
		  "                     '100024', "+
		   "                    '100026', "+
		   "                    '100034', "+
	 	   "                    '100035', "+
		    "                   '100033', "+
		     "                  '100036', "+
		    "                   '100037', "+
		     "                  '100062', "+
		    "                   '100045', "+
		    "                   '100066', "+
		    "                   '100063', "+
		    "                   '100065', "+
		    "                   '100067', "+
		    "                   '100090', "+
		    "                   '100091') "+
		 " connect by t.pid = prior t.id "+
		 "  ORDER BY T.TCODE ";
	auditOpSql = "select code id,text from ( " + basesql + ") v where v.pid = '"+unit_id+"'";
}


String inspectOpSql  = "select e.id,e.name from employee e, tzsb_inspection_info i where i.check_op_id like '%'||e.id||'%'  and i.id='" + infoId + "'";
%>


<% 

String code = request.getParameter("code");
String pageName = request.getParameter("pageName");
String version = request.getParameter("version");

//String pagePath = "/rtbox/app/templates/"+code+"/"+version+"/"+pageName;

%>
<%@include file="/rtbox/public/base.jsp"%>
 <link href="rtbox/app/templates/default/tpl.css" rel="stylesheet" />
 <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<style type="text/css">
		body, table, tr, th, td, input { margin: 0; padding: 0;}
		.dtitle { text-align: center; font-weight: bold; font-size:24px; font-family: 'simsun'; }
		#layout2 {width: 90%; font-family: 'simsun'; }
		#layout2 table { position: relative; font-size: 14px; table-layout: fixed; vertical-align: top; border-collapse: collapse!important; width: 90%; }
		#layout2 table tr{min-height:7mm;height:auto; }
		#layout2 table.two { border: 1px solid #000; cellpadding:1; cellspacing:1; }
		#layout2 table.two td { border: 1px solid #000; }
		#layout2 table.l-checkboxlist-table { border: 0px solid #fff; cellpadding:1; cellspacing:1; }
		#layout2 table.l-checkboxlist-table td { border: 0px solid #fff; }
		
		#layout2 table.two td.noborder1 {border-right:0px solid #fff!important;}
		#layout2 table.two td.noborder2 {border-left:0px solid #fff!important;}
		#layout2 table p { margin: 0;width: 90%; text-align: center; }
		#layout2 table input {height: 1.4em;padding: 0px; }
		 #layout2 table input.iptw2 {width:72%;}
		#layout2 table input:focus { outline: 0 none; background: rgba(0,0,0,.02); border: 1px solid #ccc; }
		#layout2 table textarea { width: 90%;border: 1px solid #fff;}
		#layout2 table textarea:focus { outline: 0 none; background: rgba(0,0,0,.02); border: 1px solid #ccc; }
		#layout2 table.l-checkboxlist-table input { width:20px}
		#layout2 table p.bianma {text-align:right;}
		.l-text{height:100%;}
		.l-text-field{position:static!important;width:100%}
		.l-text-wrapper{width:95%}
		#docx4j_tbl_4 tr td:nth-child(1){  text-align: left;}
		#docx4j_tbl_4 tr td:nth-child(1) p{  text-align: left;}
		#docx4j_tbl_4 tr td:nth-child(1) p span{  text-align: left;}
		#docx4j_tbl_4 tr td:nth-child(1) span{  text-align: left;}
		.error{
			float:right;
			position: relative;
		}
		</style>
<script type="text/javascript" src="rtbox/app/templates/default/tpl_photo2.js"></script>
<script src="rtdroc/app/draw/util/RtDraw.js"></script>
<script type="text/javascript" src="rtbox/public/js/page-data.js"></script>
<script type="text/javascript">
	var markOptions = <u:dict code="problem_nature_type"></u:dict>;
	var fk_report_id = "${param.fk_report_id}";
	var input = "${param.input}";
	var code_ext = "${param.code_ext}";
	var info_id="${param.info_id}"; //ZQ EDIT 0827 
	var pageStatus = "${param.pageStatus}";
	var check_op ="${param.check_op}";
	var relColumn='fk_report_id=${param.fk_report_id}&fk_inspection_info_id=${param.fk_inspection_info_id}';
	var rtboxId = "${param.rtPageId}";
	var rtboxCode = "${param.code}";
	var modelType = "report";
	var isBatchBusiness = "${param.isBatchBusiness}";
	var pageCode = "${param.pageCode}"
	var code_ext = null;
	if(pageCode.split("__").length>1){
		code_ext = pageCode.split("__")[1];
	}
	
	var auditors = <u:dict sql="<%=auditOpSql%>"></u:dict>;
	var checkers = <u:dict sql="<%=inspectOpSql%>"></u:dict>;
	$(function() {
		if(isBatchBusiness){
			$("#formObj").attr("action","inspection/zzjd/reportInfoInputNew.do");
			$("#formObj").attr("getAction","inspection/zzjd/reportInfoLoadNew.do");
		}
		/* if(paramss.mobile!=1&&paramss.mobile!=2){
			rtboxId = parent.rtboxId;
		} */
		//页面布局
		/* $("#layout2").ligerLayout({
			rightWidth : 150,
			space : 3,
			allowTopResize : false
		}); */
		//先查询模板内容再渲染
		getPageData(rtboxId,"${param.pageCode}",function(data){
				//console.log(res.data)
			$("#layout1").html(data);
				
				
			//处理图片
			initPicture();
			//初始化选择操作事件
			initSlectTool();
				
			$("form").ligerForm();
			
			try {
				//检验人员
				$("#base__inspect_op").ligerGetComboBoxManager().setData(checkers);
				
			} catch (e) {
				console.error("setData:err="+e.message)
			}
			try {
				//审核人
				$("#base__audit_op").ligerGetComboBoxManager().setData(auditors);
				
			} catch (e) {
				console.error("setData:err="+e.message)
			}
			initForm();
	
			
			var width = $("#layout2").width();
			$(".l-layout-center").css("width",width)
			if("${param.print}"=='1'){
				printPreviewForm();
			}
			
			$(".checkboxDiv").ligerCheckBoxList();
			
			try {
				changeCheckBoxDIV();
			} catch (e) {
				// TODO: handle exception
			}
			
			//设置复选框默认值
			setligerCheckBoxListInitValue();
			
			
			
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
<script type="text/javascript" src="rtbox/app/templates/default/tpl02.js"></script>
<!-- <script type="text/javascript" src="rtbox/app/templates/default/jquery.js"></script> -->
<script type="text/javascript" src="rtbox/app/templates/default/SelectTool.js"></script>
<script type="text/javascript" src="rtbox/app/templates/default/Label.js"></script>

<script type="text/javascript" src="rtbox/public/js/browser.js"></script>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>

<script type="text/javascript" src="rtbox/app/templates/default/enter.js"></script>
</head>
<body>
	<div id="layout" style="width: 99.8%">
	<input type="hidden" id="fk_report_id" name="fk_report_id" value="${param.fk_report_id}">
	<input type="hidden" id="fk_inspection_info_id" name="fk_inspection_info_id" value="${param.fk_inspection_info_id}">
	<input type="hidden" id="inputFocus" name="inputFocus" />
	<input type="hidden" id="code_ext" name="code_ext"  value="${param.code_ext}"/> 
	<div position="center" style="overflow: auto;width: 100%" align="center">
		<form id="formObj" action="reportItemValueAction/saveMap.do"
					getAction="reportItemValueAction/detailMap.do">
			
			<%-- <jsp:include page="<%=pagePath %>"></jsp:include> --%>
			<div id="layout1"  class="a4-main " style="width: 100%;height: 100%" >
			</div>
		</form>
	</div>
	</div>
 <OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="0"></OBJECT>
</body>
</html>
