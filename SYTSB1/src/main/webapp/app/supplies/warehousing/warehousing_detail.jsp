<%@page import="java.util.Date"%>
<%@page import="com.khnt.security.util.SecurityUtil"%>
<%@page import="com.khnt.security.CurrentSessionUser"%>
<%@page import="com.khnt.rbac.impl.bean.User"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus = "${param.pageStatus}">
<title>存货管理</title>
  <%@ include file="/k/kui-base-form.jsp"%>
<!-- 生成条形码JS导入 -->
<script type="text/javascript" src="app/common/lodop/LodopFuncs.js"></script>
<script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
<script type="text/javascript" src="/app/common/js/render.js"></script>

<%
	CurrentSessionUser curUser = SecurityUtil.getSecurityUser();
	User uu = (User)curUser.getSysUser();
	com.khnt.rbac.impl.bean.Employee e = (com.khnt.rbac.impl.bean.Employee)uu.getEmployee();
	String userId=e.getId();
	String userid = SecurityUtil.getSecurityUser().getId();
	String users=curUser.getName();
	 
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    String rkTime = sf.format(new Date());
    String sql=" select id,GYSMC FROM TJY2_CH_SUPPLIER WHERE STATUS=1 and CREATE_ORG_ID='"+uu.getOrg().getId()+"'";
    String typeSql="SELECT ID,LX_NAME FROM TJY2_CH_GOODS_TYPE WHERE STATE='1' and CREATE_ORG_ID='"+uu.getOrg().getId()+"'";
