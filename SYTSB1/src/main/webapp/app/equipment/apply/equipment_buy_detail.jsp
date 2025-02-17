﻿<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    CurrentSessionUser user=(CurrentSessionUser)request.getSession().getAttribute("currentSessionUser");
%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!--获取当前登录人  -->
<%
	CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
	User uu = (User)curUser.getSysUser();
	com.khnt.rbac.impl.bean.Employee e = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
	String userId=e.getId();
	String userid = SecurityUtil.getSecurityUser().getId();
	String users=curUser.getName();
	%>
<%@taglib uri="http://bpm.khnt.com" prefix="bpm" %>
<head pageStatus="${param.status}">
<title>设备采购申请</title>
<%@ include file="/k/kui-base-form.jsp"%>
<%
	String status = request.getParameter("status");
%>
<script type="text/javascript" src="pub/bpm/js/util.js"></script>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript" src="app/common/js/render.js"></script>
<link rel="Stylesheet" href="app/finance/css/jquery.autocomplete.css" />
<link type="text/css" rel="stylesheet" href="app/finance/css/form_detail.css" /> 
<script type="text/javascript" src="app/humanresources/js/doc_order.js"></script>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<script type="text/javascript" src="app/common/lodop/LodopFuncs.js"></script>
<script type="text/javascript">
	//jQuery页面载入事件
	var isChecked = "${param.isChecked}";
	var serviceId = "${requestScope.serviceId}";//提交数据的id
	var activityId = "${requestScope.activityId}";//流程id
	var processId = "${requestScope.processId}";//过程
	var areaFlag;//改变状态
	<bpm:ifPer function="TJY2_BUY_JHLZ" activityId = "${requestScope.activityId}">areaFlag="2";</bpm:ifPer>//计划论证
	<bpm:ifPer function="TJY2_BUY_SQBMSP" activityId = "${requestScope.activityId}">areaFlag="3";</bpm:ifPer>//申请部门审核
	<bpm:ifPer function="TJY2_BUY_ZNBMSP" activityId = "${requestScope.activityId}">areaFlag="4";</bpm:ifPer>//职能部门审核
	var pageStatus="${param.status}";
	var modifyType="${param.type}";
	var renew="${param.renew}";
	var deviceGrid;
	var columns=[];
	var tbar="";
	$(function(){
		defineColumns();
		initGrid();
		if(isChecked!="" && typeof(isChecked)!="undefined"){
			/* initGridFalse();//定义列表，并且设置列表不可编辑 */
			queryEquipmentList1("${requestScope.serviceId}");
			$("#table1").transform("detail");
     		$("#table1").setValues("equipmentBuyAction/detail.do?id=${requestScope.serviceId}");
     		if(areaFlag=="3"){
     			$("#table3").attr("disabled",true);
     		}else if(areaFlag=="4"){
     			$("#table2").attr("disabled",true);
     		}else{
     			$("#table2").attr("disabled",true);
        		$("#table3").attr("disabled",true);
     		}
     		//查询设备列表
     		queryEquipmentList("${requestScope.serviceId}");
    		tbar=[{ text: '审核不通过', id: 'shbtg', icon: 'del', click: shbtg},
                  { text: '审核通过', id: 'shtg', icon: 'submit', click: shtg},
                  { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
        }else if(pageStatus=="detail"){
    		tbar=[
				  { text: '打印', id: 'print', icon: 'print', click: print},
    		      { text: '关闭', id: 'close', icon:'cancel', click:function(){api.close();}}];
    	}else if(pageStatus=="modify"){
        	if(modifyType=="sign"){
        		initGridFalse();//定义列表，并且设置列表不可编辑
        		$("#table1").transform("detail");
        		$("#table1").setValues("equipmentBuyAction/detail.do?id=${param.id}");
         		$("#table2").attr("disabled",true);
        		$("#table3").attr("disabled",true);
        	}else if(modifyType=="buy"){
        		initGridFalse();//定义列表，并且设置列表不可编辑
        		$("#table1").transform("detail");
         		$("#table1").setValues("equipmentBuyAction/detail.do?id=${param.id}");
         		$("#table2").attr("disabled",true);
        		$("#table3").attr("disabled",true);
        	}else if(modifyType=="inspect"){
        		initGridFalse();//定义列表，并且设置列表不可编辑
        		$("#table1").transform("detail");
         		$("#table1").setValues("equipmentBuyAction/detail.do?id=${param.id}");
         		$("#table2").attr("disabled",true);
        		$("#table3").attr("disabled",true);
        	}else{
        		$("#table2").attr("disabled",true);
        		$("#table3").attr("disabled",true);
        		tbar=[{text : '保存',id : 'save',icon : 'save',click : save}, 
    				  {text : '关闭',id : 'close',icon : 'cancel',click : function(){api.close();}
    			}];
        	}
        }else{
        	$("#table2").attr("disabled",true);
    		$("#table3").attr("disabled",true);
    		tbar=[{text : '保存',id : 'save',icon : 'save',click : save}, 
				  {text : '关闭',id : 'close',icon : 'cancel',click : function(){api.close();}
			}];
        }
		$("#formObj").initForm({ //参数设置示例
			showToolbar: true,
          	toolbar : tbar,
            toolbarPosition : "bottom",
			getSuccess : function(res) {
				if(res.success){
					/* deviceGrid.loadData({
						Rows : res.inspectionDatas
					}); */
					$("#formObj").setValues({id:res.id});
					$("#formObj").setValues(res.data);
				}
			}
		});
		$("body").append('<object style="height:1px;" id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0><embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed></object>');
		$("#form_contract").initForm({ //参数设置示例
    	    success : function(res){
    	    	$("#conId").val(res.id);
    	    	top.$.dialog.notice({
					content : '保存成功'
				});
    	    	api.data.window.Qm.refreshGrid();
    	    },
	            getSuccess : function(res) {
            	 $("#form_contract").setValues(res.con)
            	 if (res.con.documentName!= null && res.con.documentName!= undefined){
 					showAttachFile(res.con);
 				}
             },
           afterParse : function(formObj) {//form表单完成渲染后，回调函数

             }
         });
	  // 合同上传
	  if(pageStatus=="modify"&&modifyType=="sign"){
		var receiptUploadConfig = {
    			fileSize : "50mb",	//文件大小限制
    			businessId : "",	//业务ID
    			buttonId : "procufilesBtn",		//上传按钮ID
    			container : "procufilesDIV",	//上传控件容器ID
    			title : "文件",	//文件选择框提示
    			extName : "doc",	//文件扩展名限制
    			workItem : "",	//页面多点上传文件标识符号
    			fileNum : 1,	//限制上传文件数目
    			callback : function(file){	//上传成功后回调函数,实现页面文件显示，交与业务自行后续处理
    				var id=$("#conId").val();
    				if(renew=="renew"){
    	    			id="";
    	    		}
    			 $.ajax({
                     url: "equipmentContractAction/saveFile.do?id="+id+"&documentId="+file[0].path,
                     type: "POST",
                     data:"&documentName="+file[0].name,
                     dataType:'json',
                     async: false,
                     success:function (data) {
                        if(data.success){
                     	addAttachFile(file);
                     /* editor(file[0].path,file[0].name,"add"); */
                        }else{
                            $.ligerDialog.warn(data.msg);
                        }
                     },
                     error:function () {
                         $.ligerDialog.warn("提交失败！");
                     }
                 });
    			}
    	};
		var receiptUploader= new KHFileUploader(receiptUploadConfig);
	  }
	});
	//定义列表
	function defineColumns(){
		if(pageStatus=="add"){
			var nowDate = new Date().format("yyyy-MM-dd");
			$("#applyDate").val(nowDate);
			columns.push({
				display: "<a class='l-a iconfont l-icon-add' href='javascript:deviceGrid.addEditRow();'><span>增加</span></a>",
				isSort: false, 
				width: '30',
				render : function(item, index) {
    				return "<a class='l-a l-icon-del' href='javascript:delDevice(deviceGrid," + index + ")'><span>删除</span></a>";
    			}
			});
		}else if(pageStatus=="detail"){
			//查询设备列表
			queryEquipmentList1("${param.id}");
		}else if(pageStatus=="modify"){
			if(modifyType=="sign"){
				//查询设备列表
		        queryEquipmentList("${param.id}");
			}else{
				columns.push({
					display: "<a class='l-a iconfont l-icon-add' href='javascript:deviceGrid.addEditRow();'><span>增加</span></a>",
					isSort: false, 
					width: '30',
					render: function(rowdata, index, value){
						var h = "";
						if(!rowdata._editing){
							h += "<a class='l-a l-icon-del' href='javascript:delDevice(deviceGrid,"+index+")' title='删除'><span>删除</span></a> ";
						}
						return h;
					}
				});
				//查询设备列表
		        queryEquipmentList("${param.id}");
			}
		}
		columns.push({display: 'id', name: 'id', hide:true},
				{display: 'id', name: 'applyListId', hide:true},
				{display: '设备名称',width: '16%',name: 'eqName',type:'text',required:true,editor : {type : 'text',maxlength:'200'}},
				{display: '规格型号',width: '16%',name:'eqModel',type:'text',required:true,editor : {type : 'text',maxlength:'200'}},
				{display: '单价（元）',width: '14%',name: 'eqBuyPrice',type:'float',required:true,editor : {type : 'float',minValue:0}},
				{display: '数量（件）',width: '14%',name: 'eqBuyCount',type:'int' ,required:true,editor : {type : 'int',minValue:1}},
				{display: '用途',width: '30%',name: 'eqUsed',type:'text',maxlength:'1000',required:true,editor : {type : 'text',maxlength:'1000'}}
		);
	}
	//detail页面加载设备列表信息
    function createJrxxGrid(data) {
        var jrxx_html = '<div class="l-grid-body l-grid-body2 l-scroll"><div class="l-grid-body-inner"><table id="table4" class="l-detail-table b-dyn-table decimal-table"><tr class="l-t-td-title" style="height:30px;font-weight:normal">'
                          +'<td class="l-t-td-title" style="width:15.6%">设备名称</td>'
                          +'<td class="l-t-td-title" style="width:16%">规格型号</td>'
                          +'<td class="l-t-td-title" style="width:14%">单价（元）</td>'
                          +'<td class="l-t-td-title" style="width:14%">数量（件）</td>'
                          +'<td class="l-t-td-title" style="width:39.7%">用途</td>'; 
        if(data && data.length > 0){
        	 for(var i=0;i<data.length;i++){ 
        		 var eqName="";
            	 var eqModel=""; 
            	 var eqBuyPrice="";
            	 var eqBuyCount="";
            	 var eqUsed="";
        		  if(data[i].eqName!=null){
        			  eqName=data[i].eqName;
        		  }
        		  if(data[i].eqModel!=null){
        			  eqModel=data[i].eqModel;
        		  }
        		  if(data[i].eqBuyPrice!=null){
        			  eqBuyPrice=data[i].eqBuyPrice;
        		  }
        		  if(data[i].eqBuyCount!=null){
        			  eqBuyCount=data[i].eqBuyCount;
        		  }
        		  if(data[i].eqUsed!=null){
        			  eqUsed=data[i].eqUsed;
        		  }
        		  
        		  
                jrxx_html += '<tr class="l-t-td-right center" style="height:30px">'
                               +'<td class="l-grid-row-cell" style="text-align:center;height:30px">'+eqName
                               +'</td><td class="l-grid-row-cell" style="text-align:center">' +eqModel
                               +'</td><td class="l-grid-row-cell" style="text-align:center">' +eqBuyPrice
                               +'</td><td class="l-grid-row-cell" style="text-align:center">' +eqBuyCount
                               +'</td><td class="l-grid-row-cell" style="text-align:center">'+eqUsed
            }
        }else{
        	jrxx_html += '<tr><td colspan="5">无数据</td></tr>';
        }
        jrxx_html += '</table></div></div>';
        $("#device").html(jrxx_html);
    }
		
		
	 function initGrid(){
        deviceGrid = $("#device").ligerGrid({
	    	columns: columns,
	    	enabledEdit: pageStatus != "detail",
	    	rownumbers : true,
		    width:"99.6%",
		    frozenRownumbers: false,
		    usePager: false,
	        data: {Rows: []} 
    	});
	 } 
	 function initGridFalse(){
	        deviceGrid = $("#device").ligerGrid({
		    	columns: columns,
		    	enabledEdit: false,
		    	rownumbers : true,
			    width:"99.6%",
			    frozenRownumbers: false,
			    usePager: false,
		        data: {Rows: []} 
	    	});
	 } 
	function save(){
		url = "equipmentBuyAction/applySave.do"; 
		url1 = "equipmentBuyAction/applyUpdate.do"; 
		//验证表单数据
		if($('#formObj').validate().form()){
			var formData = $("#formObj").getValues();
	        var data = {};
	        data = formData;
	        //验证grid
	        if(!validateGrid(deviceGrid)){
				return false;
			}
	        data["equipmentApplyList"] = deviceGrid.getData();
	        if(data["equipmentApplyList"]==null || data["equipmentApplyList"].length ==0){
	        	$.ligerDialog.error('提示：' + '设备信息不能为空！');
	        	return false;
	        }
	        $("body").mask("正在保存数据，请稍后！");
	        if(pageStatus=="add"){
	        	$.ajax({
		        	url: url,
		            type: "POST",
		            datatype: "json",
		            contentType: "application/json; charset=utf-8",
		           	data: $.ligerui.toJSON(data),
		            success: function(data, stats){
		            	$("body").unmask();
		                if(data["success"]){
		                	if(api.data.window.Qm){
		                		api.data.window.Qm.refreshGrid();
		                	}
		                    top.$.dialog.notice({content:'保存成功'});
		                    api.close();
		                }else{
		                	$.ligerDialog.error('提示：' + data.msg);
		                	$("#save").removeAttr("disabled");
		                }
		            },
		            error: function(data,stats){
		            	$("body").unmask();
		                $.ligerDialog.error('提示：' + data.msg);
		                $("#save").removeAttr("disabled");
		            }
		        });
	        }else if(pageStatus=="modify"){
	        	$.ajax({
		        	url: url1,
		            type: "POST",
		            datatype: "json",
		            contentType: "application/json; charset=utf-8",
		           	data: $.ligerui.toJSON(data),
		            success: function(data, stats){
		            	$("body").unmask();
		                if(data["success"]){
		                	if(api.data.window.Qm){
		                		api.data.window.Qm.refreshGrid();
		                	}
		                    top.$.dialog.notice({content:'保存成功'});
		                    api.close();
		                }else{
		                	$.ligerDialog.error('提示：' + data.msg);
		                	$("#save").removeAttr("disabled");
		                }
		            },
		            error: function (data,stats) {
		            	$("body").unmask();
		                $.ligerDialog.error('提示：' + data.msg);
		                $("#save").removeAttr("disabled");
		            }
		        });
	        }
		}
	}
	function close(){	
		 api.close();
	}		
	function delDevice(obj, index) {
		$.ligerDialog.confirm("确定删除该数据？", function(yes){
       		if(yes){
       			obj.deleteRow(index);
       		}
       	});
	}
	//详情页面查询设备列表
	function queryEquipmentList1(id){
		$.ajax({
        	url: "equipmentBuyAction/detail1.do?id="+id,
            type: "POST",
            success: function (resp) {
                if (resp.success) {
                	$("#formObj").setValues(resp.baseEquipment2Apply);
                	var equipmentApplyLists = resp.equipmentApplyLists;
                	//将设备显示在申请列表里面
                	createJrxxGrid(equipmentApplyLists);
                }else{
                	$.ligerDialog.error('提示：' + data.msg);
                }
            },
            error: function (data0,stats) {
                $.ligerDialog.error('提示：' + data.msg);
            }
        });
	}
	//其他页面查询设备列表
	function queryEquipmentList(id){
		$.ajax({
        	url: "equipmentBuyAction/detail1.do?id="+id,
            type: "POST",
            success: function (resp) {
                if (resp.success) {
                	$("#formObj").setValues(resp.baseEquipment2Apply);
                	var equipmentApplyLists = resp.equipmentApplyLists;
                	//将设备显示在申请列表里面
                	for(var e in equipmentApplyLists){
                		var bb = {id : equipmentApplyLists[e].id,
                				applyListId: equipmentApplyLists[e].applyListId,
                				eqName : equipmentApplyLists[e].eqName,
                				eqModel: equipmentApplyLists[e].eqModel,
                				eqBuyPrice : equipmentApplyLists[e].eqBuyPrice,
                				eqBuyCount: equipmentApplyLists[e].eqBuyCount,
                				eqUsed: equipmentApplyLists[e].eqUsed};
						deviceGrid.addRow(bb);
                	} 
                }else{
                	$.ligerDialog.error('提示：' + data.msg);
                }
            },
            error: function (data0,stats) {
                $.ligerDialog.error('提示：' + data.msg);
            }
        });
	}
	//申请人选择
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
	            $("#applyId").val(p.id);
	            $("#applyName").val(p.name);
	            $("#applyUnitId").val(p.org_id);
	            $("#applyUnitName").val(p.org_name);
	        }
	    });
    }
	//合同签订人选择
	function choosePerson1(){
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
	            $("#signedMan").val(p.name);
	        }
	    });
    }
    //审核通过
	function shtg(){
     	var id=$("#id").val();
     	var formData=$("#formObj").getValues();
     	var equipmentBuyApply=$.ligerui.toJSON(formData)
        $.ligerDialog.confirm('是否审核通过？', function (yes){
	        if(!yes){return false;}
	        $("body").mask("提交中...");
	        getServiceFlowConfig("TJY2_EQUIPMENT_BUY","",function(result,data){
	        	if(result){
	            	top.$.ajax({
	                	url: "equipmentBuyAction/subPass.do?id="+serviceId+
	                    	"&typeCode=TJY2_EQUIPMENT_BUY&status="+"&activityId="+activityId+"&areaFlag="+areaFlag,
	                    type: "POST",
	                    dataType:'json',
	          	 		data: {"equipmentBuyApply":equipmentBuyApply},
	                    async: false,
	                    success:function (data) {
	                    	if (data) {
	                       	api.data.window.Qm.refreshGrid();
	                    	api.close();
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
		
	//审核不通过
	function shbtg(){
		 var id=$("#id").val();
       	 var formData=$("#formObj").getValues();
       	 var equipmentBuyApply=$.ligerui.toJSON(formData)
    	 $.ligerDialog.confirm('是否要不通过？', function (yes){
	         if(!yes){return false;}
	    	 $("body").mask("正在处理，请稍后！");
	    	 getServiceFlowConfig("TJY2_EQUIPMENT_BUY","",function(result,data){
	    		 if(result){
	                 top.$.ajax({
	                     url: "equipmentBuyAction/shbtg.do?id="+serviceId+
	                    		 "&typeCode=TJY2_EQUIPMENT_BUY&status="+"&activityId="+activityId+"&areaFlag="+areaFlag+"&processId="+processId,
	                     type: "POST",
	                     dataType:'json',
	                     data:{"equipmentBuyApply":equipmentBuyApply},
	                     async: false,
	                     success:function (data) {
	                         if (data) {
	                           	api.data.window.Qm.refreshGrid();
				                api.close();
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
	//添加合同附件
	  function addAttachFile(files){
  	  	status="add";
  		if("${param.status}"=="detail"){
  			$("#procufilesBtn").hide();
  		}
  		if(files){
  			var attContainerId="procufiles";
  			$.each(files,function(i){
  				var file=files[i];
  				
  				 $("#procufiles3").append("<li id='"+file.id+"'>"+
  						"<div><a href='#' onclick='editor(\""+file.path+"\",\""+file.name+"\",\""+status+"\");return false'>"+file.name+"</a></div>"+
  						"<div class='l-icon-close progress' onclick='deleteUploadFile(\""+file.id+"\",\""+file.path+"\",this,getUploadFile)'>&nbsp;</div>"+
  						"</li>"); 
  			});
  			//getUploadFile();
  		}
  	}
	// 将上传的合同附件id写入隐藏框中
  	function getUploadFile(){
  		var attachId="";
  		var i=0;
  		$("#procufiles li").each(function(){
  			attachId+=(i==0?"":",")+this.id;
  			i=i+1;
  		});
  		if(attachId!=""){
  			attachId=attachId.substring(0,attachId.length);
  		}
  		$("#uploadFiles").val(attachId);
  	}
  	//编辑word文档
  	function editor(docId,docName,status){
  		var type="";
  		var id=$("#conId").val()
  		var documentDoc=$("#documentDoc").val()
  		var doc="0";
  		if(documentDoc!=""&&documentDoc!=null){
  			doc="1";
  		}
  		if(status==""){
  			type="modify";
  		}else{
  			type="add";
  		}
  		//打开生成报告页面
  	 	openContentDoc({
  	 		docId : docId,
  	 		doc: doc,
  			status : "draft",
  			id:id,
  			type:type,
  			window:window,
  			title : docName,
  			tbar : {
  				edit : true,
  				print : true,
  				layout : true
  			}
  		}); 
  		
  	}
  	// 显示合同
      function showAttachFile(file){
      	if("${param.status}"=="detail"){
  			$("#procufilesBtn").hide();
  		}
  		if(file){  
  			//详情
  			var attContainerId="procufiles3";
  			if("${param.status}"=="detail"){	
  					 //显示附件
  					$("#"+attContainerId).append("<li id='"+file.id+"'>"+
  											"<div><a href='#' onclick='editor(\""+file.documentId+"\",\""+file.documentName+"\",\""+status+"\");return false'>"+file.documentName+"</a></div>"+
  											"</li>");
  			}
  			//修改
  			else if("${param.status}"=="modify"){
  				
  					$("#"+attContainerId).append("<li id='"+file.id+"'>"+
  							"<div><a href='#' onclick='editor(\""+file.documentId+"\",\""+file.documentName+"\",\""+status+"\");return false'>"+file.documentName+"</a></div>"+
  							"<div class='l-icon-close progress' onclick='deleteUploadFile(\""+file.id+"\",\""+file.documentId+"\",this,getUploadFile)'>&nbsp;</div>"+
  							"</li>");
  				getUploadFile();
  			}
  		}
      }
      //----------------------------------------------------------------------------
      //打印
      function print(){
          var LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
          var strBodyStyle = "<style>" + $("#pstyle").html() + "</style>";
          LODOP.PRINT_INIT("打印设备购买申请单");
          LODOP.ADD_PRINT_HTM('12px', '10px', "750px", "100%", strBodyStyle+ $("#formObj").html());
          LODOP.SET_PRINT_PAGESIZE (1, 0, 0,"A4");
          LODOP.PREVIEW();
          //LODOP.PRINT();
       }
      </script>
      
<style type="text/css" media="print" id="pstyle">
* {font-family:"宋体";font-size:12px;letter-spacing:normal;}
* {
    font-family:"宋体";
    font-size:12px;
    letter-spacing:normal;
    
}
h1{font-family:宋体;font-size:6mm; text-align:center;margin:10px 0 0 0;}
table{ margin:-2 auto;width: 650px;}
table td{ height:40px;}
.l-detail-table td, .l-detail-table {
    border-collapse: collapse;
    border: 1px solid black;
}
#table1,#table2,#table3{
 padding:5px;
    border:0px solid #CFE3F8;
    border-top:0px;
    border-left:0px;
    word-break:break-all;
    table-layout:fixed;
}
#table4{
 padding:5px;
    border:0px solid #CFE3F8;
    border-top:0px;
    border-left:0px;
    word-break:break-all;
    table-layout:fixed;
    text-align:center;
    margin-left:48px;
}
.l-t-td-left{ text-align:center;}
.fieldset-caption{margin:15px 0px 2px 0px;}
.l-t-td-title{}
</style>
</head>
<body>
	<div title="申请表" id="form1">
	<form name="formObj" id="formObj" method="post" action="equipmentBuyAction/applySave.do"
		getAction="equipmentBuyAction/detail.do?id=${param.id}">
		<h1 style="padding:5mm 0 2mm 0;font-family:微软雅黑;font-size:6mm;text-align:center;">采&nbsp;购&nbsp;申&nbsp;请&nbsp;表</h1></br>
		<input type="hidden" id="id" name="id"/>
		<input type="hidden" id="applyId" name="applyId" value="<%=userid %>"/>
		<input type="hidden" id="applyUnitId" name="applyUnitId" value="<%=curUser.getDepartment().getOrgCode() %>"/>
			<table id="table1" cellpadding="3" cellspacing="0" class="l-detail-table">
			     <tr>
					<td class="l-t-td-left">申请人</td>
					<td class="l-t-td-right" >
					<input  ltype='text' readonly="readonly" value="<%=users %>" id="applyName" name="applyName" type="text" onclick="choosePerson()" ligerui="{iconItems:[{icon:'user',click:choosePerson}]}"/>
					<td class="l-t-td-left">申请部门</td>
					<td class="l-t-td-right">
					<input  validate="{maxlength:50,required:true}" value="<%=curUser.getDepartment().getOrgName() %>" readonly="readonly" ltype="text"  name="applyUnitName" id="applyUnitName"  type="text" ligerui="{iconItems:[{icon:'org'}]}"/>
				</tr>
				<tr>
					<td class="l-t-td-left">申请日期</td>
					<td class="l-t-td-right" colspan="3">
					<input name="applyDate" id="applyDate" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" /></td>
				</tr>
				</table>
			<div id="device" style="width:100%"></div>
			<table id="table2" cellpadding="3" cellspacing="0" class="l-detail-table">
			     <tr>
				 <td colspan="5" text-align="center" height="30px" background="#f6f6f6" text-align="left">申请部门负责人意见</td>
				 </tr>
				 <tr>
					<td colspan="5">
						<textarea name="sqbmyj" id="sqbmyj" rows="4" cols="25" class="l-textarea" maxlength="400"></textarea>
					</td>
				 </tr>
				 <tr>
					<td class="l-t-td-left" colspan="3"></td>
					<td class="l-t-td-left">审核日期</td>
					<td class="l-t-td-right" >
					<input name="sqbmshDate" id="sqbmshDate" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" /></td>
				 </tr>
				</table>
			<table id="table3" cellpadding="3" cellspacing="0" class="l-detail-table">
			     <tr>
					<td colspan="5" text-align="center" height="30px" background="#f6f6f6" text-align="left">职能部门负责人意见</td>
				 </tr>
				 <tr>
					<td colspan="5">
						<textarea name="znbmyj" id="znbmyj" rows="4" cols="25" class="l-textarea" maxlength="400"></textarea>
					</td>
				 </tr>
				 <tr>
				 	<td class="l-t-td-left" colspan="3"></td>
					<td class="l-t-td-left">审核日期</td>
					<td class="l-t-td-right" >
					<input name="znbmshDate" id="znbmshDate" type="text" ltype='date' ligerui="{format:'yyyy-MM-dd'}" /></td>
				 </tr>
				</table>
	</form>
	</div>
	<%-- <c:if test="${param.status=='detail' || (param.status=='modify'&& param.type=='sign')}">
		<div title="合同签订" id="form2">
			<%@ include file="equipment_buy_contract.jsp"%>
		</div>
		<div title="采购确认" id="form3">
			<%@ include file="equipment_buy_confirmed.jsp"%>
		</div>
		<div title="验货确认" id="form4">
			<%@ include file="equipment_buy_examine.jsp"%>
		</div>
	</c:if> --%>
</body>
</html>
