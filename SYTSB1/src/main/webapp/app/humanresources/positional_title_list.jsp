<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date" %>
<%
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String nowTime = "";
    nowTime = dateFormat.format(new Date());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <%@include file="/k/kui-base-list.jsp" %>
    <script type="text/javascript">
        var qmUserConfig = {
            sp_fields: [
                {name: "position_title_type", compare: "like", columWidth: 0.33},
                {name: "position_title_start_date", compare: "like", columWidth: 0.33},
                {name: "signed_man", compare: "like", columWidth: 0.33}
            ],
            tbar: [{
                text: '新增', id: 'new', icon: 'add', click: add
            }, {
                text: '详情', id: 'detail', icon: 'detail', click: detail
            }, {
                text: '继续聘用', id: 'renew', icon: 'add', click: renew
            }, {
                text: '结束聘用', id: 'modify', icon: 'modify', click: modify
            }, {
                text: '删除', id: 'del', icon: 'del', click: del
            }, {
                text: '提醒设置', id: 'setting', icon: 'setting', click: setting
            }
                <sec:authorize access="hasRole('sys_administrate')">
                , {
                    text: '发送提醒', id: 'send', icon: 'outbox', click: send
                }
                </sec:authorize>
            ],

            listeners: {
                rowClick: function (rowData, rowIndex) {

                },
                rowDblClick: function (rowData, rowIndex) {
                    detail();
                },
                selectionChange: function (rowData, rowIndex) {

                    var count = Qm.getSelectedCount();
                    Qm.setTbar({
                        detail: count == 1,
                        renew: Qm.getValueByName("termination") == "生效中" && count == 1,
                        modify: Qm.getValueByName("termination") == "生效中" && count == 1,
                        del: count > 0,
                        settings: count > 1
                    });
                },
                rowAttrRender: function (rowData, rowid) {
                    // var war = rowData.waring;
                    var war = rowData.expire;
                    var fontColor = "black";
                    // 数据状态（2：已录入报告）
                    if(war < 0) {
                        fontColor = "red";
                    }
                    if (war < 60 && war >= 0) {
                        fontColor = "orange";
                    }
                    if(rowData.termination==='终止') {
                        fontColor = 'grey';
                    }

                    return "style='color:" + fontColor + "'";
                }
            }
        }


        function add() {
            top.$.dialog({
                width: 900,
                height: 500,
                lock: true,
                parent: api,
                data: {
                    window: window
                },
                title: "新增",
                content: 'url:app/humanresources/positional_title_detail.jsp?pageStatus=add&id=' + Qm.getValueByName("fk_employee_id") + "&positionTitle=positionTitle&renew="
            });
        }

        function setting() {
            top.$.dialog({
                width: 500,
                height: 280,
                lock: true,
                parent: api,
                data: {
                    window: window
                },
                title: "聘用提醒设置",
                content: 'url:app/humanresources/employee_position_title_setting.jsp?pageStatus=modify&&ids=' + Qm.getValuesByName("id")
            });

        }

        function renew() {
            top.$.dialog({
                width: 900,
                height: 500,
                lock: true,
                parent: api,
                data: {
                    window: window
                },
                title: "详情",
                content: 'url:app/humanresources/positional_title_detail.jsp?pageStatus=modify&id=' + Qm.getValueByName("fk_employee_id") + "&positionTitle=positionTitle&renew=renew"
            });

        }


        function send() {
            $.ajax({
                url: "positionTitleRemindAction/sendPositionTitleRemind.do",
                type: "POST",
                datatype: "json",
                contentType: "application/json; charset=utf-8",
                success: function (resp) {
                    if (resp.success) {
                        top.$.dialog.notice({content: '消息发送成功！'});
                    } else {
                        $.ligerDialog.error('消息发送失败！');
                    }
                },
                error: function (data) {
                    $.ligerDialog.alert("消息发送失败！");
                }
            });
        }

        function modify() {
            var id = Qm.getValueByName("id");
            $.ligerDialog.confirm('是否终止聘用？', function (yes) {
                if (!yes) {
                    return false;
                }
                top.$.ajax({
                    url: "positionTitleAction/terminationPositionTitle.do?id=" + id,
                    type: "GET",
                    dataType: 'json',
                    async: false,
                    success: function (data) {
                        if (data.success) {
                            $.ligerDialog.success("终止成功！");
                            Qm.refreshGrid();//刷新
                        } else {
                            $.ligerDialog.warn(data.msg);
                        }
                    },
                    error: function () {
                        $.ligerDialog.warn("终止失败！");
                    }
                });
            });
        }

        function del() {
            $.del("确定删除?",
                "positionTitleAction/delete.do",
                {"ids": Qm.getValuesByName("id").toString()});
        }

        function detail() {
            top.$.dialog({
                width: 900,
                height: 500,
                lock: true,
                parent: api,
                data: {
                    window: window
                },
                title: "详情",
                content: 'url:app/humanresources/positional_title_detail.jsp?pageStatus=detail&id=' + Qm.getValueByName("fk_employee_id") + "&positionTitle=positionTitle"
            });
        }
    </script>

</head>
<body>
<div class="item-tm" id="titleElement">
    <div class="l-page-title">
        <div class="l-page-title-note">提示：列表数据项
            <font color="orange">“橙色”</font>代表60天内聘用到期的数据，
            <font color="red">“红色”</font>代表已经超出聘用期的数据。
            <font color="#808080">“灰色”</font>代表已经终止的聘用数据。
        </div>
    </div>
</div>
<qm:qm pageid="TJY2_RL_POSITION_T">
    <%--<qm:param name="termination" value="0" compare="="/>--%>
</qm:qm>
</body>
</html>