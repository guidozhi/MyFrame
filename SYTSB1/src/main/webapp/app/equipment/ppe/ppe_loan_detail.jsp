﻿<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus = "${param.pageStatus}">
<title>资产借用、归还登记</title>
<%@ include file="/k/kui-base-form.jsp"%>
<script type="text/javascript" src="app/common/lodop/LodopFuncs.js"></script>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<link type="text/css" rel="stylesheet" href="app/finance/css/form_detail.css" />
<script type="text/javascript">
	var pageStatus = "${param.pageStatus}";
	var ppeGrid;
	var columns=[];
	var tbar="";
	$(function() {
		if(pageStatus=="detail"){
	 		tbar=[{text: "打印", id: "print", icon: "print", click: print},
	 		      {text: "关闭", icon: "cancel", click: function(){api.close();}}];
	 	}else{
	 		defineColumns();
			initGrid();
	 		tbar=[{text: "保存并借出", icon: "save", click: saveAndLoan},
	 		      {text: "保存", icon: "save", click: save},
				  {text: "关闭", icon: "cancel", click: function(){api.close();}}];
	 	}
		$("#form1").initForm({
			success: function (response) {//处理成功
	    		if (response.success) {
	            	top.$.dialog.notice({
	             		content: "保存成功！"
	            	});
	         		api.data.window.refreshGrid();
	            	api.close();
	    		} else {
	           		$.ligerDialog.error('保存失败！<br/>' + response.msg);
	      		}
			}, getSuccess: function (response){
				$("#form1").setValues(response.ppeLoan);
				var ppeLoanSubs=response.ppeLoanSubs;
				if(pageStatus=="detail"){
					createJrxxGrid(ppeLoanSubs)
				}else{
					for(var i in ppeLoanSubs){
                		var tt = {id : ppeLoanSubs[i].id,
                				loanFk: ppeLoanSubs[i].loanFk,
                				ppeFk : ppeLoanSubs[i].ppeFk,
                				selfNo: ppeLoanSubs[i].selfNo,
                				assetName : ppeLoanSubs[i].assetName,
                				spaciModel: ppeLoanSubs[i].spaciModel,
                				sn: ppeLoanSubs[i].sn,
                				numbers: ppeLoanSubs[i].numbers,
                				unit: ppeLoanSubs[i].unit,
                				remark: ppeLoanSubs[i].remark,
                				status: ppeLoanSubs[i].status,
                				backDate: ppeLoanSubs[i].backDate,
                				preBackDate: ppeLoanSubs[i].preBackDate,
                				};
						ppeGrid.addRow(tt);
                	}
				}
				
			},
			showToolbar: true,
            toolbarPosition: "bottom",
            toolbar: tbar
    	});
	})
	function saveAndLoan(){
		//表单验证
    	if ($("#form1").validate().form()) {
    		/* $("#form1").submit(); */
    		var formData = $("#form1").getValues();
	        var data = {};
	        data = formData;
	        //验证grid
	        if(!validateGrid(ppeGrid)){
				return false;
			}
	        data["ppeLoanSubs"] = ppeGrid.getData();
	        if(data["ppeLoanSubs"]==null || data["ppeLoanSubs"].length ==0){
	        	$.ligerDialog.error('提示：' + '资产不能为空！');
	        	return false;
	        }
	        $("body").mask("正在保存数据，请稍后！");
	        $.ajax({
	            url:"PpeLoanAction/saveLoan.do?pageStatus=${param.pageStatus}&status=YJC",
	            type: "POST",
	            datatype: "json",
	            contentType: "application/json; charset=utf-8",
	           	data: $.ligerui.toJSON(data),
	            success: function (data, stats) {
	                if (data["success"]) {
	                	if(api.data.window.Qm){
	                		api.data.window.Qm.refreshGrid();
	                	}
	                    top.$.dialog.notice({content:'保存成功'});
	                    api.close();
	                }else{
	                	$.ligerDialog.error('提示：' + data.msg);
	                }
	            },
	            error: function (data,stats) {
	                $.ligerDialog.error('提示：' + data.msg);
	            }
	        });
    	}else{
    		$.ligerDialog.error('提示：' + '请填写完整后保存！');
    	}
	}
	function save(){
		//表单验证
    	if ($("#form1").validate().form()) {
    		/* $("#form1").submit(); */
    		var formData = $("#form1").getValues();
	        var data = {};
	        data = formData;
	        //验证grid
	        if(!validateGrid(ppeGrid)){
				return false;
			}
	        data["ppeLoanSubs"] = ppeGrid.getData();
	        if(data["ppeLoanSubs"]==null || data["ppeLoanSubs"].length ==0){
	        	$.ligerDialog.error('提示：' + '资产不能为空！');
	        	return false;
	        }
	        /* else if(data["ppeLoanSubs"].length >10){
	        	$.ligerDialog.error('提示：' + '资产不能大于10个！');
	        	return false;
	        } */
	        $("body").mask("正在保存数据，请稍后！");
	        $.ajax({
	            url:"PpeLoanAction/saveLoan.do?pageStatus=${param.pageStatus}&status=WJC",
	            type: "POST",
	            datatype: "json",
	            contentType: "application/json; charset=utf-8",
	           	data: $.ligerui.toJSON(data),
	            success: function (data, stats) {
	                if (data["success"]) {
	                	if(api.data.window.Qm){
	                		api.data.window.Qm.refreshGrid();
	                	}
	                    top.$.dialog.notice({content:'保存成功'});
	                    api.close();
	                }else{
	                	$.ligerDialog.error('提示：' + data.msg);
	                }
	            },
	            error: function (data,stats) {
	                $.ligerDialog.error('提示：' + data.msg);
	            }
	        });
    	}else{
    		$.ligerDialog.error('提示：' + '请填写完整后保存！');
    	}
	}
	//使用部门选择
	function chooseOrg(){
        top.$.dialog({
            width: 800,
            height: 450,
            lock: true,
            parent: api,
            title: "选择部门",
            content: 'url:app/common/org_choose.jsp',
            cancel: true,
            ok: function(){
                var p = this.iframe.contentWindow.getSelectedPerson();
                if(!p){
                    top.$.notice("请选择部门！", 3, "k/kui/images/icons/dialog/32X32/hits.png");
                    return false;
                }
                $("#departmentId").val(p.id);
                $("#departmentName").val(p.name);
            }
        });
    }
	//使用人员选择 
	function choosePerson(value){
		selectUnitOrUser("4",1,"","",function(datas){
			if(!datas.code)return;
			/* var codeArr = datas.code.split(",");
			var nameArr = datas.name.split(","); */
			var codeArr = datas.code;
			var nameArr = datas.name;
			if(value.id=="loanerName"){
				$("#loanerId").val(codeArr);
	            $("#loanerName").val(nameArr);
			}else if(value.id=="handover"){
				$("#handoverId").val(codeArr);
	            $("#handover").val(nameArr);
			}else if(value.id=="backer"){
				$("#backerId").val(codeArr);
	            $("#backer").val(nameArr);
			}else if(value.id=="receiver"){
				$("#receiverId").val(codeArr);
	            $("#receiver").val(nameArr);
			}
		});
	}
	//打印
    function print(){
        var LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
        var strBodyStyle = "<style>" + $("#pstyle").html() + "</style>";
        LODOP.PRINT_INIT("打印固定资产借用、归还登记表");
        LODOP.ADD_PRINT_HTM('12px', '10px', "750px", "100%", strBodyStyle+ $("#form1").html());
        LODOP.SET_PRINT_PAGESIZE (1, 0, 0,"A4");
        LODOP.PREVIEW();
        //LODOP.PRINT();
        api.close();
     }
	//定义colums
	function defineColumns(){
		if(pageStatus=="add"){
			columns.push({ display: "<a class='l-a iconfont l-icon-add' href='javascript:void(0);' onclick='javascript:addPpe()' title='增加'><span>增加</span></a>", 
				isSort: false, 
				width: '30',
				height:'5%', 
				render: function (rowdata, index, value ) {
					var h = "";
					if (!rowdata._editing) {
						h += "<a class='l-a l-icon-del' href='javascript:delPpe(ppeGrid,"+index+")' title='删除'><span>删除</span></a> ";
					}
					return h;
				}
			});
		}else if(pageStatus=="modify"){
			columns.push({ display: "<a class='l-a iconfont l-icon-add' href='javascript:void(0);' onclick='javascript:addPpe()' title='增加'><span>增加</span></a>", 
				isSort: false, 
				width: '30',
				height:'5%', 
				render: function (rowdata, index, value ) {
					var h = "";
					if (!rowdata._editing) {
						h += "<a class='l-a l-icon-del' href='javascript:delPpe(ppeGrid,"+index+")' title='删除'><span>删除</span></a> ";
					}
					return h;
				}
			});
		}
		columns.push({display: 'id', name: 'id', hide:true },
					 {display: 'loanFk', name: 'loanFk', hide:true },
					 {display: 'ppeFk', name: 'ppeFk', hide:true },
					 {display: '资产编号',width: '10%',name: 'selfNo',type:'text',required:false},
					 {display: '资产名称',width: '15%',name: 'assetName',type: 'text',required:false},
					 {display: '规格型号',width: '15%',name: 'spaciModel',type:'text',required:false},
					 {display: '序列号',width: '10%',name: 'sn',type:'text',required:false},
					 {display: '数量',width: '6%',name: 'numbers',type:'int',required:true,editor : {type: 'spinner',maxlength:'32'}},
					 {display: '单位',width: '6%',name: 'unit',type:'text',required:false},
					 {display: 'status', name: 'status', hide:true },
					 {display: '预计归还</br>日期',width: '10%', name: 'preBackDate',type:'date',required:true,editor : {type : 'date'}},
					 {display: '归还日期',width: '10%', name: 'backDate',type:'date'},
					 {display: '备注',width: '12%',name: 'remark',type:'text',required:false,editor : {type : 'text',maxlength:'500'}}
		);
	}
	//可grid列表
	function initGrid(){
	    ppeGrid = $("#ppe").ligerGrid({
			columns: columns,
		    enabledEdit:pageStatus!="detail",
		    clickToEdit: true,
		    rownumbers: true,    
		    width:"99.80%",
		    frozenRownumbers: false,
		    usePager: false,
		    data: {Rows: []}
		});
	}
	function addPpe() {
		 dia1 = $.ligerDialog.open({ 
		 title: '选择', 
		 width: 700, 
		 height: 500,
		 parent:api, 
		 url: 'app/equipment/ppe/ppe_choose_list.jsp',
		 data: {"window" : window},
		 buttons: [
		    { text: '确定', onclick: f_importOK },
			{ text: '关闭', onclick: f_importCancel }
		]
		                                                                                                
		 });
	}
	function f_importOK(item, dialog){
		   var rows = dialog.frame.f_select();
		   if (!rows){
		   	top.$.notice("请选择行！");
		       return;
		   } 
		   var devRow = ppeGrid.rows;
		   var isexist=false;
		 
		   for(var i in rows){
			 if(rows[i].loan_status=="已借用"){
				 $.ligerDialog.error("自编号为：“"+rows[i].self_no+"”的资产已被借用！");
				 return
			 }
		   	 for(var j in devRow){
		   		if(rows[i].id == devRow[j].ppeFk){
		   			isexist = true;
		   			break;
		   		}else{
		   			isexist = false;
		   		}
		   	}
		   	 if(!isexist){
		   		var tt = {
		   				ppeFk: rows[i].id, 
		   				selfNo : rows[i].self_no,
		   				assetName : rows[i].asset_name,
		   				spaciModel : rows[i].spaci_model,
		   				sn: rows[i].sn,
		   				numbers: "1",
		   				unit : rows[i].unit,
		   				status : "0"}
				ppeGrid.addRow(tt);
		   	 }
		   }
		}
	function f_importCancel(item, dialog){
		dialog.close();
	}
	function delPpe(obj, index) {
		obj.deleteRow(index);
	}
	//detail页面加载设备信息
    function createJrxxGrid(data) {
        var jrxx_html = '<div class="l-grid-body l-grid-body2 l-scroll"><table class="l-detail-table b-dyn-table decimal-table"><tr class="l-t-td-title" style="height:30px;font-weight:normal">'
        				  +'<td class="l-t-td-title" style="width:12.3%">资产编号</td>'
        				  +'<td class="l-t-td-title" style="width:14.4%">资产名称</td>'
                          +'<td class="l-t-td-title" style="width:11.7%">规格型号</td>'
                          +'<td class="l-t-td-title" style="width:15%">序列号</td>'
                          +'<td class="l-t-td-title" style="width:6.7%">数量</td>'
                          +'<td class="l-t-td-title" style="width:6.8%">单位</td>'
                          +'<td class="l-t-td-title" style="width:13.2%">预计归还</br>日期</td>'
                          +'<td class="l-t-td-title" style="width:13.2%">归还日期</td>'
                          +'<td class="l-t-td-title" style="width:13.5%">备注</td>';
        if(data && data.length > 0){
        	 for(var i=0;i<data.length;i++){ 
        		 var selfNo="";
            	 var assetName=""; 
            	 var spaciModel="";
            	 var sn="";
            	 var numbers="";
            	 var unit="";
            	 var backDate="";
            	 var preBackDate="";
            	 var remark="";
        		  if(data[i].selfNo!=null){
        			  selfNo=data[i].selfNo;
        		  }
        		  if(data[i].assetName!=null){
        			  assetName=data[i].assetName;
        		  }
        		  if(data[i].spaciModel!=null){
        			  spaciModel=data[i].spaciModel;
        		  }
        		  if(data[i].sn!=null){
        			  sn=data[i].sn;
        		  }
        		  if(data[i].numbers!=null){
        			  numbers=data[i].numbers;
        		  }
        		  if(data[i].unit!=null){
        			  unit=data[i].unit;
        		  }
        		  if(data[i].backDate!=null){
        			  backDate=data[i].backDate;
        		  }
        		  if(data[i].preBackDate!=null){
        			  preBackDate=data[i].preBackDate;
        		  }
        		  if(data[i].remark!=null){
        			  remark=data[i].remark;
        		  }
        		  jrxx_html += '<tr class="l-t-td-right center" style="height:30px">'
                      +'<td class="l-grid-row-cell" style="text-align:center;height:30px">'+selfNo
                      +'</td><td class="l-grid-row-cell" style="text-align:center">' +assetName
                      +'</td><td class="l-grid-row-cell" style="text-align:center">' +spaciModel
                      +'</td><td class="l-grid-row-cell" style="text-align:center">' +sn
                      +'</td><td class="l-grid-row-cell" style="text-align:center">' +numbers
                      +'</td><td class="l-grid-row-cell" style="text-align:center">' +unit
                      +'</td><td class="l-grid-row-cell" style="text-align:center">' +dateFormat(preBackDate,"yyyy-MM-dd")
                      +'</td><td class="l-grid-row-cell" style="text-align:center">' +dateFormat(backDate,"yyyy-MM-dd")
                      +'</td><td class="l-grid-row-cell" style="text-align:center">' +remark
        		  /* if(data[i].status==0){
        			  jrxx_html += '<tr class="l-t-td-right center" style="height:30px">'
                          +'<td class="l-grid-row-cell" style="text-align:center;height:30px;color: red;">'+selfNo
                          +'</td><td class="l-grid-row-cell" style="text-align:center;color: red;">' +assetName
                          +'</td><td class="l-grid-row-cell" style="text-align:center;color: red;">' +spaciModel
                          +'</td><td class="l-grid-row-cell" style="text-align:center;color: red;">' +sn
                          +'</td><td class="l-grid-row-cell" style="text-align:center;color: red;">' +numbers
                          +'</td><td class="l-grid-row-cell" style="text-align:center;color: red;">' +unit
                          +'</td><td class="l-grid-row-cell" style="text-align:center;color: red;">' +remark
        		  } */
            }
        	if(data.length<10){
        		for(var j=0;j<10-data.length;j++){
        			jrxx_html += '<tr class="l-t-td-right center" style="height:30px">'
                        +'<td class="l-grid-row-cell" style="text-align:center;height:30px">'+""
                        +'</td><td class="l-grid-row-cell" style="text-align:center">' +""
                        +'</td><td class="l-grid-row-cell" style="text-align:center">' +""
                        +'</td><td class="l-grid-row-cell" style="text-align:center">' +""
                        +'</td><td class="l-grid-row-cell" style="text-align:center">' +""
                        +'</td><td class="l-grid-row-cell" style="text-align:center">' +""
                        +'</td><td class="l-grid-row-cell" style="text-align:center">' +""
                        +'</td><td class="l-grid-row-cell" style="text-align:center">' +""
                        +'</td><td class="l-grid-row-cell" style="text-align:center">' +""
        		}
        	}
        }else{
        	jrxx_html += '<tr><td colspan="7">无数据</td></tr>';
        }
        jrxx_html += '</table></div>';
        $("#ppe").html(jrxx_html);
    }
	
	//格式化日期格式，兼容IE
    function dateFormat(dateString,format) {
        if(!dateString)return "";
        var time = new Date(dateString.replace(/-/g,'/').replace(/T|Z/g,' ').trim());
        var o = {
            "M+": time.getMonth()+1, //月份
            "d+": time.getDate(), //日
            "h+": time.getHours(), //小时
            "m+": time.getMinutes(), //分
            "s+": time.getSeconds(), //秒
            "q+": Math.floor((time.getMonth()+3)/3),//季度
            "S": time.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (time.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(format)) format = format.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return format;
    }
</script>
<style type="text/css" media="print" id="pstyle">
* {font-family:"宋体";font-size:15px;letter-spacing:normal;}
table{ margin:-2 auto;width: 650px;}
table td{ height:40px}
.l-detail-table td, .l-detail-table {
    border-collapse: collapse;
    border: 1px solid black;
}
.l-detail-table {
    padding:0px;
    border:0px solid #CFE3F8;
    border-top:0px;
    border-left:0px;
    word-break:break-all;
    table-layout:fixed;
}
.check {
    width:650px;
}
.l-t-td-title{ text-align:center;}
.l-t-td-left{ text-align:center;}
.l-t-td-right{ padding-left:5px;}
</style>
</head>
<body>
    <form id="form1" action="PpeLoanAction/save.do?pageStatus=${param.pageStatus}" getAction="PpeLoanAction/detailLoan.do?id=${param.id}">
     <h1 style="padding:5mm 0 2mm 0;font-family:宋体;font-size:7mm;text-align:center;">固定资产借用、归还登记表</h1>
     <input type="hidden" name="id" id="id"/>
     <input type="hidden" name="departmentId" id="departmentId"/>
     <input type="hidden" name="loanerId" id="loanerId"/>
     <input type="hidden" name="handoverId" id="handoverId"/>
     <input type="hidden" name="backerId" id="backerId"/>
     <input type="hidden" name="receiverId" id="receiverId"/>
     <input type="hidden" name="type" id="type"/>
     <input type="hidden" name="status" id="status"/>
     <input  type="hidden"name="backDate" id="backDate"/>
     
     <input type="hidden" name="createDate" id="createDate"/>
     <input type="hidden" name="createId" id="createId"/>
     <input type="hidden" name="createBy" id="createBy"/>
     <input type="hidden" name="lastModifyDate" id="lastModifyDate"/>
     <input type="hidden" name="lastModifyId" id="lastModifyId"/>
     <input type="hidden" name="lastModifyBy" id="lastModifyBy"/>
     <input type="hidden" name="fkPpeId" id="fkPpeId"/>
     <input type="hidden" name="ppeSelfNo" id="ppeSelfNo"/>
     <table class="check">
        <tr>
        	<td width="78.5%">&nbsp;</td>
            <td width="58px" align="right">编号：</td>
            <td class="l-t-td-right" style="width: 125px;"><input name="identifier" id="identifier" type="text" ltype='text' readonly="readonly"/></td>
           </tr>
       </table>
       <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
	  <tr> 
        <td class="l-t-td-left"> 借用部门</td>
        <td class="l-t-td-right"> 
        <input name="departmentName" id="departmentName" type="text" ltype='text' validate="{required:true,maxlength:128}" ligerui="{iconItems:[{icon:'org',click:chooseOrg}]}"/>
        </td>
        <td class="l-t-td-left"> 借用日期</td>
        <td class="l-t-td-right"> 
        <input name="loanDate" id="loanDate" type="text" ltype='date' validate="{required:true}" ligerui="{format:'yyyy-MM-dd'}"/>
        </td>
       </tr>
       </table>
       	<div id="ppe"></div> 
       <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
       <tr>
       <td class="l-t-td-left"> 借用用途<br /></td>
       <td colspan="5"> 
        	<textarea id="purpose" name="purpose" rows="4" cols="100" validate="{required:true,maxlength:3000}" class="l-textarea"></textarea> 
        </td>
       </tr>
       <tr>
       <td class="l-t-td-left"><br/>申请部门<br />领导<br /><br /></td>
       <td colspan="5"> 
        	<textarea id="" name="" rows="4" cols="100" validate="{maxlength:3000}" readonly="readonly" class="l-textarea"></textarea> 
        </td>
       </tr>
       <tr>
       <td class="l-t-td-left"><br />保障部<br />领导<br /><br /></td>
       <td colspan="5"> 
        	<textarea id="" name="" rows="4" cols="100" validate="{maxlength:3000}" readonly="readonly" class="l-textarea"></textarea> 
        </td>
       </tr>
       <tr> 
        <td class="l-t-td-left" rowspan="2"> 借出时状况<br />(借用人填写)</td>
        <td colspan="3"  rowspan="2"> 
        	<textarea id="loanSate" name="loanSate" rows="3" cols="100" class="l-textarea">完好无损</textarea> 
        </td>
        <td class="l-t-td-left"> 借用人</td>
        <td class="l-t-td-right"> 
        <input name="loanerName" id="loanerName" type="text" ltype="text" onclick="choosePerson(this)"/>
        </td>
        </tr>
        <tr>
        <td class="l-t-td-left"> 交接人</td>
        <td class="l-t-td-right"> 
        <input name="handover" id="handover" type="text" ltype="text" onclick="choosePerson(this)"/>
        </td>
        </tr>
       <tr> 
        <td class="l-t-td-left" rowspan="2"> 归还时状况<br />(接收人填写)</td>
        <td colspan="3"  rowspan="2"> 
        	<textarea id="backSate" name="backSate" rows="3" cols="100" class="l-textarea"></textarea> 
        </td>
        <td class="l-t-td-left"> 归还人</td>
        <td class="l-t-td-right"> 
        <input name="backer" id="backer" type="text" ltype="text" onclick="choosePerson(this)"/>
        </td>
        </tr>
        <tr>
        <td class="l-t-td-left"> 接收人</td>
        <td class="l-t-td-right"> 
        <input name="receiver" id="receiver" type="text" ltype="text" onclick="choosePerson(this)"/>
        </td>
       </tr>
      </table>
      <table>
      	<tr>
         <td align="left" colspan="6">备注：借用人在外借期间妥善保管好资产，保证完好无损，用后及时归还。如有损坏或丢失，需进行维修或赔偿。</td>
        </tr>
      </table>
    </form> 

</body>
</html>