%>
<script type="text/javascript">
	var pageStatus = "${param.pageStatus}";
    var column=[];
	var grid;
	var tbar="";
	var rows=[];//预删除数据
	$(function() {
		//ligerui float默认取消0.00
		 $.ligerui.controls.Spinner.prototype. _getVerifyValue =function(value) {
	            var g = this,
	                p = this.options;
	            var newvalue = null;
	            if (p.type == 'float') {
	                newvalue = g._round(value, p.decimalplace);
	                if(newvalue=='0.00'){
	                    newvalue='';
	                }
	            } else if (p.type == 'int') {
	                newvalue = parseInt(value);
	                if(newvalue=='0'){
	                    newvalue='1';
	                }
	            } else if (p.type == 'time') {
	                newvalue = value;
	            }
	            if (!g._isVerify(newvalue)) {
	                return g.value;
	            } else {
	                return newvalue;
	            }
	        }

	        $.ligerui.controls.Grid.prototype. _getCellHtml = function(rowdata, column) {
	            var g = this,
	                p = this.options;
	            if (column.isrownumber) //todo 更改分页序号
	                return '<div class="l-grid-row-cell-inner">' + (parseInt(rowdata['__index']) + 1) + '</div>';
	            var htmlarr = [];
	            if(!column["_hide"]&&column["editor"]&&column["editor"]["type"]!="select"){
	                htmlarr.push('<div data-type="tab" data-value="tab_'+ (parseInt(rowdata['__index']) + 1) +"_"+ (parseInt(column['columnindex'])) +'" class="l-grid-row-cell-inner"');
				}else{
	                htmlarr.push('<div class="l-grid-row-cell-inner"');
				}
	            htmlarr.push(' style = "width:' + parseInt(column._width - 8) + 'px;');
	            if (p.fixedCellHeight) htmlarr.push('height:' + p.rowHeight + 'px;min-height:' + p.rowHeight + 'px; ');
	            if (column.align) htmlarr.push('text-align:' + column.align + ';');
	            var content = g._getCellContent(rowdata, column);
	            htmlarr.push('">' + content + '</div>');
	            return htmlarr.join('');
	        }

	        $.ligerDefaults.Grid.editors['string'].create = $.ligerDefaults.Grid.editors['text'].create = function(container, editParm, ligerui) {
			    preTab = "tab_"+(parseInt(editParm['rowindex']) + 1) +"_"+ (parseInt(editParm["column"]['columnindex']));
	            var str1 = "<input type='text' data-type='tab' data-value='tab_"+ (parseInt(editParm['rowindex']) + 1) +"_"+ (parseInt(editParm["column"]['columnindex'])) +"' class='l-text-editing'/>";
	            var input = $(str1);
	            input.attr("ligerui", liger.toJSON(ligerui));
	            container.append(input);
	            input.ligerTextBox(); //input.focus();
	            return input;
	        }

	        $.ligerDefaults.Grid.editors['int'].create = $.ligerDefaults.Grid.editors['float'].create = $.ligerDefaults.Grid.editors['spinner'].create = function(container, editParm) {
	            preTab = "tab_"+(parseInt(editParm['rowindex']) + 1) +"_"+ (parseInt(editParm["column"]['columnindex']));
	            var column = editParm.column;
	            var input = $("<input type='text'/>");
	            container.append(input);
	            input.css({
	                border: '#6E90BE'
	            });
	            var options = {
	                type: column.editor.type == 'float' ? 'float' : 'int'
	            };
	            //todo add by ttaomeng
	            var ext = column.editor.p || column.editor.ext;
	            if (ext) {
	                var tmp = typeof(ext) == 'function' ? ext(editParm.record, editParm.rowindex, editParm.value, column) : ext;
	                $.extend(options, tmp);
	            }
	            if (column.editor.minValue != undefined) options.minValue = column.editor.minValue;
	            if (column.editor.maxValue != undefined) options.maxValue = column.editor.maxValue;
	            if (column.editor.disBlank != undefined) options.disBlank = column.editor.disBlank;
	            if (column.editor.decimalplace != undefined) options.decimalplace = column.editor.decimalplace;
	            input.ligerSpinner(options); //input.focus();
	            return input;
	        }
			//end

		
		if(pageStatus=="detail"){
			tbar=[{text: "关闭", icon: "cancel", click: function(){api.close();}}];
 		}else{
 			tbar=[
 	         		{text: "保存", icon: "save", click: save},
 	         		//{text: "提交", icon: "save", click: saveTj},
 					{text: "关闭", icon: "cancel", click: function(){api.close();}}];
 		}
		if(pageStatus=='modify'){
			$("#xzGysmc").ligerComboBox().setDisabled(true);
		}
		
		$("#form1").initForm({
			success: function (response) {//处理成功
	    		if (response.success) {
	            	top.$.dialog.notice({
	             		content: "保存成功！"
	            	});
	         		api.data.window.Qm.refreshGrid();
	            	api.close();
	    		} else {
	           		$.ligerDialog.error('保存失败！<br/>' + response.msg);
	      		}
			}, getSuccess: function (response){
				if(pageStatus=="detail"){
					$("#xzGysmc").html(response.data.goodsAndOrder[0].goods.supplier.gysmc);
					$("#rk_time").html(response.data.goodsAndOrder[0].goods.rk_time.substring(0,10));
				}else{
					$("#xzGysmc").ligerGetTextBoxManager().setValue(response.data.goodsAndOrder[0].goods.supplier.id);
					$("#rk_time").val(response.data.goodsAndOrder[0].goods.rk_time.substring(0,10));
				}
				$("#gysId").val(response.data.goodsAndOrder[0].goods.supplier.id);
				if (response.attachs != null && response.attachs != undefined)
					showAttachFile(response.attachs);
			},
			showToolbar: true,
            toolbarPosition: "bottom",
            toolbar: tbar
    	});
		
		
		
		

		init();
		query();
		callUnit("<%=uu.getOrg().getId()%>","<%=uu.getOrg().getOrgName()%>");

	});

	function save(){
		if(rows.length!=0){//删除
			$.ajax({
                url: "com/tjy2/warehousings/deleteWarehoesingByGoodsId.do",
                type: "POST",
                async: false, 
                data:{"ids":rows.toString()},
                success : function(data, stats) {
                }
			})
		}
		 if ($("#form1").validate().form()) {
			 var formData = $("#form1").getValues();
			 var lb=grid.getData();
			 formData.goods=lb;
			 
			 var tj=true;
			 for(var j =0;j<lb.length;j++){
				var wpmc=lb[j].wpmc;
				var je=lb[j].je;
				if(lb[j].se==''){
					lb[j].se=0.00;
				}
				if(wpmc==''||je==''){
					tj=false;  
				}
			  }
			 if(tj){
				 if(lb.length==0){
					 $.ligerDialog.error('提示：请填写物资信息！');
				 }else{
					 var instruction=$.ligerui.toJSON(formData);
	 	 			 $("body").mask("正在保存数据，请稍后！");
	 				 $.ajax({
	 	                 url: "com/tjy2/warehousings/saveBean.do",
	 	                 type: "POST",
	 	                 async: false, 
	 	                 data:{"warehousing":instruction},
	 	                 success : function(data, stats) {
	 	 					$("body").unmask();
	 	 					if (data["success"]) {
	 	 						top.$.dialog.notice({
	 	 							content : '保存成功'
	 	 						});
	 	 						api.data.window.Qm.refreshGrid();
	 	 						api.close();
	 	 					} else {
	 	 						$.ligerDialog.error('提示：' + data.message);
	 	 					}
	 	 				},
	 	                 error : function(data) {
	 	                     $("body").unmask();
	 	                     $.ligerDialog.error('保存数据失败！');
	 	                 }
	 	             });
				 }
			 }else{
				 $.ligerDialog.error('提示：产品名称、金额请填写完整！');
			 }
		 }
	}
	//四舍五入
	 function decimal(num,v){
	    	var vv = Math.pow(10,v);
	    	return Math.round(num*vv)/vv;
	    	}
	
    function init(){
    	var typeName=<u:dict sql='<%=typeSql%>'/>;
		var wpmc = api.data.wpmc;//父窗口的数据
		var ggxh = api.data.ggxh;//父窗口的数据
     	   column =[
					{ display: '物资表ID', name: 'id',type: 'text',hide:true},
					{ display: '创建时间', name: 'create_time',type: 'text',hide:true},
					{ display: '创建人id', name: 'create_user_id',type: 'text',hide:true},
					{ display: '创建人', name: 'create_user_name',type: 'text',hide:true},
					{ display: '创建部门id', name: 'create_org_id',type: 'text',hide:true},
					{ display: '创建部门', name: 'create_org_name',type: 'text',hide:true},
					{ display: '创建单位Id', name: 'create_unit_id',type: 'text',hide:true},
					{ display: '创建单位', name: 'create_unit_name',type: 'text',hide:true},
					{ display: '库存数量', name: 'sl',type: 'text',hide:true},
					
 		 			{ display: '产品名称', name: 'wpmc',align: 'center', width: 178,
						editor: { type: 'text',ligerui: {autocomplete: {data: wpmc,option:{matchContains:true}}} },totalSummary:{ render: function (e){return "合计" }}},
 		 			{ display: '供应商', name: 'gysmc',align: 'center', width: 180,type: 'text',hide:true,editor: { type:'text'}},
 		 			{ display: '供应商ID', name: 'fk_gys_id',align: 'center', width: 180,type: 'text',hide:true},
 		 			{display: '类型',width: '80',name: 'lx_id',type:'text',required:true,
 						editor : {type : 'select', data: typeName ,ext:{emptyOption:false}},
 						 render: function (item) {
 				                return render(item["lx_id"],typeName);
 					}},
 		 			{ display: '规格及型号', name: 'ggjxh',align: 'center', width: 180,
 						editor: { type: 'text',ligerui: {autocomplete: {data: ggxh,option:{matchContains:true}}} }},
 		 			{ display: '单位', name: 'dw',align: 'center', width: 80,editor: { type: 'text' }},
 		 			{ display: '数量', name: 'cssl',align: 'center', width: 80,editor: { type: 'int',minValue:'1' }},
 		 			{ display: '单价', name: 'je',align: 'center', width: 80,type:'spinner', editor: { type: 'float',minValue:'0',ext:{value:"0"} }},
 		 			
 		 			{ display: '金额', name: 'zje',align: 'center', width: 80,type: 'float',
 		 				render:function(e){
 		 				var zje=parseFloat(e.je)*parseFloat(e.cssl);
 		 				return decimal(zje,2)}
 		 				,totalSummary:{render: function (e){ 
 		 					return "<div id='aaa'>"+decimal( e.sum,2)+"</div>" }}
 		 			},
					
 		 			{ display: '发票号',name:'fph',align:'center',width:100,editor:{type:'text'},validate:{required:true} },
 		 			{ display: '备注', name: 'bzs',align: 'center', width: 180,editor: { type: 'text' }}
 		 			
 				]
               if("${param.pageStatus}"!="detail"){
            	   column.unshift({display: "<a class='l-a iconfont l-icon-add' href='javascript:addDevice();'><span>增加</span></a>",
						isSort: false, 
						width: '30',
						render : function(item, index) {
							return "<a class='l-a l-icon-del' href='javascript:delDevice(grid," + index + ")'><span>删除</span></a>";
						}
					});
              }
    		 grid = $("#checkGrid").ligerGrid({
             columns:column, 
             data:{Rows:[]},
             rownumbers:true,
             frozenRownumbers: false,
             usePager: false,
             enabledEdit: '${param.pageStatus}'=='detail'?false:true,
		     clickToEdit: true,
		     rownumbers: true, 
		     unSetValidateAttr:false,
		     onBeforeEdit: f_onBeforeEdit,
		     onAfterEdit:f_onAfterEdit,
             height:'90%'
 	  	});
     	  
    }
    var edit=false;
    function f_onBeforeEdit(e){
    	if(e.record.id!=""){
    		$.ajax({
    			url:"com/tjy2/warehousings/sfOut.do",
    			type:"POST",
    			async:false,
    			data:{"goodsId":e.record.id},
    			success:function(data){
    				if(data.success){
    					edit= !data.ck;
    				}
    				}
    			})
    	}else{edit=true;}
    	
        return edit;
    }
		function f_onAfterEdit(e){
			var data=grid.getData();
			var zje=parseFloat(e.record.je)+parseFloat(e.record.cssj);
			grid.updateCell('zje', decimal(zje,2), e.record); 
			var sum=0;
			for(var i=0; i<data.length;i++){
				var je=data[i].je==''? 0:data[i].je;
				sum=sum+decimal(parseFloat(je)*data[i].cssl,2);
			}
			$("#aaa").html(decimal(sum,2));//更新汇总

	    	console.log(e);
	    	console.log(edit);
	    	if(edit){
	    		grid.updateCell('sl', e.record.cssl, e.record); 
	    	}
	    	
		}
	//选择人员后回调函数
	 var f_rows='';
	 function delDevice(row,index){
		 var rowData= grid.getSelectedRow();
  		$.ajax({
			url:"com/tjy2/warehousings/sfOut.do",
			type:"POST",
			data:{"goodsId":rowData.id},
			success:function(data){
				if(rowData.id!="" && data.ck){
					$.ligerDialog.alert('提示：'+rowData.wpmc+"已经出库，不可以删除！");
				}else{
					row.deleteSelectedRow();
					if(pageStatus=="modify" ){
			  			//预删除数据
			  			rows.push(rowData.id);
			  		 }
					var data=grid.getData();
					var sum=0;
					for(var i=0; i<data.length;i++){
						if(data[i].id==rowData.id){
							data.splice(i,1);
						}
					}
					//刷新合计
					$("#aaa").html(decimal(sum,2));//更新汇总
				}
			}
		 });
	 }
	 function getGysId(val,text){
		 $("#gysId").val(val);
		 //查询供应商类型
		 $.ajax({
			url:"com/tjy2/supplier/getbean.do",
			type:"POST",
			data:{"id":val},
			success:function(data){
				$("#gysType").val(data.data.zycpjfw);
			}
		 });
	 }

		function addDevice() {
			var gysId=$("#gysId").val();
			var gys=$("#xzGysmc").val();
			var gysType=$("#gysType").val();
			if(gysId==null || gysId==""){
				$.ligerDialog.error('提示：请先选择供应商！');
				//return false;
			}else{
				var addRow = { 
						id:'',
						create_time:'',
						create_user_id:'',
						create_user_name:'',
						create_org_id:'',
						create_org_name:'',
						create_unit_name:'',
						create_unit_id:'',
						sl:'',
						wpmc:'',
						gysmc:gys,
						fk_gys_id:gysId,
						lx_id:gysType,
						ggjxh:'',
						dw:'',
						dj:0,
						cssl:1,
						je:'',
						se:'',
						fph:'',
	<%-- 					sybm:"<%=curUser.getDepartment().getOrgName()%>", --%>
	<%-- 					sybm_id:"<%=curUser.getDepartment().getId()%>", --%>
//	 					rk_time:'',
						bzs:''
					};
				grid.addRow(addRow);
			}
		}
		
		function callUnit(id,text){
			$("#lxrbm").val(text);
			$("#lxrbm_id").val(id);
		}
		
		var types='';
		//选择联系人
		function selectUser(type){
			var org_id = $("#lxrbm_id").val();
			var org_name = $("#lxrbm").val();
			types=type;
			winOpen({
                width : 200,
                height : 420,
                lock : true,
                title : "选择联系人",
                content : 'url:app/supplies/warehousing/choose_user_list.jsp?org_id='+org_id,
                data : {
                    "window" : window
                }
            });
		}

		
		function callUser(id, name){
			if(types==1){
				$('#lxrmc_id').val(id);
				$('#lxrmc').val(name);
				$.ajax({
					url:"com/tjy2/warehousings/getUserTel.do",
					type:"POST",
					data:{"userId":id},
					success:function(data){
						$("#dh").val(data.data);
					}
				 });
			}else if(types==2){
				$("#bmfzr").val(name);
			}else if(types==3){
				$("#jbr").val(name);
			}else if(types==4){
				$("#cg").val(name);
			}else if(types==5){
				$("#kg").val(name);
			}
			
		}
		function query(){
			$.ajax({
                url: "com/tjy2/warehousings/detailGoods.do",
                type: "POST",
                data:{"id":'${param.id}'},
                success : function(data, stats) {
                	var gridDataArr=new Array();
                	
                	for(var i=0; i<data.data.length;i++){
                		var rowData=new Object();
                		rowData.id=data.data[i].goods.id;
                		rowData.create_time=data.data[i].goods.create_time;
                		rowData.create_user_id=data.data[i].goods.create_user_id;
                		rowData.create_user_name=data.data[i].goods.create_user_name;
                		rowData.create_org_name=data.data[i].goods.create_org_name;
                		rowData.create_org_id=data.data[i].goods.create_org_id;
                		rowData.create_unit_id=data.data[i].goods.create_unit_id;
                		rowData.create_unit_name=data.data[i].goods.create_unit_name;
                		rowData.sl=data.data[i].goods.sl;
                		
                		rowData.wpmc=data.data[i].goods.wpmc;
                		rowData.gysmc=data.data[i].goods.gysmc;
                		if(data.data[i].goods.supplier!=null){
                    		rowData.fk_gys_id=data.data[i].goods.supplier.id;
                		}
                		if(data.data[i].goods.goodstype!=null){
                			rowData.lx_id=data.data[i].goods.goodstype.id;//data.data[i].lx_id;
                		}
                		
                		rowData.ggjxh=data.data[i].goods.ggjxh;
                		rowData.dw=data.data[i].goods.dw;
                		rowData.cssl=data.data[i].goods.cssl;
                		rowData.je=data.data[i].goods.je;
                		rowData.zje=data.data[i].goods.je*data.data[i].goods.cssl ;
                		rowData.bzs=data.data[i].bz;
                		rowData.fph=data.data[i].fph;
						
        				gridDataArr.push(rowData);
                	}

        		    if(gridDataArr!=null){
        			   grid.loadData({Rows:gridDataArr});
//        				var sum=0;
//        				for(var i=0; i<gridDataArr.length;i++){
//        					console.log(gridDataArr[i].se);
//        					var se=gridDataArr[i].se==''? 0:gridDataArr[i].se;
//        					sum=sum+decimal(((parseFloat(gridDataArr[i].je)+parseFloat(se))*gridDataArr[i].cssl),2);
//        				}
//        				if(pageStatus=="detail"){
//        					$("#zje").html(decimal(sum,2));
//        				}else{
//        					$("#zje").val(decimal(sum,2));
//        				}
        		    }
        		    $("body").unmask();
				},
                error : function(data) {
                    $("body").unmask();
                    $.ligerDialog.error('保存数据失败！');
                }
            });
			
		}
		

		//start
	    var preTab = null;
	    $(document).keyup(function (e) {
	        if ( e.keyCode == 13 ) {
				chooseNext(e);
	        }
	    });
		var preTab;
	    function chooseNext(event){
	        if(preTab==null){
	            preTab = "tab_1_1";
			}
			var preTabSp = preTab.split("_");
			var rowLength = grid.getData().length;
			var columnsLength = grid.columns.length;
			var target = null;
			var startColumnIndex = (parseInt(preTabSp[2]) + 1);
			var startRowIndex = (parseInt(preTabSp[1]));
	        for (var j = startRowIndex; j <= rowLength; j++) {
	            for (var i = startColumnIndex; i <= columnsLength; i++) {
	                if ( $("div[data-value=" + preTabSp[0] + "_" + j + "_" + i + "]").length == 1 ) {
	                    target = $("div[data-value=" + preTabSp[0] + "_" + j + "_" + i + "]")[0];
	                    break;
	                }
	            }
	            if(target!=null){
	                break;
				}else{
	                //一行循环完毕没有发现匹配的div，则跳转到下一行时，重置startColumnIndex为1;
	                startColumnIndex=1;
				}
	        }
	        if ( target == null ) {
	            target = $("div[data-type=tab]")[0];
	        }
			$(target).trigger("click.grid");
		}
		//end
