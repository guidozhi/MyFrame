<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %> 
<%@page import="java.text.SimpleDateFormat"%>
<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<%
CurrentSessionUser user = SecurityUtil.getSecurityUser();
User uu = (User)user.getSysUser();
com.khnt.rbac.impl.bean.Employee e = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
e.getId();
String userId=e.getId();
String uId = SecurityUtil.getSecurityUser().getId();
%>

    <title></title>
    <%@include file="/k/kui-base-form.jsp" %>
	 <script type="text/javascript" src="pub/bpm/js/util.js"></script>
	 <link type="text/css" rel="stylesheet" href="app/finance/css/form_detail.css" />
	 <script type="text/javascript" src="app/common/lodop/LodopFuncs.js"></script>
	 <link rel="Stylesheet" href="app/finance/css/jquery.autocomplete.css" />
     <script type="text/javascript">
     var businessPart="100020,100021,100022,100023,100024,100063,100034,100035,100037,100033,100065,100036,100049,100066,100067,100068"
    	 var pageStatus = "${param.pageStatus}";
    var tbar="";
    var isChecked="${param.isChecked}";
    var serviceId = "${requestScope.serviceId}";//提交数据的id
	var activityId = "${requestScope.activityId}";//流程id
	var areaFlag;//改变状态
     <bpm:ifPer function="TJY2_CW_JK_cwsh" activityId = "${requestScope.activityId}">areaFlag="1";</bpm:ifPer>
  	 <bpm:ifPer function="TJY2_CW_JK_bmld" activityId = "${requestScope.activityId}">areaFlag="2";</bpm:ifPer>
  	 <bpm:ifPer function="TJY2_CW_JK_fgyld" activityId = "${requestScope.activityId}">areaFlag="3";</bpm:ifPer>
  	 <bpm:ifPer function="TJY2_CW_JK_dwfzr" activityId = "${requestScope.activityId}">areaFlag="4";</bpm:ifPer>
  	 
    $(function () {
    	//$("#areaFlag").val(areaFlag);
//    	if(isChecked!="" && typeof(isChecked)!="undefined"){
//        	$("#jksq").transform("detail",function(){});
//   	    	$("#jksq").setValues("money/borrow/detail.do?id=${requestScope.serviceId}");
//         	 tbar=[{ text: '审核不通过', id: 'del', icon: 'del', click: nosubmitSh},
//                    { text: '审核通过', id: 'submit', icon: 'submit', click: submitSh},
//                    { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
//         } else {alert(11);
    	//默认隐藏分管领导签字部分
        $("#fgld").hide();
        
        if(pageStatus=="add"){
            var orgId = <%=user.getDepartment().getId() %>;
            //如果是新增，申请部门id设为当前登录者所在部门的id
            $("#departmentId").val(orgId);
        
            //如果是新增，并且当前登录人所在部门为业务部门（机电、承压），则将管领导签字部分显示出来
            if(businessPart.indexOf(orgId)>-1){
                $("#fgld").show();
            }
        }
		if ("${param.status}"=="a"){
            tbar=[{ text: '保存', id: 'up1', icon: 'save', click: directChange},
                { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];}
//        }
        if ("${param.pageStatus}"=="detail"){
        tbar=[	<sec:authorize access="hasRole('TJY2_CW_CHECK')">
			      { text: '打印', id: 'print', icon: 'print', click: print},
			      </sec:authorize>
        	  { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];}
        if ("${param.pageStatus}"=="detail" && "${param.a}"=="a"){
        	if("${param.jkstatus}"=="1"){
        		tbar=[{ text: '取消确认', id: 'submit', icon: 'submit', click: unjkqr},
              	  { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}
              ];
        	}else if("${param.jkstatus}"=="0"){
        		tbar=[{ text: '确认', id: 'submit', icon: 'submit', click: jkqr},
              	  { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}
              ];
        	}
            }
        if ("${param.status}"=="aa"){
        	tbar=[ { text: '保存', id: 'up', icon: 'save', click: directChange1},
        	       { text: '审核', id: 'chu', icon: 'dispose', click: chu},
        			<sec:authorize access="hasRole('TJY2_CW_CHECK')">
                   { text: '打印', id: 'print', icon: 'print', click: print},
                   </sec:authorize>
             	  { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];}
    	$("#form1").initForm({
            showToolbar: true,
            toolbarPosition: "bottom",
            toolbar: tbar,
            success: function (response) {
            	
				if(response.success){
					top.$.notice("保存成功！",3);
					api.data.window.Qm.refreshGrid();
					if("${param.pageStatus}" != "edit"){
						api.close();
						
					}
				}else{
					$.ligerDialog.error("操作失败！<br/>" + response.msg);
				}
			},
            getSuccess : function(res) {
            	
            	if(res.data.status=='CSTG' && "${param.status}"=="aa"){
            		$("#chu").hide();
                    $("#up").hide();
                    //form1   jksq
                	$("#form1").transform("detail",function(){});
           	    	$("#form1").setValues(res.data);
                    //toDetail();
                }
                var zje=res.data.import_money;
                var money = zje.toString();
                var temp = money.split(".");
                if(temp[1]){
                	if(temp[1].length==1){
                		var a = zje+"0";
                        $("#import_money").val(a);
                        //$("#import_money").html(a);
                        $("#money").val(a);
                        //$("#money").html(a);
                	}
                }else{
                    var a = zje+".00";
                    $("#import_money").val(a);
                    //$("#import_money").html(a);
                    $("#money").val(a);
                    //$("#money").html(a);
                }
            	if ("${param.pageStatus}"=="modify"){
            		if(temp[0].length>0){
                    $("#yuan").val(temp[0].charAt(temp[0].length-1));}
                    if(temp[0].length>1){
                    $("#shi").val(temp[0].charAt(temp[0].length-2));}
                    if(temp[0].length>2){
                    $("#bai").val(temp[0].charAt(temp[0].length-3));}
                    if(temp[0].length>3){
                    $("#qian").val(temp[0].charAt(temp[0].length-4));}
                    if(temp[0].length>4){
                    $("#wan").val(temp[0].charAt(temp[0].length-5));}
                    if(temp[0].length>5){
                    $("#shiwan").val(temp[0].charAt(temp[0].length-6));}
                    if(temp[1]){
	                    if(temp[1].length==1){
	                    $("#jiao").val(temp[1].charAt(temp[1].length-1));
	                    $("#fen").val("0");}
	                    if(temp[1].length>1){
                    	$("#jiao").val(temp[1].charAt(temp[1].length-2));
	                    $("#fen").val(temp[1].charAt(temp[1].length-1));}
	                }else{
                        $("#jiao").val("0");
                        $("#fen").val("0");
                     }
            	}else if("${param.pageStatus}"=="detail"){
            		if(temp[0].length>0){
                        $("#yuan").html(temp[0].charAt(temp[0].length-1));}
                        if(temp[0].length>1){
                        $("#shi").html(temp[0].charAt(temp[0].length-2));}
                        if(temp[0].length>2){
                        $("#bai").html(temp[0].charAt(temp[0].length-3));}
                        if(temp[0].length>3){
                        $("#qian").html(temp[0].charAt(temp[0].length-4));}
                        if(temp[0].length>4){
                        $("#wan").html(temp[0].charAt(temp[0].length-5));}
                        if(temp[0].length>5){
                        $("#shiwan").html(temp[0].charAt(temp[0].length-6));}
                        if(temp[1]){//alert(114);
        	                if(temp[1].length==1){
                        	var a = zje+"0";
                            $("#import_money").html(a);
                            $("#money").html(a);	
        	                $("#jiao").html(temp[1].charAt(temp[1].length-1));
        	                $("#fen").html("0");}
        	                if(temp[1].length>1){
        	                $("#jiao").html(temp[1].charAt(temp[1].length-2));
        	                $("#fen").html(temp[1].charAt(temp[1].length-1));}
        	                //alert(temp[1].charAt(temp[1].length-1));
                        }else{
                        	var a = zje+".00";
                            $("#import_money").html(a);
                            $("#money").html(a);
                            $("#jiao").html("0");
                            $("#fen").html("0");
                         }
            	}
              	//如果申请部门id不为空，并且属于业务部门（机电、承压），则将管领导签字部分显示出来
                if(res.data.departmentId!="" && businessPart.indexOf(res.data.departmentId)>-1){
                    $("#fgld").show();
                }
                }
    	});
    	$("body").append('<object style="height:1px;" id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0><embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed></object>');
//     	$('#borrower').autocomplete("employee/basic/searchEmployee.do", {
//             max: 12,    //列表里的条目数
//             minChars: 1,    //自动完成激活之前填入的最小字符
//             width: 200,     //提示的宽度，溢出隐藏
//             scrollHeight: 300,   //提示的高度，溢出显示滚动条
//             scroll: false,   //当结果集大于默认高度时是否使用卷轴显示
//             matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
//             autoFill: false,    //自动填充
//             formatItem: function(row, i, max) {
//                 return row.name + '   ' + row.mobileTel;
//             },
//             formatMatch: function(row, i, max) {
//                 return row.name + row.mobileTel;
//             },
//             formatResult: function(row) {
//                 return row.name;
//             }
//         }).result(function(event, row, formatted) {
//             //alert(row.id);
//             $("#borrowerId").val(row.id);
//         }); 
    	$('#department').autocomplete("employee/basic/searchOrg.do", {
            max: 12,    //列表里的条目数
            minChars: 1,    //自动完成激活之前填入的最小字符
            width: 200,     //提示的宽度，溢出隐藏
            scrollHeight: 300,   //提示的高度，溢出显示滚动条
            scroll: false,   //当结果集大于默认高度时是否使用卷轴显示
            matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
            autoFill: false,    //自动填充
            formatItem: function(row, i, max) {
            //alert(row);
                return row.orgName;
            },
            formatMatch: function(row, i, max) {
                return row.orgName;
            },
            formatResult: function(row) {
                return row.orgName;
            }
        }).result(function(event, row, formatted) {
           // alert(row.orgId);
            //$("#departmentId").val(row.orgId);

        });
    	
    });
    window.onload = function() { 
    	$('#unit-txt').change(function() {
			var unit = $("#unit-txt").val();
			if("四川省特种设备检验研究院" == unit || "中共四川省特种设备检验研究院委员会" == unit || "四川省特种设备检验检测协会" == unit){
				//$("#departmentId").val("");
				$('#department').val(""); 
			}else if("鼎盛公司" == unit){
				$("#departmentId").val("100052");
    			$("#department").val(unit);
			}else if("司法鉴定中心" == unit){
				$("#departmentId").val("100044");
    			$("#department").val(unit);
			}else if("四川省特种设备检验研究院工会委员会" == unit){
				$("#departmentId").val("100059");
    			$("#department").val(unit);
			}else if("基建办" == unit){
				$("#departmentId").val("100050");
    			$("#department").val(unit);
			}
		});
    };  
    function print(){
       var LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
       var strBodyStyle = "<style>" + $("#pstyle").html() + "</style>";
       LODOP.PRINT_INIT("打印借款单");
       LODOP.ADD_PRINT_HTM('120px', '100px', "770px", "520px", strBodyStyle + $("#form1").html());
       LODOP.SET_PRINT_PAGESIZE (2, 0, 0,"B5(ISO)");
       LODOP.PRINT();
    }
    function chu() {
		var id = "${param.id}";
    	$.ligerDialog.confirm('是否要审批？', function (yes){
          	 if(!yes){return false;}
              top.$.ajax({
                   url: "money/borrow/cwshtg.do?id="+id,
                   type: "POST",
                   dataType:'json',
                   async: false,
                   success:function (data) {
                   	$("body").unmask();
                           api.data.window.Qm.refreshGrid();
                       //api.close();
                       top.$.dialog.notice({content:'操作成功！'});
                       toDetail();
                       api.data.window.reLoad();
                       //setTimeout("close()","300");
                   },
                   error:function () {
                   	 $("body").unmask();
                       $.ligerDialog.error("审批失败！");
                        $("#save").removeAttr("disabled");
                   }
               });
          });
//		alert(id);
// 		var status = "aa";
// 		top.$.dialog({
// 			width: 600,
// 			height: 250,
// 			lock: true,
// 			parent: api,
// 			data: {
// 				window: window
// 			},
// 			title: "财务审核",
// 			content: 'url:app/finance/finance_borrow_yijian.jsp?id='+id+'&status='+status+'&pageStatus=add'
// 		});
	}	
    function nosubmitSh(){
    	$.ligerDialog.confirm('是否要不通过审核？', function (yes){
           if(!yes){return false;}
    	 $("body").mask("正在处理，请稍后！");
    	 
    	 getServiceFlowConfig("TJY2_CW_JK","",function(result,data){
             if(result){
                  top.$.ajax({
                      url: "money/borrow/cwth.do?id="+serviceId+
                     		 "&typeCode=TJY2_CW_JK&status="+"&activityId="+activityId+"&areaFlag="+areaFlag,
                      type: "GET",
                      dataType:'json',
                      async: false,
                      success:function (data) {
                          if (data) {
                             $.ligerDialog.alert("操作成功！！！");
                             api.data.window.Qm.refreshGrid();//刷新
                             $("body").unmask();
                          }
                      },
                      error:function () {
                          $.ligerDialog.alert("出错了!！请重试！");
                          $("body").unmask();
                      }
                  });
             }else{
              $.ligerDialog.alert("出错了！请重试！");
              $("body").unmask();
             }
          });
     });
    }
    function submitSh(){
    	var serviceId = "${requestScope.serviceId}";//提交数据的id
    	var activityId = "${requestScope.activityId}";//流程id
        $.ligerDialog.confirm('是否提交审核？', function (yes){
        if(!yes){return false;}
         $("body").mask("提交中...");
         getServiceFlowConfig("TJY2_CW_JK","",function(result,data){
                if(result){
                     top.$.ajax({
                         url: "money/borrow/cwtj.do?id="+serviceId+
                        		 "&typeCode=TJY2_CW_JK&status="+"&activityId="+activityId+"&areaFlag="+areaFlag,
                         type: "GET",
                         dataType:'json',
                         async: false,
                         success:function (data) {
                             if (data) {
                                $.ligerDialog.alert("审核成功！！！");
                                //api.clsoe();
                                api.data.window.Qm.refreshGrid();//刷新
                                $("body").unmask();
                             }
                         }
                     });
                }else{
                 $.ligerDialog.alert("出错了！请重试！");
                 $("body").unmask();
                }
             });
        });
    	
    }
    function save(){
   	 var obj=$("#form1").validate().form();
   	 if(obj){
   		 $("#form1").submit();
   	 }else{
   		 return;
   	}
   }
   function close(){
   	if(api.data.window.submitAction)
   		api.data.window.submitAction();
   		api.close();
   }
   //计算和
   function sumAmount() {
	    var cash = document.getElementById("cash").value;
	    var money = document.getElementById("money").value;
	    var sumamount = document.getElementById("import_money");
	    var sumAmount = parseFloat(cash == "" ? 0 : cash) + parseFloat(money == "" ? 0 : money) ;
	    sumamount.value = sumAmount == 0 ? "" : sumAmount.toFixed(2); 
	    //document.getElementById("rmb").value = digit_uppercase(sumAmount);
	    digit_uppercase(sumAmount);
	}
   function digit_uppercase(n) {
	   $.ajax({
       	url:  "moneyConvertAction/moneyConvert.do?sumAmount="+n.toString(),
           type: "POST",
           datatype: "json",
           contentType: "application/json; charset=utf-8",
           success: function (resp) {
           	if(resp.success){
           		//alert(resp.sumAmountSup);
           		document.getElementById("rmb").value=resp.sumAmountSup;
           	}else{
           		$.ligerDialog.alert("请检查输入金额是否正确！");
           	}
           },
           error: function (data) {
           	$.ligerDialog.alert("请检查输入金额是否正确！");
           }
       });
	    /* var fraction = ['角', '分'];
	    var digit = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'];
	    var unit = [['元', '万', '亿'], ['', '拾', '佰', '仟']];
	    var head = n < 0 ? '欠' : '';
	    n = Math.abs(n);
	    var s = '';
	    for (var i = 0; i < fraction.length; i++) {
	        s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
	    }
	    s = s || '整';
	    n = Math.floor(n);
	    for (var i = 0; i < unit[0].length && n > 0; i++) {
	        var p = '';
	        for (var j = 0; j < unit[1].length && n > 0; j++) {
	            p = digit[n % 10] + unit[1][j] + p;
	            n = Math.floor(n / 10);
	        }
	        s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
	    }
	    return head + s.replace(/(零.)*零元/, '元').replace(/(零.)+/g, '零').replace(/^整$/, '零元整'); */
	}
   function getMonyNum(obj) {
       document.getElementById(obj).focus();
       //document.getElementById(obj).value = '';
       var d_jiao, d_fen, d_yuan, d_shi, d_bai, d_qian, d_wan, d_shiwan, tmp_yuan = "";

       d_jiao = document.getElementById("jiao").value;
       d_fen = document.getElementById("fen").value;

       d_yuan = document.getElementById("yuan").value;
       d_shi = document.getElementById("shi").value;
       d_bai = document.getElementById("bai").value;
       d_qian = document.getElementById("qian").value;
       d_wan = document.getElementById("wan").value;
       d_shiwan = document.getElementById("shiwan").value;

       if (d_shiwan != "") { tmp_yuan += d_shiwan; }
       if (d_wan == "" && tmp_yuan != "") { d_wan = "0"; }
       tmp_yuan += d_wan;
       if (d_qian == "" && tmp_yuan != "") { d_qian = "0"; }
       tmp_yuan += d_qian;
       if (d_bai == "" && tmp_yuan != "") { d_bai = "0"; }
       tmp_yuan += d_bai;
       if (d_shi == "" && tmp_yuan != "") { d_shi = "0"; }
       tmp_yuan += d_shi;
       if (d_yuan == "" && tmp_yuan != "0") { d_yuan = "0"; }
       tmp_yuan += d_yuan;
       tmp_yuan += ".";
       if (d_jiao == "") { d_jiao = "0"; }
       tmp_yuan += d_jiao;
       if (d_fen == "") { d_fen = "0"; }
       tmp_yuan += d_fen;
//        for(var i=0;i<tmp_yuan.length;i++){
//            if(tmp_yuan.charAt(i)>0){
//                tmp_yuan = tmp_yuan.substring(i,tmp_yuan.length);
//                break;
//            }
//          }
       //document.getElementById("import_money").value = tmp_yuan;
       document.getElementById("money").value = tmp_yuan;
       sumAmount();
   }

    function setMony() {
       var sumAmount = document.getElementById("money").value;

       var d_jiao, d_fen, d_yuan, d_shi, d_bai, d_qian, d_wan, d_shiwan, tmp_yuan;
       if (sumAmount.length > 0) {
           if (sumAmount.indexOf(".") > 0) {
               var tmp_yuan = sumAmount.substring(0, sumAmount.indexOf("."));
               document.getElementById("jiao").value = sumAmount.substring(sumAmount.indexOf(".") + 1, sumAmount.indexOf(".") + 2);
               document.getElementById("fen").value = sumAmount.substring(sumAmount.indexOf(".") + 2, sumAmount.indexOf(".") + 3);
           } else {
               tmp_yuan = sumAmount;
           }
           document.getElementById("yuan").value = tmp_yuan.substring(tmp_yuan.length, tmp_yuan.length - 1);
           document.getElementById("shi").value = tmp_yuan.substring(tmp_yuan.length - 1, tmp_yuan.length - 2);
           document.getElementById("bai").value = tmp_yuan.substring(tmp_yuan.length - 2, tmp_yuan.length - 3);
           document.getElementById("qian").value = tmp_yuan.substring(tmp_yuan.length - 3, tmp_yuan.length - 4);
           document.getElementById("wan").value = tmp_yuan.substring(tmp_yuan.length - 4, tmp_yuan.length - 5);
           document.getElementById("shiwan").value = tmp_yuan.substring(tmp_yuan.length - 5, tmp_yuan.length - 6);
       }
   } 
   function directChange(){ 
   	var obj=$("#form1").validate().form();
	 if(obj){
		 if($("#department").val() != "" && $("#department").val() != undefined){
	           if($("#departmentId").val() == "" || $("#departmentId").val() == undefined){
	               $.ligerDialog.warn("部门id为空，请重新选择部门！");
	               return;
	           }
	         }
		 if($("#borrower").val() != "" && $("#borrower").val() != undefined){
	           if($("#borrowerId").val() == "" || $("#borrowerId").val() == undefined){
	               $.ligerDialog.warn("人员id为空，请重新选择人员！");
	               return;
	           }
	         }
		 //$("#form1").submit();
		 var formData = $("#form1").getValues();
         $("body").mask("正在保存......");
        $.ajax({
            url: "money/borrow/save.do",
            type: "POST",
            datatype: "json",
            contentType: "application/json; charset=utf-8",
            data: $.ligerui.toJSON(formData),
            success: function (data, stats) {
                $("body").unmask();//alert(99);
                if (data["success"]) {
                	top.$.notice(data.msg,3);	
                    //top.$.dialog.notice({content:'保存成功！'});
                    api.data.window.Qm.refreshGrid();//刷新
                    api.close();
                }else{
                    $.ligerDialog.error('提示：' + data.msg);
                    api.data.window.Qm.refreshGrid();//刷新
                }
            },
            error: function (data,stats) {
                $("body").unmask();
                $.ligerDialog.error('提示：' + data.msg);
            }
        });
	 }else{
		 return;
	}}
   function directChange1(){ 
	   	var obj=$("#form1").validate().form();
		 if(obj){
			 if($("#department").val() != "" && $("#department").val() != undefined){
		           if($("#departmentId").val() == "" || $("#departmentId").val() == undefined){
		               $.ligerDialog.warn("部门id为空，请重新选择部门！");
		               return;
		           }
		         }
			 if($("#borrower").val() != "" && $("#borrower").val() != undefined){
		           if($("#borrowerId").val() == "" || $("#borrowerId").val() == undefined){
		               $.ligerDialog.warn("人员id为空，请重新选择人员！");
		               return;
		           }
		         }
			 //$("#form1").submit();
			 var formData = $("#form1").getValues();
	         $("body").mask("正在保存......");
	        $.ajax({
	            url: "money/borrow/save.do",
	            type: "POST",
	            datatype: "json",
	            contentType: "application/json; charset=utf-8",
	            data: $.ligerui.toJSON(formData),
	            success: function (data, stats) {
	                $("body").unmask();
	                if (data["success"]) {
	                	top.$.notice(data.msg,3);	
	                    //top.$.dialog.notice({content:'保存成功！'});
	                    api.data.window.Qm.refreshGrid();//刷新
	                    //api.close();
	                }else{
	                    $.ligerDialog.error('提示：' + data.msg);
	                    api.data.window.Qm.refreshGrid();//刷新
	                }
	            },
	            error: function (data,stats) {
	                $("body").unmask();
	                $.ligerDialog.error('提示：' + data.msg);
	            }
	        });
		 }else{
			 return;
		}}
   function chooseOrg(){
	   var dw =	$("#unit-txt").val();
		 var parent_id="100000";
		  if(dw=="鼎盛公司"){
			  parent_id="100047";
		 }
		 if(dw=="四川省特种设备检验检测协会"){
			  parent_id="100042";
			 }
		 if(dw=="四川省特种设备检验研究院工会委员会"){
			  parent_id="100048";
			 }
		 if(dw=="基建办"){
			  parent_id="100043";
			 } 
		 if(dw=="中共四川省特种设备检验研究院委员会"){
			  parent_id="100079";
			 } 
       top.$.dialog({
           width: 800,
           height: 450,
           lock: true,
           parent: api,
           title: "选择部门",
           content: 'url:app/common/org_choose_new.jsp?par_id='+parent_id,
           cancel: true,
           ok: function(){
               var p = this.iframe.contentWindow.getSelectedPerson();
               if(!p){
                   top.$.notice("请选择部门！", 3, "k/kui/images/icons/dialog/32X32/hits.png");
                   return false;
               }
               $("#departmentId").val(p.id);
               $("#department").val(p.name);
               if("<%=user.getDepartment().getId() %>"!=p.id){
               	top.$.notice("<span style='color:red;'>所选部门与人员信息不符，请留意！</span>", 7, "k/kui/images/icons/dialog/32X32/hits.png");
               }
           }
       });
   }
   function choosePerson(){
       top.$.dialog({
           width: 800,
           height: 450,
           lock: true,
           parent: api,
           title: "选择人员",
           content: 'url:app/common/person_choose.jsp',
           cancel: true,
           ok: function(){
               var p = this.iframe.contentWindow.getSelectedPerson();
               if(!p){
                   top.$.notice("请选择人员！", 3, "k/kui/images/icons/dialog/32X32/hits.png");
                   return false;
               }
               $("#borrowerId").val(p.id);
               $("#borrower").val(p.name);
           }
       });
   }
 //通过2步做到输入的为非负数
   //1.去掉多余的小数点
   //2.保证只能输入小数点或数字
   	function onlyNonNegative(obj) {
    var inputChar = event.keyCode;
    //alert(event.keyCode);
    //1.判断是否有多于一个小数点
    if(inputChar==190 ) {//输入的是否为.
	    var index1 = obj.value.indexOf(".") + 1;//取第一次出现.的后一个位置
	    var index2 = obj.value.indexOf(".",index1);
	    while(index2!=-1) {
		      //alert("有多个.");
		     obj.value = obj.value.substring(0,index2);
		     index2 = obj.value.indexOf(".",index1);
	    }
    }
    //2.如果输入的不是.或者不是数字，替换 g:全局替换
   	obj.value = obj.value.replace(/[^(\d|.)]/g,"");
    var money=document.getElementById("money").value;
    var cash = document.getElementById("cash").value;
   	}
    function toDetail(){
    	$("#chu").hide();
        $("#up").hide();
        //form1   jksq
    	$("#form1").transform("detail",function(){});
	    $("#form1").setValues("money/borrow/detail.do?id=${param.id}");
    }
   	function reLoad(){
        location.reload();
    }
   	function jkqr(){ 
//     	var repayment_man_id=$("#repayment_man_id").val();
//         var repayment_man=$("#repayment_man").val();
//         var repayment_time=$("#repayment_time").val();
        var id="${param.id}";

 			 $.ligerDialog.confirm('是否确认借款？', function (yes){
 	        	if(!yes){return false;}
 	            top.$.ajax({
 	                 url: "money/borrow/jkcheck.do?id="+id+"&check=1",//+"&repayment_man_id="+repayment_man_id+
 	                		 //"&repayment_time="+repayment_time,
 	                 type: "POST",
 	                 dataType:'json',
 	                // data:"&repayment_man="+repayment_man,
 	                 async: false,
 	                 success:function (data) {
//  	                	 if("还款已提交"==up_status){
//  	                		 $.ligerDialog.warn("此条还款已提交！");
//  	                	 }else{
 		                	 if(data.success){
 		                		 top.$.notice('确认借款成功！',3);
 		                		api.close();
 		                		api.data.window.refreshGrid();//刷新
 		                     }else{
 		                         $.ligerDialog.warn(data.msg);
 		                     }
// 	                	 }
 	                 },
 	                 error:function () {
 	                     $.ligerDialog.warn("确认借款失败！");
 	                 }
 	             });
 	        });

// 			 $("#form1").submit();
// 		 }else{
// 			 return;
// 		}
	}
	function unjkqr(){ 
//     	var repayment_man_id=$("#repayment_man_id").val();
//         var repayment_man=$("#repayment_man").val();
//         var repayment_time=$("#repayment_time").val();
        var id="${param.id}";

 			 $.ligerDialog.confirm('是否取消借款？', function (yes){
 	        	if(!yes){return false;}
 	            top.$.ajax({
 	                 url: "money/borrow/jkcheck.do?id="+id+"&check=0",//+"&repayment_man_id="+repayment_man_id+
 	                		 //"&repayment_time="+repayment_time,
 	                 type: "POST",
 	                 dataType:'json',
 	                // data:"&repayment_man="+repayment_man,
 	                 async: false,
 	                 success:function (data) {
//  	                	 if("还款已提交"==up_status){
//  	                		 $.ligerDialog.warn("此条还款已提交！");
//  	                	 }else{
 		                	 if(data.success){
 		                		 top.$.notice('取消借款成功！',3);
 		                		api.close();
 		                		api.data.window.refreshGrid();//刷新
 		                     }else{
 		                         $.ligerDialog.warn(data.msg);
 		                     }
// 	                	 }
 	                 },
 	                 error:function () {
 	                     $.ligerDialog.warn("取消借款失败！");
 	                 }
 	             });
 	        });

// 			 $("#form1").submit();
// 		 }else{
// 			 return;
// 		}
	}
    </script>
    <style>
h2{font-family:宋体;font-size:6mm; text-align:center;margin:11px 0 0 0;}
    </style>
   <style type="text/css" media="print" id="pstyle">
* {
    font-family:"宋体";
    font-size:15px;
    letter-spacing:normal;
    
}
table{ margin:0 auto;}
table td{ height:40px;}
.l-detail-table td, .l-detail-table {
    border-collapse: collapse;
    border: 1px solid black;
}
.l-detail-table {
    padding:5px;
    border:0px solid #CFE3F8;
    border-top:0px;
    border-left:0px;
    word-break:break-all;
    table-layout:fixed;
}
.check {
    width:720px;
}
.l-t-td-left{ text-align:center;}
.l-t-td-right{ padding-left:5px;}
.fybx2{   height:40px; line-height:20px; overflow: hidden;}

h2{font-family:宋体;font-size:6mm; text-align:center;margin:10px 0 0 0;}

</style>
</head>
<body>
<form id="form1" action="money/borrow/save.do" getAction="money/borrow/detail.do?id=${param.id}">
    <input type="hidden" id="id" name="id"/>
    <input type="hidden" name="status" id="status" value=""/>
    <input type="hidden" name="repayment_status" id="repayment_status" value=""/>
    <input type="hidden" id="borrowerId" name="borrowerId" value="<%=userId%>"/>
    <input type="hidden" id="registrant" name="registrant" value=""/>
    <input type="hidden" id="registrantId" name="registrantId" value=""/>
    <input type="hidden" id="departmentId" name="departmentId" value=""/>
    
    <h2>借&nbsp;款&nbsp;单 </h2>
     <table class="check">
             <tr>
                    <td width="160px"></td>
                    <td width="160px" class="l-t-td-right">
                    </td>
                    <td width="380px">&nbsp;</td>
                    <td width="80px" align="center">编号:</td>
                    <td  style="width: 130px;" class="l-t-td-right"><input ltype='text' name="identifier" type="text" id="identifier" readonly="readonly"/></td>
                    <td width="280px">&nbsp;</td>
                    <td width="80px" align="center"></td>
                    <td width="60" class="l-t-td-right"></td>
                    <td width="20"></td>
                     </td>
                    
                   
                </tr>
            </table>
    <table id="jksq" class="l-detail-table has-head-image" width="720px">
        <tr>
	        <th style="border:0px;width:45px"></th>
	        <th style="border:0px;width:45px"></th>
	        <th style="border:0px;width:45px"></th>
	        <th style="border:0px;width:45px"></th>
            <th style="border:0px;width:45px"></th>
            <th style="border:0px;width:45px"></th>
            <th style="border:0px;width:45px"></th>
            <th style="border:0px;width:45px"></th>
            <th style="border:0px;width:45px"></th>
            <th style="border:0px;width:45px"></th>
            <th style="border:0px;width:45px"></th>
            <th style="border:0px;width:65px"></th>
            <th style="border:0px;width:20px"></th>
            <th style="border:0px;width:20px"></th>
            <th style="border:0px;width:20px"></th>
            <th style="border:0px;width:20px"></th>
            <th style="border:0px;width:20px"></th>
            <th style="border:0px;width:20px"></th>
            <th style="border:0px;width:20px"></th>
            <th style="border:0px;width:20px"></th>
        </tr>
        <tr>
        	<td class="l-t-td-left" colspan="2">单位</td>
        	<td colspan="6" class="l-t-td-right">
        		<%-- <u:combo  name="unit" code="TJY2_UNIT" attribute="initValue:'tjy'" validate="{required:true}"/> --%>
        		<%if(request.getParameter("isCheck").toString().equals("1")){
        			%>
        			<sec:authorize access="hasRole('TJY2_CW_CHECK')">
                  	<sec:authorize access="!hasRole('TJY2_CW_CHECK_XSG')">
			    		<u:combo  name="unit" code="TJY2_UNIT" attribute="initValue:'tjy'" validate="{required:true}"/>
					</sec:authorize>
                  	
			    	<sec:authorize access="hasRole('TJY2_CW_CHECK_XSG')">
			    		<u:combo name="unit" code="TJY2_UNIT_XSG" attribute="initValue:'tjy'"  validate="required:true"/>
					</sec:authorize>
					</sec:authorize>
					<sec:authorize access="!hasRole('TJY2_CW_CHECK')">
	                  	<u:combo  name="unit" code="TJY2_UNIT" attribute="initValue:'tjy'" validate="{required:true}"/>
					</sec:authorize>
        			<%
        		}else{
        			%>
        			<u:combo  name="unit" code="TJY2_UNIT" attribute="initValue:'tjy'" validate="{required:true}"/>
        			<%
        		} %>
        	</td>
        	<td class="l-t-td-left" colspan="2">部门</td>
        	<td class="l-t-td-right" colspan="10">
        		<input readonly="readonly" validate="{maxlength:50,required:true}"  ltype="text"  name="department" id="department" 
        		type="text" onclick="chooseOrg()" ligerui="{iconItems:[{icon:'org',click:chooseOrg}]}" 
        		 value="<%=user.getDepartment().getOrgName() %>"/>
        	</td>
        </tr>
        <tr>
        	<td class="l-t-td-left" colspan="2">日期</td>
        	<%SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
				String nowTime=""; 
				nowTime = dateFormat.format(new Date());%>
        	<td colspan="6" class="l-t-td-right"><input name="borrowMoneyDate" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" value="<%=nowTime%>" /></td>
        	<td class="l-t-td-left" colspan="2">姓名</td>
        	<td colspan="10" class="l-t-td-right">
				<input name="borrower" id="borrower" ligerUi="{disabled:true}" type="text" ltype="text" validate="{required:true,maxlength:25}" value="<sec:authentication property="principal.name"/>"/>
			</td>
        </tr>
        <tr>
        	<td colspan="12" align="center" class="l-t-td-left">付款方式</td>
        	<td colspan="8" align="center" class="l-t-td-left">金额</td>
        </tr>
        <tr>
        	<td class="l-t-td-left" colspan="2">现金</td>
        	<td colspan="10" class="l-t-td-left">转账、电汇</td>
			<td align="center">拾万</td>
			<td align="center">万</td>
			<td align="center">仟</td>
			<td align="center">佰</td>
			<td align="center">拾</td>
			<td align="center">元</td>
			<td align="center">角</td>
			<td align="center">分</td>
		</tr>
        <tr>
        	<td rowspan="2" class="l-t-td-right" colspan="2"><input id="cash" name="cash" type="text" ltype='text' onblur="sumAmount();" validate="{maxlength:10}" onkeyup="onlyNonNegative(this)"/></td>
        	<td class="l-t-td-left" colspan="3">收款单位</td>
        	<td class="l-t-td-left" colspan="3">开户行</td>
        	<td class="l-t-td-left" colspan="3">帐号</td>
        	<td class="l-t-td-left" colspan="1">金额</td>
            <td class="l-t-td-right"><input name="shiwan" id="shiwan" type="text" ltype="text" onkeyup="getMonyNum('wan');" validate="{maxlength:1}" /></td>
            <td class="l-t-td-right"><input name="wan" id="wan" type="text" ltype="text" onkeyup="getMonyNum('qian');" validate="{maxlength:1}" /></td>
            <td class="l-t-td-right"><input name="qian" id="qian" type="text" ltype="text" onkeyup="getMonyNum('bai');" validate="{maxlength:1}" /></td>
            <td class="l-t-td-right"><input name="bai" id="bai" type="text" ltype="text" onkeyup="getMonyNum('shi');" validate="{maxlength:1}" /></td>
            <td class="l-t-td-right"><input name="shi" id="shi" type="text" ltype="text" onkeyup="getMonyNum('yuan');" validate="{maxlength:1}" /></td>
            <td class="l-t-td-right"><input name="yuan" id="yuan" type="text" ltype="text" onkeyup="getMonyNum('jiao');" validate="{maxlength:1}" /></td>
            <td class="l-t-td-right"><input name="jiao" id="jiao" type="text" ltype="text" onkeyup="getMonyNum('fen');" validate="{maxlength:1}" /></td>
            <td class="l-t-td-right"><input name="fen" id="fen" ltype="text" onkeyup="getMonyNum('fen');" validate="{maxlength:1}" type="text" /></td>
        </tr>
         <tr>
        	<td class="l-t-td-right" colspan="3"><input name="payee" type="text" ltype="text" validate="{maxlength:200}" /></td>
        	<td class="l-t-td-right" colspan="3"><input name="openingBank" type="text" ltype="text" validate="{maxlength:200}" /></td>
        	<td class="l-t-td-right" colspan="3"><input name="accounts" type="text" ltype="text" validate="{maxlength:50}" /></td>
        	<td class="l-t-td-right" colspan="1"><input name="money" id="money" ltype="text" type="text" onblur="sumAmount();" validate="{maxlength:10}" onkeyup="onlyNonNegative(this)"/></td>
        	<td class="l-t-td-right" colspan="8">
				<input readonly="readonly" name="import_money" id="import_money" ltype="text" type="text" title="总金额" onkeyup="sumAmount();" validate="{required:true}"/>
			</td>
        </tr>
        <tr>
        	<td class="l-t-td-left" colspan="2">人民币大写</td>
        	<td colspan="6" class="l-t-td-right"><input readonly="readonly" name="rmb" id="rmb" type="text" ltype="text" validate="{required:true,maxlength:200}" /></td>
			<td class="l-t-td-left" colspan="2">备注</td>
			<td class="l-t-td-right" colspan="10"><input name="remark" type="text" ltype="text" validate="{maxlength:2000}" /></td>
        </tr>
    </table>
	<table class="check">
		<tr>
			<td>单位负责人：</td>
			<td class="l-td-opinion">
                <p class="signer-date">
                    <span class="l-content signer"></span>
                </p>
			 </td>
			<td><div id="fgld">分管领导：</div></td>
            <td class="l-td-opinion">
                <p class="signer-date">
                    <span class="l-content signer"></span>
                </p>
             </td>
			<td>部门领导：</td>
            <td class="l-td-opinion">
                <p class="signer-date">
                    <span class="l-content signer"></span>
                </p>
             </td>
			<td>财务审核：</td>
            <td class="l-td-opinion">
                <p class="signer-date">
                    <span class="l-content signer"></span>
                </p>
             </td>
			<td>借款人：</td>
			<td class="l-t-td-right" width="90px">
<%--                 <input ligerUi="{disabled:true}" ltype='text' value="<%=user.getName() %>" id="registrant" name="registrant" type="text"/> --%>
             </td>
		</tr>
	</table>
	
</form>
</body>
</html>
