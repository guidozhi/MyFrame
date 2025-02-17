﻿<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head pageStatus="${param.status}">
<title>通用数据导入导出业务模版配置</title>
<%@ include file="/k/kui-base-form.jsp"%>
<script type="text/javascript" src="pub/fileupload/js/fileupload.js"></script>
<script type="text/javascript">
	var columnGrid;
	var pageFileUploaderRuntime=null;
	$(function() {
		$(".tab").ligerTab({
			height:$(window).height()-50
		});
		$("#formObj").initForm({
			getSuccess: function(response){
				if(response.success){
					if(response.data.busConfigInfo){
						var bcfg = $.parseJSON(response.data.busConfigInfo);
						if(bcfg.split)
							$("#split").ligerGetRadioGroupManager().setValue(bcfg.split);
						if(bcfg.sheetAt)
							$("#sheetAt").ligerGetTextBoxManager().setValue(bcfg.sheetAt);
					}
					columnGrid.loadData({Rows:response.data.columns});
					if(response.data.tplId){
		                $("#uploader_container").hide();
						createFileView(response.data.tplId,"下载模板文件",${param.status=='edit'},"uploaded_list",false,function(fid){
		                    $("#tplId").val("");
		                    $("#uploader_container").show();
		                    createUploadRuntime();
		                });
					}else{
						createUploadRuntime();
					}
				}
			},
			toolbar: [{
				text: "生成数据列",
				icon: "communication",
				click: generateColumns
			},{
                text: "保存",
                icon: "save",
                click: saveCfg
            },{
				text: "取消",
				icon: "close",
				click: function(){
					api.close();
				}
			}]<c:if test="${param.status=='add'}">,
			afterParse:function(){
				createUploadRuntime();
			}</c:if>
		});
		
		var requireds = <u:dict code="sys_sf"/>;
		var dateTypes = <u:dict code="pub_expimp_data_type"/>;
        var bindTypes = [{id:"none",text:"不绑定"},{id:"json",text:"JSON字符串"},{id:"code",text:"数据字典"}];
		
		columnGrid = $("#column_grid").ligerGrid({
			columns : [ 
				{display:'主键',name:'id',hide:true},
                {display:'顺序',name:'order',align:'center',width:40,editor:{type:"text"}}, 
				{display:'列号',name:'column',align:'center',width:40,isSort: true,editor:{type:"int"},type:"int"}, 
				{display:'字段名',name:'field',align:'left',width:120,isSort: false,editor:{type:"text"}},
				{display:'列名',name:'name',align:'left',width:130,isSort: false,editor:{type:"text"}},
				{display:'数据类型',name:'types',align:"left",width:120,isSort: false,editor:{type:"select",data:dateTypes,ext:{emptyOption:false}},
					render: function(rowdata, rowindex, value){
						for (var i in dateTypes) {
							if (dateTypes[i]["id"] == rowdata["types"])
								return dateTypes[i]['text'];
						}
						return rowdata["types"];
					}
				},
				{display:'长度',name:'maxLength',align:'right',width:50,isSort: false,editor:{type:"int"}},
				{display:'绑定类型',name:'bindType',align:'left',width:80,isSort: false,editor:{type:"select",emptyOption:false,data:bindTypes,ext:{emptyOption:false}},
					render: function(rowdata, rowindex, value){
                        for (var i in bindTypes) {
                            if (bindTypes[i]["id"] == rowdata["bindType"])
                                return bindTypes[i]['text'];
                        }
                        return rowdata["bindType"];
                    }},
				{display:'绑定数据',name:'bindData',align:'left',width:120,isSort: false,editor:{type:"text"}},
				{display:'格式化',name:'format',align:'left',width:110,isSort: false,editor:{type:"text"}},
                {display:'备注',name:'remark',align:'left',width:100,isSort: false,editor:{type:"text"}},
				{display:'是否必需',name:'required',align:'center',width:60,isSort: false,editor:{type:"select",data:requireds,ext:{emptyOption:false}},
					render: function(rowdata, rowindex, value){
						for (var i in requireds) {
							if (requireds[i]["id"] == rowdata["required"])
								return requireds[i]['text'];
						}
						return rowdata["required"];
					}
				},
				{
					display: "<a class='l-a l-icon-add' href='javascript:columnGrid.addEditRow();' title='增加'><span>增加</span></a>", 
					isSort: false, 
					width: 40, 
					render: function (rowdata, rowindex, value) {
			            var h = "<a class='l-a l-icon-del' href='javascript:columnGrid.deleteSelectedRow();' title='删除'><span>删除</span></a>";
			            return h;
		            }
		        }
			],
			usePager: false,
			height: $("div[tabid='sub_tab']").height(),
			data:{Rows:[]},
			enabledEdit: ${param.status!='detail'}
		});
		$(window).bind("resize",function(){
			columnGrid._onResize();
		});
	});
	
	function createUploadRuntime(){
		pageFileUploaderRuntime = new KHFileUploader({
            fileSize : "50mb",
            container : "uploader_container",
            buttonId : "upload_btn",
            title : "*",
            extName : "*",
            fileNum : 1,
            callback : function(files) {
            	pageFileUploaderRuntime.destroy();
                $("#uploader_container").hide();
                $("#tplId").val(files[0].id);
                createFileView(files[0].id,files[0].name,true,"uploaded_list",false,function(fid){
                    $("#tplId").val("");
                    $("#uploader_container").show();
                    createUploadRuntime();
                });
            }
        });
	}
	
	function generateColumns(){
		var wrapper = $("#wrapper-txt").ligerGetComboBoxManager().getValue();
        var content;
		if(wrapper=="sql")
		    content = $("#expSql").val();
		else
			content = $("#classTable").val();
		if(content==""){
			$.ligerDialog.warn((wrapper=="sql"?"SQL":(wrapper=="bean"?"JavaBean Class":"数据库表名"))+"必须填写！");
			return;
		}
		$.getJSON("pub/expimp/cfg/generateColumns.do",{type:wrapper,content:content},function(response){
			if(response.success)
			    columnGrid.loadData({Rows:response.data});
			else
                $.ligerDialog.error("操作失败！<br/>" + response.msg);
		});
	}
	
	function deleteColumn(){
		var rd = columnGrid.getSelectedRow();
		if(rd.id){
			$.getJSON("pub/expimp/cfg/deleteColumn.do?id=" + rd.id,function(resp){
				if(resp.success)
					columnGrid.deleteSelectedRow();
				else
					$.ligerDialog.error("删除失败，请稍后重试！");
			});
		}
		else
			columnGrid.deleteSelectedRow();
	}
	
	function setUcgItem(cfgItem){
		if(cfgItem){
			var cis = cfgItem.split(",");
			$.each(cis,function(){
				$("#" + this + "_c")[0].checked = true;
			});
		}
	}
	
	function getUcgItem(types){
		var cfgItem = "";
		$(".ucg-item :checkbox").each(function(){
			if(this.checked) cfgItem += this.value + ",";
		});
		if(types=="csv"){
			if($("#split_c").attr("checked"))cfgItem += "split,";
		}
		else if(types=="excel"){
			if($("#sheetAt_c").attr("checked"))cfgItem += "sheetAt,";
		}
		return cfgItem.replace(/,$/,"");
	}
	
	function saveCfg(){
		var formData = $("#formObj").getValues();
		var cfgItem = getUcgItem(formData.sourceType);
		//formData["configItem"] = cfgItem;
		formData["busConfigInfo"] = '{"split":"' + formData.split + '","sheetAt":"' + formData.sheetAt + '"}';
		formData["columns"] = columnGrid.getData();
		$("body").mask("正在保存数据！");
		$.ajax({
            url: "pub/expimp/cfg/saveConfig.do",
            type: "POST",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: $.ligerui.toJSON(formData),
            success: function (resp, stats) {
            	$("body").unmask();
            	if(resp.success){
    				top.$.notice("操作成功！",3);
    				api.data.Qm.refreshGrid();
    				api.close();
    			}else{
    				$.ligerDialog.error("保存失败！<br/>" + resp.msg);
    			}
            },
            error: function (data) {
                $.ligerDialog.error('保存失败！<br/>' + data.msg);
        		$("body").unmask();
            }
        });
	}
	
	function changeUserConfig(i){
		if(i==0){
			$("#user_config_item").hide();
		}else{
			$("#user_config_item").show();
		}
	}
	
	function wrapperChange(t){
		var wrapper = $("#sourceType-txt").ligerGetComboBoxManager().getValue();
		if(wrapper=="buss"){
			$("#db_class_name").hide();
			$("#data_wrapper_").hide();
			return;
		}
		
		if(t=="map"){
			$("#db_class_name").hide();
            $("#sql_exp").hide();
		}else if(t=="bean"){
			$("#db_class_name").show();
            $("#sql_exp").hide();
			$("#db_class_name td:first").text("JavaBean Class：");
		}else if(t=="sql"){
            $("#db_class_name").hide();
            $("#sql_exp").show();
		}else{
			$("#db_class_name").show();
            $("#sql_exp").hide();
			$("#db_class_name td:first").text("数据库表名：");
		}
	}
	
	function sourceTypeChange(t){
		if(t=="buss"){
			$("#db_class_name").hide();
			$("#data_wrapper_").hide();
			$("#text_split").hide();
			$("#excel_sheet").hide();
			$(".col_row_").hide();
			$("#user_config").hide();
		}else if(t=="excel"){
			$("#text_split").hide();
			$("#excel_sheet").show();
			$("#data_wrapper_").show();
			$(".col_row_").show();
			$("#user_config").show();
		}else if(t=="csv"){
			$("#text_split").show();
			$("#excel_sheet").hide();
			$("#data_wrapper_").show();
			$(".col_row_").show();
			$("#user_config").show();
		}
	}
