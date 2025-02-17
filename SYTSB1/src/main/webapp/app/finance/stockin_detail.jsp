<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>

<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.pageStatus}">
    <title></title>
    <bpm:ifPer function="功能编号" activityId="环节id">
    //对应的功能权限操作
</bpm:ifPer>
    <%@include file="/k/kui-base-form.jsp" %>
    <%
SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
String nowTime=""; 
nowTime = dateFormat.format(new Date());
%>
<!--获取当前登录人  -->
<%CurrentSessionUser useres = SecurityUtil.getSecurityUser();
String user=useres.getName();
String userid= useres.getId();%>
<script type="text/javascript" src="pub/bpm/js/util.js"></script>
<script type="text/javascript" src="app/common/lodop/LodopFuncs.js"></script>
 <script type="text/javascript" src="app/finance/js/jquery.autocomplete.js"></script>
<link rel="Stylesheet" href="app/finance/css/jquery.autocomplete.css" />
 <link type="text/css" rel="stylesheet" href="app/finance/css/form_detail.css" />
    <script type="text/javascript">

    function openFile1(){
    	window.location.href = $("base").attr("href")+"fileupload2/downloadCompress.do?id=402883a265e6f8150165ea7d4a494ae6&proportion=0";
    }
    function openFile(){
    	   var url='url:app/finance/bxzd.jsp';
    	   	 top.$.dialog({
    		         width: 400,
    		         height:240,
    		         lock: true,
    		         parent: api,
    		         data: {
    		       	 window: window
    		         },
    		         title: "费用报销",
    		         content: url
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
  }
    
    var tbar="";
    var serviceId = "${requestScope.serviceId}";//提交数据的id
	var activityId = "${requestScope.activityId}";//流程id
	var areaFlag;//改变状态
	var tbar="";
	var isChecked="${param.isChecked}";
	var depid='<sec:authentication property="principal.unit.id" />';
     <bpm:ifPer function="TJY2_CW_FYBX_cwsh" activityId = "${requestScope.activityId}">areaFlag="1";</bpm:ifPer>
  	 <bpm:ifPer function="TJY2_CW_FYBX_bmsh" activityId = "${requestScope.activityId}">areaFlag="2";</bpm:ifPer>
  	 <bpm:ifPer function="TJY2_CW_FYBX_fgsh" activityId = "${requestScope.activityId}">areaFlag="3";</bpm:ifPer>
  	 <bpm:ifPer function="TJY2_CW_FYBX_dwsh" activityId = "${requestScope.activityId}">areaFlag="4";</bpm:ifPer>
 
    var emails = [
            { name: "Peter Pan", to: "peter@pan.de" },
            { name: "Molly", to: "molly@yahoo.com" },
            { name: "Forneria Marconi", to: "live@japan.jp" },
            { name: "Master <em>Sync</em>", to: "205bw@samsung.com" },
            { name: "Dr. <strong>Tech</strong> de Log", to: "g15@logitech.com" },
            { name: "Don Corleone", to: "don@vegas.com" },
            { name: "Mc Chick", to: "info@donalds.org" },
            { name: "Donnie Darko", to: "dd@timeshift.info" },
            { name: "Quake The Net", to: "webmaster@quakenet.org" },
            { name: "Dr. Write", to: "write@writable.com" },
            { name: "GG Bond", to: "Bond@qq.com" },
            { name: "Zhuzhu Xia", to: "zhuzhu@qq.com" }
        ];
    $(function () {
    if("${param.pageStatus}"=="add"){
            var orgId = <%=useres.getDepartment().getId() %>;
            //如果是新增，申请部门id设为当前登录者所在部门的id
            $("#departmentId").val(orgId);
        }
       var tbar=[{ text: '保存', id: 'up', icon: 'save', click: save},
           { text: '关闭', id: 'close', icon:'cancel', click:function(){api.data.window.Qm.refreshGrid();api.close();}}];
       if ("${param.pageStatus}"=="modify")
    	   var tbar=[{ text: '保存', id: 'up', icon: 'save', click: save},
               { text: '关闭', id: 'close', icon:'cancel', click:function(){
            	   api.data.window.Qm.refreshGrid();//刷新
                   api.close(); 
            	   /* api.data.window.api.close();
			     	api.close();  
			     	api.data.window.api.data.window.Qm.refreshGrid(); */}}]; 	
       if ("${param.pageStatus}"=="detail")
       var tbar=[
			<sec:authorize access="hasRole('TJY2_CW_CHECK')">
			{ text: '打印', id: 'print', icon: 'print', click: print},
			</sec:authorize>
	        { text: '关闭', id: 'close', icon:'cancel', click:function(){api.data.window.Qm.refreshGrid();api.close();}}
         ];
       if ("${param.pageStatus}"=="detail" && "${param.a}"=="a"){
       	if("${param.bxstatus}"=="1"){
       		tbar=[{ text: '取消确认', id: 'submit', icon: 'submit', click: undirectChange},
             	  { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}
             ];
       	}else if("${param.bxstatus}"=="0"){
       		tbar=[{ text: '确认', id: 'submit', icon: 'submit', click: directChange},
             	  { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}
             ];
       	}
           }
       $("#form1").initForm({
           showToolbar: true,
           toolbarPosition: "bottom",
           toolbar: tbar,
           getSuccess: function(resp){
               var readers = [];
               var names = [];
               var ids = [];
               if(resp.data.clCcr){
                    names = resp.data.clCcr.split(",");
                    ids = resp.data.clCcid.split(",");
                    for(var i=0;i<names.length;i++){
                       readers.push({
                           types : "reader",
                           name: names[i],
                           id: ids[i]
                       });
                   } 
                   addReader(readers,false);
               }
            	top.$.ajax({
                    url: "lsts/finance/getWarehousingByFybxId.do?fybxId=${param.id}",
                    type: "POST",
                    dataType:'json',
                    async: false,
                    success:function (data) {
                    	console.log(data);
                    	if(data.success){
                    		
                    		var bh="";
                    		$.each(data.data,function(i,v){
                    			bh=bh+v.warehousing_num+",";
                    		})
                    		bh=bh.length>0?bh.substring(0,bh.lastIndexOf(',')):'';
                    		$("#rkdh").html(bh);
                    	}
                    }
            	});

                var a=resp.data.handleTime;
                if(a!=null){
                	var handleTime=a.substring(a.indexOf("/")+1,a.lastIndexOf(" "));
                 	$("#handleTime").html(handleTime);
                }
             	
           }
       });
	    	$("body").append('<object style="height:1px;" id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0><embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed></object>');
	    	
	    	 $('#peopleConcerned').autocomplete("employee/basic/searchEmployee.do", {
                    max: 12,    //列表里的条目数
                    minChars: 1,    //自动完成激活之前填入的最小字符
                    width: 200,     //提示的宽度，溢出隐藏
                    scrollHeight: 300,   //提示的高度，溢出显示滚动条
                    scroll: false,   //当结果集大于默认高度时是否使用卷轴显示
                    matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                    autoFill: false,    //自动填充
                    formatItem: function(row, i, max) {
                        return row.name + '   ' + row.mobileTel;
                    },
                    formatMatch: function(row, i, max) {
                        return row.name + row.mobileTel;
                    },
                    formatResult: function(row) {
                        return row.name;
                    }
                }).result(function(event, row, formatted) {
  //                  alert(row.mobileTel);
                });
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
   				  //部门ID           	
 //                alert(row.orgId);
                 document.getElementById("departmentId").value = row.orgId
             });
             
                //选择单位时 清空部门
	    	 $('#unit-txt').change(function() {
	    			var unit = $("#unit-txt").val();
	    			if("四川省特种设备检验研究院" == unit || "中共四川省特种设备检验研究院委员会" == unit || "四川省特种设备检验检测协会" == unit){
	    				if("四川省特种设备检验研究院" == unit){
	    					$('#department').val("<%=useres.getDepartment().getOrgName()%>"); 
	    				}else{
	    					$('#department').val(""); 
	    				}
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

	    });
    window.onload = function() { 

    }; 


	    function print(){ 
	       var html= $("#form1").html();
	       var LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
	                    var strBodyStyle = "<style>" + $("#pstyle").html() + "</style>";
	                    LODOP.PRINT_INIT("打印费用报销单");
	                    LODOP.ADD_PRINT_HTM('90px', '90px', "770px", "520px", strBodyStyle + html.replace($("#bx").html(),""));
	                    //LODOP.SET_PRINT_PAGESIZE (2, 1760, 2500,"");
	                    LODOP.SET_PRINT_PAGESIZE (2, 0, 0,"B5(ISO)");
	                     LODOP.PRINT();
	                     // LODOP.PREVIEW(); 
	    }
function addSecSeal(){
	
}
//审核不通过
function nosubmitSh(){
	$.ligerDialog.confirm('是否要不通过审核？', function (yes){
       if(!yes){return false;}
	 $("body").mask("正在处理，请稍后！");
	 
	 getServiceFlowConfig("TJY2_CW_FYBX","",function(result,data){
         if(result){
              top.$.ajax({
                  url: "lsts/finance/fyth.do?id="+serviceId+
                 		 "&typeCode=TJY2_CW_FYBX&status="+"&activityId="+activityId+"&areaFlag="+areaFlag,
                  type: "GET",
                  dataType:'json',
                  async: false,
                  success:function (data) {
                      if (data) {
                         $.ligerDialog.alert("操作成功！！！");
                         api.data.window.Qm.refreshGrid();
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


//审核通过
function submitfybxd(){
	var serviceId = "${requestScope.serviceId}";
	var activityId = "${requestScope.activityId}";
    $.ligerDialog.confirm('是否提交审核？', function (yes){
    if(!yes){ 
                return false;
             }
     $("body").mask("提交中...");
     getServiceFlowConfig("TJY2_CW_FYBX","",function(result,data){
            if(result){
                 top.$.ajax({
                     url: "lsts/finance/submitfybxd.do?id="+serviceId+
                    		 "&typeCode=TJY2_CW_JK&status="+"&activityId="+activityId+"&areaFlag="+areaFlag,
                     type: "GET",
                     dataType:'json',
                     async: false,
                     success:function (data) {
                         if (data) {
                            $.ligerDialog.alert("审核成功！！！");
                            api.data.window.Qm.refreshGrid();api.close();
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

function SunAmount() {
	  

   
    var money1 = document.getElementById("money1").value;
    var money2 = document.getElementById("money2").value;
    var money3 = document.getElementById("money3").value;
    var money4 = document.getElementById("money4").value;
   
    
    
    var sumamount = document.getElementById("total");
    var sumAmount = parseFloat(money1 == "" ? 0 : money1) + parseFloat(money2 == "" ? 0 : money2) + parseFloat

(money3 == "" ? 0 : money3) + parseFloat(money4 == "" ? 0 : money4);
    sumamount.value = sumAmount == 0 ? "" : sumAmount.toFixed(2); 
    //document.getElementById("daxie").value = digit_uppercase(sumAmount);
    digit_uppercase(sumAmount);
    
}

function digit_uppercase(sumAmount) {
	$.ajax({
    	url:  "moneyConvertAction/moneyConvert.do?sumAmount="+sumAmount.toString(),
        type: "POST",
        datatype: "json",
        contentType: "application/json; charset=utf-8",
        success: function (resp) {
        	if(resp.success){
        		//alert(resp.sumAmountSup);
        		document.getElementById("daxie").value=resp.sumAmountSup;
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

   
   
function save(){
	/* alert($("#departmentId").val());
	alert($("#userId").val()); */
     var obj=$("#form1").validate().form();
     if(obj){
         if($("#department").val() != "" && $("#department").val() != undefined){
           if($("#departmentId").val() == "" || $("#departmentId").val() == undefined){
               $.ligerDialog.warn("部门id为空，请重新选择部门！");
               return;
           }/*else if($("#userId").val() == "" || $("#userId").val() == undefined ){
        	   $.ligerDialog.warn("报销人id为空，请重新选择报销人！");
        	   return;
           }*/
         }
     	var obj=$("#form1").validate().form();
		 if(obj){
			 var formData = $("#form1").getValues();
			 console.log(formData);
// 			 formData["rkdh"]=$("#rkdh").html();
			 $("body").mask("正在保存，请稍后！");
         $.ajax({
             url: "lsts/finance/save.do?rkdh="+$("#rkdh").html(),
             type: "POST",
             datatype: "json",
             contentType: "application/json; charset=utf-8",
             data: $.ligerui.toJSON(formData),
             success: function (data, stats) {
                 $("body").unmask();
                 if(data.success){
                     top.$.dialog.notice({content:'操作成功！'});
                     if("${param.pageStatus}"=="add"){
                    	api.data.window.Qm.refreshGrid();
                     	api.close();
                    	/* api.data.window.api.close();
     			     	api.close();  
     			     	api.data.window.api.data.window.Qm.refreshGrid(); */
                     }else{
                    	api.data.window.Qm.refreshGrid();
                    	api.close();
                     }
                 }else{
                	 $.ligerDialog.error('提示：' + data.ts);
                 }
             },
             error: function (data,stats) {
                 $("body").unmask();
                 $.ligerDialog.error('提示：' + data.msg);
             }
         });
         
			 //$("#form1").submit();
			 //api.data.window.toDetail();
			 //setTimeout("close()","300");
		 }else{
			 return;
		}
   }else{
       return;
  }
} 
function close(){
	if(api.data.window.submitAction)
		api.data.window.submitAction();
		api.close();
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
                    $("#userId").val(p.id);
                    $("#peopleConcerned").val(p.name);
                }
            });
        }
function chooseOrg(){
	var dw =$("#unit-txt").val();
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
                    if("<%=useres.getDepartment().getId() %>"!=p.id){
                    	$("#rkdh").html("");
                    	top.$.notice("<span style='color:red;'>所选部门与人员信息不符，请留意！</span>", 7, "k/kui/images/icons/dialog/32X32/hits.png");
                    }
                }
            });
        }

function submitsh() {
	var id = $("#id").val();
	top.$.dialog({
		width: 600,
		height: 250,
		lock: true,
		parent: api,
		data: {
			window: window
		},
		title: "审核",
		content: 'url:app/finance/fybxd_yijian_detail.jsp?id=' + id + '&pageStatus=add'
	});
}
function directChange(){ 
//	var repayment_man_id=$("#repayment_man_id").val();
//    var repayment_man=$("#repayment_man").val();
//    var repayment_time=$("#repayment_time").val();
   var id="${param.id}";

		 $.ligerDialog.confirm('是否确认已报销？', function (yes){
        	if(!yes){return false;}
            top.$.ajax({
                 url: "lsts/finance/bxsubmit.do?id="+id+"&check=1",//+"&repayment_man_id="+repayment_man_id+
                		 //"&repayment_time="+repayment_time,
                 type: "POST",
                 dataType:'json',
                // data:"&repayment_man="+repayment_man,
                 async: false,
                 success:function (data) {
//	                	 if("还款已提交"==up_status){
//	                		 $.ligerDialog.warn("此条还款已提交！");
//	                	 }else{
	                	 if(data.success){
	                		 top.$.notice('确认已报销成功！',3);
	                		api.close();
	                		api.data.window.Qm.refreshGrid();//刷新
	                     }else{
	                         $.ligerDialog.warn(data.msg);
	                     }
//                	 }
                 },
                 error:function () {
                     $.ligerDialog.warn("确认已报销失败！");
                 }
             });
        });

//		 $("#form1").submit();
//	 }else{
//		 return;
//	}
}
function openRk(){
	var dep=$("#department").val();
	if(dep==""){
		 $.ligerDialog.warn("请先选择部门！");
	}
	if("<%=useres.getDepartment().getOrgName() %>"==dep){
		 var rkdbh=$("#rkdh").html();
    	 winOpen({
 			width :$(top).width()-700,
 			height : 420,
 			lock : true,
 			title : "选择入库单号",
 			content : 'url:app/supplies/out/return_rk_dh.jsp?cw=1',
 			data : {"window" : window,'rkbh':rkdbh}
 		});
	}	
}
function returnRkBh (bh,zje){
	$("#rkdh").html(bh);
	$("#money1").val(zje);
    $("#money1").trigger("keyup");
}
function undirectChange(){ 
//	var repayment_man_id=$("#repayment_man_id").val();
//    var repayment_man=$("#repayment_man").val();
//    var repayment_time=$("#repayment_time").val();
   var id="${param.id}";

		 $.ligerDialog.confirm('是否取消已报销？', function (yes){
        	if(!yes){return false;}
            top.$.ajax({
                 url: "lsts/finance/bxsubmit.do?id="+id+"&check=0",//+"&repayment_man_id="+repayment_man_id+
                		 //"&repayment_time="+repayment_time,
                 type: "POST",
                 dataType:'json',
                // data:"&repayment_man="+repayment_man,
                 async: false,
                 success:function (data) {
//	                	 if("还款已提交"==up_status){
//	                		 $.ligerDialog.warn("此条还款已提交！");
//	                	 }else{
	                	 if(data.success){
	                		 top.$.notice('取消已报销成功！',3);
	                		api.close();
	                		api.data.window.Qm.refreshGrid();//刷新
	                     }else{
	                         $.ligerDialog.warn(data.msg);
	                     }
//                	 }
                 },
                 error:function () {
                     $.ligerDialog.warn("取消已报销失败！");
                 }
             });
        });

//		 $("#form1").submit();
//	 }else{
//		 return;
//	}
}    
    </script>
    <style>
    div .input{border-bottom:0px;}
    .l-detail-table1 td, .l-detail-table1, .l-table td1, .l-table1 {
    border-collapse: collapse;
    border: 1px solid #d2e0f1;
}
h2{font-family:宋体;font-size:6mm; text-align:center;margin:10px 0 0 0;}
    </style>
   <style type="text/css" media="print" id="pstyle">
* {
	font-family:"宋体";
	font-size:15px;
	letter-spacing:normal;
	
}
table{ margin:0 auto;}
table td{ height:36px;}
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
	width:770px;
}
.l-t-td-left{ text-align:center;}
.l-t-td-right{ padding-left:5px;}
.fybx2{   height:40px; line-height:20px; overflow: hidden;}

h2{font-family:宋体;font-size:7mm; text-align:center;margin:10px 0 0 0;}

</style>
</head>
<body>
<base
	href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

<form id="form1" action="lsts/finance/save.do" getAction="lsts/finance/detail.do?id=${param.id}">
     <input type="hidden" value="${param.id}" id="id" name="id">
     <input type="hidden"  id="fybxType" name="fybxType" value="${param.fybxType}">
     <input type="hidden"  id="status" name="status">
     <input type="hidden" value="<%=user %>" id="peopleConcerned" name="peopleConcerned" />
      <input type="hidden" id="userId" value="<%=userid %>" name="userId"/><!-- 报销人id -->
      <input type="hidden" value="" id="departmentId" name="departmentId">
     
			<h2>存货入库报销单 </h2>
			<div style="text-float: right;text-align: right;" id="bx">
			<a href="javascript:void(0);"  onclick="openRk()"  style=" height: 21px; border: 1px #26bbdb solid;
			line-height: 21px; padding: 0 11px; background: #e4e4e4; border-radius: 3px; display: inline-block; text-decoration: none;outline: none; font-size: 20px;color: #26bbdb" >选择入库单</a>
			
			
			<a href="javascript:void(0);"  onclick="openFile1()"  style=" height: 21px; border: 1px #26bbdb solid;
			line-height: 21px; padding: 0 11px; background: #e4e4e4; border-radius: 3px; display: inline-block; text-decoration: none;outline: none; font-size: 20px;color: #FF0000" >开具发票说明 </a>
			
			<a href="javascript:void(0);" onclick="openFile()"  style=" height: 21px; border: 1px #26bbdb solid;
			line-height: 21px; padding: 0 11px; background: #e4e4e4; border-radius: 3px; display: inline-block; text-decoration: none;outline: none; font-size: 20px;" >报销制度</a>
			</div>
			<div style="height:2px">&nbsp;</div>
			<table class="check">
			 <tr>
                    <td width="160px">报销日期：</td>
                    <td width="160px" class="l-t-td-right">
                    <input name="bsDate" type="text" ltype="date" validate="{required:true}" 
        			ligerui="{initValue:'',format:'yyyy-MM-dd'}" id="bsDate" value="<%=nowTime%>" />
                    </td>
                    <td width="380px">&nbsp;</td>
                    <td width="80px" align="center">编号:</td>
                    <td  style="width: 130px;"   class="l-t-td-right"><input ltype='text'   name="identifier" type="text" id="identifier" readonly="readonly"/></td>
                    <td width="280px">&nbsp;</td>
                    <td width="80px" align="center">附件:</td>
                    <td width="60" class="l-t-td-right"><input onkeydown="onlyNonNegative(this)"  validate="{required:false,maxlength:10}"ltype='text'  name="accessory" type="text" id="Recipients" /></td>
                    <td width="20">张</td>
                    </td>
                   
                </tr>
			</table>
			<table style="margin:0px;padding:0px" width="770px" class="l-detail-table">
                <tr>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            <th style="border:0px;width:40px"></th>
            </tr>
                <tr>
                  <td class="l-t-td-left" colspan="2">单位</td>
                  <td class="l-t-td-right" colspan="4">
				 <input name="unit" id="unit-txt" type="text" ltype="select" validate="{required:true}" ligerui="{initValue:'tjy', data: <u:dict code='TJY2_UNIT'/>}"  ></input>
				  </td>
                  <td class="l-t-td-left" colspan="2">部门</td>
               	  <td class="l-t-td-right" colspan="4"><input readonly="readonly" value="<%=useres.getDepartment().getOrgName() %>"  validate="{maxlength:50,required:true}" ltype="text"  name="department" id="department"  type="text" onclick="chooseOrg()" ligerui="{iconItems:[{icon:'org',click:chooseOrg}]}"/></td>
                  <td class="l-t-td-left" colspan="2" rowspan="4">单位负责人</td>
                  <td rowspan="4" colspan="4" class="l-t-td-right"></td>
                </tr>
                <tr height="50px">
                	<td class="l-t-td-left" colspan="2">入库单号</td>
                	<td class="l-t-td-right" colspan="10" id="rkdh" onclick="openRk()"></td>
                </tr>
                <tr>
                  <td class="l-t-td-left" colspan="3">经济类型</td>
                  <td class="l-t-td-left" colspan="3">费用项目</td>
                  <td class="l-t-td-left" colspan="2">类别</td>
                  <td class="l-t-td-left" colspan="4">金额</td>
                </tr>
                <tr>
                  <td class="l-t-td-right" colspan="3">
                  	<input type="text" id="subItem1" name="subItem1" ltype="select" validate="{required:false}" 
                  		ligerui="{
                  				initValue:'存货',
								readonly:true,
								data: [ { text:'存货', id:'存货' }]
							}"/>
<!-- 							data: [ { text:'专用材料费', id:'专用材料费' },{ text:'其他商品服务支出', id:'其他商品服务支出' }] -->
							</td>
                  <td class="l-t-td-right" colspan="3">
                    <input ltype="text" id="costItem1" name="costItem1" type="text" /></td>
                  <td class="l-t-td-right" colspan="2">
                  	<u:combo attribute="initValue:''" name="class1" code="fybx_class" /></td>
                    <!-- <input ltype='text'  validate="{maxlength:32}"  name="class1" type="text" id="SortName1" /></td> -->
                  <td class="l-t-td-right" colspan="4">
                      <input ltype='text' onkeyup="SunAmount();"  validate="{maxlength:32}" onkeydown="onlyNonNegative(this)" name="money1" type="text" id="money1"  /></td>
                </tr>
                <tr>
                  <td class="l-t-td-right" colspan="3">
                    <input type="text" id="subItem2" name="subItem2" ltype="select" validate="{required:false}" 
                  		ligerui="{
								initValue:'',
								readonly:true,
								data: [ { text:'存货', id:'存货' }]
							}"/></td>
                  <td class="l-t-td-right" colspan="3">
                   <input ltype='text'  name="costItem2" type="text" id="costItem2" /></td>
                  <td class="l-t-td-right" colspan="2">
                    <u:combo attribute="initValue:''" name="class2" code="fybx_class" /></td>
                      <!-- <input ltype='text'   name="class2" type="text" id="SortName2" /></td> -->
                  <td class="l-t-td-right" colspan="4">
                      <input ltype='text' onkeyup="SunAmount();" onkeydown="onlyNonNegative(this)"  name="money2" type="text" id="money2"  /></td>
                  <td class="l-t-td-left" rowspan="3" colspan="2">部门负责人</td>
                  <td class="l-t-td-right" rowspan="3" colspan="4"></td>
                </tr>
                <tr>
                  <td class="l-t-td-right" colspan="3">
                    <input type="text" id="subItem3" name="subItem3" ltype="select" validate="{required:false}" 
                  		ligerui="{
								value:'',
								readonly:true,
								data: [ { text:'存货', id:'存货' }]
							}"/></td>
                  <td class="l-t-td-right" colspan="3">
                      <input ltype='text'  name="costItem3" type="text" id="costItem3" /></td>
                  <td class="l-t-td-right" colspan="2">
                  	<u:combo attribute="initValue:''" name="class3" code="fybx_class" /></td>
                      <!-- <input ltype='text'  name="class3" type="text" id="SortName3" /></td> -->
                  <td class="l-t-td-right" colspan="4">
                      <input ltype='text' onkeyup="SunAmount();" name="money3" onkeydown="onlyNonNegative(this)" type="text" id="money3" /></td>
                </tr>
                <tr>
                  <td class="l-t-td-right" colspan="3">
                    <input type="text" id="subItem4" name="subItem4" ltype="select" validate="{required:false}" 
                  		ligerui="{
								value:'',
								readonly:true,
								data: [ { text:'存货', id:'存货' }]
							}"/></td>
                  <td class="l-t-td-right" colspan="3">
                    <input ltype='text'  name="costItem4" type="text" id="costItem4" />
                  </td>
                  <td class="l-t-td-right" colspan="2">
                  	<u:combo attribute="initValue:''" name="class4" code="fybx_class" /></td>
                    <!-- <input ltype='text'  name="class4" type="text" id="SortName4" /></td> -->
                  <td class="l-t-td-right" colspan="4"><input ltype='text' onkeyup="SunAmount();" onkeydown="onlyNonNegative(this)"  name="money4" type="text" id="money4"  /></td>
                  
                </tr>
                <tr>
                  <td colspan="6" class="l-t-td-left">报销金额合计</td>
                  <td class="l-t-td-right" colspan="6"><input onfocus="this.blur()" validate="{required:true,maxlength:32}" ltype='text'  name="total" type="text" id="total" onkeydown="SunAmount();" /> </td>
                  <td align="right" class="l-t-td-left" colspan="2">报销人</td>
                  <td class="l-t-td-right" colspan="4">&nbsp;
                     <!--<input ligerUi="{disabled:true}"   value="<%=user %>" ltype='text' id="peopleConcerned" name="peopleConcerned" type="text" id="Reviewers" />--></td>
                </tr>
                <tr><td colspan="6" class="l-t-td-left">核实金额（大写）</td>
                   <td class="l-t-td-right"  colspan="12" ><input align="middle" onfocus="this.blur()" validate="{required:true,maxlength:100}" ltype="text" name="wordFigure" id="daxie" /></td> 
                </tr>
                </table>
	<table class="check">
        <tr>
            <td width="200px">财务审核：</td>
            <td class="l-td-opinion">
                <p class="signer-date">
                    <span class="l-content signer"></span>
                </p>
             </td>
              <td width="80px">审核日期：</td>
            <td width="160px" class="l-t-td-right">
                    <input ltype='text' name="handleTime"  id="handleTime" readonly="readonly"/></td>
            <td>出纳：</td>
            <td class="l-td-opinion">
                <p class="signer-date">
                    <span class="l-content signer"></span>
                </p>
             </td>
        </tr>
    </table>
</form>
</body>
</html>
