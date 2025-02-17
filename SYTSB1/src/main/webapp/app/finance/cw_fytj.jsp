<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="java.util.Calendar"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <%@include file="/k/kui-base-list.jsp" %>
    <%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
    <script test="text/javascript">
    $(function () {
        /* $("#btn1").css({"height":"20px","line-height":"18px"})
        $("#btn2").css({"height":"20px","line-height":"18px"}) */
      
        $("#btn1").ligerButton({
            icon:"count",
            click: function (){
                init();
            },text:"统计"
        });
        
        $("#btn2").ligerButton({
            icon:"excel-export",
            click: function (){
                out();
            },text:"导出"
        });
        
        $("#form1").ligerForm();    
        
        var data = <u:dict sql="select t.id as code,t.org_name as text from SYS_ORG t where t.parent_id = '100000' and t.id not in ('100032', '100039', '100069', '100048', '100070', '100038', '100059', '100061', '100051') and t.status = 'used' order by t.orders"/>;
        var tt = new Array();
        tt.push({id:'all',text:'全部'});
        for(var i in data){
           tt.push(data[i]);
        }
        $("#org_id").ligerComboBox().setData(tt);
        $("#org_id").ligerGetComboBoxManager().setValue("all");
        init();       
    });
    
    
    function init(){
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        var org_id = $("#org_id").ligerGetComboBoxManager().findValueByText($("#org_id").val());
        var userName = $("#userName").val();
        if(org_id==""){
            //$.ligerDialog.alert("请先选择部门！");
            //return;
        }
        $.post("cw/fytj/getCwData.do",{"startDate":startDate,"endDate":endDate,"org_id":org_id,"userName":userName}, function(resp){
            inputGrid = $("#countGrid").ligerGrid({
                columns: [
                        { display: '姓名', name: 'userName',align: 'center', width: 150},
                         { display: '所在部门', name: 'depName',align: 'center', width: 250},
                         { display: '差旅补助', name: 'clBz',align: 'center', width: 120,type: 'int'},
                         { display: '培训费', name: 'pxf',align: 'center', width: 120,type: 'int'},
                         { display: '培训补助', name: 'pxBz',align: 'center', width: 120,type: 'int'},
                         { display: '交通费（培训）', name: 'pxJt',align: 'center', width: 120,type: 'int'},
                         { display: '住宿费（培训）', name: 'pxZs',align: 'center', width: 120,type: 'int'},
                         { display: '小计', name: 'taotal',align: 'center', width: 150,type: 'int'}
                ], 
                data:{Rows:eval(JSON.stringify(resp.data))},//json格式的字符串转为对象
                height:'100%',
                usePager:false,
                width:'100%',
                onSelectRow: function (rowdata, rowindex){
                	$("#userName").val(rowdata.userName);
                	$("#org_id").val(rowdata.depName);
                }
             },"json");
        });
    }
    function out()
    {
        /* var org_id = $("input[name='org_id']").ligerGetComboBoxManager().getValue(); */
        var org_id = $("#org_id").ligerGetComboBoxManager().findValueByText($("#org_id").val());
        if(org_id==""){
            $.ligerDialog.alert("请先选择部门！");
            return;
        }
        $("#org_id1").val(org_id);
        $("body").mask("正在导出数据,请等待...");
        /* $("#form1").attr("action","sta/analyse/exportCountByUser.do"); */
        $("#form1").attr("action","cw/fytj/exportCount.do"); 
        $("#form1").submit();
        $("body").unmask();
    };
    </script>
</head>
<%
    String firstDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
    String curDate  = DateUtil.getDateTime("yyyy-MM-dd", new Date());
%>
<body>
<form name="form1" id="form1" action="" getAction="" target="_blank">
<input type="hidden" name="org_id1" id="org_id1"/>
<div class="item-tm">
    <div class="l-page-title2 has-icon has-note" style="height: 80px">
        <div class="l-page-title2-div"></div>
        <div class="l-page-title2-text"><h1>财务报账费用统计</h1></div>
        <div class="l-page-title2-note">以部门、人员为统计对象。</div>
        <div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"/></div>
        <div class="l-page-title-content" style="top:25px;height:80px;"> 
            <table border="0" cellpadding="0" cellspacing="0" width="">
                <tr>
                    <td width:"60" style="text-align: right;">姓名：</td>
                    <td width="110px">
                        <input type="text" name="userName" id="userName" ltype="text"/>
                    </td>
                    <td width="60" style="text-align: right;">部门：</td>
                    <td width="110px">
                        <input type="text" name="org_id" id="org_id" ltype="select" ligerui="{
                            readonly:true,
                            tree:{checkbox:false,data: }
                        }"/>
                    </td>
                    <td width="110" style="text-align: right;">审批通过时间从：</td>
                    <td width="110">
                            <input id="startDate" name="startDate" type="text" ltype="date" value="<%=firstDate %>"/>
                    </td>
                    <td align="center">&nbsp;至&nbsp;</td>
                    <td width="110">
                        <input id="endDate" name="endDate" type="text" ltype="date" value="<%=curDate %>"/>
                    </td>
                    <td width="" style="text-align: right;float: left;padding-left: 5px;">
                        <div id="btn1"></div><div id="btn2"></div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
</form>
<div position="center">
    <div id="countGrid"></div>   
</div>
</body>
</html>