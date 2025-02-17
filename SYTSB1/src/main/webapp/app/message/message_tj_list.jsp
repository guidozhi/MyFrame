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
      <script src="app/fwxm/scientific/js/echarts.js"></script>
    <script src="app/statistics/js/china.js"></script>
    <script test="text/javascript">
 
    $(function () {
        $("#btn1").css({"height":"20px","line-height":"18px"})
        $("#btn2").css({"height":"20px","line-height":"18px"})
         $("#btn3").css({"height":"20px","line-height":"20px"});
      
        $("#btn1").ligerButton({
            icon:"count",
            click: function (){
                init();
            },text:"统计"
        });
        $("#form1").ligerForm();    
        
        var data = <u:dict sql="select t.id code,t.org_name text from SYS_ORG t order by t.orders"/>;
        var tt = new Array();
        tt.push({id:'all',text:'全部'});
        for(var i in data){
           tt.push(data[i]);
        }
        //$("#org_id").ligerComboBox().setData(tt);
       // $("#org_id").ligerGetComboBoxManager().setValue("all");
        init();       
    });
    
    
    function init(){
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
       // var org_id = $("#org_id").ligerGetComboBoxManager().findValueByText($("#org_id").val());
        var org_id=$("#org_id2").val();
        var name = $("#name").val();
        if(org_id==""){
            //$.ligerDialog.alert("请先选择部门！");
            //return;
        }
        $.post("messageHistory/getMessageLog.do",{"startDate":startDate,"endDate":endDate,"org_id":org_id,"name":name}, function(resp){
        	inputGrid = $("#countGrid").ligerGrid({
                columns: [
                         { display: '部门名称', name: 'depName',align: 'center', width: 210},
                         { display: '接收人', name: 'userName',align: 'center', width: 120},
                         { display: '消息类型', name: 'zsxz',align: 'center', width: 120},
                         { display: '发送数量', name: 'zsxm',align: 'center', width: 120,type: 'int'}
                ], 
                 data:{Rows:eval(JSON.stringify(resp.data))},//json格式的字符串转为对象 */
                height:'100%',
                usePager:false,
                width:'100%'
             },"json");
        });
    }
    function out()
    {
        /* var org_id = $("input[name='org_id']").ligerGetComboBoxManager().getValue(); */
        /* var org_id = $("#org_id").ligerGetComboBoxManager().findValueByText($("#org_id").val()); */
        var org_id=$("#org_id2").val();
        if(org_id==""){
            $.ligerDialog.alert("请先选择部门！");
            return;
        }
        $("#org_id1").val(org_id);
        $("body").mask("正在导出数据,请等待...");
        /* $("#form1").attr("action","sta/analyse/exportCountByUser.do"); */
        $("#form1").attr("action","tj/exportCount.do"); 
        $("#form1").submit();
        $("body").unmask();
    };
    function selectOrg(){
    	 top.$.dialog({
	         width: 350,
	         height: 400,
	         lock: true,
	         parent: api,
	         data: {
	       	 window: window
	         },
	         title: "部门",
	         content: 'url:app/finance/cw_bmfytj_org.jsp'
    	  });
    }
    function clickNodeId(unitId,unitName){
    	$("#org_id").val(unitName);
    	$("#org_id2").val(unitId);
    }
    </script>
    <style>
    div{margin: 0.5px;}
    .l-button {padding:0 8px 2px 9px;}
    </style>
</head>
<%
    String firstDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
    String curDate  = DateUtil.getDateTime("yyyy-MM-dd", new Date());
%>
<body>
<form name="form1" id="form1" action="" getAction="" target="_blank">
<input type="hidden" name="org_id1" id="org_id1"/>
<input type="hidden" name="org_id2" id="org_id2"/>
<div class="item-tm">
    <div class="l-page-title2 has-icon has-note" style="height: 80px">
        <div class="l-page-title2-div"></div>
        <div class="l-page-title2-text"><h1>消息发送统计</h1></div>
        <div class="l-page-title2-note">以消息为统计对象。</div>
        <div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"/></div>
        <div class="l-page-title-content" style="top:25px;height:80px;"> 
            <table border="0" cellpadding="0" cellspacing="0" width="">
                <tr>
                    <%-- <td style="text-align: right;width:50px;">：</td>
                    <td width="140px">
                        <u:combo attribute="initValue:''"   name="unitName" code="TJY2_UNIT" />
                       <input text="text" name="unitName" id ="unitName" 
							ltype="select" ligerui="{initValue:'',data:<u:dict code='TJY2_UNIT' />}" />
                    </td> --%>
                    <td width="100" style="text-align: right;width:60px;">部门名称：</td>
                    <td width="150px">
                        <input type="text" name="org_id" id="org_id" ltype="text" onclick="selectOrg()" ligerui="{
                            readonly:true,
                            tree:{checkbox:false,data: }
                        }"/>
                    </td>
                     <td width="100" style="text-align: right;width:60px;">接收人：</td>
                    <td width="150px">
                        <input type="text" name="name" id="name" ltype="text"  />
                    </td>
                    <td style="text-align: right;width:120px;">开始时间从：</td>
                    <td width="100">
                            <input id="startDate" name="startDate" type="text" ltype="date" value="<%=firstDate %>"/>
                    </td>
                    <td align="center">&nbsp;至&nbsp;</td>
                    <td width="100">
                        <input id="endDate" name="endDate" type="text" ltype="date" value="<%=curDate %>"/>
                    </td>
                    <td colspan="1" align="right">
                        <div id="btn1"></div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
</form>
<div id="container"  position="center">
    <div id="countGrid"></div>   
</div>
<div id="main"  style="width:100%;height:80%;"></div> 

</body>

</html>