﻿<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.status}">
<title></title>
<%@ include file="/k/kui-base-form.jsp"%>
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
<style>
	body{
		margin:0;
		padding:0;
		font-size: 14px;
	}
	#formObj{
		width:1000px;
		margin:0 auto;
	}
	h2{
		text-align: center;
		font-size: 16px;
	}
	#formObj table{
		width:100%;
		margin:10px 0;
		
	}
	#formObj table tr td{
		min-height:30px;
		border:1px solid #ccc;
		line-height:100%;
		text-align:center;
	}
	#formObj table tr td.lb{
		width:15%;
		text-align: right;
	}
	#formObj table tr td.txt{
		width:35%;
		text-align:center;
	}
	#formObj table tr td image{
		width:400px;
		height:200px;
	}
</style>
<script type="text/javascript" src="app/common/js/idCard.js"></script>
<script type="text/javascript" src="app/common/js/windowprint.js"></script>
<%
	String pageStatus = request.getParameter("status");
%>
<script type="text/javascript">
	var pageStatus = "${param.status}";		
	$(function() {
		
	    $("#formObj").initForm({
			toolbar: [
	      		{
	      			text: "打印", 
	      			icon: "print", 
	      			click: function(){
	      				myPrint(document.getElementById("formObj"));
	      			}
	      		},
				{
					text: "关闭", icon: "cancel", click: function(){
						api.close();
					}
				}
			], 
			toolbarPosition: "bottom"
		});	
	    var row = api.data.row;
	    $("#pulldown_op").html(row.pulldown_op);
	    $("#job_unit").html(row.report_com_name);
	    $("#regional_name").html(row.regional_name);
	    
	    $("#mdy_user_name").html(row.mdy_user_name);
	    $("#linkmode").html(row.linkmode);
	    $("#pulldown_time").html(row.pulldown_time);
	    //单独处理项,设备类别
	    var device_type = row.device_sort_code.substr(0,1);
	    var d_type = "";
		// 电梯要显示安装区域，游乐设施和起重机械不需要显示安装区域
		if(device_type == "4"){	// 起重机械
			d_type = "起重机械";// 设备类型
		}else if(device_type == "6"){	// 游乐设施
			d_type = "游乐设施";// 设备类型
		}else if(device_type == "3"){	// 电梯
			d_type = "电梯";// 设备类型
		}				 
		$("#device_type").html(d_type);
	  	//单独处理项,签名
	  	/* $.post("report/draw/getReportDrawSign.do",{id:row.id},function(res){
	  		if(res.success){ */
	  			//报告书编号
	  			/* var report_sn = '';
	  			if(res.one){
	  				report_sn = res.one;	
	  			}else{
	  				var seq = res.seq;
		  			var noseq = res.noseq;
		  			var flag = false;
		  			$.each(seq,function(key,val){
		  				if(flag){
		  					report_sn += "|"+key;
			  				for(var i=0;i<val.length;i++){
			  					var last = val[i].length-1;
			  					if(i>0){
			  						report_sn +=",["+val[i][0]+"-"+val[i][last]+"]";
			  					}else{
			  						report_sn +="["+val[i][0]+"-"+val[i][last]+"]";
			  					}
			  				}
		  				}else{
		  					report_sn += key;
			  				for(var i=0;i<val.length;i++){
			  					var last = val[i].length-1;
			  					if(i>0){
			  						report_sn +=",["+val[i][0]+"-"+val[i][last]+"]";
			  					}else{
			  						report_sn +="["+val[i][0]+"-"+val[i][last]+"]";
			  					}
			  				}
			  				flag = true;
		  				}
		  				
		  			});
		  			var flag2=false;
		  			$.each(noseq,function(key,val){
		  				if(flag){
		  					report_sn += "|"+key;
		  					for(var i=0;i<val.length;i++){
		  						if(flag2){
			  						report_sn +=",["+val[i]+"]";
			  					}else{
			  						report_sn +="["+val[0]+"]";
			  						flag2 = true;
			  					}
		  					}
		  				}else{
		  					report_sn += key;
		  					for(var i=0;i<val.length;i++){
		  						if(flag2){
			  						report_sn +=",["+val[i]+"]";
			  					}else{
			  						report_sn +="["+val[0]+"]";
			  						flag2 = true;
			  					}
		  					}
			  				flag = true;
		  				}
		  			});	
	  			} */
	  			/* console.log(res.rows);
	  			var rows = res.rows;
	  			var htm = ""
	  			$.each(rows,function(i,row){
	  				if(i==0){
	  					htm+=row;
	  				}else{
	  					htm+="，"+row;
	  				}
	  				
	  			});
	  			$("#info_report_sn").html(htm);
	  			//份数
	  			$("#count").html(res.rows.length);
	  			//签名
	  			$("table tr td image").attr("src",res.image);
	  		}else{
	  			alert(res.msg);
	  		}
	  	}); */
	  	
	  	$.post("report/draw/reportDrawDetail2.do",{id:row.id},function(res){
	  		if(res.success){
	  			//$("table tr td image").attr("src",res.image);
	  			document.getElementById("signImg").src = "data:image/png;base64,"+res.image;
	  			$("#info_report_sn").html(res.formatReportSn);
	  			var sn_arr=res.reportSn.split(",");
	  			$("#count").html(sn_arr.length);
	  		}
	  	})
	  	
	});
	function myPrint(obj) 
	{ 
		var oldstr = document.body.innerHTML; 
		var newstr = obj.outerHTML; 
		document.body.innerHTML = newstr; 
		if(getExplorer() == "IE"){
			pagesetup_null();
		}
		window.print(); 
		document.body.innerHTML = oldstr; 
		$("a[ligeruiid='Button1000']").bind('click',function(){
			myPrint(document.getElementById("formObj"));
		}); 
		$("a[ligeruiid='Button1001']").bind('click',function(){
			api.close();
		});  
		return false;  
	} 

	function pagesetup_null(){                
	    var hkey_root,hkey_path,hkey_key;
	    hkey_root="HKEY_CURRENT_USER";
	    hkey_path="//Software//Microsoft//Internet Explorer//PageSetup//";
	    try{
	        var RegWsh = new ActiveXObject("WScript.Shell");
	        hkey_key="header";
	        RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
	        hkey_key="footer";
	        RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
	    }catch(e){}
	}

	function getExplorer() {
	    var explorer = window.navigator.userAgent ;
	    //ie
	    if (explorer.indexOf("MSIE") >= 0) {
	        return "IE";
	    }
	    //firefox
	    else if (explorer.indexOf("Firefox") >= 0) {
	        return "Firefox";
	    }
	    //Chrome
	    else if(explorer.indexOf("Chrome") >= 0){
	        return "Chrome";
	    }
	    //Opera
	    else if(explorer.indexOf("Opera") >= 0){
	        return "Opera";
	    }
	    //Safari
	    else if(explorer.indexOf("Safari") >= 0){
	        return "Safari";
	    }
	}
