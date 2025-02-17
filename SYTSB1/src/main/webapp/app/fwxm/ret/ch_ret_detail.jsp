<%@ page import="com.khnt.security.CurrentSessionUser" %>
<%@ page import="com.khnt.security.util.SecurityUtil" %><%--
  Created by IntelliJ IDEA.
  User: songxuemao
  Date: 16/5/25
  Time: 19:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
    CurrentSessionUser user = SecurityUtil.getSecurityUser();
%>
<html>
<head pageStatus="${param.status}">
    <title></title>
    <%@ include file="/k/kui-base-form.jsp" %>
    <script type="text/javascript" src="pub/selectUser/selectUnitOrUser.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#form1").initForm({
                toolbar: [
                    {
                        text: "保存", icon: "save", click: function () {
                            submitForm();
                        }
                    },
                    {
                        text: "取消", icon: "cancel", click: function () {
                            api.close();
                        }
                    }
                ],
                getSuccess: function (resp) {
                    if (resp.success) {
                        var Rows = [];

                        for (var i = 0; i < resp.data.goods.length; i++) {
                            Rows.push({
                                id: resp.data.goods[i]["goods"]["id"],
                                wpmc: resp.data.goods[i]["goods"]["wpmc"],
                                ggjxh: resp.data.goods[i]["goods"]["ggjxh"],
                                warehousing_num: resp.data.goods[i]["goods"]["warehousing_num"],
                                je: resp.data.goods[i]["goods"]["je"],
                                se: resp.data.goods[i]["goods"]["se"],
                                sl: resp.data.goods[i]["sl"],
                                bz: resp.data.goods[i]["bz"]
                            })
                            ids.push(resp.data.goods[i]["goods"]["id"]);
                        }
                        if (mainGrid != null) {
                            mainGrid.loadData({Rows: Rows});
                        }
                    } else {
                        $.ligerDialog.error(resp.msg);
                    }
                }
            });
            initGrid();
        });
        var mainGrid;

        function initGrid() {
            var canEdit = '${param.status}' != 'detail';
            var columns = [{display: "id", name: "id", hide: true, align: "left"},
                {display: "存货名称", name: "wpmc", width: 150, align: "center"},
                {
                    display: "规格及型号", name: "ggjxh", width: 200, align: "center"
                },
                {display: "入库单号", name: "warehousing_num", width: 150, align: "left"},
                {display: "单价", name: "je", width: 80, align: "right"},
                {
                    display: "数量",
                    name: "sl",
                    width: 50,
                    align: "right",
                    type: 'int',
                    editor: {type: 'spinner', minValue: '1'},
                    min: 1,
                    required: true,
                    maxlength: 200
                }, {display: "备注", name: "bz", width: '10%', align: "left", editor: {type: 'text'}, maxlength: 2000}
            ];
            if ('detail' != '${param.status}') {
                columns.push({
                    display: "<a class='l-a iconfont l-icon-add' href='javascript: chooseGoods();'><span>增加</span></a>",
                    width: '50',
                    isSort: false,
                    align: "center",
                    render: function (item, index) {
                        return '<a class="l-a l-icon-del" href="javascript:deleteMainGridRow()"><span>删除</span></a>';
                    }
                });
            }
            mainGrid = $("#goodsGrid").ligerGrid({
                columns: columns,
                enabledEdit: canEdit,
                rownumbers: true,
                frozenRownumbers: false,
                colDraggable: false,
                rowDraggable: false,
                isScroll: true,
                usePager: false,
                data: {Rows: []}
            });
        }

        function chooseGoods() {
            var ckId = $("#ckId").val();
            if (ckId == '' || typeof (ckId) == 'undefined') {
                $.ligerDialog.error("请选择出库单");
                return;
            }
            winOpen({
                lock: true,
                title: "选择需退还的存货",
                content: 'url:app/fwxm/ret/ch_ret_choose_ck_detail.jsp?status=modify&id=' + ckId,
                data: {
                    "window": window,
                    callback: addToMainGrid
                }
            }).max();
        }

        var ids = [];

        function addToMainGrid(datas) {
            if (datas["ckd"]["id"] != $("#ckId").val()) {
                ids = [];
                mainGrid.loadData({Rows: []});
                $("#ckId").val(datas["ckd"]["id"]);
                $("#ckBh").val(datas["ckd"]["ckdbh"]);
                $("#trOrgName").val(datas["ckd"]["lqbm"]);
                $("#trName").val(datas["ckd"]["lqr"]);
            }
            var dataRow = datas["Rows"];
            for (var i = 0; i < dataRow.length; i++) {
                //去重添加
                if ($.inArray(dataRow[i]["id"], ids) < 0) {
                    mainGrid.addRow(dataRow[i]);
                    ids.push(dataRow[i]["id"]);
                }
            }
        }

        function deleteMainGridRow() {
            $.ligerDialog.confirm("确定删除该数据？", function (yes) {
                if (yes) {
                    var data = mainGrid.getData();
                    var row = mainGrid.getSelectedRow();
                    $("#" + row["__id"]).remove(); //移除对应的区域
                    data.splice(row["__index"], 1);
                    ids.splice($.inArray(row["id"], ids), 1);
                    var data1 = {
                        Rows: data
                    };
                    if (mainGrid != null) {
                        mainGrid.loadData(data1);
                    }
                }
            });
        }

        function submitForm() {
            if (!$("#form1").validate().form()) {
                return;
            }
            var formDatas = $("#form1").getValues();
            var goodsDatas = mainGrid.getData();
            if (goodsDatas.length <= 0) {
                $.ligerDialog.error("请选择存货");
                return;
            }
            $("body").mask("正在保存请稍后...");
            var goods = [];
            for (var i = 0; i < goodsDatas.length; i++) {
                goods.push({
                    goods: goodsDatas[i],
                    sl: goodsDatas[i]["sl"],
                    fk_goods_id: goodsDatas[i]["id"],
                    bz: goodsDatas[i]["bz"],
                })
            }
            formDatas["goods"] = goods;
            $.ajax({
                url: "chreturn/savechtrbyck.do",
                data: JSON.stringify(formDatas),
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                type: "post",
                async: true,
                success: function (data) {
                    $("body").unmask("");
                    if (data.success) {
                        api.data.window.Qm.refreshGrid();
                        api.close();
                    } else {
                        $.ligerDialog.error(data.msg);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $("body").unmask("");
                    $.ligerDialog.error("网络出现问题，请联系管理员或刷新后再试");
                }
            });
        }

        function chooseCK() {
            winOpen({
                width: 1000,
                height: 800,
                lock: true,
                title: "",
                content: 'url:app/fwxm/ret/ch_ret_choose_ck.jsp',
                data: {
                    "window": window,
                    callback: addToMainGrid
                }
            });
        }
    </script>
    <style type="text/css" id="sStyle">
        .l-grid-row-cell-inner {
            height: auto !important;
            max-height: 88px !important;
            white-space: normal !important;
        }

        .l-grid-row-cell .l-grid-row-cell-edited {
            height: auto !important;
            max-height: 88px !important;
            white-space: normal !important;
            word-wrap: break-word;
        }
    </style>
</head>
<body>
<form name="form1" id="form1" method="post"
      getAction="chreturn/detail.do?id=${param.id}">
    <fieldset class="l-fieldset">
        <legend class="l-legend">
            <div>退还单</div>
        </legend>
        <table class="l-detail-table">
            <input id="ckId" type="hidden" name="ckId" value=""/>
            <input type="hidden" name="id"/>
            <input type="hidden" name="trId" value=""/>
            <input type="hidden" name="trOrgId" value=""/>
            <input type="hidden" name="trUnitId" value=""/>
            <input type="hidden" name="status"/>
            <tr>
                <td class="l-t-td-left">出库单：</td>
                <td class="l-t-td-right"><input id="ckBh" name="ckBh" type="text" ltype="text"
                                                ligerui="{iconItems:[{icon:'org',click:chooseCK}],readonly:true}"/></td>
                <td class="l-t-td-left"></td>
                <td class="l-t-td-right"></td>
            </tr>
            <tr>
                <td class="l-t-td-left">退还部门：</td>
                <td class="l-t-td-right"><input id="trOrgName" name="trOrgName" type="text" ltype="text"
                                                value=""/></td>
                <td class="l-t-td-left">退还人：</td>
                <td class="l-t-td-right"><input id="trName" name="trName" type="text" ltype="text"
                                                value=""/>
                </td>
            </tr>
            <tr>
                <td class="l-t-td-left">退还原因：</td>
                <td class="l-t-td-right" colspan="3"><textarea rows="3" id="trRemark" name="trRemark" type="text"
                                                               ltype="text" validate="{maxlength:1000}"
                                                               ligerui="{disabled:false}"></textarea></td>
            </tr>
        </table>
    </fieldset>
    <fieldset class="l-fieldset">
        <legend class="l-legend">
            <div>退还物品</div>
        </legend>
        <div id="goodsGrid"></div>
    </fieldset>
</form>
</body>
</html>