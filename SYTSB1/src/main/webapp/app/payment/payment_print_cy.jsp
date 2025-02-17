<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.khnt.utils.DateUtil"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.scts.payment.bean.InspectionPayInfo"%>
<%@ page import="java.util.List"%>
<%@ page import="com.scts.payment.bean.InspectionInfoDTO"%>
<%@ page import="com.lsts.finance.bean.CwBankDTO"%>
<%@ page import="com.khnt.utils.StringUtil"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	InspectionPayInfo inspectionPayInfo = (InspectionPayInfo)request.getAttribute("inspectionPayInfo");
	String kpdw = inspectionPayInfo.getCompany_name();
	kpdw = new String(kpdw).trim();
	kpdw = kpdw.replaceAll("　", "");
	List<CwBankDTO> cwBankDTOList = inspectionPayInfo.getCwBankDTOList();
	Set<String> times = new HashSet<String>();
	boolean diffDw = false;
	if(!cwBankDTOList.isEmpty()){
		for(CwBankDTO cwBankDTO : cwBankDTOList){
			if(cwBankDTO != null){
				String zzdw = cwBankDTO.getAccountName();
				zzdw = new String(zzdw).trim();
				zzdw = zzdw.replaceAll("　", "");
				if(!kpdw.equals(zzdw)){
					diffDw = true;
				}
				if(cwBankDTO.getJyTime() != null){
					times.add(DateUtil.getDateTime("yyyy-MM-dd", cwBankDTO.getJyTime()));
				}
			}
			
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<%@ include file="/k/kui-base-form.jsp"%>
<script type="text/javascript" src="app/common/js/windowprint.js"></script>
<script type="text/javascript" src="app/common/lodop/LodopFuncs.js"></script>
<style type="text/css">
#table td{border:0px black solid}
table tr td{word-wrap:break-word;word-break:break-all;}
table tr td:first-child{width:100px;text-align:left;}
</style>
<script type="text/javascript">
	$(function() {
		$("#form1").initForm({ //参数设置示例
			toolbar : [ {
				text : '打印预览',
				id : 'prn_preview',
				icon : 'print',
				click:prn_preview
			}, {
				text : '打印',
				id : 'prn_print',
				icon : 'print',
				click : prn_print
			}, {
				text : '关闭',
				id : 'close',
				icon : 'cancel',
				click : close
			} ],
			getSuccess : function(res) {
			
			}
		});
	})
	
	function prn_preview() {	
		CreateOneFormPage();	
		LODOP.PREVIEW();	
	};
	
	function prn_print() {		
		CreateOneFormPage();
		LODOP.PRINT();
		close();	
	};
	
	function CreateOneFormPage(){		
		var xprq="${reportBorrow.operator_date}";
		if(xprq!=null){
			xprq=xprq.substring(0,10);
		}
		
			LODOP=getLodop();
			// 设置打印样式  
			var strBodyStyle="<style> table{border:2;text-align:center;margin-left:60px;} table,td { border: 0 solid #000000;border-collapse:collapse;font-size:12px } table tr td{word-wrap:break-word;word-break:break-all;} table tr td:first-child{width:100px;text-align:left;} span.money{display:inline-block;text-align:right;}</style>";
			// 设置打印方式
			LODOP.SET_PRINT_PAGESIZE(1, 0, 0,"B5");	// 1 纵向打印 2 横向打印
			// 设置打印页码
			var pageHtml = "<font style='font-size:12px' format='ChineseNum'><span tdata='pageNO'>第##页</span>/<span tdata='pageCount'>共##页</span></font>";
			// 打印页码（上边距、左边距、宽、高）
			LODOP.ADD_PRINT_HTM(0,550,300,20,pageHtml);	
			// 循环上一命令（页码）
			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
			
				var printReportContent = "";
				printReportContent += "<table width='600' cellpadding='0' cellspacing='0' align='center' border='0' id='table'>"
										+"<caption><center><font size='3'><b>四川省特种设备检验研究院<br/>检验收费结算单</b></font></center></caption>"
										+"<tr align='center' height='20px'>"
											+"<td>受检单位</td>"
											+"<td colspan='4'>${inspectionPayInfo.report_com_name}</td>"
											+"<td>合同号/编号</td>"
											+"<td>${inspectionPayInfo.pay_no}</td>"
										+"</tr>"
										+"<tr align='center' width='60' height='20px'>"
											+"<td>开票单位</td>"
											+"<td colspan='4'><%=kpdw %></td>"
											+"<td>检验部门</td>"
											+"<td>${inspectionPayInfo.check_dep_name}</td>"
										+"</tr>"
										+ "<tr align='center'><td colspan='7'></td></tr>"
										+ "<tr align='center'><td colspan='7'></td></tr>"
										+ "<tr height='20px'>"
											+"<td align='center'>合计</td>"
											+"<td>共${inspectionPayInfo.report_count}份报告</td>"
											+"<td>${inspectionPayInfo.pay_received}</td>";
				if("borrow" == "${param.type_1}"){
					printReportContent += "<td>借票日期</td><td  align='left'><%=inspectionPayInfo.getPay_date()==null?"":DateUtil.getDateTime("yyyy-MM-dd", inspectionPayInfo.getPay_date())%></td>";
				}else if("invoice" == "${param.type_1}"){
					printReportContent += "<td>开票日期</td><td  align='left'><%=inspectionPayInfo.getPay_date()==null?"":DateUtil.getDateTime("yyyy-MM-dd", inspectionPayInfo.getPay_date())%></td>"; 
				}
				printReportContent += "<td>消票日期</td><td>" ;
				if("${reportBorrow.borrow_status==3}"){
					printReportContent +=xprq;
				}
	
				printReportContent += "</td></tr>";
				printReportContent += "<tr align='left'>"
										+"<td>发票编号</td>"
										+"<td colspan='6' align='left'><%=inspectionPayInfo.getInvoice_no()%></td>"
							  		 +"</tr>"
							  		 +"<tr align='left' height='20px'>"
								  		 +"<td colspan='2'>部门负责人：</td>"
								  		 +"<td colspan='2'>监检人员：</td>"
								  		 +"<td style='font-size:13px' colspan='3'><b>经办人签字：</b></td>"
							  		 +"</tr>"
							  		 +"<tr align='left' height='20px'>"
								  		 +"<td colspan='2'>日期：</td>"
								  		 +"<td colspan='2'>日期：</td>"
								  		 +"<td style='font-size:13px；padding-right:50px' colspan='3'><b>电话：</b></td>"
							  		 +"</tr>";
				for (var i = 0; i < 6; i++) {
					printReportContent += "<tr height='20px'><tr>";
				}
				printReportContent += "<tr height='20px'>"
										+"<td colspan='7' style='position:relative;'>"
										+"<div style='width:100%;position:absolute;top:100px;bottom:20px;text-align:right;'><b>";
				var cash = parseFloat("${inspectionPayInfo.cash_pay}");
				var transfer = parseFloat("${inspectionPayInfo.remark}");
				var pos = parseFloat("${inspectionPayInfo.pos}");
				var handIn = parseFloat("${inspectionPayInfo.hand_in}");
				var draft = parseFloat("${inspectionPayInfo.draft}");
				if(cash!=0){
					printReportContent += "<div><span class='money' style='width:70%;'></span><span class='money' style='width:30%;'>现金:"+cash+"元</span></div>";
				}
				if(pos!=0){
					printReportContent += "<div><span class='money' style='width:70%;'></span><span class='money' style='width:30%;'>POS:"+pos+"元</span></div>";
				}
				
				if(transfer){
					printReportContent += "<div><span class='money' style='width:70%;'></span><span class='money' style='width:30%;'>转账:"+transfer+"元</span></div>";
					
					<%
					if(diffDw){
						%>
						printReportContent += "<font size='4'>▲</font>";
						<%
					}
					for(String time : times){
						%>
						printReportContent += "冲<%=time%><br>";
						<%
					}
					%>
					
				}
				if(handIn!=0){
					printReportContent += "<div><span class='money' style='width:70%;'></span><span class='money' style='width:30%;'>上缴财政:"+handIn+"元</span></div>";
				}
				if(draft!=0){
					printReportContent += "<div><span class='money' style='width:70%;'></span><span class='money' style='width:30%;'>承兑汇票:"+draft+"元</span></div>";
				}
				
				printReportContent += "</b></div></td></tr>  ";
				printReportContent += "<tr align='center' height='20px'><td colspan='7' style='text-align:center;border:none;'><div align='center' style=' position:absolute; top:400px;right:200px;bottom:20px;left:200px'>（<%=inspectionPayInfo.getCheck_dep_name()%>）<%=DateUtil.getDateTime("yyyy-MM-dd", new Date()) %></div></td></tr></table>";

				// 获取打印内容
				var strFormHtml=strBodyStyle+"<body>"+printReportContent+"</body>";
				LODOP.NewPage();	// 强制分页
				// 打印表格（上边距、左边距、宽、高、打印内容）
				LODOP.ADD_PRINT_TABLE(20,0,"100%","100%",strFormHtml);	
			
			
	}
	
	function doPrintPage(strBodyStyle, printReportContent){
		// 获取打印内容
		if(printReportContent!=""){
			printReportContent = printReportContent+"</table>";
		}
		var strFormHtml=strBodyStyle+"<body>"+printReportContent+"</body>";
		LODOP.NewPage();	// 强制分页
		LODOP.ADD_PRINT_TABLE(20,0,"100%","100%",strFormHtml);
	}
	
	function close(){	
		if("1" == "${param.type}"){
			api.data.pwindow.api.close();
			api.data.pwindow.api.data.window.refreshGrid();
		}else{
			api.data.window.refreshGrid();
		}
		api.close();
	}
</script>
</head>
<body >
<form id="form1" style="height:99%;">
	<div style="overflow:hidden;text-align:center" id='printPaymentDiv'>
		<table width="700" cellpadding="6" cellspacing="0" align="center" border="0" id="table">
			<caption><center><font size="3"><b>四川省特种设备检验研究院<br/>检验收费结算单</b></font></center></caption>
	       		<tr align="center">
	       			<td>受检单位</td>
	         		<td colspan="4"><c:out value="${inspectionPayInfo.report_com_name}"></c:out></td>
	         		<td>合同号/编号</td>
	              	<td><c:out value="${inspectionPayInfo.pay_no}"></c:out></td>
	       		</tr>	
	       		<tr align="center">
	       			<td>开票单位</td>
	         		<td colspan="4"><c:out value="${inspectionPayInfo.company_name}"></c:out></td>
	         		<td>检验部门</td>
	              	<td><c:out value="${inspectionPayInfo.check_dep_name}"></c:out></td>
	       		</tr>	
	       		<tr align="center">
	       			<td>序号</td>
	       			<!-- <td>报告/证书编号</td> -->
	       			<td>设备注册代码</td>
	       			<td>数量</td>
	       			<td>单位（台）</td>
	       			<td>监检费（元）</td>
	       			<td colspan="2">备注</td>
	       		</tr>    
	         	<tr align="center">
	            	<td>&nbsp;&nbsp;</td>  
	           		<td>&nbsp;&nbsp;</td>
	               	<td></td>
	              	<td></td>
	              	<td>&nbsp;&nbsp;</td>
	              	<td colspan="2"></td>
	        	</tr>
	        	<tr align="center">
	            	<td>&nbsp;&nbsp;</td>  
	           		<td>&nbsp;&nbsp;</td>
	               	<td></td>
	              	<td></td>
	              	<td>&nbsp;&nbsp;</td>
	              	<td colspan="2"></td>
	        	</tr>
	        	<tr align="center">
	            	<td>&nbsp;&nbsp;</td>  
	           		<td>&nbsp;&nbsp;</td>
	               	<td></td>
	              	<td></td>
	              	<td>&nbsp;&nbsp;</td>
	              	<td colspan="2"></td>
	        	</tr>
	        	<tr align="center">
	            	<td>&nbsp;&nbsp;</td>  
	           		<td>&nbsp;&nbsp;</td>
	               	<td></td>
	              	<td></td>
	              	<td>&nbsp;&nbsp;</td>
	              	<td colspan="2"></td>
	        	</tr>
	        	<tr align="center">
	            	<td>&nbsp;&nbsp;</td>  
	           		<td>&nbsp;&nbsp;</td>
	               	<td></td>
	              	<td></td>
	              	<td>&nbsp;&nbsp;</td>
	              	<td colspan="2"></td>
	        	</tr>
	        	<tr>
	       			<td align="center">合计</td>
	         		<td align="left">共<c:out value="${inspectionPayInfo.report_count}"></c:out>份报告</td>
	              	<td  align="left"><c:out value="${inspectionPayInfo.pay_received}"></c:out></td>
	              	<c:choose>
		       			<c:when test="${type_1 eq 'borrow'}">
			         		<td>借票日期</td>
			         		<td align="left">
			         			<c:if test="${!empty inspectionPayInfo.pay_date}">
			         				<fmt:formatDate value="${inspectionPayInfo.pay_date}" pattern="yyyy-MM-dd"/>
			         			</c:if>
		         			</td>
		              	</c:when>
		              	<c:when test="${type_1 eq 'invoice'}">
	         				<td >开票日期</td>
	         				<td align="left">
	         					<c:if test="${!empty inspectionPayInfo.pay_date}">
			         				<fmt:formatDate value="${inspectionPayInfo.pay_date}" pattern="yyyy-MM-dd"/>
			         			</c:if>
	       					</td>
		              	</c:when>
	       			</c:choose>
	       			<td>消票日期</td>
	       			<td>
	       			<c:if test="${reportBorrow.borrow_status==3}">
	       			<fmt:formatDate value="${reportBorrow.operator_date}" pattern="yyyy-MM-dd"/>
	       			</c:if>
	       			</td>
	       		</tr>	
	       		<tr align="left">
	       			<td>发票编号</td>
	         		<td colspan="6" align="left">
	         			<c:out value="${inspectionPayInfo.invoice_no}"></c:out>
	         		</td>
	       		</tr>	
	       		
	       		<tr align="left">
	       			<td width='50'>现金</td>
	         		<td width='120'>
	         			<c:if test="${!empty inspectionPayInfo.cash_pay && inspectionPayInfo.cash_pay != '0.0'}">
	         				<c:out value="${inspectionPayInfo.cash_pay}"></c:out>元
	         			</c:if>
         			</td>
	         		<td width='50'>POS</td>
	              	<td width='100'>
	              		<c:if test="${!empty inspectionPayInfo.pos && inspectionPayInfo.pos != '0.0'}">
	         				<c:out value="${inspectionPayInfo.pos}"></c:out>元
	         			</c:if>
         			</td>
	              	<td>转账</td>
	              	<td width='100'>
						<c:if test="${!empty inspectionPayInfo.remark && inspectionPayInfo.remark != '0.0'}">
	         				<c:out value="${inspectionPayInfo.remark}"></c:out>元
	         			</c:if>
					</td>
	              	<td width='150' style="text-align:left;font-size:13px"><b>经办人签字：</b></td>
	       		</tr>	
	       		<tr align="left">
	       			<td colspan="3">部门负责人：</td>
	         		<td colspan="3">监检人员：</td>
	         		<td ></td>
	       		</tr>	
	       		<tr align="left">
	         		<td colspan="3">日期：</td>
	         		<td colspan="3">日期：</td>
	         		<td style="font-size:13px"><b>电话：</b></td>
	       		</tr>	
	       			
	       		<tr align="right">
	       			<td colspan="7" style="text-align:right;border:none;">
	       				<div style="text-align:right;padding-right:30px">
	       					（<c:out value="${inspectionPayInfo.check_dep_name}"></c:out>）
	       					<fmt:formatDate value="<%=new Date()%>" pattern="yyyy-MM-dd"/>
	       				</div>
	       			</td>
	       		</tr>	
		</table>
	</div>
</form>
</body>
</html>