<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="/k/kui-base-list.jsp" %>
<%  
	HashMap<String,Object> map = (HashMap<String,Object>)request.getSession().getAttribute("data");
	List<String> depts = (List<String>)map.get("depts");
	List<String> clsses_in = (List<String>)map.get("clsses_in");
	List<String> clsses_out = (List<String>)map.get("clsses_out");
	List<Map<String,Object>> rows_in = (List<Map<String,Object>>)map.get("rows_in");
	List<Map<String,Object>> rows_out = (List<Map<String,Object>>)map.get("rows_out");
	String title = "部门收支对比表";
	response.setHeader("Content-disposition","attachment; filename="+new String(title.getBytes("gb2312"), "ISO8859-1" )+".xls"); 
%>
<script type="text/javascript">
	$(function () {  	
		exportToExcel();
	});
	function exportToExcel() { 
		var winname = window.open('', '_blank', 'top=10000'); 
		var strHTML = document.all.tableExcel.innerHTML; 
  		winname.document.open('text/html', 'replace'); 
    	winname.document.writeln(strHTML); 
     	winname.document.execCommand('saveas','','<%=title%>.xls'); 
     	winname.close(); 
  		closeCurWin();
	}
	
	function closeCurWin() {
	    window.opener=null;
	    window.open('','_self');
	    window.close();
	}
</script>
<style type="text/css">
#table td{text-align:center;}
</style>
</head>
<body>
<div id="tableExcel"> 
	<table width="700" cellpadding="6" cellspacing="0" bgcolor="#cccccc" border="1" id="table">
    	<tr align="center">
        	<td colspan="<%=clsses_in.size()+clsses_out.size()+3%>">
        		<font size="3"><b><%=title%></b></font>
        	</td>
    	</tr>
    	<tr>
    		<td colspan="<%=clsses_in.size()+clsses_out.size()+3%>" align="center">
    			<c:if test="${sessionScope.start!=''}">
        			<c:out value="统计时间：${sessionScope.start}"></c:out>
        			<c:choose>
	        			<c:when test="${sessionScope.end!=''}">
	        				<c:out value="至 ${sessionScope.end}"></c:out>
	        			</c:when>
	        			<c:otherwise>
	        				<c:out value="至今"></c:out>
	        			</c:otherwise>
	        		</c:choose>
        		</c:if>
        		<c:if test="${'' eq sessionScope.start && '' eq sessionScope.end}">
        			统计截止至<%=DateUtil.getDateTime("yyyy年MM月dd日",new Date())%>
        		</c:if>
    		</td>
    	</tr>
            <tr>
				<td align="center">部门</td>
				<%
				for(int i=0;i<clsses_out.size();i++){
					%>
					<td align="center"><%=clsses_out.get(i) %></td>
					<%
				}
				%>
				<td align="center">支出小计</td>
				<%
				for(int i=0;i<clsses_in.size();i++){
					%>
					<td align="center"><%=clsses_in.get(i) %></td>
					<%
				}
				%>
				<td align="center">收入小计</td>
			</tr>
			<%
			BigDecimal d2 = new BigDecimal(Integer.toString(1));
			for(String dept : depts){
			%>
			<tr align="center">
				<td><%=dept%></td>  
				<%
				BigDecimal out_total = new BigDecimal(0);
				//float out_total = 0;
				for(String clss : clsses_out){
					boolean flag = true;
					for(Map<String,Object> row : rows_out){
						if(dept.equals(row.get("DEPT").toString()) 
									&& clss.equals(row.get("CLSS").toString())){
							String feeStr = row.get("MONEY").toString();
							
							BigDecimal fee =  new BigDecimal(feeStr);
							out_total = out_total.add(fee);
							
							/* float fee =  Float.parseFloat(feeStr);
							out_total += fee; */
							%>
							<td><%=fee.divide(d2,2,BigDecimal.ROUND_HALF_UP).toString()%></td>
							<%
							flag = false;
						}
					}
					if(flag){
						%>
						<td>0</td>
						<%
					}
				}
				%>
				<td><%=out_total %></td>
				<%
				//float in_total = 0;
				BigDecimal in_total = new BigDecimal(0);
				for(String clss : clsses_in){
					boolean flag = true;
					for(Map<String,Object> row : rows_in){
						if(dept.equals(row.get("DEPT").toString()) 
									&& clss.equals(row.get("CLSS").toString())){
							String feeStr = row.get("MONEY").toString();
							
							BigDecimal fee =  new BigDecimal(feeStr);
							out_total = out_total.add(fee);
							
							/* float fee =  Float.parseFloat(feeStr);
							out_total += fee; */
							%>
							<td><%=fee.divide(d2,2,BigDecimal.ROUND_HALF_UP).toString()%></td>
							<%
							flag = false;
						}
					}
					if(flag){
						%>
						<td>0</td>
						<%
					}
				}
				%>
				<td><%=out_total %></td>
			</tr>
			<%
			}
			%>
			<tr>
				<td align="center">合计</td>
				<%
				BigDecimal out_total_sum = new BigDecimal(0);
				//float out_total_sum = 0;
				for(String classOut : clsses_out){
					//float out_class_total = 0;
					BigDecimal out_class_total = new BigDecimal(0);
					for(Map<String,Object> r : rows_out){
						if(classOut.equals(r.get("CLSS").toString())){
							
							BigDecimal fee =  new BigDecimal(r.get("MONEY").toString());
							out_class_total = out_class_total.add(fee);
							out_total_sum = out_total_sum.add(fee);
							
							/* float f =  Float.parseFloat(r.get("MONEY").toString());
							out_class_total += f;
							out_total_sum += f; */
						}
					}
					%>
						<td align="center"><%=out_class_total.divide(d2,2,BigDecimal.ROUND_HALF_UP).toString()%></td>
					<%
				}
				%>
				<td align="center"><%=out_total_sum.divide(d2,2,BigDecimal.ROUND_HALF_UP).toString()%></td>
				<%
				BigDecimal in_total_sum = new BigDecimal(0);
				//float in_total_sum = 0;
				for(String classIn : clsses_in){
					//float in_class_sum = 0;
					BigDecimal in_class_sum = new BigDecimal(0);
					for(Map<String,Object> r : rows_in){
						if(classIn.equals(r.get("CLSS").toString())){
							
							BigDecimal fee =  new BigDecimal(r.get("MONEY").toString());
							in_class_sum = in_class_sum.add(fee);
							in_total_sum = in_total_sum.add(fee);
							
							/* float f =  Float.parseFloat(r.get("MONEY").toString());
							in_class_sum += f;
							in_total_sum += f; */
						}
					}
					%>
						<td align="center"><%=in_class_sum.divide(d2,2,BigDecimal.ROUND_HALF_UP).toString()%></td>
					<%
				}
				%>
				<td align="center"><%=in_total_sum.divide(d2,2,BigDecimal.ROUND_HALF_UP).toString()%></td>
		    </tr>
	</table>
</div>
</body>
</html>