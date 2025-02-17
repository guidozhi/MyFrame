<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="java.util.Calendar"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <%
    String firstDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
    String lastDate  = DateUtil.getLastDateStringOfYear("yyyy-MM-dd");
	%>
    <%@include file="/k/kui-base-list.jsp" %>
    <%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
    <script test="text/javascript">
    $(function () {
        $("#btn1").css({"height":"20px","line-height":"18px"})
        $("#btn2").css({"height":"20px","line-height":"18px"})
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
    	var column;
    	var org_id = $("#org_id").ligerGetComboBoxManager().findValueByText($("#org_id").val());
    	var userName = $("#userName").val();
    	var startDate = $("#startDate").val();
        var lastDate = $("#lastDate").val();
        column=[
        	{ display: '员工ID', name: 'peopleId',hide:true,isAllowHide: false},
        	{ display: '部门名称', name: 'org_name',align: 'center', width: '12%'},
            { display: '姓名', name: 'emp_name',align: 'center', width: '10%'},
			{ display: '职务', name: 'work_title',align: 'center', width: '12%'},
			{ display: '联系电话', name: 'emp_phone',align: 'center', width: '10%'},
			{ display: '外出时间', name: 'gwwc_date',align: 'center', width: '15%'},
			{ display: '外出地点', name: 'gwwc_place',align: 'center', width: '10%'},
			{ display: '外出事由', name: 'gwwc_reason',align: 'center', width: '20%'},
			{ display: '备注', name: 'remark',align: 'center', width: '10%'}];
         $.post("BgLeaveAction/getGwwcInfoCount.do",{"org_id":org_id,"userName":userName,"startDate":startDate,"lastDate":lastDate}, function(resp){
            inputGrid = $("#countGrid").ligerGrid({
                columns: column, 
                data:{Rows:eval(JSON.stringify(resp.data))},//json格式的字符串转为对象
                height:'100%',
                usePager:false,
                rownumbers: true, 
                width:'100%',
                onSelectRow: function (rowdata, rowindex){
                },
                onDblClickRow : function (rowdata, rowindex, rowobj){
                	/* top.$.dialog({
             	       width: 1100,
             	       height: 800,
             	       lock: true,
             	       parent: api,
             	       data: {
             	     	 window: window
             	       },
             	       title: "请休假列表",
             	       content: 'url:app/humanresources/leave/leave_analysis_list.jsp?people_id='+rowdata.peopleId+"&startDate="+startDate+"&lastDate="+lastDate
             	    }); */
                }
             },"json");
        }); 
    }
    function out()
    {
    	var org_id = $("#org_id").ligerGetComboBoxManager().findValueByText($("#org_id").val());
        $("#org_id1").val(org_id);
        if(org_id==""){
            $.ligerDialog.alert("请先选择部门！");
            return;
        }
        $("body").mask("正在导出数据,请等待...");
        $("#form1").attr("action","BgLeaveAction/getGwwcInfoCountExport.do"); 
        $("#form1").submit();
        $("body").unmask();
    };
    </script>
    <style>
    div{margin: 0.5px;}
    .l-button {padding:0 8px 2px 9px;}
    </style>
</head>
<body>
<form name="form1" id="form1" action="" getAction="">
<input type="hidden" name="org_id1" id="org_id1"/>
<div class="item-tm">
    <div class="l-page-title2 has-icon has-note" style="height: 100px">
        <div class="l-page-title2-div"></div>
        <div class="l-page-title2-text"><h1>四川省特种设备检验研究院中层干部公务外出统计</h1></div>
        <div class="l-page-title2-note">以中层干部公务外出为统计对象。</div>
        <div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"/></div>
        <div class="l-page-title-content" style="top:65%;height:80px;"> 
            <table border="0" cellpadding="0" cellspacing="0" width="">
                <tr>
                    <td width="100" style="text-align: right;width:60px;">部门名称：</td>
                    <td width="150px">
                        <input type="text" name="org_id" id="org_id" ltype="select" ligerui="{
                            readonly:true,
                            tree:{checkbox:false,data: }
                        }"/>
                    </td>
                    <td width="100" style="text-align: right;width:60px;">姓名：</td>
                    <td width="150px">
                        <input type="text" id="userName" name="userName" ltype="text"/>
                    </td>
                    <td width="100" style="text-align: right;width:60px;">年份从：</td>
                    <td width="100">
                        <input id="startDate" name="startDate" type="text" ltype="date" value="<%=firstDate %>"/>
                    </td>
                    <td align="center">&nbsp;到&nbsp;</td>
                    <td width="100">
                        <input id="lastDate" name="lastDate" type="text" ltype="date" value="<%=lastDate %>"/>
                    </td>
                    <td colspan="1" align="right">
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