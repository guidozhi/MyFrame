<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.khnt.security.CurrentSessionUser" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.utils.StringUtil"%>
<%
	String type = request.getParameter("type");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>通用查询</title>
<%@ include file="/k/kui-base-list.jsp"%>
<%
	CurrentSessionUser user = SecurityUtil.getSecurityUser();
	String orgId=user.getUnit().getId();
	String departmentId=user.getDepartment().getId();
	
	String sql="select * from TJY2_OFFICE_MEETING_ROOM r where r.id in ( select distinct(a.id) from TJY2_OFFICE_MEETING_ROOM a left join ( select b.FK_OFFICE_MEETING_ROOM from TJY2_OFFICE_MEETING_REQ b "+
				" where (b.MEETING_STATUS!='1' or b.status!='4') ";
	String startTime=request.getParameter("startTime");
	String endTime=request.getParameter("endTime");
	//取指定时间段空闲的会议室
	if(!(StringUtil.isEmpty(startTime)||StringUtil.isEmpty(endTime))){
		sql+=" and( end_time<=to_date('"+startTime+"','yyyy-mm-dd HH24:mi:ss') or start_time>=to_date('"+endTime+"','yyyy-mm-dd HH24:mi:ss') or (start_time is null and end_time is null))";
	}
	sql +=" ) temp on a.id = temp.FK_OFFICE_MEETING_ROOM )";
%>
<script type="text/javascript">
	$(function(){
		getData();
	})
	
	function selectionChange() {
		var count = Qm.getSelectedCount();//行选择个数
		var noticeState = Qm.getValuesByName("release").toString();//行选择个数
	}
	function getData() {
        var data = {page: 1, pagesize: 100, start: 0,defaultSearchCondition: "[{name:'creator_unit_id',compare:'=',value:'<%=orgId%>'}]"};
        $.post("qm?_method=q&pageid=TJY2_OFFICE_SELROOM",data,parseData,'json')
    }
    var index = 0;
    function parseData(data) {
        $("#content div").animate({opacity: 'hide'}, "slow");
        $("#content").empty();
        if (data.rows.length > 0) {
            index = data.rows.length;
            for (var j in data.rows) {
            	var src = "app/office/meeting/default.png";
            	if(data.rows[j].roompic_id!=''&&data.rows[j].roompic_id!=null&&data.rows[j].roompic_id!="undefined"){
            		/* src = "fileupload/downloadByObjId.do?id="+data.rows[j].roompic_id; */
            		var roompicIds = data.rows[j].roompic_id.split(",");
            		src = "fileupload/previewImage.do?id="+roompicIds[0];
            	}
                $("#content").append("<div class='tt' id='div" + j + "' style='display:none;cursor:pointer' onClick='getRoomInfo(this)'>" +
                        "<table>" +
                        "<tr><td rowspan='5'><input type='hidden' name='roomid' value='"+data.rows[j].id+"'><input type='hidden' name='roomname' value='"+data.rows[j].code+"'><input type='hidden' name='place' value='"+data.rows[j].place+"'><input type='hidden' name='pnumber' value='"+data.rows[j].accommodate_no+"'><img src ='"+src+"' width='160' height='130'/></td><td>&nbsp;&nbsp;会议室名称："+data.rows[j].code+"</td></tr>"+
                      /*   "<tr><td>&nbsp;&nbsp;管&nbsp;&nbsp;理&nbsp;人："+data.rows[j].manager_name+"</td></tr>"+ */
                        "<tr><td>&nbsp;&nbsp;联系电话："+data.rows[j].manager_phone+"</td></tr>"+
                        "<tr><td>&nbsp;&nbsp;容纳人数："+data.rows[j].accommodate_no+"</td></tr>"+
                        "<tr><td>&nbsp;&nbsp;室内设施："+data.rows[j].facility+"</td></tr>"+
                        "<tr><td>&nbsp;&nbsp;所在地点："+data.rows[j].place+"</td></tr>"+
                        "</table></div>");
                
            }
            animate(0);
        }else{
        	$("#content").html("暂无任何会议室！")
        }
    }
    function animate(i) {
        if (i < index) {
            $("#div" + i).animate({opacity: 'show'}, "normal", function () {
                animate(++i)
            });
        }
    }
  //选择结果
	function getSelectResult() {
		var result = {
			roomid : "", roomcode : "" , place : "" , pnumber : ""
		};
		result.roomid = $("#selectedId").val();
		result.roomcode = $("#roomName").val();
		result.place = $("#place").val();
		result.pnumber=$("#pNumber").val();
		return result;
	}
    function getRoomInfo(obj){
		var selectedId = $(obj).find("input[name='roomid']").val();
		var roomname = $(obj).find("input[name='roomname']").val();
		var place = $(obj).find("input[name='place']").val();
		var pnumber = $(obj).find("input[name='pnumber']").val();
		$('.tt').filter(function(){
			$(this).css("background-color","#ededed");
		})
		$(obj).css("background-color","#bcc7e9");
		//改变按钮状态
		$("#selectedId").val(selectedId);
		$("#roomName").val(roomname);
		$("#place").val(place);
		$("#pNumber").val(pnumber);
	}
</script>
<style type="text/css">
    .tt {
        float: left;
        background-color: #ededed;
        margin: 5px;
        padding: 5px;
        width: 350px;
        height: 130px;
    }
</style>
</head>
<body>
<input type="hidden" id="selectedId"/>
<input type="hidden" id="roomName"/>
<input type="hidden" id="place"/>
<input type="hidden" id="pNumber"/>
<div class="item-tm">
		<div class="div1" id="tbar" style=""></div>
	</div>
	<div id="content" class="scroll-tm">
	</div>
</body>
</html>