</script>

</head>
<body>


    <form id="form1" getAction="com/tjy2/warehousings/detail.do?id=${param.id}">
     <input type="hidden" name="id"/>
     <input type="hidden" name="create_user_id"/>
     <input type="hidden" name="create_user_name"/>
     <input type="hidden" name="create_org_name"/>
     <input type="hidden" name="create_org_id"/>
     <input type="hidden" name="create_time"/>
     <input type="hidden" name="warehousing_num"/>
     <input type="hidden" id="gysType"/>
     <input type="hidden" name="state" /> 
     <input type="hidden" name="fybxd_id" /> 
     <input type="hidden" name="bz_zt" /> 
     
     <table border="1" cellpadding="3" cellspacing="0" width="" class="l-detail-table">
    		<tr>
       		<td class="l-t-td-left"></td>
       		<td class="l-t-td-right"></td>
       		<td class="l-t-td-left"></td>
       		<td class="l-t-td-right"></td>
       		<td class="l-t-td-left"></td>
       		<td class="l-t-td-right"></td>
       		</tr>
       		
       <tr>
       		<td class="l-t-td-left">收货单位：</td>
       		<td class="l-t-td-right" colspan="5">
       		<input name="shdw" value="四川省特种设备检验研究院" ligerUi="{disabled:true}" id="shdw" type="text" ltype='text' readonly="true" validate="{maxlength:200}" /></td>
       		
       </tr>
       <tr>
       		<td class="l-t-td-left">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
       		<td class="l-t-td-right" colspan="5"><input name="dz" id="dz" value="成都市东风路二段北二巷4号" type="text" ltype='text' ligerUi="{disabled:true}" validate="{maxlength:200}" /></td>
       </tr>
       <tr>
       		<td class="l-t-td-left">联系人部门：</td>
       		<td class="l-t-td-right">
       		<input name="lxrbm_id" id="lxrbm_id" type="hidden" />
       		<input  readonly="readonly" title="点击此处选择业务部门" name="lxrbm" id="lxrbm"
					ltype='text' validate="{required:true}"  ligerUi="{disabled:true}" />
       		</td>
       		<td class="l-t-td-left">联系人姓名：</td>
       		<td class="l-t-td-right">
       		<input  name="lxrmc_id" id="lxrmc_id" type="hidden"/>
       		