</script>
</head>
<body>
	<form name="formObj" id="formObj" method="post" action="">
		<h2>四川省特种设备检验研究院报告领取记录</h2>
		<table>
			<tr>
				<td colspan = "4" style="text-indent:10px;">今&nbsp;<span id="pulldown_op"></span>&nbsp;领取省特检院检验报告（定检/验收）<span id="count"></span> 份。</td>
			</tr>
			<tr>
				<td class="lb">报告使用单位：</td><td id="job_unit" name="job_unit" class="txt"></td>
				<td class="lb">安装区域：</td><td id="regional_name" name="regional_name" class="txt"></td>
			</tr>
			<tr>
				<td class="lb">设备类型：</td><td class="txt" id="device_type" name="device_type"><</td>
				<td class="lb">报告书编号：</td><td class="txt" id="info_report_sn" name="info_report_sn"></td>
			</tr>
			<tr>
				<td class="lb">经办人：</td><td class="txt" id="mdy_user_name" name="mdy_user_name"></td>
				<td class="lb">领取人电话：</td><td class="txt" id="linkmode" name="linkmode"></td>
			</tr>
			<tr>
				<td class="lb">领取人（签字）确认：</td>
				<td colspan="3">
					<!--<image src=""/>-->
					<img id="signImg" style="width:220px;vertical-align: text-bottom;" src="" />
				</td>
			</tr>
			<tr>
				<td class="lb">领取日期：</td><td colspan="3" id="pulldown_time" name="pulldown_time"></td>
			</tr>
		</table>
		
	</form>
</body>
</html>