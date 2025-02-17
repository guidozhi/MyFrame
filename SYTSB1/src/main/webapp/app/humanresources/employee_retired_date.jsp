<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date" %>
<%
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String nowTime = "";
    nowTime = dateFormat.format(new Date());
    String sql = "select t.*,to_char(sysdate, 'yyyy')-substr(emp_id_card, 7, 4) as age from TJY2_RL_EMPLOYEE_BASE t" +
            " where (t.retired_warn_except = '0' or t.work_title_warn_except is null) and (((emp_sex = '1' and to_char(add_months(sysdate, -708), 'yyyy') = " +
            "   substr(emp_id_card, 7, 4)) " +
            " or (emp_sex = '0' and to_char(add_months(sysdate, -762), 'yyyy') = " +
            "    substr(emp_id_card, 7, 4)) " +
            "  and to_char(sysdate, 'mm')-9 >= substr(emp_birthday, 0, 2)) " +
            "  or " +
            "  ((emp_sex = '1' and to_char(add_months(sysdate, -720), 'yyyy') = " +
            "      substr(emp_id_card, 7, 4)) " +
            "   or (emp_sex = '0' and to_char(add_months(sysdate, -780), 'yyyy') = " +
            "      substr(emp_id_card, 7, 4)) " +
            " and to_char(sysdate, 'mm')+3 >= substr(emp_birthday, 0, 2))" +
            " or( (emp_sex = '1' and to_char(add_months(sysdate, -720), 'yyyy')  >" +
            "  substr(emp_id_card, 7, 4)) " +
            " or (emp_sex = '0' and to_char(add_months(sysdate, -780), 'yyyy') > " +
            "substr(emp_id_card, 7, 4))))";
    String sql1 = "select T.*, trunc(RETIRED_DATE - sysdate) DAYS,to_char(sysdate, 'yyyy') - substr(EMP_ID_CARD, 7, 4) as AGE\n" +
            "  from (\n" +
            "         select base.*,\n" +
            "                case\n" +
            "                  when POSITION = '1' then\n" +
            "                    case\n" +
            "                      when EMP_SEX = '1' then add_months(to_date(substr(EMP_ID_CARD, 7, 8), 'yyyy-mm-dd'), 660)\n" +
            "                      else add_months(to_date(substr(EMP_ID_CARD, 7, 8), 'yyyy-mm-dd'), 600) end\n" +
            "                  else\n" +
            "                    case\n" +
            "                      when EMP_SEX = '1' then add_months(to_date(substr(EMP_ID_CARD, 7, 8), 'yyyy-mm-dd'), 720)\n" +
            "                      else add_months(to_date(substr(EMP_ID_CARD, 7, 8), 'yyyy-mm-dd'), 660) end\n" +
            "                  end RETIRED_DATE\n" +
            "           from TJY2_RL_EMPLOYEE_BASE BASE\n" +
            "          where (BASE.RETIRED_WARN_EXCEPT = '0' or BASE.WORK_TITLE_WARN_EXCEPT is null)\n" +
            "       ) T order by DAYS";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <%@include file="/k/kui-base-list.jsp" %>
    <script type="text/javascript">
        var qmUserConfig = {
            sp_defaults: {columnWidth: 0.3, labelAlign: 'right', labelSeparator: '', labelWidth: 120},
            sp_fields: [
                {name: "emp_name", compare: "like", columWidth: 0.33},
                {name: "emp_sex", compare: "like", columWidth: 0.33},
                {name: "work_department", compare: "=", columWidth: 0.33}
            ],
            tbar: [{
                text: '详情', id: 'detail', icon: 'detail', click: detail
            }
                /* , "-", {
                    text: '新增', id: 'add', icon: 'add', click: add
                }, "-", {
                    text: '修改', id: 'edit', icon: 'modify', click: edit
                }
                , "-", {
                    text: '删除', id: 'positive', icon: 'del', click: positive
                }, "-", {
                    text: '解聘', id: 'dismissal', icon: 'del', click: dismissal
                }*/, "-", {
                    text: '退休', id: 'retired', icon: 'del', click: retired
                }, "-", {
                    text: '例外人员', id: 'except', icon: 'user', click: except
                }
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
                        edit: count == 1,
                        positive: count > 0,
                        dismissal: count == 1,
                        retired: count == 1

                    });
                },
                rowAttrRender: function (rowData, rowid) {
                    // 获取还有多少天退休
                    var days = rowData.days;
                    var fontColor = 'black';
                    console.log(days)
                    if (days < 0) {
                        fontColor = 'red';
                    } else if (days <= 90) {
                        fontColor = 'orange'
                    }
                    return "style='color:" + fontColor + "'";
                }
            }
        }


        function positive() {
            $.del("确定删除?", "employeeBaseAction/deleteBase.do", {
                "ids": Qm.getValuesByName("id").toString()
            });

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
                content: 'url:app/humanresources/registration_datail.jsp?pageStatus=detail&id=' + Qm.getValueByName("id")
            });
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
                title: "新增信息",
                content: 'url:app/humanresources/registration_datail.jsp?pageStatus=add&status=4&permission=formal'
            });

        }

        function dismissal() {
            top.$.dialog({
                width: 500,
                height: 200,
                lock: true,
                parent: api,
                data: {
                    window: window
                },
                title: "解聘员工",
                content: 'url:app/humanresources/employee_remove.jsp?pageStatus=add&id=' + Qm.getValueByName("id") + "&status=0"
            });


        }

        function retired() {
            top.$.dialog({
                width: 500,
                height: 200,
                lock: true,
                parent: api,
                data: {
                    window: window
                },
                title: "退休员工",
                content: 'url:app/humanresources/employee_remove.jsp?pageStatus=add&id=' + Qm.getValueByName("id") + "&status=1"
            });

        }

        function edit() {
            top.$.dialog({
                width: 900,
                height: 500,
                lock: true,
                parent: api,
                data: {
                    window: window
                },
                title: "修改信息",
                content: 'url:app/humanresources/registration_datail.jsp?pageStatus=modify&id=' + Qm.getValueByName("id") + "&status=4&permission=formal&fkEmployee=" + Qm.getValueByName("fk_employee")
            });

        }

        function loadGridData(treeId) {
            if (treeId != null && treeId != "100000") {
                Qm.setCondition([{
                    name: "work_department",
                    compare: "like",
                    value: treeId
                }]);
            } else {
                Qm.setCondition([]);
            }
            Qm.searchData();
        }

        function except() {
            top.$.dialog({
                width: 1000,
                height: 500,
                lock: true,
                parent: api,
                data: {
                    window: window
                },
                title: "例外人员管理",
                content: 'url:app/humanresources/employee_warn_except.jsp?exceptType=retired'
            });

        }
    </script>

</head>
<body>

<div class="item-tm" id="titleElement">
    <div class="l-page-title">
        <div class="l-page-title-note">提示：列表数据项
            <%--<font color="black">“黑色”</font>代表本人已确认，--%>
            <%--<font color="#8E8E8E">“灰色”</font>代表本人未确认。--%>
            <font color="orange">“橙色”</font>代表距退休日期不到3个月。
            <font color="red">“红色”</font>代表已超出退休日期。
        </div>
    </div>
</div>
<qm:qm pageid="TJY2_RL_EMP_RETIRED" sql="<%=sql1 %>">
    <qm:param name="emp_status" value="4" compare="in"/>
    <%--<qm:param name="days" value="0" compare=">"/>--%>
    <qm:param name="days/365" value="3" compare="<"/>
</qm:qm>
</body>
</html>