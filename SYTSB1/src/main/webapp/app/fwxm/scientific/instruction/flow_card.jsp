<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程卡</title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript">



</script>
</head>
<body style="overflow: auto">

	<table border="0" cellpadding="0" cellspacing="0" id="ow_big_table">
	
	<tr>
		<td class="obt2">
		<div class="obt2_d" style="overflow:auto;">

	<table border="0" cellpadding="0" cellspacing="0" width="" height="" align="center" style="margin-left: 50px;">
	<tr class="d_tr" >
		<td align="center" style="margin:0 auto;">
			<style>
			.st1 {border:1px solid #DAB693;background:#FFFDF3;padding:10px 3px 10px 10px;width:280px;font-size:14px;font-weight:bold;}
			.show0 {border:1px solid #93C7DA;background:#F3FDFF;padding:10px 3px 10px 10px;width:280px;text-align:left;}
			.show1 {padding:2px 0px 2px 0px;}
			</style>
			<div class="st1">编号：${sn}</div>
			
				<c:forEach var="step" items="${flowStep}" varStatus="status">
					<div class="show0">
						<div class="show1">操作步骤:  ${step.op_action}</div>	
						<div class="show1">操作人员:  ${step.op_user_name}</div>
						<div class="show1">操作时间:  ${step.op_time}</div>						
						<div class="show1">操作说明:  ${step.op_remark}</div>				
					</div>	
					<c:if test="${status.count<size}">
					
						<div class="jt1"><img src="k/kui/images/jt.gif"></div>	
					</c:if>
					
				</c:forEach>
			</td>
		</tr>
	</table>
		
		</div>
		</td>
	</tr>
	<tr>
		<td class="obt3" align="right" style="">
			
			
			<a  class="l-button-warp l-button" ligeruiid="Button1009" style="margin-top:30px;" onclick="javasript:api.close();"><span class="l-button-main l-button-hasicon"><span class="l-button-text">关闭</span><span class="l-button-icon iconfont l-icon-cancel"></span></span></a>
		</td>
	</tr>
</table>
			

			
			

</body>
</html>