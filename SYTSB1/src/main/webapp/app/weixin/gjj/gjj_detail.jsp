<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>

<!DOCTYPE html>
<html>
<head>
<title>公积金信息</title>
<meta name="viewport"
	content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/app/k/km/lib/app-end.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/k/km/lib/kh-mobile.css" />
<script src="${pageContext.request.contextPath}/app/k/km/lib/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}	/app/k/km/lib/kh-mobile.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/pub/bpm/js/util.js"></script>
<%
String ids = (String)session.getAttribute("Name");
String phone = (String)session.getAttribute("Phone");
String accessToken = (String)session.getAttribute("AccessToken");

%>
<script type="text/javascript">
var ids ='<%=ids%>';
var phone ='<%=phone%>';
yids='';
$(function(){
	  $.ajax({
		  url:"${pageContext.request.contextPath}/Tjy2GjjAction/RL/getBusinessId.do?phone="+'<%=phone%>',
          type: "POST",
          dataType:'json',
          async: false,
          success:function (data) {
        	  if(data.success){
    			  yids=data.id;//alert(yids);
	   			  $("#form1").setValues("${pageContext.request.contextPath}/Tjy2GjjAction/RL/detail.do?id="+yids,function(res){
	   				 
	   				  var a=res.data.jySj;
	   				   $("#jySj").html(a.substring(0,7));
	   				 	var b=res.data.zrzlrzSjA;
	   				 	if(b==null){
	   				 	  $("#zrzlrzSjA").html();
	   				 	}else{
	   				 	 $("#zrzlrzSjA").html(b.substring(0,7));
	   				 	}
	   				   var c=res.data.fzrzSj;
	   				 	if(c==null){
	   				 	  $("#fzrzSj").html();
	   				 	}else{
	   				 	 $("#fzrzSj").html(c.substring(0,7));
	   				 	}
	   				   var d=res.data.zzrzSj;
	   				 	if(d==null){
	   				 	  $("#zzrzSj").html();
	   				 	}else{
	   				 	 $("#zzrzSj").html(d.substring(0,7));
	   				 	}
	   				   var e=res.data.gcsrzSj;
	   				 	if(e==null){
	   				 	  $("#gcsrzSj").html();
	   				 	}else{
	   				 	 $("#gcsrzSj").html(e.substring(0,7));
	   				 	}
	   				 var f=res.data.gjgcsrzSj;
	   				 	if(f==null){
	   				 	  $("#gjgcsrzSj").html();
	   				 	}else{
	   				 	 $("#gjgcsrzSj").html(f.substring(0,7));
	   				 	}
	   				 var g=res.data.jysrz_Sj;
	   				 	if(g==null){
	   				 	  $("#jysrz_Sj").html();
	   				 	}else{
	   				 	 $("#jysrz_Sj").html(g.substring(0,7));
	   				 	}
	   				 	//alert(res.data.brqr);
	   				 if(res.data.brqr=="1"){
	   					$("#next1").hide();
	   				  }
	   			  });
    		  }
          }
            
      });
	  getcodetabl("TJY2_XL","xl"); 
	  getcodetabl("TJY2_GJJQR","brqr");
      $("#form1").initForm({
			 getSuccess: function (res){
					if(res.data==null||res.success=="false"){
						alert("加载失败！");
					}
					
			}
 	});		
  

		 $("#next1").click(function() {
			$.ajax({
				url : "${pageContext.request.contextPath}/Tjy2GjjAction/RL/submit1.do?id="+yids,
				type : "POST",
				datatype : 'json',
				contentType: "application/json; charset=utf-8",
		      success : function (data) {
		      	if(data.success){  
		      		 dAlert("确认成功！");
						//$("#form1").transform("detail");   
						$("#next1").hide();
				       $("#form1").setValues("${pageContext.request.contextPath}/Tjy2GjjAction/RL/detail.do?id="+yids,function(res){
				    	   var a=res.data.jySj;
		   				   $("#jySj").html(a.substring(0,7));
		   				 	var b=res.data.zrzlrzSjA;
		   				 	if(b==null){
		   				 	  $("#zrzlrzSjA").html();
		   				 	}else{
		   				 	 $("#zrzlrzSjA").html(b.substring(0,7));
		   				 	}
		   				   var c=res.data.fzrzSj;
		   				 	if(c==null){
		   				 	  $("#fzrzSj").html();
		   				 	}else{
		   				 	 $("#fzrzSj").html(c.substring(0,7));
		   				 	}
		   				   var d=res.data.zzrzSj;
		   				 	if(d==null){
		   				 	  $("#zzrzSj").html();
		   				 	}else{
		   				 	 $("#zzrzSj").html(d.substring(0,7));
		   				 	}
		   				   var e=res.data.gcsrzSj;
		   				 	if(e==null){
		   				 	  $("#gcsrzSj").html();
		   				 	}else{
		   				 	 $("#gcsrzSj").html(e.substring(0,7));
		   				 	}
		   				 var f=res.data.gjgcsrzSj;
		   				 	if(f==null){
		   				 	  $("#gjgcsrzSj").html();
		   				 	}else{
		   				 	 $("#gjgcsrzSj").html(f.substring(0,7));
		   				 	}
		   				 var g=res.data.jysrz_Sj;
		   				 	if(g==null){
		   				 	  $("#jysrz_Sj").html();
		   				 	}else{
		   				 	 $("#jysrz_Sj").html(g.substring(0,7));
		   				 	}
		   				 	
				      });     
		      	}else{
		      		//$("body").unmask();
		      		dialogShow(data.msg, 300, 100);
		      	}
		      }
			});  		
		
		});
	});