</script>
</head>
<body>
	<form name="formObj" id="formObj" method="post" action="pub/expimp/cfg/saveConfig.do"
		getAction="pub/expimp/cfg/detail.do?id=${param.id}">
		<input name="id" type="hidden" value="${param.id}" />
		<input name="tplId" type="hidden" value="" id="tplId" />
		<div class="tab">
			<div tabid="main_tab" title="基础信息">
				<table cellpadding="3" class="l-detail-table">
					<tr>
						<td class="l-t-td-left" style="width: 150px">编号：</td>
						<td class="l-t-td-right"><input name="code" type="text" ltype='text'
							validate="{required:true,maxlength:32}" /></td>
						<td class="l-t-td-left" style="width: 100px">名称：</td>
						<td class="l-t-td-right"><input name="name" type="text" ltype='text'
							validate="{required:true,maxlength:100}" /></td>
					</tr>
					<tr>
						<td class="l-t-td-left">数据导入导出方式：</td>
						<td class="l-t-td-right"><u:combo name="sourceType"
								attribute="onSelected:sourceTypeChange,emptyOption:false" code="pub_expimp_src"
								validate="required:true" /></td>
                        <td class="l-t-td-left">业务处理接口：</td>
                        <td class="l-t-td-right"><input name="handleBean" type="text" ltype='text'
                            validate="{maxlength:255}" /></td>
                    </tr>
					<tr id="excel_sheet" style="display: none">
						<td class="l-t-td-left">Excel工作表序号：</td>
						<td class="l-t-td-right"><input name="sheetAt" id="sheetAt" type="text" ligerui="{initValue:1,suffix:'表示要导入的数据在第几个工作表',suffixWidth:200}" value="0"
							ltype='number' validate="{maxlength:2}" /></td>
					</tr>
					<tr id="text_split" style="display: none">
						<td class="l-t-td-left">文本分隔符号：</td>
						<td class="l-t-td-right"><input name="split" id="split" type="radio"
							ligerui="{initValue:'0',data:[{id:',',text:'逗号(半角)'},{id:'t',text:'制表符'}]}" ltype="radioGroup" /></td>
					</tr>
					<tr id="data_wrapper_" style="display: none">
						<td class="l-t-td-left">数据包装方式：</td>
						<td class="l-t-td-right"><u:combo name="wrapper"
								attribute="onSelected:wrapperChange,emptyOption:false" code="pub_expimp_wrap" validate="required:true" /></td>
					</tr>
					<tr class="col_row_" style="display: none">
						<td class="l-t-td-left">数据起始行号：</td>
						<td class="l-t-td-right"><input name="rowBegin" type="text" ligerui="{initValue:2,suffix:'从第几行开始导数据',suffixWidth:200}"
							ltype='number' validate="{required:true,maxlength:5}" /></td>
						<td class="l-t-td-left">数据结束行号：</td>
						<td class="l-t-td-right"><input name="rowEnd" type="text" ligerui="{initValue:-1,suffix:'-1表示最后一行',suffixWidth:200}" 
							ltype='number' validate="{required:true,maxlength:5}" /></td>
					</tr>
					<tr class="col_row_" style="display: none">
						<td class="l-t-td-left">数据起始列号：</td>
						<td class="l-t-td-right"><input name="colBegin" type="text" ligerui="{initValue:1,suffix:'从第几列开始导数据',suffixWidth:200}"
							ltype='number' validate="{required:true,maxlength:3}" /></td>
						<td class="l-t-td-left">数据结束列号：</td>
						<td class="l-t-td-right"><input name="colEnd" type="text" ligerui="{initValue:-1,suffix:'-1表示最后一列',suffixWidth:200}"
							ltype='number' validate="{required:true,maxlength:3}" /></td>
					</tr>
					<tr id="db_class_name" style="display: none">
						<td class="l-t-td-left">数据库表或者JavaBeanClass名称：</td>
						<td class="l-t-td-right"><input id="classTable" name="classTable" type="text" ltype='text'
							validate="{maxlength:255}" /></td>
					</tr>
					<tr id="sql_exp" style="display: none">
                        <td class="l-t-td-left">SQL：</td>
                        <td class="l-t-td-right" colspan="3"><textarea id="expSql" name="expSql" type="text" ltype='textarea' rows="5"
                            validate="{maxlength:3000}"></textarea></td>
                    </tr>
					<tr id="user_config" style="display: none">
						<td class="l-t-td-left">是否允许用户自定义：</td>
						<td class="l-t-td-right"><input name="userConfig" type="radio"
							ligerui="{initValue:'0',data:[{id:'1',text:'是'},{id:'0',text:'否'}],onChange:changeUserConfig}"
							value="0" ltype="radioGroup" /></td>
                        <td class="l-t-td-left">是否分批处理：</td>
                        <td class="l-t-td-right">
                        	<u:combo name="batch" ltype="radioGroup" code="sys_sf" attribute="initValue:'0'" />
                        </td>
					</tr>
					<tr id="user_config_item" style="display: none">
                        <td class="l-t-td-left">可自定义选项：</td>
                        <td class="l-t-td-right" colspan="3">
                            <input type="checkBox" name="configItem" ltype="checkboxGroup" 
                                ligerui="{data:[{id:'rowBegin_c',text:'起始行'},{id:'rowEnd_c',text:'结束行'},{id:'colBegin_c',text:'起始列'},{id:'colEnd_c',text:'结束列'},{id:'split_c',text:'文本分隔符'},{id:'sheetAt_c',text:'Excel工作表'}]}" />
                        </td>
                    </tr>
					<tr>
					<tr>
                        <td class="l-t-td-left">模板文件路径：</td>
						<td class="l-t-td-right"><input name="tplPath" type="text" ltype='text' validate="{maxlength:1024}" /></td>
                        <td class="l-t-td-left">模板文件上传：</td>
                        <td class="l-t-td-right">
                        	<div id="uploader_container">
                                <input type="button" class="l-button3" id="upload_btn" style="float: left;" value="+" />
                            </div>
                            <div class="l-upload-ok-list">
                                <ul id="uploaded_list"></ul>
                            </div>
                        </td>
                    </tr>
				</table>
			</div>
			<div tabid="sub_tab" title="数据列信息">
				<div id="column_grid"></div>
			</div>
		</div>
	</form>
</body>
</html>