<!-- 						$("#formObj").setValues(carApply); -->
       		<input name="lxrmc" id="lxrmc" ltype="text"
			validate="{required:true}" ligerui="{value:'',iconItems:[{icon:'add',click:function(){selectUser(1)}}]}" onclick="selectUser(1)"/>
       		
       		</td>
       		<td class="l-t-td-left">电话：</td>
       		<td class="l-t-td-right"><input name="dh" id="dh"  ltype="text" value=""/></td>
       </tr>
       <tr>
       		<td class="l-t-td-left">供应商：</td>
       		<td class="l-t-td-right">
       		<input id="gysId" type="hidden" />
       		<input  name="xzGysmc" id="xzGysmc" validate="{required:true}" ltype='select' 
       		ligerui="{onSelected:getGysId,
       		data:<u:dict code='' sql='<%=sql%>'/>}"/>
       		</td>
       		<td class="l-t-td-left">入库时间：</td>
       		<td class="l-t-td-right">
       		<input  name="rk_time" id="rk_time"  value="<%=rkTime %>" ltype='date'  ligerui="{format:'yyyy-MM-dd'}"  />
       		</td>
       		<td class="l-t-td-left">经办人：</td>
       		<td class="l-t-td-right">
       		<input  name="jbr" id="jbr"  value="<%=uu.getName() %>" onclick="selectUser(3)" ltype='text'/>
       		</td>
       </tr>
       <tr>
       		<td class="l-t-td-left">部门负责人：</td>
       		<td class="l-t-td-right"><input name="bmfzr" onclick="selectUser(2)" ltype="text" id="bmfzr" value=""/></td>
       		<td class="l-t-td-left">采购人：</td>
       		<td class="l-t-td-right">
       		<input  name="cg" id="cg" value="<%=uu.getName() %>" onclick="selectUser(4)"  ltype='text'/>
       		</td>
       		<td class="l-t-td-left">库管人：</td>
       		<td class="l-t-td-right">
       		<input name="kg" id="kg" value="<%=uu.getName() %>" onclick="selectUser(5)" ltype='text'/></td>
       </tr>
       <tr>