function getcodetabl(code,id){   
 $.ajax({
	url : "${pageContext.request.contextPath}/Tjy2GjjAction/RL/getcodetabl.do?tablname="+code,
	type : "POST",
	async : false,
	success : function(json) {		
		var	s="";
		if(json.success){
		for(var i in json.data) {	

		s+="<option value='"+json.data[i].value+"'>"+json.data[i].name+"</option>";				    				  				
		}
		$("#"+id).html(s);	
		
		}
	} 
 });
}
</script>

</head>
<body>



        <div class="container">		
		<div class="page-header" align="center">
			<h1></h1>
		</div>
			<div class="page-panel" id="m-page-panel">
				<form id="form1" name="form1" method="post" action="" onsubmit="return false;" getAction="" pageStatus="detail">
					<table id="gjjs" border="0" cellpadding="0" cellspacing="0" width="100%" height="" align="">
						<tr class="tr1">
							<td width="100" class="tdtext1">姓名：</td>
							<td><input type="text" class="form-control" name="name"  id="name"   ext_name="" ext_isNull="1" ></td>
						</tr>				
						<tr class="tr1">
							<td width="100" class="tdtext1">部门：</td>
							<td><input type="text" class="form-control" name="org"  id="org"   ext_name="" ext_isNull="1" ></td>
						</tr>	
						<tr class="tr1">
							<td width="100" class="tdtext1">试用期满月缴费基数：</td>
							<td><input type="text" class="form-control" name="syqmyjyJs"  id="syqmyjyJs"   ext_name="" ext_isNull="1" ></td>
						</tr>	
			            <tr class="tr1">
							<td class="tdtext1">学历：</td>
							<td><select class="form-control" id="xl" name="xl"  ext_name="" ></select></td>
						</tr>
				        <tr class="tr1">
							<td class="tdtext1">进院时间：</td>
							<td><input type="text" class="form-control" id="jySj" name="jySj" ext_name="" ext_isNull="1" ligerui="{format:'yyyy-MM'}" ></td>
						</tr>
						<tr class="tr1">
							<td class="tdtext1">主任助理任职时间：</td>
							<td><input type="text" class="form-control" id="zrzlrzSjA" name="zrzlrzSjA"  ext_name="" ext_isNull="1" ></td>
						</tr>
						<tr class="tr1">
							<td class="tdtext1">副职任职时间：</td>
							<td><input type="text" class="form-control" id="fzrzSj" name="fzrzSj"  ext_name="" ext_isNull="1" ></td>
						</tr>
						<tr class="tr1">
							<td class="tdtext1">正职任职时间：</td>
							<td><input type="text" class="form-control" id="zzrzSj" name="zzrzSj" ext_name="" ext_isNull="1" ></td>
						</tr>
					
						
						<tr class="tr1">
							<td class="tdtext1">工程师任职时间：</td>
							<td><input type="text" class="form-control" id="gcsrzSj" name="gcsrzSj"  ext_name="" ext_isNull="1" ></td>
						</tr>
						<tr class="tr1">
							<td class="tdtext1">高级工程任职时间：</td>
							<td><input type="text" class="form-control" id="gjgcsrzSj" name="gjgcsrzSj" ext_name="" ext_isNull="1" ></td>
						</tr>
						<tr class="tr1">
							<td class="tdtext1">检验师任职时间：</td>
							<td><input type="text" class="form-control" id="jysrz_Sj" name="jysrz_Sj"  ext_name="" ext_isNull="1" ></td>
						</tr>
			            
<!-- 						<tr class="tr1"> -->
<!-- 							<td class="tdtext1">确认状态：</td> -->
<!-- 							<td><select class="form-control" name="brqr"  id="brqr"   ext_name="申请状态" ext_isNull="1" ></select></td> -->
<!-- 						</tr> -->
			  </table>
		</form>
		</div>
		<div class="bt1" >
				<div class="text-center row">
					<div class=""><a id="next1" class="button button-block button-rounded button-primary">确认</a></div>
				</div>
			</div>
		</div>
</body>
</html>
		