<%@page import="java.util.Collections"%>
<%@page import="java.util.Date"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="com.khnt.bpm.core.bean.Trask"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@include file="/k/kui-base-form.jsp"%>
<script type="text/javascript">
	$(function() {
	    <c:if test="${param.status=='track'}">
		$("#tabss").ligerTab({height:"100%",showSwitch:true});
		</c:if>
		pageInit();
	});
	
	function pageInit(){
        swfobject.embedSWF("pub/bpm/flexflow/FlowTrask.swf", "flashContent", "100%", "100%", "10.0.0", null, {
            actionType: "${param.status}",
            xml: encodeURI('${flowxml}'.replace(/&&/gi, "andand"))
        }, {
            wmode : "transparent",
            menu : "true",
            allowScriptAccess : "sameDomain"
        }, {
            id: "JbpmWorkFlow",
            name: "JbpmWorkFlow",
            align:"middle"
        }, function() {
            return;
        });
        
        <c:forEach items="${subFlowXml}" var="sfx" varStatus="vs">
        swfobject.embedSWF("pub/bpm/flexflow/FlowTrask.swf", "sfx_${vs.index}", "100%", "100%", "10.0.0", null, {
            actionType: "${param.status}",
            xml: encodeURI('${sfx.value}'.replace(/&&/gi, "andand"))
        }, {
            wmode : "transparent",
            menu : "true",
            allowScriptAccess : "sameDomain"
        }, {
            id: "JbpmWorkFlow_${vs.index}",
            name: "JbpmWorkFlow_${vs.index}",
            align:"middle"
        }, function() {
            return;
        });
        </c:forEach>
        
        var traskData = [
            <% List<Object> trask = (List<Object>)request.getAttribute("trask");
            if(trask!=null && !trask.isEmpty()){
                for(int i = 0; i < trask.size(); i++){
                	Object[] tr = (Object[])trask.get(i);
                	out.print("{action:'");
                	out.print(tr[2]);
                	out.print("',person:'");
                	out.print(tr[0]);
                	out.print("',time:'");
                	out.print(DateUtil.format((Date)tr[1], "yyyy-MM-dd HH:mm:ss"));
                	out.print("',id:'");
                	out.print(tr[3]);
                	out.print("'}");
                	out.println(i==trask.size()-1?"":",");
                }
            }%>
        ];
        $("#tsGrid").ligerGrid({
            data:{Rows: traskData},
            width: '100%',
            height: '92%',
            usePager: false,
            columns:[{
                display: "操作人",
                name: "person",
                align: "center",
                width: 100
            },{
                display: "操作时间",
                name: "time",
                type: "dateTime",
                align: "center",
                width:  150
            },{
                display: "活动",
                name: "action",
                align: "left",
                minWidth: $(window).width()-280
            }]
        });
	}
	
	function _reSortOpinionData(datas){
		for(var i = 0; i < datas.length; i++){
			for(var j = 0; j < datas.length; j++){
				if(datas[i].time > datas[j].time){
					var tmp = datas[i];
					datas[i] = datas[j];
					datas[j] = tmp;
				}
			}
		}
		return datas;
	}
</script>
</head>
<body>
    <c:choose><c:when test="${param.status=='track'}">
    <div id="tabss">
        <div title="${empty subFlowXml?'':'主'}流程运行图">
            <div id="flashContent">
                <p>要查看此视图，您的电脑系统中必须安装adobe flash player 10.0以上版本。</p>
            </div>
        </div>
        <c:forEach items="${subFlowXml}" var="sfx" varStatus="vs">
            <div title="${sfx.key}">
                <div id="sfx_${vs.index}">
                    <p>要查看此视图，您的电脑系统中必须安装adobe flash player 10.0以上版本。</p>
                </div>
            </div>
        </c:forEach>
        <div title="活动记录">
           <div id="tsGrid"></div>
        </div>
    </div></c:when><c:when test="${param.status!='track'}">
        <div id="flashContent" style="width:100%;height:100%;">
            <p>要查看此视图，您的电脑系统中必须安装adobe flash player 10.0以上版本。</p>
        </div>
    </c:when></c:choose>
</body>
</html>
