<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date" %>
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
                            _submitForm();
                        }
                    },
                    {
                        text: "取消", icon: "cancel", click: function () {
                            api.close();
                        }
                    }
                ],
                success: function (resp) {
                    if (resp.success) {

                    } else {
                        $.ligerDialog.error(resp.msg);
                    }
                },
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
                {display: "金额", name: "je", width: 80, align: "right"},
                {display: "税额", name: "se", width: 80, align: "right"},
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
            var orgId = $("#blqbmId").val();
            if (orgId == '') {
                $.ligerDialog.error("请选择存货来源。");
                return;
            }
            ;
            winOpen({
                lock: true,
                title: "选择存货",
                content: 'url:app\\fwxm\\recipients\\ch_lq_choose.jsp?orgId=' + orgId,
                data: {
                    "window": window,
                    callback: addToMainGrid
                }
            }).max();
        }

        var ids = [];

        function addToMainGrid(datas) {
            for (var i = 0; i < datas.length; i++) {
                //去重添加
                if ($.inArray(datas[i]["id"], ids) < 0) {
                    mainGrid.addRow(datas[i]);
                    ids.push(datas[i]["id"]);
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
                    bz: goodsDatas[i]["bz"]
                })
            }
            formDatas["goods"] = goods;
            $.ajax({
                url: "chlq/savechlq.do",
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

        function _submitForm() {
            if ($("#form1").validate().form()) {
                var formDatas = $("#form1").getValues();
                $("body").mask("正在保存请稍后...");
                formDatas["dataFrom"] = '0';//PC端提交
                $.ajax({
                    url: "chlq/usualSave.do?processToType=3",
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
        }

        var blqbmId = "<%=user.getDepartment().getId()%>";//默认初始化保障部
        function chooseOrgDefault() {
            selectUnitOrUser("0", "0", "blqbmId", "blqbm", function (data) {
                if (data.code != "<%=user.getDepartment().getId()%>") {
                    ids = [];
                    if (mainGrid != null) {
                        mainGrid.loadData({Rows: []});
                    }
                }
            });
        }

        var defaultOrg = '<%=user.getDepartment().getId()%>';

        function chooseOrg() {
            selectUnitOrUser("0", "0", "lqOrgId", "lqOrgName", function (data) {
                if (defaultOrg != data["code"]) {
                    $("#lqName").val("");
                    $("#lqId").val("");
                    defaultOrg = data["code"];
                }
            });
        }

        function chooseUser() {
            winOpen({
                width: 200,
                height: 420,
                lock: true,
                title: "选择联系人",
                content: 'url:app/car/choose_user_list.jsp?org_id=' + defaultOrg,
                data: {
                    "window": window
                }
            });
        }

        function callUser(id, text) {
            $("#lqId").val(id);
            $("#lqName").val(text);
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
<form name="form1" id="form1" method="post" action="chlq/savechlq.do"
      getAction="chlq/detail.do?id=${param.id}">
    <fieldset class="l-fieldset">
        <legend class="l-legend">
            <div>领取单</div>
        </legend>
        <table class="l-detail-table">
            <input type="hidden" name="id"/>
            <input type="hidden" name="lqId" value="<%=user.getId()%>"/>
            <input type="hidden" name="lqOrgId" value="<%=user.getDepartment().getId()%>"/>
            <input type="hidden" name="lqUnitId" value="<%=user.getUnit().getId()%>"/>
            <input type="hidden" id="blqbmId" name="blqbmId" value="<%=user.getDepartment().getId()%>"/>
            <input type="hidden" name="status"/>
            <tr>
                <td class="l-t-td-left">领取部门：</td>
                <td class="l-t-td-right"><input id="lqOrgName" name="lqOrgName" type="text" ltype="text"
                                                value="<%=user.getDepartment().getOrgName()%>"
                                                ligerui="{readonly:true,iconItems:[{icon:'org',click:chooseOrg}]}"/>
                </td>
                <td class="l-t-td-left">领取人：</td>
                <td class="l-t-td-right"><input id="lqName" name="lqName" type="text" ltype="text"
                                                value="<%=user.getName()%>"
                                                ligerui="{readonly:true,iconItems:[{icon:'user',click:chooseUser}]}"/>
                </td>
            </tr>
            <tr>
                <td class="l-t-td-left">申请日期：</td>
                <td class="l-t-td-right">
                    <input id="createTime" name="createTime" type="text" ltype="date" validate="{required:true}"
                           ligerui="{format:'yyyy-MM-dd'}"
                           value="<%=new SimpleDateFormat("yyyy-MM-dd").format(new Date())%>"/>
                </td>
                <td class="l-t-td-left">存货来源：</td>
                <td class="l-t-td-right"><input id="blqbm" name="blqbm" type="text" ltype="text"
                                                value="<%=user.getDepartment().getOrgName()%>"
                                                ligerui="{disabled:true}"/></td>
            </tr>
            <tr>
                <td class="l-t-td-left">部门负责人：</td>
                <td class="l-t-td-right"><input name="bmfzr" type="text" ltype="text" ligerui="{maxlength:128}"/>
                </td>
                <td class="l-t-td-left">物品预估总金额：</td>
                <td class="l-t-td-right"><input name="lqzje" type="text" ltype="text"
                                                ligerui="{disabled:true,suffix:'元'}"/>
                </td>
            </tr>
            <tr>
                <td class="l-t-td-left">领取用途：</td>
                <td class="l-t-td-right" colspan="3"><textarea rows="3" id="lqRemark" name="lqRemark" type="text"
                                                               ltype="text" validate="{maxlength:1000}"
                                                               ligerui="{disabled:false}"></textarea></td>
            </tr>
            <tr>
                <td class="l-t-td-left">领取物品：</td>
                <td class="l-t-td-right" colspan="3"><textarea rows="3" id="lqwp" name="lqwp" type="text" ltype="text"
                                                               validate="{maxlength:1000}"
                                                               ligerui="{disabled:false}"></textarea></td>
            </tr>
        </table>
    </fieldset>
    <c:if test="${param.status == 'detail'}">
        <c:if test="${param.isMax=='true'}">
            <fieldset class="l-fieldset">
                <legend class="l-legend">
                    <div>已领取物品</div>
                </legend>
                <div id="goodsGrid"></div>
            </fieldset>
        </c:if>
    </c:if>
</form>
</body>
</html>