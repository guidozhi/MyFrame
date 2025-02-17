<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <style type="text/css">
    	.l-icon-tttt {background:url('../../k/kui/images/icons/16/table_save.png') no-repeat center;}
    </style>
    <%@include file="/k/kui-base-list.jsp" %>
    <script type="text/javascript" src="k/kui/frame/form.js"></script>
    <script test="text/javascript">
        var toolbar;
        $(function () {//jQuery页面载入事件
            toolbar=$("#toolbar").ligerToolBar(
            	<sec:authorize ifNotGranted="super_administrate">
					<tbar:toolBar type="ligerToolBar" code="sys_param">
					</tbar:toolBar>
				</sec:authorize>
				<sec:authorize access="hasRole('super_administrate')">
					{
                	items: [
                    "-",
                    {id: "add", icon: "add", text: "新增", click: addNewRow},
                    "-",
                    {id: "edit", icon: "modify", text: "修改",disabled:true, click: editRow},
                    "-",
                    {id: "del", icon: "del", text: "删除",disabled:true,click:del},
                    "->",
                    "-",
                    { icon: "refresh", text: "刷新缓存", click: refreshParam},
                    "-",
                    {text: "生成WEBJS文件", icon:'tttt',click: createWebJs}
                	]
            		}
				</sec:authorize>
            		);
            createGrid();
        });
        function createWebJs() {
            $.ligerDialog.confirm("您确定要生成WEBJS文件吗？", function (yes) {
                if (yes) {
                    save();
                    $.getJSON("pub/sysParam/createWebJs.do", function (data) {
                        if(data.success){
                            top.$.notice('生成成功！');
                        }else{
                            $.ligerDialog.error("生成失败！"+data.msg);
                        }
                    });
                }
            });
        }
        function refreshParam() {
            $.getJSON("pub/sysParam/refresh.do", function (data) {
                if (data.success) {
                    top.$.notice('操作成功！');
                }
            });
        }
        function del() {
            var row = manager.getSelected();
            if (row&&row["id"]) {
                if(row["sys"]=="1"){
                    $.ligerDialog.error("系统参数不能删除！");
                    return;
                }
                $.ligerDialog.confirm("您确定要删除所选数据吗？", function (yes) {
                    if (yes) {
                        $.post("pub/sysParam/delete.do", {ids: row["id"]}, function (data) {
                            manager.deleteSelectedRow();
                        }, "json");
                    }
                });
            }else{
                manager.deleteSelectedRow();
            }
        }

        function validateName(grid) {
            var array=[];
            var data = grid.getData();
            for (var i in data) {
                var value=data[i]["groups"]+data[i]["name"];
                for (var a in array) {
                    if (value == array[a]) {
                        $.ligerDialog.error("参数名称和分组的组合不能重复！");
                        return false;
                    }
                }
                array.push(value);
            }
            return true;
        }
        function save() {
            manager.endEdit();
            manager.submitEdit();
            if (validateName(manager)&&validateGrid(manager)) {
                var addData=manager.getAdded();
                var updateData=manager.getUpdated();
                var data= $.extend(addData,updateData);
                $("body").mask("正在保存！"); 
                $.ajax({
                    url: "pub/sysParam/saveParams.do",
                    type: "POST",
                    datatype: "json",
                    contentType: "application/json; charset=utf-8",
                    data: $.ligerui.toJSON(data),
                    success: function (data, stats) {
                        if (data["success"]) {
                            manager.loadData();
                            top.$.notice('保存成功',3);
                        } else {
                            $.ligerDialog.error('保存失败！' + data.msg);
                        }
                        $("body").unmask();
                    },
                    error: function (data) {
                    	 $("body").unmask();
                        $.ligerDialog.error('保存失败！' + data.msg);
                    }
                });
            }
        }

        var manager;

        function addNewRow() {
            //manager.addEditRow();
           top.$.dialog({
				width: 650,
				height: 450,
				lock: true,
				parent: api,
				data: {
					window: window,grid:manager
				},
				title: "新增参数",
				content: 'url:pub/sysparam/param_detail.jsp?pageStatus=add'
			});
        }
        function editRow() {
            //manager.addEditRow();
           var row = manager.getSelected();
           top.$.dialog({
				width: 650,
				height: 450,
				lock: true,
				parent: api,
				data: {
					window: window,grid:manager
				},
				title: "修改参数",
				content: 'url:pub/sysparam/param_detail.jsp?id='+row["id"]+'&pageStatus=modify'
			});
        }
        function render(value, data) {
            for (var i in data) {
                if (data[i]["id"] == value)
                    return data[i]['text'];
            }
            return value;
        }
        var propTypes = [
            {id: "browser", text: "前台"},
            {id: "server", text: "后台"}
        ];
        var dataTypes = [
            {id: "string", text: "字符串"},
            {id: "number", text: "数字"},
            {id: "boolean", text: "布尔型"},
            {id: "user", text: "用户自定义"}
        ];
        function createGrid() {
            var columns = [
            	{ display: '分组', name: 'groupName', width: '8%', type: 'text',hide :true},
                { display: '参数说明', name: 'remark', width: '20%', align: 'left', maxlength: 500, editor: { type: 'text' }},
                { display: '参数名称', name: 'name', width: '15%', align: 'left', type: 'text', required: true, maxlength: 50, editor: {type: 'text'}},
                { display: '参数值', name: 'value', width: '25%', align: 'left', type: 'text', editor: { type: 'textarea',height:80}},
                { display: '分组', name: 'groups', width: '6%', type: 'text', editor: { type: 'text' }},
                { display: '数据类型', name: 'dataType', width: '8%', type: 'text',required: true,
                    editor: { type: 'select', data: dataTypes},
                    render: function (item) {
                        return render(item["dataType"], dataTypes);
                    }
                },
                { display: '排序', name: 'orders', width: '4%', type: 'text',align:'left', editor: { type: 'text' }},
                { display: '分类', name: 'types', width: '7%',
                    editor: { type: 'select', data: propTypes},
                    render: function (item) {
                        return render(item["types"], propTypes);
                    }
                },
                { display: '系统参数', name: 'sys', width: '', render: function (item) {
                    return (item["sys"] == "1") ? "是" : "否";
                }
                }
            ];
            manager = $("#grid").ligerGrid({
                columns: columns,
                rownumbers: true,
                frozenRownumbers: false,
                isScroll: true,
                usePager: false,
                //enabledEdit: true,
                groupColumnName:'groupName', 
                groupRender: function (groupname,groupdata)
                {
                    return groupname;
                },
                
                onBeforeEdit: function (param) {
                    if(param["record"]["sys"] == "1"){
                        return param["column"]["name"] == "value" || param["column"]["name"] == "orders";
                    }
                    return true;
                },
                rowAttrRender: function (item, rowid) {
                    if (item["sys"] =="1") {
                        return "style='color:"+(item["types"] =="browser"?"blue":"red")+"'";
                    }
                    else
                        return "";
                },
                onSelectRow:function(){
                    toolbar.setEnabled({del:true,edit:true});
                },
                onUnSelectRow:function(){
                    toolbar.setEnabled({del:false,edit:false});
                },
                url: "pub/sysParam/getParams.do"/*,
                height:$("body").height()-$(".item-tm").height()-$(".scroll-tm").height()-50*/
            });
        }
    </script>
</head>
<body>

<div class="item-tm" id="titleElement">
    <div class="l-page-title has-icon has-note">
		<div class="l-page-title-div"></div>
		<div class="l-page-title-text"><h1>系统参数设置</h1></div>
		<div class="l-page-title-note">说明：参数分为前台参数和后台参数，前台参数将生成到静态的js文件，后台参数的使用同配置文件。</div>
		<div class="l-page-title-icon"><img src="k/kui/images/icons/32/setting_tools.png" border="0"></div>
	</div>
</div>

<div class="item-tm">
    <div id="toolbar"></div>
</div>

<div class="scroll-tm"><div id="grid"></div></div>
</body>
</html>