<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title></title>
<%@include file="/k/kui-base-list.jsp"%>
<script type="text/javascript" src="pub/bpm/js/util.js"></script>

<script type="text/javascript">
	var qmUserConfig = {
		sp_defaults:{columnWidth:0.3,labelAlign:'right',labelSeparator:'',labelWidth:120},	
		sp_fields:[
         {name:"file_id",compare: "like"},
         {name:"file_name",compare: "like"},
         {group: [
  				{name: "registrant_time", compare: ">="},
  				{label: "到", name: "registrant_time", compare: "<=", labelAlign: "center", labelWidth:20}
  		 ],columnWidth:0.4}
	    ],
		tbar: [ {
			text: '详情', id: 'detail', icon: 'detail', click: detail
		}, "-", {
			text: '申请', id: 'add', icon: 'add', click: add
		}, "-", {
			text: '修改', id: 'edit', icon: 'modify', click: edit
		}, "-", {
			text: '删除', id: 'del', icon: 'delete', click: del
		}, "-", {
	        text:'提交', id:'submit',icon:'submit', click: submit
	    }/* , "-", {
	        text:'提交新体系文件', id:'submitCd',icon:'add', click: submitCd
	    }, "-", {
	        text:'提交传递单', id:'submitTj2',icon:'submit', click: submitTj2
	    }, "-", {
	        text:'接受意见', id:'js',icon:'appear', click: js 
	    } */, "-", {
	        text:'审核过程', id:'gc',icon:'follow', click: gc
	    }, "-", {
	        text:'替换新文件', id:'th',icon:'issued', click: th 
	    }],
	   
		listeners: {
			rowClick: function(rowData, rowIndex) {
			},
			rowDblClick: function(rowData, rowIndex) {
				detail();
			},
			selectionChange: function(rowData, rowIndex) {
				var count = Qm.getSelectedCount();
				Qm.setTbar({
					detail: count==1,
					edit: count==1,
					del: count>0,
					submit: count==1
				});
			},
			selectionChange : function(rowData, rowIndex) {
				var count = Qm.getSelectedCount();//行选择个数
				//alert(count);
				var up_status = Qm.getValueByName("status");
				//alert(up_status);
				var sp_status = Qm.getValueByName("sp_status");
				//alert(sp_status);
				var status={};
				if(count==0){
					status={detail:false, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:false};
				}else if(count==1){
					if("申请单审核中"==up_status){
						if("undefined"==sp_status){
							status={detail:true, edit:false, del:true,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
						}else{
							status={detail:true, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
						}
					}else if("提交新体系文件"==up_status){
						if("undefined"==sp_status){
							status={detail:false, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
						}else{
							status={detail:true, edit:false, del:false,submit:false,submitCd:true,submitTj2:false,th:false,js:false,gc:true};
						}
					}else if("传递单未提交"==up_status){
						if("undefined"==sp_status){
							status={detail:false, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
						}else{
							status={detail:true, edit:false, del:false,submit:false,submitCd:true,submitTj2:true,th:false,js:false,gc:true};
						}
					}else if("传递单审核中"==up_status){
						if("undefined"==sp_status){
							status={detail:false, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
						}else{
							status={detail:true, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
						}
					}else if("审批通过"==up_status){
						if("undefined"==sp_status){
							status={detail:false, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:true,js:false,gc:true};
						}else{
							status={detail:true, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:true,js:false,gc:true};
						}
					}else if("审批未通过"==up_status){
						if("undefined"==sp_status){
							status={detail:false, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
						}else{
							status={detail:true, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
						}
					}else if("接收意见"==up_status){
						if("undefined"==sp_status){
							status={detail:false, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:true,gc:true};
						}else{
							status={detail:true, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:true,gc:true};
						}
					}else if("复审中"==up_status){
						if("undefined"==sp_status){
							status={detail:false, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
						}else{
							status={detail:true, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
						}
					}else if("已替换"==up_status){
						if("undefined"==sp_status){
							status={detail:false, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
						}else{
							status={detail:true, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
						}
					}else{
						status={detail:true, edit:true, del:true,submit:true,submitCd:false,submitTj2:false,th:false,js:false,gc:true};
					}
				}else{
					if("申请单审核中"==sp_status){
						status={detail:false, edit:false, del:true,submit:false,submitCd:false,submitTj2:false,th:false,gc:false};
					}else{
						status={detail:false, edit:false, del:false,submit:false,submitCd:false,submitTj2:false,th:false,gc:false};
					}
				}
				Qm.setTbar(status);
			
			},
	        rowAttrRender : function(rowData, rowid) {
                var fontColor="black";
                if (rowData.status=='审批通过'){
                    fontColor="green";
                }else if(rowData.status=='审批未通过') {
                    fontColor="red";
                }else if(rowData.status=='已提交') {
                    fontColor="orange";
                }else if(rowData.status=='申请单审核中') {
                    fontColor="blue";
                }else if(rowData.status=='传递单审核中'){
               	 	fontColor="purple";
                }else if(rowData.status=='提交新体系文件'){
               	 	fontColor="olive";
                }else if(rowData.status=='接收意见'){
               	 	fontColor="#73b9a2";
                }else if(rowData.status=='复审中'){
               	 	fontColor="#005831";
                }else if(rowData.status=='已替换'){
               	 	fontColor="#8E8E8E";
                }
                return "style='color:"+fontColor+"'";
            }
		}
	};
	function js(){
		var jsyijian="jsyijian";
		var id = Qm.getValueByName("id");
		var lcid;
		top.$.ajax({
            url: "quality/updateFile1/getactivityId.do?id="+id,
            type: "GET",
            dataType:'json',
            async: false,
            success:function (data) {
           		lcid=data.activityId;
            }
        });
		top.$.dialog({
			width: 900,
			height: 600,
			lock: true,
			parent: api,
			data: {
				window: window
			},
			title: "接收",
			content: 'url:app/qualitymanage/transmit_datail2.jsp?id=' + id + '&pageStatus=modify'+'&jsyijian='+jsyijian+"&lcid="+lcid
		});
	}
	function gc(){
		var id = Qm.getValueByName("id");
		if(!id){
           // $.ligerDialog.alert("请先选择要查看的数据！");
            var manager = $.ligerDialog.waitting('请先选择要查看的数据！');
            setTimeout(function (){manager.close();}, 4000);
            return;
        }
		top.$.dialog({
			width: 715,
			height: 350,
			lock: true,
			parent: api,
			data: {
				window: window
			},
			title: "审核过程",
			content: 'url:app/qualitymanage/update_process.jsp?ids=' + id + '&pageStatus=detail'
		});
	}
	function updateFc(){
		var id = Qm.getValueByName("id");
		var up_status = Qm.getValueByName("status");
        $.ligerDialog.confirm('是否要废除？', function (yes){
        	 if(!yes){return false;}
            top.$.ajax({
                 url: "quality/updateFile1/abolish.do?id="+id,
                 type: "GET",
                 dataType:'json',
                 async: false,
                 success:function (data) {
                	 if("已废除"==up_status){
                		 $.ligerDialog.warn("此条已废除！");
                	 }else{
	                	 if(data.success){
	                		 top.$.notice('废除成功！！！',3);
	                         Qm.refreshGrid();//刷新
	                     }else{
	                         $.ligerDialog.warn(data.msg);
	                     }
                	 }
                 },
                 error:function () {
                	 $.ligerDialog.warn('废除失败！！！');
                 }
             });
        });
	}
	function submitTj2(){
		var serviceId =Qm.getValueByName("id");//提交数据的id
    	$.ligerDialog.confirm('是否提交审核？', function (yes){
        	if(!yes){return false;}
         	$("body").mask("提交中...");
         	getServiceFlowConfig("TJY2_ZL_XGSQ","",function(result,data){
                if(result){
                     top.$.ajax({
                         url: "quality/updateFile1/cddtj.do?id="+serviceId+"&typeCode=TJY2_ZL_XGSQ&status=",
                         type: "GET",
                         dataType:'json',
                         async: false,
                         success:function (data) {
                             if (data) {
                            	 $("body").unmask();
                            	 top.$.notice('提交成功！！！',3);
                                Qm.refreshGrid();//刷新
                             }
                         }
                     });
                }else{
                	$("body").unmask();
	                 $.ligerDialog.alert("出错了！请重试！");
                }
           });
      	});
	}
	function submit(){
		var id = Qm.getValueByName("id");
        if(!id){
           // $.ligerDialog.alert("请先选择要提交审核的数据！");
       	 	top.$.notice('请先选择要提交审核的数据！',3);
            return;
        }
        $.ligerDialog.confirm('是否提交审核？', function (yes){
        if(!yes){return false; }
         $("body").mask("提交中...");
         getServiceFlowConfig("TJY2_SCIENTIFIC_XGSQ","",function(result,data){
        	 if(result){
                 top.$.ajax({
                     url: "tjy2ScientificFilesUpdateAction/subFolws.do?id="+id+"&flowId="+data.id+"&typeCode=TJY2_SCIENTIFIC_XGSQ&status="+status,
                     type: "GET",
                     dataType:'json',
                     async: false,
                     success:function (data) {
                         if (data) {
                        	$("body").unmask();
                       	 	top.$.notice('提交成功！！！',3);
                            Qm.refreshGrid();
                         }
                     },
                     error:function () {
                    	 $("body").unmask();
                    	 $.ligerDialog.error('请出错了！请重试！!');
                     }
                 });
            }else{
            	$.ligerDialog.error('出错了！请重试！',3);
             $("body").unmask();
            }
         });
        });
	}
	function add() {
		top.$.dialog({
			width: 900,
			height: 600,
			lock: true,
			parent: api,
			data: {
				window: window
			},
			title: "申请",
			content: 'url:app/fwxm/scientific/bzgl/update_apply_datail.jsp?pageStatus=add'
		});
	}
	function submitCd(){
		var id = Qm.getValueByName("id");
		top.$.dialog({
			width: 900,
			height: 600,
			lock: true,
			parent: api,
			data: {
				window: window
			},
			title: "填写传递单",
			content: 'url:app/qualitymanage/transmit_datail2.jsp?id=' + id + '&pageStatus=modify'
			//content: 'url:app/qualitymanage/update_apply_datail.jsp?pageStatus='+Qm.getValueByName("status")
		});
	}
	function th(){
		var id = Qm.getValueByName("id");
		var ids = Qm.getValueByName("quality_xybz_file_id");
		$.ligerDialog.confirm('是否要替换为新文件？', function (yes){
       	 if(!yes){return false;}
           top.$.ajax({
                url: "tjy2ScientificFilesUpdateAction/thwj.do?id="+id+"&ids="+ids,
                type: "GET",
                dataType:'json',
                async: false,
                success:function (data) {
                	 if(data.success){
                		 top.$.notice('替换成功！！！',3);
                         Qm.refreshGrid();//刷新
                     }else{
                         $.ligerDialog.warn(data.msg);
                     }
                },
                error:function () {
               	 $.ligerDialog.error('替换失败！！！');
                }
            });
       });
	}
	function edit() {
		var id = Qm.getValueByName("id");
		var status1 = Qm.getValueByName("status");
		if(status1=="传递单未提交" ){
			top.$.dialog({
				width: 900,
				height: 600,
				lock: true,
				parent: api,
				data: {
					window: window
				},
				title: "修改",
				content: 'url:app/qualitymanage/transmit_datail2.jsp?id=' + id + '&pageStatus=modify'
			});
		}else{
			top.$.dialog({
				width: 900,
				height: 550,
				lock: true,
				parent: api,
				data: {
					window: window
				},
				title: "修改",
				content: 'url:app/fwxm/scientific/bzgl/update_apply_datail.jsp?id=' + id + '&pageStatus=modify'
			});
		}
	}
	function detail() {
		var id = Qm.getValueByName("id");
		var status1 = Qm.getValueByName("status");
		var identifier_c = Qm.getValueByName("identifier_c");

		if(status1=="审核通过" || status1=="传递单审核中" || status1=="传递单未提交"){
			top.$.dialog({
				width: 1000,
				height: 550,
				lock: true,
				parent: api,
				data: {
					window: window
				},
				title: "详情",
				content: 'url:app/qualitymanage/transmit_datail2.jsp?id=' + id + '&pageStatus=detail'
			});
		}else{
			if(identifier_c==""){
				top.$.dialog({
					width: 1000,
					height: 550,
					lock: true,
					parent: api,
					data: {
						window: window
					},
					title: "详情",
					content: 'url:app/fwxm/scientific/bzgl/update_apply_datail.jsp?id=' + id + '&pageStatus=detail'
				});
			}else{
				top.$.dialog({
					width: 1000,
					height: 550,
					lock: true,
					parent: api,
					data: {
						window: window
					},
					title: "详情",
					content: 'url:app/qualitymanage/transmit_datail2.jsp?id=' + id + '&pageStatus=detail'
				});
			}
		}
		
	}
	function del() {
		$.del(kFrameConfig.DEL_MSG, "tjy2ScientificFilesUpdateAction/delete.do", {
			"ids": Qm.getValuesByName("id").toString()
		});
	}
	function refreshGrid() {
	    Qm.refreshGrid();
	}
	function close(){
		api.close();
	}
</script>
</head>
<body> 
	<div class="item-tm" id="titleElement">
		<div class="l-page-title">
			<div class="l-page-title-note">提示：列表数据项
				<font color="black">“黑色”</font>代表单据未提交，
                <font color="orange">“橙色”</font>代表已提交，
                <font color="blue">“蓝色”</font>代表申请单审核中，
                <font color="green">“绿色”</font>代表审核通过，
                <font color="red">“红色”</font>代表审核未通过，
                <font color="#8E8E8E">“灰色”</font>代表体系文件已替换。
			</div>
		</div>
	</div>
	<qm:qm pageid="TJY2_SCIENTIFIC_UP" singleSelect="true"></qm:qm>
</body>
</html>