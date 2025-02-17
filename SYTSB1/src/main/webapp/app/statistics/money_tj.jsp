<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.khnt.utils.DateUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
String code=((CurrentSessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getDepartment().getAreaCode();
String areaname=((CurrentSessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getDepartment().getAreaName();
String date = DateUtil.getDate(new Date());
Calendar c = Calendar.getInstance();
%>
    <head>
        <!-- 每个页面引入，页面编码、引入js，页面标题 -->
        <%@ include file="/k/kui-base-form.jsp"%>
        <%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
        <script type="text/javascript" src="pub/rbac/js/area.js"></script>
        
        <script type="text/javascript">
        var yearData=[];
        for(var i=0;i<90;i++){
            yearData.push({id:1990+i,text:1990+i});
        }
        var monthData=[];
        for(var i=0;i<12;i++){
        	monthData.push({id:1+i,text:1+i});
        }
            $(function () {
				$("#areaName").ligerComboBox({
					initValue:'<%=code %>',
					initText:'<%=areaname %>',
					valueFieldID:"areaCode",
					textModel:true,
					onBeforeOpen:showlist
				});
				$("#yearName").ligerComboBox({
					initValue:'<%=c.get(Calendar.YEAR)%>',
					initText:'<%=c.get(Calendar.YEAR)%>',
					valueFieldID:"year",
					textModel:true,
					data:yearData
				});
				$("#monthName").ligerComboBox({
					initValue:'<%=c.get(Calendar.MONTH) + 1%>',
					initText:'<%=c.get(Calendar.MONTH) + 1%>',
					valueFieldID:"month",
					textModel:true,
					data:monthData
				});
				$("#button1").ligerButton({icon:"count",text:"查询",click:function(){
        			search() ;
        		}});
                $("#formObj").initForm({
                     toolbar:null
                });               
                search();
            });
            
            function showlist(){
             	$(this).data('areacode','<%=code%>');
             	showAreaList.call(this);
             }
            
            var ggg;
            function search(){  
            	var areaCode;
            	areaCode = $("#areaCode").val();
            	var param = "year="+$("#year").val()+"&month="+$("#month").val()+"&areaCode="+areaCode+
            	    "&type="+$("#type-txt").ligerComboBox().getValue();
            	var url = 'dibao/money/record/get_money_total.do?'+param;
            	if(ggg){
                	ggg._setUrl(encodeURI(encodeURI(url)));
                }else{
                    var titleObj =  [
                	                 { display: '单位', name: 'areaName', width: 120,
                	                	 totalSummary:
                	                     {
                	                         render: function (suminf, column, cell)
                	                         {
                	                             return '<div>总计</div>';
                	                         },
                	                         align: 'center'
                	                     }
    								 },
                	                 { display: '发放户数', name: 'family_num', width: 150, align: 'right',
    									 render:function(v){
            	                    		 
            	                    		 return v.family_num+'户';
            	                    	 },totalSummary:
                	                     {
                	                         render: function (suminf, column, cell)
                	                         {
                	                             return '<div>' + suminf.sum + '户</div>';
                	                         },
                	                         align: 'center'
                	                     }}, 
                	                 
                	                 { display: '发放人数', name: 'person_num', width: 120,align: 'right',
											render:function(v){
            	                    		 
	            	                    		 return v.person_num+'人';
	            	                    	 },totalSummary:
	                	                     {
	                	                         render: function (suminf, column, cell)
	                	                         {
	                	                             return '<div>' + suminf.sum + '人</div>';
	                	                         },
	                	                         align: 'center'
	                	                     }},
                	                 
            	                     { display: '月发放金额', name: 'month_money', width: 180,align: 'right',
            	                    	 render:function(v){
            	                    		 
            	                    		 return v.month_money+'元';
            	                    	 },totalSummary:
                	                     {
                	                         render: function (suminf, column, cell)
                	                         {
                	                             return '<div>' + suminf.sum + '元</div>';
                	                         },
                	                         align: 'center'
                	                     }},   
            	                     { display: '季发放金额', name: 'total_money', width: 180,align: 'right',
            	                    		 render:function(v){
                	                    		 
                	                    		 return v.total_money+'元';
                	                    	 }
            	                    		 ,totalSummary:
	                	                     {
	                	                         render: function (suminf, column, cell)
	                	                         {
	                	                             return '<div>' + suminf.sum + '元</div>';
	                	                         },
	                	                         align: 'center'
	                	                     }},
                	               { display: '备注', name: 'remark', width: 150,align: 'left'} 
                	                ]; 
                	 
                     ggg = $("#maingrid4").ligerGrid({
                         columns : titleObj,
                         checkbox : false,
                         url: encodeURI(encodeURI(url)),
                         allowHideColumn: false,
                         width : '99.5%',
                         height : '99%',
//                       usePager : false, 是否分页
                         dataAction:'server',                      
                         pageSize:50 ,   
                         rownumbers:true,
                         delayLoad : false ,//初始化是是否不加载
                         usePager:false
                     });
                }
            }           
           
        </script>

    </head>
    <body>

<div class="item-tm">
	<div id="toolbar1"></div>
</div>



    <div class="item-tm">
	    <div class="l-page-title2 has-icon has-note" style="height:95px;" >	    
	    <div class="l-page-title2-div"></div>
		<div class="l-page-title2-text"><h1>居民最低生活保障金发放统计表</h1></div>
		<div class="l-page-title2-note"></div>
		<div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"></div>
		<div class="l-page-title-content" style="top:2px;">	
		  <form id="formObj">	
		    <table border="0" cellpadding="0" cellspacing="0" width="" class="l-table1">
					<tr>
					    <td width="50" align="right">低保类型&nbsp;&nbsp;</td>
						<td width="80">
						<u:combo name="type" code="db_0016" attribute="initValue:'0'"/>						 
						</td>
						<td width="60" align="right">行政区划&nbsp;&nbsp;</td>
						<td width="120">
						<input id="areaCode" name="areaCode" type="hidden" ltype='text' validate="{maxlength:50}" />
					    <input id="areaName" name="areaName" type="text" ltype='select' validate="{maxlength:100}" />						 
						</td>					
					</tr>
					<tr>
					    <td width="60" align="right">统计年份&nbsp;&nbsp;</td>
						<td width="120">
						<input id="year" name="year" type="hidden" ltype='text' validate="{maxlength:50}" />
					    <input id="yearName" name="yearName" type="text" ltype='select' validate="{maxlength:100}" />						 
						</td>
					    <td width="60" style="width:80px" align="right">统计月份&nbsp;&nbsp;</td>
						<td width="150">
						<input id="month" name="month" type="hidden" ltype='text' validate="{maxlength:50}" />
						<input id="monthName" name="monthName" type="text" ltype='select' validate="{maxlength:100}" />
						</td>						
					</tr>
				</table>
            <table border="0" cellpadding="0" cellspacing="0" width="" class="l-table1">
					<tr>								
						<td width="200" style="padding:0 0 0 30px">&nbsp;</td>
						<td width="100" style="padding:0 0 0 60px" align="right">
						<br/>
                        <div id="button1"></div>                        
                        </td> 
					</tr>
			</table>			
          </form>       
		 </div>
        </div>
    </div>       
         <div  id="maingrid4"></div>
    </body>
</html>