<!--        		<td class="l-t-td-left">验收人：</td> -->
<!--        		<td class="l-t-td-right"> -->
<!--        		<input  name="ys" id="ys" ltype='text'/> -->
<!--        		</td> -->
       		
       </tr>
       
      </table>
      
	<fieldset class="l-fieldset" >
			<legend class="l-legend">
				<div>物资信息</div>
			</legend>
			
			
<!-- 			<table id="table2" cellpadding="3" cellspacing="0" class="l-detail-table"> -->
<!-- 			     <tr> -->
<!-- 			     <td  class="l-t-td-title">产品名称</td> -->
<!-- 				 <td  class="l-t-td-title">供应商</td> -->
<!-- 				 <td class="l-t-td-title">类型</td> -->
<!-- 				 <td class=l-t-td-title>规格及型号</td> -->
<!-- 				 <td class=l-t-td-title>单位</td> -->
<!-- 				 <td class=l-t-td-title>数量</td> -->
<!-- 				 <td class=l-t-td-title>金额</td> -->
<!-- 				 <td class=l-t-td-title>税额</td> -->
<!-- 				 <td class=l-t-td-title>使用部门</td> -->
<!-- 				 <td class=l-t-td-title>入库时间</td> -->
<!-- 				 <td class=l-t-td-title>备注</td> -->
<!-- 			     </tr> -->
<!-- 				</table> -->
				
			<div    id="checkGrid" ></div>
		</fieldset>	
    </form> 


</body>
</html>
