<%@page import="java.util.Map"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.alibaba.fastjson.JSONArray"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd"); 
	String nowTime=""; 
	nowTime = dateFormat.format(new Date());
	CurrentSessionUser sessionUser=(CurrentSessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String user=sessionUser.getSysUser().getName();
String userBm= sessionUser.getDepartment().getId();
Map<String,String> roles=sessionUser.getRoles();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript">
var addType=0;//新增权限状态控制，0不可添加；1可添加

var qmUserConfig = {
		sp_defaults:{columnWidth:0.3,labelAlign:'right',labelSeparator:'',labelWidth:120},		
		sp_fields:[
				     {name: "project_name", compare: "like"},
				     {name: "project_department", compare: "like"},
				     {name: "project_head", compare: "like"}
		] , 
		tbar: [ {
			text: '详情', id: 'detail', icon: 'detail', click: detail
		}
		  ,  "-",
		  {text: '新增', id: 'add', icon: 'add', click: add } 
		  , "-", {
				text: '修改', id: 'modify', icon: 'modify', click: modify
			} 
		, "-", {
			text: '删除', id: 'del', icon: 'delete', click: del
		}, "-", {
			text: '提交', id: 'sub', icon: 'submit', click: sub
		}, "-", {
			text: '立项', id: 'ok', icon: 'ok', click: ok
		}, "-", {
			text: '评分', id: 'audit', icon: 'ok', click: audit
		}, "-", {
			text: '审核说明', id: 'remark', icon: 'check-in', click: remark
		}, "-", {
			text: '分配审核人', id: 'distribution', icon: 'userMove', click: distribution
		}, "-", {
			text: '新增权限控制', id: 'control', icon: 'userMove', click: control
		}, "-", {
			text: '复制', id: 'copy', icon: 'copy', click: copy
		}] ,
		
        listeners: {
	        rowClick: function(rowData, rowIndex) {
	        	
	        },
	        rowDblClick: function(rowData, rowIndex) {
		      detail();
	        },	
	        selectionChange: function(rowData, rowIndex) {
	        	   var flag=true;
	        	   var flag1=false;
	        	   var flag2=true;
	        	   var flag3=true;
	        	   var flag4=true;
	        	   var flag5=true;
	        	   var count = Qm.getSelectedCount();
	        	   var status=Qm.getValuesByName("status");
	        	   var create_mans=Qm.getValuesByName("create_man");
	        	   var project_heads=Qm.getValuesByName("project_head");
	        	   var audit_ids=Qm.getValuesByName("audit_id");
	        	   var audit_status=Qm.getValuesByName("audit_status");
	        	   for(var i =0;i<audit_ids.length;i++){
	        		   if(audit_ids[i]=="<%=sessionUser.getId()%>"&&audit_status=="2"){
	        			   flag5=true;
	        		   }
	        	   }
	        	   if(create_mans!="<%=user%>" && project_heads!="<%=user%>"&&'<%=roles.get("402883a05939b237015939d3838d163f")%>'=='null'){
	        		   flag3=false;
	        	   }
	        	   for(var i = 0 ; i <status.length ;i++){
	           		if(status[i]!="0"){
	           			flag=false;
	           			flag1=true;
	           			flag4=false;
	           			if('<%=roles.get("402883a05939b237015939d3838d163f")%>'!=null){
	           				flag=true;
	           			}
	           		}
	           	}
	        	   for(var i=0;i <status.length ;i++){
	        		   if(status[i]!="2"){
		           			flag2=false;
		           		}
	        	   }
		            Qm.setTbar({
		            audit: count==1&&flag5,	
		          	detail: count==1,
		          	alteration: count==1,
		          	cancellation : count>0,
		          	modify:count==1&&flag&&flag3,
		          	del:count>0&&flag&&flag3,
		          	sub:count>0&&flag4&&flag3,
		          	remark:count==1,
		          	ok:count==1&&flag1,
		          	distribution:count>0&&flag2
		       }); 
		            
		            
		            $.ajax({
						url : "tjy2ScientificResearchAction/detail.do?id=10000",
						type : "post",
						async : false,
						success : function(data) {
							if (data.success) {
							//console.log(data.data.projectType);
							if(data.data.projectType=="0" || data.data.projectType==null ){
								 Qm.setTbar({
			 					       	add: false
			 						 });
								 addType=0;//不可添加
							}else{
								addType=1;//可添加
								Qm.setTbar({
		 					       	add: true
		 						 });
							}
							}
						}
					});
		            
	        },   
	        rowAttrRender : function(rowData, rowid) {
	        	 var fontColor="black";
	        	 if (rowData.status=='1'){
	        		 fontColor="blue";
	        	 }
	        	 if (rowData.status=='2'&&rowData.audit_status=='0'){
	        		 fontColor="blue";
	        	 }
	        	 if (rowData.status=='2'&&rowData.audit_status=='1'){
	        		 fontColor="green";
	        	 }
	        	 if(rowData.status=='2'&&rowData.audit_status==''){
	        		 fontColor="green";
	        	 }
	        	 if((rowData.status=='0'||rowData.status=='')&&rowData.project_department=='1'){
	        		 fontColor="red";
	        	 }
	        	 return "style='color:"+fontColor+"'";
	 			}

}
}
		
		function control(){
			//点击修改权限时和当前状态取反
			addType=addType==0?1:0;
			console.log(addType);
			$.ajax({
				url : "tjy2ScientificResearchAction/editeAddType.do?type=" + addType,
				type : "post",
				async : false,
				success : function(data) {
					if (data.success) {
						 Qm.setTbar({
					       	add: addType==1?true:false
						 })
						top.$.notice("设置成功！");
						
					} else {
						top.$.notice("设置失败！");
					}
				}
			});
		}
		

function audit(){
	var audit_status= Qm.getValuesByName("audit_status");
		var url='url:app/fwxm/scientific/scientific_audit_check_fp.jsp?pageStatus=add&id='+Qm.getValuesByName("id");
	 	var id=Qm.getValuesByName("id");
	 	 top.$.dialog({
	         width: 900,
	         height: 600,
	         lock: true,
	         parent: api,
	         data: {
	       	 window: window
	         },
	         title: "科研项目评分审核",
	         content: url
	      });
	}
	function ok(){
	var audit_status= Qm.getValuesByName("audit_status");
		var url='url:app/fwxm/scientific/scientific_audit_check.jsp?pageStatus=add&id='+Qm.getValuesByName("id")+'&project_name='+Qm.getValuesByName("project_name");
	   	var id=Qm.getValuesByName("id");
     	 top.$.dialog({
	         width: 900,
	         height: 600,
	         lock: true,
	         parent: api,
	         data: {
	       	 window: window
	         },
	         title: "科研项目立项审核",
	         content: url
          });
	}
	/* function ok(){
		var audit_status= Qm.getValuesByName("audit_status");
		if(audit_status=="0"){
			var url='url:app/fwxm/scientific/scientific_audit_check_fp.jsp?pageStatus=add&id='+Qm.getValuesByName("id");
		}else{
			var url='url:app/fwxm/scientific/scientific_audit_check.jsp?pageStatus=add&id='+Qm.getValuesByName("id");
		}
	     	var id=Qm.getValuesByName("id");
	     	 top.$.dialog({
		         width: 900,
		         height: 600,
		         lock: true,
		         parent: api,
		         data: {
		       	 window: window
		         },
		         title: "科研项目申请书审核",
		         content: url
	          });
		} */
	  /*  $.ligerDialog.confirm('确认审批？', function (yes){
        if(!yes){return false;}
        top.$.ajax({
                     url: "tjy2ScientificResearchAction/updateConfirm.do?id="+id,
                     type: "GET",
                     dataType:'json',
                     async: false,
                     success:function (data) {
                        if(data.success){
                            $.ligerDialog.success("提交成功！");
                            Qm.refreshGrid();//刷新
                        }else{
                            $.ligerDialog.warn(data.msg);
                        }
                     },
                     error:function () {
                         $.ligerDialog.warn("提交失败！");
                     }
                 });
    }); */
		function del(){
			      $.del("确定删除?", "tjy2ScientificResearchAction/delete.do", {
			       	"ids" : Qm.getValuesByName("id").toString()
			      })
		}
		
		function detail(){
	         top.$.dialog({
		         width: 1000,
		         height: 500,
		         lock: true,
		         parent: api,
		         data: {
		       	 window: window
		         },
		         title: "详情",
		         content: 'url:app/fwxm/scientific/scientific_detail.jsp?pageStatus=detail&id='+Qm.getValueByName("id")
	          });
	         
        }
		function add(){
			top.$.dialog({
		         width: 1000,
		         height: 500,
		         lock: true,
		         parent: api,
		         data: {
		       	 window: window
		         },
		         title: "新增",
		         content: 'url:app/fwxm/scientific/scientific_detail.jsp?pageStatus=add&type=sq'
	          });
		}
        function modify(){
        	top.$.dialog({
		         width: 1000,
		         height: 500 ,
		         lock: true,
		         parent: api,
		         data: {
		       	 window: window
		         },
		         title: "修改",
		         content: 'url:app/fwxm/scientific/scientific_detail.jsp?pageStatus=modify&id='+Qm.getValueByName("id")+"&type=sq"
	          });
        	
        }
        function remark(){
        	top.$.dialog({
		         width: 600,
		         height: 400 ,
		         lock: true,
		         parent: api,
		         data: {
		       	 window: window
		         },
		         title: "审核说明",
		         content: 'url:app/fwxm/scientific/scientific_remark.jsp?pageStatus=detail&id='+Qm.getValueByName("id")
	          });
        	
        }
       function sub(){
    	   //判断值是否完整
    	   $.ajax({
				url : "tjy2ScientificResearchAction/getScientific.do?id="+Qm.getValuesByName("id"),
				type : "POST",
				success : function(data, stats) {
					$("body").unmask();
					if (data["success"]) {
						if(data.flag){
							//申报书填完才可以提交
							 $.ligerDialog.confirm("是否提交？",
										function(yes) {
									    if(yes){
									    	
									    	$.ajax({
							     				url : "tjy2ScientificResearchAction/updateConfirm.do?id="+Qm.getValuesByName("id"),
							     				type : "POST",
							     				data : {
							     					result:"1"
							     				},
							     				success : function(data, stats) {
							     					$("body").unmask();
							     					if (data["success"]) {
							     						
							     						Qm.refreshGrid();
							     						top.$.notice("提交成功！");
							     					} else {
							     						$.ligerDialog.error('提示：' + data);
							     					}
							     				},
							     				error : function(data, stats) {
							     					$("body").unmask();
							     					$.ligerDialog.error('提示：' + JSON.stringify(data));
							     				}
							     			});
									}
								        
								});
						}else{
							$.ligerDialog.error("除协作单位可以不填，其余必填！");
						}
						
					} else {
						$.ligerDialog.error('提示：' + data);
					}
				},
				error : function(data, stats) {
					$("body").unmask();
					$.ligerDialog.error('提示：' + JSON.stringify(data));
				}
			});
    	  
       }
       function distribution(){
    	   top.$.dialog({
		         width: 300,
		         height: 200 ,
		         lock: true,
		         parent: api,
		         data: {
		       	 window: window
		         },
		         title: "选择审核人",
		         content: 'url:app/fwxm/scientific/scientific_detail_cg.jsp?pageStatus=add&id='+Qm.getValuesByName("id")
	          });
       }
       function copy() {
   		$.ligerDialog.confirm('确定复制内容?', function (yes) { 
   				if(yes){
   					$.getJSON('tjy2ScientificResearchAction/copy.do',{id:Qm.getValueByName("id")},function(data){
   						if(data){
   							top.$.notice("复制成功！");
   							api.data.window.submitAction();
   							api.close();
   						}
   					});
   				}
   		});
   	}
       
</script>

</head>
<body>
 <div class="item-tm" id="titleElement">
		<div class="l-page-title">
			<div class="l-page-title-note">提示：列表数据项
				<font color="black">“黑色”</font>代表项目申报书未提交，
				<font color="red">“红色”</font>代表项目申报书审核不通过，
                <font color="blue">“蓝色”</font>代表项目申报书审核中，
                <font color="green">“绿色”</font>代表项目申报书现场评审中。
			</div>
		</div>
	</div>
       <qm:qm pageid="scientific_list">
      	<!--  若无科研项目管理权限或则项目申报查看权限，则只查看自己的相关数据 -->
       <%if(roles.get("402883a05939b237015939d3838d163f")==null && roles.get("402883a25f4dd138015f52ba0888584f")==null){ %>
       <qm:param name="create_man" value="<%=sessionUser.getName()%>" compare="="  />
       <qm:param name="project_head" value="<%=sessionUser.getName()%>" compare="=" logic="or" />
       <qm:param name="audit_id" value="<%=sessionUser.getId()%>" compare="like" logic="or" />
       <%}%> 
       </qm:qm>
</body>
</html>