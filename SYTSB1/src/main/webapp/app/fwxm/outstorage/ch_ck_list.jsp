<%@ page import="com.khnt.security.CurrentSessionUser" %>
<%@ page import="com.khnt.security.util.SecurityUtil" %><%--
  Created by IntelliJ IDEA.
  User: latiflan
  Date: 2018/10/10
  Time: 14:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
    CurrentSessionUser user = SecurityUtil.getSecurityUser();
%>
<html>
<head>
    <title>出库单列表</title>
    <%@include file="/k/kui-base-list.jsp" %>
    <script type="text/javascript">
        var columns = [
            {
                text: '详情', click: detail, icon: 'detail', id: "detail"
            },
            {
                text: '出库', click: add, icon: 'add', id: "add"
            },
            {
                text: '删除', click: del, icon: 'delete', id: "del"
            },
            {
                text: '按时间导出出库单', click: exp, icon: 'exp', id: "exp"
            },
            {
                text: '导出出库单', click: outexp, icon: 'exp', id: "outexp"
            }];
        var qmUserConfig = {
            sp_fields: [
                {name: "status", compare: "="},
                {name: "ckdbh", compare: "like"},
                {name: "ckyjdh", compare: "like"},
                {name: "lqbm", compare: "like"},
                {name: "lqr", compare: "like"},
                {
                    group: [
                        {name: "create_time", compare: ">=", value: ""},
                        {label: "到", name: "create_time", compare: "<=", value: "", labelAlign: "center", labelWidth: 20}
                    ]
                }
            ],
            tbar: columns,
            listeners: {
                rowClick: function (rowData, rowIndex) {

                },
                rowDblClick: function (rowData, rowIndex) {

                },
                selectionChange: function (rowData, rowIndex) {
                    var count = Qm.getSelectedCount();
                    var status = Qm.getValueByName("status");
                    var ckdbh = Qm.getValueByName("ckdbh");
                    var state=Qm.getValuesByName("status");
                    Qm.setTbar({
                        detail: count == 1 && status != '未出库',
                        add: count == 1 && ckdbh == '',
                        edit: count == 1 && status == 0,
                        del: count == 1 && status == '未出库',
                        outexp : count > 0 && $.inArray("未出库", state)==-1
                    });
                }
            },
            rowAttrRender: function (rowData, rowid) {
            	var fontColor="black";
				 if(rowData.status=='已报销'){
					 fontColor="green";
				 }else if(rowData.status=='已出库'){
					 fontColor='orange';
				 }else if(rowData.status=='报销中'){
					 fontColor="violet";
				 }else if(rowData.status=='报销作废'){
					 fontColor="red";
				 }
				 return "style='color:"+fontColor+"'";
				 
                //return rowData.status == '未出库' ? "style='color: red'" : "style='color: green'";
                return fontColor;
            }
        };

        function del() {
            $.del("确定删除?", "chck/deleteOrder.do", {
                "orderId": Qm.getValuesByName("id").toString(),
                "type":Qm.getValueByName("ckyjtype").toString()
            });
        }

        function edit() {
            var id = Qm.getValueByName("id");
            winOpen({
                lock: true,
                title: "出库单",
                content: 'url:app\\fwxm\\outstorage\\ch_ck_detail.jsp?status=edit&id=' + id,
                data: {
                    "window": window,
                }
            }).max();
        }

        function exp() {
            top.$.dialog({
                width: 600,
                height: 300,
                lock: true,
                title: "导出",
                content: "url:app/fwxm/outstorage/ch_ck_exp.jsp?orgId=<%=user.getDepartment().getId() %>&unitId=<%=user.getUnit().getId() %>"
            });
        }
        
        function outexp(){
            var id = Qm.getValuesByName("id");
            var url="chck/download.do?id="+id+"&orgId=<%=user.getDepartment().getId() %>&unitId=<%=user.getUnit().getId() %>";
            download(url,"post",id);
        }
        function download(url, method, id){
            jQuery('<form action="'+url+'" method="'+(method||'post')+'">' +  // action请求路径及推送方法
                '<input type="text" name="id" value="'+id+'"/>' + // id
                '</form>')
                .appendTo('body').submit().remove();
        }
        function detail() {
            var id = Qm.getValueByName("id");
            var ckyjtype = Qm.getValueByName("ckyjtype");
            var url='url:app\\fwxm\\outstorage\\ch_ck_detail.jsp?status=detail&id=' + id;
//             if(ckyjtype=="TH"){
//             url='url:app\\supplies\\out\\return_ck_detail.jsp?status=detail&id=' + id;
//             }
            winOpen({
                lock: true,
                title: "出库单",
                content: url,
                data: {
                    "window": window,
                }
            }).max();
        }

        function add() {
            var id = Qm.getValueByName("id");
            var ckyjtype = Qm.getValueByName("ckyjtype");
            //var lqtime = Qm.getValueByName("lqtime");
            var url='url:app\\fwxm\\outstorage\\ch_ck_lq_detail.jsp?status=edit&id=' + id
            if(ckyjtype=="TH"){
            	url='url:app\\supplies\\out\\return_ck_detail.jsp?status=edit&id=' + id;
            }
            winOpen({
                lock: true,
                title: "出库单",
                content: url,
                data: {
                    "window": window,
                }
            }).max();
        }
    </script>
</head>

<div class="item-tm" id="titleElement">
    <div class="l-page-title">
        <div class="l-page-title-note">提示：
            <font color="black">“黑色”</font>代表未出库，
            <font color="orange">“橙色”</font>代表已出库，
            <font color="violet">“紫色”</font>代表报销中，
            <font color="green">“绿色”</font>代表已报销，
            <font color="red">“红色”</font>代表报销作废。
        </div>
    </div>
</div>
<body>
<qm:qm pageid="tjy2_ch_ck_list" script="false" singleSelect="false">
</qm:qm>
</body>
</html>
