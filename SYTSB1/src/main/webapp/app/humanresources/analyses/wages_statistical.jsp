<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd"); 
	String nowTime=""; 
	nowTime = dateFormat.format(new Date());%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<%
CurrentSessionUser usee = SecurityUtil.getSecurityUser();
User uu = (User)usee.getSysUser();
com.khnt.rbac.impl.bean.Employee e = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
e.getId();
String userId=e.getId();

String uId = SecurityUtil.getSecurityUser().getId();
%>
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript">
var qmUserConfig = {
		tbar: [
		],
		
        listeners: {
	        rowClick: function(rowData, rowIndex) {
	        	
	        },
	        rowDblClick: function(rowData, rowIndex) {
		      detail();
	        },	
	        selectionChange: function(rowData, rowIndex) {
		         var count = Qm.getSelectedCount();
		         var yes_no = Qm.getValueByName("yes_no");
		         if(yes_no=="未确认"){
		         Qm.setTbar({
		          	detail: count==1,
		          	modify:count==1,
		          	del:count>0,
		          	submit:count==1
		       });
		         }else{
		         Qm.setTbar({
			          	detail: count==1,
			          	modify:count==1,
			          	del:count>0,
			          	submit:count<0
			       });
		         }
		         
	        },
	        rowAttrRender : function(rowData, rowid) {
	        }
}
}

function submit(){
	
	 var id = Qm.getValueByName("id");
	 
     $.ligerDialog.confirm('是否要确认？', function (yes){
     	if(!yes){return false;}
         top.$.ajax({
              url: "tjy2YwfwbgzqrbAction/submit.do?Id="+id,
              type: "GET",
              dataType:'json',
              async: false,
              success:function (data) {
                 if(data.success){
                   //  $.ligerDialog.success("提交成功！");
                    top.$.notice('确认成功！！！',3);
                     Qm.refreshGrid();//刷新
                 }else{
                     $.ligerDialog.warn("出错了！请重试！!");
                 }
              },
              error:function () {
                  //$.ligerDialog.warn("提交失败！");
             	 $.ligerDialog.error("出错了！请重试！！！");
              }
          });
     });
	
	
	
}
		
		function modify(){
	 top.$.dialog({
         width: 500,
         height: 300,
         lock: true,
         parent: api,
         data: {
       	 window: window
         },
         title: "修改",
         content: 'url:app/humanresources/ywfwbgzqr_detail.jsp?pageStatus=modify&id='+Qm.getValueByName("id")
      });
	
       }
		function add(){
			top.$.dialog({
		         width: 500,
		         height: 300,
		         lock: true,
		         parent: api,
		         data: {
		       	 window: window
		         },
		         title: "新增",
		         content: 'url:app/humanresources/ywfwbgzqr_detail.jsp?pageStatus=add&id='+Qm.getValueByName("id")
		      });
		}
		function del(){
			
	      		  $.del("确定删除?",
	      		    		"tjy2YwfwbgzqrbAction/delete.do",
	      		    		{"ids":Qm.getValuesByName("id").toString()});
	  
		}
		function detail(){
	         top.$.dialog({
		         width: 500,
		         height: 300,
		         lock: true,
		         parent: api,
		         data: {
		       	 window: window
		         },
		         title: "详情",
		         content: 'url:app/humanresources/ywfwbgzqr_detail.jsp?pageStatus=detail&id='+Qm.getValueByName("id")
	          });
        }
</script>

</head>
<body>
<form name="form1" id="form1" action="" getAction="">
<div class="item-tm">
    <div class="l-page-title2 has-icon has-note" style="height: 80px">
        <div class="l-page-title2-div"></div>
        <div class="l-page-title2-text"><h1>工资统计</h1></div>
        <div class="l-page-title2-note">以员工为统计对象。</div>
        <div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"/></div>
        <div class="l-page-title-content" style="top:25px;height:80px;"> 
        </div>
    </div>
</div>
</form>
        <qm:qm pageid="TJY2_YWFWBGZQRB " singleSelect="true">
       <sec:authorize access="!hasRole('TJY2_RL_GJJ')">
		<qm:param name="name_id" value="<%=userId%>" compare="=" />
		</sec:authorize>
       </qm:qm>
</body>
